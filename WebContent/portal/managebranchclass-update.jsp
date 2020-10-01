<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Managebranchclass_Update"
	scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script language="JavaScript" type="text/javascript">

function FormFocus() { //v1.0
  document.form1.txt_rateclass_name.focus()
}
        </script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">

	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;Rate Class
						</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="managebranchclass.jsp?all=yes">List Rate
								Classes</a> &gt;</li>
						<li><a
							href="managebranchclass-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Rate
								Class</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;Rate
										Class
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal">
											<div class="form-group">
												<label class="control-label col-md-4">Name <font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_rateclass_name" type="text"
														class="form-control" value="<%=mybean.rateclass_name %>"
														size="50" maxlength="255" />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Type<font
													color="#ff0000">*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<select name="dr_rateclass_type" class="form-control"
														id="dr_rateclass_type">
														<%=mybean.PopulateType(mybean.rateclass_type)%>
													</select>
												</div>
												</td>
												<div class="form-group">
													<center>
														<%if(mybean.status.equals("Add")){%>
														<input name="button" type="submit" class="btn btn-success"
															id="button" value="Add Rate Class"
															onClick="return SubmitFormOnce(document.form1, this);" />
														<input type="hidden" name="add_button" value="yes">
															<%}else if (mybean.status.equals("Update")){%> <input
															type="hidden" name="update_button" value="yes"> <input
																name="button" type="submit" class="btn btn-success"
																id="button" value="Update Rate Class"
																onClick="return SubmitFormOnce(document.form1, this);" />
																<input name="delete_button" type="submit"
																class="btn btn-success" id="delete_button"
																OnClick="return confirmdelete(this)"
																value="Delete Rate Class" /> <%}%>
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

	<%@include file="../Library/admin-footer.jsp"%></body>
</HTML>
