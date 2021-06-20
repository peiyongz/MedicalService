package service;

import model.Visit;
import model.User;

import security.IAMRole;
import security.Ops;
import security.Resource;
import security.Token;
import store.DoctorPatientStore;
import store.UserStore;
import store.VisitStore;

import java.util.List;

/**
 *   Visit Service
 *
 *   Web Service Endpoint Handler
 *
 */
public class VisitService {

    /**
     *    Create a visit
     *
     * @param requesterToken
     * @param visit
     * @return
     */
    public static boolean createVisit(Token requesterToken, Visit visit) {

        if (authorize(requesterToken, visit.getPatientId(), Ops.RETRIEVE)) {
            updateUserBio(visit);
            return VisitStore.createVisit(visit);
        }

        return false;
    }

    /**
     *
     *     Retrieve a visit
     *
     * @param requesterToken
     * @param visitId
     * @return
     */
    public static Visit retrieveVisit(Token requesterToken, int visitId) {

        final Visit visit = VisitStore.retrieveVisit(visitId);

        if (visit != null &&
            authorize(requesterToken, visit.getPatientId(), Ops.RETRIEVE)) {
            return visit;
        }

        return null;
    }

    /**
     *
     *    Retrieve visits
     *
     * @param requesterToken
     * @param patientId
     * @return
     */
    public static List<Visit> retrieveVisits(Token requesterToken, String patientId) {

        if (authorize(requesterToken, patientId, Ops.RETRIEVE)) {
            return VisitStore.retrieveVisits(requesterToken.getUserId(), patientId);
        }

        return null;
    }

    /**
     *     Update visit
     *
     * @param requesterToken
     * @param visit
     * @return
     */
    public static boolean updateVisit(Token requesterToken, Visit visit) {

        if (authorize(requesterToken, visit.getPatientId(), Ops.UPDATE)) {
            updateUserBio(visit);
            return VisitStore.updateVisit(visit);
        }

        return false;
    }

    private static void updateUserBio(Visit visit) {

        final User patient = UserStore.retrieveUser(visit.getPatientId());
        patient.updateBio(visit.getBloodPressureLow(),
                          visit.getBloodPressureHigh(),
                          visit.getHeartRateInMinute(),
                          visit.getHeight(),
                          visit.getWeight());
    }

    /**
     *    Authorization
     *
     * @param requesterToken
     * @param patientId
     * @param ops
     * @return
     */
    private static boolean authorize(Token requesterToken, String patientId, Ops ops) {

        if (!requesterToken.authorize(Resource.DOCTOR_PATIENT, ops)) {
            return false;
        }

        // patient is allowed for her own visits
        if (patientId.compareToIgnoreCase(requesterToken.getUserId()) == 0) {
            return true;
        }

        // doctor is allowed on her patient's
        return requesterToken.getIamRole().equals(IAMRole.DOCTOR) &&
               DoctorPatientStore.IsDocPatient(requesterToken.getUserId(), patientId);
    }
}
