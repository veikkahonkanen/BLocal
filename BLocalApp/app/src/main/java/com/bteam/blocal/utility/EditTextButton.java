package com.bteam.blocal.utility;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class EditTextButton {
    public static void setOnRightDrawableClicked(EditText edt, View.OnClickListener listener){
        // Obtained from https://stackoverflow.com/a/26269435
        edt.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edt.getRight() -
                            edt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        listener.onClick(v);
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
