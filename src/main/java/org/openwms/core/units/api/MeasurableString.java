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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;

import static java.lang.String.format;

/**
 * A MeasurableString.
 *
 * @author Heiko Scherrer
 */
public class MeasurableString implements Serializable {

    public static final String SEPARATOR = " ";
    private String amount;
    private String unit;

    @JsonCreator
    public MeasurableString(String str) {
        String[] parts = str.split(SEPARATOR);
        if (parts.length != 2) {
            throw new IllegalArgumentException(format("Not a valid MeasurableString: [%s]", str));
        }
        if (!NumberUtils.isCreatable(parts[0])) {
            throw new IllegalArgumentException(format("The amount is not a number: [%s]", parts[0]));
        }
        this.amount = String.valueOf(parts[0]);
        this.unit = String.valueOf(parts[1]);
    }

    MeasurableString() {}

    public MeasurableString(Measurable str) {
        if (str == null) {
            throw new IllegalArgumentException("MeasurableString is null");
        }
        this.amount = String.valueOf(str.getMagnitude());
        this.unit = String.valueOf(str.getUnitType());
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    @JsonValue
    public String toString() {
        return amount + SEPARATOR + unit;
    }
}
