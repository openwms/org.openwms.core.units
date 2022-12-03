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
package org.openwms.core.units.converter;

import org.openwms.core.units.api.BaseUnit;
import org.openwms.core.units.api.Measurable;
import org.openwms.core.units.api.MeasurableString;
import org.openwms.core.units.api.MetricDimension;
import org.openwms.core.units.api.MetricDimensionUnit;
import org.openwms.core.units.api.Piece;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.Weight;
import org.openwms.core.units.api.WeightUnit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.openwms.core.units.api.MetricDimensionUnit.M;
import static org.openwms.core.units.api.PieceUnit.PC;
import static org.openwms.core.units.api.WeightUnit.G;

/**
 * A Units.
 *
 * @author Heiko Scherrer
 */
public final class Units {

    public static Collection<BaseUnit<?>> getAllUnits() {
        List<BaseUnit<?>> result = new ArrayList<>();
        result.addAll(PC.getAll());
        result.addAll(G.getAll());
        result.addAll(M.getAll());
        return result;
    }

    public static Optional<BaseUnit<?>> getUnit(String name) {
        return Units.getAllUnits().stream().filter(f -> f.name().equalsIgnoreCase(name)).findFirst();
    }

    public static BigDecimal getBigDecimalMagnitude(Number magnitude) {
        if (magnitude instanceof BigDecimal asBigDecimal) {
            return asBigDecimal;
        }
        return BigDecimal.valueOf(magnitude.doubleValue());
    }

    public static Optional<Measurable> getMeasurableOptional(String name) {
        Measurable<?, ?, ?> value = null;
        try {
            value = getMeasurable(name);
        } catch (Exception e) {
            // be fine here and omit Exceptions
        }
        return Optional.ofNullable(value);
    }

    public static Measurable<?, ?, ?> getMeasurable(String name) {
        return name == null ? null : getMeasurable(new MeasurableString(name));
    }

    public static Measurable<?, ?, ?> getMeasurable(MeasurableString source) {
        return source == null ? null : getMeasurable(source.getAmount(), source.getUnit());
    }

    public static Measurable<?, ?, ?> getMeasurable(String amount, String unit) {
        if (amount == null) {
            throw new IllegalArgumentException("amount is null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit is null");
        }
        return getMeasurable(new BigDecimal(amount), unit);
    }

    public static Measurable<?, ?, ?> getMeasurable(BigDecimal amount, String unit) {

        if (amount == null) {
            throw new IllegalArgumentException("amount is null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("unit is null");
        }

        var pieceUnit = Arrays.stream(PieceUnit.values())
                .filter(v -> v.name().equals(unit))
                .findFirst();
        if (pieceUnit.isPresent()) {
            return Piece.of(amount, pieceUnit.get());
        }

        var weightUnit = Arrays.stream(WeightUnit.values())
                .filter(v -> v.name().equals(unit))
                .findFirst();
        if (weightUnit.isPresent()) {
            return Weight.of(amount, weightUnit.get());
        }

        var metricDimensionUnit = Arrays.stream(MetricDimensionUnit.values())
                .filter(v -> v.name().equals(unit))
                .findFirst();
        if (metricDimensionUnit.isPresent()) {
            return MetricDimension.of(amount, metricDimensionUnit.get());
        }

        throw new IllegalArgumentException(format("Unit type not supported [%s]", unit));
    }
}
