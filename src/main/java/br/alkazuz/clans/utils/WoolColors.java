package br.alkazuz.clans.utils;

public enum WoolColors {
    WHITE("Branco", (short) 0, 'f'),
    ORANGE("Laranja", (short) 1, '6'),
    MAGENTA("Magenta", (short) 2, '5'),
    LIGHT_BLUE("Azul Claro", (short) 3, 'b'),
    YELLOW("Amarelo", (short) 4, 'e'),
    LIME("Verde Limão", (short) 5, 'a'),
    PINK("Rosa", (short) 6, 'd'),
    GRAY("Cinza", (short) 7, '8'),
    SILVER("Prata", (short) 8, '7'),
    CYAN("Ciano", (short) 9, '3'),
    PURPLE("Roxo", (short) 10, '5'),
    BLUE("Azul", (short) 11, '9'),
    BROWN("Vermelho", (short) 14, '4'),
    GREEN("Verde", (short) 13, '2');

    private String name;
    private short data;
    private char colorCode;

    WoolColors(final String name, final short data, final char colorCode) {
        this.name = name;
        this.data = data;
        this.colorCode = colorCode;
    }

    public char getColorCode() {
        return this.colorCode;
    }

    public String getName() {
        return this.name;
    }

    public short getData() {
        return this.data;
    }

    public static WoolColors getWoolColor(final String name) {
        for (final WoolColors woolColor : values()) {
            if (woolColor.getName().equalsIgnoreCase(name)) {
                return woolColor;
            }
        }
        return null;
    }

    public static WoolColors getWoolColor(final short data) {
        for (final WoolColors woolColor : values()) {
            if (woolColor.getData() == data) {
                return woolColor;
            }
        }
        return null;
    }
}
