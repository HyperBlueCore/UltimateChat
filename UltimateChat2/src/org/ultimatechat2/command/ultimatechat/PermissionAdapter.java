package org.ultimatechat2.command.ultimatechat;

import org.bukkit.command.CommandSender;
import org.ultimatechat2.command.CommandHandler;
import org.ultimatechat2.util.function.PermissionFunction;

public abstract class PermissionAdapter<E extends CommandSender> extends PermissionFunction<E> {

    private final String errorMessage;

    public PermissionAdapter(String permission, String errorMessage) {
        super(permission);
        this.errorMessage = errorMessage;
    }

    public abstract void ifPresent(E e, CommandHandler handler, String label, String[] args);

    public void ifAbsent(E e, CommandHandler handler, String label, String[] args) {
        e.sendMessage(errorMessage);
    }

    private Object[] checkParse(Object[] args) {
        CommandHandler handler = null;
        String label = null;
        String[] arguments = null;
        if (args.length > 0) {
            Object value;
            if ((value = args[0]) instanceof CommandHandler)
                handler = (CommandHandler) value;
            if (args.length > 1) {
                label = args[1] != null ? args[1].toString() : null;
                if (args.length > 2) {
                    if ((value = args[2]) instanceof String[])
                        arguments = (String[]) value;
                }
            }
        }

        if (label == null)
            label = "";
        if (arguments == null)
            arguments = new String[0];

        return new Object[] {handler, label, arguments};
    }

    @Override
    public void ifPresent(E e, Object[] args) {
        Object[] array = checkParse(args);
        ifPresent(e, (CommandHandler) array[0], (String) array[1], (String[]) array[2]);
    }

    @Override
    public void ifAbsent(E e, Object[] args) {
        Object[] array = checkParse(args);
        ifAbsent(e, (CommandHandler) array[0], (String) array[1], (String[]) array[2]);
    }
}
