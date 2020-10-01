package axela.inventory;

/*
 *@author GuruMurthy TS 19 FEB 2013
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

public class Stock_User_Import_JLR extends Connect {

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
			String vehstock_chassis_no = "", vehstock_engine_no = "", vehstock_mfgyear = "";
			String vehstock_invoice_date = "";
			String vehstock_invoice_amount = "", vehstock_dms_date = "";
			String vehstock_delstatus_id = "0", vehstock_status_id = "0";
			String vehstock_item_id = "0";
			String trans_option_id = "";
			String item_name = "";
			String vehstock_id = "0", interiorcolor = "", exteriorcolor = "", exteriorcolorid = "0", interiorcolorid = "0";
			String vehstock_invoice_no = "";
			ArrayList<String> transoptionids = new ArrayList<String>();// for exterior and interior color
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
			if (columnLength != 11) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					errormsg = "";
					for (h = 0; h < columnLength; h++) {
						// Location_id
						vehstock_branch_id = "1";
						vehstock_vehstocklocation_id = "1";
						vehstock_delstatus_id = "3";// Instock
						vehstock_status_id = "1";// Open Stock
						vehstock_id = "0";
						// sl.no
						if (h == 0) {

						}
						// order No.
						if (h == 1) {
							vehstock_invoice_no = "";
							vehstock_invoice_no = PadQuotes(sheetData[j][h]);
						}

						// MFG YEAR
						if (h == 2) {
							vehstock_modelyear = "";
							vehstock_modelyear = PadQuotes(sheetData[j][h]);
							if (!vehstock_modelyear.equals("")) {
								if (vehstock_modelyear.length() > 4) {
									errormsg += " Invalid Model Year! <br>";
								}
							}
						}

						// Model
						if (h == 3) {

						}

						// Variant
						if (h == 4) {
							item_name = "";
							item_name = PadQuotes(sheetData[j][h]);
							vehstock_item_id = "0";
							if (!item_name.equals("")) {
								if (item_name.contains("(")) {
									item_name = item_name.replace("(", "&#40;");
								}
								if (item_name.contains(")")) {
									item_name = item_name.replace(")", "&#41;");
								}
								vehstock_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_name= '" + item_name + "'"));
								if (vehstock_item_id.equals("0")) {
									errormsg += " Invalid Variant! <br>";
								}
							} else if (item_name.equals("")) {
								errormsg += "Variant should not be empty! <br>";
							}
						}

						// Exterior Color
						if (h == 5) {
							exteriorcolor = "";
							transoptionids.clear();
							exteriorcolor = PadQuotes(sheetData[j][h]);
							if (!exteriorcolor.equals("")) {
								exteriorcolorid = CNumeric(ExecuteQuery("SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
										+ " WHERE option_name = '" + exteriorcolor + "'"
										+ " AND  option_brand_id = 60"));
								if (exteriorcolorid.equals("0")) {
									errormsg += "No stock option found with name: " + exteriorcolor + "<br>";
								} else if (!exteriorcolorid.equals("0")) {
									transoptionids.add(exteriorcolorid);
								}
							}
						}

						// Interior Color
						if (h == 6) {
							interiorcolor = "";
							interiorcolor = PadQuotes(sheetData[j][h]);
							if (!interiorcolor.equals("")) {
								interiorcolorid = CNumeric(ExecuteQuery("SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
										+ " WHERE option_name = '" + interiorcolor + "'"
										+ " AND  option_brand_id = 60"));
								if (interiorcolorid.equals("0")) {
									errormsg += "No stock option found with name: " + interiorcolor + "<br>";
								} else if (!interiorcolorid.equals("0")) {
									transoptionids.add(interiorcolorid);
								}
							}
						}

						// Chassis No.
						if (h == 7) {
							vehstock_chassis_no = "";
							vehstock_chassis_no = PadQuotes(sheetData[j][h]);
							if (vehstock_chassis_no.equals("")) {
								errormsg += "Chassis No.should not be empty! <br>";
							}
						}

						// Engine No.
						if (h == 8) {
							vehstock_engine_no = "";
							vehstock_engine_no = PadQuotes(sheetData[j][h]);
							if (!vehstock_engine_no.equals("")) {
								StrSql = "SELECT vehstock_engine_no FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'";
								if (ExecuteQuery(StrSql).equals("")) {
									StrSql = "SELECT COUNT(vehstock_id) FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_engine_no = '" + vehstock_engine_no + "'";
									if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) >= 1) {
										errormsg += "Similar Engine No.found! <br>";
									}
								}
							}
						}

						// PURCHASE DATE
						if (h == 9) {
							vehstock_invoice_date = "";
							vehstock_invoice_date = PadQuotes(sheetData[j][h]);
							if (!vehstock_invoice_date.equals("")) {
								// SOP("vehstock_invoice_date==" + vehstock_invoice_date);
								if (vehstock_invoice_date.contains("/")) {
									if (isValidDateFormatShort(vehstock_invoice_date)) {
										vehstock_invoice_date = ConvertShortDateToStr(vehstock_invoice_date);
									} else if (vehstock_invoice_date.split("/").length == 3) {
										month = vehstock_invoice_date.split("/")[0];
										if (month.length() == 1) {
											month = "0" + month;
										}
										day = vehstock_invoice_date.split("/")[1];
										if (day.length() == 1) {
											day = "0" + day;
										}
										year = vehstock_invoice_date.split("/")[2];
										if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
											vehstock_invoice_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
										} else {
											errormsg += "Invalid Purchase Date! <br>";
										}
										day = "";
										month = "";
										year = "";
									}
								} else if (vehstock_invoice_date.length() == 14) {
									if (isValidDateFormatStr(vehstock_invoice_date)) {
										vehstock_invoice_date = vehstock_invoice_date + "";
									}
								} else if (vehstock_invoice_date.contains(".")) {
									if (vehstock_invoice_date.split("\\.").length == 3) {
										day = vehstock_invoice_date.split("\\.")[0];
										if (day.length() == 1) {
											day = "0" + day;
										}
										month = vehstock_invoice_date.split("\\.")[1];
										if (month.length() == 1) {
											month = "0" + month;
										}
										year = vehstock_invoice_date.split("\\.")[2];
										if (year.length() == 2) {
											year = "20" + year;
										}
										if (isValidDateFormatShort(day + "/" + month + "/" + year)) {
											vehstock_invoice_date = year + month + day + "" + ToLongDate(kknow()).substring(8, 14) + "";
											vehstock_invoice_date = vehstock_invoice_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
										} else {
											errormsg += "Invalid Purchase Date! <br>";
										}

										day = "";
										month = "";
										year = "";
									}
								} else {
									vehstock_invoice_date = fmtShr3tofmtShr1(vehstock_invoice_date);
									if (isValidDateFormatStr(vehstock_invoice_date)) {
										vehstock_invoice_date = vehstock_invoice_date.substring(0, 8) + ToLongDate(kknow()).substring(8, 14) + "";
									} else {
										errormsg += "Invalid Purchase Date! <br>";
									}
								}
							}
						}

						// Ndp .
						if (h == 10) {
							vehstock_invoice_amount = "";
							vehstock_invoice_amount = CNumeric(PadQuotes(sheetData[j][h]));
							if (vehstock_invoice_amount.equals("0")) {
								errormsg += "NDP should not be empty! <br>";
							}
						}
					}
					if (errormsg.equals("") && !vehstock_chassis_no.equals("") && !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0")
							&& !vehstock_vehstocklocation_id.equals("0")) {
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							ResultSet rs = null;
							StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
									+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'";
							vehstock_id = CNumeric(ExecuteQuery(StrSql));
							if (!vehstock_id.equals("0")) {
								// /// Update Stock
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_branch_id = " + vehstock_branch_id + ","
										+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ","
										+ " vehstock_item_id = " + vehstock_item_id + ","
										+ " vehstock_chassis_no = '" + vehstock_chassis_no + "',"
										+ " vehstock_engine_no = '" + vehstock_engine_no + "',"
										+ " vehstock_invoice_no = '" + vehstock_invoice_no + "',";
								if (!vehstock_modelyear.equals("")) {
									StrSql += " vehstock_modelyear = '" + vehstock_modelyear + "',";
								}
								if (!vehstock_invoice_date.equals("")) {
									StrSql += " vehstock_invoice_date = '" + vehstock_invoice_date + "',";
								}
								if (!vehstock_invoice_amount.equals("0")) {
									StrSql += " vehstock_invoice_amount = " + vehstock_invoice_amount + ",";
								}
								StrSql += " vehstock_delstatus_id = " + vehstock_delstatus_id + ","
										+ " vehstock_status_id = " + vehstock_status_id + ","
										+ " vehstock_modified_id = " + vehstock_entry_id + ","
										+ " vehstock_modified_date = '" + vehstock_entry_date + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								// SOP("StrSql-------Stock-----UPDATE----------" + StrSql);
								stmttx.execute(StrSql);

								StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " WHERE trans_vehstock_id = " + vehstock_id + "";
								// SOP("StrSql==del--=" + StrSqlBreaker(StrSql));
								updateQuery(StrSql);
								Set<String> optionidscheckunique = new LinkedHashSet<String>(transoptionids);
								if (optionidscheckunique.size() != 0) {
									for (String optionids : optionidscheckunique) {
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
												+ " (trans_option_id,"
												+ " trans_vehstock_id)"
												+ " VALUES"
												+ " (" + optionids + ","
												+ " " + vehstock_id + ")";
										stmttx.addBatch(StrSql);
									}
								}
								stmttx.executeBatch();
								conntx.commit();
								updatecount++;
								// SOP("updatecount==" + updatecount);
							} else {
								if (!vehstock_chassis_no.equals("") && !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0")
										&& !vehstock_vehstocklocation_id.equals("0")) {
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
											+ " vehstock_engine_no,"
											+ " vehstock_invoice_date,"
											+ " vehstock_invoice_amount,"
											+ " vehstock_modelyear,"
											+ " vehstock_invoice_no,"
											+ " vehstock_delstatus_id,"
											+ " vehstock_notes,"
											+ " vehstock_entry_id,"
											+ " vehstock_entry_date)"
											+ " VALUES"
											+ "( '" + vehstock_item_id + "',"
											+ " " + vehstock_branch_id + ","
											+ " " + vehstock_vehstocklocation_id + ","
											+ " " + vehstock_status_id + ","
											+ " '" + vehstock_chassis_no + "',"
											+ " '" + vehstock_engine_no + "',";
									if (!vehstock_invoice_date.equals("")) {
										StrSql += " '" + vehstock_invoice_date + "',";
									} else if (vehstock_invoice_date.equals("")) {
										StrSql += "'',";
									}
									if (!vehstock_invoice_amount.equals("0")) {
										StrSql += " " + vehstock_invoice_amount + ",";
									} else if (vehstock_invoice_amount.equals("0")) {
										StrSql += "0,";
									}
									if (!vehstock_modelyear.equals("")) {
										StrSql += " '" + vehstock_modelyear + "',";
									} else if (vehstock_modelyear.equals("")) {
										StrSql += "'',";
									}
									StrSql += " '" + vehstock_invoice_no + "',"
											+ " " + vehstock_delstatus_id + ","
											+ "'',"// vehstock_notes
											+ " " + vehstock_entry_id + ","
											+ " '" + vehstock_entry_date + "')";
									// SOP("StrSql---INSERT INTO --axela_vehstock--------" + StrSql);
									stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
									rs = stmttx.getGeneratedKeys();
									while (rs.next()) {
										vehstock_id = rs.getString(1);
									}
									rs.close();
									Set<String> optionidscheckunique = new LinkedHashSet<String>(transoptionids);
									if (optionidscheckunique.size() != 0) {
										for (String optionids : optionidscheckunique) {
											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
													+ " (trans_option_id,"
													+ " trans_vehstock_id)"
													+ " VALUES"
													+ " (" + optionids + ","
													+ " " + vehstock_id + ")";
											stmttx.addBatch(StrSql);
										}
									}
									stmttx.executeBatch();
									conntx.commit();
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
					if (!errormsg.equals("") && !vehstock_chassis_no.equals("")) {
						if (!vehstock_chassis_no.equals("")) {
							stockerrormsg += "<br>" + ++count + ". Chassis NO.: " + vehstock_chassis_no + "===>" + "<br>" + errormsg;
						}
						if (!vehstock_engine_no.equals("") && vehstock_chassis_no.equals("")) {
							stockerrormsg += "<br>" + ++count + ". Engine NO.: " + vehstock_engine_no + "===>" + "<br>" + errormsg;

						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
