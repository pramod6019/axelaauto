package axela.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class PSFCustomField_Update extends Connect {

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
	// public String comp_id = "0";
	public String jcpsfcf_id = "0";
	public String jcpsfcf_crmdays_id = "0";
	public String jcpsfcf_title = "", daydesc = "";
	public String jcpsfcf_cftype_id = "0";
	public String jcpsfcf_refjcpsfcf_id = "0";
	public String jcpsfcf_numeric = "0";
	public String jcpsfcf_length_min = "";
	public String jcpsfcf_length_max = "";
	public String jcpsfcf_option = "";
	public String jcpsfcf_unique = "0";
	public String jcpsfcf_mandatory = "0";
	public String jcpsfcf_voc = "0";
	public String jcpsfcf_instruction = "";
	public String jcpsfcf_print = "0";
	// public String jcpsfcf_print = "0";
	public String jcpsfcf_fieldref = "";
	public String jcpsfcf_active = "0";
	public String crmcf_rank = "0";
	public String psfdays_brand_id = "0";
	public String psfdays_id = "0";
	public String psfdays_exe_type = "0";
	public String psfdays_daycount = "0";
	public String psfdays_desc = "";
	public String jcpsfcf_entry_id = "0";
	public String jcpsfcf_entry_date = "";
	public String jcpsfcf_modified_id = "0";
	public String jcpsfcf_modified_date = "";
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
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_role_id", request, response);
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
				jcpsfcf_id = CNumeric(PadQuotes(request.getParameter("jcpsfcf_id")));
				jcpsfcf_crmdays_id = CNumeric(PadQuotes(request.getParameter("psfdays_id")));
				psfdays_brand_id = CNumeric(PadQuotes(request.getParameter("psfdays_brand_id")));
				psfdays_id = CNumeric(PadQuotes(request.getParameter("psfdays_id")));
				// SOP("psfdays_brand_id----1-"+psfdays_brand_id);
				if ((jcpsfcf_id.equals("0")))
				{
					jcpsfcf_id = ExecuteQuery("SELECT psfdays_brand_id FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
							// + " WHERE jcpsfcf_id = "+jcpsfcf_id+"");
							+ " WHERE 1 = 1");
				}

				if (psfdays_id.equals("0") && (!jcpsfcf_id.equals("0"))) {
					psfdays_id = ExecuteQuery("SELECT jcpsfcf_crmdays_id FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
							+ " WHERE jcpsfcf_id = " + jcpsfcf_id + "");

				}
				daydesc = ExecuteQuery("SELECT CONCAT(psfdays_daycount,psfdays_desc) FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
						+ " WHERE psfdays_id = " + psfdays_id + "");

				if (psfdays_brand_id.equals("0") && (!jcpsfcf_id.equals("0"))) {
					psfdays_brand_id = ExecuteQuery("SELECT psfdays_brand_id FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
							// + " WHERE jcpsfcf_id = "+jcpsfcf_id+"");
							+ " WHERE 1 = 1");
				}
				// SOP("psfdays_brand_id--112-----"+psfdays_brand_id);

				// if((jcpsfcf_id.equals("0")))
				// {
				// jcpsfcf_id = ExecuteQuery("SELECT psfdays_brand_id FROM " +
				// compdb(comp_id) + "axela_service_jc_psfdays"
				// + " INNER JOIN " + compdb(comp_id) +
				// "axela_service_jc_psf_cf ON jcpsfcf_crmdays_id = psfdays_id"
				// //+ " WHERE jcpsfcf_id = "+jcpsfcf_id+"");
				// + " WHERE 1 = 1");
				// }

				LinkHeader += " &gt; <a href=psfdays.jsp?psfdays_brand_id=" + psfdays_brand_id + ">" + daydesc + "</a> &gt;";
				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						jcpsfcf_cftype_id = "1";
						jcpsfcf_active = "1";
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("psfcustomfield-list.jsp?psfdays_brand_id= " + psfdays_brand_id + "&psfdays_id=" + psfdays_id
									+ "&msg=Custom Field Added Successfully!" + msg + ""));
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
							response.sendRedirect(response.encodeRedirectURL("psfcustomfield-list.jsp?psfdays_brand_id= " + psfdays_brand_id + "&psfdays_id=" + psfdays_id
									+ "&msg=Custom Field Updated Successfully!" + msg + ""));
						}
					} else if (deleteB.equals("Delete Custom Field")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("psfcustomfield-list.jsp?psfdays_brand_id= " + psfdays_brand_id + "&psfdays_id=" + psfdays_id
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
		jcpsfcf_title = PadQuotes(request.getParameter("txt_jcpsfcf_title"));
		jcpsfcf_cftype_id = CNumeric(PadQuotes(request.getParameter("dr_jcpsfcf_cftype_id")));
		jcpsfcf_refjcpsfcf_id = CNumeric(PadQuotes(request.getParameter("dr_jcpsfcf_refjcpsfcf_id")));
		jcpsfcf_numeric = CheckBoxValue(PadQuotes(request.getParameter("chk_jcpsfcf_numeric")));
		jcpsfcf_length_min = PadQuotes(request.getParameter("txt_jcpsfcf_length_min"));
		jcpsfcf_length_max = PadQuotes(request.getParameter("txt_jcpsfcf_length_max"));
		jcpsfcf_option = PadQuotes(request.getParameter("txt_jcpsfcf_option"));
		jcpsfcf_unique = CheckBoxValue(PadQuotes(request.getParameter("chk_jcpsfcf_unique")));
		jcpsfcf_mandatory = CheckBoxValue(PadQuotes(request.getParameter("chk_jcpsfcf_mandatory")));
		jcpsfcf_voc = CheckBoxValue(PadQuotes(request.getParameter("chk_jcpsfcf_voc")));
		jcpsfcf_active = CheckBoxValue(PadQuotes(request.getParameter("chk_jcpsfcf_active")));
		jcpsfcf_print = CheckBoxValue(PadQuotes(request.getParameter("chk_jcpsfcf_print")));
		jcpsfcf_fieldref = PadQuotes(request.getParameter("txt_jcpsfcf_fieldref"));
		jcpsfcf_instruction = PadQuotes(request.getParameter("txt_jcpsfcf_instruction"));
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
		if (jcpsfcf_title.equals("")) {
			msg += "<br>Enter Title!";
		} else {
			StrSql = "SELECT jcpsfcf_title"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " WHERE jcpsfcf_title = '" + jcpsfcf_title + "'"
					+ " AND jcpsfcf_crmdays_id = " + jcpsfcf_crmdays_id + "";
			if (update.equals("yes")) {
				StrSql += " AND jcpsfcf_id != " + jcpsfcf_id + "";
			}
			if (ExecuteQuery(StrSql).equals(jcpsfcf_title)) {
				msg += "<br>Similar Title Found!";
			}
		}

		if (jcpsfcf_cftype_id.equals("0")) {
			msg += "<br>Select Type!";
		}

		jcpsfcf_length_min = CNumeric(jcpsfcf_length_min);
		jcpsfcf_length_max = CNumeric(jcpsfcf_length_max);
		if (jcpsfcf_cftype_id.equals("3")) {
			jcpsfcf_length_max = "1";
		} else if (jcpsfcf_cftype_id.equals("4")) {
			jcpsfcf_length_max = "10";
		} else if (jcpsfcf_cftype_id.equals("5")) {
			jcpsfcf_length_max = "10";
		} else if (jcpsfcf_cftype_id.equals("6")) {
			jcpsfcf_length_max = "16";
		} else if (jcpsfcf_cftype_id.equals("7")) {
			jcpsfcf_length_max = "5";
		}

		if (jcpsfcf_length_max.equals("0")) {
			msg += "<br>Maximum Length should be greater than 0!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " (jcpsfcf_title,"
					+ " jcpsfcf_crmdays_id,"
					+ " jcpsfcf_cftype_id,"
					+ " jcpsfcf_numeric,"
					+ " jcpsfcf_length_min,"
					+ " jcpsfcf_length_max,"
					+ " jcpsfcf_option,"
					+ " jcpsfcf_unique,"
					+ " jcpsfcf_mandatory,"
					+ " jcpsfcf_active,"
					+ " jcpsfcf_rank,"
					+ " jcpsfcf_print,"
					+ " jcpsfcf_fieldref,"
					+ " jcpsfcf_instruction,"
					+ " jcpsfcf_voc,"
					+ " jcpsfcf_entry_id,"
					+ " jcpsfcf_entry_date)"
					+ " VALUES"
					+ " ('" + jcpsfcf_title + "',"
					+ " " + jcpsfcf_crmdays_id + ","
					+ " " + jcpsfcf_cftype_id + ","
					+ " '" + jcpsfcf_numeric + "',"
					+ " " + jcpsfcf_length_min + ","
					+ " " + jcpsfcf_length_max + ","
					+ " '" + jcpsfcf_option + "',"
					+ " '" + jcpsfcf_unique + "',"
					+ " '" + jcpsfcf_mandatory + "',"
					+ " '" + jcpsfcf_active + "',"
					+ " (SELECT(COALESCE(MAX(jcpsfcf_rank), 0) + 1)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf as rank"
					// + " WHERE cf.customfield_submodule_id = " + submodule_id
					+ "),"
					// + " '" + jcpsfcf_rank + "',"
					+ " '" + jcpsfcf_print + "',"
					+ " '" + jcpsfcf_fieldref + "',"
					+ " '" + jcpsfcf_instruction + "',"
					+ " " + jcpsfcf_voc + ","
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ")";
			jcpsfcf_id = UpdateQueryReturnID(StrSql);
			SOP("StrSql===" + StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, jcpsfcf_cftype_id, jcpsfcf_refjcpsfcf_id,"
					+ " jcpsfcf_numeric, jcpsfcf_length_min, jcpsfcf_length_max,"
					+ " jcpsfcf_option, jcpsfcf_unique, jcpsfcf_mandatory, jcpsfcf_voc, jcpsfcf_active,"
					+ " jcpsfcf_print, jcpsfcf_fieldref, jcpsfcf_instruction, jcpsfcf_entry_id,"
					+ " jcpsfcf_entry_date, jcpsfcf_modified_id, jcpsfcf_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " WHERE jcpsfcf_id = " + jcpsfcf_id + "";
			// SOP("StrSql====="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					jcpsfcf_title = crs.getString("jcpsfcf_title");
					jcpsfcf_cftype_id = crs.getString("jcpsfcf_cftype_id");
					jcpsfcf_refjcpsfcf_id = crs.getString("jcpsfcf_refjcpsfcf_id");
					jcpsfcf_length_min = crs.getString("jcpsfcf_length_min");
					jcpsfcf_length_max = crs.getString("jcpsfcf_length_max");
					jcpsfcf_option = crs.getString("jcpsfcf_option");
					jcpsfcf_numeric = crs.getString("jcpsfcf_numeric");
					jcpsfcf_unique = crs.getString("jcpsfcf_unique");
					jcpsfcf_mandatory = crs.getString("jcpsfcf_mandatory");
					jcpsfcf_voc = crs.getString("jcpsfcf_voc");
					jcpsfcf_active = crs.getString("jcpsfcf_active");
					jcpsfcf_print = crs.getString("jcpsfcf_print");
					jcpsfcf_fieldref = crs.getString("jcpsfcf_fieldref");
					jcpsfcf_instruction = crs.getString("jcpsfcf_instruction");
					jcpsfcf_entry_id = crs.getString("jcpsfcf_entry_id");
					if (!jcpsfcf_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(jcpsfcf_entry_id));
						entry_date = ConvertLongDateToStr(entry_date);
					}
					jcpsfcf_modified_id = crs.getString("jcpsfcf_modified_id");
					if (!jcpsfcf_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(jcpsfcf_modified_id));
						modified_date = ConvertLongDateToStr(modified_date);
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
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " SET"
					+ " jcpsfcf_title = '" + jcpsfcf_title + "',"
					+ " jcpsfcf_numeric = '" + jcpsfcf_numeric + "',"
					+ " jcpsfcf_cftype_id = " + jcpsfcf_cftype_id + ","
					+ " jcpsfcf_refjcpsfcf_id = " + jcpsfcf_refjcpsfcf_id + ","
					+ " jcpsfcf_length_min = " + jcpsfcf_length_min + ","
					+ " jcpsfcf_length_max = " + jcpsfcf_length_max + ","
					+ " jcpsfcf_option = '" + jcpsfcf_option + "',"
					+ " jcpsfcf_unique = '" + jcpsfcf_unique + "',"
					+ " jcpsfcf_mandatory = '" + jcpsfcf_mandatory + "',"
					+ " jcpsfcf_active = '" + jcpsfcf_active + "',"
					+ " jcpsfcf_print = '" + jcpsfcf_print + "',"
					+ " jcpsfcf_fieldref = '" + jcpsfcf_fieldref + "',"
					+ " jcpsfcf_instruction = '" + jcpsfcf_instruction + "',"
					+ " jcpsfcf_voc = " + jcpsfcf_voc + ","
					+ " jcpsfcf_modified_id = " + emp_id + ","
					+ " jcpsfcf_modified_date = " + ToLongDate(kknow()) + ""
					+ " WHERE  jcpsfcf_id = " + jcpsfcf_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateCRMCfDays() throws SQLException {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT psfdays_id, concat(psfdays_daycount, psfdays_desc) AS psfdays_desc"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
				+ " GROUP BY psfdays_id"
				+ " ORDER BY psfdays_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		while (crs.next()) {
			Str.append("<option value=").append(crs.getString("psfdays_id"));
			Str.append(StrSelectdrop(crs.getString("psfdays_id"), jcpsfcf_crmdays_id));
			Str.append(">").append(crs.getString("psfdays_desc")).append("</option>\n");
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
			Str.append(StrSelectdrop(crs.getString("cftype_id"), jcpsfcf_cftype_id));
			Str.append(">").append(crs.getString("cftype_name")).append("</option>\n");
		}
		crs.close();
		return Str.toString();
	}

	public String PopulateRefTitle() throws SQLException {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT psfdays_id, psfdays_brand_id, jcpsfcf_id, jcpsfcf_title"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsfcf_crmdays_id "
				+ " WHERE psfdays_brand_id IN (6) "
				+ " AND concat( psfdays_daycount,'-',psfdays_jccat_id, '-', jcpsfcf_cftype_id ) "
				+ " IN ( SELECT concat( refjcpsfdays.psfdays_daycount,'-', psfdays_jccat_id, '-', refjcpsfcf.jcpsfcf_cftype_id ) "
				+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays AS refjcpsfdays "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_cf refjcpsfcf ON "
				+ " refjcpsfcf.jcpsfcf_crmdays_id = refjcpsfdays.psfdays_id "
				+ " WHERE 1 = 1 "
				+ " AND refjcpsfcf.jcpsfcf_id = " + jcpsfcf_id + " ) ORDER BY jcpsfcf_rank";
		// SOP("StrSql== for PopulateRefTitle ===" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value =0>Select</option>");
		while (crs.next()) {
			Str.append("<option value=").append(crs.getString("jcpsfcf_id"));
			Str.append(StrSelectdrop(crs.getString("jcpsfcf_id"), jcpsfcf_refjcpsfcf_id));
			Str.append(">").append("(").append(crs.getString("psfdays_id")).append("-").append(crs.getString("psfdays_brand_id")).append(") ")
					.append(crs.getString("jcpsfcf_title"))
					.append("</option>\n");
		}
		crs.close();
		return Str.toString();
	}

	protected void DeleteFields() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm_trans"
				// + " WHERE cftrans_submodule_id = " + submodule_id + ""
				+ " WHERE crmcftrans_crmcf_id = " + jcpsfcf_id + "";
		updateQuery(StrSql);

		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
				+ " WHERE jcpsfcf_id = " + jcpsfcf_id + "";
		updateQuery(StrSql);
	}
}
