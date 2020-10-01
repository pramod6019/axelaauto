//Shivaprasad 6/07/2015   
package axela.inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;
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

import axela.sales.Veh_Salesorder_Update;
import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class PrincipalSupport_User_Import extends Connect {

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
	public int updatecount = 0;
	public String BranchAccess = "", ExeAccess = "";
	public String upload = "";
	public String error_msg = "";
	public String target_error_msg = "";
	public int targetcolumnLength = 0;
	public int count = 0;
	public String orderplaced_id = "0";

	public InputStream inputStream = null;

	public String vehstock_brand_id = "0", orderplaced_model_id = "0", orderplaced_fueltype_id = "0";
	public String branchname = "", modelname = "", fueltypename = "";
	public String vehstock_modified_id = "0", vehstock_entry_date = "", orderplaceddate = "", orderplaced_count = "0";

	Map<String, String> fieldsMap = new HashMap<String, String>();

	public String hrs = "", min = "", day = "", month = "", year = "", location_name = "";
	public int curryear = 0, currmonth = 0;
	public Connection conntx = null;
	public Statement stmttx = null;
	public String vehstock_id = "0";
	// public String temp_vehstock_id = "0";
	// public double old_vehstock_principalsupport = 0;
	public String vehstock_principalsupport = "0";
	DecimalFormat deci = new DecimalFormat("##.##");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_principal_support_edit", request, response);
			curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				vehstock_modified_id = emp_id;
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
					vehstock_brand_id = str1[0];
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
											msg += "<br>" + updatecount + " Principal Support(s) Updated Successfully!";
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
			response.sendRedirect("../inventory/princisupport-user-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(vehstock_brand_id).equals("0")) {
			msg = msg + "<br>Select Brand!";
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
			String temp_vehstock_id = "0";
			double old_vehstock_principalsupport = 0;
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
			updatecount = 0;
			targetcolumnLength = 3;
			if (columnLength != targetcolumnLength) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					error_msg = "";
					for (h = 0; h < columnLength + 1; h++) {

						// Stock ID
						if (h == 1) {
							temp_vehstock_id = "0";
							old_vehstock_principalsupport = 0;
							vehstock_id = CNumeric(PadQuotes(sheetData[j][h]));
							if (!vehstock_id.equals("0")) {
								String StrSql = "SELECT vehstock_id, vehstock_principalsupport "
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
										+ " WHERE 1=1 "
										+ " AND model_brand_id = " + vehstock_brand_id
										+ " AND vehstock_id = " + vehstock_id;
								CachedRowSet crs = processQuery(StrSql, 0);
								while (crs.next()) {
									temp_vehstock_id = CNumeric(crs.getString("vehstock_id"));
									old_vehstock_principalsupport = crs.getDouble("vehstock_principalsupport");
								}
								crs.close();
							}
							if (temp_vehstock_id.equals("0")) {
								error_msg += " Invalid Stock " + vehstock_id + " ! <br>";
							}
						}

						// Principal Support
						if (h == 2) {
							vehstock_principalsupport = CNumeric(PadQuotes(sheetData[j][h]));
							if (!vehstock_principalsupport.equals("")) {
								vehstock_principalsupport = deci.format(Double.parseDouble(vehstock_principalsupport));
							}

						}

					}

					vehstock_entry_date = ToLongDate(kknow());
					if (msg.equals("") && error_msg.equals("")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							UpdateStockSupport(vehstock_id, old_vehstock_principalsupport);
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
					} else {
						target_error_msg = target_error_msg + error_msg;
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
			vehstock_brand_id = fieldsMap.get("dr_branch");
		}
	}

	public void UpdateStockSupport(String vehstock_id, double old_vehstock_principalsupport) {
		String so_id = "0";
		try {

			StrSql = " UPDATE " + compdb(comp_id) + "axela_vehstock"
					+ " SET vehstock_principalsupport = " + vehstock_principalsupport
					+ " WHERE vehstock_id = " + vehstock_id;
			updateQuery(StrSql);

			if (old_vehstock_principalsupport != Double.parseDouble(vehstock_principalsupport)) {

				StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_vehstock_id = " + vehstock_id;
				so_id = CNumeric(ExecuteQuery(StrSql));
				if (!so_id.equals("0")) {
					new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);
				}

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
						+ " (history_vehstock_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + vehstock_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + "Principal Support" + "',"
						+ " '" + old_vehstock_principalsupport + "',"
						+ " '" + vehstock_principalsupport + "')";
				updateQuery(StrSql);
			}
			updatecount++;
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
	public String PopulateBrands(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			// //SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					// + " AND branch_branchtype_id IN (1, 2) "
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("StrSql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>").append("Select Brand").append("</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), vehstock_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}