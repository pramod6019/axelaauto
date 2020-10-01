<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Veh_Search" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">

<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp" %>
</HEAD>
		<body  onLoad="FormFocus()" 
		class="page-container-bg-solid page-header-menu-fixed">
		
        <%@include file="../portal/header.jsp" %>
        <div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
		<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Search Vehicles </h1>
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
						<li><a href=../service/index.jsp>Service</a> &gt;</li>
						<li><a href=../service/jobcard.jsp>Job Card</a> &gt;</li>
						<li><a href=jobcard-veh-search.jsp>Search Vehicles</a>:</li>
						<li style="float:right"> <a href=vehicle-update.jsp?add=yes>Add New Vehicle...</a>: </li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
									Search Vehicles
									</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<form name="form1" method="post" class="form-horizontal">
										<div class="form-element6 form-element-center">
												<label > Search<font color=red>*</font>:&nbsp; </label>
													<input name ="txt_search" type="txt_search" class="form-control" id="txt_search" 
													value="<%=mybean.txt_search%>" size="40" maxlength="255" 
													onKeyUp="VehicleCheck('txt_search',this,'hint_txt_search');"/>
											</div>

											<center>
												<input name="search_button" type="button" class="btn btn-success" id="search_button" value="Search" 
												onClick="ContactCheck('search_button',this,'hint_txt_search');"/>
											<div class="hint" id="hint_txt_search"> Enter your search parameter! </div>
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
		</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	
    <script language="JavaScript" type="text/javascript">	
	function FormFocus()
        { 
	  document.form1.txt_search.focus();
          
	}
	
	function VehicleCheck(name,obj,hint)
          {
           // alert("hiii");           
			var value = document.getElementById("txt_search").value;
            var url = "../service/report-check.jsp?";
	        var param="&q="+ value+"&veh_search=yes";
             var str = "123";  
			 showHint(url+param, hint);
           //  setTimeout('RefreshHistory()', 1000);
        }
						
		</script>
</body>
</HTML>
