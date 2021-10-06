using System;
using System.Collections.Generic;

namespace Accounter.Core
{
	public class Result
	{
		private List<Object> _resultParts = new List<object>();

		private String _title;

		private String _cookie;

		private Boolean _hideCalcel;

		private Boolean _showBack;

		public String Title
		{
			get { return _title; }
			set { _title = value; }
		}

		public String Cookie
		{
			get { return _cookie; }
			set { _cookie = value; }
		}


		public Boolean HideCancel
		{
			get { return _hideCalcel; }
			set { _hideCalcel = value; }
		}

		public Boolean ShowBack
		{
			get { return _showBack; }
			set { _showBack = value; }
		}

		public List<Object> ResultParts
		{
			get { return _resultParts; }
			set { _resultParts = value; }
		}
	}
}
