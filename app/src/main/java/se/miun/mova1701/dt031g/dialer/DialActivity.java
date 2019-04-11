package se.miun.mova1701.dt031g.dialer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class DialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialpad);
        SoundPlayer.getInstance(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SoundPlayer.getInstance(this).destroy();
    }

}
