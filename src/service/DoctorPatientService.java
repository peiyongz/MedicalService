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

    /**
     *    Add a patient
     *
     * @param requesterToken
     * @param patientId
     * @return
     */
    public static boolean addPatient(Token requesterToken, String patientId) {

        if (!authorize(requesterToken, patientId, Ops.CREATE)) {
            return false;
        }

        DoctorPatientStore.addPatient(requesterToken.getUserId(), patientId);
        return true;
    }

    /**
     *
     *    Authorization
     *
     * @param requesterToken
     * @param patientId
     * @param ops
     * @return
     */
    private static boolean authorize(Token requesterToken, String patientId, Ops ops) {
        return requesterToken.authorize(Resource.DOCTOR_PATIENT, ops) &&
               UserStore.isPatient(patientId);

    }
}
