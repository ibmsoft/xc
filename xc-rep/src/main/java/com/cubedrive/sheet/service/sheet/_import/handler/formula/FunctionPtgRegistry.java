/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.service.sheet._import.handler.formula;


import java.util.HashMap;
import java.util.Map;

class FunctionPtgRegistry {

    private static final Map<String, FuncVarPtg> _functionPtgByName = new HashMap<>();

    private static void registerFunction(FuncVarPtg funcVarPtg) {
        _functionPtgByName.put(funcVarPtg.getName().toLowerCase(), funcVarPtg);
    }

    static {
        //date and time functions
        FuncVarPtg date = new FuncVarPtg("DATE", 3, 3);
        registerFunction(date);

        FuncVarPtg datevalue = new FuncVarPtg("DATEVALUE", 1, 1);
        registerFunction(datevalue);

        FuncVarPtg day = new FuncVarPtg("DAY", 1, 1);
        registerFunction(day);

        FuncVarPtg days360 = new FuncVarPtg("DAYS360", 2, 3);
        registerFunction(days360);

        FuncVarPtg edate = new FuncVarPtg("EDATE", 2, 2);
        registerFunction(edate);

        FuncVarPtg eomonth = new FuncVarPtg("EOMONTH", 2, 2);
        registerFunction(eomonth);

        FuncVarPtg hour = new FuncVarPtg("HOUR", 1, 1);
        registerFunction(hour);

        FuncVarPtg minute = new FuncVarPtg("MINUTE", 1, 1);
        registerFunction(minute);

        FuncVarPtg month = new FuncVarPtg("MONTH", 1, 1);
        registerFunction(month);

        FuncVarPtg networkdays = new FuncVarPtg("NETWORKDAYS", 2, 3);
        registerFunction(networkdays);

        FuncVarPtg now = new FuncVarPtg("NOW", 0, 0);
        registerFunction(now);

        FuncVarPtg second = new FuncVarPtg("SECOND", 1, 1);
        registerFunction(second);

        FuncVarPtg time = new FuncVarPtg("TIME", 3, 3);
        registerFunction(time);

        FuncVarPtg timevalue = new FuncVarPtg("TIMEVALUE", 1, 1);
        registerFunction(timevalue);

        FuncVarPtg today = new FuncVarPtg("TODAY", 0, 0);
        registerFunction(today);

        FuncVarPtg weekday = new FuncVarPtg("WEEKDAY", 1, 2);
        registerFunction(weekday);

        FuncVarPtg weeknum = new FuncVarPtg("WEEKNUM", 1, 2);
        registerFunction(weeknum);

        FuncVarPtg workday = new FuncVarPtg("WORKDAY", 2, 3);
        registerFunction(workday);

        FuncVarPtg year = new FuncVarPtg("year", 1, 1);
        registerFunction(year);

        FuncVarPtg yearfrac = new FuncVarPtg("YEARFRAC", 2, 3);
        registerFunction(yearfrac);

        //engineering functions
        FuncVarPtg besseli = new FuncVarPtg("BESSELI", 2, 2);
        registerFunction(besseli);

        FuncVarPtg besselj = new FuncVarPtg("BESSELJ", 2, 2);
        registerFunction(besselj);

        FuncVarPtg besselk = new FuncVarPtg("BESSELK", 2, 2);
        registerFunction(besselk);

        FuncVarPtg bessely = new FuncVarPtg("BESSELY", 2, 2);
        registerFunction(bessely);

        FuncVarPtg bin2dec = new FuncVarPtg("BIN2DEC", 1, 1);
        registerFunction(bin2dec);

        FuncVarPtg bin2hex = new FuncVarPtg("BIN2HEX", 1, 2);
        registerFunction(bin2hex);

        FuncVarPtg bin2oct = new FuncVarPtg("BIN2OCT", 1, 2);
        registerFunction(bin2oct);

        FuncVarPtg complex = new FuncVarPtg("COMPLEX", 2, 3);
        registerFunction(complex);

        FuncVarPtg convert = new FuncVarPtg("CONVERT", 3, 3);
        registerFunction(convert);

        FuncVarPtg dec2bin = new FuncVarPtg("DEC2BIN", 1, 2);
        registerFunction(dec2bin);

        FuncVarPtg dec2hex = new FuncVarPtg("DEC2HEX", 1, 2);
        registerFunction(dec2hex);

        FuncVarPtg dec2oct = new FuncVarPtg("dec2oct", 1, 2);
        registerFunction(dec2oct);

        FuncVarPtg delta = new FuncVarPtg("DELTA", 1, 2);
        registerFunction(delta);

        FuncVarPtg erf = new FuncVarPtg("ERF", 1, 2);
        registerFunction(erf);

        FuncVarPtg erfc = new FuncVarPtg("ERfC", 1, 1);
        registerFunction(erfc);

        FuncVarPtg gestep = new FuncVarPtg("GESTEP", 1, 2);
        registerFunction(gestep);

        FuncVarPtg hex2bin = new FuncVarPtg("HEX2BIN", 1, 2);
        registerFunction(hex2bin);

        FuncVarPtg hex2dec = new FuncVarPtg("HEX2DEC", 1, 1);
        registerFunction(hex2dec);

        FuncVarPtg hex2oct = new FuncVarPtg("HEX2OCT", 1, 2);
        registerFunction(hex2oct);

        FuncVarPtg imabs = new FuncVarPtg("IMABS", 1, 1);
        registerFunction(imabs);

        FuncVarPtg imaginary = new FuncVarPtg("IMAGINARY", 1, 1);
        registerFunction(imaginary);

        FuncVarPtg imargument = new FuncVarPtg("IMARGUMENT", 1, 1);
        registerFunction(imargument);

        FuncVarPtg imconjugate = new FuncVarPtg("IMCONJUGATE", 1, 1);
        registerFunction(imconjugate);

        FuncVarPtg imcos = new FuncVarPtg("IMCOS", 1, 1);
        registerFunction(imcos);

        FuncVarPtg imdiv = new FuncVarPtg("IMDIV", 2, 2);
        registerFunction(imdiv);

        FuncVarPtg imexp = new FuncVarPtg("IMEXP", 1, 1);
        registerFunction(imexp);

        FuncVarPtg imln = new FuncVarPtg("IMLN", 1, 1);
        registerFunction(imln);

        FuncVarPtg imlog10 = new FuncVarPtg("IMLOG10", 1, 1);
        registerFunction(imlog10);

        FuncVarPtg imlog2 = new FuncVarPtg("IMLOG2", 1, 1);
        registerFunction(imlog2);

        FuncVarPtg impower = new FuncVarPtg("IMPOWER", 2, 2);
        registerFunction(impower);

        FuncVarPtg improduct = new FuncVarPtg("IMPRODUCT", 1, 29);
        registerFunction(improduct);

        FuncVarPtg imreal = new FuncVarPtg("IMREAL", 1, 1);
        registerFunction(imreal);

        FuncVarPtg imsin = new FuncVarPtg("imsin", 1, 1);
        registerFunction(imsin);

        FuncVarPtg imsqrt = new FuncVarPtg("IMSQRT", 1, 1);
        registerFunction(imsqrt);

        FuncVarPtg imsub = new FuncVarPtg("IMSUB", 2, 2);
        registerFunction(imsub);

        FuncVarPtg imsum = new FuncVarPtg("IMSUM", 1, 29);
        registerFunction(imsum);

        FuncVarPtg oct2bin = new FuncVarPtg("OCT2BIN", 1, 2);
        registerFunction(oct2bin);

        FuncVarPtg oct2dec = new FuncVarPtg("OCT2DEC", 1, 1);
        registerFunction(oct2dec);

        FuncVarPtg oct2hex = new FuncVarPtg("OCT2HEX", 1, 2);
        registerFunction(oct2hex);

        //financial functions
        FuncVarPtg accrint = new FuncVarPtg("ACCRINT", 5, 7);
        registerFunction(accrint);

        FuncVarPtg accrintm = new FuncVarPtg("ACCRINTM", 3, 5);
        registerFunction(accrintm);

        FuncVarPtg amordegrc = new FuncVarPtg("AMORDEGRC", 6, 7);
        registerFunction(amordegrc);

        FuncVarPtg amorlinc = new FuncVarPtg("AMORLINC", 6, 7);
        registerFunction(amorlinc);

        FuncVarPtg coupdaybs = new FuncVarPtg("COUPDAYBS", 3, 4);
        registerFunction(coupdaybs);

        FuncVarPtg coupdays = new FuncVarPtg("COUPDAYS", 3, 4);
        registerFunction(coupdays);

        FuncVarPtg coupdaysnc = new FuncVarPtg("COUPDAYSNC", 3, 4);
        registerFunction(coupdaysnc);

        FuncVarPtg coupncd = new FuncVarPtg("COUPNCD", 3, 4);
        registerFunction(coupncd);

        FuncVarPtg coupnum = new FuncVarPtg("COUPNUM", 3, 4);
        registerFunction(coupnum);

        FuncVarPtg couppcd = new FuncVarPtg("COUPPCD", 3, 4);
        registerFunction(couppcd);

        FuncVarPtg cumipmt = new FuncVarPtg("CUMIPMT", 6, 6);
        registerFunction(cumipmt);

        FuncVarPtg cumprinc = new FuncVarPtg("CUMPRINC", 6, 6);
        registerFunction(cumprinc);

        FuncVarPtg db = new FuncVarPtg("DB", 4, 5);
        registerFunction(db);

        FuncVarPtg ddb = new FuncVarPtg("DDB", 4, 5);
        registerFunction(ddb);

        FuncVarPtg disc = new FuncVarPtg("DISC", 4, 5);
        registerFunction(disc);

        FuncVarPtg dollarde = new FuncVarPtg("DOLLARDE", 2, 2);
        registerFunction(dollarde);

        FuncVarPtg dollarfr = new FuncVarPtg("DOLLARFR", 2, 2);
        registerFunction(dollarfr);

        FuncVarPtg duration = new FuncVarPtg("DURATION", 5, 6);
        registerFunction(duration);

        FuncVarPtg effect = new FuncVarPtg("EFFECT", 2, 2);
        registerFunction(effect);

        FuncVarPtg fv = new FuncVarPtg("FV", 3, 5);
        registerFunction(fv);

        FuncVarPtg fvschedule = new FuncVarPtg("FVSCHEDULE", 2, 2);
        registerFunction(fvschedule);

        FuncVarPtg intrate = new FuncVarPtg("INTRATE", 4, 5);
        registerFunction(intrate);

        FuncVarPtg ipmt = new FuncVarPtg("IPMT", 4, 6);
        registerFunction(ipmt);

        FuncVarPtg irr = new FuncVarPtg("irr", 1, 2);
        registerFunction(irr);

        FuncVarPtg ispmt = new FuncVarPtg("ISPMT", 4, 4);
        registerFunction(ispmt);

        FuncVarPtg mduration = new FuncVarPtg("MDURATION", 5, 6);
        registerFunction(mduration);

        FuncVarPtg mirr = new FuncVarPtg("MIRR", 3, 3);
        registerFunction(mirr);

        FuncVarPtg nominal = new FuncVarPtg("NOMINAL", 2, 2);
        registerFunction(nominal);

        FuncVarPtg nper = new FuncVarPtg("NPER", 3, 5);
        registerFunction(nper);

        FuncVarPtg npv = new FuncVarPtg("NPV", 2, 29);
        registerFunction(npv);

        FuncVarPtg oddfprice = new FuncVarPtg("ODDFPRICE", 8, 9);
        registerFunction(oddfprice);

        FuncVarPtg oddfyield = new FuncVarPtg("ODDFYIELD", 8, 9);
        registerFunction(oddfyield);

        FuncVarPtg oddlprice = new FuncVarPtg("ODDLPRICE", 7, 8);
        registerFunction(oddlprice);

        FuncVarPtg oddlyield = new FuncVarPtg("ODDLYIELD", 8, 9);
        registerFunction(oddlyield);

        FuncVarPtg pmt = new FuncVarPtg("PMT", 3, 5);
        registerFunction(pmt);

        FuncVarPtg ppmt = new FuncVarPtg("PPMT", 4, 6);
        registerFunction(ppmt);

        FuncVarPtg price = new FuncVarPtg("PRICE", 6, 7);
        registerFunction(price);

        FuncVarPtg pricedisc = new FuncVarPtg("PRICEDISC", 4, 5);
        registerFunction(pricedisc);

        FuncVarPtg pricemat = new FuncVarPtg("PRICEMAT", 5, 6);
        registerFunction(pricemat);

        FuncVarPtg pv = new FuncVarPtg("PV", 3, 5);
        registerFunction(pv);

        FuncVarPtg rate = new FuncVarPtg("RATE", 3, 6);
        registerFunction(rate);

        FuncVarPtg received = new FuncVarPtg("RECEIVED", 4, 5);
        registerFunction(received);

        FuncVarPtg sln = new FuncVarPtg("SLN", 3, 3);
        registerFunction(sln);

        FuncVarPtg syd = new FuncVarPtg("SYD", 4, 4);
        registerFunction(syd);

        FuncVarPtg tbilleq = new FuncVarPtg("TBILLEQ", 3, 3);
        registerFunction(tbilleq);

        FuncVarPtg tbillprice = new FuncVarPtg("TBILLPRICE", 3, 3);
        registerFunction(tbillprice);

        FuncVarPtg tbillyield = new FuncVarPtg("TBILLYIELD", 3, 3);
        registerFunction(tbillyield);

        FuncVarPtg vdb = new FuncVarPtg("VDB", 5, 7);
        registerFunction(vdb);

        FuncVarPtg xirr = new FuncVarPtg("XIRR", 2, 3);
        registerFunction(xirr);

        FuncVarPtg xnpv = new FuncVarPtg("XNPV", 3, 3);
        registerFunction(xnpv);

        FuncVarPtg yield = new FuncVarPtg("YIELD", 6, 7);
        registerFunction(yield);

        FuncVarPtg yielddisc = new FuncVarPtg("YIELDDISC", 4, 5);
        registerFunction(yielddisc);

        FuncVarPtg yieldmat = new FuncVarPtg("YIELDMAT", 5, 6);
        registerFunction(yieldmat);


        //information functions
        FuncVarPtg cell = new FuncVarPtg("CELL", 1, 2);
        registerFunction(cell);

        FuncVarPtg error_type = new FuncVarPtg("ERROR.TYPE", 1, 1);
        registerFunction(error_type);

        FuncVarPtg info = new FuncVarPtg("INFO", 1, 1);
        registerFunction(info);

        FuncVarPtg isblank = new FuncVarPtg("ISBLANK", 1, 1);
        registerFunction(isblank);

        FuncVarPtg iserr = new FuncVarPtg("ISERR", 1, 1);
        registerFunction(iserr);

        FuncVarPtg iserror = new FuncVarPtg("ISERROR", 1, 1);
        registerFunction(iserror);

        FuncVarPtg islogical = new FuncVarPtg("islogical", 1, 1);
        registerFunction(islogical);

        FuncVarPtg isna = new FuncVarPtg("isna", 1, 1);
        registerFunction(isna);

        FuncVarPtg isnontext = new FuncVarPtg("ISNONTEXT", 1, 1);
        registerFunction(isnontext);

        FuncVarPtg isnumber = new FuncVarPtg("ISNUMBER", 1, 1);
        registerFunction(isnumber);

        FuncVarPtg isodd = new FuncVarPtg("ISODD", 1, 1);
        registerFunction(isodd);

        FuncVarPtg isref = new FuncVarPtg("ISREF", 1, 1);
        registerFunction(isref);

        FuncVarPtg istext = new FuncVarPtg("ISTEXT", 1, 1);
        registerFunction(istext);

        FuncVarPtg n = new FuncVarPtg("N", 1, 1);
        registerFunction(n);

        FuncVarPtg na = new FuncVarPtg("NA", 0, 0);
        registerFunction(na);

        FuncVarPtg type = new FuncVarPtg("TYPE", 1, 1);
        registerFunction(type);

        //logincal funcations
        FuncVarPtg and = new FuncVarPtg("AND", 1, 30);
        registerFunction(and);

        FuncVarPtg false_formula = new FuncVarPtg("FALSE", 0, 0);
        registerFunction(false_formula);

        FuncVarPtg if_formula = new FuncVarPtg("IF", 2, 3);
        registerFunction(if_formula);

        FuncVarPtg not_formula = new FuncVarPtg("NOT", 1, 1);
        registerFunction(not_formula);

        FuncVarPtg or_formula = new FuncVarPtg("OR", 1, 30);
        registerFunction(or_formula);

        FuncVarPtg true_formula = new FuncVarPtg("TRUE", 0, 0);
        registerFunction(true_formula);

        //lookup and reference functions
        FuncVarPtg address = new FuncVarPtg("ADDRESS", 2, 5);
        registerFunction(address);

        FuncVarPtg areas = new FuncVarPtg("AREAS", 1, 1);
        registerFunction(areas);

        FuncVarPtg choose = new FuncVarPtg("CHOOSE", 1, 29);
        registerFunction(choose);

        FuncVarPtg column = new FuncVarPtg("COLUMN", 1, 1);
        registerFunction(column);

        FuncVarPtg columns = new FuncVarPtg("COLUMNS", 1, 1);
        registerFunction(columns);

        FuncVarPtg getpivotdata = new FuncVarPtg("GETPIVOTDATA", 2, 30);
        registerFunction(getpivotdata);

        FuncVarPtg hlookup = new FuncVarPtg("HLOOKUP", 3, 4);
        registerFunction(hlookup);

        FuncVarPtg hyperlink = new FuncVarPtg("HYPERLINK", 1, 2);
        registerFunction(hyperlink);

        FuncVarPtg index = new FuncVarPtg("INDEX", 1, 3);
        registerFunction(index);

        FuncVarPtg indirect = new FuncVarPtg("INDIRECT", 1, 2);
        registerFunction(indirect);

        FuncVarPtg lookup = new FuncVarPtg("LOOKUP", 3, 3);
        registerFunction(lookup);

        FuncVarPtg match = new FuncVarPtg("MATCH", 2, 3);
        registerFunction(match);

        FuncVarPtg offset = new FuncVarPtg("OFFSET", 3, 5);
        registerFunction(offset);

        FuncVarPtg row = new FuncVarPtg("ROW", 1, 1);
        registerFunction(row);

        FuncVarPtg rows = new FuncVarPtg("ROWS", 1, 1);
        registerFunction(rows);

        FuncVarPtg rtd = new FuncVarPtg("RTD", 1, 30);
        registerFunction(rtd);

        FuncVarPtg transpose = new FuncVarPtg("TRANSPOSE", 1, 1);
        registerFunction(transpose);

        FuncVarPtg vlookup = new FuncVarPtg("VLOOKUP", 3, 4);
        registerFunction(vlookup);

        //math and trigonometry functions
        FuncVarPtg abs = new FuncVarPtg("ABS", 1, 1);
        registerFunction(abs);

        FuncVarPtg acos = new FuncVarPtg("ACOS", 1, 1);
        registerFunction(acos);

        FuncVarPtg acosh = new FuncVarPtg("ACOSH", 1, 1);
        registerFunction(acosh);

        FuncVarPtg asin = new FuncVarPtg("ASIN", 1, 1);
        registerFunction(asin);

        FuncVarPtg asinh = new FuncVarPtg("ASINH", 1, 1);
        registerFunction(asinh);

        FuncVarPtg atan = new FuncVarPtg("ATAN", 1, 1);
        registerFunction(atan);

        FuncVarPtg atan2 = new FuncVarPtg("ATAN2", 2, 2);
        registerFunction(atan2);

        FuncVarPtg atanh = new FuncVarPtg("ATANH", 1, 1);
        registerFunction(atanh);

        FuncVarPtg ceiling = new FuncVarPtg("CEILING", 2, 2);
        registerFunction(ceiling);

        FuncVarPtg combin = new FuncVarPtg("COMBIN", 2, 2);
        registerFunction(combin);

        FuncVarPtg cos = new FuncVarPtg("COS", 1, 1);
        registerFunction(cos);

        FuncVarPtg cosh = new FuncVarPtg("COSH", 1, 1);
        registerFunction(cosh);

        FuncVarPtg degrees = new FuncVarPtg("DEGREES", 1, 1);
        registerFunction(degrees);

        FuncVarPtg even = new FuncVarPtg("EVEN", 1, 1);
        registerFunction(even);

        FuncVarPtg exp = new FuncVarPtg("EXP", 1, 1);
        registerFunction(exp);

        FuncVarPtg fact = new FuncVarPtg("FACT", 1, 1);
        registerFunction(fact);

        FuncVarPtg factdouble = new FuncVarPtg("FACTDOUBLE", 1, 1);
        registerFunction(factdouble);

        FuncVarPtg floor = new FuncVarPtg("FLOOR", 2, 2);
        registerFunction(floor);

        FuncVarPtg int_formula = new FuncVarPtg("INT", 1, 1);
        registerFunction(int_formula);

        FuncVarPtg gcd = new FuncVarPtg("GCD", 1, 29);
        registerFunction(gcd);

        FuncVarPtg lcm = new FuncVarPtg("LCM", 1, 29);
        registerFunction(lcm);

        FuncVarPtg ln = new FuncVarPtg("LN", 1, 1);
        registerFunction(ln);

        FuncVarPtg log = new FuncVarPtg("LOG", 1, 2);
        registerFunction(log);

        FuncVarPtg log10 = new FuncVarPtg("LOG10", 1, 1);
        registerFunction(log10);

        FuncVarPtg mdeterm = new FuncVarPtg("MDETERM", 1, 1);
        registerFunction(mdeterm);

        FuncVarPtg minverse = new FuncVarPtg("MINVERSE", 1, 1);
        registerFunction(minverse);

        FuncVarPtg mmult = new FuncVarPtg("MMULT", 2, 2);
        registerFunction(mmult);

        FuncVarPtg mod = new FuncVarPtg("MOD", 2, 2);
        registerFunction(mod);

        FuncVarPtg mround = new FuncVarPtg("MROUND", 2, 2);
        registerFunction(mround);

        FuncVarPtg multinomial = new FuncVarPtg("MULTINOMIAL", 1, 29);
        registerFunction(multinomial);

        FuncVarPtg odd = new FuncVarPtg("ODD", 1, 1);
        registerFunction(odd);

        FuncVarPtg pi = new FuncVarPtg("PI", 0, 0);
        registerFunction(pi);

        FuncVarPtg power = new FuncVarPtg("POWER", 2, 2);
        registerFunction(power);

        FuncVarPtg product = new FuncVarPtg("PRODUCT", 1, 30);
        registerFunction(product);

        FuncVarPtg quotient = new FuncVarPtg("QUOTIENT", 2, 2);
        registerFunction(quotient);

        FuncVarPtg radians = new FuncVarPtg("RADIANS", 1, 1);
        registerFunction(radians);

        FuncVarPtg rand = new FuncVarPtg("RAND", 0, 0);
        registerFunction(rand);

        FuncVarPtg randbetween = new FuncVarPtg("RANDBETWEEN", 2, 2);
        registerFunction(randbetween);

        FuncVarPtg roman = new FuncVarPtg("ROMAN", 1, 2);
        registerFunction(roman);

        FuncVarPtg round = new FuncVarPtg("ROUND", 2, 2);
        registerFunction(round);

        FuncVarPtg rounddown = new FuncVarPtg("ROUNDDOWN", 2, 2);
        registerFunction(rounddown);

        FuncVarPtg roundup = new FuncVarPtg("ROUNDUP", 2, 2);
        registerFunction(roundup);

        FuncVarPtg seriessum = new FuncVarPtg("SERIESSUM", 4, 4);
        registerFunction(seriessum);

        FuncVarPtg sign = new FuncVarPtg("SIGN", 1, 1);
        registerFunction(sign);

        FuncVarPtg sin = new FuncVarPtg("SIN", 1, 1);
        registerFunction(sin);

        FuncVarPtg sinh = new FuncVarPtg("SINH", 1, 1);
        registerFunction(sinh);

        FuncVarPtg sqrt = new FuncVarPtg("SQRT", 1, 1);
        registerFunction(sqrt);

        FuncVarPtg sqrtpi = new FuncVarPtg("SQRTPI", 1, 1);
        registerFunction(sqrtpi);

        FuncVarPtg subtotal = new FuncVarPtg("SUBTOTAL", 2, 30);
        registerFunction(subtotal);

        FuncVarPtg sum = new FuncVarPtg("SUM", 1, 30);
        registerFunction(sum);

        FuncVarPtg sumif = new FuncVarPtg("SUMIF", 2, 3);
        registerFunction(sumif);

        FuncVarPtg sumproduct = new FuncVarPtg("SUMPRODUCT", 1, 30);
        registerFunction(sumproduct);

        FuncVarPtg sumsq = new FuncVarPtg("SUMSQ", 1, 30);
        registerFunction(sumsq);

        FuncVarPtg sumx2my2 = new FuncVarPtg("SUMX2MY2", 2, 2);
        registerFunction(sumx2my2);

        FuncVarPtg sumx2py2 = new FuncVarPtg("SUMX2PY2", 2, 2);
        registerFunction(sumx2py2);

        FuncVarPtg sumxmy2 = new FuncVarPtg("SUMXMY2", 2, 2);
        registerFunction(sumxmy2);

        FuncVarPtg tan = new FuncVarPtg("TAN", 1, 1);
        registerFunction(tan);

        FuncVarPtg tanh = new FuncVarPtg("TANH", 1, 1);
        registerFunction(tanh);

        FuncVarPtg trunc = new FuncVarPtg("TRUNC", 1, 2);
        registerFunction(trunc);

        //Statistical functions
        FuncVarPtg avedev = new FuncVarPtg("AVEDEV", 1, 30);
        registerFunction(avedev);

        FuncVarPtg average = new FuncVarPtg("AVERAGE", 1, 30);
        registerFunction(average);

        FuncVarPtg averagea = new FuncVarPtg("AVERAGEA", 1, 30);
        registerFunction(averagea);

        FuncVarPtg betadist = new FuncVarPtg("BETADIST", 3, 5);
        registerFunction(betadist);

        FuncVarPtg betainv = new FuncVarPtg("BETAINV", 3, 5);
        registerFunction(betainv);

        FuncVarPtg binomdist = new FuncVarPtg("BINOMDIST", 4, 4);
        registerFunction(binomdist);

        FuncVarPtg chidist = new FuncVarPtg("CHIDIST", 2, 2);
        registerFunction(chidist);

        FuncVarPtg chiinv = new FuncVarPtg("CHIINV", 2, 2);
        registerFunction(chiinv);

        FuncVarPtg chitest = new FuncVarPtg("CHITEST", 2, 2);
        registerFunction(chitest);

        FuncVarPtg confidence = new FuncVarPtg("CONFIDENCE", 3, 3);
        registerFunction(confidence);

        FuncVarPtg correl = new FuncVarPtg("CORREL", 2, 2);
        registerFunction(correl);

        FuncVarPtg count = new FuncVarPtg("COUNT", 1, 30);
        registerFunction(count);

        FuncVarPtg counta = new FuncVarPtg("COUNTA", 1, 30);
        registerFunction(counta);

        FuncVarPtg countblank = new FuncVarPtg("COUNTBLANK", 1, 1);
        registerFunction(countblank);

        FuncVarPtg countif = new FuncVarPtg("COUNTIF", 2, 2);
        registerFunction(countif);

        FuncVarPtg covar = new FuncVarPtg("COVAR", 2, 2);
        registerFunction(covar);

        FuncVarPtg critbinom = new FuncVarPtg("CRITBINOM", 3, 3);
        registerFunction(critbinom);

        FuncVarPtg devsq = new FuncVarPtg("DEVSQ", 1, 30);
        registerFunction(devsq);

        FuncVarPtg expondist = new FuncVarPtg("EXPONDIST", 3, 3);
        registerFunction(expondist);

        FuncVarPtg fdist = new FuncVarPtg("FDIST", 3, 3);
        registerFunction(fdist);

        FuncVarPtg finv = new FuncVarPtg("FINV", 3, 3);
        registerFunction(finv);

        FuncVarPtg fisher = new FuncVarPtg("FISHER", 1, 1);
        registerFunction(fisher);

        FuncVarPtg fisherinv = new FuncVarPtg("FISHERINV", 1, 1);
        registerFunction(fisherinv);

        FuncVarPtg forecast = new FuncVarPtg("FORECAST", 3, 3);
        registerFunction(forecast);

        FuncVarPtg frequency = new FuncVarPtg("FREQUENCY", 2, 2);
        registerFunction(frequency);

        FuncVarPtg ftest = new FuncVarPtg("FTEST", 2, 2);
        registerFunction(ftest);

        FuncVarPtg gammadist = new FuncVarPtg("GAMMADIST", 4, 4);
        registerFunction(gammadist);

        FuncVarPtg gammainv = new FuncVarPtg("GAMMAINV", 3, 3);
        registerFunction(gammainv);

        FuncVarPtg gammaln = new FuncVarPtg("GAMMALN", 1, 1);
        registerFunction(gammaln);

        FuncVarPtg geomean = new FuncVarPtg("GEOMEAN", 1, 30);
        registerFunction(geomean);

        FuncVarPtg growth = new FuncVarPtg("GROWTH", 1, 4);
        registerFunction(growth);

        FuncVarPtg harmean = new FuncVarPtg("HARMEAN", 1, 30);
        registerFunction(harmean);

        FuncVarPtg hypgeomdist = new FuncVarPtg("HYPGEOMDIST", 4, 4);
        registerFunction(hypgeomdist);

        FuncVarPtg intercept = new FuncVarPtg("INTERCEPT", 2, 2);
        registerFunction(intercept);

        FuncVarPtg kurt = new FuncVarPtg("KURT", 1, 30);
        registerFunction(kurt);

        FuncVarPtg large = new FuncVarPtg("LARGE", 2, 2);
        registerFunction(large);

        FuncVarPtg linest = new FuncVarPtg("LINEST", 1, 4);
        registerFunction(linest);

        FuncVarPtg logest = new FuncVarPtg("LOGEST", 1, 4);
        registerFunction(logest);

        FuncVarPtg loginv = new FuncVarPtg("LOGINV", 3, 3);
        registerFunction(loginv);

        FuncVarPtg lognormdist = new FuncVarPtg("LOGNORMDIST", 2, 3);
        registerFunction(lognormdist);

        FuncVarPtg max = new FuncVarPtg("MAX", 1, 30);
        registerFunction(max);

        FuncVarPtg maxa = new FuncVarPtg("MAXA", 1, 30);
        registerFunction(maxa);

        FuncVarPtg median = new FuncVarPtg("MEDIAN", 1, 30);
        registerFunction(median);

        FuncVarPtg min = new FuncVarPtg("MIN", 1, 30);
        registerFunction(min);

        FuncVarPtg mina = new FuncVarPtg("MINA", 1, 30);
        registerFunction(mina);

        FuncVarPtg mode = new FuncVarPtg("MODE", 1, 30);
        registerFunction(mode);

        FuncVarPtg negbinomdist = new FuncVarPtg("NEGBINOMDIST", 3, 3);
        registerFunction(negbinomdist);

        FuncVarPtg normdist = new FuncVarPtg("NORMDIST", 4, 4);
        registerFunction(normdist);

        FuncVarPtg norminv = new FuncVarPtg("NORMINV", 3, 3);
        registerFunction(norminv);

        FuncVarPtg normsdist = new FuncVarPtg("NORMSDIST", 1, 1);
        registerFunction(normsdist);

        FuncVarPtg normsinv = new FuncVarPtg("NORMSINV", 1, 1);
        registerFunction(normsinv);

        FuncVarPtg pearson = new FuncVarPtg("PEARSON", 2, 2);
        registerFunction(pearson);

        FuncVarPtg percentile = new FuncVarPtg("PERCENTILE", 2, 2);
        registerFunction(percentile);

        FuncVarPtg percentrank = new FuncVarPtg("PERCENTRANK", 2, 3);
        registerFunction(percentrank);

        FuncVarPtg permut = new FuncVarPtg("PERMUT", 2, 2);
        registerFunction(permut);

        FuncVarPtg poisson = new FuncVarPtg("POISSON", 3, 3);
        registerFunction(poisson);

        FuncVarPtg prob = new FuncVarPtg("PROB", 3, 4);
        registerFunction(prob);

        FuncVarPtg quartile = new FuncVarPtg("QUARTILE", 2, 2);
        registerFunction(quartile);

        FuncVarPtg rank = new FuncVarPtg("RANK", 2, 3);
        registerFunction(rank);

        FuncVarPtg rsq = new FuncVarPtg("RSQ", 2, 2);
        registerFunction(rsq);

        FuncVarPtg skew = new FuncVarPtg("SKEW", 1, 30);
        registerFunction(skew);

        FuncVarPtg slope = new FuncVarPtg("SLOPE", 2, 2);
        registerFunction(slope);

        FuncVarPtg small = new FuncVarPtg("SMALL", 2, 2);
        registerFunction(small);

        FuncVarPtg standardize = new FuncVarPtg("STANDARDIZE", 3, 3);
        registerFunction(standardize);

        FuncVarPtg stdev = new FuncVarPtg("STDEV", 1, 30);
        registerFunction(stdev);

        FuncVarPtg stdeva = new FuncVarPtg("STDEVA", 1, 30);
        registerFunction(stdeva);


        FuncVarPtg stdevp = new FuncVarPtg("STDEVP", 1, 30);
        registerFunction(stdevp);

        FuncVarPtg stdevpa = new FuncVarPtg("STDEVPA", 1, 30);
        registerFunction(stdevpa);

        FuncVarPtg steyx = new FuncVarPtg("STEYX", 2, 2);
        registerFunction(steyx);

        FuncVarPtg tdist = new FuncVarPtg("TDIST", 3, 3);
        registerFunction(tdist);

        FuncVarPtg tinv = new FuncVarPtg("TINV", 2, 2);
        registerFunction(tinv);

        FuncVarPtg trend = new FuncVarPtg("TREND", 1, 4);
        registerFunction(trend);

        FuncVarPtg trimmean = new FuncVarPtg("TRIMMEAN", 2, 2);
        registerFunction(trimmean);

        FuncVarPtg ttest = new FuncVarPtg("TTEST", 4, 4);
        registerFunction(ttest);

        FuncVarPtg var = new FuncVarPtg("VAR", 1, 30);
        registerFunction(var);

        FuncVarPtg vara = new FuncVarPtg("VARA", 1, 30);
        registerFunction(vara);

        FuncVarPtg varp = new FuncVarPtg("VARP", 1, 30);
        registerFunction(varp);

        FuncVarPtg varpa = new FuncVarPtg("VARPA", 1, 30);
        registerFunction(varpa);

        FuncVarPtg weibull = new FuncVarPtg("WEIBULL", 4, 4);
        registerFunction(weibull);

        FuncVarPtg ztest = new FuncVarPtg("ZTEST", 2, 3);
        registerFunction(ztest);

        //text functions
        FuncVarPtg asc = new FuncVarPtg("ASC", 1, 1);
        registerFunction(asc);

        FuncVarPtg bahttext = new FuncVarPtg("BAHTTEXT", 1, 1);
        registerFunction(bahttext);

        FuncVarPtg char_formula = new FuncVarPtg("CHAR", 1, 1);
        registerFunction(char_formula);

        FuncVarPtg clean = new FuncVarPtg("CLEAN", 1, 1);
        registerFunction(clean);

        FuncVarPtg code = new FuncVarPtg("CODE", 1, 1);
        registerFunction(code);

        FuncVarPtg concatenate = new FuncVarPtg("CONCATENATE", 1, 30);
        registerFunction(concatenate);

        FuncVarPtg dollar = new FuncVarPtg("DOLLAR", 1, 2);
        registerFunction(dollar);

        FuncVarPtg exact = new FuncVarPtg("EXACT", 2, 2);
        registerFunction(exact);

        FuncVarPtg find = new FuncVarPtg("FIND", 2, 3);
        registerFunction(find);

        FuncVarPtg findb = new FuncVarPtg("FINDB", 2, 3);
        registerFunction(findb);

        FuncVarPtg fixed = new FuncVarPtg("FIXED", 1, 3);
        registerFunction(fixed);

        FuncVarPtg jis = new FuncVarPtg("JIS", 1, 1);
        registerFunction(jis);

        FuncVarPtg left = new FuncVarPtg("LEFT", 1, 2);
        registerFunction(left);

        FuncVarPtg leftb = new FuncVarPtg("LEFTB", 1, 2);
        registerFunction(leftb);

        FuncVarPtg len = new FuncVarPtg("LEN", 1, 1);
        registerFunction(len);

        FuncVarPtg lenb = new FuncVarPtg("LENB", 1, 1);
        registerFunction(lenb);

        FuncVarPtg lower = new FuncVarPtg("LOWER", 1, 1);
        registerFunction(lower);

        FuncVarPtg mid = new FuncVarPtg("MID", 3, 3);
        registerFunction(mid);

        FuncVarPtg midb = new FuncVarPtg("MIDB", 3, 3);
        registerFunction(midb);

        FuncVarPtg phonetic = new FuncVarPtg("PHONETIC", 1, 1);
        registerFunction(phonetic);

        FuncVarPtg proper = new FuncVarPtg("PROPER", 1, 1);
        registerFunction(proper);

        FuncVarPtg replace = new FuncVarPtg("REPLACE", 4, 4);
        registerFunction(replace);

        FuncVarPtg replaceb = new FuncVarPtg("REPLACEB", 4, 4);
        registerFunction(replaceb);

        FuncVarPtg rept = new FuncVarPtg("REPT", 2, 2);
        registerFunction(rept);

        FuncVarPtg right = new FuncVarPtg("RIGHT", 1, 2);
        registerFunction(right);

        FuncVarPtg rightb = new FuncVarPtg("RIGHTB", 1, 2);
        registerFunction(rightb);

        FuncVarPtg search = new FuncVarPtg("SEARCH", 2, 3);
        registerFunction(search);

        FuncVarPtg searchb = new FuncVarPtg("SEARCHB", 2, 3);
        registerFunction(searchb);

        FuncVarPtg substitute = new FuncVarPtg("SUBSTITUTE", 3, 4);
        registerFunction(substitute);

        FuncVarPtg t = new FuncVarPtg("T", 1, 1);
        registerFunction(t);

        FuncVarPtg text = new FuncVarPtg("TEXT", 2, 2);
        registerFunction(text);

        FuncVarPtg trim = new FuncVarPtg("TRIM", 1, 1);
        registerFunction(trim);

        FuncVarPtg upper = new FuncVarPtg("UPPER", 1, 1);
        registerFunction(upper);

        FuncVarPtg value = new FuncVarPtg("VALUE", 1, 1);
        registerFunction(value);


    }


    public static FuncVarPtg getFunctionByName(String name) {
        return _functionPtgByName.get(name.toLowerCase());
    }
}
