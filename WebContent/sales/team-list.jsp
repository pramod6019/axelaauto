<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Team_List" scope="request" />
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
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>List Team</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="../sales/team.jsp">Teams</a>&gt;</li>
						<li><a href="team-list.jsp">List Team</a><b>:</b></li>
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
											<div class="form-element4 form-element-center">
												<label>Branch<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_branch" class="form-control"
														id="dr_branch" onChange="document.form1.submit()">
														<%=mybean.PopulateBranch(mybean.branch_id, "", "1,2", "", request)%>
													</select>
												</div>
											</div>
											<div>
												<%
													if (!mybean.branch_id.equals("0")) {
												%>
												<div align="right" style="margin-right: 7%">
													<a href="../sales/team-update.jsp?add=yes&dr_branch=<%=mybean.branch_id%>">Add Team...</a>
												</div>
												<%
													}
												%>

											</div>

										</div>
									</div>
								</div>


								<!--<div class="portlet box  "> -->
								<!--<div class="portlet-title" style="text-align: center"> -->
								<!--	<div class="caption" style="float: none"> -->
								<!--		&nbsp;  -->
								<!--	</div> -->
								<!--</div> -->
								<!--<div class="portlet-body portlet-empty"> -->
								<!--	<div class="tab-pane" id=""> -->
								<!-- START PORTLET BODY -->
								<center><%=mybean.StrHTML%></center>

								<!--	</div> -->
								<!--</div> -->
								<!-- 			</div> -->



							</form>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</HTML>
