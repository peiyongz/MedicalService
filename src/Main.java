import model.*;
import security.*;
import service.*;

import java.util.Date;
import static org.junit.Assert.*;

/**
 * This mimics the UI layer, or the interaction between the client and Frontend Service
 */
public class Main {

    public static void main(String[] args) {
        testCreatePatient();
        testCreateProvider();
        testLogin();
        testAddPatient();
        testRetrieveUpdatePatient();
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

    private static Token tokenPatient1;
    private static User userPatient1;
    private static final String userPatientId1 = "patientOne";
    private static final String userPatientPW1 = "patOnePass";

    private static Token tokenPatient2;
    private static User userPatient2;
    private static final String userPatientId2 = "patientTwo";
    private static final String userPatientPW2 = "patTwoPass";

    private static Token tokenDoctor1;
    private static User userDoctor1;
    private static final String userDoctorId1 = "doctorOne";
    private static final String userDoctorPW1 = "docOnePass";

    private static Token tokenDoctor2;
    private static User userDoctor2;
    private static final String userDoctorId2 = "doctorTwo";
    private static final String userDoctorPW2 = "docTwoPass";

    public static void testCreatePatient() {

        System.out.println("testCreatePatient");

        userPatient1 = new User(userPatientId1, "Patient", "One" , IAMRole.PATIENT,
                Gender.MALE, new Date(1992, 1, 1), "206-456-1111", "patientone@yahoo.com",
                "1110 234TH PL SE Bellevue WA 98006");
        //Allow create once
        assertTrue(UserService.createUser(userPatient1, userPatientPW1));
        assertFalse(UserService.createUser(userPatient1, userPatientPW1));

        userPatient2 = new User("patientTwo", "Patient", "Two", IAMRole.PATIENT,
                Gender.MALE, new Date(1993, 1, 1), "206-456-2222", "patientTwo@yahoo.com",
                "1110 234TH PL SE Bellevue WA 98006");
        //Allow create once
        assertTrue(UserService.createUser(userPatient2, userPatientPW2));
        assertFalse(UserService.createUser(userPatient2, userPatientPW2));
    }

     public static void testCreateProvider() {
        System.out.println("testCreateProvider");

        userDoctor1 = new User(userDoctorId1, "Doctor", "One", IAMRole.DOCTOR,
                                  Gender.MALE, new Date(1991, 1, 1),"206-123-1111", "doctorOne@yahoo.com",
                   "1110 234TH PL SE Bellevue WA 98006");
         //Allow create once
        assertTrue(UserService.createUser(userDoctor1, userDoctorPW1));
        assertFalse(UserService.createUser(userDoctor1, userDoctorPW1));

        userDoctor2 = new User(userDoctorId2,"Doctor", "Two", IAMRole.DOCTOR,
                Gender.MALE, new Date(1991,1,2), "206-123-2222", "doctorTwo@yahoo.com",
                "1110 234TH PL SE Bellevue WA 98006");
         //Allow create once
        assertTrue(UserService.createUser(userDoctor2, userDoctorPW2));
        assertFalse(UserService.createUser(userDoctor2, userDoctorPW2));
     }

     public static void testLogin() {

        System.out.println("testLogin");

        tokenPatient1 = IAMService.login(userPatientId1, userPatientPW1);
        assertNotNull(tokenPatient1);
         assertNull(IAMService.login("noone", "nopasswd"));
         assertNull(IAMService.login(userPatientId1, "wrongpasswd"));

         tokenPatient2 = IAMService.login(userPatientId2, userPatientPW2);
         assertNotNull(tokenPatient2);

         tokenDoctor1 = IAMService.login(userDoctorId1, userDoctorPW1);
         assertNotNull(tokenDoctor1);

         tokenDoctor2 = IAMService.login(userDoctorId2, userDoctorPW2);
         assertNotNull(tokenDoctor2);
     }

     public static void testAddPatient() {

         System.out.println("testAddPatient");

         //doc1 can NOT view patient1 profile
         //but can after adding patient1 as her patient
         //but still can NOT view patient2's since not her patient
         assertNull(UserService.retrieveUser(tokenDoctor1, userPatientId1));
         assertTrue(DoctorPatientService.addPatient(tokenDoctor1, userPatientId1));
         assertNotNull(UserService.retrieveUser(tokenDoctor1, userPatientId1));
         assertNull(UserService.retrieveUser(tokenDoctor1, userPatientId2));

         //doc2 can NOT view patient2 profile
         //but can after adding patient2 as her patient
         //but still can NOT view patient1's since not her patient
         assertNull(UserService.retrieveUser(tokenDoctor2, userPatientId2));
         assertTrue(DoctorPatientService.addPatient(tokenDoctor2, userPatientId2));
         assertNotNull(UserService.retrieveUser(tokenDoctor2, userPatientId2));
         assertNull(UserService.retrieveUser(tokenDoctor2, userPatientId1));
     }

     public static void testRetrieveUpdatePatient() {
         System.out.println("testRetrieveUpdatePatient");


     }

}
