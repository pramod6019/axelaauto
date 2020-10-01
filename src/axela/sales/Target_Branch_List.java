/*Saiman 26th March 2013 , 29th march 2013
 * Ved Prakash (25 Feb 2013)
 * smitha nag 29 march 2013
 */
package axela.sales;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_Branch_List extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String branch_id = "0", dr_branch_id = "0";
	public String year = "";
	public int curryear = 0;
	public String branchtarget_id = "0";
	public String branchtarget_enquiry_count = "";
	public String branchtarget_homevisit_count = "";
	public String branchtarget_testdrives_count = "";
	public String branchtarget_bookings_count = "";
	public String branchtarget_delivery_count = "";
	public String branchtarget_accessories_amount = "";
	public String branchtarget_insurance_count = "";
	public String branchtarget_ew_count = "";
	public String branchtarget_fincases_count = "";
	public String branchtarget_exchange_count = "";
	public String branchtarget_evaluation_count = "";
	public String targetid = "0";
	public String enquirycount = "0";
	public String homevisitcount = "0";
	public String testdrivecount = "0";
	public String bookingcount = "0";
	public String deliverycount = "0";
	public String accessoriesamount = "0";
	public String insurancecount = "0";
	public String ewcount = "0";
	public String fincasecount = "0";
	public String exchangecount = "0";
	public String evaluationcount = "0";
	public String amt = "0";
	public int total_enquiry_count = 0, total_homevisit_count = 0;
	public int total_testdrives_count = 0, total_bookings_count = 0;
	public int total_delivery_count = 0, total_insurance_count = 0;
	public int total_accessories_amount = 0;
	public int total_ew_count = 0, total_fincases_count = 0;
	public int total_exchange_count = 0, total_evaluation_count = 0;
	public String QueryString = "";
	public String ExeAccess = "";
	public String smartarr[][] = {};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_target_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));

				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}
				if (branch_id.equals("0")) {
					dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));

				} else {
					dr_branch_id = branch_id;
				}
				CheckForm();
				StrHTML = ListData(request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData(HttpServletRequest request) throws SQLException {
		SOP("inside listdate ");
		StringBuilder Str = new StringBuilder();
		if (!dr_branch_id.equals("0")) {
			String tid[];
			String tdate[];
			String tenquirycount[];
			String thomevisit[];
			String ttestdrive[];
			String tbooking[];
			String tdelivery[];
			String taccessories[];
			String tinsurance[];
			String tew[];
			String tfincase[];
			String texchange[];
			String tevaluation[];

			StrSql = "SELECT branchtarget_id,"
					+ " COALESCE(branchtarget_month, '') AS branchtarget_month,"
					+ " COALESCE(branchtarget_enquiry_count, 0) AS branchtarget_enquiry_count,"
					+ " COALESCE(branchtarget_homevisit_count, 0) AS branchtarget_homevisit_count,"
					+ " COALESCE(branchtarget_testdrives_count, 0) AS branchtarget_testdrives_count,"
					+ " COALESCE(branchtarget_bookings_count, 0) AS branchtarget_bookings_count,"
					+ " COALESCE(branchtarget_delivery_count, 0) AS branchtarget_delivery_count,"
					+ " COALESCE(branchtarget_insurance_count, 0) AS branchtarget_insurance_count,"
					+ " COALESCE(branchtarget_ew_count, 0) AS branchtarget_ew_count,"
					+ " COALESCE(branchtarget_fincases_count, 0) AS branchtarget_fincases_count,"
					+ " COALESCE(branchtarget_exchange_count, 0) AS branchtarget_exchange_count,"
					+ " COALESCE(branchtarget_evaluation_count, 0) AS branchtarget_evaluation_count,"
					+ " COALESCE(branchtarget_accessories_amount, 0) AS branchtarget_accessories_amount";

			CountSql = "SELECT Count(distinct branchtarget_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_target_branch"
					+ " WHERE branchtarget_branch_id=" + dr_branch_id + ""
					+ " AND SUBSTR(branchtarget_month, 1, 4) = " + year;
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			CachedRowSet crs = processQuery(StrSql, 0);

			try {
				int ind = 0;
				int index = Integer.parseInt(ExecuteQuery(CountSql));
				tid = new String[index];
				tdate = new String[index];
				tenquirycount = new String[index];
				thomevisit = new String[index];
				ttestdrive = new String[index];
				tbooking = new String[index];
				tdelivery = new String[index];
				taccessories = new String[index];
				tinsurance = new String[index];
				tew = new String[index];
				tfincase = new String[index];
				texchange = new String[index];
				tevaluation = new String[index];

				while (crs.next()) {
					tid[ind] = crs.getString("branchtarget_id");
					tdate[ind] = crs.getString("branchtarget_month");
					tenquirycount[ind] = crs.getString("branchtarget_enquiry_count");
					thomevisit[ind] = crs.getString("branchtarget_homevisit_count");
					ttestdrive[ind] = crs.getString("branchtarget_testdrives_count");
					tbooking[ind] = crs.getString("branchtarget_bookings_count");
					tdelivery[ind] = crs.getString("branchtarget_delivery_count");
					tinsurance[ind] = crs.getString("branchtarget_insurance_count");
					tew[ind] = crs.getString("branchtarget_ew_count");
					tfincase[ind] = crs.getString("branchtarget_fincases_count");
					texchange[ind] = crs.getString("branchtarget_exchange_count");
					tevaluation[ind] = crs.getString("branchtarget_evaluation_count");
					taccessories[ind] = crs.getString("branchtarget_accessories_amount");
					ind++;
				}

				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>");
				Str.append("<th >Month</th>");
				Str.append("<th>Enquiry Count</th>");
				Str.append("<th data-hide=\"phone\">Home Visit Count</th>");
				Str.append("<th data-hide=\"phone\">Test Drive Count</th>");
				Str.append("<th data-hide=\"phone\">Booking Count</th>");
				Str.append("<th data-hide=\"phone\">Delivery Count</th>");
				Str.append("<th data-hide=\"phone\">Accessories Amount</th>");
				Str.append("<th data-hide=\"phone, tablet\">Insurance Count</th>");
				Str.append("<th data-hide=\"phone, tablet\">EW Count</th>");
				Str.append("<th data-hide=\"phone, tablet\">Fincases Count</th>");
				Str.append("<th data-hide=\"phone, tablet\">Exchange Count</th>");
				Str.append("<th data-hide=\"phone, tablet\">Evaluation Count</th>");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				for (int i = 1; i <= 12; i++) {
					count = count + 1;
					for (int j = 0; j < tdate.length; j++) {
						if (tdate[j].equals(year + doublenum(i) + "01000000")) {
							targetid = tid[j];
							enquirycount = tenquirycount[j];
							homevisitcount = thomevisit[j];
							testdrivecount = ttestdrive[j];
							bookingcount = tbooking[j];
							deliverycount = tdelivery[j];
							insurancecount = tinsurance[j];
							ewcount = tew[j];
							fincasecount = tfincase[j];
							exchangecount = texchange[j];
							evaluationcount = tevaluation[j];
							amt = taccessories[j];
							break;
						} else {
							targetid = "0";
							enquirycount = "0";
							homevisitcount = "0";
							testdrivecount = "0";
							bookingcount = "0";
							deliverycount = "0";
							insurancecount = "0";
							ewcount = "0";
							fincasecount = "0";
							exchangecount = "0";
							evaluationcount = "0";
							amt = "0";
						}
					}
					Str.append("<tr><td >").append(count).append("</td>\n");
					Str.append("<td valign=top>");
					Str.append(TextMonth(i - 1)).append("-").append(year);
					Str.append("<input name=\"txt_target_id_").append(count).append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=").append(targetid).append(">");
					Str.append("</td>\n");
					Str.append("<td>").append(enquirycount).append("</td>\n");
					Str.append("<td >").append(homevisitcount).append("</td>\n");
					Str.append("<td>").append(testdrivecount).append("</td>\n");
					Str.append("<td>").append(bookingcount).append("</td>\n");
					Str.append("<td>").append(deliverycount).append("</td>\n");
					Str.append("<td>").append(amt).append("</td>\n");
					Str.append("<td>").append(insurancecount).append("</td>\n");
					Str.append("<td>").append(ewcount).append("</td>\n");
					Str.append("<td>").append(fincasecount).append("</td>\n");
					Str.append("<td>").append(exchangecount).append("</td>\n");
					Str.append("<td>").append(evaluationcount).append("</td>\n");

					Str.append("<td nowrap=nowrap><a href=\"target-branch-update.jsp?update=yes&target_id=").append(targetid).append("&dr_branch_id=").append(dr_branch_id)
							.append("&year=")
							.append(year).append("&month=").append(i).append("\">Update Target</a></td>\n");
					Str.append("</tr>\n");

					total_enquiry_count += Integer.parseInt(enquirycount);
					total_homevisit_count += Integer.parseInt(homevisitcount);
					total_testdrives_count += Integer.parseInt(testdrivecount);
					total_bookings_count += Integer.parseInt(bookingcount);
					total_delivery_count += Integer.parseInt(deliverycount);
					total_insurance_count += Integer.parseInt(insurancecount);
					total_ew_count += Integer.parseInt(ewcount);
					total_fincases_count += Integer.parseInt(fincasecount);
					total_exchange_count += Integer.parseInt(exchangecount);
					total_evaluation_count += Integer.parseInt(evaluationcount);
					total_accessories_amount += Integer.parseInt(amt);
				}
				Str.append("<tr>\n");
				Str.append("<td>\n</td>\n");
				Str.append("<td ><b>Total:</b></td>\n");
				Str.append("<td><b>").append(total_enquiry_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_homevisit_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_testdrives_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_bookings_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_delivery_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_accessories_amount).append("</b></td>\n");
				Str.append("<td><b>").append(total_insurance_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_ew_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_fincases_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_exchange_count).append("</b></td>\n");
				Str.append("<td><b>").append(total_evaluation_count).append("</b></td>\n");
				Str.append("<td>\n</td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	protected void CheckForm() {
		if (dr_branch_id.equals("0")) {
			msg = "Select Branch!";
		}
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = curryear - 3; i <= curryear + 3; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}
}
