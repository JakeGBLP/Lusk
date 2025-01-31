package it.jakegblp.lusk.test.platform;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import it.jakegblp.lusk.test.utils.TestResults;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Test environment information.
 * <br><br>
 * This class contains code taken from <a href="https://github.com/SkriptLang/Skript">SkriptLang/Skript</a>.
 * @author JakeGBLP, SkriptLang
 */
public class Environment {

	private static final Gson gson = new Gson();

	/**
	 * Name of this environment. For example, spigot-1.14.
	 */
	@Getter
    private final String name;

	/**
	 * Resource that needs to be downloaded for the environment.
	 */
	@Getter
    public static class Resource {

		/**
		 * Where to get this resource.
		 */
		private final String source;

		/**
		 * Path under platform root where it should be placed.
		 * Directories created as needed.
		 */
		private final String target;

		public Resource(String url, String target) {
			this.source = url;
			this.target = target;
		}

    }

	public static class SkriptResource extends Resource {

		private final String version;

		@Nullable
		private transient String source;

		public SkriptResource(String version, String target) {
			super(null, target);
			this.version = version;
		}

		@Override
		public String getSource() {
			try {
				generateSkriptSource();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (source == null)
				throw new IllegalStateException();
			return source;
		}

		public void generateSkriptSource() {
			if (source != null)
				return;
			System.out.println("skript version: " + version);
			if (version.equals("2.10.0"))
				source = "https://github.com/JakeGBLP/Skript/raw/2.10.0/final/Skript.jar";
			else
				source = "https://repo.skriptlang.org/releases/com/github/SkriptLang/Skript/" + version + "/Skript-" + version + ".jar";
		}
	}

	public static class SpigotResource extends Resource {

		private static final String BUILD_TOOLS_URL = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar";
		private static final String JAVA_EXEC = "java";
		private Path serverFolder;
		private Path buildToolsPath;
		public Path root;

		private final String version;

		@Nullable
		private transient String source;

		public SpigotResource(String version, String target) {
			super(null, target);
			this.version = version;

		}

		public void definePaths() {
			serverFolder = root.resolve("spigot-" + version);
			buildToolsPath = serverFolder.resolve("BuildTools.jar");
			serverFolder.toFile().mkdirs();
		}

		@Override
		public String getSource() {
			try {
				generateSource();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (source == null)
				throw new IllegalStateException();
			return source;
		}

		public void generateSource() throws IOException, InterruptedException {
			definePaths();
			if (!serverFolder.resolve("spigot-" + version + ".jar").toFile().exists()) {
				if (!buildToolsPath.toFile().exists()) {
					downloadBuildTools();
				}
				buildSpigot(version);
			}
			source = serverFolder.toString();
		}

		private void downloadBuildTools() throws IOException {
			System.out.println("Downloading BuildTools.jar...");
			try (InputStream in = new URL(BUILD_TOOLS_URL).openStream()) {
				Files.copy(in, buildToolsPath, StandardCopyOption.REPLACE_EXISTING);
			}
			System.out.println("Downloaded BuildTools.jar!");
		}

		private void buildSpigot(String version) throws IOException, InterruptedException {
			System.out.println("Running BuildTools for Spigot " + version + "...");
			ProcessBuilder processBuilder = new ProcessBuilder(
					JAVA_EXEC, "-jar", buildToolsPath.toAbsolutePath().toString(), "--rev", version
			);
			processBuilder.directory(serverFolder.toFile());
			processBuilder.inheritIO();
			Process process = processBuilder.start();
			int exitCode = process.waitFor();

			if (exitCode == 0) {
				System.out.println("Spigot build complete.");
			} else {
				System.out.println("BuildTools failed.");
				throw new RuntimeException("BuildTools exited with code " + exitCode);
			}
		}

	}

	public static class PaperResource extends Resource {

		private final String version;

		@Nullable
		private transient String source;

		public PaperResource(String version, String target) {
			super(null, target);
			this.version = version;
		}

		@Override
		public String getSource() {
			try {
				generateSource();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (source == null)
				throw new IllegalStateException();
			return source;
		}

		private void generateSource() throws IOException {
			if (source != null)
				return;

			String stringUrl = "https://api.papermc.io/v2/projects/paper/versions/" + version;
			URL url = new URL(stringUrl);
			JsonObject jsonObject;
			try (InputStream is = url.openStream()) {
				InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
				jsonObject = gson.fromJson(reader, JsonObject.class);
			}

			JsonArray jsonArray = jsonObject.get("builds").getAsJsonArray();

			int latestBuild = -1;
			for (JsonElement jsonElement : jsonArray) {
				int build = jsonElement.getAsInt();
				if (build > latestBuild) {
					latestBuild = build;
				}
			}

			if (latestBuild == -1)
				throw new IllegalStateException("No builds for this version");

			source = "https://api.papermc.io/v2/projects/paper/versions/" + version + "/builds/" + latestBuild
				+ "/downloads/paper-" + version + "-" + latestBuild + ".jar";
		}
	}

	/**
	 * Resources that need to be copied.
	 */
	private final List<Resource> resources;

