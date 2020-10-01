package axela.axelaauto_app;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.service.Booking_Enquiry;
import cloudify.connect.Connect;

public class App_Booking_Enquiry_Add extends Connect {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_enquiry_edit = "";
	public String booking_customer_id = "0";
	public String booking_veh_contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String enquiry_id = "0";
	public String booking_veh_customer_id = "0";
	public String booking_veh_item_id = "0";
	public String booking_veh_chassis_no = "";
	public String booking_veh_model_id = "0";
	public String booking_veh_model_name = "";
	public String booking_veh_engine_no = "";
	public String booking_veh_reg_no = "";
	public String booking_veh_id = "0";
	public String booking_date = "";
	public String booking_time = "";
	public String booking_type = "0";
	public String booking_driver = "";
	public String booking_other_address = "";
	public String booking_veh_branch_id = "0";
	public String booking_contact_id = "0";
	public String booking_veh_crm_id = "";
	public String booking_campaign_id = "0";
	public String booking_soe_id = "0";
	public String booking_sob_id = "0";
	public String booking_entry_id = "0";
	public String booking_entry_date = "";
	public String branch_city_id = "0";
	public String vehfollowup_id = "0";
	public String submitB = "";
	public String emp_id = "0";
	public String emp_role_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String emp_name = "";
	public String booking_refemp_id = "0";
	public String contact_name = "";
	public String contact_info = "";
	public String contact_mobile1 = "91-";
	public String contact_email1 = "";
	public String contact_pin = "", branch_pin = "";
	public String customer_name = "";
	public String customer_info = "";
	public String branch_email1 = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			emp_role_id = CNumeric(GetSession("emp_role_id", request));
			booking_time = ToShortDate(kknow());
			submitB = PadQuotes(request.getParameter("add_button"));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				booking_veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehbranch_id")));
				if (booking_veh_branch_id.equals("0")) {
					booking_veh_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (booking_veh_branch_id.equals("0")) {
						booking_veh_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 "
								+ " AND branch_branchtype_id IN (3) LIMIT 1");
					}
				}
				if (!booking_veh_branch_id.equals("0")) {
					branch_city_id = ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch where branch_id =" + booking_veh_branch_id);
				}
				if ("Add Enquiry".equals(submitB)) {
					GetValues(request, response);
					booking_entry_id = emp_id;
					booking_entry_date = ToLongDate(kknow());
					AddVehFields(response);
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (!booking_veh_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-booking-enquiry-list.jsp?msg=Booking Enquiry added successfully!"));
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		booking_contact_id = CNumeric(PadQuotes(request.getParameter("txt_contact_id")));
		// Customer Details
		if (booking_contact_id.equals("0")) {
			customer_name = PadQuotes(request.getParameter("txt_booking_customer_name"));
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
		}
		// Vechile Details
		booking_veh_model_id = CNumeric(PadQuotes(request.getParameter("dr_item_model_id")));
		booking_veh_item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
		booking_veh_reg_no = PadQuotes(request.getParameter("txt_booking_veh_reg_no"));
		booking_veh_chassis_no = PadQuotes(request.getParameter("txt_booking_veh_chassis_no"));
		booking_veh_engine_no = PadQuotes(request.getParameter("txt_booking_veh_engine_no"));
		booking_veh_crm_id = PadQuotes(request.getParameter("dr_booking_veh_executive"));
		booking_date = PadQuotes(request.getParameter("txt_booking_date"));
		booking_time = PadQuotes(request.getParameter("txt_booking_time"));
		booking_time = booking_date + " " + booking_time;
		SOP("booking_time====" + booking_time);
		booking_type = PadQuotes(request.getParameter("dr_booking_type"));
		booking_driver = PadQuotes(request.getParameter("dr_vehfollowup_pickupdriver_emp_id"));
		booking_other_address = PadQuotes(request.getParameter("txt_vehfollowup_pickuplocation"));
		booking_soe_id = CNumeric(PadQuotes(request.getParameter("dr_booking_soe_id")));
		booking_sob_id = CNumeric(PadQuotes(request.getParameter("dr_booking_sob_id")));
		// SOP("123" + booking_sob_id);
		booking_campaign_id = CNumeric(ExecuteQuery("SELECT campaign_id FROM " + compdb(comp_id) + "axela_sales_campaign"
				+ " WHERE campaign_name = 'others'"));

	}

	protected void AddVehFields(HttpServletResponse response) throws SQLException {
		if (msg.equals("")) {
			try {
				Booking_Enquiry bookingenq = new Booking_Enquiry();
				bookingenq.comp_id = comp_id;
				bookingenq.emp_id = emp_id;
				bookingenq.emp_role_id = emp_role_id;
				bookingenq.booking_entry_id = emp_id;
				bookingenq.booking_entry_date = ToLongDate(kknow());
				// Start Booking Fields
				bookingenq.booking_contact_id = booking_contact_id;
				// bookingenq.booking_veh_model_id = booking_veh_model_id;
				// bookingenq.booking_veh_item_id = booking_veh_item_id;
				bookingenq.booking_veh_reg_no = booking_veh_reg_no;
				// bookingenq.booking_veh_chassis_no = booking_veh_chassis_no;
				// bookingenq.booking_veh_engine_no = booking_veh_engine_no;
				bookingenq.booking_veh_crm_id = booking_veh_crm_id;
				bookingenq.booking_time = booking_time;
				bookingenq.booking_type = booking_type;
				bookingenq.booking_driver = booking_driver;
				bookingenq.booking_other_address = booking_other_address;
				bookingenq.booking_soe_id = booking_soe_id;
				bookingenq.booking_sob_id = booking_sob_id;
				bookingenq.booking_campaign_id = booking_campaign_id;
				bookingenq.booking_veh_branch_id = booking_veh_branch_id;
				bookingenq.branch_city_id = branch_city_id;
				bookingenq.booking_time = booking_time;
				// End Booking Fields
				// Start Customer and Contact Fields
				bookingenq.customer_name = customer_name;
				bookingenq.contact_title_id = contact_title_id;
				bookingenq.contact_fname = contact_fname;
				bookingenq.contact_lname = contact_lname;
				bookingenq.contact_mobile1 = contact_mobile1;
				bookingenq.contact_email1 = contact_email1;
				// End Customer and Contact Fields
				bookingenq.AddVehFields(response);
				msg = bookingenq.msg1;
				booking_veh_id = bookingenq.booking_veh_id;

			} catch (Exception ex) {
				SOPError("Axelaauto-App== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

			}
		}
	}

	public String PopulateServiceBookingType(String comp_id, String vehfollowup_bookingtype_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT bookingtype_id, bookingtype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_bookingtype"
					+ " WHERE 1=1"
					+ " GROUP BY bookingtype_id"
					+ " ORDER BY bookingtype_id ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bookingtype_id"));
				Str.append(StrSelectdrop(crs.getString("bookingtype_id"), vehfollowup_bookingtype_id));
				Str.append(">").append(crs.getString("bookingtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// For Service Pickup and check vehfollowup_pickupdriver_emp_id
	public String PopulateServicePickUp(String comp_id, String booking_driver) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1"
					+ " AND emp_active=1"
					+ " AND emp_pickup_driver=1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), booking_driver));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String comp_id, String booking_veh_branch_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_branchtype_id IN (3)"
					+ " AND branch_active = 1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			// SOP("StrSql == " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=")
						.append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), booking_veh_branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append("</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}

	public String PopulateTitle(String contact_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id) + "axela_title" + " WHERE 1 =  1";
			if (!CNumeric(contact_title_id).equals("0")) {
				StrSql += " AND title_id = " + contact_title_id + "";
			}
			StrSql += " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateModel(String booking_veh_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE branch_id =" + booking_veh_branch_id;
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_model_id\" id=\"dr_item_model_id\" class=\"form-control\" onchange=\"PopulateItem(this.value);\">\n");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), booking_veh_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String veh_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE item_model_id = " + veh_model_id + ""
					+ " ORDER BY item_name";
			// SOP("StrSql----PopulateItem----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), booking_veh_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMExecutive(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_crm = 1"
					+ " AND emp_active = 1";
			// weekly off
			StrSql = StrSql + " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql = StrSql + " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow()) + ""
					+ " AND leave_active = 1)";
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_booking_veh_executive\" id=\"dr_booking_veh_executive\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select Executive</option>");
			while (crs.next()) {
				SOP("booking_veh_crm_id" + booking_veh_crm_id);
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), booking_veh_crm_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " WHERE 1=1"
					+ " AND soe_active = 1"
					+ " AND empsoe_emp_id=" + emp_id + ""
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), booking_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + booking_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), booking_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
