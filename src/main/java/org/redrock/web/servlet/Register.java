package org.redrock.web.servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import org.json.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.redrock.web.util.JdbcUtil;

@WebServlet(name = "Register", value = "/register")
public class Register extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String stuid=req.getParameter("stuid");
        String password=req.getParameter("password");
        Integer status=200;
        String description="";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn=JdbcUtil.getConnection()) {
                String sql="select * from funfest where stuid="+"\""+stuid+"\"";

                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if (result.next()) {
                    System.out.println(result.getString("stuid"));
                    status = 300;
                    description = "User is existed! Please choose another username os just login it!";
                } else {
                    sql="insert into funfest (stuid, password, pScore, tScore) values (?,?,0,0);";
                    PreparedStatement pstatement = conn.prepareStatement(sql);
                    pstatement.setString(1, stuid);
                    pstatement.setString(2, password);

                    int rowsInserted = pstatement.executeUpdate();
                    if (rowsInserted > 0)
                        description="A new user was inserted successfully!";
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
        PrintWriter pw = res.getWriter();
        JSONObject result=new JSONObject();
        result.put("status",status);
        result.put("data",description);
        pw.print(result.toString());
        pw.flush();
    }
}


