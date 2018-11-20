package com.fl.dlc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DLUtil {

    public static Double getValidOvers(String text, int team) {

        //System.out.println("Overs - " + text + " Format " + DLModel.getFormat());
        Double overs = 0.0;

        overs = getMaxOvers(team);

        if (text == null || text.trim().equals("")) {
            return overs;
        }

        try {
            Double d = Double.parseDouble(text);

            if (d > overs) {
                return null;
            }

            String[] a = text.split(".");

            if (a.length > 2) {
                String s = a[1];

                if (s.length() > 1) {
                    return null;
                }

                int frac = Integer.parseInt(a[1]);

                if (frac > 6) {
                    return null;
                }

                if (frac == 6) {
                    overs = Integer.parseInt(a[0]) + 1.0;
                }
            }

            overs = d;

            if (overs < 0) {
                return null;
            }

        } catch (Exception e) {
            return null;
        }

        return overs;

    }

    public static Integer getValidScore(String text) {

        Integer score = 0;

        try {

            score = Integer.parseInt(text);

            if (score < 0) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

        return score;
    }

    public static Integer getValidWickets(String text) {

        Integer wickets = getValidScore(text);

        if (wickets == null) {
            return null;
        } else {
            if (wickets > 10) {
                return null;
            }
        }

        return wickets;
    }

    public static int getG(int format, int type) {

        if (type == 0) return DLConstants.G50_ODI_TEST_NATIONS;
        else return DLConstants.G50_ODI_REST;
    }

    public static String calculateResult() {

        //System.out.println("Calculating result " + DLModel.getT1StartOvers() + " , " + DLModel.getT2StartOvers());

        Double r1 = getResources(DLModel.getT1StartOvers(), DLModel.getT1Suspensions(), DLModel.getFormat());
        Double r2 = getResources(DLModel.getT2StartOvers(), DLModel.getT2Suspensions(), DLModel.getFormat());

        int g = DLModel.getG();
        int s = DLModel.getT1FinalScore();

        int par_score = calculateParScore(r1, r2, g, s);

        //System.out.println("Par score is " + par_score);

        Integer t2_score = DLModel.getT2FinalScore();

        if (t2_score == null) {

            return getTargetString(DLModel.getT2Suspensions(), par_score);
        } else {
            return getResultString(t2_score, par_score);
        }
    }

    private static double getSecond_InningsResource() {
        return 100.0;
    }

    private static double getFirst_InningsResource() {
        return 100.0;
    }

    public static void showAlertDialog(Context ctx, String title, String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public static double getMaxOvers(int team) {

        double overs = 0;

        if (DLModel.getFormat() <= DLConstants.ODI || DLModel.getFormat() == DLConstants.ODD) {
            overs = DLConstants.MAX_ODI_OVERS;
        } else {
            overs = DLConstants.MAX_T20_OVERS;
        }

        if (team == DLConstants.TEAM_2) {
            overs = DLModel.getT1StartOvers();
        }

        return overs;
    }

    public static Integer getScore(int team) {

        if (team == DLConstants.TEAM_1) {
            return DLModel.getT1FinalScore();
        } else {
            return DLModel.getT2FinalScore();
        }
    }

    public static double getStartOvers(int team) {

        Double overs;

        if (team == DLConstants.TEAM_1) {
            overs = DLModel.getT1StartOvers();

            if (overs == null) {
                overs = getMaxOvers(team);
            }
        } else {
            overs = DLModel.getT2StartOvers();

            if (overs == null) {
                overs = getStartOvers(DLConstants.TEAM_1);
            }
        }

        return overs;
    }

    public static double getOverDifference(Double startOvers, Double endOvers) {

        Double startOvers1 = Math.floor(startOvers);
        Double startOvers2 = startOvers - startOvers1;

        Double endOvers1 = Math.floor(endOvers);
        Double endOvers2 = endOvers - endOvers1;

        Double diff1 = startOvers1 - endOvers1;
        Double diff2 = startOvers2 - endOvers2;

        if (diff2 < 0) {
            diff1 -= 0.4; // for eg 12 becomes 11.6
        }

        diff1 += diff2;

        return diff1;
    }

    public static List<Suspension> getValidAndNonOverlappingSuspensions(List<Suspension> suspensions, int team) {

        List<Suspension> sorted_suspensions = new ArrayList<Suspension>(suspensions);
        Collections.sort(sorted_suspensions);

        List<Suspension> nonOverlappingList = new ArrayList<>();

        int i = 1;
        Suspension currentSuspension = sorted_suspensions.get(0);

        while (i < sorted_suspensions.size()) {

            Suspension s = sorted_suspensions.get(i);

            if (currentSuspension == null) {
                currentSuspension = new Suspension(s.getScore(), s.getWickets(), s.getStartOvers(), s.getEndOvers());
            } else {
                if (currentSuspension.getEndOvers() >= s.getStartOvers()
                        || currentSuspension.getScore() > s.getScore()
                        || currentSuspension.getWickets() > s.getWickets()) {
                    return null;
                }
            }
            nonOverlappingList.add(currentSuspension);
            currentSuspension = s;
            i++;
        }

        nonOverlappingList.add(currentSuspension);

        Integer score = getScore(team);

        for (Suspension s1 : nonOverlappingList) {

            if (score != null && s1.getScore() > score) {
                return null;
            }

            if (s1.getWickets() >= 10) {
                return null;
            }

            if (s1.getEndOvers() > getStartOvers(team)) {
                return null;
            }
        }

        return nonOverlappingList;

    }

    public static Double getResources(Double start_overs, List<Suspension> suspensions, int format) {

        Double start_resource = getResourceFromDB(format, 0, start_overs);

        for (Suspension s : suspensions) {
            //System.out.println("Resources for suspensions");
            Double before_resource = getResourceFromDB(format, s.getWickets(), s.getStartOvers());
            Double after_resource = getResourceFromDB(format, s.getWickets(), s.getEndOvers());

            Double diff = before_resource - after_resource;
            start_resource -= diff;
        }

        return start_resource;
    }

    public static Double getResourceFromDB(int format, int wickets, double overs) {

        //System.out.println("Getting Resource from DB for overs " + overs + " wickets " + wickets);
        return DLDBConstants.dbhelper.getResource(format, overs, wickets);
    }

    public static int calculateParScore(Double r1, Double r2, int g, int s) {

        Double score = 0.0;
        //System.out.println("r1 is " + r1 + " , r2 is " + r2);

        if (r1 >= r2) {

            score = s * r2 / r1;
        } else {

            score = s + ((r2 - r1) * g / 100);
        }

        return score.intValue();
    }

    public static String getTargetString(List<Suspension> suspensions, int par_score) {

        String result = "";

        int runs_req, wickets_left;
        double overs_left;

        if (suspensions != null && suspensions.size() > 0) {

            Suspension last = suspensions.get(suspensions.size() - 1);
            runs_req = (par_score + 1) - last.getScore();
            wickets_left = 10 - last.getWickets();
            overs_left = last.getEndOvers();
        } else {

            runs_req = par_score + 1;
            wickets_left = 10;
            overs_left = getStartOvers(DLConstants.TEAM_2);
        }


        result = "Team " + DLConstants.TEAM_2 + " needs " +
                runs_req + " runs to win from " +
                overs_left + " overs with " +
                wickets_left + " wickets remaining.";

        return result;
    }

    public static String getResultString(int t2_score, int par_score) {

        String result = "";

        int winner = 0;

        if (t2_score == par_score) {

            return "The match is tied.";
        }

        if (t2_score > par_score) {
            winner = DLConstants.TEAM_2;
        } else {
            winner = DLConstants.TEAM_1;
        }

        int margin = Math.abs(t2_score - par_score);

        result += "Team " + winner +
                " won the match by " +
                margin + " runs.";

        return result;
    }

    public static Double getTotalOversLost(List<Suspension> suspensions) {

        Double overs_lost = 0.0;

        if (suspensions == null || suspensions.size() <= 0) {
            return overs_lost;
        }

        for (Suspension s : suspensions) {

            overs_lost += (s.getStartOvers() - s.getEndOvers());
        }

        return overs_lost;
    }
}
