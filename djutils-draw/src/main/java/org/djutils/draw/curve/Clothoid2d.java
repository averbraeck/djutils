package org.djutils.draw.curve;

import org.djutils.base.AngleUtil;
import org.djutils.draw.line.PolyLine2d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.Point2d;
import org.djutils.exceptions.Throw;
import org.djutils.exceptions.Try;

/**
 * Continuous definition of a clothoid in 2d. The following definitions are available:
 * <ul>
 * <li>A clothoid between two <code>DirectedPoint2d</code>s.</li>
 * <li>A clothoid originating from a <code>DirectedPoint2d</code> with start curvature, end curvature, and <code>length</code>
 * specified.</li>
 * <li>A clothoid originating from a <code>DirectedPoint2d</code> with start curvature, end curvature, and <code>A-value</code>
 * specified.</li>
 * </ul>
 * This class is based on:
 * <ul>
 * <li>Dale Connor and Lilia Krivodonova (2014) "Interpolation of two-dimensional curves with Euler spirals", Journal of
 * Computational and Applied Mathematics, Volume 261, 1 May 2014, pp. 320-332.</li>
 * <li>D.J. Waltona and D.S. Meek (2009) "G<sup>1</sup> interpolation with a single Cornu spiral segment", Journal of
 * Computational and Applied Mathematics, Volume 223, Issue 1, 1 January 2009, pp. 86-96.</li>
 * </ul>
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://www.sciencedirect.com/science/article/pii/S0377042713006286">Connor and Krivodonova (2014)</a>
 * @see <a href="https://www.sciencedirect.com/science/article/pii/S0377042704000925">Waltona and Meek (2009)</a>
 */
public class Clothoid2d implements Curve2d, OffsetCurve2d
{

    /** Threshold to consider input to be a trivial straight or circle arc. The value is 1/10th of a degree. */
    private static final double ANGLE_TOLERANCE = 2.0 * Math.PI / 3600.0;

    /** Stopping tolerance for the Secant method to find optimal theta values. */
    private static final double SECANT_TOLERANCE = 1e-8;

    /** Start point with direction. */
    private final DirectedPoint2d startPoint;

    /** End point with direction. */
    private final DirectedPoint2d endPoint;

    /** Start curvature. */
    private final double startCurvature;

    /** End curvature. */
    private final double endCurvature;

    /** Length. */
    private final double length;

    /**
     * A-value; for scaling the Fresnel integral. The regular clothoid A-parameter is obtained by dividing by
     * {@code Math.sqrt(Math.PI)}.
     */
    private final double a;

    /** Minimum alpha value of line to draw. */
    private final double alphaMin;

    /** Maximum alpha value of line to draw. */
    private final double alphaMax;

    /** Unit vector from the origin of the clothoid, towards the positive side. */
    private final double[] t0;

    /** Normal unit vector to t0. */
    private final double[] n0;

    /** Whether the line needs to be flipped. */
    private final boolean opposite;

    /** Whether the line is reflected. */
    private final boolean reflected;

    /** Simplification to straight when valid. */
    private final Straight2d straight;

    /** Simplification to arc when valid. */
    private final Arc2d arc;

    /** Whether the shift was determined. */
    private boolean shiftDetermined;

    /** Shift in x-coordinate of start point. */
    private double shiftX;

    /** Shift in y-coordinate of start point. */
    private double shiftY;

    /** Additional shift in x-coordinate towards end point. */
    private double dShiftX;

    /** Additional shift in y-coordinate towards end point. */
    private double dShiftY;

