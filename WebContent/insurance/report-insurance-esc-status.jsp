<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Esc_Status" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
 <%@include file="../portal/header.jsp" %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Insurance Escalation Status</h1>
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
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/mis.jsp">MIS</a> &gt; </li>
							<li><a href="report-insurance-esc-status.jsp">Insurance Escalation Status</a><b>: </b></li>
							
						</ul>
						<center>
							<font color="#ff0000" ><b><%=mybean.msg%></b></font>
						</center>

						<div class="portlet box  ">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Search</div>
							</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<form name="formemp" method="post">

										<div class="form-element6 form-element-center">
											<label>Branch: </label>
											<%
												if (mybean.branch_id.equals("0")) {
											%>
											<select name="dr_branch_id" class="form-control"
												id="dr_branch_id" onChange="document.formemp.submit()">
												<%=mybean.PopulateBranch(mybean.dr_branch_id, "", "6", "", request)%>
											</select>
											<%
												} else {
											%>
											<input type="hidden" name="dr_branch" id="dr_branch"
												value="<%=mybean.branch_id%>" />
											<%=mybean.getBranchName(mybean.dr_branch_id, mybean.comp_id)%>
											<%
												}
											%>
										</div>
										<br />
										<center>
											<div>
												<%=mybean.StrHTML%></div>
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
    <%@include file="../Library/admin-footer.jsp" %>
    <%@include file="../Library/js.jsp" %>
    <script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  //document.formcontact.txt_customer_name.focus(); 
}
function frmSubmit() 
{
	document.formemp.submit();
}
    </script>
    </body>
</html>
