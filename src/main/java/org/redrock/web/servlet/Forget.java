package org.redrock.web.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import org.json.*;
import org.redrock.web.util.JdbcUtil;

import java.security.*;

@WebServlet(name = "Forget", value = "/forget")
public class Forget extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String stuid=req.getParameter("stuid");
        String password=req.getParameter("password");
        int status=100;
        String data="";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn=JdbcUtil.getConnection()) {
                String sql = "UPDATE funfest SET password=? WHERE stuid=?";

                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, password);
                statement.setString(2, stuid);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    status=200;
                    data="An existing user was updated successfully!";
                } else {
                    status=301;
                    data="Update password rowsUpdated<0";
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
        JSONObject json=new JSONObject();
        json.put("status", status);
        json.put("data", data);
        out.print(json.toString());
        out.flush();
    }
}
