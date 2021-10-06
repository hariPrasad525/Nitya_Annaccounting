package com.vimukti.accounter.mobile;

import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.ssl.SslHandler;

import com.vimukti.accounter.main.ServerConfiguration;

public class MobileServer {
	private ServerBootstrap sslConnection;
	private ServerBootstrap normalConnection;
	private SSLContext sslCtx;

	public MobileServer() {
		sslConnection();
		normalConnection();
		createContext();
	}

	private void createContext() {
		try {
			KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

			// get user password and file input stream
			char[] password = ServerConfiguration.getKeyStorePassword()
					.toCharArray();
			java.io.FileInputStream fis = new java.io.FileInputStream(
					ServerConfiguration.getMobileKeyStore());
			ks.load(fis, password);
			fis.close();

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, ServerConfiguration.getKeyPassword().toCharArray());

			sslCtx = SSLContext.getInstance("TLS");

			sslCtx.init(kmf.getKeyManagers(), null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void normalConnection() {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		normalConnection = new ServerBootstrap(factory);

		normalConnection.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(
						new MobileDecoder(),
						new MobileChannelHandler(MobileMessageHandler
								.getInstance()));
			}
		});

		normalConnection.setOption("child.tcpNoDelay", true);
		normalConnection.setOption("child.keepAlive", true);
	}

	private void sslConnection() {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		sslConnection = new ServerBootstrap(factory);

		sslConnection.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				return Channels.pipeline(new SslHandler(getSSLEngine()),
						new MobileDecoder(), new MobileChannelHandler(
								MobileMessageHandler.getInstance()));
			}
		});

		sslConnection.setOption("child.tcpNoDelay", true);
		sslConnection.setOption("child.keepAlive", true);

	}

	protected SSLEngine getSSLEngine() {
		try {
			SSLEngine sslEngine = sslCtx.createSSLEngine();
			sslEngine.setUseClientMode(false);
			sslEngine.setNeedClientAuth(false);
			return sslEngine;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void strat() {
		normalConnection.bind(new InetSocketAddress(ServerConfiguration
				.getMobileChatServerPort()));
		sslConnection.bind(new InetSocketAddress(ServerConfiguration
				.getMobileSSLChatServerPort()));
	}
}
