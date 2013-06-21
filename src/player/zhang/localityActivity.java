package player.zhang;

import java.util.LinkedList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class localityActivity  extends ListActivity
{
	private  ListView listView;
	private Myadapter adapter;
	private LayoutInflater mInflater;
	private static LinkedList<Moveinfo> filelist=new LinkedList<localityActivity.Moveinfo>();
	
	
	public static LinkedList<Moveinfo> getFilelist() {
		return filelist;
	}
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		listView=getListView();
		mInflater=getLayoutInflater();
		getMovepath();
		adapter=new Myadapter();
		listView.setAdapter(adapter);
			}
	
	class Moveinfo{
		String displayName;
		String localtionString;
		String sizeString;
		String dataString;
		BitmapDrawable image;
		
	}
	private class  Myadapter extends BaseAdapter{

		public int getCount() {
			return filelist.size();
		}

		public Object getItem(int position) {
			return filelist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final int i=position;
			
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.locationitem, null);
			}
			TextView text = (TextView) convertView.findViewById(R.id.movename);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			TextView size = (TextView) convertView.findViewById(R.id.size);
			ImageView image = (ImageView) convertView.findViewById(R.id.header);
			
			      image.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
		Toast.makeText(getApplicationContext(),filelist.get(i).localtionString , 1).show();
	              //localityActivity.this.setContentView(R.layout.dialog);
			Intent ii=new Intent(localityActivity.this,Player.class);
		    ii.putExtra("path",filelist.get(i).localtionString);
		    ii.putExtra("size",filelist.get(i).sizeString);
	    	ii.putExtra("index",String.valueOf(i));
		   startActivity(ii);	
		}
	});
			      if (filelist.get(position).image!=null) {
			    	  image.setBackgroundDrawable(filelist.get(position).image);
				}
			    
			      //缩略图 一会再设置
			text.setText(filelist.get(position).displayName);// 视频名称
			date.setText(filelist.get(position).dataString);
			size.setText(Integer.parseInt((filelist.get(position).sizeString))/1024/1024+"M");
				return convertView;
		}
		
	}
	 Cursor cursor;
	@Override
	protected void onDestroy() {
		if (cursor!=null&&!cursor.isClosed()) {
			cursor.close();
		}
		if (!filelist.isEmpty()) {
			filelist.clear();
		}
		super.onDestroy();
	}
public void getMovepath(){
	 ContentResolver contentResolver = this.getContentResolver();
	    String[] projection = new String[]{
	    		MediaStore.Audio.Media.TITLE,//文件名
	    		MediaStore.Audio.Media.DATA,//文件路径
	    		MediaStore.Audio.Media.SIZE//文件大小 /1024/1024
	    		,MediaStore.Video.Media.DATE_TAKEN,//文件创建日期
	    		MediaStore.Audio.Media.DURATION};//文件播放时长
	   	     cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, 
	            null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
	  	    if (cursor!=null) {
	    	if (cursor.getCount()>0) {
	    		while(cursor.moveToNext()){
	   	    	 System.out.println(cursor.getString(0)+"路径"+
	    		     cursor.getString(1)+"daxiao"
	   	    			 +cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
	   //	   System.out.println(DateFormat.getTimeFormat(this).format( cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE)))+"sc");
   //时长好像不管用
	   	    	 /*  String sbString=DateFormat.getTimeFormat(this).format( 
    		  cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
	  System.out.println(sbString); 	 
      StringBuffer sbBuffer=new StringBuffer();
      
	   	   sbBuffer.append(sbString.substring(sbString.indexOf(":")+1))
	   	   .append(":");
	   System.out.println(sbBuffer.toString()+"sbbuff");
*/
	  long dateTaken = cursor.getLong((cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)));  
	   	                String datetime = DateFormat.getDateFormat(this).format( dateTaken);
	   	         Moveinfo a=new Moveinfo();
	   	    	 a.displayName=cursor.getString(0);
	   	    	 a.localtionString= cursor.getString(1);
	   	      Bitmap bitmap=ThumbnailUtils.createVideoThumbnail( a.localtionString,Thumbnails.MINI_KIND);  
	          Bitmap bitmap1=ThumbnailUtils.extractThumbnail(bitmap,64,64);  
	          a.image =new BitmapDrawable (bitmap1);  
	   	    	 a.dataString=datetime;
	   	    	 a.sizeString=String.valueOf(cursor.getInt(2));
	   	    	filelist.add(a);
	   	    }
			}else {
				System.out.println("空");
			}
			
		}else {
			System.out.println("空");
		}
	    
}


}   
	       



   