    /**
     * Create clothoid between two directed points. This constructor is based on the procedure in:<br>
     * Dale Connor and Lilia Krivodonova (2014) "Interpolation of two-dimensional curves with Euler spirals", Journal of
     * Computational and Applied Mathematics, Volume 261, 1 May 2014, pp. 320-332.<br>
     * Which applies the theory proven in:<br>
     * D.J. Waltona and D.S. Meek (2009) "G<sup>1</sup> interpolation with a single Cornu spiral segment", Journal of
     * Computational and Applied Mathematics, Volume 223, Issue 1, 1 January 2009, pp. 86-96.<br>
     * This procedure guarantees that the resulting line has the minimal angle rotation that is required to connect the points.
     * If the points approximate a straight line or circle, with a tolerance of up 1/10th of a degree, those respective lines
     * are created. The numerical approximation of the underlying Fresnel integral is different from the paper. See
     * {@code Clothoid.fresnel()}.
     * @param startPoint start point
     * @param endPoint end point
     * @throws NullPointerException when <code>startPoint</code>, or <code>endPoint</code> is <code>null</code>
     * @see <a href="https://www.sciencedirect.com/science/article/pii/S0377042713006286">Connor and Krivodonova (2014)</a>
     * @see <a href="https://www.sciencedirect.com/science/article/pii/S0377042704000925">Waltona and Meek (2009)</a>
     */
    public Clothoid2d(final DirectedPoint2d startPoint, final DirectedPoint2d endPoint)
    {
        Throw.whenNull(startPoint, "startPoint");
        Throw.whenNull(endPoint, "endPoint");
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        double dx = endPoint.x - startPoint.x;
        double dy = endPoint.y - startPoint.y;
        double d2 = Math.hypot(dx, dy); // length of straight line from start to end
        double d = Math.atan2(dy, dx); // angle of line through start and end points

        double phi1 = AngleUtil.normalizeAroundZero(d - startPoint.dirZ);
        double phi2 = AngleUtil.normalizeAroundZero(endPoint.dirZ - d);
        double phi1Abs = Math.abs(phi1);
        double phi2Abs = Math.abs(phi2);

        if (phi1Abs < ANGLE_TOLERANCE && phi2Abs < ANGLE_TOLERANCE)
        {
            // Straight
            this.length = Math.hypot(endPoint.x - startPoint.x, endPoint.y - startPoint.y);
            this.a = Double.POSITIVE_INFINITY;
            this.startCurvature = 0.0;
            this.endCurvature = 0.0;
            this.straight = new Straight2d(startPoint, this.length);
            this.arc = null;
            this.alphaMin = 0.0;
            this.alphaMax = 0.0;
            this.t0 = null;
            this.n0 = null;
            this.opposite = false;
            this.reflected = false;
            return;
        }
        else if (Math.abs(phi2 - phi1) < ANGLE_TOLERANCE)
        {
            // Arc
            double r = .5 * d2 / Math.sin(phi1);
            double cosStartDirection = Math.cos(startPoint.dirZ);
            double sinStartDirection = Math.sin(startPoint.dirZ);
            double ang = Math.PI / 2.0;
            double cosAng = Math.cos(ang); // =0
            double sinAng = Math.sin(ang); // =1
            double x0 = startPoint.x - r * (cosStartDirection * cosAng + sinStartDirection * sinAng);
            double y0 = startPoint.y - r * (cosStartDirection * -sinAng + sinStartDirection * cosAng);
            double from = Math.atan2(startPoint.y - y0, startPoint.x - x0);
            double to = Math.atan2(endPoint.y - y0, endPoint.x - x0);
            if (r < 0 && to > from)
            {
                to = to - 2.0 * Math.PI;
            }
            else if (r > 0 && to < from)
            {
                to = to + 2.0 * Math.PI;
            }
            double angle = Math.abs(to - from);
            this.length = angle * Math.abs(r);
            this.a = 0.0;
            this.startCurvature = 1.0 / r;
            this.endCurvature = 1.0 / r;
            this.straight = null;
            this.arc = new Arc2d(startPoint, Math.abs(r), r > 0.0, angle);
            this.alphaMin = 0.0;
            this.alphaMax = 0.0;
            this.t0 = null;
            this.n0 = null;
            this.opposite = false;
            this.reflected = false;
            return;
        }
        this.straight = null;
        this.arc = null;

        // The algorithm assumes |phi2| to be larger than |phi1|. If this is not the case, the clothoid is created in the
        // opposite direction.
        if (phi2Abs < phi1Abs)
        {
            this.opposite = true;
            double phi3 = phi1;
            phi1 = -phi2;
            phi2 = -phi3;
            dx = -dx;
            dy = -dy;
        }
        else
        {
            this.opposite = false;
        }

        // The algorithm assumes 0 < phi2 < pi. If this is not the case, the input and output are reflected on 'd'.
        this.reflected = phi2 < 0 || phi2 > Math.PI;
        if (this.reflected)
        {
            phi1 = -phi1;
            phi2 = -phi2;
        }

        // h(phi1, phi2) guarantees for negative values along with 0 < phi1 < phi2 < pi, that a C-shaped clothoid exists.
        double[] cs = Fresnel.fresnel(alphaToT(phi1 + phi2));
        double h = cs[1] * Math.cos(phi1) - cs[0] * Math.sin(phi1);
        boolean cShape = 0 < phi1 && phi1 < phi2 && phi2 < Math.PI && h < 0; // otherwise, S-shape
        double theta = getTheta(phi1, phi2, cShape);
        double aSign = cShape ? -1.0 : 1.0;
        double thetaSign = -aSign;

        double v1 = theta + phi1 + phi2;
        double v2 = theta + phi1;
        double[] cs0 = Fresnel.fresnel(alphaToT(theta));
        double[] cs1 = Fresnel.fresnel(alphaToT(v1));
        this.a = d2 / ((cs1[1] + aSign * cs0[1]) * Math.sin(v2) + (cs1[0] + aSign * cs0[0]) * Math.cos(v2));

        dx /= d2; // normalized
        dy /= d2;
        if (this.reflected)
        {
            // reflect t0 and n0 on 'd' so that the created output clothoid is reflected back after input was reflected
            this.t0 = new double[] {Math.cos(-v2) * dx + Math.sin(-v2) * dy, -Math.sin(-v2) * dx + Math.cos(-v2) * dy};
            this.n0 = new double[] {-this.t0[1], this.t0[0]};
        }
        else
        {
            this.t0 = new double[] {Math.cos(v2) * dx + Math.sin(v2) * dy, -Math.sin(v2) * dx + Math.cos(v2) * dy};
            this.n0 = new double[] {this.t0[1], -this.t0[0]};
        }

        this.alphaMin = thetaSign * theta;
        this.alphaMax = v1; // alphaMax = theta + phi1 + phi2, which is v1
        double sign = (this.reflected ? -1.0 : 1.0);
        double curveMin = Math.PI * alphaToT(this.alphaMin) / this.a;
        double curveMax = Math.PI * alphaToT(v1) / this.a;
        this.startCurvature = sign * (this.opposite ? -curveMax : curveMin);
        this.endCurvature = sign * (this.opposite ? -curveMin : curveMax);
        this.length = this.a * (alphaToT(v1) - alphaToT(this.alphaMin));
    }

