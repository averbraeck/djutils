package org.djutils.generator.stats;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Generate the cumulative probability table.
 * @author <a href="https://www.tudelft.nl/staff/p.knoppers/">Peter Knoppers</a>
 */
public class GenerateCumulativeProbabilityTable
{

    /**
     * Generate the cumulative probability table. Output of this program should be pasted in DistNormalTable.java.
     * @param args String[]; the command line arguments (not used).
     */
    public static void main(final String[] args)
    {
        NormalDistribution nd = new NormalDistribution();
        for (int step = 0; step < 1000; step++)
        {
            double x = step / 100d;
            double cumProb = nd.cumulativeProbability(x);
            System.out.print(String.format("%s%18.16f,", step % 5 == 0 ? "            ":" ", cumProb));
            if (step % 5 == 4)
            {
                System.out.println(String.format(" /* %4.2f - %4.2f */", (step / 5 * 5) / 100d, x));
            }
        }
        System.out.println(String.format("            %18.16f", 1.0));
    }
}
