<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.accounting.Journal_Update" scope="request" />
<% mybean.doGet(request, response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
	<%@include file="../Library/css.jsp"%>
<style>
.disabled {
	background-color: #dddddd;
}
</style>
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
						<h1><%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%></h1>
					</div>
					<!-- END PAGE TITLE -->
				</div>
			</div>
			<!-- END PAGE HEAD-->
			<!-- BEGIN PAGE CONTENT BODY -->
			<div class="page-content">
			<div class="page-content-inner">
				<div class="container-fluid">
					<ul class="page-breadcrumb breadcrumb">
						<li><a href="../portal/home.jsp">Home</a> &gt;</li>
						<li><a href="financeindex.jsp">Finance</a> &gt;</li>

						<li><a
							href="voucher-list.jsp?all=yes&vouchertype_id=<%=mybean.vouchertype_id%>">List
								<%=mybean.vouchertype_name%>
						</a> &gt;</li>

						<li><a href="journal-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>
								<%=mybean.vouchertype_name%></a>:</li>

					</ul>
					<!-- END PAGE BREADCRUMBS -->

						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<form name="form1" method="post" class="form-horizontal">
							                <input name="txt_vouchertype_id" type="hidden" id="txt_vouchertype_id" value="<%=mybean.vouchertype_id%>" />
											<input type="hidden" name="hid_voucher_no" id="hid_voucher_no" value="<%= mybean.voucher_no%>" />
											<input type="hidden" name="txt_vouchertype_authorize" id="txt_vouchertype_authorize" value="<%=mybean.vouchertype_authorize %>" />
											<input type="hidden" name="txt_vouchertype_defaultauthorize" id="txt_vouchertype_defaultauthorize" value="<%=mybean.vouchertype_defaultauthorize %>" />
											<input type="hidden" name="txt_voucher_authorize_id" id="txt_voucher_authorize_id" value="<%=mybean.voucher_authorize_id %>" />
											<input type="hidden" name="txt_voucher_authorize_time" id="txt_voucher_authorize_time" value="<%=mybean.voucher_authorize_time %>" />
												
								<div class="portlet box  ">
									<div class="portlet-title" style="text-align: center">
										<div class="caption" style="float: none">
											<%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%>
										</div>
									</div>
									<div class="portlet-body portlet-empty">
										<div class="container-fluid">
											<center>
												Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</center>
											<input type="hidden" name="txt_voucher_id"
												id="txt_voucher_id" value="<%=mybean.voucher_id%>" />
										</div>
										<%if(mybean.voucher_so_id.equals("0")){ %>
										<div class="form-element6">
											<label> <%=mybean.vouchertype_name%> Date<font
												color="#ff0000">*</font>:
											</label> <input type="hidden" id="txt_session_id"
												name="txt_session_id" value="<%=mybean.session_id%>"><input
												name="txt_voucher_date" id="txt_voucher_date"
												value="<%=mybean.voucherdate%>"
												class="form-control datepicker"
												 type="text" maxlength="10" />
										</div>
										<%}else{ %>
										<div class="form-element3">
											<label> <%=mybean.vouchertype_name%> Date<font
												color="#ff0000">*</font>:
											</label><input type="hidden" id="txt_session_id"
												name="txt_session_id" value="<%=mybean.session_id%>"><input
												name="txt_voucher_date" id="txt_voucher_date"
												value="<%=mybean.voucherdate%>"
												class="form-control datepicker"
												 type="text" maxlength="10" />
										</div>
										<div class="form-element3">
											<label>SO ID: <a href='../sales/veh-salesorder-list.jsp?so_id=<%=mybean.voucher_so_id%>' target='_blank'><%=mybean.voucher_so_id%></a> </label>
											
											
											
											<input type="hidden" id="txt_voucher_so_id"
												name="txt_voucher_so_id" value="<%=mybean.voucher_so_id%>">
												
										</div>
										
										<%} %>
										<div class="form-element6">
											<label> Branch<font color="#ff0000">*</font>:
											</label> <select id="dr_voucher_branch_id"
												name="dr_voucher_branch_id" class="form-control">
												<%=mybean.PopulateBranch(mybean.voucher_branch_id, "", "", "", request)%>
											</select>
										</div>

										<div class="form-element12 form-element-center" id="display_ledger">
										
											<%=mybean.ledger.DisplayMultiLedger(mybean.voucher_id, mybean.comp_id, mybean.addB, mybean.updateB, request)%>
											
										</div>
										
										<div class="row"></div>
										
										<div class="form-element6">
											<label>Executive<font color="#ff0000">*</font>:
											</label> <select name="dr_executive"
												value="<%=mybean.voucher_emp_id%>" id="dr_executive"
												class="form-control">
												<%=mybean.PopulateExecutives(mybean.emp_id, mybean.comp_id)%>
											</select>
										</div>


										<% if (mybean.vouchertype_ref_no_enable.equals("1")) { %>
										<div class="form-element6">
											<label>Reference No.<% if (mybean.vouchertype_ref_no_mandatory.equals("1")) { %> <font color="#ff0000">*</font>: <% } %>
											</label> <input name="txt_voucher_ref_no" type="text"
												class="form-control" id="txt_voucher_ref_no"
												value="<%=mybean.voucher_ref_no%>" size="32" maxlength="50" />
										</div>

										<% } %>
										
										<div class="row"></div>
										<div class="form-element6">
											<label>Narration<font color="#ff0000">*</font>:
											</label>
											<textarea name="txt_voucher_narration" cols="70" rows="4"
												class="form-control" id="txt_voucher_narration"
												maxlength="255"
												onKeyUp="charcount('txt_voucher_narration', 'span_txt_voucher_narration','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.voucher_narration%></textarea>
											<span id="span_txt_voucher_narration">(255 Characters)</span>
										</div>
										<div class="form-element6">
											<label>Active: </label>
											<input id="chk_voucher_active" type="checkbox" name="chk_voucher_active" <%=mybean.PopulateCheck(mybean.voucher_active)%> />
										</div>
										<div class="row"></div>
										
										<% if (mybean.status.equals("Update")
												&& !(mybean.entry_by == null)
												&& !(mybean.entry_by.equals(""))) { %>

										<div class="form-element6">
											<label>Entry By: <%=mybean.unescapehtml(mybean.entry_by)%></label>
											<input name="entry_by" type="hidden" id="entry_by" value="<%=mybean.entry_by%>" />
										</div>
										<div class="form-element6">
											<label>Entry Date: <%=mybean.entry_date%></label>
											<input type="hidden" name="entry_date" value="<%=mybean.entry_date%>">
										</div>
										<% } %>

										<% if (mybean.status.equals("Update") 
												&& !(mybean.modified_by == null)
												&& !(mybean.modified_by.equals(""))) { %>

										<div class="form-element6">
											<label>Modified By: <%=mybean.unescapehtml(mybean.modified_by)%></label>
											<input name="modified_by" type="hidden" id="modified_by" value="<%=mybean.modified_by%>">
										</div>
										<div class="form-element6">
											<label>Modified Date: <%=mybean.modified_date%></label>
											<input type="hidden" name="modified_date" value="<%=mybean.modified_date%>">
										</div>
										<% } %>
										<center>
											
											<% if (mybean.status.equals("Add")) { %>
											
											<input name="addbutton" type="submit" class="btn btn-success" id="addbutton" value="Add Journal" onClick="return SubmitFormOnce(document.form1,this);" />
											<input type="hidden" name="add_button" value="yes">
											
											<% } else if (mybean.status.equals("Update")) { %>
											
											<input type="hidden" name="update_button" value="yes">
											<input name="updatebutton" type="submit" class="btn btn-success" id="updatebutton" value="Update Journal" onClick="return SubmitFormOnce(document.form1,this);" />
											<input name="delete_button" type="submit" class="btn btn-success" id="delete_button" onClick="return confirmdelete(this)" value="Delete Journal" />
											
											<% } %>
											
											</center>
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

	<%@include file="../Library/admin-footer.jsp"%>
	<%@include file="../Library/js.jsp"%>
	<script language="JavaScript" type="text/javascript">

	function journalclear(txt_id){
			if(CheckNumeric($("#dr_ledger_"+txt_id.split('_')[2]).val())==0){
				$("#dr_"+txt_id.substring(3,txt_id.length)).val("0");
				$("#cr_"+txt_id.substring(3,txt_id.length)).val("0");
				alert("Party Not Selected For "+txt_id.split('_')[2]+" Row!");
			}else{
				if(txt_id.substring(0,2)=='cr'){
					$("#dr_"+txt_id.substring(3,txt_id.length)).val("0");
				}else{
					$("#cr_"+txt_id.substring(3,txt_id.length)).val("0");
				}
			}
		}
		
		
		function journalupdate(){
			var count = eval($("#txt_ledger_count").val());
			var dr_amt=0;
			var cr_amt=0;
			for(var i=1;i<=count;i++){
				dr_amt = eval(dr_amt) + eval(CheckNumeric($("#dr_journal_"+i).val()));
				cr_amt = eval(cr_amt) + eval(CheckNumeric($("#cr_journal_"+i).val()));
			}
			$("#total_dr").val(dr_amt);
			$("#total_cr").val(cr_amt);
			
			if(dr_amt > cr_amt){
				$("#cr_difference").html(eval(dr_amt)-eval(cr_amt));
				$("#dr_difference").html("0");
			}else{
				$("#cr_difference").html("0");
				$("#dr_difference").html(eval(cr_amt)-eval(dr_amt));
			}
		}
		
		function deleteRow(obj){
			obj.parentNode.remove();
			journalupdate();
		}
	
	function addNewRecord(){
		var all_fill = true;
		var count = eval($('#txt_ledger_count').val());
		var max_count=0;
		for( ; count > 0 ; count--){
			
			$("#hid_ledger_"+count).val(CheckNumeric($("#dr_ledger_"+count).val()));
			
			if(CheckNumeric($("#dr_ledger_"+count).val())==0){
				$("#dr_journal_"+count).val("0");
				$("#cr_journal_"+count).val("0");
			}
			
			if(eval(CheckNumeric($('#count_'+count).val()))>0){
				if(eval(max_count) < eval(CheckNumeric($('#count_'+count).val()))){
					max_count = CheckNumeric($('#count_'+count).val());
				}
				if($('#dr_ledger_'+count).val()==null){
					all_fill=false;
				}
			}
		}
		if(all_fill){
			count = eval(max_count)+1;
			var data = "<tr id=tr_" + count + " class=ledger_tr>"
			+"<td id=td_" + count + " align='center'><input type=text id=count_" + count + " value=" + count + " hidden/>" + count + "</td>"
			+"<td onclick=onclick='deleteRow(this);' align='center'>X</td>"
			+"<td width='50%'><select onchange='addNewRecord();' class='form-control select2 ledger' id=dr_ledger_" + count + " id=dr_ledger_" + count + " ></select>"
			+"<input type=hidden value='0' id=hid_ledger_" + count + " name=hid_ledger_" + count + " /></td>"
			+"<td><input type=text class='form-control text-right' id=dr_journal_" + count + " name=dr_journal_" + count + " value=0 onchange=\"toFloat('dr_journal_" + count + "');journalclear('dr_journal_" + count + "');journalupdate('dr_journal_" + count + "');\"></td>"
			+"<td><input type=text class='form-control text-right' id=cr_journal_" + count + " name=cr_journal_" + count + " value=0 onchange=\"toFloat('cr_journal_" + count + "');journalclear('cr_journal_" + count + "');journalupdate('cr_journal_" + count + "');\"></td>"
			+"</tr>";
			
			$('.ledger_tr:last').after(data);
			$('#txt_ledger_count').val(eval($('#txt_ledger_count').val())+1);
			
			reloadselect2JS();
		}
		journalupdate();
	}
	
	$(".ledger").on('change',function(){
		var count = this.id.split('_')[2];
		if(eval(CheckNumeric($("#"+this.id).val()))>0){
			setTimeout(function(){$("#dr_journal_"+count).select();},500);
		}
	});
	
	function reloadselect2JS(){
		
		
		
		var head = document.getElementsByTagName('head')[0];
		var script = document.createElement('script');
		script.type='text/javascript';
		script.src='../assets/js/components-select2.min.js';
		head.appendChild(script);
	}
	</script>

</body>
</HTML>

