<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.ManageStockOptionType_Update" scope="request" />
<%mybean.doGet(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp" %>
</head>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<!-- 	BODY -->
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
						<h1><%=mybean.status%>&nbsp;Stock Option type
						</h1>
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
							<li><a href="managestockoptiontype.jsp?all=yes">List Stock Option Type</a> &gt;</li>
							<li><a href="managestockoptiontype-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Stock Option Type</a><b>:</b></li>
						</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<!-- 	PORTLET -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Stock Option Type
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<center>
												<font size="1">Form fields marked with a red asterisk
													<font color="red">*</font> are required.
												</font>
											</center><br>

											<div class="form-element6 form-element-center">
												<label>Stock Option Type<font color=red>*</font>:</label>
													<input name="txt_optiontype_name" type="text" class="form-control" id="txt_optiontype_name"
														value="<%=mybean.optiontype_name %>" maxlength="255" />
											</div>
											<center>
											<%if(mybean.status.equals("Add")){%> <input name="addbutton"
												type="submit" class="btn btn-success" id="addbutton"
												value="Add Type"
												onClick="return SubmitFormOnce(document.form1, this);" /> <input
												type="hidden" name="add_button" id="add_button" value="yes">
													<%}else if (mybean.status.equals("Update")){%> <input
													type="hidden" name="update_button" id="update_button"
													value="yes" /> <input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton" value="Update OptionType"
													onClick="return SubmitFormOnce(document.form1, this);" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete OptionType" /> <%}%>
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
<%@ include file="../Library/admin-footer.jsp"%>
<%@ include file="../Library/js.jsp"%>
<script>
function FormFocus() { //v1.0
  document.form1.txt_optiontype_name.focus()
}  
        </script>
</body>
</html>
