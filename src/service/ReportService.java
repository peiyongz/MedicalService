package service;

import model.*;

import security.*;
import store.DoctorPatientStore;
import store.UserStore;
import store.VisitStore;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 *   Report Service
 *
 *   Web Service Endpoint Handler
 */
public class ReportService {

    /**
     *
     *    patient health report
     *
     * @param requesterToken
     * @param patientId
     * @param date
     * @return
     */
    public static String healthReport(Token requesterToken, String patientId, Date date) {

        if (!authorize(requesterToken, patientId, Ops.CREATE)) {
            return "";
        }

        final StringBuffer sb = new StringBuffer();

        // grab User profile
        // name, birthday, gender, height and weight
        //
        // calculate BMI

        final User patient = UserStore.retrieveUser(patientId);
        sb.append("\n\n\n Health Summary: \n==========================\n");
        sb.append("Patient: ");
        sb.append(patient.getFirstName());
        sb.append(" ");
        sb.append(patient.getLastName());
        sb.append("\n");
        sb.append("Birthday: ");
        sb.append(patient.getBirthday());
        sb.append("\n");
        sb.append("Gender: ");
        sb.append(patient.getGender());
        sb.append("\n");
        sb.append("Height: ");
        sb.append(patient.getHeight());
        sb.append("\n");
        sb.append("Weight: ");
        sb.append(patient.getWeight());
        sb.append("\n");

        // grab Doctor-Patient
        // Primary doctor's name and phone
        final User doctor = DoctorPatientStore.getDoctor(patientId);
        sb.append("\nPhysician: ");
        sb.append(doctor.getFirstName());
        sb.append(" ");
        sb.append(doctor.getLastName());
        sb.append("\nPhone: ");
        sb.append(doctor.getPhone());
        sb.append("\n");

        // grab Visit for the past 12 months
        // calculate
        // Min, mean, median and max on
        //   blood pressure, heart rate
        final List<Visit>   visits = VisitStore.retrieveVisits(doctor.getUserId(), patientId);
        final List<Integer> bloodPressureLowList = new ArrayList<Integer>();
        final List<Integer> bloodPressureHighList = new ArrayList<Integer>();
        final List<Integer> heartRateList = new ArrayList<Integer>();
        final List<Float>   BMIList = new ArrayList<Float>();

        sb.append("\n\nVisits \n");
        for (Visit v: visits) {
            bloodPressureLowList.add(v.getBloodPressureLow());
            bloodPressureHighList.add(v.getBloodPressureHigh());
            heartRateList.add(v.getHeartRateInMinute());

            //print visit
            //TODO: to filter out visit of 12-month ago
            sb.append(v.toString());
            sb.append("\n\n");

            //Formula: 703 x weight (lbs) / [height (in)]2
            BMIList.add((float)703.0 * v.getWeight() /(v.getHeight() * v.getWeight()));
        }

        sb.append("\n\nStats \n\n");
        printStats(getStats(bloodPressureLowList), sb,"Blood Pressure Low " );
        printStats(getStats(bloodPressureHighList), sb,"Blood Pressure High " );
        printStats(getStats(heartRateList), sb,"Heart Rate " );

        sb.append("\n\nBMI \n\n");
        for (float f : BMIList) {
            sb.append("\n ");
            sb.append(f);
        }

        sb.append("\n");
        return sb.toString();
    }

    // sort
    //  return list
    //  0:  min
    //  1:  max
    //  2:  mean
    //  3:  median
    private static List<Float> getStats(List<Integer> numbers) {

        final List<Float> stats = new ArrayList<Float>(4);

        if (numbers.size() == 0) {
            return stats;
        }

        //Sort data in ascending order
        Collections.sort(numbers);

        //min
        stats.add(0, numbers.get(0).floatValue());

        //max
        stats.add(1, numbers.get(numbers.size()-1).floatValue());

        //mean
        float sum = 0;
        for (int n : numbers) {
            sum+=n;
        }
        stats.add(2, sum/numbers.size());

        //median
        final int mid1 = numbers.size()/2 - 1;
        final int mid2 = numbers.size()/2;
        if (numbers.size()%2 == 0) {
            stats.add(3, (float) (numbers.get(mid1) + numbers.get(mid2))/2);
        } else {
            stats.add( 3, (float) numbers.get(mid1));
        }

        return stats;
    }

    private static void printStats(List<Float> stats, StringBuffer sb, String msg) {
        sb.append(msg);
        sb.append("Min: ");
        sb.append(stats.get(0));
        sb.append(" Max: ");
        sb.append(stats.get(1));
        sb.append(" Mean: ");
        sb.append(stats.get(2));
        sb.append(" Median: ");
        sb.append(stats.get(3));
        sb.append("\n");
    }

    /**
     *
     *    Authorization
     *
     * @param requesterToken
     * @param patientId
     * @param ops
     * @return
     */
    private static boolean authorize(Token requesterToken, String patientId, Ops ops) {

        if (!requesterToken.authorize(Resource.REPORT, ops)) {
            return false;
        }

        // doctor is allowed on her patient's
        return requesterToken.getIamRole().equals(IAMRole.DOCTOR) &&
               DoctorPatientStore.IsDocPatient(requesterToken.getUserId(), patientId);
    }

}
