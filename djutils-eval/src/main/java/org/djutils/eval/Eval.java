package org.djutils.eval;

import java.util.ArrayList;
import java.util.List;

import org.djunits.unit.DimensionlessUnit;
import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Dimensionless;
import org.djunits.value.vdouble.scalar.SIScalar;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vdouble.scalar.base.Constants;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;
import org.djunits.value.vdouble.scalar.base.DoubleScalarAbs;
import org.djunits.value.vdouble.scalar.base.DoubleScalarRel;

/**
 * Eval - evaluate mathematical expression. Derived from software developed between 2002-2016 by Peter Knoppers.
 * <p>
 * TODO: implement strings datatype
 * <p>
 * TODO: implement if statement
 */
public class Eval
{
    /** The expression evaluation stack. */
    private List<DoubleScalar<?, ?>> stack = new ArrayList<>();

    /** The expression. */
    private String expression;

    /** Position of next token in the expression. */
    private int position = 0;

    /** Access to the variable pool. */
    private final RetrieveValue retrieveValue;

    /** Binding strength of addition and subtraction operators. */
    private static final int BIND_ADD = 1;

    /** Binding strength of multiplication and division operators. */
    private static final int BIND_MUL = 2;

    /** Binding strength of unary minus. */
    private static final int BIND_UMINUS = 3;

    /** Pi. */
    static final Dimensionless PI = new Dimensionless(Math.PI, DimensionlessUnit.SI);

    /** Euler's constant, a.k.a. the base of the natural logarithm (e). */
    private static final Dimensionless E = new Dimensionless(Math.E, DimensionlessUnit.SI);

    /** Phi. */
    private static final Dimensionless PHI = new Dimensionless((1 + Math.sqrt(5)) / 2, DimensionlessUnit.SI);

    /**
     * Construct a new expression evaluator.
     * @param expression String; the expression that the evaluator shall attempt to evaluate
     * @param retrieveValue RetrieveValue; object that allows retrieving a value (can be null)
     */
    private Eval(final String expression, final RetrieveValue retrieveValue)
    {
        this.expression = expression;
        this.retrieveValue = retrieveValue;
    }

    /**
     * Construct an expression evaluator and let it attempt to evaluate a mathematical expression
     * @param expression String; the expression
     * @param retrieveValue RetrieveValue; object that allows retrieving a value (can be null)
     * @return DoubleScalar&lt;?,?&gt;; The value of the evaluated expression
     * @throws RuntimeException when the expression cannot be evaluated
     */
    public static DoubleScalar<?, ?> evaluate(final String expression, final RetrieveValue retrieveValue)
            throws RuntimeException
    {
        Eval evaluator = new Eval(expression, retrieveValue);
        evaluator.evalLhs(0);
        evaluator.eatSpace();
        if (evaluator.position < evaluator.expression.length())
        {
            evaluator.throwException("Trailing garbage: \"" + evaluator.expression.substring(evaluator.position) + "\"");
        }
        if (evaluator.stack.size() > 1)
        {
            evaluator.throwException("Unfinished operations");
        }
        if (evaluator.stack.size() <= 0)
        {
            evaluator.throwException("No result after evaluation");
        }
        return evaluator.pop();
    }

    /**
     * Increment position up to the next non-space character.
     */
    private void eatSpace()
    {
        while (this.position < this.expression.length() && Character.isWhitespace(this.expression.charAt(this.position)))
        {
            this.position++;
        }
    }

    /**
     * Evaluate the right-hand-side of a binary operation.
     * @param bindingStrength int; the binding strength of a pending binary operation (0 if no binary operation is pending)
     * @throws RuntimeException on error
     */
    private void evalLhs(final int bindingStrength) throws RuntimeException
    {
        eatSpace();
        char token = this.expression.charAt(this.position);
        switch (token)
        {
            case '-': // Unary minus
                this.position++;
                evalLhs(BIND_UMINUS);
                push(pop().neg()); // apply the unary minus
                break;

            case '(': // parenthesized expression
                this.position++;
                evalLhs(0);
                if (this.position >= this.expression.length() || ')' != this.expression.charAt(this.position))
                {
                    throwException("Missing closing parenthesis");
                }
                this.position++; // step over the closing parenthesis
                break;

            default: // parse one operand
            {
                if (Character.isLetter(token))
                {
                    push(handleFunctionOrVariableOrNamedConstant());
                }
                else if (Character.isDigit(token) || '.' == token)
                {
                    push(handleNumber());
                    // I don't think we can automatically parse things like "123.456 m/s" into a Speed
                }
            }
        }
        evalRhs(bindingStrength);
    }

