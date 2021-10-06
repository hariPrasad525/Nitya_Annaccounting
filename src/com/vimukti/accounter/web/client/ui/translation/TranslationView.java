package com.vimukti.accounter.web.client.ui.translation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.translate.ClientLanguage;
import com.vimukti.accounter.web.client.translate.ClientMessage;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.LanguageCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TranslationView extends AbstractPagerView<ClientMessage> {
	private LanguageCombo languageCombo;
	private SelectCombo optionsCombo;
	private List<String> optionsList;
	private Label selectedLanguageLabel;
	private StyledPanel dataPanel, mainPanel;
	private StyledPanel notePanel;
	private boolean canApprove;
	private TextItem searchItem;

	// private TextItem searchItem;

	public TranslationView() {
		super();
		this.getElement().setId("TranslationView");
	}

	@Override
	protected void createControls() {
		optionsList = new ArrayList<String>();
		optionsList.add(messages.untranslated());
		optionsList.add(messages.all());
		optionsList.add(messages.myTranslations());
		optionsList.add(messages.unConfirmed());

		optionsCombo = new SelectCombo(messages.show());
		for (int i = 0; i < optionsList.size(); i++) {
			optionsCombo.addItem(optionsList.get(i));
		}
		optionsCombo.setSelected(messages.untranslated());

		languageCombo = new LanguageCombo(messages.languages()) {
			@Override
			protected void updateData(ClientLanguage result) {
				languageSelected(result);
				pager.updateListData();
			}
		};

		languageCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientLanguage>() {

					@Override
					public void selectedComboBoxItem(ClientLanguage selectItem) {
						languageSelected(selectItem);
					}
				});

		optionsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						refreshPager();
						updateListData();
					}
				});
		searchItem = new TextItem(messages.searchHere(),"searchItem");
		searchItem.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				searchItem.getValue();
				updateListData();
			}
		});

		selectedLanguageLabel = new Label();
		if (languageCombo.getSelectedValue() != null) {
			selectedLanguageLabel.setText(messages
					.selectedTranslated(languageCombo.getSelectedValue()
							.getLanguageTooltip()));
		}

		notePanel = new StyledPanel("notePanel");
		Label noteLabel = new Label(messages.translateNote());
		notePanel.add(noteLabel);
		notePanel.addStyleName("translation_note");
		noteLabel.addStyleName("translation_note_label");

		pager = new Pager(5, this) {
			@Override
			protected void initData() {
				super.initData();
			}

			@Override
			public void updateListData() {
				if ((languageCombo.getSelectedValue() != null)
						&& (optionsCombo.getSelectedValue() != null)) {
					addMessagesToView();

				}
			}

			@Override
			public int getDataSize() {
				if (t != null) {
					return t.size();
				} else {
					return 0;
				}
			}
		};
		dataPanel = new StyledPanel("dataPanel");

		DynamicForm combosForm = new DynamicForm(" combosForm");
		combosForm.add(languageCombo, optionsCombo, searchItem);

		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(combosForm);
		mainPanel.add(notePanel);
		mainPanel.add(selectedLanguageLabel);
		mainPanel.add(dataPanel);

		selectedLanguageLabel.addStyleName("selected-language");
		this.add(mainPanel);
		this.add(pager);
	}

	@Override
	public void updateListData() {
		addMessagesToView();
	}

	public void languageSelected(ClientLanguage selectItem) {
		selectedLanguageLabel.setText(messages.selectedTranslated(selectItem
				.getLanguageTooltip()));
		Accounter.createTranslateService().canApprove(
				selectItem.getLanguageCode(), new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						setCanApprove(result);
						refreshPager();
						updateListData();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void addMessagesToView() {
		Accounter.createTranslateService().getMessages(
				languageCombo.getSelectedValue().getLanguageCode(),
				getStatus(optionsCombo.getSelectedValue()),
				pager.getStartRange(), pager.getRange(), searchItem.getValue(),
				new AsyncCallback<PaginationList<ClientMessage>>() {

					@Override
					public void onSuccess(PaginationList<ClientMessage> result) {
						if (result.size() != 0) {
							createNewDataPanel();
							pager.setTotalResultCount(result.getTotalCount());
							updateData(result);
							pager.setVisible(true);
							for (int i = 0; i < result.size(); i++) {
								addMessageToMessagePanel(result.get(i),
										canApprove);
							}
						} else {
							addEmptyMsg();
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
	}

	protected void addMessageToMessagePanel(ClientMessage result,
			boolean canApprove) {
		MessagePanel messagePanel = new MessagePanel(this, languageCombo
				.getSelectedValue().getLanguageCode(), result, canApprove);
		dataPanel.add(messagePanel);
	}

	protected void addEmptyMsg() {
		createNewDataPanel();
		Label error = new Label(messages.noRecordsToShow());
		dataPanel.add(error);
//		dataPanel.setCellHorizontalAlignment(error, HasAlignment.ALIGN_CENTER);
		pager.setVisible(false);
	}

	protected void createNewDataPanel() {
		if (dataPanel != null) {
			mainPanel.remove(dataPanel);
			dataPanel = new StyledPanel("dataPanel");
		}
		mainPanel.add(dataPanel);
		dataPanel.addStyleName("translated-result");
	}

	private int getStatus(String statusMessage) {
		if (statusMessage.equals(messages.untranslated())) {
			return ClientMessage.UNTRANSLATED;
		} else if (statusMessage.equals(messages.all())) {
			return ClientMessage.ALL;
		} else if (statusMessage.equals(messages.myTranslations())) {
			return ClientMessage.MYTRANSLATIONS;
		} else if (statusMessage.equals(messages.unConfirmed())) {
			return ClientMessage.UNCONFIRMED;
		}
		return 0;

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	private void setCanApprove(boolean canApprove) {
		this.canApprove = canApprove;
	}
}
