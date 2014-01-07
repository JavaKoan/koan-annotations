package com.nps.koan;

import com.nps.koan.annotation.Enlighten;
import com.nps.koan.annotation.Vex;
import org.junit.Ignore;
import org.junit.runners.model.FrameworkMethod;

/**
 * Created by nicholas_smith on 03/01/14.
 */
public class KoanExecution {

    private int methodStartPosition;
    private int methodLength;

    private int startMarkerPosition;
    private int startMarkerLength;

    private int endMarkerPosition;

    private boolean isToBeEnlightened;
    private boolean isToBeVexed;
    private boolean isIgnored;

    private String classSource;
    private String solution;
    private String methodName;

    private FrameworkMethod method;

    public KoanExecution(FrameworkMethod method, String classSource) {
        this.method = method;
        this.classSource = classSource;
        this.isToBeEnlightened = method.getAnnotation(Enlighten.class) != null ? true : false;
        this.isToBeVexed = method.getAnnotation(Vex.class) != null ? true : false;
        this.isIgnored = method.getAnnotation(Ignore.class) != null || isToBeEnlightened || isToBeVexed;
    }

    public int getMethodStartPosition() {
        return methodStartPosition;
    }

    public void setMethodStartPosition(int methodStartPosition) {
        this.methodStartPosition = methodStartPosition;
    }

    public int getMethodLength() {
        return methodLength;
    }

    public void setMethodLength(int methodLength) {
        this.methodLength = methodLength;
    }

    public int getStartMarkerPosition() {
        return startMarkerPosition;
    }

    public void setStartMarkerPosition(int startMarkerPosition) {
        this.startMarkerPosition = startMarkerPosition;
    }

    public int getStartMarkerLength() {
        return startMarkerLength;
    }

    public void setStartMarkerLength(int startMarkerLength) {
        this.startMarkerLength = startMarkerLength;
    }

    public int getEndMarkerPosition() {
        return endMarkerPosition;
    }

    public void setEndMarkerPosition(int endMarkerPosition) {
        this.endMarkerPosition = endMarkerPosition;
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

    public String getClassSource() {
        return classSource;
    }

    public FrameworkMethod getMethod() {
        return method;
    }
}
