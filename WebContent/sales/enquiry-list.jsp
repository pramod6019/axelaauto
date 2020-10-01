<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.sales.Enquiry_List"
	scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<%response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin")); %>
<%response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS"); %>
<%//response.setHeader("Access-Control-Allow-Headers", "GAuthorization, X-Requested-With, Content-Type, Origin, Accept"); %>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<%@include file="../Library/css.jsp"%>

<style>
#listcheck {
	display: inline-block;
}
</style>
</HEAD>
<body <%if(mybean.advSearch.equals(null) || mybean.advSearch.equals("")){%>
	onLoad="LoadRows();DisplayManuf();FormFocus();" <%} else {%>
	onLoad="DisplayManuf();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%//if(!mybean.pop.equals("yes")){%>
	<%@include file="../portal/header.jsp"%>
	<%//}%>
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
						<h1>List Enquiry</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<!-- END PAGE BREADCRUMBS -->

					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<%@include file="../Library/list-body-adv.jsp"%>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function(){
		$("#tblsmartsearch").next().children().css('width','75pc');
	});
	function populatefollowup(enquiry_id){
	if(document.getElementById("followup_"+enquiry_id).innerHTML==""){
		showHint('../sales/enquiry-followup.jsp?enquiry_id='+enquiry_id, 'followup_'+enquiry_id+'');
		}
	}

	function DisplayManuf() {
		
	var preowned=document.getElementById('chk_preownedcar');	
	    if(preowned.checked==true)
		{
			displayRow('preownedmanuf');
		}else{
			hideRow('preownedmanuf');
		}  
	}
	function PopulateExecutive(){
		branch_id= document.getElementById("dr_enquirybranch").value;
		showHint('../sales/enquiry-check.jsp?executive=exe&dr_branch='+branch_id, 'hint_exe');
	}
	
</script>

<script type="text/javascript">
		function SmartAddRow(smartarropt) {
		    count = document.getElementById("dr_searchcount").value;
		    if(count=="" || count==null) count="0";
		    count = parseInt(count);
		    count++;
		    document.getElementById("dr_searchcount").value = count;
		    var table = document.getElementById("tblsmartsearch");
		    var rowCount = table.rows.length;
		    var newRow = document.getElementById("tblsmartsearch").insertRow(rowCount );
		    ///add 4 cells (<td>) to the new row and set the innerHTML to contain text boxes
		    var oCell = newRow.insertCell(0);
		    oCell.innerHTML = "<select name=\""+count+"_dr_field\" id=\""+count+"_dr_field\" class=\"form-control\" onchange = SmartPopulateCriteria(this,"+count+");"+
		    "SmartPopulateParam1(this,"+count+")>"+smartarropt+"</select>";
		    oCell = newRow.insertCell(1); 
		    oCell.innerHTML = "<div id=dr"+count+">"+
		    "<select name=\""+count+"_dr_param\" id=\""+count+"_dr_param\" class=\"form-control\" onchange = \"SmartPopulateParam1(this,"+count+");SmartPopulateParam2(this,"+count+");\" >"+
		    "<option value=0-numeric>is equal to</option>"+
		    "<option value=1-numeric>is not equal to</option>"+
		    "<option value=2-numeric>is less than</option>"+
		    "<option value=3-numeric>is less than or equal to</option>"+
		    "<option value=4-numeric>is greater than</option>"+
		    "<option value=5-numeric>is greater than or equal to</option>"+
		    "<option value=6-numeric>is between</option>"+
		    "<option value=7-numeric>is not between</option>"
		    "</select>";
		    oCell = newRow.insertCell(2);
		    bool_txt="<input type='text' name="+count+"_txt_value_1 id="+count+"_txt_value_1 class=\"form-control\" value='"+ x +"' size=30/ onchange='GetValues("+count+");'>";
		    oCell.innerHTML = "<div id=booltxt"+count+">"+bool_txt;
		    oCell = newRow.insertCell(3);
		    oCell.innerHTML = "<select class=\"form-control\" name="+count+"_dr_filter id="+count+"_dr_filter >		    "+"<option value=and>AND</option>"+
		    "<option value=or>OR</option></select>";             
		    oCell = newRow.insertCell(4);
		    var str = "<div class=\"\"><center><div class=\"btn btn-group\"><i class=\"fa fa-minus-square\" style=\"font-size:30px\" id=\"sample_editable_1_new\" onclick=\'SmartRemoveRow(this);FormFocus();\' onmouseover=\"this.title='Remove Criteria'\"></i> <i class=\"fa fa-plus-square\" style=\"font-size:30px\" id=\"sample_editable_1_new\" onclick=\'LoadRows();\' onmouseover=\"this.title='Add Criteria'\"></i></div></center></div>";
		    oCell.innerHTML=str;
		    document.getElementById("dr_searchcount_var").value=count;
		}
	</script>
</body>
</HTML>
