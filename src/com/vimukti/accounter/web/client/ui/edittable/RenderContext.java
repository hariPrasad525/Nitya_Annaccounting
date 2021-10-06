package com.vimukti.accounter.web.client.ui.edittable;


public class RenderContext<R> {

	private R row;
	private boolean isDesable;

	public RenderContext(R row) {
		this.row = row;
	}


	public R getRow() {
		return row;
	}

	public boolean isDesable() {
		return isDesable;
	}

	public void setDesable(boolean isDesable) {
		this.isDesable = isDesable;
	}

}
