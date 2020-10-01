<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Salesorder_Import" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
        <script type="text/javascript">
			
			$(document).ready(function(){
				
				$("#frmdoc").submit(function(){
					//alert("exe.nvbnv.");
					//return true;
					$('#dialog-modal').dialog('open');
					//return true;
					}
				)	
				
				// Dialog
				$('#dialog-modal').dialog({
					autoOpen: false,
					width : 200,
					height: 120,
                    //zIndex: 200,
					modal: true,
					title: "File Upload in progress!" ,
					draggable : false,
					closeOnEscape: false,
					open: function(event,ui){$(".ui-dialog-titlebar-close").hide();}
				});		
			});
		</script>
        <script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.frmdoc.dr_branch.focus();
}

        </script>
         <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

        <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" onload="FormFocus()">
<%@include file="../portal/header.jsp" %>
          <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
              <td align="left" valign="top"><a href="../portal/home.jsp">Home</a> &gt;  <a href="index.jsp">Sales</a> &gt; <a href="../sales/veh-salesorder.jsp">Sales Order</a> &gt; <a href="../sales/salesorder-import.jsp">Import Sales Order</a>:</td>
            </tr>
    <tr>
              <td align="right" valign="top"><table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
                  <tr>
                  <td align="center"><font color="#FF0000" ><b><%=mybean.msg%></b></font></td>
                </tr>
                  <tr>
                  <td height="300" align="center" valign="top">
<form name="frmdoc"  id="frmdoc" enctype="MULTIPART/FORM-DATA" method="post" onsubmit="aletdisp();">
                  <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0"  class="tableborder">
                      <tr>
                      <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                          <tr align="center">
                          <th height="20" colspan="3" ><strong><font color="#ffffff" >Import Stock</font></strong></th>
                        </tr>
                          <tr>
                          <td width="30%" align="center">&nbsp;</td>
                          <td width="70%"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                            </font></td>
                        </tr>
                          <tr valign="middle">
                          <td align="right" valign="middle">Branch<font color="#ff0000">*</font>:</td>
                          <td valign="middle"><select name="dr_branch" class="selectbox" id="dr_branch">
                              <%=mybean.PopulateBranch(mybean.so_branch_id, "", "1,2", "", request)%>
                            </select>
                              </td>
                        </tr>
                          <% //if(!stock_branch_id.equals("0")){%>
                          <tr>
                          <td align="right">Select Document<font color="#FF0000">*</font>: </td>
                          <td><strong>
                            <input NAME="filename" Type="file" class="button" id="filename" value="<%=mybean.doc_value%>" size="30">
                            </strong></td>
                        </tr>
                          <tr>
                          <td align="right">&nbsp;</td>
                          <td><font size="1">Click the Browse button to select the document from your computer!</font></td>
                        </tr>
                          <tr>
                          <td colspan="2" align="center">Allowed Formats: <b><%=mybean.importdocformat%></b></td>
                        </tr>
                          <tr>
                          <td colspan="2" align="center">Maximum Size: <b><%=mybean.docsize%> Mb</b></td>
                        </tr>
                          <tr>
                          <td colspan="2" align="center"><!-- ui-dialog -->
                              
                              <div id="dialog-modal" title="File Upload" align="center" >
                              <p align="center">Please wait...</p>
                              <img align="middle" src="../admin-ifx/loading.gif" /></div>
                              <input name="addbutton" type="submit" class="button" id="addbutton" value="Upload"/>
                              <input name="add_button" type="hidden" class="button" id="add_button" value="Upload"/>
                              </td>
                        </tr>
                          <%//}%>
                        </table></td>
                    </tr>
                    </table></form></td>
                </tr>
                </table></td>
            </tr>
    <tr>
              <td colspan="2" align="center">&nbsp;</td>
            </tr>
  </table>
         <%@include file="../Library/admin-footer.jsp" %></body>
</html>