    /**
     * Create clothoid from one point based on curvature and A-value.
     * @param startPoint start point
     * @param a A-value
     * @param startCurvature start curvature
     * @param endCurvature end curvature
     * @throws NullPointerException when <code>startPoint</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>a &le; 0.0</code>
     */
    public Clothoid2d(final DirectedPoint2d startPoint, final double a, final double startCurvature, final double endCurvature)
    {
        Throw.whenNull(startPoint, "startPoint");
        Throw.when(a <= 0.0, IllegalArgumentException.class, "A value must be above 0.");
        this.startPoint = startPoint;
        // Scale 'a', due to parameter conversion between C(alpha)/S(alpha) and C(t)/S(t); t = sqrt(2*alpha/pi).
        this.a = a * Math.sqrt(Math.PI);
        this.length = a * a * Math.abs(endCurvature - startCurvature);
        this.startCurvature = startCurvature;
        this.endCurvature = endCurvature;

        double l1 = a * a * startCurvature;
        double l2 = a * a * endCurvature;
        this.alphaMin = Math.abs(l1) * startCurvature / 2.0;
        this.alphaMax = Math.abs(l2) * endCurvature / 2.0;

        double ang = AngleUtil.normalizeAroundZero(startPoint.dirZ) - Math.abs(this.alphaMin);
        this.t0 = new double[] {Math.cos(ang), Math.sin(ang)};
        this.n0 = new double[] {this.t0[1], -this.t0[0]};
        double endDirection = ang + Math.abs(this.alphaMax);
        if (startCurvature > endCurvature)
        {
            // In these cases the algorithm works in the negative direction. We need to flip over the line through the start
            // point that runs perpendicular to the start direction.
            double m = Math.tan(startPoint.dirZ + Math.PI / 2.0);

            // Linear algebra flipping, see: https://math.stackexchange.com/questions/525082/reflection-across-a-line
            double onePlusMm = 1.0 + m * m;
            double oneMinusMm = 1.0 - m * m;
            double mmMinusOne = m * m - 1.0;
            double twoM = 2.0 * m;
            double t00 = this.t0[0];
            double t01 = this.t0[1];
            double n00 = this.n0[0];
            double n01 = this.n0[1];
            this.t0[0] = (oneMinusMm * t00 + 2 * m * t01) / onePlusMm;
            this.t0[1] = (twoM * t00 + mmMinusOne * t01) / onePlusMm;
            this.n0[0] = (oneMinusMm * n00 + 2 * m * n01) / onePlusMm;
            this.n0[1] = (twoM * n00 + mmMinusOne * n01) / onePlusMm;

            double ang2 = Math.atan2(this.t0[1], this.t0[0]);
            endDirection = ang2 - Math.abs(this.alphaMax) + Math.PI;
        }
        PolyLine2d line = toPolyLine(new Flattener2d.NumSegments(1));
        Point2d end = Try.assign(() -> line.get(line.size() - 1), "Line does not have an end point.");
        this.endPoint = new DirectedPoint2d(end.x, end.y, endDirection);

        // Fields not relevant for definition with curvatures
        this.straight = null;
        this.arc = null;
        this.opposite = false;
        this.reflected = false;
    }

