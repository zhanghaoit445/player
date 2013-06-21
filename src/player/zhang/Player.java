package player.zhang;
import java.util.LinkedList;

import player.zhang.SoundView.OnVolumeChangedListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
public class Player extends Activity   {
	private View controlView;//���N���o
	private GestureDetector mGestureDetector = null;//����ʶ��
	private ImageButton phylist;
	private ImageButton stop;
	private ImageButton up;
	private ImageButton down;
	private ImageView listone,listtwo;
	private ImageButton voice;
	private SeekBar seekBar;
	private TextView staTextView;
	private TextView endtextview;
	private VideoView videoView;
	
	private Uri uri;
	private boolean isFullScreen = false;//�Ƿ�ȫ��
	private boolean popwindow = false;//�����Ƿ���ʾ
	private boolean isPaused = false;	//�Ƿ�����ͣ
	private boolean shipinlist = false;//ҕ�l�б��Ƿ�
	private boolean voicepic = false;//��������ͼ���Ƿ���ʾ
	private boolean isSilent=false;//�Ƿ��Ǿ���
	private String sizeString ;//localityactivity  ���ݹ����ĸ�����Ϣ
	private int index;
	private Myadapter daMyadapter;
	private LinkedList<localityActivity.Moveinfo> list;
	private Vibrator vibrator;   //��Ļ���ȿ����� �𶯹�����
	private int screenHeight ,screenWidth,controlHeight ;
	private Myonclick myonclick;
    private AudioManager mAudioManager = null; //��Ƶ����
	private int maxVolume = 0;//�������
	private int currentVolume = 0;//��ǰ����  
	private PopupWindow mSoundWindow = null;//����������
private ListView lView;
	private SoundView soundView;
	private Myonitem item;
	private View xxx;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//����  �O�Þ�ȫ�� ��tag���^��횵��� setContentView ǰ���O��
	  getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	   getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	     WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        setContentView(R.layout.controler);
	      /*  Looper.myQueue().addIdleHandler(new IdleHandler() {
				public boolean queueIdle() {
					if (popowindow!=null&&videoView.isShown()) {
					//popowindow.showAtLocation(videoView, Gravity.BOTTOM, 0, 0);
						//popowindow.showAsDropDown(anchor, xoff, yoff)
						//popowindow.update(0, 0, screenWidth, controlHeight);
						}
					return false;
				}
			});*/
	        
