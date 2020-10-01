package axela.sales;
//divya 7th august
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Remote extends Connect {

    public String StrSql = "";
    public String comp_id = "0";
    public String item_id = "";
    public String url = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")) {
              item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
//            SOP("item_id--" + item_id);
              if(!item_id.equals("0")){
              StrSql = "SELECT item_url from " + compdb(comp_id) + "axela_inventory_item where item_id = " + item_id;
              url = ExecuteQuery(StrSql);
              }
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
