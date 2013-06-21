package com.alex.media;

import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import player.zhang.R;
public class AlbumListAdapter extends BaseAdapter{
	private Context myCon;
	private String[] albums;
	private HashMap<String, String> myMap;

	public AlbumListAdapter(Context con,String[] str1, HashMap<String, String> map){
		myCon = con;
		albums = str1;
		myMap = map;
	}
	
	
	public int getCount() {
		return albums.length;
	}
	public Object getItem(int position) {
		return position;
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(myCon).inflate(R.layout.albumslist,
				null);
		TextView album = (TextView)convertView.findViewById(R.id.album);
		if (albums[position].length()>15){
			album.setText(albums[position].substring(0, 12)+"...");
		} else {
			album.setText(albums[position]);
		}
	
		TextView artist = (TextView)convertView.findViewById(R.id.mysinger);
		if (albums[position].equals("sdcard")){
			artist.setText("δ֪");
		} else{
			artist.setText(myMap.get(albums[position]));
		}
		ImageView Albumsitem = (ImageView)convertView.findViewById(R.id.Albumsitem);
		Albumsitem.setImageResource(R.drawable.ic_launcher);
		return convertView;
	}
	
	

}
