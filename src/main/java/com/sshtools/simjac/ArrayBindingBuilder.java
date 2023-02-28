package com.sshtools.simjac;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArrayBindingBuilder<E> extends AbstractBindBuilder<ArrayBinding<E>, Collection<E>, ArrayBindingBuilder<E>> {

	public static <E> ArrayBindingBuilder<E> builder(Class<E> item) {
		return new ArrayBindingBuilder<>();
	}
	
	public static <E, L extends Collection<E>> ArrayBindingBuilder<E> builder(Class<E> item, L list) {
		return new ArrayBindingBuilder<E>().withGet(() -> list).withSet((n) -> {
			list.clear();
			list.addAll(n);
		});
	}

	private Optional<Consumer<Collection<E>>> setter;
	private Optional<Supplier<Collection<E>>> getter;
	private Optional<Supplier<E>> construct;
	private Optional<Function<E, Binding<E>>> binding = Optional.empty();
	
	public ArrayBindingBuilder() {
	}

	public ArrayBindingBuilder<E> withBinding(Function<E, Binding<E>> binding) {
		this.binding = Optional.of(binding);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <B, T> ArrayBindingBuilder<E> withBindingBuilder(Supplier<B> builderSupplier, Function<B, T> constructor, Function<B, Binding<E>> populator) {
		var builder = builderSupplier.get();
		withConstruct(() -> (E)constructor.apply(builder));
		return withBinding((obj) -> {
			populator.apply(builder);
			return populator.apply(builder);
		});
	}

	public ArrayBindingBuilder<E> withSet(Consumer<Collection<E>> setter) {
		this.setter = Optional.of(setter);
		return this;
	}

	public ArrayBindingBuilder<E> withConstruct(Supplier<E> getter) {
		this.construct = Optional.of(getter);
		return this;
	}

	public ArrayBindingBuilder<E> withGet(Supplier<Collection<E>> getter) {
		this.getter = Optional.of(getter);
		return this;
	}

	@Override
	public ArrayBinding<E> build() {
		var g = this.getter;
		var s = this.setter;
		var b = this.binding;
		var c = this.construct;
		return new ArrayBinding<>() {

			@Override
			public Supplier<Collection<E>> getter() {
				return g.orElseThrow(() -> new IllegalArgumentException(
						"No bound getter."));
			}

			@Override
			public Consumer<Collection<E>> setter() {
				return s.orElseThrow(() -> new IllegalArgumentException("No bound setter."));
			}

			@Override
			public Function<E, Binding<E>> binding() {
				return b.orElseThrow(() -> new IllegalArgumentException("No bound binding."));
			}

			@Override
			public Supplier<E> construct() {
				return c.orElseThrow(() -> new IllegalArgumentException("No bound constructor."));
			}
		};
	}

}
