<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<form class="form-horizontal" name="frmexport" method="get" target="_blank" action="<%=export.exportpage%>">
	<input name="vouchertype_id" id="vouchertype_id" value='<%=export.vouchertype_id%>' hidden>
	<div class="form-element6" >
		<label> Report Type : </label>
		<select name="report" class="form-control" id="report">
			<%=export.PopulatePrintOption(mybean.PadQuotes(request.getParameter("vouchertype_id")))%>
		</select>
	</div>
	
	<div class="form-element6" >
		<label> Export Format : </label>
		<select name="exporttype" class="form-control" id="exporttype">
			<%=mybean.PopulateExportFormat(mybean.PadQuotes(request.getParameter("exporttype")))%>
		</select>
	</div>
	
	<div class="form-element12" >
		<center>
		<input name="btn_export" id="btn_export" type="submit" class="btn btn-success" value="Export">
		</center>
	</div>

</form>