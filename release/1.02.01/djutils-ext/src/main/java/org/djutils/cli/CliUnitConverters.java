package org.djutils.cli;

import org.djunits.value.vdouble.scalar.AbsoluteTemperature;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Angle;
import org.djunits.value.vdouble.scalar.AngleSolid;
import org.djunits.value.vdouble.scalar.Area;
import org.djunits.value.vdouble.scalar.Density;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.Direction;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.ElectricalCharge;
import org.djunits.value.vdouble.scalar.ElectricalCurrent;
import org.djunits.value.vdouble.scalar.ElectricalPotential;
import org.djunits.value.vdouble.scalar.ElectricalResistance;
import org.djunits.value.vdouble.scalar.Energy;
import org.djunits.value.vdouble.scalar.FlowMass;
import org.djunits.value.vdouble.scalar.FlowVolume;
import org.djunits.value.vdouble.scalar.Force;
import org.djunits.value.vdouble.scalar.Frequency;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.LinearDensity;
import org.djunits.value.vdouble.scalar.Mass;
import org.djunits.value.vdouble.scalar.Position;
import org.djunits.value.vdouble.scalar.Power;
import org.djunits.value.vdouble.scalar.Pressure;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Temperature;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vdouble.scalar.Torque;
import org.djunits.value.vdouble.scalar.Volume;

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
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class CliUnitConverters
{
    /**
     * Register all DJUNITS converters for a CommandLine.
     * @param cmd String; the CommandLine for which the DJUNITS converters should be registered
     */
    public static void registerAll(final CommandLine cmd)
    {
        cmd.registerConverter(AbsoluteTemperature.class, new ABSOLUTETEMPERATURE());
        cmd.registerConverter(Acceleration.class, new ACCELERATION());
        cmd.registerConverter(Angle.class, new ANGLE());
        cmd.registerConverter(AngleSolid.class, new ANGLESOLID());
        cmd.registerConverter(Area.class, new AREA());
        cmd.registerConverter(Density.class, new DENSITY());
        cmd.registerConverter(Dimensionless.class, new DIMENSIONLESS());
        cmd.registerConverter(Direction.class, new DIRECTION());
        cmd.registerConverter(Duration.class, new DURATION());
        cmd.registerConverter(ElectricalCharge.class, new ELECTRICALCHARGE());
        cmd.registerConverter(ElectricalCurrent.class, new ELECTRICALCURRENT());
        cmd.registerConverter(ElectricalPotential.class, new ELECTRICALPOTENTIAL());
        cmd.registerConverter(ElectricalResistance.class, new ELECTRICALRESISTANCE());
        cmd.registerConverter(Energy.class, new ENERGY());
        cmd.registerConverter(FlowMass.class, new FLOWMASS());
        cmd.registerConverter(FlowVolume.class, new FLOWVOLUME());
        cmd.registerConverter(Force.class, new FORCE());
        cmd.registerConverter(Frequency.class, new FREQUENCY());
        cmd.registerConverter(Length.class, new LENGTH());
        cmd.registerConverter(LinearDensity.class, new LINEARDENSITY());
        cmd.registerConverter(Mass.class, new MASS());
        cmd.registerConverter(Position.class, new POSITION());
        cmd.registerConverter(Power.class, new POWER());
        cmd.registerConverter(Pressure.class, new PRESSURE());
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
        /** {@inheritDoc} */
        @Override
        public AbsoluteTemperature convert(final String value) throws Exception
        {
            return AbsoluteTemperature.valueOf(value);
        }
    }

    /**
     * Convert an acceleration String with unit on the command line to an Acceleration scalar.
     */
    public static class ACCELERATION implements ITypeConverter<Acceleration>
    {
        /** {@inheritDoc} */
        @Override
        public Acceleration convert(final String value) throws Exception
        {
            return Acceleration.valueOf(value);
        }
    }

    /**
     * Convert an angle String with unit on the command line to an Angle scalar.
     */
    public static class ANGLE implements ITypeConverter<Angle>
    {
        /** {@inheritDoc} */
        @Override
        public Angle convert(final String value) throws Exception
        {
            return Angle.valueOf(value);
        }
    }

    /**
     * Convert a solid angle String with unit on the command line to an AngleSolid scalar.
     */
    public static class ANGLESOLID implements ITypeConverter<AngleSolid>
    {
        /** {@inheritDoc} */
        @Override
        public AngleSolid convert(final String value) throws Exception
        {
            return AngleSolid.valueOf(value);
        }
    }

    /**
     * Convert an area String with unit on the command line to an Area scalar.
     */
    public static class AREA implements ITypeConverter<Area>
    {
        /** {@inheritDoc} */
        @Override
        public Area convert(final String value) throws Exception
        {
            return Area.valueOf(value);
        }
    }

    /**
     * Convert a density String with unit on the command line to a Density scalar.
     */
    public static class DENSITY implements ITypeConverter<Density>
    {
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
        public Duration convert(final String value) throws Exception
        {
            return Duration.valueOf(value);
        }
    }

    /**
     * Convert an electrical charge String with unit on the command line to an ElectricalCharge scalar.
     */
    public static class ELECTRICALCHARGE implements ITypeConverter<ElectricalCharge>
    {
        /** {@inheritDoc} */
        @Override
        public ElectricalCharge convert(final String value) throws Exception
        {
            return ElectricalCharge.valueOf(value);
        }
    }

    /**
     * Convert an electrical current String with unit on the command line to an ElectricalCurrent scalar.
     */
    public static class ELECTRICALCURRENT implements ITypeConverter<ElectricalCurrent>
    {
        /** {@inheritDoc} */
        @Override
        public ElectricalCurrent convert(final String value) throws Exception
        {
            return ElectricalCurrent.valueOf(value);
        }
    }

    /**
     * Convert an electrical potential String with unit on the command line to an ElectricalPotential scalar.
     */
    public static class ELECTRICALPOTENTIAL implements ITypeConverter<ElectricalPotential>
    {
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
        public Energy convert(final String value) throws Exception
        {
            return Energy.valueOf(value);
        }
    }

    /**
     * Convert a flow mass String with unit on the command line to a FlowMass scalar.
     */
    public static class FLOWMASS implements ITypeConverter<FlowMass>
    {
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
        public Frequency convert(final String value) throws Exception
        {
            return Frequency.valueOf(value);
        }
    }

    /**
     * Convert a length String with unit on the command line to a Length scalar.
     */
    public static class LENGTH implements ITypeConverter<Length>
    {
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
        public LinearDensity convert(final String value) throws Exception
        {
            return LinearDensity.valueOf(value);
        }
    }

    /**
     * Convert a mass String with unit on the command line to a Mass scalar.
     */
    public static class MASS implements ITypeConverter<Mass>
    {
        /** {@inheritDoc} */
        @Override
        public Mass convert(final String value) throws Exception
        {
            return Mass.valueOf(value);
        }
    }

    /**
     * Convert a position String with unit on the command line to a Position scalar.
     */
    public static class POSITION implements ITypeConverter<Position>
    {
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
        public Pressure convert(final String value) throws Exception
        {
            return Pressure.valueOf(value);
        }
    }

    /**
     * Convert a speed String with unit on the command line to a Speed scalar.
     */
    public static class SPEED implements ITypeConverter<Speed>
    {
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
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
        /** {@inheritDoc} */
        @Override
        public Volume convert(final String value) throws Exception
        {
            return Volume.valueOf(value);
        }
    }

}
