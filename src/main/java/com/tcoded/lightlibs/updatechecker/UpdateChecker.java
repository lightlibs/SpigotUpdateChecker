package com.tcoded.lightlibs.updatechecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("unused")
public class UpdateChecker {

    private URL checkURL;

    private final String[] currVersionSections;
    private String availableVersion;

    private final UpdateResult result;

    public UpdateChecker(String currentVersion, Integer resourceId) {
        this.currVersionSections = currentVersion.split("\\.");

        this.result = new UpdateResult(UpdateResult.Type.FAIL_SPIGOT, null, currentVersion);

        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
        } catch (MalformedURLException e) {
            result.setType(UpdateResult.Type.FAIL_SPIGOT);
            return;
        }

        run();
    }

    private void run() {
        URLConnection con;
        try {
            con = checkURL.openConnection();
        } catch (IOException e1) {
            result.setType(UpdateResult.Type.FAIL_SPIGOT);
            return;
        }

        try {
            availableVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch (IOException e) {
            result.setType(UpdateResult.Type.FAIL_SPIGOT);
            return;
        }

        if (availableVersion.isEmpty()) {
            result.setType(UpdateResult.Type.FAIL_SPIGOT);
            return;
        }

        result.setLatestVer(availableVersion);

        // Version sections of remote
        String[] versionSections = availableVersion.split("\\.");
        // Test diff
        for (int i = 0; i < versionSections.length || i < currVersionSections.length; i++) { // Continue until both versions run out of subsections
            try {
                // - Detailed walk through -
                // Statement below means: if number of sections is i+1 or greater
                // (Example: 5.3.2 has 3 sections and i = 0, true)
                //   - Explanation -
                // 5.3.2 has indexes 0-2, which also means if i = 3, we are on the 4th iteration and
                // ran out of available subsections
                boolean vSecExists = versionSections.length - i > 0;
                boolean cvSecExists = currVersionSections.length - i > 0; // current version has that many sections too?
                if (!vSecExists) { // if remote version doesn't have that many subsections (aka, we are running something newer)
                    result.setType(UpdateResult.Type.DEV_BUILD);
                    return;
                } else if (!cvSecExists) { // if local version doesn't have that many subsections (aka remote is running something newer)
                    result.setType(getUpdateResultPriority(i));
                    return;
                }
                int vSecInt = Integer.parseInt(versionSections[i]); // get int value of remote sub-section value
                int cvSecInt = Integer.parseInt(currVersionSections[i]); // get int value of local sub-section value
                if (vSecInt > cvSecInt) { // remote  > local ? We are out of date.
                    result.setType(getUpdateResultPriority(i));
                    return;
                } else if (cvSecInt > vSecInt) { // local > remote ? We are running something not yet released!
                    result.setType(UpdateResult.Type.DEV_BUILD);
                    return;
                }
            } catch (NumberFormatException e) {
                result.setType(UpdateResult.Type.UNKNOWN_VERSION); // Not a parsable number? Unknown version, since we can't compare!
                return;
            }
        }
        result.setType(UpdateResult.Type.NO_UPDATE);
    }

    public UpdateResult getResult() {
        return this.result;
    }

    public String getVersion() {
        return this.availableVersion;
    }

    public UpdateResult.Type getUpdateResultPriority(int i) {
        switch (i) {
            case 0:
                return UpdateResult.Type.UPDATE_HIGH;
            case 1:
                return UpdateResult.Type.UPDATE_MEDIUM;
            default:
                return UpdateResult.Type.UPDATE_LOW;
        }
    }

}
