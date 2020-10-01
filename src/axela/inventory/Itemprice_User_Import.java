//Shivaprasad 6/07/2015   
package axela.inventory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class Itemprice_User_Import extends Connect {

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
	public String str1[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int propcount = 0, errorcount = 0;
	public String entry_id = "0";
	public String entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", rateclass_id = "0", add_or_update = "";
	public String branch_name = "";
	public String upload = "";
	public String effective_from = "", effectivefrom = "";
	public Map itemConfigCheck = null, itemNonConfigCheck = null;

	// main item variables
	String item_id = "", item_code = "", item_service_code = "", item_hsn = "", item_price = "", variant = "", model = "", tax1 = "0", tax2 = "0", tax3 = "0", tax4 = "0";
	String item_salestax1_ledger_id = "0", item_salestax2_ledger_id = "0", item_salestax3_ledger_id = "0", item_salestax4_ledger_id = "0";

	// option item variables
	String option_item_id[] = null, option_item_price[] = null, item_ids = "";

	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				entry_id = emp_id;
				entry_date = ToLongDate(kknow());
				upload = PadQuotes(request.getParameter("add_button1"));
				effectivefrom = strToShortDate(ToShortDate(kknow()));
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

			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
				// File f = new File("d:/");
				File f = new File(savePath);
				if (!f.exists()) {
					f.mkdirs();
				}
				factory.setRepository(f);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax((1024 * 1024 * (int) docsize) + (1024 * 1024));
				List items = upload.parseRequest(request);
				// SOP("list====" + items);
				Iterator it = items.iterator();
				for (int i = 0; it.hasNext() && i <= 7; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
					}
				}
				Iterator iter = items.iterator();
				rateclass_id = str1[0];
				add_or_update = "add";
				effectivefrom = str1[1];
				msg = "";
				CheckForm();
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				for (int i = 0; iter.hasNext() && i <= 7; i++) {
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
										msg = "<br>" + propcount + " Price imported successfully!" + msg;
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
			response.sendRedirect("../service/jobcard-user-import-maruti.jsp?msg=" + msg);
		}
	}

	public void CheckForm() {
		msg = "";
		if (CNumeric(rateclass_id).equals("0")) {
			msg = msg + "<br>Select Rate Class!";
		}
		if (add_or_update.equals("add") && effectivefrom.equals("")) {
			msg = msg + "<br>Select Effective From!";
		} else if (add_or_update.equals("add")) {
			if (isValidDateFormatShort(effectivefrom)) {
				effective_from = ConvertShortDateToStr(effectivefrom);
			} else {
				msg = msg + "<br>Invalid Effective From date format!";
			}
		}

		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex)
			throws FileNotFoundException, IOException, InvalidFormatException, SQLException {
		try {
			conntx = connectDB();
			stmttx = conntx.createStatement();
			conntx.setAutoCommit(false);

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
				// SOP("rowLength==" + rowLength);
			}

			if (rowLength > 500) {
				rowLength = 500;
			}
			int h = 0;
			int j = 0;
			propcount = 0;
			option_item_id = new String[columnLength - 13];
			option_item_price = new String[columnLength - 13];
			// SOP("columnLength===" + columnLength);

			for (j = 0; j < rowLength + 1; j++) {
				errormsg = "";
				itemConfigCheck = new HashMap();
				item_salestax1_ledger_id = "0";
				item_salestax2_ledger_id = "0";
				item_salestax3_ledger_id = "0";
				item_salestax4_ledger_id = "0";
				if (j == 1) {
					for (int i = 1; i <= (columnLength - 13); i++) {
						if (!CNumeric(PadQuotes(sheetData[1][12 + i])).equals("0")) {
							option_item_id[i - 1] = PadQuotes(sheetData[1][12 + i]);
						} else {
							option_item_id[i - 1] = "0";
						}
					}

				} else if (j >= 2) {
					int count = 0;

					for (h = 0; h < columnLength; h++) {
						if (rateclass_id.equals("-1")) {
							rateclass_id = "1";
						}

						if (h == 1) {
							model = "";
							model = PadQuotes(sheetData[j][h]);
							// SOP("model==" + model);
						}

						if (h == 2) {
							variant = "";
							variant = PadQuotes(sheetData[j][h]);
							// SOP("variant==" + variant);
						}

						if (h == 3) {
							item_id = "";
							item_id = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("item_id==" + item_id);
						}
						if (h == 4) {
							item_code = "";
							item_code = PadQuotes(sheetData[j][h]);
							// SOP("item_code==" + item_code);
						}
						if (h == 5) {
							item_service_code = "";
							item_service_code = PadQuotes(sheetData[j][h]);
							// SOP("item_service_code==" + item_service_code);
						}
						if (h == 6) {
							item_hsn = "";
							item_hsn = PadQuotes(sheetData[j][h]);
							// SOP("item_hsn==" + item_hsn);
						}
						if (h == 7) {
							item_price = "";
							item_price = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("item_price==" + item_price);
						}

						if (h == 8) {
							tax1 = "";
							tax1 = PadQuotes(sheetData[j][h]);
							tax1 = tax1.replace("%", "");
							if (tax1.contains(".")) {
								tax1 = tax1.replace("0.", "");
							}
							// tax1 = CalTaxPercentage(item_price, tax1);
							// SOP("tax1==" + tax1);
						}
						if (h == 9) {
							tax2 = "";
							tax2 = PadQuotes(sheetData[j][h]);
							tax2 = tax2.replace("%", "");
							if (tax2.contains(".")) {
								tax2 = tax2.replace("0.", "");
							}
							// tax2 = CalTaxPercentage(item_price, tax2);
							// SOP("tax2==" + tax2);
						}

						if (h == 10) {// waiting for confirmation
							tax3 = "";
							tax3 = PadQuotes(sheetData[j][h]);
							tax3 = tax3.replace("%", "");
							if (tax3.contains(".")) {
								tax3 = tax3.replace("0.", "");
							}
							// tax3 = CalTaxPercentage(item_price, tax3);
							SOP("tax3==" + tax3);
						}

						if (h == 11) {
							tax4 = "";
							tax4 = PadQuotes(sheetData[j][h]);
							tax4 = tax4.replace("%", "");
							if (tax4.contains(".")) {
								tax4 = tax4.replace("0.", "");
							}
							// tax4 = CalTaxPercentage(item_price, tax4);
							SOP("tax4==" + tax4);
						}

						if (h >= 13 && h <= 30) {
							// SOP("h==" + h + "== " + PadQuotes(sheetData[j][h]));
							option_item_price[count] = "0";
							option_item_price[count] = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("option_item_price[" + count + "]==" + CNumeric(PadQuotes(sheetData[j][h])));
							count++;
						}
					}

					if (!CNumeric(item_id).equals("0") && isNumeric(item_id) && Double.parseDouble(item_price) > 0) {
						if (!tax1.equals("0") || !tax2.equals("0") || !tax3.equals("0") || !tax4.equals("0")) {
							StrSql = "SELECT";
							if (!tax1.equals("0")) {
								StrSql += " COALESCE(sgst.customer_id, 0) AS SGST,";
							}
							if (!tax2.equals("0")) {
								StrSql += " COALESCE(cgst.customer_id, 0) AS CGST,";
							}
							if (!tax3.equals("0")) {
								StrSql += " COALESCE(igst.customer_id, 0) AS IGST,";
							}
							if (!tax4.equals("0")) {
								StrSql += " COALESCE(cess.customer_id, 0) AS Cess";
							}
							StrSql += " FROM axelaauto.axela_acc_tax_type";
							if (!tax1.equals("0")) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 3"
										+ " AND customer_rate = " + tax1
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS sgst ON sgst.customer_taxtype_id = taxtype_id";
							}
							if (!tax2.equals("0")) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 4"
										+ " AND customer_rate = " + tax2
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS cgst ON cgst.customer_taxtype_id = taxtype_id";
							}
							if (!tax3.equals("0")) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 5"
										+ " AND customer_rate = " + tax3
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS igst ON igst.customer_taxtype_id = taxtype_id";
							}
							if (!tax4.equals("0")) {
								StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_taxtype_id = 6"
										+ " AND customer_rate = " + tax4
										+ " AND customer_tax = '1'"
										+ " GROUP BY customer_id ) AS cess ON cess.customer_taxtype_id = taxtype_id";
							}
							SOP("check==" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							if (crs.isBeforeFirst()) {
								while (crs.next()) {
									if (!tax1.equals("0")) {
										if (!crs.getString("SGST").equals("0")) {
											item_salestax1_ledger_id = CNumeric(crs.getString("SGST"));
										}
									}
									if (!tax2.equals("0")) {
										if (!crs.getString("CGST").equals("0")) {
											item_salestax2_ledger_id = CNumeric(crs.getString("CGST"));
										}
									}
									if (!tax3.equals("0")) {
										if (!crs.getString("IGST").equals("0")) {
											item_salestax3_ledger_id = CNumeric(crs.getString("IGST"));
										}
									}

									if (!tax4.equals("0")) {
										if (!crs.getString("Cess").equals("0")) {
											item_salestax4_ledger_id = CNumeric(crs.getString("Cess"));
										}
									}
								}
							}
						}

						StrSql = "SELECT option_item_id, item_name"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_option"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
								+ " WHERE option_itemmaster_id = " + item_id
								+ " AND item_active = 1";
						CachedRowSet crs = processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								itemConfigCheck.put(crs.getString("option_item_id"), crs.getString("item_name"));
							}
						}
						crs.close();

						if (item_ids.equals("")) {
							for (int k = 0; k < option_item_id.length; k++) {
								item_ids += option_item_id[k] + ",";
							}
							item_ids = item_ids.substring(0, item_ids.length() - 1);
							if (!item_ids.equals("")) {
								StrSql = "SELECT item_id, item_name"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE item_id IN (" + item_ids + ")";
								itemNonConfigCheck = new HashMap();
								crs = processQuery(StrSql, 0);
								if (crs.isBeforeFirst()) {
									while (crs.next()) {
										itemNonConfigCheck.put(crs.getString("item_id"), crs.getString("item_name"));
									}
								}
								crs.close();
							}
						}

						// Check Taxes for the item
						StrSql = "SELECT item_salestax1_ledger_id, item_salestax2_ledger_id,"
								+ " item_salestax3_ledger_id, item_salestax4_ledger_id, item_type_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_id = " + item_id;
						crs = processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								if (!crs.getString("item_salestax1_ledger_id").equals(item_salestax1_ledger_id)) {
									errormsg += "<br>" + "SGST rate is not matching!";
								}
								if (!crs.getString("item_salestax2_ledger_id").equals(item_salestax2_ledger_id)) {
									errormsg += "<br>" + "CGST rate is not matching!";
								}
								if (!crs.getString("item_salestax3_ledger_id").equals(item_salestax3_ledger_id)) {
									errormsg += "<br>" + "IGST rate is not matching!";
								}
								if (!crs.getString("item_salestax4_ledger_id").equals(item_salestax4_ledger_id) && crs.getString("item_type_id").equals("1")) {
									errormsg += "<br>" + "Cess rate is not matching!";
								}
							}
						}
						crs.close();

						// Item tax update
						StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item"
								+ " SET";
						if (!item_salestax1_ledger_id.equals("0")) {
							StrSql += " item_salestax1_ledger_id = " + item_salestax1_ledger_id + ",";
						}
						if (!item_salestax2_ledger_id.equals("0")) {
							StrSql += " item_salestax2_ledger_id = " + item_salestax2_ledger_id + ",";
						}
						if (!item_salestax3_ledger_id.equals("0")) {
							StrSql += " item_salestax3_ledger_id = " + item_salestax3_ledger_id + ",";
						}
						if (!item_salestax4_ledger_id.equals("0")) {
							StrSql += " item_salestax4_ledger_id = " + item_salestax4_ledger_id + ",";
						}
						StrSql += " item_modified_id = " + entry_id + ","
								+ " item_modified_date = '" + entry_date + "'"
								+ " WHERE item_id = " + item_id + "";

						// SOP("update==" + StrSql);
						// stmttx.execute(StrSql); // waiting for confirmation

						// inserting main item price
						StrSql = "SELECT price_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
								+ " WHERE price_rateclass_id = " + rateclass_id
								+ " AND price_item_id = " + item_id
								+ " AND SUBSTR(price_effective_from,1,8) = SUBSTR(" + effective_from + ",1,8)";
						// SOP("StrSql==price_id==" + StrSql);
						String price_id = ExecuteQuery(StrSql);

						if (errormsg.equals("")) {
							if (ExecuteQuery(StrSql).equals("")) {
								AddItemprice(stmttx);
							} else {
								UpdateItemprice(stmttx, price_id);
							}
							stmttx.executeBatch();
							conntx.commit();
							propcount++;
						}
					}
				}
				if (!errormsg.equals("")) {
					msg += "<br><br>" + ++errorcount + ". " + variant + "==>" + errormsg;
				}

			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			try {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void AddItemprice(Statement stmttx) throws SQLException {
		String price_id = "0";
		try {
			// inserting main item price
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_price ("
					+ " price_rateclass_id, "
					+ " price_item_id, "
					+ " price_amt,"
					+ " price_disc,"
					+ " price_disc_type,"
					+ " price_variable,"
					+ " price_effective_from,"
					+ " price_active,"
					+ " price_entry_id,"
					+ " price_entry_date"
					+ " )"
					+ " VALUES("
					+ " " + rateclass_id + ","
					+ " " + item_id + ","
					+ " " + item_price + ","
					+ " 0,"
					+ " 1,"
					+ " '0',"
					+ " " + effective_from + ","
					+ " '1',"
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ""
					+ " )";
			// SOP("main item add===" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				price_id = rs.getString(1);
			}
			rs.close();
			// inserting option item price
			for (int a = 0; a < option_item_id.length; a++) {
				if (isNumeric(PadQuotes(option_item_id[a])) && !CNumeric(PadQuotes(option_item_id[a])).equals("0")) {
					if (itemConfigCheck.containsKey(CNumeric(PadQuotes(option_item_id[a])))) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_price_trans("
								+ " pricetrans_price_id,"
								+ " pricetrans_item_id,"
								+ " pricetrans_variable,"
								+ " pricetrans_amt"
								+ " )"
								+ " VALUES("
								+ " " + price_id + ","
								+ " " + option_item_id[a] + ","
								+ " '0',"
								+ " " + CNumeric(PadQuotes(option_item_price[a])) + ""
								+ " )";
						// SOP("option item add===" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
					} else {
						errormsg += "<br>" + itemNonConfigCheck.get(CNumeric(PadQuotes(option_item_id[a]))) + " is not configured!";
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	public void UpdateItemprice(Statement stmttx, String price_id) throws SQLException {
		try {
			// updating main item price
			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_price"
					+ " SET price_amt =" + item_price
					+ " WHERE price_id = " + price_id
					+ " AND SUBSTR(price_effective_from, 1, 8) = SUBSTR(" + effective_from + ",1,8)";
			// SOP("main utem id==update===" + StrSqlBreaker(StrSql));
			stmttx.addBatch(StrSql);

			// updating option item price
			for (int a = 0; a < option_item_id.length; a++) {
				if (isNumeric(option_item_id[a]) && !option_item_id[a].equals("0")) {
					if (itemConfigCheck.containsKey(CNumeric(PadQuotes(option_item_id[a])))) {
						StrSql = "SELECT COALESCE(pricetrans_price_id,0)"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_price_trans"
								+ " WHERE pricetrans_price_id = " + price_id
								+ " AND pricetrans_item_id = " + CNumeric(PadQuotes(option_item_id[a]));
						// SOP("checking option item price===" + StrSqlBreaker(StrSql));
						if (!ExecuteQuery(StrSql).equals("")) {
							StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_price_trans"
									+ " SET pricetrans_amt = " + CNumeric(PadQuotes(option_item_price[a]))
									+ " WHERE pricetrans_price_id=" + price_id
									+ " AND pricetrans_item_id =" + CNumeric(PadQuotes(option_item_id[a]));
							// SOP("opotion item update==update===" + StrSqlBreaker(StrSql));
							stmttx.addBatch(StrSql);
						} else {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_price_trans("
									+ " pricetrans_price_id,"
									+ " pricetrans_item_id,"
									+ " pricetrans_amt"
									+ " )"
									+ " VALUES("
									+ " " + price_id + ","
									+ " " + CNumeric(PadQuotes(option_item_id[a])) + ","
									+ " " + CNumeric(PadQuotes(option_item_price[a])) + ""
									+ " )";
							// SOP("option item update==add===" + StrSqlBreaker(StrSql));
							stmttx.addBatch(StrSql);
						}
					} else {
						errormsg += "<br>" + itemNonConfigCheck.get(CNumeric(PadQuotes(option_item_id[a]))) + " is not configured!";
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateRateclass(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name,"
					+ " IF(rateclass_type = 1, 'Sales', 'Purchase') AS rateclass_type"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1"
					+ " AND rateclass_type= 1"
					+ " ORDER BY rateclass_type, rateclass_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("rateclass_id"));
				Str.append(StrSelectdrop(crs.getString("rateclass_id"), rateclass_id));
				Str.append(">").append(crs.getString("rateclass_name")).append(" (");
				Str.append(crs.getString("rateclass_type")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
