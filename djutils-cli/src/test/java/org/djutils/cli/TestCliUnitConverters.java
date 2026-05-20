package org.djutils.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

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
import org.djunits.quantity.Power;
import org.djunits.quantity.Pressure;
import org.djunits.quantity.RadioActivity;
import org.djunits.quantity.SolidAngle;
import org.djunits.quantity.Speed;
import org.djunits.quantity.Temperature;
import org.djunits.quantity.Torque;
import org.djunits.quantity.Volume;
import org.djunits.quantity.VolumetricObjectDensity;
import org.junit.jupiter.api.Test;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * JUnit test for the generated DJUNITS CLI converters.
 * <p>
 * This test is generated at 2026-05-05T15:42:40.310257200Z by org.djunits.generator.GenerateCliConvertersTest and validates
 * that each {@code @Option} field parses its default and an explicit override by comparing SI values with a small tolerance.
 * </p>
 */
@SuppressWarnings("checkstyle:visibilitymodifier")
public class TestCliUnitConverters
{
    /** Numerical tolerance used for SI value comparisons. */
    private static final double EPS = 1.0E-12;

    /**
     * Picocli options holder for all quantities under test.
     */
    @Command(description = "Test program for CLI", name = "Program", mixinStandardHelpOptions = true, version = "1.0")
    public static class Options
    {
        /** Option for AbsorbedDose with a default value string. */
        @Option(names = {"--absorbeddose"}, description = "AbsorbedDose", defaultValue = "2.0erg/g")
        protected AbsorbedDose absorbeddose;

        /** Option for Acceleration with a default value string. */
        @Option(names = {"--acceleration"}, description = "Acceleration", defaultValue = "2.0ft/s2")
        protected Acceleration acceleration;

        /** Option for AmountOfSubstance with a default value string. */
        @Option(names = {"--amountofsubstance"}, description = "AmountOfSubstance", defaultValue = "2.0amol")
        protected AmountOfSubstance amountofsubstance;

        /** Option for Angle with a default value string. */
        @Option(names = {"--angle"}, description = "Angle", defaultValue = "2.0%")
        protected Angle angle;

        /** Option for AngularAcceleration with a default value string. */
        @Option(names = {"--angularacceleration"}, description = "AngularAcceleration", defaultValue = "2.0arcmin/s2")
        protected AngularAcceleration angularacceleration;

        /** Option for AngularVelocity with a default value string. */
        @Option(names = {"--angularvelocity"}, description = "AngularVelocity", defaultValue = "2.0arcmin/s")
        protected AngularVelocity angularvelocity;

        /** Option for Area with a default value string. */
        @Option(names = {"--area"}, description = "Area", defaultValue = "2.0a")
        protected Area area;

        /** Option for ArealObjectDensity with a default value string. */
        @Option(names = {"--arealobjectdensity"}, description = "ArealObjectDensity", defaultValue = "2.0/m2")
        protected ArealObjectDensity arealobjectdensity;

        /** Option for CatalyticActivity with a default value string. */
        @Option(names = {"--catalyticactivity"}, description = "CatalyticActivity", defaultValue = "2.0akat")
        protected CatalyticActivity catalyticactivity;

        /** Option for Density with a default value string. */
        @Option(names = {"--density"}, description = "Density", defaultValue = "2.0g/cm3")
        protected Density density;

        /** Option for Dimensionless with a default value string. */
        @Option(names = {"--dimensionless"}, description = "Dimensionless", defaultValue = "2.0 ")
        protected Dimensionless dimensionless;

        /** Option for Duration with a default value string. */
        @Option(names = {"--duration"}, description = "Duration", defaultValue = "2.0as")
        protected Duration duration;

        /** Option for ElectricalCapacitance with a default value string. */
        @Option(names = {"--electricalcapacitance"}, description = "ElectricalCapacitance", defaultValue = "2.0aF")
        protected ElectricalCapacitance electricalcapacitance;

        /** Option for ElectricalConductance with a default value string. */
        @Option(names = {"--electricalconductance"}, description = "ElectricalConductance", defaultValue = "2.0aS")
        protected ElectricalConductance electricalconductance;

        /** Option for ElectricalInductance with a default value string. */
        @Option(names = {"--electricalinductance"}, description = "ElectricalInductance", defaultValue = "2.0aH")
        protected ElectricalInductance electricalinductance;

