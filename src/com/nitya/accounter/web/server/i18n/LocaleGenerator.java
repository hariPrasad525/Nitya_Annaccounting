package com.nitya.accounter.web.server.i18n;

import java.io.PrintWriter;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class LocaleGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {

		JClassType classType;

		try {
			classType = context.getTypeOracle().getType(typeName);
			String className = typeName + "Generated";

			SourceWriter src = getSourceWriter(classType, context, logger);

			if (src != null) {
				src.println("@Override");
				src.println("public DateTimeFormatInfoImpl getDateTimeFormatInfo() {return new AccounterDateTimeFormatInfoImpl();}");
				src.println("@Override");
				src.println("public String getLocaleCookieName() {return \"default\";}");
				src.println("@Override");
				src.println("public NumberConstants getNumberConstants() {return new AccounterNumberConstantsImpl();}");
				src.println("@Override");
				src.println("public boolean hasAnyRTL() {return true;}");

				src.commit(logger);
			}
			return className;
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public SourceWriter getSourceWriter(JClassType classType,
			GeneratorContext context, TreeLogger logger) {
		String packageName = classType.getPackage().getName();
		String simpleName = classType.getSimpleSourceName() + "Generated";
		ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
				packageName, simpleName);
		composer.setSuperclass(classType.getQualifiedSourceName());

		// Need to add whatever imports your generated class needs.
		composer.addImport(classType.getQualifiedSourceName());
		composer.addImport("com.google.gwt.i18n.client.constants.NumberConstants");
		composer.addImport("com.google.gwt.i18n.client.impl.cldr.DateTimeFormatInfoImpl");
		composer.addImport("com.nitya.accounter.web.client.i18n.AccounterNumberConstantsImpl");
		composer.addImport("com.nitya.accounter.web.client.i18n.AccounterDateTimeFormatInfoImpl");

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}

}
