package axela.mktg;
//Sangita 18th june 2013,modified on 21st june
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Campaign_Update extends Connect {

	public String email_from = "";
	public String campaign_subject = "";
	public String campaign_msg = "";
	public String campaign_sentdate = "";
	public String campaign_branch_id = "0";
	public String branch_id = "0";
	public String campaign_entry_id = "0";
	public String campaign_entry_date = "0";
	public String campaign_modified_id = "0";
	public String campaign_modified_date = "0";
	public String campaign_id = "0", emp_id = "0";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String msg = "";
	public String StrHTML = "";
	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String StrSql = "";
	public String status = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String comp_id = "0";
	public String BranchAccess = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			branch_id = CNumeric(PadQuotes(GetSession("emp_branch_id", request) + ""));
			CheckPerm(comp_id, "emp_mktg_campaign_access", request, response);
			msg = PadQuotes(request.getParameter("msg"));
			add = PadQuotes(request.getParameter("add"));
			update = PadQuotes(request.getParameter("update"));
			addB = PadQuotes(request.getParameter("add_button"));
			updateB = PadQuotes(request.getParameter("update_button"));
			deleteB = PadQuotes(request.getParameter("delete_button"));
			campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
			QueryString = PadQuotes(request.getQueryString());

			if (add.equals("yes")) {
				status = "Add";
			} else if (update.equals("yes")) {
				status = "Update";
			}

			if ("yes".equals(add)) {
				if (!"yes".equals(addB)) {
				} else {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_mktg_campaign_add", request).equals("1")) {
						campaign_entry_id = emp_id;
						campaign_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?all=recent&msg=Campaign added successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}

			if (update.equals("yes")) {
				if (!"yes".equals(updateB) && !"Delete Campaign".equals(deleteB)) {
					PopulateFields();
				} else if ("yes".equals(updateB) && !"Delete Campaign".equals(deleteB)) {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_mktg_campaign_edit", request).equals("1")) {
						campaign_modified_id = emp_id;
						campaign_modified_date = ToLongDate(kknow());
						UpdateFields(StrSql);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?campaign_id=" + campaign_id + "&msg=Campaign updated successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				} else if ("Delete Campaign".equals(deleteB)) {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_mktg_campaign_delete", request).equals("1")) {
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("campaign-list.jsp?msg=Campaign deleted successfully!"));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		campaign_branch_id = CNumeric(PadQuotes(request.getParameter("dr_campaign_branch_id")));
		campaign_subject = PadQuotes(request.getParameter("txt_campaign_subject"));
		campaign_msg = PadQuotes(request.getParameter("txt_campaign_msg"));
		SOP("campaign_msg--------" + campaign_msg);
	}

	protected void CheckForm() {
		msg = "";
		if (campaign_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (campaign_subject.equals("")) {
			msg = msg + "<br>Enter Subject!";
		}
		if (campaign_msg.equals("")) {
			msg = msg + "<br>Enter Message!";
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "INSERT into " + compdb(comp_id) + "axela_mktg_campaign"
						+ " ("
						+ " campaign_branch_id,"
						+ " campaign_subject,"
						+ " campaign_msg,"
						+ " campaign_attach1,"
						+ " campaign_sentdate,"
						+ " campaign_entry_id,"
						+ " campaign_entry_date)"
						+ " values"
						+ " ("
						+ " " + campaign_branch_id + ","
						+ " '" + campaign_subject + "',"
						+ " '" + campaign_msg + "',"
						+ " '',"
						+ " '',"
						+ " " + campaign_entry_id + ","
						+ " '" + ToLongDate(kknow()) + "'"
						+ " )";
				// SOP("StrSql add==" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

			}
		}
	}

	protected void PopulateFields() {
		StrSql = " SELECT campaign_branch_id, campaign_subject, campaign_msg"
				+ " FROM " + compdb(comp_id) + "axela_mktg_campaign"
				+ " WHERE campaign_id = " + campaign_id + "";
		try {
			CachedRowSet crs = processQuery(StrSql);
			while (crs.next()) {
				campaign_branch_id = crs.getString("campaign_branch_id");
				campaign_subject = crs.getString("campaign_subject");
				campaign_msg = crs.getString("campaign_msg");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(String Sql) throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_mktg_campaign"
						+ " SET "
						+ " campaign_branch_id= " + campaign_branch_id + ", "
						+ " campaign_subject= '" + campaign_subject + "', "
						+ " campaign_msg= '" + campaign_msg + "', "
						+ " campaign_modified_id= " + campaign_modified_id + ", "
						+ " campaign_modified_date= '" + campaign_modified_date + "'"
						+ " where campaign_id = " + campaign_id + " ";
				// SOP(" StrSql--" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " from " + compdb(comp_id) + "axela_branch"
					+ " where 1 = 1 " + BranchAccess
					+ " order by branch_name";
			CachedRowSet crs = processQuery(StrSql);
			Str.append("<option value = 0> Select </option>");
			// Str.append("<option value =0 " + StrSelectdrop("0", quote_branch_id) + ">Head Office</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), campaign_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	protected void DeleteFields() {
		try {
			StrSql = "SELECT email_campaign_id FROM axela_email"
					+ " WHERE email_campaign_id = " + campaign_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Campaign has emails associated!";
			}
			if (msg.equals("")) {
				StrSql = "DELETE FROM axela_mktg_campaign"
						+ " WHERE campaign_id = " + campaign_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
