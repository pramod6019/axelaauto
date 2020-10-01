package axela.sales;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
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

import cloudify.connect.Connect;

public class Enquiry_User_Import_Maruti_Temp extends Connect {

	public String StrSql = "", StrHTML = "", enquiry_error_msg = "";
	public String msg = "", emp_id = "0";
	public String emp_role_id = "0", comp_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0;
	public String enquiry_entry_id = "";
	public String enquiry_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", branch_brand_id = "0", enquiry_branch_id = "0";
	public String branch_name = "";
	public String upload = "";
	public String similar_comm_no = "";
	public String enquiry_id = "0", contact_id = "0";
	public int count = 0;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				enquiry_entry_id = emp_id;
				enquiry_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button1"));
				branch_brand_id = ExecuteQuery("SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_active = '1'"
						+ " AND branch_brand_id= '2'");

				// SOP("branch_brand_id====" + branch_brand_id);

				// contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
				if (branch_id.equals("0")) {
					if (emp_id.equals("1")) {
						enquiry_branch_id = ExecuteQuery("SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = '1'"
								+ " LIMIT 1");
					} else if (!emp_id.equals("1")) {
						enquiry_branch_id = ExecuteQuery("SELECT emp_branch_id FROM " + compdb(comp_id) + "axela_emp_branch"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
								+ " WHERE branch_active = '1'"
								+ " AND emp_id = " + emp_id + ""
								+ " LIMIT 1");
						// SOP("branch_id = " + branch_id);
					}
				} else {
					enquiry_branch_id = branch_id;
				}
				Addfile(request, response);
			}
		} catch (Exception ex) {
			SOP("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			savePath = EnquiryImportPath(comp_id);
			docsize = 10;
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
					enquiry_branch_id = str1[0];
					// SOP("branch===enq===" + enquiry_branch_id);
					if (str1[i].equals("Upload")) {
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							CheckForm();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							}
							// SOP("fileName---------" + fileName);
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
										File uploadedFile = new File(EnquiryImportPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										// msg += "<br>Document contents not in order!";
										String fileName1 = EnquiryImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = propcount + " Enquiries imported successfully!" + "" + msg;
										if (!enquiry_error_msg.equals("")) {
											msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + enquiry_error_msg;
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOP("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("enquiry-user-import-maruti.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (enquiry_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {

			String enquiry_dmsno = "", enquiry_date = "", enquiry_emp_id = "0", enquiry_emp_ref_no = "0", enquiry_emp_name = "", enquiry_sob_id = "0", enquiry_team_name = "", enquiry_team_id = "0", enquiry_custtype_id = "0";
			String customer_name = "", contact_name = "", contact_title_id = "0", contact_title, contact_fname = "", contact_lname = "", contact_fname_lname;
			String contact_mobile1 = "", contact_dob = "", contact_anniversary = "";
			String contact_phone1_code = "", contact_phone1 = "", contact_phone2_code = "", contact_phone2 = "", contact_email1 = "", contact_address = "", contact_city_id = "0", contact_pin = "";
			String enquiry_customer_id = "0", enquiry_contact_id = "0";
			String enquiry_model_id = "", enquiry_item_id = "0", enquiry_fueltype_id = "0", enquiry_loanfinancer = "", enquiry_status_id = "0", enquiry_status_date = "";
			String enquiry_soe_id = "0", enquiry_soe_name = "", enquiry_buyertype_id = "0", enquiry_desc = "", model_name = "";
			String enquiry_notes = "", enquiry_priorityenquiry_id = "0", error_msg = "";
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

			// if (rowLength > 500) {
			// rowLength = 500;
			// }
			int h = 0;
			int j = 0;
			int count = 0;

			for (j = 1; j < rowLength + 1; j++) {
				error_msg = "";
				for (h = 0; h < columnLength; h++) {

					if (enquiry_branch_id.equals("0")) {
						enquiry_branch_id = "1";
					}

					contact_city_id = CNumeric(ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id like '" + enquiry_branch_id + "'"
							+ " LIMIT 1"));

					if (h == 0) {

					}
					if (h == 1) {
						enquiry_dmsno = PadQuotes(sheetData[j][h]);
						if (enquiry_dmsno.equals("null")) {
							enquiry_dmsno = "";
						}
					}

					if (h == 2) {
						enquiry_date = PadQuotes(sheetData[j][h]);
						if (!enquiry_date.equals("null")) {
							// Date dttemp = new SimpleDateFormat("dd-mmm-yyyy").parse(enquiry_date);
							// enquiry_date = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
							// SOP("enquiry_date==" + enquiry_date);
							// String year = enquiry_date.substring(6, 10);
							// enquiry_date = enquiry_date.substring(0, 6) + year;
							// SOP("enquiry_date===" + enquiry_date);
							// enquiry_date = ConvertShortDateToStr(enquiry_date);
							// SOP("enquiry_date==123===" + enquiry_date);
							// boolean t2 = isValidDateFormatLong(enquiry_date);
							// SOP("enquiry_date==t2===" + t2);
							// if (t2 == true) {
							// } else if (t2 == false) {
							// enquiry_date = "";
							// }
							// } else {
							// enquiry_date = "";
							// }
							SOP("enquiry_date===456===" + enquiry_date);
						}
					}

					if (h == 3) {
						enquiry_team_name = sheetData[j][h].toString();
						enquiry_team_name = "";
						enquiry_team_id = "9999";
						// = CNumeric(ExecuteQuery("SELECT team_id FROM " + compdb(comp_id) + "axela_sales_team"
						// + " WHERE team_name = " + enquiry_team_name + ""
						// + " LIMIT 1"));
						// SOP("enquiry_team_id==============" + enquiry_team_id);
					}
					if (h == 4) {
						enquiry_emp_ref_no = sheetData[j][h];
						enquiry_emp_ref_no = "3011";
					}
					if (h == 5) {
						enquiry_emp_name = sheetData[j][h].toString();
						enquiry_emp_name = "Axela Admin";
						enquiry_emp_id = CNumeric(ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_name = '" + enquiry_emp_name + "'"
								+ " LIMIT 1"));

					}

					if (h == 6) {
						StrSql = "SELECT custtype_id FROM " + compdb(comp_id) + "axela_sales_enquiry_add_custtype"
								+ " WHERE custtype_name = '" + sheetData[j][h].toString() + "'"
								+ " LIMIT 1";
						// SOP("StrSql city= " + StrSql);
						enquiry_custtype_id = CNumeric(ExecuteQuery(StrSql));
						if (enquiry_custtype_id.equals("")) {
							enquiry_custtype_id = "0";
						}
					}

					if (h == 7) {// //company
						customer_name = PadQuotes(sheetData[j][h]);
						if (customer_name.equals("null") || customer_name.equals("NOT AVAILABLE")) {
							customer_name = "";
						}
					}

					if (h == 8) {// ///subcompany
						// enquiry_name = sheetData[j][h];
						// if (enquiry_name.equals("null")) {
						// enquiry_name = "";
						// }
					}
					if (h == 9) {// ///contacttitle
						contact_title_id = CNumeric(ExecuteQuery("SELECT title_id FROM " + compdb(comp_id) + "axela_title"
								+ " WHERE title_desc = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (contact_title_id.equals("0")) {
							contact_title_id = "1";
						}
					}

					if (h == 10) {// ///contactname
						contact_name = PadQuotes(sheetData[j][h]);
						contact_fname = "";
						contact_lname = "";
						if (!contact_name.equals("")) {
							if (contact_name.contains(" ")) {
								contact_fname = contact_name.split(" ")[0];
								if (contact_name.split(" ").length > 1) {
									contact_lname = contact_name.split(" ")[1];
								}

								if (contact_fname.equals("")) {
									contact_fname = contact_lname;
									contact_lname = "";
								}
							} else {
								contact_fname = contact_name;
							}
							contact_name = contact_fname + " " + contact_lname;
							if (customer_name.equals("")
									|| customer_name.equalsIgnoreCase("OTHERS")
									|| customer_name.equalsIgnoreCase("NOT AVAILABLE")) {
								customer_name = contact_name;
							}
						}
					}

					if (h == 11) {// /////address
						contact_address = PadQuotes(sheetData[j][h]);
						if (contact_address.equals("null") || contact_address.equals("N/A")) {
							contact_address = "";
						}
					}
					if (h == 12) {
						contact_pin = PadQuotes(sheetData[j][h]);
						if (contact_pin.equals("null") || contact_pin.equals("N/A")) {
							contact_pin = "";
						}
						if (contact_pin.equals("")) {
							contact_pin = ExecuteQuery("SELECT branch_pin FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = " + enquiry_branch_id + "");
						}
					}
					if (h == 13) {// ///pin desc

					}

					if (h == 14) {
						contact_phone1_code = PadQuotes(sheetData[j][h]);
						if (contact_phone1_code.equals("null")) {
							contact_phone1_code = "";
						}
					}

					if (h == 15) {
						contact_phone1 = PadQuotes(sheetData[j][h]);
						// if (contact_phone1.equals("null")) {
						// contact_phone1 = "";
						// }
						if (!contact_phone1_code.equals("") && !contact_phone1.equals("")) {
							contact_phone1 = contact_phone1_code + "-" + contact_phone1;
						}
						if (!contact_phone1.equals("") && !contact_phone1.contains("91-")) {
							contact_phone1 = "91-" + contact_phone1;
						}
						// SOP("contact_phone1===" + contact_phone1);
					}

					if (h == 16) {
						contact_phone2_code = PadQuotes(sheetData[j][h]);
						if (contact_phone2_code.equals("null")) {
							contact_phone2_code = "";
						}
					}

					if (h == 17) {
						contact_phone2 = PadQuotes(sheetData[j][h]);

						if (!contact_phone2_code.equals("") && !contact_phone2.equals("")) {
							contact_phone2 = contact_phone2_code + "-" + contact_phone2;
						}
						if (!contact_phone2.equals("") && !contact_phone2.contains("91-")) {
							contact_phone2 = "91-" + contact_phone2;
						}
						// SOP("contact_phone2===123==" + contact_phone2);
					}

					if (h == 18) {
						contact_mobile1 = PadQuotes(sheetData[j][h]);
						// SOP("contact_mobile1==123=" + contact_mobile1);
						if (contact_mobile1.equals("null")) {
							contact_mobile1 = "";
						}
						else if (!contact_mobile1.contains("91-")) {
							contact_mobile1 = "91-" + contact_mobile1;

						}
						// SOP("contact_mobile1===after adding 91===" + contact_mobile1);
					}

					if (h == 19) {// //stdcode fax

					}

					if (h == 20) {// //fax

					}
					// SOP("hbat = " + sheetData[j][h]);
					if (h == 21) {
						contact_email1 = PadQuotes(sheetData[j][h]);
						if (contact_email1.equals("null")) {
							contact_email1 = "";
						}
					}
					if (h == 22) {// ///model code

					}

					if (h == 23) {// //model name
						model_name = PadQuotes(sheetData[j][h]);
						// SOP("model_name=====" + model_name);
						if (model_name.equals("DATSUN")) {
							model_name = "Datsun GO +";
						}
						enquiry_model_id = CNumeric(ExecuteQuery("SELECT model_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_name = '" + model_name + "'"
								+ " LIMIT 1"));
						// SOP("enquiry_model_id---------I-----" + enquiry_model_id);
						// SOP("enquiry_model_id ===== " + "SELECT model_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
						// + " WHERE model_name = '" + sheetData[j][h] + "'"
						// + " LIMIT 1");
						if (enquiry_model_id.equals("0")) {
							error_msg += "<br> This Model Name: " + model_name + " is not found!";
						}
					}
					if (h == 24) {// ////fuel
						enquiry_fueltype_id = CNumeric(ExecuteQuery("SELECT fueltype_id FROM " + compdb(comp_id) + "axela_fueltype"
								+ " WHERE fueltype_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (enquiry_fueltype_id.equals("0")) {
							enquiry_fueltype_id = "1";
						}
					}

					if (h == 25) {// ///item code

					}

					if (h == 26) {
						StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_name = '" + sheetData[j][h] + "'"
								+ " AND item_fueltype_id = " + enquiry_fueltype_id + ""
								+ " AND item_model_id = " + enquiry_model_id + ""
								+ " LIMIT 1";
						enquiry_item_id = CNumeric(ExecuteQuery(StrSql));
						if (enquiry_item_id.equals("0")) {
							enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE 1=1"
									// + " AND item_fueltype_id = " + enquiry_fueltype_id + ""
									+ " AND item_model_id = " + enquiry_model_id + ""
									+ " LIMIT 1"));
						}
					}

					if (h == 27) {// ////financer name
						enquiry_loanfinancer = sheetData[j][h];
						if (enquiry_loanfinancer.equals("null") || enquiry_loanfinancer.equals("N/A")) {
							enquiry_loanfinancer = "";
						}
					}

					if (h == 28) {// ///dsa name

					}

					if (h == 29) {// ///status

					}

					if (h == 30) {// /status date
						enquiry_status_date = sheetData[j][h];

					}

					if (h == 31) {// /soe
						enquiry_soe_name = sheetData[j][h];
						SOP("enquiry_soe_name===before " + enquiry_soe_name);
						if (enquiry_soe_name.equals("Inbound Calls") || enquiry_soe_name.equals("Business Associates") || enquiry_soe_name.equals("Advertisement")
								|| enquiry_soe_name.equals("COURT") || enquiry_soe_name.equals("N/A")
								|| enquiry_soe_name.equals("Others") || enquiry_soe_name.equals("Telecalling") || enquiry_soe_name.equals("Yes")
								|| enquiry_soe_name.equals("Workshop Enquiry") || enquiry_soe_name.equals("")) {
							enquiry_soe_name = "Inbound";
						} else if (enquiry_soe_name.equals("Anytime Maruti") || enquiry_soe_name.equals("aCRM")
								|| enquiry_soe_name.equals("Autocard Customer") || enquiry_soe_name.equals("MSIL Employee")) {
							enquiry_soe_name = "ATM DMS Generated";
						} else if (enquiry_soe_name.equals("Bank") || enquiry_soe_name.equals("Campaign/Events") || enquiry_soe_name.equals("MCD")
								|| enquiry_soe_name.equals("RWA") || enquiry_soe_name.equals("RWA Event") || enquiry_soe_name.equals("Wagnor Event")) {
							enquiry_soe_name = "Event";
						} else if (enquiry_soe_name.equals("Cold") || enquiry_soe_name.equals("Cold Visits") || enquiry_soe_name.equals("DTC Colony")
								|| enquiry_soe_name.equals("References") || enquiry_soe_name.equals("taxi") || enquiry_soe_name.equals("True Value")) {
							enquiry_soe_name = "References";
						} else if (enquiry_soe_name.equals("Email") || enquiry_soe_name.equals("Email Exchange Offer")) {
							enquiry_soe_name = "Email";
						} else if (enquiry_soe_name.equals("Showroom Walk-In")) {
							enquiry_soe_name = "Walk in";
						} else if (enquiry_soe_name.equals("Web Enquiry")) {
							enquiry_soe_name = "Digital";
						}
						SOP("enquiry_soe_name===after " + enquiry_soe_name);
						enquiry_soe_id = CNumeric(ExecuteQuery("SELECT soe_id FROM " + compdb(comp_id) + "axela_soe"
								+ " WHERE soe_name = '" + enquiry_soe_name + "'"
								+ " LIMIT 1"));

						if (enquiry_soe_id.equals("0")) {
							enquiry_soe_id = "1";
						}
					}
					if (h == 32) {// /subsource

					}

					if (h == 33) {// //buyer
						enquiry_buyertype_id = CNumeric(ExecuteQuery("SELECT buyertype_id FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
								+ " WHERE buyertype_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (enquiry_buyertype_id.equals("0")) {
							enquiry_buyertype_id = "1";
						}
					}

					if (h == 34) {// /tradein
					}

					if (h == 35) {// /mode

					}

					if (h == 36) {// /lost

					}

					if (h == 37) {// /remarks
						enquiry_desc = sheetData[j][h];
						if (enquiry_desc.equals("null")) {
							enquiry_desc = "";
						}
					}
					if (h == 38) {// customerRequest

					}
					if (h == 39) {// ////dob
						contact_dob = sheetData[j][h];
						// 19/01/1989
						if (!contact_dob.equals("null")) {
							// Date dttemp = new SimpleDateFormat("dd-mmm-yyyy").parse(contact_dob);
							// enquiry_status_date = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
							boolean t2 = isValidDateFormatShort(contact_dob);
							if (t2 == true) {
								contact_dob = ConvertShortDateToStr(contact_dob);
							} else if (t2 == false) {
								contact_dob = "";
							}
						} else {
							contact_dob = "";
						}
					}
					if (h == 40) {
						contact_anniversary = sheetData[j][h];
						// 19-sep-1989
						if (!contact_anniversary.equals("null")) {
							// Date dttemp = new SimpleDateFormat("dd-mmm-yyyy").parse(contact_anniversary);
							// contact_anniversary = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
							boolean t2 = isValidDateFormatShort(contact_anniversary);
							if (t2 == true) {
								contact_anniversary = ConvertShortDateToStr(contact_anniversary);
							} else if (t2 == false) {
								contact_anniversary = "";
							}
						} else {
							contact_anniversary = "";
						}
					}
					if (h == 41) {// /testdrive given

					}

					if (h == 42) {// //testdrive date

					}

					if (h == 43) {// ///fvisit date

					}

					if (h == 44) {// //ageing

					}

					if (h == 45) {// ///dealer name

					}

					if (h == 46) {// ///Buying NO.

					}

					if (h == 47) {// ///evalator

					}

					if (h == 48) {// /Evaluator MSPIN

					}

					if (h == 49) {// //oldcar owner name

					}

					if (h == 50) {// ///evalation date

					}

					if (h == 51) {// //// Old Veh. Status

					}
					if (h == 52) {// /// Ref Price

					}
					if (h == 53) {// / Re Offered Price

					}

					if (h == 54) {// / Customer Exp. Price

					}

					if (h == 55) {// Latest Offered Price

					}

					if (h == 56) {// /Bought Date

					}

					if (h == 57) {// /Lost To POC

					}

					if (h == 58) {// Reference Type

					}

					if (h == 59) {// Referred By

					}

					if (h == 60) {// Reference No.

					}
					if (h == 61) {// Ref Vehicle Regn No.

					}
					if (h == 62) {// ref mobile no
					}
					if (h == 63) {// state dec
					}

					if (h == 64) {// dist
					}

					if (h == 65) {// teh
					}
					if (h == 66) {// village
					}

				}
				// SOP("enquiry_name = " + enquiry_name);
				enquiry_id = "0";
				if (!contact_mobile1.equals("") || !contact_phone1.equals("") || !contact_phone2.equals("")) {

					StrSql = "SELECT enquiry_id, enquiry_status_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " WHERE enquiry_branch_id = " + enquiry_branch_id + ""
							+ " AND (contact_mobile1 = '" + contact_mobile1 + "'"
							+ " OR contact_mobile2 = '" + contact_mobile1 + "')"
							+ " AND (contact_phone1 = '" + contact_phone1 + "'"
							+ " OR contact_phone1 = '" + contact_phone2 + "')"
							+ " AND (contact_phone2 = '" + contact_phone1 + "'"
							+ " OR contact_phone2 = '" + contact_phone2 + "')";
					// SOP("StrSql------1-----------" + StrSqlBreaker(StrSql));
					try {
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							enquiry_id = CNumeric(crs.getString("enquiry_id"));
							enquiry_status_id = CNumeric(crs.getString("enquiry_status_id"));
							// SOP("enquiry_id ======= " + enquiry_id);
						}
						crs.close();
					} catch (Exception ex) {
						SOP("AxelaAuto===" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
				}
				enquiry_contact_id = "0";
				enquiry_customer_id = "0";
				if (!contact_mobile1.equals("")) {
					StrSql = "SELECT contact_id, contact_customer_id"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " WHERE (contact_mobile1 = '" + contact_mobile1 + "'"
							+ " OR contact_mobile2 = '" + contact_mobile1 + "')"
							+ " AND (contact_phone1 = '" + contact_phone1 + "'"
							+ " OR contact_phone1 = '" + contact_phone2 + "')"
							+ " AND (contact_phone2 = '" + contact_phone1 + "'"
							+ " OR contact_phone2 = '" + contact_phone2 + "')";
					// SOP("StrSql------contact------" + StrSqlBreaker(StrSql));
					ResultSet rset = processQuery(StrSql, 0);
					while (rset.next()) {
						enquiry_contact_id = rset.getString("contact_id");
						enquiry_customer_id = rset.getString("contact_customer_id");
					}
					rset.close();
				}
				if ((!enquiry_id.equals("0"))) {
					error_msg += "<br>This Enquiry already available!";
				}
				// SOPError("enquiry_contact_id------" + enquiry_contact_id);
				// SOPError("enquiry_customer_id------" + enquiry_customer_id);
				// SOPError("contact_fname =--------- " + contact_fname);
				// SOPError("enquiry_status_id 1----------" + enquiry_status_id);
				// SOPError("enquiry_id =--------- " + enquiry_id);
				// SOPError("enquiry_model_id =--------- " + enquiry_model_id);

				if (!enquiry_model_id.equals("0")) {
					if (enquiry_id.equals("0")) {
						// SOP("enquiry_status_id 1=== " + enquiry_status_id);
						// SOP("contact_title_id =--------- " + contact_title_id);
						// if (enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1"))) {
						Enquiry_Quickadd_import enquiry = new Enquiry_Quickadd_import();
						// enquiry.contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
						enquiry.comp_id = comp_id;
						enquiry.emp_id = "1";
						enquiry.enquiry_team_id = "9999";
						enquiry.enquiry_emp_id = enquiry_emp_id;
						enquiry.contact_id = enquiry_contact_id;
						enquiry.enquiry_branch_id = enquiry_branch_id;
						enquiry.PopulateConfigDetails();
						// ////customer details////
						enquiry.customer_name = customer_name;
						enquiry.contact_mobile1 = contact_mobile1;
						enquiry.contact_city_id = contact_city_id;
						enquiry.contact_address = contact_address;
						enquiry.contact_pin = contact_pin;
						enquiry.contact_email1 = contact_email1;
						enquiry.contact_phone1 = contact_phone1;
						enquiry.contact_phone2 = contact_phone2;
						// ////contact details/////
						enquiry.contact_title_id = contact_title_id;
						enquiry.contact_fname = contact_fname;
						enquiry.contact_lname = contact_lname;
						enquiry.contact_jobtitle = "";
						enquiry.enquiry_customer_id = enquiry_customer_id;
						enquiry.enquiry_contact_id = enquiry_contact_id;
						enquiry.enquiry_title = "New " + model_name;
						enquiry.enquiry_desc = enquiry_desc;
						enquiry.contact_city_id = contact_city_id;
						enquiry.enquiry_date = enquiry_date;
						enquiry.enquiry_close_date = enquiry_date;
						SOP("enquiry_close_date========" + enquiry_date);
						enquiry.contact_city_id = contact_city_id;
						enquiry.contact_address = contact_address;
						enquiry.contact_pin = contact_pin;
						enquiry.enquiry_emp_id = enquiry_emp_id;
						// enquiry.crmfollowup_crm_emp_id = enquiry_emp_id;
						enquiry.enquiry_campaign_id = "2";
						enquiry.enquiry_soe_id = enquiry_soe_id;
						SOP("enquiry_sob_id==========" + enquiry_sob_id);
						enquiry.enquiry_sob_id = "2";
						enquiry.enquiry_budget = "0";
						enquiry.enquiry_notes = "";
						enquiry.enquiry_model_id = enquiry_model_id;
						enquiry.enquiry_item_id = enquiry_item_id;
						enquiry.enquiry_dmsno = enquiry_dmsno;
						enquiry.enquiry_priorityenquiry_id = enquiry_priorityenquiry_id;
						enquiry.enquiry_custtype_id = enquiry_custtype_id;
						enquiry.enquiry_loanfinancer = enquiry_loanfinancer;
						enquiry.enquiry_buyertype_id = enquiry_buyertype_id;
						enquiry.enquiry_entry_date = enquiry_entry_date;
						enquiry.enquiry_entry_id = enquiry_entry_id;
						enquiry.AddEnquiryFields();
						enquiry_id = enquiry.enquiry_id;
						error_msg += enquiry.msg;
						SOP("msg==========" + msg);
						// SOP("enquiry_id----------" + enquiry_id);
						if (!enquiry_id.equals("0") && error_msg.equals("")) {
							propcount++;
						}

						// }
					}
				}
				if (!error_msg.equals("")) {
					enquiry_error_msg += "<br>" + ++count + "." + " Enquiry Number: " + enquiry_dmsno + "==>" + error_msg;
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
