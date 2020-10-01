package axela.inventory;
//@Bhagwan Singh 16 feb 2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String BranchAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String branch_id = "0";
	public String branch_brand_id = "0";
	public String vehstock_id = "0";
	public String vehstock_branch_id = "0", vehstock_model_id = "0";
	public String vehstock_item_id = "0";
	public String vehstock_modelyear = "";
	public String vehstock_mfgyear = "";
	public String vehstock_ref_no = "";
	public String vehstock_comm_no = "";
	public String vehstock_chassis_prefix = "";
	public String vehstock_chassis_no = "";
	public String vehstock_paintwork_id = "0";
	public String vehstock_engine_no = "";
	public String vehstock_fastag = "";
	public String vehstock_key_no = "";
	public String vehstock_vehstocklocation_id = "1";
	public String vehstock_parking_no = "";
	public String vehstock_confirmed_sgw = "", confirmed_sgw = "";
	public String vehstock_invoice_no = "";
	public String invoice_date = "", ordered_date = "";
	public String vehstock_invoice_date = "";
	public String vehstock_ordered_date = "";
	public String vehstock_invoice_amount = "";
	// public String vehstock_pi_date = "";
	public String vehstock_invoiceamountaftertax = "";
	public String pi_date = "";
	public String vehstock_arrival_date = "";
	public String vehstock_pdi_date = "";
	public String vehstock_dms_date = "";

	public String vehstock_nsc = "";
	public String arrival_date = "";
	// public String vehstock_fincomp_id = "0";
	// public String vehstock_finance_amt = "";
	// public String vehstock_retail_finance_amt = "";
	// public String vehstock_utr_no1 = "";
	// public String vehstock_paid_amt1 = "";
	// public String paid_date1 = "";
	// public String vehstock_paid_date1 = "";
	// public String vehstock_utr_no2 = "";
	// public String vehstock_paid_amt2 = "";
	// public String paid_date2 = "";
	// public String vehstock_paid_date2 = "";
	// public String vehstock_utr_no3 = "";
	// public String vehstock_paid_amt3 = "";
	// public String paid_date3 = "";
	// public String vehstock_paid_date3 = "";
	public String nadcon_date = "";
	public String vehstock_stockpriority_id = "0";
	// public String vehstock_nadcon_date = "";
	public String vehstock_delstatus_id = "0", chk_delstatus_id = "0";
	public String vehstock_status_id = "0";
	public String vehstock_intransit_damage = "";
	public String rectification_date = "";
	public String vehstock_rectification_date = "";
	public String vehstock_blocked = "";
	public String vehstock_incentive = "";
	public String vehstock_notes = "";
	public String vehstock_entry_id = "0";
	public String vehstock_entry_date = "";
	public String vehstock_entry_by = "";
	public String entry_date = "";
	public String vehstock_modified_id = "0";
	public String vehstock_modified_date = "";
	public String modified_date = "";
	public String vehstock_modified_by = "";
	public String QueryString = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public DecimalFormat deci = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				// SOP("emp_role_id====" + emp_role_id);
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));

				vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_branch_id")));
				// SOP("vehstock_branch_id====" + vehstock_branch_id);
				add = PadQuotes(request.getParameter("add"));
				// SOP("add====" + add);
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());

				chk_delstatus_id = CNumeric(ExecuteQuery("SELECT vehstock_delstatus_id FROM " + compdb(comp_id) + "axela_vehstock"
						+ " WHERE vehstock_id = " + vehstock_id));
				// SOP("chk_delstatus_id===" + chk_delstatus_id);
				branch_brand_id = CNumeric(ExecuteQuery("SELECT model_brand_id "
						+ " FROM " + compdb(comp_id) + "axela_vehstock"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " WHERE vehstock_id =" + vehstock_id));
				// SOP("branch_brand_id==" + branch_brand_id);
				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						vehstock_vehstocklocation_id = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							vehstock_entry_id = emp_id;
							vehstock_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("stock-list.jsp?vehstock_id=" + vehstock_id + "&msg=Stock added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Stock")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Stock")) {
						GetValues(request, response);
						if ((ReturnPerm(comp_id, "emp_stock_edit", request).equals("1") && !chk_delstatus_id.equals("6")) || (emp_role_id.equals("1"))) {
							vehstock_modified_id = emp_id;
							vehstock_modified_date = ToLongDate(kknow());
							UpdateFields();

							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("stock-list.jsp?vehstock_id=" + vehstock_id + "&msg=Stock updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Stock")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_stock_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("stock-list.jsp?msg=Stock deleted successfully!"));
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
		vehstock_model_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_model_id")));
		vehstock_item_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_item_id")));
		vehstock_mfgyear = PadQuotes(request.getParameter("txt_vehstock_mfgyear"));
		vehstock_modelyear = PadQuotes(request.getParameter("txt_vehstock_modelyear"));
		vehstock_ref_no = PadQuotes(request.getParameter("txt_vehstock_ref_no"));
		vehstock_comm_no = PadQuotes(request.getParameter("txt_vehstock_comm_no"));
		vehstock_chassis_prefix = PadQuotes(request.getParameter("txt_vehstock_chassis_prefix"));
		vehstock_chassis_no = PadQuotes(request.getParameter("txt_vehstock_chassis_no"));
		vehstock_paintwork_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_paintwork_id")));
		vehstock_engine_no = PadQuotes(request.getParameter("txt_vehstock_engine_no"));
		vehstock_fastag = PadQuotes(request.getParameter("txt_vehstock_fastag")).toUpperCase();
		vehstock_key_no = PadQuotes(request.getParameter("txt_vehstock_key_no"));
		vehstock_vehstocklocation_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_vehstocklocation_id")));
		vehstock_parking_no = PadQuotes(request.getParameter("txt_vehstock_parking_no"));
		if (vehstock_parking_no.equals("")) {
			vehstock_parking_no = "0";
		}
		vehstock_confirmed_sgw = PadQuotes(request.getParameter("txt_vehstock_confirmed_sgw"));
		vehstock_invoice_no = PadQuotes(request.getParameter("txt_vehstock_invoice_no"));
		vehstock_invoice_date = PadQuotes(request.getParameter("txt_vehstock_invoice_date"));
		vehstock_ordered_date = PadQuotes(request.getParameter("txt_vehstock_ordered_date"));
		vehstock_invoice_amount = CNumeric(PadQuotes(request.getParameter("txt_vehstock_invoice_amount")));
		// vehvehstock_pi_date = PadQuotes(request.getParameter("txt_vehstock_pi_date"));
		vehstock_arrival_date = PadQuotes(request.getParameter("txt_vehstock_arrival_date"));
		vehstock_pdi_date = PadQuotes(request.getParameter("txt_vehstock_pdi_date"));
		vehstock_dms_date = PadQuotes(request.getParameter("txt_vehstock_dms_date"));
		vehstock_nsc = PadQuotes(request.getParameter("txt_vehstock_nsc"));
		// vehstock_finance_amt =
		// CNumeric(PadQuotes(request.getParameter("txt_vehstock_finance_amt")));
		// vehstock_retail_finance_amt =
		// CNumeric(PadQuotes(request.getParameter("txt_vehstock_retail_finance_amt")));
		// vehstock_utr_no1 = PadQuotes(request.getParameter("txt_vehstock_utr_no1"));
		// vehstock_paid_amt1 =
		// CNumeric(PadQuotes(request.getParameter("txt_vehstock_paid_amt1")));
		// vehstock_paid_date1 =
		// PadQuotes(request.getParameter("txt_vehstock_paid_date1"));
		// vehstock_utr_no2 = PadQuotes(request.getParameter("txt_vehstock_utr_no2"));
		// vehstock_paid_amt2 =
		// CNumeric(PadQuotes(request.getParameter("txt_vehstock_paid_amt2")));
		// vehstock_paid_date2 =
		// PadQuotes(request.getParameter("txt_vehstock_paid_date2"));
		// vehstock_utr_no3 = PadQuotes(request.getParameter("txt_vehstock_utr_no3"));
		// vehstock_paid_amt3 =
		// CNumeric(PadQuotes(request.getParameter("txt_vehstock_paid_amt3")));
		// vehstock_paid_date3 =
		// PadQuotes(request.getParameter("txt_vehstock_paid_date3"));
		// vehstock_nadcon_date =
		// PadQuotes(request.getParameter("txt_vehstock_nadcon_date"));
		vehstock_delstatus_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_delstatus_id")));
		vehstock_status_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_status_id")));
		vehstock_stockpriority_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_stockpriority_id")));
		vehstock_intransit_damage = PadQuotes(request.getParameter("txt_vehstock_intransit_damage"));
		vehstock_rectification_date = PadQuotes(request.getParameter("txt_vehstock_rectification_date"));
		vehstock_blocked = CheckBoxValue(PadQuotes(request.getParameter("chk_vehstock_blocked")));
		vehstock_incentive = CNumeric(PadQuotes(request.getParameter("txt_vehstock_incentive")));
		vehstock_notes = PadQuotes(request.getParameter("txt_vehstock_notes"));
		if (vehstock_notes.length() > 8000) {
			vehstock_notes = vehstock_notes.substring(0, 7999);
		}
		vehstock_entry_by = PadQuotes(request.getParameter("vehstock_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		vehstock_modified_by = PadQuotes(request.getParameter("vehstock_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		branch_brand_id = CNumeric(ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch"
				+ " WHERE branch_id = " + vehstock_branch_id + ""));
		// SOP("branch_brand_id==" + branch_brand_id);
	}

	public void CheckForm() {
		msg = "";
		try {
			if (vehstock_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}

			if (vehstock_vehstocklocation_id.equals("0")) {
				msg += "<br>Select Location!";
			}

			if (vehstock_item_id.equals("0")) {
				msg += "<br>Select Item!";
			}

			if (vehstock_modelyear.equals("")) {
				msg += "<br>Enter Model Year!";
			}
			// if (vehstock_mfgyear.equals("")) {
			// msg = msg + "<br>Enter Manufacture Year!";
			// }

			if (!vehstock_modelyear.equals("") && !vehstock_entry_date.equals("")) {
				if (Integer.parseInt(CNumeric(vehstock_modelyear)) > ((Integer.parseInt(SplitYear(vehstock_entry_date))) + 1)) {
					msg += "<br>Model Year Should be less than current Year!";
				}
			}

			// if (vehstock_ref_no.equals("")) {
			// msg = msg + "<br>Enter Reference Number!";
			// }

			if (branch_brand_id.equals("52") || branch_brand_id.equals("55") || branch_brand_id.equals("56")) {
				if (!vehstock_ref_no.equals("")) {
					StrSql = "SELECT vehstock_ref_no FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE vehstock_ref_no = '" + vehstock_ref_no + "'";

					if (update.equals("yes")) {
						StrSql = StrSql + " AND vehstock_id != " + vehstock_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Reference Number found!";
					}
					crs.close();
				}

				if (!vehstock_confirmed_sgw.equals("")) {
					// boolean tavail = isValidDateFormatShort(vehstock_confirmed_sgw);
					// SOP("vehstock_confirmed_sgw==" + vehstock_confirmed_sgw);
					if (isValidDateFormatShort(vehstock_confirmed_sgw)) {
						vehstock_confirmed_sgw = ConvertShortDateToStr(vehstock_confirmed_sgw);
						confirmed_sgw = strToShortDate(vehstock_confirmed_sgw);
					} else {
						confirmed_sgw = vehstock_confirmed_sgw;
						msg = msg + "<br>Enter valid Confirmed Date!";
					}
				}

				if (vehstock_comm_no.equals("")) {
					msg = msg + "<br>Enter Commission Number!";
				}

				if (!vehstock_comm_no.equals("")) {
					StrSql = "SELECT vehstock_comm_no FROM " + compdb(comp_id) + "axela_vehstock"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + vehstock_branch_id
							+ " WHERE vehstock_comm_no = '" + vehstock_comm_no + "'"
							+ " AND branch_brand_id = " + branch_brand_id;

					if (update.equals("yes")) {
						StrSql = StrSql + " AND vehstock_id != " + vehstock_id + "";
					}
					// SOP("StrSql==Comm==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Commission Number found!";
					}
					crs.close();
				}

				if (!vehstock_engine_no.equals("")) {
					StrSql = "SELECT vehstock_engine_no"
							+ " FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE vehstock_engine_no = '" + vehstock_engine_no + "'"
							+ " AND vehstock_id != " + vehstock_id + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Engine Number found!";
					}
				}

				if (!vehstock_chassis_no.equals("")) {
					StrSql = "SELECT vehstock_chassis_no"
							+ " FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'"
							+ " AND vehstock_id != " + vehstock_id + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Chassis Number found!";
					}
				}
			}

			if (!vehstock_fastag.equals("")) {
				StrSql = "SELECT vehstock_fastag"
						+ " FROM " + compdb(comp_id) + "axela_vehstock"
						+ " WHERE vehstock_fastag = '" + vehstock_fastag + "'"
						+ " AND vehstock_id != " + vehstock_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Similar FASTag ID found!";
				}
			}

			if (branch_brand_id.equals("2") || branch_brand_id.equals("10")) {
				if (vehstock_chassis_no.equals("")) {
					msg += "<br>Enter Chassis Number!";
				}

				if (vehstock_engine_no.equals("")) {
					msg += "<br>Enter Engine Number!";
				}

				if (!vehstock_chassis_no.equals("") && !vehstock_engine_no.equals("")) {
					StrSql = "SELECT CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) = '" + vehstock_chassis_no + "-" + vehstock_engine_no + "'"
							+ " AND vehstock_id != " + vehstock_id + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Chassis and Engine Number found!";
					}
				}
			}

			if (!branch_brand_id.equals("2") && !branch_brand_id.equals("10")
					&& !branch_brand_id.equals("52") && !branch_brand_id.equals("55") && !branch_brand_id.equals("56")) {
				if (vehstock_chassis_no.equals("")) {
					msg += "<br>Enter Chassis Number!";
				}

				if (!vehstock_engine_no.equals("")) {
					StrSql = "SELECT vehstock_engine_no"
							+ " FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE vehstock_engine_no = '" + vehstock_engine_no + "'"
							+ " AND vehstock_id != " + vehstock_id + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Engine Number found!";
					}
				}

				if (!vehstock_chassis_no.equals("")) {
					StrSql = "SELECT vehstock_chassis_no"
							+ " FROM " + compdb(comp_id) + "axela_vehstock"
							+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'"
							+ " AND vehstock_id != " + vehstock_id + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Chassis Number found!";
					}
				}

			}

			// if (vehstock_invoice_no.equals("0") || vehstock_invoice_no.equals(""))
			// {
			// msg = msg + "<br>Enter Invoice Number!";
			// }
			// if (!vehstock_status_id.equals("") && !vehstock_status_id.equals("0"))
			// {
			// if (vehstock_invoice_date.equals("")) {
			// msg = msg + "<br>Enter Invoice Date!";
			// }
			// }
			// if (vehstock_invoice_amount.equals("0") ||
			// vehstock_invoice_amount.equals("")) {
			// msg = msg + "<br>Enter Invoice Amount!";
			// }
			// }
			if (!vehstock_ordered_date.equals("")) {
				boolean valid_ordered = isValidDateFormatShort(vehstock_ordered_date);
				if (valid_ordered == true)
				{
					vehstock_ordered_date = ConvertShortDateToStr(vehstock_ordered_date);
					ordered_date = strToShortDate(vehstock_ordered_date);
				}
			}

			if (!vehstock_invoice_date.equals("")) {
				boolean tavail = isValidDateFormatShort(vehstock_invoice_date);
				if (tavail == true) {
					vehstock_invoice_date = ConvertShortDateToStr(vehstock_invoice_date);
					invoice_date = strToShortDate(vehstock_invoice_date);
				} else if (tavail == false) {
					msg = msg + "<br>Enter valid Invoice Date!";
				}
				// if
				// (isValidDateFormatShort(strToShortDate(vehstock_invoice_date)))
				// {
				// if (Long.parseLong(vehstock_invoice_date) <
				// Long.parseLong(ToLongDate(kknow()))) {
				// msg = msg + "<br>Invoice date should not be greater than " +
				// strToShortDate(ToShortDate(kknow())) + "!";
				// }
				// }
			}

			// if (!vehstock_pi_date.equals("")) {
			// boolean valid_pi = isValidDateFormatShort(vehstock_pi_date);
			// if (valid_pi == true) {
			// vehstock_pi_date = ConvertShortDateToStr(vehstock_pi_date);
			// pi_date = strToShortDate(vehstock_pi_date);
			// } else if (valid_pi == false) {
			// pi_date = strToShortDate(vehstock_pi_date);
			// msg = msg + "<br>Enter valid Pro Forma Date!";
			// }
			// }
			if (!vehstock_arrival_date.equals("")) {
				boolean valid_arrival = isValidDateFormatShort(vehstock_arrival_date);
				if (valid_arrival == true) {
					vehstock_arrival_date = ConvertShortDateToStr(vehstock_arrival_date);
					arrival_date = strToShortDate(vehstock_arrival_date);
				} else if (valid_arrival == false) {
					msg = msg + "<br>Enter valid Arrival Date!";
				}

				if (!vehstock_arrival_date.equals("") && !vehstock_invoice_date.equals("")) {
					if (isValidDateFormatShort(strToShortDate(vehstock_arrival_date)) == true && isValidDateFormatShort(strToShortDate(vehstock_invoice_date)) == true) {
						if (Long.parseLong(vehstock_invoice_date) > Long.parseLong(vehstock_arrival_date)) {
							msg = msg + "<br>Arrival Date cannot be less Invoice date!";
						}
					}
				}
			}
			if (!vehstock_pdi_date.equals("")) {
				if (isValidDateFormatShort(vehstock_pdi_date) == true) {
					// vehstock_pdi_date = ConvertShortDateToStr(vehstock_pdi_date);
				} else {
					msg = msg + "<br>Enter valid Arrival Date!";
				}
			}

			if (!vehstock_dms_date.equals("")) {
				if (isValidDateFormatShort(vehstock_dms_date) == true) {
					if (Long.parseLong(ConvertShortDateToStr(vehstock_dms_date)) <= Long.parseLong(vehstock_invoice_date)) {
						msg = msg + "<br>DMS Date Should be greater than Invoice Date!";
					}
					// vehstock_pdi_date = ConvertShortDateToStr(vehstock_pdi_date);
				} else {
					msg = msg + "<br>Enter valid DMS Date!";
				}
			}

			// if (!vehstock_paid_date1.equals("")) {
			// boolean valid_paid1 = isValidDateFormatShort(vehstock_paid_date1);
			// if (valid_paid1 == true) {
			// vehstock_paid_date1 = ConvertShortDateToStr(vehstock_paid_date1);
			// paid_date1 = strToShortDate(vehstock_paid_date1);
			// } else if (valid_paid1 == false) {
			// //paid_date1 = strToShortDate(vehstock_paid_date1);
			// msg = msg + "<br>Enter valid Paid Date1!";
			// }
			// }
			// if (!vehstock_paid_date2.equals("")) {
			// boolean valid_paid2 = isValidDateFormatShort(vehstock_paid_date2);
			// if (valid_paid2 == true) {
			// vehstock_paid_date2 = ConvertShortDateToStr(vehstock_paid_date2);
			// paid_date2 = strToShortDate(vehstock_paid_date2);
			// } else if (valid_paid2 == false) {
			// //paid_date1 = strToShortDate(vehstock_paid_date1);
			// msg += "<br>Enter valid Paid Date2!";
			// }
			// }
			//
			// if (!vehstock_paid_date3.equals("")) {
			// boolean valid_paid3 = isValidDateFormatShort(vehstock_paid_date3);
			// if (valid_paid3 == true) {
			// vehstock_paid_date3 = ConvertShortDateToStr(vehstock_paid_date3);
			// paid_date3 = strToShortDate(vehstock_paid_date3);
			// } else if (valid_paid3 == false) {
			// //paid_date1 = strToShortDate(vehstock_paid_date1);
			// msg += "<br>Enter valid Paid Date3!";
			// }
			// }
			//
			// if (!vehstock_nadcon_date.equals("")) {
			// boolean valid_nadcon = isValidDateFormatShort(vehstock_nadcon_date);
			// if (valid_nadcon == true) {
			// vehstock_nadcon_date = ConvertShortDateToStr(vehstock_nadcon_date);
			// nadcon_date = strToShortDate(vehstock_nadcon_date);
			// } else if (valid_nadcon == false) {
			// //nadcon_date = strToShortDate(vehstock_nadcon_date);
			// msg += "<br>Enter valid NADCON Sale Date!";
			// }
			// }
			if (!vehstock_rectification_date.equals("")) {
				boolean valid_rectification = isValidDateFormatShort(vehstock_rectification_date);
				if (valid_rectification == true) {
					vehstock_rectification_date = ConvertShortDateToStr(vehstock_rectification_date);
					rectification_date = strToShortDate(vehstock_rectification_date);
				} else if (valid_rectification == false) {
					msg += "<br>Enter valid Rectification Date!";
				}

				if (!vehstock_arrival_date.equals("") && !vehstock_rectification_date.equals("")) {
					if (isValidDateFormatShort(strToShortDate(vehstock_arrival_date)) && isValidDateFormatShort(strToShortDate(vehstock_rectification_date))) {
						if (Long.parseLong(vehstock_arrival_date) > Long.parseLong(vehstock_rectification_date)) {
							msg += "<br>Rectification Date cannot be less Arrival Date!";
						}
					}
				}
			}

			if (vehstock_delstatus_id.equals("0")) {
				msg = msg + "<br>Select Delivery Status!";
			}

			if (vehstock_nsc.length() > 10) {
				vehstock_nsc = vehstock_nsc.substring(0, 10);
			}
			// SOP("vehstock_nsc----" + vehstock_nsc);
			if (vehstock_status_id.equals("0")) {
				msg = msg + "<br>Select Status!";
			}
			// else if (vehstock_status_id.equals("2") &&
			// vehstock_nadcon_date.equals("")) {
			// msg += "<br>Enter NADCON Sale Date!";
			// }
			//
			// if (!vehstock_nadcon_date.equals("") && update.equals("yes")) {
			// if (isValidDateFormatShort(strToShortDate(vehstock_nadcon_date))) {
			// StrSql = "SELECT so_date FROM " + compdb(comp_id) +
			// "axela_sales_so"
			// + " WHERE so_vehstock_id = " + vehstock_id + ""
			// + " AND so_active = 1";
			// String SODate = ExecuteQuery(StrSql);
			//
			// if (!SODate.equals("")) {
			// if (isValidDateFormatShort(strToShortDate(SODate))) {
			// if (Long.parseLong(SODate) > Long.parseLong(vehstock_nadcon_date) &&
			// !vehstock_status_id.equals("2")) {
			// msg += "<br>Stock Status should be G Car!";
			// }
			// }
			// } else {
			// if (!vehstock_status_id.equals("2")) {
			// msg += "<br>Stock Status should be G Car!";
			// }
			// }
			// }
			// }
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				vehstock_id = ExecuteQuery("SELECT COALESCE(MAX(vehstock_id), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_vehstock");
				// SOP("vehstock_id-------------" + vehstock_id);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock"
						+ " (vehstock_id,"
						+ " vehstock_branch_id,"
						+ " vehstock_item_id,"
						+ " vehstock_modelyear,"
						+ " vehstock_mfgyear,"
						+ " vehstock_ref_no,"
						+ " vehstock_comm_no,"
						+ " vehstock_chassis_prefix,"
						+ " vehstock_chassis_no,"
						+ " vehstock_engine_no,"
						+ " vehstock_fastag,"
						+ " vehstock_key_no,"
						+ " vehstock_vehstocklocation_id,"
						+ " vehstock_parking_no,"
						+ " vehstock_confirmed_sgw,"
						+ " vehstock_ordered_date,"
						+ " vehstock_invoice_no,"
						+ " vehstock_invoice_date,"
						+ " vehstock_invoice_amount,"
						// + " vehstock_pi_date,"
						+ " vehstock_arrival_date,"
						+ " vehstock_pdi_date,"
						+ " vehstock_dms_date,"
						+ " vehstock_nsc,"
						// + " vehstock_finance_amt,"
						// + " vehstock_retail_finance_amt,"
						// + " vehstock_utr_no1,"
						// + " vehstock_paid_amt1,"
						// + " vehstock_paid_date1,"
						// + " vehstock_utr_no2,"
						// + " vehstock_paid_amt2,"
						// + " vehstock_paid_date2,"
						// + " vehstock_utr_no3,"
						// + " vehstock_paid_amt3,"
						// + " vehstock_paid_date3,"
						// + " vehstock_nadcon_date,"
						+ " vehstock_delstatus_id,"
						+ " vehstock_status_id,"
						+ " vehstock_intransit_damage,"
						+ " vehstock_rectification_date,"
						+ " vehstock_stockpriority_id,"
						+ " vehstock_blocked,"
						+ " vehstock_incentive,"
						+ " vehstock_notes,"
						+ " vehstock_entry_id,"
						+ " vehstock_entry_date)"
						+ " VALUES"
						+ " (" + vehstock_id + ","
						+ " " + vehstock_branch_id + ","
						+ " " + vehstock_item_id + ","
						+ " '" + vehstock_modelyear + "',"
						+ " '" + vehstock_mfgyear + "',"
						+ " '" + vehstock_ref_no + "',"
						+ " '" + vehstock_comm_no + "',"
						+ " '" + vehstock_chassis_prefix + "',"
						+ " '" + vehstock_chassis_no + "',"
						+ " '" + vehstock_engine_no + "',"
						+ " '" + vehstock_fastag + "',"
						+ " '" + vehstock_key_no + "',"
						+ " " + vehstock_vehstocklocation_id + ","
						+ " " + vehstock_parking_no + ","
						+ "'" + vehstock_confirmed_sgw + "',"
						+ "'" + vehstock_ordered_date + "',"
						+ " '" + vehstock_invoice_no + "',"
						+ " '" + vehstock_invoice_date + "',"
						+ " " + vehstock_invoice_amount + ","
						// + " '" + vehstock_pi_date + "',"
						+ " '" + vehstock_arrival_date + "',"
						+ " '" + ConvertShortDateToStr(vehstock_pdi_date) + "',"
						+ " '" + ConvertShortDateToStr(vehstock_dms_date) + "',"
						+ " '" + vehstock_nsc + "',"
						// + " " + vehstock_finance_amt + ","
						// + " " + vehstock_retail_finance_amt + ","
						// + "'" + vehstock_utr_no1 + "',"
						// + " " + vehstock_paid_amt1 + ","
						// + "'" + vehstock_paid_date1 + "',"
						// + "'" + vehstock_utr_no2 + "',"
						// + " " + vehstock_paid_amt2 + ","
						// + "'" + vehstock_paid_date2 + "',"
						// + "'" + vehstock_utr_no3 + "',"
						// + " " + vehstock_paid_amt3 + ","
						// + "'" + vehstock_paid_date3 + "',"
						// + "'" + vehstock_nadcon_date + "',"
						+ " " + vehstock_delstatus_id + ","
						+ " " + vehstock_status_id + ","
						+ " '" + vehstock_intransit_damage + "',"
						+ " '" + vehstock_rectification_date + "',"
						+ " " + vehstock_stockpriority_id + ","
						+ " " + vehstock_blocked + ","
						+ " " + vehstock_incentive + ","
						+ " '" + vehstock_notes + "',"
						+ " " + vehstock_entry_id + ","
						+ " '" + vehstock_entry_date + "')";
				// SOP("StrSql===stock=" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					vehstock_id = rs.getString(1);
				}
				rs.close();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
						+ " (trans_option_id,"
						+ " trans_vehstock_id)"
						+ " VALUES"
						+ " (" + vehstock_paintwork_id + ","
						+ " " + vehstock_id + ")";
				// SOP("StrSql======" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				try {
					conntx.setAutoCommit(true);
					stmttx.close();
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT vehstock_id, vehstock_modelyear,vehstock_mfgyear,vehstock_ref_no,vehstock_comm_no,"
					+ " vehstock_chassis_prefix, vehstock_chassis_no, vehstock_engine_no, vehstock_fastag, "
					+ " COALESCE(vehstock_confirmed_sgw, '') AS vehstock_confirmed_sgw, vehstock_key_no,"
					+ " vehstock_vehstocklocation_id, vehstock_parking_no, branch_brand_id,"
					+ " COALESCE((SELECT option_id"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND trans_vehstock_id = vehstock_id),0) AS paintwork_id,"
					+ " vehstock_item_id, item_model_id, vehstock_branch_id, vehstock_blocked, vehstock_incentive,"
					+ " vehstock_invoice_no, vehstock_ordered_date,vehstock_invoice_date, vehstock_rectification_date,"
					+ " vehstock_arrival_date, vehstock_pdi_date, vehstock_dms_date, vehstock_nsc, vehstock_invoice_amount,"
					// + " vehstock_utr_no1, vehstock_paid_amt1, vehstock_paid_date1,"
					// +
					// " vehstock_utr_no2, vehstock_paid_amt2, vehstock_paid_date2, vehstock_utr_no3, vehstock_paid_amt3,"
					// + " vehstock_paid_date3, vehstock_nadcon_date, "
					+ " vehstock_delstatus_id, vehstock_ex_price,"
					+ " vehstock_status_id, vehstock_invoiceamountaftertax,"

					+ " vehstock_stockpriority_id, "
					+ " vehstock_intransit_damage,"
					// + " vehstock_pi_date,"
					+ " vehstock_notes, vehstock_entry_id, vehstock_entry_date, vehstock_modified_id, vehstock_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE vehstock_id = " + vehstock_id + BranchAccess + "";
			// SOP("StrSql===po==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstock_branch_id = crs.getString("vehstock_branch_id");
					vehstock_item_id = crs.getString("vehstock_item_id");
					vehstock_model_id = crs.getString("item_model_id");
					vehstock_modelyear = crs.getString("vehstock_modelyear");
					// SOP("vehstock_modelyear-----" + vehstock_modelyear);
					vehstock_mfgyear = crs.getString("vehstock_mfgyear");
					vehstock_ref_no = crs.getString("vehstock_ref_no");
					vehstock_comm_no = crs.getString("vehstock_comm_no");
					vehstock_chassis_prefix = crs.getString("vehstock_chassis_prefix");
					vehstock_chassis_no = crs.getString("vehstock_chassis_no");
					vehstock_engine_no = crs.getString("vehstock_engine_no");
					vehstock_fastag = crs.getString("vehstock_fastag");
					vehstock_key_no = crs.getString("vehstock_key_no");
					vehstock_vehstocklocation_id = crs.getString("vehstock_vehstocklocation_id");
					vehstock_parking_no = crs.getString("vehstock_parking_no");
					vehstock_paintwork_id = crs.getString("paintwork_id");
					vehstock_confirmed_sgw = crs.getString("vehstock_confirmed_sgw");
					confirmed_sgw = strToShortDate(vehstock_confirmed_sgw);
					vehstock_ordered_date = crs.getString("vehstock_ordered_date");
					ordered_date = strToShortDate(vehstock_ordered_date);
					vehstock_invoice_no = crs.getString("vehstock_invoice_no");
					vehstock_invoice_date = crs.getString("vehstock_invoice_date");
					invoice_date = strToShortDate(vehstock_invoice_date);
					vehstock_invoice_amount = crs.getString("vehstock_invoice_amount");
					vehstock_invoiceamountaftertax = deci.format(crs.getDouble("vehstock_invoiceamountaftertax"));
					// vehstock_pi_date = crs.getString("vehstock_pi_date");
					// pi_date = strToShortDate(vehstock_pi_date);
					vehstock_arrival_date = crs.getString("vehstock_arrival_date");
					arrival_date = strToShortDate(vehstock_arrival_date);
					vehstock_pdi_date = strToShortDate(crs.getString("vehstock_pdi_date"));
					vehstock_dms_date = strToShortDate(crs.getString("vehstock_dms_date"));
					vehstock_nsc = crs.getString("vehstock_nsc");
					// vehstock_finance_amt = crs.getString("vehstock_finance_amt");
					// vehstock_retail_finance_amt =
					// crs.getString("vehstock_retail_finance_amt");
					// vehstock_utr_no1 = crs.getString("vehstock_utr_no1");
					// vehstock_paid_amt1 = crs.getString("vehstock_paid_amt1");
					// vehstock_paid_date1 = crs.getString("vehstock_paid_date1");
					// paid_date1 = strToShortDate(vehstock_paid_date1);
					// vehstock_utr_no2 = crs.getString("vehstock_utr_no2");
					// vehstock_paid_amt2 = crs.getString("vehstock_paid_amt2");
					// vehstock_paid_date2 = crs.getString("vehstock_paid_date2");
					// paid_date2 = strToShortDate(vehstock_paid_date2);
					// vehstock_utr_no3 = crs.getString("vehstock_utr_no3");
					// vehstock_paid_amt3 = crs.getString("vehstock_paid_amt3");
					// vehstock_paid_date3 = crs.getString("vehstock_paid_date3");
					// paid_date3 = strToShortDate(vehstock_paid_date3);
					// vehstock_nadcon_date = crs.getString("vehstock_nadcon_date");
					// nadcon_date = strToShortDate(vehstock_nadcon_date);
					vehstock_delstatus_id = crs.getString("vehstock_delstatus_id");
					vehstock_status_id = crs.getString("vehstock_status_id");
					vehstock_intransit_damage = crs.getString("vehstock_intransit_damage");
					vehstock_rectification_date = crs.getString("vehstock_rectification_date");
					rectification_date = strToShortDate(vehstock_rectification_date);
					vehstock_stockpriority_id = crs.getString("vehstock_stockpriority_id");
					vehstock_blocked = crs.getString("vehstock_blocked");
					vehstock_incentive = crs.getString("vehstock_incentive");
					vehstock_notes = crs.getString("vehstock_notes");
					branch_brand_id = crs.getString("branch_brand_id");
					vehstock_entry_id = crs.getString("vehstock_entry_id");
					if (!vehstock_entry_id.equals("")) {
						vehstock_entry_by = Exename(comp_id, Integer.parseInt(vehstock_entry_id));
					}

					entry_date = strToLongDate(crs.getString("vehstock_entry_date"));
					vehstock_modified_id = crs.getString("vehstock_modified_id");
					if (!vehstock_modified_id.equals("0")) {
						vehstock_modified_by = Exename(comp_id, Integer.parseInt(vehstock_modified_id));
						modified_date = strToLongDate(crs.getString("vehstock_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stock!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
						+ " SET"
						+ " vehstock_branch_id = " + vehstock_branch_id + ","
						+ " vehstock_item_id = " + vehstock_item_id + ","
						+ " vehstock_modelyear = '" + vehstock_modelyear + "',"
						+ " vehstock_mfgyear = '" + vehstock_mfgyear + "',"
						+ " vehstock_ref_no = '" + vehstock_ref_no + "',"
						+ " vehstock_comm_no = '" + vehstock_comm_no + "',"
						+ " vehstock_chassis_prefix = '" + vehstock_chassis_prefix + "',"
						+ " vehstock_chassis_no = '" + vehstock_chassis_no + "',"
						+ " vehstock_engine_no = '" + vehstock_engine_no + "',"
						+ " vehstock_fastag = '" + vehstock_fastag + "',"
						+ " vehstock_key_no = '" + vehstock_key_no + "',"
						+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ","
						+ " vehstock_parking_no = " + vehstock_parking_no + ","
						+ " vehstock_confirmed_sgw = '" + vehstock_confirmed_sgw + "',"
						+ " vehstock_ordered_date = '" + vehstock_ordered_date + "',"
						+ " vehstock_invoice_no = '" + vehstock_invoice_no + "',"
						+ " vehstock_invoice_date = '" + vehstock_invoice_date + "',"
						+ " vehstock_invoice_amount = " + vehstock_invoice_amount + ","
						// + " vehstock_pi_date = '" + vehstock_pi_date + "',"
						+ " vehstock_arrival_date = '" + vehstock_arrival_date + "',"
						+ " vehstock_pdi_date = '" + ConvertShortDateToStr(vehstock_pdi_date) + "',"
						+ " vehstock_dms_date = '" + ConvertShortDateToStr(vehstock_dms_date) + "',"
						+ " vehstock_nsc = '" + vehstock_nsc + "',"
						// + " vehstock_finance_amt = " + vehstock_finance_amt + ","
						// + " vehstock_retail_finance_amt = " +
						// vehstock_retail_finance_amt + ","
						// + " vehstock_utr_no1 = '" + vehstock_utr_no1 + "',"
						// + " vehstock_paid_amt1 = " + vehstock_paid_amt1 + ","
						// + " vehstock_paid_date1 = '" + vehstock_paid_date1 + "',"
						// + " vehstock_utr_no2 = '" + vehstock_utr_no2 + "',"
						// + " vehstock_paid_amt2 = " + vehstock_paid_amt2 + ","
						// + " vehstock_paid_date2 = '" + vehstock_paid_date2 + "',"
						// + " vehstock_utr_no3 = '" + vehstock_utr_no3 + "',"
						// + " vehstock_paid_amt3 = " + vehstock_paid_amt3 + ","
						// + " vehstock_paid_date3 = '" + vehstock_paid_date3 + "',"
						// + " vehstock_nadcon_date = '" + vehstock_nadcon_date + "',"
						+ " vehstock_delstatus_id = " + vehstock_delstatus_id + ","
						+ " vehstock_status_id = " + vehstock_status_id + ","
						+ " vehstock_intransit_damage = '" + vehstock_intransit_damage + "',"
						+ " vehstock_rectification_date = '" + vehstock_rectification_date + "',"
						+ " vehstock_stockpriority_id = " + vehstock_stockpriority_id + ","
						+ " vehstock_blocked = " + vehstock_blocked + ","
						+ "	vehstock_incentive = " + vehstock_incentive + ","
						+ " vehstock_notes = '" + vehstock_notes + "',"
						+ " vehstock_modified_id = " + vehstock_modified_id + ","
						+ " vehstock_modified_date = '" + vehstock_modified_date + "'"
						+ " WHERE vehstock_id = " + vehstock_id + "";
				if (!BranchAccess.equals("")) {
					// StrSql += BranchAccess.replace("branch_id",
					// "vehstock_branch_id");
				}
				// SOP("StrSql=====" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
				// delete
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
						+ " WHERE trans_vehstock_id = " + vehstock_id + "";
				stmttx.execute(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
						+ " (trans_option_id,"
						+ " trans_vehstock_id)"
						+ " VALUES"
						+ " (" + vehstock_paintwork_id + ","
						+ " " + vehstock_id + ")";
				// SOP("StrSql===option trans===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
				conntx.commit();

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				try {
					conntx.setAutoCommit(true);
					stmttx.close();
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}

	}

	protected void DeleteFields() {
		StrSql = "SELECT so_vehstock_id FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_vehstock_id = " + vehstock_id + "";
		if (ExecuteQuery(StrSql).equals(vehstock_id)) {
			msg += "<br>Stock is associated with Sales Order!";
		}

		// StrSql = "SELECT vehstockgatepass_vehstock_id FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
		// + " WHERE vehstockgatepass_vehstock_id = " + vehstock_id + "";
		// if (ExecuteQuery(StrSql).equals(vehstock_id)) {
		// msg += "<br>Stock is associated with Gate Pass!";
		// }

		if (msg.equals("")) {

			StrSql = "SELECT quote_vehstock_id FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " WHERE quote_vehstock_id = " + vehstock_id + "";

			if (ExecuteQuery(StrSql).equals(vehstock_id)) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote"
						+ " SET "
						+ " quote_vehstock_id = 0"
						+ " WHERE 1=1"
						+ " AND quote_vehstock_id = " + vehstock_id;
				ExecuteQuery(StrSql);
			}

			// Delete Gate Pass if it's associated with the Stock.
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " WHERE vehstockgatepass_vehstock_id = " + vehstock_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
					+ " WHERE trans_vehstock_id = " + vehstock_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock"
					+ " WHERE vehstock_id = " + vehstock_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateItem(String comp_id, String vehstock_model_id, String branch_id) {
		// SOP("vehstock_item_id==" + vehstock_item_id);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name, brand_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " INNER JOIN axela_brand on brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_brand_id = model_brand_id"
					+ " WHERE item_type_id = 1"
					+ " AND item_name != 'Pre Owned'"
					+ " AND item_active = 1"
					+ " AND branch_id = " + branch_id
					+ " AND model_id = " + vehstock_model_id
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql==item==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append(" <select name=\"dr_vehstock_item_id\" class=\"form-control\" id=\"dr_vehstock_item_id\" >");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), vehstock_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String comp_id, String branch_id) {
		// SOP("vehstock_item_id==" + vehstock_item_id);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name, brand_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand on brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_brand_id = model_brand_id"
					+ " WHERE 1 = 1"
					+ " AND model_active = 1"
					+ " AND branch_id = " + branch_id
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("StrSql==model==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append(" <select name=\"dr_vehstock_model_id\" class=\"form-control\" id=\"dr_vehstock_model_id\" onChange='PopulateItem();'>");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), vehstock_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDeliveryStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " ORDER BY delstatus_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("delstatus_id"));
				Str.append(StrSelectdrop(crs.getString("delstatus_id"), vehstock_delstatus_id));
				Str.append(">").append(crs.getString("delstatus_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
					+ " ORDER BY status_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("status_id"));
				Str.append(StrSelectdrop(crs.getString("status_id"), vehstock_status_id));
				Str.append(">").append(crs.getString("status_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateStockPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehstockpriority_id, vehstockpriority_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_priority"
					+ " ORDER BY vehstockpriority_rank";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstockpriority_id"));
				Str.append(StrSelectdrop(crs.getString("vehstockpriority_id"), vehstock_stockpriority_id));
				Str.append(">").append(crs.getString("vehstockpriority_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " WHERE 1=1"
					// + " vehstocklocation_branch_id = " + vehstock_branch_id + ""
					+ " ORDER BY vehstocklocation_name";

			// SOP("StrSql==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_vehstock_vehstocklocation_id\" class=\"form-control\" id=\"dr_vehstock_vehstocklocation_id\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstocklocation_id"));
				Str.append(StrSelectdrop(crs.getString("vehstocklocation_id"), vehstock_vehstocklocation_id));
				Str.append(">").append(crs.getString("vehstocklocation_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePaintWork(String comp_id, String vehstock_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, CONCAT(option_name,' (',option_code,')')as option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = option_brand_id"
					+ " WHERE 1=1"
					+ " AND option_optiontype_id = 1";
			if (!vehstock_branch_id.equals("0")) {
				StrSql += " AND branch_id = " + vehstock_branch_id;
			}
			StrSql += " GROUP BY option_name"
					+ " ORDER BY option_name";
			// SOP("StrSql == paintwork =" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_vehstock_paintwork_id\" class=\"form-control\" id=\"dr_vehstock_paintwork_id\" >");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id"));
				Str.append(StrSelectdrop(crs.getString("option_id"), vehstock_paintwork_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	// public String PopulateFinanceCompany() {
	// try {
	// StrSql = "SELECT fincomp_id, fincomp_name"
	// + " FROM " + compdb(comp_id) + "axela_finance_comp"
	// + " ORDER BY fincomp_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=\"0\"> Select </option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("fincomp_id"));
	// Str.append(StrSelectdrop(crs.getString("fincomp_id"), vehstock_fincomp_id));
	// Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }
}