    /**
     * Create clothoid from one point based on curvature and length. This method calculates the A-value as
     * <i>sqrt(L/|k2-k1|)</i>, where <i>L</i> is the length of the resulting clothoid, and <i>k2</i> and <i>k1</i> are the end
     * and start curvature.
     * @param startPoint start point.
     * @param length Length of the resulting clothoid.
     * @param startCurvature start curvature.
     * @param endCurvature end curvature;
     * @return clothoid based on curvature and length.
     * @throws NullPointerException when <code>startPoint</code> is <code>null</code>
     * @throws IllegalArgumentException when <code>length &le; 0.0</code>
     */
    public static Clothoid2d withLength(final DirectedPoint2d startPoint, final double length, final double startCurvature,
            final double endCurvature)
    {
        Throw.when(length <= 0.0, IllegalArgumentException.class, "Length must be above 0.");
        double a = Math.sqrt(length / Math.abs(endCurvature - startCurvature));
        return new Clothoid2d(startPoint, a, startCurvature, endCurvature);
    }

    /**
     * Performs alpha to t variable change.
     * @param alpha alpha value, must be positive
     * @return t value (length along the Fresnel integral, also known as x)
     */
    private static double alphaToT(final double alpha)
    {
        return alpha >= 0 ? Math.sqrt(alpha * 2.0 / Math.PI) : -Math.sqrt(-alpha * 2.0 / Math.PI);
    }

    /**
     * Returns theta value given shape to use. If no such value is found, the other shape may be attempted.
     * @param phi1 phi1.
     * @param phi2 phi2.
     * @param cShape C-shaped, or S-shaped otherwise.
     * @return theta value; the number of radians that is moved on to a side of the full clothoid.
     */
    private static double getTheta(final double phi1, final double phi2, final boolean cShape)
    {
        double sign, phiMin, phiMax;
        if (cShape)
        {
            double lambda = (1 - Math.cos(phi1)) / (1 - Math.cos(phi2));
            phiMin = 0.0;
            phiMax = (lambda * lambda * (phi1 + phi2)) / (1 - (lambda * lambda));
            sign = -1.0;
        }
        else
        {
            phiMin = Math.max(0, -phi1);
            phiMax = Math.PI / 2 - phi1;
            sign = 1;
        }

        double fMin = fTheta(phiMin, phi1, phi2, sign);
        double fMax = fTheta(phiMax, phi1, phi2, sign);
        if (fMin * fMax > 0)
        {
            throw new IllegalArgumentException(
                    "f(phiMin) and f(phiMax) have the same sign, we cant find f(theta) = 0 between them.");
        }

        // Find optimum using Secant method, see https://en.wikipedia.org/wiki/Secant_method
        double x0 = phiMin;
        double x1 = phiMax;
        double x2 = 0;
        for (int i = 0; i < 100; i++) // max 100 iterations, otherwise use latest x2 value
        {
            double f1 = fTheta(x1, phi1, phi2, sign);
            x2 = x1 - f1 * (x1 - x0) / (f1 - fTheta(x0, phi1, phi2, sign));
            x2 = Math.max(Math.min(x2, phiMax), phiMin); // this line is an essential addition to keep the algorithm at bay
            x0 = x1;
            x1 = x2;
            if (Math.abs(x0 - x1) < SECANT_TOLERANCE || Math.abs(x0 / x1 - 1) < SECANT_TOLERANCE
                    || Math.abs(f1) < SECANT_TOLERANCE)
            {
                return x2;
            }
        }

        return x2;
    }

    /**
     * Function who's solution <i>f</i>(<i>theta</i>) = 0 for the given value of <i>phi1</i> and <i>phi2</i> gives the angle
     * that solves fitting a C-shaped clothoid through two points. This assumes that <i>sign</i> = -1. If <i>sign</i> = 1, this
     * changes to <i>g</i>(<i>theta</i>) = 0 being a solution for an S-shaped clothoid.
     * @param theta angle defining the curvature of the resulting clothoid.
     * @param phi1 angle between the line through both end points, and the direction of the first point.
     * @param phi2 angle between the line through both end points, and the direction of the last point.
     * @param sign 1 for C-shaped, -1 for S-shaped.
     * @return <i>f</i>(<i>theta</i>) for <i>sign</i> = -1, or <i>g</i>(<i>theta</i>) for <i>sign</i> = 1.
     */
    private static double fTheta(final double theta, final double phi1, final double phi2, final double sign)
    {
        double thetaPhi1 = theta + phi1;
        double[] cs0 = Fresnel.fresnel(alphaToT(theta));
        double[] cs1 = Fresnel.fresnel(alphaToT(thetaPhi1 + phi2));
        return (cs1[1] + sign * cs0[1]) * Math.cos(thetaPhi1) - (cs1[0] + sign * cs0[0]) * Math.sin(thetaPhi1);
    }

