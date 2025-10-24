package com.ratnakar.pojo;

import java.util.List;

public class CreateOrderApiBase {
    private List<CreateOrderApiDetails> orders;

    public List<CreateOrderApiDetails> getOrders() {
        return orders;
    }

    public void setOrders(List<CreateOrderApiDetails> orders) {
        this.orders = orders;
    }
}
