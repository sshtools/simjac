package com.sshtools.simjac;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ArrayBinding<E> extends Binding<Collection<E>> {

	Supplier<E> construct();

	Supplier<Collection<E>> getter();

	Consumer<Collection<E>> setter();
	
	Function<E, Binding<E>> binding();
}
