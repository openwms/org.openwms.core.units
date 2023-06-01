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

import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

/**
 * An UnitUserType is used by Hibernate as converter for custom {@code Unit} types. Only subclasses of {@link AbstractMeasure} are supported
 * by this type converter.
 *
 * @author Heiko Scherrer
 */
public class UnitUserType implements CompositeUserType {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitUserType.class);

    /**
     * {@inheritDoc}
     * <p>
     * We expect that every unit has two fields, named {@code unitType} and {@code magnitude}.
     */
    @Override
    public String[] getPropertyNames() {
        return new String[]{"unitType", "magnitude"};
    }

    /**
     * {@inheritDoc}
     * <p>
     * We're going to persist both fields as Strings.
     */
    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StandardBasicTypes.STRING, StandardBasicTypes.STRING};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPropertyValue(Object component, int property) {
        if (component instanceof Piece piece) {
            return property == 0 ? piece.getUnitType() : piece.getMagnitude();
        } else if (component instanceof Weight weight) {
            return property == 0 ? weight.getUnitType() : weight.getMagnitude();
        }
        throw new TypeMismatchException(format("Incompatible type [%s]", component.getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(Object component, int property, Object value) {
        if (value instanceof PieceUnit piece) {
            component = property == 0 ? piece.getBaseUnit() : piece.getMagnitude();
        } else if (value instanceof WeightUnit weight) {
            component = property == 0 ? weight.getBaseUnit() : weight.getMagnitude();
        } else if (value instanceof BigDecimal val) {
            component = val;
        } else {
            throw new UnsupportedOperationException(format("Type [%s] not supported", value));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * We do not know the concrete implementation here and return an Unit class type.
     */
    @Override
    public Class returnedClass() {
        return Measurable.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegate to Unit implementation.
     */
    @Override
    public boolean equals(Object x, Object y) {
        if (x == y) {
            return true;
        }
        if (x == null || y == null) {
            return false;
        }
        return x.equals(y);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegate to Unit implementation.
     */
    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Try to re-assign the value read from the database to some type of Unit. Currently supported types:
     * <ul>
     * <li>Piece</li>
     * <li>Weight</li>
     * </ul>
     */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String rs0 = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        String[] val = rs0.split("@");
        String unitType = val[0];
        String unitTypeClass = val[1];
        if (Piece.class.getCanonicalName().equals(unitTypeClass)) {
            return Piece.of(rs.getBigDecimal(names[1]), PieceUnit.valueOf(unitType));
        } else if (Weight.class.getCanonicalName().equals(unitTypeClass)) {
            return Weight.of(rs.getBigDecimal(names[1]), WeightUnit.valueOf(unitType));
        }
        throw new TypeMismatchException(format("Incompatible type: [%s]", unitTypeClass));
    }

    /**
     * {@inheritDoc}
     * <p>
     * We've to store the concrete classname as well.
     */
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, StandardBasicTypes.STRING.sqlType());
            st.setNull(index + 1, StandardBasicTypes.STRING.sqlType());
        } else {
            if (value instanceof Piece piece) {
                st.setString(index, piece.getUnitType().toString() + "@" + Piece.class.getCanonicalName());
                st.setString(index + 1, piece.getMagnitude().toPlainString());
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Binding [{}@{}] to parameter [{}]", piece.getUnitType(), Piece.class.getCanonicalName(), index);
                    LOGGER.trace("Binding [{}] to parameter [{}]", piece.getMagnitude().toPlainString(), (index + 1));
                }
            } else if (value instanceof Weight weight) {
                st.setString(index, weight.getUnitType().toString() + "@" + Weight.class.getCanonicalName());
                st.setString(index + 1, weight.getMagnitude().toPlainString());
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Binding [{}@{}] to parameter [{}]", weight.getUnitType(), Weight.class.getCanonicalName(), index);
                    LOGGER.trace("Binding [{}] to parameter [{}]", weight.getMagnitude().toPlainString(), (index + 1));
                }
            } else {
                throw new TypeMismatchException(format("Incompatible type: [%s]", value.getClass().getCanonicalName()));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deepCopy(Object value) {
        return value;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Non Unit type is mutable.
     */
    @Override
    public boolean isMutable() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable disassemble(Object value, SharedSessionContractImplementor session) {
        return (Serializable) value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) {
        return cached;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) {
        return original;
    }
}