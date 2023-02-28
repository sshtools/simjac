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

import java.util.ArrayList;

public class Test {
	
	static class TestObject {
		private String name;
		private int value;
		private boolean selected;
		private short size;
		private byte flag;
		private long time;
		private double ratio;
		private float factor;
		private char marker = '*';
		
		public byte getFlag() {
			return flag;
		}

		public void setFlag(byte flag) {
			this.flag = flag;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

		public double getRatio() {
			return ratio;
		}

		public void setRatio(double ratio) {
			this.ratio = ratio;
		}

		public float getFactor() {
			return factor;
		}

		public void setFactor(float factor) {
			this.factor = factor;
		}

		public char getMarker() {
			return marker;
		}

		public void setMarker(char marker) {
			this.marker = marker;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public void setValue(int value) {
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		
		public int getValue() {
			return value;
		}
		
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public boolean isSelected() {
			return selected;
		}
		
		public void setSize(short size) {
			this.size = size;
		}
		
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
		 
		var o2 = new TestObject();
		o2.name = "Name 2";
		
		var l = new ArrayList<TestObject>();
		
		l.add(o1);
		l.add(o2);
		System.out.println(l);
		
		var bindBuild = BindingsBuilder.builder().
				withoutFailOnMissingBinds().
				withBinding(ArrayBindingBuilder.builder(TestObject.class, l).
						withConstruct(() -> new TestObject()).
						withBinding((t) -> ObjectBindingBuilder.builder(TestObject.class).
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
				withName("configuration").
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
