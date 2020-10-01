<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
	
	
	
	<!--  tablet and mobile view -->
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
		 
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		
		
	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />	 






	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
	
<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>	
	<body onLoad="FormFocus()"
	class="page-container-bg-solid page-header-menu-fixed">
	
	
	
<!-- 	MODAL START -->
<a class=" btn yellow btn-outline sbold" href="modals.html" data-target="#ajax" data-toggle="modal"> View Demo </a>
	<div class="modal fade" id="ajax" role="basic" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body">
					<img src="../assets/global/img/loading-spinner-grey.gif" alt=""
						class="loading"> <span> &nbsp;&nbsp;Loading... </span>
				</div>
			</div>
		</div>
	</div>
	<!-- NEW PAGE -->
<div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
    <h4 class="modal-title">Ajax Content</h4>
</div>
<div class="modal-body">
    <div class="row">
        <div class="col-md-12">
            <h4>Some Input</h4>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
            <p>
                <input type="text" class="col-md-12 form-control"> </p>
        </div>
    </div>
</div>
<div class="modal-footer">
    <button type="button" class="btn default" data-dismiss="modal">Close</button>
    <button type="button" class="btn blue">Save changes</button>
</div>

<!-- 	MODAL END -->



<!-- 	TABLET MOBILE AND WEB FORMAT START -->

	<form class="form-horizontal" action="">

		
<!-- 		Select DropDown -->
		<div class="form-group">
			<label class="control-label col-md-3">Default</label>
			<div class="col-md-6">
				<select class="bs-select form-control bs-select-hidden">
					<option>Mustard</option>
					<option>Ketchup</option>
					<option>Relish</option>
				</select>
			</div>
		</div>
		
<!-- 		Multiple Select -->
		<div class="form-group">
			<label class="control-label col-md-3">Default</label>
			<div class="col-md-6">
				<select class="bs-select form-control-select">
					<option>Mustard</option>
					<option>Ketchup</option>
					<option>Relish</option>
				</select>
			</div>
		</div>
		
		
		<div class="form-group">
			<label class="control-label col-md-4">Default</label>
			<div class="txt-align">
				<input type="checkbox"></input>
			</div>
		</div>
	</form>


	<!-- 	TABLET MOBILE AND WEB FORMAT END -->
	
	
<!-- 	TEXT EDITOR START  -->
	<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/summernote.css" rel="stylesheet"
	type="text/css" />
	
	<script src="../assets/js/components-editors.min.js"
		type="text/javascript"></script>
		<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
	
	
	
									<div class="form-group">
										<label class="col-md-4 control-label">Description:</label>
										<div class="col-md-6">
											<div name="summernote" id="summernote_1"></div>
											<script type="text/javascript">
                                                                    CKEDITOR.replace('txt_project_desc',
                                                                    {
                                                                      uiColor: hexc($("a:link").css("color")),
																	
                                                                    });
                                                                </script>
										</div>
									</div>
	
	
<!-- 	TEXT EDITOR END -->	


<!-- 	DATEPICKER START -->
	<link rel="shortcut icon" href="../test/favicon.ico" />
	<link href="../assets/css/bootstrap-datepicker3.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />
	
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datepicker.js"
		type="text/javascript"></script>
		
		<div class="form-group">
					<label class="control-label col-md-4">Start Date:<font
						color=#ff0000><b> *</b></font></label>
					<div class="col-md-6">
						<input name="txt_starttime" id="txt_starttime"
							value="" class="form-control date-picker"
							data-date-format="dd/mm/yyyy" type="text" value="" />
					</div>
				</div>
<!-- 	DATEPICKER END -->
	
	
<!-- 	TIME PICKER START-->
<link href="../assets/css/bootstrap-timepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap-datetimepicker.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/plugins.css" rel="stylesheet" type="text/css" />	


<script src="../assets/js/bootstrap-timepicker.js"
		type="text/javascript"></script>
	<script src="../assets/js/components-date-time-pickers.js"
		type="text/javascript"></script>
	<script src="../assets/js/bootstrap-datetimepicker.js"
		type="text/javascript"></script>
		
		
								<div class="form-group">
										<label class="control-label col-md-4">Start Time<font
											color=#ff0000><b>*</b></font>:</label>
										<div class="col-md-6">
											<div class="input-group date form_datetime">
												<input type="text" size="16"  name ="txt_task_starttime" id ="txt_task_starttime"
													value="" class="form-control">
												<span class="input-group-btn">
													<button class="btn default date-set" type="button">
														<i class="fa fa-calendar"></i>
													</button>
												</span>
											</div>
										</div>
									</div>
<!-- 	TIME PICKER END -->
	
<!-- 	FOOTABLE START -->
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../assets/css/footable.core.css">
<script src="../assets/js/footable.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function() {
			$('table')
					.footable(
							{
								toggleHTMLElement : '<span><div class="footable-toggle footable-expand" border="0"></div>'
										+ '<div class="footable-toggle footable-contract" border="0"></div></span>'
							});
		});
	</script>

<!-- 	FOOTABLE END -->
	
