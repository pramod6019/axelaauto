//Smitha Nag 18 feb 2013
package axela.sales;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDrive_Vehicle_Check extends Connect {
	
	public String model_id = "";
	public String branch_id = "";
	public String testdrive = "";
	public String status = "";
	public String testdrive_testdriveveh_id = "";
	public String testdrivedate = "";
	public String starttime = "";
	public String endtime = "";
	public String msg = "";
	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_testdrive_edit = "";
	public String StrSql = "";
	DecimalFormat deci = new DecimalFormat("#.##");
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (CNumeric(GetSession("emp_id", request)).equals("0")) {
				out.print("Your session has expired!");
				return;
			}
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = PadQuotes(request.getParameter("dr_branch_id"));
			status = PadQuotes(request.getParameter("status"));
			testdrive_testdriveveh_id = PadQuotes(request.getParameter("testdriveveh_id"));
			testdrivedate = PadQuotes(request.getParameter("testdrivedate"));
			if (testdrive.equals("")) {
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
		String search = "";
		if (!branch_id.equals("") && !branch_id.equals("0")) {
			search = " and testdriveveh_branch_id=" + branch_id;
		}
		try {
			StrSql = "SELECT testdriveveh_id, testdriveveh_name, testdriveveh_regno"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " WHERE 1 = 1 " + search + "";
			if (status.equals("Add")) {
				StrSql += " AND testdriveveh_active = '1'";
			}
			
			StrSql += " GROUP BY testdriveveh_id"
					+ " ORDER BY testdriveveh_name";
			
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_vehicle id=dr_vehicle class=form-control>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("testdriveveh_id")).append(">").append(crs.getString("testdriveveh_name")).append(" - ").append(crs.getString("testdriveveh_regno"))
						.append("");
				Str.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
