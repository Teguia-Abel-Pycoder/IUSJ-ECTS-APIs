package iusj.ECTS.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PercentileService {
    private static double calculatePercentile(ArrayList<Double> myList, double percentile) {
        if (myList == null || myList.isEmpty()) {
            throw new IllegalArgumentException("List cannot be null or empty.");
        }
        // Sort the list in ascending order
        Collections.sort(myList);

        // Calculate the position for the given percentile
        double position = (myList.size() - 1) * percentile;
        int lowerIndex = (int) Math.floor(position);
        int upperIndex = (int) Math.ceil(position);

        // If the position is an exact match, return that value
        if (lowerIndex == upperIndex) {
            return myList.get(lowerIndex);
        }

        // Otherwise, return a weighted average between the lower and upper values
        double weight = position - lowerIndex;
        return myList.get(lowerIndex) * (1 - weight) + myList.get(upperIndex) * weight;
    }

    public static int mgpPercentile(ArrayList<Double> values, double studentMgp) {

        ArrayList<Double> deciles = new ArrayList<>();

        // Compute 9 deciles (indices 0 to 8)
        for (int i = 0; i < 9; i++) {
            double k = (i + 1) / 10.0;  // 0.1, 0.2, ..., 0.9
            double percentileValue = calculatePercentile(values, k);
            deciles.add(percentileValue);
        }

        if (studentMgp >= deciles.get(0)) {
            return 0;
        } else if (studentMgp >= deciles.get(1)) {
            return 1;
        } else if (studentMgp >= deciles.get(2)) {
            return 2;
        } else if (studentMgp >= deciles.get(3)) {
            return 3;
        } else if (studentMgp >= deciles.get(4)) {
            return 4;
        } else if (studentMgp >= deciles.get(5)) {
            return 5;
        } else if (studentMgp >= deciles.get(6)) {
            return 6;
        } else if (studentMgp >= deciles.get(7)) {
            return 7;
        } else if (studentMgp >= deciles.get(8)) {
            return 8;
        } else {
            return 9; // If below all deciles, assign the lowest percentile
        }
    }

    public static Double marksPercentile(ArrayList<Double> values, int mgpIndex) {
        ArrayList<Double> deciles = new ArrayList<>();

        // Compute 10 deciles (0.1 to 1.0)
        for (int i = 0; i < 10; i++) {  // Change from 9 to 10
            double k = (i + 1) / 10.0;  // 0.1, 0.2, ..., 1.0
            double percentileValue = calculatePercentile(values, k);
            deciles.add(percentileValue);
        }

        Collections.sort(deciles, Collections.reverseOrder());
        deciles.forEach(decile -> {
            System.out.println("\nDecile: " + decile);
        });
        // Ensure mgpIndex is within bounds (0 to 9)
        if (mgpIndex < 0 || mgpIndex >= deciles.size()) {
            throw new IndexOutOfBoundsException("mgpIndex must be between 0 and 9. Given: " + mgpIndex);
        }

        return deciles.get(mgpIndex);
    }

    public Map<String, ArrayList<Double>> myPercentile(ArrayList<Double> myList) {
        Map<String, ArrayList<Double>> gradedMarks = new HashMap<>();
        gradedMarks.put("A", new ArrayList<>());
        gradedMarks.put("B", new ArrayList<>());
        gradedMarks.put("C", new ArrayList<>());
        gradedMarks.put("D", new ArrayList<>());
        gradedMarks.put("E", new ArrayList<>());

        // Sort the list in descending order
        myList.sort(Comparator.reverseOrder());

        // Calculate the indices for dividing the list
        int totalSize = myList.size();
        int A_end = (int) Math.floor(totalSize * 0.1);  // 10%
        int B_end = A_end + (int) Math.round(totalSize * 0.25);  // 25%
        int C_end = B_end + (int) Math.round(totalSize * 0.3);  // 30%
        int D_end = C_end + (int) Math.round(totalSize * 0.25);  // 25%

        // Assign marks to categorize
        for (int i = 0; i < A_end; i++) {
            gradedMarks.get("A").add(myList.get(i));
        }
        for (int i = A_end; i < B_end; i++) {
            gradedMarks.get("B").add(myList.get(i));
        }
        for (int i = B_end; i < C_end; i++) {
            gradedMarks.get("C").add(myList.get(i));
        }
        for (int i = C_end; i < D_end; i++) {
            gradedMarks.get("D").add(myList.get(i));
        }
        for (int i = D_end; i < totalSize; i++) {
            gradedMarks.get("E").add(myList.get(i));
        }

        return gradedMarks;
    }
    public double gradePercentileComparism(String courseName, String grade, Map<String, ArrayList<Double>> gradedMarks, double studentMgp, ArrayList<Double> mgps){
        return (double) Math.round(marksPercentile(gradedMarks.get(grade), mgpPercentile(mgps, studentMgp)) * 100) /100;
    }


}
