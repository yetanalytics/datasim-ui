(ns datasim-ui.timezone)

(def timezones [{:offset "Z" :zone "Africa/Abidjan"}
                    {:offset "Z" :zone "Africa/Accra"}
                    {:offset "+03:00" :zone "Africa/Addis_Ababa"}
                    {:offset "+01:00" :zone "Africa/Algiers"}
                    {:offset "+03:00" :zone "Africa/Asmara"}
                    {:offset "+03:00" :zone "Africa/Asmera"}
                    {:offset "Z" :zone "Africa/Bamako"}
                    {:offset "+01:00" :zone "Africa/Bangui"}
                    {:offset "Z" :zone "Africa/Banjul"}
                    {:offset "Z" :zone "Africa/Bissau"}
                    {:offset "+02:00" :zone "Africa/Blantyre"}
                    {:offset "+01:00" :zone "Africa/Brazzaville"}
                    {:offset "+02:00" :zone "Africa/Bujumbura"}
                    {:offset "+02:00" :zone "Africa/Cairo"}
                    {:offset "Z" :zone "Africa/Casablanca"}
                    {:offset "+01:00" :zone "Africa/Ceuta"}
                    {:offset "Z" :zone "Africa/Conakry"}
                    {:offset "Z" :zone "Africa/Dakar"}
                    {:offset "+03:00" :zone "Africa/Dar_es_Salaam"}
                    {:offset "+03:00" :zone "Africa/Djibouti"}
                    {:offset "+01:00" :zone "Africa/Douala"}
                    {:offset "Z" :zone "Africa/El_Aaiun"}
                    {:offset "Z" :zone "Africa/Freetown"}
                    {:offset "+02:00" :zone "Africa/Gaborone"}
                    {:offset "+02:00" :zone "Africa/Harare"}
                    {:offset "+02:00" :zone "Africa/Johannesburg"}
                    {:offset "+03:00" :zone "Africa/Juba"}
                    {:offset "+03:00" :zone "Africa/Kampala"}
                    {:offset "+03:00" :zone "Africa/Khartoum"}
                    {:offset "+02:00" :zone "Africa/Kigali"}
                    {:offset "+01:00" :zone "Africa/Kinshasa"}
                    {:offset "+01:00" :zone "Africa/Lagos"}
                    {:offset "+01:00" :zone "Africa/Libreville"}
                    {:offset "Z" :zone "Africa/Lome"}
                    {:offset "+01:00" :zone "Africa/Luanda"}
                    {:offset "+02:00" :zone "Africa/Lubumbashi"}
                    {:offset "+02:00" :zone "Africa/Lusaka"}
                    {:offset "+01:00" :zone "Africa/Malabo"}
                    {:offset "+02:00" :zone "Africa/Maputo"}
                    {:offset "+02:00" :zone "Africa/Maseru"}
                    {:offset "+02:00" :zone "Africa/Mbabane"}
                    {:offset "+03:00" :zone "Africa/Mogadishu"}
                    {:offset "Z" :zone "Africa/Monrovia"}
                    {:offset "+03:00" :zone "Africa/Nairobi"}
                    {:offset "+01:00" :zone "Africa/Ndjamena"}
                    {:offset "+01:00" :zone "Africa/Niamey"}
                    {:offset "Z" :zone "Africa/Nouakchott"}
                    {:offset "Z" :zone "Africa/Ouagadougou"}
                    {:offset "+01:00" :zone "Africa/Porto-Novo"}
                    {:offset "Z" :zone "Africa/Sao_Tome"}
                    {:offset "Z" :zone "Africa/Timbuktu"}
                    {:offset "+02:00" :zone "Africa/Tripoli"}
                    {:offset "+01:00" :zone "Africa/Tunis"}
                    {:offset "+02:00" :zone "Africa/Windhoek"}
                    {:offset "-10:00" :zone "America/Adak"}
                    {:offset "-09:00" :zone "America/Anchorage"}
                    {:offset "-04:00" :zone "America/Anguilla"}
                    {:offset "-04:00" :zone "America/Antigua"}
                    {:offset "-03:00" :zone "America/Araguaina"}
                    {:offset "-03:00" :zone "America/Argentina/Buenos_Aires"}
                    {:offset "-03:00" :zone "America/Argentina/Catamarca"}
                    {:offset "-03:00" :zone "America/Argentina/ComodRivadavia"}
                    {:offset "-03:00" :zone "America/Argentina/Cordoba"}
                    {:offset "-03:00" :zone "America/Argentina/Jujuy"}
                    {:offset "-03:00" :zone "America/Argentina/La_Rioja"}
                    {:offset "-03:00" :zone "America/Argentina/Mendoza"}
                    {:offset "-03:00" :zone "America/Argentina/Rio_Gallegos"}
                    {:offset "-03:00" :zone "America/Argentina/Salta"}
                    {:offset "-03:00" :zone "America/Argentina/San_Juan"}
                    {:offset "-03:00" :zone "America/Argentina/San_Luis"}
                    {:offset "-03:00" :zone "America/Argentina/Tucuman"}
                    {:offset "-03:00" :zone "America/Argentina/Ushuaia"}
                    {:offset "-04:00" :zone "America/Aruba"}
                    {:offset "-03:00" :zone "America/Asuncion"}
                    {:offset "-05:00" :zone "America/Atikokan"}
                    {:offset "-10:00" :zone "America/Atka"}
                    {:offset "-03:00" :zone "America/Bahia"}
                    {:offset "-06:00" :zone "America/Bahia_Banderas"}
                    {:offset "-04:00" :zone "America/Barbados"}
                    {:offset "-03:00" :zone "America/Belem"}
                    {:offset "-06:00" :zone "America/Belize"}
                    {:offset "-04:00" :zone "America/Blanc-Sablon"}
                    {:offset "-04:00" :zone "America/Boa_Vista"}
                    {:offset "-05:00" :zone "America/Bogota"}
                    {:offset "-07:00" :zone "America/Boise"}
                    {:offset "-03:00" :zone "America/Buenos_Aires"}
                    {:offset "-07:00" :zone "America/Cambridge_Bay"}
                    {:offset "-03:00" :zone "America/Campo_Grande"}
                    {:offset "-05:00" :zone "America/Cancun"}
                    {:offset "-04:00" :zone "America/Caracas"}
                    {:offset "-03:00" :zone "America/Catamarca"}
                    {:offset "-03:00" :zone "America/Cayenne"}
                    {:offset "-05:00" :zone "America/Cayman"}
                    {:offset "-06:00" :zone "America/Chicago"}
                    {:offset "-07:00" :zone "America/Chihuahua"}
                    {:offset "-05:00" :zone "America/Coral_Harbour"}
                    {:offset "-03:00" :zone "America/Cordoba"}
                    {:offset "-06:00" :zone "America/Costa_Rica"}
                    {:offset "-07:00" :zone "America/Creston"}
                    {:offset "-03:00" :zone "America/Cuiaba"}
                    {:offset "-04:00" :zone "America/Curacao"}
                    {:offset "Z" :zone "America/Danmarkshavn"}
                    {:offset "-08:00" :zone "America/Dawson"}
                    {:offset "-07:00" :zone "America/Dawson_Creek"}
                    {:offset "-07:00" :zone "America/Denver"}
                    {:offset "-05:00" :zone "America/Detroit"}
                    {:offset "-04:00" :zone "America/Dominica"}
                    {:offset "-07:00" :zone "America/Edmonton"}
                    {:offset "-05:00" :zone "America/Eirunepe"}
                    {:offset "-06:00" :zone "America/El_Salvador"}
                    {:offset "-08:00" :zone "America/Ensenada"}
                    {:offset "-07:00" :zone "America/Fort_Nelson"}
                    {:offset "-05:00" :zone "America/Fort_Wayne"}
                    {:offset "-03:00" :zone "America/Fortaleza"}
                    {:offset "-04:00" :zone "America/Glace_Bay"}
                    {:offset "-03:00" :zone "America/Godthab"}
                    {:offset "-04:00" :zone "America/Goose_Bay"}
                    {:offset "-04:00" :zone "America/Grand_Turk"}
                    {:offset "-04:00" :zone "America/Grenada"}
                    {:offset "-04:00" :zone "America/Guadeloupe"}
                    {:offset "-06:00" :zone "America/Guatemala"}
                    {:offset "-05:00" :zone "America/Guayaquil"}
                    {:offset "-04:00" :zone "America/Guyana"}
                    {:offset "-04:00" :zone "America/Halifax"}
                    {:offset "-05:00" :zone "America/Havana"}
                    {:offset "-07:00" :zone "America/Hermosillo"}
                    {:offset "-05:00" :zone "America/Indiana/Indianapolis"}
                    {:offset "-06:00" :zone "America/Indiana/Knox"}
                    {:offset "-05:00" :zone "America/Indiana/Marengo"}
                    {:offset "-05:00" :zone "America/Indiana/Petersburg"}
                    {:offset "-06:00" :zone "America/Indiana/Tell_City"}
                    {:offset "-05:00" :zone "America/Indiana/Vevay"}
                    {:offset "-05:00" :zone "America/Indiana/Vincennes"}
                    {:offset "-05:00" :zone "America/Indiana/Winamac"}
                    {:offset "-05:00" :zone "America/Indianapolis"}
                    {:offset "-07:00" :zone "America/Inuvik"}
                    {:offset "-05:00" :zone "America/Iqaluit"}
                    {:offset "-05:00" :zone "America/Jamaica"}
                    {:offset "-03:00" :zone "America/Jujuy"}
                    {:offset "-09:00" :zone "America/Juneau"}
                    {:offset "-05:00" :zone "America/Kentucky/Louisville"}
                    {:offset "-05:00" :zone "America/Kentucky/Monticello"}
                    {:offset "-06:00" :zone "America/Knox_IN"}
                    {:offset "-04:00" :zone "America/Kralendijk"}
                    {:offset "-04:00" :zone "America/La_Paz"}
                    {:offset "-05:00" :zone "America/Lima"}
                    {:offset "-08:00" :zone "America/Los_Angeles"}
                    {:offset "-05:00" :zone "America/Louisville"}
                    {:offset "-04:00" :zone "America/Lower_Princes"}
                    {:offset "-03:00" :zone "America/Maceio"}
                    {:offset "-06:00" :zone "America/Managua"}
                    {:offset "-04:00" :zone "America/Manaus"}
                    {:offset "-04:00" :zone "America/Marigot"}
                    {:offset "-04:00" :zone "America/Martinique"}
                    {:offset "-06:00" :zone "America/Matamoros"}
                    {:offset "-07:00" :zone "America/Mazatlan"}
                    {:offset "-03:00" :zone "America/Mendoza"}
                    {:offset "-06:00" :zone "America/Menominee"}
                    {:offset "-06:00" :zone "America/Merida"}
                    {:offset "-09:00" :zone "America/Metlakatla"}
                    {:offset "-06:00" :zone "America/Mexico_City"}
                    {:offset "-03:00" :zone "America/Miquelon"}
                    {:offset "-04:00" :zone "America/Moncton"}
                    {:offset "-06:00" :zone "America/Monterrey"}
                    {:offset "-03:00" :zone "America/Montevideo"}
                    {:offset "-05:00" :zone "America/Montreal"}
                    {:offset "-04:00" :zone "America/Montserrat"}
                    {:offset "-05:00" :zone "America/Nassau"}
                    {:offset "-05:00" :zone "America/New_York"}
                    {:offset "-05:00" :zone "America/Nipigon"}
                    {:offset "-09:00" :zone "America/Nome"}
                    {:offset "-02:00" :zone "America/Noronha"}
                    {:offset "-06:00" :zone "America/North_Dakota/Beulah"}
                    {:offset "-06:00" :zone "America/North_Dakota/Center"}
                    {:offset "-06:00" :zone "America/North_Dakota/New_Salem"}
                    {:offset "-07:00" :zone "America/Ojinaga"}
                    {:offset "-05:00" :zone "America/Panama"}
                    {:offset "-05:00" :zone "America/Pangnirtung"}
                    {:offset "-03:00" :zone "America/Paramaribo"}
                    {:offset "-07:00" :zone "America/Phoenix"}
                    {:offset "-05:00" :zone "America/Port-au-Prince"}
                    {:offset "-04:00" :zone "America/Port_of_Spain"}
                    {:offset "-05:00" :zone "America/Porto_Acre"}
                    {:offset "-04:00" :zone "America/Porto_Velho"}
                    {:offset "-04:00" :zone "America/Puerto_Rico"}
                    {:offset "-03:00" :zone "America/Punta_Arenas"}
                    {:offset "-06:00" :zone "America/Rainy_River"}
                    {:offset "-06:00" :zone "America/Rankin_Inlet"}
                    {:offset "-03:00" :zone "America/Recife"}
                    {:offset "-06:00" :zone "America/Regina"}
                    {:offset "-06:00" :zone "America/Resolute"}
                    {:offset "-05:00" :zone "America/Rio_Branco"}
                    {:offset "-03:00" :zone "America/Rosario"}
                    {:offset "-08:00" :zone "America/Santa_Isabel"}
                    {:offset "-03:00" :zone "America/Santarem"}
                    {:offset "-03:00" :zone "America/Santiago"}
                    {:offset "-04:00" :zone "America/Santo_Domingo"}
                    {:offset "-02:00" :zone "America/Sao_Paulo"}
                    {:offset "-01:00" :zone "America/Scoresbysund"}
                    {:offset "-07:00" :zone "America/Shiprock"}
                    {:offset "-09:00" :zone "America/Sitka"}
                    {:offset "-04:00" :zone "America/St_Barthelemy"}
                    {:offset "-03:30" :zone "America/St_Johns"}
                    {:offset "-04:00" :zone "America/St_Kitts"}
                    {:offset "-04:00" :zone "America/St_Lucia"}
                    {:offset "-04:00" :zone "America/St_Thomas"}
                    {:offset "-04:00" :zone "America/St_Vincent"}
                    {:offset "-06:00" :zone "America/Swift_Current"}
                    {:offset "-06:00" :zone "America/Tegucigalpa"}
                    {:offset "-04:00" :zone "America/Thule"}
                    {:offset "-05:00" :zone "America/Thunder_Bay"}
                    {:offset "-08:00" :zone "America/Tijuana"}
                    {:offset "-05:00" :zone "America/Toronto"}
                    {:offset "-04:00" :zone "America/Tortola"}
                    {:offset "-08:00" :zone "America/Vancouver"}
                    {:offset "-04:00" :zone "America/Virgin"}
                    {:offset "-08:00" :zone "America/Whitehorse"}
                    {:offset "-06:00" :zone "America/Winnipeg"}
                    {:offset "-09:00" :zone "America/Yakutat"}
                    {:offset "-07:00" :zone "America/Yellowknife"}
                    {:offset "+11:00" :zone "Antarctica/Casey"}
                    {:offset "+07:00" :zone "Antarctica/Davis"}
                    {:offset "+10:00" :zone "Antarctica/DumontDUrville"}
                    {:offset "+11:00" :zone "Antarctica/Macquarie"}
                    {:offset "+05:00" :zone "Antarctica/Mawson"}
                    {:offset "+13:00" :zone "Antarctica/McMurdo"}
                    {:offset "-03:00" :zone "Antarctica/Palmer"}
                    {:offset "-03:00" :zone "Antarctica/Rothera"}
                    {:offset "+13:00" :zone "Antarctica/South_Pole"}
                    {:offset "+03:00" :zone "Antarctica/Syowa"}
                    {:offset "Z" :zone "Antarctica/Troll"}
                    {:offset "+06:00" :zone "Antarctica/Vostok"}
                    {:offset "+01:00" :zone "Arctic/Longyearbyen"}
                    {:offset "+03:00" :zone "Asia/Aden"}
                    {:offset "+06:00" :zone "Asia/Almaty"}
                    {:offset "+02:00" :zone "Asia/Amman"}
                    {:offset "+12:00" :zone "Asia/Anadyr"}
                    {:offset "+05:00" :zone "Asia/Aqtau"}
                    {:offset "+05:00" :zone "Asia/Aqtobe"}
                    {:offset "+05:00" :zone "Asia/Ashgabat"}
                    {:offset "+05:00" :zone "Asia/Ashkhabad"}
                    {:offset "+05:00" :zone "Asia/Atyrau"}
                    {:offset "+03:00" :zone "Asia/Baghdad"}
                    {:offset "+03:00" :zone "Asia/Bahrain"}
                    {:offset "+04:00" :zone "Asia/Baku"}
                    {:offset "+07:00" :zone "Asia/Bangkok"}
                    {:offset "+07:00" :zone "Asia/Barnaul"}
                    {:offset "+02:00" :zone "Asia/Beirut"}
                    {:offset "+06:00" :zone "Asia/Bishkek"}
                    {:offset "+08:00" :zone "Asia/Brunei"}
                    {:offset "+05:30" :zone "Asia/Calcutta"}
                    {:offset "+09:00" :zone "Asia/Chita"}
                    {:offset "+08:00" :zone "Asia/Choibalsan"}
                    {:offset "+08:00" :zone "Asia/Chongqing"}
                    {:offset "+08:00" :zone "Asia/Chungking"}
                    {:offset "+05:30" :zone "Asia/Colombo"}
                    {:offset "+06:00" :zone "Asia/Dacca"}
                    {:offset "+02:00" :zone "Asia/Damascus"}
                    {:offset "+06:00" :zone "Asia/Dhaka"}
                    {:offset "+09:00" :zone "Asia/Dili"}
                    {:offset "+04:00" :zone "Asia/Dubai"}
                    {:offset "+05:00" :zone "Asia/Dushanbe"}
                    {:offset "+03:00" :zone "Asia/Famagusta"}
                    {:offset "+02:00" :zone "Asia/Gaza"}
                    {:offset "+08:00" :zone "Asia/Harbin"}
                    {:offset "+02:00" :zone "Asia/Hebron"}
                    {:offset "+07:00" :zone "Asia/Ho_Chi_Minh"}
                    {:offset "+08:00" :zone "Asia/Hong_Kong"}
                    {:offset "+07:00" :zone "Asia/Hovd"}
                    {:offset "+08:00" :zone "Asia/Irkutsk"}
                    {:offset "+03:00" :zone "Asia/Istanbul"}
                    {:offset "+07:00" :zone "Asia/Jakarta"}
                    {:offset "+09:00" :zone "Asia/Jayapura"}
                    {:offset "+02:00" :zone "Asia/Jerusalem"}
                    {:offset "+04:30" :zone "Asia/Kabul"}
                    {:offset "+12:00" :zone "Asia/Kamchatka"}
                    {:offset "+05:00" :zone "Asia/Karachi"}
                    {:offset "+06:00" :zone "Asia/Kashgar"}
                    {:offset "+05:45" :zone "Asia/Kathmandu"}
                    {:offset "+05:45" :zone "Asia/Katmandu"}
                    {:offset "+09:00" :zone "Asia/Khandyga"}
                    {:offset "+05:30" :zone "Asia/Kolkata"}
                    {:offset "+07:00" :zone "Asia/Krasnoyarsk"}
                    {:offset "+08:00" :zone "Asia/Kuala_Lumpur"}
                    {:offset "+08:00" :zone "Asia/Kuching"}
                    {:offset "+03:00" :zone "Asia/Kuwait"}
                    {:offset "+08:00" :zone "Asia/Macao"}
                    {:offset "+08:00" :zone "Asia/Macau"}
                    {:offset "+11:00" :zone "Asia/Magadan"}
                    {:offset "+08:00" :zone "Asia/Makassar"}
                    {:offset "+08:00" :zone "Asia/Manila"}
                    {:offset "+04:00" :zone "Asia/Muscat"}
                    {:offset "+02:00" :zone "Asia/Nicosia"}
                    {:offset "+07:00" :zone "Asia/Novokuznetsk"}
                    {:offset "+07:00" :zone "Asia/Novosibirsk"}
                    {:offset "+06:00" :zone "Asia/Omsk"}
                    {:offset "+05:00" :zone "Asia/Oral"}
                    {:offset "+07:00" :zone "Asia/Phnom_Penh"}
                    {:offset "+07:00" :zone "Asia/Pontianak"}
                    {:offset "+08:30" :zone "Asia/Pyongyang"}
                    {:offset "+03:00" :zone "Asia/Qatar"}
                    {:offset "+06:00" :zone "Asia/Qyzylorda"}
                    {:offset "+06:30" :zone "Asia/Rangoon"}
                    {:offset "+03:00" :zone "Asia/Riyadh"}
                    {:offset "+07:00" :zone "Asia/Saigon"}
                    {:offset "+11:00" :zone "Asia/Sakhalin"}
                    {:offset "+05:00" :zone "Asia/Samarkand"}
                    {:offset "+09:00" :zone "Asia/Seoul"}
                    {:offset "+08:00" :zone "Asia/Shanghai"}
                    {:offset "+08:00" :zone "Asia/Singapore"}
                    {:offset "+11:00" :zone "Asia/Srednekolymsk"}
                    {:offset "+08:00" :zone "Asia/Taipei"}
                    {:offset "+05:00" :zone "Asia/Tashkent"}
                    {:offset "+04:00" :zone "Asia/Tbilisi"}
                    {:offset "+03:30" :zone "Asia/Tehran"}
                    {:offset "+02:00" :zone "Asia/Tel_Aviv"}
                    {:offset "+06:00" :zone "Asia/Thimbu"}
                    {:offset "+06:00" :zone "Asia/Thimphu"}
                    {:offset "+09:00" :zone "Asia/Tokyo"}
                    {:offset "+07:00" :zone "Asia/Tomsk"}
                    {:offset "+08:00" :zone "Asia/Ujung_Pandang"}
                    {:offset "+08:00" :zone "Asia/Ulaanbaatar"}
                    {:offset "+08:00" :zone "Asia/Ulan_Bator"}
                    {:offset "+06:00" :zone "Asia/Urumqi"}
                    {:offset "+10:00" :zone "Asia/Ust-Nera"}
                    {:offset "+07:00" :zone "Asia/Vientiane"}
                    {:offset "+10:00" :zone "Asia/Vladivostok"}
                    {:offset "+09:00" :zone "Asia/Yakutsk"}
                    {:offset "+06:30" :zone "Asia/Yangon"}
                    {:offset "+05:00" :zone "Asia/Yekaterinburg"}
                    {:offset "+04:00" :zone "Asia/Yerevan"}
                    {:offset "-01:00" :zone "Atlantic/Azores"}
                    {:offset "-04:00" :zone "Atlantic/Bermuda"}
                    {:offset "Z" :zone "Atlantic/Canary"}
                    {:offset "-01:00" :zone "Atlantic/Cape_Verde"}
                    {:offset "Z" :zone "Atlantic/Faeroe"}
                    {:offset "Z" :zone "Atlantic/Faroe"}
                    {:offset "+01:00" :zone "Atlantic/Jan_Mayen"}
                    {:offset "Z" :zone "Atlantic/Madeira"}
                    {:offset "Z" :zone "Atlantic/Reykjavik"}
                    {:offset "-02:00" :zone "Atlantic/South_Georgia"}
                    {:offset "Z" :zone "Atlantic/St_Helena"}
                    {:offset "-03:00" :zone "Atlantic/Stanley"}
                    {:offset "+11:00" :zone "Australia/ACT"}
                    {:offset "+10:30" :zone "Australia/Adelaide"}
                    {:offset "+10:00" :zone "Australia/Brisbane"}
                    {:offset "+10:30" :zone "Australia/Broken_Hill"}
                    {:offset "+11:00" :zone "Australia/Canberra"}
                    {:offset "+11:00" :zone "Australia/Currie"}
                    {:offset "+09:30" :zone "Australia/Darwin"}
                    {:offset "+08:45" :zone "Australia/Eucla"}
                    {:offset "+11:00" :zone "Australia/Hobart"}
                    {:offset "+11:00" :zone "Australia/LHI"}
                    {:offset "+10:00" :zone "Australia/Lindeman"}
                    {:offset "+11:00" :zone "Australia/Lord_Howe"}
                    {:offset "+11:00" :zone "Australia/Melbourne"}
                    {:offset "+11:00" :zone "Australia/NSW"}
                    {:offset "+09:30" :zone "Australia/North"}
                    {:offset "+08:00" :zone "Australia/Perth"}
                    {:offset "+10:00" :zone "Australia/Queensland"}
                    {:offset "+10:30" :zone "Australia/South"}
                    {:offset "+11:00" :zone "Australia/Sydney"}
                    {:offset "+11:00" :zone "Australia/Tasmania"}
                    {:offset "+11:00" :zone "Australia/Victoria"}
                    {:offset "+08:00" :zone "Australia/West"}
                    {:offset "+10:30" :zone "Australia/Yancowinna"}
                    {:offset "-05:00" :zone "Brazil/Acre"}
                    {:offset "-02:00" :zone "Brazil/DeNoronha"}
                    {:offset "-02:00" :zone "Brazil/East"}
                    {:offset "-04:00" :zone "Brazil/West"}
                    {:offset "+01:00" :zone "CET"}
                    {:offset "-06:00" :zone "CST6CDT"}
                    {:offset "-04:00" :zone "Canada/Atlantic"}
                    {:offset "-06:00" :zone "Canada/Central"}
                    {:offset "-06:00" :zone "Canada/East-Saskatchewan"}
                    {:offset "-05:00" :zone "Canada/Eastern"}
                    {:offset "-07:00" :zone "Canada/Mountain"}
                    {:offset "-03:30" :zone "Canada/Newfoundland"}
                    {:offset "-08:00" :zone "Canada/Pacific"}
                    {:offset "-06:00" :zone "Canada/Saskatchewan"}
                    {:offset "-08:00" :zone "Canada/Yukon"}
                    {:offset "-03:00" :zone "Chile/Continental"}
                    {:offset "-05:00" :zone "Chile/EasterIsland"}
                    {:offset "-05:00" :zone "Cuba"}
                    {:offset "+02:00" :zone "EET"}
                    {:offset "-05:00" :zone "EST5EDT"}
                    {:offset "+02:00" :zone "Egypt"}
                    {:offset "Z" :zone "Eire"}
                    {:offset "Z" :zone "Etc/GMT"}
                    {:offset "Z" :zone "Etc/GMT+0"}
                    {:offset "-01:00" :zone "Etc/GMT+1"}
                    {:offset "-10:00" :zone "Etc/GMT+10"}
                    {:offset "-11:00" :zone "Etc/GMT+11"}
                    {:offset "-12:00" :zone "Etc/GMT+12"}
                    {:offset "-02:00" :zone "Etc/GMT+2"}
                    {:offset "-03:00" :zone "Etc/GMT+3"}
                    {:offset "-04:00" :zone "Etc/GMT+4"}
                    {:offset "-05:00" :zone "Etc/GMT+5"}
                    {:offset "-06:00" :zone "Etc/GMT+6"}
                    {:offset "-07:00" :zone "Etc/GMT+7"}
                    {:offset "-08:00" :zone "Etc/GMT+8"}
                    {:offset "-09:00" :zone "Etc/GMT+9"}
                    {:offset "Z" :zone "Etc/GMT-0"}
                    {:offset "+01:00" :zone "Etc/GMT-1"}
                    {:offset "+10:00" :zone "Etc/GMT-10"}
                    {:offset "+11:00" :zone "Etc/GMT-11"}
                    {:offset "+12:00" :zone "Etc/GMT-12"}
                    {:offset "+13:00" :zone "Etc/GMT-13"}
                    {:offset "+14:00" :zone "Etc/GMT-14"}
                    {:offset "+02:00" :zone "Etc/GMT-2"}
                    {:offset "+03:00" :zone "Etc/GMT-3"}
                    {:offset "+04:00" :zone "Etc/GMT-4"}
                    {:offset "+05:00" :zone "Etc/GMT-5"}
                    {:offset "+06:00" :zone "Etc/GMT-6"}
                    {:offset "+07:00" :zone "Etc/GMT-7"}
                    {:offset "+08:00" :zone "Etc/GMT-8"}
                    {:offset "+09:00" :zone "Etc/GMT-9"}
                    {:offset "Z" :zone "Etc/GMT0"}
                    {:offset "Z" :zone "Etc/Greenwich"}
                    {:offset "Z" :zone "Etc/UCT"}
                    {:offset "Z" :zone "Etc/UTC"}
                    {:offset "Z" :zone "Etc/Universal"}
                    {:offset "Z" :zone "Etc/Zulu"}
                    {:offset "+01:00" :zone "Europe/Amsterdam"}
                    {:offset "+01:00" :zone "Europe/Andorra"}
                    {:offset "+04:00" :zone "Europe/Astrakhan"}
                    {:offset "+02:00" :zone "Europe/Athens"}
                    {:offset "Z" :zone "Europe/Belfast"}
                    {:offset "+01:00" :zone "Europe/Belgrade"}
                    {:offset "+01:00" :zone "Europe/Berlin"}
                    {:offset "+01:00" :zone "Europe/Bratislava"}
                    {:offset "+01:00" :zone "Europe/Brussels"}
                    {:offset "+02:00" :zone "Europe/Bucharest"}
                    {:offset "+01:00" :zone "Europe/Budapest"}
                    {:offset "+01:00" :zone "Europe/Busingen"}
                    {:offset "+02:00" :zone "Europe/Chisinau"}
                    {:offset "+01:00" :zone "Europe/Copenhagen"}
                    {:offset "Z" :zone "Europe/Dublin"}
                    {:offset "+01:00" :zone "Europe/Gibraltar"}
                    {:offset "Z" :zone "Europe/Guernsey"}
                    {:offset "+02:00" :zone "Europe/Helsinki"}
                    {:offset "Z" :zone "Europe/Isle_of_Man"}
                    {:offset "+03:00" :zone "Europe/Istanbul"}
                    {:offset "Z" :zone "Europe/Jersey"}
                    {:offset "+02:00" :zone "Europe/Kaliningrad"}
                    {:offset "+02:00" :zone "Europe/Kiev"}
                    {:offset "+03:00" :zone "Europe/Kirov"}
                    {:offset "Z" :zone "Europe/Lisbon"}
                    {:offset "+01:00" :zone "Europe/Ljubljana"}
                    {:offset "Z" :zone "Europe/London"}
                    {:offset "+01:00" :zone "Europe/Luxembourg"}
                    {:offset "+01:00" :zone "Europe/Madrid"}
                    {:offset "+01:00" :zone "Europe/Malta"}
                    {:offset "+02:00" :zone "Europe/Mariehamn"}
                    {:offset "+03:00" :zone "Europe/Minsk"}
                    {:offset "+01:00" :zone "Europe/Monaco"}
                    {:offset "+03:00" :zone "Europe/Moscow"}
                    {:offset "+02:00" :zone "Europe/Nicosia"}
                    {:offset "+01:00" :zone "Europe/Oslo"}
                    {:offset "+01:00" :zone "Europe/Paris"}
                    {:offset "+01:00" :zone "Europe/Podgorica"}
                    {:offset "+01:00" :zone "Europe/Prague"}
                    {:offset "+02:00" :zone "Europe/Riga"}
                    {:offset "+01:00" :zone "Europe/Rome"}
                    {:offset "+04:00" :zone "Europe/Samara"}
                    {:offset "+01:00" :zone "Europe/San_Marino"}
                    {:offset "+01:00" :zone "Europe/Sarajevo"}
                    {:offset "+04:00" :zone "Europe/Saratov"}
                    {:offset "+03:00" :zone "Europe/Simferopol"}
                    {:offset "+01:00" :zone "Europe/Skopje"}
                    {:offset "+02:00" :zone "Europe/Sofia"}
                    {:offset "+01:00" :zone "Europe/Stockholm"}
                    {:offset "+02:00" :zone "Europe/Tallinn"}
                    {:offset "+01:00" :zone "Europe/Tirane"}
                    {:offset "+02:00" :zone "Europe/Tiraspol"}
                    {:offset "+04:00" :zone "Europe/Ulyanovsk"}
                    {:offset "+02:00" :zone "Europe/Uzhgorod"}
                    {:offset "+01:00" :zone "Europe/Vaduz"}
                    {:offset "+01:00" :zone "Europe/Vatican"}
                    {:offset "+01:00" :zone "Europe/Vienna"}
                    {:offset "+02:00" :zone "Europe/Vilnius"}
                    {:offset "+03:00" :zone "Europe/Volgograd"}
                    {:offset "+01:00" :zone "Europe/Warsaw"}
                    {:offset "+01:00" :zone "Europe/Zagreb"}
                    {:offset "+02:00" :zone "Europe/Zaporozhye"}
                    {:offset "+01:00" :zone "Europe/Zurich"}
                    {:offset "Z" :zone "GB"}
                    {:offset "Z" :zone "GB-Eire"}
                    {:offset "Z" :zone "GMT"}
                    {:offset "Z" :zone "GMT0"}
                    {:offset "Z" :zone "Greenwich"}
                    {:offset "+08:00" :zone "Hongkong"}
                    {:offset "Z" :zone "Iceland"}
                    {:offset "+03:00" :zone "Indian/Antananarivo"}
                    {:offset "+06:00" :zone "Indian/Chagos"}
                    {:offset "+07:00" :zone "Indian/Christmas"}
                    {:offset "+06:30" :zone "Indian/Cocos"}
                    {:offset "+03:00" :zone "Indian/Comoro"}
                    {:offset "+05:00" :zone "Indian/Kerguelen"}
                    {:offset "+04:00" :zone "Indian/Mahe"}
                    {:offset "+05:00" :zone "Indian/Maldives"}
                    {:offset "+04:00" :zone "Indian/Mauritius"}
                    {:offset "+03:00" :zone "Indian/Mayotte"}
                    {:offset "+04:00" :zone "Indian/Reunion"}
                    {:offset "+03:30" :zone "Iran"}
                    {:offset "+02:00" :zone "Israel"}
                    {:offset "-05:00" :zone "Jamaica"}
                    {:offset "+09:00" :zone "Japan"}
                    {:offset "+12:00" :zone "Kwajalein"}
                    {:offset "+02:00" :zone "Libya"}
                    {:offset "+01:00" :zone "MET"}
                    {:offset "-07:00" :zone "MST7MDT"}
                    {:offset "-08:00" :zone "Mexico/BajaNorte"}
                    {:offset "-07:00" :zone "Mexico/BajaSur"}
                    {:offset "-06:00" :zone "Mexico/General"}
                    {:offset "+13:00" :zone "NZ"}
                    {:offset "+13:45" :zone "NZ-CHAT"}
                    {:offset "-07:00" :zone "Navajo"}
                    {:offset "+08:00" :zone "PRC"}
                    {:offset "-08:00" :zone "PST8PDT"}
                    {:offset "+14:00" :zone "Pacific/Apia"}
                    {:offset "+13:00" :zone "Pacific/Auckland"}
                    {:offset "+11:00" :zone "Pacific/Bougainville"}
                    {:offset "+13:45" :zone "Pacific/Chatham"}
                    {:offset "+10:00" :zone "Pacific/Chuuk"}
                    {:offset "-05:00" :zone "Pacific/Easter"}
                    {:offset "+11:00" :zone "Pacific/Efate"}
                    {:offset "+13:00" :zone "Pacific/Enderbury"}
                    {:offset "+13:00" :zone "Pacific/Fakaofo"}
                    {:offset "+12:00" :zone "Pacific/Fiji"}
                    {:offset "+12:00" :zone "Pacific/Funafuti"}
                    {:offset "-06:00" :zone "Pacific/Galapagos"}
                    {:offset "-09:00" :zone "Pacific/Gambier"}
                    {:offset "+11:00" :zone "Pacific/Guadalcanal"}
                    {:offset "+10:00" :zone "Pacific/Guam"}
                    {:offset "-10:00" :zone "Pacific/Honolulu"}
                    {:offset "-10:00" :zone "Pacific/Johnston"}
                    {:offset "+14:00" :zone "Pacific/Kiritimati"}
                    {:offset "+11:00" :zone "Pacific/Kosrae"}
                    {:offset "+12:00" :zone "Pacific/Kwajalein"}
                    {:offset "+12:00" :zone "Pacific/Majuro"}
                    {:offset "-09:30" :zone "Pacific/Marquesas"}
                    {:offset "-11:00" :zone "Pacific/Midway"}
                    {:offset "+12:00" :zone "Pacific/Nauru"}
                    {:offset "-11:00" :zone "Pacific/Niue"}
                    {:offset "+11:00" :zone "Pacific/Norfolk"}
                    {:offset "+11:00" :zone "Pacific/Noumea"}
                    {:offset "-11:00" :zone "Pacific/Pago_Pago"}
                    {:offset "+09:00" :zone "Pacific/Palau"}
                    {:offset "-08:00" :zone "Pacific/Pitcairn"}
                    {:offset "+11:00" :zone "Pacific/Pohnpei"}
                    {:offset "+11:00" :zone "Pacific/Ponape"}
                    {:offset "+10:00" :zone "Pacific/Port_Moresby"}
                    {:offset "-10:00" :zone "Pacific/Rarotonga"}
                    {:offset "+10:00" :zone "Pacific/Saipan"}
                    {:offset "-11:00" :zone "Pacific/Samoa"}
                    {:offset "-10:00" :zone "Pacific/Tahiti"}
                    {:offset "+12:00" :zone "Pacific/Tarawa"}
                    {:offset "+13:00" :zone "Pacific/Tongatapu"}
                    {:offset "+10:00" :zone "Pacific/Truk"}
                    {:offset "+12:00" :zone "Pacific/Wake"}
                    {:offset "+12:00" :zone "Pacific/Wallis"}
                    {:offset "+10:00" :zone "Pacific/Yap"}
                    {:offset "+01:00" :zone "Poland"}
                    {:offset "Z" :zone "Portugal"}
                    {:offset "+09:00" :zone "ROK"}
                    {:offset "+08:00" :zone "Singapore"}
                    {:offset "-04:00" :zone "SystemV/AST4"}
                    {:offset "-04:00" :zone "SystemV/AST4ADT"}
                    {:offset "-06:00" :zone "SystemV/CST6"}
                    {:offset "-06:00" :zone "SystemV/CST6CDT"}
                    {:offset "-05:00" :zone "SystemV/EST5"}
                    {:offset "-05:00" :zone "SystemV/EST5EDT"}
                    {:offset "-10:00" :zone "SystemV/HST10"}
                    {:offset "-07:00" :zone "SystemV/MST7"}
                    {:offset "-07:00" :zone "SystemV/MST7MDT"}
                    {:offset "-08:00" :zone "SystemV/PST8"}
                    {:offset "-08:00" :zone "SystemV/PST8PDT"}
                    {:offset "-09:00" :zone "SystemV/YST9"}
                    {:offset "-09:00" :zone "SystemV/YST9YDT"}
                    {:offset "+03:00" :zone "Turkey"}
                    {:offset "Z" :zone "UCT"}
                    {:offset "-09:00" :zone "US/Alaska"}
                    {:offset "-10:00" :zone "US/Aleutian"}
                    {:offset "-07:00" :zone "US/Arizona"}
                    {:offset "-06:00" :zone "US/Central"}
                    {:offset "-05:00" :zone "US/East-Indiana"}
                    {:offset "-05:00" :zone "US/Eastern"}
                    {:offset "-10:00" :zone "US/Hawaii"}
                    {:offset "-06:00" :zone "US/Indiana-Starke"}
                    {:offset "-05:00" :zone "US/Michigan"}
                    {:offset "-07:00" :zone "US/Mountain"}
                    {:offset "-08:00" :zone "US/Pacific"}
                    {:offset "-08:00" :zone "US/Pacific-New"}
                    {:offset "-11:00" :zone "US/Samoa"}
                    {:offset "Z" :zone "UTC"}
                    {:offset "Z" :zone "Universal"}
                    {:offset "+03:00" :zone "W-SU"}
                    {:offset "Z" :zone "WET"}
                    {:offset "Z" :zone "Zulu"}])
