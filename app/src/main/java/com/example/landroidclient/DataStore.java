package com.example.landroidclient;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.vanniktech.rxpermission.Permission;
import com.vanniktech.rxpermission.RealRxPermission;

import java.util.Set;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class DataStore {

    public final static String KEY_CONTEXT  = "ID_CONTEXT";

    public static long getLong(Context context, String key, long defValue) {
        return getSettings(context).getLong(key, defValue);
    }

    public static void putLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long getInt(Context context, String key, int defValue) {
        return getSettings(context).getInt(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getSettings(context).getBoolean(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static float getFloat(Context context, String key, float defValue) {
        return getSettings(context).getFloat(key, defValue);
    }

    public static void putFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static Set<String> getStringSet(Context context, String key, Set<String> defValue) {
        return getSettings(context).getStringSet(key, defValue);
    }

    public static void putStringSet(Context context, String key, Set<String> value) {
        getEditor(context).putStringSet(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return getSettings(context).getString(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static Single<Boolean> realRxPermisssion(Context context) {
        return RealRxPermission.getInstance(context)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .all(new Predicate<Permission>() {
                    @Override
                    public boolean test(Permission permission) {
                        return permission.state() == Permission.State.GRANTED;
                    }
                })
                .flatMap(new Function<Boolean, Single<Boolean>>() {
                    @Override
                    public Single<Boolean> apply(Boolean granted) {
                        if (granted) {
                            return Single.just(granted);
                        }
                        return Single.error(new SecurityException("Missing Permission for Unik V2"));
                    }
                });
    }

    private static void remove(Context context, String key) {
        getEditor(context).remove(key).commit();
    }

    private static SharedPreferences getSharedPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSettings(context).edit();
    }

    private static SharedPreferences getSettings(Context context) {
        return getSharedPreference(context);
    }

}
