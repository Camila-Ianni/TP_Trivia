package com.example.triviamaster.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
=======
import com.example.triviamaster.R;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
import com.example.triviamaster.databinding.ItemRankingBinding;
import com.example.triviamaster.models.RankingItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {
<<<<<<< HEAD
    private final List<RankingItem> rankingList;
    private final int startIndex;
=======
    private List<RankingItem> rankingList;
    private int startIndex;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

    public RankingAdapter(List<RankingItem> rankingList, int startIndex) {
        this.rankingList = rankingList;
        this.startIndex = startIndex;
    }

    public RankingAdapter(List<RankingItem> rankingList) {
        this(rankingList, 0);
    }

    public class RankingViewHolder extends RecyclerView.ViewHolder {
<<<<<<< HEAD
        private final ItemRankingBinding binding;
=======
        private ItemRankingBinding binding;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

        RankingViewHolder(ItemRankingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RankingItem item, int position) {
            binding.tvRankPosition.setText(String.valueOf(startIndex + position + 1));
            binding.ivRankingAvatar.setImageResource(item.getAvatarResId());
            binding.tvRankingPlayerName.setText(item.getPlayerName());
<<<<<<< HEAD
            binding.tvRankingDetails.setText("Racha: " + item.getMaxStreak() + " • " + item.getDate());
            binding.tvRankingScore.setText(
                    NumberFormat.getNumberInstance(Locale.US).format(item.getScore()) + " Point");
=======
            binding.tvRankingDetails.setText(
                    binding.getRoot().getContext().getString(
                            R.string.ranking_details_format,
                            item.getMaxStreak(),
                            item.getDate()
                    )
            );
            String score = NumberFormat.getNumberInstance(Locale.US).format(item.getScore());
            binding.tvRankingScore.setText(
                    binding.getRoot().getContext().getString(R.string.ranking_points_format, score)
            );
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
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
