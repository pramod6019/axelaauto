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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import cloudify.connect.Connect;

public class Enquiry_User_Import_Audi extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", error_msg = "", emp_id = "0";
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
	public String branch_id = "0", enquiry_branch_id = "0";
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
				// SOP("branch_id==" + branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				enquiry_entry_id = emp_id;
				enquiry_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button1"));
				// contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
				// SOP("property branch_id==" + branch_id);

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
						// SOP("branch_id==" + branch_id);
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
					// SOP("enquiry_branch_i==" + enquiry_branch_id);
					if (str1[i].equals("Upload")) {
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							CheckForm();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							}
							// SOP("fileName==" + fileName);
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
										msg = "<br>" + propcount + " Enquiries imported successfully! " + "<br>" + msg;
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
			response.sendRedirect("enquiry-user-import-maserati.jsp?msg=" + msg);
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

			String enquiry_id = "0", enquiry_dmsno = "", enquiry_date = "", enquiry_close_date = "", enquiry_emp_id = "0", enquiry_lead_id = "0", enquiry_team_id = "0", enquiry_custtype_id = "0";
			String customer_name = "", contact_name = "", contact_title_id = "0", contact_title, contact_fname = "", contact_lname = "", contact_fname_lname;
			String contact_mobile1 = "", customer_mobile2 = "", contact_dob = "", contact_anniversary = "";
			String contact_phone1_code = "", contact_phone1 = "", contact_phone2_code = "", contact_phone2 = "", contact_email1 = "", contact_email2 = "";
			String contact_address = "", contact_city_id = "0", contact_pin = "";
			String enquiry_customer_id = "0", enquiry_contact_id = "0";
			String enquiry_model_id = "0", enquiry_item_id = "0", enquiry_fueltype_id = "0", enquiry_loanfinancer = "", enquiry_status_id = "0", enquiry_status_date = "";
			String enquiry_soe_id = "0", enquiry_sob_id = "0", enquiry_campaign_id = "0", followup_feedbacktype_id = "0", enquiry_buyertype_id = "0", enquiry_desc = "", model_name = "";
			String enquiry_notes = "", enquiry_priorityenquiry_id = "0", enquiry_enquirycat_id = "0";
			int rowLength = 0;
			int columnLength = 0;
			int enquiryColumnLength = 24;
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
			int count = 0, error_count = 0;
			// SOP("rowLength==" + rowLength);
			// SOP("columnLength==" + columnLength);
			int secondrow = 0;

			if (enquiryColumnLength != columnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					// SOP("rowLength=="+(rowLength+1));
					for (h = 0; h < columnLength + 1; h++) {
						enquiry_id = "0";
						if (enquiry_branch_id.equals("0")) {
							enquiry_branch_id = "1";
						}

						// Lead Serial No.
						if (h == 0) {
							enquiry_lead_id = CNumeric(sheetData[j][h]);
							if (enquiry_lead_id.equals("0") || enquiry_lead_id.equals("")) {
								enquiry_lead_id = "0";
							}
							// SOP("enquiry_lead_id==" + enquiry_lead_id);
						}

						if (h == 1) {

						}

						if (h == 2) {

						}

						// Customer Name
						if (h == 3) {
							customer_name = PadQuotes(sheetData[j][h]);
							// SOP("customer_name==" + customer_name);

							contact_title_id = CNumeric(ExecuteQuery("SELECT title_id"
									+ " FROM " + compdb(comp_id) + "axela_title"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_title_id=title_id"
									+ " WHERE contact_fname = '" + customer_name + "'"
									+ " LIMIT 1"));
							if (contact_title_id.equals("0")) {
								contact_title_id = "1";
							}
							// SOP("contact_title_id==" + contact_title_id);
							if (!customer_name.equals("")) {
								if (contact_name.contains(" ")) {
									contact_fname = customer_name.split(" ")[0];
									if (contact_name.split(" ").length > 1) {
										contact_lname = contact_name.split(" ")[1];
									}

									if (contact_fname.equals("")) {
										contact_fname = customer_name;
										contact_lname = "";
									}
								} else {
									contact_fname = customer_name;
								}
								contact_name = contact_fname + " " + contact_lname;
								if (customer_name.equals("")
										|| customer_name.equalsIgnoreCase("OTHERS")
										|| customer_name.equalsIgnoreCase("NOT AVAILABLE")) {
									customer_name = contact_name;
								}
							}

							// SOP("contact_fname==" + contact_fname);
							// SOP("contact_lname==" + contact_lname);
						}

						// contact mobile1
						if (h == 4) {
							contact_mobile1 = PadQuotes(sheetData[j][h]);
							if (contact_mobile1.equals("null")) {
								contact_mobile1 = "";
							}
							else if (!contact_mobile1.contains("91-")) {
								contact_mobile1 = "91-" + contact_mobile1;
							}
							SOP("contact_mobile1==" + contact_mobile1);
						}

						// contact mobile2
						if (h == 5) {
							customer_mobile2 = PadQuotes(sheetData[j][h]);
							if (customer_mobile2.equals("null")) {
								customer_mobile2 = "";
							}
							else if (!customer_mobile2.contains("91-")) {
								customer_mobile2 = "91-" + customer_mobile2;
							}
							// SOP("customer_mobile2==" + customer_mobile2);
						}

						// contact email
						if (h == 6) {
							contact_email1 = sheetData[j][h];
							if (contact_email1.equals("null")) {
								contact_email1 = "";
							}
							// SOP("contact_email1==" + contact_email1);
						}

						// contact email2
						if (h == 7) {
							contact_email2 = sheetData[j][h];
							if (contact_email2.equals("null")) {
								contact_email2 = "";
							}
						}

						// Address
						if (h == 8) {
							contact_address = PadQuotes(sheetData[j][h]);
							if (contact_address.equals("null") || contact_address.equals("N/A")) {
								contact_address = "";
							}
							// SOP("contact_address==" + contact_address);
						}

						if (h == 9) {

						}

						// conact city
						if (h == 10) {
							contact_city_id = CNumeric(ExecuteQuery("SELECT city_id FROM " + compdb(comp_id) + "axela_city"
									+ " WHERE city_name like '" + sheetData[j][h] + "'"
									+ " LIMIT 1"));
							// SOP("contact_city_id==" + contact_city_id);
						}

						// Pincode
						if (h == 11) {
							contact_pin = PadQuotes(sheetData[j][h]);
							if (contact_pin.equals("null") || contact_pin.equals("N/A")) {
								contact_pin = "";
							}
							if (contact_pin.equals("")) {
								contact_pin = ExecuteQuery("SELECT branch_pin FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE branch_id = " + enquiry_branch_id + "");
							}
							// SOP("contact_pin==" + contact_pin);
						}

						// Enquiry date
						if (h == 12) {
							enquiry_date = (sheetData[j][h]);
							// SOP("enquiry_date==" + enquiry_date);
							if (!enquiry_date.equals("null")) {
								boolean t2 = isValidDateFormatShort(enquiry_date);
								if (t2 == true) {
									enquiry_date = enquiry_date;
									enquiry_close_date = strToShortDate(ToShortDate(kknow()));
								} else if (t2 == false) {
									enquiry_date = "";
									// error message
									error_msg += "<br>" + "Enquiry Date is Invalid!.";
								}
							} else {
								enquiry_date = "";
							}
							if (customer_name.equals("") && contact_mobile1.equals("") && enquiry_date.equals("")) {
								continue;
							}
						}

						// model name
						if (h == 13) {
							model_name = PadQuotes(sheetData[j][h]);
							// SOP("model_name==" + model_name);

							enquiry_model_id = CNumeric(ExecuteQuery("SELECT model_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
									+ " WHERE model_name = '" + model_name + "'"
									+ " LIMIT 1"));
							// SOP("enquiry_model_id==" + enquiry_model_id);

							// Variant Name
							if (!model_name.equals("")) {
								StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id"
										+ " WHERE model_name = '" + model_name + "'"
										+ " AND item_model_id = " + enquiry_model_id + ""
										+ " LIMIT 1";
								// SOP("StrSql item_id==" + StrSql);
								enquiry_item_id = CNumeric(ExecuteQuery(StrSql));
								if (enquiry_item_id.equals("0")) {
									enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE 1=1"
											// + " AND item_fueltype_id = " + enquiry_fueltype_id + ""
											+ " AND item_model_id = " + enquiry_model_id + ""
											+ " LIMIT 1"));
								}
								// SOP("enquiry_item_id==" + enquiry_item_id);
							}
							if (enquiry_fueltype_id.equals("0")) {
								enquiry_fueltype_id = "1";
							}
						}

						if (h == 14) {

						}

						// Qualified Lead Owner
						if (h == 15) {
							enquiry_emp_id = CNumeric(ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_name = '" + sheetData[j][h].toString() + "'"
									+ " LIMIT 1"));
							// SOP("enquiry_emp_id==" + enquiry_emp_id);
							if (!enquiry_emp_id.equals("")) {
								enquiry_team_id = CNumeric(ExecuteQuery("SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe"
										+ " WHERE teamtrans_emp_id = '" + enquiry_emp_id + "'"
										+ " LIMIT 1"));
								// SOP("enquiry_emp_id==" + enquiry_emp_id);
							}
							// SOP("enquiry_team_id==" + enquiry_team_id);
						}

						// Current Lead Rating
						if (h == 16) {// priority
							enquiry_priorityenquiry_id = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("enquiry_priorityenquiry_id==" + enquiry_priorityenquiry_id);
						}

						// Source of enquiry
						if (h == 17) {
							enquiry_soe_id = CNumeric(ExecuteQuery("SELECT soe_id FROM " + compdb(comp_id) + "axela_soe"
									+ " WHERE soe_name = '" + sheetData[j][h] + "'"
									+ " LIMIT 1"));
							// SOP("enquiry_soe_id==" + enquiry_soe_id);
							// if (enquiry_soe_id.equals("0")) {
							// enquiry_soe_id = "10";
							// }
							// SOP("enquiry_soe_id==" + enquiry_soe_id);
						}

						// Lead Business
						if (h == 18) {
							enquiry_sob_id = CNumeric(ExecuteQuery("SELECT sob_id FROM " + compdb(comp_id) + "axela_sob"
									+ " WHERE sob_name = '" + sheetData[j][h] + "'"
									+ " LIMIT 1"));
							// SOP("enquiry_sob_id==" + enquiry_sob_id);
						}

						if (h == 19) {

						}

						// campaign
						if (h == 20) {
							enquiry_campaign_id = CNumeric(ExecuteQuery("SELECT campaign_id FROM " + compdb(comp_id) + "axela_sales_campaign"
									+ " WHERE campaign_name LIKE '%" + sheetData[j][h] + "%'"
									+ " LIMIT 1"));
							// SOP("enquiry_campaign_id==" + enquiry_campaign_id);
						}

						// action taken
						if (h == 21) {
							followup_feedbacktype_id = CNumeric(ExecuteQuery("SELECT feedbacktype_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_feedback_type"
									+ " WHERE feedbacktype_name LIKE '%" + sheetData[j][h] + "%'"
									+ " LIMIT 1"));
							// SOP("followup_feedbacktype_id==" + followup_feedbacktype_id);
						}

						// remarks
						if (h == 22) {
							// enquiry_notes = PadQuotes(sheetData[j][h]);
						}

					}
					if (!contact_mobile1.equals("") || !contact_phone1.equals("") || !contact_phone2.equals("")) {
						count++;
						StrSql = "SELECT enquiry_id, enquiry_status_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " WHERE enquiry_branch_id = " + enquiry_branch_id + ""
								+ " AND (contact_mobile1 = '" + contact_mobile1 + "'"
								+ " OR contact_mobile2 = '" + contact_mobile1 + "') LIMIT 1";
						// + " AND (contact_phone2 = '" + contact_phone1 + "'"
						// + " OR contact_phone2 = '" + contact_phone2 + "')";
						SOP("StrSql==enquiry_id==" + StrSqlBreaker(StrSql));
						try {
							ResultSet rset = processQuery(StrSql, 0);

							while (rset.next()) {
								enquiry_id = CNumeric(rset.getString("enquiry_id"));
								enquiry_status_id = CNumeric(rset.getString("enquiry_status_id"));

								// SOP("enquiry_id==" + enquiry_id);
							}
							rset.close();
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
								+ " OR contact_mobile2 = '" + contact_mobile1 + "') LIMIT 1";

						// SOP("StrSql==contact==" + StrSqlBreaker(StrSql));
						ResultSet rset = processQuery(StrSql, 0);
						while (rset.next()) {
							enquiry_contact_id = rset.getString("contact_id");
							enquiry_customer_id = rset.getString("contact_customer_id");
						}
						rset.close();
					}

					if (enquiry_status_id.equals("") || enquiry_status_id.equals("0")) {
						enquiry_status_id = "1"; // open
					}
					if (enquiry_priorityenquiry_id.equals("") || enquiry_priorityenquiry_id.equals("0")) {
						enquiry_priorityenquiry_id = "3"; // By Default Priority should be Cold.
					}

					if (enquiry_buyertype_id.equals("") || enquiry_buyertype_id.equals("0")) {
						enquiry_buyertype_id = "1"; // first time buyer
					}

					if (enquiry_enquirycat_id.equals("0") || enquiry_enquirycat_id.equals("")) {
						enquiry_enquirycat_id = "1"; // category
					}
					SOP("enquiry_status_id==" + enquiry_status_id);
					SOP("enquiry_id==" + enquiry_id);
					if ((!enquiry_id.equals("0") && enquiry_status_id.equals("1"))) {
						// error_msg += "" + "<br>Enquiry is already present.";
						// SOP("error_msg==" + error_msg);
					}
					// SOPError("enquiry_contact_id==" + enquiry_contact_id);
					// SOPError("enquiry_customer_id==" + enquiry_customer_id);
					// SOPError("contact_fname==" + contact_fname);
					// SOPError("enquiry_status_id==" + enquiry_status_id);
					// SOPError("enquiry_id==" + enquiry_id);
					// SOPError("enquiry_model_id==final==" + enquiry_model_id);
					if (!enquiry_model_id.equals("0")) {
						if ((enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1")))) {
							// SOP("contact_title_id==" + contact_title_id);
							// if (enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1"))) {
							Enquiry_Quickadd enquiry = new Enquiry_Quickadd();
							// enquiry.contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
							enquiry.comp_id = comp_id;
							enquiry.emp_id = emp_id;
							enquiry.enquiry_emp_id = enquiry_emp_id;
							enquiry.enquiry_team_id = enquiry_team_id;
							enquiry.contact_id = enquiry_contact_id;
							enquiry.enquiry_branch_id = enquiry_branch_id;
							enquiry.PopulateConfigDetails();
							// ////customer details//////
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
							enquiry.enquiry_close_date = enquiry_close_date;
							enquiry.contact_address = contact_address;
							enquiry.contact_pin = contact_pin;
							enquiry.enquiry_emp_id = enquiry_emp_id;
							// enquiry.crmfollowup_crm_emp_id = enquiry_emp_id;
							enquiry.enquiry_campaign_id = enquiry_campaign_id;
							enquiry.enquiry_soe_id = enquiry_soe_id;
							enquiry.enquiry_sob_id = enquiry_sob_id;
							enquiry.enquiry_budget = "0";
							enquiry.enquiry_notes = enquiry_notes;
							enquiry.enquiry_model_id = enquiry_model_id;
							enquiry.enquiry_item_id = enquiry_item_id;
							enquiry.enquiry_dmsno = enquiry_dmsno;
							enquiry.enquiry_priorityenquiry_id = enquiry_priorityenquiry_id;
							enquiry.enquiry_custtype_id = enquiry_custtype_id;
							enquiry.enquiry_loanfinancer = enquiry_loanfinancer;
							enquiry.enquiry_buyertype_id = enquiry_buyertype_id;
							enquiry.enquiry_lead_id = enquiry_lead_id;
							enquiry.followup_feedbacktype_id = followup_feedbacktype_id;
							enquiry.enquiry_entry_id = emp_id;
							enquiry.enquiry_entry_date = ToLongDate(kknow());
							enquiry.AddEnquiryFields();
							enquiry_id = enquiry.enquiry_id;
							error_msg += enquiry.msg;
							// SOP("msg==" + msg);
							// SOP("enquiry.enquiry_id== + enquiry.enquiry_id);
							if (!enquiry_id.equals("0")) {
								propcount++;
							}

						}
					} else {
						error_msg += "<br>" + "Invalid Model Name.";
					}

					if (!error_msg.equals("")) {
						error_count++;
						if (!customer_name.equals("") && !customer_name.equals("-")) {
							msg += " <br><br> " + error_count + "." + " Please correct following errors for the Customer ===> " + customer_name;
						} else {
							msg += " <br><br> " + error_count + "." + " Please correct following errors for the Contact No. ===> " + contact_mobile1;
						}
						msg += error_msg;
					}
				}
			}
			// SOP("second row==>" + secondrow);
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
