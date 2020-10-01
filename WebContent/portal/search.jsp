<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean1" class="axela.portal.Search" scope="request" />
<%
	mybean1.doPost(request, response);
%>
<jsp:setProperty name="mybean1" property="*" />

<form name="myForm" id="uni_search" method="post" target="_parent" action="../portal/search.jsp">
	<div class="container-fluid portlet box"
		style="margin-bottom: 0px; margin-right: 0px;">
		<div class="portlet-title" style="text-align: center; float: none;">
			<div class="caption" style="float: none;">
				<center>
					<b>Search</b>
				</center>
			</div>
		</div>
		<div class="portlet-body portlet-empty">
			<div class="tab-pane" id="news">


				<div class="container-fluid">
<!-- 						<center> -->
						<div class="col-md-12">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="form-group">
									<input name="txt_search" type="text" class="form-control"
										id="txt_search" value="<%=mybean1.search%>" size="30"
										maxlength="255" placeholder="Enter Search Text" required=required />
										<span id="error_msg" style="display:none; float:left"><font color=red>Enter Search Text.</font></span>
								</div>
							</div>
							</div>
							
							
							<div class="container-fluid">
									<div class="col-md-5 col-sm-5 col-xs-12">
									<label class="control-label col-md-2 col-sm-2 col-xs-2" style="float: left;  vertical-align:middle; margin-top: 5px;">For</label>
								<div class="form-group col-md-10 col-sm-10 col-xs-10">
									<select name="dr_module_id" class="form-control"
										id="dr_module_id">
										<%=mybean1.PopulateModule()%>
									</select>
									</div>
									
									</div>
									<div class="col-md-5 col-sm-5 col-xs-12">
									<label class="control-label col-md-2 col-sm-2 col-xs-2" style="float: left;  vertical-align:middle; margin-top: 5px; margin-left: 5px;">By</label>
								<div class="form-group col-md-8 col-sm-8 col-xs-10">
									<select name="dr_module_type" class="form-control"
										id="dr_module_type">
										<%=mybean1.PopulateModuleType()%>
									</select>
								</div>
									
									
									</div>
									<div class="col-md-2 col-sm-2 col-xs-12">
									<center>
							<input name="go" id="go" type="submit" class="btn btn-success"
								value="Go" onsubmit="submit_form();" style="margin-top: 0px;" />
							<input type="hidden" name="btn_go" value="Go" />
						</center>
									
									
									</div>
							
							</div>
				</div>			
						
						
						
							
					</div>
					
				</div>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript">


function submit_form() {
document.getElementById("uni_search").submit();
}

var form = document.getElementById('uni_search'); // form has to have ID: <form id="formID">
form.noValidate = true;
form.addEventListener('submit', function(event) { // listen for form submitting
        if (!event.target.checkValidity()) {
            event.preventDefault(); // dismiss the default functionality
            document.getElementById('error_msg').style.display = "block";
//             alert('Please, fill the form'); // error message
        }
    }, false);
</script>
