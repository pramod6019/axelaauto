<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Faqexecutivecat_Update"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../Library/css.jsp"%>

</HEAD>
<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
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
						<h1>FAQ Executive Update</h1>
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
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="faq.jsp">FAQ</a> &gt;</li>
						<li><a href="faqexecutivecat-list.jsp?all=yes">List
								Executive Categories</a> &gt;</li>
						<li><a
							href="faqexecutivecat-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Category</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;Category
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											Form fields marked with a red asterisk <b><font
												color="#ff0000">*</font></b> are required.
										</center>
										</br>
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-element6 form-element-center">
												<label > Name: <font color=red>*</font>
												</label>
													<input name="txt_cat_name" type="text" class="form-control"
														id="txt_cat_name" value="<%=mybean.cat_name%>" size="50"
														maxlength="255">
											</div>
											<center>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6 ">
												<label > Entry By: 
												</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6 ">
												<label > Entry Date: 
												</label>
													<%=mybean.entry_date%>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6 ">
												<label > Modified By:
													
												</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update")
														&& !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6 ">
												<label > Modified
													Date: 
												</label>
													<%=mybean.modified_date%>
											</div>
											<%
												}
											%>
											</center>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add Category" /> <input type="hidden"
													name="add_button" value="Add Category"> 
													<% } else if (mybean.status.equals("Update")) { %>
													<div class="form-element12">
													<input type="hidden" name="update_button"
													value="Update Category"> <input name="button"
														type="submit" class="btn btn-success" id="button"
														value="Update Category" /> <input name="delete_button"
														type="submit" class="btn btn-success" id="delete_button"
														onClick="return confirmdelete(this)"
														value="Delete Category" /> <input type="hidden"
														name="Update" value="yes"> 
														</div>
														<% } %>
														 <input type="hidden" name="entry_by"
															value="<%=mybean.entry_by%>"> <input
																type="hidden" name="entry_date"
																value="<%=mybean.entry_date%>"> <input
																	type="hidden" name="modified_by"
																	value="<%=mybean.modified_by%>"> <input
																		type="hidden" name="modified_date"
																		value="<%=mybean.modified_date%>">
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
	<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_cat_name.focus()
	}
</script>
</body>
</HTML>
