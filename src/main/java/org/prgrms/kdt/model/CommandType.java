package org.prgrms.kdt.model;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CommandType {
    CREATE("create"),
    LIST("list"),
    EXIT("exit");

    private final String command;
    private static final Map<String, CommandType> commandMap = Collections.unmodifiableMap(
            Stream.of(values())
                    .collect(Collectors.toMap(CommandType::getCommand, Function.identity()))
    );

    public String getCommand() {
        return command;
    }

    CommandType(String command) {
        this.command = command;
    }

    /**
     * command 타입 찾기
     * @param command
     * @return CommandType
     * @throws InvalidParameterException command가 null 이거나, 지정된 command를 찾을 수 없습니다.
     */
    public static CommandType findCommandType(String command) {
        validateCommandNullSafe(command);

        return Optional.ofNullable(commandMap.get(command.toLowerCase()))
                .orElseThrow(InvalidParameterException::new);
    }

    private static void validateCommandNullSafe(String command) {
        if (command == null) {
            throw new InvalidParameterException();
        }
    }
}
