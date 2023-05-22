package com.sshtools.simjac;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface MapBinding<K, V> extends Binding<Map<K, V>> {

	Function<K, V> construct();

	Supplier<Map<K, V>> getter();

	Consumer<Map<K, V>> setter();
	
	
	BiFunction<K, V, Binding<V>> binding();
}
