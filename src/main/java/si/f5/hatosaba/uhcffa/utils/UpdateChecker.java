package si.f5.hatosaba.uhcffa.utils;

import com.google.gson.JsonObject;
import si.f5.hatosaba.uhcffa.Uhcffa;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class UpdateChecker {

    public static boolean checkForUpdates() {
        return checkForUpdates(true);
    }

    private static Map<String, String> attributes = new HashMap<>();

    static {
        try {
            Enumeration<URL> resources = Uhcffa.instance().getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);

            while (resources.hasMoreElements()) {
                InputStream inputStream = resources.nextElement().openStream();
                Manifest manifest = new Manifest(inputStream);
                manifest.getMainAttributes().forEach((o, o2) -> System.out.println(o.toString() + ":" + o2.toString()));
                manifest.getMainAttributes().forEach((key, value) -> attributes.put(key.toString(), (String) value));
                inputStream.close();
            }
        } catch (IOException ignored) {}
    }

    static String getManifestValue(String key) {
        return attributes.get(key);
    }

    public static boolean checkForUpdates(boolean verbose) {
        JsonObject jsonObject = null;
        try {
            String buildHash = getManifestValue("Git-Revision");
            System.out.println(buildHash);

            if (buildHash == null || buildHash.equalsIgnoreCase("unknown") || buildHash.length() != 40) {
                System.out.println("Git-Revision wasn't available, plugin is a dev build");
                System.out.println("You will receive no support for this plugin version.");
                return false;
            }

            /*String minimumHash = HttpUtil.requestHttp("https://raw.githubusercontent.com/DiscordSRV/DiscordSRV/randomaccessfiles/minimumbuild").trim();
            if (minimumHash.length() == 40) { // make sure we have a hash
                JsonObject minimumComparisonResult = jsonObject = Uhcffa.instance().getGson().fromJson(HttpUtil.requestHttp("https://api.github.com/repos/DiscordSRV/DiscordSRV/compare/" + minimumHash + "..." + buildHash + "?per_page=1"), JsonObject.class);
                boolean minimumAhead = minimumComparisonResult.get("status").getAsString().equalsIgnoreCase("behind");
                if (minimumAhead) {
                    printUpdateMessage("The current build of DiscordSRV does not meet the minimum required to be secure! DiscordSRV will not start.");
                    //Uhcffa.instance().disablePlugin();
                    return true;
                }
            } else {
                System.out.println("Failed to check against minimum version of DiscordSRV: received minimum build was not 40 characters long & thus not a commit hash");
            }*/

            // build is ahead of minimum so that's good

            String masterHash = Uhcffa.instance().getGson().fromJson(HttpUtil.requestHttp("https://api.github.com/repos/DiscordSRV/DiscordSRV/git/refs/heads/master"), JsonObject.class).getAsJsonObject("object").get("sha").getAsString();
            JsonObject masterComparisonResult = jsonObject = Uhcffa.instance().getGson().fromJson(HttpUtil.requestHttp("https://api.github.com/repos/DiscordSRV/DiscordSRV/compare/" + masterHash + "..." + buildHash + "?per_page=1"), JsonObject.class);
            String masterStatus = masterComparisonResult.get("status").getAsString();
            switch (masterStatus.toLowerCase()) {
                case "ahead":
                case "diverged":
                    if (!verbose) {
                        return false;
                    }
                    String developHash = Uhcffa.instance().getGson().fromJson(HttpUtil.requestHttp("https://api.github.com/repos/DiscordSRV/DiscordSRV/git/refs/heads/develop"), JsonObject.class).getAsJsonObject("object").get("sha").getAsString();
                    JsonObject developComparisonResult = jsonObject = Uhcffa.instance().getGson().fromJson(HttpUtil.requestHttp("https://api.github.com/repos/DiscordSRV/DiscordSRV/compare/" + developHash + "..." + buildHash + "?per_page=1"), JsonObject.class);
                    String developStatus = developComparisonResult.get("status").getAsString();
                    switch (developStatus.toLowerCase()) {
                        case "ahead":
                            System.out.println("This build of DiscordSRV is ahead of master and develop. [latest private dev build]");
                            return false;
                        case "identical":
                            System.out.println("This build of DiscordSRV is identical to develop. [latest public dev build]");
                            return false;
                        case "behind":
                            System.out.println("This build of DiscordSRV is ahead of master but behind develop. Update your development build!");
                            return true;
                    }
                    return false;
                case "behind":
                    jsonObject = masterComparisonResult;
                    printUpdateMessage("The current build of DiscordSRV is outdated by " + masterComparisonResult.get("behind_by").getAsInt() + " commits!");
                    return true;
                case "identical":
                    if (verbose) System.out.println("DiscordSRV is up-to-date. (" + buildHash + ")");
                    return false;
                default:
                    System.out.println("Got weird build comparison status from GitHub: " + masterStatus + ". Assuming plugin is up-to-date.");
                    return false;
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                if (e.getMessage().contains("google.gson") && jsonObject != null) {
                    try {
                        // Not required, make log messages huge
                        jsonObject.remove("files");
                        jsonObject.remove("commits");
                    } catch (Throwable ignored) {}
                    System.out.println("Update check failed due to unexpected json response: " + e.getMessage() + " (" + jsonObject + ")");
                } else {
                    System.out.println("Update check failed: " + e.getMessage());
                }
            } else {
                System.out.println("Update check failed: " + e.getClass().getName());
            }
            System.out.println(e);
            return false;
        }
    }

    private static void printUpdateMessage(String explanation) {
        System.out.println("\n\n" + explanation + " Get the latest build at your favorite distribution center.\n\n" +
                "Spigot: https://www.spigotmc.org/resources/discordsrv.18494/\n" +
                "Github: https://github.com/DiscordSRV/DiscordSRV/releases\n" +
                "Direct Download: https://get.discordsrv.com\n");
    }

}
