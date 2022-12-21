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
package org.openwms.core.units.jsr385.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A EachesTest.
 *
 * @author Heiko Scherrer
 */
class EachesTest {

    @Test
    void compare_eaches_with_dozens() {
        var TWELVE_EACHES = Each.of(12);
        var ONE_DOZEN = Dozen.of(1);
        assertThat(ONE_DOZEN).isEqualTo(TWELVE_EACHES);

        var ONE_EACH = Each.of(1);
        assertThat(ONE_EACH.add(ONE_DOZEN)).isEqualTo(Each.of(13));
    }

    @Test
    void test_substraction_of_Each() {
        assertThat(Each.of(3).subtract(Each.of(1))).isEqualTo(Each.of(2));
        assertThat(Each.of(3).subtract(Each.of(3))).isEqualTo(Each.of(0));
        assertThat(Each.of(3).subtract(Each.of(-1))).isEqualTo(Each.of(4));
        assertThat(Each.of(3).subtract(Each.of(4))).isEqualTo(Each.of(-1));
    }

    @Test
    void test_mixed_substraction() {
        assertThat(Dozen.of(1).subtract(Each.of(1))).isEqualTo(Each.of(11));
        assertThat(Dozen.of(1).subtract(Each.of(12))).isEqualTo(Each.of(0));
        assertThat(Dozen.of(1).subtract(Each.of(13))).isEqualTo(Each.of(-1));
        assertThat(Each.of(1).subtract(Dozen.of(1))).isEqualTo(Each.of(-11));
    }

    @Test
    void test_divide_Eaches() {
        assertThat(Each.of(3).divide(Each.of(1))).isEqualTo(Each.of(3));
        assertThat(Each.of(4).divide(Each.of(2))).isEqualTo(Each.of(2));
        assertThat(Each.of(0).divide(Each.of(1))).isEqualTo(Each.of(0));
    }
}