package com.healthcare.modules.modle;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "STEPBEAN".
 */
public class stepbean {

    private String day;
    private Long bengin;
    private Long end;
    private Integer stepcount;
    private Integer source;

    public stepbean() {
    }

    public stepbean(String day, Long bengin, Long end, Integer stepcount, Integer source) {
        this.day = day;
        this.bengin = bengin;
        this.end = end;
        this.stepcount = stepcount;
        this.source = source;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getBengin() {
        return bengin;
    }

    public void setBengin(Long bengin) {
        this.bengin = bengin;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Integer getStepcount() {
        return stepcount;
    }

    public void setStepcount(Integer stepcount) {
        this.stepcount = stepcount;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

}
