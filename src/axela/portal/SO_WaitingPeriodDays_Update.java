package axela.portal;
//divya 31st may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SO_WaitingPeriodDays_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String sowaitingperiod_brand_id = "0";
	public String dr_brand_id = "0";
	public String sowaitingperiod_id = "0";
	public String brandconfig_id = "0";
	public String sowaitingperiod_days = "0";
	public String sowaitingperiod_enable = "";
	public String QueryString = "";
	// crm

	public String sowaitingperiod_email_sub = "";
	public String sowaitingperiod_email_format = "";
	public String sowaitingperiod_sms_format = "";

	public String sowaitingperiod_entry_id = "0";
	public String sowaitingperiod_entry_date = "";
	public String sowaitingperiod_modified_id = "0";
	public String sowaitingperiod_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				sowaitingperiod_id = PadQuotes(request.getParameter("sowaitingperiod_id"));
				brandconfig_id = CNumeric(PadQuotes(request.getParameter("brandconfig_id")));
				QueryString = PadQuotes(request.getQueryString());
				sowaitingperiod_brand_id = CNumeric(PadQuotes(request.getParameter("dr_sowaitingperiod_brand_id")));
				dr_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				// SOP("dr_brand_id====" + dr_brand_id);
				if (!dr_brand_id.equals("0")) {
					sowaitingperiod_brand_id = dr_brand_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						sowaitingperiod_days = "0";
					} else {
						GetValues(request, response);
						sowaitingperiod_entry_id = emp_id;
						sowaitingperiod_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("so-waitingperioddays-list.jsp?dr_brand=" + dr_brand_id + "&msg=SO Waiting Period Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete SO Waiting Period".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete SO Waiting Period".equals(deleteB)) {
						GetValues(request, response);
						sowaitingperiod_modified_id = emp_id;
						sowaitingperiod_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("so-waitingperioddays-list.jsp?dr_brand=" + dr_brand_id + "&msg=SO Waiting Period Updated Successfully!"));
						}
					} else if ("Delete SO Waiting Period".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("so-waitingperioddays-list.jsp?dr_brand=" + dr_brand_id + "&msg=SO Waiting Period Deleted Successfully!"));
						}
					}
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
		sowaitingperiod_days = CNumeric(PadQuotes(request.getParameter("txt_sowaitingperiod_days")));
		sowaitingperiod_enable = PadQuotes(request.getParameter("chk_sowaitingperiod_enable"));
		if (sowaitingperiod_enable.equals("on")) {
			sowaitingperiod_enable = "1";
		} else {
			sowaitingperiod_enable = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (sowaitingperiod_brand_id.equals("0")) {
			msg = msg + "<br>Select Brand!";
		}

		if (sowaitingperiod_days.equals("0")) {
			msg = msg + "<br>Enter Days!";
		} else {
			try {
				if (!sowaitingperiod_days.equals("0")) {
					StrSql = "SELECT sowaitingperiod_days"
							+ " FROM " + compdb(comp_id) + "axela_brand_sowaitingperioddays"
							+ " WHERE sowaitingperiod_days = " + sowaitingperiod_days + ""
							+ " AND sowaitingperiod_brand_id = " + sowaitingperiod_brand_id + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND sowaitingperiod_id!= " + sowaitingperiod_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Days Found!";
					}
					crs.close();
				}

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	protected void AddFields() {
		CheckForm();

		if (msg.equals("")) {
			try {
				sowaitingperiod_id = ExecuteQuery("Select (coalesce(max(sowaitingperiod_id),0)+1)"
						+ " FROM " + compdb(comp_id) + "axela_brand_sowaitingperioddays");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_brand_sowaitingperioddays"
						+ " (sowaitingperiod_id,"
						+ " sowaitingperiod_brand_id,"
						+ " sowaitingperiod_days,"
						+ " sowaitingperiod_enable,"
						+ " sowaitingperiod_email_sub,"
						+ " sowaitingperiod_email_format,"
						+ " sowaitingperiod_sms_format,"
						+ " sowaitingperiod_entry_id,"
						+ " sowaitingperiod_entry_date)"
						+ " values"
						+ " (" + sowaitingperiod_id + ","
						+ " " + sowaitingperiod_brand_id + ","
						+ " " + sowaitingperiod_days + ","
						+ " '" + sowaitingperiod_enable + "',"
						+ " '" + sowaitingperiod_email_sub + "',"
						+ " '" + sowaitingperiod_email_format + "',"
						+ " '" + sowaitingperiod_sms_format + "',"
						+ " " + sowaitingperiod_entry_id + ","
						+ " '" + sowaitingperiod_entry_date + "')";
				// SOP("add---------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select sowaitingperiod_brand_id,"
					+ " sowaitingperiod_days,"
					+ " sowaitingperiod_email_sub,"
					+ " sowaitingperiod_email_format,"
					+ " sowaitingperiod_sms_format,"
					+ " sowaitingperiod_enable,"
					+ " sowaitingperiod_entry_id,"
					+ " sowaitingperiod_entry_date,"
					+ " sowaitingperiod_modified_id,"
					+ " sowaitingperiod_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_brand_sowaitingperioddays"
					+ " WHERE sowaitingperiod_id = " + sowaitingperiod_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					sowaitingperiod_brand_id = crs.getString("sowaitingperiod_brand_id");
					sowaitingperiod_days = crs.getString("sowaitingperiod_days");
					sowaitingperiod_email_sub = crs.getString("sowaitingperiod_email_sub");
					sowaitingperiod_email_format = crs.getString("sowaitingperiod_email_format");
					sowaitingperiod_sms_format = crs.getString("sowaitingperiod_sms_format");
					sowaitingperiod_enable = crs.getString("sowaitingperiod_enable");
					sowaitingperiod_entry_id = crs.getString("sowaitingperiod_entry_id");
					if (!sowaitingperiod_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(sowaitingperiod_entry_id));
						entry_date = strToLongDate(crs.getString("sowaitingperiod_entry_date"));
					}
					sowaitingperiod_modified_id = crs.getString("sowaitingperiod_modified_id");
					if (!sowaitingperiod_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(sowaitingperiod_modified_id));
						modified_date = strToLongDate(crs.getString("sowaitingperiod_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid SO Waiting Period!"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_brand_sowaitingperioddays"
						+ " SET"
						+ " sowaitingperiod_brand_id = '" + sowaitingperiod_brand_id + "',"
						+ " sowaitingperiod_days = " + sowaitingperiod_days + ","
						+ " sowaitingperiod_enable = '" + sowaitingperiod_enable + "',"
						+ " sowaitingperiod_modified_id = " + sowaitingperiod_modified_id + ","
						+ " sowaitingperiod_modified_date = '" + sowaitingperiod_modified_date + "'"
						+ " WHERE sowaitingperiod_id = " + sowaitingperiod_id + "";
				// SOP("StrSql-------update-------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {

				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		// StrSql = "SELECT crm_sowaitingperiod_id"
		// + " FROM " + compdb(comp_id) +
		// "axela_brand_sowaitingperiod WHERE crm_sowaitingperiod_id = " +
		// sowaitingperiod_id;
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>CRM days is associated with CRM Follow-up!";
		// }
		if (msg.equals("")) {
			try {
				StrSql = "Delete"
						+ " FROM " + compdb(comp_id) + "axela_brand_sowaitingperioddays"
						+ " WHERE sowaitingperiod_id =" + sowaitingperiod_id;
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), dr_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
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
