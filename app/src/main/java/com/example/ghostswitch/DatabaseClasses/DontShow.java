package com.example.ghostswitch.DatabaseClasses;

public class DontShow {
    private String dontName;
    private String dontInstance;

    public DontShow(String dontName, String dontInstance) {
        this.dontName = dontName;
        this.dontInstance = dontInstance;
    }

    public String getDontName() {
        return dontName;
    }

    public void setDontName(String dontName) {
        this.dontName = dontName;
    }

    public String getDontInstance() {
        return dontInstance;
    }

    public void setDontInstance(String dontInstance) {
        this.dontInstance = dontInstance;
    }
}
