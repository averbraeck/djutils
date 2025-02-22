package org.djutils.cli;

import org.djunits.value.vdouble.scalar.AbsoluteTemperature;
import org.djunits.value.vdouble.scalar.AbsorbedDose;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.AmountOfSubstance;
import org.djunits.value.vdouble.scalar.Angle;
import org.djunits.value.vdouble.scalar.AngularAcceleration;
import org.djunits.value.vdouble.scalar.AngularVelocity;
import org.djunits.value.vdouble.scalar.Area;
import org.djunits.value.vdouble.scalar.CatalyticActivity;
import org.djunits.value.vdouble.scalar.Density;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.Direction;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.ElectricalCapacitance;
import org.djunits.value.vdouble.scalar.ElectricalCharge;
import org.djunits.value.vdouble.scalar.ElectricalConductance;
import org.djunits.value.vdouble.scalar.ElectricalCurrent;
import org.djunits.value.vdouble.scalar.ElectricalInductance;
import org.djunits.value.vdouble.scalar.ElectricalPotential;
import org.djunits.value.vdouble.scalar.ElectricalResistance;
import org.djunits.value.vdouble.scalar.Energy;
import org.djunits.value.vdouble.scalar.EquivalentDose;
import org.djunits.value.vdouble.scalar.FlowMass;
import org.djunits.value.vdouble.scalar.FlowVolume;
import org.djunits.value.vdouble.scalar.Force;
import org.djunits.value.vdouble.scalar.Frequency;
import org.djunits.value.vdouble.scalar.Illuminance;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.LinearDensity;
import org.djunits.value.vdouble.scalar.LuminousFlux;
import org.djunits.value.vdouble.scalar.LuminousIntensity;
import org.djunits.value.vdouble.scalar.MagneticFlux;
import org.djunits.value.vdouble.scalar.MagneticFluxDensity;
import org.djunits.value.vdouble.scalar.Mass;
import org.djunits.value.vdouble.scalar.Momentum;
import org.djunits.value.vdouble.scalar.Position;
import org.djunits.value.vdouble.scalar.Power;
import org.djunits.value.vdouble.scalar.Pressure;
import org.djunits.value.vdouble.scalar.RadioActivity;
import org.djunits.value.vdouble.scalar.SolidAngle;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Temperature;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vdouble.scalar.Torque;
import org.djunits.value.vdouble.scalar.Volume;

import jakarta.annotation.Generated;
import picocli.CommandLine;
import picocli.CommandLine.ITypeConverter;

