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

import org.dozer.DozerConverter;
import org.dozer.MappingException;
import org.openwms.core.units.api.Measurable;
import org.openwms.core.units.api.MeasurableString;
import org.openwms.core.units.api.Piece;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.Weight;
import org.openwms.core.units.api.WeightUnit;

import java.util.Arrays;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A MeasurableStringConverter.
 *
 * @author Heiko Scherrer
 */
public class MeasurableStringConverter extends DozerConverter<Measurable, MeasurableString> {

    public MeasurableStringConverter() {
        super(Measurable.class, MeasurableString.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeasurableString convertTo(Measurable source, MeasurableString destination) {
        if (source == null) {
            return null;
        }
        return new MeasurableString(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurable convertFrom(MeasurableString source, Measurable destination) {
        if (source == null) {
            return null;
        }
        Optional<PieceUnit> pieceUnit = Arrays.stream(PieceUnit.values()).filter(v -> v.name().equals(source.getUnit())).findFirst();
        if (pieceUnit.isPresent()) {
            return Piece.of(Integer.parseInt(source.getAmount()), pieceUnit.get());
        }
        Optional<WeightUnit> weightUnit = Arrays.stream(WeightUnit.values()).filter(v -> v.name().equals(source.getUnit())).findFirst();
        if (weightUnit.isPresent()) {
            return Weight.of(Integer.parseInt(source.getAmount()), weightUnit.get());
        }
        throw new MappingException(format("Type not supported [%s]", source));
    }
}
