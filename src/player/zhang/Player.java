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
	private View controlView;//各種按鈕
	private GestureDetector mGestureDetector = null;//手势识别
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
	private boolean isFullScreen = false;//是否全屏
	private boolean popwindow = false;//泡泡是否显示
	private boolean isPaused = false;	//是否是暂停
	private boolean shipinlist = false;//視頻列表是否
	private boolean voicepic = false;//声音调节图标是否显示
	private boolean isSilent=false;//是否是静音
	private String sizeString ;//localityactivity  传递过来的各种信息
	private int index;
	private Myadapter daMyadapter;
	private LinkedList<localityActivity.Moveinfo> list;
	private Vibrator vibrator;   //屏幕亮度控制器 震动管理器
	private int screenHeight ,screenWidth,controlHeight ;
	private Myonclick myonclick;
    private AudioManager mAudioManager = null; //音频管理
	private int maxVolume = 0;//最大声音
	private int currentVolume = 0;//当前声音  
	private PopupWindow mSoundWindow = null;//声音控制器
private ListView lView;
	private SoundView soundView;
	private Myonitem item;
	private View xxx;
@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//下三  設置為全屏 与tag不過必須得在 setContentView 前面設置
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
	list=localityActivity.getFilelist();//得到列表 list
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
				//动画可以根据 上次 的短信项目来设置    这里就不设置了 
			/*	TranslateAnimation	showAction = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
*/
						//本文来自编程入门网：http://www.bianceng.cn/OS/extra/201105/26837.htm
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
						controlView.setVisibility(View.GONE);//隐藏各种按钮
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
				if(isSilent){//是否静音
					//如长按声音按键前 是静音状态  那么声音按键 设置成有声 图片
					voice.setImageResource(R.drawable.soundcontrol);
				}else{
		     		//如长按声音按键前 是有音量状态  那么声音按键 设置成静音 图片
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
			videoView.stopPlayback();//停止视频播放
			new AlertDialog.Builder(Player.this)
            .setTitle("伤不起")//标题
            .setMessage("您所播的视频格式不正确，播放已停止。")//提示消息
            .setPositiveButton("知道了",//确定按钮
                    new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							videoView.stopPlayback();
						}
                    })
            .setCancelable(false)//设置撤销键 不好用
            .show();
			
			return false;
		}
	});
	
	
