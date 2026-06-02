package org.djutils.serialization;

import java.util.HashMap;
import java.util.Map;

import org.djunits.quantity.Angle;
import org.djunits.quantity.Duration;
import org.djunits.quantity.Length;
import org.djunits.quantity.Temperature;
import org.djunits.unit.Unit;
import org.djunits.unit.Units;
import org.djutils.exceptions.Throw;

/**
 * Defined absolute unit types.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public class AbsUnitType
{
    /** map per quantity of code number to unit. */
    private static Map<AbsQuantityType, Map<Integer, AbsUnitType>> codeUnitMap = new HashMap<>();

    /** map of unit to unit type. */
    private static Map<Unit<?, ?>, AbsUnitType> unitTypeMap = new HashMap<>();

    /** the code of the unit as a byte. */
    private final int code;

    /** the corresponding quantity. */
    private final AbsQuantityType absQuantityType;

    /** the unit. */
    private final Unit<?, ?> unit;

    /** the unit name. */
    private final String name;

    /** the unit description. */
    private final String abbreviation;

    /* =================================================== DIRECTION =================================================== */

    /** Direction.RADIAN unit type with code 0. */
    public static final AbsUnitType DIRECTION_RADIAN =
            new AbsUnitType(AbsQuantityType.DIRECTION, 0, Angle.Unit.rad, "RADIAN", "rad");

    /** Direction.ARCMINUTE unit type with code 1. */
    public static final AbsUnitType DIRECTION_ARCMINUTE =
            new AbsUnitType(AbsQuantityType.DIRECTION, 1, Angle.Unit.arcmin, "ARCMINUTE", "arcmin");

    /** Direction.ARCSECOND unit type with code 2. */
    public static final AbsUnitType DIRECTION_ARCSECOND =
            new AbsUnitType(AbsQuantityType.DIRECTION, 2, Angle.Unit.arcsec, "ARCSECOND", "arcsec");

    /** Direction.CENTESIMAL_ARCMINUTE unit type with code 3. */
    public static final AbsUnitType DIRECTION_CENTESIMAL_ARCMINUTE =
            new AbsUnitType(AbsQuantityType.DIRECTION, 3, Angle.Unit.cdm, "CENTESIMAL_ARCMINUTE", "centesimal_arcmin");

    /** Direction.CENTESIMAL_ARCSECOND unit type with code 4. */
    public static final AbsUnitType DIRECTION_CENTESIMAL_ARCSECOND =
            new AbsUnitType(AbsQuantityType.DIRECTION, 4, Angle.Unit.cds, "CENTESIMAL_ARCSECOND", "centesimal_arcsec");

    /** Direction.DEGREE unit type with code 5. */
    public static final AbsUnitType DIRECTION_DEGREE =
            new AbsUnitType(AbsQuantityType.DIRECTION, 5, Angle.Unit.deg, "DEGREE", "deg");

    /** Direction.GRAD unit type with code 6. */
    public static final AbsUnitType DIRECTION_GRAD =
            new AbsUnitType(AbsQuantityType.DIRECTION, 6, Angle.Unit.grad, "GRAD", "grad");

    /** Direction.PERCENT unit type with code 7. */
    public static final AbsUnitType DIRECTION_PERCENT =
            new AbsUnitType(AbsQuantityType.DIRECTION, 7, Angle.Unit.percent, "PERCENT", "%");

    /* =================================================== POSITION ==================================================== */

    /** Position.METER unit type with code 0. */
    public static final AbsUnitType POSITION_METER = new AbsUnitType(AbsQuantityType.POSITION, 0, Length.Unit.m, "METER", "m");

    /** Position.ATTOMETER unit type with code 1. */
    public static final AbsUnitType POSITION_ATTOMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 1, Units.resolve(Length.Unit.class, "am"), "ATTOMETER", "am");

    /** Position.FEMTOMETER unit type with code 2. */
    public static final AbsUnitType POSITION_FEMTOMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 2, Units.resolve(Length.Unit.class, "fm"), "FEMTOMETER", "fm");

    /** Position.PICOMETER unit type with code 3. */
    public static final AbsUnitType POSITION_PICOMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 3, Units.resolve(Length.Unit.class, "pm"), "PICOMETER", "pm");

    /** Position.NANOMETER unit type with code 4. */
    public static final AbsUnitType POSITION_NANOMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 4, Length.Unit.nm, "NANOMETER", "nm");

    /** Position.MICROMETER unit type with code 5. */
    public static final AbsUnitType POSITION_MICROMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 5, Length.Unit.mum, "MICROMETER", "μm");

    /** Position.MILLIMETER unit type with code 6. */
    public static final AbsUnitType POSITION_MILLIMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 6, Length.Unit.mm, "MILLIMETER", "mm");

    /** Position.CENTIMETER unit type with code 7. */
    public static final AbsUnitType POSITION_CENTIMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 7, Length.Unit.cm, "CENTIMETER", "cm");

    /** Position.DECIMETER unit type with code 8. */
    public static final AbsUnitType POSITION_DECIMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 8, Length.Unit.dm, "DECIMETER", "dm");

    /** Position.DEKAMETER unit type with code 9. */
    public static final AbsUnitType POSITION_DEKAMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 9, Units.resolve(Length.Unit.class, "dam"), "DEKAMETER", "dam");

    /** Position.HECTOMETER unit type with code 10. */
    public static final AbsUnitType POSITION_HECTOMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 10, Length.Unit.hm, "HECTOMETER", "hm");

    /** Position.KILOMETER unit type with code 11. */
    public static final AbsUnitType POSITION_KILOMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 11, Length.Unit.km, "KILOMETER", "km");

    /** Position.MEGAMETER unit type with code 12. */
    public static final AbsUnitType POSITION_MEGAMETER =
            new AbsUnitType(AbsQuantityType.POSITION, 12, Units.resolve(Length.Unit.class, "Mm"), "MEGAMETER", "Mm");

    /** Position.INCH unit type with code 13. */
    public static final AbsUnitType POSITION_INCH = new AbsUnitType(AbsQuantityType.POSITION, 13, Length.Unit.in, "INCH", "in");

    /** Position.FOOT unit type with code 14. */
    public static final AbsUnitType POSITION_FOOT = new AbsUnitType(AbsQuantityType.POSITION, 14, Length.Unit.ft, "FOOT", "ft");

    /** Position.YARD unit type with code 15. */
    public static final AbsUnitType POSITION_YARD = new AbsUnitType(AbsQuantityType.POSITION, 15, Length.Unit.yd, "YARD", "yd");

    /** Position.MILE unit type with code 16. */
    public static final AbsUnitType POSITION_MILE = new AbsUnitType(AbsQuantityType.POSITION, 16, Length.Unit.mi, "MILE", "mi");

    /** Position.NAUTICAL_MILE unit type with code 17. */
    public static final AbsUnitType POSITION_NAUTICAL_MILE =
            new AbsUnitType(AbsQuantityType.POSITION, 17, Length.Unit.NM, "NAUTICAL_MILE", "NM");

    /** Position.ASTRONOMICAL_UNIT unit type with code 18. */
    public static final AbsUnitType POSITION_ASTRONOMICAL_UNIT =
            new AbsUnitType(AbsQuantityType.POSITION, 18, Length.Unit.AU, "ASTRONOMICAL_UNIT", "AU");

    /** Position.PARSEC unit type with code 19. */
    public static final AbsUnitType POSITION_PARSEC =
            new AbsUnitType(AbsQuantityType.POSITION, 19, Length.Unit.pc, "PARSEC", "pc");

    /** Position.LIGHTYEAR unit type with code 20. */
    public static final AbsUnitType POSITION_LIGHTYEAR =
            new AbsUnitType(AbsQuantityType.POSITION, 20, Length.Unit.ly, "LIGHTYEAR", "ly");

    /** Position.ANGSTROM unit type with code 21. */
    public static final AbsUnitType POSITION_ANGSTROM =
            new AbsUnitType(AbsQuantityType.POSITION, 21, Length.Unit.A, "ANGSTROM", "Å");

    /* ================================================= TEMPERATURE =================================================== */

    /** AbsoluteTemperature.KELVIN unit type with code 0. */
    public static final AbsUnitType ABSOLUTETEMPERATURE_KELVIN =
            new AbsUnitType(AbsQuantityType.TEMPERATURE, 0, Temperature.Unit.K, "K", "K");

    /** AbsoluteTemperature.DEGREE_CELSIUS unit type with code 1. */
    public static final AbsUnitType ABSOLUTETEMPERATURE_DEGREE_CELSIUS =
            new AbsUnitType(AbsQuantityType.TEMPERATURE, 1, Temperature.Unit.degC, "DEGREE_CELSIUS", "OC");

    /** AbsoluteTemperature.DEGREE_FAHRENHEIT unit type with code 2. */
    public static final AbsUnitType ABSOLUTETEMPERATURE_DEGREE_FAHRENHEIT =
            new AbsUnitType(AbsQuantityType.TEMPERATURE, 2, Temperature.Unit.degF, "DEGREE_FAHRENHEIT", "OF");

    /** AbsoluteTemperature.DEGREE_RANKINE unit type with code 3. */
    public static final AbsUnitType ABSOLUTETEMPERATURE_DEGREE_RANKINE =
            new AbsUnitType(AbsQuantityType.TEMPERATURE, 3, Temperature.Unit.degR, "DEGREE_RANKINE", "OR");

    /** AbsoluteTemperature.DEGREE_REAUMUR unit type with code 4. */
    public static final AbsUnitType ABSOLUTETEMPERATURE_DEGREE_REAUMUR =
            new AbsUnitType(AbsQuantityType.TEMPERATURE, 4, Temperature.Unit.degRe, "DEGREE_REAUMUR", "ORé");

    /* ===================================================== TIME ====================================================== */

    /** Time.SECOND unit type with code 0. */
    public static final AbsUnitType TIME_SECOND = new AbsUnitType(AbsQuantityType.TIME, 0, Duration.Unit.s, "SECOND", "s");

    /** Time.ATTOSECOND unit type with code 1. */
    public static final AbsUnitType TIME_ATTOSECOND =
            new AbsUnitType(AbsQuantityType.TIME, 1, Units.resolve(Duration.Unit.class, "as"), "ATTOSECOND", "as");

    /** Time.FEMTOSECOND unit type with code 2. */
    public static final AbsUnitType TIME_FEMTOSECOND =
            new AbsUnitType(AbsQuantityType.TIME, 2, Units.resolve(Duration.Unit.class, "fs"), "FEMTOSECOND", "fs");

    /** Time.PICOSECOND unit type with code 3. */
    public static final AbsUnitType TIME_PICOSECOND =
            new AbsUnitType(AbsQuantityType.TIME, 3, Duration.Unit.ps, "PICOSECOND", "ps");

    /** Time.NANOSECOND unit type with code 4. */
    public static final AbsUnitType TIME_NANOSECOND =
            new AbsUnitType(AbsQuantityType.TIME, 4, Duration.Unit.ns, "NANOSECOND", "ns");

    /** Time.MICROSECOND unit type with code 5. */
    public static final AbsUnitType TIME_MICROSECOND =
            new AbsUnitType(AbsQuantityType.TIME, 5, Duration.Unit.mus, "MICROSECOND", "μs");

    /** Time.MILLISECOND unit type with code 6. */
    public static final AbsUnitType TIME_MILLISECOND =
            new AbsUnitType(AbsQuantityType.TIME, 6, Duration.Unit.ms, "MILLISECOND", "ms");

    /** Time.MINUTE unit type with code 7. */
    public static final AbsUnitType TIME_MINUTE = new AbsUnitType(AbsQuantityType.TIME, 7, Duration.Unit.min, "MINUTE", "min");

    /** Time.HOUR unit type with code 8. */
    public static final AbsUnitType TIME_HOUR = new AbsUnitType(AbsQuantityType.TIME, 8, Duration.Unit.h, "HOUR", "hr");

    /** Time.DAY unit type with code 9. */
    public static final AbsUnitType TIME_DAY = new AbsUnitType(AbsQuantityType.TIME, 9, Duration.Unit.day, "DAY", "day");

    /** Time.WEEK unit type with code 10. */
    public static final AbsUnitType TIME_WEEK = new AbsUnitType(AbsQuantityType.TIME, 10, Duration.Unit.wk, "WEEK", "wk");

    /* ================================================== END TYPES ==================================================== */

    /**
     * Make an absolute unit type for serialization.
     * @param absQuantityType the corresponding serialization unit type
     * @param code the code of the unit provided as an int
     * @param unit the djunits data type
     * @param name the unit name
     * @param abbreviation the unit abbreviation
     */
    public AbsUnitType(final AbsQuantityType absQuantityType, final int code, final Unit<?, ?> unit, final String name,
            final String abbreviation)
    {
        Throw.whenNull(absQuantityType, "absQuantityType should not be null");
        Throw.whenNull(unit, "unit should not be null");
        Throw.whenNull(name, "name should not be null");
        Throw.whenNull(abbreviation, "abbreviation should not be null");
        Throw.when(name.length() == 0, SerializationRuntimeException.class, "name should not be empty");
        Throw.when(abbreviation.length() == 0, SerializationRuntimeException.class, "abbreviation should not be empty");

        this.absQuantityType = absQuantityType;
        this.code = code;
        this.unit = unit;
        this.name = name;
        this.abbreviation = abbreviation;
        Map<Integer, AbsUnitType> codeMap = codeUnitMap.get(this.absQuantityType);
        if (codeMap == null)
        {
            codeMap = new HashMap<>();
            codeUnitMap.put(this.absQuantityType, codeMap);
        }
        codeMap.put(this.code, this);
        unitTypeMap.put(this.unit, this);
    }

    /**
     * Return the unit type belonging to the display code.
     * @param unitType the unit type to search for
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static AbsUnitType getAbsUnitType(final AbsQuantityType unitType, final Integer code)
    {
        Map<Integer, AbsUnitType> byteMap = codeUnitMap.get(unitType);
        return byteMap == null ? null : byteMap.get(code);
    }

    /**
     * Return the unit type belonging to the display code.
     * @param quantityTypeCode the quantity type to search for
     * @param unitCode the unit code to search for.
     * @return the unit type, or null if not found.
     */
    public static AbsUnitType getAbsUnitType(final byte quantityTypeCode, final int unitCode)
    {
        AbsQuantityType quantityType = AbsQuantityType.getAbsQuantityType(quantityTypeCode);
        Map<Integer, AbsUnitType> codeMap = codeUnitMap.get(quantityType);
        return codeMap == null ? null : codeMap.get(unitCode);
    }

    /**
     * Return the unit belonging to the unit code.
     * @param quantityTypeCode the quantity type to search for
     * @param unitCode the unit code to search for.
     * @return the unit type, or null if not found.
     */
    public static Unit<?, ?> getUnit(final byte quantityTypeCode, final int unitCode)
    {
        AbsQuantityType unitType = AbsQuantityType.getAbsQuantityType(quantityTypeCode);
        Map<Integer, AbsUnitType> codeMap = codeUnitMap.get(unitType);
        return codeMap == null ? null : codeMap.get(unitCode) == null ? null : codeMap.get(unitCode).unit;
    }

    /**
     * Return the unit belonging to the unit code.
     * @param absQuantityType the unit type to search for
     * @param code the code to search for.
     * @return the unit, or null if not found.
     */
    public static Unit<?, ?> getUnit(final AbsQuantityType absQuantityType, final int code)
    {
        Map<Integer, AbsUnitType> codeMap = codeUnitMap.get(absQuantityType);
        return codeMap == null ? null : codeMap.get(code) == null ? null : codeMap.get(code).unit;
    }

    /**
     * @return unitType
     */
    public AbsQuantityType getAbsQuantityType()
    {
        return this.absQuantityType;
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     */
    public static AbsUnitType getAbsUnitType(final Unit<?, ?> unit)
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
        AbsQuantityType type = AbsQuantityType.getAbsQuantityType(unit);
        AbsUnitType displayType = type == null ? null : getAbsUnitType(unit);
        return displayType == null ? null : displayType.getIntCode();
    }

    /**
     * Return the display code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     */
    public static byte getByteCode(final Unit<?, ?> unit)
    {
        AbsQuantityType type = AbsQuantityType.getAbsQuantityType(unit);
        AbsUnitType displayType = type == null ? null : getAbsUnitType(unit);
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
