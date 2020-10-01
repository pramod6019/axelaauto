package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRGzipVirtualizer;
import cloudify.connect.Connect;

public class SO_Print extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "10";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String tax_check = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "", vouchertype_name = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	public String emp_id = "0";
	public String enquiry_id = "0", total_amt = "", brand_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf", voucher_authorize = "";
	public String msg = "";
	public String msg1 = "";
	public long voucher_amount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public String gst_type = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_acc_voucher_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				// reportfrom = reportfrom.substring(0, reportfrom.length() - 1);
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				comp_id = CNumeric(GetSession("comp_id", request));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucher_authorize = CNumeric(ExecuteQuery("SELECT voucher_authorize"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE 1=1"
						+ " AND voucher_id =" + voucher_id));

				if (voucher_authorize.equals("0")) {
					msg = "msg=Access denied. Its Not Authorized!";
					response.sendRedirect("../portal/error.jsp?" + msg);
				}

				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;
				// SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = SalesOrderDetails();
				report.doPost(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (Exception e) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
		}

	}
	public List<Map> SalesOrderDetails() throws IOException {

		HashMap dataMap;
		String custPh = "", branch_logo = "", branchname = "", comp_logo = "";
		String cusMblel = "", emailID = "";
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT voucher_so_id, voucher_jc_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_id = " + voucher_id;
			CachedRowSet crs1 = processQuery(StrSql, 0);
			while (crs1.next()) {
				StrSql = "SELECT"
						+ " COALESCE(dtvouchertrans.itemcount,'0') AS itemcount,"
						+ " COALESCE(dtvouchertrans.labourcount,'0') AS labourcount,"
						+ " voucher_date, voucher_id, voucher_so_id, voucher_jc_id, branch_invoice_terms, branch_bill_terms,"
						+ " branch_logo, branch_brand_id, voucher_branch_id, comp_logo, voucher_amount,"
						+ " voucher_customer_id, voucher_consignee_add, voucher_billing_add,"
						+ " voucher_narration, voucher_notes, voucher_entry_id,"
						+ " IF(COALESCE (billcmp.fincomp_name, '') != '', COALESCE (billcmp.fincomp_name, ''),'Other Bank') AS bank_name,"
						+ " COALESCE(main.vouchertrans_cheque_bank,'') AS vouchertrans_cheque_bank,"
						+ " COALESCE(main.vouchertrans_cheque_no,'') AS vouchertrans_cheque_no,"
						+ " COALESCE(main.vouchertrans_cheque_date,'') AS vouchertrans_cheque_date,"
						+ " COALESCE(main.vouchertrans_paymode_id,'0') AS vouchertrans_paymode_id,"
						+ " COALESCE(vouchertrans_bank_id,'0') AS vouchertrans_bank_id,"
						+ " COALESCE(vouchertrans_transaction_no,'') AS vouchertrans_transaction_no,"
						+ " COALESCE(paymode_name,'') AS paymode_name,"
						+ " vouchertype_label,"
						+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount,"
						+ " vouchertype_turnoverdisc, voucher_ref_no, voucher_custref_no,"
						+ " SUM(vouchertrans_qty) AS totalqty,";
				if (vouchertype_id.equals("6")) {
					StrSql += " CONCAT( branch_invoice_prefix, voucher_no, branch_invoice_suffix ) AS voucher_no,";
				} else if (vouchertype_id.equals("7")) {
					StrSql += " CONCAT( branch_bill_prefix, voucher_no ) AS voucher_no,";
				}
				StrSql += " COALESCE((SELECT CONCAT(so_prefix, so_no) AS so_no"
						+ " FROM " + compdb(comp_id) + "axela_sales_so where so_id = voucher_so_id limit 1),'') AS  so_no,";
				if (!crs1.getString("voucher_so_id").equals("0")) {
					StrSql += " COALESCE(vehstock_chassis_no,'') AS vehstock_chassis_no,"
							+ " COALESCE(vehstock_engine_no,'') AS vehstock_engine_no,"
							+ " COALESCE(so_reg_no,'') AS so_reg_no,"
							+ " COALESCE(option_name,'') AS option_name,"
							+ " COALESCE(socomp.fincomp_name,'') AS fincomp_name,"
							+ " COALESCE (model_name, '') AS model_name,";
				} else if (!crs1.getString("voucher_jc_id").equals("0")) {
					StrSql += " COALESCE(jc_no,'') AS jc_no,"
							+ " COALESCE(veh_reg_no,'') AS veh_reg_no,"
							+ "	COALESCE(veh_chassis_no,'') AS veh_chassis_no,"
							+ "	COALESCE(veh_engine_no,'') AS veh_engine_no,"
							+ " COALESCE (jctype_name, '') AS jctype_name,"
							+ " COALESCE (preownedmodel_name, '') AS model_name,";
				}

				StrSql += " voucher_gatepass, voucher_lrno, voucher_cashdiscount,"
						+ " voucher_turnoverdisc, branch_add, branch_vat,branch_pan, branch_tin, branch_gst, branch_cin, branch_cst, branch_pin,"
						+ " branch_phone1,voucher_consignee_add, branch_phone2, branch_mobile1, branch_mobile2,"
						+ " branch_email1, branch_email2, branch_invoice_name, comp_logo,"
						+ " comp_name, customer_name, customer_pan_no, customer_address, customer_code, customer_gst_no,"
						+ " customer_pin, customer_name, customer_mobile1, customer_mobile2,"
						+ " customer_phone1, customer_phone2, customer_email1, customer_email2,"
						+ " CONCAT( contact_fname, ' ', contact_lname ) AS contact_name,"
						+ " title_desc, COALESCE (branchcity.city_name, '') AS city_name,"
						+ " COALESCE (location_name, '') AS location_name,"
						+ " COALESCE (branchstate.state_name, '') AS state_name,"
						+ " COALESCE (customercity.city_name, '') AS cust_city,"
						+ " COALESCE ( customerstate.state_name, '' ) AS cust_state,"
						+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms,"
						+ " comp_name"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " LEFT JOIN ("
						+ " SELECT"
						+ " vouchertrans_voucher_id,"
						+ " SUM(IF(item_type_id != 4, 1, 0)) AS itemcount,"
						+ " SUM(IF(item_type_id = 4, 1, 0)) AS labourcount"
						+ " FROM"
						+ " " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
						+ " WHERE vouchertrans_rowcount != 0"
						+ " AND vouchertrans_option_id = 0"
						+ " GROUP BY vouchertrans_voucher_id"
						+ " ) AS dtvouchertrans ON dtvouchertrans.vouchertrans_voucher_id = voucher_id";

				if (!crs1.getString("voucher_so_id").equals("0")) {
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = voucher_so_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp socomp ON socomp.fincomp_id = so_fincomp_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
				} else if (!crs1.getString("voucher_jc_id").equals("0")) {
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = voucher_jc_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
							+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
							+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id";
				}
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
						+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = " + voucherclass_id
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans main ON main.vouchertrans_voucher_id = voucher_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp billcmp  ON billcmp.fincomp_id = vouchertrans_bank_id";
				if (vouchertype_id.equals("7")) {
					StrSql += " INNER JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id";
				} else if (vouchertype_id.equals("6")) {
					StrSql += " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id";
				}
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
						+ " WHERE 1 = 1"
						+ " AND voucher_id = " + voucher_id
						+ " AND vouchertype_id = " + vouchertype_id
						+ " GROUP BY voucher_id"
						+ " ORDER BY voucher_id DESC";
				// SOP("StrSql======jgjh===" + StrSql);
				crs = processQuery(StrSql, 0);
				while (crs.next()) {

					emp_name = crs.getString("emp_name").toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
					dataMap = new HashMap();
					voucher_amount = (long) crs.getDouble("voucher_amount");
					// total_amt = IndianCurrencyFormatToWord(voucher_amount).toUpperCase();
					String contact = "", comp_name = "", voucher_no = "", voucher_details = "", voucher_refno = "";
					comp_name += crs.getString("comp_name");
					contact += crs.getString("branch_add") + ", ";
					contact += "" + crs.getString("city_name") + " - ";
					contact += crs.getString("branch_pin") + ",\n";
					contact += crs.getString("state_name") + ",\n";
					// + rs.getString("branch_country") + ".";
					if (!crs.getString("branch_phone1").equals("")) {
						contact += " " + crs.getString("branch_phone1");
						if (!crs.getString("branch_phone2").equals("")) {
							contact += ", " + crs.getString("branch_phone2");
						}
					}
					if (!crs.getString("branch_mobile1").equals("")) {
						contact += ", " + crs.getString("branch_mobile1");
						if (!crs.getString("branch_mobile2").equals("")) {
							contact += ", " + crs.getString("branch_mobile2");
						}
					}
					if (!crs.getString("branch_email1").equals("")) {
						contact += ", " + crs.getString("branch_email1");
						if (!crs.getString("branch_email2").equals("")) {
							contact += ", " + crs.getString("branch_email2");
						}
					}
					// voucher_no = "Sales Invoice No.: " + crs.getString("voucher_no") + "   ";
					voucher_refno = crs.getString("voucher_ref_no");

					if (!crs.getString("voucher_no").equals("")) {
						if (vouchertype_id.equals("6")) {
							voucher_no += "" + "Invoice No.: " + crs.getString("voucher_no") + "";
						} else if (vouchertype_id.equals("7")) {
							voucher_no += "" + "Bill No.: " + crs.getString("voucher_no") + "";
						}

					}

					if (!crs.getString("voucher_jc_id").equals("0")) {
						if (!crs.getString("jc_no").equals("0"))
							voucher_no += "\n" + "Job Card No.: " + crs.getString("jc_no");
						if (!crs.getString("veh_reg_no").equals(""))
							voucher_details += "\n" + "Registration No.: " + crs.getString("veh_reg_no");
						if (!crs.getString("veh_chassis_no").equals(""))
							voucher_details += "\n" + "Chassis No.: " + crs.getString("veh_chassis_no");
						if (!crs.getString("veh_engine_no").equals(""))
							voucher_details += "\n" + "Engine No.: " + crs.getString("veh_engine_no");
						if (!crs.getString("jctype_name").equals(""))
							voucher_details += "\n" + "Service Type: " + crs.getString("jctype_name");
						if (!crs.getString("model_name").equals(""))
							voucher_details += "\n" + "Model: " + crs.getString("model_name");

					} else if (!crs.getString("voucher_so_id").equals("0")) {
						dataMap.put("so_hypothecation", crs.getString("fincomp_name"));
						if (!crs.getString("so_no").equals(""))
							voucher_no += "\n" + "Sales Order No.: " + crs.getString("so_no");
						if (!crs.getString("so_reg_no").equals(""))
							voucher_details += "\n" + "Registration No.: " + crs.getString("so_reg_no");
						if (!crs.getString("vehstock_chassis_no").equals(""))
							voucher_details += "\n" + "Chassis No.: " + crs.getString("vehstock_chassis_no");
						if (!crs.getString("vehstock_engine_no").equals(""))
							voucher_details += "\n" + "Engine No.: " + crs.getString("vehstock_engine_no");
						if (!crs.getString("option_name").equals(""))
							voucher_details += "\n" + "Colour: " + crs.getString("option_name");
						if (!crs.getString("model_name").equals(""))
							voucher_details += "\n" + "Model: " + crs.getString("model_name");
					}
					if (!crs.getString("branch_logo").equals("")) {
						branch_logo = BranchLogoPath(comp_id) + crs.getString("branch_logo");
						dataMap.put("branch_logo", branch_logo);
					}
					if (!crs.getString("branch_brand_id").equals("7") &&
							!crs.getString("branch_brand_id").equals("6") &&
							!crs.getString("branch_brand_id").equals("51")) {
						if (!crs.getString("comp_logo").equals("")) {
							comp_logo = CompLogoPath() + crs.getString("comp_logo");
							dataMap.put("comp_logo", comp_logo);
						}
					}
					dataMap.put("branch_brand_id", crs.getString("branch_brand_id"));

					// else if (!voucher_refno.equals("")) {
					// voucher_no += "Ref.No.:" + voucher_refno + "\n ";
					// }

					if (!crs.getString("branch_invoice_name").equals("")) {
						dataMap.put("branch_invoice", crs.getString("branch_invoice_name"));
					}

					if (crs.getString("branch_brand_id").equals("7")) {
						dataMap.put("branch_invoice_name", "For Kerala Cars Pvt. Ltd.");
						dataMap.put("branch_brand_name", "Kerala Cars Pvt. Ltd.");
						dataMap.put("branch_invoice", "Kerala Cars Pvt. Ltd.");
					} else if (crs.getString("branch_brand_id").equals("102")) {
						dataMap.put("branch_invoice_name", "Indel Automotives Kochi Pvt. Ltd.");
						dataMap.put("branch_brand_name", "Indel Automotives Kochi Pvt. Ltd.");
					} else if (crs.getString("branch_brand_id").equals("6")) {
						dataMap.put("branch_invoice_name", "MGF Motors Ltd.");
						dataMap.put("branch_brand_name", "HMIL");

					} else if (crs.getString("branch_brand_id").equals("101")) {
						dataMap.put("branch_invoice_name", "Indel Automotives Pvt. Ltd.");
						dataMap.put("branch_brand_name", "Indel Automotives Pvt. Ltd.");
					} else {
						if (!crs.getString("branch_invoice_name").equals("")) {
							dataMap.put("branch_invoice_name", unescapehtml(crs.getString("branch_invoice_name")));
							dataMap.put("branch_brand_name", unescapehtml(crs.getString("branch_invoice_name")));
						}
					}
					String branch_info = "";
					if (!crs.getString("branch_gst").equals("")) {
						if (!branch_info.equals("")) {
							branch_info += "\n";
						}
						branch_info += "GSTIN: " + crs.getString("branch_gst");
					}

					if (!crs.getString("branch_cin").equals("")) {
						if (!branch_info.equals("")) {
							branch_info += "\n";
						}
						branch_info += "CIN: " + crs.getString("branch_cin");
					}

					if (!crs.getString("branch_pan").equals("")) {
						if (!branch_info.equals("")) {
							branch_info += "\n";
						}
						branch_info += "PAN: " + crs.getString("branch_pan");

					}

					if (!crs.getString("branch_vat").equals("")) {
						if (!branch_info.equals("")) {
							branch_info += "\n";
						}
						branch_info += "VAT: " + crs.getString("branch_vat");
					}
					if (!crs.getString("branch_tin").equals("")) {
						if (!branch_info.equals("")) {
							branch_info += "\n";
						}
						branch_info += "TIN: " + crs.getString("branch_tin");
					}

					dataMap.put("branch_info", branch_info);

					if (!crs.getString("customer_gst_no").equals("")) {
						dataMap.put("customer_gst_no", crs.getString("customer_gst_no"));
					}

					if (!crs.getString("totalqty").equals("")) {
						dataMap.put("totalqty", crs.getString("totalqty"));
					}
					if (!crs.getString("customer_code").equals("")) {
						dataMap.put("customer_code", crs.getString("customer_code"));
					}
					if (!crs.getString("customer_name").equals("")) {
						dataMap.put("customer_name", unescapehtml(crs.getString("customer_name")));
					}
					if (!crs.getString("voucher_billing_add").equals("")) {
						dataMap.put("voucher_billing_add", unescapehtml(crs.getString("voucher_billing_add") + "\n" + voucher_details));
					}
					gst_type = GetGstType(crs.getString("voucher_customer_id"), crs.getString("voucher_branch_id"), comp_id);
					// SOP("gst_type==========" + gst_type);
					dataMap.put("gst_type", gst_type);
					dataMap.put("itemcount", unescapehtml(crs.getString("itemcount")));
					dataMap.put("labourcount", unescapehtml(crs.getString("labourcount")));
					// SOP("itemcount======" + crs.getString("itemcount"));
					// SOP("labourcount======" + crs.getString("labourcount"));
					dataMap.put("totalitemcount", (crs.getDouble("labourcount") + crs.getDouble("itemcount")) + "");

					// Mobile
					if (!crs.getString("customer_mobile1").equals("")) {
						cusMblel = "Mobile No.: " + crs.getString("customer_mobile1");
						if (!crs.getString("customer_mobile2").equals("")) {
							cusMblel += ", " + crs.getString("customer_mobile2");
						}
						dataMap.put("cusMblel", cusMblel);
					}
					// Phone
					if (!crs.getString("customer_phone1").equals("")) {
						custPh = "Phone No.: " + crs.getString("customer_phone1");
						if (!crs.getString("customer_phone2").equals("")) {
							custPh += ", " + crs.getString("customer_mobile2");
						}
						dataMap.put("custPh", custPh);
					}
					// Email
					if (!crs.getString("customer_email1").equals("")) {
						emailID = "Email ID: " + crs.getString("customer_email1");
						if (!crs.getString("customer_email2").equals("")) {
							emailID += ", " + crs.getString("customer_email2");
						}
						dataMap.put("emailID", emailID);
					}

					dataMap.put("voucher_so_id", crs.getString("voucher_so_id"));
					dataMap.put("voucher_jc_id", crs.getString("voucher_jc_id"));
					dataMap.put("customer_pan_no", crs.getString("customer_pan_no"));
					dataMap.put("comp_ID", Integer.parseInt(comp_id));
					dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
					dataMap.put("voucher_id", unescapehtml(voucher_id));

					if (!crs.getString("voucher_amount").equals("0.00")) {
						dataMap.put("voucher_amountwords", toTitleCase(IndianCurrencyFormatToWord((int) Math.ceil(crs.getDouble("voucher_amount")))) + " Only");
					}

					dataMap.put("contact", unescapehtml(contact));
					dataMap.put("voucher_no", unescapehtml(voucher_no));
					// dataMap.put("voucher_narration", crs.getString("voucher_narration"));

					tax_check = PadQuotes(ExecuteQuery("SELECT count(customer_name)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_tax_id"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id
							+ " AND vouchertrans_tax_id != 0"
							+ " AND vouchertrans_item_id = 0"
							+ " GROUP BY customer_id"));
					dataMap.put("tax_check", tax_check);
					dataMap.put("branch_gst", crs.getString("branch_gst"));
					dataMap.put("branch_cst", crs.getString("branch_cst"));
					dataMap.put("vouchertype_label", crs.getString("vouchertype_label"));
					if (vouchertype_id.equals("6")) {
						dataMap.put("vouchertype_name", "TAX INVOICE");
						dataMap.put("voucher_amount", df.format(crs.getDouble("voucher_amount")));
						dataMap.put("branch_invoice_terms", unescapehtml(crs.getString("branch_invoice_terms")));
					} else if (vouchertype_id.equals("7")) {
						dataMap.put("branch_invoice_terms", unescapehtml(crs.getString("branch_bill_terms")));
						dataMap.put("voucher_amount", df.format(crs.getDouble("voucher_amount")));
						msg1 = "Received a sum of Rs. " + (int) crs.getDouble("voucher_amount") + "/- (Rupees "
								+ toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount")))
								+ " Only/-) by ";

						msg1 += crs.getString("paymode_name");
						if (crs.getString("vouchertrans_paymode_id").equals("2")) {
							msg1 += " vide cheque number " + crs.getString("vouchertrans_cheque_no")
									+ " dated " + strToShortDate(crs.getString("vouchertrans_cheque_date"))
									+ " drawn on " + crs.getString("bank_name") + "";
						} else if (crs.getString("vouchertrans_paymode_id").equals("3")) {
							msg1 += " number " + crs.getString("vouchertrans_cheque_no") + " of "
									+ crs.getString("bank_name") + "";

							if (!crs.getString("vouchertrans_transaction_no").equals("")) {
								msg1 += " Transaction No.: " + crs.getString("vouchertrans_transaction_no");
							}
						} else if (!crs.getString("vouchertrans_paymode_id").equals("1")) {

							msg1 += " through " + crs.getString("bank_name") + "";

							if (!crs.getString("vouchertrans_transaction_no").equals("")) {
								msg1 += " Transaction No.: " + crs.getString("vouchertrans_transaction_no");
							}
						}

						dataMap.put("msg1", msg1);
						dataMap.put("vouchertype_name", "Bill");
					}

					dataMap.put("comp_name", unescapehtml(comp_name));
					dataMap.put("emp_name", unescapehtml(emp_name));
					dataMap.put("location_name", crs.getString("location_name"));
					String invoice_terms = PadQuotes(ExecuteQuery("SELECT COALESCE (config_invoice_terms, '') AS config_invoice_terms FROM " + compdb(comp_id) + "axela_config"));
					dataMap.put("invoice_terms", unescapehtml(invoice_terms));

					// dataMap.put("total_amt", total_amt);
					dataMap.put("voucher_consignee_add", crs.getString("voucher_consignee_add"));
					dataMap.put("voucher_lrno", crs.getString("voucher_lrno"));

					dataList.add(dataMap);
				}
			}

			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return dataList;
	}
}
