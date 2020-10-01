//Shivaprasad 6/07/2015   
package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class JobCard_User_Import_Ford extends Connect {
	public String StrSql = "";
	public String msg = "", emp_id = "0";
	public String comp_id = "0", session_id = "0";
	public String savePath = "", importdocformat = "", format_ford = "", location_id = "";
	public long docsize;
	public String[] docformat;
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, partscount = 0;;
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String BranchAccess = "";
	public String brand_id = "7", jc_branch_id = "0";
	public String upload = "";
	public String item_code = "";
	public String jc_id = "0";
	public String jc_jccat_name = "";
	public String jc_reg_no = "0", jc_ro_no = "", error_msg = "";
	public String jc_error_msg = "", jcerrormsg = "";
	public int jcnocount = 0, jccheck = 0;
	public int count = 0, ronumcount = 1, jctransdelete = 0, vouchertransdelete = 0, stockcheck = 1;
	public int labourcolumnLength = 0, partscolumnLength = 0, techniciancolumnLength = 0, incadeacolumnLength = 0;
	HashMap<String, String> mapronum = new HashMap<String, String>();
	HashMap<String, String> checkjcno = new HashMap<String, String>();
	HashMap<String, String> billtypecheck = new HashMap<String, String>();
	HashMap<Integer, String> checkstock = new HashMap<Integer, String>();
	public String customer_id = "";
	public String customer_name = "", contact_address1 = "", contact_address2 = "";
	public String contact_id = "";
	public String contact_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_phone1 = "";
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
	//
	// Vehicle Variables
	public String veh_id = "0";
	public String interior = "", exterior = "";
	public String item_id = "0", item_name = "", item_cat_id = "0", model_brand_id = "0", partdesc = "", catcheck = "", billcattype = "";
	public String veh_engine_no = "";
	public String veh_modelyear = "";
	public String veh_sale_date = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0";
	public String soe_id = "0", sob_id = "0";
	public String veh_variant_id = "0";
	public String veh_loanfinancer = "", veh_status_id = "0", veh_status_date = "";

	// Jobcard Variables
	public String jc_chassis_no = "";
	public String jc_jctype_id = "0", jc_notes = "", jc_jctype_name = "", jc_jccat_id = "0";
	public String jc_advice = "";
	public String jc_time_in = "0", jc_time_promised = "", jc_time_ready = "", jc_time_out = "";
	public String jc_kms = "0", jc_bill_cash_no = "";
	public String jc_emp_id = "0", jc_emp_name = "";
	public String jc_technician_emp_id = "0", jc_technician_emp_name = "";
	public String day = "", month = "", year = "", servicedueyear = "";
	public String veh_service_duekms = "", veh_service_duedate = "";
	public String jc_bill_cash_date = "";
	public String billquantity = "0", jc_sell_price = "";
	public String land_cost = "", jc_sell_cost = "", landed_value = "", jc_grandtotal = "0", jc_discamt = "0";
	public String actual_amount = "", jc_tax_total = "", jctaxtotal = "", jc_discount = "", jc_netprice = "";
	public String jc_bill_cash_parts = "0.0", jc_bill_cash_parts_tyre = "0.0", jc_bill_cash_parts_oil = "0.0";
	public String jc_bill_cash_parts_accessories = "0.0", jc_bill_cash_labour = "0";
	public String jc_bill_cash_parts_valueadd = "0.0", jc_bill_cash_labour_discamt = "0", jc_bill_insur_discamt = "0";
	public double jctax1 = 0.0, jctax2 = 0.0, jctax3 = 0.0, jctax4 = 0.0;
	public String jctrans_rowcount = "0", jctrans_option_id = "0";

	public DecimalFormat df = new DecimalFormat("0.00");
	public DecimalFormat df1 = new DecimalFormat("0.0000");
	public String veh_chassis_no = "", jc_variant_id = "0";
	public String price_id = "0", billtype_name = "", jctrans_billtype_id = "0", jctype = "", billcat_name = "", billcat_id = "0", billcat_billtype = "", item_type_id = "";
	double totalamount = 0.0, quantity = 0.0;
	public Connection conntx = null;
	public Statement stmttx = null;
	JobCard_User_Import obj = new JobCard_User_Import();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				jc_entry_id = emp_id;
				jc_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button"));
				session_id = PadQuotes(request.getParameter("txt_session_id"));
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
					location_id = str1[1];
					format_ford = str1[2];
					// SOP("jc_branch_id==" + str1[0]);
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
											if (format_ford.equals("Incadea Format")) {
												msg += "<br>" + partscount + " Item(s) Imported successfully!";
											}
											if (!jc_error_msg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + jc_error_msg;
											}
											if (format_ford.equals("Parts Format") || format_ford.equals("Incadea Format")) {
												// UpdateJobcardDetails(checkjcno);
												SOP("location_id=====" + location_id);
												// CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread("", location_id, comp_id, "0", "yes");
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
			response.sendRedirect("../service/jobcard-user-import-ford.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(jc_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (CNumeric(location_id).equals("0")) {
			msg = msg + "<br>Select Location!";
		}
		if (format_ford.equals("")) {
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
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls // type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is // .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			if (rowLength > 5000) {
				rowLength = 5000;
			}
			int h = 0;
			int j = 0;
			propcount = 0;
			partscount = 0;
			obj.propcount = 0;
			obj.brand_id = brand_id;
			labourcolumnLength = 24;
			partscolumnLength = 20;
			techniciancolumnLength = 3;
			incadeacolumnLength = 37;
			SOP("columnLength==" + columnLength);
			// SOP("labourcolumnLength==" + labourcolumnLength);
			// SOP("format_ford==" + format_ford);
			if (format_ford.equals("Labour Format") && columnLength != labourcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else if (format_ford.equals("Parts Format") && columnLength != partscolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else if (format_ford.equals("Technician Format") && columnLength != techniciancolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else if (format_ford.equals("Incadea Format") && columnLength != incadeacolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				// SOP("msg==" + msg);
				for (j = 1; j < rowLength + 1; j++) {
					CheckForm();
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {
						if (format_ford.equals("Labour Format")) {
							// jobcard no
							if (h == 0)
							{
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card No.")) {
									jc_ro_no = "";
								}
								// SOP("jc_ro_no==main==" + jc_ro_no);
							}

							// job card open date
							if (h == 1)
							{
								jc_time_in = "";
								jc_time_in = PadQuotes(sheetData[j][h]);
								if (jc_time_in.equals("null") || jc_time_in.equals("0") || jc_time_in.equals("Job Card Open Date")) {
									jc_time_in = "";
								}
								// SOP("job card open date==" + jc_time_in);
							}

							// Job Card Reg Number
							if (h == 2) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
								if (jc_reg_no.equals("null") || jc_reg_no.equals("0") || jc_reg_no.equals("Reg. Num")) {
									jc_reg_no = "";
								}
								// SOP("jc_reg_no==" + jc_reg_no);
							}

							// Chassis Number
							if (h == 3) {
								jc_chassis_no = "";
								jc_chassis_no = PadQuotes(sheetData[j][h]);
								if (jc_chassis_no.equals("null") || jc_chassis_no.equals("0") || jc_chassis_no.equals("Chassis No.")) {
									jc_chassis_no = "";
								}
								// SOP("jc_chassis_no==" + jc_chassis_no);
							}

							// SVAR
							if (h == 4) {

							}
							// Variant Code
							if (h == 5) {
								item_name = "";
								if (item_name.contains("'")) {
									item_name = item_name.replace("'", "");
								}
								item_name = PadQuotes(sheetData[j][h]);

							}
							// Mileage
							if (h == 6) {
								jc_kms = "0";
								jc_kms = PadQuotes(sheetData[j][h]);
								if (jc_kms.equals("null") || jc_kms.equals("") || jc_kms.equals("Mileage")) {
									jc_kms = "0";
								}
								// SOP("jc_kms==" + jc_kms);
							}

							// Sale Date
							if (h == 7) {
								veh_sale_date = "";
								veh_sale_date = PadQuotes(sheetData[j][h]);
								if (veh_sale_date.equals("null") || veh_sale_date.equals("0") || veh_sale_date.equals("Sale Date")) {
									veh_sale_date = "";
								}
								// SOP("veh_sale_date==" + veh_sale_date);
							}

							// Customer Catagory
							if (h == 8) {// ///
								jc_jccat_name = "";
								jc_jccat_name = PadQuotes(sheetData[j][h]);
								if (jc_jccat_name.equals("null") || jc_jccat_name.equals("Customer Category")) {
									jc_jccat_name = "";
								}
								// SOP("jc_jccat_name==" + jc_jccat_name);
							}

							// Service Advisor
							if (h == 9) {
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
									jc_emp_name = "";
								}
							}

							// party_name(customer)
							if (h == 10) {
								customer_name = "";

								customer_name = PadQuotes(sheetData[j][h]);
								if (customer_name.equals("null") || customer_name.equals("0") || customer_name.equals("Customer Name")) {
									customer_name = "";
								}
								if (!customer_name.equals("")) {
									contact_name = customer_name;
									// SOP("customer_name==" + customer_name);
								}

							}
							// address1
							if (h == 11) {
								contact_address = "";
								contact_address = PadQuotes(sheetData[j][h]);
								// // SOP("customer_address==" + customer_address);
								if (contact_address.equals("null") || contact_address.equals("0") || contact_address.equals("Address")) {
									contact_address = "";
								}
							}

							// address2
							if (h == 12) {
								contact_address1 = "";
								contact_address1 = PadQuotes(sheetData[j][h]);
							}

							// address3
							if (h == 13) {
								contact_address2 = "";
								contact_city = "";
								contact_address2 = PadQuotes(sheetData[j][h]);
								contact_city = contact_address2;
								// String address[] = contact_address2.split(" ");
								// for (int i = 0; i < address.length; i++) {
								// contact_city_id = ExecuteQuery("SELECT city_id FROM " + compdb(comp_id) + "axela_city"
								// + " WHERE city_name='" + address[i] + "'"
								// + " AND city_id !=0"
								// + " ORDER BY city_id LIMIT 1") + "";
								// // SOP("address[i]==" + address[i]);
								// }
								// SOP("contact_city_id==" + contact_city_id);
							}

							// Res_Phone
							if (h == 14) {
								contact_phone1 = "";
								contact_phone1 = PadQuotes(sheetData[j][h]);
								// SOP("contact_phone1==" + contact_phone1);
								if (!contact_phone1.equals("") && contact_phone1.contains("-")) {
									contact_phone1 = contact_phone1.substring(1, contact_phone1.length());
									contact_phone1 = "91-" + contact_phone1;
								}
								if (!contact_phone1.equals("") && !contact_phone1.contains("91-")) {
									contact_phone1 = "91-" + contact_phone1;
								}
								if (!IsValidPhoneNo11(contact_phone1)) {
									contact_phone1 = "";
								}
							}

							// Mobile Number
							if (h == 15) {
								contact_mobile1 = "";
								contact_mobile1 = PadQuotes(sheetData[j][h]);

							}

							// Email Id
							if (h == 16) {
								contact_email1 = "";
								contact_email1 = PadQuotes(sheetData[j][h]);
								if (contact_email1.equals("null") || contact_email1.equals("0") || contact_email1.equals("Email ID.")) {
									contact_email1 = "";
								}

							}

							// party name
							if (h == 17) {

							}

							// Date Of Birth....
							if (h == 18) {
								contact_dob = "";
								contact_dob = PadQuotes(sheetData[j][h]);
								if (contact_dob.equals("null") || contact_dob.equals("0") || contact_dob.equals("Date of Birth")) {
									contact_dob = "";
								}
								// SOP("contact_dob===" + contact_dob);
							}

							// Service Type....
							if (h == 19) {
								jc_jctype_name = "";
								jc_jctype_name = PadQuotes(sheetData[j][h]);
								// SOP("jc_jctype_name==" + jc_jctype_name);
								if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("Service Type")) {
									jc_jctype_name = "";
								}
							}

							// Bill Number....
							if (h == 20) {
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
							}

							// Bill Date....
							if (h == 21) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = PadQuotes(sheetData[j][h]);
								// SOP("jc_bill_cash_date==" + jc_bill_cash_date);
							}

							// Labor Amount...
							if (h == 22) {
								jc_bill_cash_labour = "";
								jc_bill_cash_labour = sheetData[j][h] + "";
								SOP("jc_bill_cash_labour==" + jc_bill_cash_labour);
							}

							// parts amount
							if (h == 23) {
								jc_bill_cash_parts = "";
								jc_bill_cash_parts = CNumeric(sheetData[j][h]);
								// SOP("jc_bill_cash_parts==" + jc_bill_cash_parts);
							}

						}

						// Start Here Parts Feilds
						if (format_ford.equals("Parts Format")) {

							// get all parts feilds from Parts-Format xl-sheet

							// Ro_number
							if (h == 0) {
								jc_id = "0";
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card No.")) {
									error_msg += " Ro.No. should not to be empty !<br> ";
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
										jctransdelete = 0;
										vouchertransdelete = 0;

									}
								}
								if (ronumcount >= 1 && jctransdelete == 0) {
									mapronum.put("ronum-" + ronumcount, jc_ro_no);
								}

								// SOP("jc_ro_no==main==" + jc_ro_no);
							}

							// Vin_number
							if (h == 1) {
								jc_chassis_no = "";
								jc_chassis_no = PadQuotes(sheetData[j][h]);
								if (jc_chassis_no.equals("null") || jc_chassis_no.equals("0") || jc_chassis_no.equals("Chassis No.")) {
									jc_chassis_no = "";
								}
								// SOP("jc_chassis_no==parts==" + jc_chassis_no);
							}

							// RCATE(Service Type)
							if (h == 3) {
								jc_jctype_name = "";
								jc_jctype_id = "0";
								jc_jctype_name = PadQuotes(sheetData[j][h]);
								// SOP("jc_jctype_name==" + jc_jctype_name);
								if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("Service Type")) {
									jc_jctype_name = "";
								}
							}

							// Bill_Date
							if (h == 5) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = ConvertShortDateToStr(sheetData[j][h]);
								// SOP("jc_bill_cash_date==tech==" + jc_bill_cash_date);
							}

							// PART_NUM(Item_code)
							if (h == 7) {
								item_code = "";
								item_code = PadQuotes(sheetData[j][h]);
								if (item_code.equals("")) {
									error_msg += " Part No. should not to be empty !<br> ";
								}
							}

							// PART_DESC(item_name)
							if (h == 8) {
								partdesc = "";
								item_id = "0";
								jc_variant_id = "0";
								if (partdesc.contains("'")) {
									partdesc = partdesc.replace("'", "");
								}
								partdesc = PadQuotes(sheetData[j][h]);
								// SOP("partdesc==" + partdesc);
								if (partdesc.equals("")) {
									error_msg += " Part Description should not to be empty !<br> ";
								} else if (!partdesc.equals("")) {
									if (partdesc.contains("(")) {
										partdesc = partdesc.replace("(", "&#40;");
									}
									if (partdesc.contains(")")) {
										partdesc = partdesc.replace(")", "&#41;");
									}
									StrSql = "SELECT item_id"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + " axela_inventory_item_model ON model_id = item_model_id"
											+ " WHERE 1 = 1"
											+ " AND item_code = '" + item_code + "'"
											+ " AND model_brand_id =" + brand_id
											+ " LIMIT 1";
									// // StrSql = "SELECT  model_id"
									// // + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
									// // + " WHERE model_name = 'Ford Parts'";
									// // model_id = CNumeric(ExecuteQuery(StrSql));
									//
									// StrSql = "SELECT item_id"
									// + " FROM " + compdb(comp_id) + "axela_inventory_item"
									// + " WHERE 1=1"
									// + " AND item_name = '" + partdesc + "'";
									// if (!item_code.equals("")) {
									// StrSql += " AND item_code = '" + item_code + "'";
									// }
									// StrSql += " LIMIT 1";
									SOP("item==" + StrSql);
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

							// category
							if (h == 9) {
								billcat_id = "0";
								billcat_name = "";
								billcat_name = PadQuotes(sheetData[j][h]);
								if (billcat_name.equals("null") || billcat_name.equals("")) {
									error_msg += "Part Category is Empty for this Ro No. ==>" + jc_ro_no + "<br>";
								}
								if (!billcat_name.equals("")) {
									if (isNumeric(billcat_name) && billcat_id.equals("0")) {
										billcat_id = checkBillCode(billcat_name);
									} else if (billcat_id.equals("0")) {
										StrSql = "SELECT billcat_id"
												+ " FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
												+ " WHERE 1=1"
												+ " AND billcat_brand_id = " + brand_id
												+ " AND (billcat_name = '" + billcat_name + "'"
												+ " OR billcat_code= '" + billcat_name + "')";
										// SOP(("billcat_id StrSql==" + StrSql);
										billcat_id = CNumeric(ExecuteQuery(StrSql));
									}

								} else if (billcat_id.equals("0") || billcat_name.equals("null")) {
									error_msg += "Invalid Part Category for this Ro No. ==>" + jc_ro_no + "<br>";
								}
							}

							// BILL_QTY
							if (h == 11) {
								billquantity = "";
								billquantity = PadQuotes(sheetData[j][h]);
								// SOP("billquantity==" + billquantity);
							}

							// Service_advisor
							if (h == 12) {
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
									jc_emp_name = "";
								}
								if (!jc_emp_name.equals("")) {
									if (jc_emp_id.equals("0")) {
										StrSql = "SELECT emp_id"
												+ " FROM " + compdb(comp_id) + "axela_emp"
												+ " WHERE emp_name LIKE '%" + jc_emp_name + "%'"
												+ " AND emp_service = 1"
												+ " AND emp_branch_id = " + jc_branch_id
												+ " LIMIT 1";
										// SOP("StrSql==service advisor tech==" + StrSql);
										jc_emp_id = CNumeric(ExecuteQuery(StrSql));
										// SO// SOP("_emp_id==tech==" + jc_emp_id);
									}
								}
								if (jc_emp_id.equals("0") || jc_emp_name.equals("")) {
									error_msg += "<br> No Service advisor Found with name: " + jc_emp_name + " For RefNo:" + jc_ro_no;

								}

								// SOP("jc_emp_name==" + jc_emp_name);
							}

							// tax(total parts amount)
							if (h == 13) {
								jc_tax_total = "";
								jc_tax_total = PadQuotes(sheetData[j][h]);
								// SOP("total_parts_price==" + total_parts_price);
							}

							// sell_price
							if (h == 14) {
								jc_sell_price = "";
								jc_sell_price = PadQuotes(sheetData[j][h]);
								// SOP("sell_price==" + jc_sell_price);

							}

							// Landed_cost
							if (h == 15) {
								land_cost = "";
								land_cost = PadQuotes(sheetData[j][h]);
								// SOP("land_cost==" + land_cost);

							}

							// SELL_COST
							if (h == 16) {
								jc_sell_cost = "";
								jc_sell_cost = PadQuotes(sheetData[j][h]);
								// SOP("sell_cost==" + jc_sell_cost);
							}

							// LANDED_VALUE
							if (h == 17) {
								landed_value = "";
								landed_value = PadQuotes(sheetData[j][h]);
								// SOP("landed_value==" + landed_value);

							}

							// DISCOUNT_AMT
							if (h == 18) {
								jc_bill_cash_labour_discamt = "";
								jc_bill_cash_labour_discamt = PadQuotes(sheetData[j][h]);
								// SOP("discount==" + jc_bill_cash_discamt);

							}

							// ACTUAL_SELL
							if (h == 19) {
								actual_amount = "";
								actual_amount = PadQuotes(sheetData[j][h]);
								// SOP("actual_amount==" + actual_amount);
							}

						}

						if (format_ford.equals("Technician Format")) {

							// Bill_date
							if (h == 0) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = ConvertShortDateToStr(sheetData[j][h]);
								// SOP("jc_bill_cash_date==tech==" + jc_bill_cash_date);
							}

							// Ro_Num
							if (h == 1) {
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card No.")) {
									jc_ro_no = "";
								}
								if (jc_ro_no.equals(""))
								{
									error_msg += "<br>" + " Ro_Number should not empty ";
								}
								// SOP("==main==" + );
							}

							// EMP_NAME
							if (h == 2) {
								jc_emp_id = "0";
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								if (!jc_emp_name.equals("")) {
									if (jc_emp_id.equals("0")) {
										StrSql = "SELECT emp_id"
												+ " FROM " + compdb(comp_id) + "axela_emp"
												+ " WHERE emp_name like '%" + jc_emp_name + "%'"
												+ " AND emp_service = 1"
												+ " AND emp_technician = 1"
												+ " AND emp_branch_id = " + jc_branch_id
												+ " LIMIT 1";
										// SOP("StrSql==service advisor tech==" + StrSql);
										jc_emp_id = CNumeric(ExecuteQuery(StrSql));
										// SOP("jc_emp_id==tech==" + jc_emp_id);
									}
								}
								if (jc_emp_id.equals("0") || jc_emp_name.equals("")) {
									error_msg += "<br> No Service advisor Found with name: " + jc_emp_name + " For RefNo:" + jc_ro_no;

								}

							}
						}
						if (format_ford.equals("Incadea Format")) {
							// document type

							if (h == 1) {

							}
							// Invoice No
							if (h == 2) {
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
								// SOP("jc_bill_cash_no==" + jc_bill_cash_no);
							}
							// Invoice Date
							if (h == 3) {
								jc_bill_cash_date = "";
								jc_bill_cash_date = PadQuotes(sheetData[j][h]);
								// SOP("jc_bill_cash_date=0=" + jc_bill_cash_date);
								if (isValidDateFormatShort(jc_bill_cash_date)) {
									jc_bill_cash_date = ConvertShortDateToStr(jc_bill_cash_date);
								}
								// SOP("jc_bill_cash_date=1=" + jc_bill_cash_date);

							}
							// R/O No
							if (h == 4) {
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								// SOP("jc_ro_no==" + jc_ro_no);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card No.")) {
									error_msg += " Ro.No. should not to be empty !<br> ";
								}
							}
							// R/O Date
							if (h == 5) {
								jc_time_in = "";
								jc_time_in = PadQuotes(sheetData[j][h]);
								// SOP("jc_time_in==" + jc_time_in);
							}
							// Repair Category
							if (h == 6) {
								jc_jccat_name = "";
								jc_jccat_name = PadQuotes(sheetData[j][h]);
								if (jc_jccat_name.equals("null") || jc_jccat_name.equals("Customer Category")) {
									jc_jccat_name = "";
								}
								if (!jc_jccat_name.equals("")) {
									jc_jctype_name = jc_jccat_name;
								}
							}
							// Service Advisor Name
							if (h == 7) {
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								// SOP("jc_emp_name==from excell==" + jc_emp_name);
								if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
									jc_emp_name = "";
								}
							}
							// Resource Name
							if (h == 8) {
								jc_technician_emp_name = "";
								jc_technician_emp_name = PadQuotes(sheetData[j][h]);
							}
							// Bill Type
							if (h == 9) {
								billtype_name = "";
								billtype_name = PadQuotes(sheetData[j][h]);
								if (billtype_name.equals("Customer")) {
									billtype_name = "Cash";
									if (billtypecheck.containsKey(billtype_name)) {
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
								} else if (billtype_name.equals("Internal")) {
									billtype_name = "Internal";
									if (billtypecheck.containsKey(billtype_name)) {
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
								} else if (billtype_name.equals("Warranty")) {
									billtype_name = "Warranty";
									if (billtypecheck.containsKey(billtype_name)) {
										jctrans_billtype_id = billtypecheck.get(billtype_name);
									}
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
								if (billtype_name.equals("")) {
									error_msg += " Bill Type should not to be empty!<br>";
								}

							}
							// Type
							if (h == 10) {
								jctype = "";
								jctype = PadQuotes(sheetData[j][h]);
								if (jctype.equals("Ext. Service")) {
									jctype = "Labor"; // Temporary condition
								}
								if (jctype.equals("Labor")) {
									jctype = "Labor";
								}
								if (jctype.equals("Item")) {
									jctype = "Item";
								}
								if (jctype.equals("")) {
									error_msg += " Job Card Type should not to be empty!<br> ";
								}

							}
							// Part / Labor Group
							if (h == 11) {
								billcat_id = "0";
								billcat_name = "";
								billcattype = "";
								billcat_name = PadQuotes(sheetData[j][h]);
								if (billcat_name.equals("null") || billcat_name.equals("")) {
									if (jctype.equals("Labor")) {
										billcat_name = "Labour";
									} else if (jctype.equals("Item")) {
										billcat_name = "Others";
									}
								}
								// SOP("billcat_name ==" + billcat_name);
								if (!billcat_name.equals("")) {
									if (billcat_name.equals("Ext. Service")) {
										billcat_name = "Labour"; // Temporary condition
									}
									if (isNumeric(billcat_name)) {
										billcat_id = checkBillCode(billcat_name);
									} else if (billcat_id.equals("0")) {
										StrSql = "SELECT billcat_id, billcat_billtype"
												+ " FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
												+ " WHERE 1=1"
												+ " AND billcat_brand_id = " + brand_id
												+ " AND (billcat_name = '" + billcat_name + "'"
												+ " OR billcat_code= '" + billcat_name + "')";
										// SOP("billcat_id StrSql==" + StrSql);
										CachedRowSet crs = processQuery(StrSql, 0);
										if (crs.isBeforeFirst()) {
											while (crs.next()) {
												billcat_id = crs.getString("billcat_id");
												billcattype = crs.getString("billcat_billtype");
											}
										}
										crs.close();
									}
									SOP("billcat_name==2=" + billcat_name);
									SOP("jctype==2=" + jctype);
									if (billcat_id.equals("0") || billcat_name.equals("null")) {
										error_msg += "Invalid Part Category for this Ro No. ==>" + jc_ro_no + "<br>";
									}
									// if (jctype.equals("Labor")) {
									// if (!billcattype.equals("Labour")) {
									// SOP("1256");
									// error_msg += "Invalid Part Category with Job Card Type for Part Group: " + billcat_name + "<br>";
									// }
									// }
								}
							}
							// No.
							if (h == 12) {
								item_code = "";
								item_code = PadQuotes(sheetData[j][h]);
								if (item_code.equals("")) {
									error_msg += " Part No. should not to be empty !<br> ";
								}
							}
							// Description
							if (h == 13) {
								partdesc = "";
								item_id = "0";
								jc_variant_id = "0";
								item_type_id = "0";
								partdesc = PadQuotes(sheetData[j][h]);
								if (partdesc.equals("")) {
									error_msg += " Part Description should not to be empty !<br> ";
								} else if (!partdesc.equals("")) {
									if (partdesc.contains("(")) {
										partdesc = partdesc.replace("(", "&#40;");
									}
									if (partdesc.contains(")")) {
										partdesc = partdesc.replace(")", "&#41;");
									}
									StrSql = "SELECT item_id, item_type_id "
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + " axela_inventory_item_model ON model_id = item_model_id"
											+ " WHERE 1 = 1"
											+ " AND item_code = '" + item_code + "'"
											+ " AND model_brand_id =" + brand_id
											+ " LIMIT 1";

									// if (Integer.getInteger(item_id) > 1) {
									// error_msg += " Part No.is associated with another item!<br> ";
									// }

									// StrSql = "SELECT  model_id"
									// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
									// + " WHERE model_name = 'Ford Parts'";
									// model_id = CNumeric(ExecuteQuery(StrSql));

									// StrSql = "SELECT item_id"
									// + " FROM " + compdb(comp_id) + "axela_inventory_item"
									// + " WHERE 1=1"
									// + " AND item_name = '" + partdesc + "'";
									// if (!item_code.equals("")) {
									// StrSql += " AND item_code = '" + item_code + "'";
									// }
									// StrSql += " LIMIT 1";
									SOP("item==" + StrSql);
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.isBeforeFirst()) {
										while (crs.next()) {
											item_id = CNumeric(crs.getString("item_id"));
											item_type_id = CNumeric(crs.getString("item_type_id"));
										}
										if (item_type_id.equals("3") && jctype.equals("Labor")) {
											error_msg += " This " + partdesc + "Item is Associated with Part type!<br> ";
										}
										if (item_type_id.equals("4") && jctype.equals("Item")) {
											error_msg += "This " + partdesc + " Item is Associated with Labour type!<br> ";
										}
									}
									crs.close();

								}
							}
							// Quantity
							if (h == 14) {
								billquantity = "";
								billquantity = PadQuotes(sheetData[j][h]);
							}
							// Amount
							if (h == 15) {
								jc_sell_price = "";
								jc_sell_price = PadQuotes(sheetData[j][h]);
							}
							// Discount Amount
							if (h == 16) {
								jc_bill_cash_labour_discamt = "";
								jc_bill_cash_labour_discamt = PadQuotes(sheetData[j][h]);
							}
							// Net Amount
							if (h == 17) {
								jc_sell_cost = "";
								jc_sell_cost = PadQuotes(sheetData[j][h]);

							}
							// Tax Amount
							if (h == 18) {
								jctax1 = 0.0;
								jctax1 = Double.parseDouble(sheetData[j][h]);
							}
							// Service Tax Amount
							if (h == 19) {
								jctax2 = 0.0;
								jctax1 = Double.parseDouble(sheetData[j][h]);
							}
							// Swachh Bharath Tax Amount
							if (h == 20) {
								jctax3 = 0.0;
								jctax1 = Double.parseDouble(sheetData[j][h]);
							}
							// Krishi Kalyan Cess
							if (h == 21) {
								jc_tax_total = "";
								jctax4 = 0.0;
								jctax4 = Double.parseDouble(sheetData[j][h]);
								jctaxtotal = Double.toString(jctax1 + jctax2 + jctax3 + jctax4);
								jc_tax_total = jctaxtotal;
							}
							// Invoice Amount
							if (h == 22) {
								jc_netprice = "";
								jc_netprice = PadQuotes(sheetData[j][h]);

							}
							// Bill To Customer No.
							if (h == 23) {
							}
							// Sell To Customer No.
							if (h == 24) {
							}
							// Sell To Customer Name
							if (h == 25) {
								customer_name = "";
								customer_name = PadQuotes(sheetData[j][h]);
							}
							// Address 1
							if (h == 26) {
								contact_address1 = "";
								contact_address1 = PadQuotes(sheetData[j][h]);
							}
							// Address 2
							if (h == 27) {
								contact_address2 = "";
								contact_address2 = PadQuotes(sheetData[j][h]);
								contact_address = contact_address1 + contact_address2;
							}
							// City
							if (h == 28) {
								contact_city = "";
								contact_city = PadQuotes(sheetData[j][h]);
							}
							// State
							if (h == 29) {
								contact_state = "";
								contact_state = PadQuotes(sheetData[j][h]);
							}
							// Pin
							if (h == 30) {
								contact_pin = "";
								contact_pin = PadQuotes(sheetData[j][h]);
							}
							// Email
							if (h == 31) {
								contact_email1 = "";
								contact_email1 = PadQuotes(sheetData[j][h]);
								if (contact_email1.equals("null") || contact_email1.equals("0") || contact_email1.equals("Email ID.")) {
									contact_email1 = "";
								}

							}
							// Mobile No.
							if (h == 32) {
								contact_mobile1 = "";
								contact_mobile1 = PadQuotes(sheetData[j][h]);

							}
							// Vin Number
							if (h == 33) {
								jc_chassis_no = "";
								jc_chassis_no = PadQuotes(sheetData[j][h]);
							}
							// Reg Number
							if (h == 34) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
							}
							// Vehicle Sales Date
							if (h == 35) {
								veh_sale_date = "";
								veh_sale_date = PadQuotes(sheetData[j][h]);
							}

							// Carline
							if (h == 36) {
								item_name = "";
								item_name = PadQuotes(sheetData[j][h]);
							}

						}

					}

					jc_entry_date = ToLongDate(kknow());

					if (!jc_ro_no.equals("") && format_ford.equals("Labour Format")) {
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

					if (format_ford.equals("Technician Format") && !jc_bill_cash_date.equals("") && !jc_ro_no.equals("") && !jc_emp_id.equals("0")) {
						UpdateTechinicianFormat();
						if (!error_msg.equals("")) {
							// SOP("error_msg==" + error_msg);
							jc_error_msg += ++count + "." + " RO Number: " + jc_ro_no + "==><br>" + error_msg;
						}
					}
					if (format_ford.equals("Technician Format") && jc_emp_id.equals("0")) {
						jc_error_msg += "<br>" + "Technician is Empty for this Ro_Number ==>" + jc_ro_no;
					}
					if (format_ford.equals("Parts Format") && !jc_ro_no.equals("") && !jc_chassis_no.equals("")) {
						PartsFormat(request, response);
						if (!error_msg.equals("")) {
							// SOP("error_msg==" + error_msg);
							jc_error_msg += ++count + "." + " RO Number: " + jc_ro_no + "==><br>" + error_msg;
						}
					}
					if (format_ford.equals("Incadea Format")) {
						jcerrormsg = "";
						if (error_msg.equals("")) {
							jc_id = "0";
							if (!jc_ro_no.equals("")) {
								jccheck = 0;
								++jcnocount;
								StrSql = "SELECT jc_id, jc_ro_no"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE 1=1"
										+ " AND jc_ro_no = '" + jc_ro_no + "'"
										+ " AND jc_branch_id = " + jc_branch_id
										+ " LIMIT 1";
								// SOP("jc==" + StrSql);
								CachedRowSet crs = processQuery(StrSql, 0);
								if (crs.isBeforeFirst()) {
									while (crs.next()) {
										jc_id = CNumeric(crs.getString("jc_id"));
										jc_ro_no = crs.getString("jc_ro_no");
									}
								}
								crs.close();
								// SOP("Job Card jc_id " + jc_id);
								// if (!jc_id.equals("0")) {
								// if (checkjcno.containsValue(jc_id)) {
								// SOP("Job Card is already ");
								// // If jccheck is 1 means Job Card is already added, and Parts can be added.
								// jccheck = 1;
								// } else {
								// SOP("Job Card is not added ");
								// // If jccheck is 0 means Job Card is not added, and it has to be added before adding Parts.
								// jccheck = 0;
								// checkjcno.put("jcno" + jcnocount, jc_id);
								// }
								// } else {
								// SOP("Job Card is not added jcid=0 ");
								// checkjcno.put("jcno" + jcnocount, jc_id);
								// }
								if (!jc_id.equals("0")) {
									if (checkjcno.containsValue(jc_ro_no)) {
										// SOP("Job Card is already ");
										// If jccheck is 1 means Job Card is already added, and Parts can be added.
										jccheck = 1;
									} else {
										// SOP("Job Card is not added ");
										// If jccheck is 0 means Job Card is not added, and it has to be added before adding Parts.
										jccheck = 0;
										checkjcno.put("jcno" + jcnocount, jc_ro_no);
									}
								} else {
									// SOP("Job Card is not added jcid=0 ");
									checkjcno.put("jcno" + jcnocount, jc_ro_no);
								}
							}
							SOP("checkjcno===" + checkjcno.toString());
							if (jctype.equals("Labor")) {
								if (jccheck == 0) {
									// SOP("job card first");
									// Add Job Card.
									jc_bill_cash_labour = jc_netprice;
									jcerrormsg += obj.ValidateSheetData(comp_id, customer_name, contact_mobile1, contact_email1,
											contact_phone1, contact_address, contact_dob, contact_anniversary, contact_city, contact_pin,
											veh_engine_no, veh_sale_date, item_name, jc_branch_id,
											jc_reg_no, jc_chassis_no, jc_ro_no, jc_emp_name,
											jc_technician_emp_name, jc_time_in, jc_bill_cash_no, jc_bill_cash_date, jc_jctype_name,
											jc_jccat_name, jc_kms, jc_grandtotal, jc_bill_cash_labour,
											jc_bill_cash_parts, jc_bill_cash_parts_valueadd, jc_bill_cash_parts_oil, jc_bill_cash_parts_tyre,
											jc_bill_cash_parts_accessories, jc_bill_cash_labour_discamt, jc_notes, jc_advice,
											jc_entry_id, jc_entry_date, error_msg);
									propcount = obj.propcount;
									PartsFormat(request, response);
									// SOP("propcount =11=" + propcount);
								} else if (jccheck == 1) {
									// SOP("222");
									// Add Parts for Job Card.
									PartsFormat(request, response);
								}
							} else if (jctype.equals("Item")) {
								if (jccheck == 1) {
									// SOP("333");
									PartsFormat(request, response);
								} else if (jccheck == 0) {
									SOP("parts job card first");
									jc_bill_cash_labour = "0";
									jcerrormsg += obj.ValidateSheetData(comp_id, customer_name, contact_mobile1, contact_email1,
											contact_phone1, contact_address, contact_dob, contact_anniversary, contact_city, contact_pin,
											veh_engine_no, veh_sale_date, item_name, jc_branch_id,
											jc_reg_no, jc_chassis_no, jc_ro_no, jc_emp_name,
											jc_technician_emp_name, jc_time_in, jc_bill_cash_no, jc_bill_cash_date, jc_jctype_name,
											jc_jccat_name, jc_kms, jc_grandtotal, jc_bill_cash_labour,
											jc_bill_cash_parts, jc_bill_cash_parts_valueadd, jc_bill_cash_parts_oil, jc_bill_cash_parts_tyre,
											jc_bill_cash_parts_accessories, jc_bill_cash_labour_discamt, jc_notes, jc_advice,
											jc_entry_id, jc_entry_date, error_msg);
									propcount = obj.propcount;

									if (jcerrormsg.equals("")) {
										PartsFormat(request, response);
									}
									// SOP("propcount =15=" + propcount);
								}
							}
						}
						SOP("error_msg==" + error_msg);
						SOP("jc_error_msg==" + jc_error_msg);
						if (!error_msg.equals("") || !jcerrormsg.equals("")) {
							jc_error_msg += ++count + "." + " RO Number: " + jc_ro_no + "==><br>" + error_msg + jcerrormsg;

							// if (!error_msg.equals("") && jc_error_msg.equals("")) {
							// jc_error_msg += ++count + "." + " RO Number: " + jc_ro_no + "==><br>" + error_msg;
							// } else if (!error_msg.equals("") && !jc_error_msg.equals("")) {
							// jc_error_msg += ++count + "." + " RO Number: " + jc_ro_no + "==><br>" + error_msg;
							// }
						}
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public void PartsFormat(HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!jc_ro_no.equals("")) {
			SOP("parts===");
			// SOP("jc_ro_no==" + jc_ro_no);
			if (format_ford.equals("Incadea Format")) {
				Iterator iterator = mapronum.entrySet().iterator();
				String flag = "false";
				while (iterator.hasNext()) {
					Map.Entry obj = (Map.Entry) iterator.next();
					if (obj.getValue().equals(jc_ro_no)) {
						// SOP("come");
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
				if (ronumcount >= 1 && jctransdelete == 0) {
					mapronum.put("ronum-" + ronumcount, jc_ro_no);

				}
				// SOP("map==" + mapronum);
			}
			try {
				StrSql = "SELECT jc_id, jc_variant_id"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_ro_no = '" + jc_ro_no + "'"
						+ " AND jc_branch_id=" + jc_branch_id;
				SOP("StrSql=======" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						jc_variant_id = crs.getString("jc_variant_id");
						jc_id = CNumeric(crs.getString("jc_id"));
					}
				}
				// if (!checkstock.containsKey(jc_id)) {
				// checkstock.put(stockcheck, jc_id);
				// stockcheck++;
				// }
				crs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Main check for jc
			if (!jc_id.equals("0")) {
				// insert into jc table then get the item_id
				if (item_id.equals("0")) {
					if (!partdesc.equals("")) {
						AddItem();
					}
				}
				// SOP("item_id==" + item_id + "jc_id==" + jc_id);
				// get item_id and insert into trans table
				if (!item_id.equals("0") && !jc_id.equals("0")) {
					// SOP(("44");

					try {
						conntx = connectDB();
						conntx.setAutoCommit(false);
						stmttx = conntx.createStatement();
						if (!billcat_id.equals("0")) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_inventory_item"
									+ " SET  item_billcat_id = " + billcat_id + ","
									+ " item_model_id = 81,"
									+ " item_modified_id = " + jc_entry_id + ","
									+ " item_modified_date = '" + jc_entry_date + "'"
									+ " WHERE item_id = " + item_id;
							stmttx.execute(StrSql);
							SOP("StrSql=update=" + StrSql);
						}

						AddPartsTrans(request, response);
						// if (!format_ford.equals("Incadea Format")) {
						// UpdateJobcardBillDetails(jc_id, brand_id, jc_bill_cash_no, jc_bill_cash_date);
						// }
						StrSql = "SELECT billcat_billtype, SUM(jctrans_qty) AS quantity, item_type_id, SUM(jctrans_netprice) AS totalamount"
								+ " FROM " + compdb(comp_id) + "axela_service_jc"
								+ "	INNER JOIN " + compdb(comp_id) + " axela_service_jc_trans ON jctrans_jc_id = jc_id"
								+ "	INNER JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = jctrans_item_id"
								+ "	INNER JOIN " + compdb(comp_id) + "axela_inventory_item_bill_cat ON billcat_id = item_billcat_id"
								+ "	AND billcat_brand_id = " + brand_id
								+ "	WHERE jctrans_rowcount != 0"
								+ "	AND jctrans_option_id = 0"
								+ "	AND jctrans_item_id != 0"
								// + "	AND jctrans_billtype_id != 3"// warranty
								+ "	AND jctrans_jc_id = " + jc_id
								+ "	GROUP BY billcat_billtype, item_type_id";
						SOP("StrSql=bill=" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET ";
							while (crs.next()) {
								billcat_billtype = crs.getString("billcat_billtype");
								totalamount = Double.parseDouble(crs.getString("totalamount"));
								quantity = Double.parseDouble(crs.getString("quantity"));
								item_type_id = crs.getString("item_type_id");
								SOP("billcat_billtype=" + billcat_billtype);
								SOP("jctype=" + jctype);
								if (!billcat_billtype.equals("") && totalamount != 0.0) {
									if (item_type_id.equals("3")) {
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

									} else if (item_type_id.equals("4")) {
										if (billcat_billtype.equals("Tyre")) {
											StrSql += " jc_bill_cash_labour_tyre = " + totalamount + ","
													+ " jc_bill_cash_labour_tyre_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Oil")) {
											StrSql += " jc_bill_cash_labour_oil = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Battery")) {
											StrSql += " jc_bill_cash_labour_battery = " + totalamount + ","
													+ " jc_bill_cash_labour_battery_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Break")) {
											StrSql += " jc_bill_cash_labour_brake = " + totalamount + ","
													+ " jc_bill_cash_labour_brake_qty = " + quantity + ",";
										}
										if (billcat_billtype.equals("Accessories")) {
											StrSql += " jc_bill_cash_labour_accessories = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Valueadd")) {
											StrSql += " jc_bill_cash_labour_valueadd = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Extwarranty")) {
											StrSql += " jc_bill_cash_labour_extwarranty = " + totalamount + ","
													+ " jc_bill_cash_labour_extwarranty_qty= " + quantity + ",";
										}
										if (billcat_billtype.equals("Wheelalign")) {
											StrSql += " jc_bill_cash_labour_wheelalign = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Cng")) {
											StrSql += " jc_bill_cash_labour_cng = " + totalamount + ",";
										}
										if (billcat_billtype.equals("Labour")) {
											StrSql += " jc_bill_cash_labour = " + totalamount + ",";
										}
									}
								}
							}
							if (billtype_name.equals("Warranty")) {
								StrSql += " jc_bill_warranty_no= '" + jc_bill_cash_no + "',"
										+ " jc_bill_warranty_date = '" + jc_bill_cash_date + "',"
										+ " jc_bill_warranty_customername = '" + customer_name + "',";
							} else if (billtype_name.equals("Cash") || billtype_name.equals("Internal")) {
								StrSql += " jc_bill_cash_no= '" + jc_bill_cash_no + "',"
										+ " jc_bill_cash_date = '" + jc_bill_cash_date + "',"
										+ " jc_bill_cash_customername = '" + customer_name + "',";
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
				error_msg += "Job card is not Present for this Ro No. : " + jc_ro_no + "<br>";
			}
		}
	}
	public void UpdateTechinicianFormat() throws Exception {
		if (error_msg.equals("")) {
			try {
				jc_entry_date = ToLongDate(kknow());
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET "
						+ " jc_bill_cash_date = '" + jc_bill_cash_date + "',"
						+ " jc_technician_emp_id = '" + jc_emp_id + "',"
						+ " jc_modified_id = " + jc_entry_id + ","
						+ " jc_modified_date = '" + jc_entry_date + "'"
						+ " WHERE 1=1"
						+ " AND jc_ro_no = '" + jc_ro_no + "'";
				// SOP("StrSql==updatetechnician==" + StrSql);
				stmttx.execute(StrSql);
				propcount++;
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
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				} catch (Exception e) {
					msg = "<br>Transaction Error!";
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
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
	}

	public void AddPartsTrans(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			// SOP("AddPartsTrans==");
			double jctrans_qty = 0.00, jctrans_price = 0.00, jctrans_amount = 0.00;
			double jctrans_netprice = 0.00, jctrans_discountamount = 0.00, jctrans_taxamount = 0.00;
			jctrans_qty = Double.parseDouble(billquantity);
			jctrans_price = Double.parseDouble(jc_sell_price);
			jctrans_amount = Double.parseDouble(jc_sell_cost);
			if (!jc_sell_price.equals("") && format_ford.equals("Incadea Format")) {// for incadeca from 31/10/2017
				jctrans_netprice = Double.parseDouble(jc_sell_price);
			} else if (format_ford.equals("Parts Format") && !actual_amount.equals("")) {// for parts from 31/10/2017
				jctrans_netprice = Double.parseDouble(actual_amount);
			}
			jctrans_discountamount = Double.parseDouble(jc_bill_cash_labour_discamt);
			if (!jctaxtotal.equals("")) {
				jctrans_taxamount = Double.parseDouble(jc_tax_total);
			}
			else {
				jctrans_taxamount = Double.parseDouble(jc_tax_total) - Double.parseDouble(jc_sell_price);
			}
			// SOP("jctrans_rowcount==111==" + jctrans_rowcount);
			// SOP("jc_id==" + jc_id);

			// Main Item entry in jc_trans table
			// SOP("Rowcount== " + "SELECT COALESCE(MAX(jc.jctrans_rowcount), 0) + 1"
			// + " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
			// + " WHERE jctrans_jc_id = " + jc_id);
			// jctrans_rowcount = CNumeric(ExecuteQuery("SELECT COALESCE(MAX(jc.jctrans_rowcount), 0) + 1"
			// + " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
			// + " WHERE jctrans_jc_id = " + jc_id));
			// SOP("jctrans_rowcount==222==" + jctrans_rowcount);
			// jctrans_option_id = jctrans_rowcount;

			if (jctransdelete == 0) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
						+ " WHERE jctrans_jc_id =" + jc_id;
				// SOP(("StrSql==" + StrSql);
				stmttx.addBatch(StrSql);
				jctransdelete = 0;
				if (format_ford.equals("Incadea Format")) {
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
							+ " jc_bill_cash_labour = 0.00,"
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
				} else if (format_ford.equals("Parts Format")) {
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
				}
				stmttx.addBatch(StrSql);

			}
			StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_service_jc_trans"
					+ " ( jctrans_jc_id,"
					+ " jctrans_customer_id,"
					+ " jctrans_item_id,"
					+ " jctrans_discount,"
					+ " jctrans_location_id,"
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
					+ " " + item_id + ","
					+ " 0," // jctrans_discount
					+ " " + location_id + "," // jctrans_location_id
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
			SOP("StrSql==JC Main Item==" + StrSql);
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
			// SOP(("StrSql==Discount==" + StrSql);
			stmttx.addBatch(StrSql);

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
					+ " 8," // jctrans_customer_id
					+ " " + item_id + ","
					+ " 1," // jctrans_tax
					+ " 7," // jctrans_tax_id
					+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
					+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
					+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
					+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
					+ " " + jctrans_taxamount + "," // jctrans_price
					+ " " + jctrans_taxamount + "," // jctrans_amount
					+ " " + jctrans_billtype_id + ","// jctrans_billtype_id
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP(("StrSql==Tax==" + StrSql);
			stmttx.addBatch(StrSql);
			stmttx.executeBatch();
			conntx.commit();

			StrSql = "SELECT voucher_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE voucher_jc_id = " + jc_id + ""
					+ "	AND voucher_active = 1";
			voucher_id = CNumeric(ExecuteQuery(StrSql));
			// SOP("voucher_id==" + voucher_id);
			if (!voucher_id.equals("0")) {
				UpdateVoucher(); // To update existing invoice for the Job Card.
			}
			// SOP("COUNT==" + propcount);
			if (format_ford.equals("Parts Format"))
			{
				propcount++;
			}
			if (format_ford.equals("Incadea Format"))
			{
				partscount++;
			}
			// SOP("COUNT=123=" + partscount);
			// conntx.commit();
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
	public void AddItem() throws Exception {

		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item"
					+ " (item_name,"
					+ " item_code,"
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
					+ "	item_purchase_ledger_id,"
					+ "	item_purchasediscount_ledger_id,"
					+ "	item_purchasetax1_ledger_id,"
					+ " item_notes,"
					+ " item_entry_id,"
					+ " item_entry_date)"
					+ " VALUES ("
					+ " '" + partdesc + "'," // item_name
					+ " '" + item_code + "',"
					+ " " + billcat_id + ",";// billcat_id
			if (jctype.equals("Labor")) {
				StrSql += "4,";// item_type_id(Labour)
			} else {
				StrSql += " 3,";// item_type_id(Parts)
			}
			StrSql += " 1,"// item_active
					+ " 1," // item_uom_id
					+ " 1," // item_alt_uom_id
					+ "	24," // item_cat_id
					+ " 81," // model id
					+ " 1," // item_sales_ledger_id
					+ " 0," // item_salesdiscount_ledger_id
					+ " 17," // item_salestax1_ledger_id
					+ " 5," // item_purchase_ledger_id
					+ " 0," // item_purchasediscount_ledger_id
					+ " 17," // item_purchasetax1_ledger_id
					+ "''," // item_notes
					+ " " + jc_entry_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("strsql==Item==" + StrSql);
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
			// SOP("StrSql==Sales Price==" + StrSql);
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
			vouchertrans_netprice = Double.parseDouble(jc_sell_cost);
			vouchertrans_discountamount = Double.parseDouble(jc_bill_cash_labour_discamt);
			vouchertrans_taxamount = Double.parseDouble(jc_tax_total) - Double.parseDouble(jc_sell_price);

			// Main Item entry in jc_trans table
			// vouchertrans_rowcount = CNumeric(ExecuteQuery("SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) + 1"
			// + " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
			// + " WHERE vouchertrans_voucher_id =" + voucher_id));
			// vouchertrans_option_id = vouchertrans_rowcount;
			// SOP("vouchertransdelete==" + vouchertransdelete);
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
					+ " " + location_id + ","// vouchertrans_location_id
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
			// SOP("StrSql==Voucher Main Item==" + StrSql);
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
					+ " 1" // jctrans_dc
					+ ")";
			// SOP("StrSql==Discount==" + StrSql);
			stmttx.addBatch(StrSql);

			// Tax entry in jc_trans table
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
					+ " 8," // jctrans_customer_id
					+ " " + item_id + ","
					+ " 1," // jctrans_tax
					+ " 7," // jctrans_tax_id
					+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
					+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
					+ " " + vouchertrans_taxamount + "," // jctrans_price
					+ " " + vouchertrans_taxamount + "," // jctrans_amount
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("StrSql==Tax==" + StrSql);
			stmttx.addBatch(StrSql);
			stmttx.executeBatch();

			// Update voucher amount in voucher table
			StrSql = "SELECT "
					+ "@totalamt := SUM(CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_amount END) AS amount, "
					+ "@totaldis := SUM(CASE WHEN vouchertrans_discount = 1 THEN vouchertrans_amount END) AS discount, "
					+ "@totaltax := SUM(CASE WHEN vouchertrans_tax = 1 THEN vouchertrans_amount END) AS tax, "
					+ "@total := (@totalamt - @totaldis) + @totaltax AS netamount "
					+ "FROM " + compdb(comp_id) + "axela_acc_voucher_trans "
					+ "WHERE vouchertrans_voucher_id = " + voucher_id;

			CachedRowSet crs = processQuery(StrSql, 0);
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
	public String PopulateFormatFord(String compid, String format_ford) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Labour Format\"").append(StrSelectdrop("Labour Format", format_ford)).append(">Labour Format</option>\n");
		Str.append("<option value=\"Parts Format\"").append(StrSelectdrop("Parts Format", format_ford)).append(">Parts Format</option>\n");
		Str.append("<option value=\"Technician Format\"").append(StrSelectdrop("Technician Format", format_ford)).append(">Technician Format</option>\n");
		Str.append("<option value=\"Incadea Format\"").append(StrSelectdrop("Incadea Format", format_ford)).append(">Incadea Format</option>\n");
		return Str.toString();
	}

	public String checkBillCode(String billcat_name) {
		try {
			int billcode = 0;
			billcode = Integer.valueOf(billcat_name);
			SOP("billcode==" + billcode);
			// if (billcode >= 2000 && billcode <= 2999) {
			// billcat_name = "Brake";
			// }
			if (billcode == 2101 || billcode == 2205) { // as per disscusion on 2/11/17
				billcat_name = "Brake";
			}
			if ((billcode >= 7000 && billcode <= 7999) || billcode == 9007) {
				billcat_name = "Accessories";
			}
			if (billcode == 1301) {
				billcat_name = "Oil";
			}
			if (billcode == 8101) {
				billcat_name = "Tyre";
			}
			if ((billcode >= 1000 && billcode <= 1999 && billcode != 1301) || (billcode >= 2000 && billcode <= 2999 && billcode != 2101 && billcode != 2205)
					|| (billcode >= 3000 && billcode <= 6999 && billcode != 4101 && billcode != 4102) ||
					(billcode >= 8000 && billcode <= 9000 && billcode != 8101)
					|| (billcode >= 9101 && billcode <= 9104)
					|| billcode == 9999) {
				billcat_name = "Parts";
			}
			if (billcode == 4101 || billcode == 4102) {
				billcat_name = "Battery";
			}
			if (!billcat_name.equals("")) {
				StrSql = "SELECT billcat_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
						+ " WHERE 1=1"
						+ " AND billcat_brand_id = " + brand_id
						+ " AND  billcat_name= '" + billcat_name + "'";
				SOP("billcat_id StrSql==" + StrSql);
				billcat_id = CNumeric(ExecuteQuery(StrSql));
			}
			return billcat_id;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "0";
		}

	}
	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id + ""
					+ " GROUP BY location_id"
					+ " ORDER BY location_name";
			// SOP("StrSql==loc=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_location_id\" name=\"dr_location_id\" class=\"form-control\" >\n");
			Str.append("<option value=\"0\">Select Location</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(Selectdrop(crs.getInt("location_id"), location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public void UpdateJobcardDetails(Map checkstock) throws SQLException {
		int checkjcid = 1;
		Iterator iterator = checkstock.entrySet().iterator();
		while (iterator.hasNext()) {
			jc_id = (String) checkstock.get(checkjcid);
			UpdateJobcardStock(jc_id);
			UpdateJobcardBillDetails(jc_id, brand_id, jc_bill_cash_no, jc_bill_cash_date);
		}
	}
	public void UpdateJobcardStock(String jc_id) {

	}
	public void UpdateJobcardBillDetails(String jc_id, String brand_id, String jc_bill_cash_no, String jc_bill_cash_date) throws SQLException {

		StrSql = "SELECT billcat_billtype, SUM(jctrans_qty) AS quantity, item_type_id, SUM(jctrans_netprice) AS totalamount"
				+ " FROM " + compdb(comp_id) + "axela_service_jc"
				+ "	INNER JOIN " + compdb(comp_id) + " axela_service_jc_trans ON jctrans_jc_id = jc_id"
				+ "	INNER JOIN " + compdb(comp_id) + " axela_inventory_item ON item_id = jctrans_item_id"
				+ "	INNER JOIN " + compdb(comp_id) + "axela_inventory_item_bill_cat ON billcat_id = item_billcat_id"
				+ "	AND billcat_brand_id = " + brand_id
				+ "	WHERE jctrans_rowcount != 0"
				+ "	AND jctrans_option_id = 0"
				+ "	AND jctrans_item_id != 0"
				+ "	AND jctrans_billtype_id != 3"// warranty
				+ "	AND jctrans_jc_id = " + jc_id
				+ "	GROUP BY billcat_billtype, item_type_id";
		SOP("StrSql=bill=" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
					+ " SET ";
			while (crs.next()) {
				billcat_billtype = crs.getString("billcat_billtype");
				totalamount = Double.parseDouble(crs.getString("totalamount"));
				quantity = Double.parseDouble(crs.getString("quantity"));
				item_type_id = crs.getString("item_type_id");
				SOP("billcat_billtype=" + billcat_billtype);
				SOP("jctype=" + jctype);
				if (!billcat_billtype.equals("") && totalamount != 0.0) {
					if (item_type_id.equals("3")) {
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

					} else if (item_type_id.equals("4")) {
						if (billcat_billtype.equals("Tyre")) {
							StrSql += " jc_bill_cash_labour_tyre = " + totalamount + ","
									+ " jc_bill_cash_labour_tyre_qty= " + quantity + ",";
						}
						if (billcat_billtype.equals("Oil")) {
							StrSql += " jc_bill_cash_labour_oil = " + totalamount + ",";
						}
						if (billcat_billtype.equals("Battery")) {
							StrSql += " jc_bill_cash_labour_battery = " + totalamount + ","
									+ " jc_bill_cash_labour_battery_qty= " + quantity + ",";
						}
						if (billcat_billtype.equals("Break")) {
							StrSql += " jc_bill_cash_labour_brake = " + totalamount + ","
									+ " jc_bill_cash_labour_brake_qty = " + quantity + ",";
						}
						if (billcat_billtype.equals("Accessories")) {
							StrSql += " jc_bill_cash_labour_accessories = " + totalamount + ",";
						}
						if (billcat_billtype.equals("Valueadd")) {
							StrSql += " jc_bill_cash_labour_valueadd = " + totalamount + ",";
						}
						if (billcat_billtype.equals("Extwarranty")) {
							StrSql += " jc_bill_cash_labour_extwarranty = " + totalamount + ","
									+ " jc_bill_cash_labour_extwarranty_qty= " + quantity + ",";
						}
						if (billcat_billtype.equals("Wheelalign")) {
							StrSql += " jc_bill_cash_labour_wheelalign = " + totalamount + ",";
						}
						if (billcat_billtype.equals("Cng")) {
							StrSql += " jc_bill_cash_labour_cng = " + totalamount + ",";
						}
						if (billcat_billtype.equals("Labour")) {
							StrSql += " jc_bill_cash_labour = " + totalamount + ",";
						}
					}
				}
			}
			if (billtype_name.equals("Warranty")) {
				StrSql += " jc_bill_warranty_no= '" + jc_bill_cash_no + "',"
						// + " jc_bill_warranty_date = '" + jc_bill_cash_date + "',"
						+ " jc_bill_warranty_customername = '" + customer_name + "',";
			} else if (billtype_name.equals("Cash") || billtype_name.equals("Internal")) {
				StrSql += " jc_bill_cash_no= '" + jc_bill_cash_no + "',"
						// + " jc_bill_cash_date = '" + jc_bill_cash_date + "',"
						+ " jc_bill_cash_customername = '" + customer_name + "',";
			}
			StrSql += " jc_modified_id = " + jc_entry_id + ","
					+ " jc_modified_date = '" + jc_entry_date + "'"
					+ " WHERE jc_id = " + jc_id;
			SOP("StrSql=parts=" + StrSql);
			stmttx.execute(StrSql);
		}
		crs.close();

	}
}
