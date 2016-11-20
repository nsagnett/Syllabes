package com.syllabes.model;

public class Word {
    private String label;
    private String[] syllabes;
    private boolean alreadyUsed;

    public Word(String label) {
        this.label = label;
        alreadyUsed = false;
    }

    public String getLabel() {
        return label;
    }

    public String[] getSyllabes() {
        return syllabes;
    }

    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public void setSyllabes(String[] syllabes) {
        this.syllabes = syllabes;
    }
}
