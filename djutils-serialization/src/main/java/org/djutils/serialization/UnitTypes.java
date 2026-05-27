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
public class UnitTypes
{
    /** map per quantity of code number to unit. */
    private static Map<QuantityType, Map<Integer, UnitTypes>> codeUnitMap = new HashMap<>();

    /** map of unit to unit type. */
    private static Map<Unit<?, ?>, UnitTypes> unitTypeMap = new HashMap<>();

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
    public static final UnitTypes DIMENSIONLESS_SI = new UnitTypes(QuantityType.DIMENSIONLESS, 0, Unitless.BASE, "SI", "[]");

    /* ================================================= ACCELERATION ================================================== */

    /** Acceleration.METER_PER_SECOND_2 unit type with code 0. */
    public static final UnitTypes ACCELERATION_METER_PER_SECOND_2 =
            new UnitTypes(QuantityType.ACCELERATION, 0, Acceleration.Unit.m_s2, "METER_PER_SECOND_2", "m/s2");

    /** Acceleration.KM_PER_HOUR_2 unit type with code 1. */
    public static final UnitTypes ACCELERATION_KM_PER_HOUR_2 =
            new UnitTypes(QuantityType.ACCELERATION, 1, Acceleration.Unit.km_h2, "KM_PER_HOUR_2", "km/h2");

    /** Acceleration.INCH_PER_SECOND_2 unit type with code 2. */
    public static final UnitTypes ACCELERATION_INCH_PER_SECOND_2 =
            new UnitTypes(QuantityType.ACCELERATION, 2, Acceleration.Unit.in_s2, "INCH_PER_SECOND_2", "in/s2");

    /** Acceleration.FOOT_PER_SECOND_2 unit type with code 3. */
    public static final UnitTypes ACCELERATION_FOOT_PER_SECOND_2 =
            new UnitTypes(QuantityType.ACCELERATION, 3, Acceleration.Unit.ft_s2, "FOOT_PER_SECOND_2", "ft/s2");

    /** Acceleration.MILE_PER_HOUR_2 unit type with code 4. */
    public static final UnitTypes ACCELERATION_MILE_PER_HOUR_2 =
            new UnitTypes(QuantityType.ACCELERATION, 4, Acceleration.Unit.mi_h2, "MILE_PER_HOUR_2", "mi/h2");

    /** Acceleration.MILE_PER_HOUR_PER_SECOND unit type with code 5. */
    public static final UnitTypes ACCELERATION_MILE_PER_HOUR_PER_SECOND =
            new UnitTypes(QuantityType.ACCELERATION, 5, Acceleration.Unit.mi_h_s, "MILE_PER_HOUR_PER_SECOND", "mi/h/s");

    /** Acceleration.KNOT_PER_SECOND unit type with code 6. */
    public static final UnitTypes ACCELERATION_KNOT_PER_SECOND =
            new UnitTypes(QuantityType.ACCELERATION, 6, Acceleration.Unit.kt_s, "KNOT_PER_SECOND", "kt/s");

    /** Acceleration.GAL unit type with code 7. */
    public static final UnitTypes ACCELERATION_GAL =
            new UnitTypes(QuantityType.ACCELERATION, 7, Acceleration.Unit.Gal, "GAL", "Gal");

    /** Acceleration.STANDARD_GRAVITY unit type with code 8. */
    public static final UnitTypes ACCELERATION_STANDARD_GRAVITY =
            new UnitTypes(QuantityType.ACCELERATION, 8, Acceleration.Unit.g, "STANDARD_GRAVITY", "g");

    /** Acceleration.MILE_PER_SECOND_2 unit type with code 9. */
    public static final UnitTypes ACCELERATION_MILE_PER_SECOND_2 =
            new UnitTypes(QuantityType.ACCELERATION, 9, Acceleration.Unit.mi_s2, "MILE_PER_SECOND_2", "mi/s2");

    /* ================================================== SOLIDANGLE =================================================== */

    /** SolidAngle.STERADIAN unit type with code 0. */
    public static final UnitTypes SOLIDANGLE_STERADIAN =
            new UnitTypes(QuantityType.SOLIDANGLE, 0, SolidAngle.Unit.sr, "STERADIAN", "sr");

    /** SolidAngle.SQUARE_DEGREE unit type with code 1. */
    public static final UnitTypes SOLIDANGLE_SQUARE_DEGREE =
            new UnitTypes(QuantityType.SOLIDANGLE, 1, SolidAngle.Unit.sq_deg, "SQUARE_DEGREE", "sq.deg");

    /* ===================================================== ANGLE ===================================================== */

    /** Angle.RADIAN unit type with code 0. */
    public static final UnitTypes ANGLE_RADIAN = new UnitTypes(QuantityType.ANGLE, 0, Angle.Unit.rad, "RADIAN", "rad");

    /** Angle.ARCMINUTE unit type with code 1. */
    public static final UnitTypes ANGLE_ARCMINUTE =
            new UnitTypes(QuantityType.ANGLE, 1, Angle.Unit.arcmin, "ARCMINUTE", "arcmin");

    /** Angle.ARCSECOND unit type with code 2. */
    public static final UnitTypes ANGLE_ARCSECOND =
            new UnitTypes(QuantityType.ANGLE, 2, Angle.Unit.arcsec, "ARCSECOND", "arcsec");

    /** Angle.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final UnitTypes ANGLE_CENTESIMAL_ARCMINUTE =
            new UnitTypes(QuantityType.ANGLE, 3, Angle.Unit.cdm, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Angle.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final UnitTypes ANGLE_CENTESIMAL_ARCSECOND =
            new UnitTypes(QuantityType.ANGLE, 4, Angle.Unit.cds, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Angle.DEGREE unit type with code 5. */
    public static final UnitTypes ANGLE_DEGREE = new UnitTypes(QuantityType.ANGLE, 5, Angle.Unit.deg, "DEGREE", "deg");

    /** Angle.GRAD unit type with code 6. */
    public static final UnitTypes ANGLE_GRAD = new UnitTypes(QuantityType.ANGLE, 6, Angle.Unit.grad, "GRAD", "grad");

    /** Angle.PERCENT unit type with code 7. */
    public static final UnitTypes ANGLE_PERCENT = new UnitTypes(QuantityType.ANGLE, 7, Angle.Unit.percent, "PERCENT", "%");

    /* =================================================== DIRECTION =================================================== */

    /** Direction.RADIAN unit type with code 0. */
    public static final UnitTypes DIRECTION_RADIAN = new UnitTypes(QuantityType.DIRECTION, 0, Angle.Unit.rad, "RADIAN", "rad");

    /** Direction.ARCMINUTE unit type with code 1. */
    public static final UnitTypes DIRECTION_ARCMINUTE =
            new UnitTypes(QuantityType.DIRECTION, 1, Angle.Unit.arcmin, "ARCMINUTE", "arcmin");

    /** Direction.ARCSECOND unit type with code 2. */
    public static final UnitTypes DIRECTION_ARCSECOND =
            new UnitTypes(QuantityType.DIRECTION, 2, Angle.Unit.arcsec, "ARCSECOND", "arcsec");

    /** Direction.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final UnitTypes DIRECTION_CENTESIMAL_ARCMINUTE =
            new UnitTypes(QuantityType.DIRECTION, 3, Angle.Unit.cdm, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Direction.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final UnitTypes DIRECTION_CENTESIMAL_ARCSECOND =
            new UnitTypes(QuantityType.DIRECTION, 4, Angle.Unit.cds, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Direction.DEGREE unit type with code 5. */
    public static final UnitTypes DIRECTION_DEGREE = new UnitTypes(QuantityType.DIRECTION, 5, Angle.Unit.deg, "DEGREE", "deg");

    /** Direction.GRAD unit type with code 6. */
    public static final UnitTypes DIRECTION_GRAD = new UnitTypes(QuantityType.DIRECTION, 6, Angle.Unit.grad, "GRAD", "grad");

    /** Direction.PERCENT unit type with code 7. */
    public static final UnitTypes DIRECTION_PERCENT =
            new UnitTypes(QuantityType.DIRECTION, 7, Angle.Unit.percent, "PERCENT", "%");

    /* ===================================================== AREA ====================================================== */

    /** Area.SQUARE_METER unit type with code 0. */
    public static final UnitTypes AREA_SQUARE_METER = new UnitTypes(QuantityType.AREA, 0, Area.Unit.m2, "SQUARE_METER", "m2");

    /** Area.SQUARE_ATTOMETER unit type with code 1. */
    public static final UnitTypes AREA_SQUARE_ATTOMETER =
            new UnitTypes(QuantityType.AREA, 1, Area.Unit.am2, "SQUARE_ATTOMETER", "am2");

    /** Area.SQUARE_FEMTOMETER unit type with code 2. */
    public static final UnitTypes AREA_SQUARE_FEMTOMETER =
            new UnitTypes(QuantityType.AREA, 2, Area.Unit.fm2, "SQUARE_FEMTOMETER", "fm2");

    /** Area.SQUARE_PICOMETER unit type with code 3. */
    public static final UnitTypes AREA_SQUARE_PICOMETER =
            new UnitTypes(QuantityType.AREA, 3, Area.Unit.pm2, "SQUARE_PICOMETER", "pm2");

    /** Area.SQUARE_NANOMETER unit type with code 4. */
    public static final UnitTypes AREA_SQUARE_NANOMETER =
            new UnitTypes(QuantityType.AREA, 4, Area.Unit.nm2, "SQUARE_NANOMETER", "nm2");

    /** Area.SQUARE_MICROMETER unit type with code 5. */
    public static final UnitTypes AREA_SQUARE_MICROMETER =
            new UnitTypes(QuantityType.AREA, 5, Area.Unit.mum2, "SQUARE_MICROMETER", "μm2");

    /** Area.SQUARE_MILLIMETER unit type with code 6. */
    public static final UnitTypes AREA_SQUARE_MILLIMETER =
            new UnitTypes(QuantityType.AREA, 6, Area.Unit.mm2, "SQUARE_MILLIMETER", "mm2");

    /** Area.SQUARE_CENTIMETER unit type with code 7. */
    public static final UnitTypes AREA_SQUARE_CENTIMETER =
            new UnitTypes(QuantityType.AREA, 7, Area.Unit.cm2, "SQUARE_CENTIMETER", "cm2");

    /** Area.SQUARE_DECIMETER unit type with code 8. */
    public static final UnitTypes AREA_SQUARE_DECIMETER =
            new UnitTypes(QuantityType.AREA, 8, Area.Unit.dm2, "SQUARE_DECIMETER", "dm2");

    /** Area.SQUARE_DEKAMETER unit type with code 9. */
    public static final UnitTypes AREA_SQUARE_DEKAMETER =
            new UnitTypes(QuantityType.AREA, 9, Area.Unit.dam2, "SQUARE_DEKAMETER", "dam2");

    /** Area.SQUARE_HECTOMETER unit type with code 10. */
    public static final UnitTypes AREA_SQUARE_HECTOMETER =
            new UnitTypes(QuantityType.AREA, 10, Area.Unit.hm2, "SQUARE_HECTOMETER", "hm2");

    /** Area.SQUARE_KILOMETER unit type with code 11. */
    public static final UnitTypes AREA_SQUARE_KILOMETER =
            new UnitTypes(QuantityType.AREA, 11, Area.Unit.km2, "SQUARE_KILOMETER", "km2");

    /** Area.SQUARE_MEGAMETER unit type with code 12. */
    public static final UnitTypes AREA_SQUARE_MEGAMETER =
            new UnitTypes(QuantityType.AREA, 12, Units.resolve(Area.Unit.class, "Mm2"), "SQUARE_MEGAMETER", "Mm2");

    /** Area.SQUARE_INCH unit type with code 13. */
    public static final UnitTypes AREA_SQUARE_INCH = new UnitTypes(QuantityType.AREA, 13, Area.Unit.in2, "SQUARE_INCH", "in2");

    /** Area.SQUARE_FOOT unit type with code 14. */
    public static final UnitTypes AREA_SQUARE_FOOT = new UnitTypes(QuantityType.AREA, 14, Area.Unit.ft2, "SQUARE_FOOT", "ft2");

    /** Area.SQUARE_YARD unit type with code 15. */
    public static final UnitTypes AREA_SQUARE_YARD = new UnitTypes(QuantityType.AREA, 15, Area.Unit.yd2, "SQUARE_YARD", "yd2");

    /** Area.SQUARE_MILE unit type with code 16. */
    public static final UnitTypes AREA_SQUARE_MILE = new UnitTypes(QuantityType.AREA, 16, Area.Unit.mi2, "SQUARE_MILE", "mi2");

    /** Area.SQUARE_NAUTICAL_MILE unit type with code 17. */
    public static final UnitTypes AREA_SQUARE_NAUTICAL_MILE =
            new UnitTypes(QuantityType.AREA, 17, Area.Unit.NM2, "SQUARE_NAUTICAL_MILE", "NM2");

    /** Area.ACRE unit type with code 18. */
    public static final UnitTypes AREA_ACRE = new UnitTypes(QuantityType.AREA, 18, Area.Unit.ac, "ACRE", "acre");

    /** Area.ARE unit type with code 19. */
    public static final UnitTypes AREA_ARE = new UnitTypes(QuantityType.AREA, 19, Area.Unit.a, "ARE", "a");

    /** Area.CENTIARE unit type with code 20. */
    public static final UnitTypes AREA_CENTIARE = new UnitTypes(QuantityType.AREA, 20, Area.Unit.ca, "CENTIARE", "ca");

    /** Area.HECTARE unit type with code 21. */
    public static final UnitTypes AREA_HECTARE = new UnitTypes(QuantityType.AREA, 21, Area.Unit.ha, "HECTARE", "ha");

    /* ==================================================== DENSITY ==================================================== */

    /** Density.KG_PER_METER_3 unit type with code 0. */
    public static final UnitTypes DENSITY_KG_PER_METER_3 =
            new UnitTypes(QuantityType.DENSITY, 0, Density.Unit.kg_m3, "KG_PER_METER_3", "kg/m3");

    /** Density.GRAM_PER_CENTIMETER_3 unit type with code 1. */
    public static final UnitTypes DENSITY_GRAM_PER_CENTIMETER_3 =
            new UnitTypes(QuantityType.DENSITY, 1, Density.Unit.g_cm3, "GRAM_PER_CENTIMETER_3", "g/cm3");

    /* =============================================== ELECTRICALCHARGE ================================================ */

