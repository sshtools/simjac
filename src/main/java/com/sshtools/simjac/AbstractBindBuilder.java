package com.sshtools.simjac;

public abstract class AbstractBindBuilder<B extends Binding<?>, T, Z extends AbstractBindBuilder<B, T, Z>> {

	public abstract B build();
}