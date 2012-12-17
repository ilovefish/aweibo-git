package com.weitian.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.weitian.types.Group;
import com.weitian.types.WeitianType;

public abstract class BaseGroupAdapter<T extends WeitianType> extends BaseAdapter {

	Group<T> group = null;
	
	public BaseGroupAdapter(Context context){
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (group == null) ? 0 : group.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return group.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public boolean hasStableIds(){
		return true;
	}
	
	public boolean isEmpty(){
		return (group == null) ? true : group.isEmpty();
	}
	
	public void setGroup(Group<T> g){
		group = g;
		//notifyDataSetInvalidated();
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
