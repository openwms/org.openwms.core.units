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

import com.github.dozermapper.core.DozerConverter;
import com.github.dozermapper.core.MappingException;
import org.openwms.core.units.api.BaseUnit;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.WeightUnit;

import java.util.Arrays;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A BaseUnitConverter.
 *
 * @author Heiko Scherrer
 */
public class BaseUnitConverter extends DozerConverter<BaseUnit, String> {

    public BaseUnitConverter() {
        super(BaseUnit.class, String.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertTo(BaseUnit source, String destination) {
        if (source == null) {
            return null;
        }
        return source.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseUnit convertFrom(String source, BaseUnit destination) {
        if (source == null) {
            return null;
        }
        Optional<PieceUnit> pieceUnit = Arrays.stream(PieceUnit.values()).filter(v -> v.name().equals(source)).findFirst();
        if (pieceUnit.isPresent()) {
            return pieceUnit.get();
        }
        Optional<WeightUnit> weightUnit = Arrays.stream(WeightUnit.values()).filter(v -> v.name().equals(source)).findFirst();
        if (weightUnit.isPresent()) {
            return weightUnit.get();
        }
        throw new MappingException(format("BaseUnit type not supported [%s]", source));
    }
}
