package me.sixhackathon.bumppay.paymitObjects;

import java.util.HashMap;
import java.util.Map;


public class SendMoney {

    private Integer amount;
    private String phoneNumber;
    private Integer creditCardId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount The amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The creditCardId
     */
    public Integer getCreditCardId() {
        return creditCardId;
    }

    /**
     * @param creditCardId The creditCardId
     */
    public void setCreditCardId(Integer creditCardId) {
        this.creditCardId = creditCardId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}