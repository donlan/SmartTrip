package dong.lan.model.bean.checklist;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 17-3-7 ： 下午12:31.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
@RealmClass
public class CheckItem extends RealmObject {
    private boolean isCheck;
    private String name;
    private String remark;

    public CheckItem() {
    }

    public CheckItem(boolean isCheck, String name, String remark) {
        this.isCheck = isCheck;
        this.name = name;
        this.remark = remark;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
