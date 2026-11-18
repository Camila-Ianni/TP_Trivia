package com.example.triviamaster.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.triviamaster.R;
import com.example.triviamaster.databinding.ItemRankingBinding;
import com.example.triviamaster.models.RankingItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    public interface OnPlayerClickListener {
        void onPlayerClick(RankingItem item);
    }

    private final List<RankingItem> rankingList;
    private final int startIndex;
    private final OnPlayerClickListener clickListener;

    public RankingAdapter(List<RankingItem> rankingList, int startIndex) {
        this(rankingList, startIndex, null);
    }

    public RankingAdapter(List<RankingItem> rankingList, int startIndex, OnPlayerClickListener clickListener) {
        this.rankingList = rankingList;
        this.startIndex = startIndex;
        this.clickListener = clickListener;
    }

    public RankingAdapter(List<RankingItem> rankingList) {
        this(rankingList, 0, null);
    }

    public class RankingViewHolder extends RecyclerView.ViewHolder {
        private final ItemRankingBinding binding;

        RankingViewHolder(ItemRankingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RankingItem item, int position) {
            binding.tvRankPosition.setText(String.valueOf(startIndex + position + 1));
            binding.ivRankingAvatar.setImageResource(item.getAvatarResId());
            binding.tvRankingPlayerName.setText(item.getPlayerName());
            binding.tvRankingDetails.setText("Racha: " + item.getMaxStreak() + " • " + item.getDate());
            String formattedScore = NumberFormat.getNumberInstance(Locale.US).format(item.getScore());
            binding.tvRankingScore.setText(binding.getRoot().getContext().getString(
                    R.string.ranking_score_format, formattedScore));
            binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onPlayerClick(item);
                }
            });
        }
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RankingViewHolder(ItemRankingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(RankingViewHolder holder, int position) {
        holder.bind(rankingList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }
}
