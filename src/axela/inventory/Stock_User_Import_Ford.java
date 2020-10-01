package axela.inventory;

/*
 *@author GuruMurthy TS 19 FEB 2013
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

//import sun.java2d.loops.XorPixelWriter.ShortData;SOP

public class Stock_User_Import_Ford extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", stockerrormsg = "", emp_id = "0", comp_id = "0", errormsg = "";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int stockcount = 0, updatecount = 0, count = 0;
	public String vehstock_entry_id = "";
	public String vehstock_entry_date = "";
	public String campaign_name = "";
	public String BranchAccess = "";
	public String branch_id = "0";
	public String vehstock_branch_id = "0";
	public String upload = "";
	public String similar_comm_no = "";
	public String vehstock_modelyear = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	String day = "", month = "", year = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);

				vehstock_entry_id = CNumeric(GetSession("emp_id", request));
				vehstock_entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button1"));
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

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			savePath = StockDocPath(comp_id);
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
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
										} else {
											temp = "";
											break;
										}
									}
									msg = msg + temp;
								}
								if (msg.equals("")) {
									if (!fileName.equals("")) {
										File uploadedFile = new File(StockDocPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										String fileName1 = StockDocPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										if (msg.equals("")) {
											msg = stockcount + " Stock(s) imported successfully!" + "<br>";
											msg += updatecount + " Stock(s) Updated successfully!";
											if (!stockerrormsg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + stockerrormsg;
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
			response.sendRedirect("stock-import.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			String vehstock_arrival_date = "", vehstock_vehstocklocation_id = "0";
			String vehstock_chassis_no = "";
			String vehstock_invoice_date = "";
			String vehstock_invoice_amount = "", vehstock_dms_date = "";
			String vehstock_delstatus_id = "0", vehstock_status_id = "0";
			String vehstock_item_id = "0";
			String trans_option_id = "", vehstock_notes = "";
			String model_code = "";
			String vehstock_nsc = "", dealer_code = "", color = "";
			String vehstock_invoice_no = "";
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xlstype
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}
			int h = 0;
			int j = 0;
			count = 0;
			stockcount = 0;
			updatecount = 0;
			stockerrormsg = "";
			if (columnLength != 21) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					errormsg = "";
					for (h = 0; h < columnLength; h++) {
						// / Entity Code
						if (h == 0) {
							model_code = "";
							vehstock_item_id = "0";
							model_code = PadQuotes(sheetData[j][h]);
							vehstock_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
									+ " WHERE item_code= '" + model_code + "'"));
							if (vehstock_item_id.equals("0")) {
								errormsg += " Invalid Entity code! <br>";
							}
							// SOP("vehstock_item_id----------------+" + vehstock_item_id);
						}
						// Brand
						if (h == 1) {

						}
						// Model
						if (h == 2) {

						}
						// Color
						if (h == 3) {
							color = PadQuotes(sheetData[j][h]);
							// SOP("color-------------" + color);
							trans_option_id = CNumeric(ExecuteQuery("SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " WHERE option_name = '" + color + "'"
									+ " AND  option_brand_id = 7"));
							// SOP("trans_option_id--7---------" + trans_option_id);
						}

						// Black Pack
						if (h == 4) {

						}

						// Option Pack
						if (h == 5) {

						}

						// NSC
						if (h == 6) {
							vehstock_nsc = PadQuotes(sheetData[j][h]);
							// SOP("vehstock_nsc--------------" + vehstock_nsc);
						}

						// Vin Number
						if (h == 7) {
							vehstock_chassis_no = PadQuotes(sheetData[j][h]);
							if (vehstock_chassis_no.equals("")) {
								errormsg += "Vin No.should not be empty! <br>";
							}
							// SOP("vehstock_chassis_no====" + vehstock_chassis_no);
						}

						// Date of Inv
						if (h == 8) {
							vehstock_invoice_date = PadQuotes(sheetData[j][h]);
							// SOP("vehstock_invoice_date====" + vehstock_invoice_date);
						}
						// Inv Num
						if (h == 9) {
							vehstock_invoice_no = PadQuotes(sheetData[j][h]);
							// SOP("vehstock_invoice_no====" + vehstock_invoice_no);
						}

						// Ship to
						if (h == 11) {
							vehstock_vehstocklocation_id = "0";
							dealer_code = PadQuotes(sheetData[j][h]);
							branch_id = CNumeric(ExecuteQuery("SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_code= '" + dealer_code + "'"));
							if (branch_id.equals("0")) {
								errormsg += " Invalid Dealer Code! <br>";
							}
							// SOP("vehstock_branch_id-------------------" +
							// vehstock_branch_id);
							// "dealer_code------------------------" + dealer_code);
							// Location_id

							if (comp_id.equals("1011")) { // Indel Hyundai
								if (branch_id.equals("6")) {
									vehstock_branch_id = "6";
									vehstock_vehstocklocation_id = "24";//as per discussion on 11/12/17
								} else if (branch_id.equals("41")) {
									vehstock_branch_id = "41";
									vehstock_vehstocklocation_id = "18";
								} else if (branch_id.equals("42")) {
									vehstock_branch_id = "42";
									vehstock_vehstocklocation_id = "16";
								} else if (branch_id.equals("43")) {
									vehstock_branch_id = "43";
									vehstock_vehstocklocation_id = "17";
								} else if (branch_id.equals("45")) {
									vehstock_branch_id = "45";
									vehstock_vehstocklocation_id = "15";
								} else if (branch_id.equals("46")) {
									vehstock_branch_id = "46";
									vehstock_vehstocklocation_id = "19";
								} else if (branch_id.equals("47")) {
									vehstock_branch_id = "47";
									vehstock_vehstocklocation_id = "14";
								} else if (branch_id.equals("50")) {
									vehstock_branch_id = "50";
									vehstock_vehstocklocation_id = "9";
								}
							}
							if (vehstock_vehstocklocation_id.equals("0")) {
								errormsg += "Branch location should not be empty!<br>";
							}
						}
						// SOP("dealer_code====" + dealer_code + "," + "vehstock_branch_id---------" + vehstock_branch_id);

						// Total Price
						if (h == 17) {
							vehstock_invoice_amount = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("vehstock_invoice_amount====" +vehstock_invoice_amount);
						}

						// Delivery Status
						if (h == 18) {
							vehstock_delstatus_id = CNumeric(PadQuotes(sheetData[j][h]));
							if (vehstock_delstatus_id.equals("0")) {
								vehstock_delstatus_id = "3";
								// SOP("vehstock_delstatus_id------------------" + vehstock_delstatus_id);
							}
						}
						// Stock StatusId
						if (h == 19) {
							vehstock_status_id = CNumeric(PadQuotes(sheetData[j][h]));
							if (vehstock_status_id.equals("0")) {
								vehstock_status_id = "1";
							}
						}
						if (h == 20) {
							vehstock_dms_date = PadQuotes(sheetData[j][h]);
							if (!vehstock_dms_date.equals("")) {
								// SOP("vehstock_dms_date==" + vehstock_dms_date);
								if (vehstock_dms_date.contains("/")) {
									if (isValidDateFormatShort(vehstock_dms_date)) {
										vehstock_dms_date = ConvertShortDateToStr(vehstock_dms_date);
									} else if (vehstock_dms_date.split("/").length == 3) {
										month = vehstock_dms_date.split("/")[0];
										if (month.length() == 1) {
											month = "0" + month;
										}
										day = vehstock_dms_date.split("/")[1];
										if (day.length() == 1) {
											day = "0" + day;
										}
										year = vehstock_dms_date.split("/")[2];
										if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
											vehstock_dms_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
										} else {
											errormsg += "Invalid DMS Date! <br>";
										}
										day = "";
										month = "";
										year = "";
									}
								} else if (vehstock_dms_date.length() == 14) {
									if (isValidDateFormatStr(vehstock_dms_date)) {
										vehstock_dms_date = vehstock_dms_date + "";
									}
								} else if (vehstock_dms_date.contains(".")) {
									if (vehstock_dms_date.split("\\.").length == 3) {
										day = vehstock_dms_date.split("\\.")[0];
										if (day.length() == 1) {
											day = "0" + day;
										}
										month = vehstock_dms_date.split("\\.")[1];
										if (month.length() == 1) {
											month = "0" + month;
										}
										year = vehstock_dms_date.split("\\.")[2];
										if (year.length() == 2) {
											year = "20" + year;
										}
										if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
											vehstock_dms_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
											vehstock_dms_date = vehstock_dms_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
										} else {
											errormsg += "Invalid DMS Date! <br>";
										}

										day = "";
										month = "";
										year = "";
									}
								} else {
									vehstock_dms_date = fmtShr3tofmtShr1(vehstock_dms_date);
									if (isValidDateFormatStr(vehstock_dms_date)) {
										vehstock_dms_date = vehstock_dms_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
									} else {
										errormsg += "Invalid DMS Date! <br>";
									}
								}
								// SOP("vehstock_dms_date=" + vehstock_dms_date);
							}
						}

					}
					if (!isValidDateFormatShort(vehstock_invoice_date)) {
						vehstock_modelyear = "";
						vehstock_invoice_date = "";
					}
					else {
						vehstock_modelyear = SplitYear(ConvertShortDateToStr(vehstock_invoice_date));
						vehstock_invoice_date = ConvertShortDateToStr(vehstock_invoice_date);
						// SOP("vehstock_invoice_date==" + vehstock_invoice_date);
					}
					if (vehstock_invoice_date.equals("")) {
						errormsg += "Invoice Date not present!";
					}
					// SOP("vehstock_chassis_no==" + vehstock_chassis_no);
					// SOP("vehstock_item_id==" + vehstock_item_id);
					// SOP("vehstock_branch_id==" + vehstock_branch_id);
					// SOP("errormsg==" + errormsg);
					if (errormsg.equals("") && !vehstock_chassis_no.equals("") && !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0")
							&& !vehstock_vehstocklocation_id.equals("0")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
									+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'";
							String vehstock_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("vehstock_id----------------" + vehstock_id);
							if (!vehstock_id.equals("0")) {
								// /// Update Stock
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_branch_id = " + vehstock_branch_id + ","
										+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ","
										+ " vehstock_item_id = " + vehstock_item_id + ","
										+ " vehstock_chassis_no = '" + vehstock_chassis_no + "',"
										+ " vehstock_invoice_date = " + vehstock_invoice_date + ","
										+ " vehstock_invoice_no = '" + vehstock_invoice_no + "',"
										+ " vehstock_invoice_amount = '" + vehstock_invoice_amount + "',";
								if (!vehstock_dms_date.equals("")) {
									StrSql += " vehstock_dms_date = " + vehstock_dms_date + ",";
								}
								StrSql += " vehstock_delstatus_id = '" + vehstock_delstatus_id + "',"
										+ " vehstock_nsc = '" + vehstock_nsc + "',"
										+ " vehstock_status_id = '" + vehstock_status_id + "',"
										+ " vehstock_modelyear = '" + vehstock_modelyear + "',"
										// + " vehstock_notes = '" + vehstock_notes + "',"
										+ " vehstock_modified_id = " + vehstock_entry_id + ","
										+ " vehstock_modified_date = '" + vehstock_entry_date + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								// SOP("StrSql-------Stock-----UPDATE----------" + StrSqlBreaker(StrSql));
								stmttx.execute(StrSql);

								StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " WHERE trans_vehstock_id = " + vehstock_id + "";
								// SOP("StrSql==del--=" + StrSqlBreaker(StrSql));
								updateQuery(StrSql);

								if (!trans_option_id.equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
											+ " (trans_option_id,"
											+ " trans_vehstock_id)"
											+ " VALUES"
											+ " (" + trans_option_id + ","
											+ " " + vehstock_id + ")";
									// SOP("StrSql=t---------=updatetrans=" + StrSqlBreaker(StrSql));
									updateQuery(StrSql);
								}
								updatecount++;
								// SOP("updatecount==" + updatecount);
							} else {

								if (!vehstock_chassis_no.equals("") && !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0") && !vehstock_vehstocklocation_id.equals("0")) {
									StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'";
									// SOP("StrSql--------------before insert-------" + StrSql);
								}

								if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock"
											+ "("
											+ " vehstock_item_id,"
											+ " vehstock_branch_id,"
											+ " vehstock_vehstocklocation_id,"
											+ " vehstock_status_id,"
											+ " vehstock_chassis_no,"
											+ " vehstock_invoice_date,"
											+ " vehstock_dms_date,"
											+ " vehstock_invoice_no,"
											+ " vehstock_invoice_amount,"
											+ " vehstock_delstatus_id,"
											+ " vehstock_nsc,"
											+ " vehstock_modelyear,"
											+ " vehstock_entry_id,"
											+ " vehstock_entry_date)"
											+ " VALUES"
											+ "( '" + vehstock_item_id + "',"
											+ " " + vehstock_branch_id + ","
											+ " " + vehstock_vehstocklocation_id + ","
											+ " " + vehstock_status_id + ","
											+ " '" + vehstock_chassis_no + "',"
											+ " '" + vehstock_invoice_date + "',";
									if (!vehstock_dms_date.equals("")) {
										StrSql += " '" + vehstock_dms_date + "',";
									} else if (vehstock_dms_date.equals("")) {
										StrSql += "'',";
									}
									StrSql += " '" + vehstock_invoice_no + "',"
											+ " '" + vehstock_invoice_amount + "',"
											+ " " + vehstock_delstatus_id + ","
											+ " '" + vehstock_nsc + "',"
											+ " '" + vehstock_modelyear + "',"
											+ " " + vehstock_entry_id + ","
											+ " '" + vehstock_entry_date + "')";

									// SOP("StrSql---INSERT INTO --axela_vehstock--------" + StrSql);
									vehstock_id = UpdateQueryReturnID(StrSql);
									if (!trans_option_id.equals("0")) {
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
												+ " (trans_option_id,"
												+ " trans_vehstock_id)"
												+ " VALUES"
												+ " (" + trans_option_id + ","
												+ " " + vehstock_id + ")";
										// SOP("1-------trans_vehstock_id ----------insert------" + StrSql);
										updateQuery(StrSql);
									}
									stockcount++;
									// SOP("stockcount==" + stockcount);
								}
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
					if (!errormsg.equals("") && !dealer_code.equals("")) {
						stockerrormsg += "<br>" + ++count + ". VIN NO.: " + vehstock_chassis_no + "===>" + "<br>" + errormsg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
