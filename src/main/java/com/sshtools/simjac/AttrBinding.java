package com.sshtools.simjac;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface AttrBinding<T> {
	
	String name();

	Supplier<T> getter();

	Consumer<T> setter();

	Class<T> type();
	
	boolean omitNull();
	
	boolean nullBlank();
}