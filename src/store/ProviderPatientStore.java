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
public class ProviderPatientStore {

    // mock Doctor-Patient Table
    // key: PROVIDER
    // value: set of PATIENT
    private static Map<User, Set<User>> providerPatientMap;

    static {
        providerPatientMap = new HashMap<User, Set<User>>();
    }

    /**
        "INSERT PROVIDER_PATIENT ..."
    */
    public static void add(User provider, User patient) {


        if (!providerPatientMap.containsKey(provider)) {
            Set<User> patients = new HashSet<User>();
            patients.add(patient);
        } else {
            Set<User> patients = providerPatientMap.get(provider);
            patients.add(patient);
        }
    }


    /**
         "SELECT * FROM PROVIDER_PATIENT WHERE ..."
     */
    public static boolean IsDocPatient(User doctor, User patient) {
        final Set<User> patients = providerPatientMap.get(doctor);
        return patients == null ? false : patients.contains(patient);
    }

}
