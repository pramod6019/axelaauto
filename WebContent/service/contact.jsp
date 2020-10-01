<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Contact" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- 					BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Search Contacts</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="index.jsp">Service</a> &gt;</li>
							<li><a href="ticket.jsp">Tickets</a> &gt;</li>
							<li><a href="contact.jsp">Search Contacts</a><b>:</b></li>

						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search Contacts
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label> Search <font color=red>*</font>:&nbsp; </label>
												<input name="txt_search" type="txt_search" size="40" maxlength="13"
													class="form-control" id="txt_search" value="<%=mybean.txt_search%>"
													onKeyUp="ContactCheck('txt_search',this,'hint_txt_search')" 
													placeholder="Enter Mobile Number!"/>
											</div>
											<center>
												<input name="search_button" type="button"
													class="btn btn-success" id="search_button" value="Search"
													onClick="ContactCheck('txt_search',this,'hint_txt_search');" />
											</center>
											<center>
												<div class="" id="hint_txt_search"></div>
											</center>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">

		function ContactCheck(name, obj, hint) {
			var value = document.getElementById("txt_search").value;
			var url = "../service/contact-check.jsp?";
			var param = "&contact=" + value;
			var str = "123";
			if(value.length >= 13  && value != ""){
			showHint(url + param, hint);
			}
// 			$('#'+Hint).html('<div id=loading align=center><img align=center alt="test" src=\"../admin-ifx/loading.gif\" /></div>');
		}
		
		function isNumber(ob) {
			var invalidChar = /[^0-9]/gi
			if (invalidChar.test(ob.value)) {
				ob.value = ob.value.replace(invalidChar, "");
			}
		}
		
		function SendSms(){
			var contact_id = $("#contact_id").val();
			var contact_mobile = $("#contact_mobile").val();
			var contact_name = $("#contact_name").val();
			var exe_id = $("#allexecutives").val();
			showHint('../service/contact-check.jsp?executivesendsms=yes&contact_id='+contact_id+'&exe_id=' + exe_id+'&contact_name='+contact_name+'&contact_mobile=' + contact_mobile , 'sentmsg');
			setTimeout('hideform();', 400);
		}						

	function hideform() {
		var sentmsg = $("#sentmsg").text().trim();
		if (sentmsg == 'SMS Sent Succesfully!') {
			$('#form1').hide();
			$('#sendSms').hide();
		}
	}
		
	</script>
</body>
</HTML>
