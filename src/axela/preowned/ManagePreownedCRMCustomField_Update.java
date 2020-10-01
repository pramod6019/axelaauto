package axela.preowned;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedCRMCustomField_Update extends Connect {

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
	public String precrmcf_id = "0";
	public String precrmcf_precrmfollowupdays_id = "0";
	public String precrmcf_title = "", daydesc = "";
	public String precrmcf_cftype_id = "0";
	public String precrmcf_numeric = "0";
	public String precrmcf_length_min = "";
	public String precrmcf_length_max = "";
	public String precrmcf_option = "";
	public String precrmcf_unique = "0";
	public String precrmcf_mandatory = "0";
	public String precrmcf_voc = "0";
	public String precrmcf_instruction = "";
	public String precrmcf_print = "0";
	public String precrmcf_fieldref = "";
	public String precrmcf_active = "0";
	public String precrmcf_rank = "0";
	public String brand_id = "0";
	public String precrmfollowupdays_id = "0";
	public String precrmcf_entry_id = "0";
	public String precrmcf_entry_date = "";
	public String precrmcf_modified_id = "0";
	public String precrmcf_modified_date = "";
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
				precrmcf_id = CNumeric(PadQuotes(request.getParameter("precrmcf_id")));
				precrmcf_precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("precrmfollowupdays_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				precrmfollowupdays_id = CNumeric(PadQuotes(request.getParameter("precrmfollowupdays_id")));
				if ((precrmcf_id.equals("0")))
				{
					precrmcf_id = ExecuteQuery("SELECT precrmfollowupdays_brand_id FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
							// + " WHERE precrmcf_id = "+precrmcf_id+"");
							+ " WHERE 1 = 1");
				}
				// SOP("precrmcf_id-----in cf update---"+precrmcf_id);
				if (precrmfollowupdays_id.equals("0") && (!precrmcf_id.equals("0"))) {
					precrmfollowupdays_id = ExecuteQuery("SELECT precrmcf_precrmfollowupdays_id FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
							+ " WHERE precrmcf_id = " + precrmcf_id + "");

				}
				daydesc = ExecuteQuery("SELECT CONCAT(precrmfollowupdays_daycount,precrmfollowupdays_desc) FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
						+ " WHERE precrmfollowupdays_id = " + precrmfollowupdays_id + "");

				if (brand_id.equals("0") && (!precrmcf_id.equals("0"))) {
					brand_id = ExecuteQuery("SELECT precrmfollowupdays_brand_id FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
							// + " WHERE precrmcf_id = "+precrmcf_id+"");
							+ " WHERE 1 = 1");
				}
				// if((precrmcf_id.equals("0")))
				// {
				// precrmcf_id = ExecuteQuery("SELECT precrmfollowupdays_brand_id FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
				// ///+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crm_cf ON precrmcf_precrmfollowupdays_id = precrmfollowupdays_id"
				// //+ " WHERE precrmcf_id = "+precrmcf_id+"");
				// + " WHERE 1 = 1");
				// }

				LinkHeader += " &gt; <a href=managepreownedcrmfollowupdays.jsp?brand_id=" + brand_id + ">" + daydesc + "</a> &gt;";
				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						precrmcf_cftype_id = "1";
						precrmcf_active = "1";
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedcrmcustomfield-list.jsp?brand_id= " + brand_id + "&precrmfollowupdays_id=" + precrmfollowupdays_id
									+ "&msg=Custom Field Added Successfully!"
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
							response.sendRedirect(response.encodeRedirectURL("managepreownedcrmcustomfield-list.jsp?brand_id= " + brand_id + "&precrmfollowupdays_id=" + precrmfollowupdays_id
									+ "&msg=Custom Field Updated Successfully!" + msg + ""));
						}
					} else if (deleteB.equals("Delete Custom Field")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedcrmcustomfield-list.jsp?brand_id= " + brand_id + "&precrmfollowupdays_id=" + precrmfollowupdays_id
									+ "&msg=Custom Field Deleted Successfully!"));
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
		precrmcf_title = unescapehtml(PadQuotes(request.getParameter("txt_precrmcf_title")));

		precrmcf_cftype_id = CNumeric(PadQuotes(request.getParameter("dr_precrmcf_cftype_id")));
		precrmcf_numeric = CheckBoxValue(PadQuotes(request.getParameter("chk_precrmcf_numeric")));
		precrmcf_length_min = PadQuotes(request.getParameter("txt_precrmcf_length_min"));
		precrmcf_length_max = PadQuotes(request.getParameter("txt_precrmcf_length_max"));
		precrmcf_option = PadQuotes(request.getParameter("txt_precrmcf_option"));
		precrmcf_unique = CheckBoxValue(PadQuotes(request.getParameter("chk_precrmcf_unique")));
		precrmcf_mandatory = CheckBoxValue(PadQuotes(request.getParameter("chk_precrmcf_mandatory")));
		precrmcf_voc = CheckBoxValue(PadQuotes(request.getParameter("chk_precrmcf_voc")));
		precrmcf_active = CheckBoxValue(PadQuotes(request.getParameter("chk_precrmcf_active")));
		precrmcf_print = CheckBoxValue(PadQuotes(request.getParameter("chk_precrmcf_print")));
		precrmcf_fieldref = PadQuotes(request.getParameter("txt_precrmcf_fieldref"));
		precrmcf_instruction = PadQuotes(request.getParameter("txt_precrmcf_instruction"));
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
		if (precrmcf_title.equals("")) {
			msg += "<br>Enter Title!";
		} else {
			StrSql = "SELECT precrmcf_title"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " WHERE precrmcf_title = '" + precrmcf_title + "'"
					+ " AND precrmcf_precrmfollowupdays_id = " + precrmcf_precrmfollowupdays_id + "";
			if (update.equals("yes")) {
				StrSql += " AND precrmcf_id != " + precrmcf_id + "";
			}
			if (ExecuteQuery(StrSql).equals(precrmcf_title)) {
				msg += "<br>Similar Title Found!";
			}
		}
		if (precrmcf_cftype_id.equals("0")) {
			msg += "<br>Select Type!";
		}

		precrmcf_length_min = CNumeric(precrmcf_length_min);
		precrmcf_length_max = CNumeric(precrmcf_length_max);
		if (precrmcf_cftype_id.equals("3")) {
			precrmcf_length_max = "1";
		} else if (precrmcf_cftype_id.equals("4")) {
			precrmcf_length_max = "10";
		} else if (precrmcf_cftype_id.equals("5")) {
			precrmcf_length_max = "10";
		} else if (precrmcf_cftype_id.equals("6")) {
			precrmcf_length_max = "16";
		} else if (precrmcf_cftype_id.equals("7")) {
			precrmcf_length_max = "5";
		}

		if (precrmcf_length_max.equals("0")) {
			msg += "<br>Maximum Length should be greater than 0!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " (precrmcf_title,"
					// + " crmcf_ref_title,"
					+ " precrmcf_precrmfollowupdays_id,"
					+ " precrmcf_cftype_id,"
					+ " precrmcf_numeric,"
					+ " precrmcf_length_min,"
					+ " precrmcf_length_max,"
					+ " precrmcf_option,"
					+ " precrmcf_unique,"
					+ " precrmcf_mandatory,"
					+ " precrmcf_active,"
					+ " precrmcf_rank,"
					+ " precrmcf_print,"
					+ " precrmcf_fieldref,"
					+ " precrmcf_instruction,"
					+ " precrmcf_voc,"
					+ " precrmcf_entry_id,"
					+ " precrmcf_entry_date)"
					+ " VALUES"
					+ " ('" + precrmcf_title + "',"
					// + " " + crmcf_ref_title + ","
					+ " " + precrmcf_precrmfollowupdays_id + ","
					+ " " + precrmcf_cftype_id + ","
					+ " '" + precrmcf_numeric + "',"
					+ " " + precrmcf_length_min + ","
					+ " " + precrmcf_length_max + ","
					+ " '" + precrmcf_option + "',"
					+ " '" + precrmcf_unique + "',"
					+ " '" + precrmcf_mandatory + "',"
					+ " '" + precrmcf_active + "',"
					+ " (SELECT(COALESCE(MAX(cf.precrmcf_rank), 0) + 1)"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_cf cf"
					+ "  WHERE cf.precrmcf_precrmfollowupdays_id = " + precrmcf_precrmfollowupdays_id + ""
					+ "),"
					+ " '" + precrmcf_print + "',"
					+ " '" + precrmcf_fieldref + "',"
					+ " '" + precrmcf_instruction + "',"
					+ " " + precrmcf_voc + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ")";
			// SOP("StrSql--------------" + StrSqlBreaker(StrSql));
			precrmcf_id = UpdateQueryReturnID(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT precrmcf_id, precrmcf_title, precrmcf_cftype_id,"
					+ " precrmcf_numeric, precrmcf_length_min, precrmcf_length_max,"
					+ " precrmcf_option, precrmcf_unique, precrmcf_mandatory, precrmcf_voc, precrmcf_active,"
					+ " precrmcf_print, precrmcf_fieldref, precrmcf_instruction, precrmcf_entry_id,"
					+ " precrmcf_entry_date, precrmcf_modified_id, precrmcf_modified_date,"
					+ " precrmcf_cftype_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays on precrmfollowupdays_id = precrmcf_precrmfollowupdays_id" // to delete later
					+ " WHERE precrmcf_id = " + precrmcf_id + "";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					precrmcf_title = crs.getString("precrmcf_title");
					// crmcf_ref_title = crs.getString("crmcf_ref_title");
					precrmcf_cftype_id = crs.getString("precrmcf_cftype_id");
					precrmcf_length_min = crs.getString("precrmcf_length_min");
					precrmcf_length_max = crs.getString("precrmcf_length_max");
					precrmcf_option = crs.getString("precrmcf_option");
					precrmcf_numeric = crs.getString("precrmcf_numeric");
					precrmcf_unique = crs.getString("precrmcf_unique");
					precrmcf_mandatory = crs.getString("precrmcf_mandatory");
					precrmcf_voc = crs.getString("precrmcf_voc");
					precrmcf_active = crs.getString("precrmcf_active");
					precrmcf_print = crs.getString("precrmcf_print");
					precrmcf_fieldref = crs.getString("precrmcf_fieldref");
					precrmcf_instruction = crs.getString("precrmcf_instruction");
					precrmcf_entry_id = crs.getString("precrmcf_entry_id");
					if (!crs.getString("precrmcf_entry_id").equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("precrmcf_entry_id"));
						entry_date = strToLongDate(crs.getString("precrmcf_entry_date"));
					}
					precrmcf_modified_date = crs.getString("precrmcf_modified_date");
					if (!crs.getString("precrmcf_modified_id").equals("0")) {
						modified_by = Exename(comp_id, crs.getInt("precrmcf_modified_id"));
						modified_date = strToLongDate(crs.getString("precrmcf_modified_date"));
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
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crm_cf"
					+ " SET"
					+ " precrmcf_title = '" + precrmcf_title + "',"
					+ " precrmcf_numeric = '" + precrmcf_numeric + "',"
					+ " precrmcf_cftype_id = " + precrmcf_cftype_id + ","
					+ " precrmcf_length_min = " + precrmcf_length_min + ","
					+ " precrmcf_length_max = " + precrmcf_length_max + ","
					+ " precrmcf_option = '" + precrmcf_option + "',"
					+ " precrmcf_unique = '" + precrmcf_unique + "',"
					+ " precrmcf_mandatory = '" + precrmcf_mandatory + "',"
					+ " precrmcf_active = '" + precrmcf_active + "',"
					+ " precrmcf_print = '" + precrmcf_print + "',"
					+ " precrmcf_fieldref = '" + precrmcf_fieldref + "',"
					+ " precrmcf_instruction = '" + precrmcf_instruction + "',"
					+ " precrmcf_voc = " + precrmcf_voc + ","
					+ " precrmcf_modified_id = " + emp_id + ","
					+ " precrmcf_modified_date = " + ToLongDate(kknow()) + ""
					+ " WHERE  precrmcf_id = " + precrmcf_id + "";
			SOP("StrSql-------------" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		}
	}

	public String PopulateCRMCfDays() throws SQLException {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT precrmfollowupdays_id, concat(precrmfollowupdays_daycount, precrmfollowupdays_desc) AS precrmfollowupdays_desc"
				+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
				+ " GROUP BY precrmfollowupdays_id"
				+ " ORDER BY precrmfollowupdays_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		while (crs.next()) {
			Str.append("<option value=").append(crs.getString("precrmfollowupdays_id"));
			Str.append(StrSelectdrop(crs.getString("precrmfollowupdays_id"), precrmcf_precrmfollowupdays_id));
			Str.append(">").append(crs.getString("precrmfollowupdays_desc")).append("</option>\n");
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
			Str.append(StrSelectdrop(crs.getString("cftype_id"), precrmcf_cftype_id));
			Str.append(">").append(crs.getString("cftype_name")).append("</option>\n");
		}
		crs.close();
		return Str.toString();
	}

	protected void DeleteFields() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_crm_trans"
				+ " WHERE precrmcftrans_precrmcf_id = " + precrmcf_id + "";
		updateQuery(StrSql);

		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_crm_cf"
				+ " WHERE precrmcf_id = " + precrmcf_id + "";
		updateQuery(StrSql);
	}
}
