package com.sshtools.simjac;

import java.util.Optional;

public class BindingsBuilder {
	boolean failOnMissingBinds = true;
	Optional<Binding<?>> binding;

	public static BindingsBuilder builder() {
		return new BindingsBuilder();
	}

	private BindingsBuilder() {
	}

	public <T> BindingsBuilder withBinding(Binding<T> binding) {
		this.binding = Optional.of(binding);
		return this;
	}

	public BindingsBuilder withoutFailOnMissingBinds() {
		return withFailOnMissingBinds(false);
	}

	public BindingsBuilder withFailOnMissingBinds(boolean failOnMissingBinds) {
		this.failOnMissingBinds = failOnMissingBinds;
		return this;
	}

	public Bindings build() {
		return new BindingsImpl(this);
	}
}
