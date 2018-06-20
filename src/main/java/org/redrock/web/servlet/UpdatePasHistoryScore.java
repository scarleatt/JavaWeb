package org.redrock.web.servlet;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import org.redrock.web.util.JdbcUtil;

@WebServlet(name = "UpdatePasHistoryScore", value = "/updatePasHistoryScore")
public class UpdatePasHistoryScore extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String stuid=req.getParameter("stuid");
        Integer status=100,score=-1;
        String description="";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn=JdbcUtil.getConnection()) {
                String sql = "select pScore from funfest where stuid="+"\""+stuid+"\"";

                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);
                
                if (result.next()) {
                    score=result.getInt("pScore");
                    status=200;
                    description="update time history score successfully!";
                } else {
                    status=305;
                    description="Please register first!";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        res.setContentType("application/json");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        res.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST,DELETE,PUT");
        res.setHeader("Access-Control-Max-Age", "1800");
        PrintWriter out = res.getWriter();
        JSONObject json=new JSONObject(), retScore=new JSONObject();
        retScore.put("score", score);
        json.put("status", status);
        json.put("data", retScore);
        json.put("description", description);
        out.print(json.toString());
        out.flush();
    }
}
