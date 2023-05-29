# simjac

A small Java library used by various other JAdaptive projects that can be used to bind a 
JSON structure to Java objects for the purposes of application configuration. 

Rather than using reflection, it uses a builder pattern along with `Supplier<?>` and `Consumer<?>`
to model the binding. 

 * Uses no reflection, so ideal for configuration free usage with Graal native images.
 * Finds the best location to place to JSON based on OS (currently has special behaviour for Windows, Linux and Mac OS.
 * `USER` or `GLOBAL` scopes, or `CUSTOM` locations.
 * A single JSON object's attributes may be bound to multiple unrelated attributes of Java objects,
   useful for binding JSON data to JavaFX UI for example.
 
## TODO

 * Binding of `List`, `Map` and complex object types.
 * Custom storage backends.
 
## Example

```java
package test;
import static com.sshtools.simjac.binding.AttrBindBuilder.*;
import com.sshtools.simjac.store.ConfigurationStoreBuilder;

public class Test {
	
	public static void main(String[] args) {
		var t = new TestObject();
		var store = ConfigurationStoreBuilder.builder().
				withName("configuration").
				withApp("SimjacTest").
				withoutFailOnMissingBinds().
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
						).
				build();

		store.store();
		
		// or

		store.retrieve();
	}
	
	static class TestObject {
		private String name;
		private int value;
		private boolean selected;
		private short size;
		private byte flag;
		private long time;
		private double ratio;
		private float factor;
		private char marker;
		
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
}

```