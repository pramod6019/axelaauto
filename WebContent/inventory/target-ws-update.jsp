<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Target_Ws_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="../assets/css/font-awesome.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet" type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet" id="style_components" type="text/css" />
<link rel="shortcut icon" type="image/x-icon" href="../admin-ifx/axela.ico">
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css" rel="stylesheet" type="text/css" />
<LINK REL="STYLESHEET" TYPE="text/css" HREF="../assets/css/footable.core.css">


<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Wholesale Target Update</h1>
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
						<li><a href="../inventory/index.jsp">Inventory</a>&gt;</li>
						<li><a href="../inventory/stock.jsp">Stock</a>&gt;</li>
						<li><a href="../inventory/stock.jsp">Stock</a> &gt; <a
							href="target-ws-list.jsp?dr_branch=<%=mybean.branch_id%>&dr_year=<%=mybean.year%>"><%=mybean.month_name%>-<%=mybean.year%></a>
							&gt; <a
							href="target-ws-list.jsp?dr_branch=<%=mybean.branch_id%>&dr_year=<%=mybean.year%>"><%=mybean.branch_name%></a>
							&gt; <a href="target-ws-update.jsp?<%=mybean.QueryString%>">Update
								Target</a>:
						<li>
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
									<div class="caption" style="float: none">Wholesale Target
										Update</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<div class="container-fluid">
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Model<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select id="dr_wstarget_model_id"
																name="dr_wstarget_model_id" class="form-control"
																onchange="GetOptionType(this.value);">
																<option value="0">Select</option>
																<%=mybean.PopulateModel()%>

															</select>
														</div>
													</div>
												</div>
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Fuel Type<font
															color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<select id="dr_wstarget_fueltype_id"
																name="dr_wstarget_fueltype_id" class="form-control"
																onchange="GetOptionType(this.value);">
																<option value="0">Select</option>
																<%=mybean.PopulateFuelType()%>
															</select>
														</div>
													</div>


												</div>
												<div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="control-label col-md-4">Wholesale
															Count<font color="#ff0000">*</font>:
														</label>
														<div class="col-md-6">
															<input name="txt_wstarget_count" type="text"
																id="txt_wstarget_count" class="form-control"
																value="<%=mybean.wstarget_count%>" size="10"
																maxlength="10"
																onkeyup="toInteger('txt_wstarget_count','Qty')" />
														</div>
													</div>
												</div>
											</div>
											
											<center>
											<input
																			name="update_button" type="submit" class="btn btn-success"
																			value="Add Target" onClick="AddTarget();" />
											</center>
										</form>
									</div>
								</div>
							</div>
							<div><%=mybean.StrHTML%></div>
						</div>
					</div>

<%@include file="../Library/admin-footer.jsp"%>
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<script type="text/javascript" src="../Library/smart.js"></script>
<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>
<script type="text/javascript">
	function isNumber(ob) {
		var invalidChar = /[^0-9]/gi
		if (invalidChar.test(ob.value)) {
			ob.value = ob.value.replace(invalidChar, "");
		}
	}
	function AddTarget() {
		document.form1.submit();
	}
</script>
					
</body>
</HTML>
