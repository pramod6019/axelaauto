<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Movement_Add"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>

</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed" >

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
						<h1>Vehicle Movement</h1>
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
						<li><a href="index.jsp">Service</a> &gt;</li>
						<li><a href="movement-add.jsp">Vehicle Movement</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										<center>Vehicle Movement</center>
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
										
									<div class="tab-pane" id="">
										<form name="form1" id="form1" enctype="MULTIPART/FORM-DATA" method="post" class="form-horizontal">
										
											<br> <br>
											<div class="form-element4"></div>
											<div class="form-element6">
												<label >Branch<font
														color="#ff0000">*</font>: &nbsp;
												</label>
												<%
													System.out.println("branch_id --" + mybean.branch_id);
													if (mybean.branch_id.equals("0")) {
												%>
													<select name="dr_vehmove_branch_id" class="form-control"
														id="dr_vehmove_branch_id">
														<%=mybean.PopulateBranch()%>
													</select>
												<%
													} else {
												%>
													<input type="hidden" id="dr_vehmove_branch_id"
														name="dr_vehmove_branch_id"
														value="<%=mybean.vehmove_branch_id%>" />
													<%=mybean.getBranchName(mybean.vehmove_branch_id, mybean.comp_id)%>
												<%
													}
												%>
											</div>
											
											<div class="form-element2">
											  <center>
												<input name="print_button" type="button" 
												class="btn btn-success" id="print_button" value="Print"
												onclick="PrintMovement('in');" style="margin-top: 23px;"/>
											  </center>	
											</div>
											
											
												<center style="text-align: center">
													<font size=""><b>Vehicle IN</b></font>
												</center>
										</br>
											<div class="row">
												
												<div class="form-element3">
													<label>Reg.No.<font color=red>*</font>:&nbsp; </label>
													<input name="txt_vehmove_in_reg_no" type="text"
																class="form-control" id="txt_vehmove_in_reg_no"
																value="<%=mybean.vehmove_reg_no_in%>" size="10"
																maxlength="10" />
												</div>
												
												

												<div class="form-element3">
														<label >KMS IN: &nbsp;
														</label>
															<input name="txt_vehmove_kms_in" type="text"
																class="form-control" id="txt_vehmove_kms_in"
																value="<%=mybean.vehmove_kms_in%>" size="10"
																maxlength="9" onkeyup="toInteger(this.id);">
												</div>
												
												<div class="form-element3">
														<label >JC Type<font
															color="#ff0000">*</font>: &nbsp;
														</label>
															<select name="dr_vehmove_jc_id" class="form-control"
														id="dr_vehmove_jc_id">
														<%=mybean.PopulateJCType()%>
													</select>
												</div>

												<div class="form-element3 form-element-margin">
														<label >Internal:&nbsp; </label>
															<input name="chk_vehmove_internal_in" type="checkbox"
																id="chk_vehmove_internal_in"
																<%=mybean.PopulateCheck(mybean.vehmove_internal)%>>
												</div>
											</div>
											
											<div class="form-element3">
													<label>Mobile 1<font color="#ff0000">*</font>: </label>
													<input name="txt_contact_mobile1" type="text" class="form-control"
														id="txt_contact_mobile1" size="32" maxlength="13" 
														onkeyup="toPhone('txt_contact_mobile1','Contact Mobile1');"
														value="<%=mybean.contact_mobile1 %>" />
													<span id="showcontactsmobil1" class="hint" ></span>
												</div>
											<input  id="addnfields" type="hidden" />
											<div id="adddetails" hidden>
                                                   <div class="form-element1 form-element">
														<label >Title<font color="#ff0000">*</font>:&nbsp; </label>
														 <select name="dr_title"
															class="form-control" id="dr_title">
																<%=mybean.PopulateTitle(mybean.comp_id)%>
														</select>
													</div>

												<div class="form-element3">
													<label>Customer Name<font color="#ff0000">*</font>&nbsp;</label>
													<input class="form-control" name="txt_contact_name" type="text"
														id="txt_contact_name" 
														value="<%=mybean.contact_name %>"
														size="50" maxlength="255" />
												</div>
												
												<div class="form-element3">
													<label >Variant <font color="#ff0000">*</font>: &nbsp; </label>
													<select class="form-control select2" id="servicevariant" name="servicevariant">
														<%=mybean.vehincheck.PopulateVariant(mybean.variant_id, mybean.comp_id)%>
													</select>
												</div>
												
											</div>
											
											<div class="row"></div>
											
											<div class="form-element6">
												<label >Select Document <font color="#ff0000">*</font>&nbsp;: </label>
													<input NAME="filename" Type="file"
														class="button btn btn-success" id="filename"
														style="margin-left: 1px;" value="<%=mybean.doc_value%>" size="30">
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<font size="">Click the Browse button to select the document from your computer!</font>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												Allowed Formats: <b><%=mybean.uploaddocformat%></b>
											</div>
											<div class="row"></div>
											<div class="form-element6">
												Maximum Size: <b><%=mybean.docsize%> Mb</b>
											</div>
											
											<div class="row"></div>
											
											
												<center>
													<div class="form-element6 form-element-center">
												<input name="inbutton" type="submit" class="btn btn-success"
													id="inbutton" value="IN" />
													</div>
													
												<!-- <input name="inbutton1" type="hidden" class="button" id="inbutton1" value="IN" />
												<input name="inbutton2" type="hidden" class="button" id="inbutton2" value="IN" />
												<input name="inbutton3" type="hidden" class="button" id="inbutton3" value="IN" />
												<input name="inbutton4" type="hidden" class="button" id="inbutton4" value="IN" />
												<input name="inbutton5" type="hidden" class="button" id="inbutton5" value="IN" />
												<input name="inbutton6" type="hidden" class="button" id="inbutton6" value="IN" />
												<input name="inbutton7" type="hidden" class="button" id="inbutton7" value="IN" />
												<input name="inbutton8" type="hidden" class="button" id="inbutton8" value="IN" />
												<input name="inbutton9" type="hidden" class="button" id="inbutton9" value="IN" />
												<input name="inbutton10" type="hidden" class="button" id="inbutton10" value="IN" />
												<input name="inbutton11" type="hidden" class="button" id="inbutton11" value="IN" />
												<input name="inbutton12" type="hidden" class="button" id="inbutton12" value="IN" /> -->
