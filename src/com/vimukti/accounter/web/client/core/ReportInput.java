package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface ReportInput extends Serializable, IsSerializable {

	public static final int REPORT_EXPORT_TYPE_PDF = 1001;

	public static final int REPORT_EXPORT_TYPE_CSV = 1002;

}
