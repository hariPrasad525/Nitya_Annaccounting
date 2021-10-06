package com.nitya.accounter.taxreturn.core;

import com.nitya.accounter.taxreturn.vat.request.IRenvelope;

import net.n3.nanoxml.IXMLElement;
import net.n3.nanoxml.XMLElement;

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
