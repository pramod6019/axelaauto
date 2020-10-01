<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.ManageBrochure"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>

<body class="page-container-bg-solid page-header-menu-fixed">

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
						<h1>List Brochure</h1>
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="managebrochure.jsp">List Brochure</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font><br>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Search Brochure
									</div>
								</div>
								
								<div class="container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->

										<form name="form1" method="post" action="managebrochure.jsp" class="form-horizontal">

											<div class="form-element3">
												<label>Brand<font color="red">*</font>: </label>
												<select name="dr_brand_id" class="form-control"
													id="dr_brand_id" onChange="PopulateModelChange(this.id);">
													<%=mybean.PopulatePrincipal()%>
												</select>
											</div>

											<div class="form-element3">
												<label>Rate Class<font color="red">*</font>: </label>
												<select name="dr_rateclass_id" class="form-control" id="dr_rateclass_id"
													onChange="PopulateModelChange(this.id);">
													<%=mybean.PopulateRateClass()%>
												</select>
											</div>

											<div class="form-element3">
												<label>Model: </label> 
												<select name="dr_model" class="form-control" id="dr_model"
													onChange="PopulateModelChange(this.id);">
													<%=mybean.PopulateModel()%>
												</select>
											</div>

											<div class="form-element3">
												<label>Variant: </label> 
												<select name="dr_item" class="form-control" id="dr_item"
													onChange="PopulateItem(this.id);">
													<%=mybean.PopulateItem()%>
												</select>
											</div>

											<%
												if (!mybean.brand_id.equals("0")
														&& !mybean.rateclass_id.equals("0")) {
											%>
											
											<div style="float: right">
												<a href="../portal/docs-update.jsp?add=yes&brand_id=<%=mybean.brand_id%>&brochure_rateclass_id=<%=mybean.rateclass_id%>&dr_model=<%=mybean.model_id%>&dr_item=<%=mybean.item_id%>">Add Brochure...</a>
											</div>
											
											<%
												}
											%>
											
										</form>
									</div>
								</div>
							</div>
							<center>
								<%=mybean.StrHTML%>
							</center>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function PopulateItem() {
			document.form1.submit()
		}

		function PopulateModelChange() {
			document.getElementById("dr_item").value = "0";
			document.form1.submit();
		}
	</script>
</body>
</html>
