package com.sshtools.simjac;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.json.JsonValue;

public interface Bindings extends Supplier<JsonValue>, Consumer<JsonValue> {

}
