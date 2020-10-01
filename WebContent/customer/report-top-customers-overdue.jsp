<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.customer.Report_Top_Customers_Overdue" scope="request" />
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
<%@include file="../Library/css.jsp" %>
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
						<h1>Top Customers Overdue</h1>
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
						<li><a href="report-top-customers-overdue.jsp">Top
								Customers Overdue</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Top Customers
										Overdue</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1"
											class="form-horizontal">
											<%
												if (mybean.branch_id.equals("0")) {
											%>
											<div class="form-element3">
												<label>Branch:</label> <select name="dr_branch"
													id="dr_branch" class="form-control"
													onChange="ExeCheck();PopulateTeam();">
													<%=mybean.PopulateBranch(mybean.dr_branch_id, "all", "", "", request)%>
												</select>
											</div>
											<%
												} else {
											%>
											<input type="hidden" name="dr_branch" id="dr_branch"
												value="<%=mybean.branch_id%>" />
											<%
												}
											%>

											<div class="form-element3">
												<label>Teams:</label>
												<div id="multiteam">
													<select name="dr_team" multiple="multiple"  
														class="form-control multiselect-dropdown" id="dr_team" onChange="ExeCheck();">
														<%=mybean.PopulateTeam(mybean.comp_id)%>
													</select>
												</div>
											</div>
											<div class="form-element3">
												<label>Executive:</label> <div id="exeHint"><%=mybean.PopulateSalesExecutives(mybean.comp_id)%></div>
											</div>
											<div class="form-element3">
												<label></label>
												<center>
													<input name="submit_button" type="submit"
														class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit">
												</center>
											</div>
										</form>
									</div>
								</div>
							</div>
							<center><%=mybean.StrHTML%></center>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
	<script language="JavaScript" type="text/javascript">
	function PopulateTeam() {
		var branch_id = document.getElementById("dr_branch").value;
		//alert("branch_id=11=="+branch_id);  
		showHint('../sales/report-team-check.jsp?team=yes&exe_branch_id='
				+ branch_id, 'multiteam');
	}
	function ExeCheck() { //v1.0    
		var branch_id = document.getElementById("dr_branch").value;
		var team_id = outputSelected(document.getElementById("dr_team").options);
		showHint('../sales/mis-check.jsp?multiple=yes&team_id=' + team_id
				+ '&exe_branch_id=' + branch_id, 'exeHint');
	}
</script>
</body>
</HTML>
