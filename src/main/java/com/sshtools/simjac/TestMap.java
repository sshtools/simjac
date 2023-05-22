package com.sshtools.simjac;

import static com.sshtools.simjac.AttrBindBuilder.xboolean;
import static com.sshtools.simjac.AttrBindBuilder.xbyte;
import static com.sshtools.simjac.AttrBindBuilder.xchar;
import static com.sshtools.simjac.AttrBindBuilder.xdouble;
import static com.sshtools.simjac.AttrBindBuilder.xfloat;
import static com.sshtools.simjac.AttrBindBuilder.xinteger;
import static com.sshtools.simjac.AttrBindBuilder.xlong;
import static com.sshtools.simjac.AttrBindBuilder.xshort;
import static com.sshtools.simjac.AttrBindBuilder.xstring;

import java.util.HashMap;

public class TestMap {
	
	interface ITest{
		String getName();

		boolean isSelected();

		void setSelected(boolean selected);

		int getValue();

		void setValue(int value);

		void setName(String name);

		void setMarker(char marker);

		char getMarker();

		void setFactor(float factor);

		float getFactor();

		void setRatio(double ratio);

		double getRatio();

		void setTime(long time);

		long getTime();

		void setFlag(byte flag);

		byte getFlag();

		void setSize(short size);

		short getSize();
	}
	
	static class TestObject implements ITest {
		private String name;
		private int value;
		private boolean selected;
		private short size;
		private byte flag;
		private long time;
		private double ratio;
		private float factor;
		private char marker = '*';
		
		@Override
		public byte getFlag() {
			return flag;
		}

		@Override
		public void setFlag(byte flag) {
			this.flag = flag;
		}

		@Override
		public long getTime() {
			return time;
		}

		@Override
		public void setTime(long time) {
			this.time = time;
		}

		@Override
		public double getRatio() {
			return ratio;
		}

		@Override
		public void setRatio(double ratio) {
			this.ratio = ratio;
		}

		@Override
		public float getFactor() {
			return factor;
		}

		@Override
		public void setFactor(float factor) {
			this.factor = factor;
		}

		@Override
		public char getMarker() {
			return marker;
		}

		@Override
		public void setMarker(char marker) {
			this.marker = marker;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public void setValue(int value) {
			this.value = value;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int getValue() {
			return value;
		}

		@Override
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		@Override
		public boolean isSelected() {
			return selected;
		}

		@Override
		public void setSize(short size) {
			this.size = size;
		}

		@Override
		public short getSize() {
			return size;
		}

		@Override
		public String toString() {
			return "TestObject [name=" + name + ", value=" + value + ", selected=" + selected + ", size=" + size
					+ ", flag=" + flag + ", time=" + time + ", ratio=" + ratio + ", factor=" + factor + ", marker="
					+ marker + "]";
		}
	}
	
	public static void main(String[] args) {
		 
		var o1 = new TestObject();
		o1.name = "Name 1";
		o1.value = 123;
		o1.selected = true;
		 
		var o2 = new TestObject();
		o2.name = "Name 2";
		o1.value = 546;
		o1.selected = false;
		
		var l = new HashMap<String, ITest>();
		
		l.put("t1", o1);
		l.put("t2", o2);
		System.out.println(l);
		
		var bindBuild = BindingsBuilder.builder().
				withoutFailOnMissingBinds().
				withBinding(MapBindingBuilder.builder(String.class, ITest.class, l).
						withConstruct((k) -> new TestObject()).
						withBinding((k, t) -> ObjectBindingBuilder.builder(ITest.class).
								withBinding(
										xstring("name", t::setName, t::getName).build(),
										xinteger("value", t::setValue, t::getValue).build(),
										xboolean("selected", t::setSelected, t::isSelected).build(),
										xshort("size", t::setSize, t::getSize).build(),
										xbyte("flag", t::setFlag, t::getFlag).build(),
										xlong("time", t::setTime, t::getTime).build(),
										xdouble("ratio", t::setRatio, t::getRatio).build(),
										xfloat("factor", t::setFactor, t::getFactor).build(),
										xchar("marker", t::setMarker, t::getMarker).build()
										).build()).
						build());
		var bind = bindBuild.build();
		
		var storeBuild = ConfigurationStoreBuilder.builder().
				withName("configuration-map").
				withApp("SimjacTest").
				withSerializer(bind).
				withDeserializer(bind);
		
		var store = storeBuild.build();

		store.store();
		
		// or
		l.clear();
		store.retrieve();
		System.out.println(l);
	}
}
