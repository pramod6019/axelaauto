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

public class JobCard_User_Import_Volvo extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", errormsg = "";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "", jc_jccat_name = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0;
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String BranchAccess = "", format_volvo = "";
	public String tax1 = "0";
	public String branch_id = "0", jc_branch_id = "0", brand_id = "51";
	public String branch_name = "", veh_warranty_expirydate = "";
	String item_salestax1_ledger_id = "0", billtype_name = "", item_salestax2_ledger_id = "0", item_salestax3_ledger_id = "0", item_salestax4_ledger_id = "0";
	public String upload = "";
	public String similar_comm_no = "";
	public String jc_id = "0";
	public String vehkms_id = "0";
	public String jc_reg_no = "0", jc_ro_no = "", jc_no = "", error_msg = "";
	public String jc_error_msg = "";
	public String jc_kms = "0", jc_discamt = "0", jc_grandtotal = "0", jc_bill_cash_valueadd = "0", jc_chassis_no = "", jctrans_billtype_id = "0";
	public int labourcolumnLength = 0, partscolumnLength = 0, techniciancolumnLength = 0;
	public int count = 0, ronumcount = 1, jctransdelete = 0, vouchertransdelete = 0, k = 0;
	HashMap<String, String> mapronum = new HashMap<String, String>();
	HashMap<String, String> billtypecheck = new HashMap<String, String>();

	public String customer_name = "", caruser_name = "";
	public String contact_id = "";
	public String contact_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_phone1 = "", contact_phone2 = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String contact_email1 = "", contact_email2 = "";
	public String contact_address = "", contact_address1 = "", contact_address2 = "", contact_address3 = "", contact_city = "";
	public String contact_city_id = "0", contact_pin = "", contact_state = "";
	public String contact_title = "0";
	public String contact_dob = "", contact_anniversary = "";
	public String contact_title_id = "1";
	String sgsttaxamt = "", cgsttaxamt = "", igsttaxamt = "", cesstaxamt = "";

	// Stock Variables
	public String vehstock_id = "0";
	public String option_id = "0";
	public String so_id = "0", voucher_id = "0";

	// Vehicle Variables
	public String veh_id = "0";
	public String interior = "", exterior = "";
	public String item_id = "0", item_name = "", model_brand_id = "0";
	public String model_name = "", preownedmodel_id = "0", item_service_code = "";
	public String veh_engine_no = "", veh_reg_no = "";
	public String veh_modelyear = "";
	public String veh_sale_date = "", veh_insur_date = "", item_hsn = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0";
	public String soe_name = "", sob_name = "", soe_id = "0", sob_id = "0";
	public String veh_model_id = "0", veh_item_id = "0", veh_fueltype_id = "0", veh_loanfinancer = "", veh_status_id = "0", veh_status_date = "";
	public String veh_soe_id = "0", veh_buyertype_id = "0", veh_desc = "";
	public String veh_notes = "", veh_priorityveh_id = "0", veh_vehsource_id = "0", vehsource_name = "";

	// Jobcard Variables
	public String jc_jcstage_id = "6";
	public String jc_jctype_id = "0", jc_notes = "", jc_jctype_name = "", jc_jccat_id = "0";
	public String jc_advice = "";
	public String jc_time_in = "0", jc_time_promised = "", jc_time_ready = "", jc_time_out = "", jc_bill_cash_labour_discamt = "";
	public String jc_emp_id = "0", jc_emp_name = "", jc_bill_cash_no = "";
	public String jc_technician_emp_id = "0", jc_technician_emp_name = "";
	public String hrs = "", min = "", day = "", month = "", year = "", servicedueyear = "", veh_service_duekms = "", veh_service_duedate = "";
	public String jc_bill_cash_date = "", jc_customer_name = "", jc_bill_cash_parts = "0", jc_bill_cash_tyre = "0.0", jc_bill_cash_oil = "0.0";
	public String jc_bill_cash_accessories = "0.0", jc_bill_cash_labour = "0.0";
	public String jc_customername = "";

	public String servicetype = "", veh_chassis_no = "", item = "", custcat = "", serviadv = "", technician = "", customername = "", jc_customer_id = "", telephone = "",
			jc_contact_id = "";
	public String customer_id = "0";
	public String chassisid = "0";
	// parts variable
	String jctrans_location_id = "0";
	public String billquantity = "0", item_code = "", jc_bill_city = "", price_id = "0", sublet = "", other = "", jc_amt_labour = "";
	public String jc_sell_price = "";
	public String jc_sell_cost = "", jc_netprice = "", billcat_name = "", billcat_billtype = "", item_cat_id = "0", billcat_id = "0";
	public String actual_amount = "", jc_tax_total = "", jc_discount = "";
	double totalamount = 0.0, quantity = 0.0;
	public String jctrans_rowcount = "0", jctrans_option_id = "0";
	public DecimalFormat df = new DecimalFormat("0.00");
	public DecimalFormat df1 = new DecimalFormat("0.0000");

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
					format_volvo = str1[1];
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
										File uploadedFile = new File(VehicleImportPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										String fileName1 = VehicleImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0, request, response);
										if (msg.equals("")) {
											msg += "<br>" + propcount + " Jobcard(s) Imported successfully!";
											if (obj.jcpresent != 0) {
												msg += "<br>" + obj.jcpresent + " JobCard(s) already present!";
											}
											if (!jc_error_msg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + jc_error_msg;
											}
											if (format_volvo.equals("Parts Format")) {
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
			response.sendRedirect("../service/jobcard-user-import-volvo.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(jc_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (format_volvo.equals("")) {
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
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			if (rowLength > 2000) {
				rowLength = 2000;
			}
			int h = 0;
			int j = 0;
			labourcolumnLength = 38;
			partscolumnLength = 19;
			propcount = 0;
			obj.propcount = 0;
			obj.brand_id = brand_id;

			StrSql = "SELECT location_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id;
			jctrans_location_id = CNumeric(ExecuteQuery(StrSql));
			SOP("format_volvo==" + format_volvo);
			if (format_volvo.equals("Labour Format") && columnLength != labourcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else if (format_volvo.equals("Parts Format") && columnLength != partscolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
			else {
				for (j = 1; j < rowLength + 1; j++) {
					// CheckForm();
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {
						if (format_volvo.equals("Labour Format")) {
							// Ro_no
							if (h == 1) {
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card Ro.")) {
									jc_ro_no = "";
								}
							}
							// job card RO Date
							if (h == 2) {
								jc_time_in = "";
								jc_time_in = PadQuotes(sheetData[j][h]);
								if (jc_time_in.equals("null") || jc_time_in.equals("0") || jc_time_in.equals("Job Card Open Date")) {
									jc_time_in = "";
								}
							}
							// jc_jccat_id
							if (h == 3) {
								jc_jccat_name = "";
								jc_jccat_name = PadQuotes(sheetData[j][h]);
							}

							// Chassis Number....
							if (h == 4) {
								jc_chassis_no = "";
								jc_chassis_no = PadQuotes(sheetData[j][h]);
								if (jc_chassis_no.equals("null") || jc_chassis_no.equals("0") || jc_chassis_no.equals("Chassis No.")) {
									jc_chassis_no = "";
								}
							}
							// Mileage....
							if (h == 5) {
								jc_kms = "0";
								jc_kms = PadQuotes(sheetData[j][h]);
								if (jc_kms.equals("null") || jc_kms.equals("") || jc_kms.equals("Mileage")) {
									jc_kms = "0";
								}
								veh_lastservice_kms = jc_kms;
							}
							// model

							if (h == 6)
							{
								item_name = "";
								item_name = PadQuotes(sheetData[j][h]);
								if (item_name.equals("null") || item_name.equals("0") || item_name.equals("Model")) {
									item_name = "";
								}
							}

							// variant
							if (h == 7) {

							}

							// billtype...
							if (h == 8) {
								// jc_jctype_name = "";
								// jc_jctype_name = PadQuotes(sheetData[j][h]);
								// // SOP("jc_jctype_name==" + jc_jctype_name);
								// if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("Service Type")) {
								// jc_jctype_name = "";
								// }
								// SOP("88==" + jc_jctype_name);
							}

							// bill no
							if (h == 9) {
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
							}

							// bill date
							if (h == 10) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = ConvertShortDateToStr(sheetData[j][h]);
							}

							// customer name
							if (h == 11) {
								customer_name = "";
								customer_name = PadQuotes(sheetData[j][h]);
							}

							// Mobile Number....
							if (h == 12) {
								contact_mobile1 = "";
								contact_mobile1 = PadQuotes(sheetData[j][h]);
							}

							// city name
							if (h == 13) {
								contact_city = "";
								contact_city = PadQuotes(sheetData[j][h]);
							}

							// state name
							if (h == 14) {

							}
							// GST number
							if (h == 15) {

							}

							// reg no
							if (h == 16) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
								if (jc_reg_no.equals("null") || jc_reg_no.equals("0") || jc_reg_no.equals("Reg. Num")) {
									jc_reg_no = "";
								}
							}

							// parts value
							if (h == 17) {

							}

							// parts discount
							if (h == 18) {

							}

							// final parts value
							if (h == 19) {

							}

							// parts tax
							if (h == 20) {

							}

							// parts tax detail
							if (h == 21) {

							}

							// parts total
							if (h == 22) {

							}

							// labour value
							if (h == 23) {

							}

							// labour bill discount
							if (h == 24) {
								jc_discamt = "";
								jc_discamt = PadQuotes(sheetData[j][h]);
							}

							// final labour ammnt
							if (h == 25) {
								jc_amt_labour = "";
								jc_amt_labour = PadQuotes(sheetData[j][h]);
								if (jc_amt_labour.equals("")) {
									jc_amt_labour = "0";
								}
							}

							// labour tax
							if (h == 26) {

							}

							// labour tax detail
							if (h == 27) {

							}

							// IGST 18%
							if (h == 28) {

							}

							// CGST 9%
							if (h == 29) {

							}

							// SGST 9%
							if (h == 30) {

							}

							// labour total
							if (h == 31) {

							}

							// sublet
							if (h == 32) {
								sublet = "";
								sublet = PadQuotes(sheetData[j][h]);
								if (sublet.equals("")) {
									sublet = "0";
								}
							}
							// other
							if (h == 33) {
								other = "";
								other = PadQuotes(sheetData[j][h]);
								if (other.equals("")) {
									other = "0";
								}
								if (!jc_amt_labour.equals("") && !sublet.equals("") && !other.equals("")) {
									jc_bill_cash_labour = Double.toString((Double.parseDouble(jc_amt_labour) + Double.parseDouble(sublet) + Double.parseDouble(other)));
								}
							}

							// discount
							if (h == 34) {
								// jc_discamt = "";
								// jc_discamt = PadQuotes(sheetData[j][h]);
								// SOP("341" + jc_discamt);
							}

							// bill ammnt
							if (h == 36) {
								jc_grandtotal = "0";
								jc_grandtotal = PadQuotes(sheetData[j][h]);
							}

							// service advisor
							if (h == 37) {
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
									jc_emp_name = "";
								}
							}
						}
						// Start Here Parts Feilds

						if (format_volvo.equals("Parts Format")) {
							// Ro Bill No
							if (h == 1) {
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
								// SOP("jc_bill_cash_no======" + jc_bill_cash_no);
							}
							// Ro Bill Date
							if (h == 2) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = ConvertShortDateToStr(PadQuotes(sheetData[j][h]));
							}
							// party name
							if (h == 3) {
								jc_customer_name = "";
								jc_customer_name = PadQuotes(sheetData[j][h]);
							}
							// Ro Number
							if (h == 4) {
								jc_id = "0";
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								// SOP("jc_ro_no===" + jc_ro_no);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card No.") || jc_ro_no.equals("")) {
									error_msg += " Ro No. should not be empty !<br> ";
								}
								Iterator iterator = mapronum.entrySet().iterator();
								String flag = "false";
								while (iterator.hasNext()) {
									Map.Entry obj = (Map.Entry) iterator.next();
									if (obj.getValue().equals(jc_ro_no)) {
										flag = "true";
										jctransdelete = 1;
										vouchertransdelete = 1;
									}
									if (!iterator.hasNext() && flag.equals("false")) {
										ronumcount++;
										mapronum.put("ronum" + ronumcount, jc_ro_no);
										jctransdelete = 0;
										vouchertransdelete = 0;
									}
								}
								if (ronumcount >= 1 && jctransdelete == 0) {
									mapronum.put("ronum" + ronumcount, jc_ro_no);
								}

							}
							// Reg No.
							if (h == 5) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
							}
							// VIN
							if (h == 6) {
							}
							// part issue type
							if (h == 7) {
								billtype_name = "";
								billtype_name = PadQuotes(sheetData[j][h]);
								jctrans_billtype_id = "0";
								if (!billtype_name.equals("")) {
									if (billtype_name.equals("Dealer Paid")) {
										billtype_name = "Internal";
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
									if (billtype_name.equals("Billable")) {
										billtype_name = "Cash";
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
									if (billtype_name.equalsIgnoreCase("Warranty")) {
										billtype_name = "Warranty";
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
									if (billtype_name.equalsIgnoreCase("Extended Warranty")) {
										billtype_name = "Insurance";
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
									if (!billtypecheck.containsKey(billtype_name)) {
										jctrans_billtype_id = CNumeric(ExecuteQuery("SELECT billtype_id"
												+ " FROM " + compdb(comp_id) + "axela_service_jc_bill_type "
												+ " WHERE billtype_name = '" + billtype_name + "'"));
										if (!jctrans_billtype_id.equals("0")) {
											billtypecheck.put(billtype_name, jctrans_billtype_id);
											jctrans_billtype_id = billtypecheck.get(billtype_name);
										}
										if (jctrans_billtype_id.equals("0")) {
											error_msg += "Invalid Bill Type for this Ro No. ==>" + jc_ro_no + "<br>";
										}
									}
								} else if (billtype_name.equals("")) {
									error_msg += "Part Type should not be empty! <br>";
								}
							}
							// Service advisor
							if (h == 8) {

							}
							// part no.
							if (h == 9) {

								item_code = "";
								item_code = PadQuotes(sheetData[j][h]);
								if (item_code.equals("")) {
									error_msg += "Item Code should not be empty! <br>";
								}
							}
							// Part Name
							if (h == 10) {
								item_name = "";
								item_id = "0";
								item_name = PadQuotes(sheetData[j][h]);
								// SOP("item_name==" + item_name);
								if (!item_name.equals("")) {
									StrSql = "SELECT item_id"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + " axela_inventory_item_model ON model_id = item_model_id"
											+ " WHERE 1 = 1"
											+ " AND item_code = '" + item_code + "'"
											+ " AND model_brand_id =" + brand_id
											+ " LIMIT 1";
									// SOP("item==" + StrSql);
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.isBeforeFirst()) {
										while (crs.next()) {
											item_id = CNumeric(crs.getString("item_id"));
										}
									}
									SOP("item id==" + item_id);
									crs.close();
								} else {
									error_msg += " Item Name Should Not To Be Empty !<br> ";
								}
							}

							if (h == 11) {
								item_hsn = "";
								item_hsn = PadQuotes(sheetData[j][h]);
							}

							// Part Cat
							if (h == 12) {
								billcat_id = "0";
								billcat_name = "";
								billcat_name = PadQuotes(sheetData[j][h]);
								if (billcat_name.equals("null") || billcat_name.equals("Party Category") || billcat_name.equals("")) {
									error_msg += "Part Category is Empty for this RO No. ==>" + jc_ro_no + "<br>";
								} else if (!billcat_name.equals(""))
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
									error_msg += "Invalid Part Category for this RO No. ==>" + jc_ro_no + "<br>";
								}
							}

							// Qty
							if (h == 13) {
								billquantity = "";
								billquantity = PadQuotes(sheetData[j][h]);
							}
							// Selling Price
							if (h == 14) {
								jc_sell_price = "";
								jc_sell_price = PadQuotes(sheetData[j][h]);
							}
							// Value without Tax
							if (h == 15) {
								jc_sell_cost = "";
								jc_sell_cost = PadQuotes(sheetData[j][h]);
								// SOP("jc_sell_cost==" + jc_sell_cost);
							}
							// Dis amt
							if (h == 16) {
								jc_discount = "";
								jc_discount = PadQuotes(sheetData[j][h]);
							}
							// Charge Amt
							if (h == 17) {
								jc_tax_total = "";
								jc_tax_total = PadQuotes(sheetData[j][h]);
							}
							// Item Value
							if (h == 18) {
								jc_netprice = "";
								jc_netprice = PadQuotes(sheetData[j][h]);
								// SOP("jc_netprice==" + jc_netprice);
							}

						}
					}
					if (format_volvo.equals("Parts Format")) {
						if (!jc_ro_no.equals("") && error_msg.equals("")) {
							PartsFormat(request, response);
						}
						if (!error_msg.equals("")) {
							// SOP("error_msg==" + error_msg);
							jc_error_msg += "<br>" + ++count + "." + " Ro No. : " + jc_ro_no + "==><br>" + error_msg;
						}

					}
					jc_entry_date = ToLongDate(kknow());
					if (!jc_ro_no.equals("") && format_volvo.equals("Labour Format")) {
						jc_error_msg += obj.ValidateSheetData(comp_id, customer_name, contact_mobile1, contact_email1,
								contact_phone1, contact_address, contact_dob, contact_anniversary, contact_city, contact_pin,
								veh_engine_no, veh_sale_date, item_name, jc_branch_id,
								jc_reg_no, jc_chassis_no, jc_ro_no, jc_emp_name,
								jc_technician_emp_name, jc_time_in, jc_bill_cash_no, jc_bill_cash_date, jc_jctype_name,
								jc_jccat_name, jc_kms, jc_grandtotal, jc_bill_cash_labour,
								jc_bill_cash_parts, jc_bill_cash_valueadd, jc_bill_cash_oil, jc_bill_cash_tyre,
								jc_bill_cash_accessories, jc_discamt, jc_notes, jc_advice,
								jc_entry_id, jc_entry_date, error_msg);
						propcount = obj.propcount;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public String PopulateFormatVolvo(String compid, String format_volvo) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Labour Format\"").append(StrSelectdrop("Labour Format", format_volvo)).append(">Labour Format</option>\n");
		Str.append("<option value=\"Parts Format\"").append(StrSelectdrop("Parts Format", format_volvo)).append(">Parts Format</option>\n");
		return Str.toString();
	}

	public void PartsFormat(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!jc_ro_no.equals("")) {

			StrSql = "SELECT jc_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_ro_no = '" + jc_ro_no + "'"
					+ " AND jc_branch_id=" + jc_branch_id;
			jc_id = CNumeric(ExecuteQuery(StrSql));
			// Main check for jc

			if (!jc_id.equals("0")) {
				// insert into jc table then get the variant_id
				if (item_id.equals("0")) {

					AddItem();
				}
				// get variant_id and insert into trans table
				if (!item_id.equals("0") && !jc_id.equals("0")) {
					// SOP(("44");

					try {
						conntx = connectDB();
						conntx.setAutoCommit(false);
						stmttx = conntx.createStatement();
						if (!billcat_id.equals("0")) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_inventory_item"
									+ " SET  item_billcat_id = " + billcat_id + ","
									+ " item_modified_id = " + jc_entry_id + ","
									+ " item_modified_date = '" + jc_entry_date + "'"
									+ " WHERE item_id = " + item_id;
							stmttx.execute(StrSql);
							// SOP("StrSql==" + StrSql);
						}

						AddPartsTrans(request, response);

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
						SOP("StrSql==" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET ";
							while (crs.next()) {
								billcat_billtype = crs.getString("billcat_billtype");
								totalamount = Double.parseDouble(crs.getString("totalamount"));
								quantity = Double.parseDouble(crs.getString("quantity"));
								SOP("billcat_billtype==" + billcat_billtype);
								SOP("billtype_name==" + billtype_name);
								if (!billcat_billtype.equals("") && totalamount != 0.0) {
									if (billtype_name.equalsIgnoreCase("Insurance")) {
										if (!jc_bill_cash_no.equals("")) {
											StrSql += " jc_bill_insur_no = '" + jc_bill_cash_no + "',";
										}
										if (!jc_customer_name.equals("")) {
											StrSql += " jc_bill_insur_customername = '" + jc_customer_name + "',";
										}
										if (billcat_billtype.equals("Parts")) {
											StrSql += " jc_bill_insur_parts = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Tyre")) {
											StrSql += " jc_bill_insur_parts_tyre = " + totalamount + ","
													+ " jc_bill_insur_parts_tyre_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Oil")) {
											StrSql += " jjc_bill_insur_parts_oil = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Battery")) {
											StrSql += " jc_bill_insur_parts_battery = " + totalamount + ","
													+ " jc_bill_insur_parts_battery_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Break")) {
											StrSql += " jc_bill_insur_parts_brake = " + totalamount + ","
													+ " jc_bill_insur_parts_brake_qty = " + quantity + ",";
										}
										if (billcat_billtype.equals("Accessories")) {
											StrSql += " jc_bill_insur_parts_accessories = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Labour")) {
											StrSql += " jc_bill_insur_labour = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Valueadd")) {
											StrSql += " jc_bill_insur_parts_valueadd = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Extwarranty")) {
											StrSql += " jc_bill_insur_parts_extwarranty = " + totalamount + ","
													+ " jc_bill_insur_parts_extwarranty_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Wheelalign")) {
											StrSql += " jc_bill_insur_parts_wheelalign = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Cng")) {
											StrSql += " jc_bill_insur_parts_cng = " + totalamount + ",";
										}
										if (!jc_bill_cash_date.equals("")) {
											StrSql += " jc_bill_insur_date = '" + jc_bill_cash_date + "',";
										}
									} else if (billtype_name.equalsIgnoreCase("Cash") || billtype_name.equalsIgnoreCase("Warranty")
											|| billtype_name.equalsIgnoreCase("Internal")) {
										if (!jc_bill_cash_no.equals("")) {
											if (billtype_name.equalsIgnoreCase("Cash") || billtype_name.equalsIgnoreCase("Internal")) {
												StrSql += " jc_bill_cash_no = '" + jc_bill_cash_no + "',";
												if (!jc_customer_name.equals("")) {
													StrSql += " jc_bill_cash_customername = '" + jc_customer_name + "',";
												}
												if (!jc_bill_cash_date.equals("")) {
													StrSql += " jc_bill_cash_date = '" + jc_bill_cash_date + "',";
												}
											}
											if (billtype_name.equalsIgnoreCase("Warranty")) {
												StrSql += " jc_bill_warranty_no = '" + jc_bill_cash_no + "',";
												if (!jc_customer_name.equals("")) {
													StrSql += " jc_bill_warranty_customername = '" + jc_customer_name + "',";
												}
												if (!jc_bill_cash_date.equals("")) {
													StrSql += " jc_bill_warranty_date = '" + jc_bill_cash_date + "',";
												}
											}
										}
										if (billcat_billtype.equals("Parts")) {
											StrSql += " jc_bill_cash_parts = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Tyre")) {
											StrSql += " jc_bill_cash_parts_tyre = " + totalamount + ","
													+ " jc_bill_cash_parts_tyre_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Oil")) {
											StrSql += " jc_bill_cash_parts_oil = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Battery")) {
											StrSql += " jc_bill_cash_parts_battery = " + totalamount + ","
													+ " jc_bill_cash_parts_battery_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Break")) {
											StrSql += " jc_bill_cash_parts_brake = " + totalamount + ","
													+ " jc_bill_cash_parts_brake_qty = " + quantity + ",";
										}
										if (billcat_billtype.equals("Accessories")) {
											StrSql += " jc_bill_cash_parts_accessories = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Labour")) {
											StrSql += " jc_bill_cash_parts_labour = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Valueadd")) {
											StrSql += " jc_bill_cash_parts_valueadd = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Extwarranty")) {
											StrSql += " jc_bill_cash_parts_extwarranty = " + totalamount + ","
													+ " jc_bill_cash_parts_extwarranty_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Wheelalign")) {
											StrSql += " jc_bill_cash_parts_wheelalign = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Cng")) {
											StrSql += " jc_bill_cash_parts_cng = " + totalamount + ",";
										}
									}
								}
							}
							StrSql += " jc_modified_id = " + jc_entry_id + ","
									+ " jc_modified_date = '" + jc_entry_date + "'"
									+ " WHERE jc_id = " + jc_id;
							SOP("StrSql=parts=" + StrSql);
							stmttx.execute(StrSql);
						}
						crs.close();
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
				error_msg += "Job card is not Present for this RO No. : " + jc_ro_no + "<br>";
			}
		}
	}
	public void AddItem() throws Exception {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			double tax = 0.00;
			tax = (Double.parseDouble(jc_tax_total) / (Double.parseDouble(jc_sell_cost) - Double.parseDouble(jc_discount))) * 100;
			SOP("tax==" + tax);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item"
					+ " (item_name,"
					+ " item_code,"
					+ " item_hsn,"
					+ " item_billcat_id,"
					+ " item_type_id,"
					+ " item_active,"
					+ " item_uom_id,"
					+ " item_alt_uom_id,"
					+ " item_cat_id,"
					+ " item_model_id,"
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
					+ " '" + item_name + "'," // item_name
					+ " '" + item_code + "',"
					+ " '" + item_hsn + "',"
					+ " " + billcat_id + "," // billcat_id
					+ " 3," // item_type_id(Parts)
					+ "	1," // item_active
					+ " 1," // item_uom_id
					+ " 1," // item_alt_uom_id
					+ "	23," // item_cat_id
					+ " 80," // model id
					+ " 1," // item_sales_ledger_id
					+ " 0," // item_salesdiscount_ledger_id
					+ " " + item_salestax1_ledger_id + ","
					+ " " + item_salestax2_ledger_id + ","
					+ " " + item_salestax3_ledger_id + ","
					+ " " + item_salestax4_ledger_id + ","
					+ "''," // item_notes
					+ " " + jc_entry_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP(("strsql==Item==" + StrSql));
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
					+ " (SELECT branch_rateclass_id from " + compdb(comp_id) + "axela_branch " + " where branch_id = " + jc_branch_id + "),"
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
			double tax = 0.00;
			double jctrans_qty = 0.00, jctrans_price = 0.00, jctrans_amount = 0.00;
			double jctrans_netprice = 0.00, jctrans_discountamount = 0.00, jctrans_taxamount = 0.00;

			jctrans_qty = Double.parseDouble(billquantity);
			jctrans_price = Double.parseDouble(jc_sell_price);
			jctrans_amount = Double.parseDouble(jc_sell_cost);
			jctrans_netprice = Double.parseDouble(jc_sell_cost);// without tax from 19/6/17
			jctrans_discountamount = Double.parseDouble(jc_discount);
			jctrans_taxamount = Double.parseDouble(jc_tax_total);

			StrSql = "SELECT item_id, item_salestax1_ledger_id,"
					+ " item_salestax2_ledger_id, item_salestax3_ledger_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item "
					+ " WHERE 1=1"
					+ " AND item_id = " + item_id;
			// SOP("StrSql==fdefdf===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_salestax1_ledger_id = CNumeric(crs.getString("item_salestax1_ledger_id"));
					item_salestax2_ledger_id = CNumeric(crs.getString("item_salestax2_ledger_id"));
					item_salestax3_ledger_id = CNumeric(crs.getString("item_salestax3_ledger_id"));
					item_id = CNumeric(crs.getString("item_id"));
				}
			}
			crs.close();

			tax = (jctrans_taxamount / (jctrans_amount - jctrans_discountamount)) * 100;

			if (!item_salestax1_ledger_id.equals("0") || !item_salestax2_ledger_id.equals("0") ||
					!item_salestax3_ledger_id.equals("0")) {
				StrSql = "SELECT customer_rate"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE  customer_id = " + item_salestax1_ledger_id;
				tax1 = CNumeric(ExecuteQuery(StrSql));

				cgsttaxamt = Double.toString(((Math.round(tax) / 2 * (jctrans_amount - jctrans_discountamount)) / 100));
				sgsttaxamt = cgsttaxamt;
				igsttaxamt = Double.toString(jctrans_taxamount);
				if (billtype_name.equals("Internal")) {
					sgsttaxamt = "0";
					cgsttaxamt = "0";
					igsttaxamt = "0";
				}
				if ((billtype_name.equals("Cash") || billtype_name.equals("Warranty") || billtype_name.equals("Insurance"))) {
					// SOP("trans=2=");
					if (!(Math.round(tax) / 2 == Math.round(Double.parseDouble(tax1)))) {
						error_msg += "Tax configuration is not matching for the item: " + item_name + " (" + item_code + ")!" + "<br>";
					}
				}
				if (error_msg.equals("")) {
					if (jctransdelete == 0) {
						StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
								+ " WHERE jctrans_jc_id =" + jc_id;
						// SOP("StrSql==" + StrSql);
						stmttx.addBatch(StrSql);
						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
								+ " SET jc_bill_cash_parts = 0.00,"
								+ " jc_bill_cash_parts_tyre_qty = 0.00,"
								+ " jc_bill_cash_parts_tyre = 0.00,"
								+ " jc_bill_cash_parts_oil = 0.00,"
								+ " jc_bill_cash_parts_battery_qty = 0.00,"
								+ " jc_bill_cash_parts_battery = 0.00,"
								+ " jc_bill_cash_parts_brake_qty = 0.00,"
								+ " jc_bill_cash_parts_brake = 0.00,"
								+ " jc_bill_cash_parts_accessories = 0.00,"
								+ " jc_bill_cash_parts_valueadd = 0.00,"
								+ " jc_bill_cash_parts_extwarranty_qty = 0.00,"
								+ " jc_bill_cash_parts_extwarranty = 0.00,"
								+ " jc_bill_cash_parts_wheelalign = 0.00,"
								+ " jc_bill_cash_parts_cng = 0.00,"
								+ " jc_bill_cash_labour_tyre_qty = 0.00,"
								+ " jc_bill_cash_labour_tyre = 0.00,"
								+ " jc_bill_cash_labour_oil = 0.00,"
								+ " jc_bill_cash_labour_battery_qty = 0.00,"
								+ " jc_bill_cash_labour_battery = 0.00,"
								+ " jc_bill_cash_labour_brake_qty = 0.00,"
								+ " jc_bill_cash_labour_brake = 0.00,"
								+ " jc_bill_cash_labour_accessories = 0.00,"
								+ " jc_bill_cash_labour_valueadd = 0.00,"
								+ " jc_bill_cash_labour_extwarranty_qty = 0.00,"
								+ " jc_bill_cash_labour_extwarranty = 0.00,"
								+ " jc_bill_cash_labour_wheelalign = 0.00,"
								+ " jc_bill_cash_labour_cng = 0.00"
								+ " WHERE jc_id = " + jc_id;
						stmttx.addBatch(StrSql);
						jctransdelete = 0;
					}
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
							+ " " + jctrans_billtype_id + ","// jctrans_billtype_id
							+ " 1," // jctrans_stock
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
							+ " " + jctrans_billtype_id + ","// jctrans_billtype_id
							+ " 1" // jctrans_dc
							+ ")";
					// SOP(("StrSql==Discount==" + StrSql));
					stmttx.addBatch(StrSql);
					if (!item_salestax1_ledger_id.equals("0")) {
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
								+ " " + sgsttaxamt + "," // jctrans_amount;
								+ " " + jctrans_billtype_id + ","// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax1==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					if (!item_salestax2_ledger_id.equals("0")) {
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
								+ " " + jctrans_billtype_id + ","// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax2==" + StrSql);
						stmttx.addBatch(StrSql);
					}
					if (!item_salestax3_ledger_id.equals("0")) {
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
								+ " " + jctrans_billtype_id + ","// jctrans_billtype_id
								+ " '" + ToLongDate(kknow()) + "')";
						// SOP("StrSql==Tax3==" + StrSql);
						stmttx.addBatch(StrSql);
					}
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

			} else {
				error_msg += "Tax is not configured for the item: " + item_name + " (" + item_code + ")!" + "<br>";
			}
			// SOP("propcount==" + propcount);
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
			double tax = 0.00;
			double vouchertrans_qty = 0.00, vouchertrans_price = 0.00, vouchertrans_amount = 0.00, amount = 0.00;
			double vouchertrans_netprice = 0.00, vouchertrans_discountamount = 0.00, vouchertrans_taxamount = 0.00;
			String vouchertrans_rowcount = "", vouchertrans_option_id = "";

			vouchertrans_qty = Double.parseDouble(billquantity);
			vouchertrans_price = Double.parseDouble(jc_sell_price);
			vouchertrans_amount = Double.parseDouble(jc_sell_cost);
			vouchertrans_netprice = Double.parseDouble(jc_sell_cost);
			vouchertrans_discountamount = Double.parseDouble(jc_discount);
			vouchertrans_taxamount = Double.parseDouble(jc_tax_total);

			// Main Item entry in jc_trans table
			// vouchertrans_rowcount = CNumeric(ExecuteQuery("SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) + 1"
			// + " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
			// + " WHERE vouchertrans_voucher_id =" + voucher_id));
			// vouchertrans_option_id = vouchertrans_rowcount;
			// SOP(("vouchertransdelete==" + vouchertransdelete);
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
				}
			}
			crs.close();
			tax = (vouchertrans_taxamount / (vouchertrans_amount - vouchertrans_discountamount)) * 100;
			cgsttaxamt = Double.toString(((Math.round(tax) / 2 * (vouchertrans_amount - vouchertrans_discountamount)) / 100));
			sgsttaxamt = cgsttaxamt;
			if (billtype_name.equals("Dealer Paid") || billtype_name.equals("Billable")) {
				sgsttaxamt = "0";
				cgsttaxamt = "0";
			}
			if (!item_salestax1_ledger_id.equals("0") || !item_salestax2_ledger_id.equals("0") ||
					!item_salestax3_ledger_id.equals("0")) {
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
						+ " 1," // jctrans_customer_id
						+ " " + jctrans_location_id + ","// vouchertrans_location_id
						+ " " + item_id + ","
						+ " 0," // jctrans_discount
						+ " 0," // jctrans_tax
						+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
						+ "	0," // jctrans_option_id
						+ " " + vouchertrans_qty + ","
						+ " " + vouchertrans_price + ","
						+ " " + vouchertrans_amount + ","
						+ " " + vouchertrans_netprice + ","
						+ " " + vouchertrans_discountamount + ","
						+ " " + vouchertrans_taxamount + ","
						+ " " + vouchertrans_qty + ","
						+ " 1," // jctrans_alt_uom_id
						+ " " + vouchertrans_price + ","
						+ " 1," // jctrans_convfactor
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 0" // jctrans_dc
						+ ")";
				// SOP(("StrSql==Voucher Main Item==" + StrSql));
				stmttx.addBatch(StrSql);

				// Discount entry in jc_trans table
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
						+ " 2," // jctrans_customer_id
						+ " " + item_id + ","
						+ " 1," // jctrans_discount
						+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
						+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
						+ " " + vouchertrans_discountamount + ",";// jctrans_price
				if (!(vouchertrans_amount == 0.00)) {
					StrSql += " " + df1.format((vouchertrans_discountamount / vouchertrans_amount * 100)) + ",";
				} else {
					StrSql += "0,";// vouchertrans_discount_perc
				}
				StrSql += " " + vouchertrans_discountamount + "," // jctrans_amount
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 1" // vouchertrans_dc
						+ ")";
				// SOP(("StrSql==Discount==" + StrSql));
				stmttx.addBatch(StrSql);

				// Tax entry in jc_trans table
				if (!item_salestax1_ledger_id.equals("0")) {
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
				if (!item_salestax2_ledger_id.equals("0")) {
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
				if (!item_salestax3_ledger_id.equals("0")) {
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
							+ " " + vouchertrans_taxamount + "," // vouchertrans_price
							+ " " + vouchertrans_taxamount + "," // vouchertrans_amount
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql==voucher Tax3==" + StrSql);
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