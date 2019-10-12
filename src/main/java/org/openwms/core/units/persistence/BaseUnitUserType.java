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
package org.openwms.core.units.persistence;

import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.openwms.core.units.api.BaseUnit;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

/**
 * A BaseUnitUserType is used by Hibernate as converter for custom {@code BaseUnit} types. Only subclasses of {@link BaseUnit} are supported
 * by this type converter.
 *
 * @author Heiko Scherrer
 */
public class BaseUnitUserType implements CompositeUserType {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseUnitUserType.class);

    /**
     * {@inheritDoc}
     * <p>
     * Only the property {@code baseUnit} is accepted.
     */
    @Override
    public String[] getPropertyNames() {
        return new String[]{"baseUnit"};
    }

    /**
     * {@inheritDoc}
     * <p>
     * Property is persisted as concatenated String
     */
    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StandardBasicTypes.STRING};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getPropertyValue(Object component, int property) {
        return component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(Object component, int property, Object value) {
        throw new UnsupportedOperationException("BaseUnit types are immutable");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class returnedClass() {
        return BaseUnit.class;
    }

    /**
     * {@inheritDoc}
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
     */
    @Override
    public int hashCode(Object x) {
        return x.hashCode();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Try to re-assign the value read from the database to some type of BaseUnit. Currently supported types:
     * <ul>
     * <li>PieceUnit</li>
     * <li>WeightUnit</li>
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
        if (PieceUnit.class.getCanonicalName().equals(unitTypeClass)) {
            return PieceUnit.valueOf(unitType);
        } else if (WeightUnit.class.getCanonicalName().equals(unitTypeClass)) {
            return WeightUnit.valueOf(unitType);
        }
        throw new TypeMismatchException(format("Incompatible type: [%s]", unitTypeClass));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, StandardBasicTypes.STRING.sqlType());
        } else {
            if (value instanceof PieceUnit) {
                PieceUnit piece = (PieceUnit) value;
                st.setString(index, piece.name() + "@" + PieceUnit.class.getCanonicalName());
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Binding [{}@{}] to parameter [{}]", piece.name(), PieceUnit.class.getCanonicalName(), index);
                }
            } else if (value instanceof WeightUnit) {
                WeightUnit weight = (WeightUnit) value;
                st.setString(index, weight.name() + "@" + WeightUnit.class.getCanonicalName());
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Binding [{}@{}] to parameter [{}]", weight.name(), WeightUnit.class.getCanonicalName(), index);
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