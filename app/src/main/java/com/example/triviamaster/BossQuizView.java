package com.example.triviamaster;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import androidx.core.content.res.ResourcesCompat;

import com.example.triviamaster.models.Question;

import java.util.Arrays;
import java.util.List;

public class BossQuizView extends FrameLayout {
    public interface OptionClickListener {
        void onOptionClick(int index);
    }

    private static final int ART_WIDTH = 941;
    private static final int ART_HEIGHT = 1672;
    private static final int PURPLE = Color.rgb(91, 57, 153);

    private final ImageView background;
    private final TextView questionText;
    private final List<TextView> optionViews;
    private final Typeface pixelFont;
    private final Handler animationHandler = new Handler(Looper.getMainLooper());
    private int frameIndex;
    private int[] frameResources = {
            R.drawable.boss_monster_1, R.drawable.boss_monster_2,
            R.drawable.boss_monster_3, R.drawable.boss_monster_4
    };
    private boolean artworkControls;
    private final Runnable frameAnimator = new Runnable() {
        @Override
        public void run() {
            setFrame(frameResources[frameIndex]);
            frameIndex = (frameIndex + 1) % frameResources.length;
            animationHandler.postDelayed(this, 420L);
        }
    };

    public BossQuizView(Context context) {
        this(context, null);
    }

    public BossQuizView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        pixelFont = ResourcesCompat.getFont(context, R.font.pixelify_sans);

        background = new ImageView(context);
        background.setScaleType(ImageView.ScaleType.FIT_XY);
        background.setContentDescription("Boss del nivel 5");
        background.setImageResource(R.drawable.boss_monster_1);
        addView(background);

        questionText = createTextView(context, 18);
        questionText.setGravity(Gravity.CENTER);
        questionText.setTextSize(15);
        questionText.setMaxLines(4);
        questionText.setEllipsize(null);
        questionText.setLineSpacing(0, 0.9f);
        addView(questionText);

