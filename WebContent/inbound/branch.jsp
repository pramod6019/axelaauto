<jsp:useBean id="mybean" class="axela.service.Branch" scope="request"/>
<%mybean.doPost(request,response); %>
<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
									Search Employees
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form id="form1" name="form1"  method="post" class="form-horizontal">
								<div class="container-fluid ">
<!-- 										<div class="form-group"> -->
	                             <div class="form-element6 form-element-center">
												<label> Search<font color=red>*</font>:&nbsp; </label>
													<input name ="txt_search" type="txt_search" class="form-control" id="txt_search" 
													value="<%=mybean.txt_search%>" size="40" maxlength="255" 
													onKeyUp="BranchCheck('txt_search',this,'hint_txt_search');"
													placeholder=" Enter your search parameter!"/>
											</div>
												<div class="form-element6 form-element-center">
													<label> Type<font color="#ff0000">*</font>:&nbsp; </label>
													<select name="dr_type_id" class="form-control"
														id="dr_type_id">
														<%=mybean.PopulateType(mybean.comp_id)%>
													</select>
													<!-- 																			<div class="hint" id="hint_dr_enquiry_lostcase1_id"></div> -->
												</div>
												<center>
												<input name="search_button" type="button" class="btn btn-success" id="search_button" value="Search" 
												onClick="BranchCheck('txt_search',this,'hint_txt_search');"/>
											</center>
											
											<center>
											<div class="" id="hint_txt_search"></div>
											</center>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
