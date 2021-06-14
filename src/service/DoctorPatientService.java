package service;

import store.DoctorPatientStore;
import security.*;

/**
 *   Doctor Patient Service
 *
 *   Web Service Endpoint Handler
 */
public class DoctorPatientService {

    public static boolean addPatient(Token token, String patientId) {

        if (!token.authorize(Resource.PROVIDER_PATIENT, Ops.CREATE)) {
            return false;
        }

        DoctorPatientStore.addPatient(token.getUserId(), patientId);
        return true;
    }
}
