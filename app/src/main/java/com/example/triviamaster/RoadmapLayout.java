package com.example.triviamaster;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RoadmapLayout extends FrameLayout {

    public static final int HOTSPOT_MONSTER_START = 0;
    public static final int HOTSPOT_CAVE = 1;
    public static final int HOTSPOT_MONSTER_RIVER = 2;
    public static final int HOTSPOT_MONSTER_LAKE = 3;
    public static final int HOTSPOT_MONSTER_WATERFALL = 4;
    public static final int HOTSPOT_MONSTER_MOUNTAIN = 5;

    public interface StarsProvider {
        int getStars(int levelId);
    }

    public interface OnNodeClickListener {
        void onNodeClick(int levelId, boolean isUnlocked, int stars);
    }

    public interface OnHotspotClickListener {
        void onHotspotClick(int hotspotId);
    }

    public interface SpecialCompletedProvider {
        boolean isAllCompleted(int hotspotId);
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

    private static final class HotspotSpec {
        final int id;
        final float xFrac;
        final float yFrac;
        final float sizeFrac;
        final boolean showAlertBadge;

        HotspotSpec(int id, float xFrac, float yFrac, float sizeFrac, boolean showAlertBadge) {
            this.id = id;
            this.xFrac = xFrac;
            this.yFrac = yFrac;
            this.sizeFrac = sizeFrac;
            this.showAlertBadge = showAlertBadge;
        }
    }

    public static final List<NodeCoord> COORDS = Arrays.asList(
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

    private static final List<HotspotSpec> HOTSPOTS = Arrays.asList(
            new HotspotSpec(HOTSPOT_MONSTER_START, 0.42f, 0.025f, 0.15f, true),
            new HotspotSpec(HOTSPOT_CAVE, 0.85f, 0.125f, 0.22f, true),
            new HotspotSpec(HOTSPOT_MONSTER_RIVER, 0.19f, 0.245f, 0.18f, true),
            new HotspotSpec(HOTSPOT_MONSTER_LAKE, 0.20f, 0.395f, 0.18f, true),
            new HotspotSpec(HOTSPOT_MONSTER_WATERFALL, 0.87f, 0.395f, 0.18f, true),
            new HotspotSpec(HOTSPOT_MONSTER_MOUNTAIN, 0.84f, 0.545f, 0.18f, true)
    );

    private static final int HOTSPOT_TAG_OFFSET = 100;

    private final ImageView bgImageView;
    private final List<ValueAnimator> activeAnimators = new ArrayList<>();

    public RoadmapLayout(Context context) {
        this(context, null);
    }

    public RoadmapLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoadmapLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bgImageView = new ImageView(context);
        bgImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        bgImageView.setImageResource(R.drawable.img_roadmap_full);
        bgImageView.setContentDescription("Roadmap");
        addView(bgImageView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
                Object tag = child.getTag();
                if (tag instanceof Integer && (Integer) tag >= HOTSPOT_TAG_OFFSET) {
                    int hotspotIdx = (Integer) tag - HOTSPOT_TAG_OFFSET;
                    if (hotspotIdx >= 0 && hotspotIdx < HOTSPOTS.size()) {
                        int hotspotSize = (int) (width * HOTSPOTS.get(hotspotIdx).sizeFrac);
                        int hotspotSpec = View.MeasureSpec.makeMeasureSpec(hotspotSize, View.MeasureSpec.EXACTLY);
                        child.measure(hotspotSpec, hotspotSpec);
                        continue;
                    }
                }
                child.measure(nodeSpec, nodeSpec);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
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
                    if (tagIndex >= HOTSPOT_TAG_OFFSET) {
                        int hotspotIdx = tagIndex - HOTSPOT_TAG_OFFSET;
                        if (hotspotIdx >= 0 && hotspotIdx < HOTSPOTS.size()) {
                            HotspotSpec spec = HOTSPOTS.get(hotspotIdx);
                            int cx = (int) (w * spec.xFrac);
                            int cy = (int) (h * spec.yFrac);
                            int halfW = child.getMeasuredWidth() / 2;
                            int halfH = child.getMeasuredHeight() / 2;
                            child.layout(cx - halfW, cy - halfH, cx + halfW, cy + halfH);
                        }
                    } else if (tagIndex >= 0 && tagIndex < COORDS.size()) {
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

    public void populateRoadmap(int maxUnlocked, StarsProvider starsProvider,
                                OnNodeClickListener onNodeClick, Runnable onCastleClick) {
        populateRoadmap(maxUnlocked, starsProvider, onNodeClick, onCastleClick, null);
    }

    public void populateRoadmap(int maxUnlocked, StarsProvider starsProvider,
                                OnNodeClickListener onNodeClick, Runnable onCastleClick,
                                OnHotspotClickListener onHotspotClick) {
        populateRoadmap(maxUnlocked, starsProvider, onNodeClick, onCastleClick, onHotspotClick, null);
    }

    public void populateRoadmap(int maxUnlocked, StarsProvider starsProvider,
                                OnNodeClickListener onNodeClick, Runnable onCastleClick,
                                OnHotspotClickListener onHotspotClick,
                                SpecialCompletedProvider specialCompletedProvider) {
        for (ValueAnimator anim : activeAnimators) {
            anim.cancel();
        }
        activeAnimators.clear();

        while (getChildCount() > 1) {
            removeViewAt(getChildCount() - 1);
        }

        float density = getResources().getDisplayMetrics().density;
        Typeface pixelFont = ResourcesCompat.getFont(getContext(), R.font.pixelify_sans_family);
        if (pixelFont == null) {
            pixelFont = Typeface.DEFAULT_BOLD;
        }

        for (int i = 0; i < HOTSPOTS.size(); i++) {
            HotspotSpec spec = HOTSPOTS.get(i);
            FrameLayout hotspotView = new FrameLayout(getContext());
            hotspotView.setTag(HOTSPOT_TAG_OFFSET + i);
            hotspotView.setClickable(true);
            hotspotView.setFocusable(true);

            boolean allSpecialDone = specialCompletedProvider != null
                    && specialCompletedProvider.isAllCompleted(spec.id);
            if (spec.showAlertBadge && !allSpecialDone) {
                TextView badge = new TextView(getContext());
                int badgeSize = (int) (22 * density);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(badgeSize, badgeSize);
                lp.gravity = Gravity.TOP | Gravity.END;
                lp.topMargin = (int) (4 * density);
                lp.rightMargin = (int) (4 * density);
                badge.setLayoutParams(lp);
                badge.setBackgroundResource(R.drawable.bg_badge_gift_alert);
                badge.setText("!");
                badge.setTextColor(Color.WHITE);
                badge.setTextSize(12.0f);
                badge.setTypeface(pixelFont);
                badge.setGravity(Gravity.CENTER);
                hotspotView.addView(badge);

                ObjectAnimator pulseX = ObjectAnimator.ofFloat(badge, "scaleX", 1.0f, 1.18f, 1.0f);
                pulseX.setDuration(1400L);
                pulseX.setRepeatCount(ObjectAnimator.INFINITE);
                pulseX.start();
                ObjectAnimator pulseY = ObjectAnimator.ofFloat(badge, "scaleY", 1.0f, 1.18f, 1.0f);
                pulseY.setDuration(1400L);
                pulseY.setRepeatCount(ObjectAnimator.INFINITE);
                pulseY.start();
                activeAnimators.add(pulseX);
                activeAnimators.add(pulseY);
            }

            final int hotspotId = spec.id;
            hotspotView.setOnClickListener(v -> {
                if (onHotspotClick != null) {
                    onHotspotClick.onHotspotClick(hotspotId);
                }
            });
            addView(hotspotView);
        }

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
                tvLevelNum.setTypeface(pixelFont);
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

        requestLayout();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (ValueAnimator anim : activeAnimators) {
            anim.cancel();
        }
        activeAnimators.clear();
    }
}
