<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_Generate"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>
<body onload="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">

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
						<h1>Generate Enquiry Form</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="enquiry-generate.jsp">Generate Enquiry Form</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Generate Enquiry
										Form</div>
								</div>
								<div class="container-fluid portlet-body">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<font color=#ff0000>*</font> are required.
											</font>
										</center>

										<form name="formcontact" method="post" class="form-horizontal">

											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
												<select name="dr_branch_id" class="form-control"
													id="dr_branch_id"
													onChange="showHint('../sales/executives-check.jsp?lead_branch_id=' + GetReplace(this.value)+'&dr=dr_lead_emp_id','dr_emp_id')">
													<%=mybean.PopulateBranch(mybean.branch_id, "", "1,2", "", request)%>
												</select>
											</div>

											<div class="form-element6">
												<label> Sales Consultant<font color=red>*</font>: </label>
												<select id="dr_emp_id" name="dr_emp_id"
													class="form-control">
													<%=mybean.PopulateExecutive()%>
												</select>

											</div>

											<% if (mybean.config_sales_soe.equals("1")) { %>

											<div class="form-element6 form-element-center">
												<label> Source Of Enquiry<font color=red>*</font>: </label>
												<select id="dr_lead_soe_id" name="dr_lead_soe_id"
													class="form-control">
													<%=mybean.PopulateSoe()%>
												</select>

											</div>

											<% } %>

											<% if (mybean.config_sales_sob.equals("1")) { %>

											<div class="form-element6 form-element-center">
												<label> Source Of Business<font color=red>*</font>: </label>
												<select id="dr_lead_sob_id" name="dr_lead_sob_id"
													class="form-control">
													<%=mybean.PopulateSob()%>
												</select>

											</div>

											<% } %>

											<center>

												<input type="submit" name="generate_form" id="generate_form"
													class="btn btn-success" value="Generate Form">
											</center>

											<% if (!mybean.FormHTML.equals("")) { %>

											<div class="form-element6 form-element-center">
												<textarea name="textarea" id="textarea" cols="70" rows="20"
													class="form-control"><%=mybean.FormHTML%></textarea>
											</div>

											<% } %>
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

	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

</body>
</html>
