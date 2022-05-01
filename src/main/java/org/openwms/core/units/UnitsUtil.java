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
package org.openwms.core.units;

import org.hibernate.TypeMismatchException;
import org.openwms.core.units.api.Measurable;
import org.openwms.core.units.api.MetricDimension;
import org.openwms.core.units.api.MetricDimensionUnit;
import org.openwms.core.units.api.Piece;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.Weight;
import org.openwms.core.units.api.WeightUnit;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * A UnitsUtil.
 *
 * @author Heiko Scherrer
 */
public final class UnitsUtil {

    private UnitsUtil() {}

    public static BigDecimal sumPieces(Stream<Piece> pieceStream) {
        return pieceStream
                .map(s -> BigDecimal.valueOf(s.getMagnitude().floatValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Measurable<?, ?, ?> fromString(String quantityType, BigDecimal quantity) {
        final String[] type = quantityType.split("@");
        final String unitType = type[0];
        final String unitTypeClass = type[1];

        if (Piece.class.getCanonicalName().equals(unitTypeClass)) {
            return Piece.of(quantity, PieceUnit.valueOf(unitType));
        } else if (Weight.class.getCanonicalName().equals(unitTypeClass)) {
            return Weight.of(quantity, WeightUnit.valueOf(unitType));
        } else if (MetricDimension.class.getCanonicalName().equals(unitTypeClass)) {
            return MetricDimension.of(quantity, MetricDimensionUnit.M);
        }
        throw new TypeMismatchException(format("Incompatible type: [%s]", unitTypeClass));
    }
}
