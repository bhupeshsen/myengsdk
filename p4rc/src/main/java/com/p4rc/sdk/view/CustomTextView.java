package com.p4rc.sdk.view;

import com.p4rc.sdk.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;


public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    
    private void init(AttributeSet attrs){
        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }
    	TypedArray styledAttrs = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String textHtml = styledAttrs.getString(R.styleable.CustomTextView_html);
        String fontName = styledAttrs.getString(R.styleable.CustomTextView_typeface);
        styledAttrs.recycle();
        setTextHtml(textHtml);
        setTypeface(fontName);    	
    }
    
    public void setTextHtml(String textHtml){
        if (textHtml != null) {
            setText(Html.fromHtml(textHtml));
        }  	
    }    

    public void setTypeface(String fontName){
        if (fontName != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
            setTypeface(typeface);
        }  	
    }    
    
}

