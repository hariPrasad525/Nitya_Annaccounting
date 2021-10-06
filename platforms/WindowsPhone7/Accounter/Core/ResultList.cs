using System;
using System.Collections.Generic;

namespace Accounter.Core
{
	public class ResultList
	{

		private List<Record> _records = new List<Record>();

		private Boolean _isMultiSelectionEnabled;

		public String Title { get; set; }

		public List<Record> Records
		{
			get { return _records; }
			set { _records = value; }
		}

		public Boolean MultiSelectionEnable
		{
			get { return _isMultiSelectionEnabled; }
			set { _isMultiSelectionEnabled = value; }
		}

	}
}
