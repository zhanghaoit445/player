package player.zhang;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;

public class VideoView extends SurfaceView implements MediaPlayerControl  {
	private  Context mContext ;
	  private OnErrorListener mOnErrorListener;//播放出错事件监听
	private SurfaceView sufview;
    private MediaPlayer mMediaPlayer = null;//媒体播放器
    private int mVideoWidth,mVideoHeight;//视频宽度
    private int         mSurfaceWidth;//Surface宽度
    private int         mSurfaceHeight;//Surface高度
    private boolean     mIsPrepared;//是否准备好的
    private int         mSeekWhenPrepared;// 指定进度什么时候缓冲好
    private SurfaceHolder mSurfaceHolder = null;//
    private boolean     mStartWhenPrepared;// 开始什么时候缓冲好
    private MediaController mMediaController;//媒体控制器
    private int         mDuration;//視頻總長
    private OnCompletionListener mOnCompletionListener;//视频播放完成事件监听
    private MediaPlayer.OnPreparedListener mOnPreparedListener;//视频准备好事件监听
    private Uri uri;//視頻uri
	public VideoView(Context context) {
		super(context);
		   mContext = context;
	        initVideoView();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 這兩個 構造函數必須加 ？？？？？？？？？？？？？
	 * @param context
	 * @param attrs
	 */
	public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        initVideoView();
    }
	public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        //初始化
        initVideoView();
    }
    public int getVideoWidth(){
    	return mVideoWidth;
    }
    
    public int getVideoHeight(){
    	return mVideoHeight;
    }
    //评估视频 高 宽
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	         int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
	        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
	        //对加载的视频设置视频尺寸大小
	        setMeasuredDimension(width,height);//设置测量视频尺寸
	    }
	
	
