<%-- 
    Document : quote-check
    Created on: Sep 17, 2012, 4:49:28 PM
    Author   : Ajit
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Movement_Print_PDF"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
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
							<h1>Vehicle IN</h1>
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
							<li><a href="movement-print.jsp">Vehicle Movement</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
					
							<div class="tab-pane" id="">
								<!-- 					BODY START -->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Vehicle IN</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<form name="form1" method="post" class="form-horizontal">
												<div class="container-fluid ">
													<div class="form-element4">
														<label>Branch<font color="#ff0000">*</font>:
														</label>
														<%
															if (mybean.branch_id.equals("0")) {
														%>
														<select name="dr_branch_id" class="form-control"
															id="dr_branch_id">
															<%=mybean.PopulateBranch()%>
														</select>
														<%
															} else {
														%>
														<input type="hidden" id="dr_branch_id" name="dr_branch_id"
															value="<%=mybean.branch_id%>" />
														<%=mybean.getBranchName(mybean.branch_id, mybean.comp_id)%>
														<%
															}
														%>
													</div>
													<div class="form-element4">
														<label>From Date <font color=red><b> *</b></font>:
														</label> <input name="txt_start_time" id="txt_start_time" value=""
															class="form-control datepicker" type="text"
															value="<%=mybean.start_time%>" />
													</div>
													<div class="form-element4">
														<label>To Date<font color=#ff0000><b>
																	*</b></font>:
														</label> <input name="txt_end_time" id="txt_end_time" value=""
															class="form-control datepicker" type="text"
															value="<%=mybean.end_time%>" />
													</div>
												</div>
												<center>
													<input name="print_button" type="submit"
														class="btn btn-success" id="print_button" value="Print In" />
													<input name="print_button" type="submit"
														class="btn btn-success" id="print_button"
														value="Print Out" />
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
		<!-- END CONTAINER -->
		<%@include file="../Library/js.jsp"%>
		<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>