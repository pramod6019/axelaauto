<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Group_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="stylesheet" href="../assets/css/bootstrap-multiselect.css" type="text/css">
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css"/>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1><%=mybean.status%>&nbsp;Item Group
						</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
						<li><a href="inventory-group-list.jsp?all=yes">List Item
								Group</a> &gt;</li>
						<li><a
							href="inventory-group-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Item
								Group</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Item Group
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.<br>
											</font>
										</center>
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-group">
												<label class="control-label col-md-4"> Name<font
													color=red>*</font><b>:</b>
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_group_name" type="text"
														class="form-control" id="txt_group_name"
														value="<%=mybean.group_name%>" size="50" maxlength="255" />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Description:
												</label>
												<div class="col-md-6 col-xs-12">
													<textarea name="txt_group_desc" cols="50" rows="5"
														class="form-control" id="txt_group_desc"
														onkeyup="charcount('txt_group_desc', 'span_txt_group_desc','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.group_desc%></textarea>
													<span id=span_txt_group_desc> 255 characters </span>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> Item Group
													Type<font color="#ff0000">*</font><b>:</b>&nbsp;
												</label>
												<div class="col-md-6 col-xs-12">
													<select id="dr_group_type" name="dr_group_type"
														class="form-control">
														<%=mybean.PopulateGroupType()%>
													</select>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4"> After Tax: </label>
												<div class="txt-align">
													<input id="ch_group_aftertax" type="checkbox"
														name="ch_group_aftertax"
														<%=mybean.PopulateCheck(mybean.group_aftertax)%> />
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Active: </label>
												<div class="txt-align">
													<input id="ch_group_active" type="checkbox"
														name="ch_group_active"
														<%=mybean.PopulateCheck(mybean.group_active)%> />
												</div>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry by: </label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>" />
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry Date: </label>
												<div class="txt-align">
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified by: </label>
												<div class="txt-align">
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified Date:
												</label>
												<div class="txt-align">
													<%=mybean.modified_date%>
													<input type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>" />
												</div>
											</div>
											<%
												}
											%>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Group"
													onClick="return SubmitFormOnce(document.form1, this);" />
												<input type="hidden" name="add_button" value="yes">
													<%}else if (mybean.status.equals("Update")){%> <input
													type="hidden" name="update_button" value="yes"> <input
														name="button" type="submit" class="btn btn-success"
														id="button" value="Update Group"
														onClick="return SubmitFormOnce(document.form1, this);" />
														<input name="delete_button" type="submit"
														class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete Group" />
														<%}%>
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
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js"	type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js"	type="text/javascript"></script>
<script type="text/javascript" src="../assets/js/bootstrap-multiselect.js" ></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_group_name.focus()
	}
</script>
	</body>
</HTML>
