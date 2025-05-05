package com.example.RestService.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.LongStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.RestService.common.exception.InternalServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class AppUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);
    
    public static String generatePinCode(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static String generatePinCode() {
        return generatePinCode(6);
    }

    public static String generateActivationCode() { 
        return generatePinCode(6);
    }

    public static String convertToJson(Object o) { 
        try { 
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException var1) { 
            LOGGER.error("ConvertToJson() message: {}", var1.getMessage());
            LOGGER.error("", var1);
            throw new InternalServerErrorException(var1.getMessage());
        }
    }

    public static <T> T getObjectToJson(String json, Class<T> tClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(json, tClass);
        } catch (JsonProcessingException var1) {
            LOGGER.error("getObjectToJson() message: {}", var1.getMessage());
            LOGGER.error("", var1);
            throw new InternalServerErrorException(var1.getMessage());
        }
    }

    public static String updatePhoneNumberFormat(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.startsWith("959")) { 
            return phoneNumber;
        }
        if (phoneNumber != null && phoneNumber.startsWith("09")) {
            return phoneNumber.replaceFirst("09",  "959");
        }
        throw new InternalServerErrorException("Invalid Phone number");
    }

    public static String dateFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if (date != null) {
            return simpleDateFormat.format(date);
        }

        return simpleDateFormat.format(new Date());
    }

    public static String dateFormat(Date date) {
        return dateFormat(date, "yyyy-MM-dd");
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        List<DateTimeFormatter> formatters = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),  
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        );

        for (DateTimeFormatter formatter : formatters) {
            try{
                return LocalDateTime.parse(dateTimeString, formatter);

            } catch (DateTimeParseException ignored) {

            }
        }

        return null;
        
    }

    public static LocalDate parseDate(String dateString) { 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date time: " + e.getMessage());
            return null;
        }
    }

    public static Date dateFormat(String date) throws ParseException { 
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(date);
    }

    public static <T, R> R getOrDefault(T object, Function<T, R> propertyExtractor, R defaultValue) {
        return Optional.ofNullable(object) 
                .map(propertyExtractor)
                .orElse(defaultValue);
    }

    public static BigDecimal convertToBigDecimal(Object value) { 
        if (value instanceof BigDecimal) { 
            return (BigDecimal) value;
        } else if ( value instanceof Integer ) {
            return BigDecimal.valueOf((Integer) value);
        } else if ( value instanceof Long ) { 
            return BigDecimal.valueOf((Long) value);
        } else if ( value != null ) {
            return new BigDecimal(value.toString());
        } else { 
            return BigDecimal.ZERO;
        }
    }

    public static List<Long> convertToArray(String arrStr) { 
        if (arrStr == null || arrStr.isEmpty()) { 
            return List.of();
        } 
        long[] arr = Arrays.stream(arrStr.split(","))
                .mapToLong(Long::parseLong)
                .toArray();

        Long[] boxedArray = LongStream.of(arr).boxed().toArray(Long[]::new);
        return Arrays.asList(boxedArray);
        
    }
}