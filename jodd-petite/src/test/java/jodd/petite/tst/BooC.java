// Copyright (c) 2003-present, Jodd Team (jodd.org). All Rights Reserved.

package jodd.petite.tst;

import jodd.petite.meta.PetiteInject;

public class BooC {

	private final Foo foo;

	public BooC() {
		foo = null;
	}
	@PetiteInject
	private BooC(Foo foo) {
		this.foo = foo;
	}

	public Foo getFoo() {
		return foo;
	}
}
