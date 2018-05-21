package com.ekalips.cahscrowd.stuff.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.ekalips.cahscrowd.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ekalips on 6/19/17.
 */

public class HighlightableTextView extends AppCompatTextView {


    private static final int HIGHLIGHT_DEFAULT = 0;
    private static final int HIGHLIGHT_BOLD = 1;
    private static final int HIGHLIGHT_COLOR = 2;
    private static final int HIGHLIGHT_SIZE = 4;

    private String highlightText = "";
    private int highlightColor = Color.BLACK;
    private int highlightStyle = HIGHLIGHT_DEFAULT;
    private float highlightSize = 1.f;

    public HighlightableTextView(Context context) {
        super(context);
    }

    public HighlightableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public HighlightableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HighlightableTextView);
        highlightStyle = array.getInteger(R.styleable.HighlightableTextView_highlightStyle, HIGHLIGHT_DEFAULT);
        highlightText = array.getString(R.styleable.HighlightableTextView_highlightText);
        highlightSize = array.getFloat(R.styleable.HighlightableTextView_highlightSize, 1.f);
        highlightColor = array.getColor(R.styleable.HighlightableTextView_highlightColor, getCurrentTextColor());
        highlightText = highlightText == null ? "" : highlightText;
        array.recycle();
        invalidateHighlight();
    }

    public void setHighlightText(String highlightText) {
        this.highlightText = highlightText;
        force = true;
        invalidateHighlight();
    }

    public void setHighlightStyle(int highlightStyle) {
        this.highlightStyle = highlightStyle;
        force = true;
        invalidateHighlight();
    }

    @Override
    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
        force = true;
        invalidateHighlight();
    }

    public void setHighlightSize(float highlightSize) {
        this.highlightSize = highlightSize;
        invalidateHighlight();
    }

    String lastText;
    boolean force = false;

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        invalidateHighlight();
    }

    private void invalidateHighlight() {
        if (highlightText == null)
            return;
        String newText = getText() == null ? "" : getText().toString();
        if (TextUtils.isEmpty(newText)) return;
        boolean containsHighlightedText = newText.contains(highlightText);
        boolean textChanged = !newText.equals(lastText);
        if ((!containsHighlightedText || !textChanged) && !force)
            return;

        force = false;
        lastText = newText;
        SpannableStringBuilder builder = new SpannableStringBuilder(newText);
        Pattern pattern = Pattern.compile(highlightText);
        Matcher matcher = pattern.matcher(builder);

        boolean isBold = containsFlag(highlightStyle, HIGHLIGHT_BOLD);
        boolean isColor = containsFlag(highlightStyle, HIGHLIGHT_COLOR);
        boolean isSize = containsFlag(highlightStyle, HIGHLIGHT_SIZE);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start == end) continue;

            if (isBold) {
                builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (isColor) {
                builder.setSpan(new ForegroundColorSpan(highlightColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (isSize) {
                builder.setSpan(new RelativeSizeSpan(highlightSize), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(builder);
    }


    private boolean containsFlag(int flagSet, int flag) {
        return (flagSet | flag) == flagSet;
    }


}