    @Override
    public DirectedPoint2d getStartPoint()
    {
        return this.startPoint;
    }

    @Override
    public DirectedPoint2d getEndPoint()
    {
        return this.endPoint;
    }

    /**
    * Start curvature of this Clothoid.
    * @return start curvature of this Clothoid
    */
    public double getStartCurvature()
    {
        return this.startCurvature;
    }

    /**
    * End curvature of this Clothoid.
    * @return end curvature of this Clothoid
    */
    public double getEndCurvature()
    {
        return this.endCurvature;
    }

     /**
     * Start radius of this Clothoid.
     * @return start radius of this Clothoid
     */
    public double getStartRadius()
    {
        return 1.0 / this.startCurvature;
    }

     /**
     * End radius of this Clothoid.
     * @return end radius of this Clothoid
     */
    public double getEndRadius()
    {
        return 1.0 / this.endCurvature;
    }

    /**
     * Return A, the clothoid scaling parameter.
     * @return a, the clothoid scaling parameter.
     */
    public double getA()
    {
        // Scale 'a', due to parameter conversion between C(alpha)/S(alpha) and C(t)/S(t); t = sqrt(2*alpha/pi).
        // The value of 'this.a' is used when scaling the Fresnel integral, which is why this is stored.
        return this.a / Math.sqrt(Math.PI);
    }

    /**
     * Calculates shifts if these have not yet been calculated.
     */
    private void assureShift()
    {
        if (this.shiftDetermined)
        {
            return;
        }

        DirectedPoint2d p1 = this.opposite ? this.endPoint : this.startPoint;
        DirectedPoint2d p2 = this.opposite ? this.startPoint : this.endPoint;

        // Create first point to figure out the required overall shift
        double[] csMin = Fresnel.fresnel(alphaToT(this.alphaMin));
        double xMin = this.a * (csMin[0] * this.t0[0] - csMin[1] * this.n0[0]);
        double yMin = this.a * (csMin[0] * this.t0[1] - csMin[1] * this.n0[1]);
        this.shiftX = p1.x - xMin;
        this.shiftY = p1.y - yMin;

        // Due to numerical precision, we linearly scale over alpha such that the final point is exactly on p2
        if (p2 != null)
        {
            double[] csMax = Fresnel.fresnel(alphaToT(this.alphaMax));
            double xMax = this.a * (csMax[0] * this.t0[0] - csMax[1] * this.n0[0]);
            double yMax = this.a * (csMax[0] * this.t0[1] - csMax[1] * this.n0[1]);
            this.dShiftX = p2.x - (xMax + this.shiftX);
            this.dShiftY = p2.y - (yMax + this.shiftY);
        }
        else
        {
            this.dShiftX = 0.0;
            this.dShiftY = 0.0;
        }

        this.shiftDetermined = true;
    }

    /**
     * Returns a point on the clothoid at a fraction of curvature along the clothoid.
     * @param fraction fraction of curvature along the clothoid
     * @param offset offset relative to radius
     * @return point on the clothoid at a fraction of curvature along the clothoid
     */
    private Point2d getPoint(final double fraction, final double offset)
    {
        double f = this.opposite ? 1.0 - fraction : fraction;
        double alpha = this.alphaMin + f * (this.alphaMax - this.alphaMin);
        double[] cs = Fresnel.fresnel(alphaToT(alpha));
        double x = this.shiftX + this.a * (cs[0] * this.t0[0] - cs[1] * this.n0[0]) + f * this.dShiftX;
        double y = this.shiftY + this.a * (cs[0] * this.t0[1] - cs[1] * this.n0[1]) + f * this.dShiftY;
        double d = getDirectionForAlpha(alpha) + Math.PI / 2;
        return new Point2d(x + Math.cos(d) * offset, y + Math.sin(d) * offset);
    }

    @Override
    public Point2d getPoint(final double fraction)
    {
        if (this.arc != null)
        {
            return this.arc.getPoint(fraction);
        }
        else if (this.straight != null)
        {
            return this.straight.getPoint(fraction);
        }
        return getPoint(fraction, 0);
    }

    @Override
    public Point2d getPoint(final double fraction, final PieceWiseLinearOffset2d of)
    {
        if (this.arc != null)
        {
            return this.arc.getPoint(fraction, of);
        }
        else if (this.straight != null)
        {
            return this.straight.getPoint(fraction, of);
        }
        return getPoint(fraction, of.get(fraction));
    }

