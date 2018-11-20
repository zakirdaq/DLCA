package com.fl.dlc.util;

import java.io.Serializable;

public class Suspension implements Serializable, Comparable<Suspension> {

    private Integer score;
    private Integer wickets;
    private Double startOvers;
    private Double endOvers;

    public Suspension(Integer score, Integer wickets, Double startOvers, Double endOvers) {
        this.score = score;
        this.wickets = wickets;
        this.startOvers = startOvers;
        this.endOvers = endOvers;
    }

    public Suspension() {
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getWickets() {
        return wickets;
    }

    public void setWickets(Integer wickets) {
        this.wickets = wickets;
    }

    public Double getStartOvers() {
        return startOvers;
    }

    public void setStartOvers(Double startOvers) {
        this.startOvers = startOvers;
    }

    public Double getEndOvers() {
        return endOvers;
    }

    public void setEndOvers(Double endOvers) {
        this.endOvers = endOvers;
    }

    @Override
    public String toString() {
        return String.format("%.1f", DLUtil.getOverDifference(startOvers, endOvers)) + " overs lost (" + startOvers + "-" + endOvers + "), score is " + score + "/" + wickets;
    }

    @Override
    public int compareTo(Suspension another) {
        return getStartOvers().compareTo(another.getStartOvers());
    }
}
