<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Cust_Feedback" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
    
    <script type="text/javascript" src="../Library/Validate.js"></script>
    <script type="text/javascript" src="../Library/dynacheck-post.js"></script>
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
    <script language="JavaScript" type="text/javascript">
	function SingleSelect(regex,name,current,span_id)
        {		
		    re = new RegExp(regex);
			total=0;
            for(i = 0; i < document.forms[0].elements.length; i++) {
                elm = document.forms[0].elements[i];
					//alert('hello'+elm);
                    if (elm.type == 'checkbox') {
                        if (elm.name==name && re.test(elm.name) && elm!=current) {
                            elm.checked = false;
                        }
						else if (elm.name==name && re.test(elm.name) && elm==current)
						{							
							document.getElementById(span_id).innerHTML=elm.value;
						}
                    }
                }
				var asd=document.body.getElementsByTagName('span').length;
				//alert(asd);
				for(i=0;i<asd-1;i++){
				//alert(document.body.getElementsByTagName('span').item(i).innerHTML);
					total=parseInt(total)+parseInt(document.body.getElementsByTagName('span').item(i).innerHTML);
				}
				document.getElementById('total').innerHTML=total;
				//if(default_value=="1") current.checked = true;            
        }
    </script>
    <link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

    <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
<%@include file="../portal/header.jsp" %>
<TABLE width="98%" border="0" align="center" cellPadding="0" cellSpacing="0">
      <TR>
    <TD align="left"><a href="../portal/home.jsp">Home</a> &gt; <a href="../service/index.jsp">Service</a> &gt; <a href="jobcard-list.jsp?all=yes">List Job Cards</a> &gt;&nbsp;<a href="jobcard-cust-feedback.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Customer Feedback</a>:</TD>
  </TR>
      <tr>
    <td>&nbsp;</td>
  </tr>
      <TR>
    <TD align="center" vAlign="top"><font color="#ff0000" ><b><%=mybean.msg%></font></TD>
  </TR>
      <TR>
    <TD align="center" vAlign="top"><form name="formjc"  method="post">
        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="1"  class="tableborder" >
          <tr valign="middle" >
            <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                <tbody>
                  <tr align="middle">
                    <th colspan="4" align="center"><%=mybean.status%>&nbsp;Customer Feedback</th>
                  </tr>
                  <tr>
                    <td>&nbsp;</td>
                    <td valign="top" colspan="4"><font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </font></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Customer:</td>
                    <td><a href="../customer/customer-list.jsp?customer_id=<%=mybean.jc_customer_id%>"target="_blank"><%=mybean.customer_name%></a></td>
                    <td align="right">Contact:</td>
                    <td><a href="../customer/customer-contact-list.jsp?contact_id=<%=mybean.jc_contact_id%>"target="_blank"><%=mybean.contact_name%></a></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Contact Mobile No.:</td>
                    <td><%=mybean.contact_mobile1%></td>
                    <td align="right">Contact Email ID:</td>
                    <td><%=mybean.contact_email1%></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Job Card ID:</td>
                    <td ><a href="jobcard-list.jsp?jc_id=<%=mybean.jc_id%>"> <%=mybean.jc_id%></a></td>
                    <td align="right">Job Card Date:</td>
                    <td ><%=mybean.jc_time_in%></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Delivered Date:</td>
                    <td ><%=mybean.jc_time_out%></td>
                    <td align="right">Service Advisor:</td>
                    <td ><a href="executive-summary.jsp?emp_id=<%=mybean.jc_emp_id%>"> <%=mybean.jc_emp_name%></a></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Service Advisor Mobile No:</td>
                    <td><%=mybean.emp_mobile1%></td>
                    <td align="right">Service Advisor Email ID:</td>
                    <td><%=mybean.emp_email1%></td>
                  </tr>
                  <tr valign="center">
                    <td align="right">Reg. No.:</td>
                    <td><a href="../service/vehicle-list.jsp?veh_id=<%=mybean.jc_veh_id%>"target="_blank"><%=mybean.veh_reg_no%></a></td>
                    <td align="right">Item:</td> 
                    <td><a href="../inventory/inventory-item-list.jsp?item_id=<%=mybean.veh_variant_id%>"target="_blank"><%=mybean.variant_name%></a></td>
                  </tr>
                  <tr valign="center">
                    <td colspan="4" align="center"><%=mybean.StrHTML%></td>
                  </tr>
                  <% if (!mybean.entry_by.equals("")) { %>
                  <tr>
                    <td align="right">Entry By:</td>
                    <td align="left" colspan="3"><%=mybean.unescapehtml(mybean.entry_by)%>
                      <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>"></td>
                  </tr>
                  <tr>
                    <td align="right">Entry Date:</td>
                    <td align="left" colspan="3"><%=mybean.entry_date%>
                      <input name="entry_date" type="hidden" id="entry_date" value="<%=mybean.entry_date%>"></td>
                  </tr>
                  <%}%>
                  <% if (!mybean.modified_by.equals("")) { %>
                  <tr>
                    <td align="right">Modified By:</td>
                    <td align="left" colspan="3"><%=mybean.unescapehtml(mybean.modified_by)%>
                      <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>"></td>
                  </tr>
                  <tr>
                    <td align="right">Modified Date:</td>
                    <td align="left" colspan="3"><%=mybean.modified_date%>
                      <input name="modified_date" type="hidden" id="modified_date" value="<%=mybean.modified_date%>"></td> 
                  </tr>
                  <%}%>
                  <tr valign="center">
                    <td align="center" colspan="4"><b>Total Points:&nbsp;<span id="total"><%=mybean.total%></span></b>&nbsp;&nbsp;&nbsp;
                      <input name="submit_button" type="submit" class="button" id="submit_button" value="Submit" onClick="onPress();return SubmitFormOnce(document.formjc, this);"/>
                      <input type="hidden" name="jc_id" value="<%=mybean.jc_id%>">
                      <%if (mybean.status.equals("Update")){%>
                      <input name="delete_button" type="submit" class="button" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Feedback" />
                      <%}%></td>
                  </tr>
                </tbody>
              </table></td>
          </tr>
        </table>
      </form></TD>
  </TR>
    </TABLE> <%@include file="../Library/admin-footer.jsp" %></body>
</HTML>