package org.djutils.data;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.djunits.value.Value;
import org.djunits.value.base.Scalar;
import org.djutils.base.Identifiable;
import org.djutils.exceptions.Throw;
import org.djutils.primitives.Primitive;

/**
 * Column identifier and descriptor.
 * <p>
 * Copyright (c) 2020-2025 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djutils.org/docs/current/djutils/licenses.html">DJUTILS License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 * @param <T> value type
 */
public class Column<T> implements Identifiable, Serializable
{

    /** */
    private static final long serialVersionUID = 20230125L;

    /** Id. */
    private final String id;

    /** Description. */
    private final String description;

    /** Value type. */
    private final Class<T> valueType;

    /** Unit. */
    private final String unit;

    /**
     * Make a new column for a table with an id, description, type, and unit.
     * @param id id of the column
     * @param description description of the column
     * @param valueType value type of the column
     * @param unit unit, may be {@code null}
     */
    @SuppressWarnings("unchecked")
    public Column(final String id, final String description, final Class<T> valueType, final String unit)
    {
        Throw.whenNull(id, "id may not be null.");
        Throw.when(id.length() == 0, IllegalArgumentException.class, "id cannot be empty");
        Throw.whenNull(description, "description may not be null.");
        Throw.whenNull(valueType, "valueType may not be null.");
        this.id = id;
        this.description = description;
        this.valueType = valueType.isPrimitive() ? (Class<T>) Primitive.getWrapper(valueType) : valueType;
        Throw.when(Value.class.isAssignableFrom(valueType) && (unit == null || unit.length() == 0),
                IllegalArgumentException.class, "For a DJUNITS value, unit cannot be null or the empty string");
        if (Scalar.class.isAssignableFrom(valueType))
        {
            try
            {
                Method valueOfMethod = valueType.getDeclaredMethod("valueOf", String.class);
                valueOfMethod.invoke(null, "1.0" + unit);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                    | SecurityException exception)
            {
                throw new IllegalArgumentException(
                        "unit of the Column does not belong to Scalar of type " + valueType.getSimpleName());
            }
        }
        this.unit = unit;
    }

    /**
     * Make a new column for a table with an id, description, type. The unit is blank (null).
     * @param id id of the column
     * @param description description of the column
     * @param valueType value type of the column
     */
    public Column(final String id, final String description, final Class<T> valueType)
    {
        this(id, description, valueType, null);
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    /**
     * Returns the column description.
     * @return column description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * Returns the type of the values in the column.
     * @return type of the values in the column
     */
    public Class<T> getValueType()
    {
        return this.valueType;
    }

    /**
     * Returns the unit of the column. Data is written an read using this unit.
     * @return unit of the column
     */
    public String getUnit()
    {
        return this.unit;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Column<?> other = (Column<?>) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString()
    {
        return "Column [id=" + this.id + ", description=" + this.description + ", valueType=" + this.valueType
                + (this.unit == null ? "]" : ", unit=" + this.unit + "]");
    }

}
