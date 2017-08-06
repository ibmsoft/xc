/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package com.cubedrive.sheet.service.sheet._export.dataformat;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;



public class LocaleDate {

    public static final String date_Locale_en_us = "[$-0409]";

    private final static Map<String, String> localePrefixes = prepareLocalePrefixes();

    private final static Map<String, Locale> locales = prepareLocales();

    private Map<String, DateFormatSymbols> dateFormatSymbolMap = new HashMap<>();

    private static final LocaleDate _instance = new LocaleDate();

    private static final Map<String, String> empty_format_map = Collections.emptyMap();

    private final Map<String, Map<String, String>> dateI10nMap = new HashMap<>();
    
    private final Map<String, Map<String, String>> datetimeI10nMap = new HashMap<>();


    private LocaleDate() {

    }

    public DateFormatSymbols getDateFormatSymbols(String excelLocale) {
        DateFormatSymbols dateFormatSymboles = dateFormatSymbolMap.get(excelLocale);
        if (dateFormatSymboles == null) {
            Locale locale = locales.get(excelLocale);
            if (locale == null) {
                locale = Locale.US;
            }
            dateFormatSymboles = new DateFormatSymbols(locale);
            dateFormatSymbolMap.put(excelLocale, dateFormatSymboles);
        }
        return dateFormatSymboles;
    }

    public String getExcelDateFormat(String locale, String feyaDateFormat) throws IOException {
        Map<String, String> fmtMap = dateI10nMap.get(locale);
        if (fmtMap == null) {
            fmtMap = loadDateI10nFmt(locale);
            dateI10nMap.put(locale, fmtMap);
        }
        return fmtMap.get(feyaDateFormat);
    }
    
    public String getExcelDatetimeFormat(String locale, String feyaDateFormat) throws IOException {
        Map<String, String> fmtMap = datetimeI10nMap.get(locale);
        if (fmtMap == null) {
            fmtMap = loadDatetimeI10nFmt(locale);
            datetimeI10nMap.put(locale, fmtMap);
        }
        return fmtMap.get(feyaDateFormat);
    }


    public static LocaleDate getInstance() {
        return _instance;
    }


    private static Map<String, String> prepareLocalePrefixes() {
        Map<String, String> result = new HashMap<String, String>();

        result.put("ar_AE", "[$-3801]");
        result.put("ar_BH", "[$-3C01]");
        result.put("ar_DZ", "[$-1401]");
        result.put("ar_EG", "[$-C01]");
        result.put("ar_IQ", "[$-0801]");
        result.put("ar_JO", "[$-2C01]");
        result.put("ar_KW", "[$-3401]");
        result.put("ar_LB", "[$-3001]");
        result.put("ar_LY", "[$-1001]");
        result.put("ar_MA", "[$-1801]");
        result.put("ar_OM", "[$-2001]");
        result.put("ar_QA", "[$-4001]");
        result.put("ar_SA", "[$-0401]");
        result.put("ar_SY", "[$-2801]");
        result.put("ar_TN", "[$-1C01]");
        result.put("ar_YE", "[$-2401]");
        result.put("be", "[$-0423]");
        result.put("bg", "[$-0402]");
        result.put("ca", "[$-0403]");
        result.put("cs", "[$-0405]");
        result.put("da", "[$-0406]");
        result.put("de_AT", "[$-C07]");
        result.put("de_CH", "[$-0807]");
        result.put("de_DE", "[$-0407]");
        result.put("de_LU", "[$-1007]");
        result.put("el", "[$-0408]");
        result.put("en_AU", "[$-C09]");
        result.put("en_CA", "[$-1009]");
        result.put("en_GB", "[$-0809]");
        result.put("en_IE", "[$-1809]");
        result.put("en_IN", "[$-4009]");
        result.put("en_NZ", "[$-1409]");
        result.put("en_PH", "[$-3409]");
        result.put("en_US", "[$-0409]");
        result.put("en_ZA", "[$-1C09]");
        result.put("es_AR", "[$-2C0A]");
        result.put("es_BO", "[$-400A]");
        result.put("es_CL", "[$-340A]");
        result.put("es_CO", "[$-240A]");
        result.put("es_CR", "[$-140A]");
        result.put("es_DO", "[$-1C0A]");
        result.put("es_EC", "[$-300A]");
        result.put("es_ES", "[$-40A]");
        result.put("es_GT", "[$-100A]");
        result.put("es_HN", "[$-480A]");
        result.put("es_MX", "[$-80A]");
        result.put("es_NI", "[$-4C0A]");
        result.put("es_PA", "[$-180A]");
        result.put("es_PE", "[$-280A]");
        result.put("es_PR", "[$-500A]");
        result.put("es_PY", "[$-3C0A]");
        result.put("es_SV", "[$-440A]");
        result.put("es_UY", "[$-380A]");
        result.put("es_VE", "[$-200A]");
        result.put("et", "[$-0425]");
        result.put("fi", "[$-40B]");
        result.put("fr_BE", "[$-80C]");
        result.put("fr_CA", "[$-C0C]");
        result.put("fr_CH", "[$-100C]");
        result.put("fr_FR", "[$-40C]");
        result.put("fr_LU", "[$-140C]");
        result.put("hr", "[$-41A]");
        result.put("hu", "[$-40E]");
        result.put("is", "[$-40F]");
        result.put("it_CH", "[$-0810]");
        result.put("it_IT", "[$-0410]");
        result.put("ja", "[$-0411]");
        result.put("ko", "[$-0412]");
        result.put("lt", "[$-0427]");
        result.put("lv", "[$-0426]");
        result.put("mk", "[$-42F]");
        result.put("ms_MY", "[$-43E]");
        result.put("mt", "[$-43A]");
        result.put("ne", "[$-0461]");
        result.put("nl_BE", "[$-0813]");
        result.put("nl_NL", "[$-0413]");
        result.put("no_NO", "[$-0814]");
        result.put("pl", "[$-0415]");
        result.put("pt_BR", "[$-0416]");
        result.put("pt_PT", "[$-0816]");
        result.put("ro", "[$-0418]");
        result.put("ru", "[$-0419]");
        result.put("sk", "[$-41B]");
        result.put("sl", "[$-0424]");
        result.put("sq", "[$-41C]");
        result.put("sr", "[$-C1A]");
        result.put("sv_SE", "[$-41D]");
        result.put("th", "[$-41E]");
        result.put("tr", "[$-41F]");
        result.put("uk", "[$-0422]");
        result.put("vi", "[$-42A]");
        result.put("zh_CN", "[$-0804]");
        result.put("zh_HK", "[$-C04]");
        result.put("zh_SG", "[$-1004]");
        result.put("zh_TW", "[$-0404]");
        return result;
    }

