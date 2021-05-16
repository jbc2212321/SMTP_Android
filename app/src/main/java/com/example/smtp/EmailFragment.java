package com.example.smtp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smtp.databinding.FragmentEmailBinding;
import com.example.smtp.databinding.FragmentPersonalBinding;
import com.example.smtp.emil.smtpCommand;
import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment {
    private FragmentEmailBinding binding;
    private String username;

    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        username=getArguments().getString("username");
//        System.out.println("EmailFragment:"+username);
        binding = FragmentEmailBinding.inflate(getLayoutInflater());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        return binding.getRoot();
    }

    @Override
    public  void onActivityCreated(@Nullable Bundle savedInstanceState) {
   //     System.out.println("onClick!!");
        super.onActivityCreated(savedInstanceState);
        binding.editTextEmailSender.setText(username);
        binding.editTextEmailSender.setEnabled(false);
        binding.editTextEmailSender.setGravity(Gravity.CENTER);
        binding.buttonEmailSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public synchronized void onClick(View v) {
                final String reciver= String.valueOf(binding.editTextEmailReciver.getText());  //收件人


                final String theme= String.valueOf(binding.editTextEmailTheme.getText());  //主题
                final String content= String.valueOf(binding.editTextEmailContent.getText());  //内容
            //    if(content.equals("")){content="null";}
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("currentUser", username);
                        try {
                            HttpUtil.doPost("getPSMT", paramsMap);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                System.out.println("commandON");
                smtpCommand command=new smtpCommand();
                System.out.println("getMEssager:\n"+"发送人:"+username+"\n收件人:"+reciver+"\n主题: "+theme+"\n内容: "+content);
                command.send(username,"333",username,reciver,theme,content);
//                Toast toast=Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_SHORT);
//                toast.show();

            }
        });
    }
}
