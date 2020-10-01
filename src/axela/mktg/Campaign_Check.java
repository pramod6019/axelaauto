package axela.mktg;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Check extends HttpServlet {

	public String emp_id = "", branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	Connect ct = new Connect();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		emp_id = (session.getAttribute("emp_id")).toString();
		branch_id = ct.CNumeric(ct.PadQuotes(request.getParameter("branch_id")));
		StrHTML = PopulateCampaign();
	}

	public String PopulateCampaign() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " Select campaign_id, campaign_subject, campaign_entry_date"
					+ " FROM axela_mktg_campaign"
					+ " where campaign_entry_date <=" + ct.ToLongDate(ct.kknow()) + " "
					+ " and campaign_entry_date> " + ct.ToLongDate(ct.AddHoursDate(ct.StringToDate(ct.ToLongDate(ct.kknow())), -31, 0, 0))
					+ " and campaign_branch_id=" + branch_id
					+ " GROUP BY campaign_id"
					+ " ORDER BY campaign_entry_date desc";

			// SOP("StrSql==== in PopulateCampaign" + StrSql);
			CachedRowSet crs = ct.processQuery(StrSql);
			Str.append("<select name=dr_campaign id=dr_campaign class=selectbox ><option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(">").append(crs.getString("campaign_subject")).append("-").append(ct.strToLongDate(crs.getString("campaign_entry_date"))).append("</option>\n");
			}
			Str.append("</select>");

			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			ct.SOPError(ct.ClientName + "===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
