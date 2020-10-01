<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Manage_UOM_Update" scope="request"/>
<%mybean.doGet(request,response); %>
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
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css"/>
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
  <%@include file="../portal/header.jsp" %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;UOM</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href="../portal/manager.jsp">Business Manager</a> &gt; </li>
						<li><a href="manage-uom-list.jsp?all=yes">List UOM</a>&nbsp;&gt; </li>
						<li><a href="manage-uom-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;UOM</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
					<font color="#ff0000" ><b><%=mybean.msg%></b></font><br>
					</center>
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;UOM</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" class="form-horizontal" method="post">
										<center><font>Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
                    </font></center>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Name<font color="#ff0000">*</font>:</label>
												<div class="col-md-6">
													<input type="text" class = "form-control" name="txt_uom_name" id="txt_uom_name" value = "<%=mybean.uom_name%>">
                                                </div>
											</div>
										</div>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Short Name<font color="#ff0000">*</font>:</label>
												<div class="col-md-6">
													<input type="text" class = "form-control" name="txt_uom_shortname" id="txt_uom_shortname" value = "<%=mybean.uom_shortname%>"></td>
                                                </div>
											</div>
										</div>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Parent UOM:</label>
												<div class="col-md-6">
													<select name="dr_uom_parent_id" id="dr_uom_parent_id" class="form-control">
                      <%=mybean.PopulateParentUOM()%>
                    </select></div>
											</div>
										</div>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Ratio:</label>
												<div class="col-md-6">
													<input type="text" class="form-control" name="txt_uom_ratio" id="txt_uom_ratio" value="<%=mybean.uom_ratio%>"></td>
                                                 </div>
											</div>
										</div>
										<% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Entry By:</label>
												<div class="col-md-6" style="top:8px">
													<%=mybean.unescapehtml(mybean.entry_by)%></div>
											</div>
										</div>
										<%} %>
										<% if (mybean.status.equals("Update") &&!(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Entry Date:</label>
												<div class="col-md-6" style="top:8px">
													<%=mybean.entry_date%>
													</div>
											</div>
										</div>
										
										<%} %>
										<% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Modified By:</label>
												<div class="col-md-6" style="top:8px">
												<%=mybean.unescapehtml(mybean.modified_by)%>
													</div>
											</div>
										</div>
										<%} %>
										<% if (mybean.status.equals("Update") &&!(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
										<div class="form-body">
											<div class="form-group">
												<label class="col-md-3 control-label">Modified Date:</label>
												<div class="col-md-6" style="top:8px">
												<%=mybean.modified_date%>
													</div>
											</div>
										</div>
										<%} %>
										<center><%if(mybean.status.equals("Add")){%>
                    <input name="button" type="submit" class="btn btn-success" id="button" value="Add UOM"  onClick="return SubmitFormOnce(document.form1, this);" />
                    <input type="hidden" name="add_button" value="yes">
                    <%}else if (mybean.status.equals("Update")){%>
                    <input type="hidden" name="update_button" value="yes">
                    <input name="button" type="submit" class="btn btn-success" id="button" value="Update UOM"  onClick="return SubmitFormOnce(document.form1, this);" />
                    <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" onClick="return confirmdelete(this)" value="Delete UOM"/>
                    <input type="hidden" name="Update" value="yes">
                    <%}%>
                    <input type="hidden" name="entry_by" value="<%=mybean.entry_by%>"/>
                    <input type="hidden" name="entry_date" value="<%=mybean.entry_date%>"/>
                    <input type="hidden" name="modified_by" value="<%=mybean.modified_by%>"/>
                    <input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"/></center>
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

<%@include file="../Library/admin-footer.jsp" %>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../assets/js/components-date-time-pickers.js"	type="text/javascript"></script>
<script src="../assets/js/bootstrap-datetimepicker.js"	type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
</body>
</HTML>
