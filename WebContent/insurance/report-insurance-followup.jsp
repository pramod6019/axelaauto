<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.insurance.Report_Insurance_Followup" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
	
	<style>
	@media (max-width: 320px)
	{
	#chk_pending_followup{
		
    margin-top: 6px;
    margin-left: -30px;
	}
	}
	</style>
<script language="JavaScript" type="text/javascript">
	 function ExeCheck() {
		 //alert("yudsd");
	var branch_id=document.getElementById("dr_branch").value;
	showHint('../service/report-check.jsp?insurfollowexecutive=yes&branch_id=' + GetReplace(branch_id),'exeHint');
    }
	</script>
</HEAD>
<body   class="page-container-bg-solid page-header-menu-fixed">
<%@include file="../portal/header.jsp" %>
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
						<h1>Insurance Enquiry Follow-up</h1>
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
						<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
						<li><a href="report-insurance-followup.jsp">Insurance Enquiry Follow-up</a><b>:</b></li>
						
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<!-- 	PORTLET -->
					<center><font color="red"><b> <%=mybean.msg%> </b></font></center>
	<div class="portlet box  ">
				<div class="portlet-title" style="text-align: center">
					<div class="caption" style="float: none">
						Insurance Enquiry Follow-up 
					</div>
				</div>
				<div class="portlet-body portlet-empty">
					<div class="tab-pane" id="">
					<form method="post" name="frm1"  id="frm1" class="form-horizontal">
						<!-- START PORTLET BODY -->
						<!-- 	PORTLET -->
	<!-- FORM START -->
	<div class="container-fluid">
		<div class="form-element3">
			<label> Executive: &nbsp;</label>
			<div>
			 <%=mybean.reportexe.PopulateDrInsuranceExecutives(mybean.exe_id, mybean.comp_id, mybean.ExeAccess)%>	
			</div>
		</div>



<!-- FORM START -->
<div class="form-element3">
<label>Start Date<font color=red>*</font>:&nbsp; </label>
<div>
	 <input name ="txt_starttime" id="txt_starttime" type="text" value="<%=mybean.start_time %>" size="12" maxlength="10" 
						class="form-control datepicker" type="text" />

</div>
</div>

<div class="form-element3">
<label> End Date<font color=red>*</font>:&nbsp; </label>
<div>
 <input name ="txt_endtime" id ="txt_endtime" type="text"  value="<%=mybean.end_time %>" size="12" 
 	maxlength="10" class="form-control datepicker"/>	
</div>
</div>


<div class="form-element3 form-element-margin">
<label>Pending Follow-up:&nbsp;</label>
 <input id="chk_pending_followup" name="chk_pending_followup" type="checkbox" <%=mybean.PopulateCheck(mybean.pending_followup)%> />	
</div>
</div>

<div class="row"></div>

<div class="form-element12" id="">
	<div id="" class="form-element2 form-element-padding">
	<%=mybean.PopulateInsurFollowupFields(mybean.comp_id, "0", 0) %></div>
    <div id="disprow1" class="form-element2 form-element-padding" hidden></div>
	<div id="disprow2" class="form-element2 form-element-padding" hidden></div>
	<div id="disprow3" class="form-element2 form-element-padding" hidden></div>
	<div id="disprow4" class="form-element2 form-element-padding" hidden></div>
	<div id="disprow5" class="form-element2 form-element-padding" hidden></div>
</div>

<!-- </div> -->
<div class="container-fluid">


<div class="form-element4">

</div>

<div class="form-element4"><br></br>
<div text-align="right">
<center><input name="submit_button" type="button" class="btn btn-success" id="submit_button" value="Go"
onclick="holdfollowupfields();" /></center>
</div>
</div>
</div>
</form>
					</div>
				</div>
				
			</div>
			<center> <div id="listfollowup"> </div> </center>
			</div>
				</div>
				</div>
			</div>
		</div>

	</div>
	<!-- END CONTAINER -->
<%@include file="../Library/js.jsp"%>
 <%@include file="../Library/admin-footer.jsp" %></body>
 <script language="JavaScript" type="text/javascript">
 function populatefolllowupfields(parentid_id, divid){
		var parentid = document.getElementById(parentid_id).value;
		$("#rowcount").val(divid);
		showHint('insurance-enquiry-dash-check.jsp?reportInsurFollowupFields=yes&parentid=' + parentid+'&divid='+divid.replace("disprow",""), divid );
	}
 
 function hidefields(divid){
		divid=divid.replace("dr_followup_disp","");
		for(i=divid;i<=5;i++){
			$('#disprow'+i).html('');
			$('#disprow'+i).hide();
		}
		
	}
 
 function holdfollowupfields(){
	var dr_emp_id = $("#dr_emp_id").val();
	var txt_starttime = $("#txt_starttime").val();
	var txt_endtime = $("#txt_endtime").val();
	var chk_pending_followup=document.getElementById('chk_pending_followup').checked;
	var dr_followup_disp1 = $("#dr_followup_disp1").val();
	var dr_followup_disp2 = $("#dr_followup_disp2").val();
	var dr_followup_disp3 = $("#dr_followup_disp3").val();
	var dr_followup_disp4 = $("#dr_followup_disp4").val();
	var dr_followup_disp5 = $("#dr_followup_disp5").val();
		
		showHint('insurance-enquiry-dash-check.jsp?reportInsurFollowup=yes&dr_emp_id='+dr_emp_id
				+'&dr_followup_disp1='+dr_followup_disp1
				+'&dr_followup_disp2='+dr_followup_disp2
				+'&dr_followup_disp3='+dr_followup_disp3
				+'&dr_followup_disp4='+dr_followup_disp4
				+'&dr_followup_disp5='+dr_followup_disp5
				+'&chk_pending_followup='+chk_pending_followup
				+'&txt_starttime='+txt_starttime
				+'&txt_endtime='+ txt_endtime, 'listfollowup');
		
	}
 
 </script>
 
 
 
</html>