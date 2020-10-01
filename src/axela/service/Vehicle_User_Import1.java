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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Vehicle_User_Import1 extends Connect {

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
	public String veh_entry_id = "";
	public String veh_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", veh_branch_id = "0", insurpolicy_emp_id = "0";
	public String branch_name = "";
	public String upload = "";
	public String similar_comm_no = "";
	public String veh_id = "0";
	public String veh_followup = "0";
	public String veh_emp_id = "0", veh_emp_name = "";
	public String customer_name = "", contact_name = "", contact_title = "0", contact_fname = "", contact_lname = "", contact_fname_lname;
	public String contact_mobile1 = "", contact_mobile2 = "", contact_email1 = "", contact_email2 = "", contact_address = "", contact_city = "", contact_city_id = "0", contact_pin = "";
	public String interior = "", exterior = "", item_name = "", soe_name = "", sob_name = "", veh_vin_no = "", veh_engine_no = "", veh_reg_no = "", veh_modelyear = "", veh_sale_date = "",
			veh_insur_date = "", veh_lastservice = "", veh_kms = "";
	public String veh_customer_id = "0", veh_contact_id = "0", soe_id = "0", sob_id = "0";
	public String veh_model_id = "0", model_brand_id = "0", veh_variant_id = "0", veh_fueltype_id = "0", veh_loanfinancer = "", veh_status_id = "0", veh_status_date = "";
	public String veh_soe_id = "0", veh_buyertype_id = "0", veh_desc = "", model_name = "";
	public String veh_notes = "", veh_priorityveh_id = "0", veh_vehsource_id = "0", vehsource_name = "", veh_chassis_no = "", veh_comm_no = "",
			veh_service_duedate = "", veh_service_duekms = "0";
	public String option_id = "0", veh_lastservice_kms = "0", veh_renewal_date = "", error_msg = "", item_service_code = "";
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
				veh_branch_id = CNumeric(PadQuotes((request.getParameter("dr_branch"))));
				insurpolicy_emp_id = CNumeric(PadQuotes((request.getParameter("dr_insur_emp_id"))));
				veh_followup = CheckBoxValue(PadQuotes(request.getParameter("chk_veh_followup")));
				SOP("veh_branch_id===" + veh_branch_id);
				SOP("insurpolicy_emp_id===" + insurpolicy_emp_id);
				SOP("veh_followup===" + veh_followup);
				veh_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button"));
				// SOP("property branch_id=========" + branch_id);
				if (insurpolicy_emp_id.equals("0")) {
					insurpolicy_emp_id = emp_id;
				}
				// if (branch_id.equals("0")) {
				// if (emp_id.equals("1")) {
				// veh_branch_id =
				// ExecuteQuery("SELECT branch_id FROM axela_branch"
				// + " WHERE branch_active = '1'"
				// + " LIMIT 1");
				// } else if (!emp_id.equals("1")) {
				// veh_branch_id =
				// ExecuteQuery("SELECT emp_branch_id FROM axela_emp_branch"
				// + " INNER JOIN axela_branch ON branch_id = emp_branch_id"
				// + " WHERE branch_active = '1'"
				// + " AND emp_id = " + emp_id + ""
				// + " LIMIT 1");
				// // SOP("branch_id = " + branch_id);
				// }
				// } else {
				// veh_branch_id = branch_id;
				// }
				Addfile(request, response);
			}
		} catch (Exception ex) {
			SOP("AxelaCRM===" + this.getClass().getName());
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
					veh_branch_id = str1[0];
					insurpolicy_emp_id = CNumeric(PadQuotes(str1[1]));
					veh_followup = CheckBoxValue(CNumeric(PadQuotes(str1[2])));
					SOP("veh_branch_id===" + veh_branch_id);
					SOP("insurpolicy_emp_id===" + insurpolicy_emp_id);
					SOP("veh_followup===" + veh_followup);
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
										msg = "<br>" + propcount + " Vehicles imported successfully!" + errormsg;
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOP("AxelaCRM===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("veh-user-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (veh_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (insurpolicy_emp_id.equals("0")) {
			msg = msg + "<br>Select Insurance Executive!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {

			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls
															// type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is
															// .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			if (rowLength > 500) {
				rowLength = 500;
			}
			int h = 0;
			int j = 0;
			int count = 0;
			propcount = 0;

			for (j = 1; j < rowLength + 1; j++) {
				CheckForm();
				error_msg = "";
				for (h = 0; h < columnLength + 1; h++) {

					if (h == 0) {
						customer_name = sheetData[j][h];
						if (customer_name.equals("null")) {
							customer_name = "";
						}
					}

					if (h == 1) {
						contact_name = sheetData[j][h];
						contact_fname = "";
						contact_lname = "";

						if (contact_name.equals("null")) {
							contact_name = "";
						}

						if (!customer_name.equals("")) {
							contact_name = customer_name;
						}
						if (!contact_name.equals("")) {
							if (contact_name.contains(" ")) {
								contact_title = contact_name.split(" ")[0];

								contact_title = ExecuteQuery("SELECT title_id FROM " + compdb(comp_id) + "axela_title"
										+ " WHERE title_desc = '" + contact_title + "'");
								if (CNumeric(contact_title).equals("0")) {
									contact_title = "1";
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
								contact_title = "1";
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
						contact_mobile1 = sheetData[j][h];
						if (contact_mobile1.equals("null")) {
							contact_mobile1 = "";
						}
					}

					if (h == 3) {
						contact_mobile2 = sheetData[j][h];
						if (contact_mobile2.equals("null")) {
							contact_mobile2 = "";
						}
					}

					if (h == 4) {
						contact_email1 = sheetData[j][h];
						if (contact_email1.equals("null")) {
							contact_email1 = "";
						}
					}
					if (h == 5) {
						contact_email2 = sheetData[j][h];
						if (contact_email2.equals("null")) {
							contact_email2 = "";
						}
					}
					if (h == 6) {// /////address
						contact_address = sheetData[j][h];
						if (contact_address.equals("null") || contact_address.equals("N/A")) {
							contact_address = "";
						}
					}
					if (h == 7) {
						contact_city = sheetData[j][h];
						if (contact_city.equals("null")) {
							contact_city = "";
						}
						if (!contact_city.equals("")) {
							contact_city = ExecuteQuery("SELECT city_id FROM " + compdb(comp_id) + "axela_city"
									+ " WHERE city_name = '" + contact_city + "'");
						}
						if (CNumeric(contact_city).equals("0")) {
							contact_city = CNumeric(ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = '" + veh_branch_id + "'"
									+ " LIMIT 1"));
						}
					}
					if (h == 8) {
						contact_pin = sheetData[j][h];
						if (contact_pin.equals("null") || contact_pin.equals("N/A")) {
							contact_pin = "";
						}
						if (contact_pin.equals("")) {
							contact_pin = ExecuteQuery("SELECT branch_pin FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = " + veh_branch_id + "");
						}
					}

					if (h == 9) {
						veh_sale_date = sheetData[j][h];
						if (veh_sale_date.equals("null")) {
							veh_sale_date = "";
						}
					}

					if (h == 10) {
						model_name = sheetData[j][h];
						// SOP("model_name=="+model_name);
						if (model_name.equals("null")) {
							model_name = "";
						}
						StrSql = "SELECT model_id, model_brand_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_name = '" + model_name + "'"
								+ " LIMIT 1";
						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							veh_model_id = crs.getString("model_id");
							model_brand_id = crs.getString("model_brand_id");
						}
						crs.close();
						SOP("veh_model_id===" + veh_model_id);
						SOP("model_brand_id===" + model_brand_id);
						// veh_model_id =
						// CNumeric(ExecuteQuery("SELECT model_id FROM " +
						// compdb(comp_id) + "axela_inventory_item_model"
						// + " WHERE model_name = '" + model_name + "'"
						// + " LIMIT 1"));
						// SOP("veh_model_id===" + veh_model_id);
					}
					veh_variant_id = "0";
					if (h == 11) {
						item_service_code = sheetData[j][h];
						// SOP("model_service_code===" + model_service_code);
						if (item_service_code.equals("null") || item_service_code.equals("0")) {
							item_service_code = "";
						}
					}

					if (h == 12) {
						interior = sheetData[j][h];
						if (interior.equals("null")) {
							interior = "";
						}
					}

					if (h == 13) {
						exterior = sheetData[j][h];
						if (exterior.equals("null")) {
							exterior = "";
						}
					}

					// if (h == 14) {
					// veh_vin_no = sheetData[j][h];
					// if (veh_vin_no.equals("null")) {
					// veh_vin_no = "";
					// }
					// }
					if (h == 14) {
						veh_chassis_no = sheetData[j][h];
						if (veh_chassis_no.equals("null")) {
							veh_chassis_no = "";
						}
					}
					if (h == 15) {
						veh_engine_no = sheetData[j][h];
						if (veh_engine_no.equals("null")) {
							veh_engine_no = "";
						}
					}

					// if (h == 16) {
					// veh_comm_no = sheetData[j][h];
					// if (veh_comm_no.equals("null")) {
					// veh_comm_no = "";
					// }
					// }

					if (h == 16) {
						veh_modelyear = sheetData[j][h];
						if (veh_modelyear.equals("null")) {
							veh_modelyear = "";
						}
					}

					if (h == 17) {
						veh_insur_date = sheetData[j][h];
						if (veh_insur_date.equals("null")) {
							veh_insur_date = "";
						}
					}

					if (h == 18) {
						veh_reg_no = sheetData[j][h];
						if (veh_reg_no.equals("null")) {
							veh_reg_no = "";
						}
					}

					if (h == 19) {
						veh_lastservice = sheetData[j][h];
						if (veh_lastservice.equals("null")) {
							veh_lastservice = "";
						}
						if (veh_lastservice.equals("")) {
							veh_lastservice = veh_sale_date;
						}
					}

					if (h == 20) {
						veh_kms = sheetData[j][h];
						if (veh_kms.equals("null")) {
							veh_kms = "0";
						}
					}

					if (h == 21) {
						veh_emp_name = sheetData[j][h];
						if (veh_emp_name.equals("null")) {
							veh_emp_name = "";
						}
						if (!veh_emp_name.equals("")) {
							veh_emp_id = ExecuteQuery("SELECT emp_id"
									+ " FROM " + compdb(comp_id) + "axela_emp WHERE emp_name='" + veh_emp_name + "'");
						} else {
							veh_emp_id = "1";
						}

					}

					if (h == 22) {
						soe_name = sheetData[j][h];
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

					if (h == 23) {
						sob_name = sheetData[j][h];
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
					if (h == 24) {
						vehsource_name = sheetData[j][h];
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
				}
				veh_entry_date = "";
				veh_entry_date = ToLongDate(kknow());

				if (veh_reg_no.equals("")) {
					error_msg += (propcount + "") + ".";
					error_msg += " " + "Veh Reg.No. is empty!";
				}

				String veh_reg_no_check = "";
				StrSql = "SELECT veh_reg_no" + " FROM " + compdb(comp_id)
						+ " axela_service_veh" + " WHERE veh_reg_no = '"
						+ veh_reg_no + "'";
				// SOP("StrSql==jc_ro_no_check=" + StrSql);
				veh_reg_no_check = PadQuotes(ExecuteQuery(StrSql));

				// veh_id = "0";
				// veh_contact_id = "0";
				// veh_customer_id = "0";

				// Main check for jc
				// SOP("veh_reg_no==" + veh_reg_no);
				if (!veh_reg_no.equals("")) {
					StrSql = "SELECT COALESCE(veh_id, 0)"
							+ " FROM " + compdb(comp_id) + "axela_service_veh"
							+ " WHERE veh_reg_no = '" + veh_reg_no + "'";
					// SOP("StrSql 2 = " + StrSql);
					veh_id = CNumeric(ExecuteQuery(StrSql));
					if (!veh_id.equals("0")) {
						// SOP("veh_id ==already present = " + veh_id);
					}

					if (veh_id.equals("0")) {

						// SOP("item_id===" + item_id);
						if (!veh_variant_id.equals("0")) {
							if (veh_contact_id.equals("0")) {
								if (CNumeric(veh_customer_id).equals("0")) {
									veh_customer_id = AddCustomer();
								}

								if (CNumeric(veh_contact_id).equals("0")) {
									veh_contact_id = AddContact();
								}
							}
							// SOP("veh_contact_id==" + veh_contact_id +
							// " veh_customer_id==" + veh_customer_id);

							if (!CNumeric(veh_contact_id).equals("0") && !CNumeric(veh_customer_id).equals("0")) {
								AddVehicle();
							} else {
								// StrSql = "UPDATE " + compdb(comp_id) +
								// "import_vehicle"
								// + " SET imp_active = '2'"
								// + " WHERE imp_id = " + imp_id + "";
								// updateQuery(StrSql);
							}
						} // End Scope of veh_variant_id !=0
						else {
							// StrSql = "UPDATE " + compdb(comp_id) +
							// "import_vehicle"
							// + " SET imp_active = '2'"
							// + " WHERE imp_id = " + imp_id + "";
							// updateQuery(StrSql);
						}
					} else {
						// For updating veh followup time for existing
						// vehicles...
						StrSql = " UPDATE " + compdb(comp_id) + "axela_service_followup"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
								+ " SET vehfollowup_followup_time = " + " CONCAT(substr(" + ToShortDate(kknow()) + ",1,8), 100000)"
								+ " WHERE 1=1"
								// + " AND veh_vehsource_id = 2 "
								+ " AND DATE_FORMAT(DATE_SUB(veh_calservicedate, INTERVAL 1 MONTH),'%Y%m%d%h%i%s') <= '" + ToLongDate(kknow()) + "' "
								+ " AND vehfollowup_veh_id = " + veh_id
								+ " AND vehfollowup_desc = ''";
						// SOP("StrSql=veh present==" + StrSql);
						stmttx.execute(StrSql);

						// For inserting the recent kms of the vehicle into
						// veh_kms table
						if (!veh_kms.equals("0") || veh_kms.equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
									+ " (vehkms_veh_id,"
									+ " vehkms_kms,"
									+ " vehkms_entry_id,"
									+ " vehkms_entry_date)"
									+ " VALUES"
									+ " (" + veh_id + ","
									+ " " + veh_kms + ","
									+ " " + veh_entry_id + ","
									+ " '" + veh_lastservice + "')";
							// SOP("strsql===if===veh kms===" +
							// StrSqlBreaker(StrSql));
							stmttx.execute(StrSql);

							// For updating the recent kms of the vehicle into
							// veh table
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET ";
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
								StrSql += " veh_kms = " + veh_kms + ",";
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
							// SOP("strsql===if==111=veh===" + StrSql);
							stmttx.execute(StrSql);
						}

						// StrSql = "UPDATE " + compdb(comp_id) +
						// "import_vehicle"
						// + " SET imp_active = '1'"
						// + " WHERE imp_id = " + imp_id + "";
						// updateQuery(StrSql);
					}
				}

				// SOP("veh_id=======" + veh_id);
				// SOP("contact_fname===" + contact_fname);
				if (veh_id.equals("0") && !contact_fname.equals("")) {
					Vehicle_Update veh = new Vehicle_Update();
					veh.comp_id = comp_id;
					veh.emp_id = emp_id;
					// veh.veh_insuremp_id = insurpolicy_emp_id;
					veh.customer_branch_id = veh_branch_id;
					veh.customer_name = customer_name;
					veh.customer_id = veh_customer_id;
					// veh.account_city_id = veh_customer_id;
					veh.contact_id = veh_contact_id;
					veh.contact_title_id = contact_title;
					veh.contact_fname = contact_fname;
					veh.contact_lname = contact_lname;
					veh.contact_mobile1 = contact_mobile1;
					veh.contact_mobile2 = contact_mobile2;
					veh.contact_email1 = contact_email1;
					veh.contact_email2 = contact_email2;
					veh.contact_city_id = contact_city;
					veh.veh_so_id = "0";
					// veh.item_model_id = veh_model_id;
					veh.veh_variant_id = veh_variant_id;
					veh.veh_modelyear = veh_modelyear;
					veh.veh_chassis_no = veh_chassis_no;
					veh.veh_engine_no = veh_engine_no;
					veh.veh_reg_no = veh_reg_no;
					veh.vehsaledate = veh_sale_date;
					veh.veh_emp_id = emp_id;
					veh.veh_vehsource_id = veh_vehsource_id;
					veh.veh_kms = veh_kms;
					veh.veh_lastservice = veh_lastservice;
					veh.veh_lastservice_kms = veh_kms;
					veh.veh_service_duekms = "0";
					veh.veh_service_duedate = "";
					veh.veh_warranty_expirydate = "";
					// veh.veh_iacs = "0";
					veh.soe_id = soe_id;
					veh.sob_id = sob_id;
					veh.veh_notes = "";
					veh.veh_entry_id = "1";
					veh.veh_entry_date = ToLongDate(kknow());
					veh.AddFields();
					SOP("Import check fields===" + veh.msg);
					if (!veh.msg.equals("")) {
						errormsg += "<br>Reg. No.: " + veh_reg_no + "=> " + veh.msg;
					}
					veh_id = veh.veh_id;
					SOP("after insert veh_id=====" + veh_id);
					// SOP("after insert interior====="+interior);
					// SOP("after insert exterior====="+exterior);
					if (!veh_id.equals("0")) {
						propcount++;
					}

					if (!veh_id.equals("0")) {
						option_id = "0";
						StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
								+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
								+ " WHERE option_brand_id = " + model_brand_id
								+ " AND option_name = '" + interior + "'";
						option_id = ExecuteQuery(StrSql);
						SOP("after insert veh_id=====" + option_id);

						if (!CNumeric(option_id).equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " (vehtrans_option_id,"
									+ " vehtrans_veh_id)"
									+ " VALUES"
									+ " ('" + option_id + "',"
									+ " '" + veh_id + "')";
							SOP("StrSql=interior=" + StrSql);
							updateQuery(StrSql);
						}
						if (!interior.equals(exterior)) {
							option_id = "0";
							StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
									+ " WHERE option_brand_id = " + model_brand_id
									+ " AND option_name = '" + exterior + "'";
							option_id = ExecuteQuery(StrSql);

							if (!CNumeric(option_id).equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
										+ " (vehtrans_option_id,"
										+ " vehtrans_veh_id)"
										+ " VALUES"
										+ " ('" + option_id + "',"
										+ " '" + veh_id + "')";
								SOP("StrSql=exterior=" + StrSql);
								updateQuery(StrSql);
							}
						}
					}
				}

			}
		} catch (Exception ex) {
			SOPError("AxelaCRM===" + this.getClass().getName());
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
			SOP("AxelaCRM===" + this.getClass().getName());
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

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), insurpolicy_emp_id));
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
				+ " (" + branch_id + ","
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
		// SOP("StrSql==cust==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_customer_id = rs.getString(1);
		}

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
				+ " contact_notes,"
				+ " contact_active,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + veh_customer_id + ","
				+ " 1,"
				+ " " + contact_title + ","
				+ " '" + contact_fname + "',"
				+ " '" + contact_lname + "',"
				+ " '" + contact_mobile1 + "',"
				+ " '" + contact_mobile2 + "',"
				+ " '" + contact_email1 + "',"
				+ " '" + contact_email2 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " '',"
				+ " '1',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		// SOP("StrSql==contact=" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_contact_id = rs.getString(1);
		}
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
				+ " (" + branch_id + ","
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
				+ " " + ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 AND emp_crm = 1 and emp_active = 1 "
						+ " ORDER BY RAND() LIMIT 1") + ","
				+ " " + insurpolicy_emp_id + ","
				+ " '" + veh_renewal_date + "',"
				+ " '" + veh_notes + "',"
				+ " " + veh_entry_id + ","
				+ " '" + veh_entry_date + "')";
		// SOP("StrSql===veh==" + StrSql);
		stmttx.execute(StrSql,
				Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmttx.getGeneratedKeys();

		while (rs.next()) {
			veh_id = rs.getString(1);
		}
		rs.close();
		veh_id = CNumeric(veh_id);
		if (!veh_id.equals("0")) {
			// count++;
			// SOP("count===veh==" + count);
		}

		if (!veh_id.equals("0")) {
			StrSql = "SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_insur = 1"
					+ " ORDER BY RAND()"
					+ " LIMIT 1";
			String insurpolicy_emp_id = CNumeric(ExecuteQuery(StrSql));

			if (insurpolicy_emp_id.equals("0")) {
				insurpolicy_emp_id = CNumeric(veh_emp_id);
			}

			if (!insurpolicy_emp_id.equals("0")) {
				AddInsurFollowupFields(veh_id, veh_sale_date, insurpolicy_emp_id);
			}
		}

		// SOP("StrSql===veh_id==" + veh_id);
		if (!CNumeric(veh_kms).equals("0") && !veh_id.equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
					+ " (vehkms_veh_id,"
					+ " vehkms_kms,"
					+ " vehkms_entry_id,"
					+ " vehkms_entry_date)"
					+ " VALUES"
					+ " (" + veh_id + ","
					+ " " + veh_kms + ","
					+ " " + veh_entry_id + ","
					+ " '" + veh_lastservice + "')";
			stmttx.execute(StrSql);
		}
		if (!veh_id.equals("0")) {
			option_id = "0";
			StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
					+ " WHERE option_name = '" + interior + "'";
			option_id = CNumeric(ExecuteQuery(StrSql));

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
				StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
						+ " WHERE option_name = '" + exterior + "'";
				option_id = CNumeric(ExecuteQuery(StrSql));

				if (!option_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
							+ " (vehtrans_option_id,"
							+ " vehtrans_veh_id)"
							+ " VALUES"
							+ " (" + option_id + ","
							+ " " + veh_id + ")";
					// SOP("StrSql=exterior=" + StrSql);
					stmttx.execute(StrSql);
				}
			}
		}
		conntx.commit();
		new Manage_Veh_Kms().VehKmsUpdate(veh_id, comp_id);

		// StrSql = "UPDATE " + compdb(comp_id) + "import_vehicle"
		// + " SET imp_active = '1'"
		// + " WHERE imp_id = " + imp_id + "";
		// updateQuery(StrSql);

	}

	public void AddInsurFollowupFields(String veh_id, String veh_sale_date,
			String insurpolicy_emp_id) throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id)
					+ "axela_insurance_followup"
					+ " (insurfollowup_veh_id," + " insurfollowup_emp_id,"
					+ " insurfollowup_followup_time,"
					+ " insurfollowup_followuptype_id,"
					+ " insurfollowup_priorityinsurfollowup_id,"
					+ " insurfollowup_desc," + " insurfollowup_entry_id,"
					+ " insurfollowup_entry_time," + " insurfollowup_trigger)"
					+ " VALUES" + " (" + veh_id + ","
					+ " " + insurpolicy_emp_id + ","
					+ " IF(CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5, 8)) <"
					+ " SUBSTR('" + ToLongDate(kknow()) + "', 1, 8), "
					+ " CONCAT(SUBSTR(DATE_ADD('" + ToLongDate(kknow()) + "', INTERVAL 1 YEAR), 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date
					+ ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5)),"
					+ " CONCAT(SUBSTR('" + ToLongDate(kknow()) + "', 1, 4), SUBSTR((DATE_FORMAT(DATE_ADD(" + veh_sale_date + ", INTERVAL 10 MONTH), '%Y%m%d%h%i%s')), 5))),"
					// + " DATE_FORMAT(DATE_ADD(" + veh_sale_date +
					// ", INTERVAL 11 MONTH), '%Y%m%d%h%i%s'),"
					+ " 1,"
					+ " 1," + " ''," + " " + veh_entry_id + "," + " '" + veh_entry_date
					+ "'," + " 0)";
			SOP("StrSql===insur followup==" + StrSql);
			stmttx.execute(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh" + " SET"
					+ " veh_insuremp_id = " + insurpolicy_emp_id + ""
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
}
