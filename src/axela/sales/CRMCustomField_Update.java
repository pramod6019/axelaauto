package axela.sales;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CRMCustomField_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String LinkHeader = "";
	public String crmcf_id = "0";
	public String crmcf_crmdays_id = "0";
	public String crmcf_title = "", daydesc = "";
	public String crmcf_cftype_id = "0";
	public String crmcf_numeric = "0";
	public String crmcf_length_min = "";
	public String crmcf_length_max = "";
	public String crmcf_option = "";
	public String crmcf_unique = "0";
	public String crmcf_mandatory = "0";
	public String crmcf_voc = "0";
	public String crmcf_instruction = "";
	public String crmcf_print = "0";
	public String crmcf_fieldref = "";
	public String crmcf_active = "0";
	public String crmcf_rank = "0";
	public String brand_id = "0";
	public String crmdays_id = "0";
	public String crmcf_entry_id = "0";
	public String crmcf_entry_date = "";
	public String crmcf_modified_id = "0";
	public String crmcf_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	// public String module_id = "0";
	// public String module_name = "";
	// public String submodule_id = "0";
	// public String submodule_name = "";
	// public String emp_formatdate = "";
	// public String emp_formattime = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// emp_formatdate = GetSession("formatdate_name", request);
				// emp_formattime = GetSession("formattime_name", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				crmcf_id = CNumeric(PadQuotes(request.getParameter("crmcf_id")));
				crmcf_crmdays_id = CNumeric(PadQuotes(request.getParameter("crmdays_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				crmdays_id = CNumeric(PadQuotes(request.getParameter("crmdays_id")));
				if ((crmcf_id.equals("0")))
				{
					crmcf_id = ExecuteQuery("SELECT crmdays_brand_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
							// + " WHERE crmcf_id = "+crmcf_id+"");
							+ " WHERE 1 = 1");
				}
				// SOP("crmcf_id-----in cf update---"+crmcf_id);
				if (crmdays_id.equals("0") && (!crmcf_id.equals("0"))) {
					crmdays_id = ExecuteQuery("SELECT crmcf_crmdays_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
							+ " WHERE crmcf_id = " + crmcf_id + "");

				}
				daydesc = ExecuteQuery("SELECT CONCAT(crmdays_daycount,crmdays_desc)"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " WHERE crmdays_id = " + crmdays_id + "");

				if (brand_id.equals("0") && (!crmcf_id.equals("0"))) {
					brand_id = ExecuteQuery("SELECT crmdays_brand_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
							// + " WHERE crmcf_id = "+crmcf_id+"");
							+ " WHERE 1 = 1");
				}
				// if((crmcf_id.equals("0")))
				// {
				// crmcf_id = ExecuteQuery("SELECT crmdays_brand_id FROM " + compdb(comp_id) + "axela_sales_crmdays"
				// ///+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
				// //+ " WHERE crmcf_id = "+crmcf_id+"");
				// + " WHERE 1 = 1");
				// }

				LinkHeader += " &gt; <a href=crmdays.jsp?brand_id=" + brand_id + ">" + daydesc + "</a> &gt;";
				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						crmcf_cftype_id = "1";
						crmcf_active = "1";
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("crmcustomfield-list.jsp?brand_id= " + brand_id + "&crmdays_id=" + crmdays_id + "&msg=Custom Field added successfully!"
									+ msg + ""));
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !deleteB.equals("Delete Custom Field")) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !deleteB.equals("Delete Custom Field")) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("crmcustomfield-list.jsp?brand_id= " + brand_id + "&crmdays_id=" + crmdays_id
									+ "&msg=Custom Field updated successfully!" + msg + ""));
						}
					} else if (deleteB.equals("Delete Custom Field")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("crmcustomfield-list.jsp?brand_id= " + brand_id + "&crmdays_id=" + crmdays_id
									+ "&msg=Custom Field deleted successfully!"));
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		crmcf_title = unescapehtml(PadQuotes(request.getParameter("txt_crmcf_title")));

		crmcf_cftype_id = CNumeric(PadQuotes(request.getParameter("dr_crmcf_cftype_id")));
		crmcf_numeric = CheckBoxValue(PadQuotes(request.getParameter("chk_crmcf_numeric")));
		crmcf_length_min = PadQuotes(request.getParameter("txt_crmcf_length_min"));
		crmcf_length_max = PadQuotes(request.getParameter("txt_crmcf_length_max"));
		crmcf_option = PadQuotes(request.getParameter("txt_crmcf_option"));
		crmcf_unique = CheckBoxValue(PadQuotes(request.getParameter("chk_crmcf_unique")));
		crmcf_mandatory = CheckBoxValue(PadQuotes(request.getParameter("chk_crmcf_mandatory")));
		crmcf_voc = CheckBoxValue(PadQuotes(request.getParameter("chk_crmcf_voc")));
		crmcf_active = CheckBoxValue(PadQuotes(request.getParameter("chk_crmcf_active")));
		crmcf_print = CheckBoxValue(PadQuotes(request.getParameter("chk_crmcf_print")));
		crmcf_fieldref = PadQuotes(request.getParameter("txt_crmcf_fieldref"));
		crmcf_instruction = PadQuotes(request.getParameter("txt_crmcf_instruction"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));

		// if(customfield_option.length() > 5000){
		// customfield_option = customfield_option.substring(0, 4999);
		// }
		// if(customfield_instruction.length() > 1000){
		// customfield_option = customfield_option.substring(0, 999);
		// }
	}

	protected void CheckForm() {
		msg = "";
		if (crmcf_title.equals("")) {
			msg += "<br>Enter Title!";
		} else {
			StrSql = "SELECT crmcf_title"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " WHERE crmcf_title = '" + crmcf_title + "'"
					+ " AND crmcf_crmdays_id = " + crmcf_crmdays_id + "";
			if (update.equals("yes")) {
				StrSql += " AND crmcf_id != " + crmcf_id + "";
			}
			if (ExecuteQuery(StrSql).equals(crmcf_title)) {
				msg += "<br>Similar Title Found!";
			}
		}
		if (crmcf_cftype_id.equals("0")) {
			msg += "<br>Select Type!";
		}

		crmcf_length_min = CNumeric(crmcf_length_min);
		crmcf_length_max = CNumeric(crmcf_length_max);
		if (crmcf_cftype_id.equals("3")) {
			crmcf_length_max = "1";
		} else if (crmcf_cftype_id.equals("4")) {
			crmcf_length_max = "10";
		} else if (crmcf_cftype_id.equals("5")) {
			crmcf_length_max = "10";
		} else if (crmcf_cftype_id.equals("6")) {
			crmcf_length_max = "16";
		} else if (crmcf_cftype_id.equals("7")) {
			crmcf_length_max = "5";
		}

		if (crmcf_length_max.equals("0")) {
			msg += "<br>Maximum Length should be greater than 0!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " (crmcf_title,"
					// + " crmcf_ref_title,"
					+ " crmcf_crmdays_id,"
					+ " crmcf_cftype_id,"
					+ " crmcf_numeric,"
					+ " crmcf_length_min,"
					+ " crmcf_length_max,"
					+ " crmcf_option,"
					+ " crmcf_unique,"
					+ " crmcf_mandatory,"
					+ " crmcf_active,"
					+ " crmcf_rank,"
					+ " crmcf_print,"
					+ " crmcf_fieldref,"
					+ " crmcf_instruction,"
					+ " crmcf_voc,"
					+ " crmcf_entry_id,"
					+ " crmcf_entry_date)"
					+ " VALUES"
					+ " ('" + crmcf_title + "',"
					// + " " + crmcf_ref_title + ","
					+ " " + crmcf_crmdays_id + ","
					+ " " + crmcf_cftype_id + ","
					+ " '" + crmcf_numeric + "',"
					+ " " + crmcf_length_min + ","
					+ " " + crmcf_length_max + ","
					+ " '" + crmcf_option + "',"
					+ " '" + crmcf_unique + "',"
					+ " '" + crmcf_mandatory + "',"
					+ " '" + crmcf_active + "',"
					+ " (SELECT(COALESCE(MAX(cf.crmcf_rank), 0) + 1)"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf cf"
					+ "  WHERE cf.crmcf_crmdays_id = " + crmcf_crmdays_id + ""
					+ "),"
					+ " '" + crmcf_print + "',"
					+ " '" + crmcf_fieldref + "',"
					+ " '" + crmcf_instruction + "',"
					+ " " + crmcf_voc + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ")";
			crmcf_id = UpdateQueryReturnID(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT crmcf_id, crmcf_title, crmcf_cftype_id,"
					+ " crmcf_numeric, crmcf_length_min, crmcf_length_max,"
					+ " crmcf_option, crmcf_unique, crmcf_mandatory, crmcf_voc, crmcf_active,"
					+ " crmcf_print, crmcf_fieldref, crmcf_instruction, crmcf_entry_id,"
					+ " crmcf_entry_date, crmcf_modified_id, crmcf_modified_date,"
					+ " crmcf_refcrmcf_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crmcf_crmdays_id" // to delete later
					+ " WHERE crmcf_id = " + crmcf_id + "";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					crmcf_title = crs.getString("crmcf_title");
					// crmcf_ref_title = crs.getString("crmcf_ref_title");
					crmcf_cftype_id = crs.getString("crmcf_cftype_id");
					crmcf_length_min = crs.getString("crmcf_length_min");
					crmcf_length_max = crs.getString("crmcf_length_max");
					crmcf_option = crs.getString("crmcf_option");
					crmcf_numeric = crs.getString("crmcf_numeric");
					crmcf_unique = crs.getString("crmcf_unique");
					crmcf_mandatory = crs.getString("crmcf_mandatory");
					crmcf_voc = crs.getString("crmcf_voc");
					crmcf_active = crs.getString("crmcf_active");
					crmcf_print = crs.getString("crmcf_print");
					crmcf_fieldref = crs.getString("crmcf_fieldref");
					crmcf_instruction = crs.getString("crmcf_instruction");
					crmcf_entry_id = crs.getString("crmcf_entry_id");
					if (!crs.getString("crmcf_entry_id").equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("crmcf_entry_id"));
						entry_date = strToLongDate(crs.getString("crmcf_entry_date"));
					}
					crmcf_modified_date = crs.getString("crmcf_modified_date");
					if (!crs.getString("crmcf_modified_id").equals("0")) {
						modified_by = Exename(comp_id, crs.getInt("crmcf_modified_id"));
						modified_date = strToLongDate(crs.getString("crmcf_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Custom Field!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm_cf"
					+ " SET"
					+ " crmcf_title = '" + crmcf_title + "',"
					+ " crmcf_numeric = '" + crmcf_numeric + "',"
					+ " crmcf_cftype_id = " + crmcf_cftype_id + ","
					+ " crmcf_length_min = " + crmcf_length_min + ","
					+ " crmcf_length_max = " + crmcf_length_max + ","
					+ " crmcf_option = '" + crmcf_option + "',"
					+ " crmcf_unique = '" + crmcf_unique + "',"
					+ " crmcf_mandatory = '" + crmcf_mandatory + "',"
					+ " crmcf_active = '" + crmcf_active + "',"
					+ " crmcf_print = '" + crmcf_print + "',"
					+ " crmcf_fieldref = '" + crmcf_fieldref + "',"
					+ " crmcf_instruction = '" + crmcf_instruction + "',"
					+ " crmcf_voc = " + crmcf_voc + ","
					+ " crmcf_modified_id = " + emp_id + ","
					+ " crmcf_modified_date = " + ToLongDate(kknow()) + ""
					+ " WHERE  crmcf_id = " + crmcf_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateCRMCfDays() throws SQLException {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT crmdays_id, concat(crmdays_daycount, crmdays_desc) AS crmdays_desc"
				+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
				+ " GROUP BY crmdays_id"
				+ " ORDER BY crmdays_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		while (crs.next()) {
			Str.append("<option value=").append(crs.getString("crmdays_id"));
			Str.append(StrSelectdrop(crs.getString("crmdays_id"), crmcf_crmdays_id));
			Str.append(">").append(crs.getString("crmdays_desc")).append("</option>\n");
		}
		crs.close();
		return Str.toString();
	}

	public String PopulateCfType() throws SQLException {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT cftype_id, cftype_name"
				+ " FROM axela_cf_type"
				+ " GROUP BY cftype_id"
				+ " ORDER BY cftype_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		while (crs.next()) {
			Str.append("<option value=").append(crs.getString("cftype_id"));
			Str.append(StrSelectdrop(crs.getString("cftype_id"), crmcf_cftype_id));
			Str.append(">").append(crs.getString("cftype_name")).append("</option>\n");
		}
		crs.close();
		return Str.toString();
	}

	protected void DeleteFields() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm_trans"
				// ////+ " WHERE cftrans_submodule_id = " + submodule_id + ""
				+ " WHERE crmcftrans_crmcf_id = " + crmcf_id + "";
		updateQuery(StrSql);

		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm_cf"
				+ " WHERE crmcf_id = " + crmcf_id + "";
		updateQuery(StrSql);
	}
}
