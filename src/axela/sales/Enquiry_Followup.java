package axela.sales;
// Saiman on 24th april 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Followup extends Connect {

	public String enquiry_id = "0";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrHTML = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
			if (!enquiry_id.equals("0")) {
				StrHTML = ListFollowup();
			}
		}
	}

	public String ListFollowup() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT date_format(followup_followup_time,'%d-%m-%Y %H:%i') AS followuptime, followuptype_name, followup_desc "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id "
					+ " WHERE followup_enquiry_id=" + enquiry_id
					+ " ORDER BY followup_followup_time desc";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append(crs.getString("followuptime"));
				Str.append(" => ");
				Str.append(crs.getString("followuptype_name")).append(": ");
				Str.append(crs.getString("followup_desc")).append("<br>");
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
