package com.example.triviamaster;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.SocialAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.QuestionBank;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.FriendshipRepository;
import com.example.triviamaster.data.db.QuestionRepository;
import com.example.triviamaster.databinding.ActivitySocialBinding;
import com.example.triviamaster.models.Friendship;
import com.example.triviamaster.models.Question;
import com.example.triviamaster.models.User;
import com.example.triviamaster.network.ActiveDuelSession;
import com.example.triviamaster.network.LocalAreaMatchmaker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SocialActivity extends AppCompatActivity {

    private ActivitySocialBinding binding;
    private PreferencesManager prefsManager;
    private FriendshipRepository friendshipRepository;
    private QuestionRepository questionRepository;
    private LocalAreaMatchmaker matchmaker;
    private AlertDialog searchingDialog;
    private SocialAdapter requestsAdapter;
    private SocialAdapter friendsAdapter;
    private SocialAdapter searchAdapter;
    private final Map<Long, String> statusByUserId = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySocialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (view, insets) -> {
            Insets statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            ViewGroup.LayoutParams lp = binding.layoutSocialHeader.getLayoutParams();
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) lp;
                params.topMargin = statusBarInsets.top + (int) (8 * getResources().getDisplayMetrics().density);
                binding.layoutSocialHeader.setLayoutParams(params);
            }
            return insets;
        });

        prefsManager = new PreferencesManager(this);
        friendshipRepository = new FriendshipRepository(this);
        questionRepository = new QuestionRepository(this);
        matchmaker = new LocalAreaMatchmaker(this);

        setupRecyclers();
        setupListeners();
        setupBottomNav();
        loadFriendsAndRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFriendsAndRequests();
    }

    private void setupRecyclers() {
        SocialAdapter.OnSocialActionListener listener = new SocialAdapter.OnSocialActionListener() {
            @Override
            public void onAcceptRequest(Friendship friendship) {
                friendshipRepository.acceptFriendRequest(friendship.getId(), new DbCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean ok) {
                        if (isFinishing()) return;
                        Toast.makeText(SocialActivity.this,
                                getString(R.string.social_request_accepted, friendship.getOtherUsername()),
                                Toast.LENGTH_SHORT).show();
                        loadFriendsAndRequests();
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
            }

            @Override
            public void onRejectRequest(Friendship friendship) {
                friendshipRepository.removeFriendship(friendship.getId(), new DbCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean ok) {
                        if (isFinishing()) return;
                        loadFriendsAndRequests();
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
            }

            @Override
            public void onChallengeFriend(Friendship friendship) {
                launchDuelWithOpponent(friendship.getOtherUsername());
            }

            @Override
            public void onAddUser(User user) {
                long myId = prefsManager.getCurrentUserId();
                friendshipRepository.sendFriendRequest(myId, user.getId(), new DbCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (isFinishing()) return;
                        if (FriendshipRepository.RESULT_AUTO_ACCEPTED.equals(result)) {
                            Toast.makeText(SocialActivity.this,
                                    getString(R.string.social_request_accepted, user.getUsername()),
                                    Toast.LENGTH_SHORT).show();
                        } else if (FriendshipRepository.RESULT_ALREADY_FRIENDS.equals(result)) {
                            Toast.makeText(SocialActivity.this,
                                    getString(R.string.social_request_already_friends, user.getUsername()),
                                    Toast.LENGTH_SHORT).show();
                        } else if (FriendshipRepository.RESULT_ALREADY_PENDING.equals(result)) {
                            Toast.makeText(SocialActivity.this,
                                    getString(R.string.social_request_already_pending, user.getUsername()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SocialActivity.this,
                                    getString(R.string.social_request_sent, user.getUsername()),
                                    Toast.LENGTH_SHORT).show();
                        }
                        loadFriendsAndRequests();
                        performSearch();
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                });
            }
        };

        requestsAdapter = new SocialAdapter(SocialAdapter.MODE_INCOMING_REQUEST, prefsManager, listener);
        friendsAdapter = new SocialAdapter(SocialAdapter.MODE_FRIEND, prefsManager, listener);
        searchAdapter = new SocialAdapter(SocialAdapter.MODE_SEARCH_USER, prefsManager, listener);

        binding.rvIncomingRequests.setLayoutManager(new LinearLayoutManager(this));
        binding.rvIncomingRequests.setAdapter(requestsAdapter);

        binding.rvFriendsList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvFriendsList.setAdapter(friendsAdapter);

        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        binding.rvSearchResults.setAdapter(searchAdapter);
    }

    private void setupListeners() {
        binding.btnRandomDuel.setOnClickListener(v -> startRandomDuel());
        binding.btnLocalDuel.setOnClickListener(v ->
                startActivity(new Intent(SocialActivity.this, DuelActivity.class)));
        binding.btnSocialSearch.setOnClickListener(v -> performSearch());
    }

    private void startRandomDuel() {
        String savedName = prefsManager.getLastPlayerName();
        String localName = (savedName != null && !savedName.trim().isEmpty()) ? savedName.trim() : "Jugador";

        searchingDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.duel_searching_area_title)
                .setMessage(R.string.duel_searching_area_desc)
                .setCancelable(false)
                .setNegativeButton(R.string.duel_btn_cancel_search, (d, w) -> {
                    matchmaker.stop();
                    d.dismiss();
                })
                .show();

        questionRepository.getRandomQuestions(16, new DbCallback<List<Question>>() {
            @Override
            public void onSuccess(List<Question> dbQuestions) {
                if (isFinishing()) return;
                List<Integer> qIds = extractDuelQuestionIds(dbQuestions);
                beginAreaMatchmaking(localName, qIds);
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) return;
                List<Integer> qIds = extractDuelQuestionIds(QuestionBank.INSTANCE.getRandomQuiz(12));
                beginAreaMatchmaking(localName, qIds);
            }
        });
    }

    private List<Integer> extractDuelQuestionIds(List<Question> source) {
        List<Integer> ids = new ArrayList<>();
        if (source != null) {
            for (Question q : source) {
                if (q != null && !q.isWordPuzzle() && q.getOptions() != null && q.getOptions().size() >= 4) {
                    ids.add(q.getId());
                    if (ids.size() >= 6) {
                        break;
                    }
                }
            }
        }
        return ids;
    }

    private void beginAreaMatchmaking(String localName, List<Integer> qIds) {
        matchmaker.startSearch(localName, qIds, new LocalAreaMatchmaker.MatchCallback() {
            @Override
            public void onMatchFound(ActiveDuelSession session) {
                if (isFinishing()) {
                    session.close();
                    return;
                }
                if (searchingDialog != null && searchingDialog.isShowing()) {
                    searchingDialog.dismiss();
                }
                ActiveDuelSession.setCurrent(session);
                Intent intent = new Intent(SocialActivity.this, DuelActivity.class);
                intent.putExtra(DuelActivity.EXTRA_IS_NETWORK_DUEL, true);
                startActivity(intent);
            }

            @Override
            public void onNoPlayersInArea() {
                if (isFinishing()) return;
                if (searchingDialog != null && searchingDialog.isShowing()) {
                    searchingDialog.dismiss();
                }
                new AlertDialog.Builder(SocialActivity.this)
                        .setTitle(R.string.duel_no_players_area_title)
                        .setMessage(R.string.duel_no_players_area_desc)
                        .setPositiveButton(R.string.btn_back, null)
                        .show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (matchmaker != null) {
            matchmaker.stop();
        }
        if (searchingDialog != null && searchingDialog.isShowing()) {
            searchingDialog.dismiss();
        }
        super.onDestroy();
    }

    private void launchDuelWithOpponent(String opponentName) {
        Intent intent = new Intent(SocialActivity.this, DuelActivity.class);
        intent.putExtra(DuelActivity.EXTRA_OPPONENT_NAME, opponentName);
        startActivity(intent);
    }

    private void performSearch() {
        long myId = prefsManager.getCurrentUserId();
        String query = binding.etSocialSearch.getText() != null
                ? binding.etSocialSearch.getText().toString().trim()
                : "";
        friendshipRepository.searchUsersToAdd(myId, query, new DbCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                if (isFinishing()) return;
                binding.tvSearchHeader.setVisibility(View.VISIBLE);
                if (users == null || users.isEmpty()) {
                    binding.rvSearchResults.setVisibility(View.GONE);
                    binding.tvEmptySearch.setVisibility(View.VISIBLE);
                } else {
                    binding.tvEmptySearch.setVisibility(View.GONE);
                    binding.rvSearchResults.setVisibility(View.VISIBLE);
                    searchAdapter.setSearchUsers(users, statusByUserId);
                }
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }

    private void loadFriendsAndRequests() {
        long myId = prefsManager.getCurrentUserId();
        friendshipRepository.getFriendsAndRequests(myId, new DbCallback<List<Friendship>>() {
            @Override
            public void onSuccess(List<Friendship> items) {
                if (isFinishing()) return;
                statusByUserId.clear();
                List<Friendship> incoming = new ArrayList<>();
                List<Friendship> accepted = new ArrayList<>();
                if (items != null) {
                    for (Friendship f : items) {
                        statusByUserId.put(f.getOtherUserId(), f.getStatus());
                        if (f.isAccepted()) {
                            accepted.add(f);
                        } else if (f.isIncoming()) {
                            incoming.add(f);
                        }
                    }
                }

                if (incoming.isEmpty()) {
                    binding.tvRequestsHeader.setVisibility(View.GONE);
                    binding.rvIncomingRequests.setVisibility(View.GONE);
                } else {
                    binding.tvRequestsHeader.setVisibility(View.VISIBLE);
                    binding.rvIncomingRequests.setVisibility(View.VISIBLE);
                    requestsAdapter.setFriendships(incoming);
                }

                friendsAdapter.setFriendships(accepted);
                binding.tvEmptyFriends.setVisibility(accepted.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception error) {
            }
        });
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
        View btnNavQuests = findViewById(R.id.btnNavQuests);
        View btnNavQuestions = findViewById(R.id.btnNavQuestions);
        View btnNavProfile = findViewById(R.id.btnNavProfile);

        int inactiveColor = ContextCompat.getColor(this, R.color.text_muted);
        if (ivNavMap != null) ivNavMap.setColorFilter(inactiveColor);
        if (ivNavLeaderboard != null) ivNavLeaderboard.setColorFilter(inactiveColor);
        if (ivNavSocial != null) ivNavSocial.setColorFilter(ContextCompat.getColor(this, R.color.ref_btn_coral));
        if (ivNavQuests != null) ivNavQuests.setColorFilter(inactiveColor);
        if (ivNavQuestions != null) ivNavQuestions.setColorFilter(inactiveColor);
        if (ivNavProfile != null) ivNavProfile.setColorFilter(inactiveColor);

        if (btnNavMap != null) {
            btnNavMap.setOnClickListener(v -> {
                Intent intent = new Intent(SocialActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
        if (btnNavLeaderboard != null) {
            btnNavLeaderboard.setOnClickListener(v -> {
                startActivity(new Intent(SocialActivity.this, RankingActivity.class));
                finish();
            });
        }
        if (btnNavQuests != null) {
            btnNavQuests.setOnClickListener(v -> {
                startActivity(new Intent(SocialActivity.this, QuestsActivity.class));
                finish();
            });
        }
        if (btnNavQuestions != null) {
            btnNavQuestions.setVisibility(prefsManager.isCurrentUserAdmin() ? View.VISIBLE : View.GONE);
            btnNavQuestions.setOnClickListener(v -> {
                startActivity(new Intent(SocialActivity.this, QuestionManagerActivity.class));
                finish();
            });
        }
        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                startActivity(new Intent(SocialActivity.this, ProfileActivity.class));
                finish();
            });
        }
    }
}
