package org.chirper.domain.models.binding;

import javax.validation.constraints.NotNull;

public class ChirpEditBindingModel {

    @NotNull
    private String content;

    public ChirpEditBindingModel() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
