	
	<center>
		<font color="red"><b><%=mybean.msg%></b></font><br>
	</center>

<%
	if (mybean.smart.equals("")) {
%>
<%
	if (mybean.EnableSearch.equals("1")) {
%>
					<div class="container-fluid portlet box  ">
						<div class="portlet-title" style="text-align: center">
							<div class="caption" style="float: none">Search</div>
						</div>
						<div class="portlet-body container-fluid">
							<div class="tab-pane" id="">
								<form action="" method="get" name="frm1" class="form-horizontal">
									<div class="form-element6">
												<label class="">Start Date<font
													color=#ff0000><b>*</b></font>:</label>
													<input name="txt_starttime" id="txt_starttime"
														value="<%=mybean.start_time%>"
														class="form-control datepicker"
														type="text" value="" />
											</div>
											<div class="form-element6">
												<label class="">End Date<font
													color=#ff0000><b>*</b></font>:</label>
													<input name="txt_endtime" id="txt_endtime"
														value="<%=mybean.end_time%>"
														class="form-control datepicker"
														 type="text" value="" />
											</div>
								
<!-- 									<div class="form-group"> -->
<!-- 										<label class="control-label col-md-4"> Start Date<font -->
<!-- 											color=red>*</font>: -->
<!-- 										</label> -->
<!-- 										<div class="col-md-6 col-xs-12" id="emprows"> -->
<!-- 											<input name="txt_starttime" id="txt_starttime" type="text" -->
<!-- 												class="form-control" value="" -->
<!-- 												size="12" maxlength="10" /> -->
<!-- 										</div> -->
<!-- 									</div> -->

<!-- 									<div class="form-group"> -->
<!-- 										<label class="control-label col-md-4"> End Date<font -->
<!-- 											color=red>*</font>: -->
<!-- 										</label> -->
<!-- 										<div class="col-md-6 col-xs-12" id="emprows"> -->
<!-- 											<input name="txt_endtime" id="txt_endtime" type="text" -->
<%-- 												class="form-control" value="<%=mybean.end_time%>" size="12" --%>
<!-- 												maxlength="10" /> -->
<!-- 										</div> -->
<!-- 									</div> -->
									<div class="form-element12">
										<center>
											<input name="submit_button" type="submit"
												class="btn btn-success" id="submit_button" value="Go" />
												 <input type="hidden" name="submit_button" value="Submit">
										</center>
									</div>
								</form>
						</div>
						</div>
					</div>
					<%
						}
					%>





					
						<center>
							<%
								if (!mybean.StrHTML.equals("")) {
							%>
							<%=mybean.StrHTML%>
							<%
								} else {
							%>
							<font color="red"><strong>No records found!</strong></font>
							<%
								}
							%>
						</center>
					
					<%
						if (!mybean.StrHTML.equals("")) {
					%>
					<center>
					<%=mybean.ListLink%>
					</center>
					<%
						}
					%>
					<%
						}
					%>
					<%
						if ((mybean.smart.equals("yes") || !mybean.StrHTML.equals(""))
								&& mybean.ExportPerm.equals("1")) {
					%>

					<div>
						<div>Export</div>
						<form name="frmexport" method="get" target="_blank"
							class="form-horizontal" action="<%=export.exportpage%>">
							<div>

								<div>Report Type:&nbsp;</div>
								<div>
									<select name="report" class="selectbox" id="report">
										<%=export.PopulatePrintOption()%>
									</select>
								</div>
								<div>Export Format:&nbsp;</div>
								<div>
									<select name="exporttype" class="selectbox" id="exporttype">
										<%=mybean.PopulateExportFormat(mybean.PadQuotes(request
						.getParameter("exporttype")))%>
									</select>
								</div>
								<div align="center">
									<input name="btn_export" id="btn_export" type="submit"
										class="button" value="Export">
								</div>

							</div>
						</form>
					</div>
					<!-- #EndLibraryItem -->
					<%
						}
					%>
