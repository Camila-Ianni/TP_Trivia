package com.example.triviamaster;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
<<<<<<< HEAD
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RoadmapLayout extends FrameLayout {

=======

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.List;

public class RoadmapLayout extends FrameLayout {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    public interface StarsProvider {
        int getStars(int levelId);
    }

<<<<<<< HEAD
    public interface OnNodeClickListener {
        void onNodeClick(int levelId, boolean isUnlocked, int stars);
    }

    public static final class NodeCoord {
        private final float xFrac;
        private final float yFrac;

        public NodeCoord(float xFrac, float yFrac) {
            this.xFrac = xFrac;
            this.yFrac = yFrac;
        }

        public float getXFrac() {
            return xFrac;
        }

        public float getYFrac() {
            return yFrac;
        }
    }

    public static final List<NodeCoord> COORDS = Arrays.asList(
=======
    public interface LevelClickListener {
        void onLevelClick(int levelId, boolean unlocked, int stars);
    }

    public interface CastleClickListener {
        void onCastleClick();
    }

    private static final int LEVEL_COUNT = 16;
    private static final int CASTLE_INDEX = LEVEL_COUNT;
    private static final float BACKGROUND_HEIGHT_RATIO = 3.0235784f;
    private static final float NODE_SIZE_RATIO = 0.09153953f;

    private static final List<NodeCoord> COORDINATES = List.of(
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
            new NodeCoord(0.5065f, 0.0586f),
            new NodeCoord(0.5982f, 0.1061f),
            new NodeCoord(0.4594f, 0.1466f),
            new NodeCoord(0.5806f, 0.1877f),
            new NodeCoord(0.651f, 0.2413f),
            new NodeCoord(0.4189f, 0.33f),
            new NodeCoord(0.5259f, 0.3831f),
            new NodeCoord(0.4971f, 0.4465f),
            new NodeCoord(0.3857f, 0.4932f),
            new NodeCoord(0.5444f, 0.5337f),
            new NodeCoord(0.6266f, 0.5783f),
            new NodeCoord(0.4516f, 0.6469f),
            new NodeCoord(0.5766f, 0.677f),
            new NodeCoord(0.4979f, 0.7415f),
            new NodeCoord(0.4775f, 0.7917f),
            new NodeCoord(0.5072f, 0.8483f),
            new NodeCoord(0.4993f, 0.9731f)
    );

<<<<<<< HEAD
    private final ImageView bgImageView;
    private final List<ValueAnimator> activeAnimators = new ArrayList<>();
=======
    private final ImageView backgroundImage;
    private final List<ValueAnimator> activeAnimations = new ArrayList<>();
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

    public RoadmapLayout(Context context) {
        this(context, null);
    }

    public RoadmapLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoadmapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
<<<<<<< HEAD
        bgImageView = new ImageView(context);
        bgImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        bgImageView.setImageResource(R.drawable.img_roadmap_full);
        bgImageView.setContentDescription("Roadmap");
        addView(bgImageView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
=======
        backgroundImage = createBackgroundImage(context);
        addView(backgroundImage, new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        ));
    }

    public static List<NodeCoord> getCoords() {
        return COORDINATES;
    }

    private static ImageView createBackgroundImage(Context context) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        image.setImageResource(R.drawable.img_roadmap_full);
        image.setContentDescription("Roadmap");
        return image;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
<<<<<<< HEAD
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * 3.0235784f);
        int exactWidthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int exactHeightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        bgImageView.measure(exactWidthSpec, exactHeightSpec);

        int nodeSize = (int) (width * 0.09153953f);
        int nodeSpec = View.MeasureSpec.makeMeasureSpec(nodeSize, View.MeasureSpec.EXACTLY);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != bgImageView && child.getVisibility() != View.GONE) {
=======
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * BACKGROUND_HEIGHT_RATIO);
        int exactWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int exactHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        backgroundImage.measure(exactWidth, exactHeight);

        int nodeSize = (int) (width * NODE_SIZE_RATIO);
        int nodeSpec = MeasureSpec.makeMeasureSpec(nodeSize, MeasureSpec.EXACTLY);
        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            if (child != backgroundImage && child.getVisibility() != View.GONE) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
                child.measure(nodeSpec, nodeSpec);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
