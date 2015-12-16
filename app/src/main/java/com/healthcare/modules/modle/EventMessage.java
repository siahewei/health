package com.healthcare.modules.modle;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/11
 */
public class EventMessage {

    public long startStamp;
    public long endStamp;
    public int steps;
    public float stepValue;

    public EventMessage(long startStamp, long endStamp, int steps, float stepValue) {
        this.startStamp = startStamp;
        this.endStamp = endStamp;
        this.steps = steps;
        this.stepValue = stepValue;
    }

    public long getStartStamp() {
        return startStamp;
    }

    public long getEndStamp() {
        return endStamp;
    }

    public int getSteps() {
        return steps;
    }

    public float getStepValue() {
        return stepValue;
    }
}
