package com.zelius.webext.model;

import com.zelius.webext.data.DataWebsite;

/**
 * Created by Zelius on 30/03/2015.
 */
public class SectionModel {
    private DataWebsite.EnumSections section;
    private String label;
    private String url;

    public SectionModel(DataWebsite.EnumSections section, String label, String url) {
        this.section = section;
        this.label = label;
        this.url = url;
    }

    public DataWebsite.EnumSections getSection() {
        return section;
    }

    public void setSection(DataWebsite.EnumSections section) {
        this.section = section;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
