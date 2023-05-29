package com.sshtools.simjac.binding;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class ObjectBindingBuilderTest {

	@Test
	void testSimpleReadObject() {
		var obj = new SimpleObject();
		obj.name = "Joe B";
		obj.age = 20;
		obj.turnip = true;

		var bldr = ObjectBindingBuilder.builder(SimpleObject.class)
				.withBinding(AttrBindBuilder.xstring("name", () -> obj.name).build())
				.withBinding(AttrBindBuilder.xinteger("age", () -> obj.age).build())
				.withBinding(AttrBindBuilder.xboolean("turnip", () -> obj.turnip).build()).build();
		var bindings = bldr.bindings();

		assertEquals("Joe B", bindings.get("name").getter().get());
		assertEquals(20, bindings.get("age").getter().get());
		assertEquals(true, bindings.get("turnip").getter().get());
	}

	@SuppressWarnings("unchecked")
	@Test
	void testSimpleWriteObject() {
		var obj = new SimpleObject();

		var bldr = ObjectBindingBuilder.builder(SimpleObject.class)
				.withBinding(AttrBindBuilder.xstring("name", s -> obj.name = s).build())
				.withBinding(AttrBindBuilder.xinteger("age", s -> obj.age = s).build())
				.withBinding(AttrBindBuilder.xboolean("turnip", s -> obj.turnip = s).build()).build();
		var bindings = bldr.bindings();

		((Consumer<String>) bindings.get("name").setter()).accept("Joe B");
		((Consumer<Integer>) bindings.get("age").setter()).accept(20);
		((Consumer<Boolean>) bindings.get("turnip").setter()).accept(true);

		assertEquals("Joe B", obj.name);
		assertEquals(20, obj.age);
		assertEquals(true, obj.turnip);
	}

	@Test
	void testSimpleReadWriteObject() {
		var obj = new SimpleObject();
		var bldr = ObjectBindingBuilder.builder(SimpleObject.class)
				.withBinding(AttrBindBuilder.xstring("name", n -> obj.name = n, () -> obj.name).build())
				.withBinding(AttrBindBuilder.xinteger("age", n -> obj.age = n, () -> obj.age).build())
				.withBinding(AttrBindBuilder.xboolean("turnip", n -> obj.turnip = n, () -> obj.turnip).build()).build();
	}

	public static class SimpleObject {
		private String name;
		private int age;
		private boolean turnip;
	}
}
