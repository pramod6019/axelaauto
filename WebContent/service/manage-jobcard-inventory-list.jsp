<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.Manage_JobCard_Inventory_List" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html>
<html>
<head>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
 <%@include file="../Library/css.jsp"%>
</head>
<body class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>List Job Card Inventory</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<div class="page-content-inner">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/manager.jsp">Business Manager</a> &gt;</li>
							<li><a href="manage-jobcard-inventory-list.jsp">List Job Card Inventory</a><b>:</b></li>
						</ul>
					
					<div class="tab-pane" id="">
					    <div align="right"><font color="#ff0000" ><%=mybean.LinkAddPage%></font></div>
					  
					  	<center><font color="#ff0000" ><b><%=mybean.msg%></b></font><br></center>
					  
					 		 <div class="portlet box">
								<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Job Card Inventory List</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
									    <form name="form1"  class="form-horizontal" method="post">
									        <div class="form-body">
									           <div class="form-element6 form-element-center">
									           	 <label>Inventory Model<font color="#ff0000">*</font>:</label>
									              <select name="dr_model_id" class="selectbox form-control" id="dr_model_id" onChange="document.form1.submit()" >
									                <%=mybean.PopulateModel()%>
									              </select>
									            </div>
									          
									          <% if(!mybean.invent_model_id.equals("0") && !mybean.invent_model_id.equals("")) {%>
									          <center><%=mybean.StrHTML%></center>
									          <% } %>
									       </div>
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

 <%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp" %>
	<script type="text/javascript" src="../Library/smart.js"></script>

</body>
</html>
