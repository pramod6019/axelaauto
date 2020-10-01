package axela.mktg;
//Saiman 27th june 2013

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

public class Send_Campaign extends Connect {

	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String msg = "";
	public String email_id = "";
	public String StrSql = "";
	public String go = "";
	public String send = "";
	public String sendb = "";
	public String campaign_id = "";
	public String campaign_branch_id = "0";
	public String campaign_target = "";
	public String campaign_subject = "";
	public String campaign_msg = "";
	public String campaign_sentdate = "";
	public String branch_name = "";
	public String QueryString = "";
	public String email_from = "";
	public String branch_id = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mktg_campaign_access", request, response);
			emp_id = session.getAttribute("emp_id").toString();
			campaign_id = CNumeric(PadQuotes(request.getParameter("dr_campaign")));
			send = PadQuotes(request.getParameter("submit_button"));
			QueryString = PadQuotes(request.getQueryString());
			// SOP("QueryString = " + QueryString);
			branch_id = (session.getAttribute("emp_branch_id")).toString();
			if (session.getAttribute("leadstrsql").toString().equals("")) {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?&msg=No Opportunity Selected!"));
			}
			if (branch_id.equals("0")) {
				campaign_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			} else {
				campaign_branch_id = branch_id;
			}

			if (send.equals("Send Now")) {
				if (!campaign_id.equals("0")) {
					StrSql = "select campaign_subject, campaign_msg, campaign_sentdate "
							+ " from axela_mktg_campaign "
							+ " where campaign_id = " + campaign_id;
					CachedRowSet crs = processQuery(StrSql);
					while (crs.next()) {
						campaign_subject = crs.getString("campaign_subject");
						campaign_msg = crs.getString("campaign_msg");
						campaign_sentdate = crs.getString("campaign_sentdate");
					}
					crs.close();

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

					StrSql = " SELECT concat(title_desc, ' ', lead_fname,' ', lead_lname), " + campaign_id + ","
							+ " branch_email1, lead_email ,"
							+ " " + campaign_subject + ", " + campaign_msg + ","
							+ " " + ToLongDate(kknow()) + ", " + emp_id + ", 0"
							+ " from axela_mktg_lead"
							+ " inner join axela_branch on branch_id = lead_branch_id"
							+ " INNER JOIN axela_mktg_campaign ON campaign_branch_id = branch_id "
							+ " inner join axela_title on title_id=lead_title_id"
							+ " left join axela_empcount on empcount_id=lead_empcount_id"
							+ " inner join axela_emp on emp_id = lead_emp_id"
							+ " left join axela_soe on soe_id=lead_soe_id"
							+ " left join axela_sob on sob_id=lead_sob_id"
							+ " WHERE lead_active='1'"
							+ " and campaign_id = " + campaign_id
							// + " AND lead_unsubscribed = 0"
							+ " And lead_email!='' " + session.getAttribute("leadstrsql")
							+ " GROUP BY lead_id  order by lead_id desc ";
					// SOP("StrSql check lead -- " + StrSql);
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

				} else {
					msg = "Error! </br>Select a Campaign to send!";
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
					+ "(email_contact, email_campaign_id, email_from, email_to, "
					+ " email_subject, email_msg, email_date, email_entry_id, email_sent)"
					+ StrSql;
			// SOP("StrSql--+++++++++++" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);

			if (campaign_sentdate.equals("")) {
				StrSql = "UPDATE axela_mktg_campaign "
						+ " SET "
						+ " campaign_sentdate = " + ToLongDate(kknow()) + ""
						+ " WHERE campaign_id = " + campaign_id + " and campaign_sentdate=''";
				stmttx.execute(StrSql);
			}

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

	public String PopulateCampaign() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " Select campaign_id, campaign_subject, campaign_entry_date"
					+ " FROM axela_mktg_campaign"
					+ " where campaign_entry_date <=" + ToLongDate(kknow()) + " "
					+ " and campaign_entry_date> " + ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), -31, 0, 0))
					+ " and campaign_branch_id=" + branch_id
					+ " GROUP BY campaign_id"
					+ " ORDER BY campaign_entry_date desc";
			CachedRowSet crs = processQuery(StrSql);
			// SOP("strsql==="+StrSql);
			Str.append("<select name=dr_campaign id=dr_campaign class=selectbox ><option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(">").append(crs.getString("campaign_subject")).append(" - ").append(strToLongDate(crs.getString("campaign_entry_date"))).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
