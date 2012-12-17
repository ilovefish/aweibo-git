package com.weitian.types;

import java.util.ArrayList;
import java.util.Collection;

public class Group<T extends WeitianType> extends ArrayList<T> implements WeitianType{

	private static final long serialVersionUID = 1L;

    private String mType;
	
	public Group() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Group(Collection<? extends T> collection) {
		super(collection);
		// TODO Auto-generated constructor stub
	}

	public Group(int capacity) {
		super(capacity);
		// TODO Auto-generated constructor stub
	}
	
	public String getmType() {
		return mType;
	}

	public void setmType(String mType) {
		this.mType = mType;
	}

}
