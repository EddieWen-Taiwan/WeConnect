package com.nccu.weconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class subjectAdapter extends BaseAdapter {

	String[] subject_title, subject_note;
	int[] subject;
	Context context;

	public subjectAdapter( Context context, String[] subject_title, String[] subject_note, int[] subject ) {
		this.context = context;
		this.subject_title = subject_title;
		this.subject_note = subject_note;
		this.subject = subject;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return subject_title.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return subject_title[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

//		if( convertView == null ) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate( R.layout.subject_sample, parent, false );

			ImageView image = (ImageView) convertView.findViewById(R.id.subject);
			TextView title = (TextView) convertView.findViewById(R.id.title);
			TextView note = (TextView) convertView.findViewById(R.id.note);

			image.setImageResource( subject[position] );
			title.setText( subject_title[position] );
			note.setText( subject_note[position] );

//		}	// if convertView == null

		return convertView;
	}

}
