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

public class Stock_User_Import_Nexa extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0", comp_id = "0";
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
	public String vehstock_branch_id = "0";
	public String upload = "";
	public String similar_comm_no = "";
	public String vehstock_modelyear = "", stockerrormsg = "", errormsg = "";

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
					// vehstock_branch_id = str1[0];
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
			String vehstock_chassis_prefix = "";
			String vehstock_chassis_no = "";
			String vehstock_engine_no = "";
			String vehstock_invoice_date = "";
			String vehstock_invoice_amount = "";
			String vehstock_delstatus_id = "";
			String item_code = "", vehstock_item_id = "0";
			String trans_option_id = "", vehstock_notes = "", branch_code = "";

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
			stockcount = 0;
			updatecount = 0;
			stockerrormsg = "";
			if (columnLength != 10) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					errormsg = "";
					for (h = 0; h < columnLength; h++) {
						if (h == 1) {
							// SOP(sheetData[j][h].toString());
							vehstock_vehstocklocation_id = "0";
							branch_code = "";
							branch_code = PadQuotes(sheetData[j][h]);
							vehstock_branch_id = CNumeric(ExecuteQuery("SELECT branch_id FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_code = '" + PadQuotes(sheetData[j][h]) + "'"));
							// SOP("vehstock_branch_id----------1----" +
							// vehstock_branch_id);
							if (comp_id.equals("1009")) { // DD Motors
								if (vehstock_branch_id.equals("12") || vehstock_branch_id.equals("29")) {
									vehstock_vehstocklocation_id = "1";
								} else if (vehstock_branch_id.equals("20")) {
									vehstock_vehstocklocation_id = "11"; // Dehradun PDI
								} else if (vehstock_branch_id.equals("43")) {
									vehstock_vehstocklocation_id = "26"; // Karnal PDI
								} else {
									vehstock_vehstocklocation_id = "1";
								}
							}
							if (vehstock_branch_id.equals("0")) {
								errormsg += "Invalid Delear Code!<br>";
							}
							if (vehstock_vehstocklocation_id.equals("0")) {
								errormsg += "Branch location should not be empty!<br>";
							}

						}
						if (h == 2) {
							// SOP("item_code----1-----" +
							// sheetData[j][h].toString());
							item_code = "";
							item_code = PadQuotes(sheetData[j][h]);
							vehstock_item_id = CNumeric(ExecuteQuery("SELECT COALESCE((SELECT vehstockvariant_item_id"
									+ " FROM " + compdb(comp_id) + "axela_vehstock_variant"
									+ " WHERE"
									+ " vehstockvariant_code = '" + item_code + "' LIMIT 1), 0)"));
							if (vehstock_item_id.equals("0")) {
								errormsg += "Invalid Item Code!<br>";
							}
							// SOP("vehstock_item_id-------------" + vehstock_item_id);
						}

						if (h == 3) {
							// SOP(" sheetData[j][h] -----------" +
							// sheetData[j][h]);

							trans_option_id = CNumeric(ExecuteQuery("SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " WHERE option_code = '" + PadQuotes(sheetData[j][h]) + "'"
									+ " AND option_brand_id=10"));
							// SOP("trans_option_id==123=" + trans_option_id);
						}

						if (h == 4) {
							// SOP("4");
							vehstock_chassis_prefix = PadQuotes(sheetData[j][h]);
							if (vehstock_chassis_prefix.equals("null")) {
								vehstock_chassis_prefix = "";
							}
							// SOP("vehstock_chassis_prefix==123=" +
							// vehstock_chassis_prefix);
						}

						if (h == 5) {
							// SOP("5");
							vehstock_chassis_no = PadQuotes(sheetData[j][h]);
							if (vehstock_chassis_no.equals("")) {
								errormsg += "Chassis No.should not be empty! <br>";
							}
						}

						if (h == 6) {
							// SOP("6");
							vehstock_engine_no = PadQuotes(sheetData[j][h]);
							if (vehstock_engine_no.equals("")) {
								errormsg += "Engine No.should not be empty! <br>";
							}
							// SOP("vehstock_engine_no==123=" + vehstock_engine_no);
						}

						if (h == 7) {
							// SOP("7");
							vehstock_invoice_date = PadQuotes(sheetData[j][h]);
							// SOP("vehstock_engine_no==123=" + vehstock_engine_no);
							// SOP("inv date===" +
							// ConvertShortDateToStr(stock_invoice_date));
						}

						if (h == 8) {
							// SOP("8");
							vehstock_invoice_amount = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("vehstock_invoice_amount111====" +
							// vehstock_invoice_amount);
						}

						if (h == 9) {
							// SOP("9");
							vehstock_delstatus_id = PadQuotes(sheetData[j][h]);
							// vehstock_delstatus_id =
							// ExecuteQuery("SELECT delstatus_id FROM " +
							// compdb(comp_id) + "axela_sales_so_delstatus WHERE"
							// + " delstatus_name LIKE '%" + sheetData[j][h] +
							// "%'");
							if (CNumeric(vehstock_delstatus_id).equals("0")) {
								vehstock_delstatus_id = "3";
							}
						}

					}

					if (!isValidDateFormatShort(vehstock_invoice_date)) {
						vehstock_modelyear = "";
						vehstock_invoice_date = "";
						// SOP("vehstock_modelyear==invalid=="+stock_modelyear);
					}
					else {
						vehstock_modelyear = SplitYear(ConvertShortDateToStr(vehstock_invoice_date));
						vehstock_invoice_date = ConvertShortDateToStr(vehstock_invoice_date);
						// SOP("vehstock_modelyear==valid=="+stock_modelyear);
					}
					// SOP("vehstock_chassis_no----------" + vehstock_chassis_no);
					SOP("vehstock_engine_no----------" + vehstock_engine_no);
					SOP("vehstock_branch_id----------" + vehstock_branch_id);
					SOP("vehstock_item_id----------" + vehstock_item_id);

					if (errormsg.equals("") && !vehstock_chassis_no.equals("") && !vehstock_engine_no.equals("") && !vehstock_branch_id.equals("0") && !vehstock_item_id.equals("0")) {
						// SOP("1");
						// if (!stock_chassis_no.equals("") &&
						// !stock_branch_id.equals("0") &&
						// !stock_item_id.equals("0")) {
						StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
								+ " WHERE CONCAT(vehstock_chassis_no,'-', vehstock_engine_no) = '" + vehstock_chassis_no + "-" + vehstock_engine_no + "'";
						String vehstock_id = CNumeric(ExecuteQuery(StrSql));
						// SOP("vehstock_id -----------" + vehstock_id);
						// SOP("vehstock_chassis_no --------------------" +
						// vehstock_chassis_no);
						// SOP("vehstock_branch_id -----------" + vehstock_branch_id);
						// SOP("vehstock_id -----------" + vehstock_id);
						if (!vehstock_id.equals("0")) {
							// /// Update Stock
							StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
									+ " SET"
									+ " vehstock_branch_id = " + vehstock_branch_id + ","
									+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ","
									+ " vehstock_item_id = " + vehstock_item_id + ","
									+ " vehstock_chassis_prefix = '" + vehstock_chassis_prefix + "',"
									+ " vehstock_chassis_no = '" + vehstock_chassis_no + "',"
									+ " vehstock_engine_no = '" + vehstock_engine_no + "',"
									+ " vehstock_invoice_date = '" + vehstock_invoice_date + "',"
									+ " vehstock_invoice_amount = '" + vehstock_invoice_amount + "',"
									// + " vehstock_arrival_date = '" +
									// ConvertShortDateToStr(vehstock_arrival_date) +
									// "',"
									// + " vehstock_delstatus_id = " +
									// vehstock_delstatus_id + ","
									+ " vehstock_status_id = 1,"
									// + " vehstock_modelyear = '2015',"
									+ " vehstock_modelyear = '" + vehstock_modelyear + "',"
									+ " vehstock_notes = '" + vehstock_notes + "',"
									+ " vehstock_modified_id = " + vehstock_entry_id + ","
									+ " vehstock_modified_date = '" + vehstock_entry_date + "'"
									+ " WHERE vehstock_id = " + vehstock_id + "";
							// SOP("StrSql-------Stock-----UPDATE----------" +
							// StrSqlBreaker(StrSql));
							updateQuery(StrSql);

							StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
									+ " WHERE trans_vehstock_id = " + vehstock_id + "";
							// SOP("StrSql==d=" + StrSqlBreaker(StrSql));
							updateQuery(StrSql);

							if (!trans_option_id.equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " (trans_option_id,"
										+ " trans_vehstock_id)"
										+ " VALUES"
										+ " (" + trans_option_id + ","
										+ " " + vehstock_id + ")";
								// SOP("StrSql=-------=add=" +
								// StrSqlBreaker(StrSql));
								updateQuery(StrSql);
							}
							updatecount++;
						} else {
							stockcount++;
							if (!vehstock_chassis_no.equals("") && !vehstock_engine_no.equals("")) {
								StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) = '" + vehstock_chassis_no + "-" + vehstock_engine_no + "'";

							}

							// SOP("vehstock_vehstocklocation_id-------------in ----------" +
							// vehstock_vehstocklocation_id);
							if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock"
										+ "("
										+ " vehstock_item_id,"
										+ " vehstock_branch_id,"
										+ " vehstock_vehstocklocation_id,"
										+ " vehstock_status_id,"
										+ " vehstock_chassis_prefix,"
										+ " vehstock_chassis_no,"
										+ " vehstock_engine_no,"
										+ " vehstock_invoice_date,"
										+ " vehstock_invoice_amount,"
										+ " vehstock_delstatus_id,"
										+ " vehstock_modelyear,"
										+ " vehstock_notes,"
										+ " vehstock_entry_id,"
										+ " vehstock_entry_date)"
										+ " VALUES"
										+ " ((SELECT COALESCE((SELECT item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE"
										+ " item_code = '" + item_code + "' LIMIT 1), 0)),"
										+ " " + vehstock_branch_id + ","
										+ " " + vehstock_vehstocklocation_id + ","
										+ " 1,"
										+ " '" + vehstock_chassis_prefix + "',"
										+ " '" + vehstock_chassis_no + "',"
										+ " '" + vehstock_engine_no + "',"
										+ " '" + vehstock_invoice_date + "',"
										+ " '" + vehstock_invoice_amount + "',"
										+ " " + vehstock_delstatus_id + ","
										+ " '" + vehstock_modelyear + "',"
										+ " '" + vehstock_notes + "',"
										+ " " + vehstock_entry_id + ","
										+ " '" + vehstock_entry_date + "')";
								// SOP("StrSql---INSERT INTO --axela_vehstock--------"
								// + StrSql);
								vehstock_id = UpdateQueryReturnID(StrSql);
								// SOP("StrSql--------4----Insert----------" +
								// StrSqlBreaker(StrSql));
								if (!trans_option_id.equals("0")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
											+ " (trans_option_id,"
											+ " trans_vehstock_id)"
											+ " VALUES"
											+ " (" + trans_option_id + ","
											+ " " + vehstock_id + ")";
									updateQuery(StrSql);
								}
							}
						}
					}
					if (!errormsg.equals("")) {
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
