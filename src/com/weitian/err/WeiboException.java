package com.weitian.err;

public class WeiboException extends Exception{
	private static final long serialVersionUID = 1L;
    
    private String mExtra;

	public WeiboException() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public WeiboException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}


	public WeiboException(String detailMessage,String extra) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
		setmExtra(extra);
	}

	public String getmExtra() {
		return mExtra;
	}

	private void setmExtra(String mExtra) {
		this.mExtra = mExtra;
	}
    
}
