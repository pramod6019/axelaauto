<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
	<div class="container-fluid">
<form name="form1" id="form1" method="get">
	<%
		StringBuilder Str = new StringBuilder();
		for (int j = 0; j < mybean.smartarr.length; j++) {
			Str.append("<option value=").append(j).append("-")
					.append(mybean.smartarr[j][1]).append(">")
					.append(mybean.smartarr[j][0]).append("</option>");
		}
	%>


<script language="Javascript" type="text/javascript">
function LoadRows() {
	 SmartAddRow('<%=Str%>');
		}
</script>

	<%
		if (request.getParameter("group") == null) {
	%>

	<div class="container-fluid">
		<ul class="page-breadcrumb breadcrumb">
			<%=mybean.LinkHeader%>
		</ul>
	</div>
	<%
		}
	%>

	<div class="container-fluid">
		
		<div class="container-fluid" style="max-width: 100%;">
		<table class="table table-bordered table-striped table-condensed">
			<thead class="bg-white">
				<tr>
					<td style="position: relative; vertical-align:middle; width: 33%;"><center>
							<b><a href="#" id="search"><i class="fa fa-search" style="padding:0px"
									id="button"></i><span id="icons"><i class="fa fa-plus " id="plus"></i><i class="fa fa-minus" id="minus" style="display:none"></i></span></a></b>
							</center>
					</td>
					<td style="position: relative; vertical-align:middle; width: 33%;" align=left>
					
					<%
						if (request.getParameter("group") == null) {
					%>
						
						<div id="linkcenter">
							<b><%=mybean.LinkAddPage%></b>
							</div>
							
					</td>
							<%
								if (!mybean.StrHTML.equals("") && mybean.ExportPerm.equals("1")) {
							%>
					<td align=center style="vertical-align:middle; width: 33%;">
							
							
							 <input name="PrintButton" type="button"
								class="btn btn-success" id="PrintButton" value="Export"
								onClick="remote=window.open('<%=mybean.LinkExportPage%>','print','');remote.focus();">
						</td>	
							<%
								}else{%>
									<td align=center style="vertical-align:middle; width: 33%;">
									</td>	
								<%}%>
					<% } %>
				</tr>
			</thead>
		</table>
				<div class="portlet box" id="open" style="display:none">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							Search <a style="color: white" href="javascript:SmartAddRow('<%=Str%>');BootstrapSelect();"><i class="fa fa-plus-square"></i></a>
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<!-- START PORTLET BODY -->
							<form class="form-horizontal" role="form" name="form1" id="form1"
								method="get">
								<div class="form-group" style="margin-bottom: 1px;">

									<table class="table table-responsive" id="tblsmartsearch"
										name="tblsmartsearch" style="margin-bottom: -10px;">

										<%=mybean.SmartSearch.PopulateSmartSearch(mybean.smartarr, request)%>

									</table>
								</div>
								<div class="form-group">
										<center>
<%-- 											<!--<input type="button" value="Add Criteria"  class="button" onClick="SmartAddRow('<%=Str%>');" />&nbsp;&nbsp;--> --%>
											<input type="submit" value="Search" name="advsearch_button"
												class="btn btn-success" onClick="onPress()" />
										</center>
								</div>
								<div class="form-group">
									<center>

										<%
											String searchcount = "0";
											if (request.getParameter("dr_searchcount") != null)
												searchcount = mybean.CNumeric(request
														.getParameter("dr_searchcount"));
										%>
										<input type="hidden" value="<%=searchcount%>"
											name="dr_searchcount" id="dr_searchcount" /> <input
											type="hidden" value="<%=searchcount%>"
											name="dr_searchcount_var" id="dr_searchcount_var" />
										<%
											if (request.getParameter("group") != null) {
										%>
										<input type="hidden"
											value="<%=request.getParameter("group")%>" name="group"
											id="group" />
										<%
											}
										%>
									</center>

								</div>
							</form>
						</div>
					</div>
				</div>
				
		</div>
	</div>
	<script>
	function onPress() {
		localStorage.portlet_value = 3;
	}
	</script>
	<center>
			<font color="#ff0000"><b><%=mybean.msg%></b></font>
		</center>
	<div class="container-fluid">
<!-- 		<div class="container-fluid portlet box"> -->
<!-- 			<div class="portlet-title" style="text-align: center"> -->
<!-- 				<div class="caption" style="float: none"></div> -->
<!-- 			</div> -->
<!-- 			<div class="portlet-body"> -->
				<div class="tab-pane" id="">
					<center>
						<strong><%=mybean.RecCountDisplay%></strong>
					</center>
					<!-- START PORTLET BODY -->
				
					<center>
						<%=mybean.PageNaviStr%>
					</center>
				
					<center>
						<%=mybean.StrHTML%>
					</center>
					
					<center>
						<%=mybean.PageNaviStr%>
					</center>
				</div>
<!-- 			</div> -->
<!-- 		</div> -->
	</div>

</form>
</div>
	 