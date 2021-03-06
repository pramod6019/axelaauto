<%@ page errorPage="../portal/error-page.jsp"%>

<jsp:useBean id="mybean" class="axela.inventory.Stock_User_Import_Maruti" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../Library/css.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#frmdoc").submit(function() {
			//alert("exe.nvbnv.");
			//return true;
			$('#dialog-modal').dialog('open');
			//return true;
		})

		// //Dialog
		$('#dialog-modal').dialog({
			autoOpen : false,
			width : 200,
			height : 120,
			//zIndex: 200,
			modal : true,
			title : "File Upload in progress!",
			draggable : false,
			closeOnEscape : false,
			open : function(event, ui) {
				$(".ui-dialog-titlebar-close").hide();
			}
		});
	});
</script>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.dr_branch.focus();
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed" onload="FormFocus()">
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
						<h1>Maruti Stock Import</h1>
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
						<li><a href="index.jsp">Inventory</a> &gt;</li>
						<li><a href="../inventory/stock-user-import.jsp">Import
								Stock</a> &gt;</li>
						<li><a href="../inventory/stock-user-import-maruti.jsp">Maruti
								Stock Import</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
					</div>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form class="form-horizontal" name="frmdoc" id="frmdoc"
								enctype="MULTIPART/FORM-DATA" method="post"
								onsubmit="aletdisp();">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Download the Stock template file</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing Stock by downloading template file. <br />
												Download the template and enter Stock data as per the
												headings. <br /> Headings marked in red are manadatory.
												Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/stock-template-maruti.xlsx"
													target="_blank"><b>Click here to download Stock
														Template Maruti</b></a>
											</center>
											<br>
										</div>
									</div>
								</div>
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Stock</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
<!-- 											<div class="form-element6 form-element-center"> -->
<!-- 												<label>Branch<font color=red>*</font>: </label> -->
<!-- 												<select name="dr_branch_id" id="dr_branch_id" -->
<!-- 													class="form-control" style="margin-top: 9px;" onchange="PopulateLocation()"> -->
<%-- 													<%=mybean.PopulateStockBranch(mybean.comp_id)%> --%>
<!-- 												</select> -->
<!-- 											</div> -->
<!-- 											<div class="form-element6 form-element-center"> -->
<!-- 												<label>Location<font color=red>*</font>: </label> -->
<!-- 												<div id="dr_location"> -->
<%-- 													<%=mybean.PopulateStockLocation(mybean.comp_id)%> --%>
<!-- 												</div> -->
<!-- 											</div> -->
											
											<div class="form-element6 form-element-center">
												<label>Select Document<font color=red>*</font>: </label>
												 <input name="filename" type="file" class="button btn btn-success"
												  	id="filename" value="<%=mybean.doc_value%>" size="30">
											</div>

											<div class="form-element6 form-element-center">
												<font size="">Click the Browse button to select the document from your computer!</font>
											</div>
											<div class="form-element6 form-element-center">
												Allowed Formats: <b><%=mybean.importdocformat%></b>
											</div>
											<div class="form-element6 form-element-center">
												Maximum Size: <b><%=mybean.docsize%> Mb</b>
											</div>
											<div align="center">
												<input name="addbutton" type="submit" class="button btn btn-success" id="addbutton" value="Upload" />
												<input name="add_button1" type="hidden" class="button" id="add_button" value="Upload" />
												<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%></body>
	<script language="JavaScript" type="text/javascript">
	function PopulateLocation() {
		var branch_id = document.getElementById("dr_branch_id").value;
		showHint('../inventory/inventory-check.jsp?vehstock_branch_id=' + branch_id + '&brand_id=2'+ '&branch_location=yes' , 'dr_location');
	}
</script>
</html>
