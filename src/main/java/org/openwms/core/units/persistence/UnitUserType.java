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
package org.openwms.core.units.persistence;

import org.hibernate.HibernateException;
import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;
import org.openwms.core.units.api.AbstractMeasure;
import org.openwms.core.units.api.Measurable;
import org.openwms.core.units.api.Piece;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.Weight;
import org.openwms.core.units.api.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.lang.String.format;

/**
 * An UnitUserType is used by Hibernate as converter for custom {@code Unit} types. Only subclasses of {@link AbstractMeasure} are supported
 * by this type converter.
 *
 * @author Heiko Scherrer
 */
public class UnitUserType implements CompositeUserType<Measurable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitUserType.class);

    public static class MeasurableMapper {
        String magnitude;
        String unitType;
    }

    @Override
    public Class<?> embeddable() {
        return MeasurableMapper.class;
    }

    @Override
    public Class<Measurable> returnedClass() {
        return Measurable.class;
    }

    @Override
    public Measurable instantiate(ValueAccess valueAccess, SessionFactoryImplementor sessionFactory) {
        // alphabetical
        final BigDecimal magnitude = valueAccess.getValue( 0, BigDecimal.class );
        final String fullUnitType = valueAccess.getValue( 1, String.class );
        if (fullUnitType == null) {
            return null;
        }
        String[] val = fullUnitType.split("@");
        String unitType = val[0];
        String unitTypeClass = val[1];

        if (Piece.class.getCanonicalName().equals(unitTypeClass)) {
            return Piece.of(magnitude, PieceUnit.valueOf(unitType));
        } else if (Weight.class.getCanonicalName().equals(unitTypeClass)) {
            return Weight.of(magnitude, WeightUnit.valueOf(unitType));
        }
        throw new TypeMismatchException(format("Incompatible type: [%s]", unitTypeClass));
    }

    @Override
    public Object getPropertyValue(Measurable component, int property) throws HibernateException {
        // alphabetical
        switch ( property ) {
            case 0:
                return component.getMagnitude();
            case 1:
                return component.getUnitType();
        }
        return null;
    }

    @Override
    public boolean equals(Measurable x, Measurable y) {
        return x == y || x != null && Objects.equals( x.getMagnitude(), y.getMagnitude() )
                && Objects.equals( x.getUnitType(), y.getUnitType() );
    }

    @Override
    public int hashCode(Measurable x) {
        return Objects.hash( x.getMagnitude(), x.getUnitType() );
    }

    @Override
    public Measurable deepCopy(Measurable value) {
        return value; // immutable
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Measurable value) {
        return new String[] { value.getMagnitude().toString(), value.getUnitType().toString() };
    }

    @Override
    public Measurable assemble(Serializable cached, Object owner) {
        final String[] parts = (String[]) cached;

        String[] val = parts[1].split("@");
        String unitType = val[0];
        String unitTypeClass = val[1];

        if (Piece.class.getCanonicalName().equals(unitTypeClass)) {
            return Piece.of(Integer.parseInt(parts[0]), PieceUnit.valueOf(unitType));
        } else if (Weight.class.getCanonicalName().equals(unitTypeClass)) {
            return Weight.of(Integer.parseInt(parts[0]), WeightUnit.valueOf(unitType));
        };
        throw new TypeMismatchException(format("Incompatible type: [%s]", unitTypeClass));
    }

    @Override
    public Measurable replace(Measurable detached, Measurable managed, Object owner) {
        return detached;
    }


}