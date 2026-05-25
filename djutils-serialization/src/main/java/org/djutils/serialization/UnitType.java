package org.djutils.serialization;

import java.util.HashMap;
import java.util.Map;

import org.djunits.quantity.AbsorbedDose;
import org.djunits.quantity.Acceleration;
import org.djunits.quantity.AmountOfSubstance;
import org.djunits.quantity.Angle;
import org.djunits.quantity.AngularAcceleration;
import org.djunits.quantity.AngularVelocity;
import org.djunits.quantity.Area;
import org.djunits.quantity.CatalyticActivity;
import org.djunits.quantity.Density;
import org.djunits.quantity.Duration;
import org.djunits.quantity.ElectricCharge;
import org.djunits.quantity.ElectricCurrent;
import org.djunits.quantity.ElectricPotential;
import org.djunits.quantity.ElectricalCapacitance;
import org.djunits.quantity.ElectricalConductance;
import org.djunits.quantity.ElectricalInductance;
import org.djunits.quantity.ElectricalResistance;
import org.djunits.quantity.Energy;
import org.djunits.quantity.EquivalentDose;
import org.djunits.quantity.FlowMass;
import org.djunits.quantity.FlowVolume;
import org.djunits.quantity.Force;
import org.djunits.quantity.Frequency;
import org.djunits.quantity.Illuminance;
import org.djunits.quantity.Length;
import org.djunits.quantity.LinearDensity;
import org.djunits.quantity.LuminousFlux;
import org.djunits.quantity.LuminousIntensity;
import org.djunits.quantity.MagneticFlux;
import org.djunits.quantity.MagneticFluxDensity;
import org.djunits.quantity.Mass;
import org.djunits.quantity.Momentum;
import org.djunits.quantity.Power;
import org.djunits.quantity.Pressure;
import org.djunits.quantity.RadioActivity;
import org.djunits.quantity.SolidAngle;
import org.djunits.quantity.Speed;
import org.djunits.quantity.Temperature;
import org.djunits.quantity.Torque;
import org.djunits.quantity.Volume;
import org.djunits.unit.Unit;
import org.djunits.unit.Unitless;
import org.djunits.unit.Units;
import org.djunits.unit.system.UnitSystem;
import org.djutils.exceptions.Throw;

