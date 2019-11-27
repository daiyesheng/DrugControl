package com.example.drugcontrol.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.drugcontrol.R;


public class ClearEditText extends AppCompatEditText implements OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;
    private boolean hasFoucs;
    private Paint mPaint;
    private int currentSize = 1;
    public boolean isUnderline;//是否显示下划线
    public boolean isClearIconShow; //是否显示清除按钮
    public int colorUnderline;
    private int mDrawableWidth;
    private int mDrawableHeight;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画底线
        if (isUnderline) {
            canvas.drawLine(0, this.getHeight() - 5, this.getWidth(), this.getHeight() - 5, mPaint);
        }
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.clearEditText);
        isUnderline = array.getBoolean(R.styleable.clearEditText_edittext_underline, true);//是否设置编辑框的下划线,默认设置
        isClearIconShow = array.getBoolean(R.styleable.clearEditText_edittext_clear_icon, true);//是否设显示清除按钮,默认设置
        colorUnderline = array.getColor(R.styleable.clearEditText_edittext_underline_color, Color.parseColor("#e6e6e6"));//是否设显示清除按钮,默认设置
        mDrawableWidth = array.getDimensionPixelSize(R.styleable.clearEditText_compoundDrawableWidth, -1);
        mDrawableHeight = array.getDimensionPixelSize(R.styleable.clearEditText_compoundDrawableHeight, -1);

        array.recycle();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(colorUnderline);
        mPaint.setStrokeWidth(currentSize);

        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.ic_clear);
        }
        if (mDrawableWidth > 0 || mDrawableHeight > 0) {
            mClearDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
            //reset left top right bottom drawable size
            initCompoundDrawableSize();
        } else {
            mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        }

        setClearIconVisible(false);

        setOnFocusChangeListener(this);

        addTextChangedListener(this);

    }

    private void initCompoundDrawableSize() {
        Drawable[] drawables = getCompoundDrawables();
        for (Drawable drawable : drawables) {
            if (drawable == null) {
                continue;
            }
            Rect realBounds = drawable.getBounds();
            float scaleFactor = realBounds.height() / (float) realBounds.width();

            float drawableWidth = realBounds.width();
            float drawableHeight = realBounds.height();

            if (mDrawableWidth > 0) {
                // save scale factor of image
                if (drawableWidth > mDrawableWidth) {
                    drawableWidth = mDrawableWidth;
                    drawableHeight = drawableWidth * scaleFactor;
                }
            }
            if (mDrawableHeight > 0) {
                // save scale factor of image
                if (drawableHeight > mDrawableHeight) {
                    drawableHeight = mDrawableHeight;
                    drawableWidth = drawableHeight / scaleFactor;
                }
            }

            realBounds.right = realBounds.left + Math.round(drawableWidth);
            realBounds.bottom = realBounds.top + Math.round(drawableHeight);

            drawable.setBounds(realBounds);
        }
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        Canvas canvas = new Canvas();
        if (hasFocus) {
            if (isClearIconShow) {
                setClearIconVisible(getText().length() > 0);
            }
            // 画底线
            if (isUnderline) {
                mPaint.setColor(Color.parseColor("#178FF2"));
                canvas.drawLine(0, this.getHeight() - 10, this.getWidth() - 20, this.getHeight() - 10, mPaint);
            }
        } else {
            setClearIconVisible(false);
            // 画底线
            if (isUnderline) {
                mPaint.setColor(Color.parseColor("#e6e6e6"));
                canvas.drawLine(0, this.getHeight() - 10, this.getWidth() - 20, this.getHeight() - 10, mPaint);
            }
        }
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if (hasFoucs) {
            if (isClearIconShow) {
                setClearIconVisible(s.length() > 0);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void setShakeAnimation() {
        this.setAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

}