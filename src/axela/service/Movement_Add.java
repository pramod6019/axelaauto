package axela.service;
//aJIt

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import cloudify.connect.Connect;

public class Movement_Add extends Connect {

	public String InB = "";
	public String OutB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0", emp_role_id = "0";
	public String vehmove_branch_id = "0";
	public String vehmove_jctype_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String veh_id = "0", vehkms_id = "0";
	public String vehmove_id = "0";
	public String vehmove_reg_no = "";
	public String vehmove_reg_no_in = "";
	public String vehmove_reg_no_out = "";
	public String vehmove_internal = "0";
	public String vehmove_kms_in = "0";
	public String vehmove_kms_out = "0";
	public String BranchAccess = "";
	public String veh_sale_date = "", veh_lastservice = "";

	public String customer_id = "0", contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public InputStream inputStream = null;

	public Connection conntx = null;
	public Statement stmttx = null;
	public PreparedStatement psmt = null;

	public Vehicle_In_Check vehincheck = new Vehicle_In_Check();

	public String contact_mobile1 = "91-", contact_name = "", variant_id = "0";
	public String savePath = "", doc_value = "", uploaddocformat = "";
	public long docsize;
	public String fileName = "", buttonValue = "";
	public String[] docformat;

	protected JobCard_Update JCAdd = new JobCard_Update();

	Map<String, String> fieldsMap = new HashMap<String, String>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// SOP("comp_id--" + comp_id);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_movement_add", request, response);
				Addfile(request, response);
				if (msg.equals("")) {
					msg = PadQuotes(request.getParameter("msg"));
				}
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

	protected void CheckForm() {
		try {
			msg = "";
			if (buttonValue.equals("IN")) {
				if (vehmove_branch_id.equals("0")) {
					msg += "<br>Select Branch!";
				}

				if (vehmove_reg_no_in.equals("")) {
					msg += "<br>Enter Reg. No.!";
				} else if (!IsValidRegNo(vehmove_reg_no_in.replace(" ", ""))) {
					msg += "<br>Enter valid Reg. No.!";
				} else {
					StrSql = "SELECT veh_id, veh_sale_date, veh_lastservice"
							+ " FROM " + compdb(comp_id) + "axela_service_veh"
							+ " WHERE veh_reg_no = '" + vehmove_reg_no_in.replace(" ", "") + "'";
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							veh_id = CNumeric(PadQuotes(crs.getString("veh_id")));
							veh_sale_date = PadQuotes(crs.getString("veh_sale_date"));
							if (veh_sale_date.equals("")) {
								veh_sale_date = "20140101000000";
							}
							veh_lastservice = PadQuotes(crs.getString("veh_lastservice"));
							if (veh_lastservice.equals("")) {
								veh_lastservice = veh_sale_date;
							}
						}
					}
					crs.close();

					StrSql = "SELECT vehmove_id FROM " + compdb(comp_id) + "axela_service_veh_move"
							+ " WHERE vehmove_reg_no = '" + vehmove_reg_no_in.replace(" ", "") + "'"
							+ " AND vehmove_branch_id = " + vehmove_branch_id + ""
							+ " AND vehmove_timeout = ''";
					if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
						msg += "<br>Vehicle is already present!";
					}
					if (veh_id.equals("0")) {
						if (contact_title_id.equals("0")) {
							msg = msg + "<br>Select Title!";
						}
						if (contact_name.equals("")) {
							msg = msg + "<br>Enter Contact name!";
						}
						if (variant_id.equals("0")) {
							msg = msg + "<br>Select Variant!";
						}
					} else {
						contact_title_id = "0";
						contact_name = "";
						variant_id = "";
					}

					// // SOP("inputStream.available()===" + inputStream.available());
					// // SOP("docsize===" + docsize);
					// if (((inputStream.available()) > docsize) && !fileName.equals("")) {
					// msg = msg + "<br>Image size should not exceed " + 2 + " Mb!";
					// }
					if (!fileName.equals("") && fileName.contains(".")) {
						int pos = fileName.lastIndexOf(".");
						// img_value.lastIndexOf(".")).toLowerCase());
						if (!ImageFormats.contains(fileName.substring(
								fileName.lastIndexOf(".")).toLowerCase())) {
							msg = msg + "<br>Unable to upload"
									+ fileName.substring(fileName.lastIndexOf("."))
									+ " format";
						}
					}
					else {
						msg = msg + "<br>Invalid Format";
					}
				}
				if (vehmove_jctype_id.equals("0")) {
					msg += "<br>Select JC Type!";
				}

