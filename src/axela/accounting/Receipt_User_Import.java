//Shivaprasad 6/07/2015   
package axela.accounting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Receipt_User_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", errormsg = "";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, updatecount = 0;
	public String voucher_entry_id = "0";
	public String voucher_entry_date = "";
	public String BranchAccess = "";
	public String upload = "";
	public String error_msg = "";
	public String voucher_error_msg = "";
	public int receiptcolumnLength = 0;
	public int count = 0;
	HashMap<String, String> mapronum = new HashMap<String, String>();

	public String customer_name = "", customer_id = "0";
	public String contact_name = "";
	public String contact_fname = "", contact_lname = "";
	public String customer_mobile1 = "";
	public String customer_address = "";
	public String contact_state = "";
	public String customer_pan_no = "0", customer_gst_no = "";

	// Voucher Variables
	public String voucher_id = "0", voucher_amount = "", voucher_no = "", voucher_date = "", voucher_narration = "";
	public String voucher_branch_id = "0", vouchertype_name = "", voucher_ref_no = "", voucher_location_id = "0", voucher_customer_id = "0";
	public String voucher_contact_id = "0";
	// Vehicle Variables
	public String veh_chassis_no = "";

	public String hrs = "", min = "", day = "", month = "", year = "", location_name = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_acc_receipt_access", request, response);
				voucher_entry_id = emp_id;
				upload = PadQuotes(request.getParameter("add_button"));
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			savePath = VehicleImportPath(comp_id);
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
				for (int i = 0; it.hasNext() && i < 9; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
					}
				}
				Iterator iter = items.iterator();
				msg = "";
				for (int i = 0; iter.hasNext() && i < 9; i++) {
					voucher_branch_id = str1[0];
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
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf("."))
													+ " format!";
										} else {
											temp = "";
											break;
										}
									}
									msg += temp;
								}
								if (msg.equals("")) {
									if (!fileName.equals("")) {
										File uploadedFile = new File(VehicleImportPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										String fileName1 = VehicleImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0, request, response);
										// SOP("msg==" + msg);
										if (msg.equals("")) {
											msg += "<br>" + propcount + " Receipt(s) Imported successfully!";
											if (!voucher_error_msg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + voucher_error_msg;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../accounting/receipt-user-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (CNumeric(voucher_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, InvalidFormatException {
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
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is.xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}
			if (rowLength > 1000) {
				rowLength = 1000;
			}
			int h = 0;
			int j = 0;
			count = 0;
			propcount = 0;
			// updatecount = 0;
			receiptcolumnLength = 16;

			if (columnLength != receiptcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
			else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {

						// Voucher Name / Voucher type
						if (h == 0) {
							vouchertype_name = "";
							vouchertype_name = PadQuotes(sheetData[j][h]);
						}
						// Voucher No
						if (h == 1) {
							voucher_no = "";
							voucher_no = PadQuotes(sheetData[j][h]);
						}

						// Voucher Date
						if (h == 2) {
							voucher_date = "";
							voucher_date = sheetData[j][h];
							if (!voucher_date.equals("")) {
								if (voucher_date.contains("/")) {
									if (isValidDateFormatShort(voucher_date)) {
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
											error_msg += "Invalid Voucher Date! <br>";
										}
										day = "";
										month = "";
										year = "";
									}
								} else if (voucher_date.length() == 14) {
									if (isValidDateFormatStr(voucher_date)) {
										voucher_date = voucher_date + "";
									}
								} else if (voucher_date.contains(".")) {
									if (voucher_date.split("\\.").length == 3) {
										day = voucher_date.split("\\.")[0];
										if (day.length() == 1) {
											day = "0" + day;
										}
										month = voucher_date.split("\\.")[1];
										if (month.length() == 1) {
											month = "0" + month;
										}
										year = voucher_date.split("\\.")[2];
										if (year.length() == 2) {
											year = "20" + year;
										}
										if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
											voucher_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
											voucher_date = voucher_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
										} else {
											error_msg += "Invalid Voucher Date! <br>";
										}

										day = "";
										month = "";
										year = "";
									}
								} else {
									voucher_date = fmtShr3tofmtShr1(voucher_date);
									if (isValidDateFormatStr(voucher_date)) {
										voucher_date = voucher_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
									} else {
										error_msg += "Invalid Voucher Date! <br>";
									}
								}
							}
						}
						// Customer Name
						if (h == 3) {
							customer_name = "";
							customer_name = PadQuotes(sheetData[j][h]);
						}
						// Customer Id
						if (h == 4) {
							// customer_id = "";
							// customer_id = PadQuotes(sheetData[j][h]);
						}
						// Vin
						if (h == 5) {
							veh_chassis_no = "";
							veh_chassis_no = sheetData[j][h];
						}

						// Customer Address
						if (h == 6) {
							customer_address = "";
							customer_address = PadQuotes(sheetData[j][h]);

						}
						// State
						if (h == 7) {
							contact_state = "";
							contact_state = PadQuotes(sheetData[j][h]);
						}
						// Mobile Number
						if (h == 8) {
							customer_mobile1 = "";
							customer_mobile1 = PadQuotes(sheetData[j][h]);
							if (!customer_mobile1.equals("") && !customer_mobile1.equals("0")) {

								if (customer_mobile1.contains(",")) {
									customer_mobile1 = customer_mobile1.split(",")[0];
								} else if (customer_mobile1.substring(0, 3).equals("+91")) {
									customer_mobile1 = customer_mobile1.replace("+91", "");
								} else if (customer_mobile1.substring(0, 2).equals("91") && customer_mobile1.length() > 10) {
									customer_mobile1 = customer_mobile1.replaceFirst("91", "");
								}
								else if (customer_mobile1.substring(0, 1).equals("0") && customer_mobile1.length() > 10) {
									customer_mobile1 = customer_mobile1.replaceFirst("0", "");
								}
								customer_mobile1 = customer_mobile1.replaceAll("[^0-9]+", "");
								if (!customer_mobile1.contains("91-")) {
									customer_mobile1 = "91-" + customer_mobile1;
								}
								if (!IsValidMobileNo11(customer_mobile1)) {
									error_msg += "Please enter valid Mobile! <br>";
								}
								SOP("contact_mobile1==" + customer_mobile1);
							}
							else if (customer_mobile1.equals("0")) {
								error_msg += "Please enter valid Mobile! <br>";
							}
						}
						// PAN Number
						if (h == 9) {
							customer_pan_no = "";
							customer_pan_no = PadQuotes(sheetData[j][h]);
						}
						// GSTIN/UIN Number
						if (h == 10) {
							customer_gst_no = "";
							customer_gst_no = PadQuotes(sheetData[j][h]);
							if (!customer_gst_no.equals("") && !customer_gst_no.matches("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z0-9]{3}")) {
								error_msg += "Enter Valid GST No!<br>";
							}
						}
						// Amt
						if (h == 11) {
							voucher_amount = "";
							voucher_amount = PadQuotes(sheetData[j][h]);
							if (!voucher_amount.equals("") && isNumeric(voucher_amount)) {
								if (Double.parseDouble(voucher_amount) <= 0) {
									error_msg += "Amount: Must be greater than 0!<br>";
								}
							}
						}
						// Ledger Name
						if (h == 12) {
							customer_name = "";
							customer_name = PadQuotes(sheetData[j][h]);
						}
						// Ref Name.
						if (h == 13) {
							voucher_ref_no = "";
							voucher_ref_no = PadQuotes(sheetData[j][h]);
						}
						// Narration....
						if (h == 14) {
							voucher_narration = "";
							voucher_narration = PadQuotes(sheetData[j][h]);
						}
						// Location....
						if (h == 15) {
							location_name = "";
							voucher_location_id = "0";
							location_name = PadQuotes(sheetData[j][h]);
							/*
							 * if (!location_name.equals("")) { StrSql = "SELECT location_id" + " FROM " + compdb(comp_id) + "axela_inventory_location" + " WHERE location_name = '" + location_name +
							 * "'" + " ORDER BY location_name"; voucher_location_id = CNumeric(ExecuteQuery(StrSql)); }
							 */
						}
					}
					voucher_entry_date = ToLongDate(kknow());
					if (msg.equals("") && error_msg.equals("")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							// if (!insurenquiry_reg_no.equals("")) {
							StrSql = "SELECT voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
									+ " WHERE voucher_vouchertype_id = 9"
									+ " AND voucher_branch_id = " + voucher_branch_id
									+ " AND voucher_no = '" + voucher_no + "'";
							voucher_id = CNumeric(ExecuteQuery(StrSql));
							if (voucher_id.equals("0")) {
								AddReceiptDetails();
								propcount++;
							}
							conntx.commit();
						} catch (Exception e) {
							if (conntx.isClosed()) {
								SOPError("connection is closed.....");
							}
							if (!conntx.isClosed() && conntx != null) {
								conntx.rollback();
							}
							msg = "<br>Transaction Error!";
							SOPError("Axelaauto== " + this.getClass().getName());
							SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
						} finally {
							conntx.setAutoCommit(true);
							stmttx.close();
							if (conntx != null && !conntx.isClosed()) {
								conntx.close();
							}
						}
					} else if (!error_msg.equals("")) {
						if (!voucher_no.equals("")) {
							voucher_error_msg += ++count + "." + " Voucher No.===> " + voucher_no + "<br>" + error_msg;
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public void AddReceiptDetails() {
		try {
			if (!customer_mobile1.equals("")) {
				StrSql = "SELECT voucher_customer_id,voucher_contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_customer_id = customer_id "
						+ " WHERE 1 = 1 "
						+ " AND (customer_mobile1 = '" + customer_mobile1 + "'"
						+ " OR customer_mobile2 = '" + customer_mobile1 + "') ";
				// SOP("StrSql==Check Enquiry Mobile 1==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						voucher_customer_id = crs.getString("voucher_customer_id");
						voucher_contact_id = crs.getString("voucher_contact_id");
					}
				}
				crs.close();
			}
			if (voucher_customer_id.equals("0")) {
				voucher_customer_id = AddCustomer();
				voucher_contact_id = AddContact();
				AddReceipt(voucher_customer_id, voucher_contact_id);
			} else if (!voucher_customer_id.equals("0")) {
				if (voucher_contact_id.equals("0")) {
					voucher_contact_id = AddContact();
				}
				AddReceipt(voucher_customer_id, voucher_contact_id);
			}
		} catch (Exception ex) {
			try {
				if (conntx.isClosed() && conntx != null) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			} catch (Exception e) {
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			}
		}
	}
	public void AddReceipt(String voucher_customer_id, String voucher_contact_id) throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_voucher"
				+ " ("
				+ " voucher_vouchertype_id,"
				// + " voucher_tag_id,"
				// + " voucher_entry_type,"
				+ " voucher_no,"
				+ " voucher_branch_id,"
				+ " voucher_date,"
				+ " voucher_amount,"
				+ " voucher_narration,"
				+ " voucher_customer_id,"
				+ " voucher_contact_id,"
				// + " voucher_emp_id,"
				// + " voucher_invoice_id,"
				// + " voucher_so_id,"
				// + " voucher_jc_id,"
				+ " voucher_ref_no,"
				// + " voucher_driver_no,"
				// + " voucher_tempo_no,"
				// + " voucher_terms,"
				+ " voucher_active,"
				// + " voucher_inactivestatus_id,"
				// + " voucher_inactivestatus_date,"
				// + " voucher_notes,"
				+ " voucher_entry_id,"
				+ " voucher_entry_date)"
				+ " VALUES" + " ("
				+ 9 + ","// voucher_vouchertype_id
				// + " " + voucher_tag_id + ","
				// + " " + voucher_entry_type + ","
				+ " " + voucher_no + ","
				+ " " + voucher_branch_id + ","
				+ " '" + voucher_date + "',"
				+ " " + voucher_amount + ","
				+ " '" + voucher_narration + "',"
				+ " " + voucher_customer_id + ","
				+ " " + voucher_contact_id + ","
				// + " " + voucher_emp_id + ","
				// + " " + voucher_invoice_id + ","
				// + " " + voucher_so_id + ","
				// + " " + voucher_jc_id + ","
				+ " '" + voucher_ref_no + "',"
				// + " '" + voucher_driver_no + "',"
				// + " '" + voucher_tempo_no + "',"
				// + " '" + vouchertype_terms + "',"
				+ " '" + 1 + "',"// voucher_active
				// + " '" + voucher_inactivestatus_id + "',"
				// + " '" + voucher_inactivestatus_date + "',"
				// + " '" + voucher_notes + "',"
				+ " " + voucher_entry_id + ","
				+ " '" + voucher_entry_date + "')";
		// StrSql = CompDB(StrSql);
		// SOP("add fields==" + StrSqlBreaker(StrSql));
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			voucher_id = rs.getString(1);
			// vouchertrans_voucher_id = voucher_id;
		}
		rs.close();

	}

	public String AddContact() throws SQLException {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_mobile1,"
				+ " contact_address,"
				+ " contact_state,"
				+ " contact_active,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + voucher_customer_id + ","
				+ " " + 1 + ","// contact_title_id
				+ " '" + customer_name + "',"
				+ " '" + customer_mobile1 + "',"
				+ " '" + customer_address + "',"
				+ " '" + contact_state + "',"
				+ " '1'," // contact_active
				+ " " + voucher_entry_id + ","
				+ " '" + voucher_entry_date + "')";
		// //SOP("StrSql==Customer Contact==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			voucher_contact_id = rs.getString(1);
		}
		rs.close();
		return voucher_contact_id;
	}

	public String AddCustomer() throws SQLException {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_mobile1,"
				+ " customer_address,"
				+ " customer_pan_no,"
				+ " customer_gst_no,"
				+ " customer_type,"
				+ " customer_active,"
				+ " customer_entry_id,"
				+ " customer_entry_date)"
				+ " VALUES"
				+ " (" + voucher_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + customer_mobile1 + "',"
				+ " '" + customer_address + "',"
				+ " '" + customer_pan_no + "',"
				+ " '" + customer_gst_no + "',"
				+ " '1'," // customer_type
				+ " '1',"// customer_active
				+ " " + voucher_entry_id + ","
				+ " '" + voucher_entry_date + "')";
		// //SOP("StrSql==Customer==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			voucher_customer_id = rs.getString(1);
		}
		rs.close();
		return voucher_customer_id;
	}

}