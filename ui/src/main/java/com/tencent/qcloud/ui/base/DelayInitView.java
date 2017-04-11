package com.tencent.qcloud.ui.base;

/**
 * Created by 梁桂栋 on 17-3-23 ： 下午3:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 用于延迟加载的接口,例如首页初始化完用户信息后,才进行其他数据的初始化
 */

public interface DelayInitView<T> {

    void start(T data);
}
