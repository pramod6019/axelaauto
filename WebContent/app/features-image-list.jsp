<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.app.Features_Image_List" scope="request"/>
<%mybean.doGet(request, response);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../assets/css/font-awesome.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/bootstrap.css" rel="stylesheet"
	type="text/css" />
<link href="../assets/css/components-rounded.css" rel="stylesheet"
	id="style_components" type="text/css" />
<link rel="shortcut icon" href="../test/favicon.ico" />
<!-- <link href="../assets/css/multi-select.css" rel="stylesheet" -->
<!-- 	type="text/css" /> -->
	<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />
<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />

    </HEAD>
    <body   class="page-container-bg-solid page-header-menu-fixed">
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
						<h1>List Images
						</h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
				<div class="container-fluid" >
					<ul class="page-breadcrumb breadcrumb">
					<li><a href="../portal/home.jsp">Home</a> &gt;</li>
					<li><a href="../portal/features-index.jsp">Feature</a> &gt;</li> 
					<li> <a href="features.jsp">Features</a> &gt;</li>
					<li> <a href="features-list.jsp?all=yes"> List Features</a> &gt;</li>
					<li> <a href="features-list.jsp?feature_id=<%=mybean.feature_id%>"><%=mybean.feature_name%> </a> </li>
					<li> <a href="features-image-list.jsp?feature_id=<%=mybean.feature_id%>">List Images</a> : </li>
					</ul>
                           
                                <div width="30%" align="right"><a href="features-image.jsp?add=yes&amp;feature_id=<%=mybean.feature_id%>">Add
                                        New Image...</a></div>
                            

                    <center><strong><%=mybean.RecCountDisplay%></strong></center>
                           <center><%=mybean.PageNaviStr%></center>
                         <center><%=mybean.StrHTML%></center>
                              <center><%=mybean.PageNaviStr%></center>
                            
          </div>
          </div>
          </div>
          </div>
          
        
        <%@ include file="../Library/admin-footer.jsp" %>
        
         <script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/dynacheck.js"></script>
    </body>
</HTML>
