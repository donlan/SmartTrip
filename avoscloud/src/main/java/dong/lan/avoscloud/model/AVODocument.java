package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:02.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

@AVClassName("Document")
public class AVODocument extends AVObject {

    public void setTravel(AVOTravel travel){
        put("travel",travel);
    }

    public AVOTravel getTravel(){
        try {
            return getAVObject("travel",AVOTravel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public AVUser getCreator(){
        return super.getAVUser("creator");
    }

    public void setCreator(AVUser user){
        super.put("creator",user);
    }


    public AVFile getRawDocument(){
        return super.getAVFile("documentFile");
    }

    public void setRawDocument(AVFile file){
        super.put("documentFile",file);
    }

    public String getId(){
        return getString("id");
    }

    public void setId(String id){
        put("id",id);
    }

    public long getCreateTime(){
        return getLong("createTime");
    }

    public void setCreateTime(long createTime){
        put("createTime",createTime);
    }
    public String getName(){
        return getString("name");
    }

    public void setName(String name){
        put("name",name);
    }

    public String getUrl(){
        return getString("url");
    }

    public void setUrl(String url){
        put("url",url);
    }

    public String getLocalPath(){
        return getString("localPath");
    }

    public void setLocalPath(String localPath){
        put("localPath",localPath);
    }

    public int getRole(){
        return getInt("role");
    }

    public void setRole(int role){
        put("role",role);
    }
}
