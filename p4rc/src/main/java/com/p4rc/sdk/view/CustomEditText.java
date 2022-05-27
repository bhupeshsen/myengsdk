package com.p4rc.sdk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.p4rc.sdk.R;

public class CustomEditText extends EditText {

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	
    private void init(AttributeSet attrs){
        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String fontName = styledAttrs.getString(R.styleable.CustomTextView_typeface);
        styledAttrs.recycle();

        setTypeface(fontName);    	
    }
    
    public void setTypeface(String fontName){
        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
            setTypeface(typeface);
        }  	
    }    
    
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
        if (isInEditMode()) {
            return;
        }
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if (focused){
			this.setTag(this.getHint()==null ? "" :this.getHint());
			try {
				this.setHint("");
			} catch (NullPointerException e) {
				// Workaround for app crashes on HTC Desire HD
			}
			showSoftKeyboard(CustomEditText.this);
		}
		else{
			try {
				this.setHint(this.getTag() ==null || !(this.getTag() instanceof CharSequence)  ? "" : (String)this.getTag());
			} catch (NullPointerException e) {
				// Workaround for app crashes on HTC Desire HD
			}
		}
	}
	

	private void showSoftKeyboard(View v) {
		((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(v, 0);
	}
}
