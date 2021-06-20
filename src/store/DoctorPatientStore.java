package store;

import model.Audit;
import model.User;
import security.Ops;
import security.Resource;
import security.Token;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 *  An implementation of the persistent layer for Doctor-Patient in lieu of RDBMS
 *
 */
public class DoctorPatientStore {

    // Doctor-Patient Table
    // key: Doctor
    // value: set of PATIENT
    private static Map<String, Set<String>> doctorPatientMap;

    // Patient-Doctor Table
    // key: patient
    // value: set of Doctor
    private static Map<String, Set<String>> patientDoctorMap;

    static {
        doctorPatientMap = new HashMap<String, Set<String>>();
        patientDoctorMap = new HashMap<String, Set<String>>();
    }

    /**
     *
     *    Add patient
     *
     * @param doctorId
     * @param patientId
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

        if (!patientDoctorMap.containsKey(patientId)) {
            Set<String> doctors = new HashSet<String>();
            doctors.add(doctorId);
            patientDoctorMap.put(patientId, doctors);
        } else {
            Set<String> doctors = patientDoctorMap.get(patientId);
            doctors.add(doctorId);
        }

        addAudit(doctorId, Ops.CREATE.name());
    }


    public static User getDoctor(String patientId) {

        if (patientDoctorMap.containsKey(patientId)) {
            final Set<String> doctors = patientDoctorMap.get(patientId);
            final String doctorId = doctors.stream().collect(Collectors.toList()).get(0);
            return UserStore.retrieveUser(doctorId);
        }

        return null;
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
