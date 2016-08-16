package org.classy;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum AccessFlag {

    ACC_PUBLIC      (0x0001),
    ACC_PRIVATE     (0x0002),
    ACC_PROTECTED   (0x0004),
    ACC_STATIC      (0x0008),
    ACC_FINAL       (0x0010),
    ACC_SUPER       (0x0020),
    ACC_SYNCHRONIZED(0x0020),
    ACC_VOLATILE    (0x0040),
    ACC_BRIDGE      (0x0040),
    ACC_TRANSIENT   (0x0080),
    ACC_VARARGS     (0x0080),
    ACC_NATIVE      (0x0100),
    ACC_INTERFACE   (0x0200),
    ACC_ABSTRACT    (0x0400),
    ACC_STRICT      (0x0800),
    ACC_SYNTHETIC   (0x1000),
    ACC_ANNOTATION  (0x2000),
    ACC_ENUM        (0x4000),
    ACC_MANDATED    (0x8000);

    public static final Set<AccessFlag> classFlags = Collections.unmodifiableSet(EnumSet.of(
            ACC_PUBLIC,
            ACC_FINAL,
            ACC_SUPER,
            ACC_INTERFACE,
            ACC_ABSTRACT,
            ACC_SYNTHETIC,
            ACC_ANNOTATION,
            ACC_ENUM));

    public static final Set<AccessFlag> fieldFlags = Collections.unmodifiableSet(EnumSet.of(
            ACC_PUBLIC,
            ACC_PRIVATE,
            ACC_PROTECTED,
            ACC_STATIC,
            ACC_FINAL,
            ACC_VOLATILE,
            ACC_TRANSIENT,
            ACC_SYNTHETIC,
            ACC_ENUM));

    public static final Set<AccessFlag> methodFlags = Collections.unmodifiableSet(EnumSet.of(
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

    public static final Set<AccessFlag> parameterFlags = Collections.unmodifiableSet(EnumSet.of(
            ACC_FINAL,
            ACC_SYNTHETIC,
            ACC_MANDATED));

    public static final Set<AccessFlag> innerClassFlags = Collections.unmodifiableSet(EnumSet.of(
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

    public final int value;

    AccessFlag(int value) {
        this.value = value;
    }

    public static EnumSet<AccessFlag> set(int mask, Set<AccessFlag> source) {
        EnumSet<AccessFlag> flags = EnumSet.noneOf(AccessFlag.class);
        for (AccessFlag flag : source) {
            if ((mask & flag.value) != 0) {
                flags.add(flag);
            }
        }
        return flags;
    }

    public static EnumSet<AccessFlag> set(AccessFlag... flags) {
        switch (flags.length) {
            case 0:
                return EnumSet.noneOf(AccessFlag.class);
            case 1:
                return EnumSet.of(flags[0]);
            case 2:
                return EnumSet.of(flags[0], flags[1]);
            case 3:
                return EnumSet.of(flags[0], flags[1], flags[2]);
            case 4:
                return EnumSet.of(flags[0], flags[1], flags[2], flags[3]);
            default:
                EnumSet<AccessFlag> set = EnumSet.noneOf(AccessFlag.class);
                for (AccessFlag flag : flags) {
                    set.add(flag);
                }
                return set;
        }
    }
}