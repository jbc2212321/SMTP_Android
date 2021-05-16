package com.example.smtp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.smtp.emil.popCommand;
import com.example.smtp.util.HttpUtil;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    public synchronized  void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

//        System.out.println("适配器长度:"+jsonArray.size());
//        System.out.println("position："+position);
        final JSONObject jsonObject = jsonArray.getJSONObject(position);
        holder.textView.setText(String.valueOf(jsonObject.getInteger("email_no")));
        holder.button.setText(jsonObject.getString("email_subject"));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("number",String.valueOf(jsonObject.getInteger("email_no")));
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_reciveFragment_to_emailDetailFragment,bundle);
            }
        });
        final String[] answer = new String[1];
        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setTitle("确认删除?");
                builder.setMessage("");
                // 更新
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //进行更新的方法
                        System.out.println("删除的当前位置:"+position);
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

                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> paramsMap = new HashMap<>();
                                System.out.println("删除位置的编号:"+jsonArray.getJSONObject(position).getInteger("email_no"));
                                popCommand command=new popCommand();
                                try {
                                    command.DELETE(jsonArray.getJSONObject(position).getInteger("email_no"));
                                } catch (IOException e) {
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
                        removeData(position);
                    }
                });
                // 稍后更新
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog noticeDialog = builder.create();
                noticeDialog.show();

                //shanchu

            }
        });

    }
    //  删除数据
    public void removeData(int position) {
        jsonArray.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button,button_delete;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
           textView=itemView.findViewById(R.id.textView_email_no);
            button=itemView.findViewById(R.id.button_email_look);
            button_delete=itemView.findViewById(R.id.button_email_delete);
        }
    }
    public void showNoticeDialog(String data, Context mContext) {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("确认删除?");
        builder.setMessage(data);
        // 更新
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //进行更新的方法
            }
        });
        // 稍后更新
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }
}
