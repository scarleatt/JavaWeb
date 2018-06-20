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
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "RankList", value = "/rankList")
public class RankList extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String now_stuid = req.getParameter("stuid");
        int js_rank=-1,cg_rank=-1,js_myScore=0,cg_myScore=0;
        int status = 100;
        JSONObject data=new JSONObject(),js=new JSONObject(),
                cg=new JSONObject();
        HashMap<String,String> js_t=new HashMap<>(),
                cg_t=new HashMap<>();
        List<HashMap<String,String>> js_data=new LinkedList<>(),
                cg_data=new LinkedList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            try (Connection conn = JdbcUtil.getConnection()) {
                String sql = "SELECT stuid,tScore FROM funfest order by tScore DESC";

                Statement statement = conn.createStatement();
                ResultSet result = statement.executeQuery(sql);

                int count = 1;
                String stuid; int score;
                System.out.println();
                System.out.println(sql);
                System.out.println("cg");
                try {
                    while (result.next()) {
                        stuid = result.getString("stuid");
                        score = result.getInt("tScore");
                        if (stuid.equals(now_stuid)) {
                            js_rank = count;
                            js_myScore = score;
                            System.out.println("js_rank="+js_rank+"\n"+"js_my_Score="+js_myScore);
                        }
                        js_t.put("stuid", stuid);
                        js_t.put("tScore", String.valueOf(score));
                        js_data.add(js_t);
                        js_t = new HashMap<>();
                        count++;
                    }
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                if (js_rank<0) js_rank=js_t.size()+1;
                js.put("length",count-1);
                js.put("rank", js_rank);
                js.put("data", js_data);
                js.put("myScore", js_myScore);

                System.out.println("js");
                sql = "SELECT stuid,pScore FROM funfest order by pScore DESC";
                statement = conn.createStatement();
                result = statement.executeQuery(sql);
                count = 1;
                try {
                    while (result.next()) {
                        stuid = result.getString("stuid");
                        score = result.getInt("pScore");
                        if (stuid.equals(now_stuid)) {
                            cg_rank = count;
                            cg_myScore = score;
                            System.out.println("cg_rank="+cg_rank+"\n"+"cg_my_Score="+cg_myScore);
                        }
                        cg_t.put("stuid", stuid);
                        cg_t.put("pScore", String.valueOf(score));
                        cg_data.add(cg_t);
                        cg_t = new HashMap<>();
                        count++;
                    }
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
                if (cg_rank<0)
                    cg_rank=cg_t.size()+1;
                cg.put("length", count-1);
                cg.put("data", cg_data);
                cg.put("rank", cg_rank);
                cg.put("myScore", cg_myScore);
                data.put("js",js);
                data.put("cg", cg);
                if (count>0) {
                    status = 200;
                } else {
                    status = 300;
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
        data.put("cg", cg);
        data.put("js", js);
        json.put("data", data);
        out.print(json.toString());
        out.flush();
    }
}
