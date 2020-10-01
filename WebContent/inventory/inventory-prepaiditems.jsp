<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Prepaiditems" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   

<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
                <script>
                    /// function for search items
                    function ItemSearch()
                    {
                        var value = document.getElementById("txt_search").value;
                        var url = "item-check.jsp?";
                        var param="&q="+ value;
                        var str = "123";
                        showHint(url+param,'hint_search_item');
                    }
                    //end

                    /// function for adding a item into prepaid card
                    function AddPrepaidItem(item_id){
                        var qty = document.getElementById(item_id).value;
                        var itemmaster_id = document.getElementById("txt_itemmaster_id").value
                        window.location = "inventory-prepaiditems.jsp?add=yes&trans_itemmaster_id="+itemmaster_id+"&item_id="+item_id+"&qty="+qty+"";
                    }
                    //end
                    /// function for deleting a item from prepaid card
                    function DeletePrepaidItem(item_id){
                        var itemmaster_id = document.getElementById("txt_itemmaster_id").value
                        window.location = "inventory-prepaiditems.jsp?delete=yes&trans_itemmaster_id="+itemmaster_id+"&item_id="+item_id+"";
                    }
                    //end
                </script>
        
		<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
    <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <%@include file="../portal/header.jsp" %>
 <form name="form1" id="form1" method="get">
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">

  <tr>
    <td><%=mybean.LinkHeader%>&nbsp;<br></td>
  </tr>
  <tr><td valign="top"  colspan="2" align="center"><font color="#ff0000"><b><%=mybean.msg%></b></font><br></td></tr>
  <tr>
    <td height="200"  colspan="2" align="center" h>
	<input type="hidden" id="txt_itemmaster_id" name="txt_itemmaster_id" value="<%=mybean.itemmaster_id%>">
	<%=mybean.StrHTML%></td></tr>
    <tr>
    <td valign="top"  colspan="2" align="center">&nbsp;</td></tr>
  </table>
  <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td align="center" valign="top"><table width="100%" border="1" cellpadding="0" cellspacing="0" class="tableborder">
      <tr>
        <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
          <tr>
            <th>Search Items To Add:</th>
          </tr>
          <tr>
            <td align="center" valign="top"><input name ="txt_search" type="text" class="textbox" id="txt_search" value="" size="30" maxlength="255" onKeyUp="ItemSearch();"/></td>
          </tr>
          <tr>
            <td align="center" valign="top" height="100"><div id="hint_search_item"> Enter your search parameter! </div></td>
          </tr>
        </table></td>
      </tr>
    </table>    
    </tr>
  </table>
 </form> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
