package org.djutils.serialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.djunits.quantity.AbsorbedDose;
import org.djunits.quantity.Acceleration;
import org.djunits.quantity.AmountOfSubstance;
import org.djunits.quantity.Angle;
import org.djunits.quantity.AngularAcceleration;
import org.djunits.quantity.AngularVelocity;
import org.djunits.quantity.Area;
import org.djunits.quantity.CatalyticActivity;
import org.djunits.quantity.Density;
import org.djunits.quantity.Dimensionless;
import org.djunits.quantity.Direction;
import org.djunits.quantity.Duration;
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
import org.djunits.quantity.Position;
import org.djunits.quantity.Power;
import org.djunits.quantity.Pressure;
import org.djunits.quantity.RadioActivity;
import org.djunits.quantity.SolidAngle;
import org.djunits.quantity.Speed;
import org.djunits.quantity.Temperature;
import org.djunits.quantity.Time;
import org.djunits.quantity.Torque;
import org.djunits.quantity.Volume;
import org.djunits.unit.Unit;

/**
 * The unit types with their code, including static methods to quickly find a unit type.
 * <p>
 * Copyright (c) 2016-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class QuantityType
{
    /** the unit types from number to type. */
    private static Map<Byte, QuantityType> byteTypeMap = new HashMap<>();

    /** the unit types from class to type. */
    private static Map<Class<? extends Unit<?, ?>>, QuantityType> unitTypeMap = new HashMap<>();

    /** Dimensionless unit type with code 0. */
    public static final QuantityType DIMENSIONLESS =
            new QuantityType(0, Dimensionless.Unit.class, "Dimensionless", "Unit without a dimension", "[]");

    /** Acceleration unit type with code 1. */
    public static final QuantityType ACCELERATION =
            new QuantityType(1, Acceleration.Unit.class, "Acceleration", "Acceleration", "[m/s^2]");

    /** SolidAngle unit type with code 2. */
    public static final QuantityType SOLIDANGLE =
            new QuantityType(2, SolidAngle.Unit.class, "SolidAngle", "Solid angle (steradian)", "[sr]");

    /** Angle unit type with code 3. */
    public static final QuantityType ANGLE = new QuantityType(3, Angle.Unit.class, "Angle", "Angle (relative)", "[rad]");

    /** Direction unit type with code 4. */
    public static final QuantityType DIRECTION =
            new QuantityType(4, Direction.Unit.class, "Direction", "Angle (absolute)", "[rad]");

    /** Area unit type with code 5. */
    public static final QuantityType AREA = new QuantityType(5, Area.Unit.class, "Area", "Area (m2)", "[m^2]");

    /** Density unit type with code 6. */
    public static final QuantityType DENSITY =
            new QuantityType(6, Density.Unit.class, "Density", "Density based on mass and length", "[kg/m^3]");

    /** ElectricalCharge unit type with code 7. */
    public static final QuantityType ELECTRICALCHARGE =
            new QuantityType(7, ElectricalCharge.Unit.class, "ElectricalCharge", "Electrical charge (Coulomb)", "[s.A]");

    /** ElectricalCurrent unit type with code 8. */
    public static final QuantityType ELECTRICALCURRENT =
            new QuantityType(8, ElectricalCurrent.Unit.class, "ElectricalCurrent", "Electrical current (Ampere)", "[A]");

    /** ElectricalPotential unit type with code 9. */
    public static final QuantityType ELECTRICALPOTENTIAL = new QuantityType(9, ElectricalPotential.Unit.class,
            "ElectricalPotential", "Electrical potential (Volt)", "[kg.m^2/s^3.A]");

    /** ElectricalResistance unit type with code 10. */
    public static final QuantityType ELECTRICALRESISTANCE = new QuantityType(10, ElectricalResistance.Unit.class,
            "ElectricalResistance", "Electrical resistance (Ohm)", "[kg.m^2/s^3.A^2]");

    /** Energy unit type with code 11. */
    public static final QuantityType ENERGY =
            new QuantityType(11, Energy.Unit.class, "Energy", "Energy (Joule)", "[kg.m^2/s^2]");

    /** FlowMass unit type with code 12. */
    public static final QuantityType FLOWMASS =
            new QuantityType(12, FlowMass.Unit.class, "FlowMass", "Mass flow rate ", "[kg/s]");

    /** FlowVolume unit type with code 13. */
    public static final QuantityType FLOWVOLUME =
            new QuantityType(13, FlowVolume.Unit.class, "FlowVolume", "Volume flow rate", "[m^3/s]");

    /** Force unit type with code 14. */
    public static final QuantityType FORCE = new QuantityType(14, Force.Unit.class, "Force", "Force (Newton)", "[kg.m/s^2]");

    /** Frequency unit type with code 15. */
    public static final QuantityType FREQUENCY =
            new QuantityType(15, Frequency.Unit.class, "Frequency", "Frequency (Hz)", "[1/s]");

    /** Length unit type with code 16. */
    public static final QuantityType LENGTH = new QuantityType(16, Length.Unit.class, "Length", "Length (relative)", "[m]");

    /** Position unit type with code 17. */
    public static final QuantityType POSITION =
            new QuantityType(17, Position.Unit.class, "Position", "Length (absolute)", "[m]");

    /** LinearDensity unit type with code 18. */
    public static final QuantityType LINEARDENSITY =
            new QuantityType(18, LinearDensity.Unit.class, "LinearDensity", "Linear density ", "[1/m]");

    /** Mass unit type with code 19. */
    public static final QuantityType MASS = new QuantityType(19, Mass.Unit.class, "Mass", "Mass", "[kg]");

    /** Power unit type with code 20. */
    public static final QuantityType POWER = new QuantityType(20, Power.Unit.class, "Power", "Power (Watt)", "[kg.m^2/s^3]");

    /** Pressure unit type with code 21. */
    public static final QuantityType PRESSURE =
            new QuantityType(21, Pressure.Unit.class, "Pressure", "Pressure (Pascal)", "[kg/m.s^2]");

    /** Speed unit type with code 22. */
    public static final QuantityType SPEED = new QuantityType(22, Speed.Unit.class, "Speed", "Speed", "[m/s]");

    /** Temperature unit type with code 23. */
    public static final QuantityType TEMPERATURE =
            new QuantityType(23, Temperature.Unit.class, "Temperature", "Temperature (relative)", "[K]");

    /** AbsoluteTemperature unit type with code 24. */
    public static final QuantityType ABSOLUTETEMPERATURE =
            new QuantityType(24, AbsoluteTemperature.Unit.class, "AbsoluteTemperature", "Temperature (absolute)", "[K]");

    /** Duration unit type with code 25. */
    public static final QuantityType DURATION = new QuantityType(25, Duration.Unit.class, "Duration", "Time (relative)", "[s]");

    /** Time unit type with code 26. */
    public static final QuantityType TIME = new QuantityType(26, Time.Unit.class, "Time", "Time (absolute)", "[s]");

    /** Torque unit type with code 27. */
    public static final QuantityType TORQUE =
            new QuantityType(27, Torque.Unit.class, "Torque", "Torque (Newton-meter)", "[kg.m^2/s^2]");

    /** Volume unit type with code 28. */
    public static final QuantityType VOLUME = new QuantityType(28, Volume.Unit.class, "Volume", "Volume", "[m^3]");

    /** AbsorbedDose unit type with code 29. */
    public static final QuantityType ABSORBEDDOSE =
            new QuantityType(28, AbsorbedDose.Unit.class, "Absorbed dose", "Absorbed Dose (Gray)", "[m^2/s^2]");

    /** AmountOfSubstance unit type with code 30. */
    public static final QuantityType AMOUNTOFSUBSTANCE =
            new QuantityType(30, AmountOfSubstance.Unit.class, "Amount of substance", "Amount of substance (mole)", "[mol]");

    /** CatalyticActivity unit type with code 31. */
    public static final QuantityType CATALYTICACTIVITY =
            new QuantityType(31, CatalyticActivity.Unit.class, "Catalytic activity", "Catalytic activity (katal)", "[mol/s]");

    /** ElectricalCapacitance unit type with code 32. */
    public static final QuantityType ELECTRICALCAPACITANCE = new QuantityType(32, ElectricalCapacitance.Unit.class,
            "Electrical capacitance", "Electrical capacitance (Farad)", "[s^4.A^2/kg.m^2]");

    /** ElectricalConductance unit type with code 33. */
    public static final QuantityType ELECTRICALCONDUCTANCE = new QuantityType(33, ElectricalConductance.Unit.class,
            "Electrical conductance", "Electrical conductance (Siemens)", "[s^3.A^2/kg.m^2]");

    /** ElectricalInductance unit type with code 34. */
    public static final QuantityType ELECTRICALINDUCTANCE = new QuantityType(34, ElectricalInductance.Unit.class,
            "Electrical inductance", "Electrical inductance (Henry)", "[kg.m^2/s^2.A^2]");

    /** EquivalentDose unit type with code 35. */
    public static final QuantityType EQUIVALENTDOSE =
            new QuantityType(35, EquivalentDose.Unit.class, "Equivalent dose", "Equivalent dose (Sievert)", "[m^2/s^2]");

    /** Illuminance unit type with code 36. */
    public static final QuantityType ILLUMINANCE =
            new QuantityType(36, Illuminance.Unit.class, "Illuminance", "Illuminance (lux)", "[sr.cd/m^2]");

    /** LuminousFlux unit type with code 37. */
    public static final QuantityType LUMINOUSFLUX =
            new QuantityType(37, LuminousFlux.Unit.class, "Luminous flux", "Luminous flux (lumen)", "[sr.cd]");

    /** LuminousIntensity unit type with code 38. */
    public static final QuantityType LUMINOUSINTENSITY =
            new QuantityType(38, LuminousIntensity.Unit.class, "Luminous intensity", "Luminous intensity (candela)", "[cd]");

    /** MagneticFluxDensity unit type with code 39. */
    public static final QuantityType MAGNETICFLUXDENSITY = new QuantityType(39, MagneticFluxDensity.Unit.class,
            "Magnetic flux density", "Magnetic flux density (Tesla)", "[kg/s^2.A]");

    /** MagneticFlux unit type with code 40. */
    public static final QuantityType MAGNETICFLUX =
            new QuantityType(40, MagneticFlux.Unit.class, "Magnetic flux", "Magnetic flux (Weber)", "[kg.m^2/s^2.A]");

    /** RadioActivity unit type with code 41. */
    public static final QuantityType RADIOACTIVITY =
            new QuantityType(41, RadioActivity.Unit.class, "Radioactivity", "Radioactivity (Becquerel)", "[1/s]");

    /** AngularAcceleration unit type with code 42. */
    public static final QuantityType ANGULARACCELERATION =
            new QuantityType(42, AngularAcceleration.Unit.class, "AngularAcceleration", "AngularAcceleration", "[rad/s^2]");

    /** AngularVelocity unit type with code 43. */
    public static final QuantityType ANGULARVELOCITY =
            new QuantityType(43, AngularVelocity.Unit.class, "AngularVelocity", "AngularVelocity", "[rad/s]");

    /** AngularAcceleration unit type with code 44. */
    public static final QuantityType MOMENTUM = new QuantityType(44, Momentum.Unit.class, "Momentum", "Momentum", "[rad/s^2]");

    /** The code of the unit as a byte. */
    private final byte code;

    /** The djunits data type. */
    private final Class<? extends Unit<?, ?>> djunitsType;

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
    public static Class<? extends Unit<?, ?>> getUnitClass(final byte code)
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
    public final Class<? extends Unit<?, ?>> getDjunitsType()
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
