//Shivaprasad 6/07/2015   
package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
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

public class Service_Target_User_Import extends Connect {

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
	public String service_target_entry_id = "0";
	public String service_target_entry_date = "";
	public String BranchAccess = "", ExeAccess = "";
	public String upload = "";
	public String error_msg = "";
	public String target_error_msg = "";
	public int targetcolumnLength = 0;
	public int count = 0;
	HashMap<String, String> mapronum = new HashMap<String, String>();

	// Voucher Variables
	public String service_target_emp_id = "0", service_target_jc_count = "0", service_target_pms_count = "0";
	public String service_target_labour_amount = "0", service_target_parts_amount = "0", service_target_oil_amount = "0", service_target_tyre_count = "0";
	public String service_target_tyre_amount = "0", service_target_break_count = "0", service_target_break_amount = "0", service_target_battery_count = "0";
	public String service_target_vas_amount = "0", service_target_battery_amount = "0", service_target_accessories_amount = "0";
	public String service_target_extwarranty_count = "0", service_target_extwarranty_amount = "0", service_target_wheelalign_amount = "0";
	public String service_target_cng_amount = "0", service_target_id = "0", emp_name = "";
	public String branch_id = "0", vouchertype_name = "", voucher_ref_no = "", voucher_location_id = "0", voucher_customer_id = "0";
	public String voucher_contact_id = "0";
	// Vehicle Variables
	public String veh_chassis_no = "";

