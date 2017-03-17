package com.dbkudryavtsev.ccw.childrencrosswords.main.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.dbkudryavtsev.ccw.childrencrosswords.main.Activities.CrosswordActivity;
import com.dbkudryavtsev.ccw.childrencrosswords.main.R;
import com.dbkudryavtsev.ccw.childrencrosswords.main.Utilities.JSONInteraction;

public class ChoiceView extends View {
    public final static String chosenRectString = "chosenRect";
    private Drawable levelRect;
    private Paint white = new Paint();
    private int MAX_FILL;
    private  int margin;
    private int wordHeight;
    private Rect rects[];

    public ChoiceView(Context context) {
        super(context);
        //JSONInteraction.createResourceFiles(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        MAX_FILL = 4;
        margin = (int) (getWidth() * .015);
        wordHeight =(int) (getWidth() / MAX_FILL -  1.2*margin);
        white.setColor(ContextCompat.getColor(getContext(), R.color.white));
        white.setStyle(Paint.Style.FILL);
        white.setTextSize(wordHeight);
        levelRect = ContextCompat.getDrawable(getContext(), R.drawable.square);
        rects = new Rect[R.raw.class.getFields().length];
        for (int i = 0; i < rects.length; i++) {
            rects[i] = new Rect(margin * (i + 1) + i * wordHeight, margin, margin * (i + 1) + i * wordHeight + wordHeight, margin + wordHeight);
        }
    }

    private Rect letterBounds = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int horizontal_step, vertical_step;
        for (int i = 0; i < rects.length; i++) {
            levelRect.setBounds(rects[i]);
            levelRect.draw(canvas);
            white.getTextBounds(Integer.toString(i+1), 0, 1, letterBounds);
            horizontal_step = (int) (wordHeight -
                    white.measureText(Integer.toString(i+1))) / 2;
            vertical_step = wordHeight - (wordHeight - letterBounds.height()) / 2;
            canvas.drawText(Integer.toString(i+1),
                   rects[i].left + horizontal_step, rects[i].top + vertical_step, white);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int chosenRect = -1;
        for (int i = 0; i < rects.length; i++) {
            if (rects[i].contains((int) event.getX(), (int) event.getY())) {
                chosenRect = i;
                break;
            }
        }
        chosenRect++;
        if(chosenRect>0) {
            Intent intent = new Intent(this.getContext(), CrosswordActivity.class);
            try {
                intent.putExtra(chosenRectString, getResources().getIdentifier("crossword"+chosenRect,
                        "raw", getContext().getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            getContext().startActivity(intent);
        }
        return super.onTouchEvent(event);
    }
}