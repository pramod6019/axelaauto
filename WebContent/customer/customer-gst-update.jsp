<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.gst.Gst_Update" scope="page" />
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

<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
	<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/select2.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/select2-bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.txt_doc_name.focus()
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
<%-- 	<% --%>
<!-- // 		if (mybean.group.equals("")) { -->
<%-- 	%> --%>
<%-- 	<%@include file="header.jsp"%> --%>
<%-- 	<% --%>
<!-- // 		} -->
<%-- 	%> --%>

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
<%-- 						<h1><%=mybean.status %> Document</h1> --%>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<ul class="page-breadcrumb breadcrumb">
<%-- 						<%=mybean.LinkHeader%> --%>
				</ul>
				
				
					<center>
						
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#FF0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="frmdoc"
											enctype="MULTIPART/FORM-DATA" method="post">
											
											<div class="form-group">
												<label class="control-label col-md-4">Name:&nbsp;
												</label>
												<div class="txt-align">
													<b><%=mybean.customer_name %></b>
												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4">Address:&nbsp;
												</label>
												
												<div class="col-md-6 col-xs-12">
													<textarea name="txt_customer_address" cols="40" rows="4"
														class="form-control" id="txt_customer_address"
														onKeyUp="charcount('txt_customer_address', 'span_txt_customer_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.customer_address%></textarea>
													<span id="span_txt_customer_address"> (255
														Characters)</span>
												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4">City:&nbsp;
												</label>
												<div class="col-md-6 col-xs-12">
													<select class="form-control-select select2" id="maincity"
														name="maincity">
														<%=mybean.citycheck.PopulateCities(mybean.customer_city_id,
					mybean.comp_id)%>
													</select>

												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4">PAN No:&nbsp;
												</label>
												<div class="col-md-6">
														<input name="txt_customer_pan_no" type="text"
															class="form-control" id="txt_customer_pan_no"
															value="<%=mybean.customer_pan_no%>" size="10"
															maxlength="10"></input><b>Format: ABCDF1234F</b>
													</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4">GST Reg Date<font color=red>*</font>:&nbsp;</label>
												<div class="col-md-6">
													<input name="txt_customer_gst_regdate" id="txt_customer_gst_regdate"
														value="<%=mybean.gst_regdate%>"
														class="form-control date-picker"
														data-date-format="dd/mm/yyyy" type="text" maxlength="10" />
												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4">GST No<font color=red>*</font>:&nbsp;
												</label>
												<div class="col-md-6 col-xs-12">
													<input name="txt_customer_gst_no" type="text"
														class="form-control" id="txt_customer_gst_no"
														value="<%=mybean.customer_gst_no%>" size="50"
														maxlength="255">
												</div>
											</div>
											
											<div class="form-group">
												<label class="control-label col-md-4">Select
													Document :&nbsp;
												</label>
												<div class="col-md-6 col-xs-12" style="top:-8px">
													<input NAME="filename" Type="file" class="btn btn-success" id="filename" value="<%=mybean.gst_doc_value%>" size="34">
														<span>Click the Browse button to select the
												document from your computer</span>
												</div>

											</div>
											
											
											
											
											
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry By:</label>
												<div class="txt-align">
													<%=mybean.entry_by%>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Entry Date: </label>
												<div class="txt-align">
													<%=mybean.entry_date%>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified By:</label>
												<div class="txt-align">
													<%=mybean.modified_by%>
												</div>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Modified Date:</label>
												<div class="txt-align">
													<%=mybean.modified_date%>
												</div>
											</div>
											<%
												}
											%>
											<center>
												
  <input name="add_button" type="submit" class="btn btn-success" value="Add GST" />
<!--    <input name="add_button" type="submit" class="btn btn-success" value="Add Document" />  -->

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
	<!-- END CONTAINER ------>
<%-- 	<%@include file="../Library/admin-footer.jsp"%> --%>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
		<script src="../assets/js/components-date-time-pickers.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-select2.min.js"
		type="text/javascript"></script>
	
	<script>
	$(function() {
		$("#txt_customer_gst_regdate").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
		
	});
</body>
</HTML>
