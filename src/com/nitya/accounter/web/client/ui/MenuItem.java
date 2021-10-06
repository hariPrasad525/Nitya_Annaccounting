package com.nitya.accounter.web.client.ui;

public class MenuItem {

	private String title;
	private String urlToken;
	private String shortcut;

	public MenuItem(String title, String token) {
		this.title = title;
		this.urlToken = token;
	}

	public MenuItem(String title) {
		this.title = title;
	}

	public MenuItem() {
	}

	public MenuItem(String title, String token, String shortcut) {
		this.title = title;
		this.urlToken = token;
		this.shortcut = shortcut;
	}

	public String getUrlToken() {
		return urlToken;
	}

	public void setUrlToken(String urlToken) {
		this.urlToken = urlToken;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isSeparator() {
		return title == null;
	}

	public String getShortcut() {
		return shortcut;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

}
