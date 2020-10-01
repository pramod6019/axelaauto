<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Email_Send" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
<link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />	
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
<%if (mybean.target.equals("1") && mybean.target.equals("2")
					&& mybean.target.equals("3")) {%>
	document.form1.contact_id.focus();
<%}%>
	}
</script>
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
						<h1>Send Email</h1>
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
						<%=mybean.linkheader%>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<center>
								<font color="#FF0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Send Email
										<%=mybean.status%>
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
											<%
												if (mybean.branch_id.equals("0")
														&& mybean.contact_id.equals("0")
														&& (mybean.target.equals("1") || mybean.target.equals("2") || mybean.target
																.equals("3"))) {
											%>
											<div class="form-element6">
												<label> Branch<font color=red>*</font>: </label>
													<select name="dr_branch" id="dr_branch"
														class="form-control" onChange="FormSubmit();">
														<%=mybean.PopulateBranch(mybean.customer_branch_id, "all", "","", request)%>
													</select>
											</div>
											<%
												}
											%>
											<%
												if ((!mybean.contact_id.equals("0"))) {
											%>
												<div class="form-element6">
												<label> Contact Name<font color=red>*</font>:
												</label>
													<a href="../customer/customer-contact-list.jsp?contact_id=<%=mybean.contact_id%>"><%=mybean.contact_fname%></a>
											</div>
											<%
												}
											%>
											<%
												if ((mybean.contact_id.equals("0"))) {
											%>
												<div class="form-element6">
												<label> Send To<font color=red>*</font>: </label>
													<select name="target" id="target" class="form-control">
														<%=mybean.PopulateSendTo()%>
													</select>
											</div>
											<%
												}
											%>
												<div class="form-element6">
												<label> Subject<font color=red>*</font>: </label>
													<input name="txt_email_subject" type="text"
														class="form-control" value="<%=mybean.email_subject%>"
														size="63" maxlength="1000">
											</div>
												<div class="form-element6">
												<label> Message<font color=red>*</font>: </label>
													<textarea name="txt_email_msg" cols="60" rows="12"
														class="form-control summernote_1" id="txt_email_msg" maxlength="10000"><%=mybean.email_msg%></textarea>
													<script type="text/javascript">
														CKEDITOR
																.replace(
																		'txt_email_msg',
																		{
																			uiColor : hexc($(
																					"a:link")
																					.css(
																							"color")),
																		});
													</script>
													<br> <span id="span_email_msg"></span>
											</div>
											<div class="row"></div>
											<center>
												<input name="send_button" type="submit"
													class="btn btn-success" Value="Send" size="30"
													maxlength="255"> <%
 	if (!mybean.smart.equals("")) {
 %> <input
													name="smart" id="smart" type="hidden" Value="yes">
														<%
															}
														%>
												
											</center>
											<br></br>

											<table class="table table-bordered">
												<tr>
													<th colspan="2" style="text-align: center"><b>Substitution
															Variables </b></th>
												</tr>
												<tr>
													<td align="right">Customer Name:</td>
													<td align="left">[CUSTOMERNAME]</td>
												</tr>
												<tr>
													<td align="right">Customer ID:</td>
													<td align="left">[CUSTOMERID]</td>
												</tr>
												<tr>
													<td align="right">Contact Name:</td>
													<td align="left">[CONTACTNAME]</td>
												</tr>
												<tr>
													<td align="right">Contact ID:</td>
													<td align="left">[CONTACTID]</td>
												</tr>
												<tr>
													<td align="right">Contact Job Title:</td>
													<td align="left">[CONTACTJOBTITLE]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile 1:</td>
													<td align="left">[CONTACTMOBILE1]</td>
												</tr>
												<tr>
													<td align="right">Contact Mobile 2:</td>
													<td align="left">[CONTACTMOBILE2]</td>
												</tr>
												<tr>
													<td align="right">Contact Email 1:</td>
													<td align="left">[CONTACTEMAIL1]</td>
												</tr>
												<tr>
													<td align="right">Contact Email 2:</td>
													<td align="left">[CONTACTEMAIL2]</td>
												</tr>
											</table>

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
<%@include file="../Library/js.jsp" %>
	<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
   <script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
</body>
</HTML>
