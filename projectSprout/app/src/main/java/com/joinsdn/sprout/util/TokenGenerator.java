package com.joinsdn.sprout.util;

import java.util.Random;

public class TokenGenerator {
    private static final String[] COLORS =
            new String[] {
                    "Red",
                    "Orange",
                    "Yellow",
                    "Green",
                    "Blue",
                    "Indigo",
                    "Violet",
                    "Purple",
                    "Lavender",
                    "Fuchsia",
                    "Plum",
                    "Orchid",
                    "Magenta",
            };

    private static final String[] TREATS =
            new String[] {
                    "Alpha",
                    "Beta",
                    "Cupcake",
                    "Donut",
                    "Eclair",
                    "Froyo",
                    "Gingerbread",
                    "Honeycomb",
                    "Ice Cream Sandwich",
                    "Jellybean",
                    "Kit Kat",
                    "Lollipop",
                    "Marshmallow",
                    "Nougat",
                    "Oreo",
                    "Pie"
            };

    private static final Random generator = new Random();

    private TokenGenerator() {}

    /** Generate a random Android agent codename */
    public static String generate() {
        String color = COLORS[generator.nextInt(COLORS.length)];
        String treat = TREATS[generator.nextInt(TREATS.length)];
        return color + " " + treat;
    }
}
