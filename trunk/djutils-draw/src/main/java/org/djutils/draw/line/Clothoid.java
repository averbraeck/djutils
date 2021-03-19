package org.djutils.draw.line;

import org.djutils.complex.Complex;
import org.djutils.draw.DrawRuntimeException;
import org.djutils.exceptions.Throw;
import org.djutils.polynomialroots.PolynomialRoots;

/**
 * Approximate a clothoid with a PolyLine3d. <br>
 * Derived from https://github.com/ebertolazzi/G1fitting/blob/master/src/Clothoid.cc
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 */
public final class Clothoid
{
    /** Utility class. */
    private Clothoid()
    {
        // do not instantiate
    }

    /** ??? */
    static final double A_THRESOLD = 0.01;

    /** ??? */
    static final int A_SERIE_SIZE = 3;

    //@formatter:off
    /** Fresnel coefficients FN. */
    static final double[] FN = 
    { 
        0.49999988085884732562,
        1.3511177791210715095,
        1.3175407836168659241,
        1.1861149300293854992,
        0.7709627298888346769,
        0.4173874338787963957,
        0.19044202705272903923,
        0.06655998896627697537,
        0.022789258616785717418,
        0.0040116689358507943804,
        0.0012192036851249883877 
        };

    /** Fresnel coefficients FD. */
    static final double[] FD = 
    { 
        1.0,
        2.7022305772400260215,
        4.2059268151438492767,
        4.5221882840107715516,
        3.7240352281630359588,
        2.4589286254678152943,
        1.3125491629443702962,
        0.5997685720120932908,
        0.20907680750378849485,
        0.07159621634657901433,
        0.012602969513793714191,
        0.0038302423512931250065 
    };

    /** Fresnel coefficients GN. */
    static final double[] GN = 
    { 
        0.50000014392706344801,
        0.032346434925349128728,
        0.17619325157863254363,
        0.038606273170706486252,
        0.023693692309257725361,
        0.007092018516845033662,
        0.0012492123212412087428,
        0.00044023040894778468486,
        -8.80266827476172521e-6,
        -1.4033554916580018648e-8,
        2.3509221782155474353e-10 
    };

    /** Fresnel coefficients GD. */
    static final double[] GD = 
    { 
        1.0,
        2.0646987497019598937,
        2.9109311766948031235,
        2.6561936751333032911,
        2.0195563983177268073,
        1.1167891129189363902,
        0.57267874755973172715,
        0.19408481169593070798,
        0.07634808341431248904,
        0.011573247407207865977,
        0.0044099273693067311209,
        -0.00009070958410429993314 
    };

    /** Pi. */
    static final double m_pi        = Math.PI;
    /** Half Pi. */
    static final double m_pi_2      = Math.PI / 2;
    /** Two Pi. */
    static final double m_2pi       = 2 * Math.PI;
    /** One over Pi. */
    static final double m_1_pi      = 1 / Math.PI;
    /** One over square root of Pi. */
    static final double m_1_sqrt_pi = 1 / Math.sqrt(Math.PI);

    /*
     *  #######                                           
     *  #       #####  ######  ####  #    # ###### #      
     *  #       #    # #      #      ##   # #      #      
     *  #####   #    # #####   ####  # #  # #####  #      
     *  #       #####  #           # #  # # #      #      
     *  #       #   #  #      #    # #   ## #      #      
     *  #       #    # ######  ####  #    # ###### ###### 
     */
    //@formatter:on

    /**
     * Purpose: This program computes the Fresnel integrals.
     * 
     * <pre>
     * C(x) and S(x) using subroutine FCS
     * Input :  x --- Argument of C(x) and S(x)
     * Output:  C --- C(x)
     * S --- S(x)
     * Example:
     * x          C(x)          S(x)
     * -----------------------------------
     * 0.0      .00000000      .00000000
     * 0.5      .49234423      .06473243
     * 1.0      .77989340      .43825915
     * 1.5      .44526118      .69750496
     * 2.0      .48825341      .34341568
     * 2.5      .45741301      .61918176
     * Purpose: Compute Fresnel integrals C(x) and S(x)
     * Input :  x --- Argument of C(x) and S(x)
     * Output:  C --- C(x)
     * S --- S(x)
     * </pre>
     * 
     * @param y double;
     * @return double[]; double array with two elements; C is stored in the first, S in the second
     */
    private static double[] fresnelCS(final double y)
    {

        final double eps = 1E-15;
        final double x = y > 0 ? y : -y;
        double resultC;
        double resultS;

        if (x < 1.0)
        {
            double twofn, fact, denterm, numterm, sum, term;

            final double s = m_pi_2 * (x * x);
            final double t = -s * s;

            // Cosine integral series
            twofn = 0.0;
            fact = 1.0;
            denterm = 1.0;
            numterm = 1.0;
            sum = 1.0;
            do
            {
                twofn += 2.0;
                fact *= twofn * (twofn - 1.0);
                denterm += 4.0;
                numterm *= t;
                term = numterm / (fact * denterm);
                sum += term;
            }
            while (Math.abs(term) > eps * Math.abs(sum));

            resultC = x * sum;

            // Sine integral series
            twofn = 1.0;
            fact = 1.0;
            denterm = 3.0;
            numterm = 1.0;
            sum = 1.0 / 3.0;
            do
            {
                twofn += 2.0;
                fact *= twofn * (twofn - 1.0);
                denterm += 4.0;
                numterm *= t;
                term = numterm / (fact * denterm);
                sum += term;
            }
            while (Math.abs(term) > eps * Math.abs(sum));

            resultS = m_pi_2 * sum * (x * x * x);
        }
        else if (x < 6.0)
        {

            // Rational approximation for f
            double sumn = 0.0;
            double sumd = FD[11];
            for (int k = 10; k >= 0; --k)
            {
                sumn = FN[k] + x * sumn;
                sumd = FD[k] + x * sumd;
            }
            double f = sumn / sumd;

            // Rational approximation for g
            sumn = 0.0;
            sumd = GD[11];
            for (int k = 10; k >= 0; --k)
            {
                sumn = GN[k] + x * sumn;
                sumd = GD[k] + x * sumd;
            }
            double g = sumn / sumd;

            double u = m_pi_2 * (x * x);
            double sinU = Math.sin(u);
            double cosU = Math.cos(u);
            resultC = 0.5 + f * sinU - g * cosU;
            resultS = 0.5 - f * cosU - g * sinU;
        }
        else
        {

            double absterm;

            // x >= 6; asymptotic expansions for f and g

            final double s = m_pi * x * x;
            final double t = -1 / (s * s);

            // Expansion for f
            double numterm = -1.0;
            double term = 1.0;
            double sum = 1.0;
            double oldterm = 1.0;
            double eps10 = 0.1 * eps;

            do
            {
                numterm += 4.0;
                term *= numterm * (numterm - 2.0) * t;
                sum += term;
                absterm = Math.abs(term);
                Throw.when(false, oldterm >= absterm, DrawRuntimeException.class,
                        "In FresnelCS f not converged to eps, x = " + x + " oldterm = " + oldterm + " absterm = " + absterm);
                oldterm = absterm;
            }
            while (absterm > eps10 * Math.abs(sum));

            double f = sum / (m_pi * x);

            // Expansion for g
            numterm = -1.0;
            term = 1.0;
            sum = 1.0;
            oldterm = 1.0;

            do
            {
                numterm += 4.0;
                term *= numterm * (numterm + 2.0) * t;
                sum += term;
                absterm = Math.abs(term);
                Throw.when(oldterm >= absterm, DrawRuntimeException.class, "In FresnelCS g does not converge to eps, x = " + x
                        + " oldterm = " + oldterm + " absterm = " + absterm);
                oldterm = absterm;
            }
            while (absterm > eps10 * Math.abs(sum));

            double g = m_pi * x;
            g = sum / (g * g * x);

            double u = m_pi_2 * (x * x);
            double sinU = Math.sin(u);
            double cosU = Math.cos(u);
            resultC = 0.5 + f * sinU - g * cosU;
            resultS = 0.5 - f * cosU - g * sinU;

        }
        if (y < 0)
        {
            resultC = -resultC;
            resultS = -resultS;
        }
        return new double[] { resultC, resultS };
    }

