/**
 * 
 */
package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.translate.ClientLanguage;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CoreUtils {
	// ISO 639-2 Code
	private static ClientLanguage[] languages = new ClientLanguage[] {
			new ClientLanguage("English", "English", "eng"),
			new ClientLanguage("Afrikaans", "Afrikaans", "afr"),
			new ClientLanguage("Albanian", "Shqip", "sqi"),
			new ClientLanguage("Arabic", "�?العربية�?", "ara"),
			new ClientLanguage("Armenian", "Հայերեն", "hye"),
			new ClientLanguage("Azeri", "Azərbaycan dili", "aze"),
			new ClientLanguage("Basque", "Euskara", "eus"),
			new ClientLanguage("Belarusian", "Белару�?ка�?", "bel"),
			new ClientLanguage("Bengali", "বাংলা", "ben"),
			new ClientLanguage("Bosnian", "Bosanski", "bos"),
			new ClientLanguage("Bulgarian", "Българ�?ки", "bul"),
			new ClientLanguage("Catalan", "Català", "cat"),
			new ClientLanguage("Croatian", "Hrvatski", "hrv"),
			new ClientLanguage("Czech", "Čeština", "ces"),
			new ClientLanguage("Danish", "Dansk", "dan"),
			new ClientLanguage("Dutch", "Nederlands", "dum"),
			new ClientLanguage("Spanish (Spain)", "Español (España)", "spa"),
			new ClientLanguage("Esperanto", "Espéranto", "epo"),
			new ClientLanguage("Estonian", "Eesti", "est"),
			new ClientLanguage("Faroese", "Føroyskt", "fao"),
			new ClientLanguage("Philippine", "Filipino", "phi"),
			new ClientLanguage("Finnish", "Suomi", "fin"),
			new ClientLanguage("French (Canada)", "Français (Canada)", "fra"),
			new ClientLanguage("French France", "Français France", "fra"),
			new ClientLanguage("Frisian", "Frisian", "fry"),
			new ClientLanguage("Galician", "Galego", "glg"),
			new ClientLanguage("Georgian", "ქ�?რთული", "kat"),
			new ClientLanguage("German", "Deutsch", "deu"),
			new ClientLanguage("Greek", "Ελληνικά", "ell"),
			new ClientLanguage("Haitian Creole", "Kreyòl", "hat"),
			new ClientLanguage("Hebrew", "�?עברית�?", "heb"),
			new ClientLanguage("Hindi", "हिन�?दी", "hin"),
			new ClientLanguage("Hungarian", "Magyar", "hun"),
			new ClientLanguage("Icelandic", "�?slenska", "isl"),
			new ClientLanguage("Indonesian", "Bahasa Indonesia", "ind"),
			new ClientLanguage("Irish", "Gaeilge", "mga"),
			new ClientLanguage("Italian", "Italiano", "ita"),
			new ClientLanguage("Japanese", "日本語", "jpn"),
			new ClientLanguage("Korean", "한국어", "kor"),
			new ClientLanguage("Kurdish", "Kurdî", "kur"),
			new ClientLanguage("Latin", "lingua latina", "lat"),
			new ClientLanguage("Latvian", "Latviešu", "lav"),
			new ClientLanguage("Leet", "Speak", "LEE"),
			new ClientLanguage("Lithuanian", "Lietuvių", "lit"),
			new ClientLanguage("Macedonian", "Македон�?ки", "mkd"),
			new ClientLanguage("Malay", "Bahasa Melayu", "msa"),
			new ClientLanguage("Malayalam", "മലയാളം", "mal"),
			new ClientLanguage("Maltese", "Maltese", "mlt"),
			new ClientLanguage("Nepali", "नेपाली", "nep"),
			new ClientLanguage("Norwegian (bokmal)", "Norsk (bokmål)", "nob"),
			new ClientLanguage("Norwegian (nynorsk)", "Norsk (nynorsk)", "nno"),
			new ClientLanguage("Pashto", "�?پښتو�?", "pus"),
			new ClientLanguage("Persian", "�?�?ارسی�?", "fas"),
			new ClientLanguage("Polish", "Polski", "pol"),
			new ClientLanguage("Português (Brasil)", "Português (Brasil)",
					"por"),
			new ClientLanguage("Português (Portugal)", "Português (Portugal)",
					"por"),
			new ClientLanguage("Punjabi", "ਪੰਜਾਬੀ", "pan"),
			new ClientLanguage("Romanian", "Română", "ron"),
			new ClientLanguage("Russian", "Ру�?�?кий", "rus"),
			new ClientLanguage("Serbian", "Срп�?ки", "srp"),
			new ClientLanguage("Simplified Chinese (China)", "中文(简体)", "zho"),
			new ClientLanguage("Slovak", "Sloven�?ina", "slk"),
			new ClientLanguage("Slovenian", "Slovenš�?ina", "slv"),
			new ClientLanguage("Swahili", "Kiswahili", "swa"),
			new ClientLanguage("Swedish", "Svenska", "swe"),
			new ClientLanguage("Tamil", "தமிழ�?", "tam"),
			new ClientLanguage("Telugu", "తెల�?గ�?", "tel"),
			new ClientLanguage("Thai", "ภาษาไทย", "tha"),
			new ClientLanguage("Traditional Chinese (Taiwan)", "中文(�?��?�)", "zho"),
			new ClientLanguage("Traditional Chinese(Hong Kong)", "中文(香港)",
					"zho"), new ClientLanguage("Turkish", "Türkçe", "tur"),
			new ClientLanguage("Ukrainian", "Україн�?ька", "ukr"),
			new ClientLanguage("Urdu", "اردو", "urd"),
			new ClientLanguage("Vietnamese", "Tiếng Việt", "vie"),
			new ClientLanguage("Welsh", "Cymraeg", "cym"),
			new ClientLanguage("Yiddish", "ייִדיש", "yid") };
	private static String[] countries = new String[] { "United Kingdom",
			"United States", "Afghanistan", "Albania", "Algeria",
			"American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica",
			"Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Austria",
			"Australia", "Azerbaijan", "Bahamas, The", "Bahrain", "Bangladesh",
			"Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda",
			"Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",
			"Bouvet Island", "Brazil", "British Indian Ocean Territory",
			"Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia",
			"Cameroon", "Canada", "Cape Verde", "Cayman Islands",
			"Central African Republic", "Chad", "Chile", "China",
			"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
			"Comoros", "Congo, Democratic Republic of the",
			"Congo, Republic of the", "Cook Islands", "Costa Rica",
			"Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic",
			"Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador",
			"Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
			"Ethiopia", "Falkland Islands (Islas Malvinas)", "Faroe Islands",
			"Fiji", "Finland", "France", "French Guiana", "French Polynesia",
			"French Southern and Antarctic Lands", "Gabon", "Gambia, The",
			"Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland",
			"Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea",
			"Guinea-Bissau", "Guyana", "Haiti",
			"Heard Island and McDonald Islands", "Holy See (Vatican City)",
			"Honduras", "Hong Kong", "Hungary", "Iceland", "India",
			"Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel",
			"Italy", "Jamaica", "Jan Mayen", "Japan", "Jordan", "Kazakhstan",
			"Kenya", "Kiribati", "Korea, North", "Korea, South", "Kuwait",
			"Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia",
			"Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia",
			"Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
			"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
			"Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova",
			"Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique",
			"Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
			"New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
			"Niue", "Norfolk Island", "Northern Mariana Islands", "Norway",
			"Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea",
			"Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",
			"Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russia",
			"Rwanda", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
			"Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
			"Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia",
			"Senegal", "Serbia and Montenegro", "Seychelles", "Sierra Leone",
			"Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia",
			"South Africa", "South Georgia and the South Sandwich Islands",
			"Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard", "Swaziland",
			"Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan",
			"Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga",
			"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
			"Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine",
			"United Arab Emirates", "Uruguay", "Uzbekistan", "Vanuatu",
			"Venezuela", "Vietnam", "Virgin Islands", "Wallis and Futuna",
			"Western Sahara", "Yemen", "Zambia", "Zimbabwe" };

	private static String[] timezones = new String[] { "UTC-12:00 Etc/GMT+12",
			"UTC-11:00 Etc/GMT+11", "UTC-11:00 MIT", "UTC-11:00 Pacific/Apia",
			"UTC-11:00 Pacific/Midway", "UTC-11:00 Pacific/Niue",
			"UTC-11:00 Pacific/Pago_Pago", "UTC-11:00 Pacific/Samoa",
			"UTC-11:00 US/Samoa", "UTC-10:00 America/Adak",
			"UTC-10:00 America/Atka", "UTC-10:00 Etc/GMT+10", "UTC-10:00 HST",
			"UTC-10:00 Pacific/Fakaofo", "UTC-10:00 Pacific/Honolulu",
			"UTC-10:00 Pacific/Johnston", "UTC-10:00 Pacific/Rarotonga",
			"UTC-10:00 Pacific/Tahiti", "UTC-10:00 SystemV/HST10",
			"UTC-10:00 US/Aleutian", "UTC-10:00 US/Hawaii",
			"UTC-9:30 Pacific/Marquesas", "UTC-9:00 AST",
			"UTC-9:00 America/Anchorage", "UTC-9:00 America/Juneau",
			"UTC-9:00 America/Nome", "UTC-9:00 America/Sitka",
			"UTC-9:00 America/Yakutat", "UTC-9:00 Etc/GMT+9",
			"UTC-9:00 Pacific/Gambier", "UTC-9:00 SystemV/YST9",
			"UTC-9:00 SystemV/YST9YDT", "UTC-9:00 US/Alaska",
			"UTC-8:00 America/Dawson", "UTC-8:00 America/Ensenada",
			"UTC-8:00 America/Los_Angeles", "UTC-8:00 America/Metlakatla",
			"UTC-8:00 America/Santa_Isabel", "UTC-8:00 America/Tijuana",
			"UTC-8:00 America/Vancouver", "UTC-8:00 America/Whitehorse",
			"UTC-8:00 Canada/Pacific", "UTC-8:00 Canada/Yukon",
			"UTC-8:00 Etc/GMT+8", "UTC-8:00 Mexico/BajaNorte", "UTC-8:00 PST",
			"UTC-8:00 PST8PDT", "UTC-8:00 Pacific/Pitcairn",
			"UTC-8:00 SystemV/PST8", "UTC-8:00 SystemV/PST8PDT",
			"UTC-8:00 US/Pacific", "UTC-8:00 US/Pacific-New",
			"UTC-7:00 America/Boise", "UTC-7:00 America/Cambridge_Bay",
			"UTC-7:00 America/Chihuahua", "UTC-7:00 America/Dawson_Creek",
			"UTC-7:00 America/Denver", "UTC-7:00 America/Edmonton",
			"UTC-7:00 America/Hermosillo", "UTC-7:00 America/Inuvik",
			"UTC-7:00 America/Mazatlan", "UTC-7:00 America/Ojinaga",
			"UTC-7:00 America/Phoenix", "UTC-7:00 America/Shiprock",
			"UTC-7:00 America/Yellowknife", "UTC-7:00 Canada/Mountain",
			"UTC-7:00 Etc/GMT+7", "UTC-7:00 MST", "UTC-7:00 MST7MDT",
			"UTC-7:00 Mexico/BajaSur", "UTC-7:00 Navajo", "UTC-7:00 PNT",
			"UTC-7:00 SystemV/MST7", "UTC-7:00 SystemV/MST7MDT",
			"UTC-7:00 US/Arizona", "UTC-7:00 US/Mountain",
			"UTC-6:00 America/Bahia_Banderas", "UTC-6:00 America/Belize",
			"UTC-6:00 America/Cancun", "UTC-6:00 America/Chicago",
			"UTC-6:00 America/Costa_Rica", "UTC-6:00 America/El_Salvador",
			"UTC-6:00 America/Guatemala", "UTC-6:00 America/Indiana/Knox",
			"UTC-6:00 America/Indiana/Tell_City", "UTC-6:00 America/Knox_IN",
			"UTC-6:00 America/Managua", "UTC-6:00 America/Matamoros",
			"UTC-6:00 America/Menominee", "UTC-6:00 America/Merida",
			"UTC-6:00 America/Mexico_City", "UTC-6:00 America/Monterrey",
			"UTC-6:00 America/North_Dakota/Beulah",
			"UTC-6:00 America/North_Dakota/Center",
			"UTC-6:00 America/North_Dakota/New_Salem",
			"UTC-6:00 America/Rainy_River", "UTC-6:00 America/Rankin_Inlet",
			"UTC-6:00 America/Regina", "UTC-6:00 America/Swift_Current",
			"UTC-6:00 America/Tegucigalpa", "UTC-6:00 America/Winnipeg",
			"UTC-6:00 CST", "UTC-6:00 CST6CDT", "UTC-6:00 Canada/Central",
			"UTC-6:00 Canada/East-Saskatchewan",
			"UTC-6:00 Canada/Saskatchewan", "UTC-6:00 Chile/EasterIsland",
			"UTC-6:00 Etc/GMT+6", "UTC-6:00 Mexico/General",
			"UTC-6:00 Pacific/Easter", "UTC-6:00 Pacific/Galapagos",
			"UTC-6:00 SystemV/CST6", "UTC-6:00 SystemV/CST6CDT",
			"UTC-6:00 US/Central", "UTC-6:00 US/Indiana-Starke",
			"UTC-5:00 America/Atikokan", "UTC-5:00 America/Bogota",
			"UTC-5:00 America/Cayman", "UTC-5:00 America/Coral_Harbour",
			"UTC-5:00 America/Detroit", "UTC-5:00 America/Fort_Wayne",
			"UTC-5:00 America/Grand_Turk", "UTC-5:00 America/Guayaquil",
			"UTC-5:00 America/Havana", "UTC-5:00 America/Indiana/Indianapolis",
			"UTC-5:00 America/Indiana/Marengo",
			"UTC-5:00 America/Indiana/Petersburg",
			"UTC-5:00 America/Indiana/Vevay",
			"UTC-5:00 America/Indiana/Vincennes",
			"UTC-5:00 America/Indiana/Winamac",
			"UTC-5:00 America/Indianapolis", "UTC-5:00 America/Iqaluit",
			"UTC-5:00 America/Jamaica", "UTC-5:00 America/Kentucky/Louisville",
			"UTC-5:00 America/Kentucky/Monticello", "UTC-5:00 America/Lima",
			"UTC-5:00 America/Louisville", "UTC-5:00 America/Montreal",
			"UTC-5:00 America/Nassau", "UTC-5:00 America/New_York",
			"UTC-5:00 America/Nipigon", "UTC-5:00 America/Panama",
			"UTC-5:00 America/Pangnirtung", "UTC-5:00 America/Port-au-Prince",
			"UTC-5:00 America/Resolute", "UTC-5:00 America/Thunder_Bay",
			"UTC-5:00 America/Toronto", "UTC-5:00 Canada/Eastern",
			"UTC-5:00 Cuba", "UTC-5:00 EST", "UTC-5:00 EST5EDT",
			"UTC-5:00 Etc/GMT+5", "UTC-5:00 IET", "UTC-5:00 Jamaica",
			"UTC-5:00 SystemV/EST5", "UTC-5:00 SystemV/EST5EDT",
			"UTC-5:00 US/East-Indiana", "UTC-5:00 US/Eastern",
			"UTC-5:00 US/Michigan", "UTC-4:30 America/Caracas",
			"UTC-4:00 America/Anguilla", "UTC-4:00 America/Antigua",
			"UTC-4:00 America/Argentina/San_Luis", "UTC-4:00 America/Aruba",
			"UTC-4:00 America/Asuncion", "UTC-4:00 America/Barbados",
			"UTC-4:00 America/Blanc-Sablon", "UTC-4:00 America/Boa_Vista",
			"UTC-4:00 America/Campo_Grande", "UTC-4:00 America/Cuiaba",
			"UTC-4:00 America/Curacao", "UTC-4:00 America/Dominica",
			"UTC-4:00 America/Eirunepe", "UTC-4:00 America/Glace_Bay",
			"UTC-4:00 America/Goose_Bay", "UTC-4:00 America/Grenada",
			"UTC-4:00 America/Guadeloupe", "UTC-4:00 America/Guyana",
			"UTC-4:00 America/Halifax", "UTC-4:00 America/La_Paz",
			"UTC-4:00 America/Manaus", "UTC-4:00 America/Marigot",
			"UTC-4:00 America/Martinique", "UTC-4:00 America/Moncton",
			"UTC-4:00 America/Montserrat", "UTC-4:00 America/Port_of_Spain",
			"UTC-4:00 America/Porto_Acre", "UTC-4:00 America/Porto_Velho",
			"UTC-4:00 America/Puerto_Rico", "UTC-4:00 America/Rio_Branco",
			"UTC-4:00 America/Santiago", "UTC-4:00 America/Santo_Domingo",
			"UTC-4:00 America/St_Barthelemy", "UTC-4:00 America/St_Kitts",
			"UTC-4:00 America/St_Lucia", "UTC-4:00 America/St_Thomas",
			"UTC-4:00 America/St_Vincent", "UTC-4:00 America/Thule",
			"UTC-4:00 America/Tortola", "UTC-4:00 America/Virgin",
			"UTC-4:00 Antarctica/Palmer", "UTC-4:00 Atlantic/Bermuda",
			"UTC-4:00 Atlantic/Stanley", "UTC-4:00 Brazil/Acre",
			"UTC-4:00 Brazil/West", "UTC-4:00 Canada/Atlantic",
			"UTC-4:00 Chile/Continental", "UTC-4:00 Etc/GMT+4", "UTC-4:00 PRT",
			"UTC-4:00 SystemV/AST4", "UTC-4:00 SystemV/AST4ADT",
			"UTC-3:30 America/St_Johns", "UTC-3:30 CNT",
			"UTC-3:30 Canada/Newfoundland", "UTC-3:00 AGT",
			"UTC-3:00 America/Araguaina",
			"UTC-3:00 America/Argentina/Buenos_Aires",
			"UTC-3:00 America/Argentina/Catamarca",
			"UTC-3:00 America/Argentina/ComodRivadavia",
			"UTC-3:00 America/Argentina/Cordoba",
			"UTC-3:00 America/Argentina/Jujuy",
			"UTC-3:00 America/Argentina/La_Rioja",
			"UTC-3:00 America/Argentina/Mendoza",
			"UTC-3:00 America/Argentina/Rio_Gallegos",
			"UTC-3:00 America/Argentina/Salta",
			"UTC-3:00 America/Argentina/San_Juan",
			"UTC-3:00 America/Argentina/Tucuman",
			"UTC-3:00 America/Argentina/Ushuaia", "UTC-3:00 America/Bahia",
			"UTC-3:00 America/Belem", "UTC-3:00 America/Buenos_Aires",
			"UTC-3:00 America/Catamarca", "UTC-3:00 America/Cayenne",
			"UTC-3:00 America/Cordoba", "UTC-3:00 America/Fortaleza",
			"UTC-3:00 America/Godthab", "UTC-3:00 America/Jujuy",
			"UTC-3:00 America/Maceio", "UTC-3:00 America/Mendoza",
			"UTC-3:00 America/Miquelon", "UTC-3:00 America/Montevideo",
			"UTC-3:00 America/Paramaribo", "UTC-3:00 America/Recife",
			"UTC-3:00 America/Rosario", "UTC-3:00 America/Santarem",
			"UTC-3:00 America/Sao_Paulo", "UTC-3:00 Antarctica/Rothera",
			"UTC-3:00 BET", "UTC-3:00 Brazil/East", "UTC-3:00 Etc/GMT+3",
			"UTC-2:00 America/Noronha", "UTC-2:00 Atlantic/South_Georgia",
			"UTC-2:00 Brazil/DeNoronha", "UTC-2:00 Etc/GMT+2",
			"UTC-1:00 America/Scoresbysund", "UTC-1:00 Atlantic/Azores",
			"UTC-1:00 Atlantic/Cape_Verde", "UTC-1:00 Etc/GMT+1",
			"UTC+0:00 Africa/Abidjan", "UTC+0:00 Africa/Accra",
			"UTC+0:00 Africa/Bamako", "UTC+0:00 Africa/Banjul",
			"UTC+0:00 Africa/Bissau", "UTC+0:00 Africa/Casablanca",
			"UTC+0:00 Africa/Conakry", "UTC+0:00 Africa/Dakar",
			"UTC+0:00 Africa/El_Aaiun", "UTC+0:00 Africa/Freetown",
			"UTC+0:00 Africa/Lome", "UTC+0:00 Africa/Monrovia",
			"UTC+0:00 Africa/Nouakchott", "UTC+0:00 Africa/Ouagadougou",
			"UTC+0:00 Africa/Sao_Tome", "UTC+0:00 Africa/Timbuktu",
			"UTC+0:00 America/Danmarkshavn", "UTC+0:00 Atlantic/Canary",
			"UTC+0:00 Atlantic/Faeroe", "UTC+0:00 Atlantic/Faroe",
			"UTC+0:00 Atlantic/Madeira", "UTC+0:00 Atlantic/Reykjavik",
			"UTC+0:00 Atlantic/St_Helena", "UTC+0:00 Eire", "UTC+0:00 Etc/GMT",
			"UTC+0:00 Etc/GMT+0", "UTC+0:00 Etc/GMT-0", "UTC+0:00 Etc/GMT0",
			"UTC+0:00 Etc/Greenwich", "UTC+0:00 Etc/UCT", "UTC+0:00 Etc/UTC",
			"UTC+0:00 Etc/Universal", "UTC+0:00 Etc/Zulu",
			"UTC+0:00 Europe/Belfast", "UTC+0:00 Europe/Dublin",
			"UTC+0:00 Europe/Guernsey", "UTC+0:00 Europe/Isle_of_Man",
			"UTC+0:00 Europe/Jersey", "UTC+0:00 Europe/Lisbon",
			"UTC+0:00 Europe/London", "UTC+0:00 GB", "UTC+0:00 GB-Eire",
			"UTC+0:00 GMT", "UTC+0:00 GMT0", "UTC+0:00 Greenwich",
			"UTC+0:00 Iceland", "UTC+0:00 Portugal", "UTC+0:00 UCT",
			"UTC+0:00 UTC", "UTC+0:00 Universal", "UTC+0:00 WET",
			"UTC+0:00 Zulu", "UTC+1:00 Africa/Algiers",
			"UTC+1:00 Africa/Bangui", "UTC+1:00 Africa/Brazzaville",
			"UTC+1:00 Africa/Ceuta", "UTC+1:00 Africa/Douala",
			"UTC+1:00 Africa/Kinshasa", "UTC+1:00 Africa/Lagos",
			"UTC+1:00 Africa/Libreville", "UTC+1:00 Africa/Luanda",
			"UTC+1:00 Africa/Malabo", "UTC+1:00 Africa/Ndjamena",
			"UTC+1:00 Africa/Niamey", "UTC+1:00 Africa/Porto-Novo",
			"UTC+1:00 Africa/Tunis", "UTC+1:00 Africa/Windhoek",
			"UTC+1:00 Arctic/Longyearbyen", "UTC+1:00 Atlantic/Jan_Mayen",
			"UTC+1:00 CET", "UTC+1:00 ECT", "UTC+1:00 Etc/GMT-1",
			"UTC+1:00 Europe/Amsterdam", "UTC+1:00 Europe/Andorra",
			"UTC+1:00 Europe/Belgrade", "UTC+1:00 Europe/Berlin",
			"UTC+1:00 Europe/Bratislava", "UTC+1:00 Europe/Brussels",
			"UTC+1:00 Europe/Budapest", "UTC+1:00 Europe/Copenhagen",
			"UTC+1:00 Europe/Gibraltar", "UTC+1:00 Europe/Ljubljana",
			"UTC+1:00 Europe/Luxembourg", "UTC+1:00 Europe/Madrid",
			"UTC+1:00 Europe/Malta", "UTC+1:00 Europe/Monaco",
			"UTC+1:00 Europe/Oslo", "UTC+1:00 Europe/Paris",
			"UTC+1:00 Europe/Podgorica", "UTC+1:00 Europe/Prague",
			"UTC+1:00 Europe/Rome", "UTC+1:00 Europe/San_Marino",
			"UTC+1:00 Europe/Sarajevo", "UTC+1:00 Europe/Skopje",
			"UTC+1:00 Europe/Stockholm", "UTC+1:00 Europe/Tirane",
			"UTC+1:00 Europe/Vaduz", "UTC+1:00 Europe/Vatican",
			"UTC+1:00 Europe/Vienna", "UTC+1:00 Europe/Warsaw",
			"UTC+1:00 Europe/Zagreb", "UTC+1:00 Europe/Zurich", "UTC+1:00 MET",
			"UTC+1:00 Poland", "UTC+2:00 ART", "UTC+2:00 Africa/Blantyre",
			"UTC+2:00 Africa/Bujumbura", "UTC+2:00 Africa/Cairo",
			"UTC+2:00 Africa/Gaborone", "UTC+2:00 Africa/Harare",
			"UTC+2:00 Africa/Johannesburg", "UTC+2:00 Africa/Kigali",
			"UTC+2:00 Africa/Lubumbashi", "UTC+2:00 Africa/Lusaka",
			"UTC+2:00 Africa/Maputo", "UTC+2:00 Africa/Maseru",
			"UTC+2:00 Africa/Mbabane", "UTC+2:00 Africa/Tripoli",
			"UTC+2:00 Asia/Amman", "UTC+2:00 Asia/Beirut",
			"UTC+2:00 Asia/Damascus", "UTC+2:00 Asia/Gaza",
			"UTC+2:00 Asia/Istanbul", "UTC+2:00 Asia/Jerusalem",
			"UTC+2:00 Asia/Nicosia", "UTC+2:00 Asia/Tel_Aviv", "UTC+2:00 CAT",
			"UTC+2:00 EET", "UTC+2:00 Egypt", "UTC+2:00 Etc/GMT-2",
			"UTC+2:00 Europe/Athens", "UTC+2:00 Europe/Bucharest",
			"UTC+2:00 Europe/Chisinau", "UTC+2:00 Europe/Helsinki",
			"UTC+2:00 Europe/Istanbul", "UTC+2:00 Europe/Kaliningrad",
			"UTC+2:00 Europe/Kiev", "UTC+2:00 Europe/Mariehamn",
			"UTC+2:00 Europe/Minsk", "UTC+2:00 Europe/Nicosia",
			"UTC+2:00 Europe/Riga", "UTC+2:00 Europe/Simferopol",
			"UTC+2:00 Europe/Sofia", "UTC+2:00 Europe/Tallinn",
			"UTC+2:00 Europe/Tiraspol", "UTC+2:00 Europe/Uzhgorod",
			"UTC+2:00 Europe/Vilnius", "UTC+2:00 Europe/Zaporozhye",
			"UTC+2:00 Israel", "UTC+2:00 Libya", "UTC+2:00 Turkey",
			"UTC+3:00 Africa/Addis_Ababa", "UTC+3:00 Africa/Asmara",
			"UTC+3:00 Africa/Asmera", "UTC+3:00 Africa/Dar_es_Salaam",
			"UTC+3:00 Africa/Djibouti", "UTC+3:00 Africa/Kampala",
			"UTC+3:00 Africa/Khartoum", "UTC+3:00 Africa/Mogadishu",
			"UTC+3:00 Africa/Nairobi", "UTC+3:00 Antarctica/Syowa",
			"UTC+3:00 Asia/Aden", "UTC+3:00 Asia/Baghdad",
			"UTC+3:00 Asia/Bahrain", "UTC+3:00 Asia/Kuwait",
			"UTC+3:00 Asia/Qatar", "UTC+3:00 Asia/Riyadh", "UTC+3:00 EAT",
			"UTC+3:00 Etc/GMT-3", "UTC+3:00 Europe/Moscow",
			"UTC+3:00 Europe/Samara", "UTC+3:00 Europe/Volgograd",
			"UTC+3:00 Indian/Antananarivo", "UTC+3:00 Indian/Comoro",
			"UTC+3:00 Indian/Mayotte", "UTC+3:00 W-SU",
			"UTC+3:07 Asia/Riyadh87", "UTC+3:07 Asia/Riyadh88",
			"UTC+3:07 Asia/Riyadh89", "UTC+3:07 Mideast/Riyadh87",
			"UTC+3:07 Mideast/Riyadh88", "UTC+3:07 Mideast/Riyadh89",
			"UTC+3:30 Asia/Tehran", "UTC+3:30 Iran", "UTC+4:00 Asia/Baku",
			"UTC+4:00 Asia/Dubai", "UTC+4:00 Asia/Muscat",
			"UTC+4:00 Asia/Tbilisi", "UTC+4:00 Asia/Yerevan",
			"UTC+4:00 Etc/GMT-4", "UTC+4:00 Indian/Mahe",
			"UTC+4:00 Indian/Mauritius", "UTC+4:00 Indian/Reunion",
			"UTC+4:00 NET", "UTC+4:30 Asia/Kabul",
			"UTC+5:00 Antarctica/Mawson", "UTC+5:00 Asia/Aqtau",
			"UTC+5:00 Asia/Aqtobe", "UTC+5:00 Asia/Ashgabat",
			"UTC+5:00 Asia/Ashkhabad", "UTC+5:00 Asia/Dushanbe",
			"UTC+5:00 Asia/Karachi", "UTC+5:00 Asia/Oral",
			"UTC+5:00 Asia/Samarkand", "UTC+5:00 Asia/Tashkent",
			"UTC+5:00 Asia/Yekaterinburg", "UTC+5:00 Etc/GMT-5",
			"UTC+5:00 Indian/Kerguelen", "UTC+5:00 Indian/Maldives",
			"UTC+5:00 PLT", "UTC+5:30 Asia/Colombo", "UTC+5:30 Asia/Kolkata",
			"UTC+5:30 IST", "UTC+5:45 Asia/Katmandu",
			"UTC+6:00 Antarctica/Vostok", "UTC+6:00 Asia/Almaty",
			"UTC+6:00 Asia/Bishkek", "UTC+6:00 Asia/Dacca",
			"UTC+6:00 Asia/Dhaka", "UTC+6:00 Asia/Novokuznetsk",
			"UTC+6:00 Asia/Novosibirsk", "UTC+6:00 Asia/Omsk",
			"UTC+6:00 Asia/Qyzylorda", "UTC+6:00 Asia/Thimbu",
			"UTC+6:00 Asia/Thimphu", "UTC+6:00 BST", "UTC+6:00 Etc/GMT-6",
			"UTC+6:00 Indian/Chagos", "UTC+6:30 Asia/Rangoon",
			"UTC+6:30 Indian/Cocos", "UTC+7:00 Antarctica/Davis",
			"UTC+7:00 Asia/Bangkok", "UTC+7:00 Asia/Ho_Chi_Minh",
			"UTC+7:00 Asia/Hovd", "UTC+7:00 Asia/Jakarta",
			"UTC+7:00 Asia/Krasnoyarsk", "UTC+7:00 Asia/Phnom_Penh",
			"UTC+7:00 Asia/Pontianak", "UTC+7:00 Asia/Saigon",
			"UTC+7:00 Asia/Vientiane", "UTC+7:00 Etc/GMT-7",
			"UTC+7:00 Indian/Christmas", "UTC+7:00 VST",
			"UTC+8:00 Antarctica/Casey", "UTC+8:00 Asia/Brunei",
			"UTC+8:00 Asia/Choibalsan", "UTC+8:00 Asia/Chongqing",
			"UTC+8:00 Asia/Chungking", "UTC+8:00 Asia/Harbin",
			"UTC+8:00 Asia/Hong_Kong", "UTC+8:00 Asia/Irkutsk",
			"UTC+8:00 Asia/Kashgar", "UTC+8:00 Asia/Kuala_Lumpur",
			"UTC+8:00 Asia/Kuching", "UTC+8:00 Asia/Macao",
			"UTC+8:00 Asia/Macau", "UTC+8:00 Asia/Makassar",
			"UTC+8:00 Asia/Manila", "UTC+8:00 Asia/Shanghai",
			"UTC+8:00 Asia/Singapore", "UTC+8:00 Asia/Taipei",
			"UTC+8:00 Asia/Ujung_Pandang", "UTC+8:00 Asia/Ulaanbaatar",
			"UTC+8:00 Asia/Ulan_Bator", "UTC+8:00 Asia/Urumqi",
			"UTC+8:00 Australia/Perth", "UTC+8:00 Australia/West",
			"UTC+8:00 CTT", "UTC+8:00 Etc/GMT-8", "UTC+8:00 Hongkong",
			"UTC+8:00 PRC", "UTC+8:00 Singapore", "UTC+8:45 Australia/Eucla",
			"UTC+9:00 Asia/Dili", "UTC+9:00 Asia/Jayapura",
			"UTC+9:00 Asia/Pyongyang", "UTC+9:00 Asia/Seoul",
			"UTC+9:00 Asia/Tokyo", "UTC+9:00 Asia/Yakutsk",
			"UTC+9:00 Etc/GMT-9", "UTC+9:00 JST", "UTC+9:00 Japan",
			"UTC+9:00 Pacific/Palau", "UTC+9:00 ROK", "UTC+9:30 ACT",
			"UTC+9:30 Australia/Adelaide", "UTC+9:30 Australia/Broken_Hill",
			"UTC+9:30 Australia/Darwin", "UTC+9:30 Australia/North",
			"UTC+9:30 Australia/South", "UTC+9:30 Australia/Yancowinna",
			"UTC+10:00 AET", "UTC+10:00 Antarctica/DumontDUrville",
			"UTC+10:00 Asia/Sakhalin", "UTC+10:00 Asia/Vladivostok",
			"UTC+10:00 Australia/ACT", "UTC+10:00 Australia/Brisbane",
			"UTC+10:00 Australia/Canberra", "UTC+10:00 Australia/Currie",
			"UTC+10:00 Australia/Hobart", "UTC+10:00 Australia/Lindeman",
			"UTC+10:00 Australia/Melbourne", "UTC+10:00 Australia/NSW",
			"UTC+10:00 Australia/Queensland", "UTC+10:00 Australia/Sydney",
			"UTC+10:00 Australia/Tasmania", "UTC+10:00 Australia/Victoria",
			"UTC+10:00 Etc/GMT-10", "UTC+10:00 Pacific/Chuuk",
			"UTC+10:00 Pacific/Guam", "UTC+10:00 Pacific/Port_Moresby",
			"UTC+10:00 Pacific/Saipan", "UTC+10:00 Pacific/Truk",
			"UTC+10:00 Pacific/Yap", "UTC+10:30 Australia/LHI",
			"UTC+10:30 Australia/Lord_Howe", "UTC+11:00 Antarctica/Macquarie",
			"UTC+11:00 Asia/Anadyr", "UTC+11:00 Asia/Kamchatka",
			"UTC+11:00 Asia/Magadan", "UTC+11:00 Etc/GMT-11",
			"UTC+11:00 Pacific/Efate", "UTC+11:00 Pacific/Guadalcanal",
			"UTC+11:00 Pacific/Kosrae", "UTC+11:00 Pacific/Noumea",
			"UTC+11:00 Pacific/Pohnpei", "UTC+11:00 Pacific/Ponape",
			"UTC+11:00 SST", "UTC+11:30 Pacific/Norfolk",
			"UTC+12:00 Antarctica/McMurdo", "UTC+12:00 Antarctica/South_Pole",
			"UTC+12:00 Etc/GMT-12", "UTC+12:00 Kwajalein", "UTC+12:00 NST",
			"UTC+12:00 NZ", "UTC+12:00 Pacific/Auckland",
			"UTC+12:00 Pacific/Fiji", "UTC+12:00 Pacific/Funafuti",
			"UTC+12:00 Pacific/Kwajalein", "UTC+12:00 Pacific/Majuro",
			"UTC+12:00 Pacific/Nauru", "UTC+12:00 Pacific/Tarawa",
			"UTC+12:00 Pacific/Wake", "UTC+12:00 Pacific/Wallis",
			"UTC+12:45 NZ-CHAT", "UTC+12:45 Pacific/Chatham",
			"UTC+13:00 Etc/GMT-13", "UTC+13:00 Pacific/Enderbury",
			"UTC+13:00 Pacific/Tongatapu", "UTC+14:00 Etc/GMT-14",
			"UTC+14:00 Pacific/Kiritimati" };

	private static String[][] states = new String[][] {
			// UNITED KINGDOM
			{ "Buckinghamshire", "Hampshire", "Cambridgeshire", "Hackney",
					"East Riding of Yorkshire", "Worcestershire",
					"Barking and Dagenham", "Moray", "Staffordshire", "Devon",
					"Harrow", "Falkirk", "Rhondda Cynon Taff", "East Ayrshire",
					"Sutton", "Larne", "Bedfordshire", "Tameside", "Cleveland",
					"Lothian Region", "Coventry", "Nottinghamshire",
					"Leicestershire", "Knowsley", "Torbay", "Wrexham",
					"Derbyshire", "Darlington", "Renfrewshire",
					"Hammersmith and Fulham", "Highland", "Orkney Islands",
					"West Yorkshire", "Merton", "Hertfordshire", "Merseyside",
					"Bristol", "Bexley", "East Renfrewshire", "Edinburgh",
					"Thurrock", "North Lanarkshire", "Barnet", "Conwy",
					"Bracknell Forest", "North Yorkshire", "Milton Keynes",
					"Rotherham", "London", "Angus", "Scottish Borders",
					"Dumfries and Galloway", "Greater London", "Tyne and Wear",
					"Surrey", "Suffolk", "Leeds", "Fermanagh", "Oxfordshire",
					"Moyle", "West Midlands", "East Lothian", "Aberdeenshire",
					"Calderdale", "Newport", "Ceredigion", "Limavady", "Clwyd",
					"Dungannon", "Rutland", "Sheffield", "Wokingham", "Slough",
					"Dyfed", "Cookstown", "Richmond upon Thames", "Blackpool",
					"Doncaster", "North Lincolnshire", "Denbighshire",
					"Bolton", "Caerphilly", "Lewisham", "Sunderland", "Armagh",
					"Stockton-on-Tees", "North Tyneside", "Greenwich",
					"Lincolnshire", "Solihull", "Vale of Glamorgan",
					"Grampian Region", "Islington", "Oldham", "Magherafelt",
					"Waltham Forest", "Bury", "South Yorkshire",
					"Northumberland", "Middlesbrough", "Southampton", "Durham",
					"Enfield", "Greater Manchester", "Redcar and Cleveland",
					"Sefton", "StHelens", "West Sussex", "Trafford", "Newham",
					"West Glamorgan", "Kensington and Chelsea", "Dorset",
					"Argyll and Bute", "Hillingdon", "East Dunbartonshire",
					"South Gloucestershire", "Neath Port Talbot", "Fife",
					"Down", "Merthyr Tydfil", "Castlereagh", "Wolverhampton",
					"Liverpool", "Plymouth", "Croydon", "Ealing", "Brent",
					"Nottingham", "Norfolk", "Lancashire", "Strabane",
					"Halton", "Camden", "Ballymoney", "Windsor and maidenhead",
					"Isles of Scilly", "Coleraine", "Reading", "Wirral",
					"Derry", "Cumbria", "Carrickfergus", "Southwark",
					"Gateshead", "Manchester", "Wiltshire", "South Glamorgan",
					"Gloucestershire", "South Lanarkshire", "Essex",
					"North Down", "Warrington", "East Sussex", "Belfast",
					"Herefordshire", "North Somerset", "Antrim", "Banbridge",
					"Carmarthenshire", "Northamptonshire", "Walsall",
					"Clackmannanshire", "South Ayrshire", "North Ayrshire",
					"Bradford", "Ballymena", "Dundee City", "Tayside Region",
					"Glasgow City", "Omagh", "Medway", "Warwickshire",
					"Haringey", "South Tyneside", "Kirklees", "Mid Glamorgan",
					"Flintshire", "Ards", "Lambeth", "Shropshire",
					"Telford and Wrekin", "Hounslow", "Craigavon",
					"Birmingham", "Monmouthshire", "Isle of Wight",
					"Portsmouth", "Somerset", "Shetland Islands", "Poole",
					"Sandwell", "West Dunbartonshire", "Borders Region",
					"Aberdeen City", "Berkshire", "Stirling",
					"Southend-on-Sea", "Bath and North East Somerset",
					"Western Isles", "Inverclyde", "Midlothian",
					"Perth and Kinross", "Leicester", "Swansea",
					"Kingston upon Hull", "Wandsworth", "West Berkshire",
					"Tower Hamlets", "Lisburn", "Bournemouth", "Cardiff",
					"Eilean Siar", "Dudley", "North East Lincolnshire",
					"Wigan", "Gwent", "Strathclyde Region", "West Lothian",
					"Redbridge", "Cheshire", "Newtownabbey",
					"Brighton and Hove", "Wakefield", "Newcastle upon Tyne",
					"Bromley", "Stockport", "Torfaen", "Newry and Mourne",
					"Hartlepool", "Pembrokeshire", "Hereford and Worcester",
					"Peterborough", "Stoke-on-Trent", "Powys", "Barnsley",
					"Kent", "Westminster", "Bridgend", "Derby", "Cornwall",
					"Gwynedd", "Blackburn with Darwen", "Humberside", "York",
					"Swindon", "Isle of Anglesey" },
			// US
			{ "Alabama", "Alaska", "Arizona", "Arkansas", "California",
					"Colorado", "Connecticut", "Delaware",
					"District of Columbia", "Florida", "Georgia", "Hawaii",
					"Idaho", "Illinois", "Indiana", "Iowa", "Kansas",
					"Kentucky", "Louisiana", "Maine", "Maryland",
					"Massachusetts", "Michigan", "Minnesota", "Mississippi",
					"Missouri", "Montana", "Nebraska", "Nevada",
					"New Hampshire", "New Jersey", "New Mexico", "New York",
					"North Carolina", "North Dakota", "Ohio", "Oklahoma",
					"Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
					"South Dakota", "Tennessee", "Texas", "Utah", "Vermont",
					"Virginia", "Washington", "West Virginia", "Wisconsin",
					"Wyoming"

			},
			// AFGHANISTAN
			{ "Badah_šan", "Badgis", "Baglan", "Balh_", "Bamiyan", "Farah",
					"Faryab", "Gawr", "Gazni", "H_awst", "Herat", "Hilmand",
					"Jawzjan", "Kabul", "Kandahar", "Kapisa", "Kunarha",
					"Kunduz", "Lagman", "Lawgar", "Maydan-Wardak", "Nangarhar",
					"Nimruz", "Nuristan", "Paktika", "Paktiya", "Parwan",
					"Samangan", "Sar-e Pul", "Tah_ar", "Uruzgan", "Zabul" },// ALBANIA
			{ "Berat", "Bulqizë", "Delvinë", "Devoll", "Dibrë", "Durrës",
					"Elbasan", "Fier", "Gjirokastër", "Gramsh", "Has",
					"Kavajë", "Kolonjë", "Korçë", "Krujë", "Kuçovë", "Kukës",
					"Kurbin", "Lezhë", "Librazhd", "Lushnjë", "Mallakastër",
					"Malsi e Madhe", "Mat", "Mirditë", "Peqin", "Përmet",
					"Pogradec", "Pukë", "Sarandë", "Shkodër", "Skrapar",
					"Tepelenë", "Tirana", "Tropojë", "Vlorë" },
			// ALGERIA
			{ "Adrar", "al-Agwat", "al-Bayad", "al-Buirah", "Algier",
					"al-Jilfah", "al-Masilah", "al-Midyah", "al-Wad",
					"an-Na'amah", "Annabah", "aš-Šalif", "at-Tarif",
					"'Ayn ad-Dafla", "'Ayn Timušanat", "Baššar", "Batnah",
					"Bijayah", "Biskrah", "Blidah", "Bumardas",
					"Burj Bu Arririj", "Galizan", "Gardayah", "H_anšalah",
					"Ilizi", "Jijili", "Milah", "Mu'askar", "Mustaganam",
					"Qalmah", "Qusantinah", "Sa'idah", "Sakikdah", "Satif",
					"Sidi bal'abbas", "Suq Ahras", "Tamanrasat", "Tibazah",
					"Tibissah", "Tilimsan", "Tinduf", "Tisamsilt", "Tiyarat",
					"Tizi Wazu", "Umm-al-Bawagi", "Wahran", "Warqla" },
			// AMERICAN SAMOA
			{ "Eastern", "Manu'a", "Swains Island", "Western" },
			// ANDORRA
			{ "Andorra la Vella", "Canillo", "Encamp", "Escaldes-Engordany",
					"La Massana", "Ordino", "Sant Julià de Lòria" }
			// ANGOLA
			,
			{ "Bengo", "Benguela", "Bié", "Cabinda", "Cuando-Cubango",
					"Cuanza-Norte", "Cuanza-Sul", "Cunene", "Huambo", "Huíla",
					"Luanda", "Lunda Norte", "Lunda Sul", "Malanje", "Moxico",
					"Namibe", "Uíge", "Zaire" }
			// ANGUILLA
			,
			{ "" }
			// ANTARCTICA
			,
			{}
			// ANTIGUA AND BARBUDA
			,
			{ "Barbuda", "Saint George", "Saint John", "Saint Mary",
					"Saint Paul", "Saint Peter", "Saint Philip" }
			// ARGENTINA
			,
			{ "Buenos Aires", "Catamarca", "Chaco", "Chubut", "Córdoba",
					"Corrientes", "Distrito Federal", "Entre Ríos", "Formosa",
					"Jujuy", "La Pampa", "La Rioja", "Mendoza", "Misiones",
					"Neuquén", "Río Negro", "Salta", "San Juan", "San Luis",
					"Santa Cruz", "Santa Fé", "Santiago del Estero",
					"Tierra del Fuego", "Tucumán" }
			// ARMENIA
			,
			{ "Aragatsotn", "Ararat", "Armavir", "Gegharkunik", "Kotayk",
					"Lori", "Shirak", "Syunik", "Tavush", "Vayots Dzor",
					"Yerevan" }
			// ARUBA
			,
			{ "" }
			// AUSTRIA
			,
			{ "Burgenland", "Kärnten", "Niederösterreich", "Oberösterreich",
					"Salzburg", "Steiermark", "Tirol", "Vorarlberg", "Wien" }
			// AUSTRALIA
			,
			{ "Australian Capital Territory", "New South Wales",
					"Northern Territory", "Queensland", "South Australia",
					"Tasmania", "Victoria", "Western Australia" }
			// AZERBAIJAN
			,
			{ "Abseron", "Aran", "Baki", "Dagliq Sirvan", "G?nc?-Qazax",
					"K?lb?c?r-Laçin", "L?nk?ran", "Naxçivan", "Quba-Xaçmaz",
					"S?ki-Zaqatala", "Yuxari Qarabag" }
			// BAHAMAS, THE
			,
			{ "Abaco", "Acklins Island", "Andros", "Berry Islands", "Biminis",
					"Cat Island", "Crooked Island", "Eleuthera",
					"Exuma and Cays", "Grand Bahama", "Inagua Islands",
					"Long Island", "Mayaguana", "New Providence",
					"Ragged Island", "Rum Cay", "San Salvador" }
			// BAHRAIN
			,
			{ "al-Garbiyah", "al-H?idd", "al-Muh?arraq", "al-Wusta",
					"ar-Rifa'a", "aš-Šamaliyah", "'Isa", "Jidh?afs",
					"Madinat H?amad", "Manama", "Sitrah" }
			// BANGLADESH
			,
			{ "Bagar Hat", "Bandarban", "Barguna", "Barisal", "Bhola",
					"Bogora", "Brahman Bariya", "Chandpur", "Chattagam",
					"Chuadanga", "Dhaka", "Dinajpur", "Faridpur", "Feni",
					"Gaybanda", "Gazipur", "Gopalganj", "Habiganj",
					"Jaipur Hat", "Jamalpur", "Jessor", "Jhalakati",
					"Jhanaydah", "Khagrachhari", "Khulna", "Kishorganj",
					"Koks Bazar", "Komilla", "Kurigram", "Kushtiya",
					"Lakshmipur", "Lalmanir Hat", "Madaripur", "Magura",
					"Maimansingh", "Manikganj", "Maulvi Bazar", "Meherpur",
					"Munshiganj", "Naral", "Narayanganj", "Narsingdi", "Nator",
					"Naugaon", "Nawabganj", "Netrakona", "Nilphamari",
					"Noakhali", "Pabna", "Panchagarh", "Patuakhali",
					"Pirojpur", "Rajbari", "Rajshahi", "Rangamati", "Rangpur",
					"Satkhira", "Shariatpur", "Sherpur", "Silhat", "Sirajganj",
					"Sunamganj", "Tangayal", "Thakurgaon" }
			// BARBADOS
			,
			{ "Christ Church", "Saint Andrew", "Saint George", "Saint James",
					"Saint John", "Saint Joseph", "Saint Lucy",
					"Saint Michael", "Saint Peter", "Saint Philip",
					"Saint Thomas" }
			// BELARUS
			,
			{ "Christ Church", "Saint Andrew", "Saint George", "Saint James",
					"Saint John", "Saint Joseph", "Saint Lucy",
					"Saint Michael", "Saint Peter", "Saint Philip",
					"Saint Thomas" }
			// BELGIUM
			,
			{ "Antwerpen", "Brabant Wallon", "Brüssel", "Flämisch Brabant",
					"Hennegau", "Limburg", "Lüttich", "Luxemburg", "Namur",
					"Ost-Flandern", "West-Flandern" }
			// BELIZE
			,
			{ "Belize", "Cayo", "Corozal", "Orange Walk", "Stann Creek",
					"Toledo" }
			// BENIN
			,
			{ "Alibori", "Atacora", "Atlantique", "Borgou", "Collines",
					"Couffo", "Donga", "Littoral", "Mono", "Ouémé", "Plateau",
					"Zou" }
			// BERMUDA
			,
			{ "Hamilton", "Saint George" }
			// BHUTAN
			,
			{ "Bumthang", "Chhukha", "Dagana", "Gasa", "Haa", "Lhuntse",
					"Mongar", "Paro", "Pemagatshel", "Punakha",
					"Samdrup Jongkhar", "Samtse", "Sarpang", "Thimphu",
					"Trashigang", "Trashiyangtse", "Trongsa", "Tsirang",
					"Wangdue Phodrang", "Zhemgang" }
			// BOLIVIA
			,
			{ "Tarija" }
			// BOSNIA AND HERZEGOVINA
			,
			{ "Distrikt Brcko", "Federacija Bosna i Hercegovina",
					"Republika Srpska" }
			// BOTSWANA
			,
			{ "Bobonong", "Boteti", "Chobe", "Francistown", "Gaborone",
					"Ghanzi", "Jwaneng", "Kgalagadi North", "Kgalagadi South",
					"Kgatleng", "Kweneng", "Lobatse", "Mahalapye", "Ngamiland",
					"Ngwaketse", "North East", "Okavango", "Orapa",
					"Selibe Phikwe", "Serowe-Palapye", "South East", "Sowa",
					"Tutume" }
			// BOUVET ISLAND
			,
			{}
			// BRAZIL
			,
			{ "Acre", "Alagoas", "Amapá", "Amazonas", "Bahia", "Ceará",
					"Distrito Federal", "Espírito Santo", "Goiás", "Maranhão",
					"Mato Grosso", "Mato Grosso do Sul", "Minas Gerais",
					"Pará", "Paraíba", "Paraná", "Pernambuco", "Piauí",
					"Rio de Janeiro", "Rio Grande do Norte",
					"Rio Grande do Sul", "Rondônia", "Roraima",
					"Santa Catarina", "São Paulo", "Sergipe", "Tocantins" }
			// BRITISH INDIAN OCEAN TERRITORY
			,
			{ "Tortola" }
			// BRUNEI
			,
			{ "Belait", "Brunei-Muara", "Temburong", "Tutong" }
			// BULGARIA
			,
			{ "Blagoevgrad", "Burgas", "Dobric", "Gabrovo", "Haskovo",
					"Jambol", "Kardžali", "Kjustendil", "Lovec", "Montana",
					"Pazardžik", "Pernik", "Pleven", "Plovdiv", "Razgrad",
					"Ruse", "Silistra", "Sliven", "Smoljan", "Sofija grad",
					"Sofijska oblast", "Stara Zagora", "Šumen", "Targovište",
					"Varna", "Veliko Tarnovo", "Vidin", "Vraca" }
			// BURKINA FASO
			,
			{ "Balé", "Bam", "Banwa", "Bazéga", "Bougouriba", "Boulgou",
					"Boulkiemdé", "Comoé", "Ganzourgou", "Gnagna", "Gourma",
					"Houet", "Ioba", "Kadiogo", "Kénédougou", "Komandjoari",
					"Kompienga", "Kossi", "Koulpélogo", "Kouritenga",
					"Kourwéogo", "Léraba", "Loroum", "Mouhoun", "Nahouri",
					"Namentenga", "Nayala", "Noumbiel", "Oubritenga",
					"Oudalan", "Passoré", "Poni", "Sanguié", "Sanmatenga",
					"Séno", "Sissili", "Soum", "Sourou", "Tapoa", "Tuy",
					"Yagha", "Yatenga", "Ziro", "Zondoma", "Zoundwéogo" }
			// BURUNDI
			,
			{ "Bubanza", "Bujumbura", "Bururi", "Cankuzo", "Cibitoke",
					"Gitega", "Karuzi", "Kayanza", "Kirundo", "Makamba",
					"Muramvya", "Muyinga", "Ngozi", "Rutana", "Ruyigi" }
			// CAMBODIA
			,
			{ "Banteay Mean Chey", "Bat Dâmbâng", "Kâmpóng Cham",
					"Kâmpóng Chhnang", "Kâmpóng Spoeu", "Kâmpóng Thum",
					"Kâmpôt", "Kândal", "Kaôh Kông", "Krâchéh", "Krong Kaeb",
					"Krong Pailin", "Krong Preah Sihanouk", "Môndôl Kiri",
					"Otdar Mean Chey", "Phnum Pénh", "Pousat", "Preah Vihéar",
					"Prey Veaeng", "Rôtanak Kiri", "Siem Reab",
					"Stueng Traeng", "Svay Rieng", "Takaev" }
			// CAMEROON
			,
			{ "Adamaoua", "Centre", "Est", "Littoral", "Nord", "Nord Extrème",
					"Nordouest", "Ouest", "Sud", "Sudouest" }
			// CANADA
			,
			{ "Alberta", "British Columbia", "Manitoba", "New Brunswick",
					"Newfoundland and Labrador", "Northwest Territories",
					"Nova Scotia", "Nunavut", "Ontario",
					"Prince Edward Island", "Québec", "Saskatchewan", "Yukon" }
			// CAPE VERDE
			,
			{ "Boavista", "Brava", "Fogo", "Maio", "Sal", "Santo Antão",
					"São Nicolau", "São Tiago", "São Vicente" }
			// CAYMAN ISLANDS
			,
			{ "Grand Cayman" }
			// CENTRAL AFRICAN REPUBLIC
			,
			{ "Bamingui-Bangoran", "Bangui", "Basse-Kotto", "Haute-Kotto",
					"Haut-Mbomou", "Kémo", "Lobaye", "Mambéré-Kadéï", "Mbomou",
					"Nana-Gribizi", "Nana-Mambéré", "Ombella Mpoko", "Ouaka",
					"Ouham", "Ouham-Pendé", "Sangha-Mbaéré", "Vakaga" }
			// CHAD
			,
			{ "Batha", "Biltine", "Bourkou-Ennedi-Tibesti", "Chari-Baguirmi",
					"Guéra", "Kanem", "Lac", "Logone Occidental",
					"Logone Oriental", "Mayo-Kébbi", "Moyen-Chari", "Ouaddaï",
					"Salamat", "Tandjilé" }
			// CHILE
			,
			{ "Aisén", "Antofagasta", "Araucanía", "Atacama", "Bío Bío",
					"Coquimbo", "Libertador General Bernardo O'Higgins",
					"Los Lagos", "Magellanes", "Maule", "Metropolitana",
					"Tarapacá", "Valparaíso" }
			// CHINA
			,
			{ "Anhui", "Aomen", "Beijing", "Chongqing", "Fujian", "Gansu",
					"Guangdong", "Guangxi", "Guizhou", "Hainan", "Hebei",
					"Heilongjiang", "Henan", "Hongkong", "Hubei", "Hunan",
					"Jiangsu", "Jiangxi", "Jilin", "Liaoning", "Neimenggu",
					"Ningxia", "Qinghai", "Shaanxi", "Shandong", "Shanghai",
					"Shanxi", "Sichuan", "Tianjin", "Xinjiang", "Xizang",
					"Yunnan", "Zhejiang" }
			// CHRISTMAS ISLAND
			,
			{}
			// COCOS (KEELING) ISLANDS
			,
			{ "Home Island", "West Island" }
			// COLOMBIA
			,
			{ "Amazonas", "Antioquia", "Arauca", "Atlántico", "Bogotá",
					"Bolívar", "Boyacá", "Caldas", "Caquetá", "Casanare",
					"Cauca", "César", "Chocó", "Córdoba", "Cundinamarca",
					"Guainía", "Guaviare", "Huila", "La Guajira", "Magdalena",
					"Meta", "Nariño", "Norte de Santander", "Putumayo",
					"Quindió", "Risaralda", "San Andrés y Providencia",
					"Santander", "Sucre", "Tolima", "Valle del Cauca",
					"Vaupés", "Vichada" }
			// COMOROS
			,
			{ "Mwali", "Ndzouani", "Ngazidja" }
			// CONGO, DEMOCRATIC REPUBLIC OF THE
			,
			{ "Bandundu", "Bas-Congo", "Équateur", "Haut-Congo",
					"Kasai-Occidental", "Kasai-Oriental", "Katanga",
					"Kinshasa", "Maniema", "Nord-Kivu", "Sud-Kivu" }
			// CONGO, REPUBLIC OF THE
			,
			{ "Bouenza", "Brazzaville", "Cuvette", "Cuvette-Ouest", "Kouilou",
					"Lékoumou", "Likouala", "Niari", "Plateaux", "Pool",
					"Sangha" }
			// COOK ISLANDS
			,
			{ "Aitutaki", "Atiu", "Mangaia", "Manihiki", "Mauke", "Mitiaro",
					"Nassau", "Pukapuka", "Rakahanga", "Rarotonga", "Tongareva" }
			// COSTA RICA
			,
			{ "Alajuela", "Cartago", "Guanacaste", "Heredia", "Limón",
					"Puntarenas", "San José" }
			// COTE D'IVOIRE
			,
			{ "Abidjan", "Bouaké", "Daloa", "Korhogo", "Yamoussoukro" }
			// CROATIA
			,
			{ "Bjelovar-Bilogora", "Dubrovnik-Neretva", "Grad Zagreb", "Istra",
					"Karlovac", "Koprivnica-Križevci", "Krapina-Zagorje",
					"Lika-Senj", "Medimurje", "Osijek-Baranja",
					"Požega-Slavonija", "Primorje-Gorski Kotar",
					"Šibenik-Knin", "Sisak-Moslavina",
					"Slavonski Brod-Posavina", "Split-Dalmacija", "Varaždin",
					"Virovitica-Podravina", "Vukovar-Srijem", "Zadar", "Zagreb" }
			// CUBA
			,
			{ "Camagüey", "Ciego de �?vila", "Cienfuegos",
					"Ciudad de la Habana", "Granma", "Guantánamo", "Holguín",
					"Isla de la Juventud", "La Habana", "Las Tunas",
					"Matanzas", "Pinar del Río", "Sancti Spíritus",
					"Santiago de Cuba", "Villa Clara" }
			// CYPRUS
			,
			{ "Government controlled area", "Turkish controlled area" }
			// CZECH REPUBLIC
			,
			{ "Jihoceský", "Jihomoravský", "Karlovarský", "Královéhradecký",
					"Liberecký", "Moravskoslezský", "Olomoucký", "Pardubický",
					"Plzenský", "Prag", "Stredoceský", "Ústecký", "Vysocina",
					"Zlínský" }
			// DENMARK
			,
			{ "Hovedstaden", "Midtjylland", "Nordjylland", "Sjælland",
					"Syddanmark" }
			// DJIBOUTI
			,
			{ "Ali Sabieh", "Dikhil", "Djibouti", "Obock", "Tadjoura" }
			// DOMINICA
			,
			{ "Saint Andrew", "Saint David", "Saint George", "Saint John",
					"Saint Joseph", "Saint Luke", "Saint Mark",
					"Saint Patrick", "Saint Paul", "Saint Peter" }
			// DOMINICAN REPUBLIC
			,
			{ "Azua", "Baoruco", "Barahona", "Dajabón", "Duarte", "Elías Piña",
					"El Seibo", "Espaillat", "Hato Mayor", "Independencia",
					"La Altagracia", "La Romana", "La Vega",
					"María Trinidad Sánchez", "Monseñor Nouel", "Monte Cristi",
					"Monte Plata", "Pedernales", "Peravia", "Puerto Plata",
					"Salcedo", "Samaná", "Sánchez Ramírez", "San Cristóbal",
					"San José de Ocoa", "San Juan", "San Pedro de Macorís",
					"Santiago", "Santiago Rodríguez", "Santo Domingo",
					"Valverde" }
			// ECUADOR
			,
			{ "Azuay", "Bolívar", "Cañar", "Carchi", "Chimborazo", "Cotopaxi",
					"El Oro", "Esmeraldas", "Galápagos", "Guayas", "Imbabura",
					"Loja", "Los Ríos", "Manabí", "Morona Santiago", "Napo",
					"Orellana", "Pastaza", "Pichincha", "Sucumbíos",
					"Tungurahua", "Zamora Chinchipe" }
			// EGYPT
			,
			{ "ad-Daqahliyah", "al-Bah?r-al-Ah?mar", "al-Buh?ayrah",
					"Alexandria", "al-Fayyum", "al-Garbiyah", "al-Ismailiyah",
					"al-Minufiyah", "al-Minya", "al-Qalyubiyah",
					"al-Wadi al-Jadid", "aš-Šarqiyah", "Assiut", "Assuan",
					"as-Suways", "Bani Suwayf", "Bur Sa'id", "Dumyat", "Giseh",
					"Kafr-aš-Šayh_", "Kairo", "Luxor", "Matruh", "Qina",
					"Šamal Sina", "Sawhaj", "South Sinai" }
			// EL SALVADOR
			,
			{ "Ahuachapán", "Cabañas", "Chalatenango", "Cuscatlán",
					"La Libertad", "La Paz", "La Unión", "Morazán",
					"San Miguel", "San Salvador", "Santa Ana", "San Vicente",
					"Sonsonate", "Usulután" }
			// EQUATORIAL GUINEA
			,
			{ "Annobón", "Bioko Norte", "Bioko Sur", "Centro Sur", "Kié-Ntem",
					"Litoral", "Wele-Nzas" }
			// ERITREA
			,
			{ "Anseba", "Debub", "Debub-Keih-Bahri", "Gash-Barka", "Maekel",
					"Semien-Keih-Bahri" }
			// ESTONIA
			,
			{ "Harju", "Hiiu", "Ida-Viru", "Järva", "Jogeva", "Lääne",
					"Lääne-Viru", "Pärnu", "Polva", "Rapla", "Saare", "Tartu",
					"Valga", "Viljandi", "Voru" }
			// ETHIOPIA
			,
			{ "Addis Abeba", "Afar", "Amhara", "Benishangul", "Diredawa",
					"Gambella", "Harar", "Oromia", "Somali", "Southern",
					"Tigray" }
			// FALKLAND ISLANDS (ISLAS MALVINAS)
			,
			{ "Falkland Islands", "South Georgia" }
			// FAROE ISLANDS
			,
			{ "Klaksvík", "Norðara Eysturoy", "Norðoy", "Sandoy", "Streymoy",
					"Suðuroy", "Syðra Eysturoy", "Tórshavn", "Vága" }
			// FIJI
			,
			{ "Central", "Eastern", "Northern", "Western" }
			// FINLAND
			,
			{ "Ahvenanmaa", "Etelä-Karjala", "Etelä-Pohjanmaa", "Etelä-Savo",
					"Itä-Uusimaa", "Kainuu", "Kanta-Häme", "Keski-Pohjanmaa",
					"Keski-Suomi", "Kymenlaakso", "Lappland", "Päijät-Häme",
					"Pirkanmaa", "Pohjanmaa", "Pohjois-Karjala",
					"Pohjois-Pohjanmaa", "Pohjois-Savo", "Satakunta",
					"Uusimaa", "Varsinais-Suomi" }
			// FRANCE
			,
			{ "Alsace", "Aquitaine", "Auvergne", "Basse-Normandie",
					"Bourgogne", "Bretagne", "Centre", "Champagne-Ardenne",
					"Corse", "Franche-Comté", "Haute-Normandie",
					"Île-de-France", "Languedoc-Roussillon", "Limousin",
					"Lorraine", "Midi-Pyrénées", "Nord-Pas-de-Calais",
					"Pays-de-la-Loire", "Picardie", "Poitou-Charentes",
					"Provence-Alpes-Côte-d'Azur", "Rhône-Alpes" }
			// FRENCH GUIANA
			,
			{ "Cayenne", "Saint-Laurent-du-Maroni" }
			// FRENCH POLYNESIA
			,
			{ "Îles du Vent", "Îles sous le Vent", "Marquises",
					"Tuamotu-Gambier", "Tubuai" }
			// FRENCH SOUTHERN AND ANTARCTIC LANDS
			,
			{ "Amsterdam", "Crozet Islands", "Kerguelen" }
			// GABON
			,
			{ "Estuaire", "Haut-Ogooué", "Moyen-Ogooué", "Ngounié", "Nyanga",
					"Ogooué-Ivindo", "Ogooué-Lolo", "Ogooué-Maritime",
					"Woleu-Ntem" }
			// GAMBIA, THE
			,
			{ "Banjul", "Basse", "Brikama", "Janjanbureh", "Kanifing",
					"Kerewan", "Kuntaur", "Mansakonko" }
			// GEORGIA
			,
			{ "Abkhasia", "Ajaria", "Guria", "Imereti", "Kakheti",
					"Kvemo Kartli", "Mtskheta-Mtianeti",
					"Raga-Lechkumi and Kverno Svaneti",
					"Samagrelo and Zemo Svaneti", "Samtskhe-Javakheti",
					"Shida Kartli", "Tiflis" }
			// GERMANY
			,
			{ "Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen",
					"Hamburg", "Hessen", "Mecklenburg-Vorpommern",
					"Niedersachsen", "Nordrhein-Westfalen", "Rheinland-Pfalz",
					"Saarland", "Sachsen", "Sachsen-Anhalt",
					"Schleswig-Holstein", "Thüringen" }
			// GHANA
			,
			{ "Ashanti", "Brong-Ahafo", "Central", "Eastern", "Greater Accra",
					"Northern", "Upper East", "Upper West", "Volta", "Western" }
			// GIBRALTAR
			,
			{ "" }
			// GREECE
			,
			{ "Attikí", "Kentriki Ellada", "Nisia Aigaiou Kriti",
					"Voria Ellada" }
			// GREENLAND
			,
			{ "Aasiaat", "Ammassalik", "Illoqqortoormiut", "Ilulissat",
					"Ivittuut", "Kangaatsiaq", "Maniitsoq", "Nanortalik",
					"Narsaq", "Nuuk", "Paamiut", "Qaanaaq", "Qaqortoq",
					"Qasigiannguit", "Qeqertarsuaq", "Sisimiut",
					"Udenfor kommunal inddeling", "Upernavik", "Uummannaq"

			}
			// GRENADA
			,
			{ "Carriacou-Petite Martinique", "Saint Andrew", "Saint Davids",
					"Saint George", "Saint John", "Saint Mark", "Saint Patrick"

			}
			// GUADELOUPE
			,
			{ "Basse-Terre", "Grande-Terre", "Îles des Saintes", "La Désirade",
					"Marie-Galante", "Saint Barthélemy", "Saint Martin" }
			// GUAM
			,
			{ "Agana Heights", "Agat", "Barrigada", "Chalan-Pago-Ordot",
					"Dededo", "Hagatña", "Inarajan", "Mangilao", "Merizo",
					"Mongmong-Toto-Maite", "Santa Rita", "Sinajana",
					"Talofofo", "Tamuning", "Yigo", "Yona" }
			// GUATEMALA
			,
			{ "Alta Verapaz", "Baja Verapaz", "Chimaltenango", "Chiquimula",
					"El Progreso", "Escuintla", "Guatemala", "Huehuetenango",
					"Izabal", "Jalapa", "Jutiapa", "Petén", "Quezaltenango",
					"Quiché", "Retalhuleu", "Sacatepéquez", "San Marcos",
					"Santa Rosa", "Sololá", "Suchitepéquez", "Totonicapán",
					"Zacapa" }
			// GUINEA
			,
			{ "Basse Guinée", "Conakry", "Guinée Forestière", "Haute Guinée",
					"Moyenne Guinée" }
			// GUINEA-BISSAU
			,
			{ "Bafatá", "Biombo", "Bissau", "Bolama", "Cacheu", "Gabú", "Oio",
					"Quinara", "Tombali" }
			// GUYANA
			,
			{ "Barima-Waini", "Cuyuni-Mazaruni", "Demerara-Mahaica",
					"East Berbice-Corentyne",
					"Essequibo Islands-West Demerara", "Mahaica-Berbice",
					"Pomeroon-Supenaam", "Upper Demerara-Berbice",
					"Upper Takutu-Upper Essequibo" }
			// HAITI
			,
			{ "Artibonite", "Centre", "Grand'Anse", "Nord", "Nord-Est",
					"Nord-Ouest", "Ouest", "Sud", "Sud-Est" }
			// HEARD ISLAND AND MCDONALD ISLANDS
			,
			{}
			// HOLY SEE (VATICAN CITY)
			,
			{}
			// HONDURAS
			,
			{ "Atlántida", "Choluteca", "Colón", "Comayagua", "Copán",
					"Cortés", "Distrito Central", "El Paraíso",
					"Francisco Morazán", "Gracias a Dios", "Intibucá",
					"Islas de la Bahía", "La Paz", "Lempira", "Ocotepeque",
					"Olancho", "Santa Bárbara", "Valle", "Yoro" }
			// HONG KONG
			,
			{ "Hongkong", "Kowloon and New Kowl" }
			// HUNGARY
			,
			{ "Bács-Kiskun", "Baranya", "Békés", "Borsod-Abaúj-Zemplén",
					"Budapest", "Csongrád", "Fejér", "Gyor-Moson-Sopron",
					"Hajdú-Bihar", "Heves", "Jász-Nagykun-Szolnok",
					"Komárom-Esztergom", "Nógrád", "Pest", "Somogy",
					"Szabolcs-Szatmár-Bereg", "Tolna", "Vas", "Veszprém",
					"Zala" }
			// ICELAND
			,
			{ "Austurland", "Höfuðborgarsvæði", "Norðurland eystra",
					"Norðurland vestra", "Suðurland", "Suðurnes", "Vestfirðir",
					"Vesturland" }
			// INDIA
			,
			{ "Andaman and Nicobar Islands", "Andhra Pradesh",
					"Arunachal Pradesh", "Assam", "Bangla", "Bihar",
					"Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli",
					"Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana",
					"Himachal Pradesh", "Jammu and Kashmir", "Jharkhand",
					"Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh",
					"Maharashtra", "Manipur", "Meghalaya", "Mizoram",
					"Nagaland", "Orissa", "Pondicherry", "Punjab", "Rajasthan",
					"Sikkim", "Tamil Nadu", "Tripura", "Uttaranchal",
					"Uttar Pradesh" }
			// INDONESIA
			,
			{ "Aceh", "Bali", "Bangka-Belitung", "Banten", "Bengkulu",
					"Gorontalo", "Jakarta", "Jambi", "Jawa Barat",
					"Jawa Tengah", "Jawa Timur", "Kalimantan Barat",
					"Kalimantan Selatan", "Kalimantan Tengah",
					"Kalimantan Timur", "Lampung", "Maluku", "Maluku Utara",
					"Nusa Tenggara Barat", "Nusa Tenggara Timur", "Papua",
					"Riau", "Riau Kepulauan", "Sulawesi Selatan",
					"Sulawesi Tengah", "Sulawesi Tenggara", "Sulawesi Utara",
					"Sumatera Barat", "Sumatera Selatan", "Sumatera Utara",
					"Yogyakarta" }
			// IRAN
			,
			{ "Ardabil", "Azarbayejan-e Gharbi", "Azarbayejan-e Sharqi",
					"Bushehr", "Chahar Mahal-e Bakhtiari", "Esfahan", "Fars",
					"Gilan", "Golestan", "Hamadan", "Hormozgan", "Ilam",
					"Kerman", "Kermanshah", "Khorasan-e Razavi",
					"Khorasan Janubi", "Khorasan Shamali", "Khuzestan",
					"Kohgiluyeh-e Boyerahmad", "Kordestan", "Lorestan",
					"Markazi", "Mazandaran", "Qazvin", "Qom", "Semnan",
					"Sistan-e Baluchestan", "Teheran", "Yazd", "Zanjan" }
			// IRAQ
			,
			{ "al-Anbar", "al-Basrah", "al-Mut_anna", "al-Qadisiyah",
					"an-Najaf", "as-Sulaymaniyah", "at-Ta'mim", "Babil",
					"Bagdad", "Dahuk", "�?i Qar", "Diyala", "Irbil", "Karbala",
					"Maysan", "Ninawa", "Salah?-ad-Din", "Wasit" }
			// IRELAND
			,
			{ "Carlow", "Cavan", "Clare", "Cork", "Donegal", "Dublin",
					"Galway", "Kerry", "Kildare", "Kilkenny", "Laois",
					"Leitrim", "Limerick", "Longford", "Louth", "Mayo",
					"Meath", "Monaghan", "Offaly", "Roscommon", "Sligo",
					"Tipperary North Riding", "Tipperary South Riding",
					"Waterford", "Westmeath", "Wexford", "Wicklow" }
			// ISLE OF MAN
			,
			{ "Castletown", "Douglas", "Laxey", "Onchan", "Peel", "Port Erin",
					"Port Saint Mary", "Ramsey" }
			// ISRAEL
			,
			{ "Hadarom", "Haifa", "Hamerkaz", "Haz_afon", "Jerusalem",
					"Judea and Samaria", "Tel Aviv" }
			// ITALY
			,
			{ "Abruzzen", "Apulien", "Basilicata", "Calabria", "Campania",
					"Emilia-Romagna", "Friuli-Venezia Giulia", "Lazio",
					"Ligurien", "Lombardei", "Marken", "Molise", "Piemonte",
					"Sardinien", "Sizilien", "Toscana", "Trentino-Alto Adige",
					"Umbria", "Valle d'Aosta", "Veneto" }
			// JAMAICA
			,
			{ "Clarendon", "Hanover", "Kingston", "Manchester", "Portland",
					"Saint Andrew", "Saint Ann", "Saint Catherine",
					"Saint Elizabeth", "Saint James", "Saint Mary",
					"Saint Thomas", "Trelawney", "Westmoreland" }
			// JAN MAYEN
			,
			{ "Länsimaa " }
			// JAPAN
			,
			{ "Aichi", "Akita", "Aomori", "Chiba", "Ehime", "Fukui", "Fukuoka",
					"Fukushima", "Gifu", "Gumma", "Hiroshima", "Hokkaido",
					"Hyogo", "Ibaraki", "Ishikawa", "Iwate", "Kagawa",
					"Kagoshima", "Kanagawa", "Kochi", "Kumamoto", "Kyoto",
					"Mie", "Miyagi", "Miyazaki", "Nagano", "Nagasaki", "Nara",
					"Niigata", "Oita", "Okayama", "Okinawa", "Osaka", "Saga",
					"Saitama", "Shiga", "Shimane", "Shizuoka", "Tochigi",
					"Tokio", "Tokushima", "Tottori", "Toyama", "Wakayama",
					"Yamagata", "Yamaguchi", "Yamanashi" }
			// JORDAN
			,
			{ "'Ajlun", "al-'Aqabah", "al-Balqa'", "al-Karak", "al-Mafraq",
					"'Amman", "at-Tafilah", "az-Zarqa'", "Irbid", "Jaraš",
					"Ma'an", "Madaba" }
			// KAZAKHSTAN
			,
			{ "Akmechet", "Almaty", "Aqmola", "Aqtöbe", "Atyrau",
					"Batis Kazakstan", "Mankistau", "Ontüstik Kazakstan",
					"Pavlodar", "Qaragandy", "Qostanay", "Sigis Kazakstan",
					"Soltüstik Kazakstan", "Taraz" }
			// KENYA
			,
			{ "Central", "Coast", "Eastern", "Nairobi", "North Eastern",
					"Nyanza", "Rift Valley", "Western" }
			// KIRIBATI
			,
			{ "Abaiang", "Abemana", "Aranuka", "Arorae", "Banaba", "Beru",
					"Butaritari", "Kiritimati", "Kuria", "Maiana", "Makin",
					"Marakei", "Nikunau", "Nonouti", "Onotoa",
					"Phoenix Islands", "Tabiteuea North", "Tabiteuea South",
					"Tabuaeran", "Tamana", "Tarawa North", "Tarawa South",
					"Teraina" }
			// KOREA, NORTH
			,
			{ "Chagangdo", "Hamgyongbukto", "Hamgyongnamdo", "Hwanghaebukto",
					"Hwanghaenamdo", "Kangwon", "Pyonganbukto", "Pyongannamdo",
					"Pyongyang", "Rason", "Yanggang" }
			// KOREA, SOUTH
			,
			{ "Cheju", "Chollabuk", "Chollanam", "Chungchongbuk",
					"Chungchongnam", "Inchon", "Kangwon", "Kwangju", "Kyonggi",
					"Kyongsangbuk", "Kyongsangnam", "Pusan", "Soul", "Taegu",
					"Taejon", "Ulsan" }

			// KUWAIT
			,
			{ "al-Ah?madi", "al-'Asamah", "al-Farwaniyah", "al-Jahra'",
					"Hawalli", "Mubarak al-Kabir" }
			// KYRGYZSTAN
			,
			{ "Batken", "Biskek", "Celalabad", "Çuy", "Issik-Göl", "Narin",
					"Os", "Talas" }
			// LAOS
			,
			{ "Attopu", "Bokeo", "Bolikhamsay", "Champasak", "Houaphanh",
					"Khammouane", "Luang Nam Tha", "Luang Prabang", "Oudomxay",
					"Phongsaly", "Saravan", "Savannakhet", "Sekong",
					"Viangchan Prefecture", "Viangchan Province", "Xaignabury",
					"Xiang Khuang" }
			// LATVIA
			,
			{ "Aizkraukles", "Aluksnes", "Balvu", "Bauskas", "Cesu",
					"Daugavpils", "Daugavpils pilseta", "Dobeles", "Gulbenes",
					"Jekabspils", "Jelgava", "Jelgavas", "Jurmala pilseta",
					"Kraslavas", "Kuldigas", "Liepaja", "Liepajas", "Limbažu",
					"Ludzas", "Madonas", "Ogres", "Preilu", "Rezekne",
					"Rezeknes", "Riga", "Rigas", "Saldus", "Talsu", "Tukuma",
					"Valkas", "Valmieras", "Ventspils", "Ventspils pilseta" }
			// LEBANON
			,
			{ "al-Biqa'a", "al-Janub", "an-Nabatiyah", "aš-Šamal",
					"Jabal Lubnan" }
			// LESOTHO
			,
			{ "Berea", "Butha-Buthe", "Leribe", "Mafeteng", "Maseru",
					"Mohale's Hoek", "Mokhotlong", "Qacha's Nek", "Quthing",
					"Thaba-Tseka" }
			// LIBERIA
			,
			{ "Bomi", "Bong", "Grand Bassa", "Grand Cape Mount", "Grand Gedeh",
					"Maryland and Grand Kru", "Montserrado", "Nimba" }
			// LIBYA
			,
			{ "Ajdabiya", "al-Butnan", "al-Hizam al-Ah_d?ar",
					"al-Jabal al-Ahd?ar", "al-Jifarah", "al-Jufrah",
					"al-Kufrah", "al-Marj", "al-Marqab", "al-Qubbah",
					"al-Wah?at", "an-Nuqat al-Hums", "az-Zawiyah", "Bangazi",
					"Bani Walid", "Darnah", "Gadamis", "Garyan", "Gat",
					"Marzuq", "Misratah", "Mizdah", "Nalut", "Sabha",
					"Sabratah wa Surman", "Surt", "Tarabulus",
					"Tarhunah wa Masallatah", "Wadi al-H?ayat", "Wadi aš-Šati",
					"Yafran wa Jadu" }
			// LIECHTENSTEIN
			,
			{ "Balzers", "Eschen", "Gamprin", "Mauren", "Planken", "Ruggell",
					"Schaan", "Schellenberg", "Triesen", "Triesenberg",
					"Vaduz", }
			// LITHUANIA
			,
			{ "Kaunas", "Klaipeda", "Panevezys", "Šiauliai", "Vilna" }
			// LUXEMBOURG
			,
			{ "Capellen", "Clervaux", "Diekirch", "Echternach",
					"Esch-sur-Alzette", "Grevenmacher", "Luxemburg", "Mersch",
					"Redange", "Remich", "Vianden", "Wiltz" }
			// MACAU
			/*
			 * , { "Macau" }
			 */
			// MACEDONIA
			,
			{ "Berovo", "Bitola", "Brod", "Debar", "Delcevo", "Demir Hisar",
					"Gevgelija", "Gostivar", "Kavadarci", "Kicevo", "Kocani",
					"Kratovo", "Kriva Palanka", "Kruševo", "Kumanovo",
					"Negotino", "Ohrid", "Prilep", "Probištip", "Radoviš",
					"Resen", "Skopje", "Štip", "Struga", "Strumica",
					"Sveti Nikole", "Tetovo", "Valandovo", "Veles", "Vinica" }
			// MADAGASCAR
			,
			{ "Antananarivo", "Antsiranana", "Fianarantsoa", "Mahajanga",
					"Toamasina", "Toliary" }
			// MALAWI
			,
			{ "Balaka", "Blantyre City", "Chikwawa", "Chiradzulu", "Chitipa",
					"Dedza", "Dowa", "Karonga", "Kasungu", "Lilongwe City",
					"Machinga", "Mangochi", "Mchinji", "Mulanje", "Mwanza",
					"Mzimba", "Mzuzu City", "Nkhata Bay", "Nkhotakota",
					"Nsanje", "Ntcheu", "Ntchisi", "Phalombe", "Rumphi",
					"Salima", "Thyolo", "Zomba Municipality" }
			// MALAYSIA
			,
			{ "Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan", "Melaka",
					"Negeri Sembilan", "Pahang", "Perak", "Perlis",
					"Pulau Pinang", "Sabah", "Sarawak", "Selangor",
					"Terengganu" }
			// MALDIVES
			,
			{ "Alif Alif", "Alif Dhaal", "Baa", "Dhaal", "Faaf", "Gaaf Alif",
					"Gaaf Dhaal", "Ghaviyani", "Haa Alif", "Haa Dhaal", "Kaaf",
					"Laam", "Lhaviyani", "Malé", "Miim", "Nuun", "Raa",
					"Shaviyani", "Siin", "Thaa", "Vaav" }
			// MALI
			,
			{ "Bamako", "Gao", "Kayes", "Kidal", "Koulikoro", "Mopti", "Ségou",
					"Sikasso", "Tombouctou" }
			// MALTA
			,
			{ "Gozo and Comino", "Northern", "Northern Harbour",
					"South Eastern", "Southern Harbour", "Western" }
			// MARSHALL ISLANDS
			,
			{ "Ailinlaplap", "Ailuk", "Arno", "Aur", "Bikini", "Ebon",
					"Enewetak", "Jabat", "Jaluit", "Kili", "Kwajalein", "Lae",
					"Lib", "Likiep", "Majuro", "Maloelap", "Mejit", "Mili",
					"Namorik", "Namu", "Rongelap", "Ujae", "Utrik", "Wotho",
					"Wotje" }
			// MARTINIQUE
			,
			{ "Fort-de-France", "La Trinité", "Le Marin", "Saint-Pierre" }
			// MAURITANIA
			,
			{ "Adrar", "Assaba", "Brakna", "�?ah_lat Nawadibu", "Guidimagha",
					"Gurgul", "Hud-al-Garbi", "Hud-aš-Šarqi", "Inširi",
					"Nawakšut", "Takant", "Tiris Zammur", "Trarza" }
			// MAURITIUS
			,
			{ "Black River", "Flacq", "Grand Port", "Moka", "Pamplemousses",
					"Plaines Wilhelm", "Riviere du Rempart", "Rodrigues",
					"Savanne" }
			// MAYOTTE
			,
			{ "Mayotte", "Pamanzi" }
			// MEXICO
			,
			{ "Aguascalientes", "Baja California", "Baja California Sur",
					"Campeche", "Chiapas", "Chihuahua", "Coahuila", "Colima",
					"Distrito Federal", "Durango", "Guanajuato", "Guerrero",
					"Hidalgo", "Jalisco", "México", "Michoacán", "Morelos",
					"Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro",
					"Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora",
					"Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán",
					"Zacatecas" }
			// MICRONESIA, FEDERATED STATES OF
			,
			{ "Chuuk", "Kusaie", "Pohnpei", "Yap" }
			// MOLDOVA
			,
			{ "Anenii Noi", "Balti", "Basarabeasca", "Briceni", "Cahul",
					"Calarasi", "Camenca", "Cantemir", "Causeni", "Chisinau",
					"Cimislia", "Criuleni", "Donduseni", "Drochia",
					"Dubasari municipiu", "Edinet", "Falesti", "Floresti",
					"Gagauzia", "Glodeni", "Grigoriopol", "Hîncesti",
					"Ialoveni", "Leova", "Nisporeni", "Ocnita", "Orhei",
					"Rezina", "Rîbnita", "Rîscani", "Sîngerei", "Slobozia",
					"Soldanesti", "Soroca", "Stefan Voda", "Straseni",
					"Taraclia", "Telenesti", "Tighina", "Tiraspol", "Ungheni" }
			// MONACO
			,
			{ "Fontvieille", "La Condamine", "Monaco-Ville", "Monte Carlo" }
			// MONGOLIA
			,
			{ "Arhangaj", "Bajanhongor", "Bajan-Ölgij", "Bulgan", "Darhan-Uul",
					"Dornod", "Dornogovi", "Dundgovi", "Govi-Altaj",
					"Govisumber", "Hèntij", "Hovd", "Hövsgöl", "Ömnögovi",
					"Orhon", "Övörhangaj", "Sèlèngè", "Sühbaatar", "Töv",
					"Ulaanbaatar", "Uvs", "Zavhan" }
			// MONTSERRAT
			,
			{ "Plymouth " }
			// MOROCCO
			,
			{ "Casablanca", "Chaouia-Ouardigha", "Doukkala-Abda",
					"Fès-Boulemane", "Gharb-Chrarda-Béni Hssen", "Guelmim",
					"Marrakech-Tensift-Al Haouz", "Meknes-Tafilalet",
					"Oriental", "Rabat-Salé-Zammour-Zaer", "Souss Massa-Draâ",
					"Tadla-Azilal", "Tangier-Tétouan",
					"Taza-Al Hoceima-Taounate" }
			// MOZAMBIQUE
			,
			{ "Cabo Delgado", "Gaza", "Inhambane", "Manica", "Maputo",
					"Maputo Provincia", "Nampula", "Niassa", "Sofala", "Tete",
					"Zambezia" }
			// NAMIBIA
			,
			{ "Caprivi", "Erongo", "Hardap", "Karas", "Kavango", "Khomas",
					"Kunene", "Ohangwena", "Omaheke", "Omusati", "Oshana",
					"Oshikoto", "Otjozondjupa" }
			// NAURU
			,
			{ "Aiwo" }
			// NEPAL
			,
			{ "Baglung", "Banke", "Bara", "Bardiya", "Bhaktapur", "Chitwan",
					"Dadeldhura", "Dailekh", "Dang Deokhuri", "Darchula",
					"Dhankuta", "Dhanusa", "Dolakha", "Doti", "Gorkha", "Ilam",
					"Jhapa", "Jumla", "Kailali", "Kanchanpur", "Kapilvastu",
					"Kaski", "Kathmandu", "Kavrepalanchok", "Lalitpur",
					"Mahottari", "Makwanpur", "Morang", "Nawalparasi",
					"Nuwakot", "Palpa", "Parsa", "Rautahat", "Rupandehi",
					"Sankhuwasabha", "Saptari", "Sarlahi", "Sindhuli",
					"Siraha", "Sunsari", "Surkhet", "Syangja", "Tanahu",
					"Udayapur" }
			// NETHERLANDS
			,
			{ "Drenthe", "Flevoland", "Friesland", "Gelderland", "Groningen",
					"Limburg", "Noord-Brabant", "Noord-Holland", "Overijssel",
					"Utrecht", "Zeeland", "Zuid-Holland" }
			// NETHERLANDS ANTILLES
			,
			{ "Bonaire", "Curaçao", "Saba", "Sint Eustatius", "Sint Maarten" }
			// NEW CALEDONIA
			,
			{ "Îles", "Nord", "Sud" }
			// NEW ZEALAND
			,
			{ "Auckland", "Bay of Plenty", "Canterbury", "Gisborne",
					"Hawke's Bay", "Manawatu-Wanganui", "Marlborough",
					"Nelson", "Northland", "Otago", "Southland", "Taranaki",
					"Tasman", "Waikato", "Wellington", "West Coast" },
			// NICARAGUA
			{ "Atlántico Norte", "Atlántico Sur", "Boaco", "Carazo",
					"Chinandega", "Chontales", "Estelí", "Granada", "Jinotega",
					"León", "Madriz", "Managua", "Masaya", "Matagalpa",
					"Nueva Segovia", "Río San Juan", "Rivas" },
			// NIGER
			{ "Agadez", "Diffa", "Dosso", "Maradi", "Niamey", "Tahoua",
					"Tillabéry", "Zinder" },
			// NIGERIA
			{ "Abia", "Abuja Federal Capital Territory", "Adamawa",
					"Akwa Ibom", "Anambra", "Bauchi", "Bayelsa", "Benue",
					"Borno", "Cross River", "Delta", "Ebonyi", "Edo", "Ekiti",
					"Enugu", "Gombe", "Imo", "Jigawa", "Kaduna", "Kano",
					"Katsina", "Kebbi", "Kogi", "Kwara", "Lagos", "Nassarawa",
					"Niger", "Ogun", "Ondo", "Osun", "Oyo", "Plateau",
					"Rivers", "Sokoto", "Taraba", "Yobe", "Zamfara" },
			// NIUE
			{ "" },
			// NORFOLK ISLAND
			{ "" },
			// NORTHERN MARIANA ISLANDS
			{ "Northern Islands", "Rota", "Saipan", "Tinian" },
			// NORWAY
			{ "Akershus", "Aust-Agder", "Buskerud", "Finnmark", "Hedmark",
					"Hordaland", "Møre og Romsdal", "Nordland",
					"Nord-Trøndelag", "Oppland", "Oslo", "Østfold", "Rogaland",
					"Sogn og Fjordane", "Sør-Trøndelag", "Telemark", "Troms",
					"Vest-Agder", "Vestfold" },
			// OMAN
			{ "ad-Dah_iliyah", "al-Batinah", "aš-Šarqiyah", "az?-Z?ahirah",
					"Maskat", "Musandam", "Z?ufar" },
			// PAKISTAN
			{ "Azad Kashmir", "Baluchistan", "Federal Capital Area",
					"Federally administered Tribal Areas", "Northern Areas",
					"North-West Frontier", "Punjab", "Sind" },
			// PALAU
			{ "Aimeliik", "Airai", "Angaur", "Hatobohei", "Kayangel", "Koror",
					"Melekeok", "Ngaraard", "Ngardmau", "Ngaremlengui",
					"Ngatpang", "Ngchesar", "Ngerchelong", "Ngiwal", "Peleliu",
					"Sonsorol" },
			// PANAMA
			{ "Bocas del Toro", "Chiriquí", "Coclé", "Colón", "Darién",
					"Emberá", "Herrera", "Kuna de Madungandí",
					"Kuna de Wargandí", "Kuna Yala", "Los Santos",
					"Ngöbe Buglé", "Panamá", "Veraguas" }
			// PAPUA NEW GUINEA
			,
			{ "Eastern Highlands", "East New Britain", "East Sepik", "Enga",
					"Fly River", "Gulf", "Madang", "Manus", "Milne Bay",
					"Morobe", "National Capital District", "New Ireland",
					"North Solomons", "Oro", "Sandaun", "Simbu",
					"Southern Highlands", "Western Highlands",
					"West New Britain" },
			// PARAGUAY

			{ "Alto Paraguay", "Alto Paraná", "Amambay", "Asunción",
					"Boquerón", "Caaguazú", "Caazapá", "Canendiyú", "Central",
					"Concepción", "Cordillera", "Guairá", "Itapúa", "Misiones",
					"Ñeembucú", "Paraguarí", "Presidente Hayes", "San Pedro" },
			// PERU

			{ "Amazonas", "Ancash", "Apurímac", "Arequipa", "Ayacucho",
					"Cajamarca", "Callao", "Cusco", "Huancavelica", "Huánuco",
					"Ica", "Junín", "La Libertad", "Lambayeque",
					"Lima Provincias", "Loreto", "Madre de Dios", "Moquegua",
					"Pasco", "Piura", "Puno", "San Martín", "Tacna", "Tumbes",
					"Ucayali" },
			// PHILIPPINES

			{ "Bicol", "Cagayan Valley", "Caraga", "Central Luzon",
					"Central Mindanao", "Central Visayas", "Cordillera",
					"Eastern Visayas", "Ilocos", "Muslim Mindanao",
					"National Capital Region", "Northern Mindanao",
					"Southern Mindanao", "Southern Tagalog",
					"Western Mindanao", "Western Visayas" },

			// POLAND
			{ "Dolnoslaskie", "Kujawsko-Pomorskie", "Lódzkie", "Lubelskie",
					"Lubuskie", "Malopolskie", "Mazowieckie", "Opolskie",
					"Podkarpackie", "Podlaskie", "Pomorskie", "Slaskie",
					"Swietokrzyskie", "Warminsko-Mazurskie", "Wielkopolskie",
					"Zachodnio-Pomorskie" },
			// PORTUGAL

			{ "Açores", "Alentejo", "Algarve", "Centro",
					"Lisboa e Vale do Tejo", "Madeira", "Norte" },
			// PUERTO RICO

			{ "Arecibo", "Bayamón", "Carolina", "Guayama", "Humacao",
					"Mayagüez-Aguadilla", "Ponce", "San Juan" },
			// QATAR

			{ "al-Guwayriyah", "al-H_awr", "al-Jumayliyah", "al-Wakrah",
					"ar-Rayyan", "aš-Šamal", "Doha", "Jariyan al-Batnah",
					"Musay'id", "Umm Salal" },
			// REUNION
			{ "Saint-Benoît", "Saint-Denis", "Saint-Paul", "Saint-Pierre" },
			// ROMANIA
			{ "Alba", "Arad", "Arges", "Bacau", "Bihor", "Bistrita-Nasaud",
					"Botosani", "Braila", "Brasov", "Bukarest", "Buzau",
					"Calarasi", "Caras-Severin", "Cluj", "Constanta",
					"Covasna", "Dâmbovita", "Dolj", "Galati", "Giurgiu",
					"Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi",
					"Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt", "Olt",
					"Prahova", "Salaj", "Satu Mare", "Sibiu", "Suceava",
					"Teleorman", "Timis", "Tulcea", "Vâlcea", "Vaslui",
					"Vrancea" },
			// RUSSIA
			{ "Adygeja", "Aga", "Alanija", "Altaj", "Amur", "Arhangelsk",
					"Astrahan", "Baškortostan", "Belgorod", "Brjansk",
					"Burjatija", "Cecenija", "Celjabinsk", "Cita", "Cukotka",
					"Cuvašija", "Dagestan", "Evenkija", "Gorno-Altaj",
					"Habarovsk", "Hakasija", "Hanty-Mansija", "Ingušetija",
					"Irkutsk", "Ivanovo", "Jamalo-Nenets", "Jaroslavl",
					"Jevrej", "Kabardino-Balkarija", "Kaliningrad",
					"Kalmykija", "Kaluga", "Kamcatka", "Karacaj-Cerkessija",
					"Karelija", "Kemerovo", "Kirov", "Komi", "Komi-Permjak",
					"Korjakija", "Kostroma", "Krasnodar", "Krasnojarsk",
					"Kurgan", "Kursk", "Leningrad", "Lipeck", "Magadan",
					"Marij El", "Mordovija", "Moskau", "Moskovskaja Oblast",
					"Murmansk", "Nenets", "Nižnij Novgorod", "Novgorod",
					"Novosibirsk", "Omsk", "Orenburg", "Orjol", "Penza",
					"Perm", "Primorje", "Pskov", "Rjazan", "Rostov", "Saha",
					"Sahalin", "Samara", "Sankt Petersburg", "Saratov",
					"Smolensk", "Stavropol", "Sverdlovsk", "Tajmyr", "Tambov",
					"Tatarstan", "Tjumen", "Tomsk", "Tula", "Tver", "Tyva",
					"Udmurtija", "Uljanovsk", "Ust-Orda", "Vladimir",
					"Volgograd", "Vologda", "Voronež" },

			// RWANDA
			{ "Butare", "Byumba", "Cyangugu", "Gikongoro", "Gisenyi",
					"Gitarama", "Kibungo", "Kibuye", "Ruhengeri",
					"Ville de Kigali" },
			// SAINT HELENA
			{ "Ascension", "Saint Helena", "Tristan da Cunha" },
			// SAINT KITTS AND NEVIS
			{ "Christ Church Nichola Town", "Saint Anne Sandy Point",
					"Saint George Basseterre", "Saint George Gingerland",
					"Saint James Windward", "Saint John Capesterre",
					"Saint John Figtree", "Saint Mary Cayon",
					"Saint Paul Capesterre", "Saint Paul Charlestown",
					"Saint Peter Basseterre", "Saint Thomas Lowland",
					"Saint Thomas Middle Island", "Trinity Palmetto Point" },
			// SAINT LUCIA
			{ "Anse-la-Raye", "Canaries", "Castries", "Choiseul", "Dennery",
					"Gros Inlet", "Laborie", "Micoud", "Soufrière",
					"Vieux Fort" },
			// SAINT PIERRE AND MIQUELON
			{ "Miquelon-Langlade", "Saint-Pierre" },
			// SAINT VINCENT AND THE GRENADINES
			{ "Charlotte", "Grenadines", "Saint David", "Saint George" },
			// SAMOA
			{ "Apia Urban Area", "North West Upolu", "Rest of Upolu", "Savaii" },
			// SAN MARINO
			{ "Acquaviva", "Borgo Maggiore", "Chiesanuova", "Domagnano",
					"Faetano", "Fiorentino", "Montegiardino", "San Marino",
					"Serravalle" },
			// SAO TOME AND PRINCIPE
			{ "�?gua Grande", "Cantagalo", "Caué", "Lemba", "Lobata",
					"Mé-Zochi", "Pagué" },
			// SAUDI ARABIA
			{ "al-Bah?ah", "al-H?udud-aš-Šamaliyah", "al-Jawf", "al-Madinah",
					"al-Qasim", "'Asir", "aš-Šarqiyah", "H?a'il", "Jizan",
					"Makkah", "Najran", "Riad", "Tabuk" },
			// SENEGAL
			{ "Dakar", "Diourbel", "Fatick", "Kaolack", "Kolda", "Louga",
					"Matam", "Saint-Louis", "Tambacounda", "Thiès",
					"Ziguinchor" },
			// SERBIA AND MONTENEGRO
			{ "Central Serbia", "Vojvodina" },
			// SEYCHELLES
			{ "Mahé" },
			// SIERRA LEONE
			{ "Eastern", "Northern", "Southern", "Western" },
			// SINGAPORE
			{ " " },
			// SLOVAKIA
			{ "Banskobystrický", "Bratislavský", "Košický", "Nitrianský",
					"Prešovský", "Trencianský", "Trnavský", "Žilinský" },
			// SLOVENIA
			{ "Gorenjska", "Goriška", "Jugovžodna Slovenija", "Koroška",
					"Notranjsko-kraška", "Obalno-kraška", "Osrednjeslovenska",
					"Podravska", "Pomurska", "Savinjska", "Spodnjeposavska",
					"Zasavska" },
			// SOLOMON ISLANDS
			{ "Central", "Choiseul", "Guadalcanal", "Isabel",
					"Makira and Ulawa", "Malaita", "Rennell and Bellona",
					"Temotu", "Western" },
			// SOMALIA
			{ "Awdaal", "Baakool", "Baarii", "Baay", "Banaadir", "Gaalguuduud",
					"Gedoo", "Hiiraan", "Jubbada Dhexe", "Jubbada Hoose",
					"Mudug", "Nuugaal", "Sanaag", "Shabeellaha Dhexe",
					"Shabeellaha Hoose", "Sool", "Togdeer", "Woqooyi Galbeed" },
			// SOUTH AFRICA
			{ "Eastern Cape", "Free State", "Gauteng", "KwaZulu Natal",
					"Limpopo", "Mpumalanga", "Northern Cape", "North West",
					"Western Cape" },
			// SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS
			{},
			// SPAIN
			{ "Andalusien", "Aragonien", "Asturien", "Balearen", "Baskenland",
					"Ceuta", "Extremadura", "Galizien", "Kanaren",
					"Kantabrien", "Kastilien-La Mancha", "Kastilien-León",
					"Katalonien", "La Rioja", "Madrid", "Melilla", "Murcia",
					"Navarra", "Valencia" },
			// SRI LANKA

			{ "Amparai", "Anuradhapuraya", "Badulla", "Colombo", "Galla",
					"Gampaha", "Hambantota", "Kalatura", "Kegalla",
					"Kilinochchi", "Kurunegala", "Mad?akalpuwa", "Maha Nuwara",
					"Mannarama", "Matale", "Matara", "Monaragala",
					"Nuwara Eliya", "Puttalama", "Ratnapuraya",
					"Tirikunamalaya", "Vavuniyawa", "Yapanaya" },
			// SUDAN

			{ "A'ali-an-Nil", "al-Bah?r-al-Ah?mar", "al-Buh?ayrat",
					"al-Jazirah", "al-Qad?arif", "al-Wah?dah",
					"an-Nil-al-Abyad?", "an-Nil-al-Azraq", "aš-Šamaliyah",
					"Bah?r-al-Jabal", "Garb-al-Istiwa'iyah",
					"Garb Bah?r-al-Gazal", "Garb Darfur", "Garb Kurdufan",
					"Janub Darfur", "Janub Kurdufan", "Junqali", "Kassala",
					"Khartum", "Nahr-an-Nil", "Šamal Bah?r-al-Gazal",
					"Šamal Darfur", "Šamal Kurdufan", "Šarq-al-Istiwa'iyah",
					"Sinnar", "Warab" },
			// SURINAME
			{ "Brokopondo", "Commewijne", "Coronie", "Marowijne", "Nickerie",
					"Para", "Paramaribo", "Saramacca", "Wanica" },
			// SVALBARD
			{ "Jan Mayen", "Svalbard" },
			// SWAZILAND
			{ "Hhohho", "Lubombo", "Manzini", "Shiselweni" },
			// SWEDEN
			{ "Blekinge", "Dalarna", "Gävleborg", "Gotland", "Halland",
					"Jämtland", "Jönköping", "Kalmar", "Kronoberg",
					"Norrbotten", "Örebro", "Östergötland", "Skåne",
					"Södermanland", "Stockholm", "Uppsala", "Värmland",
					"Västerbotten", "Västernorrland", "Västmanland",
					"Västra Götaland" },
			// SWITZERLAND
			{ "Aargau", "Appenzell-Ausser Rhoden", "Appenzell Inner-Rhoden",
					"Basel-Landschaft", "Basel-Stadt", "Bern", "Freiburg",
					"Genf", "Glarus", "Graubünden", "Jura", "Luzern",
					"Neuenburg", "Nidwalden", "Obwalden", "Sankt Gallen",
					"Schaffhausen", "Schwyz", "Solothurn", "Tessin", "Thurgau",
					"Uri", "Waadt", "Wallis", "Zug", "Zürich" },

			// SYRIA
			{ "al-H?asakah", "al-Ladiqiyah", "al-Qunaytirah", "ar-Raqqah",
					"as-Suwayda", "Damaskus", "Dar'a", "Dayr-az-Zawr",
					"H?alab", "H?amah", "H?ims", "Idlib", "Tartus" },

			// TAIWAN
			{ "Changhwa", "Chiayi Hsien", "Chiayi Shih", "Hsinchu Hsien",
					"Hsinchu Shih", "Hualien", "Ilan", "Kaohsiung Hsien",
					"Kaohsiung Shih", "Keelung Shih", "Kinmen", "Miaoli",
					"Nantou", "Penghu", "Pingtung", "Taichung Hsien",
					"Taichung Shih", "Tainan Hsien", "Tainan Shih",
					"Taipei Hsien", "Taitung", "Taoyuan", "Yunlin" }
			// TAJIKISTAN
			,
			{ "Dushanbe", "Gorno-Badakhshan", "Karotegin", "Khatlon", "Sughd" }
			// TANZANIA
			,
			{ "Arusha", "Dar es Salaam", "Dodoma", "Iringa", "Kagera",
					"Kigoma", "Kilimanjaro", "Lindi", "Manyara", "Mara",
					"Mbeya", "Morogoro", "Mtwara", "Mwanza", "Pwani", "Rukwa",
					"Ruvuma", "Shinyanga", "Singida", "Tabora", "Tanga",
					"Zanzibar and Pemba" }
			// THAILAND
			,
			{ "Central", "Krung Thep", "Northeastern", "Northern", "Southern" }
			// TIMOR-LESTE
			,
			{}
			// TOGO
			,
			{ "Centre", "Kara", "Maritime", "Plateaux", "Savanes" }
			// TOKELAU
			,
			{ "Atafu", "Fakaofo", "Nukunonu" }
			// TONGA
			,
			{ "Eua", "Ha'apai", "Niuas", "Tongatapu", "Vava'u" }
			// TRINIDAD AND TOBAGO
			,
			{ "Arima", "Chaguanas", "Couva-Tabaquite-Talparo", "Diego Martín",
					"Mayaro-Río Claro", "Peñal Débé", "Point Fortín",
					"Port of Spain", "Princes Town", "San Fernando",
					"Sangre Grande", "San Juan-Laventville", "Siparia",
					"Tobago", "Tunapuna-Piarco" }
			// TUNISIA
			,
			{ "al-Kaf", "al-Mahdiyah", "al-Munastir", "al-Qasrayn",
					"al-Qayrawan", "Aryanah", "Bajah", "Bin 'Arus", "Binzart",
					"Jundubah", "Madaniyin", "Manubah", "Nabul", "Qabis",
					"Qafsah", "Qibili", "Safaqis", "Sidi Bu Zayd", "Silyanah",
					"Susah", "Tatawin", "Tawzar", "Tunis", "Zagwan" }
			// TURKEY
			,
			{ "Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Aksaray",
					"Amasya", "Ankara", "Antalya", "Ardahan", "Artvin",
					"Aydin", "Balikesir", "Bartin", "Batman", "Bayburt",
					"Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
					"Çanakkale", "Çankiri", "Çorum", "Denizli", "Diyarbakir",
					"Düzce", "Edirne", "Elazig", "Erzincan", "Erzurum",
					"Eskisehir", "Gaziantep", "Giresun", "Gümüshane",
					"Hakkari", "Hatay", "Igdir", "Isparta", "Istanbul",
					"Izmir", "Kahramanmaras", "Karabük", "Karaman", "Kars",
					"Kastamonu", "Kayseri", "Kilis", "Kirikkale", "Kirklareli",
					"Kirsehir", "Kocaeli", "Konya", "Kütahya", "Malatya",
					"Manisa", "Mardin", "Mersin", "Mugla", "Mus", "Nevsehir",
					"Nigde", "Ordu", "Osmaniye", "Rize", "Sakarya", "Samsun",
					"Sanliurfa", "Siirt", "Sinop", "Sirnak", "Sivas",
					"Tekirdag", "Tokat", "Trabzon", "Tunceli", "Usak", "Van",
					"Yalova", "Yozgat", "Zonguldak" }
			// TURKMENISTAN
			,
			{ "Ahal", "Asgabat", "Balkan", "Dasoguz", "Lebap", "Mari" }
			// TURKS AND CAICOS ISLANDS
			,
			{ "Grand Turk", "Middle Caicos", "North Caicos",
					"Providenciales and West Caicos", "Salt Cay",
					"South Caicos and East Caicos" }
			// TUVALU
			,
			{ "Funafuti", "Nanumanga", "Nanumea", "Niutao", "Nui", "Nukufetau",
					"Nukulaelae", "Vaitupu" }
			// UGANDA
			,
			{ "Central", "Eastern", "Northern", "Western" }
			// UKRAINE
			,
			{ "Central", "Eastern", "Northern", "Western" }
			// UNITED ARAB EMIRATES
			,
			{ "Abu Dhabi", "'Ajman", "al-Fujayrah", "aš-Šariqah", "Dubai",
					"Ra's al-H_aymah", "Umm al-Qaywayn" }
			// URUGUAY
			,
			{ "Artigas", "Canelones", "Cerro Largo", "Colonia", "Durazno",
					"Flores", "Florida", "Lavalleja", "Maldonado",
					"Montevideo", "Paysandú", "Río Negro", "Rivera", "Rocha",
					"Salto", "San José", "Soriano", "Tacuarembó",
					"Treinta y Tres" }
			// UZBEKISTAN
			,
			{ "Andijon", "Buhoro", "Cizah", "Fargona", "Horazm", "Kaskadarya",
					"Korakalpogiston", "Namangan", "Navoi", "Samarkand",
					"Sirdare", "Surhondar", "Taschkent" }
			// VANUATU
			,
			{ "Malampa", "Penama", "Sanma", "Shefa", "Tafea", "Torba" }
			// VENEZUELA
			,
			{ "Amazonas", "Anzoátegui", "Apure", "Aragua", "Barinas",
					"Bolívar", "Carabobo", "Cojedes", "Delta Amacuro",
					"Distrito Capital", "Falcón", "Guárico", "Lara", "Mérida",
					"Miranda", "Monagas", "Nueva Esparta", "Portuguesa",
					"Sucre", "Táchira", "Trujillo", "Vargas", "Yaracuy",
					"Zulia" }
			// VIETNAM
			,
			{ "B?c Trung B?", "�??ng b?ng sông C?u Long", "�??ng b?ng sông H?ng",
					"�?ông B?c B?", "�?ông Nam B?", "Duyên h?i Nam Trung B?",
					"Tây B?c B?", "Tây Nguyên" }
			// VIRGIN ISLANDS
			,
			{ "Saint Croix", "Saint John", "Saint Thomas" }
			// WALLIS AND FUTUNA
			,
			{ "Alo", "Hahake", "Hihifo", "Mua", "Sigave" }
			// WESTERN SAHARA
			,
			{ "al-'Ayun", "as-Samarah", "Bu Jaydur", "Wad-ad-�?ahab" }
			// YEMEN
			,
			{ "Abyan", "ad-Dali'", "Aden", "al-Bayda", "al-H?udaydah",
					"al-Jawf", "al-Mahrah", "al-Mahwit", "Amanah al-'Asmah",
					"Amran", "�?amar", "Hadramaut", "Hajjah", "Ibb", "Lahij",
					"Ma'rib", "Raymah", "Šabwah", "Sa'dah", "San'a", "Ta'izz" }
			// ZAMBIA
			,
			{ "Central", "Copperbelt", "Eastern", "Luapala", "Lusaka",
					"Northern", "North-Western", "Southern", "Western" },
			// ZIMBABWE,
			{ "Bulawayo", "Harare", "Manicaland", "Mashonaland Central",
					"Mashonaland East", "Mashonaland West", "Masvingo",
					"Matabeleland North", "Matabeleland South", "Midlands" } };

	/*
	 * private static String[] currencyCodes = new String[] { "USD ", "AFN ",
	 * "ALL ", "DZD ", "AOA ", "ARS ", "AMD ", "AWG ", "AUD ", "AZN ", "BSD ",
	 * "BHD ", "BDT ", "BBD ", "BYR ", "BZD ", "BMD ", "BTN ", "BOB ", "BAM ",
	 * "BWP ", "BRL ", "GBP ", "BND ", "BGN ", "BIF ", "KHR ", "CAD ", "CVE ",
	 * "KYD ", "CLP ", "CNY ", "COP ", "KMF ", "CDF ", "CDF ", "CRC ", "HRK ",
	 * "CUC ", "CZK ", "DJF ", "DOP ", "DOP ", "EGP ", "ERN ", "EEK ", "ETB ",
	 * "FKP ", "FJD ", "GMD ", "GEL ", "GHS ", "GIP ", "GTQ ", "GNF ", "GYD ",
	 * "HTG ", "HNL ", "HKD ", "HUF ", "ISK ", "INR", "IDR ", "IRR ", "IQD ",
	 * "ILS ", "JMD ", "JPY ", "JOD ", "KZT ", "KES ", "KPW ", "KRW ", "KWD ",
	 * "KGS ", "LAK ", "LVL ", "LBP", "LSL ", "LRD ", "LYD", "LTL ", "MKD",
	 * "MWK", "MYR ", "MVR ", "MRO ", "MUR ", "MXN ", "MDL ", "MNT ", "MAD ",
	 * "MZN ", "NAD ", "NPR ", "ANG ", "NZD ", "NIO ", "NGN ", "NOK ", "OMR",
	 * "PKR ", "PAB ", "PGK", "PYG ", "PEN ", "PHP", "QAR ", "RON ", "RUB ",
	 * "RWF", "SHP", "WST ", "STD ", "SAR", "RSD ", "SCR ", "SLL ", "SGD ",
	 * "SKK ", "SBD ", "SOS ", "ZAR ", "LKR ", "SRD", "SZL ", "CHF ", "SYP ",
	 * "TWD", "TJS ", "TZS ", "THB ", "TOP ", "TTD ", "TND ", "TMT ", "TRY ",
	 * "UGX", "UAH ", "AED ", "UYU ", "UZS ", "VUV", "VEF ", "VND", "YER",
	 * "ZMK ", "ZWD", "DKK" };
	 */

	private static ClientCurrency[] currencies = new ClientCurrency[] {
			new ClientCurrency("United States Dollar", "USD", "$"),
			new ClientCurrency("Afghan Afghani", "AFN", "؋"),
			new ClientCurrency("Albanian Lek", "ALL", "Lek"),
			new ClientCurrency("Angolan Kwanza", "AOA", "Kz"),
			new ClientCurrency("Argentine Peso", "ARS", "$"),
			new ClientCurrency("Armenian Dram", "AMD", "Դ"),
			new ClientCurrency("Aruban Guilder", "AWG", "Æ’"),
			new ClientCurrency("Australian Dollar", "AUD", "$"),
			new ClientCurrency("Azerbaijani Manat", "AZN", "ман"),
			new ClientCurrency("Bahamian Dollar", "BSD", "B$"),
			new ClientCurrency("Bahraini Dinar", "BHD", "BD"),
			new ClientCurrency("Bangladeshi Taka", "BDT", "Tk"),
			new ClientCurrency("Barbadian Dollar", "BBD", "Bds$"),
			new ClientCurrency("Belarusian Ruble", "BYR", "Br"),
			new ClientCurrency("Belize Dollar", "BZD", "BZ$"),
			new ClientCurrency("Bhutanese Ngultrum", "BTN", "Nu."),
			new ClientCurrency("Bolivian Boliviano", "BOB", "Bs."),
			new ClientCurrency("Bosnia Mark", "BAM", "KM"),
			new ClientCurrency("Botswana Pula", "BWP", "P"),
			new ClientCurrency("Brazilian Real", "BRL", "R$"),
			new ClientCurrency("British Pound", "GBP", "£"),
			new ClientCurrency("Brunei Dollar", "BND", "B$"),
			new ClientCurrency("Bulgarian Lev", "BGN", "лв"),
			new ClientCurrency("Burundian Franc", "BIF", "Fbu"),
			new ClientCurrency("Cambodian Riel", "KHR", "f"),
			new ClientCurrency("Canadian Dollar", "CAD", "$"),
			new ClientCurrency("CapeVerdean Escudo", "CVE", "Esc"),
			new ClientCurrency("CaymanIslands Dollar", "KYD", "KY$"),
			new ClientCurrency("Chilean Peso", "CLP", "$"),
			new ClientCurrency("Chinese Yuan", "CNY", "¥"),
			new ClientCurrency("Colombian Peso", "COP", "Col$"),
			new ClientCurrency("Comorian Franc", "KMF", "CF"),
			new ClientCurrency("Congolese Franc", "CDF", "F"),
			new ClientCurrency("Congolese Franc", "CDF", "F"),
			new ClientCurrency("CostaRican Colón", "CRC", "₡"),
			new ClientCurrency("Croatian Kuna", "HRK", "kn"),
			new ClientCurrency("CubanConvertible Peso", "CUC", "$"),
			new ClientCurrency("Czech Koruna", "CZK", "K�?"),
			new ClientCurrency("Djiboutian Franc", "DJF", "Fdj"),
			new ClientCurrency("Dominican Peso", "DOP", "RD$"),
			new ClientCurrency("East Caribbean Dollar", "XCD", "EC$"),
			new ClientCurrency("Egyptian Pound", "EGP", "Â£"),
			new ClientCurrency("Eritrean Nakfa", "ERN", "Nfa"),
			new ClientCurrency("Estonian Kroon", "EEK", "KR"),
			new ClientCurrency("Ethiopian Birr", "ETB", "Br"),
			new ClientCurrency("FalklandIsland Pound", "FKP", "£"),
			new ClientCurrency("Fijian Dollar", "FJD", "FJ$"),
			new ClientCurrency("Gambian dalasi", "GMD", "D"),
			new ClientCurrency("Georgian Lari", "GEL", ""),
			new ClientCurrency("Ghanaian Cedi", "GHS", "¢"),
			new ClientCurrency("Gibraltar Pound", "GIP", "£"),
			new ClientCurrency("Guatemalan Quetzal", "GTQ", "Q"),
			new ClientCurrency("Guinean Franc", "GNF", "FG"),
			new ClientCurrency("Guyanese Dollar", "GYD", "GY$"),
			new ClientCurrency("Haitian Gourde", "HTG", "G"),
			new ClientCurrency("Honduran Lempira", "HNL", "L"),
			new ClientCurrency("HongKong Dollar", "HKD", "HK$"),
			new ClientCurrency("Hungarian Forint", "HUF", "Ft"),
			new ClientCurrency("Icelandic Króna", "ISK", "kr"),
			new ClientCurrency("Indian Rupee", "INR", "₹"),
			new ClientCurrency("Indonesian Rupiah", "IDR", "Rp"),
			new ClientCurrency("Iranian Rial", "IRR", "U+FDFC"),
			new ClientCurrency("Iraqi Dinar", "IQD", "dinar"),
			new ClientCurrency("Israeli NewSheqel", "ILS", "₪"),
			new ClientCurrency("Jamaican Dollar", "JMD", "J$"),
			new ClientCurrency("Japanese Yen", "JPY", "¥"),
			new ClientCurrency("Jordanian Dinar", "JOD", "dinar"),
			new ClientCurrency("Kazakhstani Tenge", "KZT", "T"),
			new ClientCurrency("Kenyan Shilling", "KES", "KSh"),
			new ClientCurrency("NorthKorean Won", "KPW", "W"),
			new ClientCurrency("SouthKorean Won", "KRW", "W"),
			new ClientCurrency("Kuwaiti Dinar", "KWD", "KWD"),
			new ClientCurrency("Kyrgyzstani Som", "KGS", "som "),
			new ClientCurrency("Lao Pound", "LAK", "KN"),
			new ClientCurrency("Latvian Lats", "LVL", "Ls"),
			new ClientCurrency("Lebanese Pound", "LBP", "£"),
			new ClientCurrency("Lesotho Loti", "LSL", "M"),
			new ClientCurrency("Liberian Dollar", "LRD", "L$"),
			new ClientCurrency("Libyan Dinar", "LYD", "LD"),
			new ClientCurrency("Lithuanian Litas", "LTL", "Lt"),
			new ClientCurrency("Macedonian Denar", "MKD", "denar"),
			new ClientCurrency("Malawian Kwacha", "MWK", "MK"),
			new ClientCurrency("Malaysian Ringgit", "MYR", "RM"),
			new ClientCurrency("Maldivian Rufiyaa", "MVR", "Rf"),
			new ClientCurrency("Mauritanian Ouguiya", "MRO", "UM"),
			new ClientCurrency("Mauritian Rupee", "MUR", "Rs"),
			new ClientCurrency("Mexican Peso", "MXN", "$"),
			new ClientCurrency("Moldovan Leu", "MDL", "leu"),
			new ClientCurrency("Mongolian Tugrik", "MNT", "₮"),
			new ClientCurrency("Moroccan Dirham", "MAD", "MAD"),
			new ClientCurrency("Mozambican Metical", "MZN", "MTn"),
			new ClientCurrency("Namibian Dollar", "NAD", "N$"),
			new ClientCurrency("Nepalese Rupee", "NPR", "Rs"),
			new ClientCurrency("NetherlandsAntillean Guilder", "ANG", "ƒ"),
			new ClientCurrency("NewZealand Dollar", "NZD", "NZ$"),
			new ClientCurrency("Nicaraguan Córdoba", "NIO", "C$"),
			new ClientCurrency("Nigerian Naira", "NGN", "₦"),
			new ClientCurrency("Norwegian Krone", "NOK", "kr"),
			new ClientCurrency("Omani Rial", "OMR", "rial"),
			new ClientCurrency("Pakistani Rupee", "PKR", "Rs."),
			new ClientCurrency("Panamanian Balboa", "PAB", "B./"),
			new ClientCurrency("PapuaNewGuinean Kina", "PGK", "K"),
			new ClientCurrency("Paraguayan Guaraní", "PYG", "Gs"),
			new ClientCurrency("PeruvianNuevo Sol", "PEN", "S/."),
			new ClientCurrency("Philippine Peso", "PHP", "₱"),
			new ClientCurrency("Polish złoty", "PLN", "zł"),
			new ClientCurrency("Qatari Riyal", "QAR", "QR"),
			new ClientCurrency("Romanian Leu", "RON", "L"),
			new ClientCurrency("Russian Ruble", "RUB", "R"),
			new ClientCurrency("Rwandan Franc", "RWF", "RF"),
			new ClientCurrency("SaintHelenian Pound", "SHP", "£"),
			new ClientCurrency("Samoan Tala", "WST", "WS$"),
			new ClientCurrency("SãoToméandPríncipe Dobra", "STD", "Db"),
			new ClientCurrency("Saudi Riyal", "SAR", "SR"),
			new ClientCurrency("Serbian Dinar", "RSD", "din."),
			new ClientCurrency("Seychellois Rupee", "SCR", "SR"),
			new ClientCurrency("SierraLeonean Leone", "SLL", "Le"),
			new ClientCurrency("Singapore Dollar", "SGD", "S$"),
			new ClientCurrency("Slovak Koruna", "SKK", ""),
			new ClientCurrency("SolomonIslands Dollar", "SBD", "SI$"),
			new ClientCurrency("Somali Shilling", "SOS", "Sh."),
			new ClientCurrency("SouthAfrican Rand", "ZAR", "R"),
			new ClientCurrency("SriLankan Rupee", "LKR", "Rs"),
			new ClientCurrency("Surinamese Dollar", "SRD", "$"),
			new ClientCurrency("Swazi Lilangeni", "SZL", "E"),
			new ClientCurrency("Swiss Franc", "CHF", "Fr."),
			new ClientCurrency("Syrian Pound", "SYP", "Â£S"),
			new ClientCurrency("TaiwaneseNew Dollar", "TWD", "NT$"),
			new ClientCurrency("Tajikistani Somoni", "TJS", "TJS"),
			new ClientCurrency("Tanzanian Shilling", "TZS", "x/y"),
			new ClientCurrency("Thai Baht", "THB", "฿"),
			new ClientCurrency("Tongan Pa?anga", "TOP", "T$"),
			new ClientCurrency("TrinidadandTobago Dollar", "TTD", "TT$"),
			new ClientCurrency("Tunisian Dinar", "TND", "DT"),
			new ClientCurrency("Turkmenistani Manat", "TMT", "m"),
			new ClientCurrency("Turkish Lira", "TRY", "YTL"),
			new ClientCurrency("Ugandan Shilling", "UGX", "USh"),
			new ClientCurrency("Ukrainian Hryvnia", "UAH", "₴"),
			new ClientCurrency("UnitedArabEmirates Dirham", "AED", "Dhs"),
			new ClientCurrency("Uruguayan Peso", "UYU", "$U"),
			new ClientCurrency("Uzbekistani Som", "UZS", ""),
			new ClientCurrency("Vanuatu Vatu", "VUV", "VT"),
			new ClientCurrency("VenezuelanBolívar Fuertes", "VEF", "Bs"),
			new ClientCurrency("Vietnamese Dong", "VND", "₫"),
			new ClientCurrency("Yemeni Rial", "YER", ""),
			new ClientCurrency("Zambian Kwacha", "ZMK", "ZK"),
			new ClientCurrency("Zimbabwean Dollar", "ZWD", "Z$"),
			new ClientCurrency("DanishKrone", "DKK", "Kr"),
			new ClientCurrency("Euro", "EUR", "€"),
			new ClientCurrency("Cyprus Pound", "CYP", "£"),
			new ClientCurrency("Ecuadorian sucre", "ECS", "S/."),
			new ClientCurrency("French Polynesia", "XPF", "FF"),
			new ClientCurrency("Swedish Krona", "SEK", "kr"),
			new ClientCurrency("Czech koruna", "CZK", "K�?"),
			new ClientCurrency("West African CFA franc", "XOF", "K�?"),
			new ClientCurrency("Macanese pataca", "MOP", "MOP$"),
			new ClientCurrency("Sudanese pound", "SDG", "SDP") };

	/*
	 * private static String[] currencyNames = new String[] {
	 * "United States  Dollar", "Afghan  Afghani", "Albanian  Lek",
	 * "Algerian  Dinar", "Angolan  Kwanza", "Argentine  Peso",
	 * "Armenian  Dram", "Aruban  Guilder", "Australian  Dollar",
	 * "Azerbaijani  Manat", "Bahamian  Dollar", "Bahraini  Dinar",
	 * "Bangladeshi  Taka", "Barbadian  Dollar", "Belarusian  Ruble",
	 * "Belize   Dollar", "Bermudian  Dollar", "Bhutanese  Ngultrum",
	 * "Bolivian  Boliviano", "Bosnia and Herzegovina Convertible  Mark",
	 * "Botswana  Pula", "Brazilian  Real", "British  Pound", "Brunei  Dollar",
	 * "Bulgarian  Lev", "Burundian  Franc", "Cambodian  Riel",
	 * "Canadian  Dollar", "Cape Verdean  Escudo", "Cayman Islands  Dollar",
	 * "Chilean  Peso", "Chinese  Yuan", "Colombian  Peso", "Comorian  Franc",
	 * "Congolese  Franc", "Congolese  Franc", "Costa Rican  ColÃ³n",
	 * "Croatian  Kuna", "Cuban Convertible  Peso", "Czech  Koruna",
	 * "Djiboutian  Franc", "Dominican  Peso", "Dominican  Peso",
	 * "Egyptian  Pound", "Eritrean  Nakfa", "Estonian  Kroon",
	 * "Ethiopian  Birr", "Falkland Island Pound", "Fijian  Dollar",
	 * "Gambian  dalasi", "Georgian  Lari", "Ghanaian  Cedi",
	 * "Gibraltar  Pound", "Guatemalan  Quetzal", "Guinean  Franc",
	 * "Guyanese  Dollar", "Haitian  Gourde", "Honduran  Lempira",
	 * "Hong Kong  Dollar", "Hungarian  Forint", "Icelandic  KrÃ³na",
	 * "Indian Rupee", "Indonesian  Rupiah", "Iranian  Rial", "Iraqi  Dinar",
	 * "Israeli  New Sheqel", "Jamaican  Dollar", "Japanese  Yen",
	 * "Jordanian  Dinar", "Kazakhstani  Tenge", "Kenyan  Shilling",
	 * "North Korean  Won", "South Korean  Won", "Kuwaiti  Dinar",
	 * "Kyrgyzstani  Som", "Lao  Pound", "Latvian Lats", "Lebanese  Pound",
	 * "Lesotho  Loti", "Liberian  Dollar", "Libyan  Dinar",
	 * "Lithuanian  Litas", "Macedonian  Denar", "Malawian  Kwacha",
	 * "Malaysian  Ringgit", "Maldivian  Rufiyaa", "Mauritanian  Ouguiya",
	 * "Mauritian  Rupee", "Mexican  Peso", "Moldovan  Leu",
	 * "Mongolian  Tugrik", "Moroccan  Dirham", "Mozambican Metical",
	 * "Namibian  Dollar", "Nepalese  Rupee", "Netherlands Antillean  Guilder",
	 * "New Zealand  Dollar", "Nicaraguan  CÃ³rdoba", "Nigerian  Naira",
	 * "Norwegian  Krone", "Omani  Rial", "Pakistani  Rupee",
	 * "Panamanian  Balboa", "Papua New Guinean  Kina", "Paraguayan  GuaranÃ­",
	 * "Peruvian Nuevo  Sol", "Philippine  Peso", "Qatari  Riyal",
	 * "Romanian  Leu", "Russian  Ruble", "Rwandan  Franc",
	 * "Saint Helenian  Pound", "Samoan  Tala",
	 * "SÃ£o TomÃ© and PrÃ­ncipe  Dobra", "Saudi  Riyal", "Serbian  Dinar",
	 * "Seychellois  Rupee", "Sierra Leonean  Leone", "Singapore  Dollar",
	 * "Slovak  Koruna", "Solomon Islands  Dollar", "Somali   Shilling",
	 * "South African  Rand", "Sri Lankan  Rupee", "Surinamese  Dollar",
	 * "Swazi  Lilangeni", "Swiss  Franc", "Syrian  Pound",
	 * "Taiwanese New  Dollar", "Tajikistani  Somoni", "Tanzanian  Shilling",
	 * "Thai  Baht", "Tongan  Pa?anga", "Trinidad and Tobago  Dollar",
	 * "Tunisian  Dinar", "Turkmenistani  Manat", "Turkish  Lira",
	 * "Ugandan  Shilling", "Ukrainian  Hryvnia",
	 * "United Arab Emirates  Dirham", "Uruguayan  Peso", "Uzbekistani  Som",
	 * "Vanuatu  Vatu", "Venezuelan BolÃ­var  Fuertes", "Vietnamese  Dong",
	 * "Yemeni  Rial", "Zambian  Kwacha", "Zimbabwean  Dollar", "Danish Krone"
	 * };
	 * 
	 * private static String[] currencySymbols = new String[] { "\u0024", "Afs",
	 * "Lek", "DA", "Kz", "\u0024", "", "Afl", "A$", "m", "B$", "BD", "৳",
	 * "Bds$", "Br", "BZ$", "BD$", "Nu.", "Bs.", "KM", "P", "R$", "£", "B$",
	 * "лв", "FBu", "f", "$", "Esc", "KY$", "$", "¥", "Col$", "CF", "F", "F",
	 * "₡", "kn", "$", "K�?", "Fdj", "RD$", "£", "Nfa", "KR", "Br", "£", "FJ$",
	 * "D", "GEL" };
	 */

	public static List<ClientLanguage> getLanguages() {
		List<ClientLanguage> languagesList = new ArrayList<ClientLanguage>();
		for (int i = 0; i < languages.length; i++) {
			languagesList.add(languages[i]);
		}
		return languagesList;
	}

	public static String[] getCountries() {
		return countries;
	}

	public static List<String> getCountriesAsList() {
		return Arrays.asList(countries);
	}

	public static String[] getTimeZones() {
		return timezones;
	}

	public static List<String> getTimeZonesAsList() {
		return Arrays.asList(timezones);
	}

	public static List<String> getMonthNames() {
		String[] monthNames = new String[] { "January", "February", "March",
				"April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		List<String> months = new ArrayList<String>();
		for (int i = 0; i < monthNames.length; i++) {
			months.add(monthNames[i]);
		}
		return months;
	}

	public static String[] getStatesForCountry(String country) {
		return states[getCountriesAsList().indexOf(country)];
	}

	public static List<String> getStatesAsListForCountry(String country) {
		String[] state = states[getCountriesAsList().indexOf(country)];
		return Arrays.asList(state);
	}

	public static List<ClientCurrency> getCurrencies(
			List<ClientCurrency> existCurrencies) {
		List<ClientCurrency> returnedList = new ArrayList<ClientCurrency>();
		returnedList.addAll(existCurrencies);
		for (int i = 0; i < currencies.length; i++) {
			boolean isExists = false;
			ClientCurrency clientCurrency = currencies[i];
			for (ClientCurrency currency : existCurrencies) {
				if (clientCurrency.getFormalName().endsWith(
						currency.getFormalName())) {
					isExists = true;
					break;
				}
			}
			if (!isExists) {
				returnedList.add(currencies[i]);
			}
		}

		return returnedList;
	}

	public static ClientCurrency getCurrency(String formalName) {
		int index = 0;
		for (int i = 0; i < currencies.length; i++) {
			if (currencies[i].getFormalName().equals(formalName)) {
				index = i;
				break;
			}
		}
		return currencies[index];
	}

	public static String getCurrencySymbol(String primaryCurrency) {
		int index = 0;
		for (int i = 0; i < currencies.length; i++) {
			if (currencies[i].getFormalName().equals(primaryCurrency)) {
				index = i;
				break;
			}
		}
		return currencies[index].getSymbol();
	}

	public static void getCurrencies() {
	}

	/**
	 * 
	 * @param amount
	 * @return
	 */
	public static String getAmountCurrencySymbol(String amount) {
		for (int i = 0; i < currencies.length; i++) {
			if (amount.contains(currencies[i].getSymbol())) {
				return currencies[i].getSymbol();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param amount
	 * @return
	 */
	public static String getAmountCurrencyFormalName(String amount) {
		for (int i = 0; i < currencies.length; i++) {
			if (amount.contains(currencies[i].getFormalName())) {
				return currencies[i].getFormalName();
			}
		}
		return null;
	}
}
