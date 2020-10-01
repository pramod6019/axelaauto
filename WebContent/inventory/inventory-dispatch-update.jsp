<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Dispatch_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	    <HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">   
<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />


	
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	    <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
	    <script>
				    $(function() {
    $( "#txt_dispatch_date" ).datepicker({
      showButtonPanel: true,
      dateFormat: "dd/mm/yy"
    });

  });
	    </script>
	    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
	    <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
        <TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
          <TR>
            <TD align="left">Home &gt; <a href="index.jsp">Inventory</a> &gt; <a href="inventory-adj-list.jsp?all=yes">List Dispatches</a>&nbsp;&gt; <%=mybean.status%>&nbsp;Dispatch:</TD>
          </TR>
          <TR>
            <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></TD>
          </TR>
          <TR>
            <TD height="300" align="center" vAlign="top"><form name="form1"  method="post">
                <table width="100%" border="1" align="center"  cellpadding="0" cellspacing="0" class="tableborder">
                <tr>
                    <td><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                        <tr align="center">
                        <th colspan="3"><strong><font color="#ffffff" >&nbsp; <%=mybean.status%>&nbsp;Dispatch</font></strong></th>
                      </tr>
                        <tr>
                        <td width="30%">&nbsp;</td>
                        <td><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                          </font></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle">Location<font color="#ff0000">*</font>:</td>
                        <td align="left"><select name="dr_dispatch_warehouse_id" id="dr_dispatch_warehouse_id" class="selectbox">
                            <%=mybean.PopulateLocation()%>
                          </select></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle">Category<font color="#ff0000">*</font>:</td>
                        <td align="left"><select name="dr_cat_id" class="selectbox" id="dr_cat_id" onChange="showHint('../inventory/inventory-check.jsp?adj_cat_id=' + GetReplace(this.value)+'&dr_item_id=dr_item_id','dispatch_item_id');">
                            <%=mybean.PopulateCat()%>
                          </select></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle">Item<font color="#ff0000">*</font>:</td>
                        <td align="left"><span id="dispatch_item_id"><%=mybean.PopulateItem()%></span></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right">Date<font color="#ff0000">*</font>: </td>
                        <td align="left"><%if(mybean.empEditperm.equals("1")){%>
                            <input  name="txt_dispatch_date" type="text" class="textbox" id="txt_dispatch_date" value="<%=mybean.dispatchdate%>" size="16" maxlength="16">
                            <%} else{%>
                            <b><%=mybean.dispatchdate%></b>
                            <input name="txt_dispatch_date" type="hidden" class="textbox"  id ="txt_dispatch_date" value = "<%=mybean.dispatchdate%>">
                            <%}%></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle">Qty<font color="#ff0000">*</font>:</td>
                        <td align="left"><input name="txt_dispatch_qty" type="text" class="textbox" id="txt_dispatch_qty" value="<%=mybean.dispatch_qty%>" onKeyUp="toInteger('txt_dispatch_qty','Qty')" size="10" maxlength="10"></td>
                      </tr>
                        <tr valign="middle">
                        <td align="right" valign="middle">Narration<font color="#ff0000">*</font>:</td>
                        <td align="left"><textarea name="txt_dispatch_naration"  cols="50" rows="5" class="textbox" id="txt_dispatch_naration"  onkeyup="charcount('txt_dispatch_naration', 'span_txt_dispatch_naration','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.dispatch_naration%></textarea>
                            <span id=span_txt_dispatch_naration> 255 characters </span></td>
                      </tr>
                        <% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
                        <tr valign="middle">
                        <td align="right" valign="middle">Entry By:</td>
                        <td align="left"><%=mybean.unescapehtml(mybean.entry_by)%></td>
                      </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
                        <tr valign="middle">
                        <td align="right" valign="middle">Entry Date:</td>
                        <td align="left"><%=mybean.entry_date%></td>
                      </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
                        <tr valign="middle">
                        <td align="right" valign="middle">Modified By:</td>
                        <td align="left"><%=mybean.unescapehtml(mybean.modified_by)%></td>
                      </tr>
                        <%}%>
                        <% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
                        <tr valign="middle">
                        <td align="right" valign="middle">Modified Date:</td>
                        <td align="left"><%=mybean.modified_date%></td>
                      </tr>
                        <%}%>
                        <tr align="center">
                        <td colspan="2" valign="middle"><%if(mybean.status.equals("Add")){%>
                            <input name="button" type="submit" class="button" id="button" value="Add Dispatch" />
                            <input type="hidden" name="add_button" value="Add Dispatch">
                            <%}else if (mybean.status.equals("Update")){%>
                            <input type="hidden" name="update_button" value="Update Dispatch">
                            <input name="button" type="submit" class="button" id="button" value="Update Dispatch" />
                            <input name="delete_button" type="submit" class="button" id="delete_button" onClick="return confirmdelete(this)" value="Delete Dispatch"/>
                            <input type="hidden" name="Update" value="yes">
                            <%}%>
                            <input type="hidden" name="entry_by" value="<%=mybean.entry_by%>">
                            <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
                            <input type="hidden" name="modified_by" value="<%=mybean.modified_by%>">
                            <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"></td>
                      </tr>
                      </table></td>
                  </tr>
              </table>
              </form></TD>
          </TR>
        </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>
