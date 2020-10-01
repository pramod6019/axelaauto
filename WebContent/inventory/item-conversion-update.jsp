<%@ page errorPage="../portal/error-page.jsp"%>
<jsp:useBean id="mybean" class="axela.inventory.Item_Conversion_Update"
	scope="request" />
<%
	mybean.doGet(request, response);  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%>-<%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
<link rel="shortcut icon" type="image/x-icon"
	href="../admin-ifx/axela.ico">
	<link href='../Library/jquery.qtip.css' rel='stylesheet'
		type='text/css' />

	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/bootstrap.css" rel="stylesheet"
		type="text/css" />
	<link href="../assets/css/components-rounded.css" rel="stylesheet"
		id="style_components" type="text/css" />
	<link href="../assets/css/font-awesome.css" rel="stylesheet"
		type="text/css" />
		<link href="../assets/themes/theme<%=mybean.GetTheme(request)%>.css"
	rel="stylesheet" type="text/css" />


	<script type="text/javascript" src="../Library/Validate.js"></script>
	<script type="text/javascript"
		src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
	<script src="../assets/js/jquery.min.js" type="text/javascript"></script>
	<script src="../assets/js/bootstrap.min.js" type="text/javascript"></script>
<LINK REL="STYLESHEET" TYPE="text/css"
	HREF="../Library/theme<%=mybean.GetTheme(request)%>/style.css">
<link href="../Library/theme<%=mybean.GetTheme(request)%>/menu.css"     
	rel="stylesheet" media="screen" type="text/css" />
<link
	href="../Library/theme<%=mybean.GetTheme(request)%>/font-awesome.css"
	rel="stylesheet" media="screen" type="text/css" />
<script type="text/javascript" src="../Library/Validate.js"></script>
<script type="text/javascript" src="../Library/jquery.js"></script>
<script type="text/javascript"
	src="../Library/jquery-ui.js?target=<%=mybean.jsver%>"></script> 
	<script type="text/javascript" src="../Library/dynacheck.js?target=<%=mybean.jsver%>"></script>
<script type="text/javascript"
	src="../Library/theme<%=mybean.GetTheme(request)%>/menu.js"></script>
<link rel="stylesheet" type="text/css"
	href="../Library/jquery.multiselect.css" />
<script type="text/javascript" src="../Library/jquery.multiselect.js"></script>
<script language="JavaScript" type="text/javascript">
	
</script>
<script language="JavaScript" type="text/javascript">
	$(function() {
		$("#txt_voucher_date").datepicker({
			showButtonPanel : true,
			dateFormat : "dd/mm/yy"
		});
	});

	function AddIssueItem() {
		var item_id = document.getElementById('itemconversion').value;
		var rateclass_id = document.getElementById('dr_rateclass_id').value;
		var item_qty = document.getElementById('txt_quantity').value;
		var session_id = document.getElementById('txt_session_id').value;
		var location_id = document.getElementById('dr_location').value;
		showHint(
				'../inventory/item-conversion-check.jsp?additem=yes&term=issue'
						+ '&vouchertype_id=' +
<%=mybean.vouchertype_id%>
	+ '&rateclass_id=' + rateclass_id  
						+ '&item_id=' + item_id
						+ '&location_id=' + location_id 
						+ '&session_id=' + session_id 
						+ '&item_qty='+ item_qty, 'displayissueitem');
		$("#itemconversion").set("val","");
		document.getElementById("txt_quantity").value = "";

	}
	function AddReceiveItem() {
		var item_id = document.getElementById('itemconversion1').value;
		var rateclass_id = document.getElementById('dr_rateclass_id').value;
		var item_qty = document.getElementById('txt_quantity1').value;
		var session_id = document.getElementById('txt_session_id').value;
		var location_id = document.getElementById('dr_location').value;
		showHint(
				'../inventory/item-conversion-check.jsp?additem=yes&term=receive'
						+ '&vouchertype_id=' +
<%=mybean.vouchertype_id%>
	+ '&rateclass_id=' + rateclass_id  + '&item_id=' + item_id
						+ '&location_id=' + location_id 
						+ '&session_id=' + session_id 
						+ '&item_qty=' 
						+ item_qty, 'displayreceiveitem');
		$("#itemconversion1").set("val","");
		document.getElementById("txt_quantity1").value = "";
	}

	//For Deleting cart item
	function delete_cart_item(item_id, term) {
		var session_id = document.getElementById('txt_session_id').value;
		 
		if (term =='issue') {
			showHintFootable('../inventory/item-conversion-check.jsp?item_id='
					+ item_id 
					+ '&vouchertype_id=<%=mybean.vouchertype_id%>'

	+ '&term=' + term 
	+ '&session_id=' + session_id 
	+ '&delete_cartitem=yes',
					'displayissueitem');
			document.getElementById('txt_quantity').value = "";
		} else if (term =='receive') {
			showHintFootable('../inventory/item-conversion-check.jsp?item_id='
					+ item_id + '&vouchertype_id=<%=mybean.vouchertype_id%>' 

	+ '&term=' + term 
	+ '&session_id=' + session_id   
	+ '&delete_cartitem=yes',
					'displayreceiveitem');
			document.getElementById('txt_quantity1').value = "";  
		}
	}
