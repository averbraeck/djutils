package org.djutils.cli;

import static org.junit.Assert.assertEquals;

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
import org.djunits.unit.VolumeUnit;
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
        @Option(names = {"--absorbeddose"}, description = "AbsorbedDose", defaultValue = "200Gy")
        protected AbsorbedDose absorbeddose;

        /** */
        @Option(names = {"--acceleration"}, description = "Acceleration", defaultValue = "2.0m/s^2")
        protected Acceleration acceleration;

        /** */
        @Option(names = {"--amountofsubstance"}, description = "AmountOfSubstance", defaultValue = "200.0mol")
        protected AmountOfSubstance amountofsubstance;

        /** */
        @Option(names = {"--angle"}, description = "Angle", defaultValue = "2.0rad")
        protected Angle angle;

        /** */
        @Option(names = {"--angularacceleration"}, description = "AngularAcceleration", defaultValue = "2.0rad/s2")
        protected AngularAcceleration angularacceleration;

        /** */
        @Option(names = {"--angularvelocity"}, description = "AngularVelocity", defaultValue = "2.0rad/s")
        protected AngularVelocity angularvelocity;

        /** */
        @Option(names = {"--area"}, description = "Area", defaultValue = "2.0m^2")
        protected Area area;

        /** */
        @Option(names = {"--catalyticactivity"}, description = "CatalyticActivity", defaultValue = "2.0kat")
        protected CatalyticActivity catalyticactivity;

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
        @Option(names = {"--electricalcapacitance"}, description = "ElectricalCapacitance", defaultValue = "2.0F")
        protected ElectricalCapacitance electricalcapacitance;

        /** */
        @Option(names = {"--electricalcharge"}, description = "ElectricalCharge", defaultValue = "2.0C")
        protected ElectricalCharge electricalcharge;

        /** */
        @Option(names = {"--electricalconductance"}, description = "ElectricalConductance", defaultValue = "2.0S")
        protected ElectricalConductance electricalconductance;

        /** */
        @Option(names = {"--electricalcurrent"}, description = "ElectricalCurrent", defaultValue = "2.0A")
        protected ElectricalCurrent electricalcurrent;

        /** */
        @Option(names = {"--electricalinductance"}, description = "ElectricalInductance", defaultValue = "2.0H")
        protected ElectricalInductance electricalinductance;

        /** */
        @Option(names = {"--electricalpotential"}, description = "ElectricalPotential", defaultValue = "2.0V")
        protected ElectricalPotential electricalpotential;

        /** */
        @Option(names = {"--electricalresistance"}, description = "ElectricalResistance", defaultValue = "2.0ohm")
        protected ElectricalResistance electricalresistance;

        /** */
        @Option(names = {"--energy"}, description = "Energy", defaultValue = "2.0J")
        protected Energy energy;

        /** */
        @Option(names = {"--equivalentdose"}, description = "EquivalentDose", defaultValue = "2.0Sv")
        protected EquivalentDose equivalentdose;

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
        @Option(names = {"--illuminance"}, description = "Illuminance", defaultValue = "2.0lx")
        protected Illuminance illuminance;

        /** */
        @Option(names = {"--length"}, description = "Length", defaultValue = "2.0m")
        protected Length length;

        /** */
        @Option(names = {"--lineardensity"}, description = "LinearDensity", defaultValue = "2.0/m")
        protected LinearDensity lineardensity;

        /** */
        @Option(names = {"--luminousflux"}, description = "LuminousFlux", defaultValue = "2.0lm")
        protected LuminousFlux luminousflux;

        /** */
        @Option(names = {"--luminousintensity"}, description = "LuminousIntensity", defaultValue = "2.0cd")
        protected LuminousIntensity luminousintensity;

        /** */
        @Option(names = {"--magneticflux"}, description = "MagneticFlux", defaultValue = "2.0Wb")
        protected MagneticFlux magneticflux;

        /** */
        @Option(names = {"--magneticfluxdensity"}, description = "MagneticFluxDensity", defaultValue = "2.0T")
        protected MagneticFluxDensity magneticfluxdensity;

        /** */
        @Option(names = {"--mass"}, description = "Mass", defaultValue = "2.0kg")
        protected Mass mass;

        /** */
        @Option(names = {"--momentum"}, description = "Momentum", defaultValue = "2.0kgm/s")
        protected Momentum momentum;

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
        @Option(names = {"--radioactivity"}, description = "RadioActivity", defaultValue = "2.0Bq")
        protected RadioActivity radioactivity;

        /** */
        @Option(names = {"--solidangle"}, description = "SolidAngle", defaultValue = "2.0sr")
        protected SolidAngle solidangle;

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
        assertEquals(new AbsorbedDose(200.0, AbsorbedDoseUnit.GRAY), options.absorbeddose);
        args = new String[] {"--absorbeddose", "100.0erg/g"};
        CliUtil.execute(options, args);
        assertEquals(new AbsorbedDose(100.0, AbsorbedDoseUnit.ERG_PER_GRAM), options.absorbeddose);

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
        assertEquals(new AmountOfSubstance(200.0, AmountOfSubstanceUnit.MOLE), options.amountofsubstance);
        args = new String[] {"--amountofsubstance", "10mmol"};
        CliUtil.execute(options, args);
        assertEquals(new AmountOfSubstance(10.0, AmountOfSubstanceUnit.MILLIMOLE), options.amountofsubstance);

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
        assertEquals(new AngularAcceleration(2.0, AngularAccelerationUnit.RADIAN_PER_SECOND_SQUARED),
                options.angularacceleration);
        args = new String[] {"--angularacceleration", "10.0c\"/sec2"};
        CliUtil.execute(options, args);
        assertEquals(new AngularAcceleration(10.0, AngularAccelerationUnit.CENTESIMAL_ARCSECOND_PER_SECOND_SQUARED),
                options.angularacceleration);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new AngularVelocity(2.0, AngularVelocityUnit.RADIAN_PER_SECOND), options.angularvelocity);
        args = new String[] {"--angularvelocity", "10.0c\"/sec"};
        CliUtil.execute(options, args);
        assertEquals(new AngularVelocity(10.0, AngularVelocityUnit.CENTESIMAL_ARCSECOND_PER_SECOND), options.angularvelocity);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new Area(2.0, AreaUnit.SQUARE_METER), options.area);
        args = new String[] {"--area", "1.0ac"};
        CliUtil.execute(options, args);
        assertEquals(new Area(1.0, AreaUnit.ACRE), options.area);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new CatalyticActivity(2.0, CatalyticActivityUnit.KATAL), options.catalyticactivity);
        args = new String[] {"--catalyticactivity", "1.0mkat"};
        CliUtil.execute(options, args);
        assertEquals(new CatalyticActivity(1.0, CatalyticActivityUnit.MILLIKATAL), options.catalyticactivity);

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
        assertEquals(new ElectricalCapacitance(2.0, ElectricalCapacitanceUnit.FARAD), options.electricalcapacitance);
        args = new String[] {"--electricalcapacitance", "1.0uF"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCapacitance(1.0, ElectricalCapacitanceUnit.MICROFARAD), options.electricalcapacitance);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCharge(2.0, ElectricalChargeUnit.COULOMB), options.electricalcharge);
        args = new String[] {"--electricalcharge", "1.0mAh"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalCharge(1.0, ElectricalChargeUnit.MILLIAMPERE_HOUR), options.electricalcharge);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new ElectricalConductance(2.0, ElectricalConductanceUnit.SIEMENS), options.electricalconductance);
        args = new String[] {"--electricalconductance", "1.0mS"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalConductance(1.0, ElectricalConductanceUnit.MILLISIEMENS), options.electricalconductance);

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
        assertEquals(new ElectricalInductance(2.0, ElectricalInductanceUnit.HENRY), options.electricalinductance);
        args = new String[] {"--electricalinductance", "1.0mH"};
        CliUtil.execute(options, args);
        assertEquals(new ElectricalInductance(1.0, ElectricalInductanceUnit.MILLIHENRY), options.electricalinductance);

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
        args = new String[] {"--electricalresistance", "1.0stohm"};
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
        assertEquals(new EquivalentDose(2.0, EquivalentDoseUnit.SIEVERT), options.equivalentdose);
        args = new String[] {"--equivalentdose", "1.0rem"};
        CliUtil.execute(options, args);
        assertEquals(new EquivalentDose(1.0, EquivalentDoseUnit.REM), options.equivalentdose);

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
        args = new String[] {"--flowvolume", "1.0gal(US)/day"};
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
        assertEquals(new Illuminance(2.0, IlluminanceUnit.LUX), options.illuminance);
        args = new String[] {"--illuminance", "1.0nx"};
        CliUtil.execute(options, args);
        assertEquals(new Illuminance(1.0, IlluminanceUnit.NOX), options.illuminance);

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
        assertEquals(new LuminousFlux(2.0, LuminousFluxUnit.LUMEN), options.luminousflux);
        args = new String[] {"--luminousflux", "1.0srcd"};
        CliUtil.execute(options, args);
        assertEquals(new LuminousFlux(1.0, LuminousFluxUnit.LUMEN), options.luminousflux);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new LuminousIntensity(2.0, LuminousIntensityUnit.CANDELA), options.luminousintensity);
        args = new String[] {"--luminousintensity", "1.0cd"};
        CliUtil.execute(options, args);
        assertEquals(new LuminousIntensity(1.0, LuminousIntensityUnit.SI), options.luminousintensity);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new MagneticFlux(2.0, MagneticFluxUnit.WEBER), options.magneticflux);
        args = new String[] {"--magneticflux", "1.0Mx"};
        CliUtil.execute(options, args);
        assertEquals(new MagneticFlux(1.0, MagneticFluxUnit.MAXWELL), options.magneticflux);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new MagneticFluxDensity(2.0, MagneticFluxDensityUnit.TESLA), options.magneticfluxdensity);
        args = new String[] {"--magneticfluxdensity", "1.0G"};
        CliUtil.execute(options, args);
        assertEquals(new MagneticFluxDensity(1.0, MagneticFluxDensityUnit.GAUSS), options.magneticfluxdensity);

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
        assertEquals(new Momentum(2.0, MomentumUnit.SI), options.momentum);
        args = new String[] {"--momentum", "1.0kgm/sec"};
        CliUtil.execute(options, args);
        assertEquals(new Momentum(1.0, MomentumUnit.KILOGRAM_METER_PER_SECOND), options.momentum);

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
        assertEquals(new RadioActivity(2.0, RadioActivityUnit.BECQUEREL), options.radioactivity);
        args = new String[] {"--radioactivity", "1.0Ci"};
        CliUtil.execute(options, args);
        assertEquals(new RadioActivity(1.0, RadioActivityUnit.CURIE), options.radioactivity);

        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(new SolidAngle(2.0, SolidAngleUnit.STERADIAN), options.solidangle);
        args = new String[] {"--solidangle", "1.0sq.deg"};
        CliUtil.execute(options, args);
        assertEquals(new SolidAngle(1.0, SolidAngleUnit.SQUARE_DEGREE), options.solidangle);

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
