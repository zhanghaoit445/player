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
	  private OnErrorListener mOnErrorListener;//���ų����¼�����
	private SurfaceView sufview;
    private MediaPlayer mMediaPlayer = null;//ý�岥����
    private int mVideoWidth,mVideoHeight;//��Ƶ���
    private int         mSurfaceWidth;//Surface���
    private int         mSurfaceHeight;//Surface�߶�
    private boolean     mIsPrepared;//�Ƿ�׼���õ�
    private int         mSeekWhenPrepared;// ָ������ʲôʱ�򻺳��
    private SurfaceHolder mSurfaceHolder = null;//
    private boolean     mStartWhenPrepared;// ��ʼʲôʱ�򻺳��
    private MediaController mMediaController;//ý�������
    private int         mDuration;//ҕ�l���L
    private OnCompletionListener mOnCompletionListener;//��Ƶ��������¼�����
    private MediaPlayer.OnPreparedListener mOnPreparedListener;//��Ƶ׼�����¼�����
    private Uri uri;//ҕ�luri
	public VideoView(Context context) {
		super(context);
		   mContext = context;
	        initVideoView();
		// TODO Auto-generated constructor stub
	}
	/**
	 * �@�ɂ� ���캯����횼� ��������������������������
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
        //��ʼ��
        initVideoView();
    }
    public int getVideoWidth(){
    	return mVideoWidth;
    }
    
    public int getVideoHeight(){
    	return mVideoHeight;
    }
    //������Ƶ �� ��
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	         int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
	        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
	        //�Լ��ص���Ƶ������Ƶ�ߴ��С
	        setMeasuredDimension(width,height);//���ò�����Ƶ�ߴ�
	    }
	
	
/**
 * ��ʼ������
 */
 private void initVideoView() {
	//��ʼ��VideoView
     mVideoWidth = 0;//��Ƶ���
     mVideoHeight = 0;//��Ƶ�߶�
     //surfaceHolder 
     getHolder().addCallback(mSHCallback);//��surface�����״̬���м���
     getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//����surfaceview��ά���Լ��Ļ����������ǵȴ���Ļ����Ⱦ���潫�������͵��û���ǰ
     setFocusable(true);//�ɻ�ý���
     setFocusableInTouchMode(true);//�ڴ���ģʽ�ɻ�ý���
     //VideoView��ý���
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
         openVideo();//����Ƶ
     }

  
	public void surfaceDestroyed(SurfaceHolder holder)
     {
         // after we return from this we can't use the surface any more
         mSurfaceHolder = null;
         if (mMediaController != null) mMediaController.hide();
         if (mMediaPlayer != null) {
             mMediaPlayer.reset();//����
             mMediaPlayer.release();
             mMediaPlayer = null;
         }
     }
 };
 
 //���F�e�`�ĕr����{����
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
	  //ע�������û򲥷Ź����з�������ʱ���õĻص����������δָ���ص������� ��ص��������ؼ٣�VideoView ��֪ͨ�û������˴���

 public void setOnErrorListener(OnErrorListener l)
 {
     mOnErrorListener = l;
 }
 //��Ƶ�������ʱ���õĻص�����
 public void setOnCompletionListener(OnCompletionListener l)//ע����ý���ļ��������ʱ���õĻص�����
 {
     mOnCompletionListener = l;
 }

	public void start() {//��ʼ����
         if (mMediaPlayer != null&& mIsPrepared) {
        	 mMediaPlayer.start();
    } else {
       
    }

		
	}

	public void pause() {//��ͣ����
        if (mMediaPlayer != null && mIsPrepared) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
        mStartWhenPrepared = false;
    
		
	}

	   public boolean isPlaying() {//�ж��Ƿ����ڲ�����Ƶ
	        if (mMediaPlayer != null && mIsPrepared) {
	            return mMediaPlayer.isPlaying();
	        }
	        return false;
	    }

	   public int getBufferPercentage() {//��û������İٷֱ�
	  
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
	            mMediaPlayer.stop();//ֹͣ����
	            mMediaPlayer.release();//�ͷ���Դ
	            mMediaPlayer = null;
	        }		
	}
	public void setVideoURI(Uri uri) {
		 this.uri = uri;
	        mStartWhenPrepared = false;// ָ����ʼλ�ü�����Ƶ
	        mSeekWhenPrepared = 0;// ָ������λ�ü�����Ƶ
	        openVideo();//����Ƶ
	        requestLayout();//��viewȷ�������Ѿ������ʺ����е�����ʱ����view��������������Ҫ��parent view���µ�������onMeasure onLayout�������������Լ�λ�á�
	        invalidate();//View���������ʹview�ػ�
	}
	MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
	        public void onPrepared(MediaPlayer mp) {
	            // briefly show the mediacontroller
	        	//�Ƿ�������
	            mIsPrepared = true;
	            if (mOnPreparedListener != null) {
	                mOnPreparedListener.onPrepared(mMediaPlayer);
	            }
	            
	            mVideoWidth = mp.getVideoWidth();
	            mVideoHeight = mp.getVideoHeight();
	            if (mVideoWidth != 0 && mVideoHeight != 0) {
	                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
	            	//����surface ���
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
	            	//���ǲ�֪����Ƶ�ߴ��С��������ζ�Ҫִ��
	            	//mSeekWhenPrepared ָ������λ�ý��м�����Ƶ
	                if (mSeekWhenPrepared != 0) {
	                	//���ò��Ž���λ��
	                    mMediaPlayer.seekTo(mSeekWhenPrepared);
	                    mSeekWhenPrepared = 0;
	                }
	                //ָ����ʼλ�ý��м�����Ƶ
	                if (mStartWhenPrepared) {
	                	//��ʼ����
	                    mMediaPlayer.start();
	                    mStartWhenPrepared = false;
	                }
	            }
	        }
	    };
	    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l)//ע����ý���ļ�������ϣ����Բ���ʱ���õĻص�����
	    {
	        mOnPreparedListener = l;
	    }
	private void openVideo() {
		if (uri == null || mSurfaceHolder == null) {
            return;
        }
        //���ֻط�  ��ͣ
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();//����
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {
            mMediaPlayer = new MediaPlayer();//ý�岥����
            //��Ƶ�������ʱ����
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				public void onCompletion(MediaPlayer mp) {
		            if (mOnCompletionListener != null) {
		                mOnCompletionListener.onCompletion(mMediaPlayer);
		            }
		        }
			});
            //��Ƶ�������ʱ����
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
 
            //���û򲥷�ʱ����������ʱ ����
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mDuration = -1;//��Ƶ����ʱ��     
            //����Դ
            mMediaPlayer.setDataSource(mContext, uri);
            //������ʾ����
            mMediaPlayer.setDisplay(mSurfaceHolder);
            //��������
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //��Ƶ����ʱ ��Ļ������
            mMediaPlayer.setScreenOnWhilePlaying(true);
            
            //�������߳� ׼������
            mMediaPlayer.prepareAsync();// Ϊ�˲��������̶߳��첽׼��
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
		   public int getDuration() {//�����������Ƶ����ʱ��
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

		   public int getCurrentPosition() {//��õ�ǰ����λ��
		        if (mMediaPlayer != null && mIsPrepared) {
		        			            return mMediaPlayer.getCurrentPosition();
		        }
		        return 0;
		    }

		   public void seekTo(int msec) {//���ò���λ��
		        if (mMediaPlayer != null && mIsPrepared) {
		            mMediaPlayer.seekTo(msec);
		        } else {
		            mSeekWhenPrepared = msec;
		        }
		    }


		   
}
