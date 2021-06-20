package service;

import model.MedicalStudy;
import store.MedicalStudyStore;
import security.*;

import java.util.Set;

/**
 *   MedicalStudy Service
 *
 *   Web Service Endpoint Handler
 */
public class MedicalStudyService {

    public static boolean createStudy(Token requesterToken, MedicalStudy study ) {

        if (!authorize(requesterToken, Ops.CREATE)) {
            return false;
        }

        return MedicalStudyStore.createStudy(study);
    }

    public static boolean participateStudy(Token requesterToken, int studyId) {

       //every one can participate a study
        return MedicalStudyStore.participateStudy(studyId, requesterToken.getUserId());
    }

    public static Set<Integer> StudyParticipated(Token requesterToken) {

        if (!requesterToken.authorize(Resource.MEDICAL_STUDY, Ops.RETRIEVE)) {
            return null;
        }

        return MedicalStudyStore.studyParticpated(requesterToken.getUserId());
    }

    public static Set<String> partientParticiated(Token requesterToken, Integer studyId) {

        if (!requesterToken.authorize(Resource.MEDICAL_STUDY, Ops.RETRIEVE)) {
            return null;
        }

        return MedicalStudyStore.partientParticiated(studyId);
    }

    private static boolean authorize(Token requesterToken, Ops ops) {
        return requesterToken.authorize(Resource.MEDICAL_STUDY, ops);
    }
}
