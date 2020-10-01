<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Itemprice_User_Import"
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
<%@include file="../Library/css.jsp"%>
<script type="text/javascript">

		// Dialog
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
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed"
	onload="FormFocus()">
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
						<h1>Import Variant Price</h1>
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
						<li><a href="stock.jsp">Stock</a> &gt;</li>
						<li><a href="itemprice-user-import.jsp">Import Variant Price</a><b>:</b></li>
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
										<div class="caption" style="float: none">Download the Variant Price template file</div>

									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing Variant Prices by downloading template file. <br />
                            Download the template and enter Variant Price data as per the headings. <br />
                            Headings marked in red are manadatory. Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/variantprice.xlsx" target="_blank"><b>Click here to download Variant Price template</b></a>
											</center>
											<br>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Variant Price</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6 form-element-center">
												<label>Rate Class<font color=red>*</font>:
												</label> <select name="dr_branch" id="dr_branch"
													class="form-control" style="margin-top: 9px;">
													<%=mybean.PopulateRateclass(mybean.comp_id)%>
												</select>
											</div>
											<div class="form-element6 form-element-center">
												<label>Effective From<font color=red><b>*</b></font>:</label> 
												<input name="txt_effective_from"
													id="txt_effective_from" value=""
													class="form-control datepicker" type="text"
													value='<%=mybean.effectivefrom%>' style="margin-top: 9px;" />
											</div>

											<div class="form-element6 form-element-center">
												<label>Select Document<font color=red>*</font>: </label>
													<input NAME="filename" Type="file"
														class="button btn btn-success" id="filename"
														value="<%=mybean.doc_value%>" size="30">
											</div>

												<div class="form-element6 form-element-center">
												<font size="">Click the Browse button to select the
													document from your computer!</font>
											</div>
												<div class="form-element6 form-element-center">
												Allowed Formats: <b><%=mybean.importdocformat%></b>
											</div>
												<div class="form-element6 form-element-center">
												Maximum Size: <b><%=mybean.docsize%> Mb</b>
											</div>
											<div align="center">
												<input name="addbutton" type="submit"
													class="button btn btn-success" id="addbutton"
													value="Upload" /> <input name="add_button1" type="hidden"
													class="button" id="add_button" value="Upload" /><input
													name="add_button2" type="hidden" class="button"
													id="add_button3" value="Upload" />
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
	<%@include file="../Library/js.jsp"%>
	</body>
</html>