    @Override
    public Double getDirection(final double fraction)
    {
        if (this.arc != null)
        {
            return this.arc.getDirection(fraction);
        }
        else if (this.straight != null)
        {
            return this.straight.getDirection(fraction);
        }
        return getDirectionForAlpha(this.alphaMin + fraction * (this.alphaMax - this.alphaMin));
    }

    /**
     * Returns the direction at given alpha.
     * @param alpha alpha
     * @return direction at given alpha
     */
    private double getDirectionForAlpha(final double alpha)
    {
        double rot = Math.atan2(this.t0[1], this.t0[0]);
        // abs because alpha = -3deg has the same direction as alpha = 3deg in an S-curve where alpha = 0 is the middle
        rot += this.reflected ? -Math.abs(alpha) : Math.abs(alpha);
        if (this.opposite)
        {
            rot += Math.PI;
        }
        return AngleUtil.normalizeAroundZero(rot);
    }

    @Override
    public PolyLine2d toPolyLine(final Flattener2d flattener)
    {
        if (this.straight != null)
        {
            return this.straight.toPolyLine(flattener);
        }
        if (this.arc != null)
        {
            return this.arc.toPolyLine(flattener);
        }
        assureShift();
        return flattener.flatten(this);
    }

    @Override
    public PolyLine2d toPolyLine(final OffsetFlattener2d flattener, final PieceWiseLinearOffset2d offsets)
    {
        Throw.whenNull(offsets, "offsets");
        if (this.straight != null)
        {
            return this.straight.toPolyLine(flattener, offsets);
        }
        if (this.arc != null)
        {
            return this.arc.toPolyLine(flattener, offsets);
        }
        assureShift();
        return flattener.flatten(this, offsets);
    }

    @Override
    public double getLength()
    {
        return this.length;
    }

    /**
     * Returns whether the shape was applied as a Clothoid, an Arc, or as a Straight, depending on start and end position and
     * direction.
     * @return "Clothoid", "Arc" or "Straight"
     */
    public String getAppliedShape()
    {
        return this.straight == null ? (this.arc == null ? "Clothoid" : "Arc") : "Straight";
    }

    @Override
    public String toString()
    {
        return "Clothoid [startPoint=" + this.startPoint + ", endPoint=" + this.endPoint + ", startCurvature="
                + this.startCurvature + ", endCurvature=" + this.endCurvature + ", length=" + this.length + "]";
    }

}

/**
 * Utility class to create clothoid lines, in particular the Fresnel integral based on:
 * <ul>
 * <li>W.J. Cody (1968) Chebyshev approximations for the Fresnel integrals. Mathematics of Computation, Vol. 22, Issue 102, pp.
 * 450–453.</li>
 * </ul>
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 * @see <a href="https://www.ams.org/journals/mcom/1985-44-170/S0025-5718-1985-0777277-6/S0025-5718-1985-0777277-6.pdf">Cody
 *      (1968)</a>
 */
final class Fresnel
{

    // {@formatter:off}
    /** Numerator coefficients to calculate C(t) in region 1. */
    private static final double[] CN1 = new double[] {
            9.999999999999999421E-01,
            -1.994608988261842706E-01, 
            1.761939525434914045E-02,
            -5.280796513726226960E-04, 
            5.477113856826871660E-06
    };

    /** Denominator coefficients to calculate C(t) in region 1. */
    private static final double[] CD1 = new double[] {
            1.000000000000000000E+00,
            4.727921120104532689E-02,
            1.099572150256418851E-03,
            1.552378852769941331E-05,
            1.189389014228757184E-07
    };

    /** Numerator coefficients to calculate C(t) in region 2. */
    private static final double[] CN2 = new double[] {
            1.00000000000111043640E+00,
            -2.07073360335323894245E-01,
            1.91870279431746926505E-02,
            -6.71376034694922109230E-04,
            1.02365435056105864908E-05,
            -5.68293310121870728343E-08
    };

    /** Denominator coefficients to calculate C(t) in region 3. */
    private static final double[] CD2 = new double[] {
            1.00000000000000000000E+00,
            3.96667496952323433510E-02,
            7.88905245052359907842E-04,
            1.01344630866749406081E-05,
            8.77945377892369265356E-08,
            4.41701374065009620393E-10
    };

    /** Numerator coefficients to calculate S(t) in region 1. */
    private static final double[] SN1 = new double[] {
            5.2359877559829887021E-01,
            -7.0748991514452302596E-02,
            3.8778212346368287939E-03,
            -8.4555728435277680591E-05,
            6.7174846662514086196E-07
    };

