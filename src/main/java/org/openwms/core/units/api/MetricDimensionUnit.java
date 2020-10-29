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
 * A MetricDimensionUnit.
 *
 * @author Heiko Scherrer
 */
public enum MetricDimensionUnit implements BaseUnit<MetricDimensionUnit>  {

    /** Millimeters. */
    MM(new BigDecimal(.001)),

    /** Centimeters. */
    CM(new BigDecimal(.01)),

    /** Decimeters. */
    DM(new BigDecimal(.1)),

    /** Meters. */
    M(new BigDecimal(1))
    ;

    private BigDecimal magnitude;
    private static MetricDimensionUnit[] all = { MetricDimensionUnit.MM, MetricDimensionUnit.CM, MetricDimensionUnit.DM, MetricDimensionUnit.M };

    /**
     * Create a new {@code MetricDimensionUnit}.
     *
     * @param magnitude The base unit of the MetricDimension
     */
    MetricDimensionUnit(BigDecimal magnitude) {
        this.magnitude = magnitude;
    }

    /**
     * Get the magnitude of this {@code MetricDimensionUnit}.
     *
     * @return the magnitude of the MetricDimension
     */
    public BigDecimal getMagnitude() {
        return this.magnitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MetricDimensionUnit> getAll() {
        return Arrays.asList(all);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetricDimensionUnit getBaseUnit() {
        return M;
    }
}
