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
import org.djunits.unit.MoneyPerAreaUnit;
import org.djunits.unit.MoneyPerDurationUnit;
import org.djunits.unit.MoneyPerEnergyUnit;
import org.djunits.unit.MoneyPerLengthUnit;
import org.djunits.unit.MoneyPerMassUnit;
import org.djunits.unit.MoneyPerVolumeUnit;
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
 * The Sim0MQ unit types with their code, including static methods to quickly find a unit type.
 * <p>
 * Copyright (c) 2016-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Mar 4, 2017 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SerializationUnits implements Serializable
{
    /** */
    private static final long serialVersionUID = 20170304L;

    /** the unit types from number to type. */
    private static Map<Byte, SerializationUnits> byteTypeMap = new HashMap<>();

    /** the unit types from class to type. */
    private static Map<Class<? extends Unit<?>>, SerializationUnits> unitTypeMap = new HashMap<>();

    /** Dimensionless unit type with code 0. */
    public static final SerializationUnits DIMENSIONLESS =
            new SerializationUnits(0, DimensionlessUnit.class, "Dimensionless", "Unit without a dimension", "[]");

    /** Acceleration unit type with code 1. */
    public static final SerializationUnits ACCELERATION =
            new SerializationUnits(1, AccelerationUnit.class, "Acceleration", "Acceleration", "[m/s^2]");

    /** AngleSolid unit type with code 2. */
    public static final SerializationUnits ANGLESOLID =
            new SerializationUnits(2, AngleSolidUnit.class, "AngleSolid", "Solid angle ", "[steradian]");

    /** Angle unit type with code 3. */
    public static final SerializationUnits ANGLE = new SerializationUnits(3, AngleUnit.class, "Angle", "Angle (relative)", "[rad]");

    /** Direction unit type with code 4. */
    public static final SerializationUnits DIRECTION =
            new SerializationUnits(4, DirectionUnit.class, "Direction", "Angle  (absolute)", "[rad]");

    /** Area unit type with code 5. */
    public static final SerializationUnits AREA = new SerializationUnits(5, AreaUnit.class, "Area", "Area", "[m^2]");

    /** Density unit type with code 6. */
    public static final SerializationUnits DENSITY =
            new SerializationUnits(6, DensityUnit.class, "Density", "Density based on mass and length", "[kg/m^3]");

    /** ElectricalCharge unit type with code 7. */
    public static final SerializationUnits ELECTRICALCHARGE =
            new SerializationUnits(7, ElectricalChargeUnit.class, "ElectricalCharge", "Electrical charge (Coulomb)", "[sA]");

    /** ElectricalCurrent unit type with code 8. */
    public static final SerializationUnits ELECTRICALCURRENT =
            new SerializationUnits(8, ElectricalCurrentUnit.class, "ElectricalCurrent", "Electrical current (Ampere)", "[A]");

    /** ElectricalPotential unit type with code 9. */
    public static final SerializationUnits ELECTRICALPOTENTIAL = new SerializationUnits(9, ElectricalPotentialUnit.class,
            "ElectricalPotential", "Electrical potential (Volt)", "[kgm^2/s^3A]");

    /** ElectricalResistance unit type with code 10. */
    public static final SerializationUnits ELECTRICALRESISTANCE = new SerializationUnits(10, ElectricalResistanceUnit.class,
            "ElectricalResistance", "Electrical resistance (Ohm)", "[kgm^2/s^3A^2]");

    /** Energy unit type with code 11. */
    public static final SerializationUnits ENERGY =
            new SerializationUnits(11, EnergyUnit.class, "Energy", "Energy (Joule)", "[kgm^2/s^2]");

    /** FlowMass unit type with code 12. */
    public static final SerializationUnits FLOWMASS =
            new SerializationUnits(12, FlowMassUnit.class, "FlowMass", "Mass flow rate ", "[kg/s]");

    /** FlowVolume unit type with code 13. */
    public static final SerializationUnits FLOWVOLUME =
            new SerializationUnits(13, FlowVolumeUnit.class, "FlowVolume", "Volume flow rate", "[m^3/s]");

    /** Force unit type with code 14. */
    public static final SerializationUnits FORCE = new SerializationUnits(14, ForceUnit.class, "Force", "Force (Newton)", "[kgm/s^2]");

    /** Frequency unit type with code 15. */
    public static final SerializationUnits FREQUENCY =
            new SerializationUnits(15, FrequencyUnit.class, "Frequency", "Frequency (Hz)", "[1/s]");

    /** Length unit type with code 16. */
    public static final SerializationUnits LENGTH = new SerializationUnits(16, LengthUnit.class, "Length", "Length (relative)", "[m]");

    /** Position unit type with code 17. */
    public static final SerializationUnits POSITION =
            new SerializationUnits(17, PositionUnit.class, "Position", "Length (absolute)", "[m]");

    /** LinearDensity unit type with code 18. */
    public static final SerializationUnits LINEARDENSITY =
            new SerializationUnits(18, LinearDensityUnit.class, "LinearDensity", "Linear density ", "[1/m]");

    /** Mass unit type with code 19. */
    public static final SerializationUnits MASS = new SerializationUnits(19, MassUnit.class, "Mass", "Mass", "[kg]");

    /** Power unit type with code 20. */
    public static final SerializationUnits POWER = new SerializationUnits(20, PowerUnit.class, "Power", "Power (Watt)", "[kgm^2/s^3]");

    /** Pressure unit type with code 21. */
    public static final SerializationUnits PRESSURE =
            new SerializationUnits(21, PressureUnit.class, "Pressure", "Pressure (Pascal)", "[kg/ms^2]");

    /** Speed unit type with code 22. */
    public static final SerializationUnits SPEED = new SerializationUnits(22, SpeedUnit.class, "Speed", "Speed", "[m/s]");

    /** Temperature unit type with code 23. */
    public static final SerializationUnits TEMPERATURE =
            new SerializationUnits(23, TemperatureUnit.class, "Temperature", "Temperature (relative)", "[K]");

    /** AbsoluteTemperature unit type with code 24. */
    public static final SerializationUnits ABSOLUTETEMPERATURE =
            new SerializationUnits(24, AbsoluteTemperatureUnit.class, "AbsoluteTemperature", "Temperature (absolute)", "[K]");

    /** Duration unit type with code 25. */
    public static final SerializationUnits DURATION = new SerializationUnits(25, DurationUnit.class, "Duration", "Time (relative)", "[s]");

    /** Time unit type with code 26. */
    public static final SerializationUnits TIME = new SerializationUnits(26, TimeUnit.class, "Time", "Time (absolute)", "[s]");

    /** Torque unit type with code 27. */
    public static final SerializationUnits TORQUE =
            new SerializationUnits(27, TorqueUnit.class, "Torque", "Torque (Newton-meter)", "[kgm^2/s^2]");

    /** Volume unit type with code 28. */
    public static final SerializationUnits VOLUME = new SerializationUnits(28, VolumeUnit.class, "Volume", "Volume", "[m^3]");

    /** Money unit type with code 100. */
    public static final SerializationUnits MONEY =
            new SerializationUnits(100, MoneyUnit.class, "Money", "Money (cost in e.g., $, €, ...)", "[$]");

    /** MoneyPerArea unit type with code 101. */
    public static final SerializationUnits MONEYPERAREA =
            new SerializationUnits(101, MoneyPerAreaUnit.class, "MoneyPerArea", "Money/Area (cost/m^2)", "[$/m^2]");

    /** MoneyPerEnergy unit type with code 102. */
    public static final SerializationUnits MONEYPERENERGY =
            new SerializationUnits(102, MoneyPerEnergyUnit.class, "MoneyPerEnergy", "Money/Energy (cost/W)", "[$s^3/kgm^2]");

    /** MoneyPerLength unit type with code 103. */
    public static final SerializationUnits MONEYPERLENGTH =
            new SerializationUnits(103, MoneyPerLengthUnit.class, "MoneyPerLength", "Money/Length (cost/m)", "[$/m]");

    /** MoneyPerMass unit type with code 104. */
    public static final SerializationUnits MONEYPERMASS =
            new SerializationUnits(104, MoneyPerMassUnit.class, "MoneyPerMass", "Money/Mass (cost/kg)", "[$/kg]");

    /** MoneyPerDuration unit type with code 105. */
    public static final SerializationUnits MONEYPERDURATION =
            new SerializationUnits(105, MoneyPerDurationUnit.class, "MoneyPerDuration", "Money/Duration (cost/s)", "[$/s]");

    /** MoneyPerVolume unit type with code 106. */
    public static final SerializationUnits MONEYPERVOLUME =
            new SerializationUnits(106, MoneyPerVolumeUnit.class, "MoneyPerVolume", "Money/Volume (cost/m^3)", "[$/m^3]");

    /** The code of the unit as a byte. */
    private final byte code;

    /** The djunits data type. */
    private final Class<? extends Unit<?>> djunitsType;

    /** The unit name. */
    private final String name;

    /** The unit description. */
    private final String description;

    /** The SI or default unit in SI-elements. */
    private final String siUnit;

    /**
     * Construct a new UnitType and put it in the maps.
     * @param code int; the byte code of the unit provided as an int
     * @param djunitsType Class&lt;U&gt;; the djunits data type
     * @param name String; the unit name
     * @param description String; the unit description
     * @param siUnit String; the SI or default unit in SI-elements
     * @param <U> the Unit
     */
    public <U extends Unit<U>> SerializationUnits(final int code, final Class<U> djunitsType, final String name,
            final String description, final String siUnit)
    {
        this.code = (byte) code;
        this.djunitsType = djunitsType;
        this.name = name;
        this.description = description;
        this.siUnit = siUnit;

        byteTypeMap.put(this.code, this);
        unitTypeMap.put(this.djunitsType, this);
    }

    /**
     * Return the unit type belonging to the byte code.
     * @param code byte; the code to search for.
     * @return UnitType; the unit type, or null if not found.
     */
    public static SerializationUnits getUnitType(final byte code)
    {
        return byteTypeMap.get(code);
    }

    /**
     * Return the unit class belonging to the byte code.
     * @param code byte; the code to search for.
     * @return Class; the unit class, or null if not found.
     */
    public static Class<? extends Unit<?>> getUnitClass(final byte code)
    {
        SerializationUnits type = byteTypeMap.get(code);
        return type == null ? null : type.getDjunitsType();
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit U; the unit to search for.
     * @return UnitType; the unit type, or null if not found.
     * @param <U> the Unit
     */
    public static <U extends Unit<U>> SerializationUnits getUnitType(final U unit)
    {
        return unitTypeMap.get(unit.getClass());
    }

    /**
     * Return the byte code belonging to the unit class.
     * @param unit U; the unit to search for.
     * @return byte; the unit type code, or null if not found.
     * @param <U> the Unit
     */
    public static <U extends Unit<U>> byte getUnitCode(final U unit)
    {
        SerializationUnits type = unitTypeMap.get(unit.getClass());
        return type == null ? null : type.getCode();
    }

    /**
     * Retrieve the byte code of this UnitType.
     * @return byte; the byte code of this UnitType
     */
    public final byte getCode()
    {
        return this.code;
    }

    /**
     * Retrieve the DJUNITS type of this UnitType.
     * @return Class; the DJUNITS type of this UnitType
     */
    public final Class<? extends Unit<?>> getDjunitsType()
    {
        return this.djunitsType;
    }

    /**
     * Retrieve the name of the UnitType.
     * @return String; the name of this UnitType
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the description of this UnitType.
     * @return String; the description of this UnitType
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Retrieve the SI unit of this UnitType.
     * @return String the SI unit of this UnitType
     */
    public final String getSiUnit()
    {
        return this.siUnit;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.code;
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.djunitsType == null) ? 0 : this.djunitsType.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.siUnit == null) ? 0 : this.siUnit.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({ "checkstyle:designforextension", "needbraces" })
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SerializationUnits other = (SerializationUnits) obj;
        if (this.code != other.code)
            return false;
        if (this.description == null)
        {
            if (other.description != null)
                return false;
        }
        else if (!this.description.equals(other.description))
            return false;
        if (this.djunitsType == null)
        {
            if (other.djunitsType != null)
                return false;
        }
        else if (!this.djunitsType.equals(other.djunitsType))
            return false;
        if (this.name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.siUnit == null)
        {
            if (other.siUnit != null)
                return false;
        }
        else if (!this.siUnit.equals(other.siUnit))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "UnitType [code=" + this.code + ", name=" + this.name + ", description=" + this.description + ", siUnit="
                + this.siUnit + "]";
    }

}
