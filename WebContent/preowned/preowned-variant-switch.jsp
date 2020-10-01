<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Variant_Switch" scope="request"/>
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
						<h1> Pre-Owned Variant Switch</h1>
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
							<li><a href="managepreownedvariant.jsp?all=yes">List Pre Owned Variant</a> &gt;</li>
							<li><a href="preowned-variant-switch.jsp?<%=mybean.QueryString%>"> Pre Owned Variant Switch</a><b>:</b></li>
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
								 Pre-Owned Variant Switch
							</div>
						</div>
						<div class="portlet-body portlet-empty container-fluid">
							<div class="tab-pane" id="">
								<!-- START PORTLET BODY -->
								<form name="form1"  method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color="red">*</font> are required.</font>
											</center>
										
										<div class="form-element6">	
											<div class="form-element6 form-element-center">
												<label>From Manufacturer :</label>
												<a href="managepreownedmanufacturer.jsp?carmanuf_id=<%=mybean.carmanuf_id%>"><%=mybean.carmanuf_name%></a>
											</div>
											
											<div class="form-element6 form-element-center">
												<label>From Model :</label>
												<a href="managepreownedmodel.jsp?preownedmodel_id=<%=mybean.preownedmodel_id%>"><%=mybean.preownedmodel_name%></a>
											</div>
											
											<div class="form-element6 form-element-center">
												<label>From Variant :</label>
												<a href="managepreownedvariant.jsp?variant_id=<%=mybean.variant_id%>"><%=mybean.variant_name%></a>
											</div>
										</div>
										
										<div class="form-element6">	
											<div class="form-element12 form-element">
													<label>To Variant<font color="#ff0000">*</font>: </label>
													<select class="form-control select2" id="preownedvariant" name="preownedvariant" >
														<%=mybean.modelcheck.PopulateVariant(mybean.to_variant_id)%>
													</select>
												</div>
										</div>	
										<div class="row"></div>
											
											<center>
						                      <input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Submit" onClick="return SubmitFormOnce(document.form1, this);" />
						                       <input type="hidden" id="add_button" name="add_button" value="yes"/>
						                      <input type="hidden" id="carmanuf_id" name="carmanuf_id" value="<%=mybean.carmanuf_id%>" />
						                      <input type="hidden" id="preownedmodel_id" name="preownedmodel_id" value="<%=mybean.preownedmodel_id%>" />
						                      <input type="hidden" id="variant_id" name="variant_id" value="<%=mybean.variant_id%>" />
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

</body>
</html>