	controlView = findViewById(R.id.button);
	endtextview = (TextView) controlView.findViewById(R.id.duration);
	staTextView = (TextView) controlView.findViewById(R.id.has_played);
	phylist=(ImageButton)controlView.findViewById(R.id.button1);
	up=(ImageButton)controlView.findViewById(R.id.button2);
	stop=(ImageButton)controlView.findViewById(R.id.button3);
	down=(ImageButton)controlView.findViewById(R.id.button4);
	voice=(ImageButton)controlView.findViewById(R.id.button5);
	xxx=findViewById(R.id.xxx);
	xxx.setVisibility(View.VISIBLE);
	listone=(ImageView)findViewById(R.id.wxnm);
	listtwo=(ImageView)xxx.findViewById(R.id.playList222);
	list=localityActivity.getFilelist();//�õ��б� list
	lView=(ListView) xxx.findViewById(R.id.pllist);
	daMyadapter= new Myadapter();
	item=new Myonitem();
	listone.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
			/*Intent intent=new Intent(Player.this,List.class);
			startActivityForResult(intent, 1);*/
			xxx.requestFocus();
			lView.setFocusable(true);
			lView.requestFocusFromTouch();
			if (shipinlist) {
				listone.setVisibility(View.VISIBLE);
				//�������Ը��� �ϴ� �Ķ�����Ŀ������    ����Ͳ������� 
			/*	TranslateAnimation	showAction = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
*/
						//�������Ա����������http://www.bianceng.cn/OS/extra/201105/26837.htm
			//	lView.setAnimation(showAction);
				//lView.setVisibility(View.GONE);
				//listone.setImageResource(R.drawable.ico_playlist_unfold);
				//listone.setPadding(350, 10, 0, 10);
					}else {			
				///listone.setImageResource(R.drawable.ico_playlist_fold);
			//listone.setPadding(300, lView.getHeight()/2, lView.getWidth(), lView.getHeight()/2);
						listone.setVisibility(View.GONE);
						
						xxx.setVisibility(View.VISIBLE);
						listtwo.setVisibility(View.VISIBLE);
						controlView.setVisibility(View.GONE);//���ظ��ְ�ť
			controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,android.R.anim.fade_out ));
			lView.setAdapter(daMyadapter);
			lView.setAnimation(AnimationUtils.loadAnimation(Player.this, android.R.anim.fade_in));
			lView.setVisibility(View.VISIBLE);
			lView.setOnItemClickListener(item);
		}	shipinlist=!shipinlist;
		}});
	listtwo.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
  xxx.setVisibility(View.GONE);
  listone.setVisibility(View.VISIBLE);
  shipinlist=!shipinlist;
		}
	});
	voice.setOnLongClickListener(new OnLongClickListener() {
	public boolean onLongClick(View v) {
				if(isSilent){//�Ƿ���
					//�糤����������ǰ �Ǿ���״̬  ��ô�������� ���ó����� ͼƬ
					voice.setImageResource(R.drawable.soundcontrol);
				}else{
		     		//�糤����������ǰ ��������״̬  ��ô�������� ���óɾ��� ͼƬ
					voice.setImageResource(R.drawable.soundmute);
				}
				isSilent = !isSilent;
				updateVolume(currentVolume);
		return true;
		}
	});
	seekBar=(SeekBar) controlView.findViewById(R.id.seekbar);
	myonclick=new Myonclick();
	stop.setOnClickListener(myonclick);   
    up.setOnClickListener(myonclick);
    down.setOnClickListener(myonclick);
    phylist.setOnClickListener(myonclick);
    voice.setOnClickListener(myonclick);
	videoView=(VideoView) findViewById(R.id.vv);
   // videoView=new VideoView(Player.this);
	videoView.setOnErrorListener(new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
			videoView.stopPlayback();//ֹͣ��Ƶ����
			new AlertDialog.Builder(Player.this)
            .setTitle("�˲���")//����
            .setMessage("����������Ƶ��ʽ����ȷ��������ֹͣ��")//��ʾ��Ϣ
            .setPositiveButton("֪����",//ȷ����ť
                    new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							videoView.stopPlayback();
						}
                    })
            .setCancelable(false)//���ó����� ������
            .show();
			
			return false;
		}
	});
	
	
