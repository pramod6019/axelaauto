<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.accounting.Voucher_Multiple" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>  
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
<link href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css" rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
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
<script type="text/javascript" src="../Library/smart.js"></script>
<script type="text/javascript"
	src="../Library/purchase.js"></script>  
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
        <body   onLoad="" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0"> 
 <tr>
    <td valign="top"  height="200" colspan="2" align="center">
            <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="tableborder" >
            <tr valign="top" >  
                <td><table width="100%" border="0" cellpadding="1" cellspacing="0" class="listtable">            
                    <tr>   
                    <td align="center" valign="top" colspan="2"><font size="2"><b><font color="#ff0000"><%=mybean.msg %></font></b></font>
                  </tr>
                    <tr>            
                    <td align="center" valign="top" colspan="2"><b>Multiple&nbsp;<%=mybean.vouchertype_name %></b>
                    </tr>  
                  </table></td>          
              </tr>  
              <tr>
              <td valign="top" colspan="2" align="center"><form name="form1"  method="post"><%=mybean.StrHTML%>        
              </td>
              </tr>
              <tr>
                <td colspan="2" align="center"><%if(mybean.status.equals("Add")){%>
                        <input name="addbutton" type="submit" class="button" id="addbutton" value="Add" onClick="return SubmitFormOnce(document.form1,this);" />
                        <input type="hidden" name="add_button" value="yes">  
                        <%} %> 
                        </form> 
                        </td>       
                  </tr>
                   <tr>
              <td valign="top" colspan="2" align="center"><%=mybean.StrHTML1%>        
              </td>
              </tr>
          </table>   
          </td>    
  </tr>   
</table>    
        </body>     
</HTML>
