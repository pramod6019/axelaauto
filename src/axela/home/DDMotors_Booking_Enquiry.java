package axela.home;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.service.Booking_Enquiry;
import cloudify.connect.Connect;

public class DDMotors_Booking_Enquiry extends Connect {
	public String AppRun = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String booking_veh_branch_id = "0";
	public String customer_name = "";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String booking_veh_model_id = "0";
	public String booking_veh_variant_id = "0";
	public String booking_veh_reg_no = "0";
	public String booking_veh_crm_id = "";
	public String booking_type = "";
	public String followup_nextfollowup_time = "0";
	public String booking_soe_id = "0";
	public String booking_sob_id = "0";
	public String booking_campaign_id = "0";
	public String vehfollowup_voc = "";
	public String msg = "";
	public String submitb = "", urlpath = "";
	public String ddwebsite_check = "";
	public Booking_Enquiry enquiry = new Booking_Enquiry();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			if (AppRun().equals("1")) {
				comp_id = "1009";
			} else {
				comp_id = "1009";
			}
			SetSession("comp_id", comp_id, request);
			followup_nextfollowup_time = strToLongDate(ToShortDate(kknow()));
			// SOP("followup_nextfollowup_time===========" + followup_nextfollowup_time);
			submitb = PadQuotes(request.getParameter("addbutton"));
			GetValues(request);
			// SOP("submitb====" + submitb);
			if (submitb.equals("Add Enquiry")) {
				AddFields(request, response);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doPost(request, response);
	}

	public void GetValues(HttpServletRequest request) {
		try {
			booking_veh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			customer_name = PadQuotes(request.getParameter("txt_booking_customer_name"));
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
			booking_veh_model_id = CNumeric(PadQuotes(request.getParameter("dr_booking_veh_model_id")));
			ddwebsite_check = PadQuotes(request.getParameter("ddwebsite_check"));
			if (!booking_veh_model_id.equals("0")) {
				ddwebsiteVariant(booking_veh_model_id);
			}
			booking_veh_reg_no = PadQuotes(request.getParameter("txt_booking_veh_reg_no"));
			booking_veh_crm_id = PadQuotes(request.getParameter("dr_booking_veh_executive"));
			booking_type = PadQuotes(request.getParameter("dr_booking_type"));
			followup_nextfollowup_time = PadQuotes(request.getParameter("txt_nextfollowup_time"));
			booking_soe_id = CNumeric(PadQuotes(request.getParameter("dr_booking_soe_id")));
			booking_sob_id = CNumeric(PadQuotes(request.getParameter("dr_booking_sob_id")));
			booking_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_booking_campaign_id")));
			vehfollowup_voc = PadQuotes(request.getParameter("txt_vehfollowup_voc"));
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFields(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			enquiry.booking_veh_branch_id = booking_veh_branch_id;
			enquiry.emp_id = "1";
			enquiry.comp_id = comp_id;
			enquiry.ddwebsite_check = ddwebsite_check;
			enquiry.customer_name = customer_name;
			enquiry.contact_title_id = contact_title_id;
			enquiry.contact_fname = contact_fname;
			enquiry.contact_lname = contact_lname;
			enquiry.contact_mobile1 = contact_mobile1;
			enquiry.contact_email1 = contact_email1;
			enquiry.booking_veh_variant_id = booking_veh_variant_id;
			enquiry.booking_veh_reg_no = booking_veh_reg_no;
			enquiry.booking_veh_crm_id = "681"; // Jyothi
			enquiry.booking_type = booking_type;
			enquiry.followup_nextfollowup_time = followup_nextfollowup_time;
			enquiry.booking_soe_id = booking_soe_id;
			enquiry.booking_sob_id = booking_sob_id;
			enquiry.booking_campaign_id = booking_campaign_id;
			enquiry.vehfollowup_voc = vehfollowup_voc;
			enquiry.booking_entry_date = ToLongDate(kknow());
			enquiry.AddVehFields(response);
			msg = enquiry.msg;
			// SOP("msg=====" + msg);
			if (booking_veh_model_id.equals("0")) {
				msg += "<br>Select Model!";
			}
			if (!msg.equals("")) {
				msg = "Error!" + msg;
			} else if (msg.equals("")) {
				msg = "Success from Website";
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateTitle(String contact_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " WHERE 1 =  1";
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateBranches(String comp_id, String booking_veh_branch_id) {
		// //SOP("booking_veh_branch_id====" + booking_veh_branch_id);
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_branchtype_id IN (3)"
					+ " AND branch_active = 1"
					+ " AND branch_brand_id IN (2)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			// //SOP("StrSql == " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), booking_veh_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			// //SOP("StrSql == " + booking_veh_branch_id);
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public void ddwebsiteVariant(String booking_veh_model_id) {
		try {
			StrSql = "SELECT"
					+ " variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE 1 = 1"
					+ " AND variant_preownedmodel_id = " + booking_veh_model_id
					+ " ORDER BY variant_id"
					+ " LIMIT 1";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				booking_veh_variant_id = CNumeric(PadQuotes(crs.getString("variant_id")));
			}
			// SOP("booking_veh_variant_id=======" + booking_veh_variant_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModel(String enquiry_model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM axelaauto.axela_preowned_model"
					+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1"
					+ " AND carmanuf_id IN (2)"
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql---------PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
