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

    public static boolean createStudy(Token token, MedicalStudy study ) {

        if (!token.authorize(Resource.MEDICAL_STUDY, Ops.CREATE)) {
            return false;
        }

        return MedicalStudyStore.createStudy(study);
    }

    public static boolean participateStudy(Token token, int studyId) {

       //every one can participate a study
        return MedicalStudyStore.participateStudy(studyId, token.getUserId());
    }

    public static Set<Integer> StudyParticipated(Token token) {

        if (!token.authorize(Resource.MEDICAL_STUDY, Ops.RETRIEVE)) {
            return null;
        }

        return MedicalStudyStore.studyParticpated(token.getUserId());
    }

    public static Set<String> partientParticiated(Token token, Integer studyId) {

        if (!token.authorize(Resource.MEDICAL_STUDY, Ops.RETRIEVE)) {
            return null;
        }

        return MedicalStudyStore.partientParticiated(studyId);
    }
}
