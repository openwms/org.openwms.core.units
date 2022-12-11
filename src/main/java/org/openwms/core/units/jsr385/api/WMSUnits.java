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
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import java.util.Objects;

import static java.lang.String.format;

/**
 * A WMSUnits.
 *
 * @author Heiko Scherrer
 */
public class WMSUnits {

    public static final Unit<Dimensionless> EACH = AbstractUnit.ONE;
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
        } else if (Dozen.class.getCanonicalName().equals(unitType) && "DOZ".equals(unit)) {
            return true;
        }
        return false;
    }

    /**
     * Parse the given {@code unit} string into a supported Unit type.
     *
     * @param unit As string
     * @return The Unit instance
     * @throws NullPointerException In case the unit {@literal null}
     * @throws UnitConversionException In case the unit is not supported
     */
    public static Unit<?> parse(String unit) {
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
    public static Quantity<Dimensionless> getQuantity(double value, String unit) {
        if ("PC".equals(unit)) {
            return Each.of(value);
        } else if ("DOZ".equals(unit)) {
            return Dozen.of(value);
        }
        throw new UnitConversionException(format("Not a supported unit [%s]", unit));
    }

    /**
     * Get a quantity with zero amount and the given {@code unit}.
     *
     * @param unit The unit of the quantity
     * @return A new instance
     * @param <Q> Type of unit
     */
    public static <Q extends Quantity<Q>> ComparableQuantity<Q> ZERO(Unit<Q> unit) {
        return Quantities.getQuantity(0, unit);
    }
}
