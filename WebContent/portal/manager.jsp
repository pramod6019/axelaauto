<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Manager" scope="request" />
<%mybean.doPost(request, response);%>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>


	<style>
 		a{ 
 			font-size: 16px; 
 		} 
		.breadfont{
			font-size: 14px;
		}
	</style>
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Business Manager</h1>
					</div>
					<!-- END PAGE TITLE -->

				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<!-- BEGIN PAGE BREADCRUMBS -->
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="home.jsp" class="breadfont">Home</a> &gt;</li>
							<li><a href="manager.jsp" class="breadfont">Business Manager</a><b>:</b></li>
						</ul>
					
						<div class="container-fluid portlet box">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Business Manager</div>
							</div>
							<div class="portlet-body">
								<div class="tab-pane" id="">
									<table class="table table-bordered">
										<tr>
											<td><b><a
													href="../portal/executive-list.jsp?all=yes">Manage
														Executives</a></b></td>
										</tr>
										<tr>
											<td><a href="../portal/manage-configure.jsp"><b>Configure
														Axela</b></a><a href="../portal/manage-configure.jsp"></a></td>
										</tr>
										<tr>
											<td><a href="managebrandconfig-list.jsp"><b>
												Brand Config</b></a><a href="../portal/manage-configure.jsp"></a></td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					
				
				<%-- 					<% --%>
				<!-- // 						//if (mybean.comp_module_activity.equals("1")) { -->
				<%-- 					%> --%>
				<div class="container-fluid">
					<h4>
						<b><center>Manage Masters</center></b>
					</h4>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Activity</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a
											href="../portal/configure-activity.jsp" id="activity"><b><center>Configure
														Activity</center></b></a></td>
									</tr>
									<tr>
										<td><ul>
												<li><a
													href="../portal/manageactivitystatus.jsp?all=yes">Activity
														Status</a></li>
												<li><a href="../portal/manageactivitytype.jsp?all=yes">Activity
														Type</a></li>
											</ul></td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<%-- 								<%//}%> --%>

								</table>
							</div>
						</div>
					</div>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Customer</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a
											href="../customer/configure-customer.jsp" id="customer"><b><center>Configure
														Customer</center></b></a></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="../portal/managesoe.jsp?all=yes">Source
														of Enquiry</a></li>
												<li><a href="managesob.jsp?all=yes">Source of
														Business</a></li>
												<li><a href="../customer/customer-group-list.jsp?all=yes">Groups</a></li>
											</ul></td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>

								</table>
							</div>
						</div>
					</div>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Sales</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a
											href="../sales/configure-sales.jsp?all=yes" id="sales"><b><center>Configure
														Sales</center></b></a></td>
									</tr>
									<tr>
										<td><b>Leads</b></td>
										<td><b>Enquiry</b></td>
										<td><b>Sales Order</b></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="managesoe.jsp?all=yes">Source of
														Enquiry</a></li>
												<li><a href="managesob.jsp?all=yes">Source of
														Business</a></li>
											</ul></td>
										<td><ul>
												<li><a
													href="../sales/managefinancecompany-list.jsp?all=yes">Finance
														Company</a></li>
												<li><a
													href="../sales/manageenquirypriority.jsp?all=yes">Priority</a></li>
												<li><a href="../sales/managecampaigntype.jsp?all=yes">Campaign
														Type</a></li>
												<li><a href="../sales/managebrochure.jsp">Brochure</a></li>
												<%
													if (mybean.AppRun().equals("0")) {
												%>
												<li><a href="../sales/managestage.jsp?all=yes">Stage</a></li>
												<%
													}
												%>
												<li><a href="../sales/managecorporate-list.jsp?all=yes">Corporate</a></li>
												<li><a href="../sales/managefollowupdesc.jsp?all=yes">Follow-up
														Description</a></li>
												<li><a href="../sales/crmdays.jsp">CRM Days</a></li>
												<!--<li><a href="../sales/managecrmfollowupdays.jsp">CRM Follow-up Days</a></li>-->
												<li><a href="../sales/managetradeinmake.jsp?all=yes">Make</a></li>
												<li><a href="../sales/managecrmconcern.jsp?all=yes">CRM Concern</a></li>
												<li><a href="../sales/managetradeinmodel.jsp?all=yes">Model</a></li>
												<li><a href="../sales/managetradeincolour.jsp?all=yes">Colour</a></li>
												<li><a href="../sales/managelostcase1.jsp?all=yes">Lost
														Case 1</a></li>
												<li><a href="../sales/managelostcase2.jsp?all=yes">Lost
														Case 2</a></li>
												<li><a href="../sales/managelostcase3.jsp?all=yes">Lost
														Case 3</a></li>
												<li><a href="../sales/lost-case-move.jsp?all=yes">Move
														Lost Case</a></li>
												<li><a href="../sales/executives-move.jsp?all=yes">Move
														Executives</a></li>
											</ul></td>
										<td><ul>
												<li><a href="../sales/managedelivered.jsp?all=yes">Delivery
														Status</a></li>
												<li><a href="../sales/wf-docs-list.jsp?all=yes">Work
														Flow Documents</a></li>
												<!-- <li><a href="../sales/managepsfdays.jsp?all=yes">PSF Days</a></li>-->
											</ul></td>
									</tr>
									<tr>
										<td><b>Test Drives</b></td>
										<td><b>Others</b></td>
										<td><b>Incentive</b></td>
									</tr>
									<tr>
										<td><ul>
												<li><a
													href="../sales/managetestdrivedriver.jsp?all=yes">Test
														Drive Driver</a></li>
												<li><a
													href="../sales/managetestdrivelocation.jsp?all=yes">Test
														Drive Location</a></li>
												<li><a
													href="../sales/managetestdrivevehicle.jsp?all=yes">Test
														Drive Vehicle</a></li>
												<li><a
													href="../sales/managetestdrivecolour.jsp?all=yes">Test
														Drive Colour</a></li>
												<li><a href="../sales/testdriveoutage-list.jsp?all=yes">Test
														Drive Outage</a></li>
											</ul></td>
										<td><ul>
												<li><a href="../sales/enquiry-generate.jsp">Generate
														Website Enquiry Form</a></li>
														<li><a href="../sales/manageenquirytaglist.jsp">Enquiry Tags</a></li>
														<li><a href="../sales/managetargettransfer.jsp">Target Transfer</a></li>
											</ul></td>
										<td>



