package com.flowerworld.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.Friend;
import com.flowerworld.app.tool.util.MemberLevelUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class FriendAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater = null;

    private List<Friend> dataList = null;

    protected DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_empty)
            .showImageForEmptyUri(R.drawable.image_empty).showImageOnFail(R.drawable.image_loadfailed)
            .showStubImage(R.drawable.image_empty).resetViewBeforeLoading().cacheInMemory().cacheOnDisc()
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_4444)
            .displayer(new RoundedBitmapDisplayer(5)).build();

    public FriendAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Friend> data) {
        this.dataList = data;
    }

    public List<Friend> getData() {
        return this.dataList;
    }

    public void appendData(List<Friend> append) {
        this.dataList.addAll(append);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        String s = dataList.get(position).getID();
        return Long.parseLong(s);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.friend_list_item, null);

            holder = new Holder();
            holder.header = (ImageView) convertView.findViewById(R.id.friend_list_header);
            holder.name = (TextView) convertView.findViewById(R.id.friend_list_text_name);
            holder.company = (TextView) convertView.findViewById(R.id.friend_list_text_company);
            holder.memberLevel = (ImageView) convertView.findViewById(R.id.friend_list_image_member_type);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Friend friend = (Friend) getItem(position);
        ImageLoader.getInstance().displayImage(friend.getHeader(), holder.header, options);
        holder.name.setText(friend.getUserName());
        holder.company.setText(friend.getCompany());
        holder.memberLevel.setImageResource(MemberLevelUtility.switchMemberNum(friend.getCompanyLevel()));

        return convertView;
    }

    private class Holder {
        ImageView header;
        TextView name;
        TextView company;
        ImageView memberLevel;
    }

}