<<<<<<< HEAD
        int w = right - left;
        int h = bottom - top;
        bgImageView.layout(0, 0, w, h);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != bgImageView && child.getVisibility() != View.GONE) {
                Object tag = child.getTag();
                if (tag instanceof Integer) {
                    int tagIndex = (Integer) tag;
                    if (tagIndex >= 0 && tagIndex < COORDS.size()) {
                        NodeCoord coord = COORDS.get(tagIndex);
                        int cx = (int) (w * coord.getXFrac());
                        int cy = (int) (h * coord.getYFrac());
                        int halfW = child.getMeasuredWidth() / 2;
                        int halfH = child.getMeasuredHeight() / 2;
                        child.layout(cx - halfW, cy - halfH, cx + halfW, cy + halfH);
                    }
                }
            }
        }
    }

    public void populateRoadmap(int maxUnlocked, StarsProvider starsProvider, OnNodeClickListener onNodeClick, Runnable onCastleClick) {
        for (ValueAnimator anim : activeAnimators) {
            anim.cancel();
        }
        activeAnimators.clear();

        while (getChildCount() > 1) {
            removeViewAt(getChildCount() - 1);
        }

        float density = getResources().getDisplayMetrics().density;
        for (int levelId = 1; levelId <= 16; levelId++) {
            int index = levelId - 1;
            int stars = starsProvider.getStars(levelId);
            boolean isCompleted = levelId < maxUnlocked || stars > 0;
            boolean isCurrent = levelId == maxUnlocked && !isCompleted;
            boolean isLocked = levelId > maxUnlocked;

            FrameLayout nodeLayout = new FrameLayout(getContext());
            nodeLayout.setTag(index);
            nodeLayout.setElevation(6 * density);

            if (isCompleted) {
                nodeLayout.setBackgroundResource(R.drawable.pixel_node_completed);
            } else if (isCurrent) {
                nodeLayout.setBackgroundResource(R.drawable.pixel_node_current);
                ObjectAnimator animX = ObjectAnimator.ofFloat(nodeLayout, "scaleX", 1.0f, 1.1f, 1.0f);
                animX.setDuration(1200L);
                animX.setRepeatCount(ObjectAnimator.INFINITE);
                animX.setRepeatMode(ObjectAnimator.REVERSE);
                animX.start();

                ObjectAnimator animY = ObjectAnimator.ofFloat(nodeLayout, "scaleY", 1.0f, 1.1f, 1.0f);
                animY.setDuration(1200L);
                animY.setRepeatCount(ObjectAnimator.INFINITE);
                animY.setRepeatMode(ObjectAnimator.REVERSE);
                animY.start();

                activeAnimators.add(animX);
                activeAnimators.add(animY);
            } else {
                nodeLayout.setBackgroundResource(R.drawable.pixel_node_locked);
            }

            if (isCurrent) {
                TextView tvLevelNum = new TextView(getContext());
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.gravity = 17;
                tvLevelNum.setLayoutParams(lp);
                tvLevelNum.setText(String.valueOf(levelId));
                tvLevelNum.setTextSize(15.0f);
                Typeface font = ResourcesCompat.getFont(getContext(), R.font.pixelify_sans_family);
                if (font == null) {
                    font = Typeface.DEFAULT_BOLD;
                }
                tvLevelNum.setTypeface(font);
                tvLevelNum.setTextColor(Color.WHITE);
                tvLevelNum.setShadowLayer(3.0f, 2.0f, 2.0f, Color.parseColor("#7E3800"));
                nodeLayout.addView(tvLevelNum);
            }

            final int finalLevelId = levelId;
            final boolean finalIsLocked = isLocked;
            final int finalStars = stars;
            nodeLayout.setOnClickListener(v -> {
                if (onNodeClick != null) {
                    onNodeClick.onNodeClick(finalLevelId, !finalIsLocked, finalStars);
                }
            });
            addView(nodeLayout);
        }

        boolean allCompleted = maxUnlocked > 16;
        FrameLayout castleLayout = new FrameLayout(getContext());
        castleLayout.setTag(16);
        castleLayout.setElevation(8 * density);

        if (allCompleted) {
            castleLayout.setBackgroundResource(R.drawable.pixel_node_completed);
            ImageView starIv = new ImageView(getContext());
            int size = (int) (16 * density);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(size, size);
            lp.gravity = 17;
            starIv.setLayoutParams(lp);
            starIv.setImageResource(R.drawable.ic_star_golden);
            starIv.setContentDescription("Completado");
            castleLayout.addView(starIv);
        } else {
            castleLayout.setBackgroundResource(R.drawable.pixel_node_locked);
        }

        castleLayout.setOnClickListener(v -> {
            if (onCastleClick != null) {
                onCastleClick.run();
            }
        });
        addView(castleLayout);

=======
        int width = right - left;
        int height = bottom - top;
        backgroundImage.layout(0, 0, width, height);

        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            if (child == backgroundImage || child.getVisibility() == View.GONE) {
                continue;
            }

            Object tag = child.getTag();
            if (!(tag instanceof Integer)) {
                continue;
            }

            int coordinateIndex = (Integer) tag;
            if (coordinateIndex < 0 || coordinateIndex >= COORDINATES.size()) {
                continue;
            }

            NodeCoord coordinate = COORDINATES.get(coordinateIndex);
            int centerX = (int) (width * coordinate.xFraction);
            int centerY = (int) (height * coordinate.yFraction);
            int halfWidth = child.getMeasuredWidth() / 2;
            int halfHeight = child.getMeasuredHeight() / 2;
            child.layout(
                    centerX - halfWidth,
                    centerY - halfHeight,
                    centerX + halfWidth,
                    centerY + halfHeight
            );
        }
    }

    public void populateRoadmap(
            int maxUnlocked,
            StarsProvider starsProvider,
            LevelClickListener nodeClick,
            CastleClickListener castleClick
    ) {
        stopAnimations();
        removeLevelNodes();

        float density = getResources().getDisplayMetrics().density;
        for (int levelId = 1; levelId <= LEVEL_COUNT; levelId++) {
            int stars = starsProvider.getStars(levelId);
            boolean completed = levelId < maxUnlocked || stars > 0;
            boolean current = levelId == maxUnlocked && !completed;
            boolean locked = levelId > maxUnlocked;
            int selectedLevel = levelId;
            boolean selectedLevelLocked = locked;

            FrameLayout node = createNode(levelId - 1, density, completed, current);
            if (current) {
                addCurrentLevelLabel(node, levelId);
            }
            node.setOnClickListener(view ->
                    nodeClick.onLevelClick(selectedLevel, !selectedLevelLocked, stars));
            addView(node);
        }

        addCastleNode(maxUnlocked > LEVEL_COUNT, density, castleClick);
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        requestLayout();
        invalidate();
    }

