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

import org.openwms.core.units.api.UnitConversionException;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.function.Calculus;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.ProductUnit;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A WMSUnits.
 *
 * @author Heiko Scherrer
 */
public class WMSUnits {

    public static final Unit<Dimensionless> EACH = new ProductUnit(AbstractUnit.ONE);//<Dimensionless>.AbstractUnit.ONE;
    public static final Unit<Dimensionless> DOZEN = AbstractUnit.ONE;

    private WMSUnits() { }

    /**
     * Check whether the given {@code unit} string is a valid and supported Unit.
     *
     * @param unit As string
     * @return {@literal true} if supported
     */
    public static boolean exists(String unit) {
        return "PC".equals(unit) || "DOZ".equals(unit);
    }

    /**
     * Check whether the given {@code unit} string is a valid and supported Unit.
     *
     * @param unit As string
     * @return {@literal true} if supported
     */
    public static boolean exists(String unit, String unitType) {
        if (Each.class.getCanonicalName().equals(unitType) && "PC".equals(unit)) {
            return true;
        }
        return Dozen.class.getCanonicalName().equals(unitType) && "DOZ".equals(unit);
    }

    public static Unit resolve(String unit, String shortUnitType) {
        if (Each.class.getSimpleName().equals(shortUnitType) && "PC".equals(unit)) {
            return Each.of(0).getUnit();
        }
        else if(Dozen.class.getSimpleName().equals(shortUnitType) && "DOZ".equals(unit)) {
            return Dozen.of(0).getUnit();
        }
        throw new UnitConversionException(format("Not a supported unit [%s / %s]", unit, shortUnitType));
    }
    public static Unit<?> resolve(String unitType) {
        if (Each.class.getCanonicalName().equals(unitType)) {
            return Each.of(0).getUnit();
        } else if (Dozen.class.getCanonicalName().equals(unitType)) {
            return Dozen.of(0).getUnit();
        }
        return null;
    }

    /**
     * Parse the given {@code unit} string into a supported Unit type.
     *
     * @param unit As string
     * @return The Unit instance
     * @throws NullPointerException In case the unit {@literal null}
     * @throws UnitConversionException In case the unit is not supported
     */
    @SuppressWarnings("rawtypes")
    public static Unit parse(String unit) {
        Objects.requireNonNull(unit, "Unit must not be null");
        if (exists(unit)) {
            return AbstractUnit.ONE;
        }
        throw new UnitConversionException(format("Not a supported unit [%s]", unit));
    }

    /**
     * Find and return a supported quantity of the given {@code unit} with the {@code amount}.
     *
     * @param value The amount of the quantity
     * @param unit The unit of the quantity
     * @return A new instance
     * @throws UnitConversionException In case the unit is not supported or {@literal null}
     */
    public static Optional<Quantity<?>> getQuantity(double value, String unit) {
        if ("PC".equals(unit)) {
            return Optional.of(Each.of(value));
        } else if ("DOZ".equals(unit)) {
            return Optional.of(Dozen.of(value));
        }
        return Optional.empty();
    }

    /**
     * Get a quantity with zero amount and the given {@code unit}.
     *
     * @param unit The unit of the quantity
     * @return A new instance
     * @param <Q> Type of unit
     */
    public static <Q extends Quantity<Q>> Quantity<Q> ZERO(Unit<Q> unit) {
        return Quantities.getQuantity(0, unit);
    }

    public static Optional<Quantity<?>> getQuantity(String quantity) {
        var parts = quantity.split(" ");
        if (parts.length != 2) {
            return Optional.empty();
        }
        return WMSUnits.getQuantity(Double.parseDouble(parts[0]), parts[1]);
    }

    /**
     * Get a quantity with zero amount and the given {@code unit}.
     *
     * @param unit The unit of the quantity
     * @return A new instance
     * @param <Q> Type of unit
     */
    public static <Q extends Quantity<Q>> boolean isNegative(Quantity<Q> quantity) {
        return NumberUtils.signum(quantity.getValue()) == -1;
    }

    /**
     * Get a quantity with zero amount and the given {@code unit}.
     *
     * @param unit The unit of the quantity
     * @return A new instance
     * @param <Q> Type of unit
     */
    public static <Q extends Quantity<Q>> boolean isZeroOrNegative(Quantity<Q> quantity) {
        var res = NumberUtils.signum(quantity.getValue());
        return res == -1 || res == 0;
    }

    /**
     * Optionally converts given {@link Quantity} to desired {@link Unit},
     * based on whether both units are compatible.
     */
    @SuppressWarnings("unchecked")
    public static <Q extends Quantity<Q>> Optional<Quantity<Q>> asUnit(Quantity<?> quantity, Unit<Q> unit) {
        return quantity.getUnit().isCompatible(unit)
                ? Optional.of(((Quantity<Q>)quantity).to(unit))
                : Optional.empty();
    }

    public static boolean isComparable(Quantity<?> quantity, Quantity<?> withQuantity) {
        return quantity.getUnit().isCompatible(withQuantity.getUnit());
    }

    public static int compareQuantities(Quantity qty1, Quantity qty2) {
        if (qty1.getUnit().equals(qty2.getUnit())) {
            return Calculus.currentNumberSystem().compare(qty1.getValue(), qty2.getValue());
        }
        if (!isComparable(qty1, qty2)) {
            throw new UnitConversionException(format("Units not comparable [%s] [%s]", qty1, qty2));
        }
        return Calculus.currentNumberSystem().compare(qty1.getValue(), qty2.to(qty1.getUnit()).getValue());
    }
}
