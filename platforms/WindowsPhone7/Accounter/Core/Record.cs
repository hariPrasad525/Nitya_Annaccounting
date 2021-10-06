using System;
using System.Collections.Generic;

namespace Accounter.Core
{
	public class Record
	{
		private List<Cell> _cells = new List<Cell>();

		public String Code { get; set; }

		public List<Cell> Cells
		{
			get { return _cells; }
			set { _cells = value; }
		}

	}
}
