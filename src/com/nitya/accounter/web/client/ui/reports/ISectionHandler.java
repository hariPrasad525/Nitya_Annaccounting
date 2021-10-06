package com.nitya.accounter.web.client.ui.reports;


public interface ISectionHandler<R> {
	public void OnSectionAdd(Section<R> section);

	public void OnSectionEnd(Section<R> section);

}

