package player.zhang;

import java.util.LinkedList;

import player.zhang.localityActivity.Moveinfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NetworkActivity extends Activity{
	LinkedList<Moveinfo> filelist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
         filelist = localityActivity.getFilelist();
		//List view=new List(this,filelist);
		//setContentView(view);
	}
	
	
	private LayoutInflater mInflater;
	private ListView listView;
	private Myadapter adapter;
private void init() {
	mInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	listView=(ListView)mInflater.inflate(R.layout.list, null);
	TextView  tex=new TextView(this);
    tex.setText("Ò•îlÁÐ±í");

listView.addHeaderView(tex);
adapter=new Myadapter();
listView.setAdapter(adapter);
	}
private class Myadapter extends BaseAdapter{

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
	if (convertView==null) {
		convertView=mInflater.inflate(R.layout.list_item, null);
	}
		TextView textView= (TextView) convertView.findViewById(R.id.text22);
		textView.setText(filelist.get(position).displayName);
		return convertView;
	}
}
}