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

public class JobCard_User_Import_OneTriumph extends Connect {

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
	public String branch_id = "0", jc_branch_id = "0", brand_id = "151";
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

	public String servicetype = "", veh_chassis_no = "", item = "", jctime = "", serviadv = "", technician = "", customername = "", jc_customer_id = "", telephone = "",
			jc_contact_id = "";
	public String customer_id = "0";
	public String chassisid = "0";
	// parts variable
	public String jc_bill_quantity = "0", item_code = "", price_id = "0", timein = "", billtime = "";
	public String jc_sell_price = "";
	public String jc_sell_cost = "", jc_netprice = "", billcat_name = "", billcat_billtype = "", item_cat_id = "0", billcat_id = "0";
	public String actual_amount = "", jc_tax_total = "", jc_discount = "";
	double totalamount = 0.0;
	public String jctrans_rowcount = "0", jctrans_option_id = "0", status = "";
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
					// format_OneTriumph = str1[1];
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
											msg += "<br>" + propcount + " Delivered Jobcard(s) Imported successfully!";
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
			response.sendRedirect("../service/jobcard-user-import-onetriumph.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		SOP("CheckForm==");
		msg = "";
		if (CNumeric(jc_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (format_OneTriumph.equals("")) {
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
			propcount = 0;
			obj.propcount = 0;
			obj.brand_id = brand_id;
			labourcolumnLength = 30;
			// partscolumnLength = 18;
			// techniciancolumnLength = 3;

			if (columnLength != labourcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
			// else if (format_OneTriumph.equals("Parts Format") && columnLength != partscolumnLength) {
			// msg += "<br>" + "Document columns doesn't match with the template!";
			// }
			// else if (format_OneTriumph.equals("Technician Format") && columnLength != techniciancolumnLength) {
			// msg += "<br>" + "Document columns doesn't match with the template!";
			// }
			else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {
						// if (format_OneTriumph.equals("Labour Format")) {

						// Dealer Name
						if (h == 1) {
						}

						// City
						if (h == 2) {
							contact_city = "";
							contact_city = PadQuotes(sheetData[j][h]);
						}
						// Jobcard No.
						if (h == 3) {
							jc_ro_no = "";
							jc_ro_no = PadQuotes(sheetData[j][h]);
						}
						// Jobcard date and time
						if (h == 4) {
							jc_time_in = "";
							jc_time_in = PadQuotes(sheetData[j][h]);
						}
						// location
						if (h == 5) {

						}
						// Reg Number....
						if (h == 6) {
							jc_reg_no = "";
							jc_reg_no = PadQuotes(sheetData[j][h]);
						}
						// ITEM Name....
						if (h == 7) {
							item_name = "";
							item_name = PadQuotes(sheetData[j][h]);

						}
						// Chassis Number....
						if (h == 8) {
							jc_chassis_no = "";
							jc_chassis_no = PadQuotes(sheetData[j][h]);
						}
						// Sale date
						if (h == 9) {
							veh_sale_date = "";
							veh_sale_date = PadQuotes(sheetData[j][h]);
						}
						// mileage
						if (h == 10) {
							jc_kms = "";
							jc_kms = PadQuotes(sheetData[j][h]);
						}
						// Customer Name
						if (h == 11) {
							customer_name = "";
							customer_name = PadQuotes(sheetData[j][h]);
							if (customer_name.equals("null") || customer_name.equals("0") || customer_name.equals("Customer Name")) {
								customer_name = "";
							}
						}

						// Customer Phone....
						if (h == 12) {
							contact_mobile1 = "";
							contact_mobile1 = PadQuotes(sheetData[j][h]);
						}

						// NATURE OF REPAIR
						if (h == 13) {
							jc_jctype_name = "";
							jc_jctype_name = PadQuotes(sheetData[j][h]);
							if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("NATURE OF REPAIR")) {
								jc_jctype_name = "";
							}
						}
						// SUB SERVICE TYPE
						if (h == 14) {

						}
						// Technician....
						if (h == 15) {
							jc_technician_emp_name = "";
							jc_technician_emp_name = PadQuotes(sheetData[j][h]);
							// SOP("jc_technician_emp_name==from excell==" + jc_technician_emp_name);
							if (jc_technician_emp_name.equals("null") || jc_technician_emp_name.equals("0") || jc_technician_emp_name.equals("Technician")) {
								jc_technician_emp_name = "";
							}
						}
						// Status
						if (h == 16) {
							status = "";
							status = PadQuotes(sheetData[j][h]).toLowerCase();
							SOP("status==" + status);
						}
						// part order status
						if (h == 17) {

						}
						// remarks
						if (h == 18) {

						}
						// close date
						if (h == 19) {

						}
						// estimated date
						if (h == 20) {

						}

						// Delivery Date....
						if (h == 21) {
							jc_bill_cash_date = "";
							jc_bill_cash_date = PadQuotes(sheetData[j][h]);
						}
						// aging
						if (h == 22) {

						}
						// spare parts value
						if (h == 23) {

						}
						// Labour Amt
						if (h == 24) {
							jc_bill_cash_labour = "";
							jc_bill_cash_labour = PadQuotes(sheetData[j][h]);
							if (jc_bill_cash_labour.equals("") || jc_bill_cash_labour.equals("0") || jc_bill_cash_labour.equals("Labour Amt")) {
								jc_bill_cash_labour = "0";
							}
							SOP("jc_bill_cash_labour==" + jc_bill_cash_labour);
						}
						// Pending Reason
						if (h == 25) {

						}
						// Other Charge
						if (h == 26) {

						}
						// Pending Reason Remark
						if (h == 27) {

						}
						// Parts Order Date
						if (h == 28) {

						}
						// Action Area
						if (h == 29) {

						}
					}
					if (status.equals("delivered")) {
						jc_entry_date = ToLongDate(kknow());
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
			// }
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	// public String PopulateFormatOneTriumph(String compid, String format_OneTriumph) {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=\"\">Select</option>\n");
	// Str.append("<option value=\"Labour Format\"").append(StrSelectdrop("Labour Format", format_OneTriumph)).append(">Labour Format</option>\n");
	// Str.append("<option value=\"Parts Format\"").append(StrSelectdrop("Parts Format", format_OneTriumph)).append(">Parts Format</option>\n");
	// // Str.append("<option value=\"Technician Format\"").append(StrSelectdrop("Technician Format", format_OneTriumph)).append(">Technician Format</option>\n");
	// return Str.toString();
	// }

}