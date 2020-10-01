<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Home" scope="request"/>
<%mybean.doPost(request, response);%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/> 
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico"> 
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?Math.random()"></script>
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />

<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<style>
.myclass {
	cursor:pointer;
}

.button {
  margin: .4em;
  padding: 3em;
  cursor: pointer;
  text-decoration: none;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}

.float {
  display: inline-block;
  -webkit-transition-duration: 0.3s;
  transition-duration: 0.3s;
  -webkit-transition-property: transform;
  transition-property: transform;
  -webkit-transform: translateZ(0);
  transform: translateZ(0);
  box-shadow: 0 0 1px rgba(0, 0, 0, 0);
}

.float:hover, .float:focus, .float:active {
  -webkit-transform: translateY(-5px);
  transform: translateY(-5px);
}

#dbvalue {
	font-size:30px;
	color:#FFF;
	font-family:Arial, Helvetica, sans-serif;
}

#dblabel {
	font-size:14px;
	color:#FFF;
}
</style>

<script>
	$(function() {
		
    $( "#txt_from_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	
	$( "#txt_to_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });
	
	});	
	
	function CheckDetails() {
		var from, to;
		from = document.getElementById("txt_from_date").value;
		to = document.getElementById("txt_to_date").value;
		//alert("from = "+from);
		//alert("to = "+to);
		if(from =='' || to == '') {
			alert("From and To date must be selected!");
		} else {
			var fdate = new Date(from);
			var tdate = new Date(to);
			//alert(new Date());
			//alert("fdate = "+new Date(fdate));
			//alert("tdate = "+new Date(fdate)).toLocaleFormat('%Y-%m-%d'));
			if(fdate.setHours(0,0,0,0)>tdate.setHours(0,0,0,0)) {
			//if(new Date("2014-10-17").toLocaleFormat('%Y-%m-%d')>
			//new Date("2014-10-17").toLocaleFormat('%Y-%m-%d')) {
				alert("From date must be less than To date");
			} else {
				showHint('../app/home-check.jsp?ajax=yes&from='+from+'&to='+to+'', 'ajaxhint');
			}
		}
	}
	
	function Redirect(id) {
		var from, to;
		from = document.getElementById("txt_from_date").value;
		to = document.getElementById("txt_to_date").value;
		//alert(id);
		if(id == 1) {
			//alert("************");
			window.location = "../app/model-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}
		if(id == '2') {
			window.location = "../app/model-offers-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}
		if(id =='3') {
			window.location = "../app/servicecentre-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}
		if(id =='4') {
			window.location = "../app/showroom-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}
		/* if(id =='5') {
			window.location = "../preowned/preownedtestdrive-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}
		if(id =='6') {
			window.location = "../portal/servicebooking-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}
		if(id =='7') {
			window.location = "../preowned/insurance-list.jsp?homefilter=yes&from="+from+"&to="+to+"";
		}  */
	}
</script>


</HEAD>
<body onLoad="CheckDetails();" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" >
 <%@include file="../portal/header.jsp" %>
<table width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
<tr>
     <TD height="25"><a href="../portal/home.jsp">Home</a>:</TD>
  </tr>
  <tr>
    <td vAlign="bottom"><table width="100%" border="1" cellspacing="0" cellpadding="0" class="tableborder">
      <tr>
        <td><table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
          <tr valign="middle">
            <td  align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0" class="listtable">
              <tr>
                <th>App Dashboard</th>
              </tr>
            </table></td>
          </tr>
          <tr valign="top">
            <td>&nbsp;</td>
          </tr>
          <tr valign="middle">
            <td  align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr valign="middle">
                <td align="right" colspan="2">From<font color="#ff0000">*</font>:
                  <input name="txt_from_date" type="text" class="textbox"  id ="txt_from_date" value = "<%=mybean.from_date%>" size="12" maxlength="10" onchange="CheckDetails();" />
                  &nbsp;&nbsp;&nbsp;</td>
                <td align="left" colspan="2">&nbsp;&nbsp;&nbsp;To<font color="#ff0000">*</font>:
                  <input name= "txt_to_date" type="text" class="textbox"  id ="txt_to_date" value = "<%=mybean.to_date%>" size="12" maxlength="10" onchange="CheckDetails();"/></td>
              </tr>
            </table></td>
          </tr>
          <tr valign="top">
            <td>&nbsp;</td>
          </tr>
          <tr valign="top">
            <td height="300">
            <div id="ajaxhint">
            </div>
            </td>
          </tr>
        </table></td>
        </tr>
    </table></td>
  </tr>
  
  <tr>
    <td align="center">&nbsp;</td>
  </tr>
  
</table>

<%@ include file="../Library/admin-footer.jsp" %>
</body>
</HTML>