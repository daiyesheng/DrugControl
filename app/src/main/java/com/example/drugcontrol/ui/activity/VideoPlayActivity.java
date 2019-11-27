package com.example.drugcontrol.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.drugcontrol.R;
import com.example.drugcontrol.widget.ScaleFrameLayout;

import java.io.IOException;

import io.reactivex.observers.DisposableObserver;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoPlayActivity extends ToolbarActivity implements IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnSeekCompleteListener,
        IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener {


    private LinearLayout rootContainer;
    private TextureView mTextureView;
    private ImageView imgVideoCover;
    private ProgressBar progressBar;    //底部的进度条 不可拖动

    private ScaleFrameLayout layoutVideoContainer;
    private FrameLayout layoutVideoInfo;    //视频播放布局外部的遮罩  待当前播放进度 时间 总时间 可拖动等信息控件
    private TextView tvVideoCurTime;    //视频当前时间
    private SeekBar mSeekBarVideo;    //可拖动进度的seekbar
    private TextView tvVideoTotalTime;    //视频总的时间
    private ImageView imgPlay;  //播放按钮
    private TextView tvTitles;
    long lastProgress;
    private ImageView imgBack;


    private IjkMediaPlayer mPlayer;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private int vWidth; //视频的宽
    private int vHeight;//视频的高
    private DisposableObserver mSubscriber; //异步更新当前时间、视频总时间、当前进度的Subscriber对象
    private Point mPoint;

    private Bitmap bitmapLastFrame; //跳转其它页面后视频当前进度的bitmap对象
    private boolean seekToLastPosi; //是否需要移动到上个位置

    public final int PLAYER_NORMAL = 1;        // 普通播放器
    public final int PLAYER_FULL_SCREEN = 2;   // 全屏播放器
    private int mPlayerState = PLAYER_NORMAL;   //默认是普通状态


    public static void start(Context context) {
        Intent starter = new Intent(context, VideoPlayActivity.class);
        context.startActivity(starter);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_video_play;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        super.initView(savedInstanceState, contentView);
        initTitle(getResources().getString(R.string.video_play));
        initBackClick(NO_RES, view -> onBackPressed());

        layoutVideoContainer = findViewById(R.id.layoutVideo);
        mTextureView = findViewById(R.id.textureView);
        imgVideoCover = findViewById(R.id.imgVideoCover);
        progressBar = findViewById(R.id.progressBar);
        imgBack = findViewById(R.id.imgBack);

        rootContainer = findViewById(R.id.container);
        layoutVideoInfo = findViewById(R.id.layoutVideoInfo);
        tvVideoCurTime = findViewById(R.id.tvVideoCurTime);
        mSeekBarVideo = findViewById(R.id.mSeekBarVideo);
        tvVideoTotalTime = findViewById(R.id.tvVideoTotalTime);
        imgPlay = findViewById(R.id.imgPlay);
        tvTitles = findViewById(R.id.tvTitles);

        applyDebouncingClickListener(
                findViewById(R.id.imgBack),
                findViewById(R.id.imgPlay),
                findViewById(R.id.imgVideoExpand)
        );

        imgPlay.setSelected(true);
        mPoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(mPoint);
        mTextureView.setSurfaceTextureListener(this);
        mSeekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lastProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPlayer != null) {
                    mPlayer.seekTo(seekBar.getProgress());
                    mPlayer.start();
                }
            }
        });


    }

    @Override
    public void doBusiness() {

    }

    @Override
    public <P> P initPresenter() {
        return null;
    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgPlay:
                if (mPlayer == null) {  //进来直接点播放的话 如果上次视频不为空 就直接播放上个视频
                    seekToLastPosi = true;
                    readyToPlay();
                    return;
                }
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    imgPlay.setSelected(true);
                } else {
                    mPlayer.start();
                    imgPlay.setSelected(false);
                }
                break;
            case R.id.imgVideoExpand:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (mPlayerState != PLAYER_NORMAL) {
            exitFullScreen();
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (bitmapLastFrame != null) {
            imgVideoCover.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            imgVideoCover.setImageBitmap(bitmapLastFrame);

            //切换后台后回来重新全屏
            if (mPlayerState == PLAYER_FULL_SCREEN) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        super.onDestroy();
    }


    //退出全屏
    private void exitFullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewGroup contentView = this.findViewById(android.R.id.content);
        contentView.removeView(layoutVideoContainer);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutVideoContainer.setRatio(375f / 216f);   //恢复默认的比例
        layoutVideoContainer.postInvalidate();
        rootContainer.addView(layoutVideoContainer, 0, params);
        mPlayerState = PLAYER_NORMAL;
    }


    @Override
    public void onCompletion(IMediaPlayer mediaPlayer) { //当MediaPlayer播放完成后触发
        showControlWidget();
        //手动set一下时间 避免时间同步不正确
        tvVideoCurTime.setText(tvVideoTotalTime.getText().toString());
        imgPlay.setSelected(true);
    }

    private void showControlWidget() {
        if (imgBack.getVisibility() != View.VISIBLE) {
            imgBack.setVisibility(View.VISIBLE);
            imgBack.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        }
        if (layoutVideoInfo.getVisibility() != View.VISIBLE) {
            layoutVideoInfo.setVisibility(View.VISIBLE);
            layoutVideoInfo.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        }
        if (imgPlay.getVisibility() != View.VISIBLE) {
            imgPlay.setVisibility(View.VISIBLE);
            imgPlay.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        }
        //显示控制的布局之后要隐藏当前底部不可拖动的进度条
        if (progressBar.getVisibility() != View.GONE) {
            progressBar.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            progressBar.setVisibility(View.GONE);
        }
    }

    private void dismissControlWidget() {
        if (imgBack.getVisibility() != View.GONE) {
            imgBack.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            imgBack.setVisibility(View.GONE);
        }
        /*if (imgCollect.getVisibility() != View.GONE) {
            imgCollect.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            imgCollect.setVisibility(View.GONE);
        }*/
        if (layoutVideoInfo.getVisibility() != View.GONE) {
            layoutVideoInfo.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            layoutVideoInfo.setVisibility(View.GONE);
        }
        if (imgPlay.getVisibility() != View.GONE) {
            imgPlay.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
            imgPlay.setVisibility(View.GONE);
        }
        //隐藏控制的布局之后要显示当前底部不可拖动的进度条
        if (progressBar.getVisibility() != View.VISIBLE) {
            progressBar.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onError(IMediaPlayer mediaPlayer, int whatError, int i1) {
        LogUtils.d("onError:" + whatError);
        imgPlay.setSelected(true);
        onHideLoading();
        showControlWidget();
        ToastUtils.showShort("出错了 T_T");
        if (mPlayer != null) {  //发生Error的时候需要重新释放资源
            mPlayer.release();
            mPlayer = null;
        }
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mediaPlayer, int whatInfo, int i1) {
        // 当一些特定信息出现或者警告时触发
        LogUtils.d("onInfo:" + whatInfo);
        switch (whatInfo) {
            case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:   //开始渲染
                onHideLoading();
                dismissControlWidget();
                if (mPlayer.isPlaying()) {  //避免播放完毕后，再次播放图标状态不正确的bug
                    imgPlay.setSelected(false);
                }
                //隐藏封面图  暂时用这种方法代替 imageView假装是视频首帧
                if (imgVideoCover.getVisibility() != View.GONE) {
                    imgVideoCover.setVisibility(View.GONE);
                    tvTitle.setVisibility(View.GONE);
                }
                seekToLastPosi = false;
                break;
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:  //开始缓冲
                imgPlay.setVisibility(View.GONE);   //开始缓冲的时候应该隐藏播放暂停按钮
                onShowLoading();
                break;
            case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:   //缓冲结束
                imgPlay.setVisibility(View.VISIBLE);   //缓冲结束的时候应该显示播放暂停按钮
                onHideLoading();
                break;
        }
        return true;
    }

    @Override
    public void onSeekComplete(IMediaPlayer mediaPlayer) {
        // seek操作完成时触发
        if (mPlayer.isPlaying()) {
            imgPlay.setSelected(false);
        }
    }

    //准备播放
    private void readyToPlay() {
        if (!mTextureView.isAvailable()) {
            return;
        }
        if (mPlayer != null) {  //再次播放的时候需要重新释放资源
            mPlayer.release();
        }
        mPlayer = new IjkMediaPlayer();
        mPlayer.setScreenOnWhilePlaying(true); //常亮
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnSeekCompleteListener(this);
        mPlayer.setOnVideoSizeChangedListener(this);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setSurface(mSurface);
        try {
//获得播放源访问入口
            AssetFileDescriptor afd = getAssets().openFd("3ddemo.mp4");// 注意这里的区别

            //构建IjkPlayer能识别的IMediaDataSource，下面的RawDataSourceProvider实现了IMediaDataSource接口
            RawDataSourceProvider sourceProvider = new RawDataSourceProvider(afd);
            //给IjkPlayer设置播放源
            mPlayer.setDataSource(sourceProvider);
            //准备播放
            mPlayer.prepareAsync();
            mSeekBarVideo.setProgress((int) mPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //播放url方式
//        Uri uri = Uri.parse(videoUrl);
//        try {
//            mPlayer.setDataSource(this, uri);
//            mPlayer.prepareAsync();
//            onShowLoading();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public void onPrepared(IMediaPlayer mediaPlayer) {
        LogUtils.d("onPrepared");
        // 当prepare完成后，该方法触发，在这里我们播放视频
        //首先取得video的宽和高
//        vWidth = mediaPlayer.getVideoWidth();
//        vHeight = mediaPlayer.getVideoHeight();
//        if (vWidth > mPoint.x || vHeight > mPoint.y) {
//            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
//            float wRatio = (float) vWidth / (float) mPoint.x;
//            float hRatio = (float) vHeight / (float) mPoint.y;
//            //选择大的一个进行缩放
//            float ratio = Math.max(wRatio, hRatio);
//            vWidth = (int) Math.ceil((float) vWidth / ratio);
//            vHeight = (int) Math.ceil((float) vHeight / ratio);
//            //设置surfaceView的布局参数
//            mTextureView.setLayoutParams(new FrameLayout.LayoutParams(vWidth, vHeight));
//        }
        if (seekToLastPosi) {
            mPlayer.seekTo(lastProgress);
        }
        mPlayer.start();
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //下面开始实例化MediaPlayer对象
        if (mSurfaceTexture == null) {
            mSurfaceTexture = surface;
            mSurface = new Surface(mSurfaceTexture);
        } else {
            mTextureView.setSurfaceTexture(mSurfaceTexture);
        }
        LogUtils.d("onSurfaceTextureAvailable");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.d("onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtils.d("onSurfaceTextureDestroyed");
        return mSurfaceTexture == null;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        LogUtils.d("onSurfaceTextureUpdated");
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

    }
}