/**
 * Defined unit types.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public class UnitType
{
    /** map per quantity of code number to unit. */
    private static Map<QuantityType, Map<Integer, UnitType>> codeUnitMap = new HashMap<>();

    /** map of unit to unit type. */
    private static Map<Unit<?, ?>, UnitType> unitTypeMap = new HashMap<>();

    /** the code of the unit as a byte. */
    private final int code;

    /** the corresponding quantity. */
    private final QuantityType quantityType;

    /** the unit. */
    private final Unit<?, ?> unit;

    /** the unit name. */
    private final String name;

    /** the unit description. */
    private final String abbreviation;

    /* ================================================= DIMENSIONLESS ================================================= */

    /** Dimensionless.SI unit type with code 0. */
    public static final UnitType DIMENSIONLESS_SI = new UnitType(QuantityType.DIMENSIONLESS, 0, Unitless.BASE, "SI", "[]");

    /* ================================================= ACCELERATION ================================================== */

    /** Acceleration.METER_PER_SECOND_2 unit type with code 0. */
    public static final UnitType ACCELERATION_METER_PER_SECOND_2 =
            new UnitType(QuantityType.ACCELERATION, 0, Acceleration.Unit.m_s2, "METER_PER_SECOND_2", "m/s2");

    /** Acceleration.KM_PER_HOUR_2 unit type with code 1. */
    public static final UnitType ACCELERATION_KM_PER_HOUR_2 =
            new UnitType(QuantityType.ACCELERATION, 1, Acceleration.Unit.km_h2, "KM_PER_HOUR_2", "km/h2");

    /** Acceleration.INCH_PER_SECOND_2 unit type with code 2. */
    public static final UnitType ACCELERATION_INCH_PER_SECOND_2 =
            new UnitType(QuantityType.ACCELERATION, 2, Acceleration.Unit.in_s2, "INCH_PER_SECOND_2", "in/s2");

    /** Acceleration.FOOT_PER_SECOND_2 unit type with code 3. */
    public static final UnitType ACCELERATION_FOOT_PER_SECOND_2 =
            new UnitType(QuantityType.ACCELERATION, 3, Acceleration.Unit.ft_s2, "FOOT_PER_SECOND_2", "ft/s2");

    /** Acceleration.MILE_PER_HOUR_2 unit type with code 4. */
    public static final UnitType ACCELERATION_MILE_PER_HOUR_2 =
            new UnitType(QuantityType.ACCELERATION, 4, Acceleration.Unit.mi_h2, "MILE_PER_HOUR_2", "mi/h2");

    /** Acceleration.MILE_PER_HOUR_PER_SECOND unit type with code 5. */
    public static final UnitType ACCELERATION_MILE_PER_HOUR_PER_SECOND =
            new UnitType(QuantityType.ACCELERATION, 5, Acceleration.Unit.mi_h_s, "MILE_PER_HOUR_PER_SECOND", "mi/h/s");

    /** Acceleration.KNOT_PER_SECOND unit type with code 6. */
    public static final UnitType ACCELERATION_KNOT_PER_SECOND =
            new UnitType(QuantityType.ACCELERATION, 6, Acceleration.Unit.kt_s, "KNOT_PER_SECOND", "kt/s");

    /** Acceleration.GAL unit type with code 7. */
    public static final UnitType ACCELERATION_GAL =
            new UnitType(QuantityType.ACCELERATION, 7, Acceleration.Unit.Gal, "GAL", "Gal");

    /** Acceleration.STANDARD_GRAVITY unit type with code 8. */
    public static final UnitType ACCELERATION_STANDARD_GRAVITY =
            new UnitType(QuantityType.ACCELERATION, 8, Acceleration.Unit.g, "STANDARD_GRAVITY", "g");

    /** Acceleration.MILE_PER_SECOND_2 unit type with code 9. */
    public static final UnitType ACCELERATION_MILE_PER_SECOND_2 =
            new UnitType(QuantityType.ACCELERATION, 9, Acceleration.Unit.mi_s2, "MILE_PER_SECOND_2", "mi/s2");

    /* ================================================== SOLIDANGLE =================================================== */

    /** SolidAngle.STERADIAN unit type with code 0. */
    public static final UnitType SOLIDANGLE_STERADIAN =
            new UnitType(QuantityType.SOLIDANGLE, 0, SolidAngle.Unit.sr, "STERADIAN", "sr");

    /** SolidAngle.SQUARE_DEGREE unit type with code 1. */
    public static final UnitType SOLIDANGLE_SQUARE_DEGREE =
            new UnitType(QuantityType.SOLIDANGLE, 1, SolidAngle.Unit.sq_deg, "SQUARE_DEGREE", "sq.deg");

    /* ===================================================== ANGLE ===================================================== */

    /** Angle.RADIAN unit type with code 0. */
    public static final UnitType ANGLE_RADIAN = new UnitType(QuantityType.ANGLE, 0, Angle.Unit.rad, "RADIAN", "rad");

    /** Angle.ARCMINUTE unit type with code 1. */
    public static final UnitType ANGLE_ARCMINUTE =
            new UnitType(QuantityType.ANGLE, 1, Angle.Unit.arcmin, "ARCMINUTE", "arcmin");

    /** Angle.ARCSECOND unit type with code 2. */
    public static final UnitType ANGLE_ARCSECOND =
            new UnitType(QuantityType.ANGLE, 2, Angle.Unit.arcsec, "ARCSECOND", "arcsec");

    /** Angle.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final UnitType ANGLE_CENTESIMAL_ARCMINUTE =
            new UnitType(QuantityType.ANGLE, 3, Angle.Unit.cdm, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Angle.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final UnitType ANGLE_CENTESIMAL_ARCSECOND =
            new UnitType(QuantityType.ANGLE, 4, Angle.Unit.cds, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Angle.DEGREE unit type with code 5. */
    public static final UnitType ANGLE_DEGREE = new UnitType(QuantityType.ANGLE, 5, Angle.Unit.deg, "DEGREE", "deg");

    /** Angle.GRAD unit type with code 6. */
    public static final UnitType ANGLE_GRAD = new UnitType(QuantityType.ANGLE, 6, Angle.Unit.grad, "GRAD", "grad");

    /** Angle.PERCENT unit type with code 7. */
    public static final UnitType ANGLE_PERCENT = new UnitType(QuantityType.ANGLE, 7, Angle.Unit.percent, "PERCENT", "%");

    /* =================================================== DIRECTION =================================================== */

    /** Direction.RADIAN unit type with code 0. */
    public static final UnitType DIRECTION_RADIAN = new UnitType(QuantityType.DIRECTION, 0, Angle.Unit.rad, "RADIAN", "rad");

    /** Direction.ARCMINUTE unit type with code 1. */
    public static final UnitType DIRECTION_ARCMINUTE =
            new UnitType(QuantityType.DIRECTION, 1, Angle.Unit.arcmin, "ARCMINUTE", "arcmin");

    /** Direction.ARCSECOND unit type with code 2. */
    public static final UnitType DIRECTION_ARCSECOND =
            new UnitType(QuantityType.DIRECTION, 2, Angle.Unit.arcsec, "ARCSECOND", "arcsec");

    /** Direction.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final UnitType DIRECTION_CENTESIMAL_ARCMINUTE =
            new UnitType(QuantityType.DIRECTION, 3, Angle.Unit.cdm, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Direction.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final UnitType DIRECTION_CENTESIMAL_ARCSECOND =
            new UnitType(QuantityType.DIRECTION, 4, Angle.Unit.cds, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Direction.DEGREE unit type with code 5. */
    public static final UnitType DIRECTION_DEGREE = new UnitType(QuantityType.DIRECTION, 5, Angle.Unit.deg, "DEGREE", "deg");

    /** Direction.GRAD unit type with code 6. */
    public static final UnitType DIRECTION_GRAD = new UnitType(QuantityType.DIRECTION, 6, Angle.Unit.grad, "GRAD", "grad");

    /** Direction.PERCENT unit type with code 7. */
    public static final UnitType DIRECTION_PERCENT =
            new UnitType(QuantityType.DIRECTION, 7, Angle.Unit.percent, "PERCENT", "%");

    /* ===================================================== AREA ====================================================== */

    /** Area.SQUARE_METER unit type with code 0. */
    public static final UnitType AREA_SQUARE_METER = new UnitType(QuantityType.AREA, 0, Area.Unit.m2, "SQUARE_METER", "m2");

    /** Area.SQUARE_ATTOMETER unit type with code 1. */
    public static final UnitType AREA_SQUARE_ATTOMETER =
            new UnitType(QuantityType.AREA, 1, Area.Unit.am2, "SQUARE_ATTOMETER", "am2");

    /** Area.SQUARE_FEMTOMETER unit type with code 2. */
    public static final UnitType AREA_SQUARE_FEMTOMETER =
            new UnitType(QuantityType.AREA, 2, Area.Unit.fm2, "SQUARE_FEMTOMETER", "fm2");

    /** Area.SQUARE_PICOMETER unit type with code 3. */
    public static final UnitType AREA_SQUARE_PICOMETER =
            new UnitType(QuantityType.AREA, 3, Area.Unit.pm2, "SQUARE_PICOMETER", "pm2");

    /** Area.SQUARE_NANOMETER unit type with code 4. */
    public static final UnitType AREA_SQUARE_NANOMETER =
            new UnitType(QuantityType.AREA, 4, Area.Unit.nm2, "SQUARE_NANOMETER", "nm2");

    /** Area.SQUARE_MICROMETER unit type with code 5. */
    public static final UnitType AREA_SQUARE_MICROMETER =
            new UnitType(QuantityType.AREA, 5, Area.Unit.mum2, "SQUARE_MICROMETER", "μm2");

    /** Area.SQUARE_MILLIMETER unit type with code 6. */
    public static final UnitType AREA_SQUARE_MILLIMETER =
            new UnitType(QuantityType.AREA, 6, Area.Unit.mm2, "SQUARE_MILLIMETER", "mm2");

    /** Area.SQUARE_CENTIMETER unit type with code 7. */
    public static final UnitType AREA_SQUARE_CENTIMETER =
            new UnitType(QuantityType.AREA, 7, Area.Unit.cm2, "SQUARE_CENTIMETER", "cm2");

    /** Area.SQUARE_DECIMETER unit type with code 8. */
    public static final UnitType AREA_SQUARE_DECIMETER =
            new UnitType(QuantityType.AREA, 8, Area.Unit.dm2, "SQUARE_DECIMETER", "dm2");

    /** Area.SQUARE_DEKAMETER unit type with code 9. */
    public static final UnitType AREA_SQUARE_DEKAMETER =
            new UnitType(QuantityType.AREA, 9, Area.Unit.dam2, "SQUARE_DEKAMETER", "dam2");

    /** Area.SQUARE_HECTOMETER unit type with code 10. */
    public static final UnitType AREA_SQUARE_HECTOMETER =
            new UnitType(QuantityType.AREA, 10, Area.Unit.hm2, "SQUARE_HECTOMETER", "hm2");

    /** Area.SQUARE_KILOMETER unit type with code 11. */
    public static final UnitType AREA_SQUARE_KILOMETER =
            new UnitType(QuantityType.AREA, 11, Area.Unit.km2, "SQUARE_KILOMETER", "km2");

    /** Area.SQUARE_MEGAMETER unit type with code 12. */
    public static final UnitType AREA_SQUARE_MEGAMETER =
            new UnitType(QuantityType.AREA, 12, Units.resolve(Area.Unit.class, "Mm2"), "SQUARE_MEGAMETER", "Mm2");

    /** Area.SQUARE_INCH unit type with code 13. */
    public static final UnitType AREA_SQUARE_INCH = new UnitType(QuantityType.AREA, 13, Area.Unit.in2, "SQUARE_INCH", "in2");

    /** Area.SQUARE_FOOT unit type with code 14. */
    public static final UnitType AREA_SQUARE_FOOT = new UnitType(QuantityType.AREA, 14, Area.Unit.ft2, "SQUARE_FOOT", "ft2");

    /** Area.SQUARE_YARD unit type with code 15. */
    public static final UnitType AREA_SQUARE_YARD = new UnitType(QuantityType.AREA, 15, Area.Unit.yd2, "SQUARE_YARD", "yd2");

    /** Area.SQUARE_MILE unit type with code 16. */
    public static final UnitType AREA_SQUARE_MILE = new UnitType(QuantityType.AREA, 16, Area.Unit.mi2, "SQUARE_MILE", "mi2");

    /** Area.SQUARE_NAUTICAL_MILE unit type with code 17. */
    public static final UnitType AREA_SQUARE_NAUTICAL_MILE =
            new UnitType(QuantityType.AREA, 17, Area.Unit.NM2, "SQUARE_NAUTICAL_MILE", "NM2");

    /** Area.ACRE unit type with code 18. */
    public static final UnitType AREA_ACRE = new UnitType(QuantityType.AREA, 18, Area.Unit.ac, "ACRE", "acre");

    /** Area.ARE unit type with code 19. */
    public static final UnitType AREA_ARE = new UnitType(QuantityType.AREA, 19, Area.Unit.a, "ARE", "a");

    /** Area.CENTIARE unit type with code 20. */
    public static final UnitType AREA_CENTIARE = new UnitType(QuantityType.AREA, 20, Area.Unit.ca, "CENTIARE", "ca");

    /** Area.HECTARE unit type with code 21. */
    public static final UnitType AREA_HECTARE = new UnitType(QuantityType.AREA, 21, Area.Unit.ha, "HECTARE", "ha");

    /* ==================================================== DENSITY ==================================================== */

    /** Density.KG_PER_METER_3 unit type with code 0. */
    public static final UnitType DENSITY_KG_PER_METER_3 =
            new UnitType(QuantityType.DENSITY, 0, Density.Unit.kg_m3, "KG_PER_METER_3", "kg/m3");

    /** Density.GRAM_PER_CENTIMETER_3 unit type with code 1. */
    public static final UnitType DENSITY_GRAM_PER_CENTIMETER_3 =
            new UnitType(QuantityType.DENSITY, 1, Density.Unit.g_cm3, "GRAM_PER_CENTIMETER_3", "g/cm3");

    /* =============================================== ELECTRICALCHARGE ================================================ */

    /** ElectricCharge.COULOMB unit type with code 0. */
    public static final UnitType ELECTRICCHARGE_COULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 0, ElectricCharge.Unit.C, "COULOMB", "C");

    /** ElectricalCharge.PICOCOULOMB unit type with code 1. */
    public static final UnitType ELECTRICCHARGE_PICOCOULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 1, Units.resolve(ElectricCharge.Unit.class, "pC"), "PICOCOULOMB", "pC");

    /** ElectricCharge.NANOCOULOMB unit type with code 2. */
    public static final UnitType ELECTRICCHARGE_NANOCOULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 2, Units.resolve(ElectricCharge.Unit.class, "nC"), "NANOCOULOMB", "nC");

    /** ElectricCharge.MICROCOULOMB unit type with code 3. */
    public static final UnitType ELECTRICCHARGE_MICROCOULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 3, ElectricCharge.Unit.muC, "MICROCOULOMB", "μC");

    /** ElectricCharge.MILLICOULOMB unit type with code 4. */
    public static final UnitType ELECTRICCHARGE_MILLICOULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 4, ElectricCharge.Unit.mC, "MILLICOULOMB", "mC");

    /** ElectricCharge.ABCOULOMB unit type with code 5. */
    public static final UnitType ELECTRICCHARGE_ABCOULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 5, ElectricCharge.Unit.abC, "ABCOULOMB", "abC");

    /** ElectricCharge.ATOMIC_UNIT unit type with code 6. */
    public static final UnitType ELECTRICCHARGE_ATOMIC_UNIT =
            new UnitType(QuantityType.ELECTRICCHARGE, 6, ElectricCharge.Unit.e, "ATOMIC_UNIT", "au");

    /** ElectricCharge.EMU unit type with code 7. */
    public static final UnitType ELECTRICCHARGE_EMU =
            new UnitType(QuantityType.ELECTRICCHARGE, 7, ElectricCharge.Unit.emu, "EMU", "emu");

    /** ElectricCharge.ESU unit type with code 8. */
    public static final UnitType ELECTRICCHARGE_ESU =
            new UnitType(QuantityType.ELECTRICCHARGE, 8, ElectricCharge.Unit.esu, "ESU", "esu");

    /** ElectricCharge.FARADAY unit type with code 9. */
    public static final UnitType ELECTRICCHARGE_FARADAY =
            new UnitType(QuantityType.ELECTRICCHARGE, 9, ElectricCharge.Unit.F, "FARADAY", "F");

    /** ElectricCharge.FRANKLIN unit type with code 10. */
    public static final UnitType ELECTRICCHARGE_FRANKLIN =
            new UnitType(QuantityType.ELECTRICCHARGE, 10, ElectricCharge.Unit.Fr, "FRANKLIN  ", "Fr");

    /** ElectricCharge.STATCOULOMB unit type with code 11. */
    public static final UnitType ELECTRICCHARGE_STATCOULOMB =
            new UnitType(QuantityType.ELECTRICCHARGE, 11, ElectricCharge.Unit.statC, "STATCOULOMB", "statC");

    /** ElectricCharge.MILLIAMPERE_HOUR unit type with code 12. */
    public static final UnitType ELECTRICCHARGE_MILLIAMPERE_HOUR =
            new UnitType(QuantityType.ELECTRICCHARGE, 12, ElectricCharge.Unit.mAh, "MILLIAMPERE_HOUR", "mAh");

    /** ElectricCharge.AMPERE_HOUR unit type with code 13. */
    public static final UnitType ELECTRICCHARGE_AMPERE_HOUR =
            new UnitType(QuantityType.ELECTRICCHARGE, 13, ElectricCharge.Unit.Ah, "AMPERE_HOUR", "Ah");

    /** ElectricCharge.KILOAMPERE_HOUR unit type with code 14. */
    public static final UnitType ELECTRICCHARGE_KILOAMPERE_HOUR =
            new UnitType(QuantityType.ELECTRICCHARGE, 14, ElectricCharge.Unit.kAh, "KILOAMPERE_HOUR", "kAh");

    /** ElectricCharge.MEGAAMPERE_HOUR unit type with code 15. */
    public static final UnitType ELECTRICCHARGE_MEGAAMPERE_HOUR =
            new UnitType(QuantityType.ELECTRICCHARGE, 15, ElectricCharge.Unit.MAh, "MEGAAMPERE_HOUR", "MAh");

    /** ElectricCharge.MILLIAMPERE_SECOND unit type with code 16. */
    public static final UnitType ELECTRICCHARGE_MILLIAMPERE_SECOND =
            new UnitType(QuantityType.ELECTRICCHARGE, 16, ElectricCharge.Unit.mAs, "MILLIAMPERE_SECOND", "mAs");

    /* ============================================= ELECTRICCURRENT ================================================= */

    /** ElectricCurrent.AMPERE unit type with code 0. */
    public static final UnitType ELECTRICCURRENT_AMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 0, ElectricCurrent.Unit.A, "AMPERE", "A");

    /** ElectricCurrent.NANOAMPERE unit type with code 1. */
    public static final UnitType ELECTRICCURRENT_NANOAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 1, Units.resolve(ElectricCurrent.Unit.class, "nA"), "NANOAMPERE", "nA");

    /** ElectricCurrent.MICROAMPERE unit type with code 2. */
    public static final UnitType ELECTRICCURRENT_MICROAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 2, ElectricCurrent.Unit.muA, "MICROAMPERE", "μA");

    /** ElectricCurrent.MILLIAMPERE unit type with code 3. */
    public static final UnitType ELECTRICCURRENT_MILLIAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 3, ElectricCurrent.Unit.mA, "MILLIAMPERE", "mA");

    /** ElectricCurrent.KILOAMPERE unit type with code 4. */
    public static final UnitType ELECTRICCURRENT_KILOAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 4, ElectricCurrent.Unit.kA, "KILOAMPERE", "kA");

    /** ElectricCurrent.MEGAAMPERE unit type with code 5. */
    public static final UnitType ELECTRICCURRENT_MEGAAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 5, ElectricCurrent.Unit.MA, "MEGAAMPERE", "MA");

    /** ElectricCurrent.ABAMPERE unit type with code 6. */
    public static final UnitType ELECTRICCURRENT_ABAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 6, ElectricCurrent.Unit.abA, "ABAMPERE", "abA");

    /** ElectricCurrent.STATAMPERE unit type with code 7. */
    public static final UnitType ELECTRICCURRENT_STATAMPERE =
            new UnitType(QuantityType.ELECTRICCURRENT, 7, ElectricCurrent.Unit.statA, "STATAMPERE", "statA");

    /* ============================================ ELECTRICPOTENTIAL ================================================ */

    /** ElectricPotential.VOLT unit type with code 0. */
    public static final UnitType ELECTRICPOTENTIAL_VOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 0, ElectricPotential.Unit.V, "VOLT", "V");

    /** ElectricPotential.NANOVOLT unit type with code 1. */
    public static final UnitType ELECTRICPOTENTIAL_NANOVOLT = new UnitType(QuantityType.ELECTRICPOTENTIAL, 1,
            Units.resolve(ElectricPotential.Unit.class, "nV"), "NANOVOLT", "nV");

    /** ElectricPotential.MICROVOLT unit type with code 2. */
    public static final UnitType ELECTRICPOTENTIAL_MICROVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 2, ElectricPotential.Unit.muV, "MICROVOLT", "μV");

    /** ElectricPotential.MILLIVOLT unit type with code 3. */
    public static final UnitType ELECTRICPOTENTIAL_MILLIVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 3, ElectricPotential.Unit.mV, "MILLIVOLT", "mV");

    /** ElectricPotential.KILOVOLT unit type with code 4. */
    public static final UnitType ELECTRICPOTENTIAL_KILOVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 4, ElectricPotential.Unit.kV, "KILOVOLT", "kV");

    /** ElectricPotential.MEGAVOLT unit type with code 5. */
    public static final UnitType ELECTRICPOTENTIAL_MEGAVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 5, ElectricPotential.Unit.MV, "MEGAVOLT", "MV");

    /** ElectricPotential.GIGAVOLT unit type with code 6. */
    public static final UnitType ELECTRICPOTENTIAL_GIGAVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 6, ElectricPotential.Unit.GV, "GIGAVOLT", "GV");

    /** ElectricPotential.ABVOLT unit type with code 7. */
    public static final UnitType ELECTRICPOTENTIAL_ABVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 7, ElectricPotential.Unit.abV, "ABVOLT", "abV");

    /** ElectricPotential.STATVOLT unit type with code 8. */
    public static final UnitType ELECTRICPOTENTIAL_STATVOLT =
            new UnitType(QuantityType.ELECTRICPOTENTIAL, 8, ElectricPotential.Unit.statV, "STATVOLT", "statV");

    /* =========================================== ELECTRICALRESISTANCE ================================================ */

    /** ElectricalResistance.OHM unit type with code 0. */
    public static final UnitType ELECTRICALRESISTANCE_OHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 0, ElectricalResistance.Unit.ohm, "OHM", "Ω");

    /** ElectricalResistance.NANOOHM unit type with code 1. */
    public static final UnitType ELECTRICALRESISTANCE_NANOOHM = new UnitType(QuantityType.ELECTRICALRESISTANCE, 1,
            Units.resolve(ElectricalResistance.Unit.class, "nohm"), "NANOOHM", "nΩ");

    /** ElectricalResistance.MICROOHM unit type with code 2. */
    public static final UnitType ELECTRICALRESISTANCE_MICROOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 2, ElectricalResistance.Unit.muohm, "MICROOHM", "μΩ");

    /** ElectricalResistance.MILLIOHM unit type with code 3. */
    public static final UnitType ELECTRICALRESISTANCE_MILLIOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 3, ElectricalResistance.Unit.mohm, "MILLIOHM", "mΩ");

    /** ElectricalResistance.KILOOHM unit type with code 4. */
    public static final UnitType ELECTRICALRESISTANCE_KILOOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 4, ElectricalResistance.Unit.kohm, "KILOOHM", "kΩ");

    /** ElectricalResistance.MEGAOHM unit type with code 5. */
    public static final UnitType ELECTRICALRESISTANCE_MEGAOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 5, ElectricalResistance.Unit.Mohm, "MEGAOHM", "MΩ");

    /** ElectricalResistance.GIGAOHM unit type with code 6. */
    public static final UnitType ELECTRICALRESISTANCE_GIGAOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 6, ElectricalResistance.Unit.Gohm, "GIGAOHM", "GΩ");

    /** ElectricalResistance.ABOHM unit type with code 7. */
    public static final UnitType ELECTRICALRESISTANCE_ABOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 7, ElectricalResistance.Unit.abohm, "ABOHM", "abΩ");

    /** ElectricalResistance.STATOHM unit type with code 8. */
    public static final UnitType ELECTRICALRESISTANCE_STATOHM =
            new UnitType(QuantityType.ELECTRICALRESISTANCE, 8, ElectricalResistance.Unit.statohm, "STATOHM", "statΩ");

    /* ==================================================== ENERGY ===================================================== */

    /** Energy.JOULE unit type with code 0. */
    public static final UnitType ENERGY_JOULE = new UnitType(QuantityType.ENERGY, 0, Energy.Unit.J, "JOULE", "J");

    /** Energy.PICOJOULE unit type with code 1. */
    public static final UnitType ENERGY_PICOJOULE =
            new UnitType(QuantityType.ENERGY, 1, Units.resolve(Energy.Unit.class, "pJ"), "PICOJOULE", "pJ");

    /** Energy.NANOJOULE unit type with code 2. */
    public static final UnitType ENERGY_NANOJOULE =
            new UnitType(QuantityType.ENERGY, 2, Units.resolve(Energy.Unit.class, "nJ"), "NANOJOULE", "nJ");

    /** Energy.MICROJOULE unit type with code 3. */
    public static final UnitType ENERGY_MICROJOULE = new UnitType(QuantityType.ENERGY, 3, Energy.Unit.muJ, "MICROJOULE", "μJ");

    /** Energy.MILLIJOULE unit type with code 4. */
    public static final UnitType ENERGY_MILLIJOULE = new UnitType(QuantityType.ENERGY, 4, Energy.Unit.mJ, "MILLIJOULE", "mJ");

    /** Energy.KILOJOULE unit type with code 5. */
    public static final UnitType ENERGY_KILOJOULE = new UnitType(QuantityType.ENERGY, 5, Energy.Unit.kJ, "KILOJOULE", "kJ");

    /** Energy.MEGAJOULE unit type with code 6. */
    public static final UnitType ENERGY_MEGAJOULE = new UnitType(QuantityType.ENERGY, 6, Energy.Unit.MJ, "MEGAJOULE", "MJ");

    /** Energy.GIGAJOULE unit type with code 7. */
    public static final UnitType ENERGY_GIGAJOULE = new UnitType(QuantityType.ENERGY, 7, Energy.Unit.GJ, "GIGAJOULE", "GJ");

    /** Energy.TERAJOULE unit type with code 8. */
    public static final UnitType ENERGY_TERAJOULE = new UnitType(QuantityType.ENERGY, 8, Energy.Unit.TJ, "TERAJOULE", "TJ");

    /** Energy.PETAJOULE unit type with code 9. */
    public static final UnitType ENERGY_PETAJOULE = new UnitType(QuantityType.ENERGY, 9, Energy.Unit.PJ, "PETAJOULE", "PJ");

    /** Energy.ELECTRONVOLT unit type with code 10. */
    public static final UnitType ENERGY_ELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 10, Energy.Unit.eV, "ELECTRONVOLT", "eV");

    /** Energy.MICROELECTRONVOLT unit type with code 11. */
    public static final UnitType ENERGY_MICROELECTRONVOLT = new UnitType(QuantityType.ENERGY, 11,
            Energy.Unit.eV.deriveUnit("mueV", "microelectronvolt", 1E-6, UnitSystem.SI_ACCEPTED), "MICROELECTRONVOLT", "μeV");

    /** Energy.MILLIELECTRONVOLT unit type with code 12. */
    public static final UnitType ENERGY_MILLIELECTRONVOLT = new UnitType(QuantityType.ENERGY, 12,
            Energy.Unit.eV.deriveUnit("meV", "millielectronvolt", 1E-3, UnitSystem.SI_ACCEPTED), "MILLIELECTRONVOLT", "meV");

    /** Energy.KILOELECTRONVOLT unit type with code 13. */
    public static final UnitType ENERGY_KILOELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 13, Energy.Unit.keV, "KILOELECTRONVOLT", "keV");

    /** Energy.MEGAELECTRONVOLT unit type with code 14. */
    public static final UnitType ENERGY_MEGAELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 14, Energy.Unit.MeV, "MEGAELECTRONVOLT", "MeV");

    /** Energy.GIGAELECTRONVOLT unit type with code 15. */
    public static final UnitType ENERGY_GIGAELECTRONVOLT =
            new UnitType(QuantityType.ENERGY, 15, Energy.Unit.GeV, "GIGAELECTRONVOLT", "GeV");

    /** Energy.TERAELECTRONVOLT unit type with code 16. */
    public static final UnitType ENERGY_TERAELECTRONVOLT = new UnitType(QuantityType.ENERGY, 16,
            Energy.Unit.eV.deriveUnit("TeV", "teraelectronvolt", 1E12, UnitSystem.SI_ACCEPTED), "TERAELECTRONVOLT", "TeV");

    /** Energy.PETAELECTRONVOLT unit type with code 17. */
    public static final UnitType ENERGY_PETAELECTRONVOLT = new UnitType(QuantityType.ENERGY, 17,
            Energy.Unit.eV.deriveUnit("PeV", "petaelectronvolt", 1E15, UnitSystem.SI_ACCEPTED), "PETAELECTRONVOLT", "PeV");

    /** Energy.EXAELECTRONVOLT unit type with code 18. */
    public static final UnitType ENERGY_EXAELECTRONVOLT = new UnitType(QuantityType.ENERGY, 18,
            Energy.Unit.eV.deriveUnit("EeV", "exaelectronvolt", 1E18, UnitSystem.SI_ACCEPTED), "EXAELECTRONVOLT", "EeV");

    /** Energy.WATT_HOUR unit type with code 19. */
    public static final UnitType ENERGY_WATT_HOUR = new UnitType(QuantityType.ENERGY, 19, Energy.Unit.Wh, "WATT_HOUR", "Wh");

    /** Energy.FEMTOWATT_HOUR unit type with code 20. */
    public static final UnitType ENERGY_FEMTOWATT_HOUR = new UnitType(QuantityType.ENERGY, 20,
            Energy.Unit.Wh.deriveUnit("fWh", "femtowatthour", 1E-15, UnitSystem.SI_ACCEPTED), "FEMTOWATT_HOUR", "fWh");

    /** Energy.PICOWATT_HOUR unit type with code 21. */
    public static final UnitType ENERGY_PICOWATT_HOUR = new UnitType(QuantityType.ENERGY, 21,
            Energy.Unit.Wh.deriveUnit("pWh", "picowatthour", 1E-12, UnitSystem.SI_ACCEPTED), "PICOWATT_HOUR", "pWh");

    /** Energy.NANOWATT_HOUR unit type with code 22. */
    public static final UnitType ENERGY_NANOWATT_HOUR = new UnitType(QuantityType.ENERGY, 22,
            Energy.Unit.Wh.deriveUnit("nWh", "nanowatthour", 1E-9, UnitSystem.SI_ACCEPTED), "NANOWATT_HOUR", "nWh");

    /** Energy.MICROWATT_HOUR unit type with code 23. */
    public static final UnitType ENERGY_MICROWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 23, Energy.Unit.muWh, "MICROWATT_HOUR", "μWh");

    /** Energy.MILLIWATT_HOUR unit type with code 24. */
    public static final UnitType ENERGY_MILLIWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 24, Energy.Unit.mWh, "MILLIWATT_HOUR", "mWh");

    /** Energy.KILOWATT_HOUR unit type with code 25. */
    public static final UnitType ENERGY_KILOWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 25, Energy.Unit.kWh, "KILOWATT_HOUR", "kWh");

    /** Energy.MEGAWATT_HOUR unit type with code 26. */
    public static final UnitType ENERGY_MEGAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 26, Energy.Unit.MWh, "MEGAWATT_HOUR", "MWh");

    /** Energy.GIGAWATT_HOUR unit type with code 27. */
    public static final UnitType ENERGY_GIGAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 27, Energy.Unit.GWh, "GIGAWATT_HOUR", "GWh");

    /** Energy.TERAWATT_HOUR unit type with code 28. */
    public static final UnitType ENERGY_TERAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 28, Energy.Unit.TWh, "TERAWATT_HOUR", "TWh");

    /** Energy.PETAWATT_HOUR unit type with code 29. */
    public static final UnitType ENERGY_PETAWATT_HOUR =
            new UnitType(QuantityType.ENERGY, 29, Energy.Unit.PWh, "PETAWATT_HOUR", "PWh");

    /** Energy.CALORIE unit type with code 30. */
    public static final UnitType ENERGY_CALORIE = new UnitType(QuantityType.ENERGY, 30, Energy.Unit.cal, "CALORIE", "cal");

    /** Energy.KILOCALORIE unit type with code 31. */
    public static final UnitType ENERGY_KILOCALORIE =
            new UnitType(QuantityType.ENERGY, 31, Energy.Unit.kcal, "KILOCALORIE", "kcal");

    /** Energy.CALORIE_IT unit type with code 32. */
    public static final UnitType ENERGY_CALORIE_IT =
            new UnitType(QuantityType.ENERGY, 32, Energy.Unit.cal_IT, "CALORIE_IT", "cal(IT)");

    /** Energy.INCH_POUND_FORCE unit type with code 33. */
    public static final UnitType ENERGY_INCH_POUND_FORCE =
            new UnitType(QuantityType.ENERGY, 33, Energy.Unit.in_lbf, "INCH_POUND_FORCE", "in lbf");

    /** Energy.FOOT_POUND_FORCE unit type with code 34. */
    public static final UnitType ENERGY_FOOT_POUND_FORCE =
            new UnitType(QuantityType.ENERGY, 34, Energy.Unit.ft_lbf, "FOOT_POUND_FORCE", "ft lbf");

    /** Energy.ERG unit type with code 35. */
    public static final UnitType ENERGY_ERG = new UnitType(QuantityType.ENERGY, 35, Energy.Unit.erg, "ERG", "erg");

    /** Energy.BTU_ISO unit type with code 36. */
    public static final UnitType ENERGY_BTU_ISO =
            new UnitType(QuantityType.ENERGY, 36, Energy.Unit.BTU_ISO, "BTU_ISO", "BTU(ISO)");

    /** Energy.BTU_IT unit type with code 37. */
    public static final UnitType ENERGY_BTU_IT = new UnitType(QuantityType.ENERGY, 37, Energy.Unit.BTU_IT, "BTU_IT", "BTU(IT)");

    /** Energy.STHENE_METER unit type with code 38. */
    public static final UnitType ENERGY_STHENE_METER =
            new UnitType(QuantityType.ENERGY, 38, Energy.Unit.sn_m, "STHENE_METER", "sth.m");

    /* =================================================== FLOWMASS ==================================================== */

    /** FlowMass.KG_PER_SECOND unit type with code 0. */
    public static final UnitType FLOWMASS_KG_PER_SECOND =
            new UnitType(QuantityType.FLOWMASS, 0, FlowMass.Unit.kg_s, "KG_PER_SECOND", "kg/s");

    /** FlowMass.POUND_PER_SECOND unit type with code 1. */
    public static final UnitType FLOWMASS_POUND_PER_SECOND =
            new UnitType(QuantityType.FLOWMASS, 1, FlowMass.Unit.lb_s, "POUND_PER_SECOND", "lb/s");

    /* ================================================== FLOWVOLUME =================================================== */

    /** FlowVolume.CUBIC_METER_PER_SECOND unit type with code 0. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_SECOND =
            new UnitType(QuantityType.FLOWVOLUME, 0, FlowVolume.Unit.m3_s, "CUBIC_METER_PER_SECOND", "m3/s");

    /** FlowVolume.CUBIC_METER_PER_MINUTE unit type with code 1. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_MINUTE =
            new UnitType(QuantityType.FLOWVOLUME, 1, FlowVolume.Unit.m3_min, "CUBIC_METER_PER_MINUTE", "m3/min");

    /** FlowVolume.CUBIC_METER_PER_HOUR unit type with code 2. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_HOUR =
            new UnitType(QuantityType.FLOWVOLUME, 2, FlowVolume.Unit.m3_h, "CUBIC_METER_PER_HOUR", "m3/h");

    /** FlowVolume.CUBIC_METER_PER_DAY unit type with code 3. */
    public static final UnitType FLOWVOLUME_CUBIC_METER_PER_DAY =
            new UnitType(QuantityType.FLOWVOLUME, 3, FlowVolume.Unit.m3_day, "CUBIC_METER_PER_DAY", "m3/day");

    /** FlowVolume.CUBIC_INCH_PER_SECOND unit type with code 4. */
    public static final UnitType FLOWVOLUME_CUBIC_INCH_PER_SECOND =
            new UnitType(QuantityType.FLOWVOLUME, 4, FlowVolume.Unit.in3_s, "CUBIC_INCH_PER_SECOND", "in3/s");

    /** FlowVolume.CUBIC_INCH_PER_MINUTE unit type with code 5. */
    public static final UnitType FLOWVOLUME_CUBIC_INCH_PER_MINUTE =
            new UnitType(QuantityType.FLOWVOLUME, 5, FlowVolume.Unit.in3_min, "CUBIC_INCH_PER_MINUTE", "in3/min");

    /** FlowVolume.CUBIC_FEET_PER_SECOND unit type with code 6. */
    public static final UnitType FLOWVOLUME_CUBIC_FEET_PER_SECOND =
            new UnitType(QuantityType.FLOWVOLUME, 6, FlowVolume.Unit.ft3_s, "CUBIC_FEET_PER_SECOND", "ft3/s");

    /** FlowVolume.CUBIC_FEET_PER_MINUTE unit type with code 7. */
    public static final UnitType FLOWVOLUME_CUBIC_FEET_PER_MINUTE =
            new UnitType(QuantityType.FLOWVOLUME, 7, FlowVolume.Unit.ft3_min, "CUBIC_FEET_PER_MINUTE", "ft3/min");

    /** FlowVolume.GALLON_PER_SECOND unit type with code 8. */
    public static final UnitType FLOWVOLUME_GALLON_PER_SECOND =
            new UnitType(QuantityType.FLOWVOLUME, 8, FlowVolume.Unit.gal_US_s, "GALLON_PER_SECOND", "gal/s");

    /** FlowVolume.GALLON_PER_MINUTE unit type with code 9. */
    public static final UnitType FLOWVOLUME_GALLON_PER_MINUTE =
            new UnitType(QuantityType.FLOWVOLUME, 9, FlowVolume.Unit.gal_US_min, "GALLON_PER_MINUTE", "gal/min");

    /** FlowVolume.GALLON_PER_HOUR unit type with code 10. */
    public static final UnitType FLOWVOLUME_GALLON_PER_HOUR =
            new UnitType(QuantityType.FLOWVOLUME, 10, FlowVolume.Unit.gal_US_h, "GALLON_PER_HOUR", "gal/h");

    /** FlowVolume.GALLON_PER_DAY unit type with code 11. */
    public static final UnitType FLOWVOLUME_GALLON_PER_DAY =
            new UnitType(QuantityType.FLOWVOLUME, 11, FlowVolume.Unit.gal_US_day, "GALLON_PER_DAY", "gal/day");

    /** FlowVolume.LITER_PER_SECOND unit type with code 12. */
    public static final UnitType FLOWVOLUME_LITER_PER_SECOND =
            new UnitType(QuantityType.FLOWVOLUME, 12, FlowVolume.Unit.L_s, "LITER_PER_SECOND", "l/s");

    /** FlowVolume.LITER_PER_MINUTE unit type with code 13. */
    public static final UnitType FLOWVOLUME_LITER_PER_MINUTE =
            new UnitType(QuantityType.FLOWVOLUME, 13, FlowVolume.Unit.L_min, "LITER_PER_MINUTE", "l/min");

    /** FlowVolume.LITER_PER_HOUR unit type with code 14. */
    public static final UnitType FLOWVOLUME_LITER_PER_HOUR =
            new UnitType(QuantityType.FLOWVOLUME, 14, FlowVolume.Unit.L_h, "LITER_PER_HOUR", "l/h");

    /** FlowVolume.LITER_PER_DAY unit type with code 15. */
    public static final UnitType FLOWVOLUME_LITER_PER_DAY =
            new UnitType(QuantityType.FLOWVOLUME, 15, FlowVolume.Unit.L_day, "LITER_PER_DAY", "l/day");

    /* ==================================================== FORCE ====================================================== */

    /** Force.NEWTON unit type with code 0. */
    public static final UnitType FORCE_NEWTON = new UnitType(QuantityType.FORCE, 0, Force.Unit.N, "NEWTON", "N");

    /** Force.KILOGRAM_FORCE unit type with code 1. */
    public static final UnitType FORCE_KILOGRAM_FORCE =
            new UnitType(QuantityType.FORCE, 1, Force.Unit.kgf, "KILOGRAM_FORCE", "kgf");

    /** Force.OUNCE_FORCE unit type with code 2. */
    public static final UnitType FORCE_OUNCE_FORCE = new UnitType(QuantityType.FORCE, 2, Force.Unit.ozf, "OUNCE_FORCE", "ozf");

    /** Force.POUND_FORCE unit type with code 3. */
    public static final UnitType FORCE_POUND_FORCE = new UnitType(QuantityType.FORCE, 3, Force.Unit.lbf, "POUND_FORCE", "lbf");

    /** Force.TON_FORCE unit type with code 4. */
    public static final UnitType FORCE_TON_FORCE = new UnitType(QuantityType.FORCE, 4, Force.Unit.tnf, "TON_FORCE", "tnf");

    /** Force.DYNE unit type with code 5. */
    public static final UnitType FORCE_DYNE = new UnitType(QuantityType.FORCE, 5, Force.Unit.dyn, "DYNE", "dyne");

    /** Force.STHENE unit type with code 6. */
    public static final UnitType FORCE_STHENE = new UnitType(QuantityType.FORCE, 6, Force.Unit.sn, "STHENE", "sth");

    /* ================================================== FREQUENCY ==================================================== */

    /** Frequency.HERTZ unit type with code 0. */
    public static final UnitType FREQUENCY_HERTZ = new UnitType(QuantityType.FREQUENCY, 0, Frequency.Unit.Hz, "HERTZ", "Hz");

    /** Frequency.KILOHERTZ unit type with code 1. */
    public static final UnitType FREQUENCY_KILOHERTZ =
            new UnitType(QuantityType.FREQUENCY, 1, Frequency.Unit.kHz, "KILOHERTZ", "kHz");

    /** Frequency.MEGAHERTZ unit type with code 2. */
    public static final UnitType FREQUENCY_MEGAHERTZ =
            new UnitType(QuantityType.FREQUENCY, 2, Frequency.Unit.MHz, "MEGAHERTZ", "MHz");

    /** Frequency.GIGAHERTZ unit type with code 3. */
    public static final UnitType FREQUENCY_GIGAHERTZ =
            new UnitType(QuantityType.FREQUENCY, 3, Frequency.Unit.GHz, "GIGAHERTZ", "GHz");

    /** Frequency.TERAHERTZ unit type with code 4. */
    public static final UnitType FREQUENCY_TERAHERTZ =
            new UnitType(QuantityType.FREQUENCY, 4, Frequency.Unit.THz, "TERAHERTZ", "THz");

    /** Frequency.PER_SECOND unit type with code 5. */
    public static final UnitType FREQUENCY_PER_SECOND = new UnitType(QuantityType.FREQUENCY, 5,
            Frequency.Unit.Hz.deriveUnit("/s", "per second", 1.0, UnitSystem.SI_DERIVED), "PER_SECOND", "1/s");

    /** Frequency.PER_ATTOSECOND unit type with code 6. */
    public static final UnitType FREQUENCY_PER_ATTOSECOND = new UnitType(QuantityType.FREQUENCY, 6,
            Frequency.Unit.Hz.deriveUnit("/as", "per attosecond", 1E18, UnitSystem.SI_DERIVED), "PER_ATTOSECOND", "1/as");

    /** Frequency.PER_FEMTOSECOND unit type with code 7. */
    public static final UnitType FREQUENCY_PER_FEMTOSECOND = new UnitType(QuantityType.FREQUENCY, 7,
            Frequency.Unit.Hz.deriveUnit("/fs", "per femtosecond", 1E15, UnitSystem.SI_DERIVED), "PER_FEMTOSECOND", "1/fs");

    /** Frequency.PER_PICOSECOND unit type with code 8. */
    public static final UnitType FREQUENCY_PER_PICOSECOND = new UnitType(QuantityType.FREQUENCY, 8,
            Frequency.Unit.Hz.deriveUnit("/ps", "per picosecond", 1E12, UnitSystem.SI_DERIVED), "PER_PICOSECOND", "1/ps");

    /** Frequency.PER_NANOSECOND unit type with code 9. */
    public static final UnitType FREQUENCY_PER_NANOSECOND = new UnitType(QuantityType.FREQUENCY, 9,
            Frequency.Unit.Hz.deriveUnit("/ns", "per nanosecond", 1E9, UnitSystem.SI_DERIVED), "PER_NANOSECOND", "1/ns");

    /** Frequency.PER_MICROSECOND unit type with code 10. */
    public static final UnitType FREQUENCY_PER_MICROSECOND = new UnitType(QuantityType.FREQUENCY, 10,
            Frequency.Unit.Hz.deriveUnit("/mus", "/μs", "per microsecond", 1E6, UnitSystem.SI_DERIVED), "PER_MICROSECOND",
            "1/μs");

    /** Frequency.PER_MILLISECOND unit type with code 11. */
    public static final UnitType FREQUENCY_PER_MILLISECOND = new UnitType(QuantityType.FREQUENCY, 11,
            Frequency.Unit.Hz.deriveUnit("/ms", "per millisecond", 1E3, UnitSystem.SI_DERIVED), "PER_MILLISECOND", "1/ms");

    /** Frequency.PER_MINUTE unit type with code 12. */
    public static final UnitType FREQUENCY_PER_MINUTE = new UnitType(QuantityType.FREQUENCY, 12,
            Frequency.Unit.Hz.deriveUnit("/min", "per minute", 1.0 / 60.0, UnitSystem.OTHER), "PER_MINUTE", "1/min");

    /** Frequency.PER_HOUR unit type with code 13. */
    public static final UnitType FREQUENCY_PER_HOUR = new UnitType(QuantityType.FREQUENCY, 13,
            Frequency.Unit.Hz.deriveUnit("/h", "per hour", 1.0 / 3600.0, UnitSystem.OTHER), "PER_HOUR", "1/hr");

    /** Frequency.PER_DAY unit type with code 14. */
    public static final UnitType FREQUENCY_PER_DAY = new UnitType(QuantityType.FREQUENCY, 14,
            Frequency.Unit.Hz.deriveUnit("/day", "per day", 1.0 / (3600.0 * 24.0), UnitSystem.OTHER), "PER_DAY", "1/day");

    /** Frequency.PER_WEEK unit type with code 15. */
    public static final UnitType FREQUENCY_PER_WEEK = new UnitType(QuantityType.FREQUENCY, 15,
            Frequency.Unit.Hz.deriveUnit("/wk", "per week", 1.0 / (3600.0 * 24.0 * 7.0), UnitSystem.OTHER), "PER_WEEK", "1/wk");

    /** Frequency.RPM unit type with code 16. */
    public static final UnitType FREQUENCY_RPM = new UnitType(QuantityType.FREQUENCY, 16, Frequency.Unit.rpm, "RPM", "rpm");

    /* ==================================================== LENGTH ===================================================== */

    /** Length.METER unit type with code 0. */
    public static final UnitType LENGTH_METER = new UnitType(QuantityType.LENGTH, 0, Length.Unit.m, "METER", "m");

    /** Length.ATTOMETER unit type with code 1. */
    public static final UnitType LENGTH_ATTOMETER = new UnitType(QuantityType.LENGTH, 1, Length.Unit.am, "ATTOMETER", "am");

    /** Length.FEMTOMETER unit type with code 2. */
    public static final UnitType LENGTH_FEMTOMETER = new UnitType(QuantityType.LENGTH, 2, Length.Unit.fm, "FEMTOMETER", "fm");

    /** Length.PICOMETER unit type with code 3. */
    public static final UnitType LENGTH_PICOMETER = new UnitType(QuantityType.LENGTH, 3, Length.Unit.pm, "PICOMETER", "pm");

    /** Length.NANOMETER unit type with code 4. */
    public static final UnitType LENGTH_NANOMETER = new UnitType(QuantityType.LENGTH, 4, Length.Unit.nm, "NANOMETER", "nm");

    /** Length.MICROMETER unit type with code 5. */
    public static final UnitType LENGTH_MICROMETER = new UnitType(QuantityType.LENGTH, 5, Length.Unit.mum, "MICROMETER", "μm");

    /** Length.MILLIMETER unit type with code 6. */
    public static final UnitType LENGTH_MILLIMETER = new UnitType(QuantityType.LENGTH, 6, Length.Unit.mm, "MILLIMETER", "mm");

    /** Length.CENTIMETER unit type with code 7. */
    public static final UnitType LENGTH_CENTIMETER = new UnitType(QuantityType.LENGTH, 7, Length.Unit.cm, "CENTIMETER", "cm");

    /** Length.DECIMETER unit type with code 8. */
    public static final UnitType LENGTH_DECIMETER = new UnitType(QuantityType.LENGTH, 8, Length.Unit.dm, "DECIMETER", "dm");

    /** Length.DEKAMETER unit type with code 9. */
    public static final UnitType LENGTH_DEKAMETER = new UnitType(QuantityType.LENGTH, 9, Length.Unit.dam, "DEKAMETER", "dam");

    /** Length.HECTOMETER unit type with code 10. */
    public static final UnitType LENGTH_HECTOMETER = new UnitType(QuantityType.LENGTH, 10, Length.Unit.hm, "HECTOMETER", "hm");

    /** Length.KILOMETER unit type with code 11. */
    public static final UnitType LENGTH_KILOMETER = new UnitType(QuantityType.LENGTH, 11, Length.Unit.km, "KILOMETER", "km");

    /** Length.MEGAMETER unit type with code 12. */
    public static final UnitType LENGTH_MEGAMETER =
            new UnitType(QuantityType.LENGTH, 12, Units.resolve(Length.Unit.class, "Mm"), "MEGAMETER", "Mm");

    /** Length.INCH unit type with code 13. */
    public static final UnitType LENGTH_INCH = new UnitType(QuantityType.LENGTH, 13, Length.Unit.in, "INCH", "in");

    /** Length.FOOT unit type with code 14. */
    public static final UnitType LENGTH_FOOT = new UnitType(QuantityType.LENGTH, 14, Length.Unit.ft, "FOOT", "ft");

    /** Length.YARD unit type with code 15. */
    public static final UnitType LENGTH_YARD = new UnitType(QuantityType.LENGTH, 15, Length.Unit.yd, "YARD", "yd");

    /** Length.MILE unit type with code 16. */
    public static final UnitType LENGTH_MILE = new UnitType(QuantityType.LENGTH, 16, Length.Unit.mi, "MILE", "mi");

    /** Length.NAUTICAL_MILE unit type with code 17. */
    public static final UnitType LENGTH_NAUTICAL_MILE =
            new UnitType(QuantityType.LENGTH, 17, Length.Unit.NM, "NAUTICAL_MILE", "NM");

    /** Length.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitType LENGTH_ASTRONOMICAL_UNIT =
            new UnitType(QuantityType.LENGTH, 18, Length.Unit.AU, "ASTRONOMICAL_UNIT", "au");

    /** Length.PARSEC unit type with code 19. */
    public static final UnitType LENGTH_PARSEC = new UnitType(QuantityType.LENGTH, 19, Length.Unit.pc, "PARSEC", "pc");

    /** Length.LIGHTYEAR unit type with code 20. */
    public static final UnitType LENGTH_LIGHTYEAR = new UnitType(QuantityType.LENGTH, 20, Length.Unit.ly, "LIGHTYEAR", "ly");

    /** Length.ANGSTROM unit type with code 21. */
    public static final UnitType LENGTH_ANGSTROM = new UnitType(QuantityType.LENGTH, 21, Length.Unit.A, "ANGSTROM", "Å");

    /* =================================================== POSITION ==================================================== */

    /** Position.METER unit type with code 0. */
    public static final UnitType POSITION_METER = new UnitType(QuantityType.POSITION, 0, Length.Unit.m, "METER", "m");

    /** Position.ATTOMETER unit type with code 1. */
    public static final UnitType POSITION_ATTOMETER =
            new UnitType(QuantityType.POSITION, 1, Units.resolve(Length.Unit.class, "am"), "ATTOMETER", "am");

    /** Position.FEMTOMETER unit type with code 2. */
    public static final UnitType POSITION_FEMTOMETER =
            new UnitType(QuantityType.POSITION, 2, Units.resolve(Length.Unit.class, "fm"), "FEMTOMETER", "fm");

    /** Position.PICOMETER unit type with code 3. */
    public static final UnitType POSITION_PICOMETER =
            new UnitType(QuantityType.POSITION, 3, Units.resolve(Length.Unit.class, "pm"), "PICOMETER", "pm");

    /** Position.NANOMETER unit type with code 4. */
    public static final UnitType POSITION_NANOMETER = new UnitType(QuantityType.POSITION, 4, Length.Unit.nm, "NANOMETER", "nm");

    /** Position.MICROMETER unit type with code 5. */
    public static final UnitType POSITION_MICROMETER =
            new UnitType(QuantityType.POSITION, 5, Length.Unit.mum, "MICROMETER", "μm");

    /** Position.MILLIMETER unit type with code 6. */
    public static final UnitType POSITION_MILLIMETER =
            new UnitType(QuantityType.POSITION, 6, Length.Unit.mm, "MILLIMETER", "mm");

    /** Position.CENTIMETER unit type with code 7. */
    public static final UnitType POSITION_CENTIMETER =
            new UnitType(QuantityType.POSITION, 7, Length.Unit.cm, "CENTIMETER", "cm");

    /** Position.DECIMETER unit type with code 8. */
    public static final UnitType POSITION_DECIMETER = new UnitType(QuantityType.POSITION, 8, Length.Unit.dm, "DECIMETER", "dm");

    /** Position.DEKAMETER unit type with code 9. */
    public static final UnitType POSITION_DEKAMETER =
            new UnitType(QuantityType.POSITION, 9, Units.resolve(Length.Unit.class, "dam"), "DEKAMETER", "dam");

    /** Position.HECTOMETER unit type with code 10. */
    public static final UnitType POSITION_HECTOMETER =
            new UnitType(QuantityType.POSITION, 10, Length.Unit.hm, "HECTOMETER", "hm");

    /** Position.KILOMETER unit type with code 11. */
    public static final UnitType POSITION_KILOMETER =
            new UnitType(QuantityType.POSITION, 11, Length.Unit.km, "KILOMETER", "km");

    /** Position.MEGAMETER unit type with code 12. */
    public static final UnitType POSITION_MEGAMETER =
            new UnitType(QuantityType.POSITION, 12, Units.resolve(Length.Unit.class, "Mm"), "MEGAMETER", "Mm");

    /** Position.INCH unit type with code 13. */
    public static final UnitType POSITION_INCH = new UnitType(QuantityType.POSITION, 13, Length.Unit.in, "INCH", "in");

    /** Position.FOOT unit type with code 14. */
    public static final UnitType POSITION_FOOT = new UnitType(QuantityType.POSITION, 14, Length.Unit.ft, "FOOT", "ft");

    /** Position.YARD unit type with code 15. */
    public static final UnitType POSITION_YARD = new UnitType(QuantityType.POSITION, 15, Length.Unit.yd, "YARD", "yd");

    /** Position.MILE unit type with code 16. */
    public static final UnitType POSITION_MILE = new UnitType(QuantityType.POSITION, 16, Length.Unit.mi, "MILE", "mi");

    /** Position.NAUTICAL_MILE unit type with code 17. */
    public static final UnitType POSITION_NAUTICAL_MILE =
            new UnitType(QuantityType.POSITION, 17, Length.Unit.NM, "NAUTICAL_MILE", "NM");

    /** Position.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitType POSITION_ASTRONOMICAL_UNIT =
            new UnitType(QuantityType.POSITION, 18, Length.Unit.AU, "ASTRONOMICAL_UNIT", "AU");

    /** Position.PARSEC unit type with code 19. */
    public static final UnitType POSITION_PARSEC = new UnitType(QuantityType.POSITION, 19, Length.Unit.pc, "PARSEC", "pc");

    /** Position.LIGHTYEAR unit type with code 20. */
    public static final UnitType POSITION_LIGHTYEAR =
            new UnitType(QuantityType.POSITION, 20, Length.Unit.ly, "LIGHTYEAR", "ly");

    /** Position.ANGSTROM unit type with code 21. */
    public static final UnitType POSITION_ANGSTROM = new UnitType(QuantityType.POSITION, 21, Length.Unit.A, "ANGSTROM", "Å");

    /* ================================================= LINEARDENSITY ================================================= */

    /** LinearDensity.KG_PER_METER unit type with code 0. */
    public static final UnitType LINEARDENSITY_KG_PER_METER =
            new UnitType(QuantityType.LINEARDENSITY, 0, LinearDensity.Unit.kg_m, "PER_METER", "1/m");

    /* ===================================================== MASS ====================================================== */

    /** Mass.KILOGRAM unit type with code 0. */
    public static final UnitType MASS_KILOGRAM = new UnitType(QuantityType.MASS, 0, Mass.Unit.kg, "KILOGRAM", "kg");

    /** Mass.FEMTOGRAM unit type with code 1. */
    public static final UnitType MASS_FEMTOGRAM =
            new UnitType(QuantityType.MASS, 1, Units.resolve(Mass.Unit.class, "fg"), "FEMTOGRAM", "fg");

    /** Mass.PICOGRAM unit type with code 2. */
    public static final UnitType MASS_PICOGRAM =
            new UnitType(QuantityType.MASS, 2, Units.resolve(Mass.Unit.class, "pg"), "PICOGRAM", "pg");

    /** Mass.NANOGRAM unit type with code 3. */
    public static final UnitType MASS_NANOGRAM =
            new UnitType(QuantityType.MASS, 3, Units.resolve(Mass.Unit.class, "ng"), "NANOGRAM", "ng");

    /** Mass.MICROGRAM unit type with code 4. */
    public static final UnitType MASS_MICROGRAM = new UnitType(QuantityType.MASS, 4, Mass.Unit.mug, "MICROGRAM", "μg");

    /** Mass.MILLIGRAM unit type with code 5. */
    public static final UnitType MASS_MILLIGRAM = new UnitType(QuantityType.MASS, 5, Mass.Unit.mg, "MILLIGRAM", "mg");

    /** Mass.GRAM unit type with code 6. */
    public static final UnitType MASS_GRAM = new UnitType(QuantityType.MASS, 6, Mass.Unit.g, "GRAM", "kg");

    /** Mass.MEGAGRAM unit type with code 7. */
    public static final UnitType MASS_MEGAGRAM =
            new UnitType(QuantityType.MASS, 7, Units.resolve(Mass.Unit.class, "Mg"), "MEGAGRAM", "Mg");

    /** Mass.GIGAGRAM unit type with code 8. */
    public static final UnitType MASS_GIGAGRAM =
            new UnitType(QuantityType.MASS, 8, Units.resolve(Mass.Unit.class, "Gg"), "GIGAGRAM", "Gg");

    /** Mass.TERAGRAM unit type with code 9. */
    public static final UnitType MASS_TERAGRAM =
            new UnitType(QuantityType.MASS, 9, Units.resolve(Mass.Unit.class, "Tg"), "TERAGRAM", "Tg");

    /** Mass.PETAGRAM unit type with code 10. */
    public static final UnitType MASS_PETAGRAM =
            new UnitType(QuantityType.MASS, 10, Units.resolve(Mass.Unit.class, "Pg"), "PETAGRAM", "Pg");

    /** Mass.MICROELECTRONVOLT unit type with code 11. */
    public static final UnitType MASS_MICROELECTRONVOLT =
            new UnitType(QuantityType.MASS, 11, Mass.Unit.mueV, "MICROELECTRONVOLT", "μeV");

    /** Mass.MILLIELECTRONVOLT unit type with code 12. */
    public static final UnitType MASS_MILLIELECTRONVOLT =
            new UnitType(QuantityType.MASS, 12, Mass.Unit.meV, "MILLIELECTRONVOLT", "meV");

    /** Mass.ELECTRONVOLT unit type with code 13. */
    public static final UnitType MASS_ELECTRONVOLT = new UnitType(QuantityType.MASS, 13, Mass.Unit.eV, "ELECTRONVOLT", "eV");

    /** Mass.KILOELECTRONVOLT unit type with code 14. */
    public static final UnitType MASS_KILOELECTRONVOLT =
            new UnitType(QuantityType.MASS, 14, Mass.Unit.keV, "KILOELECTRONVOLT", "keV");

    /** Mass.MEGAELECTRONVOLT unit type with code 15. */
    public static final UnitType MASS_MEGAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 15, Mass.Unit.MeV, "MEGAELECTRONVOLT", "MeV");

    /** Mass.GIGAELECTRONVOLT unit type with code 16. */
    public static final UnitType MASS_GIGAELECTRONVOLT =
            new UnitType(QuantityType.MASS, 16, Mass.Unit.GeV, "GIGAELECTRONVOLT", "GeV");

    /** Mass.TERAELECTRONVOLT unit type with code 17. */
    public static final UnitType MASS_TERAELECTRONVOLT = new UnitType(QuantityType.MASS, 17,
            Mass.Unit.eV.deriveUnit("TeV", "teraelectronvolt", 1E12, UnitSystem.OTHER), "TERAELECTRONVOLT", "TeV");

    /** Mass.PETAELECTRONVOLT unit type with code 18. */
    public static final UnitType MASS_PETAELECTRONVOLT = new UnitType(QuantityType.MASS, 18,
            Mass.Unit.eV.deriveUnit("PeV", "petaelectronvolt", 1E15, UnitSystem.OTHER), "PETAELECTRONVOLT", "PeV");

    /** Mass.EXAELECTRONVOLT unit type with code 19. */
    public static final UnitType MASS_EXAELECTRONVOLT = new UnitType(QuantityType.MASS, 19,
            Mass.Unit.eV.deriveUnit("EeV", "exaelectronvolt", 1E18, UnitSystem.OTHER), "EXAELECTRONVOLT", "EeV");

    /** Mass.OUNCE unit type with code 20. */
    public static final UnitType MASS_OUNCE = new UnitType(QuantityType.MASS, 20, Mass.Unit.oz, "OUNCE", "oz");

    /** Mass.POUND unit type with code 21. */
    public static final UnitType MASS_POUND = new UnitType(QuantityType.MASS, 21, Mass.Unit.lb, "POUND", "lb");

    /** Mass.DALTON unit type with code 22. */
    public static final UnitType MASS_DALTON = new UnitType(QuantityType.MASS, 22, Mass.Unit.Da, "DALTON", "Da");

    /** Mass.TON_LONG unit type with code 23. */
    public static final UnitType MASS_TON_LONG =
            new UnitType(QuantityType.MASS, 23, Mass.Unit.long_tn, "TON_LONG", "ton (long)");

    /** Mass.TON_SHORT unit type with code 24. */
    public static final UnitType MASS_TON_SHORT =
            new UnitType(QuantityType.MASS, 24, Mass.Unit.sh_tn, "TON_SHORT", "ton (short)");

    /** Mass.TONNE unit type with code 25. */
    public static final UnitType MASS_TONNE = new UnitType(QuantityType.MASS, 25, Mass.Unit.t, "TONNE", "tonne");

    /* ==================================================== POWER ====================================================== */

    /** Power.WATT unit type with code 0. */
    public static final UnitType POWER_WATT = new UnitType(QuantityType.POWER, 0, Power.Unit.W, "WATT", "W");

    /** Power.FEMTOWATT unit type with code 1. */
    public static final UnitType POWER_FEMTOWATT =
            new UnitType(QuantityType.POWER, 1, Units.resolve(Power.Unit.class, "fW"), "FEMTOWATT", "fW");

    /** Power.PICOWATT unit type with code 2. */
    public static final UnitType POWER_PICOWATT =
            new UnitType(QuantityType.POWER, 2, Units.resolve(Power.Unit.class, "pW"), "PICOWATT", "pW");

    /** Power.NANOWATT unit type with code 3. */
    public static final UnitType POWER_NANOWATT =
            new UnitType(QuantityType.POWER, 3, Units.resolve(Power.Unit.class, "nW"), "NANOWATT", "nW");

    /** Power.MICROWATT unit type with code 4. */
    public static final UnitType POWER_MICROWATT = new UnitType(QuantityType.POWER, 4, Power.Unit.muW, "MICROWATT", "μW");

    /** Power.MILLIWATT unit type with code 5. */
    public static final UnitType POWER_MILLIWATT = new UnitType(QuantityType.POWER, 5, Power.Unit.mW, "MILLIWATT", "mW");

    /** Power.KILOWATT unit type with code 6. */
    public static final UnitType POWER_KILOWATT = new UnitType(QuantityType.POWER, 6, Power.Unit.kW, "KILOWATT", "kW");

    /** Power.MEGAWATT unit type with code 7. */
    public static final UnitType POWER_MEGAWATT = new UnitType(QuantityType.POWER, 7, Power.Unit.MW, "MEGAWATT", "MW");

    /** Power.GIGAWATT unit type with code 8. */
    public static final UnitType POWER_GIGAWATT = new UnitType(QuantityType.POWER, 8, Power.Unit.GW, "GIGAWATT", "GW");

    /** Power.TERAWATT unit type with code 9. */
    public static final UnitType POWER_TERAWATT = new UnitType(QuantityType.POWER, 9, Power.Unit.TW, "TERAWATT", "TW");

    /** Power.PETAWATT unit type with code 10. */
    public static final UnitType POWER_PETAWATT = new UnitType(QuantityType.POWER, 10, Power.Unit.PW, "PETAWATT", "PW");

    /** Power.ERG_PER_SECOND unit type with code 11. */
    public static final UnitType POWER_ERG_PER_SECOND =
            new UnitType(QuantityType.POWER, 11, Power.Unit.erg_s, "ERG_PER_SECOND", "erg/s");

    /** Power.FOOT_POUND_FORCE_PER_SECOND unit type with code 12. */
    public static final UnitType POWER_FOOT_POUND_FORCE_PER_SECOND =
            new UnitType(QuantityType.POWER, 12, Power.Unit.ft_lbf_s, "FOOT_POUND_FORCE_s", "ft.lbf/s");

    /** Power.FOOT_POUND_FORCE_PER_MINUTE unit type with code 13. */
    public static final UnitType POWER_FOOT_POUND_FORCE_PER_MINUTE =
            new UnitType(QuantityType.POWER, 13, Power.Unit.ft_lbf_min, "FOOT_POUND_FORCE_PER_MINUTE", "ft.lbf/min");

    /** Power.FOOT_POUND_FORCE_PER_HOUR unit type with code 14. */
    public static final UnitType POWER_FOOT_POUND_FORCE_PER_HOUR =
            new UnitType(QuantityType.POWER, 14, Power.Unit.ft_lbf_h, "FOOT_POUND_FORCE_PER_HOUR", "ft.lbf/h");

    /** Power.HORSEPOWER_METRIC unit type with code 15. */
    public static final UnitType POWER_HORSEPOWER_METRIC =
            new UnitType(QuantityType.POWER, 15, Power.Unit.hp_M, "HORSEPOWER_METRIC", "hp");

    /** Power.STHENE_METER_PER_SECOND unit type with code 16. */
    public static final UnitType POWER_STHENE_METER_PER_SECOND =
            new UnitType(QuantityType.POWER, 16, Power.Unit.sn_m_s, "STHENE_METER_PER_SECOND", "sth/s");

    /* ==================================================== PRESSURE =================================================== */

    /** Pressure.PASCAL unit type with code 0. */
    public static final UnitType PRESSURE_PASCAL = new UnitType(QuantityType.PRESSURE, 0, Pressure.Unit.Pa, "PASCAL", "Pa");

    /** Pressure.HECTOPASCAL unit type with code 1. */
    public static final UnitType PRESSURE_HECTOPASCAL =
            new UnitType(QuantityType.PRESSURE, 1, Pressure.Unit.hPa, "HECTOPASCAL", "hPa");

    /** Pressure.KILOPASCAL unit type with code 2. */
    public static final UnitType PRESSURE_KILOPASCAL =
            new UnitType(QuantityType.PRESSURE, 2, Pressure.Unit.kPa, "KILOPASCAL", "kPa");

    /** Pressure.ATMOSPHERE_STANDARD unit type with code 3. */
    public static final UnitType PRESSURE_ATMOSPHERE_STANDARD =
            new UnitType(QuantityType.PRESSURE, 3, Pressure.Unit.atm, "ATMOSPHERE_STANDARD", "atm");

    /** Pressure.ATMOSPHERE_TECHNICAL unit type with code 4. */
    public static final UnitType PRESSURE_ATMOSPHERE_TECHNICAL =
            new UnitType(QuantityType.PRESSURE, 4, Pressure.Unit.at, "ATMOSPHERE_TECHNICAL", "at");

    /** Pressure.MILLIBAR unit type with code 5. */
    public static final UnitType PRESSURE_MILLIBAR =
            new UnitType(QuantityType.PRESSURE, 5, Pressure.Unit.mbar, "MILLIBAR", "mbar");

    /** Pressure.BAR unit type with code 6. */
    public static final UnitType PRESSURE_BAR = new UnitType(QuantityType.PRESSURE, 6, Pressure.Unit.bar, "BAR", "bar");

    /** Pressure.BARYE unit type with code 7. */
    public static final UnitType PRESSURE_BARYE = new UnitType(QuantityType.PRESSURE, 7, Pressure.Unit.Ba, "BARYE", "Ba");

    /** Pressure.MILLIMETER_MERCURY unit type with code 8. */
    public static final UnitType PRESSURE_MILLIMETER_MERCURY =
            new UnitType(QuantityType.PRESSURE, 8, Pressure.Unit.mmHg, "MILLIMETER_MERCURY", "mmHg");

    /** Pressure.CENTIMETER_MERCURY unit type with code 9. */
    public static final UnitType PRESSURE_CENTIMETER_MERCURY =
            new UnitType(QuantityType.PRESSURE, 9, Pressure.Unit.cmHg, "CENTIMETER_MERCURY", "cmHg");

    /** Pressure.INCH_MERCURY unit type with code 10. */
    public static final UnitType PRESSURE_INCH_MERCURY =
            new UnitType(QuantityType.PRESSURE, 10, Pressure.Unit.inHg, "INCH_MERCURY", "inHg");

    /** Pressure.FOOT_MERCURY unit type with code 11. */
    public static final UnitType PRESSURE_FOOT_MERCURY =
            new UnitType(QuantityType.PRESSURE, 11, Pressure.Unit.ftHg, "FOOT_MERCURY", "ftHg");

    /** Pressure.KGF_PER_SQUARE_MM unit type with code 12. */
    public static final UnitType PRESSURE_KGF_PER_SQUARE_MM =
            new UnitType(QuantityType.PRESSURE, 12, Pressure.Unit.kgf_mm2, "KGF_PER_SQUARE_MM", "kgf/mm2");

    /** Pressure.PIEZE unit type with code 13. */
    public static final UnitType PRESSURE_PIEZE = new UnitType(QuantityType.PRESSURE, 13, Pressure.Unit.pz, "PIEZE", "pz");

    /** Pressure.POUND_PER_SQUARE_INCH unit type with code 14. */
    public static final UnitType PRESSURE_POUND_PER_SQUARE_INCH =
            new UnitType(QuantityType.PRESSURE, 14, Pressure.Unit.lbf_in2, "POUND_PER_SQUARE_INCH", "lb/in2");

    /** Pressure.POUND_PER_SQUARE_FOOT unit type with code 15. */
    public static final UnitType PRESSURE_POUND_PER_SQUARE_FOOT =
            new UnitType(QuantityType.PRESSURE, 15, Pressure.Unit.lbf_ft2, "POUND_PER_SQUARE_FOOT", "lb/ft2");

    /** Pressure.TORR unit type with code 16. */
    public static final UnitType PRESSURE_TORR = new UnitType(QuantityType.PRESSURE, 16, Pressure.Unit.torr, "TORR", "torr");

    /* ==================================================== SPEED ====================================================== */

    /** Speed.METER_PER_SECOND unit type with code 0. */
    public static final UnitType SPEED_METER_PER_SECOND =
            new UnitType(QuantityType.SPEED, 0, Speed.Unit.m_s, "METER_PER_SECOND", "m/s");

    /** Speed.METER_PER_HOUR unit type with code 1. */
    public static final UnitType SPEED_METER_PER_HOUR =
            new UnitType(QuantityType.SPEED, 1, Speed.Unit.m_h, "METER_PER_HOUR", "m/h");

    /** Speed.KM_PER_SECOND unit type with code 2. */
    public static final UnitType SPEED_KM_PER_SECOND =
            new UnitType(QuantityType.SPEED, 2, Speed.Unit.km_s, "KM_PER_SECOND", "km/s");

    /** Speed.KM_PER_HOUR unit type with code 3. */
    public static final UnitType SPEED_KM_PER_HOUR =
            new UnitType(QuantityType.SPEED, 3, Speed.Unit.km_h, "KM_PER_HOUR", "km/h");

    /** Speed.INCH_PER_SECOND unit type with code 4. */
    public static final UnitType SPEED_INCH_PER_SECOND =
            new UnitType(QuantityType.SPEED, 4, Speed.Unit.in_s, "INCH_PER_SECOND", "in/s");

    /** Speed.INCH_PER_MINUTE unit type with code 5. */
    public static final UnitType SPEED_INCH_PER_MINUTE =
            new UnitType(QuantityType.SPEED, 5, Speed.Unit.in_min, "INCH_PER_MINUTE", "in/min");

    /** Speed.INCH_PER_HOUR unit type with code 6. */
    public static final UnitType SPEED_INCH_PER_HOUR =
            new UnitType(QuantityType.SPEED, 6, Speed.Unit.in_h, "INCH_PER_HOUR", "in/h");

    /** Speed.FOOT_PER_SECOND unit type with code 7. */
    public static final UnitType SPEED_FOOT_PER_SECOND =
            new UnitType(QuantityType.SPEED, 7, Speed.Unit.ft_s, "FOOT_PER_SECOND", "ft/s");

    /** Speed.FOOT_PER_MINUTE unit type with code 8. */
    public static final UnitType SPEED_FOOT_PER_MINUTE =
            new UnitType(QuantityType.SPEED, 8, Speed.Unit.ft_min, "FOOT_PER_MINUTE", "ft/min");

    /** Speed.FOOT_PER_HOUR unit type with code 9. */
    public static final UnitType SPEED_FOOT_PER_HOUR =
            new UnitType(QuantityType.SPEED, 9, Speed.Unit.ft_h, "FOOT_PER_HOUR", "ft/h");

    /** Speed.MILE_PER_SECOND unit type with code 10. */
    public static final UnitType SPEED_MILE_PER_SECOND =
            new UnitType(QuantityType.SPEED, 10, Speed.Unit.mi_s, "MILE_PER_SECOND", "mi/s");

    /** Speed.MILE_PER_MINUTE unit type with code 11. */
    public static final UnitType SPEED_MILE_PER_MINUTE =
            new UnitType(QuantityType.SPEED, 11, Speed.Unit.mi_min, "MILE_PER_MINUTE", "mi/min");

    /** Speed.MILE_PER_HOUR unit type with code 12. */
    public static final UnitType SPEED_MILE_PER_HOUR =
            new UnitType(QuantityType.SPEED, 12, Speed.Unit.mi_h, "MILE_PER_HOUR", "mi/h");

    /** Speed.KNOT unit type with code 13. */
    public static final UnitType SPEED_KNOT = new UnitType(QuantityType.SPEED, 13, Speed.Unit.kt, "KNOT", "kt");

    /* ================================================== TEMPERATURE ================================================== */

    /** Temperature.KELVIN unit type with code 0. */
    public static final UnitType TEMPERATURE_KELVIN =
            new UnitType(QuantityType.TEMPERATUREDIFFERENCE, 0, Temperature.Unit.K, "KELVIN", "K");

    /** Temperature.DEGREE_CELSIUS unit type with code 1. */
    public static final UnitType TEMPERATURE_DEGREE_CELSIUS =
            new UnitType(QuantityType.TEMPERATUREDIFFERENCE, 1, Temperature.Unit.degC, "DEGREE_CELSIUS", "OC");

    /** Temperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final UnitType TEMPERATURE_DEGREE_FAHRENHEIT =
            new UnitType(QuantityType.TEMPERATUREDIFFERENCE, 2, Temperature.Unit.degF, "DEGREE_FAHRENHEIT", "OF");

    /** Temperature.DEGREE_RANKINE unit type with code 3. */
    public static final UnitType TEMPERATURE_DEGREE_RANKINE =
            new UnitType(QuantityType.TEMPERATUREDIFFERENCE, 3, Temperature.Unit.degR, "DEGREE_RANKINE", "OR");

    /** Temperature.DEGREE_REAUMUR unit type with code 4. */
    public static final UnitType TEMPERATURE_DEGREE_REAUMUR =
            new UnitType(QuantityType.TEMPERATUREDIFFERENCE, 4, Temperature.Unit.degRe, "DEGREE_REAUMUR", "ORé");

    /* ============================================= ABSOLUTETEMPERATURE =============================================== */

    /** AbsoluteTemperature.KELVIN unit type with code 0. */
    public static final UnitType ABSOLUTETEMPERATURE_KELVIN =
            new UnitType(QuantityType.TEMPERATURE, 0, Temperature.Unit.K, "K", "K");

    /** AbsoluteTemperature.DEGREE_CELSIUS unit type with code 1. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_CELSIUS =
            new UnitType(QuantityType.TEMPERATURE, 1, Temperature.Unit.degC, "DEGREE_CELSIUS", "OC");

    /** AbsoluteTemperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_FAHRENHEIT =
            new UnitType(QuantityType.TEMPERATURE, 2, Temperature.Unit.degF, "DEGREE_FAHRENHEIT", "OF");

    /** AbsoluteTemperature.DEGREE_RANKINE unit type with code 3. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_RANKINE =
            new UnitType(QuantityType.TEMPERATURE, 3, Temperature.Unit.degR, "DEGREE_RANKINE", "OR");

    /** AbsoluteTemperature.DEGREE_REAUMUR unit type with code 4. */
    public static final UnitType ABSOLUTETEMPERATURE_DEGREE_REAUMUR =
            new UnitType(QuantityType.TEMPERATURE, 4, Temperature.Unit.degRe, "DEGREE_REAUMUR", "ORé");

    /* =================================================== DURATION ==================================================== */

    /** Duration.SECOND unit type with code 0. */
    public static final UnitType DURATION_SECOND = new UnitType(QuantityType.DURATION, 0, Duration.Unit.s, "SECOND", "s");

    /** Duration.ATTOSECOND unit type with code 1. */
    public static final UnitType DURATION_ATTOSECOND =
            new UnitType(QuantityType.DURATION, 1, Units.resolve(Duration.Unit.class, "as"), "ATTOSECOND", "as");

    /** Duration.FEMTOSECOND unit type with code 2. */
    public static final UnitType DURATION_FEMTOSECOND =
            new UnitType(QuantityType.DURATION, 2, Units.resolve(Duration.Unit.class, "fs"), "FEMTOSECOND", "fs");

    /** Duration.PICOSECOND unit type with code 3. */
    public static final UnitType DURATION_PICOSECOND =
            new UnitType(QuantityType.DURATION, 3, Duration.Unit.ps, "PICOSECOND", "ps");

    /** Duration.NANOSECOND unit type with code 4. */
    public static final UnitType DURATION_NANOSECOND =
            new UnitType(QuantityType.DURATION, 4, Duration.Unit.ns, "NANOSECOND", "ns");

    /** Duration.MICROSECOND unit type with code 5. */
    public static final UnitType DURATION_MICROSECOND =
            new UnitType(QuantityType.DURATION, 5, Duration.Unit.mus, "MICROSECOND", "μs");

    /** Duration.MILLISECOND unit type with code 6. */
    public static final UnitType DURATION_MILLISECOND =
            new UnitType(QuantityType.DURATION, 6, Duration.Unit.ms, "MILLISECOND", "ms");

    /** Duration.MINUTE unit type with code 7. */
    public static final UnitType DURATION_MINUTE = new UnitType(QuantityType.DURATION, 7, Duration.Unit.min, "MINUTE", "min");

    /** Duration.HOUR unit type with code 8. */
    public static final UnitType DURATION_HOUR = new UnitType(QuantityType.DURATION, 8, Duration.Unit.h, "HOUR", "hr");

    /** Duration.DAY unit type with code 9. */
    public static final UnitType DURATION_DAY = new UnitType(QuantityType.DURATION, 9, Duration.Unit.day, "DAY", "day");

    /** Duration.WEEK unit type with code 10. */
    public static final UnitType DURATION_WEEK = new UnitType(QuantityType.DURATION, 10, Duration.Unit.wk, "WEEK", "wk");

    /* ===================================================== TIME ====================================================== */

    /** Time.SECOND unit type with code 0. */
    public static final UnitType TIME_SECOND = new UnitType(QuantityType.TIME, 0, Duration.Unit.s, "SECOND", "s");

    /** Time.ATTOSECOND unit type with code 1. */
    public static final UnitType TIME_ATTOSECOND =
            new UnitType(QuantityType.TIME, 1, Units.resolve(Duration.Unit.class, "as"), "ATTOSECOND", "as");

    /** Time.FEMTOSECOND unit type with code 2. */
    public static final UnitType TIME_FEMTOSECOND =
            new UnitType(QuantityType.TIME, 2, Units.resolve(Duration.Unit.class, "fs"), "FEMTOSECOND", "fs");

    /** Time.PICOSECOND unit type with code 3. */
    public static final UnitType TIME_PICOSECOND = new UnitType(QuantityType.TIME, 3, Duration.Unit.ps, "PICOSECOND", "ps");

    /** Time.NANOSECOND unit type with code 4. */
    public static final UnitType TIME_NANOSECOND = new UnitType(QuantityType.TIME, 4, Duration.Unit.ns, "NANOSECOND", "ns");

    /** Time.MICROSECOND unit type with code 5. */
    public static final UnitType TIME_MICROSECOND = new UnitType(QuantityType.TIME, 5, Duration.Unit.mus, "MICROSECOND", "μs");

    /** Time.MILLISECOND unit type with code 6. */
    public static final UnitType TIME_MILLISECOND = new UnitType(QuantityType.TIME, 6, Duration.Unit.ms, "MILLISECOND", "ms");

    /** Time.MINUTE unit type with code 7. */
    public static final UnitType TIME_MINUTE = new UnitType(QuantityType.TIME, 7, Duration.Unit.min, "MINUTE", "min");

    /** Time.HOUR unit type with code 8. */
    public static final UnitType TIME_HOUR = new UnitType(QuantityType.TIME, 8, Duration.Unit.h, "HOUR", "hr");

    /** Time.DAY unit type with code 9. */
    public static final UnitType TIME_DAY = new UnitType(QuantityType.TIME, 9, Duration.Unit.day, "DAY", "day");

    /** Time.WEEK unit type with code 10. */
    public static final UnitType TIME_WEEK = new UnitType(QuantityType.TIME, 10, Duration.Unit.wk, "WEEK", "wk");

    /* ==================================================== TORQUE ===================================================== */

    /** Torque.NEWTON_METER unit type with code 0. */
    public static final UnitType TORQUE_NEWTON_METER =
            new UnitType(QuantityType.TORQUE, 0, Torque.Unit.Nm, "NEWTON_METER", "Nm");

    /** Torque.POUND_FOOT unit type with code 1. */
    public static final UnitType TORQUE_POUND_FOOT =
            new UnitType(QuantityType.TORQUE, 1, Torque.Unit.lbf_ft, "POUND_FOOT", "lb.ft");

    /** Torque.POUND_INCH unit type with code 2. */
    public static final UnitType TORQUE_POUND_INCH =
            new UnitType(QuantityType.TORQUE, 2, Torque.Unit.lbf_in, "POUND_INCH", "lb.in");

    /** Torque.METER_KILOGRAM_FORCE unit type with code 3. */
    public static final UnitType TORQUE_METER_KILOGRAM_FORCE =
            new UnitType(QuantityType.TORQUE, 3, Torque.Unit.m_kgf, "METER_KILOGRAM_FORCE", "m.kgf");

    /* ==================================================== VOLUME ===================================================== */

    /** Volume.CUBIC_METER unit type with code 0. */
    public static final UnitType VOLUME_CUBIC_METER = new UnitType(QuantityType.VOLUME, 0, Volume.Unit.m3, "CUBIC_METER", "m3");

    /** Volume.CUBIC_ATTOMETER unit type with code 1. */
    public static final UnitType VOLUME_CUBIC_ATTOMETER =
            new UnitType(QuantityType.VOLUME, 1, Units.resolve(Volume.Unit.class, "am3"), "CUBIC_ATTOMETER", "am3");

    /** Volume.CUBIC_FEMTOMETER unit type with code 2. */
    public static final UnitType VOLUME_CUBIC_FEMTOMETER =
            new UnitType(QuantityType.VOLUME, 2, Units.resolve(Volume.Unit.class, "fm3"), "CUBIC_FEMTOMETER", "fm3");

    /** Volume.CUBIC_PICOMETER unit type with code 3. */
    public static final UnitType VOLUME_CUBIC_PICOMETER =
            new UnitType(QuantityType.VOLUME, 3, Units.resolve(Volume.Unit.class, "pm3"), "CUBIC_PICOMETER", "pm3");

    /** Volume.CUBIC_NANOMETER unit type with code 4. */
    public static final UnitType VOLUME_CUBIC_NANOMETER =
            new UnitType(QuantityType.VOLUME, 4, Units.resolve(Volume.Unit.class, "nm3"), "CUBIC_NANOMETER", "nm3");

    /** Volume.CUBIC_MICROMETER unit type with code 5. */
    public static final UnitType VOLUME_CUBIC_MICROMETER =
            new UnitType(QuantityType.VOLUME, 5, Units.resolve(Volume.Unit.class, "mum3"), "CUBIC_MICROMETER", "μm3");

    /** Volume.CUBIC_MILLIMETER unit type with code 6. */
    public static final UnitType VOLUME_CUBIC_MILLIMETER =
            new UnitType(QuantityType.VOLUME, 6, Volume.Unit.mm3, "CUBIC_MILLIMETER", "mm3");

    /** Volume.CUBIC_CENTIMETER unit type with code 7. */
    public static final UnitType VOLUME_CUBIC_CENTIMETER =
            new UnitType(QuantityType.VOLUME, 7, Volume.Unit.cm3, "CUBIC_CENTIMETER", "cm3");

    /** Volume.CUBIC_DECIMETER unit type with code 8. */
    public static final UnitType VOLUME_CUBIC_DECIMETER =
            new UnitType(QuantityType.VOLUME, 8, Volume.Unit.dm3, "CUBIC_DECIMETER", "dm3");

    /** Volume.CUBIC_DEKAMETER unit type with code 9. */
    public static final UnitType VOLUME_CUBIC_DEKAMETER =
            new UnitType(QuantityType.VOLUME, 9, Units.resolve(Volume.Unit.class, "dam3"), "CUBIC_DEKAMETER", "dam3");

    /** Volume.CUBIC_HECTOMETER unit type with code 10. */
    public static final UnitType VOLUME_CUBIC_HECTOMETER =
            new UnitType(QuantityType.VOLUME, 10, Volume.Unit.hm3, "CUBIC_HECTOMETER", "hm3");

    /** Volume.CUBIC_KILOMETER unit type with code 11. */
    public static final UnitType VOLUME_CUBIC_KILOMETER =
            new UnitType(QuantityType.VOLUME, 11, Volume.Unit.km3, "CUBIC_KILOMETER", "km3");

    /** Volume.CUBIC_MEGAMETER unit type with code 12. */
    public static final UnitType VOLUME_CUBIC_MEGAMETER =
            new UnitType(QuantityType.VOLUME, 12, Units.resolve(Volume.Unit.class, "Mm3"), "CUBIC_MEGAMETER", "Mm3");

    /** Volume.CUBIC_INCH unit type with code 13. */
    public static final UnitType VOLUME_CUBIC_INCH =
            new UnitType(QuantityType.VOLUME, 13, Volume.Unit.in3, "CUBIC_INCH", "in3");

    /** Volume.CUBIC_FOOT unit type with code 14. */
    public static final UnitType VOLUME_CUBIC_FOOT =
            new UnitType(QuantityType.VOLUME, 14, Volume.Unit.ft3, "CUBIC_FOOT", "ft3");

    /** Volume.CUBIC_YARD unit type with code 15. */
    public static final UnitType VOLUME_CUBIC_YARD =
            new UnitType(QuantityType.VOLUME, 15, Volume.Unit.yd3, "CUBIC_YARD", "yd3");

    /** Volume.CUBIC_MILE unit type with code 16. */
    public static final UnitType VOLUME_CUBIC_MILE =
            new UnitType(QuantityType.VOLUME, 16, Volume.Unit.mi3, "CUBIC_MILE", "mi3");

    /** Volume.LITER unit type with code 17. */
    public static final UnitType VOLUME_LITER = new UnitType(QuantityType.VOLUME, 17, Volume.Unit.L, "LITER", "l");

    /** Volume.GALLON_IMP unit type with code 18. */
    public static final UnitType VOLUME_GALLON_IMP =
            new UnitType(QuantityType.VOLUME, 18, Volume.Unit.gal_imp, "GALLON_IMP", "gal (imp)");

    /** Volume.GALLON_US_FLUID unit type with code 19. */
    public static final UnitType VOLUME_GALLON_US_FLUID =
            new UnitType(QuantityType.VOLUME, 19, Volume.Unit.gal_US, "GALLON_US_FLUID", "gal (US)");

    /** Volume.OUNCE_IMP_FLUID unit type with code 20. */
    public static final UnitType VOLUME_OUNCE_IMP_FLUID =
            new UnitType(QuantityType.VOLUME, 20, Volume.Unit.fl_oz_imp, "OUNCE_IMP_FLUID", "oz (imp)");

    /** Volume.OUNCE_US_FLUID unit type with code 21. */
    public static final UnitType VOLUME_OUNCE_US_FLUID =
            new UnitType(QuantityType.VOLUME, 21, Volume.Unit.fl_oz_US, "OUNCE_US_FLUID", "oz (US)");

    /** Volume.PINT_IMP unit type with code 22. */
    public static final UnitType VOLUME_PINT_IMP =
            new UnitType(QuantityType.VOLUME, 22, Volume.Unit.pt_imp, "PINT_IMP", "pt (imp)");

    /** Volume.PINT_US_FLUID unit type with code 23. */
    public static final UnitType VOLUME_PINT_US_FLUID =
            new UnitType(QuantityType.VOLUME, 23, Volume.Unit.pt_US, "PINT_US_FLUID", "pt (US)");

    /** Volume.QUART_IMP unit type with code 24. */
    public static final UnitType VOLUME_QUART_IMP =
            new UnitType(QuantityType.VOLUME, 24, Volume.Unit.qt_imp, "QUART_IMP", "qt (imp)");

    /** Volume.QUART_US_FLUID unit type with code 25. */
    public static final UnitType VOLUME_QUART_US_FLUID =
            new UnitType(QuantityType.VOLUME, 25, Volume.Unit.qt_US, "QUART_US_FLUID", "qt (US)");

    /** Volume.CUBIC_PARSEC unit type with code 26. */
    public static final UnitType VOLUME_CUBIC_PARSEC =
            new UnitType(QuantityType.VOLUME, 26, Volume.Unit.pc3, "CUBIC_PARSEC", "pc3");

    /** Volume.CUBIC_LIGHTYEAR unit type with code 27. */
    public static final UnitType VOLUME_CUBIC_LIGHTYEAR =
            new UnitType(QuantityType.VOLUME, 27, Volume.Unit.ly3, "CUBIC_LIGHTYEAR", "ly3");

    /* ================================================= ABSORBEDDOSE ================================================== */

    /** AbsorbedDose.GRAY unit type with code 0. */
    public static final UnitType ABSORBEDDOSE_GRAY =
            new UnitType(QuantityType.ABSORBEDDOSE, 0, AbsorbedDose.Unit.Gy, "GRAY", "Gy");

    /** AbsorbedDose.MILLIGRAY unit type with code 1. */
    public static final UnitType ABSORBEDDOSE_MILLIGRAY =
            new UnitType(QuantityType.ABSORBEDDOSE, 1, AbsorbedDose.Unit.mGy, "MILLIGRAY", "mGy");

    /** AbsorbedDose.MICROGRAY unit type with code 2. */
    public static final UnitType ABSORBEDDOSE_MICROGRAY =
            new UnitType(QuantityType.ABSORBEDDOSE, 2, AbsorbedDose.Unit.muGy, "MICROGRAY", "μGy");

    /** AbsorbedDose.ERG_PER_GRAM unit type with code 3. */
    public static final UnitType ABSORBEDDOSE_ERG_PER_GRAM =
            new UnitType(QuantityType.ABSORBEDDOSE, 3, AbsorbedDose.Unit.erg_g, "ERG_PER_GRAM", "erg/g");

    /** AbsorbedDose.RAD unit type with code 4. */
    public static final UnitType ABSORBEDDOSE_RAD =
            new UnitType(QuantityType.ABSORBEDDOSE, 4, AbsorbedDose.Unit.rad, "RAD", "rad");

    /* =============================================== AMOUNTOFSUBSTANCE =============================================== */

    /** AmountOfSubstance.MOLE unit type with code 0. */
    public static final UnitType AMOUNTOFSUBSTANCE_MOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 0, AmountOfSubstance.Unit.mol, "MOLE", "mol");

    /** AmountOfSubstance.MILLIMOLE unit type with code 1. */
    public static final UnitType AMOUNTOFSUBSTANCE_MILLIMOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 1, AmountOfSubstance.Unit.mmol, "MILLIMOLE", "mmol");

    /** AmountOfSubstance.MICROMOLE unit type with code 2. */
    public static final UnitType AMOUNTOFSUBSTANCE_MICROMOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 2, AmountOfSubstance.Unit.mumol, "MICROMOLE", "μmol");

    /** AmountOfSubstance.NANOMOLE unit type with code 3. */
    public static final UnitType AMOUNTOFSUBSTANCE_NANOMOLE =
            new UnitType(QuantityType.AMOUNTOFSUBSTANCE, 3, AmountOfSubstance.Unit.nmol, "NANOMOLE", "nmol");

    /* ================================================ CATALYTICACTIVITY ============================================== */

    /** CatalyticActivity.KATAL unit type with code 0. */
    public static final UnitType CATALYTICACTIVITY_KATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 0, CatalyticActivity.Unit.kat, "KATAL", "kat");

    /** CatalyticActivity.MILLIKATAL unit type with code 1. */
    public static final UnitType CATALYTICACTIVITY_MILLIKATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 1, CatalyticActivity.Unit.mkat, "MILLIKATAL", "mkat");

    /** CatalyticActivity.MICROKATAL unit type with code 2. */
    public static final UnitType CATALYTICACTIVITY_MICROKATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 2, CatalyticActivity.Unit.mukat, "MICROKATAL", "μkat");

    /** CatalyticActivity.NANOKATAL unit type with code 3. */
    public static final UnitType CATALYTICACTIVITY_NANOKATAL =
            new UnitType(QuantityType.CATALYTICACTIVITY, 3, CatalyticActivity.Unit.nkat, "NANOKATAL", "nkat");

    /* ============================================= ELECTRICALCAPACITANCE ============================================= */

    /** ElectricalCapacitance.FARAD unit type with code 0. */
    public static final UnitType ELECTRICALCAPACITANCE_FARAD =
            new UnitType(QuantityType.ELECTRICALCAPACITANCE, 0, ElectricalCapacitance.Unit.F, "FARAD", "F");

    /** ElectricalCapacitance.MILLIFARAD unit type with code 1. */
    public static final UnitType ELECTRICALCAPACITANCE_MILLIFARAD =
            new UnitType(QuantityType.ELECTRICALCAPACITANCE, 1, ElectricalCapacitance.Unit.mF, "MILLIFARAD", "mF");

    /** ElectricalCapacitance.MICROFARAD unit type with code 2. */
    public static final UnitType ELECTRICALCAPACITANCE_MICROFARAD =
            new UnitType(QuantityType.ELECTRICALCAPACITANCE, 2, ElectricalCapacitance.Unit.muF, "MICROFARAD", "uF");

    /** ElectricalCapacitance.NANOFARAD unit type with code 3. */
    public static final UnitType ELECTRICALCAPACITANCE_NANOFARAD =
            new UnitType(QuantityType.ELECTRICALCAPACITANCE, 3, ElectricalCapacitance.Unit.nF, "NANOFARAD", "nF");

    /** ElectricalCapacitance.PICOFARAD unit type with code 4. */
    public static final UnitType ELECTRICALCAPACITANCE_PICOFARAD =
            new UnitType(QuantityType.ELECTRICALCAPACITANCE, 4, ElectricalCapacitance.Unit.pF, "PICOFARAD", "pF");

    /* ============================================= ELECTRICALCONDUCTANCE ============================================= */

    /** ElectricalConductance.SIEMENS unit type with code 0. */
    public static final UnitType ELECTRICALCONDUCTANCE_SIEMENS =
            new UnitType(QuantityType.ELECTRICALCONDUCTANCE, 0, ElectricalConductance.Unit.S, "SIEMENS", "S");

    /** ElectricalConductance.MILLISIEMENS unit type with code 1. */
    public static final UnitType ELECTRICALCONDUCTANCE_MILLISIEMENS =
            new UnitType(QuantityType.ELECTRICALCONDUCTANCE, 1, ElectricalConductance.Unit.mS, "MILLISIEMENS", "mS");

    /** ElectricalConductance.MICROSIEMENS unit type with code 2. */
    public static final UnitType ELECTRICALCONDUCTANCE_MICROSIEMENS =
            new UnitType(QuantityType.ELECTRICALCONDUCTANCE, 2, ElectricalConductance.Unit.muS, "MICROSIEMENS", "μS");

    /** ElectricalConductance.NANOSIEMENS unit type with code 3. */
    public static final UnitType ELECTRICALCONDUCTANCE_NANOSIEMENS =
            new UnitType(QuantityType.ELECTRICALCONDUCTANCE, 3, ElectricalConductance.Unit.nS, "NANOSIEMENS", "nS");

    /* ============================================= ELECTRICALINDUCTANCE ============================================= */

    /** ElectricalInductance.HENRY unit type with code 0. */
    public static final UnitType ELECTRICALINDUCTANCE_HENRY =
            new UnitType(QuantityType.ELECTRICALINDUCTANCE, 0, ElectricalInductance.Unit.H, "HENRY", "H");

    /** ElectricalInductance.MILLIHENRY unit type with code 1. */
    public static final UnitType ELECTRICALINDUCTANCE_MILLIHENRY = new UnitType(QuantityType.ELECTRICALINDUCTANCE, 1,
            Units.resolve(ElectricalInductance.Unit.class, "mH"), "MILLIHENRY", "mH");

    /** ElectricalInductance.MICROHENRY unit type with code 2. */
    public static final UnitType ELECTRICALINDUCTANCE_MICROHENRY = new UnitType(QuantityType.ELECTRICALINDUCTANCE, 2,
            Units.resolve(ElectricalInductance.Unit.class, "muH"), "MICROHENRY", "μH");

    /** ElectricalInductance.NANOHENRY unit type with code 3. */
    public static final UnitType ELECTRICALINDUCTANCE_NANOHENRY = new UnitType(QuantityType.ELECTRICALINDUCTANCE, 3,
            Units.resolve(ElectricalInductance.Unit.class, "nH"), "NANOHENRY", "nH");

    /* ================================================= EQUIVALENTDOSE ================================================ */

    /** EquivalentDose.SIEVERT unit type with code 0. */
    public static final UnitType EQUIVALENTDOSE_SIEVERT =
            new UnitType(QuantityType.EQUIVALENTDOSE, 0, EquivalentDose.Unit.Sv, "SIEVERT", "Sv");

    /** EquivalentDose.MILLISIEVERT unit type with code 1. */
    public static final UnitType EQUIVALENTDOSE_MILLISIEVERT =
            new UnitType(QuantityType.EQUIVALENTDOSE, 1, EquivalentDose.Unit.mSv, "MILLISIEVERT", "mSv");

    /** EquivalentDose.MICROSIEVERT unit type with code 2. */
    public static final UnitType EQUIVALENTDOSE_MICROSIEVERT =
            new UnitType(QuantityType.EQUIVALENTDOSE, 2, EquivalentDose.Unit.muSv, "MICROSIEVERT", "μSv");

    /** EquivalentDose.REM unit type with code 3. */
    public static final UnitType EQUIVALENTDOSE_REM =
            new UnitType(QuantityType.EQUIVALENTDOSE, 3, EquivalentDose.Unit.rem, "REM", "rem");

    /* ================================================== ILLUMINANCE ================================================== */

    /** Illuminance.LUX unit type with code 0. */
    public static final UnitType ILLUMINANCE_LUX = new UnitType(QuantityType.ILLUMINANCE, 0, Illuminance.Unit.lx, "LUX", "lx");

    /** Illuminance.MILLILUX unit type with code 1. */
    public static final UnitType ILLUMINANCE_MILLILUX =
            new UnitType(QuantityType.ILLUMINANCE, 1, Illuminance.Unit.mlx, "MILLILUX", "mlx");

    /** Illuminance.MICROLUX unit type with code 2. */
    public static final UnitType ILLUMINANCE_MICROLUX =
            new UnitType(QuantityType.ILLUMINANCE, 2, Illuminance.Unit.mulx, "MICROLUX", "μlx");

    /** Illuminance.KILOLUX unit type with code 3. */
    public static final UnitType ILLUMINANCE_KILOLUX =
            new UnitType(QuantityType.ILLUMINANCE, 3, Illuminance.Unit.klx, "KILOLUX", "klux");

    /** Illuminance.PHOT unit type with code 4. */
    public static final UnitType ILLUMINANCE_PHOT =
            new UnitType(QuantityType.ILLUMINANCE, 4, Illuminance.Unit.ph, "PHOT", "ph");

    /** Illuminance.NOX unit type with code 5. */
    public static final UnitType ILLUMINANCE_NOX = new UnitType(QuantityType.ILLUMINANCE, 5, Illuminance.Unit.nx, "NOX", "nx");

    /* ================================================= LUMINOUSFLUX ================================================== */

    /** LuminousFlux.LUMEN unit type with code 0. */
    public static final UnitType LUMINOUSFLUX_LUMEN =
            new UnitType(QuantityType.LUMINOUSFLUX, 0, LuminousFlux.Unit.lm, "LUMEN", "lm");

    /* ============================================== LUMINOUSINTENSITY ================================================ */

    /** LuminousIntensity.CANDELA unit type with code 0. */
    public static final UnitType LUMINOUSINTENSITY_CANDELA =
            new UnitType(QuantityType.LUMINOUSINTENSITY, 0, LuminousIntensity.Unit.cd, "CANDELA", "cd");

    /* ============================================= MAGNETICFLUXDENSITY =============================================== */

    /** MagneticFluxDensity.TESLA unit type with code 0. */
    public static final UnitType MAGNETICFLUXDENSITY_TESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 0, MagneticFluxDensity.Unit.T, "TESLA", "T");

    /** MagneticFluxDensity.MILLITESLA unit type with code 1. */
    public static final UnitType MAGNETICFLUXDENSITY_MILLITESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 1, MagneticFluxDensity.Unit.mT, "MILLITESLA", "mT");

    /** MagneticFluxDensity.MICROTESLA unit type with code 2. */
    public static final UnitType MAGNETICFLUXDENSITY_MICROTESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 2, MagneticFluxDensity.Unit.muT, "MICROTESLA", "μT");

    /** MagneticFluxDensity.NANOTESLA unit type with code 3. */
    public static final UnitType MAGNETICFLUXDENSITY_NANOTESLA =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 3, MagneticFluxDensity.Unit.nT, "NANOTESLA", "nT");

    /** MagneticFluxDensity.GAUSS unit type with code 4. */
    public static final UnitType MAGNETICFLUXDENSITY_GAUSS =
            new UnitType(QuantityType.MAGNETICFLUXDENSITY, 4, MagneticFluxDensity.Unit.G, "GAUSS", "G");

    /* ================================================ MAGNETICFLUX =================================================== */

    /** MagneticFlux.WEBER unit type with code 0. */
    public static final UnitType MAGNETICFLUX_WEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 0, MagneticFlux.Unit.Wb, "WEBER", "Wb");

    /** MagneticFlux.MILLIWEBER unit type with code 1. */
    public static final UnitType MAGNETICFLUX_MILLIWEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 1, MagneticFlux.Unit.mWb, "MILLIWEBER", "mWb");

    /** MagneticFlux.MICROWEBER unit type with code 2. */
    public static final UnitType MAGNETICFLUX_MICROWEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 2, MagneticFlux.Unit.muWb, "MICROWEBER", "μWb");

    /** MagneticFlux.NANOWEBER unit type with code 3. */
    public static final UnitType MAGNETICFLUX_NANOWEBER =
            new UnitType(QuantityType.MAGNETICFLUX, 3, MagneticFlux.Unit.nWb, "NANOWEBER", "nWb");

    /** MagneticFlux.MAXWELL unit type with code 4. */
    public static final UnitType MAGNETICFLUX_MAXWELL =
            new UnitType(QuantityType.MAGNETICFLUX, 4, MagneticFlux.Unit.Mx, "MAXWELL", "Mx");

    /* ================================================ RADIOACTIVITY ================================================== */

    /** RadioActivity.BECQUEREL unit type with code 0. */
    public static final UnitType RADIOACTIVITY_BECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 0, RadioActivity.Unit.Bq, "BECQUEREL", "Bq");

    /** RadioActivity.KILOBECQUEREL unit type with code 1. */
    public static final UnitType RADIOACTIVITY_KILOBECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 1, RadioActivity.Unit.kBq, "KILOBECQUEREL", "kBq");

    /** RadioActivity.MEGABECQUEREL unit type with code 2. */
    public static final UnitType RADIOACTIVITY_MEGABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 2, RadioActivity.Unit.MBq, "MEGABECQUEREL", "MBq");

    /** RadioActivity.GIGABECQUEREL unit type with code 3. */
    public static final UnitType RADIOACTIVITY_GIGABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 3, RadioActivity.Unit.GBq, "GIGABECQUEREL", "GBq");

    /** RadioActivity.TERABECQUEREL unit type with code 4. */
    public static final UnitType RADIOACTIVITY_TERABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 4, RadioActivity.Unit.TBq, "TERABECQUEREL", "TBq");

    /** RadioActivity.PETABECQUEREL unit type with code 5. */
    public static final UnitType RADIOACTIVITY_PETABECQUEREL =
            new UnitType(QuantityType.RADIOACTIVITY, 5, Units.resolve(RadioActivity.Unit.class, "PBq"), "PETABECQUEREL", "PBq");

    /** RadioActivity.CURIE unit type with code 6. */
    public static final UnitType RADIOACTIVITY_CURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 6, RadioActivity.Unit.Ci, "CURIE", "Ci");

    /** RadioActivity.MILLICURIE unit type with code 7. */
    public static final UnitType RADIOACTIVITY_MILLICURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 7, RadioActivity.Unit.mCi, "MILLICURIE", "mCi");

    /** RadioActivity.MICROCURIE unit type with code 8. */
    public static final UnitType RADIOACTIVITY_MICROCURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 8, RadioActivity.Unit.muCi, "MICROCURIE", "μCi");

    /** RadioActivity.NANOCURIE unit type with code 9. */
    public static final UnitType RADIOACTIVITY_NANOCURIE =
            new UnitType(QuantityType.RADIOACTIVITY, 9, RadioActivity.Unit.nCi, "NANOCURIE", "nCi");

    /** RadioActivity.RUTHERFORD unit type with code 10. */
    public static final UnitType RADIOACTIVITY_RUTHERFORD =
            new UnitType(QuantityType.RADIOACTIVITY, 10, RadioActivity.Unit.Rd, "RUTHERFORD", "Rd");

    /* ============================================= ANGULARACCELERATION =============================================== */

    /** AngularAcceleration.RADIAN_PER_SECOND_2 unit type with code 0. */
    public static final UnitType ANGULARACCELERATION_RADIAN_PER_SECOND_2 = new UnitType(QuantityType.ANGULARACCELERATION, 0,
            AngularAcceleration.Unit.rad_s2, "RADIAN_PER_SECOND_SQUARED", "rad/s2");

    /** AngularAcceleration.DEGREE_PER_SECOND_2 unit type with code 1. */
    public static final UnitType ANGULARACCELERATION_DEGREE_PER_SECOND_2 = new UnitType(QuantityType.ANGULARACCELERATION, 1,
            AngularAcceleration.Unit.deg_s2, "DEGREE_PER_SECOND_SQUARED", "deg/s2");

    /** AngularAcceleration.ARCMINUTE_PER_SECOND_2 unit type with code 2. */
    public static final UnitType ANGULARACCELERATION_ARCMINUTE_PER_SECOND_2 = new UnitType(QuantityType.ANGULARACCELERATION, 2,
            AngularAcceleration.Unit.arcmin_s2, "ARCMINUTE_PER_SECOND_SQUARED", "arcmin/s2");

    /** AngularAcceleration.ARCSECOND_PER_SECOND_2 unit type with code 3. */
    public static final UnitType ANGULARACCELERATION_ARCSECOND_PER_SECOND_2 = new UnitType(QuantityType.ANGULARACCELERATION, 3,
            AngularAcceleration.Unit.arcsec_s2, "ARCSECOND_PER_SECOND_SQUARED", "arcsec/s2");

    /** AngularAcceleration.GRAD_PER_SECOND_2 unit type with code 4. */
    public static final UnitType ANGULARACCELERATION_GRAD_PER_SECOND_2 = new UnitType(QuantityType.ANGULARACCELERATION, 4,
            AngularAcceleration.Unit.grad_s2, "GRAD_PER_SECOND_SQUARED", "grad/s2");

    /** AngularAcceleration.CENTESIMAL_ARCMINUTE_PER_SECOND_SQUARED unit type with code 5. */
    public static final UnitType ANGULARACCELERATION_CENTECIMAL_ARCMINUTE_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 5, AngularAcceleration.Unit.cdm_s2,
                    "CENTECIMALARCMINUTE_PER_SECOND_SQUARED", "cdm/s2");

    /** AngularAcceleration.CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED unit type with code 6. */
    public static final UnitType ANGULARACCELERATION_CENTESIMAL_ARCSECOND_PER_SECOND_2 =
            new UnitType(QuantityType.ANGULARACCELERATION, 6, AngularAcceleration.Unit.cds_s2,
                    "CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED", "cds/s2");

    /* =============================================== ANGULARVELOCITY ================================================= */

    /** AngularVelocity.RADIAN_PER_SECOND unit type with code 0. */
    public static final UnitType ANGULARVELOCITY_RADIAN_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 0, AngularVelocity.Unit.rad_s, "RADIAN_PER_SECOND", "rad/s");

    /** AngularVelocity.DEGREE_PER_SECOND unit type with code 1. */
    public static final UnitType ANGULARVELOCITY_DEGREE_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 1, AngularVelocity.Unit.deg_s, "DEGREE_PER_SECOND", "deg/s");

    /** AngularVelocity.ARCMINUTE_PER_SECOND unit type with code 2. */
    public static final UnitType ANGULARVELOCITY_ARCMINUTE_PER_SECOND = new UnitType(QuantityType.ANGULARACCELERATION, 2,
            AngularVelocity.Unit.arcmin_s, "ARCMINUTE_PER_SECOND", "arcmin/s");

    /** AngularVelocity.ARCSECOND_PER_SECOND unit type with code 3. */
    public static final UnitType ANGULARVELOCITY_ARCSECOND_PER_SECOND = new UnitType(QuantityType.ANGULARACCELERATION, 3,
            AngularVelocity.Unit.arcsec_s, "ARCSECOND_PER_SECOND", "arcsec/s");

    /** AngularVelocity.GRAD_PER_SECOND unit type with code 4. */
    public static final UnitType ANGULARVELOCITY_GRAD_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 4, AngularVelocity.Unit.grad_s, "GRAD_PER_SECOND", "grad/s");

    /** AngularVelocity.CENTESIMAL_ARCMINUTE_PER_SECOND unit type with code 5. */
    public static final UnitType ANGULARVELOCITY_CENTECIMAL_ARCMINUTE_PER_SECOND = new UnitType(
            QuantityType.ANGULARACCELERATION, 5, AngularVelocity.Unit.cdm_s, "CENTECIMALARCMINUTE_PER_SECOND", "cdm/s");

    /** AngularVelocity.CENTESIMAL_ARCSECOND_PER_SECOND unit type with code 6. */
    public static final UnitType ANGULARVELOCITY_CENTESIMAL_ARCSECOND_PER_SECOND = new UnitType(
            QuantityType.ANGULARACCELERATION, 6, AngularVelocity.Unit.cds_s, "CENTESIMAL_ARCSECOND_PER_SECOND", "cds/s");

    /* ================================================== MOMENTUM ===================================================== */

    /** Momentum.KILOGRAM_METER_PER_SECOND unit type with code 0. */
    public static final UnitType KILOGRAM_METER_PER_SECOND =
            new UnitType(QuantityType.ANGULARACCELERATION, 0, Momentum.Unit.kgm_s, "KILOGRAM_METER_PER_SECOND", "kgm/s");

    /* ================================================== END TYPES ==================================================== */

    /**
     * Make a unit type for serialization.
     * @param unitType the corresponding serialization unit type
     * @param code the code of the unit provided as an int
     * @param unit the djunits data type
     * @param name the unit name
     * @param abbreviation the unit abbreviation
     */
    public UnitType(final QuantityType unitType, final int code, final Unit<?, ?> unit, final String name,
            final String abbreviation)
    {
        Throw.whenNull(unitType, "unitType should not be null");
        Throw.whenNull(unit, "djunitsType should not be null");
        Throw.whenNull(name, "name should not be null");
        Throw.whenNull(abbreviation, "abbreviation should not be null");
        Throw.when(name.length() == 0, SerializationRuntimeException.class, "name should not be empty");
        Throw.when(abbreviation.length() == 0, SerializationRuntimeException.class, "abbreviation should not be empty");

        this.quantityType = unitType;
        this.code = code;
        this.unit = unit;
        this.name = name;
        this.abbreviation = abbreviation;
        Map<Integer, UnitType> codeMap = codeUnitMap.get(this.quantityType);
        if (codeMap == null)
        {
            codeMap = new HashMap<>();
            codeUnitMap.put(this.quantityType, codeMap);
        }
        codeMap.put(this.code, this);
        unitTypeMap.put(this.unit, this);
    }

    /**
     * Return the display type belonging to the display code.
     * @param unitType the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitType getDisplayType(final QuantityType unitType, final Integer code)
    {
        Map<Integer, UnitType> byteMap = codeUnitMap.get(unitType);
        return byteMap == null ? null : byteMap.get(code);
    }

    /**
     * Return the display type belonging to the display code.
     * @param quantityTypeCode the quantity type to search for
     * @param unitCode the unit code to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitType getDisplayType(final byte quantityTypeCode, final int unitCode)
    {
        QuantityType quantityType = QuantityType.getQuantityType(quantityTypeCode);
        Map<Integer, UnitType> codeMap = codeUnitMap.get(quantityType);
        return codeMap == null ? null : codeMap.get(unitCode);
    }

    /**
     * Return the unit belonging to the display code.
     * @param quantityTypeCode the quantity type to search for
     * @param unitCode the unit code to search for.
     * @return the unit type, or null if not found.
     */
    public static Unit<?, ?> getUnit(final byte quantityTypeCode, final int unitCode)
    {
        QuantityType unitType = QuantityType.getQuantityType(quantityTypeCode);
        Map<Integer, UnitType> codeMap = codeUnitMap.get(unitType);
        return codeMap == null ? null : codeMap.get(unitCode) == null ? null : codeMap.get(unitCode).unit;
    }

    /**
     * Return the unit belonging to the display code.
     * @param unitType the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static Unit<?, ?> getUnit(final QuantityType unitType, final int code)
    {
        Map<Integer, UnitType> codeMap = codeUnitMap.get(unitType);
        return codeMap == null ? null : codeMap.get(code) == null ? null : codeMap.get(code).unit;
    }

    /**
     * @return unitType
     */
    public QuantityType getQuantityType()
    {
        return this.quantityType;
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitType getDisplayType(final Unit<?, ?> unit)
    {
        return unitTypeMap.get(unit);
    }

    /**
     * Return the display code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     */
    public static int getIntCode(final Unit<?, ?> unit)
    {
        QuantityType type = QuantityType.getQuantityType(unit);
        UnitType displayType = type == null ? null : getDisplayType(unit);
        return displayType == null ? null : displayType.getIntCode();
    }

    /**
     * Return the display code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     */
    public static byte getByteCode(final Unit<?, ?> unit)
    {
        QuantityType type = QuantityType.getQuantityType(unit);
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
     * @return unit
     */
    public final Unit<?, ?> getUnit()
    {
        return this.unit;
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
