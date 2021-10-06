package com.nitya.accounter.developer.api.process.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nitya.accounter.core.ClientConvertUtil;
import com.nitya.accounter.core.Job;
import com.nitya.accounter.web.client.core.ClientJob;
import com.nitya.accounter.web.client.core.Features;

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
