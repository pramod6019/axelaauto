<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Branch_Logo" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<%@include file="../Library/css.jsp"%>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Branch Logo</h1>
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
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="branch-list.jsp?all=yes">List Branches</a> &gt;</li>
							<li><a
								href="branch-list.jsp?branch_id=<%=mybean.branch_id%>"><%=mybean.branch_name%></a>
								&gt;</li>
							<li><a href="branch-logo.jsp?<%=mybean.QueryString%>">Upload
									Logo</a>:</li>
						</ul>

						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<div class="portlet box  ">
								<center>
									<font color="#ff0000"><b> <%=mybean.msg%></b></font>
								</center>
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>
										Logo
									</div>
								</div>

								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form EncType="multipart/form-data" name="frmupload"
											method="post">

											<center>
												<%
													if (mybean.branch_logo != null && !mybean.branch_logo.equals("")) {
												%>
												<img
													src="../Thumbnail.do?branchlogo=<%=mybean.branch_logo%>&width=500">
												<br>
												<%
													}
												%>
											</center>

											<center>
												<input name="filename" Type="file" class="btn btn-success"
													size="30">
											</center>

											<center>
												Click the Browse button to select the document from your
												computer!</br> Allowed Formats: <b><%=mybean.ImageFormats%></b>
											</center>

											<br />

											<center>
												<strong>
												 <% if (mybean.status.equals("Add")) { %> 
												
												<input type="submit" name="add_button" class="btn btn-success" value="Add Logo" />
												<input name="branch_id" type="hidden" id="branch_id" value="<%=mybean.branch_id%>"> 
												
												<% } else if (mybean.status.equals("Update")) { %>
												
												<input type="submit" name="update_button" class="btn btn-success" value="Update Logo" />
												<input type="submit" name="delete_button" class="btn btn-success" value="Delete Logo" onClick="return confirmdelete(this)" />
												
													<% } %>
												</strong>
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
<script type="text/javascript">
	function FormFocus() {
		document.frmupload.filename.focus()
	}
</script>
</body>
</html>
