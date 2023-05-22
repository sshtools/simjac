package com.sshtools.simjac;

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
		this.failOnMissingBinds = builder.failOnMissingBinds;
		this.binding = builder.binding.orElseThrow(() -> new IllegalStateException("No binding."));
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
			if(binding instanceof ObjectBinding) {
				var objBinding = (ObjectBinding<Object>)binding;
				jsonValue.asJsonObject().forEach((k,v) -> {
					var attrBinding = objBinding.bindings().get(k);
					if (attrBinding == null) {
						if (failOnMissingBinds) {
							throw new IllegalArgumentException(MessageFormat.format("Attribute {0} has no binding.", k));
						}
					} else {
						deserialize(attrBinding, v);
					}
				});
			}
			else if(binding instanceof MapBinding) {
				var mapBinding = (MapBinding<Object,Object>)binding;
				var mapMap = new LinkedHashMap<Object,Object>();
				jsonValue.asJsonObject().forEach((k,v) -> {
					var object = mapBinding.construct().apply(k);
					var attrBinding = mapBinding.binding().apply(k, object);
					if (attrBinding == null) {
						if (failOnMissingBinds) {
							throw new IllegalArgumentException(MessageFormat.format("Attribute {0} has no binding.", k));
						}
					} else {
						deserialize((Binding<?>) attrBinding, v);
						mapMap.put(k, object);
					}
				});
				mapBinding.setter().accept(mapMap);
			}
			else
				throw new IllegalArgumentException(MessageFormat.format("Binding mismatch. Expected an {0}, but got a {1}.", ObjectBinding.class.getName(), binding.getClass().getName()));
		}
		else if (jsonValue.getValueType() == ValueType.ARRAY) {
			if(!(binding instanceof ArrayBinding)) {
				throw new IllegalArgumentException(MessageFormat.format("Binding mismatch. Expected an {0}, but got a {1}.", ArrayBinding.class.getName(), binding.getClass().getName()));
			}
			var arrBinding = (ArrayBinding<Object>)binding;
			var arrayList = new ArrayList<>();
			jsonValue.asJsonArray().forEach(v -> {
				var object = arrBinding.construct().get();
				var bnd = arrBinding.binding().apply(object);
				deserialize(bnd, v);
				arrayList.add(object);
			});
			arrBinding.setter().accept(arrayList);
		}
		else if (jsonValue.getValueType() == ValueType.FALSE) {
			((AttrBinding<Boolean>)binding).setter().accept(false);
		}
		else if (jsonValue.getValueType() == ValueType.TRUE) {
			((AttrBinding<Boolean>)binding).setter().accept(true);
			
		}
		else if (jsonValue.getValueType() == ValueType.NULL) {
			if(((AttrBinding<Object>)binding).nullBlank()) {	
				var attrBinding = (AttrBinding<?>)binding;
				if (attrBinding.type().equals(Double.class)) {
					((Consumer<Double>) attrBinding.setter()).accept(0d);
				} else if (attrBinding.type().equals(Float.class)) {
					((Consumer<Float>) attrBinding.setter()).accept(0f);
				} else if (attrBinding.type().equals(BigInteger.class)) {
					((Consumer<BigInteger>) attrBinding.setter())
							.accept(BigInteger.valueOf(0));
				} else if (attrBinding.type().equals(BigDecimal.class)) {
					((Consumer<BigDecimal>) attrBinding.setter())
							.accept(BigDecimal.valueOf(0));
				} else if (attrBinding.type().equals(Long.class)) {
					((Consumer<Long>) attrBinding.setter()).accept(0l);
				} else if (attrBinding.type().equals(Integer.class)) {
					((Consumer<Integer>) attrBinding.setter()).accept(0);
				} else if (attrBinding.type().equals(Short.class)) {
					((Consumer<Short>) attrBinding.setter()).accept((short)0);
				} else if (attrBinding.type().equals(Character.class)) {
					((Consumer<Character>) attrBinding.setter()).accept((char)0);
				} else if (attrBinding.type().equals(Byte.class)) {
					((Consumer<Byte>) attrBinding.setter()).accept((byte)0);
				} else if (attrBinding.type().equals(String.class))  {
					((AttrBinding<Object>)binding).setter().accept("");
					
				} else {
					((AttrBinding<Object>)binding).setter().accept(null);
				}
			}
			else {
				((AttrBinding<Object>)binding).setter().accept(null);
			}
		}
		else if (jsonValue.getValueType() == ValueType.NUMBER) {
			var attrBinding = (AttrBinding<?>)binding;
			if (attrBinding.type().equals(Double.class)) {
				((Consumer<Double>) attrBinding.setter()).accept(((JsonNumber)jsonValue).doubleValue());
			} else if (attrBinding.type().equals(Float.class)) {
				((Consumer<Float>) attrBinding.setter()).accept((float) ((JsonNumber)jsonValue).doubleValue());
			} else if (attrBinding.type().equals(BigInteger.class)) {
				((Consumer<BigInteger>) attrBinding.setter())
						.accept(((JsonNumber)jsonValue).bigIntegerValue());
			} else if (attrBinding.type().equals(BigDecimal.class)) {
				((Consumer<BigDecimal>) attrBinding.setter())
						.accept(((JsonNumber)jsonValue).bigDecimalValue());
			} else if (attrBinding.type().equals(Long.class)) {
				((Consumer<Long>) attrBinding.setter()).accept(((JsonNumber)jsonValue).longValue());
			} else if (attrBinding.type().equals(Integer.class)) {
				((Consumer<Integer>) attrBinding.setter()).accept((int)((JsonNumber)jsonValue).longValue());
			} else if (attrBinding.type().equals(Short.class)) {
				((Consumer<Short>) attrBinding.setter()).accept((short)((JsonNumber)jsonValue).longValue());
			} else if (attrBinding.type().equals(Character.class)) {
				((Consumer<Character>) attrBinding.setter()).accept((char)((JsonNumber)jsonValue).longValue());
			} else if (attrBinding.type().equals(Byte.class)) {
				((Consumer<Byte>) attrBinding.setter()).accept((byte)((JsonNumber)jsonValue).longValue());
			}
		}
		else if (jsonValue.getValueType() == ValueType.STRING) {
			Class<?> etype = ((AttrBinding<Object>)binding).type();
			if(Enum.class.isAssignableFrom(etype)) {
				((AttrBinding<Enum>)binding).setter().accept(Enum.valueOf((Class<Enum>)etype, ((JsonString)jsonValue).getString()));
			}
			else {
				((AttrBinding<String>)binding).setter().accept(((JsonString)jsonValue).getString());
			}	
		}
		else
			throw new UnsupportedOperationException();
		
	}
	
	private JsonValue serializeValue(Binding<?> b) {
		if(b instanceof ArrayBinding || b instanceof ObjectBinding || b instanceof MapBinding){
			return serializeStructure(b);
		}
		else {
			var bind = (AttrBinding<?>)b;
			var type = bind.type();
			var val = bind.getter().get();
			if (type.equals(String.class)) {
				if (val == null || val.equals("")) {
					if (bind.nullBlank()) {
						return Json.createValue((String) "");
					} else {
						return null;
					}
				} else {
					return Json.createValue((String) val);
				}
			} else if (bind.type().equals(Double.class)) {
				return Json.createValue((Double) val);
			} else if (bind.type().equals(Float.class)) {
				return Json.createValue((Float) val);
			} else if (bind.type().equals(BigInteger.class)) {
				return Json.createValue((BigInteger) val);
			} else if (bind.type().equals(BigDecimal.class)) {
				return Json.createValue((BigDecimal) val);
			} else if (bind.type().equals(Long.class)) {
				return Json.createValue((Long) val);
			} else if (bind.type().equals(Integer.class)) {
				return Json.createValue((Integer) val);
			} else if (bind.type().equals(Short.class)) {
				return Json.createValue((Short) val);
			} else if (bind.type().equals(Character.class)) {
				return Json.createValue((Character) val);
			} else if (bind.type().equals(Byte.class)) {
				return Json.createValue((Byte) val);
			} else if (bind.type().equals(Boolean.class)) {
				return  (Boolean) val ? JsonValue.TRUE : JsonValue.FALSE;
			} else if (Enum.class.isAssignableFrom(bind.type())) {
				return Json.createValue(((Enum<?>) val).name());
			} else {
				throw new UnsupportedOperationException();
			}
		}
	}
	
	private JsonStructure serializeStructure(Binding<?> binding) {
		if(binding instanceof ArrayBinding) {
			var bldr = Json.createArrayBuilder();
			serializeArray((ArrayBinding<?>) binding, bldr);
			return bldr.build();
		}
		else if(binding instanceof MapBinding) {
			var bldr = Json.createObjectBuilder();
			serializeMap((MapBinding<?, ?>)binding, bldr);
			return bldr.build();
		}
		else {
			var bldr = Json.createObjectBuilder();
			serializeObject((ObjectBinding<?>)binding, bldr);
			return bldr.build();
		}
	}

	private <E> void serializeArray(ArrayBinding<E> array, JsonArrayBuilder bldr) {
		Function<E, Binding<E>> bnd = array.binding();
		for(var object : array.getter().get()) {
			bldr.add(serializeStructure(bnd.apply(object)));
		}
		
	}
	private <K, V> void serializeMap(MapBinding<K, V> map,  JsonObjectBuilder bldr) {
		BiFunction<K, V, Binding<V>> bnd = map.binding();
		for(var entry : map.getter().get().entrySet()) {
			bldr.add(entry.getKey().toString(), serializeValue(bnd.apply(entry.getKey(), entry.getValue())));
		}
	}
	
	private void serializeObject(ObjectBinding<?> object,  JsonObjectBuilder bldr) {
		object.bindings().forEach((k,v) -> bldr.add(k, serializeValue(v)));
	}

}
