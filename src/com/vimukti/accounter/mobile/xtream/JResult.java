package com.vimukti.accounter.mobile.xtream;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.InputType;
import com.vimukti.accounter.mobile.ResultList;

public class JResult {
	List<Object> resultParts = new ArrayList<Object>();
	public String cookie;
	public String title;
	public boolean hideCancel;
	public boolean showBack;

	public void addAll(List<Object> resultParts) {
		int commandIndex = 97;
		int resultIndex = 1;
		for (Object object : resultParts) {
			if (object instanceof String) {
				Message message = new Message(object.toString());
				this.resultParts.add(message);
			} else if (object instanceof ResultList) {
				ResultList res = (ResultList) object;
				if (res.isEmpty()) {
					continue;
				}
				JResultList jResultList = new JResultList();
				resultIndex = jResultList.addAll(res, resultIndex);
				this.resultParts.add(jResultList);
			} else if (object instanceof CommandList) {
				JCommandList jResultList = new JCommandList();
				commandIndex = jResultList.addAll((CommandList) object,
						commandIndex);
				this.resultParts.add(jResultList);
			} else if (object instanceof InputType) {
				this.resultParts.add(object);
			}
		}
	}
}
