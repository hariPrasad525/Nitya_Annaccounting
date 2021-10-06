package com.nitya.accounter.core;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * @author vimukti2
 * 
 */
public class News {

	private long id;

	private Date sqlDate;

	private boolean sticky;

	private String url;

	private String body;
	
	private String title;

	public boolean isSticky() {
		return sticky;
	}

	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public Date getSqlDate() {
		return sqlDate;
	}

	public void setSqlDate(Timestamp sqlDate) {
		this.sqlDate = sqlDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
