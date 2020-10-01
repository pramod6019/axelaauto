//Shivaprasad 6/07/2015   
package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_User_Import extends Connect {

	public String StrSql = "", StrHTML = "", msg = "";
	public int count = 0;
	public String emp_id = "0", comp_id = "0";
	public String BranchAccess = "", brand_id = "";
	public int propcount = 0;
	public Connection conntx = null;
	public Statement stmttx = null;
	String customer_id = "0";
	String contact_id = "0";
	int jcpresent = 0;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		emp_id = CNumeric(GetSession("emp_id", request));
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckSession(request, response);
		BranchAccess = CheckNull(session.getAttribute("BranchAccess"));
		CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
		StrHTML = GetJobCardImportList();
	}

	public String GetJobCardImportList() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_branch_id = branch_id  OR emp.emp_all_branches = '1'"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_branch empbranch ON empbranch.emp_branch_id = branch_id"
					+ " WHERE 1 = 1"
					+ " AND branch_active = '1'"
					+ " AND branch_branchtype_id = 3";
			if (!emp_id.equals("1")) {
				StrSql += " AND ( emp.emp_id = " + emp_id + " OR empbranch.emp_id = " + emp_id + " )";
			}

			StrSql += " GROUP BY branch_brand_id"
					+ " ORDER BY branch_brand_id";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"table-responsive\">\n");
			Str.append("<table class=\"table table-hover table-bordered table-responsive\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<tr align=\"center\">\n");
					if (crs.getString("branch_brand_id").equals("2")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-maruti.jsp><b>Click here to Import Maruti Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("151")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-onetriumph.jsp><b>Click here to Import OneTriumph Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("7")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-ford.jsp><b>Click here to Import Ford Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("9")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-honda.jsp><b>Click here to Import Honda Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("6")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-hyundai.jsp><b>Click here to Import Hyundai Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("51")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-volvo.jsp><b>Click here to Import Volvo Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("101")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-suzuki.jsp><b>Click here to Import Suzuki Job Cards</b></a>");
						Str.append("</td>");
					} else if (crs.getString("branch_brand_id").equals("153")) {
						Str.append("<td>");
						Str.append("<br><a href=jobcard-user-import-harley-davidson.jsp><b>Click here to Import Harley Davidson Job Cards</b></a>");
						Str.append("</td>");
					}
					Str.append("</tr>\n");
				}
			} else {
				Str.append("<font color=\"red\"><b> No Branch Found!</b></font>");
			}
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public void EstablishConnResources(String comp_id, Connection conntx, Statement stmttx) {
		this.comp_id = comp_id;
		this.conntx = conntx;
		this.stmttx = stmttx;
	}

	public String ValidateSheetData(String comp_id, String customer_name, String contact_mobile1, String contact_email1,
			String contact_phone1, String contact_address, String contact_dob, String contact_anniversary, String contact_city, String contact_pin,
			String veh_engine_no, String veh_sale_date, String variant_name, String jc_branch_id,
			String jc_reg_no, String jc_chassis_no, String jc_ro_no, String jc_emp_name,
			String jc_technician_emp_name, String jc_time_in, String jc_bill_cash_no, String jc_bill_cash_date, String jc_jctype_name,
			String jc_jccat_name, String jc_kms, String jc_grandtotal, String jc_bill_cash_labour,
			String jc_bill_cash_parts, String jc_bill_cash_parts_valueadd, String jc_bill_cash_parts_oil, String jc_bill_cash_parts_tyre,
			String jc_bill_cash_parts_accessories, String jc_bill_cash_labour_discamt, String jc_notes, String jc_advice,
			String jc_entry_id, String jc_entry_date, String error_msg) {

		StringBuilder Str = new StringBuilder();
		String jc_error_msg = "", status = "";
		String contact_title = "0", contact_fname = "", contact_name = "", contact_title_id = "1", contact_lname = "";
		String veh_id = "0", veh_modelyear = "", veh_lastservice_kms = "0", veh_lastservice = "";
		String veh_service_duedate = "", veh_service_duekms = "0";
		String variant_id = "0", model_brand_id = "0", model_id = "0";
		String checkjcrefno = "", servicedueyear = "";
		String jc_emp_id = "0", jc_technician_emp_id = "0", jc_jccat_id = "0";
		String jc_jctype_id = "0", jc_time_promised = "", jc_time_ready = "", jc_time_out = "";
		String day = "", month = "", year = "";
		String customer_id = "0";
		String contact_id = "0";
		String contact_state = "", contact_city_id = "0";

		try {
			this.comp_id = comp_id;
			if (brand_id.equals("6") || brand_id.equals("7")) {
				jc_bill_cash_parts = "0.0";
			}
			// customer name validation
			if (customer_name.equals("null") || customer_name.equals("") || customer_name.equals("Customer Name")) {
				customer_name = "";
				error_msg += "Customer Name should not be empty! <br>";
			}
			if (!customer_name.equals("")) {
				contact_name = customer_name;
				if (!contact_name.equals("")) {
					contact_title = "0";
					if (contact_name.contains(" ")) {
						contact_title = contact_name.split(" ")[0];
						StrSql = "SELECT title_id"
								+ " FROM  " + compdb(comp_id) + "axela_title"
								+ " WHERE title_desc LIKE '%" + contact_title + "%'";
						contact_title = CNumeric(ExecuteQuery(StrSql));
						if (contact_title.equals("0")) {
							contact_title = "1";
							contact_fname = contact_name;
						} else {
							if (contact_name.split(" ").length > 1) {
								contact_fname = contact_name;
							}
						}
					} else {
						contact_title = "1";
						contact_fname = contact_name;
					}
					contact_name = contact_fname;
				}
			}
			// contact mobile validation
			if (contact_mobile1.equals("")) {
				error_msg += "Mobile Number Should not be empty! <br>";
			}
			if (!contact_mobile1.equals("") && !contact_mobile1.equals("0")) {

				if (contact_mobile1.contains(",")) {
					contact_mobile1 = contact_mobile1.split(",")[0];
				} else if (contact_mobile1.substring(0, 3).equals("+91")) {
					contact_mobile1 = contact_mobile1.replace("+91", "");
				} else if (contact_mobile1.substring(0, 2).equals("91") && contact_mobile1.length() > 10) {
					contact_mobile1 = contact_mobile1.replaceFirst("91", "");
				}
				else if (contact_mobile1.substring(0, 1).equals("0") && contact_mobile1.length() > 10) {
					contact_mobile1 = contact_mobile1.replaceFirst("0", "");
				}
				contact_mobile1 = contact_mobile1.replaceAll("[^0-9]+", "");
				if (!contact_mobile1.contains("91-")) {
					contact_mobile1 = "91-" + contact_mobile1;
				}
				if (!IsValidMobileNo11(contact_mobile1)) {
					error_msg += "Please enter valid Mobile! <br>";
				}
				// SOP("contact_mobile1==" + contact_mobile1);
			} else if (contact_mobile1.equals("0")) {
				error_msg += "Please enter valid Mobile! <br>";
			}
			// City validation
			if (!contact_city.equals("")) {

				StrSql = "SELECT city_id"
						+ " FROM " + compdb(comp_id) + "axela_city"
						+ " WHERE city_name = '" + contact_city + "'";
				contact_city_id = CNumeric(ExecuteQuery(StrSql));
				// SOP("StrSql=brand_id=" + brand_id);
				// SOP("StrSql=city=" + StrSql + "  contact_city_id=" + contact_city_id);
				if ((brand_id.equals("7") || brand_id.equals("51")) && contact_city_id.equals("0")) {// AS vijay sir said 19/9/17 for only ford

					StrSql = "SELECT  branch_city_id, branch_pin"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + jc_branch_id;
					// SOP("strsql==" + StrSql);
					ResultSet rs = processQuery(StrSql, 0);
					while (rs.next()) {
						contact_city_id = rs.getString("branch_city_id");
						contact_pin = rs.getString("branch_pin");
					}
					rs.close();
				}
				if (contact_city_id.equals("0")) {
					error_msg += " Invalid City name! <br>";
				}

				if (!contact_city_id.equals("0")) {
					StrSql = "SELECT state_name"
							+ " FROM " + compdb(comp_id) + "axela_state"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_state_id = state_id"
							+ " WHERE city_id = " + contact_city_id + "";
					contact_state = PadQuotes(ExecuteQuery(StrSql));
				}
			} else if (contact_city.equals("")) {
				if (brand_id.equals("6") || brand_id.equals("7") || brand_id.equals("51") || brand_id.equals("9") || brand_id.equals("1") || brand_id.equals("2"))
				{
					StrSql = "SELECT  branch_city_id, branch_pin"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + jc_branch_id;
					ResultSet rs = processQuery(StrSql, 0);
					while (rs.next()) {
						contact_city_id = rs.getString("branch_city_id");
						contact_pin = rs.getString("branch_pin");
					}
					rs.close();
					if (!contact_city_id.equals("0")) {
						StrSql = "SELECT state_name"
								+ " FROM " + compdb(comp_id) + "axela_state"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_state_id = state_id"
								+ " WHERE city_id = " + contact_city_id + "";
						contact_state = PadQuotes(ExecuteQuery(StrSql));
					}
				} else {
					error_msg += " Customer City Should not be empty! <br>";
				}
			}

			// contact Phone validation
			if (!contact_phone1.equals("") && !contact_phone1.equals("0")) {
				contact_phone1 = contact_phone1.replaceAll("[^-0-9]+", "");
				if (!contact_phone1.contains("91-")) {
					contact_phone1 = "91-" + contact_phone1;
				}
				if (!IsValidPhoneNo11(contact_phone1)) {
					error_msg += "Please enter valid Phone! <br>";
				}
			} else if (contact_phone1.equals("0")) {
				error_msg += "Please enter valid Phone! <br>";
			}
			// contact_email1 valdation
			if (!IsValidEmail(contact_email1)) {
				contact_email1 = "";
			}
			// contact_dob valdation
			if (!contact_dob.equals("")) {
				if (isValidDateFormatShort(contact_dob)) {
					contact_dob = ConvertShortDateToStr(contact_dob);
				} else if (contact_dob.split("/").length == 3) {
					month = contact_dob.split("/")[0];
					if (month.length() == 1) {
						month = "0" + month;
					}
					day = contact_dob.split("/")[1];
					if (day.length() == 1) {
						day = "0" + day;
					}
					year = contact_dob.split("/")[2];
					if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
						contact_dob = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
					} else {
						error_msg += "Invalid DOB! <br>";
					}
					day = "";
					month = "";
					year = "";
				} else {
					error_msg += "Invalid DOB! <br>";
				}
			}
			// contact_anniversary valdation
			if (!contact_anniversary.equals("")) {
				if (isValidDateFormatShort(contact_anniversary)) {
					contact_anniversary = ConvertShortDateToStr(contact_anniversary);
				} else if (contact_anniversary.split("/").length == 3) {
					month = contact_anniversary.split("/")[0];
					if (month.length() == 1) {
						month = "0" + month;
					}
					day = contact_anniversary.split("/")[1];
					if (day.length() == 1) {
						day = "0" + day;
					}
					year = contact_anniversary.split("/")[2];
					if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
						contact_anniversary = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
					} else {
						error_msg += "Invalid Anniversary! <br>";
					}
					day = "";
					month = "";
					year = "";
				} else {
					error_msg += "Invalid Anniversary! <br>";
				}
			}
			// vehicle sale date
			if (veh_sale_date.equals(""))
			{
				veh_sale_date = "";
			}
			if (!veh_sale_date.equals("")) {
				day = "";
				month = "";
				year = "";
				veh_modelyear = "";
				// SOP("veh_sale_date=0=" + veh_sale_date);
				if (isValidDateFormatShort(veh_sale_date)) {
					veh_sale_date = ConvertShortDateToStr(veh_sale_date);
					veh_modelyear = veh_sale_date.substring(0, 4);
				} else if (veh_sale_date.split("/").length == 3) {
					month = veh_sale_date.split("/")[0];
					if (month.length() == 1) {
						month = "0" + month;
					}
					day = veh_sale_date.split("/")[1];
					if (day.length() == 1) {
						day = "0" + day;
					}
					year = veh_sale_date.split("/")[2];

					if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
						veh_sale_date = year + month + day + "000000";
						veh_modelyear = year;
					}
				} else if (veh_sale_date.contains(".")) {
					if (veh_sale_date.split("\\.").length == 3) {
						day = veh_sale_date.split("\\.")[0];
						if (day.length() == 1) {
							day = "0" + day;
						}
						month = veh_sale_date.split("\\.")[1];
						if (month.length() == 1) {
							month = "0" + month;
						}
						year = veh_sale_date.split("\\.")[2];
						if (year.length() == 2) {
							year = "20" + year;
						}
						if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
							veh_sale_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
							veh_sale_date = veh_sale_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
						} else {
							error_msg += "Invalid Sale Date ! <br>";
						}
						day = "";
						month = "";
						year = "";
					}
				} else {
					veh_sale_date = "";
					veh_modelyear = "";
				}
			}
			// Veh Chasis number validation
			if (jc_chassis_no.equals("null") || jc_chassis_no.equals("") || jc_chassis_no.equals("Vehicle No")) {
				jc_chassis_no = "";
				error_msg += "Chassis No. Should not  be empty! <br>";
			}
			if (brand_id.equals("151")) {// as per vijay sir
				if (variant_name.contains("(")) {
					variant_name = variant_name.replace("(", "&#40;");
				}
				if (variant_name.contains(")")) {
					variant_name = variant_name.replace(")", "&#41;");
				}
				StrSql = "SELECT variant_id"
						+ " FROM  axelaauto.axela_preowned_model"
						+ " INNER JOIN  axelaauto.axela_preowned_variant ON variant_preownedmodel_id = preownedmodel_id"
						+ " WHERE preownedmodel_name = '" + variant_name + "'"
						+ " LIMIT 1";
				variant_id = CNumeric(ExecuteQuery(StrSql));
			} else if (!variant_name.equals("")) {
				if (variant_name.contains("(")) {
					variant_name = variant_name.replace("(", "&#40;");
				}
				if (variant_name.contains(")")) {
					variant_name = variant_name.replace(")", "&#41;");
				}
				StrSql = "SELECT variant_id"
						+ " FROM  axelaauto.axela_preowned_variant"
						+ " WHERE variant_name = '" + variant_name + "'"
						+ " LIMIT 1";
				variant_id = CNumeric(ExecuteQuery(StrSql));

				if (variant_id.equals("0")) {
					StrSql = "SELECT variant_id"
							+ " FROM  axelaauto.axela_preowned_variant"
							+ " WHERE variant_service_code = '" + variant_name + "'"
							+ " LIMIT 1";
					// SOP("StrSql==variant=" + StrSql);
					variant_id = CNumeric(ExecuteQuery(StrSql));
				}
				// if (variant_id.equals("0")) {
				// StrSql = "SELECT variant_id"
				// + " FROM  axelaauto.axela_preowned_variant"
				// + " WHERE item_code = '" + variant_name + "'"
				// + " LIMIT 1";
				// variant_id = CNumeric(ExecuteQuery(StrSql));
				// }
				// if (!variant_id.equals("0")) {
				// model_brand_id = "0";
				// StrSql = "SELECT model_brand_id"
				// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON variant_preownedmodel_id = model_id"
				// + " WHERE variant_id = " + variant_id;
				// model_brand_id = CNumeric(ExecuteQuery(StrSql));
				// }
			} else {
				// variant name Validation
				if (variant_name.equals("null") || variant_name.equals("") || variant_name.equals("0")) {
					variant_name = "";
					error_msg += "Model/Variant Name should not be empty! <br>";
				}
			}
			if (variant_id.equals("0") && !variant_name.equals("")) {
				error_msg += "Invalid Model/Variant Name! <br>";
			}
			// Veh reg number validation
			if (jc_reg_no.equals("null") || jc_reg_no.equals("") || jc_reg_no.equals("Vehicle Reg. No")) {
				jc_reg_no = "";
				// error_msg += "Reg. No. should not be empty! <br>";
			}
			if (!jc_reg_no.equals("")) {
				if (jc_reg_no.length() <= 20) {
					jc_reg_no = jc_reg_no.replaceAll("[^a-zA-Z0-9]+", "");// for Special Charcter
				}
			}

			// Refrence number validation
			if (!jc_ro_no.equals("")) {
				StrSql = "SELECT jc_ro_no"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_ro_no = '" + jc_ro_no + "'"
						+ " AND jc_branch_id = " + jc_branch_id;
				checkjcrefno = PadQuotes(ExecuteQuery(StrSql));
				// SOP("StrSql===" + StrSql);
				// // Main check for jc
				if (checkjcrefno.equals(jc_ro_no)) {
					jcpresent++;
				}
			}
			// Service advisor validation
			if (brand_id.equals("151")) {// as per vijay sir
				jc_emp_id = "1";
			}
			else {
				if (!jc_emp_name.equals("")) {
					if (jc_emp_name.contains("(")) {
						jc_emp_name = jc_emp_name.replace("(", "&#40;");
					}
					if (jc_emp_name.contains(")")) {
						jc_emp_name = jc_emp_name.replace(")", "&#41;");
					}
					StrSql = "SELECT emp_id"
							+ " FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_name = '" + jc_emp_name + "'"
							+ " AND emp_service = 1"
							+ " AND emp_branch_id = " + jc_branch_id
							+ " LIMIT 1";
					// SOP("StrSql===" + StrSql);
					jc_emp_id = CNumeric(ExecuteQuery(StrSql));
				}
				if (jc_emp_id.equals("0")) {
					error_msg += "No Service Advisor found with name: " + jc_emp_name + "<br>";
					// jc_emp_id = "1";
				}
			}
			// Technician validation
			if (!jc_technician_emp_name.equals("")) {
				// jc_technician_emp_name = jc_technician_emp_name.replace("(TE)", "");
				if (jc_technician_emp_name.contains("(")) {
					jc_technician_emp_name = jc_technician_emp_name.replace("(", "&#40;");
				}
				if (jc_technician_emp_name.contains(")")) {
					jc_technician_emp_name = jc_technician_emp_name.replace(")", "&#41;");
				}
				StrSql = "SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_name = '" + jc_technician_emp_name + "'"
						+ " AND emp_technician = 1"
						+ " AND emp_branch_id = " + jc_branch_id
						+ " LIMIT 1";
				// SOP("StrSql===" + StrSql);
				jc_technician_emp_id = CNumeric(ExecuteQuery(StrSql));
				if (jc_technician_emp_id.equals("0")) {
					error_msg += "No Technician Found with name: " + jc_technician_emp_name + "<br> ";
				}
			}
			if (brand_id.equals("151") && jc_technician_emp_id.equals("0")) {// as per vijay sir
				jc_technician_emp_id = "1";
			}

			// Jc time in validation
			if (jc_time_in.equals("null") || jc_time_in.equals("") || jc_time_in.equals("R/O Date")) {
				jc_time_in = "";
				error_msg += "R/o Date should not be empty! <br>";
			}
			if (!jc_time_in.equals("") && !checkjcrefno.equals(jc_ro_no)) {
				if (isValidDateFormatStr(jc_time_in) && jc_time_in.length() == 14) {
					jc_time_promised = jc_time_in;
					jc_time_ready = jc_time_in;
					jc_time_out = jc_time_in;
					veh_lastservice = jc_time_in;
				} else if (jc_time_in.contains("/")) {
					// SOP("jc_time_in==" + jc_time_in);
					if (isValidDateFormatShort(jc_time_in)) {
						jc_time_in = ConvertShortDateToStr(jc_time_in);
						jc_time_in = jc_time_in.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
						jc_time_promised = jc_time_in;
						jc_time_ready = jc_time_in;
						jc_time_out = jc_time_in;
						veh_lastservice = jc_time_in;
					} else if (jc_time_in.split("/").length == 3) {
						month = jc_time_in.split("/")[0];
						if (month.length() == 1) {
							month = "0" + month;
						}
						day = jc_time_in.split("/")[1];
						if (day.length() == 1) {
							day = "0" + day;
						}
						year = jc_time_in.split("/")[2];
						if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
							jc_time_in = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
							jc_time_in = jc_time_in.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
							jc_time_promised = jc_time_in;
							jc_time_ready = jc_time_in;
							jc_time_out = jc_time_in;
							veh_lastservice = jc_time_in;
						} else {
							error_msg += "Invalid JC Time In! <br>";
						}
						day = "";
						month = "";
						year = "";
					}
				} else if (jc_time_in.contains(".")) {
					if (jc_time_in.split("\\.").length == 3) {
						day = jc_time_in.split("\\.")[0];
						if (day.length() == 1) {
							day = "0" + day;
						}
						month = jc_time_in.split("\\.")[1];
						if (month.length() == 1) {
							month = "0" + month;
						}
						year = jc_time_in.split("\\.")[2];
						if (year.length() == 2) {
							year = "20" + year;
						}
						if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
							jc_time_in = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
							jc_time_in = jc_time_in.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
							jc_time_promised = jc_time_in;
							jc_time_ready = jc_time_in;
							jc_time_out = jc_time_in;
							veh_lastservice = jc_time_in;
						} else {
							error_msg += "Invalid JC Time In! <br>";
						}
						day = "";
						month = "";
						year = "";
					}
				} else {
					jc_time_in = fmtShr3tofmtShr1(jc_time_in);
					if (isValidDateFormatStr(jc_time_in))
					{
						jc_time_in = jc_time_in.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
						jc_time_promised = jc_time_in;
						jc_time_ready = jc_time_in;
						jc_time_out = jc_time_in;
						veh_lastservice = jc_time_in;
					} else {
						error_msg += "Invalid JC Time In! <br>";
					}
				}
				// SOP("jc_time_promised =" + jc_time_in);
				// SOP("jc_time_ready =" + jc_time_in);
				// SOP("jc_time_out = " + jc_time_in);
				// SOP("veh_lastservice =" + jc_time_in);
			}
			// Bill date validation
			if (jc_bill_cash_date.equals("null") || jc_bill_cash_date.equals("") || jc_bill_cash_date.equals("Bill Date")) {
				jc_bill_cash_date = "";
				error_msg += "Bill Date should not be empty! <br>";
			}
			if (!jc_bill_cash_date.equals("")) {
				// SOP("jc_bill_cash_date==" + jc_bill_cash_date);
				if (jc_bill_cash_date.contains("/")) {
					if (isValidDateFormatShort(jc_bill_cash_date)) {
						jc_bill_cash_date = ConvertShortDateToStr(jc_bill_cash_date);
					} else if (jc_bill_cash_date.split("/").length == 3) {
						month = jc_bill_cash_date.split("/")[0];
						if (month.length() == 1) {
							month = "0" + month;
						}
						day = jc_bill_cash_date.split("/")[1];
						if (day.length() == 1) {
							day = "0" + day;
						}
						year = jc_bill_cash_date.split("/")[2];
						if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
							jc_bill_cash_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
						} else {
							error_msg += "Invalid Bill Date! <br>";
						}
						day = "";
						month = "";
						year = "";
					}
				} else if (jc_bill_cash_date.length() == 14) {
					if (isValidDateFormatStr(jc_bill_cash_date)) {
						jc_bill_cash_date = jc_bill_cash_date + "";
					}
				} else if (jc_bill_cash_date.contains(".")) {
					if (jc_bill_cash_date.split("\\.").length == 3) {
						day = jc_bill_cash_date.split("\\.")[0];
						if (day.length() == 1) {
							day = "0" + day;
						}
						month = jc_bill_cash_date.split("\\.")[1];
						if (month.length() == 1) {
							month = "0" + month;
						}
						year = jc_bill_cash_date.split("\\.")[2];
						if (year.length() == 2) {
							year = "20" + year;
						}
						if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
							jc_bill_cash_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
							jc_bill_cash_date = jc_bill_cash_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
						} else {
							error_msg += "Invalid Bill Date! <br>";
						}

						day = "";
						month = "";
						year = "";
					}
				} else {
					jc_bill_cash_date = fmtShr3tofmtShr1(jc_bill_cash_date);
					if (isValidDateFormatStr(jc_bill_cash_date)) {
						jc_bill_cash_date = jc_bill_cash_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
					} else {
						error_msg += "Invalid Bill Date! <br>";
					}
				}
			}

			// catagory validation
			jc_jccat_id = "0";
			if (jc_jccat_name.equals("")) {
				if (brand_id.equals("2") || brand_id.equals("1")) {
					jc_jccat_name = "General";
					jc_jccat_id = "3";
				} else if (brand_id.equals("151")) {// as per vijay sir
					jc_jccat_name = "A+";
					jc_jccat_id = "11";
				}

			} else if (!jc_jccat_name.equals("")) {
				StrSql = "SELECT jccat_id"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
						+ " WHERE jccat_name='" + jc_jccat_name + "'";
				// SOP("cat===" + StrSql);
				jc_jccat_id = CNumeric(ExecuteQuery(StrSql));
				if (jc_jccat_id.equals("0")) {
					StrSql = "SELECT jccat_id"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
							+ " WHERE jccat_name LIKE '%" + jc_jccat_name.toLowerCase() + "%'";
					// SOP("cat=1==" + StrSql);
					jc_jccat_id = CNumeric(ExecuteQuery(StrSql));
				}
			}
			// jc type validation
			jc_jctype_id = "0";
			if (!jc_jctype_name.equals("")) {
				StrSql = "SELECT jctype_id"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
						+ " WHERE jctype_name='" + jc_jctype_name + "'";
				jc_jctype_id = CNumeric(ExecuteQuery(StrSql));
			}
			if (jc_jctype_id.equals("0") && !jc_jctype_name.equals("")) {
				error_msg += "Invalid Jobcard Type! <br>";
			}
			if (jc_jccat_id.equals("0") && !jc_jccat_name.equals("")) {
				error_msg += "Invalid Jobcard Category! <br>";
			}

			if (jc_jccat_id.equals("0") && jc_jctype_id.equals("0")) {
				error_msg += "Either Jobcard Category/Jobcard Type should be present! <br>";
			}
			// jc kms validation
			if (jc_kms.equals(""))
			{
				jc_kms = "0";
			}

			if (!veh_lastservice.equals("")) {
				int duekms = 0;
				int duecount = 0;
				day = "";
				month = "";
				year = "";
				veh_service_duedate = strToShortDate(veh_lastservice);
				day = veh_service_duedate.split("/")[0];
				month = veh_service_duedate.split("/")[1];
				year = veh_service_duedate.split("/")[2];
				int i = Integer.parseInt(year);

				if (Long.parseLong(veh_lastservice.substring(1, 8)) > Long.parseLong(ToLongDate(kknow()).substring(1, 8))) {
					servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
				} else {
					servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
					servicedueyear = (Integer.parseInt(servicedueyear) + 1) + "";
				}
				while (i < Integer.parseInt(servicedueyear)) {
					if (duecount == 0) {
						duekms += Integer.parseInt(veh_lastservice_kms) + 10000;
					} else {
						duekms += 10000;
					}
					i++;
					duecount++;
				}
				if (duekms == 0) {
					duekms = Integer.parseInt(veh_lastservice_kms);
				}
				veh_service_duekms = duekms + "";
				veh_service_duedate = servicedueyear + month + day + "000000";
			}
			if (!variant_id.equals("0")) {
				model_id = CNumeric(ExecuteQuery("SELECT variant_preownedmodel_id"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " WHERE variant_id = " + variant_id));
			}

			if (!jc_time_in.equals("") && !jc_bill_cash_date.equals("") && !variant_id.equals("0")) {
				if (error_msg.equals("")) {
					if (!checkjcrefno.equals(jc_ro_no)) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();

							if (!jc_reg_no.equals("")) {
								StrSql = "SELECT veh_id"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE 1=1"
										+ "	AND veh_reg_no = '" + jc_reg_no + "'";
								veh_id = CNumeric(ExecuteQuery(StrSql));

								StrSql = "SELECT veh_id, veh_chassis_no, veh_reg_no,"
										+ " COALESCE(variant_preownedmodel_id, '0') AS variant_preownedmodel_id"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ "	LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
										+ " WHERE 1=1"
										+ "	AND veh_reg_no = '" + jc_reg_no + "'"
										+ "	OR (veh_chassis_no = '" + jc_chassis_no + "'"
										+ " AND  variant_preownedmodel_id= " + model_id + ")";
								if (!veh_id.equals("0")) {
									StrSql += " AND veh_id = " + veh_id;
								}
								// SOP("StrSql==" + StrSql);
								CachedRowSet crs = processQuery(StrSql, 0);
								if (crs.next()) {
									veh_id = CNumeric(crs.getString("veh_id"));
									// SOP("jc_chassis_no==" + jc_chassis_no + ", veh_chassis_no==" + crs.getString("veh_chassis_no"));
									// SOP("jc_reg_no==" + jc_reg_no + ", jc_reg_no==" + crs.getString("veh_reg_no"));
									// SOP("model_id==" + model_id + ", variant_preownedmodel_id==" + crs.getString("variant_preownedmodel_id"));
									// SOP("veh_id==" + veh_id);
									if (!veh_id.equals("0")) {
										// If chassis.Number is Same but Reg.No and model is different
										if (jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& !model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& !jc_reg_no.equals(crs.getString("veh_reg_no"))) {
											SOP("111");
											status = "add";
										}

										// If Reg.No ,model and chassis Number is Same
										if (jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& jc_reg_no.equals(crs.getString("veh_reg_no"))) {
											SOP("222");
											status = "update";
										}

										// If Reg.No empty but chassis Number and model is same
										if (jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& crs.getString("veh_reg_no").equals("")) {
											SOP("333");
											status = "update";
										}

										// If Reg.No is diffrent but model and chassis Number same
										if (jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& !jc_reg_no.equals(crs.getString("veh_reg_no"))
												&& !crs.getString("veh_reg_no").equals("")) {
											SOP("444");
											status = "update";
										}

										// If Reg.No and chassis Number is Same but model is diffrent
										if (jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& !model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& jc_reg_no.equals(crs.getString("veh_reg_no"))) {
											SOP("555");
											status = "update";
										}

										// If Reg.No and model is same but chassis Number empty
										if (crs.getString("veh_chassis_no").equals("")
												&& model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& jc_reg_no.equals(crs.getString("veh_reg_no"))) {
											SOP("777");
											status = "update";
										}

										// If Reg.No and model is same but chassis Number is different
										if (!jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& !crs.getString("veh_chassis_no").equals("")
												&& model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& jc_reg_no.equals(crs.getString("veh_reg_no"))) {
											SOP("666");
											error_msg += "Reg No. associated with other Chassis No. : " + jc_reg_no + "<br>";
										}

										// If Reg.No is same but model,chassis Number is different
										if (!jc_chassis_no.equals(crs.getString("veh_chassis_no"))
												&& !model_id.equals(crs.getString("variant_preownedmodel_id"))
												&& jc_reg_no.equals(crs.getString("veh_reg_no"))) {
											if ((brand_id.equals("2") || brand_id.equals("1")) && comp_id.equals("1009")) {
												SOP("coming");
												status = "update";
											} else {
												SOP("888");
												error_msg += "Reg No. associated with other Chassis No. : " + jc_reg_no + "<br>";
											}
										}
									}
								} else {
									SOP("999");
									status = "add";
								}
								crs.close();
							}
							else if (jc_reg_no.equals("") && !jc_chassis_no.equals("") && !model_id.equals("")) {
								StrSql = "SELECT veh_id "
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ "	LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
										+ " WHERE 1=1"
										+ "	AND veh_chassis_no = '" + jc_chassis_no + "'";
								if ((brand_id.equals("2") || brand_id.equals("1")) && !veh_engine_no.equals("")) {
									StrSql += " AND veh_engine_no = " + veh_engine_no;
								} else {
									StrSql += " AND variant_preownedmodel_id = " + model_id + "";
								}
								// SOP("StrSql=regnoempty===" + StrSql);
								CachedRowSet crs = processQuery(StrSql, 0);
								if (crs.next()) {
									veh_id = CNumeric(crs.getString("veh_id"));
									if (!veh_id.equals("0")) {
										SOP("56987");
										status = "update";
									}
								}
								else {
									SOP("1234");
									status = "add";
								}
								crs.close();
							}
							SOP("status==" + status);
							if (status.equals("add")) {
								InsertVehicle(error_msg, jc_branch_id, jc_bill_cash_date, customer_name,
										contact_fname, contact_lname, contact_city, contact_mobile1, contact_phone1,
										contact_title_id, contact_email1, contact_address, contact_dob, contact_anniversary, contact_pin,
										veh_engine_no, veh_lastservice, veh_lastservice_kms,
										veh_service_duekms, veh_service_duedate, veh_sale_date, jc_chassis_no, jc_reg_no,
										variant_name, jc_jctype_id, contact_city_id, contact_state,
										variant_id, model_brand_id, veh_modelyear, jc_jccat_id, jc_kms, jc_ro_no, jc_bill_cash_no,
										jc_time_in, jc_time_promised, jc_time_ready, jc_time_out, jc_emp_id,
										jc_technician_emp_id, jc_grandtotal, jc_bill_cash_labour, jc_bill_cash_parts,
										jc_bill_cash_parts_valueadd, jc_bill_cash_parts_oil, jc_bill_cash_parts_tyre,
										jc_bill_cash_parts_accessories,
										jc_bill_cash_labour_discamt, jc_advice, jc_notes, jc_entry_id, jc_entry_date);
								propcount++;
							} else if (status.equals("update")) {
								UpdateVehicle(error_msg, jc_branch_id, jc_bill_cash_date, customer_name,
										contact_fname, contact_lname, contact_city, contact_mobile1,
										contact_phone1, contact_title_id, contact_email1, contact_address,
										contact_dob, contact_anniversary, contact_pin,
										veh_engine_no, veh_lastservice_kms, veh_service_duekms, veh_service_duedate, veh_sale_date,
										jc_chassis_no, jc_reg_no, variant_name, jc_jctype_id, contact_city_id, contact_state,
										variant_id, model_brand_id, veh_modelyear, jc_jccat_id, jc_kms, jc_ro_no, jc_bill_cash_no,
										jc_time_in, jc_time_promised, jc_time_ready, jc_time_out, jc_emp_id,
										jc_technician_emp_id, jc_grandtotal, jc_bill_cash_labour, jc_bill_cash_parts,
										jc_bill_cash_parts_valueadd, jc_bill_cash_parts_oil, jc_bill_cash_parts_tyre,
										jc_bill_cash_parts_accessories,
										jc_bill_cash_labour_discamt, jc_advice, jc_notes,
										jc_entry_id, jc_entry_date, veh_id);
								propcount++;
							}

							conntx.commit();
						} catch (Exception e) {
							if (conntx.isClosed()) {
								SOPError("connection is closed.....");
							}
							if (!conntx.isClosed() && conntx != null) {
								conntx.rollback();
							}
							msg = "<br>Transaction Error!";
							SOPError("Axelaauto== " + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
						} finally {
							conntx.setAutoCommit(true);
							stmttx.close();
							if (conntx != null && !conntx.isClosed()) {
								conntx.close();
							}
						}
					}
				}
			}
			if (!error_msg.equals("")) {
				error_msg = "<br>" + ++count + "." + " RO Number: " + jc_ro_no + "==><br>" + error_msg;
			}
			return error_msg;
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	public void InsertVehicle(String error_msg, String jc_branch_id, String jc_bill_cash_date, String customer_name,
			String contact_fname, String contact_lname, String contact_city, String contact_mobile1, String contact_phone1,
			String contact_title_id, String contact_email1, String contact_address, String contact_dob, String contact_anniversary, String contact_pin,
			String veh_engine_no, String veh_lastservice, String veh_lastservice_kms,
			String veh_service_duekms, String veh_service_duedate, String veh_sale_date, String jc_chassis_no, String jc_reg_no,
			String variant_name, String jc_jctype_id, String contact_city_id, String contact_state,
			String variant_id, String model_brand_id, String veh_modelyear, String jc_jccat_id, String jc_kms, String jc_ro_no, String jc_bill_cash_no,
			String jc_time_in, String jc_time_promised, String jc_time_ready, String jc_time_out, String jc_emp_id,
			String jc_technician_emp_id, String jc_grandtotal, String jc_bill_cash_labour, String jc_bill_cash_parts,
			String jc_bill_cash_parts_valueadd, String jc_bill_cash_parts_oil, String jc_bill_cash_parts_tyre,
			String jc_bill_cash_parts_accessories,
			String jc_bill_cash_labour_discamt, String jc_advice, String jc_notes, String jc_entry_id, String jc_entry_date) throws SQLException
	{

		// try {
		String customer_id = "0";
		String contact_id = "0";
		String contact_mobile2 = "", contact_email2 = "";
		String vehstock_id = "0";
		String jc_id = "0";
		String so_id = "0";
		String veh_id = "0", option_id = "0";
		String interior = "", exterior = "";
		ResultSet rs = null;
		String jc_bill_insur_labour_discamt = "0";
		double jc_discamt = Double.parseDouble(jc_bill_cash_labour_discamt) + Double.parseDouble(jc_bill_insur_labour_discamt);

		if (error_msg.equals("")) {
			if (!variant_id.equals("0")) {
				if (!contact_mobile1.equals("")) {
					StrSql = "SELECT customer_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE (customer_mobile1 = '" + contact_mobile1 + "'"
							+ " OR customer_mobile2 = '" + contact_mobile1 + "')";
					// SOP("check mobile1==" + StrSql);
					customer_id = CNumeric(ExecuteQuery(StrSql));
				}
				if (customer_id.equals("0") && !contact_mobile2.equals("")) {
					StrSql = "SELECT customer_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE (customer_mobile1 = '" + contact_mobile2 + "'"
							+ " OR customer_mobile2 = '" + contact_mobile2 + "')";
					// SOP("check mobile2==" + StrSql);
					customer_id = CNumeric(ExecuteQuery(StrSql));
				}
				if (!customer_id.equals("0")) {

					// Check Service type contact.
					StrSql = "SELECT contact_id"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
							+ " WHERE contact_contacttype_id = 9"
							+ " AND customer_id =" + customer_id;
					// SOP("check Service type==" + StrSql);
					contact_id = CNumeric(ExecuteQuery(StrSql));

				}
				// SOP("contact_id==" + contact_id);
				// SOP("customer_id==" + customer_id);
				if (!contact_id.equals("0")) {
					updateContact(contact_id, contact_mobile1, contact_mobile2, jc_entry_id, jc_entry_date);
				}
				if (customer_id.equals("0")) {
					customer_id = AddCustomer(jc_branch_id, customer_name, contact_phone1, contact_mobile1, contact_mobile2,
							contact_email1, contact_email2, contact_address, contact_city_id, contact_pin,
							jc_entry_id, jc_entry_date);
				}
				if (!customer_id.equals("0") && contact_id.equals("0")) {
					contact_id = AddContact(customer_id, contact_title_id, contact_fname, contact_lname, contact_mobile1, contact_mobile2,
							contact_phone1, contact_email1, contact_email2, contact_address, contact_city_id, contact_pin, contact_dob,
							contact_anniversary, jc_entry_id, jc_entry_date);
				}

				customer_id = CNumeric(customer_id);
				contact_id = CNumeric(contact_id);
				so_id = CNumeric(so_id);
				variant_id = CNumeric(variant_id);
				jc_kms = CNumeric(jc_kms);
				veh_service_duekms = CNumeric(veh_service_duekms);

				if (!contact_id.equals("0") && !customer_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
							+ " (veh_branch_id, "
							+ " veh_customer_id,"
							+ " veh_contact_id,"
							+ " veh_so_id,"
							+ " veh_variant_id,"
							+ " veh_modelyear,"
							+ " veh_chassis_no,"
							+ " veh_engine_no,"
							+ " veh_reg_no,"
							+ " veh_sale_date,"
							+ " veh_emp_id,"
							+ " veh_kms,"
							+ " veh_lastservice,"
							+ " veh_lastservice_kms,"
							+ " veh_service_duedate,"
							+ " veh_service_duekms,"
							+ " veh_crmemp_id,"
							+ " veh_insuremp_id,"
							+ " veh_notes,"
							+ " veh_entry_id,"
							+ " veh_entry_date)"
							+ " VALUES"
							+ " (" + jc_branch_id + ","
							+ customer_id + ","
							+ " " + contact_id + ","
							+ " " + so_id + ","
							+ " " + variant_id + ","
							+ " '" + veh_modelyear + "',"
							+ " '" + jc_chassis_no + "',"
							+ " '" + veh_engine_no + "',"
							+ " '" + jc_reg_no.replace(" ", "").replace("-", "") + "',"
							+ " '" + veh_sale_date + "',"
							+ " " + jc_emp_id + ","
							+ " " + jc_kms + ","
							+ " '" + veh_lastservice + "',"
							+ " " + veh_lastservice_kms + ","
							+ " '" + veh_service_duedate + "',"
							+ " " + veh_service_duekms + ","
							+ ExecuteQuery("SELECT emp_id "
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE 1=1"
									+ " AND emp_crm = 1"
									+ " AND emp_active = 1 "
									+ " ORDER BY RAND()"
									+ " LIMIT 1") + ","
							+ " 0," // veh_insuremp_id
							+ " '" + variant_name + "',"
							+ " " + jc_entry_id + ","
							+ " '" + jc_entry_date + "')";
					// SOP("StrSql==Insert Vehicle==" + StrSql);
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						veh_id = rs.getString(1);
					}
					rs.close();
				}
				veh_id = CNumeric(veh_id);
				vehstock_id = CNumeric(vehstock_id);
				if (!veh_id.equals("0")) {
					StrSql = "SELECT emp_id"
							+ " FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_active = 1"
							+ " AND emp_insur = 1"
							+ " ORDER BY RAND()"
							+ " LIMIT 1";
					String insurpolicy_emp_id = CNumeric(ExecuteQuery(StrSql));
					if (insurpolicy_emp_id.equals("0")) {
						insurpolicy_emp_id = CNumeric(jc_emp_id);
					}
				}

				if (!veh_id.equals("0") && !vehstock_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
							+ " (vehtrans_option_id,"
							+ " vehtrans_veh_id)"
							+ " SELECT trans_option_id," + " " + veh_id + ""
							+ " FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
							+ " WHERE trans_vehstock_id = " + vehstock_id + "";
					// SOP("strsql==Insert Veh Kms==" + StrSqlBreaker(StrSql));
					stmttx.execute(StrSql);

				} else if (!veh_id.equals("0")) {
					option_id = "0";
					if (!model_brand_id.equals("0") && !interior.equals("")) {
						StrSql = "SELECT option_id"
								+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
								+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
								+ " WHERE option_name = '" + interior + "'"
								+ " AND option_brand_id = " + model_brand_id;
						option_id = CNumeric(ExecuteQuery(StrSql));
					};
					option_id = CNumeric(option_id);

					if (!option_id.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
								+ " (vehtrans_option_id,"
								+ " vehtrans_veh_id)"
								+ " VALUES"
								+ " (" + option_id + "," + " "
								+ veh_id + ")";
						// SOP("strsql===else===veh option trans1======="+StrSqlBreaker(StrSql));
						stmttx.execute(StrSql);
					}
					// SOP("interior==" + interior + " exterior=="+ exterior);
					if (!interior.equals(exterior)) {
						option_id = "0";
						if (!model_brand_id.equals("0") && !exterior.equals("")) {
							StrSql = "SELECT option_id"
									+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
									+ " WHERE option_name = '" + exterior + "'"
									+ " AND option_brand_id = " + model_brand_id;
							option_id = CNumeric(ExecuteQuery(StrSql));
						}
						if (!option_id.equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " (vehtrans_option_id,"
									+ " vehtrans_veh_id)"
									+ " VALUES" + " ("
									+ option_id + "," + " "
									+ veh_id + ")";
							// SOP("strsql======veh option trans2======="+StrSqlBreaker(StrSql));
							stmttx.execute(StrSql);
						}
					}
				}
				conntx.commit();
				if (!contact_id.equals("0") && !customer_id.equals("0") && !veh_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc"
							+ " (jc_branch_id,"
							+ " jc_no,"
							+ " jc_time_in,"
							+ " jc_customer_id,"
							+ " jc_contact_id,"
							+ " jc_veh_id,"
							+ " jc_reg_no,"
							+ " jc_variant_id,"
							+ " jc_jctype_id,"
							+ " jc_jccat_id,"
							+ " jc_kms,"
							+ " jc_discamt,"
							+ " jc_bill_cash_date,"
							+ " jc_bill_address,"
							+ " jc_bill_city,"
							+ " jc_bill_pin,"
							+ " jc_bill_state,"
							+ " jc_del_address,"
							+ " jc_del_city,"
							+ " jc_del_pin,"
							+ " jc_del_state,"
							+ " jc_notes,"
							+ " jc_cust_voice,"
							+ " jc_advice,"
							+ " jc_instructions,"
							+ " jc_terms,"
							+ " jc_emp_id,"
							+ " jc_technician_emp_id,"
							+ " jc_inventory,"
							+ " jc_ro_no,"
							+ " jc_bill_cash_no,"
							+ " jc_time_promised,"
							+ " jc_time_ready,"
							+ " jc_time_out,"
							+ " jc_jcstage_id,"
							+ " jc_grandtotal,"
							+ " jc_bill_cash_labour,"
							+ " jc_bill_cash_parts,"
							+ " jc_bill_cash_parts_valueadd, "
							+ " jc_bill_cash_parts_oil, "
							+ " jc_bill_cash_parts_tyre,"
							+ " jc_bill_cash_parts_accessories,"
							+ " jc_bill_cash_labour_discamt,"
							+ " jc_active,"
							+ " jc_entry_id,"
							+ " jc_entry_date)"
							+ " VALUES"
							+ " "
							+ "(" + jc_branch_id + ","
							+ " (SELECT COALESCE(MAX(jc.jc_no), 0) + 1"
							+ " FROM " + compdb(comp_id) + "axela_service_jc AS jc"
							+ " WHERE jc.jc_branch_id  = " + jc_branch_id + "),"
							+ " '" + jc_time_in + "',"
							+ " " + customer_id + ","
							+ " " + contact_id + ","
							+ " " + veh_id + ","
							+ " '" + jc_reg_no.replace(" ", "").replace("-", "") + "',"
							+ " " + variant_id + ","
							+ " " + jc_jctype_id + ","
							+ " " + jc_jccat_id + ","
							+ " " + jc_kms + ","
							+ " " + jc_discamt + ","
							+ " '" + jc_bill_cash_date + "',"
							+ " '" + contact_address + "',"
							+ " '" + contact_city + "',"
							+ " '" + contact_pin + "',"
							+ " '" + contact_state + "',"
							+ " '" + contact_address + "',"
							+ " '" + contact_city + "',"
							+ " '" + contact_pin + "',"
							+ " '" + contact_state + "',"
							+ " '" + jc_notes + "',"
							+ " ''," // jc_cust_voice
							+ " '" + jc_advice + "',"
							+ " ''," // jc_instructions
							+ " ''," // jc_terms
							+ " " + jc_emp_id + ","
							+ " " + jc_technician_emp_id + ","
							+ " ''," // jc_inventory
							+ " '" + jc_ro_no + "',"
							+ " '" + jc_bill_cash_no + "',"
							+ " '" + jc_time_promised + "',"
							+ " '" + jc_time_ready + "',"
							+ " '" + jc_time_out + "',"
							+ " 6," // jc_jcstage_id
							+ " '" + jc_grandtotal + "',"
							+ " '" + jc_bill_cash_labour + "',"
							+ " '" + jc_bill_cash_parts + "',"
							+ " '" + jc_bill_cash_parts_valueadd + "',"
							+ " '" + jc_bill_cash_parts_oil + "',"
							+ " '" + jc_bill_cash_parts_tyre + "',"
							+ " '" + jc_bill_cash_parts_accessories + "',"
							+ " '" + jc_bill_cash_labour_discamt + "',"
							+ " '1'," // jc_active
							+ " " + jc_entry_id + ","
							+ " '" + jc_entry_date + "')";
					// SOP("strsql==Insert JC==" + (StrSql));
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					rs = stmttx.getGeneratedKeys();

					while (rs.next()) {
						jc_id = rs.getString(1);
					}
					rs.close();
					// SOP("jc_id==else=" + jc_id);
					jc_id = CNumeric(jc_id);

					if (!jc_id.equals("0")) {
						SOP("insert before addpsf");
						if (brand_id.equals("2")) {
							AddPSFFields(jc_id, comp_id, jc_jccat_id, "0");
						}
						if (brand_id.equals("7")) {
							AddPSFFields(jc_id, comp_id, "0", jc_jctype_id);
						}
						SOP("insert after addpsf");
					}
				}
				// SOP("jc_kms====" + jc_kms);
				if (!jc_kms.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
							+ " (vehkms_veh_id,"
							+ " vehkms_kms,"
							+ " vehkms_entry_id,"
							+ " vehkms_entry_date)"
							+ " VALUES"
							+ " (" + veh_id + ","
							+ " " + jc_kms + ","
							+ " " + jc_entry_id + ","
							+ " '" + jc_entry_date + "')";
					// SOP("StrSql==else=service veh kms while adding vehicle==" + StrSql);
					stmttx.execute(StrSql);
				}
				if (!veh_id.equals("0") && !jc_id.equals("0")
						&& (comp_id.equals("1009")
								&& !jc_jctype_id.equals("4")
								&& !jc_jctype_id.equals("5")
								&& !jc_jctype_id.equals("7")
								&& !jc_jctype_id.equals("8")
								&& !jc_jctype_id.equals("13"))) {
					// SOP("insert before  updateveh");
					UpdateVehicleFollowup(veh_id, jc_id, jc_time_in, jc_branch_id);
					// SOP("insert after updateveh");
				}

			}
		}

		// }
		// catch (Exception ex) {
		// try {
		// if (conntx.isClosed() && conntx != null) {
		// SOPError("connection is closed...");
		// }
		// if (!conntx.isClosed() && conntx != null) {
		// conntx.rollback();
		// SOPError("connection rollback...");
		// }
		// msg = "<br>Transaction Error!";
		// SOPError("Axelaauto== " + this.getClass().getName());
		// SOPError("Error in "
		// + new Exception().getStackTrace()[0].getMethodName() + ": "
		// + ex);
		// } catch (Exception e) {
		// msg = "<br>Transaction Error!";
		// SOPError("Axelaauto== " + this.getClass().getName());
		// SOPError("Error in "
		// + new Exception().getStackTrace()[0].getMethodName() + ": "
		// + ex);
		// }
		// }
	}
	public void UpdateVehicle(String error_msg, String jc_branch_id, String jc_bill_cash_date, String customer_name,
			String contact_fname, String contact_lname, String contact_city, String contact_mobile1,
			String contact_phone1, String contact_title_id, String contact_email1, String contact_address,
			String contact_dob, String contact_anniversary, String contact_pin,
			String veh_engine_no, String veh_lastservice_kms, String veh_service_duekms, String veh_service_duedate, String veh_sale_date,
			String jc_chassis_no, String jc_reg_no, String variant_name, String jc_jctype_id, String contact_city_id, String contact_state,
			String variant_id, String model_brand_id, String veh_modelyear, String jc_jccat_id, String jc_kms, String jc_ro_no, String jc_bill_cash_no,
			String jc_time_in, String jc_time_promised, String jc_time_ready, String jc_time_out, String jc_emp_id,
			String jc_technician_emp_id, String jc_grandtotal, String jc_bill_cash_labour, String jc_bill_cash_parts,
			String jc_bill_cash_parts_valueadd, String jc_bill_cash_parts_oil, String jc_bill_cash_parts_tyre,
			String jc_bill_cash_parts_accessories,
			String jc_bill_cash_labour_discamt, String jc_advice, String jc_notes,
			String jc_entry_id, String jc_entry_date, String veh_id) throws SQLException {
		// try {
		if (error_msg.equals("")) {
			String customer_id = "0";
			String contact_id = "0";
			String contact_mobile2 = "", contact_email2 = "";
			String soe_id = "0", sob_id = "0";
			String vehstock_id = "0";
			String jc_id = "0";
			String veh_lastservice = "";
			String jc_bill_insur_labour_discamt = "0";
			double jc_discamt = Double.parseDouble(jc_bill_cash_labour_discamt) + Double.parseDouble(jc_bill_insur_labour_discamt);

			stmttx = conntx.createStatement();
			StrSql = " SELECT veh_customer_id,veh_contact_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE 1= 1"
					+ " AND veh_id = " + veh_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("veh_customer_id");
					contact_id = crs.getString("veh_contact_id");
				}
				if (!contact_id.equals("0")) {
					// checking for contact_contacttype_id Service
					contact_id = CNumeric(ExecuteQuery("SELECT  contact_id  FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
							+ " WHERE contact_contacttype_id = 9"
							+ " AND (customer_mobile1 = '" + contact_mobile1 + "'"
							+ " OR customer_mobile2 = '" + contact_mobile1 + "')"
							+ " AND customer_id =" + customer_id));
					if (contact_id.equals("0") && !contact_mobile2.equals("")) {
						contact_id = CNumeric(ExecuteQuery("SELECT  contact_id  FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
								+ " WHERE contact_contacttype_id = 9"
								+ " AND (customer_mobile1 = '" + contact_mobile2 + "'"
								+ " OR customer_mobile2 = '" + contact_mobile2 + "')"
								+ " AND customer_id =" + customer_id));
					}
					if (!contact_id.equals("0")) {
						updateContact(contact_id, contact_mobile1, contact_mobile2, jc_entry_id, jc_entry_date);
					} else if (contact_id.equals("0")) {
						contact_id = AddContact(customer_id, contact_title_id, contact_fname, contact_lname, contact_mobile1, contact_mobile2,
								contact_phone1, contact_email1, contact_email2, contact_address, contact_city_id, contact_pin, contact_dob,
								contact_anniversary, jc_entry_id, jc_entry_date);
					}
				}
			} else {
				customer_id = AddCustomer(jc_branch_id, customer_name, contact_phone1, contact_mobile1, contact_mobile2,
						contact_email1, contact_email2, contact_address, contact_city_id, contact_pin,
						jc_entry_id, jc_entry_date);
				contact_id = AddContact(customer_id, contact_title_id, contact_fname, contact_lname, contact_mobile1, contact_mobile2,
						contact_phone1, contact_email1, contact_email2, contact_address, contact_city_id, contact_pin, contact_dob,
						contact_anniversary, jc_entry_id, jc_entry_date);
			}
			crs.close();

			if (!variant_id.equals("0")) {

				// For updating the recent kms of the vehicle into veh table
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET ";
				if (!customer_id.equals("0")) {
					StrSql += " veh_customer_id = " + customer_id + ",";
				}
				if (!contact_id.equals("0")) {
					StrSql += " veh_contact_id = " + contact_id + ",";
				}
				if (!variant_id.equals("0")) {
					StrSql += " veh_variant_id = '" + variant_id + "',";
				}
				if (!veh_modelyear.equals("")) {
					StrSql += " veh_modelyear = '" + veh_modelyear + "',";
				}
				if (!jc_reg_no.equals("")) {
					StrSql += " veh_reg_no = '" + jc_reg_no.replace(" ", "").replace("-", "") + "',";
				}
				if (!jc_chassis_no.equals("")) {
					StrSql += " veh_chassis_no = '" + jc_chassis_no + "',";
				}
				if (!veh_engine_no.equals("")) {
					StrSql += " veh_engine_no = '" + veh_engine_no + "',";
				}
				if (!jc_kms.equals("0")) {
					StrSql += " veh_kms = " + jc_kms + ",";
				}
				if (!veh_lastservice.equals("")) {
					StrSql += " veh_lastservice = '" + veh_lastservice + "',";
				}
				if (!veh_lastservice_kms.equals("0")) {
					StrSql += " veh_lastservice_kms = " + veh_lastservice_kms + ",";
				}
				if (!veh_service_duedate.equals("")) {
					StrSql += " veh_service_duedate = '" + veh_service_duedate + "',";
				}
				if (!veh_service_duekms.equals("0")) {
					StrSql += " veh_service_duekms = " + veh_service_duekms + ",";
				}
				if (!veh_sale_date.equals("")) {
					StrSql += " veh_sale_date = " + veh_sale_date + ",";
				}
				if (!jc_emp_id.equals("0")) {
					StrSql += " veh_emp_id = " + jc_emp_id + ",";
				}

				if (!jc_emp_id.equals("0") || !veh_service_duekms.equals("0") || !veh_service_duedate.equals("")
						|| !veh_lastservice_kms.equals("0") || !veh_lastservice.equals("") || !jc_kms.equals("0")
						|| !veh_engine_no.equals("") || !jc_chassis_no.equals("") || !contact_id.equals("0")
						|| !customer_id.equals("0")) {
					StrSql += " veh_modified_id = " + jc_entry_id + ","
							+ " veh_modified_date  = '" + jc_entry_date + "',";
				}
				StrSql = StrSql.substring(0, StrSql.length() - 1);
				StrSql += " WHERE veh_id =" + veh_id;
				// SOP("strsql====veh update===" + StrSql);
				stmttx.execute(StrSql);
				if (!contact_id.equals("0") && !customer_id.equals("0") && !veh_id.equals("0")) {

					// For inserting the recent kms of the vehicle into veh_kms table
					if (!jc_kms.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
								+ " (vehkms_veh_id,"
								+ " vehkms_kms,"
								+ " vehkms_entry_id,"
								+ " vehkms_entry_date)"
								+ " VALUES"
								+ " (" + veh_id + ","
								+ " " + jc_kms + ","
								+ " " + jc_entry_id + ","
								+ " '" + jc_entry_date + "')";
						// SOP(("strsql===Insert veh kms===" + StrSqlBreaker(StrSql));
						stmttx.execute(StrSql);

					}
					conntx.commit();
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc"
							+ " (jc_branch_id,"
							+ " jc_no,"
							+ " jc_time_in,"
							+ " jc_customer_id,"
							+ " jc_contact_id,"
							+ " jc_veh_id,"
							+ " jc_reg_no,"
							+ " jc_variant_id,"
							+ " jc_jctype_id,"
							+ " jc_jccat_id,"
							+ " jc_kms,"
							+ " jc_discamt,"
							+ " jc_bill_cash_date,"
							+ " jc_bill_address,"
							+ " jc_bill_city,"
							+ " jc_bill_pin,"
							+ " jc_bill_state,"
							+ " jc_del_address,"
							+ " jc_del_city,"
							+ " jc_del_pin,"
							+ " jc_del_state,"
							+ " jc_notes,"
							+ " jc_cust_voice,"
							+ " jc_advice,"
							+ " jc_instructions,"
							+ " jc_terms,"
							+ " jc_emp_id,"
							+ " jc_technician_emp_id,"
							+ " jc_inventory,"
							+ " jc_ro_no,"
							+ " jc_bill_cash_no,"
							+ " jc_time_promised,"
							+ " jc_time_ready,"
							+ " jc_time_out,"
							+ " jc_jcstage_id,"
							+ " jc_grandtotal,"
							+ " jc_bill_cash_labour,"
							+ " jc_bill_cash_parts,"
							+ " jc_bill_cash_parts_valueadd, "
							+ " jc_bill_cash_parts_oil, "
							+ " jc_bill_cash_parts_tyre,"
							+ " jc_bill_cash_parts_accessories,"
							+ " jc_bill_cash_labour_discamt,"
							+ " jc_active,"
							+ " jc_entry_id,"
							+ " jc_entry_date)"
							+ " VALUES"
							+ " "
							+ "(" + jc_branch_id + ","
							+ " (SELECT COALESCE(MAX(jc.jc_no), 0) + 1"
							+ " FROM " + compdb(comp_id) + "axela_service_jc AS jc"
							+ " WHERE jc.jc_branch_id  = " + jc_branch_id + "),"
							+ " '" + jc_time_in + "',"
							+ " " + customer_id + ","
							+ " " + contact_id + ","
							+ " " + veh_id + ","
							+ " '" + jc_reg_no.replace(" ", "").replace("-", "") + "',"
							+ " " + variant_id + ","
							+ " " + jc_jctype_id + ","
							+ " " + jc_jccat_id + ","
							+ " " + jc_kms + ","
							+ " " + jc_discamt + ","
							+ " '" + jc_bill_cash_date + "',"
							+ " '" + contact_address + "',"
							+ " '" + contact_city + "',"
							+ " '" + contact_pin + "',"
							+ " '" + contact_state + "',"
							+ " '" + contact_address + "',"
							+ " '" + contact_city + "',"
							+ " '" + contact_pin + "',"
							+ " '" + contact_state + "',"
							+ " '" + jc_notes + "',"
							+ " ''," // jc_cust_voice
							+ " '" + jc_advice + "',"
							+ " ''," // jc_instructions
							+ " ''," // jc_terms
							+ " " + jc_emp_id + ","
							+ " " + jc_technician_emp_id + ","
							+ " ''," // jc_inventory
							+ " '" + jc_ro_no + "',"
							+ " '" + jc_bill_cash_no + "',"
							+ " '" + jc_time_promised + "',"
							+ " '" + jc_time_ready + "',"
							+ " '" + jc_time_out + "',"
							+ " 6," // jc_jcstage_id
							+ " '" + jc_grandtotal + "',"
							+ " '" + jc_bill_cash_labour + "',"
							+ " '" + jc_bill_cash_parts + "',"
							+ " '" + jc_bill_cash_parts_valueadd + "',"
							+ " '" + jc_bill_cash_parts_oil + "',"
							+ " '" + jc_bill_cash_parts_tyre + "',"
							+ " '" + jc_bill_cash_parts_accessories + "',"
							+ " '" + jc_bill_cash_labour_discamt + "',"
							+ " '1'," // jc_active
							+ " " + jc_entry_id + ","
							+ " '" + jc_entry_date + "')";
					// SOP("strsql==Insert JC==" + (StrSql));
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();

					while (rs.next()) {
						jc_id = rs.getString(1);
					}
					rs.close();
					// SOP("jc_id==else=" + jc_id);
					jc_id = CNumeric(jc_id);

					if (!jc_id.equals("0")) {
						// SOP(" before add psf");
						if (brand_id.equals("2")) {
							AddPSFFields(jc_id, comp_id, jc_jccat_id, "0");
						}
						if (brand_id.equals("7")) {
							AddPSFFields(jc_id, comp_id, "0", jc_jctype_id);
						}

						// SOP("after add psf");
					}
					if (!veh_id.equals("0") && !jc_id.equals("0")
							&& (comp_id.equals("1009")
									&& !jc_jctype_id.equals("4")// RUNNING RE
									&& !jc_jctype_id.equals("5")// Repeat Repair
									&& !jc_jctype_id.equals("7")// Body Shop
									&& !jc_jctype_id.equals("8")// Running Repair
							&& !jc_jctype_id.equals("13"))) {// BODY REPAI
						// SOP("insert before  updateveh");
						UpdateVehicleFollowup(veh_id, jc_id, jc_time_in, jc_branch_id);
						// SOP("insert after updateveh");
					}
				}
			}

		}
		// } catch (Exception ex) {
		// try {
		// if (conntx.isClosed() && conntx != null) {
		// SOPError("connection is closed...");
		// }
		// if (!conntx.isClosed() && conntx != null) {
		// conntx.rollback();
		// SOPError("connection rollback...");
		// }
		// msg = "<br>Transaction Error!";
		// SOPError("Axelaauto== " + this.getClass().getName());
		// SOPError("Error in "
		// + new Exception().getStackTrace()[0].getMethodName() + ": "
		// + ex);
		// } catch (Exception e) {
		// msg = "<br>Transaction Error!";
		// SOPError("Axelaauto== " + this.getClass().getName());
		// SOPError("Error in "
		// + new Exception().getStackTrace()[0].getMethodName() + ": "
		// + ex);
		// }
		// }
	}
	public void AddPSFFields(String jc_id, String comp_id, String jc_jccat_id, String jc_jctype_id) throws SQLException {
		try {
			StrSql = "SELECT jc_id,"
					+ " COALESCE((SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service_psf = 1"
					+ " AND emp_active = 1"
					+ " AND (emp_branch_id = jc_branch_id"
					+ " OR emp_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp_branch"
					+ " WHERE emp_branch_id = jc_branch_id ))"
					+ " AND FIND_IN_SET(emp_id,psfdays_emp_id)"
					+ " LIMIT 1), 1),"
					+ " psfdays_id,"
					+ " DATE_FORMAT(DATE_ADD(CONCAT(SUBSTR(jc_time_out, 1, 8), SUBSTR(jc_time_out, 9, 14)),"
					+ " INTERVAL (psfdays_daycount - 1) DAY	),'%Y%m%d%H%i%s')"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_brand_id = branch_brand_id"
					+ " WHERE jc_id =" + jc_id
					+ " AND psfdays_active = 1";
			if (!jc_jccat_id.equals("0")) {
				StrSql += " AND jc_jccat_id IN ( SELECT psfdayscattrans_jccat_id"
						+ " FROM " + compdb(comp_id) + "axela_psfdayscattrans"
						+ " WHERE 1 = 1"
						+ " AND psfdayscattrans_psfdays_id = psfdays_id )";
			}
			if (!jc_jctype_id.equals("0")) {
				StrSql += " AND jc_jctype_id IN ( SELECT psfdaytypestrans_jctype_id"
						+ " FROM " + compdb(comp_id) + "axela_psfdaystypetrans"
						+ " WHERE 1 = 1"
						+ " AND psfdaytypestrans_psfdays_id = psfdays_id )";
			}
			// SOP("strsql============selectpsf==============" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf"
					+ " (jcpsf_jc_id,"
					+ " jcpsf_emp_id,"
					+ " jcpsf_psfdays_id,"
					+ " jcpsf_followup_time" + " ) "
					+ StrSql;
			// SOP("strsql==AddPSFFields==" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void UpdateVehicleFollowup(String veh_id, String jc_id, String jc_time_in, String jc_branch_id) throws SQLException {
		try {

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_desc= ''"
					+ " AND vehfollowup_vehaction_id != 1"
					+ " AND vehfollowup_veh_id= " + veh_id;
			// SOP("delete===" + StrSql);

			stmttx.addBatch(StrSql);

			StrSql = "SELECT vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(vehfollowup_appt_time, 1, 8) >= DATE_FORMAT(DATE_SUB(veh_lastservice,INTERVAL 90 DAY),' %Y%m%d ')"
					+ " AND SUBSTR(vehfollowup_appt_time, 1, 8) <= SUBSTR(veh_lastservice,1, 8)"
					+ " AND vehfollowup_contactable_id = 1"
					+ " AND vehfollowup_veh_id= " + veh_id
					+ " AND vehfollowup_veh_id NOT IN (SELECT jccheck.vehfollowup_veh_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup AS jccheck "
					+ " WHERE jccheck.vehfollowup_jc_id = " + jc_id + ")";

			// SOP("StrSql===1======" + StrSql);
			String vehfollowup_emp_id = "";
			if (!ExecuteQuery(StrSql).equals("")) {
				vehfollowup_emp_id = "(SELECT IF(veh_crmemp_id = 0, 1, veh_crmemp_id)"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE veh_id = " + veh_id + ")";
			} else {
				vehfollowup_emp_id = "1";
			}

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
					+ " (vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " vehfollowup_vehcalltype_id,"
					+ " vehfollowup_dueservice,"
					+ " vehfollowup_contactable_id,"
					+ " vehfollowup_desc,"
					+ " vehfollowup_followup_time,"
					+ " vehfollowup_jc_id,"
					+ " vehfollowup_followup_main,"
					+ " vehfollowup_entry_id,"
					+ " vehfollowup_entry_time)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + vehfollowup_emp_id + ","
					+ "	COALESCE((SELECT calltype.vehfollowup_vehcalltype_id"
					+ " FROM " + compdb(comp_id) + "axela_service_followup calltype"
					+ " WHERE calltype.vehfollowup_veh_id = " + veh_id
					+ " ORDER BY calltype.vehfollowup_id DESC"
					+ " LIMIT 1), 0 ),"
					+ "	COALESCE((SELECT dueservice.vehfollowup_dueservice"
					+ " FROM " + compdb(comp_id) + "axela_service_followup dueservice"
					+ " WHERE dueservice.vehfollowup_veh_id = " + veh_id
					+ " ORDER BY dueservice.vehfollowup_id DESC"
					+ " LIMIT 1), 0 ),"
					+ " " + 1 + "," // vehfollowup_contactable_id
					+ " CONCAT('Already serviced at ',(SELECT branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id=" + jc_branch_id + ")),"
					+ " '" + jc_time_in + "',"
					+ " " + jc_id + ","
					+ " " + 0 + "," // vehfollowup_followup_main
					+ " " + 1 + "," // vehfollowup_entry_id
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP(("StrSql=====Already Serviced====" + StrSql);
			stmttx.addBatch(StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
					+ " (vehfollowup_veh_id,"
					+ " vehfollowup_emp_id,"
					+ " vehfollowup_vehcalltype_id,"
					+ " vehfollowup_desc,"
					+ " vehfollowup_followup_time,"
					+ " vehfollowup_followup_main,"
					+ " vehfollowup_entry_id,"
					+ " vehfollowup_entry_time)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + vehfollowup_emp_id + ","
					+ " " + 97 + "," // vehfollowup_vehcalltype_id
					+ " '',"
					+ " '" + ConvertLongDateToStr(AddDayMonthYearStr(jc_time_in, 0, 0, 3, 0)) + "',"
					+ " " + 1 + ","// vehfollowup_followup_main
					+ " " + 1 + ","// vehfollowup_entry_id
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP(("StrSql==New follow-up==" + StrSql);
			stmttx.addBatch(StrSql);

			// // For Veh Contactable Status
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
					+ " SET veh_contactable_id = '" + 1 + "'"
					+ " WHERE 1=1 "
					+ " AND veh_id = " + veh_id;
			SOP("StrSql====contactable add====" + StrSql);
			stmttx.addBatch(StrSql);
			stmttx.executeBatch();
		} catch (Exception e) {
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}

	}

	// public void AddInsurFollowupFields(String veh_id, String veh_sale_date,
	// String insurpolicy_emp_id) throws SQLException {
	// try {
	// StrSql = "INSERT INTO " + compdb(comp_id)
	// + "axela_insurance_followup"
	// + " (insurfollowup_veh_id," + " insurfollowup_emp_id,"
	// + " insurfollowup_followup_time,"
	// + " insurfollowup_followuptype_id,"
	// + " insurfollowup_priorityinsurfollowup_id,"
	// + " insurfollowup_desc," + " insurfollowup_entry_id,"
	// + " insurfollowup_entry_time," + " insurfollowup_trigger)"
	// + " VALUES" + " (" + veh_id + "," + " " + insurpolicy_emp_id
	// + "," + " CONCAT(SUBSTR('" + ToLongDate(kknow())
	// + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date
	// + ", INTERVAL 11 MONTH), '%Y%m%d%h%i%s')), 5))," + " 1,"
	// + " 1," + " ''," + " " + jc_entry_id + "," + " '" + jc_entry_date
	// + "'," + " 0)";
	// // SOP("StrSql===insur followup==" + StrSql);
	// stmttx.execute(StrSql);
	//
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh" + " SET"
	// + " veh_insuremp_id = " + insurpolicy_emp_id + ""
	// + " WHERE veh_id = " + veh_id + "";
	// // SOP("strsql==update=veh=insur==" + StrSqlBreaker(StrSql));
	// stmttx.execute(StrSql);
	// } catch (Exception ex) {
	// if (conntx.isClosed()) {
	// SOPError("connection is closed...");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + ex);
	// }
	// }
	public String AddCustomer(String jc_branch_id, String customer_name, String contact_phone1, String contact_mobile1, String contact_mobile2,
			String contact_email1, String contact_email2, String contact_address, String contact_city_id, String contact_pin,
			String jc_entry_id, String jc_entry_date) throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_phone1,"
				+ " customer_mobile1,"
				+ " customer_mobile1_phonetype_id,"
				+ " customer_mobile2,"
				+ " customer_mobile2_phonetype_id,"
				+ " customer_email1,"
				+ " customer_email2,"
				+ " customer_address,"
				+ " customer_city_id,"
				+ " customer_pin,"
				+ " customer_since,"
				+ " customer_accgroup_id,"
				+ " customer_type,"
				+ " customer_active,"
				+ " customer_notes,"
				+ " customer_entry_id,"
				+ " customer_entry_date)"
				+ " VALUES"
				+ " (" + jc_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + contact_phone1 + "',"
				+ " '" + contact_mobile1 + "',"
				+ " 3," // Service customer_mobile1_phonetype_id
				+ " '" + contact_mobile2 + "',"
				+ " 3," // Service customer_mobile2_phonetype_id
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " '" + ToShortDate(kknow()) + "',"
				+ " 32,"// customer_accgroup_id
				+ " 1," // customer_type
				+ " '1'," // customer_active
				+ " '',"
				+ " " + jc_entry_id + ","
				+ " '" + jc_entry_date + "')";
		// SOP("StrSql==INSERT INTO customer==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			customer_id = rs.getString(1);
		}
		rs.close();
		return customer_id;
	}

	public String AddContact(String customer_id, String contact_title_id, String contact_fname, String contact_lname, String contact_mobile1,
			String contact_mobile2, String contact_phone1, String contact_email1, String contact_email2, String contact_address,
			String contact_city_id, String contact_pin, String contact_dob, String contact_anniversary, String jc_entry_id, String jc_entry_date)
			throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_contacttype_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_lname,"
				+ " contact_mobile1,"
				+ " contact_mobile1_phonetype_id,"
				+ " contact_mobile2,"
				+ " contact_mobile2_phonetype_id,"
				+ " contact_phone1,"
				+ " contact_email1,"
				+ " contact_email2,"
				+ " contact_address,"
				+ " contact_city_id,"
				+ " contact_pin,"
				+ " contact_dob,"
				+ " contact_anniversary,"
				+ " contact_notes,"
				+ " contact_active,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + customer_id + ","
				+ " 9," // contact_contacttype_id
				+ " " + contact_title_id + ","
				+ " '" + contact_fname + "',"
				+ " '" + contact_lname + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '3'," // Service contact_mobile1_phonetype_id
				+ " '" + contact_mobile2 + "',"
				+ " '3'," // Service contact_mobile2_phonetype_id
				+ " '" + contact_phone1 + "',"
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " '" + contact_dob + "',"
				+ " '" + contact_anniversary + "',"
				+ " '',"
				+ " '1',"
				+ " " + jc_entry_id + ","
				+ " '" + jc_entry_date + "')";
		// SOP("StrSql==INSERT INTO axela_customer_contact==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			contact_id = rs.getString(1);
		}
		rs.close();
		return contact_id;
	}
	public void updateContact(String contact_id, String contact_mobile1, String contact_mobile2, String jc_entry_id, String jc_entry_date) throws SQLException {

		stmttx = conntx.createStatement();
		// For updating Contact Details for existing vehicles...
		StrSql = " UPDATE " + compdb(comp_id) + "axela_customer_contact"
				+ " SET ";
		if (!contact_mobile1.equals("")) {
			StrSql += " contact_mobile1 = '" + contact_mobile1 + "',"
					+ " contact_mobile1_phonetype_id = 3,";
		}
		if (!contact_mobile2.equals("")) {
			StrSql += " contact_mobile2 = '" + contact_mobile2 + "',"
					+ " contact_mobile2_phonetype_id = 3,";
		}
		StrSql += " contact_modified_id = " + jc_entry_id + ","
				+ " contact_modified_date = '" + jc_entry_date + "'"
				+ " WHERE 1=1"
				// + " AND veh_vehsource_id = 2 "
				+ " AND contact_id = " + contact_id;
		// SOP("StrSql=veh Contact update==" + StrSql);
		stmttx.execute(StrSql);

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}
}