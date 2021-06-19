package store;

import model.Audit;
import model.User;
import security.Ops;
import security.Resource;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 *  An implementation of the persistent layer for Doctor-Patient in lieu of RDBMS
 *
 */
public class DoctorPatientStore {

    // Doctor-Patient Table
    // key: Doctor
    // value: set of PATIENT
    private static Map<String, Set<String>> doctorPatientMap;

    static {
        doctorPatientMap = new HashMap<String, Set<String>>();
    }

    /**
        "INSERT PROVIDER_PATIENT ..."
    */
    public static void addPatient(String doctorId, String patientId) {

        if (!doctorPatientMap.containsKey(doctorId)) {
            Set<String> patients = new HashSet<String>();
            patients.add(patientId);
            doctorPatientMap.put(doctorId, patients);
        } else {
            Set<String> patients = doctorPatientMap.get(doctorId);
            patients.add(patientId);
        }

        addAudit(doctorId, Ops.CREATE.name());
    }


    /**
         "SELECT * FROM PROVIDER_PATIENT WHERE ..."
     */
    public static boolean IsDocPatient(String doctorId, String patientId) {
        final Set<String> patients = doctorPatientMap.get(doctorId);
        return patients == null ? false : patients.contains(patientId);
    }

    private static void addAudit(String doctorId, String ops) {
        AuditStore.add(new Audit(Resource.DOCTOR_PATIENT.name(), ops, doctorId, doctorId, doctorId));
    }
}
