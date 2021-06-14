import model.Gender;
import model.User;
import security.IAMRole;
import security.IAMService;
import service.UserService;
import security.Token;

import java.util.Date;
import static org.junit.Assert.*;

/**
 * This mimics the UI layer, or the interaction between the client and Frontend Service
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        testCreatePatient();
        testCreateProvider();
        testLogin();
    }


    /**
     *  Patient
     *
     *     To create patients
     *
     *     Login
     *
     *     Retrieve own particular
     *     Update own particular
     *
     *     Retrieve others
     *     Update others
     *
     *  Provider
     *
     *     To create providers
     *
     *     Login
     *     View own particular
     *     Update particular
     *
     *     Add patient
     *
     *     --owned patient
     *     Add Visit
     *     View Visit
     *     Update Visit
     *
     *     --none-owned patient
     *     Add Visit
     *     View Visit
     *     Update Visit
     *
     *  Login as providers
     *
     *
     */

    private static Token tokenPatientOne;
    private static User userPatientOne;
    private static final String userPatientOneId = "patientOne";
    private static final String userPatientOnePW = "patOnePass";

    private static Token tokenPatientTwo;
    private static User userPatientTwo;
    private static final String userPatientTwoId = "patientTwo";
    private static final String userPatientTwoPW = "patTwoPass";

    private static Token tokenDoctorOne;
    private static User userDoctorOne;
    private static final String userDoctorOneId = "doctorOne";
    private static final String userDoctorOnePW = "docOnePass";

    private static Token tokenDoctorTwo;
    private static User userDoctorTwo;
    private static final String userDoctorTwoId = "doctorTwo";
    private static final String userDoctorTwoPW = "docTwoPass";

    public static void testCreatePatient() {

        System.out.println("testCreatePatient");

        userPatientOne = new User(userPatientOneId, "Patient", "One" , IAMRole.PATIENT,
                Gender.MALE, new Date(1992, 1, 1), "206-456-1111", "patientone@yahoo.com",
                "1110 234TH PL SE Bellevue WA 98006");
        //Allow create once
        assertTrue(UserService.createUser(userPatientOne, userPatientOnePW));
        assertFalse(UserService.createUser(userPatientOne, userPatientOnePW));

        userPatientTwo = new User("patientTwo", "Patient", "Two", IAMRole.PATIENT,
                Gender.MALE, new Date(1993, 1, 1), "206-456-2222", "patientTwo@yahoo.com",
                "1110 234TH PL SE Bellevue WA 98006");
        //Allow create once
        assertTrue(UserService.createUser(userPatientTwo, userPatientTwoPW));
        assertFalse(UserService.createUser(userPatientTwo, userPatientTwoPW));
    }

     public static void testCreateProvider() {
        System.out.println("testCreateProvider");

        userDoctorOne = new User(userDoctorOneId, "Doctor", "One", IAMRole.DOCTOR,
                                  Gender.MALE, new Date(1991, 1, 1),"206-123-1111", "doctorOne@yahoo.com",
                   "1110 234TH PL SE Bellevue WA 98006");
         //Allow create once
        assertTrue(UserService.createUser(userDoctorOne, userDoctorOnePW));
        assertFalse(UserService.createUser(userDoctorOne, userDoctorOnePW));

        userDoctorTwo = new User(userDoctorTwoId,"Doctor", "Two", IAMRole.DOCTOR,
                Gender.MALE, new Date(1991,1,2), "206-123-2222", "doctorTwo@yahoo.com",
                "1110 234TH PL SE Bellevue WA 98006");
         //Allow create once
        assertTrue(UserService.createUser(userDoctorTwo, userDoctorTwoPW));
        assertFalse(UserService.createUser(userDoctorTwo, userDoctorTwoPW ));
     }

     public static void testLogin() {

        System.out.println("testLogin");

        tokenPatientOne = IAMService.login(userPatientOneId, userPatientOnePW);
        assertNotNull(tokenPatientOne);
         assertNull(IAMService.login("noone", "nopasswd"));
         assertNull(IAMService.login(userPatientOneId, "wrongpasswd"));

         tokenPatientTwo = IAMService.login(userPatientTwoId, userPatientTwoPW);
         assertNotNull(tokenPatientTwo);

         tokenDoctorOne= IAMService.login(userDoctorOneId, userDoctorOnePW);
         assertNotNull(tokenDoctorOne);

         tokenDoctorTwo= IAMService.login(userDoctorTwoId, userDoctorTwoPW);
         assertNotNull(tokenDoctorTwo);
     }


}
