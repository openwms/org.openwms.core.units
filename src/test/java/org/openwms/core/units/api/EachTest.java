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

import org.junit.jupiter.api.Test;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A EachTest.
 *
 * @author Heiko Scherrer
 */
class EachTest {

    @Test
    void compare_eaches_with_dozens() {
        var TWELVE_EACHES = Piece.of(12);
        var ONE_DOZEN = Dozen.of(1);
        assertThat(ONE_DOZEN).isEqualTo(TWELVE_EACHES);

        var ONE_EACH = Piece.of(1);
        assertThat(ONE_EACH.add(ONE_DOZEN)).isEqualTo(Piece.of(13));
    }

    static class Piece {
        static Quantity<Dimensionless> of(double amount) {
            return Quantities.getQuantity(amount, AbstractUnit.ONE);
        }
    }

    static class Dozen {
        static Quantity<Dimensionless> of(double amount) {
            return Quantities.getQuantity(12 * amount, AbstractUnit.ONE);
        }
    }
}