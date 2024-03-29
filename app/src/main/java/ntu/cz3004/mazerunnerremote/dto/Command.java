package ntu.cz3004.mazerunnerremote.dto;

import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by calvin on 31/1/2018.
 */

public class Command {
    private String commandString;
    private int h;
    private int w;
    private int direction;

    public Command(CommandTypes commandType) {
        commandString = typeToString(commandType);
    }

    public void setLocation(int w, int h) {
        this.h = h;
        this.w = w;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getCommandString() {

        if (commandString == typeToString(CommandTypes.FORWARD) ||
                commandString == typeToString(CommandTypes.REVERSE) ||
                commandString == typeToString(CommandTypes.ROTATE_LEFT) ||
                commandString == typeToString(CommandTypes.ROTATE_RIGHT) ||
                commandString == typeToString(CommandTypes.STRAFE_LEFT) ||
                commandString == typeToString(CommandTypes.STRAFE_RIGHT)
                ) {
            return commandString;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("command", commandString);
        } catch (Exception e) {
        }
        if (commandString == typeToString(CommandTypes.BEGIN_FASTEST_PATH)) {
            try {
                obj.put("wayPoint", new JSONArray(new int[]{19 - h, w}));
            } catch (Exception e) {
            }
        } else if (commandString == typeToString(CommandTypes.BEGIN_EXPLORE)) {
            try {
                obj.put("robotPos", new JSONArray(new int[]{18 - h, w + 1, direction}));
            } catch (Exception e) {
            }
        }
        return obj.toString();
    }

    private String typeToString(CommandTypes commandType) {
        switch (commandType) {
            case FORWARD:
                return "F 1";
            case REVERSE:
                return "B 1";
            case STRAFE_LEFT:
                return "L 1,F 1,R 1";
            case STRAFE_RIGHT:
                return "R 1,F 1,L 1";
            case ROTATE_LEFT:
                return "L 1";
            case ROTATE_RIGHT:
                return "R 1";
            case BEGIN_EXPLORE:
                return "beginExplore";
            case BEGIN_FASTEST_PATH:
                return "beginFastest";
            case SEND_MAP:
                return "sendArena";
            case AUTO_START:
                return "autoStart";
            case AUTO_STOP:
                return "autoStop";
//            case PATH_WAYPOINT:
//                return "wayPoint";
//            case ROBOT_LOCATION:
//                return "robotPos";
            default:
                return "";
        }
    }


    public enum CommandTypes {
        FORWARD, REVERSE, STRAFE_LEFT, STRAFE_RIGHT, ROTATE_LEFT, ROTATE_RIGHT,
        BEGIN_EXPLORE, BEGIN_FASTEST_PATH, SEND_MAP,

        // Everything below are not supported by AMT
        AUTO_START, AUTO_STOP
        //, PATH_WAYPOINT, ROBOT_LOCATION
    }
}