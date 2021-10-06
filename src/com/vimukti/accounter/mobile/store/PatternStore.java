/**
 * 
 */
package com.vimukti.accounter.mobile.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.mobile.AccounterMobileException;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.PatternResult;
import com.vimukti.accounter.mobile.Result;

/**
 * @author Prasanna Kumar G
 * 
 */
public class PatternStore {
	Logger log = Logger.getLogger(PatternStore.class);

	public static PatternStore INSTANCE = new PatternStore();

	private Map<String, PatternResult> patterns = new HashMap<String, PatternResult>();

	public Result find(String pattern, boolean isAuthenticated, Company company) {
		if (pattern == null || pattern.isEmpty()) {
			return null;
		}
		pattern = pattern.toLowerCase().trim();
		PatternResult result = patterns.get(pattern);
		if (result == null) {
			return null;
		}

		return result.render(isAuthenticated, company);
	}

	public void reload() throws AccounterMobileException {
		try {
			log.info("Loading Patterns...");
			XStream xStream = getPatternXStream();

			File file = getFile("");

			List<Object> objects = (List<Object>) xStream
					.fromXML(new FileInputStream(file));

			updateMap(objects);

		} catch (Exception e) {
			e.printStackTrace();
			throw new AccounterMobileException(e);
		}
	}

	private XStream getPatternXStream() {
		XStream xStream = new XStream(new DomDriver());

		xStream.alias("patterns", List.class);
		xStream.alias("include", String.class);
		xStream.alias("pattern", Pattern.class);
		xStream.aliasAttribute(Pattern.class, "condition", "if");
		xStream.aliasAttribute(Pattern.class, "login", "login");
		xStream.alias("input", String.class);
		xStream.addImplicitCollection(Pattern.class, "inputs");
		xStream.alias("text", Text.class);
		xStream.aliasAttribute(Text.class, "text", "name");
		xStream.alias("command", PCommand.class);
		xStream.aliasAttribute(PCommand.class, "condition", "if");
		xStream.aliasAttribute(PCommand.class, "title", "title");
		xStream.aliasAttribute(PCommand.class, "value", "value");
		return xStream;

	}

	/**
	 * @param objects
	 * @throws FileNotFoundException
	 */
	private void updateMap(List<Object> objects) throws FileNotFoundException {
		for (Object obj : objects) {
			if (obj instanceof String) {
				XStream xStream = getPatternXStream();
				File include = new File(ServerConfiguration.getMobileStore()
						+ File.separator + (String) obj);
				List<Object> fromXML = (List<Object>) xStream
						.fromXML(new FileInputStream(include));
				updateMap(fromXML);
			} else if (obj instanceof Pattern) {
				Pattern pattern = (Pattern) obj;
				PatternResult result = new PatternResult();
				CommandList commands = new CommandList();
				result.setOutputs(pattern.outputs);
				result.condition = pattern.condition;
				result.login = pattern.login;
				result.add(commands);
				if (pattern.inputs != null) {
					for (String input : pattern.inputs) {
						patterns.put(input.toLowerCase(), result);
					}
				}
			}
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private File getFile(String language) {
		return new File(ServerConfiguration.getMobileStore() + File.separator
				+ "patterns.xml");
	}

	public static void main(String[] args) throws AccounterMobileException {
		PatternStore.INSTANCE.reload();
	}
}
