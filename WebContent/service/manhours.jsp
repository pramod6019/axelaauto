<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Man_Hours" scope="request"/>
<%mybean.doGet(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
        <head>
        <title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
        <meta http-equiv="pragma" content="no-cache"/>
        <meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
        <%@include file="../Library/css.jsp"%>
	
</head>

<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>Man Hours</h1>
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
						<li><a href="../service/index.jsp">Service</a> &gt;</li>
						<li><a href="manhours.jsp">Man Hours</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
						<font color="#ff0000"><b><%=mybean.msg%></b></font>
					</center>
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Man Hours
									</div>
								</div>
								<div class="portlet-body portlet-empty container-fluid">
									<div class="tab-pane" id="">
										<!-- START PORTLET BODY -->
										<center>
											<font size="1">Form fields marked with a red asterisk
												<b><font color="#ff0000">*</font></b> are required.
											</font>
										</center><br>
										<form name="form1" method="post" class="form-horizontal">
											<div class="form-element6">
												<label > Branch<font color=red>*</font>: </label>
													<select name="dr_branch_id" class="form-control selectbox" id="dr_branch_id" onChange="PopulateBay();">
						                                <%=mybean.PopulateBranch()%>
						                              </select>
											</div>
											
											<div class="form-element6">
												<label >Bay<font color=red>*</font>: </label>
													<span id="bayHint">
						                              <select name="dr_baytrans_bay_id" class="form-control selectbox" id="dr_baytrans_bay_id">
						                              <%=mybean.PopulateBay()%>
						                          </select>
						                              </span>
											</div>
											
											<div class="form-element6">
												<label > Job Card Id<font color=red>*</font>: </label>
													<input type="text" name="txt_baytrans_jc_id" id="txt_baytrans_jc_id" class="form-control textbox"  value="" onKeyUp="toInteger('txt_jc_id');" maxlength="10" size="15"/>
                          							<input type="hidden" name="jc_id" id="jc_id" value="<%=mybean.baytrans_jc_id%>"/>
											</div>
											
											<div class="form-element6">
												<label > User Id<font color=red>*</font>: </label>
													<input type="text" name="txt_baytrans_emp_id" id="txt_baytrans_emp_id" class="form-control textbox" value="" onKeyUp="toInteger('txt_baytrans_emp_id');" maxlength="10" size="15"/>
											</div>

											<center>
											<div class="form-element6 form-center">
											<input name="start_button" type="submit" class="btn btn-success" id="start_button" value="START" />
											</div><div class="form-element6 form-center">
											<input name="Stop_button" type="submit" class="btn btn-success" id="Stop_button" value="STOP" />
											</div>
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
	
         <%@include file="../Library/admin-footer.jsp" %>
         <%@include file="../Library/js.jsp" %>
	<script language="JavaScript" type="text/javascript">
        
        function PopulateBay(){
			//alert("*******");
            var branch_id = document.getElementById('dr_branch_id').value;
                if(branch_id!="" && branch_id!="0")
				{
					 showHint('../service/report-check.jsp?bay_branch_id='+branch_id,'bayHint');
				}
				
		  }
		  
		function FormFocus() { //v1.0
		  document.form1.txt_baytrans_jc_id.focus()
		}
    </script>
        
</body>
</HTML>
