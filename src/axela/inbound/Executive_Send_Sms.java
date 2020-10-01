package axela.inbound;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executive_Send_Sms extends Connect {

	public String StrHTML = "", StrHTML1 = "", StrPostponed = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String branchtype = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "", exe_id = "", contact_id = "", customer_id = "", call_id = "";
	public String msg = "", contact_name = "", contact_mobile = "91-";
	public String email = "", sms = "";
	public String exe_names = "";
	public CRE_Check crecheck = new CRE_Check();
	CachedRowSet crs = null;
	public String emp_jobtitle, emp_name, emp_mobile1, emp_email1 = "", sms_branch_id = "0";
	public String smsmsg = "";
	public String config_exe_sms_enable, config_exe_sms_format, config_contact_sms_enable, config_contact_sms_format = "";
	public String comp_sms_enable = "";
	public String callback_callid = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				exe_id = CNumeric(PadQuotes(request.getParameter("exe_id")));
				call_id = CNumeric(PadQuotes(request.getParameter("call_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				contact_name = PadQuotes(request.getParameter("contact_name"));
				contact_mobile = PadQuotes(request.getParameter("contact_mobile"));
				if (contact_name.contains("*")) {
					contact_name = contact_name.replace("*", " ");
				}
				if (!contact_mobile.contains("91-")) {
					contact_mobile = "91-" + contact_mobile;
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateconFigDetails(HttpServletRequest request, String comp_id, String exe_id, String call_id, String customer_id, String contact_id, String contact_name, String contact_mobile) {
		emp_id = CNumeric(GetSession("emp_id", request));
		sms_branch_id = CNumeric(GetSession("emp_branch_id", request));
		if (exe_id.equals("") || exe_id.equals("null") || exe_id.equals("0")) {
			msg = "Select Executive!";
		}
		if (contact_name.equals("")) {
			msg += "</br> Select Contact Name!";
		}
		if (contact_mobile.equals("")) {
			msg += "</br> Enter Contact Mobile!";
		} else if (!IsValidMobileNo11(contact_mobile)) {
			msg += "</br> Enter Valid Contact Mobile!";
		}
		try {
			if (msg.equals("")) {
				StrSql = "SELECT jobtitle_desc, emp_name,"
						+ " emp_mobile1, emp_email1, emp_branch_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
						+ " WHERE 1=1"
						+ " AND emp_id = " + exe_id;
				// SOP("StrSql=emp=" + StrSql);
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					emp_jobtitle = crs.getString("jobtitle_desc");
					emp_name = crs.getString("emp_name");
					emp_mobile1 = crs.getString("emp_mobile1");
					emp_email1 = crs.getString("emp_email1");
				}
				if (emp_mobile1.equals("")) {
					msg = "Executive's Mobile not found!";
					return msg;
				} else if (!IsValidMobileNo11(emp_mobile1)) {
					msg = "Executive's Mobile is not valid!";
					return msg;
				}
				crs.close();

				if (msg.equals("")) {
					StrSql = "SELECT config_exe_sms_enable, config_exe_sms_format,"
							+ " config_contact_sms_enable, config_contact_sms_format,"
							+ " comp_sms_enable"
							+ " FROM " + compdb(comp_id) + "axela_config,"
							+ compdb(comp_id) + "axela_comp"
							+ " WHERE 1=1"
							+ "";
					// SOP("======" + StrSql);
					crs = processQuery(StrSql);
					while (crs.next()) {
						config_exe_sms_enable = crs.getString("config_exe_sms_enable");
						config_exe_sms_format = crs.getString("config_exe_sms_format");
						config_contact_sms_enable = crs.getString("config_contact_sms_enable");
						config_contact_sms_format = crs.getString("config_contact_sms_format");
						comp_sms_enable = crs.getString("comp_sms_enable");
					}

					if (comp_sms_enable.equals("1")) {
						if (config_exe_sms_enable.equals("1") && !config_exe_sms_format.equals("")) {
							SendSMS(comp_id, exe_id, emp_mobile1, contact_mobile, "0", contact_name, "executive");
							AddCallback(comp_id, exe_id, call_id, customer_id, contact_id, contact_name, contact_mobile);
						}
						if (config_contact_sms_enable.equals("1") && !config_contact_sms_format.equals("")) {
							SendSMS(comp_id, exe_id, emp_mobile1, contact_mobile, contact_id, contact_name, "contact");
						}
					}
					msg = "SMS Sent Succesfully!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return msg;
	}
	protected void SendSMS(String comp_id, String exe_id, String emp_mobile1, String contact_mobile, String contact_id, String contact_name, String type) throws SQLException {
		if (sms_branch_id.equals("0")) {
			sms_branch_id = "1";
		}
		if (type.equals("executive")) {
			smsmsg = config_exe_sms_format;

			smsmsg = "replace('" + smsmsg + "','[EXENAME]',emp_name)";
			smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
			smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
			smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
			smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',COALESCE(branch_name, 'Head Office'))";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHEMAIL1]',COALESCE(branch_email1,''))";
			smsmsg = smsmsg.replace("[CONTACTNAME]", contact_name);
			smsmsg = smsmsg.replace("[CONTACTMOBILE]", contact_mobile);

			StrSql = "SELECT"
					+ " COALESCE(branch_id,1),"
					+ "'" + emp_id + "',"
					+ " '0',"
					+ " '',"
					+ " '" + emp_mobile1 + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch On branch_id = emp_branch_id"
					+ " WHERE emp_id = " + exe_id;

			// SOP("StrSql====1234===" + StrSql);

		} else if (type.equals("contact")) {
			smsmsg = config_contact_sms_format;

			smsmsg = "replace('" + smsmsg + "','[EXENAME]',emp_name)";
			smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
			smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
			smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
			smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]',COALESCE(branch_name, 'Head Office'))";
			smsmsg = "replace(" + smsmsg + ",'[BRANCHEMAIL1]',COALESCE(branch_email1,''))";
			smsmsg = smsmsg.replace("[CONTACTNAME]", contact_name);
			smsmsg = smsmsg.replace("[CONTACTMOBILE]", contact_mobile);

			StrSql = "SELECT"
					+ " COALESCE(branch_id,1),"
					+ "'" + emp_id + "',"
					+ " '0',"
					+ " '',"
					+ " '" + contact_mobile + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch On branch_id = emp_branch_id"
					+ " WHERE emp_id = " + exe_id;

			// SOP("StrSql====1234===" + StrSql);

		}

		try {

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ "(sms_branch_id,"
					+ "sms_emp_id,"
					+ "sms_contact_id,"
					+ "sms_contact,"
					+ "sms_mobileno,"
					+ "sms_msg,"
					+ "sms_date ,"
					+ "sms_sent ,"
					+ "sms_entry_id)"
					+ " " + StrSql + "";
			updateQuery(StrSql);
			// SOP("StrSql----------insert sms-----------" + StrSql);

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void AddCallback(String comp_id, String exe_id, String call_id, String customer_id, String contact_id, String contact_name, String contact_mobile) {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_callback"
					+ "(callback_emp_id,"
					+ " callback_customer_id,"
					+ " callback_contact_id,"
					+ " callback_contact_name,"
					+ " callback_call_id,"
					+ " callback_phone_number,"
					+ " callback_entry_id,"
					+ " callback_entry_time)"
					+ " VALUES"
					+ " ("
					+ "" + exe_id + ","
					+ " " + customer_id + ","
					+ " " + contact_id + ","
					+ " '" + contact_name + "',"
					+ " " + call_id + ","
					+ " '" + contact_mobile + "',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ ")";
			SOP("StrSql------callback----------" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
