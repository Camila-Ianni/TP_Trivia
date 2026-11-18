package com.example.triviamaster.utils;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import java.util.Arrays;
import java.util.List;

public final class CountryHelper {

    public static final class CountryOption {
        private final String code;
        private final String flag;
        private final String name;
        private final String languageTag;

        public CountryOption(String code, String flag, String name, String languageTag) {
            this.code = code;
            this.flag = flag;
            this.name = name;
            this.languageTag = languageTag;
        }

        public String getCode() {
            return code;
        }

        public String getFlag() {
            return flag;
        }

        public String getName() {
            return name;
        }

        public String getLanguageTag() {
            return languageTag;
        }

        @Override
        public String toString() {
            return flag + " " + name;
        }
    }

    private static final List<CountryOption> COUNTRIES = Arrays.asList(
            new CountryOption("AR", "\uD83C\uDDE6\uD83C\uDDF7", "Argentina", "es"),
            new CountryOption("US", "\uD83C\uDDFA\uD83C\uDDF8", "United States", "en"),
            new CountryOption("GB", "\uD83C\uDDEC\uD83C\uDDE7", "United Kingdom", "en"),
            new CountryOption("CA", "\uD83C\uDDE8\uD83C\uDDE6", "Canada", "en"),
            new CountryOption("IT", "\uD83C\uDDEE\uD83C\uDDF9", "Italia", "it"),
            new CountryOption("ES", "\uD83C\uDDEA\uD83C\uDDF8", "España", "es"),
            new CountryOption("MX", "\uD83C\uDDF2\uD83C\uDDFD", "México", "es"),
            new CountryOption("CO", "\uD83C\uDDE8\uD83C\uDDF4", "Colombia", "es"),
            new CountryOption("CL", "\uD83C\uDDE8\uD83C\uDDF1", "Chile", "es"),
            new CountryOption("PE", "\uD83C\uDDF5\uD83C\uDDEA", "Perú", "es"),
            new CountryOption("UY", "\uD83C\uDDFA\uD83C\uDDFE", "Uruguay", "es"),
            new CountryOption("VE", "\uD83C\uDDFB\uD83C\uDDEA", "Venezuela", "es"),
            new CountryOption("EC", "\uD83C\uDDEA\uD83C\uDDE8", "Ecuador", "es"),
            new CountryOption("PY", "\uD83C\uDDF5\uD83C\uDDFE", "Paraguay", "es"),
            new CountryOption("BO", "\uD83C\uDDE7\uD83C\uDDF4", "Bolivia", "es")
    );

    private CountryHelper() {
    }

    public static List<CountryOption> getCountries() {
        return COUNTRIES;
    }

    public static CountryOption getByCode(String code) {
        if (code != null) {
            for (CountryOption option : COUNTRIES) {
                if (option.getCode().equalsIgnoreCase(code.trim())) {
                    return option;
                }
            }
        }
        return COUNTRIES.get(0);
    }

    public static String getLanguageForCountry(String code) {
        return getByCode(code).getLanguageTag();
    }

    public static void applyLocaleForCountry(String code) {
        String lang = getLanguageForCountry(code);
        LocaleListCompat target = LocaleListCompat.forLanguageTags(lang);
        LocaleListCompat current = AppCompatDelegate.getApplicationLocales();
        if (!target.toLanguageTags().equals(current.toLanguageTags())) {
            AppCompatDelegate.setApplicationLocales(target);
        }
    }
}
