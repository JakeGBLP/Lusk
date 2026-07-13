package it.jakegblp.lusk.test.utils;

import java.util.Map;
import java.util.Set;

/**
 * Contains test results: successes, failures and doc failure. Will be serialized with Gson
 * for transfering it between processes of gradle/environment and the actual spigot server.
 *
 * @param succeeded  Succeeded tests.
 * @param failed     Failed tests.
 * @param docsFailed If the docs failed to generate when running gradle doc gen commands.
 */
public record TestResults(Set<String> succeeded, Map<String, String> failed, boolean docsFailed) {

    public String createReport() {
        StringBuilder sb = new StringBuilder("Succeeded:\n");
        for (String test : succeeded)
            sb.append(test).append('\n');
        sb.append("Failed:\n");
        for (Map.Entry<String, String> entry : failed.entrySet())
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
        return sb.toString();
    }

}
