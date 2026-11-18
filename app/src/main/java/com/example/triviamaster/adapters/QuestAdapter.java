package com.example.triviamaster.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.databinding.ItemQuestBinding;
import com.example.triviamaster.models.QuestItem;
import java.util.List;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {

    public interface OnClaimQuestListener {
        void onClaim(QuestItem item);
    }

    private final List<QuestItem> quests;
    private final OnClaimQuestListener listener;

    public QuestAdapter(List<QuestItem> quests, OnClaimQuestListener listener) {
        this.quests = quests;
        this.listener = listener;
    }

    public class QuestViewHolder extends RecyclerView.ViewHolder {
        private final ItemQuestBinding binding;

        QuestViewHolder(ItemQuestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(QuestItem item) {
            binding.tvQuestTitle.setText(item.getTitle());
            binding.tvQuestDesc.setText(item.getDescription());
            binding.tvQuestReward.setText(item.getRewardLabel());
            binding.pbQuestProgress.setMax(item.getTargetProgress());
            binding.pbQuestProgress.setProgress(item.getCurrentProgress());
            binding.tvQuestProgress.setText(binding.getRoot().getContext().getString(
                    R.string.quests_progress_format,
                    item.getCurrentProgress(),
                    item.getTargetProgress()));

            if (item.isClaimed()) {
                binding.btnClaimQuest.setText(R.string.quests_btn_claimed);
                binding.btnClaimQuest.setEnabled(false);
                binding.btnClaimQuest.setAlpha(0.55f);
                binding.btnClaimQuest.setBackgroundResource(R.drawable.bg_pill_score_white);
                binding.btnClaimQuest.setTextColor(0xFF4A3E6D);
            } else if (item.isCompleted()) {
                binding.btnClaimQuest.setText(R.string.quests_btn_claim);
                binding.btnClaimQuest.setEnabled(true);
                binding.btnClaimQuest.setAlpha(1.0f);
                binding.btnClaimQuest.setBackgroundResource(R.drawable.bg_btn_pill_coral);
                binding.btnClaimQuest.setTextColor(0xFFFFFFFF);
            } else {
                binding.btnClaimQuest.setText(R.string.quests_btn_in_progress);
                binding.btnClaimQuest.setEnabled(false);
                binding.btnClaimQuest.setAlpha(0.65f);
                binding.btnClaimQuest.setBackgroundResource(R.drawable.bg_pill_score_white);
                binding.btnClaimQuest.setTextColor(0xFF7A7886);
            }

            binding.btnClaimQuest.setOnClickListener(v -> {
                if (listener != null && item.isCompleted() && !item.isClaimed()) {
                    listener.onClaim(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestViewHolder(ItemQuestBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
        holder.bind(quests.get(position));
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }
}