	/**
	 * Resources that need to be downloaded.
	 */
	@Nullable
	private final List<Resource> downloads;

	/**
	 * Paper resources that need to be downloaded.
	 */
	@Nullable
	private final List<PaperResource> paperDownloads;

	/**
	 * Spigot resources that need to be downloaded.
	 */
	@Nullable
	private final List<SpigotResource> spigotDownloads;

	/**
	 * Skript resources that need to be downloaded.
	 */
	@Nullable
	private final List<SkriptResource> skriptDownloads;

	/**
	 * Where Lusk should be placed under platform root.
	 * Directories created as needed.
	 */
	private final String luskTarget;

	/**
	 * Added after platform's own JVM flags.
	 */
	private final String[] commandLine;

	public Environment(String name, List<Resource> resources, @Nullable List<Resource> downloads, @Nullable List<PaperResource> paperDownloads, @Nullable List<SpigotResource> spigotDownloads, @Nullable List<SkriptResource> skriptDownloads, String luskTarget, String... commandLine) {
		this.name = name;
		this.resources = resources;
		this.downloads = downloads;
		this.paperDownloads = paperDownloads;
		this.spigotDownloads = spigotDownloads;
		this.skriptDownloads = skriptDownloads;
		this.luskTarget = luskTarget;
		this.commandLine = commandLine;
	}

    public void initialize(Path dataRoot, Path runnerRoot) throws IOException, InterruptedException {
		Path env = runnerRoot.resolve(name);
		Path lusk = env.resolve(luskTarget);
		Files.createDirectories(lusk.getParent());
		Path buildLibsDir = Paths.get("build", "libs");
		File[] jarFiles = buildLibsDir.toFile().listFiles((dir, name) -> name.endsWith(".jar"));

		if (jarFiles == null || jarFiles.length == 0)
			throw new RuntimeException("No JAR files found in the build/libs directory!");

		Path luskSource = jarFiles[0].toPath();
		Files.copy(luskSource, lusk, StandardCopyOption.REPLACE_EXISTING);

		if (this.spigotDownloads != null)
			resources.addAll(this.spigotDownloads);

        // Copy resources
		for (Resource resource : resources) {
			System.out.println("---->"+dataRoot);
			if (resource instanceof SpigotResource spigotResource) {
				spigotResource.root = runnerRoot;
				spigotResource.generateSource();
				break;
			}
			Path source = dataRoot.resolve(resource.getSource());
			Path target = env.resolve(resource.getTarget());
			System.out.println("source: " +source);
			System.out.println("target: " +target);
			Files.createDirectories(target.getParent());
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		}

		List<Resource> downloads = new ArrayList<>();
		if (this.downloads != null)
			downloads.addAll(this.downloads);
		if (this.paperDownloads != null)
			downloads.addAll(this.paperDownloads);
		if (this.skriptDownloads != null)
			downloads.addAll(this.skriptDownloads);

		// Download additional resources
		for (Resource resource : downloads) {
			assert resource != null;
			String source = resource.getSource();
			URL url = new URL(source);
			Path target = env.resolve(resource.getTarget());
			Files.createDirectories(target.getParent());
			try (InputStream is = url.openStream()) {
				Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	@Nullable
	public TestResults runTests(Path runnerRoot, Path testsRoot, boolean devMode, String verbosity,
								long timeout, Set<String> jvmArgs) throws IOException, InterruptedException {
		Path env = runnerRoot.resolve(name);
		Path resultsPath = env.resolve("test_results.json");
		Files.deleteIfExists(resultsPath);
		List<String> args = new ArrayList<>();
		args.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
		args.add("-ea");
		args.add("-Dskript.testing.enabled=true");
		args.add("-Dskript.testing.dir=" + testsRoot);
		args.add("-Dskript.testing.devMode=" + devMode);
		if (!verbosity.equalsIgnoreCase("null"))
			args.add("-Dskript.testing.verbosity=" + verbosity);
		args.add("-Dskript.testing.results=test_results.json");
		args.add("-Ddisable.watchdog=true");
		args.addAll(jvmArgs);
		args.addAll(Arrays.asList(commandLine));

		Process process = new ProcessBuilder(args)
				.directory(env.toFile())
				.redirectOutput(Redirect.INHERIT)
				.redirectError(Redirect.INHERIT)
				.redirectInput(Redirect.INHERIT)
				.start();

		Runtime.getRuntime().addShutdownHook(new Thread(process::destroy));

		// Catch tests running for abnormally long time
		if (!devMode && timeout > 0) {
			new Timer("runner watchdog", true).schedule(new TimerTask() {
				@Override
				public void run() {
					if (process.isAlive()) {
						System.err.println("Test environment is taking too long, failing...");
						System.exit(1);
					}
				}
			}, timeout);
		}

		int code = process.waitFor();
		if (code != 0)
			throw new IOException("environment returned with code " + code);

		// Read test results
		if (!Files.exists(resultsPath))
			return null;

		TestResults results = new Gson().fromJson(new String(Files.readAllBytes(resultsPath)), TestResults.class);
		assert results != null;
		return results;

	}


}