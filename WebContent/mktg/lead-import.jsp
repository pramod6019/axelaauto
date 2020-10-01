<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.mktg.Lead_Import" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <HEAD>
        <title><%=mybean.AppName%> - <%=mybean.ClientName%></title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
        <link rel="shortcut icon"  type="image/x-icon" href="../admin-ifx/axela.ico">     
        <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/jquery-ui.css">
        <LINK REL="STYLESHEET" TYPE="text/css" HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
        <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css" rel="stylesheet" media="screen" type="text/css" />
        <link href="../Library/theme<%=mybean.GetTheme(request)%>/menu-mobile.css" rel="stylesheet" media="screen" type="text/css" />
        <script type="text/javascript" src="../Library/Validate.js"></script>
        <script type="text/javascript" src="../Library/dynacheck.js"></script>
        <script type="text/javascript" src="../Library/jquery.js"></script>
        <script type="text/javascript" src="../Library/jquery-ui.js"></script>
        <script type="text/javascript" src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
        <script type="text/javascript">
			
			$(document).ready(function(){
				
				$("#frmdoc").submit(function(){
					//alert("exe.nvbnv.");
					//return true;
					$('#dialog-modal').dialog('open');
					//return true;
					}
				)	
				
				// Dialog
				$('#dialog-modal').dialog({
					autoOpen: false,
					width : 200,
					height: 120,
                    //zIndex: 200,
					modal: true,
					title: "File Upload in progress!" ,
					draggable : false,
					closeOnEscape: false,
					open: function(event,ui){$(".ui-dialog-titlebar-close").hide();}
				});		
			});
		</script>
        <script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.frmdoc.dr_branch.focus();
}

        </script>
        </HEAD>

<body class="page-container-bg-solid page-header-menu-fixed"
	onload="FormFocus()">
	<%@include file="../portal/header.jsp"%>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!----- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Import Lead:</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="index.jsp">Marketing</a> &gt;</li>
						<li> <a href="../mktg/lead.jsp">Lead</a> &gt; </b></li>
						<li><a href="enquiry-user-import-general.jsp">Import General Enquiries</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
					</div>
					
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form class="form-horizontal" name="frmdoc" id="frmdoc"
								enctype="MULTIPART/FORM-DATA" method="post">
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Lead
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<td align="left" colspan="2">
											<font size="1">Form fields marked with a red asterisk <b><font color="#ff0000">*</font></b> are required.<br />
                                            </font></td>
                                            
											<div class="form-group">
												<label class="control-label col-md-4"
													">Branch<font color=red>*</font>:
												</label>
												<div class="col-md-5 col-xs-12" id="emprows">
													<select name="enquiry_branch_id" class="form-control"
														id="enquiry_branch_id">
														 <%=mybean.PopulateBranch(mybean.lead_branch_id, "", request)%>
													</select>
												</div>
											</div>

											<div class="form-group">
												<label class="control-label col-md-4"
													style="margin-top: 10px;">Select Document<font
													color=red>*</font>:
												</label>
												<div class="col-md-6 col-xs-12" id="emprows">

													<input NAME="filename" Type="file"
														class="button btn btn-success" id="filename"
														value="<%=mybean.doc_value%>" size="30">
												</div>
											</div>

											<div align="center">
												<font size="">Click the Browse button to select the
													document from your computer!</font>
											</div>
											<div colspan="2" align="center">
												Allowed Formats: <b><%=mybean.importdocformat%></b>
											</div>
											<div colspan="2" align="center">
												Maximum Size: <b><%=mybean.docsize%> Mb</b>
											</div>
											
											<div align="center">
							<div id="dialog-modal" title="File Upload" align="center" >
                              <p align="center">Please wait...</p>
                              <img align="middle" src="../admin-ifx/loading.gif" /></div>
                              <input name="addbutton" type="submit" class="button" id="addbutton" value="Upload"/>
                              <input name="add_button" type="hidden" class="button" id="add_button" value="Upload"/>
											</div>

										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>


	<%@include file="../Library/admin-footer.jsp"%>

	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<link rel="shortcut icon" type="image/x-icon"
		href="../admin-ifx/axela.ico">
</body>
</html>