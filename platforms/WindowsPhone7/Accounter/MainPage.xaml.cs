using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using Microsoft.Phone.Controls;
using Accounter.Service;
using Accounter.Core;
using System.Windows.Navigation;
using Microsoft.Phone.Shell;

namespace Accounter
{
	public partial class MainPage : PhoneApplicationPage, ConnectionListener
	{
		Connection _connection { get; set; }

		// Constructor
		public MainPage()
		{
			InitializeComponent();
			this.Loaded += new RoutedEventHandler(MainPage_Loaded);
		}

		private void MainPage_Loaded(object sender, RoutedEventArgs e)
		{
			if (_connection != null && _connection._isConnectedFirstTime)
			{
				return;
			}
			this._connection = Connection.NewConnection();
			this._connection.addListener(this);
			this._connection.Connect();
			Progress.Visibility = Visibility.Visible;
			//this.NavigationService.Navigated += RootFrame_Navigated;
		}

		private void recreateView(Result result)
		{
			foreach (Object resultPart in result.ResultParts)
			{
				if (resultPart is String)
				{
					addTextResult(resultPart as String);
				} else if (resultPart is CommandsList)
				{
					addCommand(resultPart as CommandsList);
				} else if (resultPart is ResultList)
				{
					addResultList(resultPart as ResultList);
				} else if (resultPart is InputType)
				{
					addInputType(resultPart as InputType);
				}
			}

		}
		private void addInputType(InputType inputType)
		{

			if (inputType.Type == InputType.INPUT_TYPE_PASSWORD)
			{
				PasswordBox passwordBox = new PasswordBox();
				passwordBox.Password = inputType.Value;
				ContentPanel.Children.Add(passwordBox);
				passwordBox.Focus();
				passwordBox.LostFocus += new RoutedEventHandler(TextChanged_Clicked);
				passwordBox.KeyDown += OnKeyDownHandler;
			} else if (inputType.Type == InputType.INPUT_TYPE_DATE)
			{
				DatePicker datePicker = new DatePicker();
				datePicker.Header = inputType.Name;
				datePicker.Value = toDateTime(inputType.Value);
				ContentPanel.Children.Add(datePicker);
				datePicker.Focus();
				datePicker.ValueChanged += Date_Changed;

			} else
			{
				TextBox textBox = new TextBox();
				textBox.Text = inputType.Value;
				InputScope Keyboard = new InputScope();
				InputScopeName ScopeName = new InputScopeName();
				switch (inputType.Type)
				{
					case InputType.INPUT_TYPE_DATE:
						ScopeName.NameValue = InputScopeNameValue.Date;
						break;
					case InputType.INPUT_TYPE_AMOUNT:
						ScopeName.NameValue = InputScopeNameValue.CurrencyAmount;
						break;
					case InputType.INPUT_TYPE_EMAIL:
						ScopeName.NameValue = InputScopeNameValue.EmailUserName;
						break;
					case InputType.INPUT_TYPE_NUMBER:
						ScopeName.NameValue = InputScopeNameValue.Number;
						break;
					case InputType.INPUT_TYPE_PASSWORD:
						ScopeName.NameValue = InputScopeNameValue.Password;
						break;
					case InputType.INPUT_TYPE_PHONE:
						ScopeName.NameValue = InputScopeNameValue.TelephoneNumber;
						break;
					case InputType.INPUT_TYPE_URL:
						ScopeName.NameValue = InputScopeNameValue.Url;
						break;
					case InputType.INPUT_TYPE_STRING:
						ScopeName.NameValue = InputScopeNameValue.Text;
						break;
				}
				Keyboard.Names.Add(ScopeName);
				textBox.InputScope = Keyboard;
				ContentPanel.Children.Add(textBox);
				textBox.Focus();
				textBox.LostFocus += new RoutedEventHandler(TextChanged_Clicked);
				textBox.KeyDown += OnKeyDownHandler;
			}
		}

