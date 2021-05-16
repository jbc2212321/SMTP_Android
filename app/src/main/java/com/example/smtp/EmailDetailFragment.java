package com.example.smtp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONObject;
import com.example.smtp.databinding.FragmentEmailDetailBinding;
import com.example.smtp.emil.popCommand;
import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailDetailFragment extends Fragment {
    private FragmentEmailDetailBinding binding;
    String number;

    public EmailDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        number = getArguments().getString("number");
        binding = FragmentEmailDetailBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public synchronized void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String[] answer = new String[1];
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> paramsMap = new HashMap<>();
                paramsMap.put("number", number);
                try {
                    answer[0] = HttpUtil.doPost("getEmailDetail", paramsMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> paramsMap = new HashMap<>();
                try {
                    HttpUtil.doPost("getPOP3", paramsMap);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t2.start();

        popCommand command=new popCommand();
        String content="";
        try {
            content=command.getContent(Integer.parseInt(number));
        } catch (IOException e) {
            e.printStackTrace();
        }






        System.out.println("answer[0]:"+answer[0]);
        JSONObject jsonObject = JSONObject.parseObject(answer[0]);
        binding.editTextEmailReciver.setText(jsonObject.getString("email_to"));
        binding.editTextEmailSender.setText(jsonObject.getString("email_from")); //发件人
        binding.editTextEmailTheme.setText(jsonObject.getString("email_subject")); //主题
        binding.editTextEmailContent.setText(content); //内容
        binding.editTextEmailReciver.setEnabled(false);
        binding.editTextEmailSender.setEnabled(false);
        binding.editTextEmailTheme.setEnabled(false);
        binding.editTextEmailContent.setEnabled(false);
    }
}
