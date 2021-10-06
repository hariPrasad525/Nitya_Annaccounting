using System;
using System.Net;
using System.Collections.Generic;
using System.IO;
using Accounter.Core;
using System.Net.Sockets;
using System.Text;
using System.Globalization;
using System.Threading;
using System.Windows.Threading;


namespace Accounter.Service
{
	public class Connection
	{

		// The maximum size of the data buffer to use with the asynchronous socket methods
		const int MAX_BUFFER_SIZE = 2048;

		//Maximum Attempts
		const int MAX_ATEEMPTS = 20;

		internal String Cookie { get; set; }

		private String hostName;

		private int port;

		private List<ConnectionListener> _listeners = new List<ConnectionListener>();

		internal Boolean _isConnectedFirstTime;

		internal string LastMessage { get; set; }

		internal Boolean _isProcessingRequest;

		private SocketAsyncEventArgs _socketEventArg;

		private int attempts;

		private int interval;




		public Connection(String url, int port)
		{
			this.hostName = url;
			this.port = port;
		}

		internal void addListener(ConnectionListener connectionListener)
		{
			_listeners.Add(connectionListener);
		}



		public void Connect()
		{

			_socketEventArg = new SocketAsyncEventArgs();
			DnsEndPoint hostEntry = new DnsEndPoint(hostName, port);

			// Create a socket and connect to the server
			Socket sock = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

			_socketEventArg.Completed += new EventHandler<SocketAsyncEventArgs>(SocketEventArg_Completed);

			_socketEventArg.RemoteEndPoint = hostEntry;
			_socketEventArg.UserToken = sock;
			sock.ConnectAsync(_socketEventArg);
			sock.NoDelay = true;
		}

		// A single callback is used for all socket operations. 
		// This method forwards execution on to the correct handler 
		// based on the type of completed operation
		private void SocketEventArg_Completed(object sender, SocketAsyncEventArgs e)
		{
			switch (e.LastOperation)
			{
				case SocketAsyncOperation.Connect:
					ProcessConnect(e);
					break;
				case SocketAsyncOperation.Receive:
					ProcessReceive(e);
					break;
				case SocketAsyncOperation.Send:
					ProcessSend(e);
					break;
				default:
					throw new Exception("Invalid operation completed");
			}
		}

		// Called when a ConnectAsync operation completes
		private void ProcessConnect(SocketAsyncEventArgs e)
		{
			if (e.SocketError == SocketError.Success)
			{
				_isConnectedFirstTime = true;
				interval = 0;
				attempts = 0;
				// Successfully connected to the server
				foreach (ConnectionListener _listener in _listeners)
				{
					_listener.OnConnect();
				}
				String language = CultureInfo.CurrentCulture.TwoLetterISOLanguageName;

				if (Cookie == null)
				{
					SendMessage("Hello " + language);
				} else
				{
					SendMessage(Cookie + ' ' + language);
				}

			} else
			{
				ProcessConnectionFailed();
			}
		}

		private byte[] _receivedData = new byte[] { };
		private int _receivedDataLength;
		// Called when a ReceiveAsync operation completes
		// </summary>
		private void ProcessReceive(SocketAsyncEventArgs e)
		{
			if (e.SocketError == SocketError.Success)
			{
				// Received data from server
				MemoryStream stream = new MemoryStream();
				stream.Write(_receivedData, 0, _receivedData.Length);
				stream.Write(e.Buffer, e.Offset, e.BytesTransferred);
				stream.Position = 0;
				if (_receivedData.Length == 0)
				{
					byte[] lengthBytes = new byte[4];
					stream.Read(lengthBytes, 0, 4);
					Array.Reverse(lengthBytes);
					_receivedDataLength = BitConverter.ToInt32(lengthBytes, 0);
				}
				byte[] data = stream.ToArray();

				if (_receivedDataLength > 0 && data.Length - 4 < _receivedDataLength)
				{
					_receivedData = data;
					Socket sock = e.UserToken as Socket;
					_socketEventArg.SetBuffer(new Byte[2048], 0, 2048);
					bool willRaiseEvent = sock.ReceiveAsync(e);
				} else
				{
					_isProcessingRequest = false;
					String message = Encoding.UTF8.GetString(data, 4, _receivedDataLength);
					_receivedData = new byte[] { };
					MessageReceived(message);
				}

			} else
			{
				throw new SocketException((int)e.SocketError);
			}
		}


