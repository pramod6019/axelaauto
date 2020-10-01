//Shivaprasad 6/07/2015   
package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class JobCard_User_Import_Harley_Davidson extends Connect {

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
	public String branch_id = "0", jc_branch_id = "0", brand_id = "153";
	public String branch_name = "", veh_warranty_expirydate = "";
	public String upload = "";
	public String similar_comm_no = "";
	public String jc_id = "0";
	public String vehkms_id = "0";
	public String jc_reg_no = "0", jc_ro_no = "", jc_no = "", error_msg = "";
	public String jc_error_msg = "";
	public String jc_kms = "0", jc_discamt = "0", jc_grandtotal = "0", jc_bill_cash_valueadd = "0", jc_chassis_no = "";
	public int labourcolumnLength = 0, partscolumnLength = 0, techniciancolumnLength = 0;
	public int count = 0, ronumcount = 1, jctransdelete = 0, vouchertransdelete = 0, k = 0;
	HashMap<String, String> mapronum = new HashMap<String, String>();

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

	// Stock Variables
	public String vehstock_id = "0";
	public String option_id = "0";
	public String so_id = "0", voucher_id = "0";

	// Vehicle Variables
	public String veh_id = "0";
	public String interior = "", exterior = "";
	public String item_id = "0", item_name = "", model_brand_id = "0";
	public String model_name = "", model_id = "0", item_service_code = "";
	public String veh_engine_no = "", veh_reg_no = "";
	public String veh_modelyear = "";
	public String veh_sale_date = "", veh_insur_date = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0";
	public String soe_name = "", sob_name = "", soe_id = "0", sob_id = "0";
	public String veh_model_id = "0", veh_variant_id = "0", veh_fueltype_id = "0", veh_loanfinancer = "", veh_status_id = "0", veh_status_date = "";
	public String veh_soe_id = "0", veh_buyertype_id = "0", veh_desc = "";
	public String veh_notes = "", veh_priorityveh_id = "0", veh_vehsource_id = "0", vehsource_name = "";

	// Jobcard Variables
	public String jc_jcstage_id = "6";
	public String jc_jctype_id = "0", jc_notes = "", jc_jctype_name = "", jc_jccat_id = "0";
	public String jc_advice = "";
	public String jc_time_in = "0", jc_time_promised = "", jc_time_ready = "", jc_time_out = "";
	public String jc_emp_id = "0", jc_emp_name = "", jc_bill_cash_no = "";
	public String jc_technician_emp_id = "0", jc_technician_emp_name = "";
	public String hrs = "", min = "", day = "", month = "", year = "", servicedueyear = "", veh_service_duekms = "", veh_service_duedate = "";
	public String jc_bill_cash_date = "", jc_bill_cash_parts = "0", jc_bill_cash_tyre = "0.0", jc_bill_cash_oil = "0.0";
	public String jc_bill_cash_accessories = "0.0", jc_bill_cash_labour = "0.0";
	public String jc_customername = "";

	public String servicetype = "", veh_chassis_no = "", item = "", custcat = "", serviadv = "", technician = "", customername = "", jc_customer_id = "", telephone = "",
			jc_contact_id = "";
	public String customer_id = "0";
	public String chassisid = "0";
	// parts variable
	public String item_code = "", price_id = "0", timein = "", billtime = "";
	public String jc_sell_price = "";
	public String jc_sell_cost = "", jc_netprice = "", billcat_name = "", billcat_billtype = "", item_cat_id = "0", billcat_id = "0";
	public String actual_amount = "", jc_tax_total = "", jc_discount = "";
	double totalamount = 0.0;
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
			response.sendRedirect("../service/jobcard-user-import-harley-davidson.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(jc_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (format_volvo.equals("")) {
		// msg = msg + "<br>Select Format!";
		// }
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
			labourcolumnLength = 76;
			// partscolumnLength = 18;
			propcount = 0;
			obj.propcount = 0;
			SOP("columnLength===" + columnLength);
			// techniciancolumnLength = 3;
			if (columnLength != labourcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
			// else if (format_volvo.equals("Parts Format") && columnLength != partscolumnLength) {
			// msg += "<br>" + "Document columns doesn't match with the template!";
			// }
			// else if (format_volvo.equals("Technician Format") && columnLength != techniciancolumnLength) {
			// msg += "<br>" + "Document columns doesn't match with the template!";
			// }
			else {
				for (j = 1; j < rowLength + 1; j++) {
					// CheckForm();
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {

						// Reg Number....
						if (h == 2) {
							jc_reg_no = "";
							jc_reg_no = PadQuotes(sheetData[j][h]);
							if (jc_reg_no.equals("null") || jc_reg_no.equals("0") || jc_reg_no.equals("Reg. Num")) {
								jc_reg_no = "";
							}
						}
						// Job Card Chassis Number....
						if (h == 3) {
							jc_chassis_no = "";
							jc_chassis_no = sheetData[j][h];
							if (jc_chassis_no.equals("null") || jc_chassis_no.equals("0") || jc_chassis_no.equals("Chassis No.")) {
								jc_chassis_no = "";
							}
							// SOP("jc_reg_no===" + jc_reg_no);
						}
						// Item Name
						if (h == 4) {
							item_name = "";
							item_name = PadQuotes(sheetData[j][h]);
						}
						// Sale date
						if (h == 5) {
							veh_sale_date = "";
							veh_sale_date = PadQuotes(sheetData[j][h]);
						}
						// Customer Full Name
						if (h == 7) {
							customer_name = "";
							customer_name = PadQuotes(sheetData[j][h]);
							if (customer_name.equals("null") || customer_name.equals("0") || customer_name.equals("Customer Name")) {
								customer_name = "";
							}
						}
						// Address 1....
						if (h == 8) {
							contact_address1 = "";
							contact_address1 = PadQuotes(sheetData[j][h]);
							// SOP("customer_address====" + customer_address);
							if (contact_address1.equals("null") || contact_address1.equals("0") || contact_address1.equals("Address")) {
								contact_address1 = "";
							}
						}
						// Address 2....
						if (h == 9) {
							contact_address2 = "";
							contact_address2 = PadQuotes(sheetData[j][h]);
							// SOP("customer_address====" + customer_address);
							if (contact_address2.equals("null") || contact_address2.equals("0") || contact_address2.equals("Address")) {
								contact_address2 = "";
							}
						}
						// Address 3....
						if (h == 10) {
							contact_address3 = "";
							contact_address3 = PadQuotes(sheetData[j][h]);
							// SOP("customer_address====" + customer_address);
							if (contact_address3.equals("null") || contact_address3.equals("0") || contact_address3.equals("Address")) {
								contact_address3 = "";
							}
							contact_address = contact_address1 + contact_address2 + contact_address3;
						}
						// Mobile Number....
						if (h == 11) {
							contact_mobile1 = "";
							contact_phone1 = "";
							contact_mobile1 = sheetData[j][h];

							if (!contact_mobile1.equals("")) {
								if (contact_mobile1.contains(":")) {
									contact_mobile1 = contact_mobile1.split(":")[0];
								}
								if (contact_mobile1.substring(0, 3).equals("011") && contact_mobile1.length() == 11) {
									contact_phone1 = contact_mobile1;
									contact_phone1 = contact_mobile1.replaceFirst("011", "11-");
								}
							}
							// SOP("contact_mobile1==222==" + contact_mobile1);
						}
						// state
						if (h == 12) {
							contact_state = "";
							contact_state = sheetData[j][h];
						}
						// City Name
						if (h == 13) {
							contact_city = "";
							contact_city = PadQuotes(sheetData[j][h]);
						}
						// job card RO
						if (h == 14) {
							jc_ro_no = "";
							jc_ro_no = PadQuotes(sheetData[j][h]);
							if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card Ro.")) {
								jc_ro_no = "";
							}
							// SOP("jc_ro_no===" + jc_ro_no);
						}
						// job card RO Date
						if (h == 15) {
							jc_time_in = "";
							jc_time_in = sheetData[j][h];
							if (jc_time_in.equals("null") || jc_time_in.equals("0") || jc_time_in.equals("Job Card Open Date")) {
								jc_time_in = "";
							}
							SOP("jc_time_in=1=" + jc_time_in);
							if (jc_time_in.contains(".") && !jc_time_in.equals("")) {
								if (jc_time_in.split("\\.").length == 3) {
									day = jc_time_in.split("\\.")[0];
									if (day.length() == 1) {
										day = "0" + day;
									}
									month = jc_time_in.split("\\.")[1];
									if (month.length() == 1) {
										month = "0" + month;
									}
									year = jc_time_in.split("\\.")[2];
									if (year.length() == 2) {
										year = "20" + year;
									}
									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										jc_time_in = year + month + day;
									} else {

										error_msg += "Invalid JC Time In! <br>";
									}
									day = "";
									month = "";
									year = "";
								}
								SOP("jc_time_in=2=" + jc_time_in);
							}

						}
						// time -in
						if (h == 18) {
							timein = "";
							timein = sheetData[j][h];
							SOP("timein=4=" + timein);
							if (timein.contains(".") && !timein.equals("")) {
								if (timein.split("\\.").length == 2) {
									hrs = timein.split("\\.")[0];
									if (hrs.length() == 1) {
										hrs = "0" + hrs;
									} else if (hrs.length() > 2) {
										hrs = hrs.substring(0, 2);
									}
									min = timein.split("\\.")[1];
									if (min.length() == 1) {
										min = "0" + min;
									} else if (min.length() > 2) {
										min = min.substring(0, 2);
									}
									jc_time_in += hrs + min + "00";

								}
							}
							else if (timein.length() == 2) {
								jc_time_in += hrs + "00" + "00";
							}
							if (!jc_time_in.equals("") && timein.equals("") || timein.equals("0")) {
								jc_time_in += ToLongDate(kknow()).substring(8, 14) + "";
							}
						}
						// Bill Date....
						if (h == 24) {
							jc_bill_cash_date = "";
							jc_bill_cash_date = sheetData[j][h];
							// SOP("jc_bill_date==" + jc_bill_date);
							if (jc_bill_cash_date.contains(".") && !jc_bill_cash_date.equals("")) {
								if (jc_bill_cash_date.split("\\.").length == 3) {
									day = jc_bill_cash_date.split("\\.")[0];
									if (day.length() == 1) {
										day = "0" + day;
									}
									month = jc_bill_cash_date.split("\\.")[1];
									if (month.length() == 1) {
										month = "0" + month;
									}
									year = jc_bill_cash_date.split("\\.")[2];
									if (year.length() == 2) {
										year = "20" + year;
									}
									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										jc_bill_cash_date = year + month + day;
									} else {
										SOP("jc_bill_date==" + jc_bill_cash_date);
										error_msg += "Invalid Bill Date! <br>";
									}
									day = "";
									month = "";
									year = "";
								}
							}

						}
						// bill time
						if (h == 25) {
							billtime = "";
							billtime = sheetData[j][h];
							if (billtime.contains(".") && !billtime.equals("")) {
								if (billtime.split("\\.").length == 2) {
									hrs = billtime.split("\\.")[0];
									if (hrs.length() == 1) {
										hrs = "0" + hrs;
									} else if (hrs.length() > 2) {
										hrs = hrs.substring(0, 2);
									}
									min = billtime.split("\\.")[1];
									if (min.length() == 1) {
										min = "0" + min;
									} else if (min.length() > 2) {
										min = min.substring(0, 2);
									}
									jc_bill_cash_date += hrs + min + "00";
								}
							}
							else if (billtime.length() == 2) {
								jc_bill_cash_date += hrs + "00" + "00";
							}
							if (!jc_bill_cash_date.equals("") && billtime.equals("") || billtime.equals("0")) {
								jc_bill_cash_date += ToLongDate(kknow()).substring(8, 14) + "";
							}
						}
						// Bill No.
						if (h == 26) {
							jc_bill_cash_no = "";
							jc_bill_cash_no = sheetData[j][h];
						}
						// Mileage....
						if (h == 35) {
							jc_kms = "0";
							jc_kms = sheetData[j][h];
							if (jc_kms.equals("null") || jc_kms.equals("") || jc_kms.equals("Mileage")) {
								jc_kms = "0";
							}
						}
						// Service Advisor....
						if (h == 36) {
							jc_emp_name = "";
							jc_emp_name = PadQuotes(sheetData[j][h]);
							if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
								jc_emp_name = "";
							}
							// SOP("jc_emp_name===" + jc_emp_name);
						}
						// Type Of Repair...
						if (h == 38) {
							jc_jctype_name = "";
							jc_jctype_name = PadQuotes(sheetData[j][h]);
							// SOP("jc_jctype_name==" + jc_jctype_name);
							if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("Service Type")) {
								jc_jctype_name = "";
							}
						}
						// Labour Amt
						if (h == 50) {
							jc_bill_cash_labour = "0";
							jc_bill_cash_labour = sheetData[j][h];
							// SOP("jc_grandtotal==" + jc_grandtotal);
						}
						// dob
						if (h == 73) {
							contact_dob = "";
							contact_dob = sheetData[j][h];
						}
						// anniversary
						if (h == 74) {
							contact_anniversary = "";
							contact_anniversary = sheetData[j][h];
						}

					}
					jc_entry_date = ToLongDate(kknow());
					if (!jc_ro_no.equals("")) {
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
}