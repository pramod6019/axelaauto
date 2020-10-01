<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Activity" scope="request" />
<% mybean.doPost(request, response); %>
<!DOCTYPE html>
<html lang="en">
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1" name="viewport" />

<%@include file="../Library/css.jsp" %>
<link href='../Library/fullcalendar.css' rel='stylesheet' type='text/css' />
<link href='../Library/fullcalendar.print.css' media='print' rel='stylesheet' type='text/css' />
<link href="../assets/css/bootstrap-calendar.css" rel="stylesheet" type="text/css" />
<link href='../Library/jquery.qtip.css' rel='stylesheet' type='text/css'/>

<style type='text/css'>
#calendar {
	margin: 0 10px;
}
table {
    border-collapse: collapse;
}
.table-condensed tbody tr:last-child
{
	border-bottom: 1px solid black;
}
.tooltip{
background-color: black;
}
</style>
</HEAD>

<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>

	<div class="page-container">
		<div class="page-content-wrapper">
			<font color="#ff0000"><b><%=mybean.msg%></b></font>
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Activity</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<!-- BEGIN PAGE BREADCRUMBS -->
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="home.jsp">Home</a>&gt;</li>
						<li><a href="activity.jsp">Activity</a><b>:</b></li> 
					</ul>
					<!-- END PAGE BREADCRUMBS -->
				</div>
				<form name="act" method="post">
					<div class="container-fluid">
						
							<div class="form-element12 form-element">
								<div class="form-element4 form-element">
									<div class="container-fluid portlet box" style="height: 695px;">
										<div class="portlet-title">
											<div class="caption"></div>
										</div>
										<div class="portlet-body portlet-empty" style="height: 650px;">
											<div class="tab-pane" id="news">
												<div class="form-element6">
													<label>Branch:</label>
													<select name="dr_branch_id" id="dr_branch_id"
														class="dropdown form-control" onchange="PopulateTeam();PopulateExecutive();">
														<%=mybean.PopulateBranches(mybean.branch_id, mybean.comp_id)%>
													</select>
												</div>
												<div class="form-element6">
													<label>Team:</label>
													<span id="hintTeam">
															<%=mybean.PopulateTeam(mybean.branch_id, mybean.team_id, mybean.comp_id)%>
													</span>
												</div>
												
												<div class="form-element12">
												<label>Executive:</label>
												<span id="teamexe">
														<%=mybean.PopulateExe(mybean.branch_id, mybean.team_id)%>
												</span>
												</div>
												
												<div class="form-element12">
													<!-- 	<center> -->
													<input type="hidden" name="txt_d" id="txt_d" value="<%=mybean.d%>"><br>
													<center>
														<div class="date-picker" data-date-format="dd/mm/yyyy"> </div>
													</center>
													<br>
													<center>
														<a href="activity.jsp">Today</a>&nbsp;is&nbsp;<%=mybean.TodayDatePicker%>
														<br/>
														<a href="../portal/activity-update.jsp?add=yes"><b>Add New Activity</b></a>
													</center>
												</div>
											</div>
										</div>
									</div>
								</div>


								<div class="form-element8">
									<div class="portlet light portlet-fit calendar">
										<div class="portlet-title">
											<div class="caption">
												<i class=" icon-layers font"></i>
												<span class="caption-subject font sbold uppercase">Calendar</span>
											</div>
										</div>
										<div class="portlet-body">
											<div class="row">
												<div id="calendar" class="has-toolbar"></div>
											</div>
										</div>
									</div>
								</div>

							</div>
						</div>
					</div>

				</form>
			</div>
		</div>


	</div>
	<%@ include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp" %>
	
<script type="text/javascript" src='../assets/js/bootstrap-calendar.js'></script>
<script src="../assets/js/calendar.js" type="text/javascript"></script>
<script type='text/javascript' src='../Library/fullcalendar.min.js'></script>
<script type='text/javascript' src='../Library/jquery.qtip.js'></script>
	<script>
$(".date-picker").click(function(){
	var daydate = $(".active").html();
	var dateText = $(".active").text();

	if(daydate.length<2){
		var date = dateText.substr(0,1);
		var month = dateText.substr(1,3);
		var year = dateText.substr(4,7)
	}else{
		var date = dateText.substr(0,2);
		var month = dateText.substr(2,3);
		var year = dateText.substr(5,8)
	}
	
	 switch(month)
	{
	case 'Jan':	month='01';break;
	case 'Feb':	month='02';break;
	case 'Mar':	month='03';break;
	case 'Apr':	month='04';break;
	case 'May':	month='05';break;
	case 'Jun':	month='06';break;
	case 'Jul': month='07';break;
	case 'Aug':	month='08';break;
	case 'Sep':	month='09';break;
	case 'Oct':	month='10';break;
	case 'Nov':	month='11';break;
	case 'Dec':	month='12';break;
	default:break;
	} 
	var newDate = month+"/"+date+"/"+year;
// 	alert("\ndateText=="+newDatee);
 		var d = new Date(newDate);
		$('#calendar').fullCalendar('gotoDate', d); 
});

function PopulateExecutive() {	
    var branch_id = document.getElementById('dr_branch_id').value;	
	var team_id = document.getElementById('dr_team').value;
// 	alert(team_id);
	showHint('../portal/activity-check.jsp?executive=yes&dr_team=' + team_id+'&dr_branch_id='+branch_id,'teamexe');
}

function PopulateTeam() {	
 var branch_id = document.getElementById('dr_branch_id').value;	
	//alert(team_id);
	showHint('../portal/activity-check.jsp?team=yes&dr_branch_id='+branch_id,'hintTeam');
}
</script> 
	
</body>

</html>