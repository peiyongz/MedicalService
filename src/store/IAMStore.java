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
     *    User                      Provider      1          1         1        1
     *    User                      Patient       1          1         1        1
     *
     *    Visit                     Provider      1          1         1        1
     *    Visit                     Patient       0          1         0        0
     *
     *    Provider_Patient          Provider      1          1         1        1
     *    Provider_Patient          Patient       0          0         0        0
     *
     *    Medical_Study             Provider      1          1         1        1
     *    Medical_Study             Patient       0          0         0        0
     *
     *    Audit                     Provider      0          1         0        0
     *    Audit                     Patient       0          1         0        0
     */

    static {
        roleResourcePermMap = new HashMap<IAMRole, Map<Resource, Permission>>();

        Map<Resource, Permission> providerMap = new HashMap<Resource, Permission>();
        roleResourcePermMap.put(IAMRole.DOCTOR, providerMap);
        Map<Resource, Permission> patientMap = new HashMap<Resource, Permission>();
        roleResourcePermMap.put(IAMRole.PATIENT, patientMap);

        providerMap.put(Resource.USER, Permission.PERMISSION_ALL);
        patientMap.put(Resource.USER, Permission.PERMISSION_ALL);

        providerMap.put(Resource.VISIT, Permission.PERMISSION_ALL);
        patientMap.put(Resource.VISIT, Permission.PERMISSION_RETRIEVE_ONLY);

        providerMap.put(Resource.PROVIDER_PATIENT, Permission.PERMISSION_ALL);
        patientMap.put(Resource.PROVIDER_PATIENT, Permission.PERMISSION_NONE);

        providerMap.put(Resource.MEDICAL_STUDY, Permission.PERMISSION_ALL);
        patientMap.put(Resource.MEDICAL_STUDY, Permission.PERMISSION_NONE);

        providerMap.put(Resource.AUDIT, Permission.PERMISSION_RETRIEVE_ONLY);
        patientMap.put(Resource.AUDIT, Permission.PERMISSION_RETRIEVE_ONLY);

    }

    public static Token getToken(String userId) {

        final User user = UserStore.retrieveUser(userId);
        if (user == null) {
            return null;
        }

        return new Token(userId, user.getIamRole(), roleResourcePermMap.get(user.getIamRole()));
    }
}