<<<<<<< HEAD
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (ValueAnimator anim : activeAnimators) {
            anim.cancel();
        }
        activeAnimators.clear();
=======
    private FrameLayout createNode(int coordinateIndex, float density, boolean completed, boolean current) {
        FrameLayout node = new FrameLayout(getContext());
        node.setTag(coordinateIndex);
        node.setElevation((current ? 7 : 6) * density);

        if (completed) {
            node.setBackgroundResource(R.drawable.pixel_node_completed);
        } else if (current) {
            node.setBackgroundResource(R.drawable.pixel_node_current);
            addPulseAnimation(node);
        } else {
            node.setBackgroundResource(R.drawable.pixel_node_locked);
        }
        return node;
    }

    private void addPulseAnimation(View node) {
        ObjectAnimator scaleX = createPulse(node, "scaleX");
        ObjectAnimator scaleY = createPulse(node, "scaleY");
        scaleX.start();
        scaleY.start();
        activeAnimations.add(scaleX);
        activeAnimations.add(scaleY);
    }

    private ObjectAnimator createPulse(View node, String property) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(node, property, 1.0f, 1.1f, 1.0f);
        animator.setDuration(1200L);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        return animator;
    }

    private void addCurrentLevelLabel(FrameLayout node, int levelId) {
        TextView label = new TextView(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        params.gravity = 17;
        label.setLayoutParams(params);
        label.setText(String.valueOf(levelId));
        label.setTextSize(15.0f);
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.pixelify_sans_family);
        label.setTypeface(font != null ? font : Typeface.DEFAULT_BOLD);
        label.setTextColor(Color.WHITE);
        label.setShadowLayer(3.0f, 2.0f, 2.0f, Color.parseColor("#7E3800"));
        node.addView(label);
    }

    private void addCastleNode(
            boolean completed, float density, CastleClickListener castleClick) {
        FrameLayout castle = createNode(CASTLE_INDEX, density, completed, false);
        if (completed) {
            ImageView star = new ImageView(getContext());
            int size = (int) (16 * density);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
            params.gravity = 17;
            star.setLayoutParams(params);
            star.setImageResource(R.drawable.ic_star_golden);
            star.setContentDescription("Completado");
            castle.addView(star);
        }
        castle.setOnClickListener(view -> castleClick.onCastleClick());
        addView(castle);
    }

    private void stopAnimations() {
        for (ValueAnimator animation : activeAnimations) {
            animation.cancel();
        }
        activeAnimations.clear();
    }

    private void removeLevelNodes() {
        while (getChildCount() > 1) {
            removeViewAt(getChildCount() - 1);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimations();
        super.onDetachedFromWindow();
    }

    public static final class NodeCoord {
        private final float xFraction;
        private final float yFraction;

        public NodeCoord(float xFraction, float yFraction) {
            this.xFraction = xFraction;
            this.yFraction = yFraction;
        }

        public float getXFrac() {
            return xFraction;
        }

        public float getYFrac() {
            return yFraction;
        }
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
    }
}
