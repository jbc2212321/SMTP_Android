package com.example.smtp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smtp.databinding.FragmentHomeBinding;
import com.example.smtp.util.EventS;
import com.example.smtp.util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment getInstance(String username){
        HomeFragment myFragment = new HomeFragment();
        Bundle bundle=new Bundle();
        bundle.putString("username", username); //key and value
        myFragment.setArguments(bundle);

        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
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
                            answer[0] = HttpUtil.doPost("checkUser", paramsMap);
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

                if (answer[0].equals("登录成功")) {
//                    HomeFragment homeFragment=getInstance(username);
                    Bundle bundle=new Bundle();
                    bundle.putString("username",username);
                    //


                    //

                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_homeFragment_to_personalFragment,bundle);
                }

            }
        });

        binding.buttonHomeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_homeFragment_to_registerFragment);
            }
        });
    }

}
