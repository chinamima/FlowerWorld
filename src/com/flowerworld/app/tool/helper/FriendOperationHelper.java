package com.flowerworld.app.tool.helper;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.flowerworld.app.R;
import com.flowerworld.app.dao.base.DaoSession;
import com.flowerworld.app.dao.bean.*;
import com.flowerworld.app.dao.beandao.FavoritesDao;
import com.flowerworld.app.dao.beandao.FriendDao;
import com.flowerworld.app.dao.beandao.FriendGroupDao;
import com.flowerworld.app.interf.IActivityRequestThreadManager;
import com.flowerworld.app.interf.IHttpProcess;
import com.flowerworld.app.tool.http.HttpRequestFacade;
import com.flowerworld.app.tool.util.ApplicationContextUtil;
import com.flowerworld.app.tool.util.GsonJsonUtil;
import com.flowerworld.app.tool.util.ToastUtil;
import com.flowerworld.app.tool.util.UnitUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendOperationHelper {

    public static final void addFriendGroup(final Context context, final String groupName, final Runnable successfulAction) {
        if (TextUtils.isEmpty(groupName)) {
            ToastUtil.show(context, "好友分组名称不能为空");
            return;
        }

        HttpRequestFacade.requestHttp((IActivityRequestThreadManager) context, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_GROUP_SAVE;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                int id = GsonJsonUtil.optInt(resultObj.get("id"));

                FriendGroupDao dao = GlobalVariableBean.getDaoSession(context, GlobalVariableBean.userInfo.memberId)
                        .getFriendGroupDao();
                FriendGroup g = new FriendGroup(id + "", (int) dao.count(), groupName);
                dao.insert(g);

                ToastUtil.show(context, "添加成功");
                if (null != successfulAction) {
                    successfulAction.run();
                }

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put(GlobalConstant.sessionId, GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("name", groupName);
            }
        });
    }

    public static void requestFriendData(final IActivityRequestThreadManager iartm, final Runnable succeedAction) {
        HttpRequestFacade.requestHttp(iartm, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_LIST;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
                JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                dealFriendData(resultObj);

                succeedAction.run();

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
            }
        });
    }

    private static void dealFriendData(JsonObject result) {
//		DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "friend.db", null);
//		SQLiteDatabase db = helper.getWritableDatabase();
//		DaoSession daoSession = new DaoMaster(db).newSession();
        DaoSession daoSession = GlobalVariableBean.getDaoSession(ApplicationContextUtil.getApplicationContext(),
                GlobalVariableBean.userInfo.memberId);
        if (result.has("friends")) {
            addFriend(daoSession, result.get("friends").getAsJsonArray());
        }
        if (result.has("group")) {
            addFriendGroup(daoSession, result.get("group").getAsJsonArray());
        }

        if (result.has("favorites")) {
            addFavorites(daoSession, result.get("favorites").getAsJsonArray());
        }

//		List<Friend> list = daoSession.getFriendDao().queryRaw(" where " + FriendDao.Properties.ID.columnName);
//		List<Friend> list = daoSession.getFriendDao().queryBuilder().where(FriendDao.Properties.ID.eq("2")).list();
//		Log.d(TAG, "");
    }

    private static void addFriend(DaoSession daoSession, JsonArray friends) {
        List<Friend> fList = new ArrayList<Friend>();
        Friend f = null;
        JsonObject o = null;
        for (int i = 0; i < friends.size(); i++) {
            o = GsonJsonUtil.optJsonObject(friends.get(i));
            if (null == o) {
                continue;
            }

            f = new Friend();
            f.setID(GsonJsonUtil.optString(o.get("fuid"), ""));
            f.setOrderNum(i);
            f.setUserName(GsonJsonUtil.optString(o.get("fusername"), ""));
            f.setRealName(GsonJsonUtil.optString(o.get("realname"), ""));
            f.setGroupID(GsonJsonUtil.optString(o.get("gid"), ""));
            f.setGroupName(GsonJsonUtil.optString(o.get("groupName"), ""));
            f.setCompany(GsonJsonUtil.optString(o.get("company"), ""));
            f.setCompanyLevel(GsonJsonUtil.optString(o.get("memberLevel"), ""));
            f.setHeader(GsonJsonUtil.optString(o.get("logo"), ""));

            fList.add(f);
        }

        FriendDao dao = daoSession.getFriendDao();
        dao.deleteAll();
        dao.insertInTx(fList);
    }

    private static void addFriendGroup(DaoSession daoSession, JsonArray group) {
        List<FriendGroup> gList = new ArrayList<FriendGroup>();
        FriendGroup g = new FriendGroup("-1", -1, "所有");
        gList.add(g);
        JsonObject o = null;
        for (int i = 0; i < group.size(); i++) {
            o = GsonJsonUtil.optJsonObject(group.get(i));
            if (null == o) {
                continue;
            }

            g = new FriendGroup();
            g.setID(GsonJsonUtil.optString(o.get("id"), ""));
            g.setOrderNum(i);
            g.setName(GsonJsonUtil.optString(o.get("name"), ""));

            gList.add(g);
        }

        FriendGroupDao dao = daoSession.getFriendGroupDao();
        dao.deleteAll();
        dao.insertInTx(gList);
    }

    private static void addFavorites(DaoSession daoSession, JsonArray favorites) {
        List<Favorites> fList = new ArrayList<Favorites>();
        Favorites f = null;
        JsonObject o = null;
        for (int i = 0; i < favorites.size(); i++) {
            o = GsonJsonUtil.optJsonObject(favorites.get(i));
            if (null == o) {
                continue;
            }

            f = new Favorites();
            f.setID(GsonJsonUtil.optString(o.get("favuid"), "-1" + i));
            f.setOrderNum(i);
            f.setFavname(GsonJsonUtil.optString(o.get("favname"), ""));
            f.setGroupID(GsonJsonUtil.optString(o.get("gid"), "-1"));
            f.setDes(GsonJsonUtil.optString(o.get("describ"), ""));
            f.setHome(GsonJsonUtil.optString(o.get("home"), ""));
            f.setCityName(GsonJsonUtil.optString(o.get("cityName"), ""));
            f.setProvinceName(GsonJsonUtil.optString(o.get("provinceName"), ""));

            fList.add(f);
        }

        FavoritesDao dao = daoSession.getFavoritesDao();
        dao.deleteAll();
        dao.insertInTx(fList);
    }

    public static void deleteFriendGroup(final IActivityRequestThreadManager i, final String id, final Runnable succeedAction) {
        HttpRequestFacade.requestHttp(i, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_GROUP_DELETE;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
//				JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
//				JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                FriendGroupDao dao = GlobalVariableBean.getDaoSession(i.getContextHanler(), GlobalVariableBean.userInfo.memberId)
                        .getFriendGroupDao();
                dao.deleteByKey(id);

                succeedAction.run();

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("id", id);
            }
        });
    }

    public static void doAllTransfer(final IActivityRequestThreadManager i, final String targetGid, final String srcGid,
            final Runnable succeedAction) {
        HttpRequestFacade.requestHttp(i, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_GROUP_ALL_TRANSFER;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
//				JsonObject obj = GsonJsonUtil.parse(resultStr).getAsJsonObject();
//				JsonObject resultObj = obj.get(HttpRequestFacade.RESULT_PARAMS_RESULT).getAsJsonObject();

                FriendOperationHelper.requestFriendData(i, succeedAction);

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("gid1", srcGid);
                params.put("gid2", targetGid);
            }
        });
    }

    public static void doOneTransfer(final IActivityRequestThreadManager i, final String srcFid, final String targetGid,
            final Runnable succeedAction) {
        HttpRequestFacade.requestHttp(i, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_GROUP_ONE_TRANSFER;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                FriendOperationHelper.requestFriendData(i, succeedAction);

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("gid", targetGid);
                params.put("fuid", srcFid);
            }
        });
    }

    public static final void renameFriendGroup(final IActivityRequestThreadManager i, final String targetGid,
            final String newName, final Runnable succeedAction) {
        if (TextUtils.isEmpty(newName)) {
            return;
        }

        HttpRequestFacade.requestHttp(i, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_GROUP_SAVE;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                FriendOperationHelper.requestFriendData(i, succeedAction);

                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("name", newName);
                params.put("id", targetGid);
            }
        });
    }

    public static List<Map<String, String>> getFriendGroupWithoutSrcGid(String srcGid) {
        QueryBuilder<FriendGroup> b = GlobalVariableBean
                .getDaoSession(ApplicationContextUtil.getApplicationContext(), GlobalVariableBean.userInfo.memberId)
                .getFriendGroupDao().queryBuilder();
        if (!TextUtils.isEmpty(srcGid)) {
            b.where(FriendGroupDao.Properties.ID.notEq(srcGid), FriendGroupDao.Properties.ID.notEq("-1"));
        }
        List<FriendGroup> list = b.build().list();

        List<Map<String, String>> result = new ArrayList<Map<String, String>>(list.size());
        Map<String, String> m = null;
        for (FriendGroup fg : list) {
            m = new HashMap<String, String>();
            m.put("id", fg.getID());
            m.put("name", fg.getName());
            result.add(m);
        }

        return result;
    }

    public static void showDialogCancelRelation(final Context context, final String fuid, final Runnable succeedAction) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_friend_cancel, null);

        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog_NoTitle_NoActionBar);
        dialog.setContentView(v, new ViewGroup.LayoutParams(UnitUtil.transformDipToPx(300), LayoutParams.WRAP_CONTENT));

        v.findViewById(R.id.dialog_friend_cancel_button_confirm).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelFriendRelation((IActivityRequestThreadManager) context, fuid, succeedAction);
            }
        });
        v.findViewById(R.id.dialog_friend_cancel_button_goback).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static final void cancelFriendRelation(final IActivityRequestThreadManager i, final String fuid,
            final Runnable succeedAction) {
        HttpRequestFacade.requestHttp(i, new IHttpProcess() {

            @Override
            public String processUrl(int sign) {
                return GlobalVariableBean.APIRoot + GlobalConstant.URL_FRIEND_DELETE;
            }

            @Override
            public boolean processResponseSucceed(String resultStr, int sign) throws Exception {
                FriendOperationHelper.requestFriendData(i, succeedAction);
                return true;
            }

            @Override
            public boolean processResponseFailed(String resultStr, int sign) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void processParams(Map<String, Object> params, int sign) {
                params.put("sessionId", GlobalVariableBean.sessionId);
                params.put("memberId", GlobalVariableBean.userInfo.memberId);
                params.put("fuid", fuid);
            }
        });
    }

}
