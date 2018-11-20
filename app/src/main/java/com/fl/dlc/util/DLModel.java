package com.fl.dlc.util;

import java.util.List;

public class DLModel {

    private static int format;
    private static int g;
    private static Double t1StartOvers;
    private static Integer t1FinalScore;
    private static Double t2StartOvers;
    private static Integer t2FinalScore;
    private static List<Suspension> t1Suspensions;
    private static List<Suspension> t2Suspensions;

    public static int getFormat() {
        return format;
    }

    public static void setFormat(int format) {
        DLModel.format = format;
    }
    public static int getG() {
        return g;
    }

    public static void setG(int g) {
        DLModel.g = g;
    }

    public static Double getT1StartOvers() {
        return t1StartOvers;
    }

    public static void setT1StartOvers(Double t1StartOvers) {
        DLModel.t1StartOvers = t1StartOvers;
    }

    public static Integer getT1FinalScore() {
        return t1FinalScore;
    }

    public static void setT1FinalScore(Integer t1FinalScore) {
        DLModel.t1FinalScore = t1FinalScore;
    }

    public static Double getT2StartOvers() {
        return t2StartOvers;
    }

    public static void setT2StartOvers(Double t2StartOvers) {
        DLModel.t2StartOvers = t2StartOvers;
    }

    public static Integer getT2FinalScore() {
        return t2FinalScore;
    }

    public static void setT2FinalScore(Integer t2FinalScore) {
        DLModel.t2FinalScore = t2FinalScore;
    }

    public static List<Suspension> getT1Suspensions() {
        return t1Suspensions;
    }

    public static void setT1Suspensions(List<Suspension> t1Suspensions) {
        DLModel.t1Suspensions = t1Suspensions;
    }

    public static List<Suspension> getT2Suspensions() {
        return t2Suspensions;
    }

    public static void setT2Suspensions(List<Suspension> t2Suspensions) {
        DLModel.t2Suspensions = t2Suspensions;
    }
}
