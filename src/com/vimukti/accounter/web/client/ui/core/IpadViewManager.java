package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.company.TransactionsCenterView;
import com.vimukti.accounter.web.client.ui.settings.InventoryCentreView;

/**
 * 
 * 
 */

public class IpadViewManager extends ViewManager {

	@Override
	protected StyledPanel createRightPanel() {
		// We don't need right panel in iPad so return null
		return null;
	}

	@Override
	public boolean isIpad() {
		return true;
	}

	@Override
	public ImageButton getCloseButton() {
		ImageButton closeButton = new ImageButton(Accounter.getFinanceImages()
				.ipadClose(), "cancel");
		closeButton.getElement().setId("closeButton");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				existingView.cancel();
			}
		});
		return closeButton;

	}

	@Override
	public ImageButton getAddNewButton() {

		addNewButton = new ImageButton(Accounter.getFinanceImages().ipadAdd(),
				"add");
		addNewButton.getElement().setId("addNewButton");
		addNewButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BaseListView baseListView;
				Action action;
				if (existingView instanceof TransactionsCenterView) {
					TransactionsCenterView centerView = (TransactionsCenterView) existingView;
					baseListView = centerView.baseListView;
					action = baseListView.getAddNewAction();
				} else if (existingView instanceof InventoryCentreView) {
					action = ((InventoryCentreView) existingView)
							.getAddNewAction();
				} else {
					baseListView = (BaseListView) existingView;
					action = baseListView.getAddNewAction();
				}
				if (action != null) {
					action.run(null, false);
				}
			}

		});
		return addNewButton;

	}

	@Override
	public ImageButton getSearchButton() {

		ImageButton searchButton = new ImageButton(Accounter.getFinanceImages()
				.ipadSearch(), "search");
		searchButton.getElement().setId("searchButton");
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// ActionFactory.getSearchInputAction().run();
				String historyToken = ActionFactory.getSearchInputAction()
						.getHistoryToken();
				History.newItem(historyToken, false);
				Accounter.getMainFinanceWindow().historyChanged(historyToken);
			}
		});
		return searchButton;
	}

	@Override
	public ImageButton getEditButton() {
		ImageButton editButton = new ImageButton(Accounter.getFinanceImages()
				.ipadEdit(), "edit");
		editButton.getElement().setId("editButton");
		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				viewTitleLabel.setText(existingView.getAction().getCatagory()
						+ "  >  " + existingView.getAction().getEditText());
				existingView.onEdit();
			}
		});
		return editButton;

	}

	@Override
	public ImageButton getExportButton() {
		ImageButton exportButton = new ImageButton(Accounter.getFinanceImages()
				.ipadDownload(), "savelocal");
		exportButton.getElement().setId("exportButton");
		exportButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				existingView.exportToCsv();

			}
		});
		return exportButton;

	}

	@Override
	public ImageButton createPrintButton() {
		ImageButton printButton = new ImageButton(Accounter.getFinanceImages()
				.ipadDownload(), "download");
		printButton.getElement().setId("printButton");
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				existingView.print();

			}
		});
		return printButton;

	}

	@Override
	public ImageButton getNextButton() {
		ImageButton nextButton = new ImageButton(Accounter.getFinanceImages()
				.nextIcon(), "next");
		nextButton.getElement().setId("nextButton");
		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.forward();
			}
		});
		return nextButton;

	}

	@Override
	public ImageButton getPreviousButton() {
		ImageButton previousButton = new ImageButton(Accounter
				.getFinanceImages().previousIcon(), "previous");
		previousButton.getElement().setId("previousButton");
		previousButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				History.back();
			}
		});
		return previousButton;

	}

}
