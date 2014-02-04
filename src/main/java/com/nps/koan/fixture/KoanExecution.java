/**
 * Copyright 2014 Nicholas Smith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nps.koan.fixture;

import com.nps.koan.fixture.annotation.Enlighten;
import com.nps.koan.fixture.annotation.Vex;
import org.junit.Ignore;
import org.junit.runners.model.FrameworkMethod;

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
