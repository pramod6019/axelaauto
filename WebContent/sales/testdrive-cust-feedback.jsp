<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.TestDrive_Cust_Feedback" scope="request"/>
<%mybean.doPost(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
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
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>

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
     <link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"

	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>

    <body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
    <%@include file="../portal/header.jsp" %>
    
    
    <!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Customer Feedback</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="testdrive.jsp">Test Drives</a>&gt;</li>
						<li><a href="testdrive-list.jsp?all=yes">List Test Drives</a>&gt;</li>
						<li><a href="testdrive-cust-feedback.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Customer Feedback</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
<form name="formtestdrive"  method="post" class="form-horizontal">
<center><font color="#ff0000" ><b><%=mybean.msg%></font></center>
					<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						<%=mybean.status%>&nbsp;Customer Feedback 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
					<center>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required. </center>
						
<div class="form-group">
			<label class="control-label col-md-4">Customer: &nbsp;</label>
			<div class="txt-align">
<a href="../customer/customer-list.jsp?customer_id=<%=mybean.testdrive_customer_id%>"target="_blank"><%=mybean.customer_name%> (<%=mybean.testdrive_customer_id%>)</a>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-4">Oppurtunity No.: &nbsp;</label>
			<div class="txt-align">
<a href=enquiry-dash.jsp?enquiry_id=<%=mybean.testdrive_enquiry_id%> ><b><%=mybean.enquiry_no%></b></a>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-4">Sales Consultant: &nbsp;</label>
			<div class="txt-align">
<a href="executive-summary.jsp?emp_id=<%=mybean.testdrive_emp_id%>"> <%=mybean.testdrive_emp_name%></a>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-4">Test Drive ID: &nbsp;</label>
			<div class="txt-align">
<a href="testdrive-list.jsp?testdrive_id=<%=mybean.testdrive_id%>"> <%=mybean.testdrive_id%></a>
			</div>
		</div>
		
		
		<%=mybean.StrHTML%>
		
		  <% if (!mybean.entry_by.equals("")) { %>
		<div class="form-group">
			<label class="control-label col-md-4">Entry By: &nbsp;</label>
			<div class="txt-align">
<%=mybean.unescapehtml(mybean.entry_by)%>
                        <input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>">
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-4">Entry Date: &nbsp;</label>
			<div class="txt-align">
<%=mybean.entry_date%>
                        <input name="entry_date" type="hidden" id="entry_date" value="<%=mybean.entry_date%>">
			</div>
		</div>
		<%} %>
		
		  <% if (!mybean.modified_by.equals("")) { %>
		<div class="form-group">
			<label class="control-label col-md-4">Modified By: &nbsp;</label>
			<div class="txt-align">
<%=mybean.unescapehtml(mybean.modified_by)%>
                        <input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-md-4">Modified Date: &nbsp;</label>
			<div class="txt-align">
<%=mybean.modified_date%>
                        <input name="modified_date" type="hidden" id="modified_date" value="<%=mybean.modified_date%>">
			</div>
		</div>
	<%} %>
	
	<div class="form-group">
			<label class="control-label col-md-4"><b>Total Points: &nbsp;</label>
			<div class="txt-align">
<span id="total"><%=mybean.total%></span>
			</div>
		</div>
	
<%-- 	<b>Total Points:&nbsp;<span id="total"><%=mybean.total%></span></b>&nbsp;&nbsp;&nbsp; --%>
	<center>
                        <input name="submit_button" type="submit" class="button btn btn-success" id="submit_button" value="Submit" onClick="onPress();return SubmitFormOnce(document.formtestdrive, this);"/>
                        <input type="hidden" name="testdrive_id" value="<%=mybean.testdrive_id%>">
                        <input name="enquiry_id" type="hidden" id="enquiry_id" value="<%=mybean.testdrive_enquiry_id%>">
                        <%if (mybean.status.equals("Update")){%>
                        <input name="delete_button" type="submit" class="button btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Feedback" />
                        <%}%></td>
	</center>	
	
	<br>
	
		
					</div>
				</div>
			</div>
						
						
	</form>

					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
    
    
    
     <%@include file="../Library/admin-footer.jsp" %>
     </body>
</HTML>
