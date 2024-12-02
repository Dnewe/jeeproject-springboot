package com.project.jeeproject_springboot.util;

import com.project.jeeproject_springboot.model.Result;

import java.util.Collection;
import java.util.List;

public class MathUtil {


    public static double calculateAverageFromResults(List<Result> results) {
        double totalWeightedGrades = 0.0;
        double totalWeights = 0.0;
        for (Result result : results) {
            totalWeightedGrades += result.getGrade() / result.getMaxScore() * result.getWeight();
            totalWeights += result.getWeight();
        }
        return Math.round((totalWeights == 0.0 ? 0.0 : totalWeightedGrades / totalWeights)*20*100)/100.;
    }

    public static double calculateAverageFromDouble(Collection<Double> values) {
        double totalValues = 0.0;
        int count = 0;
        for (Double value : values) {
            totalValues += value;
            count++;
        }
        return Math.round((totalValues / count) *100)/100.;
    }
}
