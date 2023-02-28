package com.sshtools.simjac;

import java.util.Map;

public interface ObjectBinding<T> extends Binding<T> {
	Map<String, AttrBinding<?>> bindings();
}
