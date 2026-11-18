package com.example.triviamaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.databinding.ItemSocialUserBinding;
import com.example.triviamaster.models.Friendship;
import com.example.triviamaster.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.SocialViewHolder> {

    public static final int MODE_INCOMING_REQUEST = 1;
    public static final int MODE_FRIEND = 2;
    public static final int MODE_SEARCH_USER = 3;

    public interface OnSocialActionListener {
        void onAcceptRequest(Friendship friendship);
        void onRejectRequest(Friendship friendship);
        void onChallengeFriend(Friendship friendship);
        void onAddUser(User user);
    }

    private final int mode;
    private final PreferencesManager prefsManager;
    private final OnSocialActionListener listener;
    private final List<Friendship> friendships = new ArrayList<>();
    private final List<User> searchUsers = new ArrayList<>();
    private Map<Long, String> statusByUserId;

    public SocialAdapter(int mode, PreferencesManager prefsManager, OnSocialActionListener listener) {
        this.mode = mode;
        this.prefsManager = prefsManager;
        this.listener = listener;
    }

    public void setFriendships(List<Friendship> items) {
        friendships.clear();
        if (items != null) {
            friendships.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setSearchUsers(List<User> users, Map<Long, String> statusByUserId) {
        searchUsers.clear();
        if (users != null) {
            searchUsers.addAll(users);
        }
        this.statusByUserId = statusByUserId;
        notifyDataSetChanged();
    }

    @Override
    public SocialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SocialViewHolder(ItemSocialUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(SocialViewHolder holder, int position) {
        Context ctx = holder.binding.getRoot().getContext();
        if (mode == MODE_SEARCH_USER) {
            User user = searchUsers.get(position);
            holder.binding.ivSocialAvatar.setImageResource(
                    prefsManager.getAvatarById(user.getAvatarId()).getIconResId());
            String flag = com.example.triviamaster.utils.CountryHelper.getByCode(user.getCountry()).getFlag();
            holder.binding.tvSocialUsername.setText(flag + " " + user.getUsername());
            holder.binding.tvSocialStats.setText(ctx.getString(
                    R.string.social_player_stats_format, user.getHighScore(), user.getMaxStreak()));
            holder.binding.btnSocialSecondary.setVisibility(View.GONE);

            String status = statusByUserId != null ? statusByUserId.get(user.getId()) : null;
            if ("ACCEPTED".equalsIgnoreCase(status)) {
                holder.binding.btnSocialPrimary.setText(R.string.social_status_friends);
                holder.binding.btnSocialPrimary.setEnabled(false);
                holder.binding.btnSocialPrimary.setAlpha(0.6f);
            } else if ("PENDING".equalsIgnoreCase(status)) {
                holder.binding.btnSocialPrimary.setText(R.string.social_status_pending);
                holder.binding.btnSocialPrimary.setEnabled(false);
                holder.binding.btnSocialPrimary.setAlpha(0.6f);
            } else {
                holder.binding.btnSocialPrimary.setText(R.string.social_btn_add);
                holder.binding.btnSocialPrimary.setEnabled(true);
                holder.binding.btnSocialPrimary.setAlpha(1f);
                holder.binding.btnSocialPrimary.setOnClickListener(v -> listener.onAddUser(user));
            }
            return;
        }

        Friendship f = friendships.get(position);
        holder.binding.ivSocialAvatar.setImageResource(
                prefsManager.getAvatarById(f.getOtherAvatarId()).getIconResId());
        holder.binding.tvSocialUsername.setText(f.getOtherUsername());
        holder.binding.tvSocialStats.setText(ctx.getString(
                R.string.social_player_stats_format, f.getOtherHighScore(), f.getOtherMaxStreak()));

        if (mode == MODE_INCOMING_REQUEST) {
            holder.binding.btnSocialSecondary.setVisibility(View.VISIBLE);
            holder.binding.btnSocialSecondary.setText(R.string.social_btn_reject);
            holder.binding.btnSocialSecondary.setOnClickListener(v -> listener.onRejectRequest(f));
            holder.binding.btnSocialPrimary.setText(R.string.social_btn_accept);
            holder.binding.btnSocialPrimary.setEnabled(true);
            holder.binding.btnSocialPrimary.setAlpha(1f);
            holder.binding.btnSocialPrimary.setOnClickListener(v -> listener.onAcceptRequest(f));
        } else {
            holder.binding.btnSocialSecondary.setVisibility(View.GONE);
            holder.binding.btnSocialPrimary.setText(R.string.social_btn_challenge);
            holder.binding.btnSocialPrimary.setEnabled(true);
            holder.binding.btnSocialPrimary.setAlpha(1f);
            holder.binding.btnSocialPrimary.setOnClickListener(v -> listener.onChallengeFriend(f));
        }
    }

    @Override
    public int getItemCount() {
        return mode == MODE_SEARCH_USER ? searchUsers.size() : friendships.size();
    }

    static class SocialViewHolder extends RecyclerView.ViewHolder {
        final ItemSocialUserBinding binding;

        SocialViewHolder(ItemSocialUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