    /**
     * ???
     * @param nk int; size of the provided arrays
     * @param t double;
     * @param C double[]; should have length nk
     * @param S double[]; shouldhave laength nk
     */
    private static void fresnelCS(final int nk, final double t, final double[] C, final double[] S)
    {
        double[] cs = fresnelCS(t);
        C[0] = cs[0];
        S[0] = cs[1];

        if (nk > 1)
        {
            double tt = m_pi_2 * (t * t);
            double ss = Math.sin(tt);
            double cc = Math.cos(tt);
            C[1] = ss * m_1_pi;
            S[1] = (1 - cc) * m_1_pi;
            if (nk > 2)
            {
                C[2] = (t * ss - S[0]) * m_1_pi;
                S[2] = (C[0] - t * cc) * m_1_pi;
            }
        }
    }

    /**
     * ???
     * @param a double;
     * @param b double;
     * @return double[] with two elements set to X and Y
     */
    private static double[] evalXYaLarge(final double a, final double b)
    {
        double s = a > 0 ? +1 : -1;
        double absa = Math.abs(a);
        double z = m_1_sqrt_pi * Math.sqrt(absa);
        double ell = s * b * m_1_sqrt_pi / Math.sqrt(absa);
        double g = -0.5 * s * (b * b) / absa;
        double cg = Math.cos(g) / z;
        double sg = Math.sin(g) / z;

        // double Cl, Sl, Cz, Sz;
        double[] resultL = fresnelCS(ell);
        double[] resultZ = fresnelCS(ell + z);

        double dC0 = resultZ[0] - resultL[0];
        double dS0 = resultZ[1] - resultL[1];

        double X = cg * dC0 - s * sg * dS0;
        double Y = sg * dC0 + s * cg * dS0;
        return new double[] { X, Y };
    }

    // -------------------------------------------------------------------------
    // nk max 3
    /**
     * ???
     * @param nk int; minimum 0; maximum 3
     * @param a double; ?
     * @param b double; ?
     * @param X double[]; ?
     * @param Y double[]; ?
     */
    private static void evalXYaLarge(final int nk, final double a, final double b, double[] X, double[] Y)
    {
        Throw.when(nk <= 0 || nk >= 4, DrawRuntimeException.class,
                "In evalXYaLarge first argument nk must be in 1..3, nk " + nk);

        double s = a > 0 ? +1 : -1;
        double absa = Math.abs(a);
        double z = m_1_sqrt_pi * Math.sqrt(absa);
        double ell = s * b * m_1_sqrt_pi / Math.sqrt(absa);
        double g = -0.5 * s * (b * b) / absa;
        double cg = Math.cos(g) / z;
        double sg = Math.sin(g) / z;

        double[] Cl = new double[3];
        double[] Sl = new double[3];
        double[] Cz = new double[3];
        double[] Sz = new double[3];

        fresnelCS(nk, ell, Cl, Sl);
        fresnelCS(nk, ell + z, Cz, Sz);

        double dC0 = Cz[0] - Cl[0];
        double dS0 = Sz[0] - Sl[0];
        X[0] = cg * dC0 - s * sg * dS0;
        Y[0] = sg * dC0 + s * cg * dS0;
        if (nk > 1)
        {
            cg /= z;
            sg /= z;
            double dC1 = Cz[1] - Cl[1];
            double dS1 = Sz[1] - Sl[1];
            double DC = dC1 - ell * dC0;
            double DS = dS1 - ell * dS0;
            X[1] = cg * DC - s * sg * DS;
            Y[1] = sg * DC + s * cg * DS;
            if (nk > 2)
            {
                double dC2 = Cz[2] - Cl[2];
                double dS2 = Sz[2] - Sl[2];
                DC = dC2 + ell * (ell * dC0 - 2 * dC1);
                DS = dS2 + ell * (ell * dS0 - 2 * dS1);
                cg = cg / z;
                sg = sg / z;
                X[2] = cg * DC - s * sg * DS;
                Y[2] = sg * DC + s * cg * DS;
            }
        }
    }

    /**
     * LommelReduced.
     * @param mu double; ?
     * @param nu double; ?
     * @param b double; ?
     * @return double; ?
     */
    private static double LommelReduced(final double mu, final double nu, final double b)
    {
        double tmp = 1 / ((mu + nu + 1) * (mu - nu + 1));
        double res = tmp;
        for (int n = 1; n <= 100; ++n)
        {
            tmp *= (-b / (2 * n + mu - nu + 1)) * (b / (2 * n + mu + nu + 1));
            res += tmp;
            if (Math.abs(tmp) < Math.abs(res) * 1e-50)
            {
                break;
            }
        }
        return res;
    }

    /**
     * ???
     * @param nk int; ?
     * @param b double; ?
     * @param X double[]; ?
     * @param Y double[]; ?
     */
    private static void evalXYazero(final int nk, final double b, double X[], double Y[])
    {
        double sb = Math.sin(b);
        double cb = Math.cos(b);
        double b2 = b * b;
        if (Math.abs(b) < 1e-3)
        {
            X[0] = 1 - (b2 / 6) * (1 - (b2 / 20) * (1 - (b2 / 42)));
            Y[0] = (b / 2) * (1 - (b2 / 12) * (1 - (b2 / 30)));
        }
        else
        {
            X[0] = sb / b;
            Y[0] = (1 - cb) / b;
        }
        // use recurrence in the stable part
        int m = (int) Math.floor(2 * b);
        if (m >= nk)
        {
            m = nk - 1;
        }
        if (m < 1)
        {
            m = 1;
        }
        for (int k = 1; k < m; ++k)
        {
            X[k] = (sb - k * Y[k - 1]) / b;
            Y[k] = (k * X[k - 1] - cb) / b;
        }
        // use Lommel for the unstable part
        if (m < nk)
        {
            double A = b * sb;
            double D = sb - b * cb;
            double B = b * D;
            double C = -b2 * sb;
            double rLa = LommelReduced(m + 0.5, 1.5, b);
            double rLd = LommelReduced(m + 0.5, 0.5, b);
            for (int k = m; k < nk; ++k)
            {
                double rLb = LommelReduced(k + 1.5, 0.5, b);
                double rLc = LommelReduced(k + 1.5, 1.5, b);
                X[k] = (k * A * rLa + B * rLb + cb) / (1 + k);
                Y[k] = (C * rLc + sb) / (2 + k) + D * rLd;
                rLa = rLc;
                rLd = rLb;
            }
        }
    }

