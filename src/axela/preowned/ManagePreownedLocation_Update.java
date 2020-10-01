package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedLocation_Update extends Connect {

    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String preownedlocation_id = "";
    public String preownedlocation_branch_id = "";
    public String preownedlocation_name = "";
    public String BranchAccess;
    public String emp_role_id = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String QueryString = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                emp_role_id = CNumeric(GetSession("emp_role_id", request));
                CheckPerm(comp_id, "emp_preowned_stock_access", request, response);
                BranchAccess = GetSession("BranchAccess", request);
                add = PadQuotes(request.getParameter("Add"));
                update = PadQuotes(request.getParameter("Update"));
                addB = PadQuotes(request.getParameter("add_button"));
                updateB = PadQuotes(request.getParameter("update_button"));
                deleteB = PadQuotes(request.getParameter("delete_button"));
                msg = PadQuotes(request.getParameter("msg"));
                preownedlocation_id = CNumeric(PadQuotes(request.getParameter("preownedlocation_id")));
                QueryString = PadQuotes(request.getQueryString());

                if ("yes".equals(add)) {
                    status = "Add";
                    if (!"yes".equals(addB)) {
                        preownedlocation_branch_id = "";
                        preownedlocation_name = "";
                    } else {
                        CheckPerm(comp_id, "emp_item_add", request, response);
                        GetValues(request, response);
                        AddFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managepreownedlocation.jsp?preownedlocation_id=" + preownedlocation_id + "&msg=Pre Owned Location Added Successfully!"));
                        }
                    }
                } else if ("yes".equals(update)) {
                    status = "Update";
                    if (!"yes".equals(updateB) && !"Delete Location".equals(deleteB)) {
                        PopulateFields(response);
                    } else if ("yes".equals(updateB) && !"Delete Location".equals(deleteB)) {
                        CheckPerm(comp_id, "emp_item_edit", request, response);
                        GetValues(request, response);
                        UpdateFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managepreownedlocation.jsp?preownedlocation_id=" + preownedlocation_id + "&msg=Pre Owned Location Updated Successfully!"));
                        }
                    } else if ("Delete Location".equals(deleteB)) {
                        CheckPerm(comp_id, "emp_item_delete", request, response);
                        GetValues(request, response);
                        DeleteFields();
                        if (!msg.equals("")) {
                            msg = "Error!" + msg;
                        } else {
                            response.sendRedirect(response.encodeRedirectURL("managepreownedlocation.jsp?msg=Pre Owned Location Deleted Successfully!"));
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

    protected void GetValues(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        preownedlocation_branch_id = PadQuotes(request.getParameter("dr_branch"));
        preownedlocation_name = PadQuotes(request.getParameter("txt_location_name"));  
    }

    protected void CheckForm() {
        msg = "";
        if (preownedlocation_branch_id.equals("0")) {
            msg = msg + "<br>Select Branch!";
        }

        if (preownedlocation_name.equals("")) {
            msg = msg + "<br>Enter Name!";
        }

        if (!preownedlocation_name.equals("") && !preownedlocation_branch_id.equals("0")) {
            StrSql = "SELECT preownedlocation_name FROM " + compdb(comp_id) + "axela_preowned_location"
                    + " WHERE preownedlocation_name = '" + preownedlocation_name + "'"
                    + " AND preownedlocation_branch_id = " + preownedlocation_branch_id + ""
                    + " AND preownedlocation_id != " + preownedlocation_id + "";
            if (ExecuteQuery(StrSql).equals(preownedlocation_name)) {
                msg = msg + "<br>Similar Location Name Found!";
            }
        }
    }

    protected void AddFields() {
        CheckForm();
        if (msg.equals("")) {
            preownedlocation_id = ExecuteQuery("SELECT (COALESCE(MAX(preownedlocation_id), 0) + 1) FROM " + compdb(comp_id) + "axela_preowned_location");
            StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_location"
                    + " (preownedlocation_id,"
                    + " preownedlocation_branch_id,"
                    + " preownedlocation_name)"
                    + " values"
                    + " (" + preownedlocation_id + ","
                    + " " + preownedlocation_branch_id + ","
                    + " '" + preownedlocation_name + "')";
            updateQuery(StrSql);
        }
    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "Select * from " + compdb(comp_id) + "axela_preowned_location"
                    + " inner join " + compdb(comp_id) + "axela_branch on branch_id = preownedlocation_branch_id"
                    + " where preownedlocation_id = " + preownedlocation_id + BranchAccess;
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    preownedlocation_branch_id = crs.getString("preownedlocation_branch_id");
                    preownedlocation_name = crs.getString("preownedlocation_name");
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Location!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFields() {
        CheckForm();
        if (msg.equals("")) {
            StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_location"
                    + " SET"
                    + " preownedlocation_branch_id = " + preownedlocation_branch_id + ","
                    + " preownedlocation_name = '" + preownedlocation_name + "'"
                    + " where preownedlocation_id = " + preownedlocation_id + "";
            updateQuery(StrSql);
        }
    }

    protected void DeleteFields() {
        //Association with Pre Owned
        StrSql = "SELECT stock_location_id from " + compdb(comp_id) + "axela_stock"
                + " where stock_location_id = " + preownedlocation_id + "";
        if (!ExecuteQuery(StrSql).equals("")) {
            msg = msg + "<br>Location is Associated with Pre Owned!";
        }
//Delete records
        if (msg.equals("")) {
            StrSql = "Delete from " + compdb(comp_id) + "axela_stock_Location"
                    + " where preownedlocation_id = " + preownedlocation_id + "";
            updateQuery(StrSql);
        }
    }
}
