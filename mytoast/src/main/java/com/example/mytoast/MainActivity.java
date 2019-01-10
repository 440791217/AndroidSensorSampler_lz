package com.example.mytoast;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MediaButtonReceiver mediaButtonReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化Handler对象
        MyHandler handler = new MyHandler();
//初始化媒体(耳机)广播对象.
         mediaButtonReceiver = new MediaButtonReceiver(handler);
//注册媒体(耳机)广播对象
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        intentFilter.setPriority(100);
        registerReceiver(mediaButtonReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mediaButtonReceiver);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//
//                Toast.makeText(MainActivity.this,"-",Toast.LENGTH_SHORT).show();
//
//                return true;
//
//            case KeyEvent.KEYCODE_MEDIA_PREVIOUS://耳机三个按键是的上键，注意并不是耳机上的三个按键的物理位置的上下。
//                //按键功能定义的处理。
//                //一般把功能定义为
//                //音乐：播放上一首
//                //视频：播放上一个视频或后退。
//                //收音机：向上搜索。
//                Toast.makeText(MainActivity.this,"KEYCODE_MEDIA_PREVIOUS",Toast.LENGTH_SHORT).show();
//                return true;
//            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://耳机单按键的按键或三按键耳机的中间按键。
//                Toast.makeText(MainActivity.this,"KEYCODE_MEDIA_PLAY_PAUSE",Toast.LENGTH_SHORT).show();
//                return true;
//            case KeyEvent.KEYCODE_HEADSETHOOK://耳机单按键的按键或三按键耳机的中间按键。与上面的按键可能是相同的，具体得看驱动定义。
//                //按键功能定义的处理。一般与KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE、键的处理一样。
//                //一般把功能定义为：音乐播放和暂停。
//                Toast.makeText(MainActivity.this,"KEYCODE_HEADSETHOOK",Toast.LENGTH_SHORT).show();
//                return true;
//            case KeyEvent.KEYCODE_MEDIA_NEXT://耳机三个按键是的下键。
//                //按键功能定义的处理。
//                //一般把功能定义为：
//                //音乐：播放下一首。
//                //视频：播放下一个视频或前进。
//                Toast.makeText(MainActivity.this,"KEYCODE_MEDIA_NEXT",Toast.LENGTH_SHORT).show();
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                Toast.makeText(MainActivity.this,"+",Toast.LENGTH_SHORT).show();
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_MUTE:
//                Toast.makeText(MainActivity.this,"KEYCODE_VOLUME_MUTE",Toast.LENGTH_SHORT).show();
//
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch(what){
                case 100://单击按键广播
                    Bundle data = msg.getData();
                    //按键值
                    int keyCode = data.getInt("key_code");
                    //按键时长
                    long eventTime = data.getLong("event_time");
                    //设置超过2000毫秒，就触发长按事件
                    boolean isLongPress = (eventTime>2000);

                    switch(keyCode){
                        case KeyEvent.KEYCODE_HEADSETHOOK://播放或暂停
                        case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://播放或暂停

                            Toast.makeText(MainActivity.this,"playOrPause();",Toast.LENGTH_SHORT).show();
                            break;

                        //短按=播放下一首音乐，长按=当前音乐快进
                        case KeyEvent.KEYCODE_MEDIA_NEXT:
                            if(isLongPress){
                                //自定义
                                Toast.makeText(MainActivity.this,"fastNext(1);",Toast.LENGTH_SHORT).show();
                            }else{
                                //自定义
                                Toast.makeText(MainActivity.this,"playNext();",Toast.LENGTH_SHORT).show();
                            }
                            break;

                        //短按=播放上一首音乐，长按=当前音乐快退
                        case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                            if(isLongPress){
                                //自定义
                                Toast.makeText(MainActivity.this,"fastPrevious(1);",Toast.LENGTH_SHORT).show();
                            }else{
                                //自定义
                                Toast.makeText(MainActivity.this,"playPrevious();",Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }

                    break;
                default://其他消息-则扔回上层处理
                    super.handleMessage(msg);
            }
        }
    }
}
