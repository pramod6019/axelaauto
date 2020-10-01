<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Preowned_Dash_Image_Update" scope="page" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.txt_img_name.focus()
	}
</script>
</HEAD>
<body onload="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
	<div class="page-container">
		<%@include file="../portal/header.jsp"%>
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>
							<%=mybean.status%>
							Image
						</h1>
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
							<li><a href="index.jsp">Pre Owned</a> &gt;</li>
							<li><a href="preowned.jsp">Pre Owned</a> &gt;</li>
							<li><a href="preowned-list.jsp?all=yes">List Pre Owned</a>&gt;</li>
							<li><a
								href="preowned-dash.jsp?pop=yes&amp;preowned_id=<%=mybean.preowned_id%>"><%=mybean.preowned_title%></a>
								&gt;</li>
							<li><a href="preowned-dash-image.jsp?all=yes">List Image</a>
								&gt;</li>
							<li><a
								href="preowned-dash-image-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									Image</a>:</li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<form name="frmdoc" enctype="MULTIPART/FORM-DATA" method="post"
								class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<div class="tab-pane" id="">
									<!-- 					BODY START -->
									<div class="portlet box  ">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">
												<center>
													<%=mybean.status%>
													Image
												</center>
											</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<form name="formcontact" method="post"
													class="form-horizontal">
													<center>
														<font size="">Form fields marked with a red
															asterisk <font color=#ff0000>*</font> are required.
														</font>
													</center>
													
													<% if (mybean.img_value != null && !mybean.img_value.equals("")) { %>
													
													<center>
														<img src="../Thumbnail.do?preownedimg=<%=mybean.img_value%>&width=500"><br>
													</center>
													
													<% } %>
														
													<div class="form-element12">
														<div class="form-element6 form-element form-element-center">
															<label>Select Image<font color="#ff0000">*</font>: </label>
															<strong>
																<input NAME="filename" Type="file" class="btn btn-success"
																	id="filename" value="<%=mybean.img_value%>" size="30" />
															</strong>
														</div>
													</div>

													<div class="form-element12">
														<div class="form-element6 form-element form-element-center">
															<center>Click the Browse button to select the
																document from your computer!</center>
														</div>
													</div>

													<div class="form-element12">
														<div class="form-element6 form-element form-element-center">
															<label>Title<font color="#ff0000">*</font>: </label>
															<input name="txt_img_name" type="text" class="form-control" id="txt_img_name"
																value="<%=mybean.img_title%>" size="50" maxlength="255" />
														</div>
													</div>

													<div class="form-element12">
														<div class="form-element6 form-element form-element-center">
															<label>Allowed Formats: </label>
															<div>
																<b><%=mybean.ImageFormats%></b>
															</div>
														</div>
													</div>

													<% if (mybean.status.equals("Update") && !(mybean.img_entry_by == null) 
															&& !(mybean.img_entry_by.equals(""))) { %>
														<div class="form-element6">
															<label>Entry By</label>
															<span>
																<%=mybean.img_entry_by%>
															</span>
														</div>
													<% } %>
													
													<% if (mybean.status.equals("Update") && !(mybean.img_entry_date == null) 
															&& !(mybean.img_entry_date.equals(""))) { %>
														<div class="form-element6">
															<label>Entry Date</label>
															<span>
																<%=mybean.img_entry_date%>
															</span>
														</div>
													
													<% } %>
													
													<% if (mybean.status.equals("Update") && !(mybean.img_modified_by == null) 
															&& !(mybean.img_modified_by.equals(""))) { %>
														<div class="form-element6">
															<label>Modified By:</label>
															<span>
																<%=mybean.img_modified_by%>
															</span>
														</div>
													<% } %>
													<% if (mybean.status.equals("Update") && !(mybean.img_modified_date == null)
															&& !(mybean.img_modified_date.equals(""))) { %>

														<div class="form-element6">
															<label>Modified Date:</label>
															<span>
																<%=mybean.img_modified_date%>
															</span>
														</div>
														
													<% } %>
													
													<strong>
													 <% if (mybean.status.equals("Add")) { %>
														<center>
															<input name="add_button" type="submit" class="btn btn-success" value="Add Image" />
															<input type="hidden" name="add" value="yes"/>
															<input type="hidden" name="img_id" value="<%=mybean.img_id%>">
															<input type="hidden" name="preowned_id" value="<%=mybean.preowned_id%>">
														</center>
													<% } else if (mybean.status.equals("Update")) { %>
														<center>
															<input name="update_button" type="submit" class="btn btn-success" value="Update Image" />
															<input type="hidden" name="update" value="yes" />
															<input type="hidden" name="img_id" value="<%=mybean.img_id%>">
															<input type="hidden" name="preowned_id" value="<%=mybean.preowned_id%>">
															<input name="delete_button" type="submit" class="btn btn-success"
																OnClick="return confirmdelete(this)" value="Delete Image" />
														</center> 
													<% } %>
													</strong>
												</form>
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

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>
</body>
</HTML>
