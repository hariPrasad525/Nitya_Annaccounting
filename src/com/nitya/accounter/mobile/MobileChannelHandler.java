package com.nitya.accounter.mobile;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.nitya.accounter.mobile.MobileAdaptor.AdaptorType;
import com.nitya.accounter.utils.SecureUtils;

public class MobileChannelHandler extends SimpleChannelHandler {

	private MobileMessageHandler messageHandler;

	public MobileChannelHandler(MobileMessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	public void messageReceived(final ChannelHandlerContext ctx,
			final MessageEvent e) throws Exception {
		System.out.println("Got Message From: "
				+ ctx.getChannel().getRemoteAddress());

		String message = (String) e.getMessage();
		String networkId = (String) ctx.getAttachment();
		MobileChannelContext context = new MobileChannelContext(networkId,
				message, AdaptorType.MOBILE,
				AccounterChatServer.NETWORK_TYPE_MOBILE) {

			@Override
			public void send(String string) {
				System.out.println("Sending Result: to "
						+ ctx.getChannel().getRemoteAddress());
				ChannelBuffer copiedBuffer = ChannelBuffers.copiedBuffer(
						string, Charset.forName("UTF-8"));

				ByteBuffer buffer = ByteBuffer.allocate(copiedBuffer
						.writerIndex() + 4);
				buffer.putInt(copiedBuffer.writerIndex());
				copiedBuffer.getBytes(0, buffer);
				buffer.flip();
				ChannelBuffer wrappedBuffer = ChannelBuffers
						.wrappedBuffer(buffer);
				e.getChannel().write(wrappedBuffer);
			}

			@Override
			public void changeNetworkId(String networkId) {
				ctx.setAttachment(networkId);
			}
		};
		messageHandler.putMessage(context);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		System.out.println("Channel Connected From "
				+ ctx.getChannel().getRemoteAddress());
		ctx.setAttachment(SecureUtils.createID(16));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		System.out.println("Channel Disconnected From "
				+ ctx.getChannel().getRemoteAddress());
	}
}

class MobileDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel arg1,
			ChannelBuffer buff) throws Exception {

		ChannelBuffer allocate = (ChannelBuffer) ctx.getAttachment();
		if (allocate == null) {
			int capacity = buff.readableBytes();
			if (capacity < 4) {
				return null;
			}
			int length = buff.readInt();
			allocate = ChannelBuffers.buffer(length);
		}
		if (buff.readableBytes() > 0) {
			buff.readBytes(allocate);
		}
		if (!allocate.writable()) {
			// finished reading all
			ctx.setAttachment(null);
			return allocate.toString(Charset.forName("UTF-8"));
		} else {
			// Set as attachment so that we can read the rest later
			ctx.setAttachment(allocate);
			return null;
		}
	}
}