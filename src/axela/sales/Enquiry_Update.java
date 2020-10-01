package axela.sales;
//Saiman 11th Feb 2013
//divya

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Enquiry_Update extends Connect {

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
	public String enquiry_id = "0";
	public String enquiry_branch_id = "0";
	public String enquiry_customer_id = "0";
	public String link_customer_name = "0";
	public String link_contact_name = "0";
	public String enquiry_contact_id = "0";
	public String customer_id = "0";
	public String contact_id = "0";
	public String enquiry_title = "";
	public String enquiry_desc = "";
	public String enquiry_date = "";
	public String enquirydate = "";
	public String enquiry_close_date = "";
	public String closedate = "";
	public String enquiry_value_syscal = "";
	public String enquiry_value = "0";
	public String enquiry_avpresent = "";
	public String enquiry_manager_assist = "";
	public String enquiry_emp_id = "0";
	public String enquiry_refemp_id = "0";
	public String team_id = "0";
	public String enquiry_stage_id = "0";
	public String enquiry_status_id = "1";
	public String enquiry_status_desc = "";
	public String enquiry_status_date = "";
	public String statusdate = "";
	public String enquiry_priorityenquiry_id = "0";
	public String priorityenquiry_desc = "";
	public String priorityenquiry_duehrs = "0";
	public String enquiry_soe_id = "0";
	public String enquiry_sob_id = "0";
	public String enquiry_campaign_id = "0";
	public String enquiry_qcsno = "";
	public String enquiry_notes = "";
	public String enquiry_entry_id = "0";
	public String enquiry_enquirytype_id = "0";
	public String enquiry_entry_date = "";
	public String enquiry_modified_id = "0";
	public String enquiry_modified_date = "";
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
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String config_sales_sob = "";
	public String config_sales_enquiry_refno = "";
	public String enquiry_model_id = "0";
	public String enquiry_item_id = "0";
	public String preowned_variant_id = "0";
	public String branch_brand_id = "0";
	public Preowned_Variant_Check VariantCheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				enquiry_enquirytype_id = CNumeric(PadQuotes(request.getParameter("enquiry_enquirytype_id")));
				// SOP("enquiry_enquirytype_id----getvalues-------" +
				// enquiry_enquirytype_id);
				branch_brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				enquiry_emp_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_emp_id")));
				enquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				enquiry_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				enquiry_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));

				preowned_variant_id = PadQuotes(request.getParameter("preownedvariant"));

				if (!contact_id.equals("0")) {
					enquiry_contact_id = contact_id;
				}
				// SOP("enquiry_contact_id-=-" + enquiry_contact_id +
				// "--contact_id--" + contact_id);
				if (!enquiry_contact_id.equals("0")) {
					PopulateContactDetails(response);
				}
				PopulateConfigDetails();

				if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(update)) {
					if (!("yes").equals(updateB) && !"Delete Enquiry".equals(deleteB)) {
						PopulateFields(response);
					} else if (("yes").equals(updateB) && !"Delete Enquiry".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_enquiry_edit", request).equals("1")) {
							enquiry_modified_id = emp_id;
							enquiry_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Enquiry updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("enquiry-list.jsp?enquiry_id=" + enquiry_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Enquiry".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_enquiry_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Enquiry deleted successfully!";
								response.sendRedirect(response.encodeRedirectURL("enquiry-list.jsp?msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
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
		preowned_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		SOP("preowned_variant_id==GetValues==" + preowned_variant_id);
		enquiry_title = PadQuotes(request.getParameter("txt_enquiry_title"));
		enquiry_desc = PadQuotes(request.getParameter("txt_enquiry_desc"));
		enquirydate = PadQuotes(request.getParameter("txt_enquiry_date"));
		enquiry_date = ConvertShortDateToStr(enquirydate);
		closedate = PadQuotes(request.getParameter("txt_enquiry_close_date"));
		enquiry_close_date = ConvertShortDateToStr(closedate);
		enquiry_value = CNumeric(PadQuotes(request.getParameter("txt_enquiry_value")));
		enquiry_avpresent = PadQuotes(request.getParameter("chk_enquiry_avpresent"));
		if (enquiry_avpresent.equals("on")) {
			enquiry_avpresent = "1";
		} else {
			enquiry_avpresent = "0";
		}
		enquiry_manager_assist = PadQuotes(request.getParameter("chk_enquiry_manager_assist"));
		if (enquiry_manager_assist.equals("on")) {
			enquiry_manager_assist = "1";
		} else {
			enquiry_manager_assist = "0";
		}
		enquiry_model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
		enquiry_item_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_item_id")));
		enquiry_emp_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_emp_id")));
		enquiry_stage_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_stage_id")));
		enquiry_status_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_status_id")));
		enquiry_status_desc = PadQuotes(request.getParameter("txt_enquiry_status_desc"));
		enquiry_priorityenquiry_id = PadQuotes(request.getParameter("dr_priorityenquiry_id"));
		enquiry_refemp_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_refemp_id")));
		enquiry_soe_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_soe_id")));
		enquiry_sob_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_sob_id")));
		enquiry_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_campaign_id")));
		// SOP("enquiry_campaign_id=gg=="+enquiry_campaign_id);
		enquiry_qcsno = PadQuotes(request.getParameter("txt_enquiry_qcsno"));
		enquiry_notes = PadQuotes(request.getParameter("txt_enquiry_notes"));
		team_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_team")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (enquiry_title.equals("")) {
			msg = msg + "<br>Enter Enquiry Title!";
		}
		if (!enquiry_title.equals("")) {
			if (enquiry_title.length() < 5) {
				msg = msg + "<br>Enquiry Title Should have Atleast five character!";
			}
		}
		// if (!link_customer_name.equals("")){
		// link_customer_name = toTitleCase(link_customer_name);
		// }
		if (enquirydate.equals("")) {
			msg = msg + "<br>Enter Date!";
		} else {
			if (isValidDateFormatShort(enquirydate)) {
				enquiry_date = ConvertShortDateToStr(enquirydate);
			} else {
				msg = msg + "<br>Enter Valid Date!";
			}
			if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(enquirydate))) {
				msg = msg + " <br>Date can't be greater than Current Date!";
			}
		}
		if (closedate.equals("")) {
			msg = msg + "<br>Enter Closed Date!";
		} else {
			// if (isValidDateFormatShort(closedate)) {
			// enquiry_close_date = ConvertShortDateToStr(closedate);
			// if (Long.parseLong(ConvertShortDateToStr(closedate)) <
			// Long.parseLong(ToShortDate(kknow()))) {
			// msg = msg + " <br>Close Date cannot be less than Current Date!";
			// }
			// } else {
			// msg = msg + "<br>Enter valid Closed Date!";
			// }
			if (!isValidDateFormatShort(closedate)) {
				msg = msg + "<br>Enter valid Closed Date!";
			}
		}
		if (enquiry_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (enquiry_model_id.equals("0")) {
		// msg = msg + "<br>Select Model!";
		// }
		// if (enquiry_item_id.equals("0")) {
		// msg = msg + "<br>Select Variant!";
		// }

		if (enquiry_stage_id.equals("0")) {
			msg = msg + "<br>Select Stage!";
		}
		if (enquiry_status_id.equals("0")) {
			msg = msg + "<br>Select Status!";
		}
		if (enquiry_status_desc.equals("")) {
			msg = msg + "<br>Enter Status Comments!";
		}
		if (team_id.equals("0")) {
			msg = msg + "<br>Select Team!";
		}
		if (enquiry_emp_id.equals("0")) {
			msg = msg + "<br>Select Sales Consultant!";
		}
		if (config_sales_soe.equals("1")) {
			if (enquiry_soe_id.equals("0")) {
				msg = msg + "<br>Select Source of Enquiry!";
			}
		}
		if (config_sales_sob.equals("1")) {
			if (enquiry_sob_id.equals("0")) {
				msg = msg + "<br>Select Source of Bussiness!";
			}
		}
		if (config_sales_campaign.equals("1")) {
			if (enquiry_campaign_id.equals("0")) {
				msg = msg + "<br>Select Campaign!";
			}
		}
		if (config_sales_enquiry_refno.equals("1")) {
			if (enquiry_qcsno.equals("")) {
				msg = msg + "<br>Enter Enquiry QCS No.!";
			} else {
				if (enquiry_qcsno.length() < 2) {
					msg = msg + "<br>Enquiry QCS No. Should be Atleast Two Digits!";
				}
				if (!enquiry_branch_id.equals("0")) {
					StrSql = "SELECT enquiry_qcsno"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " WHERE enquiry_branch_id = " + enquiry_branch_id + ""
							+ " AND enquiry_qcsno = '" + enquiry_qcsno + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " and enquiry_id!=" + enquiry_id + "";
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Enquiry QCS No. found!";
					}
				}
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_sales_enquiry.*, customer_id, customer_name, contact_id, contact_title_id, contact_fname, contact_lname, title_desc, "
					+ " contact_mobile1, contact_phone1, contact_email1, contact_address, enquiry_preownedvariant_id, priorityenquiry_desc, priorityenquiry_duehrs, "
					// + " preownedmodel_name, variant_name,"
					+ " contact_city_id, contact_pin, branch_name, branch_code, COALESCE(branch_brand_id, 0) branch_brand_id,"
					+ " COALESCE(teamtrans_team_id, 0) AS teamtrans_team_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					// +
					// " LEFT JOIN axela_preowned_model on preownedmodel_id = preowned_preownedmodel_id"
					// +
					// " LEFT JOIN axela_preowned_variant on variant_id = enquiry_preownedvariant_id"
					+ " WHERE enquiry_id=" + enquiry_id + BranchAccess + ExeAccess + " "
					+ " GROUP BY enquiry_id ";
			// SOP("PopulateFields===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					enquiry_id = crs.getString("enquiry_id");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
					enquiry_customer_id = crs.getString("customer_id");
					enquiry_contact_id = crs.getString("contact_id");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					enquiry_model_id = crs.getString("enquiry_model_id");
					enquiry_model_id = crs.getString("enquiry_model_id");
					enquiry_item_id = crs.getString("enquiry_item_id");
					preowned_variant_id = crs.getString("enquiry_preownedvariant_id");
					enquiry_title = crs.getString("enquiry_title");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\" target=_blank>" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\" target=_blank>" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					enquiry_desc = crs.getString("enquiry_desc");
					enquiry_date = crs.getString("enquiry_date");
					enquirydate = strToShortDate(enquiry_date);
					enquiry_close_date = crs.getString("enquiry_close_date");
					closedate = strToShortDate(enquiry_close_date);
					enquiry_value_syscal = crs.getString("enquiry_value_syscal");
					enquiry_value = crs.getString("enquiry_value");
					enquiry_avpresent = crs.getString("enquiry_avpresent");
					enquiry_manager_assist = crs.getString("enquiry_manager_assist");
					team_id = crs.getString("teamtrans_team_id");
					enquiry_emp_id = crs.getString("enquiry_emp_id");
					enquiry_stage_id = crs.getString("enquiry_stage_id");
					enquiry_refemp_id = crs.getString("enquiry_refemp_id");
					enquiry_status_id = crs.getString("enquiry_status_id");
					enquiry_status_date = crs.getString("enquiry_status_date");
					statusdate = strToLongDate(enquiry_status_date);
					enquiry_status_desc = crs.getString("enquiry_status_desc");
					enquiry_priorityenquiry_id = crs.getString("enquiry_priorityenquiry_id");
					priorityenquiry_desc = crs.getString("priorityenquiry_desc");
					priorityenquiry_duehrs = crs.getString("priorityenquiry_duehrs");
					enquiry_soe_id = crs.getString("enquiry_soe_id");
					enquiry_sob_id = crs.getString("enquiry_sob_id");
					enquiry_campaign_id = crs.getString("enquiry_campaign_id");
					enquiry_qcsno = crs.getString("enquiry_qcsno");
					enquiry_notes = crs.getString("enquiry_notes");
					enquiry_entry_id = crs.getString("enquiry_entry_id");
					entry_by = Exename(comp_id, crs.getInt("enquiry_entry_id"));
					entry_date = strToLongDate(crs.getString("enquiry_entry_date"));
					enquiry_modified_id = crs.getString("enquiry_modified_id");
					branch_brand_id = crs.getString("branch_brand_id");
					if (!enquiry_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(enquiry_modified_id));
						modified_date = strToLongDate(crs.getString("enquiry_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry!"));

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
					enquiry_model_id = ExecuteQuery("SELECT variant_preownedmodel_id FROM axela_preowned_variant"
							+ " WHERE variant_id = " + preowned_variant_id);
				}
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET "
						+ "enquiry_branch_id= " + enquiry_branch_id + ", "
						+ "enquiry_customer_id= " + enquiry_customer_id + ", "
						+ "enquiry_contact_id= " + enquiry_contact_id + ", "
						+ "enquiry_title= '" + enquiry_title + "', "
						+ "enquiry_desc= '" + enquiry_desc + "', "
						+ "enquiry_model_id= " + enquiry_model_id + ", "

						+ "enquiry_preownedvariant_id= " + preowned_variant_id + ", "

						+ "enquiry_item_id= " + enquiry_item_id + ", "
						+ "enquiry_date= '" + enquiry_date + "', "
						+ "enquiry_close_date= '" + enquiry_close_date + "', "
						+ "enquiry_value= " + enquiry_value + ", ";

				StrSql = StrSql + "enquiry_avpresent= '" + enquiry_avpresent + "', "
						+ "enquiry_manager_assist= '" + enquiry_manager_assist + "', "
						+ "enquiry_emp_id= " + enquiry_emp_id + ", "
						+ "enquiry_refemp_id= " + enquiry_refemp_id + ", "
						+ "enquiry_stage_id= " + enquiry_stage_id + ", "
						+ "enquiry_status_id= " + enquiry_status_id + ", "
						+ "enquiry_status_date= '" + enquiry_status_date + "', "
						+ "enquiry_status_desc= '" + enquiry_status_desc + "', ";
				// + "enquiry_priorityenquiryfollowup_id= " +
				// enquiry_priorityfollowup_id + ", "
				// + "enquiry_priorityenquiry_id= " + enquiry_priorityenquiry_id + ", ";
				// StrSql = StrSql + "enquiry_project_id= " + enquiry_project_id
				// + ", ";
				if (config_sales_soe.equals("1")) {
					StrSql = StrSql + "enquiry_soe_id= " + enquiry_soe_id + ", ";
				}
				if (config_sales_sob.equals("1")) {
					StrSql = StrSql + "enquiry_sob_id= " + enquiry_sob_id + ", ";
				}
				if (config_sales_campaign.equals("1")) {
					StrSql = StrSql + "enquiry_campaign_id= " + enquiry_campaign_id + ", ";
				}
				StrSql = StrSql + "enquiry_qcsno= '" + enquiry_qcsno + "', ";

				StrSql = StrSql + "enquiry_notes= '" + enquiry_notes + "', "
						+ "enquiry_modified_id= " + enquiry_modified_id + ", "
						+ "enquiry_modified_date= '" + enquiry_modified_date + "'"
						+ " where enquiry_id = " + enquiry_id + " ";
				// SOP(" StrSql--" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
				if (!enquiry_close_date.equals("")) {
					EnquiryPriorityUpdate(comp_id, enquiry_id);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

		}
	}

	protected void DeleteFields() {
		try {

			// Association with Quote
			StrSql = "SELECT quote_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " WHERE quote_enquiry_id =" + enquiry_id + " ";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Enquiry is associated with Quote!";
			}

			// Association with SO
			StrSql = "SELECT so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_enquiry_id =" + enquiry_id + " ";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Enquiry is associated with Sales Order!";
			}

			// Association with Invoice
			// StrSql = "Select invoice_id"
			// + " from " + compdb(comp_id) + "axela_invoice"
			// + " where invoice_enquiry_id = " + enquiry_id + " ";
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg = msg + "<br>Enquiry is associated with Invoice!";
			// }

			// Association with Test Drive
			StrSql = "SELECT testdrive_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " WHERE testdrive_enquiry_id = " + enquiry_id + " ";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Enquiry is associated with Test Drive!";
			}
			if (msg.equals("")) {

				StrSql = "SELECT doc_value"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_docs "
						+ " WHERE doc_enquiry_id = " + enquiry_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(EnquiryDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_docs where doc_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_followup where followup_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm where crm_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_history where history_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id =" + enquiry_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateStage() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT stage_id, stage_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage "
					+ " ORDER BY stage_rank";
			// SOP("----------------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("stage_id")).append("");
				Str.append(StrSelectdrop(crs.getString("stage_id"), enquiry_stage_id));
				Str.append(">").append(crs.getString("stage_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select status_id, status_name "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_status "
					+ " where 1 = 1 "
					+ " order by status_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCountry==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("status_id")).append("");
				Str.append(StrSelectdrop(crs.getString("status_id"), enquiry_status_id));
				Str.append(">").append(crs.getString("status_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "select priorityenquiry_id, priorityenquiry_desc, priorityenquiry_duehrs "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " where 1 = 1 "
					+ " order by priorityenquiry_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option  value=").append(crs.getString("priorityenquiry_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityenquiry_id"), enquiry_priorityenquiry_id));
				Str.append(">").append(crs.getString("priorityenquiry_desc")).append(" (").append(crs.getString("priorityenquiry_duehrs")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateTeamExecutives(String branch_id, String team_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " where 1=1 and emp_sales='1' and emp_active='1' and (emp_branch_id = " + branch_id + " or emp_id = 1 "
					+ " or emp_id in (select empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" + branch_id + "))";
			if (!team_id.equals("0")) {
				StrSql = StrSql + " and emp_id in (select teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe "
						+ " where teamtrans_team_id=" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id "
					+ " order by emp_name ";
			// SOP("StrSql====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_emp_id\" id=\"dr_enquiry_emp_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), enquiry_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTeam(String branch_id, String comp_id, String team_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team"
					+ " where team_branch_id = " + branch_id + ""
					+ " group by team_id "
					+ " order by team_name ";
			// SOP("PopulateTeam---"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_team\" id=\"dr_enquiry_team\" class=\"form-control\" onChange=\"PopulateTeamExc();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id"));
				Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "select soe_id, soe_name "
					+ " from " + compdb(comp_id) + "axela_soe "
					+ " where 1 = 1 "
					+ " group by soe_id"
					+ " order by soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), enquiry_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "select sob_id, sob_name "
					+ " from " + compdb(comp_id) + "axela_sob "
					+ " where 1 = 1 "
					+ " group by sob_id"
					+ " order by sob_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), enquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCampaign(String enquiry_date) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			if (!enquiry_branch_id.equals("0")) {
				StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
						+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
						+ " WHERE  1 = 1 and camptrans_branch_id = " + enquiry_branch_id
						+ " AND campaign_active = '1' "
						+ " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + enquiry_date + "',1,8) "
						+ " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + enquiry_date + "',1,8) "
						+ " GROUP BY campaign_id "
						+ " ORDER BY campaign_name ";
				// SOP("StrSql==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("campaign_id"));
					Str.append(StrSelectdrop(crs.getString("campaign_id"), enquiry_campaign_id));
					Str.append(">").append(crs.getString("campaign_name")).append(" (");
					Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ").append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
				}
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateReferenceExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1 = 1"
					+ " AND emp_sales='0'"
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_enquiry_refemp_id\" id=\"dr_enquiry_refemp_id\" class=\"dropdown form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(Selectdrop(crs.getInt("emp_id"), enquiry_refemp_id));
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
	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob, config_sales_campaign, "
				+ " config_sales_enquiry_refno"
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp ";
		// SOP(StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateContactDetails(HttpServletResponse response) {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT customer_id, contact_id, customer_name, "
					+ " concat(title_desc, ' ', contact_fname, ' ', contact_lname) as contact_name "
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER join " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER join " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id "
					+ " WHERE 1=1 and contact_id = " + enquiry_contact_id + "";
			// SOP("v==" + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_customer_id = crs.getString("customer_id");
					enquiry_contact_id = crs.getString("contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\" target=_blank>" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\" target=_blank>" + crs.getString("contact_name") + "</a>";
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1"
					+ "	AND model_sales = 1"
					+ "	AND model_active = 1"
					+ " AND model_brand_id = " + branch_brand_id
					+ " ORDER BY model_name";
			// SOP("StrSql------PopulateModel------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), enquiry_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1=1"
					+ " AND item_type_id = 1 "
					+ " AND item_model_id = " + enquiry_model_id
					+ " ORDER BY item_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), enquiry_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateVariant() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE 1=1";
			// if (!preowned_model_id.equals("0")) {
			StrSql = StrSql + " AND variant_preownedmodel_id = " + preowned_variant_id;
			// }
			StrSql = StrSql + " ORDER BY variant_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("");
				Str.append(StrSelectdrop(crs.getString("variant_id"), preowned_variant_id));
				Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id IN (1, 2)";
			SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=")
						.append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"),
						branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code"))
						.append(")</option>\n");
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
}
