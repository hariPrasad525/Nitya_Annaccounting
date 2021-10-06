package com.nitya.accounter.taxreturn;

import java.util.List;

import com.nitya.accounter.taxreturn.core.Authentication;
import com.nitya.accounter.taxreturn.core.Body;
import com.nitya.accounter.taxreturn.core.Channel;
import com.nitya.accounter.taxreturn.core.ChannelRouting;
import com.nitya.accounter.taxreturn.core.GatewayAdditions;
import com.nitya.accounter.taxreturn.core.GovTalkMessage;
import com.nitya.accounter.taxreturn.core.GovtTalkDetails;
import com.nitya.accounter.taxreturn.core.Header;
import com.nitya.accounter.taxreturn.core.IDAuthentication;
import com.nitya.accounter.taxreturn.core.Key;
import com.nitya.accounter.taxreturn.core.Keys;
import com.nitya.accounter.taxreturn.core.MessageDetails;
import com.nitya.accounter.taxreturn.core.SenderDetails;
import com.nitya.accounter.taxreturn.vat.request.IRenvelope;
import com.nitya.accounter.taxreturn.vat.request.IRheader;
import com.nitya.accounter.taxreturn.vat.request.VATDeclarationRequest;

public class GovTalkMessageGenerator {
	public static GovTalkMessage getDeleteMessage(String correlationId) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("HMRC-VAT-DEC-TEST");
		messageDatails.setQualifier("request");
		messageDatails.setFunction("delete");
		messageDatails.setCorrelationID(correlationId);
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp(null);

		header.setSenderDatails(null);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		govtTalkDetails.setGatewayAdditions(null);
		govtTalkDetails.setChannelRoutings(null);

		Body body = message.getBody();
		body.setiRenvelope(null);
		return message;
	}

	public static GovTalkMessage getPollMessage(String correlationId) {
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("MOSWTSC2");
		messageDatails.setQualifier("poll");
		messageDatails.setFunction("submit");
		messageDatails.setCorrelationID(correlationId);
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp("");

		header.setSenderDatails(null);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		govtTalkDetails.setGatewayAdditions(null);

		Body body = message.getBody();
		body.setiRenvelope(null);
		return message;
	}

	public static GovTalkMessage getRequestMessage(DSPMessage dspMessage) {
		if (dspMessage == null) {
			return null;
		}
		GovTalkMessage message = new GovTalkMessage();
		message.setEnvelopVersion("2.0");
		Header header = message.getHeader();

		MessageDetails messageDatails = header.getMessageDatails();
		messageDatails.setClazz("HMCE-VAT-DEC");
		messageDatails.setQualifier("request");
		messageDatails.setFunction("submit");
		messageDatails.setCorrelationID("");
		messageDatails.setResponseEndPoint(null);
		messageDatails.setTransformation("XML");
		messageDatails.setGatewayTest(null);
		messageDatails.setGatewayTimestamp(null);

		SenderDetails senderDatails = header.getSenderDatails();
		senderDatails.setEmailAddress(dspMessage.getEmailId());
		IDAuthentication iDAuthentication = senderDatails.getiDAuthentication();
		iDAuthentication.setSenderId(dspMessage.getSenderId());
		List<Authentication> authentications = iDAuthentication
				.getAuthentications();
		Authentication authentication = new Authentication();
		authentication.setMethod("clear");
		authentication.setValue("testing1");
		authentication.setSignature(null);
		authentications.add(authentication);

		GovtTalkDetails govtTalkDetails = message.getGovtTalkDetails();
		Keys keys = govtTalkDetails.getKeys();
		Key key = new Key();
		key.setType("VATRegNo");
		key.setValue(dspMessage.getVatRegistrationNumber());
		keys.getKeys().add(key);

		govtTalkDetails.setTargetDetails(null);
		govtTalkDetails.setGatewayValidation(null);
		govtTalkDetails.setGovTalkErrors(null);
		GatewayAdditions gatewayAdditions = govtTalkDetails
				.getGatewayAdditions();
		gatewayAdditions.setType(null);
		gatewayAdditions.setValue(null);
		List<ChannelRouting> channelRoutings = govtTalkDetails
				.getChannelRoutings();

		ChannelRouting routing = new ChannelRouting();
		Channel channel = routing.getChannel();
		channel.setuRI("1995");
		channel.setProduct("Anna Accounting");
		channel.setVersion("2.02");
		channelRoutings.add(routing);

		Body body = message.getBody();
		IRenvelope iRenvelope = new IRenvelope();
		body.setiRenvelope(iRenvelope);
		iRenvelope.setvATDeclarationRequest(new VATDeclarationRequest(
				dspMessage.getTaxReturn()));

		IRheader iRheader = new IRheader();
		iRenvelope.setiRheader(iRheader);
		iRheader.getKeys().getKeys().add(key);
		iRheader.setSender("Individual");
		iRheader.setPeriodID("2011-1");
		iRheader.setPeriodStart("1");
		iRheader.setPeriodEnd("22");
		// IRmark

		return message;
	}
}
