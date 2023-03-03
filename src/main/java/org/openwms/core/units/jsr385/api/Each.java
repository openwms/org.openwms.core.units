/*
 * Copyright 2005-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.core.units.jsr385.api;

import tech.units.indriya.AbstractQuantity;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import java.io.Serializable;

/**
 * A Each represents a dimensionless unit.
 *
 * @author Heiko Scherrer
 */
public final class Each<Q extends Quantity<Q>> extends AbstractQuantity<Q> implements Serializable {

    public static final Unit EACH_UNIT = new EachUnit();//new BaseUnit<>("PC", "Each", AbstractQuantity.ONE.getUnit().getDimension());
    private final Quantity<Q> delegate;

    private Each(Quantity<Q> delegate) {
        super(new EachUnit<Q>());
        this.delegate = delegate;
    }

    /**
     * Create a new Each instance with the given {@code amount}.
     *
     * @param amount The amount of the Each
     * @return A new instance
     */
    public static Quantity<Dimensionless> of(double amount) {
        return new Each<>(Quantities.getQuantity(amount, EACH_UNIT));
    }

    @Override
    public Number getValue() {
        return delegate.getValue();
    }

    @Override
    public ComparableQuantity<Q> add(Quantity<Q> that) {
        return new Each(Quantities.getQuantity(delegate.add(that).getValue(), EACH_UNIT));
    }

    @Override
    public ComparableQuantity<Q> subtract(Quantity<Q> that) {
        return new Each(Quantities.getQuantity(delegate.subtract(that).getValue(), EACH_UNIT));
    }

    @Override
    public ComparableQuantity<?> divide(Quantity<?> that) {
        return new Each<>(Quantities.getQuantity(delegate.divide(that).getValue(), EACH_UNIT));
    }

    @Override
    public ComparableQuantity<Q> divide(Number that) {
        return new Each(Quantities.getQuantity(delegate.divide(that).getValue(), EACH_UNIT));
    }

    @Override
    public ComparableQuantity<?> multiply(Quantity<?> multiplier) {
        return new Each<>(Quantities.getQuantity(delegate.multiply(multiplier).getValue(), EACH_UNIT));
    }

    @Override
    public ComparableQuantity<Q> multiply(Number multiplier) {
        return new Each(Quantities.getQuantity(delegate.multiply(multiplier).getValue(), EACH_UNIT));
    }

    @Override
    public ComparableQuantity<?> inverse() {
        return new Each<>(delegate.inverse());
    }

    @Override
    public Quantity<Q> negate() {
        return new Each(Quantities.getQuantity(delegate.negate().getValue(), EACH_UNIT));
    }
}
