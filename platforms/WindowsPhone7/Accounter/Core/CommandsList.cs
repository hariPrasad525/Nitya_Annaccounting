using System.Collections.Generic;

namespace Accounter.Core
{
	public class CommandsList
	{

		private List<Command> _commands = new List<Command>();

		public List<Command> Commands
		{
			get { return _commands; }
			set { _commands = value; }
		}

	}
}
