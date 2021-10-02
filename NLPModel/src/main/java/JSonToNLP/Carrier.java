package JSonToNLP;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Carrier {

    private TagObject[] tokens;

    public void setTokens(TagObject[] tokens) {
        this.tokens = tokens;
    }
    @JsonProperty("tokens")
    public TagObject[] getTokens() {
        return tokens;
    }
}
