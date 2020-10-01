package axela.portal;
//////// Modified by Ceeba on  October 17, 2008
//Saiman 28th Aug 2012
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Sms_Exe_Send extends Connect {

	public String sms_id = "0";
	public String sms_mobileno = "";
	public String sms_msg = "";
	public String sms_date = "";
	public String sms_sent = "";
	public String sms_student = "";
	public String sms_entry_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_branch_id = "0", branch_id = "0", branchfilter = "";
	public String emp_role_id = "0", emp_jobtitle_id = "0";
	public String status = "";
	public String sendB = "";
	public String msg = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String comp_sms_enable = "";
	public String[] exe_team_trans;
	public String sms_allsms = "1";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_sms_send", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				sms_allsms = PadQuotes(request.getParameter("chk_sms_allexe"));
				if (sms_allsms.equals("on")) {
					sms_allsms = "1";
				} else {
					sms_allsms = "0";
				}
				if (branch_id.equals("0")) {
					emp_branch_id = PadQuotes(request.getParameter("dr_branch"));
				} else {
					emp_branch_id = branch_id;
				}
				if (emp_branch_id.equals("")) {
					emp_branch_id = "-1";
				}
				emp_jobtitle_id = PadQuotes(request.getParameter("dr_desig"));
				if (emp_jobtitle_id.equals("")) {
					emp_jobtitle_id = "0";
				}
				emp_role_id = PadQuotes(request.getParameter("dr_role"));
				if (emp_role_id.equals("")) {
					emp_role_id = "0";
				}

				StrSql = "SELECT comp_sms_enable "
						+ " FROM " + compdb(comp_id) + "axela_comp "
						+ " WHERE comp_active = '1'";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					comp_sms_enable = crs.getString("comp_sms_enable");
				}
				crs.close();
				if (comp_sms_enable.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				sendB = PadQuotes(request.getParameter("send_button"));
				msg = PadQuotes(request.getParameter("msg"));
				sms_id = PadQuotes(request.getParameter("sms_id"));
				sms_msg = PadQuotes(request.getParameter("txt_sms_msg"));

				if (!"Send".equals(sendB)) {
					sms_mobileno = "";
					sms_date = "";
					sms_sent = "";
					sms_entry_id = "";
				} else {
					CheckPerm(comp_id, "emp_sms_send", request, response);
					GetValues(request, response);

					sms_entry_id = CNumeric(GetSession("emp_id", request));
					sms_date = ToLongDate(kknow());
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						CheckForm();
						if (msg.equals("")) {
							ListExe();
						}
						if (msg.equals("")) {
							if (sms_allsms.equals("1")) {
								sms_allsms = "on";
							} else {
								sms_allsms = "off";
							}
							response.sendRedirect(response.encodeRedirectURL("sms-exe-send.jsp?chk_sms_allexe=" + sms_allsms + "&msg=SMS sent successfully!"));
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

	protected void CheckForm() {
		msg = "";
		if (sms_allsms.equals("0")) {
			if (exe_team_trans == null) {
				msg = msg + "<br>Select Executives!";
			}
		}
		if (sms_msg.equals("")) {
			msg = msg + "<br>Enter Message!";
		}

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			sms_id = PadQuotes(request.getParameter("sms_id"));
			sms_mobileno = PadQuotes(request.getParameter("txt_sms_mobileno"));
			sms_msg = PadQuotes(request.getParameter("txt_sms_msg"));
			sms_allsms = PadQuotes(request.getParameter("chk_sms_allexe"));
			if (sms_allsms.equals("")) {
				String exe_team_trans1 = PadQuotes((RetrunSelectArrVal(request, "sms_allexe")));
				exe_team_trans = exe_team_trans1.split(",");
			}

			if (sms_allsms.equals("on")) {
				sms_allsms = "1";
			} else {
				sms_allsms = "0";
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ListExe() {
		String StrSearch = "";
		if (sms_msg.length() > 500) {
			sms_msg = sms_msg.substring(0, 499);
		}
		try {
			sms_msg = "REPLACE('" + sms_msg + "','[NAME]',emp_name)";
			sms_msg = "REPLACE(" + sms_msg + ",'[EXECUTIVEID]',emp_id)";
			sms_msg = "REPLACE(" + sms_msg + ",'[EXECUTIVEREFNO]',emp_ref_no)";

			StrSql = "SELECT "
					+ " emp_mobile1,"
					+ "IF(emp_branch_id=0,1,emp_branch_id), "
					+ " " + sms_msg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " 0, "
					+ " emp_id, "
					+ " " + emp_id + " "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id ";

			StrSql = StrSql + " WHERE  emp_active = '1'  " + BranchAccess;

			if (sms_allsms.equals("1")) {
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
			} else if (sms_allsms.equals("0")) {
				if (exe_team_trans != null) {
					String id = "";
					for (int i = 0; i < exe_team_trans.length; i++) {
						id = id + " " + exe_team_trans[i] + ", ";
					}
					StrSearch = StrSearch + " AND emp_id in (" + id.substring(0, id.lastIndexOf(",")) + ")";
				}
			}
			StrSearch = StrSearch + " AND emp_mobile1!='' ";
			if (!emp_role_id.equals("0")) {
				StrSearch = StrSearch + " AND emp_role_id =" + emp_role_id;
			}
			if (!emp_jobtitle_id.equals("0")) {
				StrSearch = StrSearch + " AND emp_jobtitle_id =" + emp_jobtitle_id;
			}
			StrSql = StrSql + StrSearch;
			StrSql = StrSql + " GROUP BY emp_id ";
			StrSql = StrSql + "ORDER BY emp_id DESC ";
			// SOP("StrSql==" + StrSql);
			AddFields(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields(String Sql) {
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
						+ "(sms_mobileno,"
						+ "sms_branch_id,"
						+ "sms_msg,"
						+ "sms_date ,"
						+ "sms_sent ,"
						+ "sms_emp_id ,"
						+ "sms_entry_id)"
						+ "" + Sql + "";
				// SOP("StrSql====" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateExecutives() {
		try {
			StrSql = "SELECT emp_id,emp_name,emp_ref_no "
					+ "FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id "
					+ " WHERE  emp_active='1' and emp_mobile1!='' " + BranchAccess;
			if (!emp_branch_id.equals("-1")) {
				StrSql = StrSql + " AND emp_branch_id = " + emp_branch_id;
			}
			if (!emp_role_id.equals("0")) {
				StrSql = StrSql + " AND emp_role_id = " + emp_role_id;
			}
			if (!emp_jobtitle_id.equals("0")) {
				StrSql = StrSql + " AND emp_jobtitle_id = " + emp_jobtitle_id;
			}
			StrSql = StrSql + " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String Exec = " ";
			while (crs.next()) {
				Exec = Exec + "<option value=" + crs.getString("emp_id") + ">" + crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")</option> \n";
			}
			crs.close();
			return Exec;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutivesTrans() {
		String stringval = "";
		try {
			if (exe_team_trans != null) {
				StrSql = " SELECT emp_id,emp_name,emp_ref_no "
						+ "FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_active=1 "
						+ " ORDER BY emp_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (exe_team_trans != null) {
					for (int i = 0; i < exe_team_trans.length; i++) {
						if (crs.getString("emp_id").equals(exe_team_trans[i])) {
							stringval = stringval + "<option value=" + crs.getString("emp_id") + " selected>" + crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")</option> \n";
						}
					}
				}
			}
			crs.close();
			return stringval;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = " SELECT branch_id,branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active='1' "
					+ BranchAccess
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =-1>All Branches</option>");
			stringval.append("<option value =0 " + StrSelectdrop("0", emp_branch_id) + ">Head Office</option>");
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

	public String PopulateDesignation() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = " SELECT jobtitle_id, jobtitle_desc "
					+ " FROM " + compdb(comp_id) + "axela_jobtitle "
					+ " WHERE jobtitle_id!=1"
					+ " ORDER BY jobtitle_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=" + crs.getString("jobtitle_id") + "");
				stringval.append(StrSelectdrop(crs.getString("jobtitle_id"), emp_jobtitle_id));
				stringval.append(">" + crs.getString("jobtitle_desc") + "</option> \n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateRole() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT role_id,role_name "
					+ "FROM " + compdb(comp_id) + "axela_emp_role "
					+ " ORDER BY role_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=" + crs.getString("role_id") + "");
				stringval.append(StrSelectdrop(crs.getString("role_id"), emp_role_id));
				stringval.append(">" + crs.getString("role_name") + "</option> \n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
