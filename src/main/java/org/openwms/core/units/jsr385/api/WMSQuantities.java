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
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Volume;

import static javax.measure.MetricPrefix.MILLI;
import static tech.units.indriya.unit.Units.LITRE;

/**
 * A WMSQuantities.
 *
 * @author Heiko Scherrer
 */
public final class WMSQuantities {

    private WMSQuantities() {}

    /**
     * Find and return a supported quantity of the given {@code unit} with the {@code amount}.
     *
     * @param value The amount of the quantity
     * @return A new instance
     * @throws UnitConversionException In case the unit is not supported or {@literal null}
     */
    public static Quantity<Volume> millilitres(double value) {
        return Quantities.getQuantity(value, MILLI(LITRE));
    }
}
