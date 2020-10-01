<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Vehicle_Dash_Insurance_Followup" scope="request"/>
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
$(function() {
	$( "#txt_insurfollowup_time" ).datetimepicker({
      addSliderAccess: true,
	  sliderAccessArgs: {touchonly: false},
      dateFormat: "dd/mm/yy",
	  stepMinute: 5
    });
  });
 </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body   leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %> <%@include file="../Library/vehicle-dash.jsp" %><table width="98%"  border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
            <td align="center" valign="top"><font color="#ff0000" ><b><%=mybean.msg%></b></font></td>
          </tr>
            <tr>
                <td colspan="4" align="center"><%=mybean.customerdetail%></td>
              </tr>
              <tr>
                <td colspan="4" align="center"><br>
                  <%=mybean.StrHTML%></td>
              </tr>
              <tr>
                <td colspan="4" align="center">&nbsp;</td>
              </tr>
              <tr>
                <td colspan="4" height="300" valign="top">
                <form name="form1"  method="post">         
                    <table width="100%" border="1" cellspacing="0" cellpadding="0" class="listtable">
                    <tr>
                        <th colspan="2" align="center">Add Follow-up</th>
                      </tr>                
                    <tr>
                        <td align="right">Feedback<font color="#ff0000">*</font>:<br></td>
                        <td valign="top"><textarea name="txt_insurfollowup_desc" cols="50" rows="4" class="textbox" id="txt_insurfollowup_desc" onKeyUp="charcount('txt_insurfollowup_desc', 'span_txt_followup_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.insurfollowup_desc%></textarea>
                        <br>
                      <span id="span_txt_followup_desc">1000 characters</span></td>
                      </tr>
                    <tr>
                        <td align="right">Next Follow-up Time<font color="#ff0000">*</font>:</td>
                        <td valign="top"><input name="txt_insurfollowup_time" type="text" class="textbox"  id ="txt_insurfollowup_time" value = "<%=mybean.followup_time%>" size="16" maxlength="16">
                          </td>
                      </tr>
                    <tr>
                        <td align="right">Next Follow-up Type<font color="#ff0000">*</font>:</td>
                        <td valign="top"><select name="dr_insurfollowuptype" class="selectbox" id="dr_insurfollowuptype" visible="true" >
                            <%=mybean.PopulateFollowuptype()%>
                      </select></td>
                      </tr>
                    <tr>
                        <td colspan="2" align="center" valign="middle"><input name="submit_button" type="submit" class="button" id="submit_button" value="Submit" />
                        <input type="hidden" name="veh_id" id="veh_id" value="<%=mybean.veh_id%>"></td>
                      </tr>                   
                  </table>
                  </form></td>
              </tr>
            </table>
 <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>