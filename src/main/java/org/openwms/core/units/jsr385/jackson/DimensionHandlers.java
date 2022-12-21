/*
 * Units of Measurement Jackson Library
 * Copyright (c) 2005-2022, Werner Keil and others.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-385, Indriya nor the names of their contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openwms.core.units.jsr385.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import tech.units.indriya.unit.UnitDimension;

import javax.measure.Dimension;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A DimensionJsonSerializer is a Jackson JSON Serializer that takes care of serializing {@link Dimension} types into JSON.
 *
 * @author richter
 * @author keilw
 * @author Heiko Scherrer
 */
public final class DimensionHandlers {

    private DimensionHandlers() {}

    public static class DimensionJsonSerializer extends StdScalarSerializer<Dimension> {

        @Serial
        private static final long serialVersionUID = 1L;

        public DimensionJsonSerializer() {
            super(Dimension.class);
        }

        /**
         * {@inheritDoc}
         *
         * Serializes a dimension by serializing its base dimension map.
         * <p>
         * Based on my question and answer at
         * https://stackoverflow.com/questions/48509189/jsr-275-dimension-string-serialization-and-deserialization which might contain better
         * alternatives meanwhile.
         *
         * @param value the dimension to serialize
         * @param gen the generator as provided by {@link JsonSerializer}
         * @param serializers the serializers as provided by {@link JsonSerializer}
         * @throws IOException if an I/O exception occurs
         */
        @Override
        public void serialize(Dimension value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value.getBaseDimensions());
        }
    }

    public static class DimensionJsonDeserializer extends StdScalarDeserializer<Dimension> {

        @Serial
        private static final long serialVersionUID = 1L;

        public DimensionJsonDeserializer() {
            super(Dimension.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Dimension deserialize(JsonParser p, DeserializationContext ctxt)
                throws IOException {
            @SuppressWarnings("unchecked")
            Map<String, Integer> baseDimensionsStrings = p.readValueAs(Map.class);
            var baseDimensions = new HashMap<>(baseDimensionsStrings.entrySet().stream()
                    .collect(Collectors.toMap(entry -> parseBaseDimension(entry.getKey()), Map.Entry::getValue)));
            var retValue = UnitDimension.NONE;
            for (var entry : baseDimensions.entrySet()) {
                int exp = entry.getValue();
                retValue = retValue.multiply(entry.getKey().pow(exp));
            }
            return retValue;
        }

        private static Dimension parseBaseDimension(String symbol) {
            return switch (symbol) {
                case "[N]" -> UnitDimension.AMOUNT_OF_SUBSTANCE;
                case "[I]" -> UnitDimension.ELECTRIC_CURRENT;
                case "[L]" -> UnitDimension.LENGTH;
                case "[J]" -> UnitDimension.LUMINOUS_INTENSITY;
                case "[M]" -> UnitDimension.MASS;
                case "[\u0398]" -> UnitDimension.TEMPERATURE;
                case "[T]" -> UnitDimension.TIME;
                default -> throw new IllegalArgumentException(String.format("dimension symbol [%s] not supported, maybe dimensionless or wrong universe?", symbol));
            };
        }
    }
}
