package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Booking_Enquiry_Update extends Connect {

	//

	public String branch_brand_id = "0";
	public String add = "";
	public String addB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String SqlCount = "";
	public String StrHTML = "";
	public String contact_name = "";
	public String send_contact_email = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_email_formail = "";
	public String entryemp_email = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";

	// -----------------
	public String workshop_branch_id = "0";
	public String branch_name = "";
	public String enquiry_model_id = "0";
	public String model_name = "";
	public String enquiry_item_id = "0";
	public String enquiry_reg_no = "0";
	public String enquiry_engine_no = "0";
	public String enquiry_chasis_no = "0";
	public String contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_mobile = "";
	public String contact_email = "";
	public String enquiry_contact_id = "";
	public String current_milage = "";
	public String booking_time = "";
	public String crmp_emp_id = "0";

	public String bookingtype_id = "0",
			pickupdriver_emp_id = "0", pickup_location = "Jayamahal";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public Vehicle_Check vehicle = new Vehicle_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				// CheckPerm(comp_id, "emp_ticket_add", request, response);
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				SOP("addB==" + addB);
				workshop_branch_id = CNumeric(PadQuotes(request.getParameter("dr_workshop_branch_id")));
				if (workshop_branch_id.equals("0")) {
					workshop_branch_id = CNumeric(GetSession("emp_branch_id", request));
					if (workshop_branch_id.equals("0")) {
						workshop_branch_id = ExecuteQuery("SELECT branch_id "
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = 1 AND branch_branchtype_id IN (1, 2) LIMIT 1");
					}
				}
				if (!workshop_branch_id.equals("0")) {
					branch_name = ExecuteQuery("SELECT concat(branch_name, ' (', branch_code,')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + workshop_branch_id);
				}
				if (!"yes".equals(addB)) {

				}
				else {
					GetValues(request, response);
					CheckForm(comp_id);
					AddFields(comp_id);
					if (!msg.equals("")) {
						msg = "Error! " + msg;
					} else {
						// response.sendRedirect(response.encodeRedirectURL("enquiry-list.jsp?enquiry_id=" + enquiry_id + "&msg=Enquiry added successfully!"));
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_mobile = PadQuotes(request.getParameter("txt_contact_mobile"));
			contact_email = PadQuotes(request.getParameter("txt_contact_email"));
			workshop_branch_id = PadQuotes(request.getParameter("dr_workshop_branch_id"));
			enquiry_model_id = CNumeric(request.getParameter("dr_enquiry_model_id"));
			enquiry_item_id = CNumeric(request.getParameter("dr_enquiry_item_id"));
			enquiry_reg_no = PadQuotes(request.getParameter("txt_enquiry_reg_no"));
			enquiry_chasis_no = PadQuotes(request.getParameter("txt_enquiry_chassis_no"));
			current_milage = PadQuotes(request.getParameter("txt_milage_kms"));
			booking_time = PadQuotes(request.getParameter("txt_appt_time"));
			bookingtype_id = PadQuotes(request.getParameter("dr_bookingtype_id"));
			pickupdriver_emp_id = CNumeric(request.getParameter("dr_pickupdriver_emp_id"));
			pickup_location = PadQuotes(request.getParameter("txt_pickuplocation"));
			crmp_emp_id = CNumeric(request.getParameter("dr_booking_emp_id"));

			SOP("contact_title_id===" + contact_title_id);
			SOP("contact_fname===" + contact_fname);
			SOP("contact_lname===" + contact_lname);
			SOP("contact_mobile===" + contact_mobile);
			SOP("contact_email===" + contact_email);
			SOP("workshop_branch_id===" + workshop_branch_id);
			SOP("enquiry_model_id===" + enquiry_model_id);
			SOP("enquiry_item_id===" + enquiry_item_id);
			SOP("enquiry_reg_no===" + enquiry_reg_no);
			SOP("enquiry_chasis_no===" + enquiry_chasis_no);
			SOP("current_milage===" + current_milage);
			SOP("booking_time===" + booking_time);
			SOP("bookingtype_id===" + bookingtype_id);
			SOP("pickupdriver_emp_id===" + pickupdriver_emp_id);
			SOP("pickup_location===" + pickup_location);
			SOP("crmp_emp_id===" + crmp_emp_id);

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm(String comp_id) {
		msg = "";
		try {
			if (workshop_branch_id.equals("0")) {
				msg = "<br> Select Workshop!";
			}
			if (contact_title_id.equals("0")) {
				msg = msg + "<br>Select the Contact Title!";
			}
			if (contact_fname.equals("")) {
				msg = msg + "<br>Enter the Contact Person Name!";
			} else {
				contact_fname = toTitleCase(contact_fname);
			}
			if (!contact_lname.equals("")) {
				contact_lname = toTitleCase(contact_lname);
			}

			if (enquiry_model_id.equals("0")) {
				msg = msg + "<br>Select Model!";
			}
			if (enquiry_item_id.equals("0")) {
				msg = msg + "<br>Select Item!";
			}
			if (enquiry_reg_no.equals("")) {
				msg = msg + "<br>Enter Reg No!";
			}
			if (booking_time.equals("")) {
				msg = msg + "<br>Enter Booking Time!";
			}
			if (bookingtype_id.equals("0")) {
				msg = msg + "<br>Enter Booking Type!";
			}

			if (bookingtype_id.equals("2")) {
				if (pickupdriver_emp_id.equals("0")) {
					msg = msg + "<br>Select Pickup Driver!";
				}
				if (pickup_location.equals("")) {
					msg = msg + "<br>Enter Pickup Location!";
				}
			}
			if (crmp_emp_id.equals("0")) {
				msg = msg + "<br>Select CRM Executive!";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void AddFields(String comp_id) {
		StrSql = "INSERT INTO "
				+ " (" + " enquiry_no,"
				+ " enquiry_branch_id,"
				+ " enquiry_contact_id,"
				+ " enquiry_title,"
				+ " enquiry_desc,"
				+ " enquiry_date,"
				+ " enquiry_model_id,"
				+ " enquiry_item_id,"
				+ " enquiry_close_date,"
				+ ")"
				+ " VALUES (";

	}

	public String PopulateServiceWorkshopBranch(String comp_id, String workshop_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_branchtype_id IN (3)"
					+ " AND branch_active = 1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"), workshop_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			SOP("StrSql==" + StrSql);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

				}
			} else {
				// msg = "<br><br>No Ticket found!";
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateContactDetails() {
		StrSql = " select customer_name, customer_id, customer_branch_id, contact_id,"
				+ " concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
				+ " contact_email1, contact_email2, contact_mobile1, contact_mobile2"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id =contact_customer_id"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id =contact_title_id"
				+ " where contact_id=" + CNumeric(enquiry_contact_id) + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_contact_id = crs.getString("contact_id");
					// ticket_customer_id = crs.getString("customer_id");
					// customer_name = crs.getString("customer_name");
					// customer_branch_id = crs.getString("customer_branch_id");
					contact_email = crs.getString("contact_email1");
					contact_mobile = crs.getString("contact_mobile1");
					contact_name = crs.getString("contact_name");
				}
			}
			crs.close();
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateModel(String enquiry_model_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1=1"
					// + " AND model_brand_id = " + branch_brand_id
					+ " AND model_active = 1 "
					+ " AND model_sales = 1";
			// if (!CNumeric(enquiry_model_id).equals("0")) {
			// StrSql += " AND model_id = " + enquiry_model_id + "";
			// }
			StrSql += " ORDER BY model_name";
			// SOP("StrSql-----PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_model_id\" id=\"dr_enquiry_model_id\"class=\" form-control\" onChange=\"PopulateItem(this.value);\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			Str.append("<select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String enquiry_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1 AND item_type_id = 1 ";
			StrSql = StrSql + " and item_active = '1'";
			StrSql = StrSql + " and item_model_id = " + enquiry_model_id
					+ " order by item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\" form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExecutive(String booking_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Service Advisor</option>\n");

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1";
			if (add.equals("yes")) {
				StrSql += " AND emp_active = 1";
			}
			StrSql += " AND (emp_service = 1"
					+ " OR emp_service_psf = 1"
					+ " OR emp_crm = 1)"
					+ " AND (emp_branch_id = " + booking_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + booking_branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
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
			SOP("StrSQl====" + StrSql);
			// Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bookingtype_id"));
				Str.append(StrSelectdrop(crs.getString("bookingtype_id"), vehfollowup_bookingtype_id));
				Str.append(">").append(crs.getString("bookingtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
