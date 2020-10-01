package axela.portal;

import java.io.File;
import java.io.IOException;
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

import cloudify.connect.Connect;

public class Docs_Update extends Connect {

	private String fileName;
	public String LinkHeader = "";
	public String add = "";
	public String update = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String doc_id = "0";
	public String doc_value = "";
	public String doc_title = "";
	public long config_doc_size;
	public String config_doc_format = "";
	public String[] docformat;
	public String doc_remarks = "";
	public String doc_entry_id = "0";
	public String doc_entry_by = "";
	public String doc_modified_id = "0";
	public String doc_entry_date = "";
	public String doc_modified_date = "";
	public String doc_modified_by = "";
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String exe_id = "0";
	public String customer_id = "0";
	public String asset_id = "0";
	// public String insurpolicy_id = "0";
	public String project_id = "0";
	public String task_id = "0";
	public String faq_id = "0";
	public String brand_id = "0", brochure_rateclass_id = "0", brochure_id = "0";
	public String brochure_model_id = "0";
	public String brochure_item_id = "0";
	public String item_name = "";
	public String ticketfaq_id = "0";
	public String enquiry_id = "0";
	public String preowned_id = "0";
	public String so_id = "0";
	public String ticket_id = "0";
	public String jc_id = "0";
	public String accessAdd = "";
	public String accessEdit = "";
	public String accessDelete = "";
	public String name = "";
	public String id = "0";
	public String fieldName = "";
	public String tableName = "";
	public String group = "";
	public String fieldId = "";
	public String url = "";
	public String filePrefix = "";
	public String docTable = "";
	public String updateUrl = "";
	public String docFieldId = "";
	public String msgValid = "";
	public String voucher_id = "";
	public String veh_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exe_id = CNumeric(PadQuotes(request.getParameter("exe_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				asset_id = CNumeric(PadQuotes(request.getParameter("asset_id")));
				// insurpolicy_id = CNumeric(PadQuotes(request.getParameter("insurpolicy_id")));
				project_id = CNumeric(PadQuotes(request.getParameter("project_id")));
				task_id = CNumeric(PadQuotes(request.getParameter("task_id")));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				brochure_rateclass_id = CNumeric(PadQuotes(request.getParameter("brochure_rateclass_id")));
				brochure_model_id = CNumeric(PadQuotes(request.getParameter("dr_model")));
				brochure_item_id = (PadQuotes(request.getParameter("dr_item")));
				brochure_id = CNumeric(PadQuotes(request.getParameter("brochure_id")));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				faq_id = CNumeric(PadQuotes(request.getParameter("faq_id")));
				ticketfaq_id = CNumeric(PadQuotes(request.getParameter("ticketfaq_id")));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				QueryString = PadQuotes(request.getQueryString());
				group = PadQuotes(request.getParameter("group"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));

				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				msg = PadQuotes(request.getParameter("msg"));
				doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));

				if (!brochure_id.equals("0")) {
					doc_id = brochure_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if (!exe_id.equals("0")) {
					name = ExecuteQuery("Select emp_name from " + compdb(comp_id) + "axela_emp where emp_id = " + exe_id);
					accessAdd = "emp_role_id";
					accessEdit = "emp_role_id";
					accessDelete = "emp_role_id";
					savePath = ExeDocPath(comp_id);
					id = exe_id;
					fieldName = "emp_name";
					tableName = "" + compdb(comp_id) + "axela_emp";
					fieldId = "emp_id";
					url = "../portal/executive-docs-list.jsp?emp_id=";
					filePrefix = "exedoc_";
					docTable = "" + compdb(comp_id) + "axela_emp_docs";
					updateUrl = "../portal/docs-update.jsp?emp_id=";
					docFieldId = "doc_emp_id";
					LinkHeader = "<a href=../portal/home.jsp>Home</a>"
							+ " &gt; <a href=manager.jsp>Business Manager</a>"
							+ " &gt; <a href=executive-list.jsp?all=yes>List Executives</a>"
							+ " &gt; <a href=executive-list.jsp?emp_id=" + emp_id + ">" + name + "</a>"
							+ " &gt; <a href=executive-docs-list.jsp?emp_id=" + emp_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a><b>:</b>";
				}
				if (!customer_id.equals("0")) {
					name = ExecuteQuery("SELECT customer_name FROM " + compdb(comp_id) + "axela_customer where customer_id = " + customer_id);
					if (!name.equals("")) {
						accessAdd = "emp_customer_add";
						accessEdit = "emp_customer_edit";
						accessDelete = "emp_customer_delete";
						savePath = CustomerDocPath(comp_id);
						id = customer_id;
						fieldName = "customer_name";
						tableName = "" + compdb(comp_id) + "axela_customer";
						fieldId = "customer_id";
						url = "../customer/customer-docs-list.jsp?customer_id=";
						filePrefix = "customerdoc_";
						docTable = "" + compdb(comp_id) + "axela_customer_docs";
						updateUrl = "../portal/docs-update.jsp?customer_id=";
						docFieldId = "doc_customer_id";
						LinkHeader = "<a href=../customer/home.jsp>Home</a>"
								+ " &gt; <a href=../customer/customer.jsp>Customers</a>"
								+ " &gt; <a href=../customer/customer-list.jsp?all=yes>List Customers</a>"
								+ " &gt; <a href=../customer/customer-list.jsp?customer_id=" + customer_id + ">" + name + "</a>"
								+ " &gt; <a href=../customer/customer-docs-list.jsp?customer_id=" + customer_id + ">List Documents</a>"
								+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
					} else {
						response.sendRedirect("../portal/error.jsp?msg=Invalid Customer!");
					}
				}
				// Vehicle ////
				if (!veh_id.equals("0")) {
					name = ExecuteQuery("SELECT doc_title FROM " + compdb(comp_id) + "axela_service_veh_docs WHERE doc_veh_id = " + veh_id);

					accessAdd = "emp_service_vehicle_add";
					accessEdit = "emp_service_vehicle_edit";
					accessDelete = "emp_service_vehicle_delete";
					savePath = VehicleDocPath(comp_id);
					id = veh_id;
					fieldName = "veh_id";
					tableName = "" + compdb(comp_id) + "axela_service_veh";
					fieldId = "veh_id";
					url = "../service/vehicle-dash.jsp?veh_id=";
					filePrefix = "vehdoc_";
					docTable = "" + compdb(comp_id) + "axela_service_veh_docs";
					updateUrl = "../portal/docs-update.jsp?veh_id=";
					docFieldId = "doc_veh_id";
					LinkHeader = "<a href=../service/home.jsp>Home</a> &gt;"
							+ " <a href=../service/index.jsp>Service</a> &gt;"
							+ " <a href=../service/vehicle.jsp>Vehicles</a> &gt;"
							+ " <a href=../service/vehicle-list.jsp?all=yes>List Vehicles</a> &gt;"
							// + " <a href=../service/vehicle-list.jsp?veh_id=" + veh_id + ">" + name + "</a> &gt;"
							+ " <a href=../sales//vehicle-dash.jsp?veh_id=" + veh_id + ">List Documents</a> &gt; "
							+ " <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}

				// // insurance
				// if (!insurpolicy_id.equals("0")) {
				// name = ExecuteQuery("SELECT insurpolicy_id"
				// + " FROM " + compdb(comp_id) + "axela_insurance_policy"
				// + " WHERE insurpolicy_id = " + insurpolicy_id + "");
				// accessAdd = "emp_service_insurance_add";
				// accessEdit = "emp_service_insurance_edit";
				// accessDelete = "emp_service_insurance_delete";
				// savePath = ContractDocPath(comp_id);
				// id = insurpolicy_id;
				// fieldName = "insurpolicy_id";
				// tableName = "" + compdb(comp_id) + "axela_insurance_policy";
				// fieldId = "insurpolicy_id";
				// url = "../insurance/insurance-docs-list.jsp?insurpolicy_id=";
				// filePrefix = "insurdoc_";
				// docTable = "" + compdb(comp_id) + "axela_insurance_docs";
				// updateUrl = "../portal/docs-update.jsp?insurpolicy_id=";
				// docFieldId = "doc_insur_id";
				// LinkHeader =
				// "<a href=../service/home.jsp>Home</a> &gt; <a href=../insurance/index.jsp>Insurance</a> &gt;<a href=../insurance/insurance-list.jsp?all=yes>List Insurance</a> &gt; <a href=../insurance/insurance-list.jsp?insurpolicy_id="
				// + insurpolicy_id
				// + ">"
				// + name
				// + "</a> &gt; <a href=../insurance/insurance-docs-list.jsp?insurpolicy_id="
				// + insurpolicy_id
				// + " > List Documents</a> &gt; <a href=../portal/docs-update.jsp?"
				// + QueryString + ">" + status + " Document</a>:";
				// }

				if (!brochure_rateclass_id.equals("0")) {
					String brand_name = ExecuteQuery("Select brand_name from axela_brand where brand_id = " + brand_id + "");
					String rateclass_name = ExecuteQuery("Select rateclass_name from " + compdb(comp_id) + "axela_rate_class where rateclass_id = " + brochure_rateclass_id + "");
					item_name = ExecuteQuery("Select item_name from " + compdb(comp_id) + "axela_inventory_item where item_id = " + brochure_item_id + "");
					if (brochure_item_id.equals("0") || brochure_item_id.equals("-1")) {
						item_name = "General";
						brochure_model_id = "0";
					}
					name = ExecuteQuery("Select brochure_title"
							+ " from " + compdb(comp_id) + "axela_sales_enquiry_brochure"
							+ " where brochure_id = " + brochure_id + "");

					accessAdd = "emp_executive_add";
					accessEdit = "emp_executive_edit";
					accessDelete = "emp_executive_edit";
					savePath = EnquiryBrochurePath(comp_id);
					id = "&brochure_id=" + brochure_id;
					fieldName = "brochure_title";
					tableName = "" + compdb(comp_id) + "axela_sales_enquiry_brochure";
					fieldId = "brochure_id";
					url = "../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
							+ "&dr_item=" + brochure_item_id;
					filePrefix = "brochure_";
					docTable = "" + compdb(comp_id) + "axela_sales_enquiry_brochure";
					updateUrl = "../portal/docs-update.jsp?brochure_id=";
					docFieldId = "brochure_rateclass_id";
					if (!brochure_item_id.equals("0")) {
						LinkHeader = "<a href=../portal/home.jsp>Home</a>"
								+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
								+ " &gt; <a href=../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
								+ "&dr_item=" + brochure_item_id
								+ ">List Brochure</a>"
								+ " &gt; <a href=../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
								+ "&dr_item=" + brochure_item_id + ">"
								+ brand_name + "</a>"
								+ " &gt; <a href=../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
								+ "&dr_item=" + brochure_item_id + ">"
								+ item_name + "</a>"
								+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Brochure</a>:";
					} else {
						LinkHeader = "<a href=../portal/home.jsp>Home</a>"
								+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
								+ " &gt; <a href=../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
								+ "&dr_item=" + brochure_item_id
								+ ">List Brochure</a>"
								+ " &gt; <a href=../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
								+ "&dr_item=" + brochure_item_id + ">"
								+ brand_name + "</a>"
								+ " &gt; <a href=../sales/managebrochure.jsp?brand_id= " + brand_id + "&brochure_rateclass_id=" + brochure_rateclass_id + "&dr_model=" + brochure_model_id
								+ "&dr_item=" + brochure_item_id
								+ ">  General  </a>"
								+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Brochure</a>:";
					}
					// SOP("LinkHeader-------" + LinkHeader);
				}

				if (!enquiry_id.equals("0")) {
					name = ExecuteQuery("Select enquiry_title from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id = " + enquiry_id + "");
					accessAdd = "emp_enquiry_add";
					accessEdit = "emp_enquiry_edit";
					accessDelete = "emp_enquiry_delete";
					savePath = EnquiryDocPath(comp_id);
					id = enquiry_id;
					fieldName = "enquiry_no";
					tableName = "" + compdb(comp_id) + "axela_sales_enquiry";
					fieldId = "enquiry_id";
					url = "../sales/enquiry-dash.jsp?enquiry_id=";
					filePrefix = "enquirydoc_";
					docTable = "" + compdb(comp_id) + "axela_sales_enquiry_docs";
					updateUrl = "../portal/docs-update.jsp?enquiry_id=";
					docFieldId = "doc_enquiry_id";
					LinkHeader = "<a href=../sales/home.jsp>Home</a>"
							+ " &gt; <a href=../sales/index.jsp>Sales</a>"
							+ " &gt; <a href=../sales/enquiry.jsp>Enquiry</a>"
							+ " &gt; <a href=../sales/enquiry-list.jsp?all=yes>List Enquiry</a>"
							+ " &gt; <a href=../sales/enquiry-list.jsp?enquiry_id=" + enquiry_id + ">" + name + "</a>"
							+ " &gt; <a href=../sales/enquiry-dash.jsp?enquiry_id=" + enquiry_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}
				if (!voucher_id.equals("0")) {
					// name = ExecuteQuery("Select enquiry_title from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id = " + enquiry_id + "");
					accessAdd = "emp_acc_receipt_add";
					accessEdit = "emp_acc_receipt_edit";
					accessDelete = "emp_acc_receipt_delete";
					savePath = EnquiryDocPath(comp_id);
					id = voucher_id;
					fieldName = "voucher_no";
					tableName = "" + compdb(comp_id) + "axela_sales_enquiry";
					fieldId = "doc_id";
					url = "../sales/enquiry-dash.jsp?enquiry_id=";
					filePrefix = "receiptdoc_";

					docTable = "" + compdb(comp_id) + "axela_acc_docs";

					updateUrl = "../portal/docs-update.jsp?enquiry_id=";

					docFieldId = "doc_enquiry_id";
					LinkHeader = "<a href=../sales/home.jsp>Home</a>"
							+ " &gt; <a href=../accounting/index.jsp>Sales</a>"
							+ " &gt; <a href=../accounting/voucher-list.jsp?all=yes&voucherclass_id=9&vouchertype_id=9> List Receipts</a>"
							+ " &gt; <a href=../accounting/voucher-doc-list.jsp?update=yes&vouchertype_id=9&voucher_id=" + voucher_id + ">List Documents</a>";
				}
				if (!preowned_id.equals("0")) {
					name = ExecuteQuery("SELECT preowned_title FROM " + compdb(comp_id) + "axela_preowned where preowned_id = " + preowned_id + "");
					accessAdd = "emp_preowned_add";
					accessEdit = "emp_preowned_edit";
					accessDelete = "emp_preowned_delete";
					savePath = PreownedDocPath(comp_id);
					id = preowned_id;
					fieldName = "preowned_no";
					tableName = "" + compdb(comp_id) + "axela_preowned";
					fieldId = "preowned_id";
					url = "../preowned/preowned-dash-docs.jsp?preowned_id=";
					filePrefix = "preowneddoc_";
					docTable = "" + compdb(comp_id) + "axela_preowned_docs";
					updateUrl = "../portal/docs-update.jsp?preowned_id=";
					docFieldId = "doc_preowned_id";
					LinkHeader = "<a href=../preowned/home.jsp>Home</a>"
							+ " &gt; <a href=../preowned/index.jsp>Pre Owned</a>"
							+ " &gt; <a href=../preowned/preowned.jsp>Pre Owned</a>"
							+ " &gt; <a href=../preowned/preowned-list.jsp?all=yes>List Pre Owned</a>"
							+ " &gt; <a href=../preowned/preowned-list.jsp?preowned_id=" + preowned_id + ">" + name + "</a>"
							+ " &gt; <a href=../preowned/preowned-dash-docs.jsp?preowned_id=" + preowned_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}
				if (!so_id.equals("0")) {
					name = ExecuteQuery("Select so_id from " + compdb(comp_id) + "axela_sales_so where so_id = " + so_id + "");
					accessAdd = "emp_sales_order_add";
					accessEdit = "emp_sales_order_edit";
					accessDelete = "emp_sales_order_delete";
					savePath = SODocPath(comp_id);
					id = so_id;
					fieldName = "so_no";
					tableName = "" + compdb(comp_id) + "axela_sales_so";
					fieldId = "so_id";
					url = "../sales/veh-salesorder-doc-list.jsp?so_id=";
					filePrefix = "sodoc_";
					docTable = "" + compdb(comp_id) + "axela_sales_so_docs";
					updateUrl = "../portal/docs-update.jsp?so_id=";
					docFieldId = "doc_so_id";
					LinkHeader = "<a href=../sales/home.jsp>Home</a>"
							+ " &gt; <a href=../sales/index.jsp>Sales</a>"
							+ " &gt; <a href=../sales/veh-salesorder.jsp>Sales Orders</a>"
							+ " &gt; <a href=../sales/veh-salesorder-list.jsp?all=yes>List Sales Orders</a>"
							+ " &gt; <a href=../sales/veh-salesorder-list.jsp?so_id=" + so_id + ">" + name + "</a>"
							+ " &gt; <a href=../sales/veh-salesorder-doc-list.jsp?so_id=" + so_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}
				if (!faq_id.equals("0")) {
					accessAdd = "emp_faq_add";
					accessEdit = "emp_faq_edit";
					accessDelete = "emp_faq_delete";
					savePath = FaqExePath(comp_id);
					id = faq_id;
					fieldName = "faq_question";
					tableName = "" + compdb(comp_id) + "axela_faq";
					fieldId = "faq_id";
					url = "../portal/faqexecutive-doc-list.jsp?faq_id=";
					filePrefix = "exefaqdoc_";
					docTable = "" + compdb(comp_id) + "axela_faq_docs";
					updateUrl = "../portal/docs-update.jsp?faq_id=";
					docFieldId = "doc_faq_id";
					LinkHeader = "<a href=../portal/home.jsp>Home</a>"
							+ " &gt; <a href=../portal/index.jsp>Sales</a>"
							+ " &gt; <a href=../portal/salesorder.jsp>Sales Orders</a>"
							+ " &gt; <a href=../portal/salesorder-list.jsp?all=yes>List Sales Orders</a>"
							+ " &gt; <a href=../portal/salesorder-list.jsp?so_id=" + so_id + ">" + name + "</a>"
							+ " &gt; <a href=../portal/salesorder-doc-list.jsp?so_id=" + so_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}
				if (!ticketfaq_id.equals("0")) {
					name = ExecuteQuery("Select ticketfaq_question from " + compdb(comp_id) + "axela_service_ticket_faq where ticketfaq_id =" + ticketfaq_id + "");
					accessAdd = "emp_service_faq_add";
					accessEdit = "emp_service_faq_edit";
					accessDelete = "emp_service_faq_delete";
					savePath = FaqDocPath(comp_id);
					id = ticketfaq_id;
					fieldName = "ticketfaq_question";
					tableName = "" + compdb(comp_id) + "axela_service_ticket_faq";
					fieldId = "ticketfaq_id";
					url = "../service/ticket-faq-doc-list.jsp?ticketfaq_id=";
					filePrefix = "ticketfaqdoc_";
					docTable = "" + compdb(comp_id) + "axela_service_ticket_faq_docs";
					updateUrl = "../portal/docs-update.jsp?ticketfaq_id=";
					docFieldId = "doc_ticketfaq_id";
					LinkHeader = "<a href=../service/home.jsp>Home</a>"
							+ " &gt; <a href=../service/index.jsp>Service</a>"
							+ " &gt; <a href=../service/ticket-faq-list.jsp?all=yes>List FAQs</a>"
							+ " &gt; <a href=../service/ticket-faq-list.jsp?ticketfaq_id=" + ticketfaq_id + ">" + name + "</a>"
							+ " &gt; <a href=../service/ticket-faq-doc-list.jsp?ticketfaq_id=" + ticketfaq_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}
				if (!ticket_id.equals("0")) {
					name = ExecuteQuery("select ticket_subject from " + compdb(comp_id) + "axela_service_ticket where ticket_id=" + ticket_id + "");
					accessAdd = "emp_ticket_add";
					accessEdit = "emp_ticket_edit";
					accessDelete = "emp_ticket_delete";
					savePath = TicketDocPath(comp_id);
					id = ticket_id;
					fieldName = "ticket_id";
					tableName = "" + compdb(comp_id) + "axela_service_ticket";
					fieldId = "ticket_id";
					url = "../service/ticket-dash.jsp?ticket_id=";
					filePrefix = "ticketdoc_";
					docTable = "" + compdb(comp_id) + "axela_service_ticket_docs";
					updateUrl = "../portal/docs-update.jsp?ticket_id=";
					docFieldId = "doc_ticket_id";
					LinkHeader = "<a href=../service/home.jsp>Home</a>"
							+ " &gt; <a href=../service/index.jsp>Service</a>"
							+ " &gt; <a href=../service/ticket.jsp>Ticket</a>"
							+ " &gt; <a href=../service/ticket-list.jsp?all=yes>List Tickets</a>"
							+ " &gt; <a href=../service/ticket-list.jsp?ticket_id=" + ticket_id + ">" + name + "</a>"
							+ " &gt; <a href=../service/ticket-dash-attachment.jsp?ticket_id=" + ticket_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a>:";
				}

				if (!jc_id.equals("0")) {
					name = ExecuteQuery("select jc_title from " + compdb(comp_id) + "axela_service_jc where jc_id=" + jc_id + "");
					accessAdd = "emp_role_id";
					accessEdit = "emp_role_id";
					accessDelete = "emp_role_id";
					savePath = JobCardDocPath(comp_id);
					id = jc_id;
					fieldName = "jc_id";
					tableName = "" + compdb(comp_id) + "axela_service_jc";
					fieldId = "jc_id";
					url = "../service/jobcard-dash.jsp?jc_id=";
					filePrefix = "scdoc_";
					docTable = "" + compdb(comp_id) + "axela_service_jc_docs";
					updateUrl = "../portal/docs-update.jsp?jc_id=";
					docFieldId = "doc_jc_id";
					LinkHeader = "<a href=../service/home.jsp>Home</a>"
							+ " &gt; <a href=../service/index.jsp>Service</a>"
							+ " &gt; <a href=../service/jobcard.jsp>Job Card</a>"
							+ " &gt; <a href=../service/jobcard-list.jsp?all=yes>List Job Cards</a>"
							+ " <a href=../service/jobcard-list.jsp?jc_id=" + jc_id + ">" + name + "</a>"
							+ " &gt; <a href=../service/jobcard-dash-docs.jsp?jc_id=" + jc_id + ">List Documents</a>"
							+ " &gt; <a href=../portal/docs-update.jsp?" + QueryString + ">" + status + " Document</a><b>:</b>";
				}
				PopulateConfigDetails(response);
				CheckPerm(comp_id, accessAdd, request, response);
				if (update.equals("yes")) {
					PopulateFields(request, response);
				}
				String button_name = "";
				boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
				if (isMultipart) {
					// Create a factory for disk-based file items
					DiskFileItemFactory factory = new DiskFileItemFactory();
					// Set factory constraints
					factory.setSizeThreshold((1024 * 1024 * (int) config_doc_size) + (1024 * 1024));
					// File f = new File("d:/");
					File f = new File(savePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					factory.setRepository(f);
					// Create a new file upload handler
					ServletFileUpload upload = new ServletFileUpload(factory);
					// Set overall request size constraint
					upload.setSizeMax((1024 * 1024 * (int) config_doc_size) + (1024 * 1024));
					// Parse the request
					List items = upload.parseRequest(request);
					Iterator it = items.iterator();
					for (int i = 0; it.hasNext() && i < 9; i++) {
						FileItem button = (FileItem) it.next();
						if (button.isFormField()) {
							str1[i] = button.getString();
							// SOP("str1[i]=--=="+str1[i]);
						}
					}

					Iterator iter = items.iterator();
					if (add.equals("yes")) {
						button_name = "Add Document";
					} else if (update.equals("yes")) {
						button_name = "Update Document";
					}
					if (!brochure_rateclass_id.equals("0") && add.equals("yes")) {
						button_name = "Add Brochure";
					} else if (!brochure_rateclass_id.equals("0") && update.equals("yes")) {
						button_name = "Update Brochure";
					}

					for (int i = 0; iter.hasNext() && i < 9; i++) {
						// SOP("str1[i]==="+str1[i]);
						if (str1[i].equals(button_name) && add.equals("yes")) {
							FileItem item = (FileItem) iter.next();
							if (!item.isFormField()) {
								fileName = item.getName();
								doc_entry_id = emp_id;
								doc_entry_date = ToLongDate(kknow());
								// CheckPerm(comp_id,"emp_executive_access,emp_executive_add,emp_executive_edit,emp_executive_delete",request,response);
								CheckForm();
								if (!fileName.equals("") && fileName.contains(".")) {
									String temp = "";
									fileName = item.getName();
									// SOP("fileName-------" + fileName);
									// SOP("fileName-1------" +
									// fileName.substring(fileName.lastIndexOf(".")).toLowerCase());
									if (!fileName.equals("") && fileName.contains(".")) {
										docformat = config_doc_format.split(", ");
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
								} else {
									msg = msg + "<br>Invalid file format!";
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (!fileName.equals("")) {
										// GetValues(request,response);
										AddFields();
										File uploadedFile = new File(savePath + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										if (brochure_rateclass_id.equals("0")) {
											if (!enquiry_id.equals("0")) {
												response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document added successfully!#tabs-5"));
											} else if (!veh_id.equals("0")) {
												response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document added successfully!#tabs-14"));
											}
											else if (!jc_id.equals("0")) {
												response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document added successfully!#tabs-7"));
											} else if (!ticket_id.equals("0")) {
												response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document added successfully!#tabs-4"));
											} else {
												response.sendRedirect(response.encodeRedirectURL(url + id + "&msg=Document added successfully!"));
											}
										}
										if (!brochure_rateclass_id.equals("0")) {
											response.sendRedirect(response.encodeRedirectURL(url + "&dr_brand_id=" + brand_id + "&dr_rateclass_id=" + brochure_rateclass_id
													+ "&brochure_id=" + brochure_id + "&msg=Brochure added successfully!"));
										}
									}
								}
							}
						}
						if (str1[i].equals(button_name) && update.equals("yes")) {
							if (ReturnPerm(comp_id, accessEdit, request).equals("1"))
							{
								FileItem item = (FileItem) iter.next();
								if (!item.isFormField()) {
									fileName = item.getName();
									CheckForm();
									if (!fileName.equals("") && fileName.contains(".")) {
										String temp = "";
										int pos = fileName.lastIndexOf(".");
										if (pos != -1) {
											fileName = filePrefix + doc_id + fileName.substring(pos);
										}
										docformat = config_doc_format.split(", ");
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
									if (!msg.equals("")) {
										msg = "Error!" + msg;
									} else {
										doc_modified_id = emp_id;
										doc_modified_date = ToLongDate(kknow());
										UpdateFieldsWithoutFile();
										if (!fileName.equals("")) {
											String prevFile = "";
											if (brochure_id.equals("0")) {
												prevFile = ExecuteQuery("Select doc_value from " + docTable + " where doc_id = " + doc_id + "");
											} else if (!brochure_id.equals("0")) {
												prevFile = ExecuteQuery("Select brochure_value from " + docTable + " where brochure_id = " + brochure_id + "");
											}
											if (!prevFile.equals("")) {
												File uploadedFileprevFile = new File(savePath + prevFile);
												if (uploadedFileprevFile.exists()) {
													uploadedFileprevFile.delete();
												}
											}
											UpdateFields();
											File uploadedFile = new File(savePath + fileName);
											if (uploadedFile.exists()) {
												uploadedFile.delete();
											}
											item.write(uploadedFile);
											if (brochure_rateclass_id.equals("0")) {
												if (!enquiry_id.equals("0")) {
													response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document updated successfully!#tabs-5"));
												} else if (!veh_id.equals("0")) {
													response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document updated successfully!#tabs-14"));
												} else if (!jc_id.equals("0")) {
													response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document updated successfully!#tabs-7"));
												} else if (!ticket_id.equals("0")) {
													response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document updated successfully!#tabs-4"));
												} else {
													response.sendRedirect(response.encodeRedirectURL(url + id + "&msg=Document updated successfully!"));
												}
											}
											if (!brochure_rateclass_id.equals("0") && !brand_id.equals("0")) {
												response.sendRedirect(response.encodeRedirectURL(url + "&dr_brand_id=" + brand_id + "&dr_rateclass_id= " + brochure_rateclass_id
														+ "&msg=Brochure updated successfully!"));
											}
										}
									}
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
						if (str1[i].equals("Delete Document") || str1[i].equals("Delete Brochure")) {
							if (ReturnPerm(comp_id, accessDelete, request).equals("1")) {
								if (brochure_id.equals("0")) {
									fileName = ExecuteQuery("Select doc_value from " + docTable + " where doc_id = " + doc_id + "");
								} else if (!brochure_id.equals("0")) {
									fileName = ExecuteQuery("Select brochure_value from " + docTable + " where brochure_id = " + brochure_id);
								}
								File uploadedFile = new File(savePath + fileName);
								if (uploadedFile.exists()) {
									uploadedFile.delete();
								}
								DeleteFields();
								if (brochure_rateclass_id.equals("0")) {
									if (!enquiry_id.equals("0")) {
										response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document deleted successfully!#tabs-5"));
									}
									else if (!veh_id.equals("0")) {
										response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document deleted successfully!#tabs-14"));
									}
									if (!jc_id.equals("0")) {
										response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document deleted successfully!#tabs-7"));
									}
									if (!ticket_id.equals("0")) {
										response.sendRedirect(response.encodeRedirectURL(url + id + "&docmsg=Document deleted successfully!#tabs-4"));
									} else {
										response.sendRedirect(response.encodeRedirectURL(url + id + "&msg=Document deleted successfully!"));
									}
								}
								if (!brochure_rateclass_id.equals("0")) {
									response.sendRedirect(response.encodeRedirectURL(url + "&msg=Brochure deleted successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}
				}
			}
		} catch (FileUploadException Fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			if (status.equals("add")) {
				response.sendRedirect(updateUrl + id + "&add=yes&msg=" + msg);
			}
			if (status.equals("update")) {
				response.sendRedirect(updateUrl + id + "&update=yes&doc_id=" + doc_id + "&msg=" + msg);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

	// protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// doc_title = PadQuotes(request.getParameter("txt_doc_name"));
	// }

	protected void AddFields() {
		if (msg.equals("")) {
			try {
				if (brochure_rateclass_id.equals("0")) {
					doc_id = ExecuteQuery("Select coalesce(max(doc_id),0)+1 as doc_id from " + docTable);
				} else if (!brochure_rateclass_id.equals("0")) {
					brochure_id = ExecuteQuery("Select coalesce(max(brochure_id),0)+1 from " + docTable);
				}
				int pos = fileName.lastIndexOf(".");
				if (pos != -1) {
					if (!doc_id.equals("0")) {
						fileName = filePrefix + doc_id + fileName.substring(pos);
					} else if (!brochure_id.equals("0")) {
						fileName = filePrefix + brochure_id + fileName.substring(pos);
					}
				}
				if (brochure_rateclass_id.equals("0")) {
					StrSql = "Insert into " + docTable
							+ " (doc_id,"
							+ " " + docFieldId + ","
							+ " doc_value,"
							+ " doc_title,"
							+ " doc_entry_id,"
							+ " doc_entry_date,"
							+ " doc_remarks)"
							+ " values"
							+ " ('" + doc_id + "',"
							+ " " + id + ","
							+ " '" + fileName + "',"
							+ " '" + str1[1] + "',"
							+ " " + doc_entry_id + ","
							+ " '" + doc_entry_date + "',"
							+ " '" + str1[2] + "')";
					SOP("StrSql" + StrSqlBreaker(StrSql));

				} else if (!brochure_rateclass_id.equals("0")) {
					if (brochure_item_id.equals("-1")) {
						brochure_item_id = "0";
					}
					StrSql = "Insert into " + docTable
							+ " (brochure_id,"
							+ " brochure_brand_id, "
							+ " brochure_rateclass_id	,"
							// + " brochure_model_id,"
							+ " brochure_item_id,"
							+ " brochure_value,"
							+ " brochure_title,"
							+ " brochure_rank,"
							+ " brochure_entry_id,"
							+ " brochure_entry_date)"
							+ " values"
							+ " (" + brochure_id + ","
							+ " " + brand_id + ","
							+ " " + brochure_rateclass_id + ","
							// + " " + brochure_model_id + ","
							+ " " + brochure_item_id + ","
							+ " '" + fileName + "',"
							+ " '" + str1[1] + "',"
							+ " (Select Coalesce(max(enquiry_brochure.brochure_rank),0)+1"
							+ " from " + compdb(comp_id) + "axela_sales_enquiry_brochure as enquiry_brochure),"
							+ " " + doc_entry_id + ","
							+ " '" + doc_entry_date + "')";

					// SOP("StrSql=insert-----brocher-----" +
					// StrSqlBreaker(StrSql));
				}
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (brochure_id.equals("0") && brochure_rateclass_id.equals("0")) {
				StrSql = "Select * from " + docTable + " where doc_id = " + doc_id + "";
			} else if (!brochure_id.equals("0") && !brochure_rateclass_id.equals("0")) {
				StrSql = "Select brochure_title as doc_title,"
						+ " '' as doc_remarks,"
						+ " brochure_item_id,"
						+ " brochure_value as doc_value,"
						+ " brochure_entry_id as doc_entry_id,"
						+ " brochure_entry_date as doc_entry_date,"
						+ " brochure_modified_id as doc_modified_id,"
						+ " brochure_modified_date as doc_modified_date"
						+ " from " + docTable + ""
						+ " where brochure_id = " + brochure_id + ""
						+ " and brochure_rateclass_id	 = " + brochure_rateclass_id + "";

			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Document!"));
			}
			// SOP("StrSql==PopulateFields======" + StrSqlBreaker(StrSql));
			if (!doc_id.equals("0") || !brochure_id.equals("0")) {
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						doc_title = crs.getString("doc_title");
						doc_remarks = crs.getString("doc_remarks");
						doc_value = crs.getString("doc_value");

						if (!brochure_id.equals("0") && !brochure_rateclass_id.equals("0")) {
							brochure_item_id = crs.getString("brochure_item_id");
						}
						doc_entry_id = crs.getString("doc_entry_id");
						if (!doc_entry_id.equals("")) {
							doc_entry_by = Exename(comp_id, Integer.parseInt(doc_entry_id));
						}
						doc_entry_date = strToLongDate(crs.getString("doc_entry_date"));
						doc_modified_id = crs.getString("doc_modified_id");
						if (!doc_modified_id.equals("0")) {
							doc_modified_by = Exename(comp_id, Integer.parseInt(doc_modified_id));
							doc_modified_date = strToLongDate(crs.getString("doc_modified_date"));
						}
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Document!"));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void UpdateFieldsWithoutFile() {
		if (msg.equals("")) {
			try {
				if (brochure_rateclass_id.equals("0")) {
					StrSql = "UPDATE  " + docTable + ""
							+ " SET"
							+ " doc_title = '" + str1[1] + "',"
							+ " doc_modified_id = " + doc_modified_id + ","
							+ " doc_modified_date = '" + doc_modified_date + "',"
							+ " doc_remarks = '" + str1[2] + "'"
							+ " where doc_id = " + doc_id + "";
				} else if (!brochure_rateclass_id.equals("0")) {
					StrSql = "UPDATE  " + docTable + ""
							+ " SET"
							+ " brochure_title = '" + str1[1] + "',"
							+ " brochure_modified_id = " + doc_modified_id + ","
							+ " brochure_modified_date = '" + doc_modified_date + "'"
							+ " where brochure_id = " + brochure_id + "";
				}
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				if (brochure_rateclass_id.equals("0")) {
					StrSql = "UPDATE " + docTable
							+ " SET"
							+ " doc_value = '" + fileName + "',"
							+ " doc_title = '" + str1[1] + "',"
							+ " doc_modified_id = " + doc_modified_id + ","
							+ " doc_modified_date = '" + doc_modified_date + "',"
							+ " doc_remarks = '" + str1[2] + "'"
							+ " where doc_id = " + doc_id + "";

				} else if (!brochure_rateclass_id.equals("0")) {
					StrSql = "UPDATE " + docTable
							+ " SET"
							+ " brochure_value = '" + fileName + "',"
							+ " brochure_title = '" + str1[1] + "',"
							+ " brochure_modified_id = " + doc_modified_id + ","
							+ " brochure_modified_date = '" + doc_modified_date + "'"
							+ " where brochure_id = " + brochure_id + "";
				}
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				if (brochure_id.equals("0")) {
					StrSql = "Delete from " + docTable + ""
							+ " where doc_id = " + doc_id + "";
				} else if (!brochure_id.equals("0")) {
					StrSql = "Delete from " + docTable + ""
							+ " where brochure_id = " + brochure_id + "";
				}
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {
		msg = "";
		String Msg1 = "", Msg2 = "";
		doc_remarks = "";
		doc_title = "";
		// if (update.equals("yes") && fileName.equals("")) {
		// msg = msg + "<br>Select Document!";
		// } else {
		// if (add.equals("yes") && fileName.equals("")) {
		// msg = msg + "<br>Select Document!";
		// }
		// }
		if (update.equals("yes") && fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		} else {
			if (add.equals("yes") && fileName.equals("")) {
				msg = msg + "<br>Select Document!";
			}
		}
		for (int i = 0; i < str1.length; i++) {
			if (!str1[1].equals("")) {
				doc_title = str1[1];
			} else {
				Msg1 = "<br>Enter Document Name!";
			}
			if (!str1[2].equals("")) {
				doc_remarks = str1[2];
			}
		}
		// if (!brochure_rateclass_id.equals("0")) {
		// for (int i = 0; i < str1.length; i++) {
		// if (!str1[1].equals("")) {
		// doc_title = str1[1];
		// String sametitle = ExecuteQuery("SELECT brochure_title"
		// + " FROM " + compdb(comp_id) + "axela_sales_enquiry_brochure"
		// + " WHERE 1=1"
		// + " AND brochure_brand_id = " + brand_id + ""
		// + " AND brochure_rateclass_id	 = " + brochure_rateclass_id + ""
		// + " AND brochure_model_id = " + brochure_model_id + ""
		// + " AND brochure_item_id = " + brochure_item_id + ""
		// + " AND brochure_title = '" + doc_title + "'");
		//
		// if (!sametitle.equals("")) {
		// Msg2 = "<br>Similar Document Name found!";
		// }
		// // msg = msg + CheckSpecialCharacters(doc_title);
		// } else {
		// Msg1 = "<br>Enter Document Name!";
		// }
		// if (!str1[2].equals("")) {
		// doc_remarks = str1[2];
		// }
		// }
		// }
		if (!brochure_rateclass_id.equals("0")) {
			if (str1[2].equals("-1")) {
				msg = msg + "<br>Select Model!";
			}
		}
		msg = msg + Msg1 + Msg2;
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select model_id, model_name"
					+ " from " + compdb(comp_id) + "axela_inventory_item_model"
					+ " order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (!str1[2].equals("")) {
				brochure_model_id = str1[2];
			}
			Str.append("<option value=\"-1\">Select</option>");
			Str.append("<option value=\"0\" ").append(StrSelectdrop("0", brochure_model_id)).append(">General</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), brochure_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	protected void PopulateConfigDetails(HttpServletResponse response) {
		try {
			if (add.equals("yes")) {
				fieldName = "''";
				tableName = "''";
			}
			if (!fieldId.equals("")) {
				if (add.equals("yes")) {
					StrSql = "Select config_doc_size, config_doc_formats"
							+ " from " + compdb(comp_id) + "axela_config";
				} else {
					StrSql = "Select config_doc_size, config_doc_formats, " + fieldName
							+ " from " + compdb(comp_id) + "axela_config, " + tableName + "";
					if (brochure_rateclass_id.equals("0")) {
						StrSql = StrSql + " where " + fieldId + " = " + id + "";
					}
				}
				// SOP("StrSql-PopulateConfigDetails--" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						config_doc_size = (long) crs.getDouble("config_doc_size");
						config_doc_format = crs.getString("config_doc_formats");
						// SOP("config_doc_size--------" + config_doc_size);
						if (update.equals("yes")) {
							name = crs.getString(fieldName);
						}
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Order" + msg));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
