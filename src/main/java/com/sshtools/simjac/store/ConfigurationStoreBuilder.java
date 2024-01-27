/**
 * Copyright © 2023 JAdaptive Limited (support@jadaptive.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sshtools.simjac.store;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.json.JsonValue;

public final class ConfigurationStoreBuilder {
	private static String defaultApp = ConfigurationStore.class.getName();

	private static Scope defaultScope = Scope.USER;
	private String app = defaultApp;
	private Optional<Path> customRoot = Optional.empty();
	private Optional<Consumer<JsonValue>> deserializer = Optional.empty();
	// Optional<Binding<?>> binding = Optional.empty();
	private boolean failOnMissingFile = true;
	private boolean failOnParsingError = true;
	private Optional<String> name = Optional.empty();
	private Optional<Path> path = Optional.empty();
	private Scope scope = defaultScope;
	private Optional<Supplier<JsonValue>> serializer = Optional.empty();

	private ConfigurationStoreBuilder() {
	}

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

	public ConfigurationStore build() {
		return new ConfigurationStoreImpl(this);
	}

	public ConfigurationStoreBuilder withApp(Class<?> app) {
		return withApp(app.getName());
	}

	public ConfigurationStoreBuilder withApp(String app) {
		this.app = app;
		return this;
	}

	public ConfigurationStoreBuilder withCustomRoot(Optional<Path> customRoot) {
		this.customRoot = customRoot;
		this.scope = Scope.CUSTOM;
		return this;
	}

	public ConfigurationStoreBuilder withCustomRoot(Path customRoot) {
		return withCustomRoot(Optional.of(customRoot));
	}

	public ConfigurationStoreBuilder withCustomRoot(String customRoot) {
		return withPath(Path.of(customRoot));
	}

	public ConfigurationStoreBuilder withDeserializer(Consumer<JsonValue> deserializer) {
		this.deserializer = Optional.of(deserializer);
		return this;
	}

	public ConfigurationStoreBuilder withFailOnMissingFile(boolean failOnMissingFile) {
		this.failOnMissingFile = failOnMissingFile;
		return this;
	}

	public ConfigurationStoreBuilder withFailOnParsingError(boolean failOnParsingError) {
		this.failOnParsingError = failOnParsingError;
		return this;
	}

	public ConfigurationStoreBuilder withName(String name) {
		this.name = Optional.of(name);
		return this;
	}

	public ConfigurationStoreBuilder withoutFailOnMissingFile() {
		return withFailOnMissingFile(false);
	}

	public ConfigurationStoreBuilder withoutFailOnParsingError() {
		return withFailOnParsingError(false);
	}

	public ConfigurationStoreBuilder withPath(Optional<Path> path) {
		this.path = path;
		return this;
	}

	public ConfigurationStoreBuilder withPath(Path path) {
		return withPath(Optional.of(path));
	}

	public ConfigurationStoreBuilder withPath(String path) {
		return withPath(Path.of(path));
	}

	public ConfigurationStoreBuilder withScope(Scope scope) {
		if (customRoot.isPresent() && scope != Scope.CUSTOM) {
			throw new IllegalArgumentException("A custom root has been set. Scope may not be used.");
		}
		this.scope = scope;
		return this;
	}

	public ConfigurationStoreBuilder withSerializer(Supplier<JsonValue> serializer) {
		this.serializer = Optional.of(serializer);
		return this;
	}

	public ConfigurationStoreBuilder withSerializerDeserializer(SerDeser serDeser) {
		return withSerializer(serDeser).withDeserializer(serDeser);
	}

	String getApp() {
		return app;
	}

	Optional<Path> getCustomRoot() {
		return customRoot;
	}

	Optional<Consumer<JsonValue>> getDeserializer() {
		return deserializer;
	}

	Optional<String> getName() {
		return name;
	}

	Optional<Path> getPath() {
		return path;
	}

	Scope getScope() {
		return scope;
	}

	Optional<Supplier<JsonValue>> getSerializer() {
		return serializer;
	}

	boolean isFailOnMissingFile() {
		return failOnMissingFile;
	}

	boolean isFailOnParsingError() {
		return failOnParsingError;
	}

	public interface SerDeser extends Supplier<JsonValue>, Consumer<JsonValue> {

	}
}
