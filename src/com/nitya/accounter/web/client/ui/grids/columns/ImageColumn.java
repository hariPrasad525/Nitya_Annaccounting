/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ActionCell.Delegate;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class ImageColumn<T> extends Column<T, ImageResource> {

	/**
	 * Creates new Instance
	 */
	public ImageColumn() {
		super(new ImageResourceCell());
		ActionCell<ImageResource> actionCell = new ActionCell<ImageResource>(
				"", new Delegate<ImageResource>() {

					@Override
					public void execute(ImageResource object) {
						// TODO Auto-generated method stub

					}
				});
	}

}
