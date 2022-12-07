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
 * A DozenTest.
 *
 * @author Heiko Scherrer
 */
class DozenTest {

    @Test
    void test_dozen_creation() {
        assertThat(Dozen.of(1).getValue()).isEqualTo(12);
        assertThat(Dozen.of(1.5).getValue()).isEqualTo(18);
        assertThat(Dozen.of(0).getValue()).isEqualTo(0);
        assertThat(Dozen.of(-1).getValue()).isEqualTo(-12);
    }
}
