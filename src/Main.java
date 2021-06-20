import model.*;
import security.*;
import service.*;
import store.AuditStore;

import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

/**
 * This mimics the UI layer, or the interaction between the client and Frontend Service
 */
public class Main {

    public static void main(String[] args) {
        runCreatePatient();
        runCreateDoctor();
        runLogin();
        runAddPatient();
        runPatientViewUpdate();
        runCreateVisit();
        runCheckAudit();
        runMedicalStudy();
        runHealthReport();
    }


    /**
     *
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
     *  Doctor
     *
     *     To create doctor
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

    private static Token tokenPatient3;
    private static User userPatient3;
    private static final String userPatientId3 = "patientThree";
    private static final String userPatientPW3 = "patThreePass";

    private static Token tokenDoctor1;
    private static User userDoctor1;
    private static final String userDoctorId1 = "doctorOne";
    private static final String userDoctorPW1 = "docOnePass";

    private static Token tokenDoctor2;
    private static User userDoctor2;
    private static final String userDoctorId2 = "doctorTwo";
    private static final String userDoctorPW2 = "docTwoPass";

    private static Token tokenDoctor3;
    private static User userDoctor3;
    private static final String userDoctorId3 = "doctorThree";
    private static final String userDoctorPW3 = "docThreePass";

    /**
     *  Patient can create her own account
     */
    public static void runCreatePatient() {

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

        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
    }

    /**
     *  Doctor can create her own account
     */
     public static void runCreateDoctor() {

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

        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
     }

     public static void runLogin() {

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

         System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
     }

     public static void runAddPatient() {

         //doc1 can NOT view patient1 profile
         //but can after adding patient1 as her patient
         //but still can NOT view patient2's since not her patient
         assertNull(UserService.retrieveUser(tokenDoctor1, userPatientId1));

         int auditCount = AuditStore.count();
         assertTrue(DoctorPatientService.addPatient(tokenDoctor1, userPatientId1));
         // add Doc-Patient has audit log
         assertEquals(auditCount+1, AuditStore.count());

         assertNotNull(UserService.retrieveUser(tokenDoctor1, userPatientId1));
         assertNull(UserService.retrieveUser(tokenDoctor1, userPatientId2));

         //doc2 can NOT view patient2 profile
         //but can after adding patient2 as her patient
         //but still can NOT view patient1's since not her patient
         assertNull(UserService.retrieveUser(tokenDoctor2, userPatientId2));
         assertTrue(DoctorPatientService.addPatient(tokenDoctor2, userPatientId2));
         assertNotNull(UserService.retrieveUser(tokenDoctor2, userPatientId2));
         assertNull(UserService.retrieveUser(tokenDoctor2, userPatientId1));

         System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
     }

     public static void runPatientViewUpdate() {

         //patient can view her own profile
         User patient1 = UserService.retrieveUser(tokenPatient1, userPatientId1);
         assertNotNull(patient1);
         //user can update her own profile
         assertTrue(UserService.updateUser(tokenPatient1, patient1, userPatientPW1));

         //patient can view her own visit

         // user can't view others profile
         User patient2 = UserService.retrieveUser(tokenPatient1, userPatientId2);
         assertNull(patient2);

         System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
     }

     public static void runCreateVisit() {

         // patient1 is doc1's patient
         // doc1 can create visit for patient1
         // doc1 can retrieve patient1's visits with her

         int auditCount = AuditStore.count();
         final Visit visit1 = new Visit(userDoctorId1, userPatientId1, new Date(), "diagnostics 1", "treatment 1",
                 70 ,120, 60, 72, 150);
         final int doc1patient1visit1Id = visit1.getId();
         assertTrue(VisitService.createVisit(tokenDoctor1, visit1));
         //create visit has audit log
         assertEquals(auditCount + 1, AuditStore.count());

         auditCount = AuditStore.count();
         final Visit visit2 = new Visit(userDoctorId1, userPatientId1, new Date(), "diagnostics 2", "treatment 2",
                 72, 122, 62, 71, 152);
         final int doc1patient1visit2Id = visit2.getId();
         assertTrue(VisitService.createVisit(tokenDoctor1, visit2));
         //create visit has audit log
         assertEquals(auditCount + 1, AuditStore.count());

         auditCount = AuditStore.count();
         final Visit visit1Retrieved = VisitService.retrieveVisit(tokenDoctor1, doc1patient1visit1Id);
         assertNotNull(visit1Retrieved);
         assertEquals(doc1patient1visit1Id, visit1Retrieved.getId());

         //retrieve visit has no audit log
         assertEquals(auditCount, AuditStore.count());

         auditCount = AuditStore.count();
         assertTrue(VisitService.updateVisit(tokenDoctor1, visit1Retrieved));
         //update visit has audit log
         assertEquals(auditCount + 1, AuditStore.count());


         auditCount = AuditStore.count();
         final Visit visit2Retrieved = VisitService.retrieveVisit(tokenDoctor1, doc1patient1visit2Id);
         assertNotNull(visit2Retrieved);
         assertEquals(doc1patient1visit2Id, visit2Retrieved.getId());
         //retrieve visit has no audit log
         assertEquals(auditCount, AuditStore.count());

         auditCount = AuditStore.count();
         final List<Visit> visits = VisitService.retrieveVisits(tokenDoctor1, userPatientId1);
         assertNotNull(visits);
         assertEquals(2, visits.size());
         //retrieve visit has no audit log
         assertEquals(auditCount, AuditStore.count());

         //patient2 is NOT doc1's patient
         // doc1 can NOT create visit for patient2
         // doc1 can NOT retrieve
         final Visit visit3 = new Visit(userDoctorId1, userPatientId2, new Date(), "diagnostics 1", "treatment 1",
                 80, 120, 60, 12 * 6, 150);
         final int doc1patient2visit3Id = visit3.getId();
         assertFalse(VisitService.createVisit(tokenDoctor1, visit3));
         assertNull(VisitService.retrieveVisits(tokenDoctor1, userPatientId2));

         //patient1 is NOT doc2's patient
         //doc2 can NOT retrieve patient1' visit
         assertNull(VisitService.retrieveVisits(tokenDoctor2, userPatientId1));

         System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
     }

