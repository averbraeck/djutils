package org.djutils.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.djunits.quantity.Direction;
import org.djunits.quantity.Position;
import org.djunits.quantity.Temperature;
import org.djunits.quantity.Time;
import org.djunits.quantity.def.AbsBasic;
import org.djunits.unit.Unit;

/**
 * The absolute quantity types with their code, including static methods to quickly find an absolute quantity type.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. <a href="https://djutils.org/docs/license.html" target="_blank">
 * https://djutils.org/docs/license.html</a>.
 * <p>
 * @author Alexander Verbraeck
 */
public class AbsQuantityType
{
    /** the absolute quantity types from number to type. */
    private static Map<Byte, AbsQuantityType> byteTypeMap = new HashMap<>();

    /** the absolute quantity types from class to type. */
    private static Map<Class<? extends AbsBasic<?, ?, ?>>, AbsQuantityType> quantityTypeMap = new HashMap<>();

    /** {@link Direction} quantity type with code 4. */
    public static final AbsQuantityType DIRECTION =
            new AbsQuantityType(4, Direction.class, "Direction", "Direction (absolute)", "[rad]");

    /** {@link Position} quantity type with code 17. */
    public static final AbsQuantityType POSITION =
            new AbsQuantityType(17, Position.class, "Position", "Position (absolute)", "[m]");

    /** {@link Temperature} quantity type with code 24. */
    public static final AbsQuantityType TEMPERATURE =
            new AbsQuantityType(24, Temperature.class, "Temperature", "Temperature (absolute)", "[K]");

    /** {@link Time} quantity type with code 26. */
    public static final AbsQuantityType TIME = new AbsQuantityType(26, Time.class, "Time", "Time (absolute)", "[s]");

    /** The code of the absolute quantity as a byte. */
    private final byte code;

    /** The quantity class. */
    private final Class<? extends AbsBasic<?, ?, ?>> absQuantityClass;

    /** The quantity name. */
    private final String name;

    /** The quantity description. */
    private final String description;

    /** The SI or BASE unit as a String. */
    private final String siUnit;

    /**
     * Construct a new AbsQuantityType and put it in the maps.
     * @param code the byte code of the absolute quantity provided as an int
     * @param absQuantityClass the absolute quantity class
     * @param name the absolute quantity name
     * @param description the absolute quantity description
     * @param siUnit the SI or BASE unit as a String
     */
    public AbsQuantityType(final int code, final Class<? extends AbsBasic<?, ?, ?>> absQuantityClass, final String name,
            final String description, final String siUnit)
    {
        this.code = (byte) code;
        this.absQuantityClass = absQuantityClass;
        this.name = name;
        this.description = description;
        this.siUnit = siUnit;

        byteTypeMap.put(this.code, this);
        quantityTypeMap.put(this.absQuantityClass, this);
    }

    /**
     * Return the absolute quantity type belonging to the byte code.
     * @param code the code to search for.
     * @return the absolute quantity type, or null if not found.
     */
    public static AbsQuantityType getAbsQuantityType(final byte code)
    {
        return byteTypeMap.get(code);
    }

    /**
     * Return the absolute quantity class belonging to the byte code.
     * @param code the code to search for
     * @return the absolute quantity class, or null if not found
     */
    public static Class<? extends AbsBasic<?, ?, ?>> getAbsQuantityClass(final byte code)
    {
        AbsQuantityType type = byteTypeMap.get(code);
        return type == null ? null : type.getAbsQuantityClass();
    }

    /**
     * Return the absolute quantity type belonging to the absolute quantity.
     * @param absQuantity the absolute quantity to search for
     * @return the absolute quantity type, or null if not found
     */
    public static AbsQuantityType getAbsQuantityType(final AbsBasic<?, ?, ?> absQuantity)
    {
        return quantityTypeMap.get(absQuantity.getClass());
    }

    /**
     * Return the absolute quantity type belonging to the unit.
     * @param unit the unit to search for
     * @return the absolute quantity type, or null if not found
     */
    public static AbsQuantityType getAbsQuantityType(final Unit<?, ?> unit)
    {
        return quantityTypeMap.get(unit.ofSi(0.0).getClass());
    }

    /**
     * Return the byte code belonging to the absolute quantity class.
     * @param absQuantity the absolute quantity to search for
     * @return the absolute quantity type code
     * @throws IllegalArgumentException when quantity type could not be found
     */
    public static byte getAbsQuantityCode(final AbsBasic<?, ?, ?> absQuantity)
    {
        AbsQuantityType type = quantityTypeMap.get(absQuantity.getClass());
        if (type == null)
        {
            throw new IllegalArgumentException(
                    "Could not find absolute quantity type for ansolute quantity " + absQuantity + " in quantityTypeMap");
        }
        return type.getCode();
    }

    /**
     * Return the byte code of this AbsQuantityType.
     * @return the byte code of this AbsQuantityType
     */
    public final byte getCode()
    {
        return this.code;
    }

    /**
     * Return the Quantity class of this AbsQuantityType.
     * @return the Quantity class of this AbsQuantityType
     */
    public final Class<? extends AbsBasic<?, ?, ?>> getAbsQuantityClass()
    {
        return this.absQuantityClass;
    }

    /**
     * Return the name of the AbsQuantityType.
     * @return the name of this AbsQuantityType
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Return the description of this AbsQuantityType.
     * @return the description of this AbsQuantityType
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Return the SI or BASE unit of this AbsQuantityType as a String.
     * @return String the SI or BASE unit of this AbsQuantityType
     */
    public final String getSiUnit()
    {
        return this.siUnit;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.code, this.description, this.absQuantityClass, this.name, this.siUnit);
    }

    @SuppressWarnings("checkstyle:needbraces")
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbsQuantityType other = (AbsQuantityType) obj;
        return this.code == other.code && Objects.equals(this.description, other.description)
                && Objects.equals(this.absQuantityClass, other.absQuantityClass) && Objects.equals(this.name, other.name)
                && Objects.equals(this.siUnit, other.siUnit);
    }

    @Override
    public String toString()
    {
        return "AbsQuantityType [code=" + this.code + ", name=" + this.name + ", description=" + this.description + ", siUnit="
                + this.siUnit + "]";
    }

}
