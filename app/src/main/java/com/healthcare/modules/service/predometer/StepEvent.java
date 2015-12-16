package com.healthcare.modules.service.predometer;

public class StepEvent {
    private final double certainty; // probability
    private final double duration; // in seconds
    
    public StepEvent(double probability, double duration) {
            this.certainty = probability;
            this.duration = duration;
    }
    
    public double getProbability() {
            return certainty;
    }
    
    public double getDuration() {
            return duration;
    }
    
    public String toString() {
            return "StepEvent(cert=" + certainty + ", dur=" + duration + ")";
    }
    
}