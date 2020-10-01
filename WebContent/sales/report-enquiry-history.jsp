<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Enquiry_History" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Enquiry History</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY ----->
			<div class="page-content">
					<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-enquiry-history.jsp">Enquiry History</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="red"><b> <%=mybean.msg%></b></font>
					</center>
						<div class="tab-pane" id="">

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Enquiry History
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="row">
												<div class="form-element4">
													<label >Branch:</label>
													<div>
														<%
															if (mybean.branch_id.equals("0")) {
														%>
														<select name="dr_branch" class="form-control" id="dr_branch" onChange="PopulateExe();">
															<%=mybean.PopulateBranch(mybean.dr_branch_id, "", "1,2", "", request)%>
														</select>
														<%
															} else {
														%>
														<input type="hidden" name="dr_branch" id="dr_branch" value="<%=mybean.branch_id%>" />
														<%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
														<%
															}
														%>
													</div>
												</div>
												
												<div class="form-element4">
													<label>Date:</label>
													<div>
														<input name="txt_history_date" id="txt_history_date" value="<%=mybean.history_date%>"
															class="form-control datepicker" type="text" maxlength="10" />
													</div>
												</div>
												
												<div class="form-element4">
													<label >Sales Consultant:</label>
													<div>
														<div id="exeHint">
															<%=mybean.PopulateSalesExecutive()%>
														</div>
													</div>
												</div>
											</div>
											<div class="form-element4">
												<label >Enquiry ID:</label>
												<div>
													<input name="txt_enquiry_id" type="text" class="form-control" id="txt_enquiry_id"
														value="<%=mybean.enquiry_id%>" size="10" maxlength="10">
												</div>
											</div>
											
											<div class="form-element4">
												<label >DMS No.:</label>
												<div>
													<input name="txt_enquiry_dmsno" type="text" class="form-control" id="txt_enquiry_dmsno"
														value="<%=mybean.enquiry_dmsno%>" size="10" maxlength="50" />
												</div>
											</div>

											<div class="form-element12">
												<center>
													<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Search" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>

											<div>
												<%=mybean.StrHTML%>
											</div>
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
	
	<script type="text/javascript">
		function PopulateExe() {
			var branch_id = document.getElementById("dr_branch").value;
			showHint('../sales/mis-check1.jsp?history=yes&single=yes&exe_branch_id=' + branch_id, 'exeHint');
		}
	</script>
</body>
</html>
    
         