package player.zhang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SoundView extends View{

	public final static int MY_HEIGHT = 163;//可调节声音区域整体高度
	public final static int MY_WIDTH = 44;//可调节声音区域整体宽度
	private Bitmap bm , bm1;//可调节声音的小方格图片      bm粉色的小方格图片    bm1 灰色的小方格图片
	private int bitmapWidth , bitmapHeight;// 可调节声音的小方格图片宽度、高度 
	private int index;//第几个可调节声音的小方格图片
	private OnVolumeChangedListener mOnVolumeChangedListener;//声音改变监听
	private AudioManager amAudioManager;
	private final static int HEIGHT = 11;//可调节声音的小方格高度  11 * 15 = 165

	private Context mContext;
	/**
	 * 改变声音大小(接口)
	 */
	public interface OnVolumeChangedListener{
		public void setYourVolume(int index);
	}
	
	public void setOnVolumeChangeListener(OnVolumeChangedListener l){
		mOnVolumeChangedListener = l;
	}
	
	public SoundView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	
	public SoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public SoundView(Context context) {
		super(context);
		mContext = context;
		//初始化
		init();
	}
	private void init() {
		
		bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sound_line);
		bm1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sound_line1);

		bitmapWidth = bm.getWidth();
		bitmapHeight = bm.getHeight();
	
		

		amAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		
		setIndex(amAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//设置当前音量
	
	}
	@Override
	protected void onDraw(Canvas canvas) {

		int reverseIndex = 15 - index;
		//灰色的方格
		for(int i = 0;i!=reverseIndex;++i){
		
				canvas.drawBitmap(bm1, new Rect(0,0,bitmapWidth,bitmapHeight), 
					new Rect(0,i*HEIGHT,bitmapWidth,i*HEIGHT+bitmapHeight), null);
		}
		for(int i = reverseIndex;i!=15;++i){
			canvas.drawBitmap(bm, new Rect(0,0,bitmapWidth,bitmapHeight), 
					new Rect(0,i*HEIGHT,bitmapWidth,i*HEIGHT+bitmapHeight), null);
		}
		
		
		super.onDraw(canvas);
	}
	public boolean onTouchEvent(MotionEvent event) {//实现该方法来处理触屏事件
		int y = (int) event.getY();
		int n = y * 15 / MY_HEIGHT;
		Log.i("i", "在"+y+"di"+n);
		setIndex(15-n);
          return true;
	}
	
/**
 * 设置音量
 * @param streamVolume
 */
	private void setIndex(int streamVolume) {
		if(streamVolume>15){
			streamVolume = 15;
		}
		else if(streamVolume<0){
			streamVolume = 0;
		}
		if(index!=streamVolume){
			index = streamVolume;
			if(mOnVolumeChangedListener!=null){
				mOnVolumeChangedListener.setYourVolume(streamVolume);
			}
		}
		invalidate();
	}

}