    private static Map<String, Locale> prepareLocales() {
        Map<String, Locale> result = new HashMap<String, Locale>();
        for (Map.Entry<String, String> entry : localePrefixes.entrySet()) {
            String excelPrefix = entry.getValue();
            String localeIso = entry.getKey();
            String lang = localeIso.substring(0, 2);
            String country = "";
            if (localeIso.length() == 5) {
                country = localeIso.substring(3, 5);
            }
            result.put(excelPrefix, new Locale(lang, country));
        }
        return result;
    }


    private Map<String, String> loadDateI10nFmt(String locale) throws IOException {
        String fmt = String.format("date_%s.fmt", locale);
        InputStream in = getClass().getClassLoader().getResourceAsStream("com/cubedrive/sheet/service/sheet/_export/dataformat/datei10n/" + fmt);
        Map<String, String> formats = empty_format_map;
        if (in != null) {
            try {
                formats = new HashMap<>();
                List<String> lines = IOUtils.readLines(in);
                for (String line : lines) {
                    String trimLine = line == null ? "":line.trim();
                    if (trimLine.length()==0 || trimLine.startsWith("#"))
                        continue;
                    else {
                        String[] pairs = trimLine.split("<=>");
                        formats.put(pairs[0].trim(),pairs[1].trim());
                    }
                }
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        return formats;
    }
    
    private Map<String, String> loadDatetimeI10nFmt(String locale) throws IOException {
        String fmt = String.format("datetime_%s.fmt", locale);
        InputStream in = getClass().getClassLoader().getResourceAsStream("com/cubedrive/sheet/service/sheet/_export/dataformat/datei10n/" + fmt);
        Map<String, String> formats = empty_format_map;
        if (in != null) {
            try {
                formats = new HashMap<>();
                List<String> lines = IOUtils.readLines(in);
                for (String line : lines) {
                	String trimLine = line == null ? "":line.trim();
                    if (trimLine.length()==0 || trimLine.startsWith("#"))
                        continue;
                    else {
                        String[] pairs = trimLine.split("<=>");
                        formats.put(pairs[0].trim(), pairs[1].trim());
                    }
                }
            } finally {
                IOUtils.closeQuietly(in);
            }
        }
        return formats;
    }


}

