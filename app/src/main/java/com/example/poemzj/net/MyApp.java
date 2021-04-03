package com.example.poemzj.net;

import android.app.Application;

public class MyApp extends Application {
    static String Url="http://169.254.27.217:98/";
    public static String MyUid="1";
    
    public static String getUrl(){
        String MyUrl=Url;
        return MyUrl;
    }
    
}