/**
 * 初始化工作
 */
 private void initVideoView() {
	//初始化VideoView
     mVideoWidth = 0;//视频宽度
     mVideoHeight = 0;//视频高度
     //surfaceHolder 
     getHolder().addCallback(mSHCallback);//对surface对象的状态进行监听
     getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置surfaceview不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前
     setFocusable(true);//可获得焦点
     setFocusableInTouchMode(true);//在触摸模式可获得焦点
     //VideoView获得焦点
     requestFocus();
 
	
	}
 
 SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback()
 {
     public void surfaceChanged(SurfaceHolder holder, int format,
                                 int w, int h)
     {    mSurfaceWidth = w;
         mSurfaceHeight = h;
         if (mMediaPlayer != null && mIsPrepared && mVideoWidth == w && mVideoHeight == h) {
             if (mSeekWhenPrepared != 0) {
                 mMediaPlayer.seekTo(mSeekWhenPrepared);
                 mSeekWhenPrepared = 0;
             }
             mMediaPlayer.start();
         }
     }
     public void setVideoScale(int width , int height){
     	LayoutParams lp = getLayoutParams();
     	lp.height = height;
 		lp.width = width;
 		setLayoutParams(lp);
     }
     
     public void surfaceCreated(SurfaceHolder holder)
     {
         mSurfaceHolder = holder;
         openVideo();//打开视频
     }

  
	public void surfaceDestroyed(SurfaceHolder holder)
     {
         // after we return from this we can't use the surface any more
         mSurfaceHolder = null;
         if (mMediaController != null) mMediaController.hide();
         if (mMediaPlayer != null) {
             mMediaPlayer.reset();//重置
             mMediaPlayer.release();
             mMediaPlayer = null;
         }
     }
 };
 
 //出現錯誤的時候囘調函數
 private MediaPlayer.OnErrorListener mErrorListener =
	        new MediaPlayer.OnErrorListener() {
	        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {

	            if (mOnErrorListener != null) {
	                if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
	                    return true;
	                }
	            }

	            return true;
	        }
	    };
	  //注册在设置或播放过程中发生错误时调用的回调函数。如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。

 public void setOnErrorListener(OnErrorListener l)
 {
     mOnErrorListener = l;
 }
 //视频播放完成时调用的回调函数
 public void setOnCompletionListener(OnCompletionListener l)//注册在媒体文件播放完毕时调用的回调函数
 {
     mOnCompletionListener = l;
 }

	public void start() {//开始播放
         if (mMediaPlayer != null&& mIsPrepared) {
        	 mMediaPlayer.start();
    } else {
       
    }

		
	}

	public void pause() {//暂停播放
        if (mMediaPlayer != null && mIsPrepared) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
        mStartWhenPrepared = false;
    
		
	}

	   public boolean isPlaying() {//判断是否正在播放视频
	        if (mMediaPlayer != null && mIsPrepared) {
	            return mMediaPlayer.isPlaying();
	        }
	        return false;
	    }

	   public int getBufferPercentage() {//获得缓冲区的百分比
	  
	        return 0;
	    }
	public boolean canPause() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void stopPlayback() {
		 if (mMediaPlayer != null) {
	            mMediaPlayer.stop();//停止播放
	            mMediaPlayer.release();//释放资源
	            mMediaPlayer = null;
	        }		
	}
	public void setVideoURI(Uri uri) {
		 this.uri = uri;
	        mStartWhenPrepared = false;// 指定开始位置加载视频
	        mSeekWhenPrepared = 0;// 指定进度位置加载视频
	        openVideo();//打开视频
	        requestLayout();//当view确定自身已经不再适合现有的区域时，该view本身调用这个方法要求parent view重新调用他的onMeasure onLayout来对重新设置自己位置。
	        invalidate();//View本身调用迫使view重画
	}
	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
	        public void onPrepared(MediaPlayer mp) {
	            // briefly show the mediacontroller
	        	//是否加载完成
	            mIsPrepared = true;
	            if (mOnPreparedListener != null) {
	                mOnPreparedListener.onPrepared(mMediaPlayer);
	            }
	            
	            mVideoWidth = mp.getVideoWidth();
	            mVideoHeight = mp.getVideoHeight();
	            if (mVideoWidth != 0 && mVideoHeight != 0) {
	                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
	            	//设置surface 宽高
	                getHolder().setFixedSize(mVideoWidth, mVideoHeight);
	                
	                if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {

	                	if (mSeekWhenPrepared != 0) {
	                        mMediaPlayer.seekTo(mSeekWhenPrepared);
	                        mSeekWhenPrepared = 0;
	                    }
	                    if (mStartWhenPrepared) {
	                        mMediaPlayer.start();
	                        mStartWhenPrepared = false;
	                    } 
	                }
	            } else {
	                // We don't know the video size yet, but should start anyway.
	                // The video size might be reported to us later.
	            	//我们不知道视频尺寸大小，无论如何都要执行
	            	//mSeekWhenPrepared 指定进度位置进行加载视频
	                if (mSeekWhenPrepared != 0) {
	                	//设置播放进度位置
	                    mMediaPlayer.seekTo(mSeekWhenPrepared);
	                    mSeekWhenPrepared = 0;
	                }
	                //指定开始位置进行加载视频
	                if (mStartWhenPrepared) {
	                	//开始播放
	                    mMediaPlayer.start();
	                    mStartWhenPrepared = false;
	                }
	            }
	        }
	    };
	    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l)//注册在媒体文件加载完毕，可以播放时调用的回调函数
	    {
	        mOnPreparedListener = l;
	    }
	private void openVideo() {
		if (uri == null || mSurfaceHolder == null) {
            return;
        }
        //音乐回放  暂停
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();//重置
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {
            mMediaPlayer = new MediaPlayer();//媒体播放器
            //视频播放完成时调用
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
		            if (mOnCompletionListener != null) {
		                mOnCompletionListener.onCompletion(mMediaPlayer);
		            }
		        }
			});
            //视频加载完成时调用
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
 
            //设置或播放时，产生错误时 调用
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mDuration = -1;//视频的总时长     
            //数据源
            mMediaPlayer.setDataSource(mContext, uri);
            //设置显示内容
            mMediaPlayer.setDisplay(mSurfaceHolder);
            //声音类型
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //视频播放时 屏幕不休眠
            mMediaPlayer.setScreenOnWhilePlaying(true);
            
            //开启新线程 准备数据
            mMediaPlayer.prepareAsync();// 为了不阻塞主线程而异步准备
        }catch (Exception ex) {
           ex.printStackTrace();
            return;
        }
        
        
        
        
	}
	   public void setVideoScale(int width , int height){
	    	LayoutParams lp = getLayoutParams();
	    	lp.height = height;
			lp.width = width;
			setLayoutParams(lp);
	    }
		   public int getDuration() {//获得所播放视频的总时间
	        if (mMediaPlayer != null && mIsPrepared) {
	            if (mDuration > 0) {
	                return mDuration;
	            }
	            mDuration = mMediaPlayer.getDuration();
	            return mDuration;
	        }
	        mDuration = -1;
	        return mDuration;
	    }

		   public int getCurrentPosition() {//获得当前播放位置
		        if (mMediaPlayer != null && mIsPrepared) {
		        			            return mMediaPlayer.getCurrentPosition();
		        }
		        return 0;
		    }

		   public void seekTo(int msec) {//设置播放位置
		        if (mMediaPlayer != null && mIsPrepared) {
		            mMediaPlayer.seekTo(msec);
		        } else {
		            mSeekWhenPrepared = msec;
		        }
		    }


		   
}
