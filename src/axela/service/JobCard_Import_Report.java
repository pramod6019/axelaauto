package axela.service;
//aJIt 16th July 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class JobCard_Import_Report extends Connect {

	public String StrSql = "", StrHTML = "";
	public String comp_id = "0";
	public String msg = "", emp_id = "0", jc_service = "", jc_grandtotal = "0.0", month = "", day = "", year = "";
	public String emp_role_id = "0";
	public String prime_id = "0";

	// customer fields
	public String customer_id = "0", customer_name = "", customer_mobile1 = "", customer_phone1 = "", customer_email1 = "";
	public String customer_address = "", customer_city_id = "0", customer_pin = "", customer_emp_id = "0";

	// customer contact fields
	public String contact_id = "0", contact_title = "", contact_fname = "", contact_lname = "", contact_dob = "", contact_anniversary = "";
	public String contact_mobile1 = "";

	// Jobcard fields
	public String jc_id = "0", jc_branch_id = "0", jc_no = "", jc_time_in = "", jc_customer_id = "0", jc_bill_date = "";
	public String jc_contact_id = "0", jc_veh_id = "0", jc_reg_no = "", jc_jctype_id = "0";
	public String jc_jccat_id = "0", jc_kms = "", jc_emp_id = "0", jc_technician_emp_id = "0", jc_ro_no = "";
	public String jc_bill_cash_no = "", jc_jcstage_id = "0", jc_priorityjc_id = "0", jc_location_id = "0";
	public String jc_bill_cash_customername = "", jc_bill_cash_date = "", jc_open = "", jc_critical = "";
	public String jc_auth = "", jc_active = "", jc_notes = "", jc_entry_id = "0", jc_entry_date = "";

	// Vehicle fields
	public String veh_id = "0", veh_chassis_no = "", veh_engine_no = "", veh_sale_date = "", veh_variant_id = "0";

	public String city_name = "", state_name = "";
	// class variables
	public String modelName = "", fuelType = "", categoryName = "", serviceAdvisor = "";
	public String technician = "", carUserName = "", PreferedDayofFollowupTime = "", serviceType = "";
	public String model_name = "", model_id = "0";
	public String item_name = "", item_id = "0";
	public String jcbill_date = "", sale_date = "", bill_date = "", contact_name = "";
	public int count = 0, connCount = 0;
	public String recursion = "";
	public String vehid = "0";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			emp_role_id = CNumeric(session.getAttribute("emp_role_id") + "");
			comp_id = CNumeric(GetSession("comp_id", request));

			msg = ExecuteQuery("SELECT COUNT(Srl) FROM " + compdb(comp_id) + "jobcard_imp WHERE Imp_Status != '0'");

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

}
