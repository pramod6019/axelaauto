<jsp:useBean id="mybean" class="axela.sales.Enquiry_Quickadd" scope="request" />
<% mybean.doPost(request, response); %>

	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<!-- END PAGE HEAD-->
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
										<div class="caption" style="float: none">Customer Details</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->

											<center>
												<font size="1">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br></br>
												</font>
											</center>
										

											<input id="emprows" name="contact_id" type="hidden" id="contact_id"
																value="<%=mybean.enquiry_contact_id%>" />
											
											<div class="form-element6">
												<label> Branch<font color="red">*</font>: </label>
												<select name="dr_branch_id" id="dr_branch_id" onchange="branchUpdate('enquiry');"
													class="dropdown form-control">
													<%=mybean.PopulateBranches(mybean.enquiry_branch_id, mybean.comp_id)%>
												</select>
											</div>
											
											<% if (mybean.enquiry_contact_id.equals("0")) { %>
											
											<div class="form-element6">
												<label>Customer:&nbsp;</label>
												<input class="form-control" name="txt_customer_name" type="text"
													id="txt_customer_name" value="<%=mybean.customer_name%>"
													size="50" maxlength="255" />
												<span>(Name as on Invoice)</span>
											</div>
											
											<div class="row">
												<div class="form-element2 ">
													<label>Contact<font color="red">*</font>:&nbsp; </label>
													<select name="dr_title" class="form-control" id="dr_title">
															<%=mybean.PopulateTitle(mybean.contact_title_id,mybean.comp_id)%>
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
														size="30" maxlength="255" onkeyup="ShowNameHint();" /> 
													<span>Last Name</span>
												</div>
											</div>

											<div class="form-element6">
												<label>Job Title:</label>
												<input class="form-control" name="txt_contact_jobtitle" type="text"
													id="txt_contact_jobtitle" value="<%=mybean.contact_jobtitle%>"
													size="32" maxlength="255"/>
											</div>
											
											<div class="form-element6">
												<label >Email 1:</label>
												<input name="txt_contact_email1" type="text" class="form-control"
													id="txt_contact_email1" size="32" maxlength="100"
													onkeyup="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_email=' + GetReplace(this.value),'showcontactsemail');"
													value="<%=mybean.contact_email1%>"/>
													<div id="showcontactsemail" class="hint" ></div>
											</div>
											
											<div class="row">
											<div class="form-element3">
												<label>Mobile 1<font color="#ff0000">*</font>: </label>
												<input name="txt_contact_mobile1" type="text" class="form-control"
													id="txt_contact_mobile1" size="32" maxlength="13" 
													onkeyup="toPhone('txt_contact_mobile1','Contact Mobile1');
													showHint('../sales/enquiry-check.jsp?enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontactsmobil1');" 
													value="<%=mybean.contact_mobile1%>" />(91-9999999999)
												<span id="showcontactsmobil1" class="hint" ></span>
											</div>
											
 											<div class="form-element3"> 
 												<label>Mobile 2:</label> 
 												<input name="txt_contact_mobile2" type="text" class="form-control" 
 													id="txt_contact_mobile2" size="32" maxlength="13"  
 													onkeyup="toPhone('txt_contact_mobile2','Contact Mobile2'); 
 													showHint('../sales/enquiry-check.jsp?enquiry_branch_id='+<%=mybean.enquiry_branch_id%>+'&contact_mobile=' + GetReplace(this.value),'showcontactsmobil2');" 
													value="<%=mybean.contact_mobile2%>"/>(91-9999999999) 
													<span id="showcontactsmobil2" class="hint" ></span> 
											</div>
											

											<div class="form-element3">
												<label>Phone 1: </label>
												<input name="txt_contact_phone1" type="text" class="form-control" id="txt_contact_phone1"
													onkeyup="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_phone=' + GetReplace(this.value),'showcontactsphone1'); toPhone('txt_contact_phone1','Contact Phone1');"
													value="<%=mybean.contact_phone1%>" size="32" maxlength="15" />(91-80-33333333)
													<span id="showcontactsphone1" class="hint" ></span>
											</div>
											
											<div class="form-element3"> 
												<label>Phone 2:</label>
												<input name="txt_contact_phone2" type="text"
													class="form-control" id="txt_contact_phone2"
													onkeyup="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_phone=' + GetReplace(this.value),'showcontactsphone2'); toPhone('txt_contact_phone2','Contact Phone2');"
													value="<%=mybean.contact_phone2%>" size="32"
													maxlength="15" />(91-80-33333333)
											<span id="showcontactsphone2" class="hint" ></span>
											</div>
											
 											</div>
<!-- 											<div class="form-element6"> -->
<!--  												<label>Email 2:</label>  -->
<!-- 												<input name="txt_contact_email2" type="text" class="form-control" -->
<!-- 													id="txt_contact_email2" size="32" maxlength="100" -->
<%-- 													onkeyup="showHint('../sales/enquiry-check.jsp?contact_id='+<%=mybean.contact_id%>+'&contact_email=' + GetReplace(this.value),'showcontacts');"  --%>
<%--  													value="<%=mybean.contact_email2%>"/>  --%>
<!-- 											</div> -->


											<div class="form-element6">
												<label>Address:</label>
													<textarea name="txt_contact_address" cols="40" rows="4"
														class="form-control" id="txt_contact_address"
														onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.contact_address%></textarea>
													<span id="span_txt_contact_address"> (255 Characters)</span>
											</div>

											<div class="form-element6">
												<label>City<font color="#ff0000">*</font>: </label>
												<select class="form-control select2" id="maincity" name="maincity">
													<%=mybean.citycheck.PopulateCities(mybean.contact_city_id, mybean.comp_id)%>
												</select>
											</div>

											<div class="form-element6">
												<label>Pin/Zip:</label>
												<input name="txt_contact_pin" type="text"
													class="form-control" id="txt_contact_pin"
													onkeyup="toInteger('txt_contact_pin','Pin')"
													value="<%=mybean.contact_pin%>" size="10" maxlength="6" />
											</div>
											
											<% } %>

											<% if (!mybean.enquiry_contact_id.equals("0")) { %>
											
											<div class="form-element3">
												<label>Customer:&nbsp;</label>
												<div id="emprows">
													<%=mybean.customer_info%>
												</div>
											</div>

											<div class="form-element3">
												<label>Contact:&nbsp;</label>
												<div id="emprows">
													<%=mybean.contact_info%>
												</div>
											</div>

											<% } %>

										</div>
									</div>
								</div>



								<div class="portlet box">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Enquiry Details
										</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

											<div class="form-element6">
												<label> Date<font color="#ff0000">*</font>: </label>
												<% if (mybean.emp_enquiry_edit.equals("1")) { %>
													
													<input name="txt_enquiry_date" id="txt_enquiry_date" type="text"
														value="<%=mybean.enquiry_date%>" class="form-control datepicker" />
														
												<% } else { %>
													
													<%=mybean.enquiry_date%>
													
												<% } %>
											</div>

											<div class="form-element6">
												<label> Closing Date<font color="#ff0000">*</font>: </label>
												<input name="txt_enquiry_close_date" id="txt_enquiry_close_date"
													value="<%=mybean.enquiry_close_date%>" class="form-control datepicker" type="text" />
											</div>

											<div class="form-element6">
											
												<% if (mybean.branchtype_id.equals("1")) { %>
												<div class="form-element12 form-element">
													<label> Model<font color="#ff0000">*</font>: </label>
													<select name="dr_enquiry_model_id" id="dr_enquiry_model_id"
														class="dropdown form-control" onchange="populateItem();">
														<%=mybean.PopulateModel(mybean.enquiry_model_id,mybean.comp_id)%>
													</select>
												</div>
	
	
												<div class="form-element12 form-element">
													<label>Variant<font color="#ff0000">*</font>: </label>
													<!--  <select name="dr_enquiry_item_id" id="dr_enquiry_item_id" class="form-control"> -->
													<span id="modelitem">
														<%=mybean.PopulateItem(mybean.enquiry_model_id, mybean.comp_id)%>
													</span>
													<!--------- </select> -->
												</div>
												
												<% } %>
	
												<% if (!mybean.branch_brand_id.equals("1")) { %>
												
												<div class="form-element12 form-element">
													<label>Type of Buyer<font color="#ff0000">*</font>: </label>
													<select name="dr_enquiry_buyertype_id" id="dr_enquiry_buyertype_id" class="form-control">
														<%=mybean.PopulateBuyerType(mybean.comp_id)%>
													</select>
												</div>
												
												<% } %>
												
											</div>		
																								
											<div class="form-element6">
												<label> Description: </label>
												<textarea name="txt_enquiry_desc" cols="40" rows="4"
													class="form-control" id="txt_enquiry_desc"
													onkeyup="charcount('txt_enquiry_desc', 'span_txt_enquiry_desc','<font color=red>({CHAR} characters left)</font>', '8000')"><%=mybean.enquiry_desc%></textarea>
												<span id="span_txt_enquiry_desc"> (8000 Characters)</span>
											</div>
											
										</div>
										<% if (mybean.branch_brand_id.equals("55")) { %>
										<div class="form-element6">
											<label>Category <font color="#ff0000">*</font> : </label>
											<select name="dr_enquiry_enquirycat_id" id="dr_enquiry_enquirycat_id" class="form-control">
													<%=mybean.PopulateCategory(mybean.comp_id)%>
											</select>
										</div>
										<% } %>
									</div>
								</div>



								<% if (mybean.branchtype_id.equals("2")) { %>
									<div class="portlet box  " style="position: relative">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">Pre Owned</div>
										</div>
										<div class="portlet-body portlet-empty container-fluid">
											<div class="tab-pane" id="">
												<!-- 											START PORTLET BODY -->
												<!-- <div class="row"> -->
												<div class="row">
													<div class="form-element6">
														<label> Pre Owned Model<font color="#ff0000">*</font>: </label>
														<select class="form-control select2" id="preownedvariant" name="preownedvariant">
															<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_preownedvariant_id)%>
														</select>
													</div>
	
													<div class="form-element6" id="fueltype">
														<label> Fuel Type: </label>
														<select name="dr_enquiry_fueltype_id" id="dr_enquiry_fueltype_id" class="form-control">
															<%=mybean.PopulateFuelType(mybean.comp_id)%>
														</select>
													</div>
												</div>
												
												<div class="row">
													<div class="form-element6" id="fueltype">
														<label> Pref. Reg.: </label>
														<select name="dr_enquiry_prefreg_id" id="dr_enquiry_prefreg_id" class="form-control">
															<%=mybean.PopulatePrefReg(mybean.comp_id)%>
														</select>
													</div>
	
													<div class="form-element6" id="presentcar">
														<label> Present Car: </label>
														<input name="txt_enquiry_presentcar" type="text" class="form-control"
															id="txt_enquiry_presentcar" size="32" maxlength="255"
															value="<%=mybean.enquiry_presentcar%>"/>
													</div>
												</div>
												
												<div class="row">
													<div class="form-element6" id="presentcar">
														<label> Finance: </label>
														<select name="dr_enquiry_finance" id="dr_enquiry_finance" class="form-control">
															<%=mybean.PopulateFinance(mybean.comp_id)%>
														</select> 
													</div>
													
													<div class="form-element6" id="budget">
														<label> Budget: </label>
														<input name="txt_enquiry_budget" type="text" size="14"
															maxlength="10" class="form-control"
															id="txt_enquiry_budget" value="<%=mybean.enquiry_budget%>"/>
													</div>
												</div>
											</div>
										</div>
									</div>
								<% } %>


								<!-- enquiry status -->
								<div class="portlet box  " style="position: relative">
									<div class="portlet-title" style="text-align: center;">
										<div class="caption" style="float: none">Enquiry Status </div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->

												<div class="form-element6">
													<label> Team<font color="red">*</font>: </label>
