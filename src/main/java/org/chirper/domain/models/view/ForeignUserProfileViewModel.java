package org.chirper.domain.models.view;

public class ForeignUserProfileViewModel {
    private String id;

    private String username;

    public ForeignUserProfileViewModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
