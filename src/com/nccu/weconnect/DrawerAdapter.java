package com.nccu.weconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {

	Context context;
	String[] menuTitle, menuSubTitle;
	int[] menuIcon;
	int identify;

	public DrawerAdapter( Context context, String[] menuTitle,int[] menuIcon, int identify ) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.menuTitle = menuTitle;
		this.menuIcon = menuIcon;
		this.identify = identify;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuTitle.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return menuTitle[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if( convertView == null ) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if( identify == 0 ){
				convertView = inflater.inflate( R.layout.drawer_sample, parent, false);
			} else if( identify == 1 )
				convertView = inflater.inflate( R.layout.edu_sample, parent, false);

			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			TextView title = (TextView) convertView.findViewById(R.id.title);

			icon.setImageResource( menuIcon[position] );
			title.setText( menuTitle[position] );

		}	// if convertView == null

		return convertView;

	}	// getView

}
