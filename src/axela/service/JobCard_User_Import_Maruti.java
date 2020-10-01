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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

import axela.accounting.Receipt_Update;
import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class JobCard_User_Import_Maruti extends Connect {

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
	public int propcount = 0;
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", jc_branch_id = "0";
	public String branch_name = "";
	public String upload = "";
	public String similar_comm_no = "";
	public String jc_id = "0";
	public String vehkms_id = "0";
	public String jc_reg_no = "0", jc_ro_no = "", jc_no = "", error_msg = "";
	public String chassisid = "0";
	public String jc_error_msg = "";

	// Customer And Contact Variables
	public String customer_id = "0";
	public String customer_name = "", caruser_name = "";
	public String contact_id = "", customer_gst_no = "", gstno = "";
	public String contact_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_phone1 = "", contact_phone2 = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String contact_email1 = "", contact_email2 = "";
	public String contact_address = "", contact_city = "";
	public String contact_pin = "", contact_state = "", state_id = "0", customer_city_id = "0", contact_state_id = "0";
	public String contact_title = "0", customer_check = "";
	public String contact_dob = "", contact_anniversary = "";
	public String contact_title_id = "1";

	// Stock Variables
	public String stock_id = "0";
	public String option_id = "0";
	public String so_id = "0";

	// Vehicle Variables
	public String veh_id = "0", branchcode = "";
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
	public String jc_jctype_id = "0", jc_notes = "", jc_jctype_name = "", jc_jccat_id = "0", jc_jccat_name = "";
	public String jc_advice = "";
	public String jc_time_in = "0", jc_time_promised = "", jc_time_ready = "", jc_time_out = "", bill_no = "";
	public String jc_kms = "0", jc_discamt = "0", jc_grandtotal = "0", jc_bill_cash_valueadd = "0", jc_chassis_no = "";

	public String jc_emp_id = "0", jc_emp_name = "";
	public String jc_bill_cash_date = "";
	public String jc_technician_emp_id = "0", jc_technician_emp_name = "", jc_bill_cash_labour = "0", jc_bill_cash_no = "";
	public String jc_bill_cash_accessories = "0", jc_bill_cash_tyre = "0", jc_bill_cash_oil = "0", jc_bill_cash_labour_discamt = "0", jc_bill_insur_discamt = "0";
	public String hrs = "", min = "", day = "", month = "", year = "", servicedueyear = "", veh_service_duekms = "", veh_service_duedate = "";

	public int count = 0, jobcardcolumnLength = 0, labourcolumnLength = 0, jctransdelete = 0, vouchertransdelete = 0, ronumcount = 1;
	HashMap<String, String> mapronum = new HashMap<String, String>();
	public DecimalFormat df1 = new DecimalFormat("0.0000");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String format_maruti = "", jc_bill_cash_parts = "0", billtype = "", bill_num = "", jc_tax_total = "", item_type_id = "";
	public String jc_sell_price = "", jc_discount = "", jc_netprice = "", voucher_id = "0", price_id = "0", itemid1 = "0",
			voucher_invoice_id = "0", labour = "", voucherinvoiceidcheck = "0";
	ArrayList<Double> jctrans_taxamount1 = new ArrayList<Double>();
	ArrayList<Double> jctrans_taxamount2 = new ArrayList<Double>();
	// for labour
	String[] item_salestax1_ledger_id = {"", "", "", ""}, item_salestax2_ledger_id = {"", "", "", ""}, item_salestax3_ledger_id = {"", "", "", ""},
			itemlabourid = {"", "", "", ""}, itemlabourname = {"", "", "", ""};
	// for parts
	String[] item_salestax4_ledger_id = {"", "", "", ""}, item_salestax5_ledger_id = {"", "", "", ""}, item_salestax6_ledger_id = {"", "", "", ""},
			itempartsid = {"", "", "", ""}, itempartsname = {"", "", "", ""};

	String partsamt = "", partscharges = "", labouramt = "", totalbasicamt = "";// basic amt
	String grosspartsamt = "", grosspartscharges = "", grosslabour = "", totalgrossamt = "";// gross amt

	String partsdiscount = "", partsdiscountcharges = "", labourdiscount = "", totaldiscountamt = ""; // discount

	String taxpartamtC2_5 = "0", taxpartschargesC2_5 = "0", taxlabouramtC2_5 = "0", totaltaxC2_5 = "0";// @CGST2.5
	String taxpartamtS2_5 = "0", taxpartschargesS2_5 = "0", taxlabouramtS2_5 = "0", totaltaxS2_5 = "0";// @SGST2.5

	String taxpartamtC6 = "0", taxpartschargesC6 = "0", taxlabouramtC6 = "0", totaltaxC6 = "0";// @CGST6
	String taxpartamtS6 = "0", taxpartschargesS6 = "0", taxlabouramtS6 = "0", totaltaxS6 = "0";// @SGST6

	String taxpartamtC9 = "0", taxpartschargesC9 = "0", taxlabouramtC9 = "0", totaltaxC9 = "0";// @CGST9
	String taxpartamtS9 = "0", taxpartschargesS9 = "0", taxlabouramtS9 = "0", totaltaxS9 = "0";// @SGST9

	String taxpartamtC14 = "0", taxpartschargesC14 = "0", taxlabouramtC14 = "0", totaltaxC14 = "0";// @CGST14
	String taxpartamtS14 = "0", taxpartschargesS14 = "0", taxlabouramtS14 = "0", totaltaxS14 = "0";// @SGST14

	String taxpartamtI5 = "0", taxpartschargesI5 = "0", taxlabouramtI5 = "0", totaltaxI5 = "0";// @IGST5
	String taxpartamtI12 = "0", taxpartschargesI12 = "0", taxlabouramtI12 = "0", totaltaxI12 = "0";// @IGST12
	String taxpartamtI18 = "0", taxpartschargesI18 = "0", taxlabouramtI18 = "0", totaltaxI18 = "0";// @IGST18
	String taxpartamtI28 = "0", taxpartschargesI28 = "0", taxlabouramtI28 = "0", totaltaxI28 = "0";// @IGST28

	String partamtroundoff = "", partschargesroundoff = "", labouramtroundoff = "", totalroundoff = "";// roundoff

	String netpartsamt = "", netpartscharge = "", netlabouramt = "", grandtotal = "";// net amount
	String total_tax = "", taxamt = "";
	double billparts = 0.0, billlabour = 0.0, totaltax = 0.00, price = 0.00, amount = 0.00, tax = 0.00, tax1 = 0.00,
			labourtaxamt = 0.00, labdiff = 0.00, parttaxamt = 0.00, partdiff = 0.00;
	JobCard_User_Import obj = new JobCard_User_Import();

	Receipt_Update receipt = new Receipt_Update();
	String branch_invoice_prefix = "", branch_invoice_suffix = "", voucher_no = "0";
	public String voucher_customer_id = "0", voucher_contact_id = "0", voucher_billing_add = "", jctrans_location_id = "0";
	public HashMap<Integer, String> taxmap = new HashMap<Integer, String>();
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			savePath = VehicleImportPath(comp_id);
			docsize = 1;
			importdocformat = ".xls, .xlsx";

			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
				// File f = new File("d:/");
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
					format_maruti = str1[1];
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
											if (format_maruti.equals("Labour Format")) {
												// stock update
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
			response.sendRedirect("../service/jobcard-user-import-maruti.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(jc_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (format_maruti.equals("")) {
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
			if (rowLength > 2000) {
				rowLength = 2000;
			}
			int h = 0;
			int j = 0;
			propcount = 0;
			jobcardcolumnLength = 41;
			labourcolumnLength = 67;
			obj.propcount = 0;
			StrSql = "SELECT branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id=" + jc_branch_id;
			obj.brand_id = CNumeric(ExecuteQuery(StrSql));

			StrSql = "SELECT location_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id
					+ " ORDER BY location_name";
			jctrans_location_id = CNumeric(ExecuteQuery(StrSql));

			if (format_maruti.equals("Jobcard Format") && columnLength != jobcardcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					CheckForm();
					error_msg = "";
					// SOP("rowLength==="+(rowLength+1));
					for (h = 0; h < columnLength + 1 && msg.equals(""); h++) {
						if (format_maruti.equals("Jobcard Format")) {

							// Job Card Number....
							if (h == 5) {
								jc_ro_no = "";
								jc_ro_no = PadQuotes(sheetData[j][h]);
								if (jc_ro_no.equals("null") || jc_ro_no.equals("0") || jc_ro_no.equals("Job Card No.")) {
									jc_ro_no = "";
								}
								// SOP("jc_ro_no==main=" + jc_ro_no);
							}

							// Job Card Open Date....
							if (h == 6) {
								jc_time_in = "";
								jc_time_in = PadQuotes(sheetData[j][h]);
								if (jc_time_in.equals("null") || jc_time_in.equals("0") || jc_time_in.equals("Job Card Open Date")) {
									jc_time_in = "";
								}
								// for dd motor jc_time_in is considering bill date also
								if (!jc_time_in.equals("")) {
									jc_bill_cash_date = jc_time_in;
								}
								// SOP("jc_time_in===" + jc_time_in);
								// SOP("jc_bill_cash_date===" + jc_bill_cash_date);
							}
							// Bill Number
							if (h == 7) {
								jc_bill_cash_no = "";
								jc_bill_cash_no = PadQuotes(sheetData[j][h]);
								if (jc_bill_cash_no.equals("null") || jc_bill_cash_no.equals("0") || jc_bill_cash_no.equals("Bill Number")) {
									jc_bill_cash_no = "";
								}
							}
							// Job Card Reg Number....
							if (h == 8) {
								jc_reg_no = "";
								jc_reg_no = PadQuotes(sheetData[j][h]);
								if (jc_reg_no.equals("null") || jc_reg_no.equals("0") || jc_reg_no.equals("Reg. Num")) {
									jc_reg_no = "";
								}
								// SOP("jc_reg_no===" + jc_reg_no);
							}

							// Chassis Number....
							if (h == 9) {
								jc_chassis_no = "";
								jc_chassis_no = PadQuotes(sheetData[j][h]);
								if (jc_chassis_no.equals("null") || jc_chassis_no.equals("0") || jc_chassis_no.equals("Chassis No.")) {
									jc_chassis_no = "";
								}
								// SOP("jc_chassis_no==" + jc_chassis_no);
							}

							// Model Name....
							if (h == 10) {
								item_name = "";
								item_name = PadQuotes(sheetData[j][h]);
								if (item_name.equals("null") || item_name.equals("0") || item_name.equals("Model & Engine Type")) {
									item_name = "";
								}
								// SOP("item_service_code===" + item_service_code);
							}

							// Mileage..
							if (h == 12) {
								jc_kms = "0";
								jc_kms = PadQuotes(sheetData[j][h]);
								if (jc_kms.equals("null") || jc_kms.equals("") || jc_kms.equals("Mileage")) {
									jc_kms = "0";
								}
								// SOP("jc_kms===" + jc_kms);
							}

							// Sale Date....
							if (h == 13) {
								veh_sale_date = "";
								veh_sale_date = PadQuotes(sheetData[j][h]);
								if (veh_sale_date.equals("null") || veh_sale_date.equals("0") || veh_sale_date.equals("Sale Date")) {
									veh_sale_date = "";
								}
								// SOP("veh_sale_date==" + veh_sale_date);
							}

							// Customer Catagory....
							if (h == 14) {// ///
								jc_jccat_name = "";
								jc_jccat_name = PadQuotes(sheetData[j][h]);
								if (jc_jccat_name.equals("null") || jc_jccat_name.equals("Customer Category")) {
									jc_jccat_name = "";
								}
								// SOP("jc_jccat_name==" + jc_jccat_name);
							}

							// Service Advisor....
							if (h == 15) {
								jc_emp_name = "";
								jc_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_emp_name.equals("null") || jc_emp_name.equals("0") || jc_emp_name.equals("Service Advisor")) {
									jc_emp_name = "";
								}
								// SOP("jc_emp_name===" + jc_emp_name);
							}

							// Technician....
							if (h == 16) {
								jc_technician_emp_name = "";
								jc_technician_emp_name = PadQuotes(sheetData[j][h]);
								if (jc_technician_emp_name.equals("null") || jc_technician_emp_name.equals("0") || jc_technician_emp_name.equals("Technician")) {
									jc_technician_emp_name = "";
								}
								// SOP("jc_technician_emp_name===" + jc_technician_emp_name);
							}

							// Customer Name....
							if (h == 17) {
								customer_name = "";
								customer_name = PadQuotes(sheetData[j][h]);
								if (customer_name.equals("null") || customer_name.equals("0") || customer_name.equals("Customer Name")) {
									customer_name = "";
								}
								// SOP("customer_name====" + customer_name);
							}

							// Address....
							if (h == 18) {
								contact_address = "";
								contact_address = PadQuotes(sheetData[j][h]);
								// SOP("customer_address====" + contact_address);
								if (contact_address.equals("null") || contact_address.equals("0") || contact_address.equals("Address")) {
									contact_address = "";
								}
							}

							// Telephone Number....
							if (h == 19) {
								contact_phone1 = "";
								contact_phone1 = PadQuotes(sheetData[j][h]);
								// SOP("contact_phone1====" + contact_phone1);
							}

							// Mobile Number....
							if (h == 20) {
								contact_mobile1 = "";
								contact_mobile1 = PadQuotes(sheetData[j][h]);
								// SOP("contact_mobile1====" + contact_mobile1);
							}

							// Email Id....
							if (h == 21) {
								contact_email1 = "";
								contact_email1 = PadQuotes(sheetData[j][h]);
								// SOP("contact_email1====" + contact_email1);
							}

							// Car User Name....
							if (h == 22) {
								caruser_name = "";
								caruser_name = PadQuotes(sheetData[j][h]);
								// SOP("caruser_name===" + caruser_name);
								if (caruser_name.equals("null") || caruser_name.equals("0") || caruser_name.equals("Car User Name")) {
									caruser_name = "";
								}
							}

							// Date Of Birth....
							if (h == 24) {
								contact_dob = "";
								contact_dob = PadQuotes(sheetData[j][h]);
								if (contact_dob.equals("null") || contact_dob.equals("0") || contact_dob.equals("Date of Birth")) {
									contact_dob = "";
								}
								// SOP("contact_dob===" + contact_dob);
							}

							// Date Of Anniversary....
							if (h == 25) {
								contact_anniversary = "";
								contact_anniversary = PadQuotes(sheetData[j][h]);
								if (contact_anniversary.equals("null") || contact_anniversary.equals("0") || contact_anniversary.equals("Date Of Anniversary")) {
									contact_anniversary = "";
								}
								// SOP("contact_anniversary===" + contact_anniversary);
							}

							// Service Type....
							if (h == 26) {
								jc_jctype_name = "";
								jc_jctype_name = PadQuotes(sheetData[j][h]);
								// SOP("jc_jctype_name==" + jc_jctype_name);
								if (jc_jctype_name.equals("null") || jc_jctype_name.equals("0") || jc_jctype_name.equals("Service Type")) {
									jc_jctype_name = "";
								}
							}

							// Defect Details....
							if (h == 27) {
								jc_advice = "";
								jc_advice = PadQuotes(sheetData[j][h]);
								if (jc_advice.equals("null") || jc_advice.equals("0") || jc_advice.equals("Defect Details")) {
									jc_advice = "";
								}
								// SOP("jc_advice===" + jc_advice);
							}

							// Job Card Remarks....
							if (h == 28) {
								jc_notes = "";
								jc_notes = PadQuotes(sheetData[j][h]);
								// SOP("jc_notes=11=="+jc_notes);
								if (jc_notes.equals("null") || jc_notes.equals("0") || jc_notes.equals("0")) {
									jc_notes = "";
								}
								jc_notes = caruser_name + " \n" + jc_notes;
								// SOP("jc_notes=22==" + jc_notes);
							}

							// Labor Details.....
							if (h == 29) {
								String labour_details = "";
								labour_details = PadQuotes(sheetData[j][h]);
								if (labour_details.equals("null") || labour_details.equals("0") || labour_details.equals("Labor Details")) {
									labour_details = "";
								}
								// SOP("labour_details===" + labour_details);
							}

						}
						// Start Here Parts Feilds
						if (format_maruti.equals("Labour Format") && msg.equals("")) {
							if (j == 5 && h == 0) {
								if (PadQuotes(sheetData[j][0]).equals("Srl No")) {

									for (int i = 23, k = 1; i < columnLength; i++) {
										if (sheetData[j][i].contains("CGST") && sheetData[j][i].contains("14")) {
											taxmap.put(k++, "14");
										}
										if (sheetData[j][i].contains("CGST") && sheetData[j][i].contains("2.5")) {
											taxmap.put(k++, "2.5");
										}
										if (sheetData[j][i].contains("CGST") && sheetData[j][i].contains("9")) {
											taxmap.put(k++, "9");
										}
										if (sheetData[j][i].contains("IGST") && sheetData[j][i].contains("18")) {
											taxmap.put(k++, "18");
										}
										if (sheetData[j][i].contains("IGST") && sheetData[j][i].contains("28")) {
											taxmap.put(k++, "28");
										}
										if (sheetData[j][i].contains("IGST") && sheetData[j][i].contains("5")) {
											taxmap.put(k++, "5");
										}
										if (sheetData[j][i].contains("SGST") && sheetData[j][i].contains("14")) {
											taxmap.put(k++, "14");
										}
										if (sheetData[j][i].contains("SGST") && sheetData[j][i].contains("2.5")) {
											taxmap.put(k++, "2.5");
										}
										if (sheetData[j][i].contains("SGST") && sheetData[j][i].contains("9")) {
											taxmap.put(k++, "9");
										}
										if (sheetData[j][i].contains("SGST") && sheetData[j][i].contains("6")) {
											taxmap.put(k++, "6");
										}
										if (sheetData[j][i].contains("CGST") && sheetData[j][i].contains("6")) {
											taxmap.put(k++, "6");
										}
										if (sheetData[j][i].contains("IGST") && sheetData[j][i].contains("12")) {
											taxmap.put(k++, "12");
										}
									}
								}
							}
							if (j >= 7) {
								// Bill Type
								if (h == 1) {
									billtype = "";
									billtype = PadQuotes(sheetData[j][h]);
									if (billtype.equals("INS")) {
										billtype = "Insurance";
									} else if (billtype.equals("CUS")) {
										billtype = "Cash";
									} else if (billtype.equals("EWR")) {
										billtype = "Extended Warranty";
									}
									// SOP("BILLTYPE=123==" + billtype);
								}
								if (billtype.equals("Insurance") || billtype.equals("Cash") || billtype.equals("Bill Type")
										|| billtype.equals("Extended Warranty")) {
									// Customer id
									if (h == 2) {
										customer_id = "0";
										customer_id = PadQuotes(sheetData[j][h]);
										// SOP("customer_id===" + customer_id);
									}
									// party name
									if (h == 3) {
										customer_name = "";
										voucher_customer_id = "0";
										voucher_contact_id = "0";
										voucher_billing_add = "";
										gstno = "";
										customer_name = PadQuotes(sheetData[j][h]);
										// SOP("customer_name===" + customer_name);
									}
									// place of supply
									if (h == 4) {
										contact_state = "";
										state_id = "0";
										contact_state = PadQuotes(sheetData[j][h]);
										StrSql = "SELECT state_id"
												+ " FROM " + compdb(comp_id) + "axela_state"
												+ " WHERE state_name LIKE '%" + contact_state + "%'";
										// SOP("StrSql==state=" + StrSql);
										state_id = CNumeric(ExecuteQuery(StrSql));
										if (!state_id.equals("0")) {
											StrSql = "SELECT city_id"
													+ " FROM " + compdb(comp_id) + "axela_state"
													+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_state_id = state_id"
													+ " WHERE state_id = " + state_id
													+ " LIMIT 1";
											// SOP("StrSql==City=" + StrSql);
											customer_city_id = CNumeric(ExecuteQuery(StrSql));
											if (customer_city_id.equals("0")) {
												error_msg += "No City is present for this State! <br>";
											}
										} else {
											error_msg += "Invalid State name! <br>";
										}
									}
									// bill num
									if (h == 5) {
										bill_no = "";
										bill_num = "";
										branchcode = "";
										bill_no = PadQuotes(sheetData[j][h]);
										// SOP("bill_no===" + bill_no);
										if (!bill_no.equals("")) {
											if (bill_no.contains("/")) {
												if (bill_no.split("/").length == 3) {
													branchcode = bill_no.split("/")[0];
													billtype = bill_no.split("/")[1];
													if (billtype.equals("BI")) {
														billtype = "Insurance";
													} else if (billtype.equals("BR")) {
														billtype = "Cash";
													} else if (billtype.equals("EW")) {
														billtype = "Extended Warranty";
													}
													bill_num = bill_no.split("/")[2];
												}
											}
										} else {
											error_msg += "Bill No. should not be empty! <br>";
										}
										// SOP("bill_no==125=" + bill_no);
									}
									if (billtype.equals("Insurance") || billtype.equals("Cash")) {
										// Bill Date
										if (h == 6) {
											jc_bill_cash_date = "";
											jc_bill_cash_date = PadQuotes(sheetData[j][h]);
											if (jc_bill_cash_date.equals("null") || jc_bill_cash_date.equals("") || jc_bill_cash_date.equals("Bill Date")) {
												jc_bill_cash_date = "";
												error_msg += "Bill Date should not be empty! <br>";
											}
											// SOP("jc_bill_cash_date==111==" + jc_bill_cash_date);
											if (!jc_bill_cash_date.equals("")) {
												if (jc_bill_cash_date.contains("/")) {
													if (isValidDateFormatShort(jc_bill_cash_date)) {
														jc_bill_cash_date = ConvertShortDateToStr(jc_bill_cash_date);
														// SOP("jc_bill_cash_date==222==" + jc_bill_cash_date);
													} else if (jc_bill_cash_date.split("/").length == 3) {
														month = jc_bill_cash_date.split("/")[0];
														if (month.length() == 1) {
															month = "0" + month;
														}
														day = jc_bill_cash_date.split("/")[1];
														if (day.length() == 1) {
															day = "0" + day;
														}
														year = jc_bill_cash_date.split("/")[2];
														if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
															jc_bill_cash_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
														} else {
															error_msg += "Invalid Bill Date! <br>";
														}
														day = "";
														month = "";
														year = "";
														// SOP("jc_bill_cash_date==333==" + jc_bill_cash_date);
													}
												} else if (jc_bill_cash_date.length() == 14) {
													if (isValidDateFormatStr(jc_bill_cash_date)) {
														jc_bill_cash_date = jc_bill_cash_date + "";
													}
													// SOP("jc_bill_cash_date==444==" + jc_bill_cash_date);
												} else if (jc_bill_cash_date.contains(".")) {
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
															jc_bill_cash_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
															jc_bill_cash_date = jc_bill_cash_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
														} else {
															error_msg += "Invalid Bill Date! <br>";
														}

														day = "";
														month = "";
														year = "";
													}
													// SOP("jc_bill_cash_date==555==" + jc_bill_cash_date);
												} else {
													jc_bill_cash_date = fmtShr3tofmtShr1(jc_bill_cash_date);
													if (isValidDateFormatStr(jc_bill_cash_date)) {
														jc_bill_cash_date = jc_bill_cash_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
													} else {
														error_msg += "Invalid Bill Date! <br>";
													}
													// SOP("jc_bill_cash_date==666==" + jc_bill_cash_date);
												}
											}
										}

										// Reg No.
										if (h == 7) {
											jc_reg_no = "";
											voucher_customer_id = "0";
											voucher_contact_id = "0";
											voucher_billing_add = "";
											gstno = "";
											contact_state_id = "0";
											jc_reg_no = PadQuotes(sheetData[j][h]);
											// SOP("jc_bill_cash_date==" + jc_bill_cash_date);
											if (!jc_reg_no.equals("") && billtype.equals("Cash")) {
												StrSql = "SELECT customer_id, contact_id, customer_address, customer_gst_no, city_state_id"
														+ " FROM " + compdb(comp_id) + "axela_service_jc"
														+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id  = jc_customer_id"
														+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
														+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id  = jc_contact_id"
														+ " WHERE 1=1"
														+ "	AND jc_reg_no = '" + jc_reg_no + "'"
														// + " AND SUBSTR(jc_time_in, 1, 8) = '" + jc_bill_cash_date.substring(0, 8) + "'"
														+ " AND jc_branch_id=" + jc_branch_id;
												// SOP("StrSql==customer=" + StrSql);
												CachedRowSet crs = processQuery(StrSql, 0);
												if (crs.next()) {
													voucher_customer_id = crs.getString("customer_id");
													voucher_contact_id = crs.getString("contact_id");
													voucher_billing_add = crs.getString("customer_address");
													gstno = crs.getString("customer_gst_no");
													contact_state_id = crs.getString("city_state_id");
												} else {
													error_msg += "No Customer found with name: " + customer_name + "<br>";
												}
												crs.close();
											} else if (jc_reg_no.equals("")) {
												error_msg += "Reg. No. should not be empty! <br>";
											}

										}
										// GST num
										if (h == 8) {
											customer_gst_no = "";
											customer_gst_no = PadQuotes(sheetData[j][h]);
											if (customer_gst_no.equals("GSTUNREGISTERED")) {
												customer_gst_no = "";
											} else if (!customer_gst_no.equals("") && !customer_gst_no.matches("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z0-9]{3}")) {
												error_msg += "Invalid GST No.! <br>";
											}

										}
										// Deductible
										if (h == 9) {

										}
										// salvage
										if (h == 10) {

										}

										// basic details//

										// taxable parts
										if (h == 11) {
											partsamt = "0";
											partsamt = PadQuotes(sheetData[j][h]);
											if (partsamt.equals("")) {
												partsamt = "0";
											}
										}
										// taxpaid parts
										if (h == 12) {
											partscharges = "0";
											partscharges = PadQuotes(sheetData[j][h]);
											if (partscharges.equals("")) {
												partscharges = "0";
											}
										}
										// basic labour
										if (h == 13) {
											labouramt = "0";
											labouramt = PadQuotes(sheetData[j][h]);
											if (labouramt.equals("")) {
												labouramt = "0";
											}
										}
										// total
										if (h == 14) {
											totalbasicamt = "0";
											totalbasicamt = PadQuotes(sheetData[j][h]);
											if (totalbasicamt.equals("")) {
												totalbasicamt = "0";
											}
										}

										// Discount details//
										// taxable parts
										if (h == 15) {
											partsdiscount = "0";
											partsdiscount = PadQuotes(sheetData[j][h]);
											if (partsdiscount.equals("")) {
												partsdiscount = "0";
											}
										}
										// taxpaid parts
										if (h == 16) {
											partsdiscountcharges = "0";
											partsdiscountcharges = PadQuotes(sheetData[j][h]);
											if (partsdiscountcharges.equals("")) {
												partsdiscountcharges = "0";
											}

										}
										// Discount labour
										if (h == 17) {
											labourdiscount = "0";
											labourdiscount = PadQuotes(sheetData[j][h]);
											if (labourdiscount.equals("")) {
												labourdiscount = "0";
											}
										}
										// total
										if (h == 18) {
											totaldiscountamt = "0";
											totaldiscountamt = PadQuotes(sheetData[j][h]);
											if (totaldiscountamt.equals("")) {
												totaldiscountamt = "0";
											}

										}
										// Gross details//
										// taxable parts
										if (h == 19) {
											grosspartsamt = "0";
											grosspartsamt = PadQuotes(sheetData[j][h]);
											if (grosspartsamt.equals("")) {
												grosspartsamt = "0";
											}
										}
										// taxpaid parts
										if (h == 20) {
											grosspartscharges = "0";
											grosspartscharges = PadQuotes(sheetData[j][h]);
											if (grosspartscharges.equals("")) {
												grosspartscharges = "0";
											}
										}
										// Gross labour
										if (h == 21) {
											grosslabour = "0";
											grosslabour = PadQuotes(sheetData[j][h]);
											if (grosslabour.equals("")) {
												grosslabour = "0";
											}
										}
										// total
										if (h == 22) {
											totalgrossamt = "0";
											totalgrossamt = PadQuotes(sheetData[j][h]);
											if (totalgrossamt.equals("")) {
												totalgrossamt = "0";
											}
										}
										// for taxes

										if (h == 23) {
											int m = h;
											for (int l = 1; l <= taxmap.size() && m < columnLength - 8; l++) {
												for (int k = 0; k < 4; k++) {
													if (taxmap.get(l).equals("14")) {
														if (k == 0) {
															taxpartamtC14 = "0";
															taxpartamtC14 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtC14.equals("")) {
																taxpartamtC14 = "0";
															}
															// SOP("taxpartamtC14==" + taxpartamtC14);
															taxpartamtS14 = taxpartamtC14;
														}
														if (k == 1) {
															taxpartschargesC14 = "0";
															taxpartschargesC14 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesC14.equals("")) {
																taxpartschargesC14 = "0";
															}
															// SOP("taxpartschargesC14==" + taxpartschargesC14);
															taxpartschargesS14 = taxpartschargesC14;
														}
														if (k == 2) {
															taxlabouramtC14 = "0";
															taxlabouramtC14 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtC14.equals("")) {
																taxlabouramtC14 = "0";
															}
															// SOP("taxlabouramtC14==" + taxlabouramtC14);
															taxlabouramtS14 = taxlabouramtC14;
														}
														if (k == 3) {
															totaltaxC14 = "0";
															totaltaxC14 = PadQuotes(sheetData[j][m++]);
															if (totaltaxC14.equals("")) {
																totaltaxC14 = "0";
															}
															// SOP("totaltaxC14==" + totaltaxC14);
															totaltaxS14 = totaltaxC14;
														}
													}
													if (taxmap.get(l).equals("28")) {
														if (k == 0) {
															taxpartamtI28 = "0";
															taxpartamtI28 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtI28.equals("")) {
																taxpartamtI28 = "0";
															}
															// SOP("taxpartamtI28==" + taxpartamtI28);
														}
														if (k == 1) {
															taxpartschargesI28 = "0";
															taxpartschargesI28 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesI28.equals("")) {
																taxpartschargesI28 = "0";
															}
															// SOP("taxpartschargesI28==" + taxpartschargesI28);
														}
														if (k == 2) {
															taxlabouramtI28 = "0";
															taxlabouramtI28 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtI28.equals("")) {
																taxlabouramtI28 = "0";
															}
															// SOP("taxlabouramtI28==" + taxlabouramtI28);
														}
														if (k == 3) {
															totaltaxI28 = "0";
															totaltaxI28 = PadQuotes(sheetData[j][m++]);
															if (totaltaxI28.equals("")) {
																totaltaxI28 = "0";
															}
															// SOP("totaltaxI28==" + totaltaxI28);
														}
													}
													if (taxmap.get(l).equals("9")) {
														if (k == 0) {
															taxpartamtC9 = "0";
															taxpartamtC9 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtC9.equals("")) {
																taxpartamtC9 = "0";
															}
															// SOP("taxpartamtC9==" + taxpartamtC9);
															taxpartamtS9 = taxpartamtC9;
														}
														if (k == 1) {
															taxpartschargesC9 = "0";
															taxpartschargesC9 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesC9.equals("")) {
																taxpartschargesC9 = "0";
															}
															// SOP("taxpartschargesC9==" + taxpartschargesC9);
															taxpartschargesS9 = taxpartschargesC9;
														}
														if (k == 2) {
															taxlabouramtC9 = "0";
															taxlabouramtC9 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtC9.equals("")) {
																taxlabouramtC9 = "0";
															}
															// SOP("taxlabouramtC9==" + taxlabouramtC9);
															taxlabouramtS9 = taxlabouramtC9;
														}
														if (k == 3) {
															totaltaxC9 = "0";
															totaltaxC9 = PadQuotes(sheetData[j][m++]);
															if (totaltaxC9.equals("")) {
																totaltaxC9 = "0";
															}
															// SOP("totaltaxC9==" + totaltaxC9);
															totaltaxS9 = totaltaxC9;
														}
													}
													if (taxmap.get(l).equals("18")) {
														if (k == 0) {
															taxpartamtI18 = "0";
															taxpartamtI18 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtI18.equals("")) {
																taxpartamtI18 = "0";
															}
															// SOP("taxpartamtI18==" + taxpartamtI18);
														}
														if (k == 1) {
															taxpartschargesI18 = "0";
															taxpartschargesI18 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesI18.equals("")) {
																taxpartschargesI18 = "0";
															}
															// SOP("taxpartschargesI18==" + taxpartschargesI18);
														}
														if (k == 2) {
															taxlabouramtI18 = "0";
															taxlabouramtI18 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtI18.equals("")) {
																taxlabouramtI18 = "0";
															}
															// SOP("taxlabouramtI18==" + taxlabouramtI18);
														}
														if (k == 3) {
															totaltaxI18 = "0";
															totaltaxI18 = PadQuotes(sheetData[j][m++]);
															if (totaltaxI18.equals("")) {
																totaltaxI18 = "0";
															}
															// SOP("totaltaxI18==" + totaltaxI18);
														}
													}
													if (taxmap.get(l).equals("2.5")) {
														if (k == 0) {
															taxpartamtC2_5 = "0";
															taxpartamtC2_5 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtC2_5.equals("")) {
																taxpartamtC2_5 = "0";
															}
															taxpartamtS2_5 = taxpartamtC2_5;
															// SOP("taxpartamtC2_5==" + taxpartamtC2_5);
														}
														if (k == 1) {
															taxpartschargesC2_5 = "0";
															taxpartschargesC2_5 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesC2_5.equals("")) {
																taxpartschargesC2_5 = "0";
															}
															// SOP("taxpartschargesC2_5==" + taxpartschargesC2_5);
															taxpartschargesS2_5 = taxpartschargesC2_5;
														}
														if (k == 2) {
															taxlabouramtC2_5 = "0";
															taxlabouramtC2_5 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtC2_5.equals("")) {
																taxlabouramtC2_5 = "0";
															}
															// SOP("taxlabouramtC2_5==" + taxlabouramtC2_5);
															taxlabouramtS2_5 = taxlabouramtC2_5;
														}
														if (k == 3) {
															totaltaxC2_5 = "0";
															totaltaxC2_5 = PadQuotes(sheetData[j][m++]);
															if (totaltaxC2_5.equals("")) {
																totaltaxC2_5 = "0";
															}
															// SOP("totaltaxC2_5==" + totaltaxC2_5);
															totaltaxS2_5 = totaltaxC2_5;
														}
													}
													if (taxmap.get(l).equals("5")) {
														if (k == 0) {
															taxpartamtI5 = "0";
															taxpartamtI5 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtI5.equals("")) {
																taxpartamtI5 = "0";
															}
															// SOP("taxpartamtI5==" + taxpartamtI5);
														}
														if (k == 1) {
															taxpartschargesI5 = "0";
															taxpartschargesI5 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesI5.equals("")) {
																taxpartschargesI5 = "0";
															}
															// SOP("taxpartschargesI5==" + taxpartschargesI5);
														}
														if (k == 2) {
															taxlabouramtI5 = "0";
															taxlabouramtI5 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtI5.equals("")) {
																taxlabouramtI5 = "0";
															}
															// SOP("taxlabouramtI5==" + taxlabouramtI5);
														}
														if (k == 3) {
															totaltaxI5 = "0";
															totaltaxI5 = PadQuotes(sheetData[j][m++]);
															if (totaltaxI5.equals("")) {
																totaltaxI5 = "0";
															}
															// SOP("totaltaxI5==" + totaltaxI5);
														}
													}
													if (taxmap.get(l).equals("6")) {
														if (k == 0) {
															taxpartamtC6 = "0";
															taxpartamtC6 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtC6.equals("")) {
																taxpartamtC6 = "0";
															}
															// SOP("taxpartamtC6==" + taxpartamtC6);
															taxpartamtS6 = taxpartamtC6;
														}
														if (k == 1) {
															taxpartschargesC6 = "0";
															taxpartschargesC6 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesC6.equals("")) {
																taxpartschargesC6 = "0";
															}
															// SOP("taxpartschargesC6==" + taxpartschargesC6);
															taxpartschargesS6 = taxpartschargesC6;
														}
														if (k == 2) {
															taxlabouramtC6 = "0";
															taxlabouramtC6 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtC6.equals("")) {
																taxlabouramtC6 = "0";
															}
															// SOP("taxlabouramtC6==" + taxlabouramtC6);
															taxlabouramtS6 = taxlabouramtC6;
														}
														if (k == 3) {
															totaltaxC6 = "0";
															totaltaxC6 = PadQuotes(sheetData[j][m++]);
															if (totaltaxC6.equals("")) {
																totaltaxC6 = "0";
															}
															// SOP("totaltaxC6==" + totaltaxC6);
															totaltaxS6 = totaltaxC6;
														}
													}
													if (taxmap.get(l).equals("12")) {
														if (k == 0) {
															taxpartamtI12 = "0";
															taxpartamtI12 = PadQuotes(sheetData[j][m++]);
															if (taxpartamtI12.equals("")) {
																taxpartamtI12 = "0";
															}
															// SOP("taxpartamtI12==" + taxpartamtI12);
														}
														if (k == 1) {
															taxpartschargesI12 = "0";
															taxpartschargesI12 = PadQuotes(sheetData[j][m++]);
															if (taxpartschargesI12.equals("")) {
																taxpartschargesI12 = "0";
															}
															// SOP("taxpartschargesI12==" + taxpartschargesI12);
														}
														if (k == 2) {
															taxlabouramtI12 = "0";
															taxlabouramtI12 = PadQuotes(sheetData[j][m++]);
															if (taxlabouramtI12.equals("")) {
																taxlabouramtI12 = "0";
															}
															// SOP("taxlabouramtI12==" + taxlabouramtI12);
														}
														if (k == 3) {
															totaltaxI12 = "0";
															totaltaxI12 = PadQuotes(sheetData[j][m++]);
															if (totaltaxI12.equals("")) {
																totaltaxI12 = "0";
															}
															// SOP("totaltaxI12==" + totaltaxI12);
														}
													}
												}
											}
										}
										// Rounded off details//
										// taxable parts
										if (h == columnLength - 8) {
											partamtroundoff = "0";
											partamtroundoff = PadQuotes(sheetData[j][h]);
										}
										// taxpaid parts
										if (h == columnLength - 7) {
											partschargesroundoff = "0";
											partschargesroundoff = PadQuotes(sheetData[j][h]);
										}
										// Rounded off labour
										if (h == columnLength - 6) {
											labouramtroundoff = "0";
											labouramtroundoff = PadQuotes(sheetData[j][h]);
										}
										// total
										if (h == columnLength - 5) {
											totalroundoff = "0";
											totalroundoff = PadQuotes(sheetData[j][h]);
										}

										// Net Bill Amount details//
										// taxable parts
										if (h == columnLength - 4) {
											netpartsamt = "0";
											netpartsamt = PadQuotes(sheetData[j][h]);
											// SOP("netpartsamt===" + netpartsamt);
										}
										// taxpaid parts
										if (h == columnLength - 3) {
											netpartscharge = "0";
											netpartscharge = PadQuotes(sheetData[j][h]);
											// SOP("netpartscharge===" + netpartscharge);
										}
										// Net Bill Amount labour
										if (h == columnLength - 2) {
											netlabouramt = "0";
											netlabouramt = PadQuotes(sheetData[j][h]);
											// SOP("netlabouramt===" + netlabouramt);
										}
										// total
										if (h == columnLength - 1) {
											grandtotal = "0";
											grandtotal = PadQuotes(sheetData[j][h]);
											if (grandtotal.equals("")) {
												grandtotal = "0";
											}
											// SOP("grandtotal===" + grandtotal);
										}
									} else if (billtype.equals("Extended Warranty")) {
										if (error_msg.equals("")) {
											error_msg += "Invalid Bill No.<br>";
										}
									}
								}
							}
						}
					}
					jc_entry_date = "";
					jc_entry_date = ToLongDate(kknow());
					if (format_maruti.equals("Labour Format") && (billtype.equals("Insurance")
							|| billtype.equals("Cash") || billtype.equals("Extended Warranty"))) {
						if (!bill_no.equals("") && error_msg.equals("")) {
							LabourFormat(request, response);
						}
						if (!error_msg.equals("")) {
							jc_error_msg += "<br>" + ++count + "." + " Bill No. : " + bill_no + "==><br>" + error_msg;
						}
					}
					if (!jc_ro_no.equals("") && format_maruti.equals("Jobcard Format")) {
						jc_error_msg += obj.ValidateSheetData(comp_id, customer_name, contact_mobile1, contact_email1,
								contact_phone1, contact_address, contact_dob, contact_anniversary, contact_city, contact_pin,
								veh_engine_no, veh_sale_date, item_name, jc_branch_id,
								jc_reg_no, jc_chassis_no, jc_ro_no, jc_emp_name,
								jc_technician_emp_name, jc_time_in, jc_bill_cash_no, jc_bill_cash_date, jc_jctype_name,
								jc_jccat_name, jc_kms, jc_grandtotal, jc_bill_cash_labour,
								jc_bill_cash_parts, jc_bill_cash_valueadd, jc_bill_cash_oil, jc_bill_cash_tyre,
								jc_bill_cash_accessories, jc_bill_cash_labour_discamt, jc_notes, jc_advice,
								jc_entry_id, jc_entry_date, error_msg);
						propcount = obj.propcount;
					}
					if (format_maruti.equals("Labour Format") && !PadQuotes(sheetData[6][22]).equals("Total")) {
						msg += "<br>" + "Document columns doesn't match with the template!";
						break;
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public String Addcustomer() throws SQLException {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_gst_no,"
				+ " customer_city_id,"
				+ " customer_since,"
				+ " customer_accgroup_id,"
				+ " customer_type,"
				+ " customer_active,"
				+ " customer_notes,"
				+ " customer_entry_id,"
				+ " customer_entry_date)"
				+ " VALUES"
				+ " (" + jc_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + customer_gst_no + "',"
				+ " " + customer_city_id + ","
				+ " '" + ToShortDate(kknow()) + "',"
				+ " 31,"// customer_accgroup_id
				+ " 1," // customer_type
				+ " '1'," // customer_active
				+ " '',"
				+ " " + jc_entry_id + ","
				+ " '" + jc_entry_date + "')";
		// SOP("StrSql==INSERT INTO customer==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			customer_id = rs.getString(1);
		}
		rs.close();
		return customer_id;

	}

	public String PopulateFormatMaruti(String compid, String format_maruti) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"\">Select</option>\n");
		Str.append("<option value=\"Jobcard Format\"").append(StrSelectdrop("Jobcard Format", format_maruti)).append(">Job Card Format</option>\n");
		Str.append("<option value=\"Labour Format\"").append(StrSelectdrop("Labour Format", format_maruti)).append(">Labour Format</option>\n");
		return Str.toString();
	}
	public void LabourFormat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!bill_no.equals("")) {
			jc_id = "0";
			try {
				StrSql = "SELECT jc_id"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE 1=1";
				if (billtype.equals("Cash")) {// For bill type cash
					StrSql += " AND jc_bill_cash_no = 'BR/" + bill_num + "'";
				} else if (billtype.equals("Insurance")) {// For bill type insur
					StrSql += " AND jc_reg_no='" + jc_reg_no + "'";
				}
				StrSql += " AND jc_branch_id=" + jc_branch_id;
				// + " AND SUBSTR(jc_time_in, 1, 8) = '" + jc_bill_cash_date.substring(0, 8) + "'"

				// SOP("jc==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						jc_id = CNumeric(crs.getString("jc_id"));
					}
				}
				crs.close();

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				if (!jc_id.equals("0") && error_msg.equals("")) {
					if (billtype.equals("Insurance")) {
						StrSql = "SELECT customer_id, contact_id, customer_address, customer_gst_no, city_state_id"
								+ " FROM " + compdb(comp_id) + "axela_customer"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id  = customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
								+ " WHERE 1=1"
								+ "	AND customer_name = '" + customer_name + "'"
								+ " LIMIT 1";
						// SOP("insurance customer==" + StrSql);
						crs = processQuery(StrSql, 0);
						if (crs.next()) {
							voucher_customer_id = crs.getString("customer_id");
							voucher_contact_id = crs.getString("contact_id");
							voucher_billing_add = crs.getString("customer_address");
							gstno = crs.getString("customer_gst_no");
							contact_state_id = crs.getString("city_state_id");
						}
						else if (voucher_customer_id.equals("0")) {
							voucher_customer_id = Addcustomer();
						}
						crs.close();
					}
					StrSql = "SELECT customer_name"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE 1=1"
							+ "	AND customer_id = " + voucher_customer_id;

					customer_check = ExecuteQuery(StrSql);
					// SOP("customer_check==" + customer_check);
					// SOP("customer_name==" + customer_name);
					if (!customer_check.trim().equals(customer_name) && !customer_check.equals("")) {
						error_msg += "Customer name is not matching!<br>";
					}

					if (error_msg.equals("")) {
						AddLabourTrans(request, response);
						// if (!gstno.equals("") && !gstno.equals(customer_gst_no)) {
						// error_msg += customer_name + " is associated with other GST No.!<br>";
						// } else if (gstno.equals("") && error_msg.equals("")) {//for temporary
						StrSql = " UPDATE " + compdb(comp_id) + "axela_customer"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
								+ " SET";
						// if (!customer_gst_no.equals("")) {
						// StrSql += " customer_gst_no = '" + customer_gst_no + "',";
						// }
						if (!state_id.equals(contact_state_id) && !customer_city_id.equals("0")) {
							StrSql += " customer_city_id = " + customer_city_id + ",";
						}
						StrSql += " customer_modified_id = " + jc_entry_id + ","
								+ " customer_modified_date = '" + jc_entry_date + "'"
								+ " WHERE customer_id = " + voucher_customer_id;
						stmttx.execute(StrSql);
						// }
						if (billtype.equals("Cash")) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET  jc_bill_cash_labour = " + grosslabour + ","
									+ " jc_bill_cash_parts = " + grosspartsamt + ",";
							// if (!bill_no.equals("")) {
							// StrSql += " jc_bill_cash_no = '" + bill_no + "',";
							// }
							if (!labourdiscount.equals("")) {
								StrSql += " jc_bill_cash_labour_discamt = " + labourdiscount + ",";
							}
							if (!partsdiscount.equals("")) {
								StrSql += " jc_bill_cash_parts_discamt = " + partsdiscount + ",";
							}
							if (!totaldiscountamt.equals("")) {
								StrSql += " jc_discamt = " + totaldiscountamt + ",";
							}
							if (!customer_name.equals("")) {
								StrSql += " jc_bill_cash_customername = '" + customer_name + "',";
							}
							StrSql += " jc_bill_cash_date = '" + jc_bill_cash_date + "',"
									+ " jc_grandtotal= " + grandtotal + ","
									+ " jc_modified_id = " + jc_entry_id + ","
									+ " jc_modified_date = '" + jc_entry_date + "'"
									+ " WHERE jc_id = " + jc_id;
							stmttx.execute(StrSql);
							// SOP("StrSql=jccash=" + StrSql);
						} else if (billtype.equals("Insurance")) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET  jc_bill_insur_labour = " + grosslabour + ","
									+ " jc_bill_insur_parts = " + grosspartsamt + ",";
							if (!bill_no.equals("")) {
								StrSql += " jc_bill_insur_no = '" + bill_no + "',";
							}
							if (!labourdiscount.equals("")) {
								StrSql += " jc_bill_insur_labour_discamt = " + labourdiscount + ",";
							}
							if (!partsdiscount.equals("")) {
								StrSql += " jc_bill_insur_parts_discamt = " + partsdiscount + ",";
							}
							if (!totaldiscountamt.equals("")) {
								StrSql += " jc_discamt = " + totaldiscountamt + ",";
							}
							if (!customer_name.equals("")) {
								StrSql += " jc_bill_insur_customername = '" + customer_name + "',";
							}
							StrSql += " jc_bill_insur_date = '" + jc_bill_cash_date + "',"
									+ " jc_grandtotal= " + grandtotal + ","
									+ " jc_modified_id = " + jc_entry_id + ","
									+ " jc_modified_date = '" + jc_entry_date + "'"
									+ " WHERE jc_id = " + jc_id;
							// SOP("StrSql=jcinsur=" + StrSql);
							stmttx.execute(StrSql);

						}
						conntx.commit();

					}
				}
				else {
					error_msg += "Job Card is not Present<br>";
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
	}
	public void AddLabourTrans(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			double jctrans_qty = 1.00, // for maruti
			jctrans_price = 0.00, jctrans_amount = 0.00;
			double jctrans_netprice = 0.00, jctrans_discountamount = 0.00, jctranstaxamountlabour = 0.00, jctranstaxamountparts = 0.00;
			jctrans_price = Double.parseDouble(labouramt);
			jctrans_amount = Double.parseDouble(labouramt);
			jctrans_netprice = Double.parseDouble(grosslabour);
			jctrans_discountamount = Double.parseDouble(labourdiscount);
			jctrans_taxamount1.clear();
			if (!taxlabouramtC2_5.equals("0") && isNumeric(taxlabouramtC2_5)) {
				// SOP("taxC2_5");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtC2_5));
			}
			if (!taxlabouramtC6.equals("0") && isNumeric(taxlabouramtC6)) {
				// SOP("taxC6");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtC6));
			}
			if (!taxlabouramtC9.equals("0") && isNumeric(taxlabouramtC9)) {
				// SOP("taxC9");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtC9));
			}
			if (!taxlabouramtC14.equals("0") && isNumeric(taxlabouramtC14)) {
				// SOP("taxc14");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtC14));
			}
			if (!taxlabouramtI5.equals("0") && isNumeric(taxlabouramtI5)) {
				// SOP("taxI5");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtI5));
			}
			if (!taxlabouramtI12.equals("0") && isNumeric(taxlabouramtI12)) {
				// SOP("taxI12");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtI12));
			}
			if (!taxlabouramtI18.equals("0") && isNumeric(taxlabouramtI18)) {
				// SOP("taxI18");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtI18));
			}
			if (!taxlabouramtI28.equals("0") && isNumeric(taxlabouramtI28)) {
				// SOP("taxI28");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtI28));
			}
			if (!taxlabouramtS2_5.equals("0") && isNumeric(taxlabouramtS2_5)) {
				// SOP("taxS2_5");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtS2_5));
			}
			if (!taxlabouramtS6.equals("0") && isNumeric(taxlabouramtS6)) {
				// SOP("taxS6");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtS6));
			}
			if (!taxlabouramtS9.equals("0") && isNumeric(taxlabouramtS9)) {
				// SOP("taxS9");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtS9));
			}
			if (!taxlabouramtS14.equals("0") && isNumeric(taxlabouramtS14)) {
				// SOP("taxS14");
				jctrans_taxamount1.add(Double.parseDouble(taxlabouramtS14));
			}
			for (int i = 0; i < jctrans_taxamount1.size(); i++) {
				jctranstaxamountlabour += jctrans_taxamount1.get(i);
			}
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE jctrans_jc_id =" + jc_id;
			if (billtype.equals("Cash")) {
				StrSql += " AND jctrans_billtype_id =1";
			} else if (billtype.equals("Insurance")) {
				StrSql += " AND jctrans_billtype_id =2";
			}
			// SOP("StrSql=delete=" + StrSql);
			stmttx.addBatch(StrSql);

			if (jctranstaxamountlabour != 0.00) {

				// SOP("StrSql==location==" + StrSql);
				StrSql = "SELECT item_id, item_salestax1_ledger_id,"
						+ " item_salestax2_ledger_id, item_salestax3_ledger_id,"
						+ "	SUBSTR(item_name,15) AS itemname "
						+ " FROM " + compdb(comp_id) + "axela_inventory_item "
						+ " WHERE 1=1";
				if ((isNumeric(taxlabouramtC14) && !taxlabouramtC14.equals("0")) || (isNumeric(taxlabouramtS14) && !taxlabouramtS14.equals("0"))
						|| (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0"))) {
					StrSql += " AND item_name ='Maruti Service 28'";
				}
				if ((isNumeric(taxlabouramtC2_5) && !taxlabouramtC2_5.equals("0")) || (isNumeric(taxlabouramtS2_5) && !taxlabouramtS2_5.equals("0"))
						|| (isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0"))) {
					if (!((isNumeric(taxlabouramtC14) && !taxlabouramtC14.equals("0")) || (isNumeric(taxlabouramtS14) && !taxlabouramtS14.equals("0"))
					|| (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0")))) {
						StrSql += " AND item_name ='Maruti Service 5'";
					} else {
						StrSql += " OR item_name ='Maruti Service 5'";
					}

				}
				if ((isNumeric(taxlabouramtC9) && !taxlabouramtC9.equals("0")) || (isNumeric(taxlabouramtS9) && !taxlabouramtS9.equals("0"))
						|| (isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0"))) {
					if (!((isNumeric(taxlabouramtC14) && !taxlabouramtC14.equals("0")) || (isNumeric(taxlabouramtS14) && !taxlabouramtS14.equals("0"))
							|| (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0")))// for @14%
							&& !((isNumeric(taxlabouramtC2_5) && !taxlabouramtC2_5.equals("0")) || (isNumeric(taxlabouramtS2_5) && !taxlabouramtS2_5.equals("0"))
							|| (isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")))) {// for @2.%
						StrSql += " AND item_name ='Maruti Service 18'";
					} else {
						StrSql += " OR item_name ='Maruti Service 18'";
					}

				}
				if ((isNumeric(taxlabouramtC6) && !taxlabouramtC6.equals("0")) || (isNumeric(taxlabouramtS6) && !taxlabouramtS6.equals("0"))
						|| (isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0"))) {
					if (!((isNumeric(taxlabouramtC14) && !taxlabouramtC14.equals("0")) || (isNumeric(taxlabouramtS14) && !taxlabouramtS14.equals("0"))
							|| (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0"))) && // for @14%
							!((isNumeric(taxlabouramtC2_5) && !taxlabouramtC2_5.equals("0")) || (isNumeric(taxlabouramtS2_5) && !taxlabouramtS2_5.equals("0"))
							|| (isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")))// for @2.5%
							&& !((isNumeric(taxlabouramtC9) && !taxlabouramtC9.equals("0")) || (isNumeric(taxlabouramtS9) && !taxlabouramtS9.equals("0"))
							|| (isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")))) {// for @9%
						StrSql += " AND item_name ='Maruti Service 12'";
					} else {
						StrSql += " OR item_name ='Maruti Service 12'";
					}

				}
				StrSql += "ORDER BY itemname DESC";
				// SOP("StrSql==item==" + StrSql);
				for (int i = 0; i < itemlabourid.length; i++) {// make Every Iteratortion variable as empty
					itemlabourid[i] = "";
					item_salestax1_ledger_id[i] = "";
					item_salestax2_ledger_id[i] = "";
					item_salestax3_ledger_id[i] = "";
					itemlabourname[i] = "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				int k = 0;
				if (crs.next()) {
					itemlabourid[k] = crs.getString("item_id");
					item_salestax1_ledger_id[k] = crs.getString("item_salestax1_ledger_id");
					item_salestax2_ledger_id[k] = crs.getString("item_salestax2_ledger_id");
					item_salestax3_ledger_id[k] = crs.getString("item_salestax3_ledger_id");
					itemlabourname[k] = crs.getString("itemname");
					k++;
				}

				crs.close();
				labourtaxamt = 0.00;
				labdiff = 0.00;
				// Main Item entry in jc_trans table
				Set<Double> labour = new LinkedHashSet<Double>(jctrans_taxamount1);
				for (int i = 0; i < labour.size(); i++) {
					if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
							|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
						labourtaxamt += Math.rint((jctrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i])));
					} else {
						labourtaxamt += Math.rint((jctrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i]) / 2));
					}
				}
				labdiff = labourtaxamt - Math.rint(jctrans_netprice);
				// SOP("labdiff==" + labdiff);
				for (int j = 0; j < itemlabourid.length; j++) {
					for (int i = j; i < labour.size(); i++) {
						if (!itemlabourname[j].equals("")) {
							if (Math.rint((jctrans_taxamount1.get(i) / jctrans_netprice) * 100) == (Double.parseDouble(itemlabourname[j]) / 2)
									|| Math.rint((jctrans_taxamount1.get(i) / jctrans_netprice) * 100) == (Double.parseDouble(itemlabourname[j]))
									|| labourtaxamt == Math.rint(jctrans_netprice) || labdiff <= 1.00) {

								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
									jctrans_price = (jctrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i])) + jctrans_discountamount;
								} else {
									jctrans_price = (jctrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i]) / 2) + jctrans_discountamount;
								}
								tax = (jctrans_taxamount1.get(i) / jctrans_netprice) * 100;
								jctrans_amount = jctrans_price;
								// tax = tax * 2;
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
										+ " " + itemlabourid[j] + ","
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
										+ " " + jctranstaxamountlabour + ","
										+ " " + jctrans_qty + ","
										+ " 1," // jctrans_alt_uom_id
										+ " " + jctrans_price + ","
										+ " 1,"// jctrans_convfactor
										+ " '" + ToLongDate(kknow()) + "',";
								if (billtype.equals("Cash")) {
									StrSql += "1,";// jctrans_billtype_id
								} else if (billtype.equals("Insurance")) {
									StrSql += "2,";// / jctrans_billtype_id
								}
								StrSql += " 1," // jctrans_stock
										+ " 0" // jctrans_dc
										+ ")";
								// SOP("StrSql==JC Main labour Item==" + StrSql);
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
										+ " " + itemlabourid[j] + ","
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
										+ " '" + ToLongDate(kknow()) + "',";
								if (billtype.equals("Cash")) {
									StrSql += "1,";// jctrans_billtype_id
								} else if (billtype.equals("Insurance")) {
									StrSql += "2,";// / jctrans_billtype_id
								}
								StrSql += "1" // jctrans_dc
										+ ")";
								// SOP("StrSql==labour Discount==" + StrSql);
								stmttx.addBatch(StrSql);

								// Tax entry in jc_trans table
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
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
											+ " " + item_salestax3_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax3_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
											+ " " + jctrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + jctrans_taxamount1.get(i) + ",";// jctrans_amount
									if (billtype.equals("Cash")) {
										StrSql += "1";// jctrans_billtype_id
									} else if (billtype.equals("Insurance")) {
										StrSql += "2";// / jctrans_billtype_id
									}
									StrSql += " '" + ToLongDate(kknow()) + "')";
									// SOP("StrSql== labour Tax3==" + StrSql);
									stmttx.addBatch(StrSql);
								} else {
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
											+ " " + item_salestax1_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax1_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
											+ " " + jctrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + jctrans_taxamount1.get(i) + ",";// jctrans_amount
									if (billtype.equals("Cash")) {
										StrSql += "1,";// jctrans_billtype_id
									} else if (billtype.equals("Insurance")) {
										StrSql += "2,";// / jctrans_billtype_id
									}
									StrSql += " '" + ToLongDate(kknow()) + "')";
									// SOP("StrSql==labour Tax1==" + StrSql);
									stmttx.addBatch(StrSql);
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
											+ " " + item_salestax2_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax2_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
											+ " " + jctrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + jctrans_taxamount1.get(i) + ",";// jctrans_amount
									if (billtype.equals("Cash")) {
										StrSql += "1,";// jctrans_billtype_id
									} else if (billtype.equals("Insurance")) {
										StrSql += "2,";// / jctrans_billtype_id
									}
									StrSql += " '" + ToLongDate(kknow()) + "')";
									// SOP("StrSql==labour Tax2==" + StrSql);
									stmttx.addBatch(StrSql);
								}
							} else {
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
									error_msg += "Given Tax is not " + itemlabourname[j] + "% of Gross price<br>";
									break;
								} else {
									error_msg += "Given Tax is not " + (Double.parseDouble(itemlabourname[j]) / 2) + "% of Gross price<br>";
									break;
								}
							}
						}
						break;
					}
				}
			}
			jctrans_price = Double.parseDouble(partsamt);
			jctrans_amount = Double.parseDouble(partsamt);
			jctrans_netprice = Double.parseDouble(grosspartsamt);
			jctrans_discountamount = Double.parseDouble(partsdiscount);
			jctrans_taxamount2.clear();

			if (isNumeric(taxpartamtC2_5) && !taxpartamtC2_5.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtC2_5));
			}
			if (isNumeric(taxpartamtC6) && !taxpartamtC6.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtC6));
			}
			if (isNumeric(taxpartamtC9) && !taxpartamtC9.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtC9));
			}
			if (isNumeric(taxpartamtC14) && !taxpartamtC14.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtC14));
			}
			if (isNumeric(taxpartamtS2_5) && !taxpartamtS2_5.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtS2_5));
			}
			if (isNumeric(taxpartamtS6) && !taxpartamtS6.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtS6));
			}
			if (isNumeric(taxpartamtS9) && !taxpartamtS9.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtS9));
			}
			if (isNumeric(taxpartamtS14) && !taxpartamtS14.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtS14));
			}
			if (isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtI5));
			}
			if (isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtI12));
			}
			if (isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtI18));
			}
			if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0")) {
				jctrans_taxamount2.add(Double.parseDouble(taxpartamtI28));
			}

			for (int i = 0; i < jctrans_taxamount2.size(); i++) {
				jctranstaxamountparts += jctrans_taxamount2.get(i);
			}
			if (jctranstaxamountparts != 0.00) {
				// SOP("StrSql==parts location==" + StrSql);
				StrSql = "SELECT item_id, item_salestax1_ledger_id,"
						+ " item_salestax2_ledger_id, item_salestax3_ledger_id,"
						+ "	SUBSTR(item_name,14) AS itemname "
						+ " FROM " + compdb(comp_id) + "axela_inventory_item "
						+ " WHERE 1=1";
				if ((isNumeric(taxpartamtC14) && !taxpartamtC14.equals("0")) || (isNumeric(taxpartamtS14) && !taxpartamtS14.equals("0"))
						|| (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0"))) {
					StrSql += " AND item_name ='Maruti Parts 28'";
				}
				if ((isNumeric(taxpartamtC2_5) && !taxpartamtC2_5.equals("0")) || (isNumeric(taxpartamtS2_5) && !taxpartamtS2_5.equals("0"))
						|| (isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0"))) {
					if (!((isNumeric(taxpartamtC14) && !taxpartamtC14.equals("0")) || (isNumeric(taxpartamtS14) && !taxpartamtS14.equals("0"))
					|| (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0")))) {
						StrSql += " AND item_name ='Maruti Parts 5'";
					} else {
						StrSql += " OR item_name ='Maruti Parts 5'";
					}
				}
				if ((isNumeric(taxpartamtC9) && !taxpartamtC9.equals("0")) || (isNumeric(taxpartamtS9) && !taxpartamtS9.equals("0"))
						|| (isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0"))) {
					if (!((isNumeric(taxpartamtC14) && !taxpartamtC14.equals("0")) || (isNumeric(taxpartamtS14) && !taxpartamtS14.equals("0"))
							|| (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0"))) &&
							(!(isNumeric(taxpartamtC2_5) && !taxpartamtC2_5.equals("0")) || (isNumeric(taxpartamtS2_5) && !taxpartamtS2_5.equals("0"))
							|| (isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")))) {
						StrSql += " AND item_name ='Maruti Parts 18'";
					} else {
						StrSql += " OR item_name ='Maruti Parts 18'";
					}
				}
				if ((isNumeric(taxpartamtC6) && !taxpartamtC6.equals("0")) || (isNumeric(taxpartamtS6) && !taxpartamtS6.equals("0"))
						|| (isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0"))) {
					if (!((isNumeric(taxpartamtC14) && !taxpartamtC14.equals("0")) || (isNumeric(taxpartamtS14) && !taxpartamtS14.equals("0"))
							|| (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0"))) &&
							(!(isNumeric(taxpartamtC2_5) && !taxpartamtC2_5.equals("0")) || (isNumeric(taxpartamtS2_5) && !taxpartamtS2_5.equals("0"))
							|| (isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")))
							&& (!(isNumeric(taxpartamtC9) && !taxpartamtC9.equals("0")) || (isNumeric(taxpartamtS9) && !taxpartamtS9.equals("0"))
							|| (isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")))) {
						StrSql += " AND item_name ='Maruti Parts 12'";
					} else {
						StrSql += " OR item_name ='Maruti Parts 12'";
					}
				}
				StrSql += "ORDER BY itemname DESC";
				// SOP("StrSql==parts item==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				for (int i = 0; i < itempartsid.length; i++) {// make Every Iteratortion variable as empty
					itempartsid[i] = "";
					item_salestax4_ledger_id[i] = "";
					item_salestax5_ledger_id[i] = "";
					item_salestax6_ledger_id[i] = "";
					itempartsname[i] = "";
				}
				int l = 0;
				while (crs.next()) {
					itempartsid[l] = crs.getString("item_id");
					item_salestax4_ledger_id[l] = crs.getString("item_salestax1_ledger_id");
					item_salestax5_ledger_id[l] = crs.getString("item_salestax2_ledger_id");
					item_salestax6_ledger_id[l] = crs.getString("item_salestax3_ledger_id");
					itempartsname[l] = crs.getString("itemname");
					l++;
				}
				crs.close();
				// tax1 = 0.00;
				parttaxamt = 0.00;
				partdiff = 0.00;
				HashSet<Double> parts = new LinkedHashSet<Double>(jctrans_taxamount2);

				for (int i = 0; i < parts.size(); i++) {

					if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
							|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
						parttaxamt += Math.rint((jctrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[i])));
					} else {
						parttaxamt += Math.rint((jctrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[i]) / 2));
					}
				}
				for (int j = 0; j < itempartsid.length; j++) {
					for (int i = j; i < parts.size(); i++) {
						if (!itempartsname[j].equals("")) {
							if (Math.rint((jctrans_taxamount2.get(i) / jctrans_netprice) * 100) == (Double.parseDouble(itempartsname[j]) / 2)
									|| Math.rint((jctrans_taxamount2.get(i) / jctrans_netprice) * 100) == (Double.parseDouble(itempartsname[j]))
									|| (parttaxamt == Math.rint(jctrans_netprice) || partdiff <= 1.00)) {
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
									jctrans_price = (jctrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[j])) + jctrans_discountamount;
								} else {
									jctrans_price = (jctrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[j]) / 2) + jctrans_discountamount;
								}
								jctrans_amount = jctrans_price;
								// tax1 = tax1 * 2;
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
										+ " " + itempartsid[j] + ","
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
										+ " " + jctranstaxamountparts + ","
										+ " " + jctrans_qty + ","
										+ " 1," // jctrans_alt_uom_id
										+ " " + jctrans_price + ","
										+ " 1,"// jctrans_convfactor
										+ " '" + ToLongDate(kknow()) + "',";
								if (billtype.equals("Cash")) {
									StrSql += "1,";// jctrans_billtype_id
								} else if (billtype.equals("Insurance")) {
									StrSql += "2,";// / jctrans_billtype_id
								}
								StrSql += " 1," // jctrans_dc
										+ " 0" // jctrans_dc
										+ ")";
								// SOP("StrSql==JC Main parts Item==" + StrSql);
								stmttx.addBatch(StrSql);

								// // Discount entry in jc_trans table
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
										+ " " + itempartsid[j] + ","
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
										+ " '" + ToLongDate(kknow()) + "',";
								if (billtype.equals("Cash")) {
									StrSql += "1,";// jctrans_billtype_id
								} else if (billtype.equals("Insurance")) {
									StrSql += "2,";// / jctrans_billtype_id
								}
								StrSql += " 1" // jctrans_dc
										+ ")";
								// SOP("StrSql==parts Discount==" + StrSql);
								stmttx.addBatch(StrSql);

								// Tax entry in jc_trans table
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
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
											+ " " + item_salestax6_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax6_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
											+ " " + jctrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + jctrans_taxamount2.get(i) + ",";// jctrans_amount
									if (billtype.equals("Cash")) {
										StrSql += "1,";// jctrans_billtype_id
									} else if (billtype.equals("Insurance")) {
										StrSql += "2,";// / jctrans_billtype_id
									}
									StrSql += " '" + ToLongDate(kknow()) + "')";
									// SOP("StrSql==parts Tax3==" + StrSql);
									stmttx.addBatch(StrSql);
								} else {
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
											+ " " + item_salestax4_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax4_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
											+ " " + jctrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + jctrans_taxamount2.get(i) + ",";// jctrans_amount
									if (billtype.equals("Cash")) {
										StrSql += "1,";// jctrans_billtype_id
									} else if (billtype.equals("Insurance")) {
										StrSql += "2,";// / jctrans_billtype_id
									}
									StrSql += " '" + ToLongDate(kknow()) + "')";
									// SOP("StrSql==parts Tax1==" + StrSql);
									stmttx.addBatch(StrSql);
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
											+ " " + item_salestax5_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax5_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(jc.jctrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_trans AS jc"
											+ " WHERE jctrans_jc_id = " + jc_id + ")" + "," // jctrans_option_id
											+ " " + jctrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + jctrans_taxamount2.get(i) + ",";// jctrans_amount
									if (billtype.equals("Cash")) {
										StrSql += "1,";// jctrans_billtype_id
									} else if (billtype.equals("Insurance")) {
										StrSql += "2,";// / jctrans_billtype_id
									}
									StrSql += " '" + ToLongDate(kknow()) + "')";
									// SOP("StrSql==parts Tax2==" + StrSql);
									stmttx.addBatch(StrSql);
								}
							} else {
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
									error_msg += "Given Tax is not " + itempartsname[j] + "% of Gross price<br>";
									break;
								} else {
									error_msg += "Given Tax is not " + (Double.parseDouble(itempartsname[j]) / 2) + "% of Gross price<br>";
									break;
								}
							}
						}
						break;
					}
				}
			}

			stmttx.executeBatch();
			conntx.commit();
			propcount++;
			if (error_msg.equals("") && (jctranstaxamountlabour != 0.00 || jctranstaxamountparts != 0.00)) {
				StrSql = "SELECT voucher_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ "	INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = voucher_jc_id"
						+ " WHERE voucher_jc_id = " + jc_id
						+ "	AND voucher_ref_no ='" + bill_no + "'"
						+ " AND voucher_vouchertype_id = 6"
						+ "	AND voucher_active = 1";
				voucher_id = CNumeric(ExecuteQuery(StrSql));
				// SOP("StrSql==voucher=" + StrSql);
				if (!voucher_id.equals("0")) {
					UpdateVoucher(); // To update existing invoice for the Job Card.
				} else if (voucher_id.equals("0")) {
					AddVoucher(); // To Add invoice for the Job Card.
				}
				if (!voucher_id.equals("0")) {
					StrSql = "SELECT voucher_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_jc_id  =" + jc_id
							+ " AND voucher_vouchertype_id = 6"
							+ " AND voucher_active = 1";
					voucher_invoice_id = CNumeric(ExecuteQuery(StrSql));

					StrSql = "SELECT voucher_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_jc_id  =" + jc_id
							+ " AND voucher_vouchertype_id = 9"
							+ "	AND voucher_ref_no ='" + bill_no + "'"
							+ " AND voucher_active = 1";
					voucherinvoiceidcheck = CNumeric(ExecuteQuery(StrSql));

					StrSql = "SELECT voucher_no"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE 1=1"
							+ " AND voucher_id = " + voucher_invoice_id;
					// SOP("AddRecipt StrSql==" + StrSql);
					voucher_no = CNumeric(ExecuteQuery(StrSql));
					StrSql = "SELECT branch_invoice_prefix, branch_invoice_suffix"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE 1=1"
							+ " AND branch_id = " + jc_branch_id;
					// SOP("AddRecipt  prefix StrSql==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						branch_invoice_prefix = crs.getString("branch_invoice_prefix");
						branch_invoice_suffix = crs.getString("branch_invoice_suffix");
					}
					crs.close();
				}
				// SOP("voucherinvoiceidcheck==" + voucherinvoiceidcheck);
				// SOP("voucher_invoice_id==" + voucher_invoice_id);
				if (!voucher_invoice_id.equals("0") && billtype.equals("Cash")) {
					if (voucherinvoiceidcheck.equals("0")) {
						AddReceipt(request, response, voucher_invoice_id); // To Add Receipt for the Job Card.
					} else {
						UpdateReceipt(request, response, voucherinvoiceidcheck);// To update receipt for the jobcard
					}
				}
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
	public void AddVoucher() throws Exception {
		try {
			double vouchertrans_qty = 1.00, vouchertrans_price = 0.00, vouchertrans_amount = 0.00;
			double vouchertrans_netprice = 0.00, vouchertrans_discountamount = 0.00, vouchertranstaxamountlabour = 0.00, vouchertranstaxamountparts = 0.00;
			String vouchertrans_rowcount = "", vouchertrans_option_id = "", voucher_no = "0";
			ArrayList<Double> vouchertrans_taxamount1 = new ArrayList<Double>();

			vouchertrans_price = Double.parseDouble(labouramt);
			vouchertrans_amount = Double.parseDouble(labouramt);
			vouchertrans_netprice = Double.parseDouble(grosslabour);
			vouchertrans_discountamount = Double.parseDouble(labourdiscount);
			vouchertrans_taxamount1 = jctrans_taxamount1;
			voucher_no = getVoucherNo(jc_branch_id, "6", "0", "0");
			for (int i = 0; i < vouchertrans_taxamount1.size(); i++) {
				vouchertranstaxamountlabour += vouchertrans_taxamount1.get(i);
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher"
					+ " ("
					+ " voucher_vouchertype_id,"
					+ " voucher_no,"
					+ " voucher_branch_id,"
					+ " voucher_location_id,"
					+ " voucher_date,"
					+ " voucher_amount,"
					+ " voucher_jc_id,"
					+ " voucher_rateclass_id,"
					+ " voucher_customer_id,"
					+ " voucher_contact_id,"
					+ " voucher_emp_id,"
					+ " voucher_billing_add,"
					+ " voucher_consignee_add,"
					+ " voucher_ref_no,"
					+ " voucher_terms,"
					+ " voucher_active,"
					+ " voucher_entry_id,"
					+ " voucher_entry_date)"
					+ " VALUES" + " ("
					+ " " + 6 + ","
					+ " " + CNumeric(voucher_no) + ","
					+ " " + jc_branch_id + ","
					+ " " + jctrans_location_id + ","
					+ " '" + jc_bill_cash_date + "',"
					+ " " + vouchertrans_netprice + ","
					+ " " + jc_id + ","
					+ " (SELECT branch_rateclass_id FROM " + compdb(comp_id) + "axela_branch " + " WHERE branch_id = " + jc_branch_id + "),"
					+ " " + voucher_customer_id + ","
					+ " " + voucher_contact_id + ","
					+ " " + jc_entry_id + ","
					+ " '" + voucher_billing_add + "',"
					+ " '" + voucher_billing_add + "',"
					+ " '" + bill_no + "',"
					+ " '',"
					+ " '" + 1 + "',"
					+ " " + jc_entry_id + ","
					+ " " + ToLongDate(kknow()) + ")";

			// SOP("StrSQl=voucher ADD=" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				voucher_id = rs.getString(1);
			}
			rs.close();

			if (vouchertranstaxamountlabour != 0.00) {
				Set<Double> labour = new HashSet<Double>(vouchertrans_taxamount1);
				for (int j = 0; j < itemlabourid.length; j++) {
					for (int i = j; i < labour.size(); i++) {
						if (!itemlabourname[j].equals("")) {
							if (Math.rint((vouchertrans_taxamount1.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itemlabourname[j]) / 2)
									|| Math.rint((vouchertrans_taxamount1.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itemlabourname[j]))
									|| labourtaxamt == Math.rint(vouchertrans_netprice) || labdiff <= 1.00) {
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
									vouchertrans_price = (vouchertrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i])) + vouchertrans_discountamount;
								} else {
									vouchertrans_price = (vouchertrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i]) / 2) + vouchertrans_discountamount;
								}
								vouchertrans_amount = vouchertrans_price;
								tax = (vouchertrans_taxamount1.get(i) / vouchertrans_netprice) * 100;
								StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
										+ " ("
										+ " vouchertrans_voucher_id,"
										+ " vouchertrans_customer_id,"
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
										+ " " + itemlabourid[j] + ","
										+ " 0," // jctrans_discount
										+ " 0," // jctrans_tax
										+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) + 1"
										+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
										+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
										+ "	0," // jctrans_option_id
										+ " " + 1 + ","// vouchertrans_qty
										+ " " + vouchertrans_price + ","
										+ " " + vouchertrans_amount + ","
										+ " " + vouchertrans_netprice + ","
										+ " " + vouchertrans_discountamount + ","
										+ " " + vouchertranstaxamountlabour + ","
										+ " " + vouchertrans_qty + ","
										+ " 1," // jctrans_alt_uom_id
										+ " " + vouchertrans_price + ","
										+ " 1," // jctrans_convfactor
										+ " '" + jc_bill_cash_date + "',"
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
										+ " " + itemlabourid[j] + ","
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
										+ " '" + jc_bill_cash_date + "',"
										+ " 1" // jctrans_dc
										+ ")";
								// SOP("StrSql==Voucher Discount==" + StrSql);
								stmttx.addBatch(StrSql);

								// Tax entry in voucher_trans table
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
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
											+ " " + item_salestax3_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax3_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql==voucherTax=3=" + StrSql);
									stmttx.addBatch(StrSql);
								} else {
									// Tax entry in voucher_trans table
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
											+ " " + item_salestax1_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax1_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql==voucherTax=1=" + StrSql);
									stmttx.addBatch(StrSql);
									// Tax entry in voucher_trans table
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
											+ " " + item_salestax2_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax2_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql==voucherTax=2=" + StrSql);
									stmttx.addBatch(StrSql);
								}
							} else {
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
									error_msg += "Given Tax is not " + itemlabourname[j] + "% of Gross price<br>";
									break;
								} else {
									error_msg += "Given Tax is not " + (Double.parseDouble(itemlabourname[j]) / 2) + "% of Gross price<br>";
									break;
								}
							}
						}
						break;
					}
				}
			}
			vouchertrans_price = Double.parseDouble(partsamt);
			vouchertrans_amount = Double.parseDouble(partsamt);
			vouchertrans_netprice = Double.parseDouble(grosspartsamt);
			vouchertrans_discountamount = Double.parseDouble(partsdiscount);
			ArrayList<Double> vouchertrans_taxamount2 = new ArrayList<Double>();
			vouchertrans_taxamount2 = jctrans_taxamount2;
			for (int i = 0; i < vouchertrans_taxamount2.size(); i++) {
				vouchertranstaxamountparts += vouchertrans_taxamount2.get(i);
			}
			if (vouchertranstaxamountparts != 0.00) {
				Set<Double> parts = new HashSet<Double>(vouchertrans_taxamount2);
				for (int j = 0; j < itempartsid.length; j++) {
					for (int i = j; i < parts.size(); i++) {
						if (!itempartsname[j].equals("")) {
							if (Math.rint((vouchertrans_taxamount2.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itempartsname[j]) / 2)
									|| Math.rint((vouchertrans_taxamount2.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itempartsname[j]))
									|| parttaxamt == Math.rint(vouchertrans_netprice) || partdiff <= 1.00) {
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
									vouchertrans_price = (vouchertrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[j])) + vouchertrans_discountamount;
								} else {
									vouchertrans_price = (vouchertrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[j]) / 2) + vouchertrans_discountamount;
								}
								vouchertrans_amount = vouchertrans_price;
								// tax1 = tax1 * 2;
								StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
										+ " ("
										+ " vouchertrans_voucher_id,"
										+ " vouchertrans_customer_id,"
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
										+ " " + itempartsid[j] + ","
										+ " 0," // jctrans_discount
										+ " 0," // jctrans_tax
										+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) + 1"
										+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
										+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
										+ "	0," // jctrans_option_id
										+ " " + 1 + ","
										+ " " + vouchertrans_price + ","
										+ " " + vouchertrans_amount + ","
										+ " " + vouchertrans_netprice + ","
										+ " " + vouchertrans_discountamount + ","
										+ " " + vouchertranstaxamountlabour + ","
										+ " " + vouchertrans_qty + ","
										+ " 1," // jctrans_alt_uom_id
										+ " " + vouchertrans_price + ","
										+ " 1," // jctrans_convfactor
										+ " '" + jc_bill_cash_date + "',"
										+ " 0" // jctrans_dc
										+ ")";
								// SOP("StrSql==Voucher Main parts Item==" + StrSql);
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
										+ " " + itempartsid[j] + ","
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
										+ " '" + jc_bill_cash_date + "',"
										+ " 1" // jctrans_dc
										+ ")";
								// SOP("StrSql==Voucher parts Discount==" + StrSql);
								stmttx.addBatch(StrSql);

								// Tax entry in voucher_trans table
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
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
											+ " " + item_salestax6_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax6_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql==voucher parts Tax3==" + StrSql);
									stmttx.addBatch(StrSql);
								} else {
									// Tax entry in voucher_trans table
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
											+ " " + item_salestax4_ledger_id[j] + ","// jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax4_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql==voucher parts Tax=1=" + StrSql);
									stmttx.addBatch(StrSql);
									// Tax entry in voucher_trans table
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
											+ " " + item_salestax5_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax5_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql==voucher parts Tax=2=" + StrSql);
									stmttx.addBatch(StrSql);
								}
							} else {
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
									error_msg += "Given Tax is not " + itempartsname[j] + "% of Gross price<br>";
									break;
								} else {
									error_msg += "Given Tax is not " + (Double.parseDouble(itempartsname[j]) / 2) + "% of Gross price<br>";
									break;
								}
							}
						}
						break;
					}

				}
			}
			stmttx.executeBatch();
			conntx.commit();
			// Update voucher amount in voucher table
			StrSql = "SELECT "
					+ "@totalamt := SUM(CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_amount END) AS amount, "
					+ "@totaldis := SUM(CASE WHEN vouchertrans_discount = 1 THEN vouchertrans_amount END) AS discount, "
					+ "@totaltax := SUM(CASE WHEN vouchertrans_tax = 1 THEN vouchertrans_amount END) AS tax, "
					+ "@total := (@totalamt - @totaldis) + @totaltax AS netamount "// problem in variable
					+ "FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id;
			// SOP("update voucher==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				amount = (crs.getDouble("amount") - crs.getDouble("discount")) + crs.getDouble("tax");// for temp
				price = crs.getDouble("amount");
				totaltax = crs.getDouble("tax");
			}
			crs.close();
			StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher "
					+ " SET voucher_amount = " + amount + ", "
					+ " voucher_modified_id = " + jc_entry_id + ","
					+ " voucher_modified_date = '" + jc_entry_date + "'"
					+ " WHERE voucher_id = " + voucher_id;
			// SOP("update voucher amount==" + StrSql);
			updateQuery(StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans ("
					+ " vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_id + ","
					+ " " + voucher_customer_id + ","
					+ " " + jctrans_location_id + ","
					+ " " + amount + ","
					+ " '" + jc_bill_cash_date + "',"
					+ " '1'" + " )";
			updateQuery(StrSql);
			conntx.commit();
			// SOP("StrSql==sup==" + StrSqlBreaker(StrSql));

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

			double vouchertrans_qty = 1.00, vouchertrans_price = 0.00, vouchertrans_amount = 0.00;
			double vouchertrans_netprice = 0.00, vouchertrans_discountamount = 0.00, vouchertranstaxamountlabour = 0.00, vouchertranstaxamountparts = 0.00;
			String vouchertrans_rowcount = "", vouchertrans_option_id = "";

			vouchertrans_price = Double.parseDouble(labouramt);
			vouchertrans_amount = Double.parseDouble(labouramt);
			vouchertrans_netprice = Double.parseDouble(grosslabour);
			vouchertrans_discountamount = Double.parseDouble(labourdiscount);
			ArrayList<Double> vouchertrans_taxamount1 = new ArrayList<Double>();
			vouchertrans_taxamount1 = jctrans_taxamount1;
			// SOP("vouchertrans_taxamount1.size()==" + vouchertrans_taxamount1.size());
			for (int i = 0; i < vouchertrans_taxamount1.size(); i++) {
				vouchertranstaxamountlabour += vouchertrans_taxamount1.get(i);
			}
			// SOP("voucher_id=0=" + voucher_id);
			// Main Item entry in jc_trans table
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id =" + voucher_id;
			stmttx.addBatch(StrSql);
			// SOP("StrSql==Voucher update delete==" + StrSql);
			if (vouchertranstaxamountlabour != 0.00) {
				// Main Item entry in jc_trans table
				Set<Double> labour = new HashSet<Double>(vouchertrans_taxamount1);
				for (int j = 0; j < itemlabourid.length; j++) {
					for (int i = j; i < labour.size(); i++) {
						if (!itemlabourname[j].equals("")) {
							if (Math.rint((vouchertrans_taxamount1.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itemlabourname[j]) / 2)
									|| Math.rint((vouchertrans_taxamount1.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itemlabourname[j]))
									|| labourtaxamt == Math.rint(vouchertrans_netprice) || labdiff <= 1.00) {
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
									vouchertrans_price = (vouchertrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i])) + vouchertrans_discountamount;
								} else {
									vouchertrans_price = (vouchertrans_taxamount1.get(i) * 100) / (Double.parseDouble(itemlabourname[i]) / 2) + vouchertrans_discountamount;
								}
								vouchertrans_amount = vouchertrans_price;
								tax = (vouchertrans_taxamount1.get(i) / vouchertrans_netprice) * 100;
								StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
										+ " ("
										+ " vouchertrans_voucher_id,"
										+ " vouchertrans_customer_id,"
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
										+ " " + itemlabourid[j] + ","
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
										+ " " + vouchertranstaxamountlabour + ","
										+ " " + vouchertrans_qty + ","
										+ " 1," // jctrans_alt_uom_id
										+ " " + vouchertrans_price + ","
										+ " 1," // jctrans_convfactor
										+ " '" + jc_bill_cash_date + "',"
										+ " 0" // jctrans_dc
										+ ")";
								// SOP("StrSql==Voucher update Main Item==" + StrSql);
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
										+ " " + itemlabourid[j] + ","
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
										+ " '" + jc_bill_cash_date + "',"
										+ " 1" // jctrans_dc
										+ ")";
								// SOP("StrSql==voucher updateDiscount==" + StrSql);
								stmttx.addBatch(StrSql);

								// Tax entry in voucher_trans table
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
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
											+ " " + item_salestax3_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax3_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql== voucher updateTax=3=" + StrSql);
									stmttx.addBatch(StrSql);
								} else {
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
											+ " " + item_salestax1_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax1_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql== voucher updateTax=1=" + StrSql);
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
											+ " " + item_salestax2_ledger_id[j] + ","// jctrans_customer_id
											+ " " + itemlabourid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax2_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount1.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql== voucher updateTax=2=" + StrSql);
									stmttx.addBatch(StrSql);
								}
							} else {
								if (isNumeric(taxlabouramtI28) && !taxlabouramtI28.equals("0") || isNumeric(taxlabouramtI18) && !taxlabouramtI18.equals("0")
										|| isNumeric(taxlabouramtI12) && !taxlabouramtI12.equals("0") || isNumeric(taxlabouramtI5) && !taxlabouramtI5.equals("0")) {
									error_msg += "Given Tax is not " + itemlabourname[j] + "% of Gross price<br>";
									break;
								} else {
									error_msg += "Given Tax is not " + (Double.parseDouble(itemlabourname[j]) / 2) + "% of Gross price<br>";
									break;
								}
							}
						}
						break;
					}

				}
			}
			vouchertrans_price = Double.parseDouble(partsamt);
			vouchertrans_amount = Double.parseDouble(partsamt);
			vouchertrans_netprice = Double.parseDouble(grosspartsamt);
			vouchertrans_discountamount = Double.parseDouble(partsdiscount);
			ArrayList<Double> vouchertrans_taxamount2 = new ArrayList<Double>();
			vouchertrans_taxamount2 = jctrans_taxamount2;
			for (int i = 0; i < vouchertrans_taxamount2.size(); i++) {
				vouchertranstaxamountparts += vouchertrans_taxamount2.get(i);
			}
			if (vouchertranstaxamountparts != 0.00) {
				Set<Double> parts = new HashSet<Double>(vouchertrans_taxamount2);
				for (int j = 0; j < itempartsid.length; j++) {
					for (int i = j; i < parts.size(); i++) {
						if (!itempartsname[j].equals("")) {
							if (Math.rint((vouchertrans_taxamount2.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itempartsname[j]) / 2)
									|| Math.rint((vouchertrans_taxamount2.get(i) / vouchertrans_netprice) * 100) == (Double.parseDouble(itempartsname[j]))
									|| parttaxamt == Math.rint(vouchertrans_netprice) || partdiff <= 1.00) {

								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
									vouchertrans_price = (vouchertrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[j])) + vouchertrans_discountamount;
								} else {
									vouchertrans_price = (vouchertrans_taxamount2.get(i) * 100) / (Double.parseDouble(itempartsname[j]) / 2) + vouchertrans_discountamount;
								}
								vouchertrans_amount = vouchertrans_price;
								tax1 = (vouchertrans_taxamount2.get(i) / vouchertrans_netprice) * 100;
								// tax1 = tax1 * 2;
								StrSql = " INSERT INTO  " + compdb(comp_id) + "axela_acc_voucher_trans"
										+ " ("
										+ " vouchertrans_voucher_id,"
										+ " vouchertrans_customer_id,"
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
										+ " " + itempartsid[j] + ","
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
										+ " " + vouchertranstaxamountparts + ","
										+ " " + vouchertrans_qty + ","
										+ " 1," // jctrans_alt_uom_id
										+ " " + vouchertrans_price + ","
										+ " 1," // jctrans_convfactor
										+ " '" + jc_bill_cash_date + "',"
										+ " 0" // jctrans_dc
										+ ")";
								// SOP("StrSql==Voucher updateparts  Main Item==" + StrSql);
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
										+ " " + itempartsid[j] + ","
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
										+ " '" + jc_bill_cash_date + "',"
										+ " 1" // jctrans_dc
										+ ")";
								// SOP("StrSql==voucher update partsDiscount==" + StrSql);
								stmttx.addBatch(StrSql);

								// Tax entry in voucher_trans table
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
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
											+ " " + item_salestax6_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax6_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql== voucher update partsTax=3=" + StrSql);
									stmttx.addBatch(StrSql);
								} else {
									// Tax entry in voucher_trans table
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
											+ " " + item_salestax4_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax4_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql== voucher update partsTax=1=" + StrSql);
									stmttx.addBatch(StrSql);
									// Tax entry in voucher_trans table
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
											+ " " + item_salestax5_ledger_id[j] + "," // jctrans_customer_id
											+ " " + itempartsid[j] + ","
											+ " 1," // jctrans_tax
											+ " " + item_salestax5_ledger_id[j] + "," // jctrans_tax_id
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0)"
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + ","
											+ " " + "(SELECT COALESCE(MAX(voucher.vouchertrans_rowcount), 0) "
											+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans AS voucher"
											+ " WHERE vouchertrans_voucher_id = " + voucher_id + ")" + "," // jctrans_option_id
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_price
											+ " " + vouchertrans_taxamount2.get(i) + "," // jctrans_amount
											+ " '" + jc_bill_cash_date + "')";
									// SOP("StrSql== voucher update partsTax=2=" + StrSql);
									stmttx.addBatch(StrSql);

								}
							} else {
								if (isNumeric(taxpartamtI28) && !taxpartamtI28.equals("0") || isNumeric(taxpartamtI18) && !taxpartamtI18.equals("0")
										|| isNumeric(taxpartamtI12) && !taxpartamtI12.equals("0") || isNumeric(taxpartamtI5) && !taxpartamtI5.equals("0")) {
									error_msg += "Given Tax is not " + itempartsname[j] + "% of Gross price<br>";
									break;
								} else {
									error_msg += "Given Tax is not " + (Double.parseDouble(itempartsname[j]) / 2) + "% of Gross price<br>";
									break;
								}
							}
						}
						break;
					}

				}
			}
			stmttx.executeBatch();
			conntx.commit();
			// Update voucher amount in voucher table
			StrSql = "SELECT "
					+ "@totalamt := SUM(CASE WHEN vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0 THEN vouchertrans_amount END) AS amount, "
					+ "@totaldis := SUM(CASE WHEN vouchertrans_discount = 1 THEN vouchertrans_amount END) AS discount, "
					+ "@totaltax := SUM(CASE WHEN vouchertrans_tax = 1 THEN vouchertrans_amount END) AS tax, "
					+ "@total := (@totalamt - @totaldis) + @totaltax AS netamount "// problem in variable
					+ "FROM " + compdb(comp_id) + "axela_acc_voucher_trans "
					+ "WHERE vouchertrans_voucher_id = " + voucher_id;
			// SOP("update voucher==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				amount = (crs.getDouble("amount") - crs.getDouble("discount")) + crs.getDouble("tax");// for temp
				price = crs.getDouble("amount");
				totaltax = crs.getDouble("tax");
			}
			crs.close();
			StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher "
					+ " SET voucher_amount = " + amount + ", "
					+ " voucher_modified_id = " + jc_entry_id + ","
					+ " voucher_modified_date = '" + jc_entry_date + "'"
					+ " WHERE voucher_id = " + voucher_id;
			// SOP("update voucher amount==" + StrSql);
			updateQuery(StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans ("
					+ " vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_id + ","
					+ " " + voucher_customer_id + ","
					+ " " + jctrans_location_id + ","
					+ " " + amount + ","
					+ " '" + jc_bill_cash_date + "',"
					+ " '1'" + " )";
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
	public void AddReceipt(HttpServletRequest request, HttpServletResponse response, String voucher_invoice_id) throws Exception {
		receipt.comp_id = comp_id;
		receipt.addB = "yes";
		receipt.voucherdate = strToShortDate(jc_bill_cash_date);
		receipt.voucher_branch_id = jc_branch_id;
		receipt.voucher_jc_id = jc_id;
		receipt.voucher_ref_no = bill_no;
		receipt.vouchertype_id = "9";
		receipt.voucher_customer_id = voucher_customer_id;
		receipt.voucher_contact_id = voucher_contact_id;
		receipt.payment_id = "1";
		receipt.vouchertrans_customer_id = "3";
		receipt.vouchertrans_paymode_id = "1";
		receipt.voucher_amount = String.valueOf(amount);
		receipt.voucher_emp_id = jc_entry_id;
		receipt.voucher_invoice_id = voucher_invoice_id;
		receipt.voucher_narration = "Towards Invoice No." + (branch_invoice_prefix + voucher_no + branch_invoice_suffix);
		receipt.voucher_active = "1";
		receipt.emp_id = jc_entry_id;
		receipt.voucher_entry_date = jc_entry_date;
		receipt.AddFields(request);

	}
	public void UpdateReceipt(HttpServletRequest request, HttpServletResponse response, String voucherreceiptid) throws Exception {
		receipt.comp_id = comp_id;
		receipt.updateB = "yes";
		receipt.voucherdate = strToShortDate(jc_bill_cash_date);
		receipt.voucher_branch_id = jc_branch_id;
		receipt.voucher_jc_id = jc_id;
		receipt.voucher_ref_no = bill_no;
		receipt.vouchertype_id = "9";
		receipt.voucher_customer_id = voucher_customer_id;
		receipt.voucher_contact_id = voucher_contact_id;
		receipt.payment_id = "1";
		receipt.vouchertrans_customer_id = "3";
		receipt.vouchertrans_paymode_id = "1";
		receipt.voucher_amount = String.valueOf(amount);
		receipt.voucher_emp_id = jc_entry_id;
		receipt.voucher_invoice_id = voucher_invoice_id;
		receipt.voucher_narration = "Towards Invoice No." + (branch_invoice_prefix + voucher_no + branch_invoice_suffix);
		receipt.voucher_active = "1";
		receipt.emp_id = jc_entry_id;
		receipt.voucher_entry_date = jc_entry_date;
		receipt.voucher_id = voucherreceiptid;
		receipt.UpdateFields(request, response);
	}
	public String getVoucherNo(String branch_id, String vouchertype_id, String voucher_no, String voucher_id) {
		StrSql = "SELECT COALESCE(MAX(voucher_no),0)+1"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE 1=1"
				+ " AND voucher_branch_id = " + branch_id
				+ " AND voucher_vouchertype_id = " + vouchertype_id;

		if (!voucher_no.equals("0")) {
			StrSql += " AND voucher_no = " + voucher_no;
		}
		if (!voucher_id.equals("0")) {
			StrSql += " AND voucher_id != " + voucher_id;
		}
		// SOP("getVoucherNo==StrSql===" + StrSql);
		StrSql = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
		return StrSql.toString();
	}
}