    /**
     * ???
     * @param a double; ?
     * @param b double; ?
     * @param p double; ?
     * @return double[]; containing the two values; X and Y.
     */
    private static double[] evalXYaSmall(final double a, final double b, final int p)
    {
        Throw.when(p >= 11 && p <= 0, DrawRuntimeException.class, "In evalXYaSmall p = " + p + " must be in 1..10");

        double[] x0 = new double[43];
        double[] y0 = new double[43];

        int nkk = 4 * p + 3; // max 43
        evalXYazero(nkk, b, x0, y0);

        double x = x0[0] - (a / 2) * y0[2];
        double y = y0[0] + (a / 2) * x0[2];

        double t = 1;
        double aa = -a * a / 4; // controllare!
        for (int n = 1; n <= p; ++n)
        {
            t *= aa / (2 * n * (2 * n - 1));
            double bf = a / (4 * n + 2);
            int jj = 4 * n;
            x += t * (x0[jj] - bf * y0[jj + 2]);
            y += t * (y0[jj] + bf * x0[jj + 2]);
        }
        return new double[] { x, y };
    }

    /**
     * ???
     * @param nk int; ?
     * @param a double; ?
     * @param b double; ?
     * @param p double; ?
     * @param x double[]; ?
     * @param y double[]; ?
     */
    private static void evalXYaSmall(final int nk, final double a, final double b, final int p, final double[] x,
            final double[] y)
    {

        int nkk = nk + 4 * p + 2; // max 45
        double[] x0 = new double[45];
        double[] y0 = new double[45];

        Throw.when(nkk >= 46, DrawRuntimeException.class,
                "In evalXYaSmall (nk,p) = (" + nk + "," + p + ")\n" + "nk + 4*p + 2 = " + nkk + " must be less than 46\n");

        evalXYazero(nkk, b, x0, y0);

        for (int j = 0; j < nk; ++j)
        {
            x[j] = x0[j] - (a / 2) * y0[j + 2];
            y[j] = y0[j] + (a / 2) * x0[j + 2];
        }

        double t = 1;
        double aa = -a * a / 4; // controllare!
        for (int n = 1; n <= p; ++n)
        {
            t *= aa / (2 * n * (2 * n - 1));
            double bf = a / (4 * n + 2);
            for (int j = 0; j < nk; ++j)
            {
                int jj = 4 * n + j;
                x[j] += t * (x0[jj] - bf * y0[jj + 2]);
                y[j] += t * (y0[jj] + bf * x0[jj + 2]);
            }
        }
    }

    /**
     * ???
     * @param a double; ?
     * @param b double; ?
     * @param c double; ?
     * @return double[]; size two containing C and S
     */
    private static double[] GeneralizedFresnelCS(final double a, final double b, final double c)
    {

        double[] xxyy = Math.abs(a) < A_THRESOLD ? evalXYaSmall(a, b, A_SERIE_SIZE) : evalXYaLarge(a, b);

        double cosC = Math.cos(c);
        double sinC = Math.sin(c);

        // FIXME: Confusing names
        double intC = xxyy[0] * cosC - xxyy[1] * sinC;
        double intS = xxyy[0] * sinC + xxyy[1] * cosC;
        return new double[] { intC, intS };
    }

    /**
     * ???
     * @param nk int; ?
     * @param a double; ?
     * @param b double; ?
     * @param c double; ?
     * @param intC double[]; stores C results
     * @param intS double[]; stores S results
     */
    static void GeneralizedFresnelCS(final int nk, final double a, final double b, final double c, final double[] intC,
            final double[] intS)
    {

        Throw.when(nk <= 0 || nk >= 4, DrawRuntimeException.class, "nk = " + nk + " must be in 1..3");

        if (Math.abs(a) < A_THRESOLD)
        {
            evalXYaSmall(nk, a, b, A_SERIE_SIZE, intC, intS);
        }
        else
        {
            evalXYaLarge(nk, a, b, intC, intS);
        }

        double cosC = Math.cos(c);
        double sinC = Math.sin(c);

        for (int k = 0; k < nk; ++k)
        {
            double xx = intC[k];
            double yy = intS[k];
            intC[k] = xx * cosC - yy * sinC;
            intS[k] = xx * sinC + yy * cosC;
        }
    }

    /** CF coefficients. */
    private static final double[] CF = { 2.989696028701907, 0.716228953608281, -0.458969738821509, -0.502821153340377,
            0.261062141752652, -0.045854475238709 };

    /**
     * Create a clothoid connecting (x0,y0) to (x1,y1) having direction theta0 at the start point and theta1 at the end point.
     * @param x0 double; x coordinate of the start point
     * @param y0 double; y coordinate of the start point
     * @param theta0 double; direction at the start point (in radians)
     * @param x1 double; x coordinate of the end point
     * @param y1 double; y coordinate of the end point
     * @param theta1 double; direction at the end point (in radians)
     * @return int; the number of iterations
     */
    public static int buildClothoid(final double x0, final double y0, final double theta0, final double x1, final double y1,
            final double theta1)
    {
        double k;
        double dk;
        double l;
        // traslazione in (0,0)
        double dx = x1 - x0;
        double dy = y1 - y0;
        double r = Math.hypot(dx, dy);
        double phi = Math.atan2(dy, dx);

        double phi0 = theta0 - phi;
        double phi1 = theta1 - phi;

        phi0 -= m_2pi * Math.rint(phi0 / m_2pi);
        phi1 -= m_2pi * Math.rint(phi1 / m_2pi);

        if (phi0 > m_pi)
        {
            phi0 -= m_2pi;
        }
        if (phi0 < -m_pi)
        {
            phi0 += m_2pi;
        }
        if (phi1 > m_pi)
        {
            phi1 -= m_2pi;
        }
        if (phi1 < -m_pi)
        {
            phi1 += m_2pi;
        }

        double delta = phi1 - phi0;

        // punto iniziale
        double x = phi0 * m_1_pi;
        double y = phi1 * m_1_pi;
        double xy = x * y;
        y *= y;
        x *= x;
        double a =
                (phi0 + phi1) * (CF[0] + xy * (CF[1] + xy * CF[2]) + (CF[3] + xy * CF[4]) * (x + y) + CF[5] * (x * x + y * y));

        // newton
        double g = 0;
        double dg;
        double[] intC = new double[3];
        double[] intS = new double[3];
        int niter = 0;
        do
        {
            GeneralizedFresnelCS(3, 2 * a, delta - a, phi0, intC, intS);
            g = intS[0];
            dg = intC[2] - intC[1];
            a -= g / dg;
        }
        while (++niter <= 10 && Math.abs(g) > 1E-12);

        Throw.when(Math.abs(g) > 1E-8, DrawRuntimeException.class, "Newton did not converge, g = " + g + " niter = " + niter);
        double[] cs = GeneralizedFresnelCS(2 * a, delta - a, phi0);
        intC[0] = cs[0];
        intS[0] = cs[1];
        l = r / intC[0];

        Throw.when(l <= 0, DrawRuntimeException.class, "Negative length L = " + l);
        k = (delta - a) / l;
        dk = 2 * a / l / l;

        return niter;
    }

