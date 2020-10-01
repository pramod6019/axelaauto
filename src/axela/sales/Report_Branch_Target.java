package axela.sales;
// modified - 24, 26, 27,28 august 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Branch_Target extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids;
	public String brand_id = "", region_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "0";
	public String ExeAccess = "";
	public String dr_month = "";
	public String dr_year = "";
	public String branch_name = "";
	public static int year;
	public static int month;
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String principalname = "";
	public String branchname = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				// // SOP("===="+getPercentage(100, 10));
				// SOP("header = " + header);
				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				if (!header.equals("no")) {
					GetValues(request, response);
					if (go.equals("Go")) {

						StrSearch = BranchAccess + ExeAccess;
						
						if (!brand_id.equals("")) {
							StrSearch = StrSearch + " and branch_brand_id in (" + brand_id + ")";
						}
						if (!branch_id.equals("")) {
							StrSearch = StrSearch + " and branch_id in (" + branch_id + ")";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id in (" + region_id + ") ";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListBranchTarget(branch_id, dr_totalby, comp_id);
						}
					}
				} else {
					// dr_branch_id =
					// PadQuotes(request.getParameter("dr_branch_id"));

					chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
					StrHTML = ListBranchTarget(dr_branch_id, dr_totalby, comp_id);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		dr_month = PadQuotes(request.getParameter("dr_month"));
		dr_year = PadQuotes(request.getParameter("dr_year"));
		if (dr_month.equals("")) {
			dr_month = ToShortDate(kknow()).substring(4, 6);
		}

		if (dr_year.equals("")) {
			dr_year = ToShortDate(kknow()).substring(0, 4);
		}

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");

		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");

		// chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
	}

	public String ListBranchTarget(String dr_branch_id, String dr_totalby, String comp_id) {

		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();
		StrSql = "SELECT branch_id, brand_id, brand_name, branch_name,"

				+ " @enquirytarget:= COALESCE(branchtarget_enquiry_count, 0) AS enquirytarget,"
				+ " @enquiryachieved:= COALESCE((SELECT COUNT(enquiry_id)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
				+ " WHERE enquiry_branch_id = branch_id AND SUBSTR(enquiry_date, 1, 6) = " + dr_year + dr_month + "),0) AS enquiryachieved,"
				+ " FORMAT(COALESCE((@enquiryachieved / @enquirytarget) * 100, 0),2) AS enquiryperc,"

				+ " @homevisittarget := COALESCE(branchtarget_homevisit_count,0) AS homevisittarget,"
				+ " @homevisitachieved:= COALESCE ((SELECT COALESCE(count(followup_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ "	INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
				+ " WHERE enquiry_branch_id = branch_id AND SUBSTR(followup_followup_time, 1, 6) = " + dr_year + dr_month
				+ " AND followup_desc LIKE 'Home Visit Done%'),0) AS homevisitachieved,"
				+ "	FORMAT(COALESCE((@homevisitachieved / @homevisittarget) * 100,0),2) AS homevisitperc,"

				+ " @testdrivetarget := COALESCE (branchtarget_testdrives_count,0) AS testdrivetarget,"
				+ " @testdriveachieved:= COALESCE ((SELECT COALESCE(count(testdrive_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ " WHERE enquiry_branch_id = branch_id AND SUBSTR(testdrive_time, 1, 6) = " + dr_year + dr_month
				+ " AND testdrive_fb_taken = 1),0) AS testdriveachieved,"
				+ " FORMAT(COALESCE((@testdriveachieved / @testdrivetarget) * 100,0),2) AS testdriveperc,"

				+ " @bookingtarget := COALESCE(branchtarget_bookings_count,0) AS bookingtarget,"
				+ " @bookingachieved:= COALESCE ((SELECT COALESCE(count(so_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1'"
				+ " AND SUBSTR(so_date, 1, 6) = " + dr_year + dr_month + " ),0) AS bookingachieved,"
				+ " FORMAT(COALESCE((@bookingachieved / @bookingtarget) * 100,0),2) AS bookingperc,"

				+ " @deliverytarget:= COALESCE(branchtarget_delivery_count,0) AS deliverytarget,"
				+ " @deliveryachieved:= COALESCE ((SELECT COALESCE(COUNT(so_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1' AND so_delivered_date = ''"
				+ " AND SUBSTR(so_date, 1, 6) = " + dr_year + dr_month + " ),0) AS deliveryachieved,"
				+ " FORMAT(COALESCE((@deliveryachieved / @deliverytarget) * 100,0),2) AS deliveryperc,"

				+ " @accessoriestarget:= COALESCE(branchtarget_accessories_amount,0) AS accessoriestarget,"
				+ " @accessoriesachieved:= COALESCE ((SELECT COALESCE(SUM(so_mga_amount), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1'"
				+ " AND SUBSTR(so_delivered_date, 1, 6) = " + dr_year + dr_month + " ),0) AS accessoriesachieved,"
				+ " FORMAT(COALESCE((@accessoriesachieved / @accessoriestarget) * 100,0),2) AS accessoriesperc,"

				+ " @insurancetarget:= COALESCE(branchtarget_insurance_count,0) AS insurancetarget,"
				+ " @insuranceachieved:= COALESCE ((SELECT COALESCE(COUNT(DISTINCT so_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1' AND so_insur_amount > 0"
				+ " AND SUBSTR(so_delivered_date, 1, 6) = " + dr_year + dr_month + " ),0) AS insuranceachieved,"
				+ " FORMAT(COALESCE((@insuranceachieved / @insurancetarget) * 100,0),2) AS insuranceperc,"

				+ " @ewtarget:= COALESCE(branchtarget_ew_count,0) AS ewtarget,"
				+ " @ewachieved:= COALESCE ((SELECT COALESCE(COUNT(DISTINCT so_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1' AND so_insur_amount > 0"
				+ " AND SUBSTR(so_delivered_date, 1, 6) = " + dr_year + dr_month + " ),0) AS ewachieved,"
				+ " FORMAT(COALESCE((@ewachieved / @ewtarget) * 100,0),2) AS ewperc,"

				+ " @fincasestarget:= COALESCE(branchtarget_fincases_count,0) AS fincasestarget,"
				+ " @fincasesachieved:= COALESCE ((SELECT COALESCE(COUNT(DISTINCT so_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1' AND so_finance_amt > 0"
				+ " AND SUBSTR(so_delivered_date, 1, 6) = " + dr_year + dr_month + " ),0) AS fincasesachieved,"
				+ " FORMAT(COALESCE((@fincasesachieved / @fincasestarget) * 100,0),2) AS fincasesperc,"

				+ " @exetarget:= COALESCE(branchtarget_exchange_count,0) AS exetarget,"
				+ " @exeachieved:= COALESCE ((SELECT COALESCE(COUNT(DISTINCT so_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id and soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND so_active='1' AND so_exchange_amount > 0"
				+ " AND SUBSTR(so_delivered_date, 1, 6) = " + dr_year + dr_month + " ),0) AS exeachieved,"
				+ " FORMAT(COALESCE((@exeachieved / @exetarget) * 100,0),2) AS exeperc,"

				+ " @evaltarget:= COALESCE(branchtarget_evaluation_count,0) AS evaltarget,"
				+ " @evalachieved:= COALESCE ((SELECT COALESCE(COUNT(enquiry_id), 0)"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE 1=1 AND enquiry_branch_id = branch_id AND enquiry_evaluation = 1"
				+ " AND SUBSTR(enquiry_date, 1, 6) = " + dr_year + dr_month + " ),0) AS evalachieved,"
				+ " FORMAT(COALESCE((@evalachieved / @evaltarget) * 100,0),2) AS evalperc"

				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN axela_brand ON brand_id = branch_brand_id"
				// + " INNER JOIN  " + compdb(comp_id) +
				// "axela_branch_region ON region_id = branch_region_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_target_branch ON branch_id = branchtarget_branch_id"
				+ " AND SUBSTR(branchtarget_month,1,6) = " + dr_year + doublenum(Integer.parseInt(dr_month))
				+ " WHERE 1=1 " + StrSearch
				+ " AND branch_active = 1 "
				+ " GROUP BY branch_id "
				+ " ORDER BY brand_name, branch_name";
		// SOP("report bransch target======== " + StrSqlBreaker(StrSql));
		try {

			int enquirytarget = 0;
			int enquiryachieved = 0;
			float enquiryperc = 0.0f;
			int homevisittarget = 0;
			int homevisitachieved = 0;
			float homevisitperc = 0.0f;
			int testdrivetarget = 0;
			int testdriveachieved = 0;
			float testdriveperc = 0.0f;
			int bookingtarget = 0;
			int bookingachieved = 0;
			float bookingperc = 0.0f;
			int deliverytarget = 0;
			int deliveryachieved = 0;
			float deliveryperc = 0.0f;
			int accessoriestarget = 0;
			int accessoriesachieved = 0;
			float accessoriesperc = 0.0f;
			int insurancetarget = 0;
			int insuranceachieved = 0;
			float insuranceperc = 0.0f;
			int ewtarget = 0;
			int ewachieved = 0;
			float ewperc = 0.0f;
			int fincasestarget = 0;
			int fincasesachieved = 0;
			float fincasesperc = 0.0f;
			int exetarget = 0;
			int exeachieved = 0;
			float exeperc = 0.0f;
			int evaltarget = 0;
			int evalachieved = 0;
			float evalperc = 0.0f;
			// ====principal count variables===//

			int principle_enquirytarget = 0;
			int principle_enquiryachieved = 0;
			float principle_enquiryperc = 0.0f;
			int principle_homevisittarget = 0;
			int principle_homevisitachieved = 0;
			float principle_homevisitperc = 0.0f;
			int principle_testdrivetarget = 0;
			int principle_testdriveachieved = 0;
			float principle_testdriveperc = 0.0f;
			int principle_bookingtarget = 0;
			int principle_bookingachieved = 0;
			float principle_bookingperc = 0.0f;
			int principle_deliverytarget = 0;
			int principle_deliveryachieved = 0;
			float principle_deliveryperc = 0.0f;
			int principle_accessoriestarget = 0;
			int principle_accessoriesachieved = 0;
			float principle_accessoriesperc = 0.0f;
			int principle_insurancetarget = 0;
			int principle_insuranceachieved = 0;
			float principle_insuranceperc = 0.0f;
			int principle_ewtarget = 0;
			int principle_ewachieved = 0;
			float principle_ewperc = 0.0f;
			int principle_fincasestarget = 0;
			int principle_fincasesachieved = 0;
			float principle_fincasesperc = 0.0f;
			int principle_exetarget = 0;
			int principle_exeachieved = 0;
			float principle_exeperc = 0.0f;
			int principle_evaltarget = 0;
			int principle_evalachieved = 0;
			float principle_evalperc = 0.0f;

			int totalenquirytarget = 0;
			int totalenquiryachieved = 0;
			float totalenquiryperc = 0.0f;
			int totalhomevisittarget = 0;
			int totalhomevisitachieved = 0;
			float totalhomevisitperc = 0.0f;
			int totaltestdrivetarget = 0;
			int totaltestdriveachieved = 0;
			float totaltestdriveperc = 0.0f;
			int totalbookingtarget = 0;
			int totalbookingachieved = 0;
			float totalbookingperc = 0.0f;
			int totaldeliverytarget = 0;
			int totaldeliveryachieved = 0;
			float totaldeliveryperc = 0.0f;
			int totalaccessoriestarget = 0;
			int totalaccessoriesachieved = 0;
			float totalaccessoriesperc = 0.0f;
			int totalinsurancetarget = 0;
			int totalinsuranceachieved = 0;
			float totalinsuranceperc = 0.0f;
			int totalewtarget = 0;
			int totalewachieved = 0;
			float totalewperc = 0.0f;
			int totalfincasestarget = 0;
			int totalfincasesachieved = 0;
			float totalfincasesperc = 0.0f;
			int totalexetarget = 0;
			int totalexeachieved = 0;
			float totalexeperc = 0.0f;
			int totalevaltarget = 0;
			int totalevalachieved = 0;
			float totalevalperc = 0.0f;
			String check_brand_id = "", check_principalttl_id = "";
			String check_branch_id = "", check_branchttl_id = "";

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-responsive table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				if (dr_totalby.equals("1")) {
					Str.append("<th align=center data-hide=\"phone, tablet\">Branch</th>\n");
				}
				if (dr_totalby.equals("2")) {
					Str.append("<th align=center data-hide=\"phone, tablet\">Brand</th>\n");
				}

				// Str.append("<th align=center>Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Enquiry</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Home Visit</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Test Drive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Booking</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Delivery</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Accessories</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Insurance</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>EW</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Fin Cases</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Exchange</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Evaluation</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th align=center>&nbsp;</th>\n");
				for (int i = 1; i <= 11; i++) {
					Str.append("<td>T</td>\n");
					Str.append("<td>A</td>\n");
					Str.append("<td>%</td>\n");
				}
				Str.append("</tr>\n");

				int principalcount = 0;
				while (crs.next()) {

					enquirytarget = crs.getInt("enquirytarget");
					enquiryachieved = crs.getInt("enquiryachieved");
					enquiryperc = (int) crs.getDouble("enquiryperc");
					homevisittarget = crs.getInt("homevisittarget");
					homevisitachieved = crs.getInt("homevisitachieved");
					homevisitperc = (int) crs.getDouble("homevisitperc");
					testdrivetarget = crs.getInt("testdrivetarget");
					testdriveachieved = crs.getInt("testdriveachieved");
					testdriveperc = (int) crs.getDouble("testdriveperc");
					bookingtarget = crs.getInt("bookingtarget");
					bookingachieved = crs.getInt("bookingachieved");
					bookingperc = (int) crs.getDouble("bookingperc");
					deliverytarget = crs.getInt("deliverytarget");
					deliveryachieved = crs.getInt("deliveryachieved");
					deliveryperc = (int) crs.getDouble("deliveryperc");
					accessoriestarget = crs.getInt("accessoriestarget");
					accessoriesachieved = crs.getInt("accessoriesachieved");
					accessoriesperc = (int) crs.getDouble("accessoriesperc");
					insurancetarget = crs.getInt("insurancetarget");
					insuranceachieved = crs.getInt("insuranceachieved");
					insuranceperc = (int) crs.getDouble("insuranceperc");
					ewtarget = crs.getInt("ewtarget");
					ewachieved = crs.getInt("ewachieved");
					ewperc = (int) crs.getDouble("ewperc");
					fincasestarget = crs.getInt("fincasestarget");
					fincasesachieved = crs.getInt("fincasesachieved");
					fincasesperc = (int) crs.getDouble("fincasesperc");
					exetarget = crs.getInt("exetarget");
					exeachieved = crs.getInt("exeachieved");
					exeperc = (int) crs.getDouble("exeperc");
					evaltarget = crs.getInt("evaltarget");
					evalachieved = crs.getInt("evalachieved");
					evalperc = (int) crs.getDouble("evalperc");

					principle_enquirytarget += crs.getInt("enquirytarget");
					principle_enquiryachieved += crs.getInt("enquiryachieved");
					principle_enquiryperc += (int) crs.getDouble("enquiryperc");
					principle_homevisittarget += crs.getInt("homevisittarget");
					principle_homevisitachieved += crs.getInt("homevisitachieved");
					principle_homevisitperc += (int) crs.getDouble("homevisitperc");
					principle_testdrivetarget += crs.getInt("testdrivetarget");
					principle_testdriveachieved += crs.getInt("testdriveachieved");
					principle_testdriveperc += (int) crs.getDouble("testdriveperc");
					principle_bookingtarget += crs.getInt("bookingtarget");
					principle_bookingachieved += crs.getInt("bookingachieved");
					principle_bookingperc += (int) crs.getDouble("bookingperc");
					principle_deliverytarget += crs.getInt("deliverytarget");
					principle_deliveryachieved += crs.getInt("deliveryachieved");
					principle_deliveryperc += (int) crs.getDouble("deliveryperc");
					principle_accessoriestarget += crs.getInt("accessoriestarget");
					principle_accessoriesachieved += crs.getInt("accessoriesachieved");
					principle_accessoriesperc += (int) crs.getDouble("accessoriesperc");
					principle_insurancetarget += crs.getInt("insurancetarget");
					principle_insuranceachieved += crs.getInt("insuranceachieved");
					principle_insuranceperc += (int) crs.getDouble("insuranceperc");
					principle_ewtarget += crs.getInt("ewtarget");
					principle_ewachieved += crs.getInt("ewachieved");
					principle_ewperc += (int) crs.getDouble("ewperc");
					principle_fincasestarget += crs.getInt("fincasestarget");
					principle_fincasesachieved += crs.getInt("fincasesachieved");
					principle_fincasesperc += (int) crs.getDouble("fincasesperc");
					principle_exetarget += crs.getInt("exetarget");
					principle_exeachieved += crs.getInt("exeachieved");
					principle_exeperc += (int) crs.getDouble("exeperc");
					principle_evaltarget += crs.getInt("evaltarget");
					principle_evalachieved += crs.getInt("evalachieved");
					principle_evalperc += (int) crs.getDouble("evalperc");

					totalenquirytarget += enquirytarget;
					totalenquiryachieved += enquiryachieved;
					totalenquiryperc += enquiryperc;
					totalhomevisittarget += homevisittarget;
					totalhomevisitachieved += homevisitachieved;
					totalhomevisitperc += homevisitperc;
					totaltestdrivetarget += testdrivetarget;
					totaltestdriveachieved += testdriveachieved;
					totaltestdriveperc += testdriveperc;
					totalbookingtarget += bookingtarget;
					totalbookingachieved += bookingachieved;
					totalbookingperc += bookingperc;
					totaldeliverytarget += deliverytarget;
					totaldeliveryachieved += deliveryachieved;
					totaldeliveryperc += deliveryperc;
					totalaccessoriestarget += accessoriestarget;
					totalaccessoriesachieved += accessoriesachieved;
					totalaccessoriesperc += accessoriesperc;
					totalinsurancetarget += insurancetarget;
					totalinsuranceachieved += insuranceachieved;
					totalinsuranceperc += insuranceperc;
					totalewtarget += ewtarget;
					totalewachieved += ewachieved;
					totalewperc += ewperc;
					totalfincasestarget += fincasestarget;
					totalfincasesachieved += fincasesachieved;
					totalfincasesperc += fincasesperc;
					totalexetarget += exetarget;
					totalexeachieved += exeachieved;
					totalexeperc += exeperc;
					totalevaltarget += evaltarget;
					totalevalachieved += evalachieved;
					totalevalperc += evalperc;

					if (check_principalttl_id.equals("")) {
						check_principalttl_id = crs.getString("brand_id");
						principalname = crs.getString("brand_name");
					}
					// if (check_branchttl_id.equals("")) {
					// check_branchttl_id = crs.getString("branch_id");
					// branchname = crs.getString("branch_name");
					// }

					if (dr_totalby.equals("1")) {
						Str.append("<tr align=left valign=top>\n");
						Str.append("<td align=right>").append(crs.getString("branch_name")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("enquirytarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("enquiryachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("enquiryperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("homevisittarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("homevisitachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("homevisitperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("testdrivetarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("testdriveachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("testdriveperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("bookingtarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("bookingachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("bookingperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("deliverytarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("deliveryachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("deliveryperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("accessoriestarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("accessoriesachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("accessoriesperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("insurancetarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("insuranceachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("insuranceperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("ewtarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("ewachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("ewperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("fincasestarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("fincasesachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("fincasesperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("exetarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("exeachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("exeperc")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("evaltarget")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("evalachieved")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString("evalperc")).append("</td>\n");
						Str.append("</tr>");
					}

					if (!check_principalttl_id.equals(crs.getString("brand_id"))) {
						principalcount++;
						Str.append("<tr align=left valign=top>\n");
						Str.append("<td align=right><b>").append(principalname).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_enquirytarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_enquiryachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_enquiryperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_homevisittarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_homevisitachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_homevisitperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_testdrivetarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_testdriveachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_testdriveperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_bookingtarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_bookingachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_bookingperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_deliverytarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_deliveryachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_deliveryperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_accessoriestarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_accessoriesachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_accessoriesperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_insurancetarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_insuranceachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_insuranceperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_ewtarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_ewachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_ewperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_fincasestarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_fincasesachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_fincasesperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_exetarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_exeachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_exeperc).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_evaltarget).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_evalachieved).append("</b></td>\n");
						Str.append("<td align=right><b>").append(principle_evalperc).append("</b></td>\n");
						Str.append("</tr>");
						check_principalttl_id = crs.getString("brand_id");
						principalname = crs.getString("brand_name");
						principle_enquirytarget = 0;
						principle_enquiryachieved = 0;
						principle_enquiryperc = 0.0f;
						principle_homevisittarget = 0;
						principle_homevisitachieved = 0;
						principle_homevisitperc = 0.0f;
						principle_testdrivetarget = 0;
						principle_testdriveachieved = 0;
						principle_testdriveperc = 0.0f;
						principle_bookingtarget = 0;
						principle_bookingachieved = 0;
						principle_bookingperc = 0.0f;
						principle_deliverytarget = 0;
						principle_deliveryachieved = 0;
						principle_deliveryperc = 0.0f;
						principle_accessoriestarget = 0;
						principle_accessoriesachieved = 0;
						principle_accessoriesperc = 0.0f;
						principle_insurancetarget = 0;
						principle_insuranceachieved = 0;
						principle_insuranceperc = 0.0f;
						principle_ewtarget = 0;
						principle_ewachieved = 0;
						principle_ewperc = 0.0f;
						principle_fincasestarget = 0;
						principle_fincasesachieved = 0;
						principle_fincasesperc = 0.0f;
						principle_exetarget = 0;
						principle_exeachieved = 0;
						principle_exeperc = 0.0f;
						principle_evaltarget = 0;
						principle_evalachieved = 0;
						principle_evalperc = 0.0f;
					}
				} // // End Do While Loop
					// /// Last Brand Total
				Str.append("<tr align=left valign=top>\n");
				Str.append("<td align=right><b>").append(principalname).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_enquirytarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_enquiryachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_enquiryperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_homevisittarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_homevisitachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_homevisitperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_testdrivetarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_testdriveachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_testdriveperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_bookingtarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_bookingachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_bookingperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_deliverytarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_deliveryachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_deliveryperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_accessoriestarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_accessoriesachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_accessoriesperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_insurancetarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_insuranceachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_insuranceperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_ewtarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_ewachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_ewperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_fincasestarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_fincasesachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_fincasesperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_exetarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_exeachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_exeperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_evaltarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_evalachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(principle_evalperc).append("</b></td>\n");
				Str.append("</tr>");
				// // End Last Brand Total

				// // Start Grand Total
				Str.append("<tr>\n");
				Str.append("<td align=right><b>").append("Total: ").append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalenquirytarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalenquiryachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalenquiryperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalhomevisittarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalhomevisitachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalhomevisitperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totaltestdrivetarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totaltestdriveachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totaltestdriveperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingtarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totaldeliverytarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totaldeliveryachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totaldeliveryperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalaccessoriestarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalaccessoriesachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalaccessoriesperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalinsurancetarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalinsuranceachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalinsuranceperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalewtarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalewachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalewperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalfincasestarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalfincasesachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalfincasesperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalexetarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalexeachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalexeperc).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalevaltarget).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalevalachieved).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalevalperc).append("</b></td>\n");
				Str.append("</tr>\n");
				// // End Grand Total
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");

		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Brands</option>\n");
		return Str.toString();
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
}
