<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.New_Docs_Update" scope="page" />
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
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css' />

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.txt_doc_name.focus()
	}
</script>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%
		if (mybean.group.equals("")) {
	%>
	<%@include file="header.jsp"%>
	<%
		}
	%>

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
						<h1>Add Driving Licence</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
				<ul class="page-breadcrumb breadcrumb">
						<%=mybean.LinkHeader%>
				</ul>
					<center>
					</center>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<center>
								<font color="#FF0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%> Driving Licence
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="frmdoc"
											enctype="MULTIPART/FORM-DATA" method="post">
											<div class="form-element6 form-element-center">
												<label>Select Document<font color=red>*</font>: </label>
													<input NAME="filename" Type="file" class="btn btn-success"
														id="filename" value="<%=mybean.doc_value%>" size="34">
														<span>Click the Browse button to select the
												document from your computer</span>
											</div>
											
											<div class="form-element6 form-element-center">
												<label>Allowed
													Formats:</label>
													<b>.jpg, .jpeg, .gif, .png </b>
											</div>
											<div class="form-element6 form-element-center">
												<label>Maximum Size:</label>
													<b>5 Mb</b>
											</div>
											
											<center>
												<strong> <%
 	if (mybean.status.equals("Add")) {
 %> <%
 	if (!mybean.brochure_rateclass_id.equals("0")) {
 %> <input name="add_button" type="submit" class="btn btn-success"
													value="Add Brochure" /> <%
 	} else {
 %> <input name="add_button" type="submit" class="btn btn-success"
													value="Add Driving Licence" /> <%
 	}
 %> <input type="hidden" name="doc_id" value="<%=mybean.doc_id%>">
													<input type="hidden" name="brochure_id"
													value="<%=mybean.brochure_id%>"> <input
													type="hidden" name="brochure_rateclass_id	"
													value="<%=mybean.brochure_rateclass_id%>"> <input
													type="hidden" name="contact_id"
													value="<%=mybean.customer_id%>"> <%
 	} else if (mybean.status.equals("Update")) {
 %> <%
 	if (!mybean.brochure_rateclass_id.equals("0")) {
 %> <input name="update_button" type="submit"
													class="btn btn-success" value="Update Brochure" /> <%
 	} else {
 %> <input name="update_button" type="submit" class="btn btn-success"
													value="Update Document" /> <%
 	}
 %> <input type="hidden" name="doc_id" value="<%=mybean.doc_id%>">
													<input type="hidden" name="brochure_id"
													value="<%=mybean.brochure_id%>"> <input
													type="hidden" name="brochure_rateclass_id	"
													value="<%=mybean.brochure_rateclass_id%>"> <input
													type="hidden" name="contact_id"
													value="<%=mybean.customer_id%>"> <%
 	if (!mybean.brochure_rateclass_id.equals("0")) {
 %> <input name="delete_button" type="submit" class="btn btn-success"
													OnClick="return confirmdelete(this)"
													value="Delete Brochure" /> <%
 	} else {
 %> <input name="delete_button" type="submit" class="btn btn-success"
													OnClick="return confirmdelete(this)"
													value="Delete Document" /> <%
 	}
 %> <%
 	}
 %></strong>
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
		<%@include file="../Library/js.jsp" %>
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
</body>
</HTML>
