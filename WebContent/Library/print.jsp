<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<form class="form-horizontal" name="frmprint" method="get"
	target="_blank" action="<%=print.printpage%>">
	
	<div class="form-group">
		<label class="control-label col-md-4"> Report Type : </label>
		<div class="col-md-6 col-xs-12" id="emprows">
			<select name="report" class="form-control" id="report" >
			<%=print.PopulatePrintOption()%>
		</select>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-md-4"> Print Format : </label>
		<div class="col-md-6 col-xs-12" id="emprows">
			<select name="printtype" class="form-control" id="printtype">
			<%=mybean.PopulatePrintFormat(mybean.PadQuotes(request.getParameter("printtype")))%>
		</select>
		</div>
	</div>
	<div class="form-group">
		<center>		
			<input name="btn_print" id="btn_print" type="submit"
				class="btn btn-success" value="Print">
		</center>

	</div>

</form>