    /** Denominator coefficients to calculate S(t) in region 1. */
    private static final double[] SD1 = new double[] {
            1.0000000000000000000E+00,
            4.1122315114238422205E-02,
            8.1709194215213447204E-04,
            9.6269087593903403370E-06,
            5.9528122767840998345E-08
    };

    /** Numerator coefficients to calculate S(t) in region 2. */
    private static final double[] SN2 = new double[] {
            5.23598775598344165913E-01,
            -7.37766914010191323867E-02,
            4.30730526504366510217E-03,
            -1.09540023911434994566E-04,
            1.28531043742724820610E-06,
            -5.76765815593088804567E-09
    };

    /** Denominator coefficients to calculate S(t) in region 2. */
    private static final double[] SD2 = new double[] {
            1.00000000000000000000E+00,
            3.53398342167472162540E-02,
            6.18224620195473216538E-04,
            6.87086265718620117905E-06,
            5.03090581246612375866E-08,
            2.05539124458579596075E-10
    };

    /** Numerator coefficients to calculate f(t) in region 3. */
    private static final double[] FN3 = new double[] {
            3.1830975293580985290E-01,
            1.2226000551672961219E+01,
            1.2924886131901657025E+02,
            4.3886367156695547655E+02,
            4.1466722177958961672E+02,
            5.6771463664185116454E+01
    };

    /** Denominator coefficients to calculate f(t) in region 3. */
    private static final double[] FD3 = new double[] {
            1.0000000000000000000E+00,
            3.8713003365583442831E+01,
            4.1674359830705629745E+02,
            1.4740030733966610568E+03,
            1.5371675584895759916E+03,
            2.9113088788847831515E+02
    };

    /** Numerator coefficients to calculate f(t) in region 4. */
    private static final double[] FN4 = new double[] {
            3.183098818220169217E-01,
            1.958839410219691002E+01,
            3.398371349269842400E+02,
            1.930076407867157531E+03,
            3.091451615744296552E+03,
            7.177032493651399590E+02
    };

    /** Denominator coefficients to calculate f(t) in region 4. */
    private static final double[] FD4 = new double[] {
            1.000000000000000000E+00,
            6.184271381728873709E+01,
            1.085350675006501251E+03,
            6.337471558511437898E+03,
            1.093342489888087888E+04,
            3.361216991805511494E+03
    };

    /** Numerator coefficients to calculate f(t) in region 5. */
    private static final double[] FN5 = new double[] {
            -9.675460329952532343E-02,
            -2.431275407194161683E+01,
            -1.947621998306889176E+03,
            -6.059852197160773639E+04,
            -7.076806952837779823E+05,
            -2.417656749061154155E+06,
            -7.834914590078311336E+05
    };

    /** Denominator coefficients to calculate f(t) in region 5. */
    private static final double[] FD5 = new double[] {
            1.000000000000000000E+00,
            2.548289012949732752E+02,
            2.099761536857815105E+04,
            6.924122509827708985E+05,
            9.178823229918143780E+06,
            4.292733255630186679E+07,
            4.803294184260528342E+07
    };

    /** Numerator coefficients to calculate g(t) in region 3. */
    private static final double[] GN3 = new double[] {
            1.013206188102747985E-01,
            4.445338275505123778E+00,
            5.311228134809894481E+01,
            1.991828186789025318E+02,
            1.962320379716626191E+02,
            2.054214324985006303E+01
    };

    /** Denominator coefficients to calculate g(t) in region 3. */
    private static final double[] GD3 = new double[] {
            1.000000000000000000E+00,
            4.539250196736893605E+01,
            5.835905757164290666E+02,
            2.544731331818221034E+03,
            3.481121478565452837E+03,
            1.013794833960028555E+03
    };

    /** Numerator coefficients to calculate g(t) in region 4. */
    private static final double[] GN4 = new double[] {
            1.01321161761804586E-01,
            7.11205001789782823E+00,
            1.40959617911315524E+02,
            9.08311749529593938E+02,
            1.59268006085353864E+03,
            3.13330163068755950E+02
    };

    /** Denominator coefficients to calculate g(t) in region 4. */
    private static final double[] GD4 = new double[] {
            1.00000000000000000E+00,
            7.17128596939302198E+01,
            1.49051922797329229E+03,
            1.06729678030583897E+04,
            2.41315567213369742E+04,
            1.15149832376260604E+04
    };

    /** Numerator coefficients to calculate g(t) in region 5. */
    private static final double[] GN5 = new double[] {
            -1.53989733819769316E-01,
            -4.31710157823357568E+01,
            -3.87754141746378493E+03,
            -1.35678867813756347E+05,
            -1.77758950838029676E+06,
            -6.66907061668636416E+06,
            -1.72590224654836845E+06
    };
    
