<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.customer.Customer_Group_Update"
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
<body onLoad="LoadRows();" class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="../portal/header.jsp"%>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-Wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Customer Group Update</h1>
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
							<li><a href="../inventory/index.jsp">Front Office</a> &gt;</li>
							<li><a href="customer-group-list.jsp?all=yes">List Groups</a> &gt;</li>
							<li><a><%=mybean.status%> Group:</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->


						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							
							<div class="portlet box">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Group
									</div>
								</div>
								
								<div class="container-fluid portlet-body">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										
										<form name="form1" method="post" class="form-horizontal">
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>

											<div class="form-element6 form-element-center">
												<label> Description<font color=red>*</font>: </label>
												<input name="txt_group_desc" type="text"
													class="form-control" id="txt_group_desc"
													value="<%=mybean.group_desc%>" size="50" maxlength="200">
											</div>

											<% if (mybean.status.equals("Update") && !(mybean.entry_by == null)
													&& !(mybean.entry_by.equals(""))) { %>
													
											<div class="form-element6 form-element-center">
												<div class="form-element6 form-element">
													<label> Entry By:</label>
													<%=mybean.entry_by%>
												</div>

												<% } %>

												<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>

												<div class="form-element6">
													<label> Entry Date: </label>
													<%=mybean.entry_date%>
												</div>
											</div>
											
											<% } %>
											<% if (mybean.status.equals("Update") && !(mybean.modified_by == null)
													&& !(mybean.modified_by.equals(""))) { %>

											<div class="form-element6 form-element-center">
												<div class="form-element6 form-element">
													<label> Modified By:</label>
													<%=mybean.modified_by%>
												</div>

												<% } %>

												<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>

												<div class="form-element6 ">
													<label> Modified Date: </label>
													<%=mybean.modified_date%>
												</div>
											</div>
											
											<% } %>
											
											<div class="form-element12"></div>
											
											<center>

												<% if (mybean.status.equals("Add")) { %>

												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Group" /> 
												<input type="hidden" name="add_button" value="Add Group">

												<% } else if (mybean.status.equals("Update")) { %>

												<input type="hidden" name="update_button"
													value="Update Group">
												<input name="button" type="submit" class="btn btn-success" id="button"
													value="Update Group" /> 
												<input name="delete_button"
													type="submit" class="btn btn-success" id="delete_button"
													onClick="return confirmdelete(this)" value="Delete Group" />
												<input type="hidden" name="Update" value="yes">

												<% } %>

												<input type="hidden" name="entry_by"
													value="<%=mybean.entry_by%>"> <input type="hidden"
													name="entry_date" value="<%=mybean.entry_date%>"> 
												<input type="hidden" name="modified_by"
													value="<%=mybean.modified_by%>"> <input
													type="hidden" name="modified_date"
													value="<%=mybean.modified_date%>">
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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</HTML>
