/*
 * Copyright 2005-2019 the original author or authors.
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

/**
 * A WeightTest.
 * 
 * @author Heiko Scherrer
 */
class WeightTest {


    @Test void testWeight() {
        Weight w1 = new Weight(new BigDecimal(1), WeightUnit.KG);
        Weight w2 = new Weight(new BigDecimal(1), WeightUnit.T);
        w2 = w2.convertTo(WeightUnit.KG);
        assertEquals(BigDecimal.ONE, w1.getMagnitude());
        assertEquals(new BigDecimal(0), new BigDecimal("1000").subtract(w2.getMagnitude()));
        assertEquals(WeightUnit.KG, w2.getUnitType());
        w1.compareTo(w2);
    }

    @Test void testWeightComparison() {
        Weight w1 = new Weight(new BigDecimal(1), WeightUnit.G);
        Weight w2 = new Weight(new BigDecimal(1), WeightUnit.T);
        assertThat(w1.compareTo(w2)).isEqualTo(-1);
        assertThat(w2.compareTo(w1)).isEqualTo(1);

        Weight w3 = new Weight(new BigDecimal(2), WeightUnit.G);
        assertThat(w1.compareTo(w3)).isEqualTo(-1);
        assertThat(w3.compareTo(w1)).isEqualTo(1);

        Weight w4 = new Weight(new BigDecimal("0.000002"), WeightUnit.T);
        w3 = w3.convertTo(WeightUnit.T);
        assertThat(w3.compareTo(w4)).isEqualTo(0);
    }
}
