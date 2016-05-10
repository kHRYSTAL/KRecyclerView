package me.khrystal.module;

import me.khrystal.deglgationlib.base.DelegateBaseModel;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 16/5/10
 * update time:
 * email: 723526676@qq.com
 */
public class Color extends DelegateBaseModel{

    public Color(int type){
        delegationViewType = type;
    }
}