soundView=new SoundView(Player.this);
	
		soundView.setOnVolumeChangeListener(new OnVolumeChangedListener(){		
		public void setYourVolume(int index) {
			updateVolume(index);//更新音量
		} });
	mSoundWindow=new PopupWindow(soundView);
	             getScreenSize();
	 mGestureDetector = new GestureDetector(new SimpleOnGestureListener(){
	             	//双击  			
	     			public boolean onDoubleTap(MotionEvent e) {
	     				if(isFullScreen){//视频是否全屏显示状态
	     					//发现显示全屏状态，双击了 要的效果呢 是显示默认效果
	     					setVisible(1);
	     					if (popwindow) {
	     						controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,R.drawable.alpha ));
	    	     				
	     					controlView.setVisibility(View.VISIBLE);//显示各种按钮
							}
	     				}else{
	     					//发现显示非全屏状态，双击了 要的效果呢 是显示全屏效果
	     					setVisible(0);//设置视频显示尺寸
	     					controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,R.drawable.alpha ));
	     					
	     				 controlView.setVisibility(View.GONE);//隐藏各种按钮
	     				
	     				}
	     				isFullScreen = !isFullScreen;
	     				popwindow=!popwindow;
	     		   		return true;
	     			}

	     			//单击事件
	     			
	     			public boolean onSingleTapConfirmed(MotionEvent e) {//轻击屏幕
	     				Toast.makeText(Player.this, "是这里", 1).show();
	     				if (popwindow) {
	     					controlView.setVisibility(View.VISIBLE);//显示各种按钮
	     					controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,android.R.anim.fade_in ));
		     				
						}else {
							controlView.setVisibility(View.GONE);//隐藏各种按钮
							controlView.setAnimation(AnimationUtils.loadAnimation(Player.this,android.R.anim.fade_out ));
	     					
						}
	     				popwindow=!popwindow;
	     				//return super.onSingleTapConfirmed(e);
	     				return true;
	     			}

	     			//长按屏幕
	     			
	     			public void onLongPress(MotionEvent e) {
	      				if(isPaused){//暂停播放
	     				   	videoView.start();
	   	     				}else{
	    				     videoView.pause();
	     						}
	     				isPaused = !isPaused;
	     				//super.onLongPress(e);
	     		}

	     			//滑动 调节屏幕亮度
	     			
         public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
		float distanceY) {
				final double FLING_MIN_DISTANCE = 0.5;//最小距离  
                final double FLING_MIN_VELOCITY = 0.5;//最小触键力度  
            if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE  
                        && Math.abs(distanceY) > FLING_MIN_VELOCITY) {  
                                 //屏幕变亮
                    setBrightness(20);  
                }  
                if (e1.getY() - e2.getY() < FLING_MIN_DISTANCE  
                        && Math.abs(distanceY) > FLING_MIN_VELOCITY) {  
                              //屏幕变暗
                    setBrightness(-20);  
                }  
                return true;  
			}	
	     	 });
	//设置屏幕横屏
	 if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	 //获取声音 最大值 与当前值
	  mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
      maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	sizeString=this.getIntent().getStringExtra("size");
	index=Integer.parseInt(this.getIntent().getStringExtra("index"));


	uri=Uri.parse(this.getIntent().getStringExtra("path"));
	      if(uri!=null){//如果視頻地址帶過來了  設置 播放文件  uri
	             videoView.stopPlayback();//停止视频播放
	             videoView.setVideoURI(uri);//设置视频文件URI
	             stop.setImageResource(R.drawable.pause);
	         }else{
	        	   stop.setImageResource(R.drawable.play);
	        	 Toast.makeText(this,"错误", 1).show();
	        	 return;
	          }
    videoView.setOnPreparedListener(new OnPreparedListener(){
    	//注册在媒体文件加载完毕，可以播放时调用的回调函数
		
		public void onPrepared(MediaPlayer arg0) {//加载
		//视频显示尺寸  默认
			setVisible(1);
			isFullScreen = false;	
			videoView.start();  
			int i = videoView.getDuration();
			Log.d("onCompletion", ""+i);
			seekBar.setMax(i);
			i/=1000;//共多少 秒
			int minute = i/60;//共多少 分
			int hour = minute/60;//时
			int second = i%60;//秒
			minute =  minute % 60;//分
			endtextview.setText(String.format("%02d:%02d:%02d", hour,minute,second));
		handler.sendEmptyMessage(0);
		}		
    });  
   //进度条 改变 事件
    seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.i("i", "停止摸");
		}
		
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.i("i", "开始摸");
		}
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Log.i("i", "这个啥时候是摸");
			if (fromUser) {//如果是用户改变了 进度条
				videoView.seekTo(progress);
			}	
			handler.sendEmptyMessage(0);
		}
	});
    //当前 视频播放完后  继续播放下一个视频    //等会再测试
    videoView.setOnCompletionListener(new OnCompletionListener(){//注册在媒体文件播放完毕时调用的回调函数
public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			int n = list.size();
			if(++index < n){ //如果后面还有视频
				   videoView.setVideoURI(Uri.parse(list.get(index).localtionString));//设置视频文件URI
					}else{
						videoView.stopPlayback();
			Player.this.finish();
			}
		}
	});  
}
//更新音量
private void updateVolume(int index){//更新音量
	if(mAudioManager!=null&& index<maxVolume){//管理器不为空 并且不大于最大的音量
			//设置当前音量到 index
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
		case 0://进度条的改变
			int i=videoView.getCurrentPosition();
			seekBar.setProgress(i);
			i/=1000;//共多少 秒
			int minute = i/60;//共多少 分
			int hour = minute/60;//时
			int second = i%60;//秒
			minute =  minute % 60;//分
			staTextView.setText(String.format("%02d:%02d:%02d", hour,minute,second));
			sendEmptyMessageDelayed(0, 100);//这个相当于一个 循环 不停的给 这个handle 发送消息
			break;
			/*case 1:// 视频列表
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
	//设置视频显示尺寸
	switch(flag){
		case 0://全屏
			//Log.d(TAG, "screenWidth: "+screenWidth+" screenHeight: "+screenHeight);
			videoView.setVideoScale(screenWidth, screenHeight);
			//设置全屏显示效果 
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
							break;
					case 1://标准
			//获得VideoView的宽高
			int videoWidth = videoView.getVideoWidth();
			int videoHeight = videoView.getVideoHeight();
			//获得屏幕的宽高
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

			//取消全屏显示效果 
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
			break;
	}

	
}	
private void getScreenSize()//获得屏幕尺寸大小
{
	//获取手机屏幕的宽高
	Display display = getWindowManager().getDefaultDisplay();
	//手机屏幕的高度
     screenHeight = display.getHeight();
    //手机屏幕的宽度
     screenWidth = display.getWidth();
    //控制器高度  为屏幕高度的1/4
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
 * 设置屏幕亮度 带上震动
 * @param brightness
 */
/*listView.setOnItemClickListener(new Myonitem());*///不能在這裡直接設置
public void setBrightness(float brightness) {  
	    WindowManager.LayoutParams lp = getWindow().getAttributes();  
    
    //设置屏幕亮度属性   范围 0.0 --- 1.0  0.0全黑（切记不能设置成0.0）  1.0最亮
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
			Toast.makeText(getApplicationContext(), "已经是第一个视频",1).show();
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
			Toast.makeText(getApplicationContext(), "已经是最后一个视频",1).show();
		}
		break;
	case R.id.button5:
		//开始处理声音按钮  回去写吧
		Toast.makeText(getApplicationContext(), 4+"'",1).show();
		if (voicepic) {// 撤销显示
			mSoundWindow.dismiss();//SoundWindow销毁(不显示)
		}else {//显示
			if(mSoundWindow.isShowing()){
				mSoundWindow.update(0,0,SoundView.MY_WIDTH,SoundView.MY_HEIGHT);
			}else{
				//设置声音PopupWindow在VideoView的哪里显示
				//vv 参照的视图
				//Gravity.RIGHT|Gravity.CENTER_VERTICAL  是将声音PopupWindow附加在VideoView什么位置上 (右侧 垂直居中) 
				mSoundWindow.showAtLocation(videoView, Gravity.RIGHT|Gravity.CENTER_VERTICAL, 15, 0);
				//显示声音PopupWindow
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