<!-- 	MULTIPLE SELECT START-->
	<link href="../assets/css/multi-select.css" rel="stylesheet"
	type="text/css" />
	<script src="../assets/js/jquery.multi-select.js"
		type="text/javascript"></script>
	<script src="../assets/js/select2.full.min.js" type="text/javascript"></script>
	<script src="../assets/js/components-multi-select.min.js"
		type="text/javascript"></script>
		
								<div class="form-group">
										<label class="control-label col-md-4"></label>
										<div class="col-md-6 col-xs-12" id="emprows">
											<select name="my_multi_select1" multiple="multiple"
												class="multi-select" id="my_multi_select1">
												
												
											</select>
											<input type="text" id="select_list" name="select_list" hidden />
										</div>
									</div>
	
	
	<script>

	 var MultiSelect = function (element, options) {
		    this.options = options;
		    this.$element = $(element);
		    this.$container = $('<div/>', { 'class': "ms-container" });
		    this.$selectableContainer = $('<div/>', { 'class': 'ms-selectable' });
		    this.$selectionContainer = $('<div/>', { 'class': 'ms-selection', 'id' : 'select_name' });
		    this.$selectableUl = $('<ul/>', { 'class': "ms-list", 'tabindex' : '-1' });
		    this.$selectionUl = $('<ul/>', { 'class': "ms-list", 'tabindex' : '-1' });
		    this.scrollTo = 0;
		    this.elemsSelector = 'li:visible:not(.ms-optgroup-label,.ms-optgroup-container,.'+options.disabledClass+')', 'value = 111';
		  };
	
	
	</script>
<!-- 	MULTIPLE SELECT END-->	
<!-- 	BODY -->
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
						<h1>Campaign</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="campaign.jsp">Campaigns</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
					
					</center>
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					
						 

					</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
<!-- 	CONTAINER-FLUID -->
	<div class="container-fluid ">
													<div class="form-body col-md-6 col-sm-6">
																<div class="form-group">
																	<label class="col-md-3 control-label">
																	</label>
																	<div class="col-md-6" style="top: 9px">
																	</div>
																	</div>
																</div>
															
														<div class="form-body col-md-6 col-sm-6">
																<div class="form-group">
																	<label class="col-md-3 control-label">
																	</label>
																	<div class="col-md-6" style="top: 9px">
																	</div>
																</div>
															</div>

														
													</div>
	
	<!-- 	Input Textbox -->
		<div class="form-body">
			<div class="form-group">
				<label class="col-md-3 control-label">Enter Label</label>
				<div class="col-md-6">
					<input class="form-control" type="text">

				</div>
			</div>
		</div>
<!-- 	PORTLET -->
	<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Search 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
						<!-- START PORTLET BODY -->
						

					</div>
				</div>
			</div>






<!-- FORM START -->
<div class="form-group">
<label class="control-label col-md-4"> Export<font color=red>*</font>: </label>
<div class="col-md-6 col-xs-12" id="emprows">
	

