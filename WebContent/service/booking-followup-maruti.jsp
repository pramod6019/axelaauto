<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id=" mybean" class="axela.service.Booking_Followup_Maruti"
	scope="request" />
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
<%@include file="../Library/css.jsp"%>
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
						<h1>Maruti DMS Import</h1>
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
						<li><a href="booking-followup.jsp">Import Follow-Up</a> &gt;</li>
						<li><a href="../service/booking-followup-maruti.jsp">Maruti
								DMS Import</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div>
						<center>
							<font color="#FF0000"><b><%=mybean.msg%></b></font> <font
								color="#FF0000"><b><%=mybean.veh_error_msg%></b></font>
						</center>
					</div>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form class="form-horizontal" name="frmdoc" id="frmdoc"
								enctype="MULTIPART/FORM-DATA" method="post"
								onsubmit="aletdisp();">
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Download the
											DMS Import template file</div>

									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												Start importing DMS Import by downloading template file. <br />
												Download the template and enter DMS Import data as per the
												headings. <br /> Headings marked in red are manadatory.
												Don't change the header columns.
											</center>
											<center>
												<a href="../Library/template/booking-followup-maruti.xlsx"
													target="_blank"><b>Click here to download Maruti
														DMS Import template </b></a>
											</center>
											<br>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Maruti DMS
											Import</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element6">
												<label>Branch<font color=red>*</font>:
												</label>
													<select name="dr_branch" id="dr_branch"
														class="form-control" style="margin-top: 9px;">
														<%=mybean.PopulateBranch(mybean.comp_id)%>
													</select>
											</div>
											<div class="form-element6">
												<label >CRM Executive
													<font color="#ff0000">*</font>:
												</label>
													<select id="dr_veh_crmemp_id" name="dr_veh_crmemp_id"
														class="form-control">
														<%=mybean.PopulateCRMExecutive(mybean.comp_id)%>
													</select>
											</div>
											<div class="form-element6">
												<label >Select Document<font
													color=red>*</font>:
												</label>

													<input NAME="filename" Type="file" style="margin-top: 9px;"
														class="button btn btn-success" id="filename"
														value="<%=mybean.doc_value%>" size="30">
											</div>
<div class="row"></div>
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

<script language="JavaScript" type="text/javascript">
	function FormFocus() { //v1.0
		document.frmdoc.dr_branch.focus();
	}
</script>
</body>
</html>
