<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Model_Image" scope="page"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

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
						<h1>Update Image</h1>
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
						<li><a href="../app/home.jsp">App</a> &gt;</li>
						<li>Features &gt; <a href="features-list.jsp?all=yes"> List Features</a> &gt; </li>
						<li><a href="features-image.jsp?model_id=<%=mybean.model_id%>">Update Image</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="frmdoc" enctype="MULTIPART/FORM-DATA" method="post"
								class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="page-content-inner">
									<div class="tab-pane" id="">
										<!-- 					BODY START -->
										<div class="portlet box  ">
											<div class="portlet-title" style="text-align: center">
												<div class="caption" style="float: none">
													<center>Update Image</center>
												</div>
											</div>
											<div class="portlet-body portlet-empty">
												<div class="tab-pane" id="">
													<form name="formcontact" method="post" class="form-horizontal">
														<center>
															<font>
																Form fields marked with a red
																asterisk <font color=#ff0000>*</font> are required.
															</font>
														</center>
														<%
															if (mybean.model_img_value != null && !mybean.model_img_value.equals("")) {
														%>
														<div class="form-element6">
															<center>
																<img src="../Thumbnail.do?modelimg=<%=mybean.model_img_value%>">
															</center>
														</div>
														<%
															}
														%>
														
														<div class="form-element6 form-element-center">
															<label >Select Image<font color="#ff0000">*</font>: </label>
															<div>
																<strong>
																	<input NAME="filename" Type="file" class="btn btn-success"
																		id="filename" value="<%=mybean.model_img_value%>" size="30"/>
																</strong>
															</div>
														<span>Click the Browse button to select the document from your computer!</span>
														</div>
														
														<div class="form-element6 form-element-center">
															<label >Allowed Formats:&nbsp; </label>
															<span>
																<b><%=mybean.ImageFormats%></b>
															</span>
														</div>
														<%
															// if (mybean.status.equals("Update") && !(mybean.img_entry_by == null) && !(mybean.img_entry_by.equals(""))) {
														%>
														<!-- <tr valign="middle"><td align="right" valign="middle">Entry By<b>:</b>&nbsp;</td>
															<td><//%=mybean.img_entry_by%></td></tr>-->
														<%
															//}
														%>
														<%
															// if (mybean.status.equals("Update") && !(mybean.img_entry_date == null) && !(mybean.img_entry_date.equals(""))) {
														%>
														<!--<tr valign="middle"><td align="right" valign="middle">Entry Date<b>:</b>&nbsp;</td>
															<td><//%=mybean.img_entry_date%></td></tr>-->
														<%
															//}
														%>
														<%
															// if (mybean.status.equals("Update") && !(mybean.img_modified_by == null) && !(mybean.img_modified_by.equals(""))) {
														%>
														<!--<tr valign="middle"><td align="right" valign="middle">Modified By<b>:</b>&nbsp;</td>
															<td><//%=mybean.img_modified_by%></td></tr>-->
														<%
															//}
														%>
														<%
															// if (mybean.status.equals("Update") && !(mybean.img_modified_date == null) && !(mybean.img_modified_date.equals(""))) {
														%>
														<!--<tr valign="middle"><td align="right" valign="middle">Modified Date<b>:</b>&nbsp;</td>
															<td><//%=mybean.img_modified_date%></td></tr>-->
														<%
															//}
														%>
														<center>
															<strong>
																<input name="update_button" type="submit" class="btn btn-success" id="update_button" value="Update Image" />
																<input type="hidden" name="update" value="yes"/>
																<input type="hidden" name="model_id" value="<%=mybean.model_id%>"/>
																<input name="delete_button" type="submit" class="btn btn-success" OnClick="return confirmdelete(this)" value="Delete Image" />
															</strong>
														</center>

													</form>
												</div>
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

	<%@include file="../Library/admin-footer.jsp" %>
	
	<%@include file="../Library/js.jsp"%>
	
	<script language="JavaScript" type="text/javascript">
		function FormFocus() { //v1.0
			document.frmdoc.txt_img_name.focus()
		}
	</script>
</body>
</HTML>
