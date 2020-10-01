<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.portal.Sms_Exe_Send" scope="request" />
<%
	mybean.doPost(request, response);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt" />
<%@include file="../Library/css.jsp"%>
</HEAD>
<body onload="Displaypaymode();" class="page-container-bg-solid page-header-menu-fixed">
	<%@include file="header.jsp"%>
	<div class="page-container">
		<!-- BEGIN CONTENT -->
		<div class="page-content-wrapper">
			<!-- BEGIN CONTENT BODY -->
			<!-- BEGIN PAGE HEAD-->
			<div class="page-head">
				<div class="container-fluid">
					<!-- BEGIN PAGE TITLE -->
					<div class="page-title">
						<h1>Send Executive SMS</h1>
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
						<li><a href="home.jsp">Home</a> &gt;</li>
						<li><a href="sms.jsp">SMS</a> &gt;</li>
						<li><a href="sms-exe-send.jsp">Send Executive SMS<%=mybean.status%></a>:</li>
					</ul>
					<!-- END PAGE BREADCRUMBS -->

					
						<div class="tab-pane" id="">
							<center>
								<font color="#ff0000"><b><%=mybean.msg%></b></font>
							</center>
							<div class="container-fluid portlet box  ">
								<div class="portlet-title" style="text-align: center">
									<div class="caption" style="float: none">
										Send Executive SMS<%=mybean.status%>
									</div>
								</div>
								<div class="portlet-body">
									<div class="tab-pane" id="">
										<center>
											<font>Form fields marked with a red asterisk <b><font
													color="#ff0000">*</font></b> are required.
											</font>
										</center>
										<br />
										<form name="form1" method="post" action="sms-exe-send.jsp"
											class="form-horizontal">
											<%
												if (mybean.branch_id.equals("0")) {
											%>
											<div class="form-element6 form-element-center">
												<label >Branch <font color=red>*</font>: </label>
													<select name="dr_branch" id="dr_branch"
														class="form-control" onChange="FormSubmit();">
														<%=mybean.PopulateBranch()%>
													</select>
											</div>
											<%
												}
											%>
											<div class="form-element6 form-element-center ">
												<label >All: </label>
													<input type="checkbox" name="chk_sms_allexe" 
														id="chk_sms_allexe" onchange="Displaypaymode();"
														<%=mybean.PopulateCheck(mybean.sms_allsms)%>>
											</div>
											
											<div class="form-element6 form-element-center" style="display: none;" id ="sms_exe">
												<label>Select Executives:</label>
												<div>
													<select name="sms_allexe" multiple="multiple"
														 class="form-control multiselect-dropdown" id="sms_allexe">
													<%=mybean.PopulateExecutives()%>
													</select>
												</div>	
											</div>
										
											<div class="form-element6 form-element-center">
												<label >Message<font color=red>*</font>: </label>
													<textarea name="txt_sms_msg" class="form-control"
														id="txt_sms_msg" cols="58" rows="4"
														onKeyUp="charcount('txt_sms_msg', 'span_sms_msg','<font color=red>({CHAR} characters left)</font>', '500')"><%=mybean.sms_msg%></textarea>
													<span id="span_sms_msg"> (500 characters) </span>
											</div>
												<center>
												<div class="form-element6 form-element-center">
													<input name="send_button" type="submit"
															class=" btn btn-success" id="send_button"
																value="Send"  />
																</div>
												</center>
												<div class="form-element6 form-element-center">
													<table class="table table-bordered" >
														<tr>
															<th colspan="2"><b><center>Substitution
																		Variables</center></b></th>
														</tr>
														<tr>
															<td align="right">Executive Name:</td>
															<td>[NAME]</td>
														</tr>
														<tr>
															<td align="right">Executive ID:</td>
															<td>[EXECUTIVEID]</td>
														</tr>
														<tr>
															<td align="right">Executive Ref. No.:</td>
															<td>[EXECUTIVEREFNO]</td>
														</tr>
													</table>
											</div>
										</form>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 	<tr valign="middle" id="exes"> -->
	<!-- 		<td colspan="2" align="right"> -->
	<!-- 			<TABLE width="98%" align="center" cellPadding="0" cellSpacing="0"> -->
	<!-- 				<TBODY> -->
	<!-- 					<TR align="center"> -->
	<!-- 						<TD colspan="3"><strong>Select Executives:</strong></TD> -->
	<!-- 					</TR> -->
	<!-- 					<TR> -->
	<!-- 						<TD height="206" align="right"><select name="sms_allexe" -->
	<!-- 							size="15" multiple="multiple" class="selectbox" id="sms_allexe" -->
	<!-- 							style="width: 300px;"> -->
	<%-- 								<%=mybean.PopulateExecutives()%> --%>
	<!-- 						</select></TD> -->
	<!-- 						<TD align="center"><input name="Input3" type="button" -->
	<!-- 							class="button" -->
	<!-- 							onClick="JavaScript:AddItem('sms_allexe','smsexe_exe', '')" -->
	<!-- 							value="   Add >>"> <BR> <INPUT name="Input" -->
	<!-- 							type="button" class="button" -->
	<%-- 							onClick="JavaScript:DeleteItem('smsexe_exe')" value="<< Delete"></TD> --%>
	<!-- 						<TD align="left"><select name="smsexe_exe" size="15" -->
	<!-- 							multiple="multiple" class="selectbox" id="smsexe_exe" -->
	<!-- 							style="width: 300px;"> -->
	<%-- 								<%=mybean.PopulateExecutivesTrans()%> --%>
	<!-- 						</select></TD> -->
	<!-- 					</TR> -->
	<!-- 				</TBODY> -->
	<!-- 			</TABLE> -->
	<!-- 		</td> -->
	<!-- 	</tr> -->

