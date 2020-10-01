package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Spawn_Emp extends Connect {

	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String emp_branch_id = "";
	public String emp_role_id = "";
	public String emp_all_exe = "", emp_all_branches = "";
	public String emp_clicktocall = "";
	public String emp_routeno = "";
	public String emp_callerid = "";
	public String emp_recperpage = "";
	public int emp_timeout = 0;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// SOP("111");

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_id", request, response);
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				SetSession("comp_id", null, request);
				StrSql = " select emp_id, emp_branch_id, emp_role_id, emp_all_exe, emp_all_branches,"
						+ " emp_clicktocall, emp_routeno, emp_callerid, emp_recperpage, emp_timeout"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE 1=1"
						+ " AND emp_active = 1"
						+ " AND emp_id=" + emp_id;
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					emp_branch_id = crs.getString("emp_branch_id");
					emp_role_id = crs.getString("emp_role_id");
					emp_all_exe = crs.getString("emp_all_exe");
					emp_all_branches = crs.getString("emp_all_branches");
					emp_clicktocall = crs.getString("emp_clicktocall");
					emp_routeno = crs.getString("emp_routeno");
					emp_callerid = crs.getString("emp_callerid");
					emp_recperpage = crs.getString("emp_recperpage");
					emp_timeout = crs.getInt("emp_timeout");
				}
				crs.close();
				SetSession("admin", "yes", request);
				AssignSession(comp_id, emp_id, emp_branch_id, emp_role_id, emp_all_exe, emp_all_branches, emp_clicktocall, emp_routeno, emp_callerid, emp_recperpage, emp_timeout, request);
				// update the emp_user table for checking duplicate Sign IN
				// StrSql = "UPDATE " + compdb(comp_id) + "axela_emp_user"
				// + " SET"
				// + " user_emp_id = " + emp_id + ""
				// + " WHERE user_jsessionid = '" + session.getId() + "'";
				// updateQuery(StrSql);

				// if (AppRun().equals("1")) {
				// String user_jsessionid = session.getId();
				// int signincount = 1;
				//
				// updateQuery("UPDATE " + compdb(comp_id) + "axela_emp_log"
				// + " SET"
				// + " log_signout_time = '" + ToLongDate(kknow()) + "'"
				// + " WHERE log_emp_id = " + emp_id + ""
				// + " AND log_signout_time = ''");
				// // SOP("signincount = " + signincount);
				//
				// updateQuery("INSERT INTO " + compdb(comp_id) + "axela_emp_log"
				// + " (log_emp_id,"
				// + " log_session_id,"
				// + " log_remote_host,"
				// + " log_remote_agent,"
				// + " log_attemptcount,"
				// + " log_signin_time, "
				// + " log_signout_time)"
				// + " VALUES"
				// + " (" + emp_id + ","
				// + " '" + user_jsessionid + "',"
				// + " '" + request.getRemoteHost() + "',"
				// + " '" + request.getHeader("User-Agent") + "',"
				// + " " + signincount + ","
				// + " '" + ToLongDate(kknow()) + "',"
				// + " '')");
				//
				// updateQuery("DELETE FROM " + compdb(comp_id) + "axela_emp_user"
				// + " WHERE user_emp_id = " + emp_id);
				//
				// updateQuery("INSERT INTO " + compdb(comp_id) + "axela_emp_user"
				// + " (user_jsessionid,"
				// + " user_emp_id,"
				// + " user_logintime,"
				// + " user_ip,"
				// + " user_agent)"
				// + " VALUES"
				// + " ('" + user_jsessionid + "',"
				// + " " + emp_id + ","
				// + " '" + ToLongDate(kknow()) + "',"
				// + " '" + request.getRemoteHost() + "',"
				// + " '" + request.getHeader("User-Agent") + "')");
				// }

				response.sendRedirect(response.encodeRedirectURL("../portal/home.jsp"));
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
