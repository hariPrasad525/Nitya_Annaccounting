using System;

namespace Accounter.Core
{
	public class InputType
	{
		public const int INPUT_TYPE_NONE = 0;
		public const int INPUT_TYPE_STRING = 1;
		public const int INPUT_TYPE_NUMBER = 2;
		public const int INPUT_TYPE_AMOUNT = 3;
		public const int INPUT_TYPE_PASSWORD = 4;
		public const int INPUT_TYPE_EMAIL = 5;
		public const int INPUT_TYPE_PHONE = 6;
		public const int INPUT_TYPE_URL = 7;
		public const int INPUT_TYPE_DATE = 8;

		public int Type { get; set; }

		public String Name { get; set; }

		public String Value { get; set; }

	}
}
