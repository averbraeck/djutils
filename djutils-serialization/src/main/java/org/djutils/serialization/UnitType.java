package org.djutils.serialization;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.AbsoluteTemperatureUnit;
import org.djunits.unit.AbsorbedDoseUnit;
import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.AmountOfSubstanceUnit;
import org.djunits.unit.AngleUnit;
import org.djunits.unit.AngularAccelerationUnit;
import org.djunits.unit.AngularVelocityUnit;
import org.djunits.unit.AreaUnit;
import org.djunits.unit.CatalyticActivityUnit;
import org.djunits.unit.DensityUnit;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.DirectionUnit;
import org.djunits.unit.DurationUnit;
import org.djunits.unit.ElectricalCapacitanceUnit;
import org.djunits.unit.ElectricalChargeUnit;
import org.djunits.unit.ElectricalConductanceUnit;
import org.djunits.unit.ElectricalCurrentUnit;
import org.djunits.unit.ElectricalInductanceUnit;
import org.djunits.unit.ElectricalPotentialUnit;
import org.djunits.unit.ElectricalResistanceUnit;
import org.djunits.unit.EnergyUnit;
import org.djunits.unit.EquivalentDoseUnit;
import org.djunits.unit.FlowMassUnit;
import org.djunits.unit.FlowVolumeUnit;
import org.djunits.unit.ForceUnit;
import org.djunits.unit.FrequencyUnit;
import org.djunits.unit.IlluminanceUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.LinearDensityUnit;
import org.djunits.unit.LuminousFluxUnit;
import org.djunits.unit.LuminousIntensityUnit;
import org.djunits.unit.MagneticFluxDensityUnit;
import org.djunits.unit.MagneticFluxUnit;
import org.djunits.unit.MassUnit;
import org.djunits.unit.MomentumUnit;
import org.djunits.unit.PositionUnit;
import org.djunits.unit.PowerUnit;
import org.djunits.unit.PressureUnit;
import org.djunits.unit.RadioActivityUnit;
import org.djunits.unit.SolidAngleUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TemperatureUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.unit.TorqueUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.VolumeUnit;
import org.djutils.exceptions.Throw;

