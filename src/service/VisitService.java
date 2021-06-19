package service;

import model.Visit;
import security.IAMRole;
import security.Ops;
import security.Resource;
import security.Token;
import store.DoctorPatientStore;
import store.VisitStore;

import java.util.Date;
import java.util.List;

/**
 *   Visit Service
 *
 *   Web Service Endpoint Handler
 *
 */
public class VisitService {

    /**
     *   Create a Visit
     */
    public static boolean createVisit(Token token, Visit visit) {

        if (isPermitted(token, visit.getPatientId(), Ops.RETRIEVE)) {
            return VisitStore.createVisit(visit);
        }

        return false;
    }

    public static Visit retrieveVisit(Token token, int visitId) {

        final Visit visit = VisitStore.retrieveVisit(visitId);

        if (visit != null &&
            isPermitted(token, visit.getPatientId(), Ops.RETRIEVE)) {
            return visit;
        }

        return null;
    }

    /**
     *   Get Visits
     */
    public static List<Visit> retrieveVisits(Token token, String patientId) {

        if (isPermitted(token, patientId, Ops.RETRIEVE)) {
            return VisitStore.retrieveVisits(token.getUserId(), patientId);
        }

        return null;
    }

    /**
     *   Update a Visit
     *
     * @param visit
     */
    public static boolean updateVisit(Token token, Visit visit) {

        if (isPermitted(token, visit.getPatientId(), Ops.UPDATE)) {
            return VisitStore.updateVisit(visit);
        }

        return false;
    }

    /**
     *
     * @param token
     * @param patientId
     * @return
     */
    private static boolean isPermitted(Token token, String patientId, Ops ops) {

        if (!token.authorize(Resource.DOCTOR_PATIENT, ops)) {
            return false;
        }

        // patient is allowed for her own visits
        if (patientId.compareToIgnoreCase(token.getUserId()) == 0) {
            return true;
        }

        // doctor is allowed on her patient's
        return token.getIamRole().equals(IAMRole.DOCTOR) &&
                DoctorPatientStore.IsDocPatient(token.getUserId(), patientId);
    }
}
