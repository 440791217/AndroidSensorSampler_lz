package com.example.mytoast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class AudioBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        Log.e("mark",intentAction);
        String str = "";
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
// 获得按键字节码  
            int keyCode = keyEvent.getKeyCode();
// 按下 / 松开 按钮  
            int keyAction = keyEvent.getAction();
            // 获得事件的时间  
            long downtime = keyEvent.getEventTime();

            switch (keyCode) {
                case KeyEvent.KEYCODE_MEDIA_NEXT:
//sendAction(context,"next");
                    str = "下一曲-按键";
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    str = "上一曲-按键";
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    str = "暂停-按键";
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    str = "播放-按键";
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    str = "播放暂停键";
                    break;
            }
        }
    }
}
