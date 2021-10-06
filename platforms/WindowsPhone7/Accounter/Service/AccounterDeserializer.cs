using System;
using Accounter.Core;
using Newtonsoft.Json.Linq;

namespace Accounter.Service
{
	public class AccounterDeserializer
	{
		private static AccounterDeserializer _instance ;

		public static AccounterDeserializer INSTANCE
		{
			get
			{
				if (_instance == null)
				{
					_instance = new AccounterDeserializer();
				}
				return _instance;
			}
		}

		public Result deserialize(String json)
		{
			if (json == null || json.Equals(""))
			{
				return null;
			}
			Result result = new Result();
			try
			{
				JObject jObject = JObject.Parse(json);
				result.Cookie = (String)jObject["cookie"];
				result.Title = (String)jObject["title"];
				result.HideCancel = (Boolean)jObject["hideCancel"];
				result.ShowBack = (Boolean)jObject["showBack"];
				JArray jArray = (JArray)jObject["resultParts"];

				foreach (JObject obj in jArray)
				{
					int type = (int)obj["type"];
					switch (type)
					{
						case 0:
							result.ResultParts.Add((String)obj["message"]);
							break;
						case 1:
							ResultList resultList = new ResultList();
							resultList.MultiSelectionEnable = (Boolean)obj["isMultiSelection"];
							resultList.Title = (String)obj["title"];
							JArray records = (JArray)obj["records"];
							if (records != null)
							{
								foreach (JObject jRecord in records)
								{
									Record record = new Record();
									record.Code = (String)jRecord["code"];
									JArray cells = (JArray)jRecord["cells"];
									foreach (JObject jCell in cells)
									{
										Cell cell = new Cell();
										cell.Title = (String)jCell["title"];
										cell.Value = (String)jCell["value"];
										record.Cells.Add(cell);
									}
									resultList.Records.Add(record);
								}
							}
							result.ResultParts.Add(resultList);
							break;
						case 2:
							CommandsList commandsList = new CommandsList();
							JArray jCommands = (JArray)obj["commandNames"];
							if (jCommands != null)
							{
								foreach (JObject jCommand in jCommands)
								{
									Command command = new Command();
									command.Name = (String)jCommand["name"];
									command.Code = (String)jCommand["code"];
									commandsList.Commands.Add(command);
								}
							}
							result.ResultParts.Add(commandsList);
							break;
						case 3:
							InputType inputType = new InputType();
							inputType.Type = (int)obj["inputType"];
							inputType.Name = (String)obj["name"];
							inputType.Value = (String)obj["value"];
							result.ResultParts.Add(inputType);
							break;
					}
				}
			}
			catch (Exception exception)
			{
				Console.Write(exception.StackTrace);
			}

			return result;
		}
	}
}
