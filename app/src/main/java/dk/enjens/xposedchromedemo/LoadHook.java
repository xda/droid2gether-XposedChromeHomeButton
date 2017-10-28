package dk.enjens.xposedchromedemo;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class LoadHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if(!lpparam.packageName.equals("com.android.chrome"))
            return;
        XposedBridge.log("Loaded app: " + lpparam.packageName);
        Class<?> tintedImgButton = findClass("org.chromium.chrome.browser.widget.TintedImageButton", lpparam.classLoader);

        findAndHookMethod("org.chromium.chrome.browser.toolbar.ToolbarPhone", lpparam.classLoader, "onFinishInflate", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // this will be called before the clock was updated by the original method
            }
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                XposedBridge.log("in onFinishInflate");
                ImageButton obj = (ImageButton)getObjectField(param.thisObject, "mHomeButton");
                obj.setBackgroundColor(Color.RED);
                obj.setAlpha(1.0f);
            }
        });
    }
}
