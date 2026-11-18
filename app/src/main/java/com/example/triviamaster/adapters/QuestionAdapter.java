package com.example.triviamaster.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.databinding.ItemQuestionBinding;
import com.example.triviamaster.models.Question;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    public interface OnQuestionActionListener {
        void onEditQuestion(Question question);

        void onDeleteQuestion(Question question);
    }

    private final List<Question> questions;
    private final OnQuestionActionListener listener;

    public QuestionAdapter(List<Question> questions, OnQuestionActionListener listener) {
        this.questions = questions;
        this.listener = listener;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuestionBinding binding;

        QuestionViewHolder(ItemQuestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Question question) {
            binding.tvItemQuestionLevel.setText(binding.getRoot().getContext().getString(
                    R.string.qm_level_item_format, question.getLevelId()));
            binding.tvItemQuestionCategory.setText(question.getCategory());
            binding.tvItemQuestionOrigin.setText(binding.getRoot().getContext().getString(
                    question.isCustom() ? R.string.qm_custom_badge : R.string.qm_default_badge));
            binding.tvItemQuestionText.setText(question.getQuestionText());

            if (question.isWordPuzzle()) {
                binding.tvItemQuestionAnswer.setText(binding.getRoot().getContext().getString(
                        R.string.qm_word_badge, question.getTargetWord()));
                binding.btnEditQuestion.setVisibility(View.GONE);
            } else {
                List<String> opts = question.getOptions();
                String answerText = "";
                if (opts != null && question.getCorrectAnswerIndex() >= 0
                        && question.getCorrectAnswerIndex() < opts.size()) {
                    answerText = opts.get(question.getCorrectAnswerIndex());
                }
                binding.tvItemQuestionAnswer.setText(binding.getRoot().getContext().getString(
                        R.string.qm_correct_preview, answerText));
                binding.btnEditQuestion.setVisibility(View.VISIBLE);
            }

            binding.btnEditQuestion.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditQuestion(question);
                }
            });
            binding.btnDeleteQuestion.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteQuestion(question);
                }
            });
        }
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionViewHolder(ItemQuestionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.bind(questions.get(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
