package com.my.test.demo.designpattern.factorymethod;

import lombok.Data;

/**
 * Description:TODO
 *
 * @author dsl
 * @date 2019/11/1 13:33
 */
@Data
public class LiuFeng {

    /**
     * 打扫
     */
    private boolean doSweep;

    /**
     * 洗涤
     */
    private boolean doWash;

    /**
     * 买米
     */
    private boolean doBuyRice;

    private String whichOne;

    public void sweep() {
        this.doSweep = true;
    }

    public void wash() {
        this.doWash = true;
    }

    public void buyRice() {
        this.doBuyRice = true;
    }
}
