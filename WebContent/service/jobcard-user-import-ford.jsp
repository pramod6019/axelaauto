<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.JobCard_User_Import_Ford" scope="request" />
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

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.dr_branch.focus();
	}
	
	function PopulateLocation(){
		var branch_id = document.getElementById("dr_branch").value;
		showHint('../accounting/accounting-branch-check.jsp?sales_branch_id='+branch_id+'&branch_location=yes','span_location');     
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
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Import Ford Job Cards</h1>
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
						<li><a href="vehicle.jsp">Vehicle</a> &gt;</li>
						<li><a href="../service/jobcard-user-import.jsp">Import
								Job Cards</a> &gt;</li>
						<li><a href="jobcard-user-import-ford.jsp">Import Ford
								Job Card</a><b>:</b></li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font>
						</center>

					</div>
					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form class="form-horizontal" name="frmdoc" id="frmdoc"
								enctype="MULTIPART/FORM-DATA" method="post"
								onsubmit="aletdisp();">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Download the
											Job Card template file</div>

									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing Job Card by downloading template file. <br />
												Download the template and enter Job Card data as per the
												headings. <br /> Headings marked in red are manadatory.
												Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/jobcard-template-ford-labour.xlsx"
													target="_blank"><b>Click here to download Job Card
														template Ford Labour Format</b></a>
											</center>
											<center>
												<a href="../Library/template/jobcard-template-ford-parts.xlsx"
													target="_blank"><b>Click here to download Job Card
														template Ford Parts Format</b></a>
											</center>
											<center>
												<a href="../Library/template/jobcard-template-ford-technician.xlsx"
													target="_blank"><b>Click here to download Job Card
														template Ford Technician Format</b></a>
											</center>
											<center>
												<a href="../Library/template/jobcard-template-incadea.xlsx"
													target="_blank"><b>Click here to download Job Card
														template Ford Incadea Format</b></a>
											</center>
											<br>



										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Import Ford Job
											Cards</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6 form-element-center">
												<label >Ford Workshop<font color=red>*</font>: </label>
													<select name="dr_branch" class="form-control"
														id="dr_branch" onchange="PopulateLocation();">
															<%=mybean.PopulateBranch(mybean.jc_branch_id, "", "3", mybean.brand_id, request)%>
													</select>
											</div>
											
											<div class="form-element6 form-element-center">
														<label>Location<font color="red">*</font>:</label>
														<span id="span_location" name="span_location">
													 <%= mybean.PopulateLocation()%>
															</select>
															</span>
													</div>
											
											<div class="form-element6 form-element-center">
														<label>Format<font color="red">*</font>:</label>
															<select name="dr_format_ford"
																class="form-control" id="dr_format_ford"
																visible="true">
																<%=mybean.PopulateFormatFord(mybean.comp_id, mybean.format_ford)%>
															</select>
													</div>

											<div class="form-element6 form-element-center">
												<label >Select Document<font color=red>*</font>: </label>
													<input NAME="filename" Type="file"
														class="button btn btn-success" id="filename"
														value="<%=mybean.doc_value%>" style="margin-left: 1px;" size="30">
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
											<!--  
                              <div id="dialog-modal" title="File Upload" align="center" >
                              <p align="center">Please wait...</p>
                              <img align="middle" src="../admin-ifx/loading.gif" /></div>-->
											<div align="center">
												<input name="addbutton" type="submit"
													class="button btn btn-success" id="addbutton"
													value="Upload" /> <input name="add_button1" type="hidden"
													class="button" id="add_button" value="Upload" /><input
													name="add_button1" type="hidden" class="button"
													id="add_button1" value="Upload" /><input
													name="add_button1" type="hidden" class="button"
													id="add_button1" value="Upload" />
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
