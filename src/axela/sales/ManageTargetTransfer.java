package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class ManageTargetTransfer extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a>&gt;&nbsp;</li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp </li><li><a href=managetargettransfer.jsp>Target Transfer</a>:</li>";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String brand_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public int curryear = 0;
	public String from_year = "", to_year = "";
	public String from_month = "", to_month = "";
	public String[] brand_ids;
	public String year = "";
	public String go = "";

	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				from_year = PadQuotes(request.getParameter("dr_year"));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				go = PadQuotes(request.getParameter("submit_btn"));
				if (from_year.equals("0")) {
					from_year = curryear + "";
				}
				if (go.equals("Submit")) {
					GetValues(request);
					CheckForm();
					if (msg.equals("")) {
						TargetTranfer();
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void CheckForm() {

		msg = "";
		// if (brand_id.equals("")) {
		// msg = msg + "<br>Select Brand!<br>";
		// }
		if (from_year.equals("0")) {
			msg = msg + "<br>Select From Year!";
		}

		if (to_year.equals("0")) {
			msg = msg + "<br>Select To Year!";
		}
		if (from_month.equals("00")) {
			msg = msg + "<br>Select From Month!";
		}

		if (to_month.equals("00")) {
			msg = msg + "<br>Select To Month!";
		}

		if (!from_year.equals("0") && !from_month.equals("0") && !to_year.equals("0") && !to_month.equals("0")) {
			if ((Integer.parseInt(to_year + to_month) <= (Integer.parseInt(from_year + from_month)))) {
				msg = "<br>To Date should be greater than From Date!<br>";
			}
		}
	}

	protected void GetValues(HttpServletRequest request) {

		brand_id = PadQuotes(RetrunSelectArrVal(request, "dr_principal"));
		brand_ids = request.getParameterValues("dr_principal");
		from_year = PadQuotes(request.getParameter("dr_from_year"));
		from_month = PadQuotes(request.getParameter("dr_from_month"));
		to_year = PadQuotes(request.getParameter("dr_to_year"));
		to_month = PadQuotes(request.getParameter("dr_to_month"));
		if (!brand_id.equals("")) {
			StrSearch += "AND branch_brand_id IN(" + brand_id + ")";
		}
	}

	protected void TargetTranfer() {
		try {
			if (msg.equals("")) {
				// delete from target
				StrSql = "DELETE t FROM " + compdb(comp_id) + "axela_sales_target AS t "
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
				if (!brand_id.equals("")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
				}
				StrSql += " WHERE 1=1"
						+ " AND SUBSTR(target_startdate,1,6) = '" + to_year + to_month + "'"
						+ StrSearch;
				updateQuery(StrSql);
				// delete recedue from target model
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_target_model"
						+ " WHERE modeltarget_target_id"
						+ " NOT IN (SELECT target_id FROM " + compdb(comp_id) + "axela_sales_target)";

				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_target"
						+ " (target_emp_id,"
						+ " target_startdate,"
						+ " target_enddate,"
						+ " target_enquiry_count,"
						+ " target_enquiry_calls_count,"
						+ " target_enquiry_meetings_count,"
						+ " target_enquiry_testdrives_count,"
						+ " target_enquiry_hot_count,"
						+ " target_so_count,"
						+ " target_so_min,"
						+ " target_entry_id,"
						+ " target_entry_date)"
						+ " SELECT"
						+ " target_emp_id,"
						+ " '" + to_year + to_month + "01000000" + "',"
						+ " '" + to_year + to_month + "31000000" + "',"
						+ " target_enquiry_count,"
						+ " target_enquiry_calls_count,"
						+ " target_enquiry_meetings_count,"
						+ " target_enquiry_testdrives_count,"
						+ " target_enquiry_hot_count,"
						+ " target_so_count,"
						+ " target_so_min,"
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "'"
						+ " FROM " + compdb(comp_id) + "axela_sales_target"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
				if (!brand_id.equals("")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
				}
				StrSql += " WHERE 1 = 1"
						+ " AND emp_sales = 1"
						+ " AND emp_branch_id != 0"
						+ " AND target_startdate >='" + from_year + from_month + "01000000" + "'"
						+ " AND target_enddate <='" + from_year + from_month + "31000000" + "'"
						+ StrSearch;
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_target_model ("
						+ " modeltarget_model_id,"
						+ " modeltarget_target_id,"
						+ " modeltarget_enquiry_count,"
						+ " modeltarget_enquiry_calls_count,"
						+ " modeltarget_enquiry_meetings_count,"
						+ " modeltarget_enquiry_testdrives_count,"
						+ " modeltarget_enquiry_hot_count,"
						+ " modeltarget_so_count,"
						+ " modeltarget_so_min)"
						+ " SELECT "
						+ " modeltarget_model_id,"
						+ " (SELECT target.target_id FROM " + compdb(comp_id) + "axela_sales_target target "
						+ " WHERE target.target_startdate = '" + to_year + to_month + "01000000" + "' AND target.target_emp_id=emp_id),"
						+ " modeltarget_enquiry_count,"
						+ " modeltarget_enquiry_calls_count,"
						+ " modeltarget_enquiry_meetings_count,"
						+ " modeltarget_enquiry_testdrives_count,"
						+ " modeltarget_enquiry_hot_count,"
						+ " modeltarget_so_count,"
						+ " modeltarget_so_min"
						+ " FROM " + compdb(comp_id) + "axela_sales_target_model"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON target_id = modeltarget_target_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = target_emp_id";
				if (!brand_id.equals("")) {
					StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
				}
				StrSql += " WHERE 1 = 1"
						+ " AND target_startdate >='" + from_year + from_month + "01000000" + "'"
						+ " AND target_enddate <='" + from_year + from_month + "31000000" + "' "
						+ StrSearch;

				updateQuery(StrSql);

				msg = "<br>Target Transfered Successfully!<br>";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}
	public String PopulateYear(String year, int yeartype) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		if (yeartype == 0) {
			for (int i = curryear - 3; i <= curryear + 3; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} else if (yeartype == 1) {
			for (int i = curryear - 1; i <= curryear + 1; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		}
		return Str.toString();
	}
	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=00").append(StrSelectdrop("00", month)).append(">Select</option>");
		Str.append("<option value=01").append(StrSelectdrop("01", month)).append(">January</option>");
		Str.append("<option value=02").append(StrSelectdrop("02", month)).append(">February</option>");
		Str.append("<option value=03").append(StrSelectdrop("03", month)).append(">March</option>");
		Str.append("<option value=04").append(StrSelectdrop("04", month)).append(">April</option>");
		Str.append("<option value=05").append(StrSelectdrop("05", month)).append(">May</option>");
		Str.append("<option value=06").append(StrSelectdrop("06", month)).append(">June</option>");
		Str.append("<option value=07").append(StrSelectdrop("07", month)).append(">July</option>");
		Str.append("<option value=08").append(StrSelectdrop("08", month)).append(">August</option>");
		Str.append("<option value=09").append(StrSelectdrop("09", month)).append(">September</option>");
		Str.append("<option value=10").append(StrSelectdrop("10", month)).append(">October</option>");
		Str.append("<option value=11").append(StrSelectdrop("11", month)).append(">November</option>");
		Str.append("<option value=12").append(StrSelectdrop("12", month)).append(">December</option>");

		return Str.toString();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

}
