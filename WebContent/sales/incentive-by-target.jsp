<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Incentive_By_Target" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

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
						<h1>List Incentive</h1>
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
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
<!-- 						<li><a href="../sales/team.jsp">Incentive</a>&gt;</li> -->
						<li><a href="incentive-by-target.jsp">List Incentive By Target </a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							
								<center>
									<font color="#ff0000"><b><div id="msg"><%=mybean.msg%></div></b></font>
								</center>
							
							<form name="form1" method="get" class="form-horizontal">

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											<div class="form-element4">
												<label>Brand<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_brand" class="form-control"
														id="dr_brand" onChange="document.form1.submit()">
														<%=mybean.PopulatePrincipal(mybean.brand_id, mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											
											<div class="form-element4">
												<label>Year<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year" onChange="document.form1.submit()">
														<%=mybean.PopulateYear(mybean.year)%>
													</select>
												</div>
											</div>
											
											<div class="form-element4">
												<label>Month<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_month" class="form-control" id="dr_month" onChange="document.form1.submit()">
														<%=mybean.PopulateMonth(mybean.month)%>
													</select>
												</div>
											</div>
											
										</div>
									</div>
								</div>
								</form>
								
								<%if(mybean.msg.equals("")){ %>
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											Slab Wise
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=slabwise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listslabwise">
									<center><%=mybean.StrHTML1%></center>
								</div>
								
								</div></div></div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Overall Incentive</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=overallwise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listoverallincentive">
									<center><%=mybean.StrHTML%></center>
								</div>
								
								</div></div></div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Insurance Slab</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=insurancewise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listinsurancewise">
									<center><%=mybean.StrHTML2%></center>
								</div>
								
								</div></div></div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Finance Slab</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=financewise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listfinancewise">
									<center><%=mybean.StrHTML3%></center>
								</div>
								
								</div></div></div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Accessories Slab</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=accessorieswise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listaccessorieswise">
									<center><%=mybean.StrHTML4%></center>
								</div>
								
								</div></div></div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Extended Warranty Slab</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=ewwise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listewwise">
									<center><%=mybean.StrHTML5%></center>
								</div>
								
								</div></div></div>
								
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Exchange Slab</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
								
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-target-add.jsp?type=exchangewise&band_id=<%=mybean.band_id%>&brand_id=<%=mybean.brand_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								
								<div id="listexchangewise">
									<center><%=mybean.StrHTML6%></center>
								</div>
								
								</div></div></div>
								<%} %>

						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	
<script language="JavaScript" type="text/javascript">
$(function(){
	$("#Hintclicktocall, #Hintclicktocall80").on('hidden.bs.modal',function(){
		document.form1.submit();
	});
});
function SecurityCheck(name, obj, hint) {
 		var value;
 		var year = <%=mybean.year%>;
 		var month = <%=mybean.month%>;
 		var brand_id = GetReplace(document.getElementById("dr_brand").value);
 		var band_id = name.split("_")[3];
 		var band_from = document.getElementById("txt_incentivetargetband_from_"+band_id).value;
 		var band_to = document.getElementById("txt_incentivetargetband_to_"+band_id).value;
 		band_from =  band_from.replace("%","");
 		band_to =  band_to.replace("%","");
 		var url = "../sales/incentive-check.jsp?";
 		var param;
		var str = "123";
 		value = GetReplace(obj.value);
 		param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id+ '&year=' + year
		+ '&month=' + month+ "&band_from=" + band_from+ "&band_to=" + band_to;
 		showHint(url + param, hint);
 	}
function SecurityCheck2(name, obj, hint) {
		var value;
		var brand_id = GetReplace(document.getElementById("dr_brand").value);
		var band_id = name.split("_")[3];
		var slab_from = document.getElementById("txt_incentiveslab_from_"+band_id).value;
		var slab_to = document.getElementById("txt_incentiveslab_to_"+band_id).value;
		var year = <%=mybean.year%>;
		var month = <%=mybean.month%>;
		var url = "../sales/incentive-check.jsp?";
		var param;
		var str = "123";
		value = GetReplace(obj.value);
		param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id + "&year=" + year + "&month=" + month+ "&slab_from=" + slab_from+ "&slab_to=" + slab_to;
		showHint(url + param, hint);
	}
	
function SecurityCheck3(name, obj, hint) {
	var value;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var brand_id = GetReplace(document.getElementById("dr_brand").value);
	var band_id = name.split("_")[3];
	var slab_from = document.getElementById("txt_incentiveinsurslab_from_"+band_id).value;
	var slab_to = document.getElementById("txt_incentiveinsurslab_to_"+band_id).value;
// 	slab_from =  slab_from.replace(/%/g,"");
	slab_from =  slab_from.replace("%","");
	slab_to =  slab_to.replace("%","");
	var url = "../sales/incentive-check.jsp?";
	var param;
	var str = "123";
	value = GetReplace(obj.value);
	param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id+ "&year=" + year
	+ "&month=" + month+ "&slab_from=" + slab_from+ "&slab_to=" + slab_to;
	showHint(url + param, hint);
}

function SecurityCheck4(name, obj, hint) {
	var value;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var brand_id = GetReplace(document.getElementById("dr_brand").value);
	var band_id = name.split("_")[3];
	var slab_from = document.getElementById("txt_incentivefinanceslab_from_"+band_id).value;
	var slab_to = document.getElementById("txt_incentivefinanceslab_to_"+band_id).value;
	
	var url = "../sales/incentive-check.jsp?";
	var param;
	var str = "123";
	value = GetReplace(obj.value);
	param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id+ '&year=' + year
	+ '&month=' + month+ "&slab_from=" + slab_from+ "&slab_to=" + slab_to;
	showHint(url + param, hint);
}
function SecurityCheck5(name, obj, hint) {
	var value;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var brand_id = GetReplace(document.getElementById("dr_brand").value);
	var band_id = name.split("_")[3];
	var slab_from = document.getElementById("txt_incentiveaccesslab_from_"+band_id).value;
	var slab_to = document.getElementById("txt_incentiveaccesslab_to_"+band_id).value;
	var url = "../sales/incentive-check.jsp?";
	var param;
	var str = "123";
	value = GetReplace(obj.value);
	param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id+ '&year=' + year
	+ '&month=' + month+ "&slab_from=" + slab_from+ "&slab_to=" + slab_to;
	showHint(url + param, hint);
}
function SecurityCheck6(name, obj, hint) {
	var value;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var brand_id = GetReplace(document.getElementById("dr_brand").value);
	var band_id = name.split("_")[3];
	var slab_from = document.getElementById("txt_incentiveewslab_from_"+band_id).value;
	var slab_to = document.getElementById("txt_incentiveewslab_to_"+band_id).value;
	slab_from =  slab_from.replace("%","");
	slab_to =  slab_to.replace("%","");
	var url = "../sales/incentive-check.jsp?";
	var param;
	var str = "123";
	value = GetReplace(obj.value);
	param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id+ '&year=' + year
	+ '&month=' + month+ "&slab_from=" + slab_from+ "&slab_to=" + slab_to;
	showHint(url + param, hint);
}
function SecurityCheck7(name, obj, hint) {
	var value;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var brand_id = GetReplace(document.getElementById("dr_brand").value);
	var band_id = name.split("_")[3];
	var slab_from = document.getElementById("txt_incentiveexchangeslab_from_"+band_id).value;
	var slab_to = document.getElementById("txt_incentiveexchangeslab_to_"+band_id).value;
	slab_from =  slab_from.replace("%","");
	slab_to =  slab_to.replace("%","");
	var url = "../sales/incentive-check.jsp?";
	var param;
	var str = "123";
	value = GetReplace(obj.value);
	param = "name=" + name + "&value=" + value + "&band_id=" + band_id + "&brand_id=" + brand_id+ '&year=' + year
	+ '&month=' + month+ "&slab_from=" + slab_from+ "&slab_to=" + slab_to;
	showHint(url + param, hint);
}
 	
function deleteIncentive(band_id, type) {
	var brand_id = GetReplace(document.getElementById("dr_brand").value);
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	console.log("type=="+type);
	if(type == "overallwise"){
	showHint('../sales/incentive-check.jsp?deletetarget=yes&band_id=' + band_id+ "&brand_id=" + brand_id
			+ '&year=' + year
			+ '&month=' + month, 'msg');
	setTimeout(showHint('../sales/incentive-check.jsp?listincentivetarget=yes&band_id=' + band_id+ "&brand_id=" + brand_id
			+ '&year=' + year
			+ '&month=' + month, 'listoverallincentive'),200);
	}else{
		showHint('../sales/incentive-check.jsp?type=' + type + '&band_id=' + band_id+ "&brand_id=" + brand_id
				+ '&year=' + year
				+ '&month=' + month, 'msg');
		setTimeout(showHint('../sales/incentive-check.jsp?listtype=' + type + '&band_id=' + band_id+ "&brand_id=" + brand_id
				+ '&year=' + year
				+ '&month=' + month, 'list' + type),200);
		}
	}


function addIncentive(){
	var brand_id = <%=mybean.brand_id%>;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var type =  document.getElementById("type").value;
// 	console.log("type=="+type);
	var amount = "";
	var param = "";
	var slab_from = "";
	var slab_to = "";
	var percentage = "";
	if (type == "overallwise" ){
		var band_from = document.getElementById("txt_incentivetargetband_from").value;
		var band_to = document.getElementById("txt_incentivetargetband_to").value;
		amount = document.getElementById("txt_incentivetargetband_amount").value;
		param = '&band_from=' + band_from + '&band_to=' + band_to + '&amount=' + amount;
	}
	if (type == "slabwise" ){
		slab_from = document.getElementById("txt_incentiveslab_from").value;
		slab_to = document.getElementById("txt_incentiveslab_to").value;
		var so_amount = document.getElementById("txt_incentiveslab_soamount").value;
		var finance_amount = document.getElementById("txt_incentiveslab_finamount").value;
		var insurance_amount = document.getElementById("txt_incentiveslab_insuramount").value;
		var ew_amount = document.getElementById("txt_incentiveslab_ewamount").value;
		var accessmin_amount = document.getElementById("txt_incentiveslab_accessminamount").value;
		var access_amount = document.getElementById("txt_incentiveslab_accessamount").value;
		var exchange_amount = document.getElementById("txt_incentiveslab_exchangeamount").value;
		param = '&slab_from=' + slab_from + '&slab_to=' + slab_to + '&so_amount=' + so_amount + '&finance_amount=' + finance_amount 
				+ '&insurance_amount=' + insurance_amount + '&ew_amount=' + ew_amount + '&accessmin_amount=' + accessmin_amount 
				+ '&access_amount=' + access_amount + '&exchange_amount=' + exchange_amount ;
	}
	if (type == "insurancewise" || type == "ewwise" || type == "exchangewise"){
		slab_from = document.getElementById("txt_"+type+"_from").value;
		slab_to = document.getElementById("txt_"+type+"_to").value;
		amount = document.getElementById("txt_"+type+"_amount").value;
		param = '&slab_from=' + slab_from + '&slab_to=' + slab_to + '&amount=' + amount;
	}
	
	if (type == "financewise"){
		slab_from = document.getElementById("txt_"+type+"_from").value;
		slab_to = document.getElementById("txt_"+type+"_to").value;
		amount = document.getElementById("txt_"+type+"_amount").value;
		param = '&slab_from=' + slab_from + '&slab_to=' + slab_to + '&amount=' + amount;
	}
	
	if (type == "accessorieswise"){
		slab_from = document.getElementById("txt_"+type+"_from").value;
		slab_to = document.getElementById("txt_"+type+"_to").value;
		percentage = document.getElementById("txt_"+type+"_percentage").value;
		param = '&slab_from=' + slab_from + '&slab_to=' + slab_to + '&percentage=' + percentage;
	}
	showHint('../sales/incentive-by-target-add-check.jsp?brand_id=' + brand_id
				+ '&year=' + year
				+ '&month=' + month
				+ '&brand_id=' + brand_id
				+ param
				+ '&type=' + type
				+ '&add_button=yes','msg');
}
</script>
	
</body>
</HTML>
