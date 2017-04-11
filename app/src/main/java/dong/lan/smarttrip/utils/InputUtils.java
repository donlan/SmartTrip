package dong.lan.smarttrip.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by 梁桂栋 on 17-2-24 ： 下午3:40.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class InputUtils {

    private InputUtils() {
    }

    public static void hideInputKeyboard(EditText editText) {
        Context context = editText.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        }
    }

    public static void showInputKeyboard(EditText editText) {
        Context context = editText.getContext();
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(editText, 0);
    }

}
