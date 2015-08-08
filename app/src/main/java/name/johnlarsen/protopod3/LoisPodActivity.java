package name.johnlarsen.protopod3;

import name.johnlarsen.protopod3.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LoisPodActivity extends FragmentActivity {

    public static final String FRAGTAG = "ImmersiveModeFragment";

    /**
     * The URL we suggest as default when adding by URL. This is just so that the user doesn't
     * have to find an URL to test this sample.
     */
    final String SUGGESTED_URL = "http://www.vorbis.com/music/Epoq-Lepidoptera.ogg";

    Button mPlayButton;
    Button mPauseButton;
    Button mSkipButton;
    Button mStopButton;
    Button mOtherPlayButton;
    TextView mCurrentSong;

    Activity mActivity;

    ImmersiveModeFragment fragment;

    BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        setContentView(R.layout.activity_lois_pod);


        if (getSupportFragmentManager().findFragmentByTag(FRAGTAG) == null ) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new ImmersiveModeFragment();
            transaction.add(fragment, FRAGTAG);
            transaction.commit();
        }



        // Set up the user interaction to manually show or hide the system UI.
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Send the correct intent to the MusicService, according to the button that was clicked
                if (view == mPlayButton) {
                    Intent i = new Intent(mActivity, MusicService.class);
                    i.setAction(MusicService.ACTION_PLAY);
                    startService(i);
                }
                else if (view == mStopButton) {
                    Intent i = new Intent(mActivity, MusicService.class);
                    i.setAction(MusicService.ACTION_STOP);
                    startService(i);
                    mCurrentSong.setText("");
                }
            }
        };

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String currentSong = intent.getStringExtra(MusicService.MSG_SONG_TITLE);
                if (currentSong != null)
                    mCurrentSong.setText(currentSong);
                else
                    mCurrentSong.setText("Title Not Found!");
            }
        };

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        findViewById(R.id.playButton).setOnTouchListener(mDelayHideTouchListener);


        mPlayButton = (Button) findViewById(R.id.playButton);
        mStopButton = (Button) findViewById(R.id.stopButton);
        mCurrentSong = (TextView) findViewById(R.id.currentSong);

        mPlayButton.setOnClickListener(onClickListener);
        mStopButton.setOnClickListener(onClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fragment != null)
            fragment.toggleHideyBar();
        mCurrentSong.setText("");
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter(MusicService.EVENT_NEW_SONG));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                startService(new Intent(MusicService.ACTION_TOGGLE_PLAYBACK));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

/*
    @Override
   protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }
*/


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
/*
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
*/

/*
    Handler mHideHandler = new Handler();

    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };
*/

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    /*
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    */
}
