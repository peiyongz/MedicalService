package model;

import store.MedicalStudyStore;

import java.util.Date;

public class MedicalStudy {

    private int    studyId;
    private String studyName;
    private String description;
    private Date   launchTime;
    private Date   expireTime;

    public MedicalStudy(String name, String description, Date launchTime, Date expireTime) {
        this.studyId = MedicalStudyStore.getStudyId();
        this.studyName = name;
        this.description = description;
        this.launchTime = launchTime;
        this.expireTime = expireTime;
    }

    public int getId() {
        return studyId;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("StudyId: ");
        sb.append(this.studyId);
        sb.append("StudyName: ");
        sb.append(this.studyName);
        sb.append("Description: ");
        sb.append(this.description);
        sb.append("LaunchTime: ");
        sb.append(this.launchTime);
        sb.append("ExpiryTime: ");
        sb.append(this.expireTime);

        return sb.toString();
    }
}
