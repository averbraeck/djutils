package org.djutils.cli;

import static org.junit.Assert.assertEquals;

import org.djunits.unit.AbsoluteTemperatureUnit;
import org.djunits.unit.AccelerationUnit;
import org.djunits.unit.SolidAngleUnit;
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
import org.djunits.unit.PositionUnit;
import org.djunits.unit.PowerUnit;
import org.djunits.unit.PressureUnit;
import org.djunits.unit.SpeedUnit;
import org.djunits.unit.TemperatureUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.unit.TorqueUnit;
import org.djunits.unit.VolumeUnit;
import org.djunits.value.vdouble.scalar.AbsoluteTemperature;
import org.djunits.value.vdouble.scalar.Acceleration;
import org.djunits.value.vdouble.scalar.Angle;
import org.djunits.value.vdouble.scalar.SolidAngle;
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
import org.junit.Test;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * Program to test the DJUNITS unit converters for CLI. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class TestCLIUnitConverters
{
    /** */
    @Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
    public static class Options
    {
        /** */
        @Option(names = {"--absolutetemperature"}, description = "AbsoluteTemperature", defaultValue = "200.0K")
        protected AbsoluteTemperature absolutetemperature;

        /** */
        @Option(names = {"--acceleration"}, description = "Acceleration", defaultValue = "2.0m/s^2")
        protected Acceleration acceleration;

        /** */
        @Option(names = {"--angle"}, description = "Angle", defaultValue = "2.0rad")
        protected Angle angle;

        /** */
        @Option(names = {"--anglesolid"}, description = "SolidAngle", defaultValue = "2.0sr")
        protected SolidAngle anglesolid;

        /** */
        @Option(names = {"--area"}, description = "Area", defaultValue = "2.0m^2")
        protected Area area;

        /** */
        @Option(names = {"--density"}, description = "Density", defaultValue = "2.0kg/m^3")
        protected Density density;

        /** */
        @Option(names = {"--dimensionless"}, description = "Dimensionless", defaultValue = "2.0")
        protected Dimensionless dimensionless;

        /** */
        @Option(names = {"--direction"}, description = "Direction", defaultValue = "2.0rad(E)")
        protected Direction direction;

        /** */
        @Option(names = {"--duration"}, description = "Duration", defaultValue = "2.0s")
        protected Duration duration;

        /** */
        @Option(names = {"--electricalcharge"}, description = "ElectricalCharge", defaultValue = "2.0C")
        protected ElectricalCharge electricalcharge;

        /** */
        @Option(names = {"--electricalcurrent"}, description = "ElectricalCurrent", defaultValue = "2.0A")
        protected ElectricalCurrent electricalcurrent;

        /** */
        @Option(names = {"--electricalpotential"}, description = "ElectricalPotential", defaultValue = "2.0V")
        protected ElectricalPotential electricalpotential;

        /** */
        @Option(names = {"--electricalresistance"}, description = "ElectricalResistance", defaultValue = "2.0Ohm")
        protected ElectricalResistance electricalresistance;

        /** */
        @Option(names = {"--energy"}, description = "Energy", defaultValue = "2.0J")
        protected Energy energy;

        /** */
        @Option(names = {"--flowmass"}, description = "FlowMass", defaultValue = "2.0kg/s")
        protected FlowMass flowmass;

        /** */
        @Option(names = {"--flowvolume"}, description = "FlowVolume", defaultValue = "2.0m^3/s")
        protected FlowVolume flowvolume;

        /** */
        @Option(names = {"--force"}, description = "Force", defaultValue = "2.0N")
        protected Force force;

        /** */
        @Option(names = {"--frequency"}, description = "Frequency", defaultValue = "2.0/s")
        protected Frequency frequency;

        /** */
        @Option(names = {"--length"}, description = "Length", defaultValue = "2.0m")
        protected Length length;

        /** */
        @Option(names = {"--lineardensity"}, description = "LinearDensity", defaultValue = "2.0/m")
        protected LinearDensity lineardensity;

        /** */
        @Option(names = {"--mass"}, description = "Mass", defaultValue = "2.0kg")
        protected Mass mass;

        /** */
        @Option(names = {"--position"}, description = "Position", defaultValue = "2.0AU")
        protected Position position;

        /** */
        @Option(names = {"--power"}, description = "Power", defaultValue = "2.0W")
        protected Power power;

        /** */
        @Option(names = {"--pressure"}, description = "Pressure", defaultValue = "2.0Pa")
        protected Pressure pressure;

        /** */
        @Option(names = {"--speed"}, description = "Speed", defaultValue = "2.0m/s")
        protected Speed speed;

        /** */
        @Option(names = {"--temperature"}, description = "Temperature", defaultValue = "2.0K")
        protected Temperature temperature;

        /** */
        @Option(names = {"--time"}, description = "Time", defaultValue = "2.0h")
        protected Time time;

        /** */
        @Option(names = {"--torque"}, description = "Torque", defaultValue = "2.0N.m")
        protected Torque torque;

        /** */
        @Option(names = {"--volume"}, description = "Volume", defaultValue = "2.0m^3")
        protected Volume volume;
    }

    /**
     * Test the CliUtil methods.
     * @throws CliException on error
     * @throws IllegalAccessException on error
     * @throws IllegalArgumentException on error
     * @throws NoSuchFieldException on error
     */
    @Test
    public void testCli() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, CliException
    {
        String[] args;
        Options options;

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new AbsoluteTemperature(200.0, AbsoluteTemperatureUnit.KELVIN), options.absolutetemperature);
        args = new String[] {"--absolutetemperature", "100.0C"};
        CliUtil.execute(options, args);
        assertEquals(new AbsoluteTemperature(100.0, AbsoluteTemperatureUnit.DEGREE_CELSIUS), options.absolutetemperature);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Acceleration(2.0, AccelerationUnit.METER_PER_SECOND_2), options.acceleration);
        args = new String[] {"--acceleration", "1.0km/h^2"};
        CliUtil.execute(options, args);
        assertEquals(new Acceleration(1.0, AccelerationUnit.KM_PER_HOUR_2), options.acceleration);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Angle(2.0, AngleUnit.RADIAN), options.angle);
        args = new String[] {"--angle", "10.0deg"};
        CliUtil.execute(options, args);
        assertEquals(new Angle(10.0, AngleUnit.DEGREE), options.angle);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new SolidAngle(2.0, SolidAngleUnit.STERADIAN), options.anglesolid);
        args = new String[] {"--anglesolid", "1.0sq.deg"};
        CliUtil.execute(options, args);
        assertEquals(new SolidAngle(1.0, SolidAngleUnit.SQUARE_DEGREE), options.anglesolid);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Area(2.0, AreaUnit.SQUARE_METER), options.area);
        args = new String[] {"--area", "1.0acre"};
        CliUtil.execute(options, args);
        assertEquals(new Area(1.0, AreaUnit.ACRE), options.area);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Density(2.0, DensityUnit.KG_PER_METER_3), options.density);
        args = new String[] {"--density", "1.0g/cm^3"};
        CliUtil.execute(options, args);
        assertEquals(new Density(1.0, DensityUnit.GRAM_PER_CENTIMETER_3), options.density);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Dimensionless(2.0, DimensionlessUnit.SI), options.dimensionless);
        args = new String[] {"--dimensionless", "1.0"};
        CliUtil.execute(options, args);
        assertEquals(new Dimensionless(1.0, DimensionlessUnit.SI), options.dimensionless);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Direction(2.0, DirectionUnit.EAST_RADIAN), options.direction);
        args = new String[] {"--direction", "1.0deg(N)"};
        CliUtil.execute(options, args);
        assertEquals(new Direction(1.0, DirectionUnit.NORTH_DEGREE), options.direction);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Duration(2.0, DurationUnit.SECOND), options.duration);
        args = new String[] {"--duration", "1.0day"};
        CliUtil.execute(options, args);
        assertEquals(new Duration(1.0, DurationUnit.DAY), options.duration);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCharge(2.0, ElectricalChargeUnit.COULOMB), options.electricalcharge);
        args = new String[] {"--electricalcharge", "1.0e"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCharge(1.0, ElectricalChargeUnit.ATOMIC_UNIT), options.electricalcharge);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCurrent(2.0, ElectricalCurrentUnit.AMPERE), options.electricalcurrent);
        args = new String[] {"--electricalcurrent", "1.0statA"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCurrent(1.0, ElectricalCurrentUnit.STATAMPERE), options.electricalcurrent);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new ElectricalPotential(2.0, ElectricalPotentialUnit.VOLT), options.electricalpotential);
        args = new String[] {"--electricalpotential", "1.0abV"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalPotential(1.0, ElectricalPotentialUnit.ABVOLT), options.electricalpotential);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new ElectricalResistance(2.0, ElectricalResistanceUnit.OHM), options.electricalresistance);
        args = new String[] {"--electricalresistance", "1.0stOhm"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalResistance(1.0, ElectricalResistanceUnit.STATOHM), options.electricalresistance);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Energy(2.0, EnergyUnit.JOULE), options.energy);
        args = new String[] {"--energy", "1.0kWh"};
        CliUtil.execute(options, args);
        assertEquals(new Energy(1.0, EnergyUnit.KILOWATT_HOUR), options.energy);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new FlowMass(2.0, FlowMassUnit.KILOGRAM_PER_SECOND), options.flowmass);
        args = new String[] {"--flowmass", "1.0lb/s"};
        CliUtil.execute(options, args);
        assertEquals(new FlowMass(1.0, FlowMassUnit.POUND_PER_SECOND), options.flowmass);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new FlowVolume(2.0, FlowVolumeUnit.CUBIC_METER_PER_SECOND), options.flowvolume);
        args = new String[] {"--flowvolume", "1.0gal/day"};
        CliUtil.execute(options, args);
        assertEquals(new FlowVolume(1.0, FlowVolumeUnit.GALLON_US_PER_DAY), options.flowvolume);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Force(2.0, ForceUnit.NEWTON), options.force);
        args = new String[] {"--force", "1.0kgf"};
        CliUtil.execute(options, args);
        assertEquals(new Force(1.0, ForceUnit.KILOGRAM_FORCE), options.force);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Frequency(2.0, FrequencyUnit.PER_SECOND), options.frequency);
        args = new String[] {"--frequency", "1.0kHz"};
        CliUtil.execute(options, args);
        assertEquals(new Frequency(1.0, FrequencyUnit.KILOHERTZ), options.frequency);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Length(2.0, LengthUnit.METER), options.length);
        args = new String[] {"--length", "1.0in"};
        CliUtil.execute(options, args);
        assertEquals(new Length(1.0, LengthUnit.INCH), options.length);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new LinearDensity(2.0, LinearDensityUnit.PER_METER), options.lineardensity);
        args = new String[] {"--lineardensity", "1.0/yd"};
        CliUtil.execute(options, args);
        assertEquals(new LinearDensity(1.0, LinearDensityUnit.PER_YARD), options.lineardensity);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Mass(2.0, MassUnit.KILOGRAM), options.mass);
        args = new String[] {"--mass", "1.0GeV"};
        CliUtil.execute(options, args);
        assertEquals(new Mass(1.0, MassUnit.GIGAELECTRONVOLT), options.mass);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Position(2.0, PositionUnit.ASTRONOMICAL_UNIT), options.position);
        args = new String[] {"--position", "1.0dm"};
        CliUtil.execute(options, args);
        assertEquals(new Position(1.0, PositionUnit.DECIMETER), options.position);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Power(2.0, PowerUnit.WATT), options.power);
        args = new String[] {"--power", "1.0ft.lbf/h"};
        CliUtil.execute(options, args);
        assertEquals(new Power(1.0, PowerUnit.FOOT_POUND_FORCE_PER_HOUR), options.power);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Pressure(2.0, PressureUnit.PASCAL), options.pressure);
        args = new String[] {"--pressure", "1.0mmHg"};
        CliUtil.execute(options, args);
        assertEquals(new Pressure(1.0, PressureUnit.MILLIMETER_MERCURY), options.pressure);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Speed(2.0, SpeedUnit.METER_PER_SECOND), options.speed);
        args = new String[] {"--speed", "1.0mi/h"};
        CliUtil.execute(options, args);
        assertEquals(new Speed(1.0, SpeedUnit.MILE_PER_HOUR), options.speed);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Temperature(2.0, TemperatureUnit.KELVIN), options.temperature);
        args = new String[] {"--temperature", "1.0degC"};
        CliUtil.execute(options, args);
        assertEquals(new Temperature(1.0, TemperatureUnit.DEGREE_CELSIUS), options.temperature);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Time(2.0, TimeUnit.BASE_HOUR), options.time);
        args = new String[] {"--time", "1.0day"};
        CliUtil.execute(options, args);
        assertEquals(new Time(1.0, TimeUnit.EPOCH_DAY), options.time);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Torque(2.0, TorqueUnit.NEWTON_METER), options.torque);
        args = new String[] {"--torque", "1.0lbf.ft"};
        CliUtil.execute(options, args);
        assertEquals(new Torque(1.0, TorqueUnit.POUND_FOOT), options.torque);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Volume(2.0, VolumeUnit.CUBIC_METER), options.volume);
        args = new String[] {"--volume", "1.0L"};
        CliUtil.execute(options, args);
        assertEquals(new Volume(1.0, VolumeUnit.LITER), options.volume);
    }
}
