package org.openwms.core.units;

import org.hibernate.TypeMismatchException;
import org.junit.jupiter.api.Test;
import org.openwms.core.units.api.Measurable;
import org.openwms.core.units.api.MetricDimension;
import org.openwms.core.units.api.MetricDimensionUnit;
import org.openwms.core.units.api.Piece;
import org.openwms.core.units.api.PieceUnit;
import org.openwms.core.units.api.Weight;
import org.openwms.core.units.api.WeightUnit;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UnitsUtilTest {

    @Test
    void fromStringIncompatibleType() {
        assertThatThrownBy(() -> UnitsUtil.fromString(
                "S@org.openwms.core.units.api.IncompatibleType", BigDecimal.valueOf(20)
        )).isInstanceOf(TypeMismatchException.class)
                .hasMessageContaining("Incompatible type:");
    }

    @Test
    void fromStringPieceType() {
        final Measurable<?, ?, ?> measurable = UnitsUtil.fromString("PC@org.openwms.core.units.api.Piece", BigDecimal.valueOf(20));
        assertThat(measurable).isEqualTo(Piece.of(BigDecimal.valueOf(20), PieceUnit.PC));
    }

    @Test
    void fromStringWeightType() {
        final Measurable<?, ?, ?> measurable = UnitsUtil.fromString("KG@org.openwms.core.units.api.Weight", BigDecimal.valueOf(20.2));
        assertThat(measurable).isEqualTo(Weight.of(BigDecimal.valueOf(20.2), WeightUnit.KG));
    }

    @Test
    void fromStringMetricDimensionType() {
        final Measurable<?, ?, ?> measurable = UnitsUtil.fromString("M@org.openwms.core.units.api.MetricDimension", BigDecimal.valueOf(2));
        assertThat(measurable).isEqualTo(MetricDimension.of(BigDecimal.valueOf(2), MetricDimensionUnit.M));
    }
}