package com.sshtools.simjac;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;

public final class ConfigurationStoreImpl implements ConfigurationStore {
	private final Path path;
	private final boolean failOnMissingFile;
	private final String name;
	private final Optional<Supplier<JsonValue>> serializer;
	private final Optional<Consumer<JsonValue>> deserializer;

	ConfigurationStoreImpl(ConfigurationStoreBuilder builder) {

		var path = calcRootForScopeAppAndOs(builder.scope, builder.app, builder.customRoot);
		if (builder.path.isPresent())
			path = path.resolve(builder.path.get());

		this.path = path;
		this.failOnMissingFile = builder.failOnMissingFile;
		this.name = builder.name.orElseThrow(() -> new IllegalStateException("Name must be set."));
		this.serializer = builder.serializer;
		this.deserializer = builder.deserializer;
	}

	@Override
	public void retrieve() {
		var p = path.resolve(name);
		var f = p.resolveSibling(p.getFileName() + ".json");
		if (Files.exists(f)) {
			try (var in = Files.newBufferedReader(f)) {
				try (var parser = Json.createReader(in)) {
					deserializer.orElseThrow(() -> new IllegalStateException("Deserializer not configured."))
							.accept(parser.readValue());
				}
			} catch (IOException ioe) {
				throw new UncheckedIOException(ioe);
			}
		} else if (failOnMissingFile)
			throw new UncheckedIOException(new FileNotFoundException(MessageFormat.format("No file {0}", f)));
	}

	@Override
	public void store() {
		var p = path.resolve(name);
		checkDir(p.getParent());
		var properties = new HashMap<String, Object>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);
		var writerFactory = Json.createWriterFactory(properties);
		try (var out = Files.newBufferedWriter(p.resolveSibling(p.getFileName() + ".json"))) {
			try (var wrt = writerFactory.createWriter(out)) {
				wrt.write(serializer.orElseThrow(() -> new IllegalStateException("Sserializer not configured.")).get());
			}
		} catch (IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	private Path calcRootForOs(Scope scope, Optional<Path> customRoot) {
		if (scope == Scope.CUSTOM)
			return customRoot.orElseThrow(() -> new IllegalArgumentException(
					MessageFormat.format("Scope is {0}, but no custom root set.", scope)));

		var os = System.getProperty("os.name", "unknown").toLowerCase();
		if (os.contains("linux")) {
			switch (scope) {
			case GLOBAL:
				return Paths.get("/etc");
			case USER:
				return resolveHome().resolve(".config");
			default:
				throw new UnsupportedOperationException();
			}

		} else if (os.contains("windows")) {
			switch (scope) {
			case GLOBAL:
				return Paths.get("C:\\Program Files\\Common Files");
			case USER:
				return resolveHome().resolve("AppData").resolve("Roaming");
			default:
				throw new UnsupportedOperationException();
			}
		} else {
			switch (scope) {
			case GLOBAL:
				return Paths.get("/etc");
			case USER:
				return resolveHome();
			default:
				throw new UnsupportedOperationException();
			}
		}
	}

	private Path calcRootForScopeAppAndOs(Scope scope, String app, Optional<Path> customRoot) {
		var root = calcRootForOs(scope, customRoot);
		return root.resolve(root.equals(resolveHome()) ? "." + app : app);
	}

	private void checkDir(Path path) {
		if (Files.exists(path)) {
			if (!Files.isDirectory(path)) {
				throw new IllegalArgumentException(MessageFormat.format("{0} is not a directory.", path));
			}
		} else {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		}
	}

	private Path resolveHome() {
		return Paths.get(System.getProperty("user.home"));
	}

	@Override
	public void close() {
	}
}