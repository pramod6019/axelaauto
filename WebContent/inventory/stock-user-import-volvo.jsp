<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Stock_User_Import_Volvo" scope="request"/>    
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
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
				
				// //Dialog
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
        <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

        <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0" onload="FormFocus()">
<%@include file="../portal/header.jsp" %>
          <table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
             <td valign="top"><a href="../portal/home.jsp">Home</a> &gt; <a href="index.jsp">Inventory</a> &gt;  <a href="../inventory/stock-user-import.jsp">Import Stock</a> &gt;  <a href="../inventory/stock-user-import-volvo.jsp">Volvo Stock Import</a>:</td>
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
                            <th height="20" colspan="2" >Download the Stock template file</th>
                          </tr>
                          <tr align="center">
                            <td colspan="2" > Start importing Stock by downloading template file. <br />
                            Download the template and enter Stock data as per the headings. <br />
                            Headings marked in red are manadatory. Don't change the header columns.</td>    
                          </tr>
                          <tr align="center">
                            <td colspan="2" ><a href="../Library/template/stock-template-volvo.xlsx" target="_blank"><b>Click here to download Stock Template Volvo</b></a></td>
                          </tr>
                          <tr align="center">
                            <td colspan="2" >&nbsp;</td>  
                          </tr>
                          <tr align="center">
                          <th height="20" colspan="2" >Import Stock</th>
                        </tr>  
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
                              <input name="add_button" type="hidden" class="button" id="add_button" value="Upload"/><input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload"/>
                              </td>
                        </tr>  
                        </table></td>
                    </tr>
                    </table></form></td>
                </tr>
                </table></td>
            </tr>
    <tr>
              <td colspan="2" align="center">&nbsp;</td>
            </tr>
  </table> <%@include file="../Library/admin-footer.jsp" %>
  <%@include file="../Library/js.jsp"%></body>
</html>
