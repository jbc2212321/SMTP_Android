package com.example.smtp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private JSONArray jsonArray = new JSONArray();

    public void setJsonArray(JSONArray jsonArray){
        this.jsonArray=jsonArray;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater
                .inflate(R.layout.ceil, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        System.out.println("适配器长度:"+jsonArray.size());
//        System.out.println("position："+position);
        JSONObject jsonObject = jsonArray.getJSONObject(position);
        holder.textView.setText(String.valueOf(jsonObject.getInteger("email_no")));
        holder.button.setText(jsonObject.getString("email_subject"));
//        System.out.println("MyAdapter!");

    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
           textView=itemView.findViewById(R.id.textView_email_no);
            button=itemView.findViewById(R.id.button_email_look);
        }
    }

}
