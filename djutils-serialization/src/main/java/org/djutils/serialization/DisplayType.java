package org.djutils.serialization;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.AbsoluteTemperatureUnit;
import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.AngleSolidUnit;
import org.djunits.unit.AngleUnit;
import org.djunits.unit.AreaUnit;
import org.djunits.unit.DensityUnit;
import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.DirectionUnit;
import org.djunits.unit.DurationUnit;
import org.djunits.unit.ElectricalChargeUnit;
import org.djunits.unit.ElectricalCurrentUnit;
import org.djunits.unit.ElectricalPotentialUnit;
import org.djunits.unit.ElectricalResistanceUnit;
import org.djunits.unit.EnergyUnit;
import org.djunits.unit.FlowMassUnit;
import org.djunits.unit.FlowVolumeUnit;
import org.djunits.unit.ForceUnit;
import org.djunits.unit.FrequencyUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.unit.LinearDensityUnit;
import org.djunits.unit.MassUnit;
import org.djunits.unit.MoneyUnit;
import org.djunits.unit.PositionUnit;
import org.djunits.unit.PowerUnit;
import org.djunits.unit.PressureUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TemperatureUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.unit.TorqueUnit;
import org.djunits.unit.Unit;
import org.djunits.unit.VolumeUnit;

/**
 * DJUNITS Display Types to be used as part of a Sim0MQ message.
 * <p>
 * Copyright (c) 2016-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Mar 4, 2017 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DisplayType implements Serializable
{
    /** */
    private static final long serialVersionUID = 20170314L;

    /** the unit types from number to type. */
    private static Map<SerializationUnits, Map<Integer, DisplayType>> codeDisplayTypeMap = new HashMap<>();

    /** the unit types from class to type. */
    private static Map<Unit<?>, DisplayType> djunitsDisplayTypeMap = new HashMap<>();

    /** the code of the unit as a byte. */
    private final int code;

    /** the corresponding unit data type. */
    private final SerializationUnits unitType;

    /** the djunits data type. */
    private final Unit<?> djunitsType;

    /** the unit name. */
    private final String name;

    /** the unit description. */
    private final String abbreviation;

    /* ================================================== DIMENSIONLESS ================================================== */

    /** Dimensionless.SI unit type with code 0. */
    public static final DisplayType DIMENSIONLESS_SI =
            new DisplayType(SerializationUnits.DIMENSIONLESS, 0, DimensionlessUnit.SI, "SI", "[]");

    /* ================================================== ACCELERATION =================================================== */

    /** Acceleration.METER_PER_SECOND_2 unit type with code 0. */
    public static final DisplayType ACCELERATION_METER_PER_SECOND_2 = new DisplayType(SerializationUnits.ACCELERATION,
            0, AccelerationUnit.METER_PER_SECOND_2, "METER_PER_SECOND_2", "m/s2");

    /** Acceleration.KM_PER_HOUR_2 unit type with code 1. */
    public static final DisplayType ACCELERATION_KM_PER_HOUR_2 =
            new DisplayType(SerializationUnits.ACCELERATION, 1, AccelerationUnit.KM_PER_HOUR_2, "KM_PER_HOUR_2", "km/h2");

    /** Acceleration.INCH_PER_SECOND_2 unit type with code 2. */
    public static final DisplayType ACCELERATION_INCH_PER_SECOND_2 = new DisplayType(SerializationUnits.ACCELERATION, 2,
            AccelerationUnit.INCH_PER_SECOND_2, "INCH_PER_SECOND_2", "in/s2");

    /** Acceleration.FOOT_PER_SECOND_2 unit type with code 3. */
    public static final DisplayType ACCELERATION_FOOT_PER_SECOND_2 = new DisplayType(SerializationUnits.ACCELERATION, 3,
            AccelerationUnit.FOOT_PER_SECOND_2, "FOOT_PER_SECOND_2", "ft/s2");

    /** Acceleration.MILE_PER_HOUR_2 unit type with code 4. */
    public static final DisplayType ACCELERATION_MILE_PER_HOUR_2 =
            new DisplayType(SerializationUnits.ACCELERATION, 4, AccelerationUnit.MILE_PER_HOUR_2, "MILE_PER_HOUR_2", "mi/h2");

    /** Acceleration.MILE_PER_HOUR_PER_SECOND unit type with code 5. */
    public static final DisplayType ACCELERATION_MILE_PER_HOUR_PER_SECOND = new DisplayType(
            SerializationUnits.ACCELERATION, 5, AccelerationUnit.MILE_PER_HOUR_PER_SECOND, "MILE_PER_HOUR_PER_SECOND", "mi/h/s");

    /** Acceleration.KNOT_PER_SECOND unit type with code 6. */
    public static final DisplayType ACCELERATION_KNOT_PER_SECOND =
            new DisplayType(SerializationUnits.ACCELERATION, 6, AccelerationUnit.KNOT_PER_SECOND, "KNOT_PER_SECOND", "kt/s");

    /** Acceleration.GAL unit type with code 7. */
    public static final DisplayType ACCELERATION_GAL =
            new DisplayType(SerializationUnits.ACCELERATION, 7, AccelerationUnit.GAL, "GAL", "gal");

    /** Acceleration.STANDARD_GRAVITY unit type with code 8. */
    public static final DisplayType ACCELERATION_STANDARD_GRAVITY =
            new DisplayType(SerializationUnits.ACCELERATION, 8, AccelerationUnit.STANDARD_GRAVITY, "STANDARD_GRAVITY", "g");

    /* =================================================== ANGLESOLID ==================================================== */

    /** AngleSolid.STERADIAN unit type with code 0. */
    public static final DisplayType ANGLESOLID_STERADIAN =
            new DisplayType(SerializationUnits.ANGLESOLID, 0, AngleSolidUnit.STERADIAN, "STERADIAN", "sr");

    /** AngleSolid.SQUARE_DEGREE unit type with code 1. */
    public static final DisplayType ANGLESOLID_SQUARE_DEGREE =
            new DisplayType(SerializationUnits.ANGLESOLID, 1, AngleSolidUnit.SQUARE_DEGREE, "SQUARE_DEGREE", "sq.deg");

    /* ====================================================== ANGLE ====================================================== */

    /** Angle.RADIAN unit type with code 0. */
    public static final DisplayType ANGLE_RADIAN =
            new DisplayType(SerializationUnits.ANGLE, 0, AngleUnit.RADIAN, "RADIAN", "rad");

    /** Angle.ARCMINUTE unit type with code 1. */
    public static final DisplayType ANGLE_ARCMINUTE =
            new DisplayType(SerializationUnits.ANGLE, 1, AngleUnit.ARCMINUTE, "ARCMINUTE", "arcmin");

    /** Angle.ARCSECOND unit type with code 2. */
    public static final DisplayType ANGLE_ARCSECOND =
            new DisplayType(SerializationUnits.ANGLE, 2, AngleUnit.ARCSECOND, "ARCSECOND", "arcsec");

    /** Angle.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final DisplayType ANGLE_CENTESIMAL_ARCMINUTE = new DisplayType(SerializationUnits.ANGLE, 3,
            AngleUnit.CENTESIMAL_ARCMINUTE, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Angle.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final DisplayType ANGLE_CENTESIMAL_ARCSECOND = new DisplayType(SerializationUnits.ANGLE, 4,
            AngleUnit.CENTESIMAL_ARCSECOND, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Angle.DEGREE unit type with code 5. */
    public static final DisplayType ANGLE_DEGREE =
            new DisplayType(SerializationUnits.ANGLE, 5, AngleUnit.DEGREE, "DEGREE", "deg");

    /** Angle.GRAD unit type with code 6. */
    public static final DisplayType ANGLE_GRAD =
            new DisplayType(SerializationUnits.ANGLE, 6, AngleUnit.GRAD, "GRAD", "grad");

    /* ==================================================== DIRECTION ==================================================== */

    /** Direction.NORTH_RADIAN unit type with code 0. */
    public static final DisplayType DIRECTION_NORTH_RADIAN =
            new DisplayType(SerializationUnits.DIRECTION, 0, DirectionUnit.NORTH_RADIAN, "NORTH_RADIAN", "rad(N)");

    /** Direction.NORTH_DEGREE unit type with code 1. */
    public static final DisplayType DIRECTION_NORTH_DEGREE =
            new DisplayType(SerializationUnits.DIRECTION, 1, DirectionUnit.NORTH_DEGREE, "NORTH_DEGREE", "deg(N)");

    /** Direction.EAST_RADIAN unit type with code 2. */
    public static final DisplayType DIRECTION_EAST_RADIAN =
            new DisplayType(SerializationUnits.DIRECTION, 2, DirectionUnit.EAST_RADIAN, "EAST_RADIAN", "rad(E)");

    /** Direction.EAST_DEGREE unit type with code 2. */
    public static final DisplayType DIRECTION_EAST_DEGREE =
            new DisplayType(SerializationUnits.DIRECTION, 3, DirectionUnit.EAST_DEGREE, "EAST_DEGREE", "deg(E)");

    /* ====================================================== AREA ======================================================= */

    /** Area.SQUARE_METER unit type with code 0. */
    public static final DisplayType AREA_SQUARE_METER =
            new DisplayType(SerializationUnits.AREA, 0, AreaUnit.SQUARE_METER, "SQUARE_METER", "m2");

    /** Area.SQUARE_ATTOMETER unit type with code 1. */
    public static final DisplayType AREA_SQUARE_ATTOMETER =
            new DisplayType(SerializationUnits.AREA, 1, AreaUnit.SQUARE_ATTOMETER, "SQUARE_ATTOMETER", "am2");

    /** Area.SQUARE_FEMTOMETER unit type with code 2. */
    public static final DisplayType AREA_SQUARE_FEMTOMETER =
            new DisplayType(SerializationUnits.AREA, 2, AreaUnit.SQUARE_FEMTOMETER, "SQUARE_FEMTOMETER", "fm2");

    /** Area.SQUARE_PICOMETER unit type with code 3. */
    public static final DisplayType AREA_SQUARE_PICOMETER =
            new DisplayType(SerializationUnits.AREA, 3, AreaUnit.SQUARE_PICOMETER, "SQUARE_PICOMETER", "pm2");

    /** Area.SQUARE_NANOMETER unit type with code 4. */
    public static final DisplayType AREA_SQUARE_NANOMETER =
            new DisplayType(SerializationUnits.AREA, 4, AreaUnit.SQUARE_NANOMETER, "SQUARE_NANOMETER", "nm2");

    /** Area.SQUARE_MICROMETER unit type with code 5. */
    public static final DisplayType AREA_SQUARE_MICROMETER =
            new DisplayType(SerializationUnits.AREA, 5, AreaUnit.SQUARE_MICROMETER, "SQUARE_MICROMETER", "μm2");

    /** Area.SQUARE_MILLIMETER unit type with code 6. */
    public static final DisplayType AREA_SQUARE_MILLIMETER =
            new DisplayType(SerializationUnits.AREA, 6, AreaUnit.SQUARE_MILLIMETER, "SQUARE_MILLIMETER", "mm2");

    /** Area.SQUARE_CENTIMETER unit type with code 7. */
    public static final DisplayType AREA_SQUARE_CENTIMETER =
            new DisplayType(SerializationUnits.AREA, 7, AreaUnit.SQUARE_CENTIMETER, "SQUARE_CENTIMETER", "cm2");

    /** Area.SQUARE_DECIMETER unit type with code 8. */
    public static final DisplayType AREA_SQUARE_DECIMETER =
            new DisplayType(SerializationUnits.AREA, 8, AreaUnit.SQUARE_DECIMETER, "SQUARE_DECIMETER", "dm2");

    /** Area.SQUARE_DEKAMETER unit type with code 9. */
    public static final DisplayType AREA_SQUARE_DEKAMETER =
            new DisplayType(SerializationUnits.AREA, 9, AreaUnit.SQUARE_DEKAMETER, "SQUARE_DEKAMETER", "dam2");

    /** Area.SQUARE_HECTOMETER unit type with code 10. */
    public static final DisplayType AREA_SQUARE_HECTOMETER =
            new DisplayType(SerializationUnits.AREA, 10, AreaUnit.SQUARE_HECTOMETER, "SQUARE_HECTOMETER", "hm2");

    /** Area.SQUARE_KILOMETER unit type with code 11. */
    public static final DisplayType AREA_SQUARE_KILOMETER =
            new DisplayType(SerializationUnits.AREA, 11, AreaUnit.SQUARE_KILOMETER, "SQUARE_KILOMETER", "km2");

    /** Area.SQUARE_MEGAMETER unit type with code 12. */
    public static final DisplayType AREA_SQUARE_MEGAMETER =
            new DisplayType(SerializationUnits.AREA, 12, AreaUnit.SQUARE_MEGAMETER, "SQUARE_MEGAMETER", "Mm2");

    /** Area.SQUARE_INCH unit type with code 13. */
    public static final DisplayType AREA_SQUARE_INCH =
            new DisplayType(SerializationUnits.AREA, 13, AreaUnit.SQUARE_INCH, "SQUARE_INCH", "in2");

    /** Area.SQUARE_FOOT unit type with code 14. */
    public static final DisplayType AREA_SQUARE_FOOT =
            new DisplayType(SerializationUnits.AREA, 14, AreaUnit.SQUARE_FOOT, "SQUARE_FOOT", "ft2");

    /** Area.SQUARE_YARD unit type with code 15. */
    public static final DisplayType AREA_SQUARE_YARD =
            new DisplayType(SerializationUnits.AREA, 15, AreaUnit.SQUARE_YARD, "SQUARE_YARD", "yd2");

    /** Area.SQUARE_MILE unit type with code 16. */
    public static final DisplayType AREA_SQUARE_MILE =
            new DisplayType(SerializationUnits.AREA, 16, AreaUnit.SQUARE_MILE, "SQUARE_MILE", "mi2");

    /** Area.SQUARE_NAUTICAL_MILE unit type with code 17. */
    public static final DisplayType AREA_SQUARE_NAUTICAL_MILE =
            new DisplayType(SerializationUnits.AREA, 17, AreaUnit.SQUARE_NAUTICAL_MILE, "SQUARE_NAUTICAL_MILE", "NM2");

    /** Area.ACRE unit type with code 18. */
    public static final DisplayType AREA_ACRE =
            new DisplayType(SerializationUnits.AREA, 18, AreaUnit.ACRE, "ACRE", "acre");

    /** Area.ARE unit type with code 19. */
    public static final DisplayType AREA_ARE = new DisplayType(SerializationUnits.AREA, 19, AreaUnit.ARE, "ARE", "a");

    /** Area.CENTIARE unit type with code 20. */
    public static final DisplayType AREA_CENTIARE =
            new DisplayType(SerializationUnits.AREA, 20, AreaUnit.CENTIARE, "CENTIARE", "ca");

    /** Area.HECTARE unit type with code 21. */
    public static final DisplayType AREA_HECTARE =
            new DisplayType(SerializationUnits.AREA, 21, AreaUnit.HECTARE, "HECTARE", "ha");

    /* ===================================================== DENSITY ===================================================== */

    /** Density.KG_PER_METER_3 unit type with code 0. */
    public static final DisplayType DENSITY_KG_PER_METER_3 =
            new DisplayType(SerializationUnits.DENSITY, 0, DensityUnit.KG_PER_METER_3, "KG_PER_METER_3", "kg/m3");

    /** Density.GRAM_PER_CENTIMETER_3 unit type with code 1. */
    public static final DisplayType DENSITY_GRAM_PER_CENTIMETER_3 = new DisplayType(SerializationUnits.DENSITY, 1,
            DensityUnit.GRAM_PER_CENTIMETER_3, "GRAM_PER_CENTIMETER_3", "g/cm3");

    /* ================================================ ELECTRICALCHARGE ================================================= */

    /** ElectricalCharge.COULOMB unit type with code 0. */
    public static final DisplayType ELECTRICALCHARGE_COULOMB =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 0, ElectricalChargeUnit.COULOMB, "COULOMB", "C");

    /** ElectricalCharge.PICOCOULOMB unit type with code 1. */
    public static final DisplayType ELECTRICALCHARGE_PICOCOULOMB =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 1, ElectricalChargeUnit.PICOCOULOMB, "PICOCOULOMB", "pC");

    /** ElectricalCharge.NANOCOULOMB unit type with code 2. */
    public static final DisplayType ELECTRICALCHARGE_NANOCOULOMB =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 2, ElectricalChargeUnit.NANOCOULOMB, "NANOCOULOMB", "nC");

    /** ElectricalCharge.MICROCOULOMB unit type with code 3. */
    public static final DisplayType ELECTRICALCHARGE_MICROCOULOMB =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 3, ElectricalChargeUnit.MICROCOULOMB, "MICROCOULOMB", "μC");

    /** ElectricalCharge.MILLICOULOMB unit type with code 4. */
    public static final DisplayType ELECTRICALCHARGE_MILLICOULOMB =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 4, ElectricalChargeUnit.MILLICOULOMB, "MILLICOULOMB", "mC");

    /** ElectricalCharge.ABCOULOMB unit type with code 5. */
    public static final DisplayType ELECTRICALCHARGE_ABCOULOMB =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 5, ElectricalChargeUnit.ABCOULOMB, "ABCOULOMB", "abC");

    /** ElectricalCharge.ATOMIC_UNIT unit type with code 6. */
    public static final DisplayType ELECTRICALCHARGE_ATOMIC_UNIT =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 6, ElectricalChargeUnit.ATOMIC_UNIT, "ATOMIC_UNIT", "au");

    /** ElectricalCharge.EMU unit type with code 7. */
    public static final DisplayType ELECTRICALCHARGE_EMU =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 7, ElectricalChargeUnit.EMU, "EMU", "emu");

    /** ElectricalCharge.ESU unit type with code 8. */
    public static final DisplayType ELECTRICALCHARGE_ESU =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 8, ElectricalChargeUnit.ESU, "ESU", "esu");

    /** ElectricalCharge.FARADAY unit type with code 9. */
    public static final DisplayType ELECTRICALCHARGE_FARADAY =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 9, ElectricalChargeUnit.FARADAY, "FARADAY", "F");

    /** ElectricalCharge.FRANKLIN unit type with code 10. */
    public static final DisplayType ELECTRICALCHARGE_FRANKLIN =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 10, ElectricalChargeUnit.FRANKLIN, "FRANKLIN  ", "Fr");

    /** ElectricalCharge.STATCOULOMB unit type with code 11. */
    public static final DisplayType ELECTRICALCHARGE_STATCOULOMB = new DisplayType(SerializationUnits.ELECTRICALCHARGE,
            11, ElectricalChargeUnit.STATCOULOMB, "STATCOULOMB", "statC");

    /** ElectricalCharge.MILLIAMPERE_HOUR unit type with code 12. */
    public static final DisplayType ELECTRICALCHARGE_MILLIAMPERE_HOUR = new DisplayType(
            SerializationUnits.ELECTRICALCHARGE, 12, ElectricalChargeUnit.MILLIAMPERE_HOUR, "MILLIAMPERE_HOUR", "mAh");

    /** ElectricalCharge.AMPERE_HOUR unit type with code 13. */
    public static final DisplayType ELECTRICALCHARGE_AMPERE_HOUR =
            new DisplayType(SerializationUnits.ELECTRICALCHARGE, 13, ElectricalChargeUnit.AMPERE_HOUR, "AMPERE_HOUR", "Ah");

    /** ElectricalCharge.KILOAMPERE_HOUR unit type with code 14. */
    public static final DisplayType ELECTRICALCHARGE_KILOAMPERE_HOUR = new DisplayType(
            SerializationUnits.ELECTRICALCHARGE, 14, ElectricalChargeUnit.KILOAMPERE_HOUR, "KILOAMPERE_HOUR", "kAh");

    /** ElectricalCharge.MEGAAMPERE_HOUR unit type with code 15. */
    public static final DisplayType ELECTRICALCHARGE_MEGAAMPERE_HOUR = new DisplayType(
            SerializationUnits.ELECTRICALCHARGE, 15, ElectricalChargeUnit.MEGAAMPERE_HOUR, "MEGAAMPERE_HOUR", "MAh");

    /** ElectricalCharge.MILLIAMPERE_SECOND unit type with code 16. */
    public static final DisplayType ELECTRICALCHARGE_MILLIAMPERE_SECOND = new DisplayType(
            SerializationUnits.ELECTRICALCHARGE, 16, ElectricalChargeUnit.MILLIAMPERE_SECOND, "MILLIAMPERE_SECOND", "mAs");

    /* ============================================== ELECTRICALCURRENT ================================================== */

    /** ElectricalCurrent.AMPERE unit type with code 0. */
    public static final DisplayType ELECTRICALCURRENT_AMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 0, ElectricalCurrentUnit.AMPERE, "AMPERE", "A");

    /** ElectricalCurrent.NANOAMPERE unit type with code 1. */
    public static final DisplayType ELECTRICALCURRENT_NANOAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 1, ElectricalCurrentUnit.NANOAMPERE, "NANOAMPERE", "nA");

    /** ElectricalCurrent.MICROAMPERE unit type with code 2. */
    public static final DisplayType ELECTRICALCURRENT_MICROAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 2, ElectricalCurrentUnit.MICROAMPERE, "MICROAMPERE", "μA");

    /** ElectricalCurrent.MILLIAMPERE unit type with code 3. */
    public static final DisplayType ELECTRICALCURRENT_MILLIAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 3, ElectricalCurrentUnit.MILLIAMPERE, "MILLIAMPERE", "mA");

    /** ElectricalCurrent.KILOAMPERE unit type with code 4. */
    public static final DisplayType ELECTRICALCURRENT_KILOAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 4, ElectricalCurrentUnit.KILOAMPERE, "KILOAMPERE", "kA");

    /** ElectricalCurrent.MEGAAMPERE unit type with code 5. */
    public static final DisplayType ELECTRICALCURRENT_MEGAAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 5, ElectricalCurrentUnit.MEGAAMPERE, "MEGAAMPERE", "MA");

    /** ElectricalCurrent.ABAMPERE unit type with code 6. */
    public static final DisplayType ELECTRICALCURRENT_ABAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 6, ElectricalCurrentUnit.ABAMPERE, "ABAMPERE", "abA");

    /** ElectricalCurrent.STATAMPERE unit type with code 7. */
    public static final DisplayType ELECTRICALCURRENT_STATAMPERE =
            new DisplayType(SerializationUnits.ELECTRICALCURRENT, 7, ElectricalCurrentUnit.STATAMPERE, "STATAMPERE", "statA");

    /* ============================================= ELECTRICALPOTENTIAL ================================================= */

    /** ElectricalPotential.VOLT unit type with code 0. */
    public static final DisplayType ELECTRICALPOTENTIAL_VOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 0, ElectricalPotentialUnit.VOLT, "VOLT", "V");

    /** ElectricalPotential.NANOVOLT unit type with code 1. */
    public static final DisplayType ELECTRICALPOTENTIAL_NANOVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 1, ElectricalPotentialUnit.NANOVOLT, "NANOVOLT", "nV");

    /** ElectricalPotential.MICROVOLT unit type with code 2. */
    public static final DisplayType ELECTRICALPOTENTIAL_MICROVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 2, ElectricalPotentialUnit.MICROVOLT, "MICROVOLT", "μV");

    /** ElectricalPotential.MILLIVOLT unit type with code 3. */
    public static final DisplayType ELECTRICALPOTENTIAL_MILLIVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 3, ElectricalPotentialUnit.MILLIVOLT, "MILLIVOLT", "mV");

    /** ElectricalPotential.KILOVOLT unit type with code 4. */
    public static final DisplayType ELECTRICALPOTENTIAL_KILOVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 4, ElectricalPotentialUnit.KILOVOLT, "KILOVOLT", "kV");

    /** ElectricalPotential.MEGAVOLT unit type with code 5. */
    public static final DisplayType ELECTRICALPOTENTIAL_MEGAVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 5, ElectricalPotentialUnit.MEGAVOLT, "MEGAVOLT", "MV");

    /** ElectricalPotential.GIGAVOLT unit type with code 6. */
    public static final DisplayType ELECTRICALPOTENTIAL_GIGAVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 6, ElectricalPotentialUnit.GIGAVOLT, "GIGAVOLT", "GV");

    /** ElectricalPotential.ABVOLT unit type with code 7. */
    public static final DisplayType ELECTRICALPOTENTIAL_ABVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 7, ElectricalPotentialUnit.ABVOLT, "ABVOLT", "abV");

    /** ElectricalPotential.STATVOLT unit type with code 8. */
    public static final DisplayType ELECTRICALPOTENTIAL_STATVOLT =
            new DisplayType(SerializationUnits.ELECTRICALPOTENTIAL, 8, ElectricalPotentialUnit.STATVOLT, "STATVOLT", "statV");

    /* ============================================ ELECTRICALRESISTANCE ================================================= */

    /** ElectricalResistance.OHM unit type with code 0. */
    public static final DisplayType ELECTRICALRESISTANCE_OHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 0, ElectricalResistanceUnit.OHM, "OHM", "Ω");

    /** ElectricalResistance.NANOOHM unit type with code 1. */
    public static final DisplayType ELECTRICALRESISTANCE_NANOOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 1, ElectricalResistanceUnit.NANOOHM, "NANOOHM", "nΩ");

    /** ElectricalResistance.MICROOHM unit type with code 2. */
    public static final DisplayType ELECTRICALRESISTANCE_MICROOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 2, ElectricalResistanceUnit.MICROOHM, "MICROOHM", "μΩ");

    /** ElectricalResistance.MILLIOHM unit type with code 3. */
    public static final DisplayType ELECTRICALRESISTANCE_MILLIOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 3, ElectricalResistanceUnit.MILLIOHM, "MILLIOHM", "mΩ");

    /** ElectricalResistance.KILOOHM unit type with code 4. */
    public static final DisplayType ELECTRICALRESISTANCE_KILOOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 4, ElectricalResistanceUnit.KILOOHM, "KILOOHM", "kΩ");

    /** ElectricalResistance.MEGAOHM unit type with code 5. */
    public static final DisplayType ELECTRICALRESISTANCE_MEGAOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 5, ElectricalResistanceUnit.MEGAOHM, "MEGAOHM", "MΩ");

    /** ElectricalResistance.GIGAOHM unit type with code 6. */
    public static final DisplayType ELECTRICALRESISTANCE_GIGAOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 6, ElectricalResistanceUnit.GIGAOHM, "GIGAOHM", "GΩ");

    /** ElectricalResistance.ABOHM unit type with code 7. */
    public static final DisplayType ELECTRICALRESISTANCE_ABOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 7, ElectricalResistanceUnit.ABOHM, "ABOHM", "abΩ");

    /** ElectricalResistance.STATOHM unit type with code 8. */
    public static final DisplayType ELECTRICALRESISTANCE_STATOHM =
            new DisplayType(SerializationUnits.ELECTRICALRESISTANCE, 8, ElectricalResistanceUnit.STATOHM, "STATOHM", "statΩ");

    /* ===================================================== ENERGY ====================================================== */

    /** Energy.JOULE unit type with code 0. */
    public static final DisplayType ENERGY_JOULE =
            new DisplayType(SerializationUnits.ENERGY, 0, EnergyUnit.JOULE, "JOULE", "J");

    /** Energy.PICOJOULE unit type with code 1. */
    public static final DisplayType ENERGY_PICOJOULE =
            new DisplayType(SerializationUnits.ENERGY, 1, EnergyUnit.PICOJOULE, "PICOJOULE", "pJ");

    /** Energy.NANOJOULE unit type with code 2. */
    public static final DisplayType ENERGY_NANOJOULE =
            new DisplayType(SerializationUnits.ENERGY, 2, EnergyUnit.NANOJOULE, "NANOJOULE", "mJ");

    /** Energy.MICROJOULE unit type with code 3. */
    public static final DisplayType ENERGY_MICROJOULE =
            new DisplayType(SerializationUnits.ENERGY, 3, EnergyUnit.MICROJOULE, "MICROJOULE", "μJ");

    /** Energy.MILLIJOULE unit type with code 4. */
    public static final DisplayType ENERGY_MILLIJOULE =
            new DisplayType(SerializationUnits.ENERGY, 4, EnergyUnit.MILLIJOULE, "MILLIJOULE", "mJ");

    /** Energy.KILOJOULE unit type with code 5. */
    public static final DisplayType ENERGY_KILOJOULE =
            new DisplayType(SerializationUnits.ENERGY, 5, EnergyUnit.KILOJOULE, "KILOJOULE", "kJ");

    /** Energy.MEGAJOULE unit type with code 6. */
    public static final DisplayType ENERGY_MEGAJOULE =
            new DisplayType(SerializationUnits.ENERGY, 6, EnergyUnit.MEGAJOULE, "MEGAJOULE", "MJ");

    /** Energy.GIGAJOULE unit type with code 7. */
    public static final DisplayType ENERGY_GIGAJOULE =
            new DisplayType(SerializationUnits.ENERGY, 7, EnergyUnit.GIGAJOULE, "GIGAJOULE", "GJ");

    /** Energy.TERAJOULE unit type with code 8. */
    public static final DisplayType ENERGY_TERAJOULE =
            new DisplayType(SerializationUnits.ENERGY, 8, EnergyUnit.TERAJOULE, "TERAJOULE", "TJ");

    /** Energy.PETAJOULE unit type with code 9. */
    public static final DisplayType ENERGY_PETAJOULE =
            new DisplayType(SerializationUnits.ENERGY, 9, EnergyUnit.PETAJOULE, "PETAJOULE", "PJ");

    /** Energy.ELECTRONVOLT unit type with code 10. */
    public static final DisplayType ENERGY_ELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 10, EnergyUnit.ELECTRONVOLT, "ELECTRONVOLT", "eV");

    /** Energy.MICROELECTRONVOLT unit type with code 11. */
    public static final DisplayType ENERGY_MICROELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 11, EnergyUnit.MICROELECTRONVOLT, "MICROELECTRONVOLT", "μeV");

    /** Energy.MILLIELECTRONVOLT unit type with code 12. */
    public static final DisplayType ENERGY_MILLIELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 12, EnergyUnit.MILLIELECTRONVOLT, "MILLIELECTRONVOLT", "meV");

    /** Energy.KILOELECTRONVOLT unit type with code 13. */
    public static final DisplayType ENERGY_KILOELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 13, EnergyUnit.KILOELECTRONVOLT, "KILOELECTRONVOLT", "keV");

    /** Energy.MEGAELECTRONVOLT unit type with code 14. */
    public static final DisplayType ENERGY_MEGAELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 14, EnergyUnit.MEGAELECTRONVOLT, "MEGAELECTRONVOLT", "MeV");

    /** Energy.GIGAELECTRONVOLT unit type with code 15. */
    public static final DisplayType ENERGY_GIGAELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 15, EnergyUnit.GIGAELECTRONVOLT, "GIGAELECTRONVOLT", "GeV");

    /** Energy.TERAELECTRONVOLT unit type with code 16. */
    public static final DisplayType ENERGY_TERAELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 16, EnergyUnit.TERAELECTRONVOLT, "TERAELECTRONVOLT", "TeV");

    /** Energy.PETAELECTRONVOLT unit type with code 17. */
    public static final DisplayType ENERGY_PETAELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 17, EnergyUnit.PETAELECTRONVOLT, "PETAELECTRONVOLT", "PeV");

    /** Energy.EXAELECTRONVOLT unit type with code 18. */
    public static final DisplayType ENERGY_EXAELECTRONVOLT =
            new DisplayType(SerializationUnits.ENERGY, 18, EnergyUnit.EXAELECTRONVOLT, "EXAELECTRONVOLT", "EeV");

    /** Energy.WATT_HOUR unit type with code 19. */
    public static final DisplayType ENERGY_WATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 19, EnergyUnit.WATT_HOUR, "WATT_HOUR", "Wh");

    /** Energy.FEMTOWATT_HOUR unit type with code 20. */
    public static final DisplayType ENERGY_FEMTOWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 20, EnergyUnit.FEMTOWATT_HOUR, "FEMTOWATT_HOUR", "fWh");

    /** Energy.PICOWATT_HOUR unit type with code 21. */
    public static final DisplayType ENERGY_PICOWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 21, EnergyUnit.PICOWATT_HOUR, "PICOWATT_HOUR", "pWh");

    /** Energy.NANOWATT_HOUR unit type with code 22. */
    public static final DisplayType ENERGY_NANOWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 22, EnergyUnit.NANOWATT_HOUR, "NANOWATT_HOUR", "mWh");

    /** Energy.MICROWATT_HOUR unit type with code 23. */
    public static final DisplayType ENERGY_MICROWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 23, EnergyUnit.MICROWATT_HOUR, "MICROWATT_HOUR", "μWh");

    /** Energy.MILLIWATT_HOUR unit type with code 24. */
    public static final DisplayType ENERGY_MILLIWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 24, EnergyUnit.MILLIWATT_HOUR, "MILLIWATT_HOUR", "mWh");

    /** Energy.KILOWATT_HOUR unit type with code 25. */
    public static final DisplayType ENERGY_KILOWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 25, EnergyUnit.KILOWATT_HOUR, "KILOWATT_HOUR", "kWh");

    /** Energy.MEGAWATT_HOUR unit type with code 26. */
    public static final DisplayType ENERGY_MEGAWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 26, EnergyUnit.MEGAWATT_HOUR, "MEGAWATT_HOUR", "MWh");

    /** Energy.GIGAWATT_HOUR unit type with code 27. */
    public static final DisplayType ENERGY_GIGAWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 27, EnergyUnit.GIGAWATT_HOUR, "GIGAWATT_HOUR", "GWh");

    /** Energy.TERAWATT_HOUR unit type with code 28. */
    public static final DisplayType ENERGY_TERAWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 28, EnergyUnit.TERAWATT_HOUR, "TERAWATT_HOUR", "TWh");

    /** Energy.PETAWATT_HOUR unit type with code 29. */
    public static final DisplayType ENERGY_PETAWATT_HOUR =
            new DisplayType(SerializationUnits.ENERGY, 29, EnergyUnit.PETAWATT_HOUR, "PETAWATT_HOUR", "PWh");

    /** Energy.CALORIE unit type with code 30. */
    public static final DisplayType ENERGY_CALORIE =
            new DisplayType(SerializationUnits.ENERGY, 30, EnergyUnit.CALORIE, "CALORIE", "cal");

    /** Energy.KILOCALORIE unit type with code 31. */
    public static final DisplayType ENERGY_KILOCALORIE =
            new DisplayType(SerializationUnits.ENERGY, 31, EnergyUnit.KILOCALORIE, "KILOCALORIE", "kcal");

    /** Energy.CALORIE_IT unit type with code 32. */
    public static final DisplayType ENERGY_CALORIE_IT =
            new DisplayType(SerializationUnits.ENERGY, 32, EnergyUnit.CALORIE_IT, "CALORIE_IT", "cal(IT)");

    /** Energy.INCH_POUND_FORCE unit type with code 33. */
    public static final DisplayType ENERGY_INCH_POUND_FORCE =
            new DisplayType(SerializationUnits.ENERGY, 33, EnergyUnit.INCH_POUND_FORCE, "INCH_POUND_FORCE", "in lbf");

    /** Energy.FOOT_POUND_FORCE unit type with code 34. */
    public static final DisplayType ENERGY_FOOT_POUND_FORCE =
            new DisplayType(SerializationUnits.ENERGY, 34, EnergyUnit.FOOT_POUND_FORCE, "FOOT_POUND_FORCE", "ft lbf");

    /** Energy.ERG unit type with code 35. */
    public static final DisplayType ENERGY_ERG =
            new DisplayType(SerializationUnits.ENERGY, 35, EnergyUnit.ERG, "ERG", "erg");

    /** Energy.BTU_ISO unit type with code 36. */
    public static final DisplayType ENERGY_BTU_ISO =
            new DisplayType(SerializationUnits.ENERGY, 36, EnergyUnit.BTU_ISO, "BTU_ISO", "BTU(ISO)");

    /** Energy.BTU_IT unit type with code 37. */
    public static final DisplayType ENERGY_BTU_IT =
            new DisplayType(SerializationUnits.ENERGY, 37, EnergyUnit.BTU_IT, "BTU_IT", "BTU(IT)");

    /** Energy.STHENE_METER unit type with code 38. */
    public static final DisplayType ENERGY_STHENE_METER =
            new DisplayType(SerializationUnits.ENERGY, 38, EnergyUnit.STHENE_METER, "STHENE_METER", "sth.m");

    /* ==================================================== FLOWMASS ===================================================== */

    /** FlowMass.KG_PER_SECOND unit type with code 0. */
    public static final DisplayType FLOWMASS_KG_PER_SECOND =
            new DisplayType(SerializationUnits.FLOWMASS, 0, FlowMassUnit.KILOGRAM_PER_SECOND, "KG_PER_SECOND", "kg/s");

    /** FlowMass.POUND_PER_SECOND unit type with code 1. */
    public static final DisplayType FLOWMASS_POUND_PER_SECOND =
            new DisplayType(SerializationUnits.FLOWMASS, 1, FlowMassUnit.POUND_PER_SECOND, "POUND_PER_SECOND", "lb/s");

    /* =================================================== FLOWVOLUME ==================================================== */

    /** FlowVolume.CUBIC_METER_PER_SECOND unit type with code 0. */
    public static final DisplayType FLOWVOLUME_CUBIC_METER_PER_SECOND = new DisplayType(SerializationUnits.FLOWVOLUME,
            0, FlowVolumeUnit.CUBIC_METER_PER_SECOND, "CUBIC_METER_PER_SECOND", "m3/s");

    /** FlowVolume.CUBIC_METER_PER_MINUTE unit type with code 1. */
    public static final DisplayType FLOWVOLUME_CUBIC_METER_PER_MINUTE = new DisplayType(SerializationUnits.FLOWVOLUME,
            1, FlowVolumeUnit.CUBIC_METER_PER_MINUTE, "CUBIC_METER_PER_MINUTE", "m3/min");

    /** FlowVolume.CUBIC_METER_PER_HOUR unit type with code 2. */
    public static final DisplayType FLOWVOLUME_CUBIC_METER_PER_HOUR = new DisplayType(SerializationUnits.FLOWVOLUME, 2,
            FlowVolumeUnit.CUBIC_METER_PER_HOUR, "CUBIC_METER_PER_HOUR", "m3/h");

    /** FlowVolume.CUBIC_METER_PER_DAY unit type with code 3. */
    public static final DisplayType FLOWVOLUME_CUBIC_METER_PER_DAY = new DisplayType(SerializationUnits.FLOWVOLUME, 3,
            FlowVolumeUnit.CUBIC_METER_PER_DAY, "CUBIC_METER_PER_DAY", "m3/day");

    /** FlowVolume.CUBIC_INCH_PER_SECOND unit type with code 4. */
    public static final DisplayType FLOWVOLUME_CUBIC_INCH_PER_SECOND = new DisplayType(SerializationUnits.FLOWVOLUME, 4,
            FlowVolumeUnit.CUBIC_INCH_PER_SECOND, "CUBIC_INCH_PER_SECOND", "in3/s");

    /** FlowVolume.CUBIC_INCH_PER_MINUTE unit type with code 5. */
    public static final DisplayType FLOWVOLUME_CUBIC_INCH_PER_MINUTE = new DisplayType(SerializationUnits.FLOWVOLUME, 5,
            FlowVolumeUnit.CUBIC_INCH_PER_MINUTE, "CUBIC_INCH_PER_MINUTE", "in3/min");

    /** FlowVolume.CUBIC_FEET_PER_SECOND unit type with code 6. */
    public static final DisplayType FLOWVOLUME_CUBIC_FEET_PER_SECOND = new DisplayType(SerializationUnits.FLOWVOLUME, 6,
            FlowVolumeUnit.CUBIC_FEET_PER_SECOND, "CUBIC_FEET_PER_SECOND", "ft3/s");

    /** FlowVolume.CUBIC_FEET_PER_MINUTE unit type with code 7. */
    public static final DisplayType FLOWVOLUME_CUBIC_FEET_PER_MINUTE = new DisplayType(SerializationUnits.FLOWVOLUME, 7,
            FlowVolumeUnit.CUBIC_FEET_PER_MINUTE, "CUBIC_FEET_PER_MINUTE", "ft3/min");

    /** FlowVolume.GALLON_PER_SECOND unit type with code 8. */
    public static final DisplayType FLOWVOLUME_GALLON_PER_SECOND =
            new DisplayType(SerializationUnits.FLOWVOLUME, 8, FlowVolumeUnit.GALLON_PER_SECOND, "GALLON_PER_SECOND", "gal/s");

    /** FlowVolume.GALLON_PER_MINUTE unit type with code 9. */
    public static final DisplayType FLOWVOLUME_GALLON_PER_MINUTE = new DisplayType(SerializationUnits.FLOWVOLUME, 9,
            FlowVolumeUnit.GALLON_PER_MINUTE, "GALLON_PER_MINUTE", "gal/min");

    /** FlowVolume.GALLON_PER_HOUR unit type with code 10. */
    public static final DisplayType FLOWVOLUME_GALLON_PER_HOUR =
            new DisplayType(SerializationUnits.FLOWVOLUME, 10, FlowVolumeUnit.GALLON_PER_HOUR, "GALLON_PER_HOUR", "gal/h");

    /** FlowVolume.GALLON_PER_DAY unit type with code 11. */
    public static final DisplayType FLOWVOLUME_GALLON_PER_DAY =
            new DisplayType(SerializationUnits.FLOWVOLUME, 11, FlowVolumeUnit.GALLON_PER_DAY, "GALLON_PER_DAY", "gal/day");

    /** FlowVolume.LITER_PER_SECOND unit type with code 12. */
    public static final DisplayType FLOWVOLUME_LITER_PER_SECOND =
            new DisplayType(SerializationUnits.FLOWVOLUME, 12, FlowVolumeUnit.LITER_PER_SECOND, "LITER_PER_SECOND", "l/s");

    /** FlowVolume.LITER_PER_MINUTE unit type with code 13. */
    public static final DisplayType FLOWVOLUME_LITER_PER_MINUTE =
            new DisplayType(SerializationUnits.FLOWVOLUME, 13, FlowVolumeUnit.LITER_PER_MINUTE, "LITER_PER_MINUTE", "l/min");

    /** FlowVolume.LITER_PER_HOUR unit type with code 14. */
    public static final DisplayType FLOWVOLUME_LITER_PER_HOUR =
            new DisplayType(SerializationUnits.FLOWVOLUME, 14, FlowVolumeUnit.LITER_PER_HOUR, "LITER_PER_HOUR", "l/h");

    /** FlowVolume.LITER_PER_DAY unit type with code 15. */
    public static final DisplayType FLOWVOLUME_LITER_PER_DAY =
            new DisplayType(SerializationUnits.FLOWVOLUME, 15, FlowVolumeUnit.LITER_PER_DAY, "LITER_PER_DAY", "l/day");

    /* ===================================================== FORCE ======================================================= */

    /** Force.NEWTON unit type with code 0. */
    public static final DisplayType FORCE_NEWTON =
            new DisplayType(SerializationUnits.FORCE, 0, ForceUnit.NEWTON, "NEWTON", "N");

    /** Force.KILOGRAM_FORCE unit type with code 1. */
    public static final DisplayType FORCE_KILOGRAM_FORCE =
            new DisplayType(SerializationUnits.FORCE, 1, ForceUnit.KILOGRAM_FORCE, "KILOGRAM_FORCE", "kgf");

    /** Force.OUNCE_FORCE unit type with code 2. */
    public static final DisplayType FORCE_OUNCE_FORCE =
            new DisplayType(SerializationUnits.FORCE, 2, ForceUnit.OUNCE_FORCE, "OUNCE_FORCE", "ozf");

    /** Force.POUND_FORCE unit type with code 3. */
    public static final DisplayType FORCE_POUND_FORCE =
            new DisplayType(SerializationUnits.FORCE, 3, ForceUnit.POUND_FORCE, "POUND_FORCE", "lbf");

    /** Force.TON_FORCE unit type with code 4. */
    public static final DisplayType FORCE_TON_FORCE =
            new DisplayType(SerializationUnits.FORCE, 4, ForceUnit.TON_FORCE, "TON_FORCE", "tnf");

    /** Force.DYNE unit type with code 5. */
    public static final DisplayType FORCE_DYNE =
            new DisplayType(SerializationUnits.FORCE, 5, ForceUnit.DYNE, "DYNE", "dyne");

    /** Force.STHENE unit type with code 6. */
    public static final DisplayType FORCE_STHENE =
            new DisplayType(SerializationUnits.FORCE, 6, ForceUnit.STHENE, "STHENE", "sth");

    /* =================================================== FREQUENCY ===================================================== */

    /** Frequency.HERTZ unit type with code 0. */
    public static final DisplayType FREQUENCY_HERTZ =
            new DisplayType(SerializationUnits.FREQUENCY, 0, FrequencyUnit.HERTZ, "HERTZ", "Hz");

    /** Frequency.KILOHERTZ unit type with code 1. */
    public static final DisplayType FREQUENCY_KILOHERTZ =
            new DisplayType(SerializationUnits.FREQUENCY, 1, FrequencyUnit.KILOHERTZ, "KILOHERTZ", "kHz");

    /** Frequency.MEGAHERTZ unit type with code 2. */
    public static final DisplayType FREQUENCY_MEGAHERTZ =
            new DisplayType(SerializationUnits.FREQUENCY, 2, FrequencyUnit.MEGAHERTZ, "MEGAHERTZ", "MHz");

    /** Frequency.GIGAHERTZ unit type with code 3. */
    public static final DisplayType FREQUENCY_GIGAHERTZ =
            new DisplayType(SerializationUnits.FREQUENCY, 3, FrequencyUnit.GIGAHERTZ, "GIGAHERTZ", "GHz");

    /** Frequency.TERAHERTZ unit type with code 4. */
    public static final DisplayType FREQUENCY_TERAHERTZ =
            new DisplayType(SerializationUnits.FREQUENCY, 4, FrequencyUnit.TERAHERTZ, "TERAHERTZ", "THz");

    /** Frequency.PER_SECOND unit type with code 5. */
    public static final DisplayType FREQUENCY_PER_SECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 5, FrequencyUnit.PER_SECOND, "PER_SECOND", "1/s");

    /** Frequency.PER_ATTOSECOND unit type with code 6. */
    public static final DisplayType FREQUENCY_PER_ATTOSECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 6, FrequencyUnit.PER_ATTOSECOND, "PER_ATTOSECOND", "1/as");

    /** Frequency.PER_FEMTOSECOND unit type with code 7. */
    public static final DisplayType FREQUENCY_PER_FEMTOSECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 7, FrequencyUnit.PER_FEMTOSECOND, "PER_FEMTOSECOND", "1/fs");

    /** Frequency.PER_PICOSECOND unit type with code 8. */
    public static final DisplayType FREQUENCY_PER_PICOSECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 8, FrequencyUnit.PER_PICOSECOND, "PER_PICOSECOND", "1/ps");

    /** Frequency.PER_NANOSECOND unit type with code 9. */
    public static final DisplayType FREQUENCY_PER_NANOSECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 9, FrequencyUnit.PER_NANOSECOND, "PER_NANOSECOND", "1/ns");

    /** Frequency.PER_MICROSECOND unit type with code 10. */
    public static final DisplayType FREQUENCY_PER_MICROSECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 10, FrequencyUnit.PER_MICROSECOND, "PER_MICROSECOND", "1/μs");

    /** Frequency.PER_MILLISECOND unit type with code 11. */
    public static final DisplayType FREQUENCY_PER_MILLISECOND =
            new DisplayType(SerializationUnits.FREQUENCY, 11, FrequencyUnit.PER_MILLISECOND, "PER_MILLISECOND", "1/ms");

    /** Frequency.PER_MINUTE unit type with code 12. */
    public static final DisplayType FREQUENCY_PER_MINUTE =
            new DisplayType(SerializationUnits.FREQUENCY, 12, FrequencyUnit.PER_MINUTE, "PER_MINUTE", "1/min");

    /** Frequency.PER_HOUR unit type with code 13. */
    public static final DisplayType FREQUENCY_PER_HOUR =
            new DisplayType(SerializationUnits.FREQUENCY, 13, FrequencyUnit.PER_HOUR, "PER_HOUR", "1/hr");

    /** Frequency.PER_DAY unit type with code 14. */
    public static final DisplayType FREQUENCY_PER_DAY =
            new DisplayType(SerializationUnits.FREQUENCY, 14, FrequencyUnit.PER_DAY, "PER_DAY", "1/day");

    /** Frequency.PER_WEEK unit type with code 15. */
    public static final DisplayType FREQUENCY_PER_WEEK =
            new DisplayType(SerializationUnits.FREQUENCY, 15, FrequencyUnit.PER_WEEK, "PER_WEEK", "1/wk");

    /** Frequency.RPM unit type with code 16. */
    public static final DisplayType FREQUENCY_RPM =
            new DisplayType(SerializationUnits.FREQUENCY, 16, FrequencyUnit.RPM, "RPM", "rpm");

    /* ===================================================== LENGTH ====================================================== */

    /** Length.METER unit type with code 0. */
    public static final DisplayType LENGTH_METER =
            new DisplayType(SerializationUnits.LENGTH, 0, LengthUnit.METER, "METER", "m");

    /** Length.ATTOMETER unit type with code 1. */
    public static final DisplayType LENGTH_ATTOMETER =
            new DisplayType(SerializationUnits.LENGTH, 1, LengthUnit.ATTOMETER, "ATTOMETER", "am");

    /** Length.FEMTOMETER unit type with code 2. */
    public static final DisplayType LENGTH_FEMTOMETER =
            new DisplayType(SerializationUnits.LENGTH, 2, LengthUnit.FEMTOMETER, "FEMTOMETER", "fm");

    /** Length.PICOMETER unit type with code 3. */
    public static final DisplayType LENGTH_PICOMETER =
            new DisplayType(SerializationUnits.LENGTH, 3, LengthUnit.PICOMETER, "PICOMETER", "pm");

    /** Length.NANOMETER unit type with code 4. */
    public static final DisplayType LENGTH_NANOMETER =
            new DisplayType(SerializationUnits.LENGTH, 4, LengthUnit.NANOMETER, "NANOMETER", "nm");

    /** Length.MICROMETER unit type with code 5. */
    public static final DisplayType LENGTH_MICROMETER =
            new DisplayType(SerializationUnits.LENGTH, 5, LengthUnit.MICROMETER, "MICROMETER", "μm");

    /** Length.MILLIMETER unit type with code 6. */
    public static final DisplayType LENGTH_MILLIMETER =
            new DisplayType(SerializationUnits.LENGTH, 6, LengthUnit.MILLIMETER, "MILLIMETER", "mm");

    /** Length.CENTIMETER unit type with code 7. */
    public static final DisplayType LENGTH_CENTIMETER =
            new DisplayType(SerializationUnits.LENGTH, 7, LengthUnit.CENTIMETER, "CENTIMETER", "cm");

    /** Length.DECIMETER unit type with code 8. */
    public static final DisplayType LENGTH_DECIMETER =
            new DisplayType(SerializationUnits.LENGTH, 8, LengthUnit.DECIMETER, "DECIMETER", "dm");

    /** Length.DEKAMETER unit type with code 9. */
    public static final DisplayType LENGTH_DEKAMETER =
            new DisplayType(SerializationUnits.LENGTH, 9, LengthUnit.DEKAMETER, "DEKAMETER", "dam");

    /** Length.HECTOMETER unit type with code 10. */
    public static final DisplayType LENGTH_HECTOMETER =
            new DisplayType(SerializationUnits.LENGTH, 10, LengthUnit.HECTOMETER, "HECTOMETER", "hm");

    /** Length.KILOMETER unit type with code 11. */
    public static final DisplayType LENGTH_KILOMETER =
            new DisplayType(SerializationUnits.LENGTH, 11, LengthUnit.KILOMETER, "KILOMETER", "km");

    /** Length.MEGAMETER unit type with code 12. */
    public static final DisplayType LENGTH_MEGAMETER =
            new DisplayType(SerializationUnits.LENGTH, 12, LengthUnit.MEGAMETER, "MEGAMETER", "Mm");

    /** Length.INCH unit type with code 13. */
    public static final DisplayType LENGTH_INCH =
            new DisplayType(SerializationUnits.LENGTH, 13, LengthUnit.INCH, "INCH", "in");

    /** Length.FOOT unit type with code 14. */
    public static final DisplayType LENGTH_FOOT =
            new DisplayType(SerializationUnits.LENGTH, 14, LengthUnit.FOOT, "FOOT", "ft");

    /** Length.YARD unit type with code 15. */
    public static final DisplayType LENGTH_YARD =
            new DisplayType(SerializationUnits.LENGTH, 15, LengthUnit.YARD, "YARD", "yd");

    /** Length.MILE unit type with code 16. */
    public static final DisplayType LENGTH_MILE =
            new DisplayType(SerializationUnits.LENGTH, 16, LengthUnit.MILE, "MILE", "mi");

    /** Length.NAUTICAL_MILE unit type with code 17. */
    public static final DisplayType LENGTH_NAUTICAL_MILE =
            new DisplayType(SerializationUnits.LENGTH, 17, LengthUnit.NAUTICAL_MILE, "NAUTICAL_MILE", "NM");

    /** Length.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final DisplayType LENGTH_ASTRONOMICAL_UNIT =
            new DisplayType(SerializationUnits.LENGTH, 18, LengthUnit.ASTRONOMICAL_UNIT, "ASTRONOMICAL_UNIT", "au");

    /** Length.PARSEC unit type with code 19. */
    public static final DisplayType LENGTH_PARSEC =
            new DisplayType(SerializationUnits.LENGTH, 19, LengthUnit.PARSEC, "PARSEC", "pc");

    /** Length.LIGHTYEAR unit type with code 20. */
    public static final DisplayType LENGTH_LIGHTYEAR =
            new DisplayType(SerializationUnits.LENGTH, 20, LengthUnit.LIGHTYEAR, "LIGHTYEAR", "ly");

    /** Length.ANGSTROM unit type with code 21. */
    public static final DisplayType LENGTH_ANGSTROM =
            new DisplayType(SerializationUnits.LENGTH, 21, LengthUnit.ANGSTROM, "ANGSTROM", "Å");

    /* ==================================================== POSITION ===================================================== */

    /** Position.METER unit type with code 0. */
    public static final DisplayType POSITION_METER =
            new DisplayType(SerializationUnits.POSITION, 0, PositionUnit.METER, "METER", "m");

    /** Position.ATTOMETER unit type with code 1. */
    public static final DisplayType POSITION_ATTOMETER =
            new DisplayType(SerializationUnits.POSITION, 1, PositionUnit.ATTOMETER, "ATTOMETER", "am");

    /** Position.FEMTOMETER unit type with code 2. */
    public static final DisplayType POSITION_FEMTOMETER =
            new DisplayType(SerializationUnits.POSITION, 2, PositionUnit.FEMTOMETER, "FEMTOMETER", "fm");

    /** Position.PICOMETER unit type with code 3. */
    public static final DisplayType POSITION_PICOMETER =
            new DisplayType(SerializationUnits.POSITION, 3, PositionUnit.PICOMETER, "PICOMETER", "pm");

    /** Position.NANOMETER unit type with code 4. */
    public static final DisplayType POSITION_NANOMETER =
            new DisplayType(SerializationUnits.POSITION, 4, PositionUnit.NANOMETER, "NANOMETER", "nm");

    /** Position.MICROMETER unit type with code 5. */
    public static final DisplayType POSITION_MICROMETER =
            new DisplayType(SerializationUnits.POSITION, 5, PositionUnit.MICROMETER, "MICROMETER", "μm");

    /** Position.MILLIMETER unit type with code 6. */
    public static final DisplayType POSITION_MILLIMETER =
            new DisplayType(SerializationUnits.POSITION, 6, PositionUnit.MILLIMETER, "MILLIMETER", "mm");

    /** Position.CENTIMETER unit type with code 7. */
    public static final DisplayType POSITION_CENTIMETER =
            new DisplayType(SerializationUnits.POSITION, 7, PositionUnit.CENTIMETER, "CENTIMETER", "cm");

    /** Position.DECIMETER unit type with code 8. */
    public static final DisplayType POSITION_DECIMETER =
            new DisplayType(SerializationUnits.POSITION, 8, PositionUnit.DECIMETER, "DECIMETER", "dm");

    /** Position.DEKAMETER unit type with code 9. */
    public static final DisplayType POSITION_DEKAMETER =
            new DisplayType(SerializationUnits.POSITION, 9, PositionUnit.DEKAMETER, "DEKAMETER", "dam");

    /** Position.HECTOMETER unit type with code 10. */
    public static final DisplayType POSITION_HECTOMETER =
            new DisplayType(SerializationUnits.POSITION, 10, PositionUnit.HECTOMETER, "HECTOMETER", "hm");

    /** Position.KILOMETER unit type with code 11. */
    public static final DisplayType POSITION_KILOMETER =
            new DisplayType(SerializationUnits.POSITION, 11, PositionUnit.KILOMETER, "KILOMETER", "km");

    /** Position.MEGAMETER unit type with code 12. */
    public static final DisplayType POSITION_MEGAMETER =
            new DisplayType(SerializationUnits.POSITION, 12, PositionUnit.MEGAMETER, "MEGAMETER", "Mm");

    /** Position.INCH unit type with code 13. */
    public static final DisplayType POSITION_INCH =
            new DisplayType(SerializationUnits.POSITION, 13, PositionUnit.INCH, "INCH", "in");

    /** Position.FOOT unit type with code 14. */
    public static final DisplayType POSITION_FOOT =
            new DisplayType(SerializationUnits.POSITION, 14, PositionUnit.FOOT, "FOOT", "ft");

    /** Position.YARD unit type with code 15. */
    public static final DisplayType POSITION_YARD =
            new DisplayType(SerializationUnits.POSITION, 15, PositionUnit.YARD, "YARD", "yd");

    /** Position.MILE unit type with code 16. */
    public static final DisplayType POSITION_MILE =
            new DisplayType(SerializationUnits.POSITION, 16, PositionUnit.MILE, "MILE", "mi");

    /** Position.NAUTICAL_MILE unit type with code 17. */
    public static final DisplayType POSITION_NAUTICAL_MILE =
            new DisplayType(SerializationUnits.POSITION, 17, PositionUnit.NAUTICAL_MILE, "NAUTICAL_MILE", "NM");

    /** Position.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final DisplayType POSITION_ASTRONOMICAL_UNIT =
            new DisplayType(SerializationUnits.POSITION, 18, PositionUnit.ASTRONOMICAL_UNIT, "ASTRONOMICAL_UNIT", "au");

    /** Position.PARSEC unit type with code 19. */
    public static final DisplayType POSITION_PARSEC =
            new DisplayType(SerializationUnits.POSITION, 19, PositionUnit.PARSEC, "PARSEC", "pc");

    /** Position.LIGHTYEAR unit type with code 20. */
    public static final DisplayType POSITION_LIGHTYEAR =
            new DisplayType(SerializationUnits.POSITION, 20, PositionUnit.LIGHTYEAR, "LIGHTYEAR", "ly");

    /** Position.ANGSTROM unit type with code 21. */
    public static final DisplayType POSITION_ANGSTROM =
            new DisplayType(SerializationUnits.POSITION, 21, PositionUnit.ANGSTROM, "ANGSTROM", "Å");

    /* ================================================== LINEARDENSITY ================================================== */

    /** LinearDensity.PER_METER unit type with code 0. */
    public static final DisplayType LINEARDENSITY_PER_METER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 0, LinearDensityUnit.PER_METER, "PER_METER", "1/m");

    /** LinearDensity.PER_ATTOMETER unit type with code 1. */
    public static final DisplayType LINEARDENSITY_PER_ATTOMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 1, LinearDensityUnit.PER_ATTOMETER, "PER_ATTOMETER", "1/am");

    /** LinearDensity.PER_FEMTOMETER unit type with code 2. */
    public static final DisplayType LINEARDENSITY_PER_FEMTOMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 2, LinearDensityUnit.PER_FEMTOMETER, "PER_FEMTOMETER", "1/fm");

    /** LinearDensity.PER_PICOMETER unit type with code 3. */
    public static final DisplayType LINEARDENSITY_PER_PICOMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 3, LinearDensityUnit.PER_PICOMETER, "PER_PICOMETER", "1/pm");

    /** LinearDensity.PER_NANOMETER unit type with code 4. */
    public static final DisplayType LINEARDENSITY_PER_NANOMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 4, LinearDensityUnit.PER_NANOMETER, "PER_NANOMETER", "1/nm");

    /** LinearDensity.PER_MICROMETER unit type with code 5. */
    public static final DisplayType LINEARDENSITY_PER_MICROMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 5, LinearDensityUnit.PER_MICROMETER, "PER_MICROMETER", "1/μm");

    /** LinearDensity.PER_MILLIMETER unit type with code 6. */
    public static final DisplayType LINEARDENSITY_PER_MILLIMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 6, LinearDensityUnit.PER_MILLIMETER, "PER_MILLIMETER", "1/mm");

    /** LinearDensity.PER_CENTIMETER unit type with code 7. */
    public static final DisplayType LINEARDENSITY_PER_CENTIMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 7, LinearDensityUnit.PER_CENTIMETER, "PER_CENTIMETER", "1/cm");

    /** LinearDensity.PER_DECIMETER unit type with code 8. */
    public static final DisplayType LINEARDENSITY_PER_DECIMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 8, LinearDensityUnit.PER_DECIMETER, "PER_DECIMETER", "1/dm");

    /** LinearDensity.PER_DEKAMETER unit type with code 9. */
    public static final DisplayType LINEARDENSITY_PER_DEKAMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 9, LinearDensityUnit.PER_DEKAMETER, "PER_DEKAMETER", "1/dam");

    /** LinearDensity.PER_HECTOMETER unit type with code 10. */
    public static final DisplayType LINEARDENSITY_PER_HECTOMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 10, LinearDensityUnit.PER_HECTOMETER, "PER_HECTOMETER", "1/hm");

    /** LinearDensity.PER_KILOMETER unit type with code 11. */
    public static final DisplayType LINEARDENSITY_PER_KILOMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 11, LinearDensityUnit.PER_KILOMETER, "PER_KILOMETER", "1/km");

    /** LinearDensity.PER_MEGAMETER unit type with code 12. */
    public static final DisplayType LINEARDENSITY_PER_MEGAMETER =
            new DisplayType(SerializationUnits.LINEARDENSITY, 12, LinearDensityUnit.PER_MEGAMETER, "PER_MEGAMETER", "1/Mm");

    /** LinearDensity.PER_INCH unit type with code 13. */
    public static final DisplayType LINEARDENSITY_PER_INCH =
            new DisplayType(SerializationUnits.LINEARDENSITY, 13, LinearDensityUnit.PER_INCH, "PER_INCH", "1/in");

    /** LinearDensity.PER_FOOT unit type with code 14. */
    public static final DisplayType LINEARDENSITY_PER_FOOT =
            new DisplayType(SerializationUnits.LINEARDENSITY, 14, LinearDensityUnit.PER_FOOT, "PER_FOOT", "1/ft");

    /** LinearDensity.PER_YARD unit type with code 15. */
    public static final DisplayType LINEARDENSITY_PER_YARD =
            new DisplayType(SerializationUnits.LINEARDENSITY, 15, LinearDensityUnit.PER_YARD, "PER_YARD", "1/yd");

    /** LinearDensity.PER_MILE unit type with code 16. */
    public static final DisplayType LINEARDENSITY_PER_MILE =
            new DisplayType(SerializationUnits.LINEARDENSITY, 16, LinearDensityUnit.PER_MILE, "PER_MILE", "1/mi");

    /** LinearDensity.PER_NAUTICAL_MILE unit type with code 17. */
    public static final DisplayType LINEARDENSITY_PER_NAUTICAL_MILE = new DisplayType(SerializationUnits.LINEARDENSITY,
            17, LinearDensityUnit.PER_NAUTICAL_MILE, "PER_NAUTICAL_MILE", "1/NM");

    /** LinearDensity.PER_ASTRONOMICAL_UNIT unit type with code 18. */
    public static final DisplayType LINEARDENSITY_PER_ASTRONOMICAL_UNIT = new DisplayType(
            SerializationUnits.LINEARDENSITY, 18, LinearDensityUnit.PER_ASTRONOMICAL_UNIT, "PER_ASTRONOMICAL_UNIT", "1/au");

    /** LinearDensity.PER_PARSEC unit type with code 19. */
    public static final DisplayType LINEARDENSITY_PER_PARSEC =
            new DisplayType(SerializationUnits.LINEARDENSITY, 19, LinearDensityUnit.PER_PARSEC, "PER_PARSEC", "1/pc");

    /** LinearDensity.PER_LIGHTYEAR unit type with code 20. */
    public static final DisplayType LINEARDENSITY_PER_LIGHTYEAR =
            new DisplayType(SerializationUnits.LINEARDENSITY, 20, LinearDensityUnit.PER_LIGHTYEAR, "PER_LIGHTYEAR", "1/ly");

    /** LinearDensity.PER_ANGSTROM unit type with code 21. */
    public static final DisplayType LINEARDENSITY_PER_ANGSTROM =
            new DisplayType(SerializationUnits.LINEARDENSITY, 21, LinearDensityUnit.PER_ANGSTROM, "PER_ANGSTROM", "1/Å");

    /* ====================================================== MASS ======================================================= */

    /** Mass.KILOGRAM unit type with code 0. */
    public static final DisplayType MASS_KILOGRAM =
            new DisplayType(SerializationUnits.MASS, 0, MassUnit.KILOGRAM, "KILOGRAM", "kg");

    /** Mass.FEMTOGRAM unit type with code 1. */
    public static final DisplayType MASS_FEMTOGRAM =
            new DisplayType(SerializationUnits.MASS, 1, MassUnit.FEMTOGRAM, "FEMTOGRAM", "fg");

    /** Mass.PICOGRAM unit type with code 2. */
    public static final DisplayType MASS_PICOGRAM =
            new DisplayType(SerializationUnits.MASS, 2, MassUnit.PICOGRAM, "PICOGRAM", "pg");

    /** Mass.NANOGRAM unit type with code 3. */
    public static final DisplayType MASS_NANOGRAM =
            new DisplayType(SerializationUnits.MASS, 3, MassUnit.NANOGRAM, "NANOGRAM", "mg");

    /** Mass.MICROGRAM unit type with code 4. */
    public static final DisplayType MASS_MICROGRAM =
            new DisplayType(SerializationUnits.MASS, 4, MassUnit.MICROGRAM, "MICROGRAM", "μg");

    /** Mass.MILLIGRAM unit type with code 5. */
    public static final DisplayType MASS_MILLIGRAM =
            new DisplayType(SerializationUnits.MASS, 5, MassUnit.MILLIGRAM, "MILLIGRAM", "mg");

    /** Mass.GRAM unit type with code 6. */
    public static final DisplayType MASS_GRAM =
            new DisplayType(SerializationUnits.MASS, 6, MassUnit.GRAM, "GRAM", "kg");

    /** Mass.MEGAGRAM unit type with code 7. */
    public static final DisplayType MASS_MEGAGRAM =
            new DisplayType(SerializationUnits.MASS, 7, MassUnit.MEGAGRAM, "MEGAGRAM", "Mg");

    /** Mass.GIGAGRAM unit type with code 8. */
    public static final DisplayType MASS_GIGAGRAM =
            new DisplayType(SerializationUnits.MASS, 8, MassUnit.GIGAGRAM, "GIGAGRAM", "Gg");

    /** Mass.TERAGRAM unit type with code 9. */
    public static final DisplayType MASS_TERAGRAM =
            new DisplayType(SerializationUnits.MASS, 9, MassUnit.TERAGRAM, "TERAGRAM", "Tg");

    /** Mass.PETAGRAM unit type with code 10. */
    public static final DisplayType MASS_PETAGRAM =
            new DisplayType(SerializationUnits.MASS, 10, MassUnit.PETAGRAM, "PETAGRAM", "Pg");

    /** Mass.MICROELECTRONVOLT unit type with code 11. */
    public static final DisplayType MASS_MICROELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 11, MassUnit.MICROELECTRONVOLT, "MICROELECTRONVOLT", "μeV");

    /** Mass.MILLIELECTRONVOLT unit type with code 12. */
    public static final DisplayType MASS_MILLIELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 12, MassUnit.MILLIELECTRONVOLT, "MILLIELECTRONVOLT", "meV");

    /** Mass.ELECTRONVOLT unit type with code 13. */
    public static final DisplayType MASS_ELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 13, MassUnit.ELECTRONVOLT, "ELECTRONVOLT", "eV");

    /** Mass.KILOELECTRONVOLT unit type with code 14. */
    public static final DisplayType MASS_KILOELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 14, MassUnit.KILOELECTRONVOLT, "KILOELECTRONVOLT", "keV");

    /** Mass.MEGAELECTRONVOLT unit type with code 15. */
    public static final DisplayType MASS_MEGAELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 15, MassUnit.MEGAELECTRONVOLT, "MEGAELECTRONVOLT", "MeV");

    /** Mass.GIGAELECTRONVOLT unit type with code 16. */
    public static final DisplayType MASS_GIGAELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 16, MassUnit.GIGAELECTRONVOLT, "GIGAELECTRONVOLT", "GeV");

    /** Mass.TERAELECTRONVOLT unit type with code 17. */
    public static final DisplayType MASS_TERAELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 17, MassUnit.TERAELECTRONVOLT, "TERAELECTRONVOLT", "TeV");

    /** Mass.PETAELECTRONVOLT unit type with code 18. */
    public static final DisplayType MASS_PETAELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 18, MassUnit.PETAELECTRONVOLT, "PETAELECTRONVOLT", "PeV");

    /** Mass.EXAELECTRONVOLT unit type with code 19. */
    public static final DisplayType MASS_EXAELECTRONVOLT =
            new DisplayType(SerializationUnits.MASS, 19, MassUnit.EXAELECTRONVOLT, "EXAELECTRONVOLT", "EeV");

    /** Mass.OUNCE unit type with code 20. */
    public static final DisplayType MASS_OUNCE =
            new DisplayType(SerializationUnits.MASS, 20, MassUnit.OUNCE, "OUNCE", "oz");

    /** Mass.POUND unit type with code 21. */
    public static final DisplayType MASS_POUND =
            new DisplayType(SerializationUnits.MASS, 21, MassUnit.POUND, "POUND", "lb");

    /** Mass.DALTON unit type with code 22. */
    public static final DisplayType MASS_DALTON =
            new DisplayType(SerializationUnits.MASS, 22, MassUnit.DALTON, "DALTON", "Da");

    /** Mass.TON_LONG unit type with code 23. */
    public static final DisplayType MASS_TON_LONG =
            new DisplayType(SerializationUnits.MASS, 23, MassUnit.TON_LONG, "TON_LONG", "ton (long)");

    /** Mass.TON_SHORT unit type with code 24. */
    public static final DisplayType MASS_TON_SHORT =
            new DisplayType(SerializationUnits.MASS, 24, MassUnit.TON_SHORT, "TON_SHORT", "ton (short)");

    /** Mass.TONNE unit type with code 25. */
    public static final DisplayType MASS_TONNE =
            new DisplayType(SerializationUnits.MASS, 25, MassUnit.TONNE, "TONNE", "tonne");

    /* ===================================================== POWER ======================================================= */

    /** Power.WATT unit type with code 0. */
    public static final DisplayType POWER_WATT =
            new DisplayType(SerializationUnits.POWER, 0, PowerUnit.WATT, "WATT", "W");

    /** Power.FEMTOWATT unit type with code 1. */
    public static final DisplayType POWER_FEMTOWATT =
            new DisplayType(SerializationUnits.POWER, 1, PowerUnit.FEMTOWATT, "FEMTOWATT", "fW");

    /** Power.PICOWATT unit type with code 2. */
    public static final DisplayType POWER_PICOWATT =
            new DisplayType(SerializationUnits.POWER, 2, PowerUnit.PICOWATT, "PICOWATT", "pW");

    /** Power.NANOWATT unit type with code 3. */
    public static final DisplayType POWER_NANOWATT =
            new DisplayType(SerializationUnits.POWER, 3, PowerUnit.NANOWATT, "NANOWATT", "mW");

    /** Power.MICROWATT unit type with code 4. */
    public static final DisplayType POWER_MICROWATT =
            new DisplayType(SerializationUnits.POWER, 4, PowerUnit.MICROWATT, "MICROWATT", "μW");

    /** Power.MILLIWATT unit type with code 5. */
    public static final DisplayType POWER_MILLIWATT =
            new DisplayType(SerializationUnits.POWER, 5, PowerUnit.MILLIWATT, "MILLIWATT", "mW");

    /** Power.KILOWATT unit type with code 6. */
    public static final DisplayType POWER_KILOWATT =
            new DisplayType(SerializationUnits.POWER, 6, PowerUnit.KILOWATT, "KILOWATT", "kW");

    /** Power.MEGAWATT unit type with code 7. */
    public static final DisplayType POWER_MEGAWATT =
            new DisplayType(SerializationUnits.POWER, 7, PowerUnit.MEGAWATT, "MEGAWATT", "MW");

    /** Power.GIGAWATT unit type with code 8. */
    public static final DisplayType POWER_GIGAWATT =
            new DisplayType(SerializationUnits.POWER, 8, PowerUnit.GIGAWATT, "GIGAWATT", "GW");

    /** Power.TERAWATT unit type with code 9. */
    public static final DisplayType POWER_TERAWATT =
            new DisplayType(SerializationUnits.POWER, 9, PowerUnit.TERAWATT, "TERAWATT", "TW");

    /** Power.PETAWATT unit type with code 10. */
    public static final DisplayType POWER_PETAWATT =
            new DisplayType(SerializationUnits.POWER, 10, PowerUnit.PETAWATT, "PETAWATT", "PW");

    /** Power.ERG_PER_SECOND unit type with code 11. */
    public static final DisplayType POWER_ERG_PER_SECOND =
            new DisplayType(SerializationUnits.POWER, 11, PowerUnit.ERG_PER_SECOND, "ERG_PER_SECOND", "erg/s");

    /** Power.FOOT_POUND_FORCE_PER_SECOND unit type with code 12. */
    public static final DisplayType POWER_FOOT_POUND_FORCE_PER_SECOND = new DisplayType(SerializationUnits.POWER, 12,
            PowerUnit.FOOT_POUND_FORCE_PER_SECOND, "FOOT_POUND_FORCE_PER_SECOND", "ft.lbf/s");

    /** Power.FOOT_POUND_FORCE_PER_MINUTE unit type with code 13. */
    public static final DisplayType POWER_FOOT_POUND_FORCE_PER_MINUTE = new DisplayType(SerializationUnits.POWER, 13,
            PowerUnit.FOOT_POUND_FORCE_PER_MINUTE, "FOOT_POUND_FORCE_PER_MINUTE", "ft.lbf/min");

    /** Power.FOOT_POUND_FORCE_PER_HOUR unit type with code 14. */
    public static final DisplayType POWER_FOOT_POUND_FORCE_PER_HOUR = new DisplayType(SerializationUnits.POWER, 14,
            PowerUnit.FOOT_POUND_FORCE_PER_HOUR, "FOOT_POUND_FORCE_PER_HOUR", "ft.lbf/h");

    /** Power.HORSEPOWER_METRIC unit type with code 15. */
    public static final DisplayType POWER_HORSEPOWER_METRIC =
            new DisplayType(SerializationUnits.POWER, 15, PowerUnit.HORSEPOWER_METRIC, "HORSEPOWER_METRIC", "hp");

    /** Power.STHENE_METER_PER_SECOND unit type with code 16. */
    public static final DisplayType POWER_STHENE_METER_PER_SECOND = new DisplayType(SerializationUnits.POWER, 16,
            PowerUnit.STHENE_METER_PER_SECOND, "STHENE_METER_PER_SECOND", "sth/s");

    /* ===================================================== PRESSURE ==================================================== */

    /** Pressure.PASCAL unit type with code 0. */
    public static final DisplayType PRESSURE_PASCAL =
            new DisplayType(SerializationUnits.PRESSURE, 0, PressureUnit.PASCAL, "PASCAL", "Pa");

    /** Pressure.HECTOPASCAL unit type with code 1. */
    public static final DisplayType PRESSURE_HECTOPASCAL =
            new DisplayType(SerializationUnits.PRESSURE, 1, PressureUnit.HECTOPASCAL, "HECTOPASCAL", "hPa");

    /** Pressure.KILOPASCAL unit type with code 2. */
    public static final DisplayType PRESSURE_KILOPASCAL =
            new DisplayType(SerializationUnits.PRESSURE, 2, PressureUnit.KILOPASCAL, "KILOPASCAL", "kPa");

    /** Pressure.ATMOSPHERE_STANDARD unit type with code 3. */
    public static final DisplayType PRESSURE_ATMOSPHERE_STANDARD =
            new DisplayType(SerializationUnits.PRESSURE, 3, PressureUnit.ATMOSPHERE_STANDARD, "ATMOSPHERE_STANDARD", "atm");

    /** Pressure.ATMOSPHERE_TECHNICAL unit type with code 4. */
    public static final DisplayType PRESSURE_ATMOSPHERE_TECHNICAL =
            new DisplayType(SerializationUnits.PRESSURE, 4, PressureUnit.ATMOSPHERE_TECHNICAL, "ATMOSPHERE_TECHNICAL", "at");

    /** Pressure.MILLIBAR unit type with code 5. */
    public static final DisplayType PRESSURE_MILLIBAR =
            new DisplayType(SerializationUnits.PRESSURE, 5, PressureUnit.MILLIBAR, "MILLIBAR", "mbar");

    /** Pressure.BAR unit type with code 6. */
    public static final DisplayType PRESSURE_BAR =
            new DisplayType(SerializationUnits.PRESSURE, 6, PressureUnit.BAR, "BAR", "bar");

    /** Pressure.BARYE unit type with code 7. */
    public static final DisplayType PRESSURE_BARYE =
            new DisplayType(SerializationUnits.PRESSURE, 7, PressureUnit.BARYE, "BARYE", "Ba");

    /** Pressure.MILLIMETER_MERCURY unit type with code 8. */
    public static final DisplayType PRESSURE_MILLIMETER_MERCURY =
            new DisplayType(SerializationUnits.PRESSURE, 8, PressureUnit.MILLIMETER_MERCURY, "MILLIMETER_MERCURY", "mmHg");

    /** Pressure.CENTIMETER_MERCURY unit type with code 9. */
    public static final DisplayType PRESSURE_CENTIMETER_MERCURY =
            new DisplayType(SerializationUnits.PRESSURE, 9, PressureUnit.CENTIMETER_MERCURY, "CENTIMETER_MERCURY", "cmHg");

    /** Pressure.INCH_MERCURY unit type with code 10. */
    public static final DisplayType PRESSURE_INCH_MERCURY =
            new DisplayType(SerializationUnits.PRESSURE, 10, PressureUnit.INCH_MERCURY, "INCH_MERCURY", "inHg");

    /** Pressure.FOOT_MERCURY unit type with code 11. */
    public static final DisplayType PRESSURE_FOOT_MERCURY =
            new DisplayType(SerializationUnits.PRESSURE, 11, PressureUnit.FOOT_MERCURY, "FOOT_MERCURY", "ftHg");

    /** Pressure.KGF_PER_SQUARE_MM unit type with code 12. */
    public static final DisplayType PRESSURE_KGF_PER_SQUARE_MM =
            new DisplayType(SerializationUnits.PRESSURE, 12, PressureUnit.KGF_PER_SQUARE_MM, "KGF_PER_SQUARE_MM", "kgf/mm2");

    /** Pressure.PIEZE unit type with code 13. */
    public static final DisplayType PRESSURE_PIEZE =
            new DisplayType(SerializationUnits.PRESSURE, 13, PressureUnit.PIEZE, "PIEZE", "pz");

    /** Pressure.POUND_PER_SQUARE_INCH unit type with code 14. */
    public static final DisplayType PRESSURE_POUND_PER_SQUARE_INCH = new DisplayType(SerializationUnits.PRESSURE, 14,
            PressureUnit.POUND_PER_SQUARE_INCH, "POUND_PER_SQUARE_INCH", "lb/in2");

    /** Pressure.POUND_PER_SQUARE_FOOT unit type with code 15. */
    public static final DisplayType PRESSURE_POUND_PER_SQUARE_FOOT = new DisplayType(SerializationUnits.PRESSURE, 15,
            PressureUnit.POUND_PER_SQUARE_FOOT, "POUND_PER_SQUARE_FOOT", "lb/ft2");

    /** Pressure.TORR unit type with code 16. */
    public static final DisplayType PRESSURE_TORR =
            new DisplayType(SerializationUnits.PRESSURE, 16, PressureUnit.TORR, "TORR", "torr");

    /* ===================================================== SPEED ======================================================= */

    /** Speed.METER_PER_SECOND unit type with code 0. */
    public static final DisplayType SPEED_METER_PER_SECOND =
            new DisplayType(SerializationUnits.SPEED, 0, SpeedUnit.METER_PER_SECOND, "METER_PER_SECOND", "m/s");

    /** Speed.METER_PER_HOUR unit type with code 1. */
    public static final DisplayType SPEED_METER_PER_HOUR =
            new DisplayType(SerializationUnits.SPEED, 1, SpeedUnit.METER_PER_HOUR, "METER_PER_HOUR", "m/h");

    /** Speed.KM_PER_SECOND unit type with code 2. */
    public static final DisplayType SPEED_KM_PER_SECOND =
            new DisplayType(SerializationUnits.SPEED, 2, SpeedUnit.KM_PER_SECOND, "KM_PER_SECOND", "km/s");

    /** Speed.KM_PER_HOUR unit type with code 3. */
    public static final DisplayType SPEED_KM_PER_HOUR =
            new DisplayType(SerializationUnits.SPEED, 3, SpeedUnit.KM_PER_HOUR, "KM_PER_HOUR", "km/h");

    /** Speed.INCH_PER_SECOND unit type with code 4. */
    public static final DisplayType SPEED_INCH_PER_SECOND =
            new DisplayType(SerializationUnits.SPEED, 4, SpeedUnit.INCH_PER_SECOND, "INCH_PER_SECOND", "in/s");

    /** Speed.INCH_PER_MINUTE unit type with code 5. */
    public static final DisplayType SPEED_INCH_PER_MINUTE =
            new DisplayType(SerializationUnits.SPEED, 5, SpeedUnit.INCH_PER_MINUTE, "INCH_PER_MINUTE", "in/min");

    /** Speed.INCH_PER_HOUR unit type with code 6. */
    public static final DisplayType SPEED_INCH_PER_HOUR =
            new DisplayType(SerializationUnits.SPEED, 6, SpeedUnit.INCH_PER_HOUR, "INCH_PER_HOUR", "in/h");

    /** Speed.FOOT_PER_SECOND unit type with code 7. */
    public static final DisplayType SPEED_FOOT_PER_SECOND =
            new DisplayType(SerializationUnits.SPEED, 7, SpeedUnit.FOOT_PER_SECOND, "FOOT_PER_SECOND", "ft/s");

    /** Speed.FOOT_PER_MINUTE unit type with code 8. */
    public static final DisplayType SPEED_FOOT_PER_MINUTE =
            new DisplayType(SerializationUnits.SPEED, 8, SpeedUnit.FOOT_PER_MINUTE, "FOOT_PER_MINUTE", "ft/min");

    /** Speed.FOOT_PER_HOUR unit type with code 9. */
    public static final DisplayType SPEED_FOOT_PER_HOUR =
            new DisplayType(SerializationUnits.SPEED, 9, SpeedUnit.FOOT_PER_HOUR, "FOOT_PER_HOUR", "ft/h");

    /** Speed.MILE_PER_SECOND unit type with code 10. */
    public static final DisplayType SPEED_MILE_PER_SECOND =
            new DisplayType(SerializationUnits.SPEED, 10, SpeedUnit.MILE_PER_SECOND, "MILE_PER_SECOND", "mi/s");

    /** Speed.MILE_PER_MINUTE unit type with code 11. */
    public static final DisplayType SPEED_MILE_PER_MINUTE =
            new DisplayType(SerializationUnits.SPEED, 11, SpeedUnit.MILE_PER_MINUTE, "MILE_PER_MINUTE", "mi/min");

    /** Speed.MILE_PER_HOUR unit type with code 12. */
    public static final DisplayType SPEED_MILE_PER_HOUR =
            new DisplayType(SerializationUnits.SPEED, 12, SpeedUnit.MILE_PER_HOUR, "MILE_PER_HOUR", "mi/h");

    /** Speed.KNOT unit type with code 13. */
    public static final DisplayType SPEED_KNOT =
            new DisplayType(SerializationUnits.SPEED, 13, SpeedUnit.KNOT, "KNOT", "kt");

    /* =================================================== TEMPERATURE =================================================== */

    /** Temperature.KELVIN unit type with code 0. */
    public static final DisplayType TEMPERATURE_KELVIN =
            new DisplayType(SerializationUnits.TEMPERATURE, 0, TemperatureUnit.KELVIN, "KELVIN", "K");

    /** Temperature.DEGREE_CELSIUS unit type with code 1. */
    public static final DisplayType TEMPERATURE_DEGREE_CELSIUS =
            new DisplayType(SerializationUnits.TEMPERATURE, 1, TemperatureUnit.DEGREE_CELSIUS, "DEGREE_CELSIUS", "OC");

    /** Temperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final DisplayType TEMPERATURE_DEGREE_FAHRENHEIT =
            new DisplayType(SerializationUnits.TEMPERATURE, 2, TemperatureUnit.DEGREE_FAHRENHEIT, "DEGREE_FAHRENHEIT", "OF");

    /** Temperature.DEGREE_RANKINE unit type with code 3. */
    public static final DisplayType TEMPERATURE_DEGREE_RANKINE =
            new DisplayType(SerializationUnits.TEMPERATURE, 3, TemperatureUnit.DEGREE_RANKINE, "DEGREE_RANKINE", "OR");

    /** Temperature.DEGREE_REAUMUR unit type with code 4. */
    public static final DisplayType TEMPERATURE_DEGREE_REAUMUR =
            new DisplayType(SerializationUnits.TEMPERATURE, 4, TemperatureUnit.DEGREE_REAUMUR, "DEGREE_REAUMUR", "ORé");

    /* ============================================== ABSOLUTETEMPERATURE ================================================ */

    /** AbsoluteTemperature.KELVIN unit type with code 0. */
    public static final DisplayType ABSOLUTETEMPERATURE_KELVIN =
            new DisplayType(SerializationUnits.ABSOLUTETEMPERATURE, 0, AbsoluteTemperatureUnit.KELVIN, "KELVIN", "K");

    /** AbsoluteTemperature.DEGREE_CELSIUS unit type with code 1. */
    public static final DisplayType ABSOLUTETEMPERATURE_DEGREE_CELSIUS = new DisplayType(
            SerializationUnits.ABSOLUTETEMPERATURE, 1, AbsoluteTemperatureUnit.DEGREE_CELSIUS, "DEGREE_CELSIUS", "OC");

    /** AbsoluteTemperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final DisplayType ABSOLUTETEMPERATURE_DEGREE_FAHRENHEIT = new DisplayType(
            SerializationUnits.ABSOLUTETEMPERATURE, 2, AbsoluteTemperatureUnit.DEGREE_FAHRENHEIT, "DEGREE_FAHRENHEIT", "OF");

    /** AbsoluteTemperature.DEGREE_RANKINE unit type with code 3. */
    public static final DisplayType ABSOLUTETEMPERATURE_DEGREE_RANKINE = new DisplayType(
            SerializationUnits.ABSOLUTETEMPERATURE, 3, AbsoluteTemperatureUnit.DEGREE_RANKINE, "DEGREE_RANKINE", "OR");

    /** AbsoluteTemperature.DEGREE_REAUMUR unit type with code 4. */
    public static final DisplayType ABSOLUTETEMPERATURE_DEGREE_REAUMUR = new DisplayType(
            SerializationUnits.ABSOLUTETEMPERATURE, 4, AbsoluteTemperatureUnit.DEGREE_REAUMUR, "DEGREE_REAUMUR", "ORé");

    /* ==================================================== DURATION ===================================================== */

    /** Duration.SECOND unit type with code 0. */
    public static final DisplayType DURATION_SECOND =
            new DisplayType(SerializationUnits.DURATION, 0, DurationUnit.SECOND, "SECOND", "s");

    /** Duration.ATTOSECOND unit type with code 1. */
    public static final DisplayType DURATION_ATTOSECOND =
            new DisplayType(SerializationUnits.DURATION, 1, DurationUnit.ATTOSECOND, "ATTOSECOND", "as");

    /** Duration.FEMTOSECOND unit type with code 2. */
    public static final DisplayType DURATION_FEMTOSECOND =
            new DisplayType(SerializationUnits.DURATION, 2, DurationUnit.FEMTOSECOND, "FEMTOSECOND", "fs");

    /** Duration.PICOSECOND unit type with code 3. */
    public static final DisplayType DURATION_PICOSECOND =
            new DisplayType(SerializationUnits.DURATION, 3, DurationUnit.PICOSECOND, "PICOSECOND", "ps");

    /** Duration.NANOSECOND unit type with code 4. */
    public static final DisplayType DURATION_NANOSECOND =
            new DisplayType(SerializationUnits.DURATION, 4, DurationUnit.NANOSECOND, "NANOSECOND", "ns");

    /** Duration.MICROSECOND unit type with code 5. */
    public static final DisplayType DURATION_MICROSECOND =
            new DisplayType(SerializationUnits.DURATION, 5, DurationUnit.MICROSECOND, "MICROSECOND", "μs");

    /** Duration.MILLISECOND unit type with code 6. */
    public static final DisplayType DURATION_MILLISECOND =
            new DisplayType(SerializationUnits.DURATION, 6, DurationUnit.MILLISECOND, "MILLISECOND", "ms");

    /** Duration.MINUTE unit type with code 7. */
    public static final DisplayType DURATION_MINUTE =
            new DisplayType(SerializationUnits.DURATION, 7, DurationUnit.MINUTE, "MINUTE", "min");

    /** Duration.HOUR unit type with code 8. */
    public static final DisplayType DURATION_HOUR =
            new DisplayType(SerializationUnits.DURATION, 8, DurationUnit.HOUR, "HOUR", "hr");

    /** Duration.DAY unit type with code 9. */
    public static final DisplayType DURATION_DAY =
            new DisplayType(SerializationUnits.DURATION, 9, DurationUnit.DAY, "DAY", "day");

    /** Duration.WEEK unit type with code 10. */
    public static final DisplayType DURATION_WEEK =
            new DisplayType(SerializationUnits.DURATION, 10, DurationUnit.WEEK, "WEEK", "wk");

    /* ====================================================== TIME ======================================================= */

    /** Time.BASE_SECOND unit type with code 0. */
    public static final DisplayType TIME_BASE_SECOND =
            new DisplayType(SerializationUnits.TIME, 0, TimeUnit.BASE_SECOND, "SECOND", "s");

    /** Time.BASE_MICROSECOND unit type with code 1. */
    public static final DisplayType TIME_BASE_MICROSECOND =
            new DisplayType(SerializationUnits.TIME, 1, TimeUnit.BASE_MICROSECOND, "MICROSECOND", "μs");

    /** Time.BASE_MILLISECOND unit type with code 2. */
    public static final DisplayType TIME_BASE_MILLISECOND =
            new DisplayType(SerializationUnits.TIME, 2, TimeUnit.BASE_MILLISECOND, "MILLISECOND", "ms");

    /** Time.BASE_MINUTE unit type with code 3. */
    public static final DisplayType TIME_BASE_MINUTE =
            new DisplayType(SerializationUnits.TIME, 3, TimeUnit.BASE_MINUTE, "MINUTE", "min");

    /** Time.BASE_HOUR unit type with code 4. */
    public static final DisplayType TIME_BASE_HOUR =
            new DisplayType(SerializationUnits.TIME, 4, TimeUnit.BASE_HOUR, "HOUR", "hr");

    /** Time.BASE_DAY unit type with code 5. */
    public static final DisplayType TIME_BASE_DAY =
            new DisplayType(SerializationUnits.TIME, 5, TimeUnit.BASE_DAY, "DAY", "day");

    /** Time.BASE_WEEK unit type with code 6. */
    public static final DisplayType TIME_BASE_WEEK =
            new DisplayType(SerializationUnits.TIME, 6, TimeUnit.BASE_WEEK, "WEEK", "wk");

    /** Time.EPOCH_SECOND unit type with code 7. */
    public static final DisplayType TIME_EPOCH_SECOND =
            new DisplayType(SerializationUnits.TIME, 7, TimeUnit.EPOCH_SECOND, "SECOND (1-1-70)", "s(POSIX)");

    /** Time.EPOCH_MICROSECOND unit type with code 8. */
    public static final DisplayType TIME_EPOCH_MICROSECOND =
            new DisplayType(SerializationUnits.TIME, 8, TimeUnit.BASE_MICROSECOND, "MICROSECOND (1-1-70)", "μs(POSIX)");

    /** Time.EPOCH_MILLISECOND unit type with code 9. */
    public static final DisplayType TIME_EPOCH_MILLISECOND =
            new DisplayType(SerializationUnits.TIME, 9, TimeUnit.BASE_MILLISECOND, "MILLISECOND (1-1-70)", "ms(POSIX)");

    /** Time.EPOCH_MINUTE unit type with code 10. */
    public static final DisplayType TIME_EPOCH_MINUTE =
            new DisplayType(SerializationUnits.TIME, 10, TimeUnit.BASE_MINUTE, "MINUTE (1-1-70)", "min(POSIX)");

    /** Time.EPOCH_HOUR unit type with code 11. */
    public static final DisplayType TIME_EPOCH_HOUR =
            new DisplayType(SerializationUnits.TIME, 11, TimeUnit.BASE_HOUR, "HOUR (1-1-70)", "hr(POSIX)");

    /** Time.EPOCH_DAY unit type with code 12. */
    public static final DisplayType TIME_EPOCH_DAY =
            new DisplayType(SerializationUnits.TIME, 12, TimeUnit.BASE_DAY, "DAY (1-1-70)", "day(POSIX)");

    /** Time.EPOCH_WEEK unit type with code 13. */
    public static final DisplayType TIME_EPOCH_WEEK =
            new DisplayType(SerializationUnits.TIME, 13, TimeUnit.BASE_WEEK, "WEEK (1-1-70)", "wk(POSIX)");

    /** Time.TIME_YEAR1_SECOND unit type with code 14. */
    public static final DisplayType TIME_YEAR1_SECOND =
            new DisplayType(SerializationUnits.TIME, 14, TimeUnit.EPOCH_YEAR_1, "SECOND (1-1-0001)", "s(1-1-0001)");

    /** Time.TIME_J2000_SECOND unit type with code 15. */
    public static final DisplayType TIME_J2000_SECOND =
            new DisplayType(SerializationUnits.TIME, 15, TimeUnit.EPOCH_J2000_1, "SECOND (1-1-2000 12:00)", "s(1-1-2000)");

    /* ===================================================== TORQUE ====================================================== */

    /** Torque.NEWTON_METER unit type with code 0. */
    public static final DisplayType TORQUE_NEWTON_METER =
            new DisplayType(SerializationUnits.TORQUE, 0, TorqueUnit.NEWTON_METER, "NEWTON_METER", "Nm");

    /** Torque.POUND_FOOT unit type with code 1. */
    public static final DisplayType TORQUE_POUND_FOOT =
            new DisplayType(SerializationUnits.TORQUE, 1, TorqueUnit.POUND_FOOT, "POUND_FOOT", "lb.ft");

    /** Torque.POUND_INCH unit type with code 2. */
    public static final DisplayType TORQUE_POUND_INCH =
            new DisplayType(SerializationUnits.TORQUE, 2, TorqueUnit.POUND_INCH, "POUND_INCH", "lb.in");

    /** Torque.METER_KILOGRAM_FORCE unit type with code 3. */
    public static final DisplayType TORQUE_METER_KILOGRAM_FORCE =
            new DisplayType(SerializationUnits.TORQUE, 3, TorqueUnit.METER_KILOGRAM_FORCE, "METER_KILOGRAM_FORCE", "m.kgf");

    /* ===================================================== VOLUME ====================================================== */

    /** Volume.CUBIC_METER unit type with code 0. */
    public static final DisplayType VOLUME_CUBIC_METER =
            new DisplayType(SerializationUnits.VOLUME, 0, VolumeUnit.CUBIC_METER, "CUBIC_METER", "m3");

    /** Volume.CUBIC_ATTOMETER unit type with code 1. */
    public static final DisplayType VOLUME_CUBIC_ATTOMETER =
            new DisplayType(SerializationUnits.VOLUME, 1, VolumeUnit.CUBIC_ATTOMETER, "CUBIC_ATTOMETER", "am3");

    /** Volume.CUBIC_FEMTOMETER unit type with code 2. */
    public static final DisplayType VOLUME_CUBIC_FEMTOMETER =
            new DisplayType(SerializationUnits.VOLUME, 2, VolumeUnit.CUBIC_FEMTOMETER, "CUBIC_FEMTOMETER", "fm3");

    /** Volume.CUBIC_PICOMETER unit type with code 3. */
    public static final DisplayType VOLUME_CUBIC_PICOMETER =
            new DisplayType(SerializationUnits.VOLUME, 3, VolumeUnit.CUBIC_PICOMETER, "CUBIC_PICOMETER", "pm3");

    /** Volume.CUBIC_NANOMETER unit type with code 4. */
    public static final DisplayType VOLUME_CUBIC_NANOMETER =
            new DisplayType(SerializationUnits.VOLUME, 4, VolumeUnit.CUBIC_NANOMETER, "CUBIC_NANOMETER", "nm3");

    /** Volume.CUBIC_MICROMETER unit type with code 5. */
    public static final DisplayType VOLUME_CUBIC_MICROMETER =
            new DisplayType(SerializationUnits.VOLUME, 5, VolumeUnit.CUBIC_MICROMETER, "CUBIC_MICROMETER", "μm3");

    /** Volume.CUBIC_MILLIMETER unit type with code 6. */
    public static final DisplayType VOLUME_CUBIC_MILLIMETER =
            new DisplayType(SerializationUnits.VOLUME, 6, VolumeUnit.CUBIC_MILLIMETER, "CUBIC_MILLIMETER", "mm3");

    /** Volume.CUBIC_CENTIMETER unit type with code 7. */
    public static final DisplayType VOLUME_CUBIC_CENTIMETER =
            new DisplayType(SerializationUnits.VOLUME, 7, VolumeUnit.CUBIC_CENTIMETER, "CUBIC_CENTIMETER", "cm3");

    /** Volume.CUBIC_DECIMETER unit type with code 8. */
    public static final DisplayType VOLUME_CUBIC_DECIMETER =
            new DisplayType(SerializationUnits.VOLUME, 8, VolumeUnit.CUBIC_DECIMETER, "CUBIC_DECIMETER", "dm3");

    /** Volume.CUBIC_DEKAMETER unit type with code 9. */
    public static final DisplayType VOLUME_CUBIC_DEKAMETER =
            new DisplayType(SerializationUnits.VOLUME, 9, VolumeUnit.CUBIC_DEKAMETER, "CUBIC_DEKAMETER", "dam3");

    /** Volume.CUBIC_HECTOMETER unit type with code 10. */
    public static final DisplayType VOLUME_CUBIC_HECTOMETER =
            new DisplayType(SerializationUnits.VOLUME, 10, VolumeUnit.CUBIC_HECTOMETER, "CUBIC_HECTOMETER", "hm3");

    /** Volume.CUBIC_KILOMETER unit type with code 11. */
    public static final DisplayType VOLUME_CUBIC_KILOMETER =
            new DisplayType(SerializationUnits.VOLUME, 11, VolumeUnit.CUBIC_KILOMETER, "CUBIC_KILOMETER", "km3");

    /** Volume.CUBIC_MEGAMETER unit type with code 12. */
    public static final DisplayType VOLUME_CUBIC_MEGAMETER =
            new DisplayType(SerializationUnits.VOLUME, 12, VolumeUnit.CUBIC_MEGAMETER, "CUBIC_MEGAMETER", "Mm3");

    /** Volume.CUBIC_INCH unit type with code 13. */
    public static final DisplayType VOLUME_CUBIC_INCH =
            new DisplayType(SerializationUnits.VOLUME, 13, VolumeUnit.CUBIC_INCH, "CUBIC_INCH", "in3");

    /** Volume.CUBIC_FOOT unit type with code 14. */
    public static final DisplayType VOLUME_CUBIC_FOOT =
            new DisplayType(SerializationUnits.VOLUME, 14, VolumeUnit.CUBIC_FOOT, "CUBIC_FOOT", "ft3");

    /** Volume.CUBIC_YARD unit type with code 15. */
    public static final DisplayType VOLUME_CUBIC_YARD =
            new DisplayType(SerializationUnits.VOLUME, 15, VolumeUnit.CUBIC_YARD, "CUBIC_YARD", "yd3");

    /** Volume.CUBIC_MILE unit type with code 16. */
    public static final DisplayType VOLUME_CUBIC_MILE =
            new DisplayType(SerializationUnits.VOLUME, 16, VolumeUnit.CUBIC_MILE, "CUBIC_MILE", "mi3");

    /** Volume.LITER unit type with code 17. */
    public static final DisplayType VOLUME_LITER =
            new DisplayType(SerializationUnits.VOLUME, 17, VolumeUnit.LITER, "LITER", "l");

    /** Volume.GALLON_IMP unit type with code 18. */
    public static final DisplayType VOLUME_GALLON_IMP =
            new DisplayType(SerializationUnits.VOLUME, 18, VolumeUnit.GALLON_IMP, "GALLON_IMP", "gal (imp)");

    /** Volume.GALLON_US_FLUID unit type with code 19. */
    public static final DisplayType VOLUME_GALLON_US_FLUID =
            new DisplayType(SerializationUnits.VOLUME, 19, VolumeUnit.GALLON_US_FLUID, "GALLON_US_FLUID", "gal (US)");

    /** Volume.OUNCE_IMP_FLUID unit type with code 20. */
    public static final DisplayType VOLUME_OUNCE_IMP_FLUID =
            new DisplayType(SerializationUnits.VOLUME, 20, VolumeUnit.OUNCE_IMP_FLUID, "OUNCE_IMP_FLUID", "oz (imp)");

    /** Volume.OUNCE_US_FLUID unit type with code 21. */
    public static final DisplayType VOLUME_OUNCE_US_FLUID =
            new DisplayType(SerializationUnits.VOLUME, 21, VolumeUnit.OUNCE_US_FLUID, "OUNCE_US_FLUID", "oz (US)");

    /** Volume.PINT_IMP unit type with code 22. */
    public static final DisplayType VOLUME_PINT_IMP =
            new DisplayType(SerializationUnits.VOLUME, 22, VolumeUnit.PINT_IMP, "PINT_IMP", "pt (imp)");

    /** Volume.PINT_US_FLUID unit type with code 23. */
    public static final DisplayType VOLUME_PINT_US_FLUID =
            new DisplayType(SerializationUnits.VOLUME, 23, VolumeUnit.PINT_US_FLUID, "PINT_US_FLUID", "pt (US)");

    /** Volume.QUART_IMP unit type with code 24. */
    public static final DisplayType VOLUME_QUART_IMP =
            new DisplayType(SerializationUnits.VOLUME, 24, VolumeUnit.QUART_IMP, "QUART_IMP", "qt (imp)");

    /** Volume.QUART_US_FLUID unit type with code 25. */
    public static final DisplayType VOLUME_QUART_US_FLUID =
            new DisplayType(SerializationUnits.VOLUME, 25, VolumeUnit.QUART_US_FLUID, "QUART_US_FLUID", "qt (US)");

    /** Volume.CUBIC_PARSEC unit type with code 26. */
    public static final DisplayType VOLUME_CUBIC_PARSEC =
            new DisplayType(SerializationUnits.VOLUME, 26, VolumeUnit.CUBIC_PARSEC, "CUBIC_PARSEC", "pc3");

    /** Volume.CUBIC_LIGHTYEAR unit type with code 27. */
    public static final DisplayType VOLUME_CUBIC_LIGHTYEAR =
            new DisplayType(SerializationUnits.VOLUME, 27, VolumeUnit.CUBIC_LIGHTYEAR, "CUBIC_LIGHTYEAR", "ly3");

    /* ====================================================== MONEY ====================================================== */

    /** Money.AED unit type with code 784. */
    public static final DisplayType MONEY_AED =
            new DisplayType(SerializationUnits.MONEY, 784, MoneyUnit.AED, "AED", "United Arab Emirates dirham");

    /** Money.AFN unit type with code 971. */
    public static final DisplayType MONEY_AFN =
            new DisplayType(SerializationUnits.MONEY, 971, MoneyUnit.AFN, "AFN", "Afghan afghani");

    /** Money.ALL unit type with code 8. */
    public static final DisplayType MONEY_ALL =
            new DisplayType(SerializationUnits.MONEY, 8, MoneyUnit.ALL, "ALL", "Albanian lek");

    /** Money.AMD unit type with code 51. */
    public static final DisplayType MONEY_AMD =
            new DisplayType(SerializationUnits.MONEY, 51, MoneyUnit.AMD, "AMD", "Armenian dram");

    /** Money.ANG unit type with code 532. */
    public static final DisplayType MONEY_ANG =
            new DisplayType(SerializationUnits.MONEY, 532, MoneyUnit.ANG, "ANG", "Netherlands Antillean guilder");

    /** Money.AOA unit type with code 973. */
    public static final DisplayType MONEY_AOA =
            new DisplayType(SerializationUnits.MONEY, 973, MoneyUnit.AOA, "AOA", "Angolan kwanza");

    /** Money.ARS unit type with code 32. */
    public static final DisplayType MONEY_ARS =
            new DisplayType(SerializationUnits.MONEY, 32, MoneyUnit.ARS, "ARS", "Argentine peso");

    /** Money.AUD unit type with code 36. */
    public static final DisplayType MONEY_AUD =
            new DisplayType(SerializationUnits.MONEY, 36, MoneyUnit.AUD, "AUD", "Australian dollar");

    /** Money.AWG unit type with code 533. */
    public static final DisplayType MONEY_AWG =
            new DisplayType(SerializationUnits.MONEY, 533, MoneyUnit.AWG, "AWG", "Aruban florin");

    /** Money.AZN unit type with code 944. */
    public static final DisplayType MONEY_AZN =
            new DisplayType(SerializationUnits.MONEY, 944, MoneyUnit.AZN, "AZN", "Azerbaijani manat");

    /** Money.BAM unit type with code 977. */
    public static final DisplayType MONEY_BAM =
            new DisplayType(SerializationUnits.MONEY, 977, MoneyUnit.BAM, "BAM", "Bosnia and Herzegovina convertible mark");

    /** Money.BBD unit type with code 52. */
    public static final DisplayType MONEY_BBD =
            new DisplayType(SerializationUnits.MONEY, 52, MoneyUnit.BBD, "BBD", "Barbados dollar");

    /** Money.BDT unit type with code 50. */
    public static final DisplayType MONEY_BDT =
            new DisplayType(SerializationUnits.MONEY, 50, MoneyUnit.BDT, "BDT", "Bangladeshi taka");

    /** Money.BGN unit type with code 975. */
    public static final DisplayType MONEY_BGN =
            new DisplayType(SerializationUnits.MONEY, 975, MoneyUnit.BGN, "BGN", "Bulgarian lev");

    /** Money.BHD unit type with code 48. */
    public static final DisplayType MONEY_BHD =
            new DisplayType(SerializationUnits.MONEY, 48, MoneyUnit.BHD, "BHD", "Bahraini dinar");

    /** Money.BIF unit type with code 108. */
    public static final DisplayType MONEY_BIF =
            new DisplayType(SerializationUnits.MONEY, 108, MoneyUnit.BIF, "BIF", "Burundian franc");

    /** Money.BMD unit type with code 60. */
    public static final DisplayType MONEY_BMD =
            new DisplayType(SerializationUnits.MONEY, 60, MoneyUnit.BMD, "BMD", "Bermudian dollar");

    /** Money.BND unit type with code 96. */
    public static final DisplayType MONEY_BND =
            new DisplayType(SerializationUnits.MONEY, 96, MoneyUnit.BND, "BND", "Brunei dollar");

    /** Money.BOB unit type with code 68. */
    public static final DisplayType MONEY_BOB =
            new DisplayType(SerializationUnits.MONEY, 68, MoneyUnit.BOB, "BOB", "Boliviano");

    /** Money.BOV unit type with code 984. */
    public static final DisplayType MONEY_BOV =
            new DisplayType(SerializationUnits.MONEY, 984, MoneyUnit.BOV, "BOV", "Bolivian Mvdol (funds code)");

    /** Money.BRL unit type with code 986. */
    public static final DisplayType MONEY_BRL =
            new DisplayType(SerializationUnits.MONEY, 986, MoneyUnit.BRL, "BRL", "Brazilian real");

    /** Money.BSD unit type with code 44. */
    public static final DisplayType MONEY_BSD =
            new DisplayType(SerializationUnits.MONEY, 44, MoneyUnit.BSD, "BSD", "Bahamian dollar");

    /** Money.BTN unit type with code 64. */
    public static final DisplayType MONEY_BTN =
            new DisplayType(SerializationUnits.MONEY, 64, MoneyUnit.BTN, "BTN", "Bhutanese ngultrum");

    /** Money.BWP unit type with code 72. */
    public static final DisplayType MONEY_BWP =
            new DisplayType(SerializationUnits.MONEY, 72, MoneyUnit.BWP, "BWP", "Botswana pula");

    /** Money.BYN unit type with code 933. */
    public static final DisplayType MONEY_BYN =
            new DisplayType(SerializationUnits.MONEY, 933, MoneyUnit.BYN, "BYN", "New Belarusian ruble");

    /** Money.BYR unit type with code 974. */
    public static final DisplayType MONEY_BYR =
            new DisplayType(SerializationUnits.MONEY, 974, MoneyUnit.BYR, "BYR", "Belarusian ruble");

    /** Money.BZD unit type with code 84. */
    public static final DisplayType MONEY_BZD =
            new DisplayType(SerializationUnits.MONEY, 84, MoneyUnit.BZD, "BZD", "Belize dollar");

    /** Money.CAD unit type with code 124. */
    public static final DisplayType MONEY_CAD =
            new DisplayType(SerializationUnits.MONEY, 124, MoneyUnit.CAD, "CAD", "Canadian dollar");

    /** Money.CDF unit type with code 976. */
    public static final DisplayType MONEY_CDF =
            new DisplayType(SerializationUnits.MONEY, 976, MoneyUnit.CDF, "CDF", "Congolese franc");

    /** Money.CHE unit type with code 947. */
    public static final DisplayType MONEY_CHE =
            new DisplayType(SerializationUnits.MONEY, 947, MoneyUnit.CHE, "CHE", "WIR Euro (complementary currency)");

    /** Money.CHF unit type with code 756. */
    public static final DisplayType MONEY_CHF =
            new DisplayType(SerializationUnits.MONEY, 756, MoneyUnit.CHF, "CHF", "Swiss franc");

    /** Money.CHW unit type with code 948. */
    public static final DisplayType MONEY_CHW =
            new DisplayType(SerializationUnits.MONEY, 948, MoneyUnit.CHW, "CHW", "WIR Franc (complementary currency)");

    /** Money.CLF unit type with code 990. */
    public static final DisplayType MONEY_CLF =
            new DisplayType(SerializationUnits.MONEY, 990, MoneyUnit.CLF, "CLF", "Unidad de Fomento (funds code)");

    /** Money.CLP unit type with code 152. */
    public static final DisplayType MONEY_CLP =
            new DisplayType(SerializationUnits.MONEY, 152, MoneyUnit.CLP, "CLP", "Chilean peso");

    /** Money.CNY unit type with code 156. */
    public static final DisplayType MONEY_CNY =
            new DisplayType(SerializationUnits.MONEY, 156, MoneyUnit.CNY, "CNY", "Chinese yuan");

    /** Money.COP unit type with code 170. */
    public static final DisplayType MONEY_COP =
            new DisplayType(SerializationUnits.MONEY, 170, MoneyUnit.COP, "COP", "Colombian peso");

    /** Money.COU unit type with code 970. */
    public static final DisplayType MONEY_COU =
            new DisplayType(SerializationUnits.MONEY, 970, MoneyUnit.COU, "COU", "Unidad de Valor Real (UVR) (funds code)");

    /** Money.CRC unit type with code 188. */
    public static final DisplayType MONEY_CRC =
            new DisplayType(SerializationUnits.MONEY, 188, MoneyUnit.CRC, "CRC", "Costa Rican colon");

    /** Money.CUC unit type with code 931. */
    public static final DisplayType MONEY_CUC =
            new DisplayType(SerializationUnits.MONEY, 931, MoneyUnit.CUC, "CUC", "Cuban convertible peso");

    /** Money.CUP unit type with code 192. */
    public static final DisplayType MONEY_CUP =
            new DisplayType(SerializationUnits.MONEY, 192, MoneyUnit.CUP, "CUP", "Cuban peso");

    /** Money.CVE unit type with code 132. */
    public static final DisplayType MONEY_CVE =
            new DisplayType(SerializationUnits.MONEY, 132, MoneyUnit.CVE, "CVE", "Cape Verde escudo");

    /** Money.CZK unit type with code 203. */
    public static final DisplayType MONEY_CZK =
            new DisplayType(SerializationUnits.MONEY, 203, MoneyUnit.CZK, "CZK", "Czech koruna");

    /** Money.DJF unit type with code 262. */
    public static final DisplayType MONEY_DJF =
            new DisplayType(SerializationUnits.MONEY, 262, MoneyUnit.DJF, "DJF", "Djiboutian franc");

    /** Money.DKK unit type with code 208. */
    public static final DisplayType MONEY_DKK =
            new DisplayType(SerializationUnits.MONEY, 208, MoneyUnit.DKK, "DKK", "Danish krone");

    /** Money.DOP unit type with code 214. */
    public static final DisplayType MONEY_DOP =
            new DisplayType(SerializationUnits.MONEY, 214, MoneyUnit.DOP, "DOP", "Dominican peso");

    /** Money.DZD unit type with code 12. */
    public static final DisplayType MONEY_DZD =
            new DisplayType(SerializationUnits.MONEY, 12, MoneyUnit.DZD, "DZD", "Algerian dinar");

    /** Money.EGP unit type with code 818. */
    public static final DisplayType MONEY_EGP =
            new DisplayType(SerializationUnits.MONEY, 818, MoneyUnit.EGP, "EGP", "Egyptian pound");

    /** Money.ERN unit type with code 232. */
    public static final DisplayType MONEY_ERN =
            new DisplayType(SerializationUnits.MONEY, 232, MoneyUnit.ERN, "ERN", "Eritrean nakfa");

    /** Money.ETB unit type with code 230. */
    public static final DisplayType MONEY_ETB =
            new DisplayType(SerializationUnits.MONEY, 230, MoneyUnit.ETB, "ETB", "Ethiopian birr");

    /** Money.EUR unit type with code 978. */
    public static final DisplayType MONEY_EUR =
            new DisplayType(SerializationUnits.MONEY, 978, MoneyUnit.EUR, "EUR", "Euro");

    /** Money.FJD unit type with code 242. */
    public static final DisplayType MONEY_FJD =
            new DisplayType(SerializationUnits.MONEY, 242, MoneyUnit.FJD, "FJD", "Fiji dollar");

    /** Money.FKP unit type with code 238. */
    public static final DisplayType MONEY_FKP =
            new DisplayType(SerializationUnits.MONEY, 238, MoneyUnit.FKP, "FKP", "Falkland Islands pound");

    /** Money.GBP unit type with code 826. */
    public static final DisplayType MONEY_GBP =
            new DisplayType(SerializationUnits.MONEY, 826, MoneyUnit.GBP, "GBP", "Pound sterling");

    /** Money.GEL unit type with code 981. */
    public static final DisplayType MONEY_GEL =
            new DisplayType(SerializationUnits.MONEY, 981, MoneyUnit.GEL, "GEL", "Georgian lari");

    /** Money.GHS unit type with code 936. */
    public static final DisplayType MONEY_GHS =
            new DisplayType(SerializationUnits.MONEY, 936, MoneyUnit.GHS, "GHS", "Ghanaian cedi");

    /** Money.GIP unit type with code 292. */
    public static final DisplayType MONEY_GIP =
            new DisplayType(SerializationUnits.MONEY, 292, MoneyUnit.GIP, "GIP", "Gibraltar pound");

    /** Money.GMD unit type with code 270. */
    public static final DisplayType MONEY_GMD =
            new DisplayType(SerializationUnits.MONEY, 270, MoneyUnit.GMD, "GMD", "Gambian dalasi");

    /** Money.GNF unit type with code 324. */
    public static final DisplayType MONEY_GNF =
            new DisplayType(SerializationUnits.MONEY, 324, MoneyUnit.GNF, "GNF", "Guinean franc");

    /** Money.GTQ unit type with code 320. */
    public static final DisplayType MONEY_GTQ =
            new DisplayType(SerializationUnits.MONEY, 320, MoneyUnit.GTQ, "GTQ", "Guatemalan quetzal");

    /** Money.GYD unit type with code 328. */
    public static final DisplayType MONEY_GYD =
            new DisplayType(SerializationUnits.MONEY, 328, MoneyUnit.GYD, "GYD", "Guyanese dollar");

    /** Money.HKD unit type with code 344. */
    public static final DisplayType MONEY_HKD =
            new DisplayType(SerializationUnits.MONEY, 344, MoneyUnit.HKD, "HKD", "Hong Kong dollar");

    /** Money.HNL unit type with code 340. */
    public static final DisplayType MONEY_HNL =
            new DisplayType(SerializationUnits.MONEY, 340, MoneyUnit.HNL, "HNL", "Honduran lempira");

    /** Money.HRK unit type with code 191. */
    public static final DisplayType MONEY_HRK =
            new DisplayType(SerializationUnits.MONEY, 191, MoneyUnit.HRK, "HRK", "Croatian kuna");

    /** Money.HTG unit type with code 332. */
    public static final DisplayType MONEY_HTG =
            new DisplayType(SerializationUnits.MONEY, 332, MoneyUnit.HTG, "HTG", "Haitian gourde");

    /** Money.HUF unit type with code 348. */
    public static final DisplayType MONEY_HUF =
            new DisplayType(SerializationUnits.MONEY, 348, MoneyUnit.HUF, "HUF", "Hungarian forint");

    /** Money.IDR unit type with code 360. */
    public static final DisplayType MONEY_IDR =
            new DisplayType(SerializationUnits.MONEY, 360, MoneyUnit.IDR, "IDR", "Indonesian rupiah");

    /** Money.ILS unit type with code 376. */
    public static final DisplayType MONEY_ILS =
            new DisplayType(SerializationUnits.MONEY, 376, MoneyUnit.ILS, "ILS", "Israeli new shekel");

    /** Money.INR unit type with code 356. */
    public static final DisplayType MONEY_INR =
            new DisplayType(SerializationUnits.MONEY, 356, MoneyUnit.INR, "INR", "Indian rupee");

    /** Money.IQD unit type with code 368. */
    public static final DisplayType MONEY_IQD =
            new DisplayType(SerializationUnits.MONEY, 368, MoneyUnit.IQD, "IQD", "Iraqi dinar");

    /** Money.IRR unit type with code 364. */
    public static final DisplayType MONEY_IRR =
            new DisplayType(SerializationUnits.MONEY, 364, MoneyUnit.IRR, "IRR", "Iranian rial");

    /** Money.ISK unit type with code 352. */
    public static final DisplayType MONEY_ISK =
            new DisplayType(SerializationUnits.MONEY, 352, MoneyUnit.ISK, "ISK", "Icelandic króna");

    /** Money.JMD unit type with code 388. */
    public static final DisplayType MONEY_JMD =
            new DisplayType(SerializationUnits.MONEY, 388, MoneyUnit.JMD, "JMD", "Jamaican dollar");

    /** Money.JOD unit type with code 400. */
    public static final DisplayType MONEY_JOD =
            new DisplayType(SerializationUnits.MONEY, 400, MoneyUnit.JOD, "JOD", "Jordanian dinar");

    /** Money.JPY unit type with code 392. */
    public static final DisplayType MONEY_JPY =
            new DisplayType(SerializationUnits.MONEY, 392, MoneyUnit.JPY, "JPY", "Japanese yen");

    /** Money.KES unit type with code 404. */
    public static final DisplayType MONEY_KES =
            new DisplayType(SerializationUnits.MONEY, 404, MoneyUnit.KES, "KES", "Kenyan shilling");

    /** Money.KGS unit type with code 417. */
    public static final DisplayType MONEY_KGS =
            new DisplayType(SerializationUnits.MONEY, 417, MoneyUnit.KGS, "KGS", "Kyrgyzstani som");

    /** Money.KHR unit type with code 116. */
    public static final DisplayType MONEY_KHR =
            new DisplayType(SerializationUnits.MONEY, 116, MoneyUnit.KHR, "KHR", "Cambodian riel");

    /** Money.KMF unit type with code 174. */
    public static final DisplayType MONEY_KMF =
            new DisplayType(SerializationUnits.MONEY, 174, MoneyUnit.KMF, "KMF", "Comoro franc");

    /** Money.KPW unit type with code 408. */
    public static final DisplayType MONEY_KPW =
            new DisplayType(SerializationUnits.MONEY, 408, MoneyUnit.KPW, "KPW", "North Korean won");

    /** Money.KRW unit type with code 410. */
    public static final DisplayType MONEY_KRW =
            new DisplayType(SerializationUnits.MONEY, 410, MoneyUnit.KRW, "KRW", "South Korean won");

    /** Money.KWD unit type with code 414. */
    public static final DisplayType MONEY_KWD =
            new DisplayType(SerializationUnits.MONEY, 414, MoneyUnit.KWD, "KWD", "Kuwaiti dinar");

    /** Money.KYD unit type with code 136. */
    public static final DisplayType MONEY_KYD =
            new DisplayType(SerializationUnits.MONEY, 136, MoneyUnit.KYD, "KYD", "Cayman Islands dollar");

    /** Money.KZT unit type with code 398. */
    public static final DisplayType MONEY_KZT =
            new DisplayType(SerializationUnits.MONEY, 398, MoneyUnit.KZT, "KZT", "Kazakhstani tenge");

    /** Money.LAK unit type with code 418. */
    public static final DisplayType MONEY_LAK =
            new DisplayType(SerializationUnits.MONEY, 418, MoneyUnit.LAK, "LAK", "Lao kip");

    /** Money.LBP unit type with code 422. */
    public static final DisplayType MONEY_LBP =
            new DisplayType(SerializationUnits.MONEY, 422, MoneyUnit.LBP, "LBP", "Lebanese pound");

    /** Money.LKR unit type with code 144. */
    public static final DisplayType MONEY_LKR =
            new DisplayType(SerializationUnits.MONEY, 144, MoneyUnit.LKR, "LKR", "Sri Lankan rupee");

    /** Money.LRD unit type with code 430. */
    public static final DisplayType MONEY_LRD =
            new DisplayType(SerializationUnits.MONEY, 430, MoneyUnit.LRD, "LRD", "Liberian dollar");

    /** Money.LSL unit type with code 426. */
    public static final DisplayType MONEY_LSL =
            new DisplayType(SerializationUnits.MONEY, 426, MoneyUnit.LSL, "LSL", "Lesotho loti");

    /** Money.LYD unit type with code 434. */
    public static final DisplayType MONEY_LYD =
            new DisplayType(SerializationUnits.MONEY, 434, MoneyUnit.LYD, "LYD", "Libyan dinar");

    /** Money.MAD unit type with code 504. */
    public static final DisplayType MONEY_MAD =
            new DisplayType(SerializationUnits.MONEY, 504, MoneyUnit.MAD, "MAD", "Moroccan dirham");

    /** Money.MDL unit type with code 498. */
    public static final DisplayType MONEY_MDL =
            new DisplayType(SerializationUnits.MONEY, 498, MoneyUnit.MDL, "MDL", "Moldovan leu");

    /** Money.MGA unit type with code 969. */
    public static final DisplayType MONEY_MGA =
            new DisplayType(SerializationUnits.MONEY, 969, MoneyUnit.MGA, "MGA", "Malagasy ariary");

    /** Money.MKD unit type with code 807. */
    public static final DisplayType MONEY_MKD =
            new DisplayType(SerializationUnits.MONEY, 807, MoneyUnit.MKD, "MKD", "Macedonian denar");

    /** Money.MMK unit type with code 104. */
    public static final DisplayType MONEY_MMK =
            new DisplayType(SerializationUnits.MONEY, 104, MoneyUnit.MMK, "MMK", "Myanmar kyat");

    /** Money.MNT unit type with code 496. */
    public static final DisplayType MONEY_MNT =
            new DisplayType(SerializationUnits.MONEY, 496, MoneyUnit.MNT, "MNT", "Mongolian tögrög");

    /** Money.MOP unit type with code 446. */
    public static final DisplayType MONEY_MOP =
            new DisplayType(SerializationUnits.MONEY, 446, MoneyUnit.MOP, "MOP", "Macanese pataca");

    /** Money.MRO unit type with code 478. */
    public static final DisplayType MONEY_MRO =
            new DisplayType(SerializationUnits.MONEY, 478, MoneyUnit.MRO, "MRO", "Mauritanian ouguiya");

    /** Money.MUR unit type with code 480. */
    public static final DisplayType MONEY_MUR =
            new DisplayType(SerializationUnits.MONEY, 480, MoneyUnit.MUR, "MUR", "Mauritian rupee");

    /** Money.MVR unit type with code 462. */
    public static final DisplayType MONEY_MVR =
            new DisplayType(SerializationUnits.MONEY, 462, MoneyUnit.MVR, "MVR", "Maldivian rufiyaa");

    /** Money.MWK unit type with code 454. */
    public static final DisplayType MONEY_MWK =
            new DisplayType(SerializationUnits.MONEY, 454, MoneyUnit.MWK, "MWK", "Malawian kwacha");

    /** Money.MXN unit type with code 484. */
    public static final DisplayType MONEY_MXN =
            new DisplayType(SerializationUnits.MONEY, 484, MoneyUnit.MXN, "MXN", "Mexican peso");

    /** Money.MXV unit type with code 979. */
    public static final DisplayType MONEY_MXV = new DisplayType(SerializationUnits.MONEY, 979, MoneyUnit.MXV, "MXV",
            "Mexican Unidad de Inversion (UDI) (funds code)");

    /** Money.MYR unit type with code 458. */
    public static final DisplayType MONEY_MYR =
            new DisplayType(SerializationUnits.MONEY, 458, MoneyUnit.MYR, "MYR", "Malaysian ringgit");

    /** Money.MZN unit type with code 943. */
    public static final DisplayType MONEY_MZN =
            new DisplayType(SerializationUnits.MONEY, 943, MoneyUnit.MZN, "MZN", "Mozambican metical");

    /** Money.NAD unit type with code 516. */
    public static final DisplayType MONEY_NAD =
            new DisplayType(SerializationUnits.MONEY, 516, MoneyUnit.NAD, "NAD", "Namibian dollar");

    /** Money.NGN unit type with code 566. */
    public static final DisplayType MONEY_NGN =
            new DisplayType(SerializationUnits.MONEY, 566, MoneyUnit.NGN, "NGN", "Nigerian naira");

    /** Money.NIO unit type with code 558. */
    public static final DisplayType MONEY_NIO =
            new DisplayType(SerializationUnits.MONEY, 558, MoneyUnit.NIO, "NIO", "Nicaraguan córdoba");

    /** Money.NOK unit type with code 578. */
    public static final DisplayType MONEY_NOK =
            new DisplayType(SerializationUnits.MONEY, 578, MoneyUnit.NOK, "NOK", "Norwegian krone");

    /** Money.NPR unit type with code 524. */
    public static final DisplayType MONEY_NPR =
            new DisplayType(SerializationUnits.MONEY, 524, MoneyUnit.NPR, "NPR", "Nepalese rupee");

    /** Money.NZD unit type with code 554. */
    public static final DisplayType MONEY_NZD =
            new DisplayType(SerializationUnits.MONEY, 554, MoneyUnit.NZD, "NZD", "New Zealand dollar");

    /** Money.OMR unit type with code 512. */
    public static final DisplayType MONEY_OMR =
            new DisplayType(SerializationUnits.MONEY, 512, MoneyUnit.OMR, "OMR", "Omani rial");

    /** Money.PAB unit type with code 590. */
    public static final DisplayType MONEY_PAB =
            new DisplayType(SerializationUnits.MONEY, 590, MoneyUnit.PAB, "PAB", "Panamanian balboa");

    /** Money.PEN unit type with code 604. */
    public static final DisplayType MONEY_PEN =
            new DisplayType(SerializationUnits.MONEY, 604, MoneyUnit.PEN, "PEN", "Peruvian Sol");

    /** Money.PGK unit type with code 598. */
    public static final DisplayType MONEY_PGK =
            new DisplayType(SerializationUnits.MONEY, 598, MoneyUnit.PGK, "PGK", "Papua New Guinean kina");

    /** Money.PHP unit type with code 608. */
    public static final DisplayType MONEY_PHP =
            new DisplayType(SerializationUnits.MONEY, 608, MoneyUnit.PHP, "PHP", "Philippine peso");

    /** Money.PKR unit type with code 586. */
    public static final DisplayType MONEY_PKR =
            new DisplayType(SerializationUnits.MONEY, 586, MoneyUnit.PKR, "PKR", "Pakistani rupee");

    /** Money.PLN unit type with code 985. */
    public static final DisplayType MONEY_PLN =
            new DisplayType(SerializationUnits.MONEY, 985, MoneyUnit.PLN, "PLN", "Polish zloty");

    /** Money.PYG unit type with code 600. */
    public static final DisplayType MONEY_PYG =
            new DisplayType(SerializationUnits.MONEY, 600, MoneyUnit.PYG, "PYG", "Paraguayan guaraní");

    /** Money.QAR unit type with code 634. */
    public static final DisplayType MONEY_QAR =
            new DisplayType(SerializationUnits.MONEY, 634, MoneyUnit.QAR, "QAR", "Qatari riyal");

    /** Money.RON unit type with code 946. */
    public static final DisplayType MONEY_RON =
            new DisplayType(SerializationUnits.MONEY, 946, MoneyUnit.RON, "RON", "Romanian leu");

    /** Money.RSD unit type with code 941. */
    public static final DisplayType MONEY_RSD =
            new DisplayType(SerializationUnits.MONEY, 941, MoneyUnit.RSD, "RSD", "Serbian dinar");

    /** Money.RUB unit type with code 643. */
    public static final DisplayType MONEY_RUB =
            new DisplayType(SerializationUnits.MONEY, 643, MoneyUnit.RUB, "RUB", "Russian ruble");

    /** Money.RWF unit type with code 646. */
    public static final DisplayType MONEY_RWF =
            new DisplayType(SerializationUnits.MONEY, 646, MoneyUnit.RWF, "RWF", "Rwandan franc");

    /** Money.SAR unit type with code 682. */
    public static final DisplayType MONEY_SAR =
            new DisplayType(SerializationUnits.MONEY, 682, MoneyUnit.SAR, "SAR", "Saudi riyal");

    /** Money.SBD unit type with code 90. */
    public static final DisplayType MONEY_SBD =
            new DisplayType(SerializationUnits.MONEY, 90, MoneyUnit.SBD, "SBD", "Solomon Islands dollar");

    /** Money.SCR unit type with code 690. */
    public static final DisplayType MONEY_SCR =
            new DisplayType(SerializationUnits.MONEY, 690, MoneyUnit.SCR, "SCR", "Seychelles rupee");

    /** Money.SDG unit type with code 938. */
    public static final DisplayType MONEY_SDG =
            new DisplayType(SerializationUnits.MONEY, 938, MoneyUnit.SDG, "SDG", "Sudanese pound");

    /** Money.SEK unit type with code 752. */
    public static final DisplayType MONEY_SEK =
            new DisplayType(SerializationUnits.MONEY, 752, MoneyUnit.SEK, "SEK", "Swedish krona/kronor");

    /** Money.SGD unit type with code 702. */
    public static final DisplayType MONEY_SGD =
            new DisplayType(SerializationUnits.MONEY, 702, MoneyUnit.SGD, "SGD", "Singapore dollar");

    /** Money.SHP unit type with code 654. */
    public static final DisplayType MONEY_SHP =
            new DisplayType(SerializationUnits.MONEY, 654, MoneyUnit.SHP, "SHP", "Saint Helena pound");

    /** Money.SLL unit type with code 694. */
    public static final DisplayType MONEY_SLL =
            new DisplayType(SerializationUnits.MONEY, 694, MoneyUnit.SLL, "SLL", "Sierra Leonean leone");

    /** Money.SOS unit type with code 706. */
    public static final DisplayType MONEY_SOS =
            new DisplayType(SerializationUnits.MONEY, 706, MoneyUnit.SOS, "SOS", "Somali shilling");

    /** Money.SRD unit type with code 968. */
    public static final DisplayType MONEY_SRD =
            new DisplayType(SerializationUnits.MONEY, 968, MoneyUnit.SRD, "SRD", "Surinamese dollar");

    /** Money.SSP unit type with code 728. */
    public static final DisplayType MONEY_SSP =
            new DisplayType(SerializationUnits.MONEY, 728, MoneyUnit.SSP, "SSP", "South Sudanese pound");

    /** Money.STD unit type with code 678. */
    public static final DisplayType MONEY_STD =
            new DisplayType(SerializationUnits.MONEY, 678, MoneyUnit.STD, "STD", "São Tomé and Príncipe dobra");

    /** Money.SVC unit type with code 222. */
    public static final DisplayType MONEY_SVC =
            new DisplayType(SerializationUnits.MONEY, 222, MoneyUnit.SVC, "SVC", "Salvadoran colón");

    /** Money.SYP unit type with code 760. */
    public static final DisplayType MONEY_SYP =
            new DisplayType(SerializationUnits.MONEY, 760, MoneyUnit.SYP, "SYP", "Syrian pound");

    /** Money.SZL unit type with code 748. */
    public static final DisplayType MONEY_SZL =
            new DisplayType(SerializationUnits.MONEY, 748, MoneyUnit.SZL, "SZL", "Swazi lilangeni");

    /** Money.THB unit type with code 764. */
    public static final DisplayType MONEY_THB =
            new DisplayType(SerializationUnits.MONEY, 764, MoneyUnit.THB, "THB", "Thai baht");

    /** Money.TJS unit type with code 972. */
    public static final DisplayType MONEY_TJS =
            new DisplayType(SerializationUnits.MONEY, 972, MoneyUnit.TJS, "TJS", "Tajikistani somoni");

    /** Money.TMT unit type with code 934. */
    public static final DisplayType MONEY_TMT =
            new DisplayType(SerializationUnits.MONEY, 934, MoneyUnit.TMT, "TMT", "Turkmenistani manat");

    /** Money.TND unit type with code 788. */
    public static final DisplayType MONEY_TND =
            new DisplayType(SerializationUnits.MONEY, 788, MoneyUnit.TND, "TND", "Tunisian dinar");

    /** Money.TOP unit type with code 776. */
    public static final DisplayType MONEY_TOP =
            new DisplayType(SerializationUnits.MONEY, 776, MoneyUnit.TOP, "TOP", "Tongan pa?anga");

    /** Money.TRY unit type with code 949. */
    public static final DisplayType MONEY_TRY =
            new DisplayType(SerializationUnits.MONEY, 949, MoneyUnit.TRY, "TRY", "Turkish lira");

    /** Money.TTD unit type with code 780. */
    public static final DisplayType MONEY_TTD =
            new DisplayType(SerializationUnits.MONEY, 780, MoneyUnit.TTD, "TTD", "Trinidad and Tobago dollar");

    /** Money.TWD unit type with code 901. */
    public static final DisplayType MONEY_TWD =
            new DisplayType(SerializationUnits.MONEY, 901, MoneyUnit.TWD, "TWD", "New Taiwan dollar");

    /** Money.TZS unit type with code 834. */
    public static final DisplayType MONEY_TZS =
            new DisplayType(SerializationUnits.MONEY, 834, MoneyUnit.TZS, "TZS", "Tanzanian shilling");

    /** Money.UAH unit type with code 980. */
    public static final DisplayType MONEY_UAH =
            new DisplayType(SerializationUnits.MONEY, 980, MoneyUnit.UAH, "UAH", "Ukrainian hryvnia");

    /** Money.UGX unit type with code 800. */
    public static final DisplayType MONEY_UGX =
            new DisplayType(SerializationUnits.MONEY, 800, MoneyUnit.UGX, "UGX", "Ugandan shilling");

    /** Money.USD unit type with code 840. */
    public static final DisplayType MONEY_USD =
            new DisplayType(SerializationUnits.MONEY, 840, MoneyUnit.USD, "USD", "United States dollar");

    /** Money.USN unit type with code 997. */
    public static final DisplayType MONEY_USN = new DisplayType(SerializationUnits.MONEY, 997, MoneyUnit.USN, "USN",
            "United States dollar (next day) (funds code)");

    /** Money.UYI unit type with code 940. */
    public static final DisplayType MONEY_UYI = new DisplayType(SerializationUnits.MONEY, 940, MoneyUnit.UYI, "UYI",
            "Uruguay Peso en Unidades Indexadas (URUIURUI) (funds code)");

    /** Money.UYU unit type with code 858. */
    public static final DisplayType MONEY_UYU =
            new DisplayType(SerializationUnits.MONEY, 858, MoneyUnit.UYU, "UYU", "Uruguayan peso");

    /** Money.UZS unit type with code 860. */
    public static final DisplayType MONEY_UZS =
            new DisplayType(SerializationUnits.MONEY, 860, MoneyUnit.UZS, "UZS", "Uzbekistan som");

    /** Money.VEF unit type with code 937. */
    public static final DisplayType MONEY_VEF =
            new DisplayType(SerializationUnits.MONEY, 937, MoneyUnit.VEF, "VEF", "Venezuelan bolívar");

    /** Money.VND unit type with code 704. */
    public static final DisplayType MONEY_VND =
            new DisplayType(SerializationUnits.MONEY, 704, MoneyUnit.VND, "VND", "Vietnamese dong");

    /** Money.VUV unit type with code 548. */
    public static final DisplayType MONEY_VUV =
            new DisplayType(SerializationUnits.MONEY, 548, MoneyUnit.VUV, "VUV", "Vanuatu vatu");

    /** Money.WST unit type with code 882. */
    public static final DisplayType MONEY_WST =
            new DisplayType(SerializationUnits.MONEY, 882, MoneyUnit.WST, "WST", "Samoan tala");

    /** Money.XAF unit type with code 950. */
    public static final DisplayType MONEY_XAF =
            new DisplayType(SerializationUnits.MONEY, 950, MoneyUnit.XAF, "XAF", "CFA franc BEAC");

    /** Money.XAG unit type with code 961. */
    public static final DisplayType MONEY_XAG =
            new DisplayType(SerializationUnits.MONEY, 961, MoneyUnit.XAG, "XAG", "Silver (one troy ounce)");

    /** Money.XAU unit type with code 959. */
    public static final DisplayType MONEY_XAU =
            new DisplayType(SerializationUnits.MONEY, 959, MoneyUnit.XAU, "XAU", "Gold (one troy ounce)");

    /** Money.XBA unit type with code 955. */
    public static final DisplayType MONEY_XBA = new DisplayType(SerializationUnits.MONEY, 955, MoneyUnit.XBA, "XBA",
            "European Composite Unit (EURCO) (bond market unit)");

    /** Money.XBB unit type with code 956. */
    public static final DisplayType MONEY_XBB = new DisplayType(SerializationUnits.MONEY, 956, MoneyUnit.XBB, "XBB",
            "European Monetary Unit (E.M.U.-6) (bond market unit)");

    /** Money.XBC unit type with code 957. */
    public static final DisplayType MONEY_XBC = new DisplayType(SerializationUnits.MONEY, 957, MoneyUnit.XBC, "XBC",
            "European Unit of Account 9 (E.U.A.-9) (bond market unit)");

    /** Money.XBD unit type with code 958. */
    public static final DisplayType MONEY_XBD = new DisplayType(SerializationUnits.MONEY, 958, MoneyUnit.XBD, "XBD",
            "European Unit of Account 17 (E.U.A.-17) (bond market unit)");

    /** Money.XCD unit type with code 951. */
    public static final DisplayType MONEY_XCD =
            new DisplayType(SerializationUnits.MONEY, 951, MoneyUnit.XCD, "XCD", "East Caribbean dollar");

    /** Money.XDR unit type with code 960. */
    public static final DisplayType MONEY_XDR =
            new DisplayType(SerializationUnits.MONEY, 960, MoneyUnit.XDR, "XDR", "Special drawing rights");

    /** Money.XOF unit type with code 952. */
    public static final DisplayType MONEY_XOF =
            new DisplayType(SerializationUnits.MONEY, 952, MoneyUnit.XOF, "XOF", "CFA franc BCEAO");

    /** Money.XPD unit type with code 964. */
    public static final DisplayType MONEY_XPD =
            new DisplayType(SerializationUnits.MONEY, 964, MoneyUnit.XPD, "XPD", "Palladium (one troy ounce)");

    /** Money.XPF unit type with code 953. */
    public static final DisplayType MONEY_XPF =
            new DisplayType(SerializationUnits.MONEY, 953, MoneyUnit.XPF, "XPF", "CFP franc (franc Pacifique)");

    /** Money.XPT unit type with code 962. */
    public static final DisplayType MONEY_XPT =
            new DisplayType(SerializationUnits.MONEY, 962, MoneyUnit.XPT, "XPT", "Platinum (one troy ounce)");

    /** Money.XSU unit type with code 994. */
    public static final DisplayType MONEY_XSU =
            new DisplayType(SerializationUnits.MONEY, 994, MoneyUnit.XSU, "XSU", "SUCRE");

    /** Money.XTS unit type with code 963. */
    public static final DisplayType MONEY_XTS =
            new DisplayType(SerializationUnits.MONEY, 963, MoneyUnit.XTS, "XTS", "Code reserved for testing purposes");

    /** Money.XUA unit type with code 965. */
    public static final DisplayType MONEY_XUA =
            new DisplayType(SerializationUnits.MONEY, 965, MoneyUnit.XUA, "XUA", "ADB Unit of Account");

    /** Money.XX unit type with code 999. */
    public static final DisplayType MONEY_XXX =
            new DisplayType(SerializationUnits.MONEY, 999, MoneyUnit.XXX, "XXX", "No currency");

    /** Money.YER unit type with code 886. */
    public static final DisplayType MONEY_YER =
            new DisplayType(SerializationUnits.MONEY, 886, MoneyUnit.YER, "YER", "Yemeni rial");

    /** Money.ZAR unit type with code 710. */
    public static final DisplayType MONEY_ZAR =
            new DisplayType(SerializationUnits.MONEY, 710, MoneyUnit.ZAR, "ZAR", "South African rand");

    /** Money.ZMW unit type with code 967. */
    public static final DisplayType MONEY_ZMW =
            new DisplayType(SerializationUnits.MONEY, 967, MoneyUnit.ZMW, "ZMW", "Zambian kwacha");

    /** Money.ZWL unit type with code 932. */
    public static final DisplayType MONEY_ZWL =
            new DisplayType(SerializationUnits.MONEY, 932, MoneyUnit.ZWL, "ZWL", "Zimbabwean dollar A/10");

    /** Money.XBT unit type with code 1000. */
    public static final DisplayType MONEY_XBT =
            new DisplayType(SerializationUnits.MONEY, 1000, MoneyUnit.XBT, "XBT", "Bitcoin");

    /* =================================================== END TYPES ===================================================== */

    /**
     * @param unitType the corresponding 0MQ unit type
     * @param code the code of the unit provided as an int
     * @param djunitsType the djunits data type
     * @param name the unit name
     * @param abbreviation the unit abbreviation
     * @param <U> the unit
     */
    public <U extends Unit<U>> DisplayType(final SerializationUnits unitType, final int code, final U djunitsType,
            final String name, final String abbreviation)
    {
        super();
        this.unitType = unitType;
        this.code = code;
        this.djunitsType = djunitsType;
        this.name = name;
        this.abbreviation = abbreviation;
        Map<Integer, DisplayType> codeMap = codeDisplayTypeMap.get(this.unitType);
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
     * @param unitType UnitType; the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static DisplayType getDisplayType(final SerializationUnits unitType, final Integer code)
    {
        Map<Integer, DisplayType> byteMap = codeDisplayTypeMap.get(unitType);
        return byteMap == null ? null : byteMap.get(code);
    }

    /**
     * Return the display type belonging to the display code.
     * @param unitTypeCode the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static DisplayType getDisplayType(final byte unitTypeCode, final int code)
    {
        SerializationUnits unitType = SerializationUnits.getUnitType(unitTypeCode);
        Map<Integer, DisplayType> codeMap = codeDisplayTypeMap.get(unitType);
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
        SerializationUnits unitType = SerializationUnits.getUnitType(unitTypeCode);
        Map<Integer, DisplayType> codeMap = codeDisplayTypeMap.get(unitType);
        return codeMap == null ? null : codeMap.get(code) == null ? null : codeMap.get(code).djunitsType;
    }

    /**
     * Return the unit belonging to the display code.
     * @param unitType the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static Unit<?> getUnit(final SerializationUnits unitType, final int code)
    {
        Map<Integer, DisplayType> codeMap = codeDisplayTypeMap.get(unitType);
        return codeMap == null ? null : codeMap.get(code) == null ? null : codeMap.get(code).djunitsType;
    }

    /**
     * @return unitType
     */
    public SerializationUnits getUnitType()
    {
        return this.unitType;
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the unit
     */
    public static <U extends Unit<U>> DisplayType getDisplayType(final U unit)
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
        SerializationUnits type = SerializationUnits.getUnitType(unit);
        DisplayType displayType = type == null ? null : getDisplayType(unit);
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
        SerializationUnits type = SerializationUnits.getUnitType(unit);
        DisplayType displayType = type == null ? null : getDisplayType(unit);
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
