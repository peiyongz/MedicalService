package store;

import model.User;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 *  This class mock the persistent layer functionalities
 *
 *  Operations on  Provider-Patient
 *
 */
public class DoctorPatientStore {

    // mock Doctor-Patient Table
    // key: PROVIDER
    // value: set of PATIENT
    private static Map<String, Set<String>> providerPatientMap;

    static {
        providerPatientMap = new HashMap<String, Set<String>>();
    }

    /**
        "INSERT PROVIDER_PATIENT ..."
    */
    public static void addPatient(String providerId, String patientId) {

        if (!providerPatientMap.containsKey(providerId)) {
            Set<String> patients = new HashSet<String>();
            patients.add(patientId);
            providerPatientMap.put(providerId, patients);
        } else {
            Set<String> patients = providerPatientMap.get(providerId);
            patients.add(patientId);
        }
    }


    /**
         "SELECT * FROM PROVIDER_PATIENT WHERE ..."
     */
    public static boolean IsDocPatient(String  providerId, String patientId) {
        final Set<String> patients = providerPatientMap.get(providerId);
        return patients == null ? false : patients.contains(patientId);
    }

}
