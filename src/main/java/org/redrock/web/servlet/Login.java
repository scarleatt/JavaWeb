package org.redrock.web.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;
import javax.xml.bind.PropertyException;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import org.json.*;
import org.redrock.web.util.JdbcUtil;

import java.security.*;

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String stuid=req.getParameter("stuid");
        String password=req.getParameter("password");
        int status=100;
        String data="";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn=JdbcUtil.getConnection()) {
                String sql = "select stuid,password from funfest where stuid="+"\""+stuid+"\"";

                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);

                if (result.next()) {
                    String sql_password=result.getString("password");
                    if (sql_password.equals(password)) {
                        Cookie cookie=new Cookie("stuid", stuid);
                        cookie.setMaxAge(30*60);
                        res.addCookie(cookie);
                        System.out.println("Cookie stuid="+stuid);
                        status=200;
                        data="Successful !";
                    } else {
                        status=300;
                        data="Please input correct password...";
                    }
                } else {
                    status=305;
                    data="Please register first!";
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
