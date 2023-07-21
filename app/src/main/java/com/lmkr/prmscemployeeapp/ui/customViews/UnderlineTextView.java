package com.lmkr.prmscemployeeapp.ui.customViews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

public class UnderlineTextView extends androidx.appcompat.widget.AppCompatTextView {
    public UnderlineTextView(Context context) {
        super(context);
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public UnderlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public UnderlineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }


}
