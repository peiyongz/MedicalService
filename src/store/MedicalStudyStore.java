package store;

import model.MedicalStudy;
import model.User;
import service.UserService;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 *  An implementation of the persistent layer for MedicalStudy in lieu of RDBMS
 *
 */
public class MedicalStudyStore {

    private static int studyCount;

    private static Map<Integer, MedicalStudy> studies;
    private static Map<Integer, Set<String>> studyPatients;
    private static Map<String, Set<Integer>> patientStudies;

    static {
        studyCount = 0;

        studies = new HashMap<Integer, MedicalStudy>();
        studyPatients = new HashMap<Integer, Set<String>>();
        patientStudies = new HashMap<String, Set<Integer>>();
    }

    public static int getStudyId() {
        final int id = studyCount;
        studyCount++;
        return id;
    }

    public static boolean createStudy(MedicalStudy study) {

       if (studies.containsKey(study.getId())) {
           return false;
       }

       studies.put(study.getId(), study);
       studyPatients.put(study.getId(), new HashSet<String>());

       return true;
    }

    public static boolean participateStudy(int studyId, String patientId) {

        if (!studies.containsKey(studyId) ||
            !UserStore.isPatient(patientId)) {
            return false;
        }

        //Add the patient to the study
        Set<String> patients = studyPatients.get(studyId);
        patients.add(patientId);

        //Add the study to patient's participated studies
        final Set<Integer> patStudies = patientStudies.containsKey(patientId) ?
                                        patientStudies.get(patientId) :
                                        new HashSet<Integer>();
        patStudies.add(studyId);
        patientStudies.put(patientId, patStudies);

        return true;
    }

    public static Set<Integer> studyParticpated(String patientId) {
       return patientStudies.get(patientId);
    }

    public static Set<String> partientParticiated(Integer studyId) {
       return studyPatients.get(studyId);
    }
}
