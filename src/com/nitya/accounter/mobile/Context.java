package com.nitya.accounter.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import com.nitya.accounter.core.AccounterThreadLocal;
import com.nitya.accounter.core.Client;
import com.nitya.accounter.core.Company;
import com.nitya.accounter.core.User;
import com.nitya.accounter.mobile.utils.StringUtils;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class Context {

	private MobileSession session;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private Map<String, List<Object>> selectedRecords = new HashMap<String, List<Object>>();

	private String networkId;
	private String string;
	private String commandString;

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	public int getNetworkType() {
		return networkType;
	}

	public void setNetworkType(int networkType) {
		this.networkType = networkType;
	}

	private int networkType;
	private ClientCompanyPreferences preferences;

	/**
	 * Creates new Instance
	 */
	public Context(MobileSession mobileSession) {
		this.session = mobileSession;
	}

	public Result forward(String command) {
		return null;
	}

	public void setString(String string) {
		this.string = string;
	}

	/**
	 * Returns the Date in the Inputs if Any
	 * 
	 * @return
	 */
	public ClientFinanceDate getDate() {
		List<ClientFinanceDate> dates = getDates();
		if (dates != null && !dates.isEmpty()) {
			return dates.get(0);
		}
		return null;

	}

	/**
	 * Returns the String in the Inputs if Any
	 * 
	 * @return
	 */
	public String getString() {
		// List<String> strings = getStrings();
		// if (strings != null && !strings.isEmpty()) {
		// return strings.get(0);
		// }
		return string;
	}

	public String getNumber() {
		List<Number> numbers = getNumbers();
		if (numbers != null && !numbers.isEmpty()) {
			return numbers.get(0).toString();
		}
		return null;
	}

	/**
	 * Returns the Strings in the Inputs if Any
	 * 
	 * @return
	 */
	public List<String> getStrings() {
		return (List<String>) this.attributes.get("string");

	}

	/**
	 * Returns the Dates in the Inputs if Any
	 * 
	 * @return
	 */
	public List<ClientFinanceDate> getDates() {
		return (List<ClientFinanceDate>) this.attributes.get("dates");

	}

	/**
	 * Returns the Integer in the Inputs if Any
	 * 
	 * @return
	 */
	public Integer getInteger() {
		List<Integer> integers = getIntegers();
		if (integers != null && !integers.isEmpty()) {
			return integers.get(0);
		}
		return null;

	}

	/**
	 * Returns the Integers in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Integer> getIntegers() {
		return (List<Integer>) this.attributes.get("integers");

	}

	/**
	 * Returns the Numbers in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Number> getNumbers() {
		return (List<Number>) this.attributes.get("numbers");

	}

	/**
	 * Returns the Doubles in the Inputs if Any
	 * 
	 * @return
	 */
	public List<Double> getDoubles() {
		return (List<Double>) this.attributes.get("doubles");

	}

	/**
	 * Returns the Double in the Inputs if Any
	 * 
	 * @return
	 */
	public Double getDouble() {
		List<Double> doubles = getDoubles();
		if (doubles != null && !doubles.isEmpty()) {
			return doubles.get(0);
		}
		return null;
	}

	public void setInputs(List<String> inputs) throws AccounterMobileException {
		try {
			List<Number> numbers = new ArrayList<Number>();
			List<Integer> integers = new ArrayList<Integer>();
			List<Double> doubles = new ArrayList<Double>();
			List<String> strings = new ArrayList<String>();
			List<ClientFinanceDate> dates = new ArrayList<ClientFinanceDate>();
			for (String string : inputs) {
				if (StringUtils.isInteger(string)) {
					int parseInt = Integer.parseInt(string);
					integers.add(parseInt);
					numbers.add(parseInt);
				}
				if (StringUtils.isDouble(string)) {
					double parseInt = Double.parseDouble(string);
					doubles.add(parseInt);
					numbers.add(parseInt);
				}
				if (StringUtils.isDate(string)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"dd/MM/yyyy");
					dateFormat.parse(string);
					ClientFinanceDate date = new ClientFinanceDate(
							dateFormat.parse(string));
					dates.add(date);
				}
				strings.add(string);
			}
			this.attributes.put("numbers", numbers);
			this.attributes.put("integers", integers);
			this.attributes.put("doubles", doubles);
			this.attributes.put("string", strings);
			this.attributes.put("dates", dates);
		} catch (Exception e) {
			throw new AccounterMobileException(
					AccounterMobileException.ERROR_INVALID_INPUTS, e);
		}
	}

	public Session getHibernateSession() {
		return session.getHibernateSession();
	}

	public Result makeResult() {
		return new Result();
	}

	/**
	 * Adds an Attribute to the Context
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		this.session.getCurrentCommand().setAttribute(name, value);
	}

	/**
	 * Gets an Attributes from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return this.session.getCurrentCommand().getAttribute(name);
	}

	/**
	 * Removes an Attribute from the Context
	 * 
	 * @param name
	 * @return
	 */
	public Object removeAttribute(String name) {
		return session.getCurrentCommand().removeAttribute(name);
	}

	public Object getLast(RequirementType type) {
		return this.session.getLast(type);
	}

	public void setLast(RequirementType type, Object obj) {
		this.session.setLast(type, obj);
	}

	/**
	 * @param record
	 */
	public void putSelection(String name, Object obj) {
		if (selectedRecords.containsKey(name)) {
			selectedRecords.get(name).add(0, obj);
		} else {
			List<Object> records = new ArrayList<Object>();
			records.add(obj);
			selectedRecords.put(name, records);
		}
	}

	/**
	 * Returns the User Selected Record if isSingleSelect Result
	 * 
	 * @return
	 */
	public <T> T getSelection(String name) {
		List<Object> list = selectedRecords.get(name);
		if (list != null && !list.isEmpty()) {
			return (T) selectedRecords.get(name).get(0);
		}
		return null;
	}

	/**
	 * Returns the User Selected Records
	 * 
	 * @return
	 */
	public <T> List<T> getSelections(String name) {
		return (List<T>) selectedRecords.get(name);
	}

	public ClientAddress getAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	public MobileSession getIOSession() {
		return session;
	}

	public String getEmailId() {
		return session.getClient().getEmailId();
	}

	public User getUser() {
		return session.getUser();
	}

	public Company getCompany() {
		return session.getCompany();
	}

	public void selectCompany(Company company, Client client) {
		session.setCompanyID(company.getID());
		Set<User> users = client.getUsers();
		for (User user : users) {
			if (user.getClient() == client) {
				session.setUser(user);
				AccounterThreadLocal.set(user);
			}
		}
	}

	public String getCommandString() {
		return commandString;
	}

	public void setCommandString(String commandString) {
		this.commandString = commandString;
	}

	public void removeSelection(Object selection) {
		selectedRecords.remove(selection);
	}

	public ClientCompanyPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(ClientCompanyPreferences preferences) {
		this.preferences = preferences;
	}
}
