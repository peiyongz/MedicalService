package store;

import model.User;
import model.Visit;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 *  An implementation of the persistent layer for Visit in lieu of RDBMS
 *
 */
public class VisitStore {

    // mock Visit Table
    private static Map<String, Visit> visitStore;

    // mock Doctor-Patient Table
    // key: user with UserType == DOCTOR
    // value: user with UserType == PATIENT
    private static Map<User, User> doctorPatientMap;

    static {
        visitStore = new HashMap<String, Visit>();
        doctorPatientMap = new HashMap<User, User>();
    }

    /**
        "INSERT VISIT ..."
    */
    public static boolean createVisit(Visit visit) {

        if (visitStore.containsKey(visit.getKey())) {
            return false;
        }

        visitStore.put(visit.getKey(), visit);
        return true;
    }

    /**
     "SELECT * FROM VISIT WHERE ..."
     */
    public static Visit retrieveVisit(String doctorId, String patientId, Date visitTime) {
        return visitStore.get(Visit.getVisitKey(doctorId, patientId, visitTime));
    }

    /**
        "UPDATE VISIT ... WHERE ..."
     */
    public static boolean updateVisit(Visit visit) {

        if (!visitStore.containsKey(visit.getKey())) {
            return false;
        }

        visitStore.put(visit.getKey(), visit);
        return true;
    }

}
