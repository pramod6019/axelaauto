//Gurumurthy TS 28 JAN 2013
package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String q = "";
	public String rateclass_id = "0";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_phone1 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String jc_id = "0";
	public String jc_contact_id = "0";
	public String jc_model_id = "0";
	public String location = "";
	public String jclocation = "";
	public String StrSearch = "";
	public String jc_emp_id = "0", search = "";
	public String jc_branch_id = "", veh_search = "";
	public String bay_branch_id = "0";
	public String jc_location_id = "0";
	public String jctrans_billtype_id = "0";
	public String current_qty = "0";
	public String ExeAccess = "";
	public String branch_supervisor = "", branch_technician = "";
	public String exe_branch_id = "0", duefollowupemp_id = "0";
	public String insurfollowexecutive = "";
	public String jctechnician = "", jcadvisor = "", crmexecutive = "";
	public String multiple = "", executive = "", technician = "", advisor = "", jcpsfexecutive = "", pickup = "", bay = "";
	public String crmemp_id = "0", branch_id = "0";
	public String psfdays = "";
	public String ticket_category = "", ticket_dept_id = "0";
	public String status = "";
	public String manhours = "";
	public String[] technicianexe_ids, bay_ids;
	// GST = state or central
	public String gst_type = "", rateclass_type = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		// for psf branch to band
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			ExeAccess = GetSession("ExeAccess", request);
			jc_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			gst_type = PadQuotes(request.getParameter("gsttype"));
			// SOP("gst_type==" + gst_type);
			jc_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
			jc_contact_id = CNumeric(PadQuotes(request.getParameter("jobcard_contact_id")));
			contact_fname = PadQuotes(request.getParameter("contact_fname"));
			search = PadQuotes(request.getParameter("search"));
			branch_supervisor = PadQuotes(request.getParameter("branch_supervisor"));
			branch_technician = PadQuotes(request.getParameter("branch_technician"));
			contact_lname = PadQuotes(request.getParameter("contact_lname"));
			if (!contact_lname.equals("")) {
				contact_lname = " " + contact_lname;
			}
			jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
			manhours = PadQuotes(request.getParameter("manhours"));
			jc_model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
			jc_location_id = CNumeric(PadQuotes(request.getParameter("jc_location_id")));
			jctrans_billtype_id = CNumeric(PadQuotes(request.getParameter("jctrans_billtype_id")));
			rateclass_type = CNumeric(PadQuotes(request.getParameter("rateclass_type")));
			rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
			q = PadQuotes(request.getParameter("q"));
			q = q.replaceAll("nbsp", "&");
			contact_mobile1 = PadQuotes(request.getParameter("contact_mobile1"));
			contact_mobile2 = PadQuotes(request.getParameter("contact_mobile2"));
			contact_phone1 = PadQuotes(request.getParameter("contact_phone1"));
			contact_email1 = PadQuotes(request.getParameter("contact_email1"));
			contact_email2 = PadQuotes(request.getParameter("contact_email2"));
			exe_branch_id = CNumeric(PadQuotes(request.getParameter("exe_branch_id")));
			veh_search = PadQuotes(request.getParameter("veh_search"));
			location = PadQuotes(request.getParameter("location"));
			jclocation = PadQuotes(request.getParameter("jclocation"));
			bay_branch_id = CNumeric(PadQuotes(request.getParameter("bay_branch_id")));
			multiple = PadQuotes(request.getParameter("multiple"));
			executive = PadQuotes(request.getParameter("executive"));
			insurfollowexecutive = PadQuotes(request.getParameter("insurfollowexecutive"));
			technician = PadQuotes(request.getParameter("technician"));
			bay = PadQuotes(request.getParameter("bay"));
			advisor = PadQuotes(request.getParameter("advisor"));
			jcadvisor = PadQuotes(request.getParameter("jcadvisor"));
			jcpsfexecutive = PadQuotes(request.getParameter("jcpsfexecutive"));
			crmexecutive = PadQuotes(request.getParameter("crmexecutive"));
			duefollowupemp_id = CNumeric(PadQuotes(request.getParameter("dr_emp_id")));
			crmemp_id = CNumeric(PadQuotes(request.getParameter("crmemp_id")));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			pickup = PadQuotes(request.getParameter("pickup"));
			status = PadQuotes(request.getParameter("status"));
			psfdays = PadQuotes(request.getParameter("psfdays"));
			// calling from ticket add

			ticket_category = PadQuotes(request.getParameter("ticket_category"));
			ticket_dept_id = PadQuotes(request.getParameter("ticket_dept_id"));
			// SOP("ticket_dept_id===" + ticket_dept_id);
			if (!exe_branch_id.equals("0")) {
				jc_branch_id = exe_branch_id;
			}
			if (!contact_mobile1.equals("")) {
				StrHTML = SearchMobile1();
			} // ///////// Search Contacts for phone1
			if (!contact_mobile2.equals("")) {
				StrHTML = SearchMobile2();
			} else if (!contact_email1.equals("")) {
				StrHTML = SearchEmail1();
			} else if (!contact_email2.equals("")) {
				StrHTML = SearchEmail2();
			} else if (!contact_fname.equals("")) {
				StrHTML = SearchName();
			} else if (!jc_model_id.equals("0")) {
				StrHTML = PopulateItem();
			} else if (q.equals("") && !jc_branch_id.equals("0") && !location.equals("yes") && !executive.equals("yes") && !branch_supervisor.equals("yes") && !branch_technician.equals("yes")) {
				StrHTML = PopulateExecutive();
			} else if (!jc_branch_id.equals("0") && location.equals("yes")) {
				StrHTML = PopulateInventoryLocation();
			} else if (!q.equals("") && veh_search.equals("yes")) {
				SearchVehicle();
			}
			if (!bay_branch_id.equals("0")) {
				StrHTML = PopulateBay();
			}

			if (!q.equals("") && !rateclass_id.equals("0")) {
				branch_id = CNumeric(GetSession("branch_id", request));
				StrHTML = SearchItems();
			}
			if (executive.equals("yes")) {
				StrHTML = PopulateServiceExecutives();
			}
			if (crmexecutive.equals("yes")) {
				StrHTML = new Report_Vehicle_Service_Due_Followup().PopulateCRMExecutives(comp_id, branch_id);
				// SOP("StrHTML===" + StrHTML);
			}

			if (jcpsfexecutive.equals("yes")) {
				if (multiple.equals("yes")) {
					StrHTML = PopulateListPSFExecutives(jc_branch_id, null, ExeAccess);
				} else {
					StrHTML = PopulateDrPSFExecutives(jc_branch_id, "", ExeAccess);
				}

			}

			if (insurfollowexecutive.equals("yes")) {
				if (multiple.equals("yes")) {
					StrHTML = PopulateListInsuranceExecutives(null, ExeAccess, comp_id);
				} else {
					StrHTML = PopulateDrInsuranceExecutives("", comp_id, ExeAccess);
				}
			}
			if (psfdays.equals("yes")) {
				if (multiple.equals("yes")) {
					StrHTML = PopulateListPSFDays(jc_branch_id, null);
				} else {
					StrHTML = PopulateDrPSFDays(jc_branch_id, "", comp_id);
				}
			}

			if (technician.equals("yes")) {
				StrHTML = PopulateTechnicians(jc_branch_id, null, ExeAccess, comp_id);
			}

			if (bay.equals("yes")) {
				StrHTML = PopulateBay(jc_branch_id, null);
			}

			if (advisor.equals("yes")) {
				StrHTML = PopulateServiceAdvisors(jc_branch_id, null, ExeAccess, comp_id);
			}

			if (!jc_id.equals("0") && manhours.equals("yes") && !technician.equals("yes") && !advisor.equals("yes")) {
				StrHTML = ManHourDetails();
			}

			if (ticket_category.equals("ticket_add") && !ticket_dept_id.equals("0")) {
				// SOP("coming" + ticket_dept_id);
				StrHTML = new Ticket_Add().PopulateTicketCategory(ticket_dept_id, comp_id);
			} else if (ticket_category.equals("ticket_update") && !ticket_dept_id.equals("0")) {
				// SOP("coming" + ticket_dept_id);
				StrHTML = new Ticket_Update().PopulateTicketCategory(ticket_dept_id, comp_id);
			} else if (ticket_category.equals("ticket_dash") && !ticket_dept_id.equals("0")) {
				// SOP("coming" + ticket_dept_id);
				StrHTML = new Ticket_Dash().PopulateTicketCategory(ticket_dept_id, comp_id);
			} else if (ticket_category.equals("ticket_addmulti") && !ticket_dept_id.equals("0")) {
				// SOP("coming" + ticket_dept_id);
				StrHTML = new Report_Ticket_Status().PopulateTicketCategory(ticket_dept_id, comp_id);
			}

			if (!jc_contact_id.equals("0") && !jc_contact_id.equals("")) {
				StringBuilder Str = new StringBuilder();
				try {
					StrSql = "SELECT customer_address, customer_phone1, customer_mobile1, customer_branch_id,"
							+ " customer_city_id, city_name, state_name, customer_pin, customer_id,"
							+ " CONCAT(customer_name, ' (', customer_id, ')') customername, customer_id,"
							+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) contactname"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
							+ " WHERE contact_id = " + jc_contact_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);

					while (crs.next()) {
						Str.append("<input type=\"hidden\" name=\"customer\" id=\"customer\"");
						Str.append(" value=\"").append(crs.getString("customer_address"));
						Str.append("[&%]").append(crs.getString("city_name"));
						Str.append("[&%]").append(crs.getString("customer_pin"));
						Str.append("[&%]").append(crs.getString("state_name"));
						Str.append("[&%]").append(crs.getString("customer_id"));
						Str.append("[&%]").append(crs.getString("customername"));
						Str.append("[&%]").append(jc_contact_id);
						Str.append("[&%]").append(crs.getString("contactname"));
						Str.append("\">");
					}
					crs.close();
					StrHTML = Str.toString();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
			if (branch_supervisor.equals("yes")) {
				StrHTML = new JobCard_Update().PopulateExecutive(jc_branch_id, comp_id);
			}
			if (branch_technician.equals("yes")) {
				StrHTML = new JobCard_Update().PopulateTechnicianExecutive(jc_branch_id, comp_id);
			}
			if (jclocation.equals("yes")) {
				StrHTML = new JobCard_Update().PopulateInventoryLocation(jc_branch_id, comp_id);
			}

		}
	}
	// ///////// Search Contacts for Name
	public String SearchName() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name,"
				+ " contact_id, customer_id, customer_name"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " WHERE CONCAT(contact_fname, ' ', contact_lname) LIKE '%" + contact_fname + contact_lname + "%'"
				+ " ORDER BY contact_fname limit 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name")).append("");
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append(",'");
					Str.append(crs1.getString("contact_name")).append("',").append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	// ///////// Search Contacts for mobile
	public String SearchMobile1() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT contact_mobile1, contact_id, customer_id, customer_name,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " INNER join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " WHERE ((contact_mobile1 LIKE '%" + contact_mobile1 + "%') "
				+ " OR (REPLACE(contact_mobile1, '-', '') like '%" + contact_mobile1 + "%'))"
				+ " ORDER by contact_fname, contact_id DESC LIMIT 5";
		// SOP("Mobile=====" + StrSql);
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<b>Select Contact</b><br>");
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name")).append("");
					if (!crs1.getString("contact_mobile1").equals("")) {
						Str.append(", ").append(crs1.getString("contact_mobile1")).append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append(",'");
					Str.append(crs1.getString("contact_name")).append("',").append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}

	public String SearchMobile2() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT contact_mobile2, contact_id, customer_id, customer_name,"
				+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " WHERE ((contact_mobile2 LIKE '%" + contact_mobile2 + "%') "
				+ " OR (REPLACE(contact_mobile2, '-', '') LIKE '%" + contact_mobile2 + "%'))"
				+ " ORDER BY contact_fname, contact_id DESC"
				+ " LIMIT 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<b>Select Contact</b><br>");
				while (crs1.next()) {
					Str.append("").append(crs1.getString("contact_name")).append("");
					if (!crs1.getString("contact_mobile2").equals("")) {
						Str.append(", ").append(crs1.getString("contact_mobile2")).append("");
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append(",'");
					Str.append(crs1.getString("contact_name")).append("',").append(crs1.getString("customer_id")).append(",'");
					Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}
	// ///////// Search Contacts for phone1

	public String SearchPhone() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT contact_phone1, contact_id, customer_id, customer_name,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " WHERE ((contact_phone1 LIKE '%" + contact_phone1 + "%') "
				+ " OR (REPLACE(contact_phone1, '-', '') LIKE '%" + contact_phone1 + "%'))"
				+ " ORDER BY contact_fname, contact_id DESC"
				+ " LIMIT 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				Str.append("").append(crs1.getString("contact_name")).append("");
				if (!crs1.getString("contact_phone1").equals("")) {
					Str.append(", ").append(crs1.getString("contact_phone1")).append("");
				}
				Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append(",'");
				Str.append(crs1.getString("contact_name")).append("',").append(crs1.getString("customer_id")).append(",'");
				Str.append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}
	// ///////// Search Contacts for email

	public String SearchEmail1() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT contact_email1, contact_id, customer_id, customer_name,"
				+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " WHERE (contact_email1 LIKE '%" + contact_email1 + "%')"
				+ " ORDER BY contact_fname, contact_id DESC"
				+ " LIMIT 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<b>Select Contact</b><br>");
				while (crs1.next()) {
					Str.append(crs1.getString("contact_name"));
					if (!crs1.getString("contact_email1").equals("")) {
						Str.append(", ").append(crs1.getString("contact_email1"));
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append("," + "'").append(crs1.getString("contact_name"));
					Str.append("',").append(crs1.getString("customer_id")).append(",'").append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}

	public String SearchEmail2() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select contact_email2, contact_id, customer_id, customer_name,"
				+ " concat(title_desc,' ',contact_fname,' ', contact_lname) as contact_name"
				+ " from " + compdb(comp_id) + "axela_customer_contact"
				+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
				+ " where (contact_email2 like '%" + contact_email2 + "%') "
				+ " order by contact_fname, contact_id DESC LIMIT 5";
		try {
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<b>Select Contact</b><br>");
				while (crs1.next()) {
					Str.append(crs1.getString("contact_name"));
					if (!crs1.getString("contact_email2").equals("")) {
						Str.append(", ").append(crs1.getString("contact_email2"));
					}
					Str.append(" [<b><a href=\"javascript:SelectContact(").append(crs1.getString("contact_id")).append("," + "'").append(crs1.getString("contact_name"));
					Str.append("',").append(crs1.getString("customer_id")).append(",'").append(crs1.getString("customer_name")).append("');\">Select</a></b>]<br>");
				}
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";

		}
	}

	public void SearchVehicle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT veh_id, preownedmodel_name, variant_name, veh_iacs, variant_service_code, veh_chassis_no,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_fname,"
					+ " contact_id, veh_sale_date, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " veh_branch_id, veh_engine_no, veh_reg_no, customer_name, customer_id,"
					+ " veh_so_id, coalesce(branch_id, 0) AS branch_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE 1=1"
					// + " veh_reg_no LIKE '%" + q + "%'"
					+ " AND veh_reg_no LIKE '%" + q.replace(" ", "") + "%'"
					+ " OR veh_chassis_no LIKE '%" + q + "%'"
					+ " OR veh_engine_no LIKE '%" + q + "%'"
					+ " LIMIT 10";
			SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				// Str.append("<tr align=center>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">ID</th>\n");
				Str.append("<th data-toggle=\"true\">Item</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Model</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Chassis Number</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Engine No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sale Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">SO ID.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\" width=15%>Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("veh_id")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("variant_name"));
					if (!crs.getString("variant_service_code").equals("")) {
						Str.append(" (").append(crs.getString("variant_service_code")).append(")");
					}
					Str.append("</td>\n<td align=\"left\" valign=\"top\">").append(crs.getString("preownedmodel_name")).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\" nowrap>").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("veh_chassis_no")).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("veh_engine_no"));
					if (crs.getString("veh_iacs").equals("1")) {
						Str.append("<br><font color=\"red\"><b>IACS</b></font>");
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_fname")).append("</a>");
					Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToShortDate(crs.getString("veh_sale_date"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">");
					if (!crs.getString("veh_so_id").equals("0")) {
						Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("veh_so_id")).append("\">");
						Str.append(crs.getString("veh_so_id")).append("</a>");
					}
					Str.append("</td>\n<td valign=\"top\" align=\"center\">");
					if (!crs.getString("branch_id").equals("0")) {
						Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append(">");
						Str.append(crs.getString("branch_name")).append("</a>");
					}
					Str.append("</td>\n<td valign=top align=left nowrap>");
					Str.append("<a href=\"../service/jobcard-list.jsp?jc_veh_id=").append(crs.getString("veh_id")).append("\">List Job Card</a>");
					Str.append("<br/><a href=\"../service/jobcard-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("&branch_id=").append(crs.getString("veh_branch_id"))
							.append("\">Add Job Card</a>");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<b><font color=\"#ff0000\">No Vehicle Found!</font></b>");
			}
			crs.close();
			StrHTML = Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateItem() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_type_id = 1"
					+ " AND item_model_id = " + jc_model_id + ""
					+ " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_veh_item\" id=\"dr_veh_item\" class=\"selectbox\" onchange=\"SecurityCheck('dr_veh_variant_id',this,'hint_dr_veh_variant_id');\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SearchItems() {
		StringBuilder Str = new StringBuilder();
		try {
			String brand_id = "0";
			double tax_value1 = 0.00, tax_value2 = 0.00;
			double tax1_rate1 = 0.00, tax2_rate2 = 0.00;
			double tax1_customer_id1 = 0.00, tax2_customer_id2 = 0.00;
			// double tax3_rate3 = 0.00, tax_value3 = 0.00, tax3_customer_id3 = 0.00;
			double item_unit_price = 0.00, itemprice = 0.00, discount = 0.00, quantity = 0.00;
			String price_tax2_after_tax1 = "0";
			String price_tax3_after_tax2 = "0";
			if (q.length() > 3) {

				if (!branch_id.equals("0")) {
					brand_id = CNumeric(PadQuotes(ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + branch_id)));
				} else if (!jc_branch_id.equals("0")) {
					brand_id = CNumeric(PadQuotes(ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + jc_branch_id)));
				}

				StrSearch = " AND (item_id LIKE '%" + q + "%'"
						+ " OR item_name LIKE '%" + q + "%'"
						+ " OR item_code LIKE '%" + q + "%')";

				StrSql = "SELECT item_id, item_name, item_code,"
						+ " COALESCE(option_itemmaster_id, 0) AS item_type,"
						+ "	COALESCE ((SELECT CONCAT(price.price_amt, ',', price.price_disc, ',', price.price_variable)"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_price price"
						+ " WHERE price.price_item_id = item_id"
						+ " AND price.price_rateclass_id = " + rateclass_id + ""
						+ " AND price.price_effective_from <= '" + ToLongDate(kknow()) + "'"
						+ " AND price.price_active = '1'"
						+ " ORDER BY price.price_effective_from DESC"
						+ " LIMIT 1), '0,0,0' ) AS price," // This sub query is to get latest price along with disc and variable
						+ " item_type_id, item_nonstock, item_ticket_dept_id,"
						+ "  item_serial,";

				if (gst_type.equals("state")) {
					StrSql += " IF(tax1.customer_id > 0, tax1.customer_id, 0) AS tax_customer_id1,"
							+ " IF(tax1.customer_rate > 0,tax1.customer_rate, 0) AS tax_rate1,"
							+ " IF(tax2.customer_id > 0, tax2.customer_id, 0) AS tax_customer_id2,"
							+ " IF(tax2.customer_rate > 0,tax2.customer_rate, 0) AS tax_rate2,"
							+ " COALESCE(tax1.customer_name, '') AS tax1_name,"
							+ " COALESCE(tax2.customer_name, '') AS tax2_name,"
							+ " COALESCE(tax1.customer_id, 0) AS tax_id1,"
							+ " COALESCE(tax2.customer_id, 0) AS tax_id2,"
							+ " COALESCE(item_salestax2_aftertax1, 0) AS price_tax2_after_tax1,";

					// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id3,"
					// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate3,";
					//
					// StrSql += " COALESCE(tax4.customer_name, '') AS tax3_name,"
					// + " COALESCE(tax4.customer_id, 0) AS tax_id3,";
				}
				else if (gst_type.equals("central")) {
					StrSql += " IF(tax3.customer_id > 0, tax3.customer_id, 0) AS tax_customer_id1,"
							+ " IF(tax3.customer_rate > 0,tax3.customer_rate, 0) AS tax_rate1,"
							+ " COALESCE(tax3.customer_name, '') AS tax1_name,"
							+ " COALESCE(tax3.customer_id, 0) AS tax_id1,";
					// StrSql += " IF(tax4.customer_id > 0, tax4.customer_id, 0) AS tax_customer_id2,"
					// + " IF(tax4.customer_rate > 0,tax4.customer_rate, 0) AS tax_rate2,";

					// StrSql += " COALESCE(tax4.customer_name, '') AS tax2_name,"
					// + " COALESCE(tax4.customer_id, 0) AS tax_id2,";
					// StrSql += " '0' AS tax_customer_id3,"
					// + " '0' AS tax_rate3,";
					//
					StrSql += " '' AS tax2_name,"
							+ " 0 AS tax_customer_id2,"
							+ " 0 AS tax_rate2,"
							+ " 0 AS tax_id2,";

				}
				StrSql += " IF(item_nonstock = 0,"
						+ " COALESCE(stock_current_qty, 0), '_') AS stock_current_qty"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";

				if (gst_type.equals("state")) {
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax1 on tax1.customer_id = item_salestax1_ledger_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer tax2 on tax2.customer_id = item_salestax2_ledger_id";
				} else if (gst_type.equals("central")) {
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer tax3 on tax3.customer_id = item_salestax3_ledger_id";
				}

				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type ON type_id = item_type_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_option ON option_itemmaster_id = item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
						+ " AND stock_location_id = " + jc_location_id + ""
						+ " WHERE item_active = 1";
				if (!brand_id.equals("1")) {
					StrSql += "	AND model_brand_id = " + brand_id;
				}
				StrSql += " AND price_effective_from <= '" + ToLongDate(kknow()) + "'"
						// + " AND price_rateclass_id = " + rateclass_id + ""
						+ " AND price_active = 1"
						+ StrSearch + ""
						+ " GROUP BY item_id"
						+ " ORDER BY item_name"
						+ " LIMIT 6";

				SOP("StrSql==SearchItems==" + StrSql);

				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {

					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Code</th>\n");
					Str.append("<th>Name</th>\n");
					Str.append("<th>Price</th>\n");
					Str.append("<th>Discount</th>\n");
					// Str.append("<th>Tax</th>\n");
					Str.append("<th>Current Qty</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						tax1_rate1 = crs.getDouble("tax_rate1");
						tax2_rate2 = crs.getDouble("tax_rate2");
						// tax3_rate3 = crs.getDouble("tax_rate3");
						tax1_customer_id1 = crs.getDouble("tax_customer_id1");
						tax2_customer_id2 = crs.getDouble("tax_customer_id2");
						// tax3_customer_id3 = crs.getDouble("tax_customer_id3");
						// price_tax2_after_tax1 = crs.getString("price_tax2_after_tax1");
						discount = Double.parseDouble(crs.getString("price").split(",")[1]);
						price_tax2_after_tax1 = "0";
						price_tax3_after_tax2 = "0";
						quantity = 1;
						item_unit_price = Double.parseDouble(crs.getString("price").split(",")[0]);
						if (tax1_rate1 != 0.00) {
							tax_value1 = ((item_unit_price - (discount / quantity)) * 1 * (tax1_rate1 / 100));
						}

						if (tax2_rate2 != 0.00) {
							if ((price_tax2_after_tax1.equals("1")) && (tax_value1 != 0)) {
								tax_value2 = ((((item_unit_price - (discount / quantity)) * 1) + tax_value1) * (tax2_rate2 / 100));
							} else {
								tax_value2 = ((item_unit_price - (discount / quantity)) * 1 * (tax2_rate2 / 100));
							}
						}

						// if (tax3_rate3 != 0.00) {
						// if ((price_tax3_after_tax2.equals("1")) && (tax_value1 != 0) && (tax_value2 != 0)) {
						// tax_value3 = ((((item_unit_price - (discount / quantity)) * 1) + tax_value1 + tax_value2) * (tax3_rate3 / 100));
						// } else {
						// tax_value3 = ((item_unit_price - (discount / quantity)) * 1 * (tax3_rate3 / 100));
						// }
						// }
						Str.append("\n<tr valign=top onClick=\"ItemDetails(");
						Str.append(crs.getString("item_id")).append(",");
						Str.append(rateclass_id).append(",");
						Str.append(jctrans_billtype_id).append(",");
						Str.append(crs.getString("item_type_id")).append(",");
						Str.append(crs.getString("item_type")).append(",");
						Str.append(crs.getString("item_ticket_dept_id")).append(",");
						Str.append("'").append((crs.getString("item_name").replace("&#39;", "single_quote")).replace("'", "single_quote")).append("',");
						Str.append("1,");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[0])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[0])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[2])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[1])).append(",");
						Str.append(crs.getString("tax_customer_id1")).append(",");
						Str.append(crs.getString("tax_customer_id2")).append(",");
						// Str.append(crs.getString("tax_customer_id3")).append(",");
						Str.append(crs.getString("tax_rate1")).append(",");
						Str.append(crs.getString("tax_rate2")).append(",");
						// Str.append(crs.getString("tax_rate3")).append(",");
						Str.append("'").append(crs.getString("tax1_name")).append("',");
						// Str.append("<input name=\"txt_item_tax1\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax1\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value1)).append("\"/>\n");
						Str.append("'").append(crs.getString("tax2_name")).append("',");
						// Str.append("<input name=\"txt_item_tax2\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax2\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value2)).append("\"/>\n");
						// Str.append("'").append(crs.getString("tax3_name")).append("',");
						// Str.append("<input name=\"txt_item_tax3\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax3\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value3)).append("\"/>\n");

						Str.append("'").append(crs.getString("item_serial")).append("',");
						Str.append("'',");
						Str.append("0,'");
						Str.append(crs.getString("stock_current_qty")).append("',");
						Str.append("'add');\">\n");
						Str.append("<td valign=top align=left>").append(crs.getString("item_id")).append("</td>");
						Str.append("<td valign=top align=left><a href=\"javascript:ItemDetails(");
						Str.append(crs.getString("item_id")).append(",");
						Str.append(rateclass_id).append(",");
						Str.append(jctrans_billtype_id).append(",");
						Str.append(crs.getString("item_type_id")).append(",");
						Str.append(crs.getString("item_type")).append(",");
						Str.append(crs.getString("item_ticket_dept_id")).append(",");
						Str.append("'").append((crs.getString("item_name").replace("&#39;", "single_quote")).replace("'", "single_quote")).append("',");
						Str.append("1,");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[0])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[0])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[2])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[1])).append(",");
						Str.append(crs.getString("tax_customer_id1")).append(",");
						Str.append(crs.getString("tax_customer_id2")).append(",");
						// Str.append(crs.getString("tax_customer_id3")).append(",");
						Str.append(crs.getString("tax_rate1")).append(",");
						Str.append(crs.getString("tax_rate2")).append(",");
						// Str.append(crs.getString("tax_rate3")).append(",");
						Str.append("'").append(crs.getString("tax1_name")).append("',");
						// Str.append("<input name=\"txt_item_tax1\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax1\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value1)).append("\"/>\n");
						Str.append("'").append(crs.getString("tax2_name")).append("',");
						// Str.append("<input name=\"txt_item_tax2\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax2\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value2)).append("\"/>\n");
						// Str.append("'").append(crs.getString("tax3_name")).append("',");
						// Str.append("<input name=\"txt_item_tax3\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax3\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value3)).append("\"/>\n");
						Str.append("'").append(crs.getString("item_serial")).append("',");
						Str.append("'',");
						Str.append("0,'");
						Str.append(crs.getString("stock_current_qty")).append("',");
						Str.append("'add');\">").append(crs.getString("item_code")).append("</a></td>");
						Str.append("<td valign=top align=left><a href=\"javascript:ItemDetails(");
						Str.append(crs.getString("item_id")).append(",");
						Str.append(rateclass_id).append(",");
						Str.append(jctrans_billtype_id).append(",");
						Str.append(crs.getString("item_type_id")).append(",");
						Str.append(crs.getString("item_type")).append(",");
						Str.append(crs.getString("item_ticket_dept_id")).append(",");
						Str.append("'").append((crs.getString("item_name").replace("&#39;", "single_quote")).replace("'", "single_quote")).append("',");
						Str.append("1,");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[0])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[0])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[2])).append(",");
						Str.append(Double.parseDouble(crs.getString("price").split(",")[1])).append(",");
						Str.append(crs.getString("tax_customer_id1")).append(",");
						Str.append(crs.getString("tax_customer_id2")).append(",");
						// Str.append(crs.getString("tax_customer_id3")).append(",");
						Str.append(crs.getString("tax_rate1")).append(",");
						Str.append(crs.getString("tax_rate2")).append(",");
						// Str.append(crs.getString("tax_rate3")).append(",");
						Str.append("'").append(crs.getString("tax1_name")).append("',");
						// Str.append("<input name=\"txt_item_tax1\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax1\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value1)).append("\"/>\n");
						Str.append("'").append(crs.getString("tax2_name")).append("',");
						// Str.append("<input name=\"txt_item_tax2\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax2\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value2)).append("\"/>\n");
						// Str.append("'").append(crs.getString("tax3_name")).append("',");
						// Str.append("<input name=\"txt_item_tax3\" type=\"hidden\" class=\"form-element\" id=\"txt_item_tax3\" size=\"10\" maxlength=\"10\" disabled ").append("value=\"")
						// .append(df.format(tax_value3)).append("\"/>\n");
						Str.append("'").append(crs.getString("item_serial")).append("',");
						Str.append("'',");
						Str.append("0,'");
						Str.append(crs.getString("stock_current_qty")).append("',");
						Str.append("'add');\">").append(crs.getString("item_name")).append("</a></td>");
						Str.append("<td valign=top align=\"right\">").append(Double.parseDouble(crs.getString("price").split(",")[0])).append("</td>");
						Str.append("<td valign=top align=\"right\">").append(Double.parseDouble(crs.getString("price").split(",")[1])).append("</td>");
						// Str.append("<td valign=top align=\"right\">").append(crs.getString("tax_value")).append("</td>");
						Str.append("<td valign=top align=\"left\">").append(crs.getString("stock_current_qty")).append("</td>");
						Str.append("</tr>");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} else {
					Str.append("<b><font color=\"#ff0000\">No Items Found!</font></b>");
				}
				return Str.toString();
			} else {
				return "<center><font color='red'><b>Item Name should be more than 3 characters</b></font></center>";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_service = 1"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Service Advisor</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInventoryLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_name, location_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id + ""
					+ " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_location\" id=\"dr_location\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select Location</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public String PopulateBay() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT bay_id, bay_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
					+ " WHERE bay_active = 1"
					+ " AND bay_branch_id = " + bay_branch_id + ""
					+ " ORDER BY bay_name";
			// SOP("StrSql check = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_baytrans_bay_id\" id=\"dr_baytrans_bay_id\" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bay_id"));
				Str.append(">").append(crs.getString("bay_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateServiceExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1";
			if (pickup.equals("yes")) {
				StrSql += " AND emp_pickup_driver = 1";
			} else if (jcpsfexecutive.equals("yes")) {
				StrSql += " AND (emp_service_psf = 1"
						+ " OR emp_service_psf_iacs = 1"
						+ " OR emp_crm = 1)";
			} else if (insurfollowexecutive.equals("yes")) {
				StrSql += " AND emp_insur = 1";
			} else if (jctechnician.equals("yes")) {
				StrSql += " AND emp_technician = 1";
			} else {
				StrSql += " AND emp_service = 1";
			}
			StrSql += " AND (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + "))" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (multiple.equals("yes")) {
				Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=\"10\">\n");
			} else {
				Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"selectbox\">\n");
				Str.append("<option value=\"0\">Select</option>\n");
			}
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// SOP(Str.toString());
		return Str.toString();
	}

	// ///////////PSF EXECUTIVE////////
	public String PopulateDrPSFExecutives(String jc_branch_id, String exe_id, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND (emp_service_psf = 1"
					+ " OR emp_service = 1)"
					+ " AND (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + ")) " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql==dr=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateListPSFExecutives(String jc_branch_id, String[] exe_ids, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND (emp_service_psf = 1"
					// + " OR emp_service_psf_iacs = 1"
					+ " OR emp_service = 1)"
					+ " AND (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + ")) " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_exe\" id=\"dr_exe\" class=\"form-control\" multiple=\"multiple\" size=\"10\" style=\"width:250px\">\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	// /////////ADVISOR////////

	public String PopulateServiceAdvisors(String jc_branch_id, String[] advisorexe_ids, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_service = 1"
					+ " AND (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + ")) " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_advisor\" id=\"dr_advisor\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=\"10\">\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), advisorexe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	// /////////TECHNICIAN////////

	public String PopulateTechnicians(String jc_branch_id, String[] technicianexe_ids, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name";
			if (status.equals("yes")) {
				StrSql += " , COALESCE((SELECT baytrans_id FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
						+ " WHERE baytrans_start_time <= '" + ToLongDate(kknow()) + "'"
						+ " AND baytrans_end_time >= '" + ToLongDate(kknow()) + "'"
						+ " AND baytrans_emp_id = emp_id), 0) AS tech_status";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " and emp_technician = 1"
					+ " and (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + ")) " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_technician\" id=\"dr_technician\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=\"10\">\n");
			while (crs.next()) {
				if (!jc_branch_id.equals("0")) {
					Str.append("<option value=").append(crs.getString("emp_id"));
					Str.append(ArrSelectdrop(crs.getInt("emp_id"), technicianexe_ids));
					if (status.equals("yes")) {
						if (!crs.getString("tech_status").equals("0")) {
							Str.append(" style=color:red");
						} else {
							Str.append(" style=color:green");
						}
					}
					Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
				}
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// Bay
	public String PopulateBay(String jc_branch_id, String[] bay_ids) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT bay_id, bay_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
					+ " WHERE bay_active = 1"
					+ " AND bay_branch_id = " + jc_branch_id + ""
					+ " GROUP BY bay_id"
					+ " ORDER BY bay_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_bay\" id=\"dr_bay\" class=\"textbox\" multiple=\"multiple\" size=\"10\" style=\"width:130px\">\n");
			while (crs.next()) {
				if (!jc_branch_id.equals("0")) {
					Str.append("<option value=").append(crs.getString("bay_id"));
					Str.append(ArrSelectdrop(crs.getInt("bay_id"), bay_ids));
					Str.append(">").append(crs.getString("bay_name")).append("</option>\n");
				}
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	// /////////INSURANCE EXECUTIVE////////

	public String PopulateListInsuranceExecutives(String[] exe_ids, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=\"10\" >\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateDrInsuranceExecutives(String exe_id, String comp_id, String ExeAccess) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1" + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"dropdown form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateListPSFDays(String jc_branch_id, String[] jcpsfdays_ids) {
		StringBuilder Str = new StringBuilder();
		// SOP("1");
		try {
			StrSql = "SELECT psfdays_id, CONCAT(psfdays_daycount, psfdays_desc) AS psfdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id"
					+ " WHERE branch_id = " + jc_branch_id + ""
					+ " GROUP BY psfdays_id"
					+ " ORDER BY psfdays_daycount";
			// SOP("Strsql==2=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_psfdays\" id=\"dr_psfdays\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\" size=\"10\" >\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psfdays_id"));
				Str.append(ArrSelectdrop(crs.getInt("psfdays_id"), jcpsfdays_ids));
				Str.append(">").append(crs.getString("psfdays_desc")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDrPSFDays(String jc_branch_id, String psfdays_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		// SOP("2");
		try {
			// SOP("pafdaysid===" + psfdays_id);
			StrSql = "SELECT psfdays_id, CONCAT(psfdays_daycount, psfdays_desc) AS psfdays_desc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id"
					+ " WHERE branch_id = " + jc_branch_id + ""
					+ " GROUP BY psfdays_id"
					+ " ORDER BY psfdays_daycount";
			// SOP("StrSql===213123===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_psfdays\" id=\"dr_psfdays\" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psfdays_id"));
				Str.append(StrSelectdrop(crs.getString("psfdays_id"), psfdays_id));
				Str.append(">").append(crs.getString("psfdays_desc")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ManHourDetails() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			StrSql = "SELECT COALESCE(emp_name, '') AS emp_name,"
					+ " COALESCE(baytrans_start_time, '') AS baytrans_start_time,"
					+ " COALESCE(baytrans_end_time, '') AS baytrans_end_time,"
					+ " COALESCE(TIME_TO_SEC(TIMEDIFF(baytrans_end_time, baytrans_start_time)) / 60, '') AS tat"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = baytrans_emp_id"
					+ " WHERE emp_active = 1"
					+ " AND baytrans_jc_id = " + jc_id + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY baytrans_start_time";
			// SOP("StrSql = " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>Technician</th>\n");
				Str.append("<th>Start Time</th>\n");
				Str.append("<th>End Time</th>\n");
				Str.append("<th>Man Hours</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td valign=\"top\" align=\"left\">").append(crs.getString("emp_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("baytrans_start_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("baytrans_end_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(ConvertMintoDaysHrsMins(crs.getInt("tat"))).append("</td>\n");
					Str.append("</tr>\n");
				}

				if (count > 1) {
					StrSql = "SELECT ROUND(SUM(TIME_TO_SEC(TIMEDIFF(baytrans_end_time, baytrans_start_time)) / 60))"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
							+ " WHERE baytrans_jc_id = " + jc_id + "";
					Str.append("<tr>\n<td valign=\"top\" align=\"right\" colspan=\"3\">Total:</td>\n");
					Str.append("<td valign=\"top\" align=\"center\" nowrap>").append(ConvertMintoDaysHrsMins(Long.parseLong(CNumeric(ExecuteQuery(StrSql))))).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("No Man Hours found!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
