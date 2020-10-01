package axela.sales;
//divya 26th nov

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Check extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String enquiry_branch_id = "";
	public String enquiry_date = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("enquiry_branch_id")));
			enquiry_date = PadQuotes(request.getParameter("enquiry_date"));
			// SOP("enquiry_date--" + enquiry_date);
			// response.setContentType("text/html");
			// PrintWriter out = response.getWriter();

			if (!enquiry_branch_id.equals("0")) {
				StrHTML = PopulateCampaign();
			}
		}
		// out.println(StrHTML);
		// out.flush();
		// out.close();
	}

	public String PopulateCampaign() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id"
					+ " WHERE  1 = 1"
					+ " AND camptrans_branch_id = " + enquiry_branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8) "
					+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8) "
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			// SOP("StrSql--"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_campaign_id\" id=\"dr_enquiry_campaign_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id")).append("");
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ").append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
