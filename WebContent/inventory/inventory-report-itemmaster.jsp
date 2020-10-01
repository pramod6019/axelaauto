<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Report_ItemMaster" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body bgColor="#ffffff" leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
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
						<h1>Inventory Item Master</h1>
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
						<li><a href="index.jsp">Inventory</a> &gt;</li>
						<li><a href="../inventory/inventory-report-itemmaster.jsp">Item Master</a><b>:</b></li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					
					
					<div class="tab-pane" id="">
<!-- 					BODY START -->

							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none"><b>Inventory Item Master</b></div>
								</div>
								<div class="portlet-body portlet-empty">
									<div class="container-fluid">
										<!-- START PORTLET BODY -->
                                               <form name="formemp" class="form-horizontal" method="post">
											<div class="form-element6 form-element-center">
												<label> Category<font color=red>*</font>:
												</label> <select name="dr_cat_id" class="form-control"
													id="dr_cat_id">
													<option value=0>Select Category</option>
													<%=mybean.PopulateCategoryPop(mybean.cat_id, mybean.comp_id, "0", "1", request)%>
												</select>
											</div>
											 <center>
          <input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
                    <input type="hidden" name="submit_button" value="Submit"/>
                    </center>
										</form>
									</div>
								</div>
							</div>
                              <%=mybean.StrHTML%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
 <%@include file="../Library/admin-footer.jsp" %>
<%@include file="../Library/js.jsp"%>
                <script language="JavaScript" type="text/javascript">
                function PopulateItemPrice() { //v1.0
                document.formemp.submit();
            }
               </script>
 </body>
</html>
