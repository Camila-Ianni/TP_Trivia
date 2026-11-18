package com.example.triviamaster;

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
import com.example.triviamaster.adapters.RankingAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityRankingBinding;
import com.example.triviamaster.models.RankingItem;
import com.example.triviamaster.models.User;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class RankingActivity extends AppCompatActivity {

    private ActivityRankingBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            ViewGroup.LayoutParams layoutParams = binding.layoutRankingHeader.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
                int minMargin = (int) (56 * getResources().getDisplayMetrics().density);
                params.topMargin = Math.max(statusBarInsets.top + (int) (16 * getResources().getDisplayMetrics().density), minMargin);
                binding.layoutRankingHeader.setLayoutParams(params);
            }
            return insets;
        });

        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        setupListeners();
        setupBottomNav();
        loadRankingsFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRankingsFromDb();
    }

    private void loadRankingsFromDb() {
        userRepository.getRankingUsers(new DbCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                if (isFinishing()) {
                    return;
                }
                List<RankingItem> rankings = new ArrayList<>();
                if (users != null && !users.isEmpty()) {
                    for (User user : users) {
                        int avatarRes = prefsManager.getAvatarById(user.getAvatarId()).getIconResId();
                        String date = user.getCreatedAt() != null && !user.getCreatedAt().isEmpty()
                                ? user.getCreatedAt() : "Hoy";
                        rankings.add(new RankingItem(user.getUsername(), avatarRes,
                                user.getHighScore(), user.getMaxStreak(), date));
                    }
                } else {
                    rankings.addAll(prefsManager.getRankings());
                }
                renderRankings(rankings);
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                renderRankings(prefsManager.getRankings());
            }
        });
    }

    private void renderRankings(List<RankingItem> rankings) {
        if (rankings.isEmpty()) {
            binding.layoutPodium.setVisibility(View.GONE);
            binding.tvCongratsWinner.setText("");
            binding.tvEmptyRanking.setVisibility(View.VISIBLE);
            binding.rvRankingList.setVisibility(View.GONE);
            return;
        }

        binding.layoutPodium.setVisibility(View.VISIBLE);

        RankingItem r1 = rankings.get(0);
        binding.tvCongratsWinner.setText(r1.getPlayerName());
        binding.podiumRank1.setVisibility(View.VISIBLE);
        binding.ivPodium1Avatar.setImageResource(r1.getAvatarResId());
        binding.tvPodium1Name.setText(r1.getPlayerName());
        binding.tvPodium1Score.setText(getString(R.string.ranking_score_format, formatScore(r1.getScore())));

        if (rankings.size() >= 2) {
            RankingItem r2 = rankings.get(1);
            binding.podiumRank2.setVisibility(View.VISIBLE);
            binding.ivPodium2Avatar.setImageResource(r2.getAvatarResId());
            binding.tvPodium2Name.setText(r2.getPlayerName());
            binding.tvPodium2Score.setText(getString(R.string.ranking_score_format, formatScore(r2.getScore())));
        } else {
            binding.podiumRank2.setVisibility(View.INVISIBLE);
        }

        if (rankings.size() >= 3) {
            RankingItem r3 = rankings.get(2);
            binding.podiumRank3.setVisibility(View.VISIBLE);
            binding.ivPodium3Avatar.setImageResource(r3.getAvatarResId());
            binding.tvPodium3Name.setText(r3.getPlayerName());
            binding.tvPodium3Score.setText(getString(R.string.ranking_score_format, formatScore(r3.getScore())));
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
        binding.btnRankingClose.setOnClickListener(v -> finish());
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavQuestions = findViewById(R.id.ivNavQuestions);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavQuestions = findViewById(R.id.btnNavQuestions);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) ivNavMap.setColorFilter(inactiveColor);
        if (ivNavLeaderboard != null) ivNavLeaderboard.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        if (ivNavQuestions != null) ivNavQuestions.setColorFilter(inactiveColor);
        if (ivNavProfile != null) ivNavProfile.setColorFilter(inactiveColor);

        boolean hasSession = prefsManager.getCurrentUserId() > 0;
        if (!hasSession) {
            View bottomNav = findViewById(R.id.bottomNav);
            if (bottomNav != null) {
                bottomNav.setVisibility(View.GONE);
            }
            return;
        }

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(RankingActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavQuestions != null) {
            btnNavQuestions.setVisibility(prefsManager.isCurrentUserAdmin() ? View.VISIBLE : View.GONE);
            btnNavQuestions.setOnClickListener(v -> {
                startActivity(new Intent(RankingActivity.this, QuestionManagerActivity.class));
                finish();
            });
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                startActivity(new Intent(RankingActivity.this, ProfileActivity.class));
                finish();
            });
        }
    }
}
