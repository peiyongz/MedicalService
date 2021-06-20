package store;

import security.Permission;
import security.Resource;
import security.Token;
import security.IAMRole;
import model.User;

import java.util.Map;
import java.util.HashMap;

/**
 *  An implementation of the persistent layer for IAM in lieu of RDBMS
 *
 */
public class IAMStore {

    private static Map<IAMRole, Map<Resource, Permission>> roleResourcePermMap;

    /**
     *    Resource                  Role         Create   Retrieve   Update   Delete
     *    ===========================================================================
     *    User                      Doctor        1          1         1        1
     *    User                      Patient       1          1         1        1
     *
     *    Visit                     Doctor        1          1         1        1
     *    Visit                     Patient       0          1         0        0
     *
     *    Doctor_Patient            Doctor        1          1         1        1
     *    Doctor_Patient            Patient       0          0         0        0
     *
     *    Medical_Study             Doctor        1          1         1        1
     *    Medical_Study             Patient       0          1         0        0
     *
     *    Audit                     Doctor        0          1         0        0
     *    Audit                     Patient       0          1         0        0
     *
     *    Report                    Doctor        1          0         0        0
     *    Report                    Patient       0          0         0        0
     *
     */

    static {
        roleResourcePermMap = new HashMap<IAMRole, Map<Resource, Permission>>();

        Map<Resource, Permission> DoctorMap = new HashMap<Resource, Permission>();
        roleResourcePermMap.put(IAMRole.DOCTOR, DoctorMap);
        Map<Resource, Permission> patientMap = new HashMap<Resource, Permission>();
        roleResourcePermMap.put(IAMRole.PATIENT, patientMap);

        DoctorMap.put(Resource.USER, Permission.PERMISSION_ALL);
        patientMap.put(Resource.USER, Permission.PERMISSION_ALL);

        DoctorMap.put(Resource.VISIT, Permission.PERMISSION_ALL);
        patientMap.put(Resource.VISIT, Permission.PERMISSION_RETRIEVE_ONLY);

        DoctorMap.put(Resource.DOCTOR_PATIENT, Permission.PERMISSION_ALL);
        patientMap.put(Resource.DOCTOR_PATIENT, Permission.PERMISSION_NONE);

        DoctorMap.put(Resource.MEDICAL_STUDY, Permission.PERMISSION_ALL);
        patientMap.put(Resource.MEDICAL_STUDY, Permission.PERMISSION_RETRIEVE_ONLY);

        DoctorMap.put(Resource.AUDIT, Permission.PERMISSION_RETRIEVE_ONLY);
        patientMap.put(Resource.AUDIT, Permission.PERMISSION_RETRIEVE_ONLY);

        DoctorMap.put(Resource.REPORT, Permission.PERMISSION_ALL);
        patientMap.put(Resource.REPORT, Permission.PERMISSION_NONE);
    }

    public static Token getToken(String userId) {

        final User user = UserStore.retrieveUser(userId);
        if (user == null) {
            return null;
        }

        return new Token(userId, user.getIamRole(), roleResourcePermMap.get(user.getIamRole()));
    }
}
