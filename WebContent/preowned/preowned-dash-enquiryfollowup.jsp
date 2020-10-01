<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean"
	class="axela.preowned.Preowned_Dash_EnquiryFollowup" scope="request" />
<%
	mybean.doPost(request, response);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%@include file="../Library/css.jsp"%>

</HEAD>
<body leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
		
			<%@ include file="../Library/preowned-dash.jsp"%>
		
			<div class="page-content">
				<div class="container-fluid">
					<div class="portlet box">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Enquiry Follow-Up</div>
						</div>
						<div class="portlet-body portlet-empty">
							<!-- <div class="tab-pane" id=""> -->
							<!-- START PORTLET BODY -->
							<div class="container-fluid">
								<!-- <div class="col-md-12 col-xs-12"> -->
								<%=mybean.followupHTML%>
								<!--   </div> -->
							</div>
							<!-- 	</div> -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			// 	alert("library");
			$(".page-content").css({
				'min-height' : '20px'
			})
		});
	</script>
</body>
</HTML>