    /**
     * Create a clothoid connecting (x0,y0) to (x1,y1) having direction theta0 at the start point and theta1 at the end point.
     * @param x0 double; x coordinate of the start point
     * @param y0 double; y coordinate of the start point
     * @param theta0 double; direction at the start point (in radians)
     * @param x1 double; x coordinate of the end point
     * @param y1 double; y coordinate of the end point
     * @param theta1 double; direction at the end point (in radians)
     * @return int; the number of iterations
     */
    public static int buildClothoidMoreResults(final double x0, final double y0, final double theta0, final double x1,
            final double y1, final double theta1)
    {
        double k;
        double dk;
        double l;
        double k_1;
        double dk_1;
        double l_1;
        double k_2;
        double dk_2;
        double l_2;
        // traslazione in (0,0)
        double dx = x1 - x0;
        double dy = y1 - y0;
        double r = Math.hypot(dx, dy);
        double phi = Math.atan2(dy, dx);

        double phi0 = theta0 - phi;
        double phi1 = theta1 - phi;

        phi0 -= m_2pi * Math.rint(phi0 / m_2pi);
        phi1 -= m_2pi * Math.rint(phi1 / m_2pi);

        if (phi0 > m_pi)
        {
            phi0 -= m_2pi;
        }
        if (phi0 < -m_pi)
        {
            phi0 += m_2pi;
        }
        if (phi1 > m_pi)
        {
            phi1 -= m_2pi;
        }
        if (phi1 < -m_pi)
        {
            phi1 += m_2pi;
        }

        double delta = phi1 - phi0;

        // punto iniziale
        double x = phi0 * m_1_pi;
        double y = phi1 * m_1_pi;
        double xy = x * y;
        y *= y;
        x *= x;
        double a =
                (phi0 + phi1) * (CF[0] + xy * (CF[1] + xy * CF[2]) + (CF[3] + xy * CF[4]) * (x + y) + CF[5] * (x * x + y * y));

        // newton
        double g = 0;
        double dg;
        double[] intC = new double[3];
        double[] intS = new double[3];
        int niter = 0;
        do
        {
            GeneralizedFresnelCS(3, 2 * a, delta - a, phi0, intC, intS);
            g = intS[0];
            dg = intC[2] - intC[1];
            a -= g / dg;
        }
        while (++niter <= 10 && Math.abs(g) > 1E-12);

        Throw.when(Math.abs(g) > 1E-8, DrawRuntimeException.class, "Newton do not converge, g = " + g + " niter = " + niter);
        GeneralizedFresnelCS(3, 2 * a, delta - a, phi0, intC, intS);
        l = r / intC[0];

        Throw.when(l <= 0, DrawRuntimeException.class, "Negative length L = " + l);
        k = (delta - a) / l;
        dk = 2 * a / l / l;

        double alpha = intC[0] * intC[1] + intS[0] * intS[1];
        double beta = intC[0] * intC[2] + intS[0] * intS[2];
        double gamma = intC[0] * intC[0] + intS[0] * intS[0];
        double tx = intC[1] - intC[2];
        double ty = intS[1] - intS[2];
        double txy = l * (intC[1] * intS[2] - intC[2] * intS[1]);
        double omega = l * (intS[0] * tx - intC[0] * ty) - txy;

        delta = intC[0] * tx + intS[0] * ty;

        l_1 = omega / delta;
        l_2 = txy / delta;

        delta *= l;
        k_1 = (beta - gamma - k * omega) / delta;
        k_2 = -(beta + k * txy) / delta;

        delta *= l / 2;
        dk_1 = (gamma - alpha - dk * omega * l) / delta;
        dk_2 = (alpha - dk * txy * l) / delta;

        return niter;
    }

    // void
    // eval(final double s,
    // double & theta,
    // double & kappa,
    // double & x,
    // double & y ) const {
    // double C, S ;
    // GeneralizedFresnelCS( dk*s*s, k*s, theta0, C, S ) ;
    // x = x0 + s*C ;
    // y = y0 + s*S ;
    // theta = theta0 + s*(k+s*(dk/2)) ;
    // kappa = k + s*dk ;
    // }
    //
    // void
    // ClothoidCurve::eval( double s, double & x, double & y ) const {
    // double C, S ;
    // GeneralizedFresnelCS( dk*s*s, k*s, theta0, C, S ) ;
    // x = x0 + s*C ;
    // y = y0 + s*S ;
    // }
    //
    // void
    // ClothoidCurve::eval_D( double s, double & x_D, double & y_D ) const {
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // x_D = cos(theta) ;
    // y_D = sin(theta) ;
    // }
    //
    // void
    // ClothoidCurve::eval_DD( double s, double & x_DD, double & y_DD ) const {
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // double theta_D = k+s*dk ;
    // x_DD = -sin(theta)*theta_D ;
    // y_DD = cos(theta)*theta_D ;
    // }
    //
    // void
    // ClothoidCurve::eval_DDD( double s, double & x_DDD, double & y_DDD ) const {
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // double theta_D = k+s*dk ;
    // double C = cos(theta) ;
    // double S = sin(theta) ;
    // double th2 = theta_D*theta_D ;
    // x_DDD = -C*th2-S*dk ;
    // y_DDD = -S*th2+C*dk ;
    // }
    //
    //// offset curve
    // void
    // ClothoidCurve::eval( double s, double offs, double & x, double & y ) const {
    // double C, S ;
    // GeneralizedFresnelCS( dk*s*s, k*s, theta0, C, S ) ;
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // double nx = -sin(theta) ;
    // double ny = cos(theta) ;
    // x = x0 + s*C + offs * nx ;
    // y = y0 + s*S + offs * ny ;
    // }
    //
    // void
    // ClothoidCurve::eval_D( double s, double offs, double & x_D, double & y_D ) const {
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // double theta_D = k+s*dk ;
    // double scale = 1-offs*theta_D ;
    // x_D = cos(theta)*scale ;
    // y_D = sin(theta)*scale ;
    // }
    //
    // void
    // ClothoidCurve::eval_DD( double s, double offs, double & x_DD, double & y_DD ) const {
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // double theta_D = k+s*dk ;
    // double C = cos(theta) ;
    // double S = sin(theta) ;
    // double tmp1 = theta_D*(1-theta_D*offs) ;
    // double tmp2 = offs*dk ;
    // x_DD = -tmp1*S - C*tmp2 ;
    // y_DD = tmp1*C - S*tmp2 ;
    // }
    //
    // void
    // ClothoidCurve::eval_DDD( double s, double offs, double & x_DDD, double & y_DDD ) const {
    // double theta = theta0 + s*(k+s*(dk/2)) ;
    // double theta_D = k+s*dk ;
    // double C = cos(theta) ;
    // double S = sin(theta) ;
    // double tmp1 = theta_D*theta_D*(theta_D*offs-1) ;
    // double tmp2 = dk*(1-3*theta_D*offs) ;
    // x_DDD = tmp1*C-tmp2*S ;
    // y_DDD = tmp1*S+tmp2*C ;
    // }

    /**
     * ???
     * @param theta0 double; theta0
     * @param theta double; theta
     * @return double; kappa
     */
    private static double kappa(final double theta0, final double theta)
    {
        double x = theta0 * theta0;
        double a = -3.714 + x * 0.178;
        double b = -1.913 - x * 0.0753;
        double c = 0.999 + x * 0.03475;
        double d = 0.191 - x * 0.00703;
        double e = 0.500 - x * -0.00172;
        double t = d * theta0 + e * theta;
        return a * theta0 + b * theta + c * (t * t * t);
    }

