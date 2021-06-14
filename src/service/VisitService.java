package service;

import model.Visit;
import store.VisitStore;

import java.util.Date;

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
    public static boolean createVisit(Visit visit) {
        return VisitStore.createVisit(visit);
    }

    /**
     *   Get a Visit for UI to present
     */
    public static Visit retrieveVisit(String docId, String patientId, Date visitTime) {

        return VisitStore.retrieveVisit(docId, patientId, visitTime);
    }

    /**
     *   Update a Visit
     *
     * @param visitId
     */
    public static void updateVisit(String visitId) {

    }
}
