package axela.service;

// divya
// modified by sn 6, 7 may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.insurance.Insurance_Enquiry;
import cloudify.connect.Connect;

public class Booking_Enquiry_Check extends Connect {

	public String StrSql = "";

	public String StrSearch = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String booking_veh_crm_id = "0";
	public String booking_enquiry_branch_id = "0";
	public String insurance_veh_branch_id = "0";
	public String dr_insur_sob_id = "";
	public String insurance_sob_id = "0";
	public String update = "";
	public String model = "";
	public String campaign = "";
	public String insurcampaign = "";
	public String insurmodel = "";
	public String insurlist_model_item = "";
	// public String executive = "";
	// public String insurexecutive = "";
	public String item = "", list_model_item = "";
	public String dr_booking_sob_id = "";
	public String enquiry_sob_id = "0";
	public String branch_id = "0";
	public String brand_id = "0";
	public String item_model_id = "0";
	public String BranchAccess = "";
	public String veh_branch_id = "0";
	public String emp_id = "";
	public String booking_id = "0";
	public String vehfollowup_pickupdriver_emp_id = "0";

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			booking_veh_crm_id = CNumeric(PadQuotes(request.getParameter("booking_veh_crm_id")));
			booking_enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("booking_enquiry_branch_id")));
			// executive = PadQuotes(request.getParameter("executive"));
			// insurexecutive = PadQuotes(request.getParameter("insurexecutive"));
			item = PadQuotes(request.getParameter("item"));
			dr_booking_sob_id = PadQuotes(request.getParameter("dr_booking_sob_id"));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			item_model_id = CNumeric(PadQuotes(request.getParameter("item_model_id")));
			model = PadQuotes(request.getParameter("model"));
			campaign = PadQuotes(request.getParameter("campaign"));
			insurcampaign = PadQuotes(request.getParameter("insurcampaign"));
			insurmodel = PadQuotes(request.getParameter("insurmodel"));
			list_model_item = PadQuotes(request.getParameter("list_model_item"));
			insurlist_model_item = PadQuotes(request.getParameter("insurlist_model_item"));
			veh_branch_id = CNumeric(PadQuotes(request.getParameter("veh_branch_id")));
			emp_id = CNumeric(GetSession("emp_id", request));
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			booking_id = CNumeric(PadQuotes(request.getParameter("booking_id")));
			dr_insur_sob_id = PadQuotes(request.getParameter("dr_insur_sob_id"));
			update = PadQuotes(request.getParameter("update"));

			if (dr_booking_sob_id.equals("yes")) {
				String booking_soe_id = CNumeric(PadQuotes(request.getParameter("booking_soe_id")));
				StrHTML = new Booking_Enquiry().PopulateSOB(booking_soe_id, comp_id);
			}

			if (!booking_id.equals("0")) {
				StrHTML = PopulateServicePickUp(comp_id);
			}
			// if (model.equals("yes") && !veh_branch_id.equals("0")) {
			// StrHTML = new Booking_Enquiry().PopulateModel(veh_branch_id, comp_id);
			// }
			//
			// if (list_model_item.equals("yes")) {
			// StrHTML = new Booking_Enquiry().PopulateItem(item_model_id, comp_id);
			// }

			if (dr_insur_sob_id.equals("yes")) {
				String insur_soe_id = CNumeric(PadQuotes(request.getParameter("insur_soe_id")));
				StrHTML = PopulateInsurSob(insur_soe_id, comp_id);
			}

			if (dr_insur_sob_id.equals("insuranceenquiry")) {
				String insur_soe_id = CNumeric(PadQuotes(request.getParameter("insur_soe_id")));
				StrHTML = new Insurance_Enquiry().PopulateSOB(insur_soe_id, comp_id);
			}

			if (campaign.equals("yes") && !veh_branch_id.equals("0")) {
				StrHTML = new Booking_Enquiry().PopulateCampaign(veh_branch_id, comp_id);
			}

			// if (insurmodel.equals("yes") && !veh_branch_id.equals("0")) {
			// StrHTML = new Insurance_Enquiry().PopulateModel(veh_branch_id, comp_id);
			// }
			// if (insurlist_model_item.equals("yes")) {
			// StrHTML = new Insurance_Enquiry().PopulateItem(item_model_id, comp_id);
			// }
			if (insurcampaign.equals("yes") && !veh_branch_id.equals("0")) {
				StrHTML = new Insurance_Enquiry().PopulateCampaign(comp_id, veh_branch_id);
			}
		}
	}
	// String veh_variant_id = CNumeric(ExecuteQuery(" SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
	// + " FROM " + compdb(comp_id) + "axela_inventory_item"
	// + " WHERE item_model_id = " + item_model_id + ""
	// + " LIMIT 1"));
	public String PopulateSob(String enquiry_soe_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("enquiry_soe_id==" + enquiry_soe_id);
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + enquiry_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), enquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String PopulateServicePickUp(String comp_id) {
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
				Str.append(StrSelectdrop(crs.getString("emp_id"), vehfollowup_pickupdriver_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateInsurSob(String insur_soe_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + insur_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), insurance_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
