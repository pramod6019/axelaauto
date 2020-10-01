<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.InsurComp_Doc" scope="page"/>
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
 <%@include file="../portal/header.jsp" %>
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
						<h1><%=mybean.status%> Document</h1>
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
							<li><a href="manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="manageinsurcomp.jsp?all=yes">List Companies</a> &gt;</li>
							<li><a href="manageinsurcomp.jsp?inscomp_id=<%=mybean.inscomp_id%>"><%=mybean.inscomp_name%></a> &gt;</li>
							<li><a href="insurcomp-doc.jsp?<%=mybean.QueryString%>">Update Document</a><b>:</b></li>
						</ul>
						<!-- END PAGE BREADCRUMBS -->
						<center>
						<font color="#FF0000" ><b><%=mybean.msg%></b></font>
						</center>
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<div class="portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">
								<%=mybean.status%> Document 
							</div>
						</div>
							<div class="portlet-body portlet-empty">
								<div class="tab-pane" id="">
									<!-- START PORTLET BODY -->
									<center>
									<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br>
			                          </font>
									</center>
									<form name="frmdoc" enctype="MULTIPART/FORM-DATA" method="post" class="form-horizontal">
									<div class="form-element6 form-element-center">
										<label> Select Document<font color=red>*</font>: </label>
										<strong>
										<input NAME="filename" Type="file" class="btn btn-success" id="filename" 
										value="<%=mybean.inscomp_value%>"/>
										</strong>	
										<span>Click the Browse button to select the document from your computer</span>
									</div>
										
									<div class="form-element6 form-element-center">
										<label> Title<font color=red>*</font>: </label>
										<input name="txt_doc_name" type="text" class="form-control" 
										id="txt_doc_name" value="<%=mybean.inscomp_title%>" maxlength="255"/>	
									</div>
									
									<div class="form-element6 form-element-center">
										<label>Allowed Formats : </label>
										<%=mybean.config_doc_format%>	
									</div>
									
									<div class="form-element6 form-element-center">
										<label>Maximum Size : </label>
										<b><%=mybean.config_doc_size%> Mb</b>
									</div>
									
									<center>
										<strong>
					                     <%if (mybean.status.equals("Add")) {%>
					                     
					                     <input name="add_button" type="submit" class="btn btn-success" value="Add Document" /> 
					                    
					                     <%} else if (mybean.status.equals("Update")) {%>
					                      
					                     <input name="update_button" type="submit" class="btn btn-success" value="Update Document"/>
					                      <input name="delete_button" type="submit" class="btn btn-success" OnClick="return confirmdelete(this)" value="Delete Document"/>
					                      
					                     <%}%>
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
<%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp" %>
<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.txt_doc_name.focus()
	}
</script> </body>
</html>
