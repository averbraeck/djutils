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
 * BSD-style license. See <a href="http://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck $,
 * initial version Mar 4, 2017 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class UnitType implements Serializable
{
    /** */
    private static final long serialVersionUID = 20170304L;

    /** the unit types from number to type. */
    private static Map<Byte, UnitType> byteTypeMap = new HashMap<>();

    /** the unit types from class to type. */
    private static Map<Class<? extends Unit<?>>, UnitType> unitTypeMap = new HashMap<>();

    /** Dimensionless unit type with code 0. */
    public static final UnitType DIMENSIONLESS =
            new UnitType(0, DimensionlessUnit.class, "Dimensionless", "Unit without a dimension", "[]");

    /** Acceleration unit type with code 1. */
    public static final UnitType ACCELERATION =
            new UnitType(1, AccelerationUnit.class, "Acceleration", "Acceleration", "[m/s^2]");

    /** AngleSolid unit type with code 2. */
    public static final UnitType ANGLESOLID =
            new UnitType(2, AngleSolidUnit.class, "AngleSolid", "Solid angle ", "[steradian]");

    /** Angle unit type with code 3. */
    public static final UnitType ANGLE = new UnitType(3, AngleUnit.class, "Angle", "Angle (relative)", "[rad]");

    /** Direction unit type with code 4. */
    public static final UnitType DIRECTION =
            new UnitType(4, DirectionUnit.class, "Direction", "Angle  (absolute)", "[rad]");

    /** Area unit type with code 5. */
    public static final UnitType AREA = new UnitType(5, AreaUnit.class, "Area", "Area", "[m^2]");

    /** Density unit type with code 6. */
    public static final UnitType DENSITY =
            new UnitType(6, DensityUnit.class, "Density", "Density based on mass and length", "[kg/m^3]");

    /** ElectricalCharge unit type with code 7. */
    public static final UnitType ELECTRICALCHARGE =
            new UnitType(7, ElectricalChargeUnit.class, "ElectricalCharge", "Electrical charge (Coulomb)", "[sA]");

    /** ElectricalCurrent unit type with code 8. */
    public static final UnitType ELECTRICALCURRENT =
            new UnitType(8, ElectricalCurrentUnit.class, "ElectricalCurrent", "Electrical current (Ampere)", "[A]");

    /** ElectricalPotential unit type with code 9. */
    public static final UnitType ELECTRICALPOTENTIAL = new UnitType(9, ElectricalPotentialUnit.class,
            "ElectricalPotential", "Electrical potential (Volt)", "[kgm^2/s^3A]");

    /** ElectricalResistance unit type with code 10. */
    public static final UnitType ELECTRICALRESISTANCE = new UnitType(10, ElectricalResistanceUnit.class,
            "ElectricalResistance", "Electrical resistance (Ohm)", "[kgm^2/s^3A^2]");

    /** Energy unit type with code 11. */
    public static final UnitType ENERGY =
            new UnitType(11, EnergyUnit.class, "Energy", "Energy (Joule)", "[kgm^2/s^2]");

    /** FlowMass unit type with code 12. */
    public static final UnitType FLOWMASS =
            new UnitType(12, FlowMassUnit.class, "FlowMass", "Mass flow rate ", "[kg/s]");

    /** FlowVolume unit type with code 13. */
    public static final UnitType FLOWVOLUME =
            new UnitType(13, FlowVolumeUnit.class, "FlowVolume", "Volume flow rate", "[m^3/s]");

    /** Force unit type with code 14. */
    public static final UnitType FORCE = new UnitType(14, ForceUnit.class, "Force", "Force (Newton)", "[kgm/s^2]");

    /** Frequency unit type with code 15. */
    public static final UnitType FREQUENCY =
            new UnitType(15, FrequencyUnit.class, "Frequency", "Frequency (Hz)", "[1/s]");

    /** Length unit type with code 16. */
    public static final UnitType LENGTH = new UnitType(16, LengthUnit.class, "Length", "Length (relative)", "[m]");

    /** Position unit type with code 17. */
    public static final UnitType POSITION =
            new UnitType(17, PositionUnit.class, "Position", "Length (absolute)", "[m]");

    /** LinearDensity unit type with code 18. */
    public static final UnitType LINEARDENSITY =
            new UnitType(18, LinearDensityUnit.class, "LinearDensity", "Linear density ", "[1/m]");

    /** Mass unit type with code 19. */
    public static final UnitType MASS = new UnitType(19, MassUnit.class, "Mass", "Mass", "[kg]");

    /** Power unit type with code 20. */
    public static final UnitType POWER = new UnitType(20, PowerUnit.class, "Power", "Power (Watt)", "[kgm^2/s^3]");

    /** Pressure unit type with code 21. */
    public static final UnitType PRESSURE =
            new UnitType(21, PressureUnit.class, "Pressure", "Pressure (Pascal)", "[kg/ms^2]");

    /** Speed unit type with code 22. */
    public static final UnitType SPEED = new UnitType(22, SpeedUnit.class, "Speed", "Speed", "[m/s]");

    /** Temperature unit type with code 23. */
    public static final UnitType TEMPERATURE =
            new UnitType(23, TemperatureUnit.class, "Temperature", "Temperature (relative)", "[K]");

    /** AbsoluteTemperature unit type with code 24. */
    public static final UnitType ABSOLUTETEMPERATURE =
            new UnitType(24, AbsoluteTemperatureUnit.class, "AbsoluteTemperature", "Temperature (absolute)", "[K]");

    /** Duration unit type with code 25. */
    public static final UnitType DURATION = new UnitType(25, DurationUnit.class, "Duration", "Time (relative)", "[s]");

    /** Time unit type with code 26. */
    public static final UnitType TIME = new UnitType(26, TimeUnit.class, "Time", "Time (absolute)", "[s]");

    /** Torque unit type with code 27. */
    public static final UnitType TORQUE =
            new UnitType(27, TorqueUnit.class, "Torque", "Torque (Newton-meter)", "[kgm^2/s^2]");

    /** Volume unit type with code 28. */
    public static final UnitType VOLUME = new UnitType(28, VolumeUnit.class, "Volume", "Volume", "[m^3]");

    /** Money unit type with code 100. */
    public static final UnitType MONEY =
            new UnitType(100, MoneyUnit.class, "Money", "Money (cost in e.g., $, €, ...)", "[$]");

    /** MoneyPerArea unit type with code 101. */
    public static final UnitType MONEYPERAREA =
            new UnitType(101, MoneyPerAreaUnit.class, "MoneyPerArea", "Money/Area (cost/m^2)", "[$/m^2]");

    /** MoneyPerEnergy unit type with code 102. */
    public static final UnitType MONEYPERENERGY =
            new UnitType(102, MoneyPerEnergyUnit.class, "MoneyPerEnergy", "Money/Energy (cost/W)", "[$s^3/kgm^2]");

    /** MoneyPerLength unit type with code 103. */
    public static final UnitType MONEYPERLENGTH =
            new UnitType(103, MoneyPerLengthUnit.class, "MoneyPerLength", "Money/Length (cost/m)", "[$/m]");

    /** MoneyPerMass unit type with code 104. */
    public static final UnitType MONEYPERMASS =
            new UnitType(104, MoneyPerMassUnit.class, "MoneyPerMass", "Money/Mass (cost/kg)", "[$/kg]");

    /** MoneyPerDuration unit type with code 105. */
    public static final UnitType MONEYPERDURATION =
            new UnitType(105, MoneyPerDurationUnit.class, "MoneyPerDuration", "Money/Duration (cost/s)", "[$/s]");

    /** MoneyPerVolume unit type with code 106. */
    public static final UnitType MONEYPERVOLUME =
            new UnitType(106, MoneyPerVolumeUnit.class, "MoneyPerVolume", "Money/Volume (cost/m^3)", "[$/m^3]");

    /** the code of the unit as a byte. */
    private final byte code;

    /** the djunits data type. */
    private final Class<? extends Unit<?>> djunitsType;

    /** the unit name. */
    private final String name;

    /** the unit description. */
    private final String description;

    /** the SI or default unit in SI-elements. */
    private final String siUnit;

    /**
     * @param code the byte code of the unit provided as an int
     * @param djunitsType the djunits data type
     * @param name the unit name
     * @param description the unit description
     * @param siUnit the SI or default unit in SI-elements
     * @param <U> the Unit
     */
    public <U extends Unit<U>> UnitType(final int code, final Class<U> djunitsType, final String name,
            final String description, final String siUnit)
    {
        super();
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
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static UnitType getUnitType(final byte code)
    {
        return byteTypeMap.get(code);
    }

    /**
     * Return the unit class belonging to the byte code.
     * @param code the code to search for.
     * @return the unit class, or null if not found.
     */
    public static Class<? extends Unit<?>> getUnitClass(final byte code)
    {
        UnitType type = byteTypeMap.get(code);
        return type == null ? null : type.getDjunitsType();
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the Unit
     */
    public static <U extends Unit<U>> UnitType getUnitType(final U unit)
    {
        return unitTypeMap.get(unit.getClass());
    }

    /**
     * Return the byte code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the Unit
     */
    public static <U extends Unit<U>> byte getUnitCode(final U unit)
    {
        UnitType type = unitTypeMap.get(unit.getClass());
        return type == null ? null : type.getCode();
    }

    /**
     * @return code
     */
    public final byte getCode()
    {
        return this.code;
    }

    /**
     * @return djunitsType
     */
    public final Class<? extends Unit<?>> getDjunitsType()
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
     * @return description
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * @return siUnit
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
        UnitType other = (UnitType) obj;
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
