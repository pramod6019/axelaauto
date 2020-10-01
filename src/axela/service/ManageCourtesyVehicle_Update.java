// Sangita (03 APRIL 2013)
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCourtesyVehicle_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String courtesyveh_id = "0";
    public String courtesyveh_name = "";
    public String courtesyveh_branch_id = "0";
    public String courtesyveh_regno = "";
    public String courtesyveh_service_start_date = "";
    public String courtesyveh_service_end_date = "";
    public String courtesyveh_active = "";
    public String courtesyveh_notes = "";
    public String courtesyveh_entry_id = "";
    public String courtesyveh_entry_date = "";
    public String courtesyveh_modified_id = "";
    public String courtesyveh_modified_date = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String QueryString = "";
    public String entry_by = "";
    public String modified_by = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_service_courtesy_access", request, response);
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            addB = PadQuotes(request.getParameter("add_button"));
            updateB = PadQuotes(request.getParameter("update_button"));
            deleteB = PadQuotes(request.getParameter("delete_button"));
            msg = PadQuotes(request.getParameter("msg"));
            courtesyveh_id = CNumeric(PadQuotes(request.getParameter("courtesyveh_id")));
            QueryString = PadQuotes(request.getQueryString());

            if ("yes".equals(add)) {
                status = "Add";
                if (!"yes".equals(addB)) {
                    courtesyveh_active = "1";
                } else {
                    GetValues(request, response);
                    if (ReturnPerm(comp_id, "emp_service_courtesy_add", request).equals("1")) {
                        courtesyveh_entry_id = emp_id;
                        courtesyveh_entry_date = ToLongDate(kknow());
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managecourtesyvehicle.jsp?courtesyveh_id=" + courtesyveh_id + "&msg=Vehicle Added Successfully!"));
                        }
                    } else {
                        response.sendRedirect(AccessDenied());
                    }
                }
            } else if ("yes".equals(update)) {
                status = "Update";
                if (!"yes".equals(updateB) && !deleteB.equals("Delete Vehicle")) {
                    PopulateFields(response);
                } else if ("yes".equals(updateB) && !deleteB.equals("Delete Vehicle")) {
                    GetValues(request, response);
                    if (ReturnPerm(comp_id, "emp_service_courtesy_edit", request).equals("1")) {
                        courtesyveh_modified_id = emp_id;
                        courtesyveh_modified_date = ToLongDate(kknow());
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managecourtesyvehicle.jsp?courtesyveh_id=" + courtesyveh_id + "&msg=Vehicle Updated Successfully!"));
                        }
                    } else {
                        response.sendRedirect(AccessDenied());
                    }
                } else if (deleteB.equals("Delete Vehicle")) {
                    GetValues(request, response);
                    if (ReturnPerm(comp_id, "emp_service_courtesy_delete", request).equals("1")) {
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managecourtesyvehicle.jsp?msg=Courtesy Vehicle Deleted Successfully!"));
                        }
                    } else {
                        response.sendRedirect(AccessDenied());
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
        courtesyveh_id = CNumeric(PadQuotes(request.getParameter("courtesyveh_id")));
        courtesyveh_branch_id = PadQuotes(request.getParameter("dr_branch"));
        courtesyveh_name = PadQuotes(request.getParameter("txt_courtesyveh_name"));
        courtesyveh_regno = PadQuotes(request.getParameter("txt_courtesyveh_regno"));
        courtesyveh_service_start_date = PadQuotes(request.getParameter("txt_courtesyveh_service_start_date"));
        courtesyveh_service_end_date = PadQuotes(request.getParameter("txt_courtesyveh_service_end_date"));
        courtesyveh_active = CheckBoxValue(PadQuotes(request.getParameter("chk_courtesyveh_active")));
        courtesyveh_notes = PadQuotes(request.getParameter("txt_courtesyveh_notes"));
        entry_by = PadQuotes(request.getParameter("entry_by"));
        courtesyveh_entry_date = PadQuotes(request.getParameter("entry_date"));
        modified_by = PadQuotes(request.getParameter("modified_by"));
        courtesyveh_modified_date = PadQuotes(request.getParameter("modified_date"));
    }

    protected void CheckForm() {
        if (courtesyveh_branch_id.equals("0")) {
            msg = msg + "<br>Select Branch!";
        }

        if (courtesyveh_name.equals("")) {
            msg = msg + "<br>Enter Courtesy Vehicle Name!";
        }

        if (!courtesyveh_regno.equals("")) {
            StrSql = "SELECT courtesyveh_regno"
                    + " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " WHERE courtesyveh_regno = '" + courtesyveh_regno + "'"
                    + " AND courtesyveh_branch_id = " + courtesyveh_branch_id + ""
                    + " AND courtesyveh_id != " + courtesyveh_id + "";
            if (ExecuteQuery(StrSql).equals(courtesyveh_regno)) {
                msg += "<br>Similar Registration No. Found!";
            }
        }

        if (courtesyveh_service_start_date.equals("")) {
            msg = msg + "<br>Select Start Date!";
        } else if (!isValidDateFormatShort(courtesyveh_service_start_date)) {
            msg = msg + "<br>Enter valid Service Start Date!";
        }

        if (!courtesyveh_service_end_date.equals("") && !isValidDateFormatShort(courtesyveh_service_end_date)) {
            msg += "<br>Enter valid Service End Date!";
        }

        if (!courtesyveh_service_start_date.equals("") && isValidDateFormatShort(courtesyveh_service_start_date) && !courtesyveh_service_end_date.equals("") && isValidDateFormatShort(courtesyveh_service_end_date)) {
            long campaign_sdate = Long.parseLong(ConvertShortDateToStr(courtesyveh_service_start_date));
            long campaign_edate = Long.parseLong(ConvertShortDateToStr(courtesyveh_service_end_date));
            if (campaign_sdate > campaign_edate) {
                msg += "<br>End Date should be greater than Start Date!";
            }
        }

        if (courtesyveh_notes.length() > 8000) {
            courtesyveh_notes = courtesyveh_notes.substring(0, 7999);
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            courtesyveh_id = ExecuteQuery("SELECT (COALESCE(MAX(courtesyveh_id), 0) + 1)"
                    + " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle");

            StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " (courtesyveh_id,"
                    + " courtesyveh_branch_id,"
                    + " courtesyveh_name,"
                    + " courtesyveh_regno,"
                    + " courtesyveh_service_start_date,"
                    + " courtesyveh_service_end_date,"
                    + " courtesyveh_active,"
                    + " courtesyveh_notes,"
                    + " courtesyveh_entry_id,"
                    + " courtesyveh_entry_date)"
                    + " VALUES"
                    + " (" + courtesyveh_id + ","
                    + " " + courtesyveh_branch_id + ","
                    + " '" + courtesyveh_name + "',"
                    + " '" + courtesyveh_regno.replace(" ", "") + "',"
                    + " '" + ConvertShortDateToStr(courtesyveh_service_start_date) + "',"
                    + " '" + ConvertShortDateToStr(courtesyveh_service_end_date) + "',"
                    + " '" + courtesyveh_active + "',"
                    + " '" + courtesyveh_notes + "',"
                    + " '" + courtesyveh_entry_id + "',"
                    + " '" + courtesyveh_entry_date + "')";
            updateQuery(StrSql);
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_branch_id, courtesyveh_regno,"
                    + " courtesyveh_service_start_date, courtesyveh_service_end_date, courtesyveh_active,"
                    + " courtesyveh_notes, courtesyveh_entry_id, courtesyveh_entry_date,"
                    + " courtesyveh_modified_id, courtesyveh_modified_date"
                    + " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
                    + " WHERE courtesyveh_id = " + courtesyveh_id + "";
            CachedRowSet crs = processQuery(StrSql, 0);

            if (crs.isBeforeFirst() && !courtesyveh_id.equals("0")) {
                while (crs.next()) {
                    courtesyveh_branch_id = crs.getString("courtesyveh_branch_id");
                    courtesyveh_name = crs.getString("courtesyveh_name");
                    courtesyveh_regno = SplitRegNo(crs.getString("courtesyveh_regno"), 2);
                    courtesyveh_service_start_date = strToShortDate(crs.getString("courtesyveh_service_start_date"));
                    courtesyveh_service_end_date = strToShortDate(crs.getString("courtesyveh_service_end_date"));
                    courtesyveh_active = crs.getString("courtesyveh_active");
                    courtesyveh_notes = crs.getString("courtesyveh_notes");
                    courtesyveh_entry_id = crs.getString("courtesyveh_entry_id");
                    entry_by = Exename(comp_id, crs.getInt("courtesyveh_entry_id"));
                    courtesyveh_entry_date = strToLongDate(crs.getString("courtesyveh_entry_date"));
                    courtesyveh_modified_id = crs.getString("courtesyveh_modified_id");
                    if (!courtesyveh_modified_id.equals("0")) {
                        modified_by = Exename(comp_id, Integer.parseInt(courtesyveh_modified_id));
                        courtesyveh_modified_date = strToLongDate(crs.getString("courtesyveh_modified_date"));
                    }
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid courtesyvehicle!"));
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
            StrSql = "UPDATE " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " SET"
                    + " courtesyveh_branch_id = " + courtesyveh_branch_id + ","
                    + " courtesyveh_name = '" + courtesyveh_name + "',"
                    + " courtesyveh_regno = '" + courtesyveh_regno.replace(" ", "") + "',"
                    + " courtesyveh_service_start_date = '" + ConvertShortDateToStr(courtesyveh_service_start_date) + "',"
                    + " courtesyveh_service_end_date = '" + ConvertShortDateToStr(courtesyveh_service_end_date) + "',"
                    + " courtesyveh_active = '" + courtesyveh_active + "',"
                    + " courtesyveh_notes = '" + courtesyveh_notes + "',"
                    + " courtesyveh_modified_id = '" + courtesyveh_modified_id + "',"
                    + " courtesyveh_modified_date = '" + courtesyveh_modified_date + "'"
                    + " WHERE courtesyveh_id = " + courtesyveh_id + "";
            updateQuery(StrSql);
        }
    }

    protected void DeleteFields() {
        StrSql = "SELECT courtesycar_courtesyveh_id FROM " + compdb(comp_id) + "axela_service_courtesy_car"
                + " WHERE courtesycar_courtesyveh_id = " + courtesyveh_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = "<br>Courtesy Vehicle is Associated with a Coutesy Car(s)!";
        } else {
            StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " WHERE courtesyveh_id = " + courtesyveh_id + "";
            updateQuery(StrSql);
        }
    }
}
