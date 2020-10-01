//Shivaprasad 6/07/2015   
package axela.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
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

public class Vehicle_DMS_Status_User_Import extends Connect {

	public String StrSql = "";
	public String msg = "", emp_id = "0", errormsg = "";
	public String comp_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public int propcount = 0;
	public String BranchAccess = "";
	public String branch_id = "0";
	public String upload = "";
	public int count = 0;
	public String veh_error_msg = "", error_msg = "";

	public String customer_name = "";
	public String contact_phone1 = "";
	public String contact_mobile1 = "";
	public String contact_address = "";

	// Vehicle Variables
	public String veh_id = "0", veh_dmsstatus_id = "", dmsstatus_id = "";
	public String variant_id = "0", variant_name = "", dmsstatus = "";
	public String model_name = "", model_id = "0", variant_service_code = "";
	public String veh_chassis_no = "", veh_reg_no = "";
	public String veh_modelyear = "";
	public String veh_sale_date = "";
	public String veh_model_id = "0", veh_variant_id = "0";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
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
					dmsstatus_id = str1[0];
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
										String fileName1 = VehicleImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										if (msg.equals("")) {
											msg += "<br>" + propcount + " Vehicle DMS Status Imported successfully!";
											if (!veh_error_msg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br><br> " + veh_error_msg;
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
			response.sendRedirect("../service/vehicle-dmsstatus-user-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (CNumeric(dmsstatus_id).equals("0")) {
			msg = msg + "<br>Select DMS Status!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex)
			throws FileNotFoundException, IOException, InvalidFormatException, SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
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

			if (rowLength > 1000) {
				rowLength = 1000;
			}
			int h = 0;
			int j = 0;
			propcount = 0;

			if (columnLength == 10) {
				for (j = 1; j < rowLength + 1; j++) {
					CheckForm();
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {

						// Sl.No
						if (h == 0)
						{

						}
						// Chassis No
						if (h == 1) {
							veh_chassis_no = "";
							veh_chassis_no = sheetData[j][h];

						}
						// Reg.No
						if (h == 2) {
							veh_reg_no = "";
							veh_reg_no = PadQuotes(sheetData[j][h]);
							if (veh_reg_no.equals("")) {
								error_msg += "Reg No. should not be empty!  <br>";
							}

						}
						// Model
						if (h == 3) {
							variant_name = "";
							variant_name = PadQuotes(sheetData[j][h]);
						}
						// Sale Date
						if (h == 4) {
							veh_sale_date = "";
							veh_sale_date = sheetData[j][h];

						}
						// Customer name
						if (h == 5) {
							customer_name = "";
							customer_name = sheetData[j][h];
						}
						// Address
						if (h == 6) {
							contact_address = "";
							contact_address = sheetData[j][h];
						}
						// Phone No.
						if (h == 7) {
							contact_phone1 = "";
							contact_phone1 = sheetData[j][h];
						}
						// Mobile No.
						if (h == 8) {
							contact_mobile1 = "";
							contact_mobile1 = sheetData[j][h];
						}
						// Status
						if (h == 9) {
							veh_dmsstatus_id = "";
							dmsstatus = sheetData[j][h];
							if (!dmsstatus.equals("")) {
								veh_dmsstatus_id = dmsstatus_id;
							}
						}
					}
					if (error_msg.equals("")) {

						SOP("123");

						StrSql = "SELECT veh_id"
								+ " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " WHERE 1=1"
								+ "	AND veh_reg_no = '" + veh_reg_no + "'";
						veh_id = CNumeric(ExecuteQuery(StrSql));
						if (!veh_id.equals("0")) {
							StrSql = " UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET veh_dmsstatus_id =" + veh_dmsstatus_id + ","
									+ " veh_modified_id = " + emp_id + ","
									+ " veh_modified_date  = '" + ToLongDate(kknow()) + "'"
									+ " WHERE 1=1"
									+ "	AND veh_id = " + veh_id;
							SOP("StrSql==" + StrSql);
							stmttx.execute(StrSql);
							propcount++;
							conntx.commit();
						} else if (veh_id.equals("0")) {
							error_msg += "Vehicle is not present for this Reg No.<br>";
						}

					}
					SOP("error_msg==" + error_msg);
					if (!error_msg.equals("")) {
						if (!veh_reg_no.equals("")) {
							veh_error_msg += ++count + "." + " Reg NO. : " + veh_reg_no + "==><br>" + error_msg + "<br>";
						} else if (veh_reg_no.equals("") && !veh_chassis_no.equals("")) {
							veh_error_msg += ++count + "." + " Chassis NO. : " + veh_chassis_no + "==><br>" + error_msg + "<br>";
						} else if (veh_reg_no.equals("") && veh_chassis_no.equals("") && !customer_name.equals("")) {
							veh_error_msg += ++count + "." + "Customer Name : " + customer_name + "==><br>" + error_msg + "<br>";
						}
					}
				}
			} else {
				msg += "<br>" + "Document columns doesn't match with the template!";
			}
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				SOP("Transaction Error==");
				conntx.rollback();
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
			msg = "<br>Transaction Error!";
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
	public String PopulateDmsStatus()
	{
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT dmsstatus_id, dmsstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_service_dms_status"
					+ " GROUP BY dmsstatus_id"
					+ " ORDER BY dmsstatus_name";
			// SOP("StrSql==loc=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select DMS Status</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("dmsstatus_id"));
				Str.append(Selectdrop(crs.getInt("dmsstatus_id"), dmsstatus_id));
				Str.append(">").append(crs.getString("dmsstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