				if (contact_mobile1.equals("")) {
					msg = msg + "<br>Enter Mobile No.!";
				} else {
					if (!IsValidMobileNo11(contact_mobile1)) {
						msg = msg + "<br>Enter Valid Contact Mobile 1!";
					}
				}
			}

			if (buttonValue.equals("OUT")) {
				if (vehmove_branch_id.equals("0")) {
					msg += "<br>Select Branch!";
				}

				if (vehmove_reg_no_out.equals("")) {
					msg += "<br>Enter Reg. No.!";
				} else if (!IsValidRegNo(vehmove_reg_no_out.replace(" ", ""))) {
					msg += "<br>Enter valid Reg. No.!";

					StrSql = "SELECT vehmove_id FROM " + compdb(comp_id) + "axela_service_veh_move"
							+ " WHERE vehmove_reg_no = '" + vehmove_reg_no_out.replace(" ", "") + "'"
							+ " AND vehmove_branch_id = " + vehmove_branch_id + ""
							+ " AND vehmove_timein != ''"
							+ " AND vehmove_timeout = ''";
					if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
						msg += "<br>Invalid Vehicle!";
					}
				}

				if (vehmove_kms_out.equals("0")) {
					msg += "<br>Enter KMS OUT!";
				}
			}

			// if (vehmove_kms_in.equals("0")) {
			// msg += "<br>Enter KMS IN!";
			// }

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

	}

	protected void AddVehMoveFields(HttpServletRequest request) throws IOException, ServletException, SQLException {
		if (msg.equals("")) {
			try {
				if (vehmove_internal == null) {
					vehmove_internal = "0";
				}

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_move"
						+ " (vehmove_branch_id,"
						+ " vehmove_jctype_id,"
						+ " vehmove_veh_id,"
						+ " vehmove_reg_no,"
						+ " vehmove_mobile1,"
						+ " vehmove_internal,"
						+ " vehmove_kms_in,"
						+ " vehmove_timein,"
						+ " vehmove_timein_entry_id)"
						+ " VALUES"
						+ " (" + vehmove_branch_id + ","
						+ " " + vehmove_jctype_id + ","
						+ " COALESCE((SELECT veh_id FROM " + compdb(comp_id) + " axela_service_veh"
						+ " WHERE veh_reg_no ='" + vehmove_reg_no_in + "'), 0),"
						+ " '" + vehmove_reg_no_in.toUpperCase().replace(" ", "") + "',"
						+ " '" + contact_mobile1 + "',"
						+ " '" + vehmove_internal + "',"
						+ " " + vehmove_kms_in + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " " + emp_id + ")";

				// vehmove_id = UpdateQueryReturnID(StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					vehmove_id = rs.getString(1);
				}

				if (!veh_id.equals("0") && !vehmove_internal.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
							+ " (vehkms_id,"
							+ " vehkms_veh_id,"
							+ " vehkms_kms,"
							+ " vehkms_entry_id,"
							+ " vehkms_entry_date)"
							+ " VALUES"
							+ " (" + vehkms_id + ","
							+ " " + veh_id + ","
							+ " " + vehmove_kms_in + ","
							+ " " + emp_id + ","
							+ " '" + veh_lastservice + "')";
					// // SOP("internal==" + StrSql);
					updateQuery(StrSql);
				}
				// // SOP("veh_id=in add fields=" + veh_id);
				// if (!veh_id.equals("0")) {
				// }
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
	}

	protected void AddJobCard(HttpServletRequest request) {
		try {
			if (!veh_id.equals("0")) {
				StrSql = "SELECT veh_customer_id, veh_contact_id, veh_id, veh_reg_no, contact_fname, contact_mobile1,"
						+ " contact_lname, customer_address, city_name, customer_pin, state_name, contact_email1,"
						+ " (SELECT COALESCE((SELECT COALESCE(location_id, 0) FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + vehmove_branch_id + " LIMIT 1), 0)) AS jc_location_id"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
						+ " WHERE veh_id = " + veh_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					JCAdd.comp_id = comp_id;
					JCAdd.jc_branch_id = vehmove_branch_id;
					JCAdd.jcdate = ToLongDate(kknow());
					JCAdd.contact_fname = crs.getString("contact_fname");
					JCAdd.contact_lname = crs.getString("contact_lname");
					JCAdd.contact_mobile1 = crs.getString("contact_mobile1");
					JCAdd.contact_email1 = crs.getString("contact_email1");
					JCAdd.jc_customer_id = crs.getString("veh_customer_id");
					JCAdd.jc_contact_id = crs.getString("veh_contact_id");
					JCAdd.jc_veh_id = crs.getString("veh_id");
					JCAdd.jc_vehmove_id = vehmove_id;
					JCAdd.jc_reg_no = crs.getString("veh_reg_no");
					JCAdd.jc_type_id = "5";
					JCAdd.jc_cat_id = "3";
					JCAdd.jc_kms = vehmove_kms_in;
					JCAdd.jc_fuel_guage = "0";
					JCAdd.jc_bill_address = crs.getString("customer_address");
					JCAdd.jc_bill_city = crs.getString("city_name");
					JCAdd.jc_bill_pin = crs.getString("customer_pin");
					JCAdd.jc_bill_state = crs.getString("state_name");
					JCAdd.jc_del_address = crs.getString("customer_address");
					JCAdd.jc_del_city = crs.getString("city_name");
					JCAdd.jc_del_pin = crs.getString("customer_pin");
					JCAdd.jc_del_state = crs.getString("state_name");
					JCAdd.jc_title = "JOB CARD";
					JCAdd.jc_cust_voice = "";
					JCAdd.jc_advice = "";
					JCAdd.jc_terms = "";
					JCAdd.jc_inventory = "";
					JCAdd.jc_instructions = "";
					JCAdd.jc_emp_id = "136";
					JCAdd.jc_technician_emp_id = "0";
					JCAdd.jc_location_id = crs.getString("jc_location_id");
					JCAdd.jc_ro_no = "";
					JCAdd.jc_promisetime = ToLongDate(AddHoursDate(kknow(), 0, 4, 0));
					JCAdd.jc_readytime = "";
					JCAdd.jc_deliveredtime = "";
					JCAdd.jc_stage_id = "9";
					JCAdd.jc_priorityjc_id = "1";
					JCAdd.jc_critical = "0";
					JCAdd.jc_active = "1";
					JCAdd.jc_notes = "";
					JCAdd.jc_entry_id = emp_id;
					JCAdd.emp_id = emp_id;
					JCAdd.jc_entry_date = ToLongDate(kknow());
				}
				crs.close();
				JCAdd.PopulateConfigDetails();
			} else {
				JCAdd.comp_id = comp_id;
				JCAdd.jc_branch_id = vehmove_branch_id;
				JCAdd.jcdate = ToLongDate(kknow());
				JCAdd.jc_customer_id = "0";
				JCAdd.jc_contact_id = "0";
				JCAdd.jc_veh_id = "0";
				JCAdd.jc_vehmove_id = vehmove_id;
				JCAdd.jc_reg_no = vehmove_reg_no.toUpperCase().replace(" ", "");
				JCAdd.jc_type_id = "5";
				JCAdd.jc_cat_id = "3";
				JCAdd.jc_kms = vehmove_kms_in;
				JCAdd.jc_fuel_guage = "0";
				JCAdd.jc_bill_address = "";
				JCAdd.jc_bill_city = "";
				JCAdd.jc_bill_pin = "";
				JCAdd.jc_bill_state = "";
				JCAdd.jc_del_address = "";
				JCAdd.jc_del_city = "";
				JCAdd.jc_del_pin = "";
				JCAdd.jc_del_state = "";
				JCAdd.jc_title = "JOB CARD";
				JCAdd.jc_cust_voice = "";
				JCAdd.jc_advice = "";
				JCAdd.jc_terms = "";
				JCAdd.jc_inventory = "";
				JCAdd.jc_instructions = "";
				JCAdd.jc_emp_id = "136";
				JCAdd.jc_technician_emp_id = "0";
				StrSql = "SELECT COALESCE((SELECT COALESCE(location_id, 0) FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + vehmove_branch_id + " LIMIT 1), 0)";
				JCAdd.jc_location_id = CNumeric(ExecuteQuery(StrSql));
				JCAdd.jc_ro_no = "";
				JCAdd.jc_promisetime = ToLongDate(AddHoursDate(kknow(), 0, 4, 0));
				JCAdd.jc_readytime = "";
				JCAdd.jc_deliveredtime = "";
				JCAdd.jc_stage_id = "9";
				JCAdd.jc_priorityjc_id = "1";
				JCAdd.jc_critical = "0";
				JCAdd.jc_active = "1";
				JCAdd.jc_notes = "";
				JCAdd.jc_entry_id = emp_id;
				JCAdd.jc_entry_date = ToLongDate(kknow());
			}
			JCAdd.AddFields(request, "vehmove");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		// // SOP("in update");
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "SELECT vehmove_id FROM " + compdb(comp_id) + "axela_service_veh_move"
						+ " WHERE vehmove_reg_no = '" + vehmove_reg_no_out.replace(" ", "") + "'"
						+ " AND vehmove_branch_id = " + vehmove_branch_id + ""
						+ " AND vehmove_timeout = ''";
				vehmove_id = CNumeric(ExecuteQuery(StrSql));

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh_move"
						+ " SET"
						// + " vehmove_internal = '" + vehmove_internal + "',"
						+ " vehmove_kms_out = '" + vehmove_kms_out + "',"
						+ " vehmove_timeout = '" + ToLongDate(kknow()) + "',"
						+ " vehmove_timeout_entry_id = " + emp_id + ""
						+ " WHERE vehmove_id = " + vehmove_id + "";
				updateQuery(StrSql);

				if (!veh_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
							+ " (vehkms_veh_id,"
							+ " vehkms_kms,"
							+ " vehkms_entry_id,"
							+ " vehkms_entry_date)"
							+ " VALUES"
							+ " (" + veh_id + ","
							+ " " + vehmove_kms_out + ","
							+ " " + emp_id + ","
							+ " '" + veh_lastservice + "')";
					updateQuery(StrSql);
				}

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,3)"
					// + BranchAccess // commented this line
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Branch</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), vehmove_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJCType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(StrSelectdrop(crs.getString("jctype_id"), vehmove_jctype_id));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			savePath = VehicleImportPath(comp_id);
			uploaddocformat = ".jpg, .jpeg, .gif, .png";
			docsize = 1024 * 1024 * 2;// hardcoded to 2MB
			uploaddocformat = ".jpg, .jpeg, .gif, .png";
			msg = "";

			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));

				File f = new File(savePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				factory.setRepository(f);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax((1024 * 1024 * (int) docsize) + (1024 * 1024));

				List<FileItem> items = upload.parseRequest(request);

				// put formfields into map except filename
				for (FileItem item : items) {
					if (item.isFormField()) {
						fieldsMap.put(item.getFieldName(), item.getString());
					} else {
						fileName = item.getName();
						// File upload_doc = new File(savePath + fileName);

						inputStream = item.getInputStream();
					}
				}
				// setting checkbox=1 if checkbox's value is on
				if (fieldsMap.containsKey("chk_vehmove_internal_in")) {
					if (fieldsMap.get("chk_vehmove_internal_in").equals("on")) {
						fieldsMap.put("chk_vehmove_internal_in", "1");
					}
				} else {
					fieldsMap.put("chk_vehmove_internal_in", "0");
				}

				if (fieldsMap.containsKey("inbutton")) {
					buttonValue = fieldsMap.get("inbutton");
				} else {
					buttonValue = fieldsMap.get("outbutton");
				}

				if (buttonValue.equals("IN")) {
					vehmove_branch_id = fieldsMap.get("dr_vehmove_branch_id");
					vehmove_reg_no_in = fieldsMap.get("txt_vehmove_in_reg_no");
					vehmove_kms_in = CNumeric(fieldsMap.get("txt_vehmove_kms_in"));
					vehmove_jctype_id = fieldsMap.get("dr_vehmove_jc_id");
					vehmove_internal = fieldsMap.get("chk_vehmove_internal_in");
					contact_mobile1 = fieldsMap.get("txt_contact_mobile1");
					contact_title_id = fieldsMap.get("dr_title");
					contact_name = fieldsMap.get("txt_contact_name");
					if (fieldsMap.containsKey("servicevariant")) {
						variant_id = fieldsMap.get("servicevariant");
					}

					if (!fileName.equals("")) {
						docformat = uploaddocformat.split(", ");
						for (int j = 0; j < docformat.length; j++) {
							if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
								msg += "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format!";
							}
						}

						CheckForm();

						if (!veh_id.equals("0")) {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							AddVehMoveFields(request);
							AddDocument();
							conntx.commit();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("movement-add.jsp?msg=Vehicle added Successfully!"));
							}
						} else {
							if (msg.equals("")) {
								conntx = connectDB();
								conntx.setAutoCommit(false);
								stmttx = conntx.createStatement();
								AddVehicle(request);
								AddVehMoveFields(request);
								AddDocument();
								conntx.commit();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("movement-add.jsp?msg=Vehicle added Successfully!"));
								}
							}
						}
					} else {
						msg = msg + "<br>Select a document!";
						msg = "Error!" + msg;
					}
				}
				else if (buttonValue.equals("OUT")) {
					vehmove_reg_no_out = fieldsMap.get("txt_vehmove_out_reg_no");
					vehmove_kms_out = fieldsMap.get("txt_vehmove_kms_out");
					UpdateFields();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						response.sendRedirect(response.encodeRedirectURL("movement-add.jsp?msg=Vehicle Updated Successfully!"));
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../service/movement-add.jsp?msg=" + msg);
		} finally {
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
		}

	}
	public void AddCustomer() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_since,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_entry_id,"
					+ " customer_entry_date)"
					+ " VALUES"
					+ " ('" + vehmove_branch_id + "',"
					+ " '" + contact_name + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + ToShortDate(kknow()) + "',"
					+ " 32,"// customer_accgroup_id
					+ " 1," // customer_type
					+ " '1',"// customer_active
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();

			while (rs.next()) {
				customer_id = rs.getString(1);
			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
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

	public void AddContact() throws SQLException {
		try {

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_mobile1,"
					+ " contact_notes,"
					+ " contact_active,"
					+ " contact_entry_id,"
					+ " contact_entry_date)"
					+ " VALUES"
					+ " (" + customer_id + ","
					+ " 1,"
					+ " " + contact_title_id + ","
					+ " '" + contact_name + "',"
					+ " '',"// contact_lname
					+ " '" + contact_mobile1 + "',"
					+ " '',"
					+ " '1',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				contact_id = rs.getString(1);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
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

	public void AddVehicle(HttpServletRequest request) throws SQLException {
		CheckForm();

		if (msg.equals("")) {
			try {
				AddCustomer();
				AddContact();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
						+ " (veh_branch_id,"
						+ " veh_customer_id,"
						+ " veh_contact_id,"
						+ " veh_variant_id,"
						+ " veh_reg_no,"
						+ " veh_sale_date,"
						+ " veh_lastservice_kms,"
						+ " veh_emp_id,"
						+ " veh_notes,"
						+ " veh_entry_id,"
						+ " veh_entry_date)"
						+ " VALUES"
						+ " ('" + vehmove_branch_id + "',"
						+ " " + customer_id + ","
						+ " " + contact_id + ","
						+ " " + variant_id + ","
						+ " '" + vehmove_reg_no_in.toUpperCase().replaceAll(" ", "") + "',"
						+ " '" + veh_sale_date + "',"
						+ " " + vehmove_kms_in + ","
						+ " " + emp_id + ","
						+ " '',"
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					veh_id = rs.getString(1);
				}

				if ((!CNumeric(vehmove_kms_in).equals("0") && !CNumeric(veh_id).equals("0")) || (CNumeric(vehmove_kms_in).equals("0") && !CNumeric(veh_id).equals("0"))) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
							+ " (vehkms_veh_id,"
							+ " vehkms_kms,"
							+ " vehkms_entry_id,"
							+ " vehkms_entry_date)"
							+ " VALUES"
							+ " (" + veh_id + ","
							+ " " + CNumeric(vehmove_kms_in) + ","
							+ " " + emp_id + ","
							+ " '" + ToShortDate(kknow()) + "')";
					stmttx.execute(StrSql);
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
	}

	public String PopulateTitle(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	protected void AddDocument() throws IOException, ServletException, SQLException {
		if (msg.equals("")) {
			try {

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_insur_img"
						+ "(insurimg_veh_id,"
						+ " insurimg_value,"
						+ " insurimg_data,"
						+ " insurimg_entry_id,"
						+ " insurimg_entry_date"
						+ " )"
						+ "values"
						+ " (?,?,?,?,?)";
				PreparedStatement psmt = conntx.prepareStatement(StrSql);
				psmt.setString(1, veh_id);
				psmt.setString(2, fileName);

				System.out.println("inputStream---" + inputStream);
				if (inputStream != null) {
					psmt.setBlob(3, inputStream);
				}
				psmt.setString(4, emp_id);
				psmt.setString(5, ToLongDate(kknow()));
				psmt.executeUpdate();

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
				if (psmt != null) {
					psmt.close();
				}

			}
		}
	}

}
