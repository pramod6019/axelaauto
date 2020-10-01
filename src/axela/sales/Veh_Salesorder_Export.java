package axela.sales;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Veh_Salesorder_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String report = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "veh-salesorder-export.jsp";
	DecimalFormat df = new DecimalFormat("0.00");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			SOP("coming..");
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				report = PadQuotes(request.getParameter("report"));
				// SOP("exportB===" + exportB);
				// SOP("report===" + report);
				if (!GetSession("sostrsql", request).equals("")) {
					StrSearch = GetSession("sostrsql", request);
					StrSearch = StrSearch.replace(" emp_id ", " e.emp_id ");
					StrSearch = StrSearch.replace(" emp_active ", " e.emp_active ");
					// SOP("StrSearch===" + StrSearch);
					if (StrSearch.contains("CONCAT(emp_name, emp_ref_no)")) {
						StrSearch = StrSearch.replace("CONCAT(emp_name, emp_ref_no)", "CONCAT(e.emp_name, e.emp_ref_no)");
					}

					// / WHEN COMING FROM MONITORING BOARD
					StrSearch = StrSearch.replace(" emp_id", " e.emp_id")
							.replace(" emp_active", " e.emp_active")
							.replace(" emp_branch_id", " e.emp_branch_id");

					StrSearch += BranchAccess + ExeAccess.replace("emp_id", "so_emp_id") + "";
				}

				if (report.equals("SalesOrderDetails") && exportB.equals("Export")) {
					SalesOrderDetails(request, response);
				} else if (report.equals("BookingDetails") && exportB.equals("Export")) {
					BookingDetails(request, response);
				}
				else if (report.equals("PendingDelivery") && exportB.equals("Export")) {
					PopulatePendingDelivery(request, response);
				} else if (report.equals("AccountingDetails")) {
					if (report.equals("AccountingDetails") && exportB.equals("Export") && ReturnPerm(comp_id, "emp_acc_receipt_edit", request).equals("1")) {
						SalesOrderDetails(request, response);
					} else {
						response.sendRedirect("../portal/error.jsp?msg=Access denied. Please contact system administrator!");
					}
				} else if (report.equals("RegistrationDetails") && exportB.equals("Export")) {
					SalesOrderDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void SalesOrderDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT COALESCE(so_id, 0) AS 'SO ID',"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS 'Branch',"
					+ " COALESCE(region_name, '') AS 'Region',"
					+ " COALESCE(CONCAT(so_prefix, so_no), '') AS 'SO NO',"
					+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.',"
					+ " IF(so_date != '', DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'SO Date',"
					+ " COALESCE(enquiry_id, 0) AS 'Enquiry ID',"
					+ " IF(enquiry_date != '',"
					+ " DATE_FORMAT(CONCAT(SUBSTR(enquiry_date,1,8),'',SUBSTR(enquiry_entry_date,9,6)),"
					+ " '%d/%m/%Y %h:%i'), '') AS 'Enquiry Date',"
					+ " COALESCE(so_quote_id, 0) AS 'Quote ID',"
					+ " IF(so_retail_date != '', DATE_FORMAT(so_retail_date, '%d/%m/%Y %h:%i'), '') AS 'Retail Date',"
					+ " IF(so_delivered_date != '', DATE_FORMAT(so_delivered_date, '%d/%m/%Y %h:%i'), '') AS 'Delivered Date',"
					+ "	COALESCE(DATEDIFF(so_date, enquiry_date),0) AS 'Booking Ageing',"
					+ " COALESCE(so_netamt, 0) AS 'SO Net Amount',"
					+ " COALESCE(so_totaltax, 0) AS 'SO Total Tax',"
					+ " COALESCE(so_grandtotal, 0) AS 'SO Grand Total',"
					+ " COALESCE(so_refno, '') AS 'SO Reference No.',"
					+ " IF(so_active = 1, 'Yes', 'No') AS 'Active',"
					+ " IF(so_promise_date != '', DATE_FORMAT(so_promise_date, '%d/%m/%Y'), '') AS 'Tentative Delivery Date',"
					+ " COALESCE(soe_name,'') AS 'Source Of Enquiry',"
					+ " COALESCE(sob_name,'') AS 'Source Of Business',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname, ' (', contact_id, ')') AS 'Contact',"
					+ " COALESCE(contact_mobile1, '') AS 'Contact Mobile1',"
					+ " COALESCE(contact_mobile2, '') AS 'Contact Mobile2',"
					+ " COALESCE(contact_email1, '') AS 'Contact Email1',"
					+ " COALESCE(contact_email2, '') AS 'Contact Email2',"
					+ " COALESCE(contact_phone1, '') AS 'Contact Phone1',"
					+ " COALESCE(contact_phone2, '') AS 'Contact Phone2',"
					+ " CONCAT(contact_address,',',state_name,',',city_name,'-',contact_pin) AS 'Contact Address',"
					+ " IF(so_auth = 1, 'Yes', 'No') AS 'SO Authentication',"
					+ " COALESCE (so_pan, '') AS 'Pan No',"
					+ " COALESCE(CONCAT(customer_name, ' (', customer_id, ')'), '') AS 'Customer',"
					+ " COALESCE(e.emp_name, '') AS 'Employee Name',";
			StrSql += " COALESCE(t.emp_name, '') AS 'Team Leader',"
					+ " COALESCE(model_name, '') AS 'Model',"
					+ " COALESCE(item_code, '') AS 'Model Code',"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS Item,"
					+ " COALESCE(so_reg_no, '') AS 'Registration No.',"
					+ " COALESCE(option_name, '') AS 'Booking Colour',"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Stock Colour',"
					+ " COALESCE(vehstock_chassis_no, '') AS 'Chassis No.',"
					+ " COALESCE(vehstock_engine_no, '') AS 'Engine No.',"
					+ " COALESCE(so_booking_amount, 0) AS 'Booking Amount',"

					+ " COALESCE (vehstock_invoice_amount, 0) AS 'Purchase Cost', "
					+ "	COALESCE ( ( SELECT vouchertrans_price"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id AND item_type_id = 1"
					+ " WHERE voucher_so_id = so_id"
					+ " AND voucher_active = '1'"
					+ " AND item_model_id != 0"
					+ " AND voucher_vouchertype_id = 6"
					+ " AND vouchertrans_rowcount != 0"
					+ " AND vouchertrans_option_id = 0 LIMIT 1 ), 0 ) AS 'Sales Price With Out Tax',"
					+ " COALESCE ((	SELECT "
					+ " SUM(receipt.voucher_amount) "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher invoice "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher receipt ON receipt.voucher_invoice_id = invoice.voucher_id "
					+ " WHERE invoice.voucher_so_id = so_id "
					+ " AND invoice.voucher_vouchertype_id = 6 "
					+ " AND invoice.voucher_authorize = '1' "
					+ " AND receipt.voucher_vouchertype_id = 9 "
					+ " AND receipt.voucher_authorize = '1'),0) AS 'Total Authorized receipt amount',"

					// + " REPLACE(CONVERT(VARCHAR(128),CAST(so_booking_amount AS MONEY), 1, '.00','')), "
					+ " COALESCE(delstatus_name, '') AS 'Delivered Status',"
					+ " COALESCE(finstatus_name, '') AS 'Finance Status',"
					+ " COALESCE(fintype_name, '') AS 'Finance Type',"
					+ " COALESCE (fincomp_name, '') AS 'Finance Company/ Finance Name',"
					+ " COALESCE (so_finance_amt, 0) AS 'Finance Amount',";

			if (report.equals("AccountingDetails")) {
				StrSql += " COALESCE(so_mga_amount, 0) AS 'Accessories Amount',"
						+ " COALESCE(so_ew_amount, 0) AS 'Extended Warrenty Amount',"
						+ " COALESCE(so_insur_amount, 0) AS 'Insurance Amount',"
						+ " COALESCE(IF(so_exchange = 1, 'Exchange' , 'Non-Exchange'), '') AS 'Exchange Type',"
						+ " COALESCE(so_exchange_amount, 0) AS 'Exchange Amount',"
						+ " COALESCE(fincomp_name, '') AS 'Finance By',"
						+ " COALESCE(so_finance_amt, 0) AS 'Finance Amount', "
						+ " COALESCE(so_refund_amount, 0) AS 'Excess Refund',"
						+ " COALESCE(saletype_name, '') AS 'Type of Sale',"
						+ " COALESCE(so_offer_consumer, '') AS 'Consumer Offers',"
						+ " COALESCE(so_offer_exchange_bonus, 0) AS 'Exchange/Loyalty Bouns',"
						+ " COALESCE(so_offer_corporate, '') AS 'Corporate/Any',"
						+ " COALESCE(so_offer_spcl_scheme, '') AS 'Special Schemes',"
						+ " COALESCE(so_finance_gross, 0) AS 'Finance Gross Payout',"
						+ " COALESCE(so_finance_net, 0) AS 'Finance Net Payout',"
						+ " COALESCE(so_mga_paid, 0) AS 'Accessories Paid',"
						+ " COALESCE(so_mga_foc_amount, 0) AS 'Accessories FOC Amount',"
						+ " COALESCE(IF(so_insur_type_id = 1, 'FOC', IF(so_insur_type_id = 2, 'PAID', '')), '') AS 'Insurance Type',"
						+ " COALESCE(so_insur_gross, 0) AS 'Insurance Gross Payout',"
						+ " COALESCE(so_insur_net, 0) AS 'Insurance Net Payout',"
						+ " COALESCE(IF(so_ew_type_id = 1, 'FOC', IF(so_ew_type_id = 2, 'PAID', '')), '') AS 'EW Type',"
						+ " COALESCE(so_ew_payout, 0) AS 'EW Payout',";

			}
			if (report.equals("RegistrationDetails")) {
				StrSql += " IF(so_reg_rc_delivery = 1, 'Home', IF(so_reg_rc_delivery = 2, 'Showroom', '')) AS 'RC Delivery',"
						+ " IF(so_reg_salesfile_received_date != '', DATE_FORMAT(so_reg_salesfile_received_date, '%d/%m/%Y'), '') AS 'Sales File Received',"
						+ " COALESCE(so_reg_reg_no_choice, '') AS 'Registration No. Choice',"
						+ " COALESCE(so_reg_rto_file, '') AS 'RTO File Handover',"
						+ " IF(so_reg_temp_reg_date != '', DATE_FORMAT(so_reg_temp_reg_date, '%d/%m/%Y'), '') AS 'Date of Temp. Registration',"
						+ " COALESCE(so_reg_temp_reg_no, '') AS 'Temp. Reg. Number', "
						+ " COALESCE(IF(so_reg_hsrp = 1, 'Yes' , 'No'), '') AS 'HSRP',"
						+ " IF(so_reg_hsrp_received_date != '', DATE_FORMAT(so_reg_hsrp_received_date, '%d/%m/%Y'), '') AS 'HSRP Received Date',"
						+ " IF(so_reg_hsrp_install_date != '', DATE_FORMAT(so_reg_hsrp_install_date, '%d/%m/%Y'), '') AS 'HSRP Installation Date',"
						+ " IF(so_reg_hsrp_fitment_place = 1, 'Showroom' , IF(so_reg_hsrp_fitment_place = 2, 'Customer Place', '')) AS 'HSRP Fitment Place',"
						+ " COALESCE(so_reg_hsrp_fitment_loc, '') AS 'HSRP Customer Location',"
						+ " IF(so_reg_perm_reg_date != '', DATE_FORMAT(so_reg_perm_reg_date, '%d/%m/%Y'), '') AS 'Date of Permanent Registration',"
						+ " COALESCE(so_reg_perm_reg_no, '') AS 'Permanent Registration Number',"
						+ " IF(so_reg_rc_received_date != '', DATE_FORMAT(so_reg_rc_received_date, '%d/%m/%Y'), '') AS 'RC Received Date',"
						+ " IF(so_reg_rc_handover_date != '', DATE_FORMAT(so_reg_rc_handover_date, '%d/%m/%Y'), '') AS 'RC Handover Date to Customer',"
						+ " IF(so_rcdel_fastfox_handover_time != '', DATE_FORMAT(so_rcdel_fastfox_handover_time, '%d/%m/%Y %h:%i:%s'), '') AS 'RC Delivery By FastFox Handover Time',"
						+ " COALESCE(so_rcdel_person_received, '') AS 'RC Delivery Received By',"
						+ " COALESCE(so_rcdel_person_contact_no, '') AS 'RC Delivery Person Contact No.',"
						+ " COALESCE(so_rcdel_person_relation, '') AS 'RC Delivery Person Relation',"
						+ " IF(so_rcdel_delivery_time != '', DATE_FORMAT(so_rcdel_delivery_time, '%d/%m/%Y %h:%i:%s'), '') AS 'RC Delivery Time',"
						+ " COALESCE(so_reg_notes, '') AS 'Registration Notes',";
			}
			StrSql += " COALESCE(fueltype_name, '') AS 'Fuel Type',"
					+ " IF(COALESCE(so_cancel_date, '') != '', DATE_FORMAT(so_cancel_date, '%d/%m/%Y'), '') AS 'Cancel Date',"
					+ " COALESCE(cancelreason_name, '') AS 'Cancel Reason',"
					+ " COALESCE(so_notes, '') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp t ON t.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id";
			// if (report.equals("BookingDetails")) {
			// StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_bank ON bank_id = so_finstatus_bank_id";
			// }
			StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_status ON finstatus_id = so_finstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_type on fintype_id= so_fintype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id";
			if (report.equals("AccountingDetails")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_saletype ON saletype_id = so_saletype_id";
			}
			StrSql += " WHERE 1 = 1" + StrSearch
					+ " GROUP BY so_id"
					+ " ORDER BY so_id DESC"
					+ " LIMIT " + exportcount + "";

			// SOP("StrSql------sodetails------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			} else if (exporttype.equals("csv")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected String BookingDetails(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		printoption = "BookingDetails";
		try {
			StrSql = "SELECT "
					// + " so_id AS 'SO ID',"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS 'Branch',"
					+ " COALESCE(CONCAT(so_prefix, so_no), '') AS 'SO NO',"
					+ " IF(so_date != '', DATE_FORMAT(so_date, '%d/%m/%Y'), '') AS 'SO Date',"
					+ " IF(so_retail_date != '', DATE_FORMAT(so_retail_date, '%d/%m/%Y %h:%i'), '') AS 'Retail Date',"
					+ " IF(so_delivered_date != '', DATE_FORMAT(so_delivered_date, '%d/%m/%Y %h:%i'), '') AS 'Delivered Date',"
					+ " COALESCE(so_netamt, '') AS 'SO Net Amount',"
					+ " IF(so_active = 1, 'Yes', 'No') AS 'Active',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname, ' (', contact_id, ')') AS 'Contact',"
					+ " COALESCE(contact_mobile1, '') AS 'Contact Mobile1',"
					+ " COALESCE(contact_mobile2, '') AS 'Contact Mobile2',"
					+ " COALESCE(contact_email1, '') AS 'Contact Email1',"
					+ " COALESCE(contact_email2, '') AS 'Contact Email2',"
					+ " COALESCE(contact_phone1, '') AS 'Contact Phone1',"
					+ " COALESCE(contact_phone2, '') AS 'Contact Phone2',"
					+ " COALESCE(CONCAT(customer_name, ' (', customer_id, ')'), '') AS 'Customer',"
					+ " COALESCE(e.emp_name, '') AS 'Employee Name',"
					+ " COALESCE(e.emp_ref_no, '') AS 'EMP REF NO',"
					+ " COALESCE(t.emp_name, '') AS 'Team Leader',"
					+ " COALESCE(t.emp_ref_no, '') AS 'EMP REF NO',"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS Item,"
					+ " COALESCE(option_name, '') AS 'Booking Colour',"
					+ " COALESCE(so_booking_amount, 0) AS 'Booking Amount',"
					+ " COALESCE(bank_name, '') AS 'Finance Bank',"
					+ " COALESCE(so_notes, '') AS 'Notes',"
					+ " IF(COALESCE(so_cancel_date, '') != '', DATE_FORMAT(so_cancel_date, '%d/%m/%Y'), '') AS 'Cancel Date',"
					+ " COALESCE(cancelreason_name, '') AS 'Cancel Reason',"
					+ " COALESCE(fueltype_name, '') AS 'Fuel Type'"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp t on t.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_bank ON bank_id = so_finstatus_bank_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
					+ " WHERE 1 = 1" + StrSearch
					+ " GROUP BY so_id"
					+ " ORDER BY so_id DESC"
					+ " LIMIT " + exportcount + "";
			SOP("StrSql-------bookingsdetails------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			} else if (exporttype.equals("csv")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	protected String PopulatePendingDelivery(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		printoption = "PopulatePendingDelivery";
		try {
			StrSql = "SELECT so_id AS 'SO ID',"
					+ " CONCAT(branch_code, so_no) AS 'Sales Order',"
					+ " DATE_FORMAT(so_date, '%d/%m/%Y') AS 'Date',"
					+ " COALESCE(DATE_FORMAT(so_promise_date, '%d/%m/%Y'), '') AS 'Tentative Delivery Date',"
					+ " COALESCE(DATE_FORMAT(so_retail_date, '%d/%m/%Y %h:%i'), '') AS 'Retail Date',"
					+ " COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y %h:%i'), '') AS 'Delivered Date',"
					// + " COALESCE(vehstock_comm_no, '') AS 'Comm. No.',"
					+ " CONCAT(customer_name, ' (', customer_id, ')') AS 'Customer',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', Contact_lname, ' (', contact_id, ')') AS 'Contact',"
					+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), '') AS 'Sales Manager',"
					+ " IF(item_code !='', CONCAT(item_name,' (', item_code, ')'), item_name) AS Item,"
					+ " COALESCE(option_name, '') AS 'Colour',"
					+ " so_notes AS 'Remarks',"
					+ " COALESCE(delstatus_name, '') AS 'Delivery Status',"
					+ " so_grandtotal AS 'SO Amount',"
					+ " IF(COALESCE(vehstock_invoice_date, '') != '', DATE_FORMAT(vehstock_invoice_date, '%d/%m/%Y'), '') AS 'Invoice Date',"
					+ " COALESCE(vehstock_engine_no, '') AS 'Engine No.',"
					+ " COALESCE(status_name, '') AS 'Status',"
					+ " COALESCE(fintype_name, '') AS 'Finance Type',"
					+ " COALESCE(finstatus_name, '') AS 'Finance Status',"
					+ " IF(COALESCE(so_cancel_date, '') != '', DATE_FORMAT(so_cancel_date, '%d/%m/%Y'), '') AS 'Cancel Date',"
					+ " COALESCE(cancelreason_name, '') AS 'Cancel Reason'"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
					+ " AND status_id != 0"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_type ON fintype_id = so_fintype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_finance_status ON finstatus_id = so_finstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
					+ " WHERE so_active = 1"
					+ " AND so_delivered_date = ''" + StrSearch + ""
					+ " GROUP BY so_id "
					+ " Having ('Receipt Amount' < so_grandtotal)"
					+ " LIMIT " + exportcount + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A2", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// protected String PopulateAccountingDetails(HttpServletRequest request, HttpServletResponse response) {
	// StringBuilder Str = new StringBuilder();
	// printoption = "PopulateAccountingDetails";
	// try {
	// StrSql = "SELECT so_id AS 'SO ID',"
	// + " CONCAT(branch_code, so_no) AS 'Sales Order',"
	// + " DATE_FORMAT(so_date, '%d/%m/%Y') AS 'Date',"
	// + " COALESCE(so_mga_amount, '') AS 'Accessories Amount',"
	// + " COALESCE(so_ew_amount, '') AS 'Extended Warrenty Amount',"
	// + " COALESCE(so_insur_amount, '') AS 'Insurance Amount',"
	// + " COALESCE(IF(so_exchange = 1, 'Exchange' , 'Non-Exchange'), '') AS 'Exchange Type',"
	// + " COALESCE(so_exchange_amount, '') AS 'Exchange Amount',"
	// + " COALESCE(fincomp_name, '') AS 'Finance By',"
	// + " COALESCE(so_finance_amt, '') AS 'Finance Amount', "
	// + " COALESCE(so_refund_amount, '') AS 'Excess Refund',"
	// + " COALESCE(saletype_name, '') AS 'Type of Sale'"
	// + " FROM " + compdb(comp_id) + "axela_sales_so"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
	// + " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
	// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_saletype ON saletype_id = so_saletype_id"
	// + " WHERE so_active = 1"
	// + " GROUP BY so_id"
	// + " LIMIT " + exportcount + "";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// if (exporttype.equals("xlsx")) {
	// ExportToXLSX exportToXLSX = new ExportToXLSX();
	// exportToXLSX.Export(response, rs, printoption);
	// } else if (exporttype.equals("html")) {
	// ExportToHTML exportToHTML = new ExportToHTML();
	// exportToHTML.Export(request, response, rs, printoption);
	// } else if (exporttype.equals("pdf")) {
	// ExportToPDF exportToPDF = new ExportToPDF();
	// exportToPDF.Export(request, response, rs, printoption, "A2");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }
	public String PopulatePrintOption() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =SalesOrderDetails").append(StrSelectdrop("Sales Order Details", printoption)).append(">Sales Order Details</option>\n");
		Str.append("<option value =BookingDetails").append(StrSelectdrop("Booking Details", printoption)).append(">Booking Details</option>\n");
		Str.append("<option value = PendingDelivery").append(StrSelectdrop("PendingDelivery", printoption)).append(">Sales Order Pending Delivery</option>\n");
		Str.append("<option value = AccountingDetails").append(StrSelectdrop("AccountingDetails", printoption)).append(">Accounting Details</option>\n");
		Str.append("<option value = RegistrationDetails").append(StrSelectdrop("RegistrationDetails", printoption)).append(">Registration Details</option>\n");
		return Str.toString();
	}
}