		private void OnKeyDownHandler(object sender, KeyEventArgs e)
		{
			if (e.Key != Key.Enter)
			{
				return;
			}
			TextChanged_Clicked(sender, e);
		}


	
		private void TextChanged_Clicked(object sender, RoutedEventArgs e)
		{
			if (sender is PasswordBox)
			{
				PasswordBox passBox = sender as PasswordBox;
				if (passBox.Tag != null && passBox.Tag.Equals("Fired"))
				{
					return;
				}
				if (passBox.Password != null && !passBox.Password.Equals(""))
				{
					_connection.SendMessage(passBox.Password);
					passBox.Tag = "Fired";
				}
				
			} else
			{
				TextBox texBox = sender as TextBox;
				if (texBox.Tag != null && texBox.Tag.Equals("Fired"))
				{
					return;
				}
				if (texBox.Text != null && !texBox.Text.Equals(""))
				{
					_connection.SendMessage(texBox.Text);
					texBox.Tag = "Fired";
				}
				
			}
		}

		private void Date_Changed(object sender, DateTimeValueChangedEventArgs e)
		{
			DatePicker datePicker = sender as DatePicker;
			DateTime dateTime = e.NewDateTime.Value;
			_connection.SendMessage(toFinanceDate(dateTime));
		}

		private void addTextResult(String text)
		{
			TextBlock textBlock = new TextBlock()
			{
				Text = text,
				TextWrapping = TextWrapping.Wrap,
				Margin = new Thickness(10, 2, 10, 2),
				FontSize = 20
			};
			ContentPanel.Children.Add(textBlock);
		}

		private void addCommand(CommandsList commandList)
		{

			StackPanel commandPane = new StackPanel();
			commandPane.Margin = new Thickness(0, 3, 0, 3);
			foreach (Command command in commandList.Commands)
			{
				Button commandBtn = new Button()
				{
					Content = command.Name,
					Name = command.Code,
					BorderThickness = new Thickness(0),
					Foreground = new SolidColorBrush(Colors.Black),
					Background = new SolidColorBrush(Colors.LightGray)
				};
				commandBtn.Click += new RoutedEventHandler(Button_Clicked);
				commandPane.Children.Add(commandBtn);
			}
			ContentPanel.Children.Add(commandPane);
		}

		private void Button_Clicked(object sender, RoutedEventArgs e)
		{
			Button btn = sender as Button;
			_connection.SendMessage(btn.Name);
		}

