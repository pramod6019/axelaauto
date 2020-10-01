package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import axela.jobs.FCM;
import cloudify.connect.Connect;

public class Notification_exe_send extends Connect {

	public String notification_id = "0";
	public String notification_msg = "";
	public String sms_date = "";
	public String sms_sent = "";
	public String sms_student = "";
	public String sms_entry_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_branch_id = "", branch_id = "0", branchfilter = "";
	public String emp_role_id = "0";
	public String status = "";
	public String sendB = "";
	public String msg = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String comp_sms_enable = "";
	public String[] exe_team_trans;
	public String[] exe_jobtitle_trans;
	public String[] exe_team_ids;

	public String sms_msg = "";
	public String notifiction_title = "";
	public String notification_allexe = "1";
	private String ExeAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_notification_send", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (branch_id.equals("0")) {
					emp_branch_id = PadQuotes((request.getParameter("dr_branch")));
				} else {
					emp_branch_id = branch_id;
				}
				if (emp_branch_id.equals("")) {
					emp_branch_id = "-1";
				}
				sendB = PadQuotes(request.getParameter("send_button"));
				msg = PadQuotes(request.getParameter("msg"));
				notification_id = PadQuotes(request.getParameter("notification_id"));
				notification_msg = PadQuotes(request.getParameter("txt_notification_msg"));
				if (!"Send".equals(sendB)) {
					sms_date = "";
					sms_sent = "";
					sms_entry_id = "";
				} else {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						if (msg.equals("")) {
							ListExe();
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void ListExe() {
		String StrSearch = "";
		if (notification_msg.length() > 500) {
			notification_msg = notification_msg.substring(0, 499);
		}
		try {
			notification_msg = "REPLACE('" + notification_msg + "','[NAME]',emp_name)";
			notification_msg = "REPLACE(" + notification_msg + ",'[EXECUTIVEID]',emp_id)";
			notification_msg = "REPLACE(" + notification_msg + ",'[EXECUTIVEREFNO]',emp_ref_no)";

			StrSql = "SELECT "
					+ " COALESCE(emp_device_fcmtoken, '') AS emp_device_fcmtoken,"
					+ " COALESCE(emp_device_os, '') AS emp_device_os,"
					+ " IF(emp_branch_id=0,1,emp_branch_id) AS branch_id, "
					+ " " + notification_msg + "AS message, "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " emp_id, "
					+ "emp_name,"
					+ " " + emp_id + ","
					+ " '" + notifiction_title + "'As title,"
					+ "1"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";

			StrSql += " WHERE 1=1"
					+ " AND emp_active = '1'"
					+ BranchAccess
					+ ExeAccess;

			if (notification_allexe.equals("1") && exe_jobtitle_trans == null) {
				if (emp_branch_id.equals("-1")) {
					StrSearch = StrSearch + " ";
				} else if (emp_branch_id.equals("0")) {
					StrSearch = StrSearch + " AND emp_branch_id = 0";
				} else {
					StrSearch = StrSearch + " AND (emp_branch_id = " + emp_branch_id
							+ " OR emp_id IN (SELECT " + compdb(comp_id) + "axela_emp_branch.emp_id"
							+ " FROM " + compdb(comp_id) + "axela_emp_branch"
							+ " WHERE " + compdb(comp_id) + "axela_emp_branch.emp_branch_id = " + emp_branch_id + "))";
				}
			} else if (notification_allexe.equals("1") && exe_jobtitle_trans != null) {
				String id = "";
				for (int i = 0; i < exe_jobtitle_trans.length; i++) {
					id = id + " " + exe_jobtitle_trans[i] + ", ";
				}
				StrSearch = StrSearch + " AND emp_jobtitle_id IN (" + id.substring(0, id.lastIndexOf(",")) + ")";
			} else if (notification_allexe.equals("0")) {
				if (exe_jobtitle_trans != null) {
					String id = "";
					for (int i = 0; i < exe_jobtitle_trans.length; i++) {
						id = id + " " + exe_jobtitle_trans[i] + ", ";
					}
					StrSearch = StrSearch + " AND emp_jobtitle_id IN (" + id.substring(0, id.lastIndexOf(",")) + ")";
				}
				if (exe_team_trans != null) {
					String id = "";
					for (int i = 0; i < exe_team_trans.length; i++) {
						id = id + " " + exe_team_trans[i] + ", ";
					}
					StrSearch = StrSearch + " AND emp_id IN (" + id.substring(0, id.lastIndexOf(",")) + ")";
				}

			}
			if (!emp_role_id.equals("0")) {
				StrSearch = StrSearch + " AND emp_role_id =" + emp_role_id;
			}

			StrSql = StrSql + StrSearch
					+ " AND emp_device_fcmtoken != ''"
					+ " AND emp_device_os != ''"
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_id DESC ";

			SOP("StrSql=12=" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (!crs.getString("emp_device_fcmtoken").equals("") && !crs.getString("emp_device_os").equals(""))
					new FCM().SendPushNotification(crs.getString("emp_device_fcmtoken"), crs.getString("emp_device_os"), notifiction_title, crs.getString("message"));
			}
			crs.close();
			// AS we need it for FCM but not for WEB PART we are replcing
			StrSql = StrSql.replace("COALESCE(emp_device_os, '') AS emp_device_os,", "");
			// SOP("StrSql===" + StrSql);
			AddFields(StrSql, notifiction_title);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String AddFields(String StrSql, String notifiction_title) {
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_notification"
						+ " (notification_fcmtoken ,"
						+ " notification_branch_id ,"
						+ " notification_msg ,"
						+ " notification_date ,"
						+ " notification_emp_id ,"
						+ " notification_emp ,"
						+ " notification_entry_id ,"
						+ " notification_title,"
						+ " notification_sent"
						+ ")"
						+ "" + StrSql + "";
				// SOP("StrSql=123==" + StrSql);
				updateQuery(StrSql);
				msg = "Notification Sent Successfully!";

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return msg;
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			notification_id = CNumeric(PadQuotes(request.getParameter("notification_id")));
			notification_msg = PadQuotes(request.getParameter("txt_notification_msg"));
			notifiction_title = PadQuotes(request.getParameter("notifiction_title"));
			emp_role_id = CNumeric(PadQuotes(request.getParameter("dr_role")));
			notification_allexe = PadQuotes(request.getParameter("notification_allexe"));

			String exe_jobtitle_trans1 = PadQuotes((RetrunSelectArrVal(request, "dr_title_trans")));
			String exe_team_id1 = PadQuotes((RetrunSelectArrVal(request, "dr_executive")));
			String exe_team_trans1 = PadQuotes((RetrunSelectArrVal(request, "exe_team_trans")));

			if (exe_jobtitle_trans1 != null && !exe_jobtitle_trans1.equals("")) {
				exe_jobtitle_trans = exe_jobtitle_trans1.split(",");
			}
			if (exe_team_id1 != null && !exe_team_id1.equals("")) {
				exe_team_ids = exe_team_id1.split(",");
			}
			if (exe_team_trans1 != null && !exe_team_trans1.equals("")) {
				exe_team_trans = exe_team_trans1.split(",");
			}

			if (emp_role_id.equals("")) {
				emp_role_id = "0";
			}

			if (notification_allexe.equals("on")) {
				notification_allexe = "1";
			}
			else {
				notification_allexe = "0";
			}
			if (notification_allexe.equals("1")) {
				exe_team_trans = null;
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (notification_allexe.equals("0")) {
			if (exe_team_trans == null) {
				msg = msg + "<br>Select Executives!";
			}
		}
		if (notifiction_title.equals("")) {
			msg = msg + "<br>Enter Title!";
		}
		if (notification_msg.equals("")) {
			msg = msg + "<br>Enter Message!";
		}
	}

	public String PopulateBranch() {
		StringBuilder stringval = new StringBuilder();
		try {

			String StrSql = " SELECT branch_id,branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active='1' "
					+ BranchAccess
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";
			// SOP("StrSql=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =-1 " + StrSelectdrop("0", emp_branch_id) + " >All Branches</option>");
			stringval.append("<option value = 0 " + StrSelectdrop("0", emp_branch_id) + " >Head Office</option>");
			while (crs.next()) {
				stringval.append("<option value=" + crs.getString("branch_id") + "");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), emp_branch_id));
				stringval.append(">" + crs.getString("branch_name") + " (" + crs.getString("branch_code") + ")</option> \n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobTitle() {
		StringBuilder stringval = new StringBuilder();
		try {

			String StrSql = " SELECT jobtitle_id,jobtitle_desc "
					+ " FROM " + compdb(comp_id) + "axela_jobtitle "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp "
					+ " ON emp_jobtitle_id=jobtitle_id "
					+ " WHERE 1=1";
			if (!emp_branch_id.equals("-1") && !emp_branch_id.equals("")) {
				StrSql += " AND emp_branch_id=" + emp_branch_id;
			}
			StrSql += BranchAccess.replace("branch_id", "emp_branch_id")
					+ " GROUP BY jobtitle_id"
					+ " ORDER BY  jobtitle_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				stringval.append("<option value=" + crs.getString("jobtitle_id") + "");
				stringval.append(ArrSelectdrop(crs.getInt("jobtitle_id"), exe_team_trans));
				stringval.append(">" + crs.getString("jobtitle_desc") + "</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobTitleTrans(String[] exe_jobtitle_trans) {
		StringBuilder Str = new StringBuilder();
		try {
			String StrSql = " SELECT jobtitle_id,jobtitle_desc "
					+ " FROM " + compdb(comp_id) + "axela_jobtitle "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_jobtitle_id = jobtitle_id "
					+ " WHERE 1=1";
			if (!emp_branch_id.equals("-1") && !emp_branch_id.equals("")) {
				StrSql += " AND emp_branch_id=" + emp_branch_id;
			}
			StrSql += BranchAccess.replace("branch_id", "emp_branch_id")
					+ " GROUP BY jobtitle_id"
					+ " ORDER BY  jobtitle_desc";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (exe_jobtitle_trans != null) {
					for (int i = 0; i < exe_jobtitle_trans.length; i++) {
						if (crs.getString("jobtitle_id").equals(exe_jobtitle_trans[i])) {
							Str.append("<option value=").append(crs.getString("jobtitle_id"));
							Str.append(" selected>").append(crs.getString("jobtitle_desc"));
							Str.append("</option>\n");
						}
					}
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutives() {
		try {
			StrSql = " SELECT emp_id,emp_name,emp_ref_no "
					+ "FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id "
					+ " WHERE  emp_active='1'"
					+ " AND emp_device_fcmtoken != ''"
					+ " AND emp_device_os != ''"
					+ BranchAccess
					+ ExeAccess;
			// SOP("emp_branch_id=====" + emp_branch_id);
			StrSql = StrSql + " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			StringBuilder Exec = new StringBuilder();;
			while (crs.next()) {
				if (exe_team_ids != null) {
					for (int i = 0; i < exe_team_ids.length; i++) {
						if (crs.getString("emp_id").equals(exe_team_ids[i])) {
							Exec.append("<option value=").append(crs.getString("emp_id"));
							Exec.append(" selected>").append(crs.getString("emp_name"));
							Exec.append(" (").append(crs.getString("emp_ref_no")).append(")</option>\n");
						}
					}
				} else {
					Exec = Exec.append("");
				}
			}
			crs.close();
			return Exec.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutivesTrans(String[] exe_team_trans) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no" + " FROM "
					+ compdb(comp_id) + "axela_emp" + " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_emp_exe AS ex ON ex.empexe_id = emp_id"
					+ " WHERE ex.empexe_emp_id = '" + emp_id + "'" + " AND "
					+ compdb(comp_id) + "axela_emp.emp_active = '1'"
					+ " ORDER BY " + compdb(comp_id) + "axela_emp.emp_name";
			// SOP("StrSql===exe tramns==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (exe_team_trans != null) {
					for (int i = 0; i < exe_team_trans.length; i++) {
						if (crs.getString("emp_id").equals(exe_team_trans[i])) {
							Str.append("<option value=").append(crs.getString("emp_id"));
							Str.append(" selected>").append(crs.getString("emp_name"));
							Str.append(" (").append(crs.getString("emp_ref_no")).append(")</option>\n");
						}
					}
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
