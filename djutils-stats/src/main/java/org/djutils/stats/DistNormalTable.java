package org.djutils.stats;

import org.djutils.exceptions.Throw;

/**
 * The Normal distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/NormalDistribution.html"> http://mathworld.wolfram.com/NormalDistribution.html </a>
 * <p>
 * Copyright (c) 2002-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class DistNormalTable
{
    /**
     * Utility class should never be constructed.
     */
    private DistNormalTable()
    {
        // Do not instantiate
    }

    /**
     * Returns the cumulative probability of the x-value.
     * @param mu mean value
     * @param sigma standard deviation
     * @param x the observation x
     * @return double the cumulative probability
     * @throws IllegalArgumentException when sigma less than 0
     */
    public static double getCumulativeProbability(final double mu, final double sigma, final double x)
    {
        Throw.when(sigma < 0.0, IllegalArgumentException.class, "sigma cannot be < 0");
        int z = (int) Math.rint((x - mu) / sigma * 100);
        int absZ = Math.abs(z);
        if (absZ > 1000)
        {
            absZ = 1000;
        }
        if (z >= 0)
        {
            return DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[absZ];
        }
        if (absZ < 0) // This is actually possible, e.g. happens when sigma == 0 and x = mu - Math.ulp(mu)
        {
            absZ = 1000;
        }
        return 1 - DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[absZ];
    }

    // TODO ensure the table has decent values above 6 sigma.
    // TODO switch to a different (new) table for values way above 6 sigma.

    /**
     * Returns the x-value of the given cumulativePropability. Result range is limited to approximately 6 sigma.
     * @param mu mean value
     * @param sigma standard deviation
     * @param cumulativeProbability the cumulative probability
     * @return double the x-value that corresponds closely to the given cumulative probability
     * @throws IllegalArgumentException when sigma less than 0, or when cumulative probability not between 0 and 1 (inclusive)
     */
    public static double getInverseCumulativeProbability(final double mu, final double sigma,
            final double cumulativeProbability)
    {
        Throw.when(sigma < 0.0, IllegalArgumentException.class, "sigma cannot be < 0");
        Throw.when(cumulativeProbability < 0 || cumulativeProbability > 1, IllegalArgumentException.class,
                "cumulativeProbability should be between 0 and 1 (inclusive)");
        if (cumulativeProbability < 0.5)
        {
            return mu - getInverseCumulativeProbability(0, 1, 1.0 - cumulativeProbability) * sigma;
        }
        Throw.when(Double.isNaN(mu) || Double.isNaN(sigma), IllegalArgumentException.class, "my and sigma may not be NaN");
        double prob = cumulativeProbability;
        int tableSize = DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES.length - 1;
        int pivot = tableSize / 2;
        int stepSize = tableSize / 2;
        while (DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[pivot] > prob
                || DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[pivot + 1] < prob)
        {
            stepSize = (stepSize + 1) / 2;
            if (DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[pivot] > prob)
            {
                pivot -= stepSize;
            }
            else
            {
                pivot = Math.min(tableSize - 1, pivot + stepSize);
            }
        }
        // Linearly interpolate between pivot and pivot + 1.
        double interpolatedPivot = pivot;
        if (pivot < tableSize)
        {
            double pivotFraction = (prob - DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[pivot])
                    / (DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[pivot + 1]
                            - DistNormalTable.CUMULATIVE_NORMAL_PROBABILITIES[pivot]);
            if (Double.isFinite(pivotFraction))
            {
                interpolatedPivot += pivotFraction;
            }
        }
        return mu + CUMULATIVE_NORMAL_PROBABILITIES_TABLE_RANGE * interpolatedPivot / tableSize * sigma;
    }

    /** CUMULATIVE_NORMAL_PROBABILITIES table runs from 0 * sigma to CUMULATIVE_NORMAL_PROBABILITIES_TABLE_RANGE * sigma. */
    public static final double CUMULATIVE_NORMAL_PROBABILITIES_TABLE_RANGE = 10.0;

    /**
     * CUMULATIVE_NORMAL_PROBABILITIES represents the NORMAL DISTRIBUTION FUNCTION TABLE. In order to keep this table as fast as
     * possible no x values are stored. The range of the table is
     * {0.00,0.01,0.02,...,CUMULATIVE_NORMAL_PROBABILITIES_TABLE_RANGE}, but accuracy is severely lacking after 6.00
     */
    // @formatter:off
    public static final double[] CUMULATIVE_NORMAL_PROBABILITIES = { 
            0.5000000000000000, 0.5039893563146316, 0.5079783137169019, 0.5119664734141126, 0.5159534368528308, /* 0.00 - 0.04 */
            0.5199388058383725, 0.5239221826541068, 0.5279031701805211, 0.5318813720139873, 0.5358563925851721, /* 0.05 - 0.09 */
            0.5398278372770290, 0.5437953125423168, 0.5477584260205839, 0.5517167866545611, 0.5556700048059064, /* 0.10 - 0.14 */
            0.5596176923702425, 0.5635594628914329, 0.5674949316750384, 0.5714237159009007, 0.5753454347347955, /* 0.15 - 0.19 */
            0.5792597094391030, 0.5831661634824423, 0.5870644226482146, 0.5909541151420059, 0.5948348716977958, /* 0.20 - 0.24 */
            0.5987063256829237, 0.6025681132017605, 0.6064198731980395, 0.6102612475557972, 0.6140918811988773, /* 0.25 - 0.29 */
            0.6179114221889526, 0.6217195218220193, 0.6255158347233201, 0.6293000189406536, 0.6330717360360281, /* 0.30 - 0.34 */
            0.6368306511756190, 0.6405764332179912, 0.6443087548005468, 0.6480272924241628, 0.6517317265359823, /* 0.35 - 0.39 */
            0.6554217416103242, 0.6590970262276774, 0.6627572731517505, 0.6664021794045425, 0.6700314463394064, /* 0.40 - 0.44 */
            0.6736447797120800, 0.6772418897496524, 0.6808224912174441, 0.6843863034837774, 0.6879330505826095, /* 0.45 - 0.49 */
            0.6914624612740130, 0.6949742691024805, 0.6984682124530338, 0.7019440346051236, 0.7054014837843019, /* 0.50 - 0.54 */
            0.7088403132116536, 0.7122602811509730, 0.7156611509536759, 0.7190426911014356, 0.7224046752465351, /* 0.55 - 0.59 */
            0.7257468822499265, 0.7290690962169943, 0.7323711065310170, 0.7356527078843225, 0.7389137003071385, /* 0.60 - 0.64 */
            0.7421538891941353, 0.7453730853286639, 0.7485711049046899, 0.7517477695464295, 0.7549029063256905, /* 0.65 - 0.69 */
            0.7580363477769270, 0.7611479319100133, 0.7642375022207488, 0.7673049076991025, 0.7703500028352093, /* 0.70 - 0.74 */
            0.7733726476231317, 0.7763727075624005, 0.7793500536573503, 0.7823045624142668, 0.7852361158363629, /* 0.75 - 0.79 */
            0.7881446014166031, 0.7910299121283983, 0.7938919464141868, 0.7967306081719316, 0.7995458067395502, /* 0.80 - 0.84 */
            0.8023374568773076, 0.8051054787481916, 0.8078497978963040, 0.8105703452232880, 0.8132670569628273, /* 0.85 - 0.89 */
            0.8159398746532404, 0.8185887451082028, 0.8212136203856282, 0.8238144577547422, 0.8263912196613754, /* 0.90 - 0.94 */
            0.8289438736915181, 0.8314723925331622, 0.8339767539364704, 0.8364569406723077, 0.8389129404891691, /* 0.95 - 0.99 */
            0.8413447460685430, 0.8437523549787456, 0.8461357696272651, 0.8484949972116563, 0.8508300496690185, /* 1.00 - 1.04 */
            0.8531409436241040, 0.8554277003360904, 0.8576903456440608, 0.8599289099112310, 0.8621434279679645, /* 1.05 - 1.09 */
            0.8643339390536173, 0.8665004867572528, 0.8686431189572692, 0.8707618877599823, 0.8728568494372015, /* 1.10 - 1.14 */
            0.8749280643628496, 0.8769755969486566, 0.8789995155789816, 0.8809998925447993, 0.8829768039768913, /* 1.15 - 1.19 */
            0.8849303297782918, 0.8868605535560226, 0.8887675625521654, 0.8906514475743081, 0.8925123029254132, /* 1.20 - 1.24 */
            0.8943502263331446, 0.8961653188786995, 0.8979576849251809, 0.8997274320455578, 0.9014746709502524, /* 1.25 - 1.29 */
            0.9031995154143897, 0.9049020822047611, 0.9065824910065281, 0.9082408643497192, 0.9098773275355476, /* 1.30 - 1.34 */
            0.9114920085625982, 0.9130850380529150, 0.9146565491780330, 0.9162066775849856, 0.9177355613223310, /* 1.35 - 1.39 */
            0.9192433407662289, 0.9207301585466074, 0.9221961594734538, 0.9236414904632609, 0.9250663004656731, /* 1.40 - 1.44 */
            0.9264707403903514, 0.9278549630341062, 0.9292191230083147, 0.9305633766666681, 0.9318878820332746, /* 1.45 - 1.49 */
            0.9331927987311419, 0.9344782879110833, 0.9357445121810641, 0.9369916355360215, 0.9382198232881882, /* 1.50 - 1.54 */
            0.9394292419979409, 0.9406200594052068, 0.9417924443614469, 0.9429465667622456, 0.9440825974805306, /* 1.55 - 1.59 */
            0.9452007083004421, 0.9463010718518804, 0.9473838615457479, 0.9484492515099107, 0.9494974165258964, /* 1.60 - 1.64 */
            0.9505285319663519, 0.9515427737332771, 0.9525403181970525, 0.9535213421362800, 0.9544860226784503, /* 1.65 - 1.69 */
            0.9554345372414568, 0.9563670634759682, 0.9572837792086711, 0.9581848623864051, 0.9590704910211927, /* 1.70 - 1.74 */
            0.9599408431361829, 0.9607960967125174, 0.9616364296371288, 0.9624620196514834, 0.9632730443012738, /* 1.75 - 1.79 */
            0.9640696808870743, 0.9648521064159611, 0.9656204975541101, 0.9663750305803716, 0.9671158813408361, /* 1.80 - 1.84 */
            0.9678432252043865, 0.9685572370192472, 0.9692580910705340, 0.9699459610388002, 0.9706210199595906, /* 1.85 - 1.89 */
            0.9712834401839983, 0.9719333933402277, 0.9725710502961632, 0.9731965811229450, 0.9738101550595473, /* 1.90 - 1.94 */
            0.9744119404783614, 0.9750021048517796, 0.9755808147197775, 0.9761482356584914, 0.9767045322497883, /* 1.95 - 1.99 */
            0.9772498680518209, 0.9777844055705684, 0.9783083062323533, 0.9788217303573277, 0.9793248371339299, /* 2.00 - 2.04 */
            0.9798177845942956, 0.9803007295906232, 0.9807738277724828, 0.9812372335650623, 0.9816911001483410, /* 2.05 - 2.09 */
            0.9821355794371835, 0.9825708220623429, 0.9829969773523672, 0.9834141933163950, 0.9838226166278339, /* 2.10 - 2.14 */
            0.9842223926089095, 0.9846136652160746, 0.9849965770262679, 0.9853712692240107, 0.9857378815893312, /* 2.15 - 2.19 */
            0.9860965524865013, 0.9864474188535800, 0.9867906161927438, 0.9871262785613980, 0.9874545385640534, /* 2.20 - 2.24 */
            0.9877755273449553, 0.9880893745814530, 0.9883962084780965, 0.9886961557614472, 0.9889893416755886, /* 2.25 - 2.29 */
            0.9892758899783243, 0.9895559229380490, 0.9898295613312803, 0.9900969244408357, 0.9903581300546417, /* 2.30 - 2.34 */
            0.9906132944651614, 0.9908625324694273, 0.9911059573696632, 0.9913436809744834, 0.9915758136006543, /* 2.35 - 2.39 */
            0.9918024640754040, 0.9920237397392663, 0.9922397464494463, 0.9924505885836907, 0.9926563690446517, /* 2.40 - 2.44 */
            0.9928571892647287, 0.9930531492113757, 0.9932443473928594, 0.9934308808644532, 0.9936128452350568, /* 2.45 - 2.49 */
            0.9937903346742238, 0.9939634419195873, 0.9941322582846674, 0.9942968736670492, 0.9944573765569173, /* 2.50 - 2.54 */
            0.9946138540459333, 0.9947663918364442, 0.9949150742510089, 0.9950599842422294, 0.9952012034028739, /* 2.55 - 2.59 */
            0.9953388119762813, 0.9954728888670327, 0.9956035116518787, 0.9957307565909106, 0.9958546986389640, /* 2.60 - 2.64 */
            0.9959754114572417, 0.9960929674251472, 0.9962074376523145, 0.9963188919908250, 0.9964273990476002, /* 2.65 - 2.69 */
            0.9965330261969594, 0.9966358395933308, 0.9967359041841086, 0.9968332837226421, 0.9969280407813494, /* 2.70 - 2.74 */
            0.9970202367649454, 0.9971099319237738, 0.9971971853672350, 0.9972820550772987, 0.9973645979220951, /* 2.75 - 2.79 */
            0.9974448696695721, 0.9975229250012141, 0.9975988175258108, 0.9976725997932685, 0.9977443233084577, /* 2.80 - 2.84 */
            0.9978140385450868, 0.9978817949595954, 0.9979476410050603, 0.9980116241451057, 0.9980737908678121, /* 2.85 - 2.89 */
            0.9981341866996160, 0.9981928562191935, 0.9982498430713239, 0.9983051899807227, 0.9983589387658430, /* 2.90 - 2.94 */
            0.9984111303526351, 0.9984618047882620, 0.9985110012547626, 0.9985587580826600, 0.9986051127645077, /* 2.95 - 2.99 */
            0.9986501019683699, 0.9986937615512306, 0.9987361265723277, 0.9987772313064077, 0.9988171092568956, /* 3.00 - 3.04 */
            0.9988557931689773, 0.9988933150425907, 0.9989297061453211, 0.9989649970251971, 0.9989992175233859, /* 3.05 - 3.09 */
            0.9990323967867816, 0.9990645632804859, 0.9990957448001776, 0.9991259684843684, 0.9991552608265414, /* 3.10 - 3.14 */
            0.9991836476871714, 0.9992111543056243, 0.9992378053119327, 0.9992636247384461, 0.9992886360313546, /* 3.15 - 3.19 */
            0.9993128620620841, 0.9993363251385601, 0.9993590470163399, 0.9993810489096131, 0.9994023515020656, /* 3.20 - 3.24 */
            0.9994229749576092, 0.9994429389309754, 0.9994622625781703, 0.9994809645667930, 0.9994990630862143, /* 3.25 - 3.29 */
            0.9995165758576162, 0.9995335201438924, 0.9995499127594079, 0.9995657700796183, 0.9995811080505497, /* 3.30 - 3.34 */
            0.9995959421981360, 0.9996102876374180, 0.9996241590816000, 0.9996375708509669, 0.9996505368816620, /* 3.35 - 3.39 */
            0.9996630707343231, 0.9996751856025812, 0.9996868943214188, 0.9996982093753914, 0.9997091429067093, /* 3.40 - 3.44 */
            0.9997197067231838, 0.9997299123060366, 0.9997397708175726, 0.9997492931087195, 0.9997584897264322, /* 3.45 - 3.49 */
            0.9997673709209645, 0.9997759466530090, 0.9997842266007053, 0.9997922201665194, 0.9997999364839927, /* 3.50 - 3.54 */
            0.9998073844243643, 0.9998145726030667, 0.9998215093860952, 0.9998282028962541, 0.9998346610192799, /* 3.55 - 3.59 */
            0.9998408914098424, 0.9998469014974263, 0.9998526984920926, 0.9998582893901242, 0.9998636809795542, /* 3.60 - 3.64 */
            0.9998688798455795, 0.9998738923758614, 0.9998787247657146, 0.9998833830231846, 0.9998878729740177, /* 3.65 - 3.69 */
            0.9998922002665226, 0.9998963703763260, 0.9999003886110240, 0.9999042601147311, 0.9999079898725258, /* 3.70 - 3.74 */
            0.9999115827147992, 0.9999150433215020, 0.9999183762262973, 0.9999215858206164, 0.9999246763576213, /* 3.75 - 3.79 */
            0.9999276519560749, 0.9999305166041201, 0.9999332741629703, 0.9999359283705112, 0.9999384828448168, /* 3.80 - 3.84 */
            0.9999409410875810, 0.9999433064874658, 0.9999455823233663, 0.9999477717675982, 0.9999498778890038, /* 3.85 - 3.89 */
            0.9999519036559824, 0.9999538519394437, 0.9999557255156879, 0.9999575270692113, 0.9999592591954414, /* 3.90 - 3.94 */
            0.9999609244034022, 0.9999625251183089, 0.9999640636840972, 0.9999655423658850, 0.9999669633523707, /* 3.95 - 3.99 */
            0.9999683287581669, 0.9999696406260734, 0.9999709009292881, 0.9999721115735594, 0.9999732743992805, /* 4.00 - 4.04 */
            0.9999743911835259, 0.9999754636420336, 0.9999764934311315, 0.9999774821496115, 0.9999784313405518, /* 4.05 - 4.09 */
            0.9999793424930874, 0.9999802170441318, 0.9999810563800495, 0.9999818618382819, 0.9999826347089265, /* 4.10 - 4.14 */
            0.9999833762362704, 0.9999840876202809, 0.9999847700180520, 0.9999854245452091, 0.9999860522772731, /* 4.15 - 4.19 */
            0.9999866542509841, 0.9999872314655862, 0.9999877848840748, 0.9999883154344054, 0.9999888240106678, /* 4.20 - 4.24 */
            0.9999893114742251, 0.9999897786548160, 0.9999902263516272, 0.9999906553343298, 0.9999910663440872, /* 4.25 - 4.29 */
            0.9999914600945290, 0.9999918372726972, 0.9999921985399619, 0.9999925445329086, 0.9999928758641985, /* 4.30 - 4.34 */
            0.9999931931234007, 0.9999934968777990, 0.9999937876731730, 0.9999940660345543, 0.9999943324669582, /* 4.35 - 4.39 */
            0.9999945874560923, 0.9999948314690428, 0.9999950649549374, 0.9999952883455880, 0.9999955020561114, /* 4.40 - 4.44 */
            0.9999957064855300, 0.9999959020173534, 0.9999960890201397, 0.9999962678480394, 0.9999964388413204, /* 4.45 - 4.49 */
            0.9999966023268753, 0.9999967586187126, 0.9999969080184310, 0.9999970508156771, 0.9999971872885882, /* 4.50 - 4.54 */
            0.9999973177042203, 0.9999974423189606, 0.9999975613789263, 0.9999976751203501, 0.9999977837699519, /* 4.55 - 4.59 */
            0.9999978875452975, 0.9999979866551452, 0.9999980812997800, 0.9999981716713364, 0.9999982579541097, /* 4.60 - 4.64 */
            0.9999983403248556, 0.9999984189530811, 0.9999984940013225, 0.9999985656254156, 0.9999986339747554, /* 4.65 - 4.69 */
            0.9999986991925460, 0.9999987614160426, 0.9999988207767835, 0.9999988774008146, 0.9999989314089055, /* 4.70 - 4.74 */
            0.9999989829167575, 0.9999990320352039, 0.9999990788704038, 0.9999991235240271, 0.9999991660934341, /* 4.75 - 4.79 */
            0.9999992066718480, 0.9999992453485209, 0.9999992822088930, 0.9999993173347475, 0.9999993508043572, /* 4.80 - 4.84 */
            0.9999993826926280, 0.9999994130712355, 0.9999994420087568, 0.9999994695707970, 0.9999994958201117, /* 4.85 - 4.89 */
            0.9999995208167234, 0.9999995446180352, 0.9999995672789381, 0.9999995888519162, 0.9999996093871457, /* 4.90 - 4.94 */
            0.9999996289325921, 0.9999996475341018, 0.9999996652354917, 0.9999996820786339, 0.9999996981035375, /* 4.95 - 4.99 */
            0.9999997133484282, 0.9999997278498227, 0.9999997416426023, 0.9999997547600820, 0.9999997672340770, /* 5.00 - 5.04 */
            0.9999997790949677, 0.9999997903717610, 0.9999998010921489, 0.9999998112825659, 0.9999998209682428, /* 5.05 - 5.09 */
            0.9999998301732593, 0.9999998389205939, 0.9999998472321717, 0.9999998551289107, 0.9999998626307656, /* 5.10 - 5.14 */
            0.9999998697567705, 0.9999998765250788, 0.9999998829530026, 0.9999998890570498, 0.9999998948529597, /* 5.15 - 5.19 */
            0.9999999003557368, 0.9999999055796842, 0.9999999105384346, 0.9999999152449801, 0.9999999197117013, /* 5.20 - 5.24 */
            0.9999999239503948, 0.9999999279722992, 0.9999999317881205, 0.9999999354080568, 0.9999999388418200, /* 5.25 - 5.29 */
            0.9999999420986596, 0.9999999451873824, 0.9999999481163739, 0.9999999508936166, 0.9999999535267092, /* 5.30 - 5.34 */
            0.9999999560228840, 0.9999999583890240, 0.9999999606316790, 0.9999999627570806, 0.9999999647711582, /* 5.35 - 5.39 */
            0.9999999666795515, 0.9999999684876255, 0.9999999702004823, 0.9999999718229740, 0.9999999733597145, /* 5.40 - 5.44 */
            0.9999999748150900, 0.9999999761932709, 0.9999999774982211, 0.9999999787337082, 0.9999999799033128, /* 5.45 - 5.49 */
            0.9999999810104375, 0.9999999820583151, 0.9999999830500168, 0.9999999839884606, 0.9999999848764176, /* 5.50 - 5.54 */
            0.9999999857165200, 0.9999999865112674, 0.9999999872630332, 0.9999999879740707, 0.9999999886465194, /* 5.55 - 5.59 */
            0.9999999892824097, 0.9999999898836692, 0.9999999904521271, 0.9999999909895190, 0.9999999914974917, /* 5.60 - 5.64 */
            0.9999999919776081, 0.9999999924313503, 0.9999999928601240, 0.9999999932652629, 0.9999999936480314, /* 5.65 - 5.69 */
            0.9999999940096286, 0.9999999943511912, 0.9999999946737974, 0.9999999949784683, 0.9999999952661724, /* 5.70 - 5.74 */
            0.9999999955378276, 0.9999999957943032, 0.9999999960364234, 0.9999999962649687, 0.9999999964806787, /* 5.75 - 5.79 */
            0.9999999966842541, 0.9999999968763579, 0.9999999970576186, 0.9999999972286312, 0.9999999973899589, /* 5.80 - 5.84 */
            0.9999999975421350, 0.9999999976856642, 0.9999999978210243, 0.9999999979486676, 0.9999999980690220, /* 5.85 - 5.89 */
            0.9999999981824921, 0.9999999982894614, 0.9999999983902919, 0.9999999984853266, 0.9999999985748896, /* 5.90 - 5.94 */
            0.9999999986592876, 0.9999999987388104, 0.9999999988137320, 0.9999999988843119, 0.9999999989507948, /* 5.95 - 5.99 */
            0.9999999990134123, 0.9999999990723833, 0.9999999991279147, 0.9999999991802015, 0.9999999992294288, /* 6.00 - 6.04 */
            0.9999999992757709, 0.9999999993193922, 0.9999999993604487, 0.9999999993990872, 0.9999999994354465, /* 6.05 - 6.09 */
            0.9999999994696577, 0.9999999995018443, 0.9999999995321232, 0.9999999995606046, 0.9999999995873926, /* 6.10 - 6.14 */
            0.9999999996125852, 0.9999999996362753, 0.9999999996585500, 0.9999999996794919, 0.9999999996991789, /* 6.15 - 6.19 */
            0.9999999997176843, 0.9999999997350770, 0.9999999997514226, 0.9999999997667823, 0.9999999997812146, /* 6.20 - 6.24 */
            0.9999999997947737, 0.9999999998075113, 0.9999999998194760, 0.9999999998307134, 0.9999999998412670, /* 6.25 - 6.29 */
            0.9999999998511772, 0.9999999998604823, 0.9999999998692184, 0.9999999998774194, 0.9999999998851175, /* 6.30 - 6.34 */
            0.9999999998923426, 0.9999999998991231, 0.9999999999054858, 0.9999999999114559, 0.9999999999170571, /* 6.35 - 6.39 */
            0.9999999999223115, 0.9999999999272402, 0.9999999999318628, 0.9999999999361980, 0.9999999999402632, /* 6.40 - 6.44 */
            0.9999999999440750, 0.9999999999476485, 0.9999999999509985, 0.9999999999541387, 0.9999999999570819, /* 6.45 - 6.49 */
            0.9999999999598400, 0.9999999999624246, 0.9999999999648463, 0.9999999999671152, 0.9999999999692406, /* 6.50 - 6.54 */
            0.9999999999712315, 0.9999999999730962, 0.9999999999748423, 0.9999999999764776, 0.9999999999780087, /* 6.55 - 6.59 */
            0.9999999999794421, 0.9999999999807840, 0.9999999999820400, 0.9999999999832156, 0.9999999999843159, /* 6.60 - 6.64 */
            0.9999999999853453, 0.9999999999863086, 0.9999999999872098, 0.9999999999880529, 0.9999999999888415, /* 6.65 - 6.69 */
            0.9999999999895790, 0.9999999999902688, 0.9999999999909137, 0.9999999999915168, 0.9999999999920807, /* 6.70 - 6.74 */
            0.9999999999926077, 0.9999999999931004, 0.9999999999935609, 0.9999999999939913, 0.9999999999943934, /* 6.75 - 6.79 */
            0.9999999999947691, 0.9999999999951201, 0.9999999999954480, 0.9999999999957543, 0.9999999999960403, /* 6.80 - 6.84 */
            0.9999999999963075, 0.9999999999965570, 0.9999999999967899, 0.9999999999970074, 0.9999999999972105, /* 6.85 - 6.89 */
            0.9999999999973999, 0.9999999999975767, 0.9999999999977418, 0.9999999999978958, 0.9999999999980396, /* 6.90 - 6.94 */
            0.9999999999981736, 0.9999999999982987, 0.9999999999984153, 0.9999999999985241, 0.9999999999986255, /* 6.95 - 6.99 */
            0.9999999999987201, 0.9999999999988084, 0.9999999999988907, 0.9999999999989673, 0.9999999999990388, /* 7.00 - 7.04 */
            0.9999999999991054, 0.9999999999991676, 0.9999999999992253, 0.9999999999992792, 0.9999999999993294, /* 7.05 - 7.09 */
            0.9999999999993763, 0.9999999999994198, 0.9999999999994604, 0.9999999999994982, 0.9999999999995333, /* 7.10 - 7.14 */
            0.9999999999995661, 0.9999999999995965, 0.9999999999996250, 0.9999999999996514, 0.9999999999996760, /* 7.15 - 7.19 */
            0.9999999999996989, 0.9999999999997202, 0.9999999999997400, 0.9999999999997585, 0.9999999999997757, /* 7.20 - 7.24 */
            0.9999999999997916, 0.9999999999998064, 0.9999999999998203, 0.9999999999998330, 0.9999999999998450, /* 7.25 - 7.29 */
            0.9999999999998561, 0.9999999999998664, 0.9999999999998760, 0.9999999999998850, 0.9999999999998932, /* 7.30 - 7.34 */
            0.9999999999999010, 0.9999999999999081, 0.9999999999999147, 0.9999999999999208, 0.9999999999999265, /* 7.35 - 7.39 */
            0.9999999999999318, 0.9999999999999368, 0.9999999999999414, 0.9999999999999457, 0.9999999999999496, /* 7.40 - 7.44 */
            0.9999999999999534, 0.9999999999999567, 0.9999999999999599, 0.9999999999999629, 0.9999999999999656, /* 7.45 - 7.49 */
            0.9999999999999680, 0.9999999999999705, 0.9999999999999727, 0.9999999999999747, 0.9999999999999765, /* 7.50 - 7.54 */
            0.9999999999999782, 0.9999999999999798, 0.9999999999999813, 0.9999999999999827, 0.9999999999999840, /* 7.55 - 7.59 */
            0.9999999999999851, 0.9999999999999862, 0.9999999999999873, 0.9999999999999882, 0.9999999999999891, /* 7.60 - 7.64 */
            0.9999999999999900, 0.9999999999999907, 0.9999999999999913, 0.9999999999999920, 0.9999999999999927, /* 7.65 - 7.69 */
            0.9999999999999931, 0.9999999999999938, 0.9999999999999942, 0.9999999999999947, 0.9999999999999950, /* 7.70 - 7.74 */
            0.9999999999999953, 0.9999999999999958, 0.9999999999999960, 0.9999999999999964, 0.9999999999999967, /* 7.75 - 7.79 */
            0.9999999999999969, 0.9999999999999971, 0.9999999999999973, 0.9999999999999976, 0.9999999999999978, /* 7.80 - 7.84 */
            0.9999999999999980, 0.9999999999999980, 0.9999999999999982, 0.9999999999999984, 0.9999999999999984, /* 7.85 - 7.89 */
            0.9999999999999987, 0.9999999999999987, 0.9999999999999989, 0.9999999999999989, 0.9999999999999990, /* 7.90 - 7.94 */
            0.9999999999999991, 0.9999999999999991, 0.9999999999999992, 0.9999999999999993, 0.9999999999999993, /* 7.95 - 7.99 */
            0.9999999999999993, 0.9999999999999994, 0.9999999999999994, 0.9999999999999996, 0.9999999999999996, /* 8.00 - 8.04 */
            0.9999999999999996, 0.9999999999999996, 0.9999999999999997, 0.9999999999999997, 0.9999999999999998, /* 8.05 - 8.09 */
            0.9999999999999998, 0.9999999999999998, 0.9999999999999998, 0.9999999999999998, 0.9999999999999998, /* 8.10 - 8.14 */
            0.9999999999999998, 0.9999999999999998, 0.9999999999999998, 0.9999999999999998, 0.9999999999999999, /* 8.15 - 8.19 */
            0.9999999999999999, 0.9999999999999999, 0.9999999999999999, 0.9999999999999999, 0.9999999999999999, /* 8.20 - 8.24 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.25 - 8.29 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.30 - 8.34 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.35 - 8.39 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.40 - 8.44 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.45 - 8.49 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.50 - 8.54 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.55 - 8.59 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.60 - 8.64 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.65 - 8.69 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.70 - 8.74 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.75 - 8.79 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.80 - 8.84 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.85 - 8.89 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.90 - 8.94 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 8.95 - 8.99 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.00 - 9.04 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.05 - 9.09 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.10 - 9.14 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.15 - 9.19 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.20 - 9.24 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.25 - 9.29 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.30 - 9.34 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.35 - 9.39 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.40 - 9.44 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.45 - 9.49 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.50 - 9.54 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.55 - 9.59 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.60 - 9.64 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.65 - 9.69 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.70 - 9.74 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.75 - 9.79 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.80 - 9.84 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.85 - 9.89 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.90 - 9.94 */
            1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, 1.0000000000000000, /* 9.95 - 9.99 */
            1.0000000000000000
    };
    // @formatter:on
}