		private void addResultList(ResultList resultList)
		{
			Grid grid = new Grid();
			List<Record> resultRecords = resultList.Records;
			foreach (Record gridRecord in resultRecords)
			{
				foreach (Cell cell in gridRecord.Cells)
				{
					RowDefinition rowDef = new RowDefinition() { Height = GridLength.Auto };
					grid.RowDefinitions.Add(rowDef);
				}
			}

			GridLength cellWidth = new GridLength(1, GridUnitType.Star);
			ColumnDefinition column1Def = new ColumnDefinition() { Width = cellWidth };
			grid.ColumnDefinitions.Add(column1Def);

			ColumnDefinition column2Def = new ColumnDefinition() { Width = cellWidth };
			grid.ColumnDefinitions.Add(column2Def);

			Style style = new Style(typeof(TextBlock));
			style.Setters.Add(new Setter(TextBlock.FontSizeProperty, 25));
			style.Setters.Add(new Setter(TextBlock.TextWrappingProperty, TextWrapping.Wrap));

			int recordIndex = 0;
			foreach (Record gridRecord in resultRecords)
			{
				List<Cell> cells = gridRecord.Cells;
				int cellCount = cells.Count;

				int firstCellIndex = recordIndex;
				foreach (Cell cell in cells)
				{
					
						TextBlock nameText = new TextBlock()
						{
							Text = cell.Title,
							Name = gridRecord.Code,
							Style = style,
							VerticalAlignment = VerticalAlignment.Center,
							HorizontalAlignment = HorizontalAlignment.Stretch,
							Margin = new Thickness(10, 2, 2, 2),
							TextAlignment = TextAlignment.Left
						};
						Grid.SetColumn(nameText, 0);
						Grid.SetRow(nameText, recordIndex);
						nameText.MouseLeftButtonDown += Record_Click;
						grid.Children.Add(nameText);

					TextBlock valueText = new TextBlock()
					{
						Text = cell.Value,
						Name = gridRecord.Code,
						Style = style,
						VerticalAlignment = VerticalAlignment.Center,
						HorizontalAlignment = HorizontalAlignment.Stretch
					};


					if (cell.HasTitle())
					{
						valueText.TextAlignment = TextAlignment.Right;
						Grid.SetColumn(valueText, 1);
						valueText.Margin = new Thickness(2, 2, 10, 2);
						if (!cell.HasValue())
						{
							Grid.SetColumnSpan(nameText, 2);
						}
					} else
					{
						valueText.TextAlignment = TextAlignment.Left;
						Grid.SetColumn(valueText, 0);
						Grid.SetColumnSpan(valueText, 2);
						valueText.Margin = new Thickness(10, 2, 2, 2);
					}
					Grid.SetRow(valueText, recordIndex);
					valueText.MouseLeftButtonDown += Record_Click;
					grid.Children.Add(valueText);

					recordIndex++;
				}

				if (resultRecords.Last() != gridRecord)
				{
					Border columnBottom = new Border()
					{
						BorderThickness = new Thickness(0, 0, 0, 1),
						BorderBrush = new SolidColorBrush(Colors.White),
						Margin =new Thickness(0, 0, 0, 1)
					};
					Grid.SetRow(columnBottom, recordIndex - 1);
					Grid.SetColumnSpan(columnBottom, 2);
					grid.Children.Add(columnBottom);
				}
			}

			Border resultListBorder = getBorder();
			resultListBorder.Child = grid;
			StackPanel resultListPane = new StackPanel() { Margin = new Thickness(5, 3, 0, 3) };
			TextBlock resultListTitle = new TextBlock() { Text = resultList.Title, TextWrapping = TextWrapping.Wrap };
			resultListPane.Children.Add(resultListTitle);
			resultListPane.Children.Add(resultListBorder);
			ContentPanel.Children.Add(resultListPane);
		}

		private Border getBorder()
		{
			Border border = new Border();
			border.BorderThickness = new Thickness(2);
			border.BorderBrush = new SolidColorBrush(Colors.Gray);
			border.CornerRadius = new CornerRadius(10);
			return border;
		}

		private void Record_Click(object sender, MouseButtonEventArgs e)
		{
			TextBlock textBlock = sender as TextBlock;
			_connection.SendMessage(textBlock.Name);
		}

		public void OnMessageReceived(Result message)
		{

			Deployment.Current.Dispatcher.BeginInvoke(() =>
{
	Progress.Visibility = Visibility.Collapsed;
	processMessageReceived(message);
});
		}

		private void processMessageReceived(Result message)
		{
			if (message.ShowBack)
			{
				BackBtn.Visibility = Visibility.Visible;
			} else
			{
				BackBtn.Visibility = Visibility.Collapsed;
			}

			if (message.HideCancel)
			{
				CancelBtn.Visibility = Visibility.Collapsed;
			} else
			{
				CancelBtn.Visibility = Visibility.Visible;
			}
			if (message.Title != null && !message.Title.Equals(""))
			{
				AppTitle.Text = message.Title;
			}
			ContentPanel.Children.Clear();
			recreateView(message);
		}

		public void OnMessageSent()
		{
			Deployment.Current.Dispatcher.BeginInvoke(() =>
			{
				Progress.Visibility = Visibility.Visible;
			});
		}

		public void OnConnect()
		{

		}

