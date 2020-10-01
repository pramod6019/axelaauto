<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Sms_Contact_Send"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />

<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript"
	src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>

<script language="JavaScript" type="text/javascript">
	function hideRow(element) {
		//element refer to row id where u want to hide a row.
		var myObj = document.getElementById(element);
		//u need to a give id for a row to which u want to hide
		var cels = myObj.getElementsByTagName('td');
		for (var col_no = 0; col_no < cels.length; col_no++) {
			cels[col_no].style.display = 'none';
		}
		myObj.style.visibility = 'hidden';
	}

	function displayRow(element) {
		var myObj = document.getElementById(element);
		//u need to a give id for a row to which u want to hide
		var cels = myObj.getElementsByTagName('td')
		for (var col_no = 0; col_no < cels.length; col_no++) {
			cels[col_no].style.display = '';
		}
		myObj.style.visibility = 'visible';
		//myObj.style.height = '0px';
	}
	function Displaypaymode() {
		var zone = document.form1.chk_sms_allgroup.checked;
		if (zone == true)
			zone = "1";
		else
			zone = "0";
		//        alert("str======="+zone);
		if (zone == "1") {
			hideRow('group');
		}
		if (zone == "0") {
			displayRow('group');
		}
	}
	function FormSubmit() {
		document.form1.submit();
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onLoad="Displaypaymode();"
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
						<h1>Manage Password</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="sms.jsp">SMS</a> &gt;</li>
						<li><a href="sms-contact-send.jsp">Send Executive SMS<%=mybean.status%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Send SMS to
										<%=mybean.sendto%>
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form method="post" name="form1" class="form-horizontal"
											action="sms-contact-send.jsp">
											<%
												if (mybean.cont.equals("yes")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Contact ID: </label>
												<div class="col-md-6 col-xs-12">
													<input class="form-control" type="text" name="contact_id"
														id="contact_id" maxlength="60"
														onKeyUp="toNumber('contact_id','Contact ID');"
														value="<%=mybean.customer_id%>">
												</div>
											</div>
											<%
												}
												if (mybean.contkey.equals("yes")) {
											%>

											<div class="form-group">
												<label class="control-label col-md-4">>Contact Key
													ID: </label>
												<div class="col-md-6 col-xs-12">
													<input class="form-control" type="text"
														name="contact_key_id" id="contact_key_id"
														onKeyUp="toNumber('contact_id','Contact ID');"
														value="<%=mybean.customer_contact_id%>" size="60"
														maxlength="60">
												</div>
											</div>
											<%
												}
												if (mybean.allconn.equals("yes")) {
											%>
											<div class="form-group">
												<label class="control-label col-md-4">All: </label>
												<div class="col-md-6 col-xs-12">
													<input class="form-control" type="text"
														name="chk_sms_allgroup" id="chk_sms_allgroup"
														onClick="Displaypaymode();"
														<%=mybean.PopulateCheck(mybean.sms_allgroup)%>><input
														type="hidden" name="allconn" value="yes">
												</div>
											</div>
											<div class="form-group">
												<label class="control-label col-md-4">Group: </label>
												<div class="col-md-6 col-xs-12">
													<select name="dr_group" size="20" multiple="multiple"
														class="form-control" id="fo_group" style="width: 300px;">
														<%=mybean.PopulateGroup()%>
													</select>
												</div>
											</div>
											<%
												}
											%>
											<div class="form-group">
												<label class="control-label col-md-4">Message <font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12">
													<textarea name="txt_sms_msg" cols="58" rows="10"
														class="form-control" id="txt_sms_msg"
														onKeyUp="charcount('txt_sms_msg', 'span_sms_msg','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.sms_msg%></textarea>
													<%
														if (mybean.smartcont.equals("yes")) {
													%>
													<input type="hidden" name="smartcont" value="yes">
														<input type="hidden" name="tag" value="<%=mybean.tag%>">
															<%
																}
															%> <%
 	if (mybean.smartcontkey.equals("yes")) {
 %> <input
															type="hidden" name="smartcontkey" value="yes"> <%
 	}
 %> (<span id="span_sms_msg">500 characters)</span>
																</td>
												</div>
											</div>
											<div class="form-group">
												<center>
													<input name="send_button" type="submit"
														class="btn btn-success" id="send_button" value="Send" />
												</center>
											</div>
											<div class="form-group">
												<div class="col-md-6">
													<table class="table table-bordered"
														style="position: relative; left: 400px;">
														<tr>
															<th colspan="2"><b><center>Substitution
																		Variables</center></b></th>
														</tr>
														<tr>
															<td align="right">Contact Name:</td>
															<td>[NAME]</td>
														</tr>
														<tr>
															<td align="right">Contact ID:</td>
															<td>[CONTACTID]</td>
														</tr>
													</table>
												</div>
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
</body>
</HTML>
