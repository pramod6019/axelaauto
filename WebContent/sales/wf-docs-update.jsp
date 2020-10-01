<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Wf_Docs_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

</head>
<body onload="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">

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
						<h1><%=mybean.status%>&nbsp;Work Flow Document
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
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="wf-docs-list.jsp?all=yes">List Work Flow Documents</a> &gt;</li>
							<li><a href="wf-docs-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>Work Flow Document</a><b>:</b></li> </ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Work Flow Document
									</div>
								</div>
								<div class="container-fluid portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<font color=#ff0000>*</font> are required.
											</font>
										</center>

										<form name="formcontact" method="post" class="form-horizontal">

											<div class="form-element6">
												<label> Title<font color=red>*</font>: </label>
												<input name="txt_doc_wf_title" id="txt_doc_wf_title"
												type="text" class="form-control"
												value="<%=mybean.doc_wf_title%>" maxlength="255" size="50" />
											</div>

											<div class="form-element6">
												<label> Effective From<font color=red>*</font>: </label>
												<select id="dr_effective" name="dr_effective"
												class="form-control">
												<%=mybean.PopulateEffective()%>
												</select>
											</div>

											<div class="form-element6">
												<label> No of Days<font color=red>*</font>: </label>
												<input name="txt_doc_daynos" id="txt_doc_daynos"
													type="text" class="form-control"
													value="<%=mybean.doc_daynos%>" maxlength="3" size="10" />
											</div>

											<div class="form-element6"></div>

											<div class="form-element12">

												<%
												if (mybean.status.equals("Update") && !mybean.entry_by.equals("")) {
											%>

												<div class="form-element6 form-element">
													<label> Entry By : </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.unescapehtml(mybean.entry_by)%>">
												</div>

												<div class="form-element6">
													<label> Entry Date : </label>
													<%=mybean.entry_date%>
													<input name="entry_date" type="hidden" id="entry_date"
														value="<%=mybean.entry_date%>" />
												</div>
											</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !mybean.modified_by.equals("")) {
											%>
											<div class="form-element12">
												<div class="form-element6 form-element">
													<label> Modified By : </label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.unescapehtml(mybean.modified_by)%>" />
												</div>

												<div class="form-element6">
													<label> Modified Date : </label>
													<%=mybean.modified_date%>
													<input name="modified_date" type="hidden"
														id="modified_date" value="<%=mybean.modified_date%>">
												</div>
											</div>
											<%
												}
											%>



											<center>

												<%
													if (mybean.status.equals("Add")) {
												%>
												<input type="submit" name="addbutton" id="addbutton"
													class="btn btn-success" value="Add Document"
													onClick="return SubmitFormOnce(document.formcontact, this);">
												<input type="hidden" name="add_button" value="yes">
												<%
															}
															if (mybean.status.equals("Update")) {
														%>
												<input type="submit" name="updatebutton" id="updatebutton"
													class="btn btn-success" value="Update Document"
													onClick="return SubmitFormOnce(document.formcontact, this);">
												<input type="submit" name="delete_button" id="delete_button"
													class="btn btn-success"
													OnClick="return confirmdelete(this)"
													value="Delete Document"> <input type="hidden"
													name="update_button" id="update_button" value="yes">
												<%
																		}
																	%>

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

	<%@include file="../Library/admin-footer.jsp"%>

	<%@include file="../Library/js.jsp"%>

	<script language="JavaScript" type="text/javascript">
		function FormFocus() {
			document.form1.txt_title.focus()
		}
	</script>
</body>
</html>
