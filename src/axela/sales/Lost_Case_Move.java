package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Lost_Case_Move extends Connect {

	public String enquiry_lostcase1_from_id = "";
	public String enquiry_lostcase2_from_id = "";
	public String enquiry_lostcase3_from_id = "";
	public String enquiry_lostcase1_to_id = "";
	public String enquiry_lostcase2_to_id = "";
	public String enquiry_lostcase3_to_id = "";
	public String StrSql = "", msg = "";
	public String comp_id = "0";
	public String moveB = "";
	public String StrHTML = "";
	public String value = "";
	public String value1 = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_role_id", request, response);
		if (!comp_id.equals("0")) {
			moveB = PadQuotes(request.getParameter("movebutton"));
			try {
				GetValues(request, response);
				if (moveB.equals("Move")) {
					MoveUpdate();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		enquiry_lostcase1_from_id = CNumeric(PadQuotes(request
				.getParameter("dr_enquiry_lostcase1_from_id")));
		enquiry_lostcase2_from_id = CNumeric(PadQuotes(request
				.getParameter("dr_enquiry_lostcase2_from_id")));
		enquiry_lostcase3_from_id = CNumeric(PadQuotes(request
				.getParameter("dr_enquiry_lostcase3_from_id")));
		enquiry_lostcase1_to_id = CNumeric(PadQuotes(request
				.getParameter("dr_enquiry_lostcase1_to_id")));
		enquiry_lostcase2_to_id = CNumeric(PadQuotes(request
				.getParameter("dr_enquiry_lostcase2_to_id")));
		enquiry_lostcase3_to_id = CNumeric(PadQuotes(request
				.getParameter("dr_enquiry_lostcase3_to_id")));
	}

	public String PopulateLostCase1(String enquiry_lostcase1_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select lostcase1_id, lostcase1_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_lostcase1" + " where 1=1"
					+ " order by lostcase1_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("lostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"),
						enquiry_lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase2(String enquiry_lostcase1_id, String enquiry_lostcase2_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select lostcase2_id, lostcase2_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " where lostcase2_lostcase1_id = " + enquiry_lostcase1_id
					+ " order by lostcase2_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"),
						enquiry_lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase3(String enquiry_lostcase2_id, String enquiry_lostcase3_id) {

		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "Select lostcase3_id, lostcase3_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " where lostcase3_lostcase2_id = " + enquiry_lostcase2_id
					+ " order by lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("PopulateLostCase3======" + StrSqlBreaker(StrSql));
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"),
						enquiry_lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void CheckForm() {
		msg = "";
		if (enquiry_lostcase1_from_id.equals("0")) {
			msg += "<br>Select From Lost Case 1!";
		}
		if (enquiry_lostcase2_from_id.equals("0")) {
			msg += "<br>Select From Lost Case 2!";
		}
		if (enquiry_lostcase3_from_id.equals("0")) {
			msg += "<br>Select From Lost Case 3!";
		}
		if (enquiry_lostcase1_to_id.equals("0")) {
			msg += "<br>Select To Lost Case 1!";
		}
		if (enquiry_lostcase2_to_id.equals("0")) {
			msg += "<br>Select To Lost Case 2!";
		}
		if (enquiry_lostcase3_to_id.equals("0")) {
			msg += "<br>Select To Lost Case 3!";
		}
		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void MoveUpdate() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET enquiry_lostcase1_id = " + enquiry_lostcase1_to_id + ","
					+ " enquiry_lostcase2_id = " + enquiry_lostcase2_to_id + ","
					+ " enquiry_lostcase3_id = " + enquiry_lostcase3_to_id + ""
					+ " WHERE 1=1"
					+ " AND enquiry_lostcase1_id = " + enquiry_lostcase1_from_id
					+ " AND enquiry_lostcase2_id = " + enquiry_lostcase2_from_id
					+ " AND enquiry_lostcase3_id = " + enquiry_lostcase3_from_id;

			updateQuery(StrSql);
			msg = "Lost Case Moved Successfully!";
		}
	}
}
