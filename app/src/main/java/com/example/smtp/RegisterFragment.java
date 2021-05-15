package com.example.smtp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smtp.databinding.FragmentEmailBinding;
import com.example.smtp.databinding.FragmentRegisterBinding;
import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
private FragmentRegisterBinding binding;
    public RegisterFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = String.valueOf(binding.editTextUsername.getText());
                final String password = String.valueOf(binding.editTextPassword.getText());
                final String[] answer = new String[1];
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("username", username);
                        paramsMap.put("password", password);
                        try {
                            answer[0] = HttpUtil.doPost("addUser", paramsMap);
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
                if (answer[0].equals("注册成功")){
                    Bundle bundle=new Bundle();
                    bundle.putString("username",username);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_registerFragment_to_personalFragment,bundle);
                }
            }
        });
    }
}
