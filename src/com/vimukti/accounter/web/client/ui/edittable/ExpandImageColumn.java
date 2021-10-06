package com.vimukti.accounter.web.client.ui.edittable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.CellTree.Resources;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;

public class ExpandImageColumn<T> extends EditColumn<T> {

	private EditColumn<T> editColumn;
	Resources resources = GWT.create(CellTree.Resources.class);

	public ExpandImageColumn(EditColumn<T> column) {
		this.editColumn = column;
	}

	@Override
	protected String getColumnName() {
		return this.editColumn.getColumnName();
	}

	@Override
	public void render(IsWidget widget, RenderContext<T> context) {
		this.editColumn.render(this.editColumn.getWidget(context), context);
	}

	@Override
	public IsWidget getWidget(final RenderContext<T> context) {
		FlowPanel flowPanel = new FlowPanel();
		ExpandImage image = new ExpandImage();
		image.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ExpandImage exImg = (ExpandImage) event.getSource();
				ExpandImageColumn.this.toggleIcon(exImg);
			    ExpandImageColumn.this.getTable().onExpand(context.getRow(), exImg.getResource() == resources.cellTreeOpenItem());
			}
		});
		flowPanel.add(image);
		flowPanel.add(this.editColumn.getWidget(context));
		flowPanel.addStyleName("expandColumn");
		this.toggleIcon(image);
		return flowPanel;
	}

	private void toggleIcon(ExpandImage image) {
		if(image.getResource() == null || image.getResource() == resources.cellTreeOpenItem()) {
			image.setResource(resources.cellTreeClosedItem());
		} else {
			image.setResource(resources.cellTreeOpenItem());
		}
		
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	public int insertNewLineNumber() {
		return 0;
	}

	@Override
	public String getValueAsString(T row) {
		return "";
	}

	class ExpandImage extends Image {
		ImageResource img = null;

		@Override
		public void setResource(ImageResource resource) {
			img = resource;
			super.setResource(resource);
		}

		public ImageResource getResource() {
			return this.img;
		}

	}

}
