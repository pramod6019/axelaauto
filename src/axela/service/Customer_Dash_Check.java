/*sangita 15th july 2013*/
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Customer_Dash_Check extends Connect {

    public String customerinfo = "";
    public String customer_id = "0";
    public String emp_id = "";
    public String comp_id = "0";
    public String StrHTML = "";
//    public String model_id = "0";
    public String StrSql = "";
//    public String list_model_item = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
        emp_id = CNumeric(GetSession("emp_id", request));
        customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
        customerinfo = PadQuotes(request.getParameter("customerinfo"));
//        model_id = PadQuotes(request.getParameter("model_id"));
//        list_model_item = PadQuotes(request.getParameter("list_model_item"));
        if (customerinfo.equals("yes")) {
            StrHTML = new Enquiry_Dash_Customer().CustomerDetails(response, customer_id, "yes", comp_id);
        }
//        if (list_model_item.equals("yes")) {
//            StrHTML = new Vehicle_Update().PopulateItem(model_id);
//        }
    }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
