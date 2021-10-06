package com.vimukti.accounter.taxreturn;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.vimukti.accounter.core.Box;
import com.vimukti.accounter.core.TAXReturn;
import com.vimukti.accounter.taxreturn.core.GovTalkMessage;

public class Test {
	public static void main(String[] args) throws Exception {
		GovTalkMessage message = GovTalkMessageGenerator
				.getRequestMessage(getMessage());// BodyValue
		// fillPoll("");//Acknw
		// fillDelete("");//Response
		FileOutputStream stream = new FileOutputStream(
				new File(
						"C:/Users/vimukti04/Desktop/accounter/HMRC-VAT/LTS3.10/HMRCTools/TestData/New folder",
						"vatrequest.xml"));
		message.toXML(stream);
	}

	public static DSPMessage getMessage() {
		DSPMessage dspMessage = new DSPMessage();
		dspMessage.setSenderId("VATDEC205a01");
		dspMessage.setVatRegistrationNumber("999900001");
		TAXReturn taxReturn = new TAXReturn();
		List<Box> boxes = taxReturn.getBoxes();

		boxes.add(createBox(1, 100.50));
		boxes.add(createBox(2, 20.50));
		boxes.add(createBox(3, 121.00));
		boxes.add(createBox(4, 0.00));
		boxes.add(createBox(5, 121.00));
		boxes.add(createBox(6, 0));
		boxes.add(createBox(7, 0));
		boxes.add(createBox(8, 0));
		boxes.add(createBox(9, 0));

		dspMessage.setTaxReturn(taxReturn);
		return dspMessage;
	}

	private static Box createBox(int i, double d) {
		Box box = new Box();
		box.setBoxNumber(i);
		box.setAmount(d);
		return box;
	}
}
