<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Insurance_User_Import" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>


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
<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed"
	onload="FormFocus()">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Import Insurance Enquiry</h1>
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
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<li><a href="insurance-user-import.jsp">Import Insurance Enquiry</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>
					</div>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="frmdoc" id="frmdoc" enctype="MULTIPART/FORM-DATA"
								method="post" class="form-horizontal">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Download the
											Insurance Enquiry template file</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing Insurance Enquiry by downloading template file. <br />
												Download the template and enter Vehicle data as per the
												headings. <br /> Headings marked in red are manadatory.
												Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/insurance-vehicle-template.xlsx" target="_blank"><b>Click
														here to download Insurance Enquiry template</b></a>
											</center>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Insurance Enquiry</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6">
												<label>Branch<font color=red>*</font>: </label>
													<select name="dr_branch" class="form-control" id="dr_branch">
  														<%=mybean.PopulateBranch(mybean.insurenquiry_branch_id, "", "6", "", request)%>
													</select>
											</div>
											<div class="form-element6">
												<label>Insurance Executive:</label>
													<select class="form-control select2-multiple"  id="assigninsurcre" name="assigninsurcre" multiple>
														<%=mybean.crecheck.PopulateCreids(mybean.comp_id, mybean.empids)%>
													</select>
											</div>
<!-- 											<div class="form-element6"> -->
<!-- 												<label>Insurance Executive: </label> -->
<!-- 													<select id="dr_insur_emp_id" name="dr_insur_emp_id" -->
<!-- 														class="form-control"> -->
<%-- 														<%=mybean.PopulateInsurExecutive(mybean.comp_id)%> --%>
<!-- 													</select> -->
<!-- 											</div> -->
<!-- 											<div class="form-group"> -->
<!-- 												<label class="control-label col-md-4">Insurance Follow-up By -->
<!-- 												<font color="#ff0000">*</font>: -->
<!-- 											</label>  -->
<!-- 												<div class="col-md-6 col-xs-12" id="emprows"> -->
<!-- 													<select id="dr_veh_insfollowupby" name="dr_veh_insfollowupby" -->
<!-- 														class="form-control"> -->
<%-- 														<%=mybean.PopulateInsFollowup(mybean.veh_insfollowupby, mybean.comp_id)%> --%>
<!-- 													</select> -->
<!-- 												</div> -->
<!-- 											</div> -->
											<div class="row"></div>
											<div class="form-element6">
												<label>Insurance Type <font color="#ff0000">*</font>: </label>
													<select id="dr_insur_type_id" name="dr_insur_type_id"
														class="form-control">
														<%=mybean.PopulateInsuranceType()%>
													</select>
											</div>
											
											<div class="form-element6">
												<label>Select Document<font color=red>*</font>: </label>
													<input NAME="filename" Type="file" class="btn btn-success"
														id="filename" value="<%=mybean.doc_value%>" size="30">
											</div>
											<div class="form-element12">
												<center>
													<font size="">Click the Browse button to select the
														document from your computer!</font><br></br>
													Allowed Formats: <b><%=mybean.importdocformat%></b><br></br>
													Maximum Size: <b><%=mybean.docsize%> Mb</b><br></br>
													<div style="display: none" id="dialog-modal"
														title="File Upload" align="center">
														<p align="center">Please wait...</p>
														<img align="middle" src="../admin-ifx/loading.gif" />
													</div>
													<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Upload" />
													<input name="add_button" type="hidden" class="button" id="add_button" value="Upload" />
													<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
													<input name="add_button2" type="hidden" class="button" id="add_button2" value="Upload" />
													<input name="add_button3" type="hidden" class="button" id="add_button3" value="Upload" />
													<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
													<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
													<input name="add_button6" type="hidden" class="button" id="add_button6" value="Upload" />
													<input name="add_button7" type="hidden" class="button" id="add_button7" value="Upload" /> 
													
													<input name="add_button" type="hidden" class="button" id="add_button" value="Upload" />
													<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
													<input name="add_button2" type="hidden" class="button" id="add_button2" value="Upload" />
													<input name="add_button3" type="hidden" class="button" id="add_button3" value="Upload" />
													<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
													<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
													<input name="add_button6" type="hidden" class="button" id="add_button6" value="Upload" />
													<input name="add_button7" type="hidden" class="button" id="add_button7" value="Upload" /> 
													
													<input name="add_button" type="hidden" class="button" id="add_button" value="Upload" />
													<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
													<input name="add_button2" type="hidden" class="button" id="add_button2" value="Upload" />
													<input name="add_button3" type="hidden" class="button" id="add_button3" value="Upload" />
													<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
													<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
													<input name="add_button6" type="hidden" class="button" id="add_button6" value="Upload" />
													<input name="add_button7" type="hidden" class="button" id="add_button7" value="Upload" /> 
													
													<input name="add_button" type="hidden" class="button" id="add_button" value="Upload" />
													<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
													<input name="add_button2" type="hidden" class="button" id="add_button2" value="Upload" />
													<input name="add_button3" type="hidden" class="button" id="add_button3" value="Upload" />
													<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
													<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
													<input name="add_button6" type="hidden" class="button" id="add_button6" value="Upload" />
													<input name="add_button7" type="hidden" class="button" id="add_button7" value="Upload" /> 
													
													<input name="add_button" type="hidden" class="button" id="add_button" value="Upload" />
													<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
													<input name="add_button2" type="hidden" class="button" id="add_button2" value="Upload" />
													<input name="add_button3" type="hidden" class="button" id="add_button3" value="Upload" />
													<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
													<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
													<input name="add_button6" type="hidden" class="button" id="add_button6" value="Upload" />
													<input name="add_button7" type="hidden" class="button" id="add_button7" value="Upload" /> 
													
													<input name="add_button" type="hidden" class="button" id="add_button" value="Upload" />
													<input name="add_button1" type="hidden" class="button" id="add_button1" value="Upload" />
													<input name="add_button2" type="hidden" class="button" id="add_button2" value="Upload" />
													<input name="add_button3" type="hidden" class="button" id="add_button3" value="Upload" />
													<input name="add_button4" type="hidden" class="button" id="add_button4" value="Upload" />
													<input name="add_button5" type="hidden" class="button" id="add_button5" value="Upload" />
													<input name="add_button6" type="hidden" class="button" id="add_button6" value="Upload" />
													<input name="add_button7" type="hidden" class="button" id="add_button7" value="Upload" /> 
												</div>
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
	<%@include file="../Library/js.jsp"%>
</body>
</html>
