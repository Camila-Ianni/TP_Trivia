package com.example.triviamaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.UserRepository;
import com.example.triviamaster.databinding.ActivityShopBinding;
import com.example.triviamaster.models.User;
import com.example.triviamaster.utils.AvatarFrameHelper;
import com.example.triviamaster.utils.RetroFeedbackManager;

public final class ShopActivity extends AppCompatActivity {

    private static final int PRICE_ENERGY = 40;
    private static final int PRICE_SHIELD = 60;
    private static final int PRICE_TITLE_HUNTER = 120;
    private static final int PRICE_TITLE_SAGE = 200;
    private static final int PRICE_TITLE_LEGEND = 350;
    private static final int PRICE_FRAME_GOLD = 90;
    private static final int PRICE_FRAME_NEON = 140;
    private static final int PRICE_FRAME_FIRE = 220;

    private ActivityShopBinding binding;
    private PreferencesManager prefsManager;
    private UserRepository userRepository;
    private RetroFeedbackManager feedbackManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
        userRepository = new UserRepository(this);
        feedbackManager = new RetroFeedbackManager(this);
        setupListeners();
        setupBottomNav();
        loadUser();
    }

    private void loadUser() {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            renderUi();
            return;
        }
        userRepository.getUserById(userId, new DbCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (isFinishing() || user == null) {
                    return;
                }
                currentUser = user;
                prefsManager.syncFromUser(user);
                renderUi();
            }

            @Override
            public void onError(Exception error) {
                if (!isFinishing()) {
                    renderUi();
                }
            }
        });
    }

    private void renderUi() {
        int coins = currentUser != null ? currentUser.getCoins() : prefsManager.getPlayerCoins();
        int energy = currentUser != null ? currentUser.getEnergy() : prefsManager.getPlayerEnergy();
        int shields = currentUser != null ? currentUser.getShields() : prefsManager.getPlayerShields();

        binding.tvShopCoins.setText(getString(R.string.shop_coins_format, coins));
        binding.tvShopEnergyStatus.setText(getString(R.string.shop_energy_format, energy));
        binding.tvShopShieldsStatus.setText(getString(R.string.shop_shields_format, shields));

        binding.btnBuyEnergy.setText(getString(R.string.shop_btn_buy_format, PRICE_ENERGY));
        binding.btnBuyShield.setText(getString(R.string.shop_btn_buy_format, PRICE_SHIELD));

        renderTitleButton(binding.btnBuyTitleHunter, getString(R.string.shop_item_title_hunter), PRICE_TITLE_HUNTER);
        renderTitleButton(binding.btnBuyTitleSage, getString(R.string.shop_item_title_sage), PRICE_TITLE_SAGE);
        renderTitleButton(binding.btnBuyTitleLegend, getString(R.string.shop_item_title_legend), PRICE_TITLE_LEGEND);

        renderFrameButton(binding.btnBuyFrameGold, AvatarFrameHelper.FRAME_GOLD, PRICE_FRAME_GOLD);
        renderFrameButton(binding.btnBuyFrameNeon, AvatarFrameHelper.FRAME_NEON, PRICE_FRAME_NEON);
        renderFrameButton(binding.btnBuyFrameFire, AvatarFrameHelper.FRAME_FIRE, PRICE_FRAME_FIRE);
    }

    private void renderFrameButton(Button btn, String frameKey, int price) {
        boolean owned = currentUser != null && currentUser.ownsFrame(frameKey);
        boolean equipped = currentUser != null && frameKey.equals(currentUser.getEquippedFrame());
        if (equipped) {
            btn.setText(getString(R.string.shop_btn_equipped));
            btn.setEnabled(false);
            btn.setAlpha(0.75f);
        } else if (owned) {
            btn.setText(getString(R.string.shop_btn_equip));
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        } else {
            btn.setText(getString(R.string.shop_btn_buy_format, price));
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        }
    }

    private void renderTitleButton(Button btn, String titleName, int price) {
        boolean owned = currentUser != null && currentUser.ownsTitle(titleName);
        boolean equipped = currentUser != null && titleName.equals(currentUser.getEquippedTitle());
        if (equipped) {
            btn.setText(getString(R.string.shop_btn_equipped));
            btn.setEnabled(false);
            btn.setAlpha(0.75f);
        } else if (owned) {
            btn.setText(getString(R.string.shop_btn_equip));
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        } else {
            btn.setText(getString(R.string.shop_btn_buy_format, price));
            btn.setEnabled(true);
            btn.setAlpha(1.0f);
        }
    }

    private void setupListeners() {
        binding.btnShopBack.setOnClickListener(v -> finish());
        binding.btnBuyEnergy.setOnClickListener(v -> purchaseItem("ENERGY", null, PRICE_ENERGY, getString(R.string.shop_item_energy_title)));
        binding.btnBuyShield.setOnClickListener(v -> purchaseItem("SHIELD", null, PRICE_SHIELD, getString(R.string.shop_item_shield_title)));
        binding.btnBuyTitleHunter.setOnClickListener(v -> handleTitleClick(getString(R.string.shop_item_title_hunter), PRICE_TITLE_HUNTER));
        binding.btnBuyTitleSage.setOnClickListener(v -> handleTitleClick(getString(R.string.shop_item_title_sage), PRICE_TITLE_SAGE));
        binding.btnBuyTitleLegend.setOnClickListener(v -> handleTitleClick(getString(R.string.shop_item_title_legend), PRICE_TITLE_LEGEND));
        binding.btnBuyFrameGold.setOnClickListener(v -> handleFrameClick(AvatarFrameHelper.FRAME_GOLD, getString(R.string.shop_frame_gold), PRICE_FRAME_GOLD));
        binding.btnBuyFrameNeon.setOnClickListener(v -> handleFrameClick(AvatarFrameHelper.FRAME_NEON, getString(R.string.shop_frame_neon), PRICE_FRAME_NEON));
        binding.btnBuyFrameFire.setOnClickListener(v -> handleFrameClick(AvatarFrameHelper.FRAME_FIRE, getString(R.string.shop_frame_fire), PRICE_FRAME_FIRE));
    }

    private void handleFrameClick(String frameKey, String frameLabel, int price) {
        if (currentUser != null && currentUser.ownsFrame(frameKey)) {
            long userId = prefsManager.getCurrentUserId();
            userRepository.equipFrame(userId, frameKey, new DbCallback<User>() {
                @Override
                public void onSuccess(User updated) {
                    if (isFinishing() || updated == null) {
                        return;
                    }
                    currentUser = updated;
                    prefsManager.syncFromUser(updated);
                    feedbackManager.playPowerup();
                    Toast.makeText(ShopActivity.this,
                            getString(R.string.shop_equipped_frame_msg, frameLabel), Toast.LENGTH_SHORT).show();
                    renderUi();
                }

                @Override
                public void onError(Exception error) {
                }
            });
            return;
        }
        purchaseItem("FRAME", frameKey, price, frameLabel);
    }

    private void handleTitleClick(String titleName, int price) {
        if (currentUser != null && currentUser.ownsTitle(titleName)) {
            long userId = prefsManager.getCurrentUserId();
            userRepository.equipTitle(userId, titleName, new DbCallback<User>() {
                @Override
                public void onSuccess(User updated) {
                    if (isFinishing() || updated == null) {
                        return;
                    }
                    currentUser = updated;
                    prefsManager.syncFromUser(updated);
                    feedbackManager.playPowerup();
                    Toast.makeText(ShopActivity.this,
                            getString(R.string.shop_equipped_msg, titleName), Toast.LENGTH_SHORT).show();
                    renderUi();
                }

                @Override
                public void onError(Exception error) {
                }
            });
            return;
        }
        purchaseItem("TITLE", titleName, price, titleName);
    }

    private void purchaseItem(String itemType, String titleKey, int price, String label) {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            return;
        }
        int coins = currentUser != null ? currentUser.getCoins() : prefsManager.getPlayerCoins();
        if (coins < price) {
            feedbackManager.playWrong();
            Toast.makeText(this, getString(R.string.shop_not_enough_coins), Toast.LENGTH_SHORT).show();
            return;
        }
        userRepository.buyShopItem(userId, itemType, titleKey, price, new DbCallback<User>() {
            @Override
            public void onSuccess(User updated) {
                if (isFinishing() || updated == null) {
                    return;
                }
                currentUser = updated;
                prefsManager.syncFromUser(updated);
                feedbackManager.playVictory();
                Toast.makeText(ShopActivity.this,
                        getString(R.string.shop_purchased_msg, label), Toast.LENGTH_SHORT).show();
                renderUi();
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                Toast.makeText(ShopActivity.this, getString(R.string.shop_not_enough_coins), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNav() {
        ImageView ivNavMap = findViewById(R.id.ivNavMap);
        ImageView ivNavLeaderboard = findViewById(R.id.ivNavLeaderboard);
        ImageView ivNavSocial = findViewById(R.id.ivNavSocial);
        ImageView ivNavProfile = findViewById(R.id.ivNavProfile);
        View btnNavMap = findViewById(R.id.btnNavMap);
        View btnNavLeaderboard = findViewById(R.id.btnNavLeaderboard);
        View btnNavSocial = findViewById(R.id.btnNavSocial);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) ivNavMap.setColorFilter(inactiveColor);
        if (ivNavLeaderboard != null) ivNavLeaderboard.setColorFilter(inactiveColor);
        if (ivNavSocial != null) ivNavSocial.setColorFilter(inactiveColor);
        if (ivNavProfile != null) ivNavProfile.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));

        if (btnNavSocial != null) {
            btnNavSocial.setOnClickListener(v -> {
                startActivity(new Intent(ShopActivity.this, SocialActivity.class));
                finish();
            });
        }
        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(ShopActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> {
                startActivity(new Intent(ShopActivity.this, RankingActivity.class));
                finish();
            });
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                startActivity(new Intent(ShopActivity.this, ProfileActivity.class));
                finish();
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (feedbackManager != null) {
            feedbackManager.release();
        }
    }
}
