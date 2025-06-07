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
    @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
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
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public AbsoluteTemperature convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = AbsoluteTemperature.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an absorbed dose String with unit on the command line to an AbsorbedDose scalar.
     */
    public static class ABSORBEDDOSE implements ITypeConverter<AbsorbedDose>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public AbsorbedDose convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = AbsorbedDose.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an acceleration String with unit on the command line to an Acceleration scalar.
     */
    public static class ACCELERATION implements ITypeConverter<Acceleration>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Acceleration convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Acceleration.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an amount of substance String with unit on the command line to an AmountOfSubstance scalar.
     */
    public static class AMOUNTOFSUBSTANCE implements ITypeConverter<AmountOfSubstance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public AmountOfSubstance convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = AmountOfSubstance.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an angle String with unit on the command line to an Angle scalar.
     */
    public static class ANGLE implements ITypeConverter<Angle>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Angle convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Angle.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an angular acceleration String with unit on the command line to an AngularAcceleration scalar.
     */
    public static class ANGULARACCELERATION implements ITypeConverter<AngularAcceleration>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public AngularAcceleration convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = AngularAcceleration.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an angular velocity String with unit on the command line to an AngularVelocity scalar.
     */
    public static class ANGULARVELOCITY implements ITypeConverter<AngularVelocity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public AngularVelocity convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = AngularVelocity.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an area String with unit on the command line to an Area scalar.
     */
    public static class AREA implements ITypeConverter<Area>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Area convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Area.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a catalytic activity String with unit on the command line to a CatalyticActivity scalar.
     */
    public static class CATALYTICACTIVITY implements ITypeConverter<CatalyticActivity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public CatalyticActivity convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = CatalyticActivity.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a density String with unit on the command line to a Density scalar.
     */
    public static class DENSITY implements ITypeConverter<Density>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Density convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Density.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a dimensionless String with unit on the command line to a Dimensionless scalar.
     */
    public static class DIMENSIONLESS implements ITypeConverter<Dimensionless>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Dimensionless convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Dimensionless.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a direction String with unit on the command line to a Direction scalar.
     */
    public static class DIRECTION implements ITypeConverter<Direction>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Direction convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Direction.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a duration String with unit on the command line to a Duration scalar.
     */
    public static class DURATION implements ITypeConverter<Duration>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Duration convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Duration.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical capacitance String with unit on the command line to an ElectricalCapacitance scalar.
     */
    public static class ELECTRICALCAPACITANCE implements ITypeConverter<ElectricalCapacitance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalCapacitance convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalCapacitance.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical charge String with unit on the command line to an ElectricalCharge scalar.
     */
    public static class ELECTRICALCHARGE implements ITypeConverter<ElectricalCharge>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalCharge convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalCharge.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical conductance String with unit on the command line to an ElectricalConductance scalar.
     */
    public static class ELECTRICALCONDUCTANCE implements ITypeConverter<ElectricalConductance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalConductance convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalConductance.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical current String with unit on the command line to an ElectricalCurrent scalar.
     */
    public static class ELECTRICALCURRENT implements ITypeConverter<ElectricalCurrent>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalCurrent convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalCurrent.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical inductance String with unit on the command line to an ElectricalInductance scalar.
     */
    public static class ELECTRICALINDUCTANCE implements ITypeConverter<ElectricalInductance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalInductance convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalInductance.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical potential String with unit on the command line to an ElectricalPotential scalar.
     */
    public static class ELECTRICALPOTENTIAL implements ITypeConverter<ElectricalPotential>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalPotential convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalPotential.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an electrical resistance String with unit on the command line to an ElectricalResistance scalar.
     */
    public static class ELECTRICALRESISTANCE implements ITypeConverter<ElectricalResistance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public ElectricalResistance convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = ElectricalResistance.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an energy String with unit on the command line to an Energy scalar.
     */
    public static class ENERGY implements ITypeConverter<Energy>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Energy convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Energy.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an equivalent dose String with unit on the command line to an EquivalentDose scalar.
     */
    public static class EQUIVALENTDOSE implements ITypeConverter<EquivalentDose>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public EquivalentDose convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = EquivalentDose.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a flow mass String with unit on the command line to a FlowMass scalar.
     */
    public static class FLOWMASS implements ITypeConverter<FlowMass>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public FlowMass convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = FlowMass.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a flow volume String with unit on the command line to a FlowVolume scalar.
     */
    public static class FLOWVOLUME implements ITypeConverter<FlowVolume>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public FlowVolume convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = FlowVolume.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a force String with unit on the command line to a Force scalar.
     */
    public static class FORCE implements ITypeConverter<Force>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Force convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Force.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a frequency String with unit on the command line to a Frequency scalar.
     */
    public static class FREQUENCY implements ITypeConverter<Frequency>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Frequency convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Frequency.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert an illuminance String with unit on the command line to an Illuminance scalar.
     */
    public static class ILLUMINANCE implements ITypeConverter<Illuminance>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Illuminance convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Illuminance.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a length String with unit on the command line to a Length scalar.
     */
    public static class LENGTH implements ITypeConverter<Length>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Length convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Length.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a linear density String with unit on the command line to a LinearDensity scalar.
     */
    public static class LINEARDENSITY implements ITypeConverter<LinearDensity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public LinearDensity convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = LinearDensity.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a luminous flux String with unit on the command line to a LuminousFlux scalar.
     */
    public static class LUMINOUSFLUX implements ITypeConverter<LuminousFlux>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public LuminousFlux convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = LuminousFlux.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a luminous intensity String with unit on the command line to a LuminousIntensity scalar.
     */
    public static class LUMINOUSINTENSITY implements ITypeConverter<LuminousIntensity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public LuminousIntensity convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = LuminousIntensity.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a magnetic flux String with unit on the command line to a MagneticFlux scalar.
     */
    public static class MAGNETICFLUX implements ITypeConverter<MagneticFlux>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public MagneticFlux convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = MagneticFlux.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a magnetic flux density String with unit on the command line to a MagneticFluxDensity scalar.
     */
    public static class MAGNETICFLUXDENSITY implements ITypeConverter<MagneticFluxDensity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public MagneticFluxDensity convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = MagneticFluxDensity.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a mass String with unit on the command line to a Mass scalar.
     */
    public static class MASS implements ITypeConverter<Mass>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Mass convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Mass.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a momentum String with unit on the command line to a Momentum scalar.
     */
    public static class MOMENTUM implements ITypeConverter<Momentum>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Momentum convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Momentum.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a position String with unit on the command line to a Position scalar.
     */
    public static class POSITION implements ITypeConverter<Position>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Position convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Position.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a power String with unit on the command line to a Power scalar.
     */
    public static class POWER implements ITypeConverter<Power>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Power convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Power.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a pressure String with unit on the command line to a Pressure scalar.
     */
    public static class PRESSURE implements ITypeConverter<Pressure>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Pressure convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Pressure.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a radio activity String with unit on the command line to a RadioActivity scalar.
     */
    public static class RADIOACTIVITY implements ITypeConverter<RadioActivity>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public RadioActivity convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = RadioActivity.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a solid angle String with unit on the command line to a SolidAngle scalar.
     */
    public static class SOLIDANGLE implements ITypeConverter<SolidAngle>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public SolidAngle convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = SolidAngle.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a speed String with unit on the command line to a Speed scalar.
     */
    public static class SPEED implements ITypeConverter<Speed>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Speed convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Speed.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a temperature String with unit on the command line to a Temperature scalar.
     */
    public static class TEMPERATURE implements ITypeConverter<Temperature>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Temperature convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Temperature.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a time String with unit on the command line to a Time scalar.
     */
    public static class TIME implements ITypeConverter<Time>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Time convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Time.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a torque String with unit on the command line to a Torque scalar.
     */
    public static class TORQUE implements ITypeConverter<Torque>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Torque convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Torque.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

    /**
     * Convert a volume String with unit on the command line to a Volume scalar.
     */
    public static class VOLUME implements ITypeConverter<Volume>
    {
        @Override
        @Generated(value = "org.djunits.generator.GenerateCliConverters", date = "2025-06-07T10:15:31.719700800Z")
        public Volume convert(final String value) throws Exception
        {
            CliUtil.prepareLocale();
            var ret = Volume.valueOf(value);
            CliUtil.restoreLocale();
            return ret;
        }
    }

}
