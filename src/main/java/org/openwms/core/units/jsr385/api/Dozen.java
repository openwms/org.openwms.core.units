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

import tech.units.indriya.AbstractUnit;
import tech.units.indriya.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Dimensionless;
import java.io.Serializable;

/**
 * A Dozen represents a dimensionless 12-each unit.
 *
 * @author Heiko Scherrer
 */
public final class Dozen implements Serializable {

    private Dozen() { }

    /**
     * Create a new Dozen instance with the given {@code amount}.
     *
     * @param amount The amount of the Dozen
     * @return A new instance
     */
    public static Quantity<Dimensionless> of(double amount) {
        return Quantities.getQuantity(12 * amount, AbstractUnit.ONE);
    }
}
