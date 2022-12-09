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

import java.io.Serializable;

/**
 * An UnitType is a super type of all concrete units.
 *
 * @author Heiko Scherrer
 */
public interface UnitType extends Serializable {

    /**
     * Return the concrete type of {@code Measurable}.
     *
     * @param <T> A subtype of Measurable
     * @return The measurable
     */
    @SuppressWarnings("rawtypes")
    <T extends Measurable> T getMeasurable();
}