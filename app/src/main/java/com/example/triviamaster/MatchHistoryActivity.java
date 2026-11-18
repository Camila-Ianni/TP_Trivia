package com.example.triviamaster;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.triviamaster.adapters.MatchHistoryAdapter;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.data.db.DbCallback;
import com.example.triviamaster.data.db.MatchHistoryRepository;
import com.example.triviamaster.databinding.ActivityMatchHistoryBinding;
import com.example.triviamaster.models.MatchHistoryItem;
import java.util.ArrayList;
import java.util.List;

public final class MatchHistoryActivity extends AppCompatActivity {

    private ActivityMatchHistoryBinding binding;
    private PreferencesManager prefsManager;
    private MatchHistoryRepository historyRepository;
    private final List<MatchHistoryItem> items = new ArrayList<>();
    private MatchHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMatchHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefsManager = new PreferencesManager(this);
        historyRepository = new MatchHistoryRepository(this);
        setupRecycler();
        setupListeners();
        loadHistory();
    }

    private void setupRecycler() {
        adapter = new MatchHistoryAdapter(items, item ->
                historyRepository.deleteMatch(item.getId(), new DbCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        if (isFinishing()) {
                            return;
                        }
                        Toast.makeText(MatchHistoryActivity.this,
                                getString(R.string.history_deleted_item), Toast.LENGTH_SHORT).show();
                        loadHistory();
                    }

                    @Override
                    public void onError(Exception error) {
                    }
                }));
        binding.rvMatchHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvMatchHistory.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.btnHistoryBack.setOnClickListener(v -> finish());
        binding.btnClearHistory.setOnClickListener(v -> {
            if (items.isEmpty()) {
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.history_clear_confirm_title))
                    .setMessage(getString(R.string.history_clear_confirm_msg))
                    .setPositiveButton(getString(R.string.btn_confirm_delete), (dialog, which) ->
                            historyRepository.clearHistoryForUser(prefsManager.getCurrentUserId(), new DbCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    if (isFinishing()) {
                                        return;
                                    }
                                    Toast.makeText(MatchHistoryActivity.this,
                                            getString(R.string.history_cleared_msg), Toast.LENGTH_SHORT).show();
                                    loadHistory();
                                }

                                @Override
                                public void onError(Exception error) {
                                }
                            }))
                    .setNegativeButton(getString(R.string.btn_cancel), null)
                    .show();
        });
    }

    private void loadHistory() {
        long userId = prefsManager.getCurrentUserId();
        if (userId <= 0) {
            binding.tvEmptyHistory.setVisibility(View.VISIBLE);
            return;
        }
        binding.progressHistory.setVisibility(View.VISIBLE);
        historyRepository.getHistoryForUser(userId, new DbCallback<List<MatchHistoryItem>>() {
            @Override
            public void onSuccess(List<MatchHistoryItem> result) {
                if (isFinishing()) {
                    return;
                }
                binding.progressHistory.setVisibility(View.GONE);
                items.clear();
                if (result != null) {
                    items.addAll(result);
                }
                adapter.notifyDataSetChanged();
                binding.tvEmptyHistory.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception error) {
                if (isFinishing()) {
                    return;
                }
                binding.progressHistory.setVisibility(View.GONE);
            }
        });
    }
}
