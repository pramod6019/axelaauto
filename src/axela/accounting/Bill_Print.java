package axela.accounting;

//Bhavya 19th Apr 2016

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

public class Bill_Print extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "10";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "", vouchertype_name = "";
	public List dataList = new ArrayList();
	public Map parameters = new HashMap();
	public String FollowupDetails = "";
	public String emp_id = "0";
	public String enquiry_id = "0", principal_id = "0";
	public String StrSql = "", reportfrom = "", emp_name = "", format = "pdf";
	public String msg = "";
	public String total_amt_words = "", amt_words = "", tamt_words = "", voucher_tamt_words = "";
	public String msg1 = "";
	public long voucher_amount = 0;
	public long voucher_tamount = 0;
	JasperPrint jasperPrint;
	JRGzipVirtualizer jrGzipVirtualizer = null;
	Connection conn = null;
	public String voucher_customer_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_voucher_access," + " emp_acc_purchase_order_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_id = CNumeric(GetSession("emp_id", request));
				reportfrom = PadQuotes(request.getParameter("dr_report"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				conn = connectDB();
				JasperReport report = new JasperReport();
				report.reportfrom = "/accounting/reports/" + reportfrom;
				// SOP("reportfrom===" + reportfrom);
				report.parameters.put("REPORT_CONNECTION", conn);
				report.dataList = PurchaseOrderDetails();
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
	public List<Map> PurchaseOrderDetails() throws IOException {

		HashMap dataMap = null;
		String custPh = "", branch_logo = "", branchname = "", comp_logo = "";;
		String cusMblel = "", emailID = "";

		try {
			StrSql = "SELECT voucher_date, voucher_id, voucher_so_id, voucher_amount, voucher_customer_id,"
					+ " voucher_consignee_add, voucher_billing_add, voucher_narration,"
					+ " voucher_notes, voucher_entry_id, vouchertrans_cheque_bank,"
					+ " vouchertrans_cheque_no, vouchertrans_cheque_date, vouchertype_label,"
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount,"
					+ " vouchertype_turnoverdisc, voucher_ref_no, voucher_custref_no,"
					+ " voucher_no, vouchertype_prefix, branch_bill_prefix, vouchertype_suffix,"
					+ " voucher_gatepass, voucher_lrno, voucher_cashdiscount,"
					+ " voucher_turnoverdisc, branch_add, branch_vat, branch_pin,"
					+ " branch_phone1,voucher_consignee_add, branch_phone2, branch_bill_terms, branch_mobile1, branch_mobile2, branch_brand_id,"
					+ " branch_email1, branch_email2, branch_invoice_name,voucher_gatepass, branch_gst, branch_vat, branch_cin, branch_tin, branch_pan, branch_logo, comp_logo,"
					+ " comp_name, customer_name, customer_pan_no, customer_gst_no, customer_address, customer_code,"
					+ " customer_pin, customer_name, customer_mobile1, customer_mobile2,"
					+ " customer_phone1, customer_phone2, customer_email1, customer_email2,"
					+ " COALESCE (branchcity.city_name, '') AS city_name,"
					+ " COALESCE (branchstate.state_name, '') AS state_name,"
					+ " COALESCE (customercity.city_name, '') AS cust_city,"
					+ " COALESCE ( customerstate.state_name, '' ) AS cust_state,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms,"
					+ " COALESCE((SELECT CONCAT(so_prefix, so_no) AS so_no"
					+ " FROM " + compdb(comp_id) + "axela_sales_so where so_id = voucher_so_id limit 1),'') AS  so_no,"
					+ " COALESCE((SELECT jc_no"
					+ " FROM " + compdb(comp_id) + "axela_service_jc where jc_id = voucher_jc_id limit 1),'') AS  jc_no,"
					+ " SUM(vouchertrans_qty) AS totalqty,"
					+ " comp_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = " + voucherclass_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
					+ " WHERE 1=1"
					+ "	AND voucher_id = " + voucher_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " GROUP BY voucher_id"
					+ " ORDER BY voucher_id DESC";

			// SOP("StrSql=========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				emp_name = crs.getString("emp_name").toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
				voucher_amount = (long) crs.getDouble("voucher_amount");
				total_amt_words = IndianCurrencyFormatToWord(voucher_amount);
				amt_words = toTitleCase(total_amt_words);
				dataMap = new HashMap();
				String contact = "", comp_name = "", voucher_no = "";
				comp_name += crs.getString("comp_name");
				contact += crs.getString("branch_add") + ",";
				contact += "\n" + crs.getString("city_name") + " - ";
				if (!crs.getString("branch_pin").equals("")) {
					contact += crs.getString("branch_pin") + ",";
				}
				if (!crs.getString("state_name").equals("")) {
					contact += crs.getString("state_name") + ",";
				}
				if (!crs.getString("branch_phone1").equals("")) {
					contact += "\n" + crs.getString("branch_phone1");
					if (!crs.getString("branch_phone2").equals("")) {
						contact += ", " + crs.getString("branch_phone2");
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

				if (!crs.getString("branch_mobile1").equals("")) {
					contact += "\n" + crs.getString("branch_mobile1");
					if (!crs.getString("branch_mobile2").equals("")) {
						contact += ", " + crs.getString("branch_mobile2");
					}
				}
				// SOP("contact===" + contact);

				if (vouchertype_id.equals("4") || vouchertype_id.equals("6")) {

					dataMap.put("voucher_custref_no", unescapehtml(crs.getString("voucher_custref_no")));
				}
				if (!crs.getString("customer_code").equals("")) {

					dataMap.put("customer_code", crs.getString("customer_code"));
				}
				if (!crs.getString("branch_logo").equals("")) {
					branch_logo = BranchLogoPath(comp_id) + crs.getString("branch_logo");
					dataMap.put("branch_logo", branch_logo);
				}
				if (!crs.getString("customer_name").equals("")) {

					dataMap.put("customer_name", crs.getString("customer_name"));
				}
				if (!crs.getString("voucher_billing_add").equals("")) {

					dataMap.put("voucher_billing_add", unescapehtml(crs.getString("voucher_billing_add")));
				}

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
				if (!crs.getString("totalqty").equals("")) {
					dataMap.put("totalqty", crs.getString("totalqty"));
				}
				voucher_no = crs.getString("branch_bill_prefix") + crs.getString("voucher_no");

				if (!voucher_no.equals("")) {
					voucher_no = "Bill No.: " + voucher_no;
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
				dataMap.put("branch_bill_terms", unescapehtml(crs.getString("branch_bill_terms")));

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

				if (!crs.getString("customer_gst_no").equals("")) {
					dataMap.put("customer_gst_no", crs.getString("customer_gst_no"));
				}
				if (!crs.getString("branch_pan").equals("")) {
					dataMap.put("branch_pan", crs.getString("branch_pan"));
				}
				dataMap.put("branch_invoice_name", unescapehtml(crs.getString("branch_invoice_name")));
				dataMap.put("voucher_so_id", crs.getString("voucher_so_id"));
				dataMap.put("customer_pan_no", crs.getString("customer_pan_no"));
				dataMap.put("comp_ID", Integer.parseInt(comp_id));
				dataMap.put("voucher_date", strToShortDate(crs.getString("voucher_date")));
				dataMap.put("voucher_ref_no", unescapehtml(crs.getString("voucher_ref_no")));
				dataMap.put("voucher_no", voucher_no);
				dataMap.put("voucher_id", unescapehtml(voucher_id));
				dataMap.put("contact", unescapehtml(contact));
				dataMap.put("vouchertype_label", crs.getString("vouchertype_label"));
				dataMap.put("voucher_amount", df.format(Math.ceil(crs.getDouble("voucher_amount"))));
				if (!crs.getString("voucher_amount").equals("0.00")) {
					dataMap.put("voucher_amountwords", toTitleCase(IndianCurrencyFormatToWord((int) Math.ceil(crs.getDouble("voucher_amount")))) + " Only");
				}
				dataMap.put("comp_name", unescapehtml(comp_name));
				dataMap.put("emp_name", unescapehtml(emp_name));
				dataMap.put("vouchertype_prefix", crs.getString("vouchertype_prefix"));
				dataMap.put("vouchertype_suffix", crs.getString("vouchertype_suffix"));
				dataMap.put("voucher_consignee_add", crs.getString("voucher_consignee_add"));
				dataMap.put("voucher_gatepass", crs.getString("voucher_gatepass"));
				dataMap.put("amt_words", amt_words);

				dataList.add(dataMap);
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto====" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return dataList;
	}
}
