// smitha nag june 26 2013
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageModuleReport_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public String status = "";
    public String StrSql = "";
//    public String user_id = "0";
    public String emp_id = "0";
    public String comp_id = "0";
    public String msg = "";
    public String QueryString = "";
    public String report_module_id = "0";
    public String report_id = "0";
    public String report_name = "";
    public String report_url = "";
    public String report_moduledisplay = "";
    public String report_misdisplay = "";
    public String report_active = "";
    public String report_rank = "";
    public String report_entry_id = "0";
    public String report_entry_date = "";
    public String report_modified_id = "0";
    public String report_modified_date = "";
    public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
    public String module_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
			comp_id = CNumeric(GetSession("comp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            HttpSession session = request.getSession(true);
            if(!comp_id.equals("0"))
            {
            	 emp_id = CNumeric(GetSession("emp_id", request));
                 add = PadQuotes(request.getParameter("add"));
                 update = PadQuotes(request.getParameter("update"));
                 addB = PadQuotes(request.getParameter("add_button"));
                 updateB = PadQuotes(request.getParameter("update_button"));
                 deleteB = PadQuotes(request.getParameter("delete_button"));
                 msg = PadQuotes(request.getParameter("msg"));
                 QueryString = PadQuotes(request.getQueryString());
                 report_id = CNumeric(PadQuotes(request.getParameter("report_id")));
                 module_id = CNumeric(PadQuotes(request.getParameter("module_id")));
                 if (report_module_id.equals("0")) {
                     report_module_id = module_id;
                 }
                 if (add.equals("yes")) {
                     status = "Add";
                 } else if (update.equals("yes")) {
                     status = "Update";
                 }
                 if ("yes".equals(add)) {
                     if (!"Add Report".equals(addB)) {
                         report_name = "";
                         report_active = "1";
                         report_misdisplay = "1";
                         report_moduledisplay = "1";
                     } else {
                         report_entry_id = CNumeric(emp_id);
                         if (!emp_id.equals("0")) {
                             report_entry_date = ToLongDate(kknow());
                         }
                         GetValues(request, response);
                         AddFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managemodulereport.jsp?dr_module_id=" + report_module_id + "&msg=Report added successfully!"));
                         }
                     }
                 }
                 if ("yes".equals(update)) {
                     if (!"Update Report".equals(updateB) && !"Delete Report".equals(deleteB)) {
                         PopulateFields(response);
                     } else if ("Update Report".equals(updateB) && !"Delete Report".equals(deleteB)) {
                         report_modified_id = CNumeric(emp_id);
                         if (!emp_id.equals("0")) {
                             report_modified_date = ToLongDate(kknow());
                         }
                         GetValues(request, response);
                         UpdateFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managemodulereport.jsp?dr_module_id=" + report_module_id + "&msg=Report updated successfully!"));
                         }
                     } else if ("Delete Report".equals(deleteB)) {
                         GetValues(request, response);
                         DeleteFields();
                         if (!msg.equals("")) {
                             msg = "Error!" + msg;
                         } else {
                             response.sendRedirect(response.encodeRedirectURL("managemodulereport.jsp?dr_module_id=" + report_module_id + "&msg=Report deleted successfully!"));
                         }
                     }
                 }
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

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        report_module_id = CNumeric(PadQuotes(request.getParameter("dr_report_module_id")));
        report_name = PadQuotes(request.getParameter("txt_report_name"));
        report_url = PadQuotes(request.getParameter("txt_report_url"));

//       
        report_moduledisplay = PadQuotes(request.getParameter("ch_report_moduledisplay"));
        if (report_moduledisplay.equals("on")) {
            report_moduledisplay = "1";
        } else {
            report_moduledisplay = "0";
        }
        report_misdisplay = PadQuotes(request.getParameter("ch_report_misdisplay"));
        if (report_misdisplay.equals("on")) {
            report_misdisplay = "1";
        } else {
            report_misdisplay = "0";
        }
        report_active = PadQuotes(request.getParameter("ch_report_active"));
        if (report_active.equals("on")) {
            report_active = "1";
        } else {
            report_active = "0";
        }
        entry_by = PadQuotes(request.getParameter("entry_by"));
        entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        msg = "";
        if (report_module_id.equals("0")) {
            msg = msg + "<br>Select Module!";
        }
        if (report_name.equals("")) {
            msg = msg + "<br>Enter Report Name!";
        }
        if (!report_name.equals("")) {
            try {
                if (update.equals("yes") && !report_name.equals("")) {
                    StrSql = "Select report_name from " + maindb() + "module_report"
                            + " where report_name = '" + report_name + "' and report_id!=" + report_id + ""
                            + " and report_module_id = " + report_module_id + "";
                } else if (add.equals("yes") && !report_name.equals("")) {
                    StrSql = "Select report_name from " + maindb() + "module_report"
                            + " where report_name = '" + report_name + "'"
                            + " and report_module_id = " + report_module_id + "";
                }
                CachedRowSet crs =processQuery(StrSql, 0);
                if (crs.isBeforeFirst()) {
                    msg = msg + "<br>Similar Report Name found!";
                }
                crs.close();
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
        if (report_url.equals("")) {
            msg = msg + "<br>Enter URL!";
        } else {
            CheckReportUrl(report_url);
        }
//        SOP("CheckReportUrl(report_url) = " + CheckReportUrl(report_url));
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                report_rank = ExecuteQuery("SELECT coalesce(max(report_rank),0)+1 as rank FROM " + maindb() + "module_report where report_module_id=" + report_module_id);

                StrSql = "insert into " + maindb() + "module_report"
                        + " (report_id,"
                        + " report_module_id,"
                        + " report_name,"
                        + " report_url,"
                        + " report_moduledisplay,"
                        + " report_misdisplay,"
                        + " report_active,"
                        + " report_rank,"
                        + " report_entry_id,"
                        + " report_entry_date,"
                        + " report_modified_id,"
                        + " report_modified_date)"
                        + " values"
                        + " ((Select coalesce(max(report_id),'0')+1 from " + maindb() + "module_report as report_id),"
                        + " " + report_module_id + ","
                        + " '" + report_name + "',"
                        + " '" + report_url + "',"
                        + " " + report_moduledisplay + ","
                        + " " + report_misdisplay + ","
                        + " " + report_active + ","
                        + " " + report_rank + ","
                        + " " + report_entry_id + ","
                        + " '" + report_entry_date + "',"
                        + " 0,"
                        + " '')";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select report_module_id, report_name, report_url, report_moduledisplay,"
                    + " report_misdisplay, report_active, report_entry_id, report_entry_date,"
                    + " report_modified_id, report_modified_date"
                    + " from " + maindb() + "module_report "
                    + " inner join " + maindb() + "module on module_id=report_module_id "
                    + " where report_id = " + report_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    report_module_id = crs.getString("report_module_id");
                    report_name = crs.getString("report_name");
                    report_url = crs.getString("report_url");
                    report_moduledisplay = crs.getString("report_moduledisplay");
                    report_misdisplay = crs.getString("report_misdisplay");
                    report_active = crs.getString("report_active");
                    report_entry_id = crs.getString("report_entry_id");
                    if (!report_entry_id.equals("0")) {
                        entry_by = Exename(comp_id, Integer.parseInt(report_entry_id));
//                        entry_by = AdminExename(comp_id, crs.getInt("report_entry_id"));
                        entry_date = strToLongDate(crs.getString("report_entry_date"));
                    }
                    report_modified_id = crs.getString("report_modified_id");
                    if (!report_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(report_modified_id));
//                        modified_by = AdminExename(comp_id, Integer.parseInt(report_modified_id));
                        modified_date = strToLongDate(crs.getString("report_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("managemodulereport.jsp?&msg=Report not found!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields() {
        CheckForm();
        if (msg.equals("")) {
            try {
                StrSql = "UPDATE " + maindb() + "module_report"
                        + " SET"
                        + " report_module_id = " + report_module_id + ","
                        + " report_name = '" + report_name + "',"
                        + " report_url = '" + report_url + "',"
                        + " report_moduledisplay = " + report_moduledisplay + ","
                        + " report_misdisplay = " + report_misdisplay + ","
                        + " report_active = " + report_active + ","
                        + " report_modified_id = '" + report_modified_id + "',"
                        + " report_modified_date = " + report_modified_date + ""
                        + " where report_id = " + report_id + "";
//                SOP("StrSql update query = " + StrSql);
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        try {
            StrSql = "Delete from " + maindb() + "module_report"
                    + " where report_id = " + report_id + "";
            updateQuery(StrSql);
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public String PopulateModule() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT module_id, module_name"
                    + " from " + maindb() + "module"
                    + " order by module_name ";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("module_id")).append("");
                Str.append(Selectdrop(crs.getInt("module_id"), report_module_id));
                Str.append(">").append(crs.getString("module_name")).append("</option> \n");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public String CheckReportUrl(String url) {
        if (url.startsWith("http://")) {
            msg = "<br>Invalid URL";
        }
        return msg;
    }
}