    /**
     * theta_guess.
     * <p>
     * FIXME value parameter ok;
     * @param theta0 double; theta0
     * @param k0 double; k0
     * @return double; theta
     */
    private static double theta_guess(final double theta0, final double k0)
    {
        double x = theta0 * theta0;
        double a = -3.714 + x * 0.178;
        double b = -1.913 - x * 0.0753;
        double c = 0.999 + x * 0.03475;
        double d = 0.191 - x * 0.00703;
        double e = 0.500 - x * -0.00172;
        double e2 = e * e;
        double dt = d * theta0;
        double dt2 = dt * dt;
        double qA = c * e * e2;
        double qB = 3 * (c * d * e2 * theta0);
        double qC = 3 * c * e * dt2 + b;
        double qD = c * (dt * dt2) + a * theta0 - k0;
        boolean ok;

        Complex[] roots = PolynomialRoots.cubicRoots(qA, qB, qC, qD);
        // Count the real roots
        int nr = 0;
        for (Complex root : roots)
        {
            if (root.isReal())
            {
                nr++;
            }
        }
        // cerco radice reale piu vicina
        double theta;
        switch (nr)
        {
            case 0:
            default:
                ok = false;
                return 0;
            case 1:
                theta = roots[0].re;
                break;
            case 2:
                if (Math.abs(roots[0].re - theta0) < Math.abs(roots[1].re - theta0))
                {
                    theta = roots[0].re;
                }
                else
                {
                    theta = roots[1].re;
                }
                break;
            case 3:
                theta = roots[0].re;
                for (int i = 1; i < 3; ++i)
                {
                    if (Math.abs(theta - theta0) > Math.abs(roots[i].re - theta0))
                    {
                        theta = roots[i].re;
                    }
                }
                break;
        }
        ok = Math.abs(theta - theta0) < m_pi;
        return theta;
    }

