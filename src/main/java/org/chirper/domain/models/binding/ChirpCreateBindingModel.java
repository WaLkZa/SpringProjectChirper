package org.chirper.domain.models.binding;

import javax.validation.constraints.NotNull;

public class ChirpCreateBindingModel {

    @NotNull
    private String content;

    public ChirpCreateBindingModel() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
