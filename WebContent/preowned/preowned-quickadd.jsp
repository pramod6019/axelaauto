<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_Quickadd"
	scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" />
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</HEAD>

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
						<h1>Add <%=mybean.ReturnPreOwnedName(request)%></h1>
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
						<li><a href="../portal/home.jsp?all=yes">Home</a> &gt;</li>
						<li><a href="index.jsp"><%=mybean.ReturnPreOwnedName(request)%></a> &gt;</li>
						<!-- 						<li><a href="preowned.jsp?all=yes">Pre Owned</a>&gt;</li> -->
						<li><a href="preowned-list.jsp?all=yes">List <%=mybean.ReturnPreOwnedName(request)%></a>&gt;</li>
						<li><a href="preowned-quickadd.jsp">Add <%=mybean.ReturnPreOwnedName(request)%></a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

				
						<div class="tab-pane" id="">
							<!-- 					BODY START -->

							<form name="formenq" id="formenq" method="post"
								class="form-horizontal">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								
								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Add <%=mybean.ReturnPreOwnedName(request)%></div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.
												</font>
											</center>
											</br>
                                              <div class="form-element6">
													<label >
														Branch<font color=red>*</font>:
													</label>
														<select name="dr_branch_id" id="dr_branch_id"
															class="dropdown form-control"
															onchange="submit()">
															<%=mybean.PopulateBranches(mybean.preowned_branch_id, mybean.comp_id)%>
														</select>
													
												</div>
											<div class="form-element6">
												<label > Customer: </label>
													<input name="txt_customer_name" type="text"
														class="form-control" id="txt_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255" /> (Name as on Invoice)
											</div>

                                      <%if(mybean.preowned_contact_id.equals("0")){ %>
                                      
                                      
                                      <div class="form-element12">	
											 <div class="form-element2 ">
											<label> Contact <font color=red>*</font>: </label>
															<select name="dr_title" class="form-control"
																id="dr_title">
																	<%=mybean.PopulateTitle()%>
															</select>Title<font color=red>*</font>:
											</div>

											<div class='form-element5   form-element-margin'>

															<input name="txt_contact_fname" type="text"
																class="form-control" id="txt_contact_fname"
																value="<%=mybean.contact_fname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
																<span>First Name</span>
											</div>
											

											<div class='form-element5  form-element-margin'>

															<input name="txt_contact_lname" type="text"
																class="form-control" id="txt_contact_lname"
																value="<%=mybean.contact_lname%>" size="30"
																maxlength="255" onkeyup="ShowNameHint()" />
																<span>Last Name</span>
                                      		</div>
                                      </div>
										
											<div class="form-element6">
												<label >Job Title:</label>
												
													<input name="txt_contact_jobtitle" type="text"
														class="form-control" id="txt_contact_jobtitle"
														value="<%=mybean.contact_jobtitle%>" size="32"
														maxlength="255" />
												
											</div>
											<div class="form-element6">
												<label > Email 1:</label>
												
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
														onKeyUp="showHint('../preowned/preowned-check.jsp?contact_email=' + GetReplace(this.value),'showcontacts');"
														value="<%=mybean.contact_email1%>" size="32"
														maxlength="100" />
												
											</div>
											<div class="row"></div>
											<div class="form-element3">
												<label > Mobile 1<font
													color="#ff0000">*</font>:
												</label>
												
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														onKeyUp="showHint('../preowned/preowned-check.jsp?preowned_branch_id='+<%=mybean.preowned_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');toPhone('txt_contact_mobile1','Mobile1');"
														onChange="showHint('../preowned/preowned-check.jsp?preowned_branch_id='+<%=mybean.preowned_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontacts');toPhone('txt_contact_mobile1','Mobile1');"  
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13" /> (91-9999999999)
														<span id="showcontacts"></span>
												
											</div>

											<div class="form-element3">
												<label > Mobile 2:</label>
												
													<input name="txt_contact_mobile2" type="text"
														class="form-control" id="txt_contact_mobile2"
														onKeyUp="showHint('../preowned/preowned-check.jsp?contact_mobile=' +GetReplace(this.value),'showcontacts');toPhone('txt_contact_mobile2','Contact Mobile2');"
														onChange="showHint('../preowned/preowned-check.jsp?contact_mobile=' +GetReplace(this.value),'showcontacts');toPhone('txt_contact_mobile2','Contact Mobile2');"
														value="<%=mybean.contact_mobile2%>" size="32"
														maxlength="13" /> (91-9999999999)
												
											</div>
												<div class="form-element3">
											<label >Phone<font
													color="#ff0000">*</font>:
												</label>
												
													<input name="txt_contact_phone1" type="text"
														class="form-control" id="txt_contact_phone1"
														value="<%=mybean.contact_phone1%>" size="32"
														maxlength="14"
														onKeyUp="toPhone('txt_contact_phone1','Phone 1')" />
													(91-80-33333333)
											
											</div>
											<div class="form-element3">
												<label > Email 2:</label>
												
													<input name="txt_contact_email2" type="text"
														class="form-control" id="txt_contact_email2"
														onKeyUp="showHint('../preowned/preowned-check.jsp?contact_email=' + GetReplace(this.value),'showcontacts');"
														value="<%=mybean.contact_email2%>" size="32"
														maxlength="100" />
												
											</div>
											<div class="row"></div>
										<div class="form-element6">
												<label > Address: </label>
													<textarea name="txt_contact_address" cols="40" rows="4"
														class="form-control" id="txt_contact_address"
														onKeyUp="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
													 <span id="span_txt_contact_address"> (255
														Characters)</span>
													 <span id="showcontacts"></span>
											</div>	
											<div class="form-element6">
													<label >City<font
													color="#ff0000">*</font>:
												</label>
													<%-- <%
														  if (mybean.emp_role_id.equals("1")) {
													%> --%>
													<select class="form-control select2" id="maincity"
														name="maincity">
														<%=mybean.citycheck.PopulateCities(mybean.contact_city_id, mybean.comp_id)%>
													</select>
													<br>
												<label >Pin/Zip<font
													color="#ff0000">*</font>:
												</label>
												
													<input name="txt_contact_pin" type="text"
														class="form-control" id="txt_contact_pin"
														onKeyUp="toInteger('txt_contact_pin','Pin')"
														value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
											</div>
<!-- 											<div class="row"></div> -->
										
                                         <%}%>
											<%
												if (!mybean.preowned_contact_id.equals("0")) {
											%>
											<div class="form-element6">
												<label >Customer:</label>
												
													<%=mybean.customer_info%>

												
											</div>

											<div class="form-element6">
												<label >Contact:</label>
													<%=mybean.contact_info%>
											</div>
											<%}%>

										</div>
									</div>
								</div>


								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.ReturnPreOwnedName(request)%>
											Details</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											
											
											<div class="form-element6">
												<label >Date<font
													color="#ff0000">*</font>:
												</label>
												
												<%if (mybean.emp_preowned_edit.equals("1")) { %>
													<input name="txt_preowned_date"
														id="txt_preowned_date"
														class="form-control datepicker"
														 type="text"
														value="<%=mybean.preowned_date%>" size="12"
														maxlength="10" />
														<% } else { %>
													<%=mybean.preowned_date%>
													<% } %>
												
											</div>

											<div class="form-element6">
												<label >Closing Date<font
													color="#ff0000">*</font>:
												</label>
											
													<input name="txt_preowned_close_date"
														id="txt_preowned_close_date"
														class="form-control datepicker"
													 type="text"
														value="<%=mybean.preowned_close_date%>" size="12"
														maxlength="10" />
												
											</div>

											<div class="form-element6 form-element">
											<div class="form-element12">
												<label >Model<font
													color="#ff0000">*</font>:
												</label>
											
													<!-- 													<input tabindex="-1" class="bigdrop select2-offscreen" -->
													<!-- 														id="modelvariant" name="modelvariant" style="width: 300px" -->
													<%-- 														value="<%=mybean.preowned_variant_id%>" type="hidden" /> --%>

													<select class="form-control select2" id="preownedvariant" name="preownedvariant" >
														<%=mybean.modelcheck.PopulateVariant(mybean.preowned_variant_id)%>
													</select>
												


											</div>

											<div class="form-element12">
												<label >Sub Variant:</label>
													<input name="txt_preowned_sub_variant" type="text"
														class="form-control" id="txt_preowned_sub_variant"
														value="<%=mybean.preowned_sub_variant%>" size="42"
														maxlength="255" />
											</div>
											</div>
											
											<div class="form-element6">
												<label >Description:</label>
											
													<textarea name="txt_preowned_desc" cols="40" rows="4"
														class="form-control" id="txt_preowned_desc"
														onKeyUp="charcount('txt_preowned_desc', 'span_txt_preowned_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.preowned_desc%></textarea>
													<span id="span_txt_preowned_desc"> (8000 Characters)</span>

												
											</div>
											
										<div class="row"></div>
											<div class="form-element6">
												<label >Fuel Type<font
													color="#ff0000">*</font>:
												</label>
											
													<select name="dr_preowned_fueltype_id"
														id="dr_preowned_fueltype_id" class="form-control">
														<%=mybean.PopulateFuel()%>
													</select>

												
											</div>

											<div class="form-element6">
												<label >Exterior: </label>
												
													<select name="dr_preowned_extcolour_id"
														id="dr_preowned_extcolour_id" class="form-control">
														<%=mybean.PopulateExterior()%>
													</select>

												
											</div>
											
											
											<div class="form-element6 form-element">
											<div class="form-element12">
												<label >Interior:</label>
												

													<select name="dr_preowned_intcolour_id"
														id="dr_preowned_intcolour_id" class="form-control">
														<%=mybean.PopulateInterior()%>
													</select>

												
											</div>
											

											<div class="form-element12">
												<label >Manufactured Year<font
													color="#ff0000">*</font>:
												</label>
												
													<input name="txt_preowned_manufyear" type="text"
														class="form-control yearpicker" id="txt_preowned_manufyear"
														onkeyup="toInteger('txt_preowned_manufyear')"
														value="<%=mybean.preowned_manufyear%>" size="5"
														maxlength="4" />

												
											</div>
											</div>
											<div class="form-element6">
												<label >Options: </label>
													<textarea name="txt_preowned_options" cols="40" rows="4"
														class="form-control" id="txt_preowned_options"><%=mybean.preowned_options%></textarea>

												
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label >Registerd Year: </label>
											
													<input name="txt_preowned_regdyear" type="text"
														class="form-control yearpicker" id="txt_preowned_regdyear"
														value="<%=mybean.preowned_regdyear%>" size="5"
														maxlength=4">

												
											</div>

											<div class="form-element6">
												<label >Registration No.:
												</label>
												
													<input name="txt_preowned_regno" type="text"
														class="form-control" id="txt_preowned_regno"
														value="<%=mybean.preowned_regno%>" size="15"
														maxlength="20" />
												
											</div>

											<div class="form-element6">
												<label >Kms: </label>
												
													<input name="txt_preowned_kms" type="text"
														class="form-control" id="txt_preowned_kms"
														value="<%=mybean.preowned_kms%>" size="15" maxlength="10" />

												
											</div>

											<div class="form-element6">
												<label >Foreclosure
													Amount:</label>
												
													<input name="txt_preowned_fcamt" type="text"
														class="form-control" id="txt_preowned_fcamt"
														value="<%=mybean.preowned_fcamt%>" size="15"
														maxlength="10"
														onKeyUp="toInteger('txt_preowned_fcamt','Famt')" />

												
											</div>

											<div class="form-element6">
												<label >NOC: </label>
												
													<select name="dr_preowned_noc" id="dr_preowned_noc"
														class="form-control" />
													<%=mybean.PopulateNoc()%>
													</select>
												
											</div>

											<div class="form-element6">
												<label >Funding Bank:</label>
												
													<input name="txt_preowned_funding_bank" type="text"
														class="form-control" id="txt_preowned_funding_bank"
														value="<%=mybean.preowned_funding_bank%>" size="42"
														maxlength="255" />


												
											</div>
											<div class="row"></div>

											<div class="form-element6">
												<label >Loan No.: </label>
												
													<input name="txt_preowned_loan_no" type="text"
														class="form-control" id="txt_preowned_loan_no"
														value="<%=mybean.preowned_loan_no%>" size="15"
														maxlength="20" />

												
											</div>

											<div class="form-element6">
												<label >Insurance Date:
												</label>
												
													<input name="txt_preowned_insur_date" type="text"
														class="form-control datepicker"
														id="txt_preowned_insur_date"
														value="<%=mybean.preowned_insur_date%>" size="12"
														maxlength="10">

												
											</div>

											<div class="form-element6">
												<label >Insurance
													Type:</label>
												
													<select name="dr_preowned_insurance_id"
														id="dr_preowned_insurance_id" class="form-control" />
													<%=mybean.PopulateInsuranceType()%>
													</select>

												
											</div>

											<div class="form-element6">
												<label >Ownership:
												</label>
											
													<select name="dr_preowned_ownership_id"
														id="dr_preowned_ownership_id" class="form-control">
														<%=mybean.PopulateOwnership()%>
													</select>

												
											</div>

											<div class="form-element6">
												<label >Invoice Value:
												</label>
												
													<input name="txt_preowned_invoicevalue" type="text"
														class="form-control" id="txt_preowned_invoicevalue"
														onkeyup="toInteger('txt_preowned_invoicevalue')"
														value="<%=mybean.preowned_invoicevalue%>" size="15"
														maxlength="10" />

												
											</div>

											<div class="form-element6">
												<label > Expected
													Price: </label>
												
													<input name="txt_preowned_expectedprice" type="text"
														class="form-control" id="txt_preowned_expectedprice"
														onkeyup="toInteger('txt_preowned_expectedprice')"
														value="<%=mybean.preowned_expectedprice%>" size="15"
														maxlength="10" "SecurityCheck('txt_preowned_expectedprice',this,'hint_txt_preowned_expectedprice')"  />

												
											</div>

											<div class="form-element6">
												<label >Quoted Price:
												</label>
												
													<input name="txt_preowned_quotedprice" type="text"
														class="form-control" id="txt_preowned_quotedprice"
														onkeyup="toInteger('txt_preowned_quotedprice')"
														value="<%=mybean.preowned_quotedprice%>" size="15"
														maxlength="10" />

											</div>

										</div>
									</div>
								</div>


								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none"><%=mybean.ReturnPreOwnedName(request)%>
											Status</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

												<div class="form-element6">
													<label >
														Team<font color=red>*</font>:
													</label>
													
<!-- 														<select name="dr_enquiry_team" id="dr_enquiry_team" -->
<!-- 															class="dropdown form-control" -->
<!-- 															onchange="PopulateExecutive();"> -->
															<%=mybean.PopulateTeam(mybean.preowned_branch_id, mybean.preownedteam_id)%>
<!-- 														</select> -->
													
												</div>
												
											<div class="form-element6">
													<label >
														Pre-Owned Consultant<font color="#ff0000">*</font>:
													</label>
													<span  id="teamexe">
<!-- 														<span > -->
																<%=mybean.PopulatePreownedExecutives(mybean.preowned_branch_id, mybean.preownedteam_id, mybean.preowned_emp_id, mybean.comp_id, request)%> 
<!-- 														</span> -->
													</span>
													
												</div>
											
											
											
											<div class="form-element6">
												<label >New Car Sales
													Consultant: </label>
												
													 <select
														name="dr_preowned_sales_emp_id" class="form-control"
														id="dr_preowned_sales_emp_id">
															<%=mybean.PopulateSalesExecutives(mybean.comp_id)%>
													</select>
												
											</div>
<!-- 											<div class="row"></div> -->
											<div class="form-element3 form-element-margin">
												<label >Status<font
													color="#ff0000">*</font>:
												</label>
													<%=mybean.preownedstatus_name%>
											</div>
											<%
												if (mybean.config_preowned_soe.equals("1")) {
											%>
											<div class="form-element6">
												<label >Source of
													Enquiry<font color="#ff0000">*</font>:
												</label>
												
													<select name="dr_preowned_soe_id" id="dr_preowned_soe_id"
														class="form-control">
														<%=mybean.PopulateSoe()%>
													</select>
													<%
														if (mybean.emp_role_id.equals("1")) {
													%>
													<div class="admin-master">
														<a href="../portal/managesoe.jsp?all=yes"
															title="Manage Source of Enquiry"></a>
													</div>
													<%
														}
													%>

												
											</div>

											<%
												}
											%>

											<%
												if (mybean.config_preowned_campaign.equals("1")) {
											%>
											<div class="form-element6">
												<label >Campaign<font
													color="#ff0000">*</font>:
												</label>
												
													<span id="campaign"> <select
														name="dr_preowned_campaign_id"
														id="dr_preowned_campaign_id" class="form-control">
															<%=mybean.PopulateCampaign()%>
													</select>
													</span>
													<div class="admin-master">
														<a href="../preowned/campaign-list.jsp?all=yes"
															title="Manage Campaign"></a>
													</div>
												
											</div>
											<%
												}
											%>

											<%
												if (mybean.config_preowned_refno.equals("1")) {
											%>
											<div class="form-element6">
												<label > Ref. No.:</label>
												
													<input name="txt_preowned_refno" type="text"
														class="form-control" id="txt_preowned_refno"
														value="<%=mybean.preowned_refno%>" size="32"
														maxlength="50" />

												
											</div>

											<%
												}
											%>
											
											<div class="form-element3 form-element-margin">
												<label >Add &
													Continue: </label>
												
													<input id="chk_add_continue" type="checkbox"
														name="chk_add_continue"
														<%=mybean.PopulateCheck(mybean.addcontinue)%> />

												
											</div>
											
										<div class="form-element12">		
											<center>
												<input name="addbutton" type="submit"
													class="button btn btn-success" id="addbutton"
													value="Add <%=mybean.ReturnPreOwnedName(request)%>"/> 
													<input type="hidden" name="add_button" id="add_button" value="" />
													<input class="form-control" type="hidden"
														name="txt_branch_id" id="txt_branch_id"
														value="<%=mybean.preowned_branch_id%>" /> 
											</center>

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
	<!-- END CONTAINER -->
	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp" %>


	  <script>
$(document).ready(function() {
	$("#addbutton").click(function() {
		document.getElementById('add_button').value = "yes";
		document.getElementById('formenq').submit();
	});
});

</script>	

<script>

	
	$('#txt_preowned_regdyear').datepicker({
	   showButtonPanel: true,
     changeMonth: true,
     changeYear: true,
     dateFormat: 'MM yy',
     onClose: function() {
        var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
        var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
        $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
     },

     beforeShow: function() {
       if ((selDate = $(this).val()).length > 0) 
       {
          iYear = selDate.substring(selDate.length - 4, selDate.length);
          iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), 
                   $(this).datepicker('option', 'monthNames'));
          $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
          $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
       }
	 }
    }).focus(function(){
	   $(".ui-datepicker-calendar").hide();
   });   
    
  });
</script>
<script language="JavaScript" type="text/javascript">
	
	function FormSubmit()
        {
            document.formenq.submit();
        }
//         function FormFocus() { 
// 			document.formenq.modelvariant.focus();    			
//         }
       
        function PopulateExecutive(){
						
			var preowned_branch_id = document.getElementById('txt_branch_id').value;
			var team_id = document.getElementById('dr_preowned_team').value;
// 			alert("team_id=="+team_id);
// 			alert("preowned_branch_id=="+preowned_branch_id);
			showHint('../preowned/preowned-check.jsp?preexecutive=yes&team_id=' + team_id+'&preowned_branch_id='+preowned_branch_id,'teamexe');
		  }	
		  	
		 /*  function populateItem()
		  { 
			  var preowned_model_id = document.getElementById('dr_preowned_preownedmodel_id').value;
			 //alert("preowned_model_id"+preowned_model_id);
			  showHint('../preowned/preowned-check.jsp?variant=yes&preowned_model_id='+preowned_model_id,'modelvariant');			  
		  } */
		  
    </script>

</body>

</HTML>
