package com.kindle.domain;

public class AmazonPay {
    private String userName;
    private Integer balance;

    public AmazonPay() {
        super();
    }

    public AmazonPay(String userName, Integer balance) {
        this.userName = userName;
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AmazonPay{" +
                "userName='" + userName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
