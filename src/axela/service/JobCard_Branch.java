package axela.service;
//aJIt 25th july
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Branch extends Connect {

	public String emp_id = "";
	public String chkPermMsg = "";
	public String so_id = "0", go = "";
	public static String msg = "";
	public String StrSql = "";
	public String branch_id = "", emp_branch_id = "";
	public String rateclass_id = "";
	public String para = "";
	public String heading = "";
	public String emp_role_id = "", comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				go = PadQuotes(request.getParameter("go"));
				msg = PadQuotes(request.getParameter("msg"));
				if (!CNumeric(go).equals("")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../service/jobcard-import.jsp?branch_id=" + branch_id));
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

		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));

	}
	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
	}

	public String PopulateBranch(String branch_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = '1'"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("StrSql==="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

}
