package org.redrock.web.data;

import com.google.gson.Gson;
import com.mysql.cj.util.StringUtils;
import lombok.Data;
import okhttp3.*;

import java.io.IOException;

@Data
public class User {
    private String username;
    private String password;

    public static void main(String[] args) {

    }

}


