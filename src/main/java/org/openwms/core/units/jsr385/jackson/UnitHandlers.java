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
package org.openwms.core.units.jsr385.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.openwms.core.units.jsr385.api.WMSUnits;

import javax.measure.Unit;
import java.io.IOException;
import java.io.Serial;

/**
 * A UnitJsonDeserializer is a Jackson JSON Deserializer that takes care of deserializing JSON into {@link Unit} types.
 *
 * @author Heiko Scherrer
 */
public final class UnitHandlers {

    private UnitHandlers() { }

    @SuppressWarnings("rawtypes")
    public static class UnitJsonSerializer extends StdScalarSerializer<Unit> {

        @Serial
        private static final long serialVersionUID = 1L;

        public UnitJsonSerializer() {
            super(Unit.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(Unit unit, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (unit == null) {
                jgen.writeNull();
            }
            else {
                jgen.writeString(unit.getSymbol() + "@" + unit.getName());
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public static class UnitJsonDeserializer extends StdScalarDeserializer<Unit<?>> {

        @Serial
        private static final long serialVersionUID = 1L;

        public UnitJsonDeserializer() {
            super(Unit.class);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Unit<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            var currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_STRING) {
                var parts = jsonParser.getText().split("@");
                return WMSUnits.resolve(parts[0], parts[1]);
            }
            throw new JsonParseException(jsonParser, "Unit expected as String value");
        }
    }
}