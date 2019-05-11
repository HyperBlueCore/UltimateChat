package org.ultimatechat2.util.function;

import org.bukkit.permissions.Permissible;

public abstract class PermissionFunction<E extends Permissible> implements Function<E> {

    private final String permission;

    public PermissionFunction(String permission) {
        this.permission = permission;
    }

    public static <E extends Permissible> void executeFunction(PermissionFunction<E> function, E e, Object[] args) {
        if (function == null)
            throw new NullPointerException("Function cannot be null");
        if (e == null)
            throw new NullPointerException("Permissible cannot be null");

        if (e.hasPermission(function.permission))
            function.ifPresent(e, args);
        else
            function.ifAbsent(e, args);
    }

    @Override
    public abstract void ifPresent(E e, Object[] args);

    @Override
    public abstract void ifAbsent(E e, Object[] args);

}
