<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Fieldappt"
	scope="request" />
<jsp:useBean id="export" class="axela.insurance.Insurance_Export"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

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
						<h1>Field Appointment</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<li><a href="fieldappt.jsp">Field Appointment</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="container-fluid portlet box">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Search</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<!-- START PORTLET BODY -->
								<div class="container-fluid">
									<form class="form-horizontal" name="frm1" role="form"
										method="get">
										<div class="form-element6">
											<div>Executives:</div>
												<select name="dr_emp_id" id="dr_emp_id" class="form-control">
													<%=mybean.PopulateFieldExecutive(mybean.comp_id, request)%>
												</select>
										</div>

										<div class="form-element3">
											<div> Start Date<font color=#ff0000><b>*</b></font>: </div>
												<input name="txt_starttime" id="txt_starttime"
													value="<%=mybean.start_time%>"
													class="form-control datepicker"
													type="text"  />
										</div>

										<div class="form-element3">
											<div> End Date<font color=#ff0000><b>*</b></font>: </div>
												<input name="txt_endtime" id="txt_endtime"
													value="<%=mybean.end_time%>"
													class="form-control datepicker"
													data-date-format="dd/mm/yyyy" type="text" value="" />
										</div>


										<div class="form-element12">
											<center>
												<input name="submit_button" style="margin-top: 20px;"
													type="submit" class="btn btn-success" id="submit_button"
													value="Go" /> <input type="hidden" name="submit_button"
													value="Submit" />
											</center>
										</div>
									</form>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>

			<div>
				<center>
					<%
						if (!mybean.StrHTML.equals("")) {
					%>
					<%=mybean.StrHTML%>
					<%
						} else {
					%>
					<font color="red"><strong>No Appointment's found!</strong></font>
					<%
						}
					%>
				</center>
			</div>
			<%
				if (!mybean.StrHTML.equals("")) {
			%>
			<div>
				<center>
					<b><%=mybean.ListLink%></b>
				</center>
				<br>
			</div>
			<%}%>

		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</html>
