package com.example.triviamaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.QuestAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.MatchHistoryRepository;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityQuestsBinding;
import com.example.triviamaster.models.QuestItem;
import com.example.triviamaster.models.User;
import com.example.triviamaster.utils.RetroFeedbackManager;
import java.util.ArrayList;
import java.util.List;

public final class QuestsActivity extends AppCompatActivity {

    private ActivityQuestsBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private MatchHistoryRepository historyRepository;
    private RetroFeedbackManager feedbackManager;
    private final List<QuestItem> questList = new ArrayList<>();
    private QuestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        historyRepository = new MatchHistoryRepository(this);
        feedbackManager = new RetroFeedbackManager(this);
        setupRecycler();
        binding.btnQuestsBack.setOnClickListener(v -> finish());
        setupBottomNav();
        loadQuests();
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
        View btnNavQuestions = findViewById(R.id.btnNavQuestions);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) ivNavMap.setColorFilter(inactiveColor);
        if (ivNavLeaderboard != null) ivNavLeaderboard.setColorFilter(inactiveColor);
        if (ivNavSocial != null) ivNavSocial.setColorFilter(inactiveColor);
        if (ivNavQuests != null) ivNavQuests.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        if (ivNavQuestions != null) ivNavQuestions.setColorFilter(inactiveColor);
        if (ivNavProfile != null) ivNavProfile.setColorFilter(inactiveColor);

        if (btnNavQuestions != null) {
            btnNavQuestions.setVisibility(prefsManager.isCurrentUserAdmin() ? View.VISIBLE : View.GONE);
            btnNavQuestions.setOnClickListener(v -> {
                startActivity(new Intent(QuestsActivity.this, QuestionManagerActivity.class));
                finish();
            });
        }
        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(QuestsActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> {
                startActivity(new Intent(QuestsActivity.this, RankingActivity.class));
                finish();
            });
        }
        if (btnNavSocial != null) {
            btnNavSocial.setOnClickListener(v -> {
                startActivity(new Intent(QuestsActivity.this, SocialActivity.class));
                finish();
            });
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                startActivity(new Intent(QuestsActivity.this, ProfileActivity.class));
                finish();
            });
        }
    }

    private void setupRecycler() {
        adapter = new QuestAdapter(questList, this::claimQuest);
        binding.rvQuestsList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvQuestsList.setAdapter(adapter);
    }

    private void loadQuests() {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            return;
        }
        binding.progressQuests.setVisibility(View.VISIBLE);
        userRepository.getUserById(userId, new DbCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (isFinishing() || user == null) {
                    binding.progressQuests.setVisibility(View.GONE);
                    return;
                }
                prefsManager.syncFromUser(user);
                binding.tvQuestsCoins.setText(getString(R.string.shop_coins_format, user.getCoins()));
                historyRepository.getQuestProgressMetrics(userId, new DbCallback<int[]>() {
                    @Override
                    public void onSuccess(int[] metrics) {
                        if (isFinishing()) {
                            return;
                        }
                        binding.progressQuests.setVisibility(View.GONE);
                        int totalMatches = metrics != null && metrics.length > 0 ? metrics[0] : 0;
                        int hardWins = metrics != null && metrics.length > 1 ? metrics[1] : 0;
                        buildQuestItems(user, totalMatches, hardWins);
                    }

                    @Override
                    public void onError(Exception error) {
                        if (!isFinishing()) {
                            binding.progressQuests.setVisibility(View.GONE);
                            buildQuestItems(user, 0, 0);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception error) {
                if (!isFinishing()) {
                    binding.progressQuests.setVisibility(View.GONE);
                }
            }
        });
    }

    private void buildQuestItems(User user, int totalMatches, int hardWins) {
        questList.clear();
        questList.add(new QuestItem(
                "q_play_5",
                getString(R.string.quest_1_title),
                getString(R.string.quest_1_desc),
                getString(R.string.quest_1_reward),
                totalMatches, 5, 80, 2, 0, user.hasClaimedQuest("q_play_5")
        ));
        questList.add(new QuestItem(
                "q_streak_5",
                getString(R.string.quest_2_title),
                getString(R.string.quest_2_desc),
                getString(R.string.quest_2_reward),
                user.getMaxStreak(), 5, 100, 0, 1, user.hasClaimedQuest("q_streak_5")
        ));
        questList.add(new QuestItem(
                "q_special_3",
                getString(R.string.quest_3_title),
                getString(R.string.quest_3_desc),
                getString(R.string.quest_3_reward),
                user.getCompletedSpecialCount(), 3, 120, 0, 1, user.hasClaimedQuest("q_special_3")
        ));
        questList.add(new QuestItem(
                "q_hard_win",
                getString(R.string.quest_4_title),
                getString(R.string.quest_4_desc),
                getString(R.string.quest_4_reward),
                hardWins, 1, 150, 3, 0, user.hasClaimedQuest("q_hard_win")
        ));
        questList.add(new QuestItem(
                "q_stars_15",
                getString(R.string.quest_5_title),
                getString(R.string.quest_5_desc),
                getString(R.string.quest_5_reward),
                user.getTotalStars(), 15, 200, 0, 1, user.hasClaimedQuest("q_stars_15")
        ));
        adapter.notifyDataSetChanged();
    }

    private void claimQuest(QuestItem item) {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            return;
        }
        userRepository.claimQuestReward(
                userId,
                item.getId(),
                item.getCoinsBonus(),
                item.getEnergyBonus(),
                item.getChestBonus(),
                new DbCallback<User>() {
                    @Override
                    public void onSuccess(User updated) {
                        if (isFinishing() || updated == null) {
                            return;
                        }
                        prefsManager.syncFromUser(updated);
                        feedbackManager.playVictory();
                        Toast.makeText(QuestsActivity.this,
                                getString(R.string.quests_claimed_msg, item.getCoinsBonus()),
                                Toast.LENGTH_SHORT).show();
                        loadQuests();
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (feedbackManager != null) {
            feedbackManager.release();
        }
    }
}