<%@include file="../Library/js.jsp"%>
	<%@include file="../Library/admin-footer.jsp"%>
	 <script language="JavaScript" type="text/javascript">

	/* function hideRow(element) {
		//element refer to row id where u want to hide a row.
		var myObj = document.getElementById(element);
		//u need to a give id for a row to which u want to hide
		var cels = myObj.getElementsByTagName('select');
		for (var col_no = 0; col_no < cels.length; col_no++) {
			cels[col_no].style.display = 'none';
		}
		myObj.style.visibility = 'hidden';
	}

	function displayRow(element) {
		var myObj = document.getElementById(element);
		//u need to a give id for a row to which u want to hide
		var cels = myObj.getElementsByTagName('select')
		for (var col_no = 0; col_no < cels.length; col_no++) {
			cels[col_no].style.display = '';
		}
		myObj.style.visibility = 'visible';
		//myObj.style.height = '0px';
	}
	function Displaypaymode() {
		
		var zone = document.form1.chk_sms_allexe.checked;
		if (zone == true)
			zone = "1";
		else
			zone = "0";
		//        alert("str======="+zone);
		if (zone == "1") {
			hideRow('sms_exe');
		}
		if (zone == "0") {
			displayRow('sms_exe');
		}
	} */
// 	function onPressGroup() {
// // 		alert("coming");
// 		var sms_exe_exe =outputSelected(document.getElementById("sms_allexe").options);
// 	alert("sms_exe_exe=="+sms_exe_exe);
// // 		alert("sms_exe_exe=length="+sms_exe_exe[0]);
// // 		for (i = 0; i < sms_exe_exe.length; i++) {
// // 			alert("coming inside")
// // 			document.form1.sms_exe_exe.options[i].selected = true;
// // 		}
// 	showHint('../portal/sms-exe-send.jsp?sms_exe_exe='+sms_exe_exe,'123');
//  	}
	function FormSubmit() {
		document.form1.submit();
	}
	
	function Displaypaymode() {
		if($('#chk_sms_allexe').prop('checked') == true)
			$('#sms_exe').hide();
		else
			$('#sms_exe').show();
	}
</script>

</body>
</HTML>
