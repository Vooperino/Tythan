package com.github.archemedes.tythan.agnostic;

import com.github.archemedes.tythan.agnostic.abstracts.AbstractKyoriComponentBuilder;

public class CommonKyoriComponentBuilder extends AbstractKyoriComponentBuilder<CommonKyoriComponentBuilder> {
    public CommonKyoriComponentBuilder() {super("");}
    public CommonKyoriComponentBuilder(String initial) {super(initial);}
    @Override public CommonKyoriComponentBuilder getThis() {return this;}
}
