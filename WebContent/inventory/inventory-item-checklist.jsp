<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Item_CheckList" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
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
<script type="text/javascript" src="../Library/quote.js"></script>

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" onload="">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
<TR>
  <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Inventory</a> &gt; <a href="inventory-item-list.jsp?all=yes">List Items</a> &gt; <a href="inventory-item-list.jsp?item_id=<%=mybean.item_id%>"><%=mybean.item_name%></a> &gt; <a href="../inventory/inventory-item-checklist.jsp?item_id=<%=mybean.item_id%>">Job Card Check List:</a></TD>
</TR>
<tr>
  <td>&nbsp;</td>
</tr>
<TR>
  <TD align="center" vAlign="top"><font color="#ff0000"><b><%=mybean.msg%></b></font></TD>
</TR>
<TR>
  <TD align="center" vAlign="top"><form name="form1" id="form1" method="post" action="">
      <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder">
        <tr>
          <td align="center" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">                       
             
              <tr>
                <td valign="top" ><table border="1" cellspacing="0" cellpadding="0" align="center" class="listtable">
                    <tr>
                      <th align="center" colspan="6">Item Details</th>
                    </tr>
                    
                    <tr>
                      <td align="right" valign="top">Name<font color="#ff0000">*</font>:</td>
                      <td align="left" valign="top"><input name="txt_item_name" type="text" id="txt_item_name" value="<%=mybean.check_name%>" size="30" maxlength="255" class="textbox"/></td>
                      <td align="right" valign="top">Type:</td>
                      <td align="left" valign="top">
                      <select id="dr_check_type" name="dr_check_type" class="selectbox">
					  <%=mybean.PopulateCheckType()%>
                      </select></td>                    
                      
                    </tr>                   
                    <tr><td align="center" valign="top" colspan="4">
					<% if (mybean.status.equals("") || mybean.status.equals("Add")) {%>
                   
                    <input name="add_button" id="add_button" type="button" onClick="return SubmitFormOnce(document.form1, this);" class="button" value="Add Check List" />
                                                <input type="hidden" id="add"  name="add" value="yes">
                                                <input type="hidden" name="add_button" value="Add Check List"><%}%>
                                                <% if (mybean.status.equals("Update")) {%>
                                                
                                                <input type="hidden" id="Update"  name="Update" value="yes">           <input type="hidden" name="update_button" value="Update Check List">
                                                <input name="update_button" id="update_button" type="button" onClick="return SubmitFormOnce(document.form1, this);" class="button"  value="Update Check List" />
                                                <input name="delete_button" type="submit" class="button" id="delete_button" onClick="return confirmdelete(this)"  value="Delete Check List"/>
                                                <%}%>
                <input type="hidden" id="item_id"  name="item_id" value="<%= mybean.item_id%>">
                <input type="hidden" id="check_id"  name="check_id" value="<%= mybean.check_id%>"></td></tr>
                  </table></td>
              </tr>
              <tr>
                <td align="center" valign="top"><%=mybean.StrHTML%></td>
              </tr>
            </table></td>
        </tr>
      </table>        
    </form>
              </TD>
</TR>                
</TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
