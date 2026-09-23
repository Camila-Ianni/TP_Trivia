package com.example.triviamaster.adapters;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.triviamaster.databinding.ItemAvatarBinding;
import com.example.triviamaster.models.Avatar;

import java.util.List;
import java.util.function.Consumer;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    private List<Avatar> avatars;
    private int selectedAvatarId;
    private Consumer<Avatar> onAvatarSelected;

    public AvatarAdapter(List<Avatar> avatars, int selectedAvatarId, Consumer<Avatar> onAvatarSelected) {
        this.avatars = avatars;
        this.selectedAvatarId = selectedAvatarId;
        this.onAvatarSelected = onAvatarSelected;
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {
        private ItemAvatarBinding binding;

        AvatarViewHolder(ItemAvatarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Avatar avatar) {
            binding.ivAvatarIcon.setImageResource(avatar.getIconResId());
            binding.tvAvatarName.setText(avatar.getName());

            GradientDrawable background = new GradientDrawable();
            background.setShape(GradientDrawable.RECTANGLE);
            background.setCornerRadius(48f);
            background.setColor(Color.parseColor(avatar.getBackgroundColorHex()));
            background.setStroke(
                    avatar.getId() == selectedAvatarId ? 8 : 3,
                    Color.parseColor(avatar.getId() == selectedAvatarId ? "#6C5CE7" : "#ECE8F5")
            );
            binding.avatarContainer.setBackground(background);
            binding.getRoot().setOnClickListener(v -> {
                selectedAvatarId = avatar.getId();
                notifyDataSetChanged();
                onAvatarSelected.accept(avatar);
            });
        }
    }

    @Override
    public AvatarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvatarViewHolder(ItemAvatarBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(AvatarViewHolder holder, int position) {
        holder.bind(avatars.get(position));
    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    public Avatar getSelectedAvatar() {
        for (Avatar avatar : avatars) {
            if (avatar.getId() == selectedAvatarId) {
                return avatar;
            }
        }
        return avatars.get(0);
    }
}
