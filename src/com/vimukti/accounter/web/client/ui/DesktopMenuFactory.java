package com.vimukti.accounter.web.client.ui;


public class DesktopMenuFactory implements IMenuFactory {

	@Override
	public IMenuBar createMenuBar() {
		return new DesktopCustomMenuBar(true);
	}

	@Override
	public IMenu createMenu() {
		return new DesktopCustomMenuBar(false);
	}

}
