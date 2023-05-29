/**
 * Copyright © 2023 JAdaptive Limited (support@jadaptive.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sshtools.simjac.binding;

import java.util.Optional;

public final class BindingsBuilder {

	private boolean failOnMissingBinds = true;
	private Optional<Binding<?>> binding;

	private BindingsBuilder() {
	}

	public static BindingsBuilder builder() {
		return new BindingsBuilder();
	}

	public <T> BindingsBuilder withBinding(Binding<T> binding) {
		this.binding = Optional.of(binding);
		return this;
	}

	public BindingsBuilder withoutFailOnMissingBinds() {
		return withFailOnMissingBinds(false);
	}

	public BindingsBuilder withFailOnMissingBinds(boolean failOnMissingBinds) {
		this.failOnMissingBinds = failOnMissingBinds;
		return this;
	}

	public Bindings build() {
		return new BindingsImpl(this);
	}

	boolean isFailOnMissingBinds() {
		return failOnMissingBinds;
	}

	Optional<Binding<?>> getBinding() {
		return binding;
	}
}
