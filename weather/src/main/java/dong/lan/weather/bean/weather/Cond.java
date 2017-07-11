package dong.lan.weather.bean.weather;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class Cond {
    public String code_d;
    public String code_n;
    public String txt_d;
    public String txt_n;
    public String code;
    public String txt;

    @Override
    public String toString() {
        return "Cond{" +
                "code_d='" + code_d + '\'' +
                ", code_n='" + code_n + '\'' +
                ", txt_d='" + txt_d + '\'' +
                ", txt_n='" + txt_n + '\'' +
                ", code='" + code + '\'' +
                ", txt='" + txt + '\'' +
                '}';
    }
}