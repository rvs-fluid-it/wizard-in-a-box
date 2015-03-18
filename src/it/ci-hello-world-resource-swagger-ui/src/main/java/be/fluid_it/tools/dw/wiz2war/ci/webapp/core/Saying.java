package be.fluid_it.tools.dw.wiz2war.ci.webapp.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

@ApiModel(value = "What has to be said")
public class Saying {
    private long id;

    @Length(max = 3)
    private String content;

    public Saying() {
        // Jackson deserialization
    }

    public Saying(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    @ApiModelProperty(value = "Identification", required=true)
    public long getId() {
        return id;
    }

    @JsonProperty
    @ApiModelProperty(value = "The message", required=true)
    public String getContent() {
        return content;
    }
}