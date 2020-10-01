package axela.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ticket_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String ExeAccess = "", BranchAccess = "";
	public String comp_id = "0", emp_id = "0";

	public String[] brand_ids, region_ids, branch_ids, owner_ids;
	public String brand_id = "", region_id = "", branch_id = "", owner_id = "", msg = "", go = "", tickettype_id = "", ticketdays_id = "";

	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_ticket_access", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();

				if (go.equals("Go")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}

					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}

					if (!branch_id.equals("")) {
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}

					if (!owner_id.equals("")) {
						StrSearch += " AND ticket_emp_id IN (" + owner_id + ")";
					}

					if (!tickettype_id.equals("")) {
						StrSearch += " AND ticket_tickettype_id IN (" + tickettype_id + ")";
					}

					if (!ticketdays_id.equals("")) {
						if (tickettype_id.equals("1") || tickettype_id.equals("2") || tickettype_id.equals("3")) {
							StrSearch += " AND ticket_crm_id IN (SELECT crm_id FROM " + compdb(comp_id) + "axela_sales_crm WHERE crm_crmdays_id = " + ticketdays_id + ")";
						}
						else if (tickettype_id.equals("4")) {
							StrSearch += " AND ticket_jcpsf_id IN (SELECT jcpsf_id FROM " + compdb(comp_id) + "axela_service_jc_psf WHERE jcpsf_psfdays_id = " + ticketdays_id + ")";
						}
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						// SetSession("ticketstrsql", StrSearch, request);
						StrHTML = EnquiryTriggerStatus();
						if (StrHTML.equals("")) {
							StrHTML = "<font color='#ff0000'><b>No Follow-up Found!</b></font>";
						}
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String EnquiryTriggerStatus() throws SQLException {
		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"table-bordered\">\n");
		Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
		Str.append("<thead><tr>\n");
		Str.append("");
		Str.append("<th >Level 1</th>\n");
		Str.append("<th >Level 2</th>\n");
		Str.append("<th >Level 3</th>\n");
		Str.append("<th >Level 4</th>\n");
		Str.append("<th >Level 5</th>\n");
		Str.append("</tr>\n");
		Str.append("</thead>\n");
		Str.append("<tbody>\n");

		for (int i = 1; i <= 5; i++) {
			StrSql = " SELECT ticket_id, concat(emp_name,' (',emp_ref_no,')') AS emp_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id= ticket_branch_id"
					+ " WHERE 1=1 "
					+ " AND ticket_trigger=" + i + " AND ticket_ticketstatus_id!=3"
					+ ExeAccess
					+ StrSearch + BranchAccess
					+ " GROUP BY ticket_id ORDER BY emp_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<td valign=top align=left >");
				while (crs.next()) {
					Str.append("<a href=ticket-list.jsp?ticket_id=").append(crs.getString("ticket_id")).append(">").append(crs.getString("ticket_id")).append(": ").append(crs.getString("emp_name"))
							.append("</a><br>");
				}
				Str.append("</td>");

			} else {
				Str.append("<td valign=top align=center>--<br><br></td>");
			}
			crs.close();
		}
		Str.append("</tr>");
		Str.append("</tbody>\n");
		Str.append("</table>\n");
		Str.append("</div>\n");
		Str.append(getDepartment());
		return Str.toString();
	}

	public String getDepartment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT * "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " ORDER BY ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Department</th>\n");
				Str.append("<th>Description</th>\n");
				Str.append("<th data-hide=\"phone\">Due Hours</th>\n");
				Str.append("<th data-hide=\"phone\">Level 1</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 2</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 3</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 4</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Level 5</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("ticket_dept_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("ticket_dept_desc")).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("ticket_dept_trigger5_hrs"))).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		owner_id = RetrunSelectArrVal(request, "dr_owner_id");
		owner_ids = request.getParameterValues("dr_owner_id");
		tickettype_id = CNumeric(PadQuotes(request.getParameter("dr_tickettype_id")));
		ticketdays_id = CNumeric(PadQuotes(request.getParameter("dr_ticketdays_id")));

	}

	protected void CheckForm() {
		msg = "";
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type "
					+ " WHERE 1 = 1 "
					+ " GROUP BY tickettype_id "
					+ " ORDER BY tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("tickettype_id"), tickettype_id));
				Str.append(">").append(crs.getString("tickettype_name")).append("</option><br> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDays(String comp_id, String tickettype_id, String ticket_brand_id) {
		StringBuilder Str = new StringBuilder();
		// SOP("tickettype_id===" + tickettype_id);
		// SOP("ticket_brand_id===" + ticket_brand_id);
		try {
			if (tickettype_id.equals("1") || tickettype_id.equals("2") || tickettype_id.equals("3")) {
				StrSql = "SELECT COALESCE(crmdays_id,0) AS id,"
						+ " COALESCE(CONCAT(crmdays_daycount, crmdays_desc),'' )AS days_desc, brand_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " INNER JOIN axela_brand ON brand_id = crmdays_brand_id"
						+ " WHERE 1 = 1";
				StrSql += " AND crmdays_crmtype_id =" + tickettype_id + "";
				if (!ticket_brand_id.equals("null") && !ticket_brand_id.equals("")) {
					StrSql += " AND brand_id IN ( " + ticket_brand_id + " )";
				}
				StrSql += " GROUP BY brand_id, crmdays_id"
						+ " ORDER BY brand_name, crmdays_daycount";
			}
			else if (tickettype_id.equals("4")) {
				StrSql = "SELECT COALESCE(psfdays_id,0) AS id,"
						+ " COALESCE(CONCAT(psfdays_daycount, psfdays_desc),'') AS days_desc, brand_name"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
						+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
						+ " WHERE 1=1";
				if (!ticket_brand_id.equals("null") && !ticket_brand_id.equals("")) {
					StrSql += " AND brand_id IN ( " + ticket_brand_id + " )";
				}
				StrSql += " GROUP BY brand_id, psfdays_id"
						+ " ORDER BY brand_name, psfdays_daycount";
			}
			else {
				StrSql = "SELECT '' AS id, '' AS days_desc, '' AS brand_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " WHERE 1<>1";
			}
			// SOP("StrSql-------------PopulateDays---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_ticketdays_id\" name=\"dr_ticketdays_id\" class='form-control'>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("id")).append("");
				Str.append(StrSelectdrop(crs.getString("id"), ticketdays_id));
				Str.append(">").append(crs.getString("brand_name")).append(" - ").append(crs.getString("days_desc")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
