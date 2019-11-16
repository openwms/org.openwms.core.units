/*
 * Copyright 2005-2019 the original author or authors.
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

import static org.openwms.core.units.api.WeightUnit.KG;

/**
 * A Weight represents a real world weight, that comes with an <code>Unit</code> and a value.
 * 
 * @author Heiko Scherrer
 */
public class Weight implements Measurable<BigDecimal, Weight, WeightUnit>, Serializable {

    /** The unit of the Weight. */
    private WeightUnit unitType;
    /** The magnitude of the Weight. */
    private BigDecimal magnitude;
    /** Constant for a zero value. */
    public static final Weight ZERO = Weight.of(0);

    /* ----------------------------- methods ------------------- */
    /**
     * Accessed by persistence provider.
     */
    protected Weight() {
        super();
    }

    /**
     * Create a new Weight.
     * 
     * @param magnitude The magnitude of the Weight
     * @param unitType The unit of measure
     */
    private Weight(BigDecimal magnitude, WeightUnit unitType) {
        this.magnitude = magnitude;
        this.unitType = unitType;
    }

    /**
     * Create a new Weight.
     *
     * @param magnitude The magnitude of the Weight
     * @param unitType The unit of measure
     * @return The new instance
     */
    public static Weight of(int magnitude, WeightUnit unitType) {
        return new Weight(new BigDecimal(magnitude), unitType);
    }

    /**
     * Create a new Weight.
     *
     * @param magnitude The magnitude of the Weight as int
     * @return The new instance
     */
    public static Weight of(int magnitude) {
        return new Weight(new BigDecimal(magnitude), KG.getBaseUnit());
    }

    /**
     * Create a new Weight.
     *
     * @param magnitude The magnitude of the Weight
     * @param unitType The unit of measure
     * @return The new instance
     */
    public static Weight of(BigDecimal magnitude, WeightUnit unitType) {
        return new Weight(magnitude, unitType);
    }

    /**
     * Create a new Weight.
     *
     * @param magnitude The magnitude of the Weight as BigDecimal
     * @return The new instance
     */
    public static Weight of(BigDecimal magnitude) {
        return new Weight(magnitude, KG.getBaseUnit());
    }

    /**
     * Returns the magnitude of the Weight.
     * 
     * @return The magnitude
     */
    @Override
    public BigDecimal getMagnitude() {
        return magnitude;
    }

    /**
     * Returns the unit of the Weight.
     * 
     * @return The unit
     */
    @Override
    public WeightUnit getUnitType() {
        return unitType;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isZero() {
        return Weight.ZERO.equals(Weight.of(this.magnitude));
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
    public Weight convertTo(WeightUnit unt) {
        return new Weight(getMagnitude().scaleByPowerOfTen((this.getUnitType().ordinal() - unt.ordinal()) * 3), unt);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Measurable<BigDecimal, Weight, WeightUnit> add(Measurable<BigDecimal, Weight, WeightUnit> other) {
        if (other == null) {
            return Weight.of(this.magnitude, this.unitType);
        }
        if (this.unitType.ordinal() > other.getUnitType().ordinal()) {
            int factor = this.unitType.ordinal() - other.getUnitType().ordinal();
            return Weight.of(this.magnitude.scaleByPowerOfTen(factor * 3).add(other.getMagnitude()), other.getUnitType());
        } else if (this.unitType.ordinal() < other.getUnitType().ordinal()) {
            int factor = other.getUnitType().ordinal() - this.unitType.ordinal();
            return Weight.of(other.getMagnitude().scaleByPowerOfTen(factor * 3).add(this.magnitude), this.unitType);
        }
        return Weight.of(other.getMagnitude().add(this.magnitude), this.unitType);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Measurable<BigDecimal, Weight, WeightUnit> subtract(Measurable<BigDecimal, Weight, WeightUnit> subtrahent) {
        if (subtrahent == null) {
            return Weight.of(this.magnitude, this.unitType);
        }
        if (this.unitType.ordinal() > subtrahent.getUnitType().ordinal()) {
            int factor = this.unitType.ordinal() - subtrahent.getUnitType().ordinal();
            return Weight.of(this.magnitude.scaleByPowerOfTen(factor * 3).subtract(subtrahent.getMagnitude()), subtrahent.getUnitType());
        } else if (this.unitType.ordinal() < subtrahent.getUnitType().ordinal()) {
            int factor = subtrahent.getUnitType().ordinal() - this.unitType.ordinal();
            return Weight.of(this.magnitude.subtract(subtrahent.getMagnitude().scaleByPowerOfTen(factor * 3)), this.unitType);
        }
        return Weight.of(this.magnitude.subtract(subtrahent.getMagnitude()), this.unitType);
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public int compareTo(Weight o) {
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
     * Use amount and unit for calculation.
     * 
     * @return The hashCode
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMagnitude() == null) ? 0 : getMagnitude().hashCode());
        result = prime * result + ((getUnitType() == null) ? 0 : getUnitType().hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * Use amount and unit for comparison.
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
        Weight other = (Weight) obj;
        return 0 == other.compareTo(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getMagnitude() + " " + getUnitType();
    }
}