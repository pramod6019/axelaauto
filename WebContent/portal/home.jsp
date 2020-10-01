<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Home" scope="request" />
<% mybean.doPost(request, response); %>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
<link href='../Library/fullcalendar.css' rel='stylesheet' type='text/css' />
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href='../Library/fullcalendar.print.css' media='print' rel='stylesheet' type='text/css' />
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<style type='text/css'>
#calendar { margin: 0; }
</style>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Home</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE CONTENT INNER -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">

						<div class="col-md-4 ">
							<div class="container-fluid portlet box">
								<div class="portlet-title">
									<div class="caption">Announcements</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="news">
										<%=mybean.StrNews%>
									</div>
								</div>
							</div>
							<br>
							<form name="" id="" method="get">
<!-- 								<div class="container-fluid portlet box"> -->
<!-- 									<div class="portlet-title" style="text-align: center"> -->
<!-- 										<div class="caption">Search</div> -->
<!-- 									</div> -->
<!-- 									<div class="portlet-body portlet-empty"> -->
<!-- 										<div class="tab-pane" id="news"> -->
<!-- 											<div style="margin-bottom: 10px;"> -->
<!-- 												<div class="col-md-1 col-sm-1"></div> -->
<!-- 												<div class="col-md-7 col-sm-7"> -->
<!-- 													<input name="txt_search" type="text" class="form-control" -->
<%-- 														id="txt_search" value="<%=mybean.search%>" size="30" --%>
<!-- 														maxlength="255" /> -->
<!-- 												</div> -->
<!-- 												<div class="col-md-4 col-sm-4"> -->
<!-- 													<select name="dr_module_id" class="form-control" -->
<!-- 														id="dr_module_id"> -->
<%-- 														<%=mybean.PopulateModule()%> --%>
<!-- 													</select> -->
<!-- 												</div> -->
<!-- 											</div> -->
<!-- 											<div class="col-md-1 col-sm-1" style="text-align: center">By -->
<!-- 											</div> -->
<!-- 											<div class="col-md-7 col-sm-7"> -->
<!-- 												<select name="dr_module_type" class="form-control" id="dr_module_type"> -->
<%-- 												<span id='dr_module_type_option'><%=mybean.PopulateModuleType()%></span> --%>
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 											<div class="col-md-4 col-sm-4"> -->
<!-- 												<center> -->
<!-- 													<input name="go" type="submit" class="btn btn-success" id="go" value="Go" -->
<!-- 														onclick="onPress();return SubmitFormOnce(document.formemp,this);" /> -->
<!-- 													<input type="hidden" name="btn_go" value="Go" /> -->
<!-- 												</center> -->
<!-- 											</div> -->

<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
							</form>
							<br>

							<div class="container-fluid portlet box">
								<div class="portlet-title">
									<div class="caption">Quick Links</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<ul>
											<li class="inline-menu"><a href="../portal/canned.jsp?canned=yes&home=yes"
											data-target="#Hintclicktocall" data-toggle="modal" style="margin-top: 1px;">Canned Messages</a>
											</li>
											<li class="inline-menu"><a href="../service/contact.jsp">Search Contacts</a></li>
											<li class="inline-menu"><a href="../service/branch.jsp">Search Employees</a></li>
											<li><a href="../sales/enquiry-quickadd.jsp?add=yes">Add Enquiry</a></li>
											<li><a href="../preowned/preowned-quickadd.jsp"> Add Pre-Owned Enquiry</a></li>
											<li><a href="../service/booking-enquiry.jsp">Add Service Booking</a></li>
											
											<%if(mybean.comp_module_insurance.equals("1")){ %>
												<li><a href="../portal/ecover-signin.jsp?enquiry_add=yes "target=_blank>Add Insurance Enquiry</a></li>
											<%}%>
											
											<li><a href="../sales/enquiry-list.jsp?all=yes">List Enquiry</a></li>
											<li><a href="../sales/testdrive-list.jsp?all=yes">List Test Drives</a></li>
											<li><a href="../sales/report-stock-exe.jsp">Executive Stock Status</a></li>
											<li><a href="../sales/campaign-update.jsp?add=yes">Add Campaign</a></li>
											<li><a href="../sales/target.jsp">List Targets</a></li>
											<li><a href="../sales/report-so-pendingdelivery.jsp">Sales Orders Pending Delivery</a></li>
											<li><a href="system-password.jsp">Change Password</a></li>
											<li><a href="../customer/report-customer-birthday.jsp">Customer Birthday</a></li>
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-8">
							<div class="portlet light portlet-fit calendar">
								<div class="portlet-title">
									<div class="caption">
										<i class=" icon-layers font"></i>
										<span class="caption-subject font sbold uppercase">Calendar</span>
									</div>
								</div>
								<div class="portlet-body">
									<div class="row">
										<div class="col-md-12 col-sm-12">
											<div id="calendar" class="has-toolbar"></div>
										</div>
									</div>
								</div>
							</div>
						</div>


					</div>
					<!-- END PAGE CONTENT INNER -->
				</div>
			</div>
			<!-- END PAGE CONTENT BODY -->
			<!-- END CONTENT BODY -->
		</div>
		<!-- END CONTENT -->

	</div>



	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp" %>
	<script type="text/javascript" src="../Library/dynacheck.js"></script>
	<script src="../assets/js/moment.min.js" type="text/javascript"></script>
	<script src="../assets/js/jquery.qtip.js" type="text/javascript"></script>
	<script src="../assets/js/calendar.js" type="text/javascript"></script>
	<script type='text/javascript' src='../Library/fullcalendar.min.js'></script>
	<script type="text/javascript" src="../Library/jquery.newsTicker.js"></script>
	<script type='text/javascript' src='../Library/jquery.qtip.js'></script>
	<script>
		$('#news-container').newsTicker({
			row_height : 80,
			max_rows : 3,
			duration : 5000
		});

		/* $(document).ready(function() {
			$('#calmonth').datepicker({
				inline : true,
				showButtonPanel : true,
				onSelect : function(dateText, inst) {
					var d = new Date(dateText);
					$('#calendar').fullCalendar('gotoDate', d);
				}
			});
		}); */
		
		function SendEmail(email_id){
// 			alert("email_id==="+email_id);
			var customer_email_to = $('#txt_email').val();
// 			alert("customer_email_id==="+customer_email_to);
			if(!customer_email_to == ''){
				showHint('../portal/canned-message-check.jsp?home=yes&email=yes&email_id=' + email_id + '&customer_email_to=' + customer_email_to, 'sentmsg');
			}else{
				$("#sentmsg").html("<b> Enter Email ID!").css("color", "red");
			}
		}
		
		function SendSMS(sms_id){
			var customer_mobile_no = $('#txt_mobile').val();
// 			if(customer_mobile_no == ''){
// 				$("#sentmsg").html("<b> Enter Mobile No.!").css("color", "red");
// 			}else
			if(customer_mobile_no.length == 13){
				showHint('../portal/canned-message-check.jsp?home=yes&sms=yes&sms_id=' + sms_id + '&customer_mobile_no=' + customer_mobile_no, 'sentmsg');
			}else{
// 				alert("Incorrect");
				$("#sentmsg").html("<b> Incorrect Mobile No.!").css("color", "red");
			}
		}
	</script>
</body>
</HTML>
