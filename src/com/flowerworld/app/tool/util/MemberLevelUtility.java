package com.flowerworld.app.tool.util;

import android.view.View;
import com.flowerworld.app.R;

public class MemberLevelUtility {

    public static int switchMemberName(String name) {
        if (name.contains("理事")) {
            return R.drawable.member_lishi;
        } else if (name.contains("VIP")) {
            return R.drawable.member_vip;
        } else if (name.contains("普通")) {
            return R.drawable.member_normal;
        } else {
            return R.drawable.transparent_rectangle;
        }
    }

    public static int switchMemberNum(String level) {
        int i = -1;
        try {
            i = Integer.parseInt(level);
        } catch (Exception e) {
            i = -1;
        }
        return switchMemberNum(i);
    }

    public static int switchMemberNum(int level) {
        int resId = View.NO_ID;
        switch (level) {
        case 1:
            resId = R.drawable.member_normal;
            break;

        case 2:
            resId = R.drawable.member_vip;
            break;

        case 3:
            resId = R.drawable.member_normal;
            break;

        case 4:
            resId = R.drawable.member_normal;
            break;

        case 5:
            resId = R.drawable.member_normal;
            break;

        case 6:
            resId = R.drawable.member_normal;
            break;

        case 7:
            resId = R.drawable.member_normal;
            break;

        case 8:
            resId = R.drawable.member_normal;
            break;

        case 9:
            resId = R.drawable.member_normal;
            break;

        case 10:
            resId = R.drawable.member_normal;
            break;

        case 11:
            resId = R.drawable.member_lishi;
            break;

        case 12:
            resId = R.drawable.member_normal;
            break;

        case 13:
            resId = R.drawable.member_normal;
            break;

        default:
            resId = R.drawable.transparent_rectangle;
            break;
        }
        return resId;
    }
}
