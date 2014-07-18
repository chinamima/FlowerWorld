package com.flowerworld.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SupplyDemandAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater = null;
    private JsonArray dataArr = null;

    private static final String KEY_TYPE = "type";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_CITY = "city";
    private static final String KEY_UPDATE_DATE = "updateDate";
    private static final String KEY_PROVINCE_NAME = "provinceName";
    private static final String KEY_CITY_NAME = "cityName";

    public SupplyDemandAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            convertView = inflater.inflate(R.layout.supply_demand_list_item, null);

            holder = new Holder();
            holder.messageText = (TextView) convertView.findViewById(R.id.supply_demand_list_item_message);
            holder.contact = (TextView) convertView.findViewById(R.id.supply_demand_list_item_contact);
            holder.tel = (TextView) convertView.findViewById(R.id.supply_demand_list_item_tel);
            holder.location = (TextView) convertView.findViewById(R.id.supply_demand_list_item_location);
            holder.time = (TextView) convertView.findViewById(R.id.supply_demand_list_item_time);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        JsonObject obj = (JsonObject) getItem(position);
        holder.messageText.setText(GsonJsonUtil.optString(obj.get(KEY_TITLE), ""));
        holder.contact.setText(GsonJsonUtil.optString(obj.get(KEY_CONTACT), ""));
        holder.tel.setText(GsonJsonUtil.optString(obj.get(KEY_MOBILE), ""));
        holder.location.setText("[" + GsonJsonUtil.optString(obj.get(KEY_PROVINCE_NAME), "") + "·"
                + GsonJsonUtil.optString(obj.get(KEY_CITY_NAME), "") + "]");
        holder.time.setText(GsonJsonUtil.optString(obj.get(KEY_UPDATE_DATE), ""));

        return convertView;
    }

    private class Holder {
        TextView messageText;
        TextView contact;
        TextView tel;
        TextView location;
        TextView time;
    }

}
