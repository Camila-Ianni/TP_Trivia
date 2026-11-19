package com.example.triviamaster;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.QuestionAdapter;
import com.example.triviamaster.adapters.SocialAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.ClassroomRepository;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.databinding.ActivityClassroomDetailBinding;
import com.example.triviamaster.models.ClassroomMember;
import com.example.triviamaster.models.Friendship;
import com.example.triviamaster.models.Question;
import com.example.triviamaster.models.User;
import com.example.triviamaster.network.GeminiQuestionGenerator;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public final class ClassroomDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CLASSROOM_ID = "extra_classroom_id";
    public static final String EXTRA_CLASSROOM_NAME = "extra_classroom_name";
    public static final String EXTRA_CLASSROOM_CODE = "extra_classroom_code";

    private ActivityClassroomDetailBinding binding;
    private PreferencesManager prefsManager;
    private ClassroomRepository classroomRepository;
    private QuestionRepository questionRepository;
    private GeminiQuestionGenerator geminiGenerator;
    private SocialAdapter studentsAdapter;
    private QuestionAdapter questionAdapter;
    private final List<Question> detailQuestions = new ArrayList<>();

    private long classroomId = -1L;
    private String classroomName = "";
    private String classroomCode = "";

    private Uri pendingPdfUri;
    private TextView activePdfStatusView;

    private final ActivityResultLauncher<String[]> pickPdfLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri != null) {
                    pendingPdfUri = uri;
                    if (activePdfStatusView != null) {
                        activePdfStatusView.setText(R.string.ai_pdf_selected);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassroomDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            ViewGroup.LayoutParams lp = binding.layoutDetailHeader.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.topMargin = statusBarInsets.top + (int) (8 * getResources().getDisplayMetrics().density);
                binding.layoutDetailHeader.setLayoutParams(params);
            }
            return insets;
        });

        classroomId = getIntent().getLongExtra(EXTRA_CLASSROOM_ID, -1L);
        classroomName = getIntent().getStringExtra(EXTRA_CLASSROOM_NAME);
        classroomCode = getIntent().getStringExtra(EXTRA_CLASSROOM_CODE);
        if (classroomId <= 0) {
            finish();
            return;
        }

        prefsManager = new PreferencesManager(this);
        classroomRepository = new ClassroomRepository(this);
        questionRepository = new QuestionRepository(this);
        geminiGenerator = new GeminiQuestionGenerator(this);

        setupUI();
        loadDetails();
    }

    private void setupUI() {
        binding.btnDetailBack.setOnClickListener(v -> finish());
        binding.tvDetailClassroomName.setText(classroomName != null ? classroomName : "");
        binding.tvDetailClassroomCode.setText(classroomCode != null ? classroomCode : "");
        binding.btnDetailAiPdf.setOnClickListener(v -> showGeneratePdfDialog());

        studentsAdapter = new SocialAdapter(SocialAdapter.MODE_SEARCH_USER, prefsManager,
                new SocialAdapter.OnSocialActionListener() {
                    @Override
                    public void onAcceptRequest(Friendship friendship) {
                    }

                    @Override
                    public void onRejectRequest(Friendship friendship) {
                    }

                    @Override
                    public void onChallengeFriend(Friendship friendship) {
                    }

                    @Override
                    public void onAddUser(User user) {
                    }
                });
        binding.rvDetailStudents.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDetailStudents.setAdapter(studentsAdapter);

        questionAdapter = new QuestionAdapter(detailQuestions, new QuestionAdapter.OnQuestionActionListener() {
            @Override
            public void onEditQuestion(Question question) {
            }

            @Override
            public void onDeleteQuestion(Question question) {
                questionRepository.deleteQuestion(question.getId(), new DbCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean ok) {
                        if (!isFinishing()) loadDetails();
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
            }
        });
        binding.rvDetailQuestions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDetailQuestions.setAdapter(questionAdapter);
    }

    private void loadDetails() {
        classroomRepository.getApprovedStudentsForClassroom(classroomId, new DbCallback<List<ClassroomMember>>() {
            @Override
            public void onSuccess(List<ClassroomMember> members) {
                if (isFinishing()) return;
                List<User> mappedUsers = new ArrayList<>();
                java.util.Map<Long, String> statusMap = new java.util.HashMap<>();
                if (members != null) {
                    for (ClassroomMember m : members) {
                        User u = new User(m.getStudentId(), m.getStudentUsername(), "", "",
                                m.getStudentAvatarId(), 0, 0, 1, 12, 0, "", "");
                        mappedUsers.add(u);
                        statusMap.put(m.getStudentId(), "ACCEPTED");
                    }
                }
                studentsAdapter.setSearchUsers(mappedUsers, statusMap);
                binding.tvDetailEmptyStudents.setVisibility(mappedUsers.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception error) {
            }
        });

        questionRepository.getQuestionsByClassroom(classroomId, new DbCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> questions) {
                if (isFinishing()) return;
                int count = questions != null ? questions.size() : 0;
                binding.tvDetailQuestionsHeader.setText(getString(R.string.classroom_questions_title, count));
                detailQuestions.clear();
                if (questions != null) {
                    detailQuestions.addAll(questions);
                }
                questionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }

    private void showGeneratePdfDialog() {
        pendingPdfUri = null;
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_generate_pdf, (ViewGroup) null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        Button btnPickPdf = dialogView.findViewById(R.id.btnPickPdf);
        activePdfStatusView = dialogView.findViewById(R.id.tvSelectedPdfStatus);
        TextInputEditText etPrompt = dialogView.findViewById(R.id.etAiPrompt);
        TextInputEditText etKey = dialogView.findViewById(R.id.etGeminiApiKey);
        ProgressBar progress = dialogView.findViewById(R.id.progressAiGenerating);
        Button btnGenerate = dialogView.findViewById(R.id.btnGenerateWithAi);

        String savedKey = prefsManager.getGeminiApiKey();
        if (savedKey != null && !savedKey.isEmpty()) {
            etKey.setText(savedKey);
        }

        btnPickPdf.setOnClickListener(v -> pickPdfLauncher.launch(new String[]{"application/pdf"}));

        btnGenerate.setOnClickListener(v -> {
            String prompt = etPrompt.getText() != null ? etPrompt.getText().toString().trim() : "";
            String apiKey = etKey.getText() != null ? etKey.getText().toString().trim() : "";
            if (apiKey.isEmpty()) {
                Toast.makeText(this, R.string.ai_error_missing_key, Toast.LENGTH_SHORT).show();
                return;
            }
            if (pendingPdfUri == null || prompt.isEmpty()) {
                Toast.makeText(this, R.string.ai_error_missing_pdf, Toast.LENGTH_SHORT).show();
                return;
            }
            prefsManager.setGeminiApiKey(apiKey);
            progress.setVisibility(View.VISIBLE);
            btnGenerate.setEnabled(false);
            Toast.makeText(this, R.string.ai_generating_msg, Toast.LENGTH_SHORT).show();

            geminiGenerator.generateFromPdf(pendingPdfUri, prompt, apiKey, new DbCallback<List<Question>>() {
                @Override
                public void onSuccess(List<Question> generated) {
                    if (isFinishing()) return;
                    questionRepository.insertClassroomQuestions(classroomId, generated, new DbCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer count) {
                            if (isFinishing()) return;
                            progress.setVisibility(View.GONE);
                            btnGenerate.setEnabled(true);
                            dialog.dismiss();
                            Toast.makeText(ClassroomDetailActivity.this,
                                    getString(R.string.ai_generated_success, count != null ? count : 0),
                                    Toast.LENGTH_LONG).show();
                            loadDetails();
                        }

                        @Override
                        public void onError(Exception error) {
                            if (isFinishing()) return;
                            progress.setVisibility(View.GONE);
                            btnGenerate.setEnabled(true);
                            Toast.makeText(ClassroomDetailActivity.this, R.string.error_db_generic, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception error) {
                    if (isFinishing()) return;
                    progress.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);
                    String msg = error != null && error.getMessage() != null ? error.getMessage() : "Error";
                    Toast.makeText(ClassroomDetailActivity.this,
                            getString(R.string.ai_error_failed, msg),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }
}
