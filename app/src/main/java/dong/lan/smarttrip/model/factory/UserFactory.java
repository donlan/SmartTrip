package dong.lan.smarttrip.model.factory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.github.promeg.pinyinhelper.Pinyin;
import com.tencent.TIMFriendGenderType;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMUserProfile;

import java.util.ArrayList;
import java.util.List;

import dong.lan.model.Config;
import dong.lan.model.features.IUser;
import dong.lan.model.features.IUserInfo;
import dong.lan.model.bean.user.User;
import dong.lan.model.permission.Permission;

/**
 * Created by 梁桂栋 on 17-2-25 ： 下午1:49.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class UserFactory {
    private UserFactory() {
        //no instance
    }

    public static IUserInfo createUser(TIMUserProfile userProfile) {
        return new ProfileUser(userProfile);
    }


    public static User createRealmUser(TIMUserProfile userProfile) {
        User user = new User();
        user.setUsername(userProfile.getIdentifier());
        user.setRemark(userProfile.getRemark());
        user.setNickname(userProfile.getNickName());
        user.setSex(userProfile.getGender() == TIMFriendGenderType.Male ? 1 : 0);
        user.setIdentifier(userProfile.getIdentifier());
        user.setAvatarUrl(userProfile.getFaceUrl());
        user.setPhone(user.getIdentifier().substring(3));
        return user;
    }

    public static IUserInfo createUser(TIMGroupMemberInfo info) {
        return new GroupMemberUser(info);
    }

    public static List<IUserInfo> createUserGroup(List<TIMGroupMemberInfo> infos) {
        if (infos == null)
            return null;
        List<IUserInfo> users = new ArrayList<>();
        for (TIMGroupMemberInfo info :
                infos) {
            users.add(new GroupMemberUser(info));
        }
        return users;
    }


    public static List<IUserInfo> createUser(List<TIMUserProfile> profiles) {
        if (profiles == null)
            return null;
        List<IUserInfo> users = new ArrayList<>();
        for (TIMUserProfile profile :
                profiles) {
            users.add(new ProfileUser(profile));
        }
        return users;
    }


    public static class GroupMemberUser implements IUserInfo {

        private static final String TAG = GroupMemberUser.class.getSimpleName();
        TIMGroupMemberInfo memberInfo;

        public GroupMemberUser(TIMGroupMemberInfo memberInfo) {
            Log.d(TAG, "GroupMemberUser: "+memberInfo.getCustomInfo());
            this.memberInfo = memberInfo;
        }

        @Override
        public String getObjId() {
            return null;
        }

        @Override
        public String getUsername() {
            return memberInfo.getNameCard();
        }

        @Override
        public String getIdentifier() {
            return memberInfo.getUser();
        }

        @Override
        public String getAvatarUrl() {
            return null;
        }

        @Override
        public String getPinYin() {
            return Pinyin.toPinyin(getUsername(), "");
        }

        @Override
        public void call(Context context) {

        }

        @Override
        public void sms(Context context) {

        }


        @Override
        public String display() {
            String n = Pinyin.toPinyin(displayName(), "");
            return TextUtils.isEmpty(n) ? "#" : n.substring(0, 1).toUpperCase();
        }

        @Override
        public IUser getUser() {
            return null;
        }

        @Override
        public int getRole() {
            return Permission.LEVEL_OTHER;
        }

        @Override
        public int getStatus() {
            return Config.STATUS_NORMAL;
        }

        @Override
        public int getSex() {
            return 0;
        }

        @Override
        public String displayName() {
            if (TextUtils.isEmpty(memberInfo.getNameCard())) {
                return memberInfo.getUser();
            }
            return memberInfo.getNameCard();
        }

        @Override
        public int compareTo(@NonNull IUserInfo o) {
            if (o == this)
                return 0;
            return displayName().compareTo(o.displayName());
        }
    }

    public static class ProfileUser implements IUserInfo {

        private TIMUserProfile profile;

        public ProfileUser(TIMUserProfile profile) {
            this.profile = profile;
        }

        @Override
        public String getObjId() {
            return null;
        }

        @Override
        public String getUsername() {
            if (TextUtils.isEmpty(profile.getRemark())) {
                if (!TextUtils.isEmpty(profile.getNickName())) {
                    return profile.getNickName();
                }
            } else {
                return profile.getRemark();
            }
            return profile.getIdentifier();
        }

        @Override
        public String getIdentifier() {
            return profile.getIdentifier();
        }

        @Override
        public String getAvatarUrl() {
            return profile.getFaceUrl();
        }

        @Override
        public String getPinYin() {
            return Pinyin.toPinyin(getUsername(), "");
        }

        @Override
        public void call(Context context) {

        }

        @Override
        public void sms(Context context) {

        }


        @Override
        public String display() {
            String n = Pinyin.toPinyin(getUsername(), "");
            return TextUtils.isEmpty(n) ? "#" : n.substring(0, 1).toUpperCase();
        }

        @Override
        public IUser getUser() {
            return null;
        }

        @Override
        public int getRole() {
            return Permission.LEVEL_OTHER;
        }

        @Override
        public int getStatus() {
            return Config.STATUS_NORMAL;
        }

        @Override
        public int getSex() {
            return profile.getGender() == TIMFriendGenderType.Male ? 1 : 0;
        }

        @Override
        public String displayName() {
            if (TextUtils.isEmpty(profile.getRemark())) {
                if (TextUtils.isEmpty(profile.getNickName())) {
                    return profile.getIdentifier();
                }
                return profile.getNickName();
            }
            return profile.getRemark();
        }

        @Override
        public int compareTo(@NonNull IUserInfo o) {
            if (o == this)
                return 0;
            return displayName().compareTo(o.displayName());
        }
    }
}
