<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<form name="form1" id="form1" method="get" class="form-horizontal">
	<%
		StringBuilder Str = new StringBuilder();
		for (int j = 0; j < mybean.smartarr.length; j++) {
			Str.append("<option value=").append(j).append("-")
					.append(mybean.smartarr[j][1]).append(">")
					.append(mybean.smartarr[j][0]).append("</option>/n");
		}
	%>
	<script language="JavaScript" type="text/javascript">
function LoadRows() {
SmartAddRow('<%=Str%>');
		}
	</script>
	<%
		if (request.getParameter("group") == null) {
	%>
	<div class="container-fluid">
		<%=mybean.LinkHeader%>&nbsp;<br>
	</div>
	<%
		}
	%>
	
	
	<div class="container-fluid">
	<table class="table table-bordered table-striped table-condensed">
			<thead class="bg-white">
				<tr>
					<td style="position: relative; vertical-align:middle; width:33%"><center>
							<b><a href="#" id="search"><i class="fa fa-search" style="padding:0px"
									id="button"></i><span id="icons"><i class="fa fa-plus" id="plus"></i><i class="fa fa-minus" id="minus" style="display:none"></i></span></a></b>
							</center>
					</td>										
					<td align=center style="vertical-align:middle; width:33%">
				<%
					String searchcount = "0";
					if (request.getParameter("dr_searchcount") != null)
						searchcount = mybean.CNumeric(request
								.getParameter("dr_searchcount"));
				%>
				<input type="hidden" value="<%=searchcount%>" name="dr_searchcount"
					id="dr_searchcount" /> <input type="hidden"
					value="<%=searchcount%>" name="dr_searchcount_var"
					id="dr_searchcount_var" />
				<%
					if (request.getParameter("group") != null) {
				%>
				 <input type="hidden"
					value="<%=request.getParameter("group")%>" name="group" id="group" />
				<%
					}
				%>
				<%
					if (request.getParameter("group") == null) {
				%>
				<b><%=mybean.LinkAddPage%></b>
				</td>
				<%
					if (!mybean.StrHTML.equals("") && mybean.ExportPerm.equals("1")) {
				%>
				<td align=center style="width:33%; vertical-align:middle"> <input name="PrintButton" type="button"
					class="btn btn-success" id="PrintButton" value="Export" style="margin-top: calc; vertical-align:middle"
					onClick="remote=window.open('<%=mybean.LinkExportPage%>','print','');remote.focus();">
					<% if (!mybean.LinkPrintPage.equals("")) { %>
					<input name="PrintButton" type="button"
					class="btn btn-success" id="PrintButton" value="Print" style="margin-top: calc; vertical-align:middle"
					onClick="remote=window.open('<%=mybean.LinkPrintPage%>','print','');remote.focus();">
					<%} %>
				</td>
				<%
					}
				%>
				<%
					}
				%>
					
				</tr>
			</thead>
		</table>
	<div class="portlet box " id="open" style="position:relative; display:none" >
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">
							Search <a style="color: white"
								href="javascript:SmartAddRow('<%=Str%>');BootstrapSelect();"><i class="fa fa-plus-square"></i></a>
						</div>
					</div>
					<div class="portlet-body portlet-empty">
						<div class="tab-pane" id="">
							<!-- START PORTLET BODY -->

							<table class="table table-responsive" id="tblsmartsearch" name="tblsmartsearch">
								<%=mybean.SmartSearch.PopulateSmartSearch(mybean.smartarr, request)%>
							</table>
						<div class="form-group">
							<%=mybean.advhtml%>
						</div>
						<div class="form-group">
							<center>
								<input type="submit" value="Search" name="advsearch_button"
									class="btn btn-success" onClick="onPress()" />
							</center>
						</div>
							
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
	<div class="container-fluid" style="max-width: 100%; ">
		<center>
			<font color="#ff0000"><b><%=mybean.msg%></b></font><br>
		</center>
	</div>
	<div class="container-fluid">
				<div>
					<center>
						<strong><%=mybean.RecCountDisplay%></strong>
					</center>
				</div>
				<div>
					<center><%=mybean.PageNaviStr%><center>
				</div>
				<div><%=mybean.StrHTML%></div>
				<div>
					<center><%=mybean.PageNaviStr%></center>
				</div>
	</div>
</form>
 <scr