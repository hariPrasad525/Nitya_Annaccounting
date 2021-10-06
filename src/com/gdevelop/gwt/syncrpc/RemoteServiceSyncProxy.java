/*
 * Copyright www.gdevelop.com.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.gdevelop.gwt.syncrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamFactory;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * Base on com.google.gwt.user.client.rpc.impl.RemoteServiceProxy
 */
public class RemoteServiceSyncProxy implements SerializationStreamFactory {
	private static class DummySerializationPolicy extends SerializationPolicy {
		public boolean shouldDeserializeFields(Class<?> clazz) {
			return clazz != null;
		}

		public boolean shouldSerializeFields(Class<?> clazz) {
			return clazz != null;
		}

		public void validateDeserialize(Class<?> clazz)
				throws SerializationException {
		}

		public void validateSerialize(Class<?> clazz)
				throws SerializationException {
		}
	}

	private String moduleBaseURL;
	private String remoteServiceURL;
	private String serializationPolicyName;
	private SerializationPolicy serializationPolicy;
	private SessionManager connectionManager;

	public RemoteServiceSyncProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			SessionManager connectionManager) {
		this.moduleBaseURL = moduleBaseURL;
		this.remoteServiceURL = moduleBaseURL + remoteServiceRelativePath;
		this.serializationPolicyName = serializationPolicyName;
		this.connectionManager = connectionManager;

		if (serializationPolicyName == null) {
			serializationPolicy = new DummySerializationPolicy();
		} else {
			// TODO
			if (true) {
				// serializationPolicy = new DummySerializationPolicy();
				// return;
			}

			String policyFileName = SerializationPolicyLoader
					.getSerializationPolicyFileName(serializationPolicyName);
			InputStream is = getClass().getResourceAsStream(
					"/" + policyFileName);
			try {
				serializationPolicy = SerializationPolicyLoader.loadFromStream(
						is, null);
			} catch (Exception e) {
				throw new InvocationException(
						"Error while loading serialization policy"
								+ serializationPolicyName, e);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// Ignore this error
					}
				}
			}
		}
	}

	public SyncClientSerializationStreamReader createStreamReader(String encoded)
			throws SerializationException {
		SyncClientSerializationStreamReader reader = new SyncClientSerializationStreamReader(
				serializationPolicy);
		reader.prepareToRead(encoded);
		return reader;
	}

	public SyncClientSerializationStreamWriter createStreamWriter() {
		SyncClientSerializationStreamWriter streamWriter = new SyncClientSerializationStreamWriter(
				null, moduleBaseURL, serializationPolicyName,
				serializationPolicy);
		streamWriter.prepareToWrite();

		return streamWriter;
	}

	public Object doInvoke(
			RequestCallbackAdapter.ResponseReader responseReader,
			String requestData) throws Throwable {
		HttpURLConnection connection = null;
		InputStream is = null;
		int statusCode;

		// Send request
		try {
			URL url = new URL(remoteServiceURL);
			connection = connectionManager.openConnection(url);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty(RpcRequestBuilder.STRONG_NAME_HEADER,
					serializationPolicyName);
			connection.setRequestProperty("Content-Type",
					"text/x-gwt-rpc; charset=utf-8");
			connection.setRequestProperty("Content-Length",
					"" + requestData.getBytes("UTF-8").length);

			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(requestData);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			throw new InvocationException(
					"IOException while sending RPC request", e);
		}

		// Receive and process response
		try {
			connectionManager.handleResponseHeaders(connection);
			statusCode = connection.getResponseCode();
			if (statusCode != HttpServletResponse.SC_OK) {
				switch (statusCode) {
				case HttpServletResponse.SC_NOT_FOUND:
					throw new AccounterException(
							"Company not found. company Id is wrong.");
				case HttpServletResponse.SC_UNAUTHORIZED:
					throw new AccounterException(
							"Your company is encrypted but user secret is not found. Please create user secret.");
				case HttpServletResponse.SC_NOT_ACCEPTABLE:
					throw new AccounterException(
							"Company is locked. Try after some time.");
				case HttpServletResponse.SC_BAD_REQUEST:
					throw new AccounterException(
							"User is not found. Wrong email Id.");
				case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
					throw new AccounterException("Your company is deleted");
				case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
					throw new AccounterException(
							"Could Not Complete the Request!");
				default:
					break;
				}
			}
			is = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) > 0) {
				baos.write(buffer, 0, len);
			}
			String encodedResponse = baos.toString("UTF8");

			// System.out.println("Response payload (len = " +
			// encodedResponse.length() + "): " + encodedResponse);
			if (statusCode != HttpURLConnection.HTTP_OK) {
				throw new StatusCodeException(statusCode, encodedResponse);
			} else if (encodedResponse == null) {
				// This can happen if the XHR is interrupted by the server dying
				throw new InvocationException("No Response Payload");
			} else if (isReturnValue(encodedResponse)) {
				encodedResponse = encodedResponse.substring(4);
				return responseReader.read(createStreamReader(encodedResponse));
			} else if (isThrownException(encodedResponse)) {
				encodedResponse = encodedResponse.substring(4);
				Throwable throwable = (Throwable) createStreamReader(
						encodedResponse).readObject();
				throw throwable;
			} else {
				throw new InvocationException("Unknown response"
						+ encodedResponse);
			}
		} catch (IOException e) {
			throw new InvocationException(
					"IOException while receiving RPC response", e);
		} catch (SerializationException e) {
			throw new InvocationException(
					"Error while deserialization response", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ignore) {
				}
			}
			if (connection != null) {
				// connection.disconnect();
			}
		}
	}

	static boolean isReturnValue(String encodedResponse) {
		return encodedResponse.startsWith("//OK");
	}

	static boolean isThrownException(String encodedResponse) {
		return encodedResponse.startsWith("//EX");
	}
}
