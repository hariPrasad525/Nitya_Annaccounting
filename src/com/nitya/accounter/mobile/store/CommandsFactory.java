/**
 * 
 */
package com.nitya.accounter.mobile.store;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nitya.accounter.main.ServerConfiguration;
import com.nitya.accounter.mobile.AccounterMobileException;
import com.nitya.accounter.mobile.Command;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CommandsFactory {

	Logger log = Logger.getLogger(CommandsFactory.class);

	private Map<String, Class<?>> commands = new HashMap<String, Class<?>>();

	public static CommandsFactory INSTANCE = new CommandsFactory();

	public void reload() throws AccounterMobileException {
		loadCommands();
	}

	/**
	 * Search for the Command in the CommandFactory and Returns the Command if
	 * Exists
	 * 
	 * @param commandName
	 * @return
	 */
	public Command getCommand(String commandName) {
		try {
			Class<?> clazz = commands.get(commandName.toLowerCase().trim());
			if (clazz == null) {
				return null;
			}
			return (Command) clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Loads All Commands
	 * 
	 * @throws AccounterMobileException
	 */
	private void loadCommands() throws AccounterMobileException {
		try {
			log.info("Loading Commands...");
			XStream xStream = new XStream(new DomDriver());

			xStream.alias("commands", List.class);

			xStream.alias("command", CommandTemplate.class);
			xStream.useAttributeFor(CommandTemplate.class, "name");
			xStream.useAttributeFor(CommandTemplate.class, "className");
			xStream.aliasField("clazz", CommandTemplate.class, "className");

			xStream.alias("alias", String.class);
			xStream.addImplicitCollection(CommandTemplate.class, "aliases");

			File file = getFile("");

			Object object = xStream.fromXML(new FileInputStream(file));

			List<CommandTemplate> commands = (List<CommandTemplate>) object;
			for (CommandTemplate command : commands) {
				Class<?> forName;
				try {
					forName = Class.forName(command.className);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}

				this.commands.put(command.name.toLowerCase(), forName);
				List<String> aliases = command.aliases;
				if (aliases == null) {
					command.aliases = new ArrayList<String>();
					aliases = command.aliases;
				}
				for (String alias : aliases) {
					this.commands.put(alias.toLowerCase(), forName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(
					AccounterMobileException.ERROR_INTERNAL, e);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private File getFile(String language) {
		return new File(ServerConfiguration.getMobileStore() + File.separator
				+ "commands.xml");
	}

	public static class CommandTemplate {
		String name;
		String className;
		List<String> aliases;

		public CommandTemplate() {

		}
	}

}
