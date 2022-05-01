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
package org.openwms.core.units.api;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openwms.core.units.api.PieceUnit.DOZ;
import static org.openwms.core.units.api.PieceUnit.PC;

/**
 * A PieceTest.
 * 
 * @author Heiko Scherrer
 */
class PieceTest {

    @Test void testComparison() {
        assertThat(Piece.of(50, PC).compareTo(null)).isEqualTo(1);
        assertThat(Piece.of(50, PC).compareTo(Piece.of(30))).isEqualTo(1);
        assertThat(Piece.of(30).compareTo(Piece.of(50, PC))).isEqualTo(-1);
        assertThat(Piece.of(30).compareTo(Piece.of(30))).isEqualTo(0);
    }

    @Test void test_for_ZERO() {
        assertThat(Piece.ZERO.getMagnitude()).isEqualTo(BigDecimal.ZERO);
        assertThat(Piece.ZERO.getUnitType()).isEqualTo(PC);
    }

    @Test void testConversion() {
        Piece p30_1 = Piece.of(BigDecimal.valueOf(30.1));
        Piece p50_0 = Piece.of(BigDecimal.valueOf(50), PC);

        Piece p502 = p50_0.convertTo(DOZ);
        assertThat(p502.equals(p50_0)).isFalse();

        assertThat(p502.getMagnitude()).isEqualTo(new BigDecimal(4));
        assertThat(p502.getUnitType()).isEqualTo(DOZ);

        assertThat(p502).isEqualTo(Piece.of(4, DOZ));
        assertThat(p502).isNotEqualTo(Piece.of(50, PC));
        assertThat(p502).isEqualTo(Piece.of(48, PC));
        assertThat(p50_0.getUnitType()).isEqualTo(PC);

        assertThat(p50_0.compareTo(p30_1)).isEqualTo(1);
        assertThat(p30_1.compareTo(p50_0)).isEqualTo(-1);

        Piece p5doz = Piece.of(5, DOZ);
        assertThat(p5doz.compareTo(p50_0)).isEqualTo(1);
        assertThat(p5doz.compareTo(p30_1)).isEqualTo(1);
        assertThat(p50_0.compareTo(p5doz)).isEqualTo(-1);
        assertThat(p30_1.compareTo(p5doz)).isEqualTo(-1);

        Piece p60 = Piece.of(60, PC);
        assertThat(p5doz.compareTo(p60)).isEqualTo(0);
        assertThat(p60.compareTo(p5doz)).isEqualTo(0);
    }

    @Test void testAddition() {
        Piece p30 = Piece.of(30);
        Piece d2 = Piece.of(2, DOZ);
        assertThat(p30.add(d2)).isEqualTo(Piece.of(54, PC));
        assertThat(p30).isEqualTo(Piece.of(30, PC)); //  unmodified
        assertThat(p30.add(Piece.of(2, PC))).isEqualTo(Piece.of(32, PC));
        assertThat(p30).isEqualTo(Piece.of(30, PC)); //  unmodified
        assertThat(d2.add(Piece.of(2, PC))).isEqualTo(Piece.of(26, PC));
        assertThat(d2).isEqualTo(Piece.of(2, DOZ)); //  unmodified
        assertThat(d2.add(Piece.of(1, DOZ))).isEqualTo(Piece.of(3, DOZ));
        assertThat(d2).isEqualTo(Piece.of(2, DOZ)); //  unmodified
    }
}