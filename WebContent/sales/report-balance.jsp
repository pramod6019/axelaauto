<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Report_Balance" scope="request"/>
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
<body  class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Balance Dues</h1>
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
							<li><a href="../sales/report-balance.jsp">Balance Dues</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="red"><b> <%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">

							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Balance Dues</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form method="post" name="frm1" id="frm1" class="form-horizontal">

											<div class="form-element6">
												<label>Branches:
												<%
													if (mybean.branch_id.equals("0")) {
												%>
												</label>
												<div>
													<select name="dr_branch" id="dr_branch" class="form-control"
														onChange="PopulateExecutives();PopulateTeams();">
														<%=mybean.PopulateBranch(mybean.dr_branch_id, "all", "1,2", "", request)%>
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
											<div class="form-element6">
												<label >Month: </label>
												<div>
													<select name="drop_month" class="form-control" id="drop_month">
														<%=mybean.PopulateMonth()%>
													</select>
												</div>
											</div>
											<div class="form-element6">
												<label >Year: </label>
												<div>
													<select name="drop_year" class="form-control" id="drop_year">
														<%=mybean.PopulateYear()%>
													</select>
												</div>
											</div>

											<!-- 											============ -->
											<div class="form-element3">
												<label > Teams:</label>
												<div id="teamHint">
													<%=mybean.mischeck.PopulateTeams(mybean.branch_id, mybean.team_ids, mybean.comp_id, request)%>
												</div>
											</div>
											<div class="form-element3">
												<label>Sales Consultant:</label>
												<div id="exeHint">
													<%=mybean.mischeck.PopulateSalesExecutives(mybean.team_id, mybean.exe_ids, mybean.comp_id, request)%>
												</div>
											</div>

											<div class="form-element12">
												<center>
													<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
													<input type="hidden" name="submit_button" value="Submit" />
												</center>
											</div>
									</div>
									</form>
								</div>
							</div>
						</div>
					</div>

					<%
						if (!mybean.StrHTML.equals("")) {
					%>
					<div class="portlet box  ">
						<div class="portlet-title" style="text-align: left"></div>
						<div class="portlet-body portlet-empty">
							<div class="tab-pane" id="">
								<!-- START PORTLET BODY -->
								<center><%=mybean.StrHTML%></center>
							</div>
						</div>
					</div>
					<%
						}
					%>
				</div>
			</div>
		</div>
	</div>
	</div>


	<%@include file="../Library/admin-footer.jsp"%>
	
	<%@include file="../Library/js.jsp"%>

	<script type="text/javascript">

		 //This sholuldn't be removed because it is need branch and model
		function PopulateCRMDays(){ 
		}
		function PopulateVariants(){ 
		} 
		function PopulateColor(){ 
		} 
		//This sholuldn't be removed because it is need branch and model 

		function PopulateTeams() {
			var branch_id = outputSelected(document.getElementById("dr_branch").options);
			showHint('../sales/mis-check1.jsp?branch_id=' + branch_id + '&team=yes', 'teamHint');
		}

		function PopulateExecutives() { //v1.0
			var team_id = outputSelected(document.getElementById("dr_team").options);
			var branch_id = document.getElementById("dr_branch").value;
			if (branch_id == '0') {
				branch_id = '';
			}
			showHint('../sales/mis-check1.jsp?team_id=' + team_id + '&exe_branch_id=' + branch_id + '&executives=yes', 'exeHint');
		}
	</script>

</body>
</HTML>
