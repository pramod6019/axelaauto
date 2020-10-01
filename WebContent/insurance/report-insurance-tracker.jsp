<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Tracker"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@ include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- 	MULTIPLE SELECT END-->
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
						<h1>Insurance Tracker</h1>
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
						<li><a href="../insurance/report-insurance-tracker.jsp">Insurance
								Tracker<b>:</b></a></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Insurance
										Tracker</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<!-- FORM START -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<div class="form-element4">
												<label> Year: </label>
													<select name="dr_year" class="form-control" id="dr_year">
														<%=mybean.PopulateYears(mybean.comp_id)%>
													</select>
											</div>

											<!-- FORM START -->
											<div class="form-element4">
												<label> Month: </label>
													<div id="month"> 
													<select name="dr_month" id="dr_month" class="form-control">
															<%=mybean.PopulateMonths(mybean.comp_id, mybean.dr_month)%>
													</select>
													</div>
											</div>

											<!-- FORM START -->
											<div class="form-element4">
												<label> Executive: </label>
													<%=mybean.PopulateListInsuranceExecutives(mybean.comp_id, mybean.ExeAccess)%>
											</div>

											<!-- FORM START -->
											<div class="form-element12">
													<center>
														<input name="submit_button" type="submit"
															class="btn btn-success" id="submit_button" value="Go" style="margin-top: 1px;"/>
													</center>
													<input type="hidden" name="submit_button" value="Submit" />
											</div>
									</div>
									</form>
								</div>
							</div>
							<!-- 	PORTLET -->
							<center><%=mybean.StrHTML%>
							</center>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>

</body>
</html>