</script>

<link href="../assets/css/style.css" rel="stylesheet"
	type="text/css" />
</HEAD>
<body onload="" leftmargin="0" rightmargin="0" topmargin="0"
	bottommargin="0">
	<%@include file="../portal/header.jsp"%>
	<TABLE width="98%" border="0" align="center" cellPadding="0"
		cellSpacing="0">
		<TR>
			<TD align="left"><a href="home.jsp">Home</a> &gt; <a
				href="../inventory/index.jsp">Inventory</a>&gt; <a
				href="../accounting/voucher-list.jsp?all=yes&voucherclass_id=<%=mybean.voucherclass_id%>&vouchertype_id=<%=mybean.vouchertype_id%>">List
					<%=mybean.vouchertype_name%>s
			</a> &gt; <a href="item-conversion-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%></a>:</TD>
		</TR>
		<TR>
			<TD align="center" vAlign="top"><font color="#ff0000"><b><span
						id="msg"><%=mybean.msg%></span><br></b></font></TD>
		</TR>
		<TR>
			<TD height="400" align="center" vAlign="top"><form name="form1"
					method="post">
					<table width="100%" border="1" align="center" cellpadding="0"
						cellspacing="0" class="tableborder">
						<tr>
							<td><table width="100%" border="0" align="center"
									cellpadding="0" cellspacing="0" class="listtable">
									<tr>
										<th colspan="2"><%=mybean.status%>&nbsp;<%=mybean.vouchertype_name%></th>
									</tr>
									<tr>
										<td></td>
										<td align="center"><font size="1">Form fields
												marked with a red asterisk <b><font color="#ff0000">*</font></b>
												are required.<br>
										</font></td>
									</tr>
									<tr>
										<td align="right" valign="middle">Date<font color=red>*</font>:
										</td>
										<td align="left" valign="middle"><input
											name="txt_voucher_date" id="txt_voucher_date" type="text"
											class="textbox" value="<%=mybean.voucherdate%>" size="12"
											maxlength="10" />
											<input type="hidden" name="txt_session_id" id="txt_session_id" type="text"
											value="<%=mybean.session_id%>" />
											
											</td>
									</tr>
									<tr>
										<td align=right>Rate Class<font color="red"><b>*</b></font>:
										</td>
										<td align="left"><select id="dr_rateclass_id"
											name="dr_rateclass_id" class="selectbox">
												<option value="0">Select</option>
												<%=mybean.PopulateBranchClass()%>
										</select></td>
									</tr>

									<tr>
										<th colspan="2">Issued</th>
									</tr>
									<tr>
										<td align="right" valign="top">Location <font
											color="#ff0000">*</font>:
										</td>
										<td align="left" valign="top"><select id='dr_location'
											name='dr_location' class="selectbox">
												<%=mybean.PopulateLocation(mybean.vouchertrans_location_id)%>
										</select></td>
									</tr>
<%-- 									<tr>
										<td valign="top" align="right">Group:</td>
										<td valign="top" align="left"><select multiple="multiple"
											id="dr_itemgroup_id" name="dr_itemgroup_id" class="selectbox">
												<%=mybean.PopulateGroup(mybean.item_itemgroup_id)%>
										</select></td>
									</tr> --%>
									<tr>
										<td align="right" valign="top">Item<font color="#ff0000">*</font>:
										</td>
										<td align="left" valign="top"><%=mybean.PopulateIssueItem(mybean.item_id)%></td>
									</tr>
									<tr>
										<td align="right" valign="top">Quantity <font
											color="#ff0000">*</font>:
										</td>
										<td align="left" valign="top"><input type='text'
											name='txt_quantity'
											onkeyup="toInteger('txt_quantity','Quantity')"
											id='txt_quantity' class='textbox'
											value='<%=mybean.vouchertrans_qty%>' /> &nbsp;&nbsp;&nbsp;<input
											type='button' name='issueitem' id='issueitem' value='Add'
											class='button' onclick="AddIssueItem()"></td>
									</tr>
									<tr>
										<td align="center" valign="middle" colspan="2">
											<div id="displayissueitem"><%=mybean.ListCartItems(mybean.emp_id,
					mybean.vouchertype_id, "issue", mybean.comp_id)%>
											</div>   
										</td>
									</tr>
									<tr>
										<th colspan="2">Received</th>
									</tr>
									<tr>
										<td align="right" valign="top">Location <font
											color="#ff0000">*</font>:
										</td>
										<td align="left" valign="top"><select id='dr_location1'
											name='dr_location1' class="selectbox">
												<%=mybean.PopulateLocation(mybean.vouchertrans_location_id1)%>
										</select></td>
									</tr>
