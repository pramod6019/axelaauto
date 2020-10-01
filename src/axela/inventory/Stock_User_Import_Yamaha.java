package axela.inventory;

/*
 *@author GuruMurthy TS 19 FEB 2013
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class Stock_User_Import_Yamaha extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", comp_id = "0", stockerrormsg = "", errormsg = "";
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
				// SOP("upload==" + upload);
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
			String vehstock_invoice_date = "", vehstock_dms_date = "";
			String vehstock_invoice_amount = "";
			String vehstock_delstatus_id = "0", vehstock_status_id = "0";
			String vehstock_item_id = "0";
			String trans_option_id = "", vehstock_notes = "", vehstock_chassis_prefix = "", vehstock_engine_no = "";
			String model_code = "";
			String vehstock_nsc = "", dealer_code = "", color = "";
			String vehstock_invoice_no = "";
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			// XlsxReadFile readFile = new XlsxReadFile(); //if i/p file is
			// .xlsx type
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
			int h = 0;
			int j = 0;
			count = 0;
			stockcount = 0;
			updatecount = 0;
			stockerrormsg = "";
			if (columnLength != 12) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					errormsg = "";
					for (h = 0; h < columnLength; h++) {

						// / S no
						if (h == 0)
						{
						}

						// deler code
						if (h == 1) {
							vehstock_branch_id = "0";
							vehstock_vehstocklocation_id = "0";
							dealer_code = PadQuotes(sheetData[j][h]);
							// SOP("dealer_code==" + dealer_code);

							branch_id = CNumeric(ExecuteQuery("SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_code= '" + dealer_code + "'"));

							if (comp_id.equals("1011")) { // Indel Hyundai
								// SOP("1111");
								if (branch_id.equals("3")) {
									vehstock_branch_id = "3";
									vehstock_vehstocklocation_id = "5";
								} else if (branch_id.equals("5")) {
									vehstock_branch_id = "5";
									vehstock_vehstocklocation_id = "4";
								} else if (branch_id.equals("2")) {
									vehstock_branch_id = "2";
									vehstock_vehstocklocation_id = "68";
								}
							}
							if (vehstock_branch_id.equals("0")) {
								errormsg += "Invalid Delear Code!<br>";
							}
							if (vehstock_vehstocklocation_id.equals("0")) {
								errormsg += "Branch location should not be empty!<br>";
							}

						}
						// SOP("dealer_code==" + dealer_code);
						// SOP("vehstock_branch_id==" + vehstock_branch_id);

						// Item code
						if (h == 2) {
							model_code = PadQuotes(sheetData[j][h]);
							// SOP("model_code==" + model_code);
							vehstock_item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
									// + " INNER JOIN " + compdb(comp_id) +
									// "axela_inventory_item_model ON model_id=item_model_id "
									+ " WHERE item_code= '" + model_code + "'"));
							if (vehstock_item_id.equals("0")) {
								errormsg += "Invalid Item Code!<br>";
							}
						}

						// color
						if (h == 3) {
							color = PadQuotes(sheetData[j][h]);
							// SOP("color==" + color);
							trans_option_id = CNumeric(ExecuteQuery("SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " WHERE option_code = '" + color + "'"
									+ " AND  option_brand_id = 102"));
							// SOP("trans_option_id==" + trans_option_id);
						}

						// Chassis prefix
						if (h == 4) {
							vehstock_chassis_prefix = PadQuotes(sheetData[j][h]);
							// SOP("vehstock_chassis_prefix==" + vehstock_chassis_prefix);
						}

						// chassis no
						if (h == 5) {
							vehstock_chassis_no = PadQuotes(sheetData[j][h]);
							if (vehstock_chassis_no.equals("")) {
								errormsg += "Chassis No.should not be empty! <br>";
							}
						}

						// engine no
						if (h == 6) {
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

						// Date of Inv
						if (h == 7) {
							vehstock_invoice_date = PadQuotes(sheetData[j][h]);
							// SOP("vehstock_invoice_date==" + vehstock_invoice_date);
						}
						// Inv Amt
						if (h == 8) {
							vehstock_invoice_amount = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("vehstock_invoice_amount==" + vehstock_invoice_amount);
						}

						// Delivery Status
						if (h == 9) {
							vehstock_delstatus_id = CNumeric(PadQuotes(sheetData[j][h]));
							if (vehstock_delstatus_id.equals("0")) {
								vehstock_delstatus_id = "3";
								// SOP("vehstock_delstatus_id==instock==" + vehstock_delstatus_id);
							}
						}

						// Stock StatusId
						if (h == 10) {
							vehstock_status_id = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("vehstock_status_id==111==" + vehstock_status_id);
							if (vehstock_status_id.equals("0")) {
								vehstock_status_id = "1";
							}
						}
						if (vehstock_status_id.equals("0")) {
							vehstock_status_id = "1";
						}
						// SOP("vehstock_status_id==222==" + vehstock_status_id);
						if (h == 11) {
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
					}
					if (vehstock_invoice_date.equals("")) {
						errormsg += "Invoice Date not present!";
					}
					SOP("vehstock_chassis_no==" + vehstock_chassis_no);
					SOP("vehstock_item_id==" + vehstock_item_id);
					SOP("vehstock_branch_id==" + vehstock_branch_id);
					SOP("errormsg==" + errormsg);
					if (errormsg.equals("") && !vehstock_chassis_no.equals("")
							&& !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0")
							&& !vehstock_vehstocklocation_id.equals("0")) {
						StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
								+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'";
						// SOP("StrSql==stockid==" + StrSql);
						String vehstock_id = CNumeric(ExecuteQuery(StrSql));
						// SOP("vehstock_id==" + vehstock_id);

						if (!vehstock_id.equals("0")) {

							// /// Update Stock
							StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
									+ " SET"
									+ " vehstock_branch_id = " + vehstock_branch_id + ","
									+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ","
									+ " vehstock_item_id = " + vehstock_item_id + ","
									+ " vehstock_chassis_no = '" + vehstock_chassis_no + "',"
									+ " vehstock_chassis_prefix = '" + vehstock_chassis_prefix + "',"
									+ " vehstock_engine_no = '" + vehstock_engine_no + "',"
									+ " vehstock_invoice_date = " + vehstock_invoice_date + ","
									// + " vehstock_invoice_no = '" + vehstock_invoice_no + "',"
									+ " vehstock_invoice_amount = '" + vehstock_invoice_amount + "',";
							if (!vehstock_dms_date.equals("")) {
								StrSql += " vehstock_dms_date = " + vehstock_dms_date + ",";
							}
							StrSql += " vehstock_delstatus_id = '" + vehstock_delstatus_id + "',"
									// + " vehstock_nsc = '" + vehstock_nsc + "',"
									+ " vehstock_status_id = '" + vehstock_status_id + "',"
									// + " vehstock_modelyear = '" + vehstock_modelyear + "',"
									+ " vehstock_notes = 'PRESENT',"
									+ " vehstock_modified_id = " + vehstock_entry_id + ","
									+ " vehstock_modified_date = '" + vehstock_entry_date + "'"
									+ " WHERE vehstock_id = " + vehstock_id + "";
							SOP("StrSql==Stock UPDATE==" + StrSqlBreaker(StrSql));
							updateQuery(StrSql);
							int count = 1;
							// SOP("count==" + count++);
							StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
									+ " WHERE trans_vehstock_id = " + vehstock_id + "";
							// SOP("StrSql==del==" + StrSqlBreaker(StrSql));
							updateQuery(StrSql);

							if (!trans_option_id.equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " (trans_option_id,"
										+ " trans_vehstock_id)"
										+ " VALUES"
										+ " (" + trans_option_id + ","
										+ " " + vehstock_id + ")";
								// SOP("StrSql==trans==add==" + StrSqlBreaker(StrSql));
								updateQuery(StrSql);
							}
							updatecount++;
						} else {

							if (!vehstock_chassis_no.equals("") && !vehstock_item_id.equals("0") && !vehstock_branch_id.equals("0") && !vehstock_vehstocklocation_id.equals("0")) {
								StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_chassis_no = '" + vehstock_chassis_no + "'";
								SOP("StrSql==before insert==" + StrSql);
							}
							// SOP(" vehstock_item_id==" + vehstock_item_id);
							if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
								SOP("vehstock_engine_no==Add==" + vehstock_engine_no);
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock"
										+ "("
										+ " vehstock_item_id,"
										+ " vehstock_branch_id,"
										+ " vehstock_vehstocklocation_id,"
										+ " vehstock_status_id,"
										+ " vehstock_chassis_no,"
										+ " vehstock_chassis_prefix,"
										+ " vehstock_engine_no,"
										+ " vehstock_invoice_date,"
										+ " vehstock_dms_date,"
										// + " vehstock_invoice_no,"
										+ " vehstock_invoice_amount,"
										+ " vehstock_delstatus_id,"
										// + " vehstock_nsc,"
										// + " vehstock_modelyear,"
										// + " vehstock_notes,"
										+ " vehstock_entry_id,"
										+ " vehstock_entry_date)"
										+ " VALUES"
										+ "( '" + vehstock_item_id + "',"
										+ " " + vehstock_branch_id + ","
										+ " " + vehstock_vehstocklocation_id + ","
										+ " " + vehstock_status_id + ","
										+ " '" + vehstock_chassis_no + "',"
										+ " '" + vehstock_chassis_prefix + "',"
										+ " '" + vehstock_engine_no + "',"
										+ " '" + vehstock_invoice_date + "',";
								if (!vehstock_dms_date.equals("")) {
									StrSql += " '" + vehstock_dms_date + "',";
								} else if (vehstock_dms_date.equals("")) {
									StrSql += "'',";
								}
								StrSql += " '" + vehstock_invoice_amount + "',"
										// + " '" + vehstock_invoice_no + "',"
										+ " " + vehstock_delstatus_id + ","
										// + " '" + vehstock_nsc + "',"
										// + " '" + vehstock_modelyear + "',"
										// + " '" + vehstock_notes + "',"
										+ " " + vehstock_entry_id + ","
										+ " '" + vehstock_entry_date + "')";

								SOP("StrSql==Insert Stock==" + StrSql);
								vehstock_id = UpdateQueryReturnID(StrSql);
								if (!trans_option_id.equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
											+ " (trans_option_id,"
											+ " trans_vehstock_id)"
											+ " VALUES"
											+ " (" + trans_option_id + ","
											+ " " + vehstock_id + ")";
									// SOP("trans_stock==insert==" + StrSql);
									updateQuery(StrSql);
								}
								stockcount++;
							}
						}
					}
					if (!errormsg.equals("")) {
						if (!vehstock_chassis_no.equals("")) {
							stockerrormsg += "<br>" + ++count + ". VIN NO.: " + vehstock_chassis_no + "===>" + "<br>" + errormsg;
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