<!-- 												<select name="dr_enquiry_team" id="dr_enquiry_team" -->
<!-- 													class="dropdown form-control" -->
<!-- 													onchange="PopulateExecutive();"> -->
														<%=mybean.PopulateTeam(mybean.enquiry_branch_id, mybean.enquiry_team_id, mybean.comp_id)%>
<!-- 												</select> -->
												</div>
												<div class="form-element6">
													<label> Sales Consultant<font color="#ff0000">*</font>: </label>
													<span id="teamexe">
<!-- 													<select name="dr_enquiry_emp_id" id="dr_enquiry_emp_id" -->
<!-- 														class="dropdown form-control"> -->
															<%=mybean.PopulateSalesExecutives(mybean.enquiry_branch_id, mybean.enquiry_team_id, mybean.enquiry_emp_id, "1", mybean.comp_id, request)%>
<!-- 													</select>  -->
													</span>
												</div>

												<div class="row">
													<% if (mybean.config_sales_soe.equals("1")) { %>
													
													<div class="form-element6">
														<label>Source of Enquiry<font color="#ff0000">*</font>: </label>
														<div id="soe">
															<%=mybean.PopulateSoe(mybean.comp_id,mybean.emp_id)%>
														</div>
													</div>
													
													<% } %>
	
													<% if (mybean.config_sales_sob.equals("1")) { %>
	
													<div class="form-element6">
														<label> Source of Business<font color="#ff0000">*</font>: </label>
															<div id="dr_enquiry_sob_id">
																<%=mybean.PopulateSOB()%>
															</div>
													</div>
													
													<% } %>
													<% if (mybean.config_sales_campaign.equals("1")) { %>
	
													<div class="form-element6">
														<label> Campaign<font color="#ff0000">*</font>: </label>
														<span id="campaign">
															<select name="dr_enquiry_campaign_id" id="dr_enquiry_campaign_id"
																class="dropdown form-control">
																	<%=mybean.PopulateCampaign(mybean.comp_id)%>
															</select>
														</span>
														<!-- <div class="col-md-2" style="top: 8px"> -->
														<div class="admin-master">
															<a href="../sales/campaign-list.jsp?all=yes" title="Manage Campaign"></a>
														</div>
														<!-- </div> -->
													</div>
													<% } %>
													
	<%-- 												<% --%>
	<!-- 													if (!mybean.team_preownedbranch_id.equals("0")) { -->
	<%-- 												%> --%>
												<% if(!mybean.comp_id.equals("1009")){ %>
													<div class="form-element6">
														<label> Trade-In Model: </label>
														<select class="form-control select2" id="enquiry_tradein_preownedvariant_id"
																name="enquiry_tradein_preownedvariant_id">
																<!--  onchange="SecurityCheck('enquiry_tradein_preownedvariant_id',this,'hint_preownedvariant')" --> 
															<%=mybean.variantcheck.PopulateVariant(mybean.enquiry_tradein_preownedvariant_id)%>
														</select>
													</div>
													
												<% } %>
	<%-- 												<% --%>
	<!--  													} -->
	<%-- 												%> --%>
												</div>
												<div class="form-element12">
													<center>
														
														<!-- <button type="button" class="btn1" name="addbutton" id="addbutton" >Add Enquiry</button> -->
														<input type="hidden" name="add_button" id="add_button" value="yes" />
														<input type="hidden" name="inbound" id="inbound" value="yes" />
														<input type="hidden" id="enquiry_call_id" name="enquiry_call_id" value="<%=mybean.enquiry_call_id%>"/>
														<input type="hidden" id="branchtype_id" value="<%=mybean.branchtype_id%>" />
														<input class="form-control" type="hidden" name="txt_branch_id" id="txt_branch_id" value="<%=mybean.enquiry_branch_id%>" />
														<input name="addbutton" type="button" onclick="submitB('enquiryadd', 'addenquiry');"
														class="button btn btn-success" id="addbutton" value="Add Enquiry" />
													</center>
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
<script src="../assets/js/components-select2.min.js" type="text/javascript"></script>