<%-- 									<tr>
										<td valign="top" align="right">Group:</td>
										<td valign="top" align="left"><select multiple="multiple"
											id="dr_itemgroup_id1" name="dr_itemgroup_id1"
											class="selectbox" onChange="PopulateItem1();">
												<%=mybean.PopulateGroup(mybean.item_itemgroup_id1)%>
										</select></td>
									</tr> --%>
									<tr>
										<td align="right" valign="top">Item<font color="#ff0000">*</font>:
										</td>
										<td align="left" valign="top"><%=mybean.PopulateReceivedItem(mybean.item_id1)%></td>
									</tr>
									<tr>
										<td align="right" valign="top">Quantity <font
											color="#ff0000">*</font>:
										</td>
										<td align="left" valign="top"><input type='text'
											name='txt_quantity1'
											onkeyup="toInteger('txt_quantity1','Quantity')"
											id='txt_quantity1' class='textbox'
											value='<%=mybean.vouchertrans_qty1%>' /> &nbsp;&nbsp;&nbsp;<input
											type='button' id='receiveitem' value='Add' class='button'
											onclick="AddReceiveItem()"></td>
									</tr>
									<tr>
										<td align="center" valign="middle" colspan="2"><div
												id="displayreceiveitem"><%=mybean.ListCartItems(mybean.emp_id,
					mybean.vouchertype_id, "receive", mybean.comp_id)%>
											</div></td>
									</tr>
									<%
										if (mybean.status.equals("Update") && !(mybean.entry_by == null)
												&& !(mybean.entry_by.equals(""))) {
									%>
									<tr valign="middle">
										<td align="right">Entry by:</td>
										<td><%=mybean.unescapehtml(mybean.entry_by)%> <input
											name="entry_by" type="hidden" id="entry_by"
											value="<%=mybean.entry_by%>"></td>
									</tr>

									<%
										}
									%>
									<%
										if (mybean.status.equals("Update") && !(mybean.entry_date == null)
												&& !(mybean.entry_date.equals(""))) {
									%>
									<tr valign="middle">
										<td align="right">Entry date:</td>
										<td><%=mybean.entry_date%> <input type="hidden"
											name="entry_date" value="<%=mybean.entry_date%>"></td>
									</tr>
									<%
										}
									%>
									<%
										if (mybean.status.equals("Update") && !(mybean.modified_by == null)
												&& !(mybean.modified_by.equals(""))) {
									%>
									<tr valign="middle">
										<td align="right">Modified by:</td>
										<td><%=mybean.unescapehtml(mybean.modified_by)%> <input
											name="modified_by" type="hidden" id="modified_by"
											value="<%=mybean.modified_by%>"></td>
									</tr>
									<%
										}
									%>
									<%
										if (mybean.status.equals("Update")
												&& !(mybean.modified_date == null)
												&& !(mybean.modified_date.equals(""))) {
									%>
									<tr valign="middle">
										<td align="right">Modified date:</td>
										<td><%=mybean.modified_date%> <input type="hidden"
											name="modified_date" value="<%=mybean.modified_date%>"></td>
									</tr>
									<%
										}
									%>
									<tr>
										<td colspan="2" align="center">
											<%
												if (mybean.status.equals("Add")) {
											%> <input name="addbutton" type="submit" class="button"
											id="addbutton" value="Add Conversion"
											onclick="return SubmitFormOnce(document.form1,this);" /> <input
											type="hidden" name="add_button" value="yes"> <%
 	} else if (mybean.status.equals("Update")) {
 %> <input type="hidden" name="update_button" value="yes"> <input
													name="updatebutton" type="submit" class="button"
													id="updatebutton" value="Update Conversion"
													onclick="return SubmitFormOnce(document.form1,this);" /> <input
													name="delete_button" type="submit" class="button"
													id="delete_button" onClick="return confirmdelete(this)"
													value="Delete Conversion" /> <%
 	}
 %>
										</td>
									</tr>
								</table></td>
						</tr>
					</table>
				</form></TD>
		</TR>
	</TABLE>
	<%@include file="../Library/admin-footer.jsp"%>
</body>
</HTML>