<ul>
<li class="inline-menu"><a href="../sales/incentive-by-target.jsp?all=yes">Incentive By Target</a></li>
<li class="inline-menu"><a href="../sales/incentive-calculate.jsp">Incentive Calculate</a></li>
<li class="inline-menu"><a href="../sales/managestockincentivetargettransfer.jsp">Incentive Target Transfer</a></li>
</ul>



</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Pre-Owned Car</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a
											href="../preowned/configure-preowned.jsp" id="preowned"><b><center>Configure
														Pre-Owned</center></b></a></td>
									</tr>
									<tr>
										<td><strong>Pre-Owned</strong></td>
										<td><b>Stock</b></td>
										<td><b>Evaluation</b></td>
									</tr>
									<tr>
										<td><ul>
										<li><a
													href="../preowned/managepreownedmanufacturer.jsp?all=yes">Pre-Owned Manufacturer</a></li>
												<li><a
													href="../preowned/managepreownedmodel.jsp?all=yes">Pre-Owned Model</a></li>
												<li><a
													href="../preowned/managepreownedvariant.jsp?all=yes">Pre-Owned Variant</a></li>
												<li><a
													href="../preowned/managepreownedvariant-servicecode.jsp?all=yes">Pre-Owned Variant Service Code</a></li>
												<li><a
													href="../preowned/managepreownedpriority.jsp?all=yes">Pre-Owned Priority</a></li>
												<li><a
													href="../preowned/managepreownedlocation.jsp?all=yes">Pre-Owned Location</a></li>
												<li><a
													href="../preowned/managepreownedcrmfollowupdays.jsp">Pre-Owned CRM Follow-up Days</a></li>
												<li><a
													href="../preowned/managepreownedlostcase1.jsp?all=yes">Pre-Owned Lost Case1</a></li>
												<li><a
													href="../preowned/managepreownedlostcase2.jsp?all=yes">Pre-Owned Lost Case2</a></li>
												<li><a
													href="../preowned/managepreownedlostcase3.jsp?all=yes">Pre-Owned Lost Case3</a></li>
											</ul></td>
										<td><ul>
												<li><a href="../preowned/manageextcolour.jsp?all=yes">Exterior
														Colour</a></li>
												<li><a
													href="../preowned/manageinteriorcolour.jsp?all=yes">Interior
														Colour</a></li>
											</ul></td>
										<td><ul>
												<li><a href="../preowned/manageevaldetails.jsp?all=yes">Evaluation
														Details</a></li>
												<li><a href="../preowned/manageevalsubhead.jsp?all=yes">Evaluation
														Sub-Head</a></li>
												<li><a href="../preowned/manageevalhead.jsp?all=yes">Evaluation
														Head</a></li>
											</ul></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<%-- 								<%if(mybeanheader.autoservice==1){%> --%>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Service</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a
											href="../service/configure-service.jsp?all=yes" id="service"><b><center>Configure
														Service</center></b></a></td>
									</tr>
									<tr>
										<td><b>Ticket</b></td>
										<td><b>Contract</b></td>
										<td><b>Job Card</b></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="../service/manageticketdept.jsp?all=yes">Department</a></li>
												<li><a href="../service/manageticketcat.jsp?all=yes">Category</a></li>
												<li><a href="../service/managetickettype.jsp?all=yes">Type</a></li>
												<li><a href="../service/manageticketstatus.jsp?all=yes">Status</a></li>
											</ul></td>
										<td><ul>
												<li><a
													href="../service/managecontractservice.jsp?all=yes">Service
														Type</a></li>
												<li><a href="../service/managecontracttype.jsp?all=yes">Contract
														Type</a></li>
											</ul></td>
										<td><ul>
												<li><a
													href="../service/managejobcardpriority.jsp?all=yes">Job
														Card Priority</a></li>
												<li><a href="../service/managejobcardcat.jsp?all=yes">Category</a></li>
												<li><a href="../service/managejobcardtype.jsp?all=yes">Type</a></li>
												<li><a href="../service/managejobcardbay.jsp?all=yes">Bay</a></li>
												<li><a href="../service/managejobcardstage.jsp?all=yes">Stage</a></li>
												<li><a
													href="../service/manage-jobcard-inventory-list.jsp?all=yes">Job
														Card Inventory</a></li>
												<li><a href="../service/manage-vehsource.jsp?all=yes">Vehicle
														Source</a></li>
												<li><a href="../service/psfdays.jsp?all=yes">PSF
														Days</a></li>
												<li><a href="../service/managejcpsfconcern.jsp?all=yes">JC PSF
														Concern</a></li>
											</ul></td>
									</tr>
									<tr>
										<td><b>Call</b></td>
										<td><b>Courtesy</b></td>
										<td><b>Others</b></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="../service/managecalltype.jsp?all=yes">Call
														Type</a></li>
												<li><a href="../service/managecallpriority.jsp?all=yes">Call
														Priority</a></li>
											</ul></td>
										<td><ul>
												<li><a
													href="../service/managecourtesyvehicle.jsp?all=yes">Courtesy
														Vehicle</a></li>
												<li><a
													href="../service/managecourtesyvehicleoutage.jsp?all=yes">Courtesy
														Vehicle Outage</a></li>
												<li><a href="../service/managelocation.jsp?all=yes">Vehicle
														Location</a></li>
											</ul></td>
											<td><ul>
												<li><a href="../service/manage-veh-kms.jsp?all=yes">Update
														Vehicle Kms</a></li>
												<li><a
													href="../service/managevehiclefollowuplostcase1.jsp?all=yes">Vehicle
														Followup Lostcase</a></li>
												<li><a
													href="../service/managecompetitors-list.jsp?all=yes">Competitors</a></li>
													
												<li><a
													href="../service/report-veh-duplicate-status.jsp?all=yes">Duplicate Vehicle Status</a></li>
											</ul></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<%-- 								<%}%> --%>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Insurance</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<!-- <td colspan="3"><a
											href="../inventory/configure-inventory.jsp" id="inventory"><b><center>Configure
														Inventory</center></b></a></td> -->
									</tr>
									<tr>
										<td><ul>
												<li><a href="../insurance/manageinsurpolicy.jsp?all=yes">Insurance
														Policy</a></li>
												<li><a href="../insurance/manageinsurcomp.jsp?all=yes">Insurance
														Company</a></li>
												<li><a
													href="../insurance/manageinsurfollowuppriority.jsp?all=yes">Insurance
														Follow-up Priority</a></li>
												<li><a
													href="../insurance/manageinsurancetype.jsp?all=yes">Insurance
														Type</a></li>
												<li><a href="../insurance/manageinsursource.jsp?all=yes">Insurance
														Source</a></li>
												<li><a
													href="../insurance/manageinsurancelostcase1.jsp?all=yes">Insurance
														Lost Case</a></li>
												<li><a
													href="../insurance/insur-reminder-list.jsp?all=yes">Insurance Reminder</a></li>
											</ul></td>
											
											<td><ul>
