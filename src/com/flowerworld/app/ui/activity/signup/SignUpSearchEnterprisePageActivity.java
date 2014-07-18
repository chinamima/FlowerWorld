package com.flowerworld.app.ui.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.bean.GlobalConstant;
import com.flowerworld.app.dao.bean.GlobalVariableBean;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.ui.base.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class SignUpSearchEnterprisePageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_search_enterprise);

        initView();
    }

    private void initView() {
        initBanner(R.id.banner_layout, R.drawable.banner, R.string.signup_searchenterprise_title, 0, R.string.go_back,
                R.drawable.button_corner_rectangle_selector, R.string.signup_searchenterprise_banner_right,
                R.drawable.button_corner_rectangle_selector, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                    }
                });

//		initBottomBtn();
//		initBottomBtnSelected();
        hideView();
        initButton();
    }

    private void hideView() {
        findViewById(R.id.sign_up_search_enterprise_result).setVisibility(View.GONE);
        findViewById(R.id.sign_up_search_enterprise_list).setVisibility(View.GONE);
        findViewById(R.id.sign_up_search_enterprise_tips).setVisibility(View.GONE);
        findViewById(R.id.sign_up_search_enterprise_new_button).setVisibility(View.GONE);
    }

//	String listJson = "[{\"field7\":\"33257\",\"company\":\"陈村花木\",\"field1\":\"111\",\"field2\":\"嘿松，罗汉松\",\"field6\":\"\",\"telephone\":\"13902564818\",\"field4\":\"园林绿化施工\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"33258\",\"company\":\"山东珍雅轩水培花卉资材厂\",\"field1\":\"山东珍雅轩\",\"field2\":\"水培花卉 电子花瓶 玻璃花瓶 增氧花瓶\",\"field6\":\"\",\"telephone\":\"0539-2802102\",\"field4\":\"生产资材\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"16\",\"company\":\"湖北宏业园林资材公司\",\"field1\":\"湖北宏业园林资材公司\",\"field2\":\" \",\"field6\":\"0717-6346826\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"17\",\"company\":\"暖德花场\",\"field1\":\"暖德花场\",\"field2\":\"茶花 苏铁 茶花\",\"field6\":\" \",\"telephone\":\"13923248393\",\"field4\":\"花卉苗木生产商\",\"field9\":\"切花切叶\",\"field10\":\"\"},{\"field7\":\"18\",\"company\":\"绿之源水培花卉工作室\",\"field1\":\"绿之源水培花卉工作室\",\"field2\":\"水培花卉\",\"field6\":\"7777777777777\",\"telephone\":\"76585444\",\"field4\":\"切花切叶\",\"field9\":\"生产资材\",\"field10\":\"\"},{\"field7\":\"21\",\"company\":\"浙江省丽水市农科所农业智能化快繁中心\",\"field1\":\"浙江省丽水市农科所农业智能化快繁中心\",\"field2\":\" \",\"field6\":\" \",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"23\",\"company\":\"金茶花园艺场\",\"field1\":\"金茶花园艺场\",\"field2\":\" \",\"field6\":\"0766-2622884\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"24\",\"company\":\"圣莱特农庄\",\"field1\":\"圣莱特农庄\",\"field2\":\" \",\"field6\":\"0573-2118347\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"26\",\"company\":\"苗木基地供销社园艺场\",\"field1\":\"苗木基地供销社园艺场\",\"field2\":\" \",\"field6\":\"0527-3391156\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"27\",\"company\":\"河南省封丘县金银花种植推广中心\",\"field1\":\"河南省封丘县金银花种植推广中心\",\"field2\":\" \",\"field6\":\"86-373-8298148\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"29\",\"company\":\"中山市良种苗木示范场\",\"field1\":\"中山市良种苗木示范场\",\"field2\":\" \",\"field6\":\"0760-2120679\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"30\",\"company\":\"河南省鄢陵县花卉苗木发展中心\",\"field1\":\"河南省鄢陵县花卉苗木发展中心\",\"field2\":\" \",\"field6\":\"0138-37104104\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"36\",\"company\":\"广州市菲朗园艺\",\"field1\":\"广州市菲朗园艺\",\"field2\":\"红掌 粉掌 阿拉巴马 火焰 大哥大 亚历桑娜 粉冠军 一串红 三色堇 矮牵牛 草花\",\"field6\":\"02083799308\",\"telephone\":\" \",\"field4\":\"切花切叶\",\"field9\":\"切花切叶\",\"field10\":\"\"},{\"field7\":\"38\",\"company\":\"青州市利民温控设备\",\"field1\":\"青州市利民温控设备\",\"field2\":\"青州 温控\",\"field6\":\"0536-3861318\",\"telephone\":\" \",\"field4\":\"生产资材\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"39\",\"company\":\"河北省林业科学研究院\",\"field1\":\"河北省林业科学研究院\",\"field2\":\" \",\"field6\":\"0311-86276818\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"41\",\"company\":\"蚌埠市馥源园林花卉苗圃\",\"field1\":\"蚌埠市馥源园林花卉苗圃\",\"field2\":\"花卉苗木销售、花木租赁、园林绿化\",\"field6\":\"0552-3140028\",\"telephone\":\" \",\"field4\":\"花店资材\",\"field9\":\"切花切叶\",\"field10\":\"\"},{\"field7\":\"42\",\"company\":\"小榄镇联丰园艺场\",\"field1\":\"小榄镇联丰园艺场\",\"field2\":\" \",\"field6\":\"0760-2128899\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"43\",\"company\":\"青岛磊伟热能设备厂\",\"field1\":\"青岛磊伟热能设备厂\",\"field2\":\" \",\"field6\":\"0532-82420525\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"44\",\"company\":\"广东炬绿园林公司\",\"field1\":\"广东炬绿园林公司\",\"field2\":\"琴丝竹 雷竹 红竹 绿竹 紫竹 麻竹 毛竹 唐竹 金竹 小青竹 青皮竹 桫椤 美洲龙棕竹 桂花 南天竹 竹柏 杜英 鱼尾葵 盆架子\",\"field6\":\"020-62824069\",\"telephone\":\" 020-33010357\",\"field4\":\"切花切叶\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"46\",\"company\":\"山东宏场银杏苗木开 发基地\",\"field1\":\"山东宏场银杏苗木开 发基地\",\"field2\":\" \",\"field6\":\"86-539-6651087\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"47\",\"company\":\"江苏信苑绿化工程公司\",\"field1\":\"江苏信苑绿化工程公司\",\"field2\":\"绿化工程\",\"field6\":\"86-527-3390077\",\"telephone\":\" \",\"field4\":\"园林绿化施工\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"48\",\"company\":\"宝增银杏苗木培育场\",\"field1\":\"宝增银杏苗木培育场\",\"field2\":\" \",\"field6\":\"86-539-6651204\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"49\",\"company\":\"四川省勇昌园林景观绿化工程有限责任公司\",\"field1\":\"四川勇昌\",\"field2\":\"景观工程\",\"field6\":\"028-87863186\",\"telephone\":\" \",\"field4\":\"花店资材\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"50\",\"company\":\"广州市梦桃源风景园林有限公司\",\"field1\":\"广州市梦桃源风景园林有限公司\",\"field2\":\"广州  风景园林 \",\"field6\":\"020-37410932\",\"telephone\":\" \",\"field4\":\"花店资材\",\"field9\":\"爱花之人\",\"field10\":\"\"},{\"field7\":\"51\",\"company\":\"颜集镇堰下苗圃场\",\"field1\":\"颜集镇堰下苗圃场\",\"field2\":\" \",\"field6\":\"05273396913\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"52\",\"company\":\"湖南花木－飞鹰苗圃\",\"field1\":\"湖南花木－飞鹰苗圃\",\"field2\":\" \",\"field6\":\"0731-3125928\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"53\",\"company\":\"侨乡第一盆景园\",\"field1\":\"侨乡第一盆景园\",\"field2\":\" \",\"field6\":\"0750-5562221\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"55\",\"company\":\"沭阳鸿大园林苗圃\",\"field1\":\"沭阳鸿大园林苗圃\",\"field2\":\" \",\"field6\":\"0527-3391603\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"57\",\"company\":\"河北荣盛绿化工程有限公司\",\"field1\":\"河北荣盛绿化工程有限公司\",\"field2\":\"绿化工程\",\"field6\":\"0312-2675555转8808\",\"telephone\":\" \",\"field4\":\"园林绿化施工\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"59\",\"company\":\"杰达园艺场\",\"field1\":\"杰达园艺场\",\"field2\":\" \",\"field6\":\"0757-38303333\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"60\",\"company\":\"四通镇绿色楸树基地\",\"field1\":\"四通镇绿色楸树基地\",\"field2\":\" \",\"field6\":\"0394-2731181\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"61\",\"company\":\"恒达苗木花卉\",\"field1\":\"恒达苗木花卉\",\"field2\":\" \",\"field6\":\"0312-2551101\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"63\",\"company\":\"山东宏场银杏 苗木开发基地\",\"field1\":\"山东宏场银杏 苗木开发基地\",\"field2\":\" \",\"field6\":\"86-0539-6651087\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"66\",\"company\":\"美林景观 园艺\",\"field1\":\"美林景观 园艺\",\"field2\":\" \",\"field6\":\"86-731-2340020\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"67\",\"company\":\"上海新东方 银杏园林绿化工程公司\",\"field1\":\"上海新东方 银杏园林绿化工程公司\",\"field2\":\" \",\"field6\":\"86-539-6651777\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"68\",\"company\":\"江苏沭阳新槐苗圃\",\"field1\":\"江苏沭阳新槐苗圃\",\"field2\":\" \",\"field6\":\"86-0527-3390253\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"69\",\"company\":\"江苏省沭阳县超达花木场\",\"field1\":\"江苏省沭阳县超达花木场\",\"field2\":\" \",\"field6\":\"86-527-3390493\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"70\",\"company\":\"山东郯城创林银杏苗木培育基地\",\"field1\":\"山东郯城创林银杏苗木培育基地\",\"field2\":\" \",\"field6\":\"86-539-6651683\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"72\",\"company\":\"广东省中山市金华花木场\",\"field1\":\"金华花木场\",\"field2\":\"小叶榕 橡胶榕 红车 红珊瑚\",\"field6\":\"0760-87666218\",\"telephone\":\"13702788713\",\"field4\":\"切花切叶\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"77\",\"company\":\"盛兴花卉\",\"field1\":\"盛兴花卉\",\"field2\":\" \",\"field6\":\"0596-6621569\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"78\",\"company\":\"中国兰花植料网\",\"field1\":\"中国兰花植料网\",\"field2\":\" \",\"field6\":\"0854-4940073\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"79\",\"company\":\"绿源园林有限公司\",\"field1\":\"绿源园林有限公司\",\"field2\":\" \",\"field6\":\"0796-3566588\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"83\",\"company\":\"大汉园艺\",\"field1\":\"大汉园艺\",\"field2\":\"农膜 肥料 农药 巴西铁 荷兰铁 景观苏铁 凤梨\",\"field6\":\"020-81620790\",\"telephone\":\" \",\"field4\":\"切花切叶\",\"field9\":\"生产资材\",\"field10\":\"生产资材\"},{\"field7\":\"84\",\"company\":\"广东顺德奇美兰花种苗\",\"field1\":\"奇美兰花种苗\",\"field2\":\"蝴蝶兰 嘉德利亚兰 石斛兰 文心兰\",\"field6\":\"0757-23336347\",\"telephone\":\" \",\"field4\":\"切花切叶\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"86\",\"company\":\"河北新星林业科技有限公司\",\"field1\":\"河北新星林业科技有限公司\",\"field2\":\" \",\"field6\":\"0311-86276818\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"88\",\"company\":\"广东省惠州市新欣园艺\",\"field1\":\"新欣园艺\",\"field2\":\"穗花棋盘脚 红继木\",\"field6\":\"\",\"telephone\":\"\",\"field4\":\"切花切叶\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"90\",\"company\":\"广州芳捷园艺\",\"field1\":\"广州芳捷园艺\",\"field2\":\" \",\"field6\":\"020-81413598\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"91\",\"company\":\"广西**双旺镇天宝苗木场\",\"field1\":\"广西**双旺镇天宝苗木场\",\"field2\":\" \",\"field6\":\"0775-8582482\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"92\",\"company\":\"山东省菏泽市长青苗木花卉基地\",\"field1\":\"山东省菏泽市长青苗木花卉基地\",\"field2\":\" \",\"field6\":\"0530-5317395\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"},{\"field7\":\"94\",\"company\":\"美怡园艺有限公司\",\"field1\":\"美怡园艺有限公司\",\"field2\":\" \",\"field6\":\"020-88888888\",\"telephone\":\" \",\"field4\":\"\",\"field9\":\"\",\"field10\":\"\"}]";

    private void initList() {
        requestHttp(new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_SIGN_EXIST_ENTERPRISE;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                initListData(resultStr);
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                initListData(resultStr);
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("nameKey", ((EditText) findViewById(R.id.sign_up_search_enterprise_key_word)).getText().toString());
            }
        });
    }

    private void initListData(String resultStr) {
        JsonObject jsonObj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
        final JsonArray arr = jsonObj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonArray();

        Type listType = new TypeToken<List<Map<String, String>>>() {
        }.getType();

        List<Map<String, String>> listMap = new Gson().fromJson(arr, listType);
        String[] keys = new String[] { "company", "field10" };
        SimpleAdapter adapter = new SimpleAdapter(SignUpSearchEnterprisePageActivity.this, listMap,
                R.layout.sign_up_search_enterprise_list_item, keys, new int[] { R.id.list_item_left, R.id.list_item_right });
        ListView listView = (ListView) findViewById(R.id.sign_up_search_enterprise_list);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JsonObject obj = arr.get(position).getAsJsonObject();
                Intent intent = new Intent(SignUpSearchEnterprisePageActivity.this, SignUpStaffPageActivity.class);
                intent.putExtra(SignUpStaffPageActivity.INTENT_KEY_COMPANY_FULL_NAME,
                        GsonJsonUtil.optString(obj.get(SignUpStaffPageActivity.INTENT_KEY_COMPANY_FULL_NAME), ""));
                intent.putExtra(SignUpStaffPageActivity.INTENT_KEY_COMPANY_SHORT_NAME,
                        GsonJsonUtil.optString(obj.get(SignUpStaffPageActivity.INTENT_KEY_COMPANY_SHORT_NAME), ""));
                startActivity(intent);
            }
        });
    }

    private void initButton() {
        findViewById(R.id.sign_up_search_enterprise_new_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpSearchEnterprisePageActivity.this, SignUpNewEnterprisePageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sign_up_search_enterprise_search_button).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initList();
                findViewById(R.id.sign_up_search_enterprise_new_button).setVisibility(View.VISIBLE);
            }
        });

    }

}
