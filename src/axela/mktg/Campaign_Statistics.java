package axela.mktg;
//sn, 13 may 2013
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Statistics extends Connect {

	public String StrSql = "";
	public static String msg = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String campaign_daily_date = "";
	public String[] x = new String[32];
	public String campaign_views = "";
	public String StrSearch = "", campaign_id = "0";
	public String campaign_date = "", campaign_subject = "";
	public String chart_data = "";
	public String trafficchart_data = "";
	public String UniqueVisitorChart = "", TrafficChart = "", VisitorsChart = "";
	public String ttl_campaign_id = "", ttl_view_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mktg_campaign_access", request, response);
			HttpSession session = request.getSession(true);
			emp_id = session.getAttribute("emp_id").toString();
			campaign_id = PadQuotes(request.getParameter("campaign_id"));
			campaign_date = PadQuotes(request.getParameter("campaign_sentdate"));

			StrSql = "SELECT campaign_sentdate, campaign_subject"
					+ " FROM axela_mktg_campaign"
					// + " INNER JOIN axela_email ON email_campaign_id = campaign_id AND email_sent = 1 "
					+ " WHERE campaign_id = '" + campaign_id + "'";
			CachedRowSet crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					campaign_date = crs.getString("campaign_sentdate");
					campaign_subject = crs.getString("campaign_subject");
				}
			}
			// SOP("campaign_date = " + campaign_date);
			// SOP("campaign_subject = " + campaign_subject);

			UniqueVisitorsPieChart();
			TrafficBarGraph();

		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void UniqueVisitorsPieChart() {
		// count of Campaigns sent
		try {
			ttl_campaign_id = ExecuteQuery("select Count(email_campaign_id)"
					+ " from axela_email"
					+ " INNER JOIN axela_mktg_campaign on campaign_id = email_campaign_id"
					+ " where email_campaign_id = '" + campaign_id + "'"
					+ " and campaign_sentdate!='' and email_sent=1");

			// to find no of ppl opened the mails
			ttl_view_id = ExecuteQuery("SELECT Count(distinct view_email_id)"
					+ " FROM axela_mktg_campaign_views"
					+ " INNER JOIN axela_email on email_id = view_email_id"
					+ " INNER JOIN axela_mktg_campaign on campaign_id = email_campaign_id"
					+ " WHERE 1 = 1 and email_sent=1 and email_campaign_id = '" + campaign_id + "'");

			// SOP("ttl_campaign_id = " + ttl_campaign_id);
			// SOP("ttl_view_id = " + ttl_view_id);
			int ttl_notviewed = Integer.parseInt(ttl_campaign_id) - Integer.parseInt(ttl_view_id);

			if (!ttl_campaign_id.equals("0")) {
				chart_data = "[";
				chart_data = chart_data + "{'type': 'Viewed', 'total':" + ttl_view_id + "},";
				chart_data = chart_data + "{'type': 'Not Viewed', 'total':" + ttl_notviewed + "}";
				chart_data = chart_data + "]";
				// chart_data = "[ ['Viewed (" + ttl_view_id + ")'," + ttl_view_id + "],['Not Viewed (" + ttl_notviewed + ")'," + ttl_notviewed + "] ]";
			} else {
				UniqueVisitorChart = "<br>No Campaigns Sent!";
			}

		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void TrafficBarGraph() {
		if (!campaign_date.equals("") && !ttl_campaign_id.equals("0")) {
			StrSql = "";
			int count = 0;
			trafficchart_data = "[";
			CachedRowSet crs = null;
			int i = 0;
			for (int j = 0; j <= 30; j++) {
				i++;
				x[i] = AddDayMonthYear(strToShortDate(campaign_date), j, 0, 0, 0);
				// SOP("i ======== " +i+" date ==== "+ x[i]);
				StrSql = "SELECT Count(view_email_id) as views"
						+ " FROM axela_mktg_campaign_views"
						+ " INNER JOIN axela_email on email_id = view_email_id"
						+ " INNER JOIN axela_mktg_campaign on campaign_id = email_campaign_id"
						+ " WHERE 1 = 1 "
						+ " and email_campaign_id = '" + campaign_id + "'"
						+ " and SUBSTR(view_time, 1, 8) = " + ConvertShortDateToStr(x[i]).substring(0, 8) + "";
				// SOP("StrSql = " + StrSql);
				try {
					crs = processQuery(StrSql);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							count++;
							campaign_views = crs.getString("views") + "";
							campaign_daily_date = "'" + x[i].substring(0, 2) + "-" + TextMonth(Integer.parseInt(x[i].substring(3, 5)) - 1).substring(0, 3) + "'";
							trafficchart_data = trafficchart_data + "{'month': " + campaign_daily_date + ", 'column-1':'" + campaign_views + "'}";
							if (count <= 30) {
								trafficchart_data = trafficchart_data + ",";
							}
						}
					} else {
					}
				} catch (Exception ex) {
					SOPError(ClientName + "===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
			// SOP("nl_views = " + nl_views);
			// SOP("nl_daily_date = " + nl_daily_date);
			trafficchart_data = trafficchart_data + "]";
			try {
				crs.close();
			} catch (SQLException ex) {
				SOPError(this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			TrafficChart = "<br>No Campaigns Sent!";
		}
	}

	// public void VisitorGraph() {
	// // if (!nl_date.equals("")) {
	// StrSql = "";
	// int count = 0;
	// String email_id = "";
	// String view_count = "[";
	// String view_contact = "[";
	// int i = 0;
	// StrSql = "select email_id"
	// + " FROM axela_email"
	// + " INNER JOIN axela_email_newsletter on nl_id = email_nl_id"
	// + " where email_nl_id = '" + campaign_id + "'"
	// + " and nl_sentdate!=''";
	// try {
	// CachedRowSet crs = processQuery(StrSql);
	//
	// crs.last();
	// int nof_emailid = crs.getRow();
	// crs.beforeFirst();
	//
	// if (crs.isBeforeFirst()) {
	// while (crs.next()) {
	// SOP("email = " + crs.getString("email_id"));
	// count++;
	//
	// StrSql = "SELECT Count(view_email_id) as views,"
	// + " concat(title_desc,' ',contact_fname,' ', contact_lname) as contactname"
	// + " FROM axela_email_newsletter_views"
	// + " INNER JOIN axela_email on email_id = view_email_id"
	// + " INNER JOIN axela_email_newsletter on nl_id = email_nl_id "
	// + " INNER JOIN axela_account_contact on contact_id = email_contact_id"
	// + " INNER JOIN axela_title on title_id = contact_title_id"
	// + " WHERE 1 = 1"
	// + " and email_nl_id = '" + campaign_id + "'"
	// + " and view_email_id = '" + crs.getString("email_id") + "'";
	// // SOP("StrSql ============= " + StrSql);
	//
	// CachedRowSet crs1 = processQuery(StrSql);
	// if (crs1.isBeforeFirst()) {
	// while (crs1.next()) {
	//
	// view_count = view_count + crs1.getString("views") + "";
	// view_contact = view_contact + crs1.getString("contactname") + "";
	// if (count < nof_emailid) {
	// view_count = view_count + ", ";
	// view_contact = view_contact + ", ";
	// }
	// }
	// } else {
	// }
	// crs1.close();
	// }
	// }
	// view_count = view_count + "]";
	// view_contact = view_contact + "]";
	// crs.close();
	// // SOP("view_count = " + view_count);
	// // SOP("view_contact = " + view_contact);
	//
	// } catch (Exception ex) {
	// SOPError(ClientName+"===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	//
	// // } else {
	// // VisitorsChart = "<br>No Newsletters Sent!";
	// // }
	// }

}
