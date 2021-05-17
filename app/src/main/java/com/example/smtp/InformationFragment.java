package com.example.smtp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smtp.databinding.FragmentInformationBinding;
import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class InformationFragment extends Fragment {
    private FragmentInformationBinding binding;
    private String username;

    public InformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        username = getArguments().getString("username");
        binding = FragmentInformationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.editTextInformationUsername.setText(username);
        binding.editTextInformationUsername.setEnabled(false);
        binding.editTextInformationUsername.setGravity(Gravity.CENTER);
        binding.buttonInformationSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] answer = new String[1];
                final String password = String.valueOf(binding.editTextInformationPassword.getText());
                final String check = String.valueOf(binding.editTextInformationCheck.getText());
                if (password.length() == 0 || check.length() == 0) {
                    Toast.makeText(getActivity(), "密码不能为空!", Toast.LENGTH_SHORT).show();

                }
                else {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> paramsMap = new HashMap<>();
                            paramsMap.put("username", username);
                            paramsMap.put("password", password);
                            paramsMap.put("check", check);
                            try {
                                answer[0] = HttpUtil.doPost("modifyPassword", paramsMap);
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
                    if (answer[0].equals("密码错误")){
                        Toast.makeText(getActivity(), "密码错误!!", Toast.LENGTH_SHORT).show();
                    }else if (answer[0].equals("修改成功")){
                        Toast.makeText(getActivity(), "修改成功!!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
