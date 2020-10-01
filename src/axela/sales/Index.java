package axela.sales;
//Murali 21st jun
//Saiman 10th dec 2012
/*Smitha Nag 2,3 april 2013 */

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String StrSql = "";
	public String submitB = "";
	public String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "";
	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String brand_id = "", region_id = "";

	public String datetype = "", StrHTML = "";
	public String curr_date = ToShortDate(kknow());
	public String back_date = "";
	public String curr_month = "", back_month = "";
	public String startquarter = "", endquarter = "";
	public String back_startquarter = "", back_endquarter = "";
	String period = "";

	// Today
	public String enquiry_countday = "0";
	public String testdrive_countday = "0";
	public String so_countday = "0";
	public String retail_countday = "0";
	public String delivered_countday = "0";
	public String cancelled_countday = "0";
	// Month
	public String enquiry_countmonth = "0";
	public String testdrive_countmonth = "0";
	public String so_countmonth = "0";
	public String retail_countmonth = "0";
	public String delivered_countmonth = "0";
	public String cancelled_countmonth = "0";
	// Quarter
	public String enquiry_countquarter = "0";
	public String testdrive_countquarter = "0";
	public String so_countquarter = "0";
	public String retail_countquarter = "0";
	public String delivered_countquarter = "0";
	public String cancelled_countquarter = "0";

	// Today
	public String enquiry_countday_old = "0";
	public String testdrive_countday_old = "0";
	public String so_countday_old = "0";
	public String retail_countday_old = "0";
	public String delivered_countday_old = "0";
	public String cancelled_countday_old = "0";
	// Month
	public String enquiry_countmonth_old = "0";
	public String testdrive_countmonth_old = "0";
	public String so_countmonth_old = "0";
	public String retail_countmonth_old = "0";
	public String delivered_countmonth_old = "0";
	public String cancelled_countmonth_old = "0";
	// Quarter
	public String enquiry_countquarter_old = "0";
	public String testdrive_countquarter_old = "0";
	public String so_countquarter_old = "0";
	public String retail_countquarter_old = "0";
	public String delivered_countquarter_old = "0";
	public String cancelled_countquarter_old = "0";

	// refresh All AJAX
	public String refreshAll = "", cards = "";

	// public String psfstatus = "";
	public String salesfunnel = "";
	public String followupstatus = "";
	public String crmfollowupstatus = "";
	public String enquirypriority = "", no_enquirypriority = "";
	public String filter = "";
	public String opt = "";

	public String ListCampaign = "";
	public String dr_branch_id = "";
	public String StrSearch = "";
	public String smart = "";
	public String ExeAccess = "";
	public String NoSalesPipeline = "";
	public String BranchAccess = "";
	public String startdate = new SimpleDateFormat("dd/MM/yyyy").format(kknow());
	public String enddate = "";
	public String exe_id = "";
	public String psfchart_data = "";
	public String followupchart_data = "";
	public String enquiryprioritychart_data = "";
	public String crmfollowupchart_data = "";
	public int TotalRecords = 0;
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_lead_access, emp_enquiry_access, emp_quote_access, emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				exe_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
				filter = PadQuotes(request.getParameter("filter"));

				if (smart == null) {
					smart = "";
				}

				refreshAll = PadQuotes(request.getParameter("refreshAll"));
				cards = PadQuotes(request.getParameter("cards"));

				// psfstatus = PadQuotes(request.getParameter("psfstatus"));
				salesfunnel = PadQuotes(request.getParameter("salesfunnel"));
				followupstatus = PadQuotes(request.getParameter("followupstatus"));
				crmfollowupstatus = PadQuotes(request.getParameter("crmfollowupstatus"));
				enquirypriority = PadQuotes(request.getParameter("enquirypriority"));

				filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
				filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
				filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));

				if (filter.equals("yes")) {
					opt = PadQuotes(request.getParameter("opt"));
					populateconfigdetails();
					CheckRedirect(opt, request, response);
				}

				enddate = AddMonth(startdate, 1);
				enddate = ConvertShortDateToStr(enddate);
				if (!PadQuotes(request.getParameter("dr_month")).equals("")) {
					startdate = PadQuotes(request.getParameter("dr_month"));
					enddate = AddMonth(startdate, 1);
					enddate = ConvertShortDateToStr(enddate);
					// SOP("Start=="+startdate);
					// SOP("emd==="+enddate);
				}

				// TargetSearch = ExeAccess.replace("emp_id", "target_emp_id") + "";
				// TargetSearch = TargetSearch + " and target_startdate>= '" + ConvertShortDateToStr(startdate) + "' and target_startdate< '" + enddate + "'";
				// DateSearch = " and startdate>= '" + ConvertShortDateToStr(startdate) + "' and startdate< '" + enddate + "'";
				// ExeSearch = ExeAccess;
				// if (!exe_id.equals("0")) {
				// TargetSearch = TargetSearch + " and target_emp_id in (" + exe_id + ")";
				// ExeSearch = " and emp_id in (" + exe_id + ")";
				// }

				if (msg.equals("") && refreshAll.equals("")) {

					if (smart.equals("yes")) {
					} else {
						SetSession("ItemIssuedstrsql", StrSearch, request);
					}
				} else if (msg.equals("")) {
					ListCampaign = CampaignSummary(comp_id, request);

					if (refreshAll.equals("yes") && followupstatus.equals("yes")) {
						StrHTML = FollowupEscStatus(comp_id);
					}
					if (refreshAll.equals("yes") && crmfollowupstatus.equals("yes")) {
						StrHTML = CRMFollowupEscStatus(comp_id);
					}
					// if (refreshAll.equals("yes") && psfstatus.equals("yes")) {
					// StrHTML = psf(comp_id);
					// }

					if (refreshAll.equals("yes") && salesfunnel.equals("yes")) {
						StrHTML = SalesPipeline(comp_id);
					}
					if (refreshAll.equals("yes") && enquirypriority.equals("yes")) {
						StrHTML = EnquiryPriorityStatus(comp_id);
					}

					if (refreshAll.equals("yes") && cards.equals("yes")) {
						populateconfigdetails();
						StrHTML = SalesDetails(comp_id);
					}

					// StrHTML =

				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String CampaignSummary(String comp_id, HttpServletRequest request) {
		int enquirycount = 0;
		StringBuilder Str = new StringBuilder();
		int count = 0;

		try {
			StrSql = " SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate, "
					+ " COALESCE((SELECT COUNT(enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE  enquiry_campaign_id = campaign_id"
					+ " ),0)  AS enqcount"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " WHERE campaign_active = '1' "
					+ " AND campaign_startdate < " + ToLongDate(kknow())
					+ " AND campaign_enddate > " + ToLongDate(kknow());
			StrSql += " GROUP BY campaign_id"
					+ " ORDER BY campaign_name";
			// SOP("strsql in LmsSummary---" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
			Str.append("<tr align=center>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>Campaign</th>\n");
			Str.append("<th>Date</th>\n");
			Str.append("<th>Enquiry</th>\n");
			Str.append("</tr>\n");
			while (crs.next()) {
				count = count + 1;
				enquirycount = enquirycount + crs.getInt("enqcount");
				Str.append("<tr>\n");
				Str.append("<td valign=top align=center><b>").append(count).append(".</b></td>\n");
				Str.append("<td valign=top align=left><a href=campaign-list.jsp?campaign_id=").append(crs.getString("campaign_id")).append(">").append(crs.getString("campaign_name"))
						.append("</a></td>\n");
				Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ").append(strToShortDate(crs.getString("campaign_enddate")))
						.append("</td>");
				Str.append("<td valign=top align=right>").append(crs.getString("enqcount")).append("</td>");
				Str.append("</tr>");
			}
			crs.close();
			Str.append("<tr>\n");
			Str.append("<td align=right>&nbsp;</td>\n");
			Str.append("<td align=right>&nbsp;</td>\n");
			Str.append("<td align=right><b>Total:</b></td>\n");
			Str.append("<td align=right><b>").append(enquirycount).append("</b></td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String FollowupEscStatus(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT gr.group_id AS group_id, COALESCE(triggercount,0) AS triggercount "
					+ " FROM ( "
					+ " SELECT 1 AS group_id "
					+ " UNION "
					+ " SELECT 2 AS group_id "
					+ " UNION "
					+ " SELECT 3 AS group_id "
					+ " UNION "
					+ " SELECT 4 AS group_id "
					+ " UNION "
					+ " SELECT 5 AS group_id "
					+ " ) AS gr "
					+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS triggercount, followup_trigger"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}
			StrSql += " WHERE followup_desc = '' "
					+ " AND followup_trigger > 0 "
					+ " AND enquiry_status_id = 1 ";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY followup_trigger) AS tr ON tr.followup_trigger = gr.group_id"
					+ " WHERE 1 = 1 "
					+ " GROUP BY group_id "
					+ " ORDER BY group_id desc";
			// SOP("StrSql======FollowupEscStatus====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			TotalRecords = 5;
			int count = 0, level = 5;
			String color = "";
			followupchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (level == 5) {
						color = "#FF0033";
					} else if (level == 4) {
						color = "#FF8030";
					} else if (level == 3) {
						color = "#7ba5de";
					} else if (level == 2) {
						color = "#F8FF01";
					} else if (level == 1) {
						color = "#c3a7e2";
					}
					count++;
					followupchart_data = followupchart_data + "{\"level\": \"Level " + level + "\", \"value\":\"" + crs.getString("triggercount") + "\", \"color\":\"" + color
							+ "\", \"url\":\"report-followup-esc-status.jsp\"}";
					level--;
					if (count < TotalRecords) {
						followupchart_data = followupchart_data + ",";
					}
				}
				followupchart_data = followupchart_data + "]";
			}
			crs.close();
			// SOP("followupchart_data-----" + followupchart_data.toString());
			return followupchart_data.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String CRMFollowupEscStatus(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT gr.group_id AS group_id, COALESCE(triggercount, 0) AS triggercount "
					+ " FROM ( "
					+ " SELECT 1 AS group_id "
					+ " UNION "
					+ " SELECT 2 AS group_id "
					+ " UNION "
					+ " SELECT 3 AS group_id "
					+ " UNION "
					+ " SELECT 4 AS group_id "
					+ " UNION "
					+ " SELECT 5 AS group_id "
					+ " ) AS gr "
					+ " LEFT JOIN (SELECT COUNT(enquiry_id) AS triggercount, crm_trigger"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}
			StrSql += " WHERE crm_desc = '' "
					+ " AND crm_trigger > 0"
					+ " AND enquiry_status_id = 1";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "crm_emp_id")
					+ " GROUP BY crm_trigger) AS tr ON tr.crm_trigger = gr.group_id"
					+ " WHERE 1=1 "
					+ " GROUP BY group_id "
					+ " ORDER BY group_id desc";
			// SOP("StrSql====CRMFollowupEscStatus====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			TotalRecords = 5;
			int count = 0, level = 5;
			String color = "";
			crmfollowupchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (level == 5) {
						color = "#FF0033";
					} else if (level == 4) {
						color = "#FF8030";
					} else if (level == 3) {
						color = "#7ba5de";
					} else if (level == 2) {
						color = "#F8FF01";
					} else if (level == 1) {
						color = "#c3a7e2";
					}
					count++;
					crmfollowupchart_data = crmfollowupchart_data + "{\"level\": \"Level " + level + "\", \"value\":\"" + crs.getString("triggercount") + "\", \"color\":\"" + color
							+ "\", \"url\":\"report-crmfollowup-esc-status.jsp\"}";
					level--;
					if (count < TotalRecords) {
						crmfollowupchart_data = crmfollowupchart_data + ",";
					}
				}
				crmfollowupchart_data = crmfollowupchart_data + "]";
			}
			crs.close();
			// SOP("Crm Followupchart-----" + crmfollowupchart_data.toString());
			return crmfollowupchart_data.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SalesPipeline(String comp_id) {
		String salespipeline_data = "";
		try {
			StrSql = "SELECT stage_name, count(enquiry_id) as stage_count"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id ";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ " AND enquiry_status_id = 1 ";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY stage_id ";
			// SOP("strsql in SalesPipeline------- -" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				salespipeline_data = "[";
				while (crs.next()) {
					// Str.append("{\"title\": \"").append(crs.getString("stage_name")).append("\", \"total\":").append(crs.getString("stage_count")).append("},");
					salespipeline_data = salespipeline_data + "{\"title\": \" " + crs.getString("stage_name") + "\", \"total\":\"" + crs.getString("stage_count") + "\"},";

				}
				salespipeline_data = salespipeline_data.substring(0, salespipeline_data.length() - 1);
				salespipeline_data = salespipeline_data + "]";
			} else {
				NoSalesPipeline = "No Open Enquiry!";
			}
			crs.close();
			// SOP("Sales PipeLine----" + salespipeline_data.toString());
			return salespipeline_data.toString();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String EnquiryPriorityStatus(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT priorityenquiry_id, priorityenquiry_name,"
					+ " priorityenquiry_color, COUNT(enquiry_id) AS enquirycount"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id";
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}
			StrSql += " WHERE 1=1"
					+ " AND enquiry_status_id = 1 ";

			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " GROUP BY priorityenquiry_id "
					+ " ORDER BY priorityenquiry_id";
			SOP("StrSql======FollowupEscStatus====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			String color = "";
			// [ "red", "yellow", "blue" ],
			enquiryprioritychart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					enquiryprioritychart_data = enquiryprioritychart_data
							+ "{\"title\": \"" + crs.getString("priorityenquiry_name") + "\","
							+ " \"value\":\"" + crs.getString("enquirycount") + "\","
							+ " \"color\":\"" + crs.getString("priorityenquiry_color") + "\","
							+ " \"dataurl\":\""
							+ "../sales/enquiry-list.jsp?smart=yes&priority="
							+ crs.getString("priorityenquiry_id")
							+ "\"}";
					// if(crs.getString("priorityenquiry_name").equals("Hot")){
					//
					// }
					enquiryprioritychart_data = enquiryprioritychart_data + ",";
				}
				enquiryprioritychart_data = enquiryprioritychart_data + "]";
			} else {
				no_enquirypriority = "No Status Found!";
			}
			crs.close();
			if (enquiryprioritychart_data.contains("},]")) {
				enquiryprioritychart_data = enquiryprioritychart_data.replace("},]", "}]");
			}
			// SOP("enquiryprioritychart_data===" + enquiryprioritychart_data);
			return enquiryprioritychart_data.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SalesDetails(String comp_id) {
		String new_value = "", old_value = "";
		try {
			CachedRowSet crs = null;

			StrSql = " SELECT "
					+ " COALESCE (SUM(IF (SUBSTR(enquiry_date, 1, 8) = " + curr_date + ", 1, 0)),0) AS enquirycountday,"
					+ " COALESCE (SUM(IF (SUBSTR(enquiry_date, 1, 6) = " + curr_month + ", 1, 0)), 0) AS enquirycountmonth,"
					+ " COALESCE (SUM(IF (SUBSTR(enquiry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(enquiry_date, 1, 6) <= " + endquarter + ", 1, 0)), 0) AS enquirycountquarter,"
					+ " COALESCE (SUM(IF (SUBSTR(enquiry_date, 1, 8) = " + back_date + ",	1, 0)), 0) AS enquirycountdayold,"
					+ " COALESCE (SUM(IF (SUBSTR(enquiry_date, 1, 6) = " + back_month + ",	1,	0	)),	0) AS enquirycountmonthold,"
					+ " COALESCE (SUM(IF (SUBSTR(enquiry_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(enquiry_date, 1, 6) <= " + back_endquarter + ", 1, 0)), 0) AS enquirycountquarterold,"
					+ " COALESCE (testdrivecountday, 0) AS testdrivecountday,"
					+ " COALESCE (testdrivecountmonth, 0) AS testdrivecountmonth,"
					+ " COALESCE (testdrivecountquarter, 0) AS testdrivecountquarter,"
					+ " COALESCE (testdrivecountdayold, 0) AS testdrivecountdayold,"
					+ " COALESCE (testdrivecountmonthold,	0) AS testdrivecountmonthold,"
					+ " COALESCE (testdrivecountquarterold,	0) AS testdrivecountquarterold,"

					+ " COALESCE (socountday, 0) AS socountday,"
					+ " COALESCE (socountmonth, 0) AS socountmonth,"
					+ " COALESCE (socountquarter, 0) AS socountquarter,"
					+ " COALESCE (socountdayold, 0) AS socountdayold,"
					+ " COALESCE (socountmonthold,	0) AS socountmonthold,"
					+ " COALESCE (socountquarterold,	0) AS socountquarterold,"

					+ " COALESCE (retailcountday, 0) AS retailcountday,"
					+ " COALESCE (retailcountmonth, 0) AS retailcountmonth,"
					+ " COALESCE (retailcountquarter, 0) AS retailcountquarter,"
					+ " COALESCE (retailcountdayold, 0) AS retailcountdayold,"
					+ " COALESCE (retailcountmonthold, 0) AS retailcountmonthold,"
					+ " COALESCE (retailcountquarterold, 0) AS retailcountquarterold,"

					+ " COALESCE (deliveredcountday, 0) AS deliveredcountday,"
					+ " COALESCE (deliveredcountmonth, 0) AS deliveredcountmonth,"
					+ " COALESCE (deliveredcountquarter, 0) AS deliveredcountquarter,"
					+ " COALESCE (deliveredcountdayold, 0) AS deliveredcountdayold,"
					+ " COALESCE (deliveredcountmonthold, 0) AS deliveredcountmonthold,"
					+ " COALESCE (deliveredcountquarterold, 0) AS deliveredcountquarterold,"

					+ " COALESCE (cancelledcountday, 0) AS cancelledcountday,"
					+ " COALESCE (cancelledcountmonth, 0) AS cancelledcountmonth,"
					+ " COALESCE (cancelledcountquarter, 0) AS cancelledcountquarter,"
					+ " COALESCE (cancelledcountdayold, 0) AS cancelledcountdayold,"
					+ " COALESCE (cancelledcountmonthold, 0) AS cancelledcountmonthold,"
					+ " COALESCE (cancelledcountquarterold, 0) AS cancelledcountquarterold "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id";
			}
			StrSql += " ,("
					+ " SELECT "
					+ " SUM(IF (SUBSTR(testdrive_fb_entry_date, 1, 8) = " + curr_date + ",	1,	0)) AS testdrivecountday,"
					+ " SUM(IF (SUBSTR(testdrive_fb_entry_date, 1, 6) = " + curr_month + ",	1,	0)) AS testdrivecountmonth,"
					+ " SUM(IF (SUBSTR(testdrive_fb_entry_date, 1, 6) >= " + startquarter + " AND SUBSTR(testdrive_fb_entry_date, 1, 6) <= " + endquarter + ", 1,	0)) AS testdrivecountquarter,"
					+ " SUM(IF (SUBSTR(testdrive_fb_entry_date, 1, 8) = " + back_date + ",	1, 0)) AS testdrivecountdayold,"
					+ " SUM(IF (SUBSTR(testdrive_fb_entry_date, 1, 6) = " + back_month + ",	1,	0)) AS testdrivecountmonthold,"
					+ " SUM(IF (SUBSTR(testdrive_fb_entry_date, 1, 6) >= " + back_startquarter + " AND SUBSTR(testdrive_fb_entry_date, 1, 6) <= " + back_endquarter
					+ ", 1, 0)) AS testdrivecountquarterold"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id ";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "testdrive_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS testdrive,"
					+ " ("
					+ " SELECT"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_entry_date, 1, 8) = " + curr_date + "), 1, 0)) AS socountday,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_entry_date, 1, 6) = " + curr_month + "), 1, 0)) AS socountmonth,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_entry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_entry_date, 1, 6) <= " + endquarter + "), 1, 0)) AS socountquarter,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_entry_date, 1, 8) = " + back_date + "), 1, 0)) AS socountdayold,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_entry_date, 1, 6) = " + back_month + "), 1, 0)) AS socountmonthold,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_entry_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(so_entry_date, 1, 6) <= " + back_endquarter + "), 1, 0)) AS socountquarterold, "
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_delivered_date, 1, 8) = " + curr_date + "), 1, 0)) AS deliveredcountday,"
					+ "	SUM(IF (so_active = 1 AND (SUBSTR(so_delivered_date, 1, 6) = " + curr_month + "), 1, 0)) AS deliveredcountmonth,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_delivered_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_delivered_date, 1, 6) <= " + endquarter + " ), 1, 0)) AS deliveredcountquarter,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_delivered_date, 1, 8) = " + back_date + "), 1, 0)) AS deliveredcountdayold,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_delivered_date, 1, 6) = " + back_month + "), 1, 0)) AS deliveredcountmonthold,"
					+ " SUM(IF (so_active = 1 AND (SUBSTR(so_delivered_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(so_delivered_date, 1, 6) <= " + back_endquarter + " ), 1, 0)) AS deliveredcountquarterold,"
					+ " SUM(IF (so_active = 0 AND (SUBSTR(so_cancel_date, 1, 8) = " + curr_date + "), 1, 0)) AS cancelledcountday,"
					+ " SUM(IF (so_active = 0 AND (SUBSTR(so_cancel_date, 1, 6) = " + curr_month + "), 1, 0)) AS cancelledcountmonth,"
					+ " SUM(IF (so_active = 0 AND (SUBSTR(so_cancel_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_cancel_date, 1, 6) <= " + endquarter + "), 1, 0)) AS cancelledcountquarter,"
					+ " SUM(IF (so_active = 0 AND (SUBSTR(so_cancel_date, 1, 8) = " + back_date + "), 1, 0)) AS cancelledcountdayold,"
					+ " SUM(IF (so_active = 0 AND (SUBSTR(so_cancel_date, 1, 6) = " + back_month + "), 1, 0)) AS cancelledcountmonthold,"
					+ " SUM(IF (so_active = 0 AND (SUBSTR(so_cancel_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(so_cancel_date, 1, 6) <= " + back_endquarter + " ), 1, 0)) AS cancelledcountquarterold,"
					+ " SUM(IF (so_active = 1 AND so_retail_date != '' AND (SUBSTR(so_retail_date, 1, 8) = " + curr_date + "), 1, 0)) AS retailcountday,"
					+ " SUM(IF (so_active = 1 AND so_retail_date != '' AND (SUBSTR(so_retail_date, 1, 6) = " + curr_month + "), 1, 0)) AS retailcountmonth,"
					+ " SUM(IF (so_active = 1 AND so_retail_date != '' AND SUBSTR(so_retail_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_retail_date, 1, 6) <= " + endquarter + ", 1, 0)) AS retailcountquarter,"
					+ " SUM(IF (so_active = 1 AND so_retail_date != '' AND (SUBSTR(so_retail_date, 1, 8) = " + back_date + "), 1, 0)) AS retailcountdayold,"
					+ " SUM(IF (so_active = 1 AND so_retail_date != '' AND (SUBSTR(so_retail_date, 1, 6) = " + back_month + "), 1, 0)) AS retailcountmonthold,"
					+ " SUM(IF (so_active = 1 AND so_retail_date != '' AND SUBSTR(so_retail_date, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(so_retail_date, 1, 6) <= " + back_endquarter + ", 1, 0)) AS retailcountquarterold"
					+ " FROM " + compdb(comp_id) + "axela_sales_so";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ BranchAccess.replace("branch_id", "so_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS so"
					+ " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}

			// SOP("StrSql--------SalesDetails------" + StrSql);

			crs = processQuery(StrSql, 0);
			while (crs.next()) {

				enquiry_countday = crs.getString("enquirycountday");
				enquiry_countmonth = crs.getString("enquirycountmonth");
				enquiry_countquarter = crs.getString("enquirycountquarter");

				testdrive_countday = crs.getString("testdrivecountday");
				testdrive_countmonth = crs.getString("testdrivecountmonth");
				testdrive_countquarter = crs.getString("testdrivecountquarter");

				retail_countday = crs.getString("retailcountday");
				retail_countmonth = crs.getString("retailcountmonth");
				retail_countquarter = crs.getString("retailcountquarter");

				so_countday = crs.getString("socountday");
				so_countmonth = crs.getString("socountmonth");
				so_countquarter = crs.getString("socountquarter");

				delivered_countday = crs.getString("deliveredcountday");
				delivered_countmonth = crs.getString("deliveredcountmonth");
				delivered_countquarter = crs.getString("deliveredcountquarter");

				cancelled_countday = crs.getString("cancelledcountday");
				cancelled_countmonth = crs.getString("cancelledcountmonth");
				cancelled_countquarter = crs.getString("cancelledcountquarter");

				new_value = enquiry_countday + "," + enquiry_countmonth + "," + enquiry_countquarter + ","
						+ testdrive_countday + "," + testdrive_countmonth + "," + testdrive_countquarter + ","
						+ so_countday + "," + so_countmonth + "," + so_countquarter + ","
						+ retail_countday + "," + retail_countmonth + "," + retail_countquarter + ","
						+ delivered_countday + "," + delivered_countmonth + "," + delivered_countquarter + ","
						+ cancelled_countday + "," + cancelled_countmonth + "," + cancelled_countquarter;

				// =====================================================================================================

				enquiry_countday_old = crs.getString("enquirycountdayold");
				enquiry_countmonth_old = crs.getString("enquirycountmonthold");
				enquiry_countquarter_old = crs.getString("enquirycountquarterold");

				testdrive_countday_old = crs.getString("testdrivecountdayold");
				testdrive_countmonth_old = crs.getString("testdrivecountmonthold");
				testdrive_countquarter_old = crs.getString("testdrivecountquarterold");

				retail_countday_old = crs.getString("retailcountdayold");
				retail_countmonth_old = crs.getString("retailcountmonthold");
				retail_countquarter_old = crs.getString("retailcountquarterold");

				so_countday_old = crs.getString("socountdayold");
				so_countmonth_old = crs.getString("socountmonthold");
				so_countquarter_old = crs.getString("socountquarterold");

				delivered_countday_old = crs.getString("deliveredcountdayold");
				delivered_countmonth_old = crs.getString("deliveredcountmonthold");
				delivered_countquarter_old = crs.getString("deliveredcountquarterold");

				cancelled_countday_old = crs.getString("cancelledcountdayold");
				cancelled_countmonth_old = crs.getString("cancelledcountmonthold");
				cancelled_countquarter_old = crs.getString("cancelledcountquarterold");

				old_value = enquiry_countday_old + "," + enquiry_countmonth_old + "," + enquiry_countquarter_old + ","
						+ testdrive_countday_old + "," + testdrive_countmonth_old + "," + testdrive_countquarter_old + ","
						+ so_countday_old + "," + so_countmonth_old + "," + so_countquarter_old + ","
						+ retail_countday_old + "," + retail_countmonth_old + "," + retail_countquarter_old + ","
						+ delivered_countday_old + "," + delivered_countmonth_old + "," + delivered_countquarter_old + ","
						+ cancelled_countday_old + "," + cancelled_countmonth_old + "," + cancelled_countquarter_old;

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return new_value + ":" + old_value;
	}
	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'  and emp_sales='1' "
					+ ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=selectbox onchange=\"DashBoard();\" >");
			Str.append("<option value=0 >All Executives</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
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

	public String PopulateMonth() {
		String year = "<select name=dr_month  id=dr_month class=selectbox onchange=\"DashBoard();\">";
		year = year + "";
		String currdate = new SimpleDateFormat("01/MM/yyyy").format(kknow());
		currdate = AddMonth(currdate, -6);
		for (int i = 0; i <= 12; i++) {
			String str = AddMonth(currdate, i);
			year = year + "<option value = " + str + "" + StrSelectdrop(str, startdate) + ">" + StrShorttoYearMonth(str) + "</option>\n";
		}
		year = year + "</select>";
		return year;
	}

	public String ListReports() {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 3 AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");

				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name")).append("</a></td>");
					Str.append("</tr>");
				}
				// Str.append("</thead>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void populateconfigdetails() {

		curr_date = curr_date.substring(0, 8);
		curr_month = ToShortDate(kknow()).substring(0, 6);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		back_month = ToLongDate(cal.getTime()).substring(0, 6);
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		back_date = ToLongDate(cal.getTime()).substring(0, 8);

		if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 1 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 3) {

			startquarter = ToShortDate(kknow()).substring(0, 4) + "01";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "03";

			cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "10";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "12";

		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 4 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 6) {

			startquarter = ToShortDate(kknow()).substring(0, 4) + "04";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "06";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "01";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "03";

		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 7 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 9) {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "07";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "09";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "04";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "06";
		} else {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "10";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "12";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "07";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "09";
		}
	}

	protected void CheckRedirect(String opt, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		period = PadQuotes(request.getParameter("period"));
		filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
		filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
		filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));

		if (!filter_brand_id.equals("")) {
			StrSearch = " AND branch_brand_id IN (" + filter_brand_id + ")";
		}
		if (!filter_region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + filter_region_id + ")";
		}
		if (!filter_branch_id.equals("")) {
			StrSearch += " AND branch_id IN (" + filter_branch_id + ")";
		}

		// enquiry
		if (opt.equals("enquiry") && period.equals("today")) {
			StrSearch += " AND SUBSTR(enquiry_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("enquiry") && period.equals("month")) {
			StrSearch += " AND SUBSTR(enquiry_date, 1, 6) = " + curr_month;
		} else if (opt.equals("enquiry") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(enquiry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(enquiry_date, 1, 6) <= " + endquarter;
		}

		// // TestDrive
		if (opt.equals("testdrive") && period.equals("today")) {
			StrSearch += " AND SUBSTR(testdrive_fb_entry_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("testdrive") && period.equals("month")) {
			StrSearch += " AND SUBSTR(testdrive_fb_entry_date, 1, 6) = " + curr_month;
		} else if (opt.equals("testdrive") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(testdrive_fb_entry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(testdrive_fb_entry_date, 1, 6) <= " + endquarter;
		}

		// // Sales Order
		if (opt.equals("so") && period.equals("today")) {
			StrSearch += " AND SUBSTR(so_entry_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("so") && period.equals("month")) {
			StrSearch += " AND SUBSTR(so_entry_date, 1, 6) = " + curr_month;
		} else if (opt.equals("so") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(so_entry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_entry_date, 1, 6) <= " + endquarter;
		}

		// // retail
		if (opt.equals("retail") && period.equals("today")) {
			StrSearch += " AND SUBSTR(so_retail_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("retail") && period.equals("month")) {
			StrSearch += " AND SUBSTR(so_retail_date, 1, 6) = " + curr_month;
		} else if (opt.equals("retail") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(so_retail_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_retail_date, 1, 6) <= " + endquarter;
		}

		// // Delivered
		if (opt.equals("delivered") && period.equals("today")) {
			StrSearch += " AND SUBSTR(so_delivered_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("delivered") && period.equals("month")) {
			StrSearch += " AND SUBSTR(so_delivered_date, 1, 6) = " + curr_month;
		} else if (opt.equals("delivered") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(so_delivered_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_delivered_date, 1, 6) <= " + endquarter;
		}

		// // Cancelled
		if (opt.equals("cancelled") && period.equals("today")) {
			StrSearch += " AND SUBSTR(so_cancel_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("cancelled") && period.equals("month")) {
			StrSearch += " AND SUBSTR(so_cancel_date, 1, 6) = " + curr_month;
		} else if (opt.equals("cancelled") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(so_cancel_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(so_cancel_date, 1, 6) <= " + endquarter;
		}

		if (opt.equals("so")) {
			StrSearch += " AND so_active = 1 ";
		} else if (opt.equals("delivered")) {
			StrSearch += " AND so_delivered_date != '' ";
		} else if (opt.equals("cancelled")) {
			StrSearch += " AND so_active = 0"
					+ " AND so_cancel_date != '' ";
		} else if (opt.equals("retail")) {
			StrSearch += " AND so_active = 1"
					+ " AND so_retail_date != '' ";
		}
		// SOP("StrSearch-----" + StrSearch);

		if (opt.equals("enquiry")) {
			SetSession("enquirystrsql", StrSearch, request);
			response.sendRedirect("../sales/enquiry-list.jsp?smart=yes");
			return;
		}

		if (opt.equals("testdrive")) {
			SetSession("testdrivestrsql", StrSearch, request);
			response.sendRedirect("../sales/testdrive-list.jsp?smart=yes");
			return;
		}

		if (opt.equals("so") || opt.equals("delivered") || opt.equals("cancelled") || opt.equals("retail")) {
			SetSession("sostrsql", StrSearch, request);
			response.sendRedirect("../sales/veh-salesorder-list.jsp?smart=yes");
			return;
		}

	}
}
