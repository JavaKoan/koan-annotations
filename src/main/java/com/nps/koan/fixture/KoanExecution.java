package com.nps.koan.fixture;

import com.nps.koan.fixture.annotation.Enlighten;
import com.nps.koan.fixture.annotation.Vex;
import org.junit.Ignore;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by nicholas_smith on 03/01/14.
 */
public final class KoanExecution {

    private int startMarkerLine;
    //TODO: Add support for multiline start & end markers
    private int startMarkerLength;

    private int endMarkerLine;

    private boolean isToBeEnlightened;
    private boolean isToBeVexed;
    private boolean isIgnored;

    private String classSource;
    private String solution;

    private FrameworkMethod method;

    public KoanExecution(FrameworkMethod method, String classSource) {
        this.method = method;
        this.classSource = classSource;
        this.isToBeEnlightened = method.getAnnotation(Enlighten.class) != null ? true : false;
        this.isToBeVexed = method.getAnnotation(Vex.class) != null ? true : false;
        this.isIgnored = method.getAnnotation(Ignore.class) != null || isToBeEnlightened || isToBeVexed;
    }

    public int getStartMarkerLine() {
        return startMarkerLine;
    }

    public void setStartMarkerLine(int startMarkerLine) {
        this.startMarkerLine = startMarkerLine;
    }

    public int getEndMarkerLine() {
        return endMarkerLine;
    }

    public void setEndMarkerLine(int endMarkerLine) {
        this.endMarkerLine = endMarkerLine;
    }

    public boolean isToBeEnlightened() {
        return isToBeEnlightened;
    }

    public boolean isToBeVexed() {
        return isToBeVexed;
    }

    public boolean isIgnored() {
        return isIgnored;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getClassSource() {
        return classSource;
    }

    public FrameworkMethod getMethod() {
        return method;
    }
}
