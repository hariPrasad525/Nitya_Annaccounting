using System;

namespace Accounter.Core
{
	public class Cell
	{


		public String Title { get; set; }

		public String Value { get; set; }


		internal Boolean HasTitle()
		{
			return Title != null && !Title.Equals("");
		}

		internal Boolean HasValue()
		{
			return Value != null && !Value.Equals("");
		}

	}
}
