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

public class CldrImplGenerator extends Generator {

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
				src.println("public native boolean isRTL()/*-{return $wnd['isRTL']; }-*/;");
				src.commit(logger);
			}
			// System.out.println(typeName + " Generated");
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
		// composer.addImplementedInterface(classType.getQualifiedSourceName());
		composer.setSuperclass("CldrImpl");
		// Need to add whatever imports your generated class needs.
		composer.addImport("com.google.gwt.i18n.client.impl.CldrImpl");

		PrintWriter printWriter = context.tryCreate(logger, packageName,
				simpleName);
		if (printWriter == null) {
			return null;
		} else {
			SourceWriter sw = composer.createSourceWriter(context, printWriter);
			return sw;
		}
	}

	public static native boolean isRTL()/*-{
		return true;
	}-*/;
}