	public String hrs = "", min = "", day = "", month = "", year = "", location_name = "";
	public int curryear = 0, currmonth = 0;
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "service_target_edit", request, response);
				service_target_entry_id = emp_id;
				upload = PadQuotes(request.getParameter("add_button"));
				Addfile(request, response);
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			savePath = VehicleImportPath(comp_id);
			docsize = 1;
			importdocformat = ".xls, .xlsx";

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
					branch_id = str1[0];
					month = str1[1];
					year = str1[2];
					System.out.println("str1" + str1[1] + "str2" + str1[2] + "str3" + str1[3] + "str4" + str1[4] + "str5" + str1[5] + "str6" + str1[6] + "str7" + str1[7] + "str8" + str1[8]);
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
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf("."))
													+ " format!";
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
										String fileName1 = VehicleImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0, request, response);
										// SOP("msg==" + msg);
										if (msg.equals("")) {
											msg += "<br>" + propcount + " Target(s) Imported Successfully!";
											msg += "<br>" + updatecount + " Target(s) Updated Successfully!";
											if (!target_error_msg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + target_error_msg;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../service/service-target-user-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(branch_id).equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		// if (Long.parseLong(year + doublenum(Integer.parseInt(month))) < Integer.parseInt(ToLongDate(kknow()).substring(0, 6))) {
		// msg = msg + "<br>Target Month cannot be less than Current Month!";
		// }
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}
	public void getSheetData(String fileName, int sheetIndex, HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is.xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}
			if (rowLength > 1000) {
				rowLength = 1000;
			}
			int h = 0;
			int j = 0;
			count = 0;
			propcount = 0;
			updatecount = 0;
			targetcolumnLength = 18;
			SOP("columnLength===" + columnLength);
			if (columnLength != targetcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
			else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {

						// Service Advisor
						if (h == 0) {
							service_target_emp_id = "0";
							emp_name = "";
							emp_name = PadQuotes(sheetData[j][h]);
							if (!emp_name.equals("")) {
								if (emp_name.contains("(")) {
									emp_name = emp_name.replace("(", "&#40;");
								}
								if (emp_name.contains(")")) {
									emp_name = emp_name.replace(")", "&#41;");
								}
								StrSql = "SELECT emp_id"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " WHERE emp_name = '" + emp_name + "'"
										+ " AND emp_service = 1"
										+ " AND emp_branch_id = " + branch_id
										+ " LIMIT 1";
								// SOP("StrSql===" + StrSql);
								service_target_emp_id = CNumeric(ExecuteQuery(StrSql));
							}
							if (service_target_emp_id.equals("0")) {
								error_msg += "No Service Advisor found with name: " + emp_name + "<br>";
							}
						}
						// Job Card Count
						if (h == 1) {
							service_target_jc_count = "0";
							service_target_jc_count = CNumeric(sheetData[j][h]);
						}
						// PMS Count
						if (h == 2) {
							service_target_pms_count = "0";
							service_target_pms_count = CNumeric(sheetData[j][h]);
						}
						// Labour Amount
						if (h == 3) {
							service_target_labour_amount = "0";
							service_target_labour_amount = CNumeric(sheetData[j][h]);
						}
						// Parts Amount
						if (h == 4) {
							service_target_parts_amount = "0";
							service_target_parts_amount = CNumeric(sheetData[j][h]);
						}
						// Oil Amount
						if (h == 5) {
							service_target_oil_amount = "0";
							service_target_oil_amount = CNumeric(sheetData[j][h]);
						}
						// CNG Amount
						if (h == 6) {
							service_target_cng_amount = "0";
							service_target_cng_amount = CNumeric(sheetData[j][h]);
						}
						// Tyre Count
						if (h == 7) {
							service_target_tyre_count = "0";
							service_target_tyre_count = CNumeric(sheetData[j][h]);

						}
						// // Tyre Amount
						if (h == 8) {
							service_target_tyre_amount = "0";
							service_target_tyre_amount = CNumeric(sheetData[j][h]);
						}
						// Brake Count
						if (h == 9) {
							service_target_break_count = "0";
							service_target_break_count = CNumeric(sheetData[j][h]);

						}
						// Brake Amount
						if (h == 10) {
							service_target_break_amount = "0";
							service_target_break_amount = CNumeric(sheetData[j][h]);
						}
						// Battery Count
						if (h == 11) {
							service_target_battery_count = "0";
							service_target_battery_count = CNumeric(sheetData[j][h]);
						}
						// Battery Amount
						if (h == 12) {
							service_target_battery_amount = "0";
							service_target_battery_amount = CNumeric(sheetData[j][h]);
						}
						// Accessories Amount
						if (h == 13) {
							service_target_accessories_amount = "0";
							service_target_accessories_amount = CNumeric(sheetData[j][h]);
						}
						// VAS Amount
						if (h == 14) {
							service_target_vas_amount = "0";
							service_target_vas_amount = CNumeric(sheetData[j][h]);
						}
						// Extended Warranty Count
						if (h == 15) {
							service_target_extwarranty_count = "0";
							service_target_extwarranty_count = CNumeric(sheetData[j][h]);
						}
						// Extended Warranty Amount
						if (h == 16) {
							service_target_extwarranty_amount = "0";
							service_target_extwarranty_amount = CNumeric(sheetData[j][h]);
						}
						// Wheel Alignment Amount
						if (h == 17) {
							service_target_wheelalign_amount = "0";
							service_target_wheelalign_amount = CNumeric(sheetData[j][h]);
						}
					}
					service_target_entry_date = ToLongDate(kknow());
					if (msg.equals("") && error_msg.equals("")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							StrSql = "SELECT service_target_id"
									+ " FROM " + compdb(comp_id) + "axela_service_target"
									+ " WHERE  SUBSTR(service_target_startdate,1,6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
									+ " AND SUBSTR(service_target_enddate,1,6) = '" + year + doublenum(Integer.parseInt(month)) + "'"
									+ " AND service_target_emp_id = " + service_target_emp_id;
							service_target_id = CNumeric(ExecuteQuery(StrSql));
							if (service_target_id.equals("0")) {
								AddServiceTarget();
								propcount++;
							} else if (!service_target_id.equals("0")) {
								UpdateServiceTarget(service_target_id);
								updatecount++;
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
					} else if (!error_msg.equals("")) {
						if (!emp_name.equals("")) {
							target_error_msg += ++count + "." + error_msg;
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public void UpdateServiceTarget(String service_target_id) {
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_target "
					+ " SET"
					+ " service_target_jc_count= " + service_target_jc_count + ", "
					+ " service_target_pms_count= " + service_target_pms_count + ", "
					+ " service_target_labour_amount= " + service_target_labour_amount + ", "
					+ " service_target_parts_amount= " + service_target_parts_amount + ", "
					+ " service_target_oil_amount= " + service_target_oil_amount + ", "
					+ " service_target_tyre_count= " + service_target_tyre_count + ", "
					+ " service_target_tyre_amount= " + service_target_tyre_amount + ", "
					+ " service_target_break_count= " + service_target_break_count + ", "
					+ " service_target_break_amount= " + service_target_break_amount + ", "
					+ " service_target_battery_count= " + service_target_battery_count + ", "
					+ " service_target_battery_amount= " + service_target_battery_amount + ", "
					+ " service_target_accessories_amount= " + service_target_accessories_amount + ", "
					+ " service_target_vas_amount= " + service_target_vas_amount + ", "
					+ " service_target_extwarranty_count= " + service_target_extwarranty_count + ", "
					+ " service_target_extwarranty_amount= " + service_target_extwarranty_amount + ", "
					+ " service_target_wheelalign_amount= " + service_target_wheelalign_amount + ", "
					+ " service_target_cng_amount= " + service_target_cng_amount + ", "
					+ " service_target_modified_id = " + service_target_entry_id + ", "
					+ "service_target_modified_date=" + service_target_entry_date + " "
					+ " WHERE service_target_id=" + service_target_id + "";
			// SOP("Strsql update targets===" + StrSql);
			stmttx.execute(StrSql);
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
	public void AddServiceTarget() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_service_target"
					+ " ("
					+ " service_target_emp_id,"
					+ " service_target_startdate,"
					+ " service_target_enddate,"
					+ " service_target_jc_count,"
					+ " service_target_pms_count,"
					+ " service_target_labour_amount,"
					+ " service_target_parts_amount,"
					+ " service_target_oil_amount,"
					+ " service_target_tyre_count,"
					+ " service_target_tyre_amount,"
					+ " service_target_break_count,"
					+ " service_target_break_amount,"
					+ " service_target_battery_count,"
					+ " service_target_battery_amount,"
					+ " service_target_accessories_amount,"
					+ " service_target_vas_amount,"
					+ " service_target_extwarranty_count,"
					+ " service_target_extwarranty_amount,"
					+ " service_target_wheelalign_amount,"
					+ " service_target_cng_amount,"
					+ " service_target_entry_id,"
					+ " service_target_entry_date )"
					+ " VALUES" + " ("
					+ " " + service_target_emp_id + ","
					+ " '" + year + doublenum(Integer.parseInt(month)) + "01000000" + "',"
					+ " '" + year + doublenum(Integer.parseInt(month)) + "30000000" + "',"
					+ " " + service_target_jc_count + ","
					+ " " + service_target_pms_count + ","
					+ " " + service_target_labour_amount + ","
					+ " " + service_target_parts_amount + ","
					+ " " + service_target_oil_amount + ","
					+ " " + service_target_tyre_count + ","
					+ " " + service_target_tyre_amount + ","
					+ " " + service_target_break_count + ","
					+ " " + service_target_break_amount + ","
					+ " " + service_target_battery_count + ","
					+ " " + service_target_battery_amount + ","
					+ " " + service_target_accessories_amount + ","
					+ " " + service_target_vas_amount + ","
					+ " " + service_target_extwarranty_count + ","
					+ " " + service_target_extwarranty_amount + ","
					+ " " + service_target_wheelalign_amount + ","
					+ " " + service_target_cng_amount + ","
					+ " " + service_target_entry_id + ","
					+ " '" + service_target_entry_date + "')";
			// SOP("Strsql insert targets===" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				service_target_id = rs.getString(1);
			}
			rs.close();
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
	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			SqlStr += " ORDER BY branch_name";
			SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = curryear; i <= curryear + 10; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateMonth() {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = 1; i <= 12; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), month));
				Str.append(">").append(TextMonth(i - 1)).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}