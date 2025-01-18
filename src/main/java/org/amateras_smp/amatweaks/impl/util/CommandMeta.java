// Copyright (c) 2025 The Ama-Tweaks Authors
// This file is part of the Ama-Tweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

public class CommandMeta {
    public String command;
    public String arguments;

    public CommandMeta(String command, String arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String asString() {
        return (command + " " + arguments).strip();
    }

    @Override
    public String toString() {
        return ("command: \"" + command + "\", arguments : \"" + arguments + "\"").strip();
    }
}
