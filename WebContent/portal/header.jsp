<jsp:useBean id="mybeanheader" class="axela.portal.Header" scope="request" />
<%-- <jsp:useBean id="mybean1" class="axela.portal.Executive_Photo_Upload" scope="request" /> --%>
<meta content="width=device-width, initial-scale=1" name="viewport" />
<% mybeanheader.doPost(request, response); %>

<!-- BEGIN HEADER  -->
<div class="page-header">
	<!-- BEGIN HEADER TOP --->
	<div class="page-header-top">
		<div class="container-fluid">
			<!-- BEGIN LOGO -->
			<div class="col-md-3 col-sm-3 col-xs-6"
				>
				<%
					if (!mybeanheader.comp_logo.equals("0")) {
				%>
				<div>
					<%-- 					<%=mybeanheader.comp_logo%> --%>
					<img id="com-logo" class="img img-responsive" src="<%=mybeanheader.comp_logo%>" style="max-height: 66px; max-width: auto; margin: 5px 0px 4px 5px;">
				</div>
				<%
					}
				%>
			</div>
			
			<div class="col-md-2 col-sm-1"></div>

			<div class="col-md-2 col-sm-4 col-xs-6">
					<img src="../admin-ifx/axelaauto-logo.png" class="img img-responsive" alt="Axela Auto" style="max-height: 75px; max-width: auto;"/>
			</div>

			<!-- END LOGO -->
			
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="hor-menu top-menu mobile-align col-md-3 col-sm-3 col-xs-5"
				id="menuhover">

				<ul class="nav navbar-nav pull-right">

					<li class="dropdown dropdown-user">
						<div class="dropdown">
							<a class="dropbtn" style="color: black; font-size: 12px;">
								<div class="container-fluid col-sm-12 col-xs-12" id="move-top">
									<!-- 		<div class="col-sm-4">&nbsp;</div>  -->
									<div class="admin-icon col-md-6 col-sm-6 col-xs-8"
										style="white-space: nowrap;">
										<%
											if (!mybeanheader.GetSession("emp_id", request).equals("")) {
												out.print("&nbsp;Welcome,<br> " + mybeanheader.emp_name + "");
											}
										%>
									</div>
									<div class="col-md-1 col-xs-1"></div>
									<div class="col-md-3 col-sm-3 col-xs-4"
										style="left: -5px; text-align: right;top:-2px;">
     
										<span id="edit_image"> <a href="#basic"
											data-target="#basic" data-toggle="modal"> <%if (mybeanheader.emp_photo.equals("")) 
											{%> <img class="img-circle extra-large "
												src="../admin-ifx/admin.png" width="60px" height="60px"> <%
													} else {
												%> <img class="img-circle extra-large"
												src="../Thumbnail.do?empphoto=<%=mybeanheader.emp_photo%>&width=60"
												alt="" width="60px" height="60px"> <%
 	}
 %>
										</a>
     
											<div class="modal" id="basic">
												<div class="modal-dialog" style="width: 95%;">
													<div class="modal-content">

														<div style="text-align: right">
															<button type="button" class="btn btn-default"
																data-dismiss="modal">
																<b>&times; </b>Close
															</button>
														</div>

														<div class="modal-body" style="padding: 0px">
															<form EncType="multipart/form-data" name="frmupload"
																method="post">

																<div class="portlet box">
																	<div class="portlet-title" style="text-align: center">
																		<div class="caption" style="float: none">
																			Profile Picture</div>
																	</div>
																	<div class="portlet-body portlet-empty"
																		style="padding: 0px">
																		<div class="tab-pane" id="">
																			<!-- START PORTLET BODY -->
																			<center>

																				<%
																					if (mybeanheader.emp_photo.equals("")) {
																				%>

																				<img  src="../admin-ifx/admin.png" style="max-width: 100%;">
																				<%
																					} else {
																				%>
																				<img src="../Thumbnail.do?empphoto=<%=mybeanheader.emp_photo%>"
																					alt="" style="max-width: 100%; height: auto">
																				<%
																					}
																				%>
																			</center>

																			<br> <br>
																			<div class="container-fluid">
																				<div class="col-md-12 col-sm-12 col-xs-12">
																					<center>

																						<input NAME="filename" Type="file" class="button"
																							size="30">

																					</center>
																				</div>
																				<br> <br>
																			</div>



																		</div>
																	</div>
																</div>
																<br> <br> <br>


															</form>
														</div>
													</div>
												</div>
											</div>




										</span> <span class="username"> </span>


									</div>
								</div>
							</a>


							<ul
								class="dropdown-content dropdown-menu dropdown-menu-default pull-right"
								style="top: 40px;">
								<li><a
									href="../portal/executives-photo.jsp?emp_id=<%=mybeanheader.emp_id%>">
										<b>Upload Photo</b>
								</a></li>
								<li><a href="../portal/news-list.jsp?all=yes"> <b>News</b></a></li>
								<li><a href="../portal/system-password.jsp"><b>Manage
											Password</b></a></li>
								<li><a href="../portal/system-theme.jsp"><b>Manage
											Theme</b></a></li>
								<li><a href="../portal/system-user.jsp"><b>User
											Configuration</b> </a></li>
								<li><a href="../portal/helpdesk.jsp" target="_blank"><b>Emax
											HelpDesk</b></a></li>
								<%
									if (mybeanheader.AppRun().equals("0")) {
								%>
								<li><a href="../portal/remotesupport.jsp"> <b>Remote
											Support</b></a></li>
								<%
									}
								%>

								<li><a href="../portal/signout.jsp"><b>Signout</b></a></li>
							</ul>
						</div>
						
					</li>
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
			<div id="togler1"
					class="menu-toggler col-sm-1 col-xs-1">
					</div>
		</div>
	</div>
	<!-- END HEADER TOP -->
	<!-- BEGIN HEADER MENU -->
	<!-- 	<div class="page-header-menu" style="position:relative"> -->

	<div class="page-header-menu">
		<div class="container-fluid" id="ios-min-height">
			<div class="hor-menu hor-menu-light">
				<ul class="nav navbar-nav">

					<li class="menu-dropdown classic-menu-dropdown"><a> <b>Home</b>
					</a> <!-- 					<span><a href="../portal/search1.jsp" --> <!-- 										data-target="#Hintclicktocall" data-toggle="modal">  -->
						<!-- 										22s<b>Search --> <!-- 										<i class="fa fa-search" style="font-size: 20px; vertical-align: middle; float: right; padding: 4px;"></i> -->

						<!-- 												<span class="arrow"></span> --> <!-- 										</b> -->
						<!-- 									</a></span> -->



						<ul class="dropdown-menu pull-left">
							<li class="inline-menu"><a href="../portal/home.jsp"><b>Home</b></a></li>
							<li class="inline-menu"><a><b>Welcome to Axela Auto!</b></a></li>

							<li class=""><a><p>
										Hi and welcome here! This is a showcase of the endless
										possibilities in <b>Axela Auto</b>.
									</p></a></li>
						</ul> <span id="mob_universal_search"> <a
							style="float: right; margin-top: -35px; margin-right: 20px; font-size: 18px"
							href="../portal/search.jsp" data-target="#Hintclicktocall"
							data-toggle="modal"> Search <i class="fa fa-search"
								style="font-size: 20px; vertical-align: middle; float: right; padding: 4px;"></i>
						</a>
					</span></li>

					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Activities</b>
							<span class="arrow"></span></a>
						<ul class="dropdown-menu pull-left">
							<li class="inline-menu"><a href="../portal/activity.jsp"><b>Activities</b></a></li>
							<li class="inline-menu"><a
								href="../portal/activity-update.jsp?add=yes">Add Activity</a></li>
							<li class="inline-menu"><a
								href="../portal/activity-list.jsp?all=yes">List Activities</a></li>

						</ul></li>
					<li class="menu-dropdown mega-menu-dropdown"><a><b>Customers</b>
							<span class="arrow"></span> </a>
						<ul class="dropdown-menu" style="min-width: 300px">
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../customer/index.jsp"><b>Customers
															Report</b></a></li>
												<li class="inline-menu"><a
													href="../customer/customer.jsp"><b>Customers Today</b></a></li>
												<li class="inline-menu"><a
													href="../customer/customer-update.jsp?Add=yes">Add
														Customer</a></li>
												<li class="inline-menu"><a
													href="../customer/customer-list.jsp?all=yes">List Customers</a></li>
													<li class="inline-menu"><a
													href="../customer/suppliers-list.jsp?all=yes&tag=vendors">List Suppliers</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../customer/customer-contact.jsp"><b>Contacts</b></a></li>
												<li class="inline-menu"><a
													href="../customer/customer-contact-update.jsp?Add=yes">Add
														Contacts</a></li>
												<li class="inline-menu"><a
													href="../customer/customer-contact-list.jsp?all=yes">List
														Contacts</a></li>
												<br>
											</ul>
										</div>
									</div>
								</div>
							</li>
						</ul></li>

				<!-- 	<li class="menu-dropdown mega-menu-dropdown"><a><b>Marketing</b>
							<span class="arrow"></span> </a>
						<ul class="dropdown-menu" style="min-width: 300px">
							<li style="text-align: center;"><a href="../mktg/index.jsp"><b>Marketing</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">

												<li class="inline-menu"><a href="../mktg/lead.jsp"><b>Leads</b></a></li>
												<li class="inline-menu"><a
													href="../mktg/lead-update.jsp?add=yes">Add Lead</a></li>
												<li class="inline-menu"><a
													href="../mktg/lead-list.jsp?all=yes">List Leads</a></li>
												<li class="inline-menu"><a
													href="../mktg/lead-import.jsp?all=yes">Import Leads</a></li>
												<br>
											</ul>
										</div>

										<div class="col-md-6">
											<ul class="mega-menu-submenu">

												<li class="inline-menu"><a href="../mktg/campaign.jsp"><b>Campaign</b></a></li>
												<li class="inline-menu"><a
													href="../mktg/campaign-update.jsp?add=yes">Add Campaign</a></li>
												<li class="inline-menu"><a
													href="../mktg/campaign-list.jsp?all=yes">List Campaign</a></li>
												<br>
											</ul>
										</div>

									</div>
								</div>
							</li>
						</ul></li> -->
					<%
						if (mybeanheader.autosales == 1) {
					%>
					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Sales</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu" style="min-width: 500px">
							<li style="text-align: center;"><a href="../sales/index.jsp"><b>Sales Dashboard</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../sales/enquiry.jsp"><b>Enquiry</b></a></li>
												<li class="inline-menu"><a
													href="../sales/enquiry-quickadd.jsp">Add Enquiry</a></li>
												<li class="inline-menu"><a
													href="../sales/enquiry-list.jsp?all=yes">List Enquiry</a></li>
												<li class="inline-menu"><a
													href="../sales/enquiry-user-import.jsp">Import Enquiry</a></li>
												<li class="inline-menu"><a
													href="../service/contact.jsp">Search Contacts</a></li>
												<li class="inline-menu"><a href="../service/branch.jsp">Search
														Employees</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../sales/testdrive.jsp"><b>Test Drives</b></a></li>
												<li class="inline-menu"><a
													href="../sales/testdrive-list.jsp?all=yes">List Test
														Drives</a></li>
												<li class="inline-menu"><a
													href="../sales/testdrive-cal.jsp">Test Drive Calendar</a></li>
												<!-- <li class="inline-menu"><a
													href="../sales/testdriveoutage-list.jsp?all=yes">Test
														Drive Outage</a></li> -->
														<li class="inline-menu"><a
													href="../sales/managetestdrivevehicle.jsp?all=yes">Test
														Drive Vehicle</a></li>
												<li class="inline-menu"><a
													href="../sales/testdrive-gatepass-list.jsp?all=yes">Gate Pass List</a></li>
												
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../sales/veh-quote.jsp"><b>Quotes</b></a></li>
												<li class="inline-menu"><a
													href="../sales/veh-quote-list.jsp?all=yes">List Quotes</a></li>
												<li class="inline-menu"><a
													href="../sales/report-stock-exe.jsp">Executive Stock
														Status</a></li>
												<li class="inline-menu"><a href="../preowned/preowned-stock-status.jsp"><%=mybeanheader.ReturnPreOwnedName(request)%>
														Stock Status</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../sales/veh-salesorder.jsp"><b>Sales Orders</b></a></li>
												<li class="inline-menu"><a
													href="../sales/veh-salesorder-list.jsp?all=yes">List
														Sales Orders</a></li>
												<br>
											</ul>
										</div>

									</div>
									<div class="row">
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../sales/campaign.jsp"><b>Campaign</b></a>
												</li>
												<li class="inline-menu"><a
													href="../sales/campaign-update.jsp?add=yes">Add
														Campaign</a></li>
												<li class="inline-menu"><a
													href="../sales/campaign-list.jsp?all=yes">List Campaign</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../sales/target.jsp"><b>Target</b></a></li>
												<li class="inline-menu"><a href="../sales/target-list.jsp">List Executive Targets</a></li>
												<li class="inline-menu"><a href="../sales/target-branch-list.jsp">List Branch Targets</a></li>
														<li><a href="../sales/list-discount.jsp?all=yes">List
														Discount</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../sales/team.jsp"><b>Teams</b></a></li>
												<li class="inline-menu"><a
													href="../sales/team-update.jsp?add=yes">Add Team</a></li>
												<li class="inline-menu"><a
													href="../sales/team-list.jsp?all=yes">List Teams</a></li>
													<li class="inline-menu"><a href="../portal/leave-list.jsp?all=yes">List Leave</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Incentive</b></a></li>
												<li class="inline-menu"><a href="../sales/incentive-by-variant.jsp?all=yes">Incentive By Variant</a></li>
												<br>
											</ul>
										</div>
									</div>
								</div>
							</li>
						</ul></li>
					<%
						}
					%>
					<%
						if (mybeanheader.comp_module_preowned.equals("1")) {
					%>
					<!-- Begin User Car -->
					<li class="menu-dropdown mega-menu-dropdown"><a> <b><%=mybeanheader.ReturnPreOwnedName(request)%></b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu" style="min-width: 500px">
							<li style="text-align: center;">
								<a href="../preowned/index.jsp"><b><%=mybeanheader.ReturnPreOwnedName(request)%> Dashboard</b></a>
							</li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../preowned/preowned.jsp"><b><%=mybeanheader.ReturnPreOwnedName(request)%>
															Today</b></a></li>
												<li class="inline-menu"><a
													href="../preowned/preowned-quickadd.jsp">Add <%=mybeanheader.ReturnPreOwnedName(request)%></a></li>
												<li class="inline-menu"><a
													href="../preowned/preowned-list.jsp?all=yes">List <%=mybeanheader.ReturnPreOwnedName(request)%></a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../preowned/preowned-eval.jsp"><b>Evaluation</b></a></li>
												<li class="inline-menu"><a href="../preowned/preowned-eval-list.jsp?all=yes">List Evaluation</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../preowned/preowned-stock.jsp"><b><%=mybeanheader.ReturnPreOwnedName(request)%> Stock</b></a></li>
												<li class="inline-menu"><a href="../preowned/preowned-stock-list.jsp?all=yes"><%=mybeanheader.ReturnPreOwnedName(request)%> List Stock</a></li>
												<li class="inline-menu"><a href="../preowned/preowned-stock-status.jsp"><%=mybeanheader.ReturnPreOwnedName(request)%> Stock Status</a></li>
												<br>
											</ul>
										</div>

									</div>
									<div class="row">
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../preowned/preowned-testdrive.jsp"><b>Test Drives</b></a></li>
												<li class="inline-menu"><a
													href="../preowned/preowned-testdrive-list.jsp?all=yes">List Test Drives</a></li>
												<li class="inline-menu"><a
													href="../preowned/preowned-testdrive-cal.jsp">Test Drive Calendar</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../preowned/preowned-team.jsp"><b>Teams</b></a></li>
												<li class="inline-menu"><a href="../preowned/preowned-team-update.jsp?add=yes">Add Team</a></li>
												<li class="inline-menu"><a href="../preowned/preowned-team-list.jsp?all=yes">List Teams</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../preowned/target.jsp"><b>Target</b></a></li>
												<li class="inline-menu"><a href="../preowned/target-list.jsp">List Executive Targets</a></li>
												<br>
											</ul>
										</div>

									</div>
								</div>
							</li>
						</ul></li>
					<!-- End Pre Owned -->
					<%
						}
					%>

					<% if (mybeanheader.comp_module_service.equals("1")) { %>

					<%
						if (mybeanheader.autoservice == 1) {
					%>
					<!-- Begin Service Item -->
					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Service</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu" style="min-width: 500px">
							<li style="text-align: center;"><a
								href="../service/index.jsp"><b>Service Dashboard</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../service/ticket.jsp"><b>Tickets</b></a></li>
												<li class="inline-menu"><a
													href="../service/ticket-add.jsp?add=yes">Add Ticket</a></li>
												<li class="inline-menu"><a
													href="../service/ticket-list.jsp?all=yes">List Tickets</a></li>
												<li class="inline-menu"><a
													href="../service/holiday-list.jsp?all=yes">List
														Holidays</a></li>
												<li class="inline-menu"><a
													href="../service/ticket-faq-list.jsp?all=yes">List FAQ</a></li>
												<li class="inline-menu"><a
													href="../service/ticket-faq-cat-list.jsp?all=yes">List
														Categories</a></li>
												<li class="inline-menu"><a
													href="../service/contact.jsp">Search Contacts</a></li>
												<li class="inline-menu"><a href="../service/branch.jsp">Search
														Employees</a></li>
														<%if(mybeanheader.emp_id.equals("1")){ %>
														<li class="inline-menu"><a href="../inbound/index.jsp">Inbound </a></li>
														<li class="inline-menu"><a href="../inbound/call.jsp?call_callid=&call_no=">Inbound Call </a></li>
												<%} %>
												<br>
											</ul>
										</div>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../service/jobcard.jsp"><b>Job Cards</b></a></li>
												<li class="inline-menu"><a
													href="../service/jobcard-veh-search.jsp">Add Job Card</a></li>
												<li class="inline-menu"><a
													href="../service/jobcard-list.jsp?all=yes">List Job
														Cards</a></li>
												<li class="inline-menu"><a
													href="../service/manhours.jsp">Man Hours</a></li>
												<li class="inline-menu"><a
													href="../service/jobcard-user-import.jsp">Import Job
														Cards</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../service/vehicle.jsp"><b>Vehicles</b></a></li>
												<li class="inline-menu"><a
													href="../service/vehicle-update.jsp?add=yes">Add
														Vehicles</a></li>
												<li class="inline-menu"><a
													href="../service/vehicle-list.jsp?all=yes">List
														Vehicles</a></li>
												<li class="inline-menu"><a
													href="../service/vehicle-user-import.jsp">Import
														Vehicles</a></li>
												<li class="inline-menu"><a
													href="../service/vehicle-dmsstatus-user-import.jsp">Import
													Vehicle	DMS Status</a></li>
												<li class="inline-menu"><a
													href="../service/movement-add.jsp">Vehicle IN</a></li>
												<!-- <li class="inline-menu"><a
													href="../service/veh-follow-up-import-maruti.jsp"> DMS
														Import</a></li> -->
												<li class="inline-menu"><a
													href="../service/movement-list.jsp?all=yes">List
														Vehicle Movement</a></li>
												<li class="inline-menu"><a
													href="../service/booking-followup.jsp?all=yes">Import
														Follow-Up</a></li>
												<li class="inline-menu"><a href="../service/booking-enquiry.jsp">Add
													Service Booking</a></li>
												<br>
											</ul>
										</div>
									</div>
									
									<div class="row">
									<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li><a href="../service/service-target.jsp"><b>Target</b></a></li>
												<li><a href="../service/service-target-list.jsp">List
														Executive Targets</a></li>
												<li><a href="../service/service-target-user-import.jsp">Import Service Target</a></li>
											</ul>
										</div>
									
									
									
										<!-- 										<div class="col-md-3"> -->
										<!-- 											<ul class="mega-menu-submenu"> -->
										<!-- 												<li class="inline-menu"><a href="../service/call.jsp"><b>Calls</b></a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../service/call-update.jsp?add=yes">Add Call</a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../service/call-list.jsp?all=yes">List Calls</a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../service/callman.jsp">Call Manager</a></li> -->
										<!-- 												<br> -->
										<!-- 											</ul> -->
										<!-- 										</div> -->
										<%if (mybeanheader.AppRun().equals("0")) {%>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../service/booking.jsp"><b>Bookings</b></a></li>
												<li class="inline-menu"><a
													href="../service/booking-list.jsp?all=yes">List
														Bookings</a></li>
												<li class="inline-menu"><a
													href="../service/advisor-cal.jsp?all=yes">Advisor
														Calender</a></li>
												<!-- 												<li><a href="../service/leave-list.jsp?all=yes">Executive -->
												<!-- 														Leave</a></li> -->
												<li class="inline-menu"><a
													href="../service/parking-list.jsp?all=yes">List
														Parkings</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../service/pickup.jsp"><b>Pickup</b></a></li>
												<li class="inline-menu"><a
													href="../service/pickup-update.jsp?add=yes">Add Pickup</a></li>
												<li class="inline-menu"><a
													href="../service/pickup-list.jsp?all=yes">List Pickup</a></li>
												<li class="inline-menu"><a
													href="../service/pickup-cal.jsp">Pickup Calendar</a></li>
												<br>
											</ul>
										</div>
										
										</div>
										<div class="row">
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../service/courtesy.jsp"><b>Courtesy Cars</b></a></li>
												<li class="inline-menu"><a
													href="../service/courtesy-update.jsp?add=yes">Add
														Courtesy</a></li>
												<li class="inline-menu"><a
													href="../service/courtesy-list.jsp?all=yes">List
														Courtesy</a></li>
												<li class="inline-menu"><a
													href="../service/courtesy-cal.jsp">Courtesy Calendar</a></li>
												<li class="inline-menu"><a
													href="../service/managecourtesyvehicle.jsp?all=yes">Courtesy
														Vehicle</a></li>
												<li class="inline-menu"><a
													href="../service/managecourtesyvehicleoutage.jsp?all=yes">Courtesy
														Vehicle Outage</a></li>
												<br>
											</ul>
										</div>
										<%} %>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../service/coupon.jsp"><b>Coupon</b></a></li>
												<li class="inline-menu"><a
													href="../service/couponcampaign-list.jsp?add=yes">List Coupon Campaign</a></li>
												<br>
											</ul>
										</div>
										
									</div>

									<!-- <div class="row">
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../service/courtesy.jsp"><b>Campaign</b></a></li>
												<li class="inline-menu"><a
													href="../service/courtesy-update.jsp?add=yes">Add
														Campaign</a></li>
												<li class="inline-menu"><a
													href="../service/courtesy-list.jsp?all=yes">List
														Campaign</a></li>
												<br>
											</ul>
										</div>
										
									</div> -->
									<%
										}
									%>
								</div>
							</li>
						</ul></li>

					<% } %>

					<!-- End service item-->
					
					<!-- Start OF accessories-->
					
					<% if (mybeanheader.comp_module_accessories.equals("1")) { %>
					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Accessories</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu" style="min-width: 300px">
							<li style="text-align: center;"><a
								href="../accessories/index.jsp"><b>Accessories Dashboard</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
									<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?accessories=yes&add=yes&param3=5"><b>Quote</b></a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?accessories=yes&all=yes&voucherclass_id=5&vouchertype_id=5">List Quotes</a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?accessories=yes&add=yes&param3=27"><b>Pre-Order</b></a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/so-update.jsp?add=yes&voucherclass_id=27&vouchertype_id=27">Add Pre-Order</a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?all=yes&accessories=yes&voucherclass_id=27&vouchertype_id=27">List Pre-Order</a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/purchaseinvoice-user-import.jsp?branchtype=5">Import Purchase Invoices</a>
												</li>
											</ul>
										</div>
										<div class="col-md-6">
										<ul class="mega-menu-submenu">
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?accessories=yes&add=yes&amp;param3=6"><b>Sales Invoice</b></a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?accessories=yes&all=yes&amp;voucherclass_id=6&amp;vouchertype_id=6">List Sales Invoices</a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?accessories=yes&add=yes&amp;param3=23"><b>Sales Return</b></a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?accessories=yes&all=yes&voucherclass_id=23&vouchertype_id=23">List Sales Returns</a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?accessories=yes&add=yes&amp;param3=9"><b>Receipt</b></a>
												</li>
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?accessories=yes&all=yes&amp;voucherclass_id=9&amp;vouchertype_id=9">List Receipts</a>
												</li>
												<br>
												</ul>
										</div>
									</div>
								</div>
							</li>
						</ul></li>
					<%} %>
					
					<!-- End OF accessories-->
					<!-- Start OF insurance-->
					
					<% if (mybeanheader.comp_module_insurance.equals("1")) { %>
					<li class="menu-dropdown mega-menu-dropdown"><a href="../portal/ecover-signin.jsp"> <b>Insurance</b>
							<span class="arrow"></span>
					</a></li>
					<% } %>
					
					<!-- End insurance item-->
					
					<% if (mybeanheader.comp_module_inventory.equals("1")) { %>

					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Inventory</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu" style="width: 500px">
							<li style="text-align: center;"><a
								href="../inventory/index.jsp"><b>Inventory Dashboard</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../inventory/index.jsp"><b>Inventory</b></a></li>
												<li class="inline-menu"><a
													href="../inventory/inventory-item-update.jsp?add=yes">Add
														Item</a></li>
												<li class="inline-menu"><a
													href="../inventory/inventory-item-list.jsp?all=yes">List
														Items</a></li>
												<li class="inline-menu"><a
													href="../inventory/item-user-import.jsp">Import Items</a></li>
												<li class="inline-menu"><a
													href="../inventory/inventory-cat-list.jsp?all=yes">List
														Item Categories</a></li>
												<li class="inline-menu"><a
													href="../inventory/inventory-billcat-list.jsp?all=yes">List
														Bill Categories</a></li>
												<li class="inline-menu"><a
													href="../inventory/inventory-salescat-list.jsp?all=yes">List
														Sales Categories</a></li>
												<%
													if (mybeanheader.autoservice == 1) {
												%>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=1&vouchertype_id=1">
														List Stock Adjustments</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=2&vouchertype_id=2">
														List Stock Transfer</a></li>

												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/voucher-list.jsp?all=yes&vouchertype_id=3&voucherclass_id=3"> -->
												<!-- 														List Item Conversion</a></li> -->



												<!-- 												<li class="inline-menu"><a href="../inventory/inventory-adj-list.jsp"> -->
												<!-- 												Inventory Adjustments</a></li> -->

												<li class="inline-menu"><a
													href="../inventory/inventory-location-list.jsp?all=yes">List
														Locations</a></li>
												<li class="inline-menu"><a
													href="../inventory/inventory-reorderlevel.jsp?status=add">Reorder
														Level</a></li>
												<%
													}
												%>
												<br>
											</ul>
										</div>
										<%-- 										<% if (mybeanheader.autoservice == 1) { %> --%>
										<!-- 										<div class="col-md-3"> -->
										<!-- 											<ul class="mega-menu-submenu"> -->
										<!-- 												<li class="inline-menu"><a href="../inventory/inventory-po.jsp"><b>Purchase -->
										<!-- 															Orders</b></a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../inventory/inventory-po-update.jsp?add=yes">Add -->
										<!-- 														PO</a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../inventory/inventory-po-list.jsp?all=yes">List -->
										<!-- 														PO</a></li> -->
										<!-- 												<li class="inline-menu"><a href="../inventory/inventory-po-search.jsp">Search -->
										<!-- 														PO</a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../inventory/inventory-po-list.jsp?auth=yes">Authorise -->
										<!-- 														PO</a></li> -->
										<!-- 												<li class="inline-menu"><a -->
										<!-- 													href="../customer/customer-list.jsp?all=yes&amp;tag=vendors">List -->
										<!-- 														Suppliers</a></li> -->
										<!-- 												<br> -->
										<!-- 											</ul> -->
										<!-- 										</div> -->
										<%-- 										<%} %> --%>

										<%
											if (mybeanheader.autosales == 1) {
										%>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../inventory/stock.jsp"><b>Stock</b></a></li>
												<li class="inline-menu"><a
													href="../inventory/stock-update.jsp?add=yes">Add Stock</a></li>
												<li class="inline-menu"><a
													href="../inventory/stock-list.jsp?all=yes">List Stock</a></li>
												<!-- 							<li><a href="../inventory/stock-search.jsp">Search Stock</a></li> -->
												<li class="inline-menu"><a
													href="../inventory/stock-user-import.jsp">Import Stock</a></li>
												<!-- 												<li class="inline-menu"><a href="../inventory/stock-validate.jsp">Stock -->
												<!-- 														Validate</a></li> -->
												<li class="inline-menu"><a
													href="../inventory/stock-gatepass-list.jsp?all=yes">List
														Gate Passes</a></li>
												<li class="inline-menu"><a
													href="../inventory/target-ws-list.jsp">Wholesale Target</a></li>
												<li class="inline-menu"><a
													href="../inventory/orderplaced-list.jsp?all=yes">Orderplaced</a></li>
													<li class="inline-menu"><a
													href="../inventory/orderplaced-user-import.jsp">Import Orderplaced</a></li>
												
												<li class="inline-menu"><a
													href="../accounting/manageprincipalsupport.jsp?all=yes">Principal Support</a></li>
												<li class="inline-menu"><a
													href="../inventory/principalsupport-user-import.jsp">Import Principal support</a></li>
												<li class="inline-menu"><a
													href="../inventory/manage-stockvariant.jsp?all=yes">Stock
														Variant</a></li>
												<li class="inline-menu"><a
													href="../inventory/itemprice-user-import.jsp?all=yes">Import
														Variant Prices</a></li>
												<br>
											</ul>
										</div>
										<%
											}
										%>

										<%
											if (mybeanheader.autoservice == 1) {
										%>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>GRN</b></a></li>
												<!-- ../inventory/inventory-grn.jsp -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?voucherclass_id=20&vouchertype_id=20">List
														GRN</a></li>
												<br>
											</ul>
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Purchase
															Return</b></a></li>
												<!-- ../inventory/inventory-preturn.jsp -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?voucherclass_id=24&vouchertype_id=24">List
														Returns</a></li>
												<br>
											</ul>
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=25"><b>Delivery
															Note</b></a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=25&vouchertype_id=25">List
														Delivery Notes</a></li>
												<br>
											</ul>
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=23"><b>Sales
															Return</b></a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=23&vouchertype_id=23">List
														Sales Returns</a></li>
												<br>
											</ul>
										</div>


									</div>
									<!-- 									<div class="row"> -->
									<!-- 										<div class="col-md-4"> -->
									<!-- 											<ul class="mega-menu-submenu"> -->
									<!-- 												<li class="inline-menu"><a href="#"><b>Others</b></a></li> -->
									<!-- 												<li class="inline-menu"><a -->
									<!-- 													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=1&vouchertype_id=1"> -->
									<!-- 													List Stock Adjustments</a></li> -->

									<!-- 												<li class="inline-menu"><a -->
									<!-- 													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=23&vouchertype_id=23"> -->
									<!-- 													List Sales Return On Invoice</a></li> -->
									<!-- 												<li class="inline-menu"><a -->
									<!-- 													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=24&vouchertype_id=24"> -->
									<!-- 													List Purchase Return On Invoice</a></li> -->
									<!-- 												<br> -->
									<!-- 											</ul> -->
									<!-- 										</div> -->
									<!-- 									</div> -->
									<%
										}
									%>
								</div>
							</li>
						</ul></li>
					<% } %>

					<%	if (mybeanheader.comp_module_accounting.equals("1")) {%>

					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Accounting</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu" style="min-width: 500px">
							<li style="text-align: center;"><a
								href="../accounting/index.jsp"><b>Accounting Dashboard</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-5">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?param1=yes"><b>Income</b></a></li>
												<%-- 												<% --%>
												<!--  													if (mybeanheader.comp_module_invoice.equals("1")) { -->
												<%-- 												%> --%>
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=5">Quote</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/so-update.jsp?add=yes&amp;voucherclass_id=5&amp;vouchertype_id=5">Add -->
												<!-- 														Quote</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=5&amp;vouchertype_id=5">List
														Quotes</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/voucher.jsp?add=yes&amp;param3=4">Sales -->
												<!-- 														Order</a></li> -->
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/so-update.jsp?add=yes&amp;voucherclass_id=6&amp;vouchertype_id=6">Add -->
												<!-- 														Sales Invoice</a></li> -->
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=4&amp;vouchertype_id=4">List -->
												<!-- 														Sales Orders</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=6">Sales
														Invoice</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/so-update.jsp?add=yes&amp;voucherclass_id=6&amp;vouchertype_id=6">Add -->
												<!-- 														Sales Invoice</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=6&amp;vouchertype_id=6">List
														Sales Invoices</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=7">Bill</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/bill-update.jsp?add=yes&amp;voucherclass_id=7&amp;vouchertype_id=7">Add -->
												<!-- 														Bill</a></li> -->
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=7&amp;vouchertype_id=7">List Bills</a></li>
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?add=yes&amp;param3=9">Receipt</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/receipt-update.jsp?add=yes&amp;voucherclass_id=9&amp;vouchertype_id=9">Add -->
												<!-- 														Receipt</a></li> -->
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=9&amp;vouchertype_id=9">List Receipts</a></li>
												
<!-- 												<li class="inline-menu"> -->
<!-- 												<a href="../accounting/voucher.jsp?add=yes&amp;param3=28">Advance Receipt</a></li> -->
												
<!-- 												<li class="inline-menu"> -->
<!-- 												<a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=28&amp;vouchertype_id=28">List Advance Receipts</a></li> -->
<!-- 														<li class="inline-menu"><a href="../accounting/voucher.jsp?add=yes&amp;param3=28">Advance Receipt</a></li> -->
<!-- 													<li class="inline-menu"><a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=28&amp;vouchertype_id=28">List Advance Receipts</a></li> -->
<!-- 														<li class="inline-menu"><a -->
<!-- 													href="../accounting/receipt-user-import.jsp">Import Receipts</a></li> -->
												<li class="inline-menu">
												<a href="../accounting/voucher.jsp?add=yes&amp;param3=10">Credit Note</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/creditnote-update.jsp?add=yes&amp;voucherclass_id=10&amp;vouchertype_id=10">Add -->
												<!-- 														Credit Note</a></li> -->
												<li class="inline-menu">
												<a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=10&amp;vouchertype_id=10">List Credit Notes</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-4">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?param2=yes"><b>Expense</b></a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=11">Debit Note</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/debitnote-update.jsp?add=yes&amp;voucherclass_id=11&amp;vouchertype_id=11">Add -->
												<!-- 														Debit Note</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=11&amp;vouchertype_id=11">List Debit Notes</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=12">Purchase Order</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/po-update.jsp?add=yes&amp;voucherclass_id=12&amp;vouchertype_id=12">Add -->
												<!-- 														Purchase Order</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=12&amp;vouchertype_id=12">List Purchase Orders</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=15">Payment</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/payment-update.jsp?add=yes&amp;voucherclass_id=15&amp;vouchertype_id=15">Add -->
												<!-- 														Payment</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=15&amp;vouchertype_id=15">List
														Payments</a></li>
												<%-- 												<% --%>
												<!--  													} -->
												<%-- 												%> --%>
 												<li class="inline-menu"><a 
 													href="../accounting/voucher.jsp?add=yes&amp;param3=16">Expense</a></li> 
												<li class="inline-menu"><a
 													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=16&amp;vouchertype_id=16">List 
 														Expenses</a></li> 
												
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=21">Purchase
														Invoice</a></li>
												<!-- 												<li class="inline-menu"><a -->
												<!-- 													href="../accounting/po-update.jsp?add=yes&amp;voucherclass_id=21&amp;vouchertype_id=21">Add -->
												<!-- 														Purchase Invoice</a></li> -->
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=21&amp;vouchertype_id=21">List
														Purchase Invoices</a></li>
														<li class="inline-menu">
													<a href="../accounting/purchaseinvoice-user-import.jsp">Import Purchase Invoices</a>
												</li>
												<br>

											</ul>
										</div>
										<div class="col-md-3">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Others</b></a></li>
												<li class="inline-menu"> <a href="../accounting/voucher-list.jsp?all=yes">List Vouchers</a></li>
												<li class="inline-menu"> <a href="../accounting/ledger-list.jsp?all=yes">List Ledgers</a></li>
												<li class="inline-menu"> <a href="../accounting/reconcile.jsp">Reconciliation</a></li>
												<li class="inline-menu"> <a href="../accounting/group-list.jsp?all=yes">Groups</a></li>
												<li class="inline-menu"><a href="../accounting/voucher.jsp?add=yes&amp;param3=18">Journal</a></li>
												<li class="inline-menu"><a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=18&amp;vouchertype_id=18">List Journals</a></li>
												<li class="inline-menu"><a href="../accounting/voucher.jsp?add=yes&amp;param3=19">Contra</a></li>
												<li class="inline-menu"><a href="../accounting/voucher-list.jsp?all=yes&amp;voucherclass_id=19&amp;vouchertype_id=19">List Contras</a></li>
												<br>
											</ul>
										</div>
									</div>
								</div>
							</li>
						</ul></li>

					<% } %>
					<!-- end accounting item-->

					<!-- Begin Invoice Item -->
					<%
   	if (mybeanheader.comp_module_invoice.equals("1")) {
   %>

					<li class="menu-dropdown mega-menu-dropdown"><a> <b>Invoice</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu">
							<li style="text-align: center;"><a
								href="../invoice/index.jsp"><b>Invoice</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../accounting/voucher.jsp?add=yes&amp;param3=5"><b>Quotes</b></a></li>
												<li class="inline-menu"><a href="../accounting/so-update.jsp?add=yes&voucherclass_id=5&vouchertype_id=5">Add Quote</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=5&vouchertype_id=5">List
														Quotes</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../accounting/voucher.jsp?add=yes&amp;param3=6"><b>Invoices</b></a></li>
												<li class="inline-menu"><a
													href="../accounting/so-update.jsp?add=yes&voucherclass_id=6&vouchertype_id=6">Add
														Invoice</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=6&vouchertype_id=6">List
														Invoices</a></li>
												<br>
											</ul>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=9"><b>Receipts</b></a></li>
												<li class="inline-menu"><a
													href="../accounting/receipt-update.jsp?add=yes&voucherclass_id=9&vouchertype_id=9">Add
														Receipt</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=9&vouchertype_id=9">List
														Receipts</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a
													href="../accounting/voucher.jsp?add=yes&amp;param3=15"><b>Payments</b></a></li>
												<li class="inline-menu"><a
													href="../accounting/payment-update.jsp?add=yes&voucherclass_id=15&vouchertype_id=15">Add
														Payment</a></li>
												<li class="inline-menu"><a
													href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=15&vouchertype_id=15">List
														Payments</a></li>
												<br>
											</ul>
										</div>
									</div>
								</div>
							</li>
						</ul></li>
					<!-- End Invoice Items -->

					<%
   	}
   %>
					<!-- Invoice Container Ends -->

					<%
						if (mybeanheader.comp_module_app.equals("1")) {
					%>
					<li class="menu-dropdown mega-menu-dropdown"><a> <b>App</b>
							<span class="arrow"></span>
					</a>

						<ul class="dropdown-menu">
							<li style="text-align: center;"><a href="../app/home.jsp"><b>App</b></a></li>
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Test
															Drive</b></a></li>
												<li class="inline-menu"><a
													href="../app/testdrive-list.jsp?all=yes">List Drives</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Book A
															Service</b></a></li>
												<li class="inline-menu"><a
													href="../app/servicebooking-list.jsp?all=yes">List
														Services</a></li>
												<br>
											</ul>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Models</b></a></li>
												<li class="inline-menu"><a
													href="../app/model-update.jsp?add=yes">Add Model</a></li>
												<li class="inline-menu"><a
													href="../app/model-list.jsp?all=yes">List Models</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Offers</b></a></li>
												<li class="inline-menu"><a
													href="../app/model-offers-update.jsp?add=yes">Add
														Offers</a></li>
												<li class="inline-menu"><a
													href="../app/model-offers-list.jsp?all=yes">List Offers</a></li>
												<br>
											</ul>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Showrooms</b></a></li>
												<li class="inline-menu"><a
													href="../app/showroom-update.jsp?add=yes">Add Showroom</a></li>
												<li class="inline-menu"><a
													href="../app/showroom-list.jsp?all=yes">List Showrooms</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="#"><b>Service
															Centers</b></a></li>
												<li class="inline-menu"><a
													href="../app/servicecentre-update.jsp?add=yes">Add
														Service Center</a></li>
												<li class="inline-menu"><a
													href="../app/servicecentre-list.jsp?all=yes">List
														Service Centers</a></li>
												<br>
											</ul>
										</div>
									</div>
								</div>
							</li>
						</ul></li>
					<%
						}
					%>
					<%
						if (mybeanheader.emp_mis_access.equals("1")) {
					%>
					<li class="menu-dropdown classic-menu-dropdown"><a> <b>MIS</b>
							<span class="arrow"></span></a>
						<ul class="dropdown-menu">
							<li style="text-align: center;"><a href="../portal/mis.jsp"><b>MIS</b></a></li>
						</ul></li>

					<%
						}
					%>

					<li class="menu-dropdown mega-menu-dropdown"><a><b>More</b>
							<span class="arrow"></span> </a>
						<ul class="dropdown-menu" style="min-width: 300px">
							<li>
								<div class="mega-menu-content">
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../portal/news.jsp"><b>News</b></a></li>
												<li class="inline-menu"><a
													href="../portal/news-branch-list.jsp?all=yes">List
														Branch News</a></li>
												<li class="inline-menu"><a
													href="../portal/news-ho-list.jsp?all=yes">List Head
														Office News</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../portal/faq.jsp"><b>FAQ</b></a></li>
												<li class="inline-menu"><a
													href="../portal/faqexecutive-list.jsp?all=yes">List FAQ</a></li>
												<li class="inline-menu"><a
													href="../portal/faqexecutivecat-list.jsp?all=yes">List
														Categories</a></li>
												<br>
											</ul>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../portal/email.jsp"><b>Email</b></a></li>
												<li class="inline-menu"><a
													href="../portal/email-send.jsp?target=1">All Contacts</a></li>
												<li class="inline-menu"><a
													href="../portal/email-send.jsp?target=2">All Customers</a></li>
												<li class="inline-menu"><a
													href="../portal/email-send.jsp?target=3">All Suppliers</a></li>
												<li class="inline-menu"><a
													href="../portal/email-exe-send.jsp?chk_email_allexe=on">All
														Executives</a></li>
												<li class="inline-menu"><a
													href="../portal/email-list.jsp?all=yes">List Email</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../portal/sms.jsp"><b>SMS</b></a></li>
												<li class="inline-menu"><a
													href="../portal/sms-send.jsp?target=1">All Contacts</a></li>
												<li class="inline-menu"><a
													href="../portal/sms-send.jsp?target=2">All Customers</a></li>
												<li class="inline-menu"><a
													href="../portal/sms-send.jsp?target=3">All Suppliers</a></li>
												<li class="inline-menu"><a
													href="../portal/sms-exe-send.jsp?chk_sms_allexe=on">All
														Executives</a></li>
												<li class="inline-menu"><a
													href="../portal/sms-list.jsp?all=yes">List SMS</a></li>
												<br>
											</ul>
										</div>
									</div>
									<div class="row">
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li class="inline-menu"><a href="../portal/exe.jsp"><b>Executives</b></a></li>
												<li class="inline-menu"><a
													href="../portal/exe-list.jsp?all=yes">List Executives</a></li>
												<li class="inline-menu"><a
													href="../portal/exe-update.jsp?add=yes">Add Executives</a></li>
												<li><a href="../portal/notification-exe-send.jsp?all=yes">Send Notifications</a></li>
												<li><a href="../portal/notification-list.jsp">List Notifications</a></li>
												<br>
											</ul>
										</div>
										<div class="col-md-6">
											<ul class="mega-menu-submenu">
												<li><a href="../sales/managebrochure.jsp"><b>Brochure</b></a></li>
												<li><a href="../portal/branch-list.jsp?all=yes">List Branches</a></li>
												<li><a href="../portal/report-user-activity.jsp?all=yes">User Activity</a></li>
												<li><a href="../portal/report-export.jsp?all=yes">Report Export</a></li>
