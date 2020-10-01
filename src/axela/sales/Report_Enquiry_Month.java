package axela.sales;

//Manjur 13/05/2015
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Enquiry_Month extends Connect {

	public String StrSql = "";
	public String endtime = "", end_time = "";

	public static String msg = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "0", brand_id = "", region_id = "";
	// public String[] team_ids, exe_ids, model_ids;
	public String[] team_ids, exe_ids, model_ids, soe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String BranchAccess = "", dr_branch_id = "";
	public String soe_id = "0", sob_id = "0";
	// public String[] soe_ids;
	public String[] sob_ids;
	public String StrHTML = "", header = "";
	public String modelname = "";
	public String go = "";
	public String ExeAccess = "";
	public String enquiry_hour = "";
	public String enquiry_day = "";
	public String enquiry_month = "";
	public int enquirycount = 0;
	public int salesordercount = 0;
	public int deliverycount = 0;
	public String dr_year = "1";
	public String dr_month = "";
	public String[] x;
	public String[] row;
	public String[] column;
	public String enquiry_count = "";
	public String StrSearch = "";
	public String chart_data = "";
	public static int year;
	public static int month;
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			dr_year = CNumeric(PadQuotes(request.getParameter("dr_traffic")));
			dr_month = PadQuotes(request.getParameter("dr_month"));
			dr_year = PadQuotes(request.getParameter("dr_year"));
			dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
			if (dr_month.equals("")) {
				dr_month = ToShortDate(kknow()).substring(4, 6);
			}
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));

				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					// branch_id = (session.GetSession("emp_branch_id",
					// request)).toString();
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				if (!header.equals("no")) {
					GetValues(request, response);
					CheckForm();
					if (go.equals("Go")) {
						StrSearch = BranchAccess.replace("branch_id", "enquiry_branch_id");

						StrSearch += ExeAccess.replace("emp_id", "enquiry_emp_id");

						if (!exe_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_emp_id in (" + exe_id + ")";
						}
						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							mischeck.exe_branch_id = branch_id;
							StrSearch = StrSearch + " and branch_id IN (" + branch_id + ")";
						}
						if (!team_id.equals("")) {
							mischeck.exe_branch_id = branch_id;
							mischeck.branch_id = branch_id;
							StrSearch = StrSearch + " and teamtrans_team_id in (" + team_id + ")";
						}
						if (!model_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_model_id in (" + model_id + ")";
						}
						if (!soe_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_soe_id in (" + soe_id + ")";
						}
						if (!sob_id.equals("")) {
							StrSearch = StrSearch + " and enquiry_sob_id in (" + sob_id + ")";
						}
						StrSearch = StrSearch + " and SUBSTR(enquiry_date, 1, 6) = SUBSTR('" + dr_year + dr_month + "',1,6)";

						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						// SOP("")
						if (msg.equals("")) {
							dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
							// dr_year =
							// CNumeric(PadQuotes(request.getParameter("dr_traffic")));
							dr_month = PadQuotes(request.getParameter("dr_month"));
							dr_year = PadQuotes(request.getParameter("dr_year"));
							StrHTML = ModelDays();
							StrHTML += SOEDays();
							StrHTML += SOEModel();
							StrHTML += SOBDays();
							StrHTML += SOBModel();

						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		dr_branch_id = PadQuotes(request.getParameter("dr_branch"));
		dr_month = PadQuotes(request.getParameter("dr_month"));
		dr_year = PadQuotes(request.getParameter("dr_year"));
		year = (Integer.parseInt(dr_year));
		if (dr_month.equals("")) {
			dr_month = ToShortDate(kknow()).substring(4, 6);
		}
		month = (Integer.parseInt(dr_month));
		if (!dr_year.equals("")) {
			x = new String[32];
		}
		if (!dr_year.equals("") && !dr_month.equals("")) {
			int temp = DaysInMonth(year, month);
			for (int i = 1; i <= temp; i++) {
				if ((i + "").length() == 1) {
					x[i] = dr_year + dr_month + "0" + i + "000000";
				} else {
					x[i] = dr_year + dr_month + i + "000000";
				}
			}
		}
		if (branch_id.equals("")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");

		// SOP("brand_id-----------" + brand_id);
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
		sob_id = RetrunSelectArrVal(request, "dr_sob");
		sob_ids = request.getParameterValues("dr_sob");

	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("")) {
			msg = msg + "<br>SELECT Branch!<br>";
		}
		if (dr_year.equals("")) {
			msg = msg + "<br>SELECT Year!<br>";
		}
		if (dr_month.equals("")) {
			msg += "<br>SELECT Month!";
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name FROM " + compdb(comp_id) + "axela_sales_team "
					+ " where team_branch_id=" + dr_branch_id + " "
					+ " GROUP BY team_id " + " "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== "+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("team_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">" + (crs.getString("team_name")) + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSOE() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name  FROM " + compdb(comp_id) + "axela_soe "
					+ " WHERE 1=1 "
					+ " GROUP BY soe_id ";
			// SOP("PopulateTeam query ==== "+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("soe_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("soe_id"), soe_ids));
				Str.append(">" + (crs.getString("soe_name")) + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateSOB() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name  FROM " + compdb(comp_id) + "axela_sob "
					+ " WHERE 1=1 "
					+ " GROUP BY sob_id ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("sob_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("sob_id"), sob_ids));
				Str.append(">" + (crs.getString("sob_name")) + "</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			String exe = "";
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1' and emp_sales='1' and "
					+ " (emp_branch_id = "
					+ dr_branch_id
					+ " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id FROM axela_emp_branch empbr"
					+ " WHERE axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = "
					+ dr_branch_id
					+ ")) "
					+ ExeAccess + "";

			if (!team_id.equals("")) {
				StrSql = StrSql + " and teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql + " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOP("StrSql===="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=dr_executive id=dr_executive class=selectbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"))
						.append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option> \n");
			}
			Str.append("</SELECT>");
			crs.close();
			return exe = Str.toString();
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			String SqlStr = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_model_id = model_id"
					+ " GROUP BY model_id "
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(SqlStr, 0);
			// SOP("SqlStr in PopulateCountry==========" + SqlStr);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">").append(crs.getString("model_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("DD Motors== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateYears() {

		String year = ToShortDate(kknow()).substring(0, 4);
		StringBuilder years = new StringBuilder();
		for (int i = Integer.parseInt(year); i > Integer.parseInt(year) - 3; i--) {
			years.append("<option value = " + doublenum(i) + ""
					+ StrSelectdrop(doublenum(i), dr_year) + ">" + i
					+ "</option>\n");
		}
		return years.toString();
	}

	public String PopulateMonths() {
		String months = "";

		months += "<option value=01" + StrSelectdrop(doublenum(1), dr_month)
				+ ">January</option>\n";
		months += "<option value=02" + StrSelectdrop(doublenum(2), dr_month)
				+ ">February</option>\n";
		months += "<option value=03" + StrSelectdrop(doublenum(3), dr_month)
				+ ">March</option>\n";
		months += "<option value=04" + StrSelectdrop(doublenum(4), dr_month)
				+ ">April</option>\n";
		months += "<option value=05" + StrSelectdrop(doublenum(5), dr_month)
				+ ">May</option>\n";
		months += "<option value=06" + StrSelectdrop(doublenum(6), dr_month)
				+ ">June</option>\n";
		months += "<option value=07" + StrSelectdrop(doublenum(7), dr_month)
				+ ">July</option>\n";
		months += "<option value=08" + StrSelectdrop(doublenum(8), dr_month)
				+ ">August</option>\n";
		months += "<option value=09" + StrSelectdrop(doublenum(9), dr_month)
				+ ">September</option>\n";
		months += "<option value=10" + StrSelectdrop(doublenum(10), dr_month)
				+ ">October</option>\n";
		months += "<option value=11" + StrSelectdrop(doublenum(11), dr_month)
				+ ">November</option>\n";
		months += "<option value=12" + StrSelectdrop(doublenum(12), dr_month)
				+ ">December</option>\n";
		return months;
	}

	public String ModelDays() {
		int model_row_total = 0;
		int grandtotal = 0;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT model_id, model_name,";
		int monthdays = DaysInMonth(year, month);
		for (int i = 1; i <= monthdays; i++) {
			StrSql = StrSql + " sum(if(enquiry_date = " + x[i] + ", 1, 0)) as calday_" + i + ",";
		}
		StrSql += " 'test' FROM " + compdb(comp_id) + "axela_inventory_item_model"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_model_id = model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
				+ " WHERE 1=1"
				+ " AND model_sales =1"
				+ " AND model_active =1 " + StrSearch;
		StrSql += " GROUP BY model_id"
				+ " ORDER BY model_name";
		// SOP("StrSql==Daily--------" + StrSql);
		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<th colspan=\"" + (monthdays + 2) + "\" align=\"center\" valign=\"top\">Model By Month</th>");
				Str.append("<tr align=center>\n");
				int days = DaysInMonth(year, month);
				Str.append("<th>Model/Days</th>");
				for (int i = 1; i <= days; i++) {
					count++;
					Str.append("<th>" + i + "</th>");
				}
				Str.append("<th>Total</th>");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				HashMap<String, Integer> day_column_total = new HashMap<>();
				for (int i = 1; i <= days; i++) {
					day_column_total.put(i + "", 0);
				}
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><b>" + crs.getString("model_name") + "</b></td>");
					for (int i = 1; i <= days; i++) {
						model_row_total += crs.getInt("calday_" + i);
						Str.append("<td align=right>" + crs.getString("calday_" + i) + "</td>");
						day_column_total.put(i + "", day_column_total.get(i + "") + crs.getInt("calday_" + i));
					}
					// coltotal[k] = day_row_total + "";
					grandtotal += model_row_total;
					Str.append("<td align=right><b>" + model_row_total + "</b></td>");
					Str.append("</tr>");
					model_row_total = 0;
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				for (int i = 1; i <= days; i++) {
					Str.append("<td align=right><b>" + day_column_total.get(i + "") + "</b></td>");
				}
				Str.append("<td align=right><b>" + grandtotal + "</b></td>");
				Str.append("</tr>");
				// SOP("grandtotal=====" + grandtotal);
				Str.append("</br></br></br></br></br>");

			} else {
				Str.append("</br></br></br></br></br><center><font color=red><b>No Records Found!</b></font></center>");
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String SOEDays() {
		int soe_row_total = 0;
		int grandtotal = 0;

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT soe_id, soe_name,";
		int monthdays = DaysInMonth(year, month);
		for (int i = 1; i <= monthdays; i++) {
			StrSql = StrSql + " sum(if(enquiry_date = " + x[i] + ", 1, 0)) as calday_" + i + ",";
		}
		StrSql += " 'test' FROM " + compdb(comp_id) + "axela_soe"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_soe_id = soe_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
				+ " WHERE 1=1" + StrSearch;
		StrSql += " GROUP BY soe_id"
				+ " ORDER BY soe_name";
		// SOP("StrSql==SOE-------------" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<th colspan=\"" + (monthdays + 2) + "\" align=\"center\" valign=\"top\">SOE By Month</th>");
				Str.append("<tr align=center>\n");
				Str.append("<th>SOE/Days</th>");
				for (int i = 1; i <= monthdays; i++) {
					Str.append("<th>" + i + "</th>");
				}
				Str.append("<th>Total</th>");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				HashMap<String, Integer> day_column_total = new HashMap<>();
				for (int i = 1; i <= monthdays; i++) {
					day_column_total.put(i + "", 0);
				}
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><b>" + crs.getString("soe_name") + "</b></td>");
					for (int i = 1; i <= monthdays; i++) {
						soe_row_total += crs.getInt("calday_" + i);
						Str.append("<td align=right>" + crs.getString("calday_" + i) + "</td>");
						day_column_total.put(i + "", day_column_total.get(i + "") + crs.getInt("calday_" + i));
					}
					grandtotal += soe_row_total;
					Str.append("<td align=right><b>" + soe_row_total + "</b></td>");
					Str.append("</tr>");
					soe_row_total = 0;
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				for (int i = 1; i <= monthdays; i++) {
					Str.append("<td align=right><b>" + day_column_total.get(i + "") + "</b></td>");
				}
				Str.append("<td align=right><b>" + grandtotal + "</b></td>");
				Str.append("</tr>");
				// SOP("grandtotal=====" + grandtotal);
				Str.append("</br></br></br></br></br>");

			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String SOEModel() throws SQLException {
		int soerowtotal = 0;
		int grandtotal = 0;
		int rowcount = 0;
		HashMap<Integer, Integer> soemodelcolumntotal = new HashMap<>();
		// SOP("brand_id---1-------" + brand_id);
		StringBuilder Str = new StringBuilder();
		StrSql = "";
		String StrModel = "SELECT model_id, model_name FROM " + compdb(comp_id) + "axela_inventory_item_model "
				+ " WHERE 1=1 ";
		if (!brand_id.equals("")) {
			StrModel += " AND model_brand_id IN ( " + brand_id + ")";
		}
		StrModel += " AND model_sales = 1 AND model_active = 1"
				+ " ORDER BY model_name";
		// SOP("StrModel----------" + StrModel);

		CachedRowSet crs, crs1 = null;
		crs1 = processQuery(StrModel, 0);
		while (crs1.next()) {
			rowcount++;
			StrSql = StrSql + " SUM(if(enquiry_model_id = " + crs1.getString("model_id") + ", 1, 0)) as 'model-" + rowcount + "',";
			soemodelcolumntotal.put(rowcount, 0);
		}

		StrSql = "SELECT soe_id, soe_name, " + StrSql + " 'test'"
				+ " FROM " + compdb(comp_id) + "axela_soe"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_soe_id = soe_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
				+ " WHERE 1=1" + StrSearch;
		StrSql += " GROUP BY soe_id"
				+ " ORDER BY soe_name";
		// SOP("StrSql==SOE==mode-------------" + StrSqlBreaker(StrSql));
		try {
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<th colspan=\"" + (rowcount + 2) + "\" align=\"center\" valign=\"top\">SOE By Model</th>");
				Str.append("<tr align=center>\n");
				Str.append("<th>SOE/Models</th>");
				crs1.beforeFirst();
				while (crs1.next()) {
					Str.append("<th>" + crs1.getString("model_name") + "</th>");
				}
				// crs1.close();
				Str.append("<th>Total</th>");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><b>" + crs.getString("soe_name") + "</b></td>");
					for (int i = 1; i <= rowcount; i++) {
						soerowtotal += crs.getInt("model-" + i);
						Str.append("<td align=right>" + crs.getInt("model-" + i) + "</td>");
						soemodelcolumntotal.put(i, soemodelcolumntotal.get(i) + crs.getInt("model-" + i));
					}
					grandtotal += soerowtotal;
					Str.append("<td align=right><b>" + soerowtotal + "</b></td>");
					Str.append("</tr>");
					soerowtotal = 0;
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				for (int i = 1; i <= rowcount; i++) {
					Str.append("<td align=right><b>" + soemodelcolumntotal.get(i) + "</b></td>");
				}
				Str.append("<td align=right><b>" + grandtotal + "</b></td>");
				Str.append("</tr>");
				// SOP("grandtotal=====" + grandtotal);
				Str.append("</br></br></br></br></br>");
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();

	}

	public String SOBDays() {
		int sob_row_total = 0;
		int grandtotal = 0;

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT sob_id, sob_name,";
		int monthdays = DaysInMonth(year, month);
		for (int i = 1; i <= monthdays; i++) {
			StrSql = StrSql + " sum(if(enquiry_date = " + x[i] + ", 1, 0)) as calday_" + i + ",";
		}
		StrSql += " 'test' FROM " + compdb(comp_id) + "axela_sob"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_sob_id = sob_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
				+ " WHERE 1=1" + StrSearch;
		if (!sob_id.equals("")) {
			StrSql += " and sob_id  in ( " + sob_id + ")";
		}
		StrSql += " GROUP BY sob_name"
				+ " ORDER BY sob_id";
		// SOP("StrSql==SOB-----------" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = null;
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<th colspan=\"" + (monthdays + 2) + "\" align=\"center\" valign=\"top\">SOB By Month</th>");
				Str.append("<tr align=center>\n");
				Str.append("<th>SOB/Days</th>");
				for (int i = 1; i <= monthdays; i++) {
					Str.append("<th>" + i + "</th>");
				}
				Str.append("<th>Total</th>");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				HashMap<String, Integer> day_column_total = new HashMap<>();
				for (int i = 1; i <= monthdays; i++) {
					day_column_total.put(i + "", 0);
				}
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><b>" + crs.getString("sob_name") + "</b></td>");
					for (int i = 1; i <= monthdays; i++) {
						sob_row_total += crs.getInt("calday_" + i);
						Str.append("<td align=right>" + crs.getString("calday_" + i) + "</td>");
						day_column_total.put(i + "", day_column_total.get(i + "") + crs.getInt("calday_" + i));
					}
					// coltotal[k] = day_row_total + "";
					grandtotal += sob_row_total;
					Str.append("<td align=right><b>" + sob_row_total + "</b></td>");
					Str.append("</tr>");
					sob_row_total = 0;
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				for (int i = 1; i <= monthdays; i++) {
					Str.append("<td align=right><b>" + day_column_total.get(i + "") + "</b></td>");
				}
				Str.append("<td align=right><b>" + grandtotal + "</b></td>");
				Str.append("</tr>");
				// SOP("grandtotal=====" + grandtotal);
				Str.append("</br></br></br></br></br>");

			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String SOBModel() throws SQLException {
		int sobrowtotal = 0;
		int grandtotal = 0;
		int rowcount = 0;
		HashMap<Integer, Integer> sobmodelcolumntotal = new HashMap<>();
		// SOP("princip/al_id---------" + brand_id);
		StringBuilder Str = new StringBuilder();
		StrSql = "";
		String StrModel = "SELECT model_id, model_name FROM " + compdb(comp_id) + "axela_inventory_item_model "
				+ " INNER JOIN axela_brand on brand_id = model_brand_id"
				+ " WHERE 1=1 ";
		if (!brand_id.equals("")) {
			StrModel += " AND model_brand_id IN ( " + brand_id + ")";
		}
		StrModel += " AND model_sales = 1 "
				+ " AND model_active = 1"
				+ " ORDER BY model_name";
		// SOP("StrModel--------" + StrModel);
		CachedRowSet crs, crs1 = null;
		crs1 = processQuery(StrModel, 0);
		while (crs1.next()) {
			rowcount++;
			StrSql = StrSql + " SUM(if(enquiry_model_id = " + crs1.getString("model_id") + ", 1, 0)) as 'model-" + rowcount + "',";
			sobmodelcolumntotal.put(rowcount, 0);
		}

		StrSql = "SELECT sob_id, sob_name, " + StrSql + " 'test'"
				+ " FROM " + compdb(comp_id) + "axela_sob"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_sob_id = sob_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
				+ " WHERE 1=1" + StrSearch;

		StrSql += " GROUP BY sob_id"
				+ " ORDER BY sob_name";
		// SOP("StrSql==sob=111=model-------" + StrSqlBreaker(StrSql));
		try {
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-bordered\">\n");
				Str.append("\n<table border=\"2\" class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<th colspan=\"" + (rowcount + 2) + "\" align=\"center\" valign=\"top\">SOB By Model</th>");
				Str.append("<tr align=center>\n");
				Str.append("<th>SOB/Models</th>");
				crs1.beforeFirst();
				while (crs1.next()) {
					Str.append("<th>" + crs1.getString("model_name") + "</th>");
				}
				// crs1.close();
				Str.append("<th>Total</th>");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><b>" + crs.getString("sob_name") + "</b></td>");
					for (int i = 1; i <= rowcount; i++) {
						sobrowtotal += crs.getInt("model-" + i);
						Str.append("<td align=right>" + crs.getInt("model-" + i) + "</td>");
						sobmodelcolumntotal.put(i, sobmodelcolumntotal.get(i) + crs.getInt("model-" + i));
					}
					grandtotal += sobrowtotal;
					Str.append("<td align=right><b>" + sobrowtotal + "</b></td>");
					Str.append("</tr>");
					sobrowtotal = 0;
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				for (int i = 1; i <= rowcount; i++) {
					Str.append("<td align=right><b>" + sobmodelcolumntotal.get(i) + "</b></td>");
				}
				Str.append("<td align=right><b>" + grandtotal + "</b></td>");
				Str.append("</tr>");
				// SOP("grandtotal=====" + grandtotal);
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Sterling ===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

}
