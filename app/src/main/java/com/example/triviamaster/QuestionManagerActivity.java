package com.example.triviamaster;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.QuestionAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.DuplicateRecordException;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.databinding.ActivityQuestionManagerBinding;
import com.example.triviamaster.databinding.DialogQuestionFormBinding;
import com.example.triviamaster.models.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class QuestionManagerActivity extends AppCompatActivity {

    private ActivityQuestionManagerBinding binding;
    private QuestionRepository questionRepository;
    private final List<Question> questionList = new ArrayList<>();
    private QuestionAdapter questionAdapter;
    private String currentSearch = "";
    private int currentLevelFilter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!new PreferencesManager(this).isCurrentUserAdmin()) {
            finish();
            return;
        }
        binding = ActivityQuestionManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        questionRepository = new QuestionRepository(this);
        setupLevelFilterSpinner();
        setupRecycler();
        setupListeners();
        setupBottomNav();
        loadQuestions();
    }

    private void setupLevelFilterSpinner() {
        List<String> filterItems = new ArrayList<>();
        filterItems.add(getString(R.string.qm_filter_all_levels));
        for (int i = 1; i <= 16; i++) {
            filterItems.add(getString(R.string.qm_level_item_format, i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, filterItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLevelFilter.setAdapter(adapter);
        binding.spinnerLevelFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentLevelFilter = position;
                loadQuestions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupRecycler() {
        questionAdapter = new QuestionAdapter(questionList, new QuestionAdapter.OnQuestionActionListener() {
            @Override
            public void onEditQuestion(Question question) {
                if (question.isWordPuzzle()) {
                    Toast.makeText(QuestionManagerActivity.this,
                            getString(R.string.qm_word_puzzle_edit_notice), Toast.LENGTH_SHORT).show();
                    return;
                }
                showQuestionFormDialog(question);
            }

            @Override
            public void onDeleteQuestion(Question question) {
                confirmDeleteQuestion(question);
            }
        });
        binding.rvQuestionsList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvQuestionsList.setAdapter(questionAdapter);
    }

    private void setupListeners() {
        binding.btnQuestionManagerBack.setOnClickListener(v -> finish());
        binding.btnAddQuestion.setOnClickListener(v -> showQuestionFormDialog(null));
        binding.etSearchQuestions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearch = s != null ? s.toString() : "";
                loadQuestions();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadQuestions() {
        binding.progressQuestions.setVisibility(View.VISIBLE);
        questionRepository.getAllQuestions(currentSearch, currentLevelFilter, new DbCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> result) {
                if (isFinishing()) {
                    return;
                }
                binding.progressQuestions.setVisibility(View.GONE);
                questionList.clear();
                if (result != null) {
                    questionList.addAll(result);
                }
                questionAdapter.notifyDataSetChanged();
                binding.tvEmptyQuestions.setVisibility(questionList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                binding.progressQuestions.setVisibility(View.GONE);
                Toast.makeText(QuestionManagerActivity.this,
                        getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestionFormDialog(Question existing) {
        DialogQuestionFormBinding formBinding = DialogQuestionFormBinding.inflate(getLayoutInflater());
        AlertDialog dialog = new AlertDialog.Builder(this).setView(formBinding.getRoot()).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        boolean isEdit = existing != null;
        formBinding.tvQuestionFormTitle.setText(getString(
                isEdit ? R.string.qm_dialog_edit_title : R.string.qm_dialog_create_title));

        List<String> levels = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            levels.add(getString(R.string.qm_level_item_format, i));
        }
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, levels);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formBinding.spinnerFormLevel.setAdapter(levelAdapter);

        if (isEdit) {
            int levelPos = Math.max(0, Math.min(15, existing.getLevelId() - 1));
            formBinding.spinnerFormLevel.setSelection(levelPos);
            formBinding.etFormCategory.setText(existing.getCategory());
            formBinding.etFormQuestionText.setText(existing.getQuestionText());
            List<String> opts = existing.getOptions();
            if (opts != null) {
                if (opts.size() > 0) formBinding.etFormOptionA.setText(opts.get(0));
                if (opts.size() > 1) formBinding.etFormOptionB.setText(opts.get(1));
                if (opts.size() > 2) formBinding.etFormOptionC.setText(opts.get(2));
                if (opts.size() > 3) formBinding.etFormOptionD.setText(opts.get(3));
            }
            switch (existing.getCorrectAnswerIndex()) {
                case 1:
                    formBinding.rbOptionB.setChecked(true);
                    break;
                case 2:
                    formBinding.rbOptionC.setChecked(true);
                    break;
                case 3:
                    formBinding.rbOptionD.setChecked(true);
                    break;
                default:
                    formBinding.rbOptionA.setChecked(true);
                    break;
            }
            formBinding.etFormExplanation.setText(existing.getExplanation());
        } else if (currentLevelFilter > 0) {
            formBinding.spinnerFormLevel.setSelection(currentLevelFilter - 1);
        }

        formBinding.btnFormQuestionCancel.setOnClickListener(v -> dialog.dismiss());
        formBinding.btnFormQuestionSave.setOnClickListener(v -> {
            int levelId = formBinding.spinnerFormLevel.getSelectedItemPosition() + 1;
            String category = formBinding.etFormCategory.getText() != null
                    ? formBinding.etFormCategory.getText().toString().trim() : "";
            String text = formBinding.etFormQuestionText.getText() != null
                    ? formBinding.etFormQuestionText.getText().toString().trim() : "";
            String optA = formBinding.etFormOptionA.getText() != null
                    ? formBinding.etFormOptionA.getText().toString().trim() : "";
            String optB = formBinding.etFormOptionB.getText() != null
                    ? formBinding.etFormOptionB.getText().toString().trim() : "";
            String optC = formBinding.etFormOptionC.getText() != null
                    ? formBinding.etFormOptionC.getText().toString().trim() : "";
            String optD = formBinding.etFormOptionD.getText() != null
                    ? formBinding.etFormOptionD.getText().toString().trim() : "";
            String explanation = formBinding.etFormExplanation.getText() != null
                    ? formBinding.etFormExplanation.getText().toString().trim() : "";

            if (category.isEmpty() || text.isEmpty() || optA.isEmpty()
                    || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
                Toast.makeText(QuestionManagerActivity.this,
                        getString(R.string.qm_error_fields_required), Toast.LENGTH_SHORT).show();
                return;
            }

            int correctIdx = 0;
            int checkedId = formBinding.rgCorrectOption.getCheckedRadioButtonId();
            if (checkedId == R.id.rbOptionB) {
                correctIdx = 1;
            } else if (checkedId == R.id.rbOptionC) {
                correctIdx = 2;
            } else if (checkedId == R.id.rbOptionD) {
                correctIdx = 3;
            }

            int questionId = isEdit ? existing.getId() : 0;
            int color = isEdit ? existing.getCategoryColor() : Color.parseColor("#8B5CF6");
            boolean custom = !isEdit || existing.isCustom();
            Question payload = new Question(
                    questionId,
                    levelId,
                    text,
                    category,
                    color,
                    Arrays.asList(optA, optB, optC, optD),
                    correctIdx,
                    explanation,
                    false,
                    "",
                    0,
                    0,
                    custom
            );

            formBinding.btnFormQuestionSave.setEnabled(false);
            if (isEdit) {
                questionRepository.updateQuestion(payload, new DbCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean updated) {
                        if (isFinishing()) {
                            return;
                        }
                        dialog.dismiss();
                        Toast.makeText(QuestionManagerActivity.this,
                                getString(R.string.qm_updated_success), Toast.LENGTH_SHORT).show();
                        loadQuestions();
                    }

                    @Override
                    public void onError(Exception error) {
                        if (isFinishing()) {
                            return;
                        }
                        formBinding.btnFormQuestionSave.setEnabled(true);
                        if (error instanceof DuplicateRecordException) {
                            formBinding.tilFormQuestionText.setError(
                                    getString(R.string.qm_error_question_exists));
                        } else {
                            Toast.makeText(QuestionManagerActivity.this,
                                    getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                questionRepository.insertQuestion(payload, new DbCallback<Long>() {
                    @Override
                    public void onSuccess(Long id) {
                        if (isFinishing()) {
                            return;
                        }
                        dialog.dismiss();
                        Toast.makeText(QuestionManagerActivity.this,
                                getString(R.string.qm_created_success), Toast.LENGTH_SHORT).show();
                        loadQuestions();
                    }

                    @Override
                    public void onError(Exception error) {
                        if (isFinishing()) {
                            return;
                        }
                        formBinding.btnFormQuestionSave.setEnabled(true);
                        if (error instanceof DuplicateRecordException) {
                            formBinding.tilFormQuestionText.setError(
                                    getString(R.string.qm_error_question_exists));
                        } else {
                            Toast.makeText(QuestionManagerActivity.this,
                                    getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    private void confirmDeleteQuestion(Question question) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.qm_delete_confirm_title))
                .setMessage(getString(R.string.qm_delete_confirm_msg))
                .setPositiveButton(getString(R.string.btn_confirm_delete), (dialog, which) ->
                        questionRepository.deleteQuestion(question.getId(), new DbCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean deleted) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(QuestionManagerActivity.this,
                                        getString(R.string.qm_deleted_success), Toast.LENGTH_SHORT).show();
                                loadQuestions();
                            }

                            @Override
                            public void onError(Exception error) {
                                if (isFinishing()) {
                                    return;
                                }
                                Toast.makeText(QuestionManagerActivity.this,
                                        getString(R.string.error_db_generic), Toast.LENGTH_SHORT).show();
                            }
                        }))
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show();
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavSocial = findViewById(R.id.ivNavSocial);
        ImageView ivNavQuests = findViewById(R.id.ivNavQuests);
        ImageView ivNavQuestions = findViewById(R.id.ivNavQuestions);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavSocial = findViewById(R.id.btnNavSocial);
        View btnNavQuests = findViewById(R.id.btnNavQuests);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) ivNavMap.setColorFilter(inactiveColor);
        if (ivNavLeaderboard != null) ivNavLeaderboard.setColorFilter(inactiveColor);
        if (ivNavSocial != null) ivNavSocial.setColorFilter(inactiveColor);
        if (ivNavQuests != null) ivNavQuests.setColorFilter(inactiveColor);
        if (ivNavQuestions != null) ivNavQuestions.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        if (ivNavProfile != null) ivNavProfile.setColorFilter(inactiveColor);

        if (btnNavQuests != null) {
            btnNavQuests.setOnClickListener(v -> {
                startActivity(new Intent(QuestionManagerActivity.this, QuestsActivity.class));
                finish();
            });
        }

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(QuestionManagerActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> {
                startActivity(new Intent(QuestionManagerActivity.this, RankingActivity.class));
                finish();
            });
        }
        if (btnNavSocial != null) {
            btnNavSocial.setOnClickListener(v -> {
                startActivity(new Intent(QuestionManagerActivity.this, SocialActivity.class));
                finish();
            });
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                startActivity(new Intent(QuestionManagerActivity.this, ProfileActivity.class));
                finish();
            });
        }
    }
}
