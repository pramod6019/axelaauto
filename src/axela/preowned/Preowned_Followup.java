package axela.preowned;
// 
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Followup extends Connect {

    public String preowned_id = "0";
    public String StrSql = "";
    public String comp_id = "0";
    public String StrSearch = "";
    public String StrHTML = "";

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	CheckSession(request, response);
        HttpSession session = request.getSession(true);
        comp_id = CNumeric(GetSession("comp_id", request));
        if(!comp_id.equals("0"))
        {
        	preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
            if (!preowned_id.equals("0")) {
                StrHTML = ListFollowup();
            }
        }
        
    }

    public String ListFollowup() {
        StringBuilder Str = new StringBuilder();
        try {
            StrSql = "SELECT date_format(preownedfollowup_followup_time,'%d-%m-%Y %H:%i') AS preownedfollowuptime, preownedfollowuptype_name, preownedfollowup_desc "
                    + " FROM " + compdb(comp_id) + "axela_preowned_followup"
                    + " INNER JOIN " + compdb(comp_id) + "axela_preowned_followup_type ON preownedfollowuptype_id = preownedfollowup_preownedfollowuptype_id "
                    + " WHERE preownedfollowup_preowned_id = " + preowned_id
                    + " ORDER BY preownedfollowup_followup_time desc";

            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                Str.append(crs.getString("preownedfollowuptime"));
                Str.append(" => ");
                Str.append(crs.getString("preownedfollowuptype_name")).append(": ");
                Str.append(crs.getString("preownedfollowup_desc")).append("<br>");
            }
            crs.close();
            return Str.toString();
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            return "";
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
