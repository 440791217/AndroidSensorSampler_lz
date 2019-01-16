package com.example.media;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MediaHelper implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;//播放音频控件
    private String url;//播放地址
    private Activity activity;//上下文
    private Timer mTimer;//监听播放进度用到计时器
    private OnMediaListener mOnMediaListener;//播放完成和当前进度的接口

    private final int MOVE_TIME = 15000;//快进和快退的时间

    private boolean isPrepare;//资源是否准备完成
    private ProgressDialog dialog ;

    /**
     * 初始化操作，
     *
     * @param activity
     */
    public void init(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(this);//设置错误监听
        mediaPlayer.setOnPreparedListener(this);//资源准备完成监听
        mediaPlayer.setOnSeekCompleteListener(this);//seek监听
        mediaPlayer.setOnCompletionListener(this);//播放完成监听
        setTimer();//开启定时器
    }

    /**
     * 切换播放资源
     *
     * @param url
     */
    public void changeUrl(String url) {
        isPrepare = false;//进入资源为准备状态
        setUrl(url);//设置新的链接
        mediaPlayer.reset();//初始化
        start();//开始播放
    }


    /**
     * 开始播放，开始是在准备结束完成后才播放的
     */
    public void start() {
        dialog .show();
        try {
            mediaPlayer.setDataSource(activity, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //因为是在线播放，所以采用异步的方式进行资源准备，
        // 准备完成后会执行OnPreparedListener的onPrepared方法
        mediaPlayer.prepareAsync();
    }


    /**
     * 快进
     */
    public void next() {
        int curPosition = mediaPlayer.getCurrentPosition();
        if ((curPosition + MOVE_TIME) > mediaPlayer.getDuration()) return;
        mediaPlayer.seekTo(curPosition + MOVE_TIME);
    }

    /**
     * 快退
     */
    public void previous() {
        int curPosition = mediaPlayer.getCurrentPosition();
        if ((curPosition - MOVE_TIME) < 0) return;
        mediaPlayer.seekTo(curPosition - MOVE_TIME);
    }

    /**
     * 暂停
     */
    public void pause() {
        if (isPlay())
            mediaPlayer.pause();
    }

    /**
     * 资源释放，在退出界面是调用
     */
    public void release() {
        mediaPlayer.release();
    }

    /**
     * 继续播放
     */
    public void continuePlay() {
        if (!isPlay())
            mediaPlayer.start();

    }

    /**
     * 手动定位播放位置，在外部滑动seekbar时调用
     * @param position
     */
    public void seekTo(int position) {
        if (position < 0 || position > mediaPlayer.getDuration()) return;
        mediaPlayer.seekTo(position);
    }

    /**
     * 设置定时器
     */
    private void setTimer() {
        mTimer = new Timer();
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (isPrepare) {
                    if (isPlay())
                        mOnMediaListener.onSeek(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                }

            }
        };
        mTimer.schedule(mTimerTask, 0, 1);
    }

    /**
     * 当前是否在播放
     * @return
     */
    public boolean isPlay() {
        return mediaPlayer.isPlaying();
    }


    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        dialog.dismiss();
        Log.d("mp", "播放失败: " + what + "--" + extra);
        Toast.makeText(activity, "播放失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        dialog.dismiss();
        //准备完成，开始播放，并修改状态
        isPrepare = true;
        mp.start();

    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //播放完成
        isPrepare = false;
        mp.reset();
        mOnMediaListener.OnComplete();
    }

    public interface OnMediaListener {
        void onSeek(int curPosotion, int totalPosition);

        void OnComplete();
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setmOnSeekListener(OnMediaListener mOnSeekListener) {
        this.mOnMediaListener = mOnSeekListener;
    }
}
