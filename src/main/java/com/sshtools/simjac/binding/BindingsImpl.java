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
package com.sshtools.simjac.binding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

public class BindingsImpl implements Bindings {

	private final boolean failOnMissingBinds;
	private final Binding<?> binding;

	public BindingsImpl(BindingsBuilder builder) {
		this.failOnMissingBinds = builder.isFailOnMissingBinds();
		this.binding = builder.getBinding().orElseThrow(() -> new IllegalStateException("No binding."));
	}

	@Override
	public JsonValue get() {
		return serializeValue(binding);
	}

	@Override
	public void accept(JsonValue t) {
		deserialize(binding, t);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void deserialize(Binding<?> binding, JsonValue jsonValue) {
		if (jsonValue.getValueType() == ValueType.OBJECT) {
			if (binding instanceof ObjectBinding) {
				var objBinding = (ObjectBinding<Object>) binding;
				jsonValue.asJsonObject().forEach((k, v) -> {
					var attrBinding = objBinding.bindings().get(k);
					if (attrBinding == null) {
						if (failOnMissingBinds) {
							throw new IllegalArgumentException(
									MessageFormat.format("Attribute {0} has no binding.", k));
						}
					} else {
						deserialize(attrBinding, v);
					}
				});
			} else if (binding instanceof MapBinding) {
				var mapBinding = (MapBinding<Object, Object>) binding;
				var mapMap = new LinkedHashMap<Object, Object>();
				jsonValue.asJsonObject().forEach((k, v) -> {
					var object = mapBinding.construct().apply(k);
					var attrBinding = mapBinding.binding().apply(k, object);
					if (attrBinding == null) {
						if (failOnMissingBinds) {
							throw new IllegalArgumentException(
									MessageFormat.format("Attribute {0} has no binding.", k));
						}
					} else {
						deserialize((Binding<?>) attrBinding, v);
						mapMap.put(k, object);
					}
				});
				mapBinding.setter().accept(mapMap);
			} else {
				throw new IllegalArgumentException(
						MessageFormat.format("Binding mismatch. Expected an {0}, but got a {1}.",
								ObjectBinding.class.getName(), binding.getClass().getName()));
			}
		} else if (jsonValue.getValueType() == ValueType.ARRAY) {
			if (!(binding instanceof ArrayBinding)) {
				throw new IllegalArgumentException(
						MessageFormat.format("Binding mismatch. Expected an {0}, but got a {1}.",
								ArrayBinding.class.getName(), binding.getClass().getName()));
			}
			var arrBinding = (ArrayBinding<Object>) binding;
			var arrayList = new ArrayList<>();
			jsonValue.asJsonArray().forEach(v -> {
				var object = arrBinding.construct().get();
				var bnd = arrBinding.binding().apply(object);
				deserialize(bnd, v);
				arrayList.add(object);
			});
			arrBinding.setter().accept(arrayList);
		} else if (jsonValue.getValueType() == ValueType.FALSE) {
			((AttrBinding<Boolean>) binding).setter().accept(false);
		} else if (jsonValue.getValueType() == ValueType.TRUE) {
			((AttrBinding<Boolean>) binding).setter().accept(true);

		} else if (jsonValue.getValueType() == ValueType.NULL) {
			if (((AttrBinding<Object>) binding).omitNull()) {
				return;
			} else if (((AttrBinding<Object>) binding).nullBlank()) {
				var attrBinding = (AttrBinding<?>) binding;
				if (attrBinding.type().equals(Double.class)) {
					((Consumer<Double>) attrBinding.setter()).accept(0d);
				} else if (attrBinding.type().equals(Float.class)) {
					((Consumer<Float>) attrBinding.setter()).accept(0f);
				} else if (attrBinding.type().equals(BigInteger.class)) {
					((Consumer<BigInteger>) attrBinding.setter()).accept(BigInteger.valueOf(0));
				} else if (attrBinding.type().equals(BigDecimal.class)) {
					((Consumer<BigDecimal>) attrBinding.setter()).accept(BigDecimal.valueOf(0));
				} else if (attrBinding.type().equals(Long.class)) {
					((Consumer<Long>) attrBinding.setter()).accept(0L);
				} else if (attrBinding.type().equals(Integer.class)) {
					((Consumer<Integer>) attrBinding.setter()).accept(0);
				} else if (attrBinding.type().equals(Short.class)) {
					((Consumer<Short>) attrBinding.setter()).accept((short) 0);
				} else if (attrBinding.type().equals(Character.class)) {
					((Consumer<Character>) attrBinding.setter()).accept((char) 0);
				} else if (attrBinding.type().equals(Byte.class)) {
					((Consumer<Byte>) attrBinding.setter()).accept((byte) 0);
				} else if (attrBinding.type().equals(String.class)) {
					((AttrBinding<Object>) binding).setter().accept("");

				} else {
					((AttrBinding<Object>) binding).setter().accept(null);
				}
			} else {
				((AttrBinding<Object>) binding).setter().accept(null);
			}
		} else if (jsonValue.getValueType() == ValueType.NUMBER) {
			var attrBinding = (AttrBinding<?>) binding;
			if (attrBinding.type().equals(Double.class)) {
				((Consumer<Double>) attrBinding.setter()).accept(((JsonNumber) jsonValue).doubleValue());
			} else if (attrBinding.type().equals(Float.class)) {
				((Consumer<Float>) attrBinding.setter()).accept((float) ((JsonNumber) jsonValue).doubleValue());
			} else if (attrBinding.type().equals(BigInteger.class)) {
				((Consumer<BigInteger>) attrBinding.setter()).accept(((JsonNumber) jsonValue).bigIntegerValue());
			} else if (attrBinding.type().equals(BigDecimal.class)) {
				((Consumer<BigDecimal>) attrBinding.setter()).accept(((JsonNumber) jsonValue).bigDecimalValue());
			} else if (attrBinding.type().equals(Long.class)) {
				((Consumer<Long>) attrBinding.setter()).accept(((JsonNumber) jsonValue).longValue());
			} else if (attrBinding.type().equals(Integer.class)) {
				((Consumer<Integer>) attrBinding.setter()).accept((int) ((JsonNumber) jsonValue).longValue());
			} else if (attrBinding.type().equals(Short.class)) {
				((Consumer<Short>) attrBinding.setter()).accept((short) ((JsonNumber) jsonValue).longValue());
			} else if (attrBinding.type().equals(Character.class)) {
				((Consumer<Character>) attrBinding.setter()).accept((char) ((JsonNumber) jsonValue).longValue());
			} else if (attrBinding.type().equals(Byte.class)) {
				((Consumer<Byte>) attrBinding.setter()).accept((byte) ((JsonNumber) jsonValue).longValue());
			}
		} else if (jsonValue.getValueType() == ValueType.STRING) {
			Class<?> etype = ((AttrBinding<Object>) binding).type();
			if (Enum.class.isAssignableFrom(etype)) {
				((AttrBinding<Enum>) binding).setter()
						.accept(Enum.valueOf((Class<Enum>) etype, ((JsonString) jsonValue).getString()));
			} else {
				((AttrBinding<String>) binding).setter().accept(((JsonString) jsonValue).getString());
			}
		} else {
			throw new UnsupportedOperationException();
		}

	}

	private JsonValue serializeValue(Binding<?> b) {
		if (b instanceof ArrayBinding || b instanceof ObjectBinding || b instanceof MapBinding) {
			return serializeStructure(b);
		} else {
			var bind = (AttrBinding<?>) b;
			var type = bind.type();
			var val = bind.getter().get();
			if (val == null) {
				if (bind.omitNull()) {
					return null;
				} else if (!bind.nullBlank()) {
					return JsonValue.NULL;
				}
			}

			if (bind.type().equals(ArrayBinding.class) || bind.type().equals(ObjectBinding.class)
					|| bind.type().equals(MapBinding.class)) {
				return serializeValue((Binding<?>) bind.getter().get());
			} else if (type.equals(String.class)) {
				if (val == null) {
					return Json.createValue((String) "");
				} else {
					return Json.createValue((String) val);
				}
			} else if (bind.type().equals(Double.class)) {
				return Json.createValue(val == null ? 0 : (Double) val);
			} else if (bind.type().equals(Float.class)) {
				return Json.createValue(val == null ? 0 : (Float) val);
			} else if (bind.type().equals(BigInteger.class)) {
				return Json.createValue(val == null ? BigInteger.valueOf(0L) : (BigInteger) val);
			} else if (bind.type().equals(BigDecimal.class)) {
				return Json.createValue(val == null ? BigDecimal.valueOf(0d) : (BigDecimal) val);
			} else if (bind.type().equals(Long.class)) {
				return Json.createValue(val == null ? 0L : (Long) val);
			} else if (bind.type().equals(Integer.class)) {
				return Json.createValue(val == null ? 0 : (Integer) val);
			} else if (bind.type().equals(Short.class)) {
				return Json.createValue(val == null ? 0 : (Short) val);
			} else if (bind.type().equals(Character.class)) {
				return Json.createValue(val == null ? 0 : (Character) val);
			} else if (bind.type().equals(Byte.class)) {
				return Json.createValue(val == null ? 0 : (Byte) val);
			} else if (bind.type().equals(Boolean.class)) {
				return val == null ? JsonValue.FALSE : ((Boolean) val ? JsonValue.TRUE : JsonValue.FALSE);
			} else if (Enum.class.isAssignableFrom(bind.type())) {
				return Json.createValue(((Enum<?>) val).name());
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}

	private JsonStructure serializeStructure(Binding<?> binding) {
		if (binding instanceof ArrayBinding) {
			var bldr = Json.createArrayBuilder();
			serializeArray((ArrayBinding<?>) binding, bldr);
			return bldr.build();
		} else if (binding instanceof MapBinding) {
			var bldr = Json.createObjectBuilder();
			serializeMap((MapBinding<?, ?>) binding, bldr);
			return bldr.build();
		} else {
			var bldr = Json.createObjectBuilder();
			serializeObject((ObjectBinding<?>) binding, bldr);
			return bldr.build();
		}
	}

	private <E> void serializeArray(ArrayBinding<E> array, JsonArrayBuilder bldr) {
		Function<E, Binding<E>> bnd = array.binding();
		for (var object : array.getter().get()) {
			bldr.add(serializeStructure(bnd.apply(object)));
		}

	}

	private <K, V> void serializeMap(MapBinding<K, V> map, JsonObjectBuilder bldr) {
		BiFunction<K, V, Binding<V>> bnd = map.binding();
		for (var entry : map.getter().get().entrySet()) {
			var ser = serializeValue(bnd.apply(entry.getKey(), entry.getValue()));
			if (ser != null) {
				bldr.add(entry.getKey().toString(), ser);
			}
		}
	}

	private void serializeObject(ObjectBinding<?> object, JsonObjectBuilder bldr) {
		object.bindings().forEach((k, v) -> {
			var ser = serializeValue(v);
			if (ser != null) {
				bldr.add(k, ser);
			}
		});
	}

}
