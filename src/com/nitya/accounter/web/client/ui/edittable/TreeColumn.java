package com.nitya.accounter.web.client.ui.edittable;

import com.google.gwt.user.client.ui.IsWidget;

public class TreeColumn<R> extends EditColumn<R> {

	private EditColumn<R> forParent;
	private EditColumn<R> forChild;
	private boolean oneForBoth;

	public TreeColumn(EditColumn<R> isForParent, EditColumn<R> isForChild) {
		this.forParent = isForParent;
		this.forChild = isForChild;
	}

	public TreeColumn(EditColumn<R> forAll) {
		this.forParent = forAll;
		this.oneForBoth = true;
	}

	public String getColumnName() {
		if (forParent != null) {
			return forParent.getColumnName();
		} else {
			return forChild.getColumnName();
		}
	}

	public EditColumn<R> getColumn(boolean isParent) {
		if (forParent != null && (isParent || this.oneForBoth)) {
			return getForParent();
		} else {
			return getForChild();
		}
	}

	@Override
	public void setTable(EditTable<R> editTableImpl) {
		if (forParent != null) {
			forParent.setTable(editTableImpl);
		} else {
			forChild.setTable(editTableImpl);
		}
		super.setTable(editTableImpl);
	}

	public EditColumn<R> getForParent() {
		return forParent;
	}

	public void setForParent(EditColumn<R> forParent) {
		this.forParent = forParent;
	}

	public EditColumn<R> getForChild() {
		return forChild;
	}

	public void setForChild(EditColumn<R> forChild) {
		this.forChild = forChild;
	}

	@Override
	public int getWidth() {
		if (forParent != null) {
			return forParent.getWidth();
		} else {
			return forChild.getWidth();
		}
	}

	@Override
	public String getValueAsString(R row) {
		if (forParent != null) {
			return forParent.getValueAsString(row);
		} else {
			return forChild.getValueAsString(row);
		}
	}

	@Override
	public int insertNewLineNumber() {
		if (forParent != null) {
			return forParent.insertNewLineNumber();
		} else {
			return forChild.insertNewLineNumber();
		}
	}

	@Override
	public void render(IsWidget widget, RenderContext<R> context) {
		if (forParent != null) {
			forParent.render(widget, context);
		} else {
			forChild.render(widget, context);
		}
	}

	@Override
	public IsWidget getWidget(RenderContext<R> context) {
		if (forParent != null) {
			return forParent.getWidget(context);
		} else {
			return forChild.getWidget(context);
		}
	}
	
	@Override
	protected void updateFromGUI(IsWidget widget, R row) {
		if (forParent != null) {
			forParent.updateFromGUI(widget, row);
		} else {
			forChild.updateFromGUI(widget, row);
		}
	}

}
