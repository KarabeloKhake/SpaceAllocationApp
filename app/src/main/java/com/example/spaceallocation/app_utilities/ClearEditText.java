package com.example.spaceallocation.app_utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.spaceallocation.R;
import java.util.Objects;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class ClearEditText extends AppCompatEditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    //Data Members
    private Drawable mClearTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;

    //Constructors
    public ClearEditText(final Context context) {
        super(context);
        init(context);
    } //end overloaded constructor

    public ClearEditText(final Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    } //end overloaded constructor

    public ClearEditText(final Context context, final AttributeSet attributeSet, final int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context);
    } //end overloaded constructor

    //Custom Methods
    private void init(final Context context) {
        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_cancel);
        assert drawable != null;
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        //this wraps the drawable so that it is tinted pre Lollipop, also optional
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearTextIcon = wrappedDrawable;
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicWidth(), mClearTextIcon.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    } //end init()

    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    } //end setClearIconVisible()

    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    } //end setOnFocusChangeListener()

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    } //end setOnTouchListener()

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setClearIconVisible(s.length() > 0);
        } //end if
    } //end onTextChanged()

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(Objects.requireNonNull(getText()).length() > 0);
        } //end if
        else {
            setClearIconVisible(false);
        } //end else

        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        } //end if
    } //end onFocusChange()

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int x = (int) event.getX();

        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            } //end if
            return true;
        } //end if
        return mOnTouchListener != null && mOnTouchListener.onTouch(v, event);
    } //end onTouch()

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { } //end beforeTextChanged

    @Override
    public void afterTextChanged(Editable s) { } //end afterTextChanged()
} //end class ClearEditText
