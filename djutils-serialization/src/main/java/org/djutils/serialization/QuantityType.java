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
import org.djunits.quantity.ArealObjectDensity;
import org.djunits.quantity.CatalyticActivity;
import org.djunits.quantity.Density;
import org.djunits.quantity.Dimensionless;
import org.djunits.quantity.Direction;
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
import org.djunits.quantity.LinearObjectDensity;
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
import org.djunits.quantity.TemperatureDifference;
import org.djunits.quantity.Time;
import org.djunits.quantity.Torque;
import org.djunits.quantity.Volume;
import org.djunits.quantity.VolumetricObjectDensity;
import org.djunits.quantity.def.Quantity;

/**
 * The quantity types with their code, including static methods to quickly find a quantity type.
 * <p>
 * Copyright (c) 2016-2026 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://sim0mq.org/docs/current/license.html">Sim0MQ License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class QuantityType
{
    /** the quantity types from number to type. */
    private static Map<Byte, QuantityType> byteTypeMap = new HashMap<>();

    /** the quantity types from class to type. */
    private static Map<Class<? extends Quantity<?>>, QuantityType> quantityTypeMap = new HashMap<>();

    /** {@link Dimensionless} quantity type with code 0. */
    public static final QuantityType DIMENSIONLESS =
            new QuantityType(0, Dimensionless.class, "Dimensionless", "Quantity without a dimension", "[]");

    /** {@link Acceleration} quantity type with code 1. */
    public static final QuantityType ACCELERATION =
            new QuantityType(1, Acceleration.class, "Acceleration", "Acceleration", "[m/s^2]");

    /** {@link SolidAngle} quantity type with code 2. */
    public static final QuantityType SOLIDANGLE =
            new QuantityType(2, SolidAngle.class, "SolidAngle", "Solid angle (steradian)", "[sr]");

    /** {@link Angle} quantity type with code 3. */
    public static final QuantityType ANGLE = new QuantityType(3, Angle.class, "Angle", "Angle (relative)", "[rad]");

    /** {@link Direction} quantity type with code 4. */
    public static final QuantityType DIRECTION = new QuantityType(4, Angle.class, "Direction", "Angle (absolute)", "[rad]");

    /** {@link Area} quantity type with code 5. */
    public static final QuantityType AREA = new QuantityType(5, Area.class, "Area", "Area (m2)", "[m^2]");

    /** {@link Density} quantity type with code 6. */
    public static final QuantityType DENSITY =
            new QuantityType(6, Density.class, "Density", "Density based on mass and length", "[kg/m^3]");

    /** {@link ElectricCharge} quantity type with code 7. */
    public static final QuantityType ELECTRICCHARGE =
            new QuantityType(7, ElectricCharge.class, "ElectricCharge", "Electric charge (Coulomb)", "[s.A]");

    /** {@link ElectricCurrent} quantity type with code 8. */
    public static final QuantityType ELECTRICCURRENT =
            new QuantityType(8, ElectricCurrent.class, "ElectricCurrent", "Electric current (Ampere)", "[A]");

    /** {@link ElectricPotential} quantity type with code 9. */
    public static final QuantityType ELECTRICPOTENTIAL =
            new QuantityType(9, ElectricPotential.class, "ElectricPotential", "Electric potential (Volt)", "[kg.m^2/s^3.A]");

    /** {@link ElectricalResistance} quantity type with code 10. */
    public static final QuantityType ELECTRICALRESISTANCE = new QuantityType(10, ElectricalResistance.class,
            "ElectricalResistance", "Electrical resistance (Ohm)", "[kg.m^2/s^3.A^2]");

    /** {@link Energy} quantity type with code 11. */
    public static final QuantityType ENERGY = new QuantityType(11, Energy.class, "Energy", "Energy (Joule)", "[kg.m^2/s^2]");

    /** {@link FlowMass} quantity type with code 12. */
    public static final QuantityType FLOWMASS = new QuantityType(12, FlowMass.class, "FlowMass", "Mass flow rate ", "[kg/s]");

    /** {@link FlowVolume} quantity type with code 13. */
    public static final QuantityType FLOWVOLUME =
            new QuantityType(13, FlowVolume.class, "FlowVolume", "Volume flow rate", "[m^3/s]");

    /** {@link Force} quantity type with code 14. */
    public static final QuantityType FORCE = new QuantityType(14, Force.class, "Force", "Force (Newton)", "[kg.m/s^2]");

    /** {@link Frequency} quantity type with code 15. */
    public static final QuantityType FREQUENCY = new QuantityType(15, Frequency.class, "Frequency", "Frequency (Hz)", "[1/s]");

    /** {@link Length} quantity type with code 16. */
    public static final QuantityType LENGTH = new QuantityType(16, Length.class, "Length", "Length (relative)", "[m]");

    /** {@link Position} quantity type with code 17. */
    public static final QuantityType POSITION = new QuantityType(17, Length.class, "Position", "Length (absolute)", "[m]");

    /** {@link LinearDensity} quantity type with code 18. */
    public static final QuantityType LINEARDENSITY =
            new QuantityType(18, LinearDensity.class, "LinearDensity", "Linear density ", "[1/m]");

    /** {@link Mass} quantity type with code 19. */
    public static final QuantityType MASS = new QuantityType(19, Mass.class, "Mass", "Mass", "[kg]");

    /** {@link Power} quantity type with code 20. */
    public static final QuantityType POWER = new QuantityType(20, Power.class, "Power", "Power (Watt)", "[kg.m^2/s^3]");

    /** {@link Pressure} quantity type with code 21. */
    public static final QuantityType PRESSURE =
            new QuantityType(21, Pressure.class, "Pressure", "Pressure (Pascal)", "[kg/m.s^2]");

    /** {@link Speed} quantity type with code 22. */
    public static final QuantityType SPEED = new QuantityType(22, Speed.class, "Speed", "Speed", "[m/s]");

    /** {@link TemperatureDifference} quantity type with code 23. */
    public static final QuantityType TEMPERATUREDIFFERENCE = new QuantityType(23, TemperatureDifference.class,
            "TemperatureDifference", "Temperature difference (relative)", "[K]");

    /** {@link Temperature} quantity type with code 24. */
    public static final QuantityType TEMPERATURE =
            new QuantityType(24, TemperatureDifference.class, "Temperature", "Temperature (absolute)", "[K]");

    /** {@link Duration} quantity type with code 25. */
    public static final QuantityType DURATION = new QuantityType(25, Duration.class, "Duration", "Time (relative)", "[s]");

    /** {@link Time} quantity type with code 26. */
    public static final QuantityType TIME = new QuantityType(26, Duration.class, "Time", "Time (absolute)", "[s]");

    /** {@link Torque} quantity type with code 27. */
    public static final QuantityType TORQUE =
            new QuantityType(27, Torque.class, "Torque", "Torque (Newton-meter)", "[kg.m^2/s^2]");

    /** {@link Volume} quantity type with code 28. */
    public static final QuantityType VOLUME = new QuantityType(28, Volume.class, "Volume", "Volume", "[m^3]");

    /** {@link AbsorbedDose} quantity type with code 29. */
    public static final QuantityType ABSORBEDDOSE =
            new QuantityType(28, AbsorbedDose.class, "Absorbed dose", "Absorbed Dose (Gray)", "[m^2/s^2]");

    /** {@link AmountOfSubstance} quantity type with code 30. */
    public static final QuantityType AMOUNTOFSUBSTANCE =
            new QuantityType(30, AmountOfSubstance.class, "Amount of substance", "Amount of substance (mole)", "[mol]");

    /** {@link CatalyticActivity} quantity type with code 31. */
    public static final QuantityType CATALYTICACTIVITY =
            new QuantityType(31, CatalyticActivity.class, "Catalytic activity", "Catalytic activity (katal)", "[mol/s]");

    /** {@link ElectricalCapacitance} quantity type with code 32. */
    public static final QuantityType ELECTRICALCAPACITANCE = new QuantityType(32, ElectricalCapacitance.class,
            "Electrical capacitance", "Electrical capacitance (Farad)", "[s^4.A^2/kg.m^2]");

    /** {@link ElectricalConductance} quantity type with code 33. */
    public static final QuantityType ELECTRICALCONDUCTANCE = new QuantityType(33, ElectricalConductance.class,
            "Electrical conductance", "Electrical conductance (Siemens)", "[s^3.A^2/kg.m^2]");

    /** {@link ElectricalInductance} quantity type with code 34. */
    public static final QuantityType ELECTRICALINDUCTANCE = new QuantityType(34, ElectricalInductance.class,
            "Electrical inductance", "Electrical inductance (Henry)", "[kg.m^2/s^2.A^2]");

    /** {@link EquivalentDose} quantity type with code 35. */
    public static final QuantityType EQUIVALENTDOSE =
            new QuantityType(35, EquivalentDose.class, "Equivalent dose", "Equivalent dose (Sievert)", "[m^2/s^2]");

    /** {@link Illuminance} quantity type with code 36. */
    public static final QuantityType ILLUMINANCE =
            new QuantityType(36, Illuminance.class, "Illuminance", "Illuminance (lux)", "[sr.cd/m^2]");

    /** {@link LuminousFlux} quantity type with code 37. */
    public static final QuantityType LUMINOUSFLUX =
            new QuantityType(37, LuminousFlux.class, "Luminous flux", "Luminous flux (lumen)", "[sr.cd]");

    /** {@link LuminousIntensity} quantity type with code 38. */
    public static final QuantityType LUMINOUSINTENSITY =
            new QuantityType(38, LuminousIntensity.class, "Luminous intensity", "Luminous intensity (candela)", "[cd]");

    /** {@link MagneticFluxDensity} quantity type with code 39. */
    public static final QuantityType MAGNETICFLUXDENSITY = new QuantityType(39, MagneticFluxDensity.class,
            "Magnetic flux density", "Magnetic flux density (Tesla)", "[kg/s^2.A]");

    /** {@link MagneticFlux} quantity type with code 40. */
    public static final QuantityType MAGNETICFLUX =
            new QuantityType(40, MagneticFlux.class, "Magnetic flux", "Magnetic flux (Weber)", "[kg.m^2/s^2.A]");

    /** {@link RadioActivity} quantity type with code 41. */
    public static final QuantityType RADIOACTIVITY =
            new QuantityType(41, RadioActivity.class, "Radioactivity", "Radioactivity (Becquerel)", "[1/s]");

    /** {@link AngularAcceleration} quantity type with code 42. */
    public static final QuantityType ANGULARACCELERATION =
            new QuantityType(42, AngularAcceleration.class, "AngularAcceleration", "Angular acceleration", "[rad/s^2]");

    /** {@link AngularVelocity} quantity type with code 43. */
    public static final QuantityType ANGULARVELOCITY =
            new QuantityType(43, AngularVelocity.class, "AngularVelocity", "Angular velocity", "[rad/s]");

    /** {@link Momentum} quantity type with code 44. */
    public static final QuantityType MOMENTUM = new QuantityType(44, Momentum.class, "Momentum", "Momentum", "[rad/s^2]");

    /** {@link LinearObjectDensity} quantity type with code 45. */
    public static final QuantityType LINEAROBJECTDENSITY =
            new QuantityType(45, LinearObjectDensity.class, "LinearObjectDensity", "Linear object density", "[/m]");

    /** {@link ArealObjectDensity} quantity type with code 46. */
    public static final QuantityType AREALOBJECTDENSITY =
            new QuantityType(46, ArealObjectDensity.class, "ArealObjectDensity", "Areal object density", "[/m^2]");

    /** {@link VolumetricObjectDensity} quantity type with code 47. */
    public static final QuantityType VOLUMETRICOBJECTDENSITY = new QuantityType(47, VolumetricObjectDensity.class,
            "VolumetricObjectDensity", "Volumetric object density", "[/m^3]");

    /** The code of the quantity as a byte. */
    private final byte code;

    /** The quantity class. */
    private final Class<? extends Quantity<?>> quantityClass;

    /** The quantity name. */
    private final String name;

    /** The quantity description. */
    private final String description;

    /** The SI or BASE unit as a String. */
    private final String siUnit;

    /**
     * Construct a new QuantityType and put it in the maps.
     * @param code the byte code of the quantity provided as an int
     * @param quantityClass the quantity class
     * @param name the quantity name
     * @param description the quantity description
     * @param siUnit the SI or BASE unit as a String
     */
    public QuantityType(final int code, final Class<? extends Quantity<?>> quantityClass, final String name,
            final String description, final String siUnit)
    {
        this.code = (byte) code;
        this.quantityClass = quantityClass;
        this.name = name;
        this.description = description;
        this.siUnit = siUnit;

        byteTypeMap.put(this.code, this);
        quantityTypeMap.put(this.quantityClass, this);
    }

    /**
     * Return the quantity type belonging to the byte code.
     * @param code the code to search for.
     * @return the quantity type, or null if not found.
     */
    public static QuantityType getQuantityType(final byte code)
    {
        return byteTypeMap.get(code);
    }

    /**
     * Return the quantity class belonging to the byte code.
     * @param code the code to search for
     * @return the quantity class, or null if not found
     */
    public static Class<? extends Quantity<?>> getQuantityClass(final byte code)
    {
        QuantityType type = byteTypeMap.get(code);
        return type == null ? null : type.getQuantityClass();
    }

    /**
     * Return the quantity type belonging to the quantity class.
     * @param quantity the quantity to search for
     * @return the quantity type, or null if not found
     */
    public static QuantityType getQuantityType(final Quantity<?> quantity)
    {
        return quantityTypeMap.get(quantity.getClass());
    }

    /**
     * Return the byte code belonging to the quantity class.
     * @param quantity the quantity to search for
     * @return the quantity type code
     * @throws IllegalArgumentException when quantity type could not be found
     */
    public static byte getQuantityCode(final Quantity<?> quantity)
    {
        QuantityType type = quantityTypeMap.get(quantity.getClass());
        if (type == null)
        {
            throw new IllegalArgumentException("Could not find quantity type for quantity " + quantity + " in quantityTypeMap");
        }
        return type.getCode();
    }

    /**
     * Return the byte code of this QuantityType.
     * @return the byte code of this QuantityType
     */
    public final byte getCode()
    {
        return this.code;
    }

    /**
     * Return the Quantity class of this QuantityType.
     * @return the Quantity class of this QuantityType
     */
    public final Class<? extends Quantity<?>> getQuantityClass()
    {
        return this.quantityClass;
    }

    /**
     * Return the name of the QuantityType.
     * @return the name of this QuantityType
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Return the description of this QuantityType.
     * @return the description of this QuantityType
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * Return the SI or BASE unit of this QuantityType as a String.
     * @return String the SI or BASE unit of this QuantityType
     */
    public final String getSiUnit()
    {
        return this.siUnit;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.code, this.description, this.quantityClass, this.name, this.siUnit);
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
        QuantityType other = (QuantityType) obj;
        return this.code == other.code && Objects.equals(this.description, other.description)
                && Objects.equals(this.quantityClass, other.quantityClass) && Objects.equals(this.name, other.name)
                && Objects.equals(this.siUnit, other.siUnit);
    }

    @Override
    public String toString()
    {
        return "QuantityType [code=" + this.code + ", name=" + this.name + ", description=" + this.description + ", siUnit="
                + this.siUnit + "]";
    }

}
