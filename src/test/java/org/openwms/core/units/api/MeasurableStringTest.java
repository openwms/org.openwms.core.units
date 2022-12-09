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
package org.openwms.core.units.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.openwms.core.units.api.PieceUnit.PC;

/**
 * A MeasurableStringTest.
 *
 * @author Heiko Scherrer
 */
class MeasurableStringTest {

    @Test
    void shall_throw_creation_without_null() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new MeasurableString((Measurable) null));
    }

    @Test
    void shall_create_with_measurable() throws Exception {
        MeasurableString pc = new MeasurableString(Piece.of(1));
        assertThat(pc.getAmount()).isEqualTo("1");
        assertThat(pc.getUnit()).isEqualTo(PC.name());
    }

    @Test
    void shall_throw_without_2_parts() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new MeasurableString("1"));
    }

    @Test
    void shall_throw_without_number() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> new MeasurableString("FOO BAR"));
    }

    @Test
    void shall_serialized_as_String() throws Exception {
        MeasurableString pc = new MeasurableString("1 PC");
        assertThat(pc.toString()).isEqualTo("1 PC");

        ObjectMapper om = new ObjectMapper();
        String string = om.writeValueAsString(pc);
        assertThat(string).isEqualTo("\"1 PC\"");
    }
}