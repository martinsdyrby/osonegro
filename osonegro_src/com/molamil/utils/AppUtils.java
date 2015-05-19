package com.molamil.utils;

import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.molamil.osonegro.OsoNegroApp;

public class AppUtils {
	
	//TODO: Remove dependencies outside com.molamil package

	static public int toPX(int dp)
	{ 
		DisplayMetrics displayMetrics = OsoNegroApp.getAndroidActivity().getResources().getDisplayMetrics();
		return (int)((dp * displayMetrics.density) + 0.5);
	}

	static public int toDP(int px)
	{ 
		DisplayMetrics displayMetrics = OsoNegroApp.getAndroidActivity().getResources().getDisplayMetrics();
		return (int) ((px/displayMetrics.density)+0.5);
	}

	
	static public int getListViewHeight(ListView list)
	{
		return AppUtils.getListViewHeight(list, false);
	}
	
	static public int getListViewHeight(ListView list, Boolean scrollableHeightOnly) {
        ListAdapter adapter = list.getAdapter();
        	
        int listviewHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, list);

            mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
            				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            
            listviewHeight += mView.getMeasuredHeight();

        }
        
        listviewHeight += (list.getDividerHeight() * (adapter.getCount() - 1));
        
        if(scrollableHeightOnly)
        {
        	list.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED), 
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        	
        	listviewHeight -= list.getMeasuredHeight();
        }
        
        return listviewHeight;
	}

	public static Point getScreenSize() {
		Display display = OsoNegroApp.getAndroidActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}
}