package axela.service;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Branch_Dashboard extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "", year = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, zone_ids, branch_ids, preownedmodel_ids, advisor_ids, tech_ids, jccat_ids, jctype_ids, dr_field_ids, field_ids;
	public String brand_id = "", region_id = "", zone_id, preownedmodel_id = "", advisor_id = "", tech_id = "", jccat_id = "", jctype_id = "", dr_field_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "branch_id", dr_orderby = "0", emp_active = "0", dr_field = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "", jc_cat_filter = "", jc_type_filter = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String jobcardemp = "", technicianemp = "", empfilter = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	static DecimalFormat deci = new DecimalFormat("0.00");
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				emp_id = CNumeric(GetSession("emp_id", request));
				// SOP("===="+getPercentage(100, 10));
				if (!header.equals("no")) {
					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				if (!header.equals("no")) {
					start_time = strToShortDate(ToShortDate(kknow()));
					end_time = strToShortDate(ToShortDate(kknow()));
					if (go.equals("Go")) {
						GetValues(request, response);
						CheckForm();
						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!zone_id.equals("")) {
							StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
						}
						if (!branch_id.equals("")) {
							StrSearch += " AND branch_id IN (" + branch_id + ")";
						}
						if (!preownedmodel_id.equals("")) {
							StrModel += " AND preownedmodel_id IN (" + preownedmodel_id + ")";
						}

						if (!jccat_id.equals("")) {
							jc_cat_filter = " AND jc_jccat_id IN (" + jccat_id + ")";
						}

						if (!jctype_id.equals("")) {
							jc_type_filter = " AND jc_jctype_id IN (" + jctype_id + ")";
						}

						if (!advisor_id.equals("") && !tech_id.equals("")) {
							empfilter = " AND adv.emp_id IN (" + advisor_id + ")"
									+ " AND tech.emp_id IN (" + tech_id + ")";
						}
						else if (!advisor_id.equals("") && tech_id.equals("")) {
							empfilter = " AND adv.emp_id IN (" + advisor_id + ")";
						}
						else if (advisor_id.equals("") && !tech_id.equals("")) {
							empfilter = " AND tech.emp_id IN (" + tech_id + ")";
						}

						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							if (dr_totalby.equals("jc_jccat_id") || dr_totalby.equals("jc_jctype_id")) {
								StrHTML = JCBranchDashBoard();
							} else {
								StrHTML = ListBranchDashBoard();
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (!endtime.equals("")) {
			year = endtime.substring(6, 10);
		}
		dr_totalby = PadQuotes(request.getParameter("dr_totalby"));
		if (dr_totalby.equals("")) {
			dr_totalby = "branch_id";
		}

		dr_orderby = PadQuotes(request.getParameter("dr_orderby"));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		preownedmodel_id = RetrunSelectArrVal(request, "dr_model_id");
		preownedmodel_ids = request.getParameterValues("dr_model_id");
		advisor_id = RetrunSelectArrVal(request, "dr_jc_emp_id");
		advisor_ids = request.getParameterValues("dr_jc_emp_id");
		tech_id = RetrunSelectArrVal(request, "dr_tech_emp_id");
		tech_ids = request.getParameterValues("dr_tech_emp_id");
		jccat_id = RetrunSelectArrVal(request, "dr_jccat_id");
		jccat_ids = request.getParameterValues("dr_jccat_id");
		jctype_id = RetrunSelectArrVal(request, "dr_jctype_id");
		jctype_ids = request.getParameterValues("dr_jctype_id");
		emp_active = PadQuotes(request.getParameter("chk_emp_active"));
		dr_field_ids = request.getParameterValues("dr_field");
		dr_field_id = RetrunSelectArrVal(request, "dr_field");
		field_ids = dr_field_id.split(",");

		if (emp_active.equals("on")) {
			emp_active = "1";
		} else {
			emp_active = "0";
		}
		// SOP("emp_active==" + request.getParameter("chk_emp_active"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		switch (dr_totalby) {
			case "branch_id" :
				dr_orderby = "branch_name";
				break;
			case "branch_region_id" :
				dr_orderby = "region_name";
				break;
			case "branch_zone_id" :
				dr_orderby = "zone_name";
				break;
			case "preownedmodel_id" :
				dr_orderby = "preownedmodel_name";
				break;
			case "branch_brand_id" :
				dr_orderby = "brand_name";
				break;
			case "jc_emp_id" :
				dr_orderby = "adv.emp_name";
				break;
			case "jc_technician_emp_id" :
				dr_orderby = "tech.emp_name";
				break;
			case "jc_jccat_id" :
				dr_orderby = "jccat_name";
				break;
			case "jc_jctype_id" :
				dr_orderby = "jctype_name";
				break;
			default :
				dr_orderby = "branch_name";
				break;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListBranchDashBoard() {
		try {
			int count = 0;
			StringBuilder strHead = new StringBuilder();
			// String brand_id = "", region_id = "", branch_id = "";
			// String brand_name = "", branch_name = "", region_name = "";

			StringBuilder Str = new StringBuilder();
			StringBuilder appendStr = new StringBuilder();
			appendStr.append("?starttime=" + starttime).append("&endtime=" + endtime).append("&emp_active=" + emp_active).append("&dr_year=" + year).append("&jccat_id=" + jccat_id)
					.append("&brand_id=" + brand_id).append("&region_id=" + region_id).append("&zone_id=" + zone_id).append("&dr_branch_id=" + dr_branch_id)
					.append("&preownedmodel_id=" + preownedmodel_id).append("&sa_id=" + advisor_id).append("&st_id=" + tech_id).append("&enquiry=");

			StrSql = "SELECT branch_id,branch_name,";

			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " brand_id, brand_name,";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " region_id, region_name,";
			}
			if (dr_totalby.equals("branch_zone_id")) {
				StrSql += " zone_id, zone_name,";
			}
			if (!advisor_id.equals("") || dr_totalby.equals("jc_emp_id")) {
				StrSql += " adv.emp_id, "
						+ " COALESCE (adv.emp_name,'') AS 'advisor',"
						+ " COALESCE (adv.emp_active,'') AS 'advemp_active',";
			}
			if (!tech_id.equals("") || dr_totalby.equals("jc_technician_emp_id")) {
				StrSql += " tech.emp_id, "
						+ " COALESCE (tech.emp_name,'') AS 'technician',"
						+ " COALESCE (tech.emp_active,'') AS 'techemp_active',";
			}
			if (!preownedmodel_id.equals("") || dr_totalby.equals("preownedmodel_id")) {
				StrSql += " preownedmodel_id,preownedmodel_name,";
			}
			if (dr_totalby.equals("jc_jccat_id")) {
				StrSql += " jc_jccat_id, jccat_name,";
			}
			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += " jc_jctype_id, jctype_name,";
			}

			StrSql += " COALESCE (service_target_jc_count,0) AS rocounttarget,"
					+ " COALESCE (rocountachived, 0) AS rocountachived,"
					+ " COALESCE (service_target_labour_amount, 0) AS labouramttarget,"
					+ " COALESCE (labouramtachived, 0) AS labouramtachived,"
					+ " COALESCE (otherpartsamtachived, 0) AS otherpartsamtachived,"
					+ " COALESCE (totalpartsamtachived, 0) AS totalpartsamtachived,"
					+ " COALESCE (service_target_vas_amount, 0) AS vasamttarget,"
					+ " COALESCE (vasamtachived, 0) AS vasamtachived,"
					+ " COALESCE (service_target_oil_amount, 0) AS oilamttarget,"
					+ " COALESCE (oilamtachived, 0) AS oilamtachived,"
					+ " COALESCE (service_target_break_count, 0) AS breakcounttarget,"
					+ " COALESCE (SUM(breakcountachived), 0) AS breakcountachived,"
					+ " COALESCE (service_target_break_amount, 0) AS breakamttarget,"
					+ " COALESCE (breakamtachived, 0) AS breakamtachived,"
					+ " COALESCE (service_target_tyre_count, 0) AS tyrecounttarget,"
					+ " COALESCE (SUM(tyrecountachived), 0) AS tyrecountachived,"
					+ " COALESCE (service_target_battery_count, 0) AS batterycounttarget,"
					+ " COALESCE (SUM(batterycountachived), 0) AS batterycountachived,"
					+ " COALESCE (service_target_battery_amount, 0) AS batteryamttarget,"
					+ " COALESCE (batteryamtachived, 0) AS batteryamtachived,"
					+ " COALESCE (service_target_extwarranty_count, 0) AS extwarrantycounttarget,"
					+ " COALESCE (extwarrentycountachived, 0) AS extwarrentycountachived,"
					+ " COALESCE (service_target_extwarranty_amount, 0) AS extwarrantyamttarget,"
					+ " COALESCE (extwarrantyqtyachived, 0) AS extwarrantyqtyachived,"
					+ " COALESCE (service_target_parts_amount, 0) AS partsamttarget,"
					+ " COALESCE (partsamtachived, 0) AS partsamtachived"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_branch"
					+ " LEFT JOIN (SELECT"
					+ " jc_branch_id,"
					+ " jc_emp_id,"
					+ " jc_technician_emp_id,"
					+ " jc_variant_id,"
					+ " jc_jccat_id,"
					+ " jc_jctype_id,"
					+ " COUNT(jc_id) AS rocountachived,"
					+ "  SUM(jc_bill_cash_labour+jc_bill_cash_labour_tyre"
					+ " +jc_bill_cash_labour_oil+ jc_bill_cash_labour_battery"
					+ " + jc_bill_cash_labour_brake+ jc_bill_cash_labour_accessories"
					+ " + jc_bill_cash_labour_valueadd+ jc_bill_cash_labour_extwarranty"
					+ " + jc_bill_cash_labour_wheelalign+ jc_bill_cash_labour_cng) AS labouramtachived,"
					+ " SUM(jc_bill_cash_parts + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng)AS otherpartsamtachived,"
					+ " SUM(jc_bill_cash_parts + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_tyre + jc_bill_cash_parts_brake + jc_bill_cash_parts_battery + jc_bill_cash_parts_oil + jc_bill_cash_parts_accessories) AS totalpartsamtachived, "
					+ " SUM(jc_bill_cash_parts_valueadd) AS vasamtachived,"
					+ " SUM(jc_bill_cash_parts_oil) AS oilamtachived,"
					+ " SUM(jc_bill_cash_parts_brake_qty) AS breakcountachived,"
					+ " SUM(jc_bill_cash_parts_brake) AS breakamtachived,"
					+ " SUM(jc_bill_cash_parts_tyre_qty) AS tyrecountachived,"
					+ " SUM(jc_bill_cash_parts_battery_qty) AS batterycountachived,"
					+ " SUM(jc_bill_cash_parts_battery) AS batteryamtachived,"
					+ " SUM(jc_bill_cash_parts_extwarranty) AS extwarrentycountachived,"
					+ " SUM(jc_bill_cash_parts_extwarranty_qty)AS extwarrantyqtyachived,"
					+ " SUM(jc_bill_cash_parts) AS partsamtachived "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE 1 =1"
					+ " AND SUBSTRING(jc_bill_cash_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTRING(jc_bill_cash_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id")
					+ " GROUP BY jc_branch_id"
					+ " ) AS tbljc ON tbljc.jc_branch_id = branch_id"
					+ " LEFT JOIN (SELECT"
					+ " service_target_emp_id,"
					+ " SUM(service_target_labour_amount) AS service_target_labour_amount,"
					+ " SUM(service_target_vas_amount) AS service_target_vas_amount,"
					+ " SUM(service_target_jc_count) AS service_target_jc_count,"
					+ " SUM(service_target_tyre_count) AS service_target_tyre_count,"
					+ " SUM(service_target_tyre_amount) AS service_target_tyre_amount,"
					+ " SUM(service_target_break_count) AS service_target_break_count,"
					+ " SUM(service_target_break_amount) AS service_target_break_amount,"
					+ " SUM(service_target_battery_count) AS service_target_battery_count,"
					+ " SUM(service_target_battery_amount) AS service_target_battery_amount,"
					+ " SUM(service_target_oil_amount) AS service_target_oil_amount,"
					+ " SUM(service_target_extwarranty_count) AS service_target_extwarranty_count,"
					+ " SUM(service_target_extwarranty_amount) AS service_target_extwarranty_amount,"
					+ " SUM(service_target_accessories_amount	) AS service_target_accessories_amount,"
					+ " SUM(service_target_parts_amount ) AS service_target_parts_amount"
					+ " FROM " + compdb(comp_id) + "axela_service_target"
					+ " WHERE 1 = 1"
					+ " AND SUBSTRING(service_target_startdate,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTRING(service_target_startdate,1,8) <= SUBSTR('" + endtime + "',1,8)"
					// + BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "service_target_emp_id")
					+ " GROUP BY service_target_emp_id"
					+ " ) AS tblst ON tblst.service_target_emp_id = tbljc.jc_emp_id";

			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			if (dr_totalby.equals("branch_zone_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_zone ON zone_id = branch_zone_id";
			}
			if (!advisor_id.equals("") || dr_totalby.equals("jc_emp_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp adv ON adv.emp_id = jc_emp_id";
				if (emp_active.equals("0")) {
					StrSql += " AND adv.emp_active = 1";
				}
			}
			if (!tech_id.equals("") || dr_totalby.equals("jc_technician_emp_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp tech ON tech.emp_id = jc_technician_emp_id";
				if (emp_active.equals("0")) {
					StrSql += " AND tech.emp_active = 1";
				}
			}
			if (!preownedmodel_id.equals("") || dr_totalby.equals("preownedmodel_id")) {
				StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
			}
			if (dr_totalby.equals("jc_jccat_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id";
			}
			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id";
			}
			StrSql += " WHERE 1 = 1"
					+ jc_type_filter
					+ jc_cat_filter
					+ StrModel
					+ StrSearch

					+ empfilter
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY " + dr_totalby
					+ " ORDER BY " + dr_orderby;
			if (emp_id.equals("1"))
				SOPInfo("StrSql==in branch-dash==" + StrSql);

			double total_target_tilldate = 0;
			double day = 0;
			double daycount = 0;
			String stdate, stmonth, styear;
			String title_head = "", id = "", total_by = "", branch_name = "";
			// SOP("starttime==" + SplitDate(starttime));
			if (!SplitDate(starttime).equals("01")) {
				stdate = SplitYear(starttime) + SplitMonth(starttime) + "01000000";
				// SOP("stdate==" + stdate);
				day = getDaysBetween(stdate, endtime) + 1;
			} else {
				day = getDaysBetween(starttime, endtime) + 1;
			}

			daycount = getDaysBetween(starttime, endtime)
					+ ((DaysInMonth(Integer.parseInt(SplitYear(endtime)), Integer.parseInt(SplitMonth(endtime))) - Integer.parseInt(SplitDate(endtime))))
					+ (Integer.parseInt(SplitDate(starttime)) - 1);
			// SOP("daycount==" + daycount + "  day===" + day);

			CachedRowSet crs = processQuery(StrSql);

			if (crs.isBeforeFirst()) {
				strHead.append("<div class=\" table-bordered\">\n");
				strHead.append("<table id='table' class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
				strHead.append("<thead style=\"margin-top:50px;\">");
				strHead.append("<tr>\n");
				strHead.append("<th data-toggle=\"true\">#</th>\n");
				while (crs.next()) {
					strHead.append("<th colspan=\"4\" align=left data-toggle=\"true\"><b>");
					// SOP("jc_technician_emp_id=-===" + dr_totalby);
					switch (dr_totalby) {
						case "branch_id" :
							branch_name = crs.getString("branch_name");
							if (branch_name.contains("-")) {
								strHead.append(branch_name.split("-")[1]);
							} else {
								strHead.append(branch_name);
							}
							id = branch_id;
							break;
						case "branch_region_id" :
							strHead.append(crs.getString("region_name"));
							id = region_id;
							break;
						case "branch_zone_id" :
							strHead.append(crs.getString("zone_name"));
							id = zone_id;
							break;
						case "preownedmodel_id" :
							strHead.append(crs.getString("preownedmodel_name"));
							id = preownedmodel_id;
							break;
						case "branch_brand_id" :
							strHead.append(crs.getString("brand_name"));
							id = brand_id;
							break;
						case "jc_emp_id" :
							if (crs.getString("advemp_active").equals("0")) {
								strHead.append(crs.getString("advisor") + " <font color=red><b>[Inactive]</b></font>");
							} else {
								strHead.append(crs.getString("advisor"));
							}
							id = emp_id;
							break;
						case "jc_technician_emp_id" :
							if (crs.getString("techemp_active").equals("0")) {
								strHead.append(crs.getString("technician") + " <font color=red><b>[Inactive]</b></font>");
							} else {
								strHead.append(crs.getString("technician"));
							}
							id = emp_id;
							break;
						case "jc_jccat_id" :
							strHead.append(crs.getString("jccat_name"));
							id = jccat_id;
							break;
						case "jc_jctype_id" :
							strHead.append(crs.getString("jctype_name"));
							id = jctype_id;
							break;

						default :
							branch_name = crs.getString("branch_name");
							if (branch_name.contains("-")) {
								strHead.append(branch_name.split("-")[1]);
							} else {
								strHead.append(branch_name);
							}
							id = branch_id;
							break;
					}
					strHead.append("</b></th>\n");
				}
				strHead.append("</tr>\n");

				strHead.append("<tr>\n");
				crs.beforeFirst();
				strHead.append("<th data-toggle=\"true\">");
				strHead.append("");
				strHead.append("</th>");
				while (crs.next()) {
					strHead.append("<th data-toggle=\"true\">");
					strHead.append("T");
					strHead.append("</th>");

					strHead.append("<th data-toggle=\"true\">");
					strHead.append("MTD");
					strHead.append("</th>");

					strHead.append("<th data-toggle=\"true\">");
					strHead.append("A");
					strHead.append("</th>");

					strHead.append("<th data-toggle=\"true\">");
					strHead.append("%");
					strHead.append("</th>");
				}

				strHead.append("<tr>\n");

				strHead.append("</thead>");
				strHead.append("<tbody>\n");

				for (int i = 0; i < field_ids.length; i++) {
					dr_field_id = field_ids[i];

					// 1st row starts
					if (dr_field_id.equals("") || dr_field_id.equals("1")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("RO").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("rocounttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(crs.getString("rocounttarget"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(deci.format(Math.round(total_target_tilldate)));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(crs.getString("rocountachived"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage(crs.getDouble("rocountachived"), crs.getDouble("rocounttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}
					// 1st row ends

					// 2nd row starts
					if (dr_field_id.equals("") || dr_field_id.equals("2")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Labour Amt").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("labouramttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("labouramttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("labouramtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("labouramtachived"), (double) crs.getDouble("labouramttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}
					// 2nd row ends

					// 3rd row starts
					if (dr_field_id.equals("") || dr_field_id.equals("3")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Other Parts Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {

							strHead.append("<td align=right>");
							strHead.append("");
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append("");
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("otherpartsamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append("");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 4th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("4")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Total Parts Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							strHead.append("<td align=right>");
							strHead.append("");
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append("");
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("totalpartsamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append("");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 5th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("5")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("VAS").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("vasamttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("vasamttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("vasamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("vasamtachived"), (double) crs.getDouble("vasamttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 6th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("6")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("OIL Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("oilamttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("oilamttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("oilamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("oilamtachived"), (double) crs.getDouble("oilamttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 7th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("7")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Brake Count").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("breakcounttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("breakcounttarget"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(deci.format(Math.round(total_target_tilldate)));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("breakcountachived"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("breakcountachived"), (double) crs.getDouble("breakcounttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 8th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("8")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Brake Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("breakamttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("breakamttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("breakamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("breakamtachived"), (double) crs.getDouble("breakamttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 9th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("9")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Tyre Count").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("tyrecounttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("tyrecounttarget"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(deci.format(Math.round(total_target_tilldate)));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("tyrecountachived"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("tyrecountachived"), (double) crs.getDouble("tyrecounttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 10th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("10")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Battery Count").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("batterycounttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("batterycounttarget"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(deci.format(Math.round(total_target_tilldate)));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("batterycountachived"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("batterycountachived"), (double) crs.getDouble("batterycounttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 11th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("11")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Battery Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("batteryamttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("batteryamttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("batteryamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("batteryamtachived"), (double) crs.getDouble("batteryamttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 12th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("12")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Ext. Warranty Count").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("extwarrantycounttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("extwarrantycounttarget"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(deci.format(Math.round(total_target_tilldate)));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(crs.getDouble("extwarrentycountachived"));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("extwarrentycountachived"), (double) crs.getDouble("extwarrantycounttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 13th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("13")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Ext. Warranty Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("extwarrantyamttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("extwarrantyamttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("extwarrantyqtyachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("extwarrantyqtyachived"), (double) crs.getDouble("extwarrantyamttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}

					// 14th row starts
					if (dr_field_id.equals("") || dr_field_id.equals("14")) {
						strHead.append("<tr>\n");
						strHead.append("<td align=left><b>").append("Parts Amount").append("</b></td>");
						crs.beforeFirst();
						while (crs.next()) {
							total_target_tilldate = (crs.getDouble("partsamttarget") / daycount) * day;

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("partsamttarget")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(total_target_tilldate));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Amount(crs.getDouble("partsamtachived")));
							strHead.append("</td>");

							strHead.append("<td align=right>");
							strHead.append(Double.parseDouble(getPercentage((double) crs.getDouble("partsamtachived"), (double) crs.getDouble("partsamttarget"))) + "%");
							strHead.append("</td>");
						}
						strHead.append("</tr>");
					}
				}

				strHead.append("</tbody>\n");
				strHead.append("</table>\n");
				strHead.append("</div>\n");
			} else {
				strHead.append("<font color=red><center><b>No Reports found!</b></center></font><br>");
			}
			crs.close();
			return strHead.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTotalBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=branch_brand_id").append(StrSelectdrop("branch_brand_id", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=branch_zone_id").append(StrSelectdrop("branch_zone_id", dr_totalby)).append(">Zone</option>\n");
		Str.append("<option value=branch_id").append(StrSelectdrop("branch_id", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=preownedmodel_id").append(StrSelectdrop("preownedmodel_id", dr_totalby)).append(">Models</option>\n");
		Str.append("<option value=jc_emp_id").append(StrSelectdrop("jc_emp_id", dr_totalby)).append(">Service Advisor</option>\n");
		Str.append("<option value=jc_technician_emp_id").append(StrSelectdrop("jc_technician_emp_id", dr_totalby)).append(">Service Technician</option>\n");
		Str.append("<option value=jc_jccat_id").append(StrSelectdrop("jc_jccat_id", dr_totalby)).append(">Job Card Category</option>\n");
		Str.append("<option value=jc_jctype_id").append(StrSelectdrop("jc_jctype_id", dr_totalby)).append(">Job Card Type</option>\n");

		return Str.toString();
	}

	public String PopulateFields(String[] dr_field_ids) {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name=\"dr_field\" id=\"dr_field\" class='form-control multiselect-dropdown' multiple=multiple size=10>");
		Str.append("<option value=1").append(ArrSelectdrop(1, dr_field_ids)).append(">RO</option>\n");
		Str.append("<option value=2").append(ArrSelectdrop(2, dr_field_ids)).append(">Labour Amount</option>\n");
		Str.append("<option value=3").append(ArrSelectdrop(3, dr_field_ids)).append(">Other Parts Amount</option>\n");
		Str.append("<option value=4").append(ArrSelectdrop(4, dr_field_ids)).append(">Total Parts Amount</option>\n");
		Str.append("<option value=5").append(ArrSelectdrop(5, dr_field_ids)).append(">VAS</option>\n");
		Str.append("<option value=6").append(ArrSelectdrop(6, dr_field_ids)).append(">Oil Amount</option>\n");
		Str.append("<option value=7").append(ArrSelectdrop(7, dr_field_ids)).append(">Brake Count</option>\n");
		Str.append("<option value=8").append(ArrSelectdrop(8, dr_field_ids)).append(">Brake Amount</option>\n");
		Str.append("<option value=9").append(ArrSelectdrop(9, dr_field_ids)).append(">Tyre Count</option>\n");
		Str.append("<option value=10").append(ArrSelectdrop(10, dr_field_ids)).append(">Battery Count</option>\n");
		Str.append("<option value=11").append(ArrSelectdrop(11, dr_field_ids)).append(">Battery Amount</option>\n");
		Str.append("<option value=12").append(ArrSelectdrop(12, dr_field_ids)).append(">Ext. Warranty Count</option>\n");
		Str.append("<option value=13").append(ArrSelectdrop(13, dr_field_ids)).append(">Ext. Warranty Amount</option>\n");
		Str.append("<option value=14").append(ArrSelectdrop(14, dr_field_ids)).append(">Parts Amount</option>\n");
		Str.append("</select>");

		return Str.toString();
	}

	public String PopulateOrderBy(String comp_id, String dr_orderby) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=''>Select</option>");
		Str.append("<option value=labouramount").append(StrSelectdrop("labouramount", dr_orderby)).append(">Labour Target</option>\n");
		Str.append("<option value=billlabour").append(StrSelectdrop("billlabour", dr_orderby)).append(">Labour Achived</option>\n");
		Str.append("<option value=vasamount").append(StrSelectdrop("vasamount", dr_orderby)).append(">VAS Target</option>\n");
		Str.append("<option value=billvalueadd").append(StrSelectdrop("billvalueadd", dr_orderby)).append(">VAS Achived</option>\n");
		Str.append("<option value=jccount").append(StrSelectdrop("jccount", dr_orderby)).append(">RO Target</option>\n");
		Str.append("<option value=roachived").append(StrSelectdrop("roachived", dr_orderby)).append(">RO Achived</option>\n");
		Str.append("<option value=tyreamount").append(StrSelectdrop("tyreamount", dr_orderby)).append(">Tyre Target</option>\n");
		Str.append("<option value=billtyre").append(StrSelectdrop("billtyre", dr_orderby)).append(">Tyre Achived</option>\n");
		Str.append("<option value=breakamount").append(StrSelectdrop("breakamount", dr_orderby)).append(">Brake Target</option>\n");
		Str.append("<option value=billbreak").append(StrSelectdrop("billbreak", dr_orderby)).append(">Brake Achived</option>\n");
		Str.append("<option value=batteryamount").append(StrSelectdrop("batteryamount", dr_orderby)).append(">Battery Target</option>\n");
		Str.append("<option value=billbattery").append(StrSelectdrop("billbattery", dr_orderby)).append(">Battery Achived</option>\n");
		Str.append("<option value=oilamount").append(StrSelectdrop("oilamount", dr_orderby)).append(">Oil Target</option>\n");
		Str.append("<option value=billoil").append(StrSelectdrop("billoil", dr_orderby)).append(">Oil Achived</option>\n");
		Str.append("<option value=accessoriesamount").append(StrSelectdrop("accessoriesamount", dr_orderby)).append(">Accessories Target</option>\n");
		Str.append("<option value=billaccessories").append(StrSelectdrop("billaccessories", dr_orderby)).append(">Accessories Achived</option>\n");

		return Str.toString();
	}

	public String Amount(double value) {
		String amount = "";
		// if (value > 999999) {
		amount = deci.format((value / 100000)) + "L";
		// }
		// else {
		// // amount = deci.format(value);
		// amount = unescapehtml(IndDecimalFormat(deci.format((value))));
		// }

		return amount;

	}

	public String JCBranchDashBoard() {
		try {
			StringBuilder Str = new StringBuilder();
			String jccatname = "", jctypename = "", fieldname = "";
			int branchCount = 0, total = 0, grandtotal = 0;
			HashMap<String, Integer> jccat_name = new LinkedHashMap<String, Integer>();
			HashMap<String, Integer> jctype_name = new LinkedHashMap<String, Integer>();
			HashMap<Integer, Integer> branchTotal = new LinkedHashMap<Integer, Integer>();

			StrSql = "SELECT jccat_id, jccat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
					+ " WHERE 1 = 1";
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}

			if (!jccat_id.equals("")) {
				StrSql += " AND jccat_id IN (" + jccat_id + ")";
			}
			StrSql += " GROUP BY jccat_id"
					+ " ORDER BY jccat_name";
			// SOP("StrSql====" + StrSql);
			ResultSet rsjccat = processQuery(StrSql, 0);
			while (rsjccat.next()) {
				jccatname += " COALESCE (SUM(IF(jccat_name = '" + rsjccat.getString("jccat_name")
						+ "', 1, 0)), 0) AS '" + rsjccat.getString("jccat_name") + "', ";

				jccat_name.put(rsjccat.getString("jccat_name"), 0);
			}
			jccatname = jccatname.substring(0, jccatname.length() - 1);
			// SOP("jccatname==" + jccatname);

			StrSql = "SELECT jctype_id, jctype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
					+ " WHERE 1 = 1";
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!jctype_id.equals("")) {
				StrSql += " AND jctype_id IN (" + jctype_id + ")";
			}
			StrSql += " GROUP BY jctype_id "
					+ " ORDER BY jctype_name";

			ResultSet rsjctype = processQuery(StrSql, 0);
			while (rsjctype.next()) {
				jctypename += " COALESCE (SUM(IF(jctype_name = '" + rsjctype.getString("jctype_name")
						+ "', 1, 0)), 0) AS '" + rsjctype.getString("jctype_name") + "', ";

				jctype_name.put(rsjctype.getString("jctype_name"), 0);
			}
			jctypename = jctypename.substring(0, jctypename.length() - 1);

			StrSql = " SELECT branch_name AS 'Branch',";
			if (dr_totalby.equals("jc_jccat_id")) {
				StrSql += jccatname.substring(0, jccatname.length() - 1);
			}
			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += jctypename.substring(0, jctypename.length() - 1);
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_service_jc ";
			if (dr_totalby.equals("jc_jccat_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id ";
			}

			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id ";
			}

			if (!advisor_id.equals("") || dr_totalby.equals("jc_emp_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp adv ON adv.emp_id = jc_emp_id";
				if (emp_active.equals("0")) {
					StrSql += " AND adv.emp_active = 1";
				}
			}
			if (!tech_id.equals("") || dr_totalby.equals("jc_technician_emp_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp tech ON tech.emp_id = jc_technician_emp_id";
				if (emp_active.equals("0")) {
					StrSql += " AND tech.emp_active = 1";
				}
			}

			if (!preownedmodel_id.equals("")) {
				StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id";
			}
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
					+ " WHERE 1 = 1 "
					+ " AND branch_branchtype_id = 3"
					+ " AND SUBSTRING(jc_bill_cash_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTRING(jc_bill_cash_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
					+ StrModel
					+ StrSearch
					+ BranchAccess
					+ ExeAccess
					+ empfilter;

			if (dr_totalby.equals("jc_jccat_id")) {
				StrSql += " GROUP BY branch_id "
						+ " ORDER BY jccat_name";
			}

			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += " GROUP BY branch_id "
						+ " ORDER BY jctype_name ";
			}

			// SOP("StrSql==in jcbranch-dash==" + StrSql);

			CachedRowSet crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {

				Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");

				if (dr_totalby.equals("jc_jccat_id")) {
					Str.append("<th data-toggle=\"true\">").append("JC Category").append("</th>");
				}

				if (dr_totalby.equals("jc_jctype_id")) {
					Str.append("<th data-toggle=\"true\">").append("JC Type").append("</th>");
				}

				while (crs.next()) {
					branchCount++;
					Str.append("<th data-toggle=\"true\">");
					if (crs.getString("branch_name").contains("-")) {
						Str.append(crs.getString("branch_name").split("-")[1]);
					} else {
						Str.append(crs.getString("branch_name"));
					}
					Str.append("</th>");
				}
				Str.append("<th data-toggle=\"true\">").append("Total").append("</th>");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				// crs.beforeFirst();

				RowSetMetaData rsmd = (RowSetMetaData) crs.getMetaData();
				int numberOfColumns = rsmd.getColumnCount();

				for (int i = 2; i <= numberOfColumns; i++) {

					Str.append("<tr><td><b>").append(rsmd.getColumnLabel(i)).append("</b></td>");
					fieldname = rsmd.getColumnLabel(i);
					crs.first();

					for (int j = 1; j <= branchCount; j++) {
						total += Integer.parseInt(crs.getString(fieldname));
						Str.append("<td align=right>").append(crs.getString(fieldname)).append("</td>");

						if (branchTotal.get(j) != null) {
							branchTotal.put(j, (branchTotal.get(j) + crs.getInt(fieldname)));
						} else {
							branchTotal.put(j, crs.getInt(fieldname));
						}
						crs.next();
					}

					Str.append("<td align=right><b>").append(total).append("</b></td>");
					Str.append("</tr>");
					total = 0;
				}
				Str.append("<tr><td><b>").append("Total").append("</b></td>");
				for (int j = 1; j <= branchCount; j++) {
					Str.append("<td align=right><b>").append(branchTotal.get(j)).append("</b></td>");
					grandtotal += branchTotal.get(j);
				}
				Str.append("<td align=right><b>").append(grandtotal).append("</b></td>");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>");

			} else {
				Str.append("<font color=red><center><b>No Reports found!</b></center></font><br>");
			}
			rsjccat.close();
			rsjctype.close();
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