/**
 * DJUNITS Display Types to be used as part of a Sim0MQ message.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class UnitType implements Serializable
{
    /** */
    private static final long serialVersionUID = 20170314L;

    /** the unit types from number to type. */
    private static Map<QuantityType, Map<Integer, UnitType>> codeDisplayTypeMap = new HashMap<>();

    /** the unit types from class to type. */
    private static Map<Unit<?>, UnitType> djunitsDisplayTypeMap = new HashMap<>();

    /** the code of the unit as a byte. */
    private final int code;

    /** the corresponding unit data type. */
    private final QuantityType unitType;

    /** the djunits data type. */
    private final Unit<?> djunitsType;

    /** the unit name. */
    private final String name;

    /** the unit description. */
    private final String abbreviation;

    /* ================================================= DIMENSIONLESS ================================================= */

    /** Dimensionless.SI unit type with code 0. */
    public static final UnitType DIMENSIONLESS_SI =
            new UnitType(QuantityType.DIMENSIONLESS, 0, DimensionlessUnit.SI, "SI", "[]");

    /* ================================================= ACCELERATION ================================================== */

    /** Acceleration.METER_PER_SECOND_2 unit type with code 0. */
    public static final UnitType ACCELERATION_METER_PER_SECOND_2 = new UnitType(QuantityType.ACCELERATION, 0,
            AccelerationUnit.METER_PER_SECOND_2, "METER_PER_SECOND_2", "m/s2");

    /** Acceleration.KM_PER_HOUR_2 unit type with code 1. */
    public static final UnitType ACCELERATION_KM_PER_HOUR_2 =
            new UnitType(QuantityType.ACCELERATION, 1, AccelerationUnit.KM_PER_HOUR_2, "KM_PER_HOUR_2", "km/h2");

    /** Acceleration.INCH_PER_SECOND_2 unit type with code 2. */
    public static final UnitType ACCELERATION_INCH_PER_SECOND_2 = new UnitType(QuantityType.ACCELERATION, 2,
            AccelerationUnit.INCH_PER_SECOND_2, "INCH_PER_SECOND_2", "in/s2");

    /** Acceleration.FOOT_PER_SECOND_2 unit type with code 3. */
    public static final UnitType ACCELERATION_FOOT_PER_SECOND_2 = new UnitType(QuantityType.ACCELERATION, 3,
            AccelerationUnit.FOOT_PER_SECOND_2, "FOOT_PER_SECOND_2", "ft/s2");

    /** Acceleration.MILE_PER_HOUR_2 unit type with code 4. */
    public static final UnitType ACCELERATION_MILE_PER_HOUR_2 =
            new UnitType(QuantityType.ACCELERATION, 4, AccelerationUnit.MILE_PER_HOUR_2, "MILE_PER_HOUR_2", "mi/h2");

    /** Acceleration.MILE_PER_HOUR_PER_SECOND unit type with code 5. */
    public static final UnitType ACCELERATION_MILE_PER_HOUR_PER_SECOND = new UnitType(QuantityType.ACCELERATION, 5,
            AccelerationUnit.MILE_PER_HOUR_PER_SECOND, "MILE_PER_HOUR_PER_SECOND", "mi/h/s");

    /** Acceleration.KNOT_PER_SECOND unit type with code 6. */
    public static final UnitType ACCELERATION_KNOT_PER_SECOND =
            new UnitType(QuantityType.ACCELERATION, 6, AccelerationUnit.KNOT_PER_SECOND, "KNOT_PER_SECOND", "kt/s");

    /** Acceleration.GAL unit type with code 7. */
    public static final UnitType ACCELERATION_GAL =
            new UnitType(QuantityType.ACCELERATION, 7, AccelerationUnit.GAL, "GAL", "gal");

    /** Acceleration.STANDARD_GRAVITY unit type with code 8. */
    public static final UnitType ACCELERATION_STANDARD_GRAVITY =
            new UnitType(QuantityType.ACCELERATION, 8, AccelerationUnit.STANDARD_GRAVITY, "STANDARD_GRAVITY", "g");

    /** Acceleration.MILE_PER_SECOND_2 unit type with code 9. */
    public static final UnitType ACCELERATION_MILE_PER_SECOND_2 = new UnitType(QuantityType.ACCELERATION, 9,
            AccelerationUnit.MILE_PER_SECOND_2, "MILE_PER_SECOND_2", "mi/s2");

    /* ================================================== SOLIDANGLE =================================================== */

    /** SolidAngle.STERADIAN unit type with code 0. */
    public static final UnitType SOLIDANGLE_STERADIAN =
            new UnitType(QuantityType.SOLIDANGLE, 0, SolidAngleUnit.STERADIAN, "STERADIAN", "sr");

    /** SolidAngle.SQUARE_DEGREE unit type with code 1. */
    public static final UnitType SOLIDANGLE_SQUARE_DEGREE =
            new UnitType(QuantityType.SOLIDANGLE, 1, SolidAngleUnit.SQUARE_DEGREE, "SQUARE_DEGREE", "sq.deg");

    /* ===================================================== ANGLE ===================================================== */

    /** Angle.RADIAN unit type with code 0. */
    public static final UnitType ANGLE_RADIAN =
            new UnitType(QuantityType.ANGLE, 0, AngleUnit.RADIAN, "RADIAN", "rad");

    /** Angle.ARCMINUTE unit type with code 1. */
    public static final UnitType ANGLE_ARCMINUTE =
            new UnitType(QuantityType.ANGLE, 1, AngleUnit.ARCMINUTE, "ARCMINUTE", "arcmin");

    /** Angle.ARCSECOND unit type with code 2. */
    public static final UnitType ANGLE_ARCSECOND =
            new UnitType(QuantityType.ANGLE, 2, AngleUnit.ARCSECOND, "ARCSECOND", "arcsec");

    /** Angle.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final UnitType ANGLE_CENTESIMAL_ARCMINUTE = new UnitType(QuantityType.ANGLE, 3,
            AngleUnit.CENTESIMAL_ARCMINUTE, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Angle.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final UnitType ANGLE_CENTESIMAL_ARCSECOND = new UnitType(QuantityType.ANGLE, 4,
            AngleUnit.CENTESIMAL_ARCSECOND, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Angle.DEGREE unit type with code 5. */
    public static final UnitType ANGLE_DEGREE =
            new UnitType(QuantityType.ANGLE, 5, AngleUnit.DEGREE, "DEGREE", "deg");

    /** Angle.GRAD unit type with code 6. */
    public static final UnitType ANGLE_GRAD = new UnitType(QuantityType.ANGLE, 6, AngleUnit.GRAD, "GRAD", "grad");

    /** Angle.PERCENT unit type with code 7. */
    public static final UnitType ANGLE_PERCENT =
            new UnitType(QuantityType.ANGLE, 7, AngleUnit.PERCENT, "PERCENT", "%");

    /* =================================================== DIRECTION =================================================== */

    /** Direction.NORTH_RADIAN unit type with code 0. */
    public static final UnitType DIRECTION_NORTH_RADIAN =
            new UnitType(QuantityType.DIRECTION, 0, DirectionUnit.NORTH_RADIAN, "NORTH_RADIAN", "rad(N)");

    /** Direction.NORTH_DEGREE unit type with code 1. */
    public static final UnitType DIRECTION_NORTH_DEGREE =
            new UnitType(QuantityType.DIRECTION, 1, DirectionUnit.NORTH_DEGREE, "NORTH_DEGREE", "deg(N)");

    /** Direction.EAST_RADIAN unit type with code 2. */
    public static final UnitType DIRECTION_EAST_RADIAN =
            new UnitType(QuantityType.DIRECTION, 2, DirectionUnit.EAST_RADIAN, "EAST_RADIAN", "rad(E)");

    /** Direction.EAST_DEGREE unit type with code 2. */
    public static final UnitType DIRECTION_EAST_DEGREE =
            new UnitType(QuantityType.DIRECTION, 3, DirectionUnit.EAST_DEGREE, "EAST_DEGREE", "deg(E)");

    /* ===================================================== AREA ====================================================== */

    /** Area.SQUARE_METER unit type with code 0. */
    public static final UnitType AREA_SQUARE_METER =
            new UnitType(QuantityType.AREA, 0, AreaUnit.SQUARE_METER, "SQUARE_METER", "m2");

    /** Area.SQUARE_ATTOMETER unit type with code 1. */
    public static final UnitType AREA_SQUARE_ATTOMETER =
            new UnitType(QuantityType.AREA, 1, AreaUnit.BASE.of("am^2"), "SQUARE_ATTOMETER", "am2");

    /** Area.SQUARE_FEMTOMETER unit type with code 2. */
    public static final UnitType AREA_SQUARE_FEMTOMETER =
            new UnitType(QuantityType.AREA, 2, AreaUnit.BASE.of("fm^2"), "SQUARE_FEMTOMETER", "fm2");

    /** Area.SQUARE_PICOMETER unit type with code 3. */
    public static final UnitType AREA_SQUARE_PICOMETER =
            new UnitType(QuantityType.AREA, 3, AreaUnit.BASE.of("pm^2"), "SQUARE_PICOMETER", "pm2");

    /** Area.SQUARE_NANOMETER unit type with code 4. */
    public static final UnitType AREA_SQUARE_NANOMETER =
            new UnitType(QuantityType.AREA, 4, AreaUnit.BASE.of("nm^2"), "SQUARE_NANOMETER", "nm2");

    /** Area.SQUARE_MICROMETER unit type with code 5. */
    public static final UnitType AREA_SQUARE_MICROMETER =
            new UnitType(QuantityType.AREA, 5, AreaUnit.BASE.of("μm^2"), "SQUARE_MICROMETER", "μm2");

    /** Area.SQUARE_MILLIMETER unit type with code 6. */
    public static final UnitType AREA_SQUARE_MILLIMETER =
            new UnitType(QuantityType.AREA, 6, AreaUnit.SQUARE_MILLIMETER, "SQUARE_MILLIMETER", "mm2");

    /** Area.SQUARE_CENTIMETER unit type with code 7. */
    public static final UnitType AREA_SQUARE_CENTIMETER =
            new UnitType(QuantityType.AREA, 7, AreaUnit.SQUARE_CENTIMETER, "SQUARE_CENTIMETER", "cm2");

    /** Area.SQUARE_DECIMETER unit type with code 8. */
    public static final UnitType AREA_SQUARE_DECIMETER =
            new UnitType(QuantityType.AREA, 8, AreaUnit.SQUARE_DECIMETER, "SQUARE_DECIMETER", "dm2");

    /** Area.SQUARE_DEKAMETER unit type with code 9. */
    public static final UnitType AREA_SQUARE_DEKAMETER =
            new UnitType(QuantityType.AREA, 9, AreaUnit.BASE.of("dam^2"), "SQUARE_DEKAMETER", "dam2");

    /** Area.SQUARE_HECTOMETER unit type with code 10. */
    public static final UnitType AREA_SQUARE_HECTOMETER =
            new UnitType(QuantityType.AREA, 10, AreaUnit.SQUARE_HECTOMETER, "SQUARE_HECTOMETER", "hm2");

    /** Area.SQUARE_KILOMETER unit type with code 11. */
    public static final UnitType AREA_SQUARE_KILOMETER =
            new UnitType(QuantityType.AREA, 11, AreaUnit.SQUARE_KILOMETER, "SQUARE_KILOMETER", "km2");

    /** Area.SQUARE_MEGAMETER unit type with code 12. */
    public static final UnitType AREA_SQUARE_MEGAMETER =
            new UnitType(QuantityType.AREA, 12, AreaUnit.BASE.of("Mm^2"), "SQUARE_MEGAMETER", "Mm2");

    /** Area.SQUARE_INCH unit type with code 13. */
    public static final UnitType AREA_SQUARE_INCH =
            new UnitType(QuantityType.AREA, 13, AreaUnit.SQUARE_INCH, "SQUARE_INCH", "in2");

    /** Area.SQUARE_FOOT unit type with code 14. */
    public static final UnitType AREA_SQUARE_FOOT =
            new UnitType(QuantityType.AREA, 14, AreaUnit.SQUARE_FOOT, "SQUARE_FOOT", "ft2");

    /** Area.SQUARE_YARD unit type with code 15. */
    public static final UnitType AREA_SQUARE_YARD =
            new UnitType(QuantityType.AREA, 15, AreaUnit.SQUARE_YARD, "SQUARE_YARD", "yd2");

    /** Area.SQUARE_MILE unit type with code 16. */
    public static final UnitType AREA_SQUARE_MILE =
            new UnitType(QuantityType.AREA, 16, AreaUnit.SQUARE_MILE, "SQUARE_MILE", "mi2");

    /** Area.SQUARE_NAUTICAL_MILE unit type with code 17. */
    public static final UnitType AREA_SQUARE_NAUTICAL_MILE =
            new UnitType(QuantityType.AREA, 17, AreaUnit.SQUARE_NAUTICAL_MILE, "SQUARE_NAUTICAL_MILE", "NM2");

    /** Area.ACRE unit type with code 18. */
    public static final UnitType AREA_ACRE = new UnitType(QuantityType.AREA, 18, AreaUnit.ACRE, "ACRE", "acre");

    /** Area.ARE unit type with code 19. */
    public static final UnitType AREA_ARE = new UnitType(QuantityType.AREA, 19, AreaUnit.ARE, "ARE", "a");

    /** Area.CENTIARE unit type with code 20. */
    public static final UnitType AREA_CENTIARE =
            new UnitType(QuantityType.AREA, 20, AreaUnit.CENTIARE, "CENTIARE", "ca");

    /** Area.HECTARE unit type with code 21. */
    public static final UnitType AREA_HECTARE =
            new UnitType(QuantityType.AREA, 21, AreaUnit.HECTARE, "HECTARE", "ha");

    /* ==================================================== DENSITY ==================================================== */

    /** Density.KG_PER_METER_3 unit type with code 0. */
    public static final UnitType DENSITY_KG_PER_METER_3 =
            new UnitType(QuantityType.DENSITY, 0, DensityUnit.KG_PER_METER_3, "KG_PER_METER_3", "kg/m3");

    /** Density.GRAM_PER_CENTIMETER_3 unit type with code 1. */
    public static final UnitType DENSITY_GRAM_PER_CENTIMETER_3 =
            new UnitType(QuantityType.DENSITY, 1, DensityUnit.GRAM_PER_CENTIMETER_3, "GRAM_PER_CENTIMETER_3", "g/cm3");

    /* =============================================== ELECTRICALCHARGE ================================================ */

    /** ElectricalCharge.COULOMB unit type with code 0. */
    public static final UnitType ELECTRICALCHARGE_COULOMB =
            new UnitType(QuantityType.ELECTRICALCHARGE, 0, ElectricalChargeUnit.COULOMB, "COULOMB", "C");

    /** ElectricalCharge.PICOCOULOMB unit type with code 1. */
    public static final UnitType ELECTRICALCHARGE_PICOCOULOMB = new UnitType(QuantityType.ELECTRICALCHARGE, 1,
            ElectricalChargeUnit.BASE.getUnitByAbbreviation("pC"), "PICOCOULOMB", "pC");

    /** ElectricalCharge.NANOCOULOMB unit type with code 2. */
    public static final UnitType ELECTRICALCHARGE_NANOCOULOMB = new UnitType(QuantityType.ELECTRICALCHARGE, 2,
            ElectricalChargeUnit.BASE.getUnitByAbbreviation("nC"), "NANOCOULOMB", "nC");

    /** ElectricalCharge.MICROCOULOMB unit type with code 3. */
    public static final UnitType ELECTRICALCHARGE_MICROCOULOMB =
            new UnitType(QuantityType.ELECTRICALCHARGE, 3, ElectricalChargeUnit.MICROCOULOMB, "MICROCOULOMB", "μC");

    /** ElectricalCharge.MILLICOULOMB unit type with code 4. */
    public static final UnitType ELECTRICALCHARGE_MILLICOULOMB =
            new UnitType(QuantityType.ELECTRICALCHARGE, 4, ElectricalChargeUnit.MILLICOULOMB, "MILLICOULOMB", "mC");

    /** ElectricalCharge.ABCOULOMB unit type with code 5. */
    public static final UnitType ELECTRICALCHARGE_ABCOULOMB =
            new UnitType(QuantityType.ELECTRICALCHARGE, 5, ElectricalChargeUnit.ABCOULOMB, "ABCOULOMB", "abC");

    /** ElectricalCharge.ATOMIC_UNIT unit type with code 6. */
    public static final UnitType ELECTRICALCHARGE_ATOMIC_UNIT =
            new UnitType(QuantityType.ELECTRICALCHARGE, 6, ElectricalChargeUnit.ATOMIC_UNIT, "ATOMIC_UNIT", "au");

    /** ElectricalCharge.EMU unit type with code 7. */
    public static final UnitType ELECTRICALCHARGE_EMU =
            new UnitType(QuantityType.ELECTRICALCHARGE, 7, ElectricalChargeUnit.EMU, "EMU", "emu");

    /** ElectricalCharge.ESU unit type with code 8. */
    public static final UnitType ELECTRICALCHARGE_ESU =
            new UnitType(QuantityType.ELECTRICALCHARGE, 8, ElectricalChargeUnit.ESU, "ESU", "esu");

    /** ElectricalCharge.FARADAY unit type with code 9. */
    public static final UnitType ELECTRICALCHARGE_FARADAY =
            new UnitType(QuantityType.ELECTRICALCHARGE, 9, ElectricalChargeUnit.FARADAY, "FARADAY", "F");

    /** ElectricalCharge.FRANKLIN unit type with code 10. */
    public static final UnitType ELECTRICALCHARGE_FRANKLIN =
            new UnitType(QuantityType.ELECTRICALCHARGE, 10, ElectricalChargeUnit.FRANKLIN, "FRANKLIN  ", "Fr");

    /** ElectricalCharge.STATCOULOMB unit type with code 11. */
    public static final UnitType ELECTRICALCHARGE_STATCOULOMB =
            new UnitType(QuantityType.ELECTRICALCHARGE, 11, ElectricalChargeUnit.STATCOULOMB, "STATCOULOMB", "statC");

    /** ElectricalCharge.MILLIAMPERE_HOUR unit type with code 12. */
    public static final UnitType ELECTRICALCHARGE_MILLIAMPERE_HOUR = new UnitType(QuantityType.ELECTRICALCHARGE, 12,
            ElectricalChargeUnit.MILLIAMPERE_HOUR, "MILLIAMPERE_HOUR", "mAh");

    /** ElectricalCharge.AMPERE_HOUR unit type with code 13. */
    public static final UnitType ELECTRICALCHARGE_AMPERE_HOUR =
            new UnitType(QuantityType.ELECTRICALCHARGE, 13, ElectricalChargeUnit.AMPERE_HOUR, "AMPERE_HOUR", "Ah");

    /** ElectricalCharge.KILOAMPERE_HOUR unit type with code 14. */
    public static final UnitType ELECTRICALCHARGE_KILOAMPERE_HOUR = new UnitType(QuantityType.ELECTRICALCHARGE, 14,
            ElectricalChargeUnit.KILOAMPERE_HOUR, "KILOAMPERE_HOUR", "kAh");

    /** ElectricalCharge.MEGAAMPERE_HOUR unit type with code 15. */
    public static final UnitType ELECTRICALCHARGE_MEGAAMPERE_HOUR = new UnitType(QuantityType.ELECTRICALCHARGE, 15,
            ElectricalChargeUnit.MEGAAMPERE_HOUR, "MEGAAMPERE_HOUR", "MAh");

    /** ElectricalCharge.MILLIAMPERE_SECOND unit type with code 16. */
    public static final UnitType ELECTRICALCHARGE_MILLIAMPERE_SECOND = new UnitType(QuantityType.ELECTRICALCHARGE,
            16, ElectricalChargeUnit.MILLIAMPERE_SECOND, "MILLIAMPERE_SECOND", "mAs");

    /* ============================================= ELECTRICALCURRENT ================================================= */

    /** ElectricalCurrent.AMPERE unit type with code 0. */
    public static final UnitType ELECTRICALCURRENT_AMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 0, ElectricalCurrentUnit.AMPERE, "AMPERE", "A");

    /** ElectricalCurrent.NANOAMPERE unit type with code 1. */
    public static final UnitType ELECTRICALCURRENT_NANOAMPERE = new UnitType(QuantityType.ELECTRICALCURRENT, 1,
            ElectricalCurrentUnit.BASE.getUnitByAbbreviation("nA"), "NANOAMPERE", "nA");

    /** ElectricalCurrent.MICROAMPERE unit type with code 2. */
    public static final UnitType ELECTRICALCURRENT_MICROAMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 2, ElectricalCurrentUnit.MICROAMPERE, "MICROAMPERE", "μA");

    /** ElectricalCurrent.MILLIAMPERE unit type with code 3. */
    public static final UnitType ELECTRICALCURRENT_MILLIAMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 3, ElectricalCurrentUnit.MILLIAMPERE, "MILLIAMPERE", "mA");

    /** ElectricalCurrent.KILOAMPERE unit type with code 4. */
    public static final UnitType ELECTRICALCURRENT_KILOAMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 4, ElectricalCurrentUnit.KILOAMPERE, "KILOAMPERE", "kA");

    /** ElectricalCurrent.MEGAAMPERE unit type with code 5. */
    public static final UnitType ELECTRICALCURRENT_MEGAAMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 5, ElectricalCurrentUnit.MEGAAMPERE, "MEGAAMPERE", "MA");

    /** ElectricalCurrent.ABAMPERE unit type with code 6. */
    public static final UnitType ELECTRICALCURRENT_ABAMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 6, ElectricalCurrentUnit.ABAMPERE, "ABAMPERE", "abA");

    /** ElectricalCurrent.STATAMPERE unit type with code 7. */
    public static final UnitType ELECTRICALCURRENT_STATAMPERE =
            new UnitType(QuantityType.ELECTRICALCURRENT, 7, ElectricalCurrentUnit.STATAMPERE, "STATAMPERE", "statA");

    /* ============================================ ELECTRICALPOTENTIAL ================================================ */

    /** ElectricalPotential.VOLT unit type with code 0. */
    public static final UnitType ELECTRICALPOTENTIAL_VOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 0, ElectricalPotentialUnit.VOLT, "VOLT", "V");

    /** ElectricalPotential.NANOVOLT unit type with code 1. */
    public static final UnitType ELECTRICALPOTENTIAL_NANOVOLT = new UnitType(QuantityType.ELECTRICALPOTENTIAL, 1,
            ElectricalPotentialUnit.BASE.getUnitByAbbreviation("nV"), "NANOVOLT", "nV");

    /** ElectricalPotential.MICROVOLT unit type with code 2. */
    public static final UnitType ELECTRICALPOTENTIAL_MICROVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 2, ElectricalPotentialUnit.MICROVOLT, "MICROVOLT", "μV");

    /** ElectricalPotential.MILLIVOLT unit type with code 3. */
    public static final UnitType ELECTRICALPOTENTIAL_MILLIVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 3, ElectricalPotentialUnit.MILLIVOLT, "MILLIVOLT", "mV");

    /** ElectricalPotential.KILOVOLT unit type with code 4. */
    public static final UnitType ELECTRICALPOTENTIAL_KILOVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 4, ElectricalPotentialUnit.KILOVOLT, "KILOVOLT", "kV");

    /** ElectricalPotential.MEGAVOLT unit type with code 5. */
    public static final UnitType ELECTRICALPOTENTIAL_MEGAVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 5, ElectricalPotentialUnit.MEGAVOLT, "MEGAVOLT", "MV");

    /** ElectricalPotential.GIGAVOLT unit type with code 6. */
    public static final UnitType ELECTRICALPOTENTIAL_GIGAVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 6, ElectricalPotentialUnit.GIGAVOLT, "GIGAVOLT", "GV");

    /** ElectricalPotential.ABVOLT unit type with code 7. */
    public static final UnitType ELECTRICALPOTENTIAL_ABVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 7, ElectricalPotentialUnit.ABVOLT, "ABVOLT", "abV");

    /** ElectricalPotential.STATVOLT unit type with code 8. */
    public static final UnitType ELECTRICALPOTENTIAL_STATVOLT =
            new UnitType(QuantityType.ELECTRICALPOTENTIAL, 8, ElectricalPotentialUnit.STATVOLT, "STATVOLT", "statV");

    /* =========================================== ELECTRICALRESISTANCE ================================================ */

    /** ElectricalResistance.OHM unit type with code 0. */
    public static final UnitType ELECTRICALRESISTANCE_OHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 0, ElectricalResistanceUnit.OHM, "OHM", "Ω");

    /** ElectricalResistance.NANOOHM unit type with code 1. */
    public static final UnitType ELECTRICALRESISTANCE_NANOOHM = new UnitType(QuantityType.ELECTRICALRESISTANCE, 1,
            ElectricalResistanceUnit.BASE.getUnitByAbbreviation("nohm"), "NANOOHM", "nΩ");

    /** ElectricalResistance.MICROOHM unit type with code 2. */
    public static final UnitType ELECTRICALRESISTANCE_MICROOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 2, ElectricalResistanceUnit.MICROOHM, "MICROOHM", "μΩ");

    /** ElectricalResistance.MILLIOHM unit type with code 3. */
    public static final UnitType ELECTRICALRESISTANCE_MILLIOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 3, ElectricalResistanceUnit.MILLIOHM, "MILLIOHM", "mΩ");

    /** ElectricalResistance.KILOOHM unit type with code 4. */
    public static final UnitType ELECTRICALRESISTANCE_KILOOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 4, ElectricalResistanceUnit.KILOOHM, "KILOOHM", "kΩ");

    /** ElectricalResistance.MEGAOHM unit type with code 5. */
    public static final UnitType ELECTRICALRESISTANCE_MEGAOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 5, ElectricalResistanceUnit.MEGAOHM, "MEGAOHM", "MΩ");

    /** ElectricalResistance.GIGAOHM unit type with code 6. */
    public static final UnitType ELECTRICALRESISTANCE_GIGAOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 6, ElectricalResistanceUnit.GIGAOHM, "GIGAOHM", "GΩ");

    /** ElectricalResistance.ABOHM unit type with code 7. */
    public static final UnitType ELECTRICALRESISTANCE_ABOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 7, ElectricalResistanceUnit.ABOHM, "ABOHM", "abΩ");

    /** ElectricalResistance.STATOHM unit type with code 8. */
    public static final UnitType ELECTRICALRESISTANCE_STATOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 8, ElectricalResistanceUnit.STATOHM, "STATOHM", "statΩ");

    /* ==================================================== ENERGY ===================================================== */

    /** Energy.JOULE unit type with code 0. */
    public static final UnitType ENERGY_JOULE =
            new UnitType(QuantityType.ENERGY, 0, EnergyUnit.JOULE, "JOULE", "J");

    /** Energy.PICOJOULE unit type with code 1. */
    public static final UnitType ENERGY_PICOJOULE =
            new UnitType(QuantityType.ENERGY, 1, EnergyUnit.BASE.getUnitByAbbreviation("pJ"), "PICOJOULE", "pJ");

    /** Energy.NANOJOULE unit type with code 2. */
    public static final UnitType ENERGY_NANOJOULE =
            new UnitType(QuantityType.ENERGY, 2, EnergyUnit.BASE.getUnitByAbbreviation("nJ"), "NANOJOULE", "nJ");

    /** Energy.MICROJOULE unit type with code 3. */
    public static final UnitType ENERGY_MICROJOULE =
            new UnitType(QuantityType.ENERGY, 3, EnergyUnit.MICROJOULE, "MICROJOULE", "μJ");

    /** Energy.MILLIJOULE unit type with code 4. */
    public static final UnitType ENERGY_MILLIJOULE =
            new UnitType(QuantityType.ENERGY, 4, EnergyUnit.MILLIJOULE, "MILLIJOULE", "mJ");

    /** Energy.KILOJOULE unit type with code 5. */
    public static final UnitType ENERGY_KILOJOULE =
            new UnitType(QuantityType.ENERGY, 5, EnergyUnit.KILOJOULE, "KILOJOULE", "kJ");

    /** Energy.MEGAJOULE unit type with code 6. */
    public static final UnitType ENERGY_MEGAJOULE =
            new UnitType(QuantityType.ENERGY, 6, EnergyUnit.MEGAJOULE, "MEGAJOULE", "MJ");

    /** Energy.GIGAJOULE unit type with code 7. */
    public static final UnitType ENERGY_GIGAJOULE =
            new UnitType(QuantityType.ENERGY, 7, EnergyUnit.GIGAJOULE, "GIGAJOULE", "GJ");

    /** Energy.TERAJOULE unit type with code 8. */
    public static final UnitType ENERGY_TERAJOULE =
            new UnitType(QuantityType.ENERGY, 8, EnergyUnit.TERAJOULE, "TERAJOULE", "TJ");

    /** Energy.PETAJOULE unit type with code 9. */
    public static final UnitType ENERGY_PETAJOULE =
            new UnitType(QuantityType.ENERGY, 9, EnergyUnit.PETAJOULE, "PETAJOULE", "PJ");

    /** Energy.ELECTRONVOLT unit type with code 10. */
    public static final UnitType ENERGY_ELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 10, EnergyUnit.ELECTRONVOLT, "ELECTRONVOLT", "eV");

    /** Energy.MICROELECTRONVOLT unit type with code 11. */
    public static final UnitType ENERGY_MICROELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 11, EnergyUnit.MICROELECTRONVOLT, "MICROELECTRONVOLT", "μeV");

    /** Energy.MILLIELECTRONVOLT unit type with code 12. */
    public static final UnitType ENERGY_MILLIELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 12, EnergyUnit.MILLIELECTRONVOLT, "MILLIELECTRONVOLT", "meV");

    /** Energy.KILOELECTRONVOLT unit type with code 13. */
    public static final UnitType ENERGY_KILOELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 13, EnergyUnit.KILOELECTRONVOLT, "KILOELECTRONVOLT", "keV");

    /** Energy.MEGAELECTRONVOLT unit type with code 14. */
    public static final UnitType ENERGY_MEGAELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 14, EnergyUnit.MEGAELECTRONVOLT, "MEGAELECTRONVOLT", "MeV");

    /** Energy.GIGAELECTRONVOLT unit type with code 15. */
    public static final UnitType ENERGY_GIGAELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 15, EnergyUnit.GIGAELECTRONVOLT, "GIGAELECTRONVOLT", "GeV");

    /** Energy.TERAELECTRONVOLT unit type with code 16. */
    public static final UnitType ENERGY_TERAELECTRONVOLT = new UnitType(QuantityType.ENERGY, 16,
            EnergyUnit.BASE.getUnitByAbbreviation("TeV"), "TERAELECTRONVOLT", "TeV");

    /** Energy.PETAELECTRONVOLT unit type with code 17. */
    public static final UnitType ENERGY_PETAELECTRONVOLT = new UnitType(QuantityType.ENERGY, 17,
            EnergyUnit.BASE.getUnitByAbbreviation("PeV"), "PETAELECTRONVOLT", "PeV");

    /** Energy.EXAELECTRONVOLT unit type with code 18. */
    public static final UnitType ENERGY_EXAELECTRONVOLT = new UnitType(QuantityType.ENERGY, 18,
            EnergyUnit.BASE.getUnitByAbbreviation("EeV"), "EXAELECTRONVOLT", "EeV");

    /** Energy.WATT_HOUR unit type with code 19. */
    public static final UnitType ENERGY_WATT_HOUR =
            new UnitType(QuantityType.ENERGY, 19, EnergyUnit.WATT_HOUR, "WATT_HOUR", "Wh");

    /** Energy.FEMTOWATT_HOUR unit type with code 20. */
    public static final UnitType ENERGY_FEMTOWATT_HOUR = new UnitType(QuantityType.ENERGY, 20,
            EnergyUnit.BASE.getUnitByAbbreviation("fWh"), "FEMTOWATT_HOUR", "fWh");

    /** Energy.PICOWATT_HOUR unit type with code 21. */
    public static final UnitType ENERGY_PICOWATT_HOUR = new UnitType(QuantityType.ENERGY, 21,
            EnergyUnit.BASE.getUnitByAbbreviation("pWh"), "PICOWATT_HOUR", "pWh");

    /** Energy.NANOWATT_HOUR unit type with code 22. */
    public static final UnitType ENERGY_NANOWATT_HOUR = new UnitType(QuantityType.ENERGY, 22,
            EnergyUnit.BASE.getUnitByAbbreviation("nWh"), "NANOWATT_HOUR", "nWh");

    /** Energy.MICROWATT_HOUR unit type with code 23. */
    public static final UnitType ENERGY_MICROWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 23, EnergyUnit.MICROWATT_HOUR, "MICROWATT_HOUR", "μWh");

    /** Energy.MILLIWATT_HOUR unit type with code 24. */
    public static final UnitType ENERGY_MILLIWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 24, EnergyUnit.MILLIWATT_HOUR, "MILLIWATT_HOUR", "mWh");

    /** Energy.KILOWATT_HOUR unit type with code 25. */
    public static final UnitType ENERGY_KILOWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 25, EnergyUnit.KILOWATT_HOUR, "KILOWATT_HOUR", "kWh");

    /** Energy.MEGAWATT_HOUR unit type with code 26. */
    public static final UnitType ENERGY_MEGAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 26, EnergyUnit.MEGAWATT_HOUR, "MEGAWATT_HOUR", "MWh");

    /** Energy.GIGAWATT_HOUR unit type with code 27. */
    public static final UnitType ENERGY_GIGAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 27, EnergyUnit.GIGAWATT_HOUR, "GIGAWATT_HOUR", "GWh");

    /** Energy.TERAWATT_HOUR unit type with code 28. */
    public static final UnitType ENERGY_TERAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 28, EnergyUnit.TERAWATT_HOUR, "TERAWATT_HOUR", "TWh");

    /** Energy.PETAWATT_HOUR unit type with code 29. */
    public static final UnitType ENERGY_PETAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 29, EnergyUnit.PETAWATT_HOUR, "PETAWATT_HOUR", "PWh");

    /** Energy.CALORIE unit type with code 30. */
    public static final UnitType ENERGY_CALORIE =
            new UnitType(QuantityType.ENERGY, 30, EnergyUnit.CALORIE, "CALORIE", "cal");

    /** Energy.KILOCALORIE unit type with code 31. */
    public static final UnitType ENERGY_KILOCALORIE =
            new UnitType(QuantityType.ENERGY, 31, EnergyUnit.KILOCALORIE, "KILOCALORIE", "kcal");

    /** Energy.CALORIE_IT unit type with code 32. */
    public static final UnitType ENERGY_CALORIE_IT =
            new UnitType(QuantityType.ENERGY, 32, EnergyUnit.CALORIE_IT, "CALORIE_IT", "cal(IT)");

    /** Energy.INCH_POUND_FORCE unit type with code 33. */
    public static final UnitType ENERGY_INCH_POUND_FORCE =
            new UnitType(QuantityType.ENERGY, 33, EnergyUnit.INCH_POUND_FORCE, "INCH_POUND_FORCE", "in lbf");

    /** Energy.FOOT_POUND_FORCE unit type with code 34. */
    public static final UnitType ENERGY_FOOT_POUND_FORCE =
            new UnitType(QuantityType.ENERGY, 34, EnergyUnit.FOOT_POUND_FORCE, "FOOT_POUND_FORCE", "ft lbf");

    /** Energy.ERG unit type with code 35. */
    public static final UnitType ENERGY_ERG = new UnitType(QuantityType.ENERGY, 35, EnergyUnit.ERG, "ERG", "erg");

    /** Energy.BTU_ISO unit type with code 36. */
    public static final UnitType ENERGY_BTU_ISO =
            new UnitType(QuantityType.ENERGY, 36, EnergyUnit.BTU_ISO, "BTU_ISO", "BTU(ISO)");

    /** Energy.BTU_IT unit type with code 37. */
    public static final UnitType ENERGY_BTU_IT =
            new UnitType(QuantityType.ENERGY, 37, EnergyUnit.BTU_IT, "BTU_IT", "BTU(IT)");

    /** Energy.STHENE_METER unit type with code 38. */
    public static final UnitType ENERGY_STHENE_METER =
            new UnitType(QuantityType.ENERGY, 38, EnergyUnit.STHENE_METER, "STHENE_METER", "sth.m");

    /* =================================================== FLOWMASS ==================================================== */

    /** FlowMass.KG_PER_SECOND unit type with code 0. */
    public static final UnitType FLOWMASS_KG_PER_SECOND =
            new UnitType(QuantityType.FLOWMASS, 0, FlowMassUnit.KILOGRAM_PER_SECOND, "KG_PER_SECOND", "kg/s");

    /** FlowMass.POUND_PER_SECOND unit type with code 1. */
    public static final UnitType FLOWMASS_POUND_PER_SECOND =
            new UnitType(QuantityType.FLOWMASS, 1, FlowMassUnit.POUND_PER_SECOND, "POUND_PER_SECOND", "lb/s");

    /* ================================================== FLOWVOLUME =================================================== */

    /** FlowVolume.CUBIC_METER_PER_SECOND unit type with code 0. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_SECOND = new UnitType(QuantityType.FLOWVOLUME, 0,
            FlowVolumeUnit.CUBIC_METER_PER_SECOND, "CUBIC_METER_PER_SECOND", "m3/s");

    /** FlowVolume.CUBIC_METER_PER_MINUTE unit type with code 1. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_MINUTE = new UnitType(QuantityType.FLOWVOLUME, 1,
            FlowVolumeUnit.CUBIC_METER_PER_MINUTE, "CUBIC_METER_PER_MINUTE", "m3/min");

    /** FlowVolume.CUBIC_METER_PER_HOUR unit type with code 2. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_HOUR = new UnitType(QuantityType.FLOWVOLUME, 2,
            FlowVolumeUnit.CUBIC_METER_PER_HOUR, "CUBIC_METER_PER_HOUR", "m3/h");

    /** FlowVolume.CUBIC_METER_PER_DAY unit type with code 3. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_DAY = new UnitType(QuantityType.FLOWVOLUME, 3,
            FlowVolumeUnit.CUBIC_METER_PER_DAY, "CUBIC_METER_PER_DAY", "m3/day");

    /** FlowVolume.CUBIC_INCH_PER_SECOND unit type with code 4. */
    public static final UnitType FLOWVOLUME_CUBIC_INCH_PER_SECOND = new UnitType(QuantityType.FLOWVOLUME, 4,
            FlowVolumeUnit.CUBIC_INCH_PER_SECOND, "CUBIC_INCH_PER_SECOND", "in3/s");

    /** FlowVolume.CUBIC_INCH_PER_MINUTE unit type with code 5. */
    public static final UnitType FLOWVOLUME_CUBIC_INCH_PER_MINUTE = new UnitType(QuantityType.FLOWVOLUME, 5,
            FlowVolumeUnit.CUBIC_INCH_PER_MINUTE, "CUBIC_INCH_PER_MINUTE", "in3/min");

    /** FlowVolume.CUBIC_FEET_PER_SECOND unit type with code 6. */
    public static final UnitType FLOWVOLUME_CUBIC_FEET_PER_SECOND = new UnitType(QuantityType.FLOWVOLUME, 6,
            FlowVolumeUnit.CUBIC_FEET_PER_SECOND, "CUBIC_FEET_PER_SECOND", "ft3/s");

    /** FlowVolume.CUBIC_FEET_PER_MINUTE unit type with code 7. */
    public static final UnitType FLOWVOLUME_CUBIC_FEET_PER_MINUTE = new UnitType(QuantityType.FLOWVOLUME, 7,
            FlowVolumeUnit.CUBIC_FEET_PER_MINUTE, "CUBIC_FEET_PER_MINUTE", "ft3/min");

    /** FlowVolume.GALLON_PER_SECOND unit type with code 8. */
    public static final UnitType FLOWVOLUME_GALLON_PER_SECOND = new UnitType(QuantityType.FLOWVOLUME, 8,
            FlowVolumeUnit.GALLON_US_PER_SECOND, "GALLON_PER_SECOND", "gal/s");

    /** FlowVolume.GALLON_PER_MINUTE unit type with code 9. */
    public static final UnitType FLOWVOLUME_GALLON_PER_MINUTE = new UnitType(QuantityType.FLOWVOLUME, 9,
            FlowVolumeUnit.GALLON_US_PER_MINUTE, "GALLON_PER_MINUTE", "gal/min");

    /** FlowVolume.GALLON_PER_HOUR unit type with code 10. */
    public static final UnitType FLOWVOLUME_GALLON_PER_HOUR =
            new UnitType(QuantityType.FLOWVOLUME, 10, FlowVolumeUnit.GALLON_US_PER_HOUR, "GALLON_PER_HOUR", "gal/h");

    /** FlowVolume.GALLON_PER_DAY unit type with code 11. */
    public static final UnitType FLOWVOLUME_GALLON_PER_DAY =
            new UnitType(QuantityType.FLOWVOLUME, 11, FlowVolumeUnit.GALLON_US_PER_DAY, "GALLON_PER_DAY", "gal/day");

    /** FlowVolume.LITER_PER_SECOND unit type with code 12. */
    public static final UnitType FLOWVOLUME_LITER_PER_SECOND =
            new UnitType(QuantityType.FLOWVOLUME, 12, FlowVolumeUnit.LITER_PER_SECOND, "LITER_PER_SECOND", "l/s");

    /** FlowVolume.LITER_PER_MINUTE unit type with code 13. */
    public static final UnitType FLOWVOLUME_LITER_PER_MINUTE =
            new UnitType(QuantityType.FLOWVOLUME, 13, FlowVolumeUnit.LITER_PER_MINUTE, "LITER_PER_MINUTE", "l/min");

    /** FlowVolume.LITER_PER_HOUR unit type with code 14. */
    public static final UnitType FLOWVOLUME_LITER_PER_HOUR =
            new UnitType(QuantityType.FLOWVOLUME, 14, FlowVolumeUnit.LITER_PER_HOUR, "LITER_PER_HOUR", "l/h");

    /** FlowVolume.LITER_PER_DAY unit type with code 15. */
    public static final UnitType FLOWVOLUME_LITER_PER_DAY =
            new UnitType(QuantityType.FLOWVOLUME, 15, FlowVolumeUnit.LITER_PER_DAY, "LITER_PER_DAY", "l/day");

    /* ==================================================== FORCE ====================================================== */

    /** Force.NEWTON unit type with code 0. */
    public static final UnitType FORCE_NEWTON =
            new UnitType(QuantityType.FORCE, 0, ForceUnit.NEWTON, "NEWTON", "N");

    /** Force.KILOGRAM_FORCE unit type with code 1. */
    public static final UnitType FORCE_KILOGRAM_FORCE =
            new UnitType(QuantityType.FORCE, 1, ForceUnit.KILOGRAM_FORCE, "KILOGRAM_FORCE", "kgf");

    /** Force.OUNCE_FORCE unit type with code 2. */
    public static final UnitType FORCE_OUNCE_FORCE =
            new UnitType(QuantityType.FORCE, 2, ForceUnit.OUNCE_FORCE, "OUNCE_FORCE", "ozf");

    /** Force.POUND_FORCE unit type with code 3. */
    public static final UnitType FORCE_POUND_FORCE =
            new UnitType(QuantityType.FORCE, 3, ForceUnit.POUND_FORCE, "POUND_FORCE", "lbf");

    /** Force.TON_FORCE unit type with code 4. */
    public static final UnitType FORCE_TON_FORCE =
            new UnitType(QuantityType.FORCE, 4, ForceUnit.TON_FORCE, "TON_FORCE", "tnf");

    /** Force.DYNE unit type with code 5. */
    public static final UnitType FORCE_DYNE = new UnitType(QuantityType.FORCE, 5, ForceUnit.DYNE, "DYNE", "dyne");

    /** Force.STHENE unit type with code 6. */
    public static final UnitType FORCE_STHENE =
            new UnitType(QuantityType.FORCE, 6, ForceUnit.STHENE, "STHENE", "sth");

    /* ================================================== FREQUENCY ==================================================== */

    /** Frequency.HERTZ unit type with code 0. */
    public static final UnitType FREQUENCY_HERTZ =
            new UnitType(QuantityType.FREQUENCY, 0, FrequencyUnit.HERTZ, "HERTZ", "Hz");

    /** Frequency.KILOHERTZ unit type with code 1. */
    public static final UnitType FREQUENCY_KILOHERTZ =
            new UnitType(QuantityType.FREQUENCY, 1, FrequencyUnit.KILOHERTZ, "KILOHERTZ", "kHz");

    /** Frequency.MEGAHERTZ unit type with code 2. */
    public static final UnitType FREQUENCY_MEGAHERTZ =
            new UnitType(QuantityType.FREQUENCY, 2, FrequencyUnit.MEGAHERTZ, "MEGAHERTZ", "MHz");

    /** Frequency.GIGAHERTZ unit type with code 3. */
    public static final UnitType FREQUENCY_GIGAHERTZ =
            new UnitType(QuantityType.FREQUENCY, 3, FrequencyUnit.GIGAHERTZ, "GIGAHERTZ", "GHz");

    /** Frequency.TERAHERTZ unit type with code 4. */
    public static final UnitType FREQUENCY_TERAHERTZ =
            new UnitType(QuantityType.FREQUENCY, 4, FrequencyUnit.TERAHERTZ, "TERAHERTZ", "THz");

    /** Frequency.PER_SECOND unit type with code 5. */
    public static final UnitType FREQUENCY_PER_SECOND =
            new UnitType(QuantityType.FREQUENCY, 5, FrequencyUnit.PER_SECOND, "PER_SECOND", "1/s");

    /** Frequency.PER_ATTOSECOND unit type with code 6. */
    public static final UnitType FREQUENCY_PER_ATTOSECOND = new UnitType(QuantityType.FREQUENCY, 6,
            FrequencyUnit.BASE.getUnitByAbbreviation("/as"), "PER_ATTOSECOND", "1/as");

    /** Frequency.PER_FEMTOSECOND unit type with code 7. */
    public static final UnitType FREQUENCY_PER_FEMTOSECOND = new UnitType(QuantityType.FREQUENCY, 7,
            FrequencyUnit.BASE.getUnitByAbbreviation("/fs"), "PER_FEMTOSECOND", "1/fs");

    /** Frequency.PER_PICOSECOND unit type with code 8. */
    public static final UnitType FREQUENCY_PER_PICOSECOND = new UnitType(QuantityType.FREQUENCY, 8,
            FrequencyUnit.BASE.getUnitByAbbreviation("/ps"), "PER_PICOSECOND", "1/ps");

    /** Frequency.PER_NANOSECOND unit type with code 9. */
    public static final UnitType FREQUENCY_PER_NANOSECOND = new UnitType(QuantityType.FREQUENCY, 9,
            FrequencyUnit.BASE.getUnitByAbbreviation("/ns"), "PER_NANOSECOND", "1/ns");

    /** Frequency.PER_MICROSECOND unit type with code 10. */
    public static final UnitType FREQUENCY_PER_MICROSECOND =
            new UnitType(QuantityType.FREQUENCY, 10, FrequencyUnit.PER_MICROSECOND, "PER_MICROSECOND", "1/μs");

    /** Frequency.PER_MILLISECOND unit type with code 11. */
    public static final UnitType FREQUENCY_PER_MILLISECOND =
            new UnitType(QuantityType.FREQUENCY, 11, FrequencyUnit.PER_MILLISECOND, "PER_MILLISECOND", "1/ms");

    /** Frequency.PER_MINUTE unit type with code 12. */
    public static final UnitType FREQUENCY_PER_MINUTE =
            new UnitType(QuantityType.FREQUENCY, 12, FrequencyUnit.PER_MINUTE, "PER_MINUTE", "1/min");

    /** Frequency.PER_HOUR unit type with code 13. */
    public static final UnitType FREQUENCY_PER_HOUR =
            new UnitType(QuantityType.FREQUENCY, 13, FrequencyUnit.PER_HOUR, "PER_HOUR", "1/hr");

    /** Frequency.PER_DAY unit type with code 14. */
    public static final UnitType FREQUENCY_PER_DAY =
            new UnitType(QuantityType.FREQUENCY, 14, FrequencyUnit.PER_DAY, "PER_DAY", "1/day");

    /** Frequency.PER_WEEK unit type with code 15. */
    public static final UnitType FREQUENCY_PER_WEEK =
            new UnitType(QuantityType.FREQUENCY, 15, FrequencyUnit.PER_WEEK, "PER_WEEK", "1/wk");

    /** Frequency.RPM unit type with code 16. */
    public static final UnitType FREQUENCY_RPM =
            new UnitType(QuantityType.FREQUENCY, 16, FrequencyUnit.RPM, "RPM", "rpm");

    /* ==================================================== LENGTH ===================================================== */

    /** Length.METER unit type with code 0. */
    public static final UnitType LENGTH_METER =
            new UnitType(QuantityType.LENGTH, 0, LengthUnit.METER, "METER", "m");

    /** Length.ATTOMETER unit type with code 1. */
    public static final UnitType LENGTH_ATTOMETER =
            new UnitType(QuantityType.LENGTH, 1, LengthUnit.BASE.getUnitByAbbreviation("am"), "ATTOMETER", "am");

    /** Length.FEMTOMETER unit type with code 2. */
    public static final UnitType LENGTH_FEMTOMETER =
            new UnitType(QuantityType.LENGTH, 2, LengthUnit.BASE.getUnitByAbbreviation("fm"), "FEMTOMETER", "fm");

    /** Length.PICOMETER unit type with code 3. */
    public static final UnitType LENGTH_PICOMETER =
            new UnitType(QuantityType.LENGTH, 3, LengthUnit.BASE.getUnitByAbbreviation("pm"), "PICOMETER", "pm");

    /** Length.NANOMETER unit type with code 4. */
    public static final UnitType LENGTH_NANOMETER =
            new UnitType(QuantityType.LENGTH, 4, LengthUnit.NANOMETER, "NANOMETER", "nm");

    /** Length.MICROMETER unit type with code 5. */
    public static final UnitType LENGTH_MICROMETER =
            new UnitType(QuantityType.LENGTH, 5, LengthUnit.MICROMETER, "MICROMETER", "μm");

    /** Length.MILLIMETER unit type with code 6. */
    public static final UnitType LENGTH_MILLIMETER =
            new UnitType(QuantityType.LENGTH, 6, LengthUnit.MILLIMETER, "MILLIMETER", "mm");

    /** Length.CENTIMETER unit type with code 7. */
    public static final UnitType LENGTH_CENTIMETER =
            new UnitType(QuantityType.LENGTH, 7, LengthUnit.CENTIMETER, "CENTIMETER", "cm");

    /** Length.DECIMETER unit type with code 8. */
    public static final UnitType LENGTH_DECIMETER =
            new UnitType(QuantityType.LENGTH, 8, LengthUnit.DECIMETER, "DECIMETER", "dm");

    /** Length.DEKAMETER unit type with code 9. */
    public static final UnitType LENGTH_DEKAMETER =
            new UnitType(QuantityType.LENGTH, 9, LengthUnit.BASE.getUnitByAbbreviation("dam"), "DEKAMETER", "dam");

    /** Length.HECTOMETER unit type with code 10. */
    public static final UnitType LENGTH_HECTOMETER =
            new UnitType(QuantityType.LENGTH, 10, LengthUnit.HECTOMETER, "HECTOMETER", "hm");

    /** Length.KILOMETER unit type with code 11. */
    public static final UnitType LENGTH_KILOMETER =
            new UnitType(QuantityType.LENGTH, 11, LengthUnit.KILOMETER, "KILOMETER", "km");

    /** Length.MEGAMETER unit type with code 12. */
    public static final UnitType LENGTH_MEGAMETER =
            new UnitType(QuantityType.LENGTH, 12, LengthUnit.BASE.getUnitByAbbreviation("Mm"), "MEGAMETER", "Mm");

    /** Length.INCH unit type with code 13. */
    public static final UnitType LENGTH_INCH = new UnitType(QuantityType.LENGTH, 13, LengthUnit.INCH, "INCH", "in");

    /** Length.FOOT unit type with code 14. */
    public static final UnitType LENGTH_FOOT = new UnitType(QuantityType.LENGTH, 14, LengthUnit.FOOT, "FOOT", "ft");

    /** Length.YARD unit type with code 15. */
    public static final UnitType LENGTH_YARD = new UnitType(QuantityType.LENGTH, 15, LengthUnit.YARD, "YARD", "yd");

    /** Length.MILE unit type with code 16. */
    public static final UnitType LENGTH_MILE = new UnitType(QuantityType.LENGTH, 16, LengthUnit.MILE, "MILE", "mi");

    /** Length.NAUTICAL_MILE unit type with code 17. */
    public static final UnitType LENGTH_NAUTICAL_MILE =
            new UnitType(QuantityType.LENGTH, 17, LengthUnit.NAUTICAL_MILE, "NAUTICAL_MILE", "NM");

    /** Length.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitType LENGTH_ASTRONOMICAL_UNIT =
            new UnitType(QuantityType.LENGTH, 18, LengthUnit.ASTRONOMICAL_UNIT, "ASTRONOMICAL_UNIT", "au");

    /** Length.PARSEC unit type with code 19. */
    public static final UnitType LENGTH_PARSEC =
            new UnitType(QuantityType.LENGTH, 19, LengthUnit.PARSEC, "PARSEC", "pc");

    /** Length.LIGHTYEAR unit type with code 20. */
    public static final UnitType LENGTH_LIGHTYEAR =
            new UnitType(QuantityType.LENGTH, 20, LengthUnit.LIGHTYEAR, "LIGHTYEAR", "ly");

    /** Length.ANGSTROM unit type with code 21. */
    public static final UnitType LENGTH_ANGSTROM =
            new UnitType(QuantityType.LENGTH, 21, LengthUnit.ANGSTROM, "ANGSTROM", "Å");

    /* =================================================== POSITION ==================================================== */

    /** Position.METER unit type with code 0. */
    public static final UnitType POSITION_METER =
            new UnitType(QuantityType.POSITION, 0, PositionUnit.METER, "METER", "m");

    /** Position.ATTOMETER unit type with code 1. */
    public static final UnitType POSITION_ATTOMETER =
            new UnitType(QuantityType.POSITION, 1, PositionUnit.BASE.getUnitByAbbreviation("am"), "ATTOMETER", "am");

    /** Position.FEMTOMETER unit type with code 2. */
    public static final UnitType POSITION_FEMTOMETER =
            new UnitType(QuantityType.POSITION, 2, PositionUnit.BASE.getUnitByAbbreviation("fm"), "FEMTOMETER", "fm");

    /** Position.PICOMETER unit type with code 3. */
    public static final UnitType POSITION_PICOMETER =
            new UnitType(QuantityType.POSITION, 3, PositionUnit.BASE.getUnitByAbbreviation("pm"), "PICOMETER", "pm");

    /** Position.NANOMETER unit type with code 4. */
    public static final UnitType POSITION_NANOMETER =
            new UnitType(QuantityType.POSITION, 4, PositionUnit.NANOMETER, "NANOMETER", "nm");

    /** Position.MICROMETER unit type with code 5. */
    public static final UnitType POSITION_MICROMETER =
            new UnitType(QuantityType.POSITION, 5, PositionUnit.MICROMETER, "MICROMETER", "μm");

    /** Position.MILLIMETER unit type with code 6. */
    public static final UnitType POSITION_MILLIMETER =
            new UnitType(QuantityType.POSITION, 6, PositionUnit.MILLIMETER, "MILLIMETER", "mm");

    /** Position.CENTIMETER unit type with code 7. */
    public static final UnitType POSITION_CENTIMETER =
            new UnitType(QuantityType.POSITION, 7, PositionUnit.CENTIMETER, "CENTIMETER", "cm");

    /** Position.DECIMETER unit type with code 8. */
    public static final UnitType POSITION_DECIMETER =
            new UnitType(QuantityType.POSITION, 8, PositionUnit.DECIMETER, "DECIMETER", "dm");

    /** Position.DEKAMETER unit type with code 9. */
    public static final UnitType POSITION_DEKAMETER =
            new UnitType(QuantityType.POSITION, 9, PositionUnit.BASE.getUnitByAbbreviation("dam"), "DEKAMETER", "dam");

    /** Position.HECTOMETER unit type with code 10. */
    public static final UnitType POSITION_HECTOMETER =
            new UnitType(QuantityType.POSITION, 10, PositionUnit.HECTOMETER, "HECTOMETER", "hm");

    /** Position.KILOMETER unit type with code 11. */
    public static final UnitType POSITION_KILOMETER =
            new UnitType(QuantityType.POSITION, 11, PositionUnit.KILOMETER, "KILOMETER", "km");

    /** Position.MEGAMETER unit type with code 12. */
    public static final UnitType POSITION_MEGAMETER =
            new UnitType(QuantityType.POSITION, 12, PositionUnit.BASE.getUnitByAbbreviation("Mm"), "MEGAMETER", "Mm");

    /** Position.INCH unit type with code 13. */
    public static final UnitType POSITION_INCH =
            new UnitType(QuantityType.POSITION, 13, PositionUnit.INCH, "INCH", "in");

    /** Position.FOOT unit type with code 14. */
    public static final UnitType POSITION_FOOT =
            new UnitType(QuantityType.POSITION, 14, PositionUnit.FOOT, "FOOT", "ft");

    /** Position.YARD unit type with code 15. */
    public static final UnitType POSITION_YARD =
            new UnitType(QuantityType.POSITION, 15, PositionUnit.YARD, "YARD", "yd");

    /** Position.MILE unit type with code 16. */
    public static final UnitType POSITION_MILE =
            new UnitType(QuantityType.POSITION, 16, PositionUnit.MILE, "MILE", "mi");

    /** Position.NAUTICAL_MILE unit type with code 17. */
    public static final UnitType POSITION_NAUTICAL_MILE =
            new UnitType(QuantityType.POSITION, 17, PositionUnit.NAUTICAL_MILE, "NAUTICAL_MILE", "NM");

    /** Position.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitType POSITION_ASTRONOMICAL_UNIT =
            new UnitType(QuantityType.POSITION, 18, PositionUnit.ASTRONOMICAL_UNIT, "ASTRONOMICAL_UNIT", "au");

    /** Position.PARSEC unit type with code 19. */
    public static final UnitType POSITION_PARSEC =
            new UnitType(QuantityType.POSITION, 19, PositionUnit.PARSEC, "PARSEC", "pc");

    /** Position.LIGHTYEAR unit type with code 20. */
    public static final UnitType POSITION_LIGHTYEAR =
            new UnitType(QuantityType.POSITION, 20, PositionUnit.LIGHTYEAR, "LIGHTYEAR", "ly");

    /** Position.ANGSTROM unit type with code 21. */
    public static final UnitType POSITION_ANGSTROM =
            new UnitType(QuantityType.POSITION, 21, PositionUnit.ANGSTROM, "ANGSTROM", "Å");

    /* ================================================= LINEARDENSITY ================================================= */

    /** LinearDensity.PER_METER unit type with code 0. */
    public static final UnitType LINEARDENSITY_PER_METER =
            new UnitType(QuantityType.LINEARDENSITY, 0, LinearDensityUnit.PER_METER, "PER_METER", "1/m");

    /** LinearDensity.PER_ATTOMETER unit type with code 1. */
    public static final UnitType LINEARDENSITY_PER_ATTOMETER = new UnitType(QuantityType.LINEARDENSITY, 1,
            LinearDensityUnit.BASE.getUnitByAbbreviation("/am"), "PER_ATTOMETER", "1/am");

    /** LinearDensity.PER_FEMTOMETER unit type with code 2. */
    public static final UnitType LINEARDENSITY_PER_FEMTOMETER = new UnitType(QuantityType.LINEARDENSITY, 2,
            LinearDensityUnit.BASE.getUnitByAbbreviation("/fm"), "PER_FEMTOMETER", "1/fm");

    /** LinearDensity.PER_PICOMETER unit type with code 3. */
    public static final UnitType LINEARDENSITY_PER_PICOMETER = new UnitType(QuantityType.LINEARDENSITY, 3,
            LinearDensityUnit.BASE.getUnitByAbbreviation("/pm"), "PER_PICOMETER", "1/pm");

    /** LinearDensity.PER_NANOMETER unit type with code 4. */
    public static final UnitType LINEARDENSITY_PER_NANOMETER = new UnitType(QuantityType.LINEARDENSITY, 4,
            LinearDensityUnit.BASE.getUnitByAbbreviation("/nm"), "PER_NANOMETER", "1/nm");

    /** LinearDensity.PER_MICROMETER unit type with code 5. */
    public static final UnitType LINEARDENSITY_PER_MICROMETER =
            new UnitType(QuantityType.LINEARDENSITY, 5, LinearDensityUnit.PER_MICROMETER, "PER_MICROMETER", "1/μm");

    /** LinearDensity.PER_MILLIMETER unit type with code 6. */
    public static final UnitType LINEARDENSITY_PER_MILLIMETER =
            new UnitType(QuantityType.LINEARDENSITY, 6, LinearDensityUnit.PER_MILLIMETER, "PER_MILLIMETER", "1/mm");

    /** LinearDensity.PER_CENTIMETER unit type with code 7. */
    public static final UnitType LINEARDENSITY_PER_CENTIMETER =
            new UnitType(QuantityType.LINEARDENSITY, 7, LinearDensityUnit.PER_CENTIMETER, "PER_CENTIMETER", "1/cm");

    /** LinearDensity.PER_DECIMETER unit type with code 8. */
    public static final UnitType LINEARDENSITY_PER_DECIMETER =
            new UnitType(QuantityType.LINEARDENSITY, 8, LinearDensityUnit.PER_DECIMETER, "PER_DECIMETER", "1/dm");

    /** LinearDensity.PER_DEKAMETER unit type with code 9. */
    public static final UnitType LINEARDENSITY_PER_DEKAMETER = new UnitType(QuantityType.LINEARDENSITY, 9,
            LinearDensityUnit.BASE.getUnitByAbbreviation("/dam"), "PER_DEKAMETER", "1/dam");

    /** LinearDensity.PER_HECTOMETER unit type with code 10. */
    public static final UnitType LINEARDENSITY_PER_HECTOMETER =
            new UnitType(QuantityType.LINEARDENSITY, 10, LinearDensityUnit.PER_HECTOMETER, "PER_HECTOMETER", "1/hm");

    /** LinearDensity.PER_KILOMETER unit type with code 11. */
    public static final UnitType LINEARDENSITY_PER_KILOMETER =
            new UnitType(QuantityType.LINEARDENSITY, 11, LinearDensityUnit.PER_KILOMETER, "PER_KILOMETER", "1/km");

    /** LinearDensity.PER_MEGAMETER unit type with code 12. */
    public static final UnitType LINEARDENSITY_PER_MEGAMETER = new UnitType(QuantityType.LINEARDENSITY, 12,
            LinearDensityUnit.BASE.getUnitByAbbreviation("/Mm"), "PER_MEGAMETER", "1/Mm");

    /** LinearDensity.PER_INCH unit type with code 13. */
    public static final UnitType LINEARDENSITY_PER_INCH =
            new UnitType(QuantityType.LINEARDENSITY, 13, LinearDensityUnit.PER_INCH, "PER_INCH", "1/in");

    /** LinearDensity.PER_FOOT unit type with code 14. */
    public static final UnitType LINEARDENSITY_PER_FOOT =
            new UnitType(QuantityType.LINEARDENSITY, 14, LinearDensityUnit.PER_FOOT, "PER_FOOT", "1/ft");

    /** LinearDensity.PER_YARD unit type with code 15. */
    public static final UnitType LINEARDENSITY_PER_YARD =
            new UnitType(QuantityType.LINEARDENSITY, 15, LinearDensityUnit.PER_YARD, "PER_YARD", "1/yd");

    /** LinearDensity.PER_MILE unit type with code 16. */
    public static final UnitType LINEARDENSITY_PER_MILE =
            new UnitType(QuantityType.LINEARDENSITY, 16, LinearDensityUnit.PER_MILE, "PER_MILE", "1/mi");

    /** LinearDensity.PER_NAUTICAL_MILE unit type with code 17. */
    public static final UnitType LINEARDENSITY_PER_NAUTICAL_MILE = new UnitType(QuantityType.LINEARDENSITY, 17,
            LinearDensityUnit.PER_NAUTICAL_MILE, "PER_NAUTICAL_MILE", "1/NM");

    /** LinearDensity.PER_ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitType LINEARDENSITY_PER_ASTRONOMICAL_UNIT = new UnitType(QuantityType.LINEARDENSITY, 18,
            LinearDensityUnit.PER_ASTRONOMICAL_UNIT, "PER_ASTRONOMICAL_UNIT", "1/au");

    /** LinearDensity.PER_PARSEC unit type with code 19. */
    public static final UnitType LINEARDENSITY_PER_PARSEC =
            new UnitType(QuantityType.LINEARDENSITY, 19, LinearDensityUnit.PER_PARSEC, "PER_PARSEC", "1/pc");

    /** LinearDensity.PER_LIGHTYEAR unit type with code 20. */
    public static final UnitType LINEARDENSITY_PER_LIGHTYEAR =
            new UnitType(QuantityType.LINEARDENSITY, 20, LinearDensityUnit.PER_LIGHTYEAR, "PER_LIGHTYEAR", "1/ly");

    /** LinearDensity.PER_ANGSTROM unit type with code 21. */
    public static final UnitType LINEARDENSITY_PER_ANGSTROM =
            new UnitType(QuantityType.LINEARDENSITY, 21, LinearDensityUnit.PER_ANGSTROM, "PER_ANGSTROM", "1/Å");

    /* ===================================================== MASS ====================================================== */

    /** Mass.KILOGRAM unit type with code 0. */
    public static final UnitType MASS_KILOGRAM =
            new UnitType(QuantityType.MASS, 0, MassUnit.KILOGRAM, "KILOGRAM", "kg");

    /** Mass.FEMTOGRAM unit type with code 1. */
    public static final UnitType MASS_FEMTOGRAM =
            new UnitType(QuantityType.MASS, 1, MassUnit.BASE.getUnitByAbbreviation("fg"), "FEMTOGRAM", "fg");

    /** Mass.PICOGRAM unit type with code 2. */
    public static final UnitType MASS_PICOGRAM =
            new UnitType(QuantityType.MASS, 2, MassUnit.BASE.getUnitByAbbreviation("pg"), "PICOGRAM", "pg");

    /** Mass.NANOGRAM unit type with code 3. */
    public static final UnitType MASS_NANOGRAM =
            new UnitType(QuantityType.MASS, 3, MassUnit.BASE.getUnitByAbbreviation("ng"), "NANOGRAM", "ng");

    /** Mass.MICROGRAM unit type with code 4. */
    public static final UnitType MASS_MICROGRAM =
            new UnitType(QuantityType.MASS, 4, MassUnit.MICROGRAM, "MICROGRAM", "μg");

    /** Mass.MILLIGRAM unit type with code 5. */
    public static final UnitType MASS_MILLIGRAM =
            new UnitType(QuantityType.MASS, 5, MassUnit.MILLIGRAM, "MILLIGRAM", "mg");

    /** Mass.GRAM unit type with code 6. */
    public static final UnitType MASS_GRAM = new UnitType(QuantityType.MASS, 6, MassUnit.GRAM, "GRAM", "kg");

    /** Mass.MEGAGRAM unit type with code 7. */
    public static final UnitType MASS_MEGAGRAM =
            new UnitType(QuantityType.MASS, 7, MassUnit.BASE.getUnitByAbbreviation("Mg"), "MEGAGRAM", "Mg");

    /** Mass.GIGAGRAM unit type with code 8. */
    public static final UnitType MASS_GIGAGRAM =
            new UnitType(QuantityType.MASS, 8, MassUnit.BASE.getUnitByAbbreviation("Gg"), "GIGAGRAM", "Gg");

    /** Mass.TERAGRAM unit type with code 9. */
    public static final UnitType MASS_TERAGRAM =
            new UnitType(QuantityType.MASS, 9, MassUnit.BASE.getUnitByAbbreviation("Tg"), "TERAGRAM", "Tg");

    /** Mass.PETAGRAM unit type with code 10. */
    public static final UnitType MASS_PETAGRAM =
            new UnitType(QuantityType.MASS, 10, MassUnit.BASE.getUnitByAbbreviation("Pg"), "PETAGRAM", "Pg");

    /** Mass.MICROELECTRONVOLT unit type with code 11. */
    public static final UnitType MASS_MICROELECTRONVOLT =
            new UnitType(QuantityType.MASS, 11, MassUnit.MICROELECTRONVOLT, "MICROELECTRONVOLT", "μeV");

    /** Mass.MILLIELECTRONVOLT unit type with code 12. */
    public static final UnitType MASS_MILLIELECTRONVOLT =
            new UnitType(QuantityType.MASS, 12, MassUnit.MILLIELECTRONVOLT, "MILLIELECTRONVOLT", "meV");

    /** Mass.ELECTRONVOLT unit type with code 13. */
    public static final UnitType MASS_ELECTRONVOLT =
            new UnitType(QuantityType.MASS, 13, MassUnit.ELECTRONVOLT, "ELECTRONVOLT", "eV");

    /** Mass.KILOELECTRONVOLT unit type with code 14. */
    public static final UnitType MASS_KILOELECTRONVOLT =
            new UnitType(QuantityType.MASS, 14, MassUnit.KILOELECTRONVOLT, "KILOELECTRONVOLT", "keV");

    /** Mass.MEGAELECTRONVOLT unit type with code 15. */
    public static final UnitType MASS_MEGAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 15, MassUnit.MEGAELECTRONVOLT, "MEGAELECTRONVOLT", "MeV");

    /** Mass.GIGAELECTRONVOLT unit type with code 16. */
    public static final UnitType MASS_GIGAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 16, MassUnit.GIGAELECTRONVOLT, "GIGAELECTRONVOLT", "GeV");

    /** Mass.TERAELECTRONVOLT unit type with code 17. */
    public static final UnitType MASS_TERAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 17, MassUnit.BASE.getUnitByAbbreviation("TeV"), "TERAELECTRONVOLT", "TeV");

    /** Mass.PETAELECTRONVOLT unit type with code 18. */
    public static final UnitType MASS_PETAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 18, MassUnit.BASE.getUnitByAbbreviation("PeV"), "PETAELECTRONVOLT", "PeV");

    /** Mass.EXAELECTRONVOLT unit type with code 19. */
    public static final UnitType MASS_EXAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 19, MassUnit.BASE.getUnitByAbbreviation("EeV"), "EXAELECTRONVOLT", "EeV");

    /** Mass.OUNCE unit type with code 20. */
    public static final UnitType MASS_OUNCE = new UnitType(QuantityType.MASS, 20, MassUnit.OUNCE, "OUNCE", "oz");

    /** Mass.POUND unit type with code 21. */
    public static final UnitType MASS_POUND = new UnitType(QuantityType.MASS, 21, MassUnit.POUND, "POUND", "lb");

    /** Mass.DALTON unit type with code 22. */
    public static final UnitType MASS_DALTON = new UnitType(QuantityType.MASS, 22, MassUnit.DALTON, "DALTON", "Da");

    /** Mass.TON_LONG unit type with code 23. */
    public static final UnitType MASS_TON_LONG =
            new UnitType(QuantityType.MASS, 23, MassUnit.TON_LONG, "TON_LONG", "ton (long)");

    /** Mass.TON_SHORT unit type with code 24. */
    public static final UnitType MASS_TON_SHORT =
            new UnitType(QuantityType.MASS, 24, MassUnit.TON_SHORT, "TON_SHORT", "ton (short)");

    /** Mass.TONNE unit type with code 25. */
    public static final UnitType MASS_TONNE = new UnitType(QuantityType.MASS, 25, MassUnit.TONNE, "TONNE", "tonne");

    /* ==================================================== POWER ====================================================== */

    /** Power.WATT unit type with code 0. */
    public static final UnitType POWER_WATT = new UnitType(QuantityType.POWER, 0, PowerUnit.WATT, "WATT", "W");

    /** Power.FEMTOWATT unit type with code 1. */
    public static final UnitType POWER_FEMTOWATT =
            new UnitType(QuantityType.POWER, 1, PowerUnit.BASE.getUnitByAbbreviation("fW"), "FEMTOWATT", "fW");

    /** Power.PICOWATT unit type with code 2. */
    public static final UnitType POWER_PICOWATT =
            new UnitType(QuantityType.POWER, 2, PowerUnit.BASE.getUnitByAbbreviation("pW"), "PICOWATT", "pW");

    /** Power.NANOWATT unit type with code 3. */
    public static final UnitType POWER_NANOWATT =
            new UnitType(QuantityType.POWER, 3, PowerUnit.BASE.getUnitByAbbreviation("nW"), "NANOWATT", "nW");

    /** Power.MICROWATT unit type with code 4. */
    public static final UnitType POWER_MICROWATT =
            new UnitType(QuantityType.POWER, 4, PowerUnit.MICROWATT, "MICROWATT", "μW");

    /** Power.MILLIWATT unit type with code 5. */
    public static final UnitType POWER_MILLIWATT =
            new UnitType(QuantityType.POWER, 5, PowerUnit.MILLIWATT, "MILLIWATT", "mW");

    /** Power.KILOWATT unit type with code 6. */
    public static final UnitType POWER_KILOWATT =
            new UnitType(QuantityType.POWER, 6, PowerUnit.KILOWATT, "KILOWATT", "kW");

    /** Power.MEGAWATT unit type with code 7. */
    public static final UnitType POWER_MEGAWATT =
            new UnitType(QuantityType.POWER, 7, PowerUnit.MEGAWATT, "MEGAWATT", "MW");

    /** Power.GIGAWATT unit type with code 8. */
    public static final UnitType POWER_GIGAWATT =
            new UnitType(QuantityType.POWER, 8, PowerUnit.GIGAWATT, "GIGAWATT", "GW");

    /** Power.TERAWATT unit type with code 9. */
    public static final UnitType POWER_TERAWATT =
            new UnitType(QuantityType.POWER, 9, PowerUnit.TERAWATT, "TERAWATT", "TW");

    /** Power.PETAWATT unit type with code 10. */
    public static final UnitType POWER_PETAWATT =
            new UnitType(QuantityType.POWER, 10, PowerUnit.PETAWATT, "PETAWATT", "PW");

    /** Power.ERG_PER_SECOND unit type with code 11. */
    public static final UnitType POWER_ERG_PER_SECOND =
            new UnitType(QuantityType.POWER, 11, PowerUnit.ERG_PER_SECOND, "ERG_PER_SECOND", "erg/s");

    /** Power.FOOT_POUND_FORCE_PER_SECOND unit type with code 12. */
    public static final UnitType POWER_FOOT_POUND_FORCE_PER_SECOND = new UnitType(QuantityType.POWER, 12,
            PowerUnit.FOOT_POUND_FORCE_PER_SECOND, "FOOT_POUND_FORCE_PER_SECOND", "ft.lbf/s");

    /** Power.FOOT_POUND_FORCE_PER_MINUTE unit type with code 13. */
    public static final UnitType POWER_FOOT_POUND_FORCE_PER_MINUTE = new UnitType(QuantityType.POWER, 13,
            PowerUnit.FOOT_POUND_FORCE_PER_MINUTE, "FOOT_POUND_FORCE_PER_MINUTE", "ft.lbf/min");

    /** Power.FOOT_POUND_FORCE_PER_HOUR unit type with code 14. */
    public static final UnitType POWER_FOOT_POUND_FORCE_PER_HOUR = new UnitType(QuantityType.POWER, 14,
            PowerUnit.FOOT_POUND_FORCE_PER_HOUR, "FOOT_POUND_FORCE_PER_HOUR", "ft.lbf/h");

    /** Power.HORSEPOWER_METRIC unit type with code 15. */
    public static final UnitType POWER_HORSEPOWER_METRIC =
            new UnitType(QuantityType.POWER, 15, PowerUnit.HORSEPOWER_METRIC, "HORSEPOWER_METRIC", "hp");

    /** Power.STHENE_METER_PER_SECOND unit type with code 16. */
    public static final UnitType POWER_STHENE_METER_PER_SECOND = new UnitType(QuantityType.POWER, 16,
            PowerUnit.STHENE_METER_PER_SECOND, "STHENE_METER_PER_SECOND", "sth/s");

    /* ==================================================== PRESSURE =================================================== */

    /** Pressure.PASCAL unit type with code 0. */
    public static final UnitType PRESSURE_PASCAL =
            new UnitType(QuantityType.PRESSURE, 0, PressureUnit.PASCAL, "PASCAL", "Pa");

    /** Pressure.HECTOPASCAL unit type with code 1. */
    public static final UnitType PRESSURE_HECTOPASCAL =
            new UnitType(QuantityType.PRESSURE, 1, PressureUnit.HECTOPASCAL, "HECTOPASCAL", "hPa");

    /** Pressure.KILOPASCAL unit type with code 2. */
    public static final UnitType PRESSURE_KILOPASCAL =
            new UnitType(QuantityType.PRESSURE, 2, PressureUnit.KILOPASCAL, "KILOPASCAL", "kPa");

    /** Pressure.ATMOSPHERE_STANDARD unit type with code 3. */
    public static final UnitType PRESSURE_ATMOSPHERE_STANDARD =
            new UnitType(QuantityType.PRESSURE, 3, PressureUnit.ATMOSPHERE_STANDARD, "ATMOSPHERE_STANDARD", "atm");

    /** Pressure.ATMOSPHERE_TECHNICAL unit type with code 4. */
    public static final UnitType PRESSURE_ATMOSPHERE_TECHNICAL =
            new UnitType(QuantityType.PRESSURE, 4, PressureUnit.ATMOSPHERE_TECHNICAL, "ATMOSPHERE_TECHNICAL", "at");

    /** Pressure.MILLIBAR unit type with code 5. */
    public static final UnitType PRESSURE_MILLIBAR =
            new UnitType(QuantityType.PRESSURE, 5, PressureUnit.MILLIBAR, "MILLIBAR", "mbar");

    /** Pressure.BAR unit type with code 6. */
    public static final UnitType PRESSURE_BAR =
            new UnitType(QuantityType.PRESSURE, 6, PressureUnit.BAR, "BAR", "bar");

    /** Pressure.BARYE unit type with code 7. */
    public static final UnitType PRESSURE_BARYE =
            new UnitType(QuantityType.PRESSURE, 7, PressureUnit.BARYE, "BARYE", "Ba");

    /** Pressure.MILLIMETER_MERCURY unit type with code 8. */
    public static final UnitType PRESSURE_MILLIMETER_MERCURY =
            new UnitType(QuantityType.PRESSURE, 8, PressureUnit.MILLIMETER_MERCURY, "MILLIMETER_MERCURY", "mmHg");

    /** Pressure.CENTIMETER_MERCURY unit type with code 9. */
    public static final UnitType PRESSURE_CENTIMETER_MERCURY =
            new UnitType(QuantityType.PRESSURE, 9, PressureUnit.CENTIMETER_MERCURY, "CENTIMETER_MERCURY", "cmHg");

    /** Pressure.INCH_MERCURY unit type with code 10. */
    public static final UnitType PRESSURE_INCH_MERCURY =
            new UnitType(QuantityType.PRESSURE, 10, PressureUnit.INCH_MERCURY, "INCH_MERCURY", "inHg");

    /** Pressure.FOOT_MERCURY unit type with code 11. */
    public static final UnitType PRESSURE_FOOT_MERCURY =
            new UnitType(QuantityType.PRESSURE, 11, PressureUnit.FOOT_MERCURY, "FOOT_MERCURY", "ftHg");

    /** Pressure.KGF_PER_SQUARE_MM unit type with code 12. */
    public static final UnitType PRESSURE_KGF_PER_SQUARE_MM =
            new UnitType(QuantityType.PRESSURE, 12, PressureUnit.KGF_PER_SQUARE_MM, "KGF_PER_SQUARE_MM", "kgf/mm2");

    /** Pressure.PIEZE unit type with code 13. */
    public static final UnitType PRESSURE_PIEZE =
            new UnitType(QuantityType.PRESSURE, 13, PressureUnit.PIEZE, "PIEZE", "pz");

    /** Pressure.POUND_PER_SQUARE_INCH unit type with code 14. */
    public static final UnitType PRESSURE_POUND_PER_SQUARE_INCH = new UnitType(QuantityType.PRESSURE, 14,
            PressureUnit.POUND_PER_SQUARE_INCH, "POUND_PER_SQUARE_INCH", "lb/in2");

    /** Pressure.POUND_PER_SQUARE_FOOT unit type with code 15. */
    public static final UnitType PRESSURE_POUND_PER_SQUARE_FOOT = new UnitType(QuantityType.PRESSURE, 15,
            PressureUnit.POUND_PER_SQUARE_FOOT, "POUND_PER_SQUARE_FOOT", "lb/ft2");

    /** Pressure.TORR unit type with code 16. */
    public static final UnitType PRESSURE_TORR =
            new UnitType(QuantityType.PRESSURE, 16, PressureUnit.TORR, "TORR", "torr");

    /* ==================================================== SPEED ====================================================== */

    /** Speed.METER_PER_SECOND unit type with code 0. */
    public static final UnitType SPEED_METER_PER_SECOND =
            new UnitType(QuantityType.SPEED, 0, SpeedUnit.METER_PER_SECOND, "METER_PER_SECOND", "m/s");

    /** Speed.METER_PER_HOUR unit type with code 1. */
    public static final UnitType SPEED_METER_PER_HOUR =
            new UnitType(QuantityType.SPEED, 1, SpeedUnit.METER_PER_HOUR, "METER_PER_HOUR", "m/h");

    /** Speed.KM_PER_SECOND unit type with code 2. */
    public static final UnitType SPEED_KM_PER_SECOND =
            new UnitType(QuantityType.SPEED, 2, SpeedUnit.KM_PER_SECOND, "KM_PER_SECOND", "km/s");

    /** Speed.KM_PER_HOUR unit type with code 3. */
    public static final UnitType SPEED_KM_PER_HOUR =
            new UnitType(QuantityType.SPEED, 3, SpeedUnit.KM_PER_HOUR, "KM_PER_HOUR", "km/h");

    /** Speed.INCH_PER_SECOND unit type with code 4. */
    public static final UnitType SPEED_INCH_PER_SECOND =
            new UnitType(QuantityType.SPEED, 4, SpeedUnit.INCH_PER_SECOND, "INCH_PER_SECOND", "in/s");

    /** Speed.INCH_PER_MINUTE unit type with code 5. */
    public static final UnitType SPEED_INCH_PER_MINUTE =
            new UnitType(QuantityType.SPEED, 5, SpeedUnit.INCH_PER_MINUTE, "INCH_PER_MINUTE", "in/min");

    /** Speed.INCH_PER_HOUR unit type with code 6. */
    public static final UnitType SPEED_INCH_PER_HOUR =
            new UnitType(QuantityType.SPEED, 6, SpeedUnit.INCH_PER_HOUR, "INCH_PER_HOUR", "in/h");

    /** Speed.FOOT_PER_SECOND unit type with code 7. */
    public static final UnitType SPEED_FOOT_PER_SECOND =
            new UnitType(QuantityType.SPEED, 7, SpeedUnit.FOOT_PER_SECOND, "FOOT_PER_SECOND", "ft/s");

    /** Speed.FOOT_PER_MINUTE unit type with code 8. */
    public static final UnitType SPEED_FOOT_PER_MINUTE =
            new UnitType(QuantityType.SPEED, 8, SpeedUnit.FOOT_PER_MINUTE, "FOOT_PER_MINUTE", "ft/min");

    /** Speed.FOOT_PER_HOUR unit type with code 9. */
    public static final UnitType SPEED_FOOT_PER_HOUR =
            new UnitType(QuantityType.SPEED, 9, SpeedUnit.FOOT_PER_HOUR, "FOOT_PER_HOUR", "ft/h");

    /** Speed.MILE_PER_SECOND unit type with code 10. */
    public static final UnitType SPEED_MILE_PER_SECOND =
            new UnitType(QuantityType.SPEED, 10, SpeedUnit.MILE_PER_SECOND, "MILE_PER_SECOND", "mi/s");

    /** Speed.MILE_PER_MINUTE unit type with code 11. */
    public static final UnitType SPEED_MILE_PER_MINUTE =
            new UnitType(QuantityType.SPEED, 11, SpeedUnit.MILE_PER_MINUTE, "MILE_PER_MINUTE", "mi/min");

    /** Speed.MILE_PER_HOUR unit type with code 12. */
    public static final UnitType SPEED_MILE_PER_HOUR =
            new UnitType(QuantityType.SPEED, 12, SpeedUnit.MILE_PER_HOUR, "MILE_PER_HOUR", "mi/h");

    /** Speed.KNOT unit type with code 13. */
    public static final UnitType SPEED_KNOT = new UnitType(QuantityType.SPEED, 13, SpeedUnit.KNOT, "KNOT", "kt");

    /* ================================================== TEMPERATURE ================================================== */

    /** Temperature.KELVIN unit type with code 0. */
    public static final UnitType TEMPERATURE_KELVIN =
            new UnitType(QuantityType.TEMPERATURE, 0, TemperatureUnit.KELVIN, "KELVIN", "K");

    /** Temperature.DEGREE_CELSIUS unit type with code 1. */
    public static final UnitType TEMPERATURE_DEGREE_CELSIUS =
            new UnitType(QuantityType.TEMPERATURE, 1, TemperatureUnit.DEGREE_CELSIUS, "DEGREE_CELSIUS", "OC");

    /** Temperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final UnitType TEMPERATURE_DEGREE_FAHRENHEIT =
            new UnitType(QuantityType.TEMPERATURE, 2, TemperatureUnit.DEGREE_FAHRENHEIT, "DEGREE_FAHRENHEIT", "OF");

    /** Temperature.DEGREE_RANKINE unit type with code 3. */
    public static final UnitType TEMPERATURE_DEGREE_RANKINE =
            new UnitType(QuantityType.TEMPERATURE, 3, TemperatureUnit.DEGREE_RANKINE, "DEGREE_RANKINE", "OR");

    /** Temperature.DEGREE_REAUMUR unit type with code 4. */
    public static final UnitType TEMPERATURE_DEGREE_REAUMUR =
            new UnitType(QuantityType.TEMPERATURE, 4, TemperatureUnit.DEGREE_REAUMUR, "DEGREE_REAUMUR", "ORé");

    /* ============================================= ABSOLUTETEMPERATURE =============================================== */

    /** AbsoluteTemperature.KELVIN unit type with code 0. */
    public static final UnitType ABSOLUTETEMPERATURE_KELVIN =
            new UnitType(QuantityType.ABSOLUTETEMPERATURE, 0, AbsoluteTemperatureUnit.KELVIN, "KELVIN", "K");

    /** AbsoluteTemperature.DEGREE_CELSIUS unit type with code 1. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_CELSIUS = new UnitType(QuantityType.ABSOLUTETEMPERATURE,
            1, AbsoluteTemperatureUnit.DEGREE_CELSIUS, "DEGREE_CELSIUS", "OC");

    /** AbsoluteTemperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_FAHRENHEIT = new UnitType(
            QuantityType.ABSOLUTETEMPERATURE, 2, AbsoluteTemperatureUnit.DEGREE_FAHRENHEIT, "DEGREE_FAHRENHEIT", "OF");

    /** AbsoluteTemperature.DEGREE_RANKINE unit type with code 3. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_RANKINE = new UnitType(QuantityType.ABSOLUTETEMPERATURE,
            3, AbsoluteTemperatureUnit.DEGREE_RANKINE, "DEGREE_RANKINE", "OR");

    /** AbsoluteTemperature.DEGREE_REAUMUR unit type with code 4. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_REAUMUR = new UnitType(QuantityType.ABSOLUTETEMPERATURE,
            4, AbsoluteTemperatureUnit.DEGREE_REAUMUR, "DEGREE_REAUMUR", "ORé");

    /* =================================================== DURATION ==================================================== */

    /** Duration.SECOND unit type with code 0. */
    public static final UnitType DURATION_SECOND =
            new UnitType(QuantityType.DURATION, 0, DurationUnit.SECOND, "SECOND", "s");

    /** Duration.ATTOSECOND unit type with code 1. */
    public static final UnitType DURATION_ATTOSECOND =
            new UnitType(QuantityType.DURATION, 1, DurationUnit.BASE.getUnitByAbbreviation("as"), "ATTOSECOND", "as");

    /** Duration.FEMTOSECOND unit type with code 2. */
    public static final UnitType DURATION_FEMTOSECOND =
            new UnitType(QuantityType.DURATION, 2, DurationUnit.BASE.getUnitByAbbreviation("fs"), "FEMTOSECOND", "fs");

    /** Duration.PICOSECOND unit type with code 3. */
    public static final UnitType DURATION_PICOSECOND =
            new UnitType(QuantityType.DURATION, 3, DurationUnit.BASE.getUnitByAbbreviation("ps"), "PICOSECOND", "ps");

    /** Duration.NANOSECOND unit type with code 4. */
    public static final UnitType DURATION_NANOSECOND =
            new UnitType(QuantityType.DURATION, 4, DurationUnit.BASE.getUnitByAbbreviation("ns"), "NANOSECOND", "ns");

    /** Duration.MICROSECOND unit type with code 5. */
    public static final UnitType DURATION_MICROSECOND =
            new UnitType(QuantityType.DURATION, 5, DurationUnit.MICROSECOND, "MICROSECOND", "μs");

    /** Duration.MILLISECOND unit type with code 6. */
    public static final UnitType DURATION_MILLISECOND =
            new UnitType(QuantityType.DURATION, 6, DurationUnit.MILLISECOND, "MILLISECOND", "ms");

    /** Duration.MINUTE unit type with code 7. */
    public static final UnitType DURATION_MINUTE =
            new UnitType(QuantityType.DURATION, 7, DurationUnit.MINUTE, "MINUTE", "min");

    /** Duration.HOUR unit type with code 8. */
    public static final UnitType DURATION_HOUR =
            new UnitType(QuantityType.DURATION, 8, DurationUnit.HOUR, "HOUR", "hr");

    /** Duration.DAY unit type with code 9. */
    public static final UnitType DURATION_DAY =
            new UnitType(QuantityType.DURATION, 9, DurationUnit.DAY, "DAY", "day");

    /** Duration.WEEK unit type with code 10. */
    public static final UnitType DURATION_WEEK =
            new UnitType(QuantityType.DURATION, 10, DurationUnit.WEEK, "WEEK", "wk");

    /* ===================================================== TIME ====================================================== */

    /** Time.BASE_SECOND unit type with code 0. */
    public static final UnitType TIME_BASE_SECOND =
            new UnitType(QuantityType.TIME, 0, TimeUnit.BASE_SECOND, "SECOND", "s");

    /** Time.BASE_MICROSECOND unit type with code 1. */
    public static final UnitType TIME_BASE_MICROSECOND =
            new UnitType(QuantityType.TIME, 1, TimeUnit.BASE_MICROSECOND, "MICROSECOND", "μs");

    /** Time.BASE_MILLISECOND unit type with code 2. */
    public static final UnitType TIME_BASE_MILLISECOND =
            new UnitType(QuantityType.TIME, 2, TimeUnit.BASE_MILLISECOND, "MILLISECOND", "ms");

    /** Time.BASE_MINUTE unit type with code 3. */
    public static final UnitType TIME_BASE_MINUTE =
            new UnitType(QuantityType.TIME, 3, TimeUnit.BASE_MINUTE, "MINUTE", "min");

    /** Time.BASE_HOUR unit type with code 4. */
    public static final UnitType TIME_BASE_HOUR =
            new UnitType(QuantityType.TIME, 4, TimeUnit.BASE_HOUR, "HOUR", "hr");

    /** Time.BASE_DAY unit type with code 5. */
    public static final UnitType TIME_BASE_DAY =
            new UnitType(QuantityType.TIME, 5, TimeUnit.BASE_DAY, "DAY", "day");

    /** Time.BASE_WEEK unit type with code 6. */
    public static final UnitType TIME_BASE_WEEK =
            new UnitType(QuantityType.TIME, 6, TimeUnit.BASE_WEEK, "WEEK", "wk");

    /** Time.EPOCH_SECOND unit type with code 7. */
    public static final UnitType TIME_EPOCH_SECOND =
            new UnitType(QuantityType.TIME, 7, TimeUnit.EPOCH_SECOND, "SECOND (1-1-70)", "s(POSIX)");

    /** Time.EPOCH_MICROSECOND unit type with code 8. */
    public static final UnitType TIME_EPOCH_MICROSECOND =
            new UnitType(QuantityType.TIME, 8, TimeUnit.EPOCH_MICROSECOND, "MICROSECOND (1-1-70)", "μs(POSIX)");

    /** Time.EPOCH_MILLISECOND unit type with code 9. */
    public static final UnitType TIME_EPOCH_MILLISECOND =
            new UnitType(QuantityType.TIME, 9, TimeUnit.EPOCH_MILLISECOND, "MILLISECOND (1-1-70)", "ms(POSIX)");

    /** Time.EPOCH_MINUTE unit type with code 10. */
    public static final UnitType TIME_EPOCH_MINUTE =
            new UnitType(QuantityType.TIME, 10, TimeUnit.EPOCH_MINUTE, "MINUTE (1-1-70)", "min(POSIX)");

    /** Time.EPOCH_HOUR unit type with code 11. */
    public static final UnitType TIME_EPOCH_HOUR =
            new UnitType(QuantityType.TIME, 11, TimeUnit.EPOCH_HOUR, "HOUR (1-1-70)", "hr(POSIX)");

    /** Time.EPOCH_DAY unit type with code 12. */
    public static final UnitType TIME_EPOCH_DAY =
            new UnitType(QuantityType.TIME, 12, TimeUnit.EPOCH_DAY, "DAY (1-1-70)", "day(POSIX)");

    /** Time.EPOCH_WEEK unit type with code 13. */
    public static final UnitType TIME_EPOCH_WEEK =
            new UnitType(QuantityType.TIME, 13, TimeUnit.EPOCH_WEEK, "WEEK (1-1-70)", "wk(POSIX)");

    /** Time.TIME_YEAR1_SECOND unit type with code 14. */
    public static final UnitType TIME_YEAR1_SECOND =
            new UnitType(QuantityType.TIME, 14, TimeUnit.EPOCH_YEAR1_SECOND, "SECOND (1-1-0001)", "s(1-1-0001)");

    /** Time.TIME_J2000_SECOND unit type with code 15. */
    public static final UnitType TIME_J2000_SECOND =
            new UnitType(QuantityType.TIME, 15, TimeUnit.EPOCH_J2000_SECOND, "SECOND (1-1-2000 12:00)", "s(1-1-2000)");

    /* ==================================================== TORQUE ===================================================== */

    /** Torque.NEWTON_METER unit type with code 0. */
    public static final UnitType TORQUE_NEWTON_METER =
            new UnitType(QuantityType.TORQUE, 0, TorqueUnit.NEWTON_METER, "NEWTON_METER", "Nm");

    /** Torque.POUND_FOOT unit type with code 1. */
    public static final UnitType TORQUE_POUND_FOOT =
            new UnitType(QuantityType.TORQUE, 1, TorqueUnit.POUND_FOOT, "POUND_FOOT", "lb.ft");

    /** Torque.POUND_INCH unit type with code 2. */
    public static final UnitType TORQUE_POUND_INCH =
            new UnitType(QuantityType.TORQUE, 2, TorqueUnit.POUND_INCH, "POUND_INCH", "lb.in");

    /** Torque.METER_KILOGRAM_FORCE unit type with code 3. */
    public static final UnitType TORQUE_METER_KILOGRAM_FORCE =
            new UnitType(QuantityType.TORQUE, 3, TorqueUnit.METER_KILOGRAM_FORCE, "METER_KILOGRAM_FORCE", "m.kgf");

    /* ==================================================== VOLUME ===================================================== */

    /** Volume.CUBIC_METER unit type with code 0. */
    public static final UnitType VOLUME_CUBIC_METER =
            new UnitType(QuantityType.VOLUME, 0, VolumeUnit.CUBIC_METER, "CUBIC_METER", "m3");

    /** Volume.CUBIC_ATTOMETER unit type with code 1. */
    public static final UnitType VOLUME_CUBIC_ATTOMETER = new UnitType(QuantityType.VOLUME, 1,
            VolumeUnit.BASE.getUnitByAbbreviation("am^3"), "CUBIC_ATTOMETER", "am3");

    /** Volume.CUBIC_FEMTOMETER unit type with code 2. */
    public static final UnitType VOLUME_CUBIC_FEMTOMETER = new UnitType(QuantityType.VOLUME, 2,
            VolumeUnit.BASE.getUnitByAbbreviation("fm^3"), "CUBIC_FEMTOMETER", "fm3");

    /** Volume.CUBIC_PICOMETER unit type with code 3. */
    public static final UnitType VOLUME_CUBIC_PICOMETER = new UnitType(QuantityType.VOLUME, 3,
            VolumeUnit.BASE.getUnitByAbbreviation("pm^3"), "CUBIC_PICOMETER", "pm3");

    /** Volume.CUBIC_NANOMETER unit type with code 4. */
    public static final UnitType VOLUME_CUBIC_NANOMETER = new UnitType(QuantityType.VOLUME, 4,
            VolumeUnit.BASE.getUnitByAbbreviation("nm^3"), "CUBIC_NANOMETER", "nm3");

    /** Volume.CUBIC_MICROMETER unit type with code 5. */
    public static final UnitType VOLUME_CUBIC_MICROMETER = new UnitType(QuantityType.VOLUME, 5,
            VolumeUnit.BASE.getUnitByAbbreviation("μm^3"), "CUBIC_MICROMETER", "μm3");

    /** Volume.CUBIC_MILLIMETER unit type with code 6. */
    public static final UnitType VOLUME_CUBIC_MILLIMETER =
            new UnitType(QuantityType.VOLUME, 6, VolumeUnit.CUBIC_MILLIMETER, "CUBIC_MILLIMETER", "mm3");

    /** Volume.CUBIC_CENTIMETER unit type with code 7. */
    public static final UnitType VOLUME_CUBIC_CENTIMETER =
            new UnitType(QuantityType.VOLUME, 7, VolumeUnit.CUBIC_CENTIMETER, "CUBIC_CENTIMETER", "cm3");

    /** Volume.CUBIC_DECIMETER unit type with code 8. */
    public static final UnitType VOLUME_CUBIC_DECIMETER =
            new UnitType(QuantityType.VOLUME, 8, VolumeUnit.CUBIC_DECIMETER, "CUBIC_DECIMETER", "dm3");

    /** Volume.CUBIC_DEKAMETER unit type with code 9. */
    public static final UnitType VOLUME_CUBIC_DEKAMETER = new UnitType(QuantityType.VOLUME, 9,
            VolumeUnit.BASE.getUnitByAbbreviation("dam^3"), "CUBIC_DEKAMETER", "dam3");

    /** Volume.CUBIC_HECTOMETER unit type with code 10. */
    public static final UnitType VOLUME_CUBIC_HECTOMETER =
            new UnitType(QuantityType.VOLUME, 10, VolumeUnit.CUBIC_HECTOMETER, "CUBIC_HECTOMETER", "hm3");

    /** Volume.CUBIC_KILOMETER unit type with code 11. */
    public static final UnitType VOLUME_CUBIC_KILOMETER =
            new UnitType(QuantityType.VOLUME, 11, VolumeUnit.CUBIC_KILOMETER, "CUBIC_KILOMETER", "km3");

    /** Volume.CUBIC_MEGAMETER unit type with code 12. */
    public static final UnitType VOLUME_CUBIC_MEGAMETER = new UnitType(QuantityType.VOLUME, 12,
            VolumeUnit.BASE.getUnitByAbbreviation("Mm^3"), "CUBIC_MEGAMETER", "Mm3");

    /** Volume.CUBIC_INCH unit type with code 13. */
    public static final UnitType VOLUME_CUBIC_INCH =
            new UnitType(QuantityType.VOLUME, 13, VolumeUnit.CUBIC_INCH, "CUBIC_INCH", "in3");

    /** Volume.CUBIC_FOOT unit type with code 14. */
    public static final UnitType VOLUME_CUBIC_FOOT =
            new UnitType(QuantityType.VOLUME, 14, VolumeUnit.CUBIC_FOOT, "CUBIC_FOOT", "ft3");

    /** Volume.CUBIC_YARD unit type with code 15. */
    public static final UnitType VOLUME_CUBIC_YARD =
            new UnitType(QuantityType.VOLUME, 15, VolumeUnit.CUBIC_YARD, "CUBIC_YARD", "yd3");

    /** Volume.CUBIC_MILE unit type with code 16. */
    public static final UnitType VOLUME_CUBIC_MILE =
            new UnitType(QuantityType.VOLUME, 16, VolumeUnit.CUBIC_MILE, "CUBIC_MILE", "mi3");

    /** Volume.LITER unit type with code 17. */
    public static final UnitType VOLUME_LITER =
            new UnitType(QuantityType.VOLUME, 17, VolumeUnit.LITER, "LITER", "l");

    /** Volume.GALLON_IMP unit type with code 18. */
    public static final UnitType VOLUME_GALLON_IMP =
            new UnitType(QuantityType.VOLUME, 18, VolumeUnit.GALLON_IMP, "GALLON_IMP", "gal (imp)");

    /** Volume.GALLON_US_FLUID unit type with code 19. */
    public static final UnitType VOLUME_GALLON_US_FLUID =
            new UnitType(QuantityType.VOLUME, 19, VolumeUnit.GALLON_US, "GALLON_US_FLUID", "gal (US)");

    /** Volume.OUNCE_IMP_FLUID unit type with code 20. */
    public static final UnitType VOLUME_OUNCE_IMP_FLUID =
            new UnitType(QuantityType.VOLUME, 20, VolumeUnit.FLUID_OUNCE_IMP, "OUNCE_IMP_FLUID", "oz (imp)");

    /** Volume.OUNCE_US_FLUID unit type with code 21. */
    public static final UnitType VOLUME_OUNCE_US_FLUID =
            new UnitType(QuantityType.VOLUME, 21, VolumeUnit.FLUID_OUNCE_US, "OUNCE_US_FLUID", "oz (US)");

    /** Volume.PINT_IMP unit type with code 22. */
    public static final UnitType VOLUME_PINT_IMP =
            new UnitType(QuantityType.VOLUME, 22, VolumeUnit.PINT_IMP, "PINT_IMP", "pt (imp)");

    /** Volume.PINT_US_FLUID unit type with code 23. */
    public static final UnitType VOLUME_PINT_US_FLUID =
            new UnitType(QuantityType.VOLUME, 23, VolumeUnit.PINT_US, "PINT_US_FLUID", "pt (US)");

    /** Volume.QUART_IMP unit type with code 24. */
    public static final UnitType VOLUME_QUART_IMP =
            new UnitType(QuantityType.VOLUME, 24, VolumeUnit.QUART_IMP, "QUART_IMP", "qt (imp)");

    /** Volume.QUART_US_FLUID unit type with code 25. */
    public static final UnitType VOLUME_QUART_US_FLUID =
            new UnitType(QuantityType.VOLUME, 25, VolumeUnit.QUART_US, "QUART_US_FLUID", "qt (US)");

    /** Volume.CUBIC_PARSEC unit type with code 26. */
    public static final UnitType VOLUME_CUBIC_PARSEC =
            new UnitType(QuantityType.VOLUME, 26, VolumeUnit.CUBIC_PARSEC, "CUBIC_PARSEC", "pc3");

    /** Volume.CUBIC_LIGHTYEAR unit type with code 27. */
    public static final UnitType VOLUME_CUBIC_LIGHTYEAR =
            new UnitType(QuantityType.VOLUME, 27, VolumeUnit.CUBIC_LIGHTYEAR, "CUBIC_LIGHTYEAR", "ly3");

    /* ================================================= ABSORBEDDOSE ================================================== */

    /** AbsorbedDose.GRAY unit type with code 0. */
    public static final UnitType ABSORBEDDOSE_GRAY =
            new UnitType(QuantityType.ABSORBEDDOSE, 0, AbsorbedDoseUnit.GRAY, "GRAY", "Gy");

    /** AbsorbedDose.MILLIGRAY unit type with code 1. */
    public static final UnitType ABSORBEDDOSE_MILLIGRAY =
            new UnitType(QuantityType.ABSORBEDDOSE, 1, AbsorbedDoseUnit.MILLIGRAY, "MILLIGRAY", "mGy");

    /** AbsorbedDose.MICROGRAY unit type with code 2. */
    public static final UnitType ABSORBEDDOSE_MICROGRAY =
            new UnitType(QuantityType.ABSORBEDDOSE, 2, AbsorbedDoseUnit.MICROGRAY, "MICROGRAY", "μGy");

    /** AbsorbedDose.ERG_PER_GRAM unit type with code 3. */
    public static final UnitType ABSORBEDDOSE_ERG_PER_GRAM =
            new UnitType(QuantityType.ABSORBEDDOSE, 3, AbsorbedDoseUnit.ERG_PER_GRAM, "ERG_PER_GRAM", "erg/g");

    /** AbsorbedDose.RAD unit type with code 4. */
    public static final UnitType ABSORBEDDOSE_RAD =
            new UnitType(QuantityType.ABSORBEDDOSE, 4, AbsorbedDoseUnit.RAD, "RAD", "rad");

    /* =============================================== AMOUNTOFSUBSTANCE =============================================== */

    /** AmountOfSubstance.MOLE unit type with code 0. */
    public static final UnitType AMOUNTOFSUBSTANCE_MOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 0, AmountOfSubstanceUnit.MOLE, "MOLE", "mol");

    /** AmountOfSubstance.MILLIMOLE unit type with code 1. */
    public static final UnitType AMOUNTOFSUBSTANCE_MILLIMOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 1, AmountOfSubstanceUnit.MILLIMOLE, "MILLIMOLE", "mmol");

    /** AmountOfSubstance.MICROMOLE unit type with code 2. */
    public static final UnitType AMOUNTOFSUBSTANCE_MICROMOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 2, AmountOfSubstanceUnit.MICROMOLE, "MICROMOLE", "μmol");

    /** AmountOfSubstance.NANOMOLE unit type with code 3. */
    public static final UnitType AMOUNTOFSUBSTANCE_NANOMOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 3, AmountOfSubstanceUnit.NANOMOLE, "NANOMOLE", "nmol");

    /* ================================================ CATALYTICACTIVITY ============================================== */

    /** CatalyticActivity.KATAL unit type with code 0. */
    public static final UnitType CATALYTICACTIVITY_KATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 0, CatalyticActivityUnit.KATAL, "KATAL", "kat");

    /** CatalyticActivity.MILLIKATAL unit type with code 1. */
    public static final UnitType CATALYTICACTIVITY_MILLIKATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 1, CatalyticActivityUnit.MILLIKATAL, "MILLIKATAL", "mkat");

    /** CatalyticActivity.MICROKATAL unit type with code 2. */
    public static final UnitType CATALYTICACTIVITY_MICROKATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 2, CatalyticActivityUnit.MICROKATAL, "MICROKATAL", "μkat");

    /** CatalyticActivity.NANOKATAL unit type with code 3. */
    public static final UnitType CATALYTICACTIVITY_NANOKATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 3, CatalyticActivityUnit.NANOKATAL, "NANOKATAL", "nkat");

    /* ============================================= ELECTRICALCAPACITANCE ============================================= */

    /** ElectricalCapacitance.FARAD unit type with code 0. */
    public static final UnitType ELECTRICALCAPACITANCE_FARAD =
            new UnitType(QuantityType.ELECTRICALCAPACITANCE, 0, ElectricalCapacitanceUnit.FARAD, "FARAD", "F");

    /** ElectricalCapacitance.MILLIFARAD unit type with code 1. */
    public static final UnitType ELECTRICALCAPACITANCE_MILLIFARAD = new UnitType(QuantityType.ELECTRICALCAPACITANCE,
            1, ElectricalCapacitanceUnit.MILLIFARAD, "MILLIFARAD", "mF");

    /** ElectricalCapacitance.MICROFARAD unit type with code 2. */
    public static final UnitType ELECTRICALCAPACITANCE_MICROFARAD = new UnitType(QuantityType.ELECTRICALCAPACITANCE,
            2, ElectricalCapacitanceUnit.MICROFARAD, "MICROFARAD", "uF");

    /** ElectricalCapacitance.NANOFARAD unit type with code 3. */
    public static final UnitType ELECTRICALCAPACITANCE_NANOFARAD = new UnitType(QuantityType.ELECTRICALCAPACITANCE,
            3, ElectricalCapacitanceUnit.NANOFARAD, "NANOFARAD", "nF");

    /** ElectricalCapacitance.PICOFARAD unit type with code 4. */
    public static final UnitType ELECTRICALCAPACITANCE_PICOFARAD = new UnitType(QuantityType.ELECTRICALCAPACITANCE,
            4, ElectricalCapacitanceUnit.PICOFARAD, "PICOFARAD", "pF");

    /* ============================================= ELECTRICALCONDUCTANCE ============================================= */

    /** ElectricalConductance.SIEMENS unit type with code 0. */
    public static final UnitType ELECTRICALCONDUCTANCE_SIEMENS =
            new UnitType(QuantityType.ELECTRICALCONDUCTANCE, 0, ElectricalConductanceUnit.SIEMENS, "SIEMENS", "F");

    /** ElectricalConductance.MILLISIEMENS unit type with code 1. */
    public static final UnitType ELECTRICALCONDUCTANCE_MILLISIEMENS = new UnitType(
            QuantityType.ELECTRICALCONDUCTANCE, 1, ElectricalConductanceUnit.MILLISIEMENS, "MILLISIEMENS", "mS");

    /** ElectricalConductance.MICROSIEMENS unit type with code 2. */
    public static final UnitType ELECTRICALCONDUCTANCE_MICROSIEMENS = new UnitType(
            QuantityType.ELECTRICALCONDUCTANCE, 2, ElectricalConductanceUnit.MICROSIEMENS, "MICROSIEMENS", "μS");

    /** ElectricalConductance.NANOSIEMENS unit type with code 3. */
    public static final UnitType ELECTRICALCONDUCTANCE_NANOSIEMENS = new UnitType(
            QuantityType.ELECTRICALCONDUCTANCE, 3, ElectricalConductanceUnit.NANOSIEMENS, "NANOSIEMENS", "nS");

    /* ============================================= ELECTRICALINDUCTANCE ============================================= */

    /** ElectricalInductance.HENRY unit type with code 0. */
    public static final UnitType ELECTRICALINDUCTANCE_HENRY =
            new UnitType(QuantityType.ELECTRICALINDUCTANCE, 0, ElectricalInductanceUnit.HENRY, "HENRY", "H");

    /** ElectricalInductance.MILLIHENRY unit type with code 1. */
    public static final UnitType ELECTRICALINDUCTANCE_MILLIHENRY = new UnitType(QuantityType.ELECTRICALINDUCTANCE,
            1, ElectricalInductanceUnit.MILLIHENRY, "MILLIHENRY", "mH");

    /** ElectricalInductance.MICROHENRY unit type with code 2. */
    public static final UnitType ELECTRICALINDUCTANCE_MICROHENRY = new UnitType(QuantityType.ELECTRICALINDUCTANCE,
            2, ElectricalInductanceUnit.MICROHENRY, "MICROHENRY", "μH");

    /** ElectricalInductance.NANOHENRY unit type with code 3. */
    public static final UnitType ELECTRICALINDUCTANCE_NANOHENRY =
            new UnitType(QuantityType.ELECTRICALINDUCTANCE, 3, ElectricalInductanceUnit.NANOHENRY, "NANOHENRY", "nH");

    /* ================================================= EQUIVALENTDOSE ================================================ */

    /** EquivalentDose.SIEVERT unit type with code 0. */
    public static final UnitType EQUIVALENTDOSE_SIEVERT =
            new UnitType(QuantityType.EQUIVALENTDOSE, 0, EquivalentDoseUnit.SIEVERT, "SIEVERT", "Sv");

    /** EquivalentDose.MILLISIEVERT unit type with code 1. */
    public static final UnitType EQUIVALENTDOSE_MILLISIEVERT =
            new UnitType(QuantityType.EQUIVALENTDOSE, 1, EquivalentDoseUnit.MILLISIEVERT, "MILLISIEVERT", "mSv");

    /** EquivalentDose.MICROSIEVERT unit type with code 2. */
    public static final UnitType EQUIVALENTDOSE_MICROSIEVERT =
            new UnitType(QuantityType.EQUIVALENTDOSE, 2, EquivalentDoseUnit.MICROSIEVERT, "MICROSIEVERT", "μSv");

    /** EquivalentDose.REM unit type with code 3. */
    public static final UnitType EQUIVALENTDOSE_REM =
            new UnitType(QuantityType.EQUIVALENTDOSE, 3, EquivalentDoseUnit.REM, "REM", "rem");

    /* ================================================== ILLUMINANCE ================================================== */

    /** Illuminance.LUX unit type with code 0. */
    public static final UnitType ILLUMINANCE_LUX =
            new UnitType(QuantityType.ILLUMINANCE, 0, IlluminanceUnit.LUX, "LUX", "lx");

    /** Illuminance.MILLILUX unit type with code 1. */
    public static final UnitType ILLUMINANCE_MILLILUX =
            new UnitType(QuantityType.ILLUMINANCE, 1, IlluminanceUnit.MILLILUX, "MILLILUX", "mlx");

    /** Illuminance.MICROLUX unit type with code 2. */
    public static final UnitType ILLUMINANCE_MICROLUX =
            new UnitType(QuantityType.ILLUMINANCE, 2, IlluminanceUnit.MICROLUX, "MICROLUX", "μlx");

    /** Illuminance.KILOLUX unit type with code 3. */
    public static final UnitType ILLUMINANCE_KILOLUX =
            new UnitType(QuantityType.ILLUMINANCE, 3, IlluminanceUnit.KILOLUX, "KILOLUX", "klux");

    /** Illuminance.PHOT unit type with code 4. */
    public static final UnitType ILLUMINANCE_PHOT =
            new UnitType(QuantityType.ILLUMINANCE, 4, IlluminanceUnit.PHOT, "PHOT", "ph");

    /** Illuminance.NOX unit type with code 5. */
    public static final UnitType ILLUMINANCE_NOX =
            new UnitType(QuantityType.ILLUMINANCE, 5, IlluminanceUnit.NOX, "NOX", "nx");

    /* ================================================= LUMINOUSFLUX ================================================== */

    /** LuminousFlux.LUMEN unit type with code 0. */
    public static final UnitType LUMINOUSFLUX_LUMEN =
            new UnitType(QuantityType.LUMINOUSFLUX, 0, LuminousFluxUnit.LUMEN, "LUMEN", "lm");

    /* ============================================== LUMINOUSINTENSITY ================================================ */

    /** LuminousIntensity.CANDELA unit type with code 0. */
    public static final UnitType LUMINOUSINTENSITY_CANDELA =
            new UnitType(QuantityType.LUMINOUSINTENSITY, 0, LuminousIntensityUnit.CANDELA, "CANDELA", "cd");

    /* ============================================= MAGNETICFLUXDENSITY =============================================== */

    /** MagneticFluxDensity.TESLA unit type with code 0. */
    public static final UnitType MAGNETICFLUXDENSITY_TESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 0, MagneticFluxDensityUnit.TESLA, "TESLA", "T");

    /** MagneticFluxDensity.MILLITESLA unit type with code 1. */
    public static final UnitType MAGNETICFLUXDENSITY_MILLITESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 1, MagneticFluxDensityUnit.MILLITESLA, "MILLITESLA", "mT");

    /** MagneticFluxDensity.MICROTESLA unit type with code 2. */
    public static final UnitType MAGNETICFLUXDENSITY_MICROTESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 2, MagneticFluxDensityUnit.MICROTESLA, "MICROTESLA", "μT");

    /** MagneticFluxDensity.NANOTESLA unit type with code 3. */
    public static final UnitType MAGNETICFLUXDENSITY_NANOTESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 3, MagneticFluxDensityUnit.NANOTESLA, "NANOTESLA", "nT");

    /** MagneticFluxDensity.GAUSS unit type with code 4. */
    public static final UnitType MAGNETICFLUXDENSITY_GAUSS =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 4, MagneticFluxDensityUnit.GAUSS, "GAUSS", "G");

    /* ================================================ MAGNETICFLUX =================================================== */

    /** MagneticFlux.WEBER unit type with code 0. */
    public static final UnitType MAGNETICFLUX_WEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 0, MagneticFluxUnit.WEBER, "WEBER", "Wb");

    /** MagneticFlux.MILLIWEBER unit type with code 1. */
    public static final UnitType MAGNETICFLUX_MILLIWEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 1, MagneticFluxUnit.MILLIWEBER, "MILLIWEBER", "mWb");

    /** MagneticFlux.MICROWEBER unit type with code 2. */
    public static final UnitType MAGNETICFLUX_MICROWEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 2, MagneticFluxUnit.MICROWEBER, "MICROWEBER", "μWb");

    /** MagneticFlux.NANOWEBER unit type with code 3. */
    public static final UnitType MAGNETICFLUX_NANOWEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 3, MagneticFluxUnit.NANOWEBER, "NANOWEBER", "nWb");

    /** MagneticFlux.MAXWELL unit type with code 4. */
    public static final UnitType MAGNETICFLUX_MAXWELL =
            new UnitType(QuantityType.MAGNETICFLUX, 4, MagneticFluxUnit.MAXWELL, "MAXWELL", "Mx");

    /* ================================================ RADIOACTIVITY ================================================== */

    /** RadioActivity.BECQUEREL unit type with code 0. */
    public static final UnitType RADIOACTIVITY_BECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 0, RadioActivityUnit.BECQUEREL, "BECQUEREL", "Bq");

    /** RadioActivity.KILOBECQUEREL unit type with code 1. */
    public static final UnitType RADIOACTIVITY_KILOBECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 1, RadioActivityUnit.KILOBECQUEREL, "KILOBECQUEREL", "kBq");

    /** RadioActivity.MEGABECQUEREL unit type with code 2. */
    public static final UnitType RADIOACTIVITY_MEGABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 2, RadioActivityUnit.MEGABECQUEREL, "MEGABECQUEREL", "MBq");

    /** RadioActivity.GIGABECQUEREL unit type with code 3. */
    public static final UnitType RADIOACTIVITY_GIGABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 3, RadioActivityUnit.GIGABECQUEREL, "GIGABECQUEREL", "GBq");

    /** RadioActivity.TERABECQUEREL unit type with code 4. */
    public static final UnitType RADIOACTIVITY_TERABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 4, RadioActivityUnit.TERABECQUEREL, "TERABECQUEREL", "TBq");

    /** RadioActivity.PETABECQUEREL unit type with code 5. */
    public static final UnitType RADIOACTIVITY_PETABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 5, RadioActivityUnit.PETABECQUEREL, "PETABECQUEREL", "PBq");

    /** RadioActivity.CURIE unit type with code 6. */
    public static final UnitType RADIOACTIVITY_CURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 6, RadioActivityUnit.CURIE, "CURIE", "Ci");

    /** RadioActivity.MILLICURIE unit type with code 7. */
    public static final UnitType RADIOACTIVITY_MILLICURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 7, RadioActivityUnit.MILLICURIE, "MILLICURIE", "mCi");

    /** RadioActivity.MICROCURIE unit type with code 8. */
    public static final UnitType RADIOACTIVITY_MICROCURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 8, RadioActivityUnit.MICROCURIE, "MICROCURIE", "μCi");

    /** RadioActivity.NANOCURIE unit type with code 9. */
    public static final UnitType RADIOACTIVITY_NANOCURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 9, RadioActivityUnit.NANOCURIE, "NANOCURIE", "nCi");

    /** RadioActivity.RUTHERFORD unit type with code 10. */
    public static final UnitType RADIOACTIVITY_RUTHERFORD =
            new UnitType(QuantityType.RADIOACTIVITY, 10, RadioActivityUnit.RUTHERFORD, "RUTHERFORD", "Rd");

    /* ============================================= ANGULARACCELERATION =============================================== */

    /** AngularAcceleration.RADIAN_PER_SECOND_2 unit type with code 0. */
    public static final UnitType ANGULARACCELERATION_RADIAN_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 0, AngularAccelerationUnit.RADIAN_PER_SECOND_SQUARED,
                    "RADIAN_PER_SECOND_SQUARED", "rad/s2");

    /** AngularAcceleration.DEGREE_PER_SECOND_2 unit type with code 1. */
    public static final UnitType ANGULARACCELERATION_DEGREE_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 1, AngularAccelerationUnit.DEGREE_PER_SECOND_SQUARED,
                    "DEGREE_PER_SECOND_SQUARED", "deg/s2");

    /** AngularAcceleration.ARCMINUTE_PER_SECOND_2 unit type with code 2. */
    public static final UnitType ANGULARACCELERATION_ARCMINUTE_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 2, AngularAccelerationUnit.ARCMINUTE_PER_SECOND_SQUARED,
                    "ARCMINUTE_PER_SECOND_SQUARED", "arcmin/s2");

    /** AngularAcceleration.ARCSECOND_PER_SECOND_2 unit type with code 3. */
    public static final UnitType ANGULARACCELERATION_ARCSECOND_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 3, AngularAccelerationUnit.ARCSECOND_PER_SECOND_SQUARED,
                    "ARCSECOND_PER_SECOND_SQUARED", "arcsec/s2");

    /** AngularAcceleration.GRAD_PER_SECOND_2 unit type with code 4. */
    public static final UnitType ANGULARACCELERATION_GRAD_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 4, AngularAccelerationUnit.GRAD_PER_SECOND_SQUARED,
                    "GRAD_PER_SECOND_SQUARED", "grad/s2");

    /** AngularAcceleration.CENTESIMAL_ARCMINUTE_PER_SECOND_SQUARED unit type with code 5. */
    public static final UnitType ANGULARACCELERATION_CENTECIMAL_ARCMINUTE_PER_SECOND_2 = new UnitType(
            QuantityType.ANGULARACCELERATION, 5, AngularAccelerationUnit.CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED,
            "CENTECIMALARCMINUTE_PER_SECOND_SQUARED", "cdm/s2");

    /** AngularAcceleration.CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED unit type with code 6. */
    public static final UnitType ANGULARACCELERATION_CENTESIMAL_ARCSECOND_PER_SECOND_2 = new UnitType(
            QuantityType.ANGULARACCELERATION, 6, AngularAccelerationUnit.CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED,
            "CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED", "cds/s2");

    /* =============================================== ANGULARVELOCITY ================================================= */

    /** AngularVelocity.RADIAN_PER_SECOND unit type with code 0. */
    public static final UnitType ANGULARVELOCITY_RADIAN_PER_SECOND = new UnitType(QuantityType.ANGULARACCELERATION,
            0, AngularVelocityUnit.RADIAN_PER_SECOND, "RADIAN_PER_SECOND", "rad/s");

    /** AngularVelocity.DEGREE_PER_SECOND unit type with code 1. */
    public static final UnitType ANGULARVELOCITY_DEGREE_PER_SECOND = new UnitType(QuantityType.ANGULARACCELERATION,
            1, AngularVelocityUnit.DEGREE_PER_SECOND, "DEGREE_PER_SECOND", "deg/s");

    /** AngularVelocity.ARCMINUTE_PER_SECOND unit type with code 2. */
    public static final UnitType ANGULARVELOCITY_ARCMINUTE_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 2, AngularVelocityUnit.ARCMINUTE_PER_SECOND,
                    "ARCMINUTE_PER_SECOND", "arcmin/s");

    /** AngularVelocity.ARCSECOND_PER_SECOND unit type with code 3. */
    public static final UnitType ANGULARVELOCITY_ARCSECOND_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 3, AngularVelocityUnit.ARCSECOND_PER_SECOND,
                    "ARCSECOND_PER_SECOND", "arcsec/s");

    /** AngularVelocity.GRAD_PER_SECOND unit type with code 4. */
    public static final UnitType ANGULARVELOCITY_GRAD_PER_SECOND = new UnitType(QuantityType.ANGULARACCELERATION, 4,
            AngularVelocityUnit.GRAD_PER_SECOND, "GRAD_PER_SECOND", "grad/s");

    /** AngularVelocity.CENTESIMAL_ARCMINUTE_PER_SECOND unit type with code 5. */
    public static final UnitType ANGULARVELOCITY_CENTECIMAL_ARCMINUTE_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 5, AngularVelocityUnit.CENTESIMAL_ARCSECOND_PER_SECOND,
                    "CENTECIMALARCMINUTE_PER_SECOND", "cdm/s");

    /** AngularVelocity.CENTESIMAL_ARCSECOND_PER_SECOND unit type with code 6. */
    public static final UnitType ANGULARVELOCITY_CENTESIMAL_ARCSECOND_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 6, AngularVelocityUnit.CENTESIMAL_ARCSECOND_PER_SECOND,
                    "CENTESIMAL_ARCSECOND_PER_SECOND", "cds/s");

    /* ================================================== MOMENTUM ===================================================== */

    /** Momentum.KILOGRAM_METER_PER_SECOND unit type with code 0. */
    public static final UnitType KILOGRAM_METER_PER_SECOND = new UnitType(QuantityType.ANGULARACCELERATION, 0,
            MomentumUnit.KILOGRAM_METER_PER_SECOND, "KILOGRAM_METER_PER_SECOND", "kgm/s");

    /* ================================================== END TYPES ==================================================== */

    /**
     * @param unitType the corresponding 0MQ unit type
     * @param code the code of the unit provided as an int
     * @param djunitsType the djunits data type
     * @param name the unit name
     * @param abbreviation the unit abbreviation
     * @param <U> the unit
     */
    public <U extends Unit<U>> UnitType(final QuantityType unitType, final int code, final U djunitsType,
            final String name, final String abbreviation)
    {
        Throw.whenNull(unitType, "unitType should not be null");
        Throw.whenNull(djunitsType, "djunitsType should not be null");
        Throw.whenNull(name, "name should not be null");
        Throw.whenNull(abbreviation, "abbreviation should not be null");
        Throw.when(name.length() == 0, SerializationRuntimeException.class, "name should not be empty");
        Throw.when(abbreviation.length() == 0, SerializationRuntimeException.class, "abbreviation should not be empty");

        this.unitType = unitType;
        this.code = code;
        this.djunitsType = djunitsType;
        this.name = name;
        this.abbreviation = abbreviation;
        Map<Integer, UnitType> codeMap = codeDisplayTypeMap.get(this.unitType);
        if (codeMap == null)
        {
            codeMap = new HashMap<>();
            codeDisplayTypeMap.put(this.unitType, codeMap);
        }
        codeMap.put(this.code, this);
        djunitsDisplayTypeMap.put(this.djunitsType, this);
    }

    /**
     * Return the display type belonging to the display code.
     * @param unitType the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitType getDisplayType(final QuantityType unitType, final Integer code)
    {
        Map<Integer, UnitType> byteMap = codeDisplayTypeMap.get(unitType);
        return byteMap == null ? null : byteMap.get(code);
    }

    /**
     * Return the display type belonging to the display code.
     * @param unitTypeCode the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitType getDisplayType(final byte unitTypeCode, final int code)
    {
        QuantityType unitType = QuantityType.getUnitType(unitTypeCode);
        Map<Integer, UnitType> codeMap = codeDisplayTypeMap.get(unitType);
        return codeMap == null ? null : codeMap.get(code);
    }

    /**
     * Return the unit belonging to the display code.
     * @param unitTypeCode the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static Unit<?> getUnit(final byte unitTypeCode, final int code)
    {
        QuantityType unitType = QuantityType.getUnitType(unitTypeCode);
        Map<Integer, UnitType> codeMap = codeDisplayTypeMap.get(unitType);
        return codeMap == null ? null : codeMap.get(code) == null ? null : codeMap.get(code).djunitsType;
    }

    /**
     * Return the unit belonging to the display code.
     * @param unitType the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static Unit<?> getUnit(final QuantityType unitType, final int code)
    {
        Map<Integer, UnitType> codeMap = codeDisplayTypeMap.get(unitType);
        return codeMap == null ? null : codeMap.get(code) == null ? null : codeMap.get(code).djunitsType;
    }

    /**
     * @return unitType
     */
    public QuantityType getUnitType()
    {
        return this.unitType;
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the unit
     */
    public static <U extends Unit<U>> UnitType getDisplayType(final U unit)
    {
        return djunitsDisplayTypeMap.get(unit);
    }

    /**
     * Return the display code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the unit
     */
    public static <U extends Unit<U>> int getIntCode(final U unit)
    {
        QuantityType type = QuantityType.getUnitType(unit);
        UnitType displayType = type == null ? null : getDisplayType(unit);
        return displayType == null ? null : displayType.getIntCode();
    }

    /**
     * Return the display code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the unit
     */
    public static <U extends Unit<U>> byte getByteCode(final U unit)
    {
        QuantityType type = QuantityType.getUnitType(unit);
        UnitType displayType = type == null ? null : getDisplayType(unit);
        return displayType == null ? null : displayType.getByteCode();
    }

    /**
     * @return code
     */
    public final int getIntCode()
    {
        return this.code;
    }

    /**
     * @return code
     */
    public final byte getByteCode()
    {
        return (byte) (this.code & 0xFF);
    }

    /**
     * @return djunitsType
     */
    public final Unit<?> getDjunitsType()
    {
        return this.djunitsType;
    }

    /**
     * @return name
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * @return abbreviation
     */
    public final String getAbbreviation()
    {
        return this.abbreviation;
    }

}
