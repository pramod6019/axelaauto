<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Service_Target_User_Import" scope="request"/>  
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <HEAD>
    <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
  <%@include file="../Library/css.jsp" %>
</HEAD>

        <body class="page-container-bg-solid page-header-menu-fixed" onload="FormFocus()">
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
						<h1>Import Service Target</h1>
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
						<li><a href="../portal/home.jsp">Home</a> &gt; </li>
						<li><a href="index.jsp">Service</a> &gt; </li>
				<li><a href="service-target-user-import.jsp">Import Service Target</a>: </li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
					</div>
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!--BODY START -->
							<form class="form-horizontal" name="frmdoc" id="frmdoc" enctype="MULTIPART/FORM-DATA" method="post"
								onsubmit="aletdisp();">
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Download the Service Target template file</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing Service Target by downloading template file. <br />
												Download the template and enter Service Target as per the
												headings. <br /> Headings marked in red are manadatory.
												Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/service-target-template.xlsx"
													target="_blank"><b>Click here to download Service Target
														template </b></a>
											</center>
											<br>
										</div>
									</div>
								</div>
								<div class="portlet box ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Service Target</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6 form-element-center">
												<label>Branch<font color=red>*</font>:</label>
													<select name="dr_branch" id="dr_branch"
														class="form-control" >
														<%=mybean.PopulateBranches(mybean.branch_id, mybean.comp_id)%>
													</select>
											</div>
											<div class="form-element6 form-element-center form-element">
												<div class="form-element6 ">
												<label> Month<font color=red>*</font>:&nbsp; </label>
														<select name="dr_month" class="form-control" id="dr_month" >
														<%=mybean.PopulateMonth()%>
													</select>
												</div>
												<div class="form-element6">
												<label >Year<font color=red>*</font>:&nbsp; </label>
													<select name="dr_year" class="form-control" id="dr_year" >
														<%=mybean.PopulateYear()%>
													</select>
											</div>
											</div>
											
											<div class="row"></div>
											<div class="form-element6 form-element-center">
												<label>Select Document<font color=red>*</font>: </label>
													<input NAME="filename" Type="file"
														class="button btn btn-success" id="filename"
														style="margin-left: 1px;" value="<%=mybean.doc_value%>" size="30">
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
												<input name="addbutton" type="submit" class="button btn btn-success" id="addbutton"
													value="Upload" /> 
												<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
												<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
												<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
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

	</div>

  
   <%@include file="../Library/admin-footer.jsp" %>
    <%@include file="../Library/js.jsp" %>
        
        <script type="text/javascript">      
			  
// 			$(document).ready(function(){
				
// 				$("#frmdoc").submit(function(){
// 					//alert("exe.nvbnv.");
// 					//return true;
// 					$('#dialog-modal').dialog('open');
// 					//return true;
// 					}
// 				)	
				
// 				// Dialog
// 				$('#dialog-modal').dialog({
// 					autoOpen: false,
// 					width : 200,
// 					height: 120,
//                     //zIndex: 200,
// 					modal: true,
// 					title: "File Upload in progress!" ,
// 					draggable : false,
// 					closeOnEscape: false,
// 					open: function(event,ui){$(".ui-dialog-titlebar-close").hide();}
// 				});		
// 			});
		</script>
        <script language="JavaScript" type="text/javascript">
function FormFocus() { //v1.0
  document.frmdoc.dr_branch.focus();
}
        </script>
  </body>
</html>
