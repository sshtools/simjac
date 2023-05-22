package com.sshtools.simjac;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MapBindingBuilder<K, V> extends AbstractBindBuilder<MapBinding<K, V>, Map<K, V>, MapBindingBuilder<K, V>> {

	public static <K, V> MapBindingBuilder<K, V> builder(Class<K> key, Class<K> val) {
		return new MapBindingBuilder<>();
	}
	
	public static <K, V, M extends Map<K, V>> MapBindingBuilder<K, V> builder(Class<K> key, Class<V> value, M map) {
		return new MapBindingBuilder<K, V>().withGet(() -> map).withSet((n) -> {
			map.clear();
			map.putAll(n);
		});
	}

	private Optional<Consumer<Map<K,V>>> setter;
	private Optional<Supplier<Map<K,V>>> getter;
	private Optional<Supplier<Map.Entry<K, V>>> construct;
	private Optional<Function<Map.Entry<K, V>, Binding<Map.Entry<K, V>>>> binding = Optional.empty();
	
	public MapBindingBuilder() {
	}

	public MapBindingBuilder<K, V> withBinding(Function<Map.Entry<K, V>, Binding<Map.Entry<K, V>>> binding) {
		this.binding = Optional.of(binding);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <B, T> MapBindingBuilder<K, V> withBindingBuilder(Supplier<B> builderSupplier, Function<B, T> constructor, Function<B, Binding<Map.Entry<K, V>>> populator) {
		var builder = builderSupplier.get();
		withConstruct(() -> (Map.Entry<K, V>)constructor.apply(builder));
		return withBinding((obj) -> {
			populator.apply(builder);
			return populator.apply(builder);
		});
	}

	public MapBindingBuilder<K, V> withSet(Consumer<Map<K, V>> setter) {
		this.setter = Optional.of(setter);
		return this;
	}

	public MapBindingBuilder<K, V> withConstruct(Supplier<Map.Entry<K, V>> getter) {
		this.construct = Optional.of(getter);
		return this;
	}

	public MapBindingBuilder<K, V> withGet(Supplier<Map<K, V>> getter) {
		this.getter = Optional.of(getter);
		return this;
	}

	@Override
	public MapBinding<K, V> build() {
		var g = this.getter;
		var s = this.setter;
		var b = this.binding;
		var c = this.construct;
		return new MapBinding<>() {

			@Override
			public Supplier<Map<K, V>> getter() {
				return g.orElseThrow(() -> new IllegalArgumentException(
						"No bound getter."));
			}

			@Override
			public Consumer<Map<K, V>> setter() {
				return s.orElseThrow(() -> new IllegalArgumentException("No bound setter."));
			}

			@Override
			public Function<Map.Entry<K, V>, Binding<Map.Entry<K, V>>> binding() {
				return b.orElseThrow(() -> new IllegalArgumentException("No bound binding."));
			}

			@Override
			public Supplier<Map.Entry<K, V>> construct() {
				return c.orElseThrow(() -> new IllegalArgumentException("No bound constructor."));
			}
		};
	}

}
