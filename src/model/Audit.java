package model;

import security.*;
import java.util.Date;

public class Audit {

    private String   resourceId;
    private String   opsId;
    private String   byUserId;
    private String   refId;    //the foreign key
    private String   data;
    private Date     created;

    public Audit(String resourceId, String opsId, String refId, String data, String byUserId) {
        this.byUserId = byUserId;
        this.resourceId = resourceId;
        this.opsId = opsId;
        this.refId = refId;
        this.data = data;
        this.created = new Date();
    }

    public String getKey() {
        StringBuffer sb = new StringBuffer();
        sb.append(resourceId);
        sb.append("_");
        sb.append(opsId);
        sb.append("_");
        sb.append(refId);
        sb.append("_");
        sb.append(byUserId);
        sb.append("_");
        sb.append(created);

        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ResourceId: ");
        sb.append(resourceId);
        sb.append("OpsId: ");
        sb.append(opsId);
        sb.append("refId: ");
        sb.append(refId);
        sb.append("data: ");
        sb.append(data);
        sb.append("ByUserId: ");
        sb.append(this.byUserId);
        sb.append("created: ");
        sb.append(created);

        return sb.toString();
    }
}
