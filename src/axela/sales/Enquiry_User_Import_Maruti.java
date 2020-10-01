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

public class Enquiry_User_Import_Maruti extends Connect {

	public String StrSql = "", StrHTML = "";
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

			String enquiry_dmsno = "", enquiry_date = "", enquiry_emp_id = "0", enquiry_sob_id = "0", enquiry_team_id = "0", enquiry_custtype_id = "0";
			String customer_name = "", contact_name = "", contact_title_id = "0", contact_title, contact_fname = "", contact_lname = "", contact_fname_lname;
			String contact_mobile1 = "", contact_dob = "", contact_anniversary = "";
			String contact_phone1_code = "", contact_phone1 = "", contact_phone2_code = "", contact_phone2 = "", contact_email1 = "", contact_address = "", contact_city_id = "0", contact_pin = "";
			String enquiry_customer_id = "0", enquiry_contact_id = "0";
			String enquiry_model_id = "", enquiry_item_id = "0", enquiry_fueltype_id = "0", enquiry_loanfinancer = "", enquiry_status_id = "0", enquiry_status_date = "";
			String enquiry_soe_id = "0", enquiry_buyertype_id = "0", enquiry_desc = "", model_name = "";
			String enquiry_notes = "", enquiry_priorityenquiry_id = "0";
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
						// 19/01/1989
						SOP("enquiry_date======" + enquiry_date);
						if (!enquiry_date.equals("null")) {
							// Date dttemp = new SimpleDateFormat("dd-mmm-yyyy").parse(enquiry_date);
							// enquiry_date = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
							// SOP("enquiry_date==" + enquiry_date);
							// String year = enquiry_date.substring(6, 10);
							// enquiry_date = enquiry_date.substring(0, 6) + year;
							boolean t2 = isValidDateFormatShort(enquiry_date);
							if (t2 == true) {
								enquiry_date = enquiry_date;
							} else if (t2 == false) {
								enquiry_date = "";
							}
						} else {
							enquiry_date = "";
						}
					}

					if (h == 3) {
						enquiry_team_id = CNumeric(ExecuteQuery("SELECT team_id FROM " + compdb(comp_id) + "axela_sales_team"
								+ " WHERE team_name = '" + sheetData[j][h].toString() + "'"
								+ " LIMIT 1"));
						// SOP("enquiry_team_id==============" + enquiry_team_id);
					}
					if (h == 4) {
						// contact_email1 = sheetData[j][h];
						// if (contact_email1.equals("null")) {
						// contact_email1 = "";
						// }
					}
					if (h == 5) {
						enquiry_emp_id = CNumeric(ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_name = '" + sheetData[j][h].toString() + "'"
								+ " LIMIT 1"));
						if (enquiry_emp_id.equals("0")) {
							enquiry_emp_id = "2";
						}
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
						enquiry_priorityenquiry_id = CNumeric(ExecuteQuery("SELECT priorityenquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
								+ " WHERE priorityenquiry_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (enquiry_priorityenquiry_id.equals("0")) {
							enquiry_priorityenquiry_id = "3"; // By Default Priority should be Cold.
						}
					}

					if (h == 30) {// /status date
						enquiry_status_date = sheetData[j][h];
						// 19/01/1989
						if (!enquiry_status_date.equals("null")) {
							// Date dttemp = new SimpleDateFormat("dd-mmm-yyyy").parse(enquiry_status_date);
							// enquiry_status_date = new SimpleDateFormat("dd/MM/yyyy").format(dttemp);
							boolean t2 = isValidDateFormatShort(enquiry_status_date);
							if (t2 == true) {
								enquiry_status_date = ConvertShortDateToStr(enquiry_status_date);
							} else if (t2 == false) {
								enquiry_status_date = "";
							}
						} else {
							enquiry_status_date = "";
						}
					}

					if (h == 31) {// /soe
						enquiry_soe_id = CNumeric(ExecuteQuery("SELECT soe_id FROM " + compdb(comp_id) + "axela_soe"
								+ " WHERE soe_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (enquiry_soe_id.equals("0")) {
							enquiry_soe_id = "10";
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

					if (h == 38) {// ////dob
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
					if (h == 39) {
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

					if (h == 46) {// ///poc enquiry

					}

					if (h == 47) {// ///poc executive

					}

					if (h == 48) {// /old car owner

					}

					if (h == 49) {// ///evaluation

					}

					if (h == 50) {// ///last offered

					}

					if (h == 51) {// ////bought date

					}
					if (h == 52) {// ///lost to poc

					}
					if (h == 53) {// /ref type

					}

					if (h == 54) {// //ref by

					}

					if (h == 55) {// /ref no

					}

					if (h == 56) {// /ref veh no

					}

					if (h == 57) {// /ref mob no

					}

					if (h == 58) {// /state

					}

					if (h == 59) {// /dist

					}

					if (h == 60) {// /tah

					}
					if (h == 62) {
						SOP("sheetData[j][h]========" + sheetData[j][h]);
						enquiry_sob_id = CNumeric(ExecuteQuery("SELECT sob_id FROM " + compdb(comp_id) + "axela_sob"
								+ " WHERE sob_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						SOP("enquiry_sob_id=======" + enquiry_sob_id);
					}

				}
				// SOP("enquiry_name = " + enquiry_name);
				enquiry_id = "0";
				if (!contact_mobile1.equals("") || !contact_phone1.equals("") || !contact_phone2.equals("")) {
					count++;
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
				if ((!enquiry_id.equals("0") && enquiry_status_id.equals("1"))) {
					msg += "" + "<br>This Enquiry already available!";
				}
				// SOPError("enquiry_contact_id------" + enquiry_contact_id);
				// SOPError("enquiry_customer_id------" + enquiry_customer_id);
				// SOPError("contact_fname =--------- " + contact_fname);
				// SOPError("enquiry_status_id 1----------" + enquiry_status_id);
				// SOPError("enquiry_id =--------- " + enquiry_id);
				// SOPError("enquiry_model_id =--------- " + enquiry_model_id);

				if (!enquiry_model_id.equals("0")) {
					if ((enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1")))) {
						// SOP("enquiry_status_id 1=== " + enquiry_status_id);
						// SOP("contact_title_id =--------- " + contact_title_id);
						// if (enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1"))) {
						Enquiry_Quickadd enquiry = new Enquiry_Quickadd();
						// enquiry.contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
						enquiry.comp_id = comp_id;
						enquiry.emp_id = emp_id;
						enquiry.enquiry_team_id = enquiry_team_id;
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
						enquiry.enquiry_sob_id = enquiry_sob_id;
						enquiry.enquiry_budget = "0";
						enquiry.enquiry_notes = "";
						enquiry.enquiry_model_id = enquiry_model_id;
						enquiry.enquiry_item_id = enquiry_item_id;
						enquiry.enquiry_dmsno = enquiry_dmsno;
						enquiry.enquiry_priorityenquiry_id = enquiry_priorityenquiry_id;
						enquiry.enquiry_custtype_id = enquiry_custtype_id;
						enquiry.enquiry_loanfinancer = enquiry_loanfinancer;
						enquiry.enquiry_buyertype_id = enquiry_buyertype_id;
						enquiry.AddEnquiryFields();
						enquiry_id = enquiry.enquiry_id;
						msg += enquiry.msg;
						SOP("msg==========" + msg);
						// SOP("enquiry_id----------" + enquiry_id);
						if (!enquiry_id.equals("0")) {
							propcount++;
						}
						// }
					}
				}

			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
