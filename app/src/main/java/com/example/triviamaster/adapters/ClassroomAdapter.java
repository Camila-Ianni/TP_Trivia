package com.example.triviamaster.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.triviamaster.R;
import com.example.triviamaster.databinding.ItemClassroomBinding;
import com.example.triviamaster.models.Classroom;
import java.util.ArrayList;
import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder> {

    public interface OnClassroomActionListener {
        void onPlayClassroom(Classroom classroom);
        void onOpenAiPdf(Classroom classroom);
    }

    private final boolean isTeacherMode;
    private final OnClassroomActionListener listener;
    private final List<Classroom> items = new ArrayList<>();

    public ClassroomAdapter(boolean isTeacherMode, OnClassroomActionListener listener) {
        this.isTeacherMode = isTeacherMode;
        this.listener = listener;
    }

    public void setItems(List<Classroom> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ItemClassroomBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context ctx = holder.binding.getRoot().getContext();
        Classroom c = items.get(position);
        holder.binding.tvClassroomName.setText(c.getName());

        if (isTeacherMode) {
            holder.binding.tvClassroomMeta.setText(ctx.getString(
                    R.string.classroom_code_format, c.getCode(), c.getQuestionCount()));
            holder.binding.btnClassroomAiPdf.setVisibility(View.VISIBLE);
            holder.binding.btnClassroomAiPdf.setOnClickListener(v -> listener.onOpenAiPdf(c));
            holder.binding.btnClassroomPlay.setEnabled(true);
            holder.binding.btnClassroomPlay.setAlpha(1f);
            holder.binding.btnClassroomPlay.setText(R.string.classroom_btn_play);
            holder.binding.btnClassroomPlay.setOnClickListener(v -> listener.onPlayClassroom(c));
        } else {
            holder.binding.btnClassroomAiPdf.setVisibility(View.GONE);
            holder.binding.tvClassroomMeta.setText(ctx.getString(
                    R.string.classroom_teacher_format, c.getTeacherName(), c.getQuestionCount()));
            if (c.isApproved()) {
                holder.binding.btnClassroomPlay.setEnabled(true);
                holder.binding.btnClassroomPlay.setAlpha(1f);
                holder.binding.btnClassroomPlay.setText(R.string.classroom_btn_play);
                holder.binding.btnClassroomPlay.setOnClickListener(v -> listener.onPlayClassroom(c));
            } else {
                holder.binding.btnClassroomPlay.setEnabled(false);
                holder.binding.btnClassroomPlay.setAlpha(0.6f);
                holder.binding.btnClassroomPlay.setText(R.string.classroom_status_pending);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemClassroomBinding binding;

        ViewHolder(ItemClassroomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
