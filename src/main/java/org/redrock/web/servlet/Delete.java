package org.redrock.web.servlet;
import org.json.JSONObject;
import org.redrock.web.util.JdbcUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "Delete", value = "/delete")
public class Delete extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String stuid = req.getParameter("stuid");
        String password = req.getParameter("password");
        int status = 100;
        String data = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn = JdbcUtil.getConnection()) {
                String sql = "DELETE FROM funfest WHERE stuid=?";

                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, stuid);

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    status=200;
                    data="A user was deleted successfully!";
                    System.out.println("A user was deleted successfully!");
                } else {
                    status=300;
                    data="Error when delete user!";
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
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("data", data);
        out.print(json.toString());
        out.flush();
    }
}
