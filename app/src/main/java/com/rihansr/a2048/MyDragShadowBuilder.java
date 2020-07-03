package com.rihansr.a2048;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class MyDragShadowBuilder extends View.DragShadowBuilder {

    private static Drawable shadow;

    public MyDragShadowBuilder(View v){
        super(v);
        shadow = new ColorDrawable(Color.parseColor("#00000000"));
    }

    @Override public void onProvideShadowMetrics(Point size, Point touch){
        int width, height;
        width = getView().getWidth();
        height = getView().getHeight();
        shadow.setBounds(0,0,width,height);
        size.set(width, height);
        touch.set(width,height);
    }

    @Override public void onDrawShadow(Canvas canvas){
        shadow.draw(canvas);
    }
}
