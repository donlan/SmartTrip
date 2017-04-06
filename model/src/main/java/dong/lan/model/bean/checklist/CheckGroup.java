package dong.lan.model.bean.checklist;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 17-3-7 ： 下午12:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

@RealmClass
public class CheckGroup extends RealmObject {
    private String groupName;
    private RealmList<CheckItem> checkItems;

    public CheckGroup(String groupName, RealmList<CheckItem> checkItems) {

        this.groupName = groupName;
        this.checkItems = checkItems;
    }

    public CheckGroup() {
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public RealmList<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(RealmList<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }
}
