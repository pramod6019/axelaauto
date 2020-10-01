package axela.accounting;

//Shivaprasad 2014 Nov25

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ledger_Add extends Connect {

	public String emp_id = "0", emp_branch_id = "0", comp_id = "0";
	public String add = "";
	public String addB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String label = "Customer";

	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String entry_date = "";
	public String modified_date = "";
	public String emp_role_id = "0";
	public String customer = "";
	public String supplier = "";
	public String accsubgroup_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public String customer_emp_id = "0";
	public String customer_entry_id = "0";
	public String customer_entry_by = "";
	public String customer_entry_date = "";
	public String customer_modified_id = "0";
	public String customer_modified_by = "";
	public String customer_modified_date = "";
	public String customer_type = "";
	/* End of Config Variables */
	DecimalFormat deci = new DecimalFormat("#.##");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String checkPerm = "";
	public String vouchertype_id = "0", voucherclass_id = "0";
	public String contact_id = "0";
	public String customer_contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_pin = "";
	public String dr_month = "";
	public String dr_day = "";
	public String dr_year = "";
	public String contact_dnd = "0";
	public String link_contact_name = "";
	// / Customer variable
	public String customer_pan_no = "";
	public String customer_tin_no = "";
	public String customer_service_tax_no = "";
	public String customer_cst_no = "";
	public String customer_credit_limit = "";
	public String customer_paydays_id = "0";
	public String customer_disc_perc = "0.00";
	public String contact_city_id = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String customer_code = "";
	public String customer_email1 = "";
	public String customer_mobile1 = "";
	public String customer_branch_id = "0", customer_rateclass_id = "0";
	public String link_customer_name = "", voucherclass_file = "",
			customer_acc_cust = "0", customer_acc_supplier = "0";
	// vouchertype variables
	public String vouchertype_mobile = "1";
	public String vouchertype_email = "1";
	public String vouchertype_dob = "1";
	public String vouchertype_dnd = "1";
	public Ledger_Check ledgercheck = new Ledger_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_purchase_order_add,"
					+ "emp_acc_sales_order_add,"
					+ "emp_acc_sales_invoice_add,"
					+ "emp_acc_receipt_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				voucherclass_file = PadQuotes(request.getParameter("voucherclass_file"));
				customer = PadQuotes(request.getParameter("customer"));
				supplier = PadQuotes(request.getParameter("supplier"));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vouchertype_mobile = CNumeric(PadQuotes(request
						.getParameter("vouchertype_mobile")));
				vouchertype_email = CNumeric(PadQuotes(request
						.getParameter("vouchertype_email")));
				vouchertype_dob = CNumeric(PadQuotes(request
						.getParameter("vouchertype_dob")));
				vouchertype_dnd = CNumeric(PadQuotes(request
						.getParameter("vouchertype_dnd")));

				if (vouchertype_id.equals("12") || vouchertype_id.equals("20") || vouchertype_id.equals("21")) {
					label = "Supplier";
				}
				if (vouchertype_id.equals("0")
						&& (!customer.equals("yes") || !supplier.equals("yes"))) {
					response.sendRedirect(response
							.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ledger!"));
				} else {
					if (customer.equals("yes")) {
						accsubgroup_id = "32";
						customer_type = "1";
					} else if (supplier.equals("yes")) {
						accsubgroup_id = "31";
						customer_type = "2";
					}

					if (add.equals("yes")) {
						status = "Add";
						if (emp_branch_id.equals("0")) {
							branch_id = CNumeric(GetSession("voucher_branch_id",
									request));
							if (branch_id.equals("0")) {
								branch_id = getActiveBranchID(request, emp_id);
								SetSession("voucher_branch_id", branch_id, request);
							}

						} else {
							branch_id = emp_branch_id;
							branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code,')') AS branch_name"
									+ " FROM  " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = "
									+ branch_id);
						}

						if (!addB.equals("yes")) {
							customer_entry_date = ToLongDate(kknow());
							contact_title_id = "0";
							customer_emp_id = emp_id;
							contact_mobile1 = "91-";

						} else {
							if (ReturnPerm(comp_id, "emp_acc_purchase_order_add,"
									+ "emp_acc_sales_order_add,"
									+ "emp_acc_sales_invoice_add,"
									+ "emp_acc_receipt_add", request).equals("1")) {
								GetValues(request, response);
								AddFields();

								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response
											.encodeRedirectURL(""
													+ voucherclass_file
													+ "-update.jsp?add=yes&customer_add=yes&vouchertype_id="
													+ vouchertype_id
													+ "&voucherclass_id="
													+ voucherclass_id
													+ "&voucher_rateclass_id="
													+ customer_rateclass_id
													+ "&branch_id=" + branch_id
													+ "" + "&voucher_customer_id="
													+ customer_id + ""
													+ "&voucher_contact_id="
													+ customer_contact_id + ""
													+ "&voucher_paydays_id="
													+ customer_paydays_id
													+ "&contact_dnd=" + contact_dnd));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		customer_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		branch_id = customer_branch_id;
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		customer_code = PadQuotes(request.getParameter("txt_customer_code"));
		contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
		contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
		contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
		contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
		contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
		contact_address = PadQuotes(request.getParameter("txt_contact_address"));
		contact_city_id = PadQuotes(request.getParameter("maincity"));
		contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		contact_dnd = CheckBoxValue(PadQuotes(request
				.getParameter("chk_contact_dnd")));
		customer_pan_no = PadQuotes(request.getParameter("txt_customer_pan_no"));
		customer_tin_no = PadQuotes(request.getParameter("txt_customer_tin_no"));
		customer_service_tax_no = PadQuotes(request
				.getParameter("txt_customer_service_tax_no"));
		customer_cst_no = PadQuotes(request.getParameter("txt_customer_cst_no"));
		customer_credit_limit = CNumeric(PadQuotes(request
				.getParameter("txt_customer_credit_limit")));
		customer_paydays_id = CNumeric(PadQuotes(request
				.getParameter("dr_customer_paydays_id")));
		customer_disc_perc = CNumeric(PadQuotes(request
				.getParameter("txt_customer_disc_perc")));
		customer_emp_id = CNumeric(PadQuotes(request
				.getParameter("dr_executive")));
		customer_rateclass_id = CNumeric(PadQuotes(request
				.getParameter("drop_customer_rateclass_id")));
		customer_entry_by = PadQuotes(request.getParameter("entry_by"));
		customer_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		if (!customer_name.equals("")) {

			StrSql = "SELECT customer_name FROM  " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_name ='" + customer_name + "'"
					+ " AND customer_id !=" + customer_id
					+ " AND customer_accgroup_id = " + accsubgroup_id;

			if (!ExecutePrepQuery(StrSql, map, 0).equals("")) {
				msg += "<br>Similar Ledger Found!";
			}
		}
		if (contact_title_id.equals("0")) {
			msg += "<br>Select Title!";
		}

		if (contact_fname.equals("")) {
			msg += "<br>Enter First Name!";
		} else {
			contact_fname = toTitleCase(contact_fname);
		}

		if (!contact_lname.equals("")) {
			contact_lname = toTitleCase(contact_lname);
		}

		if (customer_name.equals("")) {
			customer_name = contact_fname + " " + contact_lname;
		} else {
			customer_name = toTitleCase(customer_name);
		}

		if (customer_code.equals("")) {
			msg += "<br>Enter " + label + " Code!";
		}
		if (contact_mobile1.equals("91-")) {
			contact_mobile1 = "";
		}
		if (contact_mobile1.equals("") && contact_phone1.equals("")) {
			msg += "<br>Enter Either Mobile or Phone!";
		}

		if (!contact_mobile1.equals("") && !IsValidMobileNo11(contact_mobile1)) {
			msg += "<br>Mobile No. is invalid!";
		}

		if (!contact_phone1.equals("") && !IsValidPhoneNo11(contact_phone1)) {
			msg += "<br>Phone No. is invalid!";
		}

		if (!contact_email1.equals("")) {
			if (!IsValidEmail(contact_email1)) {
				msg += "<br>Enter valid Contact Email!";
			} else {
				contact_email1 = contact_email1.toLowerCase();
			}
		}

		if (CNumeric(contact_city_id).equals("0")) {
			msg += "<br>Select City!";
		}

		if (customer_paydays_id.equals("-1")) {
			msg += "<br>Select Payment Days!";
		}

		if (Double.parseDouble(customer_disc_perc) > 99.00) {
			msg += "<br>Discount Percentage can't be greater than 99.00!";
		}

		if (customer_emp_id.equals("0")) {
			msg += "<br>Select Executive!";
		}
	}

	public void AddFields() throws Exception {
		CheckForm();

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_customer" + " ("
						+ " customer_branch_id," + " customer_name,"
						+ " customer_code,"
						+ " customer_mobile1,"
						+ " customer_phone1," + " customer_email1,"
						+ " customer_address," + " customer_city_id,"
						+ " customer_pin,"
						+ " customer_credit_limit,"
						+ " customer_paydays_id,"
						+ " customer_emp_id,"
						+ " customer_type,"
						+ " customer_accgroup_id,"
						+ " customer_since,"
						+ " customer_active,"
						+ " customer_notes,"
						+ " customer_entry_id,"
						+ " customer_entry_date)"
						+ " VALUES" + " (" + " "
						+ branch_id
						+ ","
						+ " '"
						+ customer_name
						+ "',"
						+ " '"
						+ customer_code
						+ "',"
						+ " '"
						+ contact_mobile1
						+ "',"
						+ " '"
						+ contact_phone1
						+ "',"
						+ " '"
						+ contact_email1
						+ "',"
						+ " '"
						+ contact_address
						+ "',"
						+ " "
						+ contact_city_id
						+ ","
						+ " '"
						+ contact_pin
						+ "',"
						+ " "
						+ customer_credit_limit
						+ ","
						+ " "
						+ customer_paydays_id
						+ ","
						+ " "
						+ customer_emp_id
						+ ","
						+ " "
						+ 1
						+ ","
						+ " "
						+ 32
						+ ","
						+ " '"
						+ ToShortDate(kknow())
						+ "',"
						+ " '1',"
						+ " '',"
						+ " "
						+ emp_id + "," + " '" + ToLongDate(kknow()) + "')";
				SOP("StrSql===cust111==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					customer_id = rs.getString(1);
				}
				rs.close();

				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_customer_contact"
						+ " (contact_customer_id," + " contact_title_id,"
						+ " contact_fname," + " contact_lname,"
						+ " contact_mobile1," + " contact_phone1,"
						+ " contact_email1," + " contact_address,"
						+ " contact_city_id," + " contact_pin,"
						+ " contact_notes,"
						+ " contact_active," + " contact_entry_id,"
						+ " contact_entry_date)" + " VALUES" + " ("
						+ customer_id
						+ ","
						+ " "
						+ contact_title_id
						+ ","
						+ " '"
						+ contact_fname
						+ "',"
						+ " '"
						+ contact_lname
						+ "',"
						+ " '"
						+ contact_mobile1
						+ "',"
						+ " '"
						+ contact_phone1
						+ "',"
						+ " '"
						+ contact_email1
						+ "',"
						+ " '"
						+ contact_address
						+ "',"
						+ " "
						+ contact_city_id
						+ ","
						+ " '"
						+ contact_pin
						+ "',"
						+ " '',"
						+ " '1',"
						+ " "
						+ emp_id
						+ ","
						+ " " + ToLongDate(kknow()) + ")";
				// SOP("StrSql===cust==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					customer_contact_id = rs1.getString(1);
				}
				rs1.close();
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
		// SOP("msg===="+msg);
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select Executive</option>\n");
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_sales = 1"
					+ " OR emp_id = 1"
					+ " GROUP BY emp_id" + " ORDER BY emp_name";
			// SOP("StrSql===cust==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop((int) crs.getDouble("emp_id"),
						customer_emp_id));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT title_id, title_desc" + " FROM  " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"),
						contact_title_id));
				Str.append(">").append(crs.getString("title_desc"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulatePaymentDays() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>");
		StrSql = "SELECT paydays_id, paydays_name" + " FROM  " + compdb(comp_id) + "axela_acc_paydays"
				+ " group by paydays_id" + " order by paydays_name";
		try {
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("paydays_id"));
				Str.append(StrSelectdrop(crs.getString("paydays_id"),
						customer_paydays_id));
				Str.append(">").append(crs.getString("paydays_name"));
				Str.append("</option>\n");
			}
			crs.close();
			map.clear();
			return Str.toString();
		} catch (Exception ex) {
			// SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>");
		Str.append("<option value = 1").append(Selectdrop(1, month))
				.append(">January</option>\n");
		Str.append("<option value = 2").append(Selectdrop(2, month))
				.append(">February</option>\n");
		Str.append("<option value = 3").append(Selectdrop(3, month))
				.append(">March</option>\n");
		Str.append("<option value = 4").append(Selectdrop(4, month))
				.append(">April</option>\n");
		Str.append("<option value = 5").append(Selectdrop(5, month))
				.append(">May</option>\n");
		Str.append("<option value = 6").append(Selectdrop(6, month))
				.append(">June</option>\n");
		Str.append("<option value = 7").append(Selectdrop(7, month))
				.append(">July</option>\n");
		Str.append("<option value = 8").append(Selectdrop(8, month))
				.append(">August</option>\n");
		Str.append("<option value = 9").append(Selectdrop(9, month))
				.append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, month))
				.append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, month))
				.append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, month))
				.append(">December</option>\n");
		return Str.toString();
	}

	public String PopulateDay(String day) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = 1; i <= 31; i++) {
			Str.append("<option value=").append(i).append(Selectdrop(i, day));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateYear(String year) {
		StringBuilder Str = new StringBuilder();
		// Calendar cal = Calendar.getInstance();
		// cal.add(Calendar.YEAR, -7);
		Str.append("<option value =-1>Select</option>");
		for (int i = Integer
				.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i >= 1900; i--) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, year)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}
}
