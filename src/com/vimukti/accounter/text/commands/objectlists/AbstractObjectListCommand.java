package com.vimukti.accounter.text.commands.objectlists;

import java.io.File;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;

public abstract class AbstractObjectListCommand extends CreateOrUpdateCommand {
	/**
	 * get Rename The Original File
	 * 
	 * @param originalFileName
	 * @param renameFileName
	 * @return
	 */
	protected String getRenameFilePath(String originalFileName,
			String renameFileName) {
		String tmpDir = ServerConfiguration.getTmpDir();
		File originalFile = new File(tmpDir, originalFileName);
		File renameFile = new File(tmpDir, renameFileName);
		originalFile.renameTo(renameFile);
		return renameFile.getAbsolutePath();
	}
}