<!-- 											$(".select2-selection__placeholder").text(this.value); -->
													
											</center>
											
											
												<center style="text-align: center">
													<font size=""><b>Vehicle Out</b></font>
												</center>
											<div class="row ">
															<div class="form-element6 ">
																	<label >Reg.No.<font
																		color="#ff0000">*</font>: &nbsp;
																	</label>
																		<input name="txt_vehmove_out_reg_no" type="text"
																			class="form-control" id="txt_vehmove_out_reg_no"
																			value="<%=mybean.vehmove_reg_no_out%>" size="30"
																			maxlength="20">
															</div>

															<div class="form-element6">
														<label >KMS OUT<font
															color="#ff0000">*</font>:&nbsp;
														</label>
															<input name="txt_vehmove_kms_out" type="text" 
																class="form-control text-align" id="txt_vehmove_kms_out"
																value="<%=mybean.vehmove_kms_out%>" size="10"
																maxlength="9" onkeyup="toInteger(this.id);">
													</div>
												</div>

												<%-- <div class="form-body col-md-4 col-sm-6">
													<div class="form-group">
														<label class="col-md-3 control-label">Internal: </label>
														<div class="txt-align" style="top: 9px">
															<input name="chk_vehmove_internal_out" type="checkbox"
																id="chk_vehmove_internal_out"
																<%=mybean.PopulateCheck(mybean.vehmove_internal)%>>
														</div>
													</div>
												</div> --%>
											</div>
											<div>
												<center>
												<div class="form-element6 form-element-center">
													<input name="outbutton" type="submit"
														class="btn btn-success" id="outbutton" value="OUT" />
												</div></center>
											</div>
											
										<!-- 	<input name="outbutton1" type="hidden" class="button" id="outbutton1" value="OUT" />
											<input name="outbutton2" type="hidden" class="button" id="outbutton2" value="OUT" />
											<input name="outbutton3" type="hidden" class="button" id="outbutton3" value="OUT" /> -->

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
	<%@include file="../Library/js.jsp"%>
<script type="text/javascript">
	
// servicevariant
	function PrintMovement(param) {
		window.open('../service/movement-print.jsp', '_blank');
	}
	
	
	$(function(){
		$('#txt_vehmove_in_reg_no').focusout(function(){
			var regno = $('#txt_vehmove_in_reg_no').val();
			var veh_id="";
			regno = regno.trim();
			showHint1('../service/vehicle-in-check.jsp?vehmove_reg_no='+regno, 'addnfields');
		});
	});
	

		function showHint1(url, Hint) {
			//alert(url);
					$('#'+Hint).html('<div id=loading align=center><img align=center alt="test" src=\"../admin-ifx/loading.gif\" /></div>');
						$.ajax({
							url: url,
							type: 'GET',
							success: function (data){
									if(data.trim() != 'SignIn'){
									$('#'+Hint).show();
									$('#'+Hint).fadeIn(500).html('' + data.trim() + '');
									FormElements();
									if(data.trim()=='0'){
										$('#adddetails').show();
									}else{
										$('#adddetails').hide();
									}
									} else{
									window.location.href = "../portal/";
									}
							}
						});
				}
	
</script>
</body>
</HTML>