    // bool
    // ClothoidCurve::setup_forward( double _x0,
    // double _y0,
    // double _theta0,
    // double _k,
    // double _x1,
    // double _y1,
    // double tol ) {
    //
    // x0 = _x0 ;
    // y0 = _y0 ;
    // theta0 = _theta0 ;
    // k = _k ;
    // s_min = 0 ;
    //
    //// Compute guess angles
    // double len = hypot( _y1-_y0, _x1-_x0 ) ;
    // double arot = atan2( _y1-_y0, _x1-_x0 ) ;
    // double th0 = theta0 - arot ;
    //// normalize angle
    // while ( th0 > m_pi ) th0 -= m_2pi ;
    // while ( th0 < -m_pi ) th0 += m_2pi ;
    //
    //// solve the problem from (0,0) to (1,0)
    // double k0 = k*len ;
    // double alpha = 2.6 ;
    // double thmin = max(-m_pi,-theta0/2-alpha) ;
    // double thmax = min( m_pi,-theta0/2+alpha) ;
    // double Kmin = kappa( th0, thmax ) ;
    // double Kmax = kappa( th0, thmin ) ;
    // bool ok ;
    // double th = theta_guess( th0, max(min(k0,Kmax),Kmin), ok ) ;
    // if ( ok ) {
    // for ( int iter = 0 ; iter < 10 ; ++iter ) {
    // double dk, L, k_1, dk_1, L_1, k_2, dk_2, L_2 ;
    // buildClothoid( 0, 0, th0,
    // 1, 0, th,
    // k, dk, L, k_1, dk_1, L_1, k_2, dk_2, L_2 ) ;
    // double f = k - k0 ;
    // double df = k_2 ;
    // double dth = f/df ;
    // th -= dth ;
    // if ( abs(dth) < tol && abs(f) < tol ) {
    //// transform solution
    // buildClothoid( x0, y0, theta0,
    // _x1, _y1, arot + th,
    // _k, dk, s_max ) ;
    // return true ;
    // }
    // }
    // }
    // return false ;
    // }
    //
    // void
    // ClothoidCurve::change_origin( double s0 ) {
    // double new_theta, new_kappa, new_x0, new_y0 ;
    // eval( s0, new_theta, new_kappa, new_x0, new_y0 ) ;
    // x0 = new_x0 ;
    // y0 = new_y0 ;
    // theta0 = new_theta ;
    // k = new_kappa ;
    // s_min -= s0 ;
    // s_max -= s0 ;
    // }
    //
    // bool
    // ClothoidCurve::bbTriangle( double offs,
    // double p0[2],
    // double p1[2],
    // double p2[2] ) const {
    // double theta_max = theta( s_max ) ;
    // double theta_min = theta( s_min ) ;
    // double dtheta = Math.abs( theta_max-theta_min ) ;
    // if ( dtheta < m_pi_2 ) {
    // double alpha, t0[2] ;
    // eval( s_min, offs, p0[0], p0[1] ) ;
    // eval_D( s_min, t0[0], t0[1] ) ; // no offset
    // if ( dtheta > 0.0001 * m_pi_2 ) {
    // double t1[2] ;
    // eval( s_max, offs, p1[0], p1[1] ) ;
    // eval_D( s_max, t1[0], t1[1] ) ; // no offset
    //// risolvo il sistema
    //// p0 + alpha * t0 = p1 + beta * t1
    //// alpha * t0 - beta * t1 = p1 - p0
    // double det = t1[0]*t0[1]-t0[0]*t1[1] ;
    // alpha = ((p1[1]-p0[1])*t1[0] - (p1[0]-p0[0])*t1[1])/det ;
    // } else {
    //// se angolo troppo piccolo uso approx piu rozza
    // alpha = s_max - s_min ;
    // }
    // p2[0] = p0[0] + alpha*t0[0] ;
    // p2[1] = p0[1] + alpha*t0[1] ;
    // return true ;
    // } else {
    // return false ;
    // }
    // }
    //
    // void
    // ClothoidCurve::bbSplit( double split_angle,
    // double split_size,
    // double split_offs,
    // vector<ClothoidCurve> & c,
    // vector<Triangle2D> & t ) const {
    //
    //// step 0: controllo se curvatura passa per 0
    // double k_min = theta_D( s_min ) ;
    // double k_max = theta_D( s_max ) ;
    // c.clear() ;
    // t.clear() ;
    // if ( k_min * k_max < 0 ) {
    //// risolvo (s-s_min)*dk+k_min = 0 --> s = s_min-k_min/dk
    // double s_med = s_min-k_min/dk ;
    //
    // ClothoidCurve tmp(*this) ;
    // tmp.trim(s_min,s_med) ;
    // tmp.bbSplit_internal( split_angle, split_size, split_offs, c, t ) ;
    // tmp.trim(s_med,s_max) ;
    // tmp.bbSplit_internal( split_angle, split_size, split_offs, c, t ) ;
    // }else
    //
    // {
    // bbSplit_internal(split_angle, split_size, split_offs, c, t);
    // }
    // }
    //
    // static double
    //
    // abs2pi( double a ) {
    // a = Math.abs(a) ;
    // while ( a > m_pi ) a -= m_2pi ;
    // return Math.abs(a) ;
    // }
    //
    // void
    // ClothoidCurve::bbSplit_internal( double split_angle,
    // double split_size,
    // double split_offs,
    // vector<ClothoidCurve> & c,
    // vector<Triangle2D> & t ) const {
    //
    // double theta_min, kappa_min, x_min, y_min,
    // theta_max, kappa_max, x_max, y_max ;
    //
    // eval( s_min, theta_min, kappa_min, x_min, y_min ) ;
    // eval( s_max, theta_max, kappa_max, x_max, y_max ) ;
    //
    // double dtheta = Math.abs( theta_max - theta_min ) ;
    // double dx = x_max - x_min ;
    // double dy = y_max - y_min ;
    // double len = hypot( dy, dx ) ;
    // double dangle = abs2pi(atan2( dy, dx )-theta_min) ;
    // if ( dtheta <= split_angle && len*tan(dangle) <= split_size ) {
    // Triangle2D tt ;
    // this->bbTriangle(split_offs,tt) ;
    // c.push_back(*this) ;
    // t.push_back(tt) ;
    // } else {
    //
    // ClothoidCurve cc(*this) ;
    // double s_med = (s_min+s_max)/2 ;
    // cc.trim(s_min,s_med) ;
    // cc.bbSplit_internal( split_angle, split_size, split_offs, c, t ) ;
    // cc.trim(s_med,s_max) ;
    // cc.bbSplit_internal( split_angle, split_size, split_offs, c, t ) ;
    // }}
    //
    // bool ClothoidCurve::intersect_internal(ClothoidCurve&c1,
    //
    // double c1_offs, double&s1,ClothoidCurve&c2,
    //
    // double c2_offs, double&s2,
    //
    // int max_iter,
    // double tolerance)const{
    //
    // double angle1a = c1.theta(c1.s_min);
    //
    // double angle1b = c1.theta(c1.s_max);
    //
    // double angle2a = c2.theta(c2.s_min);
    //
    // double angle2b = c2.theta(c2.s_max);
    //
    // // cerca angoli migliori per partire
    // double dmax = abs2pi(angle1a - angle2a);
    //
    // double dab = abs2pi(angle1a - angle2b);
    //
    // double dba = abs2pi(angle1b - angle2a);
    //
    // double dbb = abs2pi(angle1b - angle2b);s1=c1.s_min;s2=c2.s_min;if(dmax<dab)
    // {
    // dmax = dab;
    // s2 = c2.s_max;
    // }if(dmax<dba)
    // {
    // dmax = dba;
    // s1 = c1.s_min;
    // s2 = c2.s_min;
    // }if(dmax<dbb)
    // {
    // s1 = c1.s_min;
    // s2 = c2.s_max;
    // }for(
    //
    // int i = 0;i<max_iter;++i)
    // {
    // double t1[2], t2[2], p1[2], p2[2] ;
    // c1.eval( s1, c1_offs, p1[0], p1[1] ) ;
    // c1.eval_D( s1, c1_offs, t1[0], t1[1] ) ;
    // c2.eval( s2, c2_offs, p2[0], p2[1] ) ;
    // c2.eval_D( s2, c2_offs, t2[0], t2[1] ) ;
    /// *
    //// risolvo il sistema
    //// p1 + alpha * t1 = p2 + beta * t2
    //// alpha * t1 - beta * t2 = p2 - p1
    ////
    //// / t1[0] -t2[0] \ / alpha \ = / p2[0] - p1[0] \
    //// \ t1[1] -t2[1] / \ beta / \ p2[1] - p1[1] /
    // */
    // double det = t2[0]*t1[1]-t1[0]*t2[1] ;
    // double px = p2[0]-p1[0] ;
    // double py = p2[1]-p1[1] ;
    // s1 += (py*t2[0] - px*t2[1])/det ;
    // s2 += (t1[0]*py - t1[1]*px)/det ;
    // if ( s1 <= c1.s_min || s1 >= c1.s_max ||
    // s2 <= c2.s_min || s2 >= c2.s_max ) break ;
    // if ( Math.abs(px) <= tolerance ||
    // Math.abs(py) <= tolerance ) return true ;
    // }return false;}
    //
    // void ClothoidCurve::intersect(
    //
    // double offs, ClothoidCurve const&clot,
    //
    // double clot_offs, vector<double>&s1,vector<double>&s2,
    //
    // int max_iter, double tolerance)const
    // {
    // vector<ClothoidCurve> c0, c1;
    // vector<Triangle2D> t0, t1;
    // bbSplit(m_pi / 50, (s_max - s_min) / 3, offs, c0, t0);
    // clot.bbSplit(m_pi / 50, (clot.s_max - clot.s_min) / 3, clot_offs, c1, t1);
    // s1.clear();
    // s2.clear();
    // for (int i = 0; i < int(c0.size()); ++i)
    // {
    // for (int j = 0; j < int(c1.size()); ++j)
    // {
    // if (t0[i].overlap(t1[j]))
    // {
    // // uso newton per cercare intersezione
    // double tmp_s1, tmp_s2;
    // bool ok = intersect_internal(c0[i], offs, tmp_s1, c1[j], clot_offs, tmp_s2, max_iter, tolerance);
    // if (ok)
    // {
    // s1.push_back(tmp_s1);
    // s2.push_back(tmp_s2);
    // }
    // }
    // }
    // }
    // }
    //
    // // collision detection
    // bool
    // ClothoidCurve::approsimate_collision( double offs,
    // ClothoidCurve const & clot,
    // double clot_offs,
    // double max_angle,
    // double max_size ) const {
    // vector<ClothoidCurve> c0, c1 ;
    // vector<Triangle2D> t0, t1 ;
    // bbSplit( max_angle, max_size, offs, c0, t0 ) ;
    // clot.bbSplit( max_angle, max_size, clot_offs, c1, t1 ) ;
    // for ( int i = 0 ; i < int(c0.size()) ; ++i ) {
    // for ( int j = 0 ; j < int(c1.size()) ; ++j ) {
    // if ( t0[i].overlap(t1[j]) ) return true ;
    // }
    // }
    // return false ;
    // }
    //
    // void
    // ClothoidCurve::rotate( double angle, double cx, double cy ) {
    // double dx = x0 - cx ;
    // double dy = y0 - cy ;
    // double C = cos(angle) ;
    // double S = sin(angle) ;
    // double ndx = C*dx - S*dy ;
    // double ndy = C*dy + S*dx ;
    // x0 = cx + ndx ;
    // y0 = cy + ndy ;
    // theta0 += angle ;
    // }
    //
    // void
    // ClothoidCurve::scale( double s ) {
    // k /= s ;
    // dk /= s*s ;
    // s_min *= s ;
    // s_max *= s ;
    // }
    //
    // void
    // ClothoidCurve::reverse() {
    // theta0 = theta0 + m_pi ;
    // if ( theta0 > m_pi ) theta0 -= 2*m_pi ;
    // k = -k ;
    // double tmp = s_max ;
    // s_max = -s_min ;
    // s_min = -tmp ;
    // }
    //
    // std::ostream&operator<<(std::ostream&stream,ClothoidCurve const&c)
    //
    // {stream<<"x0 = "<<c.x0<<"\ny0 = "<<c.y0<<"\ntheta0 = "<<c.theta0<<"\nk = "<<c.k<<"\ndk = "<<c.dk<<"\nL =
    // "<<c.s_max-c.s_min<<"\ns_min = "<<c.s_min<<"\ns_max = "<<c.s_max<<"\n";return stream;}
    //
    // static inline bool
    //
    // power2( double a )
    // { return a*a ; }
    //
    // // **************************************************************************
    //
    // static
    // inline
    // bool
    //
    // solve2x2( double const b[2],
    // double A[2][2],
    // double x[2] ) {
    //// full pivoting
    // int ij = 0 ;
    // double Amax = Math.abs(A[0][0]) ;
    // double tmp = Math.abs(A[0][1]) ;
    // if ( tmp > Amax ) { ij = 1 ; Amax = tmp ; }
    // tmp = Math.abs(A[1][0]) ;
    // if ( tmp > Amax ) { ij = 2 ; Amax = tmp ; }
    // tmp = Math.abs(A[1][1]) ;
    // if ( tmp > Amax ) { ij = 3 ; Amax = tmp ; }
    // if ( Amax == 0 ) return false ;
    // int i[] = { 0, 1 } ;
    // int j[] = { 0, 1 } ;
    // if ( (ij&0x01) == 0x01 ) { j[0] = 1 ; j[1] = 0 ; }
    // if ( (ij&0x02) == 0x02 ) { i[0] = 1 ; i[1] = 0 ; }
    //// apply factorization
    // A[i[1]][j[0]] /= A[i[0]][j[0]] ;
    // A[i[1]][j[1]] -= A[i[1]][j[0]]*A[i[0]][j[1]] ;
    //// check for singularity
    // double epsi = 1e-10 ;
    // if ( Math.abs( A[i[1]][j[1]] ) < epsi ) {
    //// L^+ Pb
    // double tmp = (b[i[0]] + A[i[1]][j[0]]*b[i[1]]) /
    // ( (1+power2(A[i[1]][j[0]]) ) *
    // ( power2(A[i[0]][j[0]])+power2(A[i[0]][j[1]]) ) ) ;
    // x[j[0]] = tmp*A[i[0]][j[0]] ;
    // x[j[1]] = tmp*A[i[0]][j[1]] ;
    // } else { // non singular
    //// L^(-1) Pb
    // x[j[0]] = b[i[0]] ;
    // x[j[1]] = b[i[1]]-A[i[1]][j[0]]*x[j[0]] ;
    //// U^(-1) x
    // x[j[1]] /= A[i[1]][j[1]] ;
    // x[j[0]] = (x[j[0]]-A[i[0]][j[1]]*x[j[1]])/A[i[0]][j[0]] ;
    // }
    // return true ;
    // }
    //
    //// **************************************************************************
    //
    // void
    // G2data::setup( double _x0,
    // double _y0,
    // double _theta0,
    // double _kappa0,
    // double _x1,
    // double _y1,
    // double _theta1,
    // double _kappa1 ) {
    //
    // x0 = _x0 ;
    // y0 = _y0 ;
    // theta0 = _theta0;
    // kappa0 = _kappa0 ;
    // x1 = _x1 ;
    // y1 = _y1 ;
    // theta1 = _theta1 ;
    // kappa1 = _kappa1 ;
    //
    //// scale problem
    // double dx = x1 - x0 ;
    // double dy = y1 - y0 ;
    // phi = atan2( dy, dx ) ;
    // Lscale = 2/hypot( dx, dy ) ;
    //
    // th0 = theta0 - phi ;
    // th1 = theta1 - phi ;
    //
    // k0 = kappa0/Lscale ;
    // k1 = kappa1/Lscale ;
    //
    // DeltaK = k1 - k0 ;
    // DeltaTheta = th1 - th0 ;
    // }
    //
    // void
    // G2data::setTolerance( double tol ) {
    // CLOTHOID_ASSERT( tol > 0 && tol <= 0.1,
    // "setTolerance, tolerance = " << tol << " must be in (0,0.1]" ) ;
    // tolerance = tol ;
    // }
    //
    // void
    // G2data::setMaxIter( int miter ) {
    // CLOTHOID_ASSERT( miter > 0 && miter <= 1000,
    // "setMaxIter, maxIter = " << miter << " must be in [1,1000]" ) ;
    // maxIter = miter ;
    // }
    //
    // // **************************************************************************
    //
    // void
    // G2solve2arc::evalA( double alpha,
    // double L,
    // double & A,
    // double & A_1,
    // double & A_2 ) const {
    // double K = k0+k1 ;
    // double aK = alpha*DeltaK ;
    // A = alpha*(L*(aK-K)+2*DeltaTheta) ;
    // A_1 = (2*aK-K)*L+2*DeltaTheta;
    // A_2 = alpha*(aK-K) ;
    // }
    //
    // void
    // G2solve2arc::evalG( double alpha,
    // double L,
    // double th,
    // double k,
    // double G[2],
    // double G_1[2],
    // double G_2[2] ) const {
    //
    // double A, A_1, A_2, X[3], Y[3] ;
    // evalA( alpha, L, A, A_1, A_2 ) ;
    // double ak = alpha*k ;
    // double Lk = L*k ;
    // GeneralizedFresnelCS( 3, A, ak*L, th, X, Y );
    //
    // G[0] = alpha*X[0] ;
    // G_1[0] = X[0]-alpha*(Y[2]*A_1/2+Y[1]*Lk) ;
    // G_2[0] = -alpha*(Y[2]*A_2/2+Y[1]*ak) ;
    //
    // G[1] = alpha*Y[0] ;
    // G_1[1] = Y[0]+alpha*(X[2]*A_1/2+X[1]*Lk) ;
    // G_2[1] = alpha*(X[2]*A_2/2+X[1]*ak) ;
    //
    // }
    //
    // void
    // G2solve2arc::evalFJ( double const vars[2],
    // double F[2],
    // double J[2][2] ) const {
    //
    // double alpha = vars[0] ;
    // double L = vars[1] ;
    // double G[2], G_1[2], G_2[2] ;
    //
    // evalG( alpha, L, th0, k0, G, G_1, G_2 ) ;
    //
    // F[0] = G[0] - 2/L ; F[1] = G[1] ;
    // J[0][0] = G_1[0] ; J[1][0] = G_1[1] ;
    // J[0][1] = G_2[0] + 2/(L*L) ; J[1][1] = G_2[1] ;
    //
    // evalG( alpha-1, L, th1, k1, G, G_1, G_2 ) ;
    // F[0] -= G[0] ; F[1] -= G[1] ;
    // J[0][0] -= G_1[0] ; J[1][0] -= G_1[1] ;
    // J[0][1] -= G_2[0] ; J[1][1] -= G_2[1] ;
    // }
    //
    //// ---------------------------------------------------------------------------
    //
    // bool
    // G2solve2arc::solve() {
    // double X[2] = { 0.5, 2 } ;
    // bool converged = false ;
    // for ( int i = 0 ; i < maxIter && !converged ; ++i ) {
    // double F[2], J[2][2], d[2] ;
    // evalFJ( X, F, J ) ;
    // if ( !solve2x2( F, J, d ) ) break ;
    // double lenF = hypot(F[0],F[1]) ;
    // X[0] -= d[0] ;
    // X[1] -= d[1] ;
    // converged = lenF < tolerance ;
    // }
    // if ( converged ) converged = X[1] > 0 && X[0] > 0 && X[0] < 1 ;
    // if ( converged ) buildSolution( X[0], X[1] ) ;
    // return converged ;
    // }
    //
    // // **************************************************************************
    //
    // void
    // G2solve2arc::buildSolution( double alpha, double L ) {
    // double beta = 1-alpha ;
    // double LL = L/Lscale ;
    // double s0 = LL*alpha ;
    // double s1 = LL*beta ;
    //
    // double tmp = k0*alpha+k1*beta-2*DeltaTheta/L ;
    //
    // double dk0 = -Lscale*(k0+tmp)/s0 ;
    // double dk1 = Lscale*(k1+tmp)/s1 ;
    //
    // S0.setup( x0, y0, theta0, kappa0, dk0, 0, s0 ) ;
    // S1.setup( x1, y1, theta1, kappa1, dk1, -s1, 0 ) ;
    // S1.change_origin( -s1 ) ;
    // }
    //
    // // **************************************************************************
    //
    // void
    // G2solve3arc::setup( double _x0,
    // double _y0,
    // double _theta0,
    // double _kappa0,
    // double _frac0,
    // double _x1,
    // double _y1,
    // double _theta1,
    // double _kappa1,
    // double _frac1 ) {
    // G2data::setup( _x0, _y0, _theta0, _kappa0, _x1, _y1, _theta1, _kappa1 ) ;
    //
    // double tmp = 1/(2-(_frac0+_frac1)) ;
    // alpha = _frac0*tmp ;
    // beta = _frac1*tmp ;
    //
    // gamma = (1-alpha-beta)/4 ;
    // gamma2 = gamma*gamma ;
    //
    // a0 = alpha*k0 ;
    // b1 = beta*k1 ;
    //
    // double ab = alpha-beta ;
    //
    // dK0_0 = 2*alpha*DeltaTheta ;
    // dK0_1 = -alpha*(k0+a0+b1) ;
    // dK0_2 = -alpha*gamma*(beta-alpha+1) ;
    //
    // dK1_0 = -2*beta*DeltaTheta ;
    // dK1_1 = beta*(k1+a0+b1) ;
    // dK1_2 = beta*gamma*(beta-alpha-1) ;
    //
    // KM_0 = 2*gamma*DeltaTheta ;
    // KM_1 = -gamma*(a0+b1) ;
    // KM_2 = gamma2*(alpha-beta) ;
    //
    // thM_0 = (ab*DeltaTheta+(th0+th1))/2 ;
    // thM_1 = (a0-b1-ab*(a0+b1))/4 ;
    // thM_2 = (gamma*(2*ab*ab-alpha-beta-1))/8 ;
    // }
    //
    // void
    // G2solve3arc::evalFJ( double const vars[2],
    // double F[2],
    // double J[2][2] ) const {
    //
    // double eta = vars[0] ;
    // double zeta = vars[1] ;
    //
    // double dK0 = dK0_0 + eta*dK0_1 + zeta*dK0_2 ;
    // double dK1 = dK1_0 + eta*dK1_1 + zeta*dK1_2 ;
    // double KM = KM_0 + eta*KM_1 + zeta*KM_2 ;
    // double thM = thM_0 + eta*thM_1 + zeta*thM_2 ;
    //
    // double xa[3], ya[3], xb[3], yb[3], xM[3], yM[3], xP[3], yP[3] ;
    //
    // GeneralizedFresnelCS( 3, dK0, a0*eta, th0, xa, ya );
    // GeneralizedFresnelCS( 3, dK1, -b1*eta, th1, xb, yb );
    // GeneralizedFresnelCS( 3, gamma2*zeta, -KM, thM, xM, yM );
    // GeneralizedFresnelCS( 3, gamma2*zeta, KM, thM, xP, yP );
    //
    // F[0] = alpha*xa[0] + beta*xb[0] + gamma*(xM[0]+xP[0]) - 2/eta ;
    // F[1] = alpha*ya[0] + beta*yb[0] + gamma*(yM[0]+yP[0]) ;
    //
    //// D F[0] / D eta
    // J[0][0] = - alpha*(ya[2]*dK0_1/2+ya[1]*a0)
    // - beta*(yb[2]*dK1_1/2-yb[1]*b1)
    // + gamma * ((yM[1]-yP[1])*KM_1-(yM[0]+yP[0])*thM_1)
    // + 2/(eta*eta) ;
    //
    //// D F[0] / D zeta
    // J[0][1] = - alpha*(ya[2]*dK0_2/2) - beta*(yb[2]*dK1_2/2)
    // - gamma*( (yM[2]+yP[2])*gamma2/2+(yP[1]-yM[1])*KM_2+(yP[0]+yM[0])*thM_2 ) ;
    //
    //// D F[1] / D eta
    // J[1][0] = alpha*(xa[2]*dK0_1/2+xa[1]*a0) +
    // beta*(xb[2]*dK1_1/2-xb[1]*b1) +
    // gamma * ((xP[1]-xM[1])*KM_1+(xM[0]+xP[0])*thM_1) ;
    //
    //// D F[1] / D zeta
    // J[1][1] = alpha*(xa[2]*dK0_2/2) + beta*(xb[2]*dK1_2/2)
    // + gamma * ( (xM[2]+xP[2])*gamma2/2+(xP[1]-xM[1])*KM_2+(xP[0]+xM[0])*thM_2 ) ;
    //
    // }
    //
    //// ---------------------------------------------------------------------------
    //
    // bool
    // G2solve3arc::solve() {
    // double X[2] = { 2, 0 } ; // eta, zeta
    // bool converged = false ;
    // for ( int i = 0 ; i < maxIter && !converged ; ++i ) {
    // double F[2], J[2][2], d[2] ;
    // evalFJ( X, F, J ) ;
    // if ( !solve2x2( F, J, d ) ) break ;
    // double lenF = hypot(F[0],F[1]) ;
    // X[0] -= d[0] ;
    // X[1] -= d[1] ;
    // converged = lenF < tolerance ;
    // }
    // if ( converged ) converged = X[0] > 0 ; // eta > 0 !
    // if ( converged ) buildSolution( X[0], X[1] ) ;
    // return converged ;
    // }
    //
    // void
    // G2solve3arc::buildSolution( double eta, double zeta ) {
    //
    // double L0 = eta*alpha ;
    // double L1 = eta*beta ;
    // double LM = eta*gamma ;
    //
    // double dkappaM = zeta*gamma2 ; // /(eta*eta)*LM*LM ;
    // double dkappa0A = dK0_0 + eta*dK0_1 + zeta*dK0_2 ;
    // double dkappa1B = dK1_0 + eta*dK1_1 + zeta*dK1_2 ;
    // double kappaM = KM_0 + eta*KM_1 + zeta*KM_2 ;
    // double thetaM = thM_0 + eta*thM_1 + zeta*thM_2 ;
    //
    // double xa, ya, xmL, ymL ;
    // GeneralizedFresnelCS( dkappa0A, k0*L0, th0, xa, ya );
    // GeneralizedFresnelCS( dkappaM, -kappaM, thetaM, xmL, ymL );
    //
    // double xM = L0 * xa + LM * xmL - 1 ;
    // double yM = L0 * ya + LM * ymL ;
    //
    //// rovescia scalatura
    // L0 /= Lscale ;
    // L1 /= Lscale ;
    // LM /= Lscale ;
    //
    // dkappa0A /= L0*L0 ;
    // dkappa1B /= L1*L1 ;
    // dkappaM /= LM*LM ;
    // kappaM /= LM ;
    //
    // S0.setup( x0, y0, theta0, kappa0, dkappa0A, 0, L0 ) ;
    // S1.setup( x1, y1, theta1, kappa1, dkappa1B, -L1, 0 ) ;
    //
    //// la trasformazione inversa da [-1,1] a (x0,y0)-(x1,y1)
    //// g(x,y) = RotInv(phi)*(1/lambda*[X;Y] - [xbar;ybar]) = [x;y]
    //
    // double C = cos(phi) ;
    // double S = sin(phi) ;
    // double dx = (xM+1)/Lscale ;
    // double dy = yM/Lscale ;
    // SM.setup( x0 + C * dx - S * dy, y0 + C * dy + S * dx,
    // thetaM+phi, kappaM, dkappaM, -LM, LM ) ;
    //
    //// Sguess.setup_G1( x0_orig, y0_orig, theta0_orig,
    //// x1_orig, y1_orig, theta1_orig ) ;
    //
    // S1.change_origin( -L1 ) ;
    // SM.change_origin( -LM ) ;
    // }
    //
    // }
}
