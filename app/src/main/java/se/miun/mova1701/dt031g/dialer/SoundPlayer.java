package se.miun.mova1701.dt031g.dialer;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

class SoundPlayer
{
    // static variable instance of type SoundPlayer
    private static SoundPlayer instance = null;
    private SoundPool soundPool;
    private boolean soundLoaded = false;
    private int sounds[] = new int[12];
    // private constructor restricted to this class itself
    private SoundPlayer(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .build();
        } else {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundLoaded = true;
            }
        });

        loadSound(context);

    }



    public void loadSound(Context context){
        if(!soundLoaded) {
            String externalStorageDirectory = android.os.Environment.getExternalStorageDirectory().getPath() + "/Dialer/Voices/";
            String directory = "mamacita_us/";
            sounds[0] = soundPool.load(externalStorageDirectory + directory + "zero.mp3", 1);
            sounds[1] = soundPool.load(externalStorageDirectory + directory + "one.mp3", 1);
            sounds[2] = soundPool.load(externalStorageDirectory + directory + "two.mp3", 1);
            sounds[3] = soundPool.load(externalStorageDirectory + directory + "three.mp3", 1);
            sounds[4] = soundPool.load(externalStorageDirectory + directory + "four.mp3", 1);
            sounds[5] = soundPool.load(externalStorageDirectory + directory + "five.mp3", 1);
            sounds[6] = soundPool.load(externalStorageDirectory + directory + "six.mp3", 1);
            sounds[7] = soundPool.load(externalStorageDirectory + directory + "seven.mp3", 1);
            sounds[8] = soundPool.load(externalStorageDirectory + directory + "eight.mp3", 1);
            sounds[9] = soundPool.load(externalStorageDirectory + directory + "nine.mp3", 1);
            sounds[10] = soundPool.load(externalStorageDirectory + directory + "star.mp3", 1);
            sounds[11] = soundPool.load(externalStorageDirectory + directory + "pound.mp3", 1);
        }
    }


    public void playSound(DialpadButton dpButton) {
        while(true) {
            if (soundLoaded) {
                switch(dpButton.getTitle()) {
                    case "0": soundPool.play(sounds[0], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "1": soundPool.play(sounds[1], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "2": soundPool.play(sounds[2], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "3": soundPool.play(sounds[3], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "4": soundPool.play(sounds[4], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "5": soundPool.play(sounds[5], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "6": soundPool.play(sounds[6], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "7": soundPool.play(sounds[7], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "8": soundPool.play(sounds[8], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "9": soundPool.play(sounds[9], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "*": soundPool.play(sounds[10], 1.0F, 1.0F, 0, 0, 1.0F); break;
                    case "#": soundPool.play(sounds[11], 1.0F, 1.0F, 0, 0, 1.0F); break;
                }
                break;
            }
        }
    }


    public void destroy() {
        soundPool.release();
        soundPool = null;
        instance = null;
    }

    // static method to create instance of SoundPlayer class
    public static SoundPlayer getInstance(Context context)
    {
        if (instance == null)
            instance = new SoundPlayer(context);

        return instance;
    }


    public boolean isSoundLoaded() {
        return soundLoaded;
    }
}
