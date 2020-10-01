
 function showHint(url, Hint) {
//alert(url);
		$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
			$.ajax({
				url: url,
				type: 'GET',
				success: function (data){
	//alert("data=="+data);
						if(data.trim() != 'SignIn'){
						$('#'+Hint).show();
						$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
						FormElements();
						} else{
						window.location.href = "../portal/";
						}
				}
			});
	}
 
 /*  Javascript for Footable*/
 function showHintFootable(url, Hint) {
 	$('#'+Hint).html('<div id=loading align=center><img align=center src=\"../admin-ifx/loading.gif\" /></div>');
 		$.ajax({
 			url: url,
 			type: 'GET',
 			success: function (data){ 
 				if(data.trim() != 'SignIn') {
 					$('#'+Hint).show();
 					$('#'+Hint).fadeIn(500).html('' + data + '');
 					$('.footable').footable().trigger('footable_intialized');
 					FormElements();
 				} else {
 					window.location.href = "../portal/";
 				}
 			}  
 		});
 	//alert("success");
 }

function GetReplace(str)
{
  if (str.length!=0)
  {
    var re;
    re = /&/g; 
    str = str.replace(re, "nbsp");
    str=escape(str);
  }
  return str;
}

function GetReplaceString(str)
{
  if (str.length!=0)
  {
    var re;
//    re = /&/g; 
    re = /[^\d\,]/g;
    str = str.replace(re, "");
    str=escape(str);
  }
  return str;
}