soundView=new SoundView(Player.this);
	
		soundView.setOnVolumeChangeListener(new OnVolumeChangedListener(){		
		public void setYourVolume(int index) {
			updateVolume(index);//��������
		} });
	mSoundWindow=new PopupWindow(soundView);
	             getScreenSize();
	 mGestureDetector = new GestureDetector(new SimpleOnGestureListener(){
	             	//˫��  			
	     			public boolean onDoubleTap(MotionEvent e) {
	     				if(isFullScreen){//��Ƶ�Ƿ�ȫ����ʾ״̬
	     					//������ʾȫ��״̬��˫���� Ҫ��Ч���� ����ʾĬ��Ч��
	     					setVisible(1);
	     					if (popwindow) {
	     						controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,R.drawable.alpha ));
	    	     				
	     					controlView.setVisibility(View.VISIBLE);//��ʾ���ְ�ť
							}
	     				}else{
	     					//������ʾ��ȫ��״̬��˫���� Ҫ��Ч���� ����ʾȫ��Ч��
	     					setVisible(0);//������Ƶ��ʾ�ߴ�
	     					controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,R.drawable.alpha ));
	     					
	     				 controlView.setVisibility(View.GONE);//���ظ��ְ�ť
	     				
	     				}
	     				isFullScreen = !isFullScreen;
	     				popwindow=!popwindow;
	     		   		return true;
	     			}

	     			//�����¼�
	     			
	     			public boolean onSingleTapConfirmed(MotionEvent e) {//�����Ļ
	     				Toast.makeText(Player.this, "������", 1).show();
	     				if (popwindow) {
	     					controlView.setVisibility(View.VISIBLE);//��ʾ���ְ�ť
	     					controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,android.R.anim.fade_in ));
		     				
						}else {
							controlView.setVisibility(View.GONE);//���ظ��ְ�ť
							controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,android.R.anim.fade_out ));
	     					
						}
	     				popwindow=!popwindow;
	     				//return super.onSingleTapConfirmed(e);
	     				return true;
	     			}

	     			//������Ļ
	     			
	     			public void onLongPress(MotionEvent e) {
	      				if(isPaused){//��ͣ����
	     				   	videoView.start();
	   	     				}else{
	    				     videoView.pause();
	     						}
	     				isPaused = !isPaused;
	     				//super.onLongPress(e);
	     		}

	     			//���� ������Ļ����
	     			
         public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
		float distanceY) {
				final double FLING_MIN_DISTANCE = 0.5;//��С����  
                final double FLING_MIN_VELOCITY = 0.5;//��С��������  
            if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE  
                        && Math.abs(distanceY) > FLING_MIN_VELOCITY) {  
                                 //��Ļ����
                    setBrightness(20);  
                }  
                if (e1.getY() - e2.getY() < FLING_MIN_DISTANCE  
                        && Math.abs(distanceY) > FLING_MIN_VELOCITY) {  
                              //��Ļ�䰵
                    setBrightness(-20);  
                }  
                return true;  
			}	
	     	 });
	//������Ļ����
	 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	 //��ȡ���� ���ֵ �뵱ǰֵ
	  mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	sizeString=this.getIntent().getStringExtra("size");
	index=Integer.parseInt(this.getIntent().getStringExtra("index"));


	uri=Uri.parse(this.getIntent().getStringExtra("path"));
	      if(uri!=null){//���ҕ�l��ַ���^����  �O�� �����ļ�  uri
	             videoView.stopPlayback();//ֹͣ��Ƶ����
	             videoView.setVideoURI(uri);//������Ƶ�ļ�URI
	             stop.setImageResource(R.drawable.pause);
	         }else{
	        	   stop.setImageResource(R.drawable.play);
	        	 Toast.makeText(this,"����", 1).show();
	        	 return;
	          }
    videoView.setOnPreparedListener(new OnPreparedListener(){
    	//ע����ý���ļ�������ϣ����Բ���ʱ���õĻص�����
		
		public void onPrepared(MediaPlayer arg0) {//����
		//��Ƶ��ʾ�ߴ�  Ĭ��
			setVisible(1);
			isFullScreen = false;	
			videoView.start();  
			int i = videoView.getDuration();
			Log.d("onCompletion", ""+i);
			seekBar.setMax(i);
			i/=1000;//������ ��
			int minute = i/60;//������ ��
			int hour = minute/60;//ʱ
			int second = i%60;//��
			minute =  minute % 60;//��
			endtextview.setText(String.format("%02d:%02d:%02d", hour,minute,second));
		handler.sendEmptyMessage(0);
		}		
    });  
   //������ �ı� �¼�
    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.i("i", "ֹͣ��");
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.i("i", "��ʼ��");
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Log.i("i", "���ɶʱ������");
			if (fromUser) {//������û��ı��� ������
				videoView.seekTo(progress);
			}	
			handler.sendEmptyMessage(0);
		}
	});
    //��ǰ ��Ƶ�������  ����������һ����Ƶ    //�Ȼ��ٲ���
    videoView.setOnCompletionListener(new OnCompletionListener(){//ע����ý���ļ��������ʱ���õĻص�����
public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			int n = list.size();
			if(++index < n){ //������滹����Ƶ
				   videoView.setVideoURI(Uri.parse(list.get(index).localtionString));//������Ƶ�ļ�URI
					}else{
						videoView.stopPlayback();
			Player.this.finish();
			}
		}
	});  
}
//��������
private void updateVolume(int index){//��������
	if(mAudioManager!=null&& index<maxVolume){//��������Ϊ�� ���Ҳ�������������
			//���õ�ǰ������ index
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
			currentVolume = index;	
	}
	}

