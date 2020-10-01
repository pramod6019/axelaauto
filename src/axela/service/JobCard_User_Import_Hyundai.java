//Shivaprasad 6/07/2015   
package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class JobCard_User_Import_Hyundai extends Connect {

	public String StrSql = "";
	public String msg = "", emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String fileName = "";
	public int propcount = 0, updatecount = 0;
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", brand_id = "6";
	public String branch_name = "", format_hyundai = "";
	public String upload = "";
	public int count = 0, ronumcount = 1, jctransdelete = 0, vouchertransdelete = 0;
	public int labourcolumnLength = 0, partscolumnLength = 0, techniciancolumnLength = 0;
	public String jc_ro_no = "", error_msg = "";
	public String jc_error_msg = "";
	HashMap<String, String> mapronum = new HashMap<String, String>();

	// Customer And Contact Variables
	public String customer_id = "0";
	public String customer_name = "", caruser_name = "";
	public String contact_id = "";
	public String contact_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_phone1 = "", contact_phone2 = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String contact_email1 = "", contact_email2 = "";
	public String contact_address = "", contact_city = "";
	public String contact_city_id = "0", contact_pin = "", contact_state = "";
	public String contact_title = "0";
	public String contact_dob = "", contact_anniversary = "";
	public String contact_title_id = "1";

	// Stock Variables
	public String vehstock_id = "0";
	public String option_id = "0";
	public String so_id = "0", voucher_id = "0";
	double sell = 0.00;
	// Vehicle Variables
	public String veh_id = "0";
	public String interior = "", exterior = "";
	public String item_id = "0", item_name = "", model_brand_id = "0";
	public String model_name = "", item_service_code = "";
	public String veh_engine_no = "", veh_reg_no = "";
	public String veh_modelyear = "";
	public String veh_sale_date = "", veh_insur_date = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0";
	public String soe_name = "", sob_name = "", soe_id = "0", sob_id = "0";
	public String veh_model_id = "0", veh_variant_id = "0", veh_fueltype_id = "0", veh_loanfinancer = "", veh_status_id = "0", veh_status_date = "";
	public String veh_soe_id = "0", veh_buyertype_id = "0", veh_desc = "";
	public String veh_notes = "", veh_priorityveh_id = "0", veh_vehsource_id = "0", vehsource_name = "";

	// Jobcard Variables
	public String jc_id = "0", jc_branch_id = "0", jc_time_in = "0";
	public String jc_reg_no = "0", jc_jctype_id = "0", jc_jccat_id = "0";
	public String jc_kms = "0", jc_bill_cash_labour_discamt = "0", jc_grandtotal = "0", jc_bill_cash_parts_valueadd = "0", jc_chassis_no = "";
	public String jc_notes = "", jc_jctype_name = "", jc_jccat_name = "";
	public String jc_advice = "";
	public String jc_time_promised = "", jc_time_ready = "", jc_time_out = "";
	public String jc_emp_id = "0", jc_emp_name = "";
	String _check = "";
	public String jc_bill_cash_no = "", jc_bill_cash_date = "";
	public String jc_technician_emp_id = "0", jc_technician_emp_name = "", jc_bill_cash_labour = "";
	public String jc_bill_cash_parts = "0", jc_bill_cash_parts_accessories = "0", jc_bill_cash_parts_tyre = "0", jc_bill_cash_parts_oil = "0";
	public String hrs = "", min = "", day = "", month = "", year = "", servicedueyear = "", veh_service_duekms = "0", veh_service_duedate = "";
	public String checkregno = "0";

	// parts variable
	public String jc_variant_id = "0", billquantity = "0", item_code = "", item_hsn = "", price_id = "0";
	public String jc_sell_price = "", billcat_id = "0", billcat_name = "", billcat_billtype = "";
	public String jc_sell_cost = "", jc_netprice = "";
	public String actual_amount = "", jc_tax_total = "", jc_discount = "", batchdt = "", batchcd = "";
	public String tax1 = "", tax2 = "", tax3 = "", tax4 = "", tax5 = "", tax6 = "", tax7 = "", tax8 = "";
	String sgsttaxamt = "", cgsttaxamt = "", igsttaxamt = "", cesstaxamt = "";
	String item_salestax1_ledger_id = "0", item_salestax2_ledger_id = "0", item_salestax3_ledger_id = "0", item_salestax4_ledger_id = "0";
	double totalamount = 0.0, quantity = 0.0;
	public String jctrans_rowcount = "0", jctrans_option_id = "0";
	public DecimalFormat df = new DecimalFormat("0.00");
	public DecimalFormat df1 = new DecimalFormat("0.0000");
	public String veh_chassis_no = "";
	String jctrans_location_id = "0", vouchertrans_location_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	JobCard_User_Import obj = new JobCard_User_Import();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				jc_entry_id = emp_id;
				jc_entry_date = ToLongDate(kknow());
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
					jc_branch_id = str1[0];
					format_hyundai = str1[1];
					// SOP("jc_branch_id===" + jc_branch_id);
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
											msg += "<br>" + propcount + " Jobcard(s) Imported successfully!";
											if (obj.jcpresent != 0) {
												msg += "<br>" + obj.jcpresent + " JobCard(s) already present!";
											}
											if (!jc_error_msg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + jc_error_msg;
											}
											if (format_hyundai.equals("Parts Format")) {
												// SOP("jctrans_location_id=====" + jctrans_location_id);
												// CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", jctrans_location_id, comp_id, "0", "yes");
												// Thread thread = new Thread(calccurrentstockthread);
												// thread.start();
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
			response.sendRedirect("../service/jobcard-user-import-hyundai.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		// SOP(("CheckForm==");
		msg = "";
		if (CNumeric(jc_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (format_hyundai.equals("")) {
			msg = msg + "<br>Select Format!";
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
			if (rowLength > 2000) {
				rowLength = 2000;
			}
			int h = 0;
			int j = 0;
			obj.propcount = 0;
			propcount = 0;
			labourcolumnLength = 29;
			partscolumnLength = 33;
			obj.brand_id = brand_id;
			StrSql = "SELECT location_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id;
			jctrans_location_id = CNumeric(ExecuteQuery(StrSql));
			// SOP("columnLength====" + columnLength);
			// techniciancolumnLength = 3;
			if (format_hyundai.equals("Labour Format") && columnLength != labourcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else if (format_hyundai.equals("Parts Format") && columnLength != partscolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
			// else if (format_hyundai.equals("Technician Format") && columnLength != techniciancolumnLength) {
			// msg += "<br>" + "Document columns doesn't match with the template!";
			// }
			else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {
						if (format_hyundai.equals("Labour Format")) {
							// Branch Id
							// if (h == 1) {
							// jc_branch_id = "";
							// jc_branch_id = PadQuotes(sheetData[j][h]);
							// if (jc_branch_id.equals("null") || jc_branch_id.equals("0") || jc_branch_id.equals("Workshop Code")) {
							// jc_branch_id = "";
							// // error_msg += "<br> Branch Name should not to be empty  !";
							// }
							// if (!jc_branch_id.equals("")) {
							// StrSql = "SELECT branch_id"
							// + " FROM " + compdb(comp_id) + "axela_branch"
							// + " WHERE branch_code = '" + jc_branch_id + "'";
							// jc_branch_id = CNumeric(ExecuteQuery(StrSql));
							// }
							// // SOP("jc_branch_id====" + jc_branch_id);
							// }

							// Bill No
							if (h == 1) {
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
								if (jc_bill_cash_no.equals("null") || jc_bill_cash_no.equals("0") || jc_bill_cash_no.equals("Bill No")) {
									jc_bill_cash_no = "";
									// error_msg += "<br> Bill No should not to be empty  !";
								}
							}

							// Bill Date
							if (h == 2) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = PadQuotes(sheetData[j][h]);

							}

							// Bill Type
							if (h == 3) {
							}

							// Customer Name....
							if (h == 4) {
								customer_name = "";
								customer_name = PadQuotes(sheetData[j][h]);
								if (customer_name.equals("null") || customer_name.equals("0") || customer_name.equals("Customer Name")) {
									customer_name = "";
									// error_msg += "<br> customer_name is not to be empty  !";
								}
							}

							// contact_mobile
							if (h == 5) {
								contact_mobile1 = "";
								contact_mobile1 = PadQuotes(sheetData[j][h]);

							}

							// Chassis Number....
							if (h == 6) {
								jc_chassis_no = "";
								jc_chassis_no = PadQuotes(sheetData[j][h]);
							}

							// Reg Number....
							if (h == 7) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
							}

							// Model Name....
							if (h == 8) {
								item_name = "";
								if (item_name.contains("'")) {
									item_name = item_name.replace("'", "");
								}
								item_name = PadQuotes(sheetData[j][h]);

							}

							// Ref num(R/o num)
							if (h == 9) {
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("R/O No")) {
									jc_ro_no = "";
									// error_msg += " Ro. No. should not be  Empty!<br>";
								}
							}

							// R/o Date
							if (h == 10) {
								jc_time_in = "";
								jc_time_in = PadQuotes(sheetData[j][h]);

							}

							// Service Advisor....
							if (h == 11) {
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
									jc_emp_name = "";
									// error_msg += "<br> Service Advisor should not to be empty  !";
								}
							}

							// Technician....
							if (h == 12) {
								jc_technician_emp_name = "";
								jc_technician_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_technician_emp_name.equals("null") || jc_technician_emp_name.equals("0") || jc_technician_emp_name.equals("Technician")) {
									jc_technician_emp_name = "";
								}
							}

							// Total Amt
							if (h == 13) {
								jc_grandtotal = "";
								jc_grandtotal = PadQuotes(sheetData[j][h]);
								if (jc_grandtotal.equals("null") || jc_grandtotal.equals("0") || jc_grandtotal.equals("Total Amt")) {
									jc_grandtotal = "0";
								}
							}

							// Labour Amt
							if (h == 14) {
								jc_bill_cash_labour = "";
								jc_bill_cash_labour = PadQuotes(sheetData[j][h]);
								if (jc_bill_cash_labour.equals("null") || jc_bill_cash_labour.equals("0") || jc_bill_cash_labour.equals("Labour Amt")) {
									jc_bill_cash_labour = "0";
								}
							}

							// Part Amt
							if (h == 18) {
								jc_bill_cash_parts = "";
								jc_bill_cash_parts = PadQuotes(sheetData[j][h]);
								if (jc_bill_cash_parts.equals("null") || jc_bill_cash_parts.equals("0") || jc_bill_cash_parts.equals("Part Amt")) {
									jc_bill_cash_parts = "0";
								}
							}
							// Work Type
							if (h == 19) {
								jc_jctype_name = "";
								jc_jctype_name = PadQuotes(sheetData[j][h]);
								if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("Work type")) {
									jc_jctype_name = "";
								}
							}
							// Dis. Amt
							if (h == 22) {
								jc_bill_cash_labour_discamt = "";
								jc_bill_cash_labour_discamt = PadQuotes(sheetData[j][h]);
								if (jc_bill_cash_labour_discamt.equals("null") || jc_bill_cash_labour_discamt.equals("0") || jc_bill_cash_labour_discamt.equals("Dis.Amt")) {
									jc_bill_cash_labour_discamt = "0";
								}
							}
						}
						// Start Here Parts Feilds
						if (format_hyundai.equals("Parts Format")) {
							// Ro Bill Number
							if (h == 1) {
								jc_id = "0";
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
								if (jc_bill_cash_no.equals("null") || jc_bill_cash_no.equals("0") || jc_bill_cash_no.equals("Job Card No.")) {
									error_msg += "Bill No. should not to be empty! <br> ";
								}

								Iterator iterator = mapronum.entrySet().iterator();
								String flag = "false";
								while (iterator.hasNext()) {
									Map.Entry obj = (Map.Entry) iterator.next();
									if (obj.getValue().equals(jc_bill_cash_no)) {
										flag = "true";
										jctransdelete = 1;
										vouchertransdelete = 1;
									}
									if (!iterator.hasNext() && flag.equals("false")) {
										ronumcount++;
										jctransdelete = 0;
										vouchertransdelete = 0;
									}
								}
								// mapronum.put("ronum" + ronumcount, jc_bill_cash_no);
								if (ronumcount >= 1 && jctransdelete == 0) {
									mapronum.put("ronum" + ronumcount, jc_bill_cash_no);
								}
							}
							// Ro Bill Date
							if (h == 2) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = PadQuotes(sheetData[j][h]);
							}
							// Sale Type
							if (h == 3) {

							}
							// Invoice Type
							if (h == 4) {

							}
							// RO Type
							if (h == 5) {
								jc_jctype_name = "";
								jc_jctype_id = "0";
								jc_jctype_name = PadQuotes(sheetData[j][h]);
							}
							// cash/cerdit
							if (h == 6) {

							}
							// Customer code
							if (h == 7) {

							}
							// Reg No.
							if (h == 8) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
							}
							// Part Cat
							if (h == 9) {
								billcat_id = "0";
								billcat_name = "";
								billcat_name = PadQuotes(sheetData[j][h]);
								if (billcat_name.equals("null") || billcat_name.equals("Party Category")) {
									error_msg += "Part Category is Empty for this Bill No. ==>" + jc_bill_cash_no + "<br>";
								}
								StrSql = "SELECT billcat_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
										+ " WHERE 1=1"
										+ " AND billcat_brand_id = " + brand_id
										+ " AND (billcat_name = '" + billcat_name + "'"
										+ " OR billcat_code= '" + billcat_name + "')";
								// SOP("billcat_id StrSql==" + StrSql);
								billcat_id = CNumeric(ExecuteQuery(StrSql));
								// SOP("billcat_id==" + billcat_id);
								if (billcat_id.equals("0") || billcat_name.equals("null")) {
									error_msg += "Invalid Part Category for this Bill No. ==>" + jc_bill_cash_no + "<br>";
								}
							}
							// GST No.
							if (h == 10) {
							}
							// Rate(%).
							if (h == 11) {
							}
							// UQC
							if (h == 12) {
							}
							// Place of Supply
							if (h == 13) {
							}
							// Part No.
							if (h == 14) {
								item_code = "";
								item_code = PadQuotes(sheetData[j][h]);
							}
							// HSN Code
							if (h == 15) {
								item_hsn = "";
								item_hsn = PadQuotes(sheetData[j][h]);
								// SOP(("item_hsn==" + item_hsn);
							}
							// Part Name
							if (h == 16) {
								item_name = "";
								item_id = "0";
								jc_variant_id = "0";
								item_name = PadQuotes(sheetData[j][h]);
								if (item_name.equals("")) {
									error_msg += "Part Name should not to be empty! <br> ";
								} else if (!item_name.equals("")) {
									if (item_name.contains("(")) {
										item_name = item_name.replace("(", "&#40;");
									}
									if (item_name.contains(")")) {
										item_name = item_name.replace(")", "&#41;");
									}

									StrSql = "SELECT item_id"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item "
											+ " INNER JOIN " + compdb(comp_id) + " axela_inventory_item_model ON model_id = item_model_id "
											+ " INNER JOIN axelaauto.axela_brand ON brand_id = model_brand_id "
											+ " WHERE 1=1 "
											+ " AND item_name = '" + item_name + "'"
											+ " AND model_brand_id =" + brand_id
											// if (!item_code.equals("")) {
											// StrSql += " AND item_code = '" + item_code + "'";
											// }
											+ " LIMIT 1";
									// SOP("item==" + StrSql);
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.isBeforeFirst()) {
										while (crs.next()) {
											item_id = (crs.getString("item_id"));
										}
									}
									// SOP("item id==" + item_id);
									crs.close();
								}
							}
							// Batch cd
							if (h == 17) {
								batchcd = "";
								batchcd = PadQuotes(sheetData[j][h]);
							}
							// Batch Dt
							if (h == 18) {
								batchdt = "";
								batchdt = PadQuotes(sheetData[j][h]);
							}
							// Qty
							if (h == 19) {
								billquantity = "";
								billquantity = PadQuotes(sheetData[j][h]);
							}
							// Unit price
							if (h == 20) {
								jc_sell_price = "";
								jc_sell_price = PadQuotes(sheetData[j][h]);
							}
							// Dis amt
							if (h == 21) {
								jc_discount = "";
								jc_discount = PadQuotes(sheetData[j][h]);
							}
							// Value
							if (h == 22) {
								jc_sell_cost = "";
								jc_sell_cost = PadQuotes(sheetData[j][h]);
								if (jc_sell_cost.equals("0.00") || jc_sell_cost.equals("0") || jc_sell_cost.equals("")) {
									jc_sell_cost = "0";
								}
							}
							// Part Tax
							if (h == 23) {
								jc_tax_total = "";
								jc_tax_total = PadQuotes(sheetData[j][h]);
							}
							// PART TAX SGST
							if (h == 24) {
								tax1 = "";
								item_salestax1_ledger_id = "0";
								tax1 = PadQuotes(sheetData[j][h]);
								if (!tax1.equals("") && isNumeric(tax1) && !jc_sell_cost.equals("0")) {
									sgsttaxamt = tax1;
									tax1 = CalTaxPercentage(jc_sell_cost, tax1);// for tax calcuation
									tax1 = String.valueOf(Math.rint(Double.parseDouble(tax1))).replace(".0", "");// for round offand remove extra decimal
								}
							}
							// PART TAX CGST
							if (h == 25) {
								tax2 = "";
								item_salestax2_ledger_id = "0";
								tax2 = PadQuotes(sheetData[j][h]);
								if (!tax2.equals("") && isNumeric(tax2) && !jc_sell_cost.equals("0")) {
									cgsttaxamt = tax2;
									tax2 = CalTaxPercentage(jc_sell_cost, tax2);// for tax calcuation
									tax2 = String.valueOf(Math.rint(Double.parseDouble(tax2))).replace(".0", "");
								}
							}
							// PART TAX IGST
							if (h == 26) {
								tax3 = "";
								item_salestax3_ledger_id = "0";
								tax3 = PadQuotes(sheetData[j][h]);
								if (!tax3.equals("") && isNumeric(tax3) && !jc_sell_cost.equals("0")) {
									igsttaxamt = tax3;
									tax3 = CalTaxPercentage(jc_sell_cost, tax3);
									tax3 = String.valueOf(Math.rint(Double.parseDouble(tax3))).replace(".0", "");
								}
							}
							// PART TAX TGST
							if (h == 27) {
								tax4 = "";
								item_salestax4_ledger_id = "0";
								tax4 = PadQuotes(sheetData[j][h]);
								if (!tax4.equals("") && isNumeric(tax4) && !jc_sell_cost.equals("0")) {
									cesstaxamt = tax4;
									tax4 = CalTaxPercentage(jc_sell_cost, tax4);
									tax4 = String.valueOf(Math.rint(Double.parseDouble(tax4))).replace(".0", "");
								}
							}
							// FRG & INS TAX SGST
							if (h == 28) {
								tax5 = "";
								tax5 = PadQuotes(sheetData[j][h]);
							}
							// FRG & INS TAX CGST
							if (h == 29) {
								tax6 = "";
								tax6 = PadQuotes(sheetData[j][h]);
							}
							// FRG & INS TAX IGST
							if (h == 30) {
								tax7 = "";
								tax7 = PadQuotes(sheetData[j][h]);
							}
							// FRG & INS TAX TGST
							if (h == 31) {
								tax8 = "";
								tax8 = PadQuotes(sheetData[j][h]);
							}
							// Invoice Value
							if (h == 32) {
								jc_netprice = "";
								jc_netprice = PadQuotes(sheetData[j][h]);
							}

						}
					}
					if (format_hyundai.equals("Parts Format")) {
						if (error_msg.equals("") && !jc_bill_cash_no.equals("")) {
							sell = sell + Double.parseDouble(jc_sell_cost);
							PartsFormat(request, response);
						}
						if (!error_msg.equals("")) {
							jc_error_msg += "<br>" + ++count + "." + " Bill No. : " + jc_bill_cash_no + "==><br>" + error_msg;
						}

					}
					jc_entry_date = ToLongDate(kknow());
					if (!jc_ro_no.equals("") && format_hyundai.equals("Labour Format")) {
						jc_error_msg += obj.ValidateSheetData(comp_id, customer_name, contact_mobile1, contact_email1,
								contact_phone1, contact_address, contact_dob, contact_anniversary, contact_city, contact_pin,
								veh_engine_no, veh_sale_date, item_name, jc_branch_id,
								jc_reg_no, jc_chassis_no, jc_ro_no, jc_emp_name,
								jc_technician_emp_name, jc_time_in, jc_bill_cash_no, jc_bill_cash_date, jc_jctype_name,
								jc_jccat_name, jc_kms, jc_grandtotal, jc_bill_cash_labour,
								jc_bill_cash_parts, jc_bill_cash_parts_valueadd, jc_bill_cash_parts_oil, jc_bill_cash_parts_tyre,
								jc_bill_cash_parts_accessories, jc_bill_cash_labour_discamt, jc_notes, jc_advice,
								jc_entry_id, jc_entry_date, error_msg);
						propcount = obj.propcount;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public String PopulateFormatHyundai(String compid, String format_hyundai) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Labour Format\"").append(StrSelectdrop("Labour Format", format_hyundai)).append(">Labour Format</option>\n");
		Str.append("<option value=\"Parts Format\"").append(StrSelectdrop("Parts Format", format_hyundai)).append(">Parts Format</option>\n");
		// Str.append("<option value=\"Technician Format\"").append(StrSelectdrop("Technician Format", format_hyundai)).append(">Technician Format</option>\n");
		return Str.toString();
	}

	public void PartsFormat(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!jc_bill_cash_no.equals("")) {
			try {
				StrSql = "SELECT jc_id, jc_variant_id"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_bill_cash_no = '" + jc_bill_cash_no + "'"
						+ " AND jc_branch_id=" + jc_branch_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				// SOP("StrSql======" + StrSql);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						jc_variant_id = crs.getString("jc_variant_id");
						jc_id = CNumeric(crs.getString("jc_id"));
					}
				}
				crs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Main check for jc
			if (!jc_id.equals("0")) {
				// insert into jc table then get the item_id
				if (item_id.equals("0")) {
					// SOP(("33");
					AddItem();
				}
				// get item_id and insert into trans table
				if (!item_id.equals("0") && !jc_id.equals("0")) {

					try {
						conntx = connectDB();
						conntx.setAutoCommit(false);
						stmttx = conntx.createStatement();
						AddPartsTrans(request, response);
						if (error_msg.equals("")) {
							if (!billcat_id.equals("0")) {
								StrSql = " UPDATE " + compdb(comp_id) + "axela_inventory_item"
										+ " SET  item_billcat_id = " + billcat_id + ","
										+ " item_model_id = 76,"
										+ " item_modified_id = " + jc_entry_id + ","
										+ " item_modified_date = '" + jc_entry_date + "'"
										+ " WHERE item_id = " + item_id;
								stmttx.execute(StrSql);
								// SOP("StrSql==" + StrSql);
							}

							StrSql = "SELECT billcat_billtype, SUM(jctrans_qty) AS quantity, SUM(jctrans_netprice) AS totalamount"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ "	INNER JOIN " + compdb(comp_id) + " axela_service_jc_trans ON jctrans_jc_id = jc_id"
									+ "	INNER JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = jctrans_item_id"
									+ "	INNER JOIN " + compdb(comp_id) + "axela_inventory_item_bill_cat ON billcat_id = item_billcat_id"
									+ "	AND billcat_brand_id = " + brand_id
									+ "	WHERE jctrans_rowcount != 0"
									+ "	AND jctrans_option_id = 0"
									+ "	AND jctrans_item_id != 0"
									+ "	AND jctrans_jc_id = " + jc_id
									+ "	GROUP BY billcat_billtype";
							// SOP("StrSql==" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							while (crs.next()) {
								billcat_billtype = crs.getString("billcat_billtype");
								quantity = Double.parseDouble(crs.getString("quantity"));
								totalamount = Double.parseDouble(crs.getString("totalamount"));
								// SOP(("billcat_billtype=" + billcat_billtype);
								// SOP(("totalamount=" + totalamount);
								if (!billcat_billtype.equals("") && totalamount != 0.0) {
									StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
											+ " SET ";
									if (billcat_billtype.equals("Parts")) {
										StrSql += " jc_bill_cash_parts = " + totalamount;
									}
									if (billcat_billtype.equals("Tyre")) {
										StrSql += " jc_bill_cash_parts_tyre = " + totalamount + ","
												+ " jc_bill_cash_parts_tyre_qty= " + quantity;
									}
									if (billcat_billtype.equals("Oil")) {
										StrSql += " jc_bill_cash_parts_oil = " + totalamount;
									}
									if (billcat_billtype.equals("Battery")) {
										StrSql += " jc_bill_cash_parts_battery = " + totalamount + ","
												+ " jc_bill_cash_parts_battery_qty= " + quantity;
									}
									if (billcat_billtype.equals("Break")) {
										StrSql += " jc_bill_cash_parts_brake = " + totalamount + ","
												+ " jc_bill_cash_parts_brake_qty = " + quantity;
									}
									if (billcat_billtype.equals("Accessories")) {
										StrSql += " jc_bill_cash_parts_accessories = " + totalamount;
									}
									if (billcat_billtype.equals("Labour")) {
										StrSql += " jc_bill_cash_labour = " + totalamount;
									}
									if (billcat_billtype.equals("Valueadd")) {
										StrSql += " jc_bill_cash_parts_valueadd = " + totalamount;
									}
									if (billcat_billtype.equals("Extwarranty")) {
										StrSql += " jc_bill_cash_parts_extwarranty = " + totalamount + ","
												+ " jc_bill_cash_parts_extwarranty_qty= " + quantity;
									}
									if (billcat_billtype.equals("Wheelalign")) {
										StrSql += " jc_bill_cash_parts_wheelalign = " + totalamount;
									}
									if (billcat_billtype.equals("Cng")) {
										StrSql += " jc_bill_cash_parts_cng = " + totalamount;
									}
									StrSql += "," + " jc_modified_id = " + jc_entry_id + ","
											+ " jc_modified_date = '" + jc_entry_date + "'"
											+ " WHERE jc_id = " + jc_id;
									// SOP("StrSql=parts=" + StrSql);
									stmttx.execute(StrSql);
								}
							}
							crs.close();
						}
					} catch (Exception e) {
						if (conntx.isClosed()) {
							SOPError("connection is closed...");
						}
						if (!conntx.isClosed() && conntx != null) {
							conntx.rollback();
							SOPError("connection rollback...");
						}
						msg = "<br>Transaction Error!";
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
					} finally {
						conntx.setAutoCommit(true);
						if (stmttx != null && !stmttx.isClosed()) {
							stmttx.close();
						}
						if (conntx != null && !conntx.isClosed()) {
							conntx.close();
						}
					}

				}
			} else {
				error_msg += "Job card is not Present for this Bill No. : " + jc_bill_cash_no + "<br>";
			}
		}
	}
	public void AddItem() throws Exception {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			if (!tax1.equals("0") || !tax2.equals("0") || !tax3.equals("0") || !tax4.equals("0")) {
				StrSql = "SELECT";
				if (!tax1.equals("0")) {
					StrSql += " COALESCE(sgst.customer_id, 0) AS SGST,";
				}
				if (!tax2.equals("0")) {
					StrSql += " COALESCE(cgst.customer_id, 0) AS CGST,";
				}
				if (!tax1.equals("0")) {
					StrSql += " COALESCE(igst.customer_id, 0) AS IGST,";
				}
				if (!tax4.equals("0")) {
					StrSql += " COALESCE(cess.customer_id, 0) AS Cess";
				}
				if (StrSql.endsWith(",")) {// for removing extra comma
					StrSql = StrSql.substring(0, StrSql.length() - 1);
				}
				StrSql += " FROM axelaauto.axela_acc_tax_type";
				if (!tax1.equals("0")) {
					StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_taxtype_id = 3"
							+ " AND customer_rate = " + tax1
							+ " AND customer_tax = '1'"
							+ " GROUP BY customer_id ) AS sgst ON sgst.customer_taxtype_id = taxtype_id";
				}
				if (!tax2.equals("0")) {
					StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_taxtype_id = 4"
							+ " AND customer_rate = " + tax2
							+ " AND customer_tax = '1'"
							+ " GROUP BY customer_id ) AS cgst ON cgst.customer_taxtype_id = taxtype_id";
				}
				if (!tax1.equals("0")) {
					StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_taxtype_id = 5"
							+ " AND customer_rate = " + (2 * Double.parseDouble(tax1))
							+ " AND customer_tax = '1'"
							+ " GROUP BY customer_id ) AS igst ON igst.customer_taxtype_id = taxtype_id";
				}
				if (!tax4.equals("0")) {
					StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_taxtype_id = 6"
							+ " AND customer_rate = " + tax4
							+ " AND customer_tax = '1'"
							+ " GROUP BY customer_id ) AS cess ON cess.customer_taxtype_id = taxtype_id";
				}
				// SOP("check=tax=" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (!tax1.equals("0")) {
							if (!crs.getString("SGST").equals("0")) {
								item_salestax1_ledger_id = CNumeric(crs.getString("SGST"));
							}
						}
						if (!tax2.equals("0")) {
							if (!crs.getString("CGST").equals("0")) {
								item_salestax2_ledger_id = CNumeric(crs.getString("CGST"));
							}
						}
						if (!tax1.equals("0")) {
							if (!crs.getString("IGST").equals("0")) {
								item_salestax3_ledger_id = CNumeric(crs.getString("IGST"));
							}
						}

						if (!tax4.equals("0")) {
							if (!crs.getString("Cess").equals("0")) {
								item_salestax4_ledger_id = CNumeric(crs.getString("Cess"));
							}
						}
					}
				}
				crs.close();
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item"
					+ " (item_name,"
					+ " item_code,"
					+ " item_hsn,"
					+ " item_cat_id,"
					+ " item_billcat_id,"
					+ " item_type_id,"
					+ " item_model_id,"
					+ " item_active,"
					+ " item_uom_id,"
					+ " item_alt_uom_id,"
					+ "	item_sales_ledger_id,"
					+ "	item_salesdiscount_ledger_id,"
					+ "	item_salestax1_ledger_id,"
					+ "	item_salestax2_ledger_id,"
					+ "	item_salestax3_ledger_id,"
					+ "	item_salestax4_ledger_id,"
					+ " item_notes,"
					+ " item_entry_id,"
					+ " item_entry_date)"
					+ " VALUES ("
					+ " '" + item_name + "',"
					+ " '" + item_code + "',"
					+ " '" + item_hsn + "',"
					+ "	25," // item_cat_id
					+ " " + billcat_id + ","
					+ " 3," // item_type_id(Parts)
					+ " 76,"
					+ "	1," // item_active
					+ " 1," // item_uom_id
					+ " 1," // item_alt_uom_id
					+ " 1," // item_sales_ledger_id
					+ " 0," // item_salesdiscount_ledger_id
					+ " " + item_salestax1_ledger_id + ","
					+ " " + item_salestax2_ledger_id + ","
					+ " " + item_salestax3_ledger_id + ","
					+ " " + item_salestax4_ledger_id + ","
					+ "''," // item_notes
					+ " " + jc_entry_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP(("strsql==Item==" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				item_id = CNumeric(rs.getString(1));
			}
			rs.close();

			// Sales Price
			StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_inventory_item_price"
					+ " ( price_rateclass_id,"
					+ " price_item_id,"
					+ " price_amt,"
					+ " price_effective_from,"
					+ " price_active,"
					+ " price_entry_id,"
					+ " price_entry_date)"
					+ " VALUES"
					+ " ("
					+ " (SELECT branch_rateclass_id FROM " + compdb(comp_id) + "axela_branch " + " WHERE branch_id = " + jc_branch_id + "),"
					+ " " + item_id + ","
					+ " " + jc_sell_price + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 1," // price_active
					+ " " + jc_entry_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP(("StrSql==Sales Price==" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				price_id = CNumeric(rs.getString(1));
			}
			rs.close();
			jc_variant_id = item_id;

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stock "
					+ "( stock_location_id,"
					+ " stock_item_id,"
					+ " stock_entry_id,"
					+ " stock_entry_date )"
					+ " ( SELECT"
					+ " location_id,"
					+ item_id + ","
					+ jc_entry_id + ","
					+ "'" + jc_entry_date + "'"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location)";
			SOP("StrSql====" + StrSql);
			stmttx.execute(StrSql);
			conntx.commit();
			// SOP("strsql==sales price==" +StrSql);

		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}

	}
	public void AddPartsTrans(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Statement stmttx = conntx.createStatement();
			double jctrans_qty = 0.00, jctrans_price = 0.00, jctrans_amount = 0.00;
			double jctrans_netprice = 0.00, jctrans_discountamount = 0.00, jctrans_taxamount = 0.00;
			String tax = "";
			jctrans_qty = Double.parseDouble(billquantity);
			jctrans_price = Double.parseDouble(jc_sell_price);
			jctrans_amount = Double.parseDouble(jc_sell_cost);
			jctrans_netprice = Double.parseDouble(jc_sell_cost);// without tax from 19/6/17
			jctrans_discountamount = Double.parseDouble(jc_discount);
			jctrans_taxamount = Double.parseDouble(jc_tax_total);
			// Main Item entry in jc_trans table
			// jctrans_rowcount = CNumeric(ExecuteQuery("SELECT COALESCE(MAX(jc.jctrans_rowcount), 0) + 1"
			// + " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
			// + " WHERE jctrans_jc_id = " + jc_id));
			// jctrans_option_id = jctrans_rowcount;
			StrSql = "SELECT item_id, item_salestax1_ledger_id,"
					+ " item_salestax2_ledger_id, item_salestax3_ledger_id,"
					+ " item_salestax4_ledger_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " WHERE 1=1"
					+ " AND item_id = " + item_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_salestax1_ledger_id = CNumeric(crs.getString("item_salestax1_ledger_id"));
					item_salestax2_ledger_id = CNumeric(crs.getString("item_salestax2_ledger_id"));
					item_salestax3_ledger_id = CNumeric(crs.getString("item_salestax3_ledger_id"));
					item_salestax4_ledger_id = CNumeric(crs.getString("item_salestax4_ledger_id"));
					item_id = CNumeric(crs.getString("item_id"));
				}
			}
			crs.close();

			if (item_salestax1_ledger_id.equals("0") && !tax1.equals("") && !tax1.equals("0")) {
				StrSql = "SELECT customer_id"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE  customer_rate = " + tax1
						+ " AND customer_name LIKE 'SGST%'";
				crs = processQuery(StrSql, 0);
				// SOP("StrSql1==" + StrSql);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						item_salestax1_ledger_id = CNumeric(crs.getString("customer_id"));
					}
				}
				crs.close();
			}
			if (item_salestax2_ledger_id.equals("0") && !tax2.equals("") && !tax2.equals("0")) {
				StrSql = "SELECT customer_id"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE  customer_rate = " + tax2
						+ " AND customer_name LIKE 'CGST%'";
				crs = processQuery(StrSql, 0);
				// SOP("StrSql2==" + StrSql);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						item_salestax2_ledger_id = CNumeric(crs.getString("customer_id"));
					}
				}
				crs.close();
			}
			if (item_salestax3_ledger_id.equals("0") && !tax3.equals("") && !tax3.equals("0")) {
				StrSql = "SELECT customer_id"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE  customer_rate = " + tax3
						+ " AND customer_name LIKE 'IGST%'";
				crs = processQuery(StrSql, 0);
				// SOP("StrSql3==" + StrSql);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						item_salestax3_ledger_id = CNumeric(crs.getString("customer_id"));
					}
				}
				crs.close();
			}
			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item"
					+ " SET"
					+ "	item_salestax1_ledger_id = " + item_salestax1_ledger_id + ","
					+ "	item_salestax2_ledger_id = " + item_salestax2_ledger_id + ","
					+ "	item_salestax3_ledger_id = " + item_salestax3_ledger_id + ""
					+ " WHERE item_id = " + item_id + "";
			updateQuery(StrSql);
			// SOP("UPDATE=item=" + StrSql);

			if (!item_salestax1_ledger_id.equals("0") || !item_salestax2_ledger_id.equals("0") || !item_salestax3_ledger_id.equals("0") || !item_salestax4_ledger_id.equals("0")) {

				StrSql = "SELECT customer_rate"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE  customer_id = " + item_salestax1_ledger_id;
				tax = CNumeric(ExecuteQuery(StrSql));
				// SOP("tax==" + tax.replaceAll(".000", ""));
				// SOP("tax1==" + tax1);

				if (jctransdelete == 0) {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
							+ " WHERE jctrans_jc_id =" + jc_id;
					// SOP("StrSql==" + StrSql);
					stmttx.addBatch(StrSql);
					jctransdelete = 0;
				}

				if (!tax.replaceAll(".000", "").equals(tax1)) {
					if (!item_salestax1_ledger_id.equals("0") && !tax1.equals("") && !tax1.equals("0")) {
						StrSql = "SELECT customer_id"
								+ " FROM " + compdb(comp_id) + "axela_customer"
								+ " WHERE  customer_rate = " + tax1
								+ " AND customer_name LIKE 'SGST%'";
						crs = processQuery(StrSql, 0);
						// SOP("StrSql1==" + StrSql);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								item_salestax1_ledger_id = CNumeric(crs.getString("customer_id"));
							}
						}
						crs.close();
					}
					if (!item_salestax2_ledger_id.equals("0") && !tax2.equals("") && !tax2.equals("0")) {
						StrSql = "SELECT customer_id"
								+ " FROM " + compdb(comp_id) + "axela_customer"
								+ " WHERE  customer_rate = " + tax2
								+ " AND customer_name LIKE 'CGST%'";
						crs = processQuery(StrSql, 0);
						// SOP("StrSql2==" + StrSql);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								item_salestax2_ledger_id = CNumeric(crs.getString("customer_id"));
							}
						}
						crs.close();
					}
					if (!item_salestax3_ledger_id.equals("0") && !tax3.equals("") && !tax3.equals("0")) {
						StrSql = "SELECT customer_id"
								+ " FROM " + compdb(comp_id) + "axela_customer"
								+ " WHERE  customer_rate = " + tax3
								+ " AND customer_name LIKE 'IGST%'";
						crs = processQuery(StrSql, 0);
						// SOP("StrSql3==" + StrSql);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								item_salestax3_ledger_id = CNumeric(crs.getString("customer_id"));
							}
						}
						crs.close();
					}
					StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item"
							+ " SET"
							+ "	item_salestax1_ledger_id = " + item_salestax1_ledger_id + ","
							+ "	item_salestax2_ledger_id = " + item_salestax2_ledger_id + ","
							+ "	item_salestax3_ledger_id = " + item_salestax3_ledger_id + ""
							+ " WHERE item_id = " + item_id + "";
					updateQuery(StrSql);
					// SOP("UPDATE=item=" + StrSql);

					StrSql = "SELECT customer_rate"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE  customer_id = " + item_salestax1_ledger_id;
					tax = CNumeric(ExecuteQuery(StrSql));
				}

				if (tax.replaceAll(".000", "").equals(tax1)) {
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
							+ " ( jctrans_jc_id,"
							+ " jctrans_customer_id,"
							+ " jctrans_location_id,"
							+ " jctrans_item_id,"
							+ " jctrans_discount,"
							+ " jctrans_tax,"
							+ "	jctrans_rowcount,"
							+ "	jctrans_option_id,"
							+ " jctrans_qty,"
							+ " jctrans_price,"
							+ " jctrans_amount,"
							+ " jctrans_netprice,"
							+ " jctrans_discountamount,"
							+ " jctrans_taxamount,"
							+ "	jctrans_alt_qty,"
							+ "	jctrans_alt_uom_id,"
							+ "	jctrans_unit_cost,"
							+ "	jctrans_convfactor,"
							+ " jctrans_time,"
							+ " jctrans_billtype_id,"
							+ " jctrans_stock,"
							+ "	jctrans_dc)"
							+ " VALUES"
							+ " ("
							+ " " + jc_id + ","
							+ " 1," // jctrans_customer_id
							+ " " + jctrans_location_id + ","
							+ " " + item_id + ","
							+ " 0," // jctrans_discount
							+ " 0," // jctrans_tax
							+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0) + 1"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
							+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
							+ "	0," // jctrans_option_id
							+ " " + jctrans_qty + ","
							+ " " + jctrans_price + ","
							+ " " + jctrans_amount + ","
							+ " " + jctrans_netprice + ","
							+ " " + jctrans_discountamount + ","
							+ " " + jctrans_taxamount + ","
							+ " " + jctrans_qty + ","
							+ " 1," // jctrans_alt_uom_id
							+ " " + jctrans_price + ","
							+ " 1," // jctrans_convfactor
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 1,"// jctrans_billtype_id
							+ " 1,"// jctrans_stock
							+ " 0" // jctrans_dc
							+ ")";
					// SOP("StrSql==JC Main Item==" + StrSql);
					stmttx.addBatch(StrSql);

					// Discount entry in jc_trans table
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
							+ " ( jctrans_jc_id,"
							+ " jctrans_customer_id,"
							+ " jctrans_item_id,"
							+ " jctrans_discount,"
							+ "	jctrans_rowcount,"
							+ "	jctrans_option_id,"
							+ " jctrans_price,"
							+ " jctrans_discount_perc,"
							+ " jctrans_amount,"
							+ " jctrans_time,"
							+ " jctrans_billtype_id,"
							+ "	jctrans_dc)"
							+ " VALUES"
							+ " ("
							+ " " + jc_id + ","
							+ " 2," // jctrans_customer_id
							+ " " + item_id + ","
							+ " 1," // jctrans_discount
							+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
							+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
							+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
							+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
							+ " " + jctrans_discountamount + ",";// jctrans_price
					if (!(jctrans_amount == 0.00)) {
						StrSql += " " + df1.format((jctrans_discountamount / jctrans_amount * 100)) + ",";
					} else {
						StrSql += "0,";// jctrans_discount_perc
					}
					StrSql += " " + jctrans_discountamount + "," // jctrans_amount
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 1,"// jctrans_billtype_id
							+ " 1" // jctrans_dc
							+ ")";
					// SOP(("StrSql==Discount==" + StrSql));
					stmttx.addBatch(StrSql);
					if (!item_salestax1_ledger_id.equals("0") && !tax1.equals("") && !tax1.equals("0")) {
						// Tax entry in jc_trans table
						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
								+ " ( jctrans_jc_id,"
								+ " jctrans_customer_id,"
								+ " jctrans_item_id,"
								+ " jctrans_tax,"
								+ " jctrans_tax_id,"
								+ "	jctrans_rowcount,"
								+ "	jctrans_option_id,"
								+ " jctrans_price,"
								+ " jctrans_amount,"
								+ " jctrans_billtype_id,"
								+ " jctrans_time)"
								+ " VALUES"
								+ " ("
								+ " " + jc_id + ","
								+ " " + item_salestax1_ledger_id + "," // jctrans_customer_id
								+ " " + item_id + ","
								+ " 1," // jctrans_tax
								+ " " + item_salestax1_ledger_id + "," // jctrans_tax_id
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
								+ " " + sgsttaxamt + "," // jctrans_price
								+ " " + sgsttaxamt + "," // jctrans_amount
								+ " 1,"// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax1==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					if (!item_salestax2_ledger_id.equals("0") && !tax2.equals("") && !tax2.equals("0")) {
						// Tax entry in jc_trans table
						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
								+ " ( jctrans_jc_id,"
								+ " jctrans_customer_id,"
								+ " jctrans_item_id,"
								+ " jctrans_tax,"
								+ " jctrans_tax_id,"
								+ "	jctrans_rowcount,"
								+ "	jctrans_option_id,"
								+ " jctrans_price,"
								+ " jctrans_amount,"
								+ " jctrans_billtype_id,"
								+ " jctrans_time)"
								+ " VALUES"
								+ " ("
								+ " " + jc_id + ","
								+ " " + item_salestax2_ledger_id + "," // jctrans_customer_id
								+ " " + item_id + ","
								+ " 1," // jctrans_tax
								+ " " + item_salestax2_ledger_id + "," // jctrans_tax_id
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
								+ " " + cgsttaxamt + "," // jctrans_price
								+ " " + cgsttaxamt + "," // jctrans_amount
								+ " 1,"// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax2==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					if (!item_salestax3_ledger_id.equals("0") && !tax3.equals("") && !tax3.equals("0")) {
						// Tax entry in jc_trans table
						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
								+ " ( jctrans_jc_id,"
								+ " jctrans_customer_id,"
								+ " jctrans_item_id,"
								+ " jctrans_tax,"
								+ " jctrans_tax_id,"
								+ "	jctrans_rowcount,"
								+ "	jctrans_option_id,"
								+ " jctrans_price,"
								+ " jctrans_amount,"
								+ " jctrans_billtype_id,"
								+ " jctrans_time)"
								+ " VALUES"
								+ " ("
								+ " " + jc_id + ","
								+ " " + item_salestax3_ledger_id + "," // jctrans_customer_id
								+ " " + item_id + ","
								+ " 1," // jctrans_tax
								+ " " + item_salestax3_ledger_id + "," // jctrans_tax_id
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
								+ " " + igsttaxamt + "," // jctrans_price
								+ " " + igsttaxamt + "," // jctrans_amount
								+ " 1,"// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax3==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					if (!item_salestax4_ledger_id.equals("0") && !tax4.equals("")) {
						// Tax entry in jc_trans table
						StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
								+ " ( jctrans_jc_id,"
								+ " jctrans_customer_id,"
								+ " jctrans_item_id,"
								+ " jctrans_tax,"
								+ " jctrans_tax_id,"
								+ "	jctrans_rowcount,"
								+ "	jctrans_option_id,"
								+ " jctrans_price,"
								+ " jctrans_amount,"
								+ " jctrans_billtype_id,"
								+ " jctrans_time)"
								+ " VALUES"
								+ " ("
								+ " " + jc_id + ","
								+ " " + item_salestax4_ledger_id + "," // jctrans_customer_id
								+ " " + item_id + ","
								+ " 1," // jctrans_tax
								+ " " + item_salestax4_ledger_id + "," // jctrans_tax_id
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
								+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
								+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
								+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
								+ " " + cesstaxamt + "," // jctrans_price
								+ " " + cesstaxamt + "," // jctrans_amount
								+ " 1,"// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax4==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					// stmttx.executeBatch();
					// conntx.commit();

					StrSql = "SELECT voucher_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_jc_id = " + jc_id + ""
							+ "	AND voucher_active = 1";
					voucher_id = CNumeric(ExecuteQuery(StrSql));
					// SOP(("voucher_id==" + voucher_id);
					if (!voucher_id.equals("0")) {
						UpdateVoucher(); // To update existing invoice for the Job Card.
					}
					propcount++;
				}
				// else {
				// error_msg += "Tax configuration is not matching for the item: " + item_name + " (" + item_code + ")!" + "<br>";
				// }
			}
			// else {
			// error_msg += "Tax is not configured for the item: " + item_name + " (" + item_code + ")!" + "<br>";
			// }
			stmttx.executeBatch();
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}

	}
	public void UpdateVoucher() throws Exception {
		try {
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			double vouchertrans_qty = 0.00, vouchertrans_price = 0.00, vouchertrans_amount = 0.00, amount = 0.00;
			double vouchertrans_netprice = 0.00, vouchertrans_discountamount = 0.00, vouchertrans_taxamount = 0.00;
			String vouchertrans_rowcount = "", vouchertrans_option_id = "";

			vouchertrans_qty = Double.parseDouble(billquantity);
			vouchertrans_price = Double.parseDouble(jc_sell_price);
			vouchertrans_amount = Double.parseDouble(jc_sell_cost);
			vouchertrans_netprice = Double.parseDouble(jc_sell_cost);// without tax from 19/6/17
			vouchertrans_discountamount = Double.parseDouble(jc_discount);
			vouchertrans_taxamount = Double.parseDouble(jc_tax_total);
			StrSql = "SELECT item_id, item_salestax1_ledger_id,"
					+ " item_salestax2_ledger_id, item_salestax3_ledger_id,"
					+ " item_salestax4_ledger_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " WHERE 1=1"
					+ " AND item_id = " + item_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_id = CNumeric(crs.getString("item_id"));
					item_salestax1_ledger_id = CNumeric(crs.getString("item_salestax1_ledger_id"));
					item_salestax2_ledger_id = CNumeric(crs.getString("item_salestax2_ledger_id"));
					item_salestax3_ledger_id = CNumeric(crs.getString("item_salestax3_ledger_id"));
					item_salestax4_ledger_id = CNumeric(crs.getString("item_salestax4_ledger_id"));
				}
			}
			crs.close();
			if (!item_salestax1_ledger_id.equals("0") || !item_salestax2_ledger_id.equals("0") ||
					!item_salestax3_ledger_id.equals("0") || !item_salestax4_ledger_id.equals("0")) {
				if (vouchertransdelete == 0) {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " WHERE vouchertrans_voucher_id =" + voucher_id;
					stmttx.addBatch(StrSql);
					vouchertransdelete = 1;
				}
				StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_location_id,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_discount,"
						+ " vouchertrans_tax,"
						+ "	vouchertrans_rowcount,"
						+ "	vouchertrans_option_id,"
						+ " vouchertrans_qty,"
						+ " vouchertrans_price,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_netprice,"
						+ " vouchertrans_discountamount,"
						+ " vouchertrans_taxamount,"
						+ "	vouchertrans_alt_qty,"
						+ "	vouchertrans_alt_uom_id,"
						+ "	vouchertrans_unit_cost,"
						+ "	vouchertrans_convfactor,"
						+ " vouchertrans_time,"
						+ "	vouchertrans_dc)"
						+ " VALUES"
						+ " ("
						+ " " + voucher_id + ","
						+ " 1," // vouchertrans_customer_id
						+ " " + jctrans_location_id + ","// vouchertrans_location_id
						+ " " + item_id + ","
						+ " 0," // vouchertrans_discount
						+ " 0," // vouchertrans_tax
						+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
						+ "	0," // vouchertrans_option_id
						+ " " + vouchertrans_qty + ","
						+ " " + vouchertrans_price + ","
						+ " " + vouchertrans_amount + ","
						+ " " + vouchertrans_netprice + ","
						+ " " + vouchertrans_discountamount + ","
						+ " " + vouchertrans_taxamount + ","
						+ " " + vouchertrans_qty + ","
						+ " 1," // vouchertrans_alt_uom_id
						+ " " + vouchertrans_price + ","
						+ " 1," // vouchertrans_convfactor
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 0" // vouchertrans_dc
						+ ")";
				// SOP(("StrSql==Voucher Main Item==" + StrSql));
				stmttx.addBatch(StrSql);

				// Discount entry in voucher_trans table
				StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " ("
						+ " vouchertrans_voucher_id,"
						+ " vouchertrans_customer_id,"
						+ " vouchertrans_item_id,"
						+ " vouchertrans_discount,"
						+ "	vouchertrans_rowcount,"
						+ "	vouchertrans_option_id,"
						+ " vouchertrans_price,"
						+ " vouchertrans_discount_perc,"
						+ " vouchertrans_amount,"
						+ " vouchertrans_time,"
						+ "	vouchertrans_dc)"
						+ " VALUES"
						+ " ("
						+ " " + voucher_id + ","
						+ " 2," // vouchertrans_customer_id
						+ " " + item_id + ","
						+ " 1," // vouchertrans_discount
						+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
						+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // vouchertrans_option_id
						+ " " + vouchertrans_discountamount + ",";// vouchertrans_price
				if (!(vouchertrans_amount == 0.00)) {
					StrSql += " " + df1.format((vouchertrans_discountamount / vouchertrans_amount * 100)) + ",";
				} else {
					StrSql += "0,";// vouchertrans_discount_perc
				}
				StrSql += " " + vouchertrans_discountamount + "," // vouchertrans_amount
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 1" // jctrans_dc
						+ ")";
				// SOP(("StrSql== voucher Discount==" + StrSql));
				stmttx.addBatch(StrSql);

				// Tax entry in voucher_trans table
				if (!item_salestax1_ledger_id.equals("0") && !tax1.equals("")) {
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ("
							+ " vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_tax_id,"
							+ "	vouchertrans_rowcount,"
							+ "	vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time)"
							+ " VALUES"
							+ " ("
							+ " " + voucher_id + ","
							+ " " + item_salestax1_ledger_id + "," // vouchertrans_customer_id
							+ " " + item_id + ","
							+ " 1," // vouchertrans_tax
							+ " " + item_salestax1_ledger_id + "," // vouchertrans_tax_id
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // vouchertrans_option_id
							+ " " + sgsttaxamt + "," // vouchertrans_price
							+ " " + sgsttaxamt + "," // vouchertrans_amount
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql==voucher Tax1==" + StrSql);
					stmttx.addBatch(StrSql);
				}
				if (!item_salestax2_ledger_id.equals("0") && !tax2.equals("")) {
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ("
							+ " vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_tax_id,"
							+ "	vouchertrans_rowcount,"
							+ "	vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time)"
							+ " VALUES"
							+ " ("
							+ " " + voucher_id + ","
							+ " " + item_salestax2_ledger_id + "," // vouchertrans_customer_id
							+ " " + item_id + ","
							+ " 1," // vouchertrans_tax
							+ " " + item_salestax2_ledger_id + "," // vouchertrans_tax_id
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // vouchertrans_option_id
							+ " " + cgsttaxamt + "," // vouchertrans_price
							+ " " + cgsttaxamt + "," // vouchertrans_amount
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql==voucher Tax2==" + StrSql);
					stmttx.addBatch(StrSql);
				}
				if (!item_salestax3_ledger_id.equals("0") && !tax3.equals("")) {
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ("
							+ " vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_tax_id,"
							+ "	vouchertrans_rowcount,"
							+ "	vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time)"
							+ " VALUES"
							+ " ("
							+ " " + voucher_id + ","
							+ " " + item_salestax3_ledger_id + "," // vouchertrans_customer_id
							+ " " + item_id + ","
							+ " 1," // vouchertrans_tax
							+ " " + item_salestax3_ledger_id + "," // vouchertrans_tax_id
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // vouchertrans_option_id
							+ " " + igsttaxamt + "," // vouchertrans_price
							+ " " + igsttaxamt + "," // vouchertrans_amount
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql==voucher Tax3==" + StrSql);
					stmttx.addBatch(StrSql);
				}
				if (!item_salestax4_ledger_id.equals("0") && !tax4.equals("")) {
					StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " ("
							+ " vouchertrans_voucher_id,"
							+ " vouchertrans_customer_id,"
							+ " vouchertrans_item_id,"
							+ " vouchertrans_tax,"
							+ " vouchertrans_tax_id,"
							+ "	vouchertrans_rowcount,"
							+ "	vouchertrans_option_id,"
							+ " vouchertrans_price,"
							+ " vouchertrans_amount,"
							+ " vouchertrans_time)"
							+ " VALUES"
							+ " ("
							+ " " + voucher_id + ","
							+ " " + item_salestax4_ledger_id + "," // vouchertrans_customer_id
							+ " " + item_id + ","
							+ " 1," // vouchertrans_tax
							+ " " + item_salestax4_ledger_id + "," // vouchertrans_tax_id
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
							+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // vouchertrans_option_id
							+ " " + cesstaxamt + "," // vouchertrans_price
							+ " " + cesstaxamt + "," // vouchertrans_amount
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql==voucher Tax4==" + StrSql);
					stmttx.addBatch(StrSql);
				}
				stmttx.executeBatch();

				// Update voucher amount in voucher table
				StrSql = "SELECT "
						+ "@totalamt := SUM(CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_amount END) AS amount, "
						+ "@totaldis := SUM(CASE WHEN vouchertrans_discount = 1 THEN vouchertrans_amount END) AS discount, "
						+ "@totaltax := SUM(CASE WHEN vouchertrans_tax = 1 THEN vouchertrans_amount END) AS tax, "
						+ "@total := (@totalamt - @totaldis) + @totaltax AS netamount "
						+ "FROM " + compdb(comp_id) + "axela_acc_voucher_trans "
						+ "WHERE vouchertrans_voucher_id = " + voucher_id;

				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					amount = (crs.getDouble("amount") - crs.getDouble("discount")) + crs.getDouble("tax");
				}
				crs.close();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher "
						+ "SET voucher_amount = " + amount + ", "
						+ " voucher_modified_id = " + jc_entry_id + ","
						+ " voucher_modified_date = '" + jc_entry_date + "'"
						+ "WHERE voucher_id = " + voucher_id;

				updateQuery(StrSql);
				conntx.commit();
			}
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}