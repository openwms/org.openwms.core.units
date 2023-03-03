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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.openwms.core.units.jsr385.api.WMSUnits;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.Quantity.Scale;
import javax.measure.Unit;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;

import static java.lang.String.format;

/**
 * A QuantityJsonDeserializer is a Jackson JSON Deserializer that takes care of deserializing JSON into {@link Quantity} types.
 *
 * @author bantu
 * @author Heiko Scherrer
 */
public final class QuantityHandlers {

    private QuantityHandlers() { }

    @SuppressWarnings("rawtypes")
    public static class QuantityJsonSerializer extends StdScalarSerializer<Quantity> {

        @Serial
        private static final long serialVersionUID = 1L;

        public QuantityJsonSerializer() {
            super(Quantity.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(Quantity quantity, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (quantity == null) {
                jgen.writeNull();
            }
            else {
                jgen.writeStartObject();
                jgen.writeStringField("value", String.valueOf(quantity.getValue()));
                var symbol = "".equals(quantity.getUnit().getSymbol()) ? ("".equals(quantity.getUnit().toString()) ? "PC" : quantity.getUnit().toString()) : quantity.getUnit().getSymbol();
                var name = quantity.getUnit().getName() == null ? quantity.getUnit().getClass().getName() : quantity.getUnit().getName();
                jgen.writeStringField("unit", symbol + "@" + name);
                jgen.writeEndObject();
            }
        }
    }

    public static class QuantityJsonDeserializer extends StdDeserializer<Quantity<?>> {

        @Serial
        private static final long serialVersionUID = 1L;

        public QuantityJsonDeserializer() {
            super(Quantity.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Quantity<?> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
            var root = jp.readValueAsTree();
            if (root.get("value") == null) {
                throw new JsonParseException(jp, "Value not found for quantity type");
            }
            if (root.get("unit") == null) {
                throw new JsonParseException(jp, "Unit not found for quantity type");
            }
            var codec = jp.getCodec();
            var value = codec.treeToValue(root.get("value"), BigDecimal.class);
            Unit<?> unit = codec.treeToValue(root.get("unit"), Unit.class);
            if (WMSUnits.exists(unit.getSymbol())) {
                var res = WMSUnits.getQuantity(value.doubleValue(), unit.getSymbol());
                if (res.isPresent()) {
                    return res.get();
                }
                throw new JsonParseException(jp, format("Unit cannot be deserialized because it is unknown [%s]", unit.getSymbol()));
            }
            return Quantities.getQuantity(value, unit, Scale.ABSOLUTE);
        }
    }
}
