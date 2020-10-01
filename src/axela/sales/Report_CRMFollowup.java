//divya 30th may 2013
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_CRMFollowup extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0", branch_id = "0";
	public String brand_id = "0";
	public String region_id = "0";
	public String BranchAccess = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String crmdays_id = "0";
	public String[] crmdays_ids, soe_ids, sob_ids;
	public String crmdays = "0", soe_id = "", sob_id = "";
	public String crm_crmfeedbacktype_id = "0";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String go = "";
	public String team_id = "";
	public String exe_id = "";
	public String crm_satisfied = "0";
	public String crmdays_crmtype_id = "0";
	public String historydate = "";
	// // public String start_crmfollowup_date = "";
	public String enquiry_emp_id = "";
	public String enquiry_id = "";
	public String enquiry_dmsno = "";
	public String pending_followup = "", satisfied = "";
	public String msg = "";
	public String emp_all_exe = "";
	public axela.sales.MIS_Check1 mis = new axela.sales.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				// ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				start_time = DateToShortDate(kknow());
				end_time = DateToShortDate(kknow());

				GetValues(request, response);
				// SOP("brand_id==11==" + brand_id);
				// SOP("branch_id==22==" + branch_id);
				// SOP("crmdays_id==33==" + crmdays_crmtype_id);
				if (go.equals("Go")) {
					CheckForm();
					if (!brand_id.equals("0")) {
						mis.brand_id = brand_id;
						StrSearch = StrSearch + " AND branch_brand_id = " + brand_id;
					}
					if (!region_id.equals("0")) {
						StrSearch = StrSearch + " AND branch_region_id = " + region_id;
					}
					if (!branch_id.equals("0")) {
						mis.branch_id = branch_id;
						StrSearch = StrSearch + " AND enquiry_branch_id = " + branch_id;
					}
					if (!team_id.equals("0")) {
						StrSearch = StrSearch + " AND teamtrans_team_id = " + team_id;
					}
					if (!exe_id.equals("0")) {
						StrSearch = StrSearch + " AND teamtrans_emp_id = " + exe_id;
					}
					if (!crmdays_crmtype_id.equals("0")) {
						mis.crmtype_id = crmdays_crmtype_id;
						StrSearch = StrSearch + " AND crmdays_crmtype_id = " + crmdays_crmtype_id;
					}
					if (!crmdays_id.equals("")) {
						StrSearch = StrSearch + " AND crm_crmdays_id IN (" + crmdays_id + ")";
					}
					if (!crm_crmfeedbacktype_id.equals("0") && pending_followup.equals("0")) {
						StrSearch += " AND crm_crmfeedbacktype_id = " + crm_crmfeedbacktype_id + "";
					}
					if (!crm_satisfied.equals("0") && pending_followup.equals("0")) {
						StrSearch += " AND crm_satisfied = " + crm_satisfied;
					}
					if (!starttime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(crm_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(crm_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (pending_followup.equals("1")) {
						StrSearch = StrSearch + " AND crm_crmfeedbacktype_id = 0"
								+ " AND crm_desc = ''";
					}
					if (!soe_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSearch = StrSearch + " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					StrSearch += BranchAccess.replace("branch_id", "enquiry_branch_id");

					// StrSearch += ExeAccess.replace("emp_id", "crm_emp_id");

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = CRMFollowup();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// if (branch_id.equals("0")) {
		// dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		// } else {
		// dr_branch_id = branch_id;
		// }
		brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
		region_id = CNumeric(PadQuotes(request.getParameter("dr_region")));
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		team_id = CNumeric(PadQuotes(request.getParameter("dr_team")));
		exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		crmdays_id = RetrunSelectArrVal(request, "dr_crmdays_id");
		crmdays_ids = request.getParameterValues("dr_crmdays_id");
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
		crm_crmfeedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_feedbacktype")));
		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		crm_satisfied = CNumeric(PadQuotes(request.getParameter("dr_crm_satisfied")));
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
		sob_id = RetrunSelectArrVal(request, "dr_sob");
		sob_ids = request.getParameterValues("dr_sob");

		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (!PadQuotes(request.getParameter("txt_dr_crmdays_id")).equals("")) {
			crmdays_id = CNumeric(PadQuotes(request.getParameter("txt_dr_crmdays_id")));
		}

		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}
	}

	public String CRMFollowup() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT enquiry_id, customer_id, customer_name, contact_id,"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
					+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2,"
					+ " contact_email1, contact_email2,"
					+ " enquiry_date, enquiry_close_date,"
					+ " COALESCE(item_name,'') AS item_name, stage_name, status_name,"
					+ " crm_followup_time, crm_desc,"
					+ " COALESCE(crmfeedbacktype_name,'') as crmfeedbacktype_name,"
					+ " COALESCE(crm_satisfied, 0) AS crm_satisfied,"
					+ " COALESCE(e.emp_id, 0) as enquiry_emp_id,"
					+ " COALESCE(CONCAT(e.emp_name,' (', e.emp_ref_no, ')'), '') AS enquiry_emp_name,"
					+ " COALESCE(c.emp_id, 0) AS crm_emp_id,"
					+ " COALESCE(CONCAT(c.emp_name,' (', c.emp_ref_no, ')'), '') AS emp_name,"
					+ " crm_id, crmdays_daycount, crmdays_desc,"
					+ " COALESCE(ticket_id, 0) AS ticket_id,"
					+ " COALESCE(ticket_ticketstatus_id, 0) AS ticket_ticketstatus_id,"
					+ " COALESCE(ticketstatus_name, '') AS ticketstatus_name,"
					+ " COALESCE(ticket_closed_comments, '') AS ticket_closed_comments"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crm_crmdays_id = crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp c ON c.emp_id = crm_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket ON ticket_crm_id = crm_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
					+ " LEFT JOIN " + maindb() + "sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id";
			if (!branch_id.equals("0") || !brand_id.equals("0") || !region_id.equals("0")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id ";
			}
			StrSql += " WHERE 1 = 1 ";
			StrSql += StrSearch
					+ " GROUP BY crm_id"
					+ " ORDER BY crm_followup_time"
					+ " LIMIT 1000";
			SOP("StrSql=====crm folwup====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th align=center>#</th>\n");
				Str.append("<th data-toggle=\"true\" align=center>Enquiry ID</th>\n");
				Str.append("<th style=\"width:200px;\" align=center>Customer</th></style>\n");
				Str.append("<th align=center>Date</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Variant</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Stage</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Status</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>CRM Executives</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>CRM Follow-up Date</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>CRM Follow-up Days</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>CRM Feedback Type</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Experience</th>\n");
				Str.append("<th data-hide=\"phone\" align=center>Description</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("enquiry_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("enquiry_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=enquiry-list.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append(">").append(crs.getString("enquiry_id")).append("</a></td>\n");
					Str.append("<td align=left>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");

					if (!crs.getString("contact_phone1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_phone1"), 4, "T", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_phone1"), comp_id));
					}

					if (!crs.getString("contact_phone2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_phone2"), 4, "T", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_phone2"), comp_id));
					}

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("enquiry_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("enquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("enquiry_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append(strToShortDate(crs.getString("enquiry_date"))).append(" - ").append(strToShortDate(crs.getString("enquiry_close_date")));
					Str.append("</td>");
					Str.append("<td align=left>").append(crs.getString("item_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("stage_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("status_name")).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("enquiry_emp_id"))
							.append(">").append(crs.getString("enquiry_emp_name")).append("</a></td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("crm_emp_id")).append(">")
							.append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("&crm_id=")
							.append(crs.getString("crm_id") + "#tabs-3").append("','crmdash','');remote.focus();\">").append(strToLongDate(crs.getString("crm_followup_time"))).append("</a></td>\n");
					// Str.append("<td align=center><a href=enquiry-dash-crmfollowup.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("&crmfollowup_id=").append(crs.getString("crmfollowup_id")).append(">").append(strToShortDate(crs.getString("crmfollowup_followup_time"))).append("</a></td>\n");
					Str.append("<td align=left>").append(crs.getString("crmdays_daycount")).append(crs.getString("crmdays_desc")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("crmfeedbacktype_name")).append("</td>");
					if (crs.getString("crm_satisfied").equals("1")) {
						satisfied = "Satisfied";
					} else if (crs.getString("crm_satisfied").equals("2")) {
						satisfied = "Dis-Satisfied";
					} else if (crs.getString("crm_satisfied").equals("0")) {
						satisfied = "";
					}

					Str.append("<td align=left>").append(satisfied).append("</td>");
					Str.append("<td align=left style='width: 14em;'>").append(crs.getString("crm_desc"));
					if (!crs.getString("ticket_id").equals("0")) {
						Str.append("<br>Ticket ID: <a href=../service/ticket-list.jsp?ticket_id=").append(crs.getString("ticket_id")).append(">")
								.append(crs.getString("ticket_id")).append("</a>");
						Str.append("<br> Ticket Status: " + crs.getString("ticketstatus_name"));
						if (!crs.getString("ticket_closed_comments").equals("")) {
							Str.append("<br> Closing Summary: " + crs.getString("ticket_closed_comments"));
						}
					}

					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No CRM Follow-up found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulatePrincipal(String brand_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// //SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " AND branch_branchtype_id IN (1,2)"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("brand query======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateRegion(String brand_id, String region_id, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id = region_id"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id IN (1, 2)"
					+ BranchAccess;
			if (!brand_id.equals("") && !brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_region\" class=\"form-control\" id=\"dr_region\" onchange=\"PopulateBranches();\">");
			if (crs.isBeforeFirst()) {
				Str.append("<option value=0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("region_id")).append("");
					Str.append(StrSelectdrop(crs.getString("region_id"), region_id));
					Str.append(">").append(crs.getString("region_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String brand_id, String region_id, String branch_id, String comp_id, HttpServletRequest request) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
					+ BranchAccess;
			if (!brand_id.equals("") && !brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==PopulateBranches==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class=form-control onchange=\"PopulateExecutives();PopulateTeams();PopulateCRMDays();\">");
			Str.append("<option value='0'>Select</option>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(StrSelectdrop(crs.getInt("branch_id") + "", branch_id));
					Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
				}
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

	public String PopulateTeam(String exe_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
					+ " WHERE 1 = 1 "
					+ " AND team_active = 1";
			// + ExeAccess.replace("emp_id", "teamtrans_emp_id");
			// if (!exe_branch_id.equals("0") && !exe_branch_id.equals("")) {
			StrSql += " AND team_branch_id =" + exe_branch_id;
			// }
			StrSql += " GROUP BY team_id"
					+ " ORDER BY team_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_team\" class=\"form-control\" id=\"dr_team\" onchange=\"PopulateExecutives();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id"));
				Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option>\n");
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

	public String PopulateSalesExecutives(String branch_id, String exe_team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			if (!branch_id.equals("0")) {
				StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
						+ " WHERE emp_active = '1'"
						+ " AND emp_sales = '1' ";
				// + ExeAccess;
				if (!exe_team_id.equals("") && !exe_team_id.equals("0")) {
					StrSql += " AND teamtrans_team_id =" + exe_team_id;
				}
				// if (!branch_id.equals("") && !branch_id.equals("0")) {
				StrSql += " AND emp_branch_id =" + branch_id;
				// }
				StrSql += " GROUP BY emp_id"
						+ " ORDER BY emp_name";
				// SOP("StrSQl--exe--" + StrSql);
				crs = processQuery(StrSql, 0);

				Str.append("<select name=\"dr_executive\" class=\"form-control\" id=\"dr_executive\">");
				Str.append("<option value=0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("emp_id")).append("");
					Str.append(StrSelectdrop(crs.getString("emp_id"), exe_id));
					Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
				}
				Str.append("</select>");
				crs.close();
			} else {
				Str.append("<select name=\"dr_executive\" class=\"form-control\" id=\"dr_executive\">");
				Str.append("<option value=0>Select</option>");
				Str.append("</select>");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMDays(String comp_id, String crmdays_crmtype_id, String crmdays_id, String branch_id, String brand_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmdays_id, CONCAT(crmdays_daycount, crmdays_desc) AS crmdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " WHERE 1 = 1";
			if (!branch_id.equals("0") && !branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			if (!brand_id.equals("0") && !brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0") && !crmdays_crmtype_id.equals("")) {
				StrSql += " AND crmdays_crmtype_id =" + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmdays_id"
					+ " ORDER BY crmdays_daycount";
			// SOP("StrSql-------------followup---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_crmdays_id\" name=\"dr_crmdays_id\" class='form-control' onchange=\"GetCrmDaysId();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmdays_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmdays_id"), crmdays_id));
				Str.append(">").append(crs.getString("crmdays_desc")).append("</option> \n");
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

	public String PopulateCRMDaysMulti(String comp_id, String crmdays_crmtype_id, String[] crmdays_ids, String branch_id, String brand_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmdays_id, CONCAT(crmdays_daycount, crmdays_desc) AS crmdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = crmdays_brand_id"
					+ " WHERE 1 = 1";
			if (!branch_id.equals("0") && !branch_id.equals("")) {
				StrSql += " AND branch_id = " + branch_id + "";
			}
			if (!brand_id.equals("0") && !brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!crmdays_crmtype_id.equals("0") && !crmdays_crmtype_id.equals("")) {
				StrSql += " AND crmdays_crmtype_id = " + crmdays_crmtype_id + "";
			}
			StrSql += " GROUP BY crmdays_id"
					+ " ORDER BY crmdays_daycount";

			// SOP("PopulateCRMDaysMulti-----" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_crmdays_id\" name=\"dr_crmdays_id\" class='form-control multiselect-dropdown' multiple onchange=\"GetCrmDaysId();\">");
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmdays_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("crmdays_id"), crmdays_ids));
				Str.append(">").append(crs.getString("crmdays_desc")).append("</option> \n");
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

	public String PopulateCRMFeedbackType() {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT crmfeedbacktype_id, crmfeedbacktype_name"
					+ " FROM " + maindb() + "sales_crm_feedbacktype"
					+ " WHERE 1 = 1"
					+ " ORDER BY crmfeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmfeedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmfeedbacktype_id"), crm_crmfeedbacktype_id));
				Str.append(">").append(crs.getString("crmfeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCRMSatisfied() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", crm_satisfied)).append(">Satisfied</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", crm_satisfied)).append(">Dis-Satisfied</option>\n");
		return Str.toString();
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
		if (crmdays_crmtype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
		// if (crmdays_crmtype_id.equals("0")) {
		// msg = msg + "<br>Select CRM Type!";
		// }

	}

	public String PopulateCRMType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmtype_id, crmtype_name"
					+ " FROM axela_sales_crm_type"
					+ " ORDER BY crmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmtype_id"), crmdays_crmtype_id));
				Str.append(">").append(crs.getString("crmtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSoe() {
		String sb = "";
		try
		{
			StrSql = " SELECT soe_id, soe_name "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id=model_id"
					+ " ORDER BY soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sb = sb + "<option value=" + crs.getString("soe_id") + "";
				sb = sb + ArrSelectdrop(crs.getInt("soe_id"), soe_ids);
				sb = sb + ">" + crs.getString("soe_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb;
	}

	public String PopulateSob(String soe_id, String comp_id, HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try
		{
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1";
			if (!soe_id.equals("")) {
				StrSql += " AND soetrans_soe_id IN (" + soe_id + ")";
			}
			StrSql += " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("SOB===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			sb.append("<select name='dr_sob' id='dr_sob' multiple='multiple' class='form-control multiselect-dropdown'>");
			while (crs.next()) {
				sb.append("<option value=").append(crs.getString("sob_id")).append("");
				sb.append(ArrSelectdrop(crs.getInt("sob_id"), sob_ids));
				sb.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			sb.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb.toString();
	}
}
