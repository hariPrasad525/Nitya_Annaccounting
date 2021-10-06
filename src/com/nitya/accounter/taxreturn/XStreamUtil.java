package com.nitya.accounter.taxreturn;

import com.nitya.accounter.taxreturn.core.Authentication;
import com.nitya.accounter.taxreturn.core.Body;
import com.nitya.accounter.taxreturn.core.Channel;
import com.nitya.accounter.taxreturn.core.ChannelRouting;
import com.nitya.accounter.taxreturn.core.GatewayAdditions;
import com.nitya.accounter.taxreturn.core.GatewayValidation;
import com.nitya.accounter.taxreturn.core.GovTalkError;
import com.nitya.accounter.taxreturn.core.GovTalkErrors;
import com.nitya.accounter.taxreturn.core.GovTalkMessage;
import com.nitya.accounter.taxreturn.core.GovtTalkDetails;
import com.nitya.accounter.taxreturn.core.Header;
import com.nitya.accounter.taxreturn.core.ID;
import com.nitya.accounter.taxreturn.core.IDAuthentication;
import com.nitya.accounter.taxreturn.core.Key;
import com.nitya.accounter.taxreturn.core.Keys;
import com.nitya.accounter.taxreturn.core.MessageDetails;
import com.nitya.accounter.taxreturn.core.ResponseEndPoint;
import com.nitya.accounter.taxreturn.core.SenderDetails;
import com.nitya.accounter.taxreturn.core.TargetDetails;
import com.nitya.accounter.taxreturn.vat.request.IRenvelope;
import com.nitya.accounter.taxreturn.vat.request.IRheader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtil {
	private static XStream xStream;

	public static XStream getXstream() {
		if (xStream == null) {
			xStream = new XStream(new DomDriver());
			addGovTalkMessage();
		}
		return xStream;
	}

	private static void addGovTalkMessage() {
		xStream.aliasType("GovTalkMessage", GovTalkMessage.class);
		// Alias
		xStream.aliasAttribute(GovTalkMessage.class, "envelopVersion",
				"EnvelopeVersion");
		xStream.aliasAttribute(GovTalkMessage.class, "header", "Header");
		xStream.aliasAttribute(GovTalkMessage.class, "govtTalkDetails",
				"GovTalkDetails");
		xStream.aliasAttribute(GovTalkMessage.class, "body", "Body");

		// Inner
		addHeader();
		addGovtTalkDetails();
		addBody();
	}

	private static void addHeader() {
		xStream.aliasType("Header", Header.class);

		// Alias
		xStream.aliasAttribute(Header.class, "messageDatails", "MessageDetails");
		xStream.aliasAttribute(Header.class, "senderDatails", "SenderDetails");

		// Inner
		addMessageDetails();
		addSenderDetails();
	}

	private static void addSenderDetails() {
		xStream.aliasType("SenderDetails", SenderDetails.class);

		// Alias
		xStream.aliasAttribute(SenderDetails.class, "iDAuthentication",
				"IDAuthentication");
		xStream.aliasAttribute(SenderDetails.class, "x509Certificate",
				"X509Certificate");
		xStream.aliasAttribute(SenderDetails.class, "emailAddress",
				"EmailAddress");

		addIDAuthentication();
	}

	private static void addIDAuthentication() {
		xStream.aliasType("IDAuthentication", IDAuthentication.class);

		// Alias
		xStream.aliasAttribute(IDAuthentication.class, "senderId", "SenderID");
		xStream.alias("authentication", Authentication.class);
		xStream.addImplicitCollection(IDAuthentication.class, "authentications");

		addAuthentication();
	}

	private static void addAuthentication() {
		xStream.aliasType("Authentication", Authentication.class);
	}

	private static void addMessageDetails() {
		xStream.aliasType("MessageDetails", MessageDetails.class);

		// Alias
		xStream.aliasAttribute(MessageDetails.class, "clazz", "Class");
		xStream.aliasAttribute(MessageDetails.class, "qualifier", "Qualifier");
		xStream.aliasAttribute(MessageDetails.class, "function", "Function");
		xStream.aliasAttribute(MessageDetails.class, "transactionID",
				"TransactionID");
		xStream.aliasAttribute(MessageDetails.class, "correlationID",
				"CorrelationID");
		xStream.aliasAttribute(MessageDetails.class, "responseEndPoint",
				"ResponseEndPoint");
		xStream.aliasAttribute(MessageDetails.class, "gatewayTimestamp",
				"GatewayTimestamp");
		xStream.aliasAttribute(MessageDetails.class, "auditID", "AuditID");
		xStream.aliasAttribute(MessageDetails.class, "transformation",
				"Transformation");
		xStream.aliasAttribute(MessageDetails.class, "gatewayTest",
				"GatewayTest");

		// Inner
		addResponseEndPoint();
	}

	private static void addResponseEndPoint() {
		xStream.aliasType("ResponseEndPoint", ResponseEndPoint.class);
		// xStream.alias("ResponseEndPoint", ResponseEndPoint.class);
		// xStream.useAttributeFor(ResponseEndPoint.class, "pollInterval");
		// xStream.aliasAttribute(ResponseEndPoint.class, "pollInterval",
		// "PollInterval");
		xStream.registerConverter(new Converter() {

			@Override
			public boolean canConvert(Class arg0) {
				return arg0.equals(ResponseEndPoint.class);
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader arg0,
					UnmarshallingContext arg1) {
				ResponseEndPoint point = new ResponseEndPoint();
				int attributeCount = arg0.getAttributeCount();
				if (attributeCount > 0) {
					String attribute = arg0.getAttribute(0);
					point.setPollInterval(attribute);
				}
				String value = arg0.getValue().trim();
				point.setValue(value);
				return point;
			}

			@Override
			public void marshal(Object arg0, HierarchicalStreamWriter arg1,
					MarshallingContext arg2) {
			}
		});
	}

	private static void addGovtTalkDetails() {
		xStream.aliasType("GovTalkDetails", GovtTalkDetails.class);

		// Alias
		xStream.aliasAttribute(GovtTalkDetails.class, "keys", "Keys");
		xStream.aliasAttribute(GovtTalkDetails.class, "targetDetails",
				"TargetDetails");
		xStream.aliasAttribute(GovtTalkDetails.class, "gatewayValidation",
				"GatewayValidation");
		xStream.alias("channelRouting", ChannelRouting.class);
		xStream.addImplicitCollection(GovtTalkDetails.class, "channelRoutings");
		xStream.aliasAttribute(GovtTalkDetails.class, "govTalkErrors",
				"GovTalkErrors");
		xStream.aliasAttribute(GovtTalkDetails.class, "gatewayAdditions",
				"GatewayAdditions");

		addKeys();
		addTargetDetails();
		addGatewayValidation();
		addChannelRouting();
		addGovTalkErrors();
		addGatewayAdditions();
	}

	private static void addGatewayAdditions() {
		xStream.aliasType("GatewayAdditions", GatewayAdditions.class);
	}

	private static void addGovTalkErrors() {
		xStream.aliasType("GovTalkErrors", GovTalkErrors.class);

		// Alias
		xStream.alias("error", GovTalkError.class);
		xStream.addImplicitCollection(GovTalkErrors.class, "errors");

		addError();
	}

	private static void addError() {
		xStream.aliasType("Error", GovTalkError.class);

		// Alias
		xStream.aliasAttribute(GovTalkError.class, "raisedBy", "RaisedBy");
		xStream.aliasAttribute(GovTalkError.class, "number", "Number");
		xStream.aliasAttribute(GovTalkError.class, "type", "Type");
		xStream.aliasAttribute(GovTalkError.class, "text", "Text");
		xStream.aliasAttribute(GovTalkError.class, "location", "Location");
	}

	private static void addChannelRouting() {
		xStream.aliasType("ChannelRouting", ChannelRouting.class);

		// Alias
		xStream.aliasAttribute(ChannelRouting.class, "channel", "Channel");
		xStream.aliasAttribute(ChannelRouting.class, "timestamp", "Timestamp");
		xStream.alias("iD", String.class);
		xStream.addImplicitCollection(ChannelRouting.class, "iDs");

		addChannel();
		addID();
	}

	private static void addID() {
		xStream.aliasType("ID", ID.class);

		// xStream.aliasAttribute(ID.class, "type", "Type");
		xStream.registerConverter(new Converter() {

			@Override
			public boolean canConvert(Class arg0) {
				return arg0.equals(ID.class);
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader arg0,
					UnmarshallingContext arg1) {
				ID id = new ID();
				int attributeCount = arg0.getAttributeCount();
				if (attributeCount > 0) {
					String attribute = arg0.getAttribute(0);
					id.setType(attribute);
				}
				String value = arg0.getValue().trim();
				id.setValue(value);
				return id;
			}

			@Override
			public void marshal(Object arg0, HierarchicalStreamWriter arg1,
					MarshallingContext arg2) {
			}
		});
	}

	private static void addChannel() {
		xStream.aliasType("Channel", Channel.class);

		// Alias
		xStream.aliasAttribute(Channel.class, "uRI", "URI");
		xStream.aliasAttribute(Channel.class, "name", "Name");
		xStream.aliasAttribute(Channel.class, "product", "Product");
		xStream.aliasAttribute(Channel.class, "version", "Version");
	}

	private static void addGatewayValidation() {
		xStream.aliasType("GatewayValidation", GatewayValidation.class);

		// Alias
		xStream.aliasAttribute(GatewayValidation.class, "processed",
				"Processed");
		xStream.aliasAttribute(GatewayValidation.class, "result", "Result");
	}

	private static void addTargetDetails() {
		xStream.aliasType("TargetDetails", TargetDetails.class);

		// Alias
		xStream.alias("organisation", String.class);
		xStream.addImplicitCollection(TargetDetails.class, "organisations");
	}

	private static void addKeys() {
		xStream.aliasType("Keys", Keys.class);

		// Alias
		xStream.alias("key", Key.class);
		xStream.addImplicitCollection(Keys.class, "keys");

		addKey();
	}

	private static void addKey() {
		xStream.aliasType("Key", Key.class);
	}

	private static void addBody() {
		xStream.aliasType("Body", Body.class);

		xStream.aliasAttribute(Body.class, "iRenvelope", "IRenvelope");
		addIRenvelope();
	}

	private static void addIRenvelope() {
		xStream.aliasType("IRenvelope", IRenvelope.class);

		addIRheader();

		// TODO
	}

	private static void addIRheader() {
		xStream.aliasType("IRheader", IRheader.class);

		xStream.aliasAttribute(IRheader.class, "periodID", "PeriodID");
		xStream.aliasAttribute(IRheader.class, "periodStart", "PeriodStart");
		xStream.aliasAttribute(IRheader.class, "periodEnd", "PeriodEnd");
		xStream.aliasAttribute(IRheader.class, "principal", "Principal");
		xStream.aliasAttribute(IRheader.class, "agent", "Agent");
		xStream.aliasAttribute(IRheader.class, "defaultCurrency",
				"DefaultCurrency");
		xStream.aliasAttribute(IRheader.class, "manifest", "Manifest");
		xStream.aliasAttribute(IRheader.class, "iRmark", "IRmark");
		xStream.aliasAttribute(IRheader.class, "sender", "Sender");

		addPrincipal();
		addAgent();
		addIRmark();
	}

	private static void addIRmark() {
		// TODO Auto-generated method stub

	}

	private static void addAgent() {
		// TODO Auto-generated method stub

	}

	private static void addPrincipal() {
		// TODO Auto-generated method stub

	}
}
