/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.mobile.MobileAdaptor.AdaptorType;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AccounterChatServer implements ChatManagerListener,
		MessageListener {

	Logger log = Logger.getLogger(AccounterChatServer.class);
	public static final int NETWORK_TYPE_GTALK = 1;
	public static final int NETWORK_TYPE_MOBILE = 2;
	public static final int NETWORK_TYPE_CONSOLE = 100;
	private MobileMessageHandler messageHandler;

	/**
	 * Creates new Instance
	 */
	public AccounterChatServer() {
		this.messageHandler = MobileMessageHandler.getInstance();
	}

	public void start() {
		log.info("Strating Chat server...");
		try {
			ConnectionConfiguration config = new ConnectionConfiguration(
					"talk.google.com", 5222, "gmail.com");

			XMPPConnection connection = new XMPPConnection(config);
			log.info("Connecting...");
			connection.connect();
			connection.login(ServerConfiguration.getChatUsername(),
					ServerConfiguration.getChatpassword());

			connection.getRoster().setSubscriptionMode(
					SubscriptionMode.accept_all);
			connection.getChatManager().addChatListener(this);
			connection.sendPacket(new Presence(Presence.Type.available, "",
					128, Presence.Mode.available));

			AccounterTimer.INSTANCE.schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						while (true) {
							Thread.sleep(30 * 60 * 1000);
						}
					} catch (Exception e) {
						// Nothing doing
					}
				}
			}, 500);
		} catch (Exception e) {
			log.error("Unable to Strart Chat Server.");
		}
		log.info("Chat Server Started....");
	}

	@Override
	public void processMessage(final Chat chat, Message msg) {

		String message = msg.getBody();
		String from = msg.getFrom();
		if (from.contains("/")) {
			from = from.substring(0, msg.getFrom().lastIndexOf("/"));
		}
		from += " eng";
		System.out.println("Message " + message);
		if (message != null) {
			MobileChannelContext context = new MobileChannelContext(from,
					message, AdaptorType.CHAT, NETWORK_TYPE_GTALK) {

				@Override
				public void send(String string) {
					try {
						chat.sendMessage(string);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void changeNetworkId(String networkId) {
					// TODO Auto-generated method stub

				}
			};

			messageHandler.putMessage(context);

		}
	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		chat.addMessageListener(this);
	}

}