		public void OnDisconnect()
		{
			Deployment.Current.Dispatcher.BeginInvoke(() =>
{
	Progress.Visibility = Visibility.Collapsed;
	if (_connection == null || !_connection._isConnectedFirstTime)
	{
		ContentPanel.Children.Clear();
		TextBlock textBlock = new TextBlock()
		{
			Text = "Connection failed",
			Margin = new Thickness(10, 2, 10, 2),
			FontSize = 20
		};
		Button reconnect = new Button() { Content = "Reconnect" };
		reconnect.BorderThickness = new Thickness(0);
		reconnect.Foreground = new SolidColorBrush(Colors.Black);
		reconnect.Background = new SolidColorBrush(Colors.LightGray);
		reconnect.Click += new RoutedEventHandler(Reconnect_Click);
		ContentPanel.Children.Add(textBlock);
		ContentPanel.Children.Add(reconnect);

	}
});
		}

		void Reconnect_Click(object sender, RoutedEventArgs e)
		{
			if (_connection == null)
			{
				_connection = Connection.NewConnection();
			}
			Progress.Visibility = Visibility.Visible;
			_connection.Connect();
		}

		public void OnConnectionSuccess()
		{
			Deployment.Current.Dispatcher.BeginInvoke(() =>
			{
				Progress.Visibility = Visibility.Collapsed;
			});
		}

		private void CancelBtn_Click(object sender, RoutedEventArgs e)
		{
			_connection.SendMessage("Cancel");
		}

		private void BackBtn_Click(object sender, RoutedEventArgs e)
		{
			_connection.SendMessage("Back");
		}

		protected override void OnBackKeyPress(System.ComponentModel.CancelEventArgs e)
		{
			e.Cancel = true;
			_connection.SendMessage("Back");
		}


		private void Application_Launching(object sender, LaunchingEventArgs e)
		{
			//RootFrame.Navigated += RootFrame_Navigated;
			//this.OnNavigatedFrom += RootFrame_Navigated;
			this.NavigationService.Navigated += RootFrame_Navigated;
		}

		private void RootFrame_Navigated(object sender, NavigationEventArgs e)
		{
			// this is a dirty hack to circumvent the hard coded title of the datepicker

			try
			{
				if (e.Uri == null || e.Content == null || !(e.Content is DatePickerPage) || e.Uri.OriginalString != "/Microsoft.Phone.Controls.Toolkit;component/DateTimePickers/DatePickerPage.xaml")
					return;

				DatePickerPage objDatePickerPage = (DatePickerPage)e.Content;
				FrameworkElement objSystemTrayPlaceholder = (FrameworkElement)objDatePickerPage.FindName("SystemTrayPlaceholder");
				Grid objParentGrid = (Grid)objSystemTrayPlaceholder.Parent;
				TextBlock objTitleTextBox = (TextBlock)objParentGrid.Children.First(c => c.GetType() == typeof(TextBlock));
				//objTitleTextBox.Text = AppResources.ChooseDate; // put your resource access here
			}
			catch
			{

			}
		}

		private DateTime toDateTime(String value)
		{
			return toDateTime(long.Parse(value));
		}
		private DateTime toDateTime(long value)
		{
			String financeDate = value.ToString();
			int year = int.Parse(financeDate.Substring(0, 4));
			int month = int.Parse(financeDate.Substring(4, 2));
			int day = int.Parse(financeDate.Substring(6, 2));

			return new DateTime(year,month,day);
		}

		private long toFinanceDate(DateTime dateTime)
		{
			String financeDate = dateTime.Year.ToString();
			financeDate += dateTime.Month.ToString();
			financeDate += dateTime.Day.ToString();
			return long.Parse(financeDate);
		}

		private void Exit_Clicked(object sender, EventArgs e)
		{
			if (NavigationService.CanGoBack)
			{
				NavigationService.GoBack();
			} else
			{
				throw new Exception();
			}
		}

	}
}