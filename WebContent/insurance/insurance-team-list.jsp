<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Team_List" scope="request" />
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
<%@ include file="../Library/css.jsp"%>
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
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Insurance Team</h1>
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
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<li><a href="../insurance/insurance-team.jsp">Insurance Teams</a> &gt;</li>
						<li><a href="insurance-team-list.jsp">Insurance List Teams</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div>
								<center>
									<font color="#ff0000"><b><%=mybean.msg%> </b></font>
								</center>
							</div>
							<form name="form1" method="get" class="form-horizontal">

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>

									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6 form-element-center">
												<br> <label>Branch<font color=red>*</font>: </label>
														<select name="dr_branch" class="form-control"
															id="dr_branch" onChange="document.form1.submit()">
															<%=mybean.PopulateBranch(mybean.insurteam_branch_id, "", "6", "", request)%>
														</select>
											</div>
											<div>
												<%
													if (!mybean.insurteam_branch_id.equals("0")) {
												%>
												<div align="right" style="margin-right: 7%">
													<a href="../insurance/insurance-team-update.jsp?add=yes&dr_branch=<%=mybean.insurteam_branch_id%>">Add
														Team...</a>
												</div>
												<%
													}
												%>

											</div>

										</div>
									</div>
								</div>

								<center><%=mybean.StrHTML%></center>
							</form>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>

	<script type="text/javascript">
		function populatefollowup(preowned_id){
			if(document.getElementById("followup_"+preowned_id).innerHTML==""){
				showHint('../insurance/insurance-followup.jsp?preowned_id='+preowned_id,  'followup_'+preowned_id+'');
			}
		}
	</script>
</body>
</HTML>
