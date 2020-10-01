<!--           <TR> -->
								<%--             <TD align="left"><a href="home.jsp">Home</a> &gt; <a href="manager.jsp">Business Manager</a> &gt; <a href="branch-list.jsp?all=yes">List Branches</a> &gt; <a href="branch-update.jsp?<%=mybean.QueryString%>"><%=mybean.status%>&nbsp;Branch</a>:</TD> --%>
								<!--           </TR> -->
								<!--           <TR> -->
								<center>
									<font color="#ff0000"><b><%=mybean.msg%></b></font>
								</center>
								<!--           </TR> -->
								<TR>
									<TD height="300" align="center" vAlign="top">
										<form name="form1" method="post">
											<table width="100%" border="1" align="center" cellpadding="0"
												cellspacing="0" class="tableborder">
												<tr>
													<td><table width="100%" border="0" align="center"
															cellpadding="0" cellspacing="0" class="listtable">
															<tr align="center">
																<th colspan="4"><%=mybean.status%> Branch</th>
															</tr>
															<tr>
																<td>&nbsp;</td>
																<td colspan="4"><font>Form fields marked
																		with a red asterisk <b><font color="#ff0000">*</font></b>
																		are required.<br>
																</font></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Name<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_name"
																	type="text" class="form-control"
																	value="<%=mybean.branch_name%>" size="50"
																	maxlength="255" /> (Enter Minimum of 3 characters)</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Name on Invoice<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input
																	name="txt_branch_invoice_name" type="text"
																	class="form-control"
																	value="<%=mybean.branch_invoice_name%>" size="50"
																	maxlength="255" />
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Code<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_code"
																	type="text" class="form-control"
																	value="<%=mybean.branch_code%>" size="25"
																	maxlength="25" /> (Only alphanumeric characters) are
																	allowed!</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">VAT<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_vat"
																	type="text" id="txt_branch_vat" class="form-control"
																	value="<%=mybean.branch_vat%>" size="25" maxlength="25" />
																</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">CST<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_cst"
																	type="text" id="txt_branch_cst" class="form-control"
																	value="<%=mybean.branch_cst%>" size="25" maxlength="25" />
																</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">PAN<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_pan"
																	type="text" id="txt_branch_pan" class="form-control"
																	value="<%=mybean.branch_pan%>" size="25" maxlength="25" />
																</td>
															</tr>

															<tr valign="middle">
																<td align="right" valign="middle">Quote Prefix<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input
																	name="txt_branch_quote_prefix" type="text"
																	class="form-control"
																	value="<%=mybean.branch_quote_prefix%>" size="25"
																	maxlength="25" /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">SO Prefix<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_so_prefix"
																	type="text" class="form-control"
																	value="<%=mybean.branch_so_prefix%>" size="25"
																	maxlength="25" /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Invoice Prefix<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input
																	name="txt_branch_invoice_prefix" type="text"
																	class="form-control"
																	value="<%=mybean.branch_invoice_prefix%>" size="25"
																	maxlength="25" /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Receipt Prefix<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input
																	name="txt_branch_receipt_prefix" type="text"
																	class="form-control"
																	value="<%=mybean.branch_receipt_prefix%>" size="25"
																	maxlength="25" /></td>
															</tr>

															<tr valign="middle">
																<td align="right" valign="middle">Region<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><select name="dr_branch_region_id"
																	class="form-control" id="dr_branch_region_id">
																		<%=mybean.PopulateRegion()%>
																</select></td>
															</tr>


															<tr valign="middle">
																<td align="right" valign="middle">Type<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><select
																	name="dr_branch_branchtype_id" class="form-control"
																	id="dr_branch_branchtype_id">
																		<%=mybean.PopulateBranchType()%>
																</select></td>
															</tr>

															<tr valign="middle">
																<td align="right" valign="middle">Franchisee<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><select name="dr_franchisee_id"
																	class="form-control">
																		<%=mybean.PopulateFranchisee()%>
																</select></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Brand<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><select name="dr_brand_id"
																	class="form-control" id="dr_brand_id">
																		<%=mybean.PopulatePrincipal()%>
																</select></td>

															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Class<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><select
																	name="dr_branch_rateclass_id" class="form-control"
																	id="dr_branch_rateclass_id">
																		<%=mybean.PopulateClass()%>
																</select></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Mobile 1<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_mobile1"
																	type="text" id="txt_branch_mobile1"
																	onKeyUp="toPhone('txt_branch_mobile1','Mobile 1')"
																	class="form-control" value="<%=mybean.branch_mobile1%>"
																	size="20" maxlength="10" /> (9999999999)</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Mobile 2:</td>
																<td colspan="4"><input name="txt_branch_mobile2"
																	type="text" class="form-control"
																	id="txt_branch_mobile2"
																	onKeyUp="toPhone('txt_branch_mobile2','Mobile 2')"
																	value="<%=mybean.branch_mobile2%>" size="20"
																	maxlength="10" /> (9999999999)</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Phone 1<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_phone1"
																	type="text" class="form-control" id="txt_branch_phone1"
																	onKeyUp="toPhone('txt_branch_phone1','Phone 1')"
																	value="<%=mybean.branch_phone1%>" size="20"
																	maxlength="12" /> (080-33333333)</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Phone 2:</td>
																<td colspan="4"><input name="txt_branch_phone2"
																	type="text" class="form-control" id="txt_branch_phone2"
																	onKeyUp="toPhone('txt_branch_phone2','Phone 2')"
																	value="<%=mybean.branch_phone2%>" size="20"
																	maxlength="12" /> (080-33333333)</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Email 1<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><input name="txt_branch_email1"
																	type="text" class="form-control"
																	value="<%=mybean.branch_email1%>" size="50"
																	maxlength="100" /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Email 2:</td>
																<td colspan="4"><input name="txt_branch_email2"
																	type="text" class="form-control"
																	value="<%=mybean.branch_email2%>" size="50"
																	maxlength="100" /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Sales Email<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><textarea
																		name="txt_branch_sales_email" cols="50" rows="5"
																		class="form-control" id="txt_branch_sales_email"
																		onKeyUp="charcount('txt_branch_sales_email', 'span_txt_branch_sales_email','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_sales_email%></textarea>
																	<span id=span_txt_branch_sales_email> 1000
																		characters </span></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Service Email<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><textarea
																		name="txt_branch_service_email" cols="50" rows="5"
																		class="form-control" id="txt_branch_service_email"
																		onKeyUp="charcount('txt_branch_service_email', 'span_txt_branch_service_email','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_service_email%></textarea>
																	<span id=span_txt_branch_service_email> 1000
																		characters </span></td>
															</tr>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Ticket Email<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><textarea
																		name="txt_branch_ticket_email" cols="50" rows="5"
																		class="form-control" id="txt_branch_ticket_email"
																		onKeyUp="charcount('txt_branch_ticket_email', 'span_txt_branch_ticket_email','<font color=red>({CHAR} characters left)</font>', '1000')"><%=mybean.branch_ticket_email%></textarea>
																	<span id=span_txt_branch_ticket_email> 1000
																		characters </span></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Address<font
																	color="#ff0000">*</font>:
																</td>
																<td colspan="4"><textarea name="txt_branch_add"
																		cols="50" rows="5" class="form-control"
																		id="txt_branch_add"
																		onKeyUp="charcount('txt_branch_add', 'span_txt_branch_add','<font color=red>({CHAR} characters left)</font>', '255')"><%=mybean.branch_add%></textarea>
																	<span id=span_txt_branch_add> 255 characters </span></td>
															</tr>
															<tr valign="center">
																<td align="right" valign="top">City<font
																	color="#ff0000">*</font>:
																</td>
																<td align="left" valign="top">
																	<%--                         <input tabindex="-1" class="bigdrop select2-offscreen" id="maincity" name="maincity" style="width:250px"  value="<%=mybean.branch_city_id%>" type="hidden"> --%>
																	<select class="form-control select2" id="maincity"
																	name="maincity" onchange="selectcity()"
																	onload="selectcity()">

																		<%=mybean.check.PopulateCities(mybean.branch_city_id)%>
																</select> <input type="text" id="txt_maincity"
																	name="txt_maincity" hidden />

																</td>


																<td align="right" valign="top">Pin/Zip<font
																	color="#ff0000">*</font>:
																</td>
																<td><input name="txt_branch_pin" type="text"
																	class="form-control" id="txt_branch_pin"
																	onKeyUp="toInteger('txt_branch_pin','Pin')"
																	value="<%=mybean.branch_pin%>" size="10" maxlength="6" /></td>
															</tr>
															<!-- <tr valign="center">
                                    <td align="right" valign="top">State:<b></b></td>
                                    <td><span id="state_id"><%//=mybean.PopulateState()%></span></td>
                                    <td align="right" valign="top">&nbsp;</td>
                                    <td>&nbsp;</td>
                                  </tr>-->
															<tr valign="middle">
																<td align="right" valign="middle">Quote
																	Description:</td>
																<td colspan="4"><textarea name="txt_quote_desc"
																		cols="70" rows="4" class="form-control"
																		id="txt_quote_desc"><%=mybean.quote_desc%></textarea>
																	<script type="text/javascript">
																		CKEDITOR
																				.replace(
																						'txt_quote_desc',
																						{
																							uiColor : hexc($(
																									"a:link")
																									.css(
																											"color")),

																						});
																	</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Quote Terms &
																	Conditions:</td>
																<td colspan="4"><textarea name="txt_quote_terms"
																		cols="70" rows="4" class="form-control"
																		id="txt_quote_terms"><%=mybean.quote_terms%></textarea>
																	<script type="text/javascript">
																		CKEDITOR
																				.replace(
																						'txt_quote_terms',
																						{
																							uiColor : hexc($(
																									"a:link")
																									.css(
																											"color")),

																						});
																	</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Sales Order
																	Description:</td>
																<td colspan="4"><textarea name="txt_so_desc"
																		cols="70" rows="4" class="form-control"
																		id="txt_so_desc"><%=mybean.so_desc%></textarea> <script
																		type="text/javascript">
																			CKEDITOR
																					.replace(
																							'txt_so_desc',
																							{
																								uiColor : hexc($(
																										"a:link")
																										.css(
																												"color")),

																							});
																		</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Sales Order Terms
																	& Conditions:</td>
																<td colspan="4"><textarea name="txt_so_terms"
																		cols="70" rows="4" class="form-control"
																		id="txt_so_terms"><%=mybean.so_terms%></textarea> <script
																		type="text/javascript">
																			CKEDITOR
																					.replace(
																							'txt_so_terms',
																							{
																								uiColor : hexc($(
																										"a:link")
																										.css(
																												"color")),

																							});
																		</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Invoice
																	Description:</td>
																<td colspan="4"><textarea name="txt_invoice_desc"
																		cols="70" rows="4" class="form-control"
																		id="txt_invoice_desc"><%=mybean.invoice_desc%></textarea>
																	<script type="text/javascript">
																		CKEDITOR
																				.replace(
																						'txt_invoice_desc',
																						{
																							uiColor : hexc($(
																									"a:link")
																									.css(
																											"color")),

																						});
																	</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Invoice Terms &
																	Conditions:</td>
																<td colspan="4"><textarea name="txt_invoice_terms"
																		cols="70" rows="4" class="form-control"
																		id="txt_invoice_terms"><%=mybean.invoice_terms%></textarea>
																	<script type="text/javascript">
																		CKEDITOR
																				.replace(
																						'txt_invoice_terms',
																						{
																							uiColor : hexc($(
																									"a:link")
																									.css(
																											"color")),

																						});
																	</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Receipt
																	Description:</td>
																<td colspan="4"><textarea name="txt_receipt_desc"
																		cols="70" rows="4" class="form-control"
																		id="txt_receipt_desc"><%=mybean.receipt_desc%></textarea>
																	<script type="text/javascript">
																		CKEDITOR
																				.replace(
																						'txt_receipt_desc',
																						{
																							uiColor : hexc($(
																									"a:link")
																									.css(
																											"color")),

																						});
																	</script></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Active:</td>
																<td colspan="4"><input type="checkbox"
																	name="ch_branch_active"
																	<%=mybean.PopulateCheck(mybean.branch_active)%> /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Notes:</td>
																<td colspan="4"><textarea name="txt_branch_notes"
																		cols="70" rows="4" class="form-control"
																		id="txt_branch_notes"> <%=mybean.branch_notes%></textarea></td>
															</tr>

															<%
																if (mybean.status.equals("Update")) {
															%>
															<tr valign="center">
																<td align="right">Automated Tasks:</td>
																<td valign="top" colspan="4"><table width="98%"
																		border="1" cellspacing="0" cellpadding="1">
																		<tr>
																			<td align="center"><b>Task Type</b></td>
																			<td align="center"><b>Send Email</b></td>
																			<td align="center"><b>Format Email</b></td>
																			<td align="center"><b>Send SMS</b></td>
																			<td align="center"><b>Format SMS</b></td>
																		</tr>
																		<tr>
																			<td align="right">Lead:</td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_lead_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_lead_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Lead&opt=branch_lead_email_format">Format</a></td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_lead_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_lead_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Lead&opt=branch_lead_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Lead for Executive:</td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Lead for Executive&opt=branch_lead_email_exe_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Lead for Executive&opt=branch_lead_sms_exe_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Enquiry:</td>
																			<td align="center"><input
																				name="chk_branch_enquiry_email_enable"
																				type="checkbox" id="chk_branch_enquiry_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_enquiry_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Enquiry&opt=branch_enquiry_email_format">Format</a></td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_enquiry_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_enquiry_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Enquiry&opt=branch_enquiry_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Enquiry for Executive:</td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Enquiry for Executive&opt=branch_enquiry_email_exe_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Enquiry for Executive&opt=branch_enquiry_sms_exe_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Brochure:</td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_enquiry_brochure_email_enable"
																				id="chk_branch_enquiry_brochure_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_enquiry_brochure_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Brochure&opt=branch_enquiry_brochure_email_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center">&nbsp;</td>
																		</tr>

																		<tr>
																			<td align="right">Test Drive:</td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_testdrive_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_testdrive_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Test Drive&opt=branch_testdrive_email_format">Format</a></td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_testdrive_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_testdrive_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Test Drive&opt=branch_testdrive_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Test Drive for Executive:</td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Test Drive for Executive&opt=branch_testdrive_email_exe_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Test Drive for Executive&opt=branch_testdrive_sms_exe_format">Format</a></td>
																		</tr>



																		<tr>
																			<td align="right">Quote:</td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_quote_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_quote_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Quote&opt=branch_quote_email_format">Format</a></td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_quote_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_quote_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Quote&opt=branch_quote_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Quote for Executive:</td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Quote for Executive&opt=branch_quote_email_exe_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Quote for Executive&opt=branch_quote_sms_exe_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Sales Order:</td>
																			<td align="center"><input
																				name="chk_branch_so_email_enable" type="checkbox"
																				id="chk_branch_so_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_so_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Sales Order&opt=branch_so_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_so_sms_enable" type="checkbox"
																				id="chk_branch_so_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_so_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Sales Order&opt=branch_so_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Sales Order Delivered:</td>
																			<td align="center"><input
																				name="chk_branch_so_delivered_email_enable"
																				type="checkbox"
																				id="chk_branch_so_delivered_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_so_delivered_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Sales Order Delivered&opt=branch_so_delivered_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_so_delivered_sms_enable"
																				type="checkbox"
																				id="chk_branch_so_delivered_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_so_delivered_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Sales Order Delivered&opt=branch_so_delivered_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Sales Order for Executive:</td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Sales Order for Executive&opt=branch_so_email_exe_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Sales Order for Executive&opt=branch_so_sms_exe_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Service Due:</td>
																			<td align="center"><input
																				name="chk_branch_service_due_email_enable"
																				type="checkbox"
																				id="chk_branch_service_due_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_service_due_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Service Due&opt=branch_service_due_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_service_due_sms_enable"
																				type="checkbox"
																				id="chk_branch_service_due_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_service_due_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Service Due&opt=branch_service_due_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Service Booking:</td>
																			<td align="center"><input
																				name="chk_branch_service_appointment_email_enable"
																				type="checkbox"
																				id="chk_branch_service_appointment_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_service_appointment_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Service Booking&opt=branch_service_appointment_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_service_appointment_sms_enable"
																				type="checkbox"
																				id="chk_branch_service_appointment_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_service_appointment_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Service Booking&opt=branch_service_appointment_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Pre Owned:</td>
																			<td align="center"><input
																				name="chk_branch_preowned_email_enable"
																				type="checkbox"
																				id="chk_branch_preowned_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_preowned_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Pre Owned&opt=branch_preowned_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_preowned_sms_enable"
																				type="checkbox" id="chk_branch_preowned_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_preowned_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Pre Owned&opt=branch_preowned_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Pre Owned For Executive:</td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Pre Owned For Executive&opt=branch_preowned_email_exe_format">Format</a></td>
																			<td align="center">&nbsp;</td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Pre Owned For Executive&opt=branch_preowned_sms_exe_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">New Job Card:</td>
																			<td align="center"><input
																				name="chk_branch_jc_new_email_enable"
																				type="checkbox" id="chk_branch_jc_new_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_new_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=New Job Card&opt=branch_jc_new_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_jc_new_sms_enable" type="checkbox"
																				id="chk_branch_jc_new_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_new_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=New Job Card&opt=branch_jc_new_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Ready Job Card:</td>
																			<td align="center"><input
																				name="chk_branch_jc_ready_email_enable"
																				type="checkbox"
																				id="chk_branch_jc_ready_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_ready_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Ready Job Card&opt=branch_jc_ready_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_jc_ready_sms_enable"
																				type="checkbox" id="chk_branch_jc_ready_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_ready_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Ready Job Card&opt=branch_jc_ready_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Delivered Job Card:</td>
																			<td align="center"><input
																				name="chk_branch_jc_delivered_email_enable"
																				type="checkbox"
																				id="chk_branch_jc_delivered_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_delivered_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Delivered Job Card&opt=branch_jc_delivered_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_jc_delivered_sms_enable"
																				type="checkbox"
																				id="chk_branch_jc_delivered_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_delivered_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Delivered Job Card&opt=branch_jc_delivered_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Job Card Estimate:</td>
																			<td align="center"><input
																				name="chk_branch_jc_estimate_email_enable"
																				type="checkbox"
																				id="chk_branch_jc_estimate_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_estimate_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Job Card Estimate&opt=branch_jc_estimate_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_jc_estimate_sms_enable"
																				type="checkbox"
																				id="chk_branch_jc_estimate_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_estimate_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Job Card Estimate&opt=branch_jc_estimate_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Job Card Feedback:</td>
																			<td align="center"><input
																				name="chk_branch_jc_feedback_email_enable"
																				type="checkbox"
																				id="chk_branch_jc_feedback_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_feedback_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Job Card Feedback&opt=branch_jc_feedback_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_jc_feedback_sms_enable"
																				type="checkbox"
																				id="chk_branch_jc_feedback_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_jc_feedback_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Job Card Feedback&opt=branch_jc_feedback_sms_format">Format</a></td>
																		</tr>

																		<tr>
																			<td align="right">Insurance New:</td>
																			<td align="center"><input
																				name="chk_branch_insur_new_email_enable"
																				type="checkbox"
																				id="chk_branch_insur_new_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_insur_new_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Insurance New&opt=branch_insur_new_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_insur_new_sms_enable"
																				type="checkbox" id="chk_branch_insur_new_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_insur_new_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Insurance New&opt=branch_insur_new_sms_format">Format</a></td>
																		</tr>

																		<tr>
																			<td align="right">Insurance Lost:</td>
																			<td align="center"><input
																				name="chk_branch_insur_lost_email_enable"
																				type="checkbox"
																				id="chk_branch_insur_lost_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_insur_lost_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Insurance Lost&opt=branch_insur_lost_email_format">Format</a></td>
																			<td align="center"><input
																				name="chk_branch_insur_lost_sms_enable"
																				type="checkbox"
																				id="chk_branch_insur_lost_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_insur_lost_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Insurance Lost&opt=branch_insur_lost_sms_format">Format</a></td>
																		</tr>

																		<tr>
																			<td align="right">Invoice:</td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_invoice_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_invoice_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Invoice Confirmation&opt=branch_invoice_email_format">Format</a></td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_invoice_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_invoice_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Invoice Confirmation&opt=branch_invoice_sms_format">Format</a></td>
																		</tr>
																		<tr>
																			<td align="right">Receipt:</td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_receipt_email_enable"
																				<%=mybean.PopulateCheck(mybean.branch_receipt_email_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&email=yes&status=Receipt Confirmation&opt=branch_receipt_email_format">Format</a></td>
																			<td align="center"><input type="checkbox"
																				name="chk_branch_receipt_sms_enable"
																				<%=mybean.PopulateCheck(mybean.branch_receipt_sms_enable)%>></td>
																			<td align="center"><a
																				href="branch-format.jsp?branch_id=<%=mybean.branch_id%>&sms=yes&status=Receipt Confirmation&opt=branch_receipt_sms_format">Format</a></td>
																		</tr>

																	</table></td>
															</tr>
															<%
																}
															%>
															<tr valign="middle">
																<td align="right">Enable SMS:</td>
																<td colspan="4"><input type="checkbox"
																	name="chk_branch_sms_enable"
																	<%=mybean.PopulateCheck(mybean.branch_sms_enable)%>></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="top">SMS URL</td>
																<td colspan="4"><textarea name="txt_branch_sms_url"
																		cols="70" rows="4" class="form-control"
																		id="txt_branch_sms_url"><%=mybean.branch_sms_url%></textarea>
																</td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Enable Enquiry
																	Escalation:</td>
																<td colspan="4"><input type="checkbox"
																	name="ch_branch_esc_enquiry"
																	<%=mybean.PopulateCheck(mybean.branch_esc_enquiry)%> /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Enable Enquiry
																	Followup Escalation:</td>
																<td colspan="4"><input type="checkbox"
																	name="ch_branch_esc_enquiry_followup"
																	<%=mybean.PopulateCheck(mybean.branch_esc_enquiry_followup)%> /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Enable CRM
																	Followup Escalation:</td>
																<td colspan="4"><input type="checkbox"
																	name="ch_branch_esc_crm_followup"
																	<%=mybean.PopulateCheck(mybean.branch_esc_crm_followup)%> /></td>
															</tr>
															<tr valign="middle">
																<td align="right" valign="middle">Enable Service
																	PSF Followup Escalation:</td>
																<td colspan="4"><input type="checkbox"
																	name="ch_branch_esc_servicepsf_followup"
																	<%=mybean.PopulateCheck(mybean.branch_esc_servicepsf_followup)%> /></td>
															</tr>

															<% if (mybean.status.equals("Update") && !(mybean.branch_entry_by == null) && !(mybean.branch_entry_by.equals(""))) { %>
															<tr valign="middle">
																<td align="right">Entry By:</td>
																<td colspan="4"><%=mybean.unescapehtml(mybean.branch_entry_by)%>
																	<input type="hidden" name="branch_entry_by"
																	value="<%=mybean.branch_entry_by%>"></td>
															</tr>
															<%}%>
															<% if (mybean.status.equals("Update") && !(mybean.entry_date == null) && !(mybean.entry_date.equals(""))) { %>
															<tr valign="middle">
																<td align="right">Entry Date:</td>
																<td colspan="4"><%=mybean.entry_date%> <input
																	type="hidden" name="entry_date"
																	value="<%=mybean.entry_date%>"></td>
															</tr>
															<%}%>
															<% if (mybean.status.equals("Update") && !(mybean.branch_modified_by == null) && !(mybean.branch_modified_by.equals(""))) { %>
															<tr valign="middle">
																<td align="right">Modified By:</td>
																<td colspan="4"><%=mybean.unescapehtml(mybean.branch_modified_by)%>
																	<input type="hidden" name="branch_modified_by"
																	value="<%=mybean.branch_modified_by%>"></td>
															</tr>
															<%}%>
															<% if (mybean.status.equals("Update") && !(mybean.modified_date == null) && !(mybean.modified_date.equals(""))) { %>
															<tr valign="middle">
																<td align="right">Modified Date:</td>
																<td colspan="4"><%=mybean.modified_date%> <input
																	type="hidden" name="modified_date"
																	value="<%=mybean.modified_date%>"></td>
															</tr>
															<%}%>
															<tr align="center">
																<td colspan="4" valign="middle">
																	<%if (mybean.status.equals("Add")) {%> <input
																	name="button" type="submit" class="button" id="button"
																	value="Add Branch"
																	onClick="return SubmitFormOnce(document.form1, this);" />
																	<input type="hidden" name="add_button" value="yes">
																	<%} else if (mybean.status.equals("Update")) {%> <input
																	type="hidden" name="update_button" value="yes">
																	<input name="button" type="submit" class="button"
																	id="button" value="Update Branch"
																	onClick="selectcity(); return SubmitFormOnce(document.form1, this);" />
																	<input name="delete_button" type="submit"
																	class="button" id="delete_button"
																	onClick="return confirmdelete(this)"
																	value="Delete Branch" /> <%}%>
																</td>
															</tr>
															</tbody>
															<!-- ------- ---->
															<tbody>
															</tbody>
														</table></td>
												</tr>
											</table>
										</form>
									</TD>
								</TR>
							</TABLE>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>

	<%@include file="../Library/admin-footer.jsp"%></body>
</HTML>
