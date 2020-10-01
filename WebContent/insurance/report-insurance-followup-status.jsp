<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.insurance.Report_Insurance_Followup_Status"
	scope="request" />
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

<body class="page-container-bg-solid page-header-menu-fixed"
	onload="convertmultiselect();">
	<%@include file="../portal/header.jsp"%>
	<!-- 	BODY -->
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
						<h1>Insurance Follow-up Status</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="../insurance/report-insurance-followup-status.jsp">Insurance Follow-up Status</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Insurance
										Follow-up Status</div>
								</div>
								<center>
									<font color="red"><b><%=mybean.msg%></b></font>
								</center>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="container-fluid">
												<div class="form-element2">
													<label> Executive: </label>
													<div>
														<%=mybean.misCheck.PopulateListInsuranceExecutives(
					mybean.exe_ids, mybean.ExeAccess, mybean.comp_id)%>
					
					
													</div>
												</div>

												<div class="form-element4">
													<label> Start Date<font color=red>*</font>:
													</label>
													<div>
														<input name="txt_starttime" id="txt_starttime" type="text"
															value="<%=mybean.start_time%>" size="12" maxlength="10"
															class="form-control datepicker" />
													</div>
												</div>

												<div class="form-element4">
													<label> End Date<font color=red>*</font>:
													</label>
													<div>
														<input name="txt_endtime" id="txt_endtime" type="text"
															value="<%=mybean.end_time%>" size="12" maxlength="10"
															class="form-control datepicker" />
													</div>
												</div>

												<div class="form-element12">
													<div>
														<center>
															<input type="submit" name="submit_button"
																id="submit_button" class="btn btn-success" value="Go">
														</center>
														<input type="hidden" name="submit_button" value="Submit">
													</div>
												</div>
											</div>
										</form>
									</div>
								</div>
							</div>
							<center><%=mybean.StrHTML%>
							</center>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/js.jsp"%>

</body>
</HTML>
