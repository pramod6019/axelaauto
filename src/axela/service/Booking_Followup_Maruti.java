//Shivaprasad 6/07/2015   
package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
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

public class Booking_Followup_Maruti extends Connect {

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
	public int propcount = 0, updatecount = 0;
	public String veh_entry_id = "0";
	public String veh_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", veh_branch_id = "0";
	public String branch_name = "";
	public String upload = "";
	public String similar_comm_no = "";
	public String jc_id = "0";
	public String vehkms_id = "0", veh_crmemp_id = "0";
	public String error_msg = "";
	public String veh_error_msg = "";
	public String chassisid = "0";
	public int count = 0;

	// Customer And Contact Variables
	public String customer_id = "0";
	public String customer_name = "", caruser_name = "";
	public String contact_id = "0";
	public String contact_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_phone1 = "", contact_phone2 = "";
	public String contact_mobile1 = "", contact_mobile2 = "";
	public String contact_email1 = "", contact_email2 = "";
	public String contact_address = "", contact_city = "";
	public String contact_city_id = "0", contact_pin = "", contact_state = "";
	public String contact_title = "0";
	public String contact_dob = "", contact_anniversary = "";
	public String contact_title_id = "1";

	// Stock Variables
	public String vehstock_id = "0";
	public String option_id = "0";
	public String so_id = "0";

