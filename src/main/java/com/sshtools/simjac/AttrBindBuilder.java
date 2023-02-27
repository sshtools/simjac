package com.sshtools.simjac;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AttrBindBuilder<T> {

	public static <T> AttrBindBuilder<T> xobject(Class<T> type, String name, Consumer<T> setter, Supplier<T> getter) {
		return new AttrBindBuilder<>(type).withName(name).withSet(setter).withGet(getter);
	}
	
	public static <T> AttrBindBuilder<T> xobject(Class<T> type, String name, Supplier<T> getter) {
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

	private Class<T> type;
	private Optional<String> name;
	private Optional<Consumer<T>> setter;
	private Optional<Supplier<T>> getter;
	private boolean omitNull = true;
	private boolean nullBlank = true;

	private AttrBindBuilder(Class<T> type) {
		this.type = type;
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
		this.getter = Optional.of(getter);
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