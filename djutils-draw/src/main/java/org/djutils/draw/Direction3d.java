package org.djutils.draw;

import java.util.Objects;

import org.djutils.exceptions.Throw;

/**
 * Class encoding a direction in 3d space. It combines dirY (similar to tilt; measured as an angle from the positive
 * z-direction) and dirZ (similar to pan; measured as an angle from the positive x-direction).
 * <p>
 * Copyright (c) 2023-2024 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://djutils.org" target="_blank"> https://djutils.org</a>. The DJUTILS project is
 * distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://djutils.org/docs/license.html" target="_blank"> https://djutils.org/docs/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://github.com/peter-knoppers">Peter Knoppers</a>
 * @author <a href="https://github.com/wjschakel">Wouter Schakel</a>
 */
public class Direction3d
{
    /** Rotation around y-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirY;

    /** Rotation around z-axis. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public final double dirZ;

    /**
     * Construct a Direction3d.
     * @param dirY double; the dirY component for the new Direction3d
     * @param dirZ double; the dirZ component for the new Direction3d
     * @throws ArithmeticException when <code>dirY</code>, or <code>dirZ</code> is <code>NaN</code>
     * @throws IllegalArgumentException when <code>dirY</code>, or <code>dirZ</code> is infinite
     */
    public Direction3d(final double dirY, final double dirZ)
    {
        Throw.whenNaN(dirY, "dirY");
        Throw.whenNaN(dirZ, "dirZ");
        Throw.when((!Double.isFinite(dirY)) || (!Double.isFinite(dirZ)), IllegalArgumentException.class,
                "dirY and dirZ must be finite");
        this.dirY = dirY;
        this.dirZ = dirZ;
    }

    /**
     * Retrieve the dirY component of this Direction3d.
     * @return double; the <code>dirY</code> component of this <code>Direction3d</code>
     */
    public double getDirY()
    {
        return this.dirY;
    }

    /**
     * Retrieve the dirZ component of this Direction3d.
     * @return double; the <code>dirZ</code> component of this <code>Direction3d</code>
     */
    public double getDirZ()
    {
        return this.dirZ;
    }

    /**
     * Determine the angle between this Direction3d and another Direction3d. Liberally based on
     * https://www.cuemath.com/geometry/angle-between-vectors/
     * @param otherDirection Direction3d; the other Direction3d
     * @return double the angle in Radians
     * @throws NullPointerException when <code>otherDirection</code> is <code>null</code>
     */
    public double directionDifference(final Direction3d otherDirection)
    {
        double sinDirY = Math.sin(this.dirY);
        double uX = Math.cos(this.dirZ) * sinDirY;
        double uY = Math.sin(this.dirZ) * sinDirY;
        double uZ = Math.cos(this.dirY);
        double otherSinDirY = Math.sin(otherDirection.dirY);
        double oX = Math.cos(otherDirection.dirZ) * otherSinDirY;
        double oY = Math.sin(otherDirection.dirZ) * otherSinDirY;
        double oZ = Math.cos(otherDirection.dirY);
        double cosine = uX * oX + uY * oY + uZ * oZ;
        if (Math.abs(cosine) > 1.0 && Math.abs(cosine) < 1.0 + 10 * Math.ulp(1.0))
        {
            cosine = Math.signum(cosine); // Fix rounding error
        }
        return (Math.acos(cosine));
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.dirY, this.dirZ);
    }

    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Direction3d other = (Direction3d) obj;
        return Double.doubleToLongBits(this.dirY) == Double.doubleToLongBits(other.dirY)
                && Double.doubleToLongBits(this.dirZ) == Double.doubleToLongBits(other.dirZ);
    }

    @Override
    public String toString()
    {
        return "Direction3d [dirY=" + this.dirY + ", dirZ=" + this.dirZ + "]";
    }

}
