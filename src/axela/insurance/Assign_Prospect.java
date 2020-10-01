package axela.insurance;

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

public class Assign_Prospect extends Connect {
	public String BranchAccess = "", ExeAccess = "";
	public String emp_id = "0";
	public String StrSql = "", msg = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String assignB = "", reassignB = "";

	// assignment part variables
	public String assignstarttime = "", start_time = "";
	public String assignendtime = "", end_time = "", check_starttime, check_endtime;
	public double timedifference = 0;
	public String assigninsurfollowup_soe_id = "0", assigninsurfollowup_sob_id = "0";
	public String assignveh_branch_id;
	public String[] assignveh_branch_ids;
	public String assignveh_insurtype_id = "0";
	public String assigntransfer_prospect_count = "";
	public String assignveh_insfollowupby = "";
	public String assigninsurfollowup_cre_id = "0";
	public String assignveh_manufyear = "0";
	public String assignveh_principal_id = "", assignveh_model_id = "";
	public String assignveh_carmanuf_id = "", assignveh_preownedmodel_id = "";
	public String assignveh_principal_ids[], assignveh_model_ids[];
	public String assignveh_carmanuf_ids[], assignveh_preownedmodel_ids[];
	public int curryear = 0;
	// reassign part variables
	public String reassignstarttime = "";
	public String reassignendtime = "";
	public String reassignveh_branch_id;
	public String[] reassignveh_branch_ids;
	public String reassigntransfer_prospect_count = "";
	public String reassigndisposition_id = "0";
	public String reassignfrominsurfollowup_cre_id = "0";
	public String reassigntoinsurfollowup_cre_id = "0";
	public String reassignveh_principal_id = "", reassignveh_model_id;
	public String reassignveh_carmanuf_id = "", reassignveh_preownedmodel_id;
	public String reassignveh_principal_ids[], reassignveh_model_ids[];
	public String reassignveh_carmanuf_ids[], reassignveh_preownedmodel_ids[];
	// re assign part end
	public String datefilter = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public CRE_Check crecheck = new CRE_Check();
	public MIS_Check insurmis = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_prospect_assign", request, response);
		if (!comp_id.equals("0")) {
			try {
				emp_id = CNumeric(GetSession("emp_id", request));
				// BranchAccess = GetSession("BranchAccess", request);
				// ExeAccess = GetSession("ExeAccess", request);
				assignB = PadQuotes(request.getParameter("assignprospect"));
				reassignB = PadQuotes(request.getParameter("reassignprospect"));
				GetValues(request, response);
				if (assignstarttime.equals("")) {
					assignstarttime = strToLongDate(ToLongDate(kknow()));
				}
				if (assignendtime.equals("")) {
					assignendtime = strToLongDate(ToLongDate(AddHoursDate(kknow(), 0, 1, 0)));
				}
				if (reassignstarttime.equals("")) {
					reassignstarttime = strToLongDate(ToLongDate(kknow()));
				}
				if (reassignendtime.equals("")) {
					reassignendtime = strToLongDate(ToLongDate(AddHoursDate(kknow(), 0, 1, 0)));
				}
				if (!assigninsurfollowup_sob_id.equals("0")) {
					insurmis.insurfollowup_sob_id = assigninsurfollowup_sob_id;
				}
				if (assignB.equals("Assign")) {
					if (!assignveh_carmanuf_id.equals("")) {
						insurmis.carmanuf_id = assignveh_carmanuf_id;
						insurmis.carmanuf_ids = assignveh_carmanuf_ids;
					}
					if (!assignveh_preownedmodel_id.equals("")) {

						insurmis.preowned_model_id = assignveh_preownedmodel_id;
						insurmis.preowned_model_ids = assignveh_preownedmodel_ids;
					}
				}
				else if (reassignB.equals("Re Assign")) {
					if (!reassignveh_carmanuf_id.equals("")) {
						insurmis.carmanuf_id = reassignveh_carmanuf_id;
						insurmis.carmanuf_ids = reassignveh_carmanuf_ids;
					}
					if (!reassignveh_preownedmodel_id.equals("")) {
						// SOP("coming");
						insurmis.preowned_model_id = reassignveh_preownedmodel_id;
						insurmis.preowned_model_ids = reassignveh_preownedmodel_ids;
					}
				}

				if (assignB.equals("Assign")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						AssignProspect();
					}
				}
				else if (reassignB.equals("Re Assign")) {
					GetValues(request, response);
					CheckForm();
					SOP("msg=12=" + msg);
					if (msg.equals("")) {
						ReassignProspect();
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (assignB.equals("Assign")) {
			assignstarttime = PadQuotes(request.getParameter("txt_assignstarttime"));
			assignendtime = PadQuotes(request.getParameter("txt_assignendtime"));
			assigninsurfollowup_soe_id = CNumeric(PadQuotes(request.getParameter("dr_assigninsurfollowup_soe_id")));
			assigninsurfollowup_sob_id = CNumeric(PadQuotes(request.getParameter("dr_assigninsurfollowup_sob_id")));
			assigntransfer_prospect_count = CNumeric(PadQuotes(request.getParameter("txt_assigntransfer_prospect_count")));
			assignveh_insfollowupby = CNumeric(request.getParameter("dr_assignveh_insfollowupby"));
			assignveh_insurtype_id = CNumeric(request.getParameter("dr_assignveh_insurtype"));

			assignveh_branch_id = RetrunSelectArrVal(request, "dr_assignveh_branch_id");
			assignveh_branch_ids = request.getParameterValues("dr_assignveh_branch_id");
			SOP("assignveh_branch_ids==11===" + assignveh_branch_id);
			assignveh_principal_id = RetrunSelectArrVal(request, "dr_principal");
			assignveh_model_id = RetrunSelectArrVal(request, "dr_model");
			assignveh_principal_ids = request.getParameterValues("dr_principal");
			assignveh_model_ids = request.getParameterValues("dr_model");

			assignveh_carmanuf_id = RetrunSelectArrVal(request, "dr_carmanuf_id");
			assignveh_carmanuf_ids = request.getParameterValues("dr_carmanuf_id");
			assignveh_preownedmodel_id = RetrunSelectArrVal(request, "dr_preowned_model");
			assignveh_preownedmodel_ids = request.getParameterValues("dr_preowned_model");

			assignveh_manufyear = request.getParameter("manufyear");
			assigninsurfollowup_cre_id = RetrunSelectArrVal(request, "assigninsurcre");
		}
		else if (reassignB.equals("Re Assign")) {
			reassignstarttime = PadQuotes(request.getParameter("txt_reassignstarttime"));
			reassignendtime = PadQuotes(request.getParameter("txt_reassignendtime"));
			reassigntransfer_prospect_count = CNumeric(PadQuotes(request.getParameter("txt_reassigntransfer_prospect_count")));
			reassigndisposition_id = CNumeric(request.getParameter("dr_reassignveh_insurenquirydisposition_id"));

			reassignveh_branch_id = RetrunSelectArrVal(request, "dr_reassignveh_branch_id");
			reassignveh_branch_ids = request.getParameterValues("dr_reassignveh_branch_id");

			reassignveh_principal_id = RetrunSelectArrVal(request, "dr_principal_reassign");
			reassignveh_model_id = RetrunSelectArrVal(request, "dr_model_reassign");
			reassignveh_principal_ids = request.getParameterValues("dr_principal_reassign");
			reassignveh_model_ids = request.getParameterValues("dr_model_reassign");

			reassignveh_carmanuf_id = RetrunSelectArrVal(request, "dr_reassign_carmanuf_id");
			reassignveh_carmanuf_ids = request.getParameterValues("dr_reassign_carmanuf_id");
			reassignveh_preownedmodel_id = RetrunSelectArrVal(request, "dr_reassign_preowned_model");
			reassignveh_preownedmodel_ids = request.getParameterValues("dr_reassign_preowned_model");

			// SOP("reassignveh_carmanuf_id===" + reassignveh_carmanuf_id);
			// SOP("reassignveh_carmanuf_ids===" + reassignveh_carmanuf_ids);
			// SOP("reassignveh_preownedmodel_id===" + reassignveh_preownedmodel_id);
			// SOP("reassignveh_preownedmodel_ids===" + reassignveh_preownedmodel_ids);

			reassignfrominsurfollowup_cre_id = RetrunSelectArrVal(request, "reassignfrominsurcre");
			reassigntoinsurfollowup_cre_id = RetrunSelectArrVal(request, "reassigntoinsurcre");
		}
	}
	public void CheckForm() {
		msg = "";
		if (assignB.equals("Assign")) {
			if (assignstarttime.equals("")) {
				msg = msg + "<br>Select Start Time!";
			} else {
				if (isValidDateFormatLong(assignstarttime)) {
					start_time = ConvertLongDateToStr(assignstarttime);
				} else {
					msg = msg + "<br>Enter Valid Start Date!";
					start_time = "";
				}
				if (Long.parseLong(start_time) < Long.parseLong(ToLongDate(kknow()))) {
					msg = msg + "<br>Start Time Can't be less than Current Time!";
				}
			}

			if (assignendtime.equals("")) {
				msg = msg + "<br>Select End Date!";
			} else {
				if (isValidDateFormatLong(assignendtime)) {
					end_time = ConvertLongDateToStr(assignendtime);
					if (!start_time.equals("") && !end_time.equals("") && Long.parseLong(start_time) > Long.parseLong(end_time)) {
						msg = msg + "<br>Start Time should be less than End Time!";
					}
				} else {
					msg = msg + "<br>Enter Valid End Time!";
					end_time = "";
				}
			}
			if (Long.parseLong(end_time.substring(0, 8)) - Long.parseLong(start_time.substring(0, 8)) > 0) {
				msg += " <br> Prospects Can Be Assigned On same Day Only!";
			}

			if (assigntransfer_prospect_count.equals("0")) {
				msg += "<br> Prospect count Can't be Zero!";
			}
			if (assigntransfer_prospect_count.equals("")) {
				msg += "<br> Prospect count Can't be Empty!";
				assigntransfer_prospect_count = "0";
			}
			if (assignveh_insfollowupby.equals("0")) {
				msg += " <br>Select Insurance Follow-up By!";
			}
			if (assigninsurfollowup_cre_id.equals("")) {
				msg += " <br> Select One CRE!";
			}
			if (assignveh_insfollowupby.equals("1")) {
				datefilter = "insurenquiry_sale_date";
			}
			else if (assignveh_insfollowupby.equals("2")) {
				datefilter = " insurenquiry_renewal_date";
			}

			if (!assigntransfer_prospect_count.equals("") && !assigntransfer_prospect_count.equals("0") && msg.equals("")) {
				check_starttime = ConvertLongDateToStr(assignstarttime);
				check_endtime = ConvertLongDateToStr(assignendtime);
				timedifference = getMinutesBetween(check_starttime, check_endtime);
				timedifference = (int) timedifference / Integer.parseInt(assigntransfer_prospect_count);
				timedifference = (int) timedifference;
				//
				// SOP("timedifference==111==" + (int) timedifference);
				// SOP("transfer_prospect_count===" + transfer_prospect_count);
				// SOP("timedifference====" + timedifference);
			}

			if ((int) timedifference == 0) {
				timedifference = 1; // one minute diifference
			}
		}
		else if (reassignB.equals("Re Assign")) {
			if (reassignstarttime.equals("")) {
				msg = msg + "<br>Select From Follow-up Time!";
			} else {
				if (isValidDateFormatLong(reassignstarttime)) {
					start_time = ConvertLongDateToStr(reassignstarttime);
				} else {
					msg = msg + "<br>Enter From Follow-up Time!";
					start_time = "";
				}
			}

			if (assignendtime.equals("")) {
				msg = msg + "<br>Select To Follow-up Time!";
			} else {
				if (isValidDateFormatLong(reassignendtime)) {
					end_time = ConvertLongDateToStr(reassignendtime);
					if (!start_time.equals("") && !end_time.equals("") && Long.parseLong(start_time) > Long.parseLong(end_time)) {
						msg = msg + "<br>From Follow-up Time should be less than To Follow-up Time!";
					}
				} else {
					msg = msg + "<br>Enter Valid End Time!";
					end_time = "";
				}
			}

			if (reassignfrominsurfollowup_cre_id.equals("")) {
				msg += " <br> Select From CRE";
			}
			if (reassigntoinsurfollowup_cre_id.equals("")) {
				msg += " <br> Select To CRE";
			}
			if (!reassigntransfer_prospect_count.equals("") && !reassigntransfer_prospect_count.equals("0") && msg.equals("")) {
				check_starttime = ConvertLongDateToStr(assignstarttime);
				check_endtime = ConvertLongDateToStr(assignendtime);
				timedifference = getMinutesBetween(check_starttime, check_endtime);
				timedifference = (int) timedifference / Integer.parseInt(reassigntransfer_prospect_count);
				timedifference = (int) timedifference;
			}
			if ((int) timedifference == 0) {
				timedifference = 1; // one minute diifference
			}
		}

		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}

	}

	public void AssignProspect() throws SQLException {
		try {
			int count = 0;
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			StrSql = "SELECT COALESCE(emp_id,0) AS insurempid"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id IN (" + assigninsurfollowup_cre_id + ")"
					+ " ORDER BY RAND()";
			CachedRowSet crs1 = processQuery(StrSql);

			StrSql = " SELECT insurenquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " WHERE 1=1 ";
			if (!assignveh_insfollowupby.equals("3")) {
				StrSql += " AND DATE_FORMAT(DATE_SUB(CONCAT('" + ToLongDate(kknow()).substring(0, 4)
						+ "',SUBSTR(" + datefilter + ",5,14)),INTERVAL 60 DAY),'%Y%m%d%H%i%s') >= '" + start_time + "'"
						+ " AND DATE_FORMAT(DATE_SUB(CONCAT('" + ToLongDate(kknow()).substring(0, 4)
						+ "',SUBSTR(" + datefilter + ",5,14)),INTERVAL 60 DAY),'%Y%m%d%H%i%s') <= '" + end_time + "' ";
			}
			StrSql += " AND insurenquiry_id NOT IN(SELECT insurenquiryfollowup_insurenquiry_id "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup "
					+ " WHERE insurenquiryfollowup_desc = '')";

			if (!assignveh_branch_id.equals("")) {
				StrSql += " AND insurenquiry_branch_id IN ( " + assignveh_branch_id + ")";
			}

			if (!assignveh_carmanuf_id.equals("")) {
				StrSql += " AND preownedmodel_carmanuf_id IN ( " + assignveh_carmanuf_id + ")";
			}

			if (!assignveh_preownedmodel_id.equals("")) {
				StrSql += " AND variant_preownedmodel_id IN ( " + assignveh_preownedmodel_id + ")";
			}

			if (!assignveh_manufyear.equals("0")) {
				StrSql += " AND insurenquiry_modelyear = " + assignveh_manufyear;
			}
			StrSql += " GROUP BY insurenquiry_id"
					+ " LIMIT " + assigntransfer_prospect_count;
			// if (emp_id.equals("1")) {
			// // SOPInfo("StrSql=====" + StrSql);
			// }

			CachedRowSet crs = processQuery(StrSql);

			int interval = 0;
			if (crs.isBeforeFirst() && crs1.isBeforeFirst()) {
				while (crs.next() && crs1.next()) {
					count++;
					interval = interval + (int) timedifference;
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry_followup"
							+ " (insurenquiryfollowup_insurenquiry_id,"
							+ " insurenquiryfollowup_followuptype_id,"
							+ " insurenquiryfollowup_desc,"
							+ " insurenquiryfollowup_entry_time,"
							+ " insurenquiryfollowup_followup_time)"
							+ " SELECT "
							+ "'" + crs.getString("insurenquiry_id") + "',"
							+ " '1',"// insurenquiryfollowup_followuptype_id
							+ " '',"
							+ "'" + ToLongDate(kknow()) + "',"// insurenquiryfollowup_entry_time
							+ " DATE_FORMAT(DATE_ADD('" + start_time + "',INTERVAL " + interval + " MINUTE),'%Y%m%d%H%i%s')";

					// SOP("StrSql===22=" + StrSql);
					stmttx.addBatch(StrSql);

					// update veh_insur_emp_id field

					StrSql = " UPDATE " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " SET insurenquiry_emp_id = " + crs1.getString("insurempid")
							+ " WHERE insurenquiry_id = " + crs.getString("insurenquiry_id");
					SOP("StrSql===" + StrSql);
					stmttx.addBatch(StrSql);
					if (crs1.isLast()) {
						crs1.beforeFirst();
					}

				}
				stmttx.executeBatch();
				conntx.commit();
				msg = count + " Prospects Assigned Succesfully!";
			}
			else {
				msg = "<font color=\"red\">No Prospects Found!</font>";
			}
			crs.close();
			crs1.close();

		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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

	public void ReassignProspect() throws Exception {
		try {
			int count = 0;
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			StrSql = "SELECT COALESCE(emp_id,0) AS insurempid"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id IN (" + reassigntoinsurfollowup_cre_id + ")"
					+ " ORDER BY RAND()";
			CachedRowSet crs1 = processQuery(StrSql);

			StrSql = " SELECT insurenquiryfollowup_insurenquiry_id "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
					+ " WHERE 1=1 "
					+ " AND insurenquiryfollowup_desc = ''"
					+ " AND insurenquiryfollowup_followup_time >='" + start_time + "'"
					+ " AND insurenquiryfollowup_followup_time <='" + end_time + "'";

			// if (!reassigndisposition_id.equals("0")) {
			// StrSql += " AND insurenquiryfollowup_insurenquiry_id IN (SELECT sub.insurenquiryfollowup_insurenquiry_id "
			// + " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup sub"
			// + " WHERE sub.insurfollowup_disposition_id = " + reassigndisposition_id + ")";
			// }
			if (!reassignfrominsurfollowup_cre_id.equals("0")) {
				StrSql += " AND insurenquiry_emp_id IN( " + reassignfrominsurfollowup_cre_id + ")";
			}
			if (!reassignveh_carmanuf_id.equals("")) {
				StrSql += " AND preownedmodel_carmanuf_id IN ( " + reassignveh_carmanuf_id + ")";
			}

			if (!reassignveh_preownedmodel_id.equals("")) {
				StrSql += " AND variant_preownedmodel_id IN ( " + reassignveh_preownedmodel_id + ")";
			}
			if (!reassignveh_branch_id.equals("")) {
				StrSql += " AND insurenquiry_branch_id IN ( " + reassignveh_branch_id + ")";
			}

			StrSql += " GROUP BY insurenquiryfollowup_insurenquiry_id";
			if (!reassigntransfer_prospect_count.equals("") && !reassigntransfer_prospect_count.equals("0")) {
				StrSql += " LIMIT " + reassigntransfer_prospect_count;
			}

			SOP("StrSql===reassign=" + StrSql);

			CachedRowSet crs = processQuery(StrSql);

			if (crs.isBeforeFirst() && crs1.isBeforeFirst()) {
				while (crs.next() && crs1.next()) {
					count++;

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET insurenquiry_emp_id = " + crs1.getString("insurempid")
							+ " WHERE insurenquiry_id = " + crs.getString("insurenquiryfollowup_insurenquiry_id");

					SOP("StrSql===22=" + StrSql);

					if (crs1.isLast()) {
						crs1.beforeFirst();
					}
					stmttx.addBatch(StrSql);
				}
				stmttx.executeBatch();
				conntx.commit();
				msg = count + " Prospects Re-Assigned Succesfully!";
			}
			else {
				msg = "<font color=\"red\">No Prospects Found to Re-Assign!</font>";
			}

		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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

	public String PopulateProspectCountByBucket(String branch_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT "
					+ " COALESCE(SUM(IF (insurenquiry_emp_id = 0, 1, 0)),0) AS unassigned,"
					+ " COALESCE(SUM(IF (insurenquiry_emp_id != 0, 1, 0)),0) AS assigned,"
					+ " COALESCE(COUNT(insurenquiry_id),0) AS total"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
					+ " WHERE 1=1";
			if (!branch_id.equals("0") && !("null").equals(branch_id)) {
				StrSql += " AND insurenquiry_branch_id IN ( " + branch_id + ")";
			}
			SOP("StrSql==count populate==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("111");
			if (crs.isBeforeFirst()) {
				Str.append("<center><table class=\"table table-bordered table-hover table-responsive\" style=\"width: 300px\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>Type</th>\n");
				Str.append("<th>Prospect Count</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Str.append("<tr>\n");
					Str.append("<td align=right>");
					Str.append("Un-Assigned").append(":</td>");
					Str.append("<td align=right>");
					Str.append(IndFormat(crs.getString("unassigned"))).append("</td>");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=right>");
					Str.append("Assigned").append(":</td>");
					Str.append("<td align=right>");
					Str.append(IndFormat(crs.getString("assigned"))).append("</td>");
					Str.append("</tr>\n");
					Str.append("<tr>\n");
					Str.append("<td align=right>");
					Str.append("Total").append(":</td>");
					Str.append("<td align=right>");
					Str.append(IndFormat(crs.getString("total"))).append("</td>");
					Str.append("</tr>\n");
					//
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</center>\n");

			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
			// SOPinfo("StrSql===soe====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), assigninsurfollowup_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB(String insurfollowup_sob_id, String insurfollowup_soe_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + insurfollowup_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOPinfo("StrSql==sob==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=dr_assigninsurfollowup_sob_id id=dr_assigninsurfollowup_sob_id class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), insurfollowup_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			Str.append("<select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFollowupBy(String veh_insfollowupby, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"1\" " + StrSelectdrop(veh_insfollowupby, "1") + ">Sale Date</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(veh_insfollowupby, "2") + ">Renewal Date</option>");
			Str.append("<option value=\"3\" " + StrSelectdrop(veh_insfollowupby, "3") + ">Follow-up Time</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsuranceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " ORDER BY insurtype_name";
			// SOPinfo("StrSql==insurtype=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(StrSelectdrop(crs.getString("insurtype_id"), assignveh_insurtype_id));
				Str.append(">").append(crs.getString("insurtype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDisposition() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurenquirydisposition_id, insurenquirydisposition_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_disposition"
					+ " WHERE 1=1 ";
			StrSql += " GROUP BY insurenquirydisposition_id"
					+ " ORDER BY insurenquirydisposition_name";
			// SOPinfo("StrSql=dispo==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurenquirydisposition_id"));
				Str.append(StrSelectdrop(crs.getString("insurenquirydisposition_id"), reassigndisposition_id));
				Str.append(">").append(crs.getString("insurenquirydisposition_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExecutive(String emp_id, String oppr_exe1_module_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_name, emp_ref_no, axela_emp.emp_id as emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1 "
					+ " AND emp_active = 1"
					+ " AND (emp_crm =1 "
					+ " OR emp_insur = 1)";
			StrSql += " AND emp_branch_id != 0"
					// + ExeAccess
					// + BranchAccess.replace("branch_id", "emp_branch_id")
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// SOPinfo("Strsql==exe=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
		try {
			Str.append("<option value=0>Select</option>");
			for (int i = curryear; i >= curryear - 15; i--) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), assignveh_manufyear));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateBranches(String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id = 6"
					+ BranchAccess;
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_name ";

			// SOP("StrSql------PopulateBranches-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
				}
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
