//@Bhagwan Singh 11 feb 2013
package axela.customer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Customer_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String tag = "";
	public String url_tag = "";
	public String customer_id = "0";
	public String branch_id = "0", customer_branch_id = "0";
	public String customer_name = "";
	public String customer_alias = "";
	public String customer_code = "";
	public String customer_mobile1 = "", customer_mobile2 = "", customer_mobile3 = "", customer_mobile4 = "", customer_mobile5 = "", customer_mobile6 = "";
	public String customer_phone1 = "", customer_phone2 = "";
	public String customer_phone3 = "", customer_phone4 = "";
	public String customer_fax1 = "", customer_fax2 = "";
	public String customer_email1 = "", customer_email2 = "";
	public String customer_website1 = "", customer_website2 = "";
	public String customer_gst_regdate = "";
	public String customer_gst_no = "";
	public String customer_arn_no = "";
	public String customer_itstatus_id = "0";
	public String customer_address = "";
	public String customer_city_id = "0";
	public String customer_tds = "0";
	public String customer_pin = "";
	public String customer_landmark = "";
	public String customer_pan_no = "", customer_soe_id = "0", customer_sob_id = "0";
	public String fo_group_trans_temp = "";
	public String panregu = "";
	public String config_customer_soe = "";
	public String config_customer_sob = "";
	public String config_customer_dupnames = "";
	public String emp_role_id = "";
	public String gst_regdate = "";
	// public String customer_empcount_id = "0";
	public String customer_emp_id = "0";
	public String customer_type = "1";
	public String customer_since = "", customersince = "";
	public String customer_active = "";
	public String customer_notes = "";
	public String customer_entry_id = "0", customer_entry_date = "";
	public String customer_modified_id = "0", customer_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String[] fo_group_trans = new String[10];
	public String QueryString = "";
	public City_Check citycheck = new City_Check();
	public Connection conntx = null;
	public Statement stmttx = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_customer_access", request, response);
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				url_tag = PadQuotes(request.getParameter("tag"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (branch_id.equals("") || branch_id.equals("0")) {
					customer_branch_id = PadQuotes(request.getParameter("dr_customer_branch_id"));
				} else {
					customer_branch_id = branch_id;
				}
				if (emp_id.equals("") || emp_id.equals("0")) {
					customer_emp_id = CNumeric(PadQuotes(request.getParameter("dr_customer_emp_id")));
				} else {
					customer_emp_id = emp_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				}
				if (update.equals("yes")) {
					status = "Update";
					PopulateFields(response);
				}
				if (url_tag.equals("vendors")) {
					tag = "Supplier";
				} else {
					tag = "Customer";
				}

				PopulateConfigDetails();
				if ("yes".equals(add)) {
					status = "Add";
					if (!addB.equals("yes")) {
						customer_id = "0";
						customer_code = "";
						customer_name = "";
						customer_alias = "";
						customer_mobile1 = "91-";
						customer_mobile2 = "";
						customer_mobile3 = "";
						customer_mobile4 = "";
						customer_mobile5 = "";
						customer_mobile6 = "";
						customer_phone1 = "";
						customer_phone2 = "";
						customer_phone3 = "";
						customer_phone4 = "";
						customer_fax1 = "";
						customer_fax2 = "";
						customer_email1 = "";
						customer_email2 = "";
						customer_website1 = "";
						customer_website2 = "";
						customer_address = "";
						customer_pin = "";
						customer_landmark = "";
						customer_pan_no = "";
						customer_arn_no = "";
						customer_soe_id = "0";
						customer_sob_id = "0";
						// customer_empcount_id = "0";
						// customer_emp_id = emp_id;
						customer_since = "";
						customer_active = "1";
						customer_notes = "";
						customer_entry_id = "0";
						customer_entry_date = "";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_customer_add", request).equals("1")) {
							CheckForm();
							customer_entry_id = CNumeric(GetSession("emp_id", request));
							customer_entry_date = ToLongDate(kknow());
							if (msg.equals("")) {
								AddFields();
								UpdateList();
							}
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("customer-list.jsp?customer_id=" + customer_id + "&tag=" + url_tag + "&msg=" + tag + " added successfully!"));
								status = "";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!("yes").equals(updateB) && !("Delete " + tag + "").equals(deleteB)) {
						PopulateFields(response);
					} else if (("yes").equals(updateB) && !("Delete " + tag + "").equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_customer_edit", request).equals("1")) {
							UpdateList();
							CheckForm();
							customer_modified_id = CNumeric(GetSession("emp_id", request));
							customer_modified_date = ToLongDate(kknow());
							if (msg.equals("")) {
								UpdateFields(request, response);
							}
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = tag + " updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("customer-list.jsp?customer_id=" + customer_id + "&tag=" + url_tag + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (("Delete " + tag + "").equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_customer_delete", request).equals("1")) {
							UpdateList();
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("customer-list.jsp?tag=" + url_tag + "&msg=Customer deleted successfully!"));
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
		customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		customer_alias = PadQuotes(request.getParameter("txt_customer_alias"));
		customer_code = PadQuotes(request.getParameter("txt_customer_code"));
		customer_mobile1 = PadQuotes(request.getParameter("txt_customer_mobile1"));
		customer_mobile2 = PadQuotes(request.getParameter("txt_customer_mobile2"));
		customer_mobile3 = PadQuotes(request.getParameter("txt_customer_mobile3"));
		customer_mobile4 = PadQuotes(request.getParameter("txt_customer_mobile4"));
		customer_mobile5 = PadQuotes(request.getParameter("txt_customer_mobile5"));
		customer_mobile6 = PadQuotes(request.getParameter("txt_customer_mobile6"));
		customer_phone1 = PadQuotes(request.getParameter("txt_customer_phone1"));
		customer_phone2 = PadQuotes(request.getParameter("txt_customer_phone2"));
		customer_phone3 = PadQuotes(request.getParameter("txt_customer_phone3"));
		customer_phone4 = PadQuotes(request.getParameter("txt_customer_phone4"));
		customer_fax1 = PadQuotes(request.getParameter("txt_customer_fax1"));
		customer_fax2 = PadQuotes(request.getParameter("txt_customer_fax2"));
		customer_email1 = PadQuotes(request.getParameter("txt_customer_email1"));
		customer_email2 = PadQuotes(request.getParameter("txt_customer_email2"));
		customer_website1 = PadQuotes(request.getParameter("txt_customer_website1"));
		customer_website2 = PadQuotes(request.getParameter("txt_customer_website2"));

		customer_gst_no = PadQuotes(request.getParameter("txt_customer_gst_no"));
		customer_gst_regdate = PadQuotes(request.getParameter("txt_customer_gst_regdate"));
		customer_arn_no = PadQuotes(request.getParameter("txt_customer_arn_no"));
		customer_itstatus_id = PadQuotes(request.getParameter("dr_customer_itstatus_id"));
		customer_address = PadQuotes(request.getParameter("txt_customer_address"));
		customer_city_id = CNumeric(PadQuotes(request.getParameter("maincity")));
		customer_pin = PadQuotes(request.getParameter("txt_customer_pin"));
		customer_landmark = PadQuotes(request.getParameter("txt_customer_landmark"));
		customer_tds = CheckBoxValue(PadQuotes(request.getParameter("chk_customer_tds")));
		customer_pan_no = PadQuotes(request.getParameter("txt_customer_pan_no"));
		customer_soe_id = CNumeric(PadQuotes(request.getParameter("drop_customer_soe_id")));
		customer_sob_id = CNumeric(PadQuotes(request.getParameter("drop_customer_sob_id")));
		// customer_empcount_id =
		// PadQuotes(request.getParameter("drop_customer_empcount_id"));
		customer_type = CNumeric(PadQuotes(request.getParameter("dr_customer_type")));
		customer_since = PadQuotes(request.getParameter("txt_customer_since"));
		customer_active = CheckBoxValue(PadQuotes(request.getParameter("chk_customer_active")));
		customer_notes = PadQuotes(request.getParameter("txt_customer_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		fo_group_trans = request.getParameterValues("fo_group_trans");
		if (customer_entry_id.equals("")) {
			customer_entry_id = "0";
		}
	}

	protected void CheckForm() {
		String duplicate_gst = "";
		if (customer_name.equals("")) {
			msg = msg + "<br>Enter the " + tag + " Name!";
		} else {
			customer_name = toTitleCase(customer_name);
		}

		if (!customer_name.equals("") && config_customer_dupnames.equals("0")) {
			StrSql = "SELECT customer_name FROM " + compdb(comp_id) + "axela_customer where customer_name = '" + customer_name + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " and customer_id != " + customer_id;
			}
			// SOP("StrSql=====" + StrSql);
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar " + tag + " found!";
			}
		}

		if (!customer_alias.equals("")) {
			StrSql = "SELECT customer_alias FROM " + compdb(comp_id) + "axela_customer where customer_alias = '" + customer_alias + "'";
			if (update.equals("yes")) {
				StrSql += " AND customer_id != " + customer_id;
			}
			// SOP("StrSql=====" + StrSql);
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Alias found!";
			}

		}

		if (!customer_code.equals("")) {
			StrSql = "SELECT customer_code FROM " + compdb(comp_id) + "axela_customer WHERE customer_code = '" + customer_code + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " and customer_id!=" + customer_id;
			}
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar " + tag + " Code found!";
			}
		}
		if (!customer_phone1.equals("")) {
			if (!IsValidPhoneNo11(customer_phone1)) {
				msg = msg + "<br>Enter valid Phone1!";
			}
		}

		if (!customer_phone2.equals("")) {
			if (!IsValidPhoneNo11(customer_phone2)) {
				msg = msg + "<br>Enter valid Phone2!";
			}
		}

		if (!customer_phone3.equals("") && !IsValidPhoneNo11(customer_phone3)) {
			msg = msg + "<br>Enter valid Phone3!";
		}

		if (!customer_phone4.equals("") && !IsValidPhoneNo11(customer_phone4)) {
			msg = msg + "<br>Enter valid Phone4!";
		}

		if (customer_mobile1.equals("91-")) {
			customer_mobile1 = "";
		}

		if (customer_mobile1.equals("")) {
			msg += "<br>Enter Customer Mobile 1!";
		} else {
			if (!IsValidMobileNo11(customer_mobile1)) {
				msg += "<br>Enter Valid Mobile 1!";
			}
		}

		if (!customer_mobile2.equals("") && !IsValidMobileNo11(customer_mobile2)) {
			msg = msg + "<br>Mobile2 is Invalid!";
		}

		if (!customer_mobile3.equals("") && !IsValidMobileNo11(customer_mobile3)) {
			msg = msg + "<br>Mobile3 is Invalid!";
		}

		if (!customer_mobile4.equals("") && !IsValidMobileNo11(customer_mobile4)) {
			msg = msg + "<br>Mobile4 is Invalid!";
		}

		if (!customer_mobile5.equals("") && !IsValidMobileNo11(customer_mobile5)) {
			msg = msg + "<br>Mobile5 is Invalid!";
		}

		if (!customer_mobile6.equals("") && !IsValidMobileNo11(customer_mobile6)) {
			msg = msg + "<br>Mobile6 is Invalid!";
		}

		// if (customer_mobile1.contains(customer_mobile2)) {
		// msg += "<br>Mobile1 and Mobile2 Cannot be same!";
		// }
		if (!customer_mobile1.equals("") && (customer_mobile2 + "." + customer_mobile3 + "." + customer_mobile4 + "." + customer_mobile5 + "." + customer_mobile6).contains(customer_mobile1)) {
			msg += "<br>Similar Mobile Number found!";
		} else if (!customer_mobile2.equals("") && (customer_mobile1 + "." + customer_mobile3 + "." + customer_mobile4 + "." + customer_mobile5 + "." + customer_mobile6).contains(customer_mobile2)) {
			msg += "<br>Similar Mobile Number found!";
		} else if (!customer_mobile3.equals("") && (customer_mobile1 + "." + customer_mobile2 + "." + customer_mobile4 + "." + customer_mobile5 + "." + customer_mobile6).contains(customer_mobile3)) {
			msg += "<br>Similar Mobile Number found!";
		} else if (!customer_mobile4.equals("") && (customer_mobile1 + "." + customer_mobile2 + "." + customer_mobile3 + "." + customer_mobile5 + "." + customer_mobile6).contains(customer_mobile4)) {
			msg += "<br>Similar Mobile Number found!";
		} else if (!customer_mobile5.equals("") && (customer_mobile1 + "." + customer_mobile2 + "." + customer_mobile3 + "." + customer_mobile4 + "." + customer_mobile6).contains(customer_mobile5)) {
			msg += "<br>Similar Mobile Number found!";
		} else if (!customer_mobile6.equals("") && (customer_mobile1 + "." + customer_mobile2 + "." + customer_mobile3 + "." + customer_mobile4 + "." + customer_mobile5).contains(customer_mobile6)) {
			msg += "<br>Similar Mobile Number found!";
		}

		if (!customer_phone1.equals("") && (customer_phone2 + "." + customer_phone3 + "." + customer_phone4).contains(customer_phone1)) {
			msg += "<br>Similar Phone Number found!";
		} else if (!customer_phone2.equals("") && (customer_phone1 + "." + customer_phone3 + "." + customer_phone4).contains(customer_phone2)) {
			msg += "<br>Similar Phone Number found!";
		} else if (!customer_phone3.equals("") && (customer_phone1 + "." + customer_phone2 + "." + customer_phone4).contains(customer_phone3)) {
			msg += "<br>Similar Phone Number found!";
		} else if (!customer_phone4.equals("") && (customer_phone1 + "." + customer_phone2 + "." + customer_phone3).contains(customer_phone4)) {
			msg += "<br>Similar Phone Number found!";
		}

		if (!customer_email1.equals("") && !IsValidEmail(customer_email1)) {
			msg = msg + "<br>Email1 is invalid!";
		} else {
			customer_email1 = customer_email1.toLowerCase();
		}

		if (!customer_email2.equals("") && !IsValidEmail(customer_email2)) {
			msg = msg + "<br>Email2 is invalid!";
		} else {
			customer_email2 = customer_email2.toLowerCase();
		}

		if (!customer_email1.equals("")
				&& !customer_email2.equals("")
				&& customer_email2.equals(customer_email1)) {
			msg = msg + "<br>Email2 is same as Email1!";
		}

		if (!customer_website1.equals("")) {
			customer_website1 = WebValidate(customer_website1);
		}

		if (!customer_website2.equals("")) {
			customer_website2 = WebValidate(customer_website2);
		}

		if (!customer_website1.equals("") && !customer_website2.equals("") && customer_website2.equals(customer_website1)) {
			msg = msg + "<br>Website 2 is same as Website 1!";
		}
		if (!customer_gst_no.equals("") && customer_gst_regdate.equals("")) {
			msg = msg + "<br>Select GSTIN Date!";
		}
		if (!customer_gst_regdate.equals("") && customer_gst_no.equals("")) {
			msg = msg + "<br>Select GSTIN!";
		}
		if (!customer_gst_no.equals("")) {
			StrSql = "SELECT count(customer_gst_no)"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_gst_no!=\"\""
					+ " AND customer_type =" + customer_type + ""
					+ " AND customer_id!=" + customer_id
					+ " AND customer_gst_no='" + customer_gst_no + "'";
			if (!CNumeric(PadQuotes(ExecuteQuery(StrSql))).equals("0")) {
				msg = msg + "<br>Similer GSTIN Found!";
			}
		}
		// if (customer_gst_no.equals("") && !customer_itstatus_id.equals("2")) {
		// msg = msg + "<br>Enter GSTIN!";
		// } else
		if (!customer_gst_no.equals("") && !customer_gst_no.matches("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z0-9]{3}")) {
			msg = msg + "<br>Enter Valid GSTIN!";

		}
		// if (customer_gst_regdate.equals("") && !customer_itstatus_id.equals("2")) {
		// msg = msg + "<br>Select GSTIN Date!";
		// } else
		if (!customer_gst_regdate.equals("")) {
			customer_gst_regdate = ConvertShortDateToStr(customer_gst_regdate);
			gst_regdate = strToShortDate(customer_gst_regdate);
		} else if (isValidDateFormatShort(customer_gst_regdate)) {
			msg = msg + "<br>Enter Valid GSTIN Date!";

		}

		if (!customer_arn_no.equals("") && !customer_arn_no.matches("[a-zA-Z]{2}\\d{6}\\d{6}\\d{1}")) {
			msg = msg + "<br>Enter Valid ARN !";

		}
		if (customer_itstatus_id.equals("0")) {
			msg = msg + "<br>Select Status!";
		}

		if (customer_address.equals("")) {
			msg = msg + "<br>Enter Address!";
		}
		if (customer_city_id.equals("0")) {
			msg = msg + "<br>Select City!";
		}

		if (customer_pin.equals("")) {
			msg = msg + "<br>Enter Pin Code!";
		} else if (!customer_pin.equals("") && !isNumeric(customer_pin)) {
			msg = msg + "<br>Pin Code: Enter Numeric!";
		}

		if (!customer_tds.equals("0")) {
			if (customer_pan_no.equals("")) {
				msg = msg + "<br>Enter PAN!";
			}
			else if (!customer_pan_no.equals("") && !customer_pan_no.matches("[A-Z]{5}\\d{4}[A-Z]{1}")) {
				msg = msg + "<br>PAN Invalid!";
			}
		}

		if (config_customer_soe.equals("1") && customer_soe_id.equals("0")) {
			msg = msg + "<br>Select Source of Enquiry!";
		}

		if (config_customer_sob.equals("1") && customer_sob_id.equals("0")) {
			msg = msg + "<br>Select Source of Business!";
		}

		// if (customer_empcount_id.equals("0")) {
		// msg = msg + "<br>Select Executive Count!";
		// }

		if (!customer_email1.equals("")) {
			customer_email1 = customer_email1.toLowerCase();
		}

		if (!customer_email2.equals("")) {
			customer_email2 = customer_email2.toLowerCase();
		}

		if (customer_type.equals("0")) {
			msg += "<br>Select Type!";
		}
		if (!customer_since.equals("")) {
			if (isValidDateFormatShort(customer_since)) {
				customer_since = ConvertShortDateToStr(customer_since);
				customersince = strToShortDate(customer_since);
			} else {
				customersince = customer_since;
				msg = msg + "<br>Enter Valid " + tag + " Since Date!";
				customer_since = "";
			}
		}
		// if (customer_since.equals("")) {
		// msg += "<br>Enter " + tag + " Since Date!";
		// } else {
		// if (isValidDateFormatShort(customer_since)) {
		// custsince = ConvertShortDateToStr(customer_since);
		// } else {
		// customer_since = custsince;
		// msg += "<br>Enter valid " + tag + " Since Date!";
		// custsince = "";
		// }
		// }
	}
	protected void AddFields() {
		int count = 0;
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
						+ " (customer_branch_id,"
						+ " customer_name,"
						+ " customer_alias,"
						+ " customer_code,"
						+ " customer_phone1,"
						+ " customer_phone2,"
						+ " customer_phone3,"
						+ " customer_phone4,"
						+ " customer_mobile1,"
						+ " customer_mobile2,"
						+ " customer_mobile3,"
						+ " customer_mobile4,"
						+ " customer_mobile5,"
						+ " customer_mobile6,"
						+ " customer_fax1,"
						+ " customer_fax2,"
						+ " customer_email1,"
						+ " customer_email2,"
						+ " customer_website1,"
						+ " customer_website2,"

						+ " customer_gst_no,"
						+ " customer_gst_regdate,"
						+ " customer_arn_no,"
						+ " customer_itstatus_id,"
						+ " customer_address,"
						+ " customer_city_id,"
						+ " customer_tds,"
						+ " customer_pin,"
						+ " customer_landmark,"
						+ " customer_pan_no,"
						+ " customer_soe_id,"
						+ " customer_sob_id,"
						// + " customer_empcount_id,"
						+ " customer_emp_id, "
						+ " customer_since,"
						+ " customer_type,"
						+ " customer_active,"
						+ " customer_accgroup_id,"
						+ " customer_notes,"
						+ " customer_entry_id,"
						+ " customer_entry_date,"
						+ " customer_modified_id,"
						+ " customer_modified_date)"
						+ " VALUES"
						+ " ("
						+ " '" + customer_branch_id + "',"
						+ " '" + customer_name + "',"
						+ " '" + customer_alias + "',"
						+ " '" + customer_code + "',"
						+ " '" + customer_phone1 + "',"
						+ " '" + customer_phone2 + "',"
						+ " '" + customer_phone3 + "',"
						+ " '" + customer_phone4 + "',"
						+ " '" + customer_mobile1 + "',"
						+ " '" + customer_mobile2 + "',"
						+ " '" + customer_mobile3 + "',"
						+ " '" + customer_mobile4 + "',"
						+ " '" + customer_mobile5 + "',"
						+ " '" + customer_mobile6 + "',"
						+ " '" + customer_fax1 + "',"
						+ " '" + customer_fax2 + "',"
						+ " '" + customer_email1 + "',"
						+ " '" + customer_email2 + "',"
						+ " '" + customer_website1 + "',"
						+ " '" + customer_website2 + "',"
						+ " '" + customer_gst_no + "',"
						+ " '" + customer_gst_regdate + "',"
						+ " '" + customer_arn_no + "',"
						+ " '" + customer_itstatus_id + "',"
						+ " '" + customer_address + "',"
						+ " " + customer_city_id + ","
						+ " " + customer_tds + ","
						+ " '" + customer_pin + "',"
						+ " '" + customer_landmark + "',"
						+ " '" + customer_pan_no + "',"
						+ " " + customer_soe_id + ","
						+ " " + customer_sob_id + ","
						// + " " + customer_empcount_id + ","
						+ " " + customer_emp_id + ","
						+ " IF (customer_since = '','"
						+ customer_entry_date + "','"
						+ customer_since + "'),"
						+ " " + customer_type + ","
						+ " '" + customer_active + "',";

				if (customer_type.equals("1")) {
					StrSql += " 32,";
				} else if (customer_type.equals("2")) {
					StrSql += " 31,";
				}
				StrSql += " '" + customer_notes + "',"
						+ " " + customer_entry_id + ","
						+ " '" + customer_entry_date + "',"
						+ " '0',"
						+ " '')";
				// SOP("StrSql===addf===" + StrSqlBreaker(StrSql));
				customer_id = UpdateQueryReturnID(StrSql);
			} catch (Exception ex) {
				SOPError(this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_state ON state_id = city_state_id"
					+ " WHERE customer_id = " + customer_id + BranchAccess + "";

			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("PopulateFields=====" + StrSqlBreaker(StrSql));

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("customer_id");
					customer_branch_id = crs.getString("customer_branch_id");
					customer_name = crs.getString("customer_name");
					customer_alias = crs.getString("customer_alias");
					customer_code = crs.getString("customer_code");
					customer_mobile1 = crs.getString("customer_mobile1");
					customer_mobile2 = crs.getString("customer_mobile2");
					customer_mobile3 = crs.getString("customer_mobile3");
					customer_mobile4 = crs.getString("customer_mobile4");
					customer_mobile5 = crs.getString("customer_mobile5");
					customer_mobile6 = crs.getString("customer_mobile6");
					customer_phone1 = crs.getString("customer_phone1");
					customer_phone2 = crs.getString("customer_phone2");
					customer_phone3 = crs.getString("customer_phone3");
					customer_phone4 = crs.getString("customer_phone4");
					customer_fax1 = crs.getString("customer_fax1");
					customer_fax2 = crs.getString("customer_fax2");
					customer_email1 = crs.getString("customer_email1");
					customer_email2 = crs.getString("customer_email2");
					customer_website1 = crs.getString("customer_website1");
					customer_website2 = crs.getString("customer_website2");
					customer_gst_no = crs.getString("customer_gst_no");
					gst_regdate = strToShortDate(crs.getString("customer_gst_regdate"));
					customer_arn_no = crs.getString("customer_arn_no");
					customer_itstatus_id = crs.getString("customer_itstatus_id");
					customer_address = crs.getString("customer_address");
					customer_city_id = crs.getString("customer_city_id");
					customer_tds = crs.getString("customer_tds");
					customer_pin = crs.getString("customer_pin");
					customer_landmark = crs.getString("customer_landmark");
					customer_pan_no = crs.getString("customer_pan_no");
					customer_soe_id = crs.getString("customer_soe_id");
					customer_sob_id = crs.getString("customer_sob_id");
					// customer_empcount_id =
					// crs.getString("customer_empcount_id");
					customer_emp_id = crs.getString("customer_emp_id");
					customersince = strToShortDate(crs.getString("customer_since"));
					customer_type = crs.getString("customer_type");
					customer_active = crs.getString("customer_active");
					customer_notes = crs.getString("customer_notes");
					customer_entry_id = crs.getString("customer_entry_id");
					entry_by = Exename(comp_id, crs.getInt("customer_entry_id"));
					entry_date = strToLongDate(crs.getString("customer_entry_date"));
					customer_modified_id = crs.getString("customer_modified_id");
					if (!customer_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(customer_modified_id));
						modified_date = strToLongDate(crs.getString("customer_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid " + tag + "!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
					+ " SET"
					+ " customer_branch_id = " + customer_branch_id + ","
					+ " customer_name = '" + customer_name + "',"
					+ " customer_alias = '" + customer_alias + "',"
					+ " customer_code = '" + customer_code + "',"
					+ " customer_mobile1 = '" + customer_mobile1 + "',"
					+ " customer_mobile2 = '" + customer_mobile2 + "',"
					+ " customer_mobile3 = '" + customer_mobile3 + "',"
					+ " customer_mobile4 = '" + customer_mobile4 + "',"
					+ " customer_mobile5 = '" + customer_mobile5 + "',"
					+ " customer_mobile6 = '" + customer_mobile6 + "',"
					+ " customer_phone1 = '" + customer_phone1 + "',"
					+ " customer_phone2 = '" + customer_phone2 + "',"
					+ " customer_phone3 = '" + customer_phone3 + "',"
					+ " customer_phone4 = '" + customer_phone4 + "',"
					+ " customer_fax1 = '" + customer_fax1 + "',"
					+ " customer_fax2 = '" + customer_fax2 + "',"
					+ " customer_email1 = '" + customer_email1 + "',"
					+ " customer_email2 = '" + customer_email2 + "',"
					+ " customer_website1 = '" + customer_website1 + "',"
					+ " customer_website2 = '" + customer_website2 + "',"
					+ " customer_gst_no = '" + customer_gst_no + "',"
					+ " customer_gst_regdate = '" + customer_gst_regdate + "',"
					+ " customer_arn_no = '" + customer_arn_no + "',"
					+ " customer_itstatus_id = '" + customer_itstatus_id + "',"
					+ " customer_address = '" + customer_address + "',"
					+ " customer_tds = " + customer_tds + ","
					+ " customer_city_id = " + customer_city_id + ","
					+ " customer_pin = '" + customer_pin + "',"
					+ " customer_landmark = '" + customer_landmark + "',"
					+ " customer_pan_no = '" + customer_pan_no + "',"
					+ " customer_soe_id = '" + customer_soe_id + "',"
					+ " customer_sob_id = " + customer_sob_id + ","
					// + " customer_empcount_id = " + customer_empcount_id + ","
					+ " customer_emp_id = " + customer_emp_id + ", "
					+ " customer_since = '" + customer_since + "',"
					+ " customer_type = " + customer_type + ","
					+ " customer_active = '" + customer_active + "',";
			if (customer_type.equals("1")) {
				StrSql += " customer_accgroup_id = 32,";
			} else if (customer_type.equals("2")) {
				StrSql += " customer_accgroup_id = 31,";
			}
			StrSql += " customer_notes = '" + customer_notes + "',"
					+ " customer_modified_id = " + customer_modified_id + ","
					+ " customer_modified_date = '" + customer_modified_date + "'"
					+ " WHERE customer_id = " + customer_id + " ";

			updateQuery(StrSql);
		}
	}
	protected void DeleteFields(HttpServletResponse response) {
		// check Association for Activity
		StrSql = "SELECT contact_customer_id FROM " + compdb(comp_id) + "axela_activity"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = activity_contact_id"
				+ " WHERE contact_customer_id = " + customer_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Activity!";
		}

		// check Association for contacts
		StrSql = "SELECT contact_customer_id FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " WHERE contact_customer_id = " + customer_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Contact!";
		}

		// check Association for sales
		StrSql = "SELECT enquiry_customer_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_customer_id = " + customer_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Enquiry!";
		}

		// Association with Quote
		StrSql = "SELECT quote_customer_id FROM " + compdb(comp_id) + "axela_sales_quote"
				+ " WHERE quote_customer_id = " + customer_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Quote!";
		}

		// Association with SO
		StrSql = "SELECT so_customer_id FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_customer_id = " + customer_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Sales Order!";
		}

		// Association with Invoice
		StrSql = "SELECT voucher_customer_id FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_customer_id = " + customer_id + ""
				+ " AND voucher_vouchertype_id = 6";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Invoice!";
		}

		// Association with Receipt
		StrSql = "SELECT voucher_customer_id FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_customer_id = " + customer_id + ""
				+ " AND voucher_vouchertype_id = 9";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Receipt!";
		}

		// Association with Payment
		StrSql = "SELECT voucher_customer_id FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_customer_id = " + customer_id + ""
				+ " AND voucher_vouchertype_id = 15";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>" + tag + " is associated with Payment!";
		}

		// check Association for Voucher
		StrSql = "select voucher_id from " + compdb(comp_id) + "axela_acc_voucher where"
				+ " voucher_customer_id = " + customer_id + "";
		if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
			msg = msg + "<br>" + tag + " is associated with Voucher!";
		}
		try {
			if (msg.equals("")) {
				StrSql = "SELECT doc_value FROM " + compdb(comp_id) + "axela_customer_docs"
						+ " WHERE doc_customer_id = " + customer_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(CustomerDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer_docs"
						+ " WHERE doc_customer_id = " + customer_id + "";
				updateQuery(StrSql);
				// StrSql = "DELETE FROM " + compdb(comp_id) +
				// "axela_customer_contact"
				// + " WHERE contact_customer_id = " + customer_id + "";
				// updateQuery(StrSql);
				// StrSql = "DELETE FROM " + compdb(comp_id) +
				// "axela_customer_group_trans"
				// + " WHERE trans_customer_id = " + customer_id + "";
				// updateQuery(StrSql);
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE customer_id = " + customer_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void UpdateList() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_customer_group_trans"
					+ " WHERE trans_customer_id = " + customer_id + "";
			updateQuery(StrSql);

			if (fo_group_trans != null) {
				for (int i = 0; i < fo_group_trans.length; i++) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_group_trans"
							+ " (trans_customer_id,"
							+ " trans_group_id) "
							+ " VALUES"
							+ " (" + customer_id + ","
							+ " " + fo_group_trans[i] + ")";
					updateQuery(StrSql);
				}
			}
		}
	}

	StringBuilder Str = new StringBuilder();
	public String PopulateItStatus() {
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT itstatus_id, itstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_itstatus"
					+ " ORDER BY itstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("itstatus_id"));
				Str.append(StrSelectdrop(crs.getString("itstatus_id"), customer_itstatus_id));
				Str.append(">").append(crs.getString("itstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateGroup() {
		try {
			StrSql = "SELECT group_id, CONCAT(group_desc, '') AS group_desc"
					+ " FROM " + compdb(comp_id) + "axela_customer_group"
					+ " ORDER BY group_desc";

			// SOP("StrSql==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id")).append(">");
				Str.append(crs.getString("group_desc") + "(" + crs.getString("group_id") + ")").append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_customer_soe, config_customer_sob, config_customer_dupnames"
				+ " FROM " + compdb(comp_id) + "axela_config";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_customer_soe = crs.getString("config_customer_soe");
				config_customer_sob = crs.getString("config_customer_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateGroupTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			if (add.equals("yes") && fo_group_trans != null) {
				StrSql = "SELECT CONCAT(group_desc, '') AS group_desc, group_id"
						+ " FROM " + compdb(comp_id) + "axela_customer_group"
						+ " ORDER BY group_desc";
			} else {
				StrSql = "SELECT group_id, CONCAT(group_desc, '') AS group_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_group"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_group_trans ON trans_group_id = group_id"
						+ " WHERE trans_customer_id = '" + customer_id + "'"
						+ " ORDER BY group_desc";
			}
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if (add.equals("yes") && fo_group_trans != null) {
					for (int i = 0; i < fo_group_trans.length; i++) {
						if (crs.getString("group_id").equals(fo_group_trans[i])) {
							Str.append("<option value=").append(crs.getString("group_id")).append(" selected>");
							Str.append(crs.getString("group_desc")).append("</option> \n");
						}
					}
				} else if (update.equals("yes")) {
					Str.append("<option value=").append(crs.getString("group_id")).append(" selected>");
					Str.append(crs.getString("group_desc")).append("</option>\n");
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

	public String PopulateSob() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id"));
				Str.append(StrSelectdrop(crs.getString("sob_id"), customer_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id"));
				Str.append(StrSelectdrop(crs.getString("soe_id"), customer_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEmp() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_sales = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), customer_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");

		Str.append("<option value=1").append(StrSelectdrop("1", customer_type))
				.append(">").append("Customer");
		Str.append("</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", customer_type))
				.append(">").append("Supplier");
		Str.append("</option>\n");

		return Str.toString();
	}
	// public String toTitleCase1(String str) {
	// StringBuilder sb = new StringBuilder();
	// str = str.toLowerCase();
	// StringTokenizer strTitleCase = new StringTokenizer(str);
	// while (strTitleCase.hasMoreTokens()) {
	// String s = strTitleCase.nextToken();
	// int scount = strTitleCase.countTokens();
	// SOP("s ==== " + s);
	// SOP("scount ==== " + scount);
	// if (!s.substring(0, 1).equals("(")) {
	// sb.append(s.replaceFirst(s.substring(0, 1), s.substring(0,
	// 1).toUpperCase()));
	// if (scount != 0) {
	// sb.append(" ");
	// }
	// } else {
	// sb.append(s + " ");
	// }
	// }
	// return sb.toString();
	// }
}
