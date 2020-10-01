// JavaScript Document
 function showHintPost(url, str, params, Hint)
 {
    //alert("hi==========");
      if (str.length==0)
      { 
        document.getElementById(Hint).innerHTML="";
        return;
      }
      var xmlHttp;
  try
    {
    //// Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
  catch (e)
    {
    // Internet Explorer
    try
      {
      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
    catch (e)
      {
      try 
        {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
      catch (e)
        {
        alert("Your browser does not support AJAX!");
        return false;
        }
      }
    }
    xmlHttp.onreadystatechange=function()
      {
      if(xmlHttp.readyState==4 && xmlHttp.status == 200)
        {
//			alert(xmlHttp.responseText);
    	  if(Hint=='span_lostcase2'||Hint=='span_lostcase3')
    		  {
	document.getElementById(Hint).innerHTML=xmlHttp.responseText;
    		 }
//    	  if(Hint=='hint_txt_days_diff'&& str=='333')
//    		  {
//    		  document.getElementById(Hint).innerHTML=xmlHttp.responseText;
//    		  }
    	  else{
    		  if(xmlHttp.responseText!='')
    			  {
		showToast(xmlHttp.responseText);
    		 }
    	  }
        }
      }
	
	 // document.getElementById(Hint).innerHTML="Processing!";
	  url=url+"&sid="+Math.random();
	  xmlHttp.open("POST",url,true);
	  //Send the proper header information along with the request
		xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xmlHttp.setRequestHeader("Content-length", params.length);
		xmlHttp.setRequestHeader("Connection", "close");
	  xmlHttp.send(params);
	}

function GetReplacePost(str)
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