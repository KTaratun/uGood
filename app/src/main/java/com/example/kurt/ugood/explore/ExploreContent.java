package com.example.kurt.ugood.explore;

public class ExploreContent {
    private String content;
    private boolean favorite, selected;

    public ExploreContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
