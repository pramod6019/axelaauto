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

public class Vehicle_User_Import extends Connect {

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
	public String str1[] = {"", "", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, followupcount = 0, updatecount = 0;
	public String upload = "";

	// Veh Data Membecrs....
	public String veh_id = "0";
	public String veh_branch_id = "0";
	public String veh_customer_id = "0", veh_contact_id = "0";
	public String veh_variant_id = "0", veh_modelyear = "";
	public String veh_comm_no = "", veh_chassis_no = "", veh_reg_no_check = "", veh_chassis_no_check = "", veh_engine_no = "", veh_reg_no = "";
	public String veh_emp_id = "0", veh_emp_name = "";
	public String veh_sale_date = "", veh_vehsource_id = "0", vehsource_name = "", veh_kms = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0",
			veh_service_duedate = "", veh_service_duekms = "0", veh_insur_date = "";
	public String veh_crmemp_id = "0", veh_insuremp_id = "0", veh_renewal_date = "";
	public String soe_id = "0", sob_id = "0", soe_name = "", sob_name = "";
	public String veh_notes = "";
	public String BranchAccess = "";
	public String veh_followup = "0";
	public String interior = "", exterior = "", item_name = "";
	public String veh_model_id = "0", model_brand_id = "0", item_model_id = "0", item_id = "0", item_itemid = "";
	public String model_name = "";
	public String option_id = "0", item_service_code = "", service_itemid = "";
	public String veh_entry_id = "";
	public String veh_entry_date = "";
	public String veh_insfollowupby = "";
	// Customer and Contact Data Membecrs....
	public String customer_name = "", contact_name = "", contact_title_id = "0", contact_fname = "", contact_lname = "", contact_fname_lname;
	public String contact_mobile1 = "", contact_mobile2 = "", contact_email1 = "", contact_email2 = "", contact_address = "", city_name = "", contact_city_id = "0", contact_pin = "";
	public String contact_dob = "", contact_anniversary = "";
	// General Data Membecrs....
	public String error_msg = "", status = "";
	public String veh_error_msg = "", month = "", day = "", year = "", servicedueyear = "";

	public String vehid = "0", chassisid = "0";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				veh_entry_id = emp_id;
				veh_entry_date = ToLongDate(kknow());
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
				for (int i = 0; it.hasNext() && i < 10; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
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
					veh_insuremp_id = CNumeric(PadQuotes(str1[k++]));
					if (veh_insuremp_id.equals("")) {
						k--;
					}
					veh_crmemp_id = CNumeric(PadQuotes(str1[k++]));
					if (veh_crmemp_id.equals("")) {
						k--;
					}
					veh_followup = CheckBoxValue(PadQuotes(str1[k++]));
					if (veh_followup.equals("") || veh_followup.equals("0")) {
						k--;
					}
					veh_insfollowupby = str1[k++];
					k = 0;

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
										msg += "<br>Document Contents Not In Order!";
										String fileName1 = VehicleImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = "<br>" + propcount + " Vehicles Imported Successfully!";
										msg += "<br>" + updatecount + " Vehicles Updated Successfully!";
										msg += "<br>" + followupcount + " Followup Updated Successfully!";
										if (!veh_error_msg.equals("")) {
											msg += "<br><br>" + "Please Rectify The Following Errors And Import Again! <br> " + veh_error_msg;
										}
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
			msg = "Uploaded File Size Is Large!";
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
			int rowLength = 0;
			int columnLength = 0;
			int vehicleColumnLength = 27;
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
			if (rowLength > 5000) {
				rowLength = 5000;
			}
			int h = 0;
			int j = 0;
			int count = 0;
			propcount = 0;
			followupcount = 0;
			updatecount = 0;
			if (vehicleColumnLength != columnLength) {
				msg += "<br> Document Columns Doesn't Match With The Template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					CheckForm();
					error_msg = "";
					status = "";
					for (h = 0; h < columnLength + 1; h++) {
						if (h == 0) {
							customer_name = "";
							customer_name = PadQuotes(sheetData[j][h]);
							if (customer_name.equals("null")) {
								customer_name = "";
							}
						}

						if (h == 1) {
							contact_name = "";
							contact_name = PadQuotes(sheetData[j][h]);
							contact_fname = "";
							contact_lname = "";
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
							}
						}

						if (h == 2) {
							contact_mobile1 = "";
							contact_mobile1 = PadQuotes(sheetData[j][h]);
							if (contact_mobile1.equals("")) {
								error_msg += "Mobile Number Should Not Be Empty" + "<br>";
							}
							else {
								contact_mobile1 = validcontact(contact_mobile1);
								if (contact_mobile1.equals("")) {
									error_msg += " Please Enter Valid Mobile1! <br/> ";
								}
							}
							SOP("contact_mobile1==" + contact_mobile1);
						}

						if (h == 3) {
							contact_mobile2 = "";
							contact_mobile2 = PadQuotes(sheetData[j][h]);
							if (contact_mobile2.equals("")) {
								contact_mobile2 = "";
							}
							else {
								contact_mobile2 = validcontact(contact_mobile2);
								if (contact_mobile2.equals("")) {
									error_msg += " Please Enter Valid Mobile2! <br/> ";
								}
							}
							SOP("contact_mobile2==" + contact_mobile2);
						}

						if (h == 4) {
							contact_email1 = "";
							contact_email1 = PadQuotes(sheetData[j][h]);
							if (contact_email1.equals("null")) {
								contact_email1 = "";
							}
							if (!IsValidEmail(contact_email1)) {
								contact_email1 = "";
							}
						}
						if (h == 5) {
							contact_email2 = "";
							contact_email2 = PadQuotes(sheetData[j][h]);
							if (contact_email2.equals("null")) {
								contact_email2 = "";
							}
							if (!IsValidEmail(contact_email2)) {
								contact_email2 = "";
							}
						}
						if (h == 6) {
							contact_address = "";
							contact_address = PadQuotes(sheetData[j][h]);
							if (contact_address.equals("null") || contact_address.equals("N/A")) {
								contact_address = "";
							}
							if (contact_address.equals("")) {
								error_msg += "Address Is Empty!<br/>";
							}
						}
						if (h == 7) {
							city_name = "";
							contact_city_id = "0";
							city_name = PadQuotes(sheetData[j][h]);
							if (city_name.equals("null")) {
								city_name = "";
							}
							if (!contact_city_id.equals("")) {
								contact_city_id = CNumeric(ExecuteQuery("SELECT city_id FROM " + compdb(comp_id) + "axela_city"
										+ " WHERE city_name = '" + city_name + "'"));
								if (contact_city_id.equals("0")) {
									error_msg += "Invalid City Name!<br/>";
								}
							}

						}
						if (h == 8) {
							contact_pin = "";
							contact_pin = (sheetData[j][h]);
							if (contact_pin.equals("") || contact_pin.equals("null") || contact_pin.equals("N/A")) {
								contact_pin = "";
							} else {
								if (contact_pin.length() == 6) {
									if (isNumeric(contact_pin) != true) {
										error_msg += "Invalid Pin!<br/>";
									}
								} else {
									error_msg += "Invalid Pin!<br/>";
								}
							}
						}

						if (h == 9) {
							veh_sale_date = "";
							// veh_modelyear = "";??COMMENT FOR INDEL
							veh_sale_date = PadQuotes(sheetData[j][h]);
							if (veh_sale_date.equals("null")) {
								veh_sale_date = "";
							}
							if (veh_sale_date.equals("")) {
								error_msg += " Sale Date Not Present In The Sheet!<br>";
							} else {
								veh_sale_date = datevalidate(veh_sale_date);
								if (veh_sale_date.equals("")) {
									error_msg += "Invalid Sale Date ! <br>";
									veh_sale_date = "";
								}
							}
						}

						if (h == 10) {
							veh_variant_id = "0";
							item_name = "";
							item_name = PadQuotes(sheetData[j][h]);
							item_itemid = "";
							if (item_name.contains("(")) {
								item_name = item_name.replace("(", "&#40;");
							}
							if (item_name.contains(")")) {
								item_name = item_name.replace(")", "&#41;");
							}
							if (item_name.equals("null") || item_name.equals("0")) {
								item_name = "";
							}
							else {
								item_itemid = CNumeric(ExecuteQuery("SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_name='" + item_name + "'"));
							}
							if (!item_itemid.equals("0")) {
								StrSql = "SELECT item_id, item_model_id, model_brand_id" + " "
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
										+ " WHERE 1=1"
										+ " AND item_name = " + "'" + item_name + "'"
										+ " OR item_code = " + "'" + item_name + "'"
										+ " OR item_service_code = " + "'" + item_name + "'"
										+ " AND branch_id= " + veh_branch_id
										+ " LIMIT 1";
								// SOP("item id==========" + StrSql);
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
						}
						if (h == 11) {
							if (veh_variant_id.equals("0")) {
								item_service_code = "";
								item_service_code = StringEscapeUtils.escapeHtml4((sheetData[j][h]));
								service_itemid = "";
								if (item_service_code.contains("(")) {
									item_service_code = item_service_code.replace("(", "&#40;");
								}
								if (item_service_code.contains(")")) {
									item_service_code = item_service_code.replace(")", "&#41;");
								}
								if (item_service_code.equals("null") || item_service_code.equals("0")) {
									item_service_code = "";
								}

								else {
									service_itemid = CNumeric(ExecuteQuery("SELECT item_id"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_service_code='" + item_service_code + "'" + " LIMIT 1"));
								}
								if (!service_itemid.equals("0")) {
									StrSql = "SELECT item_id, item_model_id, model_brand_id" + " "
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
											+ " WHERE 1=1"
											+ " AND item_service_code = " + "'" + item_service_code + "'"
											+ " OR item_name = " + "'" + item_service_code + "'"
											+ " OR item_code = " + "'" + item_service_code + "'"
											+ " AND branch_id= " + veh_branch_id
											+ " LIMIT 1";
									// SOP("service_itemid23=========" + StrSql);
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.isBeforeFirst()) {
										while (crs.next()) {
											veh_variant_id = crs.getString("item_id");
											item_model_id = crs.getString("item_model_id");
											model_brand_id = crs.getString("model_brand_id");
										}
									}
									crs.close();
								}
							}

							if (service_itemid.equals("0") && item_itemid.equals("0")) {
								error_msg += "  Item Is Not Present <br> ";
							}

						}
						if (h == 12) {
							interior = "";
							interior = PadQuotes(sheetData[j][h]);
							if (interior.equals("null")) {
								interior = "";
							}
						}

						if (h == 13) {
							exterior = "";
							exterior = PadQuotes(sheetData[j][h]);
							if (exterior.equals("null")) {
								exterior = "";
							}
						}

						if (h == 14) {
							veh_chassis_no = "";
							veh_chassis_no = PadQuotes(sheetData[j][h]);
							if (veh_chassis_no.equals("null")) {
								veh_chassis_no = "";
							}
							if (veh_chassis_no.length() > 25) {
								error_msg += " Invalid Chassis No.==> " + veh_chassis_no + "<br>";
							}
							if (veh_variant_id.equals("0") || veh_variant_id.equals("") && !veh_chassis_no.equals("")) {
								error_msg += " Model/Item Not Present For Chassis No.==> " + veh_chassis_no + "<br>";
							}
						}
						if (h == 15) {
							veh_engine_no = "";
							veh_engine_no = PadQuotes(sheetData[j][h]);
							if (veh_engine_no.equals("null")) {
								veh_engine_no = "";
							}
							if (veh_engine_no.length() > 25) {
								error_msg += " Invalid Engine No.==> " + veh_engine_no + "<br>";
							}
						}

						if (h == 16) {
							veh_modelyear = "";
							veh_modelyear = PadQuotes(sheetData[j][h]);
							if (veh_modelyear.equals("null") || veh_modelyear.equals("")) {
								veh_modelyear = "";
							}
							if (!veh_sale_date.equals("") && veh_modelyear.equals("")) {
								veh_modelyear = veh_sale_date.substring(0, 4);
							}
							if (!veh_modelyear.equals("")) {
								if (veh_modelyear.length() == 2) {
									veh_modelyear += "20" + veh_modelyear;
								}
								if (veh_modelyear.length() == 4) {
									if (isNumeric(veh_modelyear) != true) {
										error_msg += "Invalid Vehicle Model Year!<br/>";
									}
								}
								else {
									error_msg += "Invalid Vehicle Model Year!<br/>";
								}
							}
						}

						if (h == 17) {
							veh_reg_no = "";
							veh_reg_no = PadQuotes(sheetData[j][h]);
							if (veh_reg_no.equals("null")) {
								veh_reg_no = "";
							}
							if (!veh_chassis_no.equals("") && veh_reg_no.equals("")) {
								error_msg += "Reg No. Should Not Be Empty!";
							}
							if (veh_reg_no.length() > 20) {
								error_msg += "Invalid Reg No.!";
							}
							else {
								veh_reg_no = veh_reg_no.replaceAll("[^a-zA-Z0-9]+", "");
								if (veh_reg_no.equals("") || isNumeric(veh_reg_no) == true) {
									error_msg += "Invalid Reg No.!";
								}
							}
						}
						if (h == 18) {
							veh_kms = "";
							veh_kms = PadQuotes(sheetData[j][h]);
							SOP("veh_kms=1========" + veh_kms);
							if (veh_kms.equals("null")) {
								veh_kms = "0";
							}
							if (!veh_kms.equals("0") && !veh_kms.equals("")) {
								veh_kms = veh_kms.replaceAll("[^.0-9]+", "");
								SOP("veh_kms==2=======" + veh_kms);
							}
							SOP("veh_kms==3=======" + veh_kms);
							veh_lastservice_kms = veh_kms;
						}
						if (h == 19) {
							veh_lastservice = "";
							day = "";
							month = "";
							year = "";
							servicedueyear = "";
							veh_service_duekms = "0";
							veh_service_duedate = "";
							veh_lastservice = PadQuotes(sheetData[j][h]);
							if (veh_lastservice.equals("null")) {
								veh_lastservice = "";
							}
							if (veh_lastservice.equals("")) {
								veh_lastservice = veh_sale_date;
								if (veh_lastservice.equals("0"))
								{
									error_msg = "" + "<br> Sale Date/Purchase Date Is Empty In Your Sheet";
								}
							}
							else {
								veh_lastservice = datevalidate(veh_lastservice);
								if (veh_lastservice.equals("")) {
									error_msg += "Invalid Vehicle Last Service Date! <br>";
									veh_lastservice = "";
								}
							}

							if (!veh_lastservice.equals("")) {
								int duekms = 0;
								int duecount = 0;
								veh_service_duedate = strToLongDate(veh_lastservice);
								day = veh_service_duedate.split("/")[0];
								month = veh_service_duedate.split("/")[1];
								year = veh_service_duedate.split("/")[2].substring(0, 4);
								int i = Integer.parseInt(year);
								if (Long.parseLong(veh_lastservice.substring(1, 8)) > Long.parseLong(ToLongDate(kknow()).substring(1, 8))) {
									servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
								} else {
									servicedueyear = strToShortDate(ToShortDate(kknow())).split("/")[2];
									servicedueyear = (Integer.parseInt(servicedueyear) + 1) + "";
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
							}
						}

						if (h == 20) {
							veh_emp_name = "";
							veh_emp_id = "0";
							veh_emp_name = PadQuotes(sheetData[j][h]);
							if (veh_emp_name.equals("null")) {
								veh_emp_name = "";
							}
							if (!veh_emp_name.equals("")) {
								veh_emp_id = CNumeric(ExecuteQuery("SELECT emp_id"
										+ " FROM " + compdb(comp_id) + "axela_emp WHERE emp_name='" + veh_emp_name + "'"));
							}
							if (veh_emp_id.equals("0")) {
								veh_emp_id = "1";
							}
						}
						if (h == 21) {
							contact_dob = "";
							contact_dob = PadQuotes(sheetData[j][h]);
							if (contact_dob.equals("null")) {
								contact_dob = "";
							}
							if (!contact_dob.equals("")) {
								contact_dob = datevalidate(contact_dob);
								if (contact_dob.equals("")) {
									error_msg += "Invalid DOB! <br>";
									contact_dob = "";
								}
							}
						}
						if (h == 22) {
							contact_anniversary = "";
							contact_anniversary = PadQuotes(sheetData[j][h]);
							if (contact_anniversary.equals("null")) {
								contact_anniversary = "";
							}
							if (!contact_anniversary.equals("")) {
								contact_anniversary = datevalidate(contact_anniversary);
								if (contact_anniversary.equals("")) {
									error_msg += "Invalid Contact Anniversary! <br>";
									contact_anniversary = "";
								}
							}
						}

						if (h == 23) {
							soe_name = "";
							soe_id = "0";
							soe_name = PadQuotes(sheetData[j][h]);
							if (soe_name.equals("null")) {
								soe_name = "";
							}
							if (soe_name.equals("")) {
								soe_name = "Service Data";
							}
							if (!soe_name.equals("")) {
								soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
										+ " FROM " + compdb(comp_id) + "axela_soe WHERE soe_name='" + soe_name + "'"));
							}
						}

						if (h == 24) {
							sob_name = "";
							sob_id = "0";
							sob_name = PadQuotes(sheetData[j][h]);
							if (sob_name.equals("null")) {
								sob_name = "";
							}
							if (sob_name.equals("")) {
								sob_name = "Service Data";
							}
							if (!sob_name.equals("")) {
								sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
										+ " FROM " + compdb(comp_id) + "axela_sob WHERE sob_name='" + sob_name + "'"));
							}
						}
						if (h == 25) {
							vehsource_name = "";
							veh_vehsource_id = "0";
							vehsource_name = PadQuotes(sheetData[j][h]);
							if (vehsource_name.equals("null")) {
								vehsource_name = "";
							}
							if (vehsource_name.equals("")) {
								vehsource_name = "Service Data";
							}
							if (!vehsource_name.equals("")) {
								veh_vehsource_id = CNumeric(ExecuteQuery("SELECT vehsource_id"
										+ " FROM " + compdb(comp_id) + "axela_service_veh_source WHERE vehsource_name='" + vehsource_name + "'"));
							}
						}

						if (h == 26) {
							veh_renewal_date = "";
							// veh_modelyear = "";//COMMENTED FOR INDEL
							veh_renewal_date = PadQuotes(sheetData[j][h]);
							if (veh_renewal_date.equals("null")) {
								veh_renewal_date = "";
							}
							if (veh_renewal_date.equals("")) {
								error_msg += " Renewal Date Not Present In The Sheet!<br>";
							}
							else {
								veh_renewal_date = datevalidate(veh_renewal_date);
								if (veh_renewal_date.equals("")) {
									error_msg += "Invalid Vehicle Renewal Date! <br>";
									veh_renewal_date = "";
								}
							}
						}
					}
					veh_id = "0";
					veh_contact_id = "0";
					veh_customer_id = "0";

					if (veh_reg_no.equals("")) {
						error_msg += "<br>" + "Reg No Is Empty!" + " " + " For This Customer==>" + customer_name;
						continue;
					}

					if ((!veh_reg_no.equals("")) && !veh_variant_id.equals("0")) {
						// CheckForm();
						if (msg.equals("") && error_msg.equals("")) {
							try {
								conntx = connectDB();
								conntx.setAutoCommit(false);
								stmttx = conntx.createStatement();
								veh_reg_no = veh_reg_no.replaceAll("[^a-zA-Z0-9]+", "");
								if (!veh_reg_no.equals("")) {
									StrSql = "SELECT veh_id"
											+ " FROM " + compdb(comp_id) + "axela_service_veh"
											+ " WHERE 1=1"
											+ "	AND veh_reg_no = '" + veh_reg_no + "'";
									veh_id = CNumeric(ExecuteQuery(StrSql));

									StrSql = "SELECT veh_id,veh_chassis_no, veh_reg_no, item_model_id"
											+ " FROM " + compdb(comp_id) + "axela_service_veh"
											+ "	INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
											+ " WHERE 1=1"
											+ "	AND veh_reg_no = '" + veh_reg_no + "'"
											+ "	OR (veh_chassis_no = '" + veh_chassis_no + "'"
											+ " AND item_model_id = " + item_model_id + ")";
									if (!veh_id.equals("0")) {
										StrSql += " AND veh_id = " + veh_id;
									}
									SOP("StrSql==" + StrSql);
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.next()) {
										veh_id = CNumeric(crs.getString("veh_id"));
										if (!veh_id.equals("0")) {

											// If Reg.No is already present but with different model and same chassis Number
											if (veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& !item_model_id.equals(crs.getString("item_model_id"))
													&& !veh_reg_no.equals(crs.getString("veh_reg_no"))) {
												SOP("111");
												status = "add";
											}

											// If Reg.No ,model and chassis Number is Same
											if (veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& item_model_id.equals(crs.getString("item_model_id"))
													&& veh_reg_no.equals(crs.getString("veh_reg_no"))) {
												SOP("222");
												status = "update";
											}

											// If Reg.No empty but chassis Number and model is same
											if (veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& item_model_id.equals(crs.getString("item_model_id"))
													&& crs.getString("veh_reg_no").equals("")) {
												SOP("333");
												status = "update";
											}

											// If Reg.No is diffrent but model and chassis Number same
											if (veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& item_model_id.equals(crs.getString("item_model_id"))
													&& !veh_reg_no.equals(crs.getString("veh_reg_no"))
													&& !crs.getString("veh_reg_no").equals("")) {
												SOP("444");
												status = "update";
											}

											// If Reg.No and chassis Number is Same but model is diffrent
											if (veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& !item_model_id.equals(crs.getString("item_model_id"))
													&& veh_reg_no.equals(crs.getString("veh_reg_no"))) {
												SOP("555");
												status = "update";
											}

											// If Reg.No and model is same but chassis Number is different
											if (!veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& item_model_id.equals(crs.getString("item_model_id"))
													&& veh_reg_no.equals(crs.getString("veh_reg_no"))) {
												SOP("666");
												error_msg += "Reg No. Associated With Other Chassis No. ===>" + veh_reg_no + "<br>";
											}

											// If Reg.No and model is same but chassis Number empty
											if (crs.getString("veh_chassis_no").equals("")
													&& item_model_id.equals(crs.getString("item_model_id"))
													&& veh_reg_no.equals(crs.getString("veh_reg_no"))) {
												SOP("777");
												status = "update";
											}
											// If Reg.No is same but model,chassis Number is different
											if (!veh_chassis_no.equals(crs.getString("veh_chassis_no"))
													&& !item_model_id.equals(crs.getString("item_model_id"))
													&& veh_reg_no.equals(crs.getString("veh_reg_no"))) {
												SOP("888");
												error_msg += "Reg No. Associated With Other Chassis No. ===>" + veh_reg_no + "<br>";
											}

										}
									} else {
										SOP("999");
										status = "add";
									}
									if (status.equals("add")) {
										SOP("add");
										InsertVehicle();
										// propcount++;
									} else if (status.equals("update")) {
										SOP("update");
										UpdateVehicle();
										// updatecount++;
									}
									crs.close();
								}
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
								stmttx.close();
								if (conntx != null && !conntx.isClosed()) {
									conntx.close();
								}
							}
						}
					}
					if (!error_msg.equals("")) {
						if (!veh_reg_no.equals("") && !veh_reg_no.equals("0")) {
							veh_error_msg += ++count + "." + " Registration No.===> " + veh_reg_no + "<br>" + error_msg;
						} else if (!veh_chassis_no.equals("") && !veh_chassis_no.equals("0")) {
							veh_error_msg += ++count + "." + " Chassis No.===> " + veh_chassis_no + "<br>" + error_msg;
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// public String PopulateBranch(String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT branch_id, branch_name, branch_code"
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " WHERE branch_id!=0"
	// + " AND branch_branchtype_id IN (1,3)"
	// + " ORDER BY branch_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// Str.append("<option value=0> Select </option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("branch_id"));
	// Str.append(StrSelectdrop(crs.getString("branch_id"), veh_branch_id));
	// Str.append(">").append(crs.getString("branch_name")).append(" (");
	// Str.append(crs.getString("branch_code")).append(")</option>\n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOP("AxelaAuto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }
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

			Str.append("<option value=\"0\">Select Insurance Executive</option>\n");
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

	public void InsertVehicle() {

		try {
			if (!veh_variant_id.equals("0")) {
				if (!contact_mobile1.equals("")) {
					StrSql = "SELECT customer_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE (customer_mobile1 = '" + contact_mobile1 + "'"
							+ " OR customer_mobile2 = '" + contact_mobile1 + "')";
					veh_customer_id = CNumeric(ExecuteQuery(StrSql));
				}
				if (veh_customer_id.equals("0") && !contact_mobile2.equals("")) {
					StrSql = "SELECT customer_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE (customer_mobile1 = '" + contact_mobile2 + "'"
							+ " OR customer_mobile2 = '" + contact_mobile2 + "')";
					veh_customer_id = CNumeric(ExecuteQuery(StrSql));
				}
				if (!veh_customer_id.equals("0")) {
					// Check Service type contact.
					StrSql = "SELECT contact_id"
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
							+ " WHERE contact_contacttype_id = 9"
							+ " AND customer_id =" + veh_customer_id;
					SOP("StrSql=insert=" + StrSql);
					veh_contact_id = CNumeric(ExecuteQuery(StrSql));

				}
				if (!veh_contact_id.equals("0")) {
					updateContact(veh_contact_id);
				}
				if (veh_customer_id.equals("0")) {
					veh_customer_id = AddCustomer();
				}
				if (!veh_customer_id.equals("0") && veh_contact_id.equals("0")) {
					veh_contact_id = AddContact();
				}
				if (!CNumeric(veh_contact_id).equals("0") && !CNumeric(veh_customer_id).equals("0")) {
					AddVehicle();
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
			StrSql = " UPDATE " + compdb(comp_id) + "axela_customer_contact"
					+ " SET ";
			if (!contact_mobile1.equals("")) {
				StrSql += " contact_mobile1 = '" + contact_mobile1 + "',"
						+ " contact_mobile2_phonetype_id = 3,";
			}
			if (!contact_mobile2.equals("")) {
				StrSql += " contact_mobile2 = '" + contact_mobile2 + "',"
						+ " contact_mobile2_phonetype_id = 3,";
			}

			StrSql += " contact_modified_id = " + veh_entry_id + ","
					+ " contact_modified_date = '" + veh_entry_date + "'"
					+ " WHERE 1=1"
					// + " AND veh_vehsource_id = 2 "
					+ " AND contact_id = " + veh_contact_id;
			SOP("StrSql=veh Contact update==" + StrSql);
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

			StrSql = " SELECT veh_customer_id, veh_contact_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " WHERE 1=1"
					+ " AND veh_id = " + veh_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					veh_customer_id = crs.getString("veh_customer_id");
					veh_contact_id = crs.getString("veh_contact_id");
				}
				if (!veh_contact_id.equals("0")) {
					// checking for contact_contacttype_id Service
					veh_contact_id = CNumeric(ExecuteQuery("SELECT  contact_id  FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
							+ " WHERE contact_contacttype_id = 9"
							+ " AND customer_id =" + veh_customer_id));
					if (!veh_contact_id.equals("0")) {
						updateContact(veh_contact_id);
					} else if (veh_contact_id.equals("0")) {
						veh_contact_id = AddContact();
					}
				}
			}
			else {
				veh_customer_id = AddCustomer();
				veh_contact_id = AddContact();
			}
			crs.close();

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
			if (!veh_reg_no.equals("")) {
				StrSql += " veh_reg_no = '" + veh_reg_no + "',";
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

			StrSql += " veh_modified_id = " + veh_entry_id + ",";
			StrSql += " veh_modified_date =" + veh_entry_date + ",";

			if (!veh_emp_id.equals("0")) {
				StrSql += " veh_emp_id = " + veh_emp_id + ",";
			}

			StrSql = StrSql.substring(0, StrSql.length() - 1);
			StrSql += " WHERE veh_id =" + veh_id;

			SOP("strsql===========update====vehicle=veh===" + StrSql);
			stmttx.execute(StrSql);
			updatecount++;
			// For inserting the recent kms of the vehicle into veh_kms table
			if (!veh_kms.equals("0")) {
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

				// if (!vehkms_id.equals("0")) {
				// updatecount++;
				// }

			}

			// For inserting Insurance followup of the vehicle into insur_followup table
			// if (veh_insfollowupby.equals("1") && !veh_sale_date.equals("") || veh_insfollowupby.equals("2") && !veh_renewal_date.equals("")) {
			// // Delete the existing followups whose desc is empty
			// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup"
			// + "	WHERE insurfollowup_desc = ''"
			// + "	AND insurfollowup_veh_id = " + veh_id;
			// // SOP("StrSql==Delete==Insur Followup==" + StrSql);
			// stmttx.execute(StrSql);
			//
			// // Inserting the new followups based on Sale date or Renewal date
			//
			// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
			// + " (insurfollowup_veh_id,"
			// + " insurfollowup_emp_id,"
			// + " insurfollowup_followuptype_id,"
			// + " insurfollowup_entry_id,"
			// + " insurfollowup_entry_time,"
			// + " insurfollowup_followup_time)"
			// + " VALUES ("
			// + veh_id + ", "
			// + veh_insuremp_id + ","
			// + " 1,"
			// + " " + veh_entry_id + ","
			// + " '" + veh_entry_date + "',";
			// if (veh_insfollowupby.equals("1")) {
			// // If Insurance followup by is Sale date
			// StrSql += " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + veh_sale_date + "'" + ","
			// + " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'100000'))";
			// } else if (veh_insfollowupby.equals("2")) {
			// // If Insurance followup by is Renewal date
			// StrSql += " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + "'" + veh_renewal_date + "'" + ","
			// + " INTERVAL -60 DAY), '%Y%m%d%h%i%s')), 5, 4),'100000'))";
			// }
			//
			// // SOP("StrSql==Insert==Insur Followup==========" + StrSql);
			// stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			// ResultSet rs = stmttx.getGeneratedKeys();
			// String insurpolicy_veh_id = "0";
			// while (rs.next()) {
			// insurpolicy_veh_id = rs.getString(1);
			// }
			// rs.close();
			// insurpolicy_veh_id = CNumeric(insurpolicy_veh_id);
			//
			// if (!insurpolicy_veh_id.equals("0")) {
			// followupcount++;
			// }
			// }

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
	public String AddCustomer() throws SQLException {

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
				+ " (customer_branch_id,"
				+ " customer_name,"
				+ " customer_mobile1,"
				+ " customer_mobile1_phonetype_id,"
				+ " customer_mobile2,"
				+ " customer_mobile2_phonetype_id,"
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
				+ " (" + veh_branch_id + ","
				+ " '" + customer_name + "',"
				+ " '" + contact_mobile1 + "',"
				+ " 3," // Service customer_mobile1_phonetype_id
				+ " '" + contact_mobile2 + "',"
				+ " 3," // Service customer_mobile2_phonetype_id
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
				+ " contact_mobile1_phonetype_id,"
				+ " contact_mobile2,"
				+ " contact_mobile2_phonetype_id,"
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
				+ " 9," // contact_contacttype_id
				+ " " + contact_title_id + ","
				+ " '" + contact_fname + "',"
				+ " '" + contact_lname + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '3'," // Service contact_mobile1_phonetype_id
				+ " '" + contact_mobile2 + "',"
				+ " '3'," // Service contact_mobile2_phonetype_id
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
		stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_contact_id = rs.getString(1);
		}
		rs.close();
		return veh_contact_id;
	}

	public void AddVehicle() throws SQLException {
		// SOP("veh_sale_date==9=" + veh_sale_date);
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
			if (!veh_kms.equals("0")) {
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
				SOP("INSERT==axela_service_veh_kms==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
			}
			if (!veh_insuremp_id.equals("0")) {
				if (veh_insfollowupby.equals("1") && !veh_sale_date.equals("")) {
					AddInsurFollowupFields(veh_id, veh_sale_date, veh_insuremp_id);
				} else if (veh_insfollowupby.equals("2") && !veh_renewal_date.equals("")) {
					AddInsurFollowupFields(veh_id, veh_renewal_date, veh_insuremp_id);
				}
			}

			if (veh_followup.equals("1")) {
				// String default_mileage = "30";

				// StrSql = "UPDATE "
				// + compdb(comp_id)
				// + "axela_service_veh v1"
				// + " INNER JOIN (SELECT veh_id, calkms"
				// + " FROM (SELECT veh_id, @default_mileage:="
				// + default_mileage
				// + ","
				// + " @kmscount:=COALESCE((SELECT COUNT(vehkms_id)"
				// + " FROM "
				// + compdb(comp_id)
				// + "axela_service_veh_kms"
				// + " WHERE vehkms_veh_id = veh_id), '0') AS kmscount,"
				// + " @date1:=COALESCE((SELECT vehkms_entry_date"
				// + " FROM "
				// + compdb(comp_id)
				// + "axela_service_veh_kms"
				// + " WHERE vehkms_veh_id = veh_id"
				// + " ORDER BY vehkms_id DESC LIMIT 1), '') AS date1,"
				// + " @kms1:=COALESCE((SELECT vehkms_kms"
				// + " FROM "
				// + compdb(comp_id)
				// + "axela_service_veh_kms"
				// + " WHERE vehkms_veh_id = veh_id"
				// + " ORDER BY vehkms_id DESC LIMIT 1), '') AS kms1,"
				// + " @date2:=COALESCE((SELECT vehkms_entry_date"
				// + " FROM "
				// + compdb(comp_id)
				// + "axela_service_veh_kms"
				// + " WHERE vehkms_veh_id = veh_id"
				// + " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS date2,"
				// + " @kms2:=COALESCE((SELECT vehkms_kms"
				// + " FROM "
				// + compdb(comp_id)
				// + "axela_service_veh_kms"
				// + " WHERE vehkms_veh_id = veh_id"
				// + " ORDER BY vehkms_id DESC LIMIT 1, 1), '') AS kms2,"
				// + " IF(@kmscount>1, COALESCE((veh_kms + ((@kms1 - @kms2)/DATEDIFF(@date1, @date2))*DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms + (@kms1-@kms2))),"
				// + " (IF(@kmscount=1, COALESCE((veh_kms + @default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', @date1)),(veh_kms+@default_mileage)),"
				// + " COALESCE(@default_mileage * DATEDIFF('" + ToLongDate(kknow()) + "', veh_sale_date),@default_mileage))))"
				// + " AS calkms,"
				// + " IF (@kmscount > 1,"
				// + " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / ((@kms1 - @kms2) / DATEDIFF(@date1, @date2)) ,0),"
				// + "	(IF(@kmscount = 1,"
				// + " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0),"
				// + " @calserviceday := COALESCE ((veh_lastservice_kms + 10000)  / 30 ,0)))"
				// + " ) AS calservicedate,"
				// + " IF(@kmscount > 1, COALESCE(@date1, ''), (IF(@kmscount = 1, COALESCE(@date1, ''), veh_sale_date )))"
				// + " AS lastservice_date"
				// + " FROM " + compdb(comp_id) + "axela_service_veh"
				// + " WHERE 1=1";
				// // + " AND veh_vehsource_id = 2";
				// StrSql += " AND veh_id = " + veh_id;
				// StrSql += " GROUP BY veh_id"
				// + " ORDER BY veh_id) Sat) v2"
				// + " SET"
				// + " v1.veh_cal_kms = v2.calkms,"
				// + " v1.veh_calservicedate = DATE_FORMAT(DATE_ADD(veh_lastservice,INTERVAL IF(@calserviceday< 365, @calserviceday, 365) DAY),'%Y%m%d%h%i%s')"
				// + " WHERE v1.veh_id = v2.veh_id";
				// // SOP("StrSql====vehicle kms update==" + StrSqlBreaker(StrSql));
				// stmttx.execute(StrSql);

				// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
				// + " (vehfollowup_veh_id,"
				// + " vehfollowup_emp_id,"
				// + " vehfollowup_followup_time)"
				// + " VALUES (" + veh_id + ", " + veh_crmemp_id + ","
				// + " '20151027110000')";
				//
				// SOP("strsql====add veh followup=" + StrSql);
				// stmttx.execute(StrSql);
			}
		}

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
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup"
					+ "	WHERE insurfollowup_desc = ''"
					+ "	AND insurfollowup_veh_id = " + veh_id;
			// SOP("StrSql==Delete==Insur Followup==" + StrSql);
			stmttx.execute(StrSql);

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
			// SOP("StrSql===insur followup==" + StrSql);
			stmttx.execute(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh" + " SET"
					+ " veh_insuremp_id = " + veh_insuremp_id + ""
					+ " WHERE veh_id = " + veh_id + "";
			// SOP("strsql==update=veh=insur==" + StrSqlBreaker(StrSql));
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

			Str.append("<option value=\"0\">Select CRM Executive</option>\n");
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

	public String validcontact(String contact_mobile) {
		if (contact_mobile.length() <= 14 && !contact_mobile.contains("0000000000")) {
			if (contact_mobile.contains(",")) {
				contact_mobile = contact_mobile.split(",")[0];
			}
			contact_mobile = contact_mobile.replaceAll("[^0-9]+", "");
			if (contact_mobile.charAt(0) == '0' || contact_mobile.contains("+0")) {
				contact_mobile = contact_mobile.substring(contact_mobile.indexOf("0") + 1, contact_mobile.length());
			}
			else if (contact_mobile.indexOf("9") == 0 && contact_mobile.indexOf("1") == 1 && contact_mobile.length() != 10) {
				contact_mobile = contact_mobile.substring(2, contact_mobile.length());
			}
			if (isNumeric(contact_mobile) != true) {
				contact_mobile = "";
			}
			if (!contact_mobile.contains("91-") && contact_mobile.length() == 10) {
				contact_mobile = "91-" + contact_mobile;
			}
			if (!IsValidMobileNo11(contact_mobile)) {
				contact_mobile = "";
			}
		}
		else {
			contact_mobile = "";
		}
		SOP("contact_mobile======" + contact_mobile);
		return contact_mobile;

	}

	public String datevalidate(String date) {
		String day = "";
		String month = "";
		String year = "";
		if (isValidDateFormatShort(date)) {
			date = ConvertShortDateToStr(date);
		} else if (date.split("/").length == 3) {
			month = date.split("/")[0];
			if (month.length() == 1) {
				month = "0" + month;
			}
			day = date.split("/")[1];
			if (day.length() == 1) {
				day = "0" + day;
			}
			year = date.split("/")[2];
			if (year.length() == 2) {
				year = "20" + year;
			}
			if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
				date = year + month + day + "000000";
			}
		} else if (date.contains(".")) {
			if (date.split("\\.").length == 3) {
				day = date.split("\\.")[0];
				if (day.length() == 1) {
					day = "0" + day;
				}
				month = date.split("\\.")[1];
				if (month.length() == 1) {
					month = "0" + month;
				}
				year = date.split("\\.")[2];
				if (year.length() == 2) {
					year = "20" + year;
				}
				if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
					date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
					date = date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
				} else {
					date = "";
				}
				day = "";
				month = "";
				year = "";
			}
		} else if (date.contains("-")) {
			if (date.split("\\-").length == 3) {
				day = date.split("\\-")[0];
				if (day.length() == 1) {
					day = "0" + day;
				}
				month = date.split("\\-")[1];
				if (month.length() == 1) {
					month = "0" + month;
				}
				year = date.split("\\-")[2];
				if (year.length() == 2) {
					year = "20" + year;
				}
				if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
					date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
					date = date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
				} else {
					date = "";
				}
				day = "";
				month = "";
				year = "";
			}
		}
		return date;
	}
}
