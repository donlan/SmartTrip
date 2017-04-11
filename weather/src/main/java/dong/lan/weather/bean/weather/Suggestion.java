package dong.lan.weather.bean.weather;

/**
 * Author: dooze
 * Created by: ModelGenerator on 17-4-9
 */
public class Suggestion {
    public Air air;
    public Comf comf;
    public Cw cw;
    public Drsg drsg;
    public Flu flu;
    public Sport sport;
    public Trav trav;
    public Uv uv;

    @Override
    public String toString() {
        return "Suggestion{" +
                "air=" + air +
                ", comf=" + comf +
                ", cw=" + cw +
                ", drsg=" + drsg +
                ", flu=" + flu +
                ", sport=" + sport +
                ", trav=" + trav +
                ", uv=" + uv +
                '}';
    }
}