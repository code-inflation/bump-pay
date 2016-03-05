package me.sixhackathon.bumppay.paymitObjects;

import java.util.HashMap;
import java.util.Map;

public class SignInBody {

    private String type;
    private String smscode;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The smscode
     */
    public String getSmscode() {
        return smscode;
    }

    /**
     * @param smscode The smscode
     */
    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
