package com.vimukti.accounter.taxreturn.core;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

import com.vimukti.accounter.taxreturn.vat.request.IRenvelope;

public class Body {
	private IRenvelope iRenvelope;

	public IXMLElement toXML() {
		XMLElement bodyElement = new XMLElement("Body");
		if (iRenvelope != null) {
			iRenvelope.toXML(bodyElement);
		}
		return bodyElement;
	}

	public IRenvelope getiRenvelope() {
		return iRenvelope;
	}

	public void setiRenvelope(IRenvelope iRenvelope) {
		this.iRenvelope = iRenvelope;
	}
}
