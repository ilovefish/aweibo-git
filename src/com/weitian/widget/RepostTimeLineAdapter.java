package com.weitian.widget;

import java.util.Date;
import java.util.HashMap;

import com.weitian.MainActivity;
import com.weitian.R;
import com.weitian.types.Group;
import com.weitian.types.RepostTimeLine;
import com.weitian.types.User;
import com.weitian.util.StringFormatters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RepostTimeLineAdapter extends BaseGroupAdapter<RepostTimeLine> {
	
	private Activity mActivity = null;
	private LayoutInflater mLayoutInflater = null;
	private HashMap<String, String> mCachedRelativeTimestamps;
	
	public RepostTimeLineAdapter(Activity activity) {
		super(activity);
		mActivity = activity;
		mLayoutInflater = LayoutInflater.from(activity);
		mCachedRelativeTimestamps = new HashMap<String, String>();
		// TODO Auto-generated constructor stub
	}
	
	public void setGroup(Group<RepostTimeLine> group){
		super.setGroup(group);
		for(RepostTimeLine it: group){
			Date date = new Date(it.getCreated_at());
			mCachedRelativeTimestamps.put(it.getIdstr(), 
					(String) StringFormatters.getRelativeTimeSpanString(date.getTime()));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub		
		final ViewHolder viewHolder;
		if(convertView == null){
			convertView = mLayoutInflater.inflate(R.layout.reposttimeline, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
			viewHolder.text = (TextView) convertView.findViewById(R.id.text);
			viewHolder.name = (TextView) convertView.findViewById(R.id.username);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		RepostTimeLine group = (RepostTimeLine) getItem(position);
		User user = group.getUser();
		viewHolder.name.setText(user.getName());
		viewHolder.text.setText(group.getText());
		viewHolder.time.setText(mCachedRelativeTimestamps.get(group.getIdstr()));
		
		return convertView;
	}
	
	public static class ViewHolder{
		ImageView imageView;
		TextView name;
		TextView text;
		TextView time;
	}
	
}
