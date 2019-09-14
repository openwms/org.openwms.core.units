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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A Piece.
 * 
 * @author Heiko Scherrer
 */
public class Piece implements Measurable<BigDecimal, Piece, PieceUnit>, UnitType, Serializable {

    private static final BigDecimal SHIFTER = new BigDecimal(12);

    /** The unit of the Piece. */
    private PieceUnit unitType;
    /** The magnitude of the Piece. */
    private BigDecimal magnitude;
    /** Constant for a zero value. */
    public static final Piece ZERO = new Piece(0);

    /* ----------------------------- methods ------------------- */
    /**
     * Accessed by persistence provider.
     */
    protected Piece() {
        super();
    }

    /**
     * @see UnitType#getMeasurable()
     */
    @Override
    public Piece getMeasurable() {
        return this;
    }

    /**
     * Create a new Piece.
     * 
     * @param magnitude
     *            The magnitude of the Piece
     * @param unitType
     *            The unit of measure
     */
    public Piece(int magnitude, PieceUnit unitType) {
        this.magnitude = new BigDecimal(magnitude);
        this.unitType = unitType;
    }

    /**
     * Create a new Piece.
     * 
     * @param magnitude
     *            The magnitude of the Piece as int
     */
    public Piece(int magnitude) {
        this.magnitude = new BigDecimal(magnitude);
        this.unitType = PieceUnit.PC.getBaseUnit();
    }

    /**
     * Create a new Piece.
     * 
     * @param magnitude
     *            The magnitude of the Piece
     * @param unitType
     *            The unit of measure
     */
    public Piece(BigDecimal magnitude, PieceUnit unitType) {
        this.magnitude = magnitude;
        this.unitType = unitType;
    }

    /**
     * Create a new Piece.
     * 
     * @param magnitude
     *            The magnitude of the Piece as BigDecimal
     */
    public Piece(BigDecimal magnitude) {
        this.magnitude = magnitude;
        this.unitType = PieceUnit.PC.getBaseUnit();
    }

    /**
     * Returns the magnitude of the Piece.
     * 
     * @return The magnitude
     */
    @Override
    public BigDecimal getMagnitude() {
        return magnitude;
    }

    /**
     * Returns the unit of the Piece.
     * 
     * @return The unit
     */
    @Override
    public PieceUnit getUnitType() {
        return unitType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isZero() {
        return Piece.ZERO.equals(new Piece(this.getMagnitude(), PieceUnit.DOZ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNegative() {
        return this.getMagnitude().signum() == -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Piece convertTo(PieceUnit unt) {
        if (PieceUnit.PC == unt && this.getUnitType() == PieceUnit.DOZ) {
            return new Piece(this.getMagnitude().multiply(SHIFTER), PieceUnit.PC);
        } else if (PieceUnit.DOZ == unt && this.getUnitType() == PieceUnit.PC) {
            return new Piece(this.getMagnitude().divide(SHIFTER, 0, RoundingMode.DOWN), PieceUnit.DOZ);
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Piece o) {
        if (null == o) {
            return -1;
        }
        if (o.getUnitType().ordinal() > this.getUnitType().ordinal()) {
            return compare(this.getMagnitude(), o.getMagnitude().multiply(SHIFTER));
        } else if (o.getUnitType().ordinal() < this.getUnitType().ordinal()) {
            return compare(this.getMagnitude().multiply(SHIFTER), o.getMagnitude());
        }
        return this.getMagnitude().compareTo(o.getMagnitude());
    }

    /**
     * {@inheritDoc}
     * 
     * Return a combination of amount and unit, e.g. 24 PC
     */
    @Override
    public String toString() {
        return getMagnitude() + " " + getUnitType();
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
        Piece other = (Piece) obj;
        if (magnitude == null && other.magnitude != null) {
            return false;
        }
        return this.compareTo(other) == 0;

    }

    private int compare(BigDecimal val1, BigDecimal val2) {
        if (val1.equals(val2)) {
            return 0;
        }
        return val1.compareTo(val2);
    }
}