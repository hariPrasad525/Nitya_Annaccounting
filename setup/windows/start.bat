@echo off
start jre\bin\javaw -classpath "libs/accounter.jar;libs/paypal_base.jar;libs/paypal_junit.jar;libs/opencsv-2.3.jar;libs/icu4j-4_8_1.jar;libs/gwt-comet-1.2.3.jar;libs/activation.jar;libs/adapter-core-1.2.3.823.jar;libs/adapter4gwt-1.2.3.823.jar;libs/ant-1.6.5.jar;libs/ant-contrib.jar;libs/antlr-2.7.6.jar;libs/aopalliance.jar;libs/args4j-2.0.7.jar;libs/asm-attrs.jar;libs/asm.jar;libs/bcprov-jdk15-133.jar;libs/beanlib-5.0.1beta.jar;libs/beanlib-hibernate-5.0.1beta.jar;libs/c3p0-0.9.1.jar;libs/cglib-2.1.3.jar;libs/commons-beanutils-1.7.0.jar;libs/commons-codec-1.3.jar;libs/commons-fileupload-1.2.1.jar;libs/commons-httpclient-3.1-rc1.jar;libs/commons-httpclient.jar;libs/commons-io-1.4.jar;libs/commons-lang-2.4.jar;libs/commons-logging-1.1.1.jar;libs/core-3.1.1.jar;libs/cos.jar;libs/dom4j-1.6.1-sources.jar;libs/dom4j-1.6.1.jar;libs/ezmorph-1.0.6.jar;libs/gdata-calendar-2.0.jar;libs/gdata-calendar-meta-2.0.jar;libs/gdata-client-1.0.jar;libs/gdata-client-meta-1.0.jar;libs/gdata-core-1.0.jar;libs/gin-1.5-post-gwt-2.2.jar;libs/google-collections-1.0-rc4.jar;libs/guice-2.0.jar;libs/guice-3.0-snapshot-20101120.jar;libs/guice-assistedinject-snapshot.jar;libs/guice-persist-3.0-snapshot-20101120.jar;libs/guice-servlet-3.0-snapshot-20101120.jar;libs/guice-snapshot.jar;libs/gwt-servlet.jar;libs/gwt-visualization.jar;libs/h2-1.1.115.jar;libs/hermes-1.2.0-sources.jar;libs/hermes-1.2.0.jar;libs/hibernate-commons-annotations-3.2.0.Final.jar;libs/hibernate-core-3.6.6.Final.jar;libs/hibernate-core-3.6.6.Final-sources.jar;libs/hibernate-jpa-2.0-api-1.0.1.Final.jar;libs/hibernate-util-1.2.3.823.jar;libs/httpclient-4.0.jar;libs/httpcore-4.0.1.jar;libs/ical4j.jar;libs/icu4j-3.4.4.jar;libs/isc-jakarta-oro-2_0_6.jar;libs/javassist-3.3.ga.jar;libs/javax.inject.jar;libs/jdom-1.1.jar;libs/jetty-servlet-tester-7.0.0pre3.jar;libs/jetty-util-6.1.0.jar;libs/json-lib-2.2.3-jdk15.jar;libs/json.jar;libs/servlet-api-3.0.jar;libs/jetty-client-8.0.0.v20110901.jar;libs/jetty-continuation-8.0.0.v20110901.jar;libs/jetty-io-8.0.0.v20110901.jar;libs/jetty-http-8.0.0.v20110901.jar;libs/jetty-jsp-2.1-7.5.0.v20110901.jar;libs/jetty-security-8.0.0.v20110901.jar;libs/jetty-server-8.0.0.v20110901.jar;libs/jetty-servlet-8.0.0.v20110901.jar;libs/jetty-servlets-8.0.0.v20110901.jar;libs/jetty-websocket-8.0.0.RC0.jar;libs/jetty-xml-8.0.0.v20110901.jar;libs/jetty-util-8.0.0.v20110901.jar;libs/jetty-webapp-8.0.0.v20110901.jar;libs/jsp-api-2.1.jar;libs/jsp-2.1.jar;libs/jstl-api-1.2.jar;libs/jstl-impl-1.2.jar;libs/jta-1.0.1.jar;libs/jug-1.1.2.jar;libs/junit-4.4.jar;libs/junit.jar;libs/log4j-1.2.16.jar;libs/lucene-core-2.4.1.jar;libs/lucene-snowball-2.4.1.jar;libs/mail.jar;libs/mysql-connector-java-5.1.5-bin.jar;libs/nanoxml-2.2.3.jar;libs/nekohtml-1.9.6.2.jar;libs/oauth-20090617.jar;libs/oauth-consumer-20090617.jar;libs/oauth-httpclient4-20090913.jar;libs/openid4java.jar;libs/openxri-client-1.2.0.jar;libs/openxri-syntax-1.2.0.jar;libs/pd4ml.jar;libs/pim.jar;libs/postgresql-9.1-901.jdbc4.jar;libs/s3lib.jar;libs/slf4j-api-1.6.1.jar;libs/slf4j-jcl-1.6.1.jar;libs/spring-test.jar;libs/spring.jar;libs/ss_css2.jar;libs/step2-common-1-SNAPSHOT.jar;libs/step2-consumer-1-SNAPSHOT.jar;libs/xalan-2.7.0.jar;libs/xml-apis-1.0.b2.jar;libs/xmlsec-1.3.0.jar;libs/xpp3-1.1.3.4d_b4_min.jar;libs/xstream-1.3.1.jar;libs/yguard.jar;libs/hibernate-c3p0-3.6.6.Final.jar;libs/smack.jar;libs/smackx.jar;libs/smackx-debug.jar;libs/smackx-jingle.jar;libs/ezmorph-1.0.6.jar;libs/itext-2.1.7.jar;libs/jdom-1.1.jar;libs/jettison-1.3.jar;libs/lucene-core-2.4.1.jar;libs/lucene-snowball-2.4.1.jar;libs/nanoxml-2.2.3.jar;libs/nekohtml-1.9.6.2.jar;libs/oauth-20090617.jar;libs/oauth-consumer-20090617.jar;libs/oauth-httpclient4-20090913.jar;libs/odfdom-java-0.8.7.jar;libs/openxri-client-1.2.0.jar;libs/openxri-syntax-1.2.0.jar;libs/oro-2.0.8.jar;libs/xercesImpl-2.9.1.jar;libs/xstream-1.3.1.jar;libs/netty-3.2.5.Final.jar;libs/gson-1.3.jar;libs/ooxml-schemas-1.1.jar;libs/poi-3.7.jar;libs/poi-ooxml-3.7.jar;libs/stax-api-1.0.1.jar;libs/xmlbeans-2.3.0.jar;libs/velocity-1.7.jar;libs/;libs/commons-io-2.0.1.jar;libs/fr.opensagres.xdocreport.converter-0.9.7.jar;libs/fr.opensagres.xdocreport.converter.docx.xwpf-0.9.7.jar;libs/fr.opensagres.xdocreport.core-0.9.7.jar;libs/fr.opensagres.xdocreport.document-0.9.7.jar;libs/fr.opensagres.xdocreport.document.docx-0.9.7.jar;libs/fr.opensagres.xdocreport.itext.extension-0.9.7.jar;libs/fr.opensagres.xdocreport.template-0.9.7.jar;libs/fr.opensagres.xdocreport.template.velocity-0.9.7.jar;libs/org.apache.poi.xwpf.converter-0.9.7.jar;libs/commons-collections-3.2.1.jar;libs/org.odftoolkit.odfdom.converter-0.9.7.jar;libs/fr.opensagres.xdocreport.converter.odt.odfdom-0.9.7.jar;libs/fr.opensagres.xdocreport.document.odt-0.9.7.jar" com.nitya.accounter.main.ServerMain -config "config\config.ini"
