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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Item_User_Import extends Connect {

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
	public int propcount = 0, insertitemcount = 0, updatepricecount = 0, addpricecount = 0;
	// public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String BranchAccess = "";
	public String branch_id = "0", rateclass_id = "0", add_or_update = "";
	public String branch_name = "";
	public String upload = "";
	public String effective_from = "", effectivefrom = "";

	// main item variables
	String item_id = "0", item_small_desc = "", variant = "", model = "", item_name = "", item_code = "", item_cat_id = "0";
	String item_service_code = "", item_type_id = "0", item_model_id = "0", item_fueltype_id = "0", item_uom_id = "0", item_qty = "0";
	String inventory_location_id = "0";
	String item_alt_uom_id = "0", item_model_name = "", item_cat = "", item_hsn = "", item_sac = "";

	String price_id = "0", price_amt = "", taxrate1 = "", taxrate2 = "", taxrate3 = "", taxrate4 = "", itemerrormsg = "", billcat_name = "", item_billcat_id = "0";
	String item_salestax1_ledger_id = "0", item_salestax2_ledger_id = "0", item_salestax3_ledger_id = "0", item_salestax4_ledger_id = "0", brand_id = "0";
	// option item variables
	String option_item_id[] = null, option_item_price[] = null;

	HashMap<String, String> taxratecheck = new HashMap<String, String>();
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			emp_id = CNumeric(GetSession("emp_id", request));
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_sales_item_add, emp_item_add", request, response);
				upload = PadQuotes(request.getParameter("add_button1"));
				effectivefrom = strToShortDate(ToShortDate(kknow()));
				Addfile(request, response);
			}
		} catch (Exception ex) {
			// SOP("111");
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			savePath = ItemImportPath(comp_id);
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
				for (int i = 0; it.hasNext() && i <= 9; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
					}
				}
				Iterator iter = items.iterator();
				rateclass_id = str1[0];
				effectivefrom = str1[1];
				item_type_id = str1[2];
				inventory_location_id = str1[3];

				CheckForm();
				if (!msg.equals("")) {
					msg = "Error!" + msg;
				}
				for (int i = 0; iter.hasNext() && i <= 9; i++) {
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
										File uploadedFile = new File(ItemImportPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										String fileName1 = ItemImportPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										if (msg.equals("")) {
											msg = insertitemcount + " Item(s) Added successfully!" + "<br>";
											msg += updatepricecount + " Price Updated successfully!" + "<br>";
											msg += addpricecount + " Price Added successfully!";
											if (!itemerrormsg.equals("")) {
												msg += "<br><br>" + "Please rectify the following errors and Import again! <br> " + itemerrormsg;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			msg = "Uploaded file size is large!";
			response.sendRedirect("../inventory/item-user-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (CNumeric(rateclass_id).equals("0")) {
			msg += "<br>Select Rate Class!";
		}
		if (CNumeric(item_type_id).equals("0")) {
			msg += "<br>Select Item Type!";
		}

		if (effectivefrom.equals("")) {
			msg += "<br>Select Effective From!";
		} else if (!effectivefrom.equals("")) {
			if (isValidDateFormatShort(effectivefrom)) {
				effective_from = ConvertShortDateToStr(effectivefrom);
			} else {
				msg = msg + "<br>Invalid Effective From date format!";
			}
		}
		if (CNumeric(inventory_location_id).equals("0")) {
			msg += "<br>Select Stock Location!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex)
			throws FileNotFoundException, IOException, InvalidFormatException {
		try {
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls // type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is // .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}
			if (rowLength > 4000) {
				rowLength = 4000;
			}
			int h = 0;
			int j = 0;
			int count = 0;
			propcount = 0;
			insertitemcount = 0;
			updatepricecount = 0;
			addpricecount = 0;
			itemerrormsg = "";
			if (columnLength != 18) {
				msg += "<br>" + "Document columns doesn't match with the template!";
			} else {
				for (j = 1; j < rowLength + 1; j++) {
					errormsg = "";
					item_salestax1_ledger_id = "0";
					item_salestax2_ledger_id = "0";
					item_salestax3_ledger_id = "0";
					item_salestax4_ledger_id = "0";
					for (h = 0; h < columnLength; h++) {
						if (rateclass_id.equals("-1")) {
							rateclass_id = "1";
						}
						if (h == 1) {
							item_name = "";
							item_name = PadQuotes(sheetData[j][h]);
							// SOP("item_name===" + item_name);
							if (item_name.equals("")) {
								errormsg += "Item name can not be empty!<br> ";
							}
							if (!item_name.equals("")) {
								if (item_name.contains("(")) {
									item_name = item_name.replace("(", "&#40;");
								}
								if (item_name.contains(")")) {
									item_name = item_name.replace(")", "&#41;");
								}
							}
						}
						if (h == 2) {
							item_small_desc = "";
							item_small_desc = PadQuotes(sheetData[j][h]);
						}
						if (h == 3) {
							item_code = "";
							item_id = "0";
							item_code = PadQuotes(sheetData[j][h]);
							if (!item_code.equals("")) {
								if (item_code.contains("(")) {
									item_code = item_code.replace("(", "&#40;");
								}
								if (item_code.contains(")")) {
									item_code = item_code.replace(")", "&#41;");
								}
								item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
										+ "	WHERE item_code = '" + item_code + "'"));
								if (!item_id.equals("0")) {
									StrSql = "SELECT COUNT(item_id)  FROM " + compdb(comp_id) + "axela_inventory_item"
											+ "	WHERE item_name = '" + item_name + "'"
											+ " AND item_code='" + item_code + "'";
									if (Integer.parseInt(CNumeric(ExecuteQuery(StrSql))) > 1) {
										errormsg += "Item Code is Associated with other item !<br>";
									}
								}
							} else if (item_code.equals("") && !item_name.equals("") || item_id.equals("0")) {
								item_id = CNumeric(ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
										+ "	WHERE item_name = '" + item_name + "'"));
							}
							// SOP("item_code====" + item_code);
						}
						if (h == 4) {
							item_service_code = "";
							item_service_code = PadQuotes(sheetData[j][h]);
							// SOP("item_service_code====" + item_service_code);
						}

						if (h == 5) {
							item_cat_id = "0";
							String item_cat = PadQuotes(sheetData[j][h]);
							// SOP("item_cat==" + item_cat);
							if (!item_cat.equals("")) {
								StrSql = "SELECT cat_id FROM " + compdb(comp_id) + "axela_inventory_cat"
										+ " WHERE cat_name = '" + item_cat + "'";
								SOP("Strsql ===" + StrSql);
								item_cat_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
							}

						}
						if (h == 6) {
							item_model_id = "0";
							String item_model_name = PadQuotes(sheetData[j][h]);
							// SOP("item_model_name==" + item_model_name);
							if (!item_model_name.equals("")) {
								if (item_model_name.contains("(")) {
									item_model_name = item_model_name.replace("(", "&#40;");
								}
								if (item_model_name.contains(")")) {
									item_model_name = item_model_name.replace(")", "&#41;");
								}
								StrSql = "SELECT model_id, model_brand_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
										+ " WHERE model_name = '" + item_model_name + "'";
								CachedRowSet crs = processQuery(StrSql, 0);
								// SOP("StrSql======" + StrSql);
								if (crs.isBeforeFirst()) {
									while (crs.next()) {
										item_model_id = crs.getString("model_id");
										brand_id = CNumeric(crs.getString("model_brand_id"));
									}
								}
								crs.close();
								if (item_model_id.equals("0")) {
									errormsg += " Invalid model name!<br> ";
								}
								SOP("item_model_id===" + item_model_id);
							}
							if (item_model_name.equals("")) {
								errormsg += "Model name cannot be empty!<br>";
							}

						}
						if (h == 7) {
							item_billcat_id = "0";
							billcat_name = "";
							billcat_name = PadQuotes(sheetData[j][h]);
							if (!billcat_name.equals("")) {
								if (billcat_name.contains("(")) {
									billcat_name = billcat_name.replace("(", "&#40;");
								}
								if (billcat_name.contains(")")) {
									billcat_name = billcat_name.replace(")", "&#41;");
								}
								StrSql = "SELECT billcat_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
										+ " WHERE 1=1"
										+ " AND billcat_brand_id = " + brand_id
										+ " AND (billcat_name = '" + billcat_name + "'"
										+ " OR billcat_code= '" + billcat_name + "')";
								SOP("billcat_id StrSql==" + StrSql);
								item_billcat_id = CNumeric(ExecuteQuery(StrSql));
								// SOP("billcat_id==" + billcat_id);
								if (item_billcat_id.equals("0")) {
									errormsg += "Invalid Billl Category! <br>";
								}
							} else if (!item_type_id.equals("1")) {
								errormsg += "Bill category cannot be empty!<br>";
							}
						}

						if (h == 8) {
							item_fueltype_id = "0";
							String item_fueltype_name = PadQuotes(sheetData[j][h]);
							StrSql = "SELECT fueltype_id, fueltype_name FROM " + compdb(comp_id) + "axela_fueltype"
									+ " WHERE fueltype_name='" + item_fueltype_name + "'";
							item_fueltype_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("item_fueltype_id===" + item_fueltype_id);

						}

						if (h == 9) {
							item_uom_id = "";
							String item_uom_name = PadQuotes(sheetData[j][h]);
							StrSql = "SELECT uom_id, uom_name FROM " + compdb(comp_id) + "axela_inventory_uom "
									+ " WHERE uom_name= '" + item_uom_name + "' LIMIT 1";
							// SOP("item_uom_id===12===" + StrSql);
							item_uom_id = CNumeric(ExecuteQuery(StrSql));
							// SOP("item_uom_id===" + item_uom_id);
							if (item_uom_id.equals("0")) {
								errormsg += "Invalid UOM!<br>";
							}
						}

						if (h == 10) {
							item_alt_uom_id = "";
							String item_alt_uom_name = PadQuotes(sheetData[j][h]);
							StrSql = "SELECT uom_id, uom_ratio, uom_name FROM " + compdb(comp_id) + "axela_inventory_uom "
									+ " WHERE CONCAT(uom_name,' X ',uom_ratio)= '" + item_alt_uom_name + "'";
							item_alt_uom_id = CNumeric(ExecuteQuery(StrSql));
							if (item_alt_uom_id.equals("0")) {
								errormsg += "Invalid Alternative UOM!<br>";
							}
						}
						if (h == 11) {
							taxrate1 = "";
							taxrate1 = CNumeric(PadQuotes(sheetData[j][h]));
							if (taxrate1.equals("0")) {
								if (item_type_id.equals("1")) {
									errormsg += " SGST cannot be empty! <br>";
								}
							}
							// SOP("item_qty===" + item_qty);
						}
						if (h == 12) {
							taxrate2 = "";
							taxrate2 = CNumeric(PadQuotes(sheetData[j][h]));
							if (taxrate2.equals("0")) {
								if (item_type_id.equals("1")) {
									errormsg += " CGST cannot be empty! <br>";
								}
							}
						}
						if (h == 13) {
							taxrate3 = "";
							taxrate3 = CNumeric(PadQuotes(sheetData[j][h]));
							if (taxrate3.equals("0")) {
								if (item_type_id.equals("1")) {
									errormsg += " IGST cannot be empty! <br>";
								}
							}
						}
						if (h == 14) {
							taxrate4 = "";
							taxrate4 = CNumeric(PadQuotes(sheetData[j][h]));
						}
						if (h == 15) {
							item_hsn = "";
							item_hsn = PadQuotes(sheetData[j][h]);
							if (item_type_id.equals("4")) {
								item_sac = item_hsn;
								item_hsn = "";
							}

						}
						if (h == 16) {
							price_amt = "";
							price_amt = PadQuotes(sheetData[j][h]);
							if (!price_amt.equals("")) {
								if (price_amt.contains("Rs.")) {
									price_amt = price_amt.replace("Rs.", "");
								}
								price_amt = price_amt.replaceAll("[^0-9.]", "");
								price_amt = CNumeric(price_amt);
							}
							// SOP("price_amt=/==" + price_amt);
						}
						if (h == 17) {
							item_qty = "";
							item_qty = CNumeric(PadQuotes(sheetData[j][h]));
							// SOP("item_qty===" + item_qty);
						}
					}
					if (errormsg.equals("") && !item_name.equals("") && !item_billcat_id.equals("0") && !item_type_id.equals("0")) {
						if (!taxrate1.equals("0") || !taxrate2.equals("0") || !taxrate3.equals("0") || !taxrate4.equals("0")) {
							if (!taxrate1.equals("0") && taxratecheck.size() != 0 && taxratecheck.containsKey("SGST-" + taxrate1)) {
								item_salestax1_ledger_id = CNumeric(taxratecheck.get(("SGST-" + taxrate1)));
								item_salestax2_ledger_id = CNumeric(taxratecheck.get(("CGST-" + taxrate1)));
								item_salestax3_ledger_id = CNumeric(taxratecheck.get(("IGST-" + Double.parseDouble(taxrate1) * 2)));
							}
							if (!taxrate4.equals("0")) {
								item_salestax4_ledger_id = CNumeric(taxratecheck.get(("Cess-" + taxrate4)));
							}
							if (item_salestax1_ledger_id.equals("0")) {
								StrSql = "SELECT";
								if (!taxrate1.equals("0")) {
									StrSql += " COALESCE(sgst.customer_id, 0) AS SGST,"
											+ " COALESCE(cgst.customer_id, 0) AS CGST,"
											+ " COALESCE(igst.customer_id, 0) AS IGST,";
								}
								if (!taxrate4.equals("0")) {
									StrSql += " COALESCE(cess.customer_id, 0) AS Cess,";
								}
								StrSql += " sgst.customer_taxtype_id FROM axelaauto.axela_acc_tax_type";
								if (!taxrate1.equals("0")) {
									StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
											+ " FROM " + compdb(comp_id) + "axela_customer"
											+ " WHERE customer_taxtype_id = 3"
											+ " AND customer_rate = " + taxrate1
											+ " AND customer_tax = '1'"
											+ " GROUP BY customer_id ) AS sgst ON sgst.customer_taxtype_id = taxtype_id"

											+ "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
											+ " FROM " + compdb(comp_id) + "axela_customer"
											+ " WHERE customer_taxtype_id = 4"
											+ " AND customer_rate = " + taxrate1
											+ " AND customer_tax = '1'"
											+ " GROUP BY customer_id ) AS cgst ON cgst.customer_taxtype_id = taxtype_id"

											+ "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
											+ " FROM " + compdb(comp_id) + "axela_customer"
											+ " WHERE customer_taxtype_id = 5"
											+ " AND customer_rate = " + Double.parseDouble(taxrate1) * 2
											+ " AND customer_tax = '1'"
											+ " GROUP BY customer_id ) AS igst ON igst.customer_taxtype_id = taxtype_id";
								}
								if (!taxrate4.equals("0")) {
									StrSql += "	LEFT JOIN ( SELECT customer_id, customer_taxtype_id"
											+ " FROM " + compdb(comp_id) + "axela_customer"
											+ " WHERE customer_taxtype_id = 6"
											+ " AND customer_rate = " + taxrate4
											+ " AND customer_tax = '1'"
											+ " GROUP BY customer_id ) AS cess ON cess.customer_taxtype_id = taxtype_id";
								}
								SOP("check==" + StrSql);
								CachedRowSet crs = processQuery(StrSql, 0);
								if (crs.isBeforeFirst()) {
									while (crs.next()) {
										if (!taxrate1.equals("0")) {
											if (!crs.getString("SGST").equals("0")) {
												item_salestax1_ledger_id = CNumeric(crs.getString("SGST"));
												taxratecheck.put(("SGST-" + taxrate1), item_salestax1_ledger_id);
											}

											if (!crs.getString("CGST").equals("0")) {
												item_salestax2_ledger_id = CNumeric(crs.getString("CGST"));
												taxratecheck.put(("CGST-" + taxrate1), item_salestax2_ledger_id);
											}

											if (!crs.getString("IGST").equals("0")) {
												item_salestax3_ledger_id = CNumeric(crs.getString("IGST"));
												taxratecheck.put(("IGST-" + Double.parseDouble(taxrate1) * 2), item_salestax3_ledger_id);
											}
										}

										if (!taxrate4.equals("0")) {
											if (!crs.getString("Cess").equals("0")) {
												item_salestax4_ledger_id = CNumeric(crs.getString("Cess"));
												taxratecheck.put(("Cess-" + taxrate4), item_salestax4_ledger_id);
											}
										}
									}
								}
								crs.close();
							}
						}
						try {
							conntx = connectDB();
							conntx.setAutoCommit(false);
							stmttx = conntx.createStatement();
							if (item_id.equals("0")) {
								AddItem();
							} else if (!item_id.equals("0")) {
								StrSql = "SELECT price_id FROM " + compdb(comp_id) + "axela_inventory_item_price"
										+ " WHERE price_item_id = " + item_id
										+ " AND price_effective_from='" + effective_from + "'"
										+ " AND price_rateclass_id=" + rateclass_id;
								SOP("StrSql===price_id==" + StrSql);
								price_id = CNumeric(ExecuteQuery(StrSql));
								// SOP("price_id===" + price_id);
								if (price_id.equals("0")) {
									AddItemprice();
								} else if (!price_id.equals("0")) {
									UpdateItemprice(price_id);
								}
							}
							if (!item_id.equals("0") && !item_qty.equals("0")) {
								AddStock(item_id);
								// SOP("after AddStock");
							}
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
					if (!errormsg.equals("")) {
						itemerrormsg += "<br>" + ++count + ". Item Name: " + item_name + "===>" + "<br>" + errormsg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void AddItem() {
		try {
			// inserting main item
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item ("
					+ " item_name,"
					+ " item_small_desc, "
					+ " item_code,"
					+ " item_service_code,"
					+ " item_hsn,"
					+ " item_sac,"
					+ " item_cat_id,"
					+ " item_billcat_id,"
					+ " item_type_id,"
					+ " item_model_id,"
					+ " item_fueltype_id,"
					+ " item_uom_id,"
					+ " item_alt_uom_id,"
					+ " item_sales_ledger_id,"
					+ " item_salesdiscount_ledger_id,"
					+ " item_salestax1_ledger_id,"
					+ " item_salestax2_ledger_id,"
					+ " item_salestax3_ledger_id,"
					+ " item_salestax4_ledger_id,"
					+ " item_purchase_ledger_id,"
					+ " item_purchasediscount_ledger_id,"
					+ " item_notes,"
					+ " item_active,"
					+ " item_entry_id,"
					+ " item_entry_date"
					+ " )"
					+ " VALUES("
					+ " '" + item_name + "',"
					+ " '" + item_small_desc + "',"
					+ " '" + item_code + "',"
					+ " '" + item_service_code + "',"
					+ " '" + item_hsn + "',"
					+ " '" + item_sac + "',"
					+ " " + item_cat_id + ","
					+ " " + item_billcat_id + ","
					+ " " + item_type_id + ","
					+ " " + item_model_id + ","
					+ " " + item_fueltype_id + ","
					+ " " + item_uom_id + ","
					+ " " + item_alt_uom_id + ","
					+ " 1,"// item_sales_ledger_id
					+ " 2,"// item_salesdiscount_ledger_id
					+ " " + item_salestax1_ledger_id + ","
					+ " " + item_salestax2_ledger_id + ","
					+ " " + item_salestax3_ledger_id + ","
					+ " " + item_salestax4_ledger_id + ","
					+ " 5,"// item_purchase_ledger_id
					+ " 6,"// item_purchasediscount_ledger_id"
					+ " '' ," // item_notes
					+ " 1 ," // item_active
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ""
					+ " )";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			SOP("StrSql=item=" + StrSql);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				item_id = rs.getString(1);

			}
			conntx.commit();
			if (!item_id.equals("0")) {
				AddItemprice();
			}
			conntx.commit();
			insertitemcount++;
		} catch (Exception ex) {
			// SOP("666");
			try {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					// SOP("rollback=====rollback");
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			} catch (SQLException e) {
				// SOP("777");
				e.printStackTrace();
			}
		}

	}
	public void AddItemprice() {
		String price_id = "0";
		try {

			// inserting main item price for Sales
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
					+ " " + price_amt + ","
					+ " 0,"
					+ " 1,"
					+ " '0',"
					+ " " + effective_from + ","
					+ " '1',"
					+ " " + emp_id + ","
					+ " " + ToLongDate(kknow()) + ""
					+ " )";
			SOP("main item add===" + StrSql);

			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				price_id = rs.getString(1);
				addpricecount++;
			}
			rs.close();
			// inserting option item price

			// for (int a = 0; a < option_item_id.length; a++) {
			// // SOP("option_item_id===" + option_item_id[a]);
			// if (isNumeric(PadQuotes(option_item_id[a])) && !CNumeric(PadQuotes(option_item_id[a])).equals("0")) {
			// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_price_trans("
			// + " pricetrans_price_id,"
			// + " pricetrans_item_id,"
			// + " pricetrans_variable,"
			// + " pricetrans_amt"
			// + " )"
			// + " VALUES("
			// + " " + price_id + ","
			// + " " + option_item_id[a] + ","
			// + " '0',"
			// + " " + CNumeric(PadQuotes(option_item_price[a])) + ""
			//
			// + " )";
			// // SOP("option item add===" + StrSqlBreaker(StrSql));
			// stmttx.addBatch(StrSql);
			// }
			// }
			// stmttx.executeBatch();

		} catch (Exception ex) {
			// SOP("888");
			try {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					// SOP("rollback=====rollback");
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			} catch (SQLException e) {
				// SOP("999");
				e.printStackTrace();
			}
		}

	}
	public void UpdateItemprice(String price_id) {
		try {
			// updating main item price
			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_price"
					+ " SET price_amt =" + price_amt
					+ " WHERE price_id = " + price_id
					+ "  AND SUBSTR(price_effective_from, 1, 8) = SUBSTR(" + effective_from + ",1,8)";
			// SOP("main utem id==update===" + StrSqlBreaker(StrSql));
			stmttx.addBatch(StrSql);

			// Below codes are temporarily commented code

			// updating option item price
			// SOP("option_item_id==" + option_item_id.length);
			// for (int a = 0; a < option_item_id.length; a++) {
			// // SOP("option_item_id[a]" + option_item_id[a]);
			// if (isNumeric(option_item_id[a]) && !option_item_id[a].equals("0")) {
			//
			// StrSql = "SELECT COALESCE(pricetrans_price_id,0)"
			// + " FROM " + compdb(comp_id) + "axela_inventory_item_price_trans"
			// + " WHERE pricetrans_price_id = " + price_id
			// + " AND pricetrans_item_id = " + CNumeric(PadQuotes(option_item_id[a]));
			// // SOP("checking option item price===" + StrSqlBreaker(StrSql));
			// // SOP("=====" + ExecuteQuery(StrSql));
			// if (!ExecuteQuery(StrSql).equals("")) {
			// StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_price_trans"
			// + " SET pricetrans_amt = " + CNumeric(PadQuotes(option_item_price[a]))
			// + " WHERE pricetrans_price_id=" + price_id
			// + " AND pricetrans_item_id =" + CNumeric(PadQuotes(option_item_id[a]));
			// // SOP("opotion item update==update===" + StrSqlBreaker(StrSql));
			// stmttx.addBatch(StrSql);
			// } else {
			// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_price_trans("
			// + " pricetrans_price_id,"
			// + " pricetrans_item_id,"
			// + " pricetrans_amt"
			// + " )"
			// + " VALUES("
			// + " " + price_id + ","
			// + " " + CNumeric(PadQuotes(option_item_id[a])) + ","
			// + " " + CNumeric(PadQuotes(option_item_price[a])) + ""
			// + " )";
			// // SOP("opotion item update==add===" + StrSqlBreaker(StrSql));
			// stmttx.addBatch(StrSql);
			// }
			// }
			// }
			stmttx.executeBatch();
			updatepricecount++;
		} catch (Exception ex) {
			// SOP("1010");
			try {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					// SOP("rollback=====rollback");
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName() + ": "
						+ ex);
			} catch (SQLException e) {
				// SOP("1111");
				e.printStackTrace();
			}
		}

	}

	public void AddStock(String item_id) {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stock"
				+ "(stock_location_id, "
				+ " stock_item_id, "
				+ " stock_current_qty, "
				+ "	stock_unit_cost, "
				+ "	stock_entry_id, "
				+ "	stock_entry_date)"
				+ " VALUES("
				+ " " + inventory_location_id + ","
				+ " " + item_id + ","
				+ " " + item_qty + ","
				+ "	" + price_amt + ","
				+ " " + emp_id + ","
				+ " " + ToLongDate(kknow()) + ""
				+ ")";
		SOP("StrSql=stock==" + emp_id + "   ===" + StrSql);
		updateQuery(StrSql);
	}

	public String PopulateRateclass(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name, IF(rateclass_type = 1, 'Sales', 'Purchase') AS rateclass_type"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE 1=1"
					// + " AND rateclass_type= 1"
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

	public String PopulateItemType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT type_id, type_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_type"
					+ " WHERE 1=1"
					+ " ORDER BY type_id, type_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("type_id"));
				Str.append(StrSelectdrop(crs.getString("type_id"), item_type_id));
				Str.append(">").append(crs.getString("type_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			// SOP("1212");
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	public String PopulateStockLocation(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE 1=1"
					+ " ORDER BY location_id, location_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"), inventory_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			// SOP("1212");
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

}
