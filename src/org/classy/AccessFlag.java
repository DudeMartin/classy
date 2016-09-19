package org.classy;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents an access flag. An access flag can modify the access and
 * properties of a class, interface, enum, or class member.
 *
 * @author Martin Tuskevicius
 */
public enum AccessFlag {

    ACC_PUBLIC (0x0001),
    ACC_PRIVATE (0x0002),
    ACC_PROTECTED (0x0004),
    ACC_STATIC (0x0008),
    ACC_FINAL (0x0010),
    ACC_SUPER (0x0020),
    ACC_SYNCHRONIZED (0x0020),
    ACC_VOLATILE (0x0040),
    ACC_BRIDGE (0x0040),
    ACC_TRANSIENT (0x0080),
    ACC_VARARGS (0x0080),
    ACC_NATIVE (0x0100),
    ACC_INTERFACE (0x0200),
    ACC_ABSTRACT (0x0400),
    ACC_STRICT (0x0800),
    ACC_SYNTHETIC (0x1000),
    ACC_ANNOTATION (0x2000),
    ACC_ENUM (0x4000),
    ACC_MANDATED (0x8000);

    /**
     * The set of flags that can be attributed to a class, interface, or enum.
     */
    public static final Set<AccessFlag> CLASS_FLAGS = Collections.unmodifiableSet(EnumSet.of(
            ACC_PUBLIC,
            ACC_FINAL,
            ACC_SUPER,
            ACC_INTERFACE,
            ACC_ABSTRACT,
            ACC_SYNTHETIC,
            ACC_ANNOTATION,
            ACC_ENUM));

    /**
     * The set of flags that can be attributed to a field.
     */
    public static final Set<AccessFlag> FIELD_FLAGS = Collections.unmodifiableSet(EnumSet.of(
            ACC_PUBLIC,
            ACC_PRIVATE,
            ACC_PROTECTED,
            ACC_STATIC,
            ACC_FINAL,
            ACC_VOLATILE,
            ACC_TRANSIENT,
            ACC_SYNTHETIC,
            ACC_ENUM));

    /**
     * The set of flags that can be attributed to a method.
     */
    public static final Set<AccessFlag> METHOD_FLAGS = Collections.unmodifiableSet(EnumSet.of(
            ACC_PUBLIC,
            ACC_PRIVATE,
            ACC_PROTECTED,
            ACC_STATIC,
            ACC_FINAL,
            ACC_SYNCHRONIZED,
            ACC_BRIDGE,
            ACC_VARARGS,
            ACC_NATIVE,
            ACC_ABSTRACT,
            ACC_STRICT,
            ACC_SYNTHETIC));

    /**
     * The set of flags that can be attributed to a formal method parameter.
     */
    public static final Set<AccessFlag> PARAMETER_FLAGS = Collections.unmodifiableSet(EnumSet.of(
            ACC_FINAL,
            ACC_SYNTHETIC,
            ACC_MANDATED));

    /**
     * The set of flags that can be attributed to an inner class.
     */
    public static final Set<AccessFlag> INNER_CLASS_FLAGS = Collections.unmodifiableSet(EnumSet.of(
            ACC_PUBLIC,
            ACC_PRIVATE,
            ACC_PROTECTED,
            ACC_STATIC,
            ACC_FINAL,
            ACC_INTERFACE,
            ACC_ABSTRACT,
            ACC_SYNTHETIC,
            ACC_ANNOTATION,
            ACC_ENUM));

    /**
     * The numerical value of this flag.
     */
    public final int value;

    AccessFlag(int value) {
        this.value = value;
    }

    /**
     * Returns the set of access flags corresponding to the provided bit mask.
     * This method requires a source flag set to be provided because some flags
     * have identical numerical values.
     *
     * @param mask   the bit mask.
     * @param source the source access flag set.
     * @return the set of access flags. The set could be empty, but never
     *         <code>null</code>. If <code>source</code> is <code>null</code>,
     *         then an empty set is returned.
     */
    public static EnumSet<AccessFlag> forMask(int mask, Set<AccessFlag> source) {
        EnumSet<AccessFlag> flags = EnumSet.noneOf(AccessFlag.class);
        if (source != null) {
            for (AccessFlag flag : source) {
                if ((mask & flag.value) != 0) {
                    flags.add(flag);
                }
            }
        }
        return flags;
    }

    /**
     * Returns a set of access flags.
     *
     * @param flags the array of flags to return as a set.
     * @return the set of access flags. The set could be empty, but never
     *         <code>null</code>.
     */
    public static EnumSet<AccessFlag> forFlags(AccessFlag... flags) {
        EnumSet<AccessFlag> flagSet = EnumSet.noneOf(AccessFlag.class);
        for (AccessFlag flag : flags) {
            flagSet.add(flag);
        }
        return flagSet;
    }

    /**
     * Represents a set of access flags as a bit mask.
     *
     * @param flags the set of flags.
     * @return the set represented as a bit mask. If <code>flags</code> is
     *         <code>null</code>, then <code>0</code> is returned.
     */
    public static int toBitMask(Set<AccessFlag> flags) {
        int value = 0;
        if (flags != null) {
            for (AccessFlag flag : flags) {
                value |= flag.value;
            }
        }
        return value;
    }
}