<%-- 
    Document : ticket-add
    Created on: Feb 11, 2013
    Author   : Gurumurthy TS
--%>
<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Ticket_Faq_Update"
	scope="request" />
<%mybean.doGet(request,response); %>
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
						<h1><%=mybean.status%>
							FAQ
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
							<li><a href="../service/index.jsp">Service</a> &gt;</li>
							<li><a href="ticket-faq.jsp?all=yes">FAQ</a>&gt;</li>
							<li><a href="ticket-faq-list.jsp?all=recent">List FAQ</a>
								&gt;</li>
							<li><a href="ticket-faq-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
									FAQ</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
							<font color="#ff0000"><b><%=mybean.msg%></b></font>
						</center>
						<!-- 					BODY START -->
						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">
									<center><%=mybean.status%>
										FAQ
									</center>
								</div>
							</div>
							<div class="portlet-body portlet-empty container-fluid">
								<div class="tab-pane" id="">
									<form name="form1" method="post" class="form-horizontal">
										<center>
											<font size="1">Form fields marked with a red asterisk
												<font color=#ff0000>*</font> are required.
											</font>
										</center>
										<div class="form-element6">
											<label>Department<font color="red">*</font>:
											</label> <select name="dr_ticket_dept" id="dr_ticket_dept"
												class="form-control" visible="true"
												onChange="showHint('ticket-faq-check.jsp?ticket_dept_id=' + GetReplace(this.value),'dr_ticketcat_id');">
												<%=mybean.PopulateDepartment() %>
											</select>
										</div>
										<div class="form-element6">
											<label>Category<font color="red">*</font>:
											</label> <span id="dr_ticketcat_id"><%=mybean.PopulateCat()%></span>
											<div class="admin-master">
												<a href="../service/ticket-faq-cat-list.jsp?all=yes"
													title="Manage Category"></a>
											</div>
										</div>
										<div class="row"></div>
										<div class="form-element6">
											<label>Question<font color="red">*</font>:
											</label>
											<textarea name="txt_ticketfaq_question" cols="60" rows="6"
												class="form-control" id="txt_ticketfaq_question"
												onKeyUp="charcount('txt_ticketfaq_question', 'span_txt_ticketfaq_question','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.ticketfaq_question%></textarea>
											&nbsp;<span id="span_txt_ticketfaq_question">(255
												Characters) </span>
										</div>
										<div class="form-element6">
											<label>Answer<font color="red">*</font>:
											</label>
											<textarea name="txt_ticketfaq_answer" cols="60" rows="6"
												wrap="virtual" class="form-control"
												id="txt_ticketfaq_answer"
												onKeyUp="charcount('txt_ticketfaq_answer', 'span_txt_ticketfaq_answer','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.ticketfaq_answer%></textarea>
											&nbsp;<span id="span_txt_ticketfaq_answer">(8000
												Characters) </span>
										</div>
										<div class="row">
											<div class="form-element6 form-element-margin">
												<label>Active:</label> <input id="chk_active"
													type="checkbox" name="chk_active"
													<%=mybean.PopulateCheck(mybean.ticketfaq_active)%> />
											</div>
										</div>
										<% if (mybean.status.equals("Update") &&!(mybean.entry_by == null) && !(mybean.entry_by.equals(""))) { %>
										<div class="form-element6">
											<label>Entry By:</label>
											<%=mybean.unescapehtml(mybean.entry_by)%>
											<input type="hidden" name="entry_by"
												value="<%=mybean.unescapehtml(mybean.entry_by)%>">
										</div>
										<div class="form-element6">
											<label>Entry Date:</label>
											<%=mybean.entry_date%>
											<input type="hidden" name="entry_date"
												value="<%=mybean.entry_date%>">
										</div>
										<%}%>
										<% if (mybean.status.equals("Update") &&!(mybean.modified_by == null) && !(mybean.modified_by.equals(""))) { %>
										<div class="form-element6">
											<label>Modified By:</label>
											<%=mybean.unescapehtml(mybean.modified_by)%>
											<input type="hidden" name="modified_by"
												value="<%=mybean.unescapehtml(mybean.modified_by)%>">
										</div>
										<div class="form-element6">
											<label>Modified Date:</label>
											<%=mybean.modified_date%>
											<input type="hidden" name="modified_date"
												value="<%=mybean.modified_date%>">
										</div>
										<%}%>
										<div class="row"></div>
										<%if(mybean.status.equals("Add")){%>
										<center>
											<input name="addbutton" type="submit" class="btn btn-success"
												id="addbutton" value="Add FAQ"
												onClick="return SubmitFormOnce(document.form1, this);" /> <input
												type="hidden" id="add_button" name="add_button" value="yes" />
										</center>

										<%}else if (mybean.status.equals("Update")){%>
										<center>
											<div class="form-element12">
												<input type="hidden" id="update_button" name="update_button"
													value="yes" /> <input name="updatebutton" type="submit"
													class="btn btn-success" id="updatebutton"
													value="Update FAQ"
													onClick="return SubmitFormOnce(document.form1, this);" /> <input
													name="delete_button" type="submit" class="btn btn-success"
													id="delete_button" OnClick="return confirmdelete(this)"
													value="Delete FAQ" />
											</div>
										</center>
										<%}%>
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

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript"> 

function FormFocus() { //v1.0
  document.form1.txt_ticketfaq_question.focus()
}
        </script>
</body>
</HTML>


