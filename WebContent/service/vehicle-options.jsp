<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.service.Vehicle_Options"
	scope="request" />
<%
	mybean.doGet(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="../Library/css.jsp"%>
</HEAD>
<body class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Vehicle Option</h1>
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
						<li><a href=../service/index.jsp>Service</a> &gt;</li>
						<li><a href="../service/vehicle.jsp">Vehicles</a> &gt;</li>
						<li><a href="vehicle-list.jsp?all=yes">List Vehicle</a> &gt;</li>
						<li><a
							href="vehicle-list.jsp?veh_id=<%=mybean.vehtrans_veh_id%>">Vehicle
								ID: <%=mybean.vehtrans_veh_id%></a> &gt;</li>
						<li><a
							href="vehicle-options.jsp?veh_id=<%=mybean.vehtrans_veh_id%>">Vehicle
								Option</a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->
					<div class="page-content-inner">
						<div class="tab-pane" id="">
							<!-- 					BODY START -->
							<div class="portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Options for
										<%=mybean.itemmaster_name%>
										<input type="hidden" id="txt_itemmaster_id"
											name="txt_itemmaster_id" value="<%=mybean.vehtrans_veh_id%>" />
									</div>
								</div>

								<div class="portlet-body portlet-empty">
									<div class="tab-pane" id="">
										<form name="form1" method="post"
											action="vehicle-options.jsp?add=yes&veh_id=<%=mybean.vehtrans_veh_id%>"
											class="form-horizontal">
											<br>
											<div class="form-element6 form-element-center">
												<label > Search<font color=red>*</font>:
												</label>
													<input name="txt_search" type="text" class="form-control"
														id="txt_search" value="" size="30" maxlength="255"
														onKeyUp="ItemSearch();" />
											</div>
											<!-- <center>
												<input name="search_button" type="button" class="btn btn-success" id="search_button" value="Search" 
												onClick="ContactCheck('search_button',this,'hint_txt_search');"/>
											</center> -->
											<center>
												<div class="hint" id="hint_search_item">Enter your
													search parameter!</div>
											</center>

											<%=mybean.StrHTML%>

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
	</div>

	</div>
	<%@include file="../Library/admin-footer.jsp"%>
<%@include file="../Library/js.jsp"%>
	<script type="text/javascript">
    var value = document.getElementById("txt_search").value;
    var url = "vehicle-item-check.jsp?";
    var veh_id = <%=mybean.vehtrans_veh_id%>;
    var param="q="+value+"&veh_id="+veh_id;
			var str = "123";
			showHint(url + param, 'hint_search_item');
		}
		function AddOptionItem() {
			document.form1.submit();
		}
	</script>

</body>
</HTML>