    /** ElectricCharge.COULOMB unit type with code 0. */
    public static final UnitTypes ELECTRICCHARGE_COULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 0, ElectricCharge.Unit.C, "COULOMB", "C");

    /** ElectricalCharge.PICOCOULOMB unit type with code 1. */
    public static final UnitTypes ELECTRICCHARGE_PICOCOULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 1, Units.resolve(ElectricCharge.Unit.class, "pC"), "PICOCOULOMB", "pC");

    /** ElectricCharge.NANOCOULOMB unit type with code 2. */
    public static final UnitTypes ELECTRICCHARGE_NANOCOULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 2, Units.resolve(ElectricCharge.Unit.class, "nC"), "NANOCOULOMB", "nC");

    /** ElectricCharge.MICROCOULOMB unit type with code 3. */
    public static final UnitTypes ELECTRICCHARGE_MICROCOULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 3, ElectricCharge.Unit.muC, "MICROCOULOMB", "μC");

    /** ElectricCharge.MILLICOULOMB unit type with code 4. */
    public static final UnitTypes ELECTRICCHARGE_MILLICOULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 4, ElectricCharge.Unit.mC, "MILLICOULOMB", "mC");

    /** ElectricCharge.ABCOULOMB unit type with code 5. */
    public static final UnitTypes ELECTRICCHARGE_ABCOULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 5, ElectricCharge.Unit.abC, "ABCOULOMB", "abC");

    /** ElectricCharge.ATOMIC_UNIT unit type with code 6. */
    public static final UnitTypes ELECTRICCHARGE_ATOMIC_UNIT =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 6, ElectricCharge.Unit.e, "ATOMIC_UNIT", "au");

    /** ElectricCharge.EMU unit type with code 7. */
    public static final UnitTypes ELECTRICCHARGE_EMU =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 7, ElectricCharge.Unit.emu, "EMU", "emu");

    /** ElectricCharge.ESU unit type with code 8. */
    public static final UnitTypes ELECTRICCHARGE_ESU =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 8, ElectricCharge.Unit.esu, "ESU", "esu");

    /** ElectricCharge.FARADAY unit type with code 9. */
    public static final UnitTypes ELECTRICCHARGE_FARADAY =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 9, ElectricCharge.Unit.F, "FARADAY", "F");

    /** ElectricCharge.FRANKLIN unit type with code 10. */
    public static final UnitTypes ELECTRICCHARGE_FRANKLIN =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 10, ElectricCharge.Unit.Fr, "FRANKLIN  ", "Fr");

    /** ElectricCharge.STATCOULOMB unit type with code 11. */
    public static final UnitTypes ELECTRICCHARGE_STATCOULOMB =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 11, ElectricCharge.Unit.statC, "STATCOULOMB", "statC");

    /** ElectricCharge.MILLIAMPERE_HOUR unit type with code 12. */
    public static final UnitTypes ELECTRICCHARGE_MILLIAMPERE_HOUR =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 12, ElectricCharge.Unit.mAh, "MILLIAMPERE_HOUR", "mAh");

    /** ElectricCharge.AMPERE_HOUR unit type with code 13. */
    public static final UnitTypes ELECTRICCHARGE_AMPERE_HOUR =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 13, ElectricCharge.Unit.Ah, "AMPERE_HOUR", "Ah");

    /** ElectricCharge.KILOAMPERE_HOUR unit type with code 14. */
    public static final UnitTypes ELECTRICCHARGE_KILOAMPERE_HOUR =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 14, ElectricCharge.Unit.kAh, "KILOAMPERE_HOUR", "kAh");

    /** ElectricCharge.MEGAAMPERE_HOUR unit type with code 15. */
    public static final UnitTypes ELECTRICCHARGE_MEGAAMPERE_HOUR =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 15, ElectricCharge.Unit.MAh, "MEGAAMPERE_HOUR", "MAh");

    /** ElectricCharge.MILLIAMPERE_SECOND unit type with code 16. */
    public static final UnitTypes ELECTRICCHARGE_MILLIAMPERE_SECOND =
            new UnitTypes(QuantityType.ELECTRICCHARGE, 16, ElectricCharge.Unit.mAs, "MILLIAMPERE_SECOND", "mAs");

    /* ============================================= ELECTRICCURRENT ================================================= */

    /** ElectricCurrent.AMPERE unit type with code 0. */
    public static final UnitTypes ELECTRICCURRENT_AMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 0, ElectricCurrent.Unit.A, "AMPERE", "A");

    /** ElectricCurrent.NANOAMPERE unit type with code 1. */
    public static final UnitTypes ELECTRICCURRENT_NANOAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 1, Units.resolve(ElectricCurrent.Unit.class, "nA"), "NANOAMPERE", "nA");

    /** ElectricCurrent.MICROAMPERE unit type with code 2. */
    public static final UnitTypes ELECTRICCURRENT_MICROAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 2, ElectricCurrent.Unit.muA, "MICROAMPERE", "μA");

    /** ElectricCurrent.MILLIAMPERE unit type with code 3. */
    public static final UnitTypes ELECTRICCURRENT_MILLIAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 3, ElectricCurrent.Unit.mA, "MILLIAMPERE", "mA");

    /** ElectricCurrent.KILOAMPERE unit type with code 4. */
    public static final UnitTypes ELECTRICCURRENT_KILOAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 4, ElectricCurrent.Unit.kA, "KILOAMPERE", "kA");

    /** ElectricCurrent.MEGAAMPERE unit type with code 5. */
    public static final UnitTypes ELECTRICCURRENT_MEGAAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 5, ElectricCurrent.Unit.MA, "MEGAAMPERE", "MA");

    /** ElectricCurrent.ABAMPERE unit type with code 6. */
    public static final UnitTypes ELECTRICCURRENT_ABAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 6, ElectricCurrent.Unit.abA, "ABAMPERE", "abA");

    /** ElectricCurrent.STATAMPERE unit type with code 7. */
    public static final UnitTypes ELECTRICCURRENT_STATAMPERE =
            new UnitTypes(QuantityType.ELECTRICCURRENT, 7, ElectricCurrent.Unit.statA, "STATAMPERE", "statA");

    /* ============================================ ELECTRICPOTENTIAL ================================================ */

    /** ElectricPotential.VOLT unit type with code 0. */
    public static final UnitTypes ELECTRICPOTENTIAL_VOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 0, ElectricPotential.Unit.V, "VOLT", "V");

    /** ElectricPotential.NANOVOLT unit type with code 1. */
    public static final UnitTypes ELECTRICPOTENTIAL_NANOVOLT = new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 1,
            Units.resolve(ElectricPotential.Unit.class, "nV"), "NANOVOLT", "nV");

    /** ElectricPotential.MICROVOLT unit type with code 2. */
    public static final UnitTypes ELECTRICPOTENTIAL_MICROVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 2, ElectricPotential.Unit.muV, "MICROVOLT", "μV");

    /** ElectricPotential.MILLIVOLT unit type with code 3. */
    public static final UnitTypes ELECTRICPOTENTIAL_MILLIVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 3, ElectricPotential.Unit.mV, "MILLIVOLT", "mV");

    /** ElectricPotential.KILOVOLT unit type with code 4. */
    public static final UnitTypes ELECTRICPOTENTIAL_KILOVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 4, ElectricPotential.Unit.kV, "KILOVOLT", "kV");

    /** ElectricPotential.MEGAVOLT unit type with code 5. */
    public static final UnitTypes ELECTRICPOTENTIAL_MEGAVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 5, ElectricPotential.Unit.MV, "MEGAVOLT", "MV");

    /** ElectricPotential.GIGAVOLT unit type with code 6. */
    public static final UnitTypes ELECTRICPOTENTIAL_GIGAVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 6, ElectricPotential.Unit.GV, "GIGAVOLT", "GV");

    /** ElectricPotential.ABVOLT unit type with code 7. */
    public static final UnitTypes ELECTRICPOTENTIAL_ABVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 7, ElectricPotential.Unit.abV, "ABVOLT", "abV");

    /** ElectricPotential.STATVOLT unit type with code 8. */
    public static final UnitTypes ELECTRICPOTENTIAL_STATVOLT =
            new UnitTypes(QuantityType.ELECTRICPOTENTIAL, 8, ElectricPotential.Unit.statV, "STATVOLT", "statV");

    /* =========================================== ELECTRICALRESISTANCE ================================================ */

    /** ElectricalResistance.OHM unit type with code 0. */
    public static final UnitTypes ELECTRICALRESISTANCE_OHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 0, ElectricalResistance.Unit.ohm, "OHM", "Ω");

    /** ElectricalResistance.NANOOHM unit type with code 1. */
    public static final UnitTypes ELECTRICALRESISTANCE_NANOOHM = new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 1,
            Units.resolve(ElectricalResistance.Unit.class, "nohm"), "NANOOHM", "nΩ");

    /** ElectricalResistance.MICROOHM unit type with code 2. */
    public static final UnitTypes ELECTRICALRESISTANCE_MICROOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 2, ElectricalResistance.Unit.muohm, "MICROOHM", "μΩ");

    /** ElectricalResistance.MILLIOHM unit type with code 3. */
    public static final UnitTypes ELECTRICALRESISTANCE_MILLIOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 3, ElectricalResistance.Unit.mohm, "MILLIOHM", "mΩ");

    /** ElectricalResistance.KILOOHM unit type with code 4. */
    public static final UnitTypes ELECTRICALRESISTANCE_KILOOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 4, ElectricalResistance.Unit.kohm, "KILOOHM", "kΩ");

    /** ElectricalResistance.MEGAOHM unit type with code 5. */
    public static final UnitTypes ELECTRICALRESISTANCE_MEGAOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 5, ElectricalResistance.Unit.Mohm, "MEGAOHM", "MΩ");

    /** ElectricalResistance.GIGAOHM unit type with code 6. */
    public static final UnitTypes ELECTRICALRESISTANCE_GIGAOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 6, ElectricalResistance.Unit.Gohm, "GIGAOHM", "GΩ");

    /** ElectricalResistance.ABOHM unit type with code 7. */
    public static final UnitTypes ELECTRICALRESISTANCE_ABOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 7, ElectricalResistance.Unit.abohm, "ABOHM", "abΩ");

    /** ElectricalResistance.STATOHM unit type with code 8. */
    public static final UnitTypes ELECTRICALRESISTANCE_STATOHM =
            new UnitTypes(QuantityType.ELECTRICALRESISTANCE, 8, ElectricalResistance.Unit.statohm, "STATOHM", "statΩ");

    /* ==================================================== ENERGY ===================================================== */

    /** Energy.JOULE unit type with code 0. */
    public static final UnitTypes ENERGY_JOULE = new UnitTypes(QuantityType.ENERGY, 0, Energy.Unit.J, "JOULE", "J");

    /** Energy.PICOJOULE unit type with code 1. */
    public static final UnitTypes ENERGY_PICOJOULE =
            new UnitTypes(QuantityType.ENERGY, 1, Units.resolve(Energy.Unit.class, "pJ"), "PICOJOULE", "pJ");

    /** Energy.NANOJOULE unit type with code 2. */
    public static final UnitTypes ENERGY_NANOJOULE =
            new UnitTypes(QuantityType.ENERGY, 2, Units.resolve(Energy.Unit.class, "nJ"), "NANOJOULE", "nJ");

    /** Energy.MICROJOULE unit type with code 3. */
    public static final UnitTypes ENERGY_MICROJOULE = new UnitTypes(QuantityType.ENERGY, 3, Energy.Unit.muJ, "MICROJOULE", "μJ");

    /** Energy.MILLIJOULE unit type with code 4. */
    public static final UnitTypes ENERGY_MILLIJOULE = new UnitTypes(QuantityType.ENERGY, 4, Energy.Unit.mJ, "MILLIJOULE", "mJ");

    /** Energy.KILOJOULE unit type with code 5. */
    public static final UnitTypes ENERGY_KILOJOULE = new UnitTypes(QuantityType.ENERGY, 5, Energy.Unit.kJ, "KILOJOULE", "kJ");

    /** Energy.MEGAJOULE unit type with code 6. */
    public static final UnitTypes ENERGY_MEGAJOULE = new UnitTypes(QuantityType.ENERGY, 6, Energy.Unit.MJ, "MEGAJOULE", "MJ");

    /** Energy.GIGAJOULE unit type with code 7. */
    public static final UnitTypes ENERGY_GIGAJOULE = new UnitTypes(QuantityType.ENERGY, 7, Energy.Unit.GJ, "GIGAJOULE", "GJ");

    /** Energy.TERAJOULE unit type with code 8. */
    public static final UnitTypes ENERGY_TERAJOULE = new UnitTypes(QuantityType.ENERGY, 8, Energy.Unit.TJ, "TERAJOULE", "TJ");

    /** Energy.PETAJOULE unit type with code 9. */
    public static final UnitTypes ENERGY_PETAJOULE = new UnitTypes(QuantityType.ENERGY, 9, Energy.Unit.PJ, "PETAJOULE", "PJ");

    /** Energy.ELECTRONVOLT unit type with code 10. */
    public static final UnitTypes ENERGY_ELECTRONVOLT =
            new UnitTypes(QuantityType.ENERGY, 10, Energy.Unit.eV, "ELECTRONVOLT", "eV");

    /** Energy.MICROELECTRONVOLT unit type with code 11. */
    public static final UnitTypes ENERGY_MICROELECTRONVOLT = new UnitTypes(QuantityType.ENERGY, 11,
            Energy.Unit.eV.deriveUnit("mueV", "microelectronvolt", 1E-6, UnitSystem.SI_ACCEPTED), "MICROELECTRONVOLT", "μeV");

    /** Energy.MILLIELECTRONVOLT unit type with code 12. */
    public static final UnitTypes ENERGY_MILLIELECTRONVOLT = new UnitTypes(QuantityType.ENERGY, 12,
            Energy.Unit.eV.deriveUnit("meV", "millielectronvolt", 1E-3, UnitSystem.SI_ACCEPTED), "MILLIELECTRONVOLT", "meV");

    /** Energy.KILOELECTRONVOLT unit type with code 13. */
    public static final UnitTypes ENERGY_KILOELECTRONVOLT =
            new UnitTypes(QuantityType.ENERGY, 13, Energy.Unit.keV, "KILOELECTRONVOLT", "keV");

    /** Energy.MEGAELECTRONVOLT unit type with code 14. */
    public static final UnitTypes ENERGY_MEGAELECTRONVOLT =
            new UnitTypes(QuantityType.ENERGY, 14, Energy.Unit.MeV, "MEGAELECTRONVOLT", "MeV");

    /** Energy.GIGAELECTRONVOLT unit type with code 15. */
    public static final UnitTypes ENERGY_GIGAELECTRONVOLT =
            new UnitTypes(QuantityType.ENERGY, 15, Energy.Unit.GeV, "GIGAELECTRONVOLT", "GeV");

    /** Energy.TERAELECTRONVOLT unit type with code 16. */
    public static final UnitTypes ENERGY_TERAELECTRONVOLT = new UnitTypes(QuantityType.ENERGY, 16,
            Energy.Unit.eV.deriveUnit("TeV", "teraelectronvolt", 1E12, UnitSystem.SI_ACCEPTED), "TERAELECTRONVOLT", "TeV");

    /** Energy.PETAELECTRONVOLT unit type with code 17. */
    public static final UnitTypes ENERGY_PETAELECTRONVOLT = new UnitTypes(QuantityType.ENERGY, 17,
            Energy.Unit.eV.deriveUnit("PeV", "petaelectronvolt", 1E15, UnitSystem.SI_ACCEPTED), "PETAELECTRONVOLT", "PeV");

    /** Energy.EXAELECTRONVOLT unit type with code 18. */
    public static final UnitTypes ENERGY_EXAELECTRONVOLT = new UnitTypes(QuantityType.ENERGY, 18,
            Energy.Unit.eV.deriveUnit("EeV", "exaelectronvolt", 1E18, UnitSystem.SI_ACCEPTED), "EXAELECTRONVOLT", "EeV");

    /** Energy.WATT_HOUR unit type with code 19. */
    public static final UnitTypes ENERGY_WATT_HOUR = new UnitTypes(QuantityType.ENERGY, 19, Energy.Unit.Wh, "WATT_HOUR", "Wh");

    /** Energy.FEMTOWATT_HOUR unit type with code 20. */
    public static final UnitTypes ENERGY_FEMTOWATT_HOUR = new UnitTypes(QuantityType.ENERGY, 20,
            Energy.Unit.Wh.deriveUnit("fWh", "femtowatthour", 1E-15, UnitSystem.SI_ACCEPTED), "FEMTOWATT_HOUR", "fWh");

    /** Energy.PICOWATT_HOUR unit type with code 21. */
    public static final UnitTypes ENERGY_PICOWATT_HOUR = new UnitTypes(QuantityType.ENERGY, 21,
            Energy.Unit.Wh.deriveUnit("pWh", "picowatthour", 1E-12, UnitSystem.SI_ACCEPTED), "PICOWATT_HOUR", "pWh");

    /** Energy.NANOWATT_HOUR unit type with code 22. */
    public static final UnitTypes ENERGY_NANOWATT_HOUR = new UnitTypes(QuantityType.ENERGY, 22,
            Energy.Unit.Wh.deriveUnit("nWh", "nanowatthour", 1E-9, UnitSystem.SI_ACCEPTED), "NANOWATT_HOUR", "nWh");

    /** Energy.MICROWATT_HOUR unit type with code 23. */
    public static final UnitTypes ENERGY_MICROWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 23, Energy.Unit.muWh, "MICROWATT_HOUR", "μWh");

    /** Energy.MILLIWATT_HOUR unit type with code 24. */
    public static final UnitTypes ENERGY_MILLIWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 24, Energy.Unit.mWh, "MILLIWATT_HOUR", "mWh");

    /** Energy.KILOWATT_HOUR unit type with code 25. */
    public static final UnitTypes ENERGY_KILOWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 25, Energy.Unit.kWh, "KILOWATT_HOUR", "kWh");

    /** Energy.MEGAWATT_HOUR unit type with code 26. */
    public static final UnitTypes ENERGY_MEGAWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 26, Energy.Unit.MWh, "MEGAWATT_HOUR", "MWh");

    /** Energy.GIGAWATT_HOUR unit type with code 27. */
    public static final UnitTypes ENERGY_GIGAWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 27, Energy.Unit.GWh, "GIGAWATT_HOUR", "GWh");

    /** Energy.TERAWATT_HOUR unit type with code 28. */
    public static final UnitTypes ENERGY_TERAWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 28, Energy.Unit.TWh, "TERAWATT_HOUR", "TWh");

    /** Energy.PETAWATT_HOUR unit type with code 29. */
    public static final UnitTypes ENERGY_PETAWATT_HOUR =
            new UnitTypes(QuantityType.ENERGY, 29, Energy.Unit.PWh, "PETAWATT_HOUR", "PWh");

    /** Energy.CALORIE unit type with code 30. */
    public static final UnitTypes ENERGY_CALORIE = new UnitTypes(QuantityType.ENERGY, 30, Energy.Unit.cal, "CALORIE", "cal");

    /** Energy.KILOCALORIE unit type with code 31. */
    public static final UnitTypes ENERGY_KILOCALORIE =
            new UnitTypes(QuantityType.ENERGY, 31, Energy.Unit.kcal, "KILOCALORIE", "kcal");

    /** Energy.CALORIE_IT unit type with code 32. */
    public static final UnitTypes ENERGY_CALORIE_IT =
            new UnitTypes(QuantityType.ENERGY, 32, Energy.Unit.cal_IT, "CALORIE_IT", "cal(IT)");

    /** Energy.INCH_POUND_FORCE unit type with code 33. */
    public static final UnitTypes ENERGY_INCH_POUND_FORCE =
            new UnitTypes(QuantityType.ENERGY, 33, Energy.Unit.in_lbf, "INCH_POUND_FORCE", "in lbf");

    /** Energy.FOOT_POUND_FORCE unit type with code 34. */
    public static final UnitTypes ENERGY_FOOT_POUND_FORCE =
            new UnitTypes(QuantityType.ENERGY, 34, Energy.Unit.ft_lbf, "FOOT_POUND_FORCE", "ft lbf");

    /** Energy.ERG unit type with code 35. */
    public static final UnitTypes ENERGY_ERG = new UnitTypes(QuantityType.ENERGY, 35, Energy.Unit.erg, "ERG", "erg");

    /** Energy.BTU_ISO unit type with code 36. */
    public static final UnitTypes ENERGY_BTU_ISO =
            new UnitTypes(QuantityType.ENERGY, 36, Energy.Unit.BTU_ISO, "BTU_ISO", "BTU(ISO)");

    /** Energy.BTU_IT unit type with code 37. */
    public static final UnitTypes ENERGY_BTU_IT = new UnitTypes(QuantityType.ENERGY, 37, Energy.Unit.BTU_IT, "BTU_IT", "BTU(IT)");

    /** Energy.STHENE_METER unit type with code 38. */
    public static final UnitTypes ENERGY_STHENE_METER =
            new UnitTypes(QuantityType.ENERGY, 38, Energy.Unit.sn_m, "STHENE_METER", "sth.m");

    /* =================================================== FLOWMASS ==================================================== */

    /** FlowMass.KG_PER_SECOND unit type with code 0. */
    public static final UnitTypes FLOWMASS_KG_PER_SECOND =
            new UnitTypes(QuantityType.FLOWMASS, 0, FlowMass.Unit.kg_s, "KG_PER_SECOND", "kg/s");

    /** FlowMass.POUND_PER_SECOND unit type with code 1. */
    public static final UnitTypes FLOWMASS_POUND_PER_SECOND =
            new UnitTypes(QuantityType.FLOWMASS, 1, FlowMass.Unit.lb_s, "POUND_PER_SECOND", "lb/s");

    /* ================================================== FLOWVOLUME =================================================== */

    /** FlowVolume.CUBIC_METER_PER_SECOND unit type with code 0. */
    public static final UnitTypes FLOWVOLUME_CUBIC_METER_PER_SECOND =
            new UnitTypes(QuantityType.FLOWVOLUME, 0, FlowVolume.Unit.m3_s, "CUBIC_METER_PER_SECOND", "m3/s");

    /** FlowVolume.CUBIC_METER_PER_MINUTE unit type with code 1. */
    public static final UnitTypes FLOWVOLUME_CUBIC_METER_PER_MINUTE =
            new UnitTypes(QuantityType.FLOWVOLUME, 1, FlowVolume.Unit.m3_min, "CUBIC_METER_PER_MINUTE", "m3/min");

    /** FlowVolume.CUBIC_METER_PER_HOUR unit type with code 2. */
    public static final UnitTypes FLOWVOLUME_CUBIC_METER_PER_HOUR =
            new UnitTypes(QuantityType.FLOWVOLUME, 2, FlowVolume.Unit.m3_h, "CUBIC_METER_PER_HOUR", "m3/h");

    /** FlowVolume.CUBIC_METER_PER_DAY unit type with code 3. */
    public static final UnitTypes FLOWVOLUME_CUBIC_METER_PER_DAY =
            new UnitTypes(QuantityType.FLOWVOLUME, 3, FlowVolume.Unit.m3_day, "CUBIC_METER_PER_DAY", "m3/day");

    /** FlowVolume.CUBIC_INCH_PER_SECOND unit type with code 4. */
    public static final UnitTypes FLOWVOLUME_CUBIC_INCH_PER_SECOND =
            new UnitTypes(QuantityType.FLOWVOLUME, 4, FlowVolume.Unit.in3_s, "CUBIC_INCH_PER_SECOND", "in3/s");

    /** FlowVolume.CUBIC_INCH_PER_MINUTE unit type with code 5. */
    public static final UnitTypes FLOWVOLUME_CUBIC_INCH_PER_MINUTE =
            new UnitTypes(QuantityType.FLOWVOLUME, 5, FlowVolume.Unit.in3_min, "CUBIC_INCH_PER_MINUTE", "in3/min");

    /** FlowVolume.CUBIC_FEET_PER_SECOND unit type with code 6. */
    public static final UnitTypes FLOWVOLUME_CUBIC_FEET_PER_SECOND =
            new UnitTypes(QuantityType.FLOWVOLUME, 6, FlowVolume.Unit.ft3_s, "CUBIC_FEET_PER_SECOND", "ft3/s");

    /** FlowVolume.CUBIC_FEET_PER_MINUTE unit type with code 7. */
    public static final UnitTypes FLOWVOLUME_CUBIC_FEET_PER_MINUTE =
            new UnitTypes(QuantityType.FLOWVOLUME, 7, FlowVolume.Unit.ft3_min, "CUBIC_FEET_PER_MINUTE", "ft3/min");

    /** FlowVolume.GALLON_PER_SECOND unit type with code 8. */
    public static final UnitTypes FLOWVOLUME_GALLON_PER_SECOND =
            new UnitTypes(QuantityType.FLOWVOLUME, 8, FlowVolume.Unit.gal_US_s, "GALLON_PER_SECOND", "gal/s");

    /** FlowVolume.GALLON_PER_MINUTE unit type with code 9. */
    public static final UnitTypes FLOWVOLUME_GALLON_PER_MINUTE =
            new UnitTypes(QuantityType.FLOWVOLUME, 9, FlowVolume.Unit.gal_US_min, "GALLON_PER_MINUTE", "gal/min");

    /** FlowVolume.GALLON_PER_HOUR unit type with code 10. */
    public static final UnitTypes FLOWVOLUME_GALLON_PER_HOUR =
            new UnitTypes(QuantityType.FLOWVOLUME, 10, FlowVolume.Unit.gal_US_h, "GALLON_PER_HOUR", "gal/h");

    /** FlowVolume.GALLON_PER_DAY unit type with code 11. */
    public static final UnitTypes FLOWVOLUME_GALLON_PER_DAY =
            new UnitTypes(QuantityType.FLOWVOLUME, 11, FlowVolume.Unit.gal_US_day, "GALLON_PER_DAY", "gal/day");

    /** FlowVolume.LITER_PER_SECOND unit type with code 12. */
    public static final UnitTypes FLOWVOLUME_LITER_PER_SECOND =
            new UnitTypes(QuantityType.FLOWVOLUME, 12, FlowVolume.Unit.L_s, "LITER_PER_SECOND", "l/s");

    /** FlowVolume.LITER_PER_MINUTE unit type with code 13. */
    public static final UnitTypes FLOWVOLUME_LITER_PER_MINUTE =
            new UnitTypes(QuantityType.FLOWVOLUME, 13, FlowVolume.Unit.L_min, "LITER_PER_MINUTE", "l/min");

    /** FlowVolume.LITER_PER_HOUR unit type with code 14. */
    public static final UnitTypes FLOWVOLUME_LITER_PER_HOUR =
            new UnitTypes(QuantityType.FLOWVOLUME, 14, FlowVolume.Unit.L_h, "LITER_PER_HOUR", "l/h");

    /** FlowVolume.LITER_PER_DAY unit type with code 15. */
    public static final UnitTypes FLOWVOLUME_LITER_PER_DAY =
            new UnitTypes(QuantityType.FLOWVOLUME, 15, FlowVolume.Unit.L_day, "LITER_PER_DAY", "l/day");

    /* ==================================================== FORCE ====================================================== */

    /** Force.NEWTON unit type with code 0. */
    public static final UnitTypes FORCE_NEWTON = new UnitTypes(QuantityType.FORCE, 0, Force.Unit.N, "NEWTON", "N");

    /** Force.KILOGRAM_FORCE unit type with code 1. */
    public static final UnitTypes FORCE_KILOGRAM_FORCE =
            new UnitTypes(QuantityType.FORCE, 1, Force.Unit.kgf, "KILOGRAM_FORCE", "kgf");

    /** Force.OUNCE_FORCE unit type with code 2. */
    public static final UnitTypes FORCE_OUNCE_FORCE = new UnitTypes(QuantityType.FORCE, 2, Force.Unit.ozf, "OUNCE_FORCE", "ozf");

    /** Force.POUND_FORCE unit type with code 3. */
    public static final UnitTypes FORCE_POUND_FORCE = new UnitTypes(QuantityType.FORCE, 3, Force.Unit.lbf, "POUND_FORCE", "lbf");

    /** Force.TON_FORCE unit type with code 4. */
    public static final UnitTypes FORCE_TON_FORCE = new UnitTypes(QuantityType.FORCE, 4, Force.Unit.tnf, "TON_FORCE", "tnf");

    /** Force.DYNE unit type with code 5. */
    public static final UnitTypes FORCE_DYNE = new UnitTypes(QuantityType.FORCE, 5, Force.Unit.dyn, "DYNE", "dyne");

    /** Force.STHENE unit type with code 6. */
    public static final UnitTypes FORCE_STHENE = new UnitTypes(QuantityType.FORCE, 6, Force.Unit.sn, "STHENE", "sth");

    /* ================================================== FREQUENCY ==================================================== */

    /** Frequency.HERTZ unit type with code 0. */
    public static final UnitTypes FREQUENCY_HERTZ = new UnitTypes(QuantityType.FREQUENCY, 0, Frequency.Unit.Hz, "HERTZ", "Hz");

    /** Frequency.KILOHERTZ unit type with code 1. */
    public static final UnitTypes FREQUENCY_KILOHERTZ =
            new UnitTypes(QuantityType.FREQUENCY, 1, Frequency.Unit.kHz, "KILOHERTZ", "kHz");

    /** Frequency.MEGAHERTZ unit type with code 2. */
    public static final UnitTypes FREQUENCY_MEGAHERTZ =
            new UnitTypes(QuantityType.FREQUENCY, 2, Frequency.Unit.MHz, "MEGAHERTZ", "MHz");

    /** Frequency.GIGAHERTZ unit type with code 3. */
    public static final UnitTypes FREQUENCY_GIGAHERTZ =
            new UnitTypes(QuantityType.FREQUENCY, 3, Frequency.Unit.GHz, "GIGAHERTZ", "GHz");

    /** Frequency.TERAHERTZ unit type with code 4. */
    public static final UnitTypes FREQUENCY_TERAHERTZ =
            new UnitTypes(QuantityType.FREQUENCY, 4, Frequency.Unit.THz, "TERAHERTZ", "THz");

    /** Frequency.PER_SECOND unit type with code 5. */
    public static final UnitTypes FREQUENCY_PER_SECOND = new UnitTypes(QuantityType.FREQUENCY, 5,
            Frequency.Unit.Hz.deriveUnit("/s", "per second", 1.0, UnitSystem.SI_DERIVED), "PER_SECOND", "1/s");

    /** Frequency.PER_ATTOSECOND unit type with code 6. */
    public static final UnitTypes FREQUENCY_PER_ATTOSECOND = new UnitTypes(QuantityType.FREQUENCY, 6,
            Frequency.Unit.Hz.deriveUnit("/as", "per attosecond", 1E18, UnitSystem.SI_DERIVED), "PER_ATTOSECOND", "1/as");

    /** Frequency.PER_FEMTOSECOND unit type with code 7. */
    public static final UnitTypes FREQUENCY_PER_FEMTOSECOND = new UnitTypes(QuantityType.FREQUENCY, 7,
            Frequency.Unit.Hz.deriveUnit("/fs", "per femtosecond", 1E15, UnitSystem.SI_DERIVED), "PER_FEMTOSECOND", "1/fs");

    /** Frequency.PER_PICOSECOND unit type with code 8. */
    public static final UnitTypes FREQUENCY_PER_PICOSECOND = new UnitTypes(QuantityType.FREQUENCY, 8,
            Frequency.Unit.Hz.deriveUnit("/ps", "per picosecond", 1E12, UnitSystem.SI_DERIVED), "PER_PICOSECOND", "1/ps");

    /** Frequency.PER_NANOSECOND unit type with code 9. */
    public static final UnitTypes FREQUENCY_PER_NANOSECOND = new UnitTypes(QuantityType.FREQUENCY, 9,
            Frequency.Unit.Hz.deriveUnit("/ns", "per nanosecond", 1E9, UnitSystem.SI_DERIVED), "PER_NANOSECOND", "1/ns");

    /** Frequency.PER_MICROSECOND unit type with code 10. */
    public static final UnitTypes FREQUENCY_PER_MICROSECOND = new UnitTypes(QuantityType.FREQUENCY, 10,
            Frequency.Unit.Hz.deriveUnit("/mus", "/μs", "per microsecond", 1E6, UnitSystem.SI_DERIVED), "PER_MICROSECOND",
            "1/μs");

    /** Frequency.PER_MILLISECOND unit type with code 11. */
    public static final UnitTypes FREQUENCY_PER_MILLISECOND = new UnitTypes(QuantityType.FREQUENCY, 11,
            Frequency.Unit.Hz.deriveUnit("/ms", "per millisecond", 1E3, UnitSystem.SI_DERIVED), "PER_MILLISECOND", "1/ms");

    /** Frequency.PER_MINUTE unit type with code 12. */
    public static final UnitTypes FREQUENCY_PER_MINUTE = new UnitTypes(QuantityType.FREQUENCY, 12,
            Frequency.Unit.Hz.deriveUnit("/min", "per minute", 1.0 / 60.0, UnitSystem.OTHER), "PER_MINUTE", "1/min");

    /** Frequency.PER_HOUR unit type with code 13. */
    public static final UnitTypes FREQUENCY_PER_HOUR = new UnitTypes(QuantityType.FREQUENCY, 13,
            Frequency.Unit.Hz.deriveUnit("/h", "per hour", 1.0 / 3600.0, UnitSystem.OTHER), "PER_HOUR", "1/hr");

    /** Frequency.PER_DAY unit type with code 14. */
    public static final UnitTypes FREQUENCY_PER_DAY = new UnitTypes(QuantityType.FREQUENCY, 14,
            Frequency.Unit.Hz.deriveUnit("/day", "per day", 1.0 / (3600.0 * 24.0), UnitSystem.OTHER), "PER_DAY", "1/day");

    /** Frequency.PER_WEEK unit type with code 15. */
    public static final UnitTypes FREQUENCY_PER_WEEK = new UnitTypes(QuantityType.FREQUENCY, 15,
            Frequency.Unit.Hz.deriveUnit("/wk", "per week", 1.0 / (3600.0 * 24.0 * 7.0), UnitSystem.OTHER), "PER_WEEK", "1/wk");

    /** Frequency.RPM unit type with code 16. */
    public static final UnitTypes FREQUENCY_RPM = new UnitTypes(QuantityType.FREQUENCY, 16, Frequency.Unit.rpm, "RPM", "rpm");

    /* ==================================================== LENGTH ===================================================== */

    /** Length.METER unit type with code 0. */
    public static final UnitTypes LENGTH_METER = new UnitTypes(QuantityType.LENGTH, 0, Length.Unit.m, "METER", "m");

    /** Length.ATTOMETER unit type with code 1. */
    public static final UnitTypes LENGTH_ATTOMETER = new UnitTypes(QuantityType.LENGTH, 1, Length.Unit.am, "ATTOMETER", "am");

    /** Length.FEMTOMETER unit type with code 2. */
    public static final UnitTypes LENGTH_FEMTOMETER = new UnitTypes(QuantityType.LENGTH, 2, Length.Unit.fm, "FEMTOMETER", "fm");

    /** Length.PICOMETER unit type with code 3. */
    public static final UnitTypes LENGTH_PICOMETER = new UnitTypes(QuantityType.LENGTH, 3, Length.Unit.pm, "PICOMETER", "pm");

    /** Length.NANOMETER unit type with code 4. */
    public static final UnitTypes LENGTH_NANOMETER = new UnitTypes(QuantityType.LENGTH, 4, Length.Unit.nm, "NANOMETER", "nm");

    /** Length.MICROMETER unit type with code 5. */
    public static final UnitTypes LENGTH_MICROMETER = new UnitTypes(QuantityType.LENGTH, 5, Length.Unit.mum, "MICROMETER", "μm");

    /** Length.MILLIMETER unit type with code 6. */
    public static final UnitTypes LENGTH_MILLIMETER = new UnitTypes(QuantityType.LENGTH, 6, Length.Unit.mm, "MILLIMETER", "mm");

    /** Length.CENTIMETER unit type with code 7. */
    public static final UnitTypes LENGTH_CENTIMETER = new UnitTypes(QuantityType.LENGTH, 7, Length.Unit.cm, "CENTIMETER", "cm");

    /** Length.DECIMETER unit type with code 8. */
    public static final UnitTypes LENGTH_DECIMETER = new UnitTypes(QuantityType.LENGTH, 8, Length.Unit.dm, "DECIMETER", "dm");

    /** Length.DEKAMETER unit type with code 9. */
    public static final UnitTypes LENGTH_DEKAMETER = new UnitTypes(QuantityType.LENGTH, 9, Length.Unit.dam, "DEKAMETER", "dam");

    /** Length.HECTOMETER unit type with code 10. */
    public static final UnitTypes LENGTH_HECTOMETER = new UnitTypes(QuantityType.LENGTH, 10, Length.Unit.hm, "HECTOMETER", "hm");

    /** Length.KILOMETER unit type with code 11. */
    public static final UnitTypes LENGTH_KILOMETER = new UnitTypes(QuantityType.LENGTH, 11, Length.Unit.km, "KILOMETER", "km");

    /** Length.MEGAMETER unit type with code 12. */
    public static final UnitTypes LENGTH_MEGAMETER =
            new UnitTypes(QuantityType.LENGTH, 12, Units.resolve(Length.Unit.class, "Mm"), "MEGAMETER", "Mm");

    /** Length.INCH unit type with code 13. */
    public static final UnitTypes LENGTH_INCH = new UnitTypes(QuantityType.LENGTH, 13, Length.Unit.in, "INCH", "in");

    /** Length.FOOT unit type with code 14. */
    public static final UnitTypes LENGTH_FOOT = new UnitTypes(QuantityType.LENGTH, 14, Length.Unit.ft, "FOOT", "ft");

    /** Length.YARD unit type with code 15. */
    public static final UnitTypes LENGTH_YARD = new UnitTypes(QuantityType.LENGTH, 15, Length.Unit.yd, "YARD", "yd");

    /** Length.MILE unit type with code 16. */
    public static final UnitTypes LENGTH_MILE = new UnitTypes(QuantityType.LENGTH, 16, Length.Unit.mi, "MILE", "mi");

    /** Length.NAUTICAL_MILE unit type with code 17. */
    public static final UnitTypes LENGTH_NAUTICAL_MILE =
            new UnitTypes(QuantityType.LENGTH, 17, Length.Unit.NM, "NAUTICAL_MILE", "NM");

    /** Length.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitTypes LENGTH_ASTRONOMICAL_UNIT =
            new UnitTypes(QuantityType.LENGTH, 18, Length.Unit.AU, "ASTRONOMICAL_UNIT", "au");

    /** Length.PARSEC unit type with code 19. */
    public static final UnitTypes LENGTH_PARSEC = new UnitTypes(QuantityType.LENGTH, 19, Length.Unit.pc, "PARSEC", "pc");

    /** Length.LIGHTYEAR unit type with code 20. */
    public static final UnitTypes LENGTH_LIGHTYEAR = new UnitTypes(QuantityType.LENGTH, 20, Length.Unit.ly, "LIGHTYEAR", "ly");

    /** Length.ANGSTROM unit type with code 21. */
    public static final UnitTypes LENGTH_ANGSTROM = new UnitTypes(QuantityType.LENGTH, 21, Length.Unit.A, "ANGSTROM", "Å");

    /* =================================================== POSITION ==================================================== */

    /** Position.METER unit type with code 0. */
    public static final UnitTypes POSITION_METER = new UnitTypes(QuantityType.POSITION, 0, Length.Unit.m, "METER", "m");

    /** Position.ATTOMETER unit type with code 1. */
    public static final UnitTypes POSITION_ATTOMETER =
            new UnitTypes(QuantityType.POSITION, 1, Units.resolve(Length.Unit.class, "am"), "ATTOMETER", "am");

    /** Position.FEMTOMETER unit type with code 2. */
    public static final UnitTypes POSITION_FEMTOMETER =
            new UnitTypes(QuantityType.POSITION, 2, Units.resolve(Length.Unit.class, "fm"), "FEMTOMETER", "fm");

    /** Position.PICOMETER unit type with code 3. */
    public static final UnitTypes POSITION_PICOMETER =
            new UnitTypes(QuantityType.POSITION, 3, Units.resolve(Length.Unit.class, "pm"), "PICOMETER", "pm");

    /** Position.NANOMETER unit type with code 4. */
    public static final UnitTypes POSITION_NANOMETER = new UnitTypes(QuantityType.POSITION, 4, Length.Unit.nm, "NANOMETER", "nm");

    /** Position.MICROMETER unit type with code 5. */
    public static final UnitTypes POSITION_MICROMETER =
            new UnitTypes(QuantityType.POSITION, 5, Length.Unit.mum, "MICROMETER", "μm");

    /** Position.MILLIMETER unit type with code 6. */
    public static final UnitTypes POSITION_MILLIMETER =
            new UnitTypes(QuantityType.POSITION, 6, Length.Unit.mm, "MILLIMETER", "mm");

    /** Position.CENTIMETER unit type with code 7. */
    public static final UnitTypes POSITION_CENTIMETER =
            new UnitTypes(QuantityType.POSITION, 7, Length.Unit.cm, "CENTIMETER", "cm");

    /** Position.DECIMETER unit type with code 8. */
    public static final UnitTypes POSITION_DECIMETER = new UnitTypes(QuantityType.POSITION, 8, Length.Unit.dm, "DECIMETER", "dm");

    /** Position.DEKAMETER unit type with code 9. */
    public static final UnitTypes POSITION_DEKAMETER =
            new UnitTypes(QuantityType.POSITION, 9, Units.resolve(Length.Unit.class, "dam"), "DEKAMETER", "dam");

    /** Position.HECTOMETER unit type with code 10. */
    public static final UnitTypes POSITION_HECTOMETER =
            new UnitTypes(QuantityType.POSITION, 10, Length.Unit.hm, "HECTOMETER", "hm");

    /** Position.KILOMETER unit type with code 11. */
    public static final UnitTypes POSITION_KILOMETER =
            new UnitTypes(QuantityType.POSITION, 11, Length.Unit.km, "KILOMETER", "km");

    /** Position.MEGAMETER unit type with code 12. */
    public static final UnitTypes POSITION_MEGAMETER =
            new UnitTypes(QuantityType.POSITION, 12, Units.resolve(Length.Unit.class, "Mm"), "MEGAMETER", "Mm");

    /** Position.INCH unit type with code 13. */
    public static final UnitTypes POSITION_INCH = new UnitTypes(QuantityType.POSITION, 13, Length.Unit.in, "INCH", "in");

    /** Position.FOOT unit type with code 14. */
    public static final UnitTypes POSITION_FOOT = new UnitTypes(QuantityType.POSITION, 14, Length.Unit.ft, "FOOT", "ft");

    /** Position.YARD unit type with code 15. */
    public static final UnitTypes POSITION_YARD = new UnitTypes(QuantityType.POSITION, 15, Length.Unit.yd, "YARD", "yd");

    /** Position.MILE unit type with code 16. */
    public static final UnitTypes POSITION_MILE = new UnitTypes(QuantityType.POSITION, 16, Length.Unit.mi, "MILE", "mi");

    /** Position.NAUTICAL_MILE unit type with code 17. */
    public static final UnitTypes POSITION_NAUTICAL_MILE =
            new UnitTypes(QuantityType.POSITION, 17, Length.Unit.NM, "NAUTICAL_MILE", "NM");

    /** Position.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final UnitTypes POSITION_ASTRONOMICAL_UNIT =
            new UnitTypes(QuantityType.POSITION, 18, Length.Unit.AU, "ASTRONOMICAL_UNIT", "AU");

    /** Position.PARSEC unit type with code 19. */
    public static final UnitTypes POSITION_PARSEC = new UnitTypes(QuantityType.POSITION, 19, Length.Unit.pc, "PARSEC", "pc");

    /** Position.LIGHTYEAR unit type with code 20. */
    public static final UnitTypes POSITION_LIGHTYEAR =
            new UnitTypes(QuantityType.POSITION, 20, Length.Unit.ly, "LIGHTYEAR", "ly");

    /** Position.ANGSTROM unit type with code 21. */
    public static final UnitTypes POSITION_ANGSTROM = new UnitTypes(QuantityType.POSITION, 21, Length.Unit.A, "ANGSTROM", "Å");

    /* ================================================= LINEARDENSITY ================================================= */

    /** LinearDensity.KG_PER_METER unit type with code 0. */
    public static final UnitTypes LINEARDENSITY_KG_PER_METER =
            new UnitTypes(QuantityType.LINEARDENSITY, 0, LinearDensity.Unit.kg_m, "PER_METER", "1/m");

    /* ===================================================== MASS ====================================================== */

    /** Mass.KILOGRAM unit type with code 0. */
    public static final UnitTypes MASS_KILOGRAM = new UnitTypes(QuantityType.MASS, 0, Mass.Unit.kg, "KILOGRAM", "kg");

    /** Mass.FEMTOGRAM unit type with code 1. */
    public static final UnitTypes MASS_FEMTOGRAM =
            new UnitTypes(QuantityType.MASS, 1, Units.resolve(Mass.Unit.class, "fg"), "FEMTOGRAM", "fg");

    /** Mass.PICOGRAM unit type with code 2. */
    public static final UnitTypes MASS_PICOGRAM =
            new UnitTypes(QuantityType.MASS, 2, Units.resolve(Mass.Unit.class, "pg"), "PICOGRAM", "pg");

    /** Mass.NANOGRAM unit type with code 3. */
    public static final UnitTypes MASS_NANOGRAM =
            new UnitTypes(QuantityType.MASS, 3, Units.resolve(Mass.Unit.class, "ng"), "NANOGRAM", "ng");

    /** Mass.MICROGRAM unit type with code 4. */
    public static final UnitTypes MASS_MICROGRAM = new UnitTypes(QuantityType.MASS, 4, Mass.Unit.mug, "MICROGRAM", "μg");

    /** Mass.MILLIGRAM unit type with code 5. */
    public static final UnitTypes MASS_MILLIGRAM = new UnitTypes(QuantityType.MASS, 5, Mass.Unit.mg, "MILLIGRAM", "mg");

    /** Mass.GRAM unit type with code 6. */
    public static final UnitTypes MASS_GRAM = new UnitTypes(QuantityType.MASS, 6, Mass.Unit.g, "GRAM", "kg");

    /** Mass.MEGAGRAM unit type with code 7. */
    public static final UnitTypes MASS_MEGAGRAM =
            new UnitTypes(QuantityType.MASS, 7, Units.resolve(Mass.Unit.class, "Mg"), "MEGAGRAM", "Mg");

    /** Mass.GIGAGRAM unit type with code 8. */
    public static final UnitTypes MASS_GIGAGRAM =
            new UnitTypes(QuantityType.MASS, 8, Units.resolve(Mass.Unit.class, "Gg"), "GIGAGRAM", "Gg");

    /** Mass.TERAGRAM unit type with code 9. */
    public static final UnitTypes MASS_TERAGRAM =
            new UnitTypes(QuantityType.MASS, 9, Units.resolve(Mass.Unit.class, "Tg"), "TERAGRAM", "Tg");

    /** Mass.PETAGRAM unit type with code 10. */
    public static final UnitTypes MASS_PETAGRAM =
            new UnitTypes(QuantityType.MASS, 10, Units.resolve(Mass.Unit.class, "Pg"), "PETAGRAM", "Pg");

    /** Mass.MICROELECTRONVOLT unit type with code 11. */
    public static final UnitTypes MASS_MICROELECTRONVOLT =
            new UnitTypes(QuantityType.MASS, 11, Mass.Unit.mueV, "MICROELECTRONVOLT", "μeV");

    /** Mass.MILLIELECTRONVOLT unit type with code 12. */
    public static final UnitTypes MASS_MILLIELECTRONVOLT =
            new UnitTypes(QuantityType.MASS, 12, Mass.Unit.meV, "MILLIELECTRONVOLT", "meV");

    /** Mass.ELECTRONVOLT unit type with code 13. */
    public static final UnitTypes MASS_ELECTRONVOLT = new UnitTypes(QuantityType.MASS, 13, Mass.Unit.eV, "ELECTRONVOLT", "eV");

    /** Mass.KILOELECTRONVOLT unit type with code 14. */
    public static final UnitTypes MASS_KILOELECTRONVOLT =
            new UnitTypes(QuantityType.MASS, 14, Mass.Unit.keV, "KILOELECTRONVOLT", "keV");

    /** Mass.MEGAELECTRONVOLT unit type with code 15. */
    public static final UnitTypes MASS_MEGAELECTRONVOLT =
            new UnitTypes(QuantityType.MASS, 15, Mass.Unit.MeV, "MEGAELECTRONVOLT", "MeV");

    /** Mass.GIGAELECTRONVOLT unit type with code 16. */
    public static final UnitTypes MASS_GIGAELECTRONVOLT =
            new UnitTypes(QuantityType.MASS, 16, Mass.Unit.GeV, "GIGAELECTRONVOLT", "GeV");

    /** Mass.TERAELECTRONVOLT unit type with code 17. */
    public static final UnitTypes MASS_TERAELECTRONVOLT = new UnitTypes(QuantityType.MASS, 17,
            Mass.Unit.eV.deriveUnit("TeV", "teraelectronvolt", 1E12, UnitSystem.OTHER), "TERAELECTRONVOLT", "TeV");

    /** Mass.PETAELECTRONVOLT unit type with code 18. */
    public static final UnitTypes MASS_PETAELECTRONVOLT = new UnitTypes(QuantityType.MASS, 18,
            Mass.Unit.eV.deriveUnit("PeV", "petaelectronvolt", 1E15, UnitSystem.OTHER), "PETAELECTRONVOLT", "PeV");

    /** Mass.EXAELECTRONVOLT unit type with code 19. */
    public static final UnitTypes MASS_EXAELECTRONVOLT = new UnitTypes(QuantityType.MASS, 19,
            Mass.Unit.eV.deriveUnit("EeV", "exaelectronvolt", 1E18, UnitSystem.OTHER), "EXAELECTRONVOLT", "EeV");

    /** Mass.OUNCE unit type with code 20. */
    public static final UnitTypes MASS_OUNCE = new UnitTypes(QuantityType.MASS, 20, Mass.Unit.oz, "OUNCE", "oz");

    /** Mass.POUND unit type with code 21. */
    public static final UnitTypes MASS_POUND = new UnitTypes(QuantityType.MASS, 21, Mass.Unit.lb, "POUND", "lb");

    /** Mass.DALTON unit type with code 22. */
    public static final UnitTypes MASS_DALTON = new UnitTypes(QuantityType.MASS, 22, Mass.Unit.Da, "DALTON", "Da");

    /** Mass.TON_LONG unit type with code 23. */
    public static final UnitTypes MASS_TON_LONG =
            new UnitTypes(QuantityType.MASS, 23, Mass.Unit.long_tn, "TON_LONG", "ton (long)");

    /** Mass.TON_SHORT unit type with code 24. */
    public static final UnitTypes MASS_TON_SHORT =
            new UnitTypes(QuantityType.MASS, 24, Mass.Unit.sh_tn, "TON_SHORT", "ton (short)");

    /** Mass.TONNE unit type with code 25. */
    public static final UnitTypes MASS_TONNE = new UnitTypes(QuantityType.MASS, 25, Mass.Unit.t, "TONNE", "tonne");

    /* ==================================================== POWER ====================================================== */

    /** Power.WATT unit type with code 0. */
    public static final UnitTypes POWER_WATT = new UnitTypes(QuantityType.POWER, 0, Power.Unit.W, "WATT", "W");

    /** Power.FEMTOWATT unit type with code 1. */
    public static final UnitTypes POWER_FEMTOWATT =
            new UnitTypes(QuantityType.POWER, 1, Units.resolve(Power.Unit.class, "fW"), "FEMTOWATT", "fW");

    /** Power.PICOWATT unit type with code 2. */
    public static final UnitTypes POWER_PICOWATT =
            new UnitTypes(QuantityType.POWER, 2, Units.resolve(Power.Unit.class, "pW"), "PICOWATT", "pW");

    /** Power.NANOWATT unit type with code 3. */
    public static final UnitTypes POWER_NANOWATT =
            new UnitTypes(QuantityType.POWER, 3, Units.resolve(Power.Unit.class, "nW"), "NANOWATT", "nW");

    /** Power.MICROWATT unit type with code 4. */
    public static final UnitTypes POWER_MICROWATT = new UnitTypes(QuantityType.POWER, 4, Power.Unit.muW, "MICROWATT", "μW");

    /** Power.MILLIWATT unit type with code 5. */
    public static final UnitTypes POWER_MILLIWATT = new UnitTypes(QuantityType.POWER, 5, Power.Unit.mW, "MILLIWATT", "mW");

    /** Power.KILOWATT unit type with code 6. */
    public static final UnitTypes POWER_KILOWATT = new UnitTypes(QuantityType.POWER, 6, Power.Unit.kW, "KILOWATT", "kW");

    /** Power.MEGAWATT unit type with code 7. */
    public static final UnitTypes POWER_MEGAWATT = new UnitTypes(QuantityType.POWER, 7, Power.Unit.MW, "MEGAWATT", "MW");

    /** Power.GIGAWATT unit type with code 8. */
    public static final UnitTypes POWER_GIGAWATT = new UnitTypes(QuantityType.POWER, 8, Power.Unit.GW, "GIGAWATT", "GW");

    /** Power.TERAWATT unit type with code 9. */
    public static final UnitTypes POWER_TERAWATT = new UnitTypes(QuantityType.POWER, 9, Power.Unit.TW, "TERAWATT", "TW");

    /** Power.PETAWATT unit type with code 10. */
    public static final UnitTypes POWER_PETAWATT = new UnitTypes(QuantityType.POWER, 10, Power.Unit.PW, "PETAWATT", "PW");

    /** Power.ERG_PER_SECOND unit type with code 11. */
    public static final UnitTypes POWER_ERG_PER_SECOND =
            new UnitTypes(QuantityType.POWER, 11, Power.Unit.erg_s, "ERG_PER_SECOND", "erg/s");

    /** Power.FOOT_POUND_FORCE_PER_SECOND unit type with code 12. */
    public static final UnitTypes POWER_FOOT_POUND_FORCE_PER_SECOND =
            new UnitTypes(QuantityType.POWER, 12, Power.Unit.ft_lbf_s, "FOOT_POUND_FORCE_s", "ft.lbf/s");

    /** Power.FOOT_POUND_FORCE_PER_MINUTE unit type with code 13. */
    public static final UnitTypes POWER_FOOT_POUND_FORCE_PER_MINUTE =
            new UnitTypes(QuantityType.POWER, 13, Power.Unit.ft_lbf_min, "FOOT_POUND_FORCE_PER_MINUTE", "ft.lbf/min");

    /** Power.FOOT_POUND_FORCE_PER_HOUR unit type with code 14. */
    public static final UnitTypes POWER_FOOT_POUND_FORCE_PER_HOUR =
            new UnitTypes(QuantityType.POWER, 14, Power.Unit.ft_lbf_h, "FOOT_POUND_FORCE_PER_HOUR", "ft.lbf/h");

    /** Power.HORSEPOWER_METRIC unit type with code 15. */
    public static final UnitTypes POWER_HORSEPOWER_METRIC =
            new UnitTypes(QuantityType.POWER, 15, Power.Unit.hp_M, "HORSEPOWER_METRIC", "hp");

    /** Power.STHENE_METER_PER_SECOND unit type with code 16. */
    public static final UnitTypes POWER_STHENE_METER_PER_SECOND =
            new UnitTypes(QuantityType.POWER, 16, Power.Unit.sn_m_s, "STHENE_METER_PER_SECOND", "sth/s");

    /* ==================================================== PRESSURE =================================================== */

    /** Pressure.PASCAL unit type with code 0. */
    public static final UnitTypes PRESSURE_PASCAL = new UnitTypes(QuantityType.PRESSURE, 0, Pressure.Unit.Pa, "PASCAL", "Pa");

    /** Pressure.HECTOPASCAL unit type with code 1. */
    public static final UnitTypes PRESSURE_HECTOPASCAL =
            new UnitTypes(QuantityType.PRESSURE, 1, Pressure.Unit.hPa, "HECTOPASCAL", "hPa");

    /** Pressure.KILOPASCAL unit type with code 2. */
    public static final UnitTypes PRESSURE_KILOPASCAL =
            new UnitTypes(QuantityType.PRESSURE, 2, Pressure.Unit.kPa, "KILOPASCAL", "kPa");

    /** Pressure.ATMOSPHERE_STANDARD unit type with code 3. */
    public static final UnitTypes PRESSURE_ATMOSPHERE_STANDARD =
            new UnitTypes(QuantityType.PRESSURE, 3, Pressure.Unit.atm, "ATMOSPHERE_STANDARD", "atm");

    /** Pressure.ATMOSPHERE_TECHNICAL unit type with code 4. */
    public static final UnitTypes PRESSURE_ATMOSPHERE_TECHNICAL =
            new UnitTypes(QuantityType.PRESSURE, 4, Pressure.Unit.at, "ATMOSPHERE_TECHNICAL", "at");

    /** Pressure.MILLIBAR unit type with code 5. */
    public static final UnitTypes PRESSURE_MILLIBAR =
            new UnitTypes(QuantityType.PRESSURE, 5, Pressure.Unit.mbar, "MILLIBAR", "mbar");

    /** Pressure.BAR unit type with code 6. */
    public static final UnitTypes PRESSURE_BAR = new UnitTypes(QuantityType.PRESSURE, 6, Pressure.Unit.bar, "BAR", "bar");

    /** Pressure.BARYE unit type with code 7. */
    public static final UnitTypes PRESSURE_BARYE = new UnitTypes(QuantityType.PRESSURE, 7, Pressure.Unit.Ba, "BARYE", "Ba");

    /** Pressure.MILLIMETER_MERCURY unit type with code 8. */
    public static final UnitTypes PRESSURE_MILLIMETER_MERCURY =
            new UnitTypes(QuantityType.PRESSURE, 8, Pressure.Unit.mmHg, "MILLIMETER_MERCURY", "mmHg");

    /** Pressure.CENTIMETER_MERCURY unit type with code 9. */
    public static final UnitTypes PRESSURE_CENTIMETER_MERCURY =
            new UnitTypes(QuantityType.PRESSURE, 9, Pressure.Unit.cmHg, "CENTIMETER_MERCURY", "cmHg");

    /** Pressure.INCH_MERCURY unit type with code 10. */
    public static final UnitTypes PRESSURE_INCH_MERCURY =
            new UnitTypes(QuantityType.PRESSURE, 10, Pressure.Unit.inHg, "INCH_MERCURY", "inHg");

    /** Pressure.FOOT_MERCURY unit type with code 11. */
    public static final UnitTypes PRESSURE_FOOT_MERCURY =
            new UnitTypes(QuantityType.PRESSURE, 11, Pressure.Unit.ftHg, "FOOT_MERCURY", "ftHg");

    /** Pressure.KGF_PER_SQUARE_MM unit type with code 12. */
    public static final UnitTypes PRESSURE_KGF_PER_SQUARE_MM =
            new UnitTypes(QuantityType.PRESSURE, 12, Pressure.Unit.kgf_mm2, "KGF_PER_SQUARE_MM", "kgf/mm2");

    /** Pressure.PIEZE unit type with code 13. */
    public static final UnitTypes PRESSURE_PIEZE = new UnitTypes(QuantityType.PRESSURE, 13, Pressure.Unit.pz, "PIEZE", "pz");

    /** Pressure.POUND_PER_SQUARE_INCH unit type with code 14. */
    public static final UnitTypes PRESSURE_POUND_PER_SQUARE_INCH =
            new UnitTypes(QuantityType.PRESSURE, 14, Pressure.Unit.lbf_in2, "POUND_PER_SQUARE_INCH", "lb/in2");

    /** Pressure.POUND_PER_SQUARE_FOOT unit type with code 15. */
    public static final UnitTypes PRESSURE_POUND_PER_SQUARE_FOOT =
            new UnitTypes(QuantityType.PRESSURE, 15, Pressure.Unit.lbf_ft2, "POUND_PER_SQUARE_FOOT", "lb/ft2");

    /** Pressure.TORR unit type with code 16. */
    public static final UnitTypes PRESSURE_TORR = new UnitTypes(QuantityType.PRESSURE, 16, Pressure.Unit.torr, "TORR", "torr");

    /* ==================================================== SPEED ====================================================== */

    /** Speed.METER_PER_SECOND unit type with code 0. */
    public static final UnitTypes SPEED_METER_PER_SECOND =
            new UnitTypes(QuantityType.SPEED, 0, Speed.Unit.m_s, "METER_PER_SECOND", "m/s");

    /** Speed.METER_PER_HOUR unit type with code 1. */
    public static final UnitTypes SPEED_METER_PER_HOUR =
            new UnitTypes(QuantityType.SPEED, 1, Speed.Unit.m_h, "METER_PER_HOUR", "m/h");

    /** Speed.KM_PER_SECOND unit type with code 2. */
    public static final UnitTypes SPEED_KM_PER_SECOND =
            new UnitTypes(QuantityType.SPEED, 2, Speed.Unit.km_s, "KM_PER_SECOND", "km/s");

    /** Speed.KM_PER_HOUR unit type with code 3. */
    public static final UnitTypes SPEED_KM_PER_HOUR =
            new UnitTypes(QuantityType.SPEED, 3, Speed.Unit.km_h, "KM_PER_HOUR", "km/h");

    /** Speed.INCH_PER_SECOND unit type with code 4. */
    public static final UnitTypes SPEED_INCH_PER_SECOND =
            new UnitTypes(QuantityType.SPEED, 4, Speed.Unit.in_s, "INCH_PER_SECOND", "in/s");

    /** Speed.INCH_PER_MINUTE unit type with code 5. */
    public static final UnitTypes SPEED_INCH_PER_MINUTE =
            new UnitTypes(QuantityType.SPEED, 5, Speed.Unit.in_min, "INCH_PER_MINUTE", "in/min");

    /** Speed.INCH_PER_HOUR unit type with code 6. */
    public static final UnitTypes SPEED_INCH_PER_HOUR =
            new UnitTypes(QuantityType.SPEED, 6, Speed.Unit.in_h, "INCH_PER_HOUR", "in/h");

    /** Speed.FOOT_PER_SECOND unit type with code 7. */
    public static final UnitTypes SPEED_FOOT_PER_SECOND =
            new UnitTypes(QuantityType.SPEED, 7, Speed.Unit.ft_s, "FOOT_PER_SECOND", "ft/s");

    /** Speed.FOOT_PER_MINUTE unit type with code 8. */
    public static final UnitTypes SPEED_FOOT_PER_MINUTE =
            new UnitTypes(QuantityType.SPEED, 8, Speed.Unit.ft_min, "FOOT_PER_MINUTE", "ft/min");

    /** Speed.FOOT_PER_HOUR unit type with code 9. */
    public static final UnitTypes SPEED_FOOT_PER_HOUR =
            new UnitTypes(QuantityType.SPEED, 9, Speed.Unit.ft_h, "FOOT_PER_HOUR", "ft/h");

    /** Speed.MILE_PER_SECOND unit type with code 10. */
    public static final UnitTypes SPEED_MILE_PER_SECOND =
            new UnitTypes(QuantityType.SPEED, 10, Speed.Unit.mi_s, "MILE_PER_SECOND", "mi/s");

    /** Speed.MILE_PER_MINUTE unit type with code 11. */
    public static final UnitTypes SPEED_MILE_PER_MINUTE =
            new UnitTypes(QuantityType.SPEED, 11, Speed.Unit.mi_min, "MILE_PER_MINUTE", "mi/min");

    /** Speed.MILE_PER_HOUR unit type with code 12. */
    public static final UnitTypes SPEED_MILE_PER_HOUR =
            new UnitTypes(QuantityType.SPEED, 12, Speed.Unit.mi_h, "MILE_PER_HOUR", "mi/h");

    /** Speed.KNOT unit type with code 13. */
    public static final UnitTypes SPEED_KNOT = new UnitTypes(QuantityType.SPEED, 13, Speed.Unit.kt, "KNOT", "kt");

    /* ================================================== TEMPERATURE ================================================== */

    /** Temperature.KELVIN unit type with code 0. */
    public static final UnitTypes TEMPERATURE_KELVIN =
            new UnitTypes(QuantityType.TEMPERATUREDIFFERENCE, 0, Temperature.Unit.K, "KELVIN", "K");

    /** Temperature.DEGREE_CELSIUS unit type with code 1. */
    public static final UnitTypes TEMPERATURE_DEGREE_CELSIUS =
            new UnitTypes(QuantityType.TEMPERATUREDIFFERENCE, 1, Temperature.Unit.degC, "DEGREE_CELSIUS", "OC");

    /** Temperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final UnitTypes TEMPERATURE_DEGREE_FAHRENHEIT =
            new UnitTypes(QuantityType.TEMPERATUREDIFFERENCE, 2, Temperature.Unit.degF, "DEGREE_FAHRENHEIT", "OF");

    /** Temperature.DEGREE_RANKINE unit type with code 3. */
    public static final UnitTypes TEMPERATURE_DEGREE_RANKINE =
            new UnitTypes(QuantityType.TEMPERATUREDIFFERENCE, 3, Temperature.Unit.degR, "DEGREE_RANKINE", "OR");

    /** Temperature.DEGREE_REAUMUR unit type with code 4. */
    public static final UnitTypes TEMPERATURE_DEGREE_REAUMUR =
            new UnitTypes(QuantityType.TEMPERATUREDIFFERENCE, 4, Temperature.Unit.degRe, "DEGREE_REAUMUR", "ORé");

    /* ============================================= ABSOLUTETEMPERATURE =============================================== */

    /** AbsoluteTemperature.KELVIN unit type with code 0. */
    public static final UnitTypes ABSOLUTETEMPERATURE_KELVIN =
            new UnitTypes(QuantityType.TEMPERATURE, 0, Temperature.Unit.K, "K", "K");

    /** AbsoluteTemperature.DEGREE_CELSIUS unit type with code 1. */
    public static final UnitTypes ABSOLUTETEMPERATURE_DEGREE_CELSIUS =
            new UnitTypes(QuantityType.TEMPERATURE, 1, Temperature.Unit.degC, "DEGREE_CELSIUS", "OC");

    /** AbsoluteTemperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final UnitTypes ABSOLUTETEMPERATURE_DEGREE_FAHRENHEIT =
            new UnitTypes(QuantityType.TEMPERATURE, 2, Temperature.Unit.degF, "DEGREE_FAHRENHEIT", "OF");

    /** AbsoluteTemperature.DEGREE_RANKINE unit type with code 3. */
    public static final UnitTypes ABSOLUTETEMPERATURE_DEGREE_RANKINE =
            new UnitTypes(QuantityType.TEMPERATURE, 3, Temperature.Unit.degR, "DEGREE_RANKINE", "OR");

    /** AbsoluteTemperature.DEGREE_REAUMUR unit type with code 4. */
    public static final UnitTypes ABSOLUTETEMPERATURE_DEGREE_REAUMUR =
            new UnitTypes(QuantityType.TEMPERATURE, 4, Temperature.Unit.degRe, "DEGREE_REAUMUR", "ORé");

    /* =================================================== DURATION ==================================================== */

    /** Duration.SECOND unit type with code 0. */
    public static final UnitTypes DURATION_SECOND = new UnitTypes(QuantityType.DURATION, 0, Duration.Unit.s, "SECOND", "s");

    /** Duration.ATTOSECOND unit type with code 1. */
    public static final UnitTypes DURATION_ATTOSECOND =
            new UnitTypes(QuantityType.DURATION, 1, Units.resolve(Duration.Unit.class, "as"), "ATTOSECOND", "as");

    /** Duration.FEMTOSECOND unit type with code 2. */
    public static final UnitTypes DURATION_FEMTOSECOND =
            new UnitTypes(QuantityType.DURATION, 2, Units.resolve(Duration.Unit.class, "fs"), "FEMTOSECOND", "fs");

    /** Duration.PICOSECOND unit type with code 3. */
    public static final UnitTypes DURATION_PICOSECOND =
            new UnitTypes(QuantityType.DURATION, 3, Duration.Unit.ps, "PICOSECOND", "ps");

    /** Duration.NANOSECOND unit type with code 4. */
    public static final UnitTypes DURATION_NANOSECOND =
            new UnitTypes(QuantityType.DURATION, 4, Duration.Unit.ns, "NANOSECOND", "ns");

    /** Duration.MICROSECOND unit type with code 5. */
    public static final UnitTypes DURATION_MICROSECOND =
            new UnitTypes(QuantityType.DURATION, 5, Duration.Unit.mus, "MICROSECOND", "μs");

    /** Duration.MILLISECOND unit type with code 6. */
    public static final UnitTypes DURATION_MILLISECOND =
            new UnitTypes(QuantityType.DURATION, 6, Duration.Unit.ms, "MILLISECOND", "ms");

    /** Duration.MINUTE unit type with code 7. */
    public static final UnitTypes DURATION_MINUTE = new UnitTypes(QuantityType.DURATION, 7, Duration.Unit.min, "MINUTE", "min");

    /** Duration.HOUR unit type with code 8. */
    public static final UnitTypes DURATION_HOUR = new UnitTypes(QuantityType.DURATION, 8, Duration.Unit.h, "HOUR", "hr");

    /** Duration.DAY unit type with code 9. */
    public static final UnitTypes DURATION_DAY = new UnitTypes(QuantityType.DURATION, 9, Duration.Unit.day, "DAY", "day");

    /** Duration.WEEK unit type with code 10. */
    public static final UnitTypes DURATION_WEEK = new UnitTypes(QuantityType.DURATION, 10, Duration.Unit.wk, "WEEK", "wk");

    /* ===================================================== TIME ====================================================== */

    /** Time.SECOND unit type with code 0. */
    public static final UnitTypes TIME_SECOND = new UnitTypes(QuantityType.TIME, 0, Duration.Unit.s, "SECOND", "s");

    /** Time.ATTOSECOND unit type with code 1. */
    public static final UnitTypes TIME_ATTOSECOND =
            new UnitTypes(QuantityType.TIME, 1, Units.resolve(Duration.Unit.class, "as"), "ATTOSECOND", "as");

    /** Time.FEMTOSECOND unit type with code 2. */
    public static final UnitTypes TIME_FEMTOSECOND =
            new UnitTypes(QuantityType.TIME, 2, Units.resolve(Duration.Unit.class, "fs"), "FEMTOSECOND", "fs");

    /** Time.PICOSECOND unit type with code 3. */
    public static final UnitTypes TIME_PICOSECOND = new UnitTypes(QuantityType.TIME, 3, Duration.Unit.ps, "PICOSECOND", "ps");

    /** Time.NANOSECOND unit type with code 4. */
    public static final UnitTypes TIME_NANOSECOND = new UnitTypes(QuantityType.TIME, 4, Duration.Unit.ns, "NANOSECOND", "ns");

    /** Time.MICROSECOND unit type with code 5. */
    public static final UnitTypes TIME_MICROSECOND = new UnitTypes(QuantityType.TIME, 5, Duration.Unit.mus, "MICROSECOND", "μs");

    /** Time.MILLISECOND unit type with code 6. */
    public static final UnitTypes TIME_MILLISECOND = new UnitTypes(QuantityType.TIME, 6, Duration.Unit.ms, "MILLISECOND", "ms");

    /** Time.MINUTE unit type with code 7. */
    public static final UnitTypes TIME_MINUTE = new UnitTypes(QuantityType.TIME, 7, Duration.Unit.min, "MINUTE", "min");

    /** Time.HOUR unit type with code 8. */
    public static final UnitTypes TIME_HOUR = new UnitTypes(QuantityType.TIME, 8, Duration.Unit.h, "HOUR", "hr");

    /** Time.DAY unit type with code 9. */
    public static final UnitTypes TIME_DAY = new UnitTypes(QuantityType.TIME, 9, Duration.Unit.day, "DAY", "day");

    /** Time.WEEK unit type with code 10. */
    public static final UnitTypes TIME_WEEK = new UnitTypes(QuantityType.TIME, 10, Duration.Unit.wk, "WEEK", "wk");

    /* ==================================================== TORQUE ===================================================== */

    /** Torque.NEWTON_METER unit type with code 0. */
    public static final UnitTypes TORQUE_NEWTON_METER =
            new UnitTypes(QuantityType.TORQUE, 0, Torque.Unit.Nm, "NEWTON_METER", "Nm");

    /** Torque.POUND_FOOT unit type with code 1. */
    public static final UnitTypes TORQUE_POUND_FOOT =
            new UnitTypes(QuantityType.TORQUE, 1, Torque.Unit.lbf_ft, "POUND_FOOT", "lb.ft");

    /** Torque.POUND_INCH unit type with code 2. */
    public static final UnitTypes TORQUE_POUND_INCH =
            new UnitTypes(QuantityType.TORQUE, 2, Torque.Unit.lbf_in, "POUND_INCH", "lb.in");

    /** Torque.METER_KILOGRAM_FORCE unit type with code 3. */
    public static final UnitTypes TORQUE_METER_KILOGRAM_FORCE =
            new UnitTypes(QuantityType.TORQUE, 3, Torque.Unit.m_kgf, "METER_KILOGRAM_FORCE", "m.kgf");

    /* ==================================================== VOLUME ===================================================== */

    /** Volume.CUBIC_METER unit type with code 0. */
    public static final UnitTypes VOLUME_CUBIC_METER = new UnitTypes(QuantityType.VOLUME, 0, Volume.Unit.m3, "CUBIC_METER", "m3");

    /** Volume.CUBIC_ATTOMETER unit type with code 1. */
    public static final UnitTypes VOLUME_CUBIC_ATTOMETER =
            new UnitTypes(QuantityType.VOLUME, 1, Units.resolve(Volume.Unit.class, "am3"), "CUBIC_ATTOMETER", "am3");

    /** Volume.CUBIC_FEMTOMETER unit type with code 2. */
    public static final UnitTypes VOLUME_CUBIC_FEMTOMETER =
            new UnitTypes(QuantityType.VOLUME, 2, Units.resolve(Volume.Unit.class, "fm3"), "CUBIC_FEMTOMETER", "fm3");

    /** Volume.CUBIC_PICOMETER unit type with code 3. */
    public static final UnitTypes VOLUME_CUBIC_PICOMETER =
            new UnitTypes(QuantityType.VOLUME, 3, Units.resolve(Volume.Unit.class, "pm3"), "CUBIC_PICOMETER", "pm3");

    /** Volume.CUBIC_NANOMETER unit type with code 4. */
    public static final UnitTypes VOLUME_CUBIC_NANOMETER =
            new UnitTypes(QuantityType.VOLUME, 4, Units.resolve(Volume.Unit.class, "nm3"), "CUBIC_NANOMETER", "nm3");

    /** Volume.CUBIC_MICROMETER unit type with code 5. */
    public static final UnitTypes VOLUME_CUBIC_MICROMETER =
            new UnitTypes(QuantityType.VOLUME, 5, Units.resolve(Volume.Unit.class, "mum3"), "CUBIC_MICROMETER", "μm3");

    /** Volume.CUBIC_MILLIMETER unit type with code 6. */
    public static final UnitTypes VOLUME_CUBIC_MILLIMETER =
            new UnitTypes(QuantityType.VOLUME, 6, Volume.Unit.mm3, "CUBIC_MILLIMETER", "mm3");

    /** Volume.CUBIC_CENTIMETER unit type with code 7. */
    public static final UnitTypes VOLUME_CUBIC_CENTIMETER =
            new UnitTypes(QuantityType.VOLUME, 7, Volume.Unit.cm3, "CUBIC_CENTIMETER", "cm3");

    /** Volume.CUBIC_DECIMETER unit type with code 8. */
    public static final UnitTypes VOLUME_CUBIC_DECIMETER =
            new UnitTypes(QuantityType.VOLUME, 8, Volume.Unit.dm3, "CUBIC_DECIMETER", "dm3");

    /** Volume.CUBIC_DEKAMETER unit type with code 9. */
    public static final UnitTypes VOLUME_CUBIC_DEKAMETER =
            new UnitTypes(QuantityType.VOLUME, 9, Units.resolve(Volume.Unit.class, "dam3"), "CUBIC_DEKAMETER", "dam3");

    /** Volume.CUBIC_HECTOMETER unit type with code 10. */
    public static final UnitTypes VOLUME_CUBIC_HECTOMETER =
            new UnitTypes(QuantityType.VOLUME, 10, Volume.Unit.hm3, "CUBIC_HECTOMETER", "hm3");

    /** Volume.CUBIC_KILOMETER unit type with code 11. */
    public static final UnitTypes VOLUME_CUBIC_KILOMETER =
            new UnitTypes(QuantityType.VOLUME, 11, Volume.Unit.km3, "CUBIC_KILOMETER", "km3");

    /** Volume.CUBIC_MEGAMETER unit type with code 12. */
    public static final UnitTypes VOLUME_CUBIC_MEGAMETER =
            new UnitTypes(QuantityType.VOLUME, 12, Units.resolve(Volume.Unit.class, "Mm3"), "CUBIC_MEGAMETER", "Mm3");

    /** Volume.CUBIC_INCH unit type with code 13. */
    public static final UnitTypes VOLUME_CUBIC_INCH =
            new UnitTypes(QuantityType.VOLUME, 13, Volume.Unit.in3, "CUBIC_INCH", "in3");

    /** Volume.CUBIC_FOOT unit type with code 14. */
    public static final UnitTypes VOLUME_CUBIC_FOOT =
            new UnitTypes(QuantityType.VOLUME, 14, Volume.Unit.ft3, "CUBIC_FOOT", "ft3");

    /** Volume.CUBIC_YARD unit type with code 15. */
    public static final UnitTypes VOLUME_CUBIC_YARD =
            new UnitTypes(QuantityType.VOLUME, 15, Volume.Unit.yd3, "CUBIC_YARD", "yd3");

    /** Volume.CUBIC_MILE unit type with code 16. */
    public static final UnitTypes VOLUME_CUBIC_MILE =
            new UnitTypes(QuantityType.VOLUME, 16, Volume.Unit.mi3, "CUBIC_MILE", "mi3");

    /** Volume.LITER unit type with code 17. */
    public static final UnitTypes VOLUME_LITER = new UnitTypes(QuantityType.VOLUME, 17, Volume.Unit.L, "LITER", "l");

    /** Volume.GALLON_IMP unit type with code 18. */
    public static final UnitTypes VOLUME_GALLON_IMP =
            new UnitTypes(QuantityType.VOLUME, 18, Volume.Unit.gal_imp, "GALLON_IMP", "gal (imp)");

    /** Volume.GALLON_US_FLUID unit type with code 19. */
    public static final UnitTypes VOLUME_GALLON_US_FLUID =
            new UnitTypes(QuantityType.VOLUME, 19, Volume.Unit.gal_US, "GALLON_US_FLUID", "gal (US)");

    /** Volume.OUNCE_IMP_FLUID unit type with code 20. */
    public static final UnitTypes VOLUME_OUNCE_IMP_FLUID =
            new UnitTypes(QuantityType.VOLUME, 20, Volume.Unit.fl_oz_imp, "OUNCE_IMP_FLUID", "oz (imp)");

    /** Volume.OUNCE_US_FLUID unit type with code 21. */
    public static final UnitTypes VOLUME_OUNCE_US_FLUID =
            new UnitTypes(QuantityType.VOLUME, 21, Volume.Unit.fl_oz_US, "OUNCE_US_FLUID", "oz (US)");

    /** Volume.PINT_IMP unit type with code 22. */
    public static final UnitTypes VOLUME_PINT_IMP =
            new UnitTypes(QuantityType.VOLUME, 22, Volume.Unit.pt_imp, "PINT_IMP", "pt (imp)");

    /** Volume.PINT_US_FLUID unit type with code 23. */
    public static final UnitTypes VOLUME_PINT_US_FLUID =
            new UnitTypes(QuantityType.VOLUME, 23, Volume.Unit.pt_US, "PINT_US_FLUID", "pt (US)");

    /** Volume.QUART_IMP unit type with code 24. */
    public static final UnitTypes VOLUME_QUART_IMP =
            new UnitTypes(QuantityType.VOLUME, 24, Volume.Unit.qt_imp, "QUART_IMP", "qt (imp)");

    /** Volume.QUART_US_FLUID unit type with code 25. */
    public static final UnitTypes VOLUME_QUART_US_FLUID =
            new UnitTypes(QuantityType.VOLUME, 25, Volume.Unit.qt_US, "QUART_US_FLUID", "qt (US)");

    /** Volume.CUBIC_PARSEC unit type with code 26. */
    public static final UnitTypes VOLUME_CUBIC_PARSEC =
            new UnitTypes(QuantityType.VOLUME, 26, Volume.Unit.pc3, "CUBIC_PARSEC", "pc3");

    /** Volume.CUBIC_LIGHTYEAR unit type with code 27. */
    public static final UnitTypes VOLUME_CUBIC_LIGHTYEAR =
            new UnitTypes(QuantityType.VOLUME, 27, Volume.Unit.ly3, "CUBIC_LIGHTYEAR", "ly3");

    /* ================================================= ABSORBEDDOSE ================================================== */

    /** AbsorbedDose.GRAY unit type with code 0. */
    public static final UnitTypes ABSORBEDDOSE_GRAY =
            new UnitTypes(QuantityType.ABSORBEDDOSE, 0, AbsorbedDose.Unit.Gy, "GRAY", "Gy");

    /** AbsorbedDose.MILLIGRAY unit type with code 1. */
    public static final UnitTypes ABSORBEDDOSE_MILLIGRAY =
            new UnitTypes(QuantityType.ABSORBEDDOSE, 1, AbsorbedDose.Unit.mGy, "MILLIGRAY", "mGy");

    /** AbsorbedDose.MICROGRAY unit type with code 2. */
    public static final UnitTypes ABSORBEDDOSE_MICROGRAY =
            new UnitTypes(QuantityType.ABSORBEDDOSE, 2, AbsorbedDose.Unit.muGy, "MICROGRAY", "μGy");

    /** AbsorbedDose.ERG_PER_GRAM unit type with code 3. */
    public static final UnitTypes ABSORBEDDOSE_ERG_PER_GRAM =
            new UnitTypes(QuantityType.ABSORBEDDOSE, 3, AbsorbedDose.Unit.erg_g, "ERG_PER_GRAM", "erg/g");

    /** AbsorbedDose.RAD unit type with code 4. */
    public static final UnitTypes ABSORBEDDOSE_RAD =
            new UnitTypes(QuantityType.ABSORBEDDOSE, 4, AbsorbedDose.Unit.rad, "RAD", "rad");

    /* =============================================== AMOUNTOFSUBSTANCE =============================================== */

    /** AmountOfSubstance.MOLE unit type with code 0. */
    public static final UnitTypes AMOUNTOFSUBSTANCE_MOLE =
            new UnitTypes(QuantityType.AMOUNTOFSUBSTANCE, 0, AmountOfSubstance.Unit.mol, "MOLE", "mol");

    /** AmountOfSubstance.MILLIMOLE unit type with code 1. */
    public static final UnitTypes AMOUNTOFSUBSTANCE_MILLIMOLE =
            new UnitTypes(QuantityType.AMOUNTOFSUBSTANCE, 1, AmountOfSubstance.Unit.mmol, "MILLIMOLE", "mmol");

    /** AmountOfSubstance.MICROMOLE unit type with code 2. */
    public static final UnitTypes AMOUNTOFSUBSTANCE_MICROMOLE =
            new UnitTypes(QuantityType.AMOUNTOFSUBSTANCE, 2, AmountOfSubstance.Unit.mumol, "MICROMOLE", "μmol");

    /** AmountOfSubstance.NANOMOLE unit type with code 3. */
    public static final UnitTypes AMOUNTOFSUBSTANCE_NANOMOLE =
            new UnitTypes(QuantityType.AMOUNTOFSUBSTANCE, 3, AmountOfSubstance.Unit.nmol, "NANOMOLE", "nmol");

    /* ================================================ CATALYTICACTIVITY ============================================== */

    /** CatalyticActivity.KATAL unit type with code 0. */
    public static final UnitTypes CATALYTICACTIVITY_KATAL =
            new UnitTypes(QuantityType.CATALYTICACTIVITY, 0, CatalyticActivity.Unit.kat, "KATAL", "kat");

    /** CatalyticActivity.MILLIKATAL unit type with code 1. */
    public static final UnitTypes CATALYTICACTIVITY_MILLIKATAL =
            new UnitTypes(QuantityType.CATALYTICACTIVITY, 1, CatalyticActivity.Unit.mkat, "MILLIKATAL", "mkat");

    /** CatalyticActivity.MICROKATAL unit type with code 2. */
    public static final UnitTypes CATALYTICACTIVITY_MICROKATAL =
            new UnitTypes(QuantityType.CATALYTICACTIVITY, 2, CatalyticActivity.Unit.mukat, "MICROKATAL", "μkat");

    /** CatalyticActivity.NANOKATAL unit type with code 3. */
    public static final UnitTypes CATALYTICACTIVITY_NANOKATAL =
            new UnitTypes(QuantityType.CATALYTICACTIVITY, 3, CatalyticActivity.Unit.nkat, "NANOKATAL", "nkat");

    /* ============================================= ELECTRICALCAPACITANCE ============================================= */

    /** ElectricalCapacitance.FARAD unit type with code 0. */
    public static final UnitTypes ELECTRICALCAPACITANCE_FARAD =
            new UnitTypes(QuantityType.ELECTRICALCAPACITANCE, 0, ElectricalCapacitance.Unit.F, "FARAD", "F");

    /** ElectricalCapacitance.MILLIFARAD unit type with code 1. */
    public static final UnitTypes ELECTRICALCAPACITANCE_MILLIFARAD =
            new UnitTypes(QuantityType.ELECTRICALCAPACITANCE, 1, ElectricalCapacitance.Unit.mF, "MILLIFARAD", "mF");

    /** ElectricalCapacitance.MICROFARAD unit type with code 2. */
    public static final UnitTypes ELECTRICALCAPACITANCE_MICROFARAD =
            new UnitTypes(QuantityType.ELECTRICALCAPACITANCE, 2, ElectricalCapacitance.Unit.muF, "MICROFARAD", "uF");

    /** ElectricalCapacitance.NANOFARAD unit type with code 3. */
    public static final UnitTypes ELECTRICALCAPACITANCE_NANOFARAD =
            new UnitTypes(QuantityType.ELECTRICALCAPACITANCE, 3, ElectricalCapacitance.Unit.nF, "NANOFARAD", "nF");

    /** ElectricalCapacitance.PICOFARAD unit type with code 4. */
    public static final UnitTypes ELECTRICALCAPACITANCE_PICOFARAD =
            new UnitTypes(QuantityType.ELECTRICALCAPACITANCE, 4, ElectricalCapacitance.Unit.pF, "PICOFARAD", "pF");

    /* ============================================= ELECTRICALCONDUCTANCE ============================================= */

    /** ElectricalConductance.SIEMENS unit type with code 0. */
    public static final UnitTypes ELECTRICALCONDUCTANCE_SIEMENS =
            new UnitTypes(QuantityType.ELECTRICALCONDUCTANCE, 0, ElectricalConductance.Unit.S, "SIEMENS", "S");

    /** ElectricalConductance.MILLISIEMENS unit type with code 1. */
    public static final UnitTypes ELECTRICALCONDUCTANCE_MILLISIEMENS =
            new UnitTypes(QuantityType.ELECTRICALCONDUCTANCE, 1, ElectricalConductance.Unit.mS, "MILLISIEMENS", "mS");

    /** ElectricalConductance.MICROSIEMENS unit type with code 2. */
    public static final UnitTypes ELECTRICALCONDUCTANCE_MICROSIEMENS =
            new UnitTypes(QuantityType.ELECTRICALCONDUCTANCE, 2, ElectricalConductance.Unit.muS, "MICROSIEMENS", "μS");

    /** ElectricalConductance.NANOSIEMENS unit type with code 3. */
    public static final UnitTypes ELECTRICALCONDUCTANCE_NANOSIEMENS =
            new UnitTypes(QuantityType.ELECTRICALCONDUCTANCE, 3, ElectricalConductance.Unit.nS, "NANOSIEMENS", "nS");

    /* ============================================= ELECTRICALINDUCTANCE ============================================= */

    /** ElectricalInductance.HENRY unit type with code 0. */
    public static final UnitTypes ELECTRICALINDUCTANCE_HENRY =
            new UnitTypes(QuantityType.ELECTRICALINDUCTANCE, 0, ElectricalInductance.Unit.H, "HENRY", "H");

    /** ElectricalInductance.MILLIHENRY unit type with code 1. */
    public static final UnitTypes ELECTRICALINDUCTANCE_MILLIHENRY = new UnitTypes(QuantityType.ELECTRICALINDUCTANCE, 1,
            Units.resolve(ElectricalInductance.Unit.class, "mH"), "MILLIHENRY", "mH");

    /** ElectricalInductance.MICROHENRY unit type with code 2. */
    public static final UnitTypes ELECTRICALINDUCTANCE_MICROHENRY = new UnitTypes(QuantityType.ELECTRICALINDUCTANCE, 2,
            Units.resolve(ElectricalInductance.Unit.class, "muH"), "MICROHENRY", "μH");

    /** ElectricalInductance.NANOHENRY unit type with code 3. */
    public static final UnitTypes ELECTRICALINDUCTANCE_NANOHENRY = new UnitTypes(QuantityType.ELECTRICALINDUCTANCE, 3,
            Units.resolve(ElectricalInductance.Unit.class, "nH"), "NANOHENRY", "nH");

    /* ================================================= EQUIVALENTDOSE ================================================ */

    /** EquivalentDose.SIEVERT unit type with code 0. */
    public static final UnitTypes EQUIVALENTDOSE_SIEVERT =
            new UnitTypes(QuantityType.EQUIVALENTDOSE, 0, EquivalentDose.Unit.Sv, "SIEVERT", "Sv");

    /** EquivalentDose.MILLISIEVERT unit type with code 1. */
    public static final UnitTypes EQUIVALENTDOSE_MILLISIEVERT =
            new UnitTypes(QuantityType.EQUIVALENTDOSE, 1, EquivalentDose.Unit.mSv, "MILLISIEVERT", "mSv");

    /** EquivalentDose.MICROSIEVERT unit type with code 2. */
    public static final UnitTypes EQUIVALENTDOSE_MICROSIEVERT =
            new UnitTypes(QuantityType.EQUIVALENTDOSE, 2, EquivalentDose.Unit.muSv, "MICROSIEVERT", "μSv");

    /** EquivalentDose.REM unit type with code 3. */
    public static final UnitTypes EQUIVALENTDOSE_REM =
            new UnitTypes(QuantityType.EQUIVALENTDOSE, 3, EquivalentDose.Unit.rem, "REM", "rem");

    /* ================================================== ILLUMINANCE ================================================== */

    /** Illuminance.LUX unit type with code 0. */
    public static final UnitTypes ILLUMINANCE_LUX = new UnitTypes(QuantityType.ILLUMINANCE, 0, Illuminance.Unit.lx, "LUX", "lx");

    /** Illuminance.MILLILUX unit type with code 1. */
    public static final UnitTypes ILLUMINANCE_MILLILUX =
            new UnitTypes(QuantityType.ILLUMINANCE, 1, Illuminance.Unit.mlx, "MILLILUX", "mlx");

    /** Illuminance.MICROLUX unit type with code 2. */
    public static final UnitTypes ILLUMINANCE_MICROLUX =
            new UnitTypes(QuantityType.ILLUMINANCE, 2, Illuminance.Unit.mulx, "MICROLUX", "μlx");

    /** Illuminance.KILOLUX unit type with code 3. */
    public static final UnitTypes ILLUMINANCE_KILOLUX =
            new UnitTypes(QuantityType.ILLUMINANCE, 3, Illuminance.Unit.klx, "KILOLUX", "klux");

    /** Illuminance.PHOT unit type with code 4. */
    public static final UnitTypes ILLUMINANCE_PHOT =
            new UnitTypes(QuantityType.ILLUMINANCE, 4, Illuminance.Unit.ph, "PHOT", "ph");

    /** Illuminance.NOX unit type with code 5. */
    public static final UnitTypes ILLUMINANCE_NOX = new UnitTypes(QuantityType.ILLUMINANCE, 5, Illuminance.Unit.nx, "NOX", "nx");

    /* ================================================= LUMINOUSFLUX ================================================== */

    /** LuminousFlux.LUMEN unit type with code 0. */
    public static final UnitTypes LUMINOUSFLUX_LUMEN =
            new UnitTypes(QuantityType.LUMINOUSFLUX, 0, LuminousFlux.Unit.lm, "LUMEN", "lm");

    /* ============================================== LUMINOUSINTENSITY ================================================ */

    /** LuminousIntensity.CANDELA unit type with code 0. */
    public static final UnitTypes LUMINOUSINTENSITY_CANDELA =
            new UnitTypes(QuantityType.LUMINOUSINTENSITY, 0, LuminousIntensity.Unit.cd, "CANDELA", "cd");

    /* ============================================= MAGNETICFLUXDENSITY =============================================== */

    /** MagneticFluxDensity.TESLA unit type with code 0. */
    public static final UnitTypes MAGNETICFLUXDENSITY_TESLA =
            new UnitTypes(QuantityType.MAGNETICFLUXDENSITY, 0, MagneticFluxDensity.Unit.T, "TESLA", "T");

    /** MagneticFluxDensity.MILLITESLA unit type with code 1. */
    public static final UnitTypes MAGNETICFLUXDENSITY_MILLITESLA =
            new UnitTypes(QuantityType.MAGNETICFLUXDENSITY, 1, MagneticFluxDensity.Unit.mT, "MILLITESLA", "mT");

    /** MagneticFluxDensity.MICROTESLA unit type with code 2. */
    public static final UnitTypes MAGNETICFLUXDENSITY_MICROTESLA =
            new UnitTypes(QuantityType.MAGNETICFLUXDENSITY, 2, MagneticFluxDensity.Unit.muT, "MICROTESLA", "μT");

    /** MagneticFluxDensity.NANOTESLA unit type with code 3. */
    public static final UnitTypes MAGNETICFLUXDENSITY_NANOTESLA =
            new UnitTypes(QuantityType.MAGNETICFLUXDENSITY, 3, MagneticFluxDensity.Unit.nT, "NANOTESLA", "nT");

    /** MagneticFluxDensity.GAUSS unit type with code 4. */
    public static final UnitTypes MAGNETICFLUXDENSITY_GAUSS =
            new UnitTypes(QuantityType.MAGNETICFLUXDENSITY, 4, MagneticFluxDensity.Unit.G, "GAUSS", "G");

    /* ================================================ MAGNETICFLUX =================================================== */

    /** MagneticFlux.WEBER unit type with code 0. */
    public static final UnitTypes MAGNETICFLUX_WEBER =
            new UnitTypes(QuantityType.MAGNETICFLUX, 0, MagneticFlux.Unit.Wb, "WEBER", "Wb");

    /** MagneticFlux.MILLIWEBER unit type with code 1. */
    public static final UnitTypes MAGNETICFLUX_MILLIWEBER =
            new UnitTypes(QuantityType.MAGNETICFLUX, 1, MagneticFlux.Unit.mWb, "MILLIWEBER", "mWb");

    /** MagneticFlux.MICROWEBER unit type with code 2. */
    public static final UnitTypes MAGNETICFLUX_MICROWEBER =
            new UnitTypes(QuantityType.MAGNETICFLUX, 2, MagneticFlux.Unit.muWb, "MICROWEBER", "μWb");

    /** MagneticFlux.NANOWEBER unit type with code 3. */
    public static final UnitTypes MAGNETICFLUX_NANOWEBER =
            new UnitTypes(QuantityType.MAGNETICFLUX, 3, MagneticFlux.Unit.nWb, "NANOWEBER", "nWb");

    /** MagneticFlux.MAXWELL unit type with code 4. */
    public static final UnitTypes MAGNETICFLUX_MAXWELL =
            new UnitTypes(QuantityType.MAGNETICFLUX, 4, MagneticFlux.Unit.Mx, "MAXWELL", "Mx");

    /* ================================================ RADIOACTIVITY ================================================== */

    /** RadioActivity.BECQUEREL unit type with code 0. */
    public static final UnitTypes RADIOACTIVITY_BECQUEREL =
            new UnitTypes(QuantityType.RADIOACTIVITY, 0, RadioActivity.Unit.Bq, "BECQUEREL", "Bq");

    /** RadioActivity.KILOBECQUEREL unit type with code 1. */
    public static final UnitTypes RADIOACTIVITY_KILOBECQUEREL =
            new UnitTypes(QuantityType.RADIOACTIVITY, 1, RadioActivity.Unit.kBq, "KILOBECQUEREL", "kBq");

    /** RadioActivity.MEGABECQUEREL unit type with code 2. */
    public static final UnitTypes RADIOACTIVITY_MEGABECQUEREL =
            new UnitTypes(QuantityType.RADIOACTIVITY, 2, RadioActivity.Unit.MBq, "MEGABECQUEREL", "MBq");

    /** RadioActivity.GIGABECQUEREL unit type with code 3. */
    public static final UnitTypes RADIOACTIVITY_GIGABECQUEREL =
            new UnitTypes(QuantityType.RADIOACTIVITY, 3, RadioActivity.Unit.GBq, "GIGABECQUEREL", "GBq");

    /** RadioActivity.TERABECQUEREL unit type with code 4. */
    public static final UnitTypes RADIOACTIVITY_TERABECQUEREL =
            new UnitTypes(QuantityType.RADIOACTIVITY, 4, RadioActivity.Unit.TBq, "TERABECQUEREL", "TBq");

    /** RadioActivity.PETABECQUEREL unit type with code 5. */
    public static final UnitTypes RADIOACTIVITY_PETABECQUEREL =
            new UnitTypes(QuantityType.RADIOACTIVITY, 5, Units.resolve(RadioActivity.Unit.class, "PBq"), "PETABECQUEREL", "PBq");

    /** RadioActivity.CURIE unit type with code 6. */
    public static final UnitTypes RADIOACTIVITY_CURIE =
            new UnitTypes(QuantityType.RADIOACTIVITY, 6, RadioActivity.Unit.Ci, "CURIE", "Ci");

    /** RadioActivity.MILLICURIE unit type with code 7. */
    public static final UnitTypes RADIOACTIVITY_MILLICURIE =
            new UnitTypes(QuantityType.RADIOACTIVITY, 7, RadioActivity.Unit.mCi, "MILLICURIE", "mCi");

    /** RadioActivity.MICROCURIE unit type with code 8. */
    public static final UnitTypes RADIOACTIVITY_MICROCURIE =
            new UnitTypes(QuantityType.RADIOACTIVITY, 8, RadioActivity.Unit.muCi, "MICROCURIE", "μCi");

    /** RadioActivity.NANOCURIE unit type with code 9. */
    public static final UnitTypes RADIOACTIVITY_NANOCURIE =
            new UnitTypes(QuantityType.RADIOACTIVITY, 9, RadioActivity.Unit.nCi, "NANOCURIE", "nCi");

    /** RadioActivity.RUTHERFORD unit type with code 10. */
    public static final UnitTypes RADIOACTIVITY_RUTHERFORD =
            new UnitTypes(QuantityType.RADIOACTIVITY, 10, RadioActivity.Unit.Rd, "RUTHERFORD", "Rd");

    /* ============================================= ANGULARACCELERATION =============================================== */

    /** AngularAcceleration.RADIAN_PER_SECOND_2 unit type with code 0. */
    public static final UnitTypes ANGULARACCELERATION_RADIAN_PER_SECOND_2 = new UnitTypes(QuantityType.ANGULARACCELERATION, 0,
            AngularAcceleration.Unit.rad_s2, "RADIAN_PER_SECOND_SQUARED", "rad/s2");

    /** AngularAcceleration.DEGREE_PER_SECOND_2 unit type with code 1. */
    public static final UnitTypes ANGULARACCELERATION_DEGREE_PER_SECOND_2 = new UnitTypes(QuantityType.ANGULARACCELERATION, 1,
            AngularAcceleration.Unit.deg_s2, "DEGREE_PER_SECOND_SQUARED", "deg/s2");

    /** AngularAcceleration.ARCMINUTE_PER_SECOND_2 unit type with code 2. */
    public static final UnitTypes ANGULARACCELERATION_ARCMINUTE_PER_SECOND_2 = new UnitTypes(QuantityType.ANGULARACCELERATION, 2,
            AngularAcceleration.Unit.arcmin_s2, "ARCMINUTE_PER_SECOND_SQUARED", "arcmin/s2");

    /** AngularAcceleration.ARCSECOND_PER_SECOND_2 unit type with code 3. */
    public static final UnitTypes ANGULARACCELERATION_ARCSECOND_PER_SECOND_2 = new UnitTypes(QuantityType.ANGULARACCELERATION, 3,
            AngularAcceleration.Unit.arcsec_s2, "ARCSECOND_PER_SECOND_SQUARED", "arcsec/s2");

    /** AngularAcceleration.GRAD_PER_SECOND_2 unit type with code 4. */
    public static final UnitTypes ANGULARACCELERATION_GRAD_PER_SECOND_2 = new UnitTypes(QuantityType.ANGULARACCELERATION, 4,
            AngularAcceleration.Unit.grad_s2, "GRAD_PER_SECOND_SQUARED", "grad/s2");

    /** AngularAcceleration.CENTESIMAL_ARCMINUTE_PER_SECOND_SQUARED unit type with code 5. */
    public static final UnitTypes ANGULARACCELERATION_CENTECIMAL_ARCMINUTE_PER_SECOND_2 =
            new UnitTypes(QuantityType.ANGULARACCELERATION, 5, AngularAcceleration.Unit.cdm_s2,
                    "CENTECIMALARCMINUTE_PER_SECOND_SQUARED", "cdm/s2");

    /** AngularAcceleration.CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED unit type with code 6. */
    public static final UnitTypes ANGULARACCELERATION_CENTESIMAL_ARCSECOND_PER_SECOND_2 =
            new UnitTypes(QuantityType.ANGULARACCELERATION, 6, AngularAcceleration.Unit.cds_s2,
                    "CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED", "cds/s2");

    /* =============================================== ANGULARVELOCITY ================================================= */

    /** AngularVelocity.RADIAN_PER_SECOND unit type with code 0. */
    public static final UnitTypes ANGULARVELOCITY_RADIAN_PER_SECOND =
            new UnitTypes(QuantityType.ANGULARACCELERATION, 0, AngularVelocity.Unit.rad_s, "RADIAN_PER_SECOND", "rad/s");

    /** AngularVelocity.DEGREE_PER_SECOND unit type with code 1. */
    public static final UnitTypes ANGULARVELOCITY_DEGREE_PER_SECOND =
            new UnitTypes(QuantityType.ANGULARACCELERATION, 1, AngularVelocity.Unit.deg_s, "DEGREE_PER_SECOND", "deg/s");

    /** AngularVelocity.ARCMINUTE_PER_SECOND unit type with code 2. */
    public static final UnitTypes ANGULARVELOCITY_ARCMINUTE_PER_SECOND = new UnitTypes(QuantityType.ANGULARACCELERATION, 2,
            AngularVelocity.Unit.arcmin_s, "ARCMINUTE_PER_SECOND", "arcmin/s");

    /** AngularVelocity.ARCSECOND_PER_SECOND unit type with code 3. */
    public static final UnitTypes ANGULARVELOCITY_ARCSECOND_PER_SECOND = new UnitTypes(QuantityType.ANGULARACCELERATION, 3,
            AngularVelocity.Unit.arcsec_s, "ARCSECOND_PER_SECOND", "arcsec/s");

    /** AngularVelocity.GRAD_PER_SECOND unit type with code 4. */
    public static final UnitTypes ANGULARVELOCITY_GRAD_PER_SECOND =
            new UnitTypes(QuantityType.ANGULARACCELERATION, 4, AngularVelocity.Unit.grad_s, "GRAD_PER_SECOND", "grad/s");

    /** AngularVelocity.CENTESIMAL_ARCMINUTE_PER_SECOND unit type with code 5. */
    public static final UnitTypes ANGULARVELOCITY_CENTECIMAL_ARCMINUTE_PER_SECOND = new UnitTypes(
            QuantityType.ANGULARACCELERATION, 5, AngularVelocity.Unit.cdm_s, "CENTECIMALARCMINUTE_PER_SECOND", "cdm/s");

    /** AngularVelocity.CENTESIMAL_ARCSECOND_PER_SECOND unit type with code 6. */
    public static final UnitTypes ANGULARVELOCITY_CENTESIMAL_ARCSECOND_PER_SECOND = new UnitTypes(
            QuantityType.ANGULARACCELERATION, 6, AngularVelocity.Unit.cds_s, "CENTESIMAL_ARCSECOND_PER_SECOND", "cds/s");

    /* ================================================== MOMENTUM ===================================================== */

    /** Momentum.KILOGRAM_METER_PER_SECOND unit type with code 0. */
    public static final UnitTypes KILOGRAM_METER_PER_SECOND =
            new UnitTypes(QuantityType.ANGULARACCELERATION, 0, Momentum.Unit.kgm_s, "KILOGRAM_METER_PER_SECOND", "kgm/s");

    /* ================================================== END TYPES ==================================================== */

    /**
     * Make a unit type for serialization.
     * @param unitType the corresponding serialization unit type
     * @param code the code of the unit provided as an int
     * @param unit the djunits data type
     * @param name the unit name
     * @param abbreviation the unit abbreviation
     */
    public UnitTypes(final QuantityType unitType, final int code, final Unit<?, ?> unit, final String name,
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
        Map<Integer, UnitTypes> codeMap = codeUnitMap.get(this.quantityType);
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
    public static UnitTypes getDisplayType(final QuantityType unitType, final Integer code)
    {
        Map<Integer, UnitTypes> byteMap = codeUnitMap.get(unitType);
        return byteMap == null ? null : byteMap.get(code);
    }

    /**
     * Return the display type belonging to the display code.
     * @param quantityTypeCode the quantity type to search for
     * @param unitCode the unit code to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitTypes getDisplayType(final byte quantityTypeCode, final int unitCode)
    {
        QuantityType quantityType = QuantityType.getQuantityType(quantityTypeCode);
        Map<Integer, UnitTypes> codeMap = codeUnitMap.get(quantityType);
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
        Map<Integer, UnitTypes> codeMap = codeUnitMap.get(unitType);
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
        Map<Integer, UnitTypes> codeMap = codeUnitMap.get(unitType);
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
    public static UnitTypes getDisplayType(final Unit<?, ?> unit)
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
        UnitTypes displayType = type == null ? null : getDisplayType(unit);
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
        UnitTypes displayType = type == null ? null : getDisplayType(unit);
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
