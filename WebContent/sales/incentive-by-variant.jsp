<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Incentive_By_Variant" scope="request" />
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
						<li><a href="incentive-by-variant.jsp">List Incentive By Variant </a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							
								<center>
									<font color="#ff0000"><b><div id="msg"><%=mybean.msg%> </div></b></font>
								</center>
							
							<form name="form1" method="get" class="form-horizontal">

								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">Search</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="tab-pane" id="">
											<!-- START PORTLET BODY -->
											
											<div class="form-element3">
												<label>Brand<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_brand" class="form-control"
														id="dr_brand" onChange="document.form1.submit();">
														<%=mybean.PopulatePrincipal(mybean.brand_id, mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Model<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_model" class="form-control"
														id="dr_model" onChange="document.form1.submit();">
														<%=mybean.PopulateModel(mybean.brand_id, mybean.model_id, mybean.comp_id, request)%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
												<label>Year<font color=red>*</font>:&nbsp; </label>
												<div>
													<select name="dr_year" class="form-control" id="dr_year" onChange="document.form1.submit()">
														<%=mybean.PopulateYear(mybean.year)%>
													</select>
												</div>
											</div>
											
											<div class="form-element3">
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
								
								<%if(mybean.msg.equals("")){ %>
								<div align=right>
								<a class="button btn btn-success glyphicon glyphicon-plus" data-target='#Hintclicktocall' data-toggle=modal href=../sales/incentive-by-variant-add.jsp?brand_id=<%=mybean.brand_id%>&model=<%=mybean.model_id%>&month=<%=mybean.month%>&year=<%=mybean.year%>></a>
								</div>
								<%} %>
								
								<div id="listincentive">
									<center><%=mybean.StrHTML%></center>
								</div>

							</form>

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
 		var brand_id = GetReplace(document.getElementById("dr_brand").value);
 		var model_id = GetReplace(document.getElementById("dr_model").value);
 		var year = <%=mybean.year%>;
 		var month = <%=mybean.month%>;
 		var incentivevariant_id = name.split("_")[3];
 		var item_id = name.split("_")[4];
 		var from_date = document.getElementById("txt_incentivevariant_startdate_"+incentivevariant_id+"_"+item_id).value;
 		var to_date = document.getElementById("txt_incentivevariant_enddate_"+incentivevariant_id+"_"+item_id).value;
 		var url = "../sales/incentive-check.jsp?";
 		var param;
		var str = "123";
 		value = GetReplace(obj.value);
 		param = "name=" + name + "&value=" + value + "&incentivevariant_id=" + incentivevariant_id + "&brand_id=" + brand_id + "&model_id=" + model_id 
 				+ "&from_date=" + from_date+ "&to_date=" + to_date+ "&year=" + year+ "&month=" + month+"&item_id="+item_id;
 		showHint(url + param, hint);
 	}
 	
function deleteIncentive(incentivevariant_id) {
// 	alert("12334");
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var brand_id = <%=mybean.brand_id%>;
	var model_id = <%=mybean.model_id%>;
// 	alert("year==="+year);
// 	alert("month==="+month);
	showHint('../sales/incentive-check.jsp?deletevariant=yes&incentivevariant_id=' + incentivevariant_id
			+ '&year=' + year
			+ '&month=' + month, 'msg');
	showHint('../sales/incentive-check.jsp?brand_id=' + brand_id
			+ '&year=' + year
			+ '&month=' + month
			+ '&brand_id=' + brand_id
			+ '&model_id=' + model_id
			+ '&listincentivevariant=yes','listincentive');

}


function addIncentive(){
	var brand_id = <%=mybean.brand_id%>;
	var model_id = <%=mybean.model_id%>;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	var item_id = document.getElementById("dr_item").value;
	var from_date = document.getElementById("txt_incentivevariant_startdate").value;
	var to_date = document.getElementById("txt_incentivevariant_enddate").value;
	var amount = document.getElementById("txt_incentivevariant_amount").value;
// 	alert("item_id=="+item_id);
// 	alert("from_date=="+from_date);
// 	alert("to_date=="+to_date);
// 	alert("year=="+year);
// 	alert("month=="+month);
// 	alert("amount=="+amount);
	showHint('../sales/incentive-by-variant-add-check.jsp?brand_id=' + brand_id
				+ '&model_id=' + model_id	
				+ '&year=' + year
				+ '&month=' + month
				+ '&from_date=' + from_date
				+ '&to_date=' + to_date
				+ '&item_id=' + item_id
				+ '&brand_id=' + brand_id
				+ '&amount=' + amount
				+ '&add_button=yes','msg');
}

function listIncentive(){
// 	alert("12334");
	var brand_id = <%=mybean.brand_id%>;
	var model_id = <%=mybean.model_id%>;
	var year = <%=mybean.year%>;
	var month = <%=mybean.month%>;
	showHint('../sales/incentive-check.jsp?brand_id=' + brand_id
			+ '&model_id=' + model_id
			+ '&year=' + year
			+ '&month=' + month
			+ '&listincentive=yes','listincentive');
}

</script>
 	
 	
</script>
	
</body>
</HTML>