    public static void runCheckAudit() {

        int auditCount = AuditStore.count();

        //createUser has audit log
        userPatient3 = new User(userPatientId3, "Patient", "Three" , IAMRole.PATIENT,
                Gender.MALE, new Date(1994, 1, 1), "206-456-3333", "patientthree@yahoo.com",
                "1144 234TH PL SE Bellevue WA 98006");
        assertTrue(UserService.createUser(userPatient3, userPatientPW3));
        assertEquals(auditCount+1, AuditStore.count());

        userDoctor3 = new User(userDoctorId3, "Doctor", "Three", IAMRole.DOCTOR,
                Gender.MALE, new Date(1991, 1, 1),"206-123-333", "doctorThree@yahoo.com",
                "1110 444TH PL SE Bellevue WA 98006");
        auditCount = AuditStore.count();
        assertTrue(UserService.createUser(userDoctor3, userDoctorPW3));
        assertEquals(auditCount+1, AuditStore.count());

        //retrieve user has no audit log
        auditCount = AuditStore.count();
        tokenPatient3 = IAMService.login(userPatientId3, userPatientPW3);
        assertNotNull(tokenPatient3);
        UserService.retrieveUser(tokenPatient3, userPatientId3);
        assertEquals(auditCount, AuditStore.count());

        auditCount = AuditStore.count();
        tokenDoctor3 = IAMService.login(userDoctorId3, userDoctorPW3);
        assertNotNull(tokenDoctor3);
        UserService.retrieveUser(tokenDoctor3, userDoctorId3);
        assertEquals(auditCount, AuditStore.count());

        //update user has audit log
        auditCount = AuditStore.count();
        UserService.updateUser(tokenPatient3, userPatient3, "new pass");
        assertEquals(auditCount+1, AuditStore.count());

        auditCount = AuditStore.count();
        UserService.updateUser(tokenDoctor3, userDoctor3, "new pass");
        assertEquals(auditCount+1, AuditStore.count());

        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
    }

    public static void runMedicalStudy() {

        //doctor can create a new medical study
        final MedicalStudy ms1 = new MedicalStudy("md1", "md1 desc", new Date(), new Date());
        assertTrue(MedicalStudyService.createStudy(tokenDoctor1, ms1));

        //patient can not create medical study
        final MedicalStudy ms2 = new MedicalStudy("md2", "md2 desc", new Date(), new Date());
        assertFalse(MedicalStudyService.createStudy(tokenPatient1, ms2));
        assertTrue(MedicalStudyService.createStudy(tokenDoctor2, ms2));

        //patient can participate multiple studies
        //
        assertTrue(MedicalStudyService.participateStudy(tokenPatient1, ms1.getId()));
        assertTrue(MedicalStudyService.participateStudy(tokenPatient1, ms2.getId()));
        assertEquals(2, MedicalStudyService.StudyParticipated(tokenPatient1).size());
        assertNull( MedicalStudyService.StudyParticipated(tokenPatient2));
        assertNull( MedicalStudyService.StudyParticipated(tokenPatient3));

        // study can accept multiple patients
        assertTrue(MedicalStudyService.participateStudy(tokenPatient2, ms1.getId()));
        assertTrue(MedicalStudyService.participateStudy(tokenPatient3, ms1.getId()));
        assertEquals(1, MedicalStudyService.StudyParticipated(tokenPatient2).size());
        assertEquals(1, MedicalStudyService.StudyParticipated(tokenPatient3).size());

        assertEquals(3, MedicalStudyService.partientParticiated(tokenPatient1, ms1.getId()).size());
        assertEquals(1, MedicalStudyService.partientParticiated(tokenPatient1, ms2.getId()).size());

        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");

    }

    public static void runHealthReport() {

        VisitService.createVisit(tokenDoctor1, new Visit(userDoctorId1, userPatientId1, new Date(),
                                              "diagnostics 3", "treatment 3",
                                                     74, 124, 64, 71, 154));
        VisitService.createVisit(tokenDoctor1, new Visit(userDoctorId1, userPatientId1, new Date(),
                                               "diagnostics 4", "treatment 4",
                                                     76, 126, 66, 71, 156));

        VisitService.createVisit(tokenDoctor1, new Visit(userDoctorId1, userPatientId1, new Date(),
                                              "diagnostics 5", "treatment 5",
                                                     78, 128, 68, 71, 158));

        System.out.println(ReportService.healthReport(tokenDoctor1, userPatientId1, new Date()));

        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + " passed successfully");
    }
}
