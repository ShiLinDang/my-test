package com.my.test.demo.tes;

import com.my.test.demo.entity.SysUser;

import java.util.Random;

/**
 * Description:Synchronized 测试类
 *
 * @author dsl
 * @date 2020/2/27 11:43
 */
public class SynchronizedDemo {
    public static void main(String[] args) {
        BankAccount myAccount = new BankAccount("accountOfMG",10000.00);
        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int var = new Random().nextInt(100);
                        Thread.sleep(var);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double deposit = myAccount.deposit(1000.00);
                    System.out.println(Thread.currentThread().getName()+" balance:"+deposit);
                }
            }).start();
        }
        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int var = new Random().nextInt(100);
                        Thread.sleep(var);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double deposit = myAccount.withdraw(1000.00);
                    System.out.println(Thread.currentThread().getName()+" balance:"+deposit);

                }
            }).start();
        }
    }

    private static class BankAccount{
        String accountName;
        double balance;

        public BankAccount(String accountName,double balance){
            this.accountName = accountName;
            this.balance = balance;
        }

        public synchronized double deposit(double amount){
            balance = balance + amount;
            return balance;
        }

        public synchronized double  withdraw(double amount){
            balance = balance - amount;
            return balance;
        }
    }
}
