package org.djutils.serialization;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.djunits.unit.AbsoluteTemperatureUnit;
import org.djunits.unit.AbsorbedDoseUnit;
import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.AmountOfSubstanceUnit;
import org.djunits.unit.AngleUnit;
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

/**
 * The Sim0MQ unit types with their code, including static methods to quickly find a unit type.
 * <p>
 * Copyright (c) 2016-2017 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
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

    /** SolidAngle unit type with code 2. */
    public static final SerializationUnits SOLIDANGLE =
            new SerializationUnits(2, SolidAngleUnit.class, "SolidAngle", "Solid angle (steradian)", "[sr]");

    /** Angle unit type with code 3. */
    public static final SerializationUnits ANGLE =
            new SerializationUnits(3, AngleUnit.class, "Angle", "Angle (relative)", "[rad]");

    /** Direction unit type with code 4. */
    public static final SerializationUnits DIRECTION =
            new SerializationUnits(4, DirectionUnit.class, "Direction", "Angle (absolute)", "[rad]");

    /** Area unit type with code 5. */
    public static final SerializationUnits AREA = new SerializationUnits(5, AreaUnit.class, "Area", "Area (m2)", "[m^2]");

    /** Density unit type with code 6. */
    public static final SerializationUnits DENSITY =
            new SerializationUnits(6, DensityUnit.class, "Density", "Density based on mass and length", "[kg/m^3]");

    /** ElectricalCharge unit type with code 7. */
    public static final SerializationUnits ELECTRICALCHARGE =
            new SerializationUnits(7, ElectricalChargeUnit.class, "ElectricalCharge", "Electrical charge (Coulomb)", "[s.A]");

    /** ElectricalCurrent unit type with code 8. */
    public static final SerializationUnits ELECTRICALCURRENT =
            new SerializationUnits(8, ElectricalCurrentUnit.class, "ElectricalCurrent", "Electrical current (Ampere)", "[A]");

    /** ElectricalPotential unit type with code 9. */
    public static final SerializationUnits ELECTRICALPOTENTIAL = new SerializationUnits(9, ElectricalPotentialUnit.class,
            "ElectricalPotential", "Electrical potential (Volt)", "[kg.m^2/s^3.A]");

    /** ElectricalResistance unit type with code 10. */
    public static final SerializationUnits ELECTRICALRESISTANCE = new SerializationUnits(10, ElectricalResistanceUnit.class,
            "ElectricalResistance", "Electrical resistance (Ohm)", "[kg.m^2/s^3.A^2]");

    /** Energy unit type with code 11. */
    public static final SerializationUnits ENERGY =
            new SerializationUnits(11, EnergyUnit.class, "Energy", "Energy (Joule)", "[kg.m^2/s^2]");

    /** FlowMass unit type with code 12. */
    public static final SerializationUnits FLOWMASS =
            new SerializationUnits(12, FlowMassUnit.class, "FlowMass", "Mass flow rate ", "[kg/s]");

    /** FlowVolume unit type with code 13. */
    public static final SerializationUnits FLOWVOLUME =
            new SerializationUnits(13, FlowVolumeUnit.class, "FlowVolume", "Volume flow rate", "[m^3/s]");

    /** Force unit type with code 14. */
    public static final SerializationUnits FORCE =
            new SerializationUnits(14, ForceUnit.class, "Force", "Force (Newton)", "[kg.m/s^2]");

    /** Frequency unit type with code 15. */
    public static final SerializationUnits FREQUENCY =
            new SerializationUnits(15, FrequencyUnit.class, "Frequency", "Frequency (Hz)", "[1/s]");

    /** Length unit type with code 16. */
    public static final SerializationUnits LENGTH =
            new SerializationUnits(16, LengthUnit.class, "Length", "Length (relative)", "[m]");

    /** Position unit type with code 17. */
    public static final SerializationUnits POSITION =
            new SerializationUnits(17, PositionUnit.class, "Position", "Length (absolute)", "[m]");

    /** LinearDensity unit type with code 18. */
    public static final SerializationUnits LINEARDENSITY =
            new SerializationUnits(18, LinearDensityUnit.class, "LinearDensity", "Linear density ", "[1/m]");

    /** Mass unit type with code 19. */
    public static final SerializationUnits MASS = new SerializationUnits(19, MassUnit.class, "Mass", "Mass", "[kg]");

    /** Power unit type with code 20. */
    public static final SerializationUnits POWER =
            new SerializationUnits(20, PowerUnit.class, "Power", "Power (Watt)", "[kg.m^2/s^3]");

    /** Pressure unit type with code 21. */
    public static final SerializationUnits PRESSURE =
            new SerializationUnits(21, PressureUnit.class, "Pressure", "Pressure (Pascal)", "[kg/m.s^2]");

    /** Speed unit type with code 22. */
    public static final SerializationUnits SPEED = new SerializationUnits(22, SpeedUnit.class, "Speed", "Speed", "[m/s]");

    /** Temperature unit type with code 23. */
    public static final SerializationUnits TEMPERATURE =
            new SerializationUnits(23, TemperatureUnit.class, "Temperature", "Temperature (relative)", "[K]");

    /** AbsoluteTemperature unit type with code 24. */
    public static final SerializationUnits ABSOLUTETEMPERATURE =
            new SerializationUnits(24, AbsoluteTemperatureUnit.class, "AbsoluteTemperature", "Temperature (absolute)", "[K]");

    /** Duration unit type with code 25. */
    public static final SerializationUnits DURATION =
            new SerializationUnits(25, DurationUnit.class, "Duration", "Time (relative)", "[s]");

    /** Time unit type with code 26. */
    public static final SerializationUnits TIME = new SerializationUnits(26, TimeUnit.class, "Time", "Time (absolute)", "[s]");

    /** Torque unit type with code 27. */
    public static final SerializationUnits TORQUE =
            new SerializationUnits(27, TorqueUnit.class, "Torque", "Torque (Newton-meter)", "[kg.m^2/s^2]");

    /** Volume unit type with code 28. */
    public static final SerializationUnits VOLUME = new SerializationUnits(28, VolumeUnit.class, "Volume", "Volume", "[m^3]");

    /** AbsorbedDose unit type with code 29. */
    public static final SerializationUnits ABSORBEDDOSE =
            new SerializationUnits(28, AbsorbedDoseUnit.class, "Absorbed dose", "Absorbed Dose (Gray)", "[m^2/s^2]");

    /** AmountOfSubstance unit type with code 30. */
    public static final SerializationUnits AMOUNTOFSUBSTANCE = new SerializationUnits(30, AmountOfSubstanceUnit.class,
            "Amount of substance", "Amount of substance (mole)", "[mol]");

    /** CatalyticActivity unit type with code 31. */
    public static final SerializationUnits CATALYTICACTIVITY = new SerializationUnits(31, CatalyticActivityUnit.class,
            "Catalytic activity", "Catalytic activity (katal)", "[mol/s]");

    /** ElectricalCapacitance unit type with code 32. */
    public static final SerializationUnits ELECTRICALCAPACITANCE = new SerializationUnits(32, ElectricalCapacitanceUnit.class,
            "Electrical capacitance", "Electrical capacitance (Farad)", "[s^4.A^2/kg.m^2]");

    /** ElectricalConductance unit type with code 33. */
    public static final SerializationUnits ELECTRICALCONDUCTANCE = new SerializationUnits(33, ElectricalConductanceUnit.class,
            "Electrical conductance", "Electrical conductance (Siemens)", "[s^3.A^2/kg.m^2]");

    /** ElectricalInductance unit type with code 34. */
    public static final SerializationUnits ELECTRICALINDUCTANCE = new SerializationUnits(34, ElectricalInductanceUnit.class,
            "Electrical inductance", "Electrical inductance (Henry)", "[kg.m^2/s^2.A^2]");

    /** EquivalentDose unit type with code 35. */
    public static final SerializationUnits EQUIVALENTDOSE =
            new SerializationUnits(35, EquivalentDoseUnit.class, "Equivalent dose", "Equivalent dose (Sievert)", "[m^2/s^2]");

    /** Illuminance unit type with code 36. */
    public static final SerializationUnits ILLUMINANCE =
            new SerializationUnits(36, IlluminanceUnit.class, "Illuminance", "Illuminance (lux)", "[sr.cd/m^2]");

    /** LuminousFlux unit type with code 37. */
    public static final SerializationUnits LUMINOUSFLUX =
            new SerializationUnits(37, LuminousFluxUnit.class, "Luminous flux", "Luminous flux (lumen)", "[sr.cd]");

    /** LuminousIntensity unit type with code 38. */
    public static final SerializationUnits LUMINOUSINTENSITY = new SerializationUnits(38, LuminousIntensityUnit.class,
            "Luminous intensity", "Luminous intensity (candela)", "[cd]");

    /** MagneticFluxDensity unit type with code 39. */
    public static final SerializationUnits MAGNETICFLUXDENSITY = new SerializationUnits(39, MagneticFluxDensityUnit.class,
            "Magnetic flux density", "Magnetic flux density (Tesla)", "[kg/s^2.A]");

    /** MagneticFlux unit type with code 40. */
    public static final SerializationUnits MAGNETICFLUX =
            new SerializationUnits(40, MagneticFluxUnit.class, "Magnetic flux", "Magnetic flux (Weber)", "[kg.m^2/s^2.A]");

    /** RadioActivity unit type with code 41. */
    public static final SerializationUnits RADIOACTIVITY =
            new SerializationUnits(41, RadioActivityUnit.class, "Radioactivity", "Radioactivity (Becquerel)", "[1/s]");

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
    @SuppressWarnings({"checkstyle:designforextension", "needbraces"})
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
