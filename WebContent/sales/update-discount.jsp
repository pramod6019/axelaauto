<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Update_Discount" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%> 

</HEAD>
<body onload="FormFocus();"
	class="page-container-bg-solid page-header-menu-fixed">
	
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
						<h1>Add Discount</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!--- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp?all=yes">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href="list-discount.jsp?all=yes">List Discount</a> &gt;</li>
						<li><a href="update-discount.jsp?add=yes">Add Discount</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- BODY START -->
							<form name="form1" id="form1" class="form-horizontal" method="post">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>


								<!-- 	PORTLET customner details-->
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Discount Details</div>
									</div>
									<div class="portlet-body portlet-empty container-fluid">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<!--     start customer details -->

											<center>
												<font size="2">Form fields marked with a red asterisk <b><font
														color="#ff0000">*</font></b> are required.<br></br>
												</font>
											</center>
											
											<div class="form-element4">
												<label> Job Title<font color=red>*</font>: </label>
												<div >
													<select name="dr_jobtitle_id" id="dr_jobtitle_id" class="dropdown form-control">
														<%=mybean.PopulateJobtitle()%>
													</select> 
												</div>
											</div>
											
											<div class="form-element4">
												<label> Brand <font color=red>*</font>: </label> </label>
												<div >
													<select name="dr_brand" id="dr_brand" class="form-control" onChange="PopulateModel();">
														<%=mybean.PopulateBrand(mybean.discount_brand_id, mybean.comp_id)%>
													</select> 
												</div>
											</div>
											<div class="form-element4">
														<label >Model: </label>
															<span id="dr_model"> 
															<%=mybean.PopulateModel(mybean.discount_brand_id, mybean.comp_id)%>
															</span>
												</div>
											<div class="form-element4">
												<label>Discount<font color=red>*</font>: </label>
												<input name="txt_disc_amount" id="txt_disc_amount" value="<%=mybean.discount_amount%>"
													class="form-control" onKeyUp="toFloat('txt_disc_amount','Discount')" type="text" maxlength="10" />
											</div>

											<div class="form-element4">
												<label>Year<font color="#ff0000">*</font>: </label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year">
														<%=mybean.PopulateYear(mybean.year)%>
													</select>
												</div>
											</div>
											<div class="form-element4">
											<label>&nbsp;</label>
												<div>
													<select name="dr_month" class="form-control" id="dr_month">
														<%=mybean.PopulateMonth(mybean.month)%>
													</select>
												</div>
											</div>
											<!-- 												entry and modify start -->
											<%
												if (mybean.status.equals("Update") && !(mybean.discount_entry_by == null)
														&& !(mybean.discount_entry_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Entry By:&nbsp; </label>
												<span >
													<%=mybean.unescapehtml(mybean.discount_entry_by)%>
												</span>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.entry_date == null)
														&& !(mybean.entry_date.equals(""))) {
											%>
											<div class="form-element6">
												<label> Entry Date:&nbsp;</label>
												<span >
													<%=mybean.entry_date%>
												</span>
											</div>
											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.discount_modified_by == null)
														&& !(mybean.discount_modified_by.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified By:&nbsp;</label>
												<span>
													<%=mybean.unescapehtml(mybean.discount_modified_by)%>
												</span>
											</div>

											<%
												}
											%>

											<%
												if (mybean.status.equals("Update") && !(mybean.modified_date == null)
														&& !(mybean.modified_date.equals(""))) {
											%>
											<div class="form-element6">
												<label>Modified Date:&nbsp;</label>
												<span>
													<%=mybean.modified_date%>
												</span>
											</div>
											<%
												}
											%>
											
<!-- 											entry and modify finish -->
											<%
												if (mybean.status.equals("Add")) {
											%>
											<div class="form-element12">
												<center>
													<input name="addbutton" type="submit"
														class="button btn btn-success" id="addbutton" value="Add Discount"
														onclick="return SubmitFormOnce(document.form1,this);" />
												</center>
												<input type="hidden" name="add_button" value="yes" />
											</div>

											<%
												} else if (mybean.status.equals("Update")) {
											%>
											<div class="form-element12">
												<input type="hidden" name="update_button" value="yes" />
												<center>
													<input name="updatebutton" type="submit"
														class="button btn btn-success" id="updatebutton" value="Update Discount"
														onclick="return SubmitFormOnce(document.form1,this);" />
													<input name="delete_button" type="submit"
														class="button btn btn-success" id="delete_button"
														OnClick="return confirmdelete(this)" value="Delete Discount" />
												</center>
											</div>

											<%
												}
											%>
											<input type="hidden" name="discount_entry_by" value="<%=mybean.discount_entry_by%>">
											<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
											<input type="hidden" name="discount_modified_by" value="<%=mybean.discount_modified_by%>">
											<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>"> 
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
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
	function PopulateModel(){   
	    var discount_brand_id = document.getElementById('dr_brand').value;
		if(discount_brand_id!="" && discount_brand_id!="0") {
	        showHint('../sales/mis-check1.jsp?discount_brand_id='+discount_brand_id+'&discountbrand=yes', 'dr_model');
	    }		
	}
</script>
</body>
</HTML>