    /**
     * Evaluate the right hand sid of a mathematical expression. Stop at a closing parenthesis, or a binary operator that does
     * not have a higher binding strength than the argument.
     * @param bindingStrength int; if the next token is a binary operator with a lower or equal binding strength; return without
     *            consuming that token.
     * @throws RuntimeException when the expression is mathematically unsound
     */
    private void evalRhs(final int bindingStrength) throws RuntimeException
    {
        eatSpace();
        while (this.position < this.expression.length())
        {
            char token = this.expression.charAt(this.position);
            switch (token)
            {
                case ')':
                    return;

                case '*':
                    if (bindingStrength >= BIND_MUL)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_MUL);
                    multiply();
                    break;

                case '/':
                    if (bindingStrength >= BIND_MUL)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_MUL);
                    divide();
                    break;

                case '+':
                    if (bindingStrength >= BIND_ADD)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_ADD);
                    add();
                    break;

                case '-':
                    if (bindingStrength >= BIND_ADD)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_ADD);
                    subtract();
                    break;

                default:
                    throwException("Operator expected (got\"" + token + "\")");

            }

        }
    }

    /**
     * Throw an exception because the expression cannot be evaluated.
     * @param description String; description of the problem
     * @throws RuntimeException always thrown
     */
    private void throwException(final String description) throws RuntimeException
    {
        throw new RuntimeException(description + " at position " + this.position);
    }

    /**
     * Multiply the two top-most elements on the stack and push the result back onto the stack.
     */
    private void multiply()
    {
        DoubleScalar<?, ?> ds = pop();
        if (!(ds instanceof DoubleScalarRel))
        {
            throwException("Cannot multiply with " + ds + " as right hand operand");
        }
        DoubleScalarRel<?, ?> right = (DoubleScalarRel<?, ?>) ds;
        ds = pop();
        if (!(ds instanceof DoubleScalarRel))
        {
            throw new RuntimeException("Cannot multiply " + ds + " with anything");
        }
        DoubleScalarRel<?, ?> left = (DoubleScalarRel<?, ?>) ds;
        // System.out.println("left=" + left + ", right=" + right + ", product=" + left.times(right));
        push(left.times(right));
    }

    /**
     * Divide the two top-most elements on the stack and push the result back onto the stack.
     */
    private void divide()
    {
        DoubleScalar<?, ?> ds = pop();
        if (!(ds instanceof DoubleScalarRel))
        {
            throw new RuntimeException("Cannot divide by " + ds);
        }
        DoubleScalarRel<?, ?> right = (DoubleScalarRel<?, ?>) ds;
        ds = pop();
        if (!(ds instanceof DoubleScalarRel))
        {
            throw new RuntimeException("Cannot divide " + ds + " by anything");
        }
        DoubleScalarRel<?, ?> left = (DoubleScalarRel<?, ?>) ds;
        if (0.0 == right.si)
        {
            throw new RuntimeException("Division by 0");
        }
        // System.out.println("left=" + left + ", right=" + right + ", dividend=" + left.divide(right));
        push(left.divide(right));
    }

    /**
     * Add the two top-most elements on the stack and push the result back onto the stack.
     */
    private void add()
    {
        DoubleScalar<?, ?> ds = pop();
        if (!(ds instanceof DoubleScalarRel))
        {
            throwException("Cannot add an absolute value to some other value");
        }
        DoubleScalarRel<?, ?> right = (DoubleScalarRel<?, ?>) ds;
        ds = pop();
        // System.out.println("left unit : " + ds.getDisplayUnit().getStandardUnit());
        // System.out.println("right unit: " + right.getDisplayUnit().getStandardUnit());
        if (!ds.getDisplayUnit().getStandardUnit().toString().equals(right.getDisplayUnit().getStandardUnit().toString()))
        {
            throwException("Cannot add " + ds + " and " + right + " because the types are incompatible");
        }
        if (ds.isAbsolute())
        {
            // Abs + Rel -> Abs
            DoubleScalar<?, ?> sum = DoubleScalarAbs.instantiate(ds.si + right.si, ds.getDisplayUnit().getStandardUnit());
            System.out.println(ds + " + " + right + " = " + sum);
            // sum.setDisplayUnit(ds.getDisplayUnit());
            push(sum);
        }
        else
        {
            // Rel + Rel -> Rel
            DoubleScalar<?, ?> sum = DoubleScalarRel.instantiate(ds.si + right.si, ds.getDisplayUnit().getStandardUnit());
            System.out.println(ds + " + " + right + " = " + sum);
            // sum.setDisplayUnit(ds.getDisplayUnit());
            push(sum);
        }
    }

    /**
     * Subtract the two top-most elements on the stack and push the result back onto the stack.
     */
    private void subtract()
    {
        DoubleScalar<?, ?> right = pop();
        DoubleScalar<?, ?> left = pop();
        // System.out.println("left unit : " + left.getDisplayUnit().getStandardUnit());
        // System.out.println("right unit: " + right.getDisplayUnit().getStandardUnit());
        if (!left.getDisplayUnit().getStandardUnit().toString().equals(right.getDisplayUnit().getStandardUnit().toString()))
        {
            //System.out.println("left standard unit: " + left.getDisplayUnit().getStandardUnit().toString() + ", right standard unit: "
            //        + right.getDisplayUnit().getStandardUnit().toString());
            throwException("Cannot subtract " + right + " from " + left + " because the types are incompatible");
        }
        if (left.isAbsolute() && right.isAbsolute())
        {
            // Abs - Abs -> Rel
            DoubleScalar<?, ?> difference =
                    DoubleScalarRel.instantiate(left.si - right.si, left.getDisplayUnit().getStandardUnit());
            // System.out.println(left + " - " + right + " = " + difference);
            push(difference);
        }
        else if (left.isAbsolute() && right.isRelative())
        {
            // Abs - Rel -> Abs
            DoubleScalar<?, ?> difference =
                    DoubleScalarAbs.instantiate(left.si - right.si, left.getDisplayUnit().getStandardUnit());
            // System.out.println(left + " - " + right + " = " + difference);
            push(difference);
        }
        else if (right.isAbsolute())
        {
            // Rel - Abs -> error
            throwException("Cannot subtract " + right + " from " + left + " because the right operand is absolute");
        }
        else
        {
            // Rel - Rel -> Rel
            DoubleScalar<?, ?> difference =
                    DoubleScalarRel.instantiate(left.si - right.si, left.getDisplayUnit().getStandardUnit());
            // System.out.println(left + " - " + right + " = " + difference);
            push(difference);
        }
    }

    /**
     * Parse a number and convert it to a Dimensionless. If it is followed by an SI unit string inside square brackets, parse it
     * into the correct type.
     * @return SIScalar; the value of the parsed number or value
     */
    private SIScalar handleNumber()
    {
        // Parse a number value
        int startPosition = this.position;
        boolean seenSign = false;
        boolean seenExp = false;
        boolean seenExpSign = false;
        boolean seenDigit = false;
        boolean seenExpDigit = false;
        boolean seenRadix = false;
        while (this.position < this.expression.length())
        {
            char c = this.expression.charAt(this.position);
            if ((!seenDigit) && ('-' == c || '+' == c))
            {
                if (seenSign)
                {
                    throwException("Too many consecutive signs");
                }
                seenSign = true;
            }
            else if (seenExp && (!seenExpDigit) && ('-' == c || '+' == c))
            {
                if (seenExpSign)
                {
                    throwException("Too many consecutive signs");
                }
                seenExpSign = true;
            }
            else if ('e' == c || 'E' == c)
            {
                if (seenExp)
                {
                    throwException("Too many 'e' or 'E' indicators");
                }
                seenExp = true;
            }
            else if ('.' == c)
            {
                if (seenRadix)
                {
                    throw new RuntimeException("Too many '.'");
                }
            }
            else if (Character.isDigit(c))
            {
                if (seenExp)
                {
                    seenExpDigit = true;
                }
                else
                {
                    seenDigit = true;
                }
            }
            else
            {
                break;
            }
            this.position++;
        }
        eatSpace();
        String number = this.expression.substring(startPosition, this.position);
        if (this.position < this.expression.length() && '[' == this.expression.charAt(this.position))
        {
            this.position++;
            startPosition = this.position;
            while (this.position < this.expression.length())
            {
                char c = this.expression.charAt(this.position);
                if (']' == c)
                {
                    return SIScalar.valueOf(number + " " + this.expression.substring(startPosition, this.position++));
                }
                if ((!Character.isLetterOrDigit(c)) && '-' != c && '^' != c && '/' != c)
                {
                    throwException("Bad symbol in SI unit string");
                }
                this.position++;
            }
            if (this.position >= this.expression.length())
            {
                throwException("Missing closing bracket (\']\')");
            }
        }
        return SIScalar.valueOf(number);
    }

    /**
     * Process a function call, a variable interpolation, or a mathematical, or physical constant. Precondition: this.position
     * points to the first letter of the name of the function, variable, or constant.
     * @return DoubleScalar&lt;?,?&gt;; the value of the function, variable, or mathematical or physical constant
     */
    private DoubleScalar<?, ?> handleFunctionOrVariableOrNamedConstant()
    {
        int startPosition = this.position;
        while (this.position < this.expression.length())
        {
            char c = this.expression.charAt(this.position);
            if (Character.isLetterOrDigit(c) || '.' == c || '_' == c)
            {
                this.position++;
            }
            else
            {
                break;
            }
        }
        String name = this.expression.substring(startPosition, this.position);
        eatSpace();
        if (this.position < this.expression.length() && '(' == this.expression.charAt(this.position))
        {
            // Open parentheses signals the start of a function call; collect the parameters of the function on the stack and
            // count them
            this.position++;
            eatSpace();
            int argCount = 0;
            while (this.position < this.expression.length() && ')' != this.expression.charAt(this.position))
            {
                evalLhs(0);
                argCount++;
                eatSpace();
            }
            if (this.position >= this.expression.length() || ')' != this.expression.charAt(this.position))
            {
                throwException("Missing closing parentheses");
            }
            this.position++; // step over the closing parenthesis
            if (0 == argCount)
            {
                return handleZeroArgumentFunction(name);
            }
            if (1 == argCount)
            {
                return executeOneArgumentFunction(name);
            }
            else if (2 == argCount && "pow" == name)
            {
                DoubleScalar<?, ?> power = pop();
                if (!(power instanceof Dimensionless))
                {
                    throwException("Function pow cannot be applied to " + power);
                }
                DoubleScalar<?, ?> ds = pop();
                if (!(ds instanceof Dimensionless))
                {
                    throwException("Function pow cannot be applied to " + ds);
                }
                return ((Dimensionless) ds).pow(power.si);
            }
            else
            {
                throwException("Unknown " + argCount + " argument function " + name);
            }
        }
        else
        {
            // No opening parenthesis; name must be the name of a variable; look it up
            DoubleScalar<?, ?> result = null == this.retrieveValue ? null : this.retrieveValue.lookup(name);
            if (null == result)
            {
                throwException("Cannot resolve variable " + name);
            }
            return result;
        }
        return null; // cannot happen
    }

    /**
     * Execute a zero-argument function. These functions return a physical or mathematical constant, or invoke some system
     * function.
     * @param functionName String; name of the function to invoke
     * @return DoubleScalar&lt;?,?&gt;; the result of executing the function
     * @throws RuntimeException when no function with the specified name is known
     */
    private DoubleScalar<?, ?> handleZeroArgumentFunction(final String functionName) throws RuntimeException
    {
        switch (functionName)
        {
            case "AVOGADRO":
                return Constants.AVOGADRO;

            case "BOLTZMANN":
                return Constants.BOLTZMANN;

            case "CESIUM133_FREQUENCY":
                return Constants.CESIUM133_FREQUENCY;

            case "CURRENTTIME":
                return new Time(System.currentTimeMillis() / 1000d, TimeUnit.BASE_SECOND);

            case "E":
                return E;

            case "ELECTRONCHARGE":
                return Constants.ELECTRONCHARGE;

            case "ELECTRONMASS":
                return Constants.ELECTRONMASS;

            case "G":
                return Constants.G;

            case "LIGHTSPEED":
                return Constants.LIGHTSPEED;

            case "LUMINOUS_EFFICACY_540THZ":
                return Constants.LUMINOUS_EFFICACY_540THZ;

            case "NEUTRONMASS":
                return Constants.NEUTRONMASS;

            case "PI":
                return PI;

            case "PHI":
                return PHI;

            case "PLANCK":
                return Constants.PLANCK;

            case "PLANKREDUCED":
                return Constants.PLANKREDUCED;

            case "PROTONCHARGE":
                return Constants.PROTONCHARGE;

            case "PROTONMASS":
                return Constants.PROTONMASS;

            case "TAU":
                return Constants.TAU;

            case "VACUUMPERMEABILITY":
                return Constants.VACUUMPERMEABILITY;

            case "VACUUMPERMITTIVITY":
                return Constants.VACUUMPERMITTIVITY;

            case "VACUUMIMPEDANCE":
                return Constants.VACUUMIMPEDANCE;

        }
        throwException("Unknown zero-argument function " + functionName);
        return null;// cannot happen
    }

    /**
     * Execute a one-argument function on the top-element of the stack and push the result onto the stack.
     * @param functionName String; the name of the zero-argument function
     * @return DoubleScalar&lt;?,?&gt;; the result of evaluating the one-argument function
     */
    private DoubleScalar<?, ?> executeOneArgumentFunction(final String functionName)
    {
        DoubleScalar<?, ?> ds = pop();
        // All implemented one-argument functions only operate on a DimensionLess value
        if (!(ds instanceof Dimensionless))
        {
            throwException("Function " + functionName + " cannot be applied to " + ds); // should test if the name is valid
        }
        Dimensionless dl = (Dimensionless) ds;
        switch (functionName)
        {
            case "acos":
                return dl.acos();

            case "asin":
                return dl.asin();

            case "atan":
                return dl.atan();

            case "cbrt":
                return dl.cbrt();

            case "cos":
                return dl.cos();

            case "cosh":
                push(dl.cosh());
                break;

            case "exp":
                return dl.exp();

            case "expm1":
                return dl.expm1();

            case "log":
                return dl.log();

            case "log10":
                return dl.log10();

            case "log1p":
                return dl.log1p();

            case "signum":
                return dl.signum();

            case "sin":
                return dl.sin();

            case "sinh":
                return dl.sinh();

            case "sqrt":
                return dl.sqrt();

            case "tan":
                return dl.tan();

            case "tanh":
                return dl.tanh();

        }
        throwException("Unknown function " + functionName);
        return null;// cannot happen
    }

    /**
     * Push one value onto the evaluation stack.
     * @param value DoubleScalar&lt;?,?&gt;; the value to push onto the evaluation stack
     */
    private void push(final DoubleScalar<?, ?> value)
    {
        this.stack.add(value);
    }

    /**
     * Pop one value from the evaluation stack. Throw exception when stack underflows
     * @return DoubleScalar&lt;?,?&gt;; the value popped from the evaluation stack
     * @throws RuntimeException when the stack is currently empty
     */
    private DoubleScalar<?, ?> pop() throws RuntimeException
    {
        if (0 == this.stack.size())
        {
            throwException("Stack empty");
        }
        return (this.stack.remove(this.stack.size() - 1));
    }

}
