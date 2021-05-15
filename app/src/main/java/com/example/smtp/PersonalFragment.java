package com.example.smtp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.smtp.databinding.FragmentPersonalBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends Fragment {
    private FragmentPersonalBinding binding;
    public String username;

    public PersonalFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        username = getArguments().getString("username");


        binding = FragmentPersonalBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.personalUser.setText(username);
        binding.personalSmtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_personalFragment_to_emailFragment,bundle);
            }
        });
        binding.personalPop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_personalFragment_to_reciveFragment,bundle);
            }
        });
    }
}
