package com.it.mougang.gasmyr.takecare.utils.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.it.mougang.gasmyr.takecare.utils.Utils;

/**
 * Created by gamyr on 11/1/16.
 */

public class MyBadge extends TextView {
    private Typeface typeface;
    public MyBadge(@NonNull Context context) {
        super(context);
        typeface= Utils.getCampagneFont(context);
        this.setTypeface(typeface);
    }

    public MyBadge(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        typeface= Utils.getCampagneFont(context);
        this.setTypeface(typeface);
    }

    public MyBadge(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        typeface= Utils.getCampagneFont(context);
        this.setTypeface(typeface);
    }



}