        /** Option for ElectricalResistance with a default value string. */
        @Option(names = {"--electricalresistance"}, description = "ElectricalResistance", defaultValue = "2.0abohm")
        protected ElectricalResistance electricalresistance;

        /** Option for ElectricCharge with a default value string. */
        @Option(names = {"--electriccharge"}, description = "ElectricCharge", defaultValue = "2.0abC")
        protected ElectricCharge electriccharge;

        /** Option for ElectricCurrent with a default value string. */
        @Option(names = {"--electriccurrent"}, description = "ElectricCurrent", defaultValue = "2.0A")
        protected ElectricCurrent electriccurrent;

        /** Option for ElectricPotential with a default value string. */
        @Option(names = {"--electricpotential"}, description = "ElectricPotential", defaultValue = "2.0abV")
        protected ElectricPotential electricpotential;

        /** Option for Energy with a default value string. */
        @Option(names = {"--energy"}, description = "Energy", defaultValue = "2.0aJ")
        protected Energy energy;

        /** Option for EquivalentDose with a default value string. */
        @Option(names = {"--equivalentdose"}, description = "EquivalentDose", defaultValue = "2.0aSv")
        protected EquivalentDose equivalentdose;

        /** Option for FlowMass with a default value string. */
        @Option(names = {"--flowmass"}, description = "FlowMass", defaultValue = "2.0kg/s")
        protected FlowMass flowmass;

        /** Option for FlowVolume with a default value string. */
        @Option(names = {"--flowvolume"}, description = "FlowVolume", defaultValue = "2.0ft3/min")
        protected FlowVolume flowvolume;

        /** Option for Force with a default value string. */
        @Option(names = {"--force"}, description = "Force", defaultValue = "2.0aN")
        protected Force force;

        /** Option for Frequency with a default value string. */
        @Option(names = {"--frequency"}, description = "Frequency", defaultValue = "2.0aHz")
        protected Frequency frequency;

        /** Option for Illuminance with a default value string. */
        @Option(names = {"--illuminance"}, description = "Illuminance", defaultValue = "2.0alx")
        protected Illuminance illuminance;

        /** Option for Length with a default value string. */
        @Option(names = {"--length"}, description = "Length", defaultValue = "2.0A")
        protected Length length;

        /** Option for LinearDensity with a default value string. */
        @Option(names = {"--lineardensity"}, description = "LinearDensity", defaultValue = "2.0kg/m")
        protected LinearDensity lineardensity;

        /** Option for LinearObjectDensity with a default value string. */
        @Option(names = {"--linearobjectdensity"}, description = "LinearObjectDensity", defaultValue = "2.0/am")
        protected LinearObjectDensity linearobjectdensity;

        /** Option for LuminousFlux with a default value string. */
        @Option(names = {"--luminousflux"}, description = "LuminousFlux", defaultValue = "2.0lm")
        protected LuminousFlux luminousflux;

        /** Option for LuminousIntensity with a default value string. */
        @Option(names = {"--luminousintensity"}, description = "LuminousIntensity", defaultValue = "2.0acd")
        protected LuminousIntensity luminousintensity;

        /** Option for MagneticFlux with a default value string. */
        @Option(names = {"--magneticflux"}, description = "MagneticFlux", defaultValue = "2.0aWb")
        protected MagneticFlux magneticflux;

        /** Option for MagneticFluxDensity with a default value string. */
        @Option(names = {"--magneticfluxdensity"}, description = "MagneticFluxDensity", defaultValue = "2.0aT")
        protected MagneticFluxDensity magneticfluxdensity;

        /** Option for Mass with a default value string. */
        @Option(names = {"--mass"}, description = "Mass", defaultValue = "2.0ag")
        protected Mass mass;

        /** Option for Momentum with a default value string. */
        @Option(names = {"--momentum"}, description = "Momentum", defaultValue = "2.0kgm/s")
        protected Momentum momentum;

        /** Option for Power with a default value string. */
        @Option(names = {"--power"}, description = "Power", defaultValue = "2.0aW")
        protected Power power;

        /** Option for Pressure with a default value string. */
        @Option(names = {"--pressure"}, description = "Pressure", defaultValue = "2.0aPa")
        protected Pressure pressure;

