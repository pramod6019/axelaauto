<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Item_Options"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>

<link href="../assets/css/style.css" rel="stylesheet" type="text/css" />
</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0"
	onload="PopulatePrincipalSupport()">
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
						<h1>Item Options</h1>
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
						<li><a href="inventory-item-list.jsp?all=yes">List Items</a>
							&gt;</li>
						<li><a
							href="inventory-item-list.jsp?item_id=<%=mybean.option_itemmaster_id%>"><%=mybean.itemmaster_name%></a>
							&gt;</li>
						<li><a
							href="item-options.jsp?item_id=<%=mybean.option_itemmaster_id%>">Configure
								Product</a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="form1" id="form1" class="form-horizontal"
								method="post"
								action="item-options.jsp?add=yes&item_id=<%=mybean.option_itemmaster_id%>">
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Configure Items for
											<%=mybean.itemmaster_name%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<!-- START PORTLET BODY -->

											<input type="hidden" id="txt_itemmaster_id"
												name="txt_itemmaster_id"
												value="<%=mybean.option_itemmaster_id%>" />
												<div class="form-element6 form-element-center">
												<label> Search<font color=red>*</font>: </label>
													<input name="txt_search" type="text"
													class="form-control" id="txt_search" value="" size="30"
													maxlength="255" onKeyUp="ItemSearch();" />
											</div>
											<div class="hint" id="hint_search_item">
												<center>Enter your search parameter!</center>
											</div>
											<center><font size="3"><b>
												<div style="display: inline" id="item_name"><%=mybean.item_name%></div></b></font>
												 <input name="txt_item_id" type="hidden" id="txt_item_id"
													value="<%=mybean.option_item_id%>" />
											</center>
											</div>
											</div>
											</div>
											<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Item Details
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
													<div class="form-element3">
														<label> Group<font color="#ff0000">*</font>:  </label>
															<select id="dr_option_group_id" name="dr_option_group_id"
																class="form-control"
																onchange="GetOptionType(this.value);PopulatePrincipalSupport();">
																<option value="0">Select</option>
																<%=mybean.PopulateGroups()%>
															</select>
													</div>

												<div class="form-element3">
														<label> Option Type: </label>
															<span id="option_type"><%=mybean.group_type_name%></span>
												</div>
												<div class="form-element3">
														<label> Selected: </label>
															<input type="checkbox" id="chk_option_select"
																name="chk_option_select"
																<%=mybean.PopulateCheck(mybean.option_select)%> />
												</div>
														<div class="form-element3">
														<label> Executive Discount: </label>
															<input type="checkbox" id="chk_option_exediscount"
																name="chk_option_exediscount"
																<%=mybean.PopulateCheck(mybean.option_exediscount)%> />
													</div>
													<div class="row"></div>
													
													<div class="form-element3" id="principalsupport">
														<label>Principal Support:  </label>
															<select id="dr_principalsupport_id" name="dr_principalsupport_id"
																class="form-control"
																<option value="0">Select</option>
																<%=mybean.PopulatePrincipalSupport()%>
															</select>
													</div>
													
													<div class="form-element3">
														<label> Qty<font color="#ff0000">*</font>: </label>
															<input name="txt_option_qty" type="text"
																id="txt_option_qty" class="form-control"
																value="<%=mybean.option_qty%>" size="10" maxlength="10"
																onkeyup="toInteger('txt_option_qty','Qty')" />
												</div>

													<div class="form-element3">
														<label> Valid From<font color="#ff0000">*</font>: </label>
															<input name="txt_option_validfrom" type="text"
																class="form-control datepicker"
																 id="txt_option_validfrom"
																size="12" maxlength="10"
																value="<%=mybean.option_valid_from%>" />
													</div>
												<div class="form-element3">
														<label> Valid Till<font color="#ff0000">*</font>: </label>
															<input name="txt_option_validtill" type="text"
																class="form-control datepicker"
																 id="txt_option_validtill"
																size="12" maxlength="10"
																value="<%=mybean.option_valid_till%>" />
												</div>
												
												<div class="row"></div>
												<center>
												<div>
												<div id="mode_button">
													<input name="add_button" id="add_button" type="button"
														class="btn btn-success" value="Add Item"
														onClick="AddOptionItem();" />
												</div>
												</div>
												</center>
												
										</div>
									</div>
								</div>
								<%=mybean.StrHTML%>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script>
	function ItemSearch() {
		//document.getElementById("hint_item_qty").innerHTML = "";
		var itemmaster_id = document.getElementById("txt_itemmaster_id").value;
		var value = document.getElementById("txt_search").value;
		var url = "inventory-item-check.jsp?itemmaster_id=" + itemmaster_id;
		var param = "&q=" + encodeURIComponent(value);
		var str = "123";
		showHint(url + param, 'hint_search_item');
	}

	function PopulateItem(item_id, item_name) {
		document.getElementById("txt_item_id").value = item_id;
		document.getElementById("item_name").innerHTML = item_name;
	}

	function AddOptionItem() {
		document.form1.submit();
	}

	function GetOptionType(group_id) {
		showHint('inventory-item-check.jsp?group_id=' + group_id
				+ '&get_option_type=yes', 'option_type');
	}
	
	function PopulatePrincipalSupport() {
		if($("#dr_option_group_id").val()==1){
			$("#principalsupport").show();
		}else{
			$("#principalsupport").hide();
		}
	}
</script>
</body>
</HTML>
