package player.zhang;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class List extends Activity {
	private LinkedList<localityActivity.Moveinfo> list;
	ListView lit;
	Myadapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
		list=localityActivity.getFilelist();
		adapter=new Myadapter();
	lit=(ListView) findViewById(R.id.playList);
	lit.setAdapter(adapter);
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
	
}
