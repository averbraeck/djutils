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
import org.djutils.exceptions.Throw;

/**
 * Eval - evaluate mathematical expression. Derived from software developed between 2002-2016 by Peter Knoppers.
 * <p>
 * TODO: implement strings datatype
 * <p>
 * TODO: implement if statement
 * <p>
 * The precedence of binary operators follows the list of the
 * <a href="https://www.cs.bilkent.edu.tr/~guvenir/courses/CS101/op_precedence.html">Java Operator Precendence Table</a>,
 * skipping bitwise and other operators that make no sense for this evaluator and adding the exponentiation (^) operator.
 */
public class Eval
{
    /** The expression evaluation stack. */
    private List<Object> stack = new ArrayList<>();

    /** The expression. */
    private String expression;

    /** Position of next token in the expression. */
    private int position = 0;

    /** Access to the variable pool. */
    private final RetrieveValue retrieveValue;

    /** Binding strength of ternary conditional. */
    private static final int BIND_CONDITIONAL_EXPRESSION = 1;

    /** Binding strength of boolean OR operation. */
    private static final int BIND_OR = 2;

    /** Binding strength of boolean AND operation. */
    private static final int BIND_AND = 3;

    /** Binding strength of == and != operators. */
    private static final int BIND_EQUAL = 4;

    /** Binding strength of <, <=, >, >= relational operators. */
    private static final int BIND_RELATIONAL = 5;

    /** Binding strength of addition and subtraction operators. */
    private static final int BIND_ADD = 6;

    /** Binding strength of multiplication and division operators. */
    private static final int BIND_MUL = 7;

    /** Binding strength of the power operation. */
    private static final int BIND_POW = 8;

