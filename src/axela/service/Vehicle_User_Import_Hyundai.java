//Shivaprasad

package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Vehicle_User_Import_Hyundai extends Connect {

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
	public String str1[] = {"", "", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, followupcount = 0, updatecount = 0;
	public String upload = "";

	// Veh Data Membecrs....
	public String veh_id = "0";
	public String veh_branch_id = "0";
	public String veh_customer_id = "0", veh_contact_id = "0";
	public String veh_variant_id = "0", veh_modelyear = "";
	public String veh_comm_no = "", veh_chassis_no = "", veh_engine_no = "", veh_reg_no = "";
	public String veh_emp_id = "0", veh_emp_name = "";
	public String veh_sale_date = "", veh_vehsource_id = "0", vehsource_name = "", veh_kms = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0",
			veh_service_duedate = "", veh_service_duekms = "0", veh_insur_date = "";
	public String veh_crmemp_id = "0", veh_insuremp_id = "0", veh_renewal_date = "";
	public String soe_id = "0", sob_id = "0", soe_name = "", sob_name = "";
	public String veh_notes = "";
	public String veh_followup = "0";
	public String interior = "", exterior = "", item_name = "";
	public String veh_model_id = "0", model_brand_id = "0", item_model_id = "0", item_id = "0";
	public String model_name = "";
	public String option_id = "0", item_service_code = "";
	public String veh_entry_id = "";
	public String veh_entry_date = "";
	public String veh_insfollowupby = "";
	// Customer and Contact Data Membecrs....
	public String customer_name = "", status = "", technisian = "", contact_name = "", contact_title_id = "0", contact_fname = "", contact_lname = "", contact_fname_lname;
	public String contact_mobile1 = "", contact_mobile2 = "", contact_phone1 = "", contact_email1 = "", contact_email2 = "", contact_address = "", city_name = "", contact_city_id = "0",
			contact_pin = "";
	public String contact_dob = "", ro_date = "", contact_anniversary = "";
	// General Data Membecrs....
	public String error_msg = "";
	public String veh_error_msg = "", month = "", day = "", year = "", servicedueyear = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				veh_entry_id = emp_id;
				veh_entry_date = ToLongDate(kknow());
				// upload = PadQuotes(request.getParameter("add_button"));
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
				for (int i = 0; it.hasNext() && i < 10; i++) {
					FileItem button = (FileItem) it.next();
					// SOP("button===" + button);
					if (button.isFormField()) {
						// SOP("button.getString()===" + button.getString());
						str1[i] = button.getString();
					}
				}
				Iterator<?> iter = items.iterator();
				msg = "";
				int k = 0;// It represents to no. of elements selected in the form
				for (int i = 0; iter.hasNext() && i < 10; i++) {
					veh_branch_id = str1[k++];
					if (veh_branch_id.equals("")) {
						k--;
					}
					// SOP("veh_branch_id==" + veh_branch_id);
					veh_insuremp_id = CNumeric(PadQuotes(str1[k++]));
					if (veh_insuremp_id.equals("")) {
						k--;
					}
					// SOP("veh_insuremp_id==" + veh_insuremp_id);
					veh_crmemp_id = CNumeric(PadQuotes(str1[k++]));
					if (veh_crmemp_id.equals("")) {
						k--;
					}
					// SOP("veh_crmemp_id==" + veh_crmemp_id);
					veh_followup = CheckBoxValue(PadQuotes(str1[k++]));
					if (veh_followup.equals("") || veh_followup.equals("0")) {
						k--;
					}
					veh_insfollowupby = str1[k++];
					// SOP("veh_insfollowupby==" + veh_insfollowupby);
					k = 0;

					if (str1[i].equals("Upload")) {
						FileItem item = (FileItem) iter.next();
						// SOP("item===" + item);
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
										msg = "<br>" + propcount + " Vehicles imported successfully!";
										msg += "<br>" + updatecount + " Vehicles updated successfully!";
										msg += "<br>" + followupcount + " Followup updated successfully!" + "<br>" + errormsg + "";
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
			response.sendRedirect("../service/veh-user-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (veh_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (veh_insuremp_id.equals("0")) {
		// msg = msg + "<br>Select Insurance Executive!";
		// }
		// if (veh_crmemp_id.equals("0")) {
		// msg = msg + "<br>Select CRM Executive!";
		// }
		// if (veh_insfollowupby.equals("0")) {
		// msg = msg + "<br>Select Insurance Followup!";
		// }

		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
		// SOP("msg===" + msg);
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			SOP("coming");
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
			SOP("rowLength==========" + rowLength);
			if (rowLength > 500) {
				rowLength = 500;
			}
			int h = 0;
			int j = 0;
			int count = 0;
			propcount = 0;
			followupcount = 0;
			updatecount = 0;

			for (j = 1; j < rowLength + 1; j++) {
				// SOP("j==" + j);
				CheckForm();
				error_msg = "";
				for (h = 0; h < columnLength + 1; h++) {
					// Rodate---
					if (h == 0) {
						veh_lastservice = "";
						day = "";
						month = "";
						year = "";
						servicedueyear = "";
						veh_service_duekms = "0";
						veh_service_duedate = "";
						veh_lastservice = sheetData[j][h];
						if (veh_lastservice.equals("null")) {
							veh_lastservice = "";
						}
						// SOP("veh_lastservice==111=" + veh_lastservice);
						if (!veh_lastservice.equals("")) {
							day = "";
							month = "";
							year = "";
							if (!isValidDateFormatShort(veh_lastservice)) {
								// error_msg += " Invalid Date format for veh_lastservice date " + veh_lastservice + "<br>";
								veh_lastservice = "";
								// SOP("error_msg-----------------------------" + error_msg);
							}
							else if (isValidDateFormatShort(veh_lastservice)) {
								veh_lastservice = ConvertShortDateToStr(veh_lastservice);
								// SOP("veh_modelyear=direct==" + veh_modelyear);
							} else if (veh_lastservice.split("/").length == 3) {
								month = veh_lastservice.split("/")[0];
								if (month.length() == 1) {
									month = "0" + month;
								}
								day = veh_lastservice.split("/")[1];
								if (day.length() == 1) {
									day = "0" + day;
								}
								year = veh_lastservice.split("/")[2];

								if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
									// SOP("inside daymonthyear");
									veh_lastservice = year + month + day + "000000";
									// SOP("veh_modelyear=in1==" + veh_modelyear);
								}
							}
							// SOP("veh_sale_date=222==" + veh_sale_date);
						}
						if (!veh_lastservice.equals("")) {
							// if (isValidDateFormatShort(veh_lastservice)) {
							// veh_lastservice = ConvertShortDateToStr(veh_lastservice);
							// }
							if (isValidDateFormatLong(veh_lastservice)) {
								// SOP("date error=-");
								veh_lastservice = ConvertLongDateToStr(veh_lastservice);
								// SOP("veh_lastservice=1111==" + veh_lastservice);
							}
						} else {

							// veh_lastservice = veh_sale_date;
							if (veh_lastservice.equals("0"))
							{
								error_msg = "" + "<br> Purchase date is empty in your sheet";
							}
						}
						// SOP("veh_lastservice==new=" + veh_lastservice);
						if (!veh_lastservice.equals("")) {
							int duekms = 0;
							int duecount = 0;
							veh_service_duedate = strToLongDate(veh_lastservice);
							// SOP("veh_service_duedate==new=" + veh_service_duedate);
							day = veh_service_duedate.split("/")[0];
							month = veh_service_duedate.split("/")[1];
							year = veh_service_duedate.split("/")[2].substring(0, 4);
							// SOP("year===" + year);
							int i = Integer.parseInt(year);
							if (Long.parseLong(veh_lastservice.substring(1, 8)) > Long.parseLong(ToLongDate(kknow()).substring(1, 8))) {
								servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
							} else {
								servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
								// SOP("servicedueyear=11==" + servicedueyear);
								servicedueyear = (Integer.parseInt(servicedueyear) + 1) + "";
								// SOP("servicedueyear=22==" + servicedueyear);
							}
							while (i < Integer.parseInt(servicedueyear)) {
								if (duecount == 0) {
									duekms += Integer.parseInt(veh_lastservice_kms) + 10000;
								} else {
									duekms += 10000;
								}
								i++;
								duecount++;
							}
							if (duekms == 0) {
								duekms = Integer.parseInt(veh_lastservice_kms);
							}
							veh_service_duekms = duekms + "";
							veh_service_duedate = servicedueyear + month + day + "000000";
							// SOP("veh_service_duedate=new=" + veh_service_duedate);
							// SOP("veh_service_duekms=new=" + veh_service_duekms);
						}
					}

					// Status-----
					if (h == 1) {
						status = "";
						status = sheetData[j][h];
						if (status.equals("null")) {
							status = "";
						}
						// SOP("customer_name==" + customer_name);
					}

					// VinNumber---
					if (h == 2) {
						veh_chassis_no = "";
						veh_chassis_no = PadQuotes(sheetData[j][h]);
						if (veh_chassis_no.equals("null")) {
							veh_chassis_no = "";
						}
						if (veh_chassis_no.equals("")) {
							error_msg += " Chassis No. is empty!<br>";
						}

						// SOP("veh_chassis_no==" + veh_chassis_no);
					}

					// Regnumber-----
					if (h == 3) {
						veh_reg_no = "";
						veh_reg_no = PadQuotes(sheetData[j][h]);
						if (veh_reg_no.equals("null")) {
							veh_reg_no = "";
						}
						// SOP("veh_reg_no==" + veh_reg_no);
					}

					// Model-----
					if (h == 4) {
						veh_variant_id = "0";
						item_name = "";
						item_name = StringEscapeUtils.escapeHtml4((sheetData[j][h]));
						// SOP("item_name===" + item_name);
						if (item_name.contains("(")) {
							item_name = item_name.replace("(", "&#40;");
						}
						if (item_name.contains(")")) {
							item_name = item_name.replace(")", "&#41;");
						}
						SOP("item_name===" + item_name);
						if (item_name.equals("null") || item_name.equals("0")) {
							item_name = "";
						}

						if (!item_name.equals("")) {
							StrSql = "SELECT item_id, item_model_id, model_brand_id" + " "
									+ " FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
									+ " WHERE 1=1"
									+ " AND item_name ='" + item_name + "'"
									+ " AND branch_id= " + veh_branch_id
									+ " LIMIT 1";
							SOP("item id==========" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							if (crs.isBeforeFirst()) {
								while (crs.next()) {
									veh_variant_id = (crs.getString("item_id"));
									item_model_id = crs.getString("item_model_id");
									model_brand_id = crs.getString("model_brand_id");
								}
							}
							crs.close();
						}
						SOP("veh_variant_id==222==" + veh_variant_id);
						if (veh_variant_id.equals("0") || veh_variant_id.equals("") && !veh_chassis_no.equals("")) {
							error_msg += " Model/Item not present for Chassis No.==> " + veh_chassis_no + "<br>";
						}
					}

					// Mileage-----
					if (h == 5) {
						veh_kms = "";
						veh_kms = PadQuotes(sheetData[j][h]);
						if (veh_kms.equals("null")) {
							veh_kms = "";
						}
						// SOP("veh_reg_no==" + veh_reg_no);
					}

					// Sale Date
					if (h == 6) {
						veh_sale_date = "";
						day = "";
						month = "";
						year = "";
						veh_modelyear = "";
						veh_sale_date = sheetData[j][h];
						SOP("veh_sale_date==" + veh_sale_date);
						day = veh_sale_date.substring(0, 2);
						month = veh_sale_date.substring(2, 4);
						year = veh_sale_date.substring(4, 8);
						SOP("day--" + day + "\t" + "month--" + month + "\t" + "year---" + year);
						veh_sale_date = day + "/" + month + "/" + year;
						SOP("veh_sale_date---" + veh_sale_date);
						if (veh_sale_date.equals("null")) {
							veh_sale_date = "";

						}

						if (veh_sale_date.equals("") && veh_insfollowupby.equals("1")) {
							error_msg += " Sale date not present in the sheet!<br>";
						}
						if (!veh_sale_date.equals("")) {
							if (!isValidDateFormatShort(veh_sale_date) && veh_insfollowupby.equals("1")) {
								error_msg += " Invalid Date format for Sale date " + veh_sale_date + "<br>";
								veh_sale_date = "";
							} else if (isValidDateFormatShort(veh_sale_date)) {
								veh_sale_date = ConvertShortDateToStr(veh_sale_date);
								veh_modelyear = veh_sale_date.substring(0, 4);
								// SOP("veh_modelyear=direct==" + veh_modelyear);
							} else if (veh_sale_date.split("/").length == 3) {
								month = veh_sale_date.split("/")[0];
								if (month.length() == 1) {
									month = "0" + month;
								}
								day = veh_sale_date.split("/")[1];
								if (day.length() == 1) {
									day = "0" + day;
								}
								year = veh_sale_date.split("/")[2];

								if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
									// SOP("inside daymonthyear");
									veh_sale_date = year + month + day + "000000";
									veh_modelyear = year;
									// SOP("veh_modelyear=in1==" + veh_modelyear);
								} else {
									veh_sale_date = "";
								}
							} else {
								veh_sale_date = "";
							}
						} else {
							// SOP("veh_modelyear=in4==" + veh_modelyear);
						}
						// SOP("veh_sale_date=new==" + veh_sale_date);
					}

					// contact name------
					if (h == 7) {
						contact_name = "";
						contact_name = PadQuotes(sheetData[j][h]);
						contact_fname = "";
						contact_lname = "";
						// SOP("contact_name==111==" + contact_name);
						if (contact_name.equals("null")) {
							contact_name = "";
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
							// if (customer_name.equals("")
							// || customer_name.equalsIgnoreCase("OTHERS")
							// || customer_name.equalsIgnoreCase("NOT AVAILABLE")) {
							// customer_name = contact_name;
							// }
							// SOP("contact_name==222==" + contact_name);
						}
					}

					// Address---------
					if (h == 8) {
						contact_address = "";
						contact_address = PadQuotes(sheetData[j][h]);
						if (contact_address.equals("null") || contact_address.equals("N/A")) {
							contact_address = "";
						}
						// SOP("contact_address==" + contact_address);
					}

					// Email---------
					if (h == 9) {
						contact_email1 = "";
						contact_email1 = PadQuotes(sheetData[j][h]);
						if (contact_email1.equals("null")) {
							contact_email1 = "";
						}
						if (!IsValidEmail(contact_email1)) {
							contact_email1 = "";
						}
						// SOP("contact_email1==" + contact_email1);
					}

					// City-----
					if (h == 10) {
						city_name = "";
						contact_city_id = "0";
						city_name = PadQuotes(sheetData[j][h]);
						if (city_name.equals("null")) {
							city_name = "";
						}
						// SOP("city_name==" + city_name);
						if (!contact_city_id.equals("")) {
							contact_city_id = CNumeric(ExecuteQuery("SELECT city_id FROM " + compdb(comp_id) + "axela_city"
									+ " WHERE city_name = '" + city_name + "'"));
						}
						if (CNumeric(contact_city_id).equals("0")) {
							contact_city_id = CNumeric(ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = " + veh_branch_id + ""
									+ " LIMIT 1"));
						}
						// SOP("contact_city_id==" + contact_city_id);
					}

					// Pincode---------
					if (h == 11) {
						contact_pin = "";
						contact_pin = sheetData[j][h];
						// SOP("contact_pin==" + contact_pin);
						if (contact_pin.equals("null") || contact_pin.equals("N/A")) {
							contact_pin = "";
						}
						if (contact_pin.equals("")) {
							contact_pin = PadQuotes(ExecuteQuery("SELECT branch_pin FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = " + veh_branch_id + ""));
						}
					}

					// Contact mobile----
					if (h == 12) {
						contact_mobile1 = "";
						contact_mobile1 = PadQuotes(sheetData[j][h]);
						if (contact_mobile1.equals("null")) {
							contact_mobile1 = "";
						}
						if (!contact_mobile1.equals("")) {
							if (contact_mobile1.length() > 10 || contact_mobile1.length() < 10) {
								error_msg += " Mobile1 can't be greater than 10 digits!<br>";
								contact_mobile1 = "";
							}
						}
						// SOP("contact_mobile1==" + contact_mobile1);
					}

					// Residence======
					if (h == 13) {
						contact_phone1 = "";
						contact_phone1 = PadQuotes(sheetData[j][h]);
						if (contact_phone1.equals("null")) {
							contact_phone1 = "";
						}
						if (!contact_phone1.equals("")) {
							if (contact_phone1.length() > 10 || contact_phone1.length() < 10) {
								error_msg += " Phone no can't be greater than 10 digits!<br>";
								contact_phone1 = "";
							}
						}
						// SOP("contact_mobile1==" + contact_mobile1);
					}

					// Office-------
					if (h == 14) {
						contact_mobile2 = "";
						contact_mobile2 = PadQuotes(sheetData[j][h]);
						if (contact_mobile2.equals("null")) {
							contact_mobile2 = "";
						}
						if (!contact_mobile2.equals("")) {
							if (contact_mobile2.length() > 10 || contact_mobile2.length() < 10) {
								error_msg += " office no can't be greater than 10 digits!<br>";
								contact_mobile2 = "";
							}
						}
						// SOP("contact_mobile1==" + contact_mobile1);
					}

					// advisor======
					if (h == 15) {
						veh_emp_name = PadQuotes(sheetData[j][h]);
					}

					// technisian=======
					if (h == 16) {
						technisian = PadQuotes(sheetData[j][h]);
					}

					// Service Code==
					if (h == 17) {

					}

				}
				// ===============================================================================

				veh_id = "0";
				veh_contact_id = "0";
				veh_customer_id = "0";
				// SOP("veh_reg_no=111=" + veh_reg_no);
				// if (veh_reg_no.equals("") && veh_variant_id.equals("0")) {
				// count++;
				// // error_msg += (count + "") + ".";
				// error_msg += " " + "Vehicle Reg.No. is empty!";
				// // SOP("error_msg===" + error_msg);
				// }
				// if (veh_chassis_no.equals("") || veh_variant_id.equals("0")) {
				// count++;
				// // error_msg += (count + "") + ".";
				// error_msg += " " + "Chassis No. is empty!";
				// // SOP("error_msg===" + error_msg);
				// }
				// SOP("veh_engine_no---" + veh_engine_no + "" + "veh_chassis_no---" + veh_chassis_no);
				if (veh_chassis_no.equals(""))
				{
					errormsg += "<br>" + "Engine No is empty!" + " " + " for this customer-->" + customer_name;
					// SOP("error_msg------------" + error_msg);
					continue;
				}
				count++;
				if (!veh_chassis_no.equals("") && !veh_variant_id.equals("0")) {
					// SOP("1st condition---" + veh_variant_id);

					CheckForm();
					if (msg.equals("") && error_msg.equals("")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();

							StrSql = "SELECT veh_id, veh_customer_id, veh_contact_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE"
									+ " veh_chassis_no = '" + veh_chassis_no + "'";
							SOP("StrSql=present veh_id==" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							if (crs.isBeforeFirst()) {
								while (crs.next()) {
									veh_id = CNumeric(crs.getString("veh_id"));
									veh_customer_id = CNumeric(crs.getString("veh_customer_id"));
									veh_contact_id = CNumeric(crs.getString("veh_contact_id"));
								}
							}
							crs.close();
							// if (!veh_id.equals("0")) {
							// // SOP("veh_id ==already present = " + veh_id);
							// }
							// SOP("veh_id===" + veh_id);
							if (veh_id.equals("0")) {
								// SOP("veh_variant_id===" + veh_variant_id);
								String veh_chassis_no_check = "";
								String veh_engine_no_check = "";
								// veh_reg_no_check = ExecuteQuery("SELECT veh_reg_no  FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_reg_no = '" + veh_reg_no + "' ");
								veh_chassis_no_check = ExecuteQuery("SELECT veh_chassis_no FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_chassis_no = '" + veh_chassis_no + "' ");
								// if (veh_reg_no_check.equals("")) {
								// error_msg += " Registration No. already present " + veh_reg_no + "<br>";
								// }

								// StrSql = "SELECT veh_chassis_no, veh_engine_no FROM " + compdb(comp_id) + "axela_service_veh"
								// + " WHERE CONCAT(veh_chassis_no, '-', veh_engine_no) = '" + veh_chassis_no + "-" + veh_engine_no + "'";
								// SOP("StrSql---------------concat---------" + StrSql);
								// CachedRowSet crs1 = processQuery(StrSql, 0);
								// if (crs.isBeforeFirst()) {
								// while (crs1.next()) {
								// veh_chassis_no_check = crs.getString("veh_chassis_no");
								// veh_engine_no_check = crs.getString("veh_engine_no");
								// }
								// }
								// crs1.close();
								if (veh_chassis_no_check.equals(veh_chassis_no)) {
									error_msg += " Chassis No. already present " + veh_chassis_no + "<br>";
								}
								// SOP("veh_chassis_no--" + veh_chassis_no + "\t" + "veh_engine_no--" + veh_engine_no + "\t" + "veh_reg_no--" + veh_reg_no);
								// SOP("veh_engine_no_check--" + veh_engine_no_check + "\t" + "veh_chassis_no_check--" + veh_chassis_no_check);
								if (!veh_chassis_no.equals("") || !veh_reg_no.equals("")) {
									if (!veh_variant_id.equals("0") && veh_chassis_no_check.equals("")) {
										if (veh_contact_id.equals("0")) {
											if (CNumeric(veh_customer_id).equals("0")) {
												veh_customer_id = AddCustomer();
											}
											if (CNumeric(veh_contact_id).equals("0")) {
												veh_contact_id = AddContact();
											}
										}
										// SOP("veh_contact_id==" + veh_contact_id + " veh_customer_id==" + veh_customer_id);

										if (!CNumeric(veh_contact_id).equals("0") && !CNumeric(veh_customer_id).equals("0")) {
											AddVehicle();
										}

									}
								}// End Scope of veh_variant_id !=0
								else {
									// StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
									// + " SET imp_active = '2'"
									// + " WHERE imp_id = " + imp_id + "";
									// updateQuery(StrSql);
								}
							} else {
								// For updating Customer Details for existing vehicles...

								// For updating Contact Details for existing vehicles...

								// For updating vehicle info
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET ";
								if (!veh_branch_id.equals("0")) {
									StrSql += " veh_branch_id = " + veh_branch_id + ",";
								}

								StrSql += " veh_insuremp_id = " + veh_insuremp_id + ",";

								StrSql += " veh_crmemp_id = " + veh_crmemp_id + ",";

								if (!veh_sale_date.equals("0") && !veh_sale_date.equals("") && veh_insfollowupby.equals("1")) {
									StrSql += " veh_sale_date = " + veh_sale_date + ",";
								}
								if (!veh_renewal_date.equals("0") && !veh_renewal_date.equals("") && veh_insfollowupby.equals("2")) {
									StrSql += " veh_renewal_date = " + veh_renewal_date + ",";
								}
								if (!veh_customer_id.equals("0")) {
									StrSql += " veh_customer_id = " + veh_customer_id + ",";
								}
								if (!veh_contact_id.equals("0")) {
									StrSql += " veh_contact_id = " + veh_contact_id + ",";
								}
								if (!veh_chassis_no.equals("")) {
									StrSql += " veh_chassis_no = '" + veh_chassis_no + "',";
								}
								if (!veh_engine_no.equals("")) {
									StrSql += " veh_engine_no = '" + veh_engine_no + "',";
								}
								if (!veh_kms.equals("0")) {
									StrSql += " veh_kms = " + CNumeric(veh_kms) + ",";
								}
								if (!veh_lastservice.equals("")) {
									StrSql += " veh_lastservice = '" + veh_lastservice + "',";
								}
								if (!veh_lastservice_kms.equals("0")) {
									StrSql += " veh_lastservice_kms = " + veh_lastservice_kms + ",";
								}
								if (!veh_service_duedate.equals("")) {
									StrSql += " veh_service_duedate = '" + veh_service_duedate + "',";
								}
								if (!veh_service_duekms.equals("0")) {
									StrSql += " veh_service_duekms = " + veh_service_duekms + ",";
								}
								if (!veh_emp_id.equals("0")) {
									StrSql += " veh_emp_id = " + veh_emp_id + ",";
								}
								StrSql = StrSql.substring(0, StrSql.length() - 1);
								StrSql += " WHERE veh_id =" + veh_id;
								SOP("strsql===========update====vehicle=veh===" + StrSql);
								stmttx.execute(StrSql);

								// For inserting the recent kms of the vehicle into veh_kms table
								if (!veh_kms.equals("0") || veh_kms.equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
											+ " (vehkms_veh_id,"
											+ " vehkms_kms,"
											+ " vehkms_entry_id,"
											+ " vehkms_entry_date)"
											+ " VALUES"
											+ " (" + veh_id + ","
											+ " " + CNumeric(veh_kms) + ","
											+ " " + veh_entry_id + ","
											+ " '" + veh_lastservice + "')";
									SOP("strsql===if===veh kms===" + StrSqlBreaker(StrSql));
									stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
									ResultSet rs1 = stmttx.getGeneratedKeys();
									String vehkms_id = "0";
									while (rs1.next()) {
										vehkms_id = rs1.getString(1);
									}
									rs1.close();
									vehkms_id = CNumeric(vehkms_id);

									if (!vehkms_id.equals("0")) {
										updatecount++;
									}

								}

								// For inserting Insurance followup of the vehicle into insur_followup table
								if (veh_insfollowupby.equals("1") && !veh_sale_date.equals("") || veh_insfollowupby.equals("2") && !veh_renewal_date.equals("")) {
									// Delete the existing followups whose desc is empty
									StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup"
											+ "	WHERE insurfollowup_desc = ''"
											+ "	AND insurfollowup_veh_id = " + veh_id;
									SOP("StrSql==Delete==Insur Followup==" + StrSql);
									stmttx.execute(StrSql);

									// Inserting the new followups based on Sale date or Renewal date

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
											+ " (insurfollowup_veh_id,"
											+ " insurfollowup_emp_id,"
											+ " insurfollowup_followuptype_id,"
											+ " insurfollowup_entry_id,"
											+ " insurfollowup_entry_time,"
											+ " insurfollowup_followup_time)"
											+ " VALUES ("
											+ veh_id + ", "
											+ veh_insuremp_id + ","
											+ " 1,"
											+ " " + veh_entry_id + ","
											+ " '" + veh_entry_date + "',";
									if (veh_insfollowupby.equals("1")) {
										// If Insurance followup by is Sale date
										StrSql += " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + veh_sale_date + "'" + ","
												+ " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'1000000'))";
									} else if (veh_insfollowupby.equals("2")) {
										// If Insurance followup by is Renewal date
										StrSql += " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + veh_renewal_date + "'" + ","
												+ " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'100000'))";
									}

									SOP("StrSql==Insert==Insur Followup==========" + StrSql);
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

							}
						} catch (Exception e) {
							if (conntx.isClosed()) {
								SOPError("connection is closed.....");
							}
							if (!conntx.isClosed() && conntx != null) {
								conntx.rollback();
							}
							msg = "<br>Transaction Error!";
							SOPError("Axelaauto== "
									+ this.getClass().getName());
							SOPError("Error in "
									+ new Exception().getStackTrace()[0]
											.getMethodName() + ": " + e);
						} finally {
							conntx.setAutoCommit(true);
							stmttx.close();
							if (conntx != null && !conntx.isClosed()) {
								conntx.close();
							}
						}
					}
				}
				if (!error_msg.equals("")) {
					veh_error_msg += " <br> Please correct following errors and upload again.!";
					if (!veh_reg_no.equals("")) {
						veh_error_msg += "<br><br>" + count + "." + " Registration No.===>" + veh_reg_no + "<br>";
					} else if (veh_reg_no.equals("") && !customer_name.equals("")) {
						veh_error_msg += "<br>" + count + "." + " Customer===>" + customer_name + "<br>";
					}
					// SOP("error_msg------------" + error_msg);
					veh_error_msg += error_msg;
				}
			}

		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateBranch(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id!=0"
					+ " AND branch_branchtype_id IN (1,3)"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"), veh_branch_id));
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

			Str.append("<option value=\"0\">Service Advisor</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), veh_insuremp_id));
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

	public String AddCustomer() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_mobile1,"
				+ " customer_mobile2,"
				+ " customer_email1,"
				+ " customer_email2,"
				+ " customer_address,"
				+ " customer_city_id,"
				+ " customer_pin,"
				+ " customer_soe_id,"
				+ " customer_sob_id,"
				+ " customer_since,"
				+ " customer_active,"
				+ " customer_notes,"
				+ " customer_entry_id,"
				+ " customer_entry_date)"
				+ " VALUES"
				+ " (" + veh_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '" + contact_mobile2 + "',"
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " " + soe_id + ","
				+ " " + sob_id + ","
				+ " '" + ToShortDate(kknow()) + "',"
				+ " '1',"
				+ " '',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		SOP("StrSql==INSERT INTO customer==" + StrSql);
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();
		while (rs.next()) {
			veh_customer_id = rs.getString(1);
		}
		rs.close();
		return veh_customer_id;
	}

	public String AddContact() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_contacttype_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_lname,"
				+ " contact_mobile1,"
				+ " contact_mobile2,"
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
				+ " (" + veh_customer_id + ","
				+ " 1,"
				+ " " + contact_title_id + ","
				+ " '" + contact_fname + "',"
				+ " '" + contact_lname + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '" + contact_mobile2 + "',"
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " '" + contact_dob + "',"
				+ " '" + contact_anniversary + "',"
				+ " '',"
				+ " '1',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		SOP("StrSql==INSERT INTO axela_customer_contact==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_contact_id = rs.getString(1);
		}
		rs.close();
		return veh_contact_id;
	}

	public void AddVehicle() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
				+ " (veh_branch_id,"
				+ " veh_customer_id,"
				+ " veh_contact_id,"
				+ " veh_so_id,"
				+ " veh_variant_id,"
				+ " veh_modelyear,"
				+ " veh_comm_no,"
				+ " veh_chassis_no,"
				+ " veh_engine_no,"
				+ " veh_reg_no,"
				+ " veh_sale_date,"
				+ " veh_emp_id,"
				+ " veh_vehsource_id,"
				+ " veh_kms,"
				+ " veh_lastservice,"
				+ " veh_lastservice_kms,"
				+ " veh_service_duedate,"
				+ " veh_service_duekms,"
				+ " veh_iacs,"
				+ " veh_crmemp_id,"
				+ " veh_insuremp_id,"
				+ " veh_renewal_date,"
				+ " veh_notes,"
				+ " veh_entry_id,"
				+ " veh_entry_date)"
				+ " VALUES"
				+ " (" + veh_branch_id + ","
				+ " " + veh_customer_id + ","
				+ " " + veh_contact_id + ","
				+ " 0,"
				+ " " + veh_variant_id + ","
				+ " '" + veh_modelyear + "',"
				+ " " + CNumeric(veh_comm_no) + ","
				+ " '" + veh_chassis_no + "',"
				+ " '" + veh_engine_no + "',"
				+ " '" + veh_reg_no + "',"
				+ " '" + veh_sale_date + "',"
				+ " " + veh_emp_id + ","
				+ " " + veh_vehsource_id + ","
				+ " " + CNumeric(veh_kms) + ","
				+ " '" + veh_lastservice + "',"
				+ " " + CNumeric(veh_lastservice_kms) + ","
				+ " '" + veh_service_duedate + "',"
				+ " " + CNumeric(veh_service_duekms) + ","
				+ " 0,"
				+ " " + veh_crmemp_id + ","
				+ " " + veh_insuremp_id + ","
				+ " '" + veh_renewal_date + "',"
				+ " '" + veh_notes + "',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		SOP("StrSql==insert==axela_service_veh==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_id = rs.getString(1);
		}
		rs.close();
		veh_id = CNumeric(veh_id);

		if (!veh_id.equals("0")) {
			propcount++;
			// SOP("propcount===" + propcount);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
					+ " (vehkms_veh_id,"
					+ " vehkms_kms,"
					+ " vehkms_entry_id,"
					+ " vehkms_entry_date)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + CNumeric(veh_kms) + ","
					+ " " + veh_entry_id + ","
					+ " '" + veh_lastservice + "')";
			// SOP("INSERT==axela_service_veh_kms==" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);

			if (!veh_insuremp_id.equals("0")) {
				if (veh_insfollowupby.equals("1") && !veh_sale_date.equals("")) {
					AddInsurFollowupFields(veh_id, veh_sale_date, veh_insuremp_id);
				} else if (veh_insfollowupby.equals("2") && !veh_renewal_date.equals("")) {
					AddInsurFollowupFields(veh_id, veh_renewal_date, veh_insuremp_id);
				}
			}

			if (veh_followup.equals("1")) {
				String default_mileage = "30";

				StrSql = "UPDATE "
						+ compdb(comp_id)
						+ "axela_service_veh v1"
						+ " INNER JOIN (SELECT veh_id, calkms"
						+ " FROM (SELECT veh_id, @default_mileage:="
						+ default_mileage
						+ ","
						+ " @kmscount:=COALESCE((SELECT COUNT(vehkms_id)"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id), '0') AS kmscount,"
						+ " @date1:=COALESCE((SELECT vehkms_entry_date"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS date1,"
						+ " @kms1:=COALESCE((SELECT vehkms_kms"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1), '') AS kms1,"
						+ " @date2:=COALESCE((SELECT vehkms_entry_date"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS date2,"
						+ " @kms2:=COALESCE((SELECT vehkms_kms"
						+ " FROM "
						+ compdb(comp_id)
						+ "axela_service_veh_kms"
						+ " WHERE vehkms_veh_id = veh_id"
						+ " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS kms2,"
						+ " IF(@kmscount>1, COALESCE((veh_kms + ((@kms1 - @kms2)/DATEDIFF(@date1, @date2))*DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms + (@kms1-@kms2))),"
						+ " (IF(@kmscount=1, COALESCE((veh_kms + @default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms+@default_mileage)),"
						+ " COALESCE(@default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', veh_sale_date),@default_mileage))))"
						+ " AS calkms,"
						+ " IF (@kmscount > 1,"
						+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / ((@kms1 - @kms2) / DATEDIFF(@date1, @date2)) ,0),"
						+ "	(IF(@kmscount = 1,"
						+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0),"
						+ " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0)))"
						+ " ) AS calservicedate,"
						+ " IF(@kmscount > 1, COALESCE(@date1, ''), (IF(@kmscount = 1, COALESCE(@date1, ''), veh_sale_date )))"
						+ " AS lastservice_date"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE 1=1";
				// + " AND veh_vehsource_id = 2";
				StrSql += " AND veh_id = " + veh_id;
				StrSql += " GROUP BY veh_id"
						+ " ORDER BY veh_id) Sat) v2"
						+ " SET"
						+ " v1.veh_cal_kms = v2.calkms,"
						+ " v1.veh_calservicedate = DATE_FORMAT(DATE_ADD(veh_lastservice,INTERVAL IF(@calserviceday< 365, @calserviceday, 365) DAY),'%Y%m%d%h%i%s')"
						+ " WHERE v1.veh_id = v2.veh_id";
				// SOP("StrSql====vehicle kms update==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
						+ " (vehfollowup_veh_id,"
						+ " vehfollowup_emp_id,"
						+ " vehfollowup_followup_time)"
						+ " VALUES (" + veh_id + ", " + veh_crmemp_id + ","
						+ " '20151027110000')";

				SOP("strsql====add veh followup=" + StrSql);
				stmttx.execute(StrSql);
			}
		}

		// SOP("StrSql===veh_id==" + veh_id);

		if (!veh_id.equals("0")) {
			option_id = "0";
			if (!model_brand_id.equals("0") && !interior.equals("")) {
				StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
						+ " WHERE option_name = '" + interior + "'"
						+ " AND option_brand_id = " + model_brand_id;
				option_id = CNumeric(ExecuteQuery(StrSql));
			}

			if (!option_id.equals("0")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
						+ " (vehtrans_option_id,"
						+ " vehtrans_veh_id)"
						+ " VALUES"
						+ " (" + option_id + ","
						+ " " + veh_id + ")";
				// SOP("StrSql=interior=" + StrSql);
				stmttx.execute(StrSql);
			}

			// SOP("interior==" + interior + " exterior==" + exterior);
			if (!interior.equals(exterior)) {
				option_id = "0";
				if (!model_brand_id.equals("0") && !exterior.equals("")) {
					StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
							+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
							+ " WHERE option_name = '" + exterior + "'"
							+ " AND option_brand_id = " + model_brand_id;
					// SOP("StrSql===exterior===" + StrSql);
					option_id = CNumeric(ExecuteQuery(StrSql));
				}

				if (!option_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
							+ " (vehtrans_option_id,"
							+ " vehtrans_veh_id)"
							+ " VALUES"
							+ " (" + option_id + ","
							+ " " + veh_id + ")";
					// SOP("StrSql=insert==axela_service_veh_option_trans==" + StrSql);
					stmttx.execute(StrSql);
				}
			}
		}

		conntx.commit();

	}
	public void AddInsurFollowupFields(String veh_id, String veh_ins_date,
			String veh_insuremp_id) throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id)
					+ "axela_insurance_followup"
					+ " (insurfollowup_veh_id,"
					+ " insurfollowup_emp_id,"
					+ " insurfollowup_followup_time,"
					+ " insurfollowup_followuptype_id,"
					+ " insurfollowup_priorityinsurfollowup_id,"
					+ " insurfollowup_desc,"
					+ " insurfollowup_entry_id,"
					+ " insurfollowup_entry_time,"
					+ " insurfollowup_trigger)"
					+ " VALUES" + " ("
					+ veh_id + ","
					+ " " + veh_insuremp_id + ","
					+ " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + veh_ins_date + "'" + ","
					+ " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'100000'),"
					// + " DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 11 MONTH), '%Y%m%d%h%i%s'),"
					+ " 1,"
					+ " 1,"
					+ " '',"
					+ " " + veh_entry_id + ","
					+ " '" + veh_entry_date + "',"
					+ " 0)";
			SOP("StrSql===insur followup==" + StrSql);
			stmttx.execute(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh" + " SET"
					+ " veh_insuremp_id = " + veh_insuremp_id + ""
					+ " WHERE veh_id = " + veh_id + "";
			SOP("strsql==update=veh=insur==" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
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
	public String PopulateCRMExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_crm = 1"
					+ " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Service Advisor</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), veh_crmemp_id));
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
}
