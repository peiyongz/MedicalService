package store;

import model.Audit;
import model.User;
import model.Visit;
import security.Ops;
import security.Resource;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 *  An implementation of the persistent layer for Visit in lieu of RDBMS
 *
 */
public class VisitStore {

    // Visit Table
    // key: visitId
    // value: visit
    // indexed by visitId
    private static Map<Integer, Visit> visitStore;

    // key: doctor
    // value: patients' visits
    // indexed by doctor + patient
    private static Map<String, Map<String, List<Visit>>> visitsStore;

    private static int visitCount;

    // key: visitId
    // value: visit
    static {
        visitStore = new HashMap<Integer, Visit>();
        visitsStore = new HashMap<String, Map<String, List<Visit>>>();
        visitCount = 0;
    }

    /**
        "INSERT VISIT ..."
    */
    public static boolean createVisit(Visit visit) {

        if (visitStore.containsKey(visit.getId())) {
            return false;
        }

        visitStore.put(visit.getId(), visit);

        final String doctorId = visit.getDoctorId();
        final String patientId = visit.getPatientId();
        final Map<String, List<Visit>> patientMap = visitsStore.containsKey(doctorId) ?
                                                    visitsStore.get(doctorId) :
                                                    new HashMap<String, List<Visit>>();
        final List<Visit> patientVisits = patientMap.containsKey(patientId) ?
                                          patientMap.get(patientId) :
                                          new ArrayList<Visit>();
        patientVisits.add(visit);
        patientMap.put(patientId, patientVisits);
        visitsStore.put(doctorId, patientMap);

        addAudit(visit, Ops.CREATE.name());
        return true;
    }

    /**
     "SELECT * FROM VISIT WHERE ..."
     */
    public static Visit retrieveVisit(int visitId) {
        return visitStore.get(visitId);
    }

    /**
     * "SELECT * FROM VISIT
     *  WHERE DOCTORID = doctorId AND
     *        PATIENTID = patientId
     */
    public static List<Visit> retrieveVisits(String doctorId, String patientId) {

        if (!visitsStore.containsKey(doctorId)) {
            return null;
        }

        //we don't audit retrieval
        return visitsStore.get(doctorId).get(patientId);
    }

    /**
        "UPDATE VISIT ... WHERE ..."
     */
    public static boolean updateVisit(Visit visit) {

        if (!visitStore.containsKey(visit.getId())) {
            return false;
        }

        visitStore.put(visit.getId(), visit);
        addAudit(visit, Ops.UPDATE.name());
        return true;
    }

    public static int getVisitId() {
        final int curCount = visitCount;
        visitCount++;
        return curCount;
    }

    private static void addAudit(Visit visit, String ops) {
        AuditStore.add(new Audit(Resource.VISIT.name(), ops, visit.getKey(), visit.toString(), visit.getDoctorId()));
    }
}
