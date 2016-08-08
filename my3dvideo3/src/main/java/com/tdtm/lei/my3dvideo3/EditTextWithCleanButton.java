package com.tdtm.lei.my3dvideo3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by lei on 6/14/16.
 */
public class EditTextWithCleanButton extends EditText {

    private Drawable drawableRight;
    private Drawable drawableLeft;
    int actionX, actionY;
    public EditTextWithCleanButton(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        //super();
    }
    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {

        if (right != null) {
            drawableRight = right;
        }
        if (left != null){
            drawableLeft = left;
        }

        super.setCompoundDrawables(left, top, right, bottom);
    }

    public EditTextWithCleanButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();

            if (drawableRight != null) {

                bounds = null;
                bounds = drawableRight.getBounds();

                int x, y;
                int extraTapArea = 0;//13;

                /**
                 * IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                 * THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                 * CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                 * BOUND. - this process help to increase the tappable area of
                 * the rectangle.
                 */
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);

                /**Since this is right drawable subtract the value of x from the width
                 * of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                 /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if(x <= 0){
                    x += extraTapArea;
                }

                 /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extratapping area value doesn't go into negative value.
                 */

                if (y <= 0)
                    y = actionY;

                /**If drawble bounds contains the x and y points then move ahead.*/
            /*
                if (bounds.contains(x, y) && clickListener != null) {
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                */

                int e = 30;

                bounds = new Rect(bounds.left - e,bounds.top - e, bounds.right + e, bounds.bottom + e);
                if (bounds.contains(x, y)) {
                    this.setText("");
                    this.setCompoundDrawables(drawableLeft,null,null,null);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                return super.onTouchEvent(event);
            }
        }
        return true;
    }
}
