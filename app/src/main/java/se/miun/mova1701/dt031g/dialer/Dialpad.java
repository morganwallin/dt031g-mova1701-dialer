package se.miun.mova1701.dt031g.dialer;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public class Dialpad extends ConstraintLayout {
    Dialpad(Context context) {
        super(context);
    }

    public Dialpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.dialpad, this);
        //init(context, attrs, 0);
    }

    public Dialpad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dialpad, this);
       // init(context, attrs, defStyleAttr);
    }

}
