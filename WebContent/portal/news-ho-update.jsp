<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.News_Ho_Update"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</HEAD>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.formemp.txt_news_topic.focus();
	}
</script>

<body class="page-container-bg-solid page-header-menu-fixed"
	onLoad="FormFocus()">
	<%@include file="header.jsp"%>

	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1><%=mybean.status%>&nbsp;News
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
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="news.jsp?all=yes">News</a> &gt;</li>
						<li><a href="news-ho-list.jsp?all=yes">List News</a> &gt;</li>
						<li><a href="news-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;News</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

				</div>
				<!-- START CHANGE PASSWORD -->
				<div class="container-fluid">

					<center>
						<font color="#FF0000"><b><%=mybean.msg%></b></font>
					</center>
					<div class="portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none"><%=mybean.status%>&nbsp;News
							</div>
						</div>
						<div class="portlet-body portlet-empty container-fluid">
							<div class="tab-pane" id="">
								<center>
									<font sixe="1"></>Form fields marked with a red asterisk <font color=#ff0000><b>*</b></font>
									are required.</font>
								</center>
								<br>
									<form class="form-horizontal" role="form" id="formemp">

										<div class="form-element6">
												<label>News Topic <font color=red>*</font>: </label>
												<input name="txt_news_topic" id="txt_news_topic" type="text"
													class="form-control" value="<%=mybean.news_topic%>"
													size="50" maxlength="255">
										</div>
										
										<div class="form-element6">
												<label>Description <font color=red>*</font>: </label>
												<textarea name="txt_news_desc" id="txt_news_desc"
													type="text" class="form-control"><%=mybean.news_desc%></textarea>
										</div>
										
										<div class="row">
										<div class="form-element6">
												<label>Date<font color=#ff0000><b>*</b></font>: </label>
												<input name="txt_news_date" id="txt_news_date"
													value="<%=mybean.newsdate%>"
													class="form-control datepicker"
													 type="text" size="15"
													maxlength="14" />
										</div>

										<div class="form-element6 form-element-margin">
												<label>Featured:</label>
												<input name="ch_news_featured" id="ch_news_featured"
													type="checkbox"
													<%=mybean.PopulateCheck(mybean.news_featured)%>>
										</div>
										</div>
										
										<div class="row">
										<div class="form-element6 form-element-margin">
												<label>Active:</label>
												<input name="ch_news_active" id="ch_news_active"
													type="checkbox"
													<%=mybean.PopulateCheck(mybean.news_active)%> />
										</div>
										</div>
										<%
											if (mybean.status.equals("Update") && !(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) {
										%>
										<div class="form-element6">
												<label>Entry By:</label>
												<%=mybean.unescapehtml(mybean.entry_by)%>
												<input name="entry_by" id="entry_by" type="hidden"
													class="form-control" value="<%=mybean.entry_by%>">
										</div>
										<%
											}
										%>
										<%
											if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) {
										%>
										<div class="form-element6">
												<label>Entry Date:</label>
												<%=mybean.entry_date%>
												<input name="entry_date" id="entry_date" type="hidden"
													class="form-control" value="<%=mybean.entry_date%>">
										</div>
										<%
											}
										%>
										<%
											if (mybean.status.equals("Update") && !(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) {
										%>
										<div class="form-element6">
												<label>Modified By:</label>
												<%=mybean.unescapehtml(mybean.modified_by)%>
												<input name="modified_by" id="modified_by" type="hidden"
													class="form-control" value="<%=mybean.modified_by%>">
										</div>
										<%
											}
										%>
										<%
											if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) {
										%>
										<div class="form-element6">
												<label>Modified Date:</label>
												<%=mybean.modified_date%>
												<input name="modified_date" id="modified_date" type="hidden"
													class="form-control" value="<%=mybean.modified_date%>">
										</div>
										<%
											}
										%>
										
										<div class="form-element12">
										<center>
											<%
												if (mybean.status.equals("Add")) {
											%>
											<input name="add_button" type="submit"
												class="btn btn-success" id="add_button" value="Add News" />
											<input type="hidden" name="add_button" value="Add News">
												<%
													} else if (mybean.status.equals("Update")) {
												%> <input type="hidden" name="update_button"
												value="Update News"> <input name="update_button"
													type="submit" class="btn btn-success" id="update_button"
													value="Update News" /> <input name="delete_button"
													type="submit" class="btn btn-success" id="delete_button"
													OnClick="return confirmdelete(this)" value="Delete News" />
													<input type="hidden" name="news_id"
													value=<%=mybean.news_id%>> 
													
													<%}%> 
													
													<input type="hidden" name="emp_id" value="<%//=mybean.emp_id%>">
										</center>
										</div>
									</form>
							</div>
						</div>
					</div>
				</div>

				<!-- END CHANGE PASSWORD -->
			</div>
			<!-- END PAGE CONTENT BODY -->
			<!-- END CONTENT BODY -->
		</div>
		</div>
		<!-- END CONTENT -->
	</div>


	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>

</body>
</HTML>
