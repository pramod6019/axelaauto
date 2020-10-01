package axela.service;
//divya 26th nov

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Check extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String booking_id = "0";
	public String vehfollowup_pickupdriver_emp_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
			// SOP("enquiry_branch_id--" + enquiry_branch_id);
			// response.setContentType("text/html");
			// PrintWriter out = response.getWriter();

			if (!booking_id.equals("0")) {
				StrHTML = PopulateServicePickUp(comp_id);
			}
		}
		// out.println(StrHTML);
		// out.flush();
		// out.close();
	}

	public String PopulateServicePickUp(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1"
					+ " AND emp_active=1"
					+ " AND emp_pickup_driver=1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), vehfollowup_pickupdriver_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
