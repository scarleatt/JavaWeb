package org.redrock.web.servlet;

import org.json.JSONObject;
import org.redrock.web.util.JdbcUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "End", value = "/end")
public class End extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String stuid=req.getParameter("stuid");
        String score=req.getParameter("score");
        String mode=req.getParameter("style");
        Integer status=100,rank=-1,maxScore=-1,nowScore=Integer.parseInt(score);
        String description="";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn=JdbcUtil.getConnection()) {
                String sql;
                PreparedStatement prepareStatement;
                Statement statement;
                Integer rowsUpdated,savedScore;
                ResultSet result;
                if (mode.equals("js")) {
                    System.out.println("time mode user "+stuid);
                    sql="SELECT * from funfest where stuid="+"\""+stuid+"\"";
                    System.out.println(sql);
                    statement = conn.createStatement();
                    result = statement.executeQuery(sql);
                    if (result.next()) {
                        savedScore=result.getInt("tScore");
                        if (savedScore>nowScore) maxScore=savedScore;
                        else maxScore=nowScore;
                        System.out.println("Time mode maxScore="+maxScore);
                    }

                    if (maxScore.equals(nowScore)) {
                        System.out.println("Time mode score update");
                        sql = "UPDATE funfest SET tScore=? WHERE stuid=?";

                        prepareStatement = conn.prepareStatement(sql);
                        prepareStatement.setString(1, score);
                        prepareStatement.setString(2, stuid);
                        System.out.println(sql);

                        rowsUpdated = prepareStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            description="Update time score successfully!";
                            status=200;
                        } else {
                            status=301;
                            description="Update time score rowsUpdated<0";
                        }
                    } else {
                        description="No need to update time score";
                    }

                    sql="SELECT stuid,tScore FROM funfest order by tScore DESC";
                    System.out.println(sql);
                    result = statement.executeQuery(sql);
                    Integer count=0;
                    try {
                        while (result.next()) {
                            count++;
                            if (result.getString("stuid").equals(stuid)) {
                                rank = count;
                                break;
                            }
                        }
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
                } else {
                    System.out.println("pass mode user "+stuid);
                    sql="SELECT * from funfest where stuid="+"\""+stuid+"\"";
                    System.out.println(sql);
                    statement = conn.createStatement();
                    result = statement.executeQuery(sql);
                    if (result.next()) {
                        savedScore=result.getInt("pScore");
                        if (savedScore>nowScore) maxScore=savedScore;
                        else maxScore=nowScore;
                        System.out.println("Pass mode maxScore="+maxScore);
                    }

                    if (maxScore.equals(nowScore)) {
                        System.out.println("Pass mode score update");
                        sql = "UPDATE funfest SET pScore=? WHERE stuid=?";

                        prepareStatement = conn.prepareStatement(sql);
                        prepareStatement.setString(1, score);
                        prepareStatement.setString(2, stuid);
                        System.out.println(sql);

                        rowsUpdated = prepareStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            description="Update pass score successfully!";
                            status=200;
                        } else {
                            status=301;
                            description="Update pass score rowsUpdated<0";
                        }
                    } else {
                        description="No need to update pass score";
                    }

                    sql="SELECT stuid,pScore FROM funfest order by pScore DESC";
                    System.out.println(sql);
                    result = statement.executeQuery(sql);
                    Integer count=0;
                    try {
                        while (result.next()) {
                            count++;
                            if (result.getString("stuid").equals(stuid)) {
                                rank = count;
                                break;
                            }
                        }
                    } catch (NullPointerException ne) {
                        ne.printStackTrace();
                    }
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
        JSONObject result=new JSONObject(),data=new JSONObject();
        data.put("rank", rank);
        data.put("score", maxScore);
        result.put("status",status);
        result.put("data",data);
        result.put("description", description);
        pw.print(result.toString());
        pw.flush();
    }
}