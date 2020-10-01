//Aswaraj 27/04/2017   
package axela.accounting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class PurchaseInvoice_User_Import_Yamaha extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", errormsg = "", voucher_errormsg = "";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	int count = 0;
	public String branchtype = "";
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int insertinvoicecount = 0, updateitemcount = 0;
	public String BranchAccess = "";
	public String branch_id = "0", purchaserateclass_id = "0", salesrateclass_id = "0";
	public String upload = "";
	int deleteinvoicevouchertrans = 0, deletegrnvouchertrans = 0;
	String supplierentrycheck = "0";

	String voucher_id = "0", voucher_vouchertype_id = "0", voucher_date = "";
	String voucher_invoice_id = "0", voucher_grn_id = "0";
	String voucher_customer_id = "0", voucher_contact_id = "0";
	String voucher_ref_no = "", voucher_grandtotal = "";

	int vouchertrans_rowcount = 0, vouchertrans_option_id = 0;
	String vouchertrans_qty = "0";

	String item_id = "0", item_code = "", item_name = "", item_hsn = "", item_alt_uom_id = "0", item_uom_id = "1", item_type_id = "0";
	String model_id = "74", model_name = "";

	String taxid1 = "0", taxid2 = "0", taxid3 = "0";
	int taxrate1 = 0, taxrate2 = 0, taxrate3 = 0;
	String customer_code = "0", customer_gst_no = "";

	String voucherdisc = "0", vouchertax = "0";

	String inventory_location_id = "0";
	String year = "", month = "", day = "";
	String price_id = "0", price_amt = "", dealer_price = "";
	double taxamount1 = 0.0, taxamount2 = 0.0, taxamount3 = 0.0;

	public Connection conntx = null;
	public Statement stmttx = null;
	ArrayList<Integer> al = new ArrayList<Integer>();
	DecimalFormat df = new DecimalFormat("0.00");
	public String gstType = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_acc_purchase_invoice_add", request, response);
				upload = PadQuotes(request.getParameter("add_button"));
				branchtype = PadQuotes(request.getParameter("branchtype"));
				if (branchtype.equals("5")) {
					item_type_id = "2";
				}
				else if (branchtype.equals("")) {
					item_type_id = "3";
				}
				Addfile(request, response);
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			savePath = ItemImportPath(comp_id);
			docsize = 1;
			importdocformat = ".xls, .xlsx";
			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
				File f = new File(savePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				factory.setRepository(f);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax((1024 * 1024 * (int) docsize) + (1024 * 1024));
				List items = upload.parseRequest(request);
				Iterator it = items.iterator();
				for (int i = 0; it.hasNext() && i <= 9; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
					}
				}
				Iterator iter = items.iterator();
				purchaserateclass_id = str1[0];
				salesrateclass_id = str1[1];
				branch_id = str1[2];
				inventory_location_id = str1[3];
				CheckForm();
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				for (int i = 0; iter.hasNext() && i <= 9; i++) {
					if (str1[i].equals("Upload")) {
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							CheckForm();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							}
							if (!fileName.equals("")) {
								String temp = "";
								if (fileName.contains("/") || fileName.contains("\\")) {
									fileName = Filename(item.getName());
								} else {
									fileName = item.getName();
								}
								if (!fileName.equals("")) {
									docformat = importdocformat.split(", ");
									for (int j = 0; j < docformat.length; j++) {
										if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format!";
										} else {
											temp = "";
											break;
										}
									}
									msg += temp;
								}
								if (msg.equals("")) {
									if (!fileName.equals("")) {
										File uploadedFile = new File(ItemImportPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										String fileName1 = ItemImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = "<br>" + insertinvoicecount + " Purchase Invoice(s) Added successfully!<br>" + voucher_errormsg;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../accounting/invoice-user-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (CNumeric(purchaserateclass_id).equals("0")) {
			msg = msg + "<br>Select Rate Class!";
		}
		if (CNumeric(branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (CNumeric(inventory_location_id).equals("0")) {
			msg = msg + "<br>Select Location!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {

			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}
			if (rowLength > 1000) {
				rowLength = 1000;
			}
			int h = 0;
			int j = 0;
			insertinvoicecount = 0;
			updateitemcount = 0;
			if (columnLength != 34) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					CheckForm();
					errormsg = "";
					for (h = 0; h < columnLength + 1; h++) {
						// Customer Code
						if (h == 0) {
							customer_code = "0";
							customer_code = PadQuotes(sheetData[j][h]);
						}
						// Yamaha GSTN
						if (h == 1) {
							customer_gst_no = PadQuotes(sheetData[j][h]);
							voucher_customer_id = "0";
							voucher_contact_id = "0";
							if (customer_gst_no.equals("null") || customer_gst_no.equals("0") || customer_gst_no.equals("")) {
								errormsg += " Customer GST No. can not be empty!<br>";
							}
							StrSql = "SELECT customer_id, contact_id"
									+ " FROM " + compdb(comp_id) + "axela_customer"
									+ "	INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
									+ " WHERE customer_gst_no = '" + customer_gst_no + "'"
									+ " AND customer_type=2"
									+ " LIMIT 1";
							// SOP("StrSql==customer==" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							if (crs.next()) {
								voucher_customer_id = CNumeric(crs.getString("customer_id"));
								voucher_contact_id = CNumeric(crs.getString("contact_id"));
							}
							crs.close();
							if (!voucher_customer_id.equals("0")) {
								gstType = GetGstType(voucher_customer_id, branch_id, comp_id);
							}
							if (voucher_customer_id.equals("0") && voucher_contact_id.equals("0")) {
								errormsg += " Supplier not found!<br>";
							}
						}
						// Customer GSTN
						if (h == 2) {

						}
						// Invoice No.
						if (h == 3) {
							voucher_ref_no = "";
							voucher_ref_no = PadQuotes(sheetData[j][h]);
							if (voucher_ref_no.equals("null") || voucher_ref_no.equals("0") || voucher_ref_no.equals("")) {
								errormsg += " Voucher No. can not be empty!<br>";
							}
						}
						// Invoice Date
						if (h == 4) {
							voucher_date = "";
							voucher_date = sheetData[j][h];
							if (!voucher_date.equals("")) {
								if (voucher_date.contains("-")) {
									if (voucher_date.split("-").length == 3) {
										day = voucher_date.split("-")[0];
										if (day.length() == 1) {
											day = "0" + day;
										}
										month = voucher_date.split("-")[1].toUpperCase();
										Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month);
										Calendar cal = Calendar.getInstance();
										cal.setTime(date);
										month = String.valueOf(cal.get(Calendar.MONTH) + 1);
										if (month.length() == 1) {
											month = "0" + month;
										}
										year = voucher_date.split("-")[2];
										if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
											voucher_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
										} else {
											errormsg += " Invalid Invoice Date !<br>";
										}
									}
								} else if (isValidDateFormatShort(voucher_date)) {
									voucher_date = ConvertShortDateToStr(voucher_date);
								} else if (voucher_date.split("/").length == 3) {
									month = voucher_date.split("/")[0];
									if (month.length() == 1) {
										month = "0" + month;
									}
									day = voucher_date.split("/")[1];
									if (day.length() == 1) {
										day = "0" + day;
									}
									year = voucher_date.split("/")[2];
									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										voucher_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
									} else {
										errormsg += " Invalid Invoice Date !<br>";
									}
									day = "";
									month = "";
									year = "";
								} else {
									errormsg += " Invalid Invoice Date !<br>";
								}
							}
						}
						// Customer Order No
						if (h == 5) {

						}
						// Part No. Ordered
						if (h == 6) {
							item_code = "";
							item_code = PadQuotes(sheetData[j][h]);
							if (item_code.equals("null") || item_code.equals("0") || item_code.equals("")) {
								errormsg += " Item Code can not be empty!<br>";
							}
							if (!item_code.equals("")) {
								item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
										+ "	WHERE item_code = '" + item_code + "'"));
							}
						}
						// Part No. Supplied
						if (h == 7) {

						}
						// Part Description
						if (h == 8) {
							item_name = "";
							item_name = PadQuotes(sheetData[j][h]);
							if (item_name.contains("(")) {
								item_name = item_name.replace("(", "&#40;");
							}
							if (item_name.contains(")")) {
								item_name = item_name.replace(")", "&#41;");
							}
							if (item_name.equals("")) {
								errormsg += "Item Name can not be empty!<br> ";
							}
						}
						// HSN
						if (h == 9) {
							item_hsn = "";
							item_hsn = PadQuotes(sheetData[j][h]);
						}
						// Nos.
						if (h == 10) {
							vouchertrans_qty = "";
							vouchertrans_qty = CNumeric(PadQuotes(sheetData[j][h]));
							if (vouchertrans_qty.equals("0")) {
								errormsg += "Voucher Quantity can not be empty!<br>";
							}
						}
						// MRP
						if (h == 11) {
							price_amt = "";
							price_amt = CNumeric(PadQuotes(sheetData[j][h]));
							if (price_amt.equals("")) {
								errormsg += "Price can not be empty!<br>";
							}
						}
						// Unit Rate
						if (h == 12) {
							dealer_price = "";
							dealer_price = CNumeric(PadQuotes(sheetData[j][h]));
							if (dealer_price.equals("")) {
								errormsg += "Dealer Price can not be empty!<br>";
							}
						}
						// Discount
						if (h == 13) {
							voucherdisc = "";
							voucherdisc = CNumeric(PadQuotes(sheetData[j][h]));
							if (voucherdisc.equals("0")) {
								// errormsg += "Discount can not be empty!<br>";as per sujay sir said on 16/11
							} else {
								voucherdisc = String.valueOf(Double.parseDouble(voucherdisc));
							}
						}
						// Total Invoice Value
						if (h == 14) {

						}
						// Advance Document No.
						if (h == 15) {

						}
						// Unit/Unique Qty Code
						if (h == 16) {

						}
						// SGST-RATE
						if (h == 17) {
							// SGST
							taxid1 = "0";
							taxrate1 = 0;
							taxrate1 = Integer.parseInt(CNumeric(PadQuotes(sheetData[j][h])));
							if (taxrate1 != 0) {
								StrSql = "SELECT customer_id FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_rate = " + taxrate1
										+ " AND customer_tax = '1'"
										+ " AND customer_taxtype_id = 3";
								// SOP("StrSql==taxid1==" + taxid1);
								taxid1 = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
								if (taxid1.equals("0")) {
									errormsg += "Invalid SGST!<br>";
								}
							}
						}
						// SGST-Amount
						if (h == 18) {

						}
						// CGST-RATE
						if (h == 19) {
							// CGST
							taxid2 = "0";
							taxrate2 = 0;
							taxrate2 = Integer.parseInt(CNumeric(PadQuotes(sheetData[j][h])));
							if (taxrate1 != 0 && taxrate2 == 0) {
								taxrate2 = taxrate1;
							} else if (taxrate2 != 0 && taxrate1 == 0) {
								taxrate1 = taxrate2;
							}
							if (taxrate2 != 0) {
								StrSql = "SELECT customer_id FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_rate = " + taxrate2
										+ " AND customer_tax = '1'"
										+ " AND customer_taxtype_id = 4";
								// SOP("StrSql==taxid2==" + taxid2);
								taxid2 = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
								if (taxid2.equals("0")) {
									errormsg += "Invalid CGST!<br>";
								}
							}
							if (taxrate1 == 0 && taxrate2 != 0 && errormsg.equals("")) {
								taxrate1 = taxrate2;
								StrSql = "SELECT customer_id FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_rate = " + taxrate1
										+ " AND customer_tax = '1'"
										+ " AND customer_taxtype_id = 3";
								// SOP("StrSql==taxid12==" + StrSql);
								taxid1 = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
							}
						}
						// CGST-Amount
						if (h == 20) {

						}
						// IGST-RATE
						if (h == 21) {
							// IGST
							taxid3 = "0";
							taxrate3 = 0;
							taxrate3 = Integer.parseInt(CNumeric(PadQuotes(sheetData[j][h])));
							if (taxrate1 != 0 && taxrate2 != 0 && taxrate3 == 0) {
								taxrate3 = taxrate1 * 2;
							}
							if (taxrate3 != 0) {
								StrSql = "SELECT customer_id FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_rate = " + taxrate3
										+ " AND customer_tax = '1'"
										+ " AND customer_taxtype_id = 5";
								// SOP("StrSql==taxid3==" + taxid3);
								taxid3 = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
								if (taxid3.equals("0")) {
									errormsg += "Invalid IGST!<br>";
								}
								if (!taxid3.equals("0") && taxid1.equals("0") && taxid2.equals("0")) {
									taxrate1 = taxrate3 / 2;
									taxrate2 = taxrate1;
								}
							}
							if (taxrate1 == 0 && taxrate2 == 0 && taxrate3 == 0) {
								errormsg += "Tax rate can not be empty!<br>";
							}
							if (taxid3.equals("0") && !taxid1.equals("0") && !taxid2.equals("0")) {
								taxrate3 = taxrate1 * 2;
							}
						}
						// IGST-Amount
						if (h == 22) {

						}
						// GST Compensation Cess-Rate
						if (h == 23) {

						}
						// GST Compensation Cess-Amount
						if (h == 24) {

						}
						// GR No.
						if (h == 25) {

						}
						// GR Date
						if (h == 26) {

						}
						// Place Of Supply
						if (h == 27) {

						}
						// Carrier
						if (h == 28) {

						}
						// Weight
						if (h == 29) {

						}
						// Pkgs
						if (h == 30) {

						}
						// Freight
						if (h == 31) {

						}
						// Propac No.
						if (h == 32) {

						}
						// Address Of Delivery
						if (h == 33) {

						}
					}
					if (!errormsg.equals("")) {
						voucher_errormsg += ++count + ". Purchase Invoice No. " + voucher_ref_no + "===><br>" + errormsg;
					}

					if (errormsg.equals("") && !item_name.equals("")) {
						conntx = connectDB();
						conntx.setAutoCommit(false);
						stmttx = conntx.createStatement();
						if (!item_id.equals("0")) {
							StrSql = "SELECT price_amt"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
									+ " WHERE price_item_id = " + item_id
									+ " AND price_rateclass_id = " + purchaserateclass_id
									+ " ORDER BY price_effective_from DESC"
									+ " LIMIT 1";
							// SOP("StrSql===" + StrSql);
							if ((int) Double.parseDouble(dealer_price) != (int) Double.parseDouble((CNumeric(ExecuteQuery(StrSql))))) {
								AddItem("", "", "1");
								conntx.commit();
							}
						}
						if (taxrate1 != 0 || taxrate2 != 0 || taxrate3 != 0) {
							StrSql = "SELECT";
							if (taxrate1 != 0) {
								StrSql += " COALESCE(sgst.customer_id, 0) AS SGST,";
							}
							if (taxrate2 != 0) {
								StrSql += " COALESCE(cgst.customer_id, 0) AS CGST,";
							}
							if (taxrate3 != 0) {
								StrSql += " COALESCE(igst.customer_id, 0) AS IGST";
							}
							StrSql += " FROM axelaauto.axela_acc_tax_type";
							if (taxrate1 != 0) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 3"
										+ " AND customer_rate = " + taxrate1
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS sgst ON sgst.customer_taxtype_id = taxtype_id";
							}
							if (taxrate2 != 0) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 4"
										+ " AND customer_rate = " + taxrate2
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS cgst ON cgst.customer_taxtype_id = taxtype_id";
							}
							if (taxrate3 != 0) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 5"
										+ " AND customer_rate = " + taxrate3
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS igst ON igst.customer_taxtype_id = taxtype_id";
							}
							// SOP("check==" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							if (crs.isBeforeFirst()) {
								while (crs.next()) {
									if (taxrate1 != 0) {
										if (!crs.getString("SGST").equals("0")) {
											taxid1 = CNumeric(crs.getString("SGST"));
										}
									}
									if (taxrate2 != 0) {
										if (!crs.getString("CGST").equals("0")) {
											taxid2 = CNumeric(crs.getString("CGST"));
										}
									}
									if (taxrate3 != 0) {
										if (!crs.getString("IGST").equals("0")) {
											taxid3 = CNumeric(crs.getString("IGST"));
										}
									}
								}
							}
							crs.close();
						}
						if (item_id.equals("0")) {
							AddItem("1", "1", "1");
							conntx.commit();
						}
						if (!item_id.equals("0")) {
							deleteinvoicevouchertrans = 0;
							deletegrnvouchertrans = 0;
							supplierentrycheck = "0";
							voucher_grn_id = "0";
							StrSql = "SELECT voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
									+ " WHERE voucher_vouchertype_id = 21"
									+ " AND voucher_ref_no = '" + voucher_ref_no + "'";
							// SOP("StrSql==voucher==" + StrSql);
							voucher_id = CNumeric(ExecuteQuery(StrSql));
							if (voucher_id.equals("0")) {
								// Add Purchase Invoice
								vouchertrans_rowcount = 0;
								vouchertrans_option_id = 0;
								AddInvoice("21", branch_id, inventory_location_id, voucher_date,
										purchaserateclass_id, voucher_customer_id, voucher_contact_id, emp_id, voucher_ref_no);
							}
							// Add Purchase Invoice items
							if (!voucher_id.equals("0")) {
								if (!al.contains(Integer.parseInt(voucher_id))) {
									deleteinvoicevouchertrans = 1;
									deletegrnvouchertrans = 1;
								}
								vouchertrans_rowcount++;
								vouchertrans_option_id = vouchertrans_rowcount;
								AddInvoiceItems();
								conntx.commit();
								// Get Invoice Grand Total
								StrSql = "SELECT "
										+ "@totalamt := SUM(CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_amount END) AS amount, "
										+ "@totaldis := SUM(CASE WHEN vouchertrans_discount = 1 THEN vouchertrans_amount END) AS discount, "
										+ "@totaltax := SUM(CASE WHEN vouchertrans_tax = 1 THEN vouchertrans_amount END) AS tax, "
										+ "@total := (@totalamt - @totaldis) + @totaltax AS netamount "
										+ "FROM " + compdb(comp_id) + "axela_acc_voucher_trans "
										+ "WHERE vouchertrans_voucher_id = " + voucher_id;

								CachedRowSet crs = processQuery(StrSql, 0);
								while (crs.next()) {
									voucher_grandtotal = String.valueOf((crs.getDouble("amount") - crs.getDouble("discount")) + crs.getDouble("tax"));
								}
								crs.close();

								// Update Supplier entry Grand Total
								StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher_trans"
										+ " SET vouchertrans_amount = " + voucher_grandtotal + ","
										+ "	vouchertrans_netprice = " + voucher_grandtotal + ",";
								if (taxamount1 != 0.0 && taxamount2 != 0.0 && gstType.equals("state")) {
									StrSql += "	vouchertrans_taxamount = " + (taxamount1 + taxamount2) + "";
								} else if (taxamount3 != 0.0 && gstType.equals("central")) {
									StrSql += "	vouchertrans_taxamount = " + taxamount3 + "";
								}
								StrSql += " WHERE 1=1"
										+ "	AND vouchertrans_customer_id = " + voucher_customer_id
										+ " AND vouchertrans_rowcount = 0"
										+ " AND vouchertrans_option_id = 0"
										+ " AND vouchertrans_voucher_id = " + voucher_id;
								// SOP("StrSql==" + StrSql);
								stmttx.execute(StrSql);
								// Update Invoice Grand Total
								StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
										+ " SET voucher_amount = " + voucher_grandtotal
										+ " WHERE voucher_id = " + voucher_id;
								stmttx.execute(StrSql);
								conntx.commit();
								if (!al.contains(Integer.parseInt(voucher_id))) {
									al.add(Integer.parseInt(voucher_id));
									insertinvoicecount++;
								}
							}

							// Add GRN
							voucher_invoice_id = voucher_id; // Invoice ID
							StrSql = "SELECT voucher_grn_id FROM " + compdb(comp_id) + "axela_acc_voucher"
									+ " WHERE voucher_grn_id != 0"
									+ "	AND voucher_id = " + voucher_invoice_id
									+ "	AND voucher_vouchertype_id = 21";
							// SOP("StrSql==Invoice==" + StrSql);
							voucher_grn_id = CNumeric(ExecuteQuery(StrSql));

							// Add GRN, if not present.
							if (voucher_grn_id.equals("0")) {
								AddInvoice("20", branch_id, inventory_location_id, voucher_date,
										purchaserateclass_id, voucher_customer_id, voucher_contact_id, emp_id, "0");
								voucher_grn_id = voucher_id;

								StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
										+ " SET voucher_grn_id = " + voucher_grn_id
										+ " WHERE voucher_id = " + voucher_invoice_id;
								// SOP("StrSql==Invoice Update==" + StrSql);
								stmttx.execute(StrSql);
							}

							// Add GRN items if GRN Voucher is added.
							if (!voucher_grn_id.equals("0")) {
								AddGRNItems();
								conntx.commit();
								// Get Invoice Grand Total
								StrSql = "SELECT "
										+ "@totalamt := SUM(CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_amount END) AS amount, "
										+ "@totaldis := SUM(CASE WHEN vouchertrans_discount = 1 THEN vouchertrans_amount END) AS discount, "
										+ "@totaltax := SUM(CASE WHEN vouchertrans_tax = 1 THEN vouchertrans_amount END) AS tax, "
										+ "@total := (@totalamt - @totaldis) + @totaltax AS netamount "
										+ "FROM " + compdb(comp_id) + "axela_acc_voucher_trans "
										+ "WHERE vouchertrans_voucher_id = " + voucher_grn_id;
								// SOP("StrSql===" + StrSql);
								CachedRowSet crs = processQuery(StrSql, 0);
								while (crs.next()) {
									voucher_grandtotal = String.valueOf((crs.getDouble("amount") - crs.getDouble("discount")) + crs.getDouble("tax"));
								}
								crs.close();
								// // Get GRN Grand Total

								// Update Supplier entry Grand Total
								StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher_trans"
										+ " SET vouchertrans_amount = " + voucher_grandtotal + ","
										+ "	vouchertrans_netprice = " + voucher_grandtotal + ",";
								if (taxamount1 != 0.0 && taxamount2 != 0.0) {
									StrSql += "	vouchertrans_taxamount = " + (taxamount1 + taxamount2) + "";
								} else if (taxamount3 != 0.0) {
									StrSql += "	vouchertrans_taxamount = " + taxamount3 + "";
								}
								StrSql += " WHERE 1=1"
										+ "	AND vouchertrans_customer_id = " + voucher_customer_id
										+ " AND vouchertrans_rowcount = 0"
										+ " AND vouchertrans_option_id = 0"
										+ " AND vouchertrans_voucher_id = " + voucher_grn_id;

								// Update GRN Grand Total
								StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
										+ " SET voucher_amount = " + voucher_grandtotal
										+ " WHERE voucher_id = " + voucher_grn_id;
								stmttx.execute(StrSql);
								conntx.commit();
							}
						}
						if (conntx != null && !conntx.isClosed()) {
							conntx.setAutoCommit(true);
							conntx.close();
						}
					}
				}
				// Update stock
				CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", inventory_location_id, comp_id, "20", "");
				Thread thread = new Thread(calccurrentstockthread);
				thread.start();
			}
		} catch (Exception ex) {
			try {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				SOP("1236");
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.setAutoCommit(true);
					conntx.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void AddInvoice(String voucher_vouchertype_id, String voucher_branch_id, String vouchertrans_location_id,
			String voucher_date, String voucher_rateclass_id, String voucher_customer_id,
			String voucher_contact_id, String voucher_emp_id, String voucher_ref_no) throws Exception {
		try {
			voucher_contact_id = ExecuteQuery("SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact WHERE contact_customer_id = " + voucher_customer_id + " LIMIT 1");
			String voucher_no = "";
			StrSql = "SELECT COALESCE(MAX(voucher_no),0)+1"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1=1"
					+ " AND voucher_branch_id = " + voucher_branch_id
					+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id;

			voucher_no = ExecuteQuery(StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher"
					+ " ("
					+ " voucher_vouchertype_id,"
					+ " voucher_no,"
					+ " voucher_branch_id,"
					+ " voucher_location_id,"
					+ " voucher_date,"
					+ " voucher_rateclass_id,"
					+ " voucher_customer_id,"
					+ "	voucher_contact_id,"
					+ " voucher_emp_id,"
					+ " voucher_payment_date,"
					+ " voucher_open,"
					+ " voucher_ref_no,"
					+ " voucher_authorize,"
					+ "	voucher_terms,"
					+ " voucher_active,"
					+ " voucher_notes,"
					+ " voucher_entry_id,"
					+ " voucher_entry_date)"
					+ " VALUES"
					+ " (" + voucher_vouchertype_id + ","
					+ " " + voucher_no + ","
					+ " " + voucher_branch_id + ","
					+ " " + vouchertrans_location_id + ","
					+ " '" + voucher_date + "',"
					+ " " + voucher_rateclass_id + ","
					+ " " + voucher_customer_id + ","
					+ " " + voucher_contact_id + ","
					+ " " + voucher_emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'," // voucher_payment_date
					+ " '1'," // voucher_open
					+ " '" + voucher_ref_no + "',"
					+ "	''," // voucher_authorize
					+ "	''," // voucher_terms
					+ " '1'," // voucher_active
					+ " ''," // voucher_notes
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";

			// SOP("StrSQl=voucher=" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs1 = stmttx.getGeneratedKeys();
			while (rs1.next()) {
				voucher_id = rs1.getString(1);
			}
			rs1.close();

		} catch (Exception ex) {
			SOP("AddInvoice");
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddInvoiceItems() throws Exception {
		try {

			if (deleteinvoicevouchertrans == 1) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id;
				// SOP("StrSql==Delete Vouchertrans==" + StrSql);
				stmttx.addBatch(StrSql);
			}
			taxamount1 = ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty) - Double.parseDouble(voucherdisc))) * taxrate1 / 100;
			taxamount2 = ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty) - Double.parseDouble(voucherdisc))) * taxrate2 / 100;
			taxamount3 = ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty) - Double.parseDouble(voucherdisc))) * taxrate3 / 100;

			double vouchertrans_amount = Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty);

			// Main item entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					// + " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					// + " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					// + " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					// + " vouchertrans_delivery_date,"
					// + " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					// + " vouchertrans_truckspace,"
					// + " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_discountamount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_id + ","
					// + " 0," // vouchertrans_multivoucher_id
					+ " " + 5 + "," // Purchase Ledger
					+ " " + inventory_location_id + ","
					+ " " + item_id + ", "
					+ " 0, " // vouchertrans_discount
					// + " 0, " // vouchertrans_discount_perc
					+ " 0, " // vouchertrans_tax
					// + " 0, " // vouchertrans_tax_id
					+ " " + vouchertrans_rowcount + ", "
					+ " 0, " // vouchertrans_option_id
					+ " " + dealer_price + ", " // vouchertrans_price
					+ " " + vouchertrans_amount + ", " // vouchertrans_netprice
					// + " ''," // vouchertrans_delivery_date
					// + " 0," // vouchertrans_convfactor
					+ " " + vouchertrans_qty + "," // vouchertrans_qty
					// + " 0," // vouchertrans_truckspace
					// + " 0," // vouchertrans_unit_cost
					+ " " + vouchertrans_amount + ","
					+ " " + voucherdisc + "," // vouchertrans_discountamount
					+ " " + vouchertrans_qty + "," // vouchertrans_alt_qty
					+ " " + item_uom_id + "," // vouchertrans_alt_uom_id
					+ "'" + ToLongDate(kknow()) + "',"
					+ " '1'" + " )";
			// SOP("StrSql==Main item entry==== " + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			// Discount entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_id + ","
					+ " 0," // vouchertrans_multivoucher_id
					+ " " + 6 + "," // Purchase Discount Ledger
					+ " " + inventory_location_id + ","
					+ " " + item_id + ", "
					+ " 1, " // vouchertrans_discount
					+ " " + (((Double.parseDouble(voucherdisc) / Double.parseDouble(vouchertrans_qty)) * 100) / Double.parseDouble(dealer_price)) + ", " // vouchertrans_discount_perc
					+ " " + vouchertrans_rowcount + ", " //
					+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
					+ " " + (Double.parseDouble(voucherdisc) / Double.parseDouble(vouchertrans_qty)) + "," // vouchertrans_price
					+ " " + voucherdisc + ", " // vouchertrans_netprice
					+ " " + Double.parseDouble(voucherdisc) + ","
					+ "'" + ToLongDate(kknow()) + "',"
					+ " '0'" + " )";
			// SOP("StrSql==Discount entry==== " + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			// Tax entry
			if (gstType.equals("state")) {
				if (!taxid1.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_multivoucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ "	vouchertrans_price,"
							+ "	vouchertrans_amount,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES ("
							+ " " + voucher_id + ","
							+ " 0," // vouchertrans_multivoucher_id
							+ " " + taxid1 + ","
							+ " " + inventory_location_id + ","
							+ " " + item_id + ", "
							+ " 1, " // vouchertrans_tax
							+ " " + taxamount1 + ", "
							+ " " + taxamount1 + ", "
							+ " " + vouchertrans_rowcount + ", "
							+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
							+ "'" + ToLongDate(kknow()) + "',"
							+ " '1'" + " )";
					// SOP("StrSql==Tax1 entry==== " + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				}
				if (!taxid2.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_multivoucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ "	vouchertrans_price,"
							+ "	vouchertrans_amount,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES ("
							+ " " + voucher_id + ","
							+ " 0," // vouchertrans_multivoucher_id
							+ " " + taxid2 + ","
							+ " " + inventory_location_id + ","
							+ " " + item_id + ", "
							+ " 1, " // vouchertrans_tax
							+ " " + taxamount2 + ", "
							+ " " + taxamount2 + ", "
							+ " " + vouchertrans_rowcount + ", "
							+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
							+ "'" + ToLongDate(kknow()) + "',"
							+ " '1'" + " )";
					// SOP("StrSql==Tax2 entry==== " + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				}
			} else if (gstType.equals("central")) {
				if (!taxid3.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_multivoucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ "	vouchertrans_price,"
							+ "	vouchertrans_amount,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES ("
							+ " " + voucher_id + ","
							+ " 0," // vouchertrans_multivoucher_id
							+ " " + taxid3 + ","
							+ " " + inventory_location_id + ","
							+ " " + item_id + ", "
							+ " 1, " // vouchertrans_tax
							+ " " + taxamount3 + ", "
							+ " " + taxamount3 + ", "
							+ " " + vouchertrans_rowcount + ", "
							+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
							+ "'" + ToLongDate(kknow()) + "',"
							+ " '1'" + " )";
					// SOP("StrSql==Tax3 entry==== " + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
				}
			}

			// Credit the Supplier
			StrSql = "SELECT vouchertrans_voucher_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE 1=1"
					+ "	AND vouchertrans_customer_id = " + voucher_customer_id
					+ " AND vouchertrans_rowcount = 0"
					+ " AND vouchertrans_option_id = 0"
					+ " AND vouchertrans_voucher_id = " + voucher_id;
			supplierentrycheck = CNumeric(PadQuotes(ExecuteQuery(StrSql)));

			if (supplierentrycheck.equals("0")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " (vouchertrans_voucher_id,"
						+ " vouchertrans_multivoucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_location_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time,"
						+ " vouchertrans_dc)"
						+ " VALUES ("
						+ " " + voucher_id + ","
						+ " 0," // vouchertrans_multivoucher_id
						+ " " + voucher_customer_id + ","
						+ " " + inventory_location_id + ","
						+ " " + 0 + ","
						+ "'" + ToLongDate(kknow()) + "',"
						+ " '0'" + " )";
				// SOP("StrSql==Credit the Supplier==" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddGRNItems() throws Exception {
		try {

			if (deletegrnvouchertrans == 1) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_grn_id;
				// SOP("StrSql==Delete Vouchertrans==" + StrSql);
				stmttx.addBatch(StrSql);
			}
			taxamount1 = ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty) - Double.parseDouble(voucherdisc))) * taxrate1 / 100;
			taxamount2 = ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty) - Double.parseDouble(voucherdisc))) * taxrate2 / 100;
			taxamount3 = ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty) - Double.parseDouble(voucherdisc))) * taxrate3 / 100;

			double vouchertrans_amount = Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty);

			// Main item entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_delivery_date,"
					+ " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_truckspace,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_discountamount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_grn_id + ","
					+ " 0," // vouchertrans_multivoucher_id
					+ " " + 5 + "," // Purchase Ledger
					+ " " + inventory_location_id + ","
					+ " " + item_id + ", "
					+ " 0, " // vouchertrans_discount
					+ " 0, " // vouchertrans_discount_perc
					+ " 0, " // vouchertrans_tax
					+ " 0, " // vouchertrans_tax_id
					+ " " + vouchertrans_rowcount + ", "
					+ " 0, " // vouchertrans_option_id
					+ " " + dealer_price + ", " // vouchertrans_price
					+ " " + vouchertrans_amount + ", " // vouchertrans_netprice
					+ " ''," // vouchertrans_delivery_date
					+ " 0," // vouchertrans_convfactor
					+ " " + vouchertrans_qty + "," // vouchertrans_qty
					+ " 0," // vouchertrans_truckspace
					+ " 0," // vouchertrans_unit_cost
					+ " " + vouchertrans_amount + ","
					+ " " + voucherdisc + "," // vouchertrans_discountamount
					+ " " + vouchertrans_qty + "," // vouchertrans_alt_qty
					+ " " + item_uom_id + "," // vouchertrans_alt_uom_id
					+ "'" + ToLongDate(kknow()) + "',"
					+ " '1'" + " )";
			// SOP("StrSql==Main item entry==== " + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			// Discount entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_grn_id + ","
					+ " 0," // vouchertrans_multivoucher_id
					+ " " + 6 + "," // Purchase Discount Ledger
					+ " " + inventory_location_id + ","
					+ " " + item_id + ", "
					+ " 1, " // vouchertrans_discount
					+ " " + (((Double.parseDouble(voucherdisc) / Double.parseDouble(vouchertrans_qty)) * 100) / Double.parseDouble(dealer_price)) + ", " // vouchertrans_discount_perc
					+ " " + vouchertrans_rowcount + ", " //
					+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
					+ " " + (Double.parseDouble(voucherdisc) / Double.parseDouble(vouchertrans_qty)) + "," // vouchertrans_price
					+ " " + voucherdisc + ", " // vouchertrans_netprice
					+ " " + Double.parseDouble(voucherdisc) + ","
					+ "'" + ToLongDate(kknow()) + "',"
					+ " '0'" + " )";
			// SOP("StrSql==Discount entry==== " + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);

			// Tax entry
			if (gstType.equals("state")) {
				if (!taxid1.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_multivoucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ "	vouchertrans_price,"
							+ "	vouchertrans_amount,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES ("
							+ " " + voucher_grn_id + ","
							+ " 0," // vouchertrans_multivoucher_id
							+ " " + taxid1 + ","
							+ " " + inventory_location_id + ","
							+ " " + item_id + ", "
							+ " 1, " // vouchertrans_tax
							+ " " + ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty)) - Double.parseDouble(voucherdisc)) * taxrate1 / 100 + ", "
							+ " " + taxamount1 + ", "
							+ " " + vouchertrans_rowcount + ", "
							+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
							+ "'" + ToLongDate(kknow()) + "',"
							+ " '1'" + " )";
					// SOP("StrSql==Tax entry==== " + StrSqlBreaker(StrSql));

					stmttx.addBatch(StrSql);
				}
				if (!taxid2.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_multivoucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ "	vouchertrans_price,"
							+ "	vouchertrans_amount,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES ("
							+ " " + voucher_grn_id + ","
							+ " 0," // vouchertrans_multivoucher_id
							+ " " + taxid2 + ","
							+ " " + inventory_location_id + ","
							+ " " + item_id + ", "
							+ " 1, " // vouchertrans_tax
							+ " " + ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty)) - Double.parseDouble(voucherdisc)) * taxrate1 / 100 + ", "
							+ " " + taxamount1 + ", "
							+ " " + vouchertrans_rowcount + ", "
							+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
							+ "'" + ToLongDate(kknow()) + "',"
							+ " '1'" + " )";
					// SOP("StrSql==Tax entry==2== " + StrSqlBreaker(StrSql));

					stmttx.addBatch(StrSql);
				}
			} else if (gstType.equals("central")) {
				if (!taxid3.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " (vouchertrans_voucher_id,"
							+ " vouchertrans_multivoucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_location_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ "	vouchertrans_price,"
							+ "	vouchertrans_amount,"
							+ " vouchertrans_rowcount,"
							+ " vouchertrans_option_id,"
							+ " vouchertrans_time,"
							+ " vouchertrans_dc)"
							+ " VALUES ("
							+ " " + voucher_grn_id + ","
							+ " 0," // vouchertrans_multivoucher_id
							+ " " + taxid3 + ","
							+ " " + inventory_location_id + ","
							+ " " + item_id + ", "
							+ " 1, " // vouchertrans_tax
							+ " " + ((Double.parseDouble(dealer_price) * Double.parseDouble(vouchertrans_qty)) - Double.parseDouble(voucherdisc)) * taxrate3 / 100 + ", "
							+ " " + taxamount3 + ", "
							+ " " + vouchertrans_rowcount + ", "
							+ " " + vouchertrans_option_id + ", " // vouchertrans_option_id
							+ "'" + ToLongDate(kknow()) + "',"
							+ " '1'" + " )";
					// SOP("StrSql==Tax entry=3=== " + StrSqlBreaker(StrSql));

					stmttx.addBatch(StrSql);
				}
			}

			// Credit the Supplier
			StrSql = "SELECT vouchertrans_voucher_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE 1=1"
					+ "	AND vouchertrans_customer_id = " + voucher_customer_id
					+ " AND vouchertrans_rowcount = 0"
					+ " AND vouchertrans_option_id = 0"
					+ " AND vouchertrans_voucher_id = " + voucher_grn_id;
			supplierentrycheck = CNumeric(PadQuotes(ExecuteQuery(StrSql)));

			if (supplierentrycheck.equals("0")) {
				// Credit the Supplier
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " (vouchertrans_voucher_id,"
						+ " vouchertrans_multivoucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_location_id,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time,"
						+ " vouchertrans_dc)"
						+ " VALUES ("
						+ " " + voucher_grn_id + ","
						+ " 0," // vouchertrans_multivoucher_id
						+ " " + voucher_customer_id + ","
						+ " " + inventory_location_id + ","
						+ " " + item_id + ", "
						+ " " + vouchertrans_amount + ","
						+ "'" + ToLongDate(kknow()) + "',"
						+ " '0'" + " )";
				// SOP("StrSql==Credit the Supplier==" + StrSqlBreaker(StrSql));

				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddItem(String additem, String addsaleprice, String addpurchaseprice) throws Exception {
		try {
			// inserting main item
			if (additem.equals("1")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item"
						+ " ("
						+ " item_name,"
						+ " item_code,"
						+ " item_hsn,"
						+ " item_cat_id,"
						+ " item_type_id,"
						+ "	item_model_id,"
						+ " item_uom_id,"
						+ " item_alt_uom_id,"
						+ "	item_sales_ledger_id,"
						+ "	item_salesdiscount_ledger_id,"
						+ "	item_salestax1_ledger_id,"
						+ "	item_salestax2_ledger_id,"
						+ "	item_salestax3_ledger_id,"
						+ "	item_purchase_ledger_id,"
						+ "	item_purchasediscount_ledger_id,"
						+ "	item_purchasetax1_ledger_id,"
						+ "	item_purchasetax2_ledger_id,"
						+ "	item_purchasetax3_ledger_id,"
						+ " item_notes,"
						+ " item_active,"
						+ " item_entry_id,"
						+ " item_entry_date"
						+ " )"
						+ " VALUES("
						+ " '" + item_name + "',"
						+ " '" + item_code + "',"
						+ " '" + item_hsn + "',"
						+ " 11," // item_cat_id
						+ " " + item_type_id + ","// Accessories item_type_id
						+ " " + model_id + ","
						+ " 1," // item_uom_id
						+ " 1," // item_alt_uom_id
						+ " 1," // item_sales_ledger_id
						+ " 2," // item_salesdiscount_ledger_id
						+ " " + taxid1 + "," // item_salestax1_ledger_id
						+ " " + taxid2 + "," // item_salestax2_ledger_id
						+ " " + taxid3 + "," // item_salestax3_ledger_id
						+ " 5," // item_purchase_ledger_id
						+ " 6," // item_purchasediscount_ledger_id
						+ " " + taxid1 + "," // item_purchasetax1_ledger_id
						+ " " + taxid2 + "," // item_purchasetax2_ledger_id
						+ " " + taxid3 + "," // item_purchasetax3_ledger_id
						+ " '' ," // item_notes
						+ " 1 ," // item_active
						+ " " + emp_id + ","
						+ " " + ToLongDate(kknow()) + ""
						+ " )";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				// SOP("StrSql==Add Item==" + StrSql);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					item_id = rs.getString(1);
				}
				rs.close();
			}

			// Adding Sales Price
			if (addsaleprice.equals("1")) {
				StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price"
						+ " ( price_rateclass_id,"
						+ " price_item_id,"
						+ " price_amt,"
						+ " price_disc_type,"
						+ " price_effective_from,"
						+ " price_active,"
						+ " price_entry_id,"
						+ " price_entry_date)"
						+ " VALUES ("
						+ " " + salesrateclass_id + ","
						+ " " + item_id + ","
						+ " " + price_amt + ","
						+ " '1'," // price_disc_type
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '1'," + " "
						+ emp_id + ","
						+ " " + ToLongDate(kknow()) + ")";
				// SOP("strsql=======sales price========" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			// Adding Purchase Price
			if (addpurchaseprice.equals("1")) {
				StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price"
						+ " ( price_rateclass_id,"
						+ " price_item_id,"
						+ " price_amt,"
						+ " price_disc_type,"
						+ " price_effective_from,"
						+ " price_active,"
						+ " price_entry_id,"
						+ " price_entry_date)"
						+ " VALUES ("
						+ " " + purchaserateclass_id + ","
						+ " " + item_id + ","
						+ " " + dealer_price + ","
						+ " '1'," // price_disc_type
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '1'," + " "
						+ emp_id + ","
						+ " " + ToLongDate(kknow()) + ")";
				// SOP("strsql=======purchase price========" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			if (additem.equals("1")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stock "
						+ "( stock_location_id,"
						+ " stock_item_id,"
						+ " stock_unit_cost,"
						+ " stock_entry_id,"
						+ " stock_entry_date )"
						+ " ( SELECT"
						+ " location_id,"
						+ item_id + ","
						+ dealer_price + ","
						+ emp_id + ","
						+ "'" + ToLongDate(kknow()) + "'"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location)";
				// SOP("strsql=======stock========" + StrSqlBreaker(StrSql));
				stmttx.addBatch(StrSql);
			}

			stmttx.executeBatch();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				// SOP("rollback=====rollback");
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePurchaseRateclass(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name, IF(rateclass_type = 2,  'Purchase','Sales') AS rateclass_type"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1"
					+ " AND rateclass_type= 2"
					+ " ORDER BY rateclass_type, rateclass_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id"));
				Str.append(StrSelectdrop(crs.getString("rateclass_id"), purchaserateclass_id));
				Str.append(">").append(crs.getString("rateclass_name")).append(" (");
				Str.append(crs.getString("rateclass_type")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesRateclass(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name, IF(rateclass_type = 2,  'Purchase','Sales') AS rateclass_type"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1"
					+ " AND rateclass_type= 1"
					+ " ORDER BY rateclass_type, rateclass_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id"));
				Str.append(StrSelectdrop(crs.getString("rateclass_id"), salesrateclass_id));
				Str.append(">").append(crs.getString("rateclass_name")).append(" (");
				Str.append(crs.getString("rateclass_type")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLocation(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " WHERE 1=1"
					+ "	AND branch_branchtype_id = 5"
					+ " ORDER BY location_id, location_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_location_id\" name=\"dr_location_id\" class=\"form-control\" >\n");
			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"), inventory_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
