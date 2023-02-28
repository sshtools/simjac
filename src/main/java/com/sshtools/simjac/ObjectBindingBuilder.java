package com.sshtools.simjac;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ObjectBindingBuilder<T> extends AbstractBindBuilder<ObjectBinding<T>, T, ObjectBindingBuilder<T>> {

	public static <T> ObjectBindingBuilder<T> builder(Class<T> type) {
		return new ObjectBindingBuilder<>(type);
	}

	private Map<String, AttrBinding<?>> bindings = new HashMap<>();
	
	private ObjectBindingBuilder(Class<T> type) {
	}

	public ObjectBindingBuilder<T> withBinding(AttrBinding<?>... bindings) {
		for (var b : bindings) {
			this.bindings.put(b.name(), b);
		}
		return this;
	}

	@Override
	public ObjectBinding<T> build() {
		var b = Collections.unmodifiableMap(this.bindings);
		return new ObjectBinding<>() {
			@Override
			public Map<String, AttrBinding<?>> bindings() {
				return b;
			}
		};
	}

}
