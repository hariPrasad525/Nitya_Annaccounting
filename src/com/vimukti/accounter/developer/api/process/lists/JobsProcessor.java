package com.vimukti.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Job;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.Features;

public class JobsProcessor extends ListProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		checkPermission(Features.JOB_COSTING);
		ClientConvertUtil convertUtil = new ClientConvertUtil();
		List<ClientJob> resultList = new ArrayList<ClientJob>();
		Set<Job> jobs = getCompany().getJobs();
		for (Job job : jobs) {
			resultList.add(convertUtil.toClientObject(job, ClientJob.class));
		}
		sendResult(resultList);
	}

}
