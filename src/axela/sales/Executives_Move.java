package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executives_Move extends Connect {
	public String BranchAccess = "", ExeAccess = "";
	public String emp_id = "0";
	public String oppr_exe1_from_id = "";
	public String oppr_exe1_module_id = "";
	public String oppr_exe1_to_id = "";
	public String oppr_exe2_to_id = "";
	public String oppr_exe3_to_id = "";
	public String oppr_exe4_to_id = "";
	public String oppr_exe5_to_id = "";
	public String oppr_exe6_to_id = "";
	public String oppr_exe7_to_id = "";
	public String oppr_exe8_to_id = "";
	public String oppr_exe9_to_id = "";
	public String oppr_exe10_to_id = "";
	public String toemps = "";
	public String StrSql = "", msg = "";
	public String moveB = "";
	public String StrHTML = "";
	public String dr_oppr_exe_from_id = "";
	public String value = "";
	public String value1 = "";
	public String comp_id = "0";
	public String module = "";
	Enquiry_Check chk = new Enquiry_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_role_id", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			moveB = PadQuotes(request.getParameter("movebutton"));
			module = PadQuotes(request.getParameter("module"));
			oppr_exe1_module_id = PadQuotes(request.getParameter("dr_oppr_exe1_module_id"));
			// SOP("module===" + module);
			// SOP("oppr_exe1_module_id===" + oppr_exe1_module_id);

			try {
				GetValues(request, response);
				if (moveB.equals("Move")) {
					MoveUpdate();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		oppr_exe1_from_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe1_from_id")));
		oppr_exe1_module_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe1_module_id")));
		// SOP("oppr_exe1_module_id=====" + oppr_exe1_module_id);
		oppr_exe1_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe1_to_id")));
		oppr_exe2_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe2_to_id")));
		oppr_exe3_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe3_to_id")));
		oppr_exe4_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe4_to_id")));
		oppr_exe5_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe5_to_id")));
		oppr_exe6_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe6_to_id")));
		oppr_exe7_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe7_to_id")));
		oppr_exe8_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe8_to_id")));
		oppr_exe9_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe9_to_id")));
		oppr_exe10_to_id = CNumeric(PadQuotes(request.getParameter("dr_oppr_exe10_to_id")));
	}

	public String PopulateExecutive(String emp_id, String oppr_exe1_module_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_name, emp_ref_no, axela_emp.emp_id as emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1 ";
			if (oppr_exe1_module_id.equals("1")) {
				StrSql += "AND emp_sales = '1'";
			} else if (oppr_exe1_module_id.equals("2")) {
				StrSql += "AND emp_preowned = '1'";
			}
			// else if (oppr_exe1_module_id.equals("3")) {
			// StrSql += "AND emp_insur = '1'";
			// }
			else if (oppr_exe1_module_id.equals("4")) {
				StrSql += "AND emp_service = '1'";
			} else if (oppr_exe1_module_id.equals("5")) {
				StrSql += "AND emp_crm = '1'";
			}
			StrSql += " AND emp_branch_id != 0"
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (")
						.append(crs.getString("emp_ref_no")).append(") </option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateMove() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", oppr_exe1_module_id)).append(">Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", oppr_exe1_module_id)).append(">Enquiry</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", oppr_exe1_module_id)).append(">Pre Owned</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", oppr_exe1_module_id)).append(">Service</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", oppr_exe1_module_id)).append(">Service CRM</option>\n");
		return Str.toString();
	}

	public void CheckForm() {
		msg = "";
		toemps = "";
		if (oppr_exe1_from_id.equals("0")) {
			msg += "<br>Select From Sales Consultant 1!";
		}
		if (oppr_exe1_module_id.equals("0")) {
			msg += "<br>Select Module !";
		}
		if (oppr_exe1_to_id.equals("0")) {
			msg += "<br>Select To Sales Consultant 1!";
		} else {
			toemps = oppr_exe1_to_id;
		}
		if (!oppr_exe2_to_id.equals("0")) {
			toemps += "," + oppr_exe2_to_id;
		}
		if (!oppr_exe3_to_id.equals("0")) {
			toemps += "," + oppr_exe3_to_id;
		}
		if (!oppr_exe4_to_id.equals("0")) {
			toemps += "," + oppr_exe4_to_id;
		}
		if (!oppr_exe5_to_id.equals("0")) {
			toemps += "," + oppr_exe5_to_id;
		}
		if (!oppr_exe6_to_id.equals("0")) {
			toemps += "," + oppr_exe6_to_id;
		}
		if (!oppr_exe7_to_id.equals("0")) {
			toemps += "," + oppr_exe7_to_id;
		}
		if (!oppr_exe8_to_id.equals("0")) {
			toemps += "," + oppr_exe8_to_id;
		}
		if (!oppr_exe9_to_id.equals("0")) {
			toemps += "," + oppr_exe9_to_id;
		}
		if (!oppr_exe10_to_id.equals("0")) {
			toemps += "," + oppr_exe10_to_id;
		}
		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void MoveUpdate() {
		CheckForm();
		if (msg.equals("")) {
			if (oppr_exe1_module_id.equals("1")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET enquiry_emp_id = (SELECT emp_id FROM "
						+ compdb(comp_id) + "axela_emp WHERE emp_id IN (" + toemps + ") ORDER BY RAND() LIMIT 1),"
						+ " enquiry_branch_id = (SELECT emp_branch_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = enquiry_emp_id),"
						+ " enquiry_team_id = (SELECT teamtrans_team_id FROM "
						+ compdb(comp_id) + " axela_sales_team_exe WHERE teamtrans_emp_id = enquiry_emp_id)"
						+ " WHERE enquiry_status_id = 1";

				// it should transfer all the sales enquiery (not based on type)

				// if (oppr_exe1_module_id.equals("1")) {
				// StrSql += " AND enquiry_enquirytype_id = 1";
				// } else if (oppr_exe1_module_id.equals("2")) {
				// StrSql += " AND enquiry_enquirytype_id = 2";
				// }

				StrSql += " AND enquiry_emp_id = " + oppr_exe1_from_id;
				// SOP(StrSql);
				updateQuery(StrSql);
				// // Update follow-ups also
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = followup_enquiry_id"
						+ " SET followup_emp_id = enquiry_emp_id"
						+ " WHERE followup_desc = ''";
				// SOP(StrSql);
				updateQuery(StrSql);
				// For Preownded
			} else if (oppr_exe1_module_id.equals("2")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
						+ " SET preowned_emp_id = (SELECT emp_id FROM "
						+ compdb(comp_id) + "axela_emp WHERE emp_id IN (" + toemps + ") ORDER BY RAND() LIMIT 1),"
						+ " preowned_branch_id = (SELECT emp_branch_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = preowned_emp_id),"
						+ " preowned_team_id = (SELECT preownedteamtrans_team_id FROM "
						+ compdb(comp_id) + "axela_preowned_team_exe WHERE preownedteamtrans_emp_id = preowned_emp_id)"
						+ " WHERE preowned_emp_id = " + oppr_exe1_from_id;
				// SOP(StrSql);
				updateQuery(StrSql);
				// // Update follow-ups also
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_enquiry_id = preownedfollowup_preowned_id"
						+ " SET preownedfollowup_emp_id = preowned_emp_id"
						+ " WHERE preownedfollowup_desc = ''";
				// SOP(StrSql);
				updateQuery(StrSql);
			}
			// else if (oppr_exe1_module_id.equals("3")) {
			// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
			// + " SET veh_insuremp_id = (SELECT emp_id FROM "
			// + compdb(comp_id) + "axela_emp where emp_id in (" + toemps + ") ORDER BY RAND() LIMIT 1),"
			// + " veh_branch_id = (SELECT emp_branch_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = veh_insuremp_id)"
			// + " WHERE veh_insuremp_id = " + oppr_exe1_from_id;
			// // SOP(StrSql);
			// updateQuery(StrSql);
			// // // Update follow-ups also
			// StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_followup"
			// + " INNER JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = insurfollowup_veh_id"
			// + " SET insurfollowup_emp_id = veh_insuremp_id"
			// + " WHERE insurfollowup_desc = ''";
			// // SOP(StrSql);
			// updateQuery(StrSql);
			// }
			else if (oppr_exe1_module_id.equals("4")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET veh_emp_id = (SELECT emp_id FROM "
						+ compdb(comp_id) + "axela_emp where emp_id in (" + toemps + ") ORDER BY RAND() LIMIT 1),"
						+ " veh_branch_id = (SELECT emp_branch_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = veh_emp_id)"
						+ " WHERE veh_emp_id = " + oppr_exe1_from_id;
				// SOP(StrSql);
				updateQuery(StrSql);
			} else if (oppr_exe1_module_id.equals("5")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET veh_crmemp_id = (SELECT emp_id FROM "
						+ compdb(comp_id) + "axela_emp where emp_id in (" + toemps + ") ORDER BY RAND() LIMIT 1),"
						+ " veh_branch_id = (SELECT emp_branch_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = veh_crmemp_id)"
						+ " WHERE veh_crmemp_id = " + oppr_exe1_from_id;
				// SOP(StrSql);
				updateQuery(StrSql);
				// // Update follow-ups also
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = vehfollowup_veh_id"
						+ " SET vehfollowup_emp_id = veh_crmemp_id"
						+ " WHERE vehfollowup_desc = ''";
				// SOP(StrSql);
				updateQuery(StrSql);
			}

			msg = "Sales Consultant Moved Successfully!";
		}
	}

}
