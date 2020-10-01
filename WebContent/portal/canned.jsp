<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Canned"
	scope="request" />
<% mybean.doPost(request, response); %>
<style>
@media ( min-width : 992px) {
	.control-label {
		text-align: right;
	}
}

.modelstyle {
	height: 450px;
}

.modal-dialog{
	overflow-y: initial;
}

.modal-body{
	height: 400px;
	overflow-y: auto;
}


</style>
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">&times;</button>
			<h4 class="modal-title">
				<center> <b>Canned Messages</b> </center>
			</h4>
		</div>

		<div class="modal-body">
			
			<center>
				<div><span id="sentmsg"></span></div>
				<div><font color="#ff0000"><b><%=mybean.msg%></b></font></div>
			</center>
			<br>
			<div id="dialog-modal"></div>
			<div class="container-fluid">
			<% if (mybean.home.equals("yes")) { %>
			<div class="row">
					<div class="col-md-1 text-right">
							<label>Brand: </label>
					</div>
					<div class="col-md-3" style="margin-top: -20px;">
					<select name="dr_brand" id="dr_brand" class="form-element"
						onchange="PopulateCannedEmail();PopulateCannedSMS();">
						<%=mybean.PopulatePrincipal( mybean.brand_id, mybean.comp_id, request)%>
					</select>
					</div>
					
					<div class="col-md-1 text-right">
							<label>Type: </label>
					</div>
					<div class="col-md-3" style="margin-top: -20px;">
					<select name="dr_branchtype" id="dr_branchtype" class="form-element"
						onchange="PopulateCannedEmail();PopulateCannedSMS();">
						<%=mybean.PopulateBranchType(mybean.branchtype_id, request)%>
					</select>
					</div>
					
<!-- 					<div class="col-md-2 text-right"> -->
<!-- 							<label> Search: </label> -->
<!-- 					</div> -->
					<div class="col-md-3" style="margin-top: -10px;">
						<input name="txt_search" type="txt_search" maxlength="255"
						class="form-control" id="txt_search" 
						value="<%=mybean.txt_search%>"
						onKeyUp="getEmail('txt_search',this,'listEmail');getSMS('txt_search',this,'listSMS');"/>
					</div>
				</div>	
			<div class="row"></div>			
			<%} %>
			<div class="col-md-6">
						<div class="portlet box" style="height: auto;">
							<div class="portlet-title" style="text-align: center">
								<div class="caption" style="float: none">Email Messages</div>
							</div>

							<div class="portlet-body portlet-empty" id="listEmail">
									<%=mybean.StrHTML %>
							</div>
						</div>
			</div>
				
			<div class="col-md-6">
				<div class="portlet box" style="height: auto;">
					<div class="portlet-title" style="text-align: center">
						<div class="caption" style="float: none">SMS Messages</div>
					</div>

					<div class="portlet-body portlet-empty" id="listSMS">
						<%=mybean.StrHTML1 %>
					</div>
				</div>
				</div>
			</div>
			
		</div>
<script language="JavaScript" type="text/javascript">
function PopulateCannedEmail(){
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var value = document.getElementById("txt_search").value;
	console.log("111");
	if(brand_id != ""){
		showHint('../portal/canned-message-check.jsp?listemail=yes&home=yes&brand_id=' + brand_id + '&branchtype_id=' + branchtype_id + '&txt_search_email=' + value +'&canned=yes', 'listEmail');
	}
}

function PopulateCannedSMS(){
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var value = document.getElementById("txt_search").value;
	console.log("222");
	if(brand_id != ""){
		showHint('../portal/canned-message-check.jsp?listsms=yes&home=yes&brand_id=' + brand_id + '&branchtype_id=' + branchtype_id + '&txt_search_sms=' + value +'&canned=yes', 'listSMS');
	}
}

function getEmail(name, obj, hint) {
	var value = document.getElementById("txt_search").value;
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var url = "../portal/canned-message-check.jsp?listemail=yes&home=yes&canned=yes";
	var param = "&txt_search_email=" + value + '&brand_id=' + brand_id+ '&branchtype_id=' + branchtype_id;
	var str = "123";
	console.log(url + param);
// 	if(value != ""){
	showHint(url + param, hint);
// 	}
}

function getSMS(name, obj, hint) {
	var value = document.getElementById("txt_search").value;
	var brand_id = document.getElementById("dr_brand").value;
	var branchtype_id = document.getElementById("dr_branchtype").value;
	var url = "../portal/canned-message-check.jsp?listsms=yes&home=yes&canned=yes";
	var param = "&txt_search_sms=" + value + '&brand_id=' + brand_id+ '&branchtype_id=' + branchtype_id;
	var str = "123";
	console.log(url + param);
// 	if(value != ""){
	showHint(url + param, hint);
// 	}
}
// PopulateCannedEmail();PopulateCannedSMS();
</script>


