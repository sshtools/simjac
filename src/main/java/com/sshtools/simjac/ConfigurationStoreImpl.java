package com.sshtools.simjac;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonGenerator;

public final class ConfigurationStoreImpl implements ConfigurationStore {
	private final Path path;
	private final Map<String, AttrBinding<?>> bindings;
	private final boolean failOnMissingBinds;
	private final boolean failOnMissingFile;
	private final String name;

	ConfigurationStoreImpl(ConfigurationStoreBuilder builder) {
		bindings = Collections.unmodifiableMap(builder.bindings);

		var path = calcRootForScopeAppAndOs(builder.scope, builder.app, builder.customRoot);
		if (builder.path.isPresent())
			path = path.resolve(builder.path.get());

		this.path = path;
		this.failOnMissingBinds = builder.failOnMissingBinds;
		this.failOnMissingFile = builder.failOnMissingFile;
		this.name = builder.name.orElseThrow(() -> new IllegalStateException("Name must be set."));
	}

	@Override
	public void retrieve() {

		var p = path.resolve(name);
		var f = p.resolveSibling(p.getFileName() + ".json");
		if (Files.exists(f)) {
			try (var in = Files.newBufferedReader(f)) {
				try (var parser = Json.createReader(in)) {
					deserialize(parser.readValue());
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
				var bldr = Json.createObjectBuilder();
				serialize(bldr);
				wrt.writeObject(bldr.build());
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

	@SuppressWarnings("unchecked")
	private void deserialize(JsonValue jsonValue) {
		if (jsonValue.getValueType() == ValueType.OBJECT) {
			var obj = jsonValue.asJsonObject();
			for (var k : obj.keySet()) {
				var attrBinding = bindings.get(k);
				if (attrBinding == null) {
					if (failOnMissingBinds) {
						throw new IllegalArgumentException(MessageFormat.format("Attribute {0} has no binding.", k));
					}
				} else {
					if (obj.isNull(k)) {
						if (attrBinding.nullBlank()) {
							if (attrBinding.type().equals(String.class)) {
								((Consumer<String>) attrBinding.setter()).accept("");
							} else if (attrBinding.type().equals(Double.class)) {
								((Consumer<Double>) attrBinding.setter()).accept(0d);
							} else if (attrBinding.type().equals(Float.class)) {
								((Consumer<Float>) attrBinding.setter()).accept(0f);
							} else if (attrBinding.type().equals(BigInteger.class)) {
								((Consumer<BigInteger>) attrBinding.setter()).accept(BigInteger.valueOf(0));
							} else if (attrBinding.type().equals(BigDecimal.class)) {
								((Consumer<BigDecimal>) attrBinding.setter()).accept(BigDecimal.valueOf(0));
							} else if (attrBinding.type().equals(Long.class)) {
								((Consumer<Long>) attrBinding.setter()).accept(0l);
							} else if (attrBinding.type().equals(Integer.class)) {
								((Consumer<Integer>) attrBinding.setter()).accept(0);
							} else if (attrBinding.type().equals(Short.class)) {
								((Consumer<Short>) attrBinding.setter()).accept((short) 0);
							} else if (attrBinding.type().equals(Character.class)) {
								((Consumer<Character>) attrBinding.setter()).accept((char) 0);
							} else if (attrBinding.type().equals(Byte.class)) {
								((Consumer<Byte>) attrBinding.setter()).accept((byte) 0);
							} else if (attrBinding.type().equals(Boolean.class)) {
								((Consumer<Boolean>) attrBinding.setter()).accept(false);
							} else
								throw new UnsupportedOperationException(MessageFormat.format(
										"Type {0} cannot be deserialized, it is an unsupported type.",
										attrBinding.type().getName()));
						} else
							attrBinding.setter().accept(null);
					} else {
						if (attrBinding.type().equals(String.class)) {
							var v = obj.getString(k);
							if (attrBinding.nullBlank() && v.equals("")) {
								((Consumer<String>) attrBinding.setter()).accept(null);
							} else {
								((Consumer<String>) attrBinding.setter()).accept(obj.getString(k));
							}
						} else if (attrBinding.type().equals(Double.class)) {
							((Consumer<Double>) attrBinding.setter()).accept(obj.getJsonNumber(k).doubleValue());
						} else if (attrBinding.type().equals(Float.class)) {
							((Consumer<Float>) attrBinding.setter()).accept((float) obj.getJsonNumber(k).doubleValue());
						} else if (attrBinding.type().equals(BigInteger.class)) {
							((Consumer<BigInteger>) attrBinding.setter())
									.accept(obj.getJsonNumber(k).bigIntegerValue());
						} else if (attrBinding.type().equals(BigDecimal.class)) {
							((Consumer<BigDecimal>) attrBinding.setter())
									.accept(obj.getJsonNumber(k).bigDecimalValue());
						} else if (attrBinding.type().equals(Long.class)) {
							((Consumer<Long>) attrBinding.setter()).accept(obj.getJsonNumber(k).longValue());
						} else if (attrBinding.type().equals(Integer.class)) {
							((Consumer<Integer>) attrBinding.setter()).accept(obj.getInt(k));
						} else if (attrBinding.type().equals(Short.class)) {
							((Consumer<Short>) attrBinding.setter()).accept((short) obj.getInt(k));
						} else if (attrBinding.type().equals(Character.class)) {
							((Consumer<Character>) attrBinding.setter()).accept((char) obj.getInt(k));
						} else if (attrBinding.type().equals(Byte.class)) {
							((Consumer<Byte>) attrBinding.setter()).accept((byte) obj.getInt(k));
						} else if (attrBinding.type().equals(Boolean.class)) {
							((Consumer<Boolean>) attrBinding.setter()).accept(obj.getBoolean(k));
						} else
							throw new UnsupportedOperationException(
									MessageFormat.format("Type {0} cannot be deserialized, it is an unsupported type.",
											attrBinding.type().getName()));
					}
				}
			}
		} else
			throw new UnsupportedOperationException("TODO");
	}

	private Path resolveHome() {
		return Paths.get(System.getProperty("user.home"));
	}

	private void serialize(JsonObjectBuilder bldr) {
		for (var bndEn : bindings.entrySet()) {
			var bndEnVal = bndEn.getValue();
			var val = bndEnVal.getter().get();
			if (val == null) {
				if (!bndEnVal.omitNull()) {
					if (bndEnVal.type().equals(String.class) && bndEnVal.nullBlank()) {
						bldr.add(bndEn.getKey(), "");
					} else {
						bldr.addNull(bndEn.getKey());
					}
				}
			} else {
				var type = bndEnVal.type();
				if (type.equals(String.class)) {
					if (val.equals("")) {
						if (bndEnVal.nullBlank()) {
							bldr.add(bndEn.getKey(), (String) val);
						} else {
							bldr.addNull(bndEn.getKey());
						}
					} else {
						bldr.add(bndEn.getKey(), (String) val);
					}
				} else if (bndEnVal.type().equals(Double.class)) {
					bldr.add(bndEn.getKey(), (Double) val);
				} else if (bndEnVal.type().equals(Float.class)) {
					bldr.add(bndEn.getKey(), (Float) val);
				} else if (bndEnVal.type().equals(BigInteger.class)) {
					bldr.add(bndEn.getKey(), (BigInteger) val);
				} else if (bndEnVal.type().equals(BigDecimal.class)) {
					bldr.add(bndEn.getKey(), (BigDecimal) val);
				} else if (bndEnVal.type().equals(Long.class)) {
					bldr.add(bndEn.getKey(), (Long) val);
				} else if (bndEnVal.type().equals(Integer.class)) {
					bldr.add(bndEn.getKey(), (Integer) val);
				} else if (bndEnVal.type().equals(Short.class)) {
					bldr.add(bndEn.getKey(), (Short) val);
				} else if (bndEnVal.type().equals(Character.class)) {
					bldr.add(bndEn.getKey(), (Character) val);
				} else if (bndEnVal.type().equals(Byte.class)) {
					bldr.add(bndEn.getKey(), (Byte) val);
				} else if (bndEnVal.type().equals(Boolean.class)) {
					bldr.add(bndEn.getKey(), (Boolean) val);
				} else
					throw new UnsupportedOperationException(MessageFormat.format(
							"Type {0} cannot be serialized, it is an unsupported type.", val.getClass().getName()));
			}
		}
	}

	@Override
	public void close() {
		bindings.clear();
	}
}