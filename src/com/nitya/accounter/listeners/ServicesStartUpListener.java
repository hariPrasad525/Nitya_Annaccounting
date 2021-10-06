package com.nitya.accounter.listeners;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.nitya.accounter.mail.EmailManager;
import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.main.upload.AttachmentFileServer;
import com.nitya.accounter.mobile.ConsoleChatServer;
import com.nitya.accounter.setup.server.DatabaseManager;
import com.nitya.accounter.web.server.InventoryRemappingService;
import com.nitya.accounter.web.server.RecurringTool;

public class ServicesStartUpListener implements ServletContextListener {
	private ExecutorService email;
	private ExecutorService attachement;
	private ExecutorService consoleChat;
	private ScheduledExecutorService recurring;
	private ScheduledExecutorService inventoryMapping;

	// private ScheduledExecutorService subsciption;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (ServerConfiguration.isStartUpCompleted()) {
					this.cancel();
					startServices();
				}
			}
		}, 5000, 5000);
	}

	private void startServices() {
		if (!ServerConfiguration.isDesktopApp()) {
			email = Executors.newSingleThreadExecutor();
			email.execute(new EmailManager());

			attachement = Executors.newSingleThreadExecutor();
			attachement.execute(new AttachmentFileServer());

			if (ServerConfiguration.isEnableConsoleChatServer()) {
				consoleChat = Executors.newSingleThreadExecutor();
				consoleChat.execute(new ConsoleChatServer());
			}

			recurring = Executors.newSingleThreadScheduledExecutor();
			recurring.scheduleAtFixedRate(new RecurringTool(), 0, 1,
					TimeUnit.HOURS);

			inventoryMapping = Executors.newSingleThreadScheduledExecutor();
			inventoryMapping.scheduleAtFixedRate(new InventoryRemappingService(),
					0, 1, TimeUnit.HOURS);

			// if (ServerConfiguration.isStopSchduleMails()) {
			// subsciption = Executors.newSingleThreadScheduledExecutor();
			// subsciption.scheduleAtFixedRate(new SubscriptionTool(), 0, 1,
			// TimeUnit.DAYS);
			// }
		} else {
			if (DatabaseManager.isDBConfigured()) {
				recurring = Executors.newSingleThreadScheduledExecutor();
				recurring.scheduleAtFixedRate(new RecurringTool(), 0, 1,
						TimeUnit.HOURS);
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (email != null) {
			email.shutdownNow();
		}
		if (attachement != null) {
			attachement.shutdownNow();
		}
		if (consoleChat != null) {
			consoleChat.shutdownNow();
		}
		if (recurring != null) {
			recurring.shutdownNow();
		}
		if (inventoryMapping != null) {
			inventoryMapping.shutdownNow();
		}
		// if (subsciption != null) {
		// subsciption.shutdownNow();
		// }

	}

}
