<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.ManagePreownedVariant_ServiceCode_Update" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>

</head>
<body class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
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
						<h1><%=mybean.status%> Pre-Owned Variant Service Code</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managepreownedvariant-servicecode.jsp?all=yes">List Pre Owned Variant Service Code</a> &gt;</li>
							<li><a href="managepreownedvariant-servicecode-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%> Pre Owned Variant Service Code</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
<!-- 	PORTLET -->
			<div class="portlet box">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">
								<%=mybean.status%> Pre-Owned Variant Service Code
							</div>
						</div>
						<div class="portlet-body portlet-empty">
							<div class="tab-pane" id="">
								<!-- START PORTLET BODY -->
								<form name="form1"  method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color="red">*</font> are required.</font>
											</center>
											<div class="form-element6 form-element-center">
												<label>Manufacturer <font color="#ff0000">*</font>:</label>
														<%=mybean.PopulatePreownedManufacturer()%>
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Model <font color="#ff0000">*</font>:</label>
												<div id="modelHint">
														<%=mybean.PopulatePreownedModel()%>
												 </div>
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Variant <font color="#ff0000">*</font>:</label>
												<div id="variantHint">	
														<%=mybean.PopulatePreownedVariant()%>
 												</div>	 
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Service Code <font color="#ff0000">*</font>:</label>
													<input name ="txt_servicecode_code" type="txt_servicecode_code" class="form-control" 
													value="<%=mybean.servicecode_code%>" />
											</div>

											<center>
											<%if(mybean.status.equals("Add")){%>
						                      <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Pre Owned Variant Service Code" onClick="return SubmitFormOnce(document.form1, this);" />
						                      <input type="hidden" id="add_button" name="add_button" value="yes"/>
						                      <%}else if (mybean.status.equals("Update")){%>
						                      <input type="hidden" id="update_button" name="update_button" value="yes"/>
						                      <input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Pre Owned Variant Service Code" onClick="return SubmitFormOnce(document.form1, this);"  />
						                      <input name="delete_button" type="submit" class="btn btn-success" id="delete_button" OnClick="return confirmdelete(this)" value="Delete Pre Owned Variant Service Code" />
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
 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp" %>

<script language="JavaScript" type="text/javascript">
function PopulateModel(){
	var manuf_id = document.getElementById("dr_manufacturer").value;
	if(manuf_id != ""){
		showHint('../preowned/managepreownedvariant-servicecode-check.jsp?manuf_id=' + manuf_id + '&manufacturer=yes', 'modelHint');
	}
}

function PopulateVariant(){
	var model_id = document.getElementById("dr_model").value;
	if(model_id != ""){
		showHint('../preowned/managepreownedvariant-servicecode-check.jsp?model_id=' + model_id + '&model=yes', 'variantHint');
	}
}
</script>           
</body>
</html>
