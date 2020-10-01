<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.inventory.Inventory_Report_ABC" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
 <%@include file="../Library/css.jsp"%>

</HEAD>
<body onLoad="FormFocus()" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="../portal/header.jsp"%>
		<div class="page-container">
			<div class="page-content-wrapper">
				<div class="page-head">
					<div class="container-fluid">
						<div class="page-title">
							<h1>ABC Analysis Report</h1>
						</div>
					</div>
				</div>
				<div class="page-content">
				<div class="page-content-inner">
					<div class="container-fluid">
						<ul class="page-breadcrumb breadcrumb">
							<li><a href="../portal/home.jsp">Home</a> &gt;</li>
							<li><a href="../portal/mis.jsp">MIS</a> &gt;</li>
							<li><a href="inventory-report-abc.jsp">ABC Analysis</a><b>:</b></li>
						</ul>

							<div class="container-fluid">
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<form class="form-horizontal" method="post" name="frm1"
									id="frm1">
									<div class="portlet box">
										<div class="portlet-title" style="text-align: center">
											<div class="caption" style="float: none">ABC Analysis</div>
										</div>
										<div class="portlet-body portlet-empty">
											<div class="form-element3">
														<label>Branch<font color=red>*</font>: </label>
															<select name="dr_branch" id="dr_branch"
																class="form-control"
																onChange="showHint('../inventory/inventory-location-check.jsp?branch_id=' + GetReplace(this.value),'dr_location')">
																<%=mybean.PopulateBranch(mybean.branch_id, "all","","", request)%>
															</select>
															</div>
															<div class="form-element3">
														<label>Location: </label>
														<div id="dr_location">
															<select name="dr_location" class="form-control"
																id="dr_location" visible="true">
																<option value=0>Select</option>
																<%=mybean.PopulateLocation() %>
															</select>
															</div>
													</div>
										<div class="form-element3">
											<label>Start Date<font color=red>*</font>:
											</label> <input name="txt_starttime" id="txt_starttime" type="text"
												class="form-control datepicker"
												value="<%=mybean.start_time%>" size="12" maxlength="10" />
										 </div>
												<div class="form-element3">
													<label>End Date<font color=red>*</font>:
													</label> <input name="txt_endtime" id="txt_endtime" type="text"
														class="form-control datepicker"
														value="<%=mybean.end_time%>"
														size="12" maxlength="10" />
												</div>
												<div class="row"></div>
														<center>
															<input name="submit_button" type="submit"
																class="btn btn-success" id="submit_button" value="Go" />
															<input type="hidden" name="submit_button" value="Submit">
														</center>
											
											<div class="form-group">
												<div class="form-body">
													<div class="form-group">
														<center>
															<%=mybean.StrHTML%></center>
													</div>
												</div>
											</div>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
<%@include file="../Library/admin-footer.jsp" %>
 <%@include file="../Library/js.jsp"%>
</body>
</HTML>
