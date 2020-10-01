<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<jsp:useBean id="mybean" class="axela.sales.ManageTargetTransfer"
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
						<h1>Target Transfer</h1>
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
							<li><a href="managetargettransfer.jsp?all=yes">Target Transfer<b>:</b></a></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>


							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Target Transfer</div>
								</div>
								<div class="container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form method="post" class="form-horizontal" name="frm1" id="frm1">

											<div class="form-element12">
												<label> Brands:</label>
												<div>
													<select name="dr_principal" size="10" multiple="multiple"
														class="form-control multiselect-dropdown"
														id="dr_principal">
														<%=mybean.mischeck.PopulatePrincipal(mybean.brand_ids, mybean.comp_id, request)%>
													</select>
												</div>
											</div>


											<div class="form-element3">
												<label> From Year <font color=red>*</font>: </label>
												<select name="dr_from_year" class="form-control"
													id="dr_from_year">
													<%=mybean.PopulateYear(mybean.from_year, 0)%>
												</select>
											</div>

											<div class="form-element3">
												<label> From Month <font color=red>*</font>: </label>
												<select name="dr_from_month" class="form-control"
													id="dr_from_month">
													<%=mybean.PopulateMonth(mybean.from_month)%>
												</select>
											</div>

											<div class="form-element3">
												<label> To Year <font color=red>*</font>: </label>
												<select name="dr_to_year" class="form-control"
													id="dr_to_year">
													<%=mybean.PopulateYear(mybean.to_year, 1)%>
												</select>
											</div>

											<div class="form-element3">
												<label> To Month <font color=red>*</font>: </label>
												<select name="dr_to_month" class="form-control"
													id="dr_to_month">
													<%=mybean.PopulateMonth(mybean.to_month)%>
												</select>
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