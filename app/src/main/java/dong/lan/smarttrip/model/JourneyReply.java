package dong.lan.smarttrip.model;


import dong.lan.model.bean.user.User;

/**
 * Created by 梁桂栋 on 16-12-3 ： 下午4:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 游历评论回复
 */

public class JourneyReply {
    private JourneyComment comment; //所属与的评论
    private User author;            //回复人
    private String content;         //回复内容

    public JourneyComment getComment() {
        return comment;
    }

    public void setComment(JourneyComment comment) {
        this.comment = comment;
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
