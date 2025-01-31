package it.jakegblp.lusk.test.platform;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.jakegblp.lusk.test.utils.TestResults;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Main entry point of the test platform, it allows Lusk to run multiple testing environments.
 * <br><br>
 * This class contains code taken from <a href="https://github.com/SkriptLang/Skript">SkriptLang/Skript</a>.
 * @author JakeGBLP, SkriptLang
 */
public class PlatformMain {
	
	public static void main(String... args) throws IOException, InterruptedException {
		System.out.println("Initializing Lusk test platform...");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Path runnerRoot = Paths.get(args[0]);
        Path testsRoot = Paths.get(args[1]).toAbsolutePath();
        Path dataRoot = Paths.get(args[2]);
        Path envsRoot = Paths.get(args[3]);

        boolean devMode = "true".equals(args[4]);
        String verbosity = args[5].toUpperCase(Locale.ENGLISH);
		long timeout = Long.parseLong(args[6]);
		if (timeout < 0)
			timeout = 0;
		Set<String> jvmArgs = Sets.newHashSet(Arrays.copyOfRange(args, 7, args.length));
		if (jvmArgs.stream().noneMatch(arg -> arg.contains("-Xmx")))
			jvmArgs.add("-Xmx5G");

		// Load environments
		List<Environment> envs;
		if (Files.isDirectory(envsRoot)) {
			try (Stream<Path> paths = Files.walk(envsRoot)) {
				envs = paths.filter(path -> !Files.isDirectory(path))
						.map(path -> {
							try {
								return gson.fromJson(Files.readString(path), Environment.class);
							} catch (JsonSyntaxException | IOException e) {
								throw new RuntimeException(e);
							}
						})
						.collect(Collectors.toList());
			}
		} else {
			envs = Collections.singletonList(gson.fromJson(Files.readString(envsRoot), Environment.class));
		}
		System.out.println("Test environments: " + envs.stream().map(Environment::getName).collect(Collectors.joining(", ")));
		
		Set<String> allTests = new HashSet<>();
		Map<String, List<Failure>> failures = new HashMap<>();
		
		boolean docsFailed = false;
		// Run tests and collect the results
		envs.sort(Comparator.comparing(Environment::getName));
		for (Environment env : envs) {
			System.out.println("Starting testing on " + env.getName());
			env.initialize(dataRoot, runnerRoot);
			TestResults results = env.runTests(runnerRoot, testsRoot, devMode, verbosity, timeout, jvmArgs);
			if (results == null) {
				System.err.println("The test environment '" + env.getName() + "' failed to produce test results.");
				System.exit(3);
				return;
			}
			
			// Collect results
			docsFailed = results.docsFailed();
			allTests.addAll(results.getSucceeded());
			allTests.addAll(results.getFailed().keySet());
			for (Map.Entry<String, String> fail : results.getFailed().entrySet()) {
				String error = fail.getValue();
				assert error != null;
				failures.computeIfAbsent(fail.getKey(), (k) -> new ArrayList<>())
						.add(new Failure(env, error));
			}
		}

		if (docsFailed) {
			System.err.println("Documentation templates not found. Cannot generate docs!");
			System.exit(2);
			return;
		}

		// Sort results in alphabetical order
		List<String> succeeded = allTests.stream().filter(name -> !failures.containsKey(name)).sorted().collect(Collectors.toList());
        List<String> failNames = new ArrayList<>(failures.keySet());
		Collections.sort(failNames);

		// All succeeded tests in a single line
		StringBuilder output = new StringBuilder(String.format("%s Results %s%n", "-".repeat(25), "-".repeat(25)))
				.append("\nTested environments: ")
				.append(envs.stream().map(Environment::getName).collect(Collectors.joining(", ")))
				.append("\nSucceeded:\n  ")
				.append(String.join((", "), succeeded));

		if (!failNames.isEmpty()) { // More space for failed tests, they're important
			output.append("\nFailed:");
			for (String failed : failNames) {
				List<Failure> errors = failures.get(failed);
				output.append("\n  ").append(failed).append(" (on ").append(errors.size()).append(" environment").append(errors.size() == 1 ? "" : "s").append(")");
				for (Failure error : errors) {
					output.append("\n    ").append(error.string()).append(" (on ").append(error.environment().getName()).append(")");
				}
			}
			output.append(String.format("%n%n%s", "-".repeat(25)));
			System.err.print(output);
			System.exit(failNames.size()); // Error code to indicate how many tests failed.
			return;
		}
		output.append(String.format("%n%n%s", "-".repeat(25)));
		System.out.print(output);
		System.exit(0);
	}

}