//java.lang.RuntimeException: view android.widget.ListView@405874d8 being added,
//but it already has a parent

Handler handler=new Handler(){
	public void handleMessage(android.os.Message msg) {
	int key=msg.what;
		switch (key) {
		case 0://�������ĸı�
			int i=videoView.getCurrentPosition();
			seekBar.setProgress(i);
			i/=1000;//������ ��
			int minute = i/60;//������ ��
			int hour = minute/60;//ʱ
			int second = i%60;//��
			minute =  minute % 60;//��
			staTextView.setText(String.format("%02d:%02d:%02d", hour,minute,second));
			sendEmptyMessageDelayed(0, 100);//����൱��һ�� ѭ�� ��ͣ�ĸ� ���handle ������Ϣ
			break;
			/*case 1:// ��Ƶ�б�
		if (popwindow) 
			popowindow.update(0,0,0,0);
		popweiPopupWindow.showAtLocation(videoView, Gravity.RIGHT, 0, 0);
		popweiPopupWindow.update(screenWidth/10, 0, screenWidth/4,screenWidth);
			break;
*/
		default:
			break;
		}
		
	};
};
public boolean onTouchEvent(MotionEvent event) {
	// TODO Auto-generated method stub
	
	boolean result = mGestureDetector.onTouchEvent(event);
	
	if(!result){
		result = super.onTouchEvent(event);
	}
	
	return result;
}
private void setVisible(int flag) {
	//������Ƶ��ʾ�ߴ�
	switch(flag){
		case 0://ȫ��
			//Log.d(TAG, "screenWidth: "+screenWidth+" screenHeight: "+screenHeight);
			videoView.setVideoScale(screenWidth, screenHeight);
			//����ȫ����ʾЧ�� 
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
							break;
					case 1://��׼
			//���VideoView�Ŀ��
			int videoWidth = videoView.getVideoWidth();
			int videoHeight = videoView.getVideoHeight();
			//�����Ļ�Ŀ��
			int mWidth = screenWidth/2;
			int mHeight = screenHeight - 50;
			
			if (videoWidth > 0 && videoHeight > 0) {
	            if ( videoWidth * mHeight  > mWidth * videoHeight ) {
	                //Log.i("@@@", "image too tall, correcting");
	            	mHeight = mWidth * videoHeight / videoWidth;
	            } else if ( videoWidth * mHeight  < mWidth * videoHeight ) {
	                //Log.i("@@@", "image too wide, correcting");
	            	mWidth = mHeight * videoWidth / videoHeight;
	            } else {
	                
	            }
	        }
			
			videoView.setVideoScale(mWidth, mHeight);

			//ȡ��ȫ����ʾЧ�� 
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			break;
	}

	
}	
private void getScreenSize()//�����Ļ�ߴ��С
{
	//��ȡ�ֻ���Ļ�Ŀ��
	Display display = getWindowManager().getDefaultDisplay();
	//�ֻ���Ļ�ĸ߶�
     screenHeight = display.getHeight();
    //�ֻ���Ļ�Ŀ��
     screenWidth = display.getWidth();
    //�������߶�  Ϊ��Ļ�߶ȵ�1/4
    controlHeight = screenHeight/4;
    
}
@Override
protected void onDestroy() {
	/*if (popowindow!=null) {
		popowindow.dismiss();
	}
	if (popweiPopupWindow!=null) {
		popweiPopupWindow.dismiss();
	}*/
	super.onDestroy();
}
@Override
protected void onStop() {
	if (null != vibrator) {  
        vibrator.cancel();  
    }  
	super.onStop();
}
/**
 * ������Ļ���� ������
 * @param brightness
 */
