package axela.sales;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Management_DashBoard extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, soe_ids, sob_ids;
	public String preowned_model = "", include_inactive_exe = "", include_preowned = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", soe_id = "", sob_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "", StrSob = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();
	public axela.sales.Report_Monitoring_Board reportpage = new Report_Monitoring_Board();

	public String marital_status = "", emp_active = "", sex = "", address = "", Img = "";
	public String currexp[];
	public String emp_prevexp[];
	public int years = 0;
	public int months = 0;
	public int days = 0;

	public String enquiry_count = "0";
	public String homevisit_count = "0";
	public String testdrive_count = "0";
	public String booking_count = "0";
	public String delivery_count = "0";
	public String cancellation_count = "0";
	public String invoice_count = "0";
	public String reciept_count = "0";

	public String enquirystatus_count = "0";
	public String hot_count = "0";
	public String warm_count = "0";
	public String cold_count = "0";
	public String bookingstatus_count = "0";
	public String retailstatus_count = "0";
	public String managmentFilter = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				emp_copy_access = ReturnPerm(comp_id, "emp_copy_access", request);
				if (go.equals("")) {
					end_time = DateToShortDate(kknow());
					msg = "";
				}
				if (reportpage.dr_totalby.equals("0")) {
					reportpage.dr_totalby = "3";
					dr_totalby = "3";
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					StrSearch = BranchAccess;
					// SOP("StrSearch===in mb=" + StrSearch);
					StrSearch += ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}

					managmentFilter = StrSearch;

					if (!model_id.equals("")) {
						StrModel = " AND model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSoe = " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!sob_id.equals("")) {
						StrSob = " AND enquiry_sob_id IN (" + sob_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ") ";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						PopulateTodayCount();
						PopulateOpenCount();
						reportpage.dr_orderby = dr_orderby;
						if (!dr_totalby.equals("6")) {
							StrHTML = reportpage.ListMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
						} else {
							StrHTML = reportpage.ListModelMonitorBoard(branch_id, starttime, endtime, targetstarttime, targetendtime, dr_totalby, comp_id);
						}
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		dr_orderby = request.getParameter("dr_orderby");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
		sob_id = RetrunSelectArrVal(request, "dr_sob");
		sob_ids = request.getParameterValues("dr_sob");

		preowned_model = PadQuotes(request.getParameter("chk_preowned_model"));
		include_inactive_exe = PadQuotes(request.getParameter("chk_include_inactive_exe"));
		include_preowned = PadQuotes(request.getParameter("chk_include_preowned"));

		if (preowned_model.equals("on")) {
			preowned_model = "1";
		} else {
			preowned_model = "0";
		}

		if (include_inactive_exe.equals("on")) {
			include_inactive_exe = "1";
		} else {
			include_inactive_exe = "0";
		}

		if (include_preowned.equals("on")) {
			include_preowned = "1";
		} else {
			include_preowned = "0";
		}

	}

	protected void CheckForm() {
		msg = "";

		if (endtime.equals("")) {
			msg = msg + "<br>Select Date!<br>";
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				starttime = endtime.substring(0, 6) + "01" + "000000";
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
			}
		}

	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " AND team_active = 1"
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Consultants</option>\n");
		// Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=6").append(StrSelectdrop("6", dr_totalby)).append(">Models</option>\n");
		return Str.toString();
	}

	public String PopulateOrderBy(String comp_id, String include_preowned) {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name='dr_orderby' class='form-control' id='dr_orderby'>");
		Str.append("<option value=''>Select</option>");
		Str.append("<option value=retailtarget").append(StrSelectdrop("retailtarget", dr_orderby)).append(">Retail Target</option>\n");
		Str.append("<option value=enquirytarget").append(StrSelectdrop("enquirytarget", dr_orderby)).append(">Enquiry Target</option>\n");
		Str.append("<option value=enquiryopen").append(StrSelectdrop("enquiryopen", dr_orderby)).append(">Open Enquiry</option>\n");
		Str.append("<option value=enquiryfresh").append(StrSelectdrop("enquiryfresh", dr_orderby)).append(">Fresh Enquiry</option>\n");
		Str.append("<option value=enquirylost").append(StrSelectdrop("enquirylost", dr_orderby)).append(">Lost Enquiry</option>\n");
		Str.append("<option value=pendingenquiry").append(StrSelectdrop("pendingenquiry", dr_orderby)).append(">Pending Enquiry</option>\n");
		Str.append("<option value=soretail").append(StrSelectdrop("soretail", dr_orderby)).append(">Retail</option>\n");
		Str.append("<option value=sodelivered").append(StrSelectdrop("sodelivered", dr_orderby)).append(">SO Delivered</option>\n");
		Str.append("<option value=booking").append(StrSelectdrop("booking", dr_orderby)).append(">Booking</option>\n");
		Str.append("<option value=pendingbooking").append(StrSelectdrop("pendingbooking", dr_orderby)).append(">Pending Booking</option>\n");
		Str.append("<option value=cancellation").append(StrSelectdrop("cancellation", dr_orderby)).append(">Cancellation</option>\n");
		Str.append("<option value=testdrives").append(StrSelectdrop("testdrives", dr_orderby)).append(">Test Drives</option>\n");
		Str.append("<option value=kpitestdrives").append(StrSelectdrop("kpitestdrives", dr_orderby)).append(">KPI Test Drives</option>\n");
		Str.append("<option value=homevisit").append(StrSelectdrop("homevisit", dr_orderby)).append(">Home Visit</option>\n");
		Str.append("<option value=kpihomevisit").append(StrSelectdrop("kpihomevisit", dr_orderby)).append(">KPI Home Visit</option>\n");
		Str.append("<option value=accessamt").append(StrSelectdrop("accessamt", dr_orderby)).append(">Access</option>\n");
		Str.append("<option value=insurcount").append(StrSelectdrop("insurcount", dr_orderby)).append(">Insurance</option>\n");
		Str.append("<option value=ewcount").append(StrSelectdrop("ewcount", dr_orderby)).append(">Extended Warranty</option>\n");
		Str.append("<option value=fincasecount").append(StrSelectdrop("fincasecount", dr_orderby)).append(">FIN Cases</option>\n");
		Str.append("<option value=exchange").append(StrSelectdrop("exchange", dr_orderby)).append(">Exchange</option>\n");
		if (include_preowned.equals("1")) {
			Str.append("<option value=preownedenquiry").append(StrSelectdrop("preownedenquiry", dr_orderby)).append(">Pre-Owned Enquiry</option>\n");
			Str.append("<option value=evaluation").append(StrSelectdrop("evaluation", dr_orderby)).append(">Evaluation</option>\n");
		}
		Str.append("</select>");
		return Str.toString();
	}
	//

	public void PopulateTodayCount() {
		double invoicecount = 0.0;
		double recieptcount = 0.0;
		String StrSqljoin = "", StrSqlSOjoin = "";
		if (!brand_id.equals("")
				|| !region_id.equals("")
				|| !branch_id.equals("")
				|| !StrSoe.equals("")
				|| !StrSob.equals("")
				|| !StrModel.equals("")) {
			StrSqlSOjoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id";
		}
		if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
			StrSqljoin += " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
		}

		if (include_inactive_exe.equals("0")) {
			StrSqljoin += " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " AND emp_active = 1";
		}

		try {
			StrSql = "SELECT"
					+ " COALESCE ( ( SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM  " + compdb(comp_id) + "axela_sales_enquiry"
					+ StrSqljoin
					+ " WHERE 1 = 1"
					+ " AND enquiry_status_id = '1'"
					+ " AND SUBSTR(enquiry_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'enquirycount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT followup_id)"
					+ " FROM  " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
					+ StrSqljoin
					+ " WHERE followup_followuptype_id = 3"
					+ " AND enquiry_status_id = '1'"
					+ " AND SUBSTR(followup_entry_time, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'homevisitcount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT testdrive_id)"
					+ " FROM  " + compdb(comp_id) + "axela_sales_testdrive"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
					+ StrSqljoin
					+ " WHERE SUBSTR(testdrive_fb_entry_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ " AND testdrive_fb_taken = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'testdrivecount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT so_id)"
					+ " FROM  " + compdb(comp_id) + "axela_sales_so"
					+ StrSqlSOjoin
					+ StrSqljoin.replace("enquiry_emp_id", "so_emp_id")
					+ " WHERE SUBSTR(so_entry_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ " AND so_active = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "so_emp_id")
					+ " ), 0 ) AS 'bookingcount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT so_id)"
					+ " FROM  " + compdb(comp_id) + "axela_sales_so"
					+ StrSqlSOjoin
					+ StrSqljoin.replace("enquiry_emp_id", "so_emp_id")
					+ " WHERE SUBSTR(so_delivered_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ " AND so_active = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "so_emp_id")
					+ " ), 0 ) AS 'deliveredcount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT so_id)"
					+ " FROM  " + compdb(comp_id) + "axela_sales_so"
					+ StrSqlSOjoin
					+ StrSqljoin.replace("enquiry_emp_id", "so_emp_id")
					+ " WHERE SUBSTR(so_cancel_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ " AND so_active = '0'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "so_emp_id")
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ " ), 0 ) AS 'cancelledcount',"

					+ " COALESCE ( ( SELECT SUM(voucher_amount)AS voucher_amount"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ StrSqljoin.replace("enquiry_branch_id", "voucher_branch_id")
							.replace("enquiry_emp_id", "voucher_emp_id")
					+ " WHERE SUBSTR(voucher_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ " AND voucher_vouchertype_id = 6"
					+ " AND voucher_active = '1'"
					+ " AND voucher_authorize = '1'"
					+ BranchAccess.replace("branch_id", "voucher_branch_id")
					+ ExeAccess.replace("emp_id", "voucher_emp_id")
					+ managmentFilter.replace("emp_id", "voucher_emp_id")
					+ " ), 0 ) AS 'invoicecount',"

					+ " COALESCE ( ( SELECT SUM(voucher_amount)AS voucher_amount"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ StrSqljoin.replace("enquiry_branch_id", "voucher_branch_id")
							.replace("enquiry_emp_id", "voucher_emp_id")
					+ " WHERE SUBSTR(voucher_date, 1, 8) = SUBSTR('" + endtime + "',1,8)"
					+ " AND voucher_vouchertype_id = 9"
					+ " AND voucher_active = '1'"
					+ " AND voucher_authorize = '1'"
					+ BranchAccess.replace("branch_id", "voucher_branch_id")
					+ ExeAccess.replace("emp_id", "voucher_emp_id")
					+ managmentFilter.replace("emp_id", "voucher_emp_id")
					+ " ), 0 ) AS 'recieptcount'";

			// SOP("StrSql==2222====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_count = CNumeric(crs.getString("enquirycount"));
					homevisit_count = CNumeric(crs.getString("homevisitcount"));
					testdrive_count = CNumeric(crs.getString("testdrivecount"));
					booking_count = CNumeric(crs.getString("bookingcount"));
					delivery_count = CNumeric(crs.getString("deliveredcount"));
					cancellation_count = CNumeric(crs.getString("cancelledcount"));
					invoicecount = crs.getDouble("invoicecount");
					invoice_count = count(invoicecount);
					recieptcount = crs.getDouble("recieptcount");
					reciept_count = count(recieptcount);
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateOpenCount() {
		try {
			String StrSqljoin = "", StrSqlSOjoin = "";
			if (!brand_id.equals("")
					|| !region_id.equals("")
					|| !branch_id.equals("")
					|| !StrSoe.equals("")
					|| !StrSob.equals("")
					|| !StrModel.equals("")) {
				StrSqlSOjoin += " INNER JOIN  " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id";
			}
			if (!brand_id.equals("") || !region_id.equals("") || !branch_id.equals("")) {
				StrSqljoin += " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id";
			}
			if (include_inactive_exe.equals("0")) {
				StrSqljoin += " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
						+ " AND emp_active = 1";
			}
			StrSql = "SELECT"
					+ " COALESCE ( ( SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ StrSqljoin
					+ " WHERE 1 = 1"
					+ " AND enquiry_status_id = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'enquirystatuscount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ StrSqljoin
					+ " WHERE enquiry_priorityenquiry_id = 1"
					+ " AND enquiry_status_id = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'hotcount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ StrSqljoin
					+ " WHERE enquiry_priorityenquiry_id = 2"
					+ " AND enquiry_status_id = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'warmcount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT enquiry_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ StrSqljoin
					+ " WHERE enquiry_priorityenquiry_id = 3"
					+ " AND enquiry_status_id = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "enquiry_emp_id")
					+ BranchAccess.replace("branch_id", "enquiry_branch_id")
					+ ExeAccess.replace("emp_id", "enquiry_emp_id")
					+ " ), 0 ) AS 'coldcount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT so_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ StrSqlSOjoin
					+ StrSqljoin.replace("enquiry_branch_id", "so_branch_id")
							.replace("enquiry_emp_id", "so_emp_id")
					+ " WHERE 1 = 1"
					+ " AND so_active = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "so_emp_id")
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ " ), 0 ) AS 'bookingstatuscount',"

					+ " COALESCE ( ( SELECT COUNT(DISTINCT so_id)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ StrSqlSOjoin
					+ StrSqljoin.replace("enquiry_branch_id", "so_branch_id")
							.replace("enquiry_emp_id", "so_emp_id")
					+ " WHERE 1 = 1"
					+ " AND so_retail_date != ''"
					+ " AND so_delivered_date = ''"
					+ " AND so_active = '1'"
					+ StrSoe + StrSoe + StrModel.replace("model_id", "enquiry_model_id")
					+ StrSearch.replace("team_id", "enquiry_team_id")
							.replace("emp_id", "so_emp_id")
					+ BranchAccess.replace("branch_id", "so_branch_id")
					+ ExeAccess.replace("emp_id", "so_emp_id")
					+ " ), 0 ) AS 'retailstatuscount'";

			// SOP("StrSql==3333====" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquirystatus_count = CNumeric(crs.getString("enquirystatuscount"));
					hot_count = CNumeric(crs.getString("hotcount"));
					warm_count = CNumeric(crs.getString("warmcount"));
					cold_count = CNumeric(crs.getString("coldcount"));
					bookingstatus_count = CNumeric(crs.getString("bookingstatuscount"));
					retailstatus_count = CNumeric(crs.getString("retailstatuscount"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String count(double value) {
		String amount = "";
		if (value > 9999999) {
			amount = deci.format((value / 10000000)) + " Cr.";
		} else if (value > 999999) {
			amount = deci.format((value / 100000)) + " L";
		} else {
			amount = deci.format(value);
			amount = unescapehtml(IndDecimalFormat(deci.format((value))));
		}

		return amount;

	}
}
