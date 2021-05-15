package com.example.smtp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smtp.databinding.FragmentEmailBinding;
import com.example.smtp.databinding.FragmentPersonalBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmailFragment extends Fragment {
    private FragmentEmailBinding binding;

    public EmailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("测试:"+getArguments().getString("username"));
        binding = FragmentEmailBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