</div>
</div>








		Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Branch ID</th>\n");
					Str.append("<th>Branch Details</th>\n");
					Str.append("<th data-hide=\"phone\">Brand</th>\n");
					Str.append("<th data-hide=\"phone\">Contacts</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Address</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Notes</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (rs.next()) {
						Date d = new Date();
						if (rs.getString("branch_logo").equals("")) {
							Img = "";
						} else {
							Img = "<img src=\"../Thumbnail.do?branchlogo=" + rs.getString("branch_logo")
									+ "&width=200&time=" + d.getTime() + "&target=" + Math.random()
									+ "&dummy=84456663 alt=" + rs.getString("branch_name") + "\"><br>";
						}
						count = count + 1;
						if (rs.getString("branch_active").equals("0")) {
							active = "<br><font color=red > [Inactive] </font>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td>").append(count).append("</td>\n");
						Str.append("<td>").append(rs.getString("branch_id")).append("</td>\n");
						Str.append("<td>").append(Img).append(rs.getString("branch_name")).append(active).append("<br>Franchisee:  ")
								.append(" <a href=franchisee-list.jsp?franchisee_id=");
						Str.append(rs.getString("franchisee_id")).append(">").append(rs.getString("franchisee_name")).append(" (").append(rs.getString("franchisee_id")).append(")</a>");
						Str.append("<br>Class: ").append(rs.getString("rateclass_name"));
						Str.append("</td>");
						Str.append("<td>").append(rs.getString("principal_name")).append("</td>");
						Str.append("<td>");

						Str.append(SplitPhoneNo(rs.getString("branch_phone1"), 4, "T")).append("<br>");

						if (!rs.getString("branch_phone2").equals("")) {
							Str.append(SplitPhoneNo(rs.getString("branch_phone2"), 4, "T")).append("<br>");
						}

						Str.append(SplitPhoneNo(rs.getString("branch_mobile1"), 5, "M")).append("<br>");

						if (!rs.getString("branch_mobile2").equals("")) {
							Str.append(SplitPhoneNo(rs.getString("branch_mobile2"), 5, "M")).append("<br>");
						}
						Str.append("<a href=mailto:").append(rs.getString("branch_email1")).append(">").append(rs.getString("branch_email1")).append("</a><br>");
						if (!rs.getString("branch_email2").equals("")) {
							Str.append("<a href=mailto:").append(rs.getString("branch_email2")).append(">").append(rs.getString("branch_email2")).append("</a>");
						}
						Str.append("</td><td>");
						if (!rs.getString("branch_add").equals("")) {
							Str.append(rs.getString("branch_add")).append(", ");
						}
						Str.append("<br>").append(rs.getString("city_name")).append("");
						if (!rs.getString("branch_pin").equals("")) {
							Str.append(" - ").append(rs.getString("branch_pin")).append("");
						}
						// if (rs.getString("branch_pin").equals("")) {
						// Str.append(", ");
						// }
						// Str.append(rs.getString("city_name"));
						Str.append("</td>\n");
						Str.append("<td>");
						Str.append(rs.getString("branch_notes")).append("</td>\n");
						Str.append("<td><a href=\"branch-update.jsp?update=yes&branch_id=").append(rs.getString("branch_id")).append(" \">Update Branch</a>");
						Str.append("<br><a href=\"../portal/branch-logo.jsp?branch_id=").append(rs.getString("branch_id")).append("\">Update Logo</a>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");


<!-- SEARCH HIDe SHOW -->
<script>
	$(document).ready(function() {
		$("#open").hide();
		$("#search").click(function() {
			$("#open").toggle("slow");
			 $(".fa-plus").toggle();
			 $(".fa-minus").toggle();
			
		});
		$("#minus").hide();	    
		
	});
</script>


<!-- TAB SET -->
<div class="tabbable tabbable-tabdrop">
                                                <ul class="nav nav-tabs">
                                                    <li class="active">
                                                        <a href="#tab1" data-toggle="tab">Section 1</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab2" data-toggle="tab">Section 2</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab3" data-toggle="tab">Section 3</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab4" data-toggle="tab">Section 4</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab5" data-toggle="tab">Section 5</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab6" data-toggle="tab">Section 6</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab7" data-toggle="tab">Section 7</a>
                                                    </li>
                                                    <li>
                                                        <a href="#tab8" data-toggle="tab">Section 8</a>
                                                    </li>
                                                      <li>
                                                        <a href="#tab9" data-toggle="tab">Section 9</a>
                                                    </li>
                                                      <li>
                                                        <a href="#tab10" data-toggle="tab">Section 10</a>
                                                    </li>
                                                      <li>
                                                        <a href="#tab11" data-toggle="tab">Section 11</a>
                                                    </li>
                                                </ul>
                                                <div class="tab-content">
                                                    <div class="tab-pane active" id="tab1">
                                                        <p> I'm in Section 1. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab2">
                                                        <p> Howdy, I'm in Section 2. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab3">
                                                        <p> Howdy, I'm in Section 3. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab4">
                                                        <p> Howdy, I'm in Section 4. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab5">
                                                        <p> Howdy, I'm in Section 5. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab6">
                                                        <p> Howdy, I'm in Section 6. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab7">
                                                        <p> Howdy, I'm in Section 7. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab8">
                                                        <p> Howdy, I'm in Section 8. </p>
                                                    </div>
                                                    <div class="tab-pane" id="tab9">
                                                        <p> Howdy, I'm in Section 9. </p>
                                                    </div>
                                                     <div class="tab-pane" id="tab10">
                                                        <p> Howdy, I'm in Section 10. </p>
                                                    </div>
                                                     <div class="tab-pane" id="tab11">
                                                        <p> Howdy, I'm in Section 11. </p>
                                                    </div>
                                                </div>
                                            </div>




<!-- 	RANGE PICKER -->
	<div class="form-group">
                                                        <label class="col-md-3 control-label">Basic</label>
                                                        <div class="col-md-4">
                                                            <div id="demo2" class="noUi-success"></div>
                                                        </div>
                                                    </div>
<!-- 	JQUERY DATEPICKER  -->
<!-- 		put the below statement inside document.ready -->
		$('#txt_enquiry_exp_close_date').datepicker().on('changeDate', function(){
			SecurityCheck('txt_enquiry_exp_close_date',this,'hint_txt_enquiry_exp_close_date');
		});
		
<!-- 	JQUERY DATEPICKER  -->



<!-- Main Header -->
		<li class="list-group-item bg-blue bg-font-blue">Cras justo odio
		</li>
		<li class="list-group-item bg-red bg-font-red">Dapibus ac
			facilisis in</li>
		<li class="list-group-item bg-green bg-font-green">Morbi leo
			risus</li>
		<li class="list-group-item bg-purple bg-font-purple">Porta ac
			consectetur ac</li>
		<li class="list-group-item bg-yellow bg-font-yellow">Vestibulum
			at eros</li>

			
<!-- 			CKEditor -->
			<div name="" id="" class="summernote_1"> </div>
		
<link href="../assets/css/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />
        <link href="../assets/css/summernote.css" rel="stylesheet" type="text/css" />		
		
<script src="../assets/js/summernote.min.js" type="text/javascript"></script>
   <script src="../assets/js/components-editors.min.js" type="text/javascript"></script>
			