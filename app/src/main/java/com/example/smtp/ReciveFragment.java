package com.example.smtp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.smtp.databinding.FragmentHomeBinding;
import com.example.smtp.databinding.FragmentReciveBinding;
import com.example.smtp.emil.popCommand;
import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReciveFragment extends Fragment {
    FragmentReciveBinding binding;
    JSONObject jsonObject;
    MyAdapter myAdapter=new MyAdapter();
    popCommand command;
    public ReciveFragment() {
        // Required empty public constructor
    }


    @Override
    public synchronized  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String[] answer = new String[1];

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONArray jsonArray = new JSONArray();
        if(command==null)command=new popCommand();
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, String> paramsMap = new HashMap<>();
//                paramsMap.put("username",  getArguments().getString("username"));
//                try {
//                    answer[0] = HttpUtil.doPost("getCurrentUserEmail", paramsMap);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        t.start();
//        try {
//            t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//        }
//         jsonArray = JSON.parseArray(answer[0]);
//        System.out.println("长度:"+jsonArray.size());
//        for (Object obj : jsonArray) {
//            JSONObject jsonObject = (JSONObject) obj;
//            System.out.println("用户名:"+jsonObject.getString("email_to"));
//        }


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> paramsMap = new HashMap<>();
              //  paramsMap.put("currentUser", username);
                try {
                    HttpUtil.doPost("getPOP", paramsMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();




        System.out.println("commandON");
        JSONArray arr=new JSONArray();
        try {

            assert getArguments() != null;
            arr=command.LIST(getArguments().getString("username"));//此处需获取用户名
        } catch (IOException e) {
            e.printStackTrace();
        }
        myAdapter.setJsonArray(arr);
        System.out.println("new sentence"+arr);
        binding = FragmentReciveBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.recyclerView.setAdapter(myAdapter);

    }
}
