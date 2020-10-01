
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.News_Branch_Update"
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
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.formemp.txt_branchnews_topic.focus();
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
						<li><a href="news-branch-list.jsp?all=yes">List Branch
								News</a> &gt;</li>
						<li><a href="news-branch-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;News</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
						<div class="tab-pane" id="">

							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>

							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><%=mybean.status%>&nbsp;News
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font size="1">Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="formemp" class="form-horizontal">
											<div class="form-element6">
												<label>Branch: </label>
													<select name="dr_branch" id="dr_branch"
														class="form-control"><%=mybean.PopulateBranch(mybean.news_branch_id, "all", "", "",  request)%>
													</select>
											</div>
											
											<div class="form-element6">
												<label>News Topic: </label>
													<input name="txt_branchnews_topic" type="text"
														class="form-control" value="<%=mybean.news_topic%>"
														size="56" maxlength="50">
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label>Description<font color="#ff0000">*</font>: </label>
													<textarea name="txt_branchnews_desc" cols="55"
														id="txt_branchnews_desc" type="text" class="form-control"
														onKeyUp="charcount('txt_branchnews_desc', 'span_txt_branchnews_desc','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.news_desc%></textarea>
													<span id="span_txt_branchnews_desc"> (1000
														Characters)</span>
											</div>


											<div class="form-element6 form-element-margin">
												<label>Date<font color=#ff0000><b>*</b>:</font></label>
													<input name="txt_branchnews_date" id="txt_branchnews_date"
														value="<%=mybean.newsdate%>"
														class="form-control datepicker"
														size="15" maxlength="14"
														type="text" />
											</div>
											
											<div class="row"></div>

											<div class="form-element6">
												<label>Featured:</label>
													<input type="checkbox" name="ch_branchnews_featured"
														id="ch_branchnews_featured"
														<%=mybean.PopulateCheck(mybean.news_featured)%> />
											</div>


											<div class="form-element6">
												<label>Active:</label>
													<input type="checkbox" name="ch_branchnews_active"
														id="ch_branchnews_active"
														<%=mybean.PopulateCheck(mybean.news_active)%> />
											</div>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_by == null)
														&& !(mybean.entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By:</label>
													<%=mybean.unescapehtml(mybean.entry_by)%>
													<input name="entry_by" type="hidden" id="entry_by"
														value="<%=mybean.entry_by%>" />
											</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry Date:</label>
													<%=mybean.entry_date%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>" />
											</div>

											<%
												}
											%>
											<%
												if (mybean.status.equals("Update") && !(mybean.modified_by == null)
														&& !(mybean.modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By:</label>
													<%=mybean.unescapehtml(mybean.modified_by)%>
													<input name="modified_by" type="hidden" id="modified_by"
														value="<%=mybean.modified_by%>" />
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
												<label>Modified Date:</label>
													<%=mybean.modified_date%><input type="hidden"
														name="modified_date" value="<%=mybean.modified_date%>" />
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
													%>
													<input type="hidden" name="update_button"
														value="Update News"> <input name="update_button"
														type="submit" class="btn btn-success" id="update_button"
														value="Update News" /> <input name="delete_button"
														type="submit" class="btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete News" />
													<%
														}
													%>
													<input type="hidden" name="entry_date"
														value="<%=mybean.entry_date%>"> <input
														type="hidden" name="modified_date"
														value="<%=mybean.modified_date%>"> <input
														type="hidden" name="session_emp_entry_by"
														value="<%=mybean.entry_by%>"> <input type="hidden"
														name="session_emp_modified_by"
														value="<%=mybean.modified_by%>">
												</center>
											</div>
											<div class="form-group">
												<input type="hidden" name="emp_id"
													value="<%//=mybean.emp_id%>">
											</div>

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
</body>
</HTML>
