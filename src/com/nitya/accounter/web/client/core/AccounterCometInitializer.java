package com.nitya.accounter.web.client.core;

import java.io.Serializable;
import java.util.List;

import net.zschech.gwt.comet.client.CometClient;
import net.zschech.gwt.comet.client.CometListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.nitya.accounter.web.client.comet.AccounterCometSerializer;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.JNSI;

public class AccounterCometInitializer {

	
	private AccounterCometSerializer serializer;
	private CometClient cometClient;
	private boolean shouldReconnect = true;

	public AccounterCometInitializer() {

	}

	public void initiateComet() {
		serializer = GWT.create(AccounterCometSerializer.class);
		if (serializer != null)
			startCometService();
	}

	public void startCometService() {
		shouldReconnect = true;
		this.cometClient = new CometClient("/do/comet", serializer,
				new CometListener() {
					private int attempts;
					private int interval = 1;

					@Override
					public void onRefresh() {
						JNSI.log("onRefresh");
					}

					@Override
					public void onMessage(List<? extends Serializable> messages) {
						for (Serializable serializableObj : messages) {
							Accounter.getCompany().processCommand(
									serializableObj);
						}
					}

					@Override
					public void onHeartbeat() {
						JNSI.log("onHeartbeat");
					}

					@Override
					public void onError(Throwable exception, boolean connected) {
						JNSI.log("onError ->" + exception.getMessage());
						onDisconnected();
					}

					@Override
					public void onDisconnected() {
						JNSI.log("onDisconnected->" + interval);
						cometClient.stop();
						if (!shouldReconnect) {
							return;
						}
						if (attempts > 20) {
							attempts = 0;
							shouldReconnect = false;
							return;
						}
						attempts++;
						startTimer();
						interval++;

					}

					private void startTimer() {
						Scheduler.get().scheduleFixedDelay(
								new RepeatingCommand() {

									@Override
									public boolean execute() {
										JNSI.log("Re-Connecting->" + interval);
										cometClient.start();
										return false;
									}
								}, interval * 1000);
					}

					@Override
					public void onConnected(int heartbeat) {
						JNSI.log("onConnected->" + heartbeat);
						attempts = 0;
						interval = 0;
						shouldReconnect = true;
					}
				});
		cometClient.start();
	}

	public void stopComet() {
		if (cometClient != null) {
			this.cometClient.stop();
		}
		shouldReconnect = false;
		
	}

	
}
