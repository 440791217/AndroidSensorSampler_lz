package com.example.media;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cui.mediaplayer.R;

public class MainActivity extends AppCompatActivity implements MediaHelper.OnMediaListener, SeekBar.OnSeekBarChangeListener {

    private MediaHelper mediaHelper;
    private SeekBar mSeelBar;
    private TextView textView;
    private boolean isPlay;
    String url1 = "http://bmob-cdn-14121.b0.upaiyun.com/2017/09/26/464edee940b4371b80f9f4b4627f92e0.mp3";
    String url2 = "http://bmob-cdn-14121.b0.upaiyun.com/2017/09/16/9e43b26940feacdc80d31f5d51c09900.mp3";
    String url3 = "http://bmob-cdn-14121.b0.upaiyun.com/2017/09/16/9e43b26940feacdc80d31f5d51c099001.mp3";
    AudioManager mAudioManager;
    ComponentName mAudioComponentName;
    Context mContext;
    private MediaSession mSession;
    String Tag = "mark";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mContext = this;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mAudioComponentName = new ComponentName(mContext, AudioBroadcast.class); //AudioBroadcast前面静态注册的广播
        mAudioManager.registerMediaButtonEventReceiver(mAudioComponentName);  //方法过时
        mSeelBar = (SeekBar) findViewById(R.id.seekBar);
        textView = (TextView) findViewById(R.id.text);
        mSeelBar.setOnSeekBarChangeListener(this);

        mediaHelper = new MediaHelper();
        mediaHelper.init(this);
        mediaHelper.setUrl(url2);
        mediaHelper.setmOnSeekListener(this);
        mediaHelper.start();
    }

    public void mainClick(View view) {

        switch (view.getId()) {
            case R.id.change:
                if (mediaHelper.getUrl().equals(url1)) {
                    mediaHelper.changeUrl(url2);
                } else {
                    mediaHelper.changeUrl(url1);
                }
                break;
            case R.id.pro:
                mediaHelper.previous();
                break;
            case R.id.next:
                mediaHelper.next();
                break;

            case R.id.pause:
                mediaHelper.pause();
                break;

            case R.id.start:
                mediaHelper.continuePlay();
                break;

            case R.id.errror:
                mediaHelper.changeUrl(url3);
                break;
        }


    }

    @Override
    public void onSeek(final int curPosotion, final int totalPosition) {
        mSeelBar.setMax(totalPosition);
        mSeelBar.setProgress(curPosotion);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("curPosotion:" + curPosotion + "\n" + "totalPosition:" + totalPosition);
            }
        });


    }

    @Override
    public void OnComplete() {
        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //在停止滑动时进行定位，在其他地方，播放会出现奇怪的事情= =、鬼叫一样
        mediaHelper.seekTo(seekBar.getProgress());
    }

    @Override
    protected void onResume() {
        //在界面开始是判断是不是切入后台后重新唤起的，如果是就继续之前的状态
        if (mediaHelper != null && isPlay) {
            mediaHelper.continuePlay();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        //在进入后台时保存当前的播放状态
        isPlay=mediaHelper.isPlay();
        mediaHelper.pause();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //退出界面时，释放资源
        mediaHelper.release();
        super.onDestroy();
    }
}
