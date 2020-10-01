package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Sms_Contact_Send extends Connect {

    public String sms_id = "0";
    public String sms_mobileno = "";
    public String sms_msg = "";
    public String sms_date = "";
    public String sms_sent = "";
    public String sms_customer = "";
    public String sms_entry_id = "0";
    public String emp_id = "0";
    public String comp_id = "0";
    public String student_branch_id = "0", branch_id = "0", branchfilter = "";
    public String status = "";
    public String sendB = "";
    public String msg = "";
    public String customer_id = "0", customer_contact_id = "0", StrSql = "";
    public String BranchAccess = "";
    public String comp_sms_enable = "";
    public String StrSearch = "";
    public String[] group_ids;
    public String group_id = "0";
    public String sendto = "";
    public String sms_allgroup = "";
    public String allconn = "";
    public String cont = "";
    public String contkey = "";
    public String smartcont = "";
    public String tag = "";
    public String smartcontkey = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                CheckPerm(comp_id, "emp_sms_send", request, response);
                branch_id = CNumeric(GetSession("emp_branch_id", request));
                BranchAccess = GetSession("BranchAccess", request);
                allconn = PadQuotes(request.getParameter("allconn"));
                cont = PadQuotes(request.getParameter("cont"));
                contkey = PadQuotes(request.getParameter("contkey"));
                smartcont = PadQuotes(request.getParameter("smartcont"));
                tag = PadQuotes(request.getParameter("tag"));
                smartcontkey = PadQuotes(request.getParameter("smartcontkey"));
                customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
                customer_contact_id = CNumeric(PadQuotes(request.getParameter("customer_contact_id")));
                sms_allgroup = PadQuotes(request.getParameter("chk_sms_allgroup"));

                if (cont.equals("yes")) {
                    sendto = "Contact";
                } else if (contkey.equals("yes")) {
                    sendto = "Contact Key Persons";
                } else if (smartcont.equals("yes")) {
                    sendto = "Contact";
                } else if (smartcontkey.equals("yes")) {
                    sendto = "Contact Key Persons";
                } else {
                    sendto = "Contact and Key Persons";
                }
                if ("yes".equals(smartcont)) {
                    if (!GetSession("PrintSearchStr", request).equals("")) {
                        StrSearch = StrSearch + GetSession("" + tag + "PrintSearchStr",request);
                    }
                }
                if ("yes".equals(smartcontkey)) {
                    if (!GetSession("PrintSearchkeyStr", request).equals("")) {
                        StrSearch = StrSearch + GetSession("PrintSearchkeyStr", request);
                    }
                }
                if (sms_allgroup.equals("on")) {
                    sms_allgroup = "1";
                } else {
                    sms_allgroup = "0";
                }

                 StrSql = "select comp_sms_enable "
                        + " from " + compdb(comp_id) + "axela_comp "
                        + " where comp_active = '1'";
                CachedRowSet crs =processQuery(StrSql, 0);
                while (crs.next()) {
                    comp_sms_enable = crs.getString("comp_sms_enable");
                }
                crs.close();
                if (comp_sms_enable.equals("0")) {
                    response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
                }
                sendB = PadQuotes(request.getParameter("send_button"));
                msg = PadQuotes(request.getParameter("msg"));
                sms_id = CNumeric(PadQuotes(request.getParameter("sms_id")));

                if (!"Send".equals(sendB)) {
                    sms_mobileno = "";
                    sms_msg = "";
                    sms_date = "";
                    sms_sent = "";
                    sms_entry_id = "";
                } else {
                    CheckPerm(comp_id, "emp_sms_send", request, response);
                    GetValues(request, response);
                    sms_entry_id = CNumeric(GetSession("emp_id", request));
                    sms_date = ToLongDate(kknow());
                    CheckForm();
                    if (!msg.equals("")) {
                        msg = "Error!" + msg;
                    } else {
                        CheckForm();
                        if (msg.equals("")) {
                            ListStudents();
                        }
                        if (msg.equals("")) {
                            if (cont.equals("yes")) {
                                response.sendRedirect(response.encodeRedirectURL("sms-contact-send.jsp?customer_id=" + customer_id + "&cont=yes&msg=SMS sent successfully!"));
                            } else if (contkey.equals("yes")) {
                                response.sendRedirect(response.encodeRedirectURL("sms-contact-send.jsp?customer_contact_id=" + customer_contact_id + "&contkey=yes&msg=SMS sent successfully!"));

                            } else if (smartcont.equals("yes")) {
                                response.sendRedirect(response.encodeRedirectURL("sms-contact-send.jsp?tag=" + tag + "&smartcont=yes&msg=SMS sent successfully!"));

                            } else if (smartcontkey.equals("yes")) {
                                response.sendRedirect(response.encodeRedirectURL("sms-contact-send.jsp?smartcontkey=yes&msg=SMS sent successfully!"));

                            } else {
                                response.sendRedirect(response.encodeRedirectURL("sms-contact-send.jsp?chk_sms_allgroup=on&allconn=yes&msg=SMS sent successfully!"));
                            }
                        }
                    }
                }
            }
            
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void CheckForm() {
        msg = "";
        if (customer_id.equals("") && cont.equals("yes")) {
            msg = msg + "<br>Enter Contact ID!";
        }
        if (customer_contact_id.equals("") && contkey.equals("yes")) {
            msg = msg + "<br>Enter Contact Key ID!";
        }
        if (allconn.equals("yes")) {
            if (sms_allgroup.equals("0") && group_ids == null) {
                msg = msg + "<br>Select group!";
            }
        }
        if (sms_msg.equals("")) {
            msg = msg + "<br>Enter Message!";
        }
        if (group_ids != null) {
            group_id = "";
            for (int i = 0; i < group_ids.length; i++) {
                group_id = group_id + group_ids[i] + ",";
            }
            StrSearch = " and trans_group_id in (" + group_id.substring(0, group_id.lastIndexOf(",")) + ")";
        }
    }

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        try {
            sms_id = PadQuotes(request.getParameter("sms_id"));
            customer_id = PadQuotes(request.getParameter("customer_id"));
            customer_contact_id = PadQuotes(request.getParameter("customer_contact_id"));
            group_ids = request.getParameterValues("dr_group");
            sms_mobileno = PadQuotes(request.getParameter("txt_sms_mobileno"));
            sms_msg = PadQuotes(request.getParameter("txt_sms_msg"));
            sms_allgroup = PadQuotes(request.getParameter("chk_sms_allgroup"));
            if (sms_allgroup.equals("on")) {
                sms_allgroup = "1";
            } else {
                sms_allgroup = "0";
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void ListStudents() {
        if (sms_msg.length() > 500) {
            sms_msg = sms_msg.substring(0, 499);
        }
        try {
            sms_msg = "replace('" + sms_msg + "','[NAME]',customer_name)";
            if (!customer_id.equals("") && cont.equals("yes")) {
                StrSql = "SELECT "
                        + " " + emp_id + ", "
                        + " customer_mobile1, "
                        + " " + sms_msg + ", "
                        + " '" + ToLongDate(kknow()) + "', "
                        + " customer_id, "
                        + " 0, "
                        + " 0, "
                        + " " + emp_id + " "
                        + " from " + compdb(comp_id) + "axela_customer "
                        + " left  join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
                        + " inner join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
                        + " where customer_active = '1' and customer_mobile1!='' ";
                if (!customer_id.equals("")) {
                    StrSearch = StrSearch + customer_id;
                }
                StrSql = StrSql + StrSearch;
                StrSql = StrSql + " group by customer_id order by customer_id desc ";
                AddFields(StrSql);
            }
            if (!customer_contact_id.equals("") && contkey.equals("yes") || smartcontkey.equals("yes")) {
                StrSql = "SELECT "
                        + " customer_mobile1, "
                        + " " + sms_msg + ", "
                        + " '" + ToLongDate(kknow()) + "', "
                        + " customer_id, "
                        + " 0, "
                        + " 0, "
                        + " " + emp_id + " "
                        + " from " + compdb(comp_id) + "axela_customer_key "
                        + " left  join " + compdb(comp_id) + "axela_customer on customer_contact_id = customer_id "
                        + " inner join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
                        + " left  join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
                        + " where customer_active = '1' and customer_mobile1!='' ";
                if (!customer_contact_id.equals("")) {
                    StrSearch = StrSearch + customer_contact_id;
                }
                StrSql = StrSql + StrSearch;
                StrSql = StrSql + " group by customer_id order by customer_id desc ";
                AddFields(StrSql);
            }
            if (allconn.equals("yes") || smartcont.equals("yes")) {
                StrSql = "SELECT "
                        + " customer_mobile1, "
                        + " " + sms_msg + ", "
                        + " '" + ToLongDate(kknow()) + "', "
                        + " customer_id, "
                        + " 0, "
                        + " 0, "
                        + " " + emp_id + " "
                        + " from " + compdb(comp_id) + "axela_customer "
                        + " left  join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
                        + " inner join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
                        + " where customer_active = '1' and customer_mobile1!='' ";
                StrSql = StrSql + StrSearch;
                StrSql = StrSql + " group by customer_id order by customer_id desc ";
                AddFields(StrSql);

                StrSql = "SELECT "
                        + " customer_mobile1, "
                        + " " + sms_msg + ", "
                        + " '" + ToLongDate(kknow()) + "', "
                        + " customer_id, "
                        + " 0, "
                        + " 0, "
                        + " " + emp_id + " "
                        + " from " + compdb(comp_id) + "axela_customer_key "
                        + " left  join " + compdb(comp_id) + "axela_customer on customer_contact_id = customer_id "
                        + " inner join " + compdb(comp_id) + "axela_customer_group_trans on trans_customer_id = customer_id "
                        + " left  join " + compdb(comp_id) + "axela_city on city_id = customer_city_id "
                        + " where ccount_active = '1' and customer_mobile1!='' ";
                StrSql = StrSql + StrSearch;
                StrSql = StrSql + " group by customer_id order by customer_id desc ";
                AddFields(StrSql);
            }
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void AddFields(String Sql) {
        if (msg.equals("")) {
            try {
                StrSql = "insert into " + compdb(comp_id) + "axela_sms"
                        + "("
                        + "sms_emp_id, "
                        + "sms_mobileno,"
                        + "sms_msg,"
                        + "sms_date ,"
                        + "sms_customer_id ,"
                        + "sms_sent ,"
                        + "sms_credits ,"
                        + "sms_entry_id)"
                        + "" + Sql + "";
                sms_id = UpdateQueryReturnID(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    public String PopulateGroup() {
        try {
            StrSql = " select group_id, concat(group_desc,'') as group_desc "
                    + " from " + compdb(comp_id) + "axela_customer_group "
                    + " where 1=1   order by group_desc";
            CachedRowSet crs =processQuery(StrSql, 0);
            StringBuilder Str = new StringBuilder();
            while (crs.next()) {
                Str.append("<option value=" + crs.getString("group_id") + ArrSelectdrop(crs.getInt("group_id"), group_ids) + ">" + crs.getString("group_desc") + "</option> \n");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }
}
