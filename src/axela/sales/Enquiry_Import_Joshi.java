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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import cloudify.connect.Connect;

public class Enquiry_Import_Joshi extends Connect {

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
				BranchAccess = GetSession("BranchAccess", request);
				enquiry_entry_id = emp_id;
				enquiry_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button1"));
				// contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
				// SOP("property branch_id=========" + branch_id);
				if (branch_id.equals("0")) {
					if (emp_id.equals("1")) {
						enquiry_branch_id = ExecuteQuery("SELECT branch_id"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_active = '1'"
								+ " LIMIT 1");
					} else if (!emp_id.equals("1")) {
						enquiry_branch_id = ExecuteQuery("SELECT emp_branch_id"
								+ " FROM " + compdb(comp_id) + "axela_emp_branch"
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
										msg += "<br>Document contents not in order!";
										String fileName1 = EnquiryImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = propcount + " Enquiries imported successfully!";
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
			response.sendRedirect("enquiry-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (enquiry_branch_id.equals("-1")) {
			msg = msg + "<br>Select Branch!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {

			String enquiry_dmsno = "", enquiry_date = "", enquiry_emp_id = "0", enquiry_team_id = "0", enquiry_custtype_id = "0";
			String customer_name = "", contact_name = "", contact_title_id = "1", contact_title, contact_fname = "", contact_lname = "", contact_fname_lname;
			String contact_mobile1 = "", contact_dob = "", contact_anniversary = "";
			String contact_phone1_code = "", contact_phone1 = "", contact_phone2_code = "", contact_phone2 = "", contact_email1 = "", contact_address = "", contact_city_id = "0", contact_pin = "";
			String enquiry_customer_id = "0", enquiry_contact_id = "0";
			String enquiry_model_id = "0", enquiry_item_id = "0", enquiry_fueltype_id = "0", enquiry_enquirycat_id = "1", enquiry_loanfinancer = "", enquiry_status_id = "0", enquiry_status_date = "";
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

					if (enquiry_branch_id.equals("-1")) {
						enquiry_branch_id = "1";
					}

					contact_city_id = CNumeric(ExecuteQuery("SELECT branch_city_id"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id like '" + enquiry_branch_id + "'"
							+ " LIMIT 1"));

					if (h == 0) {

					}

					if (h == 3) {
						enquiry_date = PadQuotes(sheetData[j][h]);
						// 19/01/1989
						// SOP("enquiry_date==" + enquiry_date);
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
						// contact_phone1 = sheetData[j][h];
						// if (contact_mobile1.equals("null")) {
						// contact_phone1 = "";
						// }
					}

					if (h == 5) {// ////fuel
						if (sheetData[j][h].equals("Petrol,Diesel")) {
							sheetData[j][h] = "Dual Fuel";
						}
						enquiry_fueltype_id = CNumeric(ExecuteQuery("SELECT fueltype_id"
								+ " FROM " + compdb(comp_id) + "axela_fueltype"
								+ " WHERE fueltype_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (enquiry_fueltype_id.equals("0")) {
							enquiry_fueltype_id = "1";
						}
					}
					if (h == 4) {
						// contact_email1 = sheetData[j][h];
						// if (contact_email1.equals("null")) {
						// contact_email1 = "";
						// }
					}

					if (h == 14) {// /soe
						enquiry_soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
								+ " FROM " + compdb(comp_id) + "axela_soe"
								+ " WHERE soe_name = '" + sheetData[j][h] + "'"
								+ " LIMIT 1"));
						if (enquiry_soe_id.equals("0")) {
							enquiry_soe_id = "10";
						}
					}

					if (h == 16) {// //model name
						model_name = PadQuotes(sheetData[j][h]);
						// SOP("model_name=====" + model_name);
						if (model_name.equals("DATSUN")) {
							model_name = "Datsun GO +";
						}
						enquiry_model_id = CNumeric(ExecuteQuery("SELECT model_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_name = '" + model_name + "'"
								+ " LIMIT 1"));
					}

					if (h == 17) { // item name
						String itemname = StringEscapeUtils.escapeHtml4((sheetData[j][h]));
						if (itemname.contains("'")) {
							itemname = itemname.replace("'", "&#39;");
						}
						StrSql = "SELECT item_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_name = '" + itemname + "'"
								+ " AND item_fueltype_id = " + enquiry_fueltype_id + ""
								+ " AND item_model_id = " + enquiry_model_id + ""
								+ " LIMIT 1";
						// SOP("StrSql==item==" + StrSql);
						enquiry_item_id = CNumeric(ExecuteQuery(StrSql));
						if (enquiry_item_id.equals("0")) {
							enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE 1=1"
									// + " AND item_fueltype_id = " + enquiry_fueltype_id + ""
									+ " AND item_model_id = " + enquiry_model_id + ""
									+ " LIMIT 1"));
						}
					}

					if (h == 24) {
						enquiry_emp_id = CNumeric(ExecuteQuery("SELECT emp_id"
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_name = '" + sheetData[j][h].toString() + "'"
								+ " LIMIT 1"));
						if (enquiry_emp_id.equals("0")) {
							enquiry_emp_id = "2";
						}
					}

					if (h == 25) {// //company
						customer_name = PadQuotes(sheetData[j][h]);
						if (customer_name.equals("null") || customer_name.equals("NOT AVAILABLE")) {
							customer_name = "";
						}
					}

					if (h == 25) {// ///contactname
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

					if (h == 27) {
						contact_email1 = PadQuotes(sheetData[j][h]);
						if (contact_email1.equals("null")) {
							contact_email1 = "";
						}
					}

					if (h == 28) {
						if (sheetData[j][h].contains("-")) {
							contact_mobile1 = PadQuotes(sheetData[j][h]).split("-")[0];
						} else if (sheetData[j][h].contains(",")) {
							contact_mobile1 = PadQuotes(sheetData[j][h]).split(",")[0];
						} else {
							contact_mobile1 = PadQuotes(sheetData[j][h]);
						}

						if (contact_mobile1.equals("null")) {
							contact_mobile1 = "";
						}
					}

					if (h == 31) {
						StrSql = "SELECT team_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team"
								+ " WHERE team_emp_id IN (SELECT emp_id"
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_name"
								+ " LIKE '%" + sheetData[j][h].toString() + "%')";
						// SOP("StrSql==team==" + StrSql);
						enquiry_team_id = CNumeric(ExecuteQuery(StrSql));
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
				// SOP("enquiry_contact_id==" + enquiry_contact_id);
				// SOP("enquiry_customer_id==" + enquiry_customer_id);
				// SOP("contact_fname==" + contact_fname);
				// SOP("enquiry_status_id 1==" + enquiry_status_id);
				// SOP("enquiry_id==111==" + enquiry_id);
				// SOP("enquiry_model_id==" + enquiry_model_id);
				if (!enquiry_model_id.equals("0")) {
					if ((enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1")))) {
						// SOP("enquiry_status_id 1=== " + enquiry_status_id);
						// SOP("contact_title_id =--------- " + contact_title_id);
						// if (enquiry_id.equals("0") || (!enquiry_id.equals("0") && !enquiry_status_id.equals("1"))) {
						Enquiry_Quickadd enquiry = new Enquiry_Quickadd();
						// enquiry.contact_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
						enquiry.comp_id = comp_id;
						enquiry.emp_id = emp_id;
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
						enquiry.enquiry_enquirycat_id = enquiry_enquirycat_id;
						enquiry.enquiry_contact_id = enquiry_contact_id;
						enquiry.enquiry_title = "New " + model_name;
						enquiry.enquiry_desc = enquiry_desc;
						enquiry.contact_city_id = contact_city_id;
						enquiry.enquiry_date = enquiry_date;
						enquiry.enquiry_close_date = strToShortDate(ToLongDate(kknow()));
						enquiry.contact_city_id = contact_city_id;
						enquiry.contact_address = contact_address;
						enquiry.contact_pin = contact_pin;
						enquiry.enquiry_emp_id = enquiry_emp_id;
						enquiry.enquiry_team_id = enquiry_team_id;
						// enquiry.crmfollowup_crm_emp_id = enquiry_emp_id;

						enquiry.enquiry_campaign_id = "2";
						enquiry.enquiry_soe_id = enquiry_soe_id;
						enquiry.enquiry_sob_id = "0";
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
						// SOP("enquiry_id==222==" + enquiry_id);
						if (!enquiry_id.equals("0")) {
							propcount++;
						}
						// }
					} else {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_branch_id = " + enquiry_branch_id + ","
								+ " enquiry_customer_id = " + enquiry_customer_id + ","
								+ " enquiry_contact_id = " + enquiry_contact_id + ","
								+ " enquiry_title= '" + "New " + model_name + "',"
								+ " enquiry_desc= '" + enquiry_desc + "',"
								+ " enquiry_model_id= " + enquiry_model_id + ","
								+ " enquiry_item_id= " + enquiry_item_id + ","
								+ " enquiry_date= '" + ConvertShortDateToStr(enquiry_date) + "',"
								+ " enquiry_close_date= '" + ToLongDate(kknow()) + "',"
								+ " enquiry_emp_id= " + enquiry_emp_id + ","
								+ " enquiry_stage_id= 2,"
								+ " enquiry_status_id= 1,"
								+ " enquiry_status_date= '" + enquiry_status_date + "',"
								+ " enquiry_status_desc= '',"
								+ " enquiry_priorityenquiry_id= '" + enquiry_priorityenquiry_id + "',"
								+ " enquiry_soe_id= '" + enquiry_soe_id + "',"
								+ " enquiry_dmsno= '" + enquiry_dmsno + "',"
								+ " enquiry_loanfinancer= '" + enquiry_loanfinancer + "',"
								+ " enquiry_buyertype_id= '" + enquiry_buyertype_id + "',"
								+ " enquiry_custtype_id= " + enquiry_custtype_id + ","
								+ " enquiry_notes= '',"
								+ " enquiry_modified_id= " + emp_id + ","
								+ " enquiry_modified_date= '" + ToLongDate(kknow()) + "'"
								+ " where enquiry_id = " + enquiry_id + " ";
						// SOP("StrSql update==enquiry==" + StrSqlBreaker(StrSql));
						updateQuery(StrSql);
					}
				}

			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=-1> Select </option>\n");
			Str.append("<option value=0").append(StrSelectdrop("0", enquiry_branch_id)).append(">Head Office</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"), enquiry_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append(" (");
				Str.append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOP("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
