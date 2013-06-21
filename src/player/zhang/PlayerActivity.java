package player.zhang;

import com.alex.media.TestMain;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;

public class PlayerActivity extends TabActivity {

	private TabHost mTabHost;
@Override
protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.main);
      mTabHost=getTabHost();
      
      add();
      mTabHost.setCurrentTab(0);
      TabWidget tabWidget = mTabHost.getTabWidget();
		int count = tabWidget.getChildCount();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		Log.i("test", "screenWidth=" + screenWidth);
		if (count > 2) {
			for (int i = 0; i < count; i++) {
				tabWidget.getChildTabViewAt(i).setMinimumWidth(
						(screenWidth) / 2);
			}
		}
}

//05-10 23:30:15.725: E/AndroidRuntime(1614): Caused by: 
	//java.lang.IllegalArgumentException: 
		//you must specify a way to create the tab indicator.

public void  add() {
		TabSpec mTabSpec = mTabHost.newTabSpec("location");
		mTabSpec.setIndicator(getString(R.string.name), getResources().getDrawable(R.drawable.tab_folder));
		Intent intent = new Intent(this,localityActivity.class);
		mTabSpec.setContent(intent);
		mTabHost.addTab(mTabSpec);
	    TabSpec mTabSpec2 = mTabHost.newTabSpec("ntework");
	    mTabSpec2.setIndicator(getString(R.string.name2), getResources().getDrawable(R.drawable.tab_group));
		Intent intent2 = new Intent(this,TestMain.class);
		mTabSpec2.setContent(intent2);
		mTabHost.addTab(mTabSpec2);
	}
}