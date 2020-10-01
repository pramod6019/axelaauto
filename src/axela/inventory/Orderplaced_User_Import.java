//Shivaprasad 6/07/2015   
package axela.inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Orderplaced_User_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", errormsg = "";
	public String comp_id = "0";
	public String branch_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "", uploaddocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "", buttonValue = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, updatecount = 0;
	public String BranchAccess = "", ExeAccess = "";
	public String upload = "";
	public String error_msg = "";
	public String target_error_msg = "";
	public int targetcolumnLength = 0;
	public int count = 0;
	public String orderplaced_id = "0";

	public InputStream inputStream = null;

	public String orderplaced_branch_id = "0", orderplaced_model_id = "0", orderplaced_fueltype_id = "0";
	public String branchname = "", modelname = "", fueltypename = "";
	public String orderplaced_entry_id = "0", orderplaced_entry_date = "", orderplaceddate = "", orderplaced_count = "0";

	Map<String, String> fieldsMap = new HashMap<String, String>();

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
				// CheckPerm(comp_id, "service_target_edit", request, response);
				orderplaced_entry_id = emp_id;
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
					orderplaced_branch_id = str1[0];
					// SOP("orderplaced_branch_id------" + orderplaced_branch_id);
					// System.out.println("str1" + str1[1] + "str2" + str1[2] + "str3" + str1[3] + "str4" + str1[4] + "str5" + str1[5] + "str6" + str1[6] + "str7" + str1[7] + "str8" + str1[8]);
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
			// savePath = VehicleImportPath(comp_id);
			// docsize = 1;
			// importdocformat = ".xls, .xlsx";
			// msg = "";
			//
			// boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			// if (isMultipart) {
			// DiskFileItemFactory factory = new DiskFileItemFactory();
			// factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
			//
			// File f = new File(savePath);
			// if (!f.exists()) {
			// f.mkdirs();
			// }
			// factory.setRepository(f);
			// ServletFileUpload upload = new ServletFileUpload(factory);
			// upload.setSizeMax((1024 * 1024 * (int) docsize) + (1024 * 1024));
			//
			// List<FileItem> items = upload.parseRequest(request);
			//
			// // put formfields into map except filename
			// for (FileItem item : items) {
			// if (item.isFormField()) {
			// fieldsMap.put(item.getFieldName(), item.getString());
			// } else {
			// fileName = item.getName();
			// // File upload_doc = new File(savePath + fileName);
			// // inputStream = item.getInputStream();
			// }
			// }
			//
			// GetValues(request, response);
			//
			// if (buttonValue.equals("Upload")) {
			// Iterator iter = items.iterator();
			// FileItem item = (FileItem) iter.next();
			//
			// if (!fileName.equals("")) {
			// CheckForm();
			// if (!msg.equals("")) {
			// msg = "Error!" + msg;
			// }
			// if (msg.equals("")) {
			// String temp = "";
			// docformat = importdocformat.split(", ");
			// for (int j = 0; j < docformat.length; j++) {
			// if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
			// temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf("."))
			// + " format!";
			// } else {
			// temp = "";
			// break;
			// }
			// }
			// msg += temp;
			// if (msg.equals("")) {
			// File uploadedFile = new File(VehicleImportPath(comp_id) + fileName);
			// if (uploadedFile.exists()) {
			// uploadedFile.delete();
			// }
			// item.write(uploadedFile);
			// String fileName1 = VehicleImportPath(comp_id) + fileName;
			// SOP("Filename1----" + fileName1);
			// getSheetData(fileName1, 0, request, response);
			// // SOP("msg==" + msg);
			// if (msg.equals("")) {
			// msg += "<br>" + propcount + " Target(s) Imported Successfully!";
			// msg += "<br>" + updatecount + " Target(s) Updated Successfully!";
			// if (!target_error_msg.equals("")) {
			// msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + target_error_msg;
			// }
			// }
			// }
			// }
			// }
			// }
			//
			// }
		} catch (FileUploadException fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../inventory/orderplaced-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(orderplaced_branch_id).equals("0")) {
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
			targetcolumnLength = 5;

			if (columnLength != targetcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {

						// Model Name
						if (h == 1) {
							modelname = "";
							orderplaced_model_id = "0";
							modelname = PadQuotes(sheetData[j][h]);
							if (!modelname.equals("")) {
								if (modelname.contains("(")) {
									modelname = modelname.replace("(", "&#40;");
								}
								if (modelname.contains(")")) {
									modelname = modelname.replace(")", "&#41;");
								}

								orderplaced_model_id = CNumeric(ExecuteQuery("SELECT model_id "
										+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
										+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
										+ " WHERE model_name = '" + modelname + "'"
										+ " AND branch_id = " + orderplaced_branch_id));
							}
							if (orderplaced_model_id.equals("0")) {
								error_msg += " Invalid Model Name! <br>";
							}
						}

						// Fueltype name
						if (h == 2) {
							fueltypename = "";
							orderplaced_fueltype_id = "0";
							fueltypename = PadQuotes(sheetData[j][h]);
							orderplaced_fueltype_id = CNumeric(ExecuteQuery(" SELECT fueltype_id "
									+ " FROM " + compdb(comp_id) + "axela_fueltype"
									+ " WHERE fueltype_name = '" + fueltypename + "'"));
							if (orderplaced_fueltype_id.equals("0")) {
								error_msg += " Invalid Fueltype! <br>";
							}
						}

						// Orderplaced date
						if (h == 3) {
							orderplaceddate = "";
							orderplaceddate = PadQuotes(sheetData[j][h]);
							if (!orderplaceddate.equals("")) {
								if (orderplaceddate.contains(".")) {
									orderplaceddate = orderplaceddate.replaceAll("\\.", "/");
								} else if (orderplaceddate.contains("-")) {
									orderplaceddate = orderplaceddate.replaceAll("-", "/");
								} else if (orderplaceddate.length() == 14) {
									if (isValidDateFormatStr(orderplaceddate)) {
										orderplaceddate = orderplaceddate + "";
									}
								}
								if (orderplaceddate.length() != 14 && isValidDateFormatShort(orderplaceddate)) {
									orderplaceddate = ConvertShortDateToStr(orderplaceddate);
								}
							}
							if (orderplaceddate.equals("")) {
								error_msg += " Invalid Date! <br>";
							}
						}

						// Orderplaced count
						if (h == 4) {
							orderplaced_count = "0";
							orderplaced_count = PadQuotes(sheetData[j][h]);
							if (!isNumeric(orderplaced_count)) {
								error_msg += " Invalid Orderplaced Count! <br>";
							}
						}
					}
					String orderplacedcount = "0";
					// if (Double.parseDouble(orderplaceddate.substring(0, 8)) != Double.parseDouble(ToLongDate(kknow()).substring(0, 8))) {
					StrSql = " SELECT COUNT(orderplaced_id) "
							+ " FROM " + compdb(comp_id) + "axela_sales_orderplaced"
							+ " WHERE 1 = 1"
							+ " AND orderplaced_date = '" + orderplaceddate + "'"
							+ " AND orderplaced_model_id = " + orderplaced_model_id
							+ " AND orderplaced_fueltype_id = " + orderplaced_fueltype_id
							+ " AND orderplaced_branch_id = " + orderplaced_branch_id;
					orderplacedcount = ExecuteQuery(StrSql);
					// }

					if (!orderplacedcount.equals("0")) {
						error_msg += " Orderplaced Already Present with " + modelname + "!<br>";
					}

					orderplaced_entry_date = ToLongDate(kknow());
					if (msg.equals("") && error_msg.equals("")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();

							if (orderplacedcount.equals("0")) {
								AddOrderplaced();
								propcount++;
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
						if (!modelname.equals("")) {
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
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) {
		if (fieldsMap.containsKey("addbutton")) {
			buttonValue = fieldsMap.get("addbutton");
		}
		if (fieldsMap.containsKey("dr_branch")) {
			orderplaced_branch_id = fieldsMap.get("dr_branch");
		}
	}

	public void AddOrderplaced() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + " axela_sales_orderplaced"
					+ " ("
					+ " orderplaced_branch_id,"
					+ " orderplaced_model_id,"
					+ " orderplaced_fueltype_id,"
					+ " orderplaced_date,"
					+ " orderplaced_count,"
					+ " orderplaced_entry_id,"
					+ " orderplaced_entry_date"
					+ " )"
					+ " VALUES "
					+ " ("
					+ " " + orderplaced_branch_id + ","
					+ " " + orderplaced_model_id + ","
					+ " " + orderplaced_fueltype_id + ","
					+ " " + orderplaceddate + ","
					+ " " + orderplaced_count + ","
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ ")";
			// SOP("Strsql insert targets===" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				orderplaced_id = rs.getString(1);
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
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} catch (Exception e) {
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id = 1"
					+ BranchAccess
					+ " ORDER BY branch_name";
			SOP("SqlStr==branches---=" + SqlStr);
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

}