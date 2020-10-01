package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Configure_Service extends Connect {

	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String name = "";
	public String comp_id = "0";
	public String config_service_ticket = "";
	public String config_service_contract = "";
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public String config_service_contract_refno = "";
	public String config_ticket_new_email_enable = "";
	public String config_ticket_new_email_sub = "";
	public String config_ticket_new_email_format = "";
	public String config_ticket_new_sms_enable = "";
	public String config_ticket_new_sms_format = "";
	public String config_ticket_followup_email_enable = "";
	public String config_ticket_followup_email_sub = "";
	public String config_ticket_followup_email_format = "";
	public String config_ticket_followup_sms_enable = "";
	public String config_ticket_followup_sms_format = "";
	public String config_ticket_closed_email_enable = "";
	public String config_ticket_closed_email_sub = "";
	public String config_ticket_closed_email_format = "";
	public String config_ticket_closed_sms_enable = "";
	public String config_ticket_closed_sms_format = "";

	public String config_ticket_crm_email_sub = "";
	public String config_ticket_crm_email_format = "";
	public String config_ticket_pbf_email_sub = "";
	public String config_ticket_pbf_email_format = "";
	public String config_ticket_psf_email_sub = "";
	public String config_ticket_psf_email_format = "";
	public String config_ticket_jcpsf_email_sub = "";
	public String config_ticket_jcpsf_email_format = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_role_id", request, response);
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				PopulateFields();
				if ("Update".equals(updateB)) {
					GetValues(request, response);
					// if (msg.equals("")) {
					UpdateFields(request);
					response.sendRedirect(response.encodeRedirectURL("configure-service.jsp?msg=Configuration details updated successfully for Service!"));
					// }
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		config_service_ticket = PadQuotes(request.getParameter("chk_config_service_ticket"));
		config_service_contract = PadQuotes(request.getParameter("chk_config_service_contract"));
		config_service_ticket_cat = PadQuotes(request.getParameter("chk_config_service_ticket_cat"));
		config_service_ticket_type = PadQuotes(request.getParameter("chk_config_service_ticket_type"));
		config_service_contract_refno = PadQuotes(request.getParameter("chk_config_service_contract_refno"));
		config_ticket_new_email_enable = PadQuotes(request.getParameter("chk_config_ticket_new_email_enable"));
		config_ticket_new_sms_enable = PadQuotes(request.getParameter("chk_config_ticket_new_sms_enable"));
		config_ticket_followup_email_enable = PadQuotes(request.getParameter("chk_config_ticket_followup_email_enable"));
		config_ticket_followup_sms_enable = PadQuotes(request.getParameter("chk_config_ticket_followup_sms_enable"));
		config_ticket_closed_email_enable = PadQuotes(request.getParameter("chk_config_ticket_closed_email_enable"));
		config_ticket_closed_sms_enable = PadQuotes(request.getParameter("chk_config_ticket_closed_sms_enable"));

		if (config_service_ticket.equals("on")) {
			config_service_ticket = "1";
		} else {
			config_service_ticket = "0";
		}

		if (config_service_contract.equals("on")) {
			config_service_contract = "1";
		} else {
			config_service_contract = "0";
		}

		if (config_service_ticket_cat.equals("on")) {
			config_service_ticket_cat = "1";
		} else {
			config_service_ticket_cat = "0";
		}

		if (config_service_ticket_type.equals("on")) {
			config_service_ticket_type = "1";
		} else {
			config_service_ticket_type = "0";
		}

		if (config_service_contract_refno.equals("on")) {
			config_service_contract_refno = "1";
		} else {
			config_service_contract_refno = "0";
		}

		if (config_ticket_new_email_enable.equals("on")) {
			config_ticket_new_email_enable = "1";
		} else {
			config_ticket_new_email_enable = "0";
		}

		if (config_ticket_new_sms_enable.equals("on")) {
			config_ticket_new_sms_enable = "1";
		} else {
			config_ticket_new_sms_enable = "0";
		}

		if (config_ticket_followup_email_enable.equals("on")) {
			config_ticket_followup_email_enable = "1";
		} else {
			config_ticket_followup_email_enable = "0";
		}

		if (config_ticket_followup_sms_enable.equals("on")) {
			config_ticket_followup_sms_enable = "1";
		} else {
			config_ticket_followup_sms_enable = "0";
		}

		if (config_ticket_closed_email_enable.equals("on")) {
			config_ticket_closed_email_enable = "1";
		} else {
			config_ticket_closed_email_enable = "0";
		}

		if (config_ticket_closed_sms_enable.equals("on")) {
			config_ticket_closed_sms_enable = "1";
		} else {
			config_ticket_closed_sms_enable = "0";
		}
	}

	protected void PopulateFields() {
		try {
			StrSql = "Select * "
					+ "from " + compdb(comp_id) + "axela_config ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_service_ticket = crs.getString("config_service_ticket");
				config_service_contract = crs.getString("config_service_contract");
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
				config_service_contract_refno = crs.getString("config_service_contract_refno");
				config_ticket_new_email_enable = crs.getString("config_ticket_new_email_enable");
				config_ticket_new_sms_enable = crs.getString("config_ticket_new_sms_enable");
				config_ticket_followup_email_enable = crs.getString("config_ticket_followup_email_enable");
				config_ticket_followup_sms_enable = crs.getString("config_ticket_followup_sms_enable");
				config_ticket_closed_email_enable = crs.getString("config_ticket_closed_email_enable");
				config_ticket_closed_sms_enable = crs.getString("config_ticket_closed_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_config Set "
					+ "config_service_ticket = '" + config_service_ticket + "',"
					+ "config_service_contract = '" + config_service_contract + "',"
					+ "config_service_ticket_cat = '" + config_service_ticket_cat + "',"
					+ "config_service_ticket_type = '" + config_service_ticket_type + "',"
					+ "config_service_contract_refno = '" + config_service_contract_refno + "',"
					+ "config_ticket_new_email_enable = '" + config_ticket_new_email_enable + "',"
					+ "config_ticket_new_sms_enable = '" + config_ticket_new_sms_enable + "',"
					+ "config_ticket_followup_email_enable = '" + config_ticket_followup_email_enable + "',"
					+ "config_ticket_followup_sms_enable = '" + config_ticket_followup_sms_enable + "',"
					+ "config_ticket_closed_email_enable = '" + config_ticket_closed_email_enable + "',"
					+ "config_ticket_closed_sms_enable = '" + config_ticket_closed_sms_enable + "'";
			// SOP("StrSql==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
