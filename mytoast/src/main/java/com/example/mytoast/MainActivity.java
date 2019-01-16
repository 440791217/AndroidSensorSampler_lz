package com.example.mytoast;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AudioManager mAudioManager;
    ComponentName mAudioComponentName;
    Context mContext;
    private MediaSession mSession;
    String Tag = "mark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        HeadSetUtil.getInstance().setOnHeadSetListener(headSetListener);
//        HeadSetUtil.getInstance().open(this);

        mContext = this;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioComponentName = new ComponentName(mContext, AudioBroadcast.class); //AudioBroadcast前面静态注册的广播
        mAudioManager.registerMediaButtonEventReceiver(mAudioComponentName);  //方法过时
    }

    @Override
    protected void onResume() {
        super.onResume();
        createMediaSession();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaSession();
    }

    //在OnResume中使用createMediaSession()方法create
    private void createMediaSession() {
        Log.v(Tag, "createMediaSession() mSession= " + mSession);
        if (mSession == null) {
            mSession = new MediaSession(mContext, MainActivity.class.getSimpleName());
            mSession.setCallback(mSessionCallback);
            mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS);
            mSession.setActive(true);
        }
    }

    //在OnPause中使用releaseMediaSession()方法release
    private void releaseMediaSession() {
        Log.v(Tag, "releaseMediaSession() mSession=" + mSession);
        if (mSession != null) {
            mSession.setCallback(null);
            mSession.setActive(false);
            mSession.release();
            mSession = null;
        }
    }

    private final MediaSession.Callback mSessionCallback = new MediaSession.Callback() {
        @Override
        public boolean onMediaButtonEvent(Intent mediaIntent) {
            if (mSession == null || mediaIntent == null) {
                Log.v(Tag, "SessionCallback mSession= " + mSession + "mediaIntent= " + mediaIntent);
                return false;
            }
            if (Intent.ACTION_MEDIA_BUTTON.equals(mediaIntent.getAction())) {
                KeyEvent event = (KeyEvent) mediaIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                Log.v(Tag, "SessionCallback event= " + event);
                if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onKeyDown(event.getKeyCode(), event);
                    onKeyUp(event.getKeyCode(), event);
                    return true;
                }
            }
            return false;
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeadSetUtil.getInstance().close(this);
    }

    HeadSetUtil.OnHeadSetListener headSetListener = new HeadSetUtil.OnHeadSetListener() {
        @Override
        public void onDoubleClick() {
            Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
            Log.i("ksdinf", "双击");
        }

        @Override
        public void onClick() {
            Toast.makeText(MainActivity.this, "1234", Toast.LENGTH_SHORT).show();
            Log.i("ksdinf", "单击");
        }

        @Override
        public void onThreeClick() {
            Toast.makeText(MainActivity.this, "1235", Toast.LENGTH_SHORT).show();
            Log.i("ksdinf", "三连击");
        }
    };

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("mark","123");
//        printToast(parseKeyCode(keyCode));
//        return true;
//    }
//
//    public String parseKeyCode(int keyCode) {
//        String ret = "";
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_POWER:
//                // 监控/拦截/屏蔽电源键 这里拦截不了
//                ret = "get Key KEYCODE_POWER(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_RIGHT_BRACKET:
//                // 监控/拦截/屏蔽返回键
//                ret = "get Key KEYCODE_RIGHT_BRACKET";
//                break;
//            case KeyEvent.KEYCODE_MENU:
//                // 监控/拦截菜单键
//                ret = "get Key KEYCODE_MENU";
//                break;
//            case KeyEvent.KEYCODE_HOME:
//                // 由于Home键为系统键，此处不能捕获
//                ret = "get Key KEYCODE_HOME";
//                break;
//            case KeyEvent.KEYCODE_DPAD_UP:
//                // 监控/拦截/屏蔽上方向键
//                ret = "get Key KEYCODE_DPAD_UP";
//                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                // 监控/拦截/屏蔽左方向键
//                ret = "get Key KEYCODE_DPAD_LEFT";
//                break;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                // 监控/拦截/屏蔽右方向键
//                ret = "get Key KEYCODE_DPAD_RIGHT";
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                // 监控/拦截/屏蔽下方向键
//                ret = "get Key KEYCODE_DPAD_DOWN";
//                break;
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                // 监控/拦截/屏蔽中方向键
//                ret = "get Key KEYCODE_DPAD_CENTER";
//                break;
//            case KeyEvent.FLAG_KEEP_TOUCH_MODE:
//                // 监控/拦截/屏蔽长按
//                ret = "get Key FLAG_KEEP_TOUCH_MODE";
//                break;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                // 监控/拦截/屏蔽下方向键
//                ret = "get Key KEYCODE_VOLUME_DOWN(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                // 监控/拦截/屏蔽中方向键
//                ret = "get Key KEYCODE_VOLUME_UP(KeyCode:" + keyCode + ")";
//                break;
//            case 220:
//                // case KeyEvent.KEYCODE_BRIGHTNESS_DOWN:
//                // 监控/拦截/屏蔽亮度减键
//                ret = "get Key KEYCODE_BRIGHTNESS_DOWN(KeyCode:" + keyCode + ")";
//                break;
//            case 221:
//                // case KeyEvent.KEYCODE_BRIGHTNESS_UP:
//                // 监控/拦截/屏蔽亮度加键
//                ret = "get Key KEYCODE_BRIGHTNESS_UP(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PLAY:
//                ret = "get Key KEYCODE_MEDIA_PLAY(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PAUSE:
//                ret = "get Key KEYCODE_MEDIA_PAUSE(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
//                ret = "get Key KEYCODE_MEDIA_PREVIOUS(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
//                ret = "get Key KEYCODE_MEDIA_PLAY_PAUSE(KeyCode:" + keyCode + ")";
//                break;
//            case KeyEvent.KEYCODE_MEDIA_NEXT:
//                ret = "get Key KEYCODE_MEDIA_NEXT(KeyCode:" + keyCode + ")";
//                break;
//            default:
//                ret = "keyCode: "
//                        + keyCode
//                        + " (http://developer.android.com/reference/android/view/KeyEvent.html)";
//                break;
//        }
//        return ret;
//    }
//
//    public void printToast(String str) {
//        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
//        Log.e("mark",str);
//    }
}
