package model;

import security.IAMRole;

import java.util.Date;

public class User {

    //Info available at the time of registration
    private String userId;       //Not modifiable

    private String firstName;
    private String lastName;
    private IAMRole iamRole;
    private Gender gender;
    private Date   DOB;
    private String phone;
    private String email;
    private String address;

    //info will be updated from Visit
    private int    bloodPressureLow;
    private int    bloodPressureHigh;
    private int    heartRateInMinute;
    private int    height;
    private int    weight;
    private Date   creationTime;
    private Date   lastUpdateTime;

    public User(String   userId,
                String   firstName,
                String   lastName,
                IAMRole  iamRole,
                Gender   gender,
                Date     dob,
                String   phone,
                String   email,
                String   address) {
        this.userId    = userId;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.iamRole   = iamRole;
        this.gender    = gender;
        this.DOB       = dob;
        this.phone     = phone;
        this.email     = email;
        this.address   = address;
        this.creationTime = new Date();
        this.lastUpdateTime = new Date();

    }

    /**
     *
     * @param bpl
     * @param bph
     * @param hr
     * @param height
     * @param weight
     */
    public void updateBio(int bpl, int bph, int hr, int height, int weight) {
       this.bloodPressureLow=bpl;
       this.bloodPressureHigh=bph;
       this.heartRateInMinute=hr;
       this.height=height;
       this.weight=weight;
    }

    public String getKey() {
        return getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public IAMRole getIamRole() { return iamRole; };

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("UserId: ");
        sb.append(this.userId);
        sb.append("First Name: ");
        sb.append(this.firstName);
        sb.append("Last Name: ");
        sb.append(this.lastName);
        sb.append("IAM Role: ");
        sb.append(this.iamRole);
        sb.append("Gender: ");
        sb.append(this.gender);
        sb.append("DOB: ");
        sb.append(this.DOB);
        sb.append("Phone: ");
        sb.append(this.phone);
        sb.append("Email: ");
        sb.append(this.email);
        sb.append("Address: ");
        sb.append(this.address);
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

    //to expose non-privacy model
}
