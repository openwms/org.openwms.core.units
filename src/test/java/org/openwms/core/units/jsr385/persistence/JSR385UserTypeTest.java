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
package org.openwms.core.units.jsr385.persistence;

import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openwms.core.units.jsr385.api.Dozen;
import org.openwms.core.units.jsr385.api.Each;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

/**
 * A JSR385UserTypeTest.
 *
 * @author Heiko Scherrer
 */
class JSR385UserTypeTest {

    @Test void persist_dozens_as_eaches() throws SQLException {
        // arrange
        var ps = Mockito.mock(PreparedStatement.class);
        var session = Mockito.mock(SharedSessionContractImplementor.class);
        var testee = new JSR385UserType();

        // act
        testee.nullSafeSet(ps, Dozen.of(3), 0, session);

        // assert
        verify(ps).setString(0, "PC@org.openwms.core.units.jsr385.api.Each");
        verify(ps).setString(1, "36");
    }

    @Test void persist_eaches_as_eaches() throws SQLException {
        // arrange
        var ps = Mockito.mock(PreparedStatement.class);
        var session = Mockito.mock(SharedSessionContractImplementor.class);
        var testee = new JSR385UserType();

        // act
        testee.nullSafeSet(ps, Each.of(3), 0, session);

        // assert
        verify(ps).setString(0, "PC@org.openwms.core.units.jsr385.api.Each");
        verify(ps).setString(1, "3");
    }

    @Test void persist_with_unsupported_unit() {
        // arrange
        var ps = Mockito.mock(PreparedStatement.class);
        var session = Mockito.mock(SharedSessionContractImplementor.class);
        var testee = new JSR385UserType();
        var qty = Quantities.getQuantity(1, Units.CELSIUS);

        // act
        assertThrows(TypeMismatchException.class, () -> testee.nullSafeSet(ps, qty, 0, session));
    }
}
