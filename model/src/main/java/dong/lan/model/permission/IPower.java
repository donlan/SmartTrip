package dong.lan.model.permission;

/**
 * Created by 梁桂栋 on 17-3-22 ： 下午6:25.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 判断一个用户时候可以进行某项操作
 */

public interface IPower {

    boolean hasPower(IRole role);
}

