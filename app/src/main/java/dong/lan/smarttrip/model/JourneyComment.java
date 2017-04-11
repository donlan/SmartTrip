package dong.lan.smarttrip.model;


import dong.lan.model.bean.user.User;

/**
 * Created by 梁桂栋 on 16-12-3 ： 下午4:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 游历评论
 */

public class JourneyComment  {
    private Journey journey;        //所评论的游历
    private User author;            //评论人
    private String content;         //评论内容

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
