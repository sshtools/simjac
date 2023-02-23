package com.sshtools.simjac;

import java.io.Closeable;

public interface ConfigurationStore extends Closeable {

	void store();

	void retrieve();
	
    void close();

}