<!-- 											<li><a href="../insurance/manage-enquirydisposition.jsp?all=yes">Insurance -->
<!-- 													Enquiry	Disposition</a></li> -->
												<li><a href="../insurance/manage-disposition.jsp?all=yes">Disposition Configurator
														</a></li>
														<li><a href="../insurance/manageinsurancegift.jsp?all=yes">Insurance Gift
														</a></li>
											</ul></td>
											
									</tr>
								</table>
							</div>
						</div>
					</div>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Inventory</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a
											href="../inventory/configure-inventory.jsp" id="inventory"><b><center>Configure
														Inventory</center></b></a></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="../inventory/item-model.jsp?all=yes">Item
														Model</a></li>
												<li><a
													href="../inventory/inventory-group-list.jsp?all=yes">Item
														Group</a></li>
												<li><a
													href="../inventory/managestockoption.jsp?all=yes">Stock
														Option</a></li>
														<li><a
													href="../inventory/managestockoptiontype.jsp?all=yes">Stock
														Option Type</a></li>
											</ul></td>
										<td><ul>
												<li><a href="../inventory/manage-current-stock.jsp">Current
														Stock Status</a></li>
												<li><a
													href="../inventory/manage-stock-location.jsp?all=yes">Stock
														Location</a></li>
												<li><a
													href="../inventory/manage-stockdriver.jsp?all=yes">Stock
														Driver</a></li>
												<li><a
													href="../inventory/manage-stockvariant.jsp?all=yes">Stock
														Variant</a></li>
											</ul></td>
										<td><ul>
												<li><a href="../inventory/manage-uom-list.jsp?all=yes">Unit
														Of Measurement</a></li>
											</ul></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
