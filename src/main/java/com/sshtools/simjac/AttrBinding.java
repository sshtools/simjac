package com.sshtools.simjac;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface AttrBinding<T> extends Binding<T> {

	Class<T> type();
	
	String name();

	Supplier<T> getter();

	Consumer<T> setter();
	
	boolean omitNull();
	
	boolean nullBlank();
}