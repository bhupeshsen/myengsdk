package com.p4rc.sdk.view;

import com.p4rc.sdk.R;
import com.p4rc.sdk.utils.AppUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

	  public CustomButton(Context context) {
	    super(context);
	  }

	  public CustomButton(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init(attrs);
	  }

	  public CustomButton(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    init(attrs);
	  }
	  
	  private void init(AttributeSet attrs){
	        if (isInEditMode()) {
	            return;
	        }

	        TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
	        String fontName = styledAttrs.getString(R.styleable.CustomTextView_typeface);
	        styledAttrs.recycle();

	        setTypeface(fontName);
	        
	        styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton);
	        
	        boolean shadow = styledAttrs.getBoolean(R.styleable.CustomButton_shadow, false);
	        styledAttrs.recycle();
	        
	        setShadow(shadow);
	  }

	  public void setShadow(boolean shadow){
	        if (shadow) {
	        	float radius = AppUtils.convertDpToPixel(1, getContext());
	        	float dx = 0;
	        	float dy = AppUtils.convertDpToPixel(-2, getContext());;
	        	setShadowLayer(radius, dx, dy, Color.argb(0x66, 0, 0, 0)); // 40% alfa from black
	        }
	  }   

	  public void setTypeface(String fontName){
	        if (fontName != null) {
	            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
	            setTypeface(typeface);
	        }  	
	  }   

	  @SuppressWarnings("deprecation")
	  @Override
	  public void setBackgroundDrawable(Drawable d) {
	    // Replace the original background drawable (e.g. image) with a LayerDrawable that
	    // contains the original drawable.
		CustomButtonBackgroundDrawable layer = new CustomButtonBackgroundDrawable(d);
	    super.setBackgroundDrawable(layer);
	  }

	  /**
	   * The stateful LayerDrawable used by this button.
	   */
	  protected class CustomButtonBackgroundDrawable extends LayerDrawable {

	    // The color filter to apply when the button is pressed
	    protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
	    // Alpha value when the button is disabled
	    protected int _enabledAlpha = 255;
	    protected int _disabledAlpha = 100;

	    public CustomButtonBackgroundDrawable(Drawable d) {
	      super(new Drawable[] { d });
	    }

	    @Override
	    protected boolean onStateChange(int[] states) {
	      boolean enabled = false;
	      boolean pressed = false;

	      for (int state : states) {
	        if (state == android.R.attr.state_enabled)
	          enabled = true;
	        else if (state == android.R.attr.state_pressed)
	          pressed = true;
	      }

	      mutate();
	      if (enabled && pressed) {
	        setColorFilter(_pressedFilter);
	      } else 
    	  if (!enabled) {
	        setColorFilter(null);
	        setAlpha(_disabledAlpha);
	      } else {
	        setColorFilter(null);
	        setAlpha(_enabledAlpha);

	      }

	      invalidateSelf();

	      return super.onStateChange(states);
	    }

	    @Override
	    public boolean isStateful() {
	      return true;
	    }
	  }
	}

