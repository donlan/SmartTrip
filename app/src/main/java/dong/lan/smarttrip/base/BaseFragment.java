package dong.lan.smarttrip.base;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.tencent.qcloud.ui.Dialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 梁桂栋 on 17-1-18 ： 下午9:52.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class BaseFragment extends Fragment implements BaseView,DelayInitView {

    protected final static String KEY_TITTLE = "tittle";
    protected  boolean isStart;
    protected View content;
    private Unbinder unbinder;
    private Dialog dialog;

    public void dialog(String text) {
        if (dialog == null) {
            dialog = new Dialog(getContext());
        }
        dialog.setMessageText(text).show();
    }

    @Override
    public void show(String text) {
        if (getView() != null)
            Snackbar.make(getView(), text, Snackbar.LENGTH_SHORT).show();
        else
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void bindView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        content = null;
        if (unbinder != null)
            unbinder.unbind();
        unbinder = null;
        if (dialog != null)
            dialog.destroy();
        dialog = null;
    }

    @Override
    public void start(Object data) {
        isStart = true;
    }
}
