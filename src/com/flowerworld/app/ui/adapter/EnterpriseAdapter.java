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
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.MemberLevelUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class EnterpriseAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater = null;
    private JsonArray dataArr = null;

    private String prefixText0 = null;
    private String prefixText1 = null;
    private String prefixText2 = null;

    private static final String KEY_LOGO = "logo";
    private static final String KEY_TEL = "contactTel";
    private static final String KEY_COMPANY = "enterpriseName";
    private static final String KEY_MEMBER_LEVEL_NAME = "memberLevelName";
    private static final String KEY_MEMBER_LEVEL = "memberLevel";
    private static final String KEY_CITY_NAME = "cityName";
    private static final String KEY_PROVINCE_NAME = "provinceName";
    private static final String KEY_KEYWORD = "keyword";
    private static final String KEY_HOME = "home";
    private static final String KEY_ID = "id";

    protected DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_empty)
            .showImageForEmptyUri(R.drawable.image_empty).showImageOnFail(R.drawable.image_loadfailed)
            .showStubImage(R.drawable.image_empty).resetViewBeforeLoading().cacheInMemory().cacheOnDisc()
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_4444)
            .displayer(new RoundedBitmapDisplayer(5)).build();

    public EnterpriseAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        prefixText0 = context.getString(R.string.enterprise_list_keyword);
        prefixText1 = context.getString(R.string.enterprise_list_location);
        prefixText2 = context.getString(R.string.enterprise_list_tel);
    }

    public void setData(JsonArray data) {
        this.dataArr = data;
    }

    public void appendData(JsonArray append) {
        this.dataArr.addAll(append);
    }

    @Override
    public int getCount() {
        return dataArr.size();
    }

    @Override
    public Object getItem(int position) {
        return dataArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.enterprise_list_item, null);

            holder = new Holder();
            holder.pic = (ImageView) convertView.findViewById(R.id.enterprise_list_item_pic);
            holder.membertypelevel = (ImageView) convertView.findViewById(R.id.enterprise_list_item_member);
            holder.enterpriseName = (TextView) convertView.findViewById(R.id.enterprise_list_item_company);
            holder.keyword = (TextView) convertView.findViewById(R.id.enterprise_list_item_keyword);
            holder.location = (TextView) convertView.findViewById(R.id.enterprise_list_item_location);
            holder.tel = (TextView) convertView.findViewById(R.id.enterprise_list_item_tel);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        JsonObject obj = (JsonObject) getItem(position);
        holder.enterpriseName.setText(GsonJsonUtil.optString(obj.get(KEY_COMPANY), ""));
        holder.keyword.setText(prefixText0 + GsonJsonUtil.optString(obj.get(KEY_KEYWORD), ""));
        holder.location.setText(prefixText1 + GsonJsonUtil.optString(obj.get(KEY_PROVINCE_NAME), "") + " "
                + GsonJsonUtil.optString(obj.get(KEY_CITY_NAME), ""));
        holder.tel.setText(prefixText2 + GsonJsonUtil.optString(obj.get(KEY_TEL), ""));
//		AsyncImageLoader.setImageAsync(holder.pic, GsonJsonUtil.optString(obj.get(KEY_LOGO), ""), false, null);
        ImageLoader.getInstance().displayImage(GsonJsonUtil.optString(obj.get(KEY_LOGO), ""), holder.pic, options);
        holder.membertypelevel.setImageResource(MemberLevelUtility.switchMemberName(GsonJsonUtil.optString(
                obj.get(KEY_MEMBER_LEVEL_NAME), "")));

        return convertView;
    }

    private class Holder {
        ImageView pic;
        TextView enterpriseName;
        ImageView membertypelevel;
        TextView keyword;
        TextView location;
        TextView tel;
    }

}
