<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.customer.Gst_Document_Update" scope="page" />
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
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.txt_doc_name.focus()
	}
</script>
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%
		if (mybean.group.equals("")) {
	%>
	<%@include file="../portal/header.jsp"%>
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
						<h1>Update GST Document</h1>
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
						<%=mybean.LinkHeader%>
				</ul>
					<center>
					</center>
						<div class="tab-pane" id="">
							<center>
								<font color="#FF0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<%=mybean.status%> GST Document
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form class="form-horizontal" name="frmdoc"
											enctype="MULTIPART/FORM-DATA" method="post">
											<div class="form-element6 form-element-center">
												<label>Select Document<font color=red>*</font>: </label>
													<center><input NAME="filename" Type="file" class="btn btn-success"
														id="filename" value="<%=mybean.doc_value%>" size="34">
														<span>Click the Browse button to select the
												document from your computer</span></center>
											</div>
											<div class="form-element6 form-element-center">
												<label>Allowed Formats: <b><%=mybean.config_doc_format %> </b></label>
											</div>
											<div class="form-element6 form-element-center">
												<label>Maximum Size: <b>5 Mb</b></label>
											</div>
											
											<center>
												<strong> 
					<input name="add_button" type="submit" class="btn btn-success" value="Update GST Document" />
					<input name="add_button" type="submit" class="btn btn-success" value="Delete GST Document" />
 													</strong>
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
</body>
</HTML>
