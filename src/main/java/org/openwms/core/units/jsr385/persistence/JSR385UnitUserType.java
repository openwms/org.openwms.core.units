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
package org.openwms.core.units.jsr385.persistence;

import org.hibernate.TypeMismatchException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.openwms.core.units.jsr385.api.Each;
import org.openwms.core.units.jsr385.api.WMSUnits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.units.indriya.format.SimpleUnitFormat;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.UnitDimension;

import javax.measure.Unit;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.lang.String.format;

/**
 * An JSR385UnitUserType is a custom Hibernate converter for {@code Unit} types of the JSR-385 Unitofmeasures library.
 *
 * @author Heiko Scherrer
 */
public class JSR385UnitUserType implements CompositeUserType {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSR385UnitUserType.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getPropertyNames() {
        return new String[]{"unit"};
    }

    /**
     * {@inheritDoc}
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
        if (component instanceof Unit<?> unit) {
            return unit;
        }
        throw new TypeMismatchException(format("Incompatible type [%s]", component.getClass()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPropertyValue(Object component, int property, Object value) {
        if (value instanceof Unit<?> unit) {
            component = unit.toString();
        } else {
            throw new UnsupportedOperationException(format("Type [%s] not supported", value));
        }
    }

    /**
     * {@inheritDoc}
     *
     * We do not know the concrete implementation here and return the {@link Unit} class type.
     */
    @Override
    public Class returnedClass() {
        return Unit.class;
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
     */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String rs0 = rs.getString(names[0]);
        if (rs.wasNull()) {
            return null;
        }
        Unit<?> parse;
        if (rs0 == null || rs0.isEmpty()) {
            throw new TypeMismatchException("The unit must always be given");
        }
        String[] parts = rs0.split("@");
        String unit = parts[0];
        String unitTypeClass = parts[1];
        // First try with custom implementation
        if (WMSUnits.exists(unit, unitTypeClass)) {
            return WMSUnits.resolve(unitTypeClass);
        }
        parse = SimpleUnitFormat.getInstance().parse(unit);
        return Quantities.getQuantity(0, parse).getUnit();
    }

    /**
     * {@inheritDoc}
     *
     * We've to store the concrete classname as well.
     */
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, StandardBasicTypes.STRING.sqlType());
        } else {
            if (value instanceof Unit unit) {
                if (UnitDimension.NONE.equals(unit.getDimension())) {
                    st.setString(index, unit.getSymbol() + "@" + Each.class.getCanonicalName());
                    if (LOGGER.isTraceEnabled()) {
                        LOGGER.trace("Binding [{}] to parameter [{}]", Each.class.getCanonicalName(), index);
                    }
                    return;
                }
            }
            throw new TypeMismatchException(format("Incompatible type: [%s]", value.getClass().getCanonicalName()));
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
     *
     * Unit type is immutable.
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