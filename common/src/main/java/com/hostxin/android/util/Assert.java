
package com.hostxin.android.util;

import android.text.TextUtils;
import java.lang.CharSequence;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.String;

public final class Assert {
    public static void isTrue(boolean bl, Object object) {
        if (bl) return;
        throw new IllegalStateException(String.valueOf(object));
    }

    public static Object neNull(Object object) {
        if (object != null) return object;
        throw new NullPointerException(("obj = " + object));
    }

    public static Object notEmpty(Object object, Object object2) {
        if (object != null) return object;
        throw new NullPointerException(String.valueOf((Object)(object2)));
    }

    public static String notEmpty(String string) {
        if (!(TextUtils.isEmpty((CharSequence)(string)))) return string;
        throw new IllegalArgumentException(("String is = " + string));
    }

    public static String notEmpty(String string, Object object) {
        if (!(TextUtils.isEmpty((CharSequence)(string)))) return string;
        throw new IllegalArgumentException(String.valueOf((Object)(object)));
    }

    public static void notEmpty(boolean bl, Object object) {
        if (bl) return;
        throw new IllegalArgumentException(String.valueOf((Object)(object)));
    }

    public static void valid(boolean bl) {
        if (bl) return;
        throw new IllegalStateException();
    }
}

