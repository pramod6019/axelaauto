package axela.service;
//Bhagwan 11th Feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Dash_Customer_Info extends Connect {

    public String emp_id = "0";
    public String comp_id = "0";
    public String msg = "";
    public String enquiry_id = "0";
    public String enquiry_title = "";
    public String enquiry_enquirytype_id = "";
    public String BranchAccess = "";
    public String ExeAccess = "";
    public String StrSql = "";
    public String customer_name = "";   
    public String enquiry_customer_id = "0";
    public String veh_customer_id = "0";
    public String customer_communication = "";
    public String customer_address = "";
    public String customer_landmark = "";
    public String customer_notes = "";
    public String customer_active = "";
    public String customer_exe = "";
    public String group = "";
    public String config_sales_enquiry = "";
    public String comp_email_enable = "";
    public String config_email_enable = "";
    public String comp_sms_enable = "";
    public String config_sms_enable = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
             emp_id = CNumeric(GetSession("emp_id", request));
            BranchAccess = GetSession("BranchAccess", request);
            ExeAccess = GetSession("ExeAccess", request);
            CheckPerm(comp_id, "emp_opportunity_access", request, response);
            enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
            group = PadQuotes(request.getParameter("group"));
            StrSql = "select enquiry_title, enquiry_enquirytype_id"
                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
                    + " inner join " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
                    + " where 1=1 and enquiry_id =" + enquiry_id + BranchAccess + ExeAccess + ""
                    + " group by enquiry_id "
                    + " order by enquiry_id desc ";
            CachedRowSet crs = processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    enquiry_title = crs.getString("enquiry_title");
                    enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
                }
                CustomerDetails(response);
            } else {
                response.sendRedirect("../portal/error.jsp?msg=Invalid Opportunity!");
            }
            crs.close();
        }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void CustomerDetails(HttpServletResponse response) {
        try {
//            StrSql = " select enquiry_customer_id, enquiry_title, customer_name, "
//                    + " customer_address, customer_landmark, "
//                    + " customer_phone1,customer_phone2, customer_phone3, customer_phone4,"
//                    + " customer_mobile1, customer_mobile2, "
//                    + " customer_fax1, customer_fax2, "
//                    + " customer_email1, customer_email2, customer_emp_id, "
//                    + " customer_website1, customer_website2, customer_pin, customer_notes, customer_active, "
//                    + " coalesce(city_name,'') as city_name, emp_name, "
//                    + " emp_id, concat(emp_name,' (', emp_ref_no, ')') as customer_exe "
//                    + " from " + compdb(comp_id) + "axela_sales_enquiry "
//                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id "
//                    + " inner join " + compdb(comp_id) + "axela_customer on customer_id=enquiry_customer_id "
//                    + " inner join " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
//                    + " left join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
//                    + " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id "
//                    + " where enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + " group by customer_id ";

            StrSql = " select veh_customer_id, veh_reg_no, customer_name, "
                    + " customer_address, customer_landmark, "
                    + " customer_phone1,customer_phone2, customer_phone3, customer_phone4,"
                    + " customer_mobile1, customer_mobile2, "
                    + " customer_fax1, customer_fax2, "
                    + " customer_email1, customer_email2, customer_emp_id, "
                    + " customer_website1, customer_website2, customer_pin, customer_notes, customer_active, "
                    + " coalesce(city_name,'') as city_name"
//                    + " emp_name, emp_id, concat(emp_name,' (', emp_ref_no, ')') as customer_exe "
                    + " from " + compdb(comp_id) + "axela_service_veh"   
                    + " inner join " + compdb(comp_id) + "axela_customer on customer_id = veh_customer_id "
                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
//                    + " inner join " + compdb(comp_id) + "axela_emp on emp_id = veh_emp_id "
                    + " left join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
                    + " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id "
                    + " where veh_id = " + veh_customer_id + BranchAccess + ExeAccess + " group by customer_id ";
//            SOP("StrSql--"+StrSql);
            CachedRowSet crs = processQuery(StrSql, 0);  
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    //enquiry_title = crs.getString("enquiry_title");
                    veh_customer_id = crs.getString("veh_customer_id");
                    customer_name = crs.getString("customer_name");
                    if (!crs.getString("customer_phone1").equals("")) {
                        customer_communication = SplitPhoneNo(crs.getString("customer_phone1"), 4, "T") + "";
                    }
                    if (!crs.getString("customer_phone2").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_phone2"), 4, "T") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_phone2"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_phone3").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_phone3"), 4, "T") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_phone3"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_phone4").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_phone4"), 4, "T") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_phone4"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_mobile1").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_mobile1"), 5, "M") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_mobile1"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_mobile2").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_mobile2"), 5, "M") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_mobile2"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_fax1").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_fax1"), 4, "F") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_fax1"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_fax2").equals("")) {
                        if (!customer_communication.equals("")) {
                            customer_communication = customer_communication + "<br>" + SplitPhoneNo(crs.getString("customer_fax2"), 4, "F") + "";
                        } else {
                            customer_communication = SplitPhoneNo(crs.getString("customer_fax2"), 4, "T") + "";
                        }
                    }
                    if (!crs.getString("customer_email1").equals("")) {
                        customer_communication = customer_communication + "<br><a href=mailto:" + crs.getString("customer_email1") + ">" + crs.getString("customer_email1") + "</a>";
                    }
                    if (!crs.getString("customer_email2").equals("")) {
                        customer_communication = customer_communication + "<br><a href=mailto:" + crs.getString("customer_email2") + ">" + crs.getString("customer_email2") + "</a>";
                    }
                    if (!crs.getString("customer_website1").equals("")) {
                        customer_communication = customer_communication + "<br><a href=http://" + crs.getString("customer_website1") + " target=_blank>" + crs.getString("customer_website1") + "</a>";
                    }
                    if (!crs.getString("customer_website2").equals("")) {
                        customer_communication = customer_communication + "<br><a href=http://" + crs.getString("customer_website2") + " target=_blank>" + crs.getString("customer_website2") + "</a>";
                    }

                    if (!crs.getString("customer_address").equals("")) {
                        customer_address = crs.getString("customer_address");
                        if (crs.getString("city_name") != null) {
                            customer_address = customer_address + ", " + crs.getString("city_name");
                        }
                        if (!crs.getString("customer_pin").equals("")) {
                            customer_address = customer_address + " - " + crs.getString("customer_pin");
                        }
                    }
                    if (!crs.getString("customer_landmark").equals("")) {
                        customer_landmark = crs.getString("customer_landmark");
                    }
                    if (!crs.getString("customer_exe").equals("")) {
                        customer_exe = "<a href=../portal/executive-summary.jsp?emp_id=" + crs.getInt("emp_id") + ">" + crs.getString("customer_exe") + "</a>";
                    }
                    if (crs.getString("customer_active").equals("0")) {
                        customer_active = "<font color=red><b>Inactive</b></font>";
                    } else {
                        customer_active = "Active";
                    }
                    customer_notes = crs.getString("customer_notes");
                }
            } else {
                msg = "<br><br>No Customer found!";
                // response.sendRedirect(response.encodeRedirectURL("enquiry-dash-customer.jsp?enquiry_id="+enquiry_id+"&msg=No Customer found!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String ListContact(String enquiry_customer_id) {
        StringBuilder Str = new StringBuilder();
        int count = 0;
        String active = "";
        String address = "";
        try {
            StrSql = "SELECT contact_id, contact_customer_id, CONCAT(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
                    + " contact_jobtitle, contact_company, contact_phone1,"
                    + " contact_phone2, contact_mobile1, contact_mobile2, contact_anniversary,"
                    + " contact_email1, contact_email2, contact_yahoo, contact_msn, contact_aol, contact_address,"
                    + " contact_pin, contact_landmark, contact_dob, contact_active,"
                    + " coalesce(city_name,'') as city_name,"
                    + " customer_id, customer_name, coalesce(branch_name,'')as branch, coalesce(branch_code,'')as branch_code"
                    + " from " + compdb(comp_id) + "axela_customer_contact"
                    + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
                    + " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id= customer_branch_id"
                    + " left join " + compdb(comp_id) + "axela_city on city_id = contact_city_id"
                    + " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id"
                    + " where 1 = 1 and contact_customer_id = " + enquiry_customer_id + BranchAccess;
//            SOP("StrSql--"+StrSql);
            CachedRowSet crs = processQuery(StrSql, 0);
            Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
            Str.append("<tr align=center>\n");
            Str.append("<th width=5%>#</th>\n");
            Str.append("<th>ID</th>\n");
            Str.append("<th>Contact Person</th>\n");
            Str.append("<th>Communication</th>\n");
            Str.append("<th>Address</th>\n");
            Str.append("<th width=15%>Actions</th>\n");
            Str.append("</tr>\n");
            while (crs.next()) {
                count = count + 1;
                if (crs.getString("contact_active").equals("0")) {
                    active = "<font color=red><b>&nbsp;[Inactive]</b></font>";
                } else {
                    active = "";
                }
                Str.append("<tr>\n");
                Str.append("<td valign=top align=center >").append(count).append("</td>\n");
                Str.append("<td valign=top align=center nowrap><a href=../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append(">");
                Str.append(crs.getString("contact_id")).append("</a>");
                Str.append("</td>");
                Str.append("<td valign=top align=left>");
                Str.append("").append(crs.getString("contact_name")).append(active);
                if (!crs.getString("contact_jobtitle").equals("")) {
                    Str.append("<br>").append(crs.getString("contact_jobtitle"));
                }
                if (!crs.getString("contact_company").equals("")) {
                    Str.append("<br>").append(crs.getString("contact_company"));
                }
                Str.append("</td><td valign=top align=left nowrap>");
                if (!crs.getString("contact_phone1").equals("")) {
                    Str.append("").append(crs.getString("contact_phone1")).append("<br>");
                }
                if (!crs.getString("contact_phone2").equals("")) {
                    Str.append("").append(crs.getString("contact_phone2")).append("<br>");
                }
                if (!crs.getString("contact_mobile1").equals("")) {
                    Str.append("").append(crs.getString("contact_mobile1")).append("<br>");
                }
                if (!crs.getString("contact_mobile2").equals("")) {
                    Str.append("").append(crs.getString("contact_mobile2")).append("<br>");
                }
                if (!crs.getString("contact_email1").equals("")) {
                    Str.append("" + "<a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a><br>");
                }
                if (!crs.getString("contact_email2").equals("")) {
                    Str.append("" + "<a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a><br>");
                }
                if (!crs.getString("contact_yahoo").equals("")) {
                    Str.append("" + "<a href=mailto:").append(crs.getString("contact_yahoo")).append(">").append(crs.getString("contact_yahoo")).append("</a><br>");
                }
                if (!crs.getString("contact_msn").equals("")) {
                    Str.append("" + "<a href=mailto:").append(crs.getString("contact_msn")).append(">").append(crs.getString("contact_msn")).append("</a><br>");
                }
                if (!crs.getString("contact_aol").equals("")) {
                    Str.append("" + "<a href=mailto:").append(crs.getString("contact_aol")).append(">").append(crs.getString("contact_aol")).append("</a><br>");
                }
                Str.append("</td><td valign=top align=left>");
                address = crs.getString("contact_address");
                if (!address.equals("")) {
                    address = crs.getString("contact_address");
                    if (!crs.getString("city_name").equals("")) {
                        address = address + ", " + crs.getString("city_name");
                    }
                    address = address + " - " + crs.getString("contact_pin");
                    if (!crs.getString("contact_landmark").equals("")) {
                        address = address + "<br>Landmark: " + crs.getString("contact_landmark");
                    }
                }
                Str.append(address);
                Str.append("</td>\n");
                Str.append("<td valign=top align=left nowrap>");
                if (group.equals("selectcontact")) {
                    Str.append("<a href=# onClick=\"javascript:SelectCompanyContact1(").append(crs.getString("contact_id")).append(", ").append(crs.getString("contact_customer_id")).append(", '").append(crs.getString("customer_name")).append("', '").append(crs.getString("contact_name")).append("', 'prop_contact_id', 'Contact', 'span_prop_contact_id');\">Select Contact</a>\n");
                } else if (group.equals("select_quote_contact")) {
                    Str.append("<a href=# onClick=\"javascript:SelectQuoteContact(").append(crs.getString("contact_id")).append(");\">Select Contact</a>\n");
                } else if (group.equals("select_so_contact")) {
                    Str.append("<a href=# onClick=\"javascript:SelectSoContact(").append(crs.getString("contact_id")).append(");\">Select Contact</a>\n");
                } else {
                    Str.append("<a href=\"../customer/customer-contact-update.jsp?update=yes&customer_id=").append(crs.getString("customer_id")).append("&contact_id=").append(crs.getString("contact_id")).append(" \">Update Contact Person</a><br>");
                    if (config_sales_enquiry.equals("1")) {
                        Str.append("<a href=\"../sales/enquiry-quickadd.jsp?Add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Opportunity</a><br>");
                    }
                    Str.append("<a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Ticket</a><br>");
                    if (comp_email_enable.equals("1") && config_email_enable.equals("1")) {
                        Str.append("<a href=../portal/email-send.jsp?contact_id=").append(crs.getString("contact_id")).append(">Send Email</a><br>");
                    }
                    if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
                        Str.append("<a href=../portal/sms-send.jsp?contact_id=").append(crs.getString("contact_id")).append(">Send SMS</a><br>");
                    }
                    Str.append(" <br><br>\n");
                }
                Str.append("</td>");
                Str.append("</tr>");
            }
            Str.append("</table>\n");
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
        return Str.toString();
    }

    protected void PopulateConfigDetails() {

        StrSql = "SELECT coalesce(comp_email_enable,'') as comp_email_enable,"
                + " coalesce(comp_sms_enable,'') as comp_sms_enable,"
                + " coalesce(config_email_enable,'') as config_email_enable,"
                + " coalesce(config_sms_enable,'') as config_sms_enable,"
                + " coalesce(config_sales_enquiry,'') as config_sales_enquiry,"
                + " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
                + " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id = " + emp_id;
//        SOP(StrSqlBreaker(StrSql));
        try {
            CachedRowSet crs = processQuery(StrSql, 0);
            while (crs.next()) {
                comp_email_enable = crs.getString("comp_email_enable");
                comp_sms_enable = crs.getString("comp_sms_enable");
                config_email_enable = crs.getString("config_email_enable");
                config_sms_enable = crs.getString("config_sms_enable");
                config_sales_enquiry = crs.getString("config_sales_enquiry");
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
