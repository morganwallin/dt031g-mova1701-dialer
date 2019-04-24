package se.miun.mova1701.dt031g.dialer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DialpadButton extends ConstraintLayout implements View.OnTouchListener {
    public TextView title;
    public TextView message;

    public DialpadButton(Context context) {
        super(context);
        init(context, null, 0);

    }

    public DialpadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public DialpadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.setOnTouchListener(this);

        //Inflate XML
        inflate(getContext(), R.layout.dialpadbutton, this);

        title = (TextView) findViewById(R.id.dialpadTitle);
        message = (TextView) findViewById(R.id.dialpadMessage);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DialpadButton);

            String messageString = ta.getString(R.styleable.DialpadButton_message);
            setMessage(messageString);

            String titleString = ta.getString(R.styleable.DialpadButton_title);
            setTitle(titleString);

            ta.recycle();
            invalidate();
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean useSound = DialActivity.getUseSound();
            this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.animation));
            if (useSound && SoundPlayer.getInstance(getContext()).isSoundLoaded()) {
                SoundPlayer.getInstance(getContext()).playSound(this);
            }

            View parent = (View)v.getRootView();
            if (parent != null) {
                TextView textView = parent.findViewById(R.id.phoneNumberEditText);
                textView.append(this.getTitle());
            }

        }
        return true;
    }

        public void setTitle (String titleString){
            if (titleString.length() != 1) {
                //Get first char of the string
                titleString = titleString.substring(0, 1);

                //Create objects to compare the titleString with
                List<String> acceptableStrings = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "#", "*");
                ArrayList<String> stringList = new ArrayList<>();

                //Add them to an ArrayList
                stringList.addAll(acceptableStrings);

                //Check if titleString is 1-9, * or #, if not it defaults to #
                for (Object it : stringList) {
                    if (!titleString.equals(it)) {
                        titleString = "#";
                    }
                }
            }
            title.setText(titleString);
            invalidate();
            requestLayout();
        }
        public void setMessage (String messageString){
            if (messageString.length() > 4) {
                messageString = messageString.substring(0, 4);
            }
            message.setText(messageString);
            invalidate();
            requestLayout();
        }

        public String getTitle () {
            return title.getText().toString();
        }
}