    /** Binding strength of unary minus and logical negation. */
    private static final int BIND_UMINUS = 9;

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
     * @return Object; The value of the evaluated expression
     * @throws RuntimeException when the expression cannot be evaluated
     */
    public static Object evaluate(final String expression, final RetrieveValue retrieveValue) throws RuntimeException
    {
        Throw.whenNull(expression, "expression may not be null");
        Throw.when(expression.length() == 0, IllegalArgumentException.class, "Expression may not be the empty string");
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
     * Evaluate the left-hand-side of a binary operation.
     * @param bindingStrength int; the binding strength of a pending binary operation (0 if no binary operation is pending)
     * @throws RuntimeException on error
     */
    private void evalLhs(final int bindingStrength) throws RuntimeException
    {
        eatSpace();
        if (this.position >= this.expression.length())
        {
            throwException("Missing operand");
        }
        char token = this.expression.charAt(this.position);
        switch (token)
        {
            case '-': // Unary minus
            {
                this.position++;
                evalLhs(BIND_UMINUS);
                Object value = pop();
                if (value instanceof DoubleScalar<?, ?>)
                {
                    push(((DoubleScalar<?, ?>) value).neg());
                    break;
                }
                throwException("Cannot apply unary minus on " + value);
            }

            case '!':
                if (this.position >= this.expression.length() - 1 || '=' != this.expression.charAt(this.position + 1))
                {
                    // unary logical negation
                    this.position++;
                    evalLhs(BIND_UMINUS);
                    Object value = pop();
                    if (value instanceof Boolean)
                    {
                        push(!((Boolean) value));
                        break;
                    }
                    throwException("Cannot apply unary not operator on " + value);
                }
                break; // We should not get a "!=" operation here, but if we do, it results in an error later on TODO unit test
                       // for this

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

                case '^': // power operator
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_POW)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_POW);
                    power();
                    break;

                case '*':
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_MUL)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_MUL);
                    multiply();
                    break;

                case '/':
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_MUL)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_MUL);
                    divide();
                    break;

                case '+':
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_ADD)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_ADD);
                    add();
                    break;

                case '-':
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_ADD)
                    {
                        return;
                    }
                    this.position++;
                    evalLhs(BIND_ADD);
                    subtract();
                    break;

                case '&': // boolean and ?
                    if (this.position >= this.expression.length() - 1 || '&' != this.expression.charAt(this.position))
                    {
                        throwException("Single \'&\' is not a valid operator");
                    }
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_AND)
                    {
                        return;
                    }
                    this.position += 2;
                    evalLhs(BIND_AND);
                    and();
                    break;

                case '|': // boolean and ?
                    if (this.position >= this.expression.length() - 1 || '|' != this.expression.charAt(this.position))
                    {
                        throwException("Single \'|\' is not a valid operator");
                    }
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    if (bindingStrength >= BIND_OR)
                    {
                        return;
                    }
                    this.position += 2;
                    evalLhs(BIND_OR);
                    or();
                    break;

                case '<':
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    this.position++;
                    if (this.position < this.expression.length() && '=' == this.expression.charAt(this.position))
                    {
                        this.position++;
                        evalLhs(BIND_RELATIONAL);
                        Object right = pop();
                        Object left = pop();
                        if ((left instanceof DoubleScalar) && (right instanceof DoubleScalar)
                                && ((DoubleScalar<?, ?>) left).getDisplayUnit().getQuantity()
                                        .equals(((DoubleScalar<?, ?>) right).getDisplayUnit().getQuantity()))
                        {
                            push(((DoubleScalar<?, ?>) left).si <= ((DoubleScalar<?, ?>) right).si);
                            break;
                        }
                        throwException("Cannot compare " + left + " to " + right);
                    }
                    else
                    {
                        // FIXME: "<=" looks too much like "<"; should probably use a lambda expression
                        evalLhs(BIND_RELATIONAL);
                        Object right = pop();
                        Object left = pop();
                        if ((left instanceof DoubleScalar) && (right instanceof DoubleScalar)
                                && ((DoubleScalar<?, ?>) left).getDisplayUnit().getQuantity()
                                        .equals(((DoubleScalar<?, ?>) right).getDisplayUnit().getQuantity()))
                        {
                            push(((DoubleScalar<?, ?>) left).si < ((DoubleScalar<?, ?>) right).si);
                            break;
                        }
                        throwException("Cannot compare " + left + " to " + right);
                    }

                case '>': // FIXME: looks too much like case '<'. Should probably use lambda expressions
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    this.position++;
                    if (this.position < this.expression.length() && '=' == this.expression.charAt(this.position))
                    {
                        this.position++;
                        evalLhs(BIND_RELATIONAL);
                        Object right = pop();
                        Object left = pop();
                        if ((left instanceof DoubleScalar) && (right instanceof DoubleScalar)
                                && ((DoubleScalar<?, ?>) left).getDisplayUnit().getQuantity()
                                        .equals(((DoubleScalar<?, ?>) right).getDisplayUnit().getQuantity()))
                        {
                            push(((DoubleScalar<?, ?>) left).si >= ((DoubleScalar<?, ?>) right).si);
                            break;
                        }
                        throwException("Cannot compare " + left + " to " + right);
                    }
                    else
                    {
                        // FIXME: ">=" looks too much like ">"; should probably use a lambda expression
                        evalLhs(BIND_RELATIONAL);
                        Object right = pop();
                        Object left = pop();
                        if ((left instanceof DoubleScalar) && (right instanceof DoubleScalar)
                                && ((DoubleScalar<?, ?>) left).getDisplayUnit().getQuantity()
                                        .equals(((DoubleScalar<?, ?>) right).getDisplayUnit().getQuantity()))
                        {
                            push(((DoubleScalar<?, ?>) left).si > ((DoubleScalar<?, ?>) right).si);
                            break;
                        }
                        throwException("Cannot compare " + left + " to " + right);
                    }

                case '=':
                {
                    if (this.position >= this.expression.length() - 1 || '=' != this.expression.charAt(this.position + 1))
                    {
                        throwException("Single \'=\' is not a valid operator");
                    }
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    this.position += 2;
                    evalLhs(BIND_EQUAL);
                    Object right = pop();
                    Object left = pop();
                    push(left.equals(right)); // This also works for Boolean operands.
                    break;
                }

                case '!':
                {
                    if (this.position >= this.expression.length() - 1 || '=' != this.expression.charAt(this.position + 1))
                    {
                        throwException("Single \'!\' is not a valid operator");
                    }
                    if (this.stack.isEmpty())
                    {
                        throwException("Missing left operand");
                    }
                    this.position += 2;
                    evalLhs(BIND_EQUAL);
                    Object right = pop();
                    Object left = pop();
                    push(!left.equals(right)); // This also works for Boolean operands and mixed-type operands.
                    break;
                }

                case '?': // Conditional expression
                {
                    this.position++;
                    // This is special as we really do not want to evaluate or cause side effects on the non-taken part
                    Object choice = pop();
                    if (!(choice instanceof Boolean))
                    {
                        throwException("Condition does not evaluate to a logical value");
                    }
                    if (!((Boolean) choice))
                    {
                        // Skip the "then" part
                        skip(true);
                    }
                    else
                    {
                        evalLhs(0); // should consume everything up to the ':'
                    }
                    if (this.position >= this.expression.length() || ':' != this.expression.charAt(this.position))
                    {
                        throwException("Missing \':\' of conditional expresion");
                    }
                    this.position++; // skip the ':'
                    if ((Boolean) choice)
                    {
                        // Skip the "else" part
                        skip(false);
                    }
                    else
                    {
                        evalLhs(BIND_CONDITIONAL_EXPRESSION);
                    }
                    break;
                }

                case ':':
                    return;

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
     * Skip the "then" or the "else" part of a conditional expression.
     * @param thenPart boolean; if true; skip the then part (i.e. skip until a ':'); if false; skip the else part (i.e. until
     *            end of expression of a binary operator)
     */
    private void skip(final boolean thenPart)
    {
        while (this.position < this.expression.length())
        {
            switch (this.expression.charAt(this.position))
            {
                case '(': // Eat up everything until the matching closing parenthesis
                {
                    int level = 1;
                    this.position++;
                    while (this.position < this.expression.length())
                    {
                        char c = this.expression.charAt(this.position);
                        if ('(' == c)
                        {
                            level++;
                        }
                        else if (')' == c && --level == 0)
                        {
                            break;
                        }
                        this.position++;
                    }
                    this.position++; // skip the closing parenthesis
                    break;
                }

                case '?': // Nested conditional expression
                    this.position++;
                    skip(true);
                    if (this.position >= this.expression.length() || ':' != this.expression.charAt(this.position))
                    {
                        throwException("Conditional expression missing \':\'");
                    }
                    this.position++;
                    skip(false);
                    break;

                case ':':
                    return;

                case '^':
                case '*':
                case '/':
                case '+':
                case '-':
                case '<':
                case '=':
                case '>':
                case '!':
                    if (!thenPart)
                    {
                        return;
                    }
                    this.position++;
                    break;

                default:
                    this.position++;
                    break;
            }
        }
        if (thenPart)
        {
            throwException("Nonterminated conditional expression");
        }
        // Else end of expression is end of else part
    }

    /**
     * Perform the boolean AND operation on the two top-most elements on the stack and push the result back onto the stack.
     */
    private void and()
    {
        Object right = pop();
        Object left = pop();
        if ((left instanceof Boolean) && (right instanceof Boolean))
        {
            push(((Boolean) left) && ((Boolean) right));
            return;
        }
        throwException("Cannot compute logical AND of " + left + " and " + right);
    }

    /**
     * Perform the boolean OR operation on the two top-most elements on the stack and push the result back onto the stack.
     */
    private void or()
    {
        Object right = pop();
        Object left = pop();
        if ((left instanceof Boolean) && (right instanceof Boolean))
        {
            push(((Boolean) left) || ((Boolean) right));
            return;
        }
        throwException("Cannot compute logical AND of " + left + " and " + right);
    }

    /**
     * Perform the power operation on the two top-most elements on stack and push the result back onto the stack.
     */
    private void power()
    {
        Object exponent = pop();
        Object base = pop();
        if ((base instanceof DoubleScalarRel) && (exponent instanceof DoubleScalarRel)
                && ((DoubleScalarRel<?, ?>) base).getDisplayUnit().getQuantity().equals(DimensionlessUnit.SI.getQuantity())
                && ((DoubleScalarRel<?, ?>) exponent).getDisplayUnit().getQuantity().equals(DimensionlessUnit.SI.getQuantity()))
        {
            DoubleScalar<?, ?> result = DoubleScalarRel.instantiate(
                    Math.pow(((DoubleScalarRel<?, ?>) base).si, ((DoubleScalarRel<?, ?>) exponent).si), DimensionlessUnit.SI);
            // System.out.println(base + " ^ " + exponent + " = " + result);
            push(result);
            return;
        }
        throwException("Cannot raise " + base + " to power " + exponent);
    }

    /**
     * Multiply the two top-most elements on the stack and push the result back onto the stack.
     */
    private void multiply()
    {
        Object right = pop();
        Object left = pop();
        if ((right instanceof DoubleScalarRel) && (left instanceof DoubleScalarRel))
        {
            push(((DoubleScalarRel<?, ?>) left).times((DoubleScalarRel<?, ?>) right));
            return;
        }
        throwException("Cannot multiply with " + right + " as right hand operand");
    }

    /**
     * Divide the two top-most elements on the stack and push the result back onto the stack.
     */
    private void divide()
    {
        Object right = pop();
        Object left = pop();
        if ((left instanceof DoubleScalarRel) && (right instanceof DoubleScalarRel))
        {
            if (0.0 == ((DoubleScalarRel<?, ?>) right).si)
            {
                throwException("Division by 0");
            }
            push(((DoubleScalarRel<?, ?>) left).divide((DoubleScalarRel<?, ?>) right));
            return;
        }
        throwException("Cannot divide " + left + " by " + right);
    }

    /**
     * Add the two top-most elements on the stack and push the result back onto the stack.
     */
    private void add()
    {
        Object right = pop();
        Object left = pop();
        if ((left instanceof DoubleScalarRel) && (right instanceof DoubleScalarRel))
        {
            // Rel + Rel -> Rel
            DoubleScalar<?, ?> sum =
                    DoubleScalarRel.instantiate(((DoubleScalarRel<?, ?>) left).si + ((DoubleScalarRel<?, ?>) right).si,
                            ((DoubleScalarRel<?, ?>) left).getDisplayUnit().getStandardUnit());
            // System.out.println(left + " + " + right + " = " + sum);
            // Set display unit???
            push(sum);
            return;
        }
        if (right instanceof DoubleScalarAbs)
        {
            throwException("Cannot add an absolute value to some other value");
        }
        // System.out.println("left unit : " + ds.getDisplayUnit().getStandardUnit());
        // System.out.println("right unit: " + right.getDisplayUnit().getStandardUnit());
        if (left instanceof DoubleScalar && right instanceof DoubleScalar
                && (!((DoubleScalar<?, ?>) left).getDisplayUnit().getStandardUnit().toString()
                        .equals(((DoubleScalarRel<?, ?>) right).getDisplayUnit().getStandardUnit().toString())))
        {
            throwException("Cannot add " + left + " to " + right + " because the types are incompatible");
        }
        if ((left instanceof DoubleScalarAbs) && (right instanceof DoubleScalarRel))
        {
            // Abs + Rel -> Abs
            DoubleScalar<?, ?> sum =
                    DoubleScalarAbs.instantiate(((DoubleScalarAbs<?, ?, ?, ?>) left).si + ((DoubleScalarRel<?, ?>) right).si,
                            ((DoubleScalarAbs<?, ?, ?, ?>) left).getDisplayUnit().getStandardUnit());
            System.out.println(left + " + " + right + " = " + sum);
            // sum.setDisplayUnit(ds.getDisplayUnit());
            push(sum);
            return;
        }
        throwException("Cannot add " + left + " to " + right);
    }

    /**
     * Subtract the two top-most elements on the stack and push the result back onto the stack.
     */
    private void subtract()
    {
        Object right = pop();
        Object left = pop();
        if ((!(left instanceof DoubleScalar)) || (!(right instanceof DoubleScalar)))
        {
            throwException("Cannot subtract " + right + " from " + left);            
        }
        // Now we know that we're dealing with DoubleScalar objects
        // System.out.println("left unit : " + left.getDisplayUnit().getStandardUnit());
        // System.out.println("right unit: " + right.getDisplayUnit().getStandardUnit());
        if (!((DoubleScalar<?, ?>) left).getDisplayUnit().getStandardUnit().toString()
                .equals(((DoubleScalar<?, ?>) right).getDisplayUnit().getStandardUnit().toString()))
        {
            // System.out.println("left standard unit: " + left.getDisplayUnit().getStandardUnit().toString() + ", right
            // standard unit: "
            // + right.getDisplayUnit().getStandardUnit().toString());
            throwException("Cannot subtract " + right + " from " + left + " because the types are incompatible");
        }
        if ((left instanceof DoubleScalarAbs) && (right instanceof DoubleScalarAbs))
        {
            // Abs - Abs -> Rel
            DoubleScalar<?, ?> difference =
                    DoubleScalarRel.instantiate(((DoubleScalar<?, ?>) left).si - ((DoubleScalar<?, ?>) right).si,
                            ((DoubleScalar<?, ?>) left).getDisplayUnit().getStandardUnit());
            // System.out.println(left + " - " + right + " = " + difference);
            push(difference);
            return;
        }
        if ((left instanceof DoubleScalarAbs) && (right instanceof DoubleScalarRel))
        {
            // Abs - Rel -> Abs
            DoubleScalar<?, ?> difference =
                    DoubleScalarAbs.instantiate(((DoubleScalar<?, ?>) left).si - ((DoubleScalar<?, ?>) right).si,
                            ((DoubleScalar<?, ?>) left).getDisplayUnit().getStandardUnit());
            // System.out.println(left + " - " + right + " = " + difference);
            push(difference);
            return;
        }
        if ((left instanceof DoubleScalarRel) && (right instanceof DoubleScalarAbs))
        {
            // Rel - Abs -> error
            throwException("Cannot subtract " + right + " from " + left + " because the right operand is absolute");
        }
        if ((left instanceof DoubleScalarRel) && (right instanceof DoubleScalarRel))
        {
            // Rel - Rel -> Rel
            DoubleScalar<?, ?> difference =
                    DoubleScalarRel.instantiate(((DoubleScalar<?, ?>) left).si - ((DoubleScalar<?, ?>) right).si,
                            ((DoubleScalar<?, ?>) left).getDisplayUnit().getStandardUnit());
            // System.out.println(left + " - " + right + " = " + difference);
            push(difference);
            return;
        }
        throwException("Cannot subtract " + right + " from " + left);
    }

    /**
     * Parse a number and convert it to a SIScalar. If it is followed by an SI unit string inside square brackets, parse it into
     * the correct type.
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
                    throwException("Too many signs");
                }
                seenSign = true;
            }
            else if (seenExp && (!seenExpDigit) && ('-' == c || '+' == c))
            {
                if (seenExpSign)
                {
                    throwException("Too many signs in exponent");
                }
                seenExpSign = true;
            }
            else if (seenExp && seenExpSign && (!Character.isDigit(c)))
            {
                break;
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
                    throwException("Too many '.'");
                }
                if (seenExp)
                {
                    throwException("A \'.\' is not allowed after \'e\' or \'E\'");
                }
                seenRadix = true;
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
        if (seenExp && !seenExpDigit)
        {
            throwException("Exponent value missing");
        }
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
     * @return Object; the value of the function, variable, or mathematical or physical constant
     */
    private Object handleFunctionOrVariableOrNamedConstant()
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
                Object power = pop();
                Object base = pop();
                if ((base instanceof Dimensionless) && (power instanceof Dimensionless))
                {
                    return ((Dimensionless) base).pow(((Dimensionless) power).si);
                }
                throwException("Cannot raise " + base + " to power " + power);
            }
            else
            {
                throwException("Unknown " + argCount + " argument function " + name);
            }
        }
        else
        {
            // No opening parenthesis; name must be the name of a variable; look it up
            Object result = null == this.retrieveValue ? null : this.retrieveValue.lookup(name);
            if (null == result)
            {
                throwException("Cannot resolve variable " + name);
            }
            if ((result instanceof DoubleScalar) || (result instanceof Boolean))
                return result;
            throwException("Value of " + name + " is neither a DoubleScalar nor a Boolean");
        }
        return null; // cannot happen
    }

    /**
     * Execute a zero-argument function. These functions return a physical or mathematical constant, or invoke some system
     * function.
     * @param functionName String; name of the function to invoke
     * @return Object; the result of executing the function
     * @throws RuntimeException when no function with the specified name is known
     */
    private Object handleZeroArgumentFunction(final String functionName) throws RuntimeException
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

            case "VACUUMIMPEDANCE":
                return Constants.VACUUMIMPEDANCE;

            case "VACUUMPERMEABILITY":
                return Constants.VACUUMPERMEABILITY;

            case "VACUUMPERMITTIVITY":
                return Constants.VACUUMPERMITTIVITY;

            case "TRUE":
                return Boolean.TRUE;

            case "FALSE":
                return Boolean.FALSE;

        }
        throwException("Unknown zero-argument function " + functionName);
        return null;// cannot happen
    }

    /**
     * Execute a one-argument function on the top-element of the stack and push the result onto the stack.
     * @param functionName String; the name of the zero-argument function
     * @return DoubleScalar&lt;?,?&gt;; the result of evaluating the one-argument function
     */
    private Object executeOneArgumentFunction(final String functionName)
    {
        Object object = pop();
        // All implemented one-argument functions only operate on a DimensionLess value
        if (!(object instanceof DoubleScalar))
        {
            throwException("Function " + functionName + " cannot be applied to " + object); // should test if the name is valid
        }
        DoubleScalar<?, ?> ds = (DoubleScalar<?, ?>) object;
        if (!(ds.getDisplayUnit().getQuantity().equals(DimensionlessUnit.SI.getQuantity())))
        {
            throwException("Function " + functionName + " cannot be applied to " + ds);
        }
        Dimensionless dl = new Dimensionless(ds.si, DimensionlessUnit.SI);
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
                return (dl.cosh());

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
     * @param value Object; the value to push onto the evaluation stack
     */
    private void push(final Object value)
    {
        this.stack.add(value);
    }

    /**
     * Pop one value from the evaluation stack. Throw exception when stack underflows
     * @return Object; the value popped from the evaluation stack
     * @throws RuntimeException when the stack is currently empty
     */
    private Object pop() throws RuntimeException
    {
        if (0 == this.stack.size())
        {
            throwException("Stack empty");
        }
        return (this.stack.remove(this.stack.size() - 1));
    }

}
