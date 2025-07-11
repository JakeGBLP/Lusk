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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main entry point of test platform. It allows running this Skript on
 * multiple testing environments.
 */
public class PlatformMain {
	
	public static void main(String... args) throws IOException, InterruptedException {
		System.out.println("Initializing Lusk test platform...");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Path runnerRoot = Paths.get(args[0]);
		assert runnerRoot != null;
        Path testsRoot = Paths.get(args[1]).toAbsolutePath();
		assert testsRoot != null;
        Path dataRoot = Paths.get(args[2]);
		assert dataRoot != null;
        Path envsRoot = Paths.get(args[3]);
		assert envsRoot != null;
        String verbosity = args[4].toUpperCase(Locale.ENGLISH);
		long timeout = Long.parseLong(args[5]);
		if (timeout < 0)
			timeout = 0;
		Set<String> jvmArgs = Sets.newHashSet(Arrays.copyOfRange(args, 6, args.length));
		if (jvmArgs.stream().noneMatch(arg -> arg.contains("-Xmx")))
			jvmArgs.add("-Xmx5G");

		// Load environments
		List<Environment> envs;
		if (Files.isDirectory(envsRoot)) {
			envs = Files.walk(envsRoot).filter(path -> !Files.isDirectory(path))
					.map(path -> {
						try {
							return gson.fromJson(Files.readString(path), Environment.class);
						} catch (JsonSyntaxException | IOException e) {
							throw new RuntimeException(e);
						}
					}).collect(Collectors.toList());
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
			TestResults results = env.runTests(runnerRoot, testsRoot, verbosity, timeout, jvmArgs);
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