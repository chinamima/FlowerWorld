package com.flowerworld.app.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.flowerworld.app.R;
import com.flowerworld.app.tool.util.ApplicationContextUtil;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.MemberLevelUtility;
import com.flowerworld.app.ui.widget.ProductPpAttrLinear;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ProductAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater inflater = null;
    private JsonArray dataArr = null;

    private static final String KEY_PRODUCT_NAME = "productName";
    private static final String KEY_IMAGES = "images";
    private static final String KEY_PP_ATTR = "ppAttr";
    private static final String KEY_PRICE = "price";
    private static final String KEY_PRICE2 = "price2";
    private static final String KEY_SIMPLE_NAME = "simpleName";
    private static final String KEY_MEMBER_LEVEL_NAME = "memberLevelName";
    private static final String KEY_MEMBER_LEVEL = "memberLevel";
    private static final String KEY_PP_ATTR_NAME = "name";
    private static final String KEY_PP_ATTR_V1 = "v1";
    private static final String KEY_PP_ATTR_V2 = "v2";
    private static final String KEY_PP_ATTR_PP = "pp";
    private static final String KEY_PP_ATTR_DW = "dw";

    protected DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.image_empty)
            .showImageForEmptyUri(R.drawable.image_empty).showImageOnFail(R.drawable.image_loadfailed)
            .showStubImage(R.drawable.image_empty).resetViewBeforeLoading().cacheInMemory().cacheOnDisc()
            .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.ARGB_4444)
            .displayer(new RoundedBitmapDisplayer(5)).build();

    public ProductAdapter(Context context) {
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
        String s = dataArr.get(position).getAsJsonObject().get("id").getAsString();
        return Long.parseLong(s);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.product_list_item, null);

            holder = new Holder();
            holder.pic = (ImageView) convertView.findViewById(R.id.product_list_item_pic);
            holder.productName = (TextView) convertView.findViewById(R.id.product_list_item_product_name);
            holder.productPrice = (TextView) convertView.findViewById(R.id.product_list_item_product_price);
            holder.company = (TextView) convertView.findViewById(R.id.product_list_item_company);
            holder.memberType = (ImageView) convertView.findViewById(R.id.product_list_item_member_type);
            holder.propertyLayout = (LinearLayout) convertView.findViewById(R.id.product_list_item_property_layout);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        JsonObject obj = (JsonObject) getItem(position);
        holder.productName.setText(GsonJsonUtil.optString(obj.get(KEY_PRODUCT_NAME), ""));
//		AsyncImageLoader.setImageAsync(holder.pic, GsonJsonUtil.optString(obj.get(KEY_IMAGES), ""), false, null);
        ImageLoader.getInstance().displayImage(GsonJsonUtil.optString(obj.get(KEY_IMAGES), ""), holder.pic, options);
        String price = GsonJsonUtil.optString(obj.get(KEY_PRICE), "0");
        if ("0".equals(price)) {
            price = ApplicationContextUtil.getApplicationContext().getString(R.string.product_list_item_price_talk);
        } else {
            price = "￥" + price;
        }
        holder.productPrice.setText(price);
        holder.company.setText(GsonJsonUtil.optString(obj.get(KEY_SIMPLE_NAME), ""));
        holder.memberType.setImageResource(MemberLevelUtility.switchMemberName(GsonJsonUtil.optString(obj.get(KEY_MEMBER_LEVEL_NAME),
                "")));
        ProductPpAttrLinear.setPpAttr(context, holder.propertyLayout, obj.get(KEY_PP_ATTR).getAsJsonArray(), KEY_PP_ATTR_NAME,
                KEY_PP_ATTR_V1, KEY_PP_ATTR_V2, KEY_PP_ATTR_DW);

        return convertView;
    }

    private class Holder {
        ImageView pic;
        TextView productName;
        TextView productPrice;
        TextView company;
        ImageView memberType;
        LinearLayout propertyLayout;
    }

}
