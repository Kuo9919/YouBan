package com.example.poemzj.msg;

import android.content.Context;

import com.example.poemzj.msg.MessageBean;
import com.example.poemzj.R;

import java.util.ArrayList;

public class MessageUtils {
    public static ArrayList<MessageBean> getAllMes(Context context) {
        ArrayList<MessageBean> arrayList = new ArrayList<>();
        
        MessageBean mesBean1 = new MessageBean();
        mesBean1.title = "安全中心";
        mesBean1.content = "欢迎您回到游伴！...";
        mesBean1.icon = context.getResources().getDrawable(R.drawable.icon_safe);
        arrayList.add(mesBean1);
    
        MessageBean mesBean2 = new MessageBean();
        mesBean2.title = "管理员";
        mesBean2.content = "部分功能，攻城狮们在努力开发中，敬请谅解...";
        mesBean2.icon = context.getResources().getDrawable(R.drawable.icon_admin);
        arrayList.add(mesBean2);
    
        MessageBean mesBean3 = new MessageBean();
        mesBean3.title = "Nicy";
        mesBean3.content = "哈哈,请多指教!";
        mesBean3.icon = context.getResources().getDrawable(R.drawable.icon_ex1);
        arrayList.add(mesBean3);
        
        return arrayList;
    }
}