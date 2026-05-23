# Coding of quantities

Quantities are coded with one byte indicating the quantity type, and one byte indicating the display unit. The SI unit or BASE unit always has display type 0. The units for each quantity type are provided on the [following page](../unit-types). The quantities currently defined are:

| code | quantity | description | default (SI) unit | Unit types |
| ------- | ------------- | -------------------- | -------------------- | ------------------ |
| 0 | Dimensionless | Unit without a dimension | | [Units](../unit-types#0-dimensionless)  | 
| 1 | Acceleration | Acceleration | m/s<sup>2</sup>  | [Units](../unit-types#1-acceleration)  | 
| 2 | SolidAngle | Solid angle (steradian) | sr  | [Units](../unit-types#2-anglesolid)  | 
| 3 | Angle | Angle (relative) | rad  | [Units](../unit-types#3-angle)  | 
| 4 | Direction | Angle (absolute) | rad  | [Units](../unit-types#4-direction)  | 
| 5 | Area | Area | m<sup>2</sup>  | [Units](../unit-types#5-area)  | 
| 6 | Density | Density based on mass and length | kg/m<sup>3</sup>  | [Units](../unit-types#6-density)  | 
| 7 | ElectricCharge | Electric charge (Coulomb) | s.A  | [Units](../unit-types#7-electriccharge)  | 
| 8 | ElectricCurrent | Electric current (Ampere) | A  | [Units](../unit-types#8-electriccurrent)  | 
| 9 | ElectricPotential | Electric potential (Volt) | kg.m<sup>2</sup>/s<sup>3</sup>.A  | [Units](../unit-types#9-electricpotential)  | 
| 10 | ElectricalResistance | Electrical resistance (Ohm) | kg.m<sup>2</sup>/s<sup>3</sup>.A<sup>2</sup>  | [Units](../unit-types#10-electricalresistance)  | 
| 11 | Energy | Energy (Joule) | kg.m<sup>2</sup>/s<sup>2</sup>  | [Units](../unit-types#11-energy)  | 
| 12 | FlowMass | Mass flow rate | kg/s  | [Units](../unit-types#12-flowmass)  | 
| 13 | FlowVolume | Volume flow rate | m<sup>3</sup>/s  | [Units](../unit-types#13-flowvolume)  | 
| 14 | Force | Force (Newton) | kg.m/s<sup>2</sup>  | [Units](../unit-types#14-force)  | 
| 15 | Frequency | Frequency (Hz) | 1/s  | [Units](../unit-types#15-frequency)  | 
| 16 | Length | Length (relative) | m  | [Units](../unit-types#16-length)  | 
| 17 | Position | Length (absolute) | m  | [Units](../unit-types#17-position)  | 
| 18 | LinearDensity | Linear density | kg/m  | [Units](../unit-types#18-lineardensity)  | 
| 19 | Mass | Mass | kg  | [Units](../unit-types#19-mass)  | 
| 20 | Power | Power (Watt) | kg.m<sup>2</sup>/s<sup>3</sup>  | [Units](../unit-types#20-power)  | 
| 21 | Pressure | Pressure (Pascal) | kg/m.s<sup>2</sup>  | [Units](../unit-types#21-pressure)  | 
| 22 | Speed | Speed | m/s  | [Units](../unit-types#22-speed)  | 
| 23 | TemperatureDifference | Temperature difference (relative) | K  | [Units](../unit-types#23-temperaturedifference)  | 
| 24 | Temperature | Temperature (absolute) | K  | [Units](../unit-types#24-temperature)  | 
| 25 | Duration | Time (relative) | s  | [Units](../unit-types#25-duration)  | 
| 26 | Time | Time (absolute) | s  | [Units](../unit-types#26-time)  | 
| 27 | Torque | Torque (Newton-meter) | kg.m<sup>2</sup>/s<sup>2</sup>  | [Units](../unit-types#27-torque)  | 
| 28 | Volume | Volume | m<sup>3</sup>  | [Units](../unit-types#28-volume)  | 
| 29 | Absorbed dose | Absorbed dose (gray) | m<sup>2</sup>/s<sup>2</sup>  | [Units](../unit-types#29-absorbeddose)  | 
| 30 | Amount of substance | Amount of substance (mole) | mol  | [Units](../unit-types#30-amountofsubstance)  | 
| 31 | Catalytic activity | Catalytic activity (mole/s) | mol/s  | [Units](../unit-types#31-catalyticactivity)  | 
| 32 | Electrical capacitance | Electrical capacitance (farad) | s<sup>4</sup>.A<sup>2</sup>/kg.m<sup>2</sup>  | [Units](../unit-types#32-electricalcapacitance)  | 
| 33 | Electrical conductance | Electrical conductance (siemens) | s<sup>3</sup>.A<sup>2</sup>/kg.m<sup>2</sup>  | [Units](../unit-types#33-electricalconductance)  | 
| 34 | Electrical inductance | Electrical inductance (henry) | kg.m<sup>2</sup>/s<sup>2</sup>.A<sup>2</sup>  | [Units](../unit-types#34-electricalinductance)  | 
| 35 | Equivalent dose | Equivalent dose (sievert) | m<sup>2</sup>/s<sup>2</sup>  | [Units](../unit-types#35-equivalentdose)  | 
| 36 | Illuminance | Illuminance (lux) | sr.cd/m<sup>2</sup>  | [Units](../unit-types#36-illuminance)  | 
| 37 | Luminous flux | Luminous flux (lumen) | sr.cd  | [Units](../unit-types#37-luminousflux)  | 
| 38 | Luminous intensity | Luminous intensity (candela) | cd  | [Units](../unit-types#38-luminousintensity)  | 
| 39 | Magnetic flux density | Magnetic flux density (tesla) | kg/s<sup>2</sup>.A  | [Units](../unit-types#39-magneticfluxdensity)  | 
| 40 | Magnetic flux | Magnetic flux (weber) | kg.m<sup>2</sup>/s<sup>2</sup>.A  | [Units](../unit-types#40-magneticflux)  | 
| 41 | Radioactivity | Radioactivity (becquerel) | 1/s  | [Units](../unit-types#41-radioactivity)  | 
| 42 | Angular acceleration | Change in angular velocity per second | rad/s<sup>2</sup> | [Units](../unit-types#42-angularacceleration) |
| 43 | Angular velocity | Change in angular velocity per second | rad/s | [Units](../unit-types#43-angularvelocity) |
| 44 | Momentum | Linear momentum, translational momentum | kg.m/s | [Units](../unit-types#44-momentum) |
| 45 | LinearObjectDensity | Number of objects per length | /m | [Units](../unit-types#45-linearobjectdensity) |
| 46 | ArealObjectDensity | Number of objects per area | /m<sup>2</sup> | [Units](../unit-types#46-arealobjectdensity) |
| 47 | VolumetricObjectDensity | Number of objects per volume | /m<sup>3</sup> | [Units](../unit-types#47-volumetricobjectdensity) |

Some of the quantity types have a relative and an absolute variant. Relative quantities can be added to or subtracted from relative and absolute quantities; absolute quantities cannot be added, but can be subtracted, resulting in a relative quantity. As an example, one cannot add two times (3-1-2017, 5 o'clock + 3-1-2017, 3 o'clock = ??), but these values can be subtracted (3-1-2017, 5 o'clock – 3-1-2017, 3 o'clock = 2 hours). Absolute plus relative yields e.g., 3-1-2017, 17:00 + 2 hours = 3-1-2017, 19:00. Relative values can of course be added/subtracted: 2 hours + 30 minutes = 2.5 hours. See [https://djunits.org](https://djunits.org) for more information.
