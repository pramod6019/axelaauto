<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Faqexecutive_Update"
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

<%@include file="../Library/css.jsp" %>
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
				<div class="container-fluid">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="faq.jsp">FAQ</a> &gt;</li>
						<li><a href="faqexecutive-list.jsp">List Executive FAQs</a>
							&gt;</li>
						<li><a href="faqexecutive-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Executive
								FAQ</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%>&nbsp;FAQ
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="form1" method="post">
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.</font>
											</center>
											</br>
											<div class="form-element6">
												<label > Category <font color=red>*</font>:
												</label>
													<select name="dr_faq_cat_id" id="dr_faq_cat_id"
														class="form-control">
														<%=mybean.PopulateCategory()%>
													</select>
											</div>
											<div class="form-element6">
												<label > Question <font color=red>*</font>:
												</label>
													<textarea name="txt_faq_question" cols="40" rows="4"
														class="form-control" id="txt_faq_question"
														onKeyUp="charcount('txt_faq_question', 'span_txt_faq_question','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.faq_question%></textarea>
													<span id="span_txt_faq_question"> 255 characters </span>
											</div>
												<div class="row">
											<div class="form-element6">
												<label > Answer <font color=red>*</font>:
												</label>
													<textarea name="txt_faq_answer" cols="40" rows="4"
														wrap="virtual" class="form-control" id="txt_faq_answer"
														onKeyUp="charcount('txt_faq_answer', 'span_txt_faq_answer','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.faq_answer%></textarea>
													<span id="span_txt_faq_answer"> 1000 characters </span>
											</div>
											<div class="form-element6">
												<label > Active: </label>
													<input id="chk_active" type="checkbox" name="chk_active"
														<%=mybean.PopulateCheck(mybean.faq_active)%> />
											</div>
											</div>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label > Entry By: </label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
											</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label > Entry Date: </label>
													<%=mybean.entry_date%>
											</div>
											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label > Modified By: </label>
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
											<div class="form-element6">
												<label > Modified Date: </label>
													<%=mybean.modified_date%>
											</div>
											<%
												}
											%>
											<center>
												<%
													if (mybean.status.equals("Add")) {
												%>
											
												<input name="button" type="submit" class="btn btn-success"
													id="button" value="Add FAQ" /> <input type="hidden"
													name="add_button" value="Add FAQ">
													 <% } else if (mybean.status.equals("Update")) { %>
													 <div class="form-element12">
													<input type="hidden" name="update_button"
													value="Update FAQ"> <input name="button"
														type="submit" class="btn btn-success" id="button"
														value="Update FAQ" /> <input name="delete_button"
														type="submit" class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete FAQ" />
														</div>
														<%
															}
														%> <input type="hidden" name="entry_by"
														value="<%=mybean.entry_by%>"> <input type="hidden"
															name="entry_date" value="<%=mybean.entry_date%>">
																<input type="hidden" name="modified_by"
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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.form1.txt_faq_question.focus()
	}
</script>
</body>
</HTML>
