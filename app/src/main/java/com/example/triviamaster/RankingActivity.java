package com.example.triviamaster;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.adapters.RankingAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.databinding.ActivityRankingBinding;
import com.example.triviamaster.models.RankingItem;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.Nullable;

public class RankingActivity extends AppCompatActivity {
    private ActivityRankingBinding binding;
    private PreferencesManager prefsManager;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityRankingBinding.inflate(getLayoutInflater());
        ActivityRankingBinding activityRankingBinding = this.binding;
        setContentView(activityRankingBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), this::applyWindowInsets);
        this.prefsManager = new PreferencesManager(this);
        setupRankingData();
        setupListeners();
        setupBottomNav();
    }

    private WindowInsetsCompat applyWindowInsets(View view, WindowInsetsCompat insets) {
        Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
        ViewGroup.LayoutParams layoutParams = binding.layoutRankingHeader.getLayoutParams();
        ViewGroup.MarginLayoutParams params = layoutParams instanceof ViewGroup.MarginLayoutParams ? (ViewGroup.MarginLayoutParams) layoutParams : null;
        if (params != null) {
            int minMargin = (int) (56 * getResources().getDisplayMetrics().density);
            params.topMargin = Math.max(
                    statusBarInsets.top + (int) (16 * getResources().getDisplayMetrics().density),
                    minMargin
            );
            binding.layoutRankingHeader.setLayoutParams(params);
        }
        return insets;
    }
    private void setupRankingData() {
        PreferencesManager preferencesManager = this.prefsManager;
        List<RankingItem> rankings = preferencesManager.getRankings();
        if (rankings.isEmpty()) {
            ActivityRankingBinding activityRankingBinding = this.binding;
            activityRankingBinding.layoutPodium.setVisibility(View.GONE);
                        binding.tvCongratsWinner.setText("");
                        binding.tvEmptyRanking.setVisibility(View.VISIBLE);
                        binding.rvRankingList.setVisibility(View.GONE);
            return;
        }
                binding.layoutPodium.setVisibility(View.VISIBLE);
        if (!rankings.isEmpty()) {
            RankingItem r1 = rankings.get(0);
                        binding.tvCongratsWinner.setText(r1.getPlayerName());
                        binding.podiumRank1.setVisibility(View.VISIBLE);
                        binding.ivPodium1Avatar.setImageResource(r1.getAvatarResId());
                        binding.tvPodium1Name.setText(r1.getPlayerName());
                        binding.tvPodium1Score.setText(
                    getString(R.string.ranking_points_format, formatScore(r1.getScore()))
            );
        } else {
                        binding.podiumRank1.setVisibility(View.INVISIBLE);
                        binding.tvCongratsWinner.setText("");
        }
        if (rankings.size() >= 2) {
            RankingItem r2 = rankings.get(1);
                        binding.podiumRank2.setVisibility(View.VISIBLE);
                        binding.ivPodium2Avatar.setImageResource(r2.getAvatarResId());
                        binding.tvPodium2Name.setText(r2.getPlayerName());
                        binding.tvPodium2Score.setText(
                    getString(R.string.ranking_points_format, formatScore(r2.getScore()))
            );
        } else {
                        binding.podiumRank2.setVisibility(View.INVISIBLE);
        }
        if (rankings.size() >= 3) {
            RankingItem r3 = rankings.get(2);
                        binding.podiumRank3.setVisibility(View.VISIBLE);
                        binding.ivPodium3Avatar.setImageResource(r3.getAvatarResId());
                        binding.tvPodium3Name.setText(r3.getPlayerName());
                        binding.tvPodium3Score.setText(
                    getString(R.string.ranking_points_format, formatScore(r3.getScore()))
            );
        } else {
                        binding.podiumRank3.setVisibility(View.INVISIBLE);
        }
                binding.tvEmptyRanking.setVisibility(View.GONE);
                binding.rvRankingList.setVisibility(View.VISIBLE);
        binding.rvRankingList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvRankingList.setAdapter(new RankingAdapter(rankings, 0));
    }

    private String formatScore(int score) {
        return NumberFormat.getNumberInstance(Locale.US).format(score);
    }

    private void setupListeners() {
        binding.btnRankingClose.setOnClickListener(view -> finish());
    }
    private void setupBottomNav() {
        ImageView ivNavMap = (ImageView) findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = (ImageView) findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavProfile = (ImageView) findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavProfile = findViewById(R.id.btnNavProfile);
        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) {
            ivNavMap.setColorFilter(inactiveColor);
        }
        if (ivNavLeaderboard != null) {
            ivNavLeaderboard.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        }
        if (ivNavProfile != null) {
            ivNavProfile.setColorFilter(inactiveColor);
        }
        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(view -> openMap());
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(view -> finish());
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(view -> openProfile());
        }
    }
    private void openMap() {
        Intent intent = new Intent(this, MapActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void openProfile() {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }
}
