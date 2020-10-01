package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_Configure extends Connect {

	public String updateB = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String msg = "";
	public String config_admin_email = "";
	public String config_app_ver = "";
	public String config_doc_size = "";
	public String config_doc_formats = "";
	public String config_contact_sms_enable = "";
	public String config_contact_sms_format = "";
	public String config_exe_sms_enable = "";
	public String config_exe_sms_format = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";

	// public String config_sms_url = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_role_id", request, response);
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				PopulateFields();
				if ("Update".equals(updateB)) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						UpdateFields();
						response.sendRedirect(response.encodeRedirectURL("manage-configure.jsp?&msg=Configuration details updated Successfully!"));
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		config_admin_email = PadQuotes(request.getParameter("txt_admin_email"));
		config_app_ver = PadQuotes(request.getParameter("txt_config_app_ver"));
		config_doc_size = PadQuotes(request.getParameter("txt_doc_size"));
		config_doc_formats = PadQuotes(request.getParameter("txt_doc_formats"));
		config_contact_sms_enable = PadQuotes(request.getParameter("chk_config_contact_sms_enable"));
		config_contact_sms_format = PadQuotes(request.getParameter("txt_config_contact_sms_format"));
		config_exe_sms_enable = PadQuotes(request.getParameter("chk_config_exe_sms_enable"));
		config_exe_sms_format = PadQuotes(request.getParameter("txt_config_exe_sms_format"));
		config_email_enable = PadQuotes(request.getParameter("chk_config_email_enable"));
		config_sms_enable = PadQuotes(request.getParameter("chk_config_sms_enable"));
		if (config_contact_sms_enable.equals("on")) {
			config_contact_sms_enable = "1";
		} else {
			config_contact_sms_enable = "0";
		}
		if (config_exe_sms_enable.equals("on")) {
			config_exe_sms_enable = "1";
		} else {
			config_exe_sms_enable = "0";
		}
		if (config_email_enable.equals("on")) {
			config_email_enable = "1";
		} else {
			config_email_enable = "0";
		}
		if (config_sms_enable.equals("on")) {
			config_sms_enable = "1";
		} else {
			config_sms_enable = "0";
		}
		// if (config_sms_enable.equals("1")) {
		// config_sms_url = PadQuotes(request.getParameter("txt_config_sms_url"));
		// }

	}

	protected void CheckForm() {
		msg = "";
		if (config_admin_email.equals("")) {
			msg = msg + "<br>Enter Admin Email-Id!";
		}
		if (!config_admin_email.equals("")) {
			config_admin_email = config_admin_email.toLowerCase();
		}
		if (!config_admin_email.equals("") && IsValidEmail(config_admin_email) == false) {
			msg = msg + "<br>Invalid Admin email-Id!";
		}
		if (config_app_ver.equals("")) {
			msg = msg + "<br>Enter App Version!";
		}
		if (config_doc_size.equals("")) {
			config_doc_size = "0";
		}
		// if (config_sms_enable.equals("1")) {
		// if (config_sms_url.equals("")) {
		// msg += "<br>Enter SMS URL!";
		// }
		// }
		String[] str = new String[10];
		try {
			str = config_doc_formats.split(" ");
			for (int i = 0; i < str.length - 1; i++) {
				if (!str[i].startsWith(".")) {
					msg = msg + "<br>Invalid document formats!";
				}
				if (!str[i].endsWith(",")) {
					msg = msg + "<br>Invalid document formats!";
				}
			}
			if (str[str.length - 1].contains(",")) {
				msg = msg + "<br>Invalid document formats!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	protected void PopulateFields() {
		try {
			StrSql = "Select config_app_ver, config_admin_email, config_doc_size, config_doc_formats,"
					+ " config_contact_sms_enable, config_contact_sms_format, "
					+ " config_exe_sms_enable, config_exe_sms_format, "
					+ " config_email_enable, config_sms_enable "
					+ " from " + compdb(comp_id) + "axela_config "
					+ " where 1=1 ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_app_ver = crs.getString("config_app_ver");
				config_admin_email = crs.getString("config_admin_email");
				config_doc_size = crs.getString("config_doc_size");
				config_doc_formats = crs.getString("config_doc_formats");
				config_contact_sms_enable = crs.getString("config_contact_sms_enable");
				config_contact_sms_format = crs.getString("config_contact_sms_format");;
				config_exe_sms_enable = crs.getString("config_exe_sms_enable");
				config_exe_sms_format = crs.getString("config_exe_sms_format");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				// config_sms_url = crs.getString("config_sms_url");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		try {
			StrSql = "UPDATE  " + compdb(comp_id) + "axela_config Set "
					+ " config_app_ver = '" + config_app_ver + "',"
					+ " config_admin_email = '" + config_admin_email + "',"
					+ " config_doc_size = '" + config_doc_size + "',"
					+ " config_doc_formats = '" + config_doc_formats.toLowerCase() + "',"
					+ " config_contact_sms_enable = '" + config_contact_sms_enable + "',"
					+ " config_contact_sms_format = '" + config_contact_sms_format + "',"
					+ " config_exe_sms_enable = '" + config_exe_sms_enable + "',"
					+ " config_exe_sms_format = '" + config_exe_sms_format + "',"
					+ " config_email_enable = '" + config_email_enable + "',"
					+ " config_sms_enable = '" + config_sms_enable + "'";
			// + " config_sms_url = '" + config_sms_url + "'";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
}
