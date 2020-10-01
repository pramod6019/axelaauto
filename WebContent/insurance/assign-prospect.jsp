<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.insurance.Assign_Prospect"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<jsp:setProperty name="mybean" property="*" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@include file="../Library/css.jsp"%>
<style>
.multiselect-container{
/* display: none; */
overflow-y: scroll;
max-block-size: 100px;
}
</style>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Assign Prospect</h1>
					</div>
			</div>		
					<!-- END PAGE TITLE -->
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="page-content-inner">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../insurance/index.jsp">Insurance</a> &gt;</li>
						<li><a href="../insurance/assign-prospect.jsp">Assign Prospect</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Unassigned Prospect</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
									<div id="prospectcount">
                                      <%=mybean.PopulateProspectCountByBucket("0",mybean.comp_id, request) %>
                                      </div>
									</div>
								</div>
							</div>

							<!-- 							//prospect count end -->


							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Assign Prospect</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formprospect" class="form-horizontal" method="post">

													<div class="form-element4">
														<label> From Time<font color="#ff0000">*</font>: </label>
															<input name="txt_assignstarttime" id="txt_assignstarttime" value="<%=mybean.assignstarttime%>"
																class="form-control datetimepicker" type="text" maxlength="10" />
													</div>
													
													<div class="form-element4">
														<label> To Time<font color="#ff0000">*</font>: </label>
															<input name="txt_assignendtime" id="txt_assignendtime" value="<%=mybean.assignendtime%>"
																class="form-control datetimepicker" 
																type="text" maxlength="10" />
													</div>
													
													<div class="form-element4">
														<label>Branch: </label>
														<div>
															<select name="dr_assignveh_branch_id" class="form-control multiselect-dropdown" multiple="multiple"
																id="dr_assignveh_branch_id"
																onchange="populateprospectcount(this.id);">
																<%=mybean.PopulateBranches(mybean.assignveh_branch_ids, mybean.comp_id, request)%>
															</select>
														</div>	
													</div>
													
													<div class="form-element4">
														<label>Prospect Count<font color="#ff0000">*</font>: </label>
															<input type="text"  class="form-control" id="txt_assigntransfer_prospect_count" name="txt_assigntransfer_prospect_count"
															onKeyUp="toNumber('txt_assigntransfer_prospect_count','Value')" 
															value=<%=mybean.assigntransfer_prospect_count %>>
													</div>
													
													<div class="form-element4">
														<label> Insurance Follow-up By<font color="#ff0000">*</font>: </label>
															<select name="dr_assignveh_insfollowupby" class="form-control"
																id="dr_assignveh_insfollowupby">
																<%=mybean.PopulateFollowupBy(mybean.assignveh_insfollowupby, mybean.comp_id)%>
															</select>
													</div>
													
													<div class="form-element4">
														<label> Insurance Type : </label>
															<select name="dr_assignveh_insurtype" class="form-control"
																id="dr_assignveh_insurtype">
																<%=mybean.PopulateInsuranceType()%>
															</select>
													</div>
													
													<div class="row"></div>
													<div class="form-element4">
														<label> SOE: </label>
															<select name="dr_assigninsurfollowup_soe_id" id="dr_assigninsurfollowup_soe_id"
															class=" form-control" onchange="populateSob();">
															<%=mybean.PopulateSoe(mybean.comp_id)%>
														</select>
													</div>
													
													<div class="form-element4">
														<label> SOB: </label>
															<span id="assignHintSob"> 
															<%=mybean.PopulateSOB(mybean.assigninsurfollowup_sob_id, mybean.assigninsurfollowup_soe_id, mybean.comp_id)%>
															</span>
													</div>
													<div class="form-element4">
															<label> MNF. Year: </label>
															<select id="manufyear" name="manufyear" class="form-control">
																<%=mybean.PopulateYear()%>
																</select>
														</div>
														<div class="row"></div>
													<div class="form-element4">
														<label>Manuf:</label>
														<div>
														<select name="dr_carmanuf_id" 
																multiple="multiple" class="form-control multiselect-dropdown"
																id="dr_carmanuf_id" onchange="PopulateModel('');">
																<%=mybean.insurmis.PopulateManufacturer(mybean.assignveh_carmanuf_ids, mybean.assignveh_carmanuf_id, mybean.comp_id, request)%>
														</select>
														</div>
													</div>
													
													<div class="form-element4">
														<label>Model:</label>
														<div id="modelHint">
																<%=mybean.insurmis.PopulatePreOwnedModels(mybean.assignveh_preownedmodel_id, mybean.assignveh_preownedmodel_ids, "dr_preowned_model", mybean.comp_id, request)%>
														</div>
													</div>
													<div class="row"></div>
													
											<div class="form-element10">
												<label>CRE<font color="#ff0000">*</font>: </label>
													<select class="form-control select2-multiple"  id="assigninsurcre" name="assigninsurcre" multiple>
														<%=mybean.crecheck.PopulateCreids(mybean.comp_id, mybean.assigninsurfollowup_cre_id)%>
													</select>
											</div>
											
											
											<div class="form-element12">
											<center>
												<input type="hidden" name="update_button" value="Assign Prospect" /> <input
													name="assignprospect" type="submit" class="btn btn-success"
													id="assignprospect" value="Assign" />
											</center>
											</div>
										</form>
									</div>
								</div>
							</div>
							
							<!-- 							reassign part stats -->
								<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Re-Assign Prospect</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="formreassignprospect" class="form-horizontal" method="post">

													<div class="form-element3">
														<label> From Follow-up Time<font color="#ff0000">*</font>: </label>
															<input name="txt_reassignstarttime" id="txt_reassignstarttime" value="<%=mybean.reassignstarttime%>"
																class="form-control datetimepicker"
																type="text" maxlength="10" />
													</div>
													
													<div class="form-element3">
														<label> To Follow-up Time<font color="#ff0000">*</font>: </label>
															<input name="txt_reassignendtime" id="txt_reassignendtime" value="<%=mybean.reassignendtime%>"
																class="form-control datetimepicker"
																type="text" maxlength="10"/>
													</div>
													
													
													<div class="form-element3">
														<label>Prospect Count: </label>
															<input type="text"  class="form-control" id="txt_reassigntransfer_prospect_count" name="txt_reassigntransfer_prospect_count"
															onKeyUp="toNumber('txt_reassigntransfer_prospect_count','Value')" 
															value=<%=mybean.reassigntransfer_prospect_count %>>
													</div>
													
													<div class="form-element3">
														<label>Branch: </label>
														<div>
															<select name="dr_reassignveh_branch_id" class="form-control multiselect-dropdown" multiple="multiple"
																id="dr_reassignveh_branch_id"
																onchange="populateprospectcount(this.id);">
																<%=mybean.PopulateBranches(mybean.reassignveh_branch_ids, mybean.comp_id, request)%>
															</select>
														</div>	
													</div>
													
													<div class="form-element3">
														<label> Disposition:</label>
															<select
																name="dr_reassignveh_insurenquirydisposition_id" class="form-control"
																id="dr_reassignveh_insurenquirydisposition_id">
																	<%=mybean.PopulateDisposition()%>
															</select> 
													</div>

												<div class="form-element3">
														<label>Manuf:</label>
														<div>
														<select name="dr_reassign_carmanuf_id" 
																multiple="multiple" class="form-control multiselect-dropdown"
																id="dr_reassign_carmanuf_id" onchange="PopulateModel('reassign');">
																<%=mybean.insurmis.PopulateManufacturer(mybean.reassignveh_carmanuf_ids, mybean.reassignveh_carmanuf_id, mybean.comp_id, request)%>
														</select>
														</div>
													</div>
													
													<div class="form-element3">
														<label>Model:</label>
														<div id="ReassignModelHint">
																<%=mybean.insurmis.PopulatePreOwnedModels(mybean.reassignveh_preownedmodel_id, mybean.reassignveh_preownedmodel_ids, "dr_reassign_preowned_model", mybean.comp_id, request)%>
														</div>
													</div>
													<div class="row"></div>
													
													
											
											<div class="form-element10">
												<label>From CRE<font color="#ff0000">*</font>: </label>
													<select class="form-control select2-multiple"  id="reassignfrominsurcre" name="reassignfrominsurcre" multiple>
														<%=mybean.crecheck.PopulateCreids(mybean.comp_id, mybean.reassignfrominsurfollowup_cre_id)%>
													</select>
											</div>
											
											
											<div class="form-element10">
												<label>To CRE<font color="#ff0000">*</font>: </label>
													<select class="form-control select2-multiple"  id="reassigntoinsurcre" name="reassigntoinsurcre" multiple>
														<%=mybean.crecheck.PopulateCreids(mybean.comp_id, mybean.reassigntoinsurfollowup_cre_id)%>
													</select>
											</div>
											
											
											<div class="form-element12">
											<center>
												<input type="hidden" name="update_button" value="Re-Assign Prospect" /> <input
													name="reassignprospect" type="submit" class="btn btn-success"
													id="reassignprospect" value="Re Assign" />
											</center>
											</div>
										</form>
									</div>
								</div>
							</div>
							
							<!-- 							reassign part stats/ -->
							
							
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	
	<script type="text/javascript">
	function populateSob(){
		 var insurfollowup_soe_id = document.getElementById('dr_assigninsurfollowup_soe_id').value;
	  showHint('../insurance/mis-check.jsp?insurfollowup_sob_id=yes&insurfollowup_soe_id='+insurfollowup_soe_id, 'assignHintSob');
	  }
	
	function PopulateModel(reassign) {
		var Hint='modelHint';
		var carmanuf_id = outputSelected(document.getElementById("dr_carmanuf_id").options);
		containerid = 'dr_preowned_model';
		if(reassign=='reassign'){
			Hint = 'ReassignModelHint';
			carmanuf_id = outputSelected(document.getElementById("dr_reassign_carmanuf_id").options);
			containerid = 'dr_reassign_preowned_model';
		}
		
		showHint('../insurance/mis-check.jsp?preownedmodelmulti=yes&carmanuf_id=' + carmanuf_id +'&containerid=' + containerid, Hint);
	}
	
	function populateprospectcount(branch){
		if(branch=='dr_assignveh_branch_id'){
			var assignveh_branch_id = $('#dr_assignveh_branch_id').val();
			showHint('../insurance/mis-check.jsp?prospectcount=yes&insurenquiry_branch_id=' + assignveh_branch_id, 'prospectcount');
		}else{
			var reassignveh_branch_id = $('#dr_reassignveh_branch_id').val();
			showHint('../insurance/mis-check.jsp?prospectcount=yes&insurenquiry_branch_id=' + reassignveh_branch_id, 'prospectcount');
		}
		
	}
	
	</script>
</body>
</HTML>
