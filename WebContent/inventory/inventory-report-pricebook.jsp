<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.inventory.Inventory_Report_PriceBook" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body bgColor="#ffffff" leftmargin="0" rightmargin="0" topmargin="0"
	bottommargin="0" onLoad="PopulateExport()">
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
						<h1>Inventory Price Book</h1>
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
						<li><a href="index.jsp">Inventory</a> &gt;</li>
						<li><a href="../inventory/inventory-report-pricebook.jsp"
							target="_blank">Price Book</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="red"><b><%=mybean.msg%></b></font>
							</center>
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">Inventory Price
										Book</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<form name="formemp" class="form-horizontal" method="post">
											<center>
												<b>Price as on <%=mybean.date %></b>
											</center>
												<div class="form-element4">
														<label> Rate Class<font color=red>*</font>: </label>
															<select name="dr_rateclass_id	" class="form-control"
																id="dr_rateclass_id	">
																<%=mybean.PopulateBranchClass(mybean.comp_id)%>
															</select>
													</div>

												<div class="form-element4">
														<label> Brand<font color=red>*</font>: </label>
															<select name="drop_model_brand_id" class="form-control"
																id="drop_model_brand_id" onchange="PopulateModel();">
																<%=mybean.PopulatePrincipal(mybean.comp_id)%>
															</select>
												</div>
												<div class="form-element4">
														<label> Model: </label>
															<span id="modelHint"> <%=mybean.PopulateModel(mybean.comp_id, mybean.brand_id)%>
															</span>
												</div>
 											<div class="form-element4">
														<label> Fuel Type: </label>
															<select id="dr_fueltype_id" name="dr_fueltype_id"
																class="form-control">
																<%=mybean.PopulateFuelType(mybean.comp_id)%>
															</select>
												</div>
												<div class="form-element4">
														<label> Type: </label>
															<select id="dr_item_type_id" name="dr_item_type_id"
																class="form-control" onChange="PopulateExport()">
																<%=mybean.PopulateItemType(mybean.comp_id)%>
															</select>
												</div>
												<div class="form-element4">
														<label> Category: </label>
															<select name="dr_cat_id" class="form-control"
																id="dr_cat_id">
																<option value=0>Select Category</option>
																<%= mybean.PopulateCategoryPop(mybean.cat_id,mybean.comp_id,"0","1", request)%>
															</select>
												</div>
											 <div class="row"></div>
													<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" />
															<input type="hidden" name="submit_button" value="Submit"></input>
														 
														<div id="export"></div>
													</center>
											</div>
										</form>
									</div>
								</div>
							</div>
							<%=mybean.StrHTML%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	<script language="JavaScript">
                function PopulateItemPrice() { //v1.0
               		document.formemp.submit();
            	}
            	function PopulateModel(){
            		var brand_id = document.getElementById("drop_model_brand_id").value;
            	    showHint('../inventory/item-check.jsp?model=yes&brand_id='+brand_id, 'modelHint');		
            	}
            	function PopulateExport(){
            		var item_type_id = $('#dr_item_type_id').val();
            		if(item_type_id == 1){
            			document.getElementById("export").innerHTML = "<a href=\"inventory-report-pricebook-export.jsp?smart=yes\" class=\"btn btn-success\" type=\"button\">Export</a>";
            		}else {
            			document.getElementById("export").innerHTML = "";
            		}
            		
            	}
//             	function PopulateExport(){
//             		var item_type_id = $('#dr_item_type_id').val();
<%--             		var rateclass_id = <%=mybean.rateclass_id%>; --%>
//             		if(item_type_id == 1){
//             			document.getElementById("export").innerHTML = '<a href=inventory-report-pricebook-export.jsp?smart=yes&rateclass_id=' + rateclass_id + ' class=\"btn btn-success\" type=\"button\">Export</a>';
//             		}else {
//             			document.getElementById("export").innerHTML = "";
//             		}
            		
//             	}
</script>
</body>
</html>