/*listView.setOnItemClickListener(new Myonitem());*///�������@�eֱ���O��
public void setBrightness(float brightness) {  
	    WindowManager.LayoutParams lp = getWindow().getAttributes();  
    
    //������Ļ��������   ��Χ 0.0 --- 1.0  0.0ȫ�ڣ��мǲ������ó�0.0��  1.0����
    lp.screenBrightness = lp.screenBrightness + brightness / 255.0f; 
    //1.1
    if (lp.screenBrightness > 1) {  
        lp.screenBrightness = 1;  
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);  
        long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...  
        vibrator.vibrate(pattern, -1);  
    } else if (lp.screenBrightness < 0.2) {  
        lp.screenBrightness = (float) 0.2;  
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);  
        long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...  
        vibrator.vibrate(pattern, -1);  
    }  
   getWindow().setAttributes(lp);  
}
private class Myonclick implements OnClickListener {
	public void onClick(View v) {
		int key= v.getId();
	switch (key) {
	case R.id.button1:
	
		break;
	case R.id.button2:
			if (index>=1) {
			    --index;
			videoView.setVideoURI(Uri.parse(list.get(index).localtionString));
			}else {
			Toast.makeText(getApplicationContext(), "�Ѿ��ǵ�һ����Ƶ",1).show();
		}
		break;
	case R.id.button3:
		
		if (isPaused) {
			videoView.start();
			stop.setImageResource(R.drawable.play);
		}else {
			videoView.pause();
			stop.setImageResource(R.drawable.pause);
		}
		isPaused=!isPaused;
		break;
	case R.id.button4:
		if (index<list.size()-1){
			++index;
			videoView.setVideoURI(Uri.parse(list.get(index).localtionString));
			}else {
			Toast.makeText(getApplicationContext(), "�Ѿ������һ����Ƶ",1).show();
		}
		break;
	case R.id.button5:
		//��ʼ����������ť  ��ȥд��
		Toast.makeText(getApplicationContext(), 4+"'",1).show();
		if (voicepic) {// ������ʾ
			mSoundWindow.dismiss();//SoundWindow����(����ʾ)
		}else {//��ʾ
			if(mSoundWindow.isShowing()){
				mSoundWindow.update(0,0,SoundView.MY_WIDTH,SoundView.MY_HEIGHT);
			}else{
				//��������PopupWindow��VideoView��������ʾ
				//vv ���յ���ͼ
				//Gravity.RIGHT|Gravity.CENTER_VERTICAL  �ǽ�����PopupWindow������VideoViewʲôλ���� (�Ҳ� ��ֱ����) 
				mSoundWindow.showAtLocation(videoView, Gravity.RIGHT|Gravity.CENTER_VERTICAL, 15, 0);
				//��ʾ����PopupWindow
				mSoundWindow.update(0,0,SoundView.MY_WIDTH,SoundView.MY_HEIGHT);
			}
		
		}
	voicepic=!voicepic;
		break;
	default:
		break;
	}	
	} 
	
}


private class Myadapter extends BaseAdapter{
	 LayoutInflater layoutInflater = getLayoutInflater();
		public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView==null) {
		convertView=layoutInflater.inflate(R.layout.list_item, null);
				}	
				TextView findViewById = (TextView) convertView.findViewById(R.id.text22);
				findViewById.setText(list.get(position).displayName);
			     return convertView;
			}
			
			public long getItemId(int position) {
				return position;
			}
			public Object getItem(int position) {
				return list.get(position);
			}
			
			public int getCount() {
				return list.size();
		
		}
}

private class Myonitem implements OnItemClickListener{

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		videoView.setVideoURI(Uri.parse(list.get(position).localtionString));
		
	}
	
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	listone.setVisibility(View.VISIBLE);
}
}