        optionViews = Arrays.asList(
                createOption(context), createOption(context),
                createOption(context), createOption(context));
        for (int index = 0; index < optionViews.size(); index++) {
            TextView option = optionViews.get(index);
            final int optionIndex = index;
            option.setOnClickListener(view -> {
                if (getOptionClickListener() != null) {
                    getOptionClickListener().onOptionClick(optionIndex);
                }
            });
            addView(option);
        }
        animationHandler.post(frameAnimator);
    }

    private OptionClickListener optionClickListener;

    public void setOptionClickListener(OptionClickListener listener) {
        optionClickListener = listener;
    }

    public void configureBoss(int[] frames, boolean artworkControls, String description) {
        this.frameResources = frames;
        this.artworkControls = artworkControls;
        this.frameIndex = 0;
        background.setContentDescription(description);
        setFrame(frameResources[0]);
        questionText.setBackgroundResource(artworkControls
                ? android.R.color.transparent : R.drawable.bg_boss_question);
        for (TextView option : optionViews) {
            option.setBackgroundResource(artworkControls
                    ? android.R.color.transparent : R.drawable.bg_boss_option);
        }
        requestLayout();
    }

    private OptionClickListener getOptionClickListener() {
        return optionClickListener;
    }

    private TextView createTextView(Context context, int size) {
        TextView text = new TextView(context);
        text.setTextColor(PURPLE);
        text.setTextSize(size);
        text.setTypeface(pixelFont != null ? pixelFont : Typeface.DEFAULT_BOLD);
        text.setGravity(Gravity.CENTER);
        text.setIncludeFontPadding(true);
        text.setMaxLines(4);
        text.setEllipsize(TextUtils.TruncateAt.END);
        return text;
    }

    private TextView createOption(Context context) {
        TextView option = createTextView(context, 21);
        option.setTextColor(PURPLE);
        option.setTextSize(14);
        option.setMaxLines(1);
        option.setEllipsize(TextUtils.TruncateAt.END);
        option.setPadding(dp(context, 8), 0, dp(context, 4), 0);
        option.setClickable(true);
        option.setFocusable(true);
        option.setBackgroundResource(R.drawable.bg_boss_option);
        return option;
    }

    private int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }

    public void setQuestion(Question question) {
        questionText.setText(question.getQuestionText());
        questionText.setBackgroundResource(artworkControls
                ? android.R.color.transparent : R.drawable.bg_boss_question);
        for (int index = 0; index < optionViews.size(); index++) {
            optionViews.get(index).setText(
                    index < question.getOptions().size() ? question.getOptions().get(index) : "");
            optionViews.get(index).setVisibility(
                    index < question.getOptions().size() ? VISIBLE : GONE);
            optionViews.get(index).setTextColor(PURPLE);
            optionViews.get(index).setAlpha(1f);
            optionViews.get(index).setEnabled(true);
            optionViews.get(index).setBackgroundResource(artworkControls
                    ? android.R.color.transparent : R.drawable.bg_boss_option);
        }
    }

    public void setSelectedOption(int selectedIndex) {
        for (int index = 0; index < optionViews.size(); index++) {
            optionViews.get(index).setTextColor(index == selectedIndex
                    ? Color.rgb(222, 59, 91) : PURPLE);
            optionViews.get(index).setBackgroundResource(index == selectedIndex
                    ? (artworkControls ? R.drawable.bg_boss_option_selected
                    : R.drawable.bg_boss_option_selected)
                    : (artworkControls ? android.R.color.transparent
                    : R.drawable.bg_boss_option));
        }
    }

    public void showResult(int selectedIndex, int correctIndex, boolean correct) {
        for (int index = 0; index < optionViews.size(); index++) {
            optionViews.get(index).setEnabled(false);
            if (index == correctIndex) {
                optionViews.get(index).setTextColor(Color.rgb(46, 170, 100));
                optionViews.get(index).setBackgroundResource(artworkControls
                        ? R.drawable.bg_boss_option_correct : R.drawable.bg_boss_option_correct);
            } else if (index == selectedIndex && !correct) {
                optionViews.get(index).setTextColor(Color.rgb(222, 59, 91));
                optionViews.get(index).setBackgroundResource(artworkControls
                        ? R.drawable.bg_boss_option_wrong : R.drawable.bg_boss_option_wrong);
            } else {
                optionViews.get(index).setBackgroundResource(artworkControls
                        ? android.R.color.transparent : R.drawable.bg_boss_option);
            }
        }
    }

    public void setFrame(int frameResource) {
        background.setImageResource(frameResource);
    }

    @Override
    protected void onDetachedFromWindow() {
        animationHandler.removeCallbacks(frameAnimator);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = Math.round(width * ART_HEIGHT / (float) ART_WIDTH);
        int exactWidth = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int exactHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        background.measure(exactWidth, exactHeight);
        float questionX = artworkControls ? 0.13f : 0.10f;
        float questionY = artworkControls ? 0.535f : 0.515f;
        float questionWidth = artworkControls ? 0.74f : 0.80f;
        float questionHeight = artworkControls ? 0.105f : 0.135f;
        measureChildForFraction(questionText, width, height,
                questionX, questionY, questionWidth, questionHeight);
        for (int index = 0; index < optionViews.size(); index++) {
            float x = index % 2 == 0
                    ? (artworkControls ? 0.16f : 0.125f)
                    : (artworkControls ? 0.515f : 0.505f);
            float y = index < 2
                    ? (artworkControls ? 0.655f : 0.645f)
                    : (artworkControls ? 0.713f : 0.705f);
            float optionWidth = artworkControls ? 0.33f : 0.37f;
            float optionHeight = artworkControls ? 0.048f : 0.058f;
            measureChildForFraction(optionViews.get(index), width, height,
                    x, y, optionWidth, optionHeight);
        }
        setMeasuredDimension(width, height);
    }

    private void measureChildForFraction(View child, int width, int height,
                                         float x, float y, float childWidth, float childHeight) {
        child.measure(
                MeasureSpec.makeMeasureSpec(Math.round(width * childWidth), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(Math.round(height * childHeight), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        background.layout(0, 0, width, height);
        layoutChild(questionText, width, height,
                artworkControls ? 0.13f : 0.10f,
                artworkControls ? 0.535f : 0.515f);
        for (int index = 0; index < optionViews.size(); index++) {
            float x = index % 2 == 0
                    ? (artworkControls ? 0.16f : 0.125f)
                    : (artworkControls ? 0.515f : 0.505f);
            float y = index < 2
                    ? (artworkControls ? 0.655f : 0.645f)
                    : (artworkControls ? 0.713f : 0.705f);
            layoutChild(optionViews.get(index), width, height, x, y);
        }
    }

    private void layoutChild(View child, int width, int height, float x, float y) {
        int childLeft = Math.round(width * x);
        int childTop = Math.round(height * y);
        child.layout(childLeft, childTop,
                childLeft + child.getMeasuredWidth(),
                childTop + child.getMeasuredHeight());
    }
}
