package com.sshtools.simjac;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.json.JsonValue;

public class ConfigurationStoreBuilder {

	private static String defaultApp = ConfigurationStore.class.getName();
	private static Scope defaultScope = Scope.USER;

	public static ConfigurationStoreBuilder builder() {
		return new ConfigurationStoreBuilder();
	}

	public static void setDefaultApp(Class<?> defaultApp) {
		setDefaultApp(defaultApp.getName());
	}

	public static void setDefaultApp(String defaultApp) {
		ConfigurationStoreBuilder.defaultApp = defaultApp;
	}

	public static void setDefaultScope(Scope defaultScope) {
		ConfigurationStoreBuilder.defaultScope = defaultScope;
	}

	Scope scope = defaultScope;
	Optional<Path> path = Optional.empty();
	Optional<Path> customRoot = Optional.empty();
	Optional<Supplier<JsonValue>> serializer = Optional.empty();
	Optional<Consumer<JsonValue>> deserializer = Optional.empty();
	Optional<String> name = Optional.empty();
	String app = defaultApp;
//	Optional<Binding<?>> binding = Optional.empty();
	boolean failOnMissingFile = true;

	private ConfigurationStoreBuilder() {
	}

	public ConfigurationStoreBuilder withDeserializer(Consumer<JsonValue> deserializer) {
		this.deserializer = Optional.of(deserializer);
		return this;
	}

	public ConfigurationStoreBuilder withSerializer(Supplier<JsonValue> serializer) {
		this.serializer = Optional.of(serializer);
		return this;
	}

	public ConfigurationStoreBuilder withName(String name) {
		this.name = Optional.of(name);
		return this;
	}

	public ConfigurationStoreBuilder withApp(String app) {
		this.app = app;
		return this;
	}

	public ConfigurationStoreBuilder withApp(Class<?> app) {
		return withApp(app.getName());
	}

	public ConfigurationStoreBuilder withScope(Scope scope) {
		if (customRoot.isPresent() && scope != Scope.CUSTOM)
			throw new IllegalArgumentException("A custom root has been set. Scope may not be used.");
		this.scope = scope;
		return this;
	}

	public ConfigurationStoreBuilder withoutFailOnMissingFile() {
		return withFailOnMissingFile(false);
	}

	public ConfigurationStoreBuilder withFailOnMissingFile(boolean failOnMissingFile) {
		this.failOnMissingFile = failOnMissingFile;
		return this;
	}

	public ConfigurationStoreBuilder withPath(String path) {
		return withPath(Path.of(path));
	}

	public ConfigurationStoreBuilder withPath(Path path) {
		return withPath(Optional.of(path));
	}

	public ConfigurationStoreBuilder withCustomRoot(String customRoot) {
		return withPath(Path.of(customRoot));
	}

	public ConfigurationStoreBuilder withCustomRoot(Path customRoot) {
		return withCustomRoot(Optional.of(customRoot));
	}

	public ConfigurationStoreBuilder withCustomRoot(Optional<Path> customRoot) {
		this.customRoot = customRoot;
		this.scope = Scope.CUSTOM;
		return this;
	}

	public ConfigurationStoreBuilder withPath(Optional<Path> path) {
		this.path = path;
		return this;
	}

	public ConfigurationStore build() {
		return new ConfigurationStoreImpl(this);
	}
}