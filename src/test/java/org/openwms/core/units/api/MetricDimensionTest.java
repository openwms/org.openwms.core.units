/*
 * Copyright 2005-2023 the original author or authors.
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openwms.core.units.api.MetricDimensionUnit.CM;
import static org.openwms.core.units.api.MetricDimensionUnit.M;
import static org.openwms.core.units.api.Weight.ZERO;
import static org.openwms.core.units.api.WeightUnit.KG;
import static org.openwms.core.units.api.WeightUnit.T;

/**
 * A MetricDimensionTest.
 * 
 * @author Heiko Scherrer
 */
class MetricDimensionTest {

    @Test void testConversion() {
        MetricDimension one_CM = MetricDimension.of(1, CM);
        MetricDimension one_M = MetricDimension.of(1, M);
        one_M = one_M.convertTo(CM);
        assertThat(one_CM.getMagnitude()).isEqualTo(new BigDecimal(1));
        assertThat(new BigDecimal("100").subtract(one_M.getMagnitude())).isEqualTo(new BigDecimal(0));
        assertThat(one_M.getUnitType()).isEqualTo(CM);
        assertThat(one_CM.compareTo(one_M)).isLessThan(0);
        assertThat(one_CM).hasToString("1 CM");
    }

    @Test void testComparison() {
        Weight one_GRAM = Weight.of(1, WeightUnit.G);
        Weight one_TON = Weight.of(1, WeightUnit.T);
        assertThat(one_GRAM.compareTo(one_TON)).isLessThan(0);
        assertThat(one_TON.compareTo(one_GRAM)).isGreaterThan(0);

        Weight two_GRAM = Weight.of(2, WeightUnit.G);
        assertThat(one_GRAM.compareTo(two_GRAM)).isLessThan(0);
        assertThat(two_GRAM.compareTo(one_GRAM)).isGreaterThan(0);

        Weight w4 = Weight.of(new BigDecimal("0.000002"), WeightUnit.T);
        two_GRAM = two_GRAM.convertTo(WeightUnit.T);
        assertThat(two_GRAM.compareTo(w4)).isZero();
    }

    @Test void testAddition() {
        Weight one_KILO = Weight.of(1, KG);
        Weight two_KILO = Weight.of(2, KG);
        assertThat(one_KILO.add(one_KILO)).isEqualTo(two_KILO);

        Weight one_TON = Weight.of(1, T);
        assertThat(one_KILO.add(one_TON)).isEqualTo(Weight.of(1001, KG));
        assertThat(one_TON.add(one_KILO)).isEqualTo(Weight.of(1001, KG));
        assertEquals(one_TON.add(one_KILO), Weight.of(new BigDecimal("1.001"), T));
    }

    @Test void testSubtract() {
        Weight one_KILO = Weight.of(1, KG);
        Weight two_KILO = Weight.of(2, KG);
        assertThat(two_KILO.subtract(one_KILO)).isEqualTo(one_KILO);
        assertThat(two_KILO.subtract(two_KILO)).isEqualTo(ZERO);
        assertThat(two_KILO.subtract(two_KILO).isZero()).isTrue();

        Weight one_TON = Weight.of(1, T);
        assertThat(one_TON.subtract(one_KILO)).isEqualTo(Weight.of(999, KG));
        assertThat(one_KILO.subtract(one_TON).isNegative()).isTrue();
    }
}
