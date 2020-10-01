package axela.mktg;
//sangita

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Testmail extends Connect {

	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String msg = "";
	public String email_id = "";
	public String StrSql = "";
	public String go = "";
	public String send = "";
	public String sendb = "";
	public String campaign_id = "";
	public String campaign_branch_id = "";
	public String campaign_subject = "";
	public String campaign_msg = "";
	public String campaign_sentdate = "";
	public String branch_name = "";
	public String QueryString = "";
	public String email_from = "";
	public String comp_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = session.getAttribute("emp_id").toString();
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mktg_campaign_access", request, response);
			send = PadQuotes(request.getParameter("send"));
			sendb = PadQuotes(request.getParameter("send_button"));
			campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
			go = PadQuotes(request.getParameter("submit_button"));
			email_id = PadQuotes(request.getParameter("txt_email_id"));
			QueryString = PadQuotes(request.getQueryString());

			StrSql = "SELECT campaign_id, campaign_branch_id, campaign_subject, campaign_msg, "
					+ " campaign_sentdate, campaign_entry_id, campaign_entry_date, "
					+ " CONCAT(branch_name,' (',branch_code,')') as branch_name, "
					+ " CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM axela_mktg_campaign"
					+ " INNER JOIN axela_branch on branch_id = campaign_branch_id"
					+ " INNER JOIN axela_emp on emp_id = campaign_entry_id"
					+ " WHERE campaign_id = " + campaign_id;
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				campaign_branch_id = crs.getString("campaign_branch_id");
				branch_name = crs.getString("branch_name");
				campaign_subject = crs.getString("campaign_subject");
				campaign_msg = unescapehtml(crs.getString("campaign_msg"));
				entry_by = "<a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("campaign_entry_id") + ">" + crs.getString("emp_name") + "</a>";
				entry_date = strToLongDate(crs.getString("campaign_entry_date"));
			}
			crs.close();

			email_from = ExecuteQuery("select branch_email1 from axela_branch where branch_id = " + campaign_branch_id);

			if (go.equals("Go") && !email_id.equals("")) {
				StrSql = "INSERT INTO axela_email"
						+ "("
						+ " email_lead_id,"
						+ " email_lead,"
						+ " email_campaign_id,"
						+ " email_from,"
						+ " email_to,"
						+ " email_subject,"
						+ " email_msg, "
						+ " email_date,"
						+ " email_entry_id,"
						+ " email_sent)"
						+ " values"
						+ " ("
						+ " 0,"
						+ " '',"
						+ " " + campaign_id + ","
						+ " '" + email_from + "',"
						+ " '" + email_id + "',"
						+ " '" + campaign_subject + "',"
						+ " '" + campaign_msg + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " " + emp_id + ","
						+ " 0"
						+ " )";
				updateQuery(StrSql);
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				} else {
					response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?campaign_id=" + campaign_id + "&msg=Campaign sent successfully!"));
				}
			}

			if (sendb.equals("Send")) {
				StrSql = "SELECT campaign_sentdate FROM axela_mktg_campaign WHERE campaign_id = " + campaign_id + "";

				if (ExecuteQuery(StrSql).equals("")) {
					campaign_subject = "replace('" + campaign_subject + "','[LEADNAME]',lead_fname)";
					campaign_subject = "replace(" + campaign_subject + ",'[LEADID]',lead_id)";
					campaign_subject = "replace(" + campaign_subject + ",'[LEADJOBTITLE]',lead_jobtitle)";
					campaign_subject = "replace(" + campaign_subject + ",'[LEADMOBILE]',lead_mobile)";
					campaign_subject = "replace(" + campaign_subject + ",'[LEADPHONE]',lead_phone)";
					campaign_subject = "replace(" + campaign_subject + ",'[LEADEMAIL]',lead_email)";

					campaign_msg = "replace('" + campaign_msg + "','[LEADNAME]',lead_fname)";
					campaign_msg = "replace(" + campaign_msg + ",'[LEADID]',lead_id)";
					campaign_msg = "replace(" + campaign_msg + ",'[LEADJOBTITLE]',lead_jobtitle)";
					campaign_msg = "replace(" + campaign_msg + ",'[LEADMOBILE]',lead_mobile)";
					campaign_msg = "replace(" + campaign_msg + ",'[LEADPHONE]',lead_phone)";
					campaign_msg = "replace(" + campaign_msg + ",'[LEADEMAIL]',lead_email)";
					StrSql = "SELECT lead_id, concat(title_desc, ' ', lead_fname,' ', lead_lname), " + campaign_id + ", '" + email_from + "',"
							+ " lead_email,"
							+ "" + campaign_subject + ","
							+ "" + campaign_msg + ","
							+ " " + ToLongDate(kknow()) + ", " + emp_id + ", 0"
							+ " FROM axela_mktg_lead"
							+ " INNER JOIN axela_title ON title_id = lead_title_id"
							+ " INNER JOIN axela_branch ON branch_id = lead_branch_id"
							+ " INNER JOIN axela_mktg_campaign ON campaign_branch_id = branch_id";

					StrSql = StrSql + " WHERE campaign_id = " + campaign_id
							+ " AND lead_unsubscribed = 0 "
							+ " And lead_email!=''"
							+ " GROUP BY lead_id "
							+ " order by lead_id desc ";

					if (ReturnPerm(comp_id, "emp_mktg_campaign_send", request).equals("1")) {
						SendCampaign();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?campaign_id=" + campaign_id + "&msg=Campaign sent successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}

				}
			}
		} catch (Exception ex) {

			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendCampaign() throws SQLException {
		try {

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			StrSql = "INSERT INTO axela_email"
					+ "(email_lead_id, email_lead, email_campaign_id, email_from, email_to, "
					+ " email_subject, email_msg, email_date, email_entry_id, email_sent)"
					+ StrSql;

			stmttx.execute(StrSql);

			StrSql = "UPDATE axela_mktg_campaign "
					+ " SET "
					+ " campaign_sentdate = " + ToLongDate(kknow()) + ""
					+ " WHERE campaign_id = " + campaign_id + "";

			stmttx.execute(StrSql);

			conntx.commit();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("Connection rollback...");
			}
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
