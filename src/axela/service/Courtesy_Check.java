package axela.service;
//Dilip kumar 03 JUN 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Courtesy_Check extends Connect {

    public String multiple = "";
    public String courtesyveh_branch_id = "0";
    public String StrHTML = "";
    public String comp_id = "0";
    public String vehicleoutage = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	CheckSession(request, response);
        HttpSession session = request.getSession(true);
        comp_id = CNumeric(GetSession("comp_id", request));
        if(!comp_id.equals("0")){
        courtesyveh_branch_id = PadQuotes(request.getParameter("branch_id"));
        multiple = PadQuotes(request.getParameter("multiple"));
        vehicleoutage = PadQuotes(request.getParameter("vehicleoutage"));

        if (multiple.equals("yes")) {
            StrHTML = new Courtesy_Cal().PopulateVehicle(courtesyveh_branch_id);
        }

        if (vehicleoutage.equals("yes") && !CNumeric(courtesyveh_branch_id).equals("0")) {
            StrHTML = PopulateVehicle();
        }
    }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public String PopulateVehicle() {
        StringBuilder Str = new StringBuilder();
        try {
            String StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_regno"
                    + " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
                    + " WHERE courtesyveh_active = 1"
                    + " AND courtesyveh_branch_id = '" + courtesyveh_branch_id + "'"
                    + " ORDER BY courtesyveh_name";
//            SOP("StrSql = " + StrSql);
            CachedRowSet crs = processQuery(StrSql, 0);

            Str.append("<select name=\"dr_vehicle\" class=form-control id=\"dr_vehicle\">\n");
            Str.append("<option value=\"0\">Select</option>\n");
            while (crs.next()) {
                Str.append("<option value=").append(crs.getString("courtesyveh_id"));
//                Str.append(StrSelectdrop(crs.getString("courtesyveh_id"), courtesyoutage_courtesyveh_id));
                Str.append(">").append(crs.getString("courtesyveh_name")).append(" - ");
                Str.append(SplitRegNo(crs.getString("courtesyveh_regno"), 2)).append("</option>\n");
            }
            Str.append("</select>\n");
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
        return Str.toString();
    }
}
