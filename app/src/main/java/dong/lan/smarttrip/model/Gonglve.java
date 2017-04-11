package dong.lan.smarttrip.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午7:26.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class Gonglve {

    public  enum TYPE{
        NO("none"),T1(""),WENDA("问答"),ZIYOUXING("自由行攻略"),GUANFANG("官方账号");
        private String type;
        private  TYPE(String type){
            this.type = type;
        }

    }

    private String url;
    private String tittle;
    private String text;
    private List<String> imgUrls;
    private Integer start;
    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    static class Builder{
        private Gonglve gonglve;
        private List<String> imageUrls;
        public Builder(){
            gonglve = new Gonglve();
        }
        public Builder tittle(String tittle){
            gonglve.setTittle(tittle);
            return this;
        }
        public Builder text(String text){
            gonglve.setText(text);
            return this;
        }

        public Builder start(Integer start){
            gonglve.setStart(start);
            return this;
        }

        public Builder url(String url){
            gonglve.setUrl(url);
            return this;
        }
        public Builder addImageUrl(String url){
            if(imageUrls==null)
                imageUrls = new ArrayList<>();
            imageUrls.add(url);
            return this;
        }
        public Builder addImageUrls(List<String> imageUrls){
            if(this.imageUrls==null)
                this.imageUrls = new ArrayList<>();
            this.imageUrls.addAll(imageUrls);
            return this;
        }
        public Gonglve build(){
            gonglve.setImgUrls(imageUrls);
            return gonglve;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Gonglve)
            return ((Gonglve)obj).url.equals(url);
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if(url==null)
            return super.hashCode();
        return url.hashCode();
    }

    @Override
    public String toString() {
        return "tittle:"+tittle+"\ntext:"+text+"\nurl:"+url+"\nimgs:"+imgUrls.size()+"\nstart:"+start;
    }
}
