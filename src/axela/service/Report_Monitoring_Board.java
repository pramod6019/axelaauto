package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Monitoring_Board extends Connect {

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
	public String dr_totalby = "", dr_orderby = "0", emp_active = "0", dr_warranty = "";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "", jc_cat_filter = "", jc_type_filter = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "", field_id = "0";
	public String jobcardemp = "", technicianemp = "", empmainjoin = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-service-monitoring-board-search.jsp";
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
				if (field_id.equals("0")) {
					dr_field_id = "RO,Labour,Parts";
					dr_field_ids = dr_field_id.split(",");
					PopulateFields(dr_field_ids);
					PopulateOrderBy(comp_id, dr_field_id);
				}
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
						// SOP("Start D===" + starttime.substring(0, 6) + "01");
						// SOP("End D====" + endtime.substring(0, 6) + "31");
						// BranchAccess = BranchAccess;
						// .replace("branch_id", "jc_branch_id");
						// ExeAccess += ExeAccess;
						// for (int i = 0; i <= dr_field_ids.length - 1; i++) {
						// SOP("dr_field_ids=====" + dr_field_ids[i]);
						// }
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

						if (!advisor_id.equals("")) {
							jobcardemp = " AND jcmodel.jc_emp_id IN (" + advisor_id + ")";
						}
						if (!tech_id.equals("")) {
							technicianemp = " AND jcmodel.jc_technician_emp_id IN (" + tech_id + ")";
						}
						if (!advisor_id.equals("") && !tech_id.equals("")) {
							empmainjoin = " AND emp_id IN (" + advisor_id + "," + tech_id + ")";
						}
						else if (!advisor_id.equals("") && tech_id.equals("")) {
							empmainjoin = " AND emp_id IN (" + advisor_id + ")";
						}
						else if (advisor_id.equals("") && !tech_id.equals("")) {
							empmainjoin = " AND emp_id IN (" + tech_id + ")";
						}
						if (advisor_id.equals("") && !tech_id.equals("") && !dr_totalby.equals("jc_technician_emp_id")) {
							empmainjoin = "";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							// StrHTML = ListServiceMonitoringBoard();
							StrHTML = ListServiceDemo();
						}
					}
				}
			}

			// SOP("StrSearch_----------" + StrSearch);
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
			dr_totalby = "jc_emp_id";
		}
		SOP("dr_totalby==" + dr_totalby);
		dr_warranty = PadQuotes(request.getParameter("dr_warranty"));
		if (dr_warranty.equals("0")) {
			dr_warranty = "-1";
		} else if (dr_warranty.equals("2")) {
			dr_warranty = "0";
		} else if (dr_warranty.equals("1")) {
			dr_warranty = "1";
		}
		field_id = "1";
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
		dr_field_id = PadQuotes(RetrunSelectArrVal(request, "dr_field"));
		field_ids = dr_field_id.split(",");
		if (dr_field_id.equals("")) {
			dr_field_id = "RO,Labour,Parts";
		}
		SOP("dr_field_id====" + dr_field_id);
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

		// SOP("brand_id==" + brand_id);
		// SOP("region_id==" + region_id);
		// SOP("zone_id==" + zone_id);
		// SOP("branch_id==" + branch_id);
		// SOP("preownedmodel_id==" + preownedmodel_id);
		// SOP("advisor_id==" + advisor_id);
		// SOP("tech_id==" + tech_id);
		// SOP("jccat_id==" + jccat_id);

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

	public String ListServiceDemo() {

		try {
			int count = 0;
			// String brand_id = "", region_id = "", branch_id = "";
			// String brand_name = "", branch_name = "", region_name = "";
			double labour_target = 0;
			double labour_achv = 0;

			double parts_target = 0;
			double total_parts_target = 0;

			double vas_target = 0;
			double vas_achv = 0;
			double vas_labour_achv = 0;

			int ro_target = 0;
			int ro_achv = 0;

			double tyre_target_count = 0;
			double tyre_target = 0;
			double tyre_achv = 0;
			double tyre_qty = 0;

			double break_count_target = 0;
			double break_count_achv = 0;

			double break_target = 0;
			double break_achv = 0;

			double battery_target_count = 0;
			double battery_target = 0;
			double battery_achv = 0;
			double battery_qty = 0;

			double oil_target = 0;
			double oil_achv = 0;

			double access_target = 0;
			double access_achv = 0;

			double extwarranty_target_count = 0;
			double extwarranty_target = 0;
			double extwarranty_achv = 0;
			double extwarranty_qty = 0;

			double otherparts = 0;
			double totalparts = 0;

			double total_labour_target = 0.0;
			double total_labour_achv = 0.0;

			double total_vas_target = 0.0;
			double total_vas_achv = 0.0;
			double total_vas_labour_achv = 0.0;

			int total_ro_target = 0;
			int total_ro_achv = 0;

			double total_tyre_target_count = 0.0;
			double total_tyre_target = 0.0;
			double total_tyre_achv = 0.0;
			double total_tyre_qty = 0;

			double total_break_count_target = 0;
			double total_break_count_achv = 0;

			double total_break_target_count = 0.0;
			double total_break_qty = 0.0;

			double total_break_target = 0.0;
			double total_break_achv = 0.0;

			double total_battery_target_count = 0.0;
			double total_battery_target = 0.0;
			double total_battery_achv = 0.0;
			double total_battery_qty = 0.0;

			double total_oil_target = 0.0;
			double total_oil_achv = 0.0;

			double total_access_target = 0.0;
			double total_access_achv = 0.0;

			double total_extwarranty_target_count = 0.0;
			double total_extwarranty_target = 0.0;
			double total_extwarranty_achv = 0.0;
			double total_extwarranty_qty = 0.0;

			double totalotherparts = 0.0;
			double totalpartsamount = 0.0;

			StringBuilder Str = new StringBuilder();
			StringBuilder appendStr = new StringBuilder();
			appendStr.append(SearchURL)
					.append("?starttime=" + starttime).append("&dr_warranty=" + dr_warranty).append("&endtime=" + endtime).append("&emp_active=" + emp_active).append("&dr_year=" + year)
					.append("&jccat_id=" + jccat_id).append("&jctype_id=" + jctype_id)
					.append("&brand_id=" + brand_id).append("&region_id=" + region_id).append("&zone_id=" + zone_id).append("&dr_branch_id=" + dr_branch_id)
					.append("&preownedmodel_id=" + preownedmodel_id).append("&sa_id=" + advisor_id).append("&st_id=" + tech_id).append("&enquiry=");
			StrSql = " SELECT"
					+ " emp_id, emp_active, branch_brand_id, branch_id, branch_region_id, branch_zone_id, "
					+ " COALESCE (preownedmodel_id,0) AS preownedmodel_id, ";

			if (dr_totalby.equals("emp_branch_id")) {
				StrSql += " branch_name,";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " region_name,";
			}
			if (dr_totalby.equals("branch_zone_id")) {
				StrSql += " zone_name,";
			}
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " brand_name,";
			}
			if (dr_totalby.equals("preownedmodel_id")) {
				StrSql += " COALESCE (preownedmodel_name,'') AS preownedmodel_name,";
			}

			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += " COALESCE (jctype_name, 'Others') AS 'jctype_name',"
						+ " COALESCE (jc_jctype_id, 0) AS 'jc_jctype_id',";
			}

			StrSql += " COALESCE (SUM(service_target_emp_id), 0) AS service_target_emp_id,"
					+ " COALESCE (jc_jccat_id, 0) AS 'jc_jccat_id',"
					+ " COALESCE (jccat_name, 'Others') AS 'jccat_name',";

			// ro
			if (dr_field_id.contains("RO")) {
				StrSql += " COALESCE (SUM(service_target_jc_count), 0) AS 'jccount',"
						+ " COALESCE (SUM(jc_id), 0) AS 'roachived',";
			}

			// labour
			if (dr_field_id.contains("Labour")) {
				StrSql += " COALESCE (SUM(service_target_labour_amount),0) AS 'labouramount',"
						+ " COALESCE (SUM(jc_bill_cash_labour"
						+ " + jc_bill_cash_labour_tyre"
						+ " + jc_bill_cash_labour_oil"
						+ " + jc_bill_cash_labour_battery"
						+ " + jc_bill_cash_labour_brake"
						+ " + jc_bill_cash_labour_accessories"
						+ " + jc_bill_cash_labour_extwarranty"
						+ " + jc_bill_cash_labour_wheelalign"
						+ " + jc_bill_cash_labour_cng), 0) AS 'billlabour',";
			}

			// parts
			if (dr_field_id.contains("Parts")) {
				StrSql += "	COALESCE (SUM(jc_bill_cash_parts), 0) AS jc_bill_cash_parts,"
						+ " COALESCE (SUM(jc_bill_cash_parts_wheelalign), 0) AS jc_bill_cash_parts_wheelalign,"
						+ " COALESCE (SUM(jc_bill_cash_parts_cng), 0) AS jc_bill_cash_parts_cng,"
						+ " COALESCE (SUM(service_target_parts_amount), 0) AS 'service_target_parts_amount',";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("VAS")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_parts_valueadd), 0) AS 'billvalueadd',";
			}
			if (dr_field_id.contains("Parts") || dr_field_id.contains("Tyre")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_parts_tyre), 0) AS 'billtyre',";
			}
			if (dr_field_id.contains("Parts") || dr_field_id.contains("Brake")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_parts_brake), 0) AS 'billbreak',";
			}
			if (dr_field_id.contains("Parts") || dr_field_id.contains("Battery")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_parts_battery), 0) AS 'billbattery',";
			}
			if (dr_field_id.contains("Parts") || dr_field_id.contains("Oil")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_parts_oil), 0) AS 'billoil',";
			}
			if (dr_field_id.contains("Parts") || dr_field_id.contains("Accessories")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_parts_accessories), 0) AS 'billaccessories',";
			}

			// vas
			if (dr_field_id.contains("VAS")) {
				StrSql += " COALESCE (SUM(jc_bill_cash_labour_valueadd), 0) AS billlabourvalueadd,"
						+ " COALESCE (SUM(service_target_vas_amount),0) AS 'vasamount',";
			}

			// oil
			if (dr_field_id.contains("Oil")) {
				StrSql += " COALESCE (SUM(service_target_oil_amount),0) AS 'oilamount',";
			}

			// break
			if (dr_field_id.contains("Brake")) {
				StrSql += " COALESCE (SUM(service_target_break_count), 0) AS 'breakcount',"
						+ " COALESCE (SUM(jc_bill_cash_parts_brake_qty), 0) AS 'billbreakqty',"
						+ " COALESCE (SUM(service_target_break_amount),0) AS 'breakamount',";
			}

			// tyre
			if (dr_field_id.contains("Tyre")) {
				StrSql += " COALESCE (SUM(service_target_tyre_count),0) AS 'tyrecount',"
						+ " COALESCE (SUM(service_target_tyre_amount),0) AS 'tyreamount',"
						+ " COALESCE (SUM(jc_bill_cash_parts_tyre_qty), 0) AS 'billtyreqty',";
			}

			// battery
			if (dr_field_id.contains("Battery")) {
				StrSql += " COALESCE (SUM(service_target_battery_count),0) AS 'batterycount',"
						+ " COALESCE (SUM(service_target_battery_amount),0) AS 'batteryamount',"
						+ " COALESCE (SUM(jc_bill_cash_parts_battery_qty), 0) AS 'billbatteryqty',";
			}

			// accessories
			if (dr_field_id.contains("Accessories")) {
				StrSql += " COALESCE (SUM(service_target_accessories_amount),0) AS 'accessoriesamount',";
			}

			// ext
			if (dr_field_id.contains("Ext")) {
				StrSql += " COALESCE (SUM(service_target_extwarranty_count),0) AS 'extwarrantycount',"
						+ " COALESCE (SUM(service_target_extwarranty_amount),0) AS 'extwarrantyamount',"
						+ " COALESCE (SUM(jc_bill_cash_parts_extwarranty_qty), 0) AS 'billextwarrantyqty',"
						+ " COALESCE (SUM(jc_bill_cash_parts_extwarranty), 0) AS 'billextwarranty',";
			}

			StrSql += " COALESCE (emp_name,'') AS 'emp_name'"
					// Main table
					+ " FROM " + compdb(comp_id) + "axela_emp";
			// if (!brand_id.equals("") || !branch_id.equals("") || !region_id.equals("") || dr_totalby.equals("emp_branch_id")
			// || dr_totalby.equals("branch_region_id") || dr_totalby.equals("branch_brand_id")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
			// }
			// Region

			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			// Zone
			if (dr_totalby.equals("branch_zone_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_zone ON zone_id = branch_zone_id";
			}
			// Brand
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
			}
			// for jc details
			StrSql += " LEFT JOIN (SELECT"
					+ " COUNT(jc_id) AS jc_id,"
					+ " jc_emp_id,"
					+ " jc_technician_emp_id,"
					+ " jc_branch_id,"
					+ " jc_jccat_id,"
					+ " jccat_name,"
					+ " jc_jctype_id,"
					+ " jctype_name,"
					+ " preownedmodel_id,"
					+ " preownedmodel_name,";

			// Labour
			if (dr_field_id.contains("Labour")) {
				StrSql += " SUM(jc_bill_cash_labour) AS jc_bill_cash_labour,"
						+ " SUM(jc_bill_cash_labour_tyre) AS jc_bill_cash_labour_tyre,"
						+ " SUM(jc_bill_cash_labour_oil) AS jc_bill_cash_labour_oil,"
						+ " SUM(jc_bill_cash_labour_battery) AS jc_bill_cash_labour_battery,"
						+ " SUM(jc_bill_cash_labour_brake) AS jc_bill_cash_labour_brake,"
						+ " SUM(jc_bill_cash_labour_accessories) AS jc_bill_cash_labour_accessories,"

						+ " SUM(jc_bill_cash_labour_extwarranty) AS jc_bill_cash_labour_extwarranty,"
						+ " SUM(jc_bill_cash_labour_wheelalign) AS jc_bill_cash_labour_wheelalign,"
						+ " SUM(jc_bill_cash_labour_cng) AS jc_bill_cash_labour_cng,";
			}

			// parts
			if (dr_field_id.contains("Parts")) {
				StrSql += " SUM(jc_bill_cash_parts) AS jc_bill_cash_parts,"
						+ " SUM(jc_bill_cash_parts_wheelalign) AS jc_bill_cash_parts_wheelalign,"
						+ " SUM(jc_bill_cash_parts_cng) AS jc_bill_cash_parts_cng,";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("VAS")) {
				StrSql += " SUM(jc_bill_cash_parts_valueadd) AS jc_bill_cash_parts_valueadd,";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("Tyre")) {
				StrSql += " SUM(jc_bill_cash_parts_tyre) AS jc_bill_cash_parts_tyre,"
						+ " SUM(jc_bill_cash_parts_tyre_qty) AS jc_bill_cash_parts_tyre_qty,";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("Brake")) {
				StrSql += " SUM(jc_bill_cash_parts_brake) AS jc_bill_cash_parts_brake,"
						+ " SUM(jc_bill_cash_parts_brake_qty) AS jc_bill_cash_parts_brake_qty,";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("Battery")) {
				StrSql += " SUM(jc_bill_cash_parts_battery) AS jc_bill_cash_parts_battery,"
						+ " SUM(jc_bill_cash_parts_battery_qty) AS jc_bill_cash_parts_battery_qty,";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("Oil")) {
				StrSql += " SUM(jc_bill_cash_parts_oil) AS jc_bill_cash_parts_oil,";
			}

			if (dr_field_id.contains("Parts") || dr_field_id.contains("Accessories")) {
				StrSql += " SUM(jc_bill_cash_parts_accessories) AS jc_bill_cash_parts_accessories,";
			}

			if (dr_field_id.contains("VAS")) {
				StrSql += " SUM(jc_bill_cash_labour_valueadd) AS jc_bill_cash_labour_valueadd,";
			}

			// ext
			if (dr_field_id.contains("Ext")) {
				StrSql += " SUM(jc_bill_cash_parts_extwarranty) AS jc_bill_cash_parts_extwarranty,"
						+ " SUM(jc_bill_cash_parts_extwarranty_qty) AS jc_bill_cash_parts_extwarranty_qty,";
			}

			StrSql += " jc_bill_cash_date"
					+ " FROM " + compdb(comp_id) + "axela_service_jc "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id";

			StrSql += " WHERE 1 = 1";
			if (!dr_warranty.equals("-1")) {
				StrSql += " AND jc_warranty ='" + dr_warranty + "'";
			}

			StrSql += jc_cat_filter
					+ jc_type_filter
					+ StrModel;

			if (dr_totalby.equals("jc_emp_id")) {
				StrSql += technicianemp;
			}
			StrSql += " AND SUBSTRING(jc_bill_cash_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTRING(jc_bill_cash_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
					+ " GROUP BY";
			if (dr_totalby.equals("preownedmodel_id")) {
				StrSql += " preownedmodel_id,";
			}
			if (dr_totalby.equals("jc_jccat_id")) {
				StrSql += " jc_jccat_id,";
			}
			if (dr_totalby.equals("jc_jctype_id")) {
				StrSql += " jc_jctype_id,";
			}
			if (dr_totalby.equals("jc_technician_emp_id")) {
				StrSql += " jc_technician_emp_id";
			}
			else {
				StrSql += " jc_emp_id ";
			}
			StrSql += " ) AS tbljc ON ";
			if (dr_totalby.equals("jc_technician_emp_id")) {
				StrSql += "jc_technician_emp_id";
			}
			else {
				StrSql += "jc_emp_id ";
			}
			StrSql += " = emp_id"
					// for target details
					+ " LEFT JOIN (SELECT";
			// ro
			if (dr_field_id.contains("RO")) {
				StrSql += " SUM(service_target_jc_count)AS service_target_jc_count,";
			}

			// labour
			if (dr_field_id.contains("Labour")) {
				StrSql += " SUM(service_target_labour_amount)AS service_target_labour_amount,";
			}
			// parts
			if (dr_field_id.contains("Parts")) {
				StrSql += " SUM(service_target_parts_amount) AS service_target_parts_amount,";
			}

			// vas
			if (dr_field_id.contains("VAS")) {
				StrSql += " SUM(service_target_vas_amount)AS service_target_vas_amount,";
			}
			// oil
			if (dr_field_id.contains("Oil")) {
				StrSql += " SUM(service_target_oil_amount)AS service_target_oil_amount,";
			}
			// break
			if (dr_field_id.contains("Brake")) {
				StrSql += " SUM(service_target_break_count)AS service_target_break_count,"
						+ " SUM(service_target_break_amount)AS service_target_break_amount,";
			}
			// tyre
			if (dr_field_id.contains("Tyre")) {
				StrSql += " SUM(service_target_tyre_count)AS service_target_tyre_count,"
						+ " SUM(service_target_tyre_amount)AS service_target_tyre_amount,";
			}

			// battery
			if (dr_field_id.contains("Battery")) {
				StrSql += " SUM(service_target_battery_count)AS service_target_battery_count,"
						+ " SUM(service_target_battery_amount)AS service_target_battery_amount,";
			}
			// accessories
			if (dr_field_id.contains("Accessories")) {
				StrSql += " SUM(service_target_accessories_amount)AS service_target_accessories_amount,";
			}
			// ext
			if (dr_field_id.contains("Ext")) {
				StrSql += " SUM(service_target_extwarranty_count)AS service_target_extwarranty_count,"
						+ " SUM(service_target_extwarranty_amount)AS service_target_extwarranty_amount,";
			}

			StrSql += " service_target_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_service_target"
					+ " WHERE 1 = 1"
					+ " AND SUBSTRING(service_target_startdate,1,8) >= SUBSTR('" + targetstarttime + "',1,8)"
					+ " AND SUBSTRING(service_target_startdate,1,8) <= SUBSTR('" + targetendtime + "',1,8)"
					// + " AND service_target_startdate >=" + targetstarttime
					// + " AND service_target_enddate <=" + targetendtime
					+ " GROUP BY service_target_emp_id"
					+ " ) AS tblst ON tblst.service_target_emp_id = emp_id";

			// if (dr_totalby.equals("preownedmodel_id") || !preownedmodel_id.equals("")) {
			// StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = jc_variant_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_preownedmodel_id = jc_variant_id";
			// }
			StrSql += " WHERE 1 = 1";
			if (!emp_active.equals("1")) {
				StrSql += " AND emp_active = 1";
			}
			// + " AND emp_service = 1
			StrSql += " AND branch_branchtype_id = 3"
					+ " AND emp_id != 1"
					+ StrSearch
					+ empmainjoin;
			// + jobcardemp.replace("jc_emp_id", "emp_id");
			//
			// if (!dr_totalby.equals("jc_emp_id")) {
			// StrSql += technicianemp.replace("jc_technician_emp_id", "emp_id");
			// }
			StrSql += BranchAccess
					+ ExeAccess;
			if (dr_totalby.equals("jc_emp_id")) {
				StrSql += " AND emp_service = 1"
						+ " GROUP BY emp_id  ";
			} else if (dr_totalby.equals("jc_technician_emp_id")) {
				StrSql += " AND emp_technician = 1"
						+ " GROUP BY emp_id  ";
			} else {
				StrSql += " GROUP BY " + dr_totalby;
			}

			if (dr_orderby.equals("0")) {

				if (dr_totalby.equals("jc_jccat_id")) {
					StrSql += " ORDER BY jccat_name DESC";
				} else if (dr_totalby.equals("branch_zone_id")) {
					StrSql += " ORDER BY zone_name ";
				} else if (dr_totalby.equals("branch_region_id")) {
					StrSql += " ORDER BY region_name ";
				} else if (dr_totalby.equals("branch_brand_id")) {
					StrSql += " ORDER BY brand_name ";
				} else if (dr_totalby.equals("preownedmodel_id")) {
					StrSql += " ORDER BY preownedmodel_name ";
				} else if (dr_totalby.equals("emp_branch_id")) {
					StrSql += " ORDER BY branch_name ";
				} else {
					StrSql += " ORDER BY emp_name";
				}
			}
			else {
				StrSql += " ORDER BY " + dr_orderby + " DESC";
			}
			// SOP("StrSql==in serrvicemb==" + StrSql);
			// if (emp_id.equals("1") && dr_totalby.equals("jc_jccat_id")) {
			//
			// SOPInfo("StrSql==serrvicemb==" + StrSql);
			// }
			double total_target_tilldate = 0.000;
			double day = 0;
			double daycount = 0;
			day = getDaysBetween(starttime, endtime);
			daycount = getDaysBetween(starttime, endtime)
					+ ((DaysInMonth(Integer.parseInt(SplitYear(endtime)), Integer.parseInt(SplitMonth(endtime))) - Integer.parseInt(SplitDate(endtime))))
					+ (Integer.parseInt(SplitDate(starttime)) - 1);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class='container'>");
				Str.append("<div class=' table-bordered'>\n");
				Str.append("<table class='table table-hover table-bordered' data-filter='#filter' id='table'>\n");
				Str.append("<thead style='margin-top:50px;'>");
				Str.append("<tr>\n");
				Str.append("<th data-toggle='true'>#</th>\n");
				String title_head = "", id = "", total_by = "";
				switch (dr_totalby) {
					case "branch_brand_id" :
						title_head = "Brand";
						total_by = "brand";
						break;
					case "branch_region_id" :
						title_head = "Region";
						total_by = "region";
						break;
					case "branch_zone_id" :
						title_head = "Zone";
						total_by = "zone";
						break;
					case "emp_branch_id" :
						title_head = "Branch";
						total_by = "branch";
						break;
					case "preownedmodel_id" :
						title_head = "Model";
						total_by = "model";
						break;
					case "jc_emp_id" :
						title_head = "Service Advisor";
						total_by = "emp";
						break;
					case "jc_technician_emp_id" :
						title_head = "Service Technician";
						total_by = "tech";
						break;
					case "jc_jccat_id" :
						title_head = "Job Card Category";
						total_by = "jccat";
						break;
					case "jc_jctype_id" :
						title_head = "Job Card Type";
						total_by = "jctype";
						break;
					default :
						title_head = "Service Advisor";
						total_by = "emp";
						break;
				}

				Str.append("<th title=" + title_head + ">" + title_head + "</th>\n");
				if (dr_field_id.contains("RO")) {
					Str.append("<th data-hide='phone' title='RO Target'>RO T</th>\n");
					Str.append("<th data-hide='phone' title='RO MTD'>RO MTD</th>\n");
					Str.append("<th data-hide='phone' title='RO Achieved'>RO A</th>\n");
					Str.append("<th data-hide='phone'>RO %</th>\n");
				}

				if (dr_field_id.contains("Labour")) {
					Str.append("<th data-hide='phone' title='Labour Target'>Labour T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Labour MTD</th>\n");
					Str.append("<th data-hide='phone' title='Labour Achieved'>Labour A</th>\n");
					Str.append("<th data-hide='phone'>Labour %</th>\n");
				}

				if (dr_field_id.contains("Parts")) {
					Str.append("<th data-hide='phone' title='Parts Target'>Parts T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Parts MTD</th>\n");

					Str.append("<th data-hide='phone' title='Parts,Wheelalign,CNG'>Other Parts A</th>\n");
					Str.append("<th data-hide='phone' title='Total Parts Amount'>Total Parts Amount A</th>\n");
					Str.append("<th data-hide='phone' >Total Parts% </th>\n");
				}

				if (dr_field_id.contains("VAS")) {
					Str.append("<th data-hide='phone' title='VAS Target'>VAS T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>VAS MTD</th>\n");
					Str.append("<th data-hide='phone' title='VAS Achieved'>VAS A</th>\n");
					Str.append("<th data-hide='phone' title='VAS Labour Achieved'>VAS Labour A</th>\n");
					Str.append("<th data-hide='phone'>VAS %</th>\n");
				}

				if (dr_field_id.contains("Oil")) {
					Str.append("<th data-hide='phone' title='Oil Target'>Oil T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Oil MTD</th>\n");
					Str.append("<th data-hide='phone' title='Oil Achieved'>Oil A</th>\n");
					Str.append("<th data-hide='phone'>Oil %</th>\n");
				}

				if (dr_field_id.contains("Brake")) {
					Str.append("<th data-hide='phone' title='Brake Count Target'>Brake Count T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Brake Count MTD</th>\n");
					Str.append("<th data-hide='phone' title='Brake Count Achieved'>Brake Count A</th>\n");
					Str.append("<th data-hide='phone'>Brake Count %</th>\n");
				}

				if (dr_field_id.contains("Tyre")) {
					Str.append("<th data-hide='phone' title='Tyre Count Target '>Tyre Count T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Tyre Count MTD</th>\n");
					Str.append("<th data-hide='phone' title='Tyre Count Achieved'>Tyre Count A</th>\n");
					Str.append("<th data-hide='phone'>Tyre Count %</th>\n");
				}
				// Str.append("<th data-hide='phone' title='Tyre Target'>Tyre T</th>\n");
				// Str.append("<th data-hide='phone' title='Tyre Achived'>Tyre A</th>\n");
				// Str.append("<th data-hide='phone'>Tyre %</th>\n");

				// Str.append("<th data-hide='phone' title='Break Target'>Break T</th>\n");
				// Str.append("<th data-hide='phone' title='Break Achived'>Break A</th>\n");
				// Str.append("<th data-hide='phone'>Break %</th>\n");
				if (dr_field_id.contains("Battery")) {
					Str.append("<th data-hide='phone' title='Battery Count Target'>Battery Count T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Battery Count MTD</th>\n");
					Str.append("<th data-hide='phone' title='Battery Count Achieved'>Battery Count A</th>\n");
					Str.append("<th data-hide='phone'>Battery Count %</th>\n");
				}
				// Str.append("<th data-hide='phone' title='Battery Target'>Battery T</th>\n");
				// Str.append("<th data-hide='phone' title='Break Achived'>Battery A</th>\n");
				// Str.append("<th data-hide='phone'>Battery %</th>\n");
				if (dr_field_id.contains("Accessories")) {
					Str.append("<th data-hide='phone' title='Accessories Target'>Acc. T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Acc. MTD</th>\n");
					Str.append("<th data-hide='phone' title='Accessories Achieved'>Acc. A</th>\n");
					Str.append("<th data-hide='phone' >Acc. %</th>\n");
				}

				if (dr_field_id.contains("Ext")) {
					Str.append("<th data-hide='phone' title='Ext.Warranty Count Target'>Ext.Warranty Count T</th>\n");
					Str.append("<th data-hide='phone' title='RO Target'>Ext.Warranty Count MTD</th>\n");
					Str.append("<th data-hide='phone' title='Ext.Warranty Count Achieved'>Ext.Warranty Count A</th>\n");
					Str.append("<th data-hide='phone'>Ext.Warranty Count %</th>\n");
				}
				// Str.append("<th data-hide='phone' title='Ext.Warranty Target'>Ext.Warranty T</th>\n");
				// Str.append("<th data-hide='phone' title='Ext.Warranty Achived'>Ext.Warranty A</th>\n");
				// Str.append("<th data-hide='phone'>Ext.Warranty %</th>\n");

				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					emp_id = crs.getString("emp_id");
					String brand_id = crs.getString("branch_brand_id");
					String region_id = crs.getString("branch_region_id");
					String preownedmodel_id = crs.getString("preownedmodel_id");
					String zone_id = crs.getString("branch_zone_id");
					String branch_id = crs.getString("branch_id");
					String jccat_id = crs.getString("jc_jccat_id");
					// if (total_by.equals("jctype")) {
					// String jctype_id = crs.getString("jc_jctype_id");
					// }

					// ro
					if (dr_field_id.contains("RO")) {
						ro_target = crs.getInt("jccount");
						ro_achv = crs.getInt("roachived");
						total_ro_target += ro_target;
						total_ro_achv += ro_achv;
					}

					// labour
					if (dr_field_id.contains("Labour")) {
						labour_target = crs.getDouble("labouramount");
						labour_achv = crs.getDouble("billlabour");
						total_labour_target += labour_target;
						total_labour_achv += labour_achv;
					}
					// parts
					if (dr_field_id.contains("Parts")) {
						otherparts = crs.getDouble("jc_bill_cash_parts") + crs.getDouble("jc_bill_cash_parts_wheelalign") + crs.getDouble("jc_bill_cash_parts_cng");
						totalparts = otherparts + crs.getDouble("billvalueadd") + crs.getDouble("billtyre")
								+ crs.getDouble("billbreak") + crs.getDouble("billbattery")
								+ crs.getDouble("billoil") + crs.getDouble("billaccessories");
						totalotherparts += otherparts;
						totalpartsamount += totalparts;
						parts_target = crs.getDouble("service_target_parts_amount");
						total_parts_target += parts_target;
					}
					// vas
					if (dr_field_id.contains("VAS")) {
						vas_target = crs.getDouble("vasamount");
						vas_achv = crs.getDouble("billvalueadd");
						vas_labour_achv = crs.getDouble("billlabourvalueadd");
						total_vas_target += vas_target;
						total_vas_achv += vas_achv;
						total_vas_labour_achv += vas_labour_achv;
					}
					// oil
					if (dr_field_id.contains("Oil")) {
						oil_target = crs.getDouble("oilamount");
						oil_achv = crs.getDouble("billoil");
						total_oil_target += oil_target;
						total_oil_achv += oil_achv;
					}
					// break
					if (dr_field_id.contains("Brake")) {
						break_count_target = crs.getDouble("breakcount");
						break_count_achv = crs.getDouble("billbreakqty");
						total_break_target_count += break_count_target;
						total_break_qty += break_count_achv;
						break_target = crs.getDouble("breakamount");
						break_achv = crs.getDouble("billbreak");
						total_break_target += break_target;
						total_break_achv += break_achv;
					}
					// tyre
					if (dr_field_id.contains("Tyre")) {
						tyre_target_count = crs.getDouble("tyrecount");
						tyre_target = crs.getDouble("tyreamount");
						tyre_achv = crs.getDouble("billtyre");
						tyre_qty = crs.getDouble("billtyreqty");
						total_tyre_target_count += tyre_target_count;
						total_tyre_target += tyre_target;
						total_tyre_achv += tyre_achv;
						total_tyre_qty += tyre_qty;
					}
					// battery
					if (dr_field_id.contains("Battery")) {
						battery_target_count = crs.getDouble("batterycount");
						battery_target = crs.getDouble("batteryamount");
						battery_achv = crs.getDouble("billbattery");
						battery_qty = crs.getDouble("billbatteryqty");
						total_battery_target_count += battery_target_count;
						total_battery_target += battery_target;
						total_battery_achv += battery_achv;
						total_battery_qty += battery_qty;
					}
					// accessories
					if (dr_field_id.contains("Accessories")) {
						access_target = crs.getDouble("accessoriesamount");
						access_achv = crs.getDouble("billaccessories");
						total_access_target += access_target;
						total_access_achv += access_achv;
					}
					// ext
					if (dr_field_id.contains("Ext")) {
						extwarranty_target_count = crs.getDouble("extwarrantycount");
						extwarranty_target = crs.getDouble("extwarrantyamount");
						extwarranty_achv = crs.getDouble("billextwarranty");
						extwarranty_qty = crs.getDouble("billextwarrantyqty");
						total_extwarranty_target_count += extwarranty_target_count;
						total_extwarranty_target += extwarranty_target;
						total_extwarranty_qty += extwarranty_qty;
					}
					// branch_name = crs.getString("branch_name");
					// region_name = crs.getString("region_name");
					// brand_name = crs.getString("brand_name");
					// SOP("jc_bill_cash_parts==" + crs.getDouble("jc_bill_cash_parts"));
					// SOP("jc_bill_cash_parts_wheelalign==" + crs.getDouble("jc_bill_cash_parts_wheelalign"));
					// SOP("jc_bill_cash_parts_cng==" + crs.getDouble("jc_bill_cash_parts_cng"));
					// SOP("billvalueadd==" + crs.getDouble("billvalueadd"));
					// SOP("billtyre==" + crs.getDouble("billtyre"));
					// SOP("billbreak==" + crs.getDouble("billbreak"));
					// SOP("billbattery==" + crs.getDouble("billbattery"));
					// SOP("billoil==" + crs.getDouble("billoil"));
					// SOP("totalparts==" + totalparts);
					// SOP("otherparts==" + otherparts);
					// SOP("totalotherparts==" + totalotherparts);
					// SOP("totalpartsamount==" + totalpartsamount);
					count++;

					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					switch (dr_totalby) {
						case "emp_branch_id" :
							Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
							id = branch_id;
							break;
						case "branch_region_id" :
							Str.append("<td align=left>").append(crs.getString("region_name")).append("</td>\n");
							id = region_id;
							break;
						case "branch_zone_id" :
							Str.append("<td align=left>").append(crs.getString("zone_name")).append("</td>\n");
							id = zone_id;
							break;
						case "preownedmodel_id" :
							Str.append("<td align=left>").append(crs.getString("preownedmodel_name")).append("</td>\n");
							id = preownedmodel_id;
							break;
						case "branch_brand_id" :
							Str.append("<td align=left>").append(crs.getString("brand_name")).append("</td>\n");
							id = brand_id;
							break;
						case "jc_emp_id" :
							Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id")).append("&emp_active=" + crs.getString("emp_active")).append(">")
									.append(crs.getString("emp_name")).append("</a>");
							if (crs.getString("emp_active").equals("0")) {
								Str.append(" <font color=red><b>[Inactive]</b></font>");
							}
							Str.append("</td>\n");
							id = emp_id;
							break;
						case "jc_technician_emp_id" :
							Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id")).append("&emp_active=" + crs.getString("emp_active")).append(">")
									.append(crs.getString("emp_name")).append("</a>");
							if (crs.getString("emp_active").equals("0")) {
								Str.append(" <font color=red><b>[Inactive]</b></font>");
							}
							Str.append("</td>\n");
							id = emp_id;
							break;
						case "jc_jccat_id" :
							Str.append("<td align=left>").append(crs.getString("jccat_name")).append("</td>\n");
							id = jccat_id;
							break;
						case "jc_jctype_id" :
							Str.append("<td align=left>").append(crs.getString("jctype_name")).append("</td>\n");
							id = jctype_id;
							break;

						default :
							Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id")).append("&emp_active=" + crs.getString("emp_active"))
									.append(">").append(crs.getString("emp_name")).append("</a>");
							if (crs.getString("emp_active").equals("0")) {
								Str.append(" <font color=red><b>[Inactive]</b></font>");
							}
							Str.append("</td>\n");
							id = emp_id;
							break;
					}

					if (dr_totalby.equals("jc_emp_id") || dr_totalby.equals("jc_technician_emp_id")) {

						if (dr_field_id.contains("RO")) {
							// RO Target
							total_target_tilldate = (ro_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(ro_target).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(total_target_tilldate)).append("</a></b></td>\n");
							// RO Achived
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&total_by=" + total_by)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(ro_achv).append("</a></b></td>\n");
							// RO Perc
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) ro_achv, (double) ro_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Labour")) {
							// TARGET LABOUR AMOUNT
							total_target_tilldate = (labour_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget").append("&emp_branch_id=" + branch_id).
									append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(labour_target)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget").append("&emp_branch_id=" + branch_id).
									append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(total_target_tilldate)).append("</a></b></td>\n");
							// ACHIVED LABOUR AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("labouramount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(labour_achv)).append("</a></b></td>\n");
							// PERC LABOUR
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) labour_achv, (double) labour_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Parts")) {
							// Parts Target amount
							total_target_tilldate = (parts_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(parts_target)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(total_target_tilldate)).append("</a></b></td>\n");

							// OTHER PARTS AMOUNT
							Str.append("<td nowrap align=right><b>").append(count(otherparts)).append("</b></td>\n");
							// TOTAL PARTS AMOUNT
							Str.append("<td align=right><b>").append(count(totalparts)).append("</b></td>\n");
							// SOP("str total by empid=" + Str);
							// PERC Parts
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) totalparts, (double) parts_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("VAS")) {
							// TARGET VAS AMOUNT
							total_target_tilldate = (vas_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(vas_target)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(total_target_tilldate)).append("</a></b></td>\n");
							// ACHIVED VAS AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("vasamount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(vas_achv)).append("</a></b></td>\n");
							// ACHIVED VAS Labour AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("vaslabouramount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(vas_labour_achv)).append("</a></b></td>\n");
							// PERC VAS
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) vas_achv, (double) vas_achv))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Oil")) {
							// OIL TARGET AMOUNT
							total_target_tilldate = (oil_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(oil_target)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(total_target_tilldate)).append("</a></b></td>\n");
							// OIL ACHIVED AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("oilamount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(oil_achv)).append("</a></b></td>\n");
							// OIL PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) oil_achv, (double) oil_target))).append("</b></td>\n");
						}
						/*
						 * // TYRE TARGET AMOUNT Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget") .append("&emp_branch_id=" + branch_id) .append("&value=" + id +
						 * " target=_blank>") .append(unescapehtml(IndDecimalFormat(deci.format((tyre_target))))).append("</a></b></td>\n"); // TYRE ACHIVED AMOUNT
						 * Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("tyreamount"). append("&total_by=" + total_by).append("&value=" + id
						 * + " target=_blank>") .append(unescapehtml(IndDecimalFormat(deci.format((tyre_achv))))).append("</a></b></td>\n"); // TYRE PERC
						 * Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) tyre_achv, (double) tyre_target))).append("</b></td>\n"); // BREAK TARGET AMOUNT
						 * Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget") .append("&emp_branch_id=" + branch_id) .append("&value=" + id + " target=_blank>")
						 * .append(unescapehtml(IndDecimalFormat(deci.format((break_target))))).append("</a></b></td>\n"); // BREAK ACHIVED AMOUNT
						 * Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("breakamount"). append("&total_by=" + total_by).append("&value=" + id
						 * + " target=_blank>") .append(unescapehtml(IndDecimalFormat(deci.format((break_achv))))).append("</a></b></td>\n");
						 */
						if (dr_field_id.contains("Brake")) {
							// BREAK TARGET COUNT
							total_target_tilldate = (break_count_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(break_count_target)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((Math.round((total_target_tilldate)))).append("</a></b></td>\n");
							// BREAK ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("breakcount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((break_count_achv)).append("</a></b></td>\n");
							// BREAK PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) break_count_achv, (double) break_count_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Tyre")) {
							// TYRE TARGET COUNT
							total_target_tilldate = (tyre_target_count / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(tyre_target_count)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((Math.round((total_target_tilldate)))).append("</a></b></td>\n");
							// TYRE ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("tyrecount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((tyre_qty)).append("</a></b></td>\n");
							// TYRE PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) tyre_qty, (double) tyre_target_count))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Battery")) {
							// BATTERY TARGET COUNT
							total_target_tilldate = (battery_target_count / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(battery_target_count)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round((total_target_tilldate))).append("</a></b></td>\n");
							// BATTERY ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("batterycount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((battery_qty)).append("</a></b></td>\n");
							// BATTERY PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) battery_qty, (double) battery_target_count))).append("</b></td>\n");
						}
						// // BATTERY TARGET AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
						// .append("&emp_branch_id=" + branch_id)
						// .append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((battery_target))))).append("</a></b></td>\n");
						// // BATTERY ACHIVED AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("batteryamount").
						// append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((battery_achv))))).append("</a></b></td>\n");
						// // BATTERY PERC
						// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) battery_achv, (double) battery_target))).append("</b></td>\n");

						if (dr_field_id.contains("Accessories")) {
							// ACCESSIORES TARGET AMOUNT
							total_target_tilldate = (access_target / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(access_target)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(total_target_tilldate)).append("</a></b></td>\n");
							// ACCESSIORES ACHIVED AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("acessamount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(access_achv)).append("</a></b></td>\n");
							// ACCESSIORES PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) access_achv, (double) access_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Ext")) {
							// EXT.WARRANTY TARGET COUNT
							total_target_tilldate = (extwarranty_target_count / daycount) * day;
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round(extwarranty_target_count)).append("</a></b></td>\n");
							Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
									.append("&emp_branch_id=" + branch_id)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(Math.round((total_target_tilldate))).append("</a></b></td>\n");
							// EXT.WARRANTY ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("extwarrtycount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((extwarranty_qty)).append("</a></b></td>\n");
							// EXT.WARRANTY PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) extwarranty_qty, (double) extwarranty_target_count))).append("</b></td>\n");
						}
						// // EXT.WARRANTY TARGET AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("servtarget")
						// .append("&emp_branch_id=" + branch_id)
						// .append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((extwarranty_target))))).append("</a></b></td>\n");
						// // EXT.WARRANTY ACHIVED AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("extwarrtyamount").
						// append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((extwarranty_achv))))).append("</a></b></td>\n");
						// // EXT.WARRANTY PERC
						// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) extwarranty_achv, (double) extwarranty_target))).append("</b></td>\n");

					} else {
						if (dr_field_id.contains("RO")) {
							// RO TARGET AMOUNT
							total_target_tilldate = (ro_target / daycount) * day;
							Str.append("<td align=right><b>").append(ro_target).append("</b></td>\n");
							Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
							// RO ACHIVED AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&total_by=" + total_by)
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(ro_achv).append("</a></b></td>\n");
							// RO PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) ro_achv, (double) ro_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Labour")) {
							// TARGET LABOUR AMOUNT
							total_target_tilldate = (labour_target / daycount) * day;
							Str.append("<td align=right><b>").append(count(labour_target)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");
							// ACHIVED LABOUR AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("labouramount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(labour_achv)).append("</a></b></td>\n");
							// PERC LABOUR
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) labour_achv, (double) labour_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Parts")) {
							// Parts target amount
							total_target_tilldate = (parts_target / daycount) * day;
							Str.append("<td align=right><b>").append(count(parts_target)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");

							// OTHER PARTS AMOUNT
							Str.append("<td nowrap align=right><b>").append(count(otherparts)).append("</b></td>\n");
							// TOTAL PARTS AMOUNT
							Str.append("<td align=right><b>").append(count(totalparts)).append("</b></td>\n");
							// SOP("str total else" + Str);
							// PERC Parts
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) totalparts, (double) parts_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("VAS")) {
							// TARGET VAS AMOUNT
							total_target_tilldate = (vas_target / daycount) * day;
							Str.append("<td align=right><b>").append(count(vas_target)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");
							// ACHIVED VAS AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("vasamount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(vas_achv)).append("</a></b></td>\n");
							// ACHIVED VAS LAbour AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("vaslabouramount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(vas_labour_achv)).append("</a></b></td>\n");
							// VAS PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) vas_achv, (double) vas_achv))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Oil")) {
							// OIL TARGET AMOUNT
							total_target_tilldate = (oil_target / daycount) * day;
							Str.append("<td align=right><b>").append(count(oil_target)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");
							// OIL ACHIVED AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("oilamount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(oil_achv)).append("</a></b></td>\n");
							// OIL PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) oil_achv, (double) oil_target))).append("</b></td>\n");
						}
						// // TYRE TARGET AMOUNT
						// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((tyre_target))))).append("</b></td>\n");
						// // TYRE ACHIVED AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("tyreamount").
						// append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((tyre_achv))))).append("</a></b></td>\n");
						// // TYRE PERC
						// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) tyre_achv, (double) tyre_target))).append("</b></td>\n");
						// // BREAK TARGET AMOUNT
						// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((break_target))))).append("</b></td>\n");
						// // BREAK ACHIVED AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("breakamount").
						// append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((break_achv))))).append("</a></b></td>\n");
						// // BREAK PERC
						// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) break_achv, (double) break_target))).append("</b></td>\n");
						if (dr_field_id.contains("Brake")) {
							// BREAK TARGET COUNT
							total_target_tilldate = (break_count_target / daycount) * day;
							Str.append("<td align=right><b>").append(Math.round(break_count_target)).append("</b></td>\n");
							Str.append("<td align=right><b>").append((Math.round(total_target_tilldate))).append("</b></td>\n");

							// BREAK ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("breakcount")
									.append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((break_count_achv)).append("</a></b></td>\n");

							// BREAK PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) break_count_achv, (double) break_count_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Tyre")) {
							// TYRE TARGET COUNT
							total_target_tilldate = (tyre_target_count / daycount) * day;
							Str.append("<td align=right><b>").append(Math.round(tyre_target_count)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
							// TYRE ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("tyrecount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((tyre_qty)).append("</a></b></td>\n");
							// TYRE PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) tyre_qty, (double) tyre_target_count))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Battery")) {
							// BATTERY TARGET COUNT
							total_target_tilldate = (battery_target_count / daycount) * day;
							Str.append("<td align=right><b>").append(Math.round(battery_target_count)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(Math.round((total_target_tilldate))).append("</b></td>\n");
							// BATTERY ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("batterycount")
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((battery_qty)).append("</a></b></td>\n");
							// BATTERY PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) battery_qty, (double) battery_target_count))).append("</b></td>\n");
						}
						// // BATTERY TARGET AMOUNT
						// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((battery_target))))).append("</b></td>\n");
						// // BATTERY ACHIVED AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("batteryamount").
						// append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((battery_achv))))).append("</a></b></td>\n");
						// // BATTERY PERC
						// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) battery_achv, (double) battery_target))).append("</b></td>\n");

						if (dr_field_id.contains("Accessories")) {
							// ACCESSIORES TARGET AMOUNT
							total_target_tilldate = (access_target / daycount) * day;
							Str.append("<td align=right><b>").append(Math.round(access_target)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
							// ACCESSIORES ACHIVED AMOUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("acessamount").
									append("&total_by=" + total_by).append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append(count(access_achv)).append("</a></b></td>\n");
							// ACCESSIORES PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) access_achv, (double) access_target))).append("</b></td>\n");
						}

						if (dr_field_id.contains("Ext")) {
							// EXT.WARRANTY TARGET COUNT
							total_target_tilldate = (extwarranty_target_count / daycount) * day;
							Str.append("<td align=right><b>").append(Math.round(extwarranty_target_count)).append("</b></td>\n");
							Str.append("<td align=right><b>").append(Math.round((total_target_tilldate))).append("</b></td>\n");
							// EXT.WARRANTY ACHIVED COUNT
							Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("extwarrtycount")
									.append("&value=" + id).append("&emp_active=" + emp_active).append(" target=_blank>")
									.append((extwarranty_qty)).append("</a></b></td>\n");
							// EXT.WARRANTY PERC
							Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) extwarranty_qty, (double) extwarranty_target_count))).append("</b></td>\n");
						}
						// // EXT.WARRANTY TARGET AMOUNT
						// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((extwarranty_target))))).append("</b></td>\n");
						// // EXT.WARRANTY ACHIVED AMOUNT
						// Str.append("<td align=right><b><a href=").append(appendStr).append("achived").append("&jclink=").append("extwarrtyamount").
						// append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
						// .append(unescapehtml(IndDecimalFormat(deci.format((extwarranty_achv))))).append("</a></b></td>\n");
						// // EXT.WARRANTY PERC
						// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) extwarranty_achv, (double) extwarranty_target))).append("</b></td>\n");

					}
					Str.append("</tr>");
				}

				Str.append("<tr>\n");
				Str.append("<td align=right class=total-field><b>Total: </b></td>\n");
				if (dr_field_id.contains("RO")) {
					// TOTAL RO TARGET AMOUNT
					total_target_tilldate = (total_ro_target / daycount) * day;
					Str.append("<td align=right><b>").append(total_ro_target).append("</b></td>\n");
					Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
					// TOTAL RO ACHIVED AMOUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append(" target=_blank>").append(total_ro_achv)
							.append("</a></b></td>\n");
					// TOTAL RO PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_ro_achv, (double) total_ro_target))).append("</b></td>\n");
				}

				if (dr_field_id.contains("Labour")) {
					// TOTAL LABOUR TARGET AMOUNT
					total_target_tilldate = (total_labour_target / daycount) * day;
					Str.append("<td align=right><b>").append(count(total_labour_target)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");
					// TOTAL LABOUR ACHIVED AMOUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totallabouramount").
							append(" target=_blank>").append(count(total_labour_achv)).append("</a></b></td>\n");
					// TOTAL LABOUR PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_labour_achv, (double) total_labour_target))).append("</b></td>\n");
				}

				if (dr_field_id.contains("Parts")) {
					// TOTAL PARTS TARGET AMOUNT
					total_target_tilldate = (total_parts_target / daycount) * day;
					Str.append("<td align=right><b>").append(count(total_parts_target)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");

					// TOTAL OTHER PATRS AMOUNT
					Str.append("<td  align=right><b>").append(count(totalotherparts)).append("</b></td>\n");
					// TOTAL PATRS AMOUNT
					Str.append("<td align=right><b>").append(count(totalpartsamount)).append("</b></td>\n");
					// TOTAL Parts PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) totalpartsamount, (double) total_parts_target))).append("</b></td>\n");
				}

				if (dr_field_id.contains("VAS")) {
					// TOTAL VAS TARGET AMOUNT
					total_target_tilldate = (total_vas_target / daycount) * day;
					Str.append("<td align=right><b>").append(count(total_vas_target)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");
					// TOTAL VAS ACHIVED AMOUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalvasamount").
							append(" target=_blank>").append(count(total_vas_achv)).append("</a></b></td>\n");
					// TOTAL VAS ACHIVED AMOUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalvaslabouramount").
							append(" target=_blank>").append(count(total_vas_labour_achv)).append("</a></b></td>\n");
					// TOTAL VAS PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_vas_achv, (double) total_vas_achv))).append("</b></td>\n");
				}

				if (dr_field_id.contains("Oil")) {
					// TOTAL OIL TARGET AMOUNT
					total_target_tilldate = (total_oil_target / daycount) * day;
					Str.append("<td align=right><b>").append(count(total_oil_target)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(count(total_target_tilldate)).append("</b></td>\n");
					// TOTAL OIL ACHIVED AMOUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totaloilamount").
							append(" target=_blank>").append(count(total_oil_achv)).append("</a></b></td>\n");
					// TOTAL OIL PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_oil_achv, (double) total_oil_target))).append("</b></td>\n");
				}
				// // TOTAL TYRE TARGET AMOUNT
				// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_tyre_target))))).append("</b></td>\n");
				// // TOTAL TYRE ACHIVED AMOUNT
				// Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totaltyreamount").
				// append(" target=_blank>").append(unescapehtml(IndDecimalFormat(deci.format((total_tyre_achv)))))
				// .append("</a></b></td>\n");
				// // TOTAL TYRE PERC
				// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_tyre_achv, (double) total_tyre_target))).append("</b></td>\n");
				// // TOTAL BREAK TARGET AMOUNT
				// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_break_target))))).append("</b></td>\n");
				// // TOTAL BREAK ACHIVED AMOUNT
				// Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalbreakamount").
				// append(" target=_blank>").append(unescapehtml(IndDecimalFormat(deci.format((total_break_achv)))))
				// .append("</a></b></td>\n");
				// // TOTAL BREAK PERC
				// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_break_achv, (double) total_break_target))).append("</b></td>\n");

				// total_break_target_count += break_count_target;
				// total_break_qty += break_count_achv;

				if (dr_field_id.contains("Brake")) {
					// TOTAL BREAK TARGET COUNT
					total_target_tilldate = (total_break_target_count / daycount) * day;
					Str.append("<td align=right><b>").append(Math.round(total_break_target_count)).append("</b></td>\n");
					Str.append("<td align=right><b>").append((Math.round(total_target_tilldate))).append("</b></td>\n");
					// TOTAL BREAK ACHIVED COUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalbreakcount").
							append(" target=_blank>").append((total_break_qty)).append("</a></b></td>\n");
					// TOTAL BREAK PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_break_qty, (double) total_break_target_count))).append("</b></td>\n");
				}

				if (dr_field_id.contains("Tyre")) {
					// TOTAL TYRE TARGET COUNT
					total_target_tilldate = (total_tyre_target_count / daycount) * day;
					Str.append("<td align=right><b>").append(Math.round(total_tyre_target_count)).append("</b></td>\n");
					Str.append("<td align=right><b>").append((Math.round(total_target_tilldate))).append("</b></td>\n");
					// TOTAL TYRE ACHIVED COUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totaltyrecount").
							append(" target=_blank>").append((total_tyre_qty)).append("</a></b></td>\n");
					// TOTAL TYRE PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_tyre_qty, (double) total_tyre_target_count))).append("</b></td>\n");
				}

				if (dr_field_id.contains("Battery")) {
					// TOTAL BATTERY TARGET COUNT
					total_target_tilldate = (total_battery_target_count / daycount) * day;
					Str.append("<td align=right><b>").append(Math.round(total_battery_target_count)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
					// TOTAL BATTERY ACHIVED COUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalbatterycount").
							append(" target=_blank>").append((total_battery_qty)).append("</a></b></td>\n");
					// TOTAL BATTERY PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_battery_qty, (double) total_battery_target_count))).append("</b></td>\n");
				}
				// // TOTAL BATTERY TARGET AMOUNT
				// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_battery_target))))).append("</b></td>\n");
				// // TOTAL BATTERY ACHIVED AMOUNT
				// Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalbatteryamount").
				// append(" target=_blank>").append(unescapehtml(IndDecimalFormat(deci.format((total_battery_achv)))))
				// .append("</a></b></td>\n");
				// // TOTAL BATTERY PERC
				// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_battery_achv, (double) total_battery_target))).append("</b></td>\n");

				if (dr_field_id.contains("Accessories")) {
					// TOTAL ACCESSIORES TARGET AMOUNT
					total_target_tilldate = (total_access_target / daycount) * day;
					Str.append("<td align=right><b>").append(Math.round(total_access_target)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
					// TOTAL ACCESSIORES ACHIVED AMOUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalacessamount").
							append(" target=_blank>").append(count(total_access_achv)).append("</a></b></td>\n");
					// TOTAL ACCESSIORES PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_access_achv, (double) total_access_target))).append("</b></td>\n");
				}

				if (dr_field_id.contains("Ext")) {
					// TOTAL EXT.WARRANTY TARGET COUNT
					total_target_tilldate = (total_extwarranty_target_count / daycount) * day;
					Str.append("<td align=right><b>").append(Math.round(total_extwarranty_target_count)).append("</b></td>\n");
					Str.append("<td align=right><b>").append(Math.round(total_target_tilldate)).append("</b></td>\n");
					// TOTAL EXT.WARRANTY ACHIVED COUNT
					Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalextwarrtycount").
							append(" target=_blank>").append(total_extwarranty_qty).append("</a></b></td>\n");
					// TOTAL EXT.WARRANTY PERC
					Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_extwarranty_qty, (double) total_extwarranty_target_count)))
							.append("</b></td>\n");
				}
				// // TOTAL EXT.WARRANTY TARGET AMOUNT
				// Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci.format((total_extwarranty_target))))).append("</b></td>\n");
				// // TOTAL EXT.WARRANTY ACHIVED AMOUNT
				// Str.append("<td align=right><b><a href=").append(appendStr).append("totalachived").append("&jctotal=").append("totalextwarrtyamount").
				// append(" target=_blank>").append(unescapehtml(IndDecimalFormat(deci.format((total_extwarranty_achv)))))
				// .append("</a></b></td>\n");
				// // TOTAL EXT.WARRANTY PERC
				// Str.append("<td nowrap align=right><b>").append(Double.parseDouble(getPercentage((double) total_extwarranty_achv, (double) total_extwarranty_target))).append("</b></td>\n");

				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				// Str.append("</div>");
				// SOP("str total by" + Str);

			} else {
				Str.append("<font color=red><center><b>No Reports found!</b></center></font><br>");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}
	public String PopulateTotalBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=branch_brand_id").append(StrSelectdrop("branch_brand_id", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=preownedmodel_id").append(StrSelectdrop("preownedmodel_id", dr_totalby)).append(">Models</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=branch_zone_id").append(StrSelectdrop("branch_zone_id", dr_totalby)).append(">Zone</option>\n");
		Str.append("<option value=emp_branch_id").append(StrSelectdrop("emp_branch_id", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=jc_emp_id").append(StrSelectdrop("jc_emp_id", dr_totalby)).append(">Service Advisor</option>\n");
		Str.append("<option value=jc_technician_emp_id").append(StrSelectdrop("jc_technician_emp_id", dr_totalby)).append(">Service Technician</option>\n");
		Str.append("<option value=jc_jccat_id").append(StrSelectdrop("jc_jccat_id", dr_totalby)).append(">Job Card Category</option>\n");
		Str.append("<option value=jc_jctype_id").append(StrSelectdrop("jc_jctype_id", dr_totalby)).append(">Job Card Type</option>\n");
		return Str.toString();
	}
	public String PopulateJobCardWarranty() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_warranty)).append(">With Warranty</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_warranty)).append(">Without Warranty</option>\n");

		return Str.toString();
	}

	public String PopulateOrderBy(String comp_id, String dr_orderby) {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name='dr_orderby' class='form-control' id='dr_orderby' >");
		Str.append("<option value=0>Select</option>");
		// SOP("dr_orderby===" + dr_orderby);
		if (dr_orderby.contains("RO")) {
			Str.append("<option value=jccount").append(StrSelectdrop("jccount", dr_orderby)).append(">RO Target</option>\n");
			Str.append("<option value=roachived").append(StrSelectdrop("roachived", dr_orderby)).append(">RO Achieved</option>\n");
		}
		if (dr_orderby.contains("Labour")) {
			Str.append("<option value=labouramount").append(StrSelectdrop("labouramount", dr_orderby)).append(">Labour Target</option>\n");
			Str.append("<option value=billlabour").append(StrSelectdrop("billlabour", dr_orderby)).append(">Labour Achieved</option>\n");
		}
		if (dr_orderby.contains("VAS")) {
			Str.append("<option value=vasamount").append(StrSelectdrop("vasamount", dr_orderby)).append(">VAS Target</option>\n");
			Str.append("<option value=billvalueadd").append(StrSelectdrop("billvalueadd", dr_orderby)).append(">VAS Achieved</option>\n");
		}
		if (dr_orderby.contains("Oil")) {
			Str.append("<option value=oilamount").append(StrSelectdrop("oilamount", dr_orderby)).append(">Oil Target</option>\n");
			Str.append("<option value=billoil").append(StrSelectdrop("billoil", dr_orderby)).append(">Oil Achieved</option>\n");
		}
		if (dr_orderby.contains("Tyre")) {
			Str.append("<option value=tyreamount").append(StrSelectdrop("tyreamount", dr_orderby)).append(">Tyre Target</option>\n");
			Str.append("<option value=billtyre").append(StrSelectdrop("billtyre", dr_orderby)).append(">Tyre Achieved</option>\n");
		}
		if (dr_orderby.contains("Battery")) {
			Str.append("<option value=batteryamount").append(StrSelectdrop("batteryamount", dr_orderby)).append(">Battery Target</option>\n");
			Str.append("<option value=billbattery").append(StrSelectdrop("billbattery", dr_orderby)).append(">Battery Achieved</option>\n");
		}
		if (dr_orderby.contains("Brake")) {
			Str.append("<option value=breakamount").append(StrSelectdrop("breakamount", dr_orderby)).append(">Brake Target</option>\n");
			Str.append("<option value=billbreak").append(StrSelectdrop("billbreak", dr_orderby)).append(">Brake Achieved</option>\n");
		}
		if (dr_orderby.contains("Accessories")) {
			Str.append("<option value=accessoriesamount").append(StrSelectdrop("accessoriesamount", dr_orderby)).append(">Accessories Target</option>\n");
			Str.append("<option value=billaccessories").append(StrSelectdrop("billaccessories", dr_orderby)).append(">Accessories Achieved</option>\n");
		}
		Str.append("</select>");
		return Str.toString();
	}

	public String count(double value) {
		String amount = "";
		// if (value > 999999) {
		amount = deci.format((value / 100000)) + "";
		// }
		// else {
		// // amount = deci.format(value);
		// amount = unescapehtml(IndDecimalFormat(deci.format((value))));
		// }

		return amount;

	}
	public String PopulateFields(String[] dr_field_ids) {
		// SOP("dr_field_ids===" + dr_field_ids.toString());
		StringBuilder Str = new StringBuilder();
		Str.append("<select name='dr_field' id='dr_field' class='form-control multiselect-dropdown' multiple=multiple size=10 onchange='returnfieldscheck();'>");
		Str.append("<option value='RO' ").append(StrArrSelectdrop("RO", dr_field_ids)).append(">RO</option>\n");
		Str.append("<option value='Labour' ").append(StrArrSelectdrop("Labour", dr_field_ids)).append(">Labour</option>\n");
		Str.append("<option value='Parts' ").append(StrArrSelectdrop("Parts", dr_field_ids)).append(">Parts</option>\n");
		Str.append("<option value='VAS' ").append(StrArrSelectdrop("VAS", dr_field_ids)).append(">VAS</option>\n");
		Str.append("<option value='Oil' ").append(StrArrSelectdrop("Oil", dr_field_ids)).append(">Oil</option>\n");
		Str.append("<option value='Brake' ").append(StrArrSelectdrop("Brake", dr_field_ids)).append(">Brake</option>\n");
		Str.append("<option value='Tyre' ").append(StrArrSelectdrop("Tyre", dr_field_ids)).append(">Tyre</option>\n");
		Str.append("<option value='Battery' ").append(StrArrSelectdrop("Battery", dr_field_ids)).append(">Battery</option>\n");
		Str.append("<option value='Accessories' ").append(StrArrSelectdrop("Accessories", dr_field_ids)).append(">Accessories</option>\n");
		Str.append("<option value='Ext' ").append(StrArrSelectdrop("Ext", dr_field_ids)).append(">Ext. Warranty</option>\n");
		Str.append("</select>");

		return Str.toString();
	}

}
