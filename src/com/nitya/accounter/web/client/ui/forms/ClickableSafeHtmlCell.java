package com.nitya.accounter.web.client.ui.forms;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public class ClickableSafeHtmlCell implements Cell<SafeHtml> {

	private Set<String> events;
	private SafeHtmlRenderer<SafeHtml> renderer;

	public ClickableSafeHtmlCell() {
		this(CustomSafeHtmlRender.getInstance(), "click", "keydown");
	}

	public ClickableSafeHtmlCell(SafeHtmlRenderer<SafeHtml> renderer,
			String... consumedEvents) {
		events = null;
		if (consumedEvents != null && consumedEvents.length > 0) {
			events = new HashSet<String>();
			for (String event : consumedEvents) {
				events.add(event);
			}
		}
		this.renderer = renderer;
	}

	protected void onEnterKeyDown(Context context, Element parent,
			SafeHtml value, NativeEvent event,
			ValueUpdater<SafeHtml> valueUpdater) {
		if (valueUpdater != null) {
			valueUpdater.update(value);
		}
	}

	public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
		if (data != null) {
			renderer.render(data);
			sb.append(data);
		}
	}

	@Override
	public boolean dependsOnSelection() {
		return false;
	}

	@Override
	public Set<String> getConsumedEvents() {
		return events;
	}

	@Override
	public boolean handlesSelection() {
		return false;
	}

	@Override
	public boolean isEditing(com.google.gwt.cell.client.Cell.Context context,
			Element parent, SafeHtml value) {
		return false;
	}

	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
			Element parent, SafeHtml value, NativeEvent event,
			ValueUpdater<SafeHtml> valueUpdater) {
		String eventType = event.getType();
		// Special case the ENTER key for a unified user experience.
		if ("keydown".equals(eventType)
				&& event.getKeyCode() == KeyCodes.KEY_ENTER) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
		if ("click".equals(event.getType())) {
			onEnterKeyDown(context, parent, value, event, valueUpdater);
		}
	}

	@Override
	public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context,
			Element parent, SafeHtml value) {
		return false;
	}

	@Override
	public void setValue(com.google.gwt.cell.client.Cell.Context context,
			Element parent, SafeHtml value) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		render(context, value, sb);
		parent.setInnerHTML(sb.toSafeHtml().asString());
	}
}
