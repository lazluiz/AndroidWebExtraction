package com.zelius.webext.model;

import com.zelius.webext.data.DataWebsite;

/**
 * Created by Zelius on 30/03/2015.
 */
public class ContentModel {
    private DataWebsite.EnumContents content;
    private String selector;

    public ContentModel(DataWebsite.EnumContents content, String selector) {
        this.content = content;
        this.selector = selector;
    }

    public DataWebsite.EnumContents getContent() {
        return content;
    }

    public void setContent(DataWebsite.EnumContents content) {
        this.content = content;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }
}