		// Called when a SendAsync operation completes
		private void ProcessSend(SocketAsyncEventArgs e)
		{
			if (e.SocketError == SocketError.Success)
			{
				foreach (ConnectionListener _listener in _listeners)
				{
					_listener.OnMessageSent();
				}
				Socket sock = e.UserToken as Socket;
				_socketEventArg.SetBuffer(new Byte[2048], 0, 2048);
				bool willRaiseEvent = sock.ReceiveAsync(e);
			} else
			{
				ProcessConnectionFailed();
			}
		}

		public void SendMessage(long value)
		{
			SendMessage(value.ToString());
		}
		public void SendMessage(string message)
		{
			if (_isProcessingRequest)
			{
				return;
			}
			_isProcessingRequest = true;
			if (message == null || message.Equals(""))
			{
				return;
			}
			byte[] data = Encoding.UTF8.GetBytes(message);
			MemoryStream stream = new MemoryStream();
			byte[] lengthBytes = BitConverter.GetBytes(data.Length);
			Array.Reverse(lengthBytes);
			stream.Write(lengthBytes, 0, lengthBytes.Length);
			stream.Write(data, 0, data.Length);
			byte[] buffer = stream.ToArray();
			if (_socketEventArg != null)
			{
				_socketEventArg.SetBuffer(buffer, 0, buffer.Length);
				Socket sock = _socketEventArg.UserToken as Socket;
				bool willRaiseEvent = sock.SendAsync(_socketEventArg);
			}
			LastMessage = message;
		}

		private void MessageReceived(String response)
		{
			Result result = AccounterDeserializer.INSTANCE.deserialize(response);
			if (result == null)
			{
				foreach (ConnectionListener listener in _listeners)
				{
					listener.OnDisconnect();
				}
				return;
			}
			if (result.Cookie != null)
			{
				Cookie = result.Cookie;
			}
			foreach (ConnectionListener listener in _listeners)
			{
				listener.OnMessageReceived(result);
			}
		}

		private void ProcessConnectionFailed()
		{
			if (!_isConnectedFirstTime)
			{
				foreach (ConnectionListener _listener in _listeners)
				{
					_listener.OnDisconnect();
				}
			} else
			{
				ConnectionFailed();
			}
		}

		private void ConnectionFailed()
		{
			if (attempts > 20)
			{
				attempts = 0;
				interval = 0;
				this._isConnectedFirstTime = false;
				ProcessConnectionFailed();
				return;
			}
			attempts++;
			if (interval == 0)
			{
				Connect();
				interval = 1;
			} else
			{
				System.Windows.Deployment.Current.Dispatcher.BeginInvoke(() =>
					{
						DispatcherTimer timer = GetTimer(interval);
						timer.Start();

					});
			}
			interval = interval + 1;
		}

		void OnTimerTick(object sender, EventArgs e)
		{
			DispatcherTimer timer = sender as DispatcherTimer;
			timer.Stop();
			Connect();
		}

		private DispatcherTimer _timer;
		private DispatcherTimer GetTimer(int intervals)
		{
			if(_timer==null){
				_timer = new DispatcherTimer();
				_timer.Interval = new TimeSpan(0, 0, intervals);
				_timer.Tick += OnTimerTick;
			}
			
			return _timer;
		}


		internal static Connection NewConnection()
		{
			return new Connection("mobile.accounterlive.com", 9083);
		}
	}
}