/*private Bitmap createVideoThumbnail(String filePath) {  
        Bitmap bitmap = null; 
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
        try {  
           retriever.setMode(MediaMetadataRetriever.MODE_CAPTURE_FRAME_ONLY);  
            retriever.setDataSource(filePath);  
            bitmap = retriever;  
       } catch(IllegalArgumentException ex) {              // Assume this is a corrupt video file  
        } catch (RuntimeException ex) {  
           // Assume this is a corrupt video file.  
        } finally {              try {                  retriever.release();  
           } catch (RuntimeException ex) {  
                // Ignore failures while cleaning up.  
            }  
        }  
        return bitmap;  
    }*/  
     
/*private void getVideoFile(final List<videoItem> list)  
{  
  Bitmap bitmap = null;  
    
  ContentResolver mContentResolver = this.getContentResolver();  
        Cursor cursor = mContentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null,    
                null, null, MediaStore.Video.DEFAULT_SORT_ORDER);  
          
          
          
        if (cursor.moveToFirst())  
        {  
            do {  
                //ID：MediaStore.Audio.Media._ID   
               int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));    
                   
               //名称 ：MediaStore.Audio.Media.TITLE  
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));    
                                    //专辑名：MediaStore.Audio.Media.ALBUM                  String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));    
                   
                    
                //歌手名： MediaStore.Audio.Media.ARTIST   
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));    
                   
                //路径 ：MediaStore.Audio.Media.DATA   
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));    
                    
                //总播放时长 ：MediaStore.Audio.Media.DURATION  
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));    
                    
                //大小 ：MediaStore.Audio.Media.SIZE   
                int size = (int)cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));  
                 
                //拍摄时间  
                int dateTaken = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN));  
               String datetime = DateFormat.format("yyyy-MM-dd kk:mm:ss", dateTaken).toString();  
                               
               bitmap = createVideoThumbnail(url);  
  }  





        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int counter = cursor.getCount();
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        
        Log.w(TAG, "------------before looping, title = " + title);
        for(int j = 0 ; j < counter; j++){
            Log.w(TAG, "-----------title = "
                    + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            
                    cursor.moveToNext();
            
        }
        cursor.close();


	
	
	
//内存益处
public void getMovepath(final LinkedList<Moveinfo>list,final File file) {
	file.listFiles(new FileFilter() {
		
		public boolean accept(File pathname) {
			String nameString=file.getName();
			int i=nameString.indexOf(".");
				if (-1!=i) {
				nameString = nameString.substring(i);
				if(nameString.equalsIgnoreCase(".mp4")||nameString.equalsIgnoreCase(".3gp")
						||nameString.equalsIgnoreCase(".wmv")||nameString.equalsIgnoreCase(".avi")
						||nameString.equalsIgnoreCase(".rmvb")){
					Moveinfo mi = new Moveinfo();
					mi.displayName = file.getName();
					mi.localtionString = file.getAbsolutePath();
					list.add(mi);
					return true;
				}
				}else if (file.isDirectory()) {
				getMovepath(filelist, file);//如果是一个目录 就递归进去 再次查找
			}
				return false;
			
		}
	});
	
	
	

	
}

}
*/