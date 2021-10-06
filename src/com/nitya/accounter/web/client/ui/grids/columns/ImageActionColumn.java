/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.resources.client.ImageResource;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class ImageActionColumn<R> extends
		CustomColumn<R, ImageResource> {

	/**
	 * Creates new Instance
	 */
	public ImageActionColumn() {
		super(new ImageActionCell());
		setFieldUpdater(this);
	}

	protected abstract void onSelect(int index, R object);

	public void update(int index, R object, ImageResource value) {
		onSelect(index, object);
	}

	@Override
	protected boolean enableSorting() {
		return false;
	}

}