    /** Denominator coefficients to calculate g(t) in region 5. */
    private static final double[] GD5 = new double[] {
            1.00000000000000000E+00,
            2.86733194975899483E+02,
            2.69183180396242536E+04,
            1.02878693056687506E+06,
            1.62095600500231646E+07,
            9.38695862531635179E+07,
            1.40622441123580005E+08
    };
    // {@formatter:on}

    /** Utility class. */
    private Fresnel()
    {
        // do not instantiate
    }

    /**
     * Approximate the Fresnel integral. The method used is based on Cody (1968). This method applies rational approximation to
     * approximate the clothoid. For clothoid rotation beyond 1.6 rad, this occurs in polar form. The polar form is robust for
     * arbitrary large numbers, unlike polynomial expansion, and will at a large threshold converge to (0.5, 0.5). There are 5
     * regions with different fitted values for the rational approximations, in Cartesian or polar form.<br>
     * <br>
     * W.J. Cody (1968) Chebyshev approximations for the Fresnel integrals. Mathematics of Computation, Vol. 22, Issue 102, pp.
     * 450–453.
     * @param x length along the standard Fresnel integral (no scaling).
     * @return array with two double values c and s
     * @see <a href="https://www.ams.org/journals/mcom/1968-22-102/S0025-5718-68-99871-2/S0025-5718-68-99871-2.pdf">Cody
     *      (1968)</a>
     */
    public static double[] fresnel(final double x)
    {
        final double t = Math.abs(x);
        double cc, ss;
        if (t < 1.2)
        {
            cc = t * ratioEval(t, CN1, +1) / ratioEval(t, CD1, +1);
            ss = t * t * t * ratioEval(t, SN1, +1) / ratioEval(t, SD1, +1);
        }
        else if (t < 1.6)
        {
            cc = t * ratioEval(t, CN2, +1) / ratioEval(t, CD2, +1);
            ss = t * t * t * ratioEval(t, SN2, +1) / ratioEval(t, SD2, +1);
        }
        else if (t < 1.9)
        {
            double pitt2 = Math.PI * t * t / 2;
            double sinpitt2 = Math.sin(pitt2);
            double cospitt2 = Math.cos(pitt2);
            double ft = (1 / t) * ratioEval(t, FN3, -1) / ratioEval(t, FD3, -1);
            double gt = (1 / (t * t * t)) * ratioEval(t, GN3, -1) / ratioEval(t, GD3, -1);
            cc = .5 + ft * sinpitt2 - gt * cospitt2;
            ss = .5 - ft * cospitt2 - gt * sinpitt2;
        }
        else if (t < 2.4)
        {
            double pitt2 = Math.PI * t * t / 2;
            double sinpitt2 = Math.sin(pitt2);
            double cospitt2 = Math.cos(pitt2);
            double tinv = 1 / t;
            double tttinv = tinv * tinv * tinv;
            double ft = tinv * ratioEval(t, FN4, -1) / ratioEval(t, FD4, -1);
            double gt = tttinv * ratioEval(t, GN4, -1) / ratioEval(t, GD4, -1);
            cc = .5 + ft * sinpitt2 - gt * cospitt2;
            ss = .5 - ft * cospitt2 - gt * sinpitt2;
        }
        else
        {
            double pitt2 = Math.PI * t * t / 2;
            double sinpitt2 = Math.sin(pitt2);
            double cospitt2 = Math.cos(pitt2);
            double piinv = 1 / Math.PI;
            double tinv = 1 / t;
            double tttinv = tinv * tinv * tinv;
            double ttttinv = tttinv * tinv;
            double ft = tinv * (piinv + (ttttinv * ratioEval(t, FN5, -1) / ratioEval(t, FD5, -1)));
            double gt = tttinv * ((piinv * piinv) + (ttttinv * ratioEval(t, GN5, -1) / ratioEval(t, GD5, -1)));
            cc = .5 + ft * sinpitt2 - gt * cospitt2;
            ss = .5 - ft * cospitt2 - gt * sinpitt2;
        }
        if (x < 0)
        {
            cc = -cc;
            ss = -ss;
        }

        return new double[] {cc, ss};
    }

    /**
     * Evaluate numerator or denominator of rational approximation.
     * @param t value along the clothoid
     * @param coef rational approximation coefficients
     * @param sign sign of exponent, +1 for Cartesian rational approximation, -1 for polar approximation
     * @return numerator or denominator of rational approximation
     */
    private static double ratioEval(final double t, final double[] coef, final double sign)
    {
        double value = 0;
        for (int s = 0; s < coef.length; s++)
        {
            value += coef[s] * Math.pow(t, sign * 4 * s);
        }
        return value;
    }

}
