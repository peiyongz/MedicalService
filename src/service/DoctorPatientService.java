package service;

import store.DoctorPatientStore;
import security.*;
import store.UserStore;

/**
 *   Doctor Patient Service
 *
 *   Web Service Endpoint Handler
 */
public class DoctorPatientService {

    public static boolean addPatient(Token token, String patientId) {

        if (!token.authorize(Resource.DOCTOR_PATIENT, Ops.CREATE) ||
            !UserStore.isPatient(patientId)) {
            return false;
        }

        DoctorPatientStore.addPatient(token.getUserId(), patientId);
        return true;
    }
}
