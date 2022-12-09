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
package org.openwms.core.units.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.openwms.core.units.api.MetricDimensionUnit.M;

/**
 * A MetricDimension represents metric dimensional units, like meters.
 * 
 * @author Heiko Scherrer
 */
public class MetricDimension implements Measurable<BigDecimal, MetricDimension, MetricDimensionUnit>, Serializable {

    /** The unit of the MetricDimension. */
    private MetricDimensionUnit unitType;
    /** The magnitude of the MetricDimensionUnit. */
    private BigDecimal magnitude;
    /** Constant for a zero value. */
    public static final MetricDimension ZERO = MetricDimension.of(0);

    /* ----------------------------- constructors ------------------- */
    /** Accessed by persistence provider. */
    protected MetricDimension() {
        super();
    }

    /**
     * Create a new MetricDimension.
     *
     * @param magnitude The magnitude
     * @param unitType The unit of measure
     */
    private MetricDimension(BigDecimal magnitude, MetricDimensionUnit unitType) {
        this.magnitude = magnitude;
        this.unitType = unitType;
    }

    /**
     * Create a new MetricDimension.
     * 
     * @param magnitude The magnitude
     * @param unitType The unit of measure
     * @return The new instance
     */
    public static MetricDimension of(int magnitude, MetricDimensionUnit unitType) {
        return new MetricDimension(new BigDecimal(magnitude), unitType);
    }

    /**
     * Create a new MetricDimension.
     * 
     * @param magnitude The magnitude
     * @return The new instance
     */
    public static MetricDimension of(int magnitude) {
        return new MetricDimension(new BigDecimal(magnitude), M.getBaseUnit());
    }

    /**
     * Create a new MetricDimension.
     * 
     * @param magnitude The magnitude
     * @param unitType The unit of measure
     * @return The new instance
     */
    public static MetricDimension of(BigDecimal magnitude, MetricDimensionUnit unitType) {
        return new MetricDimension(magnitude, unitType);
    }

    /**
     * Create a new MetricDimension.
     * 
     * @param magnitude The magnitude
     * @return The new instance
     */
    public static MetricDimension of(BigDecimal magnitude) {
        return new MetricDimension(magnitude, M.getBaseUnit());
    }

    /* ----------------------------- methods ------------------- */
    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getMagnitude() {
        return magnitude;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MetricDimensionUnit getUnitType() {
        return unitType;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isZero() {
        return MetricDimension.ZERO.equals(new MetricDimension(this.getMagnitude(), M));
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isNegative() {
        return this.getMagnitude() == null || this.getMagnitude().signum() == -1;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public MetricDimension convertTo(MetricDimensionUnit unt) {
        return new MetricDimension(getMagnitude().scaleByPowerOfTen((this.getUnitType().ordinal() - unt.ordinal()) * 1), unt);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Measurable<BigDecimal, MetricDimension, MetricDimensionUnit> add(Measurable<BigDecimal, MetricDimension, MetricDimensionUnit> other) {
        if (other == null || other == ZERO) {
            return MetricDimension.of(this.magnitude, this.unitType);
        }
        if (this.unitType.ordinal() > other.getUnitType().ordinal()) {
            int factor = this.unitType.ordinal() - other.getUnitType().ordinal();
            return MetricDimension.of(this.magnitude.scaleByPowerOfTen(factor * 3).add(other.getMagnitude()), other.getUnitType());
        } else if (this.unitType.ordinal() < other.getUnitType().ordinal()) {
            int factor = other.getUnitType().ordinal() - this.unitType.ordinal();
            return MetricDimension.of(other.getMagnitude().scaleByPowerOfTen(factor * 3).add(this.magnitude), this.unitType);
        }
        return MetricDimension.of(other.getMagnitude().add(this.magnitude), this.unitType);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Measurable<BigDecimal, MetricDimension, MetricDimensionUnit> subtract(Measurable<BigDecimal, MetricDimension, MetricDimensionUnit> subtrahent) {
        if (subtrahent == null || subtrahent == ZERO) {
            return MetricDimension.of(this.magnitude, this.unitType);
        }
        if (this.unitType.ordinal() > subtrahent.getUnitType().ordinal()) {
            int factor = this.unitType.ordinal() - subtrahent.getUnitType().ordinal();
            return MetricDimension.of(this.magnitude.scaleByPowerOfTen(factor * 3).subtract(subtrahent.getMagnitude()), subtrahent.getUnitType());
        } else if (this.unitType.ordinal() < subtrahent.getUnitType().ordinal()) {
            int factor = subtrahent.getUnitType().ordinal() - this.unitType.ordinal();
            return MetricDimension.of(this.magnitude.subtract(subtrahent.getMagnitude().scaleByPowerOfTen(factor * 3)), this.unitType);
        }
        return MetricDimension.of(this.magnitude.subtract(subtrahent.getMagnitude()), this.unitType);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public int compareTo(MetricDimension o) {
        if (o.getUnitType().ordinal() > this.getUnitType().ordinal()) {
            int factor = o.getUnitType().ordinal() - this.getUnitType().ordinal();
            return this.magnitude.compareTo(o.magnitude.scaleByPowerOfTen(factor * 3));
        } else if (o.getUnitType().ordinal() < this.getUnitType().ordinal()) {
            int factor = this.getUnitType().ordinal() - o.getUnitType().ordinal();
            return this.magnitude.scaleByPowerOfTen(factor * 3).compareTo(o.magnitude);
        } else {
            return this.magnitude.compareTo(o.magnitude);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * Uses magnitude and unitType for calculation.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((magnitude == null) ? 0 : magnitude.hashCode());
        result = prime * result + ((unitType == null) ? 0 : unitType.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * Uses magnitude and unitType for comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MetricDimension other = (MetricDimension) obj;
        if (magnitude == null && other.magnitude != null) {
            return false;
        }
        return this.compareTo(other) == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return asString();
    }
}