/**
 * CliUnitConverters offers conversion methods for DJUNITS scalars so these can be used on the command line, e.g.:
 * 
 * <pre>
 * java -jar ProgramApp.jar --timeout=5min
 * </pre>
 * 
 * <br>
 * Copyright (c) 2018-2025 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public final class CliUnitConverters
{
    /** */
    private CliUnitConverters()
    {
        // static utility class
    }

    /**
     * Register all DJUNITS converters for a CommandLine.
     * @param cmd the CommandLine for which the DJUNITS converters should be registered
     */
    @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
    public static void registerAll(final CommandLine cmd)
    {
        cmd.registerConverter(AbsoluteTemperature.class, new ABSOLUTETEMPERATURE());
        cmd.registerConverter(AbsorbedDose.class, new ABSORBEDDOSE());
        cmd.registerConverter(Acceleration.class, new ACCELERATION());
        cmd.registerConverter(AmountOfSubstance.class, new AMOUNTOFSUBSTANCE());
        cmd.registerConverter(Angle.class, new ANGLE());
        cmd.registerConverter(AngularAcceleration.class, new ANGULARACCELERATION());
        cmd.registerConverter(AngularVelocity.class, new ANGULARVELOCITY());
        cmd.registerConverter(Area.class, new AREA());
        cmd.registerConverter(CatalyticActivity.class, new CATALYTICACTIVITY());
        cmd.registerConverter(Density.class, new DENSITY());
        cmd.registerConverter(Dimensionless.class, new DIMENSIONLESS());
        cmd.registerConverter(Direction.class, new DIRECTION());
        cmd.registerConverter(Duration.class, new DURATION());
        cmd.registerConverter(ElectricalCapacitance.class, new ELECTRICALCAPACITANCE());
        cmd.registerConverter(ElectricalCharge.class, new ELECTRICALCHARGE());
        cmd.registerConverter(ElectricalConductance.class, new ELECTRICALCONDUCTANCE());
        cmd.registerConverter(ElectricalCurrent.class, new ELECTRICALCURRENT());
        cmd.registerConverter(ElectricalInductance.class, new ELECTRICALINDUCTANCE());
        cmd.registerConverter(ElectricalPotential.class, new ELECTRICALPOTENTIAL());
        cmd.registerConverter(ElectricalResistance.class, new ELECTRICALRESISTANCE());
        cmd.registerConverter(Energy.class, new ENERGY());
        cmd.registerConverter(EquivalentDose.class, new EQUIVALENTDOSE());
        cmd.registerConverter(FlowMass.class, new FLOWMASS());
        cmd.registerConverter(FlowVolume.class, new FLOWVOLUME());
        cmd.registerConverter(Force.class, new FORCE());
        cmd.registerConverter(Frequency.class, new FREQUENCY());
        cmd.registerConverter(Illuminance.class, new ILLUMINANCE());
        cmd.registerConverter(Length.class, new LENGTH());
        cmd.registerConverter(LinearDensity.class, new LINEARDENSITY());
        cmd.registerConverter(LuminousFlux.class, new LUMINOUSFLUX());
        cmd.registerConverter(LuminousIntensity.class, new LUMINOUSINTENSITY());
        cmd.registerConverter(MagneticFlux.class, new MAGNETICFLUX());
        cmd.registerConverter(MagneticFluxDensity.class, new MAGNETICFLUXDENSITY());
        cmd.registerConverter(Mass.class, new MASS());
        cmd.registerConverter(Momentum.class, new MOMENTUM());
        cmd.registerConverter(Position.class, new POSITION());
        cmd.registerConverter(Power.class, new POWER());
        cmd.registerConverter(Pressure.class, new PRESSURE());
        cmd.registerConverter(RadioActivity.class, new RADIOACTIVITY());
        cmd.registerConverter(SolidAngle.class, new SOLIDANGLE());
        cmd.registerConverter(Speed.class, new SPEED());
        cmd.registerConverter(Temperature.class, new TEMPERATURE());
        cmd.registerConverter(Time.class, new TIME());
        cmd.registerConverter(Torque.class, new TORQUE());
        cmd.registerConverter(Volume.class, new VOLUME());
    }

    /**
     * Convert an absolute temperature String with unit on the command line to an AbsoluteTemperature scalar.
     */
    public static class ABSOLUTETEMPERATURE implements ITypeConverter<AbsoluteTemperature>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public AbsoluteTemperature convert(final String value) throws Exception
        {
            return AbsoluteTemperature.valueOf(value);
        }
    }

    /**
     * Convert an absorbed dose String with unit on the command line to an AbsorbedDose scalar.
     */
    public static class ABSORBEDDOSE implements ITypeConverter<AbsorbedDose>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public AbsorbedDose convert(final String value) throws Exception
        {
            return AbsorbedDose.valueOf(value);
        }
    }

    /**
     * Convert an acceleration String with unit on the command line to an Acceleration scalar.
     */
    public static class ACCELERATION implements ITypeConverter<Acceleration>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Acceleration convert(final String value) throws Exception
        {
            return Acceleration.valueOf(value);
        }
    }

    /**
     * Convert an amount of substance String with unit on the command line to an AmountOfSubstance scalar.
     */
    public static class AMOUNTOFSUBSTANCE implements ITypeConverter<AmountOfSubstance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public AmountOfSubstance convert(final String value) throws Exception
        {
            return AmountOfSubstance.valueOf(value);
        }
    }

    /**
     * Convert an angle String with unit on the command line to an Angle scalar.
     */
    public static class ANGLE implements ITypeConverter<Angle>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Angle convert(final String value) throws Exception
        {
            return Angle.valueOf(value);
        }
    }

    /**
     * Convert an angular acceleration String with unit on the command line to an AngularAcceleration scalar.
     */
    public static class ANGULARACCELERATION implements ITypeConverter<AngularAcceleration>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public AngularAcceleration convert(final String value) throws Exception
        {
            return AngularAcceleration.valueOf(value);
        }
    }

    /**
     * Convert an angular velocity String with unit on the command line to an AngularVelocity scalar.
     */
    public static class ANGULARVELOCITY implements ITypeConverter<AngularVelocity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public AngularVelocity convert(final String value) throws Exception
        {
            return AngularVelocity.valueOf(value);
        }
    }

    /**
     * Convert an area String with unit on the command line to an Area scalar.
     */
    public static class AREA implements ITypeConverter<Area>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Area convert(final String value) throws Exception
        {
            return Area.valueOf(value);
        }
    }

    /**
     * Convert a catalytic activity String with unit on the command line to a CatalyticActivity scalar.
     */
    public static class CATALYTICACTIVITY implements ITypeConverter<CatalyticActivity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public CatalyticActivity convert(final String value) throws Exception
        {
            return CatalyticActivity.valueOf(value);
        }
    }

    /**
     * Convert a density String with unit on the command line to a Density scalar.
     */
    public static class DENSITY implements ITypeConverter<Density>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Density convert(final String value) throws Exception
        {
            return Density.valueOf(value);
        }
    }

    /**
     * Convert a dimensionless String with unit on the command line to a Dimensionless scalar.
     */
    public static class DIMENSIONLESS implements ITypeConverter<Dimensionless>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Dimensionless convert(final String value) throws Exception
        {
            return Dimensionless.valueOf(value);
        }
    }

    /**
     * Convert a direction String with unit on the command line to a Direction scalar.
     */
    public static class DIRECTION implements ITypeConverter<Direction>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Direction convert(final String value) throws Exception
        {
            return Direction.valueOf(value);
        }
    }

    /**
     * Convert a duration String with unit on the command line to a Duration scalar.
     */
    public static class DURATION implements ITypeConverter<Duration>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Duration convert(final String value) throws Exception
        {
            return Duration.valueOf(value);
        }
    }

    /**
     * Convert an electrical capacitance String with unit on the command line to an ElectricalCapacitance scalar.
     */
    public static class ELECTRICALCAPACITANCE implements ITypeConverter<ElectricalCapacitance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalCapacitance convert(final String value) throws Exception
        {
            return ElectricalCapacitance.valueOf(value);
        }
    }

    /**
     * Convert an electrical charge String with unit on the command line to an ElectricalCharge scalar.
     */
    public static class ELECTRICALCHARGE implements ITypeConverter<ElectricalCharge>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalCharge convert(final String value) throws Exception
        {
            return ElectricalCharge.valueOf(value);
        }
    }

    /**
     * Convert an electrical conductance String with unit on the command line to an ElectricalConductance scalar.
     */
    public static class ELECTRICALCONDUCTANCE implements ITypeConverter<ElectricalConductance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalConductance convert(final String value) throws Exception
        {
            return ElectricalConductance.valueOf(value);
        }
    }

    /**
     * Convert an electrical current String with unit on the command line to an ElectricalCurrent scalar.
     */
    public static class ELECTRICALCURRENT implements ITypeConverter<ElectricalCurrent>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalCurrent convert(final String value) throws Exception
        {
            return ElectricalCurrent.valueOf(value);
        }
    }

    /**
     * Convert an electrical inductance String with unit on the command line to an ElectricalInductance scalar.
     */
    public static class ELECTRICALINDUCTANCE implements ITypeConverter<ElectricalInductance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalInductance convert(final String value) throws Exception
        {
            return ElectricalInductance.valueOf(value);
        }
    }

    /**
     * Convert an electrical potential String with unit on the command line to an ElectricalPotential scalar.
     */
    public static class ELECTRICALPOTENTIAL implements ITypeConverter<ElectricalPotential>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalPotential convert(final String value) throws Exception
        {
            return ElectricalPotential.valueOf(value);
        }
    }

    /**
     * Convert an electrical resistance String with unit on the command line to an ElectricalResistance scalar.
     */
    public static class ELECTRICALRESISTANCE implements ITypeConverter<ElectricalResistance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public ElectricalResistance convert(final String value) throws Exception
        {
            return ElectricalResistance.valueOf(value);
        }
    }

    /**
     * Convert an energy String with unit on the command line to an Energy scalar.
     */
    public static class ENERGY implements ITypeConverter<Energy>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Energy convert(final String value) throws Exception
        {
            return Energy.valueOf(value);
        }
    }

    /**
     * Convert an equivalent dose String with unit on the command line to an EquivalentDose scalar.
     */
    public static class EQUIVALENTDOSE implements ITypeConverter<EquivalentDose>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public EquivalentDose convert(final String value) throws Exception
        {
            return EquivalentDose.valueOf(value);
        }
    }

    /**
     * Convert a flow mass String with unit on the command line to a FlowMass scalar.
     */
    public static class FLOWMASS implements ITypeConverter<FlowMass>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public FlowMass convert(final String value) throws Exception
        {
            return FlowMass.valueOf(value);
        }
    }

    /**
     * Convert a flow volume String with unit on the command line to a FlowVolume scalar.
     */
    public static class FLOWVOLUME implements ITypeConverter<FlowVolume>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public FlowVolume convert(final String value) throws Exception
        {
            return FlowVolume.valueOf(value);
        }
    }

    /**
     * Convert a force String with unit on the command line to a Force scalar.
     */
    public static class FORCE implements ITypeConverter<Force>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Force convert(final String value) throws Exception
        {
            return Force.valueOf(value);
        }
    }

    /**
     * Convert a frequency String with unit on the command line to a Frequency scalar.
     */
    public static class FREQUENCY implements ITypeConverter<Frequency>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Frequency convert(final String value) throws Exception
        {
            return Frequency.valueOf(value);
        }
    }

    /**
     * Convert an illuminance String with unit on the command line to an Illuminance scalar.
     */
    public static class ILLUMINANCE implements ITypeConverter<Illuminance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Illuminance convert(final String value) throws Exception
        {
            return Illuminance.valueOf(value);
        }
    }

    /**
     * Convert a length String with unit on the command line to a Length scalar.
     */
    public static class LENGTH implements ITypeConverter<Length>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Length convert(final String value) throws Exception
        {
            return Length.valueOf(value);
        }
    }

    /**
     * Convert a linear density String with unit on the command line to a LinearDensity scalar.
     */
    public static class LINEARDENSITY implements ITypeConverter<LinearDensity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public LinearDensity convert(final String value) throws Exception
        {
            return LinearDensity.valueOf(value);
        }
    }

    /**
     * Convert a luminous flux String with unit on the command line to a LuminousFlux scalar.
     */
    public static class LUMINOUSFLUX implements ITypeConverter<LuminousFlux>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public LuminousFlux convert(final String value) throws Exception
        {
            return LuminousFlux.valueOf(value);
        }
    }

    /**
     * Convert a luminous intensity String with unit on the command line to a LuminousIntensity scalar.
     */
    public static class LUMINOUSINTENSITY implements ITypeConverter<LuminousIntensity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public LuminousIntensity convert(final String value) throws Exception
        {
            return LuminousIntensity.valueOf(value);
        }
    }

    /**
     * Convert a magnetic flux String with unit on the command line to a MagneticFlux scalar.
     */
    public static class MAGNETICFLUX implements ITypeConverter<MagneticFlux>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public MagneticFlux convert(final String value) throws Exception
        {
            return MagneticFlux.valueOf(value);
        }
    }

    /**
     * Convert a magnetic flux density String with unit on the command line to a MagneticFluxDensity scalar.
     */
    public static class MAGNETICFLUXDENSITY implements ITypeConverter<MagneticFluxDensity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public MagneticFluxDensity convert(final String value) throws Exception
        {
            return MagneticFluxDensity.valueOf(value);
        }
    }

    /**
     * Convert a mass String with unit on the command line to a Mass scalar.
     */
    public static class MASS implements ITypeConverter<Mass>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Mass convert(final String value) throws Exception
        {
            return Mass.valueOf(value);
        }
    }

    /**
     * Convert a momentum String with unit on the command line to a Momentum scalar.
     */
    public static class MOMENTUM implements ITypeConverter<Momentum>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Momentum convert(final String value) throws Exception
        {
            return Momentum.valueOf(value);
        }
    }

    /**
     * Convert a position String with unit on the command line to a Position scalar.
     */
    public static class POSITION implements ITypeConverter<Position>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Position convert(final String value) throws Exception
        {
            return Position.valueOf(value);
        }
    }

    /**
     * Convert a power String with unit on the command line to a Power scalar.
     */
    public static class POWER implements ITypeConverter<Power>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Power convert(final String value) throws Exception
        {
            return Power.valueOf(value);
        }
    }

    /**
     * Convert a pressure String with unit on the command line to a Pressure scalar.
     */
    public static class PRESSURE implements ITypeConverter<Pressure>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Pressure convert(final String value) throws Exception
        {
            return Pressure.valueOf(value);
        }
    }

    /**
     * Convert a radio activity String with unit on the command line to a RadioActivity scalar.
     */
    public static class RADIOACTIVITY implements ITypeConverter<RadioActivity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public RadioActivity convert(final String value) throws Exception
        {
            return RadioActivity.valueOf(value);
        }
    }

    /**
     * Convert a solid angle String with unit on the command line to a SolidAngle scalar.
     */
    public static class SOLIDANGLE implements ITypeConverter<SolidAngle>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public SolidAngle convert(final String value) throws Exception
        {
            return SolidAngle.valueOf(value);
        }
    }

    /**
     * Convert a speed String with unit on the command line to a Speed scalar.
     */
    public static class SPEED implements ITypeConverter<Speed>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Speed convert(final String value) throws Exception
        {
            return Speed.valueOf(value);
        }
    }

    /**
     * Convert a temperature String with unit on the command line to a Temperature scalar.
     */
    public static class TEMPERATURE implements ITypeConverter<Temperature>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Temperature convert(final String value) throws Exception
        {
            return Temperature.valueOf(value);
        }
    }

    /**
     * Convert a time String with unit on the command line to a Time scalar.
     */
    public static class TIME implements ITypeConverter<Time>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Time convert(final String value) throws Exception
        {
            return Time.valueOf(value);
        }
    }

    /**
     * Convert a torque String with unit on the command line to a Torque scalar.
     */
    public static class TORQUE implements ITypeConverter<Torque>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Torque convert(final String value) throws Exception
        {
            return Torque.valueOf(value);
        }
    }

    /**
     * Convert a volume String with unit on the command line to a Volume scalar.
     */
    public static class VOLUME implements ITypeConverter<Volume>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2020-01-17T12:27:20.797349900Z")
        public Volume convert(final String value) throws Exception
        {
            return Volume.valueOf(value);
        }
    }

}
