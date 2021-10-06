/**
 * 
 */
package com.nitya.accounter.mobile;

import java.util.List;

/**
 * @author Prasanna Kumar G
 * 
 */
public class MobileChatAdaptor implements MobileAdaptor {

	public static MobileAdaptor INSTANCE = new MobileChatAdaptor();

	/**
	 * PostProcess the Result
	 * 
	 * @param result
	 * @return
	 */
	public String postProcess(Result result) {
		if (result == null) {
			return null;
		}
		List<Object> resultParts = result.getResultParts();
		StringBuffer reply = new StringBuffer();
		String resultTitle = result.getTitle();
		if (resultTitle != null) {
			reply.append(result.getTitle());
			reply.append('\n');
		}
		int recordsCount = 1;
		int commandIndex = 97;
		for (Object part : resultParts) {
			if (part instanceof ResultList) {
				reply.append('\n');
				ResultList resultList = (ResultList) part;
				String resultListTile = resultList.getTitle();
				if (resultListTile != null) {
					reply.append(resultListTile);
					reply.append('\n');
				}
				for (int x = 0; x < resultList.size(); x++, recordsCount++) {
					Record record = resultList.get(x);
					record.setCode(recordsCount);
					reply.append(record.toString());
				}
			} else if (part instanceof CommandList) {
				CommandList commandList = (CommandList) part;
				for (int x = 0; x < commandList.size(); x++, commandIndex++) {
					reply.append('(');
					reply.append((char) commandIndex);
					reply.append(") ");
					reply.append(commandList.get(x));
					reply.append('\n');
				}
			} else if (part instanceof InputType) {
				// TODO
			} else {
				reply.append((String) part);
				reply.append('\n');
			}
		}

		return reply.toString();
	}
}
