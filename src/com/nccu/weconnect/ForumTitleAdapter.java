package com.nccu.weconnect;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ForumTitleAdapter extends BaseExpandableListAdapter {

	String[] forum_subject, forum_date, forum_time, forum_place;
	int[] forum_id;
	Context context;

	public ForumTitleAdapter( Context context, int[] forum_id, String[] forum_subject, String[] forum_date, String[] forum_time, String[] forum_place ) {
		this.context = context;
		this.forum_id = forum_id;
		this.forum_subject = forum_subject;
		this.forum_date = forum_date;
		this.forum_time = forum_time;
		this.forum_place = forum_place;
	}

// Child x Child x Child x Child x Child x Child x Child

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		if( convertView == null ) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate( R.layout.forum_info_sample, null );

			TextView subjectTitle = (TextView) convertView.findViewById(R.id.subjectTitle);
			TextView date = (TextView) convertView.findViewById(R.id.date);
			TextView time = (TextView) convertView.findViewById(R.id.time);
			TextView location = (TextView) convertView.findViewById(R.id.location);

			subjectTitle.setText( forum_subject[groupPosition] );
			date.setText( forum_date[groupPosition] );
			time.setText( forum_time[groupPosition] + "~" );
			location.setText( forum_place[groupPosition] );

			Button btn = (Button) convertView.findViewById(R.id.checklist);
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent( context, TeamList.class );
					intent.putExtra( "id", forum_id[groupPosition] );

					context.startActivity( intent );
				}

			});
			
			Button showPic = (Button) convertView.findViewById(R.id.showpic);
			showPic.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent( context, IdentifyPic.class );
					intent.putExtra( "subject", forum_subject[groupPosition] );
					intent.putExtra( "date", forum_date[groupPosition] );
					intent.putExtra( "time", forum_time[groupPosition] );

					context.startActivity( intent );
				}

			});
			

//		}	// if convertView == null

		return convertView;

	}	// getChildView

// Group x Group x Group x Group x Group x Group x Group

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return forum_date.length;
	}
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		if( convertView == null ) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate( R.layout.forum_title_sample, null );

			TextView date = (TextView) convertView.findViewById(R.id.date);
			TextView time = (TextView) convertView.findViewById(R.id.time);

			date.setText( forum_date[groupPosition] );
			time.setText( forum_time[groupPosition] + "~" );

//		}	// if convertView == null

		return convertView;
	}

// I don't know xI don't know xI don't know xI don't know xI don't know xI don't know xI don't know

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