        /** Option for RadioActivity with a default value string. */
        @Option(names = {"--radioactivity"}, description = "RadioActivity", defaultValue = "2.0aBq")
        protected RadioActivity radioactivity;

        /** Option for SolidAngle with a default value string. */
        @Option(names = {"--solidangle"}, description = "SolidAngle", defaultValue = "2.0sq.deg")
        protected SolidAngle solidangle;

        /** Option for Speed with a default value string. */
        @Option(names = {"--speed"}, description = "Speed", defaultValue = "2.0ft/h")
        protected Speed speed;

        /** Option for Temperature with a default value string. */
        @Option(names = {"--temperature"}, description = "Temperature", defaultValue = "2.0aK")
        protected Temperature temperature;

        /** Option for Torque with a default value string. */
        @Option(names = {"--torque"}, description = "Torque", defaultValue = "2.0lbf.ft")
        protected Torque torque;

        /** Option for Volume with a default value string. */
        @Option(names = {"--volume"}, description = "Volume", defaultValue = "2.0cm3")
        protected Volume volume;

        /** Option for VolumetricObjectDensity with a default value string. */
        @Option(names = {"--volumetricobjectdensity"}, description = "VolumetricObjectDensity", defaultValue = "2.0/m3")
        protected VolumetricObjectDensity volumetricobjectdensity;
    }

