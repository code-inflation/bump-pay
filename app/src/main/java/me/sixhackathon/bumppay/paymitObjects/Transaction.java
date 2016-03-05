package me.sixhackathon.bumppay.paymitObjects;

import java.util.HashMap;
import java.util.Map;


public class Transaction {

    private Integer id;
    private String number;
    private Integer timestamp;
    private Integer type;
    private Integer status;
    private Integer senderUserId;
    private Integer receiverUserId;
    private Integer amount;
    private String clientDataRequestType;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number The number
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return The timestamp
     */
    public Integer getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp The timestamp
     */
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The senderUserId
     */
    public Integer getSenderUserId() {
        return senderUserId;
    }

    /**
     * @param senderUserId The senderUserId
     */
    public void setSenderUserId(Integer senderUserId) {
        this.senderUserId = senderUserId;
    }

    /**
     * @return The receiverUserId
     */
    public Integer getReceiverUserId() {
        return receiverUserId;
    }

    /**
     * @param receiverUserId The receiverUserId
     */
    public void setReceiverUserId(Integer receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

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
     * @return The clientDataRequestType
     */
    public String getClientDataRequestType() {
        return clientDataRequestType;
    }

    /**
     * @param clientDataRequestType The clientDataRequestType
     */
    public void setClientDataRequestType(String clientDataRequestType) {
        this.clientDataRequestType = clientDataRequestType;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}