<%-- 					 <%if (mybean.comp_module_accounting.equals("1")) {%> --%>

					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Accounting</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a href="../accounting/configure-accounting.jsp"
											id="accounting"><b><center>Configure Accounting</center>
											</b></a></td>
									</tr>
              
              
						              <tr>
						                <td valign="top">&nbsp;</td>
						                <td valign="top" border=1><b>Others</b></td>
						                <td valign="top"><b>Tax</b></td>
						              </tr>
						              <tr>
						                <td valign="top" border=1><ul>
						                  </ul></td>
						                <td valign="top" border=1><ul>
						<!--                 	 <li><a href="../accounting/managegroup.jsp?all=yes">Groups</a></li> -->
						<!--                     <li><a href="../accounting/managesubgroup.jsp?all=yes">Sub Groups</a></li> -->
						                    <!--<li><a href="../accounting/managevouchertype.jsp?all=yes">Voucher Type</a></li> -->
						                    <li><a href="../accounting/managevouchertype.jsp?all=yes">Voucher Type</a></li>
						                  </ul></td>
						                <td valign="top" border=1><ul>
						                <li><a href="../accounting/managetax.jsp?all=yes">List Taxes</a></li>
						<!--                 <li><a href="../accounting/manageprincipalsupport.jsp?all=yes">Principal Support</a></li> -->
						                  </ul></td>   
						              </tr>
						              </table>
									</div>
								</div>
							</div>
						<%--               <%}%> --%>

					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Invoice</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td colspan="3"><a href="../inventory/configure-inventory.jsp"
											id="inventory"><b><center>Configure Invoice</center>
											</b></a></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="../accounting/managetax.jsp?all=yes">Tax</a></li>
											</ul></td>
										<td>&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Branch</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">

									<tr>
										<td><ul>
												<li><a href="../portal/branch-list.jsp?all=yes">List
														Branches</a></li>
												<li><a href="../portal/region-list.jsp?all=yes">List
														Regions</a></li>
												<li><a href="../portal/managebranchtype.jsp?all=yes">Branch
														Type</a></li>
												<li><a href="../portal/managerateclass.jsp?all=yes">Rate
														Class</a></li>
												<li><a href="../portal/zone-list.jsp?all=yes">List Zones
														</a></li>
											</ul></td>

										<td>&nbsp;</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<%-- 								<%//if(Integer.parseInt(mybean.comp_franchisee_count)>1){%> --%>
					
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Franchisee</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td><ul>
												<li><a href="franchisee-list.jsp?all=yes">List
														Franchisee</a></li>
												<li><a href="managefranchtype.jsp?all=yes">Franchisee
														Type</a></li>
											</ul></td>
										<ul>
											<td>&nbsp;</td>
										</ul>
										<td>&nbsp;</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<%-- 								<%//}%> --%>
					
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Others</div>
						</div>
						<div class="portlet-body">
							<div class="tab-pane" id="">
								<table class="table table-bordered">
									<tr>
										<td><b>Region</b></td>
										<td><b>Others</b></td>
										<td><b>Executive</b></td>
									</tr>
									<tr>
										<td><ul>
												<li><a href="managecity.jsp?all=yes">City</a></li>
												<li><a href="managestate.jsp?all=yes">State</a></li>
											</ul></td>
										<td><ul>
												<li><a href="managejobtitle.jsp?all=yes">Job Title</a></li>
												<li><a href="managedepartment.jsp?all=yes">Department</a></li>
												<li><a href="company-logo.jsp">Company Logo</a></li>
											</ul></td>
										<td><ul>
										
												<li><a href="executive-univ-check.jsp">Executive
														Universal Check</a></li>
														<li><a href="ecover-executives.jsp">Ecover Executives</a></li>
												<li><a href="executives-exeaccess.jsp">Executive
														ExeAccess</a></li>
												<li><a href="executives-access-log1.jsp">Access
														Log1</a></li>
												<li><a href="executives-access-log2.jsp">Access
														Log2</a></li>
												<li><a href="executives-user-active.jsp">Active
														Users</a></li>
												<li><a href="executives-user-app-active.jsp">Active
														App Users</a></li>
												<li><a href="executives-user-access.jsp">User
														Access</a></li>
												<%-- 											<%if(mybean.AppRun().equals("0")) {%> --%>
												<li><a href="manageaccess.jsp?all=yes">Module
														Access</a></li>
												<li><a href="managemodulereport.jsp">Module Report</a></li>
												<%-- 											<%}%> --%>
												<li><a href="executive-photo-check.jsp">Executive Photo Check
														</a></li>
														
											</ul>
											
											</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div></div>
	</div>
</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
</body>
</html>

