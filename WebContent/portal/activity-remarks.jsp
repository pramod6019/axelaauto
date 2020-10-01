<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Activity_Remarks"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp" %>
<script language="JavaScript" type="text/javascript">
function FormFocus() {
  document.frmstatus.txt_remarks.focus();
  }
        </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus();">
	<%@include file="header.jsp"%>
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
						<h1>Update Remarks</h1>
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
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="activity.jsp">Activities</a> &gt;</li>
						<li><a href="../portal/activity-list.jsp?all=yes">List
								Activities</a> &gt;</li>
						<li><%=mybean.activity_title%> &gt;</li>
						<li><a
							href="activity-remarks.jsp?activity_id=<%=mybean.activity_id%>">Update
								Remarks</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<div>
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
							</div>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Update Remarks</div>
								</div>
								<div class="portlet-body">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>

										<form name="frmstatus" class="form-horizontal" method="get">
											<div class="form-element6 form-element-center">
												<label>Remarks<font color=red>*</font>: </label>
													<textarea name="txt_activity_remarks" cols="50" rows="7"
														class="form-control"><%=mybean.activity_remarks%></textarea>
											</div>
											<% if (!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
											<div class="form-element6 form-element-center">
											<div class="form-element6">
												<label>Modified By <font color=red>*</font>: </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>">
											</div>
											<%}%>
											<% if (!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
											<div class="form-element6">
												<label>Modified Date
													<font color=red>*</font>:
												</label>
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>">
											</div>
											</div>
											<%}%>
											<div class="form-element6 form-element-center">
												<center>
													<input name="update_button" type="submit"
														class="btn btn-success" id="update_button" value="Update" />
													<input type="hidden" name="activity_id"
														value="<%=mybean.activity_id %>" /> <input type="hidden"
														name="employee_id" value="<%=mybean.employee_id %>" /> <input
														type="hidden" name="d" value="<%=mybean.d %>" />
												</center>
											</div>

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
	<%@include file="../Library/js.jsp" %>

</body>
</html>