	// Vehicle Variables
	public String veh_id = "0";
	public String interior = "", exterior = "";
	public String variant_id = "0", variant_name = "", preownedmodel_carmanuf_id = "0";
	public String preownedmodel_name = "", preownedmodel_id = "0", variant_service_code = "";
	public String veh_engine_no = "", veh_reg_no = "0";
	public String veh_modelyear = "";
	public String veh_sale_date = "", veh_insur_date = "";
	public String veh_lastservice = "", veh_lastservice_kms = "0";
	public String soe_name = "", sob_name = "", soe_id = "0", sob_id = "0";
	public String veh_model_id = "0", veh_variant_id = "0", veh_fueltype_id = "0", veh_loanfinancer = "", veh_status_id = "0", veh_status_date = "";
	public String veh_soe_id = "0", veh_buyertype_id = "0", veh_desc = "";
	public String veh_notes = "", veh_priorityveh_id = "0", veh_vehsource_id = "0", vehsource_name = "", vehfollowup_id = "0";
	public String vehfollowup_followup_time = "", due_service = "", last_service_review = "";
	public String veh_chassis_no = "";
	public String veh_kms = "0";
	public String veh_emp_id = "0", veh_emp_name = "";
	public String hrs = "", min = "", day = "", month = "", year = "", servicedueyear = "", veh_service_duekms = "0", veh_service_duedate = "";

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
				upload = PadQuotes(request.getParameter("add_button"));
				Addfile(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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

			boolean isMultipart = ServletFileUpload
					.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize)
						+ (1024 * 1024));
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
					// if (comp_id.equals("1009")) {
					// if (veh_branch_id.equals("1")) {
					// veh_crmemp_id = "741";
					// } else if (veh_branch_id.equals("2")) {
					// veh_crmemp_id = "711";
					// } else if (veh_branch_id.equals("3")) {
					// veh_crmemp_id = "720";
					// } else if (veh_branch_id.equals("4")) {
					// veh_crmemp_id = "720";
					// }
					// }
					veh_crmemp_id = str1[1];
					// SOP("veh_branch_id===" + veh_branch_id);
					SOP("veh_crmemp_id===1====" + veh_crmemp_id);

					// SOP("str1[i]===" + str1[i]);
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
										String fileName1 = VehicleImportPath(comp_id)
												+ fileName;
										getSheetData(fileName1, 0);
										msg += "<br>" + propcount + " DMS imported successfully!";
										msg += "<br>" + updatecount + " DMS updated successfully!";
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../service/veh-follow-up-import-maruti.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (CNumeric(veh_branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (CNumeric(veh_crmemp_id).equals("0")) {
			msg = msg + "<br>Select CRM Executive!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}

	}

	public void getSheetData(String fileName, int sheetIndex)
			throws FileNotFoundException, IOException, InvalidFormatException {
		try {

			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase()
					.equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls
															// type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf("."))
					.toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is
															// .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			if (rowLength > 10000) {
				rowLength = 10000;
			}
			int h = 0;
			int j = 0;
			propcount = 0;
			updatecount = 0;
			for (j = 1; j < rowLength + 1; j++) {
				CheckForm();
				error_msg = "";
				// SOP("rowLength==="+(rowLength+1));
				for (h = 0; h < columnLength + 1; h++) {
					// SOP("columnLength==="+(columnLength+1));

					if (veh_branch_id.equals("-1")) {
						veh_branch_id = "1";
					}

					if (h == 3) {
						vehfollowup_followup_time = "";
						vehfollowup_followup_time = sheetData[j][h];

						if (vehfollowup_followup_time.equals("null") || vehfollowup_followup_time.equals("0") || vehfollowup_followup_time.equals("Due Date")) {
							vehfollowup_followup_time = "";
						}
					}
					// SOP("veh_branch_id======" + veh_branch_id);
					// if (h == 1) {
					// String jc_follow_up_type = "";
					// // SOP("propcount===" + propcount + "--------");
					// jc_follow_up_type = PadQuotes(sheetData[j][h]);
					// if (jc_follow_up_type.equals("null") ||
					// jc_follow_up_type.equals("0") ||
					// jc_follow_up_type.equals("Follow-up Type")) {
					// jc_follow_up_type = "";
					// }
					// SOP("jc_follow_up_type=Begin==" + jc_follow_up_type);
					//
					// }
					//
					// if (h == 2) {
					// String jc_follow_up_no = "";
					// // SOP("propcount===" + propcount + "--------");
					// jc_follow_up_no = PadQuotes(sheetData[j][h]);
					// if (jc_follow_up_no.equals("null") ||
					// jc_follow_up_no.equals("0") ||
					// jc_follow_up_no.equals("Follow-up Number")) {
					// jc_follow_up_no = "";
					// }
					// SOP("jc_follow_up_no===" + jc_follow_up_no);
					//
					// }
					// if (h == 3) {
					// String jc_follow_up_date1 = "";
					// // SOP("propcount===" + propcount + "--------");
					// jc_follow_up_date1 = PadQuotes(sheetData[j][h]);
					// SOP("jc_follow_up_date1===111=" + jc_follow_up_date1);
					// if (jc_follow_up_date1.equals("null") ||
					// jc_follow_up_date1.equals("0") ||
					// jc_follow_up_date1.equals("Follow up")) {
					// jc_follow_up_date1 = "";
					// }
					// if (!jc_follow_up_date1.equals("")) {
					// day = "";
					// month = "";
					// year = "";
					// if (isValidDateFormatShort(jc_follow_up_date1)) {
					// jc_follow_up_date1 =
					// ConvertShortDateToStr(jc_follow_up_date1);
					// } else if (jc_follow_up_date1.split("/").length == 3) {
					// month = jc_follow_up_date1.split("/")[0];
					// if (month.length() == 1) {
					// month = "0" + month;
					// }
					// day = jc_follow_up_date1.split("/")[1];
					// if (day.length() == 1) {
					// day = "0" + day;
					// }
					// year = jc_follow_up_date1.split("/")[2];
					//
					// if (isValidDateFormatShort(day + "/" + month + "/" +
					// year)) {
					// // SOP("inside daymonthyear");
					// jc_follow_up_date1 = year + month + day + "000000";
					// } else {
					// jc_follow_up_date1 = "";
					// }
					// } else {
					// jc_follow_up_date1 = "";
					// }
					// } else {
					// jc_follow_up_date1 = "";
					// }
					// SOP("jc_follow_up_date1===" + jc_follow_up_date1);
					//
					// }
					//
					// if (h == 4) {
					// String jc_follow_up_date2 = "";
					// // SOP("propcount===" + propcount + "--------");
					// jc_follow_up_date2 = PadQuotes(sheetData[j][h]);
					// if (jc_follow_up_date2.equals("null") ||
					// jc_follow_up_date2.equals("0") ||
					// jc_follow_up_date2.equals("Follow up")) {
					// jc_follow_up_date2 = "";
					// }
					//
					// if (!jc_follow_up_date2.equals("")) {
					// day = "";
					// month = "";
					// year = "";
					// if (isValidDateFormatShort(jc_follow_up_date2)) {
					// jc_follow_up_date2 =
					// ConvertShortDateToStr(jc_follow_up_date2);
					// } else if (jc_follow_up_date2.split("/").length == 3) {
					// month = jc_follow_up_date2.split("/")[0];
					// if (month.length() == 1) {
					// month = "0" + month;
					// }
					// day = jc_follow_up_date2.split("/")[1];
					// if (day.length() == 1) {
					// day = "0" + day;
					// }
					// year = jc_follow_up_date2.split("/")[2];
					//
					// if (isValidDateFormatShort(day + "/" + month + "/" +
					// year)) {
					// // SOP("inside daymonthyear");
					// jc_follow_up_date2 = year + month + day + "000000";
					// } else {
					// jc_follow_up_date2 = "";
					// }
					// } else {
					// jc_follow_up_date2 = "";
					// }
					// } else {
					// jc_follow_up_date2 = "";
					// }
					// SOP("jc_follow_up_date2===" + jc_follow_up_date2);
					//
					// }

					if (h == 5) {
						veh_reg_no = "";
						veh_reg_no = PadQuotes(sheetData[j][h]);
						if (veh_reg_no.equals("null") || veh_reg_no.equals("0") || veh_reg_no.equals("Regn. No.")) {
							veh_reg_no = "";
						}
						if (veh_reg_no.contains("#")) {
							veh_reg_no = veh_reg_no.replace("#", "").trim();
						}
						// SOP("veh_reg_no===" + veh_reg_no);
					}

					if (h == 6) {
						variant_service_code = "";
						variant_service_code = PadQuotes(sheetData[j][h]);
						if (variant_service_code.equals("null") || variant_service_code.equals("0") || variant_service_code.equals("Model")) {
							variant_service_code = "";
						}
						// SOP("variant_service_code===" + variant_service_code);
					}

					if (h == 7) {
						String customer_type = "";
						customer_type = PadQuotes(sheetData[j][h]);
						if (customer_type.equals("null") || customer_type.equals("0") || customer_type.equals("Customer Type")) {
							customer_type = "";
						}
						// SOP("customer_type===" + customer_type);
					}

					if (h == 8) {
						veh_chassis_no = "";
						veh_chassis_no = sheetData[j][h];
						if (veh_chassis_no.equals("null") || veh_chassis_no.equals("0") || veh_chassis_no.equals("Chassis No.")) {
							veh_chassis_no = "";
						}
						// SOP("veh_chassis_no==" + veh_chassis_no);
					}

					if (h == 9) {
						veh_kms = "0";
						veh_kms = sheetData[j][h];
						if (veh_kms.equals("null") || veh_kms.equals("") || veh_kms.equals("Mileage")) {
							veh_kms = "0";
						}
						veh_lastservice_kms = veh_kms;
						// SOP("veh_kms===" + veh_kms);
					}

					// Due Date
					// if (h == 10) {
					// vehfollowup_followup_time = "";
					// vehfollowup_followup_time = sheetData[j][h];
					//
					// if (vehfollowup_followup_time.equals("null") || vehfollowup_followup_time.equals("0") || vehfollowup_followup_time.equals("Due Date")) {
					// vehfollowup_followup_time = "";
					// }
					// }
					// SOP("1=======vehfollowup_followup_time======" + vehfollowup_followup_time);
					if (h == 11) {
						due_service = "";
						due_service = sheetData[j][h];
						if (due_service.equals("null") || due_service.equals("0") || due_service.equals("Due Service")) {
							due_service = "";
						}
						// SOP("due_service===" + due_service);
					}

					if (h == 12) {
						last_service_review = "";
						last_service_review = sheetData[j][h];
						if (last_service_review.equals("null") || last_service_review.equals("0") || last_service_review.equals("Last Service")) {
							last_service_review = "";
						}
						// SOP("last_service_review===" + last_service_review);
					}

					if (h == 13) {
						veh_lastservice = "";
						veh_lastservice = sheetData[j][h];
						if (veh_lastservice.equals("null") || veh_lastservice.equals("0") || veh_lastservice.equals("Last Service")) {
							veh_lastservice = "";
						}
						// SOP("veh_lastservice===" + veh_lastservice);
					}

					// if (h == 14) {
					// veh_sale_date = "";
					// veh_sale_date = sheetData[j][h];
					// if (veh_sale_date.equals("null") ||
					// veh_sale_date.equals("0") ||
					// veh_sale_date.equals("Sale Date")) {
					// veh_sale_date = "";
					// }
					// SOP("veh_sale_date==" + veh_sale_date);
					//
					// }

					// if (h == 15) {
					// jc_jccat_name = "";
					// jc_jccat_name = PadQuotes(sheetData[j][h]);
					// if (jc_jccat_name.equals("null") ||
					// jc_jccat_name.equals("Cust. Catg.")) {
					// jc_jccat_name = "";
					// }
					// SOP("jc_jccat_name==" + jc_jccat_name);
					// }

					if (h == 16) {
						customer_name = "";
						customer_name = PadQuotes(sheetData[j][h]);
						if (customer_name.equals("null") || customer_name.equals("0") || customer_name.equals("Customer Name")) {
							customer_name = "";
						}
						if (!customer_name.equals("")) {
							contact_name = customer_name; // only if contact name is empty or not given
						}
						SOP("customer_name====" + customer_name);
					}

					if (h == 17) {
						contact_address = "";
						contact_address = PadQuotes(sheetData[j][h]);
						// SOP("contact_address====" + contact_address);
						if (contact_address.equals("null") || contact_address.equals("0") || contact_address.equals("Address")) {
							contact_address = "";
						}
					}

					if (h == 18) {
						contact_phone1 = "";
						contact_phone1 = sheetData[j][h];
						// SOP("contact_phone1====" + contact_phone1);
						if (contact_phone1.equals("null") || contact_phone1.equals("0") || contact_phone1.equals("Telephone No.")) {
							contact_phone1 = "";
						}
					}

					if (h == 19) {
						contact_mobile1 = "";
						contact_mobile1 = sheetData[j][h];
						// SOP("contact_mobile1====" + contact_mobile1);
						if (contact_mobile1.equals("null") || contact_mobile1.equals("0") || contact_mobile1.equals("Mobile No.")) {
							contact_mobile1 = "";
						}
					}

					if (h == 20) {
						String customer_contact_status = "";
						customer_contact_status = sheetData[j][h];
						if (customer_contact_status.equals("null") || customer_contact_status.equals("0") || customer_contact_status.equals("Customer Contact Status")) {
							customer_contact_status = "";
						}
						// SOP("customer_contact_status===" + customer_contact_status);
					}

					if (h == 25) {
						veh_engine_no = "";
						veh_engine_no = sheetData[j][h];
						// SOP("veh_engine_no===" + veh_engine_no);
						if (veh_engine_no.equals("null") || veh_engine_no.equals("0") || veh_engine_no.equals("Engine No.")) {
							veh_engine_no = "";
						}
						// if (veh_engine_no.equals("") && !veh_chassis_no.equals("")) {
						// error_msg += " " + "Engine No. is empty!<br>";
						// }
						// else if (!veh_engine_no.equals("") && veh_chassis_no.equals("")) {
						// error_msg += " " + "Chassis No. is empty!<br>";
						// }
					}

					if (h == 27) {// /////address
						contact_email1 = "";
						contact_email1 = sheetData[j][h];
						if (!IsValidEmail(contact_email1)) {
							contact_email1 = "";
						}
						// SOP("contact_email1===" + contact_email1);
					}

					if (h == 28) {
						String last_followup_remarks = "";
						last_followup_remarks = sheetData[j][h];
						if (last_followup_remarks.equals("null") || last_followup_remarks.equals("0") || last_followup_remarks.equals("Last Followup Remarks")) {
							last_followup_remarks = "";
						}
						// SOP("last_followup_remarks===" + last_followup_remarks);
					}
					if (h == 31) {// To be confirmed
						veh_sale_date = "";
						veh_sale_date = sheetData[j][h];
						// SOP("veh_sale_date==delivery=" + veh_sale_date);
						if (veh_sale_date.equals("null") || veh_sale_date.equals("0") || veh_sale_date.equals("Delivery Date (Sale)")) {
							veh_sale_date = "";
						}
					}
				}
				veh_entry_date = "";
				veh_entry_date = ToLongDate(kknow());
				// ------------------ Data Validation or Manipulation keep
				// outside inner loop always ----------------------

				// error_msg += (propcount + "") + ".";
				// error_msg += " " + "Jobcard No. is empty!<br>";
				// }
				// SOP("veh_reg_no=====1==" + veh_reg_no);
				if ((!veh_chassis_no.equals("") && !veh_engine_no.equals("")) || !veh_reg_no.equals("")) {
					// Data Validation Start
					if (!veh_lastservice.equals("")) {
						// SOP("veh_lastservice======" + veh_lastservice);
						if (isValidDateFormatShort(veh_lastservice)) {
							veh_lastservice = ConvertShortDateToStr(veh_lastservice);
							// SOP("veh_lastservice=short===" +
							// veh_lastservice);
							veh_lastservice = veh_lastservice.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
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
								// SOP("inside daymonthyear veh_lastservice");
								veh_lastservice = year + month + day + ""
										+ ToLongDate(kknow()).substring(8, 14) + "";
							} else if (isValidDateFormatLong(veh_lastservice)) {
								veh_lastservice = ConvertLongDateToStr(veh_lastservice);
								// SOP("veh_lastservice===long===" +
								// veh_lastservice);
							} else {
								error_msg += " Invalid Veh. last service date!<br>";
							}
							day = "";
							month = "";
							year = "";
						} else {
							error_msg += " Invalid Veh.last service date!<br>";
						}
						// SOP("veh_lastservice=====" + veh_lastservice);
					}

					if (!vehfollowup_followup_time.equals("")) {
						day = "";
						month = "";
						year = "";
						if (isValidDateFormatShort(vehfollowup_followup_time)) {
							vehfollowup_followup_time = ConvertShortDateToStr(vehfollowup_followup_time);
							vehfollowup_followup_time = vehfollowup_followup_time.substring(0, 8) + "110000";
							// SOP("vehfollowup_followup_time==substr=" +
							// vehfollowup_followup_time);
						} else if (vehfollowup_followup_time.split("/").length == 3) {
							month = vehfollowup_followup_time.split("/")[0];
							if (month.length() == 1) {
								month = "0" + month;
							}
							day = vehfollowup_followup_time.split("/")[1];
							if (day.length() == 1) {
								day = "0" + day;
							}
							year = vehfollowup_followup_time.split("/")[2];

							if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
								// SOP("inside daymonthyear");
								vehfollowup_followup_time = year + month + day + "110000";
							} else {
								vehfollowup_followup_time = "";
							}
						} else {
							vehfollowup_followup_time = "";
						}
					} else {
						error_msg += " Invalid Vehicle Follow-up time!<br>";
					}
					if (vehfollowup_followup_time.equals("")) {
						error_msg += " Invalid Vehicle Follow-up time!<br>";
					}
					// SOP("vehfollowup_followup_time===" +
					// vehfollowup_followup_time);

					if (!veh_lastservice.equals("")) {
						int duekms = 0;
						int duecount = 0;
						day = "";
						month = "";
						year = "";
						SOP("veh_lastservice===========" + veh_lastservice);
						if (isValidDateFormatShort(veh_lastservice)) {
							veh_service_duedate = strToShortDate(veh_lastservice);
							SOP("veh_service_duedate===========" + veh_service_duedate);
							day = veh_service_duedate.split("/")[0];
							month = veh_service_duedate.split("/")[1];
							year = veh_service_duedate.split("/")[2];
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
							// SOP("veh_service_duedate=new=" +
							// veh_service_duedate);
							// SOP("veh_service_duekms=new=" + veh_service_duekms);
						}
					} else {
						error_msg += " Invalid Veh.last service date!<br>";
					}
					// SOP("error_msg===" + error_msg);
					if (!variant_service_code.equals("")) {

						variant_id = "0";
						StrSql = "SELECT variant_id"
								+ " FROM axelaauto.axela_preowned_variant"
								+ " WHERE variant_service_code = '" + variant_service_code + "'"
								+ " LIMIT 1";
						// SOP("StrSql===variant by 1.service code==" + StrSql);
						variant_id = CNumeric(ExecuteQuery(StrSql));

						// if (variant_id.equals("0")) {
						// StrSql = "SELECT variant_id" + " FROM " + compdb(comp_id) + "axela_inventory_item"
						// + " WHERE item_name like '" + variant_service_code + "%'"
						// + " LIMIT 1";
						// SOP("StrSql===variant by 2. service code==========" + StrSql);
						// variant_id = CNumeric(ExecuteQuery(StrSql));
						// }

						if (!variant_id.equals("0")) {
							preownedmodel_carmanuf_id = "0";
							StrSql = "SELECT preownedmodel_carmanuf_id"
									+ " FROM axelaauto.axela_preowned_model"
									+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_preownedmodel_id = preownedmodel_id"
									+ " WHERE variant_id = " + variant_id;
							// SOP("StrSql===model_brand_id==" + StrSql);
							preownedmodel_carmanuf_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("model_brand_id=====" +
							// model_brand_id);
						}

					}
					// SOP("variant_id==new=" + variant_id);

					if (!veh_sale_date.equals("")) {
						day = "";
						month = "";
						year = "";
						veh_modelyear = "";
						if (isValidDateFormatShort(veh_sale_date)) {
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
								veh_sale_date = "20140101000000";
								veh_modelyear = "2014";
								// SOP("veh_modelyear=in2==" + veh_modelyear);
							}
						} else {
							veh_sale_date = "20140101000000";
							veh_modelyear = "2014";
							// SOP("veh_modelyear=in3==" + veh_modelyear);
						}
						// SOP("veh_sale_date=222==" + veh_sale_date);
					} else {
						veh_sale_date = "20140101000000";
						veh_modelyear = "2014";

						// SOP("veh_modelyear=in4==" + veh_modelyear);
					}

					// veh_emp_id = "1";

					if (!contact_name.equals("")) {
						contact_title = "0";
						if (contact_name.contains(" ")) {
							contact_title = contact_name.split(" ")[0];

							StrSql = "SELECT title_id FROM  " + compdb(comp_id) + "axela_title"
									+ " WHERE title_desc like '" + contact_title + "%'";
							// SOP("StrSql===contact_title===" + StrSql);
							contact_title = CNumeric(ExecuteQuery(StrSql));
							// SOP("contact_title===" + contact_title);
							if (contact_title.equals("0")) {
								contact_title = "1";
								contact_fname = contact_name;
							} else {
								if (contact_name.split(" ").length > 1) {
									contact_fname = contact_name;
								}
							}
						} else {
							contact_title = "1";
							contact_fname = contact_name;
						}
						contact_name = contact_fname;
					}

					veh_branch_id = CNumeric(veh_branch_id);

					// ------- Data Validation End.....

					// JC Registration No. check....
					// SOP("jc_ro_no=1=" + jc_ro_no);
					// SOP("veh_reg_no=1=" + veh_reg_no);
					// SOP("veh_lastservice=1=" + veh_lastservice);
					// SOP("variant_id=1=" + variant_id);
					// SOP("veh_sale_date=1=" + veh_sale_date);
					if (veh_reg_no.equals("")) {
						error_msg += " Reg. No. is Empty!<br>";
					}
					if (!veh_sale_date.equals("") && !variant_id.equals("0")) {
						CheckForm();
						// SOP("msg=1=" + msg);
						// SOP("error_msg==1=" + error_msg);
						if (msg.equals("") && error_msg.equals("")) {
							try {
								conntx = connectDB();
								conntx.setAutoCommit(false);
								stmttx = conntx.createStatement();
								StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_reg_no = '" + veh_reg_no + "'";
								SOP("StrSql==Veh ID==" + StrSql);
								veh_id = CNumeric(ExecuteQuery(StrSql));

								// Get the veh_id for the respective chassis_no, item_service_code and veh_reg_no is equal to empty
								if (veh_id.equals("0") && !veh_chassis_no.equals("") && !variant_service_code.equals("")) {
									StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
											+ "	INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
											+ " WHERE veh_chassis_no = '" + veh_chassis_no + "'"
											+ "	AND variant_service_code = '" + variant_service_code + "'"
											+ "	AND veh_reg_no = ''"
											+ "	LIMIT 1";
									SOP("StrSql==Veh ID==" + StrSql);
									veh_id = CNumeric(ExecuteQuery(StrSql));
								}

								if (!veh_id.equals("0") && !veh_chassis_no.equals("")) {
									StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
											+ " WHERE veh_chassis_no = '" + veh_chassis_no + "'"
											+ "	AND veh_id = '" + veh_id + "'";
									// SOP("StrSql==chassisid==" + StrSql);
									chassisid = CNumeric(ExecuteQuery(StrSql));
								}

								if (!veh_id.equals("0") && !chassisid.equals("0")) {
									if (!veh_id.equals(chassisid)) {
										errormsg += "<br>" + "Chassis No.: " + veh_chassis_no + " is associated with different Reg No. ";
									} else if (veh_id.equals(chassisid)) {
										// SOP("UpdateVehicle==Veh ID And Chassis Id Same==");
										UpdateVehicle();
									}
								} else if (!veh_id.equals("0") && chassisid.equals("0")) {
									// SOP("UpdateVehicle==Veh ID And Chassis Id not Same==");
									UpdateVehicle();
								} else if (veh_id.equals("0") && chassisid.equals("0")) {
									InsertVehicle();
								} else if (veh_id.equals("0")) {
									InsertVehicle();
								}

								so_id = CNumeric(so_id);
								variant_id = CNumeric(variant_id);
								veh_kms = CNumeric(veh_kms);
								veh_id = CNumeric(veh_id);

								// SOP("veh_id===2==" + veh_id);
								// SOP("variant_id===2==" + variant_id);
								if (!veh_id.equals("0")) {
								} else {
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

					} else {
						count++;
						error_msg += count + ".";
						// if (veh_lastservice.equals("")) {
						// error_msg += " Last service date is Empty!<br>";
						// }
						if (veh_sale_date.equals("")) {
							error_msg += " Veh. Sale Date is Empty!<br>";
						}
						if ((variant_id.equals("0") && variant_service_code.equals(""))) {
							error_msg += "Invalid item service code / item service code is Empty<br>";
						} else if (variant_id.equals("0") && !variant_service_code.equals("")) {
							error_msg += "Invalid item service code ===" + variant_service_code + "<br>";
						}
					}
					// }
				} else if (veh_reg_no.equals("") && !customer_name.equals("")) {
					count++;
					error_msg += count + ".";
					error_msg += " Reg.No. is Empty!<br>";
				}

				if (!error_msg.equals("")) {
					if (!veh_reg_no.equals("")) {
						veh_error_msg += "<br>" + " Registration No.===>" + veh_reg_no + "<br>";
					} else if (veh_reg_no.equals("") && !customer_name.equals("")) {
						veh_error_msg += "<br>" + " Customer===>" + customer_name + "<br>";
					}
					SOP("error_msg===" + error_msg);
					veh_error_msg += error_msg;
				}
				// SOP("********************END*****************************");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void InsertVehicle() {
		try {
			if (!contact_city.equals("")) {
				StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
						+ " WHERE city_name = '" + contact_city + "'";
				contact_city_id = CNumeric(ExecuteQuery(StrSql));
			}
			if (contact_city_id.equals("0")) {
				contact_city_id = "6";
				StrSql = "SELECT city_name FROM " + compdb(comp_id) + "axela_city"
						+ " WHERE city_id = " + contact_city_id + "";
				// SOP("StrSql=city==" + StrSql);
				contact_city = PadQuotes(ExecuteQuery(StrSql));
				contact_pin = "110001";
				// SOP("contact_pin===" + contact_pin);
			}
			if (!contact_city_id.equals("0"))
			{
				StrSql = "SELECT state_name FROM " + compdb(comp_id) + "axela_state"
						+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_state_id = state_id"
						+ " WHERE city_id = " + contact_city_id + "";
				contact_state = PadQuotes(ExecuteQuery(StrSql));
			}

			if (!variant_id.equals("0")) {
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
						+ " customer_since,"
						+ " customer_emp_id,"
						+ " customer_accgroup_id,"
						+ " customer_type,"
						+ " customer_active,"
						+ " customer_notes,"
						+ " customer_entry_id,"
						+ " customer_entry_date)"
						+ " VALUES" + "" + "("
						+ veh_branch_id + ","
						+ " '" + customer_name + "',"
						+ " '" + contact_mobile1 + "',"
						+ " '" + contact_mobile2 + "',"
						+ " '" + contact_email1 + "',"
						+ " '" + contact_email2 + "',"
						+ " '" + contact_address + "',"
						+ " " + contact_city_id + ","
						+ " '" + contact_pin + "',"
						+ " '" + ToShortDate(kknow()) + "',"
						+ " 1," // customer_emp_id
						+ " 32,"// customer_accgroup_id
						+ " 1," // customer_type
						+ " '1'," // customer_active
						+ " ''," // customer_notes
						+ " " + veh_entry_id + ","
						+ " '" + veh_entry_date + "')";
				SOP("StrSql=NEW==customer=========" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					customer_id = rs.getString(1);
				}
				rs.close();

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
						+ " (" + customer_id + ","
						+ " 1," // contact_contacttype_id
						+ " " + CNumeric(contact_title_id) + ","
						+ " '" + contact_fname + "',"
						+ " '" + contact_lname + "',"
						+ " '" + contact_mobile1 + "',"
						+ " '" + contact_mobile2 + "',"
						+ " '" + contact_email1 + "',"
						+ " '" + contact_email2 + "',"
						+ " '" + contact_address + "',"
						+ " " + contact_city_id + ","
						+ " '" + contact_pin + "',"
						+ " ''," // contact_notes
						+ " '1'," // contact_active
						+ " " + veh_entry_id + ","
						+ " '" + veh_entry_date + "')";
				SOP("StrSql=New==contact=========" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					contact_id = rs.getString(1);
				}
				rs.close();
				// contact_id =
				// UpdateQueryReturnID(StrSql);

				customer_id = CNumeric(customer_id);
				contact_id = CNumeric(contact_id);
				so_id = CNumeric(so_id);
				variant_id = CNumeric(variant_id);
				veh_kms = CNumeric(veh_kms);
				veh_service_duekms = CNumeric(veh_service_duekms);

				if (!contact_id.equals("0") && !customer_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
							+ " (veh_branch_id, "
							+ " veh_customer_id,"
							+ " veh_contact_id,"
							+ " veh_so_id,"
							+ " veh_variant_id,"
							+ " veh_modelyear,"
							+ " veh_chassis_no,"
							+ " veh_engine_no,"
							+ " veh_reg_no,"
							+ " veh_sale_date,"
							+ " veh_emp_id,"
							+ " veh_kms,"
							+ " veh_lastservice,"
							+ " veh_lastservice_kms,"
							+ " veh_service_duedate,"
							+ " veh_service_duekms,"
							+ " veh_crmemp_id,"
							+ " veh_insuremp_id,"
							+ " veh_notes,"
							+ " veh_entry_id,"
							+ " veh_entry_date)"
							+ " VALUES"
							+ " (" + veh_branch_id + ","
							+ customer_id + ","
							+ " " + contact_id + ","
							+ " " + so_id + ","
							+ " " + variant_id + ","
							+ " '" + veh_modelyear + "',"
							+ " '" + veh_chassis_no + "',"
							+ " '" + veh_engine_no + "',"
							+ " '" + veh_reg_no.replace(" ", "").replace("-", "") + "',"
							+ " '" + veh_sale_date + "',"
							+ " " + veh_emp_id + ","
							+ " " + veh_kms + ","
							+ " '" + veh_lastservice + "',"
							+ " " + veh_lastservice_kms + ","
							+ " '" + veh_service_duedate + "',"
							+ " " + veh_service_duekms + ","
							+ " " + veh_crmemp_id + ","
							+ " 0," // veh_insuremp_id
							+ " ''," // veh_notes
							+ " " + veh_entry_id + ","
							+ " '" + veh_entry_date + "')";
					SOP("StrSql=New==Vehicle Add=" + StrSqlBreaker(StrSql));
					stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
					rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						veh_id = rs.getString(1);
					}
					rs.close();
				}

				veh_id = CNumeric(veh_id);
				vehstock_id = CNumeric(vehstock_id);

				if (!veh_id.equals("0") && !vehstock_id.equals("0")) {
					StrSql = "INSERT INTO "
							+ compdb(comp_id) + "axela_service_veh_option_trans"
							+ " (vehtrans_option_id,"
							+ " vehtrans_veh_id)"
							+ " SELECT trans_option_id,"
							+ " " + veh_id + ""
							+ " FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
							+ " WHERE trans_vehstock_id = "
							+ vehstock_id + "";
					// SOP("strsql===else===servce veh option trans1======="+StrSqlBreaker(StrSql));
					stmttx.execute(StrSql);

				} else if (!veh_id.equals("0")) {
					option_id = "0";
					if (!preownedmodel_carmanuf_id.equals("0") && !interior.equals("")) {
						StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
								+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
								+ " WHERE option_name = '" + interior + "'"
								+ " AND option_brand_id = " + preownedmodel_carmanuf_id;
						option_id = CNumeric(ExecuteQuery(StrSql));
					};
					option_id = CNumeric(option_id);

					if (!option_id.equals("0")) {
						StrSql = "INSERT INTO "
								+ compdb(comp_id)
								+ "axela_service_veh_option_trans"
								+ " (vehtrans_option_id,"
								+ " vehtrans_veh_id)"
								+ " VALUES" + " ("
								+ option_id + "," + " "
								+ veh_id + ")";
						// SOP("strsql===else===veh option trans1======="+StrSqlBreaker(StrSql));
						stmttx.execute(StrSql);
					}

					// SOP("interior==" + interior +
					// " exterior=="+ exterior);
					if (!interior.equals(exterior)) {
						option_id = "0";
						if (!preownedmodel_carmanuf_id.equals("0") && !exterior.equals("")) {
							StrSql = "SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
									+ " WHERE option_name = '" + exterior + "'"
									+ " AND option_brand_id = " + preownedmodel_carmanuf_id;
							option_id = CNumeric(ExecuteQuery(StrSql));
						}

						if (!option_id.equals("0")) {
							StrSql = "INSERT INTO "
									+ compdb(comp_id)
									+ "axela_service_veh_option_trans"
									+ " (vehtrans_option_id,"
									+ " vehtrans_veh_id)"
									+ " VALUES" + " ("
									+ option_id + "," + " "
									+ veh_id + ")";
							// SOP("strsql======veh option trans2======="+StrSqlBreaker(StrSql));
							stmttx.execute(StrSql);
						}
					}
				}
				// SOP("contact_id==" + contact_id +
				// " customer_id=="+
				// customer_id+"  veh_id==="+veh_id);

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
					// SOP("StrSql==new service veh kms=="
					// + StrSql);
					stmttx.execute(StrSql);
				}
				if (!veh_id.equals("0")) {
					// For updating veh followup time
					// for existing vehicles...

					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_followup"
							+ "	WHERE vehfollowup_desc = ''"
							+ "	AND vehfollowup_veh_id = " + veh_id;
					SOP("StrSql==Delete==veh Followup==" + StrSql);
					stmttx.execute(StrSql);

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
							+ " (vehfollowup_veh_id,"
							+ " vehfollowup_emp_id,"
							+ " vehfollowup_vehcalltype_id,"
							+ " vehfollowup_followup_time,"
							+ " vehfollowup_dueservice,"
							+ " vehfollowup_lastservice,"
							+ " vehfollowup_lastservice_date,"
							+ " vehfollowup_followup_main,"
							+ " vehfollowup_entry_id,"
							+ " vehfollowup_entry_time)"
							+ " VALUES "
							+ " (" + veh_id + ", "
							+ " " + veh_crmemp_id + ","
							+ " 100,"
							+ "'" + vehfollowup_followup_time + "',"
							+ " '" + due_service + "',"
							+ " '" + last_service_review + "',"
							+ " '" + veh_lastservice + "',"
							+ " 1," // vehfollowup_followup_main
							+ " " + veh_entry_id + ","
							+ " '" + veh_entry_date + "')";
					SOP("StrSql==new veh followup==" + StrSql);
					stmttx.execute(StrSql);
				}
				propcount++;
				SOP("propcount==" + propcount);
				conntx.commit();
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

	public void UpdateVehicle() {
		try {

			// SOP("veh already present =" + veh_id +
			// "");
			StrSql = "SELECT veh_customer_id, veh_contact_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " WHERE 1=1";
			if (!veh_reg_no.equals("")) {
				StrSql += " AND veh_reg_no ='" + veh_reg_no + "'";
			}
			// else if (!veh_chassis_no.equals("") && !veh_engine_no.equals("")) {
			// StrSql += " AND CONCAT(veh_chassis_no, '-', veh_engine_no) = '" + veh_chassis_no + "-" + veh_engine_no + "'";
			// }
			SOP("StrSql ==Old reg no veh details==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_id = crs.getString("veh_customer_id");
					contact_id = crs.getString("veh_contact_id");
				}
			}
			crs.close();
			if (!variant_id.equals("0")) {
				if (!contact_city.equals("")) {
					StrSql = "SELECT city_id FROM " + compdb(comp_id) + "axela_city"
							+ " WHERE city_name = '" + contact_city + "'";
					contact_city_id = CNumeric(ExecuteQuery(StrSql));
				}
				if (contact_city_id.equals("0")) {
					contact_city_id = "6";
					StrSql = "SELECT city_name FROM " + compdb(comp_id) + "axela_city"
							+ " WHERE city_id = " + contact_city_id + "";
					// SOP("contact_city===" +
					// contact_city);
					contact_city = PadQuotes(ExecuteQuery(StrSql));
					contact_pin = "110001";
					// SOP("contact_pin===" +
					// contact_pin);
				}
				if (!contact_city_id.equals("0"))
				{
					StrSql = "SELECT state_name FROM " + compdb(comp_id) + "axela_state"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_state_id = state_id"
							+ " WHERE city_id = " + contact_city_id + "";
					contact_state = PadQuotes(ExecuteQuery(StrSql));
				}
				// For updating Customer Details for
				// existing vehicles...
				StrSql = " UPDATE " + compdb(comp_id) + "axela_customer"
						+ " SET customer_branch_id = " + veh_branch_id + ","
						+ " customer_name = '" + customer_name + "',"
						+ " customer_mobile1 = '" + contact_mobile1 + "',"
						+ " customer_mobile2 = '" + contact_mobile2 + "',"
						+ " customer_email1 = '" + contact_email1 + "',"
						+ " customer_email2 = '" + contact_email2 + "',"
						+ " customer_address = '" + contact_address + "',"
						+ " customer_city_id = " + contact_city_id + ","
						+ " customer_pin = " + contact_pin + ","
						+ " customer_soe_id = " + soe_id + ","
						+ " customer_sob_id = " + sob_id + ","
						+ " customer_active = '1',"
						+ " customer_modified_id = " + veh_entry_id + ","
						+ " customer_modified_date = '" + veh_entry_date + "'"
						+ " WHERE 1=1"
						// +
						// " AND veh_vehsource_id = 2 "
						+ " AND customer_id = " + customer_id;
				SOP("StrSql=existing veh Customer update==" + StrSql);
				stmttx.execute(StrSql);

				// For updating Contact Details for
				// existing vehicles...
				StrSql = " UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " SET contact_fname = '" + contact_fname + "',"
						+ " contact_lname = '" + contact_lname + "',"
						+ " contact_mobile1 = '" + contact_mobile1 + "',"
						+ " contact_mobile2 = '" + contact_mobile2 + "',"
						+ " contact_email1 = '" + contact_email1 + "',"
						+ " contact_email2 = '" + contact_email2 + "',"
						+ " contact_address = '" + contact_address + "',"
						+ " contact_city_id = " + contact_city_id + ","
						+ " contact_pin = " + contact_pin + ","
						+ " contact_dob = '" + contact_dob + "',"
						+ " contact_anniversary = '" + contact_anniversary + "',"
						+ " contact_active = '1',"
						+ " contact_modified_id = " + veh_entry_id + ","
						+ " contact_modified_date = '" + veh_entry_date + "'"
						+ " WHERE 1=1"
						// +
						// " AND veh_vehsource_id = 2 "
						+ " AND contact_id = " + contact_id;
				SOP("StrSql=existing veh Contact update==" + StrSql);
				stmttx.execute(StrSql);

				// For updating the recent kms of the
				// vehicle into veh table
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET ";
				if (!veh_branch_id.equals("0")) {
					StrSql += " veh_branch_id = " + veh_branch_id + ",";
				}
				if (!customer_id.equals("0")) {
					StrSql += " veh_customer_id = " + customer_id + ",";
				}
				if (!contact_id.equals("0")) {
					StrSql += " veh_contact_id = " + contact_id + ",";
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
				StrSql += " veh_crmemp_id = " + veh_crmemp_id + ",";
				if (!veh_emp_id.equals("0") || !veh_service_duekms.equals("0") || !veh_service_duedate.equals("")
						|| !veh_lastservice_kms.equals("0") || !veh_lastservice.equals("") || !veh_kms.equals("0")
						|| !veh_engine_no.equals("") || !veh_chassis_no.equals("") || !contact_id.equals("0")
						|| !customer_id.equals("0")) {
					StrSql += " veh_modified_id = " + veh_entry_id + ","
							+ " veh_modified_date  = '" + veh_entry_date + "',";
				}
				StrSql = StrSql.substring(0, StrSql.length() - 1);
				StrSql += " WHERE veh_id =" + veh_id;
				SOP("strsql===existing veh update===" + StrSql);
				stmttx.execute(StrSql);

				// SOP("jc_ro_no_check===" +
				// jc_ro_no_check);
				// SOP("jc_ro_no===" + jc_ro_no);
				if (!contact_id.equals("0") && !customer_id.equals("0") && !veh_id.equals("0")) {

					// For inserting the recent kms of
					// the vehicle into veh_kms table
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
						// SOP("strsql===existing veh kms insert==="
						// + StrSqlBreaker(StrSql));
						stmttx.execute(StrSql);

					}

					// For updating veh followup time
					// for existing vehicles...
					// StrSql = " SELECT vehfollowup_id FROM " + compdb(comp_id) + "axela_service_followup"
					// + " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					// + " WHERE 1=1"
					// // +
					// // " AND veh_vehsource_id = 2 "
					// + " AND DATE_FORMAT(DATE_SUB(vehfollowup_followup_time, INTERVAL 10 DAY),'%Y%m%d') <= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8)"
					// + " AND vehfollowup_veh_id = " + veh_id
					// + " AND vehfollowup_vehcalltype_id = 100";
					// // SOP("StrSql=existing veh follow up check==" + StrSql);
					// vehfollowup_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
					//
					// if (vehfollowup_id.equals("0")) {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_followup"
							+ "	WHERE vehfollowup_desc = ''"
							+ "	AND vehfollowup_veh_id = " + veh_id;
					// SOP("StrSql==Delete==veh Followup==" + StrSql);
					stmttx.execute(StrSql);

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
							+ " (vehfollowup_veh_id,"
							+ " vehfollowup_emp_id,"
							+ " vehfollowup_vehcalltype_id,"
							+ " vehfollowup_followup_time,"
							+ " vehfollowup_dueservice,"
							+ " vehfollowup_lastservice,"
							+ " vehfollowup_lastservice_date,"
							+ " vehfollowup_followup_main,"
							+ " vehfollowup_entry_id,"
							+ " vehfollowup_entry_time)"
							+ " VALUES "
							+ " (" + veh_id + ", "
							+ " " + veh_crmemp_id + ","
							+ " 100,"
							+ "'" + vehfollowup_followup_time + "',"
							+ " '" + due_service + "',"
							+ " '" + last_service_review + "',"
							+ " '" + veh_lastservice + "',"
							+ " 1," // vehfollowup_followup_main
							+ " " + veh_entry_id + ","
							+ " '" + veh_entry_date + "')";
					SOP("StrSql==existing veh followup===" + StrSql);
					stmttx.execute(StrSql);
					// }
				}
				updatecount++;

				conntx.commit();

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
				Str.append(StrSelectdrop(crs.getString("branch_id"),
						veh_branch_id));
				Str.append(">").append(crs.getString("branch_name"))
						.append(" (");
				Str.append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateCRMExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS crmemp_name"
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
				Str.append(">").append(crs.getString("crmemp_name")).append("</option>\n");
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
