package com.example.triviamaster.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.data.PreferencesManager;
import com.example.triviamaster.databinding.ItemUserBinding;
import com.example.triviamaster.models.Avatar;
import com.example.triviamaster.models.User;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnUserActionListener {
        void onEditUser(User user);

        void onDeleteUser(User user);
    }

    private final List<User> users;
    private final PreferencesManager prefsManager;
    private final OnUserActionListener listener;

    public UserAdapter(List<User> users, PreferencesManager prefsManager, OnUserActionListener listener) {
        this.users = users;
        this.prefsManager = prefsManager;
        this.listener = listener;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBinding binding;

        UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(User user) {
            Avatar avatar = prefsManager.getAvatarById(user.getAvatarId());
            binding.ivItemUserAvatar.setImageResource(avatar.getIconResId());
            binding.tvItemUserName.setText(user.getUsername() + " (" + user.getRole() + ")");
            String emailText = user.getEmail() != null && !user.getEmail().isEmpty()
                    ? user.getEmail()
                    : "-";
            binding.tvItemUserEmail.setText(emailText);
            binding.tvItemUserStats.setText(binding.getRoot().getContext().getString(
                    R.string.um_stats_format, user.getHighScore(), user.getMaxLevel()));

            binding.btnEditUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditUser(user);
                }
            });
            binding.btnDeleteUser.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteUser(user);
                }
            });
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(ItemUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
