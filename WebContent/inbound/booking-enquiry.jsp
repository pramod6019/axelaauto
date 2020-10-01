<jsp:useBean id="mybean" class="axela.service.Booking_Enquiry"
	scope="request" />
<%mybean.doPost(request, response);%>
<body Onload="PopulateBookingTime();" class="page-container-bg-solid page-header-menu-fixed">
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<form name="frmenquiry" id="frmenquiry" class="form-horizontal" method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%><div id="errorMsg"></div></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Customer
											Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->
											<center>
													<font>Form fields marked with a red asterisk <b><font
															color="#ff0000">*</font></b> are required. </font>
											</center>
											<div class="form-element6">
												<label> Branch<font color="#ff0000">*</font>:&nbsp; </label>
													<select class="form-control" name="dr_branch"
															id="dr_branch" onchange="PopulateCampaign(this.value);">
													<%=mybean.PopulateBranches(mybean.comp_id, mybean.booking_veh_branch_id)%>
													</select>
											</div>


											<div class="form-element6">
												<label>Customer:&nbsp;</label>
													<input class="form-control"
														name="txt_booking_customer_name" type="text"
														id="txt_booking_customer_name"
														value="<%=mybean.customer_name%>" size="50"
														maxlength="255" /> (Name as on Invoice)
											</div>
											
											<div class="row">
												<div class="form-element2 ">
													<label>Contact<font color=red>*</font>:&nbsp; </label>
													<select name="dr_title" class="form-control" id="dr_title">
														<%=mybean.PopulateTitle(mybean.contact_title_id, mybean.comp_id)%>
													</select>
													<span>Title</span>
												</div> 
												
												<div class="form-element5 form-element-margin">
													<input name="txt_contact_fname" type="text" class="form-control"
														id="txt_contact_fname" value="<%=mybean.contact_fname%>"
														size="30" maxlength="255" onkeyup="ShowNameHint();" />
													<span>First Name</span>
												</div> 
														
												<div class="form-element5 form-element-margin">
													<input name="txt_contact_lname" type="text" class="form-control"
														id="txt_contact_lname" value="<%=mybean.contact_lname%>"
														size="30" maxlength="255" /> 
													<span>Last Name</span>
												</div>
											</div>
											
											
											
											
											<div class="form-element6">
												<label>Mobile <font color="#ff0000">*</font>:&nbsp; </label>
													<input name="txt_contact_mobile1" type="text"
														class="form-control" id="txt_contact_mobile1"
														onKeyUp="showHint('report-check.jsp?contact_mobile1='+GetReplace(this.value)+'&search=yes','showcontacts');toPhone('txt_contact_mobile1','Mobile1');"
														value="<%=mybean.contact_mobile1%>" size="32"
														maxlength="13" /> (91-9999999999) <span id="showcontacts"></span>
											</div>
											
											<div class="form-element6">
												<label>Email :&nbsp;</label>
													<input name="txt_contact_email1" type="text"
														class="form-control" id="txt_contact_email1"
<%-- 														onkeyup="showHint('../service/booking-enquiry-check.jsp?contact_id='+<%=mybean.customer_id%>+'&booking_veh_branch_id='+<%=mybean.booking_veh_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts');" --%>
														onKeyUp="showHint('report-check.jsp?contact_email1='+GetReplace(this.value)+'&search=yes','search-contact');"
														value="<%=mybean.contact_email1%>" size="32"
														maxlength="100"><span id="search-contact"></span>
											</div>
										</div>
									</div>
								</div>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Vechile Details</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<%-- <%
												if (mybean.branchtype_id.equals("1")) {
											%> --%>
											<div class="form-element6 ">
														<label> Variant <font color="#ff0000">*</font>: </label>
														<select class="form-control select2" id="servicepreownedvariant" name="servicepreownedvariant">
															<%=mybean.variantcheck.PopulateVariant(mybean.booking_veh_variant_id, mybean.comp_id)%>
														</select>
											</div>
											<div class="form-element6">
												<label>Reg.No <font color="#ff0000">*</font>:&nbsp; </label>
													<input class="form-control" name="txt_booking_veh_reg_no"
														type="text" id="txt_booking_veh_reg_no"
														value="<%=mybean.booking_veh_reg_no%>" size="50"
														maxlength="20" />
											</div>
											<div class="row"></div>
											<div class="form-element6">
												<label> CRM Executive<font color="#ff0000">*</font>:&nbsp; </label>
													<span id="crmexe">
															<%=mybean.PopulateCRMExecutive(mybean.comp_id,request)%>
													</span>
											</div>
											
											<div class="form-element6">
												<label>Booking Type:&nbsp; </label>
													<select name="dr_booking_type" class="form-control" id="dr_booking_type"
														Onchange="PopulateBookingTime();" visible="true">
														<%=mybean.PopulateServiceBookingType(mybean.comp_id, mybean.booking_type)%>
													</select>
											</div>
											
										<div class="row"></div>	
											<div class="form-element6" id="followuptime">
												<label>Follow-up Time:&nbsp; </label>
													<input name="txt_nextfollowup_time" type="text" class="form-control datetimepicker"
														id="txt_nextfollowup_time" value="<%=mybean.followup_nextfollowup_time%>"
														size="20" maxlength="16"/>
											</div>
											
											<div class="form-element6" id="bookingtimeband">
												<label>Booking Time<font color="red">*</font>:&nbsp; </label>
													<input name="txt_booking_time" type="text" class="form-control datetimepicker"
														id="txt_booking_time" value="<%=mybean.booking_time%>"/>
											</div>
											
												<div class="form-element6">
													<label>Source of Enquiry <font color="#ff0000">*</font>:&nbsp; </label>
														<select name="dr_booking_soe_id" id="dr_booking_soe_id"
															class="dropdown form-control" onchange="populateSobBooking();">
															<%=mybean.PopulateSoe(mybean.comp_id)%>
														</select>
												</div>
												<div class="row"></div>
												<div class="form-element6">
													<label> Source of Business<font color="#ff0000">*</font> :&nbsp; </label>
														<span id="HintSob"> 
															<%=mybean.PopulateSOB(mybean.booking_soe_id, mybean.comp_id)%>
														</span>
												</div>
											<!-- End PickupBand  -->
											<div class="form-element6">
													<label> Campaign<font color="#ff0000">*</font>:&nbsp; </label>
														<span id="campaign">
															<%=mybean.PopulateCampaign(mybean.booking_veh_branch_id, mybean.comp_id)%>
														</span>
											</div>
											
											<div class="form-element6">
												<label> VOC:&nbsp;</label>
												<textarea name="txt_vehfollowup_voc" cols="70" rows="4"
													class="form-control" id="txt_vehfollowup_voc"><%=mybean.vehfollowup_voc%></textarea>
											</div>	
												
												<div class="row"></div>
													<center>
														<input name="add_button" type="button" class="button btn btn-success" id="add_button" value="Add Enquiry" onclick="submitB('servicebooking', 'addenquiry');"/>
													</center>
													<input type="hidden" name="inbound" id="inbound" value="yes" />
													<input type="hidden" name="vehfollowup_call_id" id="vehfollowup_call_id" value="<%=mybean.vehfollowup_call_id%>" />
													<input type="hidden" name="addbutton" id="addbutton" value="Add Enquiry" />
													
										</div>
									</div>
								</div>
								<!-- 	PORTLET end details-->
							</form>
						</div>
					</div>
					</div>
				</div>
			</div>
			<!--contain end  -->
		</div>
	<!-- END CONTAINER -->
</body>
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>