<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Due"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Insurance Due</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-insurance-due.jsp">Insurance Due</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Insurance Due</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<!-- START PORTLET BODY -->
											<!-- FORM START -->
												<div class="form-element3">
													<label>Executive:</label>
													<div>
														<%=mybean.reportexe.PopulateDrInsuranceExecutives(mybean.exe_id, mybean.comp_id, mybean.ExeAccess)%>

													</div>
												</div>

												<!-- FORM START -->
												<div class="form-element3">
													<label> On: </label>
													<div>
														<select name="dr_date_type" class="form-control"
															id="dr_date_type">
															<%=mybean.PopulateDate()%>
														</select>

													</div>
												</div>
												
												<!-- FORM START -->
												<div class="form-element3">
													<label> Start Date<font
														color=red>*</font>:
													</label>
														<input name="txt_starttime" id="txt_starttime" type="text"
															value="<%=mybean.start_time%>" size="12" maxlength="10"
															class="form-control datepicker" />
												</div>

												<!-- FORM START -->
												<div class="form-element3">
													<label> End Date<font
														color=red>*</font>:
													</label>
														<input name="txt_endtime" id="txt_endtime" type="text"
															value="<%=mybean.end_time%>" size="12" maxlength="10"
															class="form-control datepicker" />
												</div>

												<!-- FORM START -->
												<div class="form-element12">
													
														<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" style="margin-top: 1px;" />
																<input type="hidden" name="submit_button" value="Submit" />
														</center>
														
												</div>

											</div>



										</form>
									</div>
								</div>
								<center><%=mybean.StrHTML%></center>
								<!-- 	PORTLET -->

							</div>
						</div>
					</div>
				</div>
			</div>

		</div>
		<!-- END CONTAINER -->
		<%@include file="../Library/js.jsp"%>
			<%@include file="../Library/admin-footer.jsp"%>
</body>
</html>