package model;

import store.VisitStore;

import java.util.Date;

public class Visit {

    private Integer visitId;

    private String doctorId;        // a UserId whose UserType is  DOCTOR
    private String patientId;       // a UserId whose UserType is  PATIENT
    private Date   visitTime;       // visit time

    private String diagnostics;
    private String treatment;
    private Date   creationTime;
    private Date   lastUpdateTime;

    //info captured during the Visit
    private int    bloodPressureLow;
    private int    bloodPressureHigh;
    private int    heartRateInMinute;
    private int    height;
    private int    weight;

    public Visit(String doctorId,
                 String patientId,
                 Date   visitTime,
                 String diagnostics,
                 String treatment,
                 int    bpl,
                 int    bph,
                 int    hr,
                 int    height,
                 int    weight
                ) {
        this.visitId = VisitStore.getVisitId();
        this.doctorId  = doctorId;
        this.patientId = patientId;
        this.visitTime = visitTime;
        this.diagnostics= diagnostics;
        this.treatment= treatment;

        this.bloodPressureLow=bpl;
        this.bloodPressureHigh=bph;
        this.heartRateInMinute=hr;
        this.height=height;
        this.weight=weight;

        this.creationTime = new Date();
        this.lastUpdateTime = new Date();
    }

    public String getKey() {
        return Integer.toString(getId());
    }

    public int getId() {
        return this.visitId;
    }

    public String getDoctorId() {
        return this.doctorId;
    }

    public String getPatientId() {
        return this.patientId;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("VisitId: ");
        sb.append(this.visitId);
        sb.append("DoctorId: ");
        sb.append(this.doctorId);
        sb.append("PatientId: ");
        sb.append(this.patientId);
        sb.append("VisitTime: ");
        sb.append(this.visitTime);
        sb.append("Diagnostics: ");
        sb.append(this.diagnostics);
        sb.append("Treatment: ");
        sb.append(this.treatment);
        sb.append("Phone: ");
        sb.append("Blood Pressure Low: ");
        sb.append(this.bloodPressureLow);
        sb.append("Blood Pressure High: ");
        sb.append(this.bloodPressureHigh);
        sb.append("Heart Rate: ");
        sb.append(this.heartRateInMinute);
        sb.append("Height: ");
        sb.append(this.height);
        sb.append("Weight: ");
        sb.append(this.weight);

        return sb.toString();
    }

}
