package com.vimukti.accounter.text;

import java.util.ArrayList;

public class CommandsQueue {

	private ArrayList<ITextData> data = new ArrayList<ITextData>();

	private int cursor = 0;

	public CommandsQueue(ArrayList<ITextData> data) {
		this.data = data;
	}

	public boolean hasNext() {
		return data.size() != cursor;
	}

	private void increment() {
		cursor++;
	}

	private void decrement() {
		cursor--;
	}

	public ITextData take() {
		if (!hasNext()) {
			return null;
		}
		ITextData data = this.data.get(cursor);
		increment();
		return data;
	}

	public void revertPrevious() {
		decrement();
	}

}
