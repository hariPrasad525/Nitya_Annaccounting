using Accounter.Core;

namespace Accounter.Service
{
	interface ConnectionListener
	{
		void OnMessageReceived(Result message);

		void OnMessageSent();

		void OnConnect();

		void OnDisconnect();

		void OnConnectionSuccess();
	}
}
