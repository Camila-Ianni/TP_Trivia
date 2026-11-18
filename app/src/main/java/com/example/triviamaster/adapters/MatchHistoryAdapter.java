package com.example.triviamaster.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.databinding.ItemMatchHistoryBinding;
import com.example.triviamaster.models.MatchHistoryItem;
import java.util.List;

public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.HistoryViewHolder> {

    public interface OnDeleteHistoryListener {
        void onDeleteItem(MatchHistoryItem item);
    }

    private final List<MatchHistoryItem> items;
    private final OnDeleteHistoryListener listener;

    public MatchHistoryAdapter(List<MatchHistoryItem> items, OnDeleteHistoryListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemMatchHistoryBinding binding;

        HistoryViewHolder(ItemMatchHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MatchHistoryItem item) {
            binding.tvHistoryMode.setText(item.getModeLabel());
            binding.tvHistoryDifficulty.setText(item.getDifficultyName());
            String starsText = item.getStars() + "/3";
            binding.tvHistoryStats.setText(binding.getRoot().getContext().getString(
                    R.string.history_item_stats,
                    item.getCorrectCount(),
                    item.getTotalQuestions(),
                    item.getScore(),
                    starsText));
            binding.tvHistoryDate.setText(item.getPlayedAt());
            binding.btnDeleteHistoryItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteItem(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryViewHolder(ItemMatchHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
