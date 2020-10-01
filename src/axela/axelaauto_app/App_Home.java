//Shilpashree
package axela.axelaauto_app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Home extends Connect {

	public String StrSql = "";
	public String emp_id = "0";
	public String emp_uuid = "0";
	public String emp_active = "";
	public String ExeAccess = "";
	public CachedRowSet crs = null;
	public String level1 = "0";
	public String level2 = "0";
	public String level3 = "0";
	public String level4 = "0";
	public String level5 = "0";
	public String comp_logo = "";
	public String image = "";
	public String followupchart_data = "";
	public String NoSalesPipeline = "";
	public String BranchAccess = "";
	public String monthenquires = "0";
	public String monthbooking = "0";
	public String monthdeliveries = "0";
	public String monthcancellations = "0";
	public String todayenquires = "0";
	public String todaybooking = "0";
	public String todaydeliveries = "0";
	public String totalenquires = "0";
	public String totalhotenquires = "0";
	public String totalbooking = "0";
	public String date = "";
	public String month = "", msg = "";
	public int TotalRecords = 0;
	public String emp_name = "";
	public String branch_name = "";
	public String chart_data = "";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public String emp_branch_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(true);
		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		SetSession("comp_id", comp_id, request);
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		emp_all_exe = GetSession("emp_all_exe", request);
		emp_branch_id = CNumeric(PadQuotes(session.getAttribute("emp_branch_id") + ""));
		// if (emp_all_exe.equals("1"))
		// {
		// ExeAccess = "";
		// }

		if (!emp_id.equals("0")) {
			date = ToShortDate(kknow()).substring(4, 6);
			month = TextMonth(Integer.parseInt(date) - 1);
			msg = PadQuotes(request.getParameter("msg"));
			// / / comp_logo = ExecuteQuery("SELECT comp_logo FROM " + compdb(comp_id) + "axela_comp WHERE 1=1 ");
			// image = "<img src=\"../ThumbnailAxelaApp.do?complogo=" + comp_logo + "&width=100\">";
			String StrSql1 = "SELECT emp_name, emp_active, COALESCE(branch_name,'') AS branch_name, COALESCE(branch_logo,'') AS branch_logo, emp_branch_id, comp_name, comp_logo"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id, "
					+ compdb(comp_id) + "axela_comp"
					+ " WHERE emp_id = " + emp_id;
			CachedRowSet crs1 = processQuery(StrSql1, 0);
			try {
				while (crs1.next()) {
					emp_active = crs1.getString("emp_active");
					emp_name = crs1.getString("emp_name");
					branch_name = crs1.getString("branch_name");
					emp_branch_id = crs1.getString("emp_branch_id");
					if (emp_branch_id.equals("0"))
					{
						branch_name = crs1.getString("comp_name");
						image = "<img src=\"../Thumbnail.do?complogo=" + crs1.getString("comp_logo") + "&width=150&rand=" + Math.random() + "\" alt=''>";
					}
					else {
						branch_name = crs1.getString("branch_name");
						image = "<img src=\"../Thumbnail.do?branchlogo=" + crs1.getString("branch_logo") + "&width=150&rand=" + Math.random() + "\" alt=''>";
					}
				}
				crs1.close();
			} catch (Exception ex) {
				SOPError("Axelaauto-APP===" + this.getClass().getName());
				SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

			if (emp_active.equals("1")) {
				FollowupEscStatus();
				try {
					StrSql = "SELECT COALESCE(SUM(IF(SUBSTR(enquiry_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6),1,0)),0) AS monthenquires,"
							+ " COALESCE(SUM(IF(SUBSTR(enquiry_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8),1,0)),0) AS todayenquires,"
							+ " COALESCE(SUM(IF(enquiry_status_id=1,1,0)),0) AS totalenquires,"
							+ " COALESCE(SUM(IF(enquiry_status_id=1 AND enquiry_priorityenquiry_id=1,1,0)),0) AS totalhotenquires"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " WHERE 1=1"
							+ BranchAccess.replace("branch_id", "enquiry_branch_id");
					if (emp_all_exe.equals("0"))
					{
						StrSql += ExeAccess.replace("emp_id", "enquiry_emp_id");
					}

					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							monthenquires = crs.getString("monthenquires");
							todayenquires = crs.getString("todayenquires");
							totalenquires = crs.getString("totalenquires");
							totalhotenquires = crs.getString("totalhotenquires");
						}

					}
					crs.close();

					StrSql = "SELECT COALESCE(SUM(IF(SUBSTR(so_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6) AND so_active!='0',1,0)),0) AS monthbooking,"
							+ " COALESCE(SUM(IF(SUBSTR(so_delivered_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6),1,0)),0) AS monthdeliveries,"
							+ " COALESCE(SUM(IF(SUBSTR(so_cancel_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6),1,0)),0) AS monthcancellations,"
							+ " COALESCE(SUM(IF(SUBSTR(so_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8) AND so_active!='0',1,0)),0) AS todaybooking,"
							+ " COALESCE(SUM(IF(SUBSTR(so_delivered_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8),1,0)),0) AS todaydeliveries,"
							+ " COALESCE(SUM(IF(so_delivered_date='' AND so_cancel_date='' AND so_active!='0',1,0)),0) AS totalbooking"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " WHERE 1=1"
							+ BranchAccess.replace("branch_id", "so_branch_id");
					if (emp_all_exe.equals("0"))
					{
						StrSql += ExeAccess.replace("emp_id", "so_emp_id");
					}
					// SOP("StrSql--home---" + StrSql);
					crs1 = processQuery(StrSql, 0);
					if (crs1.isBeforeFirst()) {
						while (crs1.next()) {
							monthbooking = crs1.getString("monthbooking");
							monthdeliveries = crs1.getString("monthdeliveries");
							monthcancellations = crs1.getString("monthcancellations");
							todaybooking = crs1.getString("todaybooking");
							todaydeliveries = crs1.getString("todaydeliveries");
							totalbooking = crs1.getString("totalbooking");
						}

					}
					crs1.close();
					// SOP("StrSql===homequery==== = " + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);

				} catch (Exception e) {
					SOPError("Axelaauto-APP===" + this.getClass().getName());
					SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}

			}

		}
	}

	public void FollowupEscStatus() {
		try {
			StrSql = " SELECT gr.group_id" + " AS group_id,"
					+ " COUNT(enquiry_id)" + " AS triggercount "
					+ " FROM ( " + " SELECT 1 AS group_id "
					+ " UNION " + " SELECT 2 AS group_id "
					+ " UNION " + " SELECT 3 AS group_id "
					+ " UNION " + " SELECT 4 AS group_id "
					+ " UNION " + " SELECT 5 AS group_id " + " )" + " AS gr "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup" + " ON followup_trigger=gr.group_id "
					+ " AND followup_desc = ''"
					+ " AND followup_trigger > 0  "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry" + " ON enquiry_id = followup_enquiry_id "
					+ " AND enquiry_status_id=1 " + BranchAccess.replace("branch_id", "enquiry_branch_id");
			if (emp_all_exe.equals("0"))
			{
				StrSql += ExeAccess.replace("emp_id", "enquiry_emp_id");
			}

			StrSql += " WHERE 1=1 " + " GROUP BY group_id "
					+ " ORDER BY group_id DESC";
			CachedRowSet crs = processQuery(StrSql, 0);
			followupchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (crs.getString("group_id").equals("1")) {
						level1 = crs.getString("triggercount");
					}
					if (crs.getString("group_id").equals("2")) {
						level2 = crs.getString("triggercount");
					}
					if (crs.getString("group_id").equals("3")) {
						level3 = crs.getString("triggercount");
					}
					if (crs.getString("group_id").equals("4")) {
						level4 = crs.getString("triggercount");
					}
					if (crs.getString("group_id").equals("5")) {
						level5 = crs.getString("triggercount");
					}

				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-APP===" + this.getClass().getName());
			SOPError("Axelaauto-APP===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
