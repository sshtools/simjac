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
package com.sshtools.simjac;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ObjectBindingBuilder<T> extends AbstractBindBuilder<ObjectBinding<T>, T, ObjectBindingBuilder<T>> {

	private Map<String, AttrBinding<?>> bindings = new HashMap<>();

	private ObjectBindingBuilder(Class<T> type) {
	}

	public static <T> ObjectBindingBuilder<T> builder(Class<T> type) {
		return new ObjectBindingBuilder<>(type);
	}

	public ObjectBindingBuilder<T> withBinding(AttrBinding<?>... bindings) {
		for (var b : bindings) {
			this.bindings.put(b.name(), b);
		}
		return this;
	}

	@Override
	public ObjectBinding<T> build() {
		var b = Collections.unmodifiableMap(this.bindings);
		return new ObjectBinding<>() {
			@Override
			public Map<String, AttrBinding<?>> bindings() {
				return b;
			}
		};
	}

}
