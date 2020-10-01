<div id="tabs-2">
                        <table width="100%" border="1" align="center" cellpadding="0" cellspacing="0" class="listtable">
                            <tr>
                            <td colspan="2" align="center"><font color="#ff0000"><b><%=mybean.contact_msg%></b></font></td>
                          </tr>
                          <tr id="customer_display">
                            <td colspan="4" align="center" valign="top"><div id="div_customer_details"><%=mybean.StrCustomerDetails%></div></td>
                          </tr>
                          <tr>
                            <td colspan="4" align="center"><%=mybean.customerdetail%></td>
                          </tr>
                            <tr>
                            <td colspan="4"><form name="Frmcontact"  method="post" action="vehicle-dash.jsp?veh_id=<%=mybean.veh_id%>#tabs-2">
                                <table width="100%" border="1" cellspacing="0" cellpadding="0" class="listtable">
                                <tr>
                                    <th colspan="4" align="center">Add Contact Person</th>
                                  </tr>
                                  
                                <tr>
                                    <td align="right" valign="top">Contact Name<font color="#ff0000">*</font>:</td>
                                    <td colspan="3" valign="top"><table width="520" border="0" cellpadding="0">
                                        <tr>
                                        <td><span style="float:left">
                                          <select name="dr_new_contact_title_id" class="selectbox" id="dr_new_contact_title_id">
                                          <%=mybean.PopulateTitle(mybean.comp_id, mybean.new_contact_title_id)%>
                                        </select>
                                          <br />
                                          Title </span></td>
                                        <td><input name = "txt_new_contact_fname" type = "text" class="textbox" id= "txt_new_contact_fname" value = "<%=mybean.new_contact_fname%>" size="32" maxlength="255"/>
                                            <br />
                                            First Name</td>
                                        <td><input name = "txt_new_contact_lname" type = "text" class="textbox" id= "txt_new_contact_lname" value = "<%=mybean.new_contact_lname%>" size="32" maxlength="255"/>
                                            <br />
                                            Last Name</td>
                                      </tr>
                                      </table></td>
                                  </tr>
                                <tr>
                                    <td align="right" valign="top"> Mobile 1<font color="#ff0000">*</font>:</td>
                                    <td  align="left" valign="top"><input name ="txt_new_contact_mobile1" type="text" class="textbox"  id ="txt_new_contact_mobile1" value = "<%=mybean.new_contact_mobile1%>" size="32" maxlength="13" style="width:250px" onKeyUp="toPhone('txt_new_contact_mobile1','Mobile 1')"/>
                                    <br />
                                    (91-9999999999)
                                   
                                    </td>
                                    <td align="right" valign="top"> Mobile 2:</td>
                                    <td  align="left" valign="top"><input name ="txt_new_contact_mobile2" type="text" class="textbox"  id ="txt_new_contact_mobile2" value = "<%=mybean.new_contact_mobile2%>" size="32" maxlength="13" style="width:250px" onKeyUp="toPhone('txt_new_contact_mobile2','Mobile 2')" />
                                     <br />
                                     (91-9999999999)
                                     </td>
                                  </tr>
                                  <tr>
                               
                                 <td align="right" valign="top"> Email 1:</td>
                                    <td align="left" valign="top" colspan="3"><input name ="txt_new_contact_email1" type="text" class="textbox"  id ="txt_new_contact_email1" value = "<%=mybean.new_contact_email1%>" size="32" maxlength="50" style="width:250px"/></td>
                               </tr>
                                <tr>
                                    <td align="right" valign="top"> Notes:&nbsp; </td>
                                    <td><textarea name="txt_new_contact_notes"  cols="70" rows="4" class="textbox" id="txt_new_contact_notes" ><%=mybean.new_contact_notes%></textarea></td>
                                    <td align="right" valign="top">Type<font color="#ff0000">*</font>:</td>
                                    <td valign="top"><select id="dr_new_contact_contacttype_id" name="dr_new_contact_contacttype_id" class="selectbox">
                                        <%=mybean.PopulateContactType(mybean.comp_id)%>
                                      </select></td>
                                  </tr>
                                
                                    <td colspan="4" align="center" valign="middle"><input name="add_contact_button" type="submit" class="button" id="add_contact_button" value="Add Contact"/></td>
                                  </tr>
                              </table>    
                              </form></td>
                          </tr>
                            
                           <%--  <%if(mybean.customer_edit_perm.equals("1")){%>
                            <tr id="customer_details" style="display:none">
                            <td colspan="4" align="right" valign="top"><table width="100%" cellpadding="0" cellspacing="0" border="1" class="listtable">
                                <tr>
                                <td colspan="4" align="right" valign="top"><a href="javascript:HideCustomerDetails();">Close Update</a></td>
                              </tr>
                                <tr>
                                <td colspan="4" align="right" valign="top"><a href="javascript:window.parent.AddNewContact(<%=mybean.veh_customer_id%>);" id="new_contact_link">Add New Contact...</a></td>
                              </tr>
                                <tr>
                                <th colspan="4" align="center" valign="top">Customer Details</th>
                              </tr>
                                <tr>
                                <td align="right" valign="top">Customer<font color="#ff0000">*</font>:</td>
                                <td colspan="3" align="left" valign="top"><input name="txt_veh_customer_name" type="text" class="textbox"  id ="txt_veh_customer_name" value = "<%=mybean.veh_customer_name%>" size="70" maxlength="255" onChange="SecurityCheck('txt_veh_customer_name',this,'hint_txt_veh_customer_name')"/>
                                    <div class="hint" id="hint_txt_veh_customer_name"></div></td>
                              </tr>
                                <tr valign="center">
                                <td align="right" valign="top">Contact<font color="#ff0000">*</font>:</td>
                                <td colspan="3" valign="top"><table width="520" border="0" cellpadding="0">
                                    <tr>
                                    <td><span style="float:left">
                                      <select name="dr_title" class="selectbox" id="dr_title" onchange="SecurityCheck('dr_title',this,'hint_dr_title')">
                                      <%=mybean.PopulateTitle(mybean.comp_id, mybean.contact_title_id)%>
                                    </select>
                                      <br />
                                      Title </span>
                                        <div class="hint" id="hint_dr_title"></div></td>
                                    <td><input name = "txt_contact_fname" type = "text" class="textbox" id= "txt_contact_fname" value = "<%=mybean.contact_fname%>" size="32" maxlength="255" onchange="SecurityCheck('txt_contact_fname',this,'hint_txt_contact_fname')" />
                                        <br />
                                        First Name
                                        <div class="hint" id="hint_txt_contact_fname"></div></td>
                                    <td><input name = "txt_contact_lname" type = "text" class="textbox" id= "txt_contact_lname" value = "<%=mybean.contact_lname%>" size="32" maxlength="255"  onchange="SecurityCheck('txt_contact_lname',this,'hint_txt_contact_lname')" />
                                        <br />
                                        Last Name
                                        <div class="hint" id="hint_txt_contact_lname"></div></td>
                                  </tr>
                                  </table></td>
                              </tr>
                                <tr valign="center">
                                <td align="right" valign="top"> Mobile 1<font color="#ff0000">*</font>:</td>
                                <td  align="left" valign="top"><input name ="txt_contact_mobile1" type="text" class="textbox"  id ="txt_contact_mobile1" value = "<%=mybean.contact_mobile1%>" size="32" maxlength="13" style="width:250px"  onchange="SecurityCheck('txt_contact_mobile1',this,'hint_txt_contact_mobile1')" />
                                    <br />
                                    (91-9999999999)
                                    <div class="hint" id="hint_txt_contact_mobile1"></div></td>
                                <td align="right" valign="top"> Mobile 2:</td>
                                <td  align="left" valign="top"><input name ="txt_contact_mobile2" type="text" class="textbox"  id ="txt_contact_mobile2" value = "<%=mybean.contact_mobile2%>" size="32" maxlength="13" style="width:250px"  onchange="SecurityCheck('txt_contact_mobile2',this,'hint_txt_contact_mobile2')" />
                                    <br />
                                    (91-9999999999)
                                    <div class="hint" id="hint_txt_contact_mobile2"></div></td>
                              </tr>
                                <tr valign="center">
                                <td align="right" valign="top"> Phone 1<font color="#ff0000">*</font>:</td>
                                <td  align="left" valign="top"><input name = "txt_contact_phone1" type = "text" class="textbox" id= "txt_contact_phone1" value = "<%=mybean.contact_phone1%>" size="35" maxlength="14" onchange="SecurityCheck('txt_contact_phone1',this,'hint_txt_contact_phone1')" />
                                    <br />
                                    (91-80-33333333)
                                    <div class="hint" id="hint_txt_contact_phone1"></div></td>
                                <td align="right" valign="top"> Phone 2:</td>
                                <td  align="left" valign="top"><input name = "txt_contact_phone2" type = "text" class="textbox" id= "txt_contact_phone2" value = "<%=mybean.contact_phone2%>" size="35" maxlength="14" onchange="SecurityCheck('txt_contact_phone2',this,'hint_txt_contact_phone2')" />
                                    <br />
                                    (91-80-33333333)
                                    <div class="hint" id="hint_txt_contact_phone2"></div></td>
                              </tr>
                                <tr valign="center">
                                <td align="right" valign="top">Email 1<font color="#ff0000">*</font>:</td>
                                <td align="left" valign="top" colspan="3"><input name = "txt_contact_email1" type = "text" class="textbox" id= "txt_contact_email1" value = "<%=mybean.contact_email1%>" size="35" maxlength="100" style="width:250px"  onchange="SecurityCheck('txt_contact_email1',this,'hint_txt_contact_email1')" />
                                    <div class="hint" id="hint_txt_contact_email1"></div></td>
                              </tr>
                                <tr>
                                <td rowspan="2" align="right" valign="top">Address:</td>
                                <td  align="left" valign="top" rowspan="2"><textarea name="txt_contact_address" cols="40" rows="4" class="textbox" id="txt_contact_address"  onchange="SecurityCheck('txt_contact_address',this,'hint_txt_contact_address')" onkeyup="charcount('txt_contact_address', 'span_txt_contact_address','&lt;font color=red&gt;({CHAR} characters left)&lt;/font&gt;', '255')"   style="width:250px"><%=mybean.contact_address%></textarea>
                                    <br />
                                    <span id="span_txt_contact_address"> (255 Characters)</span>
                                    <div class="hint" id="hint_txt_contact_address"></div></td>
                                <td align="right" valign="top">City<font color="#ff0000">*</font>:</td>
                                <td align="left" valign="top"><select name="dr_city_id" id="dr_city_id" class="selectbox"  onchange="SecurityCheck('dr_city_id',this,'hint_dr_city_id')" >
                                    <%=mybean.PopulateCity(mybean.comp_id)%>
                                  </select>
                                    <div class="hint" id="hint_dr_city_id"></div></td>
                              </tr>
                                <tr valign="center">
                                <td align="right" valign="top">Pin/Zip<font color="#ff0000">*</font>:</td>
                                <td align="left" valign="top"><input name ="txt_contact_pin" type="text" class="textbox" id="txt_contact_pin" onkeyup="toInteger('txt_contact_pin','Pin')" onchange="SecurityCheck('txt_contact_pin',this,'hint_txt_contact_pin')" value="<%=mybean.contact_pin%>" size="10" maxlength="6"/>
                                    <div class="hint" id="hint_txt_contact_pin"></div>
                                    <input name="veh_id" type="hidden" id="veh_id" value="<%=mybean.veh_id%>" /></td>
                              </tr>
                              </table></td>
                          </tr>
                            <%}%> --%>
                            <%-- <tr>
                            <td colspan="4" align="center"><%=mybean.customerdetail%></td>
                          </tr> --%>
                          </table>
                      </div>
                        