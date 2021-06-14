package model;

import java.util.Date;

public class MedicalStudy {

    private String studyName;
    private Date   launchTime;
    private Date   expireTime;

    public MedicalStudy(String name, Date launchTime, Date expireTime) {
        this.studyName = name;
        this.launchTime = launchTime;
        this.expireTime = expireTime;
    }
}
