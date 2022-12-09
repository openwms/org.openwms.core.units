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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import systems.uom.ucum.format.UCUMFormat;

import javax.measure.Unit;
import java.io.IOException;
import java.io.Serial;
import java.text.ParsePosition;

/**
 * A UnitJsonDeserializer is a Jackson JSON Deserializer that takes care of deserializing JSON into {@link Unit} types.
 *
 * @author Heiko Scherrer
 */
public class UnitJsonDeserializer extends StdScalarDeserializer<Unit<?>> {

    @Serial
    private static final long serialVersionUID = -6327531740958676293L;

    public UnitJsonDeserializer() {
        super(Unit.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Unit<?> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            return UCUMFormat.getInstance(UCUMFormat.Variant.CASE_SENSITIVE).parse(jsonParser.getText(), new ParsePosition(0));
        }
        throw deserializationContext.wrongTokenException(jsonParser, (JavaType) null, JsonToken.VALUE_STRING, "Expected unit value in String format");
    }
}