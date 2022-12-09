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
package org.openwms.core.units.converter;

import com.github.dozermapper.core.DozerConverter;
import org.openwms.core.units.api.Measurable;

/**
 * A MeasurableConverter is just an in-out {@link DozerConverter} for {@link Measurable}s to avoid instantiating Measurables and passing
 * source to destination.
 *
 * @author Heiko Scherrer
 */
public class MeasurableConverter extends DozerConverter<Measurable, Measurable> {

    public MeasurableConverter() {
        super(Measurable.class, Measurable.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurable convertTo(Measurable source, Measurable destination) {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Measurable convertFrom(Measurable source, Measurable destination) {
        return source;
    }
}
