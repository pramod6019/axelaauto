<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_Target_List" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%--     <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css"> --%>
<%@include file="../Library/css.jsp"%>
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
						<h1>List Targets</h1>
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
							<li><a href="../portal/home.jsp">Home</a>&gt;</li>
							<li><a href="../insurance/index.jsp">Insurance</a>&gt;</li>
							<li><a href="insurance-target.jsp">Target</a>&gt;</li>
							<li><a href="insurance-target-list.jsp?<%=mybean.QueryString%>">List Targets</a><b>:</b></li>
						</ul>

						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="form1" method="get" class="form-horizontal">
								<div>
									<center>
										<font color="#ff0000"><b><%=mybean.msg%></b></font><br>
									</center>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>
									</div>
									<div class="container-fluid portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

												<div class="form-element6">
													<label> Executive<font color=red>*</font>:&nbsp; </label>
													 <span id="executive"> <%=mybean.PopulateEmp(mybean.branch_id, mybean.comp_id)%> </span>
												</div>

												<div class="form-element6">
													<label>Year<font color=red>*</font>:&nbsp; </label>
													<select name="dr_year" class="form-control" id="dr_year" onChange="document.form1.submit()">
														<%=mybean.PopulateYear()%>
													</select>
												</div>
										</div>
									</div>
								</div>
								<!-- START PORTLET BODY -->
								<center><%=mybean.StrHTML%></center>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
<script type="text/javascript"> -->
	function PopulateExecutive(){ 
	var branch_id = document.getElementById('dr_branch').value;
 	showHint('../service/mis-check1.jsp?exe=yes&service_branch_id=' + branch_id, 'executive')}
</script>
</body>
</HTML>
