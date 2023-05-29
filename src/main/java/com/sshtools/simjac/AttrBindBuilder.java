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
package com.sshtools.simjac;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class AttrBindBuilder<T> extends AbstractBindBuilder<AttrBinding<T>, T, AttrBindBuilder<T>> {

	private Optional<String> name;
	private Optional<Consumer<T>> setter;
	private Optional<Supplier<T>> getter;
	private boolean omitNull = true;
	private boolean nullBlank = true;
	private Map<String, AttrBinding<?>> bindings = new HashMap<>();
	private final Class<T> type;

	private AttrBindBuilder(Class<T> type) {
		this.type = type;
	}

	public static <T> AttrBindBuilder<T> xobject(Class<T> type, String name, Consumer<T> setter, Supplier<T> getter) {
		return new AttrBindBuilder<>(type).withName(name).withSet(setter).withGet(getter);
	}

	public static <P, T> AttrBindBuilder<T> xobject(Class<T> type, String name, Consumer<T> setter,
			Class<P> parentClass, Function<P, T> getter) {
		return new AttrBindBuilder<>(type).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<T> xobject(Class<T> type, String name, Supplier<T> getter) {
		return new AttrBindBuilder<>(type).withName(name).withGet(getter);
	}

	public static <P, T> AttrBindBuilder<T> xobject(Class<T> type, String name, Class<P> parentClass,
			Function<P, T> getter) {
		return new AttrBindBuilder<>(type).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<T> xobject(Class<T> type, String name, Consumer<T> setter) {
		return new AttrBindBuilder<>(type).withName(name).withSet(setter);
	}

	public static <T> AttrBindBuilder<T> xobject(Class<T> type) {
		return new AttrBindBuilder<>(type);
	}

	public static AttrBindBuilder<String> xstring() {
		return new AttrBindBuilder<>(String.class);
	}

	public static AttrBindBuilder<String> xstring(String name, Consumer<String> setter, Supplier<String> getter) {
		return new AttrBindBuilder<>(String.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<String> xstring(String name, Consumer<String> setter, Class<T> clazz,
			Function<T, String> getter) {
		return new AttrBindBuilder<>(String.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<String> xstring(String name, Function<T, String> getter) {
		return new AttrBindBuilder<>(String.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<String> xstring(String name, Supplier<String> getter) {
		return new AttrBindBuilder<>(String.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<String> xstring(String name, Consumer<String> setter) {
		return new AttrBindBuilder<>(String.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Integer> xinteger() {
		return new AttrBindBuilder<>(Integer.class);
	}

	public static AttrBindBuilder<Integer> xinteger(String name, Consumer<Integer> setter, Supplier<Integer> getter) {
		return new AttrBindBuilder<>(Integer.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Integer> xinteger(String name, Consumer<Integer> setter) {
		return new AttrBindBuilder<>(Integer.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Integer> xinteger(String name, Supplier<Integer> getter) {
		return new AttrBindBuilder<>(Integer.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Integer> xinteger(String name, Consumer<Integer> setter, Class<T> clazz,
			Function<T, Integer> getter) {
		return new AttrBindBuilder<>(Integer.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Integer> xinteger(String name, Function<T, Integer> getter) {
		return new AttrBindBuilder<>(Integer.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Boolean> xboolean() {
		return new AttrBindBuilder<>(Boolean.class);
	}

	public static AttrBindBuilder<Boolean> xboolean(String name, Consumer<Boolean> setter, Supplier<Boolean> getter) {
		return new AttrBindBuilder<>(Boolean.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Boolean> xboolean(String name, Consumer<Boolean> setter) {
		return new AttrBindBuilder<>(Boolean.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Boolean> xboolean(String name, Supplier<Boolean> getter) {
		return new AttrBindBuilder<>(Boolean.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Boolean> xboolean(String name, Consumer<Boolean> setter, Class<T> clazz,
			Function<T, Boolean> getter) {
		return new AttrBindBuilder<>(Boolean.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Boolean> xboolean(String name, Function<T, Boolean> getter) {
		return new AttrBindBuilder<>(Boolean.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Long> xlong() {
		return new AttrBindBuilder<>(Long.class);
	}

	public static AttrBindBuilder<Long> xlong(String name, Consumer<Long> setter, Supplier<Long> getter) {
		return new AttrBindBuilder<>(Long.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Long> xlong(String name, Consumer<Long> setter) {
		return new AttrBindBuilder<>(Long.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Long> xlong(String name, Supplier<Long> getter) {
		return new AttrBindBuilder<>(Long.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Long> xlong(String name, Consumer<Long> setter, Class<T> clazz,
			Function<T, Long> getter) {
		return new AttrBindBuilder<>(Long.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Long> xlong(String name, Function<T, Long> getter) {
		return new AttrBindBuilder<>(Long.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Short> xshort() {
		return new AttrBindBuilder<>(Short.class);
	}

	public static AttrBindBuilder<Short> xshort(String name, Consumer<Short> setter, Supplier<Short> getter) {
		return new AttrBindBuilder<>(Short.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Short> xshort(String name, Consumer<Short> setter) {
		return new AttrBindBuilder<>(Short.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Short> xshort(String name, Supplier<Short> getter) {
		return new AttrBindBuilder<>(Short.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Short> xshort(String name, Consumer<Short> setter, Class<T> clazz,
			Function<T, Short> getter) {
		return new AttrBindBuilder<>(Short.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Short> xshort(String name, Function<T, Short> getter) {
		return new AttrBindBuilder<>(Short.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Double> xdouble() {
		return new AttrBindBuilder<>(Double.class);
	}

	public static AttrBindBuilder<Double> xdouble(String name, Consumer<Double> setter, Supplier<Double> getter) {
		return new AttrBindBuilder<>(Double.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Double> xdouble(String name, Consumer<Double> setter) {
		return new AttrBindBuilder<>(Double.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Double> xdouble(String name, Supplier<Double> getter) {
		return new AttrBindBuilder<>(Double.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Double> xdouble(String name, Consumer<Double> setter, Class<T> clazz,
			Function<T, Double> getter) {
		return new AttrBindBuilder<>(Double.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Double> xdouble(String name, Function<T, Double> getter) {
		return new AttrBindBuilder<>(Double.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Float> xfloat() {
		return new AttrBindBuilder<>(Float.class);
	}

	public static AttrBindBuilder<Float> xfloat(String name, Consumer<Float> setter, Supplier<Float> getter) {
		return new AttrBindBuilder<>(Float.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Float> xfloat(String name, Consumer<Float> setter) {
		return new AttrBindBuilder<>(Float.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Float> xfloat(String name, Supplier<Float> getter) {
		return new AttrBindBuilder<>(Float.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Float> xfloat(String name, Consumer<Float> setter, Class<T> clazz,
			Function<T, Float> getter) {
		return new AttrBindBuilder<>(Float.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Float> xfloat(String name, Function<T, Float> getter) {
		return new AttrBindBuilder<>(Float.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Character> xchar() {
		return new AttrBindBuilder<>(Character.class);
	}

	public static AttrBindBuilder<Character> xchar(String name, Consumer<Character> setter,
			Supplier<Character> getter) {
		return new AttrBindBuilder<>(Character.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Character> xchar(String name, Consumer<Character> setter) {
		return new AttrBindBuilder<>(Character.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Character> xchar(String name, Supplier<Character> getter) {
		return new AttrBindBuilder<>(Character.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Character> xchar(String name, Consumer<Character> setter, Class<T> clazz,
			Function<T, Character> getter) {
		return new AttrBindBuilder<>(Character.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Character> xchar(String name, Function<T, Character> getter) {
		return new AttrBindBuilder<>(Character.class).withName(name).withGet(getter);
	}

	public static AttrBindBuilder<Byte> xbyte() {
		return new AttrBindBuilder<>(Byte.class);
	}

	public static AttrBindBuilder<Byte> xbyte(String name, Consumer<Byte> setter, Supplier<Byte> getter) {
		return new AttrBindBuilder<>(Byte.class).withName(name).withSet(setter).withGet(getter);
	}

	public static AttrBindBuilder<Byte> xbyte(String name, Consumer<Byte> setter) {
		return new AttrBindBuilder<>(Byte.class).withName(name).withSet(setter);
	}

	public static AttrBindBuilder<Byte> xbyte(String name, Supplier<Byte> getter) {
		return new AttrBindBuilder<>(Byte.class).withName(name).withGet(getter);
	}

	public static <T> AttrBindBuilder<Byte> xbyte(String name, Consumer<Byte> setter, Class<T> clazz,
			Function<T, Byte> getter) {
		return new AttrBindBuilder<>(Byte.class).withName(name).withSet(setter).withGet(getter);
	}

	public static <T> AttrBindBuilder<Byte> xbyte(String name, Function<T, Byte> getter) {
		return new AttrBindBuilder<>(Byte.class).withName(name).withGet(getter);
	}

	public AttrBindBuilder<T> withBinding(AttrBinding<?>... bindings) {
		for (var b : bindings) {
			this.bindings.put(b.name(), b);
		}
		return this;
	}

	public AttrBindBuilder<T> withoutOmitNull() {
		return withOmitNull(false);
	}

	public AttrBindBuilder<T> withOmitNull(boolean omitNull) {
		this.omitNull = omitNull;
		return this;
	}

	public AttrBindBuilder<T> withoutNullBlank() {
		return withNullBlank(false);
	}

	public AttrBindBuilder<T> withNullBlank(boolean nullBlank) {
		this.nullBlank = nullBlank;
		return this;
	}

	public AttrBindBuilder<T> withName(String name) {
		this.name = Optional.of(name);
		return this;
	}

	public AttrBindBuilder<T> withSet(Consumer<T> setter) {
		this.setter = Optional.of(setter);
		return this;
	}

	public AttrBindBuilder<T> withGet(Supplier<T> getter) {
		return withGet((o) -> getter.get());
	}

	public AttrBindBuilder<T> withGet(Function<?, T> getter) {
		// TODO this 'null' is temporary, need to pass down object
		this.getter = Optional.of(() -> getter.apply(null));
		return this;
	}

	public AttrBinding<T> build() {
		var g = this.getter;
		var s = this.setter;
		var n = this.name.orElseThrow(() -> new IllegalStateException("Name must be supplied."));
		var t = this.type;
		var on = this.omitNull;
		var nb = this.nullBlank;
		return new AttrBinding<>() {
			@Override
			public Supplier<T> getter() {
				return g.orElseThrow(() -> new IllegalArgumentException(
						MessageFormat.format("No bound getter for {0}.", name.orElse("Unknown"))));
			}

			@Override
			public Consumer<T> setter() {
				return s.orElseThrow(() -> new IllegalArgumentException(
						MessageFormat.format("No bound setter for {0}.", name.orElse("Unknown"))));
			}

			@Override
			public String name() {
				return n;
			}

			@Override
			public Class<T> type() {
				return t;
			}

			@Override
			public boolean omitNull() {
				return on;
			}

			@Override
			public boolean nullBlank() {
				return nb;
			}
		};
	}
}
