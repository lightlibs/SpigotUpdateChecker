package com.tcoded.lightlibs.updatechecker;

public class UpdateResult {

    private Type type;
    private String latestVer;
    private final String currentVer;

    public UpdateResult(Type typeIn, String latestVerIn, String currentVerIn) {
        this.type = typeIn;
        this.latestVer = latestVerIn;
        this.currentVer = currentVerIn;
    }

    public void setType(Type typeIn) { this.type = typeIn; }
    public void setLatestVer(String latestVerIn) { this.latestVer = latestVerIn; }

    public Type getType() { return this.type; }
    public String getCurrentVer() { return this.currentVer; }
    public String getLatestVer() { return this.latestVer; }

    public enum Type {
        NO_UPDATE,
        FAIL_SPIGOT,
        UNKNOWN_VERSION,
        UPDATE_LOW,
        UPDATE_MEDIUM,
        UPDATE_HIGH,
        DEV_BUILD
    }
}