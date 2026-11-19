package com.example.triviamaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.triviamaster.adapters.ClassroomAdapter;
import com.example.triviamaster.adapters.SocialAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.ClassroomRepository;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.databinding.ActivityClassroomBinding;
import com.example.triviamaster.models.Classroom;
import com.example.triviamaster.models.ClassroomMember;
import com.example.triviamaster.models.Friendship;
import com.example.triviamaster.models.Question;
import com.example.triviamaster.models.User;
import com.example.triviamaster.network.GeminiQuestionGenerator;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public final class ClassroomActivity extends AppCompatActivity {

    private ActivityClassroomBinding binding;
    private PreferencesManager prefsManager;
    private ClassroomRepository classroomRepository;
    private QuestionRepository questionRepository;
    private GeminiQuestionGenerator geminiGenerator;
    private ClassroomAdapter classroomAdapter;
    private SocialAdapter pendingMembersAdapter;
    private boolean isTeacherMode;

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
        binding = ActivityClassroomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            ViewGroup.LayoutParams lp = binding.layoutClassroomHeader.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.topMargin = statusBarInsets.top + (int) (8 * getResources().getDisplayMetrics().density);
                binding.layoutClassroomHeader.setLayoutParams(params);
            }
            return insets;
        });

        prefsManager = new PreferencesManager(this);
        classroomRepository = new ClassroomRepository(this);
        questionRepository = new QuestionRepository(this);
        geminiGenerator = new GeminiQuestionGenerator(this);
        isTeacherMode = prefsManager.isCurrentUserTeacher();

        setupUI();
        loadData();
    }

    private void setupUI() {
        binding.btnClassroomBack.setOnClickListener(v -> finish());
        binding.tvClassroomSubtitle.setText(isTeacherMode
                ? R.string.classroom_subtitle_teacher
                : R.string.classroom_subtitle_student);

        if (isTeacherMode) {
            binding.btnCreateClassroom.setVisibility(View.VISIBLE);
            binding.layoutJoinByCode.setVisibility(View.GONE);
            binding.btnCreateClassroom.setOnClickListener(v -> showCreateClassroomDialog());
        } else {
            binding.btnCreateClassroom.setVisibility(View.GONE);
            binding.layoutJoinByCode.setVisibility(View.VISIBLE);
            binding.btnJoinClassroom.setOnClickListener(v -> requestJoinClassroom());
        }

        classroomAdapter = new ClassroomAdapter(isTeacherMode, new ClassroomAdapter.OnClassroomActionListener() {
            @Override
            public void onPlayClassroom(Classroom classroom) {
                if (classroom.getQuestionCount() <= 0) {
                    Toast.makeText(ClassroomActivity.this, R.string.classroom_no_questions, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ClassroomActivity.this, GameActivity.class);
                intent.putExtra("extra_is_quick_play", true);
                intent.putExtra(GameActivity.EXTRA_CLASSROOM_ID, classroom.getId());
                intent.putExtra(GameActivity.EXTRA_CLASSROOM_NAME, classroom.getName());
                startActivity(intent);
            }

            @Override
            public void onOpenAiPdf(Classroom classroom) {
                showGeneratePdfDialog(classroom);
            }
        });
        binding.rvClassrooms.setLayoutManager(new LinearLayoutManager(this));
        binding.rvClassrooms.setAdapter(classroomAdapter);

        pendingMembersAdapter = new SocialAdapter(SocialAdapter.MODE_INCOMING_REQUEST, prefsManager,
                new SocialAdapter.OnSocialActionListener() {
                    @Override
                    public void onAcceptRequest(Friendship friendship) {
                        classroomRepository.approveMember(friendship.getId(), new DbCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean ok) {
                                if (isFinishing()) return;
                                Toast.makeText(ClassroomActivity.this,
                                        getString(R.string.classroom_member_approved, friendship.getOtherUsername()),
                                        Toast.LENGTH_SHORT).show();
                                loadData();
                            }

                            @Override
                            public void onError(Exception error) {
                            }
                        });
                    }

                    @Override
                    public void onRejectRequest(Friendship friendship) {
                        classroomRepository.rejectMember(friendship.getId(), new DbCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean ok) {
                                if (isFinishing()) return;
                                loadData();
                            }

                            @Override
                            public void onError(Exception error) {
                            }
                        });
                    }

                    @Override
                    public void onChallengeFriend(Friendship friendship) {
                    }

                    @Override
                    public void onAddUser(User user) {
                    }
                });
        binding.rvPendingMembers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPendingMembers.setAdapter(pendingMembersAdapter);
    }

    private void loadData() {
        long myId = prefsManager.getCurrentUserId();
        if (myId <= 0) {
            return;
        }
        if (isTeacherMode) {
            classroomRepository.getClassroomsForTeacher(myId, new DbCallback<List<Classroom>>() {
                @Override
                public void onSuccess(List<Classroom> list) {
                    if (isFinishing()) return;
                    classroomAdapter.setItems(list);
                    binding.tvEmptyClassrooms.setText(R.string.classroom_empty_teacher);
                    binding.tvEmptyClassrooms.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onError(Exception error) {
                }
            });

            classroomRepository.getPendingRequestsForTeacher(myId, new DbCallback<List<ClassroomMember>>() {
                @Override
                public void onSuccess(List<ClassroomMember> members) {
                    if (isFinishing()) return;
                    if (members == null || members.isEmpty()) {
                        binding.tvPendingRequestsTitle.setVisibility(View.GONE);
                        binding.rvPendingMembers.setVisibility(View.GONE);
                    } else {
                        List<Friendship> mapped = new ArrayList<>();
                        for (ClassroomMember m : members) {
                            mapped.add(new Friendship(
                                    m.getId(),
                                    m.getStudentId(),
                                    myId,
                                    m.getStudentId(),
                                    m.getStudentUsername() + " (" + m.getClassroomName() + ")",
                                    m.getStudentAvatarId(),
                                    0,
                                    0,
                                    "PENDING",
                                    true
                            ));
                        }
                        binding.tvPendingRequestsTitle.setVisibility(View.VISIBLE);
                        binding.rvPendingMembers.setVisibility(View.VISIBLE);
                        pendingMembersAdapter.setFriendships(mapped);
                    }
                }

                @Override
                public void onError(Exception error) {
                }
            });
        } else {
            classroomRepository.getClassroomsForStudent(myId, new DbCallback<List<Classroom>>() {
                @Override
                public void onSuccess(List<Classroom> list) {
                    if (isFinishing()) return;
                    classroomAdapter.setItems(list);
                    binding.tvEmptyClassrooms.setText(R.string.classroom_empty_student);
                    binding.tvEmptyClassrooms.setVisibility(list == null || list.isEmpty() ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onError(Exception error) {
                }
            });
        }
    }

    private void showCreateClassroomDialog() {
        EditText input = new EditText(this);
        input.setHint(R.string.classroom_hint_name);
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        input.setPadding(pad, pad, pad, pad);

        new AlertDialog.Builder(this)
                .setTitle(R.string.classroom_create_title)
                .setView(input)
                .setPositiveButton(R.string.qm_btn_save, (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) {
                        return;
                    }
                    classroomRepository.createClassroom(prefsManager.getCurrentUserId(), name, new DbCallback<Classroom>() {
                        @Override
                        public void onSuccess(Classroom created) {
                            if (isFinishing() || created == null) return;
                            Toast.makeText(ClassroomActivity.this,
                                    getString(R.string.classroom_created_msg, created.getCode()),
                                    Toast.LENGTH_LONG).show();
                            loadData();
                        }

                        @Override
                        public void onError(Exception error) {
                        }
                    });
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }

    private void requestJoinClassroom() {
        String code = binding.etJoinClassroomCode.getText() != null
                ? binding.etJoinClassroomCode.getText().toString().trim()
                : "";
        if (code.isEmpty()) {
            return;
        }
        classroomRepository.requestJoinByCode(prefsManager.getCurrentUserId(), code, new DbCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (isFinishing()) return;
                if (ClassroomRepository.JOIN_SENT.equals(result)) {
                    binding.etJoinClassroomCode.setText("");
                    Toast.makeText(ClassroomActivity.this, R.string.classroom_join_sent, Toast.LENGTH_SHORT).show();
                    loadData();
                } else if (ClassroomRepository.JOIN_ALREADY_MEMBER.equals(result)) {
                    Toast.makeText(ClassroomActivity.this, R.string.classroom_join_already_member, Toast.LENGTH_SHORT).show();
                } else if (ClassroomRepository.JOIN_ALREADY_PENDING.equals(result)) {
                    Toast.makeText(ClassroomActivity.this, R.string.classroom_join_already_pending, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ClassroomActivity.this, R.string.classroom_join_not_found, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }

    private void showGeneratePdfDialog(Classroom classroom) {
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
                    questionRepository.insertClassroomQuestions(classroom.getId(), generated, new DbCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer count) {
                            if (isFinishing()) return;
                            progress.setVisibility(View.GONE);
                            btnGenerate.setEnabled(true);
                            dialog.dismiss();
                            Toast.makeText(ClassroomActivity.this,
                                    getString(R.string.ai_generated_success, count != null ? count : 0),
                                    Toast.LENGTH_LONG).show();
                            loadData();
                        }

                        @Override
                        public void onError(Exception error) {
                            if (isFinishing()) return;
                            progress.setVisibility(View.GONE);
                            btnGenerate.setEnabled(true);
                            Toast.makeText(ClassroomActivity.this, R.string.error_db_generic, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Exception error) {
                    if (isFinishing()) return;
                    progress.setVisibility(View.GONE);
                    btnGenerate.setEnabled(true);
                    String msg = error != null && error.getMessage() != null ? error.getMessage() : "Error";
                    Toast.makeText(ClassroomActivity.this,
                            getString(R.string.ai_error_failed, msg),
                            Toast.LENGTH_LONG).show();
                }
            });
        });

        dialog.show();
    }
}
