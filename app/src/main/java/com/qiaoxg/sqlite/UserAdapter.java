package com.qiaoxg.sqlite;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qiaoxg.sqlite.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH> {

    private List<UserBean> userBeanList;
    private Context mContext;

    public UserAdapter(Context context) {
        this.mContext = context;
        this.userBeanList = new ArrayList<>();
    }

    public void setUserBeanList(List<UserBean> list) {
        userBeanList.clear();

        userBeanList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new UserVH(LayoutInflater.from(mContext).inflate(R.layout.item_user, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH userVH, int i) {
        UserBean bean = userBeanList.get(i);

        userVH.nameTv.setText(bean.getName());
        userVH.phoneTv.setText(bean.getPhone());
    }

    @Override
    public int getItemCount() {
        return userBeanList.size();
    }

    public static class UserVH extends RecyclerView.ViewHolder {

        private TextView nameTv, phoneTv;

        public UserVH(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_name);
            phoneTv = itemView.findViewById(R.id.item_phone);
        }
    }
}
