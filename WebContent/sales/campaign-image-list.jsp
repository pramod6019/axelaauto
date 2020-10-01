<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.sales.Campaign_Image_List" scope="request"/>
<%mybean.doPost(request, response);%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"/>
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.ClientName%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<%@include file="../Library/css.jsp"%>
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
						<h1>List Images</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="../sales/index.jsp">Sales</a> &gt;</li>
						<li><a href=campaign.jsp>Campaign</a> &gt;</li>
						<li><a href="campaign-list.jsp?all=yes">List Campaign</a> &gt;</li>
						 <li><a href="campaign-list.jsp?campaign_id=<%=mybean.img_campaign_id%>"><%=mybean.campaign_name%></a> &gt;</li>
						 <li> <a href="campaign-image-list.jsp?campaign_id=<%=mybean.img_campaign_id%>">List Images</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<center>
					
					</center>
					<div class="page-content-inner">
					<div class="tab-pane" id="">
<!-- 					BODY START -->
					<div style="float: right">
					<a href="campaign-image-update.jsp?add=yes&amp;campaign_id=<%=mybean.img_campaign_id%>" >Add New Image...</a>
					</div>
						 <br></br>
						 <div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">List Images</div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
										<center>
											<font color="#ff0000"><b> <%=mybean.msg%>
											</b></font>
										</center>
										<center>
											<strong><%=mybean.RecCountDisplay%></strong>
										</center>
										<center><%=mybean.PageNaviStr%></center>
										<center><%=mybean.StrHTML%></center>
										<center><%=mybean.PageNaviStr%></center>

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
</body>
</HTML>
	<!-- END CONTAINER -->
