package org.djutils.serialization;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

/**
 * The unit types with their code, including static methods to quickly find a unit type.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class QuantityType implements Serializable
{
    /** */
    private static final long serialVersionUID = 20170304L;

    /** the unit types from number to type. */
    private static Map<Byte, QuantityType> byteTypeMap = new HashMap<>();

    /** the unit types from class to type. */
    private static Map<Class<? extends Unit<?>>, QuantityType> unitTypeMap = new HashMap<>();

    /** Dimensionless unit type with code 0. */
    public static final QuantityType DIMENSIONLESS =
            new QuantityType(0, DimensionlessUnit.class, "Dimensionless", "Unit without a dimension", "[]");

    /** Acceleration unit type with code 1. */
    public static final QuantityType ACCELERATION =
            new QuantityType(1, AccelerationUnit.class, "Acceleration", "Acceleration", "[m/s^2]");

    /** SolidAngle unit type with code 2. */
    public static final QuantityType SOLIDANGLE =
            new QuantityType(2, SolidAngleUnit.class, "SolidAngle", "Solid angle (steradian)", "[sr]");

    /** Angle unit type with code 3. */
    public static final QuantityType ANGLE =
            new QuantityType(3, AngleUnit.class, "Angle", "Angle (relative)", "[rad]");

    /** Direction unit type with code 4. */
    public static final QuantityType DIRECTION =
            new QuantityType(4, DirectionUnit.class, "Direction", "Angle (absolute)", "[rad]");

    /** Area unit type with code 5. */
    public static final QuantityType AREA = new QuantityType(5, AreaUnit.class, "Area", "Area (m2)", "[m^2]");

    /** Density unit type with code 6. */
    public static final QuantityType DENSITY =
            new QuantityType(6, DensityUnit.class, "Density", "Density based on mass and length", "[kg/m^3]");

    /** ElectricalCharge unit type with code 7. */
    public static final QuantityType ELECTRICALCHARGE =
            new QuantityType(7, ElectricalChargeUnit.class, "ElectricalCharge", "Electrical charge (Coulomb)", "[s.A]");

    /** ElectricalCurrent unit type with code 8. */
    public static final QuantityType ELECTRICALCURRENT =
            new QuantityType(8, ElectricalCurrentUnit.class, "ElectricalCurrent", "Electrical current (Ampere)", "[A]");

    /** ElectricalPotential unit type with code 9. */
    public static final QuantityType ELECTRICALPOTENTIAL = new QuantityType(9, ElectricalPotentialUnit.class,
            "ElectricalPotential", "Electrical potential (Volt)", "[kg.m^2/s^3.A]");

    /** ElectricalResistance unit type with code 10. */
    public static final QuantityType ELECTRICALRESISTANCE = new QuantityType(10, ElectricalResistanceUnit.class,
            "ElectricalResistance", "Electrical resistance (Ohm)", "[kg.m^2/s^3.A^2]");

    /** Energy unit type with code 11. */
    public static final QuantityType ENERGY =
            new QuantityType(11, EnergyUnit.class, "Energy", "Energy (Joule)", "[kg.m^2/s^2]");

    /** FlowMass unit type with code 12. */
    public static final QuantityType FLOWMASS =
            new QuantityType(12, FlowMassUnit.class, "FlowMass", "Mass flow rate ", "[kg/s]");

    /** FlowVolume unit type with code 13. */
    public static final QuantityType FLOWVOLUME =
            new QuantityType(13, FlowVolumeUnit.class, "FlowVolume", "Volume flow rate", "[m^3/s]");

    /** Force unit type with code 14. */
    public static final QuantityType FORCE =
            new QuantityType(14, ForceUnit.class, "Force", "Force (Newton)", "[kg.m/s^2]");

    /** Frequency unit type with code 15. */
    public static final QuantityType FREQUENCY =
            new QuantityType(15, FrequencyUnit.class, "Frequency", "Frequency (Hz)", "[1/s]");

    /** Length unit type with code 16. */
    public static final QuantityType LENGTH =
            new QuantityType(16, LengthUnit.class, "Length", "Length (relative)", "[m]");

    /** Position unit type with code 17. */
    public static final QuantityType POSITION =
            new QuantityType(17, PositionUnit.class, "Position", "Length (absolute)", "[m]");

    /** LinearDensity unit type with code 18. */
    public static final QuantityType LINEARDENSITY =
            new QuantityType(18, LinearDensityUnit.class, "LinearDensity", "Linear density ", "[1/m]");

    /** Mass unit type with code 19. */
    public static final QuantityType MASS = new QuantityType(19, MassUnit.class, "Mass", "Mass", "[kg]");

    /** Power unit type with code 20. */
    public static final QuantityType POWER =
            new QuantityType(20, PowerUnit.class, "Power", "Power (Watt)", "[kg.m^2/s^3]");

    /** Pressure unit type with code 21. */
    public static final QuantityType PRESSURE =
            new QuantityType(21, PressureUnit.class, "Pressure", "Pressure (Pascal)", "[kg/m.s^2]");

    /** Speed unit type with code 22. */
    public static final QuantityType SPEED = new QuantityType(22, SpeedUnit.class, "Speed", "Speed", "[m/s]");

    /** Temperature unit type with code 23. */
    public static final QuantityType TEMPERATURE =
            new QuantityType(23, TemperatureUnit.class, "Temperature", "Temperature (relative)", "[K]");

    /** AbsoluteTemperature unit type with code 24. */
    public static final QuantityType ABSOLUTETEMPERATURE =
            new QuantityType(24, AbsoluteTemperatureUnit.class, "AbsoluteTemperature", "Temperature (absolute)", "[K]");

    /** Duration unit type with code 25. */
    public static final QuantityType DURATION =
            new QuantityType(25, DurationUnit.class, "Duration", "Time (relative)", "[s]");

    /** Time unit type with code 26. */
    public static final QuantityType TIME = new QuantityType(26, TimeUnit.class, "Time", "Time (absolute)", "[s]");

    /** Torque unit type with code 27. */
    public static final QuantityType TORQUE =
            new QuantityType(27, TorqueUnit.class, "Torque", "Torque (Newton-meter)", "[kg.m^2/s^2]");

    /** Volume unit type with code 28. */
    public static final QuantityType VOLUME = new QuantityType(28, VolumeUnit.class, "Volume", "Volume", "[m^3]");

    /** AbsorbedDose unit type with code 29. */
    public static final QuantityType ABSORBEDDOSE =
            new QuantityType(28, AbsorbedDoseUnit.class, "Absorbed dose", "Absorbed Dose (Gray)", "[m^2/s^2]");

    /** AmountOfSubstance unit type with code 30. */
    public static final QuantityType AMOUNTOFSUBSTANCE = new QuantityType(30, AmountOfSubstanceUnit.class,
            "Amount of substance", "Amount of substance (mole)", "[mol]");

    /** CatalyticActivity unit type with code 31. */
    public static final QuantityType CATALYTICACTIVITY = new QuantityType(31, CatalyticActivityUnit.class,
            "Catalytic activity", "Catalytic activity (katal)", "[mol/s]");

    /** ElectricalCapacitance unit type with code 32. */
    public static final QuantityType ELECTRICALCAPACITANCE = new QuantityType(32, ElectricalCapacitanceUnit.class,
            "Electrical capacitance", "Electrical capacitance (Farad)", "[s^4.A^2/kg.m^2]");

    /** ElectricalConductance unit type with code 33. */
    public static final QuantityType ELECTRICALCONDUCTANCE = new QuantityType(33, ElectricalConductanceUnit.class,
            "Electrical conductance", "Electrical conductance (Siemens)", "[s^3.A^2/kg.m^2]");

    /** ElectricalInductance unit type with code 34. */
    public static final QuantityType ELECTRICALINDUCTANCE = new QuantityType(34, ElectricalInductanceUnit.class,
            "Electrical inductance", "Electrical inductance (Henry)", "[kg.m^2/s^2.A^2]");

    /** EquivalentDose unit type with code 35. */
    public static final QuantityType EQUIVALENTDOSE =
            new QuantityType(35, EquivalentDoseUnit.class, "Equivalent dose", "Equivalent dose (Sievert)", "[m^2/s^2]");

    /** Illuminance unit type with code 36. */
    public static final QuantityType ILLUMINANCE =
            new QuantityType(36, IlluminanceUnit.class, "Illuminance", "Illuminance (lux)", "[sr.cd/m^2]");

    /** LuminousFlux unit type with code 37. */
    public static final QuantityType LUMINOUSFLUX =
            new QuantityType(37, LuminousFluxUnit.class, "Luminous flux", "Luminous flux (lumen)", "[sr.cd]");

    /** LuminousIntensity unit type with code 38. */
    public static final QuantityType LUMINOUSINTENSITY = new QuantityType(38, LuminousIntensityUnit.class,
            "Luminous intensity", "Luminous intensity (candela)", "[cd]");

    /** MagneticFluxDensity unit type with code 39. */
    public static final QuantityType MAGNETICFLUXDENSITY = new QuantityType(39, MagneticFluxDensityUnit.class,
            "Magnetic flux density", "Magnetic flux density (Tesla)", "[kg/s^2.A]");

    /** MagneticFlux unit type with code 40. */
    public static final QuantityType MAGNETICFLUX =
            new QuantityType(40, MagneticFluxUnit.class, "Magnetic flux", "Magnetic flux (Weber)", "[kg.m^2/s^2.A]");

    /** RadioActivity unit type with code 41. */
    public static final QuantityType RADIOACTIVITY =
            new QuantityType(41, RadioActivityUnit.class, "Radioactivity", "Radioactivity (Becquerel)", "[1/s]");

    /** AngularAcceleration unit type with code 42. */
    public static final QuantityType ANGULARACCELERATION = new QuantityType(42, AngularAccelerationUnit.class,
            "AngularAcceleration", "AngularAcceleration", "[rad/s^2]");

    /** AngularVelocity unit type with code 43. */
    public static final QuantityType ANGULARVELOCITY =
            new QuantityType(43, AngularVelocityUnit.class, "AngularVelocity", "AngularVelocity", "[rad/s]");

    /** AngularAcceleration unit type with code 44. */
    public static final QuantityType MOMENTUM =
            new QuantityType(44, MomentumUnit.class, "Momentum", "Momentum", "[rad/s^2]");

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
     * @param code the byte code of the unit provided as an int
     * @param djunitsType the djunits data type
     * @param name the unit name
     * @param description the unit description
     * @param siUnit the SI or default unit in SI-elements
     * @param <U> the Unit
     */
    public <U extends Unit<U>> QuantityType(final int code, final Class<U> djunitsType, final String name,
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
     * @param code the code to search for.
     * @return the unit type, or null if not found.
     */
    public static QuantityType getUnitType(final byte code)
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
        QuantityType type = byteTypeMap.get(code);
        return type == null ? null : type.getDjunitsType();
    }

    /**
     * Return the unit type belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type, or null if not found.
     * @param <U> the Unit
     */
    public static <U extends Unit<U>> QuantityType getUnitType(final U unit)
    {
        return unitTypeMap.get(unit.getClass());
    }

    /**
     * Return the byte code belonging to the unit class.
     * @param unit the unit to search for.
     * @return the unit type code, or null if not found.
     * @param <U> the Unit
     * @throws IllegalArgumentException when unit type could not be found
     */
    public static <U extends Unit<U>> byte getUnitCode(final U unit)
    {
        QuantityType type = unitTypeMap.get(unit.getClass());
        if (type == null)
        {
            throw new IllegalArgumentException("Could not find unit type for unit " + unit + " in unitTypeMap");
        }
        return type.getCode();
    }

    /**
     * Retrieve the byte code of this UnitType.
     * @return the byte code of this UnitType
     */
    public final byte getCode()
    {
        return this.code;
    }

    /**
     * Retrieve the DJUNITS type of this UnitType.
     * @return the DJUNITS type of this UnitType
     */
    public final Class<? extends Unit<?>> getDjunitsType()
    {
        return this.djunitsType;
    }

    /**
     * Retrieve the name of the UnitType.
     * @return the name of this UnitType
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the description of this UnitType.
     * @return the description of this UnitType
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

    @SuppressWarnings("checkstyle:designforextension")
    @Override
    public int hashCode()
    {
        return Objects.hash(this.code, this.description, this.djunitsType, this.name, this.siUnit);
    }

    @SuppressWarnings({"checkstyle:designforextension", "needbraces"})
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QuantityType other = (QuantityType) obj;
        return this.code == other.code && Objects.equals(this.description, other.description)
                && Objects.equals(this.djunitsType, other.djunitsType) && Objects.equals(this.name, other.name)
                && Objects.equals(this.siUnit, other.siUnit);
    }

    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "UnitType [code=" + this.code + ", name=" + this.name + ", description=" + this.description + ", siUnit="
                + this.siUnit + "]";
    }

}
