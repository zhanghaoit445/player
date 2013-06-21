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

	public final static int MY_HEIGHT = 163;//�ɵ���������������߶�
	public final static int MY_WIDTH = 44;//�ɵ�����������������
	private Bitmap bm , bm1;//�ɵ���������С����ͼƬ      bm��ɫ��С����ͼƬ    bm1 ��ɫ��С����ͼƬ
	private int bitmapWidth , bitmapHeight;// �ɵ���������С����ͼƬ��ȡ��߶� 
	private int index;//�ڼ����ɵ���������С����ͼƬ
	private OnVolumeChangedListener mOnVolumeChangedListener;//�����ı����
	private AudioManager amAudioManager;
	private final static int HEIGHT = 11;//�ɵ���������С����߶�  11 * 15 = 165

	private Context mContext;
	/**
	 * �ı�������С(�ӿ�)
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
		//��ʼ��
		init();
	}
	private void init() {
		
		bm = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sound_line);
		bm1 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sound_line1);

		bitmapWidth = bm.getWidth();
		bitmapHeight = bm.getHeight();
	
		

		amAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		
		setIndex(amAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//���õ�ǰ����
	
	}
	@Override
	protected void onDraw(Canvas canvas) {

		int reverseIndex = 15 - index;
		//��ɫ�ķ���
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
	public boolean onTouchEvent(MotionEvent event) {//ʵ�ָ÷������������¼�
		int y = (int) event.getY();
		int n = y * 15 / MY_HEIGHT;
		Log.i("i", "��"+y+"di"+n);
		setIndex(15-n);
          return true;
	}
	
/**
 * ��������
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
