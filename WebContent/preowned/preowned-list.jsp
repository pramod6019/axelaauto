<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.preowned.Preowned_List" scope="request" />
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<%@ include file="../Library/css.jsp"%>


</HEAD>
<body <%if(mybean.advSearch.equals(null)  || mybean.advSearch.equals("")){%> onLoad="LoadRows();FormFocus();" <%}%>
	class="page-container-bg-solid page-header-menu-fixed">
	<%//if(!mybean.pop.equals("yes")){%>
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
						<h1>List Pre-Owned</h1>
					</div>
				</div>
			</div>
			<div class="page-content">
				<div class="page-content-inner">
					<%@include file="../Library/list-body-adv.jsp"%>
				</div>
			</div>
		</div>
	</div>

	<!-- END CONTAINER -->

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@ include file="../Library/js.jsp"%>
	<script src="../Library/smart.js" type="text/javascript"></script> 


	<script type="text/javascript">
		function populatefollowup(preowned_id){
			if(document.getElementById("followup_"+preowned_id).innerHTML==""){
				showHint('../preowned/preowned-followup.jsp?preowned_id='+preowned_id,  'followup_'+preowned_id+'');
			}
		}
	</script>
</body>
</HTML>
