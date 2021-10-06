package com.nitya.accounter.text;

import com.nitya.accounter.core.Address;
import com.nitya.accounter.core.FinanceDate;
import com.nitya.accounter.core.Quantity;

public interface ITextData {

	public String getType();

	public String nextString(String defVal);

	public Long nextNumber(Long defVal);

	public FinanceDate nextDate(FinanceDate defVal);

	public Address nextAddress(Address defVal);

	public Quantity nextQuantity(Quantity defVal);

	public boolean isDate();

	public double nextDouble(double defVal);

	public boolean hasNext();

	public boolean isDouble();

	public Double nextDouble();

	public void reset();

	public boolean isQuantity();

	/**
	 * forward the positions
	 */
	public void forward(int position);
}