<!-- 												<li><a href="../portal/managecity.jsp?all=yes">City</a></li> -->
											</ul>
										</div>	
									</div>
									<div class="row">
 																			<%if (mybeanheader.exeaccess.equals("1")) { %> 
										
																			<%} %> 
									</div> 
									
								</div>
							</li>
						</ul></li>
					<%
						if (mybeanheader.emp_id.equals("1")) {
					%>
					<li class="menu-dropdown classic-menu-dropdown"><a><b>Manager</b>
							<span class="arrow"></span> </a>
						<ul class="dropdown-menu">
							<li class="inline-menu"><a href="../portal/manager.jsp"><b>Manager</b></a></li>
							<li class="inline-menu"><a href="../portal/executives.jsp"><b>Executives</b></a></li>
							<li class="inline-menu"><a
								href="../portal/executive-list.jsp?all=yes">List Executives</a></li>
							<li class="inline-menu"><a
								href="../portal/executives-update.jsp?add=yes">Add
									Executives</a></li>

						</ul></li>
					<%
						}
					%>

					<li id="universal-search" style="border-style: none"><a
						href="../portal/search.jsp" style="border-style: none"
						data-target="#Hintclicktocall" data-toggle="modal"><b><i
								class="fa fa-search"
								style="font-size: 20px; vertical-align: middle; padding: 4px;"></i>
								<span class="arrow"></span>
						</b>
					</a></li>


				</ul>
			</div>
			<!-- END MEGA MENU -->
		</div>
	</div>
	<!-- END HEADER MENU -->
</div>
<div class="modal fade" id="Hintclicktocall" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body"  id='modal-body'>
				<span> &nbsp;&nbsp;Loading... </span> <br> <br>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="Hintclicktocall80" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-80">
		<div class="modal-content">
			<div class="modal-body scroller" id='modal-body80'>
				<span> &nbsp;&nbsp;Loading... </span> <br> <br>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="Hintclicktocalldash" role="basic" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-body">
				<span> &nbsp;&nbsp;Loading... </span> <br> <br>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="Hintclicktocalldash80" role="basic" aria-hidden="true">
	<div class="modal-dialog modal-80">
		<div class="modal-content">
			<div class="modal-body">
				<span> &nbsp;&nbsp;Loading... </span> <br> <br>
			</div>
		</div>
	</div>
</div>
<input id='copy-access' name='copy-access' value='<%=mybeanheader.emp_copy_access%>' hidden /> <!-- to get copy access value for executive-->
<input id='date-theme' name='date-theme'	value='<%=mybeanheader.GetTheme(request)%>' hidden /><!-- to get theme color for date-picker -->