    /**
     * Tests CLI parsing for all quantities: default values and explicit overrides.
     * @throws Exception if CLI execution or parsing fails for any quantity.
     */
    @Test
    public void testCli() throws Exception
    {
        Locale.setDefault(Locale.US);
        String[] args;
        Options options;
        // AbsorbedDose default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(AbsorbedDose.valueOf("2.0erg/g").si(), options.absorbeddose.si(), EPS);
        // AbsorbedDose override
        args = new String[] {"--absorbeddose", "1.0Gy"};
        CliUtil.execute(options, args);
        assertEquals(AbsorbedDose.valueOf("1.0Gy").si(), options.absorbeddose.si(), EPS);

        // Acceleration default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Acceleration.valueOf("2.0ft/s2").si(), options.acceleration.si(), EPS);
        // Acceleration override
        args = new String[] {"--acceleration", "1.0g"};
        CliUtil.execute(options, args);
        assertEquals(Acceleration.valueOf("1.0g").si(), options.acceleration.si(), EPS);

        // AmountOfSubstance default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(AmountOfSubstance.valueOf("2.0amol").si(), options.amountofsubstance.si(), EPS);
        // AmountOfSubstance override
        args = new String[] {"--amountofsubstance", "1.0cmol"};
        CliUtil.execute(options, args);
        assertEquals(AmountOfSubstance.valueOf("1.0cmol").si(), options.amountofsubstance.si(), EPS);

        // Angle default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Angle.valueOf("2.0%").si(), options.angle.si(), EPS);
        // Angle override
        args = new String[] {"--angle", "1.0arcmin"};
        CliUtil.execute(options, args);
        assertEquals(Angle.valueOf("1.0arcmin").si(), options.angle.si(), EPS);

        // AngularAcceleration default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(AngularAcceleration.valueOf("2.0arcmin/s2").si(), options.angularacceleration.si(), EPS);
        // AngularAcceleration override
        args = new String[] {"--angularacceleration", "1.0arcsec/s2"};
        CliUtil.execute(options, args);
        assertEquals(AngularAcceleration.valueOf("1.0arcsec/s2").si(), options.angularacceleration.si(), EPS);

        // AngularVelocity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(AngularVelocity.valueOf("2.0arcmin/s").si(), options.angularvelocity.si(), EPS);
        // AngularVelocity override
        args = new String[] {"--angularvelocity", "1.0arcsec/s"};
        CliUtil.execute(options, args);
        assertEquals(AngularVelocity.valueOf("1.0arcsec/s").si(), options.angularvelocity.si(), EPS);

        // Area default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Area.valueOf("2.0a").si(), options.area.si(), EPS);
        // Area override
        args = new String[] {"--area", "1.0ac"};
        CliUtil.execute(options, args);
        assertEquals(Area.valueOf("1.0ac").si(), options.area.si(), EPS);

        // ArealObjectDensity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ArealObjectDensity.valueOf("2.0/m2").si(), options.arealobjectdensity.si(), EPS);
        // ArealObjectDensity override
        args = new String[] {"--arealobjectdensity", "1.0/m2"};
        CliUtil.execute(options, args);
        assertEquals(ArealObjectDensity.valueOf("1.0/m2").si(), options.arealobjectdensity.si(), EPS);

        // CatalyticActivity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(CatalyticActivity.valueOf("2.0akat").si(), options.catalyticactivity.si(), EPS);
        // CatalyticActivity override
        args = new String[] {"--catalyticactivity", "1.0ckat"};
        CliUtil.execute(options, args);
        assertEquals(CatalyticActivity.valueOf("1.0ckat").si(), options.catalyticactivity.si(), EPS);

        // Density default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Density.valueOf("2.0g/cm3").si(), options.density.si(), EPS);
        // Density override
        args = new String[] {"--density", "1.0kg/m3"};
        CliUtil.execute(options, args);
        assertEquals(Density.valueOf("1.0kg/m3").si(), options.density.si(), EPS);

        // Dimensionless default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Dimensionless.valueOf("2.0 ").si(), options.dimensionless.si(), EPS);
        // Dimensionless override
        args = new String[] {"--dimensionless", "1.0 "};
        CliUtil.execute(options, args);
        assertEquals(Dimensionless.valueOf("1.0 ").si(), options.dimensionless.si(), EPS);

        // Duration default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Duration.valueOf("2.0as").si(), options.duration.si(), EPS);
        // Duration override
        args = new String[] {"--duration", "1.0cs"};
        CliUtil.execute(options, args);
        assertEquals(Duration.valueOf("1.0cs").si(), options.duration.si(), EPS);

        // ElectricalCapacitance default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricalCapacitance.valueOf("2.0aF").si(), options.electricalcapacitance.si(), EPS);
        // ElectricalCapacitance override
        args = new String[] {"--electricalcapacitance", "1.0cF"};
        CliUtil.execute(options, args);
        assertEquals(ElectricalCapacitance.valueOf("1.0cF").si(), options.electricalcapacitance.si(), EPS);

        // ElectricalConductance default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricalConductance.valueOf("2.0aS").si(), options.electricalconductance.si(), EPS);
        // ElectricalConductance override
        args = new String[] {"--electricalconductance", "1.0cS"};
        CliUtil.execute(options, args);
        assertEquals(ElectricalConductance.valueOf("1.0cS").si(), options.electricalconductance.si(), EPS);

        // ElectricalInductance default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricalInductance.valueOf("2.0aH").si(), options.electricalinductance.si(), EPS);
        // ElectricalInductance override
        args = new String[] {"--electricalinductance", "1.0cH"};
        CliUtil.execute(options, args);
        assertEquals(ElectricalInductance.valueOf("1.0cH").si(), options.electricalinductance.si(), EPS);

        // ElectricalResistance default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricalResistance.valueOf("2.0abohm").si(), options.electricalresistance.si(), EPS);
        // ElectricalResistance override
        args = new String[] {"--electricalresistance", "1.0aohm"};
        CliUtil.execute(options, args);
        assertEquals(ElectricalResistance.valueOf("1.0aohm").si(), options.electricalresistance.si(), EPS);

        // ElectricCharge default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricCharge.valueOf("2.0abC").si(), options.electriccharge.si(), EPS);
        // ElectricCharge override
        args = new String[] {"--electriccharge", "1.0aC"};
        CliUtil.execute(options, args);
        assertEquals(ElectricCharge.valueOf("1.0aC").si(), options.electriccharge.si(), EPS);

        // ElectricCurrent default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricCurrent.valueOf("2.0A").si(), options.electriccurrent.si(), EPS);
        // ElectricCurrent override
        args = new String[] {"--electriccurrent", "1.0aA"};
        CliUtil.execute(options, args);
        assertEquals(ElectricCurrent.valueOf("1.0aA").si(), options.electriccurrent.si(), EPS);

        // ElectricPotential default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(ElectricPotential.valueOf("2.0abV").si(), options.electricpotential.si(), EPS);
        // ElectricPotential override
        args = new String[] {"--electricpotential", "1.0aV"};
        CliUtil.execute(options, args);
        assertEquals(ElectricPotential.valueOf("1.0aV").si(), options.electricpotential.si(), EPS);

        // Energy default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Energy.valueOf("2.0aJ").si(), options.energy.si(), EPS);
        // Energy override
        args = new String[] {"--energy", "1.0BTU(ISO)"};
        CliUtil.execute(options, args);
        assertEquals(Energy.valueOf("1.0BTU(ISO)").si(), options.energy.si(), EPS);

        // EquivalentDose default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(EquivalentDose.valueOf("2.0aSv").si(), options.equivalentdose.si(), EPS);
        // EquivalentDose override
        args = new String[] {"--equivalentdose", "1.0cSv"};
        CliUtil.execute(options, args);
        assertEquals(EquivalentDose.valueOf("1.0cSv").si(), options.equivalentdose.si(), EPS);

        // FlowMass default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(FlowMass.valueOf("2.0kg/s").si(), options.flowmass.si(), EPS);
        // FlowMass override
        args = new String[] {"--flowmass", "1.0lb/s"};
        CliUtil.execute(options, args);
        assertEquals(FlowMass.valueOf("1.0lb/s").si(), options.flowmass.si(), EPS);

        // FlowVolume default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(FlowVolume.valueOf("2.0ft3/min").si(), options.flowvolume.si(), EPS);
        // FlowVolume override
        args = new String[] {"--flowvolume", "1.0ft3/s"};
        CliUtil.execute(options, args);
        assertEquals(FlowVolume.valueOf("1.0ft3/s").si(), options.flowvolume.si(), EPS);

        // Force default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Force.valueOf("2.0aN").si(), options.force.si(), EPS);
        // Force override
        args = new String[] {"--force", "1.0cN"};
        CliUtil.execute(options, args);
        assertEquals(Force.valueOf("1.0cN").si(), options.force.si(), EPS);

        // Frequency default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Frequency.valueOf("2.0aHz").si(), options.frequency.si(), EPS);
        // Frequency override
        args = new String[] {"--frequency", "1.0cHz"};
        CliUtil.execute(options, args);
        assertEquals(Frequency.valueOf("1.0cHz").si(), options.frequency.si(), EPS);

        // Illuminance default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Illuminance.valueOf("2.0alx").si(), options.illuminance.si(), EPS);
        // Illuminance override
        args = new String[] {"--illuminance", "1.0clx"};
        CliUtil.execute(options, args);
        assertEquals(Illuminance.valueOf("1.0clx").si(), options.illuminance.si(), EPS);

        // Length default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Length.valueOf("2.0A").si(), options.length.si(), EPS);
        // Length override
        args = new String[] {"--length", "1.0am"};
        CliUtil.execute(options, args);
        assertEquals(Length.valueOf("1.0am").si(), options.length.si(), EPS);

        // LinearDensity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(LinearDensity.valueOf("2.0kg/m").si(), options.lineardensity.si(), EPS);
        // LinearDensity override
        args = new String[] {"--lineardensity", "1.0kg/m"};
        CliUtil.execute(options, args);
        assertEquals(LinearDensity.valueOf("1.0kg/m").si(), options.lineardensity.si(), EPS);

        // LinearObjectDensity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(LinearObjectDensity.valueOf("2.0/am").si(), options.linearobjectdensity.si(), EPS);
        // LinearObjectDensity override
        args = new String[] {"--linearobjectdensity", "1.0/cm"};
        CliUtil.execute(options, args);
        assertEquals(LinearObjectDensity.valueOf("1.0/cm").si(), options.linearobjectdensity.si(), EPS);

        // LuminousFlux default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(LuminousFlux.valueOf("2.0lm").si(), options.luminousflux.si(), EPS);
        // LuminousFlux override
        args = new String[] {"--luminousflux", "1.0lm"};
        CliUtil.execute(options, args);
        assertEquals(LuminousFlux.valueOf("1.0lm").si(), options.luminousflux.si(), EPS);

        // LuminousIntensity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(LuminousIntensity.valueOf("2.0acd").si(), options.luminousintensity.si(), EPS);
        // LuminousIntensity override
        args = new String[] {"--luminousintensity", "1.0ccd"};
        CliUtil.execute(options, args);
        assertEquals(LuminousIntensity.valueOf("1.0ccd").si(), options.luminousintensity.si(), EPS);

        // MagneticFlux default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(MagneticFlux.valueOf("2.0aWb").si(), options.magneticflux.si(), EPS);
        // MagneticFlux override
        args = new String[] {"--magneticflux", "1.0cWb"};
        CliUtil.execute(options, args);
        assertEquals(MagneticFlux.valueOf("1.0cWb").si(), options.magneticflux.si(), EPS);

        // MagneticFluxDensity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(MagneticFluxDensity.valueOf("2.0aT").si(), options.magneticfluxdensity.si(), EPS);
        // MagneticFluxDensity override
        args = new String[] {"--magneticfluxdensity", "1.0cT"};
        CliUtil.execute(options, args);
        assertEquals(MagneticFluxDensity.valueOf("1.0cT").si(), options.magneticfluxdensity.si(), EPS);

        // Mass default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Mass.valueOf("2.0ag").si(), options.mass.si(), EPS);
        // Mass override
        args = new String[] {"--mass", "1.0cg"};
        CliUtil.execute(options, args);
        assertEquals(Mass.valueOf("1.0cg").si(), options.mass.si(), EPS);

        // Momentum default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Momentum.valueOf("2.0kgm/s").si(), options.momentum.si(), EPS);
        // Momentum override
        args = new String[] {"--momentum", "1.0kgm/s"};
        CliUtil.execute(options, args);
        assertEquals(Momentum.valueOf("1.0kgm/s").si(), options.momentum.si(), EPS);

        // Power default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Power.valueOf("2.0aW").si(), options.power.si(), EPS);
        // Power override
        args = new String[] {"--power", "1.0cW"};
        CliUtil.execute(options, args);
        assertEquals(Power.valueOf("1.0cW").si(), options.power.si(), EPS);

        // Pressure default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Pressure.valueOf("2.0aPa").si(), options.pressure.si(), EPS);
        // Pressure override
        args = new String[] {"--pressure", "1.0at"};
        CliUtil.execute(options, args);
        assertEquals(Pressure.valueOf("1.0at").si(), options.pressure.si(), EPS);

        // RadioActivity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(RadioActivity.valueOf("2.0aBq").si(), options.radioactivity.si(), EPS);
        // RadioActivity override
        args = new String[] {"--radioactivity", "1.0Bq"};
        CliUtil.execute(options, args);
        assertEquals(RadioActivity.valueOf("1.0Bq").si(), options.radioactivity.si(), EPS);

        // SolidAngle default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(SolidAngle.valueOf("2.0sq.deg").si(), options.solidangle.si(), EPS);
        // SolidAngle override
        args = new String[] {"--solidangle", "1.0sr"};
        CliUtil.execute(options, args);
        assertEquals(SolidAngle.valueOf("1.0sr").si(), options.solidangle.si(), EPS);

        // Speed default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Speed.valueOf("2.0ft/h").si(), options.speed.si(), EPS);
        // Speed override
        args = new String[] {"--speed", "1.0ft/min"};
        CliUtil.execute(options, args);
        assertEquals(Speed.valueOf("1.0ft/min").si(), options.speed.si(), EPS);

        // Temperature default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Temperature.valueOf("2.0aK").si(), options.temperature.si(), EPS);
        // Temperature override
        args = new String[] {"--temperature", "1.0cK"};
        CliUtil.execute(options, args);
        assertEquals(Temperature.valueOf("1.0cK").si(), options.temperature.si(), EPS);

        // Torque default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Torque.valueOf("2.0lbf.ft").si(), options.torque.si(), EPS);
        // Torque override
        args = new String[] {"--torque", "1.0lbf.in"};
        CliUtil.execute(options, args);
        assertEquals(Torque.valueOf("1.0lbf.in").si(), options.torque.si(), EPS);

        // Volume default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(Volume.valueOf("2.0cm3").si(), options.volume.si(), EPS);
        // Volume override
        args = new String[] {"--volume", "1.0dam3"};
        CliUtil.execute(options, args);
        assertEquals(Volume.valueOf("1.0dam3").si(), options.volume.si(), EPS);

        // VolumetricObjectDensity default
        args = new String[] {};
        options = new Options();
        CliUtil.execute(options, args);
        assertEquals(VolumetricObjectDensity.valueOf("2.0/m3").si(), options.volumetricobjectdensity.si(), EPS);
        // VolumetricObjectDensity override
        args = new String[] {"--volumetricobjectdensity", "1.0/m3"};
        CliUtil.execute(options, args);
        assertEquals(VolumetricObjectDensity.valueOf("1.0/m3").si(), options.volumetricobjectdensity.si(), EPS);

    }
}
