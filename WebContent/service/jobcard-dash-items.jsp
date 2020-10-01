<%@ page errorPage="../portal/error-page.jsp" %>
<jsp:useBean id="mybean" class="axela.service.JobCard_Dash_Items" scope="request"/>
<%mybean.doPost(request,response); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title><%=mybean.AppName%> - <%=mybean.GetSession("comp_name", request)%></title>
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="expires" content="mon, 06 jan 1990 00:00:01 gmt"/>
<%-- <%@include file="../Library/css.jsp"%>    --%>


</HEAD>
				<body onload="list_jcitems();"  leftmargin="0" rightmargin="0" topmargin="0" bottommargin="0">
                <%@include file="../portal/header.jsp" %>
                <%@include file="../Library/jobcard-dash.jsp" %><TABLE cellSpacing="0" cellPadding="0" width="98%" height="300" border="0" align="center">
<!--tr>

                                <td width="30%" align="right"><a href="../sales/quote-update.jsp?add=yes&jc_id=<%=mybean.jc_id%>" >Add
                                        New Parts...</a><br>
                                </td>
                            </tr>
                            <tr><td>&nbsp;</td></tr-->
                  <TR>
                    <!--TD height="300" align="center" vAlign="top"><%//=mybean.StrHTML%></TD-->
                    <td width="50%" height="300" align="center" valign="top" scope="col">
                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="listtable">
                    <tr>
                      <td scope="col"><div id="jcitem_details"></div></td>
                    </tr>
                    </table>
                    </td>
                    <td width="50%" align="center" valign="top" scope="col">
                    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="listtable">
                    <tr>
                      <td valign="top" align=left>Search Items:
                        <input type="hidden" name="txt_rateclass_id	" id="txt_rateclass_id	" value="<%=mybean.rateclass_id%>"/>
                        <input type="hidden" name="jcitem_id" id="jcitem_id"/>
                        <input type="hidden" name="txt_jc_id" id="txt_jc_id" value="<%=mybean.jc_id%>"/>
                        <input name ="txt_search" type="text" class="textbox" id="txt_search" value="" size="30" maxlength="255" onKeyUp="ItemSearch();"/>
                        <div class="admin-master"><a href="../inventory/inventory-item-list.jsp?all=yes" title="Manage Item"></a></div>
                       </td>
                    </tr>
                    <tr>
                      <td valign="top" align=left><div class="hint" id="hint_search_item"> Enter your search parameter! </div></td>
                    </tr>
                    <tr>
                      <td valign="top" ><table border="1" cellspacing="0" cellpadding="0" align="center" class="listtable">
                          <tr>
                            <th align="center" colspan="6">Item Details</th>
                          </tr>
                          <tr>
                           <td colspan="6" align="center" valign="top"><div style="display:inline" id="item_name"> </div>
                           <input name="txt_item_id" type="hidden" id="txt_item_id"/>
                           <input type="hidden" name="txt_current_qty" id="txt_current_qty"/>                          </td>
                              
                            
                          </tr>
                          <tr>
                            <td align="center" valign="top">Quantity:
                              <input name="txt_item_qty" type="text" class="textbox" id="txt_item_qty"  size="10" maxlength="10" onKeyUp="CalItemTotal();"/>
                              <input type="hidden" id="emp_jc_priceupdate" name="emp_jc_priceupdate" value="<%=mybean.emp_jc_priceupdate%>"/>
                              <input type="hidden" id="jc_location_id" name="jc_location_id" value="<%=mybean.jc_location_id%>"/>
                              <input type="hidden" id="emp_jc_discountupdate" name="emp_jc_discountupdate" value="<%=mybean.emp_jc_discountupdate%>"/>
                              <input type="hidden" id="txt_item_baseprice" name="txt_item_baseprice"/>
                              <input type="hidden" name="txt_status" id="txt_status" value="<%=mybean.status%>"/>
                              <input type="hidden" id="txt_mode" name="txt_mode"/>
                              <input type="hidden" id="txt_itemprice_updatemode" name="txt_itemprice_updatemode"/>
                              <input type="hidden" id="txt_item_type_id" name="txt_item_type_id"/>
                              <input type="hidden" id="txt_item_ticket_dept_id" name="txt_item_ticket_dept_id">
                              <input type="hidden" id="txt_item_pricevariable" name="txt_item_pricevariable"/>
                              <input type="hidden" id="txt_serial" name="txt_serial"/>
                              <input type="hidden" id="txt_type" name="txt_type"/>
                             
                            </td>
                            <td align="center" valign="top">Price:
                              <input name="txt_item_price" type="text" class="textbox" id="txt_item_price" size="10" maxlength="10"  onKeyUp="CalItemTotal();" onChange="CheckBasePrice();"/></td>
                            <td align="center" valign="top">Discount:
                              <input name="txt_item_disc" type="text" class="textbox" id="txt_item_disc" size="10" maxlength="10"  onKeyUp="CalItemTotal();"/></td>
                            <td align="center" valign="top" nowrap><input name="txt_item_tax" type="hidden" id="txt_item_tax" />
                              <input name="txt_item_tax_id" type="hidden" id="txt_item_tax_id" />
                              <div style="display:inline" id="tax_name">Tax</div>
                              : <br/>
                              <div style="display:inline" id="item_tax">0</div></td>
                              
                            <td align="center" valign="top"><input name="txt_item_total" type="hidden" id="txt_item_total" />
                              <b>Total: <br/>
                              <div style="display:inline" id="item_total">0</div>
                              </b></td>
                            <td align="center" valign="top"><div id="mode_button">
                                <input name="add_button" id="add_button" type="button" class="button" value="Add" onClick="Addjcitem();"/>
                              </div></td>
                          </tr>
                          <tr id="serial_details" style="display:none">
                                    <td align="right" valign="top" nowrap>Serial No.<font color="#ff0000">*</font>:</td>
                                    <td align="left" valign="top" colspan="5"><input name="txt_item_serial" type="text" class="textbox" id="txt_item_serial" size="20" maxlength="30"/></td>
                                    </tr><tr>
                                    <td colspan="6">
                                    <span id="configure-details"></span>
                                    </td>
                                  </tr>
                        </table></td>
                    </tr>
                  </table>
                    </td>
                  </TR>
              </TABLE> 
<%--               <%@include file="../Library/js.jsp"%> --%>
<%--               <%@include file="../Library/admin-footer.jsp" %> --%>
              <script type="text/javascript" src="../Library/sc.js"></script>
              </body>
</HTML>
</HEAD>
