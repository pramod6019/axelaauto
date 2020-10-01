 
	<center>
		<font color="red"><b><%=mybean.msg%></b></font>
	</center>
<%if(mybean.smart.equals("")){%>
<%if(mybean.EnableSearch.equals("1")){%>

<div class="container-fluid portlet box">
	<div class="portlet-title" style="text-align: center">
		<div class="caption" style="float: none">Search</div>
	</div>
	<div class="portlet-body">
		<div class="tab-pane" id="">
			<!-- START PORTLET BODY -->
			<div class="container-fluid">
			<form class="form-horizontal" name="frm1" role="form" method="get">
				<%if(mybean.branch_id.equals("0")){%>
				<div class="form-element4">
					<label> Branch: </label>
						<select name="dr_branch_id" id="dr_branch_id" class="form-control">
							<%=mybean.PopulateBranch(mybean.dr_branch_id,"all", "", request)%>
						</select>
				</div>
				<%}%>
				
				<div class="form-element4">
					<label>Start Date<font
						color=#ff0000><b>*</b></font>:</label>
						<input name="txt_starttime" id="txt_starttime"
							value="<%=mybean.start_time %>" class="form-control datepicker"
							 type="text" value="" />
				</div>
				<div class="form-element4">
					<label>End Date<font
						color=#ff0000><b>*</b></font>:</label>
						<input name="txt_endtime" id="txt_endtime"
							value="<%=mybean.end_time %>" class="form-control datepicker"
							 type="text" value="" />
				</div>
<center>
				<div class="form-element12 ">
				
						<input name="submit_button" type="submit" class="btn btn-success" id="submit_button" value="Go" />
							 <input type="hidden" name="submit_button" value="Submit"/>
							
				</div>
 </center>
			</form>
</div>

		</div>
	</div>
</div>
<%}%>
<div>
	<center>
		<%if(!mybean.StrHTML.equals("")){%>
		<%=mybean.StrHTML%>
		<%}else{%>
		<font color="red"><strong>No records found!</strong></font>
		<%}%>
	</center>
</div>
<%if(!mybean.StrHTML.equals("")){%>
<div>
	<center>
		<b><%=mybean.ListLink%></b>
	</center>
	<br>
</div>
<%}%>
<%}%>
<%if((mybean.smart.equals("yes") || !mybean.StrHTML.equals("")) && mybean.ExportPerm.equals("1")){%>
		<div class="container-fluid portlet box ">
			<div class="portlet-title" style="text-align: center">
				<div class="caption" style="float: none">Export</div>
			</div>
			<div class="portlet-body">
				<div class="tab-pane" id="">
				<div class="container-fluid">
					<!-- START PORTLET BODY -->
					<form class="form-horizontal" name="frmexport" method="get" target="_blank" action="<%=export.exportpage %>">
					 <div class="form-element6">
							<label>Report Type: </label>
								<select name="report" class="form-control" id="report"> <%=export.PopulatePrintOption() %> </select>
						</div>
						<div class="form-element6">
							<label> Export Format:</label>
								<select name="exporttype" class="form-control" id="exporttype">
									<%=mybean.PopulateExportFormat(mybean.PadQuotes(request.getParameter("exporttype"))) %>
								</select>

						</div>
						<center>
						<div class="form-element12">
								<input name="btn_export" id="btn_export" type="submit" class="btn btn-success" value="Export">
						</div>
						</center>
					</form>
				</div>
				</div>
			</div>
		</div>
		 <!-- #EndLibraryItem --> <%}%> 