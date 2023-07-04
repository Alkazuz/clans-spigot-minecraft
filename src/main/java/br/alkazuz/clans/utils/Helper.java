package br.alkazuz.clans.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class Helper {

    public static Player matchOnePlayer(String playername) {
        List<Player> players = Bukkit.matchPlayer(playername);
        if (players.size() == 1)
            return players.get(0);
        return null;
    }

    public static String parseColors(String msg) {
        return msg.replace("&", "");
    }

    public static boolean isInteger(Object o) {
        return o instanceof Integer;
    }

    public static boolean isByte(String input) {
        try {
            Byte.parseByte(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isShort(String input) {
        try {
            Short.parseShort(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isFloat(String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isString(Object o) {
        return o instanceof String;
    }

    public static boolean isBoolean(Object o) {
        return o instanceof Boolean;
    }

    public static String removeChar(String s, char c) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c)
                r.append(s.charAt(i));
        }
        return r.toString();
    }

    public static String removeFirstChar(String s, char c) {
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != c) {
                r.append(s.charAt(i));
                break;
            }
        }
        return r.toString();
    }

    public static String capitalize(String content) {
        if (content.length() < 2)
            return content;
        String first = content.substring(0, 1).toUpperCase();
        return first + content.substring(1);
    }

    public static String plural(int count, String word, String ending) {
        return (count == 1) ? word : (word + ending);
    }

    public static String toColor(String hexValue) {
        if (hexValue == null)
            return "";
        return ChatColor.getByChar(hexValue).toString();
    }

    public static List fromArray(String... values) {
        List<String> out = new ArrayList<String>(Arrays.asList(values));
        out.remove("");
        return out;
    }

    public static HashSet fromArray2(String... values) {
        HashSet<String> out = new HashSet<String>(Arrays.asList(values));
        out.remove("");
        return out;
    }

    public static <T> Collection<T> convertArray(T... values) {
        return Arrays.asList(values);
    }

    public static Set<Player> fromPlayerArray(Player... values) {
        return new HashSet<Player>(Arrays.asList(values));
    }

    public static String[] toArray(Collection<String> list) {
        return list.<String>toArray(new String[list.size()]);
    }

    public static <T> T[] removeFirst(T[] args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    public static String toMessage(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(' ');
        return sb.toString().trim();
    }

    public static String toMessage(String[] args, String sep) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(' ').append(", ");
        return stripTrailing(sb.toString(), ", ");
    }

    public static String toMessage(Collection<String> args, String sep) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args)
            sb.append(arg).append(' ').append(sep);
        return stripTrailing(sb.toString(), sep);
    }

    public static String stripColors(String msg) {
		String out = msg.replaceAll("[&][0-9a-f]", "");
		out = out.replaceAll(String.valueOf('Â'), "");
		return out.replaceAll("[§][0-9a-f]", "");
	  }
	  
	  public static String getLastColorCode(String msg) {
		msg = msg.replaceAll(String.valueOf('Â'), "").trim();
		if (msg.length() < 2)
		  return ""; 
		String one = msg.substring(msg.length() - 2, msg.length() - 1);
		String two = msg.substring(msg.length() - 1);
		if (one.equals("§"))
		  return one + two; 
		if (one.equals("&"))
		  return toColor(two); 
		return "";
	  }

    public static String cleanTag(String tag) {
        return stripColors(tag).toLowerCase();
    }

    public static String stripTrailing(String msg, String sep) {
        if (msg.length() < sep.length())
            return msg;
        String out = msg;
        String first = msg.substring(0, sep.length());
        String last = msg.substring(msg.length() - sep.length(), msg.length());
        if (first.equals(sep))
            out = msg.substring(sep.length());
        if (last.equals(sep))
            out = msg.substring(0, msg.length() - sep.length());
        return out;
    }

    public static String generatePageSeparator(String sep) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < 320; i++)
            out.append(sep);
        return out.toString();
    }

    public static boolean isOnline(String playerName) {
        return (Bukkit.getPlayerExact(playerName) != null);
    }

    public static boolean testURL(String strUrl) {
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            urlConn.connect();
            if (urlConn.getResponseCode() != 200)
                return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static String escapeQuotes(String str) {
        if (str == null)
            return "";
        return str.replace("'", "''");
    }

    public static String toLocationString(Location loc) {
        return loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + " " + loc.getWorld().getName();
    }

    public static boolean isSameBlock(Location loc, Location loc2) {
        if (loc.getBlockX() == loc2.getBlockX() && loc.getBlockY() == loc2.getBlockY() && loc.getBlockZ() == loc2.getBlockZ())
            return true;
        return false;
    }

    public static boolean isSameLocation(Location loc, Location loc2) {
        if (loc.getX() == loc2.getX() && loc.getY() == loc2.getY() && loc.getZ() == loc2.getZ())
            return true;
        return false;
    }

    public static Map sortByValue(Map map) {
        List<?> list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable)((Map.Entry)o2).getValue()).compareTo(((Map.Entry)o1).getValue());
            }
        });
        Map<Object, Object> result = new LinkedHashMap<Object, Object>();
        for (Iterator<?> it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static boolean isVanished(Player player) {
        if (player != null && player.hasMetadata("vanished") &&
                !player.getMetadata("vanished").isEmpty())
            return ((MetadataValue)player.getMetadata("vanished").get(0)).asBoolean();
        return false;
    }
}
