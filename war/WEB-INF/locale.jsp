<%@page pageEncoding="UTF-8" %>
<div id="language_picker"> 
	<select id='locale_option'>
		<option style="color:#999" value="">English</option>
		<option disabled="" style="color:#999" value="">------------</option>
		 <option value="ar">العربية</option>
		 <option value="zh">中国</option>
		 <option value="nl">Nederlands</option>
		 <option value="fr">française</option>
		 <option value="de">Deutschland</option>
		 <option value="id">Bahasa Indonesia</option>
		 <option value="it">italiano</option>
		 <option value="jp">日本</option>
		 <option value="ko">한국의</option>
		 <option value="pl">polski</option>
		 <option value="pt">Português</option>
		 <option value="ru">русский</option>
		 <option value="es">spanska</option>
		 <option value="th">ไทย</option>
		 <option value="tr">Türk</option>
		 <option value="uk">Український</option>
	</select>
	<!--<a href="http://translate.evernote.com">Help translate</a>-->
	<script  type="text/javascript" >
		$(document).ready(function() {
			$('#locale_option').change(function(){
				document.cookie="locale=" + $('#locale_option').val()+"; path=/";
				 location.reload();
			});
			selectLanguage();
		});
		
		function selectLanguage(){
		var allCookies = document.cookie.split( ';' );
		var languageName= getCookieValue(allCookies,"locale");
			if(languageName!=null){
				var myselect =  document.getElementById('locale_option');
				for (var i=0; i<myselect.options.length; i++){
					if(myselect.options[i].value==languageName){
						myselect.options[i].selected="selected";
						break;
					}
				}
			}
		}
		
		function getCookieValue(ckie,nme)
		{
		   var splitValues
		   var i
		   for(i=0;i<ckie.length;++i) {
		      splitValues=ckie[i].split("=")
		      var cookieName = splitValues[0].replace(/^\s+|\s+$/g, '');
		      if(cookieName==nme) return splitValues[1]
		   }
		   return null
		}
		
	</script>
</div>