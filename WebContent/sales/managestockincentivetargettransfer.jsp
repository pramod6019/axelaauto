<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="mybean" class="axela.sales.ManageStockIncentiveTargetTransfer"
	scope="request" />

<% mybean.doGet(request, response); %>

<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<%@include file="../Library/css.jsp"%>
<body class="page-container-bg-solid page-header-menu-fixed" onLoad="FormFocus()">

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
						<h1>Incentive Target Transfer</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managetargettransfer.jsp?all=yes">Incentive Target Transfer<b>:</b></a></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>


							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Incentive Target Transfer</div>
								</div>
								<div class="container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" class="form-horizontal" name="frm1" id="frm1">

											<div class="row">
											<div class="form-element3">
												<label>Brand<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_brand" class="form-control"
														id="dr_brand" onChange="document.form1.submit()">
														<%=mybean.PopulatePrincipal(mybean.brand_id, mybean.comp_id, request)%>
													</select>
												</div>
											</div>

<!-- 											<div class="row"></div> -->
												
											<div class="form-element2">
												<label> From Year <font color=red>*</font>: </label>
												<select name="dr_from_year" class="form-control"
													id="dr_from_year">
													<%=mybean.PopulateYear(mybean.from_year, 0)%>
												</select>
											</div>

											<div class="form-element2">
												<label> From Month <font color=red>*</font>: </label>
												<select name="dr_from_month" class="form-control"
													id="dr_from_month">
													<%=mybean.PopulateMonth(mybean.from_month)%>
												</select>
											</div>

											<div class="form-element2">
												<label> To Year <font color=red>*</font>: </label>
												<select name="dr_to_year" class="form-control"
													id="dr_to_year">
													<%=mybean.PopulateYear(mybean.to_year, 1)%>
												</select>
											</div>

											<div class="form-element2">
												<label> To Month <font color=red>*</font>: </label>
												<select name="dr_to_month" class="form-control"
													id="dr_to_month">
													<%=mybean.PopulateMonth(mybean.to_month)%>
												</select>
											</div>
											</div>


											<center>
												<input type="submit" name="submit_button" id="submit_button"
													class="btn btn-success" value="Go" />
											</center>
											<input type="hidden" name="submit_btn" value="Submit" />

										</form>
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

</body>

</html>