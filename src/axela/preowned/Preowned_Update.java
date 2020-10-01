package axela.preowned;
//Saiman 11th Feb 2013

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Update extends Connect {

	public String update = "";
	public String deleteB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String preowned_id = "0";
	public String preowned_branch_id = "0";
	// public String preowned_lead_id = "0";
	public String preowned_customer_id = "0";
	public String link_customer_name = "0";
	public String link_contact_name = "0";
	public String preowned_contact_id = "0";
	public String customer_id = "0";
	public String contact_id = "0";
	public String preowned_title = "";
	public String preowned_desc = "";
	public String preowned_date = "";
	public String preowneddate = "";
	public String preowned_close_date = "";
	public String closedate = "";
	// public String preowned_value_syscal = "";
	// public String preowned_budget = "0";
	// public String preowned_avpresent = "";
	// public String preowned_manager_assist = "";
	public String preowned_emp_id = "0";
	public String preowned_sales_emp_id = "0";
	public String sales_team_id = "0";
	public String preowned_team_id = "0";
	// public String preowned_stage_id = "0";
	public String preowned_preownedstatus_id = "1";
	public String preowned_preownedstatus_desc = "";
	public String preowned_preownedstatus_date = "";
	public String statusdate = "";
	// public String preowned_priorityfollowup_id = "";
	public String preowned_prioritypreowned_id = "0";
	public String preowned_soe_id = "0";
	public String preowned_sob_id = "0";
	public String preowned_enquiry_id = "0";
	public String preowned_campaign_id = "0";
	public String preowned_refno = "";
	public String preowned_notes = "";
	public String preowned_entry_id = "0";
	public String preowned_entry_date = "";
	public String preowned_modified_id = "0";
	public String preowned_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_name = "";
	public String emp_email = "";
	public String emp_mobile = "";
	public String emp_role_id = "";
	public String contact_name = "";
	public String contact_info = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String config_preowned_campaign = "";
	public String config_preowned_soe = "";
	public String config_preowned_sob = "";
	public String config_preowned_refno = "";
	public String brandconfig_preowned_email_enable = "";
	public String brandconfig_preowned_email_sub = "";
	public String brandconfig_preowned_email_format = "";
	public String brandconfig_preowned_email_exe_sub = "";
	public String brandconfig_preowned_email_exe_format = "";
	public String brandconfig_preowned_sms_enable = "";
	public String brandconfig_preowned_sms_exe_format = "";
	public String brandconfig_preowned_sms_format = "";
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	// public String config_sales_lead_for_preowned = "";
	public String config_customers_dupnames = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String preowned_variant_id = "0";
	// public String preowned_preownedmodel_id = "0";
	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				// preowned_lead_id =
				// CNumeric(PadQuotes(request.getParameter("lead_id")));
				preowned_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_emp_id")));
				preowned_sales_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_sales_emp_id")));
				preowned_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				preowned_contact_id = PadQuotes(request.getParameter("cont_id"));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				preowned_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				preowned_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
				// SOP("preowned_sales_emp_id---------" +
				// preowned_sales_emp_id);

				if (!contact_id.equals("0") || !preowned_contact_id.equals("")) {
					PopulateContactDetails(response);
				}
				PopulateConfigDetails();

				if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !("Delete " + ReturnPreOwnedName(request)).equals(deleteB)) {
						PopulateFields(response);
						StrSql = "SELECT teamtrans_team_id FROM " + compdb(comp_id) + "axela_sales_team_exe "
								+ " WHERE teamtrans_emp_id =" + preowned_sales_emp_id;
						sales_team_id = ExecuteQuery(StrSql);
					} else if ("yes".equals(updateB) && !("Delete " + ReturnPreOwnedName(request)).equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_edit", request).equals("1")) {
							preowned_modified_id = emp_id;
							preowned_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = ReturnPreOwnedName(request) + " Updated Successfully!";
								response.sendRedirect(response.encodeRedirectURL("preowned-list.jsp?preowned_id=" + preowned_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (("Delete " + ReturnPreOwnedName(request)).equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("preowned-list.jsp?msg=" + ReturnPreOwnedName(request) + " Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// preowned_branch_id =
		// CNumeric(PadQuotes(request.getParameter("dr_branch")));
		preowned_title = PadQuotes(request.getParameter("txt_preowned_title"));
		preowned_desc = PadQuotes(request.getParameter("txt_preowned_desc"));
		preowneddate = PadQuotes(request.getParameter("txt_preowned_date"));
		preowned_date = ConvertShortDateToStr(preowneddate);
		closedate = PadQuotes(request.getParameter("txt_preowned_close_date"));
		preowned_close_date = ConvertShortDateToStr(closedate);

		// preowned_budget =
		// CNumeric(PadQuotes(request.getParameter("txt_preowned_budget")));
		// preowned_avpresent =
		// PadQuotes(request.getParameter("chk_preowned_avpresent"));
		// if (preowned_avpresent.equals("on")) {
		// preowned_avpresent = "1";
		// } else {
		// preowned_avpresent = "0";
		// }
		// preowned_manager_assist =
		// PadQuotes(request.getParameter("chk_preowned_manager_assist"));
		// if (preowned_manager_assist.equals("on")) {
		// preowned_manager_assist = "1";
		// } else {
		// preowned_manager_assist = "0";
		// }
		// preowned_preownedmodel_id =
		// CNumeric(PadQuotes(request.getParameter("dr_preowned_preownedmodel_id")));
		preowned_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));

		preowned_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_emp_id")));
		preowned_sales_emp_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_sales_emp_id")));
		preowned_preownedstatus_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_preownedstatus_id")));
		preowned_preownedstatus_desc = PadQuotes(request.getParameter("txt_preowned_preownedstatus_desc"));
		// preowned_priorityfollowup_id =
		// PadQuotes(request.getParameter("dr_priorityfollowup_id"));
		preowned_prioritypreowned_id = PadQuotes(request.getParameter("dr_preowned_prioritypreowned_id"));
		preowned_soe_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_soe_id")));
		preowned_sob_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_sob_id")));
		preowned_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_campaign_id")));
		preowned_refno = PadQuotes(request.getParameter("txt_preowned_refno"));
		preowned_notes = PadQuotes(request.getParameter("txt_preowned_notes"));
		sales_team_id = CNumeric(PadQuotes(request.getParameter("dr_sales_team")));
		preowned_team_id = CNumeric(PadQuotes(request.getParameter("dr_preowned_team")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (preowned_title.equals("")) {
			msg = msg + "<br>Enter Pre-Owned Title!";
		}
		if (!preowned_title.equals("")) {
			if (preowned_title.length() < 5) {
				msg = msg + "<br>Pre-Owned Title Should have Atleast five character!";
			}
		}
		if (preowneddate.equals("")) {
			msg = msg + "<br>Enter Date!";
		} else {
			if (isValidDateFormatShort(preowneddate)) {
				preowned_date = ConvertShortDateToStr(preowneddate);
			} else {
				msg = msg + "<br>Enter Valid Date!";
			}
			if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(preowneddate))) {
				msg = msg + " <br>Date can't be greater than Current Date!";
			}
		}
		if (closedate.equals("")) {
			msg = msg + "<br>Enter Closed Date!";
		} else {
			if (isValidDateFormatShort(closedate)) {
				preowned_close_date = ConvertShortDateToStr(closedate);
				// if (Long.parseLong(ConvertShortDateToStr(closedate)) <
				// Long.parseLong(ToShortDate(kknow()))) {
				// msg = msg +
				// " <br>Close Date cannot be less than Current Date!";
				// }
			} else {
				msg = msg + "<br>Enter valid Closed Date!";
			}
		}
		if (preowned_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (preowned_preownedmodel_id.equals("0")) {
		// msg = msg + "<br>Select Model!";
		// }
		// if (preowned_variant_id.equals("0")) {
		// msg = msg + "<br>Select Item!";
		// }
		// if (preowned_stage_id.equals("0")) {
		// msg = msg + "<br>Select Stage!";
		// }
		if (preowned_preownedstatus_id.equals("0")) {
			msg = msg + "<br>Select Status!";
		}
		if (preowned_preownedstatus_desc.equals("")) {
			msg = msg + "<br>Enter Status Comments!";
		}
		// if (preowned_priorityfollowup_id.equals("0")) {
		// msg = msg + "<br>Select Follow-up Priority!";
		// }
		if (preowned_prioritypreowned_id.equals("0")) {
			msg = msg + "<br>Select Priority!";
		}
		if (preowned_team_id.equals("0")) {
			msg = msg + "<br>Select Pre-Owned Team!";
		}
		if (preowned_emp_id.equals("0")) {
			msg = msg + "<br>Select Pre-Owned Consultant!";
		}
		if (config_preowned_soe.equals("1")) {
			if (preowned_soe_id.equals("0")) {
				msg = msg + "<br>Select Source of Enquiry!";
			}
		}
		if (config_preowned_sob.equals("1")) {
			if (preowned_sob_id.equals("0")) {
				msg = msg + "<br>Select Source of Business!";
			}
		}
		if (config_preowned_campaign.equals("1")) {
			if (preowned_campaign_id.equals("0")) {
				msg = msg + "<br>Select Campaign!";
			}
		}
		if (config_preowned_refno.equals("1")) {
			if (preowned_refno.equals("")) {
				msg = msg + "<br>Enter Pre-Owned Reference No.!";
			} else {
				if (preowned_refno.length() < 2) {
					msg = msg + "<br>Pre-Owned Reference No. Should be Atleast Two Digits!";
				}
				if (!preowned_branch_id.equals("0")) {
					StrSql = "SELECT preowned_refno"
							+ " FROM " + compdb(comp_id) + "axela_preowned"
							+ " WHERE preowned_branch_id = " + preowned_branch_id + ""
							+ " AND preowned_refno = '" + preowned_refno + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " and preowned_id!=" + preowned_id + "";
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Pre-Owned Reference No. found!";
					}
				}
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_preowned.*,preownedmodel_name, variant_name, customer_id, customer_name, contact_id, contact_title_id,"
					+ " contact_fname, contact_lname, title_desc, preowned_sales_emp_id,"
					+ " contact_mobile1, contact_phone1, contact_email1, contact_address, "
					+ " contact_city_id, contact_pin, branch_name, branch_code "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preowned ON preowned.emp_id = preowned_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp sales ON sales.emp_id = preowned_sales_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = preowned_preownedmodel_id"
					+ " LEFT JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " WHERE preowned_id = " + preowned_id + BranchAccess + ExeAccess + " "
					+ " GROUP BY preowned_id ";
			// SOP("StrSql==="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					preowned_id = crs.getString("preowned_id");
					preowned_branch_id = crs.getString("preowned_branch_id");
					preowned_enquiry_id = crs.getString("preowned_enquiry_id");
					preowned_customer_id = crs.getString("customer_id");
					preowned_contact_id = crs.getString("contact_id");
					// preowned_preownedmodel_id =
					// crs.getString("preowned_preownedmodel_id");
					preowned_variant_id = crs.getString("preowned_variant_id");
					preowned_title = crs.getString("preowned_title");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\" target=_blank>" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\" target=_blank>" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					preowned_desc = crs.getString("preowned_desc");
					preowned_date = crs.getString("preowned_date");
					preowneddate = strToShortDate(preowned_date);
					preowned_close_date = crs.getString("preowned_close_date");
					closedate = strToShortDate(preowned_close_date);
					// preowned_value_syscal =
					// crs.getString("preowned_value_syscal");
					// preowned_budget = crs.getString("preowned_budget");
					// preowned_avpresent = crs.getString("preowned_avpresent");
					// preowned_manager_assist =
					// crs.getString("preowned_manager_assist");
					preowned_emp_id = crs.getString("preowned_emp_id");
					preowned_team_id = crs.getString("preowned_team_id");
					preowned_sales_emp_id = crs.getString("preowned_sales_emp_id");
					preowned_preownedstatus_id = crs.getString("preowned_preownedstatus_id");
					preowned_preownedstatus_date = crs.getString("preowned_preownedstatus_date");
					statusdate = strToLongDate(preowned_preownedstatus_date);
					preowned_preownedstatus_desc = crs.getString("preowned_preownedstatus_desc");
					// preowned_priorityfollowup_id =
					// crs.getString("preowned_prioritypreownedfollowup_id");
					preowned_prioritypreowned_id = crs.getString("preowned_prioritypreowned_id");
					preowned_soe_id = crs.getString("preowned_soe_id");
					preowned_sob_id = crs.getString("preowned_sob_id");
					preowned_campaign_id = crs.getString("preowned_campaign_id");
					// SOP("preowned_campaign_id--"+preowned_campaign_id);
					preowned_refno = crs.getString("preowned_refno");
					preowned_notes = crs.getString("preowned_notes");

					preowned_entry_id = crs.getString("preowned_entry_id");
					entry_by = Exename(comp_id, crs.getInt("preowned_entry_id"));
					entry_date = strToLongDate(crs.getString("preowned_entry_date"));
					preowned_modified_id = crs.getString("preowned_modified_id");

					if (!preowned_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(preowned_modified_id));
						modified_date = strToLongDate(crs.getString("preowned_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre-Owned!"));

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				if (!preowned_variant_id.equals("0"))
				{
					// preowned_preownedmodel_id =
					// ExecuteQuery("SELECT variant_preownedmodel_id FROM axela_preowned_variant"
					// + " WHERE variant_id = " + preowned_variant_id);
					// SOP("preowned_preownedmodel_id-------UpdateFields---" +
					// preowned_preownedmodel_id);
				}
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
						+ " SET "
						+ "preowned_branch_id= " + preowned_branch_id + ", "
						// + "preowned_lead_id= " + preowned_lead_id + ", "
						+ "preowned_customer_id= " + preowned_customer_id + ", "
						+ "preowned_contact_id= " + preowned_contact_id + ", "
						+ "preowned_title= '" + preowned_title + "', "
						+ "preowned_desc= '" + preowned_desc + "', "
						// + "preowned_preownedmodel_id= " + // preowned_preownedmodel_id + ", "
						+ "preowned_variant_id= " + preowned_variant_id + ", "
						+ "preowned_date= '" + preowned_date + "', "
						+ "preowned_close_date= '" + preowned_close_date + "', ";
				// + "preowned_budget= " + preowned_budget + ", ";

				StrSql = StrSql + "preowned_emp_id= " + preowned_emp_id + ", "
						+ "preowned_team_id = " + preowned_team_id + ", "
						+ "preowned_sales_emp_id = " + preowned_sales_emp_id + ", "
						+ "preowned_preownedstatus_id= " + preowned_preownedstatus_id + ", "
						+ "preowned_preownedstatus_date= '" + preowned_preownedstatus_date + "', "
						+ "preowned_preownedstatus_desc= '" + preowned_preownedstatus_desc + "', "
						// + "preowned_prioritypreownedfollowup_id= " + // preowned_priorityfollowup_id + ", "
						+ "preowned_prioritypreowned_id= " + preowned_prioritypreowned_id + ", ";
				// StrSql = StrSql + "preowned_project_id= " + // preowned_project_id + ", ";
				if (config_preowned_soe.equals("1")) {
					StrSql = StrSql + "preowned_soe_id= " + preowned_soe_id + ", ";
				}
				if (config_preowned_sob.equals("1")) {
					StrSql = StrSql + "preowned_sob_id= " + preowned_sob_id + ", ";
				}
				if (config_preowned_campaign.equals("1")) {
					StrSql = StrSql + "preowned_campaign_id= " + preowned_campaign_id + ", ";
				}
				StrSql = StrSql + "preowned_refno= '" + preowned_refno + "', ";

				StrSql = StrSql + "preowned_notes= '" + preowned_notes + "', "
						+ "preowned_modified_id= " + preowned_modified_id + ", "
						+ "preowned_modified_date= '" + preowned_modified_date + "'"
						+ " where preowned_id = " + preowned_id + " ";
				// SOP(" StrSql--" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

		}
	}
	protected void DeleteFields() {
		try {

			// Association with Evaluation
			StrSql = "SELECT eval_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned_eval"
					+ " WHERE eval_preowned_id =" + preowned_id + " ";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Pre Owned is associated with Evaluation!";
			}

			// Association with Stock
			StrSql = "SELECT preownedstock_id"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " WHERE preownedstock_preowned_id =" + preowned_id + " ";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Pre-Owned is associated with Stock!";
			}
			if (msg.equals("")) {
				StrSql = "Select doc_value from " + compdb(comp_id) + "axela_preowned_docs where doc_preowned_id = " + preowned_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(PreownedDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_docs WHERE doc_preowned_id = " + preowned_id + "";
				updateQuery(StrSql);

				StrSql = "SELECT img_value FROM " + compdb(comp_id) + "axela_preowned_img WHERE img_preowned_id = " + preowned_id + "";
				String imgname = ExecuteQuery(StrSql);
				if (!imgname.equals("") && imgname != null) {
					File f = new File(PreownedImgPath(comp_id) + imgname);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_img WHERE img_preowned_id = " + preowned_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_followup WHERE preownedfollowup_preowned_id = " + preowned_id + "";
				// SOP("StrSql = " + StrSql);
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_crmfollowup WHERE precrmfollowup_preowned_id = " + preowned_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_history WHERE preownedhistory_preowned_id = " + preowned_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned WHERE preowned_id =" + preowned_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// public String PopulateStage() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=0>Select</option>");
	// try {
	// StrSql = "select stage_id, stage_name "
	// + " from " + compdb(comp_id) + "axela_preowned_stage "
	// + " order by stage_rank";
	// // SOP("----------------------" + StrSql);
	// CachedRowSet crs =processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("stage_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("stage_id"), preowned_stage_id));
	// Str.append(">").append(crs.getString("stage_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// }
	// return Str.toString();
	// }
	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT preownedstatus_id, preownedstatus_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_status "
					+ " WHERE 1 = 1 "
					+ " ORDER BY preownedstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCountry==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedstatus_id"), preowned_preownedstatus_id));
				Str.append(">").append(crs.getString("preownedstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedPriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT prioritypreowned_id, prioritypreowned_desc, prioritypreowned_duehrs "
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority "
					+ " WHERE 1 = 1 "
					+ " ORDER BY prioritypreowned_rank";
			// SOP("StrSql==PopulatePreownedPriority==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option  value=").append(crs.getString("prioritypreowned_id")).append("");
				Str.append(StrSelectdrop(crs.getString("prioritypreowned_id"), preowned_prioritypreowned_id));
				Str.append(">").append(crs.getString("prioritypreowned_desc"));
				if (crs.getDouble("prioritypreowned_duehrs") != 0) {
					Str.append(" (").append(ConvertHoursToMins(crs.getString("prioritypreowned_duehrs")));
				}
				Str.append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateSalesTeam(String sales_team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE 1 = 1 ";
			StrSql += " GROUP BY team_id "
					+ " ORDER BY team_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql==sales team====" + StrSql);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), sales_team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedTeam(String preowned_branch_id, String preowned_team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedteam_id, preownedteam_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_team"
					+ " WHERE preownedteam_branch_id = " + preowned_branch_id
					+ " GROUP BY preownedteam_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_preowned_team\" id=\"dr_preowned_team\" class=\"dropdown form-control\" onchange=\"PopulatePreExecutive();\">");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedteam_id"));
				Str.append(StrSelectdrop(crs.getString("preownedteam_id"), preowned_team_id));
				Str.append(">").append(crs.getString("preownedteam_name"))
						.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSalesExecutives(String preowned_sales_emp_id, String team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp ";
			if (!preowned_enquiry_id.equals("0")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_emp_id = emp_id";
			}
			StrSql += " WHERE 1 = 1 "
					+ " AND emp_sales = 1 ";
			if (!preowned_enquiry_id.equals("0")) {
				StrSql += " AND emp_branch_id = enquiry_branch_id"
						+ " AND enquiry_id = " + preowned_enquiry_id;
			}
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id in (SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " WHERE teamtrans_team_id =" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("StrSql------SalesExecutives---" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_preowned_sales_emp_id\" id=\"dr_preowned_sales_emp_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), preowned_sales_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedExecutives(String branch_id, String team_id, String preowned_emp_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		emp_id = CNumeric(GetSession("emp_id", request));
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_preowned = 1"
					+ " AND emp_active = 1"
					+ " AND emp_branch_id = " + branch_id;
			if (!team_id.equals("0")) {
				StrSql = StrSql + " AND emp_id in (SELECT preownedteamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_preowned_team_exe "
						+ " WHERE preownedteamtrans_team_id =" + team_id + ")";
			}
			// SOP("emp_id===" + emp_id);
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=1";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("PopulatepreTeamExecutives ==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_preowned_emp_id\" id=\"dr_preowned_emp_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), preowned_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</select>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT soe_id, soe_name "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " WHERE 1 = 1 "
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), preowned_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT sob_id, sob_name "
					+ " FROM " + compdb(comp_id) + "axela_sob "
					+ " WHERE 1 = 1 "
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), preowned_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCampaign(String preowned_date) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			if (!preowned_branch_id.equals("0")) {
				StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
						+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
						+ " WHERE  1 = 1"
						+ " AND camptrans_branch_id = " + preowned_branch_id
						+ " AND campaign_active = '1' "
						+ " AND SUBSTR(campaign_startdate, 1, 8) <= SUBSTR('" + preowned_date + "', 1, 8) "
						+ " AND SUBSTR(campaign_enddate, 1, 8) >= SUBSTR('" + preowned_date + "', 1, 8) "
						+ " GROUP BY campaign_id "
						+ " ORDER BY campaign_name ";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("campaign_id"));
					Str.append(StrSelectdrop(crs.getString("campaign_id"), preowned_campaign_id));
					Str.append(">").append(crs.getString("campaign_name")).append(" (");
					Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ").append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
				}
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_preowned_soe, config_preowned_sob, config_preowned_campaign, "
				+ " config_preowned_refno,"
				+ " config_admin_email, config_email_enable,"
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " COALESCE(brandconfig_preowned_email_enable, '') AS brandconfig_preowned_email_enable,"
				+ " COALESCE(brandconfig_preowned_email_format, '') AS brandconfig_preowned_email_format,"
				+ " COALESCE(brandconfig_preowned_email_sub, '') AS brandconfig_preowned_email_sub,"
				+ " COALESCE(brandconfig_preowned_email_exe_sub, '') AS brandconfig_preowned_email_exe_sub,"
				+ " COALESCE(brandconfig_preowned_email_exe_format, '') AS brandconfig_preowned_email_exe_format,"
				+ " COALESCE(brandconfig_preowned_sms_enable, '') AS brandconfig_preowned_sms_enable,"
				+ " COALESCE(brandconfig_preowned_sms_format, '') AS brandconfig_preowned_sms_format,"
				+ " COALESCE(brandconfig_preowned_sms_exe_format, '') AS brandconfig_preowned_sms_exe_format,"
				+ " COALESCE(IF(emp.emp_email1 != '', emp.emp_email1, emp.emp_email2), '') AS emp_email,"
				+ " COALESCE(emp.emp_name,'') AS emp_name,"
				+ " COALESCE(IF(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2), '') AS  emp_mobile, "
				+ " config_customer_dupnames "
				+ " FROM " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_emp admin "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + preowned_branch_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + preowned_emp_id + ""
				+ " WHERE 1 = 1"
				+ " AND admin.emp_id = " + emp_id;
		// SOP("StrSql------------is --------" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_preowned_refno = crs.getString("config_preowned_refno");
				config_preowned_campaign = crs.getString("config_preowned_campaign");
				config_preowned_soe = crs.getString("config_preowned_soe");
				config_preowned_sob = crs.getString("config_preowned_sob");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				// config_sales_lead_for_preowned =
				// crs.getString("config_sales_lead_for_preowned");
				config_customers_dupnames = crs.getString("config_customer_dupnames");
				brandconfig_preowned_email_enable = crs.getString("brandconfig_preowned_email_enable");
				brandconfig_preowned_email_format = crs.getString("brandconfig_preowned_email_format");
				brandconfig_preowned_email_sub = crs.getString("brandconfig_preowned_email_sub");
				brandconfig_preowned_email_exe_sub = crs.getString("brandconfig_preowned_email_exe_sub");
				brandconfig_preowned_email_exe_format = crs.getString("brandconfig_preowned_email_exe_format");
				brandconfig_preowned_sms_enable = crs.getString("brandconfig_preowned_sms_enable");
				brandconfig_preowned_sms_format = crs.getString("brandconfig_preowned_sms_format");
				brandconfig_preowned_sms_exe_format = crs.getString("brandconfig_preowned_sms_exe_format");
				emp_name = crs.getString("emp_name");
				emp_email = crs.getString("emp_email");
				emp_mobile = crs.getString("emp_mobile");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateContactDetails(HttpServletResponse response) {
		CachedRowSet crs1 = null;
		try {
			if (!contact_id.equals("0")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname, contact_lname,"
						+ " contact_email1, contact_mobile1, title_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
						+ " WHERE contact_id = " + contact_id + "";
				crs1 = processQuery(StrSql, 0);
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {
						preowned_customer_id = crs1.getString("customer_id");
						preowned_contact_id = crs1.getString("contact_id");
						contact_email1 = crs1.getString("contact_email1");
						contact_mobile1 = crs1.getString("contact_mobile1");
						link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs1.getString("customer_id") + "\" target=_blank>" + crs1.getString("customer_name") + "</a>";
						link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs1.getString("contact_id") + "\" target=_blank>" + crs1.getString("title_desc") + " "
								+ crs1.getString("contact_fname") + " " + crs1.getString("contact_lname") + "</a>";
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
				}
				crs1.close();
			} else if (!preowned_contact_id.equals("")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
						+ " contact_lname, contact_email1, contact_mobile1"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE contact_id = " + CNumeric(preowned_contact_id) + "";
				// SOP("v=="+StrSql);
				crs1 = processQuery(StrSql, 0);
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {
						preowned_customer_id = crs1.getString("customer_id");
						preowned_contact_id = crs1.getString("contact_id");
						contact_email1 = crs1.getString("contact_email1");
						contact_mobile1 = crs1.getString("contact_mobile1");
						link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs1.getString("customer_id") + "\" target=_blank>" + crs1.getString("customer_name") + "</a>";
						link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs1.getString("contact_id") + "\" target=_blank>" + crs1.getString("contact_fname") + " "
								+ crs1.getString("contact_lname") + "</a>";
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
				}
				crs1.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
