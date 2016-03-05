package me.sixhackathon.bumppay.paymitObjects;

import java.util.HashMap;
import java.util.Map;

public class Balance {

    private Integer balance;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The balance
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     * The balance
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}