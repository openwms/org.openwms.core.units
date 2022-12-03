/*
 * Copyright 2005-2020 the original author or authors.
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
package org.openwms.core.units.api;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * A WeightUnit is a concrete set of all possible weights.
 * <p>
 * In SI format.
 * </p>
 * 
 * @author Heiko Scherrer
 */
public enum WeightUnit implements BaseUnit<WeightUnit> {

    /** Milligram. */
    MG(new BigDecimal("0.001")),
    /** Gram. */
    G(new BigDecimal(1)),
    /** Kilogram. */
    KG(new BigDecimal(1000)),
    /** Tons. */
    T(new BigDecimal(1_000_000));

    private final BigDecimal magnitude;
    private static final WeightUnit[] all = { WeightUnit.MG, WeightUnit.G, WeightUnit.KG, WeightUnit.T };

    /**
     * Create a new {@code WeightUnit}.
     * 
     * @param magnitude The base unit of the WeightUnit
     */
    WeightUnit(BigDecimal magnitude) {
        this.magnitude = magnitude;
    }

    /**
     * Get the magnitude of this {@code WeightUnit}.
     * 
     * @return the magnitude
     */
    public BigDecimal getMagnitude() {
        return this.magnitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WeightUnit> getAll() {
        return Arrays.asList(all);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WeightUnit getBaseUnit() {
        return G;
    }
}