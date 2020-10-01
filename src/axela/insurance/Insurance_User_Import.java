//Shivaprasad

package axela.insurance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Insurance_User_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, followupcount = 0, updatecount = 0;
	public String upload = "";

	// insurance enquiry Data Membecrs....
	public String insurenquiry_id = "0";
	public String insurenquiry_branch_id = "0", insurenquiry_insurstatus_id = "0";
	public String insurenquiry_customer_id = "0", insurenquiry_contact_id = "0";
	public String insurenquiry_variant_id = "0", insurenquiry_modelyear = "";
	public String insurenquiry_chassis_no = "", insurenquiry_engine_no = "", insurenquiry_reg_no = "";
	public String insurenquiry_emp_id = "0";
	public String insurenquiry_sale_date = "";
	public String insurenquiry_date = "";
	public String insurenquiry_insurtype_id = "0", insurenquiry_renewal_date = "";
	public String soe_id = "0", sob_id = "0", soe_name = "", sob_name = "";
	public String insurenquiry_notes = "", insurenquiry_variant = "";
	public String BranchAccess = "";
	public String veh_followup = "0";
	public String interior = "", exterior = "", variant_name = "";
	public String option_id = "0";
	public String insurenquiry_entry_id = "";
	public String insurenquiry_entry_date = "";
	public String veh_insfollowupby = "";
	// Customer and Contact Data Membecrs....
	public String customer_name = "", contact_name = "", contact_title_id = "0", contact_fname = "", contact_lname = "", contact_fname_lname;
	public String contact_mobile1 = "", contact_mobile2 = "", contact_phone1 = "", contact_phone2 = "", contact_email1 = "", contact_email2 = "", contact_address = "", city_name = "",
			contact_city_id = "0", contact_pin = "";
	public String contact_dob = "", contact_anniversary = "";
	// General Data Membecrs....
	public String error_msg = "", status = "";
	public String insur_error_msg = "", month = "", day = "", year = "", servicedueyear = "";
	// insurance
	public String insurenquiry_previouscompname = "", insurenquiry_previousyearidv = "", insurenquiry_previousgrosspremium = "";
	public String insurenquiry_previousplanname = "", insurenquiry_policyexpirydate = "", insurenquiry_currentidv = "";
	public String insurenquiry_premium = "", insurenquiry_compoffered = "", insurenquiry_premiumwithzerodept = "",
			insurenquiry_plansuggested = "", insurenquiry_ncb = "";
	public CRE_Check crecheck = new CRE_Check();
	public String campaign_name = "", insurenquiry_campaign_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	List<String> enquiryid = new ArrayList<String>();
	List<String> enqempid = new ArrayList<String>();
	public String empids = "", enquiryids = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_insurance_enquiry_access", request, response);
				insurenquiry_entry_id = emp_id;
				insurenquiry_entry_date = ToLongDate(kknow());
				// upload = PadQuotes(request.getParameter("add_button"));
				Addfile(request, response);
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
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
			docsize = 2;
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

				for (int i = 0; it.hasNext() && i < 50; i++) {
					FileItem button = (FileItem) it.next();

					if (button.isFormField()) {
						String name = button.getFieldName();
						String value = button.getString();
						if (name.equals("assigninsurcre")) {
							enqempid.add(value);
						}
						if (name.equals("dr_insur_type_id")) {
							insurenquiry_insurtype_id = value;
						} else {
							str1[i] = value;
						}
					}
				}
				for (int k = 0; k < enqempid.size(); k++) {
					empids += enqempid.get(k) + ",";
				}
				if (empids.contains(",")) {
					empids = empids.substring(0, empids.length() - 1);
				}
				Iterator<?> iter = items.iterator();
				msg = "";
				for (int i = 0; iter.hasNext() && i < 50; i++) {
					insurenquiry_branch_id = str1[0];
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
										msg += "<br>Document contents not in order!";
										String fileName1 = VehicleImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = "<br>" + propcount + " Insurance Enquiry imported successfully!";
										// msg += "<br>" + updatecount + " Vehicles updated successfully!";
										msg += "<br>" + followupcount + " Followup updated successfully!" + "<br>" + insur_error_msg + "";
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("..//insurance/insurance-user-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (insurenquiry_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (insurenquiry_insurtype_id.equals("0")) {
			msg = msg + "<br>Select Insurance Type!";
		}

		// if (veh_crmemp_id.equals("0")) {
		// msg = msg + "<br>Select CRM Executive!";
		// }
		// if (veh_insfollowupby.equals("0")) {
		// msg = msg + "<br>Select Insurance Followup!";
		// }

		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
		// //SOP("msg===" + msg);
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			// //SOP("coming");
			int rowLength = 0;
			int columnLength = 0;
			int insurColumnLength = 37;
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
			// nfo("columnLength==========" + columnLength);
			// SOPInfo("rowLength==========" + rowLength);
			if (rowLength > 5000) {
				rowLength = 5000;
			}
			int h = 0;
			int j = 0;
			int count = 0;
			propcount = 0;
			followupcount = 0;
			updatecount = 0;

			if (insurColumnLength != columnLength) {

				insur_error_msg = "<br> Document columns doesn't match with the template!";
			}
			else {

				for (j = 1; j < rowLength + 1; j++) {
					// //SOP("j==" + j);
					CheckForm();
					error_msg = "";
					status = "";
					for (h = 0; h < columnLength + 1; h++) {
						if (h == 0) {
							customer_name = "";
							customer_name = PadQuotes(sheetData[j][h]);
							if (customer_name.equals("null") || customer_name.equals("")) {
								error_msg += "Customer Name Should not be empty" + "<br>";
							}

							// //SOP("customer_name==" + customer_name);
						}

						if (h == 1) {

							contact_name = "";
							contact_name = PadQuotes(sheetData[j][h]);
							contact_fname = "";
							contact_lname = "";
							// //SOP("contact_name==111==" + contact_name);
							if (contact_name.equals("null")) {
								contact_name = "";
								if (!customer_name.equals("")) {
									contact_name = customer_name;
								}
							}
							if (!contact_name.equals("")) {
								if (contact_name.contains(" ")) {
									contact_title_id = contact_name.split(" ")[0];

									contact_title_id = ExecuteQuery("SELECT title_id FROM " + compdb(comp_id) + "axela_title"
											+ " WHERE title_desc = '" + contact_title_id + "'");
									if (CNumeric(contact_title_id).equals("0")) {
										contact_title_id = "1";
										if (contact_name.split(" ").length > 0) {
											contact_fname = contact_name.split(" ")[0];
										}
										if (contact_name.split(" ").length > 1) {
											contact_lname = contact_name.split(" ")[1];
										}
									} else {

										if (contact_name.split(" ").length > 1) {
											contact_fname = contact_name.split(" ")[1];
										}
										if (contact_name.split(" ").length > 2) {
											contact_lname = contact_name.split(" ")[2];
										}

										if (contact_fname.equals("")) {
											contact_fname = contact_lname;
											contact_lname = "";
										}
									}
								} else {
									contact_title_id = "1";
									contact_fname = contact_name;
								}
								contact_name = contact_fname + " " + contact_lname;
								if (customer_name.equals("")
										|| customer_name.equalsIgnoreCase("OTHERS")
										|| customer_name.equalsIgnoreCase("NOT AVAILABLE")) {
									customer_name = contact_name;
								}
								// //SOP("contact_name==222==" + contact_name);
							}
						}

						if (h == 2) {
							contact_mobile1 = "";
							contact_mobile1 = PadQuotes(sheetData[j][h]);

							if (contact_mobile1.equals("")) {
								error_msg += "Mobile Number Should not be empty" + "<br>";
							}
							if (!contact_mobile1.equals("")) {
								if (contact_mobile1.contains(",")) {
									contact_mobile1 = contact_mobile1.split(",")[0];
								}
								contact_mobile1 = contact_mobile1.replaceAll("[^0-9]+", "");
								if (!contact_mobile1.contains("91-")) {
									contact_mobile1 = "91-" + contact_mobile1;
								}
								if (!IsValidMobileNo11(contact_mobile1)) {
									error_msg += " Please enter valid Mobile!<br>";
								}
							}
							// //SOP("contact_mobile1==" + contact_mobile1);
						}

						if (h == 3) {
							contact_mobile2 = "";
							contact_mobile2 = PadQuotes(sheetData[j][h]);
							if (contact_mobile2.equals("")) {
								contact_mobile2 = "";
							}
							if (!contact_mobile2.equals("")) {
								if (contact_mobile2.contains(",")) {
									contact_mobile2 = contact_mobile2.split(",")[0];
								}
								contact_mobile2 = contact_mobile2.replaceAll("[^0-9]+", "");
								if (!contact_mobile2.contains("91-")) {
									contact_mobile2 = "91-" + contact_mobile2;
								}
								if (!IsValidMobileNo11(contact_mobile2)) {
									error_msg += " Please enter valid Mobile!<br>";
								}
							}

							// SOP("contact_mobile2==" + contact_mobile2);
							// //SOP("contact_mobile2==" + contact_mobile2);
						}
						if (h == 4) {
							contact_phone1 = "";
							contact_phone1 = PadQuotes(sheetData[j][h]);

							if (contact_phone1.equals("")) {
								contact_phone1 = "";
							}
							if (!contact_phone1.equals("")) {
								contact_phone1 = contact_phone1.replaceAll("[^-0-9]+", "");
								if (!contact_phone1.contains("91-")) {
									contact_phone1 = "91-" + contact_phone1;
								}
								if (!IsValidPhoneNo11(contact_phone1)) {
									error_msg += " Please enter valid Phone!<br>";
								}
							}

							// SOP("contact_phone1==" + contact_phone1);
						}
						if (h == 5) {
							contact_phone2 = "";
							contact_phone2 = PadQuotes(sheetData[j][h]);

							if (contact_phone2.equals("")) {
								contact_phone2 = "";
							}
							if (!contact_phone2.equals("")) {
								contact_phone2 = contact_phone2.replaceAll("[^-0-9]+", "");
								if (!contact_phone2.contains("91-")) {
									contact_phone2 = "91-" + contact_phone2;
								}
								if (!IsValidPhoneNo11(contact_phone2)) {
									error_msg += " Please enter valid Phone!<br>";
								}
							}

							// SOP("contact_phone2==" + contact_phone2);
						}
						if (h == 6) {
							contact_email1 = "";
							contact_email1 = PadQuotes(sheetData[j][h]);
							if (contact_email1.equals("null")) {
								contact_email1 = "";
							}
							if (!IsValidEmail(contact_email1)) {
								contact_email1 = "";
							}
							// //SOP("contact_email1==" + contact_email1);
						}
						if (h == 7) {
							contact_email2 = "";
							contact_email2 = sheetData[j][h];
							if (contact_email2.equals("null")) {
								contact_email2 = "";
							}
							if (!IsValidEmail(contact_email2)) {
								contact_email2 = "";
							}
							// //SOP("contact_email2==" + contact_email2);
						}
						if (h == 8) {// /////address
							contact_address = "";
							contact_address = PadQuotes(sheetData[j][h]);
							if (contact_address.equals("null") || contact_address.equals("N/A")) {
								contact_address = "";
							}
							// //SOP("contact_address==" + contact_address);
						}
						if (h == 9) {
							city_name = "";
							contact_city_id = "0";
							city_name = PadQuotes(sheetData[j][h]);
							if (city_name.equals("null")) {
								city_name = "";
							}
							// //SOP("city_name==" + city_name);
							if (!contact_city_id.equals("")) {
								contact_city_id = CNumeric(ExecuteQuery("SELECT city_id FROM " + compdb(comp_id) + "axela_city"
										+ " WHERE city_name = '" + city_name + "'"));
							}
							if (CNumeric(contact_city_id).equals("0")) {
								contact_city_id = CNumeric(ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE branch_id = " + insurenquiry_branch_id + ""
										+ " LIMIT 1"));
							}
							// //SOP("contact_city_id==" + contact_city_id);
						}
						if (h == 10) {
							contact_pin = "";
							contact_pin = sheetData[j][h];
							// //SOP("contact_pin==" + contact_pin);
							if (contact_pin.equals("null") || contact_pin.equals("N/A")) {
								contact_pin = "";
							}
							if (contact_pin.equals("")) {
								contact_pin = PadQuotes(ExecuteQuery("SELECT branch_pin FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE branch_id = " + insurenquiry_branch_id + ""));
							}
						}

						if (h == 11) {
							insurenquiry_sale_date = "";
							day = "";
							month = "";
							year = "";
							insurenquiry_sale_date = sheetData[j][h];
							if (insurenquiry_sale_date.equals("null")) {
								insurenquiry_sale_date = "";

							}

							// if (insurenquiry_sale_date.equals("") && veh_insfollowupby.equals("1")) {
							// error_msg += " Sale date not present in the sheet!<br>";
							// }
							// //SOP("error_msg==" + error_msg);
							if (!insurenquiry_sale_date.equals("")) {

								if (!isValidDateFormatShort(insurenquiry_sale_date) && veh_insfollowupby.equals("1")) {
									error_msg += " Invalid Date format for Sale date " + insurenquiry_sale_date + "<br>";
									insurenquiry_sale_date = "";
									// //SOP("error_msg-----------------------------" + error_msg);
								} else if (isValidDateFormatShort(insurenquiry_sale_date)) {
									insurenquiry_sale_date = ConvertShortDateToStr(insurenquiry_sale_date);
									// veh_modelyear = veh_sale_date.substring(0, 4);
									// //SOP("veh_modelyear 2==" + veh_modelyear);
								} else if (insurenquiry_sale_date.split("/").length == 3) {
									month = insurenquiry_sale_date.split("/")[0];
									if (month.length() == 1) {
										month = "0" + month;
									}
									day = insurenquiry_sale_date.split("/")[1];
									if (day.length() == 1) {
										day = "0" + day;
									}
									year = insurenquiry_sale_date.split("/")[2];

									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										// //SOP("inside daymonthyear");
										insurenquiry_sale_date = year + month + day + "000000";
										// veh_modelyear = year;
										// //SOP("veh_modelyear=3==" + veh_modelyear);
									} else {
										insurenquiry_sale_date = "";
										// day = veh_sale_date.split("/")[0];
										// if (day.length() == 1) {
										// day = "0" + day;
										// }
										//
										// month = veh_sale_date.split("/")[1];
										// if (month.length() == 1) {
										// month = "0" + month;
										// }
										//
										// year = veh_sale_date.split("/")[2];
										//
										// if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										// // //SOP("inside daymonthyear");
										// veh_sale_date = year + month + day + "000000";
										// veh_modelyear = year;
										// // //SOP("veh_modelyear=in1==" + veh_modelyear);
										// } else {
										// veh_sale_date = "20140101000000";
										// veh_modelyear = "2014";
										// }
										// //SOP("veh_modelyear=in2==" + veh_modelyear);
									}
								} else {
									String insurenquiry_sale_date1 = fmtShr3tofmtShr1(insurenquiry_sale_date);
									// //SOP("veh_sale_date1====" + veh_sale_date1);
									if (isValidDateFormatStr(insurenquiry_sale_date1)) {
										insurenquiry_sale_date = insurenquiry_sale_date1.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
									}
									else {
										insurenquiry_sale_date = "";
									}
								}
								// //SOP("insurenquiry_sale_date=222==" + insurenquiry_sale_date);
							} else {

								// veh_sale_date = "20140101000000";
								// veh_modelyear = "2014";

								// //SOP("veh_modelyear=in4==" + veh_modelyear);
							}
							// //SOP("veh_sale_date=new==" + veh_sale_date);
						}

						if (h == 12) {
							insurenquiry_variant = "";
							insurenquiry_variant_id = "0";
							variant_name = "";
							if (variant_name.contains("'")) {
								variant_name = variant_name.replace("'", "");
							}
							variant_name = PadQuotes((sheetData[j][h]));

							// //SOP("item_name===" + item_name);

							if (variant_name.contains("(")) {
								variant_name = variant_name.replace("(", "&#40;");
							}
							if (variant_name.contains(")")) {
								variant_name = variant_name.replace(")", "&#41;");
							}
							insurenquiry_variant = variant_name;
							// //SOP("item_name===" + item_name);
							if (variant_name.equals("null") || variant_name.equals("0")) {
								variant_name = "";
							} else {
								StrSql = "SELECT variant_id FROM axela_preowned_variant WHERE variant_name='" + variant_name + "'";
								insurenquiry_variant_id = CNumeric(ExecuteQuery(StrSql));
								// SOP("StrSql==Item==" + StrSql);
							}

							// Change
							if (insurenquiry_variant_id.equals("0")) {
								variant_name = "Others";
								StrSql = "SELECT variant_id FROM axela_preowned_variant WHERE variant_name='" + variant_name + "'";
								insurenquiry_variant_id = CNumeric(ExecuteQuery(StrSql));

							}
							// SOP("insurenquiry_variant_id==222==" + insurenquiry_variant_id);

						}

						// change
						if (h == 13) {
							interior = "";
							interior = PadQuotes(sheetData[j][h]);
							if (interior.equals("null")) {
								interior = "";
							}
							// //SOP("interior==" + interior);
						}

						if (h == 14) {
							exterior = "";
							exterior = PadQuotes(sheetData[j][h]);
							if (exterior.equals("null")) {
								exterior = "";
							}
							// //SOP("exterior==" + exterior);
						}

						if (h == 15) {
							insurenquiry_chassis_no = "";
							insurenquiry_chassis_no = PadQuotes(sheetData[j][h]);
							if (insurenquiry_chassis_no.equals("null")) {
								insurenquiry_chassis_no = "";
							}

							// //SOP("insurenquiry_chassis_no==" + insurenquiry_chassis_no);
						}
						if (h == 16) {
							insurenquiry_engine_no = "";
							insurenquiry_engine_no = PadQuotes(sheetData[j][h]);
							// //SOP("veh_engine_no-------" + veh_engine_no);
							if (insurenquiry_engine_no.equals("null")) {
								insurenquiry_engine_no = "";
							}

							// //SOP("insurenquiry_engine_no-------" + insurenquiry_engine_no);

						}

						if (h == 17) {
							insurenquiry_modelyear = "";
							insurenquiry_modelyear = sheetData[j][h];
							if (insurenquiry_modelyear.equals("null")) {
								insurenquiry_modelyear = "";
							}
							// //SOP("insurenquiry_modelyear=4=" + insurenquiry_modelyear);
						}

						if (h == 18) {
							insurenquiry_reg_no = "";
							insurenquiry_reg_no = PadQuotes(sheetData[j][h]);
							if (insurenquiry_reg_no.equals("null")) {
								insurenquiry_reg_no = "";
							}

						}

						if (h == 19) {
							contact_dob = "";
							day = "";
							month = "";
							year = "";
							contact_dob = sheetData[j][h];
							if (contact_dob.equals("null")) {
								contact_dob = "";
							}
							// //SOP("contact_dob==" + contact_dob);
							if (!contact_dob.equals("")) {
								if (isValidDateFormatShort(contact_dob)) {
									contact_dob = ConvertShortDateToStr(contact_dob);
								} else if (contact_dob.split("/").length == 3) {
									month = contact_dob.split("/")[0];
									if (month.length() == 1) {
										month = "0" + month;
									}
									day = contact_dob.split("/")[1];
									if (day.length() == 1) {
										day = "0" + day;
									}
									year = contact_dob.split("/")[2];
									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										// //SOP("inside daymonthyear contact_dob");
										contact_dob = year + month + day + ""
												+ ToLongDate(kknow()).substring(8, 14) + "";
									}
									else {
										contact_dob = "";
										// error_msg += " Invalid DOB<br>";
									}
									// //SOP("contact_dob=111==" + contact_dob);
								}
								else {
									contact_dob = "";
									// error_msg += " Invalid DOB<br>";
								}
							}
							// //SOP("contact_dob=111==" + contact_dob);
						}
						if (h == 20) {
							contact_anniversary = "";
							day = "";
							month = "";
							year = "";
							contact_anniversary = sheetData[j][h];
							if (contact_anniversary.equals("null")) {
								contact_anniversary = "";
							}
							// //SOP("contact_anniversary==" + contact_anniversary);
							if (!contact_anniversary.equals("")) {
								if (isValidDateFormatShort(contact_anniversary)) {
									contact_anniversary = ConvertShortDateToStr(contact_anniversary);
								} else if (contact_anniversary.split("/").length == 3) {
									month = contact_anniversary.split("/")[0];
									if (month.length() == 1) {
										month = "0" + month;
									}
									day = contact_anniversary.split("/")[1];
									if (day.length() == 1) {
										day = "0" + day;
									}
									year = contact_anniversary.split("/")[2];
									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										// //SOP("inside daymonthyear contact_anniversary");
										contact_anniversary = year + month + day + ""
												+ ToLongDate(kknow()).substring(8, 14) + "";
									}
									else {
										contact_anniversary = "";
										// error_msg += " Invalid Date of Anniversary<br>";
									}
									// //SOP("contact_anniversary=111==" + contact_anniversary);
								} else {
									contact_anniversary = "";
									// error_msg += " Invalid Date of Anniversary<br>";
								}
							}
							// //SOP("contact_anniversary=111==" + contact_anniversary);
						}
						if (h == 21) {
							soe_name = "";
							soe_id = "0";
							soe_name = PadQuotes(sheetData[j][h]);
							if (soe_name.equals("null")) {
								soe_name = "";
							}
							if (soe_name.equals("")) {
								soe_name = "Service Data";
							}
							// //SOP("soe_name==" + soe_name);
							if (!soe_name.equals("")) {
								soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
										+ " FROM " + compdb(comp_id) + "axela_soe WHERE soe_name='" + soe_name + "'"));
							}
							// //SOP("soe_id==" + soe_id);
						}

						if (h == 22) {
							sob_name = "";
							sob_id = "0";
							sob_name = PadQuotes(sheetData[j][h]);
							if (sob_name.equals("null")) {
								sob_name = "";
							}
							if (sob_name.equals("")) {
								sob_name = "Service Data";
							}
							// //SOP("sob_name==" + sob_name);
							if (!sob_name.equals("")) {
								sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
										+ " FROM " + compdb(comp_id) + "axela_sob WHERE sob_name='" + sob_name + "'"));
							}
							// //SOP("sob_id==" + sob_id);
						}

						if (h == 23) {
							campaign_name = "";
							insurenquiry_campaign_id = "0";
							campaign_name = PadQuotes(sheetData[j][h]);
							if (campaign_name.equals("")) {
								campaign_name = "Others";
							}
							if (!campaign_name.equals("")) {
								StrSql = "SELECT campaign_id"
										+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON camptrans_campaign_id = campaign_id"
										+ " WHERE campaign_name='" + campaign_name + "'"
										+ " AND camptrans_branch_id=" + insurenquiry_branch_id;
								insurenquiry_campaign_id = CNumeric(ExecuteQuery(StrSql));
								SOP("insurenquiry_campaign_id=11=" + insurenquiry_campaign_id);

								if (insurenquiry_campaign_id.equals("0")) {
									StrSql = "SELECT campaign_id"
											+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
											+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON camptrans_campaign_id = campaign_id"
											+ " WHERE campaign_name='Others'"
											+ " AND camptrans_branch_id=" + insurenquiry_branch_id;
									insurenquiry_campaign_id = CNumeric(ExecuteQuery(StrSql));
									if (insurenquiry_campaign_id.equals("0")) {
										error_msg += "Campaign not found!" + "<br>";
									}
								}
							}
						}
						if (h == 24) {

						}
						if (h == 25) {
							insurenquiry_renewal_date = "";
							day = "";
							month = "";
							year = "";
							// veh_modelyear = "";//COMMENTED FOR INDEL
							insurenquiry_renewal_date = sheetData[j][h];
							// //SOP("veh_renewal_date==" + veh_renewal_date);
							if (insurenquiry_renewal_date.equals("null")) {
								insurenquiry_renewal_date = "";
							}
							if (insurenquiry_renewal_date.equals("") && veh_insfollowupby.equals("2")) {
								error_msg += " Renewal date not present in the sheet!<br>";
							}
							if (!insurenquiry_renewal_date.equals("")) {
								if (!isValidDateFormatShort(insurenquiry_renewal_date) && veh_insfollowupby.equals("2")) {
									error_msg += " Invalid Date format for Renewal date " + insurenquiry_renewal_date + "<br>";
									insurenquiry_renewal_date = "";
								} else if (isValidDateFormatShort(insurenquiry_renewal_date)) {
									insurenquiry_renewal_date = ConvertShortDateToStr(insurenquiry_renewal_date);
									// //SOP("veh_renewal_date==" + veh_renewal_date);
									// veh_modelyear = veh_renewal_date.substring(0, 4);
									// //SOP("veh_modelyear=7==" + veh_modelyear);
								} else if (insurenquiry_renewal_date.split("/").length == 3) {
									month = insurenquiry_renewal_date.split("/")[0];
									if (month.length() == 1) {
										month = "0" + month;
									}
									day = insurenquiry_renewal_date.split("/")[1];
									if (day.length() == 1) {
										day = "0" + day;
									}
									year = insurenquiry_renewal_date.split("/")[2];

									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										// //SOP("inside daymonthyear");
										insurenquiry_renewal_date = year + month + day + "000000";
										insurenquiry_modelyear = year;
										// //SOP("veh_modelyear=8==" + veh_modelyear);
									} else {
										insurenquiry_renewal_date = "";
									}
								} else {
									insurenquiry_renewal_date = "";
								}
								// //SOP("veh_sale_date=222==" + veh_sale_date);
							}
							// //SOP("veh_renewal_date=new==" + veh_renewal_date);
							// SOP("veh_renewal_date=new==" + insurenquiry_renewal_date);
							// SOP("veh_sale_date=new==" + insurenquiry_sale_date);
							if (insurenquiry_renewal_date.equals("") && insurenquiry_sale_date.equals("")) {
								error_msg += " Renewal Date/Purchase Date either one should be present ==> " + insurenquiry_chassis_no + "<br>";
							}
						}

						// Previous Insurance Company name
						if (h == 26) {
							insurenquiry_previouscompname = "";
							insurenquiry_previouscompname = PadQuotes(sheetData[j][h]);
						}
						// Previous Year IDV
						if (h == 27) {
							insurenquiry_previousyearidv = "";
							insurenquiry_previousyearidv = PadQuotes(sheetData[j][h]);
						}
						// Previous Gross Premium
						if (h == 28) {
							insurenquiry_previousgrosspremium = "";
							insurenquiry_previousgrosspremium = PadQuotes(sheetData[j][h]);
						}
						// Previous Plan Name
						if (h == 29) {
							insurenquiry_previousplanname = "";
							insurenquiry_previousplanname = PadQuotes(sheetData[j][h]);
						}
						// Policy Expiry Date
						if (h == 30) {
							insurenquiry_policyexpirydate = "";
							day = "";
							month = "";
							year = "";
							insurenquiry_policyexpirydate = sheetData[j][h];
							if (!insurenquiry_policyexpirydate.equals("")) {
								if (isValidDateFormatShort(insurenquiry_policyexpirydate)) {
									insurenquiry_policyexpirydate = ConvertShortDateToStr(insurenquiry_policyexpirydate);
								} else if (insurenquiry_policyexpirydate.split("/").length == 3) {
									month = insurenquiry_policyexpirydate.split("/")[0];
									if (month.length() == 1) {
										month = "0" + month;
									}
									day = insurenquiry_policyexpirydate.split("/")[1];
									if (day.length() == 1) {
										day = "0" + day;
									}
									year = insurenquiry_policyexpirydate.split("/")[2];
									if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
										insurenquiry_policyexpirydate = year + month + day + ""
												+ ToLongDate(kknow()).substring(8, 14) + "";
									}
									else {
										insurenquiry_policyexpirydate = "";
									}
								} else {
									insurenquiry_policyexpirydate = "";
								}
							}
						}
						// Current IDV
						if (h == 31) {
							insurenquiry_currentidv = "";
							insurenquiry_currentidv = PadQuotes(sheetData[j][h]);
						}
						// Premium
						if (h == 32) {
							insurenquiry_premium = "";
							insurenquiry_premium = PadQuotes(sheetData[j][h]);
						}
						// Premium with Zero Dept.
						if (h == 33) {
							insurenquiry_premiumwithzerodept = "";
							insurenquiry_premiumwithzerodept = PadQuotes(sheetData[j][h]);
						}
						// Insurance Company offered
						if (h == 34) {
							insurenquiry_compoffered = "";
							insurenquiry_compoffered = PadQuotes(sheetData[j][h]);
						}
						// Plan Suggested
						if (h == 35) {
							insurenquiry_plansuggested = "";
							insurenquiry_plansuggested = PadQuotes(sheetData[j][h]);
						}
						// NCB
						if (h == 36) {
							insurenquiry_ncb = "";
							insurenquiry_ncb = PadQuotes(sheetData[j][h]);
						}
					}
					// //SOP("insurenquiry_previouscompname==" + insurenquiry_previouscompname);
					// //SOP("insurenquiry_previousyearidv==" + insurenquiry_previousyearidv);
					// //SOP("insurenquiry_previousgrosspremium==" + insurenquiry_previousgrosspremium);
					// //SOP("insurenquiry_previousplanname==" + insurenquiry_previousplanname);
					// //SOP("insurenquiry_policyexpirydate==" + insurenquiry_policyexpirydate);
					// //SOP("insurenquiry_currentidv==" + insurenquiry_currentidv);
					// //SOP("insurenquiry_premium==" + insurenquiry_premium);
					// //SOP("insurenquiry_premiumwithzerodept==" + insurenquiry_premiumwithzerodept);
					// //SOP("insurenquiry_compoffered==" + insurenquiry_compoffered);
					// //SOP("insurenquiry_plansuggested===" + insurenquiry_plansuggested);
					// //SOP("insurenquiry_ncb===" + insurenquiry_ncb);

					insurenquiry_id = "0";
					insurenquiry_contact_id = "0";
					insurenquiry_customer_id = "0";
					//
					// //SOP("insurenquiry_reg_no==" + insurenquiry_reg_no);
					// //SOP("insurenquiry_variant_id==" + insurenquiry_variant_id);
					// //SOP("msg==" + msg);
					// SOP("msg==" + msg);
					// SOP("error_msg==" + error_msg);
					// if ((!insurenquiry_reg_no.equals("")) && !insurenquiry_variant_id.equals("0")) {
					if (msg.equals("") && error_msg.equals("")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							// if (!insurenquiry_reg_no.equals("")) {

							InsertInsurEnquiry();
							// updatecount++;

							// }
							conntx.commit();
						} catch (Exception e) {
							if (conntx.isClosed()) {
								SOPError("connection is closed.....");
							}
							if (!conntx.isClosed() && conntx != null) {
								conntx.rollback();
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
					// }
					if (!error_msg.equals("")) {
						if (!insurenquiry_reg_no.equals("") && !insurenquiry_reg_no.equals("0")) {
							insur_error_msg += ++count + "." + " Registration No.===> " + insurenquiry_reg_no + "<br>" + error_msg;
						} else if (!insurenquiry_chassis_no.equals("") && !insurenquiry_chassis_no.equals("0")) {
							insur_error_msg += ++count + "." + " Chassis No.===> " + insurenquiry_chassis_no + "<br>" + error_msg;
						}
					}
				}
				if (!empids.equals("")) {
					AddInsurFollowupFields();
				}
			}

		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateInsurExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					+ " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), insurenquiry_emp_id));
				Str.append(">").append(crs.getString("insuremp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void InsertInsurEnquiry() {

		try {
			if (!insurenquiry_variant_id.equals("0")) {
				if (!contact_mobile1.equals("")) {
					StrSql = "SELECT contact_id, insurenquiry_insurstatus_id, contact_customer_id"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
							+ " WHERE 1 = 1 "
							+ " AND (contact_mobile1 = '" + contact_mobile1 + "'"
							+ " OR contact_mobile2 = '" + contact_mobile1 + "') "
							+ " AND contact_contacttype_id = 10"
							+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id;
					// SOP("StrSql==Check Enquiry Mobile 1==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							insurenquiry_customer_id = crs.getString("contact_customer_id");
							insurenquiry_contact_id = crs.getString("contact_id");
							insurenquiry_insurstatus_id = crs.getString("insurenquiry_insurstatus_id");
						}

					}

					crs.close();
					// SOP("insurenquiry_customer_id==first==" + insurenquiry_customer_id);
					if (!insurenquiry_customer_id.equals("0") && insurenquiry_insurstatus_id.equals("1")) {
						error_msg = error_msg + "Enquiry already present with this Mobile No.===>" + contact_mobile1.replace("91-", "") + "<br>";
					}
					if (!contact_mobile2.equals("")) {
						StrSql = "SELECT contact_id, insurenquiry_insurstatus_id, contact_customer_id"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
								+ " WHERE 1 = 1 "
								+ " AND (contact_mobile1 = '" + contact_mobile2 + "'"
								+ " OR contact_mobile2 = '" + contact_mobile2 + "') "
								+ " AND contact_contacttype_id = 10"
								+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id;
						// SOP("StrSql==Check Enquiry for mobile 2==" + StrSql);
						CachedRowSet crs1 = processQuery(StrSql, 0);
						if (crs1.isBeforeFirst()) {
							while (crs1.next()) {
								insurenquiry_customer_id = crs1.getString("contact_customer_id");
								insurenquiry_contact_id = crs1.getString("contact_id");
								insurenquiry_insurstatus_id = crs1.getString("insurenquiry_insurstatus_id");
							}

						}
						crs1.close();
						// SOP("insurenquiry_customer_id==last=" + insurenquiry_customer_id);
						if (!insurenquiry_customer_id.equals("0") && insurenquiry_insurstatus_id.equals("1")) {
							error_msg = error_msg + "Enquiry already present with this Mobile No.===>" + contact_mobile2.replace("91-", "") + "<br>";
						}
					}

				}
				// SOP("error_msg== last =" + error_msg);
				// SOP("insurenquiry_insurstatus_id==last=" + insurenquiry_insurstatus_id);
				if (error_msg.equals("")) {
					insurenquiry_customer_id = AddCustomer();
					insurenquiry_contact_id = AddContact();
					AddEnquiry();
				}
			}
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
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			} catch (Exception e) {
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			}
		}
	}
	public void updateContact(String veh_contact_id) {
		try {

			stmttx = conntx.createStatement();
			// For updating Contact Details for existing vehicles...
			StrSql = " UPDATE " + compdb(comp_id) + "axela_customer_contact"
					+ " SET ";
			if (!contact_mobile1.equals("")) {
				StrSql += " contact_mobile1 = '" + contact_mobile1 + "',"
						+ " contact_mobile2_phonetype_id = 4,";
			}
			if (!contact_mobile2.equals("")) {
				StrSql += " contact_mobile2 = '" + contact_mobile2 + "',"
						+ " contact_mobile2_phonetype_id = 4,";
			}
			StrSql += " contact_modified_id = " + insurenquiry_entry_id + ","
					+ " contact_modified_date = '" + insurenquiry_entry_date + "'"
					+ " WHERE 1=1"
					// + " AND veh_vehsource_id = 2 "
					+ " AND contact_id = " + veh_contact_id;
			// //SOP("StrSql=veh Contact update==" + StrSql);
			stmttx.execute(StrSql);

			stmttx.execute(StrSql);
			conntx.commit();

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
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			} catch (Exception e) {
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			}
		}
	}

	public void UpdateVehicle() {
		try {

			stmttx = conntx.createStatement();

			StrSql = " SELECT insurenquiry_customer_id,insurenquiry_contact_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " WHERE 1=1" // veh_so_id != 0
					+ " AND insurenquiry_id = " + insurenquiry_id;
			// //SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurenquiry_customer_id = crs.getString("insurenquiry_customer_id");
					insurenquiry_contact_id = crs.getString("insurenquiry_contact_id");
				}
				if (!insurenquiry_contact_id.equals("0")) {
					// checking for contact_contacttype_id Insurance
					insurenquiry_contact_id = CNumeric(ExecuteQuery("SELECT  contact_id  FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
							+ " WHERE contact_contacttype_id = 10"
							+ " AND customer_id =" + insurenquiry_customer_id));
					if (!insurenquiry_contact_id.equals("0")) {
						updateContact(insurenquiry_contact_id);
					} else if (insurenquiry_contact_id.equals("0")) {
						insurenquiry_contact_id = AddContact();
					}

				}
				// SOPInfo("insurenquiry_customer_id==" + insurenquiry_customer_id);
				// SOPInfo("insurenquiry_contact_id==" + insurenquiry_contact_id);
				// SOPInfo("insurenquiry_id==" + insurenquiry_id);
			}

			else {
				insurenquiry_customer_id = AddCustomer();
				insurenquiry_contact_id = AddContact();
			}
			// SOPInfo("insurenquiry_customer_id=1=" + insurenquiry_customer_id);
			// SOPInfo("insurenquiry_contact_id=1=" + insurenquiry_contact_id);
			// SOPInfo("insurenquiry_id=1=" + insurenquiry_id);
			crs.close();

			// For updating enquiry info
			StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " SET ";
			if (!insurenquiry_branch_id.equals("0")) {
				StrSql += " insurenquiry_branch_id = " + insurenquiry_branch_id + ",";
			}
			StrSql += " insurenquiry_insurtype_id = " + insurenquiry_insurtype_id + ",";

			if (!insurenquiry_sale_date.equals("0") && !insurenquiry_sale_date.equals("") && veh_insfollowupby.equals("1")) {
				StrSql += " insurenquiry_sale_date = " + insurenquiry_sale_date + ",";
			}
			if (!insurenquiry_renewal_date.equals("0") && !insurenquiry_renewal_date.equals("") && veh_insfollowupby.equals("2")) {
				StrSql += " insurenquiry_renewal_date = " + insurenquiry_renewal_date + ",";
			}

			if (!insurenquiry_customer_id.equals("0")) {
				StrSql += " insurenquiry_customer_id = " + insurenquiry_customer_id + ",";
			}
			if (!insurenquiry_contact_id.equals("0")) {
				StrSql += " insurenquiry_contact_id = " + insurenquiry_contact_id + ",";
			}
			if (!insurenquiry_variant.equals("")) {
				StrSql += " insurenquiry_variant = '" + insurenquiry_variant + "',";
			}
			if (!insurenquiry_reg_no.equals("")) {
				StrSql += " insurenquiry_reg_no = '" + insurenquiry_reg_no + "',";
			}
			if (!insurenquiry_chassis_no.equals("")) {
				StrSql += " insurenquiry_chassis_no = '" + insurenquiry_chassis_no + "',";
			}
			if (!insurenquiry_engine_no.equals("")) {
				StrSql += " insurenquiry_engine_no = '" + insurenquiry_engine_no + "',";
			}

			if (!insurenquiry_previouscompname.equals("")) {
				StrSql += " insurenquiry_previouscompname = '" + insurenquiry_previouscompname + "',";
			}
			if (!insurenquiry_previousyearidv.equals("")) {
				StrSql += " insurenquiry_previousyearidv = '" + insurenquiry_previousyearidv + "',";
			}
			if (!insurenquiry_previousgrosspremium.equals("")) {
				StrSql += " insurenquiry_previousgrosspremium = '" + insurenquiry_previousgrosspremium + "',";
			}
			if (!insurenquiry_previousplanname.equals("")) {
				StrSql += " insurenquiry_previousplanname = '" + insurenquiry_previousplanname + "',";
			}
			if (!insurenquiry_policyexpirydate.equals("")) {
				StrSql += " insurenquiry_policyexpirydate = '" + insurenquiry_policyexpirydate + "',";
			}
			if (!insurenquiry_currentidv.equals("")) {
				StrSql += " insurenquiry_currentidv = '" + insurenquiry_currentidv + "',";
			}
			if (!insurenquiry_premium.equals("")) {
				StrSql += " insurenquiry_premium = '" + insurenquiry_premium + "',";
			}
			if (!insurenquiry_premiumwithzerodept.equals("")) {
				StrSql += " insurenquiry_premiumwithzerodept = '" + insurenquiry_premiumwithzerodept + "',";
			}
			if (!insurenquiry_compoffered.equals("")) {
				StrSql += " insurenquiry_compoffered = '" + insurenquiry_compoffered + "',";
			}
			if (!insurenquiry_plansuggested.equals("")) {
				StrSql += " insurenquiry_plansuggested = '" + insurenquiry_plansuggested + "',";
			}
			if (!insurenquiry_ncb.equals("")) {
				StrSql += " insurenquiry_ncb = '" + insurenquiry_ncb + "',";
			}
			if (!soe_id.equals("")) {
				StrSql += " insurenquiry_soe_id = " + soe_id + ",";
			}
			if (!sob_id.equals("")) {
				StrSql += " insurenquiry_sob_id = " + sob_id + ",";
			}
			if (!insurenquiry_campaign_id.equals("")) {
				StrSql += " insurenquiry_campaign_id = " + insurenquiry_campaign_id + ",";
			}
			StrSql += " insurenquiry_entry_id = " + insurenquiry_entry_id + ",";
			StrSql += " insurenquiry_entry_date =" + insurenquiry_entry_date + ",";

			if (!insurenquiry_emp_id.equals("0")) {
				StrSql += " insurenquiry_emp_id = " + insurenquiry_emp_id + ",";
			}
			StrSql = StrSql.substring(0, StrSql.length() - 1);
			StrSql += " WHERE insurenquiry_id =" + insurenquiry_id;

			// //SOP("strsql===========update====Enquiry=veh===" + StrSql);
			stmttx.execute(StrSql);
			updatecount++;

			// For inserting Insurance followup of the vehicle into insur_followup table
			if (veh_insfollowupby.equals("1") && !insurenquiry_sale_date.equals("") || veh_insfollowupby.equals("2") && !insurenquiry_renewal_date.equals("")) {
				// Delete the existing followups whose desc is empty
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup"
						+ "	WHERE insurfollowup_desc = ''"
						+ "	AND insurfollowup_veh_id = " + insurenquiry_id;
				// //SOP("StrSql==Delete==Insur Followup==" + StrSql);
				stmttx.execute(StrSql);

				// Inserting the new followups based on Sale date or Renewal date

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
						+ " (insurfollowup_veh_id,"
						+ " insurfollowup_followuptype_id,"
						+ " insurfollowup_entry_id,"
						+ " insurfollowup_entry_time,"
						+ " insurfollowup_followup_time)"
						+ " VALUES ("
						+ insurenquiry_id + ", "
						+ " 1,"
						+ " " + insurenquiry_entry_id + ","
						+ " '" + insurenquiry_entry_date + "',";
				if (veh_insfollowupby.equals("1")) {
					// If Insurance followup by is Sale date
					StrSql += " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + insurenquiry_sale_date + "'" + ","
							+ " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'100000'))";
				} else if (veh_insfollowupby.equals("2")) {
					// If Insurance followup by is Renewal date
					StrSql += " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + insurenquiry_renewal_date + "'" + ","
							+ " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'100000'))";
				}

				// //SOP("StrSql==Insert==Insur Followup==========" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				String insurpolicy_veh_id = "0";
				while (rs.next()) {
					insurpolicy_veh_id = rs.getString(1);
				}
				rs.close();
				insurpolicy_veh_id = CNumeric(insurpolicy_veh_id);

				if (!insurpolicy_veh_id.equals("0")) {
					followupcount++;
				}
			}

			conntx.commit();

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
		}
	}
	public String AddCustomer() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_mobile1,"
				+ " customer_mobile1_phonetype_id,"
				+ " customer_mobile2,"
				+ " customer_mobile2_phonetype_id,"
				+ " customer_phone1,"
				+ " customer_phone1_phonetype_id,"
				+ " customer_phone2,"
				+ " customer_phone2_phonetype_id,"
				+ " customer_email1,"
				+ " customer_email2,"
				+ " customer_address,"
				+ " customer_city_id,"
				+ " customer_pin,"
				+ " customer_soe_id,"
				+ " customer_sob_id,"
				+ " customer_since,"
				+ " customer_accgroup_id,"
				+ " customer_type,"
				+ " customer_active,"
				+ " customer_notes,"
				+ " customer_entry_id,"
				+ " customer_entry_date)"
				+ " VALUES"
				+ " (" + insurenquiry_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + contact_mobile1 + "',"
				+ " 4," // customer_mobile1_phonetype_id
				+ " '" + contact_mobile2 + "',"
				+ " 4," // customer_mobile1_phonetype_id
				+ " '" + contact_phone1 + "',"
				+ " 4," // customer_phone1_phonetype_id
				+ " '" + contact_phone2 + "',"
				+ " 4," // customer_phone2_phonetype_id
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " " + soe_id + ","
				+ " " + sob_id + ","
				+ " '" + ToShortDate(kknow()) + "',"
				+ " 32,"// customer_accgroup_id
				+ " 1," // customer_type
				+ " '1',"// customer_active
				+ " '',"
				+ " " + insurenquiry_entry_id + ","
				+ " '" + insurenquiry_entry_date + "')";
		// //SOP("StrSql==Customer==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			insurenquiry_customer_id = rs.getString(1);
		}
		rs.close();
		return insurenquiry_customer_id;
	}

	public String AddContact() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_contacttype_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_lname,"
				+ " contact_mobile1,"
				+ " contact_mobile1_phonetype_id,"
				+ " contact_mobile2,"
				+ " contact_mobile2_phonetype_id,"
				+ " contact_phone1,"
				+ " contact_phone1_phonetype_id,"
				+ " contact_phone2,"
				+ " contact_phone2_phonetype_id,"
				+ " contact_email1,"
				+ " contact_email2,"
				+ " contact_address,"
				+ " contact_city_id,"
				+ " contact_pin,"
				+ " contact_dob,"
				+ " contact_anniversary,"
				+ " contact_notes,"
				+ " contact_active,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + insurenquiry_customer_id + ","
				+ " 10," // contact_contacttype_id
				+ " " + contact_title_id + ","
				+ " '" + contact_fname + "',"
				+ " '" + contact_lname + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '4'," // Insurance contact_mobile1_phonetype_id
				+ " '" + contact_mobile2 + "',"
				+ " '4'," // Insurance contact_mobile1_phonetype_id
				+ " '" + contact_phone1 + "',"
				+ " '4'," // Insurance contact_phone1_phonetype_id
				+ " '" + contact_phone2 + "',"
				+ " '4'," // Insurance contact_phone2_phonetype_id
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " '" + contact_dob + "',"
				+ " '" + contact_anniversary + "',"
				+ " '',"
				+ " '1'," // contact_active
				+ " " + insurenquiry_entry_id + ","
				+ " '" + insurenquiry_entry_date + "')";
		// //SOP("StrSql==Customer Contact==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			insurenquiry_contact_id = rs.getString(1);
		}
		rs.close();
		return insurenquiry_contact_id;
	}

	public void AddEnquiry() throws SQLException {
		// SOP("veh_sale_date==9=");

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry"
				+ " (insurenquiry_branch_id,"
				+ " insurenquiry_date,"
				+ " insurenquiry_customer_id,"
				+ " insurenquiry_contact_id,"
				+ " insurenquiry_variant_id,"
				+ "	insurenquiry_variant,"
				+ " insurenquiry_modelyear,"
				+ " insurenquiry_chassis_no,"
				+ " insurenquiry_engine_no,"
				+ " insurenquiry_reg_no,"
				+ " insurenquiry_sale_date,"
				+ " insurenquiry_emp_id,"
				+ " insurenquiry_insurtype_id,"
				+ " insurenquiry_renewal_date,"
				+ " insurenquiry_previouscompname,"
				+ " insurenquiry_previousyearidv,"
				+ " insurenquiry_previousgrosspremium,"
				+ " insurenquiry_previousplanname,"
				+ " insurenquiry_policyexpirydate,"
				+ " insurenquiry_currentidv,"
				+ " insurenquiry_premium,"
				+ " insurenquiry_premiumwithzerodept,"
				+ " insurenquiry_compoffered,"
				+ " insurenquiry_plansuggested,"
				+ " insurenquiry_ncb,"
				+ "	insurenquiry_soe_id,"
				+ "	insurenquiry_sob_id,"
				+ " insurenquiry_campaign_id,"
				+ " insurenquiry_insurstage_id,"
				+ "	insurenquiry_insurstatus_id,"
				+ " insurenquiry_notes,"
				+ " insurenquiry_entry_id,"
				+ " insurenquiry_entry_date)"
				+ " VALUES"
				+ " (" + insurenquiry_branch_id + ","
				+ " '" + ToLongDate(kknow()) + "',"
				+ " " + insurenquiry_customer_id + ","
				+ " " + insurenquiry_contact_id + ","
				+ " " + insurenquiry_variant_id + ","
				+ "	'" + insurenquiry_variant + "',"
				+ " '" + insurenquiry_modelyear + "',"
				+ " '" + insurenquiry_chassis_no + "',"
				+ " '" + insurenquiry_engine_no + "',"
				+ " '" + insurenquiry_reg_no + "',"
				+ " '" + insurenquiry_sale_date + "',"
				+ " " + 0 + ","
				+ " " + insurenquiry_insurtype_id + ","
				+ " '" + insurenquiry_renewal_date + "',"
				+ " '" + insurenquiry_previouscompname + "',"
				+ " '" + insurenquiry_previousyearidv + "',"
				+ " '" + insurenquiry_previousgrosspremium + "',"
				+ " '" + insurenquiry_previousplanname + "',"
				+ " '" + insurenquiry_policyexpirydate + "',"
				+ " '" + insurenquiry_currentidv + "',"
				+ " '" + insurenquiry_premium + "',"
				+ " '" + insurenquiry_premiumwithzerodept + "',"
				+ " '" + insurenquiry_compoffered + "',"
				+ " '" + insurenquiry_plansuggested + "',"
				+ " '" + insurenquiry_ncb + "',"
				+ " " + soe_id + ","
				+ " " + sob_id + ","
				+ " " + insurenquiry_campaign_id + ","
				+ " 1,"
				+ " 1,"
				+ " '" + insurenquiry_notes + "',"
				+ " " + insurenquiry_entry_id + ","
				+ " '" + insurenquiry_entry_date + "')";
		// SOP("StrSql==Insurance Enquiry==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		conntx.commit();
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			insurenquiry_id = rs.getString(1);
		}
		rs.close();
		insurenquiry_id = CNumeric(insurenquiry_id);

		enquiryids += insurenquiry_id + ",";
		if (!insurenquiry_id.equals("0")) {
			propcount++;

		}
	}

	public void AddInsurFollowupFields() throws SQLException {
		try {
			if (enquiryids.contains(",")) {
				enquiryids = enquiryids.substring(0, enquiryids.length() - 1);
			}
			SOP("enquiryids=abc=" + enquiryids);
			SOP("empids=abc=" + empids);

			StrSql = "SELECT COALESCE(emp_id,0) AS emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id IN (" + empids + ")"
					+ " ORDER BY RAND()";
			// SOP("StrSql==emp==" + StrSql);
			CachedRowSet crs = processQuery(StrSql);

			StrSql = "SELECT insurenquiry_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " WHERE insurenquiry_id IN (" + enquiryids + ")";
			// SOP("StrSql==enq==" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql);

			if (crs.isBeforeFirst() && crs1.isBeforeFirst()) {
				while (crs.next() && crs1.next()) {
					followupcount++;
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_enquiry_followup"
							+ " (insurenquiryfollowup_insurenquiry_id,"
							+ " insurenquiryfollowup_followup_time,"
							+ " insurenquiryfollowup_entry_id,"
							+ " insurenquiryfollowup_entry_time)"
							+ " VALUES"
							+ " (" + crs1.getString("insurenquiry_id") + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql===insur followup==" + StrSql);
					updateQuery(StrSql);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET insurenquiry_emp_id = " + crs.getString("emp_id")
							+ " WHERE insurenquiry_id = " + crs1.getString("insurenquiry_id") + "";
					// SOP("strsql==update=veh=insur==" + StrSqlBreaker(StrSql));
					updateQuery(StrSql);
					if (crs.isLast()) {
						crs.beforeFirst();
					}
				}
			}
			crs.close();
			crs1.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateInsuranceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " ORDER BY insurtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(StrSelectdrop(crs.getString("insurtype_id"), insurenquiry_insurtype_id));
				Str.append(">").append(crs.getString("insurtype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsFollowup(String veh_insfollowupby, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"\">Select</option>");
			Str.append("<option value=\"1\" " + StrSelectdrop(veh_insfollowupby, "1") + ">Sale Date</option>");
			Str.append("<option value=\"2\" " + StrSelectdrop(veh_insfollowupby, "2") + ">Renewal Date